import type { ShallowRef } from "vue";
import type { ENMDaten } from "@core/core/data/enm/ENMDaten";
import type { ENMFach } from "@core/core/data/enm/ENMFach";
import type { ENMFloskelgruppe } from "@core/core/data/enm/ENMFloskelgruppe";
import type { ENMFoerderschwerpunkt } from "@core/core/data/enm/ENMFoerderschwerpunkt";
import type { ENMJahrgang } from "@core/core/data/enm/ENMJahrgang";
import type { ENMKlasse } from "@core/core/data/enm/ENMKlasse";
import type { ENMLehrer } from "@core/core/data/enm/ENMLehrer";
import type { ENMLeistung } from "@core/core/data/enm/ENMLeistung";
import type { ENMLerngruppe } from "@core/core/data/enm/ENMLerngruppe";
import type { ENMSchueler } from "@core/core/data/enm/ENMSchueler";
import type { ENMTeilleistung } from "@core/core/data/enm/ENMTeilleistung";
import type { ENMTeilleistungsart } from "@core/core/data/enm/ENMTeilleistungsart";
import type { Comparator } from "@core/java/util/Comparator";
import type { JavaMap } from "@core/java/util/JavaMap";
import type { JavaSet } from "@core/java/util/JavaSet";
import type { List } from "@core/java/util/List";
import { shallowRef, triggerRef } from "vue";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import { HashMap } from "@core/java/util/HashMap";
import { HashSet } from "@core/java/util/HashSet";
import { PairNN } from "@core/asd/adt/PairNN";
import { HashMap2D } from "@core/core/adt/map/HashMap2D";
import type { ListIterator } from "@core/java/util/ListIterator";

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

	/** Eine Referenz auf die ENM-Daten */
	protected _daten: ShallowRef<ENMDaten>;

	/** Eine Referenz auf die aktuelle Auswahl von Lerngruppen des Lehrers, auf welche in diesem Manager gefiltert wird */
	protected _filterKlassen: ShallowRef<Array<ENMKlasse>>;

	/** Eine Referenz auf die aktuell ausgewählte Klasse eines Klassenlehrers */
	protected _auswahlKlasse: ShallowRef<ENMKlasse|null>;

	/** Eine Referenz auf die aktuelle Auswahl von Lerngruppen des Lehrers, auf welche in diesem Manager gefiltert wird */
	protected _filterLerngruppen: ShallowRef<Array<EnmLerngruppenAuswahlEintrag>>;

	/** Eine Refernz auf die aktuelle Auswahl der Leistung zur Bearbeitung in diesem Manager */
	protected _auswahlLeistung: ShallowRef<PairNN<ENMLeistung, ENMSchueler>|null>;

	/** Eine Referenz auf die aktuelle Auswahl der Leistung zur Bearbeitung in diesem Manager */
	protected _auswahlSchueler: ShallowRef<ENMSchueler|null>;

	/** Die Liste aller Schüler, die in der Klassenlehreransicht dargestellt werden */
	protected _listKlasseAuswahlSchueler: ShallowRef<List<ENMSchueler>>;

	/** Ermittelt anhand der ausgewählten Lerngruppen die benötigten Leistungen als Pair */
	protected _auswahlPairLeistungSchueler: ShallowRef<List<PairNN<ENMLeistung, ENMSchueler>>>;

	/** Ein ListenIterator der Leistung/Schüler-Paare */
	protected _linkedListPairsLeistungSchueler: ListIterator<PairNN<ENMLeistung, ENMSchueler>> = new ArrayList<PairNN<ENMLeistung, ENMSchueler>>().listIterator();

	/** Ein ListenIterator der Schüler in der Klassenleitungsauswahl */
	protected _linkedListSchueler: ListIterator<ENMSchueler> = new ArrayList<ENMSchueler>().listIterator();

	/** Eine Referenz auf die ID des Lehrers, für welchen die ENM-Daten in diesem Manager verwaltet werden */
	readonly idLehrer: number;

	/** Gibt das Schuljahr der ENM-Daten zurück. */
	readonly schuljahr: number;

	/** Gibt das Halbjahr der ENM-Daten zurück. */
	readonly halbjahr: number;

	/** Die Liste aller Floskelgruppen */
	readonly listFloskelgruppen: List<ENMFloskelgruppe>;

	/** Eine Map von der ID der Förderschwerpunkte auf deren Objekte */
	readonly mapFoerderschwerpunkte: JavaMap<number, ENMFoerderschwerpunkt> = new HashMap<number, ENMFoerderschwerpunkt>();

	/** Eine Map von der ID der Jahrgänge auf deren Objekte */
	readonly mapJahrgaenge: JavaMap<number, ENMJahrgang> = new HashMap<number, ENMJahrgang>();

	/** Eine Map von der ID der Klassen auf deren Objekte */
	readonly mapKlassen: JavaMap<number, ENMKlasse> = new HashMap<number, ENMKlasse>();

	/** Eine Map von der ID der Floskelgruppen auf deren Objekte */
	readonly mapFloskelgruppen: JavaMap<string, ENMFloskelgruppe> = new HashMap<string, ENMFloskelgruppe>();

	/** Eine Map von der ID der Lehrer auf deren Objekte */
	readonly mapLehrer: JavaMap<number, ENMLehrer> = new HashMap<number, ENMLehrer>();

	/** Eine Map von der ID der Fächer auf deren Objekte */
	readonly mapFaecher: JavaMap<number, ENMFach> = new HashMap<number, ENMFach>();

	/** Eine Map von der ID der Teilleistungsarten auf deren Objekte */
	readonly mapTeilleistungsarten: JavaMap<number, ENMTeilleistungsart> = new HashMap<number, ENMTeilleistungsart>();

	/** Eine Map von der ID der Lerngruppen auf deren Objekte */
	readonly mapLerngruppen: JavaMap<number, ENMLerngruppe> = new HashMap<number, ENMLerngruppe>();

	/** Ein Set mit Lerngruppen-IDs */
	readonly setLerngruppenIDs: JavaSet<number> = new HashSet<number>();

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

	/** Die Liste aller Lerngruppen des Lehrers, sortiert nach den Jahrgängen */
	readonly listLerngruppenLehrer: List<ENMLerngruppe> = new ArrayList<ENMLerngruppe>();

	/** Die Menge aller Lerngruppen-IDs, wo der Lehrer bei der Lerngruppe als Fachlehrer eingetragen ist. */
	readonly setLerngruppenLehrer: JavaSet<number> = new HashSet<number>();

	/** Die Auswahlliste für die Lerngruppen */
	readonly listLerngruppenAuswahlliste: List<EnmLerngruppenAuswahlEintrag> = new ArrayList<EnmLerngruppenAuswahlEintrag>();

	/** Die Liste aller Klassen eines Klassenlehrers, sortiert nach Jahrgängen */
	readonly listKlassenKlassenlehrer: List<ENMKlasse> = new ArrayList<ENMKlasse>();

	/** Die Liste aller Paare mit Leistung und Schüler des Lehrers */
	readonly listPairsLeistungSchueler: List<PairNN<ENMLeistung, ENMSchueler>> = new ArrayList();

	/** Die Liste aller Lerngruppen */
	readonly listLerngruppen: List<ENMLerngruppe> = new ArrayList();


	/**
	 * Erstellt einen neue Enm-Manager für die übergebenen ENM-Daten
	 *
	 * @param daten      die ENM-Daten
	 * @param idLehrer   die ID des Lehrers, für welchen die ENM-Daten verwaltet werden
	 */
	public constructor(daten: ENMDaten, idLehrer: number) {
		this._daten = shallowRef<ENMDaten>(daten);
		this._filterLerngruppen = shallowRef<Array<EnmLerngruppenAuswahlEintrag>>(new Array<EnmLerngruppenAuswahlEintrag>());
		this._filterKlassen = shallowRef<Array<ENMKlasse>>(new Array<ENMKlasse>());
		this._auswahlLeistung = shallowRef<PairNN<ENMLeistung, ENMSchueler>|null>(null);
		this._auswahlSchueler = shallowRef<ENMSchueler|null>(null);

		this.idLehrer = idLehrer;
		this.schuljahr = daten.schuljahr;
		this.halbjahr = daten.aktuellerAbschnitt;
		this.listFloskelgruppen = daten.floskelgruppen;
		this.listLerngruppen = daten.lerngruppen;

		for (const f of daten.foerderschwerpunkte)
			this.mapFoerderschwerpunkte.put(f.id, f);

		for (const j of daten.jahrgaenge)
			this.mapJahrgaenge.put(j.id, j);

		for (const k of daten.klassen) {
			this.mapKlassen.put(k.id, k);
			this.mapKlassenSchueler.put(k.id, new ArrayList());
			if (k.klassenlehrer.contains(this.idLehrer))
				this.listKlassenKlassenlehrer.add(k);
		}
		this.listKlassenKlassenlehrer.sort(this.comparatorKlassen);
		if (!this.listKlassenKlassenlehrer.isEmpty()) {
			this._auswahlKlasse = shallowRef(this.listKlassenKlassenlehrer.getFirst());
			const schueler = this.mapKlassenSchueler.get(this._auswahlKlasse.value?.id);
			if (schueler !== null)
				this._listKlasseAuswahlSchueler = shallowRef(schueler);
			else
				this._listKlasseAuswahlSchueler = shallowRef(new ArrayList());
		}
		else {
			this._auswahlKlasse = shallowRef(null);
			this._listKlasseAuswahlSchueler = shallowRef(new ArrayList());
		}

		for (const f of daten.floskelgruppen)
			this.mapFloskelgruppen.put(f.kuerzel, f);

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
			this.mapLerngruppeTeilleistungsarten.put(l.id, new HashSet<number>());
			this.setLerngruppenIDs.add(l.id);
		}
		this.listLerngruppenLehrer.sort(this.comparatorLerngruppen);

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
			const listJahrgaenge = this.mapLerngruppeJahrgaenge.get(l.id)!;
			const listSchueler = this.mapLerngruppenSchueler.get(l.id);
			if (l.kursartID === null) {
				// Klassenunterricht - Bestimme den Jahrgang der Klasse
				const klasse = this.mapKlassen.get(l.kID);
				if (klasse !== null) {
					const jahrgang = this.mapJahrgaenge.get(klasse.idJahrgang);
					if (jahrgang !== null)
						listJahrgaenge.add(jahrgang);
				}
			} else {
				// Kursunterricht - Bestimme die Jahrgänge aller Schüler
				const setJahrgaenge = new HashSet<number>();
				if (listSchueler !== null)
					for (const schueler of listSchueler)
						setJahrgaenge.add(schueler.jahrgangID);
				for (const idJahrgang of setJahrgaenge) {
					const jahrgang = this.mapJahrgaenge.get(idJahrgang);
					if (jahrgang !== null)
						listJahrgaenge.add(jahrgang);
				}
			}
			const tmpKlassenIDs = new HashSet<number>();
			const listKlassen = new ArrayList<ENMKlasse>();
			if (listSchueler !== null)
				for (const s of listSchueler)
					tmpKlassenIDs.add(s.klasseID);
			for (const idKlasse of tmpKlassenIDs) {
				const klasse = this.mapKlassen.get(idKlasse);
				if (klasse === null)
					continue;
				listKlassen.add(klasse);
			}
			listKlassen.sort(this.comparatorKlassen);
			this.mapLerngruppeKlassen.put(l.id, listKlassen);
			if (l.lehrerID.contains(this.idLehrer)) {
				this.listLerngruppenLehrer.add(l);
				this.setLerngruppenLehrer.add(l.id);
				this.listLerngruppenAuswahlliste.add(<EnmLerngruppenAuswahlEintrag>{
					id: l.id,
					bezeichnung: this.lerngruppeGetBezeichnung(l.id),
					klassen: this.lerngruppeGetKlassenAsString(l.id),
				});
			}
		}
		for (const s of daten.schueler)
			for (const l of s.leistungsdaten)
				if (this.setLerngruppenLehrer.contains(l.lerngruppenID))
					this.listPairsLeistungSchueler.add(new PairNN(l, s));

		this._auswahlPairLeistungSchueler = shallowRef(this.listPairsLeistungSchueler);
	}

	/**
	 * Aktualisiert die Daten dieses Manager, in dem die Reaktivität des Daten-Attributs getriggert wird.
	 */
	public update(): void {
		// Object.assign(this._daten.value, this._daten.value);
		triggerRef(this._daten)
	}

	/** Gibt die aktuelle Auswahl der Lerngruppen zurück. */
	public get filterLerngruppen() : Array<EnmLerngruppenAuswahlEintrag> {
		return this._filterLerngruppen.value;
	}
	/** Setzt die aktuelle Auswahl der Lerngruppen. */
	public set filterLerngruppen(value : Array<EnmLerngruppenAuswahlEintrag>) {
		this._filterLerngruppen.value = value;
		if ((value.length === 0) || (value.length === this.listLerngruppenAuswahlliste.size()))
			this._auswahlPairLeistungSchueler.value = this.listPairsLeistungSchueler;
		else {
			const set = new HashSet<number>();
			for (const entry of value)
				set.add(entry.id);
			const list = new ArrayList<PairNN<ENMLeistung, ENMSchueler>>();
			for (const p of this.listPairsLeistungSchueler)
				if (set.contains(p.a.lerngruppenID))
					list.add(p);
			this._auswahlPairLeistungSchueler.value = list;
		}
		this._linkedListPairsLeistungSchueler = this._auswahlPairLeistungSchueler.value.listIterator();
	}

	/** Gibt die aktuelle Auswahl der Klassen zurück. */
	public get filterKlassen() : Array<ENMKlasse> {
		return this._filterKlassen.value;
	}
	/** Setzt die aktuelle Auswahl der Klassen. */
	public set filterKlassen(value : Array<ENMKlasse>) {
		this._filterKlassen.value = value;
	}

	/** Gibt die aktuell ausgewählte Leistung zurück */
	public get auswahlLeistung() : PairNN<ENMLeistung, ENMSchueler>|null {
		return this._auswahlLeistung.value;
	}
	/** Setzt die aktuell ausgewählte Leistung */
	public set auswahlLeistung(value: PairNN<ENMLeistung, ENMSchueler>|null) {
		if (value === this._auswahlLeistung.value)
			return;
		this._auswahlLeistung.value = value;
		if ((value === null) || !this.currentListContainsAuswahl(value))
			return;
		let weiter = true;
		while (weiter) {
			if (this._linkedListPairsLeistungSchueler.hasNext()) {
				const pair = this._linkedListPairsLeistungSchueler.next();
				if (pair === value)
					weiter = false;
			} else
				this._linkedListPairsLeistungSchueler = this._auswahlPairLeistungSchueler.value.listIterator();
		}
	}

	// /** Gibt den aktuell ausgewählte Klasse eines Klassenlehrers zurück */
	public get auswahlKlasse() : ENMKlasse | null {
		return this._auswahlKlasse.value;
	}
	// /** Setzt den aktuell ausgewählte Klasse eines Klassenlehrers */
	public set auswahlKlasse(value: ENMKlasse | null) {
		if (this._auswahlKlasse.value === value)
			return;
		this._auswahlKlasse.value = value;
		const schueler = this.mapKlassenSchueler.get(this._auswahlKlasse.value?.id);
		this._listKlasseAuswahlSchueler.value = schueler ?? new ArrayList<ENMSchueler>();
		if (schueler !== null)
			this.auswahlSchueler = schueler.getFirst();
	}

	// /** Gibt den aktuell ausgewählten Schüler zurück */
	public get auswahlSchueler() : ENMSchueler | null {
		return this._auswahlSchueler.value;
	}
	// /** Setzt den aktuell ausgewählten Schüler */
	public set auswahlSchueler(value: ENMSchueler | null) {
		if (value === this._auswahlSchueler.value)
			return;
		this._auswahlSchueler.value = value;
		if ((value === null) || !this._listKlasseAuswahlSchueler.value.contains(value))
			return;
		let weiter = true;
		while (weiter) {
			if (this._linkedListSchueler.hasNext()) {
				const schueler = this._linkedListSchueler.next();
				if (schueler === value)
					weiter = false;
			} else {
				this._linkedListSchueler = this._listKlasseAuswahlSchueler.value.listIterator();
			}
		}
	}

	/** Das aktuell ausgewählte Paar<Leistung, Schüler> */
	public auswahlPairLeistungSchueler() {
		return this._auswahlPairLeistungSchueler.value;
	}


	/** geht ein Element weiter in der Leistung/Schüler-Liste */
	public linkedListPairNext() {
		if (this._linkedListPairsLeistungSchueler.hasNext()) {
			const pair = this._linkedListPairsLeistungSchueler.next();
			this._auswahlLeistung.value = (pair === this._auswahlLeistung.value && this._linkedListPairsLeistungSchueler.hasNext())
				? this._linkedListPairsLeistungSchueler.next()
				: pair;
		}
	}

	/** geht ein Element zurück in der Leistung/Schüler-Liste */
	public linkedListPairPrevious() {
		if (this._linkedListPairsLeistungSchueler.hasPrevious()) {
			const pair = this._linkedListPairsLeistungSchueler.previous();
			this._auswahlLeistung.value = (pair === this._auswahlLeistung.value && this._linkedListPairsLeistungSchueler.hasPrevious())
				? this._linkedListPairsLeistungSchueler.previous()
				: pair;
		}
	}

	/** prüft, ob es ein nächstes Element gibt bei den Leistung/Schüler Paaren */
	public linkedListPairHasNext() {
		return this._linkedListPairsLeistungSchueler.hasNext();
	}


	// protected _linkedListSchueler = computed(() => this._listKlassenAuswahlSchueler.value.listIterator());

	/** Gehe einen Schüler weiter in der Liste */
	public schuelerNext() {
		if (this._linkedListSchueler.hasNext()) {
			const schueler = this._linkedListSchueler.next();
			this._auswahlSchueler.value = (schueler === this._auswahlSchueler.value && this._linkedListSchueler.hasNext())
				? this._linkedListSchueler.next()
				: schueler;
		}
	}

	/** Gehe einen Schüler zurück in der Liste */
	public schuelerPrevious() {
		if (this._linkedListSchueler.hasPrevious()) {
			const schueler = this._linkedListSchueler.previous();
			this._auswahlSchueler.value = (schueler === this._auswahlSchueler.value && this._linkedListSchueler.hasPrevious())
				? this._linkedListSchueler.previous()
				: schueler;
		}
	}

	/** Prüft, ob der Schüler einen Nachfolger hat */
	public linkedListSchuelerHasNext() {
		return this._linkedListSchueler.hasNext();
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
			const tmp = aJg.sortierung - bJg.sortierung;
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
	 * @returns die Lerngruppe oder null
	 */
	// public lerngruppeByIDOrNull(id: number) : ENMLerngruppe | null {
	// 	return this.mapLerngruppen.get(id);
	// }

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
	 * Gibt die Kursart der Lerngruppe mit der übergebene ID zurück.
	 *
	 * @param id   die ID der Lerngruppe
	 *
	 * @returns die Kursart als String
	 */
	// public lerngruppeGetKursartAsString(id: number) : string {
	// 	const lerngruppe = this.mapLerngruppen.get(id);
	// 	if (lerngruppe === null)
	// 		return '';
	// 	if (lerngruppe.kursartID === null)
	// 		return '';
	// 	return lerngruppe.kursartKuerzel ?? '';
	// }

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
	 * Gibt die Liste der Schüler der aktuellen Klassenauswahl zurück;
	 *
	 * @returns die Liste der Schüler der aktuellen Klassenauswahl
	 */
	public klasseAuswahlGetSchueler() : List<ENMSchueler> {
		return this._listKlasseAuswahlSchueler.value;
	}

	/**
	 * Prüft, ob die aktuelle Liste mit Leistungen die übergebene Leistungsauswahl enthält
	 *
	 * @param auswahl   die Leistungsauswahl, deren Vorhandensein in der Liste geprüft werden soll
	 *
	 * @returns true, wenn die Leistungsauswahl in der Liste enthalten ist, ansonsten false
	 */
	public currentListContainsAuswahl(auswahl: PairNN<ENMLeistung, ENMSchueler>|null): boolean {
		return (auswahl !== null) && this._auswahlPairLeistungSchueler.value.contains(auswahl);
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

}
