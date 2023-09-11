import { shallowRef } from "vue";

import type { SchuelerListeEintrag} from "@core";
import { Sprachendaten } from "@core";

import { api } from "~/router/Api";


interface RouteStateSchuelerLaufbahninfo {
	auswahl: SchuelerListeEintrag | undefined;
	sprachendaten: Sprachendaten | undefined;
}

export class RouteDataSchuelerLaufbahninfo {

	private static _defaultState : RouteStateSchuelerLaufbahninfo = {
		auswahl: undefined,
		sprachendaten: undefined,
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

	get sprachendaten(): Sprachendaten {
		if (this._state.value.sprachendaten === undefined)
			throw new Error("Unerwarteter Fehler: Sprachendaten wurden noch nicht geladen, es können keine Informationen zu der Sprachenfolge und den Sprachprüfungen abgerufen oder eingegeben werden.");
		return this._state.value.sprachendaten;
	}

	public async auswahlSchueler(auswahl?: SchuelerListeEintrag) {
		if (auswahl === this._state.value.auswahl)
			return;
		if (auswahl === undefined) {
			this.setPatchedDefaultState({});
			return;
		}
		try {
			// TODO const sprachendaten = await api.server.getSchuelerSprachendaten(api.schema, auswahl.id);
			const sprachendaten = new Sprachendaten();
			this.setPatchedState({ auswahl, sprachendaten })
		} catch(error) {
			throw new Error("Die Laufbahninformationen konnten nicht eingeholt werden.")
		}
	}

}

