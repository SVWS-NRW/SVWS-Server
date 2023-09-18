import { shallowRef } from "vue";

import type { FaecherListeEintrag, LehrerListeEintrag, SchuelerLeistungsdaten, SchuelerLernabschnittListeEintrag, SchuelerLernabschnittsdaten } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { routeSchuelerLeistungenDaten } from "~/router/apps/schueler/leistungsdaten/RouteSchuelerLeistungenDaten";


interface RouteStateSchuelerLeistungenDaten {
	auswahl: SchuelerLernabschnittListeEintrag | undefined;
	daten: SchuelerLernabschnittsdaten | undefined;
	mapFaecher: Map<number, FaecherListeEintrag>;
	mapLehrer: Map<number, LehrerListeEintrag>;
}

export class RouteDataSchuelerLeistungenDaten {

	private static _defaultState: RouteStateSchuelerLeistungenDaten = {
		auswahl: undefined,
		daten: undefined,
		mapFaecher: new Map(),
		mapLehrer: new Map(),
	}

	private _state = shallowRef(RouteDataSchuelerLeistungenDaten._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateSchuelerLeistungenDaten>) {
		this._state.value = Object.assign({ ... RouteDataSchuelerLeistungenDaten._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateSchuelerLeistungenDaten>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
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

	public async ladeListe() {
		const listFaecher = await	api.server.getFaecher(api.schema);
		const mapFaecher = new Map<number, FaecherListeEintrag>();
		for (const f of listFaecher)
			mapFaecher.set(f.id, f);
		const listLehrer = await api.server.getLehrer(api.schema);
		const mapLehrer = new Map<number, LehrerListeEintrag>();
		for (const l of listLehrer)
			mapLehrer.set(l.id, l);
		this.setPatchedDefaultState({ mapFaecher, mapLehrer });
	}

	public async setEintrag(lernabschnitt: SchuelerLernabschnittListeEintrag | undefined) {
		if (lernabschnitt === undefined)
			return;
		const daten = await api.server.getSchuelerLernabschnittsdatenByID(api.schema, lernabschnitt.id);
		this.setPatchedState({ auswahl: lernabschnitt, daten });
	}

	gotoLernabschnitt = async (value: SchuelerLernabschnittListeEintrag | undefined) => {
		await RouteManager.doRoute({ name: routeSchuelerLeistungenDaten.name, params: { id: value?.schuelerID, abschnitt: value?.schuljahresabschnitt, wechselNr: value?.wechselNr || undefined } });
	}

	patchLeistung = async (data : Partial<SchuelerLeistungsdaten>, id : number) => {
		// TODO await api.server.patchSchuelerLeistungsdaten(data, api.schema, id);
	}

}

