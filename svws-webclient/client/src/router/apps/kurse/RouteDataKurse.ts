import type { FaecherListeEintrag, JahrgangsListeEintrag, KursDaten, LehrerListeEintrag, Schueler} from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";

import { routeApp } from "~/router/apps/RouteApp";
import { routeKurse } from "~/router/apps/kurse/RouteKurse";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeKursDaten } from "~/router/apps/kurse/RouteKursDaten";


interface RouteStateKurse extends RouteStateInterface {
	auswahl: KursDaten | undefined;
	daten: KursDaten | undefined;
	mapKatalogeintraege: Map<number, KursDaten>;
	mapLehrer: Map<number, LehrerListeEintrag>;
	mapJahrgaenge: Map<number, JahrgangsListeEintrag>;
	mapFaecher: Map<number, FaecherListeEintrag>;
}

const defaultState = <RouteStateKurse> {
	auswahl: undefined,
	daten: undefined,
	mapKatalogeintraege: new Map(),
	mapLehrer: new Map(),
	mapJahrgaenge: new Map(),
	mapFaecher: new Map(),
	view: routeKursDaten,
};

export class RouteDataKurse extends RouteData<RouteStateKurse> {

	public constructor() {
		super(defaultState);
	}

	get auswahl(): KursDaten | undefined {
		return this._state.value.auswahl;
	}

	get mapKatalogeintraege(): Map<number, KursDaten> {
		return this._state.value.mapKatalogeintraege;
	}

	get mapLehrer(): Map<number, LehrerListeEintrag> {
		return this._state.value.mapLehrer;
	}

	get mapJahrgaenge(): Map<number, JahrgangsListeEintrag> {
		return this._state.value.mapJahrgaenge;
	}

	get mapFaecher(): Map<number, FaecherListeEintrag> {
		return this._state.value.mapFaecher;
	}

	get daten(): KursDaten {
		if (this._state.value.daten === undefined)
			throw new Error("Unerwarteter Fehler: Klassendaten nicht initialisiert");
		return this._state.value.daten;
	}

	public async ladeListe() {
		const listKatalogeintraege = await api.server.getKurseFuerAbschnitt(api.schema, routeApp.data.aktAbschnitt.value.id);
		const mapKatalogeintraege = new Map();
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
		// Laden des Fächer-Katalogs
		const listFaecher = await api.server.getFaecher(api.schema);
		const mapFaecher = new Map<number, FaecherListeEintrag>();
		for (const l of listFaecher)
			mapFaecher.set(l.id, l);
		this.setPatchedDefaultState({ auswahl, mapKatalogeintraege, mapLehrer, mapJahrgaenge, mapFaecher })
	}

	setEintrag = async (auswahl: KursDaten) => {
		const daten = await api.server.getKurs(api.schema, auswahl.id);
		this.setPatchedState({ auswahl, daten })
	}

	gotoEintrag = async (eintrag: JahrgangsListeEintrag) => {
		await RouteManager.doRoute(routeKurse.getRoute(eintrag.id));
	}

	gotoSchueler = async (eintrag: Schueler) => {
		await RouteManager.doRoute(routeSchueler.getRoute(eintrag.id));
	}

	patch = async (data : Partial<KursDaten>) => {
		if (this.auswahl === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		await api.server.patchKurs(data, api.schema, this.auswahl.id);
		Object.assign(this.daten, data);
		if (data.kuerzel !== undefined)
			this.auswahl.kuerzel = data.kuerzel;
		if (data.lehrer !== undefined)
			this.auswahl.lehrer = data.lehrer;
		if (data.idJahrgaenge !== undefined) {
			this.auswahl.idJahrgaenge.clear();
			this.auswahl.idJahrgaenge.addAll(data.idJahrgaenge);
		}
		if (data.schueler !== undefined) {
			this.auswahl.schueler.clear();
			this.auswahl.schueler.addAll(data.schueler);
		}
		this.commit();
	}

}
