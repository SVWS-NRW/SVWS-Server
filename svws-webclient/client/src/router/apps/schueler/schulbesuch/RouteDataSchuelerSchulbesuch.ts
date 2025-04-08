import { DeveloperNotificationException } from "@core";
import type { SchuelerSchulbesuchsdaten, SchuelerListeEintrag, SchuelerSchulbesuchSchule, SchuelerSchulbesuchMerkmal, List } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { SchuelerSchulbesuchManager } from "~/components/schueler/schulbesuch/SchuelerSchulbesuchManager";

interface RouteStateDataSchuelerSchulbesuch extends RouteStateInterface {
	auswahl: SchuelerListeEintrag | undefined;
	schuelerSchulbesuchManager: SchuelerSchulbesuchManager | undefined;
}

const defaultState = <RouteStateDataSchuelerSchulbesuch> {
	auswahl: undefined,
	schuelerSchulbesuchManager: undefined,
};

export class RouteDataSchuelerSchulbesuch extends RouteData<RouteStateDataSchuelerSchulbesuch> {

	public constructor() {
		super(defaultState);
	}

	get auswahl(): SchuelerListeEintrag {
		if (this._state.value.auswahl === undefined)
			throw new DeveloperNotificationException("Beim Zugriff auf die Daten sind noch keine gültigen Daten geladen.");
		return this._state.value.auswahl;
	}

	get schuelerSchulbesuchManager(): SchuelerSchulbesuchManager {
		if (this._state.value.schuelerSchulbesuchManager === undefined)
			throw new DeveloperNotificationException("SchülerSchulbesuchManager nicht initialisiert.")
		return this._state.value.schuelerSchulbesuchManager;
	}

	patch = async (data: Partial<SchuelerSchulbesuchsdaten>) : Promise<void> => {
		await api.server.patchSchuelerSchulbesuch(data, api.schema, this.auswahl.id);
	}

	addSchuelerSchulbesuchSchule = async (data: Partial<SchuelerSchulbesuchSchule>) : Promise<void> => {
		const result = await api.server.addBisherigeSchule(data, api.schema, this.auswahl.id);
		this.schuelerSchulbesuchManager.addSchuelerSchulbesuchSchule(result);
		this.commit();
	}

	patchSchuelerSchulbesuchSchule = async (id: number, data: Partial<SchuelerSchulbesuchSchule>) : Promise<void> => {
		await api.server.patchBisherigeSchule(data, api.schema, id)
		this.schuelerSchulbesuchManager.patchBisherigeSchuleById(id, data);
		this.commit();
	}

	deleteSchuelerSchulbesuchSchulen = async (ids: List<number>) : Promise<void> => {
		await api.server.deleteBisherigeSchulen(ids, api.schema);
		for (const id of ids)
			this.schuelerSchulbesuchManager.deleteBisherigeSchuleById(id);
		this.commit();
	}

	addSchuelerSchulbesuchMerkmal = async (data: Partial<SchuelerSchulbesuchMerkmal>) : Promise<void> => {
		const result = await api.server.addSchuelerMerkmal(data, api.schema, this.auswahl.id);
		this.schuelerSchulbesuchManager.addSchuelerSchulbesuchMerkmal(result);
		this.commit();
	}

	patchSchuelerSchulbesuchMerkmal = async (id: number, data: Partial<SchuelerSchulbesuchMerkmal>) : Promise<void> => {
		await api.server.patchSchuelerMerkmal(data, api.schema, id);
		this.schuelerSchulbesuchManager.patchSchuelerSchulbesuchMerkmalById(id, data);
		this.commit();
	}

	deleteSchuelerSchulbesuchMerkmale = async (ids: List<number>) : Promise<void> => {
		await api.server.deleteSchuelerMerkmale(ids, api.schema);
		for (const id of ids)
			this.schuelerSchulbesuchManager.deleteSchuelerSchulbesuchMerkmal(id);
		this.commit();
	}

	public async ladeDaten(auswahl: SchuelerListeEintrag | null) {
		if (auswahl === this._state.value.auswahl)
			return;
		if (auswahl === null)
			this.setDefaultState();
		else {
			try {
				const data: SchuelerSchulbesuchsdaten = await api.server.getSchuelerSchulbesuch(api.schema, auswahl.id);
				const schulen = await api.server.getSchulen(api.schema);
				const merkmale = await api.server.getMerkmale(api.schema);
				const entlassgruende = await api.server.getEntlassgruende(api.schema);
				const schuelerSchulbesuchManager = new SchuelerSchulbesuchManager(
					data, auswahl, api.schuleStammdaten.abschnitte, schulen, merkmale, entlassgruende, this.patch);
				this.setPatchedState({auswahl, schuelerSchulbesuchManager});
			} catch (error) {
				throw new DeveloperNotificationException("Fehler beim Erzeugen des SchuelerSchulbesuchManagers");
			}
		}
	}

}

