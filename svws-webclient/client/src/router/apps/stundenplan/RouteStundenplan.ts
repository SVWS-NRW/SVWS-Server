import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { RouteApp } from "~/router/apps/RouteApp";
import type { AuswahlChildData } from "~/components/AuswahlChildData";
import type { StundenplanAuswahlProps } from "~/components/stundenplan/SStundenplanAuswahlProps";
import type { StundenplanAppProps } from "~/components/stundenplan/SStundenplanAppProps";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

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
		];
		super.defaultChild = routeStundenplanDaten;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		const idSchuljahresabschnitt = RouteNode.getIntParam(to_params, "idSchuljahresabschnitt");
		if (idSchuljahresabschnitt instanceof Error)
			return routeError.getRoute(idSchuljahresabschnitt);
		if (idSchuljahresabschnitt === undefined)
			return routeError.getRoute(new DeveloperNotificationException("Beim Aufruf der Route ist kein gültiger Schuljahresabschnitt gesetzt."));

		await this.data.setSchuljahresabschnitt(idSchuljahresabschnitt);
		if (to_params.id instanceof Array)
			throw new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (to_params.id !== undefined) {
			const id = parseInt(to_params.id);
			const eintrag = this.data.mapKatalogeintraege.get(id);
			if ((eintrag === undefined) && (this.data.auswahl !== undefined))
				return this.getRoute(undefined);
			await this.data.setEintrag(eintrag);
		}
		if (to.name === this.name) {
			if (this.data.auswahl === undefined)
				return;
			return this.getRoute(this.data.auswahl.id);
		}
		if (!to.name.startsWith(this.data.view.name))
			for (const child of this.children)
				if (to.name.startsWith(child.name))
					this.data.setView(child, this.children);
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		this.data.reset();
	}

	public getRoute(id?: number) : RouteLocationRaw {
		if (id === undefined)
			return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt }};
		return { name: this.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getChildRoute(id: number | undefined) : RouteLocationRaw {
		const redirect_name = (routeStundenplan.selectedChild === undefined) ? routeStundenplanDaten.name : routeStundenplan.selectedChild.name;
		return { name: redirect_name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): StundenplanAuswahlProps {
		return {
			serverMode: api.mode,
			auswahl: this.data.auswahl,
			mapKatalogeintraege: () =>this.data.mapKatalogeintraege,
			gotoEintrag: this.data.gotoEintrag,
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(true),
			addEintrag: this.data.addEintrag,
			removeEintraege: this.data.removeEintraege,
		};
	}

	public getProps(to: RouteLocationNormalized): StundenplanAppProps {
		return {
			auswahl: this.data.auswahl,
			setTab: this.setTab,
			tab: this.getTab(),
			tabs: this.getTabs(),
			tabsHidden: this.children_hidden().value,
		};
	}

	private getTab(): AuswahlChildData {
		return { name: this.data.view.name, text: this.data.view.text };
	}

	private getTabs(): AuswahlChildData[] {
		const result: AuswahlChildData[] = [];
		for (const c of this.children)
			if (c.hatEineKompetenz() && c.hatSchulform())
				result.push({ name: c.name, text: c.text });
		return result;
	}

	private setTab = async (value: AuswahlChildData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new DeveloperNotificationException("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: this.data.auswahl?.id } });
		this.data.setView(node, routeStundenplan.children);
	}
}

export const routeStundenplan = new RouteStundenplan();
