import { JavaObject } from '../../java/lang/JavaObject';
import { GostBlockungsergebnisManager } from '../../core/utils/gost/GostBlockungsergebnisManager';
import { KursblockungDynFachart } from '../../core/kursblockung/KursblockungDynFachart';
import { KursblockungStatic } from '../../core/kursblockung/KursblockungStatic';
import { HashMap } from '../../java/util/HashMap';
import { KursblockungDynSchiene } from '../../core/kursblockung/KursblockungDynSchiene';
import { ArrayList } from '../../java/util/ArrayList';
import { KursblockungDynKurs } from '../../core/kursblockung/KursblockungDynKurs';
import { DeveloperNotificationException } from '../../core/exceptions/DeveloperNotificationException';
import { Logger } from '../../core/logger/Logger';
import { GostBlockungRegel } from '../../core/data/gost/GostBlockungRegel';
import { GostKursart } from '../../core/types/gost/GostKursart';
import { GostKursblockungRegelTyp } from '../../core/types/kursblockung/GostKursblockungRegelTyp';
import { Random } from '../../java/util/Random';
import { List } from '../../java/util/List';
import { HashSet } from '../../java/util/HashSet';
import { GostBlockungKurs } from '../../core/data/gost/GostBlockungKurs';
import { GostFach } from '../../core/data/gost/GostFach';
import { KursblockungDynStatistik } from '../../core/kursblockung/KursblockungDynStatistik';
import { GostBlockungsdatenManager } from '../../core/utils/gost/GostBlockungsdatenManager';
import { LinkedCollection } from '../../core/adt/collection/LinkedCollection';
import { ArrayMap } from '../../core/adt/map/ArrayMap';
import { GostFachwahl } from '../../core/data/gost/GostFachwahl';
import { GostBlockungSchiene } from '../../core/data/gost/GostBlockungSchiene';
import { KursblockungDynSchueler } from '../../core/kursblockung/KursblockungDynSchueler';
import { JavaMap } from '../../java/util/JavaMap';
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
	private readonly _regelMap : JavaMap<GostKursblockungRegelTyp, LinkedCollection<GostBlockungRegel>>;

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
	private readonly _fachartMap : HashMap<number, HashMap<number, KursblockungDynFachart>>;

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
	 * Der Konstruktor der Klasse liest alle Daten von {@link GostBlockungsdatenManager} ein und baut die relevanten
	 * Datenstrukturen auf.
	 *
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pLogger Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param pInput  Die Eingabedaten (Schnittstelle zur GUI).
	 */
	public constructor(pRandom : Random, pLogger : Logger, pInput : GostBlockungsdatenManager) {
		super();
		this._random = pRandom;
		this._logger = pLogger;
		this._regelMap = new ArrayMap(GostKursblockungRegelTyp.values());
		this._maxTimeMillis = pInput.getMaxTimeMillis();
		this._schienenArr = Array(0).fill(null);
		this._kursArr = Array(0).fill(null);
		this._kursArrFrei = Array(0).fill(null);
		this._kursMap = new HashMap();
		this._fachartArr = Array(0).fill(null);
		this._fachartMap = new HashMap();
		this._schuelerArr = Array(0).fill(null);
		this._schuelerMap = new HashMap();
		this._statistik = new KursblockungDynStatistik(this._logger);
		this.schritt01FehlerBeiReferenzen(pInput);
		this.schritt02FehlerBeiRegelGruppierung(pInput.daten().regeln);
		this.schritt03FehlerBeiFachartenErstellung(pInput);
		this.schritt04FehlerBeiSchuelerErstellung(pInput);
		this.schritt05FehlerBeiSchuelerFachwahlenErstellung(pInput, this._schuelerArr);
		this.schritt06FehlerBeiStatistikErstellung(this._fachartArr, this._schuelerArr, pInput);
		this.schritt07FehlerBeiSchienenErzeugung(pInput.getSchienenAnzahl());
		this.schritt08FehlerBeiKursErstellung(pInput);
		this.schritt09FehlerBeiKursFreiErstellung();
		this.schritt10FehlerBeiFachartKursArrayErstellung();
		this.schritt11FehlerBeiRegel_4_oder_5();
		this.schritt12FehlerBeiRegel_7_oder_8();
		this.schritt13FehlerBeiRegel_9(pInput);
		this.aktionZustandSpeichernS();
		this.aktionZustandSpeichernK();
		this.aktionZustandSpeichernG();
	}

	/**
	 * Überprüft alle Referenzen in {@link KursblockungInput} und auch die referentielle Integrität.
	 *
	 * @param pInput Das {@link KursblockungInput}-Objekt von der GUI.
	 */
	private schritt01FehlerBeiReferenzen(pInput : GostBlockungsdatenManager) : void {
		if (pInput === null)
			throw new DeveloperNotificationException("GostBlockungsdatenManager == null")
		if (pInput.daten() === null)
			throw new DeveloperNotificationException("GostBlockungsdatenManager.daten() == null")
		if (pInput.daten().fachwahlen === null)
			throw new DeveloperNotificationException("GostBlockungsdatenManager.daten().fachwahlen == null")
		if (pInput.faecherManager() === null)
			throw new DeveloperNotificationException("GostBlockungsdatenManager.faecherManager() == null")
		if (pInput.faecherManager().faecher() === null)
			throw new DeveloperNotificationException("GostBlockungsdatenManager.faecherManager().faecher() == null")
		if (GostKursart.values() === null)
			throw new DeveloperNotificationException("GostKursart.values() == null")
		if (pInput.daten().kurse === null)
			throw new DeveloperNotificationException("GostBlockungsdatenManager.daten().kurse == null")
		if (pInput.daten().regeln === null)
			throw new DeveloperNotificationException("GostBlockungsdatenManager.daten().regeln == null")
		if (pInput.getID() < 0)
			throw new DeveloperNotificationException("GostBlockungsdatenManager.getID() < 0")
		if (pInput.daten().fachwahlen.isEmpty())
			throw new DeveloperNotificationException("GostBlockungsdatenManager.daten().fachwahlen.isEmpty()")
		if (pInput.faecherManager().faecher().isEmpty())
			throw new DeveloperNotificationException("GostBlockungsdatenManager.faecherManager().faecher().isEmpty()")
		if (GostKursart.values().length <= 0)
			throw new DeveloperNotificationException("GostKursart.values().length <= 0")
		if (pInput.daten().kurse.isEmpty())
			throw new DeveloperNotificationException("GostBlockungsdatenManager.daten().kurse.isEmpty()")
		const schienenAnzahl : number = pInput.getSchienenAnzahl();
		if (schienenAnzahl <= 0)
			throw new DeveloperNotificationException("GostBlockungsdatenManager.getSchienenAnzahl() <= 0")
		const usedSchiene : HashSet<number | null> | null = new HashSet();
		for (const gSchiene of pInput.daten().schienen) {
			if (gSchiene.id < 0)
				throw new DeveloperNotificationException("GostBlockungSchiene.id < 0")
			if (gSchiene.nummer < 1)
				throw new DeveloperNotificationException("GostBlockungSchiene.nummer < 1")
			if (gSchiene.nummer > schienenAnzahl)
				throw new DeveloperNotificationException("GostBlockungSchiene.nummer > schienenAnzahl")
			if (!usedSchiene.add(gSchiene.nummer))
				throw new DeveloperNotificationException("GostBlockungSchiene.nummer existiert doppelt!")
		}
		const setKursarten : HashSet<number> = new HashSet();
		for (const iKursart of GostKursart.values()) {
			if (iKursart === null)
				throw new DeveloperNotificationException("GostKursart == null")
			if (iKursart.id < 0)
				throw new DeveloperNotificationException("GostKursart.id < 0 (" + iKursart.kuerzel + ")")
			if (!setKursarten.add(iKursart.id))
				throw new DeveloperNotificationException("GostKursart.id (" + iKursart.id + ") gibt es doppelt!")
		}
		const setFaecher : HashSet<number> = new HashSet();
		for (const iFach of pInput.faecherManager().faecher()) {
			if (iFach === null)
				throw new DeveloperNotificationException("GostFach == null")
			if (iFach.id < 0)
				throw new DeveloperNotificationException("GostFach.id < 0 (" + iFach.kuerzel + ")")
			if (!setFaecher.add(iFach.id))
				throw new DeveloperNotificationException("GostFach.id (" + iFach.id + ") gibt es doppelt!")
		}
		const setKurse : HashSet<number> = new HashSet();
		for (const iKurs of pInput.daten().kurse) {
			if (iKurs === null)
				throw new DeveloperNotificationException("GostBlockungKurs == null")
			if (iKurs.id < 0)
				throw new DeveloperNotificationException("GostBlockungKurs.id < 0")
			const fachID : number = iKurs.fach_id;
			if (!setFaecher.contains(fachID))
				throw new DeveloperNotificationException("GostBlockungKurs (id=" + iKurs.id + "): Unbekannte Fach-ID (" + fachID + ")!")
			const kursartID : number = iKurs.kursart;
			if (!setKursarten.contains(kursartID))
				throw new DeveloperNotificationException("GostBlockungKurs (id=" + iKurs.id + "): Unbekannte Kursart-ID (" + kursartID + ")!")
			setKurse.add(iKurs.id);
		}
		const setSchueler : HashSet<number> = new HashSet();
		for (const iFachwahl of pInput.daten().fachwahlen) {
			if (iFachwahl === null)
				throw new DeveloperNotificationException("GostFachwahl == null")
			const schuelerID : number = iFachwahl.schuelerID;
			if (schuelerID < 0)
				throw new DeveloperNotificationException("GostFachwahl.schuelerID < 0")
			setSchueler.add(schuelerID);
			const fachID : number = iFachwahl.fachID;
			if (!setFaecher.contains(fachID))
				throw new DeveloperNotificationException("GostFachwahl: Unbekannte Fach-ID (" + fachID + ")")
			const kursartID : number = iFachwahl.kursartID;
			if (!setKursarten.contains(kursartID))
				throw new DeveloperNotificationException("GostFachwahl: Unbekannte Kursart-ID (" + kursartID + ")")
		}
		for (const iRegel of pInput.daten().regeln) {
			if (iRegel === null)
				throw new DeveloperNotificationException("GostBlockungRegel == null")
			if (iRegel.parameter === null)
				throw new DeveloperNotificationException("GostBlockungRegel.parameter == null")
			for (let i : number = 0; i < iRegel.parameter.size(); i++)
				if (iRegel.parameter.get(i) === null)
					throw new DeveloperNotificationException("GostBlockungRegel.parameter.get(" + i + ") == null")
			if (iRegel.id < 0)
				throw new DeveloperNotificationException("GostBlockungRegel.id < 0")
			const gostRegel : GostKursblockungRegelTyp = GostKursblockungRegelTyp.fromTyp(iRegel.typ);
			if (gostRegel as unknown === GostKursblockungRegelTyp.UNDEFINIERT as unknown)
				throw new DeveloperNotificationException("GostBlockungRegel.typ (" + iRegel.typ + ") unbekannt.")
			const daten : Array<number> = iRegel.parameter.toArray(Array(0).fill(null));
			if (gostRegel as unknown === GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS as unknown) {
				const length : number = daten.length;
				if (length !== 3)
					throw new DeveloperNotificationException("KURSART_SPERRE_SCHIENEN_VON_BIS daten.length=" + length + ", statt 3!")
				const kursartID : number = iRegel.parameter.get(0)!;
				if (!setKursarten.contains(kursartID))
					throw new DeveloperNotificationException("KURSART_SPERRE_SCHIENEN_VON_BIS hat unbekannte Kursart-ID (" + kursartID + ")")
				const von : number = daten[1]!;
				const bis : number = daten[2]!;
				if (!((von >= 1) && (von <= bis) && (bis <= schienenAnzahl)))
					throw new DeveloperNotificationException("KURSART_SPERRE_SCHIENEN_VON_BIS (" + kursartID + ", " + von + ", " + bis + ") ist unlogisch!")
			}
			if (gostRegel as unknown === GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE as unknown) {
				const length : number = daten.length;
				if (length !== 2)
					throw new DeveloperNotificationException("KURS_FIXIERE_IN_SCHIENE daten.length=" + length + ", statt 2!")
				const kursID : number = daten[0].valueOf();
				if (!setKurse.contains(kursID))
					throw new DeveloperNotificationException("KURS_FIXIERE_IN_SCHIENE hat unbekannte Kurs-ID (" + kursID + ")")
				const schiene : number = daten[1]!;
				if (!((schiene >= 1) && (schiene <= schienenAnzahl)))
					throw new DeveloperNotificationException("KURS_FIXIERE_IN_SCHIENE (" + kursID + ", " + schiene + ") ist unlogisch!")
			}
			if (gostRegel as unknown === GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE as unknown) {
				const length : number = daten.length;
				if (length !== 2)
					throw new DeveloperNotificationException("KURS_SPERRE_IN_SCHIENE daten.length=" + length + ", statt 2!")
				const kursID : number = daten[0].valueOf();
				if (!setKurse.contains(kursID))
					throw new DeveloperNotificationException("KURS_SPERRE_IN_SCHIENE hat unbekannte Kurs-ID (" + kursID + ")")
				const schiene : number = daten[1]!;
				if (!((schiene >= 1) && (schiene <= schienenAnzahl)))
					throw new DeveloperNotificationException("KURS_SPERRE_IN_SCHIENE (" + kursID + ", " + schiene + ") ist unlogisch!")
			}
			if (gostRegel as unknown === GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS as unknown) {
				const length : number = daten.length;
				if (length !== 2)
					throw new DeveloperNotificationException("SCHUELER_FIXIEREN_IN_KURS daten.length=" + length + ", statt 2!")
				const schuelerID : number = daten[0].valueOf();
				if (!setSchueler.contains(schuelerID))
					throw new DeveloperNotificationException("SCHUELER_FIXIEREN_IN_KURS hat unbekannte Schüler-ID (" + schuelerID + ")")
				const kursID : number = daten[1].valueOf();
				if (!setKurse.contains(kursID))
					throw new DeveloperNotificationException("SCHUELER_FIXIEREN_IN_KURS hat unbekannte Kurs-ID (" + kursID + ")")
			}
			if (gostRegel as unknown === GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS as unknown) {
				const length : number = daten.length;
				if (length !== 2)
					throw new DeveloperNotificationException("SCHUELER_VERBIETEN_IN_KURS daten.length=" + length + ", statt 2!")
				const schuelerID : number = daten[0].valueOf();
				if (!setSchueler.contains(schuelerID))
					throw new DeveloperNotificationException("SCHUELER_VERBIETEN_IN_KURS hat unbekannte Schüler-ID (" + schuelerID + ")")
				const kursID : number = daten[1].valueOf();
				if (!setKurse.contains(kursID))
					throw new DeveloperNotificationException("SCHUELER_VERBIETEN_IN_KURS hat unbekannte Kurs-ID (" + kursID + ")")
			}
			if (gostRegel as unknown === GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS as unknown) {
				const length : number = daten.length;
				if (length !== 3)
					throw new DeveloperNotificationException("KURSART_ALLEIN_IN_SCHIENEN_VON_BIS daten.length=" + length + ", statt 3!")
				const kursartID : number = daten[0]!;
				if (!setKursarten.contains(kursartID))
					throw new DeveloperNotificationException("KURSART_ALLEIN_IN_SCHIENEN_VON_BIS hat unbekannte Kursart-ID (" + kursartID + ")")
				const von : number = daten[1]!;
				const bis : number = daten[2]!;
				if (!((von >= 1) && (von <= bis) && (bis <= schienenAnzahl)))
					throw new DeveloperNotificationException("KURSART_ALLEIN_IN_SCHIENEN_VON_BIS (" + kursartID + ", " + von + ", " + bis + ") ist unlogisch!")
			}
			if (gostRegel as unknown === GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS as unknown) {
				const length : number = daten.length;
				if (length !== 2)
					throw new DeveloperNotificationException("KURS_VERBIETEN_MIT_KURS daten.length=" + length + ", statt 2!")
				const kursID1 : number = daten[0].valueOf();
				if (!setKurse.contains(kursID1))
					throw new DeveloperNotificationException("KURS_VERBIETEN_MIT_KURS hat unbekannte 1. Kurs-ID (" + kursID1 + ")!")
				const kursID2 : number = daten[1].valueOf();
				if (!setKurse.contains(kursID2))
					throw new DeveloperNotificationException("KURS_VERBIETEN_MIT_KURS hat unbekannte 2. Kurs-ID (" + kursID2 + ")!")
				if (kursID1 === kursID2)
					throw new UserNotificationException("Die Regel 'KURS_VERBIETEN_MIT_KURS' wurde mit einem Kurs (" + kursID1 + ") und sich selbst kombiniert!")
			}
			if (gostRegel as unknown === GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS as unknown) {
				const length : number = daten.length;
				if (length !== 2)
					throw new DeveloperNotificationException("KURS_ZUSAMMEN_MIT_KURS daten.length=" + length + ", statt 2!")
				const kursID1 : number = daten[0].valueOf();
				if (!setKurse.contains(kursID1))
					throw new DeveloperNotificationException("KURS_ZUSAMMEN_MIT_KURS hat unbekannte 1. Kurs-ID (" + kursID1 + ")!")
				const kursID2 : number = daten[1].valueOf();
				if (!setKurse.contains(kursID2))
					throw new DeveloperNotificationException("KURS_ZUSAMMEN_MIT_KURS hat unbekannte 2. Kurs-ID (" + kursID2 + ")!")
				if (kursID1 === kursID2)
					throw new UserNotificationException("Die Regel 'KURS_ZUSAMMEN_MIT_KURS' wurde mit einem Kurs (" + kursID1 + ") und sich selbst kombiniert!")
			}
			if (gostRegel as unknown === GostKursblockungRegelTyp.LEHRKRAFT_BEACHTEN as unknown) {
				const length : number = daten.length;
				if (length !== 1)
					throw new DeveloperNotificationException("LEHRKRAFT_BEACHTEN daten.length=" + length + ", statt 1!")
				const auchExtern : number = daten[0].valueOf();
				if ((auchExtern < 0) || (auchExtern > 1))
					throw new DeveloperNotificationException("LEHRKRAFT_BEACHTEN AuchExterne-Wert ist nicht 0/1, sondern (" + auchExtern + ")!")
			}
			if (gostRegel as unknown === GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN as unknown) {
				const length : number = daten.length;
				if (length !== 0)
					throw new DeveloperNotificationException("LEHRKRAEFTE_BEACHTEN daten.length=" + length + ", statt 0!")
			}
		}
	}

	private schritt02FehlerBeiRegelGruppierung(pRegeln : List<GostBlockungRegel>) : void {
		const regelDatabaseIDs : HashSet<number | null> | null = new HashSet();
		for (const iRegel of pRegeln) {
			if (iRegel.id < 0)
				throw new DeveloperNotificationException("GostBlockungRegel.id < 0")
			if (!regelDatabaseIDs.add(iRegel.id))
				throw new DeveloperNotificationException("GostBlockungRegel.id (" + iRegel.id + ") gibt es doppelt!")
			const regelTyp : GostKursblockungRegelTyp = GostKursblockungRegelTyp.fromTyp(iRegel.typ);
			let list : LinkedCollection<GostBlockungRegel> | null = this._regelMap.get(regelTyp);
			if (list === null) {
				list = new LinkedCollection();
				this._regelMap.put(regelTyp, list);
			}
			list.addLast(iRegel);
		}
	}

	private schritt03FehlerBeiFachartenErstellung(pInput : GostBlockungsdatenManager) : void {
		let nFacharten : number = 0;
		const nKurse : number = pInput.daten().kurse.size();
		for (const gKurs of pInput.daten().kurse) {
			const fach : GostFach | null = pInput.faecherManager().get(gKurs.fach_id);
			if (fach === null)
				throw new DeveloperNotificationException("GostBlockungKurs (" + gKurs.id + ") die Fach-ID ist im Manager unbekannt!")
			const kursart : GostKursart | null = GostKursart.fromIDorNull(gKurs.kursart);
			if (kursart === null)
				throw new DeveloperNotificationException("GostBlockungKurs (" + gKurs.id + ") die Kursart-ID ist bei GostKursart unbekannt!")
			let kursartMap : HashMap<number, KursblockungDynFachart> | null = this._fachartMap.get(fach.id);
			if (kursartMap === null) {
				kursartMap = new HashMap();
				this._fachartMap.put(fach.id, kursartMap);
			}
			let dynFachart : KursblockungDynFachart | null = kursartMap.get(kursart.id);
			if (dynFachart === null) {
				dynFachart = new KursblockungDynFachart(this._random, nFacharten, fach, kursart, this._statistik);
				kursartMap.put(kursart.id, dynFachart);
				nFacharten++;
			}
			dynFachart.aktionMaxKurseErhoehen();
		}
		for (const iFachwahl of pInput.daten().fachwahlen) {
			const fach : GostFach | null = pInput.faecherManager().get(iFachwahl.fachID);
			if (fach === null)
				throw new DeveloperNotificationException("GostFachwahl: Die Fach-ID (" + iFachwahl.fachID + ") ist im Manager unbekannt!")
			const kursart : GostKursart | null = GostKursart.fromIDorNull(iFachwahl.kursartID);
			if (kursart === null)
				throw new DeveloperNotificationException("GostFachwahl: Die Kursart-ID (" + iFachwahl.kursartID + ") ist bei GostKursart unbekannt!")
			const schuelerID : number = iFachwahl.schuelerID;
			if (schuelerID < 0)
				throw new DeveloperNotificationException("GostFachwahl.schuelerID < 0")
			let kursartMap : HashMap<number, KursblockungDynFachart> | null = this._fachartMap.get(fach.id);
			if (kursartMap === null) {
				kursartMap = new HashMap();
				this._fachartMap.put(fach.id, kursartMap);
			}
			let dynFachart : KursblockungDynFachart | null = kursartMap.get(kursart.id);
			if (dynFachart === null) {
				dynFachart = new KursblockungDynFachart(this._random, nFacharten, fach, kursart, this._statistik);
				kursartMap.put(kursart.id, dynFachart);
				nFacharten++;
			}
			dynFachart.aktionMaxSchuelerErhoehen();
		}
		if (nFacharten <= 0)
			throw new DeveloperNotificationException("Die Blockung hat keine Facharten/Fachwahlen (Fach + Kursart).")
		this._fachartArr = Array(nFacharten).fill(null);
		for (const kursartMap of this._fachartMap.values())
			for (const fachart of kursartMap.values())
				this._fachartArr[fachart.gibNr()] = fachart;
		let kursSumme : number = 0;
		for (let i : number = 0; i < this._fachartArr.length; i++)
			kursSumme += this._fachartArr[i].gibKurseMax();
		if (kursSumme !== nKurse)
			throw new DeveloperNotificationException("Die Summe aller auf die Facharten verteilten Kurse ist ungleich der Gesamtkursanzahl.")
	}

	private schritt04FehlerBeiSchuelerErstellung(pInput : GostBlockungsdatenManager) : void {
		const setSchueler : HashSet<number> = new HashSet();
		for (const fachwahl of pInput.daten().fachwahlen)
			setSchueler.add(fachwahl.schuelerID);
		const nSchueler : number = setSchueler.size();
		const nSchienen : number = pInput.getSchienenAnzahl();
		const nKurse : number = pInput.getKursAnzahl();
		let i : number = 0;
		this._schuelerArr = Array(nSchueler).fill(null);
		for (const schuelerID of setSchueler) {
			const sID : number = schuelerID.valueOf();
			const schueler : KursblockungDynSchueler = new KursblockungDynSchueler(this._logger, this._random, sID, this._statistik, nSchienen, nKurse);
			this._schuelerArr[i] = schueler;
			this._schuelerMap.put(schuelerID, schueler);
			i++;
		}
	}

	private schritt05FehlerBeiSchuelerFachwahlenErstellung(pInput : GostBlockungsdatenManager, susArr : Array<KursblockungDynSchueler>) : void {
		const mapSchuelerFA : HashMap<KursblockungDynSchueler, LinkedCollection<KursblockungDynFachart>> = new HashMap();
		for (let i : number = 0; i < susArr.length; i++)
			mapSchuelerFA.put(susArr[i], new LinkedCollection());
		for (const iFachwahl of pInput.daten().fachwahlen) {
			const schueler : KursblockungDynSchueler | null = this._schuelerMap.get(iFachwahl.schuelerID);
			if (schueler === null)
				throw new DeveloperNotificationException("GostFachwahl.schueler --> KursblockungDynSchueler (mapping fehlt)!")
			const dynFacharten : LinkedCollection<KursblockungDynFachart> | null = mapSchuelerFA.get(schueler);
			if (dynFacharten === null)
				throw new DeveloperNotificationException("dynFacharten == null")
			const dynFachart : KursblockungDynFachart = this.gibFachart(iFachwahl.fachID, iFachwahl.kursartID);
			dynFacharten.addLast(dynFachart);
		}
		for (const schueler of susArr) {
			const listFA : LinkedCollection<KursblockungDynFachart> | null = mapSchuelerFA.get(schueler);
			if (listFA === null)
				throw new DeveloperNotificationException("mapSchuelerFA.get(schueler) == null (mapping fehlt)!")
			const arrFA : Array<KursblockungDynFachart> = listFA.toArray(Array(0).fill(null));
			schueler.aktionSetzeFachartenUndIDs(arrFA);
		}
	}

	private schritt06FehlerBeiStatistikErstellung(fachartArr : Array<KursblockungDynFachart>, susArr : Array<KursblockungDynSchueler>, pInput : GostBlockungsdatenManager) : void {
		const nFacharten : number = fachartArr.length;
		const bewertungMatrixFachart : Array<Array<number>> = [...Array(nFacharten)].map(e => Array(nFacharten).fill(0));
		for (let i : number = 0; i < susArr.length; i++) {
			const fa : Array<KursblockungDynFachart> = susArr[i].gibFacharten();
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
				const nenner : number = (kursAnz1 + kursAnz2 - 2);
				const faktor : number = nenner === 0 ? 1000000 : (Math.trunc(100 / nenner));
				bewertungMatrixFachart[nr1][nr2] *= faktor;
			}
			bewertungMatrixFachart[nr1][nr1] += 10000000;
		}
		this._statistik.aktionInitialisiere(bewertungMatrixFachart, susArr.length, fachartArr.length, pInput.getKursAnzahl());
	}

	private schritt07FehlerBeiSchienenErzeugung(pSchienen : number) : void {
		this._schienenArr = Array(pSchienen).fill(null);
		for (let nr : number = 0; nr < pSchienen; nr++)
			this._schienenArr[nr] = new KursblockungDynSchiene(this._logger, nr, this._statistik);
	}

	private schritt08FehlerBeiKursErstellung(pInput : GostBlockungsdatenManager) : void {
		const nKurse : number = pInput.getKursAnzahl();
		const nSchienen : number = pInput.getSchienenAnzahl();
		const mapKursSchieneFrei : HashMap<number, LinkedCollection<KursblockungDynSchiene>> = new HashMap();
		const mapKursSchieneLage : HashMap<number, LinkedCollection<KursblockungDynSchiene>> = new HashMap();
		this._kursArr = Array(nKurse).fill(null);
		let i : number = 0;
		for (const kurs of pInput.daten().kurse) {
			const schieneLage : LinkedCollection<KursblockungDynSchiene> = new LinkedCollection();
			mapKursSchieneLage.put(kurs.id, schieneLage);
			const schieneFrei : LinkedCollection<KursblockungDynSchiene> = new LinkedCollection();
			mapKursSchieneFrei.put(kurs.id, schieneFrei);
			const perm : Array<number> = KursblockungStatic.gibPermutation(this._random, nSchienen);
			for (let j : number = 0; j < nSchienen; j++)
				schieneFrei.addLast(this._schienenArr[perm[j]]);
			const regelnTyp1 : LinkedCollection<GostBlockungRegel> | null = this._regelMap.get(GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS);
			if (regelnTyp1 !== null)
				for (const regel1 of regelnTyp1)
					if (kurs.kursart === regel1.parameter.get(0)) {
						const von : number = regel1.parameter.get(1)!;
						const bis : number = regel1.parameter.get(2)!;
						for (let schiene : number = von; schiene <= bis; schiene++)
							schieneFrei.remove(this._schienenArr[schiene - 1]);
					}
			const regelnTyp6 : LinkedCollection<GostBlockungRegel> | null = this._regelMap.get(GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS);
			if (regelnTyp6 !== null)
				for (const regel6 of regelnTyp6) {
					const von : number = regel6.parameter.get(1)!;
					const bis : number = regel6.parameter.get(2)!;
					for (let schiene : number = 1; schiene <= this._schienenArr.length; schiene++) {
						const innerhalb : boolean = (von <= schiene) && (schiene <= bis);
						const gleicheArt : boolean = kurs.kursart === regel6.parameter.get(0);
						if (innerhalb !== gleicheArt)
							schieneFrei.remove(this._schienenArr[schiene - 1]);
					}
				}
			const regelnTyp3 : LinkedCollection<GostBlockungRegel> | null = this._regelMap.get(GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE);
			if (regelnTyp3 !== null)
				for (const regel3 of regelnTyp3)
					if (kurs.id === regel3.parameter.get(0)) {
						const schiene : number = regel3.parameter.get(1)!;
						schieneFrei.remove(this._schienenArr[schiene - 1]);
					}
			const regelnTyp2 : LinkedCollection<GostBlockungRegel> | null = this._regelMap.get(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE);
			if (regelnTyp2 !== null)
				for (const regel2 of regelnTyp2)
					if (kurs.id === regel2.parameter.get(0)) {
						const schiene : number = regel2.parameter.get(1)!;
						const dynSchiene : KursblockungDynSchiene = this._schienenArr[schiene - 1];
						if (schieneLage.contains(dynSchiene))
							continue;
						if (!schieneFrei.contains(dynSchiene))
							throw new UserNotificationException("Die Regel 'KURS_FIXIERE_IN_SCHIENE' will Kurs (id=" + kurs.id + ") in Schiene (" + schiene + ") fixieren, aber die Schiene wurde bereits gesperrt!")
						schieneFrei.remove(dynSchiene);
						schieneLage.addLast(dynSchiene);
					}
			const schienen : number = kurs.anzahlSchienen;
			if (schienen <= 0)
				throw new DeveloperNotificationException("Kurs (" + kurs.id + ") belegt nur " + schienen + " Schienen, das ist zu wenig.")
			if (schienen > this._schienenArr.length)
				throw new DeveloperNotificationException("Es gibt " + this._schienenArr.length + " Schienen, aber der Kurs (" + kurs.id + ") möchte " + schienen + " Schienen belegen.")
			const pSchienenLageFixiert : number = schieneLage.size();
			if (pSchienenLageFixiert > schienen)
				throw new DeveloperNotificationException("Kurs (" + kurs.id + ") fixert " + pSchienenLageFixiert + " Schienen, das ist mehr als seine Schienenanzahl " + schienen + ".")
			while (schieneLage.size() < schienen) {
				if (schieneFrei.isEmpty())
					throw new UserNotificationException("Der Kurs (" + kurs.id + ") hat zu viele Schienen gesperrt, so dass seine Schienenanzahl nicht erfüllt werden kann!")
				schieneLage.addLast(schieneFrei.pollFirst());
			}
			const pSchienenLage : Array<KursblockungDynSchiene> = schieneLage.toArray(Array(0).fill(null));
			const pSchienenFrei : Array<KursblockungDynSchiene> = schieneFrei.toArray(Array(0).fill(null));
			const dynFachart : KursblockungDynFachart = this.gibFachart(kurs.fach_id, kurs.kursart);
			const dynKurs : KursblockungDynKurs = new KursblockungDynKurs(this._random, pSchienenLage, pSchienenLageFixiert, pSchienenFrei, kurs.id, dynFachart, this._logger, i);
			this._kursArr[i] = dynKurs;
			this._kursMap.put(kurs.id, dynKurs);
			i++;
		}
	}

	private schritt09FehlerBeiKursFreiErstellung() : void {
		let nKursFrei : number = 0;
		for (const kurs of this._kursArr)
			if (kurs.gibHatFreiheitsgrade())
				nKursFrei++;
		this._kursArrFrei = Array(nKursFrei).fill(null);
		let j : number = 0;
		for (let i : number = 0; i < this._kursArr.length; i++)
			if (this._kursArr[i].gibHatFreiheitsgrade()) {
				this._kursArrFrei[j] = this._kursArr[i];
				j++;
			}
	}

	private schritt10FehlerBeiFachartKursArrayErstellung() : void {
		const nFacharten : number = this._fachartArr.length;
		const mapFachartList : HashMap<number, LinkedCollection<KursblockungDynKurs>> = new HashMap();
		for (let i : number = 0; i < nFacharten; i++)
			mapFachartList.put(i, new LinkedCollection());
		for (const kurs of this._kursArr) {
			const fachartNr : number = kurs.gibFachart().gibNr();
			const fachartKurse : LinkedCollection<KursblockungDynKurs> | null = mapFachartList.get(fachartNr);
			if (fachartKurse === null)
				throw new DeveloperNotificationException("mapFachartList.get(fachartNr) == null")
			fachartKurse.addLast(kurs);
		}
		for (let nr : number = 0; nr < nFacharten; nr++) {
			const list : LinkedCollection<KursblockungDynKurs> | null = mapFachartList.get(nr);
			if (list === null)
				throw new DeveloperNotificationException("mapFachartList.get(nr) == null")
			const tmpKursArr : Array<KursblockungDynKurs> = list.toArray(Array(0).fill(null));
			this._fachartArr[nr].aktionSetKurse(tmpKursArr);
		}
	}

	private schritt11FehlerBeiRegel_4_oder_5() : void {
		const regelnTyp4 : LinkedCollection<GostBlockungRegel> | null = this._regelMap.get(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS);
		if (regelnTyp4 !== null)
			for (const regel4 of regelnTyp4) {
				const schuelerID : number = regel4.parameter.get(0).valueOf();
				const kursID : number = regel4.parameter.get(1).valueOf();
				const schueler : KursblockungDynSchueler = this.gibSchueler(schuelerID);
				const fixierterKurs : KursblockungDynKurs = this.gibKurs(kursID);
				for (const kurs of fixierterKurs.gibFachart().gibKurse())
					if (kurs as unknown !== fixierterKurs as unknown)
						schueler.aktionSetzeKursSperrung(kurs.gibInternalID());
			}
		const regelnTyp5 : LinkedCollection<GostBlockungRegel> | null = this._regelMap.get(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS);
		if (regelnTyp5 !== null)
			for (const regel5 of regelnTyp5) {
				const schuelerID : number = regel5.parameter.get(0).valueOf();
				const kursID : number = regel5.parameter.get(1).valueOf();
				const schueler : KursblockungDynSchueler = this.gibSchueler(schuelerID);
				const verbotenerKurs : KursblockungDynKurs = this.gibKurs(kursID);
				schueler.aktionSetzeKursSperrung(verbotenerKurs.gibInternalID());
			}
	}

	private schritt12FehlerBeiRegel_7_oder_8() : void {
		const regelnTyp7 : LinkedCollection<GostBlockungRegel> | null = this._regelMap.get(GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS);
		if (regelnTyp7 !== null)
			for (const regel7 of regelnTyp7) {
				const kursID1 : number = regel7.parameter.get(0).valueOf();
				const kursID2 : number = regel7.parameter.get(1).valueOf();
				const kurs1 : KursblockungDynKurs = this.gibKurs(kursID1);
				const kurs2 : KursblockungDynKurs = this.gibKurs(kursID2);
				this._statistik.regelHinzufuegenKursVerbieteMitKurs(kurs1, kurs2);
			}
		const regelnTyp8 : LinkedCollection<GostBlockungRegel> | null = this._regelMap.get(GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS);
		if (regelnTyp8 !== null)
			for (const regel8 of regelnTyp8) {
				const kursID1 : number = regel8.parameter.get(0).valueOf();
				const kursID2 : number = regel8.parameter.get(1).valueOf();
				const kurs1 : KursblockungDynKurs = this.gibKurs(kursID1);
				const kurs2 : KursblockungDynKurs = this.gibKurs(kursID2);
				this._statistik.regelHinzufuegenKursZusammenMitKurs(kurs1, kurs2);
			}
	}

	private schritt13FehlerBeiRegel_9(pInput : GostBlockungsdatenManager) : void {
		const regelnTyp9 : LinkedCollection<GostBlockungRegel> | null = this._regelMap.get(GostKursblockungRegelTyp.LEHRKRAFT_BEACHTEN);
		if (regelnTyp9 !== null) {
			const vKurseMitLehrkraft : ArrayList<GostBlockungKurs> = new ArrayList();
			for (const gKurs of pInput.daten().kurse)
				if (!gKurs.lehrer.isEmpty())
					vKurseMitLehrkraft.add(gKurs);
			for (const regel9 of regelnTyp9) {
				const externBeachten : boolean = regel9.parameter.get(0) === 1;
				for (const gKurs1 of vKurseMitLehrkraft)
					for (const gKurs2 of vKurseMitLehrkraft)
						if (gKurs1.id < gKurs2.id)
							for (const gLehr1 of gKurs1.lehrer)
								for (const gLehr2 of gKurs2.lehrer)
									if ((gLehr1.id === gLehr2.id) && ((externBeachten) || (!gLehr1.istExtern))) {
										const kurs1 : KursblockungDynKurs = this.gibKurs(gKurs1.id);
										const kurs2 : KursblockungDynKurs = this.gibKurs(gKurs2.id);
										this._statistik.regelHinzufuegenKursVerbieteMitKurs(kurs1, kurs2);
									}
			}
		}
		const regelnTyp10 : LinkedCollection<GostBlockungRegel> | null = this._regelMap.get(GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN);
		if (regelnTyp10 !== null) {
			const vKurseMitLehrkraft : ArrayList<GostBlockungKurs> = new ArrayList();
			for (const gKurs of pInput.daten().kurse)
				if (!gKurs.lehrer.isEmpty())
					vKurseMitLehrkraft.add(gKurs);
			for (const regel10 of regelnTyp10)
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
	}

	private gibFachart(pFachID : number, pKursart : number) : KursblockungDynFachart {
		const kursartMap : HashMap<number, KursblockungDynFachart> | null = this._fachartMap.get(pFachID);
		if (kursartMap === null)
			throw new DeveloperNotificationException("gibFachart(" + pFachID + ", " + pKursart + ") schlug fehl (Fach)!")
		const dynFachart : KursblockungDynFachart | null = kursartMap.get(pKursart);
		if (dynFachart === null)
			throw new DeveloperNotificationException("gibFachart(" + pFachID + ", " + pKursart + ") schlug fehl (Kursart)!")
		return dynFachart;
	}

	private gibSchueler(schuelerID : number) : KursblockungDynSchueler {
		const schueler : KursblockungDynSchueler | null = this._schuelerMap.get(schuelerID);
		if (schueler === null)
			throw new DeveloperNotificationException("schuelerMap.get(" + schuelerID + ") --> kein Mapping!")
		return schueler;
	}

	private gibKurs(kursID : number) : KursblockungDynKurs {
		const kurs : KursblockungDynKurs | null = this._kursMap.get(kursID);
		if (kurs === null)
			throw new DeveloperNotificationException("kursMap.get(" + kursID + ") --> kein Mapping!")
		return kurs;
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
	 * Erzeugt ein Objekt {@link GostBlockungsergebnisManager}. Dieses Objekt beinhaltet alle Informationen aus denen
	 * die GUI die Kurs-Zu-Schiene und die SuS-Zu-Kurs-Zuordnungen rekonstruieren kann.
	 *
	 * @param  pDataManager  Das Eingabe-Objekt (der Daten-Manager).
	 * @param  pErgebnisID   Die ID des Ergebnisses.
	 * @return               Das Blockungsergebnis für die GUI.
	 */
	gibErzeugtesKursblockungOutput(pDataManager : GostBlockungsdatenManager, pErgebnisID : number) : GostBlockungsergebnisManager {
		const out : GostBlockungsergebnisManager = new GostBlockungsergebnisManager(pDataManager, pErgebnisID);
		for (const dynKurs of this._kursArr)
			for (const schienenNr of dynKurs.gibSchienenLage())
				out.setKursSchienenNr(dynKurs.gibDatenbankID(), schienenNr + 1);
		for (const dynSchueler of this._schuelerArr)
			for (const kurs of dynSchueler.gibKurswahlen())
				if (kurs !== null)
					out.setSchuelerKurs(dynSchueler.gibDatenbankID(), kurs.gibDatenbankID(), true);
		for (const gRegel of pDataManager.getMengeOfRegeln())
			if (gRegel.typ === GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ) {
				const schuelerID : number = gRegel.parameter.get(0).valueOf();
				const kursID : number = gRegel.parameter.get(1).valueOf();
				if (!out.getOfSchuelerOfKursIstZugeordnet(schuelerID, kursID))
					out.setSchuelerKurs(schuelerID, kursID, true);
			}
		return out;
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
	 * Liefert ein Array aller Schülerinnen und Schüler. Falls der Parameter {@code pNurMultiKurse} TRUE ist, dann
	 * werden nur SuS mit mindestens einem Multikurs ausgewählt.
	 *
	 * @param  pNurMultiKurse Falls TRUE, dann werden nur SuS mit mindestens einem Multikurs ausgewählt.
	 *
	 * @return                Ein Array aller Schülerinnen und Schüler.
	 */
	gibSchuelerArray(pNurMultiKurse : boolean) : Array<KursblockungDynSchueler> {
		if (pNurMultiKurse) {
			const list : LinkedCollection<KursblockungDynSchueler> = new LinkedCollection();
			for (const schueler of this._schuelerArr) {
				if (schueler.gibHatMultikurs()) {
					list.addLast(schueler);
				}
			}
			const temp : Array<KursblockungDynSchueler> = Array(list.size()).fill(null);
			for (let i : number = 0; i < temp.length; i++) {
				temp[i] = list.removeFirst();
			}
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
	 *Entfernt alle SuS aus ihren Kursen.
	 */
	aktionSchuelerAusAllenKursenEntfernen() : void {
		for (let i : number = 0; i < this._schuelerArr.length; i++) {
			this._schuelerArr[i].aktionKurseAlleEntfernen();
		}
	}

	/**
	 *Debug Ausgaben. Nur für Testzwecke.
	 */
	debug() : void {
		this._logger.modifyIndent(+4);
		this._logger.logLn("########## Schienen ##########");
		for (let i : number = 0; i < this._schienenArr.length; i++) {
			this._logger.logLn("Schiene " + (i + 1));
			this._schienenArr[i].debug(false);
		}
		this._logger.logLn("########## Facharten ##########");
		for (let i : number = 0; i < this._fachartArr.length; i++) {
			this._logger.logLn("Fachart " + this._fachartArr[i] + " --> " + this._fachartArr[i].gibKursdifferenz());
			this._fachartArr[i].debug(this._schuelerArr);
		}
		this._logger.modifyIndent(-4);
		this._statistik.debug("");
	}

	/**
	 *Speichert die Bewertung, die Kursverteilung und die Schülerverteilung im Zustand S.
	 */
	aktionZustandSpeichernS() : void {
		this._statistik.aktionBewertungSpeichernS();
		for (const kurs of this._kursArr) {
			kurs.aktionZustandSpeichernS();
		}
		for (const schueler of this._schuelerArr) {
			schueler.aktionZustandSpeichernS();
		}
	}

	/**
	 *Speichert die Bewertung, die Kursverteilung und die Schülerverteilung im Zustand K.
	 */
	aktionZustandSpeichernK() : void {
		this._statistik.aktionBewertungSpeichernK();
		for (const kurs of this._kursArr) {
			kurs.aktionZustandSpeichernK();
		}
		for (const schueler of this._schuelerArr) {
			schueler.aktionZustandSpeichernK();
		}
	}

	/**
	 *Speichert die Bewertung, die Kursverteilung und die Schülerverteilung im Zustand G.
	 */
	aktionZustandSpeichernG() : void {
		this._statistik.aktionBewertungSpeichernG();
		for (const kurs of this._kursArr) {
			kurs.aktionZustandSpeichernG();
		}
		for (const schueler of this._schuelerArr) {
			schueler.aktionZustandSpeichernG();
		}
	}

	/**
	 *Lädt den zuvor gespeicherten Zustand S (Kursverteilung und Schülerverteilung).
	 */
	aktionZustandLadenS() : void {
		for (const schueler of this._schuelerArr) {
			schueler.aktionKurseAlleEntfernen();
		}
		for (const kurs of this._kursArr) {
			kurs.aktionZustandLadenS();
		}
		for (const schueler of this._schuelerArr) {
			schueler.aktionZustandLadenS();
		}
	}

	/**
	 *Lädt den zuvor gespeicherten Zustand K (Kursverteilung und Schülerverteilung).
	 */
	aktionZustandLadenK() : void {
		for (const schueler of this._schuelerArr) {
			schueler.aktionKurseAlleEntfernen();
		}
		for (const kurs of this._kursArr) {
			kurs.aktionZustandLadenK();
		}
		for (const schueler of this._schuelerArr) {
			schueler.aktionZustandLadenK();
		}
	}

	/**
	 *Lädt den zuvor gespeicherten Zustand G (Kursverteilung und Schülerverteilung).
	 */
	aktionZustandLadenG() : void {
		for (const schueler of this._schuelerArr) {
			schueler.aktionKurseAlleEntfernen();
		}
		for (const kurs of this._kursArr) {
			kurs.aktionZustandLadenG();
		}
		for (const schueler of this._schuelerArr) {
			schueler.aktionZustandLadenG();
		}
	}

	/**
	 *Lädt den zuvor gespeicherten Zustand K (nur Kursverteilung, ohne Schülerverteilung).
	 */
	aktionZustandLadenKohneSuS() : void {
		for (const schueler of this._schuelerArr) {
			schueler.aktionKurseAlleEntfernen();
		}
		for (const kurs of this._kursArr) {
			kurs.aktionZustandLadenK();
		}
	}

	/**
	 *Verteilt alle Kurse auf ihre Schienen zufällig. Kurse die keinen Freiheitsgrad haben, werden dabei ignoriert.
	 */
	aktionKurseFreieZufaelligVerteilen() : void {
		for (const kurs of this._kursArrFrei)
			kurs.aktionZufaelligVerteilen();
	}

	/**
	 *Verteilt einen Kurs zufällig. Kurse die keinen Freiheitsgrad haben, werden dabei ignoriert.
	 */
	aktionKursVerteilenEinenZufaelligenFreien() : void {
		if (this._kursArrFrei.length === 0)
			return;
		const index : number = this._random.nextInt(this._kursArrFrei.length);
		const kurs : KursblockungDynKurs = this._kursArrFrei[index];
		kurs.aktionZufaelligVerteilen();
	}

	/**
	 * Verteilt einen Kurs zufällig. Kurse die keinen Freiheitsgrad haben, werden dabei ignoriert. Multikurse werden
	 * ebenso ignoriert.
	 */
	aktionKursFreienEinenZufaelligVerteilenAberNichtMultikurse() : void {
		if (this._kursArrFrei.length === 0) {
			return;
		}
		const perm : Array<number> | null = KursblockungStatic.gibPermutation(this._random, this._kursArrFrei.length);
		for (const index of perm) {
			const kurs : KursblockungDynKurs = this._kursArrFrei[index];
			if (kurs.gibSchienenAnzahl() === 1) {
				kurs.aktionZufaelligVerteilen();
			}
		}
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
	 * Verteilt die SuS auf die jetzige Kurslage. Pro S. werden erst die Multikurse verteilt, dann werden die übrigen
	 * Kurse mit Hilfe eines spezielle bipartiten Matching-Algorithmus verteilt. Sobald ein S. seine Nichtwahlen durch
	 * eine Veränderung der Kurslage reduzieren könnte, wird die Kurslage verändert.
	 *
	 * @return TRUE, falls es zu einer Veränderung der Kurslage kam.
	 */
	aktionKurseVerteilenNachSchuelerwunsch() : boolean {
		let kurslagenVeraenderung : boolean = false;
		const perm : Array<number> = KursblockungStatic.gibPermutation(this._random, this._schuelerArr.length);
		for (let pSchueler : number = 0; pSchueler < perm.length; pSchueler++) {
			const schueler : KursblockungDynSchueler | null = this._schuelerArr[perm[pSchueler]];
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
		for (let p : number = 0; p < perm.length; p++) {
			const i : number = perm[p];
			const schueler : KursblockungDynSchueler | null = this._schuelerArr[i];
			schueler.aktionKurseVerteilenNurMultikurseZufaellig();
			schueler.aktionKurseVerteilenNurFachartenMitEinemKurs();
			schueler.aktionKurseVerteilenMitBipartiteMatching();
		}
	}

	/**
	 * Verteilt die SuS auf die jetzige Kurslage. Pro S. werden erst die Multikurse verteilt, dann werden die übrigen
	 * Kurse mit Hilfe eines gewichteten Bipartiten-Matching-Algorithmus verteilt.
	 */
	aktionSchuelerVerteilenMitGewichtetenBipartitemMatching() : void {
		const perm : Array<number> = KursblockungStatic.gibPermutation(this._random, this._schuelerArr.length);
		for (let p : number = 0; p < perm.length; p++) {
			const i : number = perm[p];
			const schueler : KursblockungDynSchueler | null = this._schuelerArr[i];
			schueler.aktionKurseVerteilenNurMultikurseZufaellig();
			schueler.aktionKurseVerteilenNurFachartenMitEinemKurs();
			schueler.aktionKurseVerteilenMitBipartiteMatchingGewichtetem();
		}
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.kursblockung.KursblockungDynDaten'].includes(name);
	}

}

export function cast_de_svws_nrw_core_kursblockung_KursblockungDynDaten(obj : unknown) : KursblockungDynDaten {
	return obj as KursblockungDynDaten;
}
