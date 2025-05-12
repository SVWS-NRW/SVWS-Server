import { HashMap2D } from "@core/core/adt/map/HashMap2D";
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
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { Comparator } from "@core/java/util/Comparator";
import { HashMap } from "@core/java/util/HashMap";
import { HashSet } from "@core/java/util/HashSet";
import type { JavaMap } from "@core/java/util/JavaMap";
import type { JavaSet } from "@core/java/util/JavaSet";
import type { List } from "@core/java/util/List";
import { computed, ref, shallowRef } from "vue";
import type { Ref, ShallowRef } from "vue";

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
 * Das Interface für eine aktuelle Auswahl einer Leistung
 */
export interface EnmLeistungAuswahl {

	/** Der Index der ausgewählten Leistung in der aktuellen Auswahl der Liste der Schüler */
	indexSchueler: number;

	/** Der Index der ausgewählten Leistung in der aktuellen Auswahl der Liste der Leistungen der aktuellen Auswahl des Schülers */
	indexLeistung: number;

	/** Die ausgewählte Leistung */
	leistung: ENMLeistung | null;

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

	/** Eine Referenz auf die ID des Lehrers, für welchen die ENM-Daten in diesem Manager verwaltet werden */
	protected idLehrer: Ref<number>;

	/** Eine Referenz auf die aktuelle Auswahl von Lerngruppen des Lehrers, auf welche in diesem Manager gefiltert wird */
	protected _filterKlassen: ShallowRef<Array<ENMKlasse>>;

	/** Eine Referenz auf die aktuelle Auswahl von Lerngruppen des Lehrers, auf welche in diesem Manager gefiltert wird */
	protected _filterLerngruppen: ShallowRef<Array<EnmLerngruppenAuswahlEintrag>>;

	/** Eine Refernz auf die aktuelle Auswahl der Leistung zur Bearbeitung in diesem Manager */
	protected _auswahlLeistung: ShallowRef<EnmLeistungAuswahl>;

	/** Eine Refernz auf die aktuelle Auswahl der Leistung zur Bearbeitung in diesem Manager */
	protected _auswahlSchueler: Ref<ENMSchueler|null>;

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

	/** Die Liste aller Lerngruppen des Lehrers, sortiert nach den Jahrgängen */
	readonly listLerngruppenLehrer: List<ENMLerngruppe> = new ArrayList<ENMLerngruppe>();

	/** Die Menge aller Lerngruppen-IDs, wo der Lehrer bei der Lerngruppe als Fachlehrer eingetragen ist. */
	readonly setLerngruppenLehrer: JavaSet<number> = new HashSet<number>();

	/** Die Auswahlliste für die Lerngruppen */
	readonly listLerngruppenAuswahlliste: List<EnmLerngruppenAuswahlEintrag> = new ArrayList<EnmLerngruppenAuswahlEintrag>();

	/** Die Liste aller Klassen eines Klassenlehrers, sortiert nach Jahrgängen */
	readonly listKlassenKlassenlehrer: List<ENMKlasse> = new ArrayList<ENMKlasse>();

	/**
	 * Erstellt einen neue Enm-Manager für die übergebenen ENM-Daten
	 *
	 * @param daten      die ENM-Daten
	 * @param idLehrer   die ID des Lehrers, für welchen die ENM-Daten verwaltet werden
	 */
	public constructor(daten: ENMDaten, idLehrer: number) {
		this._daten = shallowRef<ENMDaten>(daten);
		this.idLehrer = ref<number>(idLehrer);
		this._filterLerngruppen = shallowRef<Array<EnmLerngruppenAuswahlEintrag>>(new Array<EnmLerngruppenAuswahlEintrag>());
		this._filterKlassen = shallowRef<Array<ENMKlasse>>(new Array<ENMKlasse>());
		this._auswahlLeistung = shallowRef<EnmLeistungAuswahl>({ indexSchueler: 0, indexLeistung: 0, leistung: null });
		this._auswahlSchueler = shallowRef<ENMSchueler|null>(null);

		for (const f of daten.foerderschwerpunkte)
			this.mapFoerderschwerpunkte.put(f.id, f);

		for (const j of daten.jahrgaenge)
			this.mapJahrgaenge.put(j.id, j);

		for (const k of daten.klassen) {
			this.mapKlassen.put(k.id, k);
			this.mapKlassenSchueler.put(k.id, new ArrayList());
			if (k.klassenlehrer.contains(this.idLehrer.value))
				this.listKlassenKlassenlehrer.add(k);
		}
		this.listKlassenKlassenlehrer.sort(this.comparatorKlassen);

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
			if (l.lehrerID.contains(this.idLehrer.value)) {
				this.listLerngruppenLehrer.add(l);
				this.setLerngruppenLehrer.add(l.id);
				this.listLerngruppenAuswahlliste.add(<EnmLerngruppenAuswahlEintrag>{
					id: l.id,
					bezeichnung: this.lerngruppeGetBezeichnung(l.id),
					klassen: this.lerngruppeGetKlassenAsString(l.id),
				});
			}
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
		}
	}

	/**
	 * Aktualisiert die Daten dieses Manager, in dem die Reaktivität des Daten-Attributs getriggert wird.
	 */
	public update(): void {
		Object.assign(this._daten.value, this._daten.value);
	}

	/**
	 * Gibt die aktuell im Manager gespeicherten Daten zurück.
	 */
	public get daten(): ENMDaten {
		return this._daten.value;
	}

	/** Gibt die aktuelle Auswahl der Lerngruppen zurück. */
	public get filterLerngruppen() : Array<EnmLerngruppenAuswahlEintrag> {
		return this._filterLerngruppen.value;
	}

	/** Setzt die aktuelle Auswahl der Lerngruppen. */
	public set filterLerngruppen(value : Array<EnmLerngruppenAuswahlEintrag>) {
		this._filterLerngruppen.value = value;
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
	public get auswahlLeistung() : EnmLeistungAuswahl {
		return this._auswahlLeistung.value;
	}

	/** Setzt die aktuell ausgewählte Leistung */
	public set auswahlLeistung(value: EnmLeistungAuswahl) {
		this._auswahlLeistung.value = value;
	}

	/** Gibt den aktuell ausgewählten Schüler zurück */
	public get auswahlSchueler() : ENMSchueler | null {
		return this._auswahlSchueler.value;
	}

	/** Setzt den aktuell ausgewählten Schüler */
	public set auswahlSchueler(value: ENMSchueler | null) {
		this._auswahlSchueler.value = value;
	}

	/**
	 * Setzt den aktuell ausgewählten Schüler auf den nächsten in der Liste
	 */
	public auswahlSchuelerNaechster(): void {
		const aktuell = this._auswahlSchueler.value;
		const listSchueler = this.klassenAuswahlGetSchueler();
		if ((aktuell === null) && (!listSchueler.isEmpty())) {
			this._auswahlSchueler.value = listSchueler.getFirst();
			return;
		}
		if (listSchueler.isEmpty()) {
			this._auswahlSchueler.value = null;
			return;
		}
		const index = listSchueler.indexOf(aktuell);
		if (index < 0) {
			this._auswahlSchueler.value = listSchueler.getFirst();
			return;
		}
		if ((index + 1) < listSchueler.size())
			this._auswahlSchueler.value = listSchueler.get(index + 1);
		else
			this._auswahlSchueler.value = listSchueler.getFirst();
	}

	/**
	 * Setzt den aktuell ausgewählten Schüler auf den vorherigen in der Liste
	 */
	public auswahlSchuelerVorheriger(): void {
		const aktuell = this._auswahlSchueler.value;
		const listSchueler = this.klassenAuswahlGetSchueler();
		if ((aktuell === null) && (!listSchueler.isEmpty())) {
			this._auswahlSchueler.value = listSchueler.getFirst();
			return;
		}
		if (listSchueler.isEmpty()) {
			this._auswahlSchueler.value = null;
			return;
		}
		const index = listSchueler.indexOf(aktuell);
		if (index < 0) {
			this._auswahlSchueler.value = listSchueler.getFirst();
			return;
		}
		if ((index) > 0)
			this._auswahlSchueler.value = listSchueler.get(index - 1);
		else
			this._auswahlSchueler.value = listSchueler.getLast();
	}

	/**
	 * Setzt die aktuell ausgewählte Leistung auf die nächste verfügbare Leistung.
	 */
	public auswahlLeistungNaechste(): void {
		const aktuell = this._auswahlLeistung.value;
		let valid = (aktuell.leistung !== null);
		const listSchueler = this.lerngruppenAuswahlGetSchueler();
		if (listSchueler.isEmpty()) {
			this._auswahlLeistung.value = { indexSchueler : 0, indexLeistung : 0, leistung: null };
			return;
		}
		if ((aktuell.indexSchueler < 0) || (aktuell.indexSchueler >= listSchueler.size()))
			valid = false;
		const schueler = valid ? listSchueler.get(aktuell.indexSchueler) : null;
		const listLeistungen = (schueler !== null) ? this.leistungenGetOfSchueler(schueler.id) : null;
		if ((listLeistungen === null) || (aktuell.indexLeistung < 0) || (aktuell.indexLeistung >= listLeistungen.size()))
			valid = false;
		const leistung = (listLeistungen !== null && valid) ? listLeistungen.get(aktuell.indexLeistung) : null;
		if (leistung?.id !== aktuell.leistung?.id)
			valid = false;
		let indexLeistung = aktuell.indexLeistung + 1;
		let indexSchueler = aktuell.indexSchueler;
		if (valid && (listLeistungen !== null)) {
			if (indexLeistung >= listLeistungen.size())
				indexLeistung = 0;
			if (indexLeistung === 0) {
				indexSchueler++;
				if (indexSchueler >= listSchueler.size())
					return; // Nichts zu tun, da die aktuell ausgewählte Leistung die letzte Leistung der Liste ist
			}
		} else {
			indexSchueler = (schueler !== null) ? aktuell.indexSchueler : 0;
			indexLeistung = (leistung !== null) ? aktuell.indexLeistung : 0;
		}
		const neuSchueler = listSchueler.get(indexSchueler);
		const neuListLeistung = this.leistungenGetOfSchueler(neuSchueler.id);
		const neuLeistung = neuListLeistung.get(indexLeistung);
		this._auswahlLeistung.value = { indexSchueler, indexLeistung, leistung: neuLeistung };
		return;
	}

	/**
	 * Setzt die aktuell ausgewählte Leistung auf die vorherige verfügbare Leistung.
	 */
	public auswahlLeistungVorherige(): void {
		const aktuell = this._auswahlLeistung.value;
		let valid = (aktuell.leistung !== null);
		const listSchueler = this.lerngruppenAuswahlGetSchueler();
		if (listSchueler.isEmpty()) {
			this._auswahlLeistung.value = { indexSchueler : 0, indexLeistung : 0, leistung: null };
			return;
		}
		if ((aktuell.indexSchueler < 0) || (aktuell.indexSchueler >= listSchueler.size()))
			valid = false;
		const schueler = valid ? listSchueler.get(aktuell.indexSchueler) : null;
		const listLeistungen = (schueler !== null) ? this.leistungenGetOfSchueler(schueler.id) : null;
		if ((listLeistungen === null) || (aktuell.indexLeistung < 0) || (aktuell.indexLeistung >= listLeistungen.size()))
			valid = false;
		const leistung = (listLeistungen !== null) ? listLeistungen.get(aktuell.indexLeistung) : null;
		if (leistung?.id !== aktuell.leistung?.id)
			valid = false;
		let indexLeistung = aktuell.indexLeistung - 1;
		let indexSchueler = aktuell.indexSchueler;
		let neuLeistung;
		if (valid && (listLeistungen !== null)) {
			if (indexLeistung < 0) {
				indexSchueler--;
				if (indexSchueler < 0)
					return; // Nichts zu tun, da die aktuell ausgewählte Leistung die erste Leistung der Liste ist
			}
			const neuSchueler = listSchueler.get(indexSchueler);
			const neuListLeistung = this.leistungenGetOfSchueler(neuSchueler.id);
			if (indexLeistung < 0)
				indexLeistung = neuListLeistung.size() - 1;
			neuLeistung = neuListLeistung.get(indexLeistung);
		} else {
			indexSchueler = (schueler !== null) ? aktuell.indexSchueler : 0;
			indexLeistung = (leistung !== null) ? aktuell.indexLeistung : 0;
			const neuSchueler = listSchueler.get(indexSchueler);
			const neuListLeistung = this.leistungenGetOfSchueler(neuSchueler.id);
			neuLeistung = neuListLeistung.get(indexLeistung);
		}
		this._auswahlLeistung.value = { indexSchueler, indexLeistung, leistung: neuLeistung };
		return;
	}

	/** Gibt das Schuljahr der ENM-Daten zurück. */
	public get schuljahr(): number {
		return this._daten.value.schuljahr;
	}

	/** Gibt das Halbjahr der ENM-Daten zurück. */
	public get halbjahr(): number {
		return this._daten.value.aktuellerAbschnitt;
	}

	/** Die aktuelle Auswahl der Lerngruppen */
	protected listLerngruppenAuswahl = computed<List<ENMLerngruppe>>(() => {
		const lerngruppen = (this.filterLerngruppen.length === 0) ? this.listLerngruppenAuswahlliste : this.filterLerngruppen;
		const result = new ArrayList<ENMLerngruppe>();
		for (const l of lerngruppen) {
			const lerngruppe = this.mapLerngruppen.get(l.id);
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
			const listLerngruppenSchueler = this.mapLerngruppenSchueler.get(lerngruppe.id);
			if (listLerngruppenSchueler === null)
				continue;
			for (const schueler of listLerngruppenSchueler)
				idsSchueler.add(schueler.id);
		}
		const result = new ArrayList<ENMSchueler>();
		for (const idSchueler of idsSchueler) {
			const schueler = this.mapSchueler.get(idSchueler);
			if (schueler === null)
				continue;
			result.add(schueler);
		}
		result.sort(this.comparatorSchueler);
		return result;
	});

	/** Die Liste der Schüler, die durch die aktuelle Lerngruppenauswahl ausgewählt sind und dort Leistungsdaten haben */
	protected listLerngruppenAuswahlSchuelerMitLeistungsdaten = computed<List<ENMSchueler>>(() => {
		const result = new ArrayList<ENMSchueler>();
		for (const schueler of this.listLerngruppenAuswahlSchueler.value) {
			if (this.mapLerngruppenAuswahlSchuelerLeistungen.value.get(schueler.id) === null)
				continue;
			result.add(schueler);
		}
		return result;
	});

	/** Die aktuelle Auswahl der Klassen */
	protected listKlassenAuswahl = computed<List<ENMKlasse>>(() => {
		const klassen = (this.filterKlassen.length === 0) ? this.listKlassenKlassenlehrer : this.filterKlassen;
		const result = new ArrayList<ENMKlasse>();
		for (const k of klassen) {
			const klasse = this.mapKlassen.get(k.id);
			if (klasse === null)
				continue;
			result.add(klasse);
		}
		return result;
	});

	/** Die Menge der Lerngruppen-IDs, die zu der Auswahl der aktuellen Lerngruppen gehört */
	protected setKlassenAuswahlIDs = computed<HashSet<number>>(() => {
		const result = new HashSet<number>();
		for (const klasse of this.listKlassenAuswahl.value)
			result.add(klasse.id);
		return result;
	});

	/** Die Liste der Schüler, die durch die aktuelle Klassenauswahl ausgewählt sind */
	protected listKlassenAuswahlSchueler = computed<List<ENMSchueler>>(() => {
		const result = new ArrayList<ENMSchueler>();
		for (const klasse of this.listKlassenAuswahl.value) {
			const listKlassenSchueler = this.mapKlassenSchueler.get(klasse.id);
			if (listKlassenSchueler === null)
				continue;
			result.addAll(listKlassenSchueler);
		}
		result.sort(this.comparatorSchueler);
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

	/** Eine HashMap mit den Leistungen der aktuellen Lerngruppenauswahl */
	protected mapLerngruppenAuswahlLeistungen = computed<HashMap<number, ENMLeistung>>(() => {
		const result = new HashMap<number, ENMLeistung>();
		for (const schueler of this.listLerngruppenAuswahlSchueler.value)
			for (const leistung of schueler.leistungsdaten)
				result.put(leistung.id, leistung);
		return result;
	});

	/** Eine HashMap mit den Schülerteilleistungen der aktuellen Lerngruppenauswahl zugeordnet zur Leistungs-ID */
	protected mapLerngruppenAuswahlSchuelerTeilleistungen = computed<HashMap2D<number, number, ENMTeilleistung>>(() => {
		const result = new HashMap2D<number, number, ENMTeilleistung>();
		for (const schuelerLeistungen of this.mapLerngruppenAuswahlSchuelerLeistungen.value.values())
			for (const leistung of schuelerLeistungen)
				for (const teilleistung of leistung.teilleistungen)
					result.put(leistung.id, teilleistung.id, teilleistung);
		return result;
	});

	/** Eine Hashmap mit den IDs der Teilleistungsarten als Menge, zugeordnet zu den Leistungen, bei denen sie vorkommen */
	protected mapLerngruppenAuswahlLeistungTeilleistungenByTeilleistungsartenId = computed<HashMap<number, HashMap<number, ENMTeilleistung>>>(() => {
		const result = new HashMap<number, HashMap<number, ENMTeilleistung>>();
		const tmp = this.mapLerngruppenAuswahlSchuelerTeilleistungen.value;
		for (const idLeistung of tmp.getKeySet()) {
			const setTeilleistungsarten = new HashMap<number, ENMTeilleistung>();
			for (const idTeilleistung of tmp.getKeySetOf(idLeistung)) {
				const teilleistung = tmp.getOrNull(idLeistung, idTeilleistung);
				if (teilleistung === null)
					continue;
				const teilleistungsart = this.mapTeilleistungsarten.get(teilleistung.artID);
				if (teilleistungsart === null)
					continue;
				setTeilleistungsarten.put(teilleistungsart.id, teilleistung);
			}
			result.put(idLeistung, setTeilleistungsarten);
		}
		return result;
	});

	/** Eine Liste mit den in Teilleistungen verwendendeten Teilleistungsarten */
	protected listLerngruppenAuswahlTeilleistungsarten = computed<List<ENMTeilleistungsart>>(() => {
		const setArten = new HashSet<number>();
		for (const mapTeilleistungsarten of this.mapLerngruppenAuswahlLeistungTeilleistungenByTeilleistungsartenId.value.values())
			for (const idTeilleistungsart of mapTeilleistungsarten.keySet())
				setArten.add(idTeilleistungsart);
		const result = new ArrayList<ENMTeilleistungsart>();
		for (const idTeilleistungsart of setArten) {
			const art = this.mapTeilleistungsarten.get(idTeilleistungsart);
			if (art === null)
				continue;
			result.add(art);
		}
		result.sort(this.comparatorTeilleistungsarten);
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
	public compareAuswahlLeistung(a: EnmLeistungAuswahl, b: EnmLeistungAuswahl): boolean {
		return (a.leistung?.id === b.leistung?.id) && (a.indexLeistung === b.indexLeistung) && (a.indexSchueler === b.indexSchueler);
	}

	/** Definition des Comparators für zwei Teilleistungen */
	public comparatorTeilleistungen = <Comparator<ENMTeilleistung>>{ compare: this.compareTeilleistungen };


	/** Gibt die Auswahlliste für die Lerngruppen zurück. */
	public get lerngruppenAuswahlliste() : List<EnmLerngruppenAuswahlEintrag> {
		return this.listLerngruppenAuswahlliste;
	}

	/** Gibt die Lerngruppen zurück, welche dem Lehrer zugeordnet sind. */
	public get lerngruppenOfLehrer() : List<ENMLerngruppe> {
		return this.listLerngruppenLehrer;
	}

	/** Gibt die Liste der Klassen eines Klassenlehrers zurück */
	public get klassenOfKlassenlehrer(): List<ENMKlasse> {
		return this.listKlassenKlassenlehrer;
	}

	/**
	 * Bestimmt die Lerngruppe anhand der übergebenen ID
	 *
	 * @param id   die ID der Lerngruppe
	 *
	 * @returns die Lerngruppe oder null
	 */
	public lerngruppeByIDOrNull(id: number) : ENMLerngruppe | null {
		return this.mapLerngruppen.get(id);
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
	public lerngruppeGetKursartAsString(id: number) : string {
		const lerngruppe = this.mapLerngruppen.get(id);
		if (lerngruppe === null)
			return '';
		if (lerngruppe.kursartID === null)
			return '';
		return lerngruppe.kursartKuerzel ?? '';
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
	 * Gibt die Liste der Schüler der aktuellen Lerngruppenauswahl zurück;
	 *
	 * @returns die Liste der Schüler der aktuellen Lerngruppenauswahl
	 */
	public lerngruppenAuswahlGetSchueler() : List<ENMSchueler> {
		return this.listLerngruppenAuswahlSchueler.value;
	}

	/**
	 * Gibt die Liste der Schüler mit Leistungen in der aktuellen Lerngruppenauswahl zurück;
	 *
	 * @returns die Liste der Schüler mit Leistungen in der aktuellen Lerngruppenauswahl
	 */
	public lerngruppenAuswahlGetSchuelerMitLeistungsdaten() : List<ENMSchueler> {
		return this.listLerngruppenAuswahlSchuelerMitLeistungsdaten.value;
	}

	/**
	 * Gibt die Liste der Schüler der aktuellen Klassenauswahl zurück;
	 *
	 * @returns die Liste der Schüler der aktuellen Klassenauswahl
	 */
	public klassenAuswahlGetSchueler() : List<ENMSchueler> {
		return this.listKlassenAuswahlSchueler.value;
	}


	/**
	 * Gibt die Liste der vorkommenden Teilleistungsarten der aktuellen Lerngruppenauswahl zurück;
	 *
	 * @returns die Liste der vorkommenden Teilleistungsarten der aktuellen Lerngruppenauswahl
	 */
	public lerngruppenAuswahlGetTeilleistungsarten() : List<ENMTeilleistungsart> {
		return this.listLerngruppenAuswahlTeilleistungsarten.value;
	}


	/**
	 * Prüft, ob die Teilleistungsart mit der übergebenen ID bei der Leistung mit der übergebenen ID vorkommt.
	 *
	 * @param idLeistung           die ID der Teilleistungsart
	 * @param idTeilleistungsart   die ID der Teilleistungsart
	 *
	 * @returns true, wenn die Teilleistungsart bei der Leistung existiert, ansonsten false
	 */
	public lerngruppenAuswahlLeistungHatTeilleistungsart(idLeistung: number, idTeilleistungsart: number) : boolean {
		return this.mapLerngruppenAuswahlLeistungTeilleistungenByTeilleistungsartenId.value.get(idLeistung)?.containsKey(idTeilleistungsart) ?? false;
	}

	/**
	 * Gibt die Teilleistung für die übergebene Leistungs-ID und die übergebene Teilleistungsart-ID zurück.
	 *
	 * @param idLeistung           die ID der Leistung
	 * @param idTeilleistungsart   die ID der Teilleistungsart
	 *
	 * @returns die Teilleistung oder null, falls eine solche Teilleistung nicht existiert.
	 */
	public lerngruppenAuswahlGetTeilleistungOrNull(idLeistung: number, idTeilleistungsart: number) : ENMTeilleistung | null {
		return this.mapLerngruppenAuswahlLeistungTeilleistungenByTeilleistungsartenId.value.get(idLeistung)?.get(idTeilleistungsart) ?? null;
	}

	/**
	 * Gibt an, ob es für eine Leistungs-ID eine Leistung in der aktuellen Lerngruppenauswahl gibt
	 *
	 * @param idLeistung			die ID der Leistung
	 *
	 * @returns die Leistung, wenn in derAuswahl vorhanden, ansonsten null
	 */
	public lerngruppenAuswahlGetLeistungOrNull(idLeistung: number|null): ENMLeistung | null {
		return this.mapLerngruppenAuswahlLeistungen.value.get(idLeistung);
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
