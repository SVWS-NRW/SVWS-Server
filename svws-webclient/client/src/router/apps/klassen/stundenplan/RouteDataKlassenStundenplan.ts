import { type StundenplanListeEintrag, StundenplanManager, type StundenplanKalenderwochenzuordnung, DeveloperNotificationException, type ApiFile, type ReportingParameter } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";
import { routeApp } from "~/router/apps/RouteApp";
import { routeKlassenStundenplan } from "./RouteKlassenStundenplan";


interface RouteStateKlassenDataStundenplan extends RouteStateInterface {
	idSchuljahresabschnitt: number;
	idKlasse: number | undefined;
	auswahl: StundenplanListeEintrag | undefined;
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	manager: StundenplanManager | undefined;
	wochentyp: number;
	kalenderwoche: StundenplanKalenderwochenzuordnung | undefined,
}

const defaultState = <RouteStateKlassenDataStundenplan> {
	idSchuljahresabschnitt: -1,
	idKlasse: undefined,
	auswahl: undefined,
	mapStundenplaene: new Map(),
	manager: undefined,
	wochentyp: 0,
	kalenderwoche: undefined,
};


export class RouteDataKlassenStundenplan extends RouteData<RouteStateKlassenDataStundenplan> {

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

	get idKlasse(): number {
		if (this._state.value.idKlasse === undefined)
			throw new DeveloperNotificationException("Unerwarteter Fehler: Klassen-ID nicht festgelegt, es können keine Informationen zum Stundenplan abgerufen oder eingegeben werden.");
		return this._state.value.idKlasse;
	}

	get hatAuswahl(): boolean {
		return (this._state.value.auswahl !== undefined);
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
		return api.config.getValue("klasse.stundenplan.ganzerStundenplan") === 'true';
	}

	setGanzerStundenplan = async (value: boolean) => {
		await api.config.setValue("klasse.stundenplan.ganzerStundenplan", value ? "true" : "false");
	}

	public async ladeListe(idKlasse : number) : Promise<boolean> {
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
			const daten = await api.server.getKlassenStundenplan(api.schema, auswahl.id, idKlasse);
			manager = new StundenplanManager(daten);
			// Wochentyp und Kalenderwoche prüfen...
			tmpKW = this.getKalenderWoche(manager, wochentyp, undefined, undefined);
		}
		this.setPatchedDefaultState({ idSchuljahresabschnitt, idKlasse: this._state.value.idKlasse, auswahl, mapStundenplaene, manager, wochentyp: tmpKW?.wochentyp, kalenderwoche: tmpKW?.kalenderwoche });
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

	public async setEintrag(idKlasse: number, idStundenplan: number | undefined, wochentypNr: number, kwjahr: number | undefined, kw: number | undefined) {
		// Prüfe, ob die vorge Auswahl mit der neuen Auswahl übereinstimmt. In diesem Fall ist keine Aktion nötig
		const vorige_auswahl = this._state.value.auswahl;
		if ((vorige_auswahl !== undefined) && (this._state.value.idKlasse === idKlasse) && (vorige_auswahl.id === idStundenplan)) {
			if ((wochentypNr !== this._state.value.wochentyp) || (kwjahr !== this._state.value.kalenderwoche?.jahr)
				|| (kw !== this._state.value.kalenderwoche?.kw)) {
				const tmpKW = this.getKalenderWoche(this.manager, wochentypNr, kwjahr, kw);
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
		const { wochentyp, kalenderwoche } = this.getKalenderWoche(manager, wochentypNr, kwjahr, kw);
		// Und zuletzt den internen State anpassen, um die Raktivität der vue-Komponenten zu triggern
		this.setPatchedState({ idKlasse, auswahl, manager, wochentyp, kalenderwoche });
	}

	public gotoStundenplan = async (value: StundenplanListeEintrag | undefined) => {
		await RouteManager.doRoute(routeKlassenStundenplan.getRoute({ idStundenplan: value?.id, wochentyp: 0, kw: "" }));
	}

	public gotoWochentyp = async (wochentyp: number) => {
		await RouteManager.doRoute(routeKlassenStundenplan.getRoute({ wochentyp }));
	}

	public gotoKalenderwoche = async (value: StundenplanKalenderwochenzuordnung | undefined) => {
		const kw = (value === undefined) ? "" : value.jahr + "." + value.kw;
		const wochentyp = (value === undefined) ? "" : value.wochentyp;
		await RouteManager.doRoute(routeKlassenStundenplan.getRoute({ wochentyp, kw }));
	}

	getPDF = api.call(async (reportingParameter: ReportingParameter, idStundenplan: number): Promise<ApiFile> => {
		if (!this.hatAuswahl)
			throw new DeveloperNotificationException("Dieser Stundenplan kann nur gedruckt werden, wenn mindestens ein Lehrer ausgewählt ist.");
		reportingParameter.idSchuljahresabschnitt = this.idSchuljahresabschnitt;
		reportingParameter.idsHauptdaten.add(idStundenplan);
		reportingParameter.idsDetaildaten.add(this.idKlasse);
		return await api.server.pdfReport(reportingParameter, api.schema);
	})
}
