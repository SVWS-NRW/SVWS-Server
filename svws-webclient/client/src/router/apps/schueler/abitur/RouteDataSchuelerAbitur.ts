import type { GostBelegpruefungErgebnis, SchuelerListeEintrag} from "@core";
import { AbiturdatenManager, DeveloperNotificationException, GostBelegpruefungsArt, GostFaecherManager, UserNotificationException } from "@core";
import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { routeSchuelerAbiturZulassung } from "~/router/apps/schueler/abitur/RouteSchuelerAbiturZulassung";

interface RouteStateDataSchuelerAbitur extends RouteStateInterface {
	// Daten, die in Abhängigkeit des ausgewählten Schülers geladen werden
	schueler: SchuelerListeEintrag | null;
	managerLaufbahnplanung: AbiturdatenManager | null;
	ergebnisBelegpruefung: GostBelegpruefungErgebnis | null;
	managerAbitur: AbiturdatenManager | null;
}

const defaultState = <RouteStateDataSchuelerAbitur> {
	schueler: null,
	view: routeSchuelerAbiturZulassung,
	managerLaufbahnplanung: null,
	ergebnisBelegpruefung: null,
	managerAbitur: null,
};


export class RouteDataSchuelerAbitur extends RouteData<RouteStateDataSchuelerAbitur> {

	public constructor() {
		super(defaultState);
	}

	get schueler(): SchuelerListeEintrag {
		const schueler = this._state.value.schueler;
		if (schueler === null)
			throw new DeveloperNotificationException("Ein Aufruf dieser Methode sollte vor der korrekten Initialisierung nicht erfolgen.");
		return schueler;
	}

	get managerLaufbahnplanung() : AbiturdatenManager {
		const managerLaufbahnplanung = this._state.value.managerLaufbahnplanung;
		if (managerLaufbahnplanung === null)
			throw new DeveloperNotificationException("Ein Aufruf dieser Methode sollte vor der korrekten Initialisierung der Laufbahnplanungsdaten nicht erfolgen.");
		return managerLaufbahnplanung;
	}

	get ergebnisBelegpruefung() : GostBelegpruefungErgebnis {
		const ergebnisBelegpruefung = this._state.value.ergebnisBelegpruefung;
		if (ergebnisBelegpruefung === null)
			throw new DeveloperNotificationException("Ein Aufruf dieser Methode sollte vor der korrekten Initialisierung der Laufbahnplanungsdaten nicht erfolgen.");
		return ergebnisBelegpruefung;
	}

	get managerAbitur() : AbiturdatenManager | null {
		return this._state.value.managerAbitur;
	}

	/**
	 * Lädt die Informationen zu der übergebenen Schüler-ID
	 *
	 * @param schueler   der Listeneintrag zu dem Schüler
	 * @param force      gibt an, ob das Laden erzwungen werden soll, obwohl die ID bereits geladen ist
	 */
	public async setSchueler(schueler: SchuelerListeEintrag, force: boolean = false) : Promise<void> {
		if ((!force) && (schueler.id === this._state.value.schueler?.id))
			return;
		// Lade die Informationen zum Abiturjahrgang des Schülers
		if (schueler.abiturjahrgang === null)
			throw new DeveloperNotificationException("Für den Schüler konnte kein Abiturjahrgang ermittelt werden.");
		let gostJahrgangsdaten = undefined;
		let listGostFaecher = undefined;
		let faecherManager = undefined;
		let listFachkombinationen = undefined;
		try {
			gostJahrgangsdaten = await api.server.getGostAbiturjahrgang(api.schema, schueler.abiturjahrgang);
			listGostFaecher = await api.server.getGostAbiturjahrgangFaecher(api.schema, schueler.abiturjahrgang);
			faecherManager = new GostFaecherManager(schueler.abiturjahrgang - 1, listGostFaecher);
			listFachkombinationen = await api.server.getGostAbiturjahrgangFachkombinationen(api.schema, schueler.abiturjahrgang);
			faecherManager.addFachkombinationenAll(listFachkombinationen);
		} catch(error) {
			throw new UserNotificationException("Die Informationen zum Abiturjahrgang " + schueler.abiturjahrgang + " und dessen Fächer konnten nicht vollständig ermittelt werden. Überpfüfen Sie diese Infromationen.");
		}
		const newState = <RouteStateDataSchuelerAbitur>{ schueler, managerLaufbahnplanung: null, ergebnisBelegpruefung: null, managerAbitur: null, view: this._state.value.view };
		try {
			const abiturdaten = await api.server.getGostSchuelerLaufbahnplanung(api.schema, schueler.id);
			newState.managerLaufbahnplanung = new AbiturdatenManager(api.mode, abiturdaten, gostJahrgangsdaten, faecherManager, GostBelegpruefungsArt.GESAMT);
			newState.ergebnisBelegpruefung = newState.managerLaufbahnplanung.getBelegpruefungErgebnis();
			newState.managerLaufbahnplanung.applyErgebnisMarkierungsalgorithmus();
		} catch (e) {
			// do nothing
		}
		try {
			// TODO hole Abiturdaten für das spezielle Abiturjahr
			const abiturdaten = await api.server.getGostSchuelerAbiturdaten(api.schema, schueler.id);
			newState.managerAbitur = new AbiturdatenManager(api.mode, abiturdaten, gostJahrgangsdaten, faecherManager, GostBelegpruefungsArt.GESAMT);
		} catch (e) {
			// do nothing
		}
		this.setPatchedDefaultState(newState)
	}

}
