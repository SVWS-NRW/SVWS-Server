import { RouteData, type RouteStateInterface } from "~/router/RouteData";

import type { List, SchuelerListeEintrag, SchuelerKAoADaten} from "@core";
import { ArrayList, SchuelerKAoAManager, DeveloperNotificationException } from "@core";
import { api } from "~/router/Api";
import { routeApp } from "../../RouteApp";


interface RouteStateSchuelerKAoA extends RouteStateInterface {
	auswahl: SchuelerListeEintrag | undefined;
	data: List<SchuelerKAoADaten>;
	schuelerKAoAManager: SchuelerKAoAManager | undefined;
}

const defaultState = <RouteStateSchuelerKAoA> {
	auswahl: undefined,
	data: new ArrayList(),
	schuelerKAoAManager: undefined,
};

export class RouteDataSchuelerKAoA extends RouteData<RouteStateSchuelerKAoA> {

	public constructor() {
		super(defaultState);
	}

	get auswahl(): SchuelerListeEintrag {
		if (this._state.value.auswahl === undefined)
			throw new DeveloperNotificationException("Unerwarteter Fehler: Schülerauswahl nicht festgelegt, es können keine Informationen zu KAoA-Daten abgerufen oder eingegeben werden.");
		return this._state.value.auswahl;
	}

	get data(): List<SchuelerKAoADaten> {
		// if (this._state.value.data === undefined)
		// 	throw new DeveloperNotificationException("Unerwarteter Fehler: Schülerauswahl nicht festgelegt, es können keine Informationen zu KAoA-Daten abgerufen oder eingegeben werden.");
		return this._state.value.data
	}

	get schuelerKaoaManager(): SchuelerKAoAManager {
		if (this._state.value.schuelerKAoAManager === undefined)
			throw new DeveloperNotificationException("Unerwarteter Fehler: Schüler-KAoA Daten nicht initialisiert");
		return this._state.value.schuelerKAoAManager;
	}

	patch = async (data : Partial<SchuelerKAoADaten>) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		console.log("TODO: Implementierung patch KAoA", data);
	}


	public async ladeDaten(auswahl: SchuelerListeEintrag | null) {
		if (auswahl === this._state.value.auswahl)
			return;
		if ((auswahl === null) || (auswahl === undefined))
			this.setPatchedDefaultState({});
		else {
			try {
				const data: List<SchuelerKAoADaten> = await api.server.getKAOAdaten(api.schema, auswahl.id);
				// TODO
				const schuelerKAoAManager = new SchuelerKAoAManager(routeApp.data.aktAbschnitt.value.id, api.abschnitt.id, api.schuleStammdaten.abschnitte, api.schulform, data, new ArrayList());
				this.setPatchedState({ auswahl, data, schuelerKAoAManager });
			} catch(error) {
				throw new DeveloperNotificationException("Die KAoA-Daten konnten nicht eingeholt werden, sind für diesen Schüler KAoA-Daten möglich?");
			}
		}
	}
}
