import { computed, ref } from "vue";
import { AbiturFachbelegung, AbiturFachbelegungHalbjahr, Fach, Fachgruppe, GostJahrgangsdaten, HashMap2D, JavaMap, SprachendatenUtils} from "../../../../../core/src";
import { ArrayList, DeveloperNotificationException, GostHalbjahr, HashMap, type AbiturdatenManager, type GostFach, type List } from "../../../../../core/src";
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
	 * @param manager   der Abiturdaten-Manager zur Durchführung der Belegprüfung
	 * @param config    die Konfiguration des Clients
	 */
	public constructor(manager: () => AbiturdatenManager, config: () => Config, jahrgang: () => GostJahrgangsdaten) {
		this.manager = manager;
		this.config = config;
		this.jahrgang = jahrgang;
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

}
