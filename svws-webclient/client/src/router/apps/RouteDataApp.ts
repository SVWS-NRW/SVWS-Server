import { computed } from "vue";
import type { RouteLocationRaw } from "vue-router";
import type { Haltestelle, Kindergarten, EinschulungsartKatalogEintrag, OrtKatalogEintrag, OrtsteilKatalogEintrag, ReligionEintrag, SchulEintrag, SchulformKatalogEintrag, TelefonArt, Erzieherart, Fahrschuelerart } from "@core";
import { Schulform, Schuljahresabschnitt } from "@core";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { api } from "~/router/Api";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteManager, routerManager } from "../RouteManager";
import type { AbschnittAuswahlDaten } from "@ui";

interface RouteStateApp extends RouteStateInterface {
	idSchuljahresabschnitt: number,
	mapSchulen: Map<string, SchulEintrag>;
	mapOrte: Map<number, OrtKatalogEintrag>;
	mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
	mapReligionen: Map<number, ReligionEintrag>;
	mapFahrschuelerarten: Map<number, Fahrschuelerart>;
	mapHaltestellen: Map<number, Haltestelle>;
	mapKindergaerten: Map<number, Kindergarten>;
	mapEinschulungsarten: Map<number, EinschulungsartKatalogEintrag>;
	mapTelefonArten: Map<number, TelefonArt>;
	mapErzieherarten: Map<number, Erzieherart>;
}

const defaultState = <RouteStateApp>{
	idSchuljahresabschnitt: -1,
	mapSchulen: new Map<string, SchulEintrag>(),
	mapOrte: new Map(),
	mapOrtsteile: new Map(),
	mapReligionen: new Map(),
	mapFahrschuelerarten: new Map(),
	mapHaltestellen: new Map(),
	mapKindergaerten: new Map(),
	mapEinschulungsarten: new Map(),
	mapTelefonArten: new Map(),
	mapErzieherarten: new Map(),
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
		const fahrschuelerarten = await api.server.getFahrschuelerarten(api.schema)
		const mapFahrschuelerarten = new Map();
		for (const fa of fahrschuelerarten)
			mapFahrschuelerarten.set(fa.id, fa);
		// Lade den Katalog der Haltestellen
		const haltestellen = await api.server.getHaltestellen(api.schema);
		const mapHaltestellen = new Map();
		for (const h of haltestellen)
			mapHaltestellen.set(h.id, h);
		// Lade den Katalog der Kindergärten
		const kindergaerten = await api.server.getKindergaerten(api.schema)
		const mapKindergaerten = new Map();
		for (const k of kindergaerten)
			mapKindergaerten.set(k.id, k);
		// Lade den Katalog der Einschulungsarten
		const einschulungsarten = await api.server.getEinschulungsarten(api.schema)
		const mapEinschulungsarten = new Map();
		for (const e of einschulungsarten)
			mapEinschulungsarten.set(e.id, e);
		// Lade den Katalog der TelefonArten
		const telefonArten = await api.server.getTelefonarten(api.schema);
		const mapTelefonArten = new Map();
		for (const ta of telefonArten)
			mapTelefonArten.set(ta.id, ta);
		// Lade den Katalog der Erzieherarten
		const erzieherarten = await api.server.getErzieherArten(api.schema);
		const mapErzieherarten = new Map();
		for (const ea of erzieherarten)
			mapErzieherarten.set(ea.id, ea);
		// Ermittle den Katalog der Schulen, welche ein Kürzel haben und als Stammschulen für Schüler in Frage kommen
		const schulen = await api.server.getSchulen(api.schema);
		const mapSchulen = new Map<string, SchulEintrag>();
		for (const schule of schulen) {
			if (schule.schulnummerStatistik === null)
				continue;
			const sfEintrag : SchulformKatalogEintrag | null = schule.idSchulform === null ? null : Schulform.data().getEintragByID(schule.idSchulform);
			const sf : Schulform | null = sfEintrag === null ? null : Schulform.data().getWertBySchluessel(sfEintrag.schluessel);
			if (sf === api.schulform)
				mapSchulen.set(schule.schulnummerStatistik, schule);
		}
		// Und aktualisiere den internen State
		this.setPatchedDefaultStateKeepView({ mapOrte, mapOrtsteile, mapReligionen, mapFahrschuelerarten, mapHaltestellen, mapKindergaerten, mapEinschulungsarten, mapSchulen, mapTelefonArten, mapErzieherarten });
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

	get mapSchulen(): Map<string, SchulEintrag> {
		return this._state.value.mapSchulen;
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

	get mapFahrschuelerarten(): Map<number, Fahrschuelerart> {
		return this._state.value.mapFahrschuelerarten;
	}

	get mapHaltestellen(): Map<number, Haltestelle> {
		return this._state.value.mapHaltestellen;
	}

	get mapKindergaerten(): Map<number, Kindergarten> {
		return this._state.value.mapKindergaerten;
	}

	get mapEinschulungsarten(): Map<number, EinschulungsartKatalogEintrag> {
		return this._state.value.mapEinschulungsarten;
	}

	get mapTelefonArten(): Map<number, TelefonArt> {
		return this._state.value.mapTelefonArten;
	}

	get mapErzieherarten(): Map<number, Erzieherart> {
		return this._state.value.mapErzieherarten;
	}

	public get idSchuljahresabschnitt() {
		return this._state.value.idSchuljahresabschnitt;
	}

}

