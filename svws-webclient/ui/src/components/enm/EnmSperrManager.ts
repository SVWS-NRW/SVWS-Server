import { HashMap2D } from "@core/core/adt/map/HashMap2D";
import { ENMConfigKlasse } from "@core/core/data/enm/ENMConfigKlasse";
import { ENMConfigKlasseSpalte } from "@core/core/data/enm/ENMConfigKlasseSpalte";
import { ENMLehrerFunktion } from "@core/core/data/enm/ENMLehrerFunktion";
import { ArrayList } from "@core/java/util/ArrayList";
import { Arrays } from "@core/java/util/Arrays";
import { HashMap } from "@core/java/util/HashMap";
import { HashSet } from "@core/java/util/HashSet";
import type { JavaMap } from "@core/java/util/JavaMap";
import type { List } from "@core/java/util/List";

/**
 * Ein Manager für die Verwaltung der Sperr-Konfiguration bei den Klassen der ENM-Daten.
 */
export class EnmSperrManager {

	/** Die Funktion der Lehrkraft */
	private readonly lehrerFunktion: ENMLehrerFunktion;

	/** Die Liste der einzelnen Konfigurationen für die einzelnen Klassen. */
	private readonly sperrungen: List<ENMConfigKlasse>;

	/** Eine Map für den schnellen Zugriff auf die Konfiguration für eine Klasse and der ID der Klasse */
	private readonly mapSperrungen: JavaMap<number, ENMConfigKlasse>;

	/** Eine 2D-Map für den schnellen Zugriff auf die Sperrungen von Spalten einer Klasse. */
	private readonly mapSperrungenSpalten: HashMap2D<number, string, ENMConfigKlasseSpalte>;

	/** Eine 2D-Map für den schnellen Zugriff auf die Sperrungen von Spalten einer Klasse für Teilleistungsarten. */
	private readonly mapSperrungenSpaltenTeilleistung: HashMap2D<number, number, ENMConfigKlasseSpalte>;

	/** Ein Set mit Spaltennamen, die Noten betreffen und für die Freigabe für Klassenleitungen relevant sind */
	private readonly setSpalteLeistungen = new HashSet<string>(Arrays.asList("Quartal", "Note", "Mahnung", "FB", "Teilnoten"));

	/**
	 * Erstellt einen neuen Manager für die Nutzung der Konfiguration von Sperrungen bei
	 * der Noteneingabe bei Klassen.
	 *
	 * @param json   der JSON-String mit der Konfiguration
	 */
	public constructor(json: string, lehrerFunktion: ENMLehrerFunktion) {
		this.lehrerFunktion = lehrerFunktion;
		this.sperrungen = this.mapJsonToListOfObjects(json, o => this.mapJsonToENMConfigKlasse(o));
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

	private mapJsonToENMConfigKlasse(input: any): ENMConfigKlasse {
		const obj = input as Partial<ENMConfigKlasse>;
		const result = new ENMConfigKlasse();
		if (obj.id === undefined) {
			throw new Error('invalid json format, missing attribute id');
		}
		result.id = obj.id;
		result.tsEingabeAb = obj.tsEingabeAb ?? null;
		result.tsEingabeBis = obj.tsEingabeBis ?? null;
		result.tsEingabeBisKlassenleitung = obj.tsEingabeBisKlassenleitung ?? null;
		result.istNoteneingabeDurchKlassenleitung = obj.istNoteneingabeDurchKlassenleitung ?? false;
		result.istFehlstundenEingabeKlassenweise = obj.istFehlstundenEingabeKlassenweise ?? false;
		if (obj.spalten !== undefined) {
			for (const elem of obj.spalten) {
				result.spalten.add(ENMConfigKlasseSpalte.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
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
				liste.add(mapJsonToObject(config));
			}
		}
		return liste;
	}

	/**
	 * Gibt zurück, ob die Eingabe für die Klasse zeitlich erlaubt ist. Das Eingabeende hängt von den Rollen ab,
	 * über welche die Berechtigung besteht (siehe getEingabeende).
	 *
	 * @param idKlasse         die ID der Klasse
	 * @param berechtigungen   die Lehrer-Funktionen, über welche die Berechtigung besteht
	 *
	 * @returns true, wenn die Eingabe erlaubt ist, und ansonsten false
	 */
	public istEingabeErlaubt(idKlasse: number, berechtigungen: Set<ENMLehrerFunktion>): boolean {
		const config = this.mapSperrungen.get(idKlasse);
		if (config === null) {
			return false;
		}
		if (config.tsEingabeAb !== null) {
			if (this.now() < config.tsEingabeAb) {
				return false;
			}
		}
		const ende = this.getEingabeende(config, berechtigungen);
		return (ende === null) || (this.now() <= ende);
	}

	/**
	 * Bestimmt das Eingabeende für die übergebenen Rollen. Für eine Leitungsfunktion und den Notenmodul-Administrator
	 * gilt das Eingabeende der Klassenleitung, sofern eines gesetzt ist. Die Konfiguration stellt sicher, dass es nicht
	 * vor dem Eingabeende liegt. Die Server wenden dieselbe Regel an.
	 *
	 * @param config           die Konfiguration der Klasse
	 * @param berechtigungen   die Lehrer-Funktionen, über welche die Berechtigung besteht
	 *
	 * @returns das Eingabeende oder null, wenn keines gesetzt ist
	 */
	private getEingabeende(config: ENMConfigKlasse, berechtigungen: Set<ENMLehrerFunktion>): string | null {
		if (config.tsEingabeBis === null) {
			return null;
		}
		const istLeitung = (this.lehrerFunktion === ENMLehrerFunktion.NOTENMODULADMIN) || berechtigungen.has(ENMLehrerFunktion.KLASSENLEITUNG)
			|| berechtigungen.has(ENMLehrerFunktion.ABTEILUNGSLEITUNG) || berechtigungen.has(ENMLehrerFunktion.SCHULLEITUNG);
		return istLeitung ? (config.tsEingabeBisKlassenleitung ?? config.tsEingabeBis) : config.tsEingabeBis;
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
	 * @param idKlasse         die ID der Klasse
	 * @param colname          der Spaltenname, für welche die Eingabe erfolgt.
	 * @param berechtigungen   die Lehrer-Funktionen für vorhandenen Berechtigung für die Anfrage
	 *
	 * @returns true, wenn die Eingabe erlaubt ist, und ansonsten false
	 */
	public istSpalteneingabeErlaubt(idKlasse: number, colname: string, berechtigungen: Set<ENMLehrerFunktion>): boolean {
		const rollen = this.setSpalteLeistungen.contains(colname) ? this.getBerechtigungenLeistungen(idKlasse, berechtigungen) : berechtigungen;
		return this.istSpalteneingabeOhneFreigabeErlaubt(idKlasse, colname, rollen);
	}

	/**
	 * Gibt zurück, ob die Eingabe für die Klasse in der Konfiguration für eine Spalte freigeschaltet ist, ohne die
	 * Freigabe der Noteneingabe für die Klassenleitung zu prüfen. Das gilt für Eingaben, für welche die Klassenleitung
	 * selbst zuständig ist, z. B. sonstige Ankreuzkompetenzen ohne Fach.
	 *
	 * @param idKlasse         die ID der Klasse
	 * @param colname          der Spaltenname, für welche die Eingabe erfolgt.
	 * @param berechtigungen   die Lehrer-Funktionen für vorhandenen Berechtigung für die Anfrage
	 *
	 * @returns true, wenn die Eingabe erlaubt ist, und ansonsten false
	 */
	public istSpalteneingabeOhneFreigabeErlaubt(idKlasse: number, colname: string, berechtigungen: Set<ENMLehrerFunktion>): boolean {
		// ist die Eingabe grundsätzlich erlaubt?
		if (!this.istEingabeErlaubt(idKlasse, berechtigungen)) {
			return false;
		}

		const config = this.mapSperrungenSpalten.getOrNull(idKlasse, colname);
		if (config === null) {
			return false; // Sollte nicht vorkommen - in dem Fall wäre die Konfiguration fehlerhaft
		}
		if (config.gesperrt) {
			return false;
		}
		return (berechtigungen.size > 0);
	}

	/**
	 * Gibt zurück, ob die Eingabe von Leistungsdaten für die Klasse zeitlich erlaubt ist.
	 *
	 * @param idKlasse         die ID der Klasse
	 * @param berechtigungen   die Lehrer-Funktionen, über welche die Berechtigung besteht
	 *
	 * @returns true, wenn die Eingabe erlaubt ist, und ansonsten false
	 */
	public istEingabeErlaubtLeistungen(idKlasse: number, berechtigungen: Set<ENMLehrerFunktion>): boolean {
		return this.istEingabeErlaubt(idKlasse, this.getBerechtigungenLeistungen(idKlasse, berechtigungen));
	}

	/**
	 * Entfernt die Klassenleitung aus den Berechtigungen, wenn die Klasse die Noteneingabe durch die Klassenleitung
	 * nicht freigibt. Die Server werten die Rolle dann bei Leistungsdaten auch für das Eingabeende nicht.
	 *
	 * @param idKlasse         die ID der Klasse
	 * @param berechtigungen   die Lehrer-Funktionen, über welche die Berechtigung besteht
	 *
	 * @returns die für Leistungsdaten wirksamen Lehrer-Funktionen
	 */
	private getBerechtigungenLeistungen(idKlasse: number, berechtigungen: Set<ENMLehrerFunktion>): Set<ENMLehrerFunktion> {
		if (!berechtigungen.has(ENMLehrerFunktion.KLASSENLEITUNG) || (this.mapSperrungen.get(idKlasse)?.istNoteneingabeDurchKlassenleitung ?? false)) {
			return berechtigungen;
		}
		const result = new Set(berechtigungen);
		result.delete(ENMLehrerFunktion.KLASSENLEITUNG);
		return result;
	}

	/**
	 * Gibt zurück, ob die Fehlstundeneingabe für die Klasse in der Konfiguration freigeschaltet ist.
	 *
	 * @param idKlasse         die ID der Klasse
	 * @param istGesamt        gibt dabei an, ob die Anfrage für die Eingabe bei Klassen (true) oder Lerngruppen (false) erfolgt
	 * @param berechtigungen   die Lehrer-Funktionen für vorhandenen Berechtigung für die Anfrage
	 *
	 * @returns true, wenn die Fehlstundeneingabe erlaubt ist, und ansonsten false
	 */
	public istFehlstundeneingabeErlaubt(idKlasse: number, istGesamt: boolean, berechtigungen: Set<ENMLehrerFunktion>): boolean {
		const istKlassenweise = this.istFehlstundeneingabeKlassenweise(idKlasse);
		if (istKlassenweise !== istGesamt) {
			return false;
		}
		// Fehlstunden bei der Lerngruppe gehören zu den Leistungsdaten und hängen daher von der Freigabe für die Klassenleitung ab
		const rollen = istKlassenweise ? berechtigungen : this.getBerechtigungenLeistungen(idKlasse, berechtigungen);
		if (!this.istEingabeErlaubt(idKlasse, rollen)) {
			return false;
		}
		const config = this.mapSperrungenSpalten.getOrNull(idKlasse, "Fehlstunden");
		if (config === null) {
			return false; // Sollte nicht vorkommen - in dem Fall wäre die Konfiguration fehlerhaft
		}
		if (config.gesperrt) {
			return false;
		}
		return (rollen.size > 0);
	}

	/**
	 * Gibt zurück, ob die Eingabe für die Klasse in der Konfiguration für eine Teilleistungsart freigeschaltet ist.
	 *
	 * @param idKlasse         die ID der Klasse
	 * @param idArt            die ID der Teilleistungsart
	 * @param berechtigungen   die Lehrer-Funktionen für vorhandenen Berechtigung für die Anfrage
	 *
	 * @returns true, wenn die Eingabe erlaubt ist, und ansonsten false
	 */
	public istTeilleistungseingabeErlaubt(idKlasse: number, idArt: number, berechtigungen: Set<ENMLehrerFunktion>): boolean {
		if (!this.istSpalteneingabeErlaubt(idKlasse, "Teilnoten", berechtigungen)) {
			return false;
		}
		const config = this.mapSperrungenSpaltenTeilleistung.getOrNull(idKlasse, idArt);
		if (config === null) {
			return false; // Sollte nicht vorkommen - in dem Fall wäre die Konfiguration fehlerhaft
		}
		return !config.gesperrt;
	}


}
