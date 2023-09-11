import { shallowRef } from "vue";

import type { List, SchuelerListeEintrag, Sprachbelegung, Sprachpruefung} from "@core";
import { ArrayList, Sprachendaten } from "@core";

import { api } from "~/router/Api";


interface RouteStateSchuelerLaufbahninfo {
	auswahl: SchuelerListeEintrag | undefined;
	sprachbelegungen: List<Sprachbelegung>;
	sprachpruefungen: List<Sprachpruefung>;
}

export class RouteDataSchuelerLaufbahninfo {

	private static _defaultState : RouteStateSchuelerLaufbahninfo = {
		auswahl: undefined,
		sprachbelegungen: new ArrayList<Sprachbelegung>(),
		sprachpruefungen: new ArrayList<Sprachpruefung>(),
	}

	private _state = shallowRef<RouteStateSchuelerLaufbahninfo>(RouteDataSchuelerLaufbahninfo._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateSchuelerLaufbahninfo>) {
		this._state.value = Object.assign({ ... RouteDataSchuelerLaufbahninfo._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateSchuelerLaufbahninfo>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	public async clear() {
		this.setPatchedDefaultState({});
	}

	get auswahl(): SchuelerListeEintrag {
		if (this._state.value.auswahl === undefined)
			throw new Error("Unerwarteter Fehler: Schülerauswahl nicht festgelegt, es können keine Informationen zur Laufbahnplanung abgerufen oder eingegeben werden.");
		return this._state.value.auswahl;
	}

	get sprachbelegungen(): List<Sprachbelegung> {
		return this._state.value.sprachbelegungen;
	}

	get sprachpruefungen(): List<Sprachpruefung> {
		return this._state.value.sprachpruefungen;
	}

	public async auswahlSchueler(auswahl?: SchuelerListeEintrag) {
		if (auswahl === this._state.value.auswahl)
			return;
		if (auswahl === undefined) {
			this.setPatchedDefaultState({});
			return;
		}
		try {
			const sprachbelegungen = await api.server.getSchuelerSprachbelegungen(api.schema, auswahl.id);
			const sprachpruefungen = await api.server.getSchuelerSprachpruefungen(api.schema, auswahl.id);
			this.setPatchedState({ auswahl, sprachbelegungen, sprachpruefungen })
		} catch(error) {
			throw new Error("Die Laufbahninformationen konnten nicht eingeholt werden.")
		}
	}

}

