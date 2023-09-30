import { shallowRef } from "vue";

import type { List, FaecherListeEintrag, LehrerListeEintrag, SchuelerLeistungsdaten, SchuelerLernabschnittListeEintrag, SchuelerLernabschnittsdaten} from "@core";
import { ArrayList, SchuelerLernabschnittManager, SchuelerListeEintrag } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { routeApp } from "~/router/apps/RouteApp";
import { routeSchuelerLeistungenDaten } from "~/router/apps/schueler/leistungsdaten/RouteSchuelerLeistungenDaten";
import { routeSchueler } from "../RouteSchueler";


interface RouteStateDataSchuelerLeistungen {
	// Daten, die in Abhängigkeit des ausgewählten Schülers geladen werden
	idSchueler: number;
	listAbschnitte: List<SchuelerLernabschnittListeEintrag>;
	listFaecher: List<FaecherListeEintrag>;
	listLehrer: List<LehrerListeEintrag>;
	// Daten, die in Abhängigkeit des ausgewählten Lernabschnitts geladen werden
	auswahl: SchuelerLernabschnittListeEintrag | undefined;
	daten: SchuelerLernabschnittsdaten | undefined;
	manager: SchuelerLernabschnittManager | undefined;
}


export class RouteDataSchuelerLeistungen {

	private static _defaultState: RouteStateDataSchuelerLeistungen = {
		idSchueler: -1,
		listAbschnitte: new ArrayList<SchuelerLernabschnittListeEintrag>(),
		listFaecher: new ArrayList(),
		listLehrer: new ArrayList(),
		auswahl: undefined,
		daten: undefined,
		manager: undefined,
	}

	private _state = shallowRef(RouteDataSchuelerLeistungen._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateDataSchuelerLeistungen>) {
		this._state.value = Object.assign({ ... RouteDataSchuelerLeistungen._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateDataSchuelerLeistungen>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}



	get idSchueler(): number {
		return this._state.value.idSchueler;
	}

	get listAbschnitte(): List<SchuelerLernabschnittListeEintrag> {
		return this._state.value.listAbschnitte;
	}

	get manager(): SchuelerLernabschnittManager {
		if (this._state.value.manager === undefined)
			throw new Error("Unerwarteter Fehler: Schüler-Lernabschnittsdaten nicht initialisiert");
		return this._state.value.manager;
	}

	protected getLernabschnitt(curState : RouteStateDataSchuelerLeistungen, idSchuljahresabschnitt : number, wechselNr : number) : SchuelerLernabschnittListeEintrag | undefined {
		let found : SchuelerLernabschnittListeEintrag | undefined = undefined;
		for (const current of curState.listAbschnitte) {
			if (current.schuljahresabschnitt === idSchuljahresabschnitt) {
				found = current;
				if (current.wechselNr === wechselNr)
					break;
			}
		}
		return found;
	}

	protected async updateSchuljahresabschnitt(curState : RouteStateDataSchuelerLeistungen, newSchuljahresabschnitt : number | undefined, newWechselNr : number) : Promise<RouteStateDataSchuelerLeistungen> {
		let found : SchuelerLernabschnittListeEintrag | undefined = undefined;
		// Prüfe, ob ein alter Schuljahresabschnitt (und ggf. die Wechselnummer) in der Liste der Lernabschnitte existiert
		if (newSchuljahresabschnitt !== undefined)
			found = this.getLernabschnitt(curState, newSchuljahresabschnitt, newWechselNr);
		// Wenn kein passender Lernabschnitt gefunden wurde, dann prüfe, ob ein Lernabschnitt für das aktuelle Halbjahr vorhanden ist.
		if (found === undefined) {
			const idSchuljahresabschnitt = routeApp.data.aktAbschnitt.value.id;
			for (const current of curState.listAbschnitte) {
				if ((current.schuljahresabschnitt === idSchuljahresabschnitt) && (current.wechselNr === 0)) {
					found = current;
					break;
				}
			}
		}
		// Ansonsten nimm einfach den letzten Lernabschnitt der Liste
		if (found === undefined) {
			if (curState.listAbschnitte.isEmpty())
				return curState;
			found = curState.listAbschnitte.get(curState.listAbschnitte.size()-1);
		}
		const daten = await api.server.getSchuelerLernabschnittsdatenByID(api.schema, found.id);
		const listKurse = await api.server.getKurseFuerAbschnitt(api.schema, found.schuljahresabschnitt);
		let schueler = routeSchueler.data.auswahl;
		if (schueler === undefined)
			schueler = new SchuelerListeEintrag();
		const manager = new SchuelerLernabschnittManager(schueler, daten, curState.listFaecher, listKurse, curState.listLehrer);
		curState = Object.assign({ ... curState }, { auswahl: found, daten, manager });
		return curState;
	}


	public async setSchueler(idSchueler: number) : Promise<void> {
		if (idSchueler == this._state.value.idSchueler)
			return;
		const listAbschnitte = await api.server.getSchuelerLernabschnittsliste(api.schema, idSchueler);
		const listFaecher = await api.server.getFaecher(api.schema);
		const listLehrer = await api.server.getLehrer(api.schema);
		let newState = <RouteStateDataSchuelerLeistungen>{ idSchueler, listAbschnitte, listFaecher, listLehrer };
		const alteAuswahl = this._state.value.auswahl;
		newState = await this.updateSchuljahresabschnitt(newState,
			alteAuswahl === undefined ? undefined : alteAuswahl.schuljahresabschnitt,
			alteAuswahl === undefined ? 0 : alteAuswahl.wechselNr);
		this.setPatchedDefaultState(newState)
	}

	get hatAuswahl() : boolean {
		return (this._state.value.auswahl !== undefined);
	}

	get auswahl(): SchuelerLernabschnittListeEintrag {
		if (this._state.value.auswahl === undefined)
			throw new Error("Unerwarteter Fehler: Lernabschnittseintrag nicht festgelegt, es können keine Informationen zu den Leistungsdaten abgerufen oder eingegeben werden.");
		return this._state.value.auswahl;
	}

	get daten(): SchuelerLernabschnittsdaten {
		if (this._state.value.daten === undefined)
			throw new Error("Unerwarteter Fehler: Leistungsdaten nicht initialisiert");
		return this._state.value.daten;
	}

	public async setLernabschnitt(idSchuljahresabschnitt : number, wechselNr : number) {
		const curAuswahl = this._state.value.auswahl;
		if ((curAuswahl === undefined) || ((idSchuljahresabschnitt === curAuswahl.schuljahresabschnitt)) && (wechselNr === curAuswahl.wechselNr))
			return;
		const newState = await this.updateSchuljahresabschnitt(this._state.value, idSchuljahresabschnitt, wechselNr);
		this.setPatchedState(newState);
	}

	gotoLernabschnitt = async (value: SchuelerLernabschnittListeEintrag) => {
		await RouteManager.doRoute({ name: routeSchuelerLeistungenDaten.name, params: { id: value.schuelerID, abschnitt: value.schuljahresabschnitt, wechselNr: value.wechselNr } });
	}

	patchLeistung = async (data : Partial<SchuelerLeistungsdaten>, id : number) => {
		// TODO await api.server.patchSchuelerLeistungsdaten(data, api.schema, id);
	}

}
