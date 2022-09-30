import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';
import { KursblockungDynFachart, cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynFachart } from '../../core/kursblockung/KursblockungDynFachart';
import { KursblockungInput, cast_de_nrw_schule_svws_core_data_kursblockung_KursblockungInput } from '../../core/data/kursblockung/KursblockungInput';
import { KursblockungStatic, cast_de_nrw_schule_svws_core_kursblockung_KursblockungStatic } from '../../core/kursblockung/KursblockungStatic';
import { HashMap, cast_java_util_HashMap } from '../../java/util/HashMap';
import { KursblockungInputRegel, cast_de_nrw_schule_svws_core_data_kursblockung_KursblockungInputRegel } from '../../core/data/kursblockung/KursblockungInputRegel';
import { KursblockungDynSchiene, cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynSchiene } from '../../core/kursblockung/KursblockungDynSchiene';
import { KursblockungDynKurs, cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynKurs } from '../../core/kursblockung/KursblockungDynKurs';
import { KursblockungInputFach, cast_de_nrw_schule_svws_core_data_kursblockung_KursblockungInputFach } from '../../core/data/kursblockung/KursblockungInputFach';
import { JavaString, cast_java_lang_String } from '../../java/lang/JavaString';
import { Logger, cast_de_nrw_schule_svws_logger_Logger } from '../../logger/Logger';
import { KursblockungException, cast_de_nrw_schule_svws_core_kursblockung_KursblockungException } from '../../core/kursblockung/KursblockungException';
import { LogLevel, cast_de_nrw_schule_svws_logger_LogLevel } from '../../logger/LogLevel';
import { System, cast_java_lang_System } from '../../java/lang/System';
import { GostKursblockungRegelTyp, cast_de_nrw_schule_svws_core_types_kursblockung_GostKursblockungRegelTyp } from '../../core/types/kursblockung/GostKursblockungRegelTyp';
import { Random, cast_java_util_Random } from '../../java/util/Random';
import { NullPointerException, cast_java_lang_NullPointerException } from '../../java/lang/NullPointerException';
import { Vector, cast_java_util_Vector } from '../../java/util/Vector';
import { HashSet, cast_java_util_HashSet } from '../../java/util/HashSet';
import { AVLSet, cast_de_nrw_schule_svws_core_adt_set_AVLSet } from '../../core/adt/set/AVLSet';
import { KursblockungDynStatistik, cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynStatistik } from '../../core/kursblockung/KursblockungDynStatistik';
import { KursblockungInputSchueler, cast_de_nrw_schule_svws_core_data_kursblockung_KursblockungInputSchueler } from '../../core/data/kursblockung/KursblockungInputSchueler';
import { LinkedCollection, cast_de_nrw_schule_svws_core_adt_collection_LinkedCollection } from '../../core/adt/collection/LinkedCollection';
import { KursblockungInputKurs, cast_de_nrw_schule_svws_core_data_kursblockung_KursblockungInputKurs } from '../../core/data/kursblockung/KursblockungInputKurs';
import { JavaInteger, cast_java_lang_Integer } from '../../java/lang/JavaInteger';
import { KursblockungOutput, cast_de_nrw_schule_svws_core_data_kursblockung_KursblockungOutput } from '../../core/data/kursblockung/KursblockungOutput';
import { JavaLong, cast_java_lang_Long } from '../../java/lang/JavaLong';
import { KursblockungInputKursart, cast_de_nrw_schule_svws_core_data_kursblockung_KursblockungInputKursart } from '../../core/data/kursblockung/KursblockungInputKursart';
import { KursblockungInputFachwahl, cast_de_nrw_schule_svws_core_data_kursblockung_KursblockungInputFachwahl } from '../../core/data/kursblockung/KursblockungInputFachwahl';
import { KursblockungDynSchueler, cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynSchueler } from '../../core/kursblockung/KursblockungDynSchueler';

export class KursblockungDynDaten extends JavaObject {

	private readonly _random : Random;

	private readonly logger : Logger;

	private readonly regelMap : HashMap<GostKursblockungRegelTyp, LinkedCollection<KursblockungInputRegel>>;

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
	 *Der Konstruktor der Klasse liest alle Daten von {@link KursblockungInput} ein und baut die relevanten
	 * Datenstrukturen auf.
	 * 
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pLogger Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param pInput  Die Eingabedaten (Schnittstelle zur GUI). 
	 * 
	 */
	public constructor(pRandom : Random, pLogger : Logger, pInput : KursblockungInput) {
		super();
		this._random = pRandom;
		this.logger = pLogger;
		this.regelMap = new HashMap();
		this.maxTimeMillis = pInput.maxTimeMillis;
		this.schienenArr = Array(0).fill(null);
		this.kursArr = Array(0).fill(null);
		this.kursArrFrei = Array(0).fill(null);
		this.kursMap = new HashMap();
		this.fachartArr = Array(0).fill(null);
		this.fachartMap = new HashMap();
		this.schuelerArr = Array(0).fill(null);
		this.schuelerMap = new HashMap();
		this.statistik = new KursblockungDynStatistik();
		if (this.schritt01FehlerBeiReferenzen(pInput)) {
			return;
		}
		if (this.schritt02FehlerBeiRegelGruppierung(pInput.regeln)) {
			return;
		}
		if (this.schritt03FehlerBeiFachartenErstellung(pInput)) {
			return;
		}
		if (this.schritt04FehlerBeiSchuelerErstellung(pInput)) {
			return;
		}
		if (this.schritt05FehlerBeiSchuelerFachwahlenErstellung(pInput.fachwahlen, this.schuelerArr)) {
			return;
		}
		if (this.schritt06FehlerBeiStatistikErstellung(this.fachartArr, this.schuelerArr)) {
			return;
		}
		if (this.schritt07FehlerBeiSchienenErzeugung(pInput.maxSchienen)) {
			return;
		}
		if (this.schritt08FehlerBeiKursErstellung(pInput)) {
			return;
		}
		if (this.schritt09FehlerBeiKursFreiErstellung(pInput)) {
			return;
		}
		if (this.schritt10FehlerBeiFachartKursArrayErstellung(pInput)) {
			return;
		}
		if (this.schritt11FehlerBeiRegel_4_oder_5()) {
			return;
		}
		this.aktionZustandSpeichernS();
		this.aktionZustandSpeichernK();
		this.aktionZustandSpeichernG();
	}

	/**
	 *Überprüft alle Referenzen in {@link KursblockungInput} und auch die referentielle Integrität.
	 * 
	 * @param  pInput Das {@link KursblockungInput}-Objekt von der GUI.
	 * @return        {@code true}, falls kein Fehler gefunden wurde. 
	 */
	private schritt01FehlerBeiReferenzen(pInput : KursblockungInput) : boolean {
		if (pInput === null) 
			throw this.fehler("Referenz \'KursblockungInput\' ist NULL!")
		if (pInput.input < 0) 
			throw this.fehler("KursblockungInput.input = " + pInput.input + "< 0, das ist bei einer Datenbank-ID unüblich.")
		if (pInput.fachwahlen === null) 
			throw this.fehler("Referenz \'KursblockungInput.fachwahlen\' ist NULL!")
		if (pInput.fachwahlen.size() === 0) 
			throw this.fehler("Die Blockung hat 0 Fachwahlen!")
		if (pInput.faecher === null) 
			throw this.fehler("Referenz \'KursblockungInput.faecher\' ist NULL!")
		if (pInput.faecher.size() === 0) 
			throw this.fehler("Die Blockung hat 0 Fächer!")
		if (pInput.kursarten === null) 
			throw this.fehler("Referenz \'KursblockungInput.kursarten\' ist NULL!")
		if (pInput.kursarten.size() === 0) 
			throw this.fehler("Die Blockung hat 0 Kursarten!")
		if (pInput.kurse === null) 
			throw this.fehler("Referenz \'KursblockungInput.kurse\' ist NULL!")
		if (pInput.kurse.size() === 0) 
			throw this.fehler("Die Blockung hat 0 Kurse!")
		if (pInput.schueler === null) 
			throw this.fehler("Referenz \'KursblockungInput.schueler\' ist NULL!")
		if (pInput.schueler.size() === 0) 
			throw this.fehler("Die Blockung hat 0 Schüler!")
		if (pInput.regeln === null) 
			throw this.fehler("Referenz \'KursblockungInput.regeln\' ist NULL!")
		let setKursarten : HashSet<Number> = new HashSet();
		for (let i : number = 0; i < pInput.kursarten.size(); i++){
			let iKursart : KursblockungInputKursart | null = pInput.kursarten.get(i);
			if (iKursart === null) 
				throw this.fehler(".kursarten.get(" + i + ") ist NULL!")
			if (iKursart.id < 0) 
				throw this.fehler("Kursart=" + iKursart.representation + " hat unübliche DB-ID=" + iKursart.id + "!")
			setKursarten.add(iKursart.id);
		}
		let setFaecher : HashSet<Number> = new HashSet();
		for (let i : number = 0; i < pInput.faecher.size(); i++){
			let iFach : KursblockungInputFach | null = pInput.faecher.get(i);
			if (iFach === null) 
				throw this.fehler(".faecher.get(" + i + ") ist NULL!")
			if (iFach.id < 0) 
				throw this.fehler("Fach=" + iFach.representation + " hat unübliche DB-ID=" + iFach.id + "!")
			setFaecher.add(iFach.id);
		}
		let setKurse : HashSet<Number> = new HashSet();
		for (let i : number = 0; i < pInput.kurse.size(); i++){
			let iKurs : KursblockungInputKurs | null = pInput.kurse.get(i);
			if (iKurs === null) 
				throw this.fehler(".kurse.get(" + i + ") ist NULL!")
			if (iKurs.id < 0) 
				throw this.fehler("Kurs=" + iKurs.representation + " hat unübliche DB-ID=" + iKurs.id + "!")
			let fachID : number = iKurs.fach;
			if (!setFaecher.contains(fachID)) 
				throw this.fehler("Kurs=" + iKurs.representation + " hat unbekannte Fach-ID (" + fachID + ")!")
			let kursartID : number = iKurs.kursart;
			if (!setKursarten.contains(kursartID)) 
				throw this.fehler("Kurs=" + iKurs.representation + " hat unbekannte Kursart-ID (" + kursartID + ")!")
			setKurse.add(iKurs.id);
		}
		let setSchueler : HashSet<Number> = new HashSet();
		for (let i : number = 0; i < pInput.schueler.size(); i++){
			let iSchueler : KursblockungInputSchueler | null = pInput.schueler.get(i);
			if (iSchueler === null) 
				throw this.fehler(".schueler.get(" + i + ") ist NULL")
			if (iSchueler.id < 0) 
				throw this.fehler("Schüler=" + iSchueler.representation + " hat unübliche DB-ID=" + iSchueler.id + "!")
			setSchueler.add(iSchueler.id);
		}
		for (let i : number = 0; i < pInput.fachwahlen.size(); i++){
			let iFachwahl : KursblockungInputFachwahl | null = pInput.fachwahlen.get(i);
			if (iFachwahl === null) 
				throw this.fehler(".fachwahlen.get(" + i + ") ist NULL!")
			let schuelerID : number = iFachwahl.schueler;
			if (!setSchueler.contains(schuelerID)) 
				throw this.fehler("Fachwahl=" + iFachwahl.representation + " hat unbekannte Schüler-ID=" + schuelerID + "!")
			let fachID : number = iFachwahl.fach;
			if (!setFaecher.contains(fachID)) 
				throw this.fehler("Fachwahl=" + iFachwahl.representation + " hat unbekannte Fach-ID=" + fachID + "!")
			let kursartID : number = iFachwahl.kursart;
			if (!setKursarten.contains(kursartID)) 
				throw this.fehler("Fachwahl=" + iFachwahl.representation + " hat unbekannte Kursart-ID=" + kursartID + "!")
		}
		for (let i : number = 0; i < pInput.regeln.size(); i++){
			let iRegel : KursblockungInputRegel | null = pInput.regeln.get(i);
			if (iRegel === null) 
				throw this.fehler(".regeln.get(" + i + ") ist NULL!")
			if (iRegel.daten === null) 
				throw this.fehler(".regeln.get(" + i + ").daten ist NULL!")
			let gostRegel : GostKursblockungRegelTyp = GostKursblockungRegelTyp.fromTyp(iRegel.typ);
			if (gostRegel as unknown === GostKursblockungRegelTyp.UNDEFINIERT as unknown) 
				throw this.fehler(".regeln.get(" + i + ") hat unbekannte Regel-ID (" + iRegel.typ + ")!")
			if (gostRegel as unknown === GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS as unknown) {
				let length : number = iRegel.daten.length;
				if (length !== 3) 
					throw this.fehler("KURSART_SPERRE_SCHIENEN_VON_BIS daten.length=" + length + ", statt 3!")
				let kursartID : number = iRegel.daten[0].valueOf();
				if (!setKursarten.contains(kursartID)) 
					throw this.fehler("KURSART_SPERRE_SCHIENEN_VON_BIS hat unbekannte Kursart-ID=" + kursartID + "!")
				let von : number = iRegel.daten[1].valueOf() - 1;
				let bis : number = iRegel.daten[2].valueOf() - 1;
				if (!((von >= 0) && (von <= bis) && (bis < pInput.maxSchienen))) 
					throw this.fehler("KURSART_SPERRE_SCHIENEN_VON_BIS (" + kursartID + ", " + (von + 1) + ", " + (bis + 1) + ") ist unlogisch!")
			}
			if (gostRegel as unknown === GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE as unknown) {
				let length : number = iRegel.daten.length;
				if (length !== 2) 
					throw this.fehler("KURS_FIXIERE_IN_SCHIENE daten.length=" + length + ", statt 2!")
				let kursID : number = iRegel.daten[0].valueOf();
				if (!setKurse.contains(kursID)) 
					throw this.fehler("KURS_FIXIERE_IN_SCHIENE hat unbekannte Kurs-ID=" + kursID + "!")
				let schiene : number = iRegel.daten[1].valueOf() - 1;
				if (!((schiene >= 0) && (schiene < pInput.maxSchienen))) 
					throw this.fehler("KURS_FIXIERE_IN_SCHIENE (" + kursID + ", " + (schiene + 1) + ") ist unlogisch!")
			}
			if (gostRegel as unknown === GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE as unknown) {
				let length : number = iRegel.daten.length;
				if (length !== 2) 
					throw this.fehler("KURS_SPERRE_IN_SCHIENE daten.length=" + length + ", statt 2!")
				let kursID : number = iRegel.daten[0].valueOf();
				if (!setKurse.contains(kursID)) 
					throw this.fehler("KURS_SPERRE_IN_SCHIENE hat unbekannte Kurs-ID=" + kursID + "!")
				let schiene : number = iRegel.daten[1].valueOf() - 1;
				if (!((schiene >= 0) && (schiene < pInput.maxSchienen))) 
					throw this.fehler("KURS_SPERRE_IN_SCHIENE (" + kursID + ", " + (schiene + 1) + ") ist unlogisch!")
			}
			if (gostRegel as unknown === GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS as unknown) {
				let length : number = iRegel.daten.length;
				if (length !== 2) 
					throw this.fehler("SCHUELER_FIXIEREN_IN_KURS daten.length=" + length + ", statt 2!")
				let schuelerID : number = iRegel.daten[0].valueOf();
				if (!setSchueler.contains(schuelerID)) 
					throw this.fehler("SCHUELER_FIXIEREN_IN_KURS hat unbekannte Schüler-ID=" + schuelerID + "!")
				let kursID : number = iRegel.daten[1].valueOf();
				if (!setKurse.contains(kursID)) 
					throw this.fehler("SCHUELER_FIXIEREN_IN_KURS hat unbekannte Kurs-ID=" + kursID + "!")
			}
			if (gostRegel as unknown === GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS as unknown) {
				let length : number = iRegel.daten.length;
				if (length !== 2) 
					throw this.fehler("SCHUELER_VERBIETEN_IN_KURS daten.length=" + length + ", statt 2!")
				let schuelerID : number = iRegel.daten[0].valueOf();
				if (!setSchueler.contains(schuelerID)) 
					throw this.fehler("SCHUELER_VERBIETEN_IN_KURS hat unbekannte Schüler-ID=" + schuelerID + "!")
				let kursID : number = iRegel.daten[1].valueOf();
				if (!setKurse.contains(kursID)) 
					throw this.fehler("SCHUELER_VERBIETEN_IN_KURS hat unbekannte Kurs-ID=" + kursID + "!")
			}
			if (gostRegel as unknown === GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS as unknown) {
				let length : number = iRegel.daten.length;
				if (length !== 3) 
					throw this.fehler("KURSART_ALLEIN_IN_SCHIENEN_VON_BIS daten.length=" + length + ", statt 3!")
				let kursartID : number = iRegel.daten[0].valueOf();
				if (!setKursarten.contains(kursartID)) 
					throw this.fehler("KURSART_ALLEIN_IN_SCHIENEN_VON_BIS hat unbekannte Kursart-ID=" + kursartID + "!")
				let von : number = iRegel.daten[1].valueOf() - 1;
				let bis : number = iRegel.daten[2].valueOf() - 1;
				if (!((von >= 0) && (von <= bis) && (bis < pInput.maxSchienen))) 
					throw this.fehler("KURSART_ALLEIN_IN_SCHIENEN_VON_BIS (" + kursartID + ", " + (von + 1) + ", " + (bis + 1) + ") ist unlogisch!")
			}
		}
		return false;
	}

	private schritt02FehlerBeiRegelGruppierung(vRegeln : Vector<KursblockungInputRegel>) : boolean {
		let regelDatabaseIDs : AVLSet<Number | null> | null = new AVLSet();
		for (let i : number = 0; i < vRegeln.size(); i++){
			let regel : KursblockungInputRegel = vRegeln.get(i);
			if (regel.databaseID !== -1) 
				if (regelDatabaseIDs.add(regel.databaseID) === false) 
					throw this.fehler("Zwei Regeln haben dieselbe ID (" + regel.databaseID + ") in der Datenbank!")
			let regelTyp : GostKursblockungRegelTyp = GostKursblockungRegelTyp.fromTyp(regel.typ);
			let list : LinkedCollection<KursblockungInputRegel> | null = this.regelMap.get(regelTyp);
			if (list === null) {
				list = new LinkedCollection();
				this.regelMap.put(regelTyp, list);
			}
			list.addLast(regel);
		}
		return false;
	}

	private schritt03FehlerBeiFachartenErstellung(pInput : KursblockungInput) : boolean {
		let mapFach : HashMap<Number, String> = new HashMap();
		for (let iFach of pInput.faecher) {
			mapFach.put(iFach.id, iFach.representation);
		}
		let mapKursart : HashMap<Number, String> = new HashMap();
		for (let iKursart of pInput.kursarten) {
			mapKursart.put(iKursart.id, iKursart.representation);
		}
		let mapSchueler : HashMap<Number, String> = new HashMap();
		for (let iSchueler of pInput.schueler) {
			mapSchueler.put(iSchueler.id, iSchueler.representation);
		}
		let nFacharten : number = 0;
		let nKurse : number = pInput.kurse.size();
		for (let i : number = 0; i < nKurse; i++){
			let iKurs : KursblockungInputKurs = pInput.kurse.get(i);
			let fachID : number = iKurs.fach;
			let kursartID : number = iKurs.kursart;
			let kursartMap : HashMap<Number, KursblockungDynFachart> | null = this.fachartMap.get(fachID);
			if (kursartMap === null) {
				kursartMap = new HashMap();
				this.fachartMap.put(fachID, kursartMap);
			}
			let dynFachart : KursblockungDynFachart | null = kursartMap.get(kursartID);
			if (dynFachart === null) {
				let strFach : String | null = mapFach.get(fachID);
				let strKursart : String | null = mapKursart.get(kursartID);
				if ((strFach === null) || (strKursart === null)) 
					throw new NullPointerException()
				let representation : String = strFach.valueOf() + ";" + strKursart.valueOf();
				dynFachart = new KursblockungDynFachart(this._random, nFacharten, representation, this.statistik);
				kursartMap.put(kursartID, dynFachart);
				nFacharten++;
			}
			dynFachart.aktionMaxKurseErhoehen();
		}
		for (let i : number = 0; i < pInput.fachwahlen.size(); i++){
			let iFachwahl : KursblockungInputFachwahl = pInput.fachwahlen.get(i);
			let schuelerID : number = iFachwahl.schueler;
			let fachID : number = iFachwahl.fach;
			let kursartID : number = iFachwahl.kursart;
			let kursartMap : HashMap<Number, KursblockungDynFachart> | null = this.fachartMap.get(fachID);
			if (kursartMap === null) {
				kursartMap = new HashMap();
				this.fachartMap.put(fachID, kursartMap);
			}
			let dynFachart : KursblockungDynFachart | null = kursartMap.get(kursartID);
			if (dynFachart === null) {
				let strFach : String | null = mapFach.get(fachID);
				let strKursart : String | null = mapKursart.get(kursartID);
				let strSchueler : String | null = mapSchueler.get(schuelerID);
				if ((strFach === null) || (strKursart === null) || (strSchueler === null)) 
					throw new NullPointerException()
				let representation : String = strFach.valueOf() + ";" + strKursart.valueOf();
				dynFachart = new KursblockungDynFachart(this._random, nFacharten, representation, this.statistik);
				kursartMap.put(kursartID, dynFachart);
				nFacharten++;
				this.logger.logLn(LogLevel.WARNING, "Schüler \'" + strSchueler.valueOf() + "\' wählt \'" + representation.valueOf() + "\', ohne das ein Kurs existiert!");
			}
			dynFachart.aktionMaxSchuelerErhoehen();
		}
		if (nFacharten === 0) {
			this.fehler("Die Blockung hat 0 Facharten.");
			return true;
		}
		this.fachartArr = Array(nFacharten).fill(null);
		for (let map of this.fachartMap.values()) {
			for (let fachart of map.values()) {
				this.fachartArr[fachart.gibNr()] = fachart;
			}
		}
		let kursSumme : number = 0;
		for (let i : number = 0; i < this.fachartArr.length; i++){
			kursSumme += this.fachartArr[i].gibKurseMax();
		}
		if (kursSumme !== nKurse) {
			this.fehler("Summe aller auf die Facharten verteilten Kurse ist ungleich der Gesamtkursanzahl.");
			return true;
		}
		return false;
	}

	private schritt04FehlerBeiSchuelerErstellung(pInput : KursblockungInput) : boolean {
		let vSchueler : Vector<KursblockungInputSchueler> = pInput.schueler;
		let nSchueler : number = vSchueler.size();
		this.schuelerArr = Array(nSchueler).fill(null);
		for (let i : number = 0; i < nSchueler; i++){
			let iSchueler : KursblockungInputSchueler = vSchueler.get(i);
			let schueler : KursblockungDynSchueler = new KursblockungDynSchueler(this._random, iSchueler, this.statistik, pInput.maxSchienen, pInput.kurse.size());
			this.schuelerArr[i] = schueler;
			this.schuelerMap.put(iSchueler.id, schueler);
		}
		return false;
	}

	private schritt05FehlerBeiSchuelerFachwahlenErstellung(vFachwahlen : Vector<KursblockungInputFachwahl>, susArr : Array<KursblockungDynSchueler>) : boolean {
		let mapSchuelerFA : HashMap<KursblockungDynSchueler, LinkedCollection<KursblockungDynFachart>> = new HashMap();
		let mapSchuelerID : HashMap<KursblockungDynSchueler, LinkedCollection<Number>> = new HashMap();
		for (let i : number = 0; i < susArr.length; i++){
			mapSchuelerFA.put(susArr[i], new LinkedCollection());
			mapSchuelerID.put(susArr[i], new LinkedCollection());
		}
		let nFachwahlen : number = vFachwahlen.size();
		for (let i : number = 0; i < nFachwahlen; i++){
			let iFachwahl : KursblockungInputFachwahl = vFachwahlen.get(i);
			let susID : number = iFachwahl.schueler;
			let fachID : number = iFachwahl.fach;
			let kursartID : number = iFachwahl.kursart;
			let schueler : KursblockungDynSchueler | null = this.schuelerMap.get(susID);
			let kursartMap : HashMap<Number, KursblockungDynFachart> | null = this.fachartMap.get(fachID);
			let dynFachart : KursblockungDynFachart | null = kursartMap === null ? null : kursartMap.get(kursartID);
			let dynFacharten : LinkedCollection<KursblockungDynFachart> | null = mapSchuelerFA.get(schueler);
			if (dynFacharten === null) 
				throw new NullPointerException()
			dynFacharten.addLast(dynFachart);
			let fachwahlIDs : LinkedCollection<Number> | null = mapSchuelerID.get(schueler);
			if (fachwahlIDs === null) 
				throw new NullPointerException()
			fachwahlIDs.addLast(iFachwahl.id);
		}
		for (let nr : number = 0; nr < susArr.length; nr++){
			let schueler : KursblockungDynSchueler = susArr[nr];
			let listFA : LinkedCollection<KursblockungDynFachart> | null = mapSchuelerFA.get(schueler);
			let listID : LinkedCollection<Number> | null = mapSchuelerID.get(schueler);
			if ((listFA === null) || (listID === null)) 
				throw new NullPointerException()
			let nWahlen : number = listFA.size();
			let arrFA : Array<KursblockungDynFachart> = Array(nWahlen).fill(null);
			let arrID : Array<number> = Array(nWahlen).fill(0);
			for (let i : number = 0; i < nWahlen; i++){
				arrFA[i] = listFA.removeFirst();
				arrID[i] = listID.removeFirst().valueOf();
			}
			schueler.aktionSetzeFachartenUndIDs(arrFA, arrID);
		}
		return false;
	}

	private schritt06FehlerBeiStatistikErstellung(fachartArr : Array<KursblockungDynFachart>, susArr : Array<KursblockungDynSchueler>) : boolean {
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
		this.statistik.aktionInitialisiere(bewertungMatrixFachart, susArr.length, fachartArr.length);
		return false;
	}

	private schritt07FehlerBeiSchienenErzeugung(pSchienen : number) : boolean {
		this.schienenArr = Array(pSchienen).fill(null);
		for (let nr : number = 0; nr < pSchienen; nr++){
			this.schienenArr[nr] = new KursblockungDynSchiene(this.logger, nr, this.statistik);
		}
		return false;
	}

	private schritt08FehlerBeiKursErstellung(pInput : KursblockungInput) : boolean {
		let vKurse : Vector<KursblockungInputKurs> = pInput.kurse;
		let nKurse : number = vKurse.size();
		let nSchienen : number = this.schienenArr.length;
		let mapKursSchieneFrei : HashMap<Number, LinkedCollection<KursblockungDynSchiene>> = new HashMap();
		let mapKursSchieneLage : HashMap<Number, LinkedCollection<KursblockungDynSchiene>> = new HashMap();
		for (let i : number = 0; i < nKurse; i++){
			let kursID : number = vKurse.get(i).id;
			let schieneFrei : LinkedCollection<KursblockungDynSchiene> = new LinkedCollection();
			mapKursSchieneLage.put(kursID, new LinkedCollection());
			mapKursSchieneFrei.put(kursID, schieneFrei);
			let perm : Array<number> = Array(nSchienen).fill(0);
			for (let j : number = 0; j < nSchienen; j++){
				perm[j] = j;
			}
			for (let j1 : number = 0; j1 < nSchienen; j1++){
				let j2 : number = this._random.nextInt(nSchienen);
				let s1 : number = perm[j1];
				let s2 : number = perm[j2];
				perm[j1] = s2;
				perm[j2] = s1;
			}
			for (let j : number = 0; j < nSchienen; j++){
				schieneFrei.addLast(this.schienenArr[perm[j]]);
			}
		}
		let regelnTyp1 : LinkedCollection<KursblockungInputRegel> | null = this.regelMap.get(GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS);
		if (regelnTyp1 !== null) 
			for (let regel1 of regelnTyp1) {
				let kursart : number = regel1.daten[0].valueOf();
				let von : number = regel1.daten[1].valueOf() - 1;
				let bis : number = regel1.daten[2].valueOf() - 1;
				for (let i : number = 0; i < pInput.kurse.size(); i++){
					let kurs : KursblockungInputKurs = pInput.kurse.get(i);
					if (kurs.kursart === kursart) {
						for (let schiene : number = von; schiene <= bis; schiene++){
							let schieneFrei : LinkedCollection<KursblockungDynSchiene> | null = mapKursSchieneFrei.get(kurs.id);
							if (schieneFrei === null) 
								throw new NullPointerException()
							schieneFrei.remove(this.schienenArr[schiene]);
						}
					}
				}
			}
		let regelnTyp6 : LinkedCollection<KursblockungInputRegel> | null = this.regelMap.get(GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS);
		if (regelnTyp6 !== null) 
			for (let regel6 of regelnTyp6) {
				let kursart : number = regel6.daten[0].valueOf();
				let von : number = regel6.daten[1].valueOf() - 1;
				let bis : number = regel6.daten[2].valueOf() - 1;
				for (let i : number = 0; i < pInput.kurse.size(); i++){
					let kurs : KursblockungInputKurs = pInput.kurse.get(i);
					if (kurs.kursart !== kursart) {
						for (let schiene : number = von; schiene <= bis; schiene++){
							let schieneFrei : LinkedCollection<KursblockungDynSchiene> | null = mapKursSchieneFrei.get(kurs.id);
							if (schieneFrei === null) 
								throw new NullPointerException()
							schieneFrei.remove(this.schienenArr[schiene]);
						}
					}
				}
			}
		let regelnTyp3 : LinkedCollection<KursblockungInputRegel> | null = this.regelMap.get(GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE);
		if (regelnTyp3 !== null) 
			for (let regel3 of regelnTyp3) {
				let kursID : number = regel3.daten[0].valueOf();
				let schiene : number = regel3.daten[1].valueOf() - 1;
				let schieneFrei : LinkedCollection<KursblockungDynSchiene> | null = mapKursSchieneFrei.get(kursID);
				if (schieneFrei === null) 
					throw new NullPointerException()
				schieneFrei.remove(this.schienenArr[schiene]);
			}
		let regelnTyp2 : LinkedCollection<KursblockungInputRegel> | null = this.regelMap.get(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE);
		if (regelnTyp2 !== null) 
			for (let regel2 of regelnTyp2) {
				let kursID : number = regel2.daten[0].valueOf();
				let schiene : number = regel2.daten[1].valueOf() - 1;
				let schieneFrei : LinkedCollection<KursblockungDynSchiene> | null = mapKursSchieneFrei.get(kursID);
				let schieneLage : LinkedCollection<KursblockungDynSchiene> | null = mapKursSchieneLage.get(kursID);
				if ((schieneFrei === null) || (schieneLage === null)) 
					throw new NullPointerException()
				if (schieneLage.contains(this.schienenArr[schiene])) 
					continue;
				if (!schieneFrei.contains(this.schienenArr[schiene])) {
					let kurs : KursblockungDynKurs | null = this.kursMap.get(kursID);
					if (kurs === null) 
						throw new NullPointerException()
					this.fehler("KURS_FIXIERE_IN_SCHIENE: Kurs=" + kurs.gibRepresentation().valueOf() + " will Schiene=" + schiene + " fixieren, die Schiene wurde aber bereits gesperrt!");
					return true;
				}
				schieneFrei.remove(this.schienenArr[schiene]);
				schieneLage.addLast(this.schienenArr[schiene]);
			}
		this.kursArr = Array(nKurse).fill(null);
		for (let i : number = 0; i < nKurse; i++){
			let iKurs : KursblockungInputKurs = vKurse.get(i);
			let representation : String = iKurs.representation;
			let fach : number = iKurs.fach;
			let kursart : number = iKurs.kursart;
			let schienen : number = iKurs.schienen;
			if (schienen <= 0) 
				throw this.fehler("Kurs \'" + representation.valueOf() + "\' belegt nur " + schienen + " Schienen, das ist zu wenig.")
			if (schienen > this.schienenArr.length) 
				throw this.fehler("Es gibt " + this.schienenArr.length + " Schienen, aber der Kurs \'" + representation.valueOf() + "\' möchte " + schienen + " Schienen belegt.")
			let listLage : LinkedCollection<KursblockungDynSchiene> | null = mapKursSchieneLage.get(iKurs.id);
			if (listLage === null) 
				throw this.fehler("listLage == null")
			let pSchienenLageFixiert : number = listLage.size();
			if (pSchienenLageFixiert > iKurs.schienen) 
				throw this.fehler("Kurs \'" + representation.valueOf() + "\' fixert " + pSchienenLageFixiert + " Schienen, das ist mehr als seine Schienenanzahl " + iKurs.schienen + " .")
			let listFrei : LinkedCollection<KursblockungDynSchiene> | null = mapKursSchieneFrei.get(iKurs.id);
			if (listFrei === null) 
				throw this.fehler("listFrei == null")
			while (listLage.size() < iKurs.schienen) {
				if (listFrei.isEmpty()) 
					throw this.fehler("Kurs \'" + representation.valueOf() + "\' hat zu viele Schienen gesperrt, so dass seine seine Schienenanzahl keinen Platz mehr hat .")
				listLage.addLast(listFrei.pollFirst());
			}
			let pSchienenLage : Array<KursblockungDynSchiene> = Array(listLage.size()).fill(null);
			for (let j : number = 0; j < pSchienenLage.length; j++){
				pSchienenLage[j] = listLage.removeFirst();
			}
			let pSchienenFrei : Array<KursblockungDynSchiene> = Array(listFrei.size()).fill(null);
			for (let j : number = 0; j < pSchienenFrei.length; j++){
				pSchienenFrei[j] = listFrei.removeFirst();
			}
			let kursartMap : HashMap<Number, KursblockungDynFachart> | null = this.fachartMap.get(fach);
			let dynFachart : KursblockungDynFachart | null = kursartMap === null ? null : kursartMap.get(kursart);
			if (dynFachart === null) 
				throw new NullPointerException()
			let kurs : KursblockungDynKurs = new KursblockungDynKurs(this._random, pSchienenLage, pSchienenLageFixiert, pSchienenFrei, iKurs, dynFachart, this.logger, i);
			this.kursArr[i] = kurs;
			this.kursMap.put(iKurs.id, kurs);
		}
		return false;
	}

	private schritt09FehlerBeiKursFreiErstellung(pInput : KursblockungInput) : boolean {
		let nKursFrei : number = 0;
		for (let i : number = 0; i < this.kursArr.length; i++){
			if (this.kursArr[i].gibHatFreiheitsgrade()) {
				nKursFrei++;
			}
		}
		this.kursArrFrei = Array(nKursFrei).fill(null);
		for (let i : number = 0, j : number = 0; i < this.kursArr.length; i++){
			if (this.kursArr[i].gibHatFreiheitsgrade()) {
				this.kursArrFrei[j] = this.kursArr[i];
				j++;
			}
		}
		return false;
	}

	private schritt10FehlerBeiFachartKursArrayErstellung(pInput : KursblockungInput) : boolean {
		let nFacharten : number = this.fachartArr.length;
		let mapFachartList : HashMap<Number, LinkedCollection<KursblockungDynKurs>> = new HashMap();
		for (let i : number = 0; i < nFacharten; i++){
			mapFachartList.put(i, new LinkedCollection());
		}
		for (let i : number = 0; i < this.kursArr.length; i++){
			let kurs : KursblockungDynKurs = this.kursArr[i];
			let fachartNr : number = kurs.gibFachart().gibNr();
			let fachartKurse : LinkedCollection<KursblockungDynKurs> | null = mapFachartList.get(fachartNr);
			if (fachartKurse === null) 
				throw new NullPointerException()
			fachartKurse.addLast(kurs);
		}
		for (let nr : number = 0; nr < nFacharten; nr++){
			let list : LinkedCollection<KursblockungDynKurs> | null = mapFachartList.get(nr);
			if (list === null) 
				throw new NullPointerException()
			let kursArr : Array<KursblockungDynKurs> = Array(list.size()).fill(null);
			for (let i : number = 0; i < kursArr.length; i++){
				kursArr[i] = list.removeFirst();
			}
			this.fachartArr[nr].aktionSetKurse(kursArr);
		}
		return false;
	}

	private schritt11FehlerBeiRegel_4_oder_5() : boolean {
		let regelnTyp4 : LinkedCollection<KursblockungInputRegel> | null = this.regelMap.get(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS);
		if (regelnTyp4 !== null) {
			for (let regel4 of regelnTyp4) {
				let schuelerID : number = regel4.daten[0].valueOf();
				let kursID : number = regel4.daten[1].valueOf();
				let schueler : KursblockungDynSchueler | null = this.schuelerMap.get(schuelerID);
				if (schueler === null) 
					throw new NullPointerException()
				let fixierterKurs : KursblockungDynKurs | null = this.kursMap.get(kursID);
				if (fixierterKurs === null) 
					throw new NullPointerException()
				for (let kurs of fixierterKurs.gibFachart().gibKurse()) {
					if (kurs as unknown !== fixierterKurs as unknown) {
						schueler.aktionSetzeKursSperrung(kurs.gibInternalID());
					}
				}
			}
		}
		let regelnTyp5 : LinkedCollection<KursblockungInputRegel> | null = this.regelMap.get(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS);
		if (regelnTyp5 !== null) {
			for (let regel5 of regelnTyp5) {
				let schuelerID : number = regel5.daten[0].valueOf();
				let kursID : number = regel5.daten[1].valueOf();
				let schueler : KursblockungDynSchueler | null = this.schuelerMap.get(schuelerID);
				if (schueler === null) 
					throw new NullPointerException()
				let verbotenerKurs : KursblockungDynKurs | null = this.kursMap.get(kursID);
				if (verbotenerKurs === null) 
					throw new NullPointerException()
				schueler.aktionSetzeKursSperrung(verbotenerKurs.gibInternalID());
			}
		}
		return false;
	}

	/**
	 *Leert die Datenstruktur und teilt dem Logger einen Fehler mit.
	 * 
	 * @param fehlermeldung Die Fehlermeldung. 
	 */
	private fehler(fehlermeldung : String) : KursblockungException | null {
		this.regelMap.clear();
		this.maxTimeMillis = 0;
		this.schienenArr = Array(0).fill(null);
		this.fachartArr = Array(0).fill(null);
		this.fachartMap.clear();
		this.kursArr = Array(0).fill(null);
		this.kursArrFrei = Array(0).fill(null);
		this.kursMap.clear();
		this.schuelerArr = Array(0).fill(null);
		this.schuelerMap.clear();
		this.statistik.clear();
		this.logger.logLn(LogLevel.ERROR, fehlermeldung);
		return new KursblockungException(fehlermeldung);
	}

	private gibBewertungsKriterium1_NichtErfuellteRegeln() : Array<Number> {
		let listeNichtErfuellterRegeln : LinkedCollection<Number | null> | null = new LinkedCollection();
		let list1 : LinkedCollection<KursblockungInputRegel> | null = this.regelMap.get(GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS);
		if (list1 !== null) 
			for (let regel of list1) {
				let kursArtID : number = regel.daten[0].valueOf();
				let schieneVon : number = regel.daten[1].valueOf() - 1;
				let schieneBis : number = regel.daten[2].valueOf() - 1;
				for (let kurs of this.kursArr) 
					if (kurs.gibDatabaseKursArtID() === kursArtID) 
						if (kurs.gibIstImSchienenIntervall(schieneVon, schieneBis)) 
							listeNichtErfuellterRegeln.add(regel.databaseID);
			}
		let list6 : LinkedCollection<KursblockungInputRegel> | null = this.regelMap.get(GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS);
		if (list6 !== null) 
			for (let regel of list6) {
				let kursArtID : number = regel.daten[0].valueOf();
				let schieneVon : number = regel.daten[1].valueOf() - 1;
				let schieneBis : number = regel.daten[2].valueOf() - 1;
				for (let kurs of this.kursArr) 
					if (kurs.gibDatabaseKursArtID() !== kursArtID) 
						if (kurs.gibIstImSchienenIntervall(schieneVon, schieneBis)) 
							listeNichtErfuellterRegeln.add(regel.databaseID);
			}
		let list2 : LinkedCollection<KursblockungInputRegel> | null = this.regelMap.get(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE);
		if (list2 !== null) 
			for (let regel of list2) {
				let kursDatabaseID : number = regel.daten[0].valueOf();
				let schiene : number = regel.daten[1].valueOf() - 1;
				let kurs : KursblockungDynKurs | null = this.kursMap.get(kursDatabaseID);
				if (kurs !== null) 
					if (kurs.gibIstInSchiene(schiene) === false) 
						listeNichtErfuellterRegeln.add(regel.databaseID);
			}
		let list3 : LinkedCollection<KursblockungInputRegel> | null = this.regelMap.get(GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE);
		if (list3 !== null) 
			for (let regel of list3) {
				let kursDatabaseID : number = regel.daten[0].valueOf();
				let schiene : number = regel.daten[1].valueOf() - 1;
				let kurs : KursblockungDynKurs | null = this.kursMap.get(kursDatabaseID);
				if (kurs !== null) 
					if (kurs.gibIstInSchiene(schiene) === true) 
						listeNichtErfuellterRegeln.add(regel.databaseID);
			}
		let list4 : LinkedCollection<KursblockungInputRegel> | null = this.regelMap.get(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS);
		if (list4 !== null) 
			for (let regel of list4) {
				let schuelerDatabaseID : number = regel.daten[0].valueOf();
				let kursDatabaseID : number = regel.daten[1].valueOf();
				let schueler : KursblockungDynSchueler | null = this.schuelerMap.get(schuelerDatabaseID);
				let kurs : KursblockungDynKurs | null = this.kursMap.get(kursDatabaseID);
				if ((schueler !== null) && (kurs !== null)) 
					if (schueler.gibIstInKurs(kurs) === false) 
						listeNichtErfuellterRegeln.add(regel.databaseID);
			}
		let list5 : LinkedCollection<KursblockungInputRegel> | null = this.regelMap.get(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS);
		if (list5 !== null) 
			for (let regel of list5) {
				let schuelerDatabaseID : number = regel.daten[0].valueOf();
				let kursDatabaseID : number = regel.daten[1].valueOf();
				let schueler : KursblockungDynSchueler | null = this.schuelerMap.get(schuelerDatabaseID);
				let kurs : KursblockungDynKurs | null = this.kursMap.get(kursDatabaseID);
				if ((schueler !== null) && (kurs !== null)) 
					if (schueler.gibIstInKurs(kurs) === true) 
						listeNichtErfuellterRegeln.add(regel.databaseID);
			}
		return listeNichtErfuellterRegeln.toArray(Array(0).fill(null));
	}

	private gibBewertungsKriterium2_NichtZugeordneteWahlen() : number {
		return this.statistik.gibBewertungNichtwahlen();
	}

	private gibBewertungsKriterium3_Kursdifferenzen_als_Histogramm() : Array<Number> {
		let original : Array<number> = this.statistik.gibArrayDerKursdifferenzen();
		let max : number = this.statistik.gibBewertungKursdifferenz();
		let copy : Array<Number> = Array(max + 1).fill(null);
		for (let i : number = 0; i <= max; i++)
			copy[i] = original[i];
		return copy;
	}

	private gibBewertungsKriterium4_GleicheFachartenProSchiene() : number {
		let summe : number = 0;
		for (let schiene of this.schienenArr) 
			summe += schiene.gibAnzahlGleicherFacharten();
		return summe;
	}

	/**
	 *Liefert das Logger-Objekt für Benutzerhinweise, Warnungen und Fehler.
	 * 
	 * @return Das Logger-Objekt für Benutzerhinweise, Warnungen und Fehler. 
	 */
	gibLogger() : Logger {
		return this.logger;
	}

	/**
	 *Liefert das Statistik-Objekt (für Anfragen zu Nichtwahlen, Kursdifferenzen, etc.).
	 * 
	 * @return Das Statistik-Objekt (für Anfragen zu Nichtwahlen, Kursdifferenzen, etc.). 
	 */
	gibStatistik() : KursblockungDynStatistik {
		return this.statistik;
	}

	/**
	 *Liefert die maximale Blockungszeit in Millisekunden. Entweder handelt es sich um einen Standardwert oder der
	 * Wert wurde im Konstruktor als Regel übergeben.
	 * 
	 * @return Liefert die maximale Blockungszeit in Millisekunden. 
	 */
	gibBlockungszeitMillis() : number {
		return this.maxTimeMillis;
	}

	/**
	 *Liefert die maximal erlaubte Anzahl an Schienen. Entweder handelt es sich um einen Standardwert oder der Wert
	 * wurde im Konstruktor als Regel übergeben.
	 * 
	 * @return Liefert die maximal erlaubte Anzahl an Schienen. 
	 */
	gibSchienenAnzahl() : number {
		return this.schienenArr.length;
	}

	/**
	 *Erzeugt ein Objekt {@link KursblockungOutput}. Dieses Objekt beinhaltet alle Informationen aus denen die GUI die
	 * Kurs-Zu-Schiene und die SuS-Zu-Kurs-Zuordnungen rekonstruieren kann.
	 * 
	 * @param  inputID   Die ID der Eingabe, also Blockung die von der GUI kommt.
	 * @param  inputSeed Der Seed, mit dem das Random-Objekt initialisiert wurde.
	 * @return           Das Blockungsergebnis für die GUI. 
	 */
	gibErzeugtesKursblockungOutput(inputSeed : number, inputID : number) : KursblockungOutput {
		let out : KursblockungOutput = new KursblockungOutput();
		out.seed = inputSeed;
		out.input = inputID;
		for (let i : number = 0; i < this.kursArr.length; i++)
			this.kursArr[i].aktionOutputErzeugen(out.kursZuSchiene);
		for (let i : number = 0; i < this.schuelerArr.length; i++)
			this.schuelerArr[i].aktionOutputsErzeugen(out.fachwahlenZuKurs);
		out.bewertungNichtErfuellteRegeln = this.gibBewertungsKriterium1_NichtErfuellteRegeln();
		out.bewertungNichtZugeordneteFachwahlen = this.gibBewertungsKriterium2_NichtZugeordneteWahlen();
		out.bewertungHistogrammDerKursdifferenzen = this.gibBewertungsKriterium3_Kursdifferenzen_als_Histogramm();
		out.bewertungGleicheFachartProSchiene = this.gibBewertungsKriterium4_GleicheFachartenProSchiene();
		return out;
	}

	/**
	 *Liefert alle Kurse.
	 * 
	 * @return Array aller Kurse. 
	 */
	gibKurseAlle() : Array<KursblockungDynKurs> {
		return this.kursArr;
	}

	/**
	 *Liefert alle Kurse deren Lage nicht komplett fixiert ist.
	 * 
	 * @return Array aller Kurse, deren Schienenlage noch veränderbar ist. 
	 */
	gibKurseDieFreiSind() : Array<KursblockungDynKurs> {
		return this.kursArrFrei;
	}

	/**
	 *Liefert die Anzahl alle Kurse deren Lage nicht komplett fixiert ist.
	 * 
	 * @return Anzahl aller Kurse, deren Schienenlage noch veränderbar ist. 
	 */
	gibKurseDieFreiSindAnzahl() : number {
		return this.kursArrFrei.length;
	}

	/**
	 *Liefert einen Long-Wert, der einer Bewertung der Fachwahlmatrix entspricht. Je kleiner der Wert, desto besser
	 * ist die Bewertung.
	 * 
	 * @return Long-Wert, der einer Bewertung der Fachwahlmatrix entspricht. 
	 */
	gibBewertungFachartPaar() : number {
		return this.statistik.gibBewertungFachartPaar();
	}

	/**
	 *Liefert ein Array aller Schülerinnen und Schüler. Falls der Parameter {@code pNurMultiKurse} TRUE ist, dann
	 * werden nur SuS mit mindestens einem Multikurs ausgewählt.
	 * 
	 * @param  pNurMultiKurse Falls TRUE, dann werden nur SuS mit mindestens einem Multikurs ausgewählt.
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
	 *Liefert ein Array aller Schülerinnen und Schüler.
	 * 
	 * @return Ein Array aller Schülerinnen und Schüler. 
	 */
	gibSchuelerArrayAlle() : Array<KursblockungDynSchueler> {
		return this.schuelerArr;
	}

	/**
	 *Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen) des Zustandes S sich
	 * verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 * 
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen) des Zustandes S sich
	 *         verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist. 
	 */
	gibBewertungJetztBesserAlsS() : number {
		return this.statistik.gibBewertungZustandS_NW_KD();
	}

	/**
	 *Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Nichtwahlen, Kursdiffenzen,
	 * Fachwahlmatrix) des Zustandes-K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 * 
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Nichtwahlen, Kursdiffenzen, Fachwahlmatrix) des
	 *         Zustandes-K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist. 
	 */
	gibCompareZustandK_NW_KD_FW() : number {
		return this.statistik.gibCompareZustandK_NW_KD_FW();
	}

	/**
	 *Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Nichtwahlen, Kursdiffenzen,
	 * Fachwahlmatrix) des Zustandes-G sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 * 
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Nichtwahlen, Kursdiffenzen, Fachwahlmatrix) des
	 *         Zustandes-G sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist. 
	 */
	gibCompareZustandG_NW_KD_FW() : number {
		return this.statistik.gibCompareZustandG_NW_KD_FW();
	}

	/**
	 *Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Fachwahlmatrix, Nichtwahlen,
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
		for (let kurs of this.kursArrFrei) {
			kurs.aktionZufaelligVerteilen();
		}
	}

	/**
	 *Verteilt einen Kurs zufällig. Kurse die keinen Freiheitsgrad haben, werden dabei ignoriert. 
	 */
	aktionKursVerteilenEinenZufaelligenFreien() : void {
		if (this.kursArrFrei.length === 0) {
			return;
		}
		let index : number = this._random.nextInt(this.kursArrFrei.length);
		let kurs : KursblockungDynKurs = this.kursArrFrei[index];
		kurs.aktionZufaelligVerteilen();
	}

	/**
	 *Verteilt einen Kurs zufällig. Kurse die keinen Freiheitsgrad haben, werden dabei ignoriert. Multikurse werden
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
	 *Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen) des Zustandes S sich
	 * verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 * 
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen) des Zustandes K sich
	 *         verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist. 
	 */
	gibBewertung_NW_KD_JetztS() : number {
		return this.statistik.gibBewertungZustandS_NW_KD();
	}

	/**
	 *Verteilt die SuS auf die jetzige Kurslage. Pro S. werden erst die Multikurse verteilt, dann werden die übrigen
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
	 *Verteilt die SuS auf die jetzige Kurslage. Pro S. werden erst die Multikurse verteilt, dann werden die übrigen
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
	 *Verteilt die SuS auf die jetzige Kurslage. Pro S. werden erst die Multikurse verteilt, dann werden die übrigen
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
