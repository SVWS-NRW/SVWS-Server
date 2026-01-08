import { ArrayList } from "@core";
import { ENMConfigSpalte } from "../../../../../core/src/core/data/enm/ENMConfigSpalte";
import type { ENMTeilleistungsart } from "../../../../../core/src/core/data/enm/ENMTeilleistungsart";
import type { Collection } from "../../../../../core/src/java/util/Collection";
import { HashMap } from "../../../../../core/src/java/util/HashMap";
import type { JavaMap } from "../../../../../core/src/java/util/JavaMap";
import type { List } from "../../../../../core/src/java/util/List";

/**
 * Ein Manager zur Verwaltung der Konfiguration von sichtbaren Spalten im Notenmodul.
 */
export class NotenmodulConfigManagerSichtbareSpalten {

	/** Eine Map für den Zugriff auf die Teilleistungsarten */
	private readonly mapTeilleistungsarten: JavaMap<number, ENMTeilleistungsart>;

	/** Ein Array mit den Namen aller ausblendbaren Spalten in den Ansichten des Notenmoduls */
	private readonly spaltenAusblendbar = ["Kurs", "Kursart", "Lehrer", "Teilnoten", "Quartalsnoten", "Note", "Mahnung", "Fehlstunden", "FB", "ASV", "AUE", "ZB"];

	/** Ein Array mit den allgemeinen Spalten */
	public readonly spaltenAllgemein = ["Kurs", "Kursart", "Lehrer"];

	/** Ein Array mit den Bemerkungs-Spalten */
	public readonly spaltenBemerkungen = ["FB", "ASV", "AUE", "ZB"];

	/** Ein Array mit den Spalten für Leistungsdaten */
	public readonly spaltenLeistungsdaten = ["Quartalsnoten", "Note", "Mahnung", "Fehlstunden"];

	/** Die Teilnoten-Spalte */
	public readonly spalteTeilleistungen = "Teilnoten";

	/** Die Map für den Zugriff auf die einzelnen Spalten der Konfiguration. Der Key kann entweder der Name einer ausblendbaren Spalte sein
	 *  oder die ID im Falle von Teilleistungen auch die ID einer einzelnen Teilleistung, die ausgeblendet werden soll. */
	private readonly mapConfigSpalte: JavaMap<number | string, ENMConfigSpalte>;

	/** Die Map für den Zugriff auf die Konfiguration anhand des Spaltennamens - auch bei Teilleistungen */
	private readonly mapConfigSpalteByName: JavaMap<string, ENMConfigSpalte>;

	/** Eine Callback-Methode, die bei einem Update der Spaltenkonfiguration aufgerufen wird. */
	private readonly writeConfig: () => Promise<void>;

	/**
	 * Erstellt einen neuen Manager, welcher mit der Liste der bisherigen Konfiguration und einer Map mit den
	 * aktuell definierten Teilleistungsarten, eine aktuelle Gesamtkonfiguration für das Notenmodul erstellt.
	 *
	 * @param list                    die Liste mit der bisher gespeicherten Konfiguration
	 * @param mapTeilleistungsarten   die Map mit den Teilleistungsarten
	 * @param writeConfig             eine Callback-Methode zur Reaktion, wenn die Konfiguration angepasst wurde
	 */
	constructor(list: List<ENMConfigSpalte>, mapTeilleistungsarten: JavaMap<number, ENMTeilleistungsart>, writeConfig: () => Promise<void>) {
		this.mapTeilleistungsarten = mapTeilleistungsarten;
		this.mapConfigSpalte = new HashMap<number | string, ENMConfigSpalte>();
		this.mapConfigSpalteByName = new HashMap<string, ENMConfigSpalte>();
		this.importConfig(list);
		this.writeConfig = writeConfig;
	}

	/**
	 * Hilfsmethode zum Import der bisherigen Konfiguration. Beim Import wird auch geprüft, ob sich durch
	 * zwischenzeitliche Änderungen an Teilleistungsarten eine angepasste Konfiguration ergeben hat. Ist dies
	 * der Fall werden neue Teilleistungsarten automatisch ergänzt und nicht mehr vorhandene entfernt.
	 *
	 * @param list   die Liste mit der bisherigen Konfiguration
	 */
	private importConfig(list: List<ENMConfigSpalte>): void {
		for (const spalte of list) {
			if (spalte.idTeilleistung === null) {
				this.mapConfigSpalte.put(spalte.name, spalte);
				this.mapConfigSpalteByName.put(spalte.name, spalte);
			} else {
				const art = this.mapTeilleistungsarten.get(spalte.idTeilleistung);
				if (art !== null) {
					spalte.name = art.bezeichnung ?? '—';
					this.mapConfigSpalte.put(art.id, spalte);
					this.mapConfigSpalteByName.put(spalte.name, spalte);
				}
			}
		}
		for (const art of this.mapTeilleistungsarten.values()) {
			if (!this.mapConfigSpalte.containsKey(art.id)) {
				const spalte = new ENMConfigSpalte();
				spalte.name = art.bezeichnung ?? '—';
				spalte.idTeilleistung = art.id;
				this.mapConfigSpalte.put(art.id, spalte);
				this.mapConfigSpalteByName.put(spalte.name, spalte);
			}
		}
		for (const art of this.spaltenAusblendbar) {
			if (!this.mapConfigSpalte.containsKey(art)) {
				const spalte = new ENMConfigSpalte();
				spalte.name = art;
				this.mapConfigSpalte.put(art, spalte);
				this.mapConfigSpalteByName.put(spalte.name, spalte);
			}
		}
	}

	/**
	 * Wechselt die Sichtbarkeit für die übergebene Spalte. Anschließend wird die Callback-Methode zum
	 * Speichern der Konfiguration aufgerufen.
	 *
	 * @param spalte   die geänderte Spaltenkonfiguration
	 */
	public async toggle(spaltenname: string) {
		const spalte = this.mapConfigSpalteByName.get(spaltenname);
		if (spalte === null) {
			return;
		}
		spalte.anzeigen = !spalte.anzeigen;
		await this.writeConfig();
	}

	/**
	 * Wechselt die Sichtbarkeit der Teilleistungen
	 * @param colname		Der Name der Spalte
	 */
	public async toggleTeilleistung(colname: string) {
		const istSichtbar = this.istSichtbar(this.spalteTeilleistungen);
		await this.toggle(colname);
		const teilleistungIstSichtbar = this.istSichtbar(colname);
		if (!istSichtbar && teilleistungIstSichtbar) {
			await this.toggle(this.spalteTeilleistungen);
		}
	}

	/**
	 * Prüft, ob nur eine Teilmende der Spalten sichtbar ist
	 * @returns true, wenn nur teilweise sichtbar, um den intermediate State zu erzeugen
	 */
	public hatNurMancheTeilleistungen(): boolean {
		const istSichtbar = this.istSichtbar(this.spalteTeilleistungen);
		for (const col of this.spaltenTeilleistungen) {
			if (this.istSichtbar(col) !== istSichtbar) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Wechselt die Sichtbarkeit für alle Teilleistungsspalten. Anschließend wird die Callback-Methode
	 * zum Speichern der Konfiguration aufgerufen.
	 */
	public async toggleAlleTeilleistungen() {
		const spalte = this.mapConfigSpalteByName.get("Teilnoten");
		if (spalte === null) {
			return;
		}
		spalte.anzeigen = !spalte.anzeigen;
		for (const col of this.mapConfigSpalte.values()) {
			if (col.idTeilleistung !== null) {
				col.anzeigen = spalte.anzeigen;
			}
		}
		await this.writeConfig();
	}

	/**
	 * Gibt zurück, ob die Spalte mit dem übergebenen Namen sichtbar ist oder nicht
	 *
	 * @param spalte   der Name der Spalte
	 *
	 * @returns true, wenn die Spalte sichtbar ist, und ansonsten false
	 */
	public istSichtbar(spalte: string): boolean {
		return (this.mapConfigSpalteByName.get(spalte)?.anzeigen === true);
	}

	/** Liefert die Spaltenkonfigurationen */
	get spalten(): Collection<ENMConfigSpalte> {
		return this.mapConfigSpalte.values();
	}

	/** Liefert die Spaltenkonfigurationen für die Teilleistungen */
	get spaltenTeilleistungen(): List<string> {
		const list = new ArrayList<string>();
		for (const col of this.mapConfigSpalte.values()) {
			if (col.idTeilleistung !== null) {
				list.add(col.name);
			}
		}
		return list;
	}

	/** Liefert die Spaltenkonfiguration als JSON-String */
	get json(): string {
		const arr = new Array<string>();
		for (const k of this.spalten) {
			arr.push(ENMConfigSpalte.transpilerToJSON(k));
		}
		return "[" + arr.join() + "]";
	}

}
