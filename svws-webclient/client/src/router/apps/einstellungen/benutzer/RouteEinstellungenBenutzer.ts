import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

import { routeApp, type RouteApp } from "~/router/apps/RouteApp";
import { routeEinstellungen } from "~/router/apps/einstellungen/RouteEinstellungen";
import { routeEinstellungenBenutzerDaten } from "~/router/apps/einstellungen/benutzer/RouteEinstellungenBenutzerDaten";
import { RouteDataEinstellungenBenutzer } from "~/router/apps/einstellungen/benutzer/RouteDataEinstellungenBenutzer";

import type { TabData } from "@ui";
import type { BenutzerAppProps } from "~/components/einstellungen/benutzer/SBenutzerAppProps";
import type { BenutzerAuswahlProps } from "~/components/einstellungen/benutzer/SBenutzerAuswahlProps";
import { RouteEinstellungenMenuGroup } from "../RouteEinstellungenMenuGroup";
import { routeError } from "~/router/error/RouteError";

const SBenutzerAuswahl = () => import("~/components/einstellungen/benutzer/SBenutzerAuswahl.vue");
const SBenutzerApp = () => import("~/components/einstellungen/benutzer/SBenutzerApp.vue");

export class RouteEinstellungenBenutzer extends RouteNode<RouteDataEinstellungenBenutzer, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.ADMIN], "einstellungen.benutzer", "einstellungen/benutzer/:id(\\d+)?", SBenutzerApp, new RouteDataEinstellungenBenutzer());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Benutzer";
		super.menugroup = RouteEinstellungenMenuGroup.BENUTZERVERWALTUNG;
		super.setView("liste", SBenutzerAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeEinstellungenBenutzerDaten,
		];
		super.defaultChild = routeEinstellungenBenutzerDaten;
	}

	public async beforeEach(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams) : Promise<boolean | void | Error | RouteLocationRaw> {
		try {
			const { id } = RouteNode.getIntParams(to_params, ["id"]);
			if (id !== undefined)
				return routeEinstellungenBenutzer.getRoute({ id });
			return true;
		} catch(e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { id } = RouteNode.getIntParams(to_params, ["id"]);
			await this.data.ladeListe();
			if (to.name === this.name) {
				if (this.data.mapBenutzer.size === 0) return;
				const [next] = this.data.mapBenutzer.values();
				return this.getRouteDefaultChild({ id: next.id });
			}
			const eintrag = (id !== undefined) ? this.data.mapBenutzer.get(id) : undefined;
			// Weiterleitung an das erste Objekt in der Liste, wenn id nicht vorhanden ist.
			if (eintrag === undefined) {
				const [next] = this.data.mapBenutzer.values();
				return routeEinstellungenBenutzerDaten.getRoute({ id: next.id });
			}
			await this.data.setBenutzer(eintrag);
		} catch(e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}

	}

	public getAuswahlProps(to: RouteLocationNormalized): BenutzerAuswahlProps {
		return {
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(false),
			auswahl: () => this.data.auswahl,
			mapBenutzer: this.data.mapBenutzer,
			gotoBenutzer: this.data.gotoBenutzer,
			createBenutzerAllgemein: this.data.createBenutzerAllgemein,
			deleteBenutzerMenge: this.data.deleteBenutzerMenge,
		};
	}

	public getProps(to: RouteLocationNormalized): BenutzerAppProps {
		return {
			auswahl: () => this.data.auswahl,
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
		this.data.setView(node, routeEinstellungen.children);
	};

}

export const routeEinstellungenBenutzer = new RouteEinstellungenBenutzer();
