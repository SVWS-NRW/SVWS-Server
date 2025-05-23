import { computed } from "vue";
import type { RouteLocationRaw } from "vue-router";
import { Schuljahresabschnitt, type OrtKatalogEintrag, type OrtsteilKatalogEintrag, type ReligionEintrag, type KatalogEintrag } from "@core";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { api } from "~/router/Api";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteManager, routerManager } from "../RouteManager";
import type { AbschnittAuswahlDaten } from "@ui";


interface RouteStateApp extends RouteStateInterface {
	idSchuljahresabschnitt: number,
	mapOrte: Map<number, OrtKatalogEintrag>;
	mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
	mapReligionen: Map<number, ReligionEintrag>;
	mapFahrschuelerarten: Map<number, KatalogEintrag>;
	mapHaltestellen: Map<number, KatalogEintrag>;
}

const defaultState = <RouteStateApp>{
	idSchuljahresabschnitt: -1,
	mapOrte: new Map(),
	mapOrtsteile: new Map(),
	mapReligionen: new Map(),
	mapFahrschuelerarten: new Map(),
	mapHaltestellen: new Map(),
	view: routeSchueler,
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
		// Lade den Katalog der Religionen
		const religionen = await api.server.getReligionen(api.schema)
		const mapReligionen = new Map();
		for (const r of religionen)
			mapReligionen.set(r.id, r);
		// Lade den Katalog der Fahrschülerarten
		const fahrschuelerarten = await api.server.getSchuelerFahrschuelerarten(api.schema)
		const mapFahrschuelerarten = new Map();
		for (const fa of fahrschuelerarten)
			mapFahrschuelerarten.set(fa.id, fa);
		// Lade den Katalog der Haltestellen
		const haltestellen = await api.server.getHaltestellen(api.schema);
		const mapHaltestellen = new Map();
		for (const h of haltestellen)
			mapHaltestellen.set(h.id, h);
		// Und aktualisiere den internen State
		this.setPatchedDefaultStateKeepView({ mapOrte, mapOrtsteile, mapReligionen, mapFahrschuelerarten, mapHaltestellen });
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
	 * Getter für die Informationen, welche für eine Schuljahresabschnittsauswahl benötigt werden.
	 */
	public getSchuljahresabschnittsauswahl(istAktiv: boolean) : AbschnittAuswahlDaten {
		return <AbschnittAuswahlDaten>{
			aktiv: istAktiv, // && api.mode !== ServerMode.STABLE,
			abschnitte: api.mapAbschnitte.value,
			aktuell: this.aktAbschnitt.value,
			schule: api.mapAbschnitte.value.get(api.schuleStammdaten.idSchuljahresabschnitt) ?? new Schuljahresabschnitt(),
			set: this.setAbschnitt,
		};
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

	get mapReligionen(): Map<number, ReligionEintrag> {
		return this._state.value.mapReligionen;
	}

	get mapFahrschuelerarten(): Map<number, KatalogEintrag> {
		return this._state.value.mapFahrschuelerarten;
	}

	get mapHaltestellen(): Map<number, KatalogEintrag> {
		return this._state.value.mapHaltestellen;
	}

	public get idSchuljahresabschnitt() {
		return this._state.value.idSchuljahresabschnitt;
	}

}

