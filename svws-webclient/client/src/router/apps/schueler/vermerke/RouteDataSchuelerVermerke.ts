import { RouteData, type RouteStateInterface } from "~/router/RouteData";

import type { List, SchuelerListeEintrag, SchuelerVermerke } from "@core";
import { ArrayList, DeveloperNotificationException } from "@core";
import { api } from "~/router/Api";

import type { VermerkartEintrag } from "@core";


interface RouteStateSchuelerVermerke extends RouteStateInterface {
	auswahl: SchuelerListeEintrag | undefined;
	schuelerVermerke: List<SchuelerVermerke>;
	mapVermerkArten: Map<number, VermerkartEintrag>;
}

const defaultState = <RouteStateSchuelerVermerke> {
	auswahl: undefined,
	schuelerVermerke: new ArrayList(),
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

	get schuelerVermerke(): List<SchuelerVermerke> {
		return this._state.value.schuelerVermerke
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

	add = async () => {
		console.log("......>", api.schema, this.auswahl.id)
		const addCanditate : Partial<SchuelerVermerke> = {idSchueler: this.auswahl.id}
		await api.server.addVermerk(addCanditate, api.schema)
		await this.ladeDaten(this.auswahl);
	}

	remove = async (idVermerk: number) => {
		console.log("delete")
		await api.server.deleteSchuelerVermerk(api.schema, this.auswahl.id, idVermerk)
		await this.ladeDaten(this.auswahl);
	}


	public async ladeDaten(auswahl: SchuelerListeEintrag | null) {
		if ((auswahl === null) || (auswahl === undefined))
			this.setPatchedDefaultState({});
		else {

			const schuelerVermerke =  await api.server.getVermerkdaten(api.schema, auswahl.id);

			const vermerkArten = await api.server.getVermerkarten(api.schema);
			const mapVermerkArten = new Map();
			for (const va of vermerkArten)
				mapVermerkArten.set(va.id, va);

			this.setPatchedDefaultState({ auswahl: auswahl, schuelerVermerke: schuelerVermerke, mapVermerkArten: mapVermerkArten });
		}
	}

}
