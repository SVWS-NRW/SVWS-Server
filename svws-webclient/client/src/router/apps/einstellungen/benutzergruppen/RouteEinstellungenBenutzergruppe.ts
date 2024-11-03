import type { WritableComputedRef } from "vue";
import { computed } from "vue";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteParamsRawGeneric, RouteRecordRaw } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

import { routeApp, type RouteApp } from "~/router/apps/RouteApp";
import { routeEinstellungen } from "~/router/apps/einstellungen/RouteEinstellungen";
import { routeEinstellungenBenutzergruppeDaten } from "~/router/apps/einstellungen/benutzergruppen/RouteEinstellungenBenutzergruppeDaten";
import { RouteDataEinstellungenBenutzergruppe } from "~/router/apps/einstellungen/benutzergruppen/RouteDataEinstellungenBenutzergruppe";

import type { TabData } from "@ui";
import type { BenutzergruppeAuswahlProps } from "~/components/einstellungen/benutzergruppen/SBenutzergruppeAuswahlProps";
import type { BenutzergruppeAppProps } from "~/components/einstellungen/benutzergruppen/SBenutzergruppeAppProps";
import { RouteEinstellungenMenuGroup } from "../RouteEinstellungenMenuGroup";

const SEinstellungenAuswahl = () => import("~/components/einstellungen/SEinstellungenAuswahl.vue")
const SBenutzergruppeAuswahl = () => import("~/components/einstellungen/benutzergruppen/SBenutzergruppeAuswahl.vue")
const SBenutzergruppeApp = () => import("~/components/einstellungen/benutzergruppen/SBenutzergruppeApp.vue")

export class RouteEinstellungenBenutzergruppe extends RouteNode<RouteDataEinstellungenBenutzergruppe, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.ADMIN ], "einstellungen.benutzergruppen", "einstellungen/benutzergruppe/:id(\\d+)?", SBenutzergruppeApp, new RouteDataEinstellungenBenutzergruppe());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Benutzergruppen";
		super.menugroup = RouteEinstellungenMenuGroup.BENUTZERVERWALTUNG;
		super.setView("liste", SBenutzergruppeAuswahl, (route) => this.getAuswahlProps(route));
		super.setView("submenu", SEinstellungenAuswahl, (route) => routeEinstellungen.getAuswahlProps(route));
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
			return routeEinstellungenBenutzergruppeDaten.getRoute({ id });
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
			return this.getRouteDefaultChild({ id: this.data.mapBenutzergruppe.values().next().value?.id });
		}
		// Weiterleitung an das erste Objekt in der Liste, wenn id nicht vorhanden ist.
		if(id !== undefined && !this.data.mapBenutzergruppe.has(id))
			return this.getRouteDefaultChild({ id: this.data.mapBenutzergruppe.values().next().value?.id });
		const eintrag = (id !== undefined) ? this.data.mapBenutzergruppe.get(id) : undefined;
		await this.data.setBenutzergruppe(eintrag);
	}

	public addRouteParamsFromState() : RouteParamsRawGeneric {
		const id = (this.data.auswahl !== undefined) ? this.data.auswahl.id : undefined;
		return { id };
	}

	public getAuswahlProps(to: RouteLocationNormalized): BenutzergruppeAuswahlProps {
		return {
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(false),
			auswahl: () => this.data.auswahl,
			mapBenutzergruppe: this.data.mapBenutzergruppe,
			gotoBenutzergruppe: this.data.gotoBenutzergruppe,
			createBenutzergruppe : this.data.create,
			deleteBenutzergruppen : this.data.deleteBenutzergruppen,
		};
	}

	public getProps(to: RouteLocationNormalized): BenutzergruppeAppProps {
		return {
			auswahl: () => this.data.auswahl,
			tabManager: () => this.createTabManagerByChildren(this.data.view.name, this.setTab),
		};
	}

	private setTab = async (value: TabData) => {
		if (value.name === this.data.view.name) return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined) throw new DeveloperNotificationException("Unbekannte Route");
		await RouteManager.doRoute(node.getRoute());
		this.data.setView(node, routeEinstellungen.children);
	};

}

export const routeEinstellungenBenutzergruppe = new RouteEinstellungenBenutzergruppe();
