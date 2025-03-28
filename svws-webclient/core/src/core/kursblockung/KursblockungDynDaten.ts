import { JavaObject } from '../../java/lang/JavaObject';
import { HashMap2D } from '../../core/adt/map/HashMap2D';
import { GostBlockungsergebnisManager } from '../../core/utils/gost/GostBlockungsergebnisManager';
import { KursblockungDynFachart } from '../../core/kursblockung/KursblockungDynFachart';
import { GostBlockungsergebnisKursSchuelerZuordnung } from '../../core/data/gost/GostBlockungsergebnisKursSchuelerZuordnung';
import type { JavaSet } from '../../java/util/JavaSet';
import { KursblockungStatic } from '../../core/kursblockung/KursblockungStatic';
import { HashMap } from '../../java/util/HashMap';
import { KursblockungDynSchiene } from '../../core/kursblockung/KursblockungDynSchiene';
import { ArrayList } from '../../java/util/ArrayList';
import { KursblockungDynKurs } from '../../core/kursblockung/KursblockungDynKurs';
import { LongArrayKey } from '../../core/adt/LongArrayKey';
import { DeveloperNotificationException } from '../../core/exceptions/DeveloperNotificationException';
import { Logger } from '../../core/logger/Logger';
import { GostBlockungRegel } from '../../core/data/gost/GostBlockungRegel';
import { GostKursart } from '../../core/types/gost/GostKursart';
import { GostKursblockungRegelTyp } from '../../core/types/kursblockung/GostKursblockungRegelTyp';
import { Random } from '../../java/util/Random';
import { GostBlockungsergebnisKursSchienenZuordnung } from '../../core/data/gost/GostBlockungsergebnisKursSchienenZuordnung';
import { GostBlockungsergebnisKursSchienenZuordnungUpdate } from '../../core/data/gost/GostBlockungsergebnisKursSchienenZuordnungUpdate';
import type { List } from '../../java/util/List';
import { HashSet } from '../../java/util/HashSet';
import { GostBlockungKurs } from '../../core/data/gost/GostBlockungKurs';
import { GostFach } from '../../core/data/gost/GostFach';
import { KursblockungDynStatistik } from '../../core/kursblockung/KursblockungDynStatistik';
import { GostBlockungsdatenManager } from '../../core/utils/gost/GostBlockungsdatenManager';
import { LinkedCollection } from '../../core/adt/collection/LinkedCollection';
import { ArrayMap } from '../../core/adt/map/ArrayMap';
import { MapUtils } from '../../core/utils/MapUtils';
import { GostBlockungsergebnisKursSchuelerZuordnungUpdate } from '../../core/data/gost/GostBlockungsergebnisKursSchuelerZuordnungUpdate';
import { Schueler } from '../../asd/data/schueler/Schueler';
import { KursblockungDynSchueler } from '../../core/kursblockung/KursblockungDynSchueler';
import { Class } from '../../java/lang/Class';
import { ListUtils } from '../../core/utils/ListUtils';
import { DTOUtils } from '../../core/utils/DTOUtils';
import type { JavaMap } from '../../java/util/JavaMap';
import { UserNotificationException } from '../../core/exceptions/UserNotificationException';

export class KursblockungDynDaten extends JavaObject {

	/**
	 * Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 */
	private readonly _random : Random;

	/**
	 * Logger für Benutzerhinweise, Warnungen und Fehler.
	 */
	private readonly _logger : Logger;

	/**
	 * Alle Regeln nach ihrer ID gruppiert und in einer Liste der Reihenfolge nach gespeichert.
	 */
	private readonly _regelMap : JavaMap<GostKursblockungRegelTyp, List<GostBlockungRegel>>;

	/**
	 * Die maximale Blockungszeit in Millisekunden.
	 */
	private readonly _maxTimeMillis : number;

	/**
	 * Diese Datenstruktur speichert die Schienen und ihre Kurse.
	 */
	private _schienenArr : Array<KursblockungDynSchiene>;

	/**
	 * Alles Kurse.
	 */
	private _kursArr : Array<KursblockungDynKurs>;

	/**
	 * Alle Kurse, die noch über Schienen wandern können.
	 */
	private _kursArrFrei : Array<KursblockungDynKurs>;

	/**
	 * Map für schnellen Zugriff auf die Kurse über ihre ID.
	 */
	private readonly _kursMap : HashMap<number, KursblockungDynKurs>;

	/**
	 * Alle Facharten. Fachart meint Fach + Kursart, z.B. "D;GK".
	 */
	private _fachartArr : Array<KursblockungDynFachart>;

	/**
	 * Map für schnellen Zugriff auf die Facharten über FachID und KursartID.
	 */
	private readonly _fachartMap2D : HashMap2D<number, number, KursblockungDynFachart>;

	/**
	 * Alle SuS.
	 */
	private _schuelerArr : Array<KursblockungDynSchueler>;

	/**
	 * Map für schnellen Zugriff auf die SuS über ihre ID.
	 */
	private readonly _schuelerMap : HashMap<number, KursblockungDynSchueler>;

	/**
	 * Das Statistik-Objekt speichert die aktuellen Nichtwahlen, Kursdifferenzen und weitere Daten.
	 */
	private readonly _statistik : KursblockungDynStatistik;


	/**
	 * Der Konstruktor der Klasse liest alle Daten von {@link GostBlockungsdatenManager} ein und baut die relevanten Datenstrukturen auf.
	 *
	 * @param random  Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param logger  Ein {@link Logger}-Objekt für Benutzerhinweise, Warnungen und Fehler.
	 * @param input   Die Eingabedaten (Schnittstelle zur GUI).
	 */
	public constructor(random : Random, logger : Logger, input : GostBlockungsdatenManager) {
		super();
		this._random = random;
		this._logger = logger;
		this._regelMap = new ArrayMap(GostKursblockungRegelTyp.values());
		this._maxTimeMillis = input.getMaxTimeMillis();
		this._schienenArr = Array(0).fill(null);
		this._kursArr = Array(0).fill(null);
		this._kursArrFrei = Array(0).fill(null);
		this._kursMap = new HashMap();
		this._fachartArr = Array(0).fill(null);
		this._fachartMap2D = new HashMap2D();
		this._schuelerArr = Array(0).fill(null);
		this._schuelerMap = new HashMap();
		this._statistik = new KursblockungDynStatistik(this._logger);
		this.fehlerBeiReferenzen(input);
		this.fehlerBeiRegelGruppierung(input.daten().regeln);
		this.fehlerBeiSchuelerErstellung(input);
		this.fehlerBeiSchienenErzeugung(input.schieneGetAnzahl());
		this.fehlerBeiFachartenErstellung(input, this._schuelerArr.length, this._schienenArr.length);
		this.fehlerBeiSchuelerFachwahlenErstellung(input, this._schuelerArr);
		this.fehlerBeiStatistikErstellung(this._fachartArr, this._schuelerArr, input);
		this.fehlerBeiKursErstellung(input, this._schuelerArr.length);
		this.fehlerBeiKursFreiErstellung();
		this.fehlerBeiFachartKursArrayErstellung();
		this.fehlerBeiRegel_4_oder_5_SCHUELER_KURS_VARIANTEN();
		this.fehlerBeiRegel_7_oder_8_KURS_MIT_KURS_VARIANTEN();
		this.fehlerBeiRegel_9_KURS_MIT_DUMMY_SUS_AUFFUELLEN();
		this.fehlerBeiRegel_10_LEHRKRAEFTE_BEACHTEN(input);
		this.fehlerBeiRegel_11_bis_14_SCHUELER_MIT_SCHUELER_VARIANTEN(input);
		this.fehlerBeiRegel_15_KURS_MAXIMALE_SCHUELERANZAHL();
		this.fehlerBeiRegel_16_SCHUELER_IGNORIEREN();
		this.fehlerBeiRegel_18_FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE();
		this.aktionZustandSpeichernS();
		this.aktionZustandSpeichernK();
		this.aktionZustandSpeichernG();
	}

	/**
	 * Überprüft alle Referenzen in {@link KursblockungInput} und auch die referentielle Integrität.
	 *
	 * @param input Das {@link KursblockungInput}-Objekt von der GUI.
	 */
	private fehlerBeiReferenzen(input : GostBlockungsdatenManager) : void {
		DeveloperNotificationException.ifNull("pInput", input);
		DeveloperNotificationException.ifNull("pInput.daten()", input.daten());
		DeveloperNotificationException.ifNull("pInput.daten().fachwahlen", input.daten().fachwahlen);
		DeveloperNotificationException.ifNull("pInput.faecherManager()", input.faecherManager());
		DeveloperNotificationException.ifNull("pInput.faecherManager().faecher()", input.faecherManager().faecher());
		DeveloperNotificationException.ifNull("GostKursart.values()", GostKursart.values());
		DeveloperNotificationException.ifNull("pInput.daten().kurse", input.daten().kurse);
		DeveloperNotificationException.ifNull("pInput.daten().regeln", input.daten().regeln);
		DeveloperNotificationException.ifInvalidID("pInput.getID()", input.getID());
		DeveloperNotificationException.ifNull("pInput", input);
		DeveloperNotificationException.ifNull("pInput", input);
		DeveloperNotificationException.ifNull("pInput", input);
		DeveloperNotificationException.ifNull("pInput", input);
		DeveloperNotificationException.ifNull("pInput", input);
		DeveloperNotificationException.ifArrayIsEmpty("GostKursart.values()", GostKursart.values());
		DeveloperNotificationException.ifCollectionIsEmpty("pInput.daten().fachwahlen", input.daten().fachwahlen);
		DeveloperNotificationException.ifCollectionIsEmpty("pInput.faecherManager().faecher()", input.faecherManager().faecher());
		DeveloperNotificationException.ifCollectionIsEmpty("pInput.daten().kurse", input.daten().kurse);
		const schienenAnzahl : number = input.schieneGetAnzahl();
		DeveloperNotificationException.ifSmaller("schienenAnzahl", schienenAnzahl, 1);
		const usedSchiene : HashSet<number> | null = new HashSet<number>();
		for (const gSchiene of input.daten().schienen) {
			DeveloperNotificationException.ifInvalidID("gSchiene.id", gSchiene.id);
			DeveloperNotificationException.ifSmaller("gSchiene.id", gSchiene.nummer, 1);
			DeveloperNotificationException.ifGreater("gSchiene.id", gSchiene.nummer, schienenAnzahl);
			DeveloperNotificationException.ifSetAddsDuplicate("usedSchiene", usedSchiene, gSchiene.nummer);
		}
		const setKursarten : HashSet<number> = new HashSet<number>();
		for (const iKursart of GostKursart.values()) {
			DeveloperNotificationException.ifNull("iKursart", iKursart);
			DeveloperNotificationException.ifInvalidID("iKursart.id", iKursart.id);
			DeveloperNotificationException.ifSetAddsDuplicate("setKursarten", setKursarten, iKursart.id);
		}
		const setFaecher : HashSet<number> = new HashSet<number>();
		for (const iFach of input.faecherManager().faecher()) {
			DeveloperNotificationException.ifNull("iFach", iFach);
			DeveloperNotificationException.ifInvalidID("iFach.id", iFach.id);
			DeveloperNotificationException.ifSetAddsDuplicate("setFaecher", setFaecher, iFach.id);
		}
		const setKurse : HashSet<number> = new HashSet<number>();
		for (const iKurs of input.daten().kurse) {
			DeveloperNotificationException.ifNull("iKurs", iKurs);
			DeveloperNotificationException.ifInvalidID("iKurs.id", iKurs.id);
			DeveloperNotificationException.ifSetNotContains("setFaecher", setFaecher, iKurs.fach_id);
			DeveloperNotificationException.ifSetNotContains("setKursarten", setKursarten, iKurs.kursart);
			DeveloperNotificationException.ifSetAddsDuplicate("setKurse", setKurse, iKurs.id);
		}
		const setSchueler : HashSet<number> = new HashSet<number>();
		for (const gSchueler of input.daten().schueler)
			DeveloperNotificationException.ifSetAddsDuplicate("setSchueler", setSchueler, gSchueler.id);
		for (const iFachwahl of input.daten().fachwahlen) {
			DeveloperNotificationException.ifNull("iFachwahl", iFachwahl);
			DeveloperNotificationException.ifInvalidID("iFachwahl.schuelerID", iFachwahl.schuelerID);
			DeveloperNotificationException.ifSetNotContains("setFaecher", setFaecher, iFachwahl.fachID);
			DeveloperNotificationException.ifSetNotContains("setKursarten", setKursarten, iFachwahl.kursartID);
			DeveloperNotificationException.ifSetNotContains("setSchueler", setSchueler, iFachwahl.schuelerID);
		}
		for (const iRegel of input.daten().regeln) {
			DeveloperNotificationException.ifNull("iRegel", iRegel);
			DeveloperNotificationException.ifNull("iRegel.parameter", iRegel.parameter);
			DeveloperNotificationException.ifInvalidID("iRegel.id", iRegel.id);
			const gostRegel : GostKursblockungRegelTyp = GostKursblockungRegelTyp.fromTyp(iRegel.typ);
			const daten : Array<number> = iRegel.parameter.toArray(Array(0).fill(null));
			for (let i : number = 0; i < daten.length; i++)
				DeveloperNotificationException.ifNull("daten[" + i + "]", daten[i]);
			switch (gostRegel) {
				case GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS: {
					KursblockungDynDaten.schritt01FehlerBeiReferenzen_Regeltyp1(daten, setKursarten, schienenAnzahl);
					break;
				}
				case GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE: {
					KursblockungDynDaten.schritt01FehlerBeiReferenzen_Regeltyp2(daten, setKurse, schienenAnzahl);
					break;
				}
				case GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE: {
					KursblockungDynDaten.schritt01FehlerBeiReferenzen_Regeltyp3(daten, setKurse, schienenAnzahl);
					break;
				}
				case GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS: {
					KursblockungDynDaten.schritt01FehlerBeiReferenzen_Regeltyp4(daten, setSchueler, setKurse);
					break;
				}
				case GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS: {
					KursblockungDynDaten.schritt01FehlerBeiReferenzen_Regeltyp5(daten, setSchueler, setKurse);
					break;
				}
				case GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS: {
					KursblockungDynDaten.schritt01FehlerBeiReferenzen_Regeltyp6(daten, setKursarten, schienenAnzahl);
					break;
				}
				case GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS: {
					KursblockungDynDaten.schritt01FehlerBeiReferenzen_Regeltyp7(daten, setKurse);
					break;
				}
				case GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS: {
					KursblockungDynDaten.schritt01FehlerBeiReferenzen_Regeltyp8(daten, setKurse);
					break;
				}
				case GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN: {
					KursblockungDynDaten.schritt01FehlerBeiReferenzen_Regeltyp9(daten, setKurse);
					break;
				}
				case GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN: {
					KursblockungDynDaten.schritt01FehlerBeiReferenzen_Regeltyp10(daten);
					break;
				}
				case GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH: {
					KursblockungDynDaten.schritt01FehlerBeiReferenzen_Regeltyp11(daten, setSchueler, setFaecher);
					break;
				}
				case GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH: {
					KursblockungDynDaten.schritt01FehlerBeiReferenzen_Regeltyp12(daten, setSchueler, setFaecher);
					break;
				}
				case GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER: {
					KursblockungDynDaten.schritt01FehlerBeiReferenzen_Regeltyp13(daten, setSchueler);
					break;
				}
				case GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER: {
					KursblockungDynDaten.schritt01FehlerBeiReferenzen_Regeltyp14(daten, setSchueler);
					break;
				}
				case GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL: {
					KursblockungDynDaten.schritt01FehlerBeiReferenzen_Regeltyp15(daten, setKurse);
					break;
				}
				case GostKursblockungRegelTyp.SCHUELER_IGNORIEREN: {
					KursblockungDynDaten.schritt01FehlerBeiReferenzen_Regeltyp16(daten, setSchueler);
					break;
				}
				case GostKursblockungRegelTyp.KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN: {
					KursblockungDynDaten.schritt01FehlerBeiReferenzen_Regeltyp17(daten, setKurse);
					break;
				}
				case GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE: {
					KursblockungDynDaten.schritt01FehlerBeiReferenzen_Regeltyp18(daten, setFaecher, setKursarten);
					break;
				}
				default: {
					throw new DeveloperNotificationException("Unbekannter Regeltyp!")
				}
			}
		}
	}

	private static schritt01FehlerBeiReferenzen_Regeltyp1(daten : Array<number>, setKursarten : HashSet<number>, schienenAnzahl : number) : void {
		const length : number = daten.length;
		DeveloperNotificationException.ifTrue("KURSART_SPERRE_SCHIENEN_VON_BIS daten.length=" + length + ", statt 3!", length !== 3);
		const kursartID : number = daten[0];
		DeveloperNotificationException.ifSetNotContains("setKursarten", setKursarten, kursartID);
		const von : number = daten[1];
		const bis : number = daten[2];
		DeveloperNotificationException.ifTrue("KURSART_SPERRE_SCHIENEN_VON_BIS (" + kursartID + ", " + von + ", " + bis + ") ist unlogisch!", !((von >= 1) && (von <= bis) && (bis <= schienenAnzahl)));
	}

	private static schritt01FehlerBeiReferenzen_Regeltyp2(daten : Array<number>, setKurse : HashSet<number>, schienenAnzahl : number) : void {
		const length : number = daten.length;
		DeveloperNotificationException.ifTrue("KURS_FIXIERE_IN_SCHIENE daten.length=" + length + ", statt 2!", length !== 2);
		const kursID : number = daten[0].valueOf();
		DeveloperNotificationException.ifSetNotContains("setKurse", setKurse, kursID);
		const schiene : number = daten[1];
		DeveloperNotificationException.ifTrue("KURS_FIXIERE_IN_SCHIENE (" + kursID + ", " + schiene + ") ist unlogisch!", !((schiene >= 1) && (schiene <= schienenAnzahl)));
	}

	private static schritt01FehlerBeiReferenzen_Regeltyp3(daten : Array<number>, setKurse : HashSet<number>, schienenAnzahl : number) : void {
		const length : number = daten.length;
		DeveloperNotificationException.ifTrue("KURS_SPERRE_IN_SCHIENE daten.length=" + length + ", statt 2!", length !== 2);
		const kursID : number = daten[0].valueOf();
		DeveloperNotificationException.ifSetNotContains("setKurse", setKurse, kursID);
		const schiene : number = daten[1];
		DeveloperNotificationException.ifTrue("KURS_SPERRE_IN_SCHIENE (" + kursID + ", " + schiene + ") ist unlogisch!", !((schiene >= 1) && (schiene <= schienenAnzahl)));
	}

	private static schritt01FehlerBeiReferenzen_Regeltyp4(daten : Array<number>, setSchueler : HashSet<number>, setKurse : HashSet<number>) : void {
		const length : number = daten.length;
		DeveloperNotificationException.ifTrue("SCHUELER_FIXIEREN_IN_KURS daten.length=" + length + ", statt 2!", length !== 2);
		const schuelerID : number = daten[0].valueOf();
		DeveloperNotificationException.ifSetNotContains("setSchueler", setSchueler, schuelerID);
		const kursID : number = daten[1].valueOf();
		DeveloperNotificationException.ifSetNotContains("setKurse", setKurse, kursID);
	}

	private static schritt01FehlerBeiReferenzen_Regeltyp5(daten : Array<number>, setSchueler : HashSet<number>, setKurse : HashSet<number>) : void {
		const length : number = daten.length;
		DeveloperNotificationException.ifTrue("SCHUELER_VERBIETEN_IN_KURS daten.length=" + length + ", statt 2!", length !== 2);
		const schuelerID : number = daten[0].valueOf();
		DeveloperNotificationException.ifSetNotContains("setSchueler", setSchueler, schuelerID);
		const kursID : number = daten[1].valueOf();
		DeveloperNotificationException.ifSetNotContains("setKurse", setKurse, kursID);
	}

	private static schritt01FehlerBeiReferenzen_Regeltyp6(daten : Array<number>, setKursarten : HashSet<number>, schienenAnzahl : number) : void {
		const length : number = daten.length;
		DeveloperNotificationException.ifTrue("KURSART_ALLEIN_IN_SCHIENEN_VON_BIS daten.length=" + length + ", statt 3!", length !== 3);
		const kursartID : number = daten[0];
		DeveloperNotificationException.ifSetNotContains("setKursarten", setKursarten, kursartID);
		const von : number = daten[1];
		const bis : number = daten[2];
		DeveloperNotificationException.ifTrue("KURSART_ALLEIN_IN_SCHIENEN_VON_BIS (" + kursartID + ", " + von + ", " + bis + ") ist unlogisch!", !((von >= 1) && (von <= bis) && (bis <= schienenAnzahl)));
	}

	private static schritt01FehlerBeiReferenzen_Regeltyp7(daten : Array<number>, setKurse : HashSet<number>) : void {
		const length : number = daten.length;
		DeveloperNotificationException.ifTrue("KURS_VERBIETEN_MIT_KURS daten.length=" + length + ", statt 2!", length !== 2);
		const kursID1 : number = daten[0].valueOf();
		DeveloperNotificationException.ifSetNotContains("setKurse", setKurse, kursID1);
		const kursID2 : number = daten[1].valueOf();
		DeveloperNotificationException.ifSetNotContains("setKurse", setKurse, kursID2);
		DeveloperNotificationException.ifTrue("Die Regel 'KURS_VERBIETEN_MIT_KURS' wurde mit einem Kurs (" + kursID1 + ") und sich selbst kombiniert!", kursID1 === kursID2);
	}

	private static schritt01FehlerBeiReferenzen_Regeltyp8(daten : Array<number>, setKurse : HashSet<number>) : void {
		const length : number = daten.length;
		DeveloperNotificationException.ifTrue("KURS_ZUSAMMEN_MIT_KURS daten.length=" + length + ", statt 2!", length !== 2);
		const kursID1 : number = daten[0].valueOf();
		DeveloperNotificationException.ifSetNotContains("setKurse", setKurse, kursID1);
		const kursID2 : number = daten[1].valueOf();
		DeveloperNotificationException.ifSetNotContains("setKurse", setKurse, kursID2);
		DeveloperNotificationException.ifTrue("Die Regel 'KURS_ZUSAMMEN_MIT_KURS' wurde mit einem Kurs (" + kursID1 + ") und sich selbst kombiniert!", kursID1 === kursID2);
	}

	private static schritt01FehlerBeiReferenzen_Regeltyp9(daten : Array<number>, setKurse : HashSet<number>) : void {
		const length : number = daten.length;
		DeveloperNotificationException.ifTrue("KURS_MIT_DUMMY_SUS_AUFFUELLEN daten.length=" + length + ", statt 2!", length !== 2);
		const kursID : number = daten[0].valueOf();
		DeveloperNotificationException.ifSetNotContains("setKurse", setKurse, kursID);
		const dummySuS : number = daten[1];
		DeveloperNotificationException.ifSmaller("dummySuS", dummySuS, 1);
		DeveloperNotificationException.ifGreater("dummySuS", dummySuS, 99);
	}

	private static schritt01FehlerBeiReferenzen_Regeltyp10(daten : Array<number>) : void {
		const length : number = daten.length;
		DeveloperNotificationException.ifTrue("LEHRKRAEFTE_BEACHTEN daten.length=" + length + ", statt 0!", length !== 0);
	}

	private static schritt01FehlerBeiReferenzen_Regeltyp11(daten : Array<number>, setSchueler : HashSet<number>, setFaecher : HashSet<number>) : void {
		const length : number = daten.length;
		DeveloperNotificationException.ifTrue("SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH daten.length=" + length + ", statt 3!", length !== 3);
		const schuelerID1 : number = daten[0].valueOf();
		const schuelerID2 : number = daten[1].valueOf();
		DeveloperNotificationException.ifSetNotContains("setSchueler", setSchueler, schuelerID1);
		DeveloperNotificationException.ifSetNotContains("setSchueler", setSchueler, schuelerID2);
		const fachID : number = daten[2].valueOf();
		DeveloperNotificationException.ifSetNotContains("setFaecher", setFaecher, fachID);
	}

	private static schritt01FehlerBeiReferenzen_Regeltyp12(daten : Array<number>, setSchueler : HashSet<number>, setFaecher : HashSet<number>) : void {
		const length : number = daten.length;
		DeveloperNotificationException.ifTrue("SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH daten.length=" + length + ", statt 3!", length !== 3);
		const schuelerID1 : number = daten[0].valueOf();
		const schuelerID2 : number = daten[1].valueOf();
		DeveloperNotificationException.ifSetNotContains("setSchueler", setSchueler, schuelerID1);
		DeveloperNotificationException.ifSetNotContains("setSchueler", setSchueler, schuelerID2);
		const fachID : number = daten[2].valueOf();
		DeveloperNotificationException.ifSetNotContains("setFaecher", setFaecher, fachID);
	}

	private static schritt01FehlerBeiReferenzen_Regeltyp13(daten : Array<number>, setSchueler : HashSet<number>) : void {
		const length : number = daten.length;
		DeveloperNotificationException.ifTrue("SCHUELER_ZUSAMMEN_MIT_SCHUELER daten.length=" + length + ", statt 2!", length !== 2);
		const schuelerID1 : number = daten[0].valueOf();
		const schuelerID2 : number = daten[1].valueOf();
		DeveloperNotificationException.ifSetNotContains("setSchueler", setSchueler, schuelerID1);
		DeveloperNotificationException.ifSetNotContains("setSchueler", setSchueler, schuelerID2);
	}

	private static schritt01FehlerBeiReferenzen_Regeltyp14(daten : Array<number>, setSchueler : HashSet<number>) : void {
		const length : number = daten.length;
		DeveloperNotificationException.ifTrue("SCHUELER_VERBIETEN_MIT_SCHUELER daten.length=" + length + ", statt 2!", length !== 2);
		const schuelerID1 : number = daten[0].valueOf();
		const schuelerID2 : number = daten[1].valueOf();
		DeveloperNotificationException.ifSetNotContains("setSchueler", setSchueler, schuelerID1);
		DeveloperNotificationException.ifSetNotContains("setSchueler", setSchueler, schuelerID2);
	}

	private static schritt01FehlerBeiReferenzen_Regeltyp15(daten : Array<number>, setKurse : HashSet<number>) : void {
		const length : number = daten.length;
		DeveloperNotificationException.ifTrue("KURS_MAXIMALE_SCHUELERANZAHL daten.length=" + length + ", statt 2!", length !== 2);
		const kursID : number = daten[0].valueOf();
		DeveloperNotificationException.ifSetNotContains("setKurse", setKurse, kursID);
		const anzahlSus : number = daten[1];
		DeveloperNotificationException.ifSmaller("anzahlSus", anzahlSus, 0);
		DeveloperNotificationException.ifGreater("anzahlSus", anzahlSus, 100);
	}

	private static schritt01FehlerBeiReferenzen_Regeltyp16(daten : Array<number>, setSchueler : HashSet<number>) : void {
		const length : number = daten.length;
		DeveloperNotificationException.ifTrue("SCHUELER_IGNORIEREN daten.length=" + length + ", statt 1!", length !== 1);
		const schuelerID : number = daten[0].valueOf();
		DeveloperNotificationException.ifSetNotContains("setSchueler", setSchueler, schuelerID);
	}

	private static schritt01FehlerBeiReferenzen_Regeltyp17(daten : Array<number>, setKurse : HashSet<number>) : void {
		const length : number = daten.length;
		DeveloperNotificationException.ifTrue("KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN daten.length=" + length + ", statt 1!", length !== 1);
		const kursID : number = daten[0].valueOf();
		DeveloperNotificationException.ifSetNotContains("setKurse", setKurse, kursID);
	}

	private static schritt01FehlerBeiReferenzen_Regeltyp18(daten : Array<number>, setFaecher : HashSet<number>, setKursarten : HashSet<number>) : void {
		const length : number = daten.length;
		DeveloperNotificationException.ifTrue("FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE daten.length=" + length + ", statt 3!", length !== 3);
		const fachID : number = daten[0].valueOf();
		DeveloperNotificationException.ifSetNotContains("setFaecher", setFaecher, fachID);
		const kursartID : number = daten[1];
		DeveloperNotificationException.ifSetNotContains("setKursarten", setKursarten, kursartID);
		const fachArtProSchieneMaximal : number = daten[2];
		DeveloperNotificationException.ifSmaller("fachArtProSchieneMaximal", fachArtProSchieneMaximal, 1);
		DeveloperNotificationException.ifGreater("fachArtProSchieneMaximal", fachArtProSchieneMaximal, 9);
	}

	private fehlerBeiRegelGruppierung(pRegeln : List<GostBlockungRegel>) : void {
		const regelDatabaseIDs : HashSet<number> | null = new HashSet<number>();
		for (const iRegel of pRegeln) {
			DeveloperNotificationException.ifInvalidID("iRegel.id", iRegel.id);
			DeveloperNotificationException.ifSetAddsDuplicate("regelDatabaseIDs", regelDatabaseIDs, iRegel.id);
			const regelTyp : GostKursblockungRegelTyp = GostKursblockungRegelTyp.fromTyp(iRegel.typ);
			MapUtils.getOrCreateArrayList(this._regelMap, regelTyp).add(iRegel);
		}
	}

	private fehlerBeiSchuelerErstellung(input : GostBlockungsdatenManager) : void {
		const setSchueler : HashSet<number> = new HashSet<number>();
		for (const gSchueler of input.daten().schueler)
			setSchueler.add(gSchueler.id);
		for (const fachwahl of input.daten().fachwahlen)
			DeveloperNotificationException.ifSetNotContains("setSchueler", setSchueler, fachwahl.schuelerID);
		const nSchueler : number = setSchueler.size();
		const nSchienen : number = input.schieneGetAnzahl();
		const nKurse : number = input.kursGetAnzahl();
		this._schuelerArr = Array(nSchueler).fill(null);
		let i : number = 0;
		for (const sID of setSchueler) {
			const schueler : KursblockungDynSchueler = new KursblockungDynSchueler(this._logger, this._random, sID, this._statistik, nSchienen, nKurse, i);
			this._schuelerArr[i] = schueler;
			this._schuelerMap.put(sID, schueler);
			i++;
		}
	}

	private fehlerBeiFachartenErstellung(input : GostBlockungsdatenManager, nSchueler : number, nSchienen : number) : void {
		let nFacharten : number = 0;
		const nKurse : number = input.daten().kurse.size();
		for (const gKurs of input.daten().kurse) {
			const fach : GostFach = input.faecherManager().getOrException(gKurs.fach_id);
			const kursart : GostKursart = GostKursart.fromID(gKurs.kursart);
			let dynFachart : KursblockungDynFachart | null = this._fachartMap2D.getOrNull(fach.id, kursart.id);
			if (dynFachart === null) {
				dynFachart = new KursblockungDynFachart(this._random, nFacharten, fach, kursart, this._statistik, nSchueler, nSchienen);
				this._fachartMap2D.put(fach.id, kursart.id, dynFachart);
				nFacharten++;
			}
			dynFachart.aktionMaxKurseErhoehen();
		}
		for (const iFachwahl of input.daten().fachwahlen) {
			const fach : GostFach = input.faecherManager().getOrException(iFachwahl.fachID);
			const kursart : GostKursart = GostKursart.fromID(iFachwahl.kursartID);
			let dynFachart : KursblockungDynFachart | null = this._fachartMap2D.getOrNull(fach.id, kursart.id);
			if (dynFachart === null) {
				dynFachart = new KursblockungDynFachart(this._random, nFacharten, fach, kursart, this._statistik, nSchueler, nSchienen);
				this._fachartMap2D.put(fach.id, kursart.id, dynFachart);
				nFacharten++;
			}
		}
		DeveloperNotificationException.ifSmaller("nFacharten", nFacharten, 1);
		this._fachartArr = Array(nFacharten).fill(null);
		for (const fachart of this._fachartMap2D.getNonNullValuesAsList())
			this._fachartArr[fachart.gibNr()] = fachart;
		let kursSumme : number = 0;
		for (const fa of this._fachartArr)
			kursSumme += fa.gibKurseMax();
		DeveloperNotificationException.ifTrue("Die Summe aller auf die Facharten verteilten Kurse ist ungleich der Gesamtkursanzahl.", kursSumme !== nKurse);
	}

	private fehlerBeiSchuelerFachwahlenErstellung(input : GostBlockungsdatenManager, susArr : Array<KursblockungDynSchueler>) : void {
		const mapSchuelerFA : HashMap<number, List<KursblockungDynFachart>> = new HashMap<number, List<KursblockungDynFachart>>();
		for (const iFachwahl of input.daten().fachwahlen) {
			const dynFachart : KursblockungDynFachart = this.gibFachart(iFachwahl.fachID, iFachwahl.kursartID);
			MapUtils.getOrCreateArrayList(mapSchuelerFA, iFachwahl.schuelerID).add(dynFachart);
		}
		for (const schueler of susArr) {
			const listFA : List<KursblockungDynFachart> = MapUtils.getOrCreateArrayList(mapSchuelerFA, schueler.gibDatenbankID());
			const arrFA : Array<KursblockungDynFachart> = listFA.toArray(Array(0).fill(null));
			schueler.aktionSetzeFachartenUndIDs(arrFA);
		}
	}

	private fehlerBeiStatistikErstellung(fachartArr : Array<KursblockungDynFachart>, susArr : Array<KursblockungDynSchueler>, input : GostBlockungsdatenManager) : void {
		const nFacharten : number = fachartArr.length;
		const bewertungMatrixFachart : Array<Array<number>> = [...Array(nFacharten)].map(e => Array(nFacharten).fill(0));
		for (const s of susArr) {
			const fa : Array<KursblockungDynFachart> = s.gibFacharten();
			for (let i1 : number = 0; i1 < fa.length; i1++) {
				const nr1 : number = fa[i1].gibNr();
				for (let i2 : number = i1 + 1; i2 < fa.length; i2++) {
					const nr2 : number = fa[i2].gibNr();
					bewertungMatrixFachart[nr1][nr2]++;
					bewertungMatrixFachart[nr2][nr1]++;
				}
			}
		}
		for (let i1 : number = 0; i1 < nFacharten; i1++) {
			const kursAnz1 : number = fachartArr[i1].gibKurseMax();
			const nr1 : number = fachartArr[i1].gibNr();
			for (let i2 : number = 0; i2 < nFacharten; i2++) {
				const kursAnz2 : number = fachartArr[i2].gibKurseMax();
				const nr2 : number = fachartArr[i2].gibNr();
				if ((kursAnz1 === 0) || (kursAnz2 === 0)) {
					bewertungMatrixFachart[nr1][nr2] = 0;
					continue;
				}
				const nenner : number = ((kursAnz1 + kursAnz2) - 2);
				const faktor : number = (nenner === 0) ? 1000000 : (Math.trunc(100 / nenner));
				bewertungMatrixFachart[nr1][nr2] *= faktor;
			}
			bewertungMatrixFachart[nr1][nr1] += 10000000;
		}
		this._statistik.aktionInitialisiere(bewertungMatrixFachart, susArr.length, fachartArr.length, input.kursGetAnzahl());
	}

	private fehlerBeiSchienenErzeugung(nSchienen : number) : void {
		this._schienenArr = Array(nSchienen).fill(null);
		for (let nr : number = 0; nr < nSchienen; nr++)
			this._schienenArr[nr] = new KursblockungDynSchiene(this._logger, nr, this._statistik);
	}

	private fehlerBeiKursErstellung(input : GostBlockungsdatenManager, nSchueler : number) : void {
		const nKurse : number = input.kursGetAnzahl();
		const nSchienen : number = input.schieneGetAnzahl();
		this._kursArr = Array(nKurse).fill(null);
		let i : number = 0;
		for (const kurs of input.daten().kurse) {
			const dynKurs : KursblockungDynKurs = this.schritt08FehlerBeiKursErstellungErzeuge(input, kurs, nSchienen, i, nSchueler);
			this._kursArr[i] = dynKurs;
			DeveloperNotificationException.ifMapPutOverwrites(this._kursMap, kurs.id, dynKurs);
			i++;
		}
	}

	private schritt08FehlerBeiKursErstellungErzeuge(input : GostBlockungsdatenManager, kurs : GostBlockungKurs, nSchienen : number, kursNr : number, nSchueler : number) : KursblockungDynKurs {
		DeveloperNotificationException.ifSmaller("kurs.anzahlSchienen", kurs.anzahlSchienen, 1);
		DeveloperNotificationException.ifGreater("kurs.anzahlSchienen", kurs.anzahlSchienen, this._schienenArr.length);
		const schieneLage : List<KursblockungDynSchiene> = new ArrayList<KursblockungDynSchiene>();
		const schieneFrei : List<KursblockungDynSchiene> = ListUtils.getCopyAsArrayListPermuted(this._schienenArr, this._random);
		for (const regel1 of MapUtils.getOrCreateArrayList(this._regelMap, GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS))
			if (kurs.kursart === regel1.parameter.get(0)) {
				const von : number = regel1.parameter.get(1);
				const bis : number = regel1.parameter.get(2);
				for (let schiene : number = von; schiene <= bis; schiene++)
					schieneFrei.remove(this._schienenArr[schiene - 1]);
			}
		for (const regel6 of MapUtils.getOrCreateArrayList(this._regelMap, GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS)) {
			const kursartStimmt : boolean = kurs.kursart === regel6.parameter.get(0);
			const von : number = regel6.parameter.get(1);
			const bis : number = regel6.parameter.get(2);
			for (let schiene : number = 1; schiene <= nSchienen; schiene++) {
				const innerhalb : boolean = (von <= schiene) && (schiene <= bis);
				if (innerhalb !== kursartStimmt)
					schieneFrei.remove(this._schienenArr[schiene - 1]);
			}
		}
		for (const regel3 of MapUtils.getOrCreateArrayList(this._regelMap, GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE))
			if (kurs.id === regel3.parameter.get(0)) {
				const schiene : number = regel3.parameter.get(1);
				schieneFrei.remove(this._schienenArr[schiene - 1]);
			}
		for (const regel2 of MapUtils.getOrCreateArrayList(this._regelMap, GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE))
			if (kurs.id === regel2.parameter.get(0)) {
				const schiene : number = regel2.parameter.get(1);
				const dynSchiene : KursblockungDynSchiene = this._schienenArr[schiene - 1];
				if (schieneLage.contains(dynSchiene))
					continue;
				UserNotificationException.ifTrue("Die Regel 'KURS_FIXIERE_IN_SCHIENE' will Kurs (id=" + kurs.id + ") in Schiene (" + schiene + ") fixieren, aber die Schiene wurde bereits gesperrt!", !schieneFrei.contains(dynSchiene));
				schieneFrei.remove(dynSchiene);
				schieneLage.add(dynSchiene);
			}
		const anzahlFixierterSchienen : number = schieneLage.size();
		DeveloperNotificationException.ifGreater("kurs.anzahlSchienen", anzahlFixierterSchienen, kurs.anzahlSchienen);
		while (schieneLage.size() < kurs.anzahlSchienen) {
			UserNotificationException.ifTrue(input.toStringKurs(kurs.id) + " hat zu viele Schienen gesperrt, so dass seine Schienenanzahl nicht erfüllt werden kann!", schieneFrei.isEmpty());
			const indexLast : number = schieneFrei.size() - 1;
			const s : KursblockungDynSchiene | null = schieneFrei.get(indexLast);
			if (s !== null) {
				schieneFrei.remove(s);
				schieneLage.add(s);
			}
		}
		const schienenLageArray : Array<KursblockungDynSchiene> = schieneLage.toArray(Array(0).fill(null));
		const schienenFreiArray : Array<KursblockungDynSchiene> = schieneFrei.toArray(Array(0).fill(null));
		const dynFachart : KursblockungDynFachart = this.gibFachart(kurs.fach_id, kurs.kursart);
		return new KursblockungDynKurs(this._random, schienenLageArray, anzahlFixierterSchienen, schienenFreiArray, kurs.id, dynFachart, this._logger, kursNr, nSchueler);
	}

	private fehlerBeiKursFreiErstellung() : void {
		let nKursFrei : number = 0;
		for (const kurs of this._kursArr)
			if (kurs.gibHatFreiheitsgrade())
				nKursFrei++;
		this._kursArrFrei = Array(nKursFrei).fill(null);
		let j : number = 0;
		for (const kurs of this._kursArr)
			if (kurs.gibHatFreiheitsgrade()) {
				this._kursArrFrei[j] = kurs;
				j++;
			}
	}

	private fehlerBeiFachartKursArrayErstellung() : void {
		const nFacharten : number = this._fachartArr.length;
		const mapFachartList : HashMap<number, List<KursblockungDynKurs>> = new HashMap<number, List<KursblockungDynKurs>>();
		for (let i : number = 0; i < nFacharten; i++)
			mapFachartList.put(i, new ArrayList<KursblockungDynKurs>());
		for (const kurs of this._kursArr) {
			const fachartNr : number = kurs.gibFachart().gibNr();
			DeveloperNotificationException.ifMapGetIsNull(mapFachartList, fachartNr).add(kurs);
		}
		for (let fachartNr : number = 0; fachartNr < nFacharten; fachartNr++) {
			const list : List<KursblockungDynKurs> | null = DeveloperNotificationException.ifMapGetIsNull(mapFachartList, fachartNr);
			const tmpKursArr : Array<KursblockungDynKurs> = list.toArray(Array(0).fill(null));
			this._fachartArr[fachartNr].aktionSetKurse(tmpKursArr);
		}
	}

	private fehlerBeiRegel_4_oder_5_SCHUELER_KURS_VARIANTEN() : void {
		const mapSchuelerZuFixierungen : HashMap<number, List<number>> = new HashMap<number, List<number>>();
		for (const regel4 of MapUtils.getOrCreateArrayList(this._regelMap, GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS)) {
			const schuelerID : number = regel4.parameter.get(0).valueOf();
			const kursID : number = regel4.parameter.get(1).valueOf();
			MapUtils.getOrCreateArrayList(mapSchuelerZuFixierungen, schuelerID).add(kursID);
			const schueler : KursblockungDynSchueler = this.gibSchueler(schuelerID);
			const fixierterKurs : KursblockungDynKurs = this.gibKurs(kursID);
			for (const kurs of fixierterKurs.gibFachart().gibKurse())
				if (kurs as unknown === fixierterKurs as unknown)
					kurs.regel_04_setzeSchuelerFixierung(schueler.internalSchuelerID);
				else
					schueler.aktionSetzeKursSperrung(kurs.gibInternalID());
		}
		for (const idSchueler of mapSchuelerZuFixierungen.keySet()) {
			const listKursIDs : List<number> = MapUtils.getOrCreateArrayList(mapSchuelerZuFixierungen, idSchueler);
			for (let index2 : number = 1; index2 < listKursIDs.size(); index2++)
				for (let index1 : number = 0; index1 < index2; index1++) {
					const kursID1 : number = listKursIDs.get(index1).valueOf();
					const kursID2 : number = listKursIDs.get(index2).valueOf();
					const kurs1 : KursblockungDynKurs = this.gibKurs(kursID1);
					const kurs2 : KursblockungDynKurs = this.gibKurs(kursID2);
					this._statistik.regelHinzufuegenKursVerbieteMitKurs(kurs1, kurs2);
				}
		}
		for (const regel5 of MapUtils.getOrCreateArrayList(this._regelMap, GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS)) {
			const schuelerID : number = regel5.parameter.get(0).valueOf();
			const kursID : number = regel5.parameter.get(1).valueOf();
			const schueler : KursblockungDynSchueler = this.gibSchueler(schuelerID);
			const verbotenerKurs : KursblockungDynKurs = this.gibKurs(kursID);
			schueler.aktionSetzeKursSperrung(verbotenerKurs.gibInternalID());
		}
	}

	private fehlerBeiRegel_7_oder_8_KURS_MIT_KURS_VARIANTEN() : void {
		for (const regel7 of MapUtils.getOrCreateArrayList(this._regelMap, GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS)) {
			const kursID1 : number = regel7.parameter.get(0).valueOf();
			const kursID2 : number = regel7.parameter.get(1).valueOf();
			const kurs1 : KursblockungDynKurs = this.gibKurs(kursID1);
			const kurs2 : KursblockungDynKurs = this.gibKurs(kursID2);
			this._statistik.regelHinzufuegenKursVerbieteMitKurs(kurs1, kurs2);
		}
		for (const regel8 of MapUtils.getOrCreateArrayList(this._regelMap, GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS)) {
			const kursID1 : number = regel8.parameter.get(0).valueOf();
			const kursID2 : number = regel8.parameter.get(1).valueOf();
			const kurs1 : KursblockungDynKurs = this.gibKurs(kursID1);
			const kurs2 : KursblockungDynKurs = this.gibKurs(kursID2);
			this._statistik.regelHinzufuegenKursZusammenMitKurs(kurs1, kurs2);
		}
	}

	private fehlerBeiRegel_9_KURS_MIT_DUMMY_SUS_AUFFUELLEN() : void {
		for (const regel9 of MapUtils.getOrCreateArrayList(this._regelMap, GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN)) {
			const kursID : number = regel9.parameter.get(0).valueOf();
			const susAnzahl : number = regel9.parameter.get(1);
			const kurs : KursblockungDynKurs = this.gibKurs(kursID);
			for (let i : number = 0; i < susAnzahl; i++)
				kurs.aktionSchuelerDummyHinzufuegen();
		}
	}

	private fehlerBeiRegel_10_LEHRKRAEFTE_BEACHTEN(pInput : GostBlockungsdatenManager) : void {
		const regelnTyp10 : List<GostBlockungRegel> | null = MapUtils.getOrCreateArrayList(this._regelMap, GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN);
		if (regelnTyp10.isEmpty())
			return;
		DeveloperNotificationException.ifGreater("Liste of regelnTyp10", regelnTyp10.size(), 1);
		const vKurseMitLehrkraft : ArrayList<GostBlockungKurs> = new ArrayList<GostBlockungKurs>();
		for (const gKurs of pInput.daten().kurse)
			if (!gKurs.lehrer.isEmpty())
				vKurseMitLehrkraft.add(gKurs);
		for (const gKurs1 of vKurseMitLehrkraft)
			for (const gKurs2 of vKurseMitLehrkraft)
				if (gKurs1.id < gKurs2.id)
					for (const gLehr1 of gKurs1.lehrer)
						for (const gLehr2 of gKurs2.lehrer)
							if (gLehr1.id === gLehr2.id) {
								const kurs1 : KursblockungDynKurs = this.gibKurs(gKurs1.id);
								const kurs2 : KursblockungDynKurs = this.gibKurs(gKurs2.id);
								this._statistik.regelHinzufuegenKursVerbieteMitKurs(kurs1, kurs2);
							}
	}

	private fehlerBeiRegel_11_bis_14_SCHUELER_MIT_SCHUELER_VARIANTEN(input : GostBlockungsdatenManager) : void {
		const setSSF : HashSet<LongArrayKey> = new HashSet<LongArrayKey>();
		for (const regel11 of MapUtils.getOrCreateArrayList(this._regelMap, GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH)) {
			const idS1 : number = regel11.parameter.get(0).valueOf();
			const idS2 : number = regel11.parameter.get(1).valueOf();
			const idF : number = regel11.parameter.get(2).valueOf();
			const key12F : LongArrayKey = new LongArrayKey(idS1, idS2, idF);
			const key21F : LongArrayKey = new LongArrayKey(idS2, idS1, idF);
			DeveloperNotificationException.ifTrue("Dopplung bei Schüler-Schüler-Fach Zusammen/Verbieten!", !setSSF.add(key12F));
			DeveloperNotificationException.ifTrue("Dopplung bei Schüler-Schüler-Fach Zusammen/Verbieten!", !setSSF.add(key21F));
			const sch1 : KursblockungDynSchueler = this.gibSchueler(idS1);
			const sch2 : KursblockungDynSchueler = this.gibSchueler(idS2);
			sch1.regel_11_zusammen_mit_schueler_in_fach(sch2, idF);
		}
		for (const regel12 of MapUtils.getOrCreateArrayList(this._regelMap, GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH)) {
			const idS1 : number = regel12.parameter.get(0).valueOf();
			const idS2 : number = regel12.parameter.get(1).valueOf();
			const idF : number = regel12.parameter.get(2).valueOf();
			const key12F : LongArrayKey = new LongArrayKey(idS1, idS2, idF);
			const key21F : LongArrayKey = new LongArrayKey(idS2, idS1, idF);
			DeveloperNotificationException.ifTrue("Dopplung bei Schüler-Schüler-Fach Zusammen/Verbieten!", !setSSF.add(key12F));
			DeveloperNotificationException.ifTrue("Dopplung bei Schüler-Schüler-Fach Zusammen/Verbieten!", !setSSF.add(key21F));
			const sch1 : KursblockungDynSchueler = this.gibSchueler(idS1);
			const sch2 : KursblockungDynSchueler = this.gibSchueler(idS2);
			sch1.regel_12_verbieten_mit_schueler_in_fach(sch2, idF);
		}
		for (const regel13 of MapUtils.getOrCreateArrayList(this._regelMap, GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER)) {
			const idS1 : number = regel13.parameter.get(0).valueOf();
			const idS2 : number = regel13.parameter.get(1).valueOf();
			for (const fach of input.schuelerGetFachListeGemeinsamerFacharten(idS1, idS2)) {
				const key12F : LongArrayKey = new LongArrayKey(idS1, idS2, fach.id);
				const key21F : LongArrayKey = new LongArrayKey(idS2, idS1, fach.id);
				DeveloperNotificationException.ifTrue("Dopplung bei Schüler-Schüler-Fach Zusammen/Verbieten!", !setSSF.add(key12F));
				DeveloperNotificationException.ifTrue("Dopplung bei Schüler-Schüler-Fach Zusammen/Verbieten!", !setSSF.add(key21F));
				const sch1 : KursblockungDynSchueler = this.gibSchueler(idS1);
				const sch2 : KursblockungDynSchueler = this.gibSchueler(idS2);
				sch1.regel_13_zusammen_mit_schueler(sch2);
			}
		}
		for (const r14 of MapUtils.getOrCreateArrayList(this._regelMap, GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER)) {
			const idS1 : number = r14.parameter.get(0).valueOf();
			const idS2 : number = r14.parameter.get(1).valueOf();
			for (const fach of input.schuelerGetFachListeGemeinsamerFacharten(idS1, idS2)) {
				const key12F : LongArrayKey = new LongArrayKey(idS1, idS2, fach.id);
				const key21F : LongArrayKey = new LongArrayKey(idS2, idS1, fach.id);
				DeveloperNotificationException.ifTrue("Dopplung bei Schüler-Schüler-Fach Zusammen/Verbieten!", !setSSF.add(key12F));
				DeveloperNotificationException.ifTrue("Dopplung bei Schüler-Schüler-Fach Zusammen/Verbieten!", !setSSF.add(key21F));
			}
			const sch1 : KursblockungDynSchueler = this.gibSchueler(idS1);
			const sch2 : KursblockungDynSchueler = this.gibSchueler(idS2);
			sch1.regel_14_verbieten_mit_schueler(sch2);
		}
	}

	private fehlerBeiRegel_15_KURS_MAXIMALE_SCHUELERANZAHL() : void {
		for (const r15 of MapUtils.getOrCreateArrayList(this._regelMap, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL)) {
			const idKurs : number = r15.parameter.get(0).valueOf();
			const maxSuS : number = r15.parameter.get(1);
			const kurs : KursblockungDynKurs = this.gibKurs(idKurs);
			kurs.regel_15_setzeMaxSuS(maxSuS);
		}
	}

	private fehlerBeiRegel_16_SCHUELER_IGNORIEREN() : void {
		for (const r16 of MapUtils.getOrCreateArrayList(this._regelMap, GostKursblockungRegelTyp.SCHUELER_IGNORIEREN)) {
			const idSchueler : number = r16.parameter.get(0).valueOf();
			const schueler : KursblockungDynSchueler = this.gibSchueler(idSchueler);
			schueler.regel_16_sperre();
		}
	}

	private fehlerBeiRegel_18_FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE() : void {
		for (const r18 of MapUtils.getOrCreateArrayList(this._regelMap, GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE)) {
			const idFach : number = r18.parameter.get(0).valueOf();
			const idKursart : number = r18.parameter.get(1);
			const maximalProSchiene : number = r18.parameter.get(2);
			const fachart : KursblockungDynFachart = this.gibFachart(idFach, idKursart);
			fachart.regel_18_maximalProSchiene(maximalProSchiene);
		}
	}

	private gibFachart(fachID : number, kursart : number) : KursblockungDynFachart {
		return this._fachartMap2D.getOrException(fachID, kursart);
	}

	private gibSchueler(schuelerID : number) : KursblockungDynSchueler {
		return DeveloperNotificationException.ifMapGetIsNull(this._schuelerMap, schuelerID);
	}

	private gibKurs(kursID : number) : KursblockungDynKurs {
		return DeveloperNotificationException.ifMapGetIsNull(this._kursMap, kursID);
	}

	/**
	 * Liefert ein neu erzeugtes {@link GostBlockungsergebnisManager}-Objekt.
	 * <br>Dieses Objekt beinhaltet alle Informationen für die GUI.
	 *
	 * @param  pDataManager  Das Eingabe-Objekt (der Daten-Manager).
	 * @param  pErgebnisID   Die ID des Ergebnisses.
	 *
	 * @return ein neu erzeugtes {@link GostBlockungsergebnisManager}-Objekt.
	 */
	gibErzeugtesKursblockungOutput(pDataManager : GostBlockungsdatenManager, pErgebnisID : number) : GostBlockungsergebnisManager {
		const out : GostBlockungsergebnisManager = new GostBlockungsergebnisManager(pDataManager, pErgebnisID);
		const kursSchienenZuordnungen : JavaSet<GostBlockungsergebnisKursSchienenZuordnung> = new HashSet<GostBlockungsergebnisKursSchienenZuordnung>();
		for (const dynKurs of this._kursArr)
			for (const schienenNr of dynKurs.gibSchienenLage()) {
				const idKurs : number = dynKurs.gibDatenbankID();
				const idSchiene : number = out.getOfSchieneID(schienenNr + 1);
				kursSchienenZuordnungen.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(idKurs, idSchiene));
			}
		const uKursSchienen : GostBlockungsergebnisKursSchienenZuordnungUpdate = out.kursSchienenUpdate_01a_FUEGE_KURS_SCHIENEN_PAARE_HINZU(kursSchienenZuordnungen);
		out.kursSchienenUpdateExecute(uKursSchienen);
		const kursSchuelerZuordnungen : JavaSet<GostBlockungsergebnisKursSchuelerZuordnung> = new HashSet<GostBlockungsergebnisKursSchuelerZuordnung>();
		for (const dynSchueler of this._schuelerArr)
			for (const kurs of dynSchueler.gibKurswahlen())
				if (kurs !== null) {
					const idKurs : number = kurs.gibDatenbankID();
					const idSchueler : number = dynSchueler.gibDatenbankID();
					kursSchuelerZuordnungen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(idKurs, idSchueler));
				}
		for (const gRegel of pDataManager.regelGetListe())
			if (gRegel.typ === GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ) {
				const idSchueler : number = gRegel.parameter.get(0).valueOf();
				const idKurs : number = gRegel.parameter.get(1).valueOf();
				kursSchuelerZuordnungen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(idKurs, idSchueler));
			}
		const uKursSchueler : GostBlockungsergebnisKursSchuelerZuordnungUpdate = out.kursSchuelerUpdate_03a_FUEGE_KURS_SCHUELER_PAARE_HINZU(kursSchuelerZuordnungen);
		out.kursSchuelerUpdateExecute(uKursSchueler);
		return out;
	}

	/**
	 * Liefert das Logger-Objekt für Benutzerhinweise, Warnungen und Fehler.
	 *
	 * @return Das Logger-Objekt für Benutzerhinweise, Warnungen und Fehler.
	 */
	gibLogger() : Logger {
		return this._logger;
	}

	/**
	 * Liefert das {@link Random}-Objekt.
	 *
	 * @return das {@link Random}-Objekt.
	 */
	gibRandom() : Random {
		return this._random;
	}

	/**
	 * Liefert das Statistik-Objekt (für Anfragen zu Nichtwahlen, Kursdifferenzen, etc.).
	 *
	 * @return Das Statistik-Objekt (für Anfragen zu Nichtwahlen, Kursdifferenzen, etc.).
	 */
	gibStatistik() : KursblockungDynStatistik {
		return this._statistik;
	}

	/**
	 * Liefert die maximale Blockungszeit in Millisekunden. Entweder handelt es sich um einen Standardwert oder der Wert
	 * wurde im Konstruktor als Regel übergeben.
	 *
	 * @return Liefert die maximale Blockungszeit in Millisekunden.
	 */
	gibBlockungszeitMillis() : number {
		return this._maxTimeMillis;
	}

	/**
	 * Liefert die maximal erlaubte Anzahl an Schienen. Entweder handelt es sich um einen Standardwert oder der Wert
	 * wurde im Konstruktor als Regel übergeben.
	 *
	 * @return Liefert die maximal erlaubte Anzahl an Schienen.
	 */
	gibSchienenAnzahl() : number {
		return this._schienenArr.length;
	}

	/**
	 * Liefert alle Kurse.
	 *
	 * @return Array aller Kurse.
	 */
	gibKurseAlle() : Array<KursblockungDynKurs> {
		return this._kursArr;
	}

	/**
	 * Liefert alle Kurse deren Lage nicht komplett fixiert ist.
	 *
	 * @return Array aller Kurse, deren Schienenlage noch veränderbar ist.
	 */
	gibKurseDieFreiSind() : Array<KursblockungDynKurs> {
		return this._kursArrFrei;
	}

	/**
	 * Liefert die Anzahl alle Kurse deren Lage nicht komplett fixiert ist.
	 *
	 * @return Anzahl aller Kurse, deren Schienenlage noch veränderbar ist.
	 */
	gibKurseDieFreiSindAnzahl() : number {
		return this._kursArrFrei.length;
	}

	/**
	 * Liefert einen Long-Wert, der einer Bewertung der Fachwahlmatrix entspricht. Je kleiner der Wert, desto besser ist
	 * die Bewertung.
	 *
	 * @return Long-Wert, der einer Bewertung der Fachwahlmatrix entspricht.
	 */
	gibBewertungFachartPaar() : number {
		return this._statistik.gibBewertungFachartPaar();
	}

	/**
	 * Liefert ein Array aller Schülerinnen und Schüler.
	 * Falls der Parameter {@code pNurMultiKurse} TRUE ist, dann werden nur SuS mit mindestens einem Multikurs ausgewählt.
	 *
	 * @param  pNurMultiKurse Falls TRUE, dann werden nur SuS mit mindestens einem Multikurs ausgewählt.
	 *
	 * @return ein Array aller Schülerinnen und Schüler.
	 */
	gibSchuelerArray(pNurMultiKurse : boolean) : Array<KursblockungDynSchueler> {
		if (pNurMultiKurse) {
			const list : LinkedCollection<KursblockungDynSchueler> = new LinkedCollection<KursblockungDynSchueler>();
			for (const schueler of this._schuelerArr)
				if (schueler.gibHatMultikurs())
					list.addLast(schueler);
			const temp : Array<KursblockungDynSchueler> = Array(list.size()).fill(null);
			for (let i : number = 0; i < temp.length; i++)
				temp[i] = list.removeFirst();
			return temp;
		}
		return this._schuelerArr;
	}

	/**
	 * Liefert ein Array aller Schülerinnen und Schüler.
	 *
	 * @return Ein Array aller Schülerinnen und Schüler.
	 */
	gibSchuelerArrayAlle() : Array<KursblockungDynSchueler> {
		return this._schuelerArr;
	}

	/**
	 * Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen) des Zustandes S sich
	 * verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 *
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen) des Zustandes S sich
	 *         verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 */
	gibBewertungJetztBesserAlsS() : number {
		return this._statistik.gibBewertungZustandS_NW_KD();
	}

	/**
	 * Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Nichtwahlen, Kursdiffenzen,
	 * Fachwahlmatrix) des Zustandes-K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 *
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Nichtwahlen, Kursdiffenzen, Fachwahlmatrix) des
	 *         Zustandes-K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 */
	gibCompareZustandK_NW_KD_FW() : number {
		return this._statistik.gibCompareZustandK_NW_KD_FW();
	}

	/**
	 * Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Nichtwahlen, Kursdiffenzen,
	 * Fachwahlmatrix) des Zustandes-G sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 *
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Nichtwahlen, Kursdiffenzen, Fachwahlmatrix) des
	 *         Zustandes-G sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 */
	gibCompareZustandG_NW_KD_FW() : number {
		return this._statistik.gibCompareZustandG_NW_KD_FW();
	}

	/**
	 * Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Fachwahlmatrix, Nichtwahlen,
	 * Kursdiffenzen) des Zustandes K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 *
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Fachwahlmatrix, Nichtwahlen, Kursdiffenzen) des
	 *         Zustandes K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 */
	gibBewertungK_FW_NW_KD_JetztBesser() : number {
		return this._statistik.gibCompareZustandK_FW_NW_KD();
	}

	/**
	 * Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen) des Zustandes S sich
	 * verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 *
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen) des Zustandes K sich
	 *         verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 */
	gibBewertung_NW_KD_JetztS() : number {
		return this._statistik.gibBewertungZustandS_NW_KD();
	}

	/**
	 * Liefert TRUE, falls dieses Objekt besser ist als das übergebene Objekt b.
	 *
	 * @param b  Das zu vergleichende Objekt.
	 *
	 * @return TRUE, falls dieses Objekt besser ist als das übergebene Objekt b.
	 */
	gibIstBesser_NW_KD_FW_Als(b : KursblockungDynDaten) : boolean {
		return this._statistik.gibIstBesser_NW_KD_FW_Als(b._statistik);
	}

	/**
	 * Debug Ausgaben. Nur für Testzwecke.
	 */
	debug() : void {
		this._logger.modifyIndent(+4);
		this._logger.logLn("########## Schienen ##########");
		for (let i : number = 0; i < this._schienenArr.length; i++) {
			this._logger.logLn("Schiene " + (i + 1));
			this._schienenArr[i].debug(false);
		}
		this._logger.logLn("########## Facharten ##########");
		for (const fa of this._fachartArr) {
			this._logger.logLn("Fachart " + fa + " --> " + fa.gibKursdifferenz());
			fa.debug(this._schuelerArr);
		}
		this._logger.modifyIndent(-4);
		this._statistik.debug("");
	}

	/**
	 * Speichert die Bewertung, die Kursverteilung und die Schülerverteilung im Zustand S.
	 */
	aktionZustandSpeichernS() : void {
		this._statistik.aktionBewertungSpeichernS();
		for (const kurs of this._kursArr)
			kurs.aktionZustandSpeichernS();
		for (const schueler of this._schuelerArr)
			schueler.aktionZustandSpeichernS();
	}

	/**
	 * Speichert die Bewertung, die Kursverteilung und die Schülerverteilung im Zustand K.
	 */
	aktionZustandSpeichernK() : void {
		this._statistik.aktionBewertungSpeichernK();
		for (const kurs of this._kursArr)
			kurs.aktionZustandSpeichernK();
		for (const schueler of this._schuelerArr)
			schueler.aktionZustandSpeichernK();
	}

	/**
	 * Speichert die Bewertung, die Kursverteilung und die Schülerverteilung im Zustand G.
	 */
	aktionZustandSpeichernG() : void {
		this._statistik.aktionBewertungSpeichernG();
		for (const kurs of this._kursArr)
			kurs.aktionZustandSpeichernG();
		for (const schueler of this._schuelerArr)
			schueler.aktionZustandSpeichernG();
	}

	/**
	 * Lädt den zuvor gespeicherten Zustand S (Kursverteilung und Schülerverteilung).
	 */
	aktionZustandLadenS() : void {
		for (const schueler of this._schuelerArr)
			schueler.aktionKurseAlleEntfernen();
		for (const kurs of this._kursArr)
			kurs.aktionZustandLadenS();
		for (const schueler of this._schuelerArr)
			schueler.aktionZustandLadenS();
	}

	/**
	 * Lädt den zuvor gespeicherten Zustand K (Kursverteilung und Schülerverteilung).
	 */
	aktionZustandLadenK() : void {
		for (const schueler of this._schuelerArr)
			schueler.aktionKurseAlleEntfernen();
		for (const kurs of this._kursArr)
			kurs.aktionZustandLadenK();
		for (const schueler of this._schuelerArr)
			schueler.aktionZustandLadenK();
	}

	/**
	 * Lädt den zuvor gespeicherten Zustand K einer anderen {@link KursblockungDynDaten}-Objekts (Kursverteilung und Schülerverteilung).
	 *
	 * @param b  Das andere {@link KursblockungDynDaten}-Objekt.
	 */
	aktionZustandLadenVon(b : KursblockungDynDaten) : void {
		if (this as unknown === b as unknown) {
			console.log(JSON.stringify("Identisch!"));
			return;
		}
		for (const schueler of this._schuelerArr)
			schueler.aktionKurseAlleEntfernen();
		for (let i : number = 0; i < this._kursArr.length; i++)
			this._kursArr[i].aktionZustandLadenVon(b._kursArr[i], this._schienenArr);
		for (let i : number = 0; i < this._schuelerArr.length; i++)
			this._schuelerArr[i].aktionZustandLadenVon(b._schuelerArr[i], this._kursArr);
	}

	/**
	 * Lädt den zuvor gespeicherten Zustand G (Kursverteilung und Schülerverteilung).
	 */
	aktionZustandLadenG() : void {
		for (const schueler of this._schuelerArr)
			schueler.aktionKurseAlleEntfernen();
		for (const kurs of this._kursArr)
			kurs.aktionZustandLadenG();
		for (const schueler of this._schuelerArr)
			schueler.aktionZustandLadenG();
	}

	/**
	 * Lädt den zuvor gespeicherten Zustand K (nur Kursverteilung, ohne Schülerverteilung).
	 */
	aktionZustandLadenKohneSuS() : void {
		for (const schueler of this._schuelerArr)
			schueler.aktionKurseAlleEntfernen();
		for (const kurs of this._kursArr)
			kurs.aktionZustandLadenK();
	}

	/**
	 * Entfernt alle SuS aus ihren Kursen.
	 */
	aktionSchuelerAusAllenKursenEntfernen() : void {
		for (const s of this._schuelerArr)
			s.aktionKurseAlleEntfernen();
	}

	/**
	 * Verteilt alle Kurse auf ihre Schienen zufällig. Kurse die keinen Freiheitsgrad haben, werden dabei ignoriert.
	 */
	aktionKurseFreieZufaelligVerteilen() : void {
		for (const kurs of this._kursArrFrei)
			kurs.aktionZufaelligVerteilen();
	}

	/**
	 * Verteilt genau einen Kurs zufällig. Kurse die keinen Freiheitsgrad haben, werden dabei ignoriert.
	 */
	aktionKursVerteilenEinenZufaelligenFreien() : void {
		if (this._kursArrFrei.length === 0)
			return;
		const index : number = this._random.nextInt(this._kursArrFrei.length);
		const kurs : KursblockungDynKurs = this._kursArrFrei[index];
		kurs.aktionZufaelligVerteilen();
	}

	/**
	 * Verteilt einen Kurs zufällig. Kurse die keinen Freiheitsgrad haben und Multikurse, werden dabei ignoriert.
	 */
	aktionKursFreienEinenZufaelligVerteilenAberNichtMultikurse() : void {
		if (this._kursArrFrei.length === 0)
			return;
		const perm : Array<number> | null = KursblockungStatic.gibPermutation(this._random, this._kursArrFrei.length);
		for (const index of perm) {
			const kurs : KursblockungDynKurs = this._kursArrFrei[index];
			if (kurs.gibSchienenAnzahl() === 1)
				kurs.aktionZufaelligVerteilen();
		}
	}

	/**
	 * Verteilt die SuS auf die jetzige Kurslage. Pro S. werden erst die Multikurse verteilt, dann werden die übrigen
	 * Kurse mit Hilfe eines spezielle bipartiten Matching-Algorithmus verteilt. Sobald ein S. seine Nichtwahlen durch
	 * eine Veränderung der Kurslage reduzieren könnte, wird die Kurslage verändert.
	 *
	 * @return TRUE, falls es zu einer Veränderung der Kurslage kam.
	 */
	aktionKurseVerteilenNachSchuelerwunsch() : boolean {
		let kurslagenVeraenderung : boolean = false;
		const perm : Array<number> = KursblockungStatic.gibPermutation(this._random, this._schuelerArr.length);
		for (const p of perm) {
			const schueler : KursblockungDynSchueler | null = this._schuelerArr[p];
			schueler.aktionKurseVerteilenNurMultikurseZufaellig();
			kurslagenVeraenderung = kurslagenVeraenderung || schueler.aktionKurseVerteilenNachDeinemWunsch();
		}
		return kurslagenVeraenderung;
	}

	/**
	 * Verteilt die SuS auf die jetzige Kurslage. Pro S. werden erst die Multikurse verteilt, dann werden die übrigen
	 * Kurse mit Hilfe eines bipartiten Matching-Algorithmus verteilt. Bereits belegte Facharten werden übersprungen.
	 */
	aktionSchuelerVerteilenMitBipartitemMatching() : void {
		const perm : Array<number> = KursblockungStatic.gibPermutation(this._random, this._schuelerArr.length);
		for (const i of perm) {
			const schueler : KursblockungDynSchueler | null = this._schuelerArr[i];
			schueler.aktionKurseVerteilenNurMultikurseZufaellig();
			schueler.aktionKurseVerteilenNurFachartenMitEinemErlaubtenKurs();
			schueler.aktionKurseVerteilenMitBipartiteMatching();
		}
	}

	/**
	 * Verteilt die SuS auf die jetzige Kurslage. Pro S. werden erst die Multikurse verteilt, dann werden die übrigen
	 * Kurse mit Hilfe eines gewichteten Bipartiten-Matching-Algorithmus verteilt.
	 */
	aktionSchuelerVerteilenMitGewichtetenBipartitemMatching() : void {
		const perm : Array<number> = KursblockungStatic.gibPermutation(this._random, this._schuelerArr.length);
		for (const i of perm) {
			const schueler : KursblockungDynSchueler | null = this._schuelerArr[i];
			schueler.aktionKurseVerteilenNurMultikurseZufaellig();
			schueler.aktionKurseVerteilenNurFachartenMitEinemErlaubtenKurs();
			schueler.aktionKurseVerteilenMitBipartiteMatchingGewichtetem();
		}
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.kursblockung.KursblockungDynDaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.kursblockung.KursblockungDynDaten'].includes(name);
	}

	public static class = new Class<KursblockungDynDaten>('de.svws_nrw.core.kursblockung.KursblockungDynDaten');

}

export function cast_de_svws_nrw_core_kursblockung_KursblockungDynDaten(obj : unknown) : KursblockungDynDaten {
	return obj as KursblockungDynDaten;
}
