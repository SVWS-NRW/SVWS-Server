import { RouteData, type RouteStateInterface } from "~/router/RouteData";

import type {List, SchuelerListeEintrag, SchulformKatalogEintrag} from "@core";
import {DeveloperNotificationException, Schueler, SchuelerVermerke, Schulform} from "@core";
import {api} from "~/router/Api";
import {routeApp} from "~/router/apps/RouteApp";


interface RouteStateSchuelerVermerke extends RouteStateInterface {
	auswahl: SchuelerListeEintrag | undefined;
	data: List<SchuelerVermerke>;
}

const defaultState = <RouteStateSchuelerVermerke> {
	data: [],
};

export class RouteDataSchuelerVermerke extends RouteData<RouteStateSchuelerVermerke> {

	public constructor() {
		super(defaultState);
	}

	get auswahl(): SchuelerListeEintrag {
		if (this._state.value.auswahl === undefined)
			throw new DeveloperNotificationException("Unerwarteter Fehler: Schülerauswahl nicht festgelegt, es können keine Informationen zu KAoA-Daten abgerufen oder eingegeben werden.");
		return this._state.value.auswahl;
	}

	get data(): List<SchuelerVermerke> {
		// if (this._state.value.data === undefined)
		// 	throw new DeveloperNotificationException("Unerwarteter Fehler: Schülerauswahl nicht festgelegt, es können keine Informationen zu KAoA-Daten abgerufen oder eingegeben werden.");
		return this._state.value.data
	}

	patch = async (data : Partial<SchuelerVermerke>) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		api.server.patchSchuelerVermerke(data, )
	}


	public async ladeDaten(auswahl: SchuelerListeEintrag | null) {
		if (auswahl === this._state.value.auswahl)
			return;
		if ((auswahl === null) || (auswahl === undefined))
			this.setPatchedDefaultState({});
		else {
			try {
				this._state.value.auswahl = auswahl;
				const vermerke = await api.server.getVermerkdaten(api.schema,auswahl.id);
				const mapVermerke = new Map();

				for (const vermerk of vermerke) {
					mapVermerke.set(vermerk.id, vermerk)
				}

				this.setPatchedDefaultState({data: vermerke});

			} catch(error) {
				throw new DeveloperNotificationException("Die Vermerk-Daten konnten nicht eingeholt werden, sind für diesen Schüler Vermerk-Daten möglich?")
			}
		}
	}
}
