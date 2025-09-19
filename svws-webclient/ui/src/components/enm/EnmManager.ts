import type { ENMDaten } from "../../../../core/src/core/data/enm/ENMDaten";
import type { ENMFach } from "../../../../core/src/core/data/enm/ENMFach";
import type { ENMFloskelgruppe } from "../../../../core/src/core/data/enm/ENMFloskelgruppe";
import type { ENMJahrgang } from "../../../../core/src/core/data/enm/ENMJahrgang";
import type { ENMKlasse } from "../../../../core/src/core/data/enm/ENMKlasse";
import type { ENMLehrer } from "../../../../core/src/core/data/enm/ENMLehrer";
import type { ENMLeistung } from "../../../../core/src/core/data/enm/ENMLeistung";
import type { ENMLerngruppe } from "../../../../core/src/core/data/enm/ENMLerngruppe";
import type { ENMSchueler } from "../../../../core/src/core/data/enm/ENMSchueler";
import type { ENMTeilleistung } from "../../../../core/src/core/data/enm/ENMTeilleistung";
import type { ENMTeilleistungsart } from "../../../../core/src/core/data/enm/ENMTeilleistungsart";
import type { Comparator } from "../../../../core/src/java/util/Comparator";
import type { JavaMap } from "../../../../core/src/java/util/JavaMap";
import type { JavaSet } from "../../../../core/src/java/util/JavaSet";
import type { List } from "../../../../core/src/java/util/List";
import { DeveloperNotificationException } from "../../../../core/src/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "../../../../core/src/java/util/ArrayList";
import { HashMap } from "../../../../core/src/java/util/HashMap";
import { HashSet } from "../../../../core/src/java/util/HashSet";
import { PairNN } from "../../../../core/src/asd/adt/PairNN";
import { HashMap2D } from "../../../../core/src/core/adt/map/HashMap2D";
import { Note } from "../../../../core/src/asd/types/Note";

/**
 * Das Interface für die Einträge der Auswahlliste für die Lerngruppen
 */
export interface EnmLerngruppenAuswahlEintrag {

	/** Die ID der Lerngruppe */
	id: number;

	/** Die Bezeichnung der Lerngruppe */
	bezeichnung: string;

	/** Die Klassen, welche bei der Lerngruppe vorhanden sind */
	klassen: string;

}

/**
 * Der Type für die Zuordnung von Floskelgruppen
 */
export type BemerkungenHauptgruppe = 'ASV'|'AUE'|'FACH'|'FÖRD'|'FSP'|'VERM'|'VERS'|'ZB';


/**
 * Ein Manager für die Verwaltung den ENM-Daten.
 */
export class EnmManager {

	/** Die ENM-Daten, welche diesem Manager zugrunde liegen */
	readonly daten: ENMDaten;

	/** Eine Referenz auf die ID des Lehrers, für welchen die ENM-Daten in diesem Manager verwaltet werden */
	readonly idLehrer: number | null;

	/** Gibt das Schuljahr der ENM-Daten zurück. */
	readonly schuljahr: number;

	/** Gibt das Halbjahr der ENM-Daten zurück. */
	readonly halbjahr: number;

	/** Die Liste aller Floskelgruppen */
	readonly listFloskelgruppen: List<ENMFloskelgruppe>;

	/** Eine Map von der ID der Jahrgänge auf deren Objekte */
	readonly mapJahrgaenge: JavaMap<number, ENMJahrgang> = new HashMap<number, ENMJahrgang>();

	/** Eine Map von der ID der Klassen auf deren Objekte */
	readonly mapKlassen: JavaMap<number, ENMKlasse> = new HashMap<number, ENMKlasse>();

	/** Eine Map von der ID der Lehrer auf deren Objekte */
	readonly mapLehrer: JavaMap<number, ENMLehrer> = new HashMap<number, ENMLehrer>();

	/** Eine Map von der ID der Fächer auf deren Objekte */
	readonly mapFaecher: JavaMap<number, ENMFach> = new HashMap<number, ENMFach>();

	/** Eine Map von der ID der Teilleistungsarten auf deren Objekte */
	readonly mapTeilleistungsarten: JavaMap<number, ENMTeilleistungsart> = new HashMap<number, ENMTeilleistungsart>();

	/** Eine Map von der ID der Lerngruppen auf deren Objekte */
	readonly mapLerngruppen: JavaMap<number, ENMLerngruppe> = new HashMap<number, ENMLerngruppe>();

	/** Eine Map von der ID der Schüler auf deren Objekte */
	readonly mapSchueler: JavaMap<number, ENMSchueler> = new HashMap<number, ENMSchueler>();

	/** Eine Map, welcher Lerngruppen-ID die Menge der zugehörigen Schüler-Objekte zuordnet */
	readonly mapLerngruppenSchueler: JavaMap<number, List<ENMSchueler>> = new HashMap<number, List<ENMSchueler>>();

	/** Eine Map, welche einer Lerngruppen-ID die Menge der zugeordneten Jahrgänge zuordnet */
	readonly mapLerngruppeJahrgaenge: HashMap<number, List<ENMJahrgang>> = new HashMap<number, List<ENMJahrgang>>();

	/** Eine Map, welche einer Klassen-ID die Menge der zugeordneten Schüler zuordnet */
	readonly mapKlassenSchueler: JavaMap<number, List<ENMSchueler>> = new HashMap<number, List<ENMSchueler>>();

	/** Eine Map, welche einer Lerngruppen-ID die Menge der zugeordneten Klassen zuordnet */
	readonly mapLerngruppeKlassen: JavaMap<number, List<ENMKlasse>> = new HashMap<number, List<ENMKlasse>>();

	/** Eine Map der Teilleistungsarten, die in Lerngruppen verwendet werden */
	readonly mapLerngruppeTeilleistungsarten: JavaMap<number, JavaSet<number>> = new HashMap<number, HashSet<number>>();

	/** Eine HahMap2D der Leistungen und Teilleistungsarten sowie der Teilleistung */
	readonly mapLeistungTeilleistungsartTeilleistung: HashMap2D<number, number, ENMTeilleistung> = new HashMap2D();

	readonly mapLerngruppeLeistungen: JavaMap<number, List<PairNN<ENMLeistung, ENMSchueler>>> = new HashMap();

	/** Die Menge aller Lerngruppen-IDs, wo der Lehrer bei der Lerngruppe als Fachlehrer eingetragen ist. */
	readonly setLerngruppenLehrer: JavaSet<number> = new HashSet<number>();

	/** Die Auswahlliste für die Lerngruppen */
	readonly mapLerngruppenAuswahl: JavaMap<number, EnmLerngruppenAuswahlEintrag> = new HashMap<number, EnmLerngruppenAuswahlEintrag>();

	/** Die Liste aller Klassen eines Klassenlehrers, sortiert nach Jahrgängen */
	readonly listKlassenKlassenlehrer: List<ENMKlasse> = new ArrayList<ENMKlasse>();

	/**
	 * Erstellt einen neue Enm-Manager für die übergebenen ENM-Daten
	 *
	 * @param daten      die ENM-Daten
	 * @param idLehrer   die ID des Lehrers, für welchen die ENM-Daten verwaltet werden
	 */
	public constructor(daten: ENMDaten, idLehrer: number | null) {
		this.daten = daten;
		this.idLehrer = idLehrer;
		this.schuljahr = daten.schuljahr;
		this.halbjahr = daten.aktuellerAbschnitt;
		this.listFloskelgruppen = daten.floskelgruppen;

		for (const j of daten.jahrgaenge)
			this.mapJahrgaenge.put(j.id, j);

		for (const k of daten.klassen) {
			this.mapKlassen.put(k.id, k);
			this.mapKlassenSchueler.put(k.id, new ArrayList());
			if ((this.idLehrer === null) || k.klassenlehrer.contains(this.idLehrer))
				this.listKlassenKlassenlehrer.add(k);
		}
		this.listKlassenKlassenlehrer.sort(this.comparatorKlassen);

		for (const l of daten.lehrer)
			this.mapLehrer.put(l.id, l);

		for (const f of daten.faecher)
			this.mapFaecher.put(f.id, f);

		for (const t of daten.teilleistungsarten)
			this.mapTeilleistungsarten.put(t.id, t);

		for (const l of daten.lerngruppen) {
			this.mapLerngruppen.put(l.id, l);
			this.mapLerngruppenSchueler.put(l.id, new ArrayList());
			this.mapLerngruppeJahrgaenge.put(l.id, new ArrayList());
			this.mapLerngruppeKlassen.put(l.id, new ArrayList());
			this.mapLerngruppeLeistungen.put(l.id, new ArrayList());
			this.mapLerngruppeTeilleistungsarten.put(l.id, new HashSet<number>());
		}

		daten.schueler.sort(this.comparatorSchueler);
		for (const s of daten.schueler) {
			this.mapSchueler.put(s.id, s);
			for (const leistung of s.leistungsdaten) {
				const idLerngruppe = leistung.lerngruppenID;
				const list = this.mapLerngruppenSchueler.get(idLerngruppe);
				if (list === null)
					throw new DeveloperNotificationException(`Die Lerngruppe mit der ID ${idLerngruppe} wird in Leistungsdaten angegeben, ist aber im Katalog der Lerngruppen nicht vorhanden.`);
				list.add(s);
				const set = this.mapLerngruppeTeilleistungsarten.get(leistung.lerngruppenID);
				if (set !== null)
					for (const teilleistung of leistung.teilleistungen) {
						set.add(teilleistung.artID);
						this.mapLeistungTeilleistungsartTeilleistung.put(leistung.id, teilleistung.artID, teilleistung);
					}
			}
			const klasse = this.mapKlassenSchueler.get(s.klasseID);
			if (klasse === null)
				throw new DeveloperNotificationException(`Die Klasse mit der ID ${s.klasseID} wird in Schülerdaten angegeben, ist aber im Katalog der Klassen nicht vorhanden.`);
			klasse.add(s);
		}

		for (const l of daten.lerngruppen) {
			const listSchueler = this.mapLerngruppenSchueler.get(l.id);
			const tmpKlassenIDs = new HashSet<number>();
			const tmpJahrgangIDs = new HashSet<number>();
			const listKlassen = new ArrayList<ENMKlasse>();
			const listJahrgaenge = new ArrayList<ENMJahrgang>();
			if (listSchueler !== null) {
				for (const s of listSchueler) {
					tmpKlassenIDs.add(s.klasseID);
					tmpJahrgangIDs.add(s.jahrgangID);
				}
			}
			for (const idKlasse of tmpKlassenIDs) {
				const klasse = this.mapKlassen.get(idKlasse);
				if (klasse === null)
					continue;
				listKlassen.add(klasse);
			}
			listKlassen.sort(this.comparatorKlassen);
			this.mapLerngruppeKlassen.put(l.id, listKlassen);
			for (const idJahrgang of tmpJahrgangIDs) {
				const jg = this.mapJahrgaenge.get(idJahrgang);
				if (jg === null)
					continue;
				listJahrgaenge.add(jg);
			}
			listJahrgaenge.sort(this.comparatorJahrgaenge);
			this.mapLerngruppeJahrgaenge.put(l.id, listJahrgaenge);
			if ((this.idLehrer === null) || l.lehrerID.contains(this.idLehrer)) {
				this.setLerngruppenLehrer.add(l.id);
			}
		}
		daten.lerngruppen.sort(this.comparatorLerngruppen);
		for (const l of daten.lerngruppen) {
			if ((this.idLehrer === null) || l.lehrerID.contains(this.idLehrer)) {
				this.mapLerngruppenAuswahl.put(l.id, <EnmLerngruppenAuswahlEintrag>{
					id: l.id,
					bezeichnung: this.lerngruppeGetBezeichnung(l.id),
					klassen: this.lerngruppeGetKlassenAsString(l.id),
				});
			}
		}

		for (const jg of this.mapLerngruppeJahrgaenge.values())
			jg.sort(this.comparatorJahrgaenge);

		for (const s of daten.schueler) {
			for (const l of s.leistungsdaten) {
				if (this.setLerngruppenLehrer.contains(l.lerngruppenID)) {
					const pair = new PairNN(l, s);
					const list = this.mapLerngruppeLeistungen.get(l.lerngruppenID);
					list?.add(pair);
				}
			}
		}
	}

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
		const aJgs = this.mapLerngruppeJahrgaenge.get(a.id);
		const bJgs = this.mapLerngruppeJahrgaenge.get(b.id);
		if (!(((aJgs === null) || (aJgs.size() !== 1)) && ((bJgs === null) || (bJgs.size() !== 1)))) {
			if ((aJgs === null) || (aJgs.size() !== 1))
				return -1;
			if ((bJgs === null) || (bJgs.size() !== 1))
				return 1;
			const aJg = aJgs.get(0);
			const bJg = bJgs.get(0);
			const tmp = this.compareJahrgaenge(aJg, bJg);
			if (tmp !== 0)
				return tmp;
		}
		// ... dann anhand der Klassen, sofern es Klassenunterricht ist, Kurse ggf. dann weiter hinten
		if ((a.kursartID === null) || (b.kursartID === null)) {
			if ((a.kursartID === null) && (b.kursartID !== null))
				return 1;
			if ((a.kursartID !== null) && (b.kursartID === null))
				return -1;
			const aKl = this.mapKlassen.get(a.kID);
			if (aKl === null)
				throw new DeveloperNotificationException(`Die Klasse mit der ID ${a.kID} wird in einer Lerngruppe angegeben, ist aber im Katalog der Klassen nicht vorhanden.`);
			const bKl = this.mapKlassen.get(b.kID);
			if (bKl === null)
				throw new DeveloperNotificationException(`Die Klasse mit der ID ${b.kID} wird in einer Lerngruppe angegeben, ist aber im Katalog der Klassen nicht vorhanden.`);
			const tmp = this.compareKlassen(aKl, bKl);
			if (tmp !== 0)
				return tmp;
		}
		// ... vergleiche dann bei Gleichheit dann anhand der Fach-Sortierung
		const aFach = this.mapFaecher.get(a.fachID);
		const bFach = this.mapFaecher.get(b.fachID);
		if (!((aFach === null) && (bFach === null))) {
			if (aFach === null)
				return -1;
			if (bFach === null)
				return 1;
			const tmp = aFach.sortierung - bFach.sortierung;
			if (tmp !== 0)
				return tmp;
		}
		// ... dann anhand der Bezeichnung der Lerngruppe
		if ((a.bezeichnung !== null) && (b.bezeichnung !== null))
			return a.bezeichnung.localeCompare(b.bezeichnung);
		if ((a.bezeichnung === null) && (b.bezeichnung !== null))
			return -1;
		if ((a.bezeichnung !== null) && (b.bezeichnung === null))
			return 1;
		// ... und ansonsten anhand der ID der Lerngruppe
		return a.id - b.id;
	}

	/** Definition des Comparators für zwei Lerngruppen */
	public comparatorLerngruppen = <Comparator<ENMLerngruppe>>{ compare: this.compareLerngruppen };

	/**
	 * Vergleicht zwei Jahrgänge miteinander und sortiert diese.
	 *
	 * @param a   der erste Jahrgang
	 * @param b   der zweite Jahrgang
	 *
	 * @returns der Wert für den Vergleich (< 0, 0 oder >0)
	 */
	protected compareJahrgaenge = (a : ENMJahrgang, b : ENMJahrgang) : number => {
		// Vergleiche zuerst anhand der gesetzten Sortierung des Jahrgangs...
		const tmp = a.sortierung - b.sortierung;
		if (tmp !== 0)
			return tmp;
		// ... und ansonsten anhand des Anzeige-Kürzels der Jahrgänge
		if ((a.kuerzelAnzeige !== null) && (b.kuerzelAnzeige !== null))
			return a.kuerzelAnzeige.localeCompare(b.kuerzelAnzeige);
		if (a.kuerzelAnzeige === null)
			return -1;
		if (b.kuerzelAnzeige === null)
			return 1;
		return 0;
	}

	/** Definition des Comparators für zwei Jahrgänge */
	public comparatorJahrgaenge = <Comparator<ENMJahrgang>>{ compare: this.compareJahrgaenge };

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

	/**
	 * Vergleicht zwei Schüler miteinander und sortiert diese.
	 *
	 * @param a   der erste Schüler
	 * @param b   der zweite Schüler
	 *
	 * @returns der Wert für den Vergleich (< 0, 0 oder >0)
	 */
	protected compareSchueler = (a : ENMSchueler, b : ENMSchueler) : number => {
		const aKlasse = this.mapKlassen.get(a.klasseID);
		const bKlasse = this.mapKlassen.get(b.klasseID);
		if ((aKlasse === null) && (bKlasse !== null))
			return -1;
		if ((aKlasse !== null) && (bKlasse === null))
			return 1;
		if ((aKlasse !== null) && (bKlasse !== null)) {
			const tmp = this.compareKlassen(aKlasse, bKlasse);
			if (tmp !== 0)
				return tmp;
		}
		if ((a.nachname !== null) && (b.nachname !== null)) {
			let tmp = a.nachname.localeCompare(b.nachname);
			if (tmp !== 0)
				return tmp;
			if ((a.vorname !== null) && (b.vorname !== null)) {
				tmp = a.vorname.localeCompare(b.vorname);
				if (tmp !== 0)
					return tmp;
				return a.id - b.id;
			}
			if ((a.vorname === null) && (b.vorname === null))
				return a.id - b.id;
			return (a.vorname === null) ? -1 : 1;
		}
		if ((a.nachname === null) && (b.nachname === null))
			return a.id - b.id;
		return (a.nachname === null) ? -1 : 1;
	}

	/** Definition des Comparators für zwei Schüler */
	public comparatorSchueler = <Comparator<ENMSchueler>>{ compare: this.compareSchueler };

	/**
	 * Vergleicht zwei Teilleistungsarten miteinander und sortiert diese.
	 *
	 * @param a   die erste Teilleistungsart
	 * @param b   die zweite Teilleistungsart
	 *
	 * @returns der Wert für den Vergleich (< 0, 0 oder >0)
	 */
	protected compareTeilleistungsarten = (a : ENMTeilleistungsart | null, b : ENMTeilleistungsart | null) : number => {
		if ((a === null) && (b === null))
			return 0;
		if ((a === null) || (a.sortierung === null))
			return -1;
		if ((b === null) || (b.sortierung === null))
			return 1;
		return a.sortierung - b.sortierung;
	}

	/** Definition des Comparators für zwei Teilleistungsarten */
	public comparatorTeilleistungsarten = <Comparator<ENMTeilleistungsart>>{ compare: this.compareTeilleistungsarten };

	/**
	 * Vergleicht zwei Teilleistungen miteinander und sortiert diese.
	 *
	 * @param a   die erste Teilleistung
	 * @param b   die zweite Teilleistung
	 *
	 * @returns der Wert für den Vergleich (< 0, 0 oder >0)
	 */
	protected compareTeilleistungen = (a : ENMTeilleistung, b : ENMTeilleistung) : number => {
		// Vergleiche zuerst anhand der gesetzten Sortierung der Teilleistungsarten...
		const aArt = this.mapTeilleistungsarten.get(a.artID);
		const bArt = this.mapTeilleistungsarten.get(b.artID);
		const tmp = this.compareTeilleistungsarten(aArt, bArt);
		if (tmp !== 0)
			return tmp;
		return a.id - b.id;
	}

	/** Vergleicht zwei Auswahlelemente. Sie sind verschieden, wenn die Schüler- oder Leistungsindizes oder die Leistungs-Id verschieden sind.  */
	public compareAuswahlLeistung(a: PairNN<ENMLeistung, ENMSchueler>|null, b: PairNN<ENMLeistung, ENMSchueler>|null): boolean {
		return (a === b);
	}

	/** Definition des Comparators für zwei Teilleistungen */
	public comparatorTeilleistungen = <Comparator<ENMTeilleistung>>{ compare: this.compareTeilleistungen };


	/**
	 * Bestimmt die Lerngruppe anhand der übergebenen ID
	 *
	 * @param id   die ID der Lerngruppe
	 *
	 * @returns die Lerngruppe
	 * @throws DeveloperNotificationException wenn die Lerngruppe nicht in den ENM-Daten existiert
	 */
	public lerngruppeByIDOrException(id: number) : ENMLerngruppe {
		const lerngruppe = this.mapLerngruppen.get(id);
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
		const klassen = this.mapLerngruppeKlassen.get(id);
		if (klassen === null)
			throw new DeveloperNotificationException("Fehler bei der Bestimmung der zugeordneten Klassen für die Lerngruppe.");
		if (klassen.isEmpty())
			return "—";
		return [...klassen].map(k => k.kuerzelAnzeige).join(",");
	}

	/**
	 * Prüft, ob der Lehrer, zu dem die ENM-Daten gehören Fachlehrer der Lerngruppe mit der übergebenen ID ist.
	 *
	 * @param id   die ID der zu prüfenden Lerngruppe
	 *
	 * @returns true, falls er Fachlehrer ist, und ansonsten false
	 */
	public lerngruppeIstFachlehrer(id: number) : boolean {
		return this.setLerngruppenLehrer.contains(id);
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
		const lerngruppe = this.mapLerngruppen.get(id);
		if (lerngruppe === null)
			return result;
		for (const idLehrer of lerngruppe.lehrerID) {
			const lehrer = this.mapLehrer.get(idLehrer);
			if (lehrer !== null)
				result.add(lehrer);
		}
		return result;
	}

	/**
	 * Gibt eine kommaseparierten String für die Kürzel der Lehrer einer Lerngruppe zurück.
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
	 * Gibt die Kursart der Leistung zurück.
	 *
	 * @param leistung   die Leistung
	 *
	 * @returns die Kursart als String
	 */
	public leistungGetKursartAsString(leistung: ENMLeistung) : string {
		// Bestimme die Lerngruppe zu der Leistung
		const lerngruppe = this.mapLerngruppen.get(leistung.lerngruppenID);
		if ((lerngruppe === null) || (lerngruppe.kursartID === null) || (lerngruppe.kursartKuerzel === null))
			return '';
		// Bei Grundkursen muss die Schriftlichkeit mit angezeigt werden
		let kuerzel = lerngruppe.kursartKuerzel;
		if (kuerzel === 'GK')
			kuerzel = kuerzel + ((leistung.istSchriftlich ?? false) ? "S" : "M");
		// Handelt es sich nicht um ein Abiturfach, so kann das allgemeine Kürzel zurückgegeben werden
		if (leistung.abiturfach === null)
			return kuerzel;
		// Setze ggf. die Kursart anhand des Abiturfaches
		const jahrgaenge = this.mapLerngruppeJahrgaenge.get(lerngruppe.id);
		if (jahrgaenge === null)
			return kuerzel;
		if (jahrgaenge.size() === 1) {
			const jahrgang = jahrgaenge.getFirst();
			if ((jahrgang.kuerzel === 'EF') || (jahrgang.kuerzel === 'S1') || (jahrgang.kuerzel === 'S2'))
				return kuerzel;
		}
		return ((leistung.abiturfach < 3) ? "LK" + leistung.abiturfach : "AB" + leistung.abiturfach);
	}

	/**
	 * Gibt die Bezeichnung des Kurses zurück.
	 *
	 * @param id   die ID der Lerngruppe
	 *
	 * @returns die Kurs-Bezeichnung
	 */
	public lerngruppeGetKursbezeichnung(id: number) : string {
		const lerngruppe = this.mapLerngruppen.get(id);
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
		const lerngruppe = this.mapLerngruppen.get(id);
		if (lerngruppe === null)
			return "";
		const fach = this.mapFaecher.get(lerngruppe.fachID);
		if (fach === null)
			return "";
		return fach.kuerzelAnzeige;
	}

	/**
	 * Gibt die Klasse des Schülers mit der übergebenen ID zurück.
	 *
	 * @param id   die ID des Schülers
	 *
	 * @returns die Klasse des Schülers
	 */
	public schuelerGetKlasse(id: number) : ENMKlasse {
		const schueler = this.mapSchueler.get(id);
		if (schueler === null)
			throw new DeveloperNotificationException("Der Schüler mit der ID " + id + " exististiert nicht.");
		const klasse = this.mapKlassen.get(schueler.klasseID);
		if (klasse === null)
			throw new DeveloperNotificationException("Der Klasse mit der ID " + schueler.klasseID + " des Schülers mit der ID " + id + " exististiert nicht.");
		return klasse;
	}

	/**
	 * Gibt zurück, ob die Fehlstunden für den übergebenen Schüler fachbezogen ermittelt werden oder nicht.
	 *
	 * @param schueler   der Schüler
	 */
	public fehlstundenFachbezogen(schueler: ENMSchueler) : boolean {
		return true;
		// TODO zukünftig anhand der Konfiguration entscheiden...
		// if (!this._daten.value.fehlstundenEingabe)
		// 	return false;
		// const jahrgang = this.mapJahrgaenge.value.get(schueler.jahrgangID);
		// if (jahrgang === null)
		// 	return false;
		// if (jahrgang.stufe === 'SI')
		// 	return this._daten.value.fehlstundenSIFachbezogen;
		// if ((jahrgang.stufe === 'SII-1') || (jahrgang.stufe === 'SII-2') || (jahrgang.stufe === 'SII-3'))
		// 	return this._daten.value.fehlstundenSIIFachbezogen;
		// return true;
	}

	public getKuerzelNote(input: string | null): string | undefined {
		return Note.fromKuerzel(input).daten(this.schuljahr)?.kuerzel;
	}

	public isValidQuartal(leistung: ENMLeistung) {
		const kuerzelNote = this.getKuerzelNote(leistung.noteQuartal);
		return ((kuerzelNote !== undefined) && (kuerzelNote !== ""));
	}

	public isValidNote(leistung: ENMLeistung): boolean {
		const kuerzelNote = this.getKuerzelNote(leistung.note);
		return ((kuerzelNote !== undefined) && (kuerzelNote !== ""));
	}

	public isValidFehlstunden(leistung: ENMLeistung) {
		return (!isNaN(Number(leistung.fehlstundenFach)))
				&& (Number(leistung.fehlstundenFach) <= 999)
				&& (Number(leistung.fehlstundenFach) >= 0)
				&& ((isNaN(Number(leistung.fehlstundenUnentschuldigtFach))) || (Number(leistung.fehlstundenFach) >= Number(leistung.fehlstundenUnentschuldigtFach)));
	}

	public isValidFehlstundenUnentschuldigt(leistung: ENMLeistung) {
		return (!isNaN(Number(leistung.fehlstundenUnentschuldigtFach)))
				&& (Number(leistung.fehlstundenUnentschuldigtFach) <= 999)
				&& (Number(leistung.fehlstundenUnentschuldigtFach) >= 0)
				&& (!isNaN(Number(leistung.fehlstundenFach)))
				&& (Number(leistung.fehlstundenUnentschuldigtFach) <= Number(leistung.fehlstundenFach));
	}

}
