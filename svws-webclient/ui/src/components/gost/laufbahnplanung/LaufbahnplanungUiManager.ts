import { computed, ref } from "vue";
import type { AbiturFachbelegungHalbjahr, GostJahrgangsdaten, GostSchuelerFachwahl, JavaMap, Sprachbelegung} from "../../../../../core/src";
import { GostAbiturFach, GostFachbereich, GostFachUtils, GostKursart, Note, Fach, Fachgruppe, HashMap2D, SprachendatenUtils,
	ArrayList, GostHalbjahr, HashMap, type AbiturdatenManager, type GostFach, type List } from "../../../../../core/src";
import type { Config } from "~/utils/Config";

/**
 * Laufbahnplanung Oberstufe: Ein Manager für die Verwaltung von UI-Spezifischen Funktionen
 * auf die aktuellen Laufbahn-Beratungsdaten eines Schülers.
 */
export class LaufbahnplanungUiManager {

	/** Der Manager für die Abiturdaten, welcher auch die Belegprüfung durchführt. */
	private manager: () => AbiturdatenManager;

	/** Die Konfiguration des Clients */
	private config: () => Config;

	/** Die Daten zum Abitur-Jahrgang */
	private jahrgang: () => GostJahrgangsdaten;

	/** Der Setter zum Setzen der Fachwahlen */
	private setWahl: (fachID: number, wahl: GostSchuelerFachwahl) => Promise<void>;

	/** Gibt an, ob bei den angebotenen Sprachfächern eine Sprachenfolge berücksichtigt werden soll oder nicht */
	private _ignoriereSprachenfolge : boolean = false;

	/** Gibt an, ob bei den einzelnen Fachbelegungen immer Noten bei den Leistungsdaten angenommen werden sollen */
	private _belegungHatImmerNoten : boolean = false;

	/** Computed Property: Ein Array mit der Anzahl von anrechenbaren Kursen in den einzelnen Halbjahren */
	private _anrechenbareKurse = computed<number[]>(() => this.manager().getAnrechenbareKurse());

	/** Computed Property: Die Anzahl der anrechenbaren Kurse für Block I des Abiturs */
	public _summeAnrechenbareKurse = computed<number>(() => this.manager().getAnrechenbareKurseBlockI());

	/** Computed Property: Die Wochenstunden, welche von dem Schüler in den einzelnen Halbjahren
	 * der gymnasialen Oberstufe für das Abitur relevant belegt wurden. */
	public _wochenstunden = computed<number[]>(() => this.manager().getWochenstunden());

	/** Computed Property: Die Summe der Jahres-Wochenstunden zurück. Das bedeutet die Summe der
	 * Durchschnitte der drei Jahre der gym. Oberstufe. */
	public _wochenstundenJahressumme = computed<number>(() => this._wochenstunden.value.reduce((p, c) => p + c, 0) / 2);

	/** Computed Property: Die durchschnittlichen Wochenstunden in den Halbjahren der Einführungsphase */
	public _wochenstundenDurchschnittEF = computed<number>(() => this.manager().getWochenstundenEinfuehrungsphase() / 2);

	/** Computed Property: Die durchschnittlichen Wochenstunden in den Halbjahren der Qualifikationsphase */
	public _wochenstundenDurchschnittQ = computed<number>(() => this.manager().getWochenstundenQualifikationsphase() / 4);

	/** Die Konfiguration, ob alle Fächer oder nur ein Teil angezeigt werden soll */
	public _faecherAnzeigen = ref<'alle' | 'nur_waehlbare' | 'nur_gewaehlt'>('alle');

	/** Der Eingabemodus */
	public _modus = ref<'manuell' | 'normal' | 'hochschreiben'>('normal');


	/**
	 * Erstellt einen neuen UI-Manager auf Basis des übergebenen Abiturdaten-Managers,
	 * welcher die Belegprüfung durchführt.
	 *
	 * @param manager                  der Abiturdaten-Manager zur Durchführung der Belegprüfung
	 * @param config                   die Konfiguration des Clients
	 * @param ignoriereSprachenfolge   gibt an, ob bei den angebotenen Sprachfächern eine Sprachenfolge berücksichtigt werden soll oder nicht
	 * @param belegungHatImmerNoten    gibt an, ob bei den einzelnen Fachbelegungen immer Noten bei den Leistungsdaten angenommen werden sollen
	 */
	public constructor(manager: () => AbiturdatenManager, config: () => Config, jahrgang: () => GostJahrgangsdaten,
		setWahl: (fachID: number, wahl: GostSchuelerFachwahl) => Promise<void>, ignoriereSprachenfolge : boolean = false,
		belegungHatImmerNoten : boolean = false) {
		this.manager = manager;
		this.config = config;
		this.jahrgang = jahrgang;
		this.setWahl = setWahl;
		this._ignoriereSprachenfolge = ignoriereSprachenfolge;
		this._belegungHatImmerNoten = belegungHatImmerNoten;
		// Lese aus der Konfiguration aus, ob alle Fächer oder nur ein Teil angezeigt werden soll
		const wert = this.config().getValue("app.schueler.laufbahnplanung.faecher.anzeigen");
		if ((wert === 'alle') || (wert === 'nur_waehlbare') || (wert === 'nur_gewaehlt')) {
			this._faecherAnzeigen.value = wert;
		} else {
			void this.config().setValue("app.schueler.laufbahnplanung.faecher.anzeigen", 'alle');
			this._faecherAnzeigen.value = 'alle';
		}
		// Lese den Eingabemodus aus der Konfiguration aus
		const modus = this.config().getValue("app.schueler.laufbahnplanung.modus");
		if ((modus === 'manuell') || (modus === 'normal') || (modus === 'hochschreiben')) {
			this._modus.value = modus;
		} else {
			void this.config().setValue("app.schueler.laufbahnplanung.modus", 'normal');
			this._modus.value = 'normal';
		}
	}


	/**
	 * Gibt zurück, ob bei den angebotenen Sprachfächern eine Sprachenfolge berücksichtigt werden soll oder nicht
	 */
	public get ignoriereSprachenfolge() : boolean {
		return this._ignoriereSprachenfolge;
	}

	/*
	 * Gibt zurück, ob bei den einzelnen Fachbelegungen immer Noten bei den Leistungsdaten angenommen werden sollen
	 */
	public get belegungHatImmerNoten() : boolean {
		return this._belegungHatImmerNoten;
	}

	/**
	 * Das aktuelle Halbjahr der Oberstufe des Abiturjahrgangs oder null, wenn der Jahrgang aktuell nicht zur Oberstufe gehört.
	 */
	private _aktuellesHalbjahr = computed<GostHalbjahr | null>(() => GostHalbjahr.fromJahrgangUndHalbjahr(this.jahrgang().jahrgang, this.jahrgang().halbjahr));

	/**
	 * Gibt das aktuelle Halbjahr der Oberstufe des Abiturjahrgangs zurück oder null, wenn der Jahrgang aktuell nicht zur Oberstufe gehört.
	 */
	public get aktuellesHalbjahr() : GostHalbjahr | null {
		return this._aktuellesHalbjahr.value;
	}

	/**
	 * Gibt an, ob das übergebene Halbjahr das aktuelle Halbjahr ist oder bereits in der Vergangenheit liegt.
	 *
	 * @param halbjahr   das Halbjahr
	 *
	 * @returns true, wenn das übergebene Halbjahr das aktuelle Halbjahr ist oder bereits in der Vergangenheit liegt, und ansonsten false
	 */
	public istAktuellOderVergangen(halbjahr: GostHalbjahr) {
		return (this.aktuellesHalbjahr !== null) && (this.aktuellesHalbjahr.id >= halbjahr.id);
	}


	/**
	 * Gibt die Anzahl der Kurs für das übergebene Halbjahr zurück.
	 */
	public getAnrechenbareKurse(hj : GostHalbjahr) : number {
		return this._anrechenbareKurse.value[hj.id];
	}

	/**
	 * Gibt die CSS-Klasse zur Einstufung zurück, ob die Anzahl der Kurse genügt.
	 *
	 * @param hj   das Halbjahr der gymnasialen Oberstufe
	 *
	 * @returns die CSS-Klasse zur Einstufung
	 */
	public getAnrechenbareKurseCSS(hj : GostHalbjahr) : string {
		const kurse = this.getAnrechenbareKurse(hj);
		if (hj.istEinfuehrungsphase()) {
			if (kurse < 10)
				return 'svws-ergebnis--not-enough';
			if ((kurse > 9) && (kurse < 11))
				return 'svws-ergebnis--low';
			if ((kurse > 10) && (kurse < 13))
				return 'svws-ergebnis--good';
			return 'svws-ergebnis--more';
		}
		if (kurse < 9)
			return 'svws-ergebnis--not-enough';
		if ((kurse > 8) && (kurse < 10))
			return 'svws-ergebnis--low';
		if ((kurse > 9) && (kurse < 12))
			return 'svws-ergebnis--good';
		return 'svws-ergebnis--more';
	}

	/**
	 * Gibt die Anzahl der anrechenbaren Kurse für Block I des Abiturs zurück.
	 */
	public get summeAnrechenbareKurse() : number {
		return this._summeAnrechenbareKurse.value;
	}

	/**
	 * Gibt die CSS-Klasse zur Einstufung zurück, ob die Anzahl der anrechenbaren Kurse für Block I des Abiturs genügt.
	 *
	 * @param hj   das Halbjahr der gymnasialen Oberstufe
	 *
	 * @returns die CSS-Klasse zur Einstufung
	 */
	public getSummeAnrechenbareKurseCSS() : string {
		if (this.summeAnrechenbareKurse < 38)
			return 'svws-ergebnis--not-enough';
		if ((this.summeAnrechenbareKurse > 37) && (this.summeAnrechenbareKurse < 40))
			return 'svws-ergebnis--low';
		if ((this.summeAnrechenbareKurse > 39) && (this.summeAnrechenbareKurse < 43))
			return 'svws-ergebnis--good';
		return 'svws-ergebnis--more';
	}

	/**
	 * Gibt die Wochenstunden für das übergebene Halbjahr zurück, welche von dem Schüler in den einzelnen Halbjahren
	 * der gymnasialen Oberstufe für das Abitur relevant belegt wurden.
	 */
	public getWochenstunden(hj : GostHalbjahr) : number {
		return this._wochenstunden.value[hj.id];
	}

	/**
	 * Gibt die CSS-Klasse zur Einstufung zurück, ob die Anzahl der Wochenstunden für das angegebene Halbjahr genügt.
	 *
	 * @param hj   das Halbjahr der gymnasialen Oberstufe
	 *
	 * @returns die CSS-Klasse zur Einstufung
	 */
	public getWochenstundenCSS(hj : GostHalbjahr) : string {
		const wst = this.getWochenstunden(hj);
		if (wst < 30)
			return 'svws-ergebnis--not-enough';
		if ((wst >= 30) && (wst < 33))
			return 'svws-ergebnis--low';
		if ((wst >= 33) && (wst < 37))
			return 'svws-ergebnis--good';
		return 'svws-ergebnis--more';
	}

	/**
	 * Gibt die Summe der Jahres-Wochenstunden zurück. Das bedeutet die Summe der
	 * Durchschnitte der drei Jahre der gym. Oberstufe.
	 */
	public get wochenstundenJahressumme() : number {
		return this._wochenstundenJahressumme.value;
	}

	/**
	 * Gibt die CSS-Klasse zur Einstufung zurück, ob die Summe der Jahres-Wochenstunden genügt.
	 *
	 * @returns die CSS-Klasse zur Einstufung
	 */
	public getWochenstundenJahressummeCSS() : string {
		if (this.wochenstundenJahressumme < 100)
			return 'svws-ergebnis--not-enough';
		if ((this.wochenstundenJahressumme >= 100) && (this.wochenstundenJahressumme < 101))
			return 'svws-ergebnis--low';
		if ((this.wochenstundenJahressumme >= 101) && (this.wochenstundenJahressumme <= 106))
			return 'svws-ergebnis--good';
		return 'svws-ergebnis--more';
	}

	/**
	 * Gibt die CSS-Klasse zur Einstufung zurück, ob der Halbjahresdurchschnitt der Wochenstunden genügt.
	 *
	 * @param wst   die Wochenstunden
	 *
	 * @returns die CSS-Klasse zur Einstufung
	 */
	private getWochenstundenDurchschnittCSS(wst: number) : string {
		if (wst < 34)
			return 'svws-ergebnis--not-enough';
		if ((wst >= 34) && (wst < 37))
			return 'svws-ergebnis--good';
		return 'svws-ergebnis--more';
	}

	/**
	 * Gibt die durchschnittlichen Wochenstunden in den Halbjahren der Einführungsphase zurück.
	 */
	public get wochenstundenDurchschnittEF() : number {
		return this._wochenstundenDurchschnittEF.value;
	}

	/**
	 * Gibt die CSS-Klasse zur Einstufung zurück, ob die durchschnittlichen Wochenstunden in den Halbjahren
	 * der Einführungsphase genügt.
	 *
	 * @returns die CSS-Klasse zur Einstufung
	 */
	public getWochenstundenDurchschnittEFCSS() : string {
		return this.getWochenstundenDurchschnittCSS(this.wochenstundenDurchschnittEF);
	}

	/**
	 * Gibt die durchschnittlichen Wochenstunden in den Halbjahren der Qualifikationsphase zurück.
	 */
	public get wochenstundenDurchschnittQ() : number {
		return this._wochenstundenDurchschnittQ.value;
	}

	/**
	 * Gibt die CSS-Klasse zur Einstufung zurück, ob die durchschnittlichen Wochenstunden in den Halbjahren
	 * der Qualifikationsphase genügt.
	 *
	 * @returns die CSS-Klasse zur Einstufung
	 */
	public getWochenstundenDurchschnittQCSS() : string {
		return this.getWochenstundenDurchschnittCSS(this.wochenstundenDurchschnittQ);
	}


	/**
	 * Gibt den konfigurierten Eingabemodus für die Laufbahnplanung zurück
	 */
	get modus(): 'manuell' | 'normal' | 'hochschreiben' {
		return this._modus.value;
	}

	/**
	 * Setzt den Eingabemodus in der Konfiguration
	 *
	 * @param modus   der Eingabemods
	 */
	setModus = async (modus: 'manuell' | 'normal' | 'hochschreiben') => {
		await this.config().setValue("app.schueler.laufbahnplanung.modus", modus);
		this._modus.value = modus;
	}

	/**
	 * Wechselt den aktuellen Eingabemodus zum nächsten verfügbaren Modus.
	 */
	public async switchModus() {
		// wenn EF1 und EF2 bereits festgelegt sind, macht der Hochschreibemodus
		// keinen Sinn mehr und wird deaktiviert.
		const festgelegt = this.jahrgang().istBlockungFestgelegt;
		if (festgelegt[0] && festgelegt[1]) {
			switch (this.modus) {
				case 'normal':
					await this.setModus('manuell');
					break;
				case 'manuell':
					await this.setModus('normal');
					break;
				case 'hochschreiben':
					await this.setModus('normal');
					break;
			}
		} else {
			switch (this.modus) {
				case 'normal':
					await this.setModus('hochschreiben');
					break;
				case 'hochschreiben':
					await this.setModus('manuell');
					break;
				case 'manuell':
					await this.setModus('normal');
					break;
			}
		}
	}


	/**
	 * Gibt die Liste alle Fächer vom Fächermanager zurück.
	 *
	 * @returns die die Liste alle Fächer vom Fächermanager
	 */
	public get alleFaecher() : List<GostFach> {
		return this.manager().faecher().faecher();
	}

	/**
	 * Erstellt eine Map mit allen wählbaren Fächer, welche ihrer ID zugeordnet sind
	 */
	private _faecherWaehlbar = computed<List<GostFach>>(() => {
		const result = new ArrayList<GostFach>();
		for (const fach of this.alleFaecher)
			if (fach.istMoeglichEF1 || fach.istMoeglichEF2 || fach.istMoeglichQ11 || fach.istMoeglichQ12 || fach.istMoeglichQ21 || fach.istMoeglichQ22)
				result.add(fach);
		return result;
	});

	/**
	 * Erstellt eine Map mit allen gewählten Fächern, welche ihrer ID zugeordnet sind
	 */
	private _faecherGewaehlt = computed<List<GostFach>>(() => {
		const result = new ArrayList<GostFach>();
		for (const fach of this.alleFaecher) {
			const fb = this.manager().getFachbelegungByID(fach.id);
			if (fb === null)
				continue;
			for (const halbjahr of GostHalbjahr.values()) {
				const fbh : AbiturFachbelegungHalbjahr | null = fb.belegungen[halbjahr.id];
				if ((fbh !== null)) {
					result.add(fach);
					break;
				}
			}
		}
		return result;
	});

	/**
	 * Gibt an, ob nicht wählbare Fächer existieren
	 */
	private _hatFaecherNichtWaehlbar = computed<boolean>(() => {
		return (this._faecherWaehlbar.value.size() !== this.alleFaecher.size());
	});

	/**
	 * Gibt die Konfiguration zurück, ob alle Fächer oder nur ein Teil angezeigt werden sollen
	 */
	get faecherAnzeigen(): 'alle' | 'nur_waehlbare' | 'nur_gewaehlt' {
		return this._faecherAnzeigen.value;
	}

	/**
	 * Setzt in der Konfiguration, ob alle Fächer oder nur ein Teil angezeigt werden sollen
	 *
	 * @param value   die Information, ob alle Fächer oder nur ein Teil angezeigt werden sollen
	 */
	setFaecherAnzeigen = async (value: 'alle' | 'nur_waehlbare' | 'nur_gewaehlt') => {
		await this.config().setValue("app.schueler.laufbahnplanung.faecher.anzeigen", value);
		this._faecherAnzeigen.value = value;
	}

	/**
	 * Wechselt bei der Konfiguration auf den nächsten Modus, in Bezug darauf, ob alle, nur wählbare
	 * oder nur gewählte Fächer angezeigt werden sollen.
	 */
	public async switchFaecherAnzeigen() {
		switch (this.faecherAnzeigen) {
			case 'alle':
				await this.setFaecherAnzeigen(this._hatFaecherNichtWaehlbar.value ? 'nur_waehlbare' : 'nur_gewaehlt')
				break;
			case 'nur_waehlbare':
				await this.setFaecherAnzeigen('nur_gewaehlt')
				break;
			case 'nur_gewaehlt':
				await this.setFaecherAnzeigen('alle')
				break;
		}
	}

	/**
	 * Gibt den Anzeigtext für den konfigurierten Modus zurück, ob alle, nur wählbare
	 * oder nur gewählte Fächer angezeigt werden sollen.
	 *
	 * @returns der Anzeigetext
	 */
	public getTextFaecherAnzeigen() {
		switch (this.faecherAnzeigen) {
			case 'alle':
				return "Alle";
			case 'nur_waehlbare':
				return "Nur wählbare"
			case 'nur_gewaehlt':
				return "Nur gewählte"
		}
	}


	/**
	 * Die Liste der Fächer gefiltert anhand des Anzeigemodus für Fächer
	 */
	private _faecherGefiltert = computed<List<GostFach>>(() => {
		switch (this.faecherAnzeigen) {
			case 'nur_waehlbare':
				return this._faecherWaehlbar.value;
			case 'nur_gewaehlt':
				return this._faecherGewaehlt.value;
			default:
				return this.alleFaecher;
		}
	});

	/**
	 * Gibt die Liste der Fächer gefiltert anhand des Anzeigemodus für Fächer zurück.
	 */
	public get faecherGefiltert() : List<GostFach> {
		return this._faecherGefiltert.value;
	}

	/**
	 * Erstellt eine Map zu den Fachgruppen aller Fächer
	 */
	private _fachgruppe = computed<JavaMap<GostFach, Fachgruppe | null>>(() => {
		const map = new HashMap<GostFach, Fachgruppe | null>();
		for (const fach of this.alleFaecher)
			map.put(fach, Fach.getBySchluesselOrDefault(fach.kuerzel).getFachgruppe(this.manager().getSchuljahr()) ?? null);
		return map;
	});

	/**
	 * Gibt die Fachgruppe zu dem übergebenen Fach zurück.
	 *
	 * @param fach   das Fach
	 *
	 * @returns die zugehörige Fachgruppe oder null, falls keine zugeordnet ist
	 */
	public getFachgruppe(fach: GostFach): Fachgruppe | null {
		return this._fachgruppe.value.get(fach);
	}

	/**
	 * Eine Map welche den Fächern in den Halbjahren die vergebenen Noten zuordnet.
	 */
	private _noten = computed<HashMap2D<GostFach, GostHalbjahr, Note>>(() => {
		const map = new HashMap2D<GostFach, GostHalbjahr, Note>();
		for (const fach of this.alleFaecher) {
			const fachbelegung = this.manager().getFachbelegungByID(fach.id);
			if (fachbelegung === null)
				continue;
			for (const hj of GostHalbjahr.values()) {
				const b = fachbelegung.belegungen[hj.id];
				if ((b === null) || (b.notenkuerzel === null))
					continue;
				const note = Note.fromKuerzel(b.notenkuerzel);
				/* Der Wert Note.KEINE muss hier erhalten bleiben, um den Zustand zu unterscheiden,
				 * dass zwar Leistungsdaten vorhanden sind, aber noch keine Note gesetzt wurde */
				map.put(fach, hj, note);
			}
		}
		return map;
	});

	/**
	 * Gibt die Note zurück, welche dem übergenen Fach in dem übergebenen Halbjahr zugeordnet ist.
	 *
	 * @param fach       das Fach
	 * @param halbjahr   das Halbjahr
	 *
	 * @returns die zugeordnete Note oder null, falls keine Note zugeordnet ist
	 */
	public getNote(fach: GostFach, halbjahr: GostHalbjahr): Note | null {
		return this._noten.value.getOrNull(fach, halbjahr);
	}


	/**
	 * Gibt zurück, ob dem übergenen Fach in dem übergebenen Halbjahr eine Note zugeordnet ist.
	 *
	 * @param fach       das Fach
	 * @param halbjahr   das Halbjahr
	 *
	 * @returns true, wenn dem übergenen Fach in dem übergebenen Halbjahr eine Note zugeordnet ist, und ansonsten false
	 */
	public hatNote(fach: GostFach, halbjahr: GostHalbjahr) : boolean {
		return this._noten.value.getOrNull(fach, halbjahr) !== null;
	}


	/**
	 * Erstellt eine Übersicht, ob bei den Fächern Doppelbelegungen vorkommen oder nicht.
	 */
	private _fachDoppelbelegungen = computed<HashMap2D<GostFach, GostHalbjahr, boolean>>(() => {
		const map = new HashMap2D<GostFach, GostHalbjahr, boolean>();
		for (const fach of this.alleFaecher) {
			// Gehe bei dem Fach zunächst davon aus, dass keine Doppelbelegung vorkommt.
			for (const hj of GostHalbjahr.values())
				map.put(fach, hj, false);
			// Prüfe, ob das Fach relevant für die Prüfungsordnung ist und auch kein Vertiefungskurs ist
			if ((!fach.istPruefungsordnungsRelevant) || (this.getFachgruppe(fach) === Fachgruppe.FG_VX))
				continue;
			// Gehe alle Fachbelegungen zu dem Statistik-Fach durch und prüfe, ob eine zweite Belegung existiert
			const fachbelegungen = this.manager().getFachbelegungByFachkuerzel(fach.kuerzel);
			for (const fachbelegung of fachbelegungen) {
				// Ignoriere die eigene Belegung des Faches
				if (fachbelegung.fachID === fach.id)
					continue;
				// Prüfe das zweite Fach, sofern es relevant für die Prüfungsordnung ist
				const fach2 = this.manager().faecher().get(fachbelegung.fachID);
				if ((fach2 === null) || !fach2.istPruefungsordnungsRelevant)
					continue;
				// Aktualisiere die Map bei einer Doppelbelegung
				for (const hj of GostHalbjahr.values())
					if (this.manager().pruefeBelegung(fachbelegung, hj))
						map.put(fach, hj, true);
			}
		}
		return map;
	});

	/**
	 * Gibt zurück, ob bei dem übergebenen Fach in dem angegebenen Halbjahr eine Doppelbelegung vorliegt oder nicht.
	 *
	 * @param fach       das Fach
	 * @param halbjahr   das Halbjahr
	 *
	 * @returns true, wenn eine Doppelbelegung vorliegt oder nicht, und ansonsten false
	 */
	public hatDoppelbelegung(fach: GostFach, halbjahr: GostHalbjahr) : boolean {
		return this._fachDoppelbelegungen.value.getOrNull(fach, halbjahr) ?? false;
	}

	/**
	 * Eine Map, welche zu einem Fach angibt, ob es sich um eine Fremdsprachen handelt oder nicht
	 */
	private _mapFremdsprachen = computed<JavaMap<GostFach, boolean>>(() => {
		const map = new HashMap<GostFach, boolean>();
		const schuljahr = this.manager().getSchuljahr();
		for (const fach of this.alleFaecher)
			map.put(fach, fach.istFremdsprache && (Fach.getBySchluesselOrDefault(fach.kuerzel).daten(schuljahr)?.istFremdsprache ?? false));
		return map;
	});

	/**
	 * Gibt zurück, ob es sich bei dem übergebenen Fach um ein Fremdsprachenfach handelt oder nicht.
	 *
	 * @param fach   das Fach
	 *
	 * @returns true, falls es sich um ein Fremdsprache handelt, und ansonsten false
	 */
	public istFremdsprache(fach: GostFach): boolean {
		return this._mapFremdsprachen.value.get(fach) ?? false;
	}

	/**
	 * Eine Map mit den Sprachbelegungen, welche dem zugehörigen Fach zugeordnet sind
	 */
	private _mapSprachbelegung = computed<JavaMap<GostFach, Sprachbelegung>>(() => {
		const map = new HashMap<GostFach, Sprachbelegung>();
		const schuljahr = this.manager().getSchuljahr();
		for (const fach of this.alleFaecher) {
			const sprach_kuerzel = Fach.getBySchluesselOrDefault(fach.kuerzel).daten(schuljahr)?.kuerzel ?? null;
			if (sprach_kuerzel === null)
				continue;
			for (const sprache of this.manager().getSprachendaten().belegungen)
				if (sprache.sprache === sprach_kuerzel)
					map.put(fach, sprache);
		}
		return map;
	});

	/**
	 * Gibt zurück, ob es für das übergebene Fach eine gültige Sprachbelegung gibt oder nicht
	 *
	 * @param fach   das Fach
	 *
	 * @returns true, wenn es für das übergebene Fach eine gültige Sprachbelegung gibt, und ansonsten false
	 */
	public hatSprachbelegung(fach: GostFach): boolean {
		if (!this.istFremdspracheMoeglich(fach))
			return false;
		const sprachbelegung = this._mapSprachbelegung.value.get(fach);
		const rf = sprachbelegung?.reihenfolge ?? 0;
		const jg = sprachbelegung?.belegungVonJahrgang ?? "";
		return (rf !== 0) && (jg !== "");
	}

	/**
	 * Gibt die Nummer des Faches in der Reihenfolge der Sprachbelegung an,
	 * sofern es sich um ein Sprachfach handelt und in der Sprachenfolge eine
	 * Belegung hat. Ansonsten wird 0 für die Reihenfolge zurückgegeben.
	 *
	 * @param fach   das Fach
	 *
	 * @returns die Nummer in der Reihenfolge oder 0
	 */
	public getSprachenfolgeNr(fach: GostFach): number {
		if (!this.istFremdspracheMoeglich(fach))
			return 0;
		const sprachbelegung = this._mapSprachbelegung.value.get(fach);
		return sprachbelegung?.reihenfolge ?? 0;
	}

	/**
	 * Gibt den Jahrgang der Sprachbelegung zurück, sofern es sich bei dem übergebenen Fach
	 * um ein Sprachfach handelt und es in der Sprachenfolge eine Belegung hat. Ansonsten
	 * wird ein leerer String für den Jahrgang zurückgegeben.
	 *
	 * @param fach   das Fach
	 *
	 * @returns das Kürzel des Jahrgangs oder ein leerer String
	 */
	public getSprachenfolgeJahrgang(fach: GostFach): string {
		if (!this.istFremdspracheMoeglich(fach))
			return "";
		const sprachbelegung = this._mapSprachbelegung.value.get(fach);
		return sprachbelegung?.belegungVonJahrgang ?? "";
	}

	/**
	 * Eine Map, welche zu einem Fach angibt, ob die Belegung einer Fremdsprache möglich ist oder nicht
	 */
	private _mapFremdsprachenMoeglich = computed<JavaMap<GostFach, boolean>>(() => {
		const map = new HashMap<GostFach, boolean>();
		const schuljahr = this.manager().getSchuljahr();
		for (const fach of this.alleFaecher) {
			if (this.istFremdsprache(fach)) {
				const f = Fach.getBySchluesselOrDefault(fach.kuerzel).daten(schuljahr)?.kuerzel ?? null;
				const istFortfuehrbar = SprachendatenUtils.istFortfuehrbareSpracheInGOSt(this.manager().getSprachendaten(), f);
				map.put(fach, (istFortfuehrbar && !fach.istFremdSpracheNeuEinsetzend) || (!istFortfuehrbar && fach.istFremdSpracheNeuEinsetzend));
			} else {
				map.put(fach, false);
			}
		}
		return map;
	});

	/**
	 * Gibt zurück, ob es sich bei dem übergebenen Fach um eine Fremdsprache handelt und bei dieser
	 * auch eine Belegung möglich ist.
	 *
	 * @param fach   das Fach
	 *
	 * @returns true, wenn eine Belegung des Fremdsprachenfaches möglich ist, und ansonsten false
	 */
	public istFremdspracheMoeglich(fach: GostFach): boolean {
		return this._mapFremdsprachenMoeglich.value.get(fach) ?? false;
	}

	/**
	 * Eine Map, welche angibt, ob bei den Fächern eine Belegung in den einzelnen Jahrgängen möglich ist oder nicht.
	 */
	private _istMoeglich = computed<HashMap2D<GostFach, GostHalbjahr, boolean>>(() => {
		const map = new HashMap2D<GostFach, GostHalbjahr, boolean>();
		for (const fach of this.alleFaecher) {
			// Prüfe, ob es sich um eine Fremdsprache handelt und deren Belegung aufgrund der Sprachenfolge ausgeschlossen ist
			if ((!this.ignoriereSprachenfolge) && (this.istFremdsprache(fach) && !this.istFremdspracheMoeglich(fach))) {
				map.put(fach, GostHalbjahr.EF1, false);
				map.put(fach, GostHalbjahr.EF2, false);
				map.put(fach, GostHalbjahr.Q11, false);
				map.put(fach, GostHalbjahr.Q12, false);
				map.put(fach, GostHalbjahr.Q21, false);
				map.put(fach, GostHalbjahr.Q22, false);
			} else {
				const istNichtErsatzOderPjk = (this.getFachgruppe(fach) !== Fachgruppe.FG_ME) && (this.getFachgruppe(fach) !== Fachgruppe.FG_PX);
				map.put(fach, GostHalbjahr.EF1, (fach.istMoeglichEF1 && !this.hatDoppelbelegung(fach, GostHalbjahr.EF1) && istNichtErsatzOderPjk));
				map.put(fach, GostHalbjahr.EF2, (fach.istMoeglichEF2 && !this.hatDoppelbelegung(fach, GostHalbjahr.EF2) && istNichtErsatzOderPjk));
				map.put(fach, GostHalbjahr.Q11, (fach.istMoeglichQ11 && !this.hatDoppelbelegung(fach, GostHalbjahr.Q11)));
				map.put(fach, GostHalbjahr.Q12, (fach.istMoeglichQ12 && !this.hatDoppelbelegung(fach, GostHalbjahr.Q12)));
				map.put(fach, GostHalbjahr.Q21, (fach.istMoeglichQ21 && !this.hatDoppelbelegung(fach, GostHalbjahr.Q21)));
				map.put(fach, GostHalbjahr.Q22, (fach.istMoeglichQ22 && !this.hatDoppelbelegung(fach, GostHalbjahr.Q22)));
			}
		}
		return map;
	});

	/**
	 * Gibt zurück, ob eine Belegung des übergebenen Faches in dem angegeben Halbjahr möglich ist oder nicht.
	 *
	 * @param fach       das Fach
	 * @param halbjahr   das Halbjahr
	 *
	 * @returns true, wenn eine Belegung des übergebenen Faches in dem angegeben Halbjahr möglich ist, und ansonsten false
	 */
	public istMoeglich(fach: GostFach, halbjahr: GostHalbjahr) : boolean {
		return this._istMoeglich.value.getOrNull(fach, halbjahr) ?? false;
	}

	/**
	 * Eine Map mit der Zuordnung der Möglichen Kursart für ein Abiturfach zu einem Fach.
	 */
	private _abiMoeglicheKursart = computed<JavaMap<GostFach, GostKursart>>(() => {
		const map = new HashMap<GostFach, GostKursart>();
		for (const fach of this.alleFaecher) {
			const tmp = this.manager().getMoeglicheKursartAlsAbiturfach(fach.id);
			if (tmp !== null)
				map.put(fach, tmp);
		}
		return map;
	});


	/**
	 * Gibt zurück, ob das übergene Fach als Abiturfach wählbar ist.
	 *
	 * @param fach   das Fach
	 *
	 * @returns true, wenn es als Abiturfach wählbar ist, und ansonsten false
	 */
	public istMoeglichAbi(fach: GostFach) : boolean {
		return (this._abiMoeglicheKursart.value.get(fach) !== null);
	}

	/**
	 * Gibt die Kursart zurück, welche für das übergeben Fach aufgrund der
	 * Fachbelegungen im Abitur wählbar ist.
	 *
	 * @param fach   das Fach
	 *
	 * @returns die Kursart
	 */
	public getMoeglicheAbiKursart(fach: GostFach): GostKursart | null {
		return this._abiMoeglicheKursart.value.get(fach);
	}


	/**
	 * Lösch-Methode für Spezialfall "Löschen einer nicht erlaubten Fachwahl" zu dem übergebenen Fach
	 *
	 * @param fach   das Fach
	 * @param halbjahr das Halbjahr, für das die Fachwahl gelöscht werden soll
	 */
	public async deleteFachwahl(fach: GostFach, halbjahr: GostHalbjahr | undefined) {
		if (halbjahr === undefined)
			return;
		if (this.istMoeglich(fach, halbjahr) && (!this.manager().istBewertet(halbjahr) || this.hatNote(fach, halbjahr)))
			return;
		const wahl = this.manager().getSchuelerFachwahl(fach.id);
		wahl.halbjahre[halbjahr.id] = null;
		await this.setWahl(fach.id, wahl);
	}


	/**
	 * Lösch-Methode für Spezialfall "Löschen einer nicht erlaubten Fachwahl" aus Abitur-Spalte zu dem übergebenen Fach
	 *
	 * @param fach   das Fach
	 */
	public async deleteFachwahlAbitur(fach: GostFach) {
		const wahl = this.manager().getSchuelerFachwahl(fach.id);
		wahl.abiturFach = null;
		await this.setWahl(fach.id, wahl);
	}


	/**
	 * Stepper für das Durchwandern der Auswahloptionen im Abiturbereich eines Faches
	 * im manuellen Modus.
	 *
	 * @param fach   das Fach
	 *
	 * @returns -
	 */
	private async stepperAbiturManuell(fach: GostFach) : Promise<void> {
		if (this.manager().istBewertet(GostHalbjahr.Q22))
			return;
		const wahl = this.manager().getSchuelerFachwahl(fach.id);
		if (wahl.halbjahre[GostHalbjahr.Q22.id] === null)
			return
		switch (wahl.abiturFach) {
			case null:
				wahl.abiturFach = (wahl.halbjahre[GostHalbjahr.Q22.id] === "LK") ? 1 : 3;
				break;
			case 1:
				wahl.abiturFach = (wahl.halbjahre[GostHalbjahr.Q22.id] === "LK") ? 2 : 3;
				break;
			case 2:
				wahl.abiturFach = (wahl.halbjahre[GostHalbjahr.Q22.id] === "LK") ? null : 3;
				break;
			case 3:
				wahl.abiturFach = (wahl.halbjahre[GostHalbjahr.Q22.id] === "LK") ? null : 4;
				break;
			case 4:
				wahl.abiturFach = null;
				break;
			default:
				wahl.abiturFach = null;
				break;
		}
		await this.setWahl(fach.id, wahl);
	}

	/**
	 * Stepper für das Durchwandern der Auswahloptionen im Abiturbereich eines Faches
	 * im normalen Modus und im Hochschreibemodus.
	 *
	 * @param fach   das Fach
	 *
	 * @returns -
	 */
	private async stepperAbiturNormal(fach: GostFach) {
		// Prüfe, ob die Wahl als Abiturfach überhaupt möglich ist
		if (!this.istMoeglichAbi(fach))
			return;
		// Bestimme die Fachwahl des Schüler und die mögliche Kursart im Abitur.
		const wahl = this.manager().getSchuelerFachwahl(fach.id);
		const abiMoeglicheKursart = this.getMoeglicheAbiKursart(fach);
		// Keine Kursart im Abitur möglich...
		if (abiMoeglicheKursart === null) {
			wahl.abiturFach = null;
			return;
		}
		// Die mögliche Kursart im Abitur ist LK (Leistungskurs)
		if (abiMoeglicheKursart === GostKursart.LK) {
			switch (wahl.abiturFach) {
				case 1:
					wahl.abiturFach = 2;
					break;
				case 2:
					if (GostFachUtils.istWaehlbarLeistungskurs1(fach))
						wahl.abiturFach = 1;
					break;
				default:
					if (GostFachUtils.istWaehlbarLeistungskurs1(fach) && !this.manager().hatAbiFach(GostAbiturFach.LK1))
						wahl.abiturFach = 1;
					wahl.abiturFach = 2;
					break;
			}
			return;
		}
		// Die mögliche Kursart im Abitur ist GK (Grundkurs)
		if (abiMoeglicheKursart === GostKursart.GK) {
			switch (wahl.abiturFach) {
				case null:
					wahl.abiturFach = (wahl.halbjahre[GostHalbjahr.Q22.id] === "M") ? 4 : 3;
					break;
				case 4:
					wahl.abiturFach = (wahl.halbjahre[GostHalbjahr.Q22.id] === "S") ? 3 : null;
					break;
				case 3:
					wahl.abiturFach = (wahl.halbjahre[GostHalbjahr.Q22.id] === "M") ? 4 : null;
					break;
				default:
					wahl.abiturFach = null;
					break;
			}
		}
		await this.setWahl(fach.id, wahl);
	}

	/**
	 * Stepper für das Durchwandern der Auswahloptionen im Abiturbereich. Diese Methode ist ein
	 * Einsprungspunkt für die vue-Komponente und wählt je nach Modus die geeignete Methode aus.
	 *
	 * @param fach   das Fach
	 *
	 * @returns -
	 */
	public async stepperAbitur(fach: GostFach) {
		switch (this.modus) {
			case 'manuell':
				await this.stepperAbiturManuell(fach);
				return;
			case 'normal':
			case 'hochschreiben':
				await this.stepperAbiturNormal(fach);
				return;
		}
	}


	/**
	 * Die Stepper-Methode für die Fachwahlen in der EF.1
	 *
	 * @param fach   das Fach
	 * @param wahl   die Fachwahl
	 */
	private stepEF1Wahl(fach: GostFach, wahl: GostSchuelerFachwahl): void {
		const istVTF = (this.getFachgruppe(fach) === Fachgruppe.FG_VX);
		const istPJK = (this.getFachgruppe(fach) === Fachgruppe.FG_PX);
		switch (wahl.halbjahre[GostHalbjahr.EF1.id]) {
			case null:
				if ((istVTF || istPJK) || (GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && !fach.istMoeglichAbiLK))
					wahl.halbjahre[GostHalbjahr.EF1.id] = "M";
				else
					wahl.halbjahre[GostHalbjahr.EF1.id] = "S";
				break;
			case "S":
				wahl.halbjahre[GostHalbjahr.EF1.id] = "M";
				break;
			case "M":
				if (GostFachbereich.SPORT.hat(fach))
					wahl.halbjahre[GostHalbjahr.EF1.id] = "AT";
				else
					wahl.halbjahre[GostHalbjahr.EF1.id] = null;
				break;
			case "AT":
				wahl.halbjahre[GostHalbjahr.EF1.id] = null;
				break;
		}
	}


	/**
	 * Die Stepper-Methode für die Fachwahlen in der EF.1
	 *
	 * @param fach   das Fach
	 * @param wahl   die Fachwahl
	 */
	private stepEF2Wahl(fach: GostFach, wahl: GostSchuelerFachwahl): void {
		const istVTF = (this.getFachgruppe(fach) === Fachgruppe.FG_VX);
		const istPJK = (this.getFachgruppe(fach) === Fachgruppe.FG_PX);
		switch (wahl.halbjahre[GostHalbjahr.EF2.id]) {
			case null:
				if ((istVTF || istPJK) || (GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && !fach.istMoeglichAbiLK))
					wahl.halbjahre[GostHalbjahr.EF2.id] = "M";
				else
					wahl.halbjahre[GostHalbjahr.EF2.id] = "S";
				break;
			case "S":
				wahl.halbjahre[GostHalbjahr.EF2.id] = "M";
				break;
			case "M":
				if (GostFachbereich.SPORT.hat(fach))
					wahl.halbjahre[GostHalbjahr.EF2.id] = "AT";
				else
					wahl.halbjahre[GostHalbjahr.EF2.id] = null;
				break;
			case "AT":
				wahl.halbjahre[GostHalbjahr.EF2.id] = null;
				break;
		}
	}

	/**
	 * Prüft, ob das übergebene Fach in dem übergebenen Halbjahr wählbar ist oder nicht.
	 *
	 * @param fach       das Fach
	 * @param halbjahr   das Halbjahr
	 *
	 * @returns true, wenn das Fach in dem Halbjahr wählbar ist, und ansonsten false
	 */
	private stepperFachIstWaehlbar(fach: GostFach, halbjahr: GostHalbjahr) : boolean {
		switch (halbjahr) {
			case GostHalbjahr.EF1: return fach.istMoeglichEF1;
			case GostHalbjahr.EF2: return fach.istMoeglichEF2;
			case GostHalbjahr.Q11: return fach.istMoeglichQ11;
			case GostHalbjahr.Q12: return fach.istMoeglichQ12;
			case GostHalbjahr.Q21: return fach.istMoeglichQ21;
			case GostHalbjahr.Q22: return fach.istMoeglichQ22;
		}
		return false;
	}

	/**
	 * Prüft, ob bei dem übergebenen Fach die Fachwahlen im wählbaren Bereich mit denen aus dem Array für die
	 * Qualifikationsphase übereinstimmen.
	 *
	 * @param fach   das Fach
	 * @param wahl   die Fachwahlen
	 * @param a      das Array für die QPhase zum Abgleich mit den Fachwahlen
	 *
	 * @returns true, wenn die Wahl übereinstimmt, und ansonsten false
	 */
	private stepperIstWahlInQPhase(fach: GostFach, wahl: GostSchuelerFachwahl, a: Array<string | null>) : boolean {
		for (const halbjahr of GostHalbjahr.getQualifikationsphase())
			if ((wahl.halbjahre[halbjahr.id] !== a[halbjahr.id - 2]) && this.stepperFachIstWaehlbar(fach, halbjahr))
				return false;
		return true;
	}

	/**
	 * Prüft, ob bei dem übergebenen Fach die Fachwahlen im wählbaren Bereich mit denen aus dem Array übereinstimmen.
	 *
	 * @param fach   das Fach
	 * @param wahl   die Fachwahlen
	 * @param a      das Array zum Abgleich mit den Fachwahlen
	 *
	 * @returns true, wenn die Wahl übereinstimmt, und ansonsten false
	 */
	private stepperIstWahl(fach: GostFach, wahl: GostSchuelerFachwahl, a: Array<string | null>) {
		for (const halbjahr of GostHalbjahr.values())
			if ((wahl.halbjahre[halbjahr.id] !== a[halbjahr.id]) && this.stepperFachIstWaehlbar(fach, halbjahr))
				return false;
		return true;
	}

	/**
	 * Die Stepper-Methode für die Fachwahlen in der EF.1 im Hochschreibe-Modus
	 *
	 * @param fach   das Fach
	 * @param wahl   die Fachwahl
	 */
	private stepEF1WahlHochschreiben(fach: GostFach, wahl: GostSchuelerFachwahl): void {
		const istVTF = (this.getFachgruppe(fach) === Fachgruppe.FG_VX);
		const istPJK = (this.getFachgruppe(fach) === Fachgruppe.FG_PX);
		switch (wahl.halbjahre[GostHalbjahr.EF1.id]) {
			case null: {
				if (wahl.abiturFach !== null) {
					wahl.halbjahre[GostHalbjahr.EF1.id] = 'S';
				// Prüfe, ob die Folgehalbjahre auch leer sind, dann setze auch diese
				} else if (this.stepperIstWahl(fach, wahl, [null, null, null, null, null, null]) && !(istVTF || istPJK)) {
					if ((GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && !fach.istMoeglichAbiLK))
						wahl.halbjahre = ['M', 'M', 'M', 'M', 'M', 'M'];
					else if ((GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && fach.istMoeglichAbiLK))
						wahl.halbjahre = ['S', 'S', 'LK', 'LK', 'LK', 'LK'];
					else
						wahl.halbjahre = [
							fach.istMoeglichEF1 ? 'S' : null,
							fach.istMoeglichEF2 ? 'S' : null,
							fach.istMoeglichQ11 ? 'S' : null,
							fach.istMoeglichQ12 ? 'S' : null,
							fach.istMoeglichQ21 ? 'S' : null,
							fach.istMoeglichQ22 ? 'M' : null,
						];
				} else {
					if (istVTF || istPJK || (GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && !fach.istMoeglichAbiLK))
						wahl.halbjahre[GostHalbjahr.EF1.id] = "M";
					else
						wahl.halbjahre[GostHalbjahr.EF1.id] = "S";
				}
				break;
			}
			case "S": {
				if (wahl.abiturFach !== null)
					wahl.halbjahre[GostHalbjahr.EF1.id] = 'M';
				// Prüfe, ob die Folgehalbjahre S,S,S,S,M sind und Abi-Fach nicht gesetzt (Spezialfälle berücksichtigen KU+MU+RE)
				else if (this.stepperIstWahl(fach, wahl, ['S', 'S', 'S', 'S', 'S', 'M']) && !(istVTF || istPJK))
					if (GostFachbereich.KUNST_MUSIK.hat(fach) || GostFachbereich.RELIGION.hat(fach))
						wahl.halbjahre = ['M', 'M', 'M', 'M', null, null];
					else
						wahl.halbjahre = [
							fach.istMoeglichEF1 ? 'M' : null,
							fach.istMoeglichEF2 ? 'M' : null,
							fach.istMoeglichQ11 ? 'M' : null,
							fach.istMoeglichQ12 ? 'M' : null,
							fach.istMoeglichQ21 ? 'M' : null,
							fach.istMoeglichQ22 ? 'M' : null,
						];
				else if (this.stepperIstWahl(fach, wahl, ['S', 'S', 'LK', 'LK', 'LK', 'LK']) && (GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && fach.istMoeglichAbiLK))
					wahl.halbjahre = ['M', 'M', 'M', 'M', 'M', 'M'];
				else
					wahl.halbjahre[GostHalbjahr.EF1.id] = "M";
				break;
			}
			case "M": {
				if (wahl.abiturFach !== null) {
					wahl.halbjahre[GostHalbjahr.EF1.id] = 'S';
				// Prüfe, ob die Folgehalbjahre M,M,M,M?,M? sind und passe diese an (Spezialfälle berücksichtigen KU+MU+RE)
				} else if ((this.stepperIstWahl(fach, wahl, ['M', 'M', 'M', 'M', 'M', 'M']) || this.stepperIstWahl(fach, wahl, ['M', 'M', 'M', 'M', null, null])) && !(istVTF || istPJK)) {
					if (GostFachbereich.SPORT.hat(fach))
						wahl.halbjahre = ["AT", "AT", "AT", "AT", "AT", "AT"];
					else
						wahl.halbjahre = [null, null, null, null, null, null];
				} else {
					if (GostFachbereich.SPORT.hat(fach))
						wahl.halbjahre[GostHalbjahr.EF1.id] = "AT";
					else
						wahl.halbjahre[GostHalbjahr.EF1.id] = null;
				}
				break;
			}
			case "AT": {
				if (wahl.abiturFach !== null) {
					wahl.halbjahre[GostHalbjahr.EF1.id] = 'S';
				} else if (this.stepperIstWahl(fach, wahl, ["AT", "AT", "AT", "AT", "AT", "AT"])) {
					wahl.halbjahre = [null, null, null, null, null, null];
				} else {
					wahl.halbjahre[GostHalbjahr.EF1.id] = null;
				}
				break;
			}
		}
	}


	/**
	 * Die Stepper-Methode für die Fachwahlen in der EF.2 im Hochschreibe-Modus
	 *
	 * @param fach   das Fach
	 * @param wahl   die Fachwahl
	 */
	private stepEF2WahlHochschreiben(fach: GostFach, wahl: GostSchuelerFachwahl): void {
		const istVTF = (this.getFachgruppe(fach) === Fachgruppe.FG_VX);
		const istPJK = (this.getFachgruppe(fach) === Fachgruppe.FG_PX);
		switch (wahl.halbjahre[GostHalbjahr.EF2.id]) {
			case null: {
				if (wahl.abiturFach !== null) {
					wahl.halbjahre[GostHalbjahr.EF2.id] = 'S';
				} else if (this.stepperIstWahlInQPhase(fach, wahl, [null, null, null, null]) && !(istVTF || istPJK)) {
					if ((GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && !fach.istMoeglichAbiLK))
						wahl.halbjahre = [wahl.halbjahre[0], 'M', 'M', 'M', 'M', 'M'];
					else if ((GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && fach.istMoeglichAbiLK))
						wahl.halbjahre = [wahl.halbjahre[0], 'S', 'LK', 'LK', 'LK', 'LK'];
					else
						wahl.halbjahre = [
							wahl.halbjahre[0],
							fach.istMoeglichEF2 ? 'S' : null,
							fach.istMoeglichQ11 ? 'S' : null,
							fach.istMoeglichQ12 ? 'S' : null,
							fach.istMoeglichQ21 ? 'S' : null,
							fach.istMoeglichQ22 ? 'M' : null,
						];
				} else {
					if (istVTF || istPJK || (GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && !fach.istMoeglichAbiLK))
						wahl.halbjahre[GostHalbjahr.EF2.id] = "M";
					else
						wahl.halbjahre[GostHalbjahr.EF2.id] = "S";
				}
				break;
			}
			case "S": {
				if (wahl.abiturFach !== null)
					wahl.halbjahre[GostHalbjahr.EF2.id] = 'M';
				else if ((this.stepperIstWahlInQPhase(fach, wahl, [null, null, null, null])
					|| this.stepperIstWahlInQPhase(fach, wahl, ['S', 'S', 'S', 'M'])) && !(istVTF || istPJK))
					if (GostFachbereich.KUNST_MUSIK.hat(fach) || GostFachbereich.RELIGION.hat(fach))
						wahl.halbjahre = [wahl.halbjahre[0], 'M', 'M', 'M', null, null];
					else
						wahl.halbjahre = [
							wahl.halbjahre[0],
							fach.istMoeglichEF2 ? 'M' : null,
							fach.istMoeglichQ11 ? 'M' : null,
							fach.istMoeglichQ12 ? 'M' : null,
							fach.istMoeglichQ21 ? 'M' : null,
							fach.istMoeglichQ22 ? 'M' : null,
						];
				else if (this.stepperIstWahlInQPhase(fach, wahl, ['LK', 'LK', 'LK', 'LK']) && (GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && fach.istMoeglichAbiLK))
					wahl.halbjahre = [wahl.halbjahre[0], 'M', 'M', 'M', 'M', 'M'];
				else
					wahl.halbjahre[GostHalbjahr.EF2.id] = "M";
				break;
			}
			case "M": {
				if (wahl.abiturFach !== null) {
					wahl.halbjahre[GostHalbjahr.EF2.id] = 'S';
				} else if ((this.stepperIstWahlInQPhase(fach, wahl, [null, null, null, null])
					|| this.stepperIstWahlInQPhase(fach, wahl, ['M', 'M', 'M', 'M'])
					|| this.stepperIstWahlInQPhase(fach, wahl, ['M', 'M', null, null])) && !(istVTF || istPJK)) {
					if (GostFachbereich.SPORT.hat(fach))
						wahl.halbjahre = [wahl.halbjahre[0], "AT", "AT", "AT", "AT", "AT"];
					else
						wahl.halbjahre = [wahl.halbjahre[0], null, null, null, null, null];
				} else {
					if (GostFachbereich.SPORT.hat(fach))
						wahl.halbjahre[GostHalbjahr.EF2.id] = "AT";
					else
						wahl.halbjahre[GostHalbjahr.EF2.id] = null;
				}
				break;
			}
			case "AT": {
				if (wahl.abiturFach !== null) {
					wahl.halbjahre[GostHalbjahr.EF2.id] = 'S';
				} else if (this.stepperIstWahl(fach, wahl, [wahl.halbjahre[0], "AT", "AT", "AT", "AT", "AT"])) {
					wahl.halbjahre = [wahl.halbjahre[0], null, null, null, null, null];
				} else {
					wahl.halbjahre[GostHalbjahr.EF2.id] = null;
				}
			}
		}
	}


	/**
	 * Hilfsmethode für die Stepper, um zu prüfen, ob bei den Fachwahlen in dem übergebenen Halbjahr eine
	 * Belegung vorliegt oder nicht.
	 *
	 * @param fachwahl   die Fachwahlen
	 * @param halbjahr   das Halbjahr
	 *
	 * @returns true, wenn eine Belegung vorliegt und ansonsten false
	 */
	private stepperHatSchuelerFachwahl(fachwahl : GostSchuelerFachwahl | null, halbjahr: GostHalbjahr) : boolean {
		if (fachwahl === null)
			return false;
		return fachwahl.halbjahre[halbjahr.id] !== null;
	}


	/**
	 * Die Stepper-Methode für die Fachwahlen in der Q1.1
	 *
	 * @param fach   das Fach
	 * @param wahl   die Fachwahl
	 */
	private stepQ11Wahl(fach: GostFach, wahl: GostSchuelerFachwahl): void {
		const istVTF = (this.getFachgruppe(fach) === Fachgruppe.FG_VX);
		const istPJK = (this.getFachgruppe(fach) === Fachgruppe.FG_PX);
		switch (wahl.halbjahre[GostHalbjahr.Q11.id]) {
			case null:
				wahl.halbjahre[GostHalbjahr.Q11.id] = (GostFachbereich.DEUTSCH.hat(fach) || GostFachbereich.MATHEMATIK.hat(fach)) ? "S" : "M";
				break;
			case "M":
				if (istVTF || istPJK || GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(fach))
					wahl.halbjahre[GostHalbjahr.Q11.id] = null;
				else if (GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && !fach.istMoeglichAbiLK)
					wahl.halbjahre[GostHalbjahr.Q11.id] = null;
				else if (GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && fach.istMoeglichAbiLK)
					wahl.halbjahre[GostHalbjahr.Q11.id] = "LK";
				else
					wahl.halbjahre[GostHalbjahr.Q11.id] = "S";
				break;
			case "S":
				//S->S ist richtig, weil DE und MA muss belegt sein, entweder S oder LK, anders geht es nicht.
				wahl.halbjahre[GostHalbjahr.Q11.id] = (fach.istMoeglichAbiLK) ? "LK" : (GostFachbereich.DEUTSCH.hat(fach) || GostFachbereich.MATHEMATIK.hat(fach)) ? "S" : null;
				break;
			case "LK":
				wahl.halbjahre[GostHalbjahr.Q11.id] = (GostFachbereich.DEUTSCH.hat(fach) || GostFachbereich.MATHEMATIK.hat(fach)) ? "S" : null;
				wahl.abiturFach = null;
				break;
		}
		// Sonderfall Sport - darf AT haben
		if ((wahl.halbjahre[GostHalbjahr.Q11.id] === null) && GostFachbereich.SPORT.hat(fach))
			wahl.halbjahre[GostHalbjahr.Q11.id] = "AT";
		else if (wahl.halbjahre[GostHalbjahr.Q11.id] === "AT" && GostFachbereich.SPORT.hat(fach))
			wahl.halbjahre[GostHalbjahr.Q11.id] = null;
		// Q11 wählt bis Q22
		switch (wahl.halbjahre[GostHalbjahr.Q11.id]) {
			case null:
				if (!istVTF) {
					wahl.halbjahre[GostHalbjahr.Q12.id] = null;
					wahl.halbjahre[GostHalbjahr.Q21.id] = null;
					wahl.halbjahre[GostHalbjahr.Q22.id] = null;
				}
				if (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(fach) && (this.jahrgang().hatZusatzkursSW)) {
					const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(this.jahrgang().beginnZusatzkursSW);
					if (beginn !== null) {
						if (beginn === GostHalbjahr.Q11) {
							if (!this.stepperHatSchuelerFachwahl(wahl, GostHalbjahr.EF2) && !this.hatDoppelbelegung(fach, GostHalbjahr.EF2)) {
								wahl.halbjahre[GostHalbjahr.Q11.id] = 'ZK'
								wahl.halbjahre[GostHalbjahr.Q12.id] = 'ZK'
							}
						}
					}
				}
				if (GostFachbereich.GESCHICHTE.hat(fach) && this.jahrgang().hatZusatzkursGE) {
					const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(this.jahrgang().beginnZusatzkursGE);
					if (beginn !== null) {
						if (beginn === GostHalbjahr.Q11) {
							if (!this.stepperHatSchuelerFachwahl(wahl, GostHalbjahr.EF2) && !this.hatDoppelbelegung(fach, GostHalbjahr.EF2)) {
								wahl.halbjahre[GostHalbjahr.Q11.id] = 'ZK'
								wahl.halbjahre[GostHalbjahr.Q12.id] = 'ZK'
							}
						}
					}
					if ((beginn !== null) && (beginn === GostHalbjahr.Q11) && (wahl.halbjahre[GostHalbjahr.EF2.id] === null)) {
						wahl.halbjahre[GostHalbjahr.Q11.id] = "ZK";
						wahl.halbjahre[GostHalbjahr.Q12.id] = "ZK";
					}
				}
				break;
			case "M":
				if (fach.istMoeglichQ12 && !istVTF)
					wahl.halbjahre[GostHalbjahr.Q12.id] = wahl.halbjahre[GostHalbjahr.Q11.id];
				if (!(istVTF || istPJK) && !GostFachbereich.KUNST_MUSIK.hat(fach) && !GostFachbereich.RELIGION.hat(fach)) {
					if (fach.istMoeglichQ21) wahl.halbjahre[GostHalbjahr.Q21.id] = wahl.halbjahre[GostHalbjahr.Q11.id];
					if (fach.istMoeglichQ22) wahl.halbjahre[GostHalbjahr.Q22.id] = wahl.halbjahre[GostHalbjahr.Q11.id];
				}
				break;
			case "S":
				if (fach.istMoeglichQ12) wahl.halbjahre[GostHalbjahr.Q12.id] = wahl.halbjahre[GostHalbjahr.Q11.id];
				if (!(istVTF || istPJK)) {
					if (fach.istMoeglichQ21)
						wahl.halbjahre[GostHalbjahr.Q21.id] = wahl.halbjahre[GostHalbjahr.Q11.id];
					// "S" kann nur für drittes Abifach gewählt werden, Vorauswahl daher "M"
					if (fach.istMoeglichQ22)
						wahl.halbjahre[GostHalbjahr.Q22.id] = "M";
				}
				break;
			case "ZK":
				wahl.halbjahre[GostHalbjahr.Q11.id] = null;
				wahl.halbjahre[GostHalbjahr.Q12.id] = null;
				break;
			case "LK": {
				wahl.halbjahre[GostHalbjahr.Q12.id] = fach.istMoeglichQ12 ? wahl.halbjahre[GostHalbjahr.Q11.id] : null;
				wahl.halbjahre[GostHalbjahr.Q21.id] = fach.istMoeglichQ21 ? wahl.halbjahre[GostHalbjahr.Q11.id] : null;
				wahl.halbjahre[GostHalbjahr.Q22.id] = fach.istMoeglichQ22 ? wahl.halbjahre[GostHalbjahr.Q11.id] : null;
				// Bedingungen für LK1
				const alle_fachbelegungen = this.manager().getFachbelegungen();
				const lk1_belegt = this.manager().pruefeExistiertAbiFach(alle_fachbelegungen, GostAbiturFach.LK1);
				const lk2_belegt = this.manager().pruefeExistiertAbiFach(alle_fachbelegungen, GostAbiturFach.LK2);
				if (GostFachbereich.DEUTSCH.hat(fach) || GostFachbereich.MATHEMATIK.hat(fach)
					|| GostFachbereich.NATURWISSENSCHAFTLICH_KLASSISCH.hat(fach)
					|| (GostFachbereich.FREMDSPRACHE.hat(fach) && !fach.istFremdSpracheNeuEinsetzend)) {
					wahl.abiturFach = !lk1_belegt ? 1 : lk2_belegt ? null : 2;
				} else {
					wahl.abiturFach = lk2_belegt ? null : 2;
				}
				break;
			}
		}
		if ((wahl.halbjahre[GostHalbjahr.Q11.id] === null) || (wahl.halbjahre[GostHalbjahr.Q11.id] === "M"))
			wahl.abiturFach = null;
	}


	/**
	 * Die Stepper-Methode für die Fachwahlen in der Q1.2
	 *
	 * @param fach   das Fach
	 * @param wahl   die Fachwahl
	 */
	private stepQ12Wahl(fach: GostFach, wahl: GostSchuelerFachwahl): void {
		const istVTF = (this.getFachgruppe(fach) === Fachgruppe.FG_VX);
		const istPJK = (this.getFachgruppe(fach) === Fachgruppe.FG_PX);
		switch (wahl.halbjahre[GostHalbjahr.Q12.id]) {
			case null:
				wahl.halbjahre[GostHalbjahr.Q12.id] = "M";
				if (istPJK && (wahl.halbjahre[GostHalbjahr.Q11.id] === null) && fach.istMoeglichQ21) {
					wahl.halbjahre[GostHalbjahr.Q21.id] = "M";
					wahl.halbjahre[GostHalbjahr.Q22.id] = null;
				}
				if (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(fach) && (this.jahrgang().hatZusatzkursSW)) {
					const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(this.jahrgang().beginnZusatzkursSW);
					if (beginn !== null) {
						if (beginn === GostHalbjahr.Q11) {
							if (!this.stepperHatSchuelerFachwahl(wahl, GostHalbjahr.EF2) && !this.hatDoppelbelegung(fach, GostHalbjahr.EF2)) {
								wahl.halbjahre[GostHalbjahr.Q11.id] = 'ZK'
								wahl.halbjahre[GostHalbjahr.Q12.id] = 'ZK'
							}
						}
						if (beginn === GostHalbjahr.Q12) {
							if (!this.stepperHatSchuelerFachwahl(wahl, GostHalbjahr.Q11) && !this.hatDoppelbelegung(fach, GostHalbjahr.Q11)) {
								wahl.halbjahre[GostHalbjahr.Q12.id] = 'ZK'
								wahl.halbjahre[GostHalbjahr.Q21.id] = 'ZK'
							}
						}
					}
				}
				if (GostFachbereich.GESCHICHTE.hat(fach) && this.jahrgang().hatZusatzkursGE) {
					const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(this.jahrgang().beginnZusatzkursGE);
					if (beginn !== null) {
						if (beginn === GostHalbjahr.Q11) {
							if (!this.stepperHatSchuelerFachwahl(wahl, GostHalbjahr.EF2) && !this.hatDoppelbelegung(fach, GostHalbjahr.EF2)) {
								wahl.halbjahre[GostHalbjahr.Q11.id] = 'ZK'
								wahl.halbjahre[GostHalbjahr.Q12.id] = 'ZK'
							}
						}
						if (beginn === GostHalbjahr.Q12) {
							if (!this.stepperHatSchuelerFachwahl(wahl, GostHalbjahr.Q11) && !this.hatDoppelbelegung(fach, GostHalbjahr.Q11)) {
								wahl.halbjahre[GostHalbjahr.Q12.id] = 'ZK'
								wahl.halbjahre[GostHalbjahr.Q21.id] = 'ZK'
							}
						}
					}
					if ((beginn !== null) && (((beginn === GostHalbjahr.Q11) && (wahl.halbjahre[GostHalbjahr.EF2.id] === null)) || ((beginn === GostHalbjahr.Q12) && (wahl.halbjahre[GostHalbjahr.Q11.id] === null)))) {
						if (beginn === GostHalbjahr.Q11)
							wahl.halbjahre[GostHalbjahr.Q11.id] = "ZK";
						wahl.halbjahre[GostHalbjahr.Q12.id] = "ZK";
						if (beginn === GostHalbjahr.Q12)
							wahl.halbjahre[GostHalbjahr.Q21.id] = "ZK";
					}
				}
				break;
			case "M":
				if (istVTF || istPJK || GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(fach))
					wahl.halbjahre[GostHalbjahr.Q12.id] = null;
				else if (GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && !fach.istMoeglichAbiLK)
					wahl.halbjahre[GostHalbjahr.Q12.id] = null;
				else if (GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && fach.istMoeglichAbiLK)
					wahl.halbjahre[GostHalbjahr.Q12.id] = "LK";
				else
					wahl.halbjahre[GostHalbjahr.Q12.id] = "S";
				break;
			case "S":
				wahl.halbjahre[GostHalbjahr.Q12.id] = (wahl.halbjahre[GostHalbjahr.Q11.id] === "LK") ? "LK" : null;
				break;
			case "ZK": {
				const beginn : GostHalbjahr | null = (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(fach))
					? GostHalbjahr.fromKuerzel(this.jahrgang().beginnZusatzkursSW ?? "")
					: GostHalbjahr.fromKuerzel(this.jahrgang().beginnZusatzkursGE ?? "");
				if ((beginn !== null) && (beginn === GostHalbjahr.Q11))
					wahl.halbjahre[GostHalbjahr.Q11.id] = null;
				wahl.halbjahre[GostHalbjahr.Q12.id] = null;
				if ((beginn !== null) && (beginn === GostHalbjahr.Q12))
					wahl.halbjahre[GostHalbjahr.Q21.id] = null;
				break;
				// TODO: Warum ist das so? Bis Q22. Was ist erlaubt: M, S, null?
			}
			case "LK":
				wahl.halbjahre[GostHalbjahr.Q12.id] = null;
				wahl.abiturFach = null;
		}
		// Sonderfall Sport - darf AT haben
		if ((wahl.halbjahre[GostHalbjahr.Q12.id] === null) && GostFachbereich.SPORT.hat(fach))
			wahl.halbjahre[GostHalbjahr.Q12.id] = "AT";
		else if ((wahl.halbjahre[GostHalbjahr.Q12.id] === "AT") && GostFachbereich.SPORT.hat(fach))
			wahl.halbjahre[GostHalbjahr.Q12.id] = null;
		// Nachfolgende HJ ebenfalls setzen
		if ((wahl.halbjahre[GostHalbjahr.Q12.id] === null) && !istVTF) {
			wahl.halbjahre[GostHalbjahr.Q21.id] = null;
			wahl.halbjahre[GostHalbjahr.Q22.id] = null;
		}
		if ((wahl.halbjahre[GostHalbjahr.Q12.id] === null) || (wahl.halbjahre[GostHalbjahr.Q12.id] === "M"))
			wahl.abiturFach = null;
	}


	/**
	 * Die Stepper-Methode für die Fachwahlen in der Q2.1
	 *
	 * @param fach   das Fach
	 * @param wahl   die Fachwahl
	 */
	private stepQ21Wahl(fach: GostFach, wahl: GostSchuelerFachwahl): void {
		const istVTF = (this.getFachgruppe(fach) === Fachgruppe.FG_VX);
		const istPJK = (this.getFachgruppe(fach) === Fachgruppe.FG_PX);
		switch (wahl.halbjahre[GostHalbjahr.Q21.id]) {
			case null:
				wahl.halbjahre[GostHalbjahr.Q21.id] = "M";
				if (istPJK && (wahl.halbjahre[GostHalbjahr.Q12.id] === null) && fach.istMoeglichQ22) {
					wahl.halbjahre[GostHalbjahr.Q22.id] = "M";
				}
				if (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(fach) && (this.jahrgang().hatZusatzkursSW)) {
					const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(this.jahrgang().beginnZusatzkursSW);
					if (beginn !== null) {
						if (beginn === GostHalbjahr.Q12) {
							if (!this.stepperHatSchuelerFachwahl(wahl, GostHalbjahr.Q11) && !this.hatDoppelbelegung(fach, GostHalbjahr.Q11)) {
								wahl.halbjahre[GostHalbjahr.Q12.id] = 'ZK'
								wahl.halbjahre[GostHalbjahr.Q21.id] = 'ZK'
							}
						}
						if (beginn === GostHalbjahr.Q21) {
							if (!this.stepperHatSchuelerFachwahl(wahl, GostHalbjahr.Q12) && !this.hatDoppelbelegung(fach, GostHalbjahr.Q12)) {
								wahl.halbjahre[GostHalbjahr.Q21.id] = 'ZK'
								wahl.halbjahre[GostHalbjahr.Q22.id] = 'ZK'
							}
						}
					}
				}
				if (GostFachbereich.GESCHICHTE.hat(fach) && this.jahrgang().hatZusatzkursGE) {
					const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(this.jahrgang().beginnZusatzkursGE);
					if (beginn !== null) {
						if (beginn === GostHalbjahr.Q12) {
							if (!this.stepperHatSchuelerFachwahl(wahl, GostHalbjahr.Q11) && !this.hatDoppelbelegung(fach, GostHalbjahr.Q11)) {
								wahl.halbjahre[GostHalbjahr.Q12.id] = 'ZK'
								wahl.halbjahre[GostHalbjahr.Q21.id] = 'ZK'
							}
						}
						if (beginn === GostHalbjahr.Q21) {
							if (!this.stepperHatSchuelerFachwahl(wahl, GostHalbjahr.Q12) && !this.hatDoppelbelegung(fach, GostHalbjahr.Q12)) {
								wahl.halbjahre[GostHalbjahr.Q21.id] = 'ZK'
								wahl.halbjahre[GostHalbjahr.Q22.id] = 'ZK'
							}
						}
					}
				}
				break;
			case "M":
				if (istVTF || istPJK || GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(fach))
					wahl.halbjahre[GostHalbjahr.Q21.id] = null;
				else if (GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && !fach.istMoeglichAbiLK)
					wahl.halbjahre[GostHalbjahr.Q21.id] = null;
				else if (GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && fach.istMoeglichAbiLK)
					wahl.halbjahre[GostHalbjahr.Q21.id] = "LK";
				else
					wahl.halbjahre[GostHalbjahr.Q21.id] = "S";
				break;
			case "S":
				wahl.halbjahre[GostHalbjahr.Q21.id] = (wahl.halbjahre[GostHalbjahr.Q12.id] === "LK") ? "LK" : null;
				break;
			case "ZK": {
				const beginn : GostHalbjahr | null = (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(fach))
					? GostHalbjahr.fromKuerzel(this.jahrgang().beginnZusatzkursSW ?? "")
					: GostHalbjahr.fromKuerzel(this.jahrgang().beginnZusatzkursGE ?? "");
				if ((beginn !== null) && (beginn === GostHalbjahr.Q12))
					wahl.halbjahre[GostHalbjahr.Q12.id] = null;
				wahl.halbjahre[GostHalbjahr.Q21.id] = null;
				if ((beginn !== null) && (beginn === GostHalbjahr.Q21))
					wahl.halbjahre[GostHalbjahr.Q22.id] = null;
				break;
			}
			case "LK":
				wahl.halbjahre[GostHalbjahr.Q21.id] = null;
				wahl.abiturFach = null;
				break;
		}
		// Sonderfall Sport - darf AT haben
		if ((wahl.halbjahre[GostHalbjahr.Q21.id] === null) && GostFachbereich.SPORT.hat(fach))
			wahl.halbjahre[GostHalbjahr.Q21.id] = "AT";
		else if ((wahl.halbjahre[GostHalbjahr.Q21.id] === "AT") && GostFachbereich.SPORT.hat(fach))
			wahl.halbjahre[GostHalbjahr.Q21.id] = null;
		// Nachfolgende HJ ebenfalls setzen
		if ((wahl.halbjahre[GostHalbjahr.Q21.id] === null) && !istVTF)
			wahl.halbjahre[GostHalbjahr.Q22.id] = null;
		if ((wahl.halbjahre[GostHalbjahr.Q21.id] === null) || (wahl.halbjahre[GostHalbjahr.Q21.id] === "ZK"))
			wahl.abiturFach = null;
	}


	/**
	 * Die Stepper-Methode für die Fachwahlen in der Q2.2
	 *
	 * @param fach   das Fach
	 * @param wahl   die Fachwahl
	 */
	private stepQ22Wahl(fach: GostFach, wahl: GostSchuelerFachwahl): void {
		const istVTF = (this.getFachgruppe(fach) === Fachgruppe.FG_VX);
		const istPJK = (this.getFachgruppe(fach) === Fachgruppe.FG_PX);
		switch (wahl.halbjahre[GostHalbjahr.Q22.id]) {
			case null:
				wahl.halbjahre[GostHalbjahr.Q22.id] = "M";
				if (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(fach) && (this.jahrgang().hatZusatzkursSW)) {
					const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(this.jahrgang().beginnZusatzkursSW);
					if (beginn !== null) {
						if (beginn === GostHalbjahr.Q21) {
							if (!this.stepperHatSchuelerFachwahl(wahl, GostHalbjahr.Q12) && !this.hatDoppelbelegung(fach, GostHalbjahr.Q12)) {
								wahl.halbjahre[GostHalbjahr.Q21.id] = 'ZK'
								wahl.halbjahre[GostHalbjahr.Q22.id] = 'ZK'
							}
						}
					}
				}
				if (GostFachbereich.GESCHICHTE.hat(fach) && this.jahrgang().hatZusatzkursGE) {
					const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(this.jahrgang().beginnZusatzkursGE);
					if (beginn !== null) {
						if (beginn === GostHalbjahr.Q21) {
							if (!this.stepperHatSchuelerFachwahl(wahl, GostHalbjahr.Q12) && !this.hatDoppelbelegung(fach, GostHalbjahr.Q12)) {
								wahl.halbjahre[GostHalbjahr.Q21.id] = 'ZK'
								wahl.halbjahre[GostHalbjahr.Q22.id] = 'ZK'
							}
						}
					}
				}
				break;
			case "M":
				if (istVTF || istPJK || GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(fach))
					wahl.halbjahre[GostHalbjahr.Q22.id] = null;
				else if (GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && !fach.istMoeglichAbiLK)
					wahl.halbjahre[GostHalbjahr.Q22.id] = null;
				else if (GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && fach.istMoeglichAbiLK)
					wahl.halbjahre[GostHalbjahr.Q22.id] = "LK";
				else
					wahl.halbjahre[GostHalbjahr.Q22.id] = "S";
				break;
			case "S":
				wahl.halbjahre[GostHalbjahr.Q22.id] = (wahl.halbjahre[GostHalbjahr.Q21.id] === "LK") ? "LK" : null;
				break;
			case "ZK": {
				const beginn : GostHalbjahr | null = (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(fach))
					? GostHalbjahr.fromKuerzel(this.jahrgang().beginnZusatzkursSW ?? "")
					: GostHalbjahr.fromKuerzel(this.jahrgang().beginnZusatzkursGE ?? "");
				if ((beginn !== null) && (beginn === GostHalbjahr.Q21)) {
					wahl.halbjahre[GostHalbjahr.Q21.id] = null;
				}
				wahl.halbjahre[GostHalbjahr.Q22.id] = null;
				break;
			}
			case "LK":
				wahl.halbjahre[GostHalbjahr.Q22.id] = null;
				wahl.abiturFach = null;
		}
		// Sonderfall Sport - darf AT haben
		if ((wahl.halbjahre[GostHalbjahr.Q22.id] === null) && GostFachbereich.SPORT.hat(fach))
			wahl.halbjahre[GostHalbjahr.Q22.id] = "AT";
		else if ((wahl.halbjahre[GostHalbjahr.Q22.id] === "AT") && GostFachbereich.SPORT.hat(fach))
			wahl.halbjahre[GostHalbjahr.Q22.id] = null;
		// Nachfolgende HJ ebenfalls setzen
		if ((wahl.halbjahre[GostHalbjahr.Q22.id] === null) || (wahl.halbjahre[GostHalbjahr.Q22.id] === "ZK"))
			wahl.abiturFach = null;
		if (wahl.abiturFach === 3 && wahl.halbjahre[GostHalbjahr.Q22.id] === "M")
			wahl.abiturFach = this.manager().pruefeExistiertAbiFach(this.manager().getFachbelegungen(), GostAbiturFach.AB4) ? null : 4;
		if (wahl.abiturFach === 4 && wahl.halbjahre[GostHalbjahr.Q22.id] === "S")
			wahl.abiturFach = this.manager().pruefeExistiertAbiFach(this.manager().getFachbelegungen(), GostAbiturFach.AB3) ? null : 3;
	}


	/**
	 * Stepper für das Durchwandern der Auswahloptionen im Bereich der Halbjahre eines Faches
	 * im manuellen Modus und im Hochschreibemodus.
	 *
	 * @param fach       das Fach
	 * @param halbjahr   das Halbjahr
	 *
	 * @returns -
	 */
	private async stepperManuell(fach: GostFach, halbjahr: GostHalbjahr) {
		if (this.manager().istBewertet(halbjahr))
			return;
		const wahl = this.manager().getSchuelerFachwahl(fach.id);
		const hj = halbjahr.id;
		const istVTF = (this.getFachgruppe(fach) === Fachgruppe.FG_VX);
		const istPJK = (this.getFachgruppe(fach) === Fachgruppe.FG_PX);
		switch (wahl.halbjahre[hj]) {
			case "AT":
				wahl.halbjahre[hj] = null;
				break;
			case "ZK":
				wahl.halbjahre[hj] = null;
				break;
			case null:
				wahl.halbjahre[hj] = "M";
				break;
			case "M":
				if (!fach.istPruefungsordnungsRelevant || istVTF || istPJK || (GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(fach)))
					wahl.halbjahre[hj] = null;
				else
					wahl.halbjahre[hj] = "S";
				break;
			case "S":
				if ((hj <= 1) || (GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(fach))) {
					if (GostFachbereich.SPORT.hat(fach))
						wahl.halbjahre[hj] = "AT";
					else
						wahl.halbjahre[hj] = null;
				} else { // in der Q-Phase als LK möglich, allerdings nicht im Fachbereich des literarisch-künstlerischen Bereichs
					wahl.halbjahre[hj] = "LK";
				}
				break;
			case "LK": {
				wahl.halbjahre[hj] = null
				if (GostFachbereich.SPORT.hat(fach))
					wahl.halbjahre[hj] = "AT";
				if (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(fach) || GostFachbereich.GESCHICHTE.hat(fach))
					wahl.halbjahre[hj] = "ZK";
				break;
			}
			default:
				wahl.halbjahre[hj] = null;
				break;
		}
		await this.setWahl(fach.id, wahl);
	}

	/**
	 * Stepper für das Durchwandern der Auswahloptionen im Bereich der Halbjahre eines Faches
	 * im Hochschreibe-Modus und im Hochschreibemodus.
	 *
	 * @param fach       das Fach
	 * @param halbjahr   das Halbjahr
	 *
	 * @returns -
	 */
	public async stepperHochschreiben(fach: GostFach, halbjahr: GostHalbjahr) {
		if ((!this.istMoeglich(fach, halbjahr)) || this.manager().istBewertet(halbjahr))
			return;
		const wahl = this.manager().getSchuelerFachwahl(fach.id);
		if (halbjahr === GostHalbjahr.EF1)
			this.stepEF1WahlHochschreiben(fach, wahl);
		else if (halbjahr === GostHalbjahr.EF2)
			this.stepEF2WahlHochschreiben(fach, wahl);
		else if (halbjahr === GostHalbjahr.Q11)
			this.stepQ11Wahl(fach, wahl);
		else if (halbjahr === GostHalbjahr.Q12)
			this.stepQ12Wahl(fach, wahl);
		else if (halbjahr === GostHalbjahr.Q21)
			this.stepQ21Wahl(fach, wahl);
		else if (halbjahr === GostHalbjahr.Q22)
			this.stepQ22Wahl(fach, wahl);
		await this.setWahl(fach.id, wahl);
	}

	/**
	 * Stepper für das Durchwandern der Auswahloptionen im Bereich der Halbjahre eines Faches
	 * im normalen Modus und im Hochschreibemodus.
	 *
	 * @param fach       das Fach
	 * @param halbjahr   das Halbjahr
	 *
	 * @returns -
	 */
	private async stepperNormal(fach: GostFach, halbjahr: GostHalbjahr) {
		if ((!this.istMoeglich(fach, halbjahr)) || this.manager().istBewertet(halbjahr))
			return;
		const wahl = this.manager().getSchuelerFachwahl(fach.id);
		if (halbjahr === GostHalbjahr.EF1)
			this.stepEF1Wahl(fach, wahl);
		else if (halbjahr === GostHalbjahr.EF2)
			this.stepEF2Wahl(fach, wahl);
		else if (halbjahr === GostHalbjahr.Q11)
			this.stepQ11Wahl(fach, wahl);
		else if (halbjahr === GostHalbjahr.Q12)
			this.stepQ12Wahl(fach, wahl);
		else if (halbjahr === GostHalbjahr.Q21)
			this.stepQ21Wahl(fach, wahl);
		else if (halbjahr === GostHalbjahr.Q22)
			this.stepQ22Wahl(fach, wahl);
		await this.setWahl(fach.id, wahl);
	}

	/**
	 * Stepper für das Durchwandern der Auswahloptionen im Bereich der Halbjahre eines Faches. Diese Methode ist ein
	 * Einsprungspunkt für die vue-Komponente und wählt je nach Modus die geeignete Methode aus.
	 *
	 * @param fach       das Fach
	 * @param halbjahr   das Halbjahr
	 *
	 * @returns -
	 */
	public async stepper(fach: GostFach, halbjahr: GostHalbjahr) {
		switch (this.modus) {
			case 'manuell':
				await this.stepperManuell(fach, halbjahr);
				return;
			case 'hochschreiben':
				await this.stepperHochschreiben(fach, halbjahr);
				return;
			case 'normal':
				await this.stepperNormal(fach, halbjahr);
				return;
		}
	}

}
