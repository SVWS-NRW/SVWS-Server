import { shallowRef } from "vue";

import type { StundenplanListeEintrag} from "@core";
import { SchuelerStundenplanManager } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { routeApp } from "~/router/apps/RouteApp";
import { routeSchueler } from "~/router/apps/RouteSchueler";

import { routeSchuelerStundenplanDaten } from "~/router/apps/schueler/stundenplan/RouteSchuelerStundenplanDaten";


interface RouteStateSchuelerDataStundenplan {
	auswahl: StundenplanListeEintrag | undefined;
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	manager: SchuelerStundenplanManager | undefined;
}

export class RouteDataSchuelerStundenplan {

	private static _defaultState: RouteStateSchuelerDataStundenplan = {
		auswahl: undefined,
		mapStundenplaene: new Map(),
		manager: undefined,
	}

	private _state = shallowRef(RouteDataSchuelerStundenplan._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateSchuelerDataStundenplan>) {
		this._state.value = Object.assign({ ... RouteDataSchuelerStundenplan._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateSchuelerDataStundenplan>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	get auswahl(): StundenplanListeEintrag {
		if (this._state.value.auswahl === undefined)
			throw new Error("Unerwarteter Fehler: Stundenplaneintrag nicht festgelegt, es können keine Informationen zum Stundenplan abgerufen oder eingegeben werden.");
		return this._state.value.auswahl;
	}

	get manager(): SchuelerStundenplanManager | undefined {
		return this._state.value.manager;
	}

	get mapStundenplaene(): Map<number, StundenplanListeEintrag> {
		return this._state.value.mapStundenplaene;
	}

	public async ladeListe() {
		const listStundenplaene = await api.server.getStundenplanlisteFuerAbschnitt(api.schema, routeApp.data.aktAbschnitt.value.id);
		const mapStundenplaene = new Map<number, StundenplanListeEintrag>();
		const auswahl = listStundenplaene.size() > 0 ? listStundenplaene.get(0) : undefined;
		for (const l of listStundenplaene)
			mapStundenplaene.set(l.id, l);
		this.setPatchedDefaultState({ auswahl, mapStundenplaene });
	}

	public async setEintrag(idSchueler: number, idStundenplan?: number) {
		if (((idStundenplan === undefined) && (this.manager === undefined)) ||
		((this.manager !== undefined) && (this.manager.getSchuelerID() === idSchueler) && (this.manager.getStundenplanID() === idStundenplan)))
			return;
		const auswahl = this.mapStundenplaene.get(idStundenplan || -1);
		if (auswahl !== undefined) {
			const daten = await api.server.getSchuelerStundenplan(api.schema, auswahl.id, idSchueler);
			const manager = new SchuelerStundenplanManager(daten);
			this.setPatchedState({auswahl, manager});
		} else this.setPatchedState({auswahl: undefined, manager: undefined})
	}

	public gotoStundenplan = async (value: StundenplanListeEintrag | undefined) => {
		await RouteManager.doRoute({ name: routeSchuelerStundenplanDaten.name, params: { id: routeSchueler.data.stammdaten.id, idStundenplan: value?.id } });
	}

}

