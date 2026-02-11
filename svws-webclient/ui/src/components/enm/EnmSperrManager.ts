import { HashMap2D } from "../../../../core/src/core/adt/map/HashMap2D";
import { ENMConfigKlasse } from "../../../../core/src/core/data/enm/ENMConfigKlasse";
import type { ENMConfigKlasseSpalte } from "../../../../core/src/core/data/enm/ENMConfigKlasseSpalte";
import { ArrayList } from "../../../../core/src/java/util/ArrayList";
import { HashMap } from "../../../../core/src/java/util/HashMap";
import type { JavaMap } from "../../../../core/src/java/util/JavaMap";
import type { List } from "../../../../core/src/java/util/List";

/**
 * Ein Manager für die Verwaltung der Sperr-Konfiguration bei den Klassen der ENM-Daten.
 */
export class EnmSperrManager {

	/** Die Liste der einzelnen Konfigurationen für die einzelnen Klassen. */
	private readonly sperrungen: List<ENMConfigKlasse>;

	/** Eine Map für den schnellen Zugriff auf die Konfiguration für eine Klasse and der ID der Klasse */
	private readonly mapSperrungen: JavaMap<number, ENMConfigKlasse>;

	/** Eine 2D-Map für den schnellen Zugriff auf die Sperrungen von Spalten einer Klasse. */
	private readonly mapSperrungenSpalten: HashMap2D<number, string, ENMConfigKlasseSpalte>;

	/** Eine 2D-Map für den schnellen Zugriff auf die Sperrungen von Spalten einer Klasse für Teilleistungsarten. */
	private readonly mapSperrungenSpaltenTeilleistung: HashMap2D<number, number, ENMConfigKlasseSpalte>;

	/**
	 * Erstellt einen neuen Manager für die Nutzung der Konfiguration von Sperrungen bei
	 * der Noteneingabe bei Klassen.
	 *
	 * @param json   der JSON-String mit der Konfiguration
	 */
	public constructor(json: string) {
		this.sperrungen = this.mapJsonToListOfObjects(json, o => ENMConfigKlasse.transpilerFromJSON(o));
		this.mapSperrungen = new HashMap<number, ENMConfigKlasse>();
		this.mapSperrungenSpalten = new HashMap2D<number, string, ENMConfigKlasseSpalte>();
		this.mapSperrungenSpaltenTeilleistung = new HashMap2D<number, number, ENMConfigKlasseSpalte>();
		for (const sperrung of this.sperrungen) {
			this.mapSperrungen.put(sperrung.id, sperrung);
			for (const col of sperrung.spalten) {
				this.mapSperrungenSpalten.put(sperrung.id, col.name, col);
				if (col.idTeilleistung !== null) {
					this.mapSperrungenSpaltenTeilleistung.put(sperrung.id, col.idTeilleistung, col);
				}
			}
		}
	}

	/**
	 * Führt ein Mapping von dem übergebenen JSON, welches ein Array beinhaltet zu einer Liste von Objekten durch.
	 * Die einzelnen Objekte werden mit dem übergebenen Lambda-Ausdruck umgewandelt.
	 *
	 * @param json              der JSON-String
	 * @param mapJsonToObject   der Mapper für ein einzelnes Objekt der Liste
	 *
	 * @returns die Liste von Objekten
	 */
	private mapJsonToListOfObjects<T>(json: string, mapJsonToObject: (value: string) => T): List<T> {
		const configs: any[] | null = JSON.parse(json);
		const liste = new ArrayList<T>();
		if (configs !== null) {
			for (const config of configs) {
				liste.add(mapJsonToObject(JSON.stringify(config)));
			}
		}
		return liste;
	}

	/**
	 * Gibt zurück, ob die Klasse vollständig gesperrt ist oder nicht.
	 *
	 * @param idKlasse   die ID der Klasse
	 *
	 * @returns true, wenn die Klasse gesperrt ist, und ansonsten false
	 */
	public istEingabeErlaubt(idKlasse: number): boolean {
		const config = this.mapSperrungen.get(idKlasse);
		if (config === null) {
			return false;
		}

		if (config.tsEingabeAb !== null) {
			if (this.now() < config.tsEingabeAb) {
				return false;
			}
		}
		if (config.tsEingabeBis !== null) {
			if (this.now() > config.tsEingabeBis) {
				return false;
			}
		}
		return true;
	}

	private now(): string {
		const now = new Date();
		return now.toLocaleString('en-CA', {
			timeZone: 'Europe/Berlin',
			year: 'numeric',
			month: '2-digit',
			day: '2-digit',
			hour: '2-digit',
			minute: '2-digit',
			second: '2-digit',
			hour12: false,
			fractionalSecondDigits: 3,
		}).replace(', ', ' ');
	}

	/**
	 * Gibt zurück, ob die Fehlstundeneingabe für die Klasse erfolgt oder für die einzelnen Lerngruppen.
	 *
	 * @param idKlasse   die ID der Klasse
	 *
	 * @returns true, wenn die Fehlstundeneingabe klassenweise erfolgt.
	 */
	public istFehlstundeneingabeKlassenweise(idKlasse: number): boolean {
		const config = this.mapSperrungen.get(idKlasse);
		if (config === null) {
			return true; // Wert nicht relevant, da die Eingabe ja komplett gesperrt ist
		}
		/**
		 * die Semantik des Attributs und der anderen Sperren ist hier nicht ideal:
		 * Sperren ist immer true, hier wird bei klassenweise auch true gesetzt, obwohl das Gegenteil gemeint ist.
		 * Wird an dieser Stelle erstmal nur negiert.
		 */
		return !config.istFehlstundenEingabeKlassenweise;
	}

	/**
	 * Gibt zurück, ob die Eingabe für die Klasse in der Konfiguration für eine Spalte freigeschaltet ist.
	 *
	 * @param idKlasse    die ID der Klasse
	 * @param colname     der Spaltenname, für welche die Eingabe erfolgt.
	 *
	 * @returns true, wenn die Eingabe erlaubt ist, und ansonsten false
	 */
	public istSpalteneingabeErlaubt(idKlasse: number, colname: string): boolean {
		if (!this.istEingabeErlaubt(idKlasse)) {
			return false;
		}
		const config = this.mapSperrungenSpalten.getOrNull(idKlasse, colname);
		if (config === null) {
			return false; // Sollte nicht vorkommen - in dem Fall wäre die Konfiguration fehlerhaft
		}
		return !config.gesperrt;
	}

	/**
	 * Gibt zurück, ob die Fehlstundeneingabe für die Klasse in der Konfiguration freigeschaltet ist.
	 *
	 * @param idKlasse    die ID der Klasse
	 * @param istGesamt   gibt dabei an, ob die Anfrage für die Eingabe bei Klassen (true) oder Lerngruppen (false) erfolgt
	 *
	 * @returns true, wenn die Fehlstundeneingabe erlaubt ist, und ansonsten false
	 */
	public istFehlstundeneingabeErlaubt(idKlasse: number, istGesamt: boolean): boolean {
		if (!this.istEingabeErlaubt(idKlasse)) {
			return false;
		}
		if (this.istFehlstundeneingabeKlassenweise(idKlasse) !== istGesamt) {
			return false;
		}
		const config = this.mapSperrungenSpalten.getOrNull(idKlasse, "Fehlstunden");
		if (config === null) {
			return false; // Sollte nicht vorkommen - in dem Fall wäre die Konfiguration fehlerhaft
		}
		return !config.gesperrt;
	}

	/**
	 * Gibt zurück, ob die Eingabe für die Klasse in der Konfiguration für eine Teilleistungsart freigeschaltet ist.
	 *
	 * @param idKlasse    die ID der Klasse
	 * @param idArt       die ID der Teilleistungsart
	 *
	 * @returns true, wenn die Eingabe erlaubt ist, und ansonsten false
	 */
	public istTeilleistungseingabeErlaubt(idKlasse: number, idArt: number): boolean {
		if (!this.istSpalteneingabeErlaubt(idKlasse, "Teilnoten")) {
			return false;
		}
		const config = this.mapSperrungenSpaltenTeilleistung.getOrNull(idKlasse, idArt);
		if (config === null) {
			return false; // Sollte nicht vorkommen - in dem Fall wäre die Konfiguration fehlerhaft
		}
		return !config.gesperrt;
	}


}
