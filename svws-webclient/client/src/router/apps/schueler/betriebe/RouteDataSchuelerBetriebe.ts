import type { List, SchuelerBetrieb, SchuelerListeEintrag } from "@core";
import { DeveloperNotificationException } from "@core";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { SchuelerBetriebeManager } from "@ui";
import { api } from "~/router/Api";
import { routeApp } from "~/router/apps/RouteApp";
import { RouteManager } from "~/router/RouteManager";
import { routeBetriebe } from "~/router/apps/schule/kataloge/betriebe/RouteBetriebe";

interface RouteStateSchuelerBetriebe extends RouteStateInterface {
	auswahl: SchuelerListeEintrag | undefined;
	manager: SchuelerBetriebeManager | undefined;
}

const defaultState = <RouteStateSchuelerBetriebe> {
	auswahl: undefined,
	manager: undefined,
};

export class RouteDataSchuelerBetriebe extends RouteData<RouteStateSchuelerBetriebe> {

	public constructor() {
		super(defaultState);
	}

	public async ladeDaten(auswahl: SchuelerListeEintrag | null) {
		if (auswahl === this._state.value.auswahl) {
			return;
		}
		if (auswahl === null) {
			this.setDefaultState();
			return;
		}
		const [schuelerBetriebe, ansprechpartner, lehrer] = await Promise.all([
			api.server.getSchuelerBetriebe(api.schema, auswahl.id),
			api.server.getBetriebAnsprechpartner(api.schema),
			api.server.getLehrer(api.schema),
		]);
		const manager = new SchuelerBetriebeManager(
			auswahl.id,
			schuelerBetriebe,
			ansprechpartner,
			lehrer,
			routeApp.cache.kataloge.betriebeById,
			routeApp.cache.kataloge.beschaeftigungsartenById
		);
		this.setPatchedState({ auswahl, manager });
	}

	add = async (data: Partial<SchuelerBetrieb>): Promise<SchuelerBetrieb> => {
		const result = await api.server.addSchuelerBetrieb(data, api.schema);
		this.manager.schuelerBetriebeById.set(result.id, result);
		this.commit();
		return result;
	};

	patch = async (idEntry: number, data: Partial<SchuelerBetrieb>): Promise<boolean> => {
		await api.server.patchSchuelerBetrieb(data, api.schema, idEntry);
		const entry = this.manager.schuelerBetriebeById.get(idEntry);
		if (entry !== undefined) {
			Object.assign(entry, data);
		}
		this.commit();
		return true;
	};

	delete = async (ids: List<number>): Promise<boolean> => {
		await api.server.deleteSchuelerBetriebe(ids, api.schema);
		for (const id of ids) {
			this.manager.schuelerBetriebeById.delete(id);
		}
		this.commit();
		return true;
	};

	get manager(): SchuelerBetriebeManager {
		if (this._state.value.manager === undefined) {
			throw new DeveloperNotificationException("Unerwarteter Fehler: SchuelerBetriebeManager Daten nicht initialisiert");
		}
		return this._state.value.manager;
	}

	get auswahl(): SchuelerListeEintrag {
		if (this._state.value.auswahl === undefined) {
			throw new DeveloperNotificationException("Unerwarteter Fehler: Schülerauswahl nicht festgelegt, es können keine Informationen zu KAoA-Daten abgerufen oder eingegeben werden.");
		}
		return this._state.value.auswahl;
	}

	goToBetrieb = async (idBetrieb: number) => {
		await RouteManager.doRoute(routeBetriebe.getRoute({ id: idBetrieb }));
	};

}

