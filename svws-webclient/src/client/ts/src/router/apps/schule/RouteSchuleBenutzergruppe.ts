import { BenutzergruppeListeEintrag, BenutzerKompetenz, Schulform } from "@svws-nrw/svws-core";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteRecordRaw } from "vue-router";
import { routeSchuleBenutzergruppeDaten } from "~/router/apps/benutzergruppe/RouteSchuleBenutzergruppeDaten";
import { RouteNodeListView } from "~/router/RouteNodeListView";
import { ListBenutzergruppe } from "~/apps/schule/benutzerverwaltung/ListBenutzergruppe";
import { computed, WritableComputedRef } from "vue";
import { RouteNode } from "~/router/RouteNode";
import { RouteApp } from "~/router/RouteApp";
import { RouteDataSchuleBenutzergruppe } from "../benutzergruppe/RouteDataSchuleBenutzergruppe";
import { BenutzergruppeAuswahlProps } from "~/components/schule/benutzergruppen/SBenutzergruppeAuswahlProps";
import { RouteManager } from "~/router/RouteManager";
import { BenutzergruppeAppProps } from "~/components/schule/benutzergruppen/SBenutzergruppeAppProps";
import { runInThisContext } from "vm";

const SBenutzergruppeAuswahl = () => import("~/components/schule/benutzergruppen/SBenutzergruppeAuswahl.vue")
const SBenutzergruppeApp = () => import("~/components/schule/benutzergruppen/SBenutzergruppeApp.vue")

export class RouteSchuleBenutzergruppe extends RouteNode<RouteDataSchuleBenutzergruppe, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.ADMIN ], "benutzergruppen", "/schule/benutzergruppe/:id(\\d+)?",SBenutzergruppeApp, new RouteDataSchuleBenutzergruppe());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Benutzergruppen";
		super.setView("liste", SBenutzergruppeAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeSchuleBenutzergruppeDaten
		];
		super.defaultChild = routeSchuleBenutzergruppeDaten;
	}

	//Wird beim zielgerichteten Aufruf der Route "/schule/benutzergruppe" ausgeführt!
	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const id = !to_params.id ? undefined : parseInt(to_params.id);
		if (id !== undefined)
			return routeSchuleBenutzergruppeDaten.getRoute(id);
		return true;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
	}

	//Wird bei jeder Änderung Routenänderung von "/schule/benutzergruppe" ausgeführt.""
	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const id = !to_params.id ? undefined : parseInt(to_params.id);
		await this.data.ladeListe();
		if (to.name === this.name) {
			if (this.data.mapBenutzergruppe.size === 0)
				return;
			return this.getRoute(this.data.mapBenutzergruppe.values().next().value.id);
		}
		//Weiterleitung an das erste Objekt in der Liste, wenn id nicht vorhanden ist.
		if(id !== undefined && !this.data.mapBenutzergruppe.has(id))
			return this.getRoute(this.data.mapBenutzergruppe.values().next().value.id);
		const eintrag = (id !== undefined) ? this.data.mapBenutzergruppe.get(id) : undefined;
		await this.data.setBenutzergruppe(eintrag);
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { id: id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): BenutzergruppeAuswahlProps {
		return {
			auswahl: () => this.data.auswahl,
			mapBenutzergruppe: this.data.mapBenutzergruppe,
			gotoBenutzergruppe: this.data.gotoBenutzergruppe,
			createBenutzergruppe : this.data.create,
			deleteBenutzergruppe_n : this.data.deleteBenutzergruppe_n
		};
	}

	public getProps(to: RouteLocationNormalized): BenutzergruppeAppProps {
		return {
			auswahl: () => this.data.auswahl
		};
	}

	public get childRouteSelector() : WritableComputedRef<RouteRecordRaw> {
		return computed({
			get: () => this.selectedChildRecord || this.defaultChild!.record,
			set: (value) => {
				this.selectedChildRecord = value;
				void RouteManager.doRoute({ name: value.name, params: { id: this.data.auswahl?.id } });
			}
		});
	}

}

export const routeSchuleBenutzergruppe = new RouteSchuleBenutzergruppe();
