import { JavaObject } from '../../../../java/lang/JavaObject';
import { GostFach } from '../../../../core/data/gost/GostFach';
import { GostAbiturFach } from '../../../../core/types/gost/GostAbiturFach';
import { AbiturFachbelegung } from '../../../../core/data/gost/AbiturFachbelegung';
import { ArrayList } from '../../../../java/util/ArrayList';
import { GostBelegpruefungsArt } from '../../../../core/abschluss/gost/GostBelegpruefungsArt';
import { GostBelegpruefung } from '../../../../core/abschluss/gost/GostBelegpruefung';
import { AbiturdatenManager } from '../../../../core/abschluss/gost/AbiturdatenManager';
import { GostFachManager } from '../../../../core/abschluss/gost/GostFachManager';
import { GostKursart } from '../../../../core/types/gost/GostKursart';
import { SprachendatenUtils } from '../../../../core/utils/schueler/SprachendatenUtils';
import { GostFachbereich } from '../../../../core/types/gost/GostFachbereich';
import { GostHalbjahr } from '../../../../core/types/gost/GostHalbjahr';
import { GostSchriftlichkeit } from '../../../../core/types/gost/GostSchriftlichkeit';
import type { List } from '../../../../java/util/List';
import { GostBelegungsfehler } from '../../../../core/abschluss/gost/GostBelegungsfehler';

export class Fremdsprachen extends GostBelegpruefung {

	private _fremdsprachen : List<AbiturFachbelegung> = new ArrayList();

	private _fremdsprachenNeu : List<AbiturFachbelegung> = new ArrayList();

	private _fremdsprachenFortgefuehrt : List<AbiturFachbelegung> = new ArrayList();

	private _biliSachfaecher : List<AbiturFachbelegung> = new ArrayList();

	/**
	 * Die Anzahl der durchgehenden bzw. potenziell durchgehenden Belegungen - nur schriftlich (für die Schwerpunktberechnung
	 *  - hier zählt auch ein bilinguales Sachfach, wo die Fremdsprache der Unterrichtsprache aus der Sek I nicht fortgeführt wurde)
	 */
	private _anzahlDurchgehendSchriftlich : number = 0;


	/**
	 * Erstellt eine neue Belegprüfung für dir Fremdsprachen.
	 *
	 * @param manager         der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public constructor(manager : AbiturdatenManager, pruefungsArt : GostBelegpruefungsArt) {
		super(manager, pruefungsArt);
	}

	protected init() : void {
		this._fremdsprachen = this.manager.getFachbelegungen(GostFachbereich.FREMDSPRACHE);
		this._fremdsprachenNeu = this.manager.filterFremdspracheNeuEinsetzend(this._fremdsprachen);
		this._fremdsprachenFortgefuehrt = this.manager.filterFremdspracheFortgefuehrt(this._fremdsprachen);
		this._biliSachfaecher = this.manager.getFachbelegungenBilingual();
		this._anzahlDurchgehendSchriftlich = 0;
	}

	protected pruefeEF1() : void {
		this.pruefeEF1Sprachenfolge();
		this.pruefeEF1Fremdsprache1();
		this.pruefeEF1FremdsprachenfolgeZweiteFremdsprache();
		this.pruefeEF1Schriftlichkeit();
		this.pruefeEF1AnzahlDurchgehenedeSprachen();
		this.pruefeEF1BilingualeSachfaecher();
		this.pruefeEF1BilingualenBildungsgang();
	}

	/**
	 * EF.1: Prüft bei der Sprachenfolge, ob eine gemäß Sprachenfolge fortgeführte
	 * Fremdsprache fehlerhafterweise als neu einsetzende Fremdsprache belegt wurde.
	 */
	private pruefeEF1Sprachenfolge() : void {
		if (this.manager.hatFortgefuehrteFremdspracheInSprachendaten(this._fremdsprachenNeu))
			this.addFehler(GostBelegungsfehler.FS_20);
		if (this.manager.hatNeuEinsetzendeFremdspracheInSprachendaten(this._fremdsprachenFortgefuehrt))
			this.addFehler(GostBelegungsfehler.FS_21);
		if (!SprachendatenUtils.hatSprachbelegung(this.manager.getSprachendaten(), "E"))
			this.addFehler(GostBelegungsfehler.FS_22_INFO);
	}

	/**
	 * Prüft, ob eine gültige Fremdsprachenbelegung mit den EF.1-Wahlen möglich ist.
	 */
	private pruefeEF1Fremdsprache1() : void {
		let gefundenFremdsprachenbelegung : boolean = false;
		let gefundenFortgefuehrteFremdsprachenbelegungOhneSprachenfolge : boolean = false;
		let gefundenFortgefuehrteFremdspracheAlsNeueinsetzende : boolean = false;
		let anzahlFortgefuehrteFremdsprachen : number = 0;
		let anzahlFortgefuehrteFremdsprachenEFBelegbar : number = 0;
		let anzahlFortgefuehrteFremdsprachenEFBelegbarFehlerMuendlich : number = 0;
		let anzahlFortgefuehrteFremdsprachenDurchgehendBelegbar : number = 0;
		let anzahlFortgefuehrteFremdsprachenDurchgehendBelegbarFehlerMuendlich : number = 0;
		const anzahlFortfuehrbareFremdsprachen : number = SprachendatenUtils.getFortfuehrbareSprachenInGOSt(this.manager.getSprachendaten()).size();
		for (const abiFachbelegung of this._fremdsprachenFortgefuehrt) {
			if (!this.manager.pruefeBelegung(abiFachbelegung, GostHalbjahr.EF1)) {
				continue;
			}
			gefundenFremdsprachenbelegung = true;
			const gostFach : GostFach | null = this.manager.getFach(abiFachbelegung);
			if (gostFach !== null && !JavaObject.equalsTranspiler(gostFach.kuerzel, (""))) {
				if (SprachendatenUtils.istFortfuehrbareSpracheInGOSt(this.manager.getSprachendaten(), gostFach.kuerzel.substring(0, 1))) {
					anzahlFortgefuehrteFremdsprachen += 1;
					if (this.manager.pruefeBelegungDurchgehendBelegbar(abiFachbelegung, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1)) {
						anzahlFortgefuehrteFremdsprachenDurchgehendBelegbar += 1;
					} else
						if (this.manager.pruefeBelegungDurchgehendBelegbar(abiFachbelegung, GostSchriftlichkeit.MUENDLICH, GostHalbjahr.EF1)) {
							anzahlFortgefuehrteFremdsprachenDurchgehendBelegbar += 1;
							anzahlFortgefuehrteFremdsprachenDurchgehendBelegbarFehlerMuendlich += 1;
						} else
							if ((gostFach.istMoeglichEF1) && (gostFach.istMoeglichEF2)) {
								anzahlFortgefuehrteFremdsprachenEFBelegbar += 1;
								if (this.manager.pruefeBelegungMitSchriftlichkeit(abiFachbelegung, GostSchriftlichkeit.MUENDLICH, GostHalbjahr.EF1)) {
									anzahlFortgefuehrteFremdsprachenEFBelegbarFehlerMuendlich += 1;
								}
							}
				} else {
					gefundenFortgefuehrteFremdsprachenbelegungOhneSprachenfolge = true;
					this.addFehler(GostBelegungsfehler.FS_23);
				}
			}
		}
		if ((anzahlFortgefuehrteFremdsprachenDurchgehendBelegbarFehlerMuendlich + anzahlFortgefuehrteFremdsprachenEFBelegbarFehlerMuendlich) > 0)
			this.addFehler(GostBelegungsfehler.FS_12);
		if ((anzahlFortgefuehrteFremdsprachen > 0) && (anzahlFortgefuehrteFremdsprachen === (anzahlFortgefuehrteFremdsprachenDurchgehendBelegbarFehlerMuendlich + anzahlFortgefuehrteFremdsprachenEFBelegbarFehlerMuendlich)))
			this.addFehler(GostBelegungsfehler.FS_16);
		if (anzahlFortgefuehrteFremdsprachenDurchgehendBelegbar > 0)
			return;
		let anzahlNeueinsetzendeFremdsprachenDurchgehendBelegbar : number = 0;
		let anzahlNeueinsetzendeFremdsprachenDurchgehendBelegbarFehlerMuendlich : number = 0;
		for (const abiFachbelegung of this._fremdsprachenNeu) {
			if (!this.manager.pruefeBelegung(abiFachbelegung, GostHalbjahr.EF1))
				continue;
			gefundenFremdsprachenbelegung = true;
			const gostFach : GostFach | null = this.manager.getFach(abiFachbelegung);
			if (gostFach !== null && !JavaObject.equalsTranspiler(gostFach.kuerzel, (""))) {
				if (!SprachendatenUtils.istFortfuehrbareSpracheInGOSt(this.manager.getSprachendaten(), gostFach.kuerzel.substring(0, 1))) {
					if (this.manager.pruefeBelegungDurchgehendBelegbar(abiFachbelegung, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1)) {
						anzahlNeueinsetzendeFremdsprachenDurchgehendBelegbar += 1;
					} else
						if (this.manager.pruefeBelegungDurchgehendBelegbar(abiFachbelegung, GostSchriftlichkeit.MUENDLICH, GostHalbjahr.EF1)) {
							anzahlNeueinsetzendeFremdsprachenDurchgehendBelegbar += 1;
							anzahlNeueinsetzendeFremdsprachenDurchgehendBelegbarFehlerMuendlich += 1;
						}
				} else {
					this.addFehler(GostBelegungsfehler.FS_20);
					gefundenFortgefuehrteFremdspracheAlsNeueinsetzende = true;
				}
			}
		}
		if (anzahlNeueinsetzendeFremdsprachenDurchgehendBelegbarFehlerMuendlich > 0)
			this.addFehler(GostBelegungsfehler.FS_12);
		if ((gefundenFremdsprachenbelegung && !(gefundenFortgefuehrteFremdsprachenbelegungOhneSprachenfolge || gefundenFortgefuehrteFremdspracheAlsNeueinsetzende)) && (anzahlFortgefuehrteFremdsprachenDurchgehendBelegbar + anzahlNeueinsetzendeFremdsprachenDurchgehendBelegbar === 0))
			this.addFehler(GostBelegungsfehler.FS_11);
		if (anzahlFortgefuehrteFremdsprachenEFBelegbar > 0) {
			if (anzahlNeueinsetzendeFremdsprachenDurchgehendBelegbar === 0)
				this.addFehler(GostBelegungsfehler.FS_10);
			return;
		}
		if (anzahlNeueinsetzendeFremdsprachenDurchgehendBelegbar === 0) {
			this.addFehler(GostBelegungsfehler.FS_18);
			return;
		}
		if ((anzahlFortgefuehrteFremdsprachenDurchgehendBelegbar + anzahlFortgefuehrteFremdsprachenEFBelegbar) === 0 && anzahlNeueinsetzendeFremdsprachenDurchgehendBelegbar > 0) {
			if (anzahlNeueinsetzendeFremdsprachenDurchgehendBelegbarFehlerMuendlich > 0) {
				this.addFehler(GostBelegungsfehler.FS_18);
			}
			if (SprachendatenUtils.hatSprachfeststellungspruefungAufEFNiveau(this.manager.getSprachendaten())) {
				this.addFehler(GostBelegungsfehler.FS_19_INFO);
			} else {
				if (anzahlFortfuehrbareFremdsprachen === 0) {
					this.addFehler(GostBelegungsfehler.FS_25);
				} else {
					this.addFehler(GostBelegungsfehler.FS_18);
					if (!SprachendatenUtils.hatZweiSprachenMitMin4JahrenDauerEndeSekI(this.manager.getSprachendaten()))
						this.addFehler(GostBelegungsfehler.FS_24);
				}
			}
		}
	}

	/**
	 * Prüft, ob eine zweite Fremdsprache in der Sek I vorhanden ist und prüft sonst auf eine neu
	 * einsetzende Fremdsprache.
	 */
	private pruefeEF1FremdsprachenfolgeZweiteFremdsprache() : void {
		if (SprachendatenUtils.hatZweiSprachenMitMin4JahrenDauerEndeSekI(this.manager.getSprachendaten()))
			return;
		if (SprachendatenUtils.hatEineSpracheMitMin4JahrenDauerEndeSekI(this.manager.getSprachendaten())) {
			if (this.manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(this._fremdsprachenNeu, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1))
				return;
			if (SprachendatenUtils.hatSpracheMit2JahrenDauerEndeSekI(this.manager.getSprachendaten())) {
				const zweiteFremdsprache : AbiturFachbelegung | null = this.manager.getSprachbelegung(SprachendatenUtils.getSpracheMit2JahrenDauerEndeSekI(this.manager.getSprachendaten()));
				if (!this.manager.pruefeBelegungMitSchriftlichkeitEinzeln(zweiteFremdsprache, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1))
					this.addFehler(GostBelegungsfehler.FS_13);
				return;
			}
		}
		if (!this.manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(this._fremdsprachenNeu, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1)) {
			this.addFehler(GostBelegungsfehler.FS_14);
		}
	}

	/**
	 * Prüft, ob alle Fremdsprachen in der EF.1 schriftlich belegt wurden.
	 */
	private pruefeEF1Schriftlichkeit() : void {
		if (this._fremdsprachen === null)
			return;
		for (const fachbelegung of this._fremdsprachen)
			if (this.manager.pruefeBelegungMitSchriftlichkeitEinzeln(fachbelegung, GostSchriftlichkeit.BELIEBIG, GostHalbjahr.EF1) && !this.manager.pruefeBelegungMitSchriftlichkeitEinzeln(fachbelegung, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1)) {
				this.addFehler(GostBelegungsfehler.FS_12);
				break;
			}
	}

	/**
	 * Zähle alle Fremdsprachen, die durchgehend schriftlich belegt wurden.
	 * Hierzu zählt auch die Unterrichtssprache eines bilingualen Sachfachs als zweite durchgehende
	 * Fremdsprache, sofern dieses durchgehende und schriftlich belegt werden kann.
	 */
	private pruefeEF1AnzahlDurchgehenedeSprachen() : void {
		const fremdsprachenDurchgehend : List<AbiturFachbelegung | null> | null = this.manager.filterBelegungenMitSchriftlichkeit(this.manager.filterDurchgehendBelegbar(this._fremdsprachen), GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1);
		this._anzahlDurchgehendSchriftlich = fremdsprachenDurchgehend.size();
		if (this._anzahlDurchgehendSchriftlich !== 1)
			return;
		const fsDurchgehend : GostFach | null = this.manager.getFach(fremdsprachenDurchgehend.get(0));
		if (fsDurchgehend === null)
			return;
		const fremdspracheDurchgehend : string | null = GostFachManager.getFremdsprache(fsDurchgehend);
		if (fremdspracheDurchgehend === null)
			return;
		const biliSachfaecherDurchgehendSchriftlich : List<AbiturFachbelegung | null> | null = this.manager.filterBelegungenMitSchriftlichkeit(this.manager.filterDurchgehendBelegbar(this._biliSachfaecher), GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1);
		for (const biliSachfach of biliSachfaecherDurchgehendSchriftlich) {
			const fach : GostFach | null = this.manager.getFach(biliSachfach);
			if ((fach === null) || (JavaObject.equalsTranspiler(fremdspracheDurchgehend, (fach.biliSprache))))
				continue;
			this._anzahlDurchgehendSchriftlich++;
			return;
		}
	}

	/**
	 * Prüft, ob die Bedingungen für die Wahl eines bilingualen Sachfaches erfüllt sind, sofern eines
	 * in der EF.1 belegt wurde.
	 */
	private pruefeEF1BilingualeSachfaecher() : void {
		if (this._biliSachfaecher === null)
			return;
		for (const biliSachfach of this._biliSachfaecher) {
			const fach : GostFach | null = this.manager.getFach(biliSachfach);
			if (fach === null)
				continue;
			const biliSprache : string | null = fach.biliSprache;
			if (!SprachendatenUtils.hatSprachbelegungInSekIMitDauer(this.manager.getSprachendaten(), biliSprache, 2)) {
				this.addFehler(GostBelegungsfehler.BIL_14);
				continue;
			}
			const fremdsprache : AbiturFachbelegung | null = this.manager.getSprachbelegung(biliSprache);
			if (this.manager.pruefeBelegungMitSchriftlichkeitEinzeln(fremdsprache, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1) || this.manager.pruefeBelegungMitSchriftlichkeitEinzeln(biliSachfach, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1))
				continue;
			this.addFehler(GostBelegungsfehler.BIL_4_INFO);
		}
	}

	/**
	 * Prüfe, ob die Bedingungen für den bilingualen Bildungsgang erfüllt sind, sofern ein solcher vom Schüler gewählt wurde.
	 */
	private pruefeEF1BilingualenBildungsgang() : void {
		const biligualeSprache : string | null = this.manager.getBiligualenBildungsgang();
		if (biligualeSprache === null)
			return;
		const biliSprache : AbiturFachbelegung | null = this.manager.getSprachbelegung(biligualeSprache);
		if (!this.manager.pruefeBelegungDurchgehendBelegbar(biliSprache, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1))
			this.addFehler(GostBelegungsfehler.BIL_10);
		if ((this._biliSachfaecher === null) || (this._biliSachfaecher.isEmpty())) {
			this.addFehler(GostBelegungsfehler.BIL_15);
			return;
		}
		if (this._biliSachfaecher.size() < 2)
			this.addFehler(GostBelegungsfehler.BIL_11_INFO);
	}

	protected pruefeGesamt() : void {
		this.pruefeGesamtSprachenfolge();
		this.pruefeGesamtFremdsprache1();
		this.pruefeGesamtFremdsprachenfolgeZweiteFremdsprache();
		this.pruefeGesamtSchriftlichkeit();
		this.pruefeGesamtAnzahlDurchgehenedeSprachen();
		this.pruefeGesamtBilingualeSachfaecher();
		this.pruefeGesamtBilingualenBildungsgang();
	}

	/**
	 * Gesamt: Prüft bei der Sprachenfolge, ob eine gemäß Sprachenfolge fortgeführte
	 * Fremdsprache fehlerhafterweise als neu einsetzende Fremdsprache belegt wurde.
	 */
	private pruefeGesamtSprachenfolge() : void {
		if (this.manager.hatFortgefuehrteFremdspracheInSprachendaten(this._fremdsprachenNeu))
			this.addFehler(GostBelegungsfehler.FS_20);
		if (this.manager.hatNeuEinsetzendeFremdspracheInSprachendaten(this._fremdsprachenFortgefuehrt))
			this.addFehler(GostBelegungsfehler.FS_21);
		if (!SprachendatenUtils.hatSprachbelegung(this.manager.getSprachendaten(), "E"))
			this.addFehler(GostBelegungsfehler.FS_22_INFO);
	}

	/**
	 * Prüft, ob eine gültige Fremdsprachenbelegung in Bezug auf eine durchgehende Belegung möglich ist.
	 */
	private pruefeGesamtFremdsprache1() : void {
		let anzahlFortgefuehrteFremdsprachenEFBelegt : number = 0;
		let anzahlFortgefuehrteFremdsprachenDurchgehendBelegt : number = 0;
		let anzahlFortgefuehrteFremdsprachenBelegtFehlerMuendlichEF : number = 0;
		const anzahlFortfuehrbareFremdsprachen : number = SprachendatenUtils.getFortfuehrbareSprachenInGOSt(this.manager.getSprachendaten()).size();
		for (const abiFachbelegung of this._fremdsprachenFortgefuehrt) {
			if (!this.manager.pruefeBelegung(abiFachbelegung, GostHalbjahr.EF1)) {
				continue;
			}
			const gostFach : GostFach | null = this.manager.getFach(abiFachbelegung);
			if (gostFach !== null && !JavaObject.equalsTranspiler(gostFach.kuerzel, (""))) {
				if (SprachendatenUtils.istFortfuehrbareSpracheInGOSt(this.manager.getSprachendaten(), gostFach.kuerzel.substring(0, 1))) {
					if (this.manager.pruefeBelegung(abiFachbelegung, GostHalbjahr.EF1, GostHalbjahr.EF2))
						anzahlFortgefuehrteFremdsprachenEFBelegt += 1;
					if (this.manager.pruefeBelegungMitSchriftlichkeit(abiFachbelegung, GostSchriftlichkeit.MUENDLICH, GostHalbjahr.EF1) || this.manager.pruefeBelegungMitSchriftlichkeit(abiFachbelegung, GostSchriftlichkeit.MUENDLICH, GostHalbjahr.EF2))
						anzahlFortgefuehrteFremdsprachenBelegtFehlerMuendlichEF += 1;
					if (this.manager.pruefeBelegung(abiFachbelegung, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22))
						anzahlFortgefuehrteFremdsprachenDurchgehendBelegt += 1;
				} else {
					this.addFehler(GostBelegungsfehler.FS_23);
				}
			}
		}
		if (anzahlFortgefuehrteFremdsprachenBelegtFehlerMuendlichEF > 0)
			this.addFehler(GostBelegungsfehler.FS_12);
		if ((anzahlFortgefuehrteFremdsprachenDurchgehendBelegt > 0) && (anzahlFortgefuehrteFremdsprachenDurchgehendBelegt === anzahlFortgefuehrteFremdsprachenBelegtFehlerMuendlichEF))
			this.addFehler(GostBelegungsfehler.FS_16);
		if (anzahlFortgefuehrteFremdsprachenDurchgehendBelegt > 0)
			return;
		let anzahlNeueinsetzendeFremdsprachenDurchgehendBelegt : number = 0;
		let anzahlNeueinsetzendeFremdsprachenBelegtFehlerMuendlichEF : number = 0;
		for (const abiFachbelegung of this._fremdsprachenNeu) {
			if (!this.manager.pruefeBelegung(abiFachbelegung, GostHalbjahr.EF1))
				continue;
			const gostFach : GostFach | null = this.manager.getFach(abiFachbelegung);
			if (gostFach !== null && !JavaObject.equalsTranspiler(gostFach.kuerzel, (""))) {
				if (!SprachendatenUtils.istFortfuehrbareSpracheInGOSt(this.manager.getSprachendaten(), gostFach.kuerzel.substring(0, 1))) {
					if (this.manager.pruefeBelegung(abiFachbelegung, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22)) {
						anzahlNeueinsetzendeFremdsprachenDurchgehendBelegt += 1;
					}
					if (this.manager.pruefeBelegungMitSchriftlichkeit(abiFachbelegung, GostSchriftlichkeit.MUENDLICH, GostHalbjahr.EF1) || this.manager.pruefeBelegungMitSchriftlichkeit(abiFachbelegung, GostSchriftlichkeit.MUENDLICH, GostHalbjahr.EF2)) {
						anzahlNeueinsetzendeFremdsprachenBelegtFehlerMuendlichEF += 1;
					}
				} else {
					this.addFehler(GostBelegungsfehler.FS_20);
				}
			}
		}
		if (anzahlNeueinsetzendeFremdsprachenBelegtFehlerMuendlichEF > 0) {
			this.addFehler(GostBelegungsfehler.FS_12);
		}
		if (anzahlNeueinsetzendeFremdsprachenDurchgehendBelegt > 0) {
			if (SprachendatenUtils.hatSprachfeststellungspruefungAufEFNiveau(this.manager.getSprachendaten())) {
				this.addFehler(GostBelegungsfehler.FS_19_INFO);
				return;
			}
			if (anzahlFortgefuehrteFremdsprachenEFBelegt === 0) {
				this.addFehler(GostBelegungsfehler.FS_10);
				if (anzahlFortfuehrbareFremdsprachen === 0) {
					this.addFehler(GostBelegungsfehler.FS_25);
				} else {
					if (!SprachendatenUtils.hatZweiSprachenMitMin4JahrenDauerEndeSekI(this.manager.getSprachendaten()))
						this.addFehler(GostBelegungsfehler.FS_24);
				}
			}
		} else {
			if (SprachendatenUtils.hatSprachfeststellungspruefungAufEFNiveau(this.manager.getSprachendaten())) {
				this.addFehler(GostBelegungsfehler.FS_18);
			} else {
				this.addFehler(GostBelegungsfehler.FS_10);
			}
		}
	}

	/**
	 * Prüft, ob eine zweite Fremdsprache in der Sek I vorhanden ist und prüft sonst auf eine neu
	 * einsetzende Fremdsprache.
	 */
	private pruefeGesamtFremdsprachenfolgeZweiteFremdsprache() : void {
		if (SprachendatenUtils.hatZweiSprachenMitMin4JahrenDauerEndeSekI(this.manager.getSprachendaten()))
			return;
		if (SprachendatenUtils.hatEineSpracheMitMin4JahrenDauerEndeSekI(this.manager.getSprachendaten())) {
			if (this.manager.pruefeBelegungExistiert(this._fremdsprachenNeu, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22))
				return;
			if (SprachendatenUtils.hatSpracheMit2JahrenDauerEndeSekI(this.manager.getSprachendaten())) {
				const zweiteFremdsprache : AbiturFachbelegung | null = this.manager.getSprachbelegung(SprachendatenUtils.getSpracheMit2JahrenDauerEndeSekI(this.manager.getSprachendaten()));
				if (!this.manager.pruefeBelegungMitSchriftlichkeit(zweiteFremdsprache, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1, GostHalbjahr.EF2))
					this.addFehler(GostBelegungsfehler.FS_13);
				return;
			}
		}
		if (!this.manager.pruefeBelegungExistiert(this._fremdsprachenNeu, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22)) {
			this.addFehler(GostBelegungsfehler.FS_14);
		}
	}

	/**
	 * Prüft, ob eine gültige Fremdsprachenbelegung in Bezug auf die Schriftlichkeit und LK-Wahl vorhanden ist.
	 */
	private pruefeGesamtSchriftlichkeit() : void {
		if (this.manager.pruefeBelegungExistiertHatMindestensEinmalSchriftlichkeit(this._fremdsprachenNeu, GostSchriftlichkeit.MUENDLICH, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21))
			this.addFehler(GostBelegungsfehler.FS_15);
		if (this.manager.pruefeBelegungExistiertHatMindestensEinmalKursart(this._fremdsprachenNeu, GostKursart.LK, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22))
			this.addFehler(GostBelegungsfehler.FS_17);
		if (this.manager.pruefeBelegungExistiertErfuelltNichtFallsBelegt(this._fremdsprachenFortgefuehrt, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1, GostHalbjahr.EF2))
			this.addFehler(GostBelegungsfehler.FS_12);
		if (this.manager.pruefeBelegungDurchgehendBelegtExistiert(this._fremdsprachenFortgefuehrt, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21))
			return;
		if (!this.manager.pruefeBelegungDurchgehendBelegtExistiert(this._fremdsprachenNeu, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21)) {
			this.addFehler(GostBelegungsfehler.FS_11);
			return;
		}
		if (SprachendatenUtils.hatSprachfeststellungspruefungAufEFNiveau(this.manager.getSprachendaten()) && this.manager.pruefeBelegungExistiertMitSchriftlichkeit(this._fremdsprachenFortgefuehrt, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1, GostHalbjahr.EF2))
			return;
		if (SprachendatenUtils.hatSprachfeststellungspruefungAufEFNiveau(this.manager.getSprachendaten()) && this.manager.pruefeBelegungDurchgehendBelegtExistiert(this._fremdsprachenNeu, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21))
			return;
		if (!this.manager.pruefeBelegungExistiertMitSchriftlichkeit(this._fremdsprachenFortgefuehrt, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1, GostHalbjahr.EF2))
			this.addFehler(GostBelegungsfehler.FS_16);
	}

	/**
	 * Zähle alle Fremdsprachen, die durchgehend schriftlich belegt wurden.
	 * Hierzu zählt auch die Unterrichtssprache eines bilingualen Sachfachs als zweite durchgehende
	 * Fremdsprache, sofern dieses durchgehend und schriftlich belegt wurde.
	 */
	private pruefeGesamtAnzahlDurchgehenedeSprachen() : void {
		const fremdsprachenDurchgehend : List<AbiturFachbelegung> = this.manager.filterBelegungen(this._fremdsprachen, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22);
		const fremdsprachenDurchgehendSchriftlich : List<AbiturFachbelegung> = this.manager.filterBelegungenMitSchriftlichkeit(fremdsprachenDurchgehend, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21);
		this._anzahlDurchgehendSchriftlich = fremdsprachenDurchgehendSchriftlich.size();
		if (this._anzahlDurchgehendSchriftlich !== 1)
			return;
		const fsDurchgehend : GostFach | null = this.manager.getFach(fremdsprachenDurchgehendSchriftlich.get(0));
		if (fsDurchgehend === null)
			return;
		const fremdspracheDurchgehend : string | null = GostFachManager.getFremdsprache(fsDurchgehend);
		if (fremdspracheDurchgehend === null)
			return;
		const biliSachfaecherDurchgehend : List<AbiturFachbelegung> = this.manager.filterBelegungen(this._biliSachfaecher, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22);
		const biliSachfaecherDurchgehendSchriftlich : List<AbiturFachbelegung> = this.manager.filterBelegungenMitSchriftlichkeit(biliSachfaecherDurchgehend, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21);
		for (const biliSachfach of biliSachfaecherDurchgehendSchriftlich) {
			const fach : GostFach | null = this.manager.getFach(biliSachfach);
			if ((fach === null) || (JavaObject.equalsTranspiler(fremdspracheDurchgehend, (fach.biliSprache))))
				continue;
			this._anzahlDurchgehendSchriftlich++;
			return;
		}
	}

	/**
	 * Prüft, ob die Bedingungen für die Wahl eines bilingualen Sachfaches erfüllt sind, sofern eines
	 * belegt wurde.
	 */
	private pruefeGesamtBilingualeSachfaecher() : void {
		if (this._biliSachfaecher === null)
			return;
		for (const biliSachfach of this._biliSachfaecher) {
			const biliFach : GostFach | null = this.manager.getFach(biliSachfach);
			if ((biliFach === null) || (!SprachendatenUtils.hatSprachbelegungInSekIMitDauer(this.manager.getSprachendaten(), biliFach.biliSprache, 2)))
				this.addFehler(GostBelegungsfehler.BIL_14);
		}
	}

	/**
	 * Prüfe, ob die Bedingungen für ein bilinguales Abitur erfüllt sind, sofern ein solches vom
	 * Schüler gewählt wurde.
	 */
	private pruefeGesamtBilingualenBildungsgang() : void {
		const biligualeSprache : string | null = this.manager.getBiligualenBildungsgang();
		if (biligualeSprache === null)
			return;
		const biliSprache : AbiturFachbelegung | null = this.manager.getSprachbelegung(biligualeSprache);
		if ((!this.manager.pruefeBelegungMitSchriftlichkeit(biliSprache, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1, GostHalbjahr.EF2) || (!this.manager.pruefeBelegungMitKursart(biliSprache, GostKursart.LK, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22))))
			this.addFehler(GostBelegungsfehler.BIL_10);
		const biliSachfaecherEF : List<AbiturFachbelegung | null> | null = this.manager.filterBelegungen(this._biliSachfaecher, GostHalbjahr.EF1, GostHalbjahr.EF2);
		if (biliSachfaecherEF.isEmpty()) {
			this.addFehler(GostBelegungsfehler.BIL_15);
			return;
		}
		if (biliSachfaecherEF.size() < 2)
			this.addFehler(GostBelegungsfehler.BIL_11_INFO);
		let hatBiliSachfaecherDurchgehendSchriftlich : boolean = false;
		if (this._biliSachfaecher !== null) {
			for (const fach of this._biliSachfaecher) {
				if (this.manager.pruefeDurchgaengigkeitSchriftlich(fach)) {
					hatBiliSachfaecherDurchgehendSchriftlich = true;
					break;
				}
			}
		}
		if (!hatBiliSachfaecherDurchgehendSchriftlich)
			this.addFehler(GostBelegungsfehler.BIL_12);
		if (!this.manager.pruefeExistiertAbiFach(this._biliSachfaecher, GostAbiturFach.AB3, GostAbiturFach.AB4))
			this.addFehler(GostBelegungsfehler.BIL_13);
	}

	/**
	 * Gibt die Anzahl der durchgehend schriftlich belegten bzw. belegbaren Fremdsprachen zurück.
	 * Durchgehend schriftlich bedeutet, dass das Fach mind. von EF.1 bis Q2.1 schriftlich belegt wurde.
	 * Hierfür kommen Fremdsprachen und ggf. ein bilinguales Sachfach infrage, dessen Unterrichtssprache
	 * nicht durchgehend schriftlich belegt.
	 *
	 * @return die Anzahl der durchgehend schriftlich belegten bzw. belegbaren Fremdsprachen zurück.
	 */
	public getAnzahlDurchgehendSchritflichBelegt() : number {
		return this._anzahlDurchgehendSchriftlich;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.GostBelegpruefung', 'de.svws_nrw.core.abschluss.gost.belegpruefung.Fremdsprachen'].includes(name);
	}

}

export function cast_de_svws_nrw_core_abschluss_gost_belegpruefung_Fremdsprachen(obj : unknown) : Fremdsprachen {
	return obj as Fremdsprachen;
}
