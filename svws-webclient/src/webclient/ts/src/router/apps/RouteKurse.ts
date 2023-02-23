import { BenutzerKompetenz, JahrgangsListeEintrag, KursListeEintrag, LehrerListeEintrag, List, Schulform, Vector } from "@svws-nrw/svws-core-ts";
import { computed, ShallowRef, shallowRef, WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteRecordRaw } from "vue-router";
import { KurseAppProps } from "~/components/kurse/SKurseAppProps";
import { KurseAuswahlProps } from "~/components/kurse/SKurseAuswahlProps";
import { routeKurseDaten } from "~/router/apps/kurse/RouteKurseDaten";
import { RouteNode } from "~/router/RouteNode";
import { api } from "../Api";
import { routeApp, RouteApp } from "../RouteApp";
import { RouteManager } from "../RouteManager";

export class RouteDataKurse {
	auswahl: ShallowRef<KursListeEintrag | undefined> = shallowRef(undefined);
	listKurse: List<KursListeEintrag> = new Vector();
	mapKurse: Map<number, KursListeEintrag> = new Map();
	mapJahrgaenge: Map<number, JahrgangsListeEintrag> = new Map();
	mapLehrer: Map<number, LehrerListeEintrag> = new Map();

	public async ladeListe() {
		this.listKurse = await api.server.getKurseFuerAbschnitt(api.schema, routeApp.data.aktAbschnitt.value.id);
		const mapKurse = new Map<number, KursListeEintrag>();
		for (const l of this.listKurse)
			mapKurse.set(l.id, l);
		this.mapKurse = mapKurse;
	}

	public async onSelect(item?: KursListeEintrag) {
		if (item === this.auswahl.value)
			return;
		if (item === undefined) {
			this.auswahl.value = undefined;
		} else {
			this.auswahl.value = item;
		}
	}

	setKurs = async (value: KursListeEintrag | undefined) => {
		if (value === undefined) {
			await RouteManager.doRoute({ name: routeKurse.name, params: { } });
			return;
		}
		const redirect_name: string = (routeKurse.selectedChild === undefined) ? routeKurseDaten.name : routeKurse.selectedChild.name;
		await RouteManager.doRoute({ name: redirect_name, params: { id: value.id } });
	}
}

const SKurseAuswahl = () => import("~/components/kurse/SKurseAuswahl.vue")
const SKurseApp = () => import("~/components/kurse/SKurseApp.vue")

export class RouteKurse extends RouteNode<RouteDataKurse, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kurse", "/kurse/:id(\\d+)?", SKurseApp, new RouteDataKurse());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Kurse";
		super.setView("liste", SKurseAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKurseDaten
		];
		super.defaultChild = routeKurseDaten;
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		return true;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		if ((to.name === this.name) && (to_params.id === undefined)) {
			await this.data.ladeListe();
			if (this.data.mapKurse.size === 0)
				// TODO Handhabung bei neuer Schule -> Liste leer
				return this.getRoute(-1);
			return this.getRoute(this.data.mapKurse.values().next().value.id);
		}
		// Laden der Jahrgänge
		const listJahrgaenge = await api.server.getJahrgaenge(api.schema);
		const mapJahrgaenge = new Map<number, JahrgangsListeEintrag>();
		for (const j of listJahrgaenge)
			mapJahrgaenge.set(j.id, j);
		this.data.mapJahrgaenge = mapJahrgaenge;
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
			await this.data.onSelect(this.data.mapKurse.get(id));
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { id: id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): KurseAuswahlProps {
		return {
			auswahl: this.data.auswahl.value,
			listKurse: this.data.listKurse,
			mapJahrgaenge: this.data.mapJahrgaenge,
			mapLehrer: this.data.mapLehrer,
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			setAbschnitt: routeApp.data.setAbschnitt,
			setKurs: this.data.setKurs
		};
	}

	public getProps(to: RouteLocationNormalized): KurseAppProps {
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

export const routeKurse = new RouteKurse();
