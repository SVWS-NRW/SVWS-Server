import type { FachDaten, FaecherListeEintrag } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import type { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";

import { routeKatalogFachDaten } from "./RouteKatalogFachDaten";
import { routeKatalogFaecher } from "./RouteKatalogFaecher";

interface RouteStateKatalogFaecher extends RouteStateInterface {
	auswahl: FaecherListeEintrag | undefined;
	daten: FachDaten | undefined;
	mapKatalogeintraege: Map<number, FaecherListeEintrag>;
}

const defaultState = <RouteStateKatalogFaecher> {
	auswahl: undefined,
	daten: undefined,
	mapKatalogeintraege: new Map(),
	view: routeKatalogFachDaten,
};

export class RouteDataKatalogFaecher extends RouteData<RouteStateKatalogFaecher> {

	public constructor() {
		super(defaultState);
	}

	get auswahl(): FaecherListeEintrag | undefined {
		return this._state.value.auswahl;
	}

	get mapKatalogeintraege(): Map<number, FaecherListeEintrag> {
		return this._state.value.mapKatalogeintraege;
	}

	get daten(): FachDaten {
		if (this._state.value.daten === undefined)
			throw new Error("Unerwarteter Fehler: Klassendaten nicht initialisiert");
		return this._state.value.daten;
	}

	public async ladeListe() {
		const listKatalogeintraege = await api.server.getFaecher(api.schema);
		const mapKatalogeintraege = new Map<number, FaecherListeEintrag>();
		const auswahl = listKatalogeintraege.size() > 0 ? listKatalogeintraege.get(0) : undefined;
		for (const l of listKatalogeintraege)
			mapKatalogeintraege.set(l.id, l);
		this.setPatchedDefaultState({ auswahl, mapKatalogeintraege })
	}

	setEintrag = async (auswahl: FaecherListeEintrag) => {
		const daten = await api.server.getFach(api.schema, auswahl.id)
		this.setPatchedState({ auswahl, daten })
	}

	gotoEintrag = async (eintrag: FaecherListeEintrag) => {
		await RouteManager.doRoute(routeKatalogFaecher.getRoute(eintrag.id));
	}

	patch = async (data : Partial<FachDaten>) => {
		if (this.auswahl === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		await api.server.patchFach(data, api.schema, this.daten.id);
		Object.assign(this.daten, data);
	}

}
