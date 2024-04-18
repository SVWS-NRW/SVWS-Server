import { computed } from "vue";
import type { RouteLocationRaw } from "vue-router";
import { type OrtKatalogEintrag, type OrtsteilKatalogEintrag, type Schuljahresabschnitt } from "@core";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { api } from "~/router/Api";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteManager, routerManager } from "../RouteManager";


interface RouteStateApp extends RouteStateInterface {
	idSchuljahresabschnitt: number,
	mapOrte: Map<number, OrtKatalogEintrag>;
	mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
}

const defaultState = <RouteStateApp>{
	idSchuljahresabschnitt: -1,
	mapOrte: new Map(),
	mapOrtsteile: new Map(),
	view: routeSchueler
};

export class RouteDataApp extends RouteData<RouteStateApp> {

	public constructor() {
		super(defaultState);
	}

	public async init() {
		// Lade den Katalog der Orte
		const orte = await api.server.getOrte(api.schema);
		const mapOrte = new Map();
		for (const o of orte)
			mapOrte.set(o.id, o);
		// Lade den Katalog der Ortsteile
		const ortsteile = await api.server.getOrtsteile(api.schema);
		const mapOrtsteile = new Map();
		for (const o of ortsteile)
			mapOrtsteile.set(o.id, o);
		// Und aktualisiere den internen State
		this.setPatchedDefaultStateKeepView({ mapOrte, mapOrtsteile });
	}

	public async leave() {
		this._state.value = this._defaultState;
	}

	aktAbschnitt = computed<Schuljahresabschnitt>(() => api.mapAbschnitte.value.get(this.idSchuljahresabschnitt) || api.abschnitt);

	public setAbschnitt = async (abschnitt: Schuljahresabschnitt) => {
		// TODO was tun, wenn das akt Halbjahr neu gesetzt wurde?
		const node = routerManager.getRouteNode();
		const params = { ... routerManager.getRouteParams()};
		params.idSchuljahresabschnitt = String(abschnitt.id);
		const locationRaw: RouteLocationRaw = {};
		locationRaw.name = node!.name;
		locationRaw.params = params;
		await RouteManager.doRoute(locationRaw);
	}

	/**
	 * Setzt den Schuljahresabschnitt und triggert damit das Laden der Defaults für diesen Abschnitt
	 *
	 * @param {number} idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 */
	public async setSchuljahresabschnitt(idSchuljahresabschnitt: number) {
		// Prüfe, ob sich der Schuljahresabschnitt geändert hat.
		if (this._state.value.idSchuljahresabschnitt === idSchuljahresabschnitt)
			return;
		// Setze den Schuljahresabschnitt
		this.setPatchedState({ idSchuljahresabschnitt });
	}

	public get mapOrte() {
		return this._state.value.mapOrte;
	}

	public get mapOrtsteile() {
		return this._state.value.mapOrtsteile;
	}

	public get idSchuljahresabschnitt() {
		return this._state.value.idSchuljahresabschnitt;
	}

}

