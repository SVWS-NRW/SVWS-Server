import { shallowRef } from "vue";

import type { List, FaecherListeEintrag, LehrerListeEintrag, KursListeEintrag, SchuelerLeistungsdaten, SchuelerLernabschnittListeEintrag, SchuelerLernabschnittsdaten } from "@core";
import { ArrayList } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { routeApp } from "~/router/apps/RouteApp";
import { routeSchuelerLeistungenDaten } from "~/router/apps/schueler/leistungsdaten/RouteSchuelerLeistungenDaten";


interface RouteStateDataSchuelerLeistungen {
	idSchueler: number | undefined;
	listAbschnitte: List<SchuelerLernabschnittListeEintrag>;
	auswahl: SchuelerLernabschnittListeEintrag | undefined;
	daten: SchuelerLernabschnittsdaten | undefined;
	mapFaecher: Map<number, FaecherListeEintrag>;
	mapLehrer: Map<number, LehrerListeEintrag>;
	mapKurse: Map<number, KursListeEintrag>;
}

export class RouteDataSchuelerLeistungen {

	private static _defaultState: RouteStateDataSchuelerLeistungen = {
		idSchueler: undefined,
		listAbschnitte: new ArrayList<SchuelerLernabschnittListeEintrag>(),
		auswahl: undefined,
		daten: undefined,
		mapFaecher: new Map(),
		mapLehrer: new Map(),
		mapKurse: new Map(),
	}

	private _state = shallowRef(RouteDataSchuelerLeistungen._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateDataSchuelerLeistungen>) {
		this._state.value = Object.assign({ ... RouteDataSchuelerLeistungen._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateDataSchuelerLeistungen>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	get idSchueler(): number | undefined {
		return this._state.value.idSchueler;
	}

	get listAbschnitte(): List<SchuelerLernabschnittListeEintrag> {
		return this._state.value.listAbschnitte;
	}

	public async ladeListe(idSchueler: number) {
		const listAbschnitte = await api.server.getSchuelerLernabschnittsliste(api.schema, idSchueler);
		const listFaecher = await api.server.getFaecher(api.schema);
		const mapFaecher = new Map<number, FaecherListeEintrag>();
		for (const f of listFaecher)
			mapFaecher.set(f.id, f);
		const listLehrer = await api.server.getLehrer(api.schema);
		const mapLehrer = new Map<number, LehrerListeEintrag>();
		for (const l of listLehrer)
			mapLehrer.set(l.id, l);
		// TODO this.setPatchedDefaultState({  });
		this.setPatchedState({ idSchueler, listAbschnitte, mapFaecher, mapLehrer })
	}

	public getEntry(idSchuljahresabschnitt: number, wechselNr: number|null) : SchuelerLernabschnittListeEintrag | undefined {
		for (const current of this.listAbschnitte)
			if ((current.schuljahresabschnitt === idSchuljahresabschnitt) && (current.wechselNr === wechselNr))
				return current;
		return undefined;
	}

	public getEntryDefault() : SchuelerLernabschnittListeEintrag | undefined {
		const entry = this.getEntry(routeApp.data.aktAbschnitt.value.id, null);
		if (entry !== undefined)
			return entry;
		if (this.listAbschnitte.size() > 0)
			return this.listAbschnitte.get(0);
		return undefined;
	}

	get auswahl(): SchuelerLernabschnittListeEintrag {
		if (this._state.value.auswahl === undefined)
			throw new Error("Unerwarteter Fehler: Lernabschnittseintrag nicht festgelegt, es können keine Informationen zu den Leistungsdaten abgerufen oder eingegeben werden.");
		return this._state.value.auswahl;
	}

	get daten(): SchuelerLernabschnittsdaten {
		if (this._state.value.daten === undefined)
			throw new Error("Unerwarteter Fehler: Leistungsdaten nicht initialisiert");
		return this._state.value.daten;
	}
	get mapFaecher(): Map<number, FaecherListeEintrag> {
		return this._state.value.mapFaecher;
	}

	get mapLehrer(): Map<number, LehrerListeEintrag> {
		return this._state.value.mapLehrer;
	}

	get mapKurse(): Map<number, KursListeEintrag> {
		return this._state.value.mapKurse;
	}

	public async setEintrag(lernabschnitt: SchuelerLernabschnittListeEintrag | undefined) {
		if (lernabschnitt === undefined)
			return;
		const daten = await api.server.getSchuelerLernabschnittsdatenByID(api.schema, lernabschnitt.id);
		const listKurse = await api.server.getKurseFuerAbschnitt(api.schema, lernabschnitt.schuljahresabschnitt);
		const mapKurse = new Map<number, KursListeEintrag>();
		for (const k of listKurse)
			mapKurse.set(k.id, k);
		this.setPatchedState({ auswahl: lernabschnitt, daten, mapKurse });
	}

	gotoLernabschnitt = async (value: SchuelerLernabschnittListeEintrag | undefined) => {
		await RouteManager.doRoute({ name: routeSchuelerLeistungenDaten.name, params: { id: value?.schuelerID, abschnitt: value?.schuljahresabschnitt, wechselNr: value?.wechselNr || undefined } });
	}

	patchLeistung = async (data : Partial<SchuelerLeistungsdaten>, id : number) => {
		// TODO await api.server.patchSchuelerLeistungsdaten(data, api.schema, id);
	}

}
