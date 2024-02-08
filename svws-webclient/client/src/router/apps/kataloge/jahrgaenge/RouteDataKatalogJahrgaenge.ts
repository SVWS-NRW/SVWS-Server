import type { JahrgangsDaten, JahrgangsListeEintrag } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";

import { routeKatalogJahrgaenge } from "./RouteKatalogJahrgaenge";
import { routeKatalogJahrgaengeDaten } from "./RouteKatalogJahrgaengeDaten";

interface RouteStateKatalogJahrgaenge extends RouteStateInterface {
	auswahl: JahrgangsListeEintrag | undefined;
	daten: JahrgangsDaten | undefined;
	mapKatalogeintraege: Map<number, JahrgangsListeEintrag>;
}

const defaultState = <RouteStateKatalogJahrgaenge> {
	auswahl: undefined,
	daten: undefined,
	mapKatalogeintraege: new Map(),
	view: routeKatalogJahrgaengeDaten,
};


export class RouteDataKatalogJahrgaenge extends RouteData<RouteStateKatalogJahrgaenge> {

	public constructor() {
		super(defaultState);
	}

	get auswahl(): JahrgangsListeEintrag | undefined {
		return this._state.value.auswahl;
	}

	get mapKatalogeintraege(): Map<number, JahrgangsListeEintrag> {
		return this._state.value.mapKatalogeintraege;
	}

	get daten(): JahrgangsDaten {
		if (this._state.value.daten === undefined)
			throw new Error("Unerwarteter Fehler: Klassendaten nicht initialisiert");
		return this._state.value.daten;
	}

	public async ladeListe() {
		const listKatalogeintraege = await api.server.getJahrgaenge(api.schema);
		const mapKatalogeintraege = new Map<number, JahrgangsListeEintrag>();
		const auswahl = listKatalogeintraege.size() > 0 ? listKatalogeintraege.get(0) : undefined;
		for (const l of listKatalogeintraege)
			mapKatalogeintraege.set(l.id, l);
		this.setPatchedDefaultState({ auswahl, mapKatalogeintraege })
	}

	setEintrag = async (auswahl: JahrgangsListeEintrag) => {
		const daten = await api.server.getJahrgang(api.schema, auswahl.id)
		this.setPatchedState({ auswahl, daten })
	}

	gotoEintrag = async (eintrag: JahrgangsListeEintrag) => {
		await RouteManager.doRoute(routeKatalogJahrgaenge.getRoute(eintrag.id));
	}

	patch = async (data : Partial<JahrgangsDaten>) => {
		if ((this.auswahl === undefined) || (this.daten === undefined))
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		await api.server.patchJahrgang(data, api.schema, this.daten.id);
		Object.assign(this.daten, data);
		if (data.kuerzel !== undefined)
			this.auswahl.kuerzel = data.kuerzel;
		if (data.bezeichnung !== undefined)
			this.auswahl.bezeichnung = data.bezeichnung;
		if (data.anzahlRestabschnitte !== undefined)
			this.auswahl.anzahlRestabschnitte = data.anzahlRestabschnitte;
		this.setPatchedState({ auswahl: this.auswahl, daten: this.daten });
	}

}
