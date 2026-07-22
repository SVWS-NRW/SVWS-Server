import { JavaObject } from '../../java/lang/JavaObject';
import { HashMap2D } from '../../core/adt/map/HashMap2D';
import { GostBlockungsergebnisManager } from '../../core/utils/gost/GostBlockungsergebnisManager';
import { KursblockungDynFachart } from '../../core/kursblockung/KursblockungDynFachart';
import { GostBlockungsergebnisKursSchuelerZuordnung } from '../../core/data/gost/GostBlockungsergebnisKursSchuelerZuordnung';
import type { JavaSet } from '../../java/util/JavaSet';
import { KursblockungStatic } from '../../core/kursblockung/KursblockungStatic';
import { StringBuilder } from '../../java/lang/StringBuilder';
import { HashMap } from '../../java/util/HashMap';
import { KursblockungDynSchiene } from '../../core/kursblockung/KursblockungDynSchiene';
import { ArrayList } from '../../java/util/ArrayList';
import { KursblockungDynKurs } from '../../core/kursblockung/KursblockungDynKurs';
import { LongArrayKey } from '../../core/adt/LongArrayKey';
import { DeveloperNotificationException } from '../../core/exceptions/DeveloperNotificationException';
import { JavaString } from '../../java/lang/JavaString';
import { Logger } from '../../core/logger/Logger';
import { GostBlockungRegel } from '../../core/data/gost/GostBlockungRegel';
import { GostKursart } from '../../core/types/gost/GostKursart';
import { LogLevel } from '../../core/logger/LogLevel';
import { GostKursblockungRegelTyp } from '../../core/types/kursblockung/GostKursblockungRegelTyp';
import { PairIteratorModus } from '../../core/adt/iterator/PairIteratorModus';
import { Random } from '../../java/util/Random';
import { GostBlockungsergebnisKursSchienenZuordnung } from '../../core/data/gost/GostBlockungsergebnisKursSchienenZuordnung';
import { PairIterable } from '../../core/adt/iterator/PairIterable';
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
	private readonly rnd: Random;

	/**
	 * Logger für Benutzerhinweise, Warnungen und Fehler.
	 */
	private readonly log: Logger;

	/**
	 * Alle Regeln nach ihrer ID gruppiert und in einer Liste der Reihenfolge nach gespeichert.
	 */
	private readonly regelMap: JavaMap<GostKursblockungRegelTyp, List<GostBlockungRegel>>;

	/**
	 * Die maximale Blockungszeit in Millisekunden.
	 */
	private readonly maxTimeMillis: number;

	/**
	 * Diese Datenstruktur speichert die Schienen und ihre Kurse.
	 */
	private schienenMenge: Array<KursblockungDynSchiene>;

	/**
	 * Alles Kurse.
	 */
	private kursMenge: Array<KursblockungDynKurs>;

	/**
	 * Alle Kurse, die noch über Schienen wandern können.
	 */
	private kursMengeFrei: Array<KursblockungDynKurs>;

	/**
	 * Map für schnellen Zugriff auf die Kurse über ihre ID.
	 */
	private readonly kursMap: HashMap<number, KursblockungDynKurs>;

	/**
	 * Alle Facharten. Fachart meint Fach + Kursart, z.B. "D;GK".
	 */
	private fachartMenge: Array<KursblockungDynFachart>;

	/**
	 * Map für schnellen Zugriff auf die Facharten über FachID und KursartID.
	 */
	private readonly fachartMap2D: HashMap2D<number, number, KursblockungDynFachart>;

	/**
	 * Alle SuS.
	 */
	private schuelerMenge: Array<KursblockungDynSchueler>;

	/**
	 * Map für schnellen Zugriff auf die SuS über ihre ID.
	 */
	private readonly schuelerMap: HashMap<number, KursblockungDynSchueler>;

	/**
	 * Das Statistik-Objekt speichert die aktuellen Nichtwahlen, Kursdifferenzen und weitere Daten.
	 */
	private readonly statistik: KursblockungDynStatistik;


	/**
	 * Der Konstruktor der Klasse liest alle Daten von {@link GostBlockungsdatenManager} ein und baut die relevanten Datenstrukturen auf.
	 *
	 * @param random   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param logger   Ein {@link Logger}-Objekt für Benutzerhinweise, Warnungen und Fehler.
	 * @param input    Die Eingabedaten (Schnittstelle zur GUI).
	 */
	public constructor(random: Random, logger: Logger, input: GostBlockungsdatenManager) {
		super();
		this.rnd = random;
		this.log = logger;
		this.regelMap = new ArrayMap(GostKursblockungRegelTyp.values());
		this.maxTimeMillis = input.getMaxTimeMillis();
		this.schienenMenge = Array(0).fill(null);
		this.kursMenge = Array(0).fill(null);
		this.kursMengeFrei = Array(0).fill(null);
		this.kursMap = new HashMap();
		this.fachartMenge = Array(0).fill(null);
		this.fachartMap2D = new HashMap2D();
		this.schuelerMenge = Array(0).fill(null);
		this.schuelerMap = new HashMap();
		this.statistik = new KursblockungDynStatistik(this.log);
		this.fehlerBeiReferenzen(input);
		this.fehlerBeiRegelGruppierung(input.daten().regeln);
		this.fehlerBeiSchuelerErstellung(input);
		this.fehlerBeiSchienenErzeugung(input.schieneGetAnzahl());
		this.fehlerBeiFachartenErstellung(input, this.schuelerMenge.length, this.schienenMenge.length);
		this.fehlerBeiSchuelerFachwahlenErstellung(input, this.schuelerMenge);
		this.fehlerBeiStatistikErstellung(this.fachartMenge, this.schuelerMenge, input);
		this.fehlerBeiKursErstellung(input, this.schuelerMenge.length);
		this.fehlerBeiKursFreiErstellung();
		this.fehlerBeiFachartKursArrayErstellung();
		this.fehlerBeiRegel4oder5();
		this.fehlerBeiRegel7oder8();
		this.fehlerBeiRegel9();
		this.fehlerBeiRegel10(input);
		this.fehlerBeiRegel11bis14(input);
		this.fehlerBeiRegel15();
		this.fehlerBeiRegel16();
		this.fehlerBeiRegel18();
		this.aktionZustandSpeichernS();
		this.aktionZustandSpeichernK();
		this.aktionZustandSpeichernG();
	}

	/**
	 * Überprüft alle Referenzen in {@link KursblockungInput} und auch die referentielle Integrität.
	 *
	 * @param input Das {@link KursblockungInput}-Objekt von der GUI.
	 */
	private fehlerBeiReferenzen(input: GostBlockungsdatenManager): void {
		DeveloperNotificationException.ifNull("pInput", input);
		DeveloperNotificationException.ifNull("pInput.daten()", input.daten());
		DeveloperNotificationException.ifNull("pInput.daten().fachwahlen", input.daten().fachwahlen);
		DeveloperNotificationException.ifNull("pInput.faecherManager()", input.faecherManager());
		DeveloperNotificationException.ifNull("pInput.faecherManager().faecher()", input.faecherManager().faecher());
		DeveloperNotificationException.ifNull("GostKursart.values()", GostKursart.values());
		DeveloperNotificationException.ifNull("pInput.daten().kurse", input.daten().kurse);
		DeveloperNotificationException.ifNull("pInput.daten().regeln", input.daten().regeln);
		DeveloperNotificationException.ifInvalidID("pInput.getID()", input.getID());
		DeveloperNotificationException.ifArrayIsEmpty("GostKursart.values()", GostKursart.values());
		DeveloperNotificationException.ifCollectionIsEmpty("pInput.daten().fachwahlen", input.daten().fachwahlen);
		DeveloperNotificationException.ifCollectionIsEmpty("pInput.faecherManager().faecher()", input.faecherManager().faecher());
		DeveloperNotificationException.ifCollectionIsEmpty("pInput.daten().kurse", input.daten().kurse);
		const schienenAnzahl: number = input.schieneGetAnzahl();
		DeveloperNotificationException.ifSmaller("schienenAnzahl", schienenAnzahl, 1);
		const usedSchiene: HashSet<number> | null = new HashSet<number>();
		for (const gSchiene of input.daten().schienen) {
			DeveloperNotificationException.ifInvalidID(JavaString.format("Die G-Schiene %s hat keine gültige ID.", gSchiene), gSchiene.id);
			DeveloperNotificationException.ifSmaller(JavaString.format("Die G-Schiene %s ist zu klein!", gSchiene), gSchiene.nummer, 1);
			DeveloperNotificationException.ifGreater(JavaString.format("Die G-Schiene %s ist zu groß!", gSchiene), gSchiene.nummer, schienenAnzahl);
			DeveloperNotificationException.ifSetAddsDuplicate("usedSchiene", usedSchiene, gSchiene.nummer);
		}
		const setKursarten: HashSet<number> = new HashSet<number>();
		for (const iKursart of GostKursart.values()) {
			DeveloperNotificationException.ifNull("iKursart", iKursart);
			DeveloperNotificationException.ifInvalidID("iKursart.id", iKursart.id);
			DeveloperNotificationException.ifSetAddsDuplicate(JavaString.format("Doppelte ID=%d in 'setKursarten'.", iKursart.id), setKursarten, iKursart.id);
		}
		const setFaecher: HashSet<number> = new HashSet<number>();
		for (const iFach of input.faecherManager().faecher()) {
			DeveloperNotificationException.ifNull("iFach", iFach);
			DeveloperNotificationException.ifInvalidID("iFach.id", iFach.id);
			DeveloperNotificationException.ifSetAddsDuplicate(JavaString.format("Doppele ID=%d in 'setFaecher'.", iFach.id), setFaecher, iFach.id);
		}
		const setKurse: HashSet<number> = new HashSet<number>();
		for (const iKurs of input.daten().kurse) {
			DeveloperNotificationException.ifNull("iKurs", iKurs);
			DeveloperNotificationException.ifInvalidID("iKurs.id", iKurs.id);
			DeveloperNotificationException.ifSetNotContains(JavaString.format("Kurs ID=%d/NR=%d referenziert das Fach %d, aber es fehlt in 'setFaecher'.", iKurs.id, iKurs.nummer, iKurs.fach_id), setFaecher, iKurs.fach_id);
			DeveloperNotificationException.ifSetNotContains(JavaString.format("Kurs ID=%d/NR=%d referenziert die Kursart Fach %d, aber es fehlt in 'setKursarten'.", iKurs.id, iKurs.nummer, iKurs.kursart), setKursarten, iKurs.kursart);
			DeveloperNotificationException.ifSetAddsDuplicate(JavaString.format("Kurs ID=%d/NR=%d ist bereits vorhanden in 'setKurse'.", iKurs.id, iKurs.nummer), setKurse, iKurs.id);
		}
		const setSchueler: HashSet<number> = new HashSet<number>();
		for (const gSchueler of input.daten().schueler) {
			DeveloperNotificationException.ifSetAddsDuplicate(JavaString.format("Schüler ID=%d ist bereits vorhanden in 'setSchueler'.", gSchueler.id), setSchueler, gSchueler.id);
		}
		for (const iFachwahl of input.daten().fachwahlen) {
			DeveloperNotificationException.ifNull("iFachwahl", iFachwahl);
			DeveloperNotificationException.ifInvalidID("iFachwahl.schuelerID", iFachwahl.schuelerID);
			DeveloperNotificationException.ifSetNotContains(JavaString.format("Fachwahl (%d,%d,%d) referenziert Fach ID=%d, aber es fehlt in 'setFaecher'.", iFachwahl.fachID, iFachwahl.kursartID, iFachwahl.schuelerID, iFachwahl.fachID), setFaecher, iFachwahl.fachID);
			DeveloperNotificationException.ifSetNotContains(JavaString.format("Fachwahl (%d,%d,%d) referenziert Kursart ID=%d, aber es fehlt in 'setKursarten'.", iFachwahl.fachID, iFachwahl.kursartID, iFachwahl.schuelerID, iFachwahl.kursartID), setKursarten, iFachwahl.kursartID);
			DeveloperNotificationException.ifSetNotContains(JavaString.format("Fachwahl (%d,%d,%d) referenziert Schüler ID=%d, aber es fehlt in 'setSchueler'.", iFachwahl.fachID, iFachwahl.kursartID, iFachwahl.schuelerID, iFachwahl.schuelerID), setSchueler, iFachwahl.schuelerID);
		}
		for (const iRegel of input.daten().regeln) {
			DeveloperNotificationException.ifNull("iRegel", iRegel);
			DeveloperNotificationException.ifNull("iRegel.parameter", iRegel.parameter);
			DeveloperNotificationException.ifInvalidID("iRegel.id", iRegel.id);
			const gostRegel: GostKursblockungRegelTyp = GostKursblockungRegelTyp.fromTyp(iRegel.typ);
			const daten: Array<number> = iRegel.parameter.toArray(Array(0).fill(null));
			for (let i: number = 0; i < daten.length; i++) {
				DeveloperNotificationException.ifNull("daten[" + i + "]", daten[i]);
			}
			switch (gostRegel) {
				case GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS: {
					KursblockungDynDaten.fehlerBeiReferenzenRegeltyp1(daten, setKursarten, schienenAnzahl);
					break;
				}
				case GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE: {
					KursblockungDynDaten.fehlerBeiReferenzenRegeltyp2(daten, setKurse, schienenAnzahl);
					break;
				}
				case GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE: {
					KursblockungDynDaten.fehlerBeiReferenzenRegeltyp3(daten, setKurse, schienenAnzahl);
					break;
				}
				case GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS: {
					KursblockungDynDaten.fehlerBeiReferenzenRegeltyp4(daten, setSchueler, setKurse);
					break;
				}
				case GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS: {
					KursblockungDynDaten.fehlerBeiReferenzenRegeltyp5(daten, setSchueler, setKurse);
					break;
				}
				case GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS: {
					KursblockungDynDaten.fehlerBeiReferenzenRegeltyp6(daten, setKursarten, schienenAnzahl);
					break;
				}
				case GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS: {
					KursblockungDynDaten.fehlerBeiReferenzenRegeltyp7(daten, setKurse);
					break;
				}
				case GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS: {
					KursblockungDynDaten.fehlerBeiReferenzenRegeltyp8(daten, setKurse);
					break;
				}
				case GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN: {
					KursblockungDynDaten.fehlerBeiReferenzenRegeltyp9(daten, setKurse);
					break;
				}
				case GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN: {
					KursblockungDynDaten.fehlerBeiReferenzenRegeltyp10(daten);
					break;
				}
				case GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH: {
					KursblockungDynDaten.fehlerBeiReferenzenRegeltyp11(daten, setSchueler, setFaecher);
					break;
				}
				case GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH: {
					KursblockungDynDaten.fehlerBeiReferenzenRegeltyp12(daten, setSchueler, setFaecher);
					break;
				}
				case GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER: {
					KursblockungDynDaten.fehlerBeiReferenzenRegeltyp13(daten, setSchueler);
					break;
				}
				case GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER: {
					KursblockungDynDaten.fehlerBeiReferenzenRegeltyp14(daten, setSchueler);
					break;
				}
				case GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL: {
					KursblockungDynDaten.fehlerBeiReferenzenRegeltyp15(daten, setKurse);
					break;
				}
				case GostKursblockungRegelTyp.SCHUELER_IGNORIEREN: {
					KursblockungDynDaten.fehlerBeiReferenzenRegeltyp16(daten, setSchueler);
					break;
				}
				case GostKursblockungRegelTyp.KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN: {
					KursblockungDynDaten.fehlerBeiReferenzenRegeltyp17(daten, setKurse);
					break;
				}
				case GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE: {
					KursblockungDynDaten.fehlerBeiReferenzenRegeltyp18(daten, setFaecher, setKursarten);
					break;
				}
				default: {
					throw new DeveloperNotificationException("Unbekannter Regeltyp!");
				}
			}
		}
	}

	private static ueberpruefeDatenLaenge(regelName: string, daten: Array<number>, expectedLength: number): void {
		const length: number = daten.length;
		DeveloperNotificationException.ifTrue(JavaString.format("%s: daten.length=%d, statt %d!", regelName, length, expectedLength), length !== expectedLength);
	}

	private static fehlerBeiReferenzenRegeltyp1(daten: Array<number>, setKursarten: HashSet<number>, schienenAnzahl: number): void {
		KursblockungDynDaten.ueberpruefeDatenLaenge("KURSART_SPERRE_SCHIENEN_VON_BIS", daten, 3);
		const kursartID: number = daten[0];
		const von: number = daten[1];
		const bis: number = daten[2];
		DeveloperNotificationException.ifSetNotContains(JavaString.format("KURSART_SPERRE_SCHIENEN_VON_BIS(%d, %d, %d): Kursart nicht vorhanden!", kursartID, von, bis), setKursarten, kursartID);
		DeveloperNotificationException.ifTrue(JavaString.format("KURSART_SPERRE_SCHIENEN_VON_BIS(%d, %d, %d): Parameter sind unlogisch!", kursartID, von, bis), !((von >= 1) && (von <= bis) && (bis <= schienenAnzahl)));
	}

	private static fehlerBeiReferenzenRegeltyp2(daten: Array<number>, setKurse: HashSet<number>, schienenAnzahl: number): void {
		KursblockungDynDaten.ueberpruefeDatenLaenge("KURS_FIXIERE_IN_SCHIENE", daten, 2);
		const kursID: number = daten[0].valueOf();
		const schiene: number = daten[1];
		DeveloperNotificationException.ifSetNotContains(JavaString.format("KURS_FIXIERE_IN_SCHIENE(%d, %d): Kurs-ID nicht vorhanden!", kursID, schiene), setKurse, kursID);
		DeveloperNotificationException.ifTrue(JavaString.format("KURS_FIXIERE_IN_SCHIENE(%d, %d): Parameter sind unlogisch!", kursID, schiene), !((schiene >= 1) && (schiene <= schienenAnzahl)));
	}

	private static fehlerBeiReferenzenRegeltyp3(daten: Array<number>, setKurse: HashSet<number>, schienenAnzahl: number): void {
		KursblockungDynDaten.ueberpruefeDatenLaenge("KURS_SPERRE_IN_SCHIENE", daten, 2);
		const kursID: number = daten[0].valueOf();
		const schiene: number = daten[1];
		DeveloperNotificationException.ifSetNotContains(JavaString.format("KURS_SPERRE_IN_SCHIENE(%d, %d): Kurs-ID nicht vorhanden!", kursID, schiene), setKurse, kursID);
		DeveloperNotificationException.ifTrue(JavaString.format("KURS_SPERRE_IN_SCHIENE(%d, %d): Parameter sind unlogisch!", kursID, schiene), !((schiene >= 1) && (schiene <= schienenAnzahl)));
	}

	private static fehlerBeiReferenzenRegeltyp4(daten: Array<number>, setSchueler: HashSet<number>, setKurse: HashSet<number>): void {
		KursblockungDynDaten.ueberpruefeDatenLaenge("SCHUELER_FIXIEREN_IN_KURS", daten, 2);
		const schuelerID: number = daten[0].valueOf();
		const kursID: number = daten[1].valueOf();
		DeveloperNotificationException.ifSetNotContains(JavaString.format("SCHUELER_FIXIEREN_IN_KURS(%d, %d): Schüler-ID nicht vorhanden!", schuelerID, kursID), setSchueler, schuelerID);
		DeveloperNotificationException.ifSetNotContains(JavaString.format("SCHUELER_FIXIEREN_IN_KURS(%d, %d): Kurs-ID nicht vorhanden!", schuelerID, kursID), setKurse, kursID);
	}

	private static fehlerBeiReferenzenRegeltyp5(daten: Array<number>, setSchueler: HashSet<number>, setKurse: HashSet<number>): void {
		KursblockungDynDaten.ueberpruefeDatenLaenge("SCHUELER_VERBIETEN_IN_KURS", daten, 2);
		const schuelerID: number = daten[0].valueOf();
		const kursID: number = daten[1].valueOf();
		DeveloperNotificationException.ifSetNotContains(JavaString.format("SCHUELER_VERBIETEN_IN_KURS(%d, %d): Schüler-ID nicht vorhanden!", schuelerID, kursID), setSchueler, schuelerID);
		DeveloperNotificationException.ifSetNotContains(JavaString.format("SCHUELER_VERBIETEN_IN_KURS(%d, %d): Kurs-ID nicht vorhanden!", schuelerID, kursID), setKurse, kursID);
	}

	private static fehlerBeiReferenzenRegeltyp6(daten: Array<number>, setKursarten: HashSet<number>, schienenAnzahl: number): void {
		KursblockungDynDaten.ueberpruefeDatenLaenge("KURSART_ALLEIN_IN_SCHIENEN_VON_BIS", daten, 3);
		const kursartID: number = daten[0];
		const von: number = daten[1];
		const bis: number = daten[2];
		DeveloperNotificationException.ifSetNotContains(JavaString.format("KURSART_ALLEIN_IN_SCHIENEN_VON_BIS(%d, %d, %d): Kursart nicht vorhanden!", kursartID, von, bis), setKursarten, kursartID);
		DeveloperNotificationException.ifTrue(JavaString.format("KURSART_ALLEIN_IN_SCHIENEN_VON_BIS(%d, %d, %d): Parameter sind unlogisch!", kursartID, von, bis), !((von >= 1) && (von <= bis) && (bis <= schienenAnzahl)));
	}

	private static fehlerBeiReferenzenRegeltyp7(daten: Array<number>, setKurse: HashSet<number>): void {
		KursblockungDynDaten.ueberpruefeDatenLaenge("KURS_VERBIETEN_MIT_KURS", daten, 2);
		const kursID1: number = daten[0].valueOf();
		const kursID2: number = daten[1].valueOf();
		DeveloperNotificationException.ifSetNotContains(JavaString.format("KURS_VERBIETEN_MIT_KURS(%d, %d): Kurs-ID1 nicht vorhanden!", kursID1, kursID2), setKurse, kursID1);
		DeveloperNotificationException.ifSetNotContains(JavaString.format("KURS_VERBIETEN_MIT_KURS(%d, %d): Kurs-ID2 nicht vorhanden!", kursID1, kursID2), setKurse, kursID2);
		DeveloperNotificationException.ifTrue(JavaString.format("KURS_VERBIETEN_MIT_KURS(%d, %d): Wurde mit sich selbst kombiniert!", kursID1, kursID2), kursID1 === kursID2);
	}

	private static fehlerBeiReferenzenRegeltyp8(daten: Array<number>, setKurse: HashSet<number>): void {
		KursblockungDynDaten.ueberpruefeDatenLaenge("KURS_ZUSAMMEN_MIT_KURS", daten, 2);
		const kursID1: number = daten[0].valueOf();
		const kursID2: number = daten[1].valueOf();
		DeveloperNotificationException.ifSetNotContains(JavaString.format("KURS_ZUSAMMEN_MIT_KURS(%d, %d): Kurs-ID1 nicht vorhanden!", kursID1, kursID2), setKurse, kursID1);
		DeveloperNotificationException.ifSetNotContains(JavaString.format("KURS_ZUSAMMEN_MIT_KURS(%d, %d): Kurs-ID2 nicht vorhanden!", kursID1, kursID2), setKurse, kursID2);
		DeveloperNotificationException.ifTrue(JavaString.format("KURS_ZUSAMMEN_MIT_KURS(%d, %d): Wurde mit sich selbst kombiniert!", kursID1, kursID2), kursID1 === kursID2);
	}

	private static fehlerBeiReferenzenRegeltyp9(daten: Array<number>, setKurse: HashSet<number>): void {
		KursblockungDynDaten.ueberpruefeDatenLaenge("KURS_MIT_DUMMY_SUS_AUFFUELLEN", daten, 2);
		const kursID: number = daten[0].valueOf();
		const dummySuS: number = daten[1];
		DeveloperNotificationException.ifSetNotContains(JavaString.format("KURS_MIT_DUMMY_SUS_AUFFUELLEN(%d, %d): Kurs-ID nicht vorhanden!", kursID, dummySuS), setKurse, kursID);
		DeveloperNotificationException.ifSmaller(JavaString.format("KURS_MIT_DUMMY_SUS_AUFFUELLEN(%d, %d): Der Wert ist zu klein!", kursID, dummySuS), dummySuS, GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN_MIN);
		DeveloperNotificationException.ifGreater(JavaString.format("KURS_MIT_DUMMY_SUS_AUFFUELLEN(%d, %d): Der Wert ist zu groß!", kursID, dummySuS), dummySuS, GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN_MAX);
	}

	private static fehlerBeiReferenzenRegeltyp10(daten: Array<number>): void {
		KursblockungDynDaten.ueberpruefeDatenLaenge("LEHRKRAEFTE_BEACHTEN", daten, 0);
	}

	private static fehlerBeiReferenzenRegeltyp11(daten: Array<number>, setSchueler: HashSet<number>, setFaecher: HashSet<number>): void {
		KursblockungDynDaten.ueberpruefeDatenLaenge("SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH", daten, 3);
		const schuelerID1: number = daten[0].valueOf();
		const schuelerID2: number = daten[1].valueOf();
		const fachID: number = daten[2].valueOf();
		DeveloperNotificationException.ifSetNotContains(JavaString.format("SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH(%d, %d, %d): Schüler-ID1 nicht vorhanden!", schuelerID1, schuelerID2, fachID), setSchueler, schuelerID1);
		DeveloperNotificationException.ifSetNotContains(JavaString.format("SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH(%d, %d, %d): Schüler-ID2 nicht vorhanden!", schuelerID1, schuelerID2, fachID), setSchueler, schuelerID2);
		DeveloperNotificationException.ifSetNotContains(JavaString.format("SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH(%d, %d, %d): Fach-ID nicht vorhanden!", schuelerID1, schuelerID2, fachID), setFaecher, fachID);
		DeveloperNotificationException.ifTrue(JavaString.format("SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH(%d, %d, %d): Wurde mit sich selbst kombiniert!", schuelerID1, schuelerID2, fachID), schuelerID1 === schuelerID2);
	}

	private static fehlerBeiReferenzenRegeltyp12(daten: Array<number>, setSchueler: HashSet<number>, setFaecher: HashSet<number>): void {
		KursblockungDynDaten.ueberpruefeDatenLaenge("SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH", daten, 3);
		const schuelerID1: number = daten[0].valueOf();
		const schuelerID2: number = daten[1].valueOf();
		const fachID: number = daten[2].valueOf();
		DeveloperNotificationException.ifSetNotContains(JavaString.format("SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH(%d, %d, %d): Schüler-ID1 nicht vorhanden!", schuelerID1, schuelerID2, fachID), setSchueler, schuelerID1);
		DeveloperNotificationException.ifSetNotContains(JavaString.format("SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH(%d, %d, %d): Schüler-ID2 nicht vorhanden!", schuelerID1, schuelerID2, fachID), setSchueler, schuelerID2);
		DeveloperNotificationException.ifSetNotContains(JavaString.format("SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH(%d, %d, %d): Fach-ID nicht vorhanden!", schuelerID1, schuelerID2, fachID), setFaecher, fachID);
		DeveloperNotificationException.ifTrue(JavaString.format("SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH(%d, %d, %d): Wurde mit sich selbst kombiniert!", schuelerID1, schuelerID2, fachID), schuelerID1 === schuelerID2);
	}

	private static fehlerBeiReferenzenRegeltyp13(daten: Array<number>, setSchueler: HashSet<number>): void {
		KursblockungDynDaten.ueberpruefeDatenLaenge("SCHUELER_ZUSAMMEN_MIT_SCHUELER", daten, 2);
		const schuelerID1: number = daten[0].valueOf();
		const schuelerID2: number = daten[1].valueOf();
		DeveloperNotificationException.ifSetNotContains(JavaString.format("SCHUELER_ZUSAMMEN_MIT_SCHUELER(%d, %d): Schüler-ID1 nicht vorhanden!", schuelerID1, schuelerID2), setSchueler, schuelerID1);
		DeveloperNotificationException.ifSetNotContains(JavaString.format("SCHUELER_ZUSAMMEN_MIT_SCHUELER(%d, %d): Schüler-ID2 nicht vorhanden!", schuelerID1, schuelerID2), setSchueler, schuelerID2);
		DeveloperNotificationException.ifTrue(JavaString.format("SCHUELER_ZUSAMMEN_MIT_SCHUELER(%d, %d): Wurde mit sich selbst kombiniert!", schuelerID1, schuelerID2), schuelerID1 === schuelerID2);
	}

	private static fehlerBeiReferenzenRegeltyp14(daten: Array<number>, setSchueler: HashSet<number>): void {
		KursblockungDynDaten.ueberpruefeDatenLaenge("SCHUELER_VERBIETEN_MIT_SCHUELER", daten, 2);
		const schuelerID1: number = daten[0].valueOf();
		const schuelerID2: number = daten[1].valueOf();
		DeveloperNotificationException.ifSetNotContains(JavaString.format("SCHUELER_VERBIETEN_MIT_SCHUELER(%d, %d): Schüler-ID1 nicht vorhanden!", schuelerID1, schuelerID2), setSchueler, schuelerID1);
		DeveloperNotificationException.ifSetNotContains(JavaString.format("SCHUELER_VERBIETEN_MIT_SCHUELER(%d, %d): Schüler-ID2 nicht vorhanden!", schuelerID1, schuelerID2), setSchueler, schuelerID2);
		DeveloperNotificationException.ifTrue(JavaString.format("SCHUELER_VERBIETEN_MIT_SCHUELER(%d, %d): Wurde mit sich selbst kombiniert!", schuelerID1, schuelerID2), schuelerID1 === schuelerID2);
	}

	private static fehlerBeiReferenzenRegeltyp15(daten: Array<number>, setKurse: HashSet<number>): void {
		KursblockungDynDaten.ueberpruefeDatenLaenge("KURS_MAXIMALE_SCHUELERANZAHL", daten, 2);
		const kursID: number = daten[0].valueOf();
		const schuelerAnzahl: number = daten[1];
		DeveloperNotificationException.ifSetNotContains(JavaString.format("KURS_MAXIMALE_SCHUELERANZAHL(%d, %d): Kurs-ID nicht vorhanden!", kursID, schuelerAnzahl), setKurse, kursID);
		DeveloperNotificationException.ifSmaller(JavaString.format("KURS_MAXIMALE_SCHUELERANZAHL(%d, %d): Schüleranzahl ist zu klein!", kursID, schuelerAnzahl), schuelerAnzahl, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL_MIN);
		DeveloperNotificationException.ifGreater(JavaString.format("KURS_MAXIMALE_SCHUELERANZAHL(%d, %d): Schüleranzahl ist zu groß!", kursID, schuelerAnzahl), schuelerAnzahl, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL_MAX);
	}

	private static fehlerBeiReferenzenRegeltyp16(daten: Array<number>, setSchueler: HashSet<number>): void {
		KursblockungDynDaten.ueberpruefeDatenLaenge("SCHUELER_IGNORIEREN", daten, 1);
		const schuelerID: number = daten[0].valueOf();
		DeveloperNotificationException.ifSetNotContains(JavaString.format("SCHUELER_IGNORIEREN(%d): Schüler-ID nicht vorhanden!", schuelerID), setSchueler, schuelerID);
	}

	private static fehlerBeiReferenzenRegeltyp17(daten: Array<number>, setKurse: HashSet<number>): void {
		KursblockungDynDaten.ueberpruefeDatenLaenge("KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN", daten, 1);
		const kursID: number = daten[0].valueOf();
		DeveloperNotificationException.ifSetNotContains(JavaString.format("KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN(%d): Kurs-ID nicht vorhanden!", kursID), setKurse, kursID);
	}

	private static fehlerBeiReferenzenRegeltyp18(daten: Array<number>, setFaecher: HashSet<number>, setKursarten: HashSet<number>): void {
		KursblockungDynDaten.ueberpruefeDatenLaenge("FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE", daten, 3);
		const fachID: number = daten[0].valueOf();
		const kursartID: number = daten[1];
		const maximum: number = daten[2];
		DeveloperNotificationException.ifSetNotContains(JavaString.format("FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE(%d, %d, %d): Fach-ID nicht vorhanden!", fachID, kursartID, maximum), setFaecher, fachID);
		DeveloperNotificationException.ifSetNotContains(JavaString.format("FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE(%d, %d, %d): Kursart nicht vorhanden!", fachID, kursartID, maximum), setKursarten, kursartID);
		DeveloperNotificationException.ifSmaller(JavaString.format("FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE(%d, %d, %d): Anzahl ist zu klein!", fachID, kursartID, maximum), maximum, GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE_MIN);
		DeveloperNotificationException.ifGreater(JavaString.format("FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE(%d, %d, %d): Anzahl ist zu groß!", fachID, kursartID, maximum), maximum, GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE_MAX);
	}

	private fehlerBeiRegelGruppierung(pRegeln: List<GostBlockungRegel>): void {
		const regelDatabaseIDs: HashSet<number> | null = new HashSet<number>();
		for (const iRegel of pRegeln) {
			DeveloperNotificationException.ifInvalidID("iRegel.id", iRegel.id);
			DeveloperNotificationException.ifSetAddsDuplicate("regelDatabaseIDs", regelDatabaseIDs, iRegel.id);
			const regelTyp: GostKursblockungRegelTyp = GostKursblockungRegelTyp.fromTyp(iRegel.typ);
			MapUtils.getOrCreateArrayList(this.regelMap, regelTyp).add(iRegel);
		}
	}

	private fehlerBeiSchuelerErstellung(input: GostBlockungsdatenManager): void {
		const setSchueler: HashSet<number> = new HashSet<number>();
		for (const gSchueler of input.daten().schueler) {
			setSchueler.add(gSchueler.id);
		}
		for (const fachwahl of input.daten().fachwahlen) {
			DeveloperNotificationException.ifSetNotContains("setSchueler", setSchueler, fachwahl.schuelerID);
		}
		const nSchueler: number = setSchueler.size();
		const nSchienen: number = input.schieneGetAnzahl();
		const nKurse: number = input.kursGetAnzahl();
		this.schuelerMenge = Array(nSchueler).fill(null);
		let i: number = 0;
		for (const sID of setSchueler) {
			const schueler: KursblockungDynSchueler = new KursblockungDynSchueler(this.log, this.rnd, sID, this.statistik, nSchienen, nKurse, i);
			this.schuelerMenge[i] = schueler;
			this.schuelerMap.put(sID, schueler);
			i++;
		}
	}

	private fehlerBeiFachartenErstellung(input: GostBlockungsdatenManager, nSchueler: number, nSchienen: number): void {
		let nFacharten: number = 0;
		const nKurse: number = input.daten().kurse.size();
		for (const gKurs of input.daten().kurse) {
			const fach: GostFach = input.faecherManager().getOrException(gKurs.fach_id);
			const kursart: GostKursart = GostKursart.fromID(gKurs.kursart);
			let dynFachart: KursblockungDynFachart | null = this.fachartMap2D.getOrNull(fach.id, kursart.id);
			if (dynFachart === null) {
				dynFachart = new KursblockungDynFachart(this.rnd, nFacharten, fach, kursart, this.statistik, nSchueler, nSchienen);
				this.fachartMap2D.put(fach.id, kursart.id, dynFachart);
				nFacharten++;
			}
			dynFachart.aktionMaxKurseErhoehen();
		}
		for (const iFachwahl of input.daten().fachwahlen) {
			const fach: GostFach = input.faecherManager().getOrException(iFachwahl.fachID);
			const kursart: GostKursart = GostKursart.fromID(iFachwahl.kursartID);
			let dynFachart: KursblockungDynFachart | null = this.fachartMap2D.getOrNull(fach.id, kursart.id);
			if (dynFachart === null) {
				dynFachart = new KursblockungDynFachart(this.rnd, nFacharten, fach, kursart, this.statistik, nSchueler, nSchienen);
				this.fachartMap2D.put(fach.id, kursart.id, dynFachart);
				nFacharten++;
			}
		}
		DeveloperNotificationException.ifSmaller("nFacharten", nFacharten, 1);
		this.fachartMenge = Array(nFacharten).fill(null);
		for (const fachart of this.fachartMap2D.getNonNullValuesAsList()) {
			this.fachartMenge[fachart.gibNr()] = fachart;
		}
		let kursSumme: number = 0;
		for (const fa of this.fachartMenge) {
			kursSumme += fa.gibKurseMax();
		}
		DeveloperNotificationException.ifTrue("Die Summe aller auf die Facharten verteilten Kurse ist ungleich der Gesamtkursanzahl.", kursSumme !== nKurse);
	}

	private fehlerBeiSchuelerFachwahlenErstellung(input: GostBlockungsdatenManager, susArr: Array<KursblockungDynSchueler>): void {
		const mapSchuelerFA: HashMap<number, List<KursblockungDynFachart>> = new HashMap<number, List<KursblockungDynFachart>>();
		for (const iFachwahl of input.daten().fachwahlen) {
			const dynFachart: KursblockungDynFachart = this.gibFachart(iFachwahl.fachID, iFachwahl.kursartID);
			MapUtils.getOrCreateArrayList(mapSchuelerFA, iFachwahl.schuelerID).add(dynFachart);
		}
		for (const schueler of susArr) {
			const listFA: List<KursblockungDynFachart> = MapUtils.getOrCreateArrayList(mapSchuelerFA, schueler.gibDatenbankID());
			const arrFA: Array<KursblockungDynFachart> = listFA.toArray(Array(0).fill(null));
			schueler.aktionSetzeFachartenUndIDs(arrFA);
		}
	}

	private fehlerBeiStatistikErstellung(fachartArr: Array<KursblockungDynFachart>, susArr: Array<KursblockungDynSchueler>, input: GostBlockungsdatenManager): void {
		const nFacharten: number = fachartArr.length;
		const wahlenMatrixFachart: Array<Array<number>> = [...Array(nFacharten)].map(e => Array(nFacharten).fill(0));
		const bewertungMatrixFachart: Array<Array<number>> = [...Array(nFacharten)].map(e => Array(nFacharten).fill(0));
		for (const s of susArr) {
			for (const pair of new PairIterable(s.gibFacharten(), PairIteratorModus.LOWER_ONLY)) {
				const nr1: number = pair.a.gibNr();
				const nr2: number = pair.b.gibNr();
				wahlenMatrixFachart[nr1][nr2]++;
				wahlenMatrixFachart[nr2][nr1]++;
			}
		}
		const cMALUS_KOLLISION: number = 10000;
		const cMALUS_DIAGONALE: number = 1000;
		for (let i1: number = 0; i1 < nFacharten; i1++) {
			for (let i2: number = 0; i2 < nFacharten; i2++) {
				const kurseVonFachart1: number = fachartArr[i1].gibKurseMax();
				const kurseVonFachart2: number = fachartArr[i2].gibKurseMax();
				const nr1: number = fachartArr[i1].gibNr();
				const nr2: number = fachartArr[i2].gibNr();
				bewertungMatrixFachart[nr1][nr2] = (nr1 === nr2) ? cMALUS_DIAGONALE : 0;
				if ((wahlenMatrixFachart[nr1][nr2] === 0) || (kurseVonFachart1 === 0) || (kurseVonFachart2 === 0)) {
					continue;
				}
				const nenner: number = kurseVonFachart1 * kurseVonFachart2;
				bewertungMatrixFachart[nr1][nr2] += Math.trunc(cMALUS_KOLLISION / nenner);
			}
		}
		this.statistik.aktionInitialisiere(bewertungMatrixFachart, susArr.length, fachartArr.length, input.kursGetAnzahl());
	}

	private fehlerBeiSchienenErzeugung(nSchienen: number): void {
		this.schienenMenge = Array(nSchienen).fill(null);
		for (let nr: number = 0; nr < nSchienen; nr++) {
			this.schienenMenge[nr] = new KursblockungDynSchiene(this.log, nr, this.statistik);
		}
	}

	private fehlerBeiKursErstellung(input: GostBlockungsdatenManager, nSchueler: number): void {
		const nKurse: number = input.kursGetAnzahl();
		const nSchienen: number = input.schieneGetAnzahl();
		this.kursMenge = Array(nKurse).fill(null);
		let i: number = 0;
		for (const kurs of input.daten().kurse) {
			const dynKurs: KursblockungDynKurs = this.schritt08FehlerBeiKursErstellungErzeuge(input, kurs, nSchienen, i, nSchueler);
			this.kursMenge[i] = dynKurs;
			DeveloperNotificationException.ifMapPutOverwrites(this.kursMap, kurs.id, dynKurs);
			i++;
		}
	}

	private schritt08FehlerBeiKursErstellungErzeuge(input: GostBlockungsdatenManager, kurs: GostBlockungKurs, nSchienen: number, kursNr: number, nSchueler: number): KursblockungDynKurs {
		DeveloperNotificationException.ifSmaller(JavaString.format("Der Kurs mit ID=%d und NR=%d belegt zu wenig (%d) Schienen!", kurs.id, kursNr, kurs.anzahlSchienen), kurs.anzahlSchienen, 1);
		DeveloperNotificationException.ifGreater(JavaString.format("Der Kurs mit ID=%d und NR=%d belegt zu viele (%d) Schienen!", kurs.id, kursNr, kurs.anzahlSchienen), kurs.anzahlSchienen, this.schienenMenge.length);
		const schieneLage: List<KursblockungDynSchiene> = new ArrayList<KursblockungDynSchiene>();
		const schieneFrei: List<KursblockungDynSchiene> = ListUtils.getCopyAsArrayListPermuted(this.schienenMenge, this.rnd);
		this.schritt08FehlerBeiKursErstellungErzeugeWendeRegel1und6An(schieneFrei, kurs, nSchienen);
		this.schritt08FehlerBeiKursErstellungErzeugeWendeRegel3und2An(schieneLage, schieneFrei, kurs);
		const anzahlFixierterSchienen: number = schieneLage.size();
		DeveloperNotificationException.ifGreater(JavaString.format("Der Kurs mit ID=%d und NR=%d hat %d Schienen fixert, aber selbst belegt er %d Schienen!", kurs.id, kursNr, anzahlFixierterSchienen, kurs.anzahlSchienen), anzahlFixierterSchienen, kurs.anzahlSchienen);
		while (schieneLage.size() < kurs.anzahlSchienen) {
			UserNotificationException.ifTrue(input.toStringKurs(kurs.id) + " hat zu viele Schienen gesperrt, so dass seine Schienenanzahl nicht erfüllt werden kann!", schieneFrei.isEmpty());
			schieneLage.add(schieneFrei.removeLast());
		}
		const schienenLageArray: Array<KursblockungDynSchiene> = schieneLage.toArray(Array(0).fill(null));
		const schienenFreiArray: Array<KursblockungDynSchiene> = schieneFrei.toArray(Array(0).fill(null));
		const dynFachart: KursblockungDynFachart = this.gibFachart(kurs.fach_id, kurs.kursart);
		return new KursblockungDynKurs(this.rnd, schienenLageArray, anzahlFixierterSchienen, schienenFreiArray, kurs.id, dynFachart, this.log, kursNr, nSchueler);
	}

	private schritt08FehlerBeiKursErstellungErzeugeWendeRegel1und6An(schieneFrei: List<KursblockungDynSchiene>, kurs: GostBlockungKurs, nSchienen: number): void {
		for (const regel1 of MapUtils.getOrCreateArrayList(this.regelMap, GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS)) {
			if (kurs.kursart === regel1.parameter.get(0)) {
				const von: number = regel1.parameter.get(1);
				const bis: number = regel1.parameter.get(2);
				for (let schiene: number = von; schiene <= bis; schiene++) {
					schieneFrei.remove(this.schienenMenge[schiene - 1]);
				}
			}
		}
		for (const regel6 of MapUtils.getOrCreateArrayList(this.regelMap, GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS)) {
			const kursartStimmt: boolean = kurs.kursart === regel6.parameter.get(0);
			const von: number = regel6.parameter.get(1);
			const bis: number = regel6.parameter.get(2);
			for (let schiene: number = 1; schiene <= nSchienen; schiene++) {
				const innerhalb: boolean = (von <= schiene) && (schiene <= bis);
				if (innerhalb !== kursartStimmt) {
					schieneFrei.remove(this.schienenMenge[schiene - 1]);
				}
			}
		}
	}

	private schritt08FehlerBeiKursErstellungErzeugeWendeRegel3und2An(schieneLage: List<KursblockungDynSchiene>, schieneFrei: List<KursblockungDynSchiene>, kurs: GostBlockungKurs): void {
		for (const regel3 of MapUtils.getOrCreateArrayList(this.regelMap, GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE)) {
			if (kurs.id === regel3.parameter.get(0)) {
				const schiene: number = regel3.parameter.get(1);
				schieneFrei.remove(this.schienenMenge[schiene - 1]);
			}
		}
		for (const regel2 of MapUtils.getOrCreateArrayList(this.regelMap, GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE)) {
			if (kurs.id === regel2.parameter.get(0)) {
				const schiene: number = regel2.parameter.get(1);
				const dynSchiene: KursblockungDynSchiene = this.schienenMenge[schiene - 1];
				if (schieneLage.contains(dynSchiene)) {
					continue;
				}
				UserNotificationException.ifTrue(JavaString.format("KURS_FIXIERE_IN_SCHIENE: Kurs (%d) soll in Schiene (%d) fixiert werden, aber die Schiene wurde bereits gesperrt!", kurs.id, schiene), !schieneFrei.contains(dynSchiene));
				schieneFrei.remove(dynSchiene);
				schieneLage.add(dynSchiene);
			}
		}
	}

	private fehlerBeiKursFreiErstellung(): void {
		let nKursFrei: number = 0;
		for (const kurs of this.kursMenge) {
			if (kurs.gibHatFreiheitsgrade()) {
				nKursFrei++;
			}
		}
		this.kursMengeFrei = Array(nKursFrei).fill(null);
		let j: number = 0;
		for (const kurs of this.kursMenge) {
			if (kurs.gibHatFreiheitsgrade()) {
				this.kursMengeFrei[j] = kurs;
				j++;
			}
		}
	}

	private fehlerBeiFachartKursArrayErstellung(): void {
		const nFacharten: number = this.fachartMenge.length;
		const mapFachartList: HashMap<number, List<KursblockungDynKurs>> = new HashMap<number, List<KursblockungDynKurs>>();
		for (let i: number = 0; i < nFacharten; i++) {
			mapFachartList.put(i, new ArrayList<KursblockungDynKurs>());
		}
		for (const kurs of this.kursMenge) {
			const fachartNr: number = kurs.gibFachart().gibNr();
			DeveloperNotificationException.ifMapGetIsNull(mapFachartList, fachartNr).add(kurs);
		}
		for (let fachartNr: number = 0; fachartNr < nFacharten; fachartNr++) {
			const list: List<KursblockungDynKurs> | null = DeveloperNotificationException.ifMapGetIsNull(mapFachartList, fachartNr);
			const tmpKursArr: Array<KursblockungDynKurs> = list.toArray(Array(0).fill(null));
			this.fachartMenge[fachartNr].aktionSetKurse(tmpKursArr);
		}
	}

	private fehlerBeiRegel4oder5(): void {
		const mapSchuelerZuFixierungen: HashMap<number, List<number>> = new HashMap<number, List<number>>();
		for (const regel4 of MapUtils.getOrCreateArrayList(this.regelMap, GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS)) {
			const schuelerID: number = regel4.parameter.get(0).valueOf();
			const kursID: number = regel4.parameter.get(1).valueOf();
			MapUtils.getOrCreateArrayList(mapSchuelerZuFixierungen, schuelerID).add(kursID);
			const schueler: KursblockungDynSchueler = this.gibSchueler(schuelerID);
			const fixierterKurs: KursblockungDynKurs = this.gibKurs(kursID);
			for (const kurs of fixierterKurs.gibFachart().gibKurse()) {
				if (kurs as unknown === fixierterKurs as unknown) {
					kurs.setzeSchuelerFixierung(schueler.internalSchuelerID);
				} else {
					schueler.aktionSetzeKursSperrung(kurs.gibInternalID());
				}
			}
		}
		for (const idSchueler of mapSchuelerZuFixierungen.keySet()) {
			const listKursIDs: List<number> = MapUtils.getOrCreateArrayList(mapSchuelerZuFixierungen, idSchueler);
			for (let index2: number = 1; index2 < listKursIDs.size(); index2++) {
				for (let index1: number = 0; index1 < index2; index1++) {
					const kursID1: number = listKursIDs.get(index1).valueOf();
					const kursID2: number = listKursIDs.get(index2).valueOf();
					const kurs1: KursblockungDynKurs = this.gibKurs(kursID1);
					const kurs2: KursblockungDynKurs = this.gibKurs(kursID2);
					this.statistik.regelHinzufuegenKursVerbieteMitKurs(kurs1, kurs2);
				}
			}
		}
		for (const regel5 of MapUtils.getOrCreateArrayList(this.regelMap, GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS)) {
			const schuelerID: number = regel5.parameter.get(0).valueOf();
			const kursID: number = regel5.parameter.get(1).valueOf();
			const schueler: KursblockungDynSchueler = this.gibSchueler(schuelerID);
			const verbotenerKurs: KursblockungDynKurs = this.gibKurs(kursID);
			schueler.aktionSetzeKursSperrung(verbotenerKurs.gibInternalID());
		}
	}

	private fehlerBeiRegel7oder8(): void {
		for (const regel7 of MapUtils.getOrCreateArrayList(this.regelMap, GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS)) {
			const kursID1: number = regel7.parameter.get(0).valueOf();
			const kursID2: number = regel7.parameter.get(1).valueOf();
			const kurs1: KursblockungDynKurs = this.gibKurs(kursID1);
			const kurs2: KursblockungDynKurs = this.gibKurs(kursID2);
			this.statistik.regelHinzufuegenKursVerbieteMitKurs(kurs1, kurs2);
		}
		for (const regel8 of MapUtils.getOrCreateArrayList(this.regelMap, GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS)) {
			const kursID1: number = regel8.parameter.get(0).valueOf();
			const kursID2: number = regel8.parameter.get(1).valueOf();
			const kurs1: KursblockungDynKurs = this.gibKurs(kursID1);
			const kurs2: KursblockungDynKurs = this.gibKurs(kursID2);
			this.statistik.regelHinzufuegenKursZusammenMitKurs(kurs1, kurs2);
		}
	}

	private fehlerBeiRegel9(): void {
		for (const regel9 of MapUtils.getOrCreateArrayList(this.regelMap, GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN)) {
			const kursID: number = regel9.parameter.get(0).valueOf();
			const susAnzahl: number = regel9.parameter.get(1);
			const kurs: KursblockungDynKurs = this.gibKurs(kursID);
			for (let i: number = 0; i < susAnzahl; i++) {
				kurs.aktionSchuelerDummyHinzufuegen();
			}
		}
	}

	private fehlerBeiRegel10(pInput: GostBlockungsdatenManager): void {
		const regelnTyp10: List<GostBlockungRegel> | null = MapUtils.getOrCreateArrayList(this.regelMap, GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN);
		if (regelnTyp10.isEmpty()) {
			return;
		}
		const size: number = regelnTyp10.size();
		DeveloperNotificationException.ifGreater(JavaString.format("LEHRKRAEFTE_BEACHTEN: Diese Regeln darf es maximal ein mal geben, sie gibt es aber %d mal!", size), size, 1);
		const mapLehrkraftNachKurse: HashMap<number, List<KursblockungDynKurs>> = new HashMap<number, List<KursblockungDynKurs>>();
		for (const gKurs of pInput.daten().kurse) {
			for (const gLehr of gKurs.lehrer) {
				const dynKurs: KursblockungDynKurs = this.gibKurs(gKurs.id);
				MapUtils.getOrCreateArrayList(mapLehrkraftNachKurse, gLehr.id).add(dynKurs);
			}
		}
		for (const kurseDerLehrkraft of mapLehrkraftNachKurse.values()) {
			for (const pair of new PairIterable(kurseDerLehrkraft, PairIteratorModus.LOWER_ONLY)) {
				this.statistik.regelHinzufuegenKursVerbieteMitKurs(pair.a, pair.b);
			}
		}
	}

	private fehlerBeiRegel11bis14(input: GostBlockungsdatenManager): void {
		const setSSF: HashSet<LongArrayKey> = new HashSet<LongArrayKey>();
		const fehlermeldungDopplung: string = "Dopplung bei Schüler-Schüler-Fach Zusammen/Verbieten!";
		for (const regel11 of MapUtils.getOrCreateArrayList(this.regelMap, GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH)) {
			const idS1: number = regel11.parameter.get(0).valueOf();
			const idS2: number = regel11.parameter.get(1).valueOf();
			const idF: number = regel11.parameter.get(2).valueOf();
			const key12F: LongArrayKey = new LongArrayKey(idS1, idS2, idF);
			const key21F: LongArrayKey = new LongArrayKey(idS2, idS1, idF);
			DeveloperNotificationException.ifTrue(fehlermeldungDopplung, !setSSF.add(key12F));
			DeveloperNotificationException.ifTrue(fehlermeldungDopplung, !setSSF.add(key21F));
			const sch1: KursblockungDynSchueler = this.gibSchueler(idS1);
			const sch2: KursblockungDynSchueler = this.gibSchueler(idS2);
			sch1.setzeZusammenMitSchuelerInFach(sch2, idF);
		}
		for (const regel12 of MapUtils.getOrCreateArrayList(this.regelMap, GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH)) {
			const idS1: number = regel12.parameter.get(0).valueOf();
			const idS2: number = regel12.parameter.get(1).valueOf();
			const idF: number = regel12.parameter.get(2).valueOf();
			const key12F: LongArrayKey = new LongArrayKey(idS1, idS2, idF);
			const key21F: LongArrayKey = new LongArrayKey(idS2, idS1, idF);
			DeveloperNotificationException.ifTrue(fehlermeldungDopplung, !setSSF.add(key12F));
			DeveloperNotificationException.ifTrue(fehlermeldungDopplung, !setSSF.add(key21F));
			const sch1: KursblockungDynSchueler = this.gibSchueler(idS1);
			const sch2: KursblockungDynSchueler = this.gibSchueler(idS2);
			sch1.setzeVerbietenMitSchuelerInFach(sch2, idF);
		}
		for (const regel13 of MapUtils.getOrCreateArrayList(this.regelMap, GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER)) {
			const idS1: number = regel13.parameter.get(0).valueOf();
			const idS2: number = regel13.parameter.get(1).valueOf();
			for (const fach of input.schuelerGetFachListeGemeinsamerFacharten(idS1, idS2)) {
				const key12F: LongArrayKey = new LongArrayKey(idS1, idS2, fach.id);
				const key21F: LongArrayKey = new LongArrayKey(idS2, idS1, fach.id);
				DeveloperNotificationException.ifTrue(fehlermeldungDopplung, !setSSF.add(key12F));
				DeveloperNotificationException.ifTrue(fehlermeldungDopplung, !setSSF.add(key21F));
				const sch1: KursblockungDynSchueler = this.gibSchueler(idS1);
				const sch2: KursblockungDynSchueler = this.gibSchueler(idS2);
				sch1.setzeZusammenMitSchueler(sch2);
			}
		}
		for (const r14 of MapUtils.getOrCreateArrayList(this.regelMap, GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER)) {
			const idS1: number = r14.parameter.get(0).valueOf();
			const idS2: number = r14.parameter.get(1).valueOf();
			for (const fach of input.schuelerGetFachListeGemeinsamerFacharten(idS1, idS2)) {
				const key12F: LongArrayKey = new LongArrayKey(idS1, idS2, fach.id);
				const key21F: LongArrayKey = new LongArrayKey(idS2, idS1, fach.id);
				DeveloperNotificationException.ifTrue(fehlermeldungDopplung, !setSSF.add(key12F));
				DeveloperNotificationException.ifTrue(fehlermeldungDopplung, !setSSF.add(key21F));
			}
			const sch1: KursblockungDynSchueler = this.gibSchueler(idS1);
			const sch2: KursblockungDynSchueler = this.gibSchueler(idS2);
			sch1.setzeVerbietenMitSchueler(sch2);
		}
	}

	private fehlerBeiRegel15(): void {
		for (const r15 of MapUtils.getOrCreateArrayList(this.regelMap, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL)) {
			const idKurs: number = r15.parameter.get(0).valueOf();
			const maxSuS: number = r15.parameter.get(1);
			const kurs: KursblockungDynKurs = this.gibKurs(idKurs);
			kurs.setzeMaxSuS(maxSuS);
		}
	}

	private fehlerBeiRegel16(): void {
		for (const r16 of MapUtils.getOrCreateArrayList(this.regelMap, GostKursblockungRegelTyp.SCHUELER_IGNORIEREN)) {
			const idSchueler: number = r16.parameter.get(0).valueOf();
			const schueler: KursblockungDynSchueler = this.gibSchueler(idSchueler);
			schueler.setzeSperreBeiKursverteilung();
		}
	}

	private fehlerBeiRegel18(): void {
		for (const r18 of MapUtils.getOrCreateArrayList(this.regelMap, GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE)) {
			const idFach: number = r18.parameter.get(0).valueOf();
			const idKursart: number = r18.parameter.get(1);
			const maximalProSchiene: number = r18.parameter.get(2);
			const fachart: KursblockungDynFachart = this.gibFachart(idFach, idKursart);
			fachart.setzeMaxAnzahlProSchiene(maximalProSchiene);
		}
	}

	private gibFachart(fachID: number, kursart: number): KursblockungDynFachart {
		return this.fachartMap2D.getOrException(fachID, kursart);
	}

	private gibSchueler(schuelerID: number): KursblockungDynSchueler {
		return DeveloperNotificationException.ifMapGetIsNull(this.schuelerMap, schuelerID);
	}

	private gibKurs(kursID: number): KursblockungDynKurs {
		return DeveloperNotificationException.ifMapGetIsNull(this.kursMap, kursID);
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
	gibErzeugtesKursblockungOutput(pDataManager: GostBlockungsdatenManager, pErgebnisID: number): GostBlockungsergebnisManager {
		const out: GostBlockungsergebnisManager = new GostBlockungsergebnisManager(pDataManager, pErgebnisID);
		const kursSchienenZuordnungen: JavaSet<GostBlockungsergebnisKursSchienenZuordnung> = new HashSet<GostBlockungsergebnisKursSchienenZuordnung>();
		for (const dynKurs of this.kursMenge) {
			for (const schienenNr of dynKurs.gibSchienenLage()) {
				const idKurs: number = dynKurs.gibDatenbankID();
				const idSchiene: number = out.getOfSchieneID(schienenNr + 1);
				kursSchienenZuordnungen.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(idKurs, idSchiene));
			}
		}
		const uKursSchienen: GostBlockungsergebnisKursSchienenZuordnungUpdate = out.kursSchienenUpdateFuegeKursSchienenPaareHinzu(kursSchienenZuordnungen);
		out.kursSchienenUpdateExecute(uKursSchienen);
		const kursSchuelerZuordnungen: JavaSet<GostBlockungsergebnisKursSchuelerZuordnung> = new HashSet<GostBlockungsergebnisKursSchuelerZuordnung>();
		for (const dynSchueler of this.schuelerMenge) {
			for (const kurs of dynSchueler.gibKurswahlen()) {
				if (kurs !== null) {
					const idKurs: number = kurs.gibDatenbankID();
					const idSchueler: number = dynSchueler.gibDatenbankID();
					kursSchuelerZuordnungen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(idKurs, idSchueler));
				}
			}
		}
		for (const gRegel of pDataManager.regelGetListe()) {
			if (gRegel.typ === GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ) {
				const idSchueler: number = gRegel.parameter.get(0).valueOf();
				const idKurs: number = gRegel.parameter.get(1).valueOf();
				kursSchuelerZuordnungen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(idKurs, idSchueler));
			}
		}
		const uKursSchueler: GostBlockungsergebnisKursSchuelerZuordnungUpdate = out.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(kursSchuelerZuordnungen);
		out.kursSchuelerUpdateExecute(uKursSchueler);
		return out;
	}

	/**
	 * Liefert das Logger-Objekt für Benutzerhinweise, Warnungen und Fehler.
	 *
	 * @return Das Logger-Objekt für Benutzerhinweise, Warnungen und Fehler.
	 */
	gibLogger(): Logger {
		return this.log;
	}

	/**
	 * Liefert das {@link Random}-Objekt.
	 *
	 * @return das {@link Random}-Objekt.
	 */
	gibRandom(): Random {
		return this.rnd;
	}

	/**
	 * Liefert das Statistik-Objekt (für Anfragen zu Nichtwahlen, Kursdifferenzen, etc.).
	 *
	 * @return Das Statistik-Objekt (für Anfragen zu Nichtwahlen, Kursdifferenzen, etc.).
	 */
	public gibStatistik(): KursblockungDynStatistik {
		return this.statistik;
	}

	/**
	 * Liefert die maximale Blockungszeit in Millisekunden. Entweder handelt es sich um einen Standardwert oder der Wert
	 * wurde im Konstruktor als Regel übergeben.
	 *
	 * @return Liefert die maximale Blockungszeit in Millisekunden.
	 */
	gibBlockungszeitMillis(): number {
		return this.maxTimeMillis;
	}

	/**
	 * Liefert die maximal erlaubte Anzahl an Schienen. Entweder handelt es sich um einen Standardwert oder der Wert
	 * wurde im Konstruktor als Regel übergeben.
	 *
	 * @return Liefert die maximal erlaubte Anzahl an Schienen.
	 */
	public gibSchienenAnzahl(): number {
		return this.schienenMenge.length;
	}

	/**
	 * Liefert alle Kurse.
	 *
	 * @return Array aller Kurse.
	 */
	gibKurseAlle(): Array<KursblockungDynKurs> {
		return this.kursMenge;
	}

	/**
	 * Liefert alle Kurse deren Lage nicht komplett fixiert ist.
	 *
	 * @return Array aller Kurse, deren Schienenlage noch veränderbar ist.
	 */
	gibKurseDieFreiSind(): Array<KursblockungDynKurs> {
		return this.kursMengeFrei;
	}

	/**
	 * Liefert die Anzahl alle Kurse deren Lage nicht komplett fixiert ist.
	 *
	 * @return Anzahl aller Kurse, deren Schienenlage noch veränderbar ist.
	 */
	gibKurseDieFreiSindAnzahl(): number {
		return this.kursMengeFrei.length;
	}

	/**
	 * Liefert einen Long-Wert, der einer Bewertung der Fachwahlmatrix entspricht. Je kleiner der Wert, desto besser ist
	 * die Bewertung.
	 *
	 * @return Long-Wert, der einer Bewertung der Fachwahlmatrix entspricht.
	 */
	gibBewertungFachartPaar(): number {
		return this.statistik.gibBewertungFachartPaar();
	}

	/**
	 * Liefert ein Array aller Schülerinnen und Schüler.
	 * Falls der Parameter {@code pNurMultiKurse} TRUE ist, dann werden nur SuS mit mindestens einem Multikurs ausgewählt.
	 *
	 * @param  pNurMultiKurse Falls TRUE, dann werden nur SuS mit mindestens einem Multikurs ausgewählt.
	 *
	 * @return ein Array aller Schülerinnen und Schüler.
	 */
	gibSchuelerArray(pNurMultiKurse: boolean): Array<KursblockungDynSchueler> {
		if (pNurMultiKurse) {
			const list: LinkedCollection<KursblockungDynSchueler> = new LinkedCollection<KursblockungDynSchueler>();
			for (const schueler of this.schuelerMenge) {
				if (schueler.gibHatMultikurs()) {
					list.addLast(schueler);
				}
			}
			const temp: Array<KursblockungDynSchueler> = Array(list.size()).fill(null);
			for (let i: number = 0; i < temp.length; i++) {
				temp[i] = list.removeFirst();
			}
			return temp;
		}
		return this.schuelerMenge;
	}

	/**
	 * Liefert ein Array aller Schülerinnen und Schüler.
	 *
	 * @return Ein Array aller Schülerinnen und Schüler.
	 */
	gibSchuelerArrayAlle(): Array<KursblockungDynSchueler> {
		return this.schuelerMenge;
	}

	/**
	 * Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdifferenzen) des Zustandes S sich
	 * verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 *
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdifferenzen) des Zustandes S sich
	 *         verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 */
	gibBewertungJetztBesserAlsS(): number {
		return this.statistik.gibBewertungZustandS1NW2KD();
	}

	/**
	 * Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Nichtwahlen, Kursdifferenzen,
	 * Fachwahlmatrix) des Zustandes-K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 *
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Nichtwahlen, Kursdifferenzen, Fachwahlmatrix) des
	 *         Zustandes-K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 */
	gibCompareZustandK1NW2KD3FW(): number {
		return this.statistik.gibCompareZustandK1NW2KD3FW();
	}

	/**
	 * Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Nichtwahlen, Kursdifferenzen,
	 * Fachwahlmatrix) des Zustandes-G sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 *
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Nichtwahlen, Kursdifferenzen, Fachwahlmatrix) des
	 *         Zustandes-G sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 */
	gibCompareZustandG1NW2KD3FW(): number {
		return this.statistik.gibCompareZustandG1NW2KD3FW();
	}

	/**
	 * Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Fachwahlmatrix, Nichtwahlen,
	 * Kursdifferenzen) des Zustandes K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 *
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Fachwahlmatrix, Nichtwahlen, Kursdifferenzen) des
	 *         Zustandes K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 */
	gibBewertungK1FW2NW3KD(): number {
		return this.statistik.gibCompareZustandK1FW2NW3KD();
	}

	/**
	 * Liefert TRUE, falls dieses Objekt besser ist als das übergebene Objekt b.
	 *
	 * @param b  Das zu vergleichende Objekt.
	 *
	 * @return TRUE, falls dieses Objekt besser ist als das übergebene Objekt b.
	 */
	gibIstBesserAls1NW2KD3FW(b: KursblockungDynDaten): boolean {
		return this.statistik.gibIstBesserAls1NW2KD3FW(b.statistik);
	}

	/**
	 * Liefert true, falls der Kurs in der Schiene ist.
	 *
	 * @param idKursDB Die Datenbank-ID des Kurses.
	 * @param schieneDB Die Datenbank-ID der Schiene (1-indiziert!).
	 * @return true, falls der Kurs in der Schiene ist.
	 */
	public gibIstKursInSchiene(idKursDB: number, schieneDB: number): boolean {
		for (const k of this.kursMenge) {
			if ((k.gibDatenbankID() === idKursDB) && (k.gibIstInSchiene(schieneDB - 1))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Liefert true, falls der Schüler im Kurs ist.
	 *
	 * @param idSchuelerDB Die Datenbank-ID des Schülers.
	 * @param idKursDB Die Datenbank-ID des Kurses.
	 * @return true, falls der Kurs in der Schiene ist.
	 */
	public gibIstSchuelerInKurs(idSchuelerDB: number, idKursDB: number): boolean {
		for (const k of this.kursMenge) {
			if (k.gibDatenbankID() === idKursDB) {
				for (const s of this.schuelerMenge) {
					if ((s.gibDatenbankID() === idSchuelerDB) && (s.gibIstInKurs(k))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Liefert true, falls die übergebene Schiene nur LK-Kurse enthält (oder keine Kurse).
	 *
	 * @param schienenNr1indiziert  Die Schienen-Nummer
	 * @return true, falls die übergebene Schiene nur LK-Kurse enthält (oder keine Kurse).
	 */
	public gibHatSchieneNurLK(schienenNr1indiziert: number): boolean {
		return this.schienenMenge[schienenNr1indiziert - 1].gibHatNurLK();
	}

	/**
	 * Liefert true, falls die übergebene Schiene keine LK-Kurse enthält.
	 *
	 * @param schienenNr1indiziert  Die Schienen-Nummer
	 * @return true, falls die übergebene Schiene keine LK-Kurse enthält.
	 */
	public gibHatSchieneKeineLK(schienenNr1indiziert: number): boolean {
		return this.schienenMenge[schienenNr1indiziert - 1].gibHatKeineLK();
	}

	/**
	 * Liefert die Anzahl an SuS in dem Kurs (oder -1 falls der Kurs nicht existiert).
	 *
	 * @param idKursDB Die Datenbank-ID des Kurses.
	 * @return die Anzahl an SuS in dem Kurs (oder -1 falls der Kurs nicht existiert).
	 */
	public gibKursgroesseDesKurses(idKursDB: number): number {
		for (const k of this.kursMenge) {
			if (k.gibDatenbankID() === idKursDB) {
				return k.gibSchuelerAnzahl();
			}
		}
		return -1;
	}

	/**
	 * Liefert die Kursdifferenz der Fachart des übergebenen Kurses (oder -1 falls der Kurs nicht existiert)..
	 *
	 * @param idKursDB Die Datenbank-ID des Kurses.
	 * @return die Kursdifferenz der Fachart des übergebenen Kurses (oder -1 falls der Kurs nicht existiert)..
	 */
	public gibKursdifferenzDesKurses(idKursDB: number): number {
		for (const k of this.kursMenge) {
			if (k.gibDatenbankID() === idKursDB) {
				return k.gibFachart().gibKursdifferenz();
			}
		}
		return -1;
	}

	/**
	 * Speichert die Bewertung, die Kursverteilung und die Schülerverteilung im Zustand S.
	 */
	aktionZustandSpeichernS(): void {
		this.statistik.aktionBewertungSpeichernS();
		for (const kurs of this.kursMenge) {
			kurs.aktionZustandSpeichernS();
		}
		for (const schueler of this.schuelerMenge) {
			schueler.aktionZustandSpeichernS();
		}
	}

	/**
	 * Speichert die Bewertung, die Kursverteilung und die Schülerverteilung im Zustand K.
	 */
	aktionZustandSpeichernK(): void {
		this.statistik.aktionBewertungSpeichernK();
		for (const kurs of this.kursMenge) {
			kurs.aktionZustandSpeichernK();
		}
		for (const schueler of this.schuelerMenge) {
			schueler.aktionZustandSpeichernK();
		}
	}

	/**
	 * Speichert die Bewertung, die Kursverteilung und die Schülerverteilung im Zustand G.
	 */
	aktionZustandSpeichernG(): void {
		this.statistik.aktionBewertungSpeichernG();
		for (const kurs of this.kursMenge) {
			kurs.aktionZustandSpeichernG();
		}
		for (const schueler of this.schuelerMenge) {
			schueler.aktionZustandSpeichernG();
		}
	}

	/**
	 * Lädt den zuvor gespeicherten Zustand S (Kursverteilung und Schülerverteilung).
	 */
	aktionZustandLadenS(): void {
		for (const schueler of this.schuelerMenge) {
			schueler.aktionKurseAlleEntfernen();
		}
		for (const kurs of this.kursMenge) {
			kurs.aktionZustandLadenS();
		}
		for (const schueler of this.schuelerMenge) {
			schueler.aktionZustandLadenS();
		}
	}

	/**
	 * Lädt den zuvor gespeicherten Zustand K (Kursverteilung und Schülerverteilung).
	 */
	aktionZustandLadenK(): void {
		for (const schueler of this.schuelerMenge) {
			schueler.aktionKurseAlleEntfernen();
		}
		for (const kurs of this.kursMenge) {
			kurs.aktionZustandLadenK();
		}
		for (const schueler of this.schuelerMenge) {
			schueler.aktionZustandLadenK();
		}
	}

	/**
	 * Lädt den zuvor gespeicherten Zustand K einer anderen {@link KursblockungDynDaten}-Objekts (Kursverteilung und Schülerverteilung).
	 *
	 * @param b  Das andere {@link KursblockungDynDaten}-Objekt.
	 */
	aktionZustandLadenVon(b: KursblockungDynDaten): void {
		if (this as unknown === b as unknown) {
			this.log.logLn(LogLevel.WARNING, "KursblockungDynDaten.aktionZustandLadenVon(...) versucht sich selbst zu laden.");
			return;
		}
		for (const schueler of this.schuelerMenge) {
			schueler.aktionKurseAlleEntfernen();
		}
		for (let i: number = 0; i < this.kursMenge.length; i++) {
			this.kursMenge[i].aktionZustandLadenVon(b.kursMenge[i], this.schienenMenge);
		}
		for (let i: number = 0; i < this.schuelerMenge.length; i++) {
			this.schuelerMenge[i].aktionZustandLadenVon(b.schuelerMenge[i], this.kursMenge);
		}
	}

	/**
	 * Lädt den zuvor gespeicherten Zustand G (Kursverteilung und Schülerverteilung).
	 */
	aktionZustandLadenG(): void {
		for (const schueler of this.schuelerMenge) {
			schueler.aktionKurseAlleEntfernen();
		}
		for (const kurs of this.kursMenge) {
			kurs.aktionZustandLadenG();
		}
		for (const schueler of this.schuelerMenge) {
			schueler.aktionZustandLadenG();
		}
	}

	/**
	 * Lädt den zuvor gespeicherten Zustand K (nur Kursverteilung, ohne Schülerverteilung).
	 */
	aktionZustandLadenKohneSuS(): void {
		for (const schueler of this.schuelerMenge) {
			schueler.aktionKurseAlleEntfernen();
		}
		for (const kurs of this.kursMenge) {
			kurs.aktionZustandLadenK();
		}
	}

	/**
	 * Entfernt alle SuS aus ihren Kursen.
	 */
	aktionSchuelerAusAllenKursenEntfernen(): void {
		for (const s of this.schuelerMenge) {
			s.aktionKurseAlleEntfernen();
		}
	}

	/**
	 * Verteilt alle Kurse auf ihre Schienen zufällig. Kurse die keinen Freiheitsgrad haben, werden dabei ignoriert.
	 */
	aktionKurseFreieZufaelligVerteilen(): void {
		for (const kurs of this.kursMengeFrei) {
			kurs.aktionZufaelligVerteilen();
		}
	}

	/**
	 * Verteilt genau einen Kurs zufällig. Kurse die keinen Freiheitsgrad haben, werden dabei ignoriert.
	 */
	aktionKursVerteilenEinenZufaelligenFreien(): void {
		if (this.kursMengeFrei.length === 0) {
			return;
		}
		const index: number = this.rnd.nextInt(this.kursMengeFrei.length);
		const kurs: KursblockungDynKurs = this.kursMengeFrei[index];
		kurs.aktionZufaelligVerteilen();
	}

	/**
	 * Verteilt einen Kurs zufällig. Kurse die keinen Freiheitsgrad haben und Multikurse, werden dabei ignoriert.
	 */
	aktionKursFreienEinenZufaelligVerteilenAberNichtMultikurse(): void {
		if (this.kursMengeFrei.length === 0) {
			return;
		}
		const perm: Array<number> | null = KursblockungStatic.gibPermutation(this.rnd, this.kursMengeFrei.length);
		for (const index of perm) {
			const kurs: KursblockungDynKurs = this.kursMengeFrei[index];
			if (kurs.gibSchienenAnzahl() === 1) {
				kurs.aktionZufaelligVerteilen();
			}
		}
	}

	/**
	 * Verändert die Lage der Kurse einer zufälligen Fachgruppe komplett neu.
	 */
	public aktionKursVerteilenEineZufaelligeFachgruppe(): void {
		if (this.fachartMenge.length === 0) {
			return;
		}
		const fachgruppenIndex: number = this.rnd.nextInt(this.fachartMenge.length);
		for (const kurs of this.fachartMenge[fachgruppenIndex].gibKurse()) {
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
	aktionKurseVerteilenNachSchuelerwunsch(): boolean {
		let kurslagenVeraenderung: boolean = false;
		const perm: Array<number> = KursblockungStatic.gibPermutation(this.rnd, this.schuelerMenge.length);
		for (const p of perm) {
			const schueler: KursblockungDynSchueler | null = this.schuelerMenge[p];
			schueler.aktionKurseVerteilenNurMultikurseZufaellig();
			kurslagenVeraenderung = kurslagenVeraenderung || schueler.aktionKurseVerteilenNachDeinemWunsch();
		}
		return kurslagenVeraenderung;
	}

	/**
	 * Gesucht wird der Schüler, der unzufrieden ist. Nach seinem Wunsch werden die Kurse neuverteilt.
	 * <br> Kurzzeitig wird der S. Kursen hinzugefügt, am Ende aber wieder entfernt.
	 *
	 * @return TRUE, falls es zu einer Veränderung der Kurslage kam.
	 */
	aktionKurseVerteilenNachSchuelerwunschSingle(): boolean {
		let kurslagenVeraenderung: boolean = false;
		const perm: Array<number> = KursblockungStatic.gibPermutation(this.rnd, this.schuelerMenge.length);
		for (const p of perm) {
			const schueler: KursblockungDynSchueler | null = this.schuelerMenge[p];
			schueler.aktionKurseVerteilenNurMultikurseZufaellig();
			schueler.aktionKurseVerteilenMitBipartiteMatching();
			const nichtwahlen: number = schueler.gibNichtwahlen();
			schueler.aktionKurseAlleEntfernen();
			if (nichtwahlen > 0) {
				schueler.aktionKurseVerteilenNurMultikurseZufaellig();
				kurslagenVeraenderung = kurslagenVeraenderung || schueler.aktionKurseVerteilenNachDeinemWunsch();
				schueler.aktionKurseAlleEntfernen();
				break;
			}
		}
		return kurslagenVeraenderung;
	}

	/**
	 * Verteilt die SuS auf die jetzige Kurslage. Pro S. werden erst die Multikurse verteilt, dann werden die übrigen
	 * Kurse mit Hilfe eines bipartiten Matching-Algorithmus verteilt. Bereits belegte Facharten werden übersprungen.
	 */
	public aktionSchuelerVerteilenMitBipartitemMatching(): void {
		const perm: Array<number> = KursblockungStatic.gibPermutation(this.rnd, this.schuelerMenge.length);
		for (const i of perm) {
			const schueler: KursblockungDynSchueler | null = this.schuelerMenge[i];
			schueler.aktionKurseVerteilenNurMultikurseZufaellig();
			schueler.aktionKurseVerteilenNurFachartenMitEinemErlaubtenKurs();
			schueler.aktionKurseVerteilenMitBipartiteMatching();
		}
	}

	/**
	 * Verteilt die SuS auf die jetzige Kurslage. Pro S. werden erst die Multikurse verteilt, dann werden die übrigen
	 * Kurse mit Hilfe eines gewichteten Bipartiten-Matching-Algorithmus verteilt.
	 */
	public aktionSchuelerVerteilenMitGewichtetenBipartitemMatching(): void {
		const perm: Array<number> = KursblockungStatic.gibPermutation(this.rnd, this.schuelerMenge.length);
		for (const i of perm) {
			const schueler: KursblockungDynSchueler | null = this.schuelerMenge[i];
			schueler.aktionKurseVerteilenNurMultikurseZufaellig();
			schueler.aktionKurseVerteilenNurFachartenMitEinemErlaubtenKurs();
			schueler.aktionKurseVerteilenMitBipartiteMatchingGewichtet();
		}
	}

	/**
	 * Setzt den S. wenn möglich in den übergebenen Kurs.
	 *
	 * @param idSchuelerDB  Die Datenbank-ID des S.
	 * @param idKursDB      Die Datenbank-ID des Kurses.
	 */
	public aktionSchuelerSetzenInKurs(idSchuelerDB: number, idKursDB: number): void {
		for (const schueler of this.schuelerMenge) {
			if (schueler.gibDatenbankID() === idSchuelerDB) {
				schueler.aktionKursSetzen(idKursDB);
			}
		}
	}

	/**
	 * Entfernt den S. wenn möglich aus den übergebenen Kurs.
	 *
	 * @param idSchuelerDB  Die Datenbank-ID des S.
	 * @param idKursDB      Die Datenbank-ID des Kurses.
	 */
	public aktionSchuelerEntfernenAusKurs(idSchuelerDB: number, idKursDB: number): void {
		for (const schueler of this.schuelerMenge) {
			if (schueler.gibDatenbankID() === idSchuelerDB) {
				schueler.aktionKursEntfernen(idKursDB);
			}
		}
	}

	/**
	 * Verschiebt den Kurs in die Schiene.
	 *
	 * @param idKursDB  Die Datenbank-ID des Kurses.
	 * @param schieneDB Die Datenbank-ID der Schiene (1-indiziert!).
	 */
	public aktionSetzeKursInSchiene(idKursDB: number, schieneDB: number): void {
		for (const k of this.kursMenge) {
			if (k.gibDatenbankID() === idKursDB) {
				k.aktionSetzeInSchiene(schieneDB - 1);
			}
		}
	}

	/**
	 * Liefert einen StringBuilder mit einer Debug-Ausgabe der Schienen mit Kursen und optional Schülern und optional Facharten.
	 *
	 * @param mitSchuelern   true, wenn bei den Schienen zusätzlich die Schüler (pro Kurs) ausgegeben werden sollen.
	 * @param mitFacharten   true, wenn zusätzlich am Ende alle Facharten mit den zugehörigen Kursen angehängt werden sollen.
	 *
	 * @return einen StringBuilder mit einer Debug-Ausgabe der Schienen mit Kursen und optional Schülern und optional Facharten.
	 */
	public erzeugeDebugAusgabe(mitSchuelern: boolean, mitFacharten: boolean): StringBuilder | null {
		const sb: StringBuilder | null = new StringBuilder();
		for (let i: number = 0; i < this.schienenMenge.length; i++) {
			sb.append(this.schienenMenge[i].debugAusgabeKurseUndSchueler(mitSchuelern, this.schuelerMenge));
		}
		if (mitFacharten) {
			for (const fachart of this.fachartMenge) {
				sb.append(fachart.debugAusgabeKurse());
			}
		}
		return sb;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.kursblockung.KursblockungDynDaten';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.kursblockung.KursblockungDynDaten'].includes(name);
	}

	public static readonly class = new Class<KursblockungDynDaten>('de.svws_nrw.core.kursblockung.KursblockungDynDaten');

}

export function cast_de_svws_nrw_core_kursblockung_KursblockungDynDaten(obj: unknown): KursblockungDynDaten {
	return obj as KursblockungDynDaten;
}
