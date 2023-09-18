import { shallowRef } from "vue";

import type { FoerderschwerpunktEintrag, JahrgangsListeEintrag, KlassenListeEintrag, LehrerListeEintrag, SchuelerLernabschnittBemerkungen,
	SchuelerLernabschnittListeEintrag, SchuelerLernabschnittsdaten} from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { routeSchuelerAbschnittDaten } from "~/router/apps/schueler/abschnitte/RouteSchuelerAbschnittDaten";


interface RouteStateDataSchuelerAbschnittDaten {
	auswahl: SchuelerLernabschnittListeEintrag | undefined;
	daten: SchuelerLernabschnittsdaten | undefined;
	mapLehrer: Map<number, LehrerListeEintrag>;
	mapJahrgaenge: Map<number, JahrgangsListeEintrag>;
	mapKlassen: Map<number, KlassenListeEintrag>;
	mapFoerderschwerpunkte: Map<number, FoerderschwerpunktEintrag>;
}

export class RouteDataSchuelerAbschnittDaten {

	private static _defaultState: RouteStateDataSchuelerAbschnittDaten = {
		auswahl: undefined,
		daten: undefined,
		mapLehrer: new Map(),
		mapJahrgaenge: new Map(),
		mapKlassen: new Map(),
		mapFoerderschwerpunkte: new Map(),
	}

	private _state = shallowRef(RouteDataSchuelerAbschnittDaten._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateDataSchuelerAbschnittDaten>) {
		this._state.value = Object.assign({ ... RouteDataSchuelerAbschnittDaten._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateDataSchuelerAbschnittDaten>) {
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
	get mapLehrer(): Map<number, LehrerListeEintrag> {
		return this._state.value.mapLehrer;
	}

	get mapJahrgaenge(): Map<number, JahrgangsListeEintrag> {
		return this._state.value.mapJahrgaenge;
	}

	get mapKlassen(): Map<number, KlassenListeEintrag> {
		return this._state.value.mapKlassen;
	}

	get mapFoerderschwerpunkte(): Map<number, FoerderschwerpunktEintrag> {
		return this._state.value.mapFoerderschwerpunkte;
	}

	public async ladeListe() {
		const lehrer = await api.server.getLehrer(api.schema);
		const mapLehrer = new Map<number, LehrerListeEintrag>();
		for (const l of lehrer)
			mapLehrer.set(l.id, l);
		const jahrgaenge = await api.server.getJahrgaenge(api.schema);
		const mapJahrgaenge = new Map<number, JahrgangsListeEintrag>();
		for (const j of jahrgaenge)
			mapJahrgaenge.set(j.id, j);
		const forderschwerpunkte = await api.server.getSchuelerFoerderschwerpunkte(api.schema);
		const mapFoerderschwerpunkte = new Map<number, FoerderschwerpunktEintrag>();
		for (const fs of forderschwerpunkte)
			mapFoerderschwerpunkte.set(fs.id, fs);
		this.setPatchedDefaultState({ mapLehrer, mapJahrgaenge, mapFoerderschwerpunkte });
	}

	public async setEintrag(auswahl: SchuelerLernabschnittListeEintrag | undefined) {
		if (((auswahl === undefined) && (this._state.value.daten === undefined)) || ((this._state.value.daten !== undefined) && (this._state.value.daten.id === auswahl?.id)))
			return;
		let daten;
		const mapKlassen = new Map();
		if (auswahl !== undefined) {
			daten = await api.server.getSchuelerLernabschnittsdatenByID(api.schema, auswahl.id);
			const listKlassen = await api.server.getKlassenFuerAbschnitt(api.schema, auswahl.schuljahresabschnitt);
			for (const k of listKlassen)
				mapKlassen.set(k.id, k);
		}
		this.setPatchedState({ auswahl, daten, mapKlassen });
	}

	gotoLernabschnitt = async (value: SchuelerLernabschnittListeEintrag | undefined) => {
		await RouteManager.doRoute({ name: routeSchuelerAbschnittDaten.name, params: { id: value?.schuelerID, abschnitt: value?.schuljahresabschnitt, wechselNr: value?.wechselNr || undefined } });
	}

	patch = async (data : Partial<SchuelerLernabschnittsdaten>) => {
		console.log("TODO: Implementierung patch", data);
		// TODO await api.server.patchSchuelerLeistungsdaten(data, api.schema, id);
	}

	patchBemerkungen = async (data : Partial<SchuelerLernabschnittBemerkungen>) => {
		console.log("TODO: Implementierung patchBemerkungen", data);
		// TODO await api.server.patchSchuelerLernabschnittsdatenBemerkungen(data, api.schema, this.daten.value.id);
	}

}

