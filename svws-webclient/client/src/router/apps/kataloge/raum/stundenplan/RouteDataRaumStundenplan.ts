import type { StundenplanKalenderwochenzuordnung, StundenplanListeEintrag, StundenplanPausenaufsicht } from "@core";
import { StundenplanManager, DeveloperNotificationException, ArrayList} from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";
import { routeApp } from "~/router/apps/RouteApp";
import { routeKatalogRaeume } from "~/router/apps/kataloge/raum/RouteKatalogRaeume";

import { routeRaumStundenplanDaten } from "~/router/apps/kataloge/raum/stundenplan/RouteRaumStundenplanDaten";


interface RouteStateRaumDataStundenplan extends RouteStateInterface {
	idRaum: number | undefined;
	auswahl: StundenplanListeEintrag | undefined;
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	manager: StundenplanManager | undefined;
	wochentyp: number;
	kalenderwoche: StundenplanKalenderwochenzuordnung | undefined,
}

const defaultState = <RouteStateRaumDataStundenplan> {
	idRaum: undefined,
	auswahl: undefined,
	mapStundenplaene: new Map(),
	manager: undefined,
	wochentyp: 0,
	kalenderwoche: undefined,
};


export class RouteDataRaumStundenplan extends RouteData<RouteStateRaumDataStundenplan> {

	public constructor() {
		super(defaultState);
	}

	get wochentyp(): number {
		return this._state.value.wochentyp;
	}

	get kalenderwoche(): StundenplanKalenderwochenzuordnung | undefined {
		return this._state.value.kalenderwoche;
	}

	get auswahl(): StundenplanListeEintrag {
		if (this._state.value.auswahl === undefined)
			throw new DeveloperNotificationException("Unerwarteter Fehler: Stundenplaneintrag nicht festgelegt, es können keine Informationen zum Stundenplan abgerufen oder eingegeben werden.");
		return this._state.value.auswahl;
	}

	get manager(): StundenplanManager {
		if (this._state.value.manager === undefined)
			throw new DeveloperNotificationException("Unerwarteter Fehler: Stunden-Manager nicht vorhanden, es können keine Informationen zum Stundenplan abgerufen oder eingegeben werden.");
		return this._state.value.manager;
	}

	get mapStundenplaene(): Map<number, StundenplanListeEintrag> {
		return this._state.value.mapStundenplaene;
	}

	get ganzerStundenplan(): boolean {
		return api.config.getValue("kataloge.raeume.stundenplan.ganzerStundenplan") === 'true';
	}

	setGanzerStundenplan = async (value: boolean) => {
		await api.config.setValue("kataloge.raeume.stundenplan.ganzerStundenplan", value ? "true" : "false");
	}

	public async ladeListe() {
		const listStundenplaene = await api.server.getStundenplanlisteFuerAbschnitt(api.schema, routeApp.data.aktAbschnitt.value.id);
		const mapStundenplaene = new Map<number, StundenplanListeEintrag>();
		const auswahl = listStundenplaene.size() > 0 ? listStundenplaene.get(0) : undefined;
		for (const l of listStundenplaene)
			mapStundenplaene.set(l.id, l);
		this.setPatchedDefaultState({ idRaum: this._state.value.idRaum, auswahl, mapStundenplaene });
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
			throw new DeveloperNotificationException("Der Wochentyp " + result.wochentyp + " passt nicht zum Wochentyp-Modell " + this.manager.getWochenTypModell() + " des Stundenplans");
		return result;
	}

	public async setEintrag(idRaum: number, idStundenplan: number | undefined, wochentyp: number, kwjahr: number | undefined, kw: number | undefined) {
		// Prüfe, ob die vorge Auswahl mit der neuen Auswahl übereinstimmt. In diesem Fall ist keine Aktion nötig
		const vorige_auswahl = this._state.value.auswahl;
		if ((vorige_auswahl !== undefined) && (this._state.value.idRaum === idRaum) && (vorige_auswahl.id === idStundenplan)) {
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
			this.setPatchedState({ idRaum: undefined, auswahl: undefined, manager: undefined, wochentyp: 0, kalenderwoche: undefined });
			return;
		}
		// Ermittle aus der Liste der Stundenpläne den Stundenplan
		const auswahl = this.mapStundenplaene.get(idStundenplan);
		if (auswahl === undefined) {
			this.setPatchedState({ idRaum: undefined, auswahl: undefined, manager: undefined, wochentyp: 0, kalenderwoche: undefined });
			return;
		}
		// Lade den Raum-Stundenplan
		const daten = await api.server.getStundenplan(api.schema, auswahl.id);
		const unterrichtsdaten = await api.server.getStundenplanUnterrichte(api.schema, auswahl.id);
		// const pausenaufsichten = await api.server.getStundenplanPausenaufsichten(api.schema, auswahl.id);
		const unterrichtsverteilung = await api.server.getStundenplanUnterrichtsverteilung(api.schema, auswahl.id);
		const manager = new StundenplanManager(daten, unterrichtsdaten, new ArrayList<StundenplanPausenaufsicht>(), unterrichtsverteilung);

		// Wochentyp und Kalenderwoche prüfen...
		const tmpKW = this.getKalenderWoche(manager, wochentyp, kwjahr, kw);
		// Und zuletzt den internen State anpassen, um die Reaktivität der vue-Komponenten zu triggern
		this.setPatchedState({ idRaum, auswahl, manager, wochentyp: tmpKW.wochentyp, kalenderwoche: tmpKW.kalenderwoche });
	}

	public gotoStundenplan = async (value: StundenplanListeEintrag) => {
		await RouteManager.doRoute(routeRaumStundenplanDaten.getRoute(routeKatalogRaeume.data.raumListeManager.daten().id, value.id, 0));
	}

	public gotoWochentyp = async (value: number) => {
		await RouteManager.doRoute(routeRaumStundenplanDaten.getRoute(routeKatalogRaeume.data.raumListeManager.daten().id, this.auswahl.id, value));
	}

	public gotoKalenderwoche = async (value: StundenplanKalenderwochenzuordnung | undefined) => {
		if (value === undefined)
			await RouteManager.doRoute(routeRaumStundenplanDaten.getRoute(routeKatalogRaeume.data.raumListeManager.daten().id, this.auswahl.id, this.wochentyp));
		else
			await RouteManager.doRoute(routeRaumStundenplanDaten.getRoute(routeKatalogRaeume.data.raumListeManager.daten().id, this.auswahl.id, value.wochentyp, value.jahr, value.kw));
	}

}

