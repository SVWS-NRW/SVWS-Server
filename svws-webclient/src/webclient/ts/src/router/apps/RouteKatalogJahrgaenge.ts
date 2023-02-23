import { JahrgangsListeEintrag, List, Schulform, Vector } from "@svws-nrw/svws-core-ts";
import { computed, ShallowRef, shallowRef, WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteRecordRaw } from "vue-router";
import { routeKatalogJahrgaengeDaten } from "~/router/apps/jahrgaenge/RouteKatalogJahrgaengeDaten";
import { RouteNode } from "~/router/RouteNode";
import { routeApp, RouteApp } from "~/router/RouteApp";
import { api } from "../Api";
import { RouteManager } from "../RouteManager";
import { JahrgaengeAuswahlProps } from "~/components/kataloge/jahrgaenge/SJahrgaengeAuswahlProps";
import { JahrgaengeAppProps } from "~/components/kataloge/jahrgaenge/SJahrgaengeAppProps";

export class RouteDataKatalogJahrgaenge {
	auswahl: ShallowRef<JahrgangsListeEintrag | undefined> = shallowRef(undefined);
	listJahrgaenge: List<JahrgangsListeEintrag> = new Vector();
	mapJahrgaenge: Map<number, JahrgangsListeEintrag> = new Map();

	public async ladeListe() {
		this.listJahrgaenge = await api.server.getJahrgaenge(api.schema);
		const mapKurse = new Map<number, JahrgangsListeEintrag>();
		for (const l of this.listJahrgaenge)
			mapKurse.set(l.id, l);
		this.mapJahrgaenge = mapKurse;
	}

	public async onSelect(item?: JahrgangsListeEintrag) {
		if (item === this.auswahl.value)
			return;
		if (item === undefined) {
			this.auswahl.value = undefined;
		} else {
			this.auswahl.value = item;
		}
	}

	setJahrgang = async (value: JahrgangsListeEintrag | undefined) => {
		if (value === undefined) {
			await RouteManager.doRoute({ name: routeKatalogJahrgaenge.name, params: { } });
			return;
		}
		const redirect_name: string = (routeKatalogJahrgaenge.selectedChild === undefined) ? routeKatalogJahrgaengeDaten.name : routeKatalogJahrgaenge.selectedChild.name;
		await RouteManager.doRoute({ name: redirect_name, params: { id: value.id } });
	}
}
const SJahrgaengeAuswahl = () => import("~/components/kataloge/jahrgaenge/SJahrgaengeAuswahl.vue")
const SJahrgaengeApp = () => import("~/components/kataloge/jahrgaenge/SJahrgaengeApp.vue")

export class RouteKatalogJahrgaenge extends RouteNode<RouteDataKatalogJahrgaenge, RouteApp> {

	public constructor() {
		super(Schulform.values(), "jahrgaenge", "/kataloge/jahrgaenge/:id(\\d+)?", SJahrgaengeApp, new RouteDataKatalogJahrgaenge());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Jahrgänge";
		super.setView("liste", SJahrgaengeAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKatalogJahrgaengeDaten
		];
		super.defaultChild = routeKatalogJahrgaengeDaten;
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		return true;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		if ((to.name === this.name) && (to_params.id === undefined)) {
			await this.data.ladeListe();
			if (this.data.mapJahrgaenge.size === 0)
				// TODO Handhabung bei neuer Schule -> Liste leer
				return this.getRoute(-1);
			return this.getRoute(this.data.mapJahrgaenge.values().next().value.id);
		}
		await this.data.ladeListe();
	}


	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (to_params.id === undefined) {
			await this.data.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id);
			await this.data.onSelect(this.data.mapJahrgaenge.get(id));
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { id: id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): JahrgaengeAuswahlProps {
		return {
			auswahl: this.data.auswahl.value,
			listJahrgaenge: this.data.listJahrgaenge,
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			setAbschnitt: routeApp.data.setAbschnitt,
			setJahrgang: this.data.setJahrgang
		};
	}

	public getProps(to: RouteLocationNormalized): JahrgaengeAppProps {
		return {
			auswahl: this.data.auswahl.value
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

export const routeKatalogJahrgaenge = new RouteKatalogJahrgaenge();
