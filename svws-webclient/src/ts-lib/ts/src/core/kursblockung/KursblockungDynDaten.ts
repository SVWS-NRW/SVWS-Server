import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';
import { GostBlockungsergebnisManager, cast_de_nrw_schule_svws_core_utils_gost_GostBlockungsergebnisManager } from '../../core/utils/gost/GostBlockungsergebnisManager';
import { KursblockungDynFachart, cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynFachart } from '../../core/kursblockung/KursblockungDynFachart';
import { KursblockungStatic, cast_de_nrw_schule_svws_core_kursblockung_KursblockungStatic } from '../../core/kursblockung/KursblockungStatic';
import { HashMap, cast_java_util_HashMap } from '../../java/util/HashMap';
import { KursblockungDynSchiene, cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynSchiene } from '../../core/kursblockung/KursblockungDynSchiene';
import { KursblockungDynKurs, cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynKurs } from '../../core/kursblockung/KursblockungDynKurs';
import { DeveloperNotificationException, cast_de_nrw_schule_svws_core_exceptions_DeveloperNotificationException } from '../../core/exceptions/DeveloperNotificationException';
import { Logger, cast_de_nrw_schule_svws_core_logger_Logger } from '../../core/logger/Logger';
import { GostBlockungRegel, cast_de_nrw_schule_svws_core_data_gost_GostBlockungRegel } from '../../core/data/gost/GostBlockungRegel';
import { GostKursart, cast_de_nrw_schule_svws_core_types_gost_GostKursart } from '../../core/types/gost/GostKursart';
import { System, cast_java_lang_System } from '../../java/lang/System';
import { GostKursblockungRegelTyp, cast_de_nrw_schule_svws_core_types_kursblockung_GostKursblockungRegelTyp } from '../../core/types/kursblockung/GostKursblockungRegelTyp';
import { Random, cast_java_util_Random } from '../../java/util/Random';
import { List, cast_java_util_List } from '../../java/util/List';
import { Vector, cast_java_util_Vector } from '../../java/util/Vector';
import { HashSet, cast_java_util_HashSet } from '../../java/util/HashSet';
import { GostBlockungKurs, cast_de_nrw_schule_svws_core_data_gost_GostBlockungKurs } from '../../core/data/gost/GostBlockungKurs';
import { GostFach, cast_de_nrw_schule_svws_core_data_gost_GostFach } from '../../core/data/gost/GostFach';
import { KursblockungDynStatistik, cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynStatistik } from '../../core/kursblockung/KursblockungDynStatistik';
import { GostBlockungsdatenManager, cast_de_nrw_schule_svws_core_utils_gost_GostBlockungsdatenManager } from '../../core/utils/gost/GostBlockungsdatenManager';
import { LinkedCollection, cast_de_nrw_schule_svws_core_adt_collection_LinkedCollection } from '../../core/adt/collection/LinkedCollection';
import { GostFachwahl, cast_de_nrw_schule_svws_core_data_gost_GostFachwahl } from '../../core/data/gost/GostFachwahl';
import { GostBlockungKursLehrer, cast_de_nrw_schule_svws_core_data_gost_GostBlockungKursLehrer } from '../../core/data/gost/GostBlockungKursLehrer';
import { JavaInteger, cast_java_lang_Integer } from '../../java/lang/JavaInteger';
import { GostBlockungSchiene, cast_de_nrw_schule_svws_core_data_gost_GostBlockungSchiene } from '../../core/data/gost/GostBlockungSchiene';
import { JavaLong, cast_java_lang_Long } from '../../java/lang/JavaLong';
import { KursblockungDynSchueler, cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynSchueler } from '../../core/kursblockung/KursblockungDynSchueler';
import { UserNotificationException, cast_de_nrw_schule_svws_core_exceptions_UserNotificationException } from '../../core/exceptions/UserNotificationException';

export class KursblockungDynDaten extends JavaObject {

	private readonly _random : Random;

	private readonly logger : Logger;

	private readonly regelMap : HashMap<GostKursblockungRegelTyp, LinkedCollection<GostBlockungRegel>>;

	private maxTimeMillis : number = 0;

	private schienenArr : Array<KursblockungDynSchiene>;

	private kursArr : Array<KursblockungDynKurs>;

	private kursArrFrei : Array<KursblockungDynKurs>;

	private readonly kursMap : HashMap<Number, KursblockungDynKurs>;

	private fachartArr : Array<KursblockungDynFachart>;

	private readonly fachartMap : HashMap<Number, HashMap<Number, KursblockungDynFachart>>;

	private schuelerArr : Array<KursblockungDynSchueler>;

	private readonly schuelerMap : HashMap<Number, KursblockungDynSchueler>;

	private readonly statistik : KursblockungDynStatistik;


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
		this.logger = pLogger;
		this.regelMap = new HashMap();
		this.maxTimeMillis = pInput.getMaxTimeMillis();
		this.schienenArr = Array(0).fill(null);
		this.kursArr = Array(0).fill(null);
		this.kursArrFrei = Array(0).fill(null);
		this.kursMap = new HashMap();
		this.fachartArr = Array(0).fill(null);
		this.fachartMap = new HashMap();
		this.schuelerArr = Array(0).fill(null);
		this.schuelerMap = new HashMap();
		this.statistik = new KursblockungDynStatistik();
		this.schritt01FehlerBeiReferenzen(pInput);
		this.schritt02FehlerBeiRegelGruppierung(pInput.daten().regeln);
		this.schritt03FehlerBeiFachartenErstellung(pInput);
		this.schritt04FehlerBeiSchuelerErstellung(pInput);
		this.schritt05FehlerBeiSchuelerFachwahlenErstellung(pInput, this.schuelerArr);
		this.schritt06FehlerBeiStatistikErstellung(this.fachartArr, this.schuelerArr, pInput);
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
		if (pInput.daten().fachwahlen.size() <= 0) 
			throw new DeveloperNotificationException("GostBlockungsdatenManager.daten().fachwahlen.size() <= 0")
		if (pInput.faecherManager().faecher().size() <= 0) 
			throw new DeveloperNotificationException("GostBlockungsdatenManager.faecherManager().faecher().size() <= 0")
		if (GostKursart.values().length <= 0) 
			throw new DeveloperNotificationException("GostKursart.values().length <= 0")
		if (pInput.daten().kurse.size() <= 0) 
			throw new DeveloperNotificationException("GostBlockungsdatenManager.daten().kurse.size() <= 0")
		let schienenAnzahl : number = pInput.getSchienenAnzahl();
		if (schienenAnzahl <= 0) 
			throw new DeveloperNotificationException("GostBlockungsdatenManager.getSchienenAnzahl() <= 0")
		let usedSchiene : HashSet<Number | null> | null = new HashSet();
		for (let gSchiene of pInput.daten().schienen) {
			if (gSchiene.id < 0) 
				throw new DeveloperNotificationException("GostBlockungSchiene.id < 0")
			if (gSchiene.nummer < 1) 
				throw new DeveloperNotificationException("GostBlockungSchiene.nummer < 1")
			if (gSchiene.nummer > schienenAnzahl) 
				throw new DeveloperNotificationException("GostBlockungSchiene.nummer > schienenAnzahl")
			if (usedSchiene.add(gSchiene.nummer) === false) 
				throw new DeveloperNotificationException("GostBlockungSchiene.nummer existiert doppelt!")
		}
		let setKursarten : HashSet<Number> = new HashSet();
		for (let iKursart of GostKursart.values()) {
			if (iKursart === null) 
				throw new DeveloperNotificationException("GostKursart == null")
			if (iKursart.id < 0) 
				throw new DeveloperNotificationException("GostKursart.id < 0 (" + iKursart.kuerzel + ")")
			if (setKursarten.add(iKursart.id) === false) 
				throw new DeveloperNotificationException("GostKursart.id (" + iKursart.id + ") gibt es doppelt!")
		}
		let setFaecher : HashSet<Number> = new HashSet();
		for (let iFach of pInput.faecherManager().faecher()) {
			if (iFach === null) 
				throw new DeveloperNotificationException("GostFach == null")
			if (iFach.id < 0) 
				throw new DeveloperNotificationException("GostFach.id < 0 (" + iFach.kuerzel + ")")
			if (setFaecher.add(iFach.id) === false) 
				throw new DeveloperNotificationException("GostFach.id (" + iFach.id + ") gibt es doppelt!")
		}
		let setKurse : HashSet<Number> = new HashSet();
		for (let iKurs of pInput.daten().kurse) {
			if (iKurs === null) 
				throw new DeveloperNotificationException("GostBlockungKurs == null")
			if (iKurs.id < 0) 
				throw new DeveloperNotificationException("GostBlockungKurs.id < 0")
			let fachID : number = iKurs.fach_id;
			if (!setFaecher.contains(fachID)) 
				throw new DeveloperNotificationException("GostBlockungKurs (id=" + iKurs.id + "): Unbekannte Fach-ID (" + fachID + ")!")
			let kursartID : number = iKurs.kursart;
			if (!setKursarten.contains(kursartID)) 
				throw new DeveloperNotificationException("GostBlockungKurs (id=" + iKurs.id + "): Unbekannte Kursart-ID (" + kursartID + ")!")
			setKurse.add(iKurs.id);
		}
		let setSchueler : HashSet<Number> = new HashSet();
		for (let iFachwahl of pInput.daten().fachwahlen) {
			if (iFachwahl === null) 
				throw new DeveloperNotificationException("GostFachwahl == null")
			let schuelerID : number = iFachwahl.schuelerID;
			if (schuelerID < 0) 
				throw new DeveloperNotificationException("GostFachwahl.schuelerID < 0")
			setSchueler.add(schuelerID);
			let fachID : number = iFachwahl.fachID;
			if (!setFaecher.contains(fachID)) 
				throw new DeveloperNotificationException("GostFachwahl: Unbekannte Fach-ID (" + fachID + ")")
			let kursartID : number = iFachwahl.kursartID;
			if (!setKursarten.contains(kursartID)) 
				throw new DeveloperNotificationException("GostFachwahl: Unbekannte Kursart-ID (" + kursartID + ")")
		}
		for (let iRegel of pInput.daten().regeln) {
			if (iRegel === null) 
				throw new DeveloperNotificationException("GostBlockungRegel == null")
			if (iRegel.parameter === null) 
				throw new DeveloperNotificationException("GostBlockungRegel.parameter == null")
			for (let i : number = 0; i < iRegel.parameter.size(); i++)
				if (iRegel.parameter.get(i) === null) 
					throw new DeveloperNotificationException("GostBlockungRegel.parameter.get(" + i + ") == null")
			if (iRegel.id < 0) 
				throw new DeveloperNotificationException("GostBlockungRegel.id < 0")
			let gostRegel : GostKursblockungRegelTyp = GostKursblockungRegelTyp.fromTyp(iRegel.typ);
			if (gostRegel as unknown === GostKursblockungRegelTyp.UNDEFINIERT as unknown) 
				throw new DeveloperNotificationException("GostBlockungRegel.typ (" + iRegel.typ + ") unbekannt.")
			let daten : Array<Number> = iRegel.parameter.toArray(Array(0).fill(null));
			if (gostRegel as unknown === GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS as unknown) {
				let length : number = daten.length;
				if (length !== 3) 
					throw new DeveloperNotificationException("KURSART_SPERRE_SCHIENEN_VON_BIS daten.length=" + length + ", statt 3!")
				let kursartID : number = iRegel.parameter.get(0).valueOf();
				if (!setKursarten.contains(kursartID)) 
					throw new DeveloperNotificationException("KURSART_SPERRE_SCHIENEN_VON_BIS hat unbekannte Kursart-ID (" + kursartID + ")")
				let von : number = daten[1].valueOf();
				let bis : number = daten[2].valueOf();
				if (!((von >= 1) && (von <= bis) && (bis <= schienenAnzahl))) 
					throw new DeveloperNotificationException("KURSART_SPERRE_SCHIENEN_VON_BIS (" + kursartID + ", " + von + ", " + bis + ") ist unlogisch!")
			}
			if (gostRegel as unknown === GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE as unknown) {
				let length : number = daten.length;
				if (length !== 2) 
					throw new DeveloperNotificationException("KURS_FIXIERE_IN_SCHIENE daten.length=" + length + ", statt 2!")
				let kursID : number = daten[0].valueOf();
				if (!setKurse.contains(kursID)) 
					throw new DeveloperNotificationException("KURS_FIXIERE_IN_SCHIENE hat unbekannte Kurs-ID (" + kursID + ")")
				let schiene : number = daten[1].valueOf();
				if (!((schiene >= 1) && (schiene <= schienenAnzahl))) 
					throw new DeveloperNotificationException("KURS_FIXIERE_IN_SCHIENE (" + kursID + ", " + schiene + ") ist unlogisch!")
			}
			if (gostRegel as unknown === GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE as unknown) {
				let length : number = daten.length;
				if (length !== 2) 
					throw new DeveloperNotificationException("KURS_SPERRE_IN_SCHIENE daten.length=" + length + ", statt 2!")
				let kursID : number = daten[0].valueOf();
				if (!setKurse.contains(kursID)) 
					throw new DeveloperNotificationException("KURS_SPERRE_IN_SCHIENE hat unbekannte Kurs-ID (" + kursID + ")")
				let schiene : number = daten[1].valueOf();
				if (!((schiene >= 1) && (schiene <= schienenAnzahl))) 
					throw new DeveloperNotificationException("KURS_SPERRE_IN_SCHIENE (" + kursID + ", " + schiene + ") ist unlogisch!")
			}
			if (gostRegel as unknown === GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS as unknown) {
				let length : number = daten.length;
				if (length !== 2) 
					throw new DeveloperNotificationException("SCHUELER_FIXIEREN_IN_KURS daten.length=" + length + ", statt 2!")
				let schuelerID : number = daten[0].valueOf();
				if (!setSchueler.contains(schuelerID)) 
					throw new DeveloperNotificationException("SCHUELER_FIXIEREN_IN_KURS hat unbekannte Schüler-ID (" + schuelerID + ")")
				let kursID : number = daten[1].valueOf();
				if (!setKurse.contains(kursID)) 
					throw new DeveloperNotificationException("SCHUELER_FIXIEREN_IN_KURS hat unbekannte Kurs-ID (" + kursID + ")")
			}
			if (gostRegel as unknown === GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS as unknown) {
				let length : number = daten.length;
				if (length !== 2) 
					throw new DeveloperNotificationException("SCHUELER_VERBIETEN_IN_KURS daten.length=" + length + ", statt 2!")
				let schuelerID : number = daten[0].valueOf();
				if (!setSchueler.contains(schuelerID)) 
					throw new DeveloperNotificationException("SCHUELER_VERBIETEN_IN_KURS hat unbekannte Schüler-ID (" + schuelerID + ")")
				let kursID : number = daten[1].valueOf();
				if (!setKurse.contains(kursID)) 
					throw new DeveloperNotificationException("SCHUELER_VERBIETEN_IN_KURS hat unbekannte Kurs-ID (" + kursID + ")")
			}
			if (gostRegel as unknown === GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS as unknown) {
				let length : number = daten.length;
				if (length !== 3) 
					throw new DeveloperNotificationException("KURSART_ALLEIN_IN_SCHIENEN_VON_BIS daten.length=" + length + ", statt 3!")
				let kursartID : number = daten[0].valueOf();
				if (!setKursarten.contains(kursartID)) 
					throw new DeveloperNotificationException("KURSART_ALLEIN_IN_SCHIENEN_VON_BIS hat unbekannte Kursart-ID (" + kursartID + ")")
				let von : number = daten[1].valueOf();
				let bis : number = daten[2].valueOf();
				if (!((von >= 1) && (von <= bis) && (bis <= schienenAnzahl))) 
					throw new DeveloperNotificationException("KURSART_ALLEIN_IN_SCHIENEN_VON_BIS (" + kursartID + ", " + von + ", " + bis + ") ist unlogisch!")
			}
			if (gostRegel as unknown === GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS as unknown) {
				let length : number = daten.length;
				if (length !== 2) 
					throw new DeveloperNotificationException("KURS_VERBIETEN_MIT_KURS daten.length=" + length + ", statt 2!")
				let kursID1 : number = daten[0].valueOf();
				if (!setKurse.contains(kursID1)) 
					throw new DeveloperNotificationException("KURS_VERBIETEN_MIT_KURS hat unbekannte 1. Kurs-ID (" + kursID1 + ")!")
				let kursID2 : number = daten[1].valueOf();
				if (!setKurse.contains(kursID2)) 
					throw new DeveloperNotificationException("KURS_VERBIETEN_MIT_KURS hat unbekannte 2. Kurs-ID (" + kursID2 + ")!")
				if (kursID1 === kursID2) 
					throw new UserNotificationException("Die Regel \'KURS_VERBIETEN_MIT_KURS\' wurde mit einem Kurs (" + kursID1 + ") und sich selbst kombiniert!")
			}
			if (gostRegel as unknown === GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS as unknown) {
				let length : number = daten.length;
				if (length !== 2) 
					throw new DeveloperNotificationException("KURS_ZUSAMMEN_MIT_KURS daten.length=" + length + ", statt 2!")
				let kursID1 : number = daten[0].valueOf();
				if (!setKurse.contains(kursID1)) 
					throw new DeveloperNotificationException("KURS_ZUSAMMEN_MIT_KURS hat unbekannte 1. Kurs-ID (" + kursID1 + ")!")
				let kursID2 : number = daten[1].valueOf();
				if (!setKurse.contains(kursID2)) 
					throw new DeveloperNotificationException("KURS_ZUSAMMEN_MIT_KURS hat unbekannte 2. Kurs-ID (" + kursID2 + ")!")
				if (kursID1 === kursID2) 
					throw new UserNotificationException("Die Regel \'KURS_ZUSAMMEN_MIT_KURS\' wurde mit einem Kurs (" + kursID1 + ") und sich selbst kombiniert!")
			}
			if (gostRegel as unknown === GostKursblockungRegelTyp.LEHRKRAFT_BEACHTEN as unknown) {
				let length : number = daten.length;
				if (length !== 1) 
					throw new DeveloperNotificationException("LEHRKRAFT_BEACHTEN daten.length=" + length + ", statt 1!")
				let auchExtern : number = daten[0].valueOf();
				if ((auchExtern < 0) || (auchExtern > 1)) 
					throw new DeveloperNotificationException("LEHRKRAFT_BEACHTEN AuchExterne-Wert ist nicht 0/1, sondern (" + auchExtern + ")!")
			}
			if (gostRegel as unknown === GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN as unknown) {
				let length : number = daten.length;
				if (length !== 0) 
					throw new DeveloperNotificationException("LEHRKRAEFTE_BEACHTEN daten.length=" + length + ", statt 0!")
			}
		}
	}

	private schritt02FehlerBeiRegelGruppierung(pRegeln : List<GostBlockungRegel>) : void {
		let regelDatabaseIDs : HashSet<Number | null> | null = new HashSet();
		for (let iRegel of pRegeln) {
			if (iRegel.id < 0) 
				throw new DeveloperNotificationException("GostBlockungRegel.id < 0")
			if (regelDatabaseIDs.add(iRegel.id) === false) 
				throw new DeveloperNotificationException("GostBlockungRegel.id (" + iRegel.id + ") gibt es doppelt!")
			let regelTyp : GostKursblockungRegelTyp = GostKursblockungRegelTyp.fromTyp(iRegel.typ);
			let list : LinkedCollection<GostBlockungRegel> | null = this.regelMap.get(regelTyp);
			if (list === null) {
				list = new LinkedCollection();
				this.regelMap.put(regelTyp, list);
			}
			list.addLast(iRegel);
		}
	}

	private schritt03FehlerBeiFachartenErstellung(pInput : GostBlockungsdatenManager) : void {
		let nFacharten : number = 0;
		let nKurse : number = pInput.daten().kurse.size();
		for (let gKurs of pInput.daten().kurse) {
			let fach : GostFach | null = pInput.faecherManager().get(gKurs.fach_id);
			if (fach === null) 
				throw new DeveloperNotificationException("GostBlockungKurs (" + gKurs.id + ") die Fach-ID ist im Manager unbekannt!")
			let kursart : GostKursart | null = GostKursart.fromIDorNull(gKurs.kursart);
			if (kursart === null) 
				throw new DeveloperNotificationException("GostBlockungKurs (" + gKurs.id + ") die Kursart-ID ist bei GostKursart unbekannt!")
			let kursartMap : HashMap<Number, KursblockungDynFachart> | null = this.fachartMap.get(fach.id);
			if (kursartMap === null) {
				kursartMap = new HashMap();
				this.fachartMap.put(fach.id, kursartMap);
			}
			let dynFachart : KursblockungDynFachart | null = kursartMap.get(kursart.id);
			if (dynFachart === null) {
				dynFachart = new KursblockungDynFachart(this._random, nFacharten, fach, kursart, this.statistik);
				kursartMap.put(kursart.id, dynFachart);
				nFacharten++;
			}
			dynFachart.aktionMaxKurseErhoehen();
		}
		for (let iFachwahl of pInput.daten().fachwahlen) {
			let fach : GostFach | null = pInput.faecherManager().get(iFachwahl.fachID);
			if (fach === null) 
				throw new DeveloperNotificationException("GostFachwahl: Die Fach-ID (" + iFachwahl.fachID + ") ist im Manager unbekannt!")
			let kursart : GostKursart | null = GostKursart.fromIDorNull(iFachwahl.kursartID);
			if (kursart === null) 
				throw new DeveloperNotificationException("GostFachwahl: Die Kursart-ID (" + iFachwahl.kursartID + ") ist bei GostKursart unbekannt!")
			let schuelerID : number = iFachwahl.schuelerID;
			if (schuelerID < 0) 
				throw new DeveloperNotificationException("GostFachwahl.schuelerID < 0")
			let kursartMap : HashMap<Number, KursblockungDynFachart> | null = this.fachartMap.get(fach.id);
			if (kursartMap === null) {
				kursartMap = new HashMap();
				this.fachartMap.put(fach.id, kursartMap);
			}
			let dynFachart : KursblockungDynFachart | null = kursartMap.get(kursart.id);
			if (dynFachart === null) {
				dynFachart = new KursblockungDynFachart(this._random, nFacharten, fach, kursart, this.statistik);
				kursartMap.put(kursart.id, dynFachart);
				nFacharten++;
			}
			dynFachart.aktionMaxSchuelerErhoehen();
		}
		if (nFacharten <= 0) 
			throw new DeveloperNotificationException("Die Blockung hat keine Facharten/Fachwahlen (Fach + Kursart).")
		this.fachartArr = Array(nFacharten).fill(null);
		for (let kursartMap of this.fachartMap.values()) 
			for (let fachart of kursartMap.values()) 
				this.fachartArr[fachart.gibNr()] = fachart;
		let kursSumme : number = 0;
		for (let i : number = 0; i < this.fachartArr.length; i++)
			kursSumme += this.fachartArr[i].gibKurseMax();
		if (kursSumme !== nKurse) 
			throw new DeveloperNotificationException("Die Summe aller auf die Facharten verteilten Kurse ist ungleich der Gesamtkursanzahl.")
	}

	private schritt04FehlerBeiSchuelerErstellung(pInput : GostBlockungsdatenManager) : void {
		let setSchueler : HashSet<Number> = new HashSet();
		for (let fachwahl of pInput.daten().fachwahlen) 
			setSchueler.add(fachwahl.schuelerID);
		let nSchueler : number = setSchueler.size();
		let nSchienen : number = pInput.getSchienenAnzahl();
		let nKurse : number = pInput.getKursAnzahl();
		let i : number = 0;
		this.schuelerArr = Array(nSchueler).fill(null);
		for (let schuelerID of setSchueler) {
			let sID : number = schuelerID.valueOf();
			let schueler : KursblockungDynSchueler = new KursblockungDynSchueler(this._random, sID, this.statistik, nSchienen, nKurse);
			this.schuelerArr[i] = schueler;
			this.schuelerMap.put(schuelerID, schueler);
			i++;
		}
	}

	private schritt05FehlerBeiSchuelerFachwahlenErstellung(pInput : GostBlockungsdatenManager, susArr : Array<KursblockungDynSchueler>) : void {
		let mapSchuelerFA : HashMap<KursblockungDynSchueler, LinkedCollection<KursblockungDynFachart>> = new HashMap();
		for (let i : number = 0; i < susArr.length; i++)
			mapSchuelerFA.put(susArr[i], new LinkedCollection());
		for (let iFachwahl of pInput.daten().fachwahlen) {
			let schueler : KursblockungDynSchueler | null = this.schuelerMap.get(iFachwahl.schuelerID);
			if (schueler === null) 
				throw new DeveloperNotificationException("GostFachwahl.schueler --> KursblockungDynSchueler (mapping fehlt)!")
			let dynFacharten : LinkedCollection<KursblockungDynFachart> | null = mapSchuelerFA.get(schueler);
			if (dynFacharten === null) 
				throw new DeveloperNotificationException("dynFacharten == null")
			let dynFachart : KursblockungDynFachart = this.gibFachart(iFachwahl.fachID, iFachwahl.kursartID);
			dynFacharten.addLast(dynFachart);
		}
		for (let schueler of susArr) {
			let listFA : LinkedCollection<KursblockungDynFachart> | null = mapSchuelerFA.get(schueler);
			if (listFA === null) 
				throw new DeveloperNotificationException("mapSchuelerFA.get(schueler) == null (mapping fehlt)!")
			let arrFA : Array<KursblockungDynFachart> = listFA.toArray(Array(0).fill(null));
			schueler.aktionSetzeFachartenUndIDs(arrFA);
		}
	}

	private schritt06FehlerBeiStatistikErstellung(fachartArr : Array<KursblockungDynFachart>, susArr : Array<KursblockungDynSchueler>, pInput : GostBlockungsdatenManager) : void {
		let nFacharten : number = fachartArr.length;
		let bewertungMatrixFachart : Array<Array<number>> = [...Array(nFacharten)].map(e => Array(nFacharten).fill(0));
		for (let i : number = 0; i < susArr.length; i++){
			let fa : Array<KursblockungDynFachart> = susArr[i].gibFacharten();
			for (let i1 : number = 0; i1 < fa.length; i1++){
				let nr1 : number = fa[i1].gibNr();
				for (let i2 : number = i1 + 1; i2 < fa.length; i2++){
					let nr2 : number = fa[i2].gibNr();
					bewertungMatrixFachart[nr1][nr2]++;
					bewertungMatrixFachart[nr2][nr1]++;
				}
			}
		}
		for (let i1 : number = 0; i1 < nFacharten; i1++){
			let kursAnz1 : number = fachartArr[i1].gibKurseMax();
			let nr1 : number = fachartArr[i1].gibNr();
			for (let i2 : number = 0; i2 < nFacharten; i2++){
				let kursAnz2 : number = fachartArr[i2].gibKurseMax();
				let nr2 : number = fachartArr[i2].gibNr();
				if ((kursAnz1 === 0) || (kursAnz2 === 0)) {
					bewertungMatrixFachart[nr1][nr2] = 0;
					continue;
				}
				let nenner : number = (kursAnz1 + kursAnz2 - 2);
				let faktor : number = nenner === 0 ? 1000000 : (Math.trunc(100 / nenner));
				bewertungMatrixFachart[nr1][nr2] *= faktor;
			}
			bewertungMatrixFachart[nr1][nr1] += 10000000;
		}
		this.statistik.aktionInitialisiere(bewertungMatrixFachart, susArr.length, fachartArr.length, pInput.getKursAnzahl());
	}

	private schritt07FehlerBeiSchienenErzeugung(pSchienen : number) : void {
		this.schienenArr = Array(pSchienen).fill(null);
		for (let nr : number = 0; nr < pSchienen; nr++)
			this.schienenArr[nr] = new KursblockungDynSchiene(this.logger, nr, this.statistik);
	}

	private schritt08FehlerBeiKursErstellung(pInput : GostBlockungsdatenManager) : void {
		let nKurse : number = pInput.getKursAnzahl();
		let nSchienen : number = pInput.getSchienenAnzahl();
		let mapKursSchieneFrei : HashMap<Number, LinkedCollection<KursblockungDynSchiene>> = new HashMap();
		let mapKursSchieneLage : HashMap<Number, LinkedCollection<KursblockungDynSchiene>> = new HashMap();
		this.kursArr = Array(nKurse).fill(null);
		let i : number = 0;
		for (let kurs of pInput.daten().kurse) {
			let schieneLage : LinkedCollection<KursblockungDynSchiene> = new LinkedCollection();
			mapKursSchieneLage.put(kurs.id, schieneLage);
			let schieneFrei : LinkedCollection<KursblockungDynSchiene> = new LinkedCollection();
			mapKursSchieneFrei.put(kurs.id, schieneFrei);
			let perm : Array<number> = KursblockungStatic.gibPermutation(this._random, nSchienen);
			for (let j : number = 0; j < nSchienen; j++)
				schieneFrei.addLast(this.schienenArr[perm[j]]);
			let regelnTyp1 : LinkedCollection<GostBlockungRegel> | null = this.regelMap.get(GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS);
			if (regelnTyp1 !== null) 
				for (let regel1 of regelnTyp1) 
					if (kurs.kursart === regel1.parameter.get(0)) {
						let von : number = regel1.parameter.get(1).valueOf();
						let bis : number = regel1.parameter.get(2).valueOf();
						for (let schiene : number = von; schiene <= bis; schiene++)
							schieneFrei.remove(this.schienenArr[schiene - 1]);
					}
			let regelnTyp6 : LinkedCollection<GostBlockungRegel> | null = this.regelMap.get(GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS);
			if (regelnTyp6 !== null) 
				for (let regel6 of regelnTyp6) {
					let von : number = regel6.parameter.get(1).valueOf();
					let bis : number = regel6.parameter.get(2).valueOf();
					for (let schiene : number = 1; schiene <= this.schienenArr.length; schiene++){
						let innerhalb : boolean = (von <= schiene) && (schiene <= bis);
						let gleicheArt : boolean = kurs.kursart === regel6.parameter.get(0);
						if (innerhalb !== gleicheArt) 
							schieneFrei.remove(this.schienenArr[schiene - 1]);
					}
				}
			let regelnTyp3 : LinkedCollection<GostBlockungRegel> | null = this.regelMap.get(GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE);
			if (regelnTyp3 !== null) 
				for (let regel3 of regelnTyp3) 
					if (kurs.id === regel3.parameter.get(0)) {
						let schiene : number = regel3.parameter.get(1).valueOf();
						schieneFrei.remove(this.schienenArr[schiene - 1]);
					}
			let regelnTyp2 : LinkedCollection<GostBlockungRegel> | null = this.regelMap.get(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE);
			if (regelnTyp2 !== null) 
				for (let regel2 of regelnTyp2) 
					if (kurs.id === regel2.parameter.get(0)) {
						let schiene : number = regel2.parameter.get(1).valueOf();
						let dynSchiene : KursblockungDynSchiene = this.schienenArr[schiene - 1];
						if (schieneLage.contains(dynSchiene)) 
							continue;
						if (!schieneFrei.contains(dynSchiene)) 
							throw new UserNotificationException("Die Regel \'KURS_FIXIERE_IN_SCHIENE\' will Kurs (id=" + kurs.id + ") in Schiene (" + schiene + ") fixieren, aber die Schiene wurde bereits gesperrt!")
						schieneFrei.remove(dynSchiene);
						schieneLage.addLast(dynSchiene);
					}
			let schienen : number = kurs.anzahlSchienen;
			if (schienen <= 0) 
				throw new DeveloperNotificationException("Kurs (" + kurs.id + ") belegt nur " + schienen + " Schienen, das ist zu wenig.")
			if (schienen > this.schienenArr.length) 
				throw new DeveloperNotificationException("Es gibt " + this.schienenArr.length + " Schienen, aber der Kurs (" + kurs.id + ") möchte " + schienen + " Schienen belegen.")
			let pSchienenLageFixiert : number = schieneLage.size();
			if (pSchienenLageFixiert > schienen) 
				throw new DeveloperNotificationException("Kurs (" + kurs.id + ") fixert " + pSchienenLageFixiert + " Schienen, das ist mehr als seine Schienenanzahl " + schienen + ".")
			while (schieneLage.size() < schienen) {
				if (schieneFrei.isEmpty()) 
					throw new UserNotificationException("Der Kurs (" + kurs.id + ") hat zu viele Schienen gesperrt, so dass seine Schienenanzahl nicht erfüllt werden kann!")
				schieneLage.addLast(schieneFrei.pollFirst());
			}
			let pSchienenLage : Array<KursblockungDynSchiene> = schieneLage.toArray(Array(0).fill(null));
			let pSchienenFrei : Array<KursblockungDynSchiene> = schieneFrei.toArray(Array(0).fill(null));
			let dynFachart : KursblockungDynFachart = this.gibFachart(kurs.fach_id, kurs.kursart);
			let dynKurs : KursblockungDynKurs = new KursblockungDynKurs(this._random, pSchienenLage, pSchienenLageFixiert, pSchienenFrei, kurs.id, dynFachart, this.logger, i);
			this.kursArr[i] = dynKurs;
			this.kursMap.put(kurs.id, dynKurs);
			i++;
		}
	}

	private schritt09FehlerBeiKursFreiErstellung() : void {
		let nKursFrei : number = 0;
		for (let kurs of this.kursArr) 
			if (kurs.gibHatFreiheitsgrade()) 
				nKursFrei++;
		this.kursArrFrei = Array(nKursFrei).fill(null);
		for (let i : number = 0, j : number = 0; i < this.kursArr.length; i++)
			if (this.kursArr[i].gibHatFreiheitsgrade()) {
				this.kursArrFrei[j] = this.kursArr[i];
				j++;
			}
	}

	private schritt10FehlerBeiFachartKursArrayErstellung() : void {
		let nFacharten : number = this.fachartArr.length;
		let mapFachartList : HashMap<Number, LinkedCollection<KursblockungDynKurs>> = new HashMap();
		for (let i : number = 0; i < nFacharten; i++)
			mapFachartList.put(i, new LinkedCollection());
		for (let kurs of this.kursArr) {
			let fachartNr : number = kurs.gibFachart().gibNr();
			let fachartKurse : LinkedCollection<KursblockungDynKurs> | null = mapFachartList.get(fachartNr);
			if (fachartKurse === null) 
				throw new DeveloperNotificationException("mapFachartList.get(fachartNr) == null")
			fachartKurse.addLast(kurs);
		}
		for (let nr : number = 0; nr < nFacharten; nr++){
			let list : LinkedCollection<KursblockungDynKurs> | null = mapFachartList.get(nr);
			if (list === null) 
				throw new DeveloperNotificationException("mapFachartList.get(nr) == null")
			let kursArr : Array<KursblockungDynKurs> = list.toArray(Array(0).fill(null));
			this.fachartArr[nr].aktionSetKurse(kursArr);
		}
	}

	private schritt11FehlerBeiRegel_4_oder_5() : void {
		let regelnTyp4 : LinkedCollection<GostBlockungRegel> | null = this.regelMap.get(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS);
		if (regelnTyp4 !== null) 
			for (let regel4 of regelnTyp4) {
				let schuelerID : number = regel4.parameter.get(0).valueOf();
				let kursID : number = regel4.parameter.get(1).valueOf();
				let schueler : KursblockungDynSchueler = this.gibSchueler(schuelerID);
				let fixierterKurs : KursblockungDynKurs = this.gibKurs(kursID);
				for (let kurs of fixierterKurs.gibFachart().gibKurse()) 
					if (kurs as unknown !== fixierterKurs as unknown) 
						schueler.aktionSetzeKursSperrung(kurs.gibInternalID());
			}
		let regelnTyp5 : LinkedCollection<GostBlockungRegel> | null = this.regelMap.get(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS);
		if (regelnTyp5 !== null) 
			for (let regel5 of regelnTyp5) {
				let schuelerID : number = regel5.parameter.get(0).valueOf();
				let kursID : number = regel5.parameter.get(1).valueOf();
				let schueler : KursblockungDynSchueler = this.gibSchueler(schuelerID);
				let verbotenerKurs : KursblockungDynKurs = this.gibKurs(kursID);
				schueler.aktionSetzeKursSperrung(verbotenerKurs.gibInternalID());
			}
	}

	private schritt12FehlerBeiRegel_7_oder_8() : void {
		let regelnTyp7 : LinkedCollection<GostBlockungRegel> | null = this.regelMap.get(GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS);
		if (regelnTyp7 !== null) 
			for (let regel7 of regelnTyp7) {
				let kursID1 : number = regel7.parameter.get(0).valueOf();
				let kursID2 : number = regel7.parameter.get(1).valueOf();
				let kurs1 : KursblockungDynKurs = this.gibKurs(kursID1);
				let kurs2 : KursblockungDynKurs = this.gibKurs(kursID2);
				this.statistik.regelHinzufuegenKursVerbieteMitKurs(kurs1, kurs2);
			}
		let regelnTyp8 : LinkedCollection<GostBlockungRegel> | null = this.regelMap.get(GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS);
		if (regelnTyp8 !== null) 
			for (let regel8 of regelnTyp8) {
				let kursID1 : number = regel8.parameter.get(0).valueOf();
				let kursID2 : number = regel8.parameter.get(1).valueOf();
				let kurs1 : KursblockungDynKurs = this.gibKurs(kursID1);
				let kurs2 : KursblockungDynKurs = this.gibKurs(kursID2);
				this.statistik.regelHinzufuegenKursZusammenMitKurs(kurs1, kurs2);
			}
	}

	private schritt13FehlerBeiRegel_9(pInput : GostBlockungsdatenManager) : void {
		let regelnTyp9 : LinkedCollection<GostBlockungRegel> | null = this.regelMap.get(GostKursblockungRegelTyp.LEHRKRAFT_BEACHTEN);
		if (regelnTyp9 !== null) {
			let vKurseMitLehrkraft : Vector<GostBlockungKurs> = new Vector();
			for (let gKurs of pInput.daten().kurse) 
				if (gKurs.lehrer.isEmpty() === false) 
					vKurseMitLehrkraft.add(gKurs);
			for (let regel9 of regelnTyp9) {
				let externBeachten : boolean = regel9.parameter.get(0) === 1;
				for (let gKurs1 of vKurseMitLehrkraft) 
					for (let gKurs2 of vKurseMitLehrkraft) 
						if (gKurs1.id < gKurs2.id) 
							for (let gLehr1 of gKurs1.lehrer) 
								for (let gLehr2 of gKurs2.lehrer) 
									if (gLehr1.id === gLehr2.id) 
										if ((externBeachten) || (!gLehr1.istExtern)) {
											let kurs1 : KursblockungDynKurs = this.gibKurs(gKurs1.id);
											let kurs2 : KursblockungDynKurs = this.gibKurs(gKurs2.id);
											this.statistik.regelHinzufuegenKursVerbieteMitKurs(kurs1, kurs2);
										}
			}
		}
		let regelnTyp10 : LinkedCollection<GostBlockungRegel> | null = this.regelMap.get(GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN);
		if (regelnTyp10 !== null) {
			let vKurseMitLehrkraft : Vector<GostBlockungKurs> = new Vector();
			for (let gKurs of pInput.daten().kurse) 
				if (gKurs.lehrer.isEmpty() === false) 
					vKurseMitLehrkraft.add(gKurs);
			for (let regel10 of regelnTyp10) 
				for (let gKurs1 of vKurseMitLehrkraft) 
					for (let gKurs2 of vKurseMitLehrkraft) 
						if (gKurs1.id < gKurs2.id) 
							for (let gLehr1 of gKurs1.lehrer) 
								for (let gLehr2 of gKurs2.lehrer) 
									if (gLehr1.id === gLehr2.id) {
										let kurs1 : KursblockungDynKurs = this.gibKurs(gKurs1.id);
										let kurs2 : KursblockungDynKurs = this.gibKurs(gKurs2.id);
										this.statistik.regelHinzufuegenKursVerbieteMitKurs(kurs1, kurs2);
									}
		}
	}

	private gibFachart(pFachID : number, pKursart : number) : KursblockungDynFachart {
		let kursartMap : HashMap<Number, KursblockungDynFachart> | null = this.fachartMap.get(pFachID);
		if (kursartMap === null) 
			throw new DeveloperNotificationException("gibFachart(" + pFachID + ", " + pKursart + ") schlug fehl (Fach)!")
		let dynFachart : KursblockungDynFachart | null = kursartMap.get(pKursart);
		if (dynFachart === null) 
			throw new DeveloperNotificationException("gibFachart(" + pFachID + ", " + pKursart + ") schlug fehl (Kursart)!")
		return dynFachart;
	}

	private gibSchueler(schuelerID : number) : KursblockungDynSchueler {
		let schueler : KursblockungDynSchueler | null = this.schuelerMap.get(schuelerID);
		if (schueler === null) 
			throw new DeveloperNotificationException("schuelerMap.get(" + schuelerID + ") --> kein Mapping!")
		return schueler;
	}

	private gibKurs(kursID : number) : KursblockungDynKurs {
		let kurs : KursblockungDynKurs | null = this.kursMap.get(kursID);
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
		return this.logger;
	}

	/**
	 * Liefert das Statistik-Objekt (für Anfragen zu Nichtwahlen, Kursdifferenzen, etc.).
	 * 
	 * @return Das Statistik-Objekt (für Anfragen zu Nichtwahlen, Kursdifferenzen, etc.).
	 */
	gibStatistik() : KursblockungDynStatistik {
		return this.statistik;
	}

	/**
	 * Liefert die maximale Blockungszeit in Millisekunden. Entweder handelt es sich um einen Standardwert oder der Wert
	 * wurde im Konstruktor als Regel übergeben.
	 * 
	 * @return Liefert die maximale Blockungszeit in Millisekunden.
	 */
	gibBlockungszeitMillis() : number {
		return this.maxTimeMillis;
	}

	/**
	 * Liefert die maximal erlaubte Anzahl an Schienen. Entweder handelt es sich um einen Standardwert oder der Wert
	 * wurde im Konstruktor als Regel übergeben.
	 * 
	 * @return Liefert die maximal erlaubte Anzahl an Schienen.
	 */
	gibSchienenAnzahl() : number {
		return this.schienenArr.length;
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
		let out : GostBlockungsergebnisManager = new GostBlockungsergebnisManager(pDataManager, pErgebnisID);
		for (let dynKurs of this.kursArr) 
			for (let schienenNr of dynKurs.gibSchienenLage()) 
				out.setKursSchienenNr(dynKurs.gibDatenbankID(), schienenNr + 1);
		for (let dynSchueler of this.schuelerArr) 
			for (let kurs of dynSchueler.gibKurswahlen()) 
				if (kurs !== null) 
					out.setSchuelerKurs(dynSchueler.gibDatenbankID(), kurs.gibDatenbankID(), true);
		for (let gRegel of pDataManager.getMengeOfRegeln()) 
			if (gRegel.typ === GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ) {
				let schuelerID : number = gRegel.parameter.get(0).valueOf();
				let kursID : number = gRegel.parameter.get(1).valueOf();
				if (out.getOfSchuelerOfKursIstZugeordnet(schuelerID, kursID) === false) 
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
		return this.kursArr;
	}

	/**
	 * Liefert alle Kurse deren Lage nicht komplett fixiert ist.
	 * 
	 * @return Array aller Kurse, deren Schienenlage noch veränderbar ist.
	 */
	gibKurseDieFreiSind() : Array<KursblockungDynKurs> {
		return this.kursArrFrei;
	}

	/**
	 * Liefert die Anzahl alle Kurse deren Lage nicht komplett fixiert ist.
	 * 
	 * @return Anzahl aller Kurse, deren Schienenlage noch veränderbar ist.
	 */
	gibKurseDieFreiSindAnzahl() : number {
		return this.kursArrFrei.length;
	}

	/**
	 * Liefert einen Long-Wert, der einer Bewertung der Fachwahlmatrix entspricht. Je kleiner der Wert, desto besser ist
	 * die Bewertung.
	 * 
	 * @return Long-Wert, der einer Bewertung der Fachwahlmatrix entspricht.
	 */
	gibBewertungFachartPaar() : number {
		return this.statistik.gibBewertungFachartPaar();
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
			let list : LinkedCollection<KursblockungDynSchueler> = new LinkedCollection();
			for (let schueler of this.schuelerArr) {
				if (schueler.gibHatMultikurs()) {
					list.addLast(schueler);
				}
			}
			let temp : Array<KursblockungDynSchueler> = Array(list.size()).fill(null);
			for (let i : number = 0; i < temp.length; i++){
				temp[i] = list.removeFirst();
			}
			return temp;
		}
		return this.schuelerArr;
	}

	/**
	 * Liefert ein Array aller Schülerinnen und Schüler.
	 * 
	 * @return Ein Array aller Schülerinnen und Schüler.
	 */
	gibSchuelerArrayAlle() : Array<KursblockungDynSchueler> {
		return this.schuelerArr;
	}

	/**
	 * Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen) des Zustandes S sich
	 * verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 * 
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen) des Zustandes S sich
	 *         verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 */
	gibBewertungJetztBesserAlsS() : number {
		return this.statistik.gibBewertungZustandS_NW_KD();
	}

	/**
	 * Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Nichtwahlen, Kursdiffenzen,
	 * Fachwahlmatrix) des Zustandes-K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 * 
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Nichtwahlen, Kursdiffenzen, Fachwahlmatrix) des
	 *         Zustandes-K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 */
	gibCompareZustandK_NW_KD_FW() : number {
		return this.statistik.gibCompareZustandK_NW_KD_FW();
	}

	/**
	 * Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Nichtwahlen, Kursdiffenzen,
	 * Fachwahlmatrix) des Zustandes-G sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 * 
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Nichtwahlen, Kursdiffenzen, Fachwahlmatrix) des
	 *         Zustandes-G sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 */
	gibCompareZustandG_NW_KD_FW() : number {
		return this.statistik.gibCompareZustandG_NW_KD_FW();
	}

	/**
	 * Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Fachwahlmatrix, Nichtwahlen,
	 * Kursdiffenzen) des Zustandes K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 * 
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Fachwahlmatrix, Nichtwahlen, Kursdiffenzen) des
	 *         Zustandes K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 */
	gibBewertungK_FW_NW_KD_JetztBesser() : number {
		return this.statistik.gibCompareZustandK_FW_NW_KD();
	}

	/**
	 *Entfernt alle SuS aus ihren Kursen. 
	 */
	aktionSchuelerAusAllenKursenEntfernen() : void {
		for (let i : number = 0; i < this.schuelerArr.length; i++){
			this.schuelerArr[i].aktionKurseAlleEntfernen();
		}
	}

	/**
	 *Debug Ausgaben. Nur für Testzwecke. 
	 */
	debug() : void {
		console.log(JSON.stringify("########## Schienen ##########"));
		for (let i : number = 0; i < this.schienenArr.length; i++){
			console.log(JSON.stringify("Schiene " + (i + 1)));
			this.schienenArr[i].debug(false);
		}
		console.log(JSON.stringify("########## Facharten ##########"));
		for (let i : number = 0; i < this.fachartArr.length; i++){
			console.log(JSON.stringify("Fachart " + this.fachartArr[i] + " --> " + this.fachartArr[i].gibKursdifferenz()));
			this.fachartArr[i].debug(this.schuelerArr);
		}
		this.statistik.debug("");
	}

	/**
	 *Speichert die Bewertung, die Kursverteilung und die Schülerverteilung im Zustand S. 
	 */
	aktionZustandSpeichernS() : void {
		this.statistik.aktionBewertungSpeichernS();
		for (let kurs of this.kursArr) {
			kurs.aktionZustandSpeichernS();
		}
		for (let schueler of this.schuelerArr) {
			schueler.aktionZustandSpeichernS();
		}
	}

	/**
	 *Speichert die Bewertung, die Kursverteilung und die Schülerverteilung im Zustand K. 
	 */
	aktionZustandSpeichernK() : void {
		this.statistik.aktionBewertungSpeichernK();
		for (let kurs of this.kursArr) {
			kurs.aktionZustandSpeichernK();
		}
		for (let schueler of this.schuelerArr) {
			schueler.aktionZustandSpeichernK();
		}
	}

	/**
	 *Speichert die Bewertung, die Kursverteilung und die Schülerverteilung im Zustand G. 
	 */
	aktionZustandSpeichernG() : void {
		this.statistik.aktionBewertungSpeichernG();
		for (let kurs of this.kursArr) {
			kurs.aktionZustandSpeichernG();
		}
		for (let schueler of this.schuelerArr) {
			schueler.aktionZustandSpeichernG();
		}
	}

	/**
	 *Lädt den zuvor gespeicherten Zustand S (Kursverteilung und Schülerverteilung). 
	 */
	aktionZustandLadenS() : void {
		for (let schueler of this.schuelerArr) {
			schueler.aktionKurseAlleEntfernen();
		}
		for (let kurs of this.kursArr) {
			kurs.aktionZustandLadenS();
		}
		for (let schueler of this.schuelerArr) {
			schueler.aktionZustandLadenS();
		}
	}

	/**
	 *Lädt den zuvor gespeicherten Zustand K (Kursverteilung und Schülerverteilung). 
	 */
	aktionZustandLadenK() : void {
		for (let schueler of this.schuelerArr) {
			schueler.aktionKurseAlleEntfernen();
		}
		for (let kurs of this.kursArr) {
			kurs.aktionZustandLadenK();
		}
		for (let schueler of this.schuelerArr) {
			schueler.aktionZustandLadenK();
		}
	}

	/**
	 *Lädt den zuvor gespeicherten Zustand G (Kursverteilung und Schülerverteilung). 
	 */
	aktionZustandLadenG() : void {
		for (let schueler of this.schuelerArr) {
			schueler.aktionKurseAlleEntfernen();
		}
		for (let kurs of this.kursArr) {
			kurs.aktionZustandLadenG();
		}
		for (let schueler of this.schuelerArr) {
			schueler.aktionZustandLadenG();
		}
	}

	/**
	 *Lädt den zuvor gespeicherten Zustand K (nur Kursverteilung, ohne Schülerverteilung). 
	 */
	aktionZustandLadenKohneSuS() : void {
		for (let schueler of this.schuelerArr) {
			schueler.aktionKurseAlleEntfernen();
		}
		for (let kurs of this.kursArr) {
			kurs.aktionZustandLadenK();
		}
	}

	/**
	 *Verteilt alle Kurse auf ihre Schienen zufällig. Kurse die keinen Freiheitsgrad haben, werden dabei ignoriert. 
	 */
	aktionKurseFreieZufaelligVerteilen() : void {
		for (let kurs of this.kursArrFrei) 
			kurs.aktionZufaelligVerteilen();
	}

	/**
	 *Verteilt einen Kurs zufällig. Kurse die keinen Freiheitsgrad haben, werden dabei ignoriert. 
	 */
	aktionKursVerteilenEinenZufaelligenFreien() : void {
		if (this.kursArrFrei.length === 0) 
			return;
		let index : number = this._random.nextInt(this.kursArrFrei.length);
		let kurs : KursblockungDynKurs = this.kursArrFrei[index];
		kurs.aktionZufaelligVerteilen();
	}

	/**
	 * Verteilt einen Kurs zufällig. Kurse die keinen Freiheitsgrad haben, werden dabei ignoriert. Multikurse werden
	 * ebenso ignoriert.
	 */
	aktionKursFreienEinenZufaelligVerteilenAberNichtMultikurse() : void {
		if (this.kursArrFrei.length === 0) {
			return;
		}
		let perm : Array<number> | null = KursblockungStatic.gibPermutation(this._random, this.kursArrFrei.length);
		for (let index of perm) {
			let kurs : KursblockungDynKurs = this.kursArrFrei[index];
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
		return this.statistik.gibBewertungZustandS_NW_KD();
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
		let perm : Array<number> = KursblockungStatic.gibPermutation(this._random, this.schuelerArr.length);
		for (let pSchueler : number = 0; pSchueler < perm.length; pSchueler++){
			let schueler : KursblockungDynSchueler | null = this.schuelerArr[perm[pSchueler]];
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
		let perm : Array<number> = KursblockungStatic.gibPermutation(this._random, this.schuelerArr.length);
		for (let p : number = 0; p < perm.length; p++){
			let i : number = perm[p];
			let schueler : KursblockungDynSchueler | null = this.schuelerArr[i];
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
		let perm : Array<number> = KursblockungStatic.gibPermutation(this._random, this.schuelerArr.length);
		for (let p : number = 0; p < perm.length; p++){
			let i : number = perm[p];
			let schueler : KursblockungDynSchueler | null = this.schuelerArr[i];
			schueler.aktionKurseVerteilenNurMultikurseZufaellig();
			schueler.aktionKurseVerteilenNurFachartenMitEinemKurs();
			schueler.aktionKurseVerteilenMitBipartiteMatchingGewichtetem();
		}
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.kursblockung.KursblockungDynDaten'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynDaten(obj : unknown) : KursblockungDynDaten {
	return obj as KursblockungDynDaten;
}
