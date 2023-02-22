import { KlassenListeEintrag, LehrerListeEintrag, List, Vector } from "@svws-nrw/svws-core-ts";
import { computed, ShallowRef, shallowRef, WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteRecordRaw } from "vue-router";
import { routeKlassenDaten } from "~/router/apps/klassen/RouteKlassenDaten";
import { RouteNode } from "~/router/RouteNode";
import { routeApp, RouteApp } from "~/router/RouteApp";
import { RouteManager } from "../RouteManager";
import { KlassenAppProps } from "~/components/klassen/SKlassenAppProps";
import { KlassenAuswahlProps } from "~/components/klassen/SKlassenAuswahlProps";
import { api } from "../Api";

export class RouteDataKlassen {
	auswahl: ShallowRef<KlassenListeEintrag | undefined> = shallowRef(undefined);
	listKlassen: List<KlassenListeEintrag> = new Vector();
	mapKlassen: Map<number, KlassenListeEintrag> = new Map();
	mapLehrer: Map<number, LehrerListeEintrag> = new Map();

	public async ladeListe() {
		this.listKlassen = await api.server.getKlassenFuerAbschnitt(api.schema, routeApp.data.aktAbschnitt.value.id);
		const mapKurse = new Map<number, KlassenListeEintrag>();
		for (const l of this.listKlassen)
			mapKurse.set(l.id, l);
		this.mapKlassen = mapKurse;
	}

	public async onSelect(item?: KlassenListeEintrag) {
		if (item === this.auswahl.value)
			return;
		if (item === undefined) {
			this.auswahl.value = undefined;
		} else {
			this.auswahl.value = item;
		}
	}

	setKlasse = async (value: KlassenListeEintrag | undefined) => {
		if (value === undefined) {
			await RouteManager.doRoute({ name: routeKlassen.name, params: { } });
			return;
		}
		const redirect_name: string = (routeKlassen.selectedChild === undefined) ? routeKlassenDaten.name : routeKlassen.selectedChild.name;
		await RouteManager.doRoute({ name: redirect_name, params: { id: value.id } });
	}
}

const SKlassenAuswahl = () => import("~/components/klassen/SKlassenAuswahl.vue")
const SKlassenApp = () => import("~/components/klassen/SKlassenApp.vue")

export class RouteKlassen extends RouteNode<RouteDataKlassen, RouteApp> {

	public constructor() {
		super("klassen", "/klassen/:id(\\d+)?", SKlassenApp, new RouteDataKlassen());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Klassen";
		super.setView("liste", SKlassenAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKlassenDaten
		];
		super.defaultChild = routeKlassenDaten;
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		return true;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		if ((to.name === this.name) && (to_params.id === undefined)) {
			await this.data.ladeListe();
			if (this.data.mapKlassen.size === 0)
				// TODO Handhabung bei neuer Schule -> Liste leer
				return this.getRoute(-1);
			return this.getRoute(this.data.mapKlassen.values().next().value.id);
		}
		// Laden des Lehrer-Katalogs
		const listLehrer = await api.server.getLehrer(api.schema);
		const mapLehrer = new Map<number, LehrerListeEintrag>();
		for (const l of listLehrer)
			mapLehrer.set(l.id, l);
		this.data.mapLehrer = mapLehrer;
		// Die Auswahlliste wird als letztes geladen
		await this.data.ladeListe();
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (to_params.id === undefined) {
			await this.data.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id);
			await this.data.onSelect(this.data.mapKlassen.get(id));
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { id: id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): KlassenAuswahlProps {
		return {
			auswahl: this.data.auswahl.value,
			listKlassen: this.data.listKlassen,
			abschnitte: routeApp.data.schuleStammdaten.abschnitte,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			setAbschnitt: routeApp.data.setAbschnitt,
			setKlasse: this.data.setKlasse
		};
	}

	public getProps(to: RouteLocationNormalized): KlassenAppProps {
		return {
			auswahl: this.data.auswahl.value,
			mapLehrer: this.data.mapLehrer
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

export const routeKlassen = new RouteKlassen();
