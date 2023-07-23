import { shallowRef } from "vue";

import type { JahrgangsListeEintrag, KlassenDaten, KlassenListeEintrag, LehrerListeEintrag, Schueler} from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { type RouteNode } from "~/router/RouteNode";
import { routeApp } from "~/router/apps/RouteApp";
import { routeKlassen } from "~/router/apps/klassen/RouteKlassen";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";

import { routeKlasseDaten } from "~/router/apps/klassen/RouteKlasseDaten";


interface RouteStateKlassen {
	auswahl: KlassenListeEintrag | undefined;
	daten: KlassenDaten | undefined;
	mapKatalogeintraege: Map<number, KlassenListeEintrag>;
	mapLehrer: Map<number, LehrerListeEintrag>;
	mapJahrgaenge: Map<number, JahrgangsListeEintrag>;
	view: RouteNode<any, any>;
}

export class RouteDataKlassen {

	private static _defaultState: RouteStateKlassen = {
		auswahl: undefined,
		daten: undefined,
		mapKatalogeintraege: new Map(),
		mapLehrer: new Map(),
		mapJahrgaenge: new Map(),
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

	get auswahl(): KlassenListeEintrag | undefined {
		return this._state.value.auswahl;
	}

	get mapKatalogeintraege(): Map<number, KlassenListeEintrag> {
		return this._state.value.mapKatalogeintraege;
	}

	get mapLehrer(): Map<number, LehrerListeEintrag> {
		return this._state.value.mapLehrer;
	}

	get mapJahrgaenge(): Map<number, JahrgangsListeEintrag> {
		return this._state.value.mapJahrgaenge;
	}

	get daten(): KlassenDaten {
		if (this._state.value.daten === undefined)
			throw new Error("Unerwarteter Fehler: Klassendaten nicht initialisiert");
		return this._state.value.daten;
	}

	public async ladeListe() {
		const listKatalogeintraege = await api.server.getKlassenFuerAbschnitt(api.schema, routeApp.data.aktAbschnitt.value.id);
		const mapKatalogeintraege = new Map<number, KlassenListeEintrag>();
		const auswahl = listKatalogeintraege.size() > 0 ? listKatalogeintraege.get(0) : undefined;
		for (const l of listKatalogeintraege)
			mapKatalogeintraege.set(l.id, l);
		// Laden der Jahrgänge
		const listJahrgaenge = await api.server.getJahrgaenge(api.schema);
		const mapJahrgaenge = new Map();
		for (const j of listJahrgaenge)
			mapJahrgaenge.set(j.id, j);
		// Laden des Lehrer-Katalogs
		const listLehrer = await api.server.getLehrer(api.schema);
		const mapLehrer = new Map();
		for (const l of listLehrer)
			mapLehrer.set(l.id, l);
		this.setPatchedDefaultState({ auswahl, mapKatalogeintraege, mapLehrer, mapJahrgaenge })
	}

	setEintrag = async (auswahl: KlassenListeEintrag | undefined) => {
		if (auswahl === undefined) {
			this.setPatchedState({ auswahl: undefined, daten: undefined })
			return;
		}
		const daten = await api.server.getKlasse(api.schema, auswahl.id)
		this.setPatchedState({ auswahl, daten })
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

}
