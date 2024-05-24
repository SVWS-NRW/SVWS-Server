import { RouteData, type RouteStateInterface } from "~/router/RouteData";

import type {List, SchuelerListeEintrag} from "@core";
import {DeveloperNotificationException, Schueler, SchuelerVermerke} from "@core";
import {api} from "~/router/Api";

import type { VermerkartEintrag } from "@core";


interface RouteStateSchuelerVermerke extends RouteStateInterface {
	auswahl: SchuelerListeEintrag | undefined;
	data: List<SchuelerVermerke>;
	mapVermerkArten: Map<number, VermerkartEintrag>;
}

const defaultState = <RouteStateSchuelerVermerke> {
	data: [],
	mapVermerkArten: new Map(),
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

	get mapVermerkArten(): Map<number, VermerkartEintrag> {
		// if (this._state.value.data === undefined)
		// 	throw new DeveloperNotificationException("Unerwarteter Fehler: Schülerauswahl nicht festgelegt, es können keine Informationen zu KAoA-Daten abgerufen oder eingegeben werden.");
		return this._state.value.mapVermerkArten
	}


	patch = async (data : Partial<SchuelerVermerke>, vid: number) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		await api.server.patchSchuelerVermerke(data, api.schema, this.auswahl.id, vid)
		await this.ladeDaten(this.auswahl);
	}

	create = async (data: SchuelerVermerke, id: number) => {
		console.log("try to create");
		await api.server.createVermerk(data, api.schema, id)

	}


	public async ladeDaten(auswahl: SchuelerListeEintrag | null) {

		// if (auswahl === this._state.value.auswahl)
		// 	return;
		if ((auswahl === null) || (auswahl === undefined))
			this.setPatchedDefaultState({});
		else {
			try {
				this._state.value.auswahl = auswahl;

				const vermerke =  await api.server.getVermerkdaten(api.schema, auswahl.id);
				const vermerkArten = await api.server.getVermerkarten(api.schema);

				const mapVermerkArten = new Map();
				for (const vA of vermerkArten)
					mapVermerkArten.set(vA.id, vA);

				// setze die Vermerke
				const mapVermerke = new Map();
				for (const vermerk of vermerke) {
					mapVermerke.set(vermerk.id, vermerk)
				}

				this.setPatchedDefaultState({data: vermerke, mapVermerkArten: mapVermerkArten});

			} catch(error) {
				throw new DeveloperNotificationException("Die Vermerk-Daten konnten nicht eingeholt werden, sind für diesen Schüler Vermerk-Daten möglich?")
			}
		}
	}
}
