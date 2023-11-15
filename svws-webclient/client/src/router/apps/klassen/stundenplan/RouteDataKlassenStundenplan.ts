import { shallowRef } from "vue";

import { type StundenplanListeEintrag, StundenplanManager, type StundenplanKalenderwochenzuordnung } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { routeApp } from "~/router/apps/RouteApp";
import { routeKlassen } from "~/router/apps/klassen/RouteKlassen";

import { routeKlassenStundenplanDaten } from "~/router/apps/klassen/stundenplan/RouteKlassenStundenplanDaten";


interface RouteStateKlassenDataStundenplan {
	idKlasse: number | undefined;
	auswahl: StundenplanListeEintrag | undefined;
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	manager: StundenplanManager | undefined;
	wochentyp: number;
	kalenderwoche: StundenplanKalenderwochenzuordnung | undefined,
}

export class RouteDataKlassenStundenplan {

	private static _defaultState: RouteStateKlassenDataStundenplan = {
		idKlasse: undefined,
		auswahl: undefined,
		mapStundenplaene: new Map(),
		manager: undefined,
		wochentyp: 0,
		kalenderwoche: undefined,
	}

	private _state = shallowRef(RouteDataKlassenStundenplan._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateKlassenDataStundenplan>) {
		this._state.value = Object.assign({ ... RouteDataKlassenStundenplan._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateKlassenDataStundenplan>) {
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

	get hatAuswahl(): boolean {
		return (this._state.value.auswahl !== undefined);
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
		this.setPatchedDefaultState({ idKlasse: this._state.value.idKlasse, auswahl, mapStundenplaene });
	}

	private getKalenderWoche(manager: StundenplanManager, wochentyp: number, kwjahr: number | undefined, kw: number | undefined) : { wochentyp?: number, kalenderwoche?: StundenplanKalenderwochenzuordnung } {
		const result : { wochentyp?: number, kalenderwoche?: StundenplanKalenderwochenzuordnung } = {};
		try {
			result.kalenderwoche = manager.kalenderwochenzuordnungGetByJahrAndKWOrException(kwjahr === undefined ? -1 : kwjahr, kw === undefined ? -1 : kw);
			result.wochentyp = result.kalenderwoche.wochentyp;
		} catch (e) {
			result.kalenderwoche = undefined;
			result.wochentyp = wochentyp;
		}
		if ((result.wochentyp < 0) || (result.wochentyp > manager.getWochenTypModell()))
			throw new Error("Der Wochentyp " + result.wochentyp + " passt nicht zum Wochentyp-Modell " + this.manager.getWochenTypModell() + " des Stundenplans");
		return result;
	}

	public async setEintrag(idKlasse: number, idStundenplan: number | undefined, wochentyp: number, kwjahr: number | undefined, kw: number | undefined) {
		// Prüfe, ob die vorge Auswahl mit der neuen Auswahl übereinstimmt. In diesem Fall ist keine Aktion nötig
		const vorige_auswahl = this._state.value.auswahl;
		if ((vorige_auswahl !== undefined) && (this._state.value.idKlasse === idKlasse) && (vorige_auswahl.id === idStundenplan)) {
			if ((wochentyp !== this._state.value.wochentyp) || (kwjahr !== this._state.value.kalenderwoche?.jahr)
				|| (kw !== this._state.value.kalenderwoche?.kw)) {
				const tmpKW = this.getKalenderWoche(this.manager, wochentyp, kwjahr, kw);
				this.setPatchedState({ wochentyp: tmpKW.wochentyp, kalenderwoche: tmpKW.kalenderwoche });
				return;
			}
			return;
		}
		// Prüfe, ob der Stundenplan deselektiert wird. In diesem Fall muss der interne State zurückgesetzt werden.
		if (idStundenplan === undefined) {
			this.setPatchedState({ idKlasse: undefined, auswahl: undefined, manager: undefined, wochentyp: 0, kalenderwoche: undefined });
			return;
		}
		// Ermittle aus der Liste der Stundenpläne den Stundenplan
		const auswahl = this.mapStundenplaene.get(idStundenplan);
		if (auswahl === undefined) {
			this.setPatchedState({ idKlasse: undefined, auswahl: undefined, manager: undefined, wochentyp: 0, kalenderwoche: undefined });
			return;
		}
		// Lade den Klassen-Stundenplan
		const daten = await api.server.getKlassenStundenplan(api.schema, auswahl.id, idKlasse);
		const manager = new StundenplanManager(daten);
		// Wochentyp und Kalenderwoche prüfen...
		const tmpKW = this.getKalenderWoche(manager, wochentyp, kwjahr, kw);
		// Und zuletzt den internen State anpassen, um die Raktivität der vue-Komponenten zu triggern
		this.setPatchedState({ idKlasse, auswahl, manager, wochentyp: tmpKW.wochentyp, kalenderwoche: tmpKW.kalenderwoche });
	}

	public gotoStundenplan = async (value: StundenplanListeEintrag | undefined) => {
		await RouteManager.doRoute({ name: routeKlassenStundenplanDaten.name, params: { id: routeKlassen.data.klassenListeManager.daten().id, idStundenplan: value?.id } });
	}

	public gotoWochentyp = async (value: number) => {
		await RouteManager.doRoute(routeKlassenStundenplanDaten.getRoute(routeKlassen.data.klassenListeManager.daten().id, this.auswahl.id, value));
	}

	public gotoKalenderwoche = async (value: StundenplanKalenderwochenzuordnung | undefined) => {
		if (value === undefined)
			await RouteManager.doRoute(routeKlassenStundenplanDaten.getRoute(routeKlassen.data.klassenListeManager.daten().id, this.auswahl.id, this.wochentyp));
		else
			await RouteManager.doRoute(routeKlassenStundenplanDaten.getRoute(routeKlassen.data.klassenListeManager.daten().id, this.auswahl.id, value.wochentyp, value.jahr, value.kw));
	}

}

