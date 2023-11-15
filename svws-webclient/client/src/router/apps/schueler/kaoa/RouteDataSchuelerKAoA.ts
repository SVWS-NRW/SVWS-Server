import { shallowRef } from "vue";

import type { SchuelerListeEintrag} from "@core";
import { SchuelerKAoADaten } from "@core";


interface RouteStateSchuelerKAoA {
	auswahl: SchuelerListeEintrag | undefined;
	data: SchuelerKAoADaten;
}

export class RouteDataSchuelerKAoA {

	private static _defaultState : RouteStateSchuelerKAoA = {
		auswahl: undefined,
		data: new SchuelerKAoADaten(),
	}

	private _state = shallowRef<RouteStateSchuelerKAoA>(RouteDataSchuelerKAoA._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateSchuelerKAoA>) {
		this._state.value = Object.assign({ ... RouteDataSchuelerKAoA._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateSchuelerKAoA>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	get auswahl(): SchuelerListeEintrag {
		if (this._state.value.auswahl === undefined)
			throw new Error("Unerwarteter Fehler: Schülerauswahl nicht festgelegt, es können keine Informationen zu KAoA-Daten abgerufen oder eingegeben werden.");
		return this._state.value.auswahl;
	}

	get data(): SchuelerKAoADaten {
		// if (this._state.value.data === undefined)
		// 	throw new Error("Unerwarteter Fehler: Schülerauswahl nicht festgelegt, es können keine Informationen zu KAoA-Daten abgerufen oder eingegeben werden.");
		return this._state.value.data
	}

	patch = async (data : Partial<SchuelerKAoADaten>) => {
		if (this.auswahl === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		console.log("TODO: Implementierung patch KAoA", data);
	}


	public async ladeDaten(auswahl: SchuelerListeEintrag | null) {
		if (auswahl === this._state.value.auswahl)
			return;
		if ((auswahl === null) || (auswahl === undefined))
			this.setPatchedDefaultState({});
		else {
			try {
				// TODO Lade KAoA-Daten
			} catch(error) {
				throw new Error("Die KAoA-Daten konnten nicht eingeholt werden, sind für diesen Schüler KAoA-Daten möglich?")
			}
		}
	}
}
