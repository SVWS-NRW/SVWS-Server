import { shallowRef } from "vue";

import type { StundenplanKalenderwochenzuordnung, StundenplanListeEintrag} from "@core";
import { StundenplanManager } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { routeApp } from "~/router/apps/RouteApp";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";

import { routeSchuelerStundenplanDaten } from "~/router/apps/schueler/stundenplan/RouteSchuelerStundenplanDaten";


interface RouteStateSchuelerDataStundenplan {
	idSchueler: number | undefined;
	auswahl: StundenplanListeEintrag | undefined;
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	manager: StundenplanManager | undefined;
	wochentyp: number;
	kalenderwoche: StundenplanKalenderwochenzuordnung | undefined,
}

export class RouteDataSchuelerStundenplan {

	private static _defaultState: RouteStateSchuelerDataStundenplan = {
		idSchueler: undefined,
		auswahl: undefined,
		mapStundenplaene: new Map(),
		manager: undefined,
		wochentyp: 0,
		kalenderwoche: undefined,
	}

	private _state = shallowRef(RouteDataSchuelerStundenplan._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateSchuelerDataStundenplan>) {
		this._state.value = Object.assign({ ... RouteDataSchuelerStundenplan._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateSchuelerDataStundenplan>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	get wochentyp(): number {
		return this._state.value.wochentyp;
	}

	get kalenderwoche(): StundenplanKalenderwochenzuordnung | undefined {
		return this._state.value.kalenderwoche;
	}

	get auswahl(): StundenplanListeEintrag {
		if (this._state.value.auswahl === undefined)
			throw new Error("Unerwarteter Fehler: Stundenplaneintrag nicht festgelegt, es können keine Informationen zum Stundenplan abgerufen oder eingegeben werden.");
		return this._state.value.auswahl;
	}

	get manager(): StundenplanManager {
		if (this._state.value.manager === undefined)
			throw new Error("Unerwarteter Fehler: Stunden-Manager nicht vorhanden, es können keine Informationen zum Stundenplan abgerufen oder eingegeben werden.");
		return this._state.value.manager;
	}

	get mapStundenplaene(): Map<number, StundenplanListeEintrag> {
		return this._state.value.mapStundenplaene;
	}

	public async ladeListe() {
		const listStundenplaene = await api.server.getStundenplanlisteFuerAbschnitt(api.schema, routeApp.data.aktAbschnitt.value.id);
		const mapStundenplaene = new Map<number, StundenplanListeEintrag>();
		const auswahl = listStundenplaene.size() > 0 ? listStundenplaene.get(0) : undefined;
		for (const l of listStundenplaene)
			mapStundenplaene.set(l.id, l);
		this.setPatchedDefaultState({ idSchueler: this._state.value.idSchueler, auswahl, mapStundenplaene });
	}

	public async setEintrag(idSchueler: number, idStundenplan: number | undefined, wochentyp: number, kwjahr: number | undefined, kw: number | undefined) {
		// Prüfe, ob die vorge Auswahl mit der neuen Auswahl übereinstimmt. In diesem Fall ist keine Aktion nötig
		const vorige_auswahl = this._state.value.auswahl;
		if ((vorige_auswahl !== undefined) && (this._state.value.idSchueler === idSchueler) && (vorige_auswahl.id === idStundenplan)) {
			if ((wochentyp !== this._state.value.wochentyp) || (kwjahr !== this._state.value.kalenderwoche?.jahr)
				|| (kw !== this._state.value.kalenderwoche?.kw)) {
				// TODO Wochentyp und Kalenderwoche prüfen...
				const tmpKalenderwoche: StundenplanKalenderwochenzuordnung | undefined = undefined;
				const tmpWochentyp = wochentyp;
				// if (tmpKalenderwoche !== undefined)
				// 	tmpWochentyp = tmpKalenderwoche.wochentyp;
				if ((tmpWochentyp < 0) || (tmpWochentyp > this.manager.getWochenTypModell()))
					throw new Error("Der Wochentyp passt nicht zum Wochentyp-Modell des Stundenplans");
				this.setPatchedState({ wochentyp: tmpWochentyp, kalenderwoche: tmpKalenderwoche });
				return;
			}
			return;
		}
		// Prüfe, ob der Stundenplan deselektiert wird. In diesem Fall muss der interne State zurückgesetzt werden.
		if (idStundenplan === undefined) {
			this.setPatchedState({ idSchueler: undefined, auswahl: undefined, manager: undefined, wochentyp: 0, kalenderwoche: undefined });
			return;
		}
		// Ermittle aus der Liste der Stundenpläne den Stundenplan
		const auswahl = this.mapStundenplaene.get(idStundenplan);
		if (auswahl === undefined) {
			this.setPatchedState({ idSchueler: undefined, auswahl: undefined, manager: undefined, wochentyp: 0, kalenderwoche: undefined });
			return;
		}
		// Lade den Lehrer-Stundenplan
		const daten = await api.server.getSchuelerStundenplan(api.schema, auswahl.id, idSchueler);
		const manager = new StundenplanManager(daten);
		// TODO Wochentyp und Kalenderwoche prüfen...
		const tmpKalenderwoche: StundenplanKalenderwochenzuordnung | undefined = undefined;
		const tmpWochentyp = wochentyp;
		// if (tmpKalenderwoche !== undefined)
		// 	tmpWochentyp = tmpKalenderwoche.wochentyp;
		if ((tmpWochentyp < 0) || (tmpWochentyp > manager.getWochenTypModell()))
			throw new Error("Der Wochentyp passt nicht zum Wochentyp-Modell des Stundenplans");
		this.setPatchedState({ idSchueler, auswahl, manager, wochentyp: tmpWochentyp, kalenderwoche: tmpKalenderwoche });
	}

	public gotoStundenplan = async (value: StundenplanListeEintrag) => {
		await RouteManager.doRoute(routeSchuelerStundenplanDaten.getRoute(routeSchueler.data.stammdaten.id, value.id, 0));
	}

	public gotoWochentyp = async (value: number) => {
		await RouteManager.doRoute(routeSchuelerStundenplanDaten.getRoute(routeSchueler.data.stammdaten.id, this.auswahl.id, value));
	}

	public gotoKalenderwoche = async (value: StundenplanKalenderwochenzuordnung | undefined) => {
		if (value === undefined)
			await RouteManager.doRoute(routeSchuelerStundenplanDaten.getRoute(routeSchueler.data.stammdaten.id, this.auswahl.id, this.wochentyp));
		else
			await RouteManager.doRoute(routeSchuelerStundenplanDaten.getRoute(routeSchueler.data.stammdaten.id, this.auswahl.id, value.wochentyp, value.jahr, value.kw));
	}

}

