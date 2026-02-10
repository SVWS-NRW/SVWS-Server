import { RouteData, type RouteStateInterface } from "~/router/RouteData";

import type { SchuelerListeEintrag, SchuelerLernabschnittListeEintrag, SchuelerKAoADaten, List } from "@core";
import { DeveloperNotificationException } from "@core";


import { api } from "~/router/Api";
import { SchuelerKAoAManager } from "@ui";

interface RouteStateSchuelerKAoA extends RouteStateInterface {
	auswahl: SchuelerListeEintrag | undefined;
	manager: SchuelerKAoAManager | undefined;
}

const defaultState = <RouteStateSchuelerKAoA> {
	auswahl: undefined,
	manager: undefined,
};

export class RouteDataSchuelerKAoA extends RouteData<RouteStateSchuelerKAoA> {

	public constructor() {
		super(defaultState);
	}

	public async ladeDaten(auswahl: SchuelerListeEintrag | null) {
		if (auswahl === this._state.value.auswahl) {
			return;
		}
		if (auswahl === null) {
			this.setDefaultState();
		} else {
			try {
				const kAoADaten: List<SchuelerKAoADaten> = await api.server.getKAoAdaten(api.schema, auswahl.id);
				const lernabschnitte: List<SchuelerLernabschnittListeEintrag> = await api.server.getSchuelerLernabschnittsliste(api.schema, auswahl.id);
				const manager = new SchuelerKAoAManager(kAoADaten, api.schuleStammdaten.abschnitte, lernabschnitte);
				this.setPatchedState({ auswahl, manager });
			} catch (error) {
				throw new DeveloperNotificationException("Die KAoA-Daten konnten nicht eingeholt werden, sind für diesen Schüler KAoA-Daten möglich?");
			}
		}
	}

	add = async (data: Partial<SchuelerKAoADaten>, id: number) => {
		const result = await api.server.addKAoAdaten(data, api.schema, id);
		this.manager.kAoADatenById.put(result.id, result);
		this.commit();
	};

	patch = async (data: Partial<SchuelerKAoADaten>, idKaoaEntry: number) => {
		await api.server.patchKAoADaten(data, api.schema, this.auswahl.id, idKaoaEntry);
		const kaoaDaten = this.manager.kAoADatenById.get(idKaoaEntry);
		if (kaoaDaten !== null) {
			Object.assign(kaoaDaten, data);
		}
		this.commit();
	};

	delete = async (idSchueler: number, idKaoaEntry: number) => {
		await api.server.deleteKAoAdaten(api.schema, idSchueler, idKaoaEntry);
		this.manager.kAoADatenById.remove(idKaoaEntry);
		this.commit();
	};

	get auswahl(): SchuelerListeEintrag {
		if (this._state.value.auswahl === undefined) {
			throw new DeveloperNotificationException("Unerwarteter Fehler: Schülerauswahl nicht festgelegt, es können keine Informationen zu KAoA-Daten abgerufen oder eingegeben werden.");
		}
		return this._state.value.auswahl;
	}

	get manager(): SchuelerKAoAManager {
		if (this._state.value.manager === undefined) {
			throw new DeveloperNotificationException("Unerwarteter Fehler: Schüler-KAoA Daten nicht initialisiert");
		}
		return this._state.value.manager;
	}

}
