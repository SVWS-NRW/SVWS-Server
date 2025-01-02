import { computed, shallowRef, triggerRef } from "vue";
import type { ShallowRef } from "vue";
import type { Comparator, ENMFach, ENMFloskelgruppe, ENMFoerderschwerpunkt, ENMJahrgang, ENMKlasse, ENMLehrer, ENMLeistung, ENMLerngruppe, ENMSchueler, ENMTeilleistungsart, JavaMap, List} from "@core";
import { ArrayList, DeveloperNotificationException, HashMap, HashSet, type ENMDaten } from "@core";

/**
 * Das Interface für die Einträge der Auswahlliste für die Lerngruppen
 */
export interface EnmLerngruppenAuswahlEintrag {

	/** Die ID der Lerngruppen */
	id: number;

	/** Die Bezeichnung der Lerngruppe */
	bezeichnung: string;

	/** Die Klassen, welche bei der Lerngruppen vorhanden sind */
	klassen: string;

}


/**
 * Ein Manager für die Verwaltung den ENM-Daten.
 */
export class EnmManager {

	/** Eine Referenz auf die ENM-Daten */
	protected daten: ShallowRef<ENMDaten>;

	/** Eine Referenz auf die ID des Lehrers, für welchen die ENM-Daten in diesem Manager verwaltet werden */
	protected idLehrer: ShallowRef<number>;

	/** Eine Referenz auf die aktuelle Auswahl von Lerngruppen des Lehrers, auf welche in diesem Manager gefiltert wird */
	protected _filterLerngruppen: ShallowRef<Array<EnmLerngruppenAuswahlEintrag>>;

	/** Eine Refernz auf die aktuelle Auswahl der Leistung zur Bearbeitung in diesem Manager */
	protected _auswahlLeistung: ShallowRef<ENMLeistung | null>;


	/**
	 * Erstellt einen neue Enm-Manager für die übergebenen ENM-Daten
	 *
	 * @param daten      die ENM-Daten
	 * @param idLehrer   die ID des Lehrers, für welchen die ENM-Daten verwaltet werden
	 */
	public constructor(daten: ENMDaten, idLehrer: number) {
		this.daten = shallowRef<ENMDaten>(daten);
		this.idLehrer = shallowRef<number>(idLehrer);
		this._filterLerngruppen = shallowRef<Array<EnmLerngruppenAuswahlEintrag>>(new Array<EnmLerngruppenAuswahlEintrag>());
		this._auswahlLeistung = shallowRef<ENMLeistung | null>(null);
	}

	/**
	 * Aktualisiert die Daten dieses Manager, in dem die Reaktivität des Daten-Attributs getriggert wird.
	 */
	public update(): void {
		triggerRef(this.daten);
	}

	/** Gibt die aktuelle Auswahl der Lerngruppen zurück. */
	public get filterLerngruppen() : Array<EnmLerngruppenAuswahlEintrag> {
		return this._filterLerngruppen.value;
	}

	/** Setzt die aktuelle Auswahl der Lerngruppen. */
	public set filterLerngruppen(value : Array<EnmLerngruppenAuswahlEintrag>) {
		this._filterLerngruppen.value = value;
	}

	/** Gibt die aktuell ausgewählte Leistung zurück */
	public get auswahlLeistung() : ENMLeistung | null {
		return this._auswahlLeistung.value;
	}

	/** Setzt die aktuell ausgewählte Leistung */
	public set auswahlLeistung(value: ENMLeistung | null) {
		this._auswahlLeistung.value = value;
	}

	/** Eine Map von der ID der Förderschwerpunkte auf deren Objekte */
	protected mapFoerderschwerpunkte = computed<JavaMap<number, ENMFoerderschwerpunkt>>(() => {
		const result = new HashMap<number, ENMFoerderschwerpunkt>();
		for (const f of this.daten.value.foerderschwerpunkte)
			result.put(f.id, f);
		return result;
	});

	/** Eine Map von der ID der Jahrgänge auf deren Objekte */
	protected mapJahrgaenge = computed<JavaMap<number, ENMJahrgang>>(() => {
		const result = new HashMap<number, ENMJahrgang>();
		for (const j of this.daten.value.jahrgaenge)
			result.put(j.id, j);
		return result;
	});

	/** Eine Map von der ID der Klassen auf deren Objekte */
	protected mapKlassen = computed<JavaMap<number, ENMKlasse>>(() => {
		const result = new HashMap<number, ENMKlasse>();
		for (const k of this.daten.value.klassen)
			result.put(k.id, k);
		return result;
	});

	/** Eine Map von der ID der Floskelgruppen auf deren Objekte */
	protected mapFloskelgruppen = computed<JavaMap<string, ENMFloskelgruppe>>(() => {
		const result = new HashMap<string, ENMFloskelgruppe>();
		for (const f of this.daten.value.floskelgruppen)
			result.put(f.kuerzel, f);
		return result;
	});

	/** Eine Map von der ID der Lehrer auf deren Objekte */
	protected mapLehrer = computed<JavaMap<number, ENMLehrer>>(() => {
		const result = new HashMap<number, ENMLehrer>();
		for (const l of this.daten.value.lehrer)
			result.put(l.id, l);
		return result;
	});

	/** Eine Map von der ID der Fächer auf deren Objekte */
	protected mapFaecher = computed<JavaMap<number, ENMFach>>(() => {
		const result = new HashMap<number, ENMFach>();
		for (const f of this.daten.value.faecher)
			result.put(f.id, f);
		return result;
	});

	/** Eine Map von der ID der Teilleistungsarten auf deren Objekte */
	protected mapTeilleistungsarten = computed<JavaMap<number, ENMTeilleistungsart>>(() => {
		const result = new HashMap<number, ENMTeilleistungsart>();
		for (const a of this.daten.value.teilleistungsarten)
			result.put(a.id, a);
		return result;
	});

	/** Eine Map von der ID der Lerngruppen auf deren Objekte */
	protected mapLerngruppen = computed<JavaMap<number, ENMLerngruppe>>(() => {
		const result = new HashMap<number, ENMLerngruppe>();
		for (const l of this.daten.value.lerngruppen)
			result.put(l.id, l);
		return result;
	});

	/** Eine Map von der ID der Schüler auf deren Objekte */
	protected mapSchueler = computed<JavaMap<number, ENMSchueler>>(() => {
		const result = new HashMap<number, ENMSchueler>();
		for (const s of this.daten.value.schueler)
			result.put(s.id, s);
		return result;
	});

	/** Eine Map, welcher Lerngruppen-ID die Menge der zugehörigen Schüler-Objekte zuordnet */
	protected mapLerngruppenSchueler = computed<JavaMap<number, List<ENMSchueler>>>(() => {
		const result = new HashMap<number, List<ENMSchueler>>();
		// Erzeuge zunächst Listen für alle IDs der Lerngruppen
		for (const lerngruppe of this.daten.value.lerngruppen)
			result.put(lerngruppe.id, new ArrayList<ENMSchueler>());
		// Gehe alle Leistungsdaten der Schüler durch, um die Lerngruppen-Zuordnung zu bestimmen
		for (const schueler of this.daten.value.schueler) {
			for (const leistung of schueler.leistungsdaten) {
				const idLerngruppe = leistung.lerngruppenID;
				const list = result.get(idLerngruppe);
				if (list === null)
					throw new DeveloperNotificationException("Die Lerngruppe mit der ID " + idLerngruppe + " wird in Leistungsdaten angegeben, ist aber im Katalog der Lerngruppen nicht vorhanden.");
				list.add(schueler);
			}
		}
		return result;
	});

	/** Eine Map, welche einer Lerngruppen-ID die Menge der zugeordneten Jahrgänge zuordnet */
	protected mapLerngruppeJahrgaenge = computed<JavaMap<number, List<ENMJahrgang>>>(() => {
		const result = new HashMap<number, List<ENMJahrgang>>();
		for (const lerngruppe of this.daten.value.lerngruppen) {
			const list = new ArrayList<ENMJahrgang>();
			// Klassen- oder Kursunterricht?
			if (lerngruppe.kursartID === null) {
				// Klassenunterricht - Bestimme den Jahrgang der Klasse
				const klasse = this.mapKlassen.value.get(lerngruppe.kID);
				if (klasse !== null) {
					const jahrgang = this.mapJahrgaenge.value.get(klasse.idJahrgang);
					if (jahrgang !== null)
						list.add(jahrgang);
				}
			} else {
				// Kursunterricht - Bestimme die Jahrgänge aller Schüler
				const listSchueler = this.mapLerngruppenSchueler.value.get(lerngruppe.id);
				const setJahrgaenge = new HashSet<number>();
				if (listSchueler !== null)
					for (const schueler of listSchueler)
						setJahrgaenge.add(schueler.jahrgangID);
				for (const idJahrgang of setJahrgaenge) {
					const jahrgang = this.mapJahrgaenge.value.get(idJahrgang);
					if (jahrgang !== null)
						list.add(jahrgang);
				}
			}
			result.put(lerngruppe.id, list);
		}
		return result;
	});

	/** Eine Map, welche einer Lerngruppen-ID die Menge der zugeordneten Klassen zuordnet */
	protected mapLerngruppeKlassen = computed<JavaMap<number, List<ENMKlasse>>>(() => {
		const result = new HashMap<number, List<ENMKlasse>>();
		for (const lerngruppe of this.daten.value.lerngruppen) {
			const list = new ArrayList<ENMKlasse>();
			const schueler = this.mapLerngruppenSchueler.value.get(lerngruppe.id);
			const tmpKlassenIDs = new HashSet<number>();
			if (schueler !== null)
				for (const s of schueler)
					tmpKlassenIDs.add(s.klasseID);
			for (const idKlasse of tmpKlassenIDs) {
				const klasse = this.mapKlassen.value.get(idKlasse);
				if (klasse === null)
					continue;
				list.add(klasse);
			}
			list.sort(this.comparatorKlassen);
			result.put(lerngruppe.id, list);
		}
		return result;
	});

	/** Die Menge aller Lerngruppen des Lehrers, sortiert nach den Jahrgängen */
	protected listLerngruppenLehrer = computed<List<ENMLerngruppe>>(() => {
		const result = new ArrayList<ENMLerngruppe>();
		for (const l of this.daten.value.lerngruppen)
			if (l.lehrerID.contains(this.idLehrer.value))
				result.add(l);
		result.sort(this.comparatorLerngruppen);
		return result;
	});

	/** Die Auswahlliste für die Lerngruppen */
	protected listLerngruppenAuswahlliste = computed<List<EnmLerngruppenAuswahlEintrag>>(() => {
		const result = new ArrayList<EnmLerngruppenAuswahlEintrag>();
		for (const l of this.listLerngruppenLehrer.value)
			result.add(<EnmLerngruppenAuswahlEintrag>{
				id: l.id,
				bezeichnung: this.lerngruppeGetBezeichnung(l.id),
				klassen: this.lerngruppeGetKlassenAsString(l.id),
			});
		return result;
	});

	/** Die aktuelle Auswahl der Lerngruppen */
	protected listLerngruppenAuswahl = computed<List<ENMLerngruppe>>(() => {
		const lerngruppen = (this.filterLerngruppen.length === 0) ? this.listLerngruppenAuswahlliste.value : this.filterLerngruppen;
		const result = new ArrayList<ENMLerngruppe>();
		for (const l of lerngruppen) {
			const lerngruppe = this.mapLerngruppen.value.get(l.id);
			if (lerngruppe === null)
				continue;
			result.add(lerngruppe);
		}
		return result;
	});

	/** Die Menge der Lerngruppen-IDs, die zu der Auswahl der aktuellen Lerngruppen gehört */
	protected setLerngruppenAuswahlIDs = computed<HashSet<number>>(() => {
		const result = new HashSet<number>();
		for (const lerngruppe of this.listLerngruppenAuswahl.value)
			result.add(lerngruppe.id);
		return result;
	});

	/** Die Liste der Schüler, die durch die aktuelle Lerngruppenauswahl ausgewählt sind */
	protected listLerngruppenAuswahlSchueler = computed<List<ENMSchueler>>(() => {
		const idsSchueler = new HashSet<number>();
		for (const lerngruppe of this.listLerngruppenAuswahl.value) {
			const listLerngruppenSchueler = this.mapLerngruppenSchueler.value.get(lerngruppe.id);
			if (listLerngruppenSchueler === null)
				continue;
			for (const schueler of listLerngruppenSchueler)
				idsSchueler.add(schueler.id);
		}
		const result = new ArrayList<ENMSchueler>();
		for (const idSchueler of idsSchueler) {
			const schueler = this.mapSchueler.value.get(idSchueler);
			if (schueler === null)
				continue;
			result.add(schueler);
		}
		return result;
	});

	/** Eine Hashmap, welche den Schülern der aktuellen Lerngruppenauswahl die ausgewählten Leistungen zuordnet */
	protected mapLerngruppenAuswahlSchuelerLeistungen = computed<HashMap<number, List<ENMLeistung>>>(() => {
		const result = new HashMap<number, List<ENMLeistung>>();
		for (const schueler of this.listLerngruppenAuswahlSchueler.value) {
			const listLeistungen = new ArrayList<ENMLeistung>();
			for (const leistung of schueler.leistungsdaten)
				if (this.setLerngruppenAuswahlIDs.value.contains(leistung.lerngruppenID))
					listLeistungen.add(leistung);
			// TODO Sortiere die Liste anhand der Sortierung ihrer Lerngruppe
			result.put(schueler.id, listLeistungen);
		}
		return result;
	});

	/**
	 * Vergleicht zwei Lerngruppen miteinander und sortiert diese anhand der Jahrgänge als erstes Kriterium und
	 * anhand der Fachsortierung als zweites Kriterium.
	 *
	 * @param a   die erste Lerngruppe
	 * @param b   die zweite Lerngruppe
	 *
	 * @returns der Wert für den Vergleich (< 0, 0 oder >0)
	 */
	protected compareLerngruppen = (a : ENMLerngruppe, b : ENMLerngruppe) : number => {
		// Vergleiche zuerst anhand der Jahrgänge, sofern diese angegeben sind ...
		const aJgs = this.mapLerngruppeJahrgaenge.value.get(a.id);
		const bJgs = this.mapLerngruppeJahrgaenge.value.get(b.id);
		if (!(((aJgs === null) || (aJgs.size() !== 1)) && ((bJgs === null) || (bJgs.size() !== 1)))) {
			if ((aJgs === null) || (aJgs.size() !== 1))
				return -1;
			if ((bJgs === null) || (bJgs.size() !== 1))
				return 1;
			const aJg = aJgs.get(0);
			const bJg = bJgs.get(0);
			const tmp = aJg.sortierung - bJg.sortierung;
			if (tmp !== 0)
				return tmp;
		}
		// ... vergleiche dann bei Gleichheit dann anhand der Fach-Sortierung
		const aFach = this.mapFaecher.value.get(a.fachID);
		const bFach = this.mapFaecher.value.get(b.fachID);
		if (!((aFach === null) && (bFach === null))) {
			if (aFach === null)
				return -1;
			if (bFach === null)
				return 1;
			const tmp = aFach.sortierung - bFach.sortierung;
			if (tmp !== 0)
				return tmp;
		}
		// ... und ansonsten anhand der ID der Lerngruppe
		return a.id - b.id;
	}

	/** Definition des Comparators für zwei Lerngruppen */
	public comparatorLerngruppen = <Comparator<ENMLerngruppe>>{ compare: this.compareLerngruppen };

	/**
	 * Vergleicht zwei Klassen miteinander und sortiert diese.
	 *
	 * @param a   die erste Klasse
	 * @param b   die zweite Klasse
	 *
	 * @returns der Wert für den Vergleich (< 0, 0 oder >0)
	 */
	protected compareKlassen = (a : ENMKlasse, b : ENMKlasse) : number => {
		// Vergleiche zuerst anhand der gesetzten Sortierung der Klasse...
		const tmp = a.sortierung - b.sortierung;
		if (tmp !== 0)
			return tmp;
		// ... und ansonsten anhand des Anzeige-Kürzels der Klassen
		if ((a.kuerzelAnzeige !== null) && (b.kuerzelAnzeige !== null))
			return a.kuerzelAnzeige.localeCompare(b.kuerzelAnzeige);
		if (a.kuerzelAnzeige === null)
			return -1;
		if (b.kuerzelAnzeige === null)
			return 1;
		return 0;
	}

	/** Definition des Comparators für zwei Klassen */
	public comparatorKlassen = <Comparator<ENMKlasse>>{ compare: this.compareKlassen };


	/** Gibt die Auswahlliste für die Lerngruppen zurück. */
	public get lerngruppenAuswahlliste() : List<EnmLerngruppenAuswahlEintrag> {
		return this.listLerngruppenAuswahlliste.value;
	}

	/** Gibt die Lerngruppen zurück, welche dem Lehrer zugeordnet sind. */
	public get lerngruppenOfLehrer() : List<ENMLerngruppe> {
		return this.listLerngruppenLehrer.value;
	}

	/**
	 * Bestimmt die Lerngruppe anhand der übergebenen ID
	 *
	 * @param id   die ID der Lerngruppe
	 *
	 * @returns die Lerngruppe oder null
	 */
	public lerngruppeByIDOrNull(id: number) : ENMLerngruppe | null {
		return this.mapLerngruppen.value.get(id);
	}

	/**
	 * Bestimmt die Lerngruppe anhand der übergebenen ID
	 *
	 * @param id   die ID der Lerngruppe
	 *
	 * @returns die Lerngruppe
	 * @throws DeveloperNotificationException wenn die Lerngruppe nicht in den ENM-Daten existiert
	 */
	public lerngruppeByIDOrException(id: number) : ENMLerngruppe {
		const lerngruppe = this.mapLerngruppen.value.get(id);
		if (lerngruppe === null)
			throw new DeveloperNotificationException("Fehler bei der Bestimmung der Bezeichnung der Lerngruppe.");
		return lerngruppe;
	}

	/**
	 * Gibt eine ausführliche Bezeichnung für die Lerngruppe mit der übergebenen ID zurück.
	 *
	 * @param id   die ID der Lerngruppe
	 *
	 * @returns die Bezeichnung der Lerngruppe
	 * @throws DeveloperNotificationException wenn die Lerngruppe nicht in den ENM-Daten existiert oder die Bezeichnung leer ist
	 */
	public lerngruppeGetBezeichnung(id: number) : string {
		const lerngruppe = this.lerngruppeByIDOrException(id);
		if (lerngruppe.bezeichnung === null)
			throw new DeveloperNotificationException("Fehler bei der Bestimmung der Bezeichnung der Lerngruppe, die Bezeichnung in den ENM-Daten ist nicht gesetzt.");
		return lerngruppe.bezeichnung;
	}

	/**
	 * Gibt eine Liste der Klasse der Lerngruppen als String zurück.
	 *
	 * @param id   die ID der Lerngruppe
	 *
	 * @returns die Bezeichnung der Lerngruppe
	 */
	public lerngruppeGetKlassenAsString(id: number) : string {
		const klassen = this.mapLerngruppeKlassen.value.get(id);
		if (klassen === null)
			throw new DeveloperNotificationException("Fehler bei der Bestimmung der zugeordneten Klassen für die Lerngruppe.");
		if (klassen.isEmpty())
			return "—";
		return [...klassen].map(k => k.kuerzelAnzeige).join(",");
	}

	/**
	 * Gibt die Lehrer-Objekte zu der Lerngruppen-ID zurück.
	 *
	 * @param id   die ID der Lerngruppe
	 *
	 * @returns die Lehrer-Objekte
	 */
	public lerngruppeGetFachlehrer(id: number) : List<ENMLehrer> {
		const result = new ArrayList<ENMLehrer>();
		const lerngruppe = this.mapLerngruppen.value.get(id);
		if (lerngruppe === null)
			return result;
		for (const idLehrer of lerngruppe.lehrerID) {
			const lehrer = this.mapLehrer.value.get(idLehrer);
			if (lehrer !== null)
				result.add(lehrer);
		}
		return result;
	}

	/**
	 * Gibt eine komma-separierten String für die Kürzel der Lehrer einer Lerngruppe zurück.
	 *
	 * @param id   die ID der Lerngruppe
	 *
	 * @returns der String mit den Kürzeln der Fachlehrer
	 */
	public lerngruppeGetFachlehrerOrNull(id: number) : string {
		const list = this.lerngruppeGetFachlehrer(id);
		return [...list].map(l => l.kuerzel).join(",");
	}

	/**
	 * Gibt die Kursart der Lerngruppe mit der übergebene ID zurück.
	 *
	 * @param id   die ID der Lerngruppe
	 *
	 * @returns die Kursart als String
	 */
	public lerngruppeGetKursartAsString(id: number) : string {
		const lerngruppe = this.mapLerngruppen.value.get(id);
		if (lerngruppe === null)
			return '';
		if (lerngruppe.kursartID === null)
			return '';
		return lerngruppe.kursartKuerzel ?? '';
	}

	/**
	 * Gibt die Bezeichnung des Kurses zurück.
	 *
	 * @param id   die ID der Lerngruppe
	 *
	 * @returns die Kurs-Bezeichnung
	 */
	public lerngruppeGetKursbezeichnung(id: number) : string {
		const lerngruppe = this.mapLerngruppen.value.get(id);
		if ((lerngruppe === null) || (lerngruppe.kursartID === null))
			return "";
		return lerngruppe.bezeichnung ?? "";
	}

	/**
	 * Gibt das Fachkürzel für das Fach der Lerngruppe zurück.
	 *
	 * @param id   die ID der Lerngruppe
	 *
	 * @returns das Fachkürzel
	 */
	public lerngruppeGetFachkuerzel(id: number) : string {
		const lerngruppe = this.mapLerngruppen.value.get(id);
		if (lerngruppe === null)
			return "";
		const fach = this.mapFaecher.value.get(lerngruppe.fachID);
		if (fach === null)
			return "";
		return fach.kuerzelAnzeige;
	}

	/**
	 * Gibt die Liste der Schüler der aktuellen Lerngruppenauswahl zurück;
	 */
	public lerngruppenAuswahlGetSchueler() : List<ENMSchueler> {
		return this.listLerngruppenAuswahlSchueler.value;
	}

	/**
	 * Gibt die Klasse des Schülers mit der übergebenen ID zurück.
	 *
	 * @param id   die ID des Schülers
	 *
	 * @returns die Klasse des Schülers
	 */
	public schuelerGetKlasse(id: number) : ENMKlasse {
		const schueler = this.mapSchueler.value.get(id);
		if (schueler === null)
			throw new DeveloperNotificationException("Der Schüler mit der ID " + id + " exististiert nicht.");
		const klasse = this.mapKlassen.value.get(schueler.klasseID);
		if (klasse === null)
			throw new DeveloperNotificationException("Der Klasse mit der ID " + schueler.klasseID + " des Schülers mit der ID " + id + " exististiert nicht.");
		return klasse;
	}

	/**
	 * Gibt die Leistungsdaten des Schülers mit der übergebenen ID zurück, welche zu den aktuell ausgewählten Lerngruppen
	 * gehören.
	 *
	 * @param id   die ID des Schülers
	 *
	 * @returns die Liste der Leistungsdaten
	 */
	public leistungenGetOfSchueler(id: number) : List<ENMLeistung> {
		return this.mapLerngruppenAuswahlSchuelerLeistungen.value.get(id) ?? new ArrayList<ENMLeistung>();
	}

}