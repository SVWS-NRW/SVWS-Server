import { BenutzerKompetenz, Schulform } from "@svws-nrw/svws-core";
import { computed, WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteRecordRaw } from "vue-router";
import { BenutzerAppProps } from "~/components/schule/benutzer/SBenutzerAppProps";
import { BenutzerAuswahlProps } from "~/components/schule/benutzer/SBenutzerAuswahlProps";
import { routeSchuleBenutzerDaten } from "~/router/apps/benutzer/RouteSchuleBenutzerDaten";
import { RouteApp } from "~/router/RouteApp";
import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import { RouteDataSchuleBenutzer } from "~/router/apps/benutzer/RouteDataSchuleBenutzer";

const SBenutzerAuswahl = () => import("~/components/schule/benutzer/SBenutzerAuswahl.vue")
const SBenutzerApp = () => import("~/components/schule/benutzer/SBenutzerApp.vue")

export class RouteSchuleBenutzer extends RouteNode<RouteDataSchuleBenutzer,RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.ADMIN ], "benutzer", "/schule/benutzer/:id(\\d+)?",SBenutzerApp, new RouteDataSchuleBenutzer());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Benutzer";
		super.setView("liste", SBenutzerAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeSchuleBenutzerDaten
		];
		super.defaultChild = routeSchuleBenutzerDaten;
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const id = !to_params.id ? undefined : parseInt(to_params.id);
		if (id !== undefined)
			return routeSchuleBenutzer.getRoute(id);
		return true;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {

	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any>{
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const id = !to_params.id ? undefined : parseInt(to_params.id);
		await this.data.ladeListe();
		if (to.name === this.name) {
			if (this.data.mapBenutzer.size === 0)
				return;
			return this.getRoute(this.data.mapBenutzer.values().next().value.id);
		}
		//Weiterleitung an das erste Objekt in der Liste, wenn id nicht vorhanden ist.
		if(id !== undefined && !this.data.mapBenutzer.has(id))
			return this.getRoute(this.data.mapBenutzer.values().next().value.id);
		const eintrag = (id !== undefined) ? this.data.mapBenutzer.get(id) : undefined;
		await this.data.setBenutzer(eintrag);

	}

	public getRoute(id?: number) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { id: id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): BenutzerAuswahlProps {
		return {
			auswahl: () => this.data.auswahl,
			mapBenutzer: this.data.mapBenutzer,
			gotoBenutzer: this.data.gotoBenutzer,
			createBenutzerAllgemein : this.data.createBenutzerAllgemein,
			deleteBenutzerAllgemein : this.data.deleteBenutzerAllgemein
		};
	}

	public getProps(to: RouteLocationNormalized): BenutzerAppProps {
		return {
			auswahl:()=> this.data.auswahl
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

export const routeSchuleBenutzer = new RouteSchuleBenutzer();
