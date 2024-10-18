import { DeveloperNotificationException, type JahrgangsDaten } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";

import { routeSchuleJahrgaenge } from "./RouteSchuleJahrgaenge";
import { routeSchuleJahrgaengeDaten } from "./RouteSchuleJahrgaengeDaten";

interface RouteStateSchuleJahrgaenge extends RouteStateInterface {
	auswahl: JahrgangsDaten | undefined;
	daten: JahrgangsDaten | undefined;
	mapKatalogeintraege: Map<number, JahrgangsDaten>;
}

const defaultState = <RouteStateSchuleJahrgaenge> {
	auswahl: undefined,
	daten: undefined,
	mapKatalogeintraege: new Map(),
	view: routeSchuleJahrgaengeDaten,
};


export class RouteDataSchuleJahrgaenge extends RouteData<RouteStateSchuleJahrgaenge> {

	public constructor() {
		super(defaultState);
	}

	get auswahl(): JahrgangsDaten | undefined {
		return this._state.value.auswahl;
	}

	get mapKatalogeintraege(): Map<number, JahrgangsDaten> {
		return this._state.value.mapKatalogeintraege;
	}

	get daten(): JahrgangsDaten {
		if (this._state.value.daten === undefined)
			throw new DeveloperNotificationException("Unerwarteter Fehler: Klassendaten nicht initialisiert");
		return this._state.value.daten;
	}

	public async ladeListe() {
		const listKatalogeintraege = await api.server.getJahrgaenge(api.schema);
		const mapKatalogeintraege = new Map<number, JahrgangsDaten>();
		const auswahl = listKatalogeintraege.size() > 0 ? listKatalogeintraege.get(0) : undefined;
		for (const l of listKatalogeintraege)
			mapKatalogeintraege.set(l.id, l);
		this.setPatchedDefaultState({ auswahl, mapKatalogeintraege })
	}

	setEintrag = async (auswahl: JahrgangsDaten) => {
		const daten = await api.server.getJahrgang(api.schema, auswahl.id)
		this.setPatchedState({ auswahl, daten })
	}

	gotoEintrag = async (eintrag: JahrgangsDaten) => {
		await RouteManager.doRoute(routeSchuleJahrgaenge.getRoute(eintrag.id));
	}

	patch = async (data : Partial<JahrgangsDaten>) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
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
