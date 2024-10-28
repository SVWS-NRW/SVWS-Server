import type { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";
import type { RouteApp } from "~/router/apps/RouteApp";
import type { TabData } from "@ui";
import type { StundenplanAuswahlProps } from "~/components/stundenplan/SStundenplanAuswahlProps";
import type { StundenplanAppProps } from "~/components/stundenplan/SStundenplanAppProps";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode, StundenplanKonfiguration } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

import { routeApp } from "~/router/apps/RouteApp";

import { routeStundenplanDaten } from "~/router/apps/stundenplan/RouteStundenplanDaten";
import { routeStundenplanKalenderwochen } from "./RouteStundenplanKalenderwochen";
import { routeStundenplanPausen } from "~/router/apps/stundenplan/RouteStundenplanPausen";
import { routeStundenplanZeitrasterPausenzeit } from "./RouteStundenplanZeitrasterPausenzeit";
import { routeStundenplanKlasse } from "~/router/apps/stundenplan/RouteStundenplanKlasse";
import { routeStundenplanUnterrichte } from "./RouteStundenplanUnterrichte";
import { RouteDataStundenplan } from "~/router/apps/stundenplan/RouteDataStundenplan";
import { routeError } from "~/router/error/RouteError";
import { routeStundenplanKataloge } from "./RouteStundenplanKataloge";
import { routeStundenplanRaum } from "./RouteStundenplanRaum";
import { ConfigElement } from "~/components/Config";

const SStundenplanAuswahl = () => import("~/components/stundenplan/SStundenplanAuswahl.vue")
const SStundenplanApp = () => import("~/components/stundenplan/SStundenplanApp.vue")

export class RouteStundenplan extends RouteNode<RouteDataStundenplan, RouteApp> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN,
			BenutzerKompetenz.STUNDENPLAN_FUNKTIONSBEZOGEN_ANSEHEN,
		], "stundenplan", "stundenplan/:id(\\d+)?", SStundenplanApp, new RouteDataStundenplan());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stundenplan";
		super.setView("liste", SStundenplanAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeStundenplanDaten,
			routeStundenplanKalenderwochen,
			routeStundenplanPausen,
			routeStundenplanZeitrasterPausenzeit,
			routeStundenplanKlasse,
			routeStundenplanUnterrichte,
			routeStundenplanRaum,
		];
		super.defaultChild = routeStundenplanDaten;
		const stundenplanConfig = new StundenplanKonfiguration();
		api.config.addElements([
			new ConfigElement("stundenplan.settings.defaults", "user", StundenplanKonfiguration.transpilerToJSON(stundenplanConfig)),
		]);
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { idSchuljahresabschnitt, id } = RouteNode.getIntParams(to_params, ["idSchuljahresabschnitt", "id"]);
			if (idSchuljahresabschnitt === undefined)
				throw new DeveloperNotificationException("Beim Aufruf der Route ist kein gültiger Schuljahresabschnitt gesetzt.");
			await this.data.setSchuljahresabschnitt(idSchuljahresabschnitt);
			if (id === -1)
				return routeStundenplanKataloge.getRouteDefaultChild();
			if (id !== undefined) {
				const eintrag = this.data.mapKatalogeintraege.get(id);
				if ((eintrag === undefined) && (this.data.auswahl !== undefined))
					return this.getRoute();
				await this.data.setEintrag(eintrag);
				if ((this.data.auswahl !== undefined) && (this.data.auswahl.id === -1))
					return routeStundenplanKataloge.getRouteDefaultChild();
			}
			if (to.name === this.name) {
				if (this.data.auswahl === undefined)
					return;
				return this.getRouteDefaultChild();
			}
			if (!to.name.startsWith(this.data.view.name))
				for (const child of this.children)
					if (to.name.startsWith(child.name))
						this.data.setView(child, this.children);
		} catch (e) {
			return routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		this.data.reset();
	}

	public addRouteParamsFromState() : RouteParamsRawGeneric {
		return { id : this.data.auswahl?.id ?? undefined };
	}

	public getAuswahlProps(to: RouteLocationNormalized): StundenplanAuswahlProps {
		return {
			serverMode: api.mode,
			auswahl: this.data.auswahl,
			mapKatalogeintraege: () => this.data.mapKatalogeintraege,
			gotoEintrag: this.data.gotoEintrag,
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(true),
			addEintrag: this.data.addEintrag,
			removeEintraege: this.data.removeEintraege,
		};
	}

	public getProps(to: RouteLocationNormalized): StundenplanAppProps {
		return {
			auswahl: this.data.auswahl,
			tabManager: () => this.createTabManagerByChildren(this.data.view.name, this.setTab),
		};
	}

	private setTab = async (value: TabData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new DeveloperNotificationException("Unbekannte Route");
		await RouteManager.doRoute(node.getRoute());
		this.data.setView(node, routeStundenplan.children);
	}

}

export const routeStundenplan = new RouteStundenplan();
