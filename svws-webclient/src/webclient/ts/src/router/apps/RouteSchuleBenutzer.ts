import { BenutzerKompetenz, Schulform } from "@svws-nrw/svws-core-ts";
import { computed, WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteRecordRaw } from "vue-router";
import { BenutzerAppProps } from "~/components/schule/benutzer/SBenutzerAppProps";
import { BenutzerAuswahlProps } from "~/components/schule/benutzer/SBenutzerAuswahlProps";
import { routeSchuleBenutzerDaten } from "~/router/apps/benutzer/RouteSchuleBenutzerDaten";
import { RouteApp } from "~/router/RouteApp";
import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "../RouteManager";
import { RouteDataSchuleBenutzer } from "./benutzer/RouteDataSchuleBenutzer";

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
		return true;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		if ((to.name === this.name) && (to_params.id === undefined)) {
			await this.data.ladeListe();
			if (this.data.mapBenutzer.size === 0)
				// TODO Handhabung bei neuer Schule -> Liste leer
				return this.getRoute(-1);
			return this.getRoute(this.data.mapBenutzer.values().next().value.id);
		}
		await this.data.ladeListe();
	}


	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (to_params.id === undefined) {
			await this.data.setBenutzer(undefined);
		} else {
			const id = parseInt(to_params.id);
			await this.data.setBenutzer(this.data.mapBenutzer.get(id));
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { id: id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): BenutzerAuswahlProps {
		return {
			auswahl: this.data.auswahl.value,
			auswahlGruppe: this.data.auswahlGruppe.value,
			listBenutzer: this.data.listBenutzer,
			setBenutzer: this.data.gotoBenutzer,
			setAuswahlGruppe: this.data.setAuswahlGruppe,
			createBenutzerAllgemein : this.data.createBenutzerAllgemein,
			deleteBenutzerAllgemein : this.data.deleteBenutzerAllgemein
		};
	}

	public getProps(to: RouteLocationNormalized): BenutzerAppProps {
		return {
			auswahl: this.data.auswahl.value,
		};
	}
	public get childRouteSelector() : WritableComputedRef<RouteRecordRaw> {
		return computed({
			get: () => this.selectedChildRecord || this.defaultChild!.record,
			set: (value) => {
				this.selectedChildRecord = value;
				void RouteManager.doRoute({ name: value.name, params: { id: this.data.auswahl.value?.id } });
			}
		});
	}

}

export const routeSchuleBenutzer = new RouteSchuleBenutzer();
