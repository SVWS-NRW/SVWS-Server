import type { List, FachDaten, LehrerListeEintrag, SchuelerLeistungsdaten, SchuelerLernabschnittListeEintrag, SchuelerLernabschnittsdaten, FoerderschwerpunktEintrag, JahrgangsDaten, SchuelerLernabschnittBemerkungen, GostSchuelerklausurTermin} from "@core";
import { ArrayList, DeveloperNotificationException, GostHalbjahr, GostKlausurplanManager, HashMap, KursManager, SchuelerLernabschnittManager } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";
import { routeApp } from "~/router/apps/RouteApp";
import { routeSchuelerLernabschnittLeistungen } from "~/router/apps/schueler/lernabschnitte/RouteSchuelerLernabschnittLeistungen";
import { routeSchueler } from "../RouteSchueler";
import { routeSchuelerLernabschnittGostKlausuren } from "./RouteSchuelerLernabschnittGostKlausuren";


interface RouteStateDataSchuelerLernabschnitte extends RouteStateInterface {
	// Daten, die in Abhängigkeit des ausgewählten Schülers geladen werden
	idSchueler: number;
	listAbschnitte: List<SchuelerLernabschnittListeEintrag>;
	hatGymOb: boolean;
	listFaecher: List<FachDaten>;
	listFoerderschwerpunkte: List<FoerderschwerpunktEintrag>;
	listJahrgaenge: List<JahrgangsDaten>;
	listLehrer: List<LehrerListeEintrag>;
	// Daten, die in Abhängigkeit des ausgewählten Lernabschnitts geladen werden
	auswahl: SchuelerLernabschnittListeEintrag | undefined;
	daten: SchuelerLernabschnittsdaten | undefined;
	manager: SchuelerLernabschnittManager | undefined;
	klausurManager: GostKlausurplanManager | undefined;
}

const defaultState = <RouteStateDataSchuelerLernabschnitte> {
	idSchueler: -1,
	listAbschnitte: new ArrayList<SchuelerLernabschnittListeEintrag>(),
	hatGymOb: false,
	listFaecher: new ArrayList(),
	listFoerderschwerpunkte: new ArrayList(),
	listJahrgaenge: new ArrayList(),
	listLehrer: new ArrayList(),
	auswahl: undefined,
	daten: undefined,
	manager: undefined,
	klausurManager: undefined,
	view: routeSchuelerLernabschnittLeistungen,
};


export class RouteDataSchuelerLernabschnitte extends RouteData<RouteStateDataSchuelerLernabschnitte> {

	public constructor() {
		super(defaultState);
	}

	get idSchueler(): number {
		return this._state.value.idSchueler;
	}

	get listAbschnitte(): List<SchuelerLernabschnittListeEintrag> {
		return this._state.value.listAbschnitte;
	}

	get hatGymOb(): boolean {
		return this._state.value.hatGymOb;
	}

	get manager(): SchuelerLernabschnittManager {
		if (this._state.value.manager === undefined)
			throw new DeveloperNotificationException("Unerwarteter Fehler: Schüler-Lernabschnittsdaten nicht initialisiert");
		return this._state.value.manager;
	}

	get hatKlausurManager() : boolean {
		return (this._state.value.klausurManager !== undefined);
	}

	get klausurManager(): GostKlausurplanManager {
		if (this._state.value.klausurManager === undefined)
			throw new DeveloperNotificationException("Unerwarteter Fehler: Schüler-Klausurmanager nicht initialisiert");
		return this._state.value.klausurManager;
	}

	protected getLernabschnitt(curState : RouteStateDataSchuelerLernabschnitte, idSchuljahresabschnitt : number, wechselNr : number) : SchuelerLernabschnittListeEintrag | undefined {
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

	protected async updateSchuljahresabschnitt(curState : RouteStateDataSchuelerLernabschnitte, newSchuljahresabschnitt : number | undefined, newWechselNr : number) : Promise<RouteStateDataSchuelerLernabschnitte> {
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
		let listKurse;
		let listKlassen;
		if ((this.hatAuswahl) && (found.schuljahresabschnitt === this.auswahl.schuljahresabschnitt)) {
			listKurse = this.manager.kursGetMenge();
			listKlassen = this.manager.klasseGetMenge();
		} else {
			[ listKurse, listKlassen ] = await Promise.all([
				api.server.getKurseFuerAbschnitt(api.schema, found.schuljahresabschnitt),
				api.server.getKlassenFuerAbschnitt(api.schema, found.schuljahresabschnitt),
			]);
		}
		const schueler = routeSchueler.data.schuelerListeManager.auswahl();
		const mapSchuljahresabschnitte = api.mapAbschnitte.value;
		const schuljahresabschnitt = mapSchuljahresabschnitte.get(daten.schuljahresabschnitt);
		if (schuljahresabschnitt === undefined)
			throw new DeveloperNotificationException("Der Schülerlernabschnitt hat keinen gültigen Schuljahresabschnitt zugeordnet. Dies darf nicht vorkommen.");
		const manager = new SchuelerLernabschnittManager(api.schulform, schueler, daten, schuljahresabschnitt, curState.listFaecher, curState.listFoerderschwerpunkte, curState.listJahrgaenge, listKlassen, listKurse, curState.listLehrer);
		let klausurManager = undefined;
		const abiturjahrgang = routeSchueler.data.schuelerListeManager.auswahl().abiturjahrgang;
		if (routeSchuelerLernabschnittGostKlausuren.hatEineKompetenz() && abiturjahrgang !== null) {
			const halbjahr = GostHalbjahr.fromAbiturjahrSchuljahrUndHalbjahr(abiturjahrgang, found.schuljahr, found.abschnitt);
			if (halbjahr !== null) {
				const gostKlausurCollection = await api.server.getGostKlausurenCollectionBySchuelerid(api.schema, schueler.id, abiturjahrgang, halbjahr.id);
				klausurManager = new GostKlausurplanManager(found.schuljahr, gostKlausurCollection.vorgaben, gostKlausurCollection.kursklausuren, gostKlausurCollection.termine, gostKlausurCollection.schuelerklausuren, gostKlausurCollection.schuelerklausurtermine);
				klausurManager.setKursManager(new KursManager(listKurse));
				const mapLehrer = new HashMap<number, LehrerListeEintrag>();
				for (const l of curState.listLehrer)
					mapLehrer.put(l.id, l);
				klausurManager.setLehrerMap(mapLehrer);
			}
		}
		curState = Object.assign({ ... curState }, { auswahl: found, daten, manager, klausurManager });
		return curState;
	}


	/**
	 * Lädt die Informationen zu den Lernabschnitten anhand der übergebenen Schüler-ID
	 *
	 * @param idSchueler   die ID des Schülers
	 * @param force        gibt an, ob das Laden erzwungen werden soll, obwohl die ID bereits geladen ist
	 */
	public async setSchueler(idSchueler: number, force: boolean = false) : Promise<void> {
		if ((!force) && (idSchueler === this._state.value.idSchueler))
			return;
		const listAbschnitte = await api.server.getSchuelerLernabschnittsliste(api.schema, idSchueler);
		let hatGymOb : boolean = false;
		for (const abschnitt of listAbschnitte) {
			if (("EF" === abschnitt.jahrgang) || ("Q1" === abschnitt.jahrgang) || ("Q2" === abschnitt.jahrgang)) {
				hatGymOb = true;
				break;
			}
		}
		let listFaecher;
		let listFoerderschwerpunkte;
		let listJahrgaenge;
		let listLehrer;
		if (this.hatAuswahl) {
			listFaecher = this.manager.fachGetMenge();
			listFoerderschwerpunkte = this.manager.foerderschwerpunktGetMenge();
			listJahrgaenge = this.manager.jahrgangGetMenge();
			listLehrer = this.manager.lehrerGetMenge();
		} else {
			[ listFaecher, listFoerderschwerpunkte, listJahrgaenge, listLehrer ] = await Promise.all([
				api.server.getFaecher(api.schema),
				api.server.getSchuelerFoerderschwerpunkte(api.schema),
				api.server.getJahrgaenge(api.schema),
				api.server.getLehrer(api.schema),
			]);
		}
		let newState = <RouteStateDataSchuelerLernabschnitte>{ idSchueler, listAbschnitte, hatGymOb, listFaecher, listFoerderschwerpunkte, listJahrgaenge, listLehrer, view: this._state.value.view };
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
			throw new DeveloperNotificationException("Unerwarteter Fehler: Lernabschnittseintrag nicht festgelegt, es können keine Informationen zu den Leistungsdaten abgerufen oder eingegeben werden.");
		return this._state.value.auswahl;
	}

	get daten(): SchuelerLernabschnittsdaten {
		if (this._state.value.daten === undefined)
			throw new DeveloperNotificationException("Unerwarteter Fehler: Lernabschnittdaten nicht initialisiert");
		return this._state.value.daten;
	}

	public async setLernabschnitt(idSchuljahresabschnitt : number, wechselNr : number) {
		const curAuswahl = this._state.value.auswahl;
		if ((curAuswahl === undefined) || ((idSchuljahresabschnitt === curAuswahl.schuljahresabschnitt) && (wechselNr === curAuswahl.wechselNr)))
			return;
		const newState = await this.updateSchuljahresabschnitt(this._state.value, idSchuljahresabschnitt, wechselNr);
		this.setPatchedState(newState);
	}

	gotoLernabschnitt = async (value: SchuelerLernabschnittListeEintrag) => {
		await RouteManager.doRoute({ name: this.view.name, params: { id: value.schuelerID, abschnitt: value.schuljahresabschnitt, wechselNr: value.wechselNr } });
	}

	patchLernabschnitt = async (data : Partial<SchuelerLernabschnittsdaten>) => {
		await api.server.patchSchuelerLernabschnittsdaten(data, api.schema, this.daten.id);
		Object.assign(this.daten, data);
		this.commit();
	}

	patchLeistung = async (data : Partial<SchuelerLeistungsdaten>, id : number) => {
		await api.server.patchSchuelerLeistungsdaten(data, api.schema, id);
		const leistung = this.manager.leistungGetByIdOrException(id);
		Object.assign(leistung, data);
		this.commit();
	}

	addLeistung = async (fachID : number) => {
		const data : Partial<SchuelerLeistungsdaten> = { lernabschnittID: this.daten.id, fachID };
		const result = await api.server.addSchuelerLeistungsdaten(data, api.schema);
		this.manager.leistungAdd(result);
		this.commit();
	}

	deleteLeistungen = async (leistungenIDs: List<number>) => {
		const leistungen = await api.server.deleteSchuelerLeistungsdatenMultiple(leistungenIDs, api.schema);
		for (const l of leistungen)
			this.manager.leistungRemoveByID(l.id);
		this.commit();
	}

	patchBemerkungen = async (data : Partial<SchuelerLernabschnittBemerkungen>) : Promise<void> => {
		await api.server.patchSchuelerLernabschnittsdatenBemerkungen(data, api.schema, this.daten.id);
		Object.assign(this.daten.bemerkungen, data);
		this.commit();
	}

	createSchuelerklausurTermin = async (id: number) => {
		const skNeu = await api.server.createGostKlausurenSchuelerklausurtermin(api.schema, id);
		this.klausurManager.schuelerklausurterminAdd(skNeu);
		this.commit();
	}

	deleteSchuelerklausurTermin = async (skt : GostSchuelerklausurTermin) => {
		await api.server.deleteGostKlausurenSchuelerklausurtermin(api.schema, skt.id);
		this.klausurManager.schuelerklausurterminRemoveById(skt.id);
		this.commit();
	}

	patchSchuelerklausurTermin = async (id: number, skt : Partial<GostSchuelerklausurTermin>) => {
		await api.server.patchGostKlausurenSchuelerklausurtermin(skt, api.schema, id);
	}

	gotoPlanung = async() => {
		const abiturjahr = this.manager.schuelerGet().abiturjahrgang;
		if (abiturjahr === null)
			return;
		const schuljahr = this.manager.schuljahresabschnittGet().schuljahr;
		const abschnitt = this.manager.schuljahresabschnittGet().abschnitt;
		const halbjahr = GostHalbjahr.fromAbiturjahrSchuljahrUndHalbjahr(abiturjahr, schuljahr, abschnitt);
		if (halbjahr === null)
			return;
		await RouteManager.doRoute({ name: "gost.klausurplanung.nachschreiber", params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr, halbjahr: halbjahr.id }});
	}

}
