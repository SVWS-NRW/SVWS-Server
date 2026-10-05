import type { SchuelerLernplattform } from "@core/core/data/schueler/SchuelerLernplattform";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";


interface RouteStateSchuelerLernplattformen extends RouteStateInterface {
	auswahl: SchuelerListeEintrag | undefined;
	schuelerLernplattformen: List<SchuelerLernplattform>;
}

const defaultState = <RouteStateSchuelerLernplattformen>{
	auswahl: undefined,
	schuelerLernplattformen: new ArrayList(),
};

export class RouteDataSchuelerLernplattformen extends RouteData<RouteStateSchuelerLernplattformen> {

	public constructor() {
		super(defaultState);
	}

	get auswahl(): SchuelerListeEintrag {
		if (this._state.value.auswahl === undefined) {
			throw new DeveloperNotificationException("Unerwarteter Fehler: Schülerauswahl nicht festgelegt, es können keine Informationen zu Lernplattformen abgerufen oder eingegeben werden.");
		}
		return this._state.value.auswahl;
	}

	get schuelerLernplattformen(): List<SchuelerLernplattform> {
		return this._state.value.schuelerLernplattformen;
	}

	patch = async (data: Partial<SchuelerLernplattform> | undefined, idLernplattform: number) => {
		if (data === undefined) {
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		}
		api.status.start();

		await api.server.patchSchuelerLernplattform(data, api.schema, this.auswahl.id, idLernplattform);

		for (const lernplattform of this.schuelerLernplattformen) {
			if (lernplattform.idLernplattform === idLernplattform) {
				Object.assign(lernplattform, data);
			}
		}
		this.commit();
		api.status.stop();
		return true;
	};

	public async ladeDaten(auswahl: SchuelerListeEintrag | null | undefined) {
		if ((auswahl === null) || (auswahl === undefined)) {
			this.setPatchedDefaultState({});
		} else {
			const schuelerLernplattformen = await api.server.getSchuelerLernplattformen(api.schema, auswahl.id);
			this.setPatchedDefaultState({ auswahl, schuelerLernplattformen });
		}
	}

}

