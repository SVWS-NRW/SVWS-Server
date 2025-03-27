import { type StundenplanKalenderwochenzuordnung, type StundenplanListeEintrag, StundenplanManager, DeveloperNotificationException, type ApiFile, type ReportingParameter } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";
import { routeApp } from "~/router/apps/RouteApp";
import { routeSchuelerStundenplan } from "./RouteSchuelerStundenplan";


interface RouteStateSchuelerDataStundenplan extends RouteStateInterface {
	idSchuljahresabschnitt: number;
	idSchueler: number | undefined;
	auswahl: StundenplanListeEintrag | undefined;
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	manager: StundenplanManager | undefined;
	wochentyp: number;
	kalenderwoche: StundenplanKalenderwochenzuordnung | undefined,
}

const defaultState = <RouteStateSchuelerDataStundenplan> {
	idSchuljahresabschnitt: -1,
	idSchueler: undefined,
	auswahl: undefined,
	mapStundenplaene: new Map(),
	manager: undefined,
	wochentyp: 0,
	kalenderwoche: undefined,
};


export class RouteDataSchuelerStundenplan extends RouteData<RouteStateSchuelerDataStundenplan> {

	public constructor() {
		super(defaultState);
	}

	get idSchuljahresabschnitt(): number {
		return this._state.value.idSchuljahresabschnitt;
	}

	get wochentyp(): number {
		return this._state.value.wochentyp;
	}

	get kalenderwoche(): StundenplanKalenderwochenzuordnung | undefined {
		return this._state.value.kalenderwoche;
	}

	get idSchueler(): number {
		if (this._state.value.idSchueler === undefined)
			throw new DeveloperNotificationException("Unerwarteter Fehler: SchülerID nicht festgelegt, es können keine Informationen zum Stundenplan abgerufen oder eingegeben werden.");
		return this._state.value.idSchueler;
	}

	get hatAuswahl(): boolean {
		return this._state.value.auswahl !== undefined;
	}

	get auswahl(): StundenplanListeEintrag {
		if (this._state.value.auswahl === undefined)
			throw new DeveloperNotificationException("Unerwarteter Fehler: Stundenplaneintrag nicht festgelegt, es können keine Informationen zum Stundenplan abgerufen oder eingegeben werden.");
		return this._state.value.auswahl;
	}

	get manager(): StundenplanManager {
		if (this._state.value.manager === undefined)
			throw new DeveloperNotificationException("Unerwarteter Fehler: Stundenplan-Manager nicht vorhanden, es können keine Informationen zum Stundenplan abgerufen oder eingegeben werden.");
		return this._state.value.manager;
	}

	get mapStundenplaene(): Map<number, StundenplanListeEintrag> {
		return this._state.value.mapStundenplaene;
	}

	get ganzerStundenplan(): boolean {
		return api.config.getValue("schueler.stundenplan.ganzerStundenplan") === 'true';
	}

	setGanzerStundenplan = async (value: boolean) => {
		await api.config.setValue("schueler.stundenplan.ganzerStundenplan", value ? "true" : "false");
	}

	public async ladeListe(idSchueler : number) : Promise<boolean> {
		const idSchuljahresabschnitt = routeApp.data.aktAbschnitt.value.id;
		if (idSchuljahresabschnitt === this._state.value.idSchuljahresabschnitt)
			return false;
		const listStundenplaene = await api.server.getStundenplanlisteFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		const mapStundenplaene = new Map<number, StundenplanListeEintrag>();
		const auswahl = listStundenplaene.size() > 0 ? listStundenplaene.get(0) : undefined;
		for (const l of listStundenplaene)
			mapStundenplaene.set(l.id, l);
		// Lade ggf. den Schüler-Stundenplan
		let manager = undefined;
		const wochentyp = 0;
		let tmpKW = undefined;
		if (auswahl !== undefined) {
			const daten = await api.server.getSchuelerStundenplan(api.schema, auswahl.id, idSchueler);
			manager = new StundenplanManager(daten);
			// Wochentyp und Kalenderwoche prüfen...
			tmpKW = this.getKalenderWoche(manager, wochentyp, undefined, undefined);
		}
		this.setPatchedDefaultState({ idSchuljahresabschnitt, idSchueler: this._state.value.idSchueler, auswahl, mapStundenplaene, manager, wochentyp: tmpKW?.wochentyp, kalenderwoche: tmpKW?.kalenderwoche });
		return true;
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

	public async setEintrag(idSchueler: number, idStundenplan: number | undefined, wochentyp: number, kwjahr: number | undefined, kw: number | undefined) {
		// Prüfe, ob die vorge Auswahl mit der neuen Auswahl übereinstimmt. In diesem Fall ist keine Aktion nötig
		const vorige_auswahl = this._state.value.auswahl;
		if ((vorige_auswahl !== undefined) && (this._state.value.idSchueler === idSchueler) && (vorige_auswahl.id === idStundenplan)) {
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
			this.setPatchedState({ idSchueler: undefined, auswahl: undefined, manager: undefined, wochentyp: 0, kalenderwoche: undefined });
			return;
		}
		// Ermittle aus der Liste der Stundenpläne den Stundenplan
		const auswahl = this.mapStundenplaene.get(idStundenplan);
		if (auswahl === undefined) {
			this.setPatchedState({ idSchueler: undefined, auswahl: undefined, manager: undefined, wochentyp: 0, kalenderwoche: undefined });
			return;
		}
		// Lade den Schüler-Stundenplan
		const daten = await api.server.getSchuelerStundenplan(api.schema, auswahl.id, idSchueler);
		const manager = new StundenplanManager(daten);
		// Wochentyp und Kalenderwoche prüfen...
		const tmpKW = this.getKalenderWoche(manager, wochentyp, kwjahr, kw);
		// Und zuletzt den internen State anpassen, um die Reaktivität der vue-Komponenten zu triggern
		this.setPatchedState({ idSchueler, auswahl, manager, wochentyp: tmpKW.wochentyp, kalenderwoche: tmpKW.kalenderwoche });
	}

	public gotoStundenplan = async (value: StundenplanListeEintrag) => {
		await RouteManager.doRoute(routeSchuelerStundenplan.getRoute({ idStundenplan: value.id, wochentyp: 0, kw: "" }));
	}

	public gotoWochentyp = async (wochentyp: number) => {
		await RouteManager.doRoute(routeSchuelerStundenplan.getRoute({ wochentyp }));
	}

	public gotoKalenderwoche = async (value: StundenplanKalenderwochenzuordnung | undefined) => {
		const kw = (value === undefined) ? "" : value.jahr + "." + value.kw;
		const wochentyp = (value === undefined) ? "" : value.wochentyp;
		await RouteManager.doRoute(routeSchuelerStundenplan.getRoute({ wochentyp, kw }));
	}

	getPDF = api.call(async (reportingParameter: ReportingParameter, idStundenplan: number): Promise<ApiFile> => {
		if (!this.hatAuswahl)
			throw new DeveloperNotificationException("Dieser Stundenplan kann nur gedruckt werden, wenn mindestens ein Lehrer ausgewählt ist.");
		reportingParameter.idSchuljahresabschnitt = this.idSchuljahresabschnitt;
		reportingParameter.idsHauptdaten.add(idStundenplan);
		reportingParameter.idsDetaildaten.add(this.idSchueler);
		return await api.server.pdfReport(reportingParameter, api.schema);
	})

}

