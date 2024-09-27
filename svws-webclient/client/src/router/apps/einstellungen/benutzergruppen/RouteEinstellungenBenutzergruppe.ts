import type { WritableComputedRef } from "vue";
import { computed } from "vue";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteRecordRaw } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

import { routeApp, type RouteApp } from "~/router/apps/RouteApp";
import { routeEinstellungen } from "~/router/apps/einstellungen/RouteEinstellungen";
import { routeEinstellungenBenutzergruppeDaten } from "~/router/apps/einstellungen/benutzergruppen/RouteEinstellungenBenutzergruppeDaten";
import { RouteDataEinstellungenBenutzergruppe } from "~/router/apps/einstellungen/benutzergruppen/RouteDataEinstellungenBenutzergruppe";

import type { AuswahlChildData } from "~/components/AuswahlChildData";
import type { BenutzergruppeAuswahlProps } from "~/components/einstellungen/benutzergruppen/SBenutzergruppeAuswahlProps";
import type { BenutzergruppeAppProps } from "~/components/einstellungen/benutzergruppen/SBenutzergruppeAppProps";

const SBenutzergruppeAuswahl = () => import("~/components/einstellungen/benutzergruppen/SBenutzergruppeAuswahl.vue")
const SBenutzergruppeApp = () => import("~/components/einstellungen/benutzergruppen/SBenutzergruppeApp.vue")

export class RouteEinstellungenBenutzergruppe extends RouteNode<RouteDataEinstellungenBenutzergruppe, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.ADMIN ], "einstellungen.benutzergruppen", "einstellungen/benutzergruppe/:id(\\d+)?", SBenutzergruppeApp, new RouteDataEinstellungenBenutzergruppe());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Benutzergruppen";
		super.setView("liste", SBenutzergruppeAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeEinstellungenBenutzergruppeDaten
		];
		super.defaultChild = routeEinstellungenBenutzergruppeDaten;
	}

	public async beforeEach(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams) : Promise<boolean | void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array)
			throw new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const id = !to_params.id ? undefined : parseInt(to_params.id);
		if (id !== undefined)
			return routeEinstellungenBenutzergruppeDaten.getRoute(id);
		return true;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		if (isEntering)
			await this.data.ladeListe();
		if (to_params.id instanceof Array)
			throw new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const id = !to_params.id ? undefined : parseInt(to_params.id);
		await this.data.ladeListe();
		if (to.name === this.name) {
			if (this.data.mapBenutzergruppe.size === 0)
				return;
			return this.getRoute(this.data.mapBenutzergruppe.values().next().value.id);
		}
		// Weiterleitung an das erste Objekt in der Liste, wenn id nicht vorhanden ist.
		if(id !== undefined && !this.data.mapBenutzergruppe.has(id))
			return this.getRoute(this.data.mapBenutzergruppe.values().next().value.id);
		const eintrag = (id !== undefined) ? this.data.mapBenutzergruppe.get(id) : undefined;
		await this.data.setBenutzergruppe(eintrag);
	}

	public getRoute(id: number | undefined) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): BenutzergruppeAuswahlProps {
		return {
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(false),
			auswahl: () => this.data.auswahl,
			mapBenutzergruppe: this.data.mapBenutzergruppe,
			gotoBenutzergruppe: this.data.gotoBenutzergruppe,
			createBenutzergruppe : this.data.create,
			deleteBenutzergruppen : this.data.deleteBenutzergruppen,
			gotoEinstellungen: routeEinstellungen.gotoEinstellungen
		};
	}

	public getProps(to: RouteLocationNormalized): BenutzergruppeAppProps {
		return {
			auswahl: () => this.data.auswahl,
			// Props für die Navigation
			setTab: this.setTab,
			tab: this.getTab(),
			tabs: this.getTabs(),
			tabsHidden: this.children_hidden().value,
		};
	}

	public get childRouteSelector() : WritableComputedRef<RouteRecordRaw> {
		return computed({
			get: () => this.selectedChildRecord || this.defaultChild!.record,
			set: (value) => {
				this.selectedChildRecord = value;
				void RouteManager.doRoute({ name: value.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: this.data.auswahl?.id } });
			}
		});
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
		if (value.name === this.data.view.name) return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined) throw new DeveloperNotificationException("Unbekannte Route");
		await RouteManager.doRoute({
			name: value.name,
			params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: this.data.auswahl?.id },
		});
		this.data.setView(node, routeEinstellungen.children);
	};

}

export const routeEinstellungenBenutzergruppe = new RouteEinstellungenBenutzergruppe();
