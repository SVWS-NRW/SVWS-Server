import { DeveloperNotificationException } from "@core";
import type { SchuelerSchulbesuchsdaten, SchuelerListeEintrag } from "@core";

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

	patch = async (data : Partial<SchuelerSchulbesuchsdaten>) : Promise<boolean> => {
		await api.server.patchSchuelerSchulbesuch(data, api.schema, this.auswahl.id);
		return true;
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

