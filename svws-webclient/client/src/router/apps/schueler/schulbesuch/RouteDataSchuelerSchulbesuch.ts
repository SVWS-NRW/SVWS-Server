import type { SchuelerSchulbesuchsdaten} from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";

interface RouteStateDataSchuelerSchulbesuch extends RouteStateInterface {
	daten: SchuelerSchulbesuchsdaten | undefined;
	idSchueler: number | undefined;
}

const defaultState = <RouteStateDataSchuelerSchulbesuch> {
	daten: undefined,
	idSchueler: undefined,
};

export class RouteDataSchuelerSchulbesuch extends RouteData<RouteStateDataSchuelerSchulbesuch> {

	public constructor() {
		super(defaultState);
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

