import { RouteData, type RouteStateInterface } from "~/router/RouteData";

import type { List, SchuelerListeEintrag , Lernplattform, SchuelerLernplattform } from "@core";
import { ArrayList, DeveloperNotificationException } from "@core";
import { api } from "~/router/Api";


interface RouteStateSchuelerLernplattformen extends RouteStateInterface {
	auswahl: SchuelerListeEintrag | undefined;
	schuelerLernplattformen: List<SchuelerLernplattform>;
	mapLernplattformen: Map<number, Lernplattform>;
}

const defaultState = <RouteStateSchuelerLernplattformen>{
	auswahl: undefined,
	schuelerLernplattformen: new ArrayList(),
	mapLernplattformen: new Map(),
};

export class RouteDataSchuelerLernplattformen extends RouteData<RouteStateSchuelerLernplattformen> {

	public constructor() {
		super(defaultState);
	}

	get auswahl(): SchuelerListeEintrag {
		if (this._state.value.auswahl === undefined)
			throw new DeveloperNotificationException("Unerwarteter Fehler: Schülerauswahl nicht festgelegt, es können keine Informationen zu Lernplattformen abgerufen oder eingegeben werden.");
		return this._state.value.auswahl;
	}

	get schuelerLernplattformen(): List<SchuelerLernplattform> {
		return this._state.value.schuelerLernplattformen;
	}

	get mapLernplattformen(): Map<number, Lernplattform> {
		return this._state.value.mapLernplattformen;
	}

	patch = async (data : Partial<SchuelerLernplattform> | undefined, idLernplattform: number) => {
		if (data === undefined)
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		api.status.start();

		await api.server.patchSchuelerLernplattform(data, api.schema, this.auswahl.id, idLernplattform);

		for (const lernplattform of this.schuelerLernplattformen)
			if (lernplattform.idLernplattform === idLernplattform)
				Object.assign(lernplattform, data);
		this.commit();
		api.status.stop();
	}

	public async ladeDaten(auswahl: SchuelerListeEintrag | null | undefined) {
		if ((auswahl === null) || (auswahl === undefined)) {
			this.setPatchedDefaultState({});
		} else {
			const schuelerLernplattformen = await api.server.getSchuelerLernplattformen(api.schema, auswahl.id);
			const lernplattformen = await api.server.getLernplattformen(api.schema);
			const mapLernplattformen = new Map();
			for (const lp of lernplattformen)
				mapLernplattformen.set(lp.id, lp);
			this.setPatchedDefaultState({ auswahl, schuelerLernplattformen, mapLernplattformen });
		}
	}

}

