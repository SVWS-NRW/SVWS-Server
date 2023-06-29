import { shallowRef } from "vue";

import type { Erzieherart, ErzieherStammdaten, List} from "@core";

import { api } from "~/router/Api";


interface RouteStateDataSchuelerErziehungsberechtigte {
	daten: List<ErzieherStammdaten> | undefined;
	idSchueler: number | undefined;
	mapErzieherarten: Map<number, Erzieherart>;
}

export class RouteDataSchuelerErziehungsberechtigte {

	private static _defaultState: RouteStateDataSchuelerErziehungsberechtigte = {
		daten: undefined,
		idSchueler: undefined,
		mapErzieherarten: new Map(),
	}

	private _state = shallowRef(RouteDataSchuelerErziehungsberechtigte._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateDataSchuelerErziehungsberechtigte>) {
		this._state.value = Object.assign({ ... RouteDataSchuelerErziehungsberechtigte._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateDataSchuelerErziehungsberechtigte>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	get daten(): List<ErzieherStammdaten> {
		if (this._state.value.daten === undefined)
			throw new Error("Beim Zugriff auf die Daten sind noch keine gültigen Daten geladen.");
		return this._state.value.daten;
	}

	public async setEintrag(idSchueler?: number) {
		if (idSchueler === undefined || idSchueler === this._state.value.idSchueler)
			return;
		const daten = await api.server.getSchuelerErzieher(api.schema, idSchueler);
		this.setPatchedState({idSchueler, daten});
	}

	public get mapErzieherarten() : Map<number, Erzieherart> {
		if (this._state.value.mapErzieherarten.size === 0)
			throw new Error("Zugriff auf den Katalog der Erzieherarten, bevor dieser geladen werden konnte.");
		return this._state.value.mapErzieherarten;
	}

	public async ladeListe() {
		const listErzieherarten = await api.server.getErzieherArten(api.schema);
		const mapErzieherarten = new Map<number, Erzieherart>();
		for (const e of listErzieherarten)
			mapErzieherarten.set(e.id, e);
		this.setPatchedDefaultState({ mapErzieherarten })
	}

	patch = async (data : Partial<ErzieherStammdaten>, id: number) => {
		await api.server.patchErzieherStammdaten(data, api.schema, id);
	}

}

