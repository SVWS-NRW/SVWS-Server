import { computed, shallowRef, triggerRef } from "vue";

import { ENMConfigSpalte } from "@core/core/data/enm/ENMConfigSpalte";
import type { ENMv2Teilleistungsart } from "@core/core/data/enm/v2/ENMv2Teilleistungsart";
import { ArrayList } from "@core/java/util/ArrayList";
import type { Collection } from "@core/java/util/Collection";
import { HashMap } from "@core/java/util/HashMap";
import type { JavaMap } from "@core/java/util/JavaMap";
import type { List } from "@core/java/util/List";

/**
 * Ein Manager zur Verwaltung der Konfiguration von sichtbaren Spalten im Notenmodul.
 */
export class NotenmodulConfigManagerSichtbareSpalten {

	/** Eine Map für den Zugriff auf die Teilleistungsarten */
	private readonly mapTeilleistungsarten: JavaMap<number, ENMv2Teilleistungsart>;

	/** Ein Array mit den Namen aller ausblendbaren Spalten in den Ansichten des Notenmoduls */
	private readonly spaltenAusblendbar = ["Kurs", "Kursart", "Lehrer", "Teilnoten", "Quartal", "Note", "Mahnung", "Fehlstunden", "FB", "ASV", "AUE", "ZB"];

	/** Ein Array mit den allgemeinen Spalten */
	public readonly spaltenAllgemein = ["Kurs", "Kursart", "Lehrer"];

	/** Ein Array mit den Bemerkungs-Spalten */
	public readonly spaltenBemerkungen = ["FB", "ASV", "AUE", "ZB"];

	/** Ein Array mit den Spalten für Leistungsdaten */
	public readonly spaltenLeistungsdaten = ["Quartal", "Note", "Mahnung", "Fehlstunden"];

	/** Die Teilnoten-Spalte */
	public readonly spalteTeilleistungen = "Teilnoten";

	/** Die Map für den Zugriff auf die einzelnen Spalten der Konfiguration. Der Key kann entweder der Name einer ausblendbaren Spalte sein
	 *  oder die ID im Falle von Teilleistungen auch die ID einer einzelnen Teilleistung, die ausgeblendet werden soll. */
	private readonly mapConfigSpalte = shallowRef(new HashMap<number | string, ENMConfigSpalte>());

	/** Die Map für den Zugriff auf die Konfiguration anhand des Spaltennamens - auch bei Teilleistungen */
	private readonly mapConfigSpalteByName = shallowRef(new HashMap<string, ENMConfigSpalte>());

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
	constructor(list: List<ENMConfigSpalte>, mapTeilleistungsarten: JavaMap<number, ENMv2Teilleistungsart>, writeConfig: () => Promise<void>) {
		this.mapTeilleistungsarten = mapTeilleistungsarten;
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
				this.mapConfigSpalte.value.put(spalte.name, spalte);
			} else {
				const art = this.mapTeilleistungsarten.get(spalte.idTeilleistung);
				if (art !== null) {
					spalte.name = art.bezeichnung ?? '—';
					this.mapConfigSpalte.value.put(art.id, spalte);
					this.mapConfigSpalteByName.value.put(spalte.name, spalte);
				}
			}
		}
		for (const art of this.mapTeilleistungsarten.values()) {
			if (!this.mapConfigSpalte.value.containsKey(art.id)) {
				const spalte = new ENMConfigSpalte();
				spalte.name = art.bezeichnung ?? '—';
				spalte.idTeilleistung = art.id;
				this.mapConfigSpalte.value.put(art.id, spalte);
				this.mapConfigSpalteByName.value.put(spalte.name, spalte);
			}
		}
		for (const art of this.spaltenAusblendbar) {
			if (!this.mapConfigSpalte.value.containsKey(art)) {
				const spalte = new ENMConfigSpalte();
				spalte.name = art;
				this.mapConfigSpalte.value.put(art, spalte);
				this.mapConfigSpalteByName.value.put(spalte.name, spalte);
			}
		}
		triggerRef(this.mapConfigSpalteByName);
		triggerRef(this.mapConfigSpalte);
	}

	/**
	 * Wechselt die Sichtbarkeit für die übergebene Spalte. Anschließend wird die Callback-Methode zum
	 * Speichern der Konfiguration aufgerufen.
	 *
	 * @param spalte   der Spaltenname
	 */
	public async toggle(spaltenname: string) {
		const spalte = this.mapConfigSpalteByName.value.get(spaltenname);
		if (spalte === null) {
			return;
		}
		spalte.anzeigen = !spalte.anzeigen;
		triggerRef(this.mapConfigSpalteByName);
		await this.writeConfig();
	}

	/**
	 * Wechselt die Sichtbarkeit der Teilleistungen
	 * @param colname		Der Name der Spalte
	 */
	public async toggleTeilleistung(colname: string) {
		await this.toggle(colname);
	}

	/**
	 * Prüft, ob nur eine Teilmenge der Spalten sichtbar ist
	 * @returns true, wenn nur teilweise sichtbar, um den intermediate State zu erzeugen
	 */
	mengeSichtbareTeilleistungen = computed(() => {
		let anzahlSichtbar = 0;
		for (const col of this.spaltenTeilleistungen.value) {
			if (this.istSichtbar(col)) {
				anzahlSichtbar++;
			}
		}
		return anzahlSichtbar;
	});

	/**
	 * Wechselt die Sichtbarkeit für alle Teilleistungsspalten. Anschließend wird die Callback-Methode
	 * zum Speichern der Konfiguration aufgerufen.
	 */
	public async toggleAlleTeilleistungen() {
		const sichtbar = this.mengeSichtbareTeilleistungen.value !== 0;
		for (const col of this.mapConfigSpalte.value.values()) {
			if ((col.idTeilleistung !== null) && (col.anzeigen === sichtbar)) {
				await this.toggleTeilleistung(col.name);
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
		return (this.mapConfigSpalteByName.value.get(spalte)?.anzeigen === true);
	}

	/** Liefert die Spaltenkonfigurationen */
	get spalten(): Collection<ENMConfigSpalte> {
		return this.mapConfigSpalte.value.values();
	}

	/** Liefert die Spaltenkonfigurationen für die Teilleistungen */
	spaltenTeilleistungen = computed(() => {
		const list = new ArrayList<string>();
		for (const col of this.mapConfigSpalte.value.values()) {
			if (col.idTeilleistung !== null) {
				list.add(col.name);
			}
		}
		list.sort({ compare: (o1, o2) => {
			const s1 = this.mapTeilleistungsarten.get(o1)?.sortierung ?? 32000;
			const s2 = this.mapTeilleistungsarten.get(o2)?.sortierung ?? 32000;
			return s1 < s2 ? -1 : 1;
		} });
		return list;
	});

	/** Liefert die Spaltenkonfiguration als JSON-String */
	get json(): string {
		const arr = new Array<string>();
		for (const k of this.spalten) {
			arr.push(ENMConfigSpalte.transpilerToJSON(k));
		}
		return "[" + arr.join() + "]";
	}

}
