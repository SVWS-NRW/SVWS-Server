import { RouteData, type RouteStateInterface } from "~/router/RouteData";

import type { List, SchuelerListeEintrag, SchuelerVermerke } from "@core";
import { ArrayList, DeveloperNotificationException } from "@core";
import { api } from "~/router/Api";

import type { VermerkartEintrag } from "@core";


interface RouteStateSchuelerVermerke extends RouteStateInterface {
	auswahl: SchuelerListeEintrag | undefined;
	data: List<SchuelerVermerke>;
	mapVermerkArten: Map<number, VermerkartEintrag>;
}

const defaultState = <RouteStateSchuelerVermerke> {
	auswahl: undefined,
	data: new ArrayList(),
	mapVermerkArten: new Map(),
};

export class RouteDataSchuelerVermerke extends RouteData<RouteStateSchuelerVermerke> {

	public constructor() {
		super(defaultState);
	}

	get auswahl(): SchuelerListeEintrag {
		if (this._state.value.auswahl === undefined)
			throw new DeveloperNotificationException("Unerwarteter Fehler: Schülerauswahl nicht festgelegt, es können keine Informationen zu Vermerk-Daten abgerufen oder eingegeben werden.");
		return this._state.value.auswahl;
	}

	get data(): List<SchuelerVermerke> {
		return this._state.value.data
	}

	get mapVermerkArten(): Map<number, VermerkartEintrag> {
		return this._state.value.mapVermerkArten
	}


	patch = async (data : Partial<SchuelerVermerke>, idVermerk: number) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		await api.server.patchSchuelerVermerke(data, api.schema, this.auswahl.id, idVermerk)
		await this.ladeDaten(this.auswahl);
	}

	create = async () => {
		await api.server.createVermerk(api.schema, this.auswahl.id)
		await this.ladeDaten(this.auswahl);
	}

	deleteVermerk = async (idVermerk: number) => {
		await api.server.deleteSchuelerVermerk(api.schema, this.auswahl.id, idVermerk)
		await this.ladeDaten(this.auswahl);
	}


	public async ladeDaten(auswahl: SchuelerListeEintrag | null) {
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

				this.setPatchedDefaultState({data: vermerke, mapVermerkArten: mapVermerkArten});
			} catch(error) {
				throw new DeveloperNotificationException("Die Vermerk-Daten konnten nicht geladen werden.")
			}
		}
	}

}
