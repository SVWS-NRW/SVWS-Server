import { shallowRef } from "vue";

import type { List, SchuelerLernabschnittListeEintrag} from "@core";
import { ArrayList } from "@core";

import { api } from "~/router/Api";

import { routeApp } from "~/router/apps/RouteApp";

interface RoutetateDataSchuelerAbschnitt {
	idSchueler: number | undefined;
	listAbschnitte: List<SchuelerLernabschnittListeEintrag>;
}

export class RouteDataSchuelerAbschnitt {

	private static _defaultState: RoutetateDataSchuelerAbschnitt = {
		idSchueler: undefined,
		listAbschnitte: new ArrayList<SchuelerLernabschnittListeEintrag>(),
	}

	private _state = shallowRef(RouteDataSchuelerAbschnitt._defaultState);

	private setPatchedDefaultState(patch: Partial<RoutetateDataSchuelerAbschnitt>) {
		this._state.value = Object.assign({ ...RouteDataSchuelerAbschnitt._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RoutetateDataSchuelerAbschnitt>) {
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
		this.setPatchedState({ idSchueler, listAbschnitte })
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

}
