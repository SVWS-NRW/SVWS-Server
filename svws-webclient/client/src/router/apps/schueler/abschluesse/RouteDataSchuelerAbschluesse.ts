import type { SchuelerListeEintrag } from "@core";
import { DeveloperNotificationException } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";


interface RouteStateSchuelerAbschluesse extends RouteStateInterface {
	auswahl: SchuelerListeEintrag | undefined;
}

const defaultState = <RouteStateSchuelerAbschluesse> {
	auswahl: undefined,
};

export class RouteDataSchuelerAbschluesse extends RouteData<RouteStateSchuelerAbschluesse> {

	public constructor() {
		super(defaultState);
	}

	public async clear() {
		this.setPatchedDefaultState({});
	}

	get auswahl(): SchuelerListeEintrag {
		if (this._state.value.auswahl === undefined)
			throw new DeveloperNotificationException("Unerwarteter Fehler: Schülerauswahl nicht festgelegt, es können keine Informationen zur den Sprachen abgerufen oder eingegeben werden.");
		return this._state.value.auswahl;
	}

	public async auswahlSchueler(auswahl: SchuelerListeEintrag | null) {
		if (auswahl === this._state.value.auswahl)
			return;
		if (auswahl === null) {
			this.setPatchedDefaultState({});
			return;
		}
		try {
			api.status.start();
			// TODO
			this.setPatchedState({ auswahl })
		} catch(error) {
			throw new DeveloperNotificationException("Die Informationen zu den Abschlüssen und Berechtigungen konnten nicht geladen werden.")
		}
		api.status.stop();
	}

}
