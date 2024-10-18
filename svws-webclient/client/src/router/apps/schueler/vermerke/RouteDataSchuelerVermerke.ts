import { RouteData, type RouteStateInterface } from "~/router/RouteData";

import type { List, SchuelerListeEintrag, SchuelerVermerke, VermerkartEintrag } from "@core";
import { ArrayList, DeveloperNotificationException } from "@core";
import { api } from "~/router/Api";


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
		return this._state.value.schuelerVermerke;
	}

	get mapVermerkArten(): Map<number, VermerkartEintrag> {
		return this._state.value.mapVermerkArten;
	}

	patch = async (data : Partial<SchuelerVermerke>, idVermerk: number) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		api.status.start();
		await api.server.patchSchuelerVermerke(data, api.schema, idVermerk);
		for (const vermerk of this.schuelerVermerke)
			if (vermerk.id === idVermerk)
				Object.assign(vermerk, data);
		this.commit();
		api.status.stop();
	}

	add = async () => {
		const addCanditate : Partial<SchuelerVermerke> = {idSchueler: this.auswahl.id}
		api.status.start();
		const vermerk = await api.server.addVermerk(addCanditate, api.schema);
		this.schuelerVermerke.add(vermerk);
		this.commit();
		api.status.stop();
	}

	remove = async (idVermerk: number) => {
		api.status.start();
		await api.server.deleteSchuelerVermerk(api.schema, this.auswahl.id, idVermerk);
		for (let i = this.schuelerVermerke.size() - 1; i >= 0; i--) {
			const vermerk = this.schuelerVermerke.get(i);
			if (vermerk.id === idVermerk) {
				this.schuelerVermerke.removeElementAt(i);
				break;
			}
		}
		this.commit();
		api.status.stop();
	}

	public async ladeDaten(auswahl: SchuelerListeEintrag | null) {
		if ((auswahl === null) || (auswahl === undefined)) {
			this.setPatchedDefaultState({});
		} else {
			const schuelerVermerke = await api.server.getVermerkdaten(api.schema, auswahl.id);
			const vermerkArten = await api.server.getVermerkarten(api.schema);
			const mapVermerkArten = new Map();
			for (const va of vermerkArten)
				mapVermerkArten.set(va.id, va);
			this.setPatchedDefaultState({ auswahl, schuelerVermerke, mapVermerkArten });
		}
	}

}
