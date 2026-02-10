import { computed, ref, shallowRef, triggerRef, type ComputedRef } from "vue";
import type { Collection, Comparator, ENMAbteilung, ENMJahrgang, ENMKlasse, ENMTeilleistungsart, JavaMap, List } from "@core";
import { ArrayList, ENMConfigKlasse, ENMConfigKlasseSpalte, HashMap, HashMap2D, HashSet, JavaString } from "@core";
import { comparatorENMAbteilung, comparatorENMJahrgang, comparatorENMKlasse } from "./NotenmodulUtils";
import type { GridColumn } from "../../../../../ui/src/ui/controls/tablegrid/GridManager";

/** Der Typ für die einzelnen Einträge in der Liste der Klassen bzw. Klassengruppen */
export type NotenmodulConfigManagerSperrungenZeile = ENMConfigKlasse | NotenmodulConfigManagerSperrungenGruppe;

/** Der Typ für eine Spalte in einem Eintrag in der Liste der Klassen bzw. Klassengruppen */
export type NotenmodulConfigManagerSperrungenZelle = ENMConfigKlasseSpalte | NotenmodulConfigManagerSperrungenGruppeSpalte;

/** Der Typ für die konkrete Gruppierung von Sperreinträgen bei der Eingabe */
export type NotenmodulConfigManagerSperrungenGruppierung = 'Keine' | 'Jahrgang' | 'Abteilung';

export class NotenmodulConfigManagerSperrungenGruppeSpalte {

	/** Die ID dieser Teilleistung in der SVWS-DB, sofern es sich um eine Teilleistung handelt. */
	public idTeilleistung: number | null = null;

	/** Der Name der Spalte */
	public name: string = "";

	/** Gibt an, ob die Spalte gesperrt werden soll oder nicht. Hier wird die Anzahl der Sperrungen in der Gruppe gezählt. */
	public gesperrt: number = 0;

}


export class NotenmodulConfigManagerSperrungenGruppe {

	/** Die ID der Wrapper aus der SVWS-DB (z.B. 16) */
	public id: number = 0;

	/** Der Zeitstempel, ab wann die Noteneingabe erlaubt ist, sofern eine Einschränkung vorliegt, sonst null. */
	public tsEingabeAb: string | null = null;

	/** Der Zeitstempel, bis wann die Noteneingabe erlaubt ist, sofern eine Einschränkung vorliegt, sonst null. */
	public tsEingabeBis: string | null = null;

	/** Gibt an, ob die Fehlstunden Wrappern- oder kursweise eingegeben werden. Hier wird die Anzahl der Klassenweisen Eingaben in der Gruppe gezählt. */
	public istFehlstundenEingabeKlassenweise: number = 0;

	/** die globale Konfiguration für die einzelnen Spalten für diese Wrapper. */
	public spalten: List<NotenmodulConfigManagerSperrungenGruppeSpalte> = new ArrayList<NotenmodulConfigManagerSperrungenGruppeSpalte>();

	/** Die Zuordnung der Klassen zu der Abteilung. */
	public readonly klassenzuordnungen: List<number> = new ArrayList<number>();

}

/**
 * Dieser Manager organisiert die Spalten der Leistungsmatrix und legt anhand der bestehenden Config
 * und den ENM-Daten fest, welche Spalten in der Matrix angezeigt werden für die Auswahl
 */
export class NotenmodulConfigManagerSperrungen {

	/** Das aktuelle Datum, das einmal pro Minute aktualisiert wird */
	private readonly datum = ref<Date>(new Date());

	/** Ein Array mit den Namen aller sperrbaren Spalten in den Ansichten des Notenmoduls */
	private readonly spaltenSperrbar = ["Quartalsnoten", "Note", "Mahnung", "Fehlstunden", "FB", "ASV", "AUE", "ZB", "Teilnoten"];

	/** Legt die Art der Gruppierung fest, welche vom Manager vorgenommen wird */
	private readonly _gruppierung = shallowRef<NotenmodulConfigManagerSperrungenGruppierung>('Keine');
	/** Der Setter für die Festlegung der Gruppierung */
	private readonly setGruppierung: (value: NotenmodulConfigManagerSperrungenGruppierung) => Promise<void>;

	/** Legt fest, ob die einzelnen Teilnoten angezeigt werden sollen oder nur gruppiert */
	private readonly _zeigeTeilnoten = shallowRef<boolean>(false);

	private readonly _mapKlassen: JavaMap<number, ENMKlasse>;
	private readonly _mapJahrgaenge: JavaMap<number, ENMJahrgang>;
	private readonly _mapAbteilungen: JavaMap<number, ENMAbteilung>;
	private readonly _mapConfigKlassen = new HashMap<number, ENMConfigKlasse>();
	private readonly _mapTeilleistungsarten: JavaMap<number, ENMTeilleistungsart>;
	private readonly _mapDefaultConfigKlasseSpalte: JavaMap<number | string, ENMConfigKlasseSpalte>;

	private readonly _listKlassen = new ArrayList<ENMConfigKlasse>();
	private readonly _mapKlassenSpalte = new HashMap2D<number, string, ENMConfigKlasseSpalte>();

	private readonly _listJahrgaenge = new ArrayList<ENMJahrgang>();
	private readonly _mapJahrgangKlassen = new HashMap<number, List<ENMConfigKlasse>>();
	private readonly _showJahrgangsklassen = shallowRef(new HashSet<number>());
	private readonly _mapJahrgangGruppe = shallowRef(new HashMap<number, NotenmodulConfigManagerSperrungenGruppe>());
	private readonly _mapJahrgangSpalte = new HashMap2D<number, string, NotenmodulConfigManagerSperrungenGruppeSpalte>();
	private readonly _mapKlassenToJahrgang = new HashMap<number, NotenmodulConfigManagerSperrungenGruppe>();
	private readonly _mapSpaltenKlassenToJahrgang = new HashMap2D<number, string, NotenmodulConfigManagerSperrungenGruppeSpalte>();

	private readonly _listAbteilungen = new ArrayList<ENMAbteilung>();
	private readonly _mapAbteilungKlassen = new HashMap<number, List<ENMConfigKlasse>>();
	private readonly _showAbteilungsklassen = shallowRef(new HashSet<number>());
	private readonly _mapAbteilungGruppe = shallowRef(new HashMap<number, NotenmodulConfigManagerSperrungenGruppe>());
	private readonly _mapAbteilungSpalte = new HashMap2D<number, string, NotenmodulConfigManagerSperrungenGruppeSpalte>();
	private readonly _mapKlassenToAbteilung = new HashMap<number, NotenmodulConfigManagerSperrungenGruppe>();
	private readonly _mapSpaltenKlassenToAbteilung = new HashMap2D<number, string, NotenmodulConfigManagerSperrungenGruppeSpalte>();

	private readonly _configAlleKlassen = shallowRef(new NotenmodulConfigManagerSperrungenGruppe());
	private readonly _mapAlleKlassenSpalte = new HashMap<string, NotenmodulConfigManagerSperrungenGruppeSpalte>();

	/** Eine Callback-Methode, die bei einem Update der Konfiguration aufgerufen wird. */
	private readonly writeConfig: () => Promise<void>;


	/**
	 * Erstellt einen neuen Manager.
	 *
	 * @param listConfig              die Liste mit den Konfigurationen für eine Klasse
	 * @param mapKlassen              eine Map der Klassen
	 * @param mapTeilleistungsarten   eine Map der Teilleistungsarten
	 * @param mapJahrgaenge           eine Map der Jahrgänge
	 * @param mapAbteilungen          eine Map der Abteilungen
	 * @param writeConfig             eine Callback-Methode, um das Schreiben der Konfiguration zu veranlassen
	 * @param gruppierung             die gewählte Gruppierung
	 * @param setGruppierung          eine Callback-Methode, um das Schreiben der Gruppierung in der Konfiguration zu veranlassen
	 */
	constructor(listConfig: List<ENMConfigKlasse>, mapKlassen: JavaMap<number, ENMKlasse>,
		mapTeilleistungsarten: JavaMap<number, ENMTeilleistungsart>, mapJahrgaenge: JavaMap<number, ENMJahrgang>,
		mapAbteilungen: JavaMap<number, ENMAbteilung>, writeConfig: () => Promise<void>,
		gruppierung: NotenmodulConfigManagerSperrungenGruppierung, setGruppierung: (value: NotenmodulConfigManagerSperrungenGruppierung) => Promise<void>) {
		this._mapKlassen = mapKlassen;
		this._mapJahrgaenge = mapJahrgaenge;
		this._mapAbteilungen = mapAbteilungen;
		this._mapTeilleistungsarten = mapTeilleistungsarten;
		this._mapDefaultConfigKlasseSpalte = this.genDefaultMapConfigKlasseSpalte();
		this._gruppierung.value = gruppierung;
		this.setGruppierung = setGruppierung;

		// Initialisiere die Konfigurationseinträge für die Klassen
		this.initListKlassen(listConfig);
		for (const item of this._listKlassen) {
			this._mapConfigKlassen.put(item.id, item);
			for (const spalte of item.spalten) {
				this._mapKlassenSpalte.put(item.id, spalte.name, spalte);
			}
		}

		setInterval(() => this.datum.value = new Date(), 60_000);

		// Initialisiere die Gruppen für Konfigurationseinträge (Jahrgänge und Abteilungen)
		this.initJahrgaenge();
		this.initAbteilungen();
		this.initAlleKlassen();
		this.updateGruppierung(this._gruppierung.value);

		this.writeConfig = writeConfig;
	}

	private now(datum: Date): string {
		return datum.toLocaleString('en-CA', {
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
	 * Initialisiert die Jahrgänge mit den dazugehörigen Klassen
	 */
	private initJahrgaenge(): void {
		this.initListJahrgaenge();
		this.initMapJahrgangKlassen();
		for (const jg of this._listJahrgaenge) {
			const gruppe = this.genJahrgangGruppe(jg.id);
			this._mapJahrgangGruppe.value.put(jg.id, gruppe);
			for (const idKlasse of gruppe.klassenzuordnungen) {
				this._mapKlassenToJahrgang.put(idKlasse, gruppe);
			}
			for (const spalte of gruppe.spalten) {
				this._mapJahrgangSpalte.put(jg.id, spalte.name, spalte);
				for (const idKlasse of gruppe.klassenzuordnungen) {
					const kl = this._mapKlassenSpalte.getOrNull(idKlasse, spalte.name);
					if (kl !== null) {
						this._mapSpaltenKlassenToJahrgang.put(idKlasse, spalte.name, spalte);
					}
				}
			}
		}
	}

	/**
	 * Initialisiert die Abteilungen mit den dazugehörigen Klassen
	 * */
	private initAbteilungen() {
		this.initListAbteilungen();
		this.initMapAbteilungKlassen();
		for (const a of this._listAbteilungen) {
			const gruppe = this.genAbteilungGruppe(a.id);
			this._mapAbteilungGruppe.value.put(a.id, gruppe);
			for (const idKlasse of gruppe.klassenzuordnungen) {
				this._mapKlassenToAbteilung.put(idKlasse, gruppe);
			}
			for (const spalte of gruppe.spalten) {
				this._mapAbteilungSpalte.put(a.id, spalte.name, spalte);
				for (const idKlasse of gruppe.klassenzuordnungen) {
					const kl = this._mapKlassenSpalte.getOrNull(idKlasse, spalte.name);
					if (kl !== null) {
						this._mapSpaltenKlassenToAbteilung.put(idKlasse, spalte.name, spalte);
					}
				}
			}
		}
	}

	/**
	 * Initialisiert die Gruppe für alle Klassen
	 * */
	private initAlleKlassen() {
		this._configAlleKlassen.value = this.genGruppe(-1, this._listKlassen);
		for (const spalte of this._configAlleKlassen.value.spalten) {
			this._mapAlleKlassenSpalte.put(spalte.name, spalte);
		}
	}


	/**
	 * Gibt eine Klasse zurück
	 *
	 * @param id   die ID der Klasse
	 *
	 * @returns die Klasse oder null
	 */
	public getKlasse(id: number): ENMKlasse | null {
		return this._mapKlassen.get(id);
	}

	/**
	 * Gibt einen Jahrgang zurück
	 *
	 * @param id   die ID des Jahrgang
	 *
	 * @returns der Jahrgang oder null
	 */
	public getJahrgang(id: number): ENMJahrgang | null {
		return this._mapJahrgaenge.get(id);
	}

	/**
	 * Gibt eine Abteilung zurück
	 *
	 * @param id   die ID der Abteilung
	 *
	 * @returns die Abteilung oder null
	 */
	public getAbteilung(id: number): ENMAbteilung | null {
		return this._mapAbteilungen.get(id);
	}

	/** Definition des Comparators für zwei ENMConfigKlasseObjekte */
	public comparatorENMConfigKlassen = <Comparator<ENMConfigKlasse>>{ compare: (aa: ENMConfigKlasse, bb: ENMConfigKlasse): number => {
		const a = this._mapKlassen.get(aa.id);
		const b = this._mapKlassen.get(bb.id);
		if (a === null || b === null) {
			return 0;
		}
		return comparatorENMKlasse.compare(a, b);
	} };

	/**
	 * Diese Funktion generiert die Liste der Configs für die Klassen und verwendet dabei bestehende Konfigurationen bzw. Defaultwerte
	 *
	 * @param listConfig Die Liste der ENM-Configdaten der Klassen
	 */
	private initListKlassen(listConfig: List<ENMConfigKlasse>): void {
		const mapConfigKlassen = new HashMap<number, ENMConfigKlasse>();
		for (const item of listConfig) {
			mapConfigKlassen.put(item.id, item);
		}
		for (const klasse of this._mapKlassen.entrySet()) {
			const tempKlasse = mapConfigKlassen.get(klasse.getKey());
			if (tempKlasse === null) {
				const defaultConfig = this.generateDefaultConfigKlasse(klasse.getKey());
				this._listKlassen.add(defaultConfig);
			} else {
				const checkedKlasse = this.checkConfigKlasse(tempKlasse);
				this._listKlassen.add(checkedKlasse);
			}
		}
		this._listKlassen.sort(this.comparatorENMConfigKlassen);
	}

	/**
	 * Diese Funktion initialisiert die Liste der Jahrgänge für die Klassen und verwendet dabei bestehende Konfigurationen bzw. Defaultwerte
	 */
	private initListJahrgaenge(): void {
		for (const entry of this._mapJahrgaenge.entrySet()) {
			this._listJahrgaenge.add(entry.getValue());
		}
		this._listJahrgaenge.sort(comparatorENMJahrgang);
	}

	/**
	 * Diese Funktion generiert die Liste der Abteilungen für die Klassen und verwendet dabei bestehende Konfigurationen bzw. Defaultwerte
	 * @returns eine Liste, zusammengesetzt aus Config und Default
	 */
	private initListAbteilungen(): void {
		for (const entry of this._mapAbteilungen.entrySet()) {
			this._listAbteilungen.add(entry.getValue());
		}
		this._listAbteilungen.sort(comparatorENMAbteilung);
	}

	/**
	 * Prüft eine Konfiguration und passt sie ggf. an
	 *
	 * @param klasse   die Klassenkonfiguration
	 * @returns gibt eine geprüfte Konbfiguration zurück, die aus allen Informationen, gespeichert in der DB und aus den ENM-Daten generiert wurde
	 */
	private checkConfigKlasse(klasse: ENMConfigKlasse): ENMConfigKlasse {
		const list = new ArrayList<ENMConfigKlasseSpalte>();
		const set = new HashSet<number | string>();
		for (const spalte of klasse.spalten) {
			if (spalte.idTeilleistung === null) {
				list.add(spalte);
				set.add(spalte.name);
			} else if (this._mapDefaultConfigKlasseSpalte.containsKey(spalte.idTeilleistung)) {
				const art = this._mapTeilleistungsarten.get(spalte.idTeilleistung);
				if (art !== null) {
					spalte.name = art.bezeichnung ?? '—';
					list.add(spalte);
					set.add(spalte.idTeilleistung);
				}
			}
		}
		for (const art of this._mapTeilleistungsarten.values()) {
			if (!set.contains(art.id)) {
				const spalte = new ENMConfigKlasseSpalte();
				spalte.idTeilleistung = art.id;
				spalte.name = art.bezeichnung ?? '—';
				list.add(spalte);
			}
		}
		for (const art of this.spaltenSperrbar) {
			if (!set.contains(art)) {
				const spalte = new ENMConfigKlasseSpalte();
				spalte.name = art;
				list.add(spalte);
			}
		}
		klasse.spalten = list;
		return klasse;
	}

	/**
	 * Generiert eine Standardkonfiguration für die Spalten einer Klass
	 *
	 * @returns gibt ein JavaMap mit den Spalten für eine Config-Klasse zurück
	 */
	private genDefaultMapConfigKlasseSpalte(): JavaMap<number | string, ENMConfigKlasseSpalte> {
		const mapConfigKlasseSpalte = new HashMap<number | string, ENMConfigKlasseSpalte>();
		for (const art of this._mapTeilleistungsarten.values()) {
			const klasseSpalte = new ENMConfigKlasseSpalte();
			klasseSpalte.idTeilleistung = art.id;
			klasseSpalte.name = art.bezeichnung ?? '—';
			mapConfigKlasseSpalte.put(art.id, klasseSpalte);
		}
		for (const art of this.spaltenSperrbar) {
			const spalte = new ENMConfigKlasseSpalte();
			spalte.name = art;
			mapConfigKlasseSpalte.put(art, spalte);
		}
		return mapConfigKlasseSpalte;
	}

	/**
	 * Generiert eine Default-Klassenkonfiguration
	 *
	 * @param id   die ID der Klasse
	 * @returns gibt eine Klassenkonfiguration zurück
	 */
	public generateDefaultConfigKlasse(id: number): ENMConfigKlasse {
		const config = new ENMConfigKlasse();
		config.id = id;
		const now = null;
		config.tsEingabeAb = now;
		config.tsEingabeBis = now;
		config.spalten.addAll(this._mapDefaultConfigKlasseSpalte.values());
		return config;
	}
	/**
	 * Generiert eine Jahrgang-Gruppe
	 *
	 * @param id   die ID des Jahrgangs
	 */
	public genJahrgangGruppe(id: number): NotenmodulConfigManagerSperrungenGruppe {
		const klassen = this._mapJahrgangKlassen.get(id) ?? new ArrayList<ENMConfigKlasse>();
		return this.genGruppe(id, klassen);
	}

	/**
	 * Generiert eine Abteilungs-Gruppe
	 *
	 * @param id   die ID der Abteilung
	 */
	public genAbteilungGruppe(id: number): NotenmodulConfigManagerSperrungenGruppe {
		const klassen = this._mapAbteilungKlassen.get(id) ?? new ArrayList<ENMConfigKlasse>();
		return this.genGruppe(id, klassen);
	}

	/**
	 * Generiert eine Gruppe für eine Abteilung etc.
	 *
	 * @param id        die ID für die Gruppe, z.B. die einer Abteilung
	 * @param klassen   die zugehörigen Klassen
	 *
	 * @returns gibt eine Gruppenkonfiguration zurück
	 */
	public genGruppe(id: number, klassen: List<ENMConfigKlasse>): NotenmodulConfigManagerSperrungenGruppe {
		const config = new NotenmodulConfigManagerSperrungenGruppe();
		config.id = id;
		const now = null; // new Date().toISOString().slice(0, -3).replace('T', ' ');
		config.tsEingabeAb = now;
		config.tsEingabeBis = now;
		for (const spalte of this._mapDefaultConfigKlasseSpalte.values()) {
			config.spalten.add(this.genGruppeSpalte(spalte, klassen));
		}
		for (const klasse of klassen) {
			config.klassenzuordnungen.add(klasse.id);
		}
		return config;
	}

	/**
	 * Generiert die Spalten für eine Gruppe
	 *
	 * @param spalte    die Spalte
	 * @param klassen   die Klassen
	 *
	 * @returns die generierte Gruppenspalte
	 */
	public genGruppeSpalte(spalte: ENMConfigKlasseSpalte, klassen: List<ENMConfigKlasse>): NotenmodulConfigManagerSperrungenGruppeSpalte {
		const val = new NotenmodulConfigManagerSperrungenGruppeSpalte();
		val.idTeilleistung = spalte.idTeilleistung;
		val.name = spalte.name;
		for (const klasse of klassen) {
			for (const klasseSpalte of klasse.spalten) {
				if (spalte.name === klasseSpalte.name) {
					if (klasseSpalte.gesperrt) {
						val.gesperrt++;
					}
					break;
				}
			}
		}
		return val;
	}

	/**
	 * Initialisiert die Daten für die Jahrgänge
	 */
	private initMapJahrgangKlassen(): void {
		for (const klasse of this._listKlassen) {
			const idJahrgang = this._mapKlassen.get(klasse.id)?.idJahrgang ?? null;
			const jahrgang = this._mapJahrgaenge.get(idJahrgang);
			if (jahrgang === null) {
				continue;
			}
			const entry = this._mapJahrgangKlassen.get(jahrgang.id);
			if (entry === null) {
				const list = new ArrayList<ENMConfigKlasse>();
				list.add(klasse);
				this._mapJahrgangKlassen.put(jahrgang.id, list);
			} else {
				entry.add(klasse);
			}
		}
	}

	/**
	 * initialisiert die Daten für die Abteilungen
	 */
	private initMapAbteilungKlassen(): void {
		for (const abteilung of this._listAbteilungen) {
			const list = new ArrayList<ENMConfigKlasse>();
			for (const klasse of abteilung.klassenzuordnungen) {
				const config = this._mapConfigKlassen.get(klasse);
				if (config !== null) {
					list.add(config);
				}
			}
			if (list.size() > 0) {
				this._mapAbteilungKlassen.put(abteilung.id, list);
			}
		}
	}

	/** Liefert die Konfiguration als JSON-String */
	get json(): string {
		const arr = new Array<string>();
		for (const k of this._listKlassen) {
			arr.push(ENMConfigKlasse.transpilerToJSON(k));
		}
		return "[" + arr.join() + "]";
	}

	/** Liefert die Gruppierungsmöglichkeiten für die Anzeige der Klassen */
	get gruppierungen(): Array<NotenmodulConfigManagerSperrungenGruppierung> {
		return ['Keine', 'Jahrgang', 'Abteilung'] as const;
	}

	/** Liefert die aktuell ausgewählte Gruppierung für die Anzeige */
	get gruppierung(): NotenmodulConfigManagerSperrungenGruppierung {
		return this._gruppierung.value;
	}

	/** Setzt die aktuell ausgewählte Gruppierung für die Anzeige */
	set gruppierung(value: NotenmodulConfigManagerSperrungenGruppierung) {
		if (value === this._gruppierung.value) {
			return;
		}
		this._gruppierung.value = value;
		void this.setGruppierung(value);
		this.updateGruppierung(value);
	}

	/** Gibt zurück, ob die einzelnen Teilnoten angezeigt werden sollen oder nur gruppiert */
	get zeigeTeilnoten(): boolean {
		return this._zeigeTeilnoten.value;
	}

	/** Setzt, ob die einzelnen Teilnoten angezeigt werden sollen oder nur gruppiert */
	set zeigeTeilnoten(value: boolean) {
		this._zeigeTeilnoten.value = value;
	}

	/** Wechselt den Zustand, ob die einzelnen Teilnoten angezeigt werden sollen oder nur gruppiert. */
	public toggleZeigeTeilnoten() {
		this._zeigeTeilnoten.value = !this._zeigeTeilnoten.value;
	}


	/**
	 * Aktualisiert die übergebenen Gruppen aus der Collection
	 *
	 * @param gruppen   die zu aktualisierenden Gruppen
	 */
	private updateGruppen(gruppen: Collection<NotenmodulConfigManagerSperrungenGruppe>) {
		for (const gruppe of gruppen) {
			gruppe.istFehlstundenEingabeKlassenweise = 0;
			let tsEingabeAb = undefined;
			let tsEingabeBis = undefined;
			for (const idKlasse of gruppe.klassenzuordnungen) {
				const row = this._mapConfigKlassen.get(idKlasse);
				if (row === null) {
					continue;
				}
				if (row.istFehlstundenEingabeKlassenweise) {
					gruppe.istFehlstundenEingabeKlassenweise++;
				}
				if (tsEingabeAb === undefined) {
					tsEingabeAb = row.tsEingabeAb;
				} else if (tsEingabeAb !== row.tsEingabeAb) {
					tsEingabeAb = null;
				}
				if (tsEingabeBis === undefined) {
					tsEingabeBis = row.tsEingabeBis;
				} else if (tsEingabeBis !== row.tsEingabeBis) {
					tsEingabeBis = null;
				}
			}
			gruppe.tsEingabeAb = tsEingabeAb ?? null;
			gruppe.tsEingabeBis = tsEingabeBis ?? null;
			for (const colGruppe of gruppe.spalten) {
				colGruppe.gesperrt = 0;
				for (const idKlasse of gruppe.klassenzuordnungen) {
					const col = this._mapKlassenSpalte.getOrNull(idKlasse, colGruppe.name);
					if (col === null) {
						continue;
					}
					if (col.gesperrt) {
						colGruppe.gesperrt++;
					}
				}
			}
		}
	}

	/**
	 * Aktualisiert die Gruppen, die z.B. Jahrgänge zusammenfassen
	 *
	 * @param value   die Gruppierung, die aktualisiert werden soll
	 */
	private updateGruppierung(value: NotenmodulConfigManagerSperrungenGruppierung) {
		if (value === 'Keine') {
			const gruppen = new ArrayList<NotenmodulConfigManagerSperrungenGruppe>();
			gruppen.add(this._configAlleKlassen.value);
			this.updateGruppen(gruppen);
			triggerRef(this._configAlleKlassen);
		} else if (value === 'Jahrgang') {
			this.updateGruppen(this._mapJahrgangGruppe.value.values());
			triggerRef(this._mapJahrgangGruppe);
		} else {
			this.updateGruppen(this._mapAbteilungGruppe.value.values());
			triggerRef(this._mapAbteilungGruppe);
		}
	}

	/**
	 * Gibt zurück, ob es sich bei der Spalte um eine Spalte mit einer Teilleistung handelt oder nicht.
	 *
	 * @param row       die Zeile
	 * @param colname   der Spaltenname
	 *
	 * @returns true, wenn es sich um eine Teilleistungsspalte handelt
	 */
	public istTeilleistung(row: NotenmodulConfigManagerSperrungenZeile, colname: string): boolean {
		const col = this.getColumn(row, colname);
		if (col === null) {
			return false;
		}
		return (col.idTeilleistung !== null);
	}

	/**
	 * Gibt die jeweilige Spalte für die Zeile und den Spaltennamen zurück
	 *
	 * @param row       die Zeile
	 * @param colname   der Spaltenname
	 *
	 * @returns Entweder die Spalte oder null
	 */
	public getColumn(row: NotenmodulConfigManagerSperrungenZeile, colname: string): NotenmodulConfigManagerSperrungenZelle | null {
		const rowIstGruppe = 'klassenzuordnungen' in row;
		if (!rowIstGruppe) {
			return this._mapKlassenSpalte.getOrNull(row.id, colname);
		}
		if (this._gruppierung.value === 'Jahrgang') {
			return this._mapJahrgangSpalte.getOrNull(row.id, colname);
		} else if (this._gruppierung.value === 'Abteilung') {
			return this._mapAbteilungSpalte.getOrNull(row.id, colname);
		}
		return this._mapAlleKlassenSpalte.get(colname);
	}

	/**
	 * Gibt zurück, ob eine Spalte für die übergebene Zeile sperrbar ist
	 *
	 * @param row       die Zeile
	 * @param colname   der Spaltenname
	 *
	 * @returns true, wenn diese Spalte gesperrt werden kann
	 */
	public istSperrbar(row: NotenmodulConfigManagerSperrungenZeile, colname: string): boolean {
		const col = this.getColumn(row, colname);
		if (col === null) {
			return false;
		}
		return (col.idTeilleistung !== null) || (this.spaltenSperrbar.includes(colname));
	}

	/**
	 * Gibt eine Liste mit den Zeilen für eine Jahrgangsansicht zurück
	 *
	 * @returns Gibt eine Liste mit den Zeilen zurück
	 */
	private getZeilenJahrgangsGruppen(): ArrayList<NotenmodulConfigManagerSperrungenZeile> {
		const list = new ArrayList<NotenmodulConfigManagerSperrungenZeile>();
		for (const entry of this._mapJahrgangKlassen.entrySet()) {
			const idJahrgang = entry.getKey();
			const gruppe = this._mapJahrgangGruppe.value.get(idJahrgang);
			if (gruppe !== null) {
				list.add(gruppe);
			}
			if (this._showJahrgangsklassen.value.contains(idJahrgang)) {
				list.addAll(entry.getValue());
			}
		}
		return list;
	}

	/**
	 * Gibt eine Liste mit den Zeilen für eine Abteilungsansicht zurück
	 *
	 * @returns Gibt eine Liste mit den Zeilen zurück
	 */
	private getZeilenAbteilungsGruppen(): ArrayList<NotenmodulConfigManagerSperrungenZeile> {
		const list = new ArrayList<NotenmodulConfigManagerSperrungenZeile>();
		for (const entry of this._mapAbteilungKlassen.entrySet()) {
			const idAbteilung = entry.getKey();
			const gruppe = this._mapAbteilungGruppe.value.get(idAbteilung);
			if (gruppe !== null) {
				list.add(gruppe);
			}
			if (this._showAbteilungsklassen.value.contains(idAbteilung)) {
				list.addAll(entry.getValue());
			}
		}
		return list;
	}

	/**
	 * Gibt die Liste der Zeilen zurück, die Aufgrund der aktuellen Ansicht dargestellt werden.
	 * Dabei werden ggf. Zeilen für die gruppierte Darstellung erzeugt.
	 *
	 * @returns die Zeilen für die aktuelle Tabellenansicht
	 */
	public zeilen(): List<NotenmodulConfigManagerSperrungenZeile> {
		if (this._gruppierung.value === "Jahrgang") {
			return this.getZeilenJahrgangsGruppen();
		} else if (this._gruppierung.value === "Abteilung") {
			return this.getZeilenAbteilungsGruppen();
		}
		const list = new ArrayList<NotenmodulConfigManagerSperrungenZeile>();
		list.addAll(this._listKlassen);
		return list;
	}

	public zeileAlleKlassen(): NotenmodulConfigManagerSperrungenZeile {
		return this._configAlleKlassen.value;
	}

	/**
	 * Gibt die Spaltendefinition für die Darstellung in der Tabelle zurück.
	 *
	 * @returns die Spaltendefinitionen
	 */
	public get columns(): Array<GridColumn<any>> {
		const result = new Array<GridColumn<any>>();
		result.push(
			{ kuerzel: "Gruppe", name: "Gruppe", width: "15rem" },
			{ kuerzel: "Klasse", name: "Klasse", width: "5rem" },
			{ kuerzel: "EingabeVon", name: "Eingabe von", width: "15rem" },
			{ kuerzel: "EingabeBis", name: "Eingabe bis", width: "15rem" },
			{ kuerzel: "FSKlassenweise", name: "FS klassenweise", width: "5rem" }
		);
		for (const col of this.spaltenSperrbar) {
			result.push({ kuerzel: col, name: col, width: '6rem' });
		}
		for (const teilleistung of this._mapTeilleistungsarten.values()) {
			const name = teilleistung.bezeichnung ?? '—';
			result.push({ kuerzel: name, name, width: '6rem' });
		}
		return result;
	}

	/**
	 * Gibt eine Map zurück, welche angibt welche Spalten aktuell sichtbar sind und welche nicht.
	 * Dabei wird berücksichtigt, ob eine Gruppierung vorliegt oder nicht, und ob die Teilleistungen
	 * detailliert angezeigt werden sollen oder nicht.
	 *
	 * @returns die Map mit der Sichtbarkeitsinformation
	 */
	public get columnsVisible(): Map<string, boolean | null> {
		const result = new Map<string, boolean | null>();
		result.set("Gruppe", this._gruppierung.value !== "Keine");
		result.set("Klasse", true);
		result.set("Eingabe von", true);
		result.set("Eingabe bis", true);
		result.set("FS klassenweise", true);
		for (const col of this.spaltenSperrbar) {
			result.set(col, true);
		}
		for (const teilleistung of this._mapTeilleistungsarten.values()) {
			const name = teilleistung.bezeichnung ?? '—';
			result.set(name, this.zeigeTeilnoten);
		}
		return result;
	}

	/**
	 * Gibt zurück, ob es sich bei der übergeben Zeile um eine Zeile für eine
	 * Gruppierung handelt oder nicht.
	 *
	 * @param row   die Zeile mit den Informationen zur Gruppierung
	 *
	 * @returns true, wenn es sich um eine Gruppe handelt, und ansonsten false
	 */
	public istGruppe(row: NotenmodulConfigManagerSperrungenZeile): boolean {
		return 'klassenzuordnungen' in row;
	}

	/**
	 * Gibt die Bezeichnung für die Gruppe zurück, sofern es sich um eine Gruppe handelt.
	 *
	 * @param row    die Zeile mit den Informationen zur Gruppe
	 *
	 * @returns die Bezeichnung für die Gruppe
	 */
	public getGruppenBezeichnung(row: NotenmodulConfigManagerSperrungenZeile): string {
		if (!this.istGruppe(row)) {
			return '';
		}
		if (this._gruppierung.value === "Jahrgang") {
			const jahrgang = this.getJahrgang(row.id);
			return jahrgang?.kuerzelAnzeige ?? '—';
		} else if (this._gruppierung.value === "Abteilung") {
			const abteilung = this.getAbteilung(row.id);
			return abteilung?.bezeichnung ?? '—';
		}
		return '—';
	}

	/**
	 * Gibt zurück, ob die Klassen der übergebenen Gruppe angezeigt werden sollen oder nicht.
	 *
	 * @param row   die Gruppe
	 *
	 * @returns true, wenn die zugehrörigen Klassen angezeigt werden sollen
	 */
	public zeigeGruppenKlassen(row: NotenmodulConfigManagerSperrungenZeile): boolean {
		if (!this.istGruppe(row)) {
			return true;
		}
		if (this._gruppierung.value === "Jahrgang") {
			return this._showJahrgangsklassen.value.contains(row.id);
		} else if (this._gruppierung.value === "Abteilung") {
			return this._showAbteilungsklassen.value.contains(row.id);
		}
		return false;
	}

	/**
	 * Schaltet den Zustand um, ob die Klassen einer Gruppe angezeigt werden oder nicht.
	 *
	 * @param row die Gruppe
	 */
	public toggleZeigeGruppenKlassen(row: NotenmodulConfigManagerSperrungenZeile): void {
		if (!this.istGruppe(row)) {
			return;
		}
		if (this._gruppierung.value === "Jahrgang") {
			if (this._showJahrgangsklassen.value.contains(row.id)) {
				this._showJahrgangsklassen.value.remove(row.id);
			} else {
				this._showJahrgangsklassen.value.add(row.id);
			}
			triggerRef(this._showJahrgangsklassen);
		} else if (this._gruppierung.value === "Abteilung") {
			if (this._showAbteilungsklassen.value.contains(row.id)) {
				this._showAbteilungsklassen.value.remove(row.id);
			} else {
				this._showAbteilungsklassen.value.add(row.id);
			}
			triggerRef(this._showAbteilungsklassen);
		}
	}

	/**
	 * Gibt die Anzeige-Kürzel der Klasse zurück, sofern es sich nicht um eine Gruppe handelt.
	 *
	 * @param row    die Zeile mit den Informationen zur Klasse
	 *
	 * @returns das Anzeige-Kürzel für die Klasse
	 */
	public getKlassenBezeichnung(row: NotenmodulConfigManagerSperrungenZeile): string {
		if (this.istGruppe(row)) {
			return '';
		}
		const klasse = this.getKlasse(row.id);
		return klasse?.kuerzelAnzeige ?? '—';
	}

	/**
	 * Gibt zurück, ob eine klassenweise Fehlstundeneingabe vorliegt oder nicht.
	 *
	 * @param row   die Zeile des Konfigurationseintrages
	 *
	 * @returns true, wenn eine klassenweise Fehlstundeneingabe vorliegt, und ansonsten false
	 */
	public hatFehlstundeneingabeKlassenweise(row: NotenmodulConfigManagerSperrungenZeile): boolean {
		return (typeof row.istFehlstundenEingabeKlassenweise === "number") ? (row.istFehlstundenEingabeKlassenweise > 0) : row.istFehlstundenEingabeKlassenweise;
	}

	/**
	 * Gibt zurück, ob teilweise eine klassenweise Fehlstundeneingabe vorliegt oder nicht.
	 *
	 * @param row   die Zeile des Konfigurationseintrages
	 *
	 * @returns true, wenn eine klassenweise Fehlstundeneingabe vorliegt, und ansonsten false
	 */
	public hatFehlstundeneingabeKlassenweiseTeilweise(row: NotenmodulConfigManagerSperrungenZeile): boolean {
		if (typeof row.istFehlstundenEingabeKlassenweise !== "number") {
			return false;
		}
		let max = 1;
		if (this._gruppierung.value === 'Jahrgang') {
			const listJahrgangsklassen = this._mapJahrgangKlassen.get(row.id);
			if (listJahrgangsklassen !== null) {
				max = listJahrgangsklassen.size();
			}
		} else if (this._gruppierung.value === 'Abteilung') {
			const listAbteilungKlassen = this._mapAbteilungKlassen.get(row.id);
			if (listAbteilungKlassen !== null) {
				max = listAbteilungKlassen.size();
			}
		} else {
			max = this._listKlassen.size();
		}
		return (row.istFehlstundenEingabeKlassenweise > 0) && (row.istFehlstundenEingabeKlassenweise < max);
	}

	/**
	 * Setzt die Default-Konfiguration bei der die Noteneingabe gesperrt ist.
	 */
	public async setConfigDefault() {
		for (const klasse of this._listKlassen) {
			klasse.tsEingabeAb = null;
			klasse.tsEingabeBis = null;
			for (const col of klasse.spalten) {
				col.gesperrt = true;
			}
		}
		this.updateGruppierung(this._gruppierung.value);
		await this.writeConfig();
	}

	/**
	 * Setzt die Konfiguration bei die Noteneingabe komplett freigeschaltet ist.
	 */
	public async setConfigAllowAll() {
		for (const klasse of this._listKlassen) {
			klasse.tsEingabeAb = null;
			klasse.tsEingabeBis = null;
			for (const col of klasse.spalten) {
				col.gesperrt = false;
			}
		}
		this.updateGruppierung(this._gruppierung.value);
		await this.writeConfig();
	}

	/**
	 * Schaltet den Wert bei einer Jahrgangsgruppe um, ob eine klassenweise Fehlstundeneingabe vorliegt oder nicht.
	 *
	 * @param row   die Zeile, die angeklickt wurde
	 */
	private async toggleFehlstundeneingabeKlassenweiseJahrgang(row: NotenmodulConfigManagerSperrungenGruppe) {
		const klassen = this._mapJahrgangKlassen.get(row.id);
		await this.toggleFehlstundeneingabeKlassenweiseGruppe(row, klassen);
		triggerRef(this._mapJahrgangGruppe);
	}

	/**
	 * Schaltet den Wert bei einer Abteilungsgruppe um, ob eine klassenweise Fehlstundeneingabe vorliegt oder nicht.
	 *
	 * @param row   die Zeile, die angeklickt wurde
	 */
	private async toggleFehlstundeneingabeKlassenweiseAbteilung(row: NotenmodulConfigManagerSperrungenGruppe) {
		const klassen = this._mapAbteilungKlassen.get(row.id);
		await this.toggleFehlstundeneingabeKlassenweiseGruppe(row, klassen);
		triggerRef(this._mapAbteilungGruppe);
	}

	/**
	 * Schaltet den Wert bei der Gruppe für alle Klassen um, ob eine klassenweise Fehlstundeneingabe vorliegt oder nicht.
	 *
	 * @param row   die Zeile, die angeklickt wurde
	 */
	private async toggleFehlstundeneingabeKlassenweiseAlleKlassen(row: NotenmodulConfigManagerSperrungenGruppe) {
		const klassen = this._listKlassen;
		await this.toggleFehlstundeneingabeKlassenweiseGruppe(row, klassen);
		triggerRef(this._configAlleKlassen);
	}

	/**
	 * Schaltet den Wert für Jahrgänge oder Abteilungen oder alle Klassen um, ob eine klassenweise Fehlstundeneingabe vorliegt oder nicht.
	 *
	 * @param row       die Zeile, die angeklickt wurde
	 * @param klassen   die Klassen der jeweiligen Gruppe
	 */
	private async toggleFehlstundeneingabeKlassenweiseGruppe(row: NotenmodulConfigManagerSperrungenGruppe, klassen: List<ENMConfigKlasse> | null) {
		if (klassen === null) {
			return;
		}
		const newState = (row.istFehlstundenEingabeKlassenweise === 0);
		for (const klasse of klassen) {
			const rowKlasse = this._mapConfigKlassen.get(klasse.id);
			if (rowKlasse === null) {
				continue;
			}
			rowKlasse.istFehlstundenEingabeKlassenweise = newState;
		}
		row.istFehlstundenEingabeKlassenweise = newState ? klassen.size() : 0;
		await this.writeConfig();
	}

	/**
	 * Schaltet den Wert bei einer einzelnen Klasse um, ob eine klassenweise Fehlstundeneingabe vorliegt oder nicht.
	 *
	 * @param row   die Zeile, die angeklickt wurde
	 */
	private async toggleFehlstundeneingabeKlassenweiseKlasse(row: ENMConfigKlasse) {
		row.istFehlstundenEingabeKlassenweise = !row.istFehlstundenEingabeKlassenweise;
		// Aktualisiere ggf. beim Jahrgang Counter für die Anzahl Sperrungen...
		const gruppeJahrgang = this._mapKlassenToJahrgang.get(row.id);
		if (gruppeJahrgang !== null) {
			if (row.istFehlstundenEingabeKlassenweise) {
				gruppeJahrgang.istFehlstundenEingabeKlassenweise++;
			} else {
				gruppeJahrgang.istFehlstundenEingabeKlassenweise--;
			}
			triggerRef(this._mapJahrgangGruppe);
		}
		// Aktualisiere ggf. bei der Abteilung den Counter für die Anzahl Sperrungen...
		const gruppeAbteilung = this._mapKlassenToAbteilung.get(row.id);
		if (gruppeAbteilung !== null) {
			if (row.istFehlstundenEingabeKlassenweise) {
				gruppeAbteilung.istFehlstundenEingabeKlassenweise++;
			} else {
				gruppeAbteilung.istFehlstundenEingabeKlassenweise--;
			}
			triggerRef(this._mapAbteilungGruppe);
		}
		// Aktualisiere bei der Gruppe für Alle den Counter für die Anzahl Sperrungen...
		const gruppeAlleKlassen = this._configAlleKlassen.value;
		if (row.istFehlstundenEingabeKlassenweise) {
			gruppeAlleKlassen.istFehlstundenEingabeKlassenweise++;
		} else {
			gruppeAlleKlassen.istFehlstundenEingabeKlassenweise--;
		}
		triggerRef(this._configAlleKlassen);
		await this.writeConfig();
	}

	/**
	 * Schaltet den Wert für eine einzelne Klasse oder einer Gruppe um, ob eine klassenweise Fehlstundeneingabe vorliegt oder nicht.
	 *
	 * @param row   die Zeile, die angeklickt wurde
	 */
	public async toggleFehlstundeneingabeKlassenweise(row: NotenmodulConfigManagerSperrungenZeile) {
		const rowIstGruppe = 'klassenzuordnungen' in row;
		if (!rowIstGruppe) {
			await this.toggleFehlstundeneingabeKlassenweiseKlasse(row);
		} else if (this._gruppierung.value === 'Jahrgang') {
			await this.toggleFehlstundeneingabeKlassenweiseJahrgang(row);
		} else if (this._gruppierung.value === 'Abteilung') {
			await this.toggleFehlstundeneingabeKlassenweiseAbteilung(row);
		} else {
			await this.toggleFehlstundeneingabeKlassenweiseAlleKlassen(row);
		}
	}


	/**
	 * Aktualisiert den Beginn oder das Ende der Noteneingabe einer Jahrgangs-Gruppe.
	 *
	 * @param row         die Zeile, die angeklickt wurde
	 * @param isoDate     das Datum
	 * @param istBeginn   gibt an, ob der Beginn oder das Ende gesetzt wird
	 */
	private async setzeDatumNoteneingabeJahrgang(row: NotenmodulConfigManagerSperrungenGruppe, isoDate: string | null, istBeginn: boolean) {
		const klassen = this._mapJahrgangKlassen.get(row.id);
		await this.setzeDatumNoteneingabeGruppe(row, klassen, isoDate, istBeginn);
		triggerRef(this._mapJahrgangGruppe);
	}

	/**
	 * Aktualisiert den Beginn oder das Ende der Noteneingabe einer Abteilungs-Gruppe.
	 *
	 * @param row         die Zeile, die angeklickt wurde
	 * @param isoDate     das Datum
	 * @param istBeginn   gibt an, ob der Beginn oder das Ende gesetzt wird
	 */
	private async setzeDatumNoteneingabeAbteilung(row: NotenmodulConfigManagerSperrungenGruppe, isoDate: string | null, istBeginn: boolean) {
		const klassen = this._mapAbteilungKlassen.get(row.id);
		await this.setzeDatumNoteneingabeGruppe(row, klassen, isoDate, istBeginn);
		triggerRef(this._mapAbteilungGruppe);
	}

	/**
	 * Aktualisiert den Beginn oder das Ende der Noteneingabe der Gruppe aller Klassen.
	 *
	 * @param row         die Zeile, die angeklickt wurde
	 * @param isoDate     das Datum
	 * @param istBeginn   gibt an, ob der Beginn oder das Ende gesetzt wird
	 */
	private async setzeDatumNoteneingabeAlleKlassen(row: NotenmodulConfigManagerSperrungenGruppe, isoDate: string | null, istBeginn: boolean) {
		const klassen = this._listKlassen;
		await this.setzeDatumNoteneingabeGruppe(row, klassen, isoDate, istBeginn);
		triggerRef(this._configAlleKlassen);
	}

	/**
	 * Aktualisiert den Beginn oder das Ende der Noteneingabe einer Gruppe.
	 *
	 * @param row         die Zeile, die angeklickt wurde
	 * @param isoDate     das Datum
	 * @param istBeginn   gibt an, ob der Beginn oder das Ende gesetzt wird
	 */
	private async setzeDatumNoteneingabeGruppe(row: NotenmodulConfigManagerSperrungenGruppe, klassen: List<ENMConfigKlasse> | null,
		isoDate: string | null, istBeginn: boolean) {
		if (klassen === null) {
			return;
		}
		if (istBeginn) {
			for (const klasse of klassen) {
				const rowKlasse = this._mapConfigKlassen.get(klasse.id);
				if (rowKlasse === null) {
					continue;
				}
				rowKlasse.tsEingabeAb = isoDate;
			}
			row.tsEingabeAb = isoDate;
		} else {
			for (const klasse of klassen) {
				const rowKlasse = this._mapConfigKlassen.get(klasse.id);
				if (rowKlasse === null) {
					continue;
				}
				rowKlasse.tsEingabeBis = isoDate;
			}
			row.tsEingabeBis = isoDate;
		}
		await this.writeConfig();
	}


	/**
	 * Aktualisiert den Beginn oder das Ende der Noteneingabe einer Gruppe aufgrund einer Änderungen bei einer einzelnen
	 * Klasse aus der Gruppe.
	 *
	 * @param row         die Zeile, die angeklickt wurde
	 * @param isoDate     das Datum
	 * @param istBeginn   gibt an, ob der Beginn oder das Ende gesetzt wird
	 */
	private setzeDatumNoteneingabeKlasseGruppenaktualisierung(row: ENMConfigKlasse, gruppe: NotenmodulConfigManagerSperrungenGruppe,
		klassen: List<ENMConfigKlasse> | null, isoDate: string | null, istBeginn: boolean) {
		if ((klassen === null) || (istBeginn && (row.tsEingabeAb === gruppe.tsEingabeAb)) || (!istBeginn && (row.tsEingabeBis === gruppe.tsEingabeBis))) {
			return;
		}
		if (istBeginn) {
			let newState = row.tsEingabeAb;
			for (const klasse of klassen) {
				const rowKlasse = this._mapConfigKlassen.get(klasse.id);
				if ((rowKlasse === null) || (rowKlasse.tsEingabeAb !== row.tsEingabeAb)) {
					newState = null;
					break;
				}
			}
			gruppe.tsEingabeAb = newState;
		} else {
			let newState = row.tsEingabeBis;
			for (const klasse of klassen) {
				const rowKlasse = this._mapConfigKlassen.get(klasse.id);
				if ((rowKlasse === null) || (rowKlasse.tsEingabeBis !== row.tsEingabeBis)) {
					newState = null;
					break;
				}
			}
			gruppe.tsEingabeBis = newState;
		}
	}

	/**
	 * Setzt den Beginn oder das Ende der Noteneingabe bei einer einzelnen Klasse.
	 *
	 * @param row         die Zeile, die angeklickt wurde
	 * @param isoDate     das Datum
	 * @param istBeginn   gibt an, ob der Beginn oder das Ende gesetzt wird
	 */
	private async setzeDatumNoteneingabeKlasse(row: ENMConfigKlasse, isoDate: string | null, istBeginn: boolean) {
		if (istBeginn) {
			row.tsEingabeAb = isoDate;
		} else {
			row.tsEingabeBis = isoDate;
		}
		// Aktualisiere ggf. beim Jahrgang das Datum
		const gruppeJahrgang = this._mapKlassenToJahrgang.get(row.id);
		if (gruppeJahrgang !== null) {
			this.setzeDatumNoteneingabeKlasseGruppenaktualisierung(row, gruppeJahrgang, this._mapJahrgangKlassen.get(row.id), isoDate, istBeginn);
			triggerRef(this._mapJahrgangGruppe);
		}
		// Aktualisiere ggf. bei der Abteilung den Counter für die Anzahl Sperrungen...
		const gruppeAbteilung = this._mapKlassenToAbteilung.get(row.id);
		if (gruppeAbteilung !== null) {
			this.setzeDatumNoteneingabeKlasseGruppenaktualisierung(row, gruppeAbteilung, this._mapAbteilungKlassen.get(row.id), isoDate, istBeginn);
			triggerRef(this._mapAbteilungGruppe);
		}
		// Aktualisiere bei der Gruppe für Alle den Counter für die Anzahl Sperrungen...
		const gruppeAlleKlassen = this._configAlleKlassen.value;
		this.setzeDatumNoteneingabeKlasseGruppenaktualisierung(row, gruppeAlleKlassen, this._listKlassen, isoDate, istBeginn);
		triggerRef(this._configAlleKlassen);
		await this.writeConfig();
	}


	/**
	 * Setzt den Beginn oder das Ende der Noteneingabe bei einer Klasse oder einer Gruppe.
	 *
	 * @param row         die Zeile, die angeklickt wurde
	 * @param isoDate     das Datum
	 * @param istBeginn   gibt an, ob der Beginn oder das Ende gesetzt wird
	 */
	public async setzeDatumNoteneingabe(row: NotenmodulConfigManagerSperrungenZeile, value: string | null, istBeginn: boolean) {
		const date = (value === null) || (JavaString.isBlank(value)) ? null : (value.replace('T', ' ') + ":00.0");
		const rowIstGruppe = 'klassenzuordnungen' in row;
		if (!rowIstGruppe) {
			await this.setzeDatumNoteneingabeKlasse(row, date, istBeginn);
		} else if (this._gruppierung.value === 'Jahrgang') {
			await this.setzeDatumNoteneingabeJahrgang(row, date, istBeginn);
		} else if (this._gruppierung.value === 'Abteilung') {
			await this.setzeDatumNoteneingabeAbteilung(row, date, istBeginn);
		} else {
			await this.setzeDatumNoteneingabeAlleKlassen(row, date, istBeginn);
		}
	}

	/**
	 * Prüfe, ob eine Noteneingabe bei der übergegeben Zeitangabe für den Beginn bzw. das Ende des Noteneingabezeitraumes
	 * zulässig ist oder nicht.
	 *
	 * @param datetime    die Zeitangabe für den Beginn oder das Ende des Noteneingabezeitraumes
	 * @param istBeginn   gibt an, ob die Zeitangabe den Beginn angibt
	 *
	 * @returns true, wenn die Noteneingabe gesperrt, d.h. nicht erlaubt ist, und ansonsten false
	 */
	public istNoteneingabeZeitlichGesperrt(datetime: string | null, istBeginn: boolean): ComputedRef<boolean> {
		return computed(() => {
			if (datetime === null) {
				return false;
			}
			if ((istBeginn && (this.now(this.datum.value) < datetime)) || (!istBeginn && (this.now(this.datum.value) > datetime))) {
				return true;
			}
			return false;
		});
	}

	public istNoteneingabeZeitlichUnmoeglich(datetime: string | null, datetime2: string | null): boolean {
		if (datetime === null || datetime2 === null) {
			return false;
		}
		const a = new Date(datetime);
		const b = new Date(datetime2);
		return a > b;
	}

	/**
	 * Gibt zurück, ob bei der Spalte eine Sperrung vorliegt oder nicht.
	 *
	 * @param colname   die Spalte
	 *
	 * @returns true, wenn eine Sperrung vorliegt und ansonsten false
	 */
	public hatSperrung(row: NotenmodulConfigManagerSperrungenZeile, colname: string): boolean {
		const col = this.getColumn(row, colname);
		if (col === null) {
			return true;
		}
		return (typeof col.gesperrt === "number") ? (col.gesperrt > 0) : col.gesperrt;
	}

	/**
	 * Gibt zurück, ob bei der Spalte eine Teilsperrung vorliegt oder nicht.
	 *
	 * @param colname   die Spalte
	 *
	 * @returns true, wenn eine Sperrung vorliegt und ansonsten false
	 */
	public hatTeilsperrung(row: NotenmodulConfigManagerSperrungenZeile, colname: string): boolean {
		const col = this.getColumn(row, colname);
		if ((col === null) || (typeof col.gesperrt !== "number") || (!this.istGruppe(row))) {
			return false;
		}
		let max = 1;
		if (this._gruppierung.value === 'Jahrgang') {
			const listJahrgangsklassen = this._mapJahrgangKlassen.get(row.id);
			if (listJahrgangsklassen !== null) {
				max = listJahrgangsklassen.size();
			}
		} else if (this._gruppierung.value === 'Abteilung') {
			const listAbteilungKlassen = this._mapAbteilungKlassen.get(row.id);
			if (listAbteilungKlassen !== null) {
				max = listAbteilungKlassen.size();
			}
		} else {
			max = this._listKlassen.size();
		}
		return (col.gesperrt > 0) && (col.gesperrt < max);
	}

	/**
	 * Schaltet den Wert für die Sperrung bei einer Jahrgangsgruppe um.
	 *
	 * @param row   die Zeile, die angeklickt wurde
	 * @param col   die Spalte, die angeklickt wurde
	 */
	private async toggleSperrungJahrgang(row: NotenmodulConfigManagerSperrungenGruppe, col: NotenmodulConfigManagerSperrungenGruppeSpalte) {
		const klassen = this._mapJahrgangKlassen.get(row.id);
		await this.toggleSperrungGruppe(klassen, col);
		triggerRef(this._mapJahrgangGruppe);
	}

	/**
	 * Schaltet den Wert für die Sperrung bei einer Abteilungsgruppe um.
	 *
	 * @param row   die Zeile, die angeklickt wurde
	 * @param col   die Spalte, die angeklickt wurde
	 */
	private async toggleSperrungAbteilung(row: NotenmodulConfigManagerSperrungenGruppe, col: NotenmodulConfigManagerSperrungenGruppeSpalte) {
		const klassen = this._mapAbteilungKlassen.get(row.id);
		await this.toggleSperrungGruppe(klassen, col);
		triggerRef(this._mapAbteilungGruppe);
	}

	/**
	 * Schaltet den Wert für die Sperrung bei einer Abteilungsgruppe um.
	 *
	 * @param row   die Zeile, die angeklickt wurde
	 * @param col   die Spalte, die angeklickt wurde
	 */
	private async toggleSperrungAlleKlassen(row: NotenmodulConfigManagerSperrungenGruppe, col: NotenmodulConfigManagerSperrungenGruppeSpalte) {
		const klassen = this._listKlassen;
		await this.toggleSperrungGruppe(klassen, col);
		triggerRef(this._configAlleKlassen);
	}

	/**
	 * Schaltet den Wert der Sperrung für Jahrgänge oder Abteilungen
	 * @param klassen	die Klassen der jeweiligen Gruppe
	 * @param col   	die Spalte, die angeklickt wurde
	 */
	private async toggleSperrungGruppe(klassen: List<ENMConfigKlasse> | null, col: NotenmodulConfigManagerSperrungenGruppeSpalte) {
		if (klassen === null) {
			return;
		}
		const newState = col.gesperrt === 0;
		for (const klasse of klassen) {
			const colKlasse = this._mapKlassenSpalte.getOrException(klasse.id, col.name);
			colKlasse.gesperrt = newState;
		}
		col.gesperrt = newState ? klassen.size() : 0;
		await this.writeConfig();
	}

	/**
	 * Schaltet den Wert für die Sperrung bei einer einzelnen Klasse um.
	 *
	 * @param col       die Spalte, die angeklickt wurde
	 */
	private async toggleSperrungKlasse(row: ENMConfigKlasse, col: ENMConfigKlasseSpalte) {
		col.gesperrt = !col.gesperrt;
		// Aktualisiere ggf. beim Jahrgang Counter für die Anzahl Sperrungen...
		const gruppeJahrgang = this._mapSpaltenKlassenToJahrgang.getOrNull(row.id, col.name);
		if (gruppeJahrgang !== null) {
			if (col.gesperrt) {
				gruppeJahrgang.gesperrt++;
			} else {
				gruppeJahrgang.gesperrt--;
			}
			triggerRef(this._mapJahrgangGruppe);
		}
		// Aktualisiere ggf. bei der Abteilung den Counter für die Anzahl Sperrungen...
		const gruppeAbteilung = this._mapSpaltenKlassenToAbteilung.getOrNull(row.id, col.name);
		if (gruppeAbteilung !== null) {
			if (col.gesperrt) {
				gruppeAbteilung.gesperrt++;
			} else {
				gruppeAbteilung.gesperrt--;
			}
			triggerRef(this._mapAbteilungGruppe);
		}
		// Aktualisiere ggf. bei der Gruppe für Alle den Counter für die Anzahl Sperrungen...
		const gruppeAlleKlassen = this._mapAlleKlassenSpalte.get(col.name);
		if (gruppeAlleKlassen !== null) {
			if (col.gesperrt) {
				gruppeAlleKlassen.gesperrt++;
			} else {
				gruppeAlleKlassen.gesperrt--;
			}
			triggerRef(this._configAlleKlassen);
		}
		await this.writeConfig();
	}

	/**
	 * Schaltet die Werte für die Sperrung für eine einzelne Klasse oder einer Gruppe um.
	 *
	 * @param row       die Zeile, die angeklickt wurde
	 * @param colname   der Name der Spalten die angeklickt wurde
	 */
	public async toggleSperrung(row: NotenmodulConfigManagerSperrungenZeile, colname: string) {
		const col = this.getColumn(row, colname);
		if (col === null) {
			return;
		}
		const rowIstGruppe = 'klassenzuordnungen' in row;
		const colIstGruppe = (typeof col.gesperrt === "number");
		if (!rowIstGruppe && !colIstGruppe) {
			await this.toggleSperrungKlasse(row, col);
		} else if (rowIstGruppe && colIstGruppe) {
			if (this._gruppierung.value === 'Jahrgang') {
				await this.toggleSperrungJahrgang(row, col);
			} else if (this._gruppierung.value === 'Abteilung') {
				await this.toggleSperrungAbteilung(row, col);
			} else {
				await this.toggleSperrungAlleKlassen(row, col);
			}
		}
	}

}
