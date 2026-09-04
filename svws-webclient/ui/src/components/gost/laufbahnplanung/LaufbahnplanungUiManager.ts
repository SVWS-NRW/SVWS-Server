import { RGBFarbe } from "@core/asd/data/RGBFarbe";
import type { Sprachbelegung } from "@core/asd/data/schueler/Sprachbelegung";
import type { Sprachpruefung } from "@core/asd/data/schueler/Sprachpruefung";
import { Fach } from "@core/asd/types/fach/Fach";
import { Fachgruppe } from "@core/asd/types/fach/Fachgruppe";
import { Note } from "@core/asd/types/Note";
import { AbiturdatenManager } from "@core/core/abschluss/gost/AbiturdatenManager";
import { HashMap2D } from "@core/core/adt/map/HashMap2D";
import type { AbiturFachbelegung } from "@core/core/data/gost/AbiturFachbelegung";
import type { AbiturFachbelegungHalbjahr } from "@core/core/data/gost/AbiturFachbelegungHalbjahr";
import type { GostFach } from "@core/core/data/gost/GostFach";
import type { GostJahrgangsdaten } from "@core/core/data/gost/GostJahrgangsdaten";
import type { GostSchuelerFachwahl } from "@core/core/data/gost/GostSchuelerFachwahl";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { GostFachbereich } from "@core/core/types/gost/GostFachbereich";
import { GostHalbjahr } from "@core/core/types/gost/GostHalbjahr";
import type { GostKursart } from "@core/core/types/gost/GostKursart";
import { GostSchriftlichkeit } from "@core/core/types/gost/GostSchriftlichkeit";
import type { ServerMode } from "@core/core/types/ServerMode";
import { SprachendatenUtils } from "@core/core/utils/schueler/SprachendatenUtils";
import { ArrayList } from "@core/java/util/ArrayList";
import { HashMap } from "@core/java/util/HashMap";
import type { JavaMap } from "@core/java/util/JavaMap";
import type { List } from "@core/java/util/List";
import { useGostLaufbahnplanungState } from "@ui/states/GostLaufbahnplanungState";
import type { Config } from "@ui/utils/Config";
import { computed, ref } from "vue";
import type { LaufbahnplanungUiStepper, LaufbahnplanungUiStepperMode } from "./LaufbahnplanungUiStepper";
import { LaufbahnplanungUiStepperAbi2013 } from "./LaufbahnplanungUiStepperAbi2013";
import { LaufbahnplanungUiStepperAbi2030 } from "./LaufbahnplanungUiStepperAbi2030";


export type LaufbahnplanungUiFaecherAnzeigen = 'alle' | 'nur_waehlbare' | 'nur_gewaehlt';


/*
 * Die Implementierung enthält Teile von experimentellem Code. Für diesen gilt folgendes:
 *
 * Bei dieser Implementierung handelt es sich um eine Umsetzung in Bezug auf möglichen zukünftigen
 * Änderungen in der APO-GOSt. Diese basiert auf der aktuellen Implementierung und integriert Aspekte
 * aus dem Eckpunktepapier und auf in den Schulleiterdienstbesprechungen erläuterten Vorhaben.
 * Sie dient der Evaluierung von möglichen Umsetzungsvarianten und als Vorbereitung einer späteren
 * Implementierung der Belegprüfung. Insbesondere sollen erste Versuche mit Laufbahnen mit einem
 * 5. Abiturfach und Projektkursen erprobt werden. Detailaspekte können erst nach Erscheinen der APO-GOSt
 * umgesetzt werden.
 * Es handelt sich also um experimentellen Code, der keine Rückschlüsse auf Details einer zukünftigen APO-GOSt
 * erlaubt.
 */
/**
 * Laufbahnplanung Oberstufe: Ein Manager für die Verwaltung von UI-Spezifischen Funktionen
 * auf die aktuellen Laufbahn-Beratungsdaten eines Schülers.
 */
export class LaufbahnplanungUiManager implements LaufbahnplanungUiStepper {

	/** Der aktuelle State für die Laufbahnplanung der Gymnasialen Oberstufe */
	private readonly gostLaufbahnplanungState = useGostLaufbahnplanungState();

	/** Der Modus, in welchem der Server betrieben wird */
	private readonly serverMode: ServerMode;

	/** Die Konfiguration des Clients */
	private readonly config: () => Config;

	/** Gibt an, ob bei den angebotenen Sprachfächern eine Sprachenfolge berücksichtigt werden soll oder nicht */
	private readonly _ignoriereSprachenfolge: boolean = false;

	/** Gibt an, ob bei den einzelnen Fachbelegungen immer Noten bei den Leistungsdaten angenommen werden sollen */
	private readonly _belegungHatImmerNoten: boolean = false;

	/** Computed Property: Ein Array mit der Anzahl von anrechenbaren Kursen in den einzelnen Halbjahren */
	private readonly _anrechenbareKurse = computed<number[]>(() => this.gostLaufbahnplanungState.valid
		? this.manager.getAnrechenbareKurse() : [0, 0, 0, 0, 0, 0]);

	/** Computed Property: Die Anzahl der anrechenbaren Kurse für Block I des Abiturs */
	public _summeAnrechenbareKurse = computed<number>(() => this.gostLaufbahnplanungState.valid
		? this.manager.getAnrechenbareKurseBlockI() : 0);

	/** Computed Property: Die Wochenstunden, welche von dem Schüler in den einzelnen Halbjahren
	 * der gymnasialen Oberstufe für das Abitur relevant belegt wurden. */
	public _wochenstunden = computed<number[]>(() => this.gostLaufbahnplanungState.valid
		? this.manager.getWochenstunden() : [0, 0, 0, 0, 0, 0]);

	/** Computed Property: Die Summe der Jahres-Wochenstunden zurück. Das bedeutet die Summe der
	 * Durchschnitte der drei Jahre der gym. Oberstufe. */
	public _wochenstundenJahressumme = computed<number>(() => this._wochenstunden.value.reduce((p, c) => p + c, 0) / 2);

	/** Computed Property: Die durchschnittlichen Wochenstunden in den Halbjahren der Einführungsphase */
	public _wochenstundenDurchschnittEF = computed<number>(() => this.gostLaufbahnplanungState.valid
		? this.manager.getWochenstundenEinfuehrungsphase() / 2 : 0);

	/** Computed Property: Die durchschnittlichen Wochenstunden in den Halbjahren der Qualifikationsphase */
	public _wochenstundenDurchschnittQ = computed<number>(() => this.gostLaufbahnplanungState.valid
		? this.manager.getWochenstundenQualifikationsphase() / 4 : 0);

	/** Die Konfiguration, ob alle Fächer oder nur ein Teil angezeigt werden soll */
	public _faecherAnzeigen = ref<LaufbahnplanungUiFaecherAnzeigen>('alle');

	/** Der Eingabemodus */
	public _modus = ref<LaufbahnplanungUiStepperMode>('normal');

	/** Der Pfad zum Speichern der Configdaten */
	private readonly _configPfade: { faecherZeigen: string, modus: string };

	/** Der Stepper für das Abitur bis 2029 */
	private readonly uiStepper2013 = new LaufbahnplanungUiStepperAbi2013(this);

	/** Der Stepper für das Abitur ab 2030 */
	private readonly uiStepper2030 = new LaufbahnplanungUiStepperAbi2030(this);


	/**
	 * Erstellt einen neuen UI-Manager auf Basis des übergebenen Abiturdaten-Managers,
	 * welcher die Belegprüfung durchführt.
	 *
	 * @param serverMode               der Mode, in welchem der Server betrieben wird
	 * @param config                   die Konfiguration des Clients
	 * @param configPfade              die zu verwendenden Pfade für das Speichern der Einstellungen in der Konfiguration (Nutzung des Managers in unterschiedlichen Ansichten)
	 * @param ignoriereSprachenfolge   gibt an, ob bei den angebotenen Sprachfächern eine Sprachenfolge berücksichtigt werden soll oder nicht
	 * @param belegungHatImmerNoten    gibt an, ob bei den einzelnen Fachbelegungen immer Noten bei den Leistungsdaten angenommen werden sollen
	 */
	public constructor(serverMode: ServerMode, config: () => Config, configPfade: { faecherZeigen: string, modus: string },
		ignoriereSprachenfolge: boolean = false, belegungHatImmerNoten: boolean = false) {
		this.serverMode = serverMode;
		this.config = config;
		this._ignoriereSprachenfolge = ignoriereSprachenfolge;
		this._belegungHatImmerNoten = belegungHatImmerNoten;
		this._configPfade = configPfade;
		// Lese aus der Konfiguration aus, ob alle Fächer oder nur ein Teil angezeigt werden soll
		const wert = this.config().getValue(configPfade.faecherZeigen);
		if ((wert === 'alle') || (wert === 'nur_waehlbare') || (wert === 'nur_gewaehlt')) {
			this._faecherAnzeigen.value = wert;
		} else {
			void this.config().setValue(configPfade.faecherZeigen, 'alle');
			this._faecherAnzeigen.value = 'alle';
		}
		// Lese den Eingabemodus aus der Konfiguration aus
		const modus = this.config().getValue(configPfade.modus);
		if ((modus === 'manuell') || (modus === 'normal') || (modus === 'hochschreiben')) {
			this._modus.value = modus;
		} else {
			void this.config().setValue(configPfade.modus, 'normal');
			this._modus.value = 'normal';
		}
	}

	private async setWahl(idFach: number, wahl: GostSchuelerFachwahl) {
		if (!this.gostLaufbahnplanungState.valid) {
			throw new DeveloperNotificationException("Es wurde auf den State der Laufbahnplanung zugegriffen, obwohl dieser nicht valide ist.");
		}
		await this.gostLaufbahnplanungState.setWahl(idFach, wahl);
	}

	private get manager(): AbiturdatenManager {
		if (!this.gostLaufbahnplanungState.valid) {
			throw new DeveloperNotificationException("Es wurde auf den State der Laufbahnplanung zugegriffen, obwohl dieser nicht valide ist.");
		}
		return this.gostLaufbahnplanungState.abiturdatenManager;
	}

	private get jahrgang(): GostJahrgangsdaten {
		if (!this.gostLaufbahnplanungState.valid) {
			throw new DeveloperNotificationException("Es wurde auf den State der Laufbahnplanung zugegriffen, obwohl dieser nicht valide ist.");
		}
		return this.gostLaufbahnplanungState.gostJahrgangsdaten;
	}

	/** Gibt an, ob der experimentelle Code ab Abitur 2030 genutzt werden soll */
	private readonly isAbi30ff = computed<boolean>(() => this.gostLaufbahnplanungState.valid &&
		 AbiturdatenManager.istAbitur2030(this.manager.getAbiturjahr()));

	/** Gibt zurück, ob es sich um einen Ui-Manager für die Abiturberechnung ab dem Jahr 2030 handelt oder nicht */
	public get istAbiturAb2030(): boolean {
		return this.isAbi30ff.value;
	}

	/**
	 * Gibt zurück, ob bei den angebotenen Sprachfächern eine Sprachenfolge berücksichtigt werden soll oder nicht
	 */
	public get ignoriereSprachenfolge(): boolean {
		return this._ignoriereSprachenfolge;
	}

	/*
	 * Gibt zurück, ob bei den einzelnen Fachbelegungen immer Noten bei den Leistungsdaten angenommen werden sollen
	 */
	public get belegungHatImmerNoten(): boolean {
		return this._belegungHatImmerNoten;
	}

	/**
	 * Das aktuelle Halbjahr der Oberstufe des Abiturjahrgangs oder null, wenn der Jahrgang aktuell nicht zur Oberstufe gehört.
	 */
	private readonly _aktuellesHalbjahr = computed<GostHalbjahr | null>(() => this.gostLaufbahnplanungState.valid
		? GostHalbjahr.fromJahrgangUndHalbjahr(this.jahrgang.jahrgang,
			this.jahrgang.halbjahr)
		: null);

	/**
	 * Gibt das aktuelle Halbjahr der Oberstufe des Abiturjahrgangs zurück oder null, wenn der Jahrgang aktuell nicht zur Oberstufe gehört.
	 */
	public get aktuellesHalbjahr(): GostHalbjahr | null {
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
	 * Gibt den Text für den Tooltip der anrechenbaren Kurse zurück.
	 *
	 * @returns der Tooltip-Text
	 */
	public getTooltipAnrechenbareKurse(): string {
		if (this.isAbi30ff.value) {
			return 'Die Anzahl der Kurse. Vertiefungskurse werden in der Qualifikationsphase mitgezählt. Zu beachten ist, dass Vertiefungskurse in der Qualifikationsphase nicht in die Gesamtqualifikation eingebracht werden können.';
		}
		return 'Die Anzahl der anrechenbaren Kurse. Vertiefungskurse werden z.B. nicht mitgezählt.';
	}


	/**
	 * Gibt die Anzahl der Kurs für das übergebene Halbjahr zurück.
	 */
	public getAnrechenbareKurse(hj: GostHalbjahr): number {
		return this._anrechenbareKurse.value[hj.id];
	}

	/**
	 * Gibt die CSS-Klasse zur Einstufung zurück, ob die Anzahl der Kurse genügt.
	 *
	 * @param hj   das Halbjahr der gymnasialen Oberstufe
	 *
	 * @returns die CSS-Klasse zur Einstufung
	 */
	public getAnrechenbareKurseCSS(hj: GostHalbjahr): string {
		const kurse = this.getAnrechenbareKurse(hj);
		if (this.isAbi30ff.value) {
			if (hj.istEinfuehrungsphase()) {
				if (kurse < 10) {
					return 'svws-ergebnis--not-enough';
				}
				if ((kurse > 9) && (kurse < 11)) {
					return 'svws-ergebnis--good';
				}
				return 'svws-ergebnis--more';
			}
			if (kurse < 9) {
				return 'svws-ergebnis--not-enough';
			}
			if ((kurse > 8) && (kurse < 10)) {
				return 'svws-ergebnis--low';
			}
			if ((kurse > 9) && (kurse < 12)) {
				return 'svws-ergebnis--good';
			}
			return 'svws-ergebnis--more';
		}
		if (hj.istEinfuehrungsphase()) {
			if (kurse < 10) {
				return 'svws-ergebnis--not-enough';
			}
			if ((kurse > 9) && (kurse < 11)) {
				return 'svws-ergebnis--low';
			}
			if ((kurse > 10) && (kurse < 13)) {
				return 'svws-ergebnis--good';
			}
			return 'svws-ergebnis--more';
		}
		if (kurse < 9) {
			return 'svws-ergebnis--not-enough';
		}
		if ((kurse > 8) && (kurse < 10)) {
			return 'svws-ergebnis--low';
		}
		if ((kurse > 9) && (kurse < 12)) {
			return 'svws-ergebnis--good';
		}
		return 'svws-ergebnis--more';
	}

	/**
	 * Gibt die Anzahl der anrechenbaren Kurse für Block I des Abiturs zurück.
	 */
	public get summeAnrechenbareKurse(): number {
		return this._summeAnrechenbareKurse.value;
	}

	/**
	 * Gibt die CSS-Klasse zur Einstufung zurück, ob die Anzahl der anrechenbaren Kurse für Block I des Abiturs genügt.
	 *
	 * @param hj   das Halbjahr der gymnasialen Oberstufe
	 *
	 * @returns die CSS-Klasse zur Einstufung
	 */
	public getSummeAnrechenbareKurseCSS(): string {
		if (this.isAbi30ff.value) {
			if ((this.summeAnrechenbareKurse === 40)) {
				return 'svws-ergebnis--good';
			}
			return 'svws-ergebnis--not-enough';
		}
		if (this.summeAnrechenbareKurse < 38) {
			return 'svws-ergebnis--not-enough';
		}
		if ((this.summeAnrechenbareKurse > 37) && (this.summeAnrechenbareKurse < 40)) {
			return 'svws-ergebnis--low';
		}
		if ((this.summeAnrechenbareKurse > 39) && (this.summeAnrechenbareKurse < 43)) {
			return 'svws-ergebnis--good';
		}
		return 'svws-ergebnis--more';
	}

	/**
	 * Gibt den Text für den Tooltip der Wochenstunden zurück.
	 *
	 * @returns der Tooltip-Text
	 */
	public getTooltipWochenstunden(): string {
		if (this.isAbi30ff.value) {
			return 'Die Anzahl der Wochenstunden.';
		}
		return 'Die Anzahl der Wochenstunden. Pro Halbjahr sollten etwa <strong>33—36</strong> Wochenstunden gewählt werden.';
	}

	/**
	 * Gibt die Wochenstunden für das übergebene Halbjahr zurück, welche von dem Schüler in den einzelnen Halbjahren
	 * der gymnasialen Oberstufe für das Abitur relevant belegt wurden.
	 */
	public getWochenstunden(hj: GostHalbjahr): number {
		return this._wochenstunden.value[hj.id];
	}

	/**
	 * Gibt die CSS-Klasse zur Einstufung zurück, ob die Anzahl der Wochenstunden für das angegebene Halbjahr genügt.
	 *
	 * @param hj   das Halbjahr der gymnasialen Oberstufe
	 *
	 * @returns die CSS-Klasse zur Einstufung
	 */
	public getWochenstundenCSS(hj: GostHalbjahr): string {
		const wst = this.getWochenstunden(hj);
		// TODO G8 beachten
		if (this.isAbi30ff.value) {
			return '';
		}
		if (wst < 30) {
			return 'svws-ergebnis--not-enough';
		}
		if ((wst >= 30) && (wst < 33)) {
			return 'svws-ergebnis--low';
		}
		if ((wst >= 33) && (wst < 37)) {
			return 'svws-ergebnis--good';
		}
		return 'svws-ergebnis--more';
	}

	/**
	 * Gibt die Summe der Jahres-Wochenstunden zurück. Das bedeutet die Summe der
	 * Durchschnitte der drei Jahre der gym. Oberstufe.
	 */
	public get wochenstundenJahressumme(): number {
		return this._wochenstundenJahressumme.value;
	}

	/**
	 * Gibt die CSS-Klasse zur Einstufung zurück, ob die Summe der Jahres-Wochenstunden genügt.
	 *
	 * @returns die CSS-Klasse zur Einstufung
	 */
	public getWochenstundenJahressummeCSS(): string {
		// TODO G8 beachten
		if (this.isAbi30ff.value) {
			return '';
		}
		if (this.wochenstundenJahressumme < 100) {
			return 'svws-ergebnis--not-enough';
		}
		if ((this.wochenstundenJahressumme >= 100) && (this.wochenstundenJahressumme < 101)) {
			return 'svws-ergebnis--low';
		}
		if ((this.wochenstundenJahressumme >= 101) && (this.wochenstundenJahressumme <= 106)) {
			return 'svws-ergebnis--good';
		}
		return 'svws-ergebnis--more';
	}


	/**
	 * Gibt zurück, ob die Zeile mit dem Wochenstundendurchschnitt angezeigt werden soll oder nicht.
	 *
	 * @returns true, wenn sie angezeigt werden soll, und ansonsten false
	 */
	public zeigeWochenstundenDurchschnitt(): boolean {
		return !this.isAbi30ff.value;
	}

	/**
	 * Gibt zurück, ob die Komponente mit den Wahlen der GKLs angezeigt werden soll oder nicht.
	 *
	 * @returns true, wenn sie angezeigt werden soll, und ansonsten false
	 */
	public zeigeGKLWahlen(): boolean {
		return this.isAbi30ff.value;
	}

	/**
	 * Gibt zurück, ob die Wahl des Referenzfaches angeboten wird oder nicht.
	 *
	 * @param fach   das Fach der Oberstufe
	 */
	public zeigeReferenzfachWahl(fach: GostFach): boolean {
		if (!this.isAbi30ff.value || (fach.kuerzel !== "PX")) {
			return false;
		}
		const wahl = this.manager.getSchuelerFachwahl(fach.id);
		return ((wahl.halbjahre[GostHalbjahr.Q21.id] !== null) && (wahl.halbjahre[GostHalbjahr.Q22.id] !== null));
	}

	/**
	 * Gibt die CSS-Klasse zur Einstufung zurück, ob der Halbjahresdurchschnitt der Wochenstunden genügt.
	 *
	 * @param wst   die Wochenstunden
	 *
	 * @returns die CSS-Klasse zur Einstufung
	 */
	private getWochenstundenDurchschnittCSS(wst: number): string {
		if (wst < 34) {
			return 'svws-ergebnis--not-enough';
		}
		if ((wst >= 34) && (wst < 37)) {
			return 'svws-ergebnis--good';
		}
		return 'svws-ergebnis--more';
	}

	/**
	 * Gibt die durchschnittlichen Wochenstunden in den Halbjahren der Einführungsphase zurück.
	 */
	public get wochenstundenDurchschnittEF(): number {
		return this._wochenstundenDurchschnittEF.value;
	}

	/**
	 * Gibt die CSS-Klasse zur Einstufung zurück, ob die durchschnittlichen Wochenstunden in den Halbjahren
	 * der Einführungsphase genügt.
	 *
	 * @returns die CSS-Klasse zur Einstufung
	 */
	public getWochenstundenDurchschnittEFCSS(): string {
		return this.getWochenstundenDurchschnittCSS(this.wochenstundenDurchschnittEF);
	}

	/**
	 * Gibt die durchschnittlichen Wochenstunden in den Halbjahren der Qualifikationsphase zurück.
	 */
	public get wochenstundenDurchschnittQ(): number {
		return this._wochenstundenDurchschnittQ.value;
	}

	/**
	 * Gibt die CSS-Klasse zur Einstufung zurück, ob die durchschnittlichen Wochenstunden in den Halbjahren
	 * der Qualifikationsphase genügt.
	 *
	 * @returns die CSS-Klasse zur Einstufung
	 */
	public getWochenstundenDurchschnittQCSS(): string {
		return this.getWochenstundenDurchschnittCSS(this.wochenstundenDurchschnittQ);
	}


	/**
	 * Gibt den konfigurierten Eingabemodus für die Laufbahnplanung zurück
	 */
	get modus(): LaufbahnplanungUiStepperMode {
		return this._modus.value;
	}

	/**
	 * Setzt den Eingabemodus in der Konfiguration
	 *
	 * @param modus   der Eingabemods
	 */
	setModus = async (modus: LaufbahnplanungUiStepperMode) => {
		await this.config().setValue("app.schueler.laufbahnplanung.modus", modus);
		this._modus.value = modus;
	};

	/**
	 * Wechselt den aktuellen Eingabemodus zum nächsten verfügbaren Modus.
	 */
	public async switchModus() {
		// wenn EF1 und EF2 bereits festgelegt sind, macht der Hochschreibemodus
		// keinen Sinn mehr und wird deaktiviert.
		const festgelegt = this.jahrgang.istBlockungFestgelegt;
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
	public get alleFaecher(): List<GostFach> {
		return this.gostLaufbahnplanungState.valid ? this.manager.faecher().faecher() : new ArrayList();
	}

	/**
	 * Erstellt eine Map mit allen wählbaren Fächer, welche ihrer ID zugeordnet sind
	 */
	private readonly _faecherWaehlbar = computed<List<GostFach>>(() => {
		const result = new ArrayList<GostFach>();
		for (const fach of this.alleFaecher) {
			if (fach.istMoeglichEF1 || fach.istMoeglichEF2 || fach.istMoeglichQ11 || fach.istMoeglichQ12 || fach.istMoeglichQ21 || fach.istMoeglichQ22) {
				result.add(fach);
			}
		}
		return result;
	});

	/**
	 * Erstellt eine Map mit allen gewählten Fächern, welche ihrer ID zugeordnet sind
	 */
	private readonly _faecherGewaehlt = computed<List<GostFach>>(() => {
		const result = new ArrayList<GostFach>();
		for (const fach of this.alleFaecher) {
			const fb = this.manager.getFachbelegungByID(fach.id);
			if (fb === null) {
				continue;
			}
			for (const halbjahr of GostHalbjahr.values()) {
				const fbh: AbiturFachbelegungHalbjahr | null = fb.belegungen[halbjahr.id];
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
	private readonly _hatFaecherNichtWaehlbar = computed<boolean>(() => {
		return (this._faecherWaehlbar.value.size() !== this.alleFaecher.size());
	});

	/**
	 * Gibt die Konfiguration zurück, ob alle Fächer oder nur ein Teil angezeigt werden sollen
	 */
	get faecherAnzeigen(): LaufbahnplanungUiFaecherAnzeigen {
		return this._faecherAnzeigen.value;
	}

	/**
	 * Setzt in der Konfiguration, ob alle Fächer oder nur ein Teil angezeigt werden sollen
	 *
	 * @param value   die Information, ob alle Fächer oder nur ein Teil angezeigt werden sollen
	 */
	setFaecherAnzeigen = async (value: LaufbahnplanungUiFaecherAnzeigen) => {
		await this.config().setValue("app.schueler.laufbahnplanung.faecher.anzeigen", value);
		this._faecherAnzeigen.value = value;
	};

	/**
	 * Wechselt bei der Konfiguration auf den nächsten Modus, in Bezug darauf, ob alle, nur wählbare
	 * oder nur gewählte Fächer angezeigt werden sollen.
	 */
	public async switchFaecherAnzeigen() {
		switch (this.faecherAnzeigen) {
			case 'alle':
				await this.setFaecherAnzeigen(this._hatFaecherNichtWaehlbar.value ? 'nur_waehlbare' : 'nur_gewaehlt');
				break;
			case 'nur_waehlbare':
				await this.setFaecherAnzeigen('nur_gewaehlt');
				break;
			case 'nur_gewaehlt':
				await this.setFaecherAnzeigen('alle');
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
				return "Nur wählbare";
			case 'nur_gewaehlt':
				return "Nur gewählte";
		}
	}


	/**
	 * Die Liste der Fächer gefiltert anhand des Anzeigemodus für Fächer
	 */
	private readonly _faecherGefiltert = computed<List<GostFach>>(() => {
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
	public get faecherGefiltert(): List<GostFach> {
		return this._faecherGefiltert.value;
	}

	/**
	 * Erstellt eine Map zu den Fachgruppen aller Fächer
	 */
	private readonly _fachgruppe = computed<JavaMap<GostFach, Fachgruppe>>(() => {
		const map = new HashMap<GostFach, Fachgruppe>();
		for (const fach of this.alleFaecher) {
			if (this.isAbi30ff.value) {
				const f = Fach.getBySchluesselOrDefault(fach.kuerzel);
				if ((f === Fach.IN) || (f === Fach.VO)) {
					continue;
				}
			}
			const fg = Fach.getBySchluesselOrDefault(fach.kuerzel).getFachgruppe(this.manager.getSchuljahr()) ?? null;
			if (fg === null) {
				continue;
			}
			map.put(fach, fg);
		}
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
	 * Gibt die Fachfarbe für das übergebene Fach zurück.
	 *
	 * @param fach   das Fach
	 *
	 * @returns die zugehörige Fachfarbe
	 */
	public getFachfarbe(fach: GostFach): string {
		if (!this.gostLaufbahnplanungState.valid) {
			return "rgb(240, 240, 240)";
		}
		const gruppe = this._fachgruppe.value.get(fach);
		const farbe: RGBFarbe = (gruppe === null) ? new RGBFarbe() : gruppe.getFarbe(this.manager.getSchuljahr());
		return "rgb(" + farbe.red + "," + farbe.green + "," + farbe.blue + ")";
	}


	/**
	 * Gibt zurück, ob es sich bei dem Fach um ein bilinguales Fach handelt oder nicht.
	 *
	 * @param fach   das Fach, welches auf Bilingualität geprüft wird
	 *
	 * @returns true, wenn es bilingual unterrichtet wird, und ansonsten false
	 */
	public istBilingual(fach: GostFach): boolean {
		if (GostFachbereich.FREMDSPRACHE.hat(fach) || GostFachbereich.DEUTSCH.hat(fach)) {
			return false;
		}
		return (fach.biliSprache !== null) && (fach.biliSprache !== "D");
	}


	/**
	 * Eine Map welche den Fächern in den Halbjahren die vergebenen Noten zuordnet.
	 */
	private readonly _noten = computed<HashMap2D<GostFach, GostHalbjahr, Note>>(() => {
		const map = new HashMap2D<GostFach, GostHalbjahr, Note>();
		for (const fach of this.alleFaecher) {
			const fachbelegung = this.manager.getFachbelegungByID(fach.id);
			if (fachbelegung === null) {
				continue;
			}
			for (const hj of GostHalbjahr.values()) {
				const b = fachbelegung.belegungen[hj.id];
				if ((b === null) || (b.notenkuerzel === null)) {
					continue;
				}
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
	public hatNote(fach: GostFach, halbjahr: GostHalbjahr): boolean {
		return this._noten.value.getOrNull(fach, halbjahr) !== null;
	}


	/**
	 * Erstellt eine Übersicht, ob bei den Fächern Doppelbelegungen vorkommen oder nicht.
	 */
	private readonly _fachDoppelbelegungen = computed<HashMap2D<GostFach, GostHalbjahr, boolean>>(() => {
		const map = new HashMap2D<GostFach, GostHalbjahr, boolean>();
		for (const fach of this.alleFaecher) {
			// Gehe bei dem Fach zunächst davon aus, dass keine Doppelbelegung vorkommt.
			for (const hj of GostHalbjahr.values()) {
				map.put(fach, hj, false);
			}
			// Prüfe, ob das Fach relevant für die Prüfungsordnung ist und auch kein Vertiefungskurs ist
			if ((!fach.istPruefungsordnungsRelevant) || (this.getFachgruppe(fach) === Fachgruppe.FG_VX)) {
				continue;
			}
			// Gehe alle Fachbelegungen zu dem Statistik-Fach durch und prüfe, ob eine zweite Belegung existiert
			const fachbelegungen = this.manager.getFachbelegungByFachkuerzel(fach.kuerzel);
			for (const fachbelegung of fachbelegungen) {
				// Ignoriere die eigene Belegung des Faches
				if (fachbelegung.fachID === fach.id) {
					continue;
				}
				// Prüfe das zweite Fach, sofern es relevant für die Prüfungsordnung ist
				const fach2 = this.manager.faecher().get(fachbelegung.fachID);
				if ((fach2 === null) || !fach2.istPruefungsordnungsRelevant) {
					continue;
				}
				// Aktualisiere die Map bei einer Doppelbelegung
				for (const hj of GostHalbjahr.values()) {
					if (this.manager.pruefeBelegung(fachbelegung, hj)) {
						map.put(fach, hj, true);
					}
				}
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
	public hatDoppelbelegung(fach: GostFach, halbjahr: GostHalbjahr): boolean {
		return this._fachDoppelbelegungen.value.getOrNull(fach, halbjahr) ?? false;
	}


	/**
	 * Gibt den Tooltip-Text für die Anzeige der Leitfächer zurück.
	 *
	 * @param fach   das Fach für den Vertiefungskurs bzw. den Projektkurs
	 *
	 * @returns der Tooltip-Text
	 */
	public getLeitfaecherTooltipText(fach: GostFach): string {
		if ((this.getFachgruppe(fach) !== Fachgruppe.FG_PX) && (this.getFachgruppe(fach) !== Fachgruppe.FG_VX)) {
			return "";
		}
		const textLeitfach = this.isAbi30ff.value ? "Referenzfach" : "Leitfach";
		let result = "";
		if (fach.projektKursLeitfach1ID !== null) {
			const leitFach1 = this.manager.faecher().get(fach.projektKursLeitfach1ID);
			if (leitFach1 !== null) {
				result += textLeitfach + " " + leitFach1.kuerzel + ": " + leitFach1.bezeichnung;
			}
		}
		if (fach.projektKursLeitfach2ID !== null) {
			const leitFach2 = this.manager.faecher().get(fach.projektKursLeitfach2ID);
			if (leitFach2 !== null) {
				if (result.length > 0) {
					result += "\n";
				}
				result += textLeitfach + " " + leitFach2.kuerzel + ": " + leitFach2.bezeichnung;
			}
		}
		return result;
	}


	/**
	 * Eine Map, welche zu einem Fach angibt, ob es sich um eine Fremdsprachen handelt oder nicht
	 */
	private readonly _mapFremdsprachen = computed<JavaMap<GostFach, boolean>>(() => {
		const map = new HashMap<GostFach, boolean>();
		for (const fach of this.alleFaecher) {
			map.put(fach, fach.istFremdsprache && (Fach.getBySchluesselOrDefault(fach.kuerzel).daten(this.manager.getSchuljahr())?.istFremdsprache ?? false));
		}
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
	private readonly _mapSprachbelegung = computed<JavaMap<GostFach, Sprachbelegung>>(() => {
		const map = new HashMap<GostFach, Sprachbelegung>();
		for (const fach of this.alleFaecher) {
			const sprach_kuerzel = Fach.getBySchluesselOrDefault(fach.kuerzel).daten(this.manager.getSchuljahr())?.kuerzel ?? null;
			if (sprach_kuerzel === null) {
				continue;
			}
			for (const belegung of this.manager.getSprachendaten().belegungen) {
				if (belegung.sprache === sprach_kuerzel) {
					map.put(fach, belegung);
				}
			}
		}
		return map;
	});

	/**
	 * Eine Map mit den Sprachprüfungen, welche dem zugehörigen Fach zugeordnet sind
	 */
	private readonly _mapSprachpruefungen = computed<JavaMap<GostFach, Sprachpruefung>>(() => {
		const map = new HashMap<GostFach, Sprachpruefung>();
		for (const fach of this.alleFaecher) {
			const sprach_kuerzel = Fach.getBySchluesselOrDefault(fach.kuerzel).daten(this.manager.getSchuljahr())?.kuerzel ?? null;
			if (sprach_kuerzel === null) {
				continue;
			}
			for (const pruefung of this.manager.getSprachendaten().pruefungen) {
				if ((pruefung.ersetzteSprache === sprach_kuerzel) && (SprachendatenUtils.istFeststellungspruefungEESAMSABestanden(pruefung))) {
					map.put(fach, pruefung);
				}
			}
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
		if (!this.istFremdspracheMoeglich(fach)) {
			return false;
		}
		const sprachbelegung = this._mapSprachbelegung.value.get(fach);
		if (sprachbelegung !== null) {
			const rf = sprachbelegung.reihenfolge ?? 0;
			const jg = sprachbelegung.belegungVonJahrgang ?? "";
			return (rf !== 0) && (jg !== "");
		}
		const sprachpruefung = this._mapSprachpruefungen.value.get(fach);
		if (sprachpruefung !== null) {
			return fach.istFremdSpracheNeuEinsetzend || SprachendatenUtils.istFeststellungspruefungEESAMSABestanden(sprachpruefung);
		}
		return false;
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
		if (!this.istFremdspracheMoeglich(fach)) {
			return 0;
		}
		const sprachbelegung = this._mapSprachbelegung.value.get(fach);
		if (sprachbelegung !== null) {
			return sprachbelegung.reihenfolge ?? 0;
		}
		const sprachpruefung = this._mapSprachpruefungen.value.get(fach);
		if (sprachpruefung !== null) {
			if (sprachpruefung.kannErstePflichtfremdspracheErsetzen) {
				return 1;
			}
			if (sprachpruefung.kannZweitePflichtfremdspracheErsetzen) {
				return 2;
			}
			if (sprachpruefung.kannWahlpflichtfremdspracheErsetzen) {
				return 3;
			}
		}
		return 0;
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
		if (!this.istFremdspracheMoeglich(fach)) {
			return "";
		}
		const sprachbelegung = this._mapSprachbelegung.value.get(fach);
		if (sprachbelegung !== null) {
			return sprachbelegung.belegungVonJahrgang ?? "";
		}
		const sprachpruefung = this._mapSprachpruefungen.value.get(fach);
		if (sprachpruefung !== null) {
			return "P";
		}
		return "";
	}

	/**
	 * Eine Map, welche zu einem Fach angibt, ob die Belegung einer Fremdsprache möglich ist oder nicht
	 */
	private readonly _mapFremdsprachenMoeglich = computed<JavaMap<GostFach, boolean>>(() => {
		const map = new HashMap<GostFach, boolean>();
		for (const fach of this.alleFaecher) {
			if (this.istFremdsprache(fach)) {
				const f = Fach.getBySchluesselOrDefault(fach.kuerzel).daten(this.manager.getSchuljahr())?.kuerzel ?? null;
				const istFortfuehrbar = SprachendatenUtils.istFortfuehrbareSpracheInGOSt(this.manager.getSprachendaten(), f);
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
	 * Prüft, ob die angegeben Fachbelegung über den Projektkurs als 5. Abiturfach belegt wurde.
	 *
	 * @param fachbelegung   die zu prüfende Fachbelegung
	 */
	public istAbi30ProjektkursAbiturfach5(fachbelegung: AbiturFachbelegung) {
		// Prüfe zunächst, ob das Fach nicht bereits selbst als Abiturfach gewählt wurde
		if (fachbelegung.abiturFach !== null) {
			return false;
		}

		// Prüfe dann, ob das potentiell als Referenzfach geeignet ist
		const hatBelegungReferenzfach = (this.manager.pruefeBelegung(fachbelegung, GostHalbjahr.EF1, GostHalbjahr.EF2) &&
			this.manager.pruefeBelegungMitSchriftlichkeit(fachbelegung, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11, GostHalbjahr.Q12));
		if (!hatBelegungReferenzfach) {
			return false;
		}

		// Prüfe nun den gewählten Projektkurs, ob dieser überhaupt eine Referenz auf dieses Fach hat
		const listPJK = this.manager.getRelevanteFachbelegungen(GostFachbereich.PROJEKTKURSE);
		if (listPJK.size() !== 1) {
			return false;
		}
		const projektkurs = listPJK.getFirst();
		return (projektkurs.abiturFach === 5) && (projektkurs.idReferenzfach === fachbelegung.fachID);
	}


	/**
	 * Prüft, ob das übergebene Fach ein Projektkurs ist und die Belegung aufgrund des 1. bzw. 2. Referenzfaches möglich ist.
	 * Wenn es möglich ist, dann wird die Belegung des Referenzfaches zuürckgegeben.
	 *
	 * @param fach     das Fach des Projektkurses
	 * @param nummer   die Nummer des zu prüfenden Referenzfaches (1 oder 2)
	 *
	 * @returns die Belegung des Referenzfaches oder null, wenn keine geeignete Referenzfachbelegung vorhanden ist
	 */
	public pruefeAbi30ProjektkursBelegungReferenzfachMoeglich(fach: GostFach, nummer: 1 | 2): AbiturFachbelegung | null {
		if (this.getFachgruppe(fach) !== Fachgruppe.FG_PX) {
			return null;
		}
		let belegungReferenzfach;
		if (nummer === 1) {
			belegungReferenzfach = (fach.projektKursLeitfach1ID === null) ? null : this.manager.getFachbelegungByID(fach.projektKursLeitfach1ID);
		} else {
			belegungReferenzfach = (fach.projektKursLeitfach2ID === null) ? null : this.manager.getFachbelegungByID(fach.projektKursLeitfach2ID);
		}
		const hatBelegungReferenzfach = (this.manager.pruefeBelegung(belegungReferenzfach, GostHalbjahr.EF1, GostHalbjahr.EF2) &&
			this.manager.pruefeBelegungMitSchriftlichkeit(belegungReferenzfach, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11, GostHalbjahr.Q12));
		if (!hatBelegungReferenzfach || !fach.istMoeglichQ21 || !fach.istMoeglichQ22
			|| this.hatDoppelbelegung(fach, GostHalbjahr.Q21) || this.hatDoppelbelegung(fach, GostHalbjahr.Q22)) {
			return null;
		}
		return belegungReferenzfach;
	}

	/**
	 * Eine Map, welche angibt, ob bei den Fächern eine Belegung in den einzelnen Jahrgängen möglich ist oder nicht.
	 */
	private readonly _istMoeglich = computed<HashMap2D<GostFach, GostHalbjahr, boolean>>(() => {
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
			} else if (this.getFachgruppe(fach) === Fachgruppe.FG_PX) {
				// Prüfe, ob die Vorraussetzungen für die Projektkursbelegungen erfüllt sind.
				map.put(fach, GostHalbjahr.EF1, false);
				map.put(fach, GostHalbjahr.EF2, false);
				if (this.isAbi30ff.value) { // experimenteller Code
					map.put(fach, GostHalbjahr.Q11, false);
					map.put(fach, GostHalbjahr.Q12, false);
					// Prüfe Belegung des Referenzfaches
					const referenzfachBelegungVorhanden = (this.pruefeAbi30ProjektkursBelegungReferenzfachMoeglich(fach, 1) !== null)
						|| (this.pruefeAbi30ProjektkursBelegungReferenzfachMoeglich(fach, 2) !== null);
					map.put(fach, GostHalbjahr.Q21, referenzfachBelegungVorhanden);
					map.put(fach, GostHalbjahr.Q22, referenzfachBelegungVorhanden);
				} else {
					map.put(fach, GostHalbjahr.Q11, (fach.istMoeglichQ11 && !this.hatDoppelbelegung(fach, GostHalbjahr.Q11)));
					map.put(fach, GostHalbjahr.Q12, (fach.istMoeglichQ12 && !this.hatDoppelbelegung(fach, GostHalbjahr.Q12)));
					map.put(fach, GostHalbjahr.Q21, (fach.istMoeglichQ21 && !this.hatDoppelbelegung(fach, GostHalbjahr.Q21)));
					map.put(fach, GostHalbjahr.Q22, (fach.istMoeglichQ22 && !this.hatDoppelbelegung(fach, GostHalbjahr.Q22)));
				}
			} else {
				const istNichtErsatz = (this.getFachgruppe(fach) !== Fachgruppe.FG_ME);
				map.put(fach, GostHalbjahr.EF1, (fach.istMoeglichEF1 && !this.hatDoppelbelegung(fach, GostHalbjahr.EF1) && istNichtErsatz));
				map.put(fach, GostHalbjahr.EF2, (fach.istMoeglichEF2 && !this.hatDoppelbelegung(fach, GostHalbjahr.EF2) && istNichtErsatz));
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
	public istMoeglich(fach: GostFach, halbjahr: GostHalbjahr): boolean {
		return this._istMoeglich.value.getOrNull(fach, halbjahr) ?? false;
	}

	/**
	 * Eine Map mit der Zuordnung der Möglichen Kursart für ein Abiturfach zu einem Fach.
	 */
	private readonly _abiMoeglicheKursart = computed<JavaMap<GostFach, GostKursart>>(() => {
		const map = new HashMap<GostFach, GostKursart>();
		for (const fach of this.alleFaecher) {
			const tmp = this.manager.getMoeglicheKursartAlsAbiturfach(fach.id);
			if (tmp !== null) {
				map.put(fach, tmp);
			}
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
	public istMoeglichAbi(fach: GostFach): boolean {
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
		if (halbjahr === undefined) {
			return;
		}
		if (this.istMoeglich(fach, halbjahr) && (!this.manager.istBewertet(halbjahr) || this.hatNote(fach, halbjahr))) {
			return;
		}
		const wahl = this.manager.getSchuelerFachwahl(fach.id);
		wahl.halbjahre[halbjahr.id] = null;
		await this.setWahl(fach.id, wahl);
	}


	/**
	 * Lösch-Methode für Spezialfall "Löschen einer nicht erlaubten Fachwahl" aus Abitur-Spalte zu dem übergebenen Fach
	 *
	 * @param fach   das Fach
	 */
	public async deleteFachwahlAbitur(fach: GostFach) {
		const wahl = this.manager.getSchuelerFachwahl(fach.id);
		wahl.abiturFach = null;
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
		if (this.istAbiturAb2030) {
			await this.uiStepper2030.stepperAbitur(fach);
		} else {
			await this.uiStepper2013.stepperAbitur(fach);
		}
	}


	/**
	 * Stepper für das Durchwandern der Referenzfächer bei einem Projektkurs-Fach.
	 *
	 * @param fach   das Projektkurs-Fach
	 *
	 * @returns -
	 */
	public async stepperReferenzfach(fach: GostFach) {
		if (this.istAbiturAb2030) {
			await this.uiStepper2030.stepperReferenzfach(fach);
		} else {
			await this.uiStepper2013.stepperReferenzfach(fach);
		}
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
	public async stepper(fach: GostFach, halbjahr: GostHalbjahr, modus?: LaufbahnplanungUiStepperMode) {
		if (this.istAbiturAb2030) {
			await this.uiStepper2030.stepper(fach, halbjahr, modus);
		} else {
			await this.uiStepper2013.stepper(fach, halbjahr, modus);
		}
	}

}
