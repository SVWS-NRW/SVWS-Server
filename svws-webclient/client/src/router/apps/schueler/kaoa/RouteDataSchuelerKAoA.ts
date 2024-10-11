import { RouteData, type RouteStateInterface } from "~/router/RouteData";

import { List, SchuelerListeEintrag, SchuelerKAoADaten, OpenApiError } from "@core";
import { ArrayList, SchuelerKAoAManager, DeveloperNotificationException } from "@core";
import { api } from "~/router/Api";
import { routeApp } from "../../RouteApp";

interface RouteStateSchuelerKAoA extends RouteStateInterface {
	auswahl: SchuelerListeEintrag | undefined;
	schuelerKAoAManager: SchuelerKAoAManager | undefined;
}

const defaultState = <RouteStateSchuelerKAoA> {
	auswahl: undefined,
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

	addKaoaDaten = async (data : Partial<SchuelerKAoADaten>, id : number) => {
		api.status.start()
		try {
			const schuelerKAoADaten = await api.server.addKAoAdaten(data, api.schema, id);
			this.schuelerKaoaManager.addKaoaDaten(schuelerKAoADaten)
			this.commit()
		} catch (error: OpenApiError) {
			throw new OpenApiError(error, error.toString() + " Fehlercode: " + error.response?.status);
		}
		api.status.stop()
	}

	deleteKaoaDaten = async (idSchueler: number, idKaoaEntry: number) => {
		api.status.start()
		try {
			await api.server.deleteKAoAdaten(api.schema, idSchueler, idKaoaEntry)
			this.schuelerKaoaManager.deleteKaoaDaten(idKaoaEntry)
			this.commit()
		} catch (error: OpenApiError) {
			throw new OpenApiError(error, error.toString() + " Fehlercode: " + error.response?.status);
		}
		api.status.stop()
	}


	public async ladeDaten(auswahl: SchuelerListeEintrag | null) {
		if (auswahl === this._state.value.auswahl)
			return;
		if ((auswahl === null) || (auswahl === undefined))
			this.setDefaultState();
		else {
			try {
				const data: List<SchuelerKAoADaten> = await api.server.getKAoAdaten(api.schema, auswahl.id);
				const schuelerKAoAManager = new SchuelerKAoAManager(routeApp.data.aktAbschnitt.value.id, api.abschnitt.id, api.schuleStammdaten.abschnitte, api.schulform, data, new ArrayList());
				this.setPatchedState({auswahl, schuelerKAoAManager});
			} catch (error) {
				throw new DeveloperNotificationException("Die KAoA-Daten konnten nicht eingeholt werden, sind für diesen Schüler KAoA-Daten möglich?");
			}
		}
	}
}
