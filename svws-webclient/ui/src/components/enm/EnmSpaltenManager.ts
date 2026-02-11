import { ENMConfigSpalte } from "../../../../core/src/core/data/enm/ENMConfigSpalte";
import { ArrayList } from "../../../../core/src/java/util/ArrayList";
import { HashMap } from "../../../../core/src/java/util/HashMap";
import type { JavaMap } from "../../../../core/src/java/util/JavaMap";
import type { List } from "../../../../core/src/java/util/List";

/**
 * Ein Manager für die Verwaltung der Sichtbar-Konfiguration bei Tabellen der ENM-Daten.
 */
export class EnmSpaltenManager {

	/** Eine Map für den schnellen Zugriff auf die Konfiguration für eine Spalte und der ID, also name oder ID einer Spalte */
	private readonly _mapSpalten: JavaMap<string, ENMConfigSpalte>;

	/** Ein Set mit den Namen der Spalten für die Übersicht der Leistungen */
	private readonly _setLeistungen = new Set(["Klasse", "Name", "Fach", "Kurs", "Kursart", "Lehrer", "Quartal", "Note", "Mahnung", "FS", "FSU", "Bemerkung"]);

	/** Ein Set mit den Namen der Spalten für die Übersicht der Teilleistungen */
	private readonly _setTeilleistungen = new Set(["Klasse", "Name", "Fach", "Kurs", "Kursart", "Lehrer", "Teilleistung", "Quartal", "Note"]);

	/** Ein Set mit den Namen der Spalten für die Übersicht der Teilleistungen */
	private readonly _setKlassenleitung = new Set(["Klasse", "Name", "FS", "FSU", "ASV", "AUE", "ZB"]);


	/**
	 * Erstellt einen neuen Manager für die Nutzung der Konfiguration von Sperrungen bei
	 * der Noteneingabe bei Klassen.
	 *
	 * @param json   der JSON-String mit der Konfiguration
	 */
	public constructor(json: string) {
		const spalten = this.mapJsonToListOfObjects(json, o => ENMConfigSpalte.transpilerFromJSON(o));
		this._mapSpalten = new HashMap<string, ENMConfigSpalte>();
		for (const spalte of spalten) {
			this._mapSpalten.put(spalte.name, spalte);
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

	get mapSpaltenLeistungen(): Map<string, boolean> {
		const map = new Map<string, boolean>();
		for (const spalte of this._mapSpalten.values()) {
			if (this._setLeistungen.has(spalte.name)) {
				map.set(spalte.name, spalte.anzeigen);
			}
		}
		return map;
	}

	get mapSpaltenTeilleistungen(): Map<string, boolean> {
		const map = new Map<string, boolean>();
		for (const spalte of this._mapSpalten.values()) {
			if (this._setTeilleistungen.has(spalte.name) || (spalte.idTeilleistung !== null)) {
				map.set(spalte.name, spalte.anzeigen);
			}
		}
		return map;
	}

	get mapSpaltenKlassenleitung(): Map<string, boolean> {
		const map = new Map<string, boolean>();
		for (const spalte of this._mapSpalten.values()) {
			if (this._setKlassenleitung.has(spalte.name)) {
				map.set(spalte.name, spalte.anzeigen);
			}
		}
		return map;
	}

}