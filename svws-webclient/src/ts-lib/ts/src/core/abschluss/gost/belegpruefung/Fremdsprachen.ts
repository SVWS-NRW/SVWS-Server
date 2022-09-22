import { JavaObject, cast_java_lang_Object } from '../../../../java/lang/JavaObject';
import { SprachendatenManager, cast_de_nrw_schule_svws_core_SprachendatenManager } from '../../../../core/SprachendatenManager';
import { GostFach, cast_de_nrw_schule_svws_core_data_gost_GostFach } from '../../../../core/data/gost/GostFach';
import { GostAbiturFach, cast_de_nrw_schule_svws_core_types_gost_GostAbiturFach } from '../../../../core/types/gost/GostAbiturFach';
import { AbiturFachbelegung, cast_de_nrw_schule_svws_core_data_gost_AbiturFachbelegung } from '../../../../core/data/gost/AbiturFachbelegung';
import { GostBelegpruefungsArt, cast_de_nrw_schule_svws_core_abschluss_gost_GostBelegpruefungsArt } from '../../../../core/abschluss/gost/GostBelegpruefungsArt';
import { GostBelegpruefung, cast_de_nrw_schule_svws_core_abschluss_gost_GostBelegpruefung } from '../../../../core/abschluss/gost/GostBelegpruefung';
import { AbiturdatenManager, cast_de_nrw_schule_svws_core_abschluss_gost_AbiturdatenManager } from '../../../../core/abschluss/gost/AbiturdatenManager';
import { JavaString, cast_java_lang_String } from '../../../../java/lang/JavaString';
import { GostFachManager, cast_de_nrw_schule_svws_core_abschluss_gost_GostFachManager } from '../../../../core/abschluss/gost/GostFachManager';
import { GostKursart, cast_de_nrw_schule_svws_core_types_gost_GostKursart } from '../../../../core/types/gost/GostKursart';
import { GostFachbereich, cast_de_nrw_schule_svws_core_types_gost_GostFachbereich } from '../../../../core/types/gost/GostFachbereich';
import { GostHalbjahr, cast_de_nrw_schule_svws_core_types_gost_GostHalbjahr } from '../../../../core/types/gost/GostHalbjahr';
import { GostSchriftlichkeit, cast_de_nrw_schule_svws_core_types_gost_GostSchriftlichkeit } from '../../../../core/types/gost/GostSchriftlichkeit';
import { List, cast_java_util_List } from '../../../../java/util/List';
import { Vector, cast_java_util_Vector } from '../../../../java/util/Vector';
import { GostBelegungsfehler, cast_de_nrw_schule_svws_core_abschluss_gost_GostBelegungsfehler } from '../../../../core/abschluss/gost/GostBelegungsfehler';

export class Fremdsprachen extends GostBelegpruefung {

	private fremdsprachen : List<AbiturFachbelegung> = new Vector();

	private fremdsprachen_neu : List<AbiturFachbelegung> = new Vector();

	private fremdsprachen_fortgefuehrt : List<AbiturFachbelegung> = new Vector();

	private biliSachfaecher : List<AbiturFachbelegung> = new Vector();

	private anzahl_schriftlich_durchgehend : number = 0;


	/**
	 * Erstellt eine neue Belegprüfung für dir Fremdsprachen.
	 *
	 * @param manager         der Daten-Manager für die Abiturdaten
	 * @param pruefungs_art   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public constructor(manager : AbiturdatenManager, pruefungs_art : GostBelegpruefungsArt) {
		super(manager, pruefungs_art);
	}

	protected init() : void {
		this.fremdsprachen = this.manager.getFachbelegungen(GostFachbereich.FREMDSPRACHE);
		this.fremdsprachen_neu = this.manager.filterFremdspracheNeuEinsetzend(this.fremdsprachen);
		this.fremdsprachen_fortgefuehrt = this.manager.filterFremdspracheFortgefuehrt(this.fremdsprachen);
		this.biliSachfaecher = this.manager.getFachbelegungenBilingual();
		this.anzahl_schriftlich_durchgehend = 0;
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
		if (this.manager.hatFortgefuehrteFremdspracheInSprachendaten(this.fremdsprachen_neu)) 
			this.addFehler(GostBelegungsfehler.FS_20);
		if (this.manager.hatNeuEinsetzendeFremdspracheInSprachendaten(this.fremdsprachen_fortgefuehrt)) 
			this.addFehler(GostBelegungsfehler.FS_21);
		if (!SprachendatenManager.hatSprachbelegung(this.manager.getSprachendaten(), "E")) 
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
		let anzahlFortfuehrbareFremdsprachen : number = SprachendatenManager.getFortfuehrbareSprachenInGOSt(this.manager.getSprachendaten()).size();
		for (let abiFachbelegung of this.fremdsprachen_fortgefuehrt) {
			if (!this.manager.pruefeBelegung(abiFachbelegung, GostHalbjahr.EF1)) {
				continue;
			}
			gefundenFremdsprachenbelegung = true;
			let gostFach : GostFach | null = this.manager.getFach(abiFachbelegung);
			if (gostFach !== null && !JavaObject.equalsTranspiler(gostFach.kuerzel, (""))) {
				if (SprachendatenManager.istFortfuehrbareSpracheInGOSt(this.manager.getSprachendaten(), gostFach.kuerzel.substring(0, 1))) {
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
		if ((anzahlFortgefuehrteFremdsprachenDurchgehendBelegbarFehlerMuendlich + anzahlFortgefuehrteFremdsprachenEFBelegbarFehlerMuendlich) > 0) {
			this.addFehler(GostBelegungsfehler.FS_12);
		}
		if ((anzahlFortgefuehrteFremdsprachen > 0) && (anzahlFortgefuehrteFremdsprachen === (anzahlFortgefuehrteFremdsprachenDurchgehendBelegbarFehlerMuendlich + anzahlFortgefuehrteFremdsprachenEFBelegbarFehlerMuendlich))) {
			this.addFehler(GostBelegungsfehler.FS_16);
		}
		if (anzahlFortgefuehrteFremdsprachenDurchgehendBelegbar > 0) {
			return;
		}
		let anzahlNeueinsetzendeFremdsprachenDurchgehendBelegbar : number = 0;
		let anzahlNeueinsetzendeFremdsprachenDurchgehendBelegbarFehlerMuendlich : number = 0;
		for (let abiFachbelegung of this.fremdsprachen_neu) {
			if (!this.manager.pruefeBelegung(abiFachbelegung, GostHalbjahr.EF1)) {
				continue;
			}
			gefundenFremdsprachenbelegung = true;
			let gostFach : GostFach | null = this.manager.getFach(abiFachbelegung);
			if (gostFach !== null && !JavaObject.equalsTranspiler(gostFach.kuerzel, (""))) {
				if (!SprachendatenManager.istFortfuehrbareSpracheInGOSt(this.manager.getSprachendaten(), gostFach.kuerzel.substring(0, 1))) {
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
		if (anzahlNeueinsetzendeFremdsprachenDurchgehendBelegbarFehlerMuendlich > 0) {
			this.addFehler(GostBelegungsfehler.FS_12);
		}
		if ((gefundenFremdsprachenbelegung && !(gefundenFortgefuehrteFremdsprachenbelegungOhneSprachenfolge || gefundenFortgefuehrteFremdspracheAlsNeueinsetzende)) && (anzahlFortgefuehrteFremdsprachenDurchgehendBelegbar + anzahlNeueinsetzendeFremdsprachenDurchgehendBelegbar === 0)) {
			this.addFehler(GostBelegungsfehler.FS_11);
		}
		if (anzahlFortgefuehrteFremdsprachenEFBelegbar > 0) {
			if (anzahlNeueinsetzendeFremdsprachenDurchgehendBelegbar === 0) {
				this.addFehler(GostBelegungsfehler.FS_10);
			}
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
			if (this.manager.hatMuttersprachenPruefungEndeEF()) {
				this.addFehler(GostBelegungsfehler.FS_19_INFO);
			} else {
				if (anzahlFortfuehrbareFremdsprachen === 0) {
					this.addFehler(GostBelegungsfehler.FS_25);
				} else {
					this.addFehler(GostBelegungsfehler.FS_18);
					if (!SprachendatenManager.hatZweiSprachenMitMin4JahrenDauerEndeSekI(this.manager.getSprachendaten())) 
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
		if (SprachendatenManager.hatZweiSprachenMitMin4JahrenDauerEndeSekI(this.manager.getSprachendaten())) 
			return;
		if (SprachendatenManager.hatEineSpracheMitMin4JahrenDauerEndeSekI(this.manager.getSprachendaten())) {
			if (this.manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(this.fremdsprachen_neu, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1)) 
				return;
			if (SprachendatenManager.hatSpracheMit2JahrenDauerEndeSekI(this.manager.getSprachendaten())) {
				let zweiteFremdsprache : AbiturFachbelegung | null = this.manager.getSprachbelegung(SprachendatenManager.getSpracheMit2JahrenDauerEndeSekI(this.manager.getSprachendaten()));
				if (!this.manager.pruefeBelegungMitSchriftlichkeitEinzeln(zweiteFremdsprache, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1)) 
					this.addFehler(GostBelegungsfehler.FS_13);
				return;
			}
		}
		if (!this.manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(this.fremdsprachen_neu, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1)) {
			this.addFehler(GostBelegungsfehler.FS_14);
		}
	}

	/**
	 * Prüft, ob alle Fremdsprachen in der EF.1 schriftlich belegt wurden.
	 */
	private pruefeEF1Schriftlichkeit() : void {
		if (this.fremdsprachen === null) 
			return;
		for (let fachbelegung of this.fremdsprachen) 
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
		let fremdsprachenDurchgehend : List<AbiturFachbelegung | null> | null = this.manager.filterBelegungenMitSchriftlichkeit(this.manager.filterDurchgehendBelegbar(this.fremdsprachen), GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1);
		this.anzahl_schriftlich_durchgehend = fremdsprachenDurchgehend.size();
		if (this.anzahl_schriftlich_durchgehend !== 1) 
			return;
		let fsDurchgehend : GostFach | null = this.manager.getFach(fremdsprachenDurchgehend.get(0));
		if (fsDurchgehend === null) 
			return;
		let fremdspracheDurchgehend : String | null = GostFachManager.getFremdsprache(fsDurchgehend);
		if (fremdspracheDurchgehend === null) 
			return;
		let biliSachfaecherDurchgehendSchriftlich : List<AbiturFachbelegung | null> | null = this.manager.filterBelegungenMitSchriftlichkeit(this.manager.filterDurchgehendBelegbar(this.biliSachfaecher), GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1);
		for (let biliSachfach of biliSachfaecherDurchgehendSchriftlich) {
			let fach : GostFach | null = this.manager.getFach(biliSachfach);
			if ((fach === null) || (JavaObject.equalsTranspiler(fremdspracheDurchgehend, (fach.biliSprache)))) 
				continue;
			this.anzahl_schriftlich_durchgehend++;
			return;
		}
	}

	/**
	 * Prüft, ob die Bedingungen für die Wahl eines bilingualen Sachfaches erfüllt sind, sofern eines
	 * in der EF.1 belegt wurde.
	 */
	private pruefeEF1BilingualeSachfaecher() : void {
		if (this.biliSachfaecher === null) 
			return;
		for (let biliSachfach of this.biliSachfaecher) {
			let fach : GostFach | null = this.manager.getFach(biliSachfach);
			if (fach === null) 
				continue;
			let biliSprache : String | null = fach.biliSprache;
			if (!SprachendatenManager.hatSprachbelegungInSekIMitDauer(this.manager.getSprachendaten(), biliSprache, 2)) {
				this.addFehler(GostBelegungsfehler.BIL_14);
				continue;
			}
			let fremdsprache : AbiturFachbelegung | null = this.manager.getSprachbelegung(biliSprache);
			if (this.manager.pruefeBelegungMitSchriftlichkeitEinzeln(fremdsprache, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1) || this.manager.pruefeBelegungMitSchriftlichkeitEinzeln(biliSachfach, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1)) 
				continue;
			this.addFehler(GostBelegungsfehler.BIL_4_INFO);
		}
	}

	/**
	 * Prüfe, ob die Bedingungen für den bilingualen Bildungsgang erfüllt sind, sofern ein solcher vom Schüler gewählt wurde.
	 */
	private pruefeEF1BilingualenBildungsgang() : void {
		let biligualeSprache : String | null = this.manager.getBiligualenBildungsgang();
		if (biligualeSprache === null) 
			return;
		let biliSprache : AbiturFachbelegung | null = this.manager.getSprachbelegung(biligualeSprache);
		if (!this.manager.pruefeBelegungDurchgehendBelegbar(biliSprache, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1)) 
			this.addFehler(GostBelegungsfehler.BIL_10);
		if ((this.biliSachfaecher === null) || (this.biliSachfaecher.size() < 1)) {
			this.addFehler(GostBelegungsfehler.BIL_15);
			return;
		}
		if (this.biliSachfaecher.size() < 2) 
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
		if (this.manager.hatFortgefuehrteFremdspracheInSprachendaten(this.fremdsprachen_neu)) 
			this.addFehler(GostBelegungsfehler.FS_20);
		if (this.manager.hatNeuEinsetzendeFremdspracheInSprachendaten(this.fremdsprachen_fortgefuehrt)) 
			this.addFehler(GostBelegungsfehler.FS_21);
		if (!SprachendatenManager.hatSprachbelegung(this.manager.getSprachendaten(), "E")) 
			this.addFehler(GostBelegungsfehler.FS_22_INFO);
	}

	/**
	 * Prüft, ob eine gültige Fremdsprachenbelegung in Bezug auf eine durchgehende Belegung möglich ist.
	 */
	private pruefeGesamtFremdsprache1() : void {
		let anzahlFortgefuehrteFremdsprachenEFBelegt : number = 0;
		let anzahlFortgefuehrteFremdsprachenDurchgehendBelegt : number = 0;
		let anzahlFortgefuehrteFremdsprachenBelegtFehlerMuendlichEF : number = 0;
		let anzahlFortfuehrbareFremdsprachen : number = SprachendatenManager.getFortfuehrbareSprachenInGOSt(this.manager.getSprachendaten()).size();
		for (let abiFachbelegung of this.fremdsprachen_fortgefuehrt) {
			if (!this.manager.pruefeBelegung(abiFachbelegung, GostHalbjahr.EF1)) {
				continue;
			}
			let gostFach : GostFach | null = this.manager.getFach(abiFachbelegung);
			if (gostFach !== null && !JavaObject.equalsTranspiler(gostFach.kuerzel, (""))) {
				if (SprachendatenManager.istFortfuehrbareSpracheInGOSt(this.manager.getSprachendaten(), gostFach.kuerzel.substring(0, 1))) {
					if (this.manager.pruefeBelegung(abiFachbelegung, GostHalbjahr.EF1, GostHalbjahr.EF2)) {
						anzahlFortgefuehrteFremdsprachenEFBelegt += 1;
					}
					if (this.manager.pruefeBelegungMitSchriftlichkeit(abiFachbelegung, GostSchriftlichkeit.MUENDLICH, GostHalbjahr.EF1) || this.manager.pruefeBelegungMitSchriftlichkeit(abiFachbelegung, GostSchriftlichkeit.MUENDLICH, GostHalbjahr.EF2)) {
						anzahlFortgefuehrteFremdsprachenBelegtFehlerMuendlichEF += 1;
					}
					if (this.manager.pruefeBelegung(abiFachbelegung, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22)) {
						anzahlFortgefuehrteFremdsprachenDurchgehendBelegt += 1;
					}
				} else {
					this.addFehler(GostBelegungsfehler.FS_23);
				}
			}
		}
		if (anzahlFortgefuehrteFremdsprachenBelegtFehlerMuendlichEF > 0) {
			this.addFehler(GostBelegungsfehler.FS_12);
		}
		if ((anzahlFortgefuehrteFremdsprachenDurchgehendBelegt > 0) && (anzahlFortgefuehrteFremdsprachenDurchgehendBelegt === anzahlFortgefuehrteFremdsprachenBelegtFehlerMuendlichEF)) {
			this.addFehler(GostBelegungsfehler.FS_16);
		}
		if (anzahlFortgefuehrteFremdsprachenDurchgehendBelegt > 0) {
			return;
		}
		let anzahlNeueinsetzendeFremdsprachenDurchgehendBelegt : number = 0;
		let anzahlNeueinsetzendeFremdsprachenBelegtFehlerMuendlichEF : number = 0;
		for (let abiFachbelegung of this.fremdsprachen_neu) {
			if (!this.manager.pruefeBelegung(abiFachbelegung, GostHalbjahr.EF1)) {
				continue;
			}
			let gostFach : GostFach | null = this.manager.getFach(abiFachbelegung);
			if (gostFach !== null && !JavaObject.equalsTranspiler(gostFach.kuerzel, (""))) {
				if (!SprachendatenManager.istFortfuehrbareSpracheInGOSt(this.manager.getSprachendaten(), gostFach.kuerzel.substring(0, 1))) {
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
			if (this.manager.hatMuttersprachenPruefungEndeEF()) {
				this.addFehler(GostBelegungsfehler.FS_19_INFO);
				return;
			}
			if (anzahlFortgefuehrteFremdsprachenEFBelegt === 0) {
				this.addFehler(GostBelegungsfehler.FS_10);
				if (anzahlFortfuehrbareFremdsprachen === 0) {
					this.addFehler(GostBelegungsfehler.FS_25);
				} else {
					if (!SprachendatenManager.hatZweiSprachenMitMin4JahrenDauerEndeSekI(this.manager.getSprachendaten())) 
						this.addFehler(GostBelegungsfehler.FS_24);
				}
			}
		} else {
			if (this.manager.hatMuttersprachenPruefungEndeEF()) {
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
		if (SprachendatenManager.hatZweiSprachenMitMin4JahrenDauerEndeSekI(this.manager.getSprachendaten())) 
			return;
		if (SprachendatenManager.hatEineSpracheMitMin4JahrenDauerEndeSekI(this.manager.getSprachendaten())) {
			if (this.manager.pruefeBelegungExistiert(this.fremdsprachen_neu, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22)) 
				return;
			if (SprachendatenManager.hatSpracheMit2JahrenDauerEndeSekI(this.manager.getSprachendaten())) {
				let zweiteFremdsprache : AbiturFachbelegung | null = this.manager.getSprachbelegung(SprachendatenManager.getSpracheMit2JahrenDauerEndeSekI(this.manager.getSprachendaten()));
				if (!this.manager.pruefeBelegungMitSchriftlichkeit(zweiteFremdsprache, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1, GostHalbjahr.EF2)) 
					this.addFehler(GostBelegungsfehler.FS_13);
				return;
			}
		}
		if (!this.manager.pruefeBelegungExistiert(this.fremdsprachen_neu, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22)) {
			this.addFehler(GostBelegungsfehler.FS_14);
		}
	}

	/**
	 * Prüft, ob eine gültige Fremdsprachenbelegung in Bezug auf die Schriftlichkeit und LK-Wahl vorhanden ist.
	 */
	private pruefeGesamtSchriftlichkeit() : void {
		if (this.manager.pruefeBelegungExistiertHatMindestensEinmalSchriftlichkeit(this.fremdsprachen_neu, GostSchriftlichkeit.MUENDLICH, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21)) 
			this.addFehler(GostBelegungsfehler.FS_15);
		if (this.manager.pruefeBelegungExistiertHatMindestensEinmalKursart(this.fremdsprachen_neu, GostKursart.LK, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22)) 
			this.addFehler(GostBelegungsfehler.FS_17);
		if (this.manager.pruefeBelegungExistiertErfuelltNichtFallsBelegt(this.fremdsprachen_fortgefuehrt, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1, GostHalbjahr.EF2)) 
			this.addFehler(GostBelegungsfehler.FS_12);
		if (this.manager.pruefeBelegungDurchgehendBelegtExistiert(this.fremdsprachen_fortgefuehrt, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21)) 
			return;
		if (!this.manager.pruefeBelegungDurchgehendBelegtExistiert(this.fremdsprachen_neu, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21)) {
			this.addFehler(GostBelegungsfehler.FS_11);
			return;
		}
		if (this.manager.hatMuttersprachenPruefungEndeEF() && this.manager.pruefeBelegungExistiertMitSchriftlichkeit(this.fremdsprachen_fortgefuehrt, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1, GostHalbjahr.EF2)) 
			return;
		if (this.manager.hatMuttersprachenPruefungEndeEF() && this.manager.pruefeBelegungDurchgehendBelegtExistiert(this.fremdsprachen_neu, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21)) 
			return;
		if (!this.manager.pruefeBelegungExistiertMitSchriftlichkeit(this.fremdsprachen_fortgefuehrt, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1, GostHalbjahr.EF2)) 
			this.addFehler(GostBelegungsfehler.FS_16);
	}

	/**
	 * Zähle alle Fremdsprachen, die durchgehend schriftlich belegt wurden.
	 * Hierzu zählt auch die Unterrichtssprache eines bilingualen Sachfachs als zweite durchgehende
	 * Fremdsprache, sofern dieses durchgehend und schriftlich belegt wurde.
	 */
	private pruefeGesamtAnzahlDurchgehenedeSprachen() : void {
		let fremdsprachenDurchgehend : List<AbiturFachbelegung> = this.manager.filterBelegungen(this.fremdsprachen, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22);
		let fremdsprachenDurchgehendSchriftlich : List<AbiturFachbelegung> = this.manager.filterBelegungenMitSchriftlichkeit(fremdsprachenDurchgehend, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21);
		this.anzahl_schriftlich_durchgehend = fremdsprachenDurchgehendSchriftlich.size();
		if (this.anzahl_schriftlich_durchgehend !== 1) 
			return;
		let fsDurchgehend : GostFach | null = this.manager.getFach(fremdsprachenDurchgehendSchriftlich.get(0));
		if (fsDurchgehend === null) 
			return;
		let fremdspracheDurchgehend : String | null = GostFachManager.getFremdsprache(fsDurchgehend);
		if (fremdspracheDurchgehend === null) 
			return;
		let biliSachfaecherDurchgehend : List<AbiturFachbelegung> = this.manager.filterBelegungen(this.biliSachfaecher, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22);
		let biliSachfaecherDurchgehendSchriftlich : List<AbiturFachbelegung> = this.manager.filterBelegungenMitSchriftlichkeit(biliSachfaecherDurchgehend, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21);
		for (let biliSachfach of biliSachfaecherDurchgehendSchriftlich) {
			let fach : GostFach | null = this.manager.getFach(biliSachfach);
			if ((fach === null) || (JavaObject.equalsTranspiler(fremdspracheDurchgehend, (fach.biliSprache)))) 
				continue;
			this.anzahl_schriftlich_durchgehend++;
			return;
		}
	}

	/**
	 * Prüft, ob die Bedingungen für die Wahl eines bilingualen Sachfaches erfüllt sind, sofern eines
	 * belegt wurde.
	 */
	private pruefeGesamtBilingualeSachfaecher() : void {
		if (this.biliSachfaecher === null) 
			return;
		for (let biliSachfach of this.biliSachfaecher) {
			let biliFach : GostFach | null = this.manager.getFach(biliSachfach);
			if ((biliFach === null) || (!SprachendatenManager.hatSprachbelegungInSekIMitDauer(this.manager.getSprachendaten(), biliFach.biliSprache, 2))) 
				this.addFehler(GostBelegungsfehler.BIL_14);
		}
	}

	/**
	 * Prüfe, ob die Bedingungen für ein bilinguales Abitur erfüllt sind, sofern ein solches vom
	 * Schüler gewählt wurde.
	 */
	private pruefeGesamtBilingualenBildungsgang() : void {
		let biligualeSprache : String | null = this.manager.getBiligualenBildungsgang();
		if (biligualeSprache === null) 
			return;
		let biliSprache : AbiturFachbelegung | null = this.manager.getSprachbelegung(biligualeSprache);
		if ((!this.manager.pruefeBelegungMitSchriftlichkeit(biliSprache, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1, GostHalbjahr.EF2) || (!this.manager.pruefeBelegungMitKursart(biliSprache, GostKursart.LK, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22)))) 
			this.addFehler(GostBelegungsfehler.BIL_10);
		let biliSachfaecherEF : List<AbiturFachbelegung | null> | null = this.manager.filterBelegungen(this.biliSachfaecher, GostHalbjahr.EF1, GostHalbjahr.EF2);
		if (biliSachfaecherEF.size() < 1) {
			this.addFehler(GostBelegungsfehler.BIL_15);
			return;
		}
		if (biliSachfaecherEF.size() < 2) 
			this.addFehler(GostBelegungsfehler.BIL_11_INFO);
		let hatBiliSachfaecherDurchgehendSchriftlich : boolean = false;
		if (this.biliSachfaecher !== null) {
			for (let fach of this.biliSachfaecher) {
				if (this.manager.pruefeDurchgaengigkeitSchriftlich(fach)) {
					hatBiliSachfaecherDurchgehendSchriftlich = true;
					break;
				}
			}
		}
		if (!hatBiliSachfaecherDurchgehendSchriftlich) 
			this.addFehler(GostBelegungsfehler.BIL_12);
		if (!this.manager.pruefeExistiertAbiFach(this.biliSachfaecher, GostAbiturFach.AB3, GostAbiturFach.AB4)) 
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
		return this.anzahl_schriftlich_durchgehend;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.abschluss.gost.belegpruefung.Fremdsprachen', 'de.nrw.schule.svws.core.abschluss.gost.GostBelegpruefung'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_abschluss_gost_belegpruefung_Fremdsprachen(obj : unknown) : Fremdsprachen {
	return obj as Fremdsprachen;
}
