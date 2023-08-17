
import type { JahrgangsListeEintrag, KlassenDaten, KlassenListeEintrag, LehrerListeEintrag, Schueler} from "@core";
import { type RouteNode } from "~/router/RouteNode";

import { shallowRef } from "vue";
import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { routeKlassen } from "~/router/apps/klassen/RouteKlassen";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";

import { routeKlasseDaten } from "~/router/apps/klassen/RouteKlasseDaten";


interface RouteStateKlassen {
	idAbschnitt: number | undefined;
	auswahl: KlassenListeEintrag | undefined;
	daten: KlassenDaten | undefined;
	mapKatalogeintraege: Map<number, KlassenListeEintrag>;
	mapLehrer: Map<number, LehrerListeEintrag>;
	mapJahrgaenge: Map<number, JahrgangsListeEintrag>;
	klassenFilter: {search: string, sichtbar: boolean};
	view: RouteNode<any, any>;
}

export class RouteDataKlassen {

	private static _defaultState: RouteStateKlassen = {
		idAbschnitt: undefined,
		auswahl: undefined,
		daten: undefined,
		mapKatalogeintraege: new Map(),
		mapLehrer: new Map(),
		mapJahrgaenge: new Map(),
		klassenFilter: {search: '', sichtbar: true},
		view: routeKlasseDaten,
	}

	private _state = shallowRef(RouteDataKlassen._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateKlassen>) {
		this._state.value = Object.assign({ ...RouteDataKlassen._defaultState }, patch);
	}


	private setPatchedState(patch: Partial<RouteStateKlassen>) {
		this._state.value = Object.assign({ ...this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ...this._state.value };
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeKlassen.children.includes(view))
			this.setPatchedState({ view: view });
		else
			throw new Error("Diese für die Klassen gewählte Ansicht wird nicht unterstützt.");
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}

	get hatAuswahl() : boolean {
		return this._state.value.auswahl !== undefined;
	}

	get auswahl(): KlassenListeEintrag | undefined {
		return this._state.value.auswahl;
	}

	get mapKatalogeintraege(): Map<number, KlassenListeEintrag> {
		const res = new Map();
		for (const [k,v] of this._state.value.mapKatalogeintraege.entries())
			if (v.kuerzel?.toLocaleLowerCase().includes(this.klassenFilter.search.toLocaleLowerCase()) && v.istSichtbar === this.klassenFilter.sichtbar)
				res.set(k,v);
		if (this._state.value.auswahl !== undefined && !res.has(this._state.value.auswahl.id))
			this._state.value.auswahl = undefined;
		return res;
	}

	get mapLehrer(): Map<number, LehrerListeEintrag> {
		return this._state.value.mapLehrer;
	}

	get mapJahrgaenge(): Map<number, JahrgangsListeEintrag> {
		return this._state.value.mapJahrgaenge;
	}

	get klassenFilter(): {search: string, sichtbar: boolean} {
		return this._state.value.klassenFilter;
	}

	get daten(): KlassenDaten {
		if (this._state.value.daten === undefined)
			throw new Error("Unerwarteter Fehler: Klassendaten nicht initialisiert");
		return this._state.value.daten;
	}

	public leave() : void {
		// Speicher die Auswahl, so dass die Route zur der Klasse ggf. wieder aktiviert wird...
		this.setPatchedDefaultState({ auswahl: this._state.value.auswahl });
	}

	setEintrag = async (idAbschnitt : number, idKlasse: number | undefined) => {
		let changed = false;
		// Prüfe, ob die Abschnitts-spezifischen Daten zu laden sind
		let mapKatalogeintraege = this._state.value.mapKatalogeintraege;
		let mapLehrer = this._state.value.mapLehrer;
		let mapJahrgaenge = this._state.value.mapJahrgaenge;
		if (idAbschnitt !== this._state.value.idAbschnitt) {
			const listKatalogeintraege = await api.server.getKlassenFuerAbschnitt(api.schema, idAbschnitt);
			mapKatalogeintraege = new Map<number, KlassenListeEintrag>();
			for (const l of listKatalogeintraege)
				mapKatalogeintraege.set(l.id, l);
			// Laden der Jahrgänge
			const listJahrgaenge = await api.server.getJahrgaenge(api.schema);
			mapJahrgaenge = new Map();
			for (const j of listJahrgaenge)
				mapJahrgaenge.set(j.id, j);
			// Laden des Lehrer-Katalogs
			const listLehrer = await api.server.getLehrer(api.schema);
			mapLehrer = new Map();
			for (const l of listLehrer)
				mapLehrer.set(l.id, l);
			changed = true;
		}
		// Prüfe, ob die Klassen-Daten sich durch die Auswahl verändert haben
		let auswahl = this._state.value.auswahl;
		let daten = this._state.value.daten;
		if ((idKlasse === undefined) || (daten?.id !== idKlasse)) {
			changed = true;
			if (idKlasse !== undefined) {
				if (!mapKatalogeintraege.has(idKlasse))
					throw new Error("Die Klasse mit der ID " + idKlasse + " kann nicht gefunden werden!");
				auswahl = mapKatalogeintraege.get(idKlasse);
			} else if ((auswahl === undefined) || (!mapKatalogeintraege.has(auswahl.id))) {
				auswahl = undefined;
				for (const k of mapKatalogeintraege) {
					// TODO if ... prüfe eintrag.istSichtbar und den Sichtbarkeitsfilter
					auswahl = k[1];
					break;
				}
			}
			daten = (auswahl === undefined) ? undefined
				: await api.server.getKlasse(api.schema, auswahl.id);
		}
		if (changed)
			this.setPatchedState({ idAbschnitt, auswahl, daten, mapKatalogeintraege, mapLehrer, mapJahrgaenge })
	}

	gotoEintrag = async (eintrag: KlassenListeEintrag) => {
		await RouteManager.doRoute(this._state.value.view.getRoute(eintrag.id));
	}

	gotoSchueler = async (eintrag: Schueler) => {
		await RouteManager.doRoute(routeSchueler.getRoute(eintrag.id));
	}

	patch = async (data : Partial<KlassenDaten>) => {
		if (this.auswahl === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		console.log("TODO: Implementierung patchKlassenDaten", data);
		//await api.server.patchKursDaten(data, api.schema, this.item.id);
	}

	setKlassenFilter = (klassenFilter: {search: string, sichtbar: boolean}) => {
		this.setPatchedState({klassenFilter});
	}
}
