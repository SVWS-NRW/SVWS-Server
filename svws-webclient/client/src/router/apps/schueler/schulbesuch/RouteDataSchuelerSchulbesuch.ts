import { shallowRef} from "vue";

import type { SchuelerSchulbesuchsdaten} from "@core";

import { api } from "~/router/Api";


interface RouteStateDataSchuelerSchulbesuch {
	daten: SchuelerSchulbesuchsdaten | undefined;
	idSchueler: number | undefined;
}

export class RouteDataSchuelerSchulbesuch {

	private static _defaultState: RouteStateDataSchuelerSchulbesuch = {
		daten: undefined,
		idSchueler: undefined,
	}

	private _state = shallowRef(RouteDataSchuelerSchulbesuch._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateDataSchuelerSchulbesuch>) {
		this._state.value = Object.assign({ ... RouteDataSchuelerSchulbesuch._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateDataSchuelerSchulbesuch>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	get daten(): SchuelerSchulbesuchsdaten {
		if (this._state.value.daten === undefined)
			throw new Error("Beim Zugriff auf die Daten sind noch keine gültigen Daten geladen.");
		return this._state.value.daten;
	}

	public async setEintrag(idSchueler?: number) {
		if (idSchueler === undefined || idSchueler === this._state.value.idSchueler)
			return;
		const daten = await api.server.getSchuelerSchulbesuch(api.schema, idSchueler);
		this.setPatchedState({idSchueler, daten});
	}

	patch = async (data : Partial<SchuelerSchulbesuchsdaten>) => {
		await api.server.patchSchuelerSchulbesuch(data, api.schema, this.daten.id);
	}
}

