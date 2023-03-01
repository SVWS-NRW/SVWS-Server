import { BenutzerKompetenz, FoerderschwerpunktEintrag, List, Schulform, Vector } from "@svws-nrw/svws-core-ts";
import { computed, ShallowRef, shallowRef, WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteRecordRaw } from "vue-router";
import { routeKatalogFoerderschwerpunkteDaten } from "~/router/apps/foerderschwerpunkte/RouteKatalogFoerderschwerpunkteDaten";
import { RouteNode } from "~/router/RouteNode";
import { routeApp, RouteApp } from "~/router/RouteApp";
import { api } from "../Api";
import { RouteManager } from "../RouteManager";
import { FoerderschwerpunkteAppProps } from "~/components/kataloge/foerderschwerpunkte/SFoerderschwerpunkteAppProps";
import { FoerderschwerpunkteAuswahlProps } from "~/components/kataloge/foerderschwerpunkte/SFoerderschwerpunkteAuswahlProps";

export class RouteDataKatalogFoerderschwerpunkte {
	auswahl: ShallowRef<FoerderschwerpunktEintrag | undefined> = shallowRef(undefined);
	listFoerderschwerpunkte: List<FoerderschwerpunktEintrag> = new Vector();
	mapFoerderschwerpunkte: Map<number, FoerderschwerpunktEintrag> = new Map();

	public async ladeListe() {
		this.listFoerderschwerpunkte = await api.server.getSchuelerFoerderschwerpunkte(api.schema);
		const mapKurse = new Map<number, FoerderschwerpunktEintrag>();
		for (const l of this.listFoerderschwerpunkte)
			mapKurse.set(l.id, l);
		this.mapFoerderschwerpunkte = mapKurse;
	}

	public async onSelect(item?: FoerderschwerpunktEintrag) {
		if (item === this.auswahl.value)
			return;
		if (item === undefined) {
			this.auswahl.value = undefined;
		} else {
			this.auswahl.value = item;
		}
	}

	setFoerderschwerpunkt = async (value: FoerderschwerpunktEintrag | undefined) => {
		if (value === undefined || value === null) {
			await RouteManager.doRoute({ name: routeKatalogFoerderschwerpunkte.name, params: { } });
			return;
		}
		const redirect_name: string = (routeKatalogFoerderschwerpunkte.selectedChild === undefined) ? routeKatalogFoerderschwerpunkteDaten.name : routeKatalogFoerderschwerpunkte.selectedChild.name;
		await RouteManager.doRoute({ name: redirect_name, params: { id: value.id } });
	}
}
const SFoerderschwerpunkteAuswahl = () => import("~/components/kataloge/foerderschwerpunkte/SFoerderschwerpunkteAuswahl.vue")
const SFoerderschwerpunkteApp = () => import("~/components/kataloge/foerderschwerpunkte/SFoerderschwerpunkteApp.vue")

export class RouteKatalogFoerderschwerpunkte extends RouteNode<RouteDataKatalogFoerderschwerpunkte, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "foerderschwerpunkte", "/kataloge/foerderschwerpunkte/:id(\\d+)?", SFoerderschwerpunkteApp, new RouteDataKatalogFoerderschwerpunkte());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Förderschwerpunkte";
		super.setView("liste", SFoerderschwerpunkteAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKatalogFoerderschwerpunkteDaten
		];
		super.defaultChild = routeKatalogFoerderschwerpunkteDaten;
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		return true;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		if ((to.name === this.name) && (to_params.id === undefined)) {
			await this.data.ladeListe();
			if (this.data.mapFoerderschwerpunkte.size === 0)
				// TODO Handhabung bei neuer Schule -> Liste leer
				return this.getRoute(-1);
			return this.getRoute(this.data.mapFoerderschwerpunkte.values().next().value.id);
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
			await this.data.onSelect(this.data.mapFoerderschwerpunkte.get(id));
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { id: id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): FoerderschwerpunkteAuswahlProps {
		return {
			auswahl: this.data.auswahl.value,
			listFoerderschwerpunkte: this.data.listFoerderschwerpunkte,
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			aktSchulabschnitt: api.schuleStammdaten.idSchuljahresabschnitt,
			setAbschnitt: routeApp.data.setAbschnitt,
			setFoerderschwerpunkt: this.data.setFoerderschwerpunkt
		};
	}

	public getProps(to: RouteLocationNormalized): FoerderschwerpunkteAppProps {
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

export const routeKatalogFoerderschwerpunkte = new RouteKatalogFoerderschwerpunkte();
