import type { FoerderschwerpunktEintrag } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";

import { routeKatalogFoerderschwerpunktDaten } from "./RouteKatalogFoerderschwerpunktDaten";
import { routeKatalogFoerderschwerpunkte } from "./RouteKatalogFoerderschwerpunkte";

interface RouteStateKatalogFoerderschwerpunkte extends RouteStateInterface {
	auswahl: FoerderschwerpunktEintrag | undefined;
	daten: FoerderschwerpunktEintrag | undefined;
	mapKatalogeintraege: Map<number, FoerderschwerpunktEintrag>;
}

const defaultState = <RouteStateKatalogFoerderschwerpunkte> {
	auswahl: undefined,
	daten: undefined,
	mapKatalogeintraege: new Map(),
	view: routeKatalogFoerderschwerpunktDaten,
};

export class RouteDataKatalogFoerderschwerpunkte extends RouteData<RouteStateKatalogFoerderschwerpunkte> {

	public constructor() {
		super(defaultState);
	}

	get auswahl(): FoerderschwerpunktEintrag | undefined {
		return this._state.value.auswahl;
	}

	get mapKatalogeintraege(): Map<number, FoerderschwerpunktEintrag> {
		return this._state.value.mapKatalogeintraege;
	}

	get daten(): FoerderschwerpunktEintrag {
		if (this._state.value.daten === undefined)
			throw new Error("Unerwarteter Fehler: Klassendaten nicht initialisiert");
		return this._state.value.daten;
	}

	public async ladeListe() {
		const listKatalogeintraege = await api.server.getSchuelerFoerderschwerpunkte(api.schema);
		const mapKatalogeintraege = new Map<number, FoerderschwerpunktEintrag>();
		const auswahl = listKatalogeintraege.size() > 0 ? listKatalogeintraege.get(0) : undefined;
		for (const l of listKatalogeintraege)
			mapKatalogeintraege.set(l.id, l);
		this.setPatchedDefaultState({ auswahl, mapKatalogeintraege })
	}

	setEintrag = async (auswahl: FoerderschwerpunktEintrag) => {
		const daten = this.mapKatalogeintraege.get(auswahl.id);
		this.setPatchedState({ auswahl, daten })
	}

	gotoEintrag = async (eintrag: FoerderschwerpunktEintrag) => {
		await RouteManager.doRoute(routeKatalogFoerderschwerpunkte.getRoute(eintrag.id));
	}

	patch = async (data : Partial<FoerderschwerpunktEintrag>) => {
		if (this.auswahl === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		console.log("TODO: Implementierung patch...Daten", data);
		//await api.server.patch...Daten(data, api.schema, this.item.id);
	}
}