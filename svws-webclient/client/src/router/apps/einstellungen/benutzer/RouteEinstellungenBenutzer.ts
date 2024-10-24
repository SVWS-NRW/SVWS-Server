import type { WritableComputedRef } from "vue";
import { computed } from "vue";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteRecordRaw } from "vue-router";

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

const SEinstellungenAuswahl = () => import("~/components/einstellungen/SEinstellungenAuswahl.vue")
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
		super.setView("submenu", SEinstellungenAuswahl, (route) => routeEinstellungen.getAuswahlProps(route));
		super.children = [routeEinstellungenBenutzerDaten];
		super.defaultChild = routeEinstellungenBenutzerDaten;
	}

	public async beforeEach(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams) : Promise<boolean | void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array)
			throw new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const id = !to_params.id ? undefined : parseInt(to_params.id);
		if (id !== undefined) return routeEinstellungenBenutzer.getRoute(id);
		return true;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array)
			throw new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const id = !to_params.id ? undefined : parseInt(to_params.id);
		await this.data.ladeListe();
		if (to.name === this.name) {
			if (this.data.mapBenutzer.size === 0) return;
			return this.getRoute(this.data.mapBenutzer.values().next().value?.id);
		}
		// Weiterleitung an das erste Objekt in der Liste, wenn id nicht vorhanden ist.
		if (id !== undefined && !this.data.mapBenutzer.has(id))
			return this.getRoute(this.data.mapBenutzer.values().next().value?.id);
		const eintrag = (id !== undefined) ? this.data.mapBenutzer.get(id) : undefined;
		await this.data.setBenutzer(eintrag);
	}

	public getRoute(id?: number): RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: id } };
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

	public get childRouteSelector(): WritableComputedRef<RouteRecordRaw> {
		return computed({
			get: () => this.selectedChildRecord || this.defaultChild!.record,
			set: (value) => {
				this.selectedChildRecord = value;
				void RouteManager.doRoute({ name: value.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: this.data.auswahl?.id }, });
			},
		});
	}

	private setTab = async (value: TabData) => {
		if (value.name === this.data.view.name) return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined) throw new DeveloperNotificationException("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: this.data.auswahl?.id } });
		this.data.setView(node, routeEinstellungen.children);
	};

}

export const routeEinstellungenBenutzer = new RouteEinstellungenBenutzer();
