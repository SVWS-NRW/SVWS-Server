import type { AbiturFachbelegung, GostBelegpruefungErgebnis, GostJahrgang, JavaMap, KursDaten, LehrerListeEintrag, List, SchuelerListeEintrag } from "@core";
import { Abiturdaten, AbiturdatenManager, ArrayList, HashMap, DeveloperNotificationException, GostBelegpruefungsArt, GostFaecherManager, UserNotificationException } from "@core";
import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { routeGostAbiturZulassung } from "~/router/apps/gost/abitur/RouteGostAbiturZulassung";

interface RouteStateDataGostAbitur extends RouteStateInterface {
	// Daten, die in Abhängigkeit des ausgewählten Schülers geladen werden
	abiJahrgang: GostJahrgang | null;
	schuelerListe: List<SchuelerListeEintrag>;
	mapLehrer: JavaMap<number, LehrerListeEintrag>;
	mapKurse: JavaMap<number, KursDaten>;
	managerLaufbahnplanungMap: JavaMap<number, AbiturdatenManager>;
	ergebnisBelegpruefungMap: JavaMap<number, GostBelegpruefungErgebnis>;
	managerAbiturMap: JavaMap<number, AbiturdatenManager>;
}

const defaultState = <RouteStateDataGostAbitur> {
	abiJahrgang: null,
	schuelerListe: new ArrayList<SchuelerListeEintrag>(),
	mapLehrer: new HashMap<number, LehrerListeEintrag>(),
	mapKurse: new HashMap<number, KursDaten>(),
	view: routeGostAbiturZulassung,
	managerLaufbahnplanungMap: new HashMap<number, AbiturdatenManager>(),
	ergebnisBelegpruefungMap: new HashMap<number, GostBelegpruefungErgebnis>(),
	managerAbiturMap: new HashMap<number, AbiturdatenManager>(),
};


export class RouteDataGostAbitur extends RouteData<RouteStateDataGostAbitur> {

	public constructor() {
		super(defaultState);
	}

	get schuelerListe(): List<SchuelerListeEintrag> {
		return this._state.value.schuelerListe;
	}

	get mapLehrer(): JavaMap<number, LehrerListeEintrag> {
		return this._state.value.mapLehrer;
	}

	get mapKurse(): JavaMap<number, KursDaten> {
		return this._state.value.mapKurse;
	}

	get managerLaufbahnplanungMap() : JavaMap<number, AbiturdatenManager> {
		return this._state.value.managerLaufbahnplanungMap;
	}

	get ergebnisBelegpruefungMap() : JavaMap<number, GostBelegpruefungErgebnis> {
		return this._state.value.ergebnisBelegpruefungMap;
	}

	get managerAbiturMap() : JavaMap<number, AbiturdatenManager> {
		return this._state.value.managerAbiturMap;
	}

	/**
	 * Lädt die Informationen zu dem übergebeben Abiturjahrgang
	 *
	 * @param abiJahrgang   die Informationen zum Abiturjahrgang
	 * @param force         gibt an, ob das Laden erzwungen werden soll, obwohl die ID bereits geladen ist
	 */
	public async setAbiturjahr(abiJahrgang: GostJahrgang, force: boolean = false) : Promise<void> {
		if ((!force) && (abiJahrgang.abiturjahr === this._state.value.abiJahrgang?.abiturjahr))
			return;
		// Lade die Informationen zum Abiturjahrgang des Schülers
		if (abiJahrgang.abiturjahr < 0)
			throw new DeveloperNotificationException("Für den Schüler konnte kein Abiturjahrgang ermittelt werden.");

		const mapKurse = new HashMap<number, KursDaten>();
		try {
			const schuljahresabschnitt = api.getAbschnittBySchuljahrUndHalbjahr(abiJahrgang.abiturjahr - 1, 2);
			if (schuljahresabschnitt !== undefined) {
				const listKurse = await api.server.getKurseFuerAbschnitt(api.schema, schuljahresabschnitt.id);
				for (const kurs of listKurse)
					mapKurse.put(kurs.id, kurs);
			}
		} catch(error) {
			// Das Laden der Kurse kann fehlschlagen, wenn der Schuljahresabschnitt für die Q2.2 noch nicht existiert
		}

		let schuelerListe = undefined;
		const mapLehrer = new HashMap<number, LehrerListeEintrag>();
		let gostJahrgangsdaten = undefined;
		let listGostFaecher = undefined;
		let faecherManager = undefined;
		let listFachkombinationen = undefined;
		try {
			schuelerListe = await api.server.getGostAbiturjahrgangSchueler(api.schema, abiJahrgang.abiturjahr);
			const listLehrer = await api.server.getLehrer(api.schema);
			for (const lehrer of listLehrer)
				mapLehrer.put(lehrer.id, lehrer);
			gostJahrgangsdaten = await api.server.getGostAbiturjahrgang(api.schema, abiJahrgang.abiturjahr);
			listGostFaecher = await api.server.getGostAbiturjahrgangFaecher(api.schema, abiJahrgang.abiturjahr);
			faecherManager = new GostFaecherManager(abiJahrgang.abiturjahr - 1, listGostFaecher);
			listFachkombinationen = await api.server.getGostAbiturjahrgangFachkombinationen(api.schema, abiJahrgang.abiturjahr);
			faecherManager.addFachkombinationenAll(listFachkombinationen);
		} catch(error) {
			throw new UserNotificationException("Die Informationen zum Abiturjahrgang " + abiJahrgang.abiturjahr +
				", dessen Schüler und dessen Fächer konnten nicht vollständig ermittelt werden. Überpfüfen Sie diese Infromationen.");
		}
		const newState = <RouteStateDataGostAbitur>{ abiJahrgang, schuelerListe, mapLehrer, mapKurse,
			managerLaufbahnplanungMap: new HashMap<number, AbiturdatenManager>(),
			ergebnisBelegpruefungMap: new HashMap<number, GostBelegpruefungErgebnis>(),
			managerAbiturMap: new HashMap<number, AbiturdatenManager>(), view: this._state.value.view };
		try {
			// TODO API-Endpunkt zum Auslesen aller Laufbahnplanungsdaten des Jahrgangs ergänzen und hier verwenden - getGostAbiturjahrgangLaufbahndaten
			const abiturdatenListe = await api.server.getGostAbiturjahrgangLaufbahndaten(api.schema, abiJahrgang.abiturjahr);
			for (const abiturdaten of abiturdatenListe) {
				const manager = new AbiturdatenManager(api.mode, abiturdaten, gostJahrgangsdaten, faecherManager, GostBelegpruefungsArt.GESAMT);
				newState.managerLaufbahnplanungMap.put(abiturdaten.schuelerID, manager);
				newState.ergebnisBelegpruefungMap.put(abiturdaten.schuelerID, manager.getBelegpruefungErgebnis());
				manager.applyErgebnisMarkierungsalgorithmus();
			}
		} catch (e) {
			// do nothing
		}
		try {
			// TODO hole Abiturdaten für das spezielle Abiturjahr
			const abiturdatenListe = await api.server.getGostAbiturjahrgangAbiturdaten(api.schema, abiJahrgang.abiturjahr);
			for (const abiturdaten of abiturdatenListe)
				newState.managerAbiturMap.put(abiturdaten.schuelerID, new AbiturdatenManager(api.mode, abiturdaten, gostJahrgangsdaten, faecherManager, GostBelegpruefungsArt.GESAMT));
		} catch (e) {
			// do nothing
		}
		this.setPatchedDefaultState(newState)
	}

	copyAbiturdatenAusLeistungsdaten = async (idSchueler: number) : Promise<void> => {
		const managerLaufbahnplanung = this.managerLaufbahnplanungMap.get(idSchueler);
		if (managerLaufbahnplanung === null)
			return;
		// Kopiere die Leistungsdaten auf dem Server ...
		await api.server.copyGostSchuelerAbiturdatenAusLeistungsdaten(api.schema, idSchueler);
		// ... und lade diese dann vom Server
		const abiturdaten = await api.server.getGostSchuelerAbiturdaten(api.schema, idSchueler);
		this.managerAbiturMap.put(idSchueler, new AbiturdatenManager(api.mode, abiturdaten, managerLaufbahnplanung.jahrgangsdaten(), managerLaufbahnplanung.faecher(), GostBelegpruefungsArt.GESAMT));
		this.commit();
	}

	updateAbiturpruefungsdaten = async (manager: () => AbiturdatenManager, belegung: Partial<AbiturFachbelegung>, berechnePflichtpruefungenNeu: boolean) => {
		if (belegung.fachID === undefined)
			throw new DeveloperNotificationException("Die FachID muss bei der Abitur-Fachbelegung gesetzt sein, damit ein Patchen möglich ist.");
		// Erstelle eine Kopie der Abiturdaten, wo die Fachbelegung aus dem Patch angepasst wurde.
		const orig = manager().daten();
		const clone = Abiturdaten.transpilerFromJSON(Abiturdaten.transpilerToJSON(orig));
		let found = false;
		for (const tmpBelegung of clone.fachbelegungen) {
			if (tmpBelegung.fachID === belegung.fachID) {
				Object.assign(tmpBelegung, belegung);
				found = true;
				break;
			}
		}
		if (!found)
			throw new DeveloperNotificationException("Die FachID ist in den Abiturdaten nicht als Belegung vorhanden.");
		// Berechnen des Prüfungsergebnisses und Senden an den Server
		AbiturdatenManager.berechnePruefungsergebnis(clone, berechnePflichtpruefungenNeu);
		await api.server.patchGostSchuelerAbiturdaten(clone, api.schema, clone.schuelerID);
		// Patchen der Originaldaten und dortige Berechnung des Prüfungsergebnisses nach erfolgreichem Senden an den Server
		for (const tmpBelegung of orig.fachbelegungen) {
			if (tmpBelegung.fachID === belegung.fachID) {
				Object.assign(tmpBelegung, belegung);
				break;
			}
		}
		manager().pruefeZulassung();
		AbiturdatenManager.berechnePruefungsergebnis(orig, berechnePflichtpruefungenNeu);
		this.commit();
	}

}
