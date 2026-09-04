import type { SchuelerSchulbesuchMerkmal } from "@core/asd/data/schueler/SchuelerSchulbesuchMerkmal";
import type { SchuelerSchulbesuchSchule } from "@core/asd/data/schueler/SchuelerSchulbesuchSchule";
import type { SchuelerSchulbesuchsdaten } from "@core/asd/data/schueler/SchuelerSchulbesuchsdaten";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import { SchuelerSchulbesuchManager } from "@ui/ui/manager/schueler/SchuelerSchulbesuchManager";
import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { routeApp } from "~/router/apps/RouteApp";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";

interface RouteStateDataSchuelerSchulbesuch extends RouteStateInterface {
	schueler: SchuelerListeEintrag | undefined;
	manager: SchuelerSchulbesuchManager | undefined;
	bisherigeSchulen: List<SchuelerSchulbesuchSchule>;
}

const defaultState = <RouteStateDataSchuelerSchulbesuch> {
	schueler: undefined,
	manager: undefined,
	bisherigeSchulen: new ArrayList(),
};

export class RouteDataSchuelerSchulbesuch extends RouteData<RouteStateDataSchuelerSchulbesuch> {

	public constructor() {
		super(defaultState);
	}

	public async ladeDaten() {
		const manager = await this.createManager();
		this.setPatchedState({ manager });
	}

	private async createManager() {
		const idSchueler = routeSchueler.data.manager.auswahlID() ?? -1;
		const idSchuljahresabschnitt = routeSchueler.data.manager.auswahl().idSchuljahresabschnitt;
		const data: SchuelerSchulbesuchsdaten = await api.server.getSchuelerSchulbesuch(api.schema, idSchueler);
		return new SchuelerSchulbesuchManager(
			data,
			idSchueler,
			idSchuljahresabschnitt,
			{
				schulenById: routeApp.cache.kataloge.schulenById,
				merkmaleById: routeApp.cache.kataloge.merkmaleById,
				entlassgruendeById: routeApp.cache.kataloge.entlassgruendeById,
				kindergaertenById: routeApp.cache.kataloge.kindergaertenById,
				jahrgaengeById: routeApp.cache.kataloge.jahrgaengeById,
				abschnitteById: this.mapSchuljahresabschnitte(),
			}
		);
	}

	private mapSchuljahresabschnitte() {
		const abschnitteById = new Map();
		for (const abschnitt of abschnittStateImpl.alle) {
			abschnitteById.set(abschnitt.id, abschnitt);
		}
		return abschnitteById;
	}

	get manager(): SchuelerSchulbesuchManager {
		if (this._state.value.manager === undefined) {
			throw new DeveloperNotificationException("SchülerSchulbesuchManager nicht initialisiert.");
		}
		return this._state.value.manager;
	}

	patch = async (idSchulbesuch: number, data: Partial<SchuelerSchulbesuchsdaten>): Promise<boolean> => {
		await api.server.patchSchuelerSchulbesuch(data, api.schema, idSchulbesuch);
		Object.assign(this.manager.daten, data);
		this.commit();
		return true;
	};

	addBisherigeSchule = async (data: Partial<SchuelerSchulbesuchSchule>): Promise<void> => {
		const result = await api.server.addBisherigeSchule(data, api.schema);
		this.manager.addBisherigeSchule(result);
		this.commit();
	};

	patchBisherigeSchule = async (data: Partial<SchuelerSchulbesuchSchule>): Promise<void> => {
		if (data.id === undefined) {
			return;
		}
		await api.server.patchBisherigeSchule(data, api.schema, data.id);
		this.manager.patchBisherigeSchuleById(data.id, data);
		this.commit();
	};

	deleteBisherigeSchulen = async (ids: List<number>): Promise<void> => {
		await api.server.deleteBisherigeSchulen(ids, api.schema);
		for (const id of ids) {
			this.manager.deleteBisherigeSchuleById(id);
		}
		this.commit();
	};

	addMerkmal = async (data: Partial<SchuelerSchulbesuchMerkmal>): Promise<void> => {
		const result = await api.server.addSchuelerMerkmal(data, api.schema);
		this.manager.addMerkmal(result);
		this.commit();
	};

	patchMerkmal = async (data: Partial<SchuelerSchulbesuchMerkmal>): Promise<void> => {
		if (data.id === undefined) {
			return;
		}
		await api.server.patchSchuelerMerkmal(data, api.schema, data.id);
		this.manager.patchMerkmalById(data.id, data);
		this.commit();
	};

	deleteMerkmale = async (ids: List<number>): Promise<void> => {
		await api.server.deleteSchuelerMerkmale(ids, api.schema);
		for (const id of ids) {
			this.manager.deleteMerkmal(id);
		}
		this.commit();
	};

}

