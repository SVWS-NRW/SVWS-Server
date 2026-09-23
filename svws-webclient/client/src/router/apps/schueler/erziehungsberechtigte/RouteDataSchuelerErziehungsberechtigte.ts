import type { ErzieherStammdaten } from "@core/core/data/erzieher/ErzieherStammdaten";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import type { List } from "@core/java/util/List";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";


interface RouteStateDataSchuelerErziehungsberechtigte extends RouteStateInterface {
	daten: List<ErzieherStammdaten> | undefined;
	idSchueler: number | undefined;
}

const defaultState = <RouteStateDataSchuelerErziehungsberechtigte> {
	daten: undefined,
	idSchueler: undefined,
};

export class RouteDataSchuelerErziehungsberechtigte extends RouteData<RouteStateDataSchuelerErziehungsberechtigte> {

	public constructor() {
		super(defaultState);
	}

	get daten(): List<ErzieherStammdaten> {
		if (this._state.value.daten === undefined) {
			throw new DeveloperNotificationException("Beim Zugriff auf die Daten sind noch keine gültigen Daten geladen.");
		}
		return this._state.value.daten;
	}

	get idSchueler(): number {
		if (this._state.value.idSchueler === undefined) {
			throw new DeveloperNotificationException("Beim Zugriff auf die Daten sind noch keine gültigen Daten geladen.");
		}
		return this._state.value.idSchueler;
	}

	public async setEintrag(idSchueler?: number) {
		if (idSchueler === undefined || idSchueler === this._state.value.idSchueler) {
			return;
		}
		const daten = await api.server.getSchuelerErzieher(api.schema, idSchueler);
		this.setPatchedState({ idSchueler, daten });
	}

	patchErzieher = async (data: Partial<ErzieherStammdaten>, id: number) => {
		await api.server.patchErzieherStammdaten(data, api.schema, id);
		const daten = await api.server.getSchuelerErzieher(api.schema, this.idSchueler);
		this.setPatchedState({ daten });
	};

	patchErzieherAnPosition = async (data: Partial<ErzieherStammdaten>, id: number, pos: number) => {
		await api.server.patchErzieherStammdatenZweitePosition(data, api.schema, id, pos);
		const daten = await api.server.getSchuelerErzieher(api.schema, this.idSchueler);
		this.setPatchedState({ daten });
	};

	addErzieher = async (data: Partial<ErzieherStammdaten>, pos: number): Promise<ErzieherStammdaten> => {
		const neu = await api.server.addSchuelerErzieher(data, api.schema, this.idSchueler, pos);
		const daten = await api.server.getSchuelerErzieher(api.schema, this.idSchueler);
		this.setPatchedState({ daten });
		return neu;
	};

	deleteErzieher = async (idEintraege: List<number>): Promise<void> => {
		api.status.start();
		await api.server.deleteErzieherStammdaten(idEintraege, api.schema);
		const daten = await api.server.getSchuelerErzieher(api.schema, this.idSchueler);
		this.setPatchedState({ daten });
		api.status.stop();
	};

}

