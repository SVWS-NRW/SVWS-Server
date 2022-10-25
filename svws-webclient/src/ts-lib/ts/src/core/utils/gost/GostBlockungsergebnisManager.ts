import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { GostBlockungsergebnisSchiene, cast_de_nrw_schule_svws_core_data_gost_GostBlockungsergebnisSchiene } from '../../../core/data/gost/GostBlockungsergebnisSchiene';
import { GostFach, cast_de_nrw_schule_svws_core_data_gost_GostFach } from '../../../core/data/gost/GostFach';
import { GostBlockungsdatenManager, cast_de_nrw_schule_svws_core_utils_gost_GostBlockungsdatenManager } from '../../../core/utils/gost/GostBlockungsdatenManager';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { GostBlockungsergebnisKurs, cast_de_nrw_schule_svws_core_data_gost_GostBlockungsergebnisKurs } from '../../../core/data/gost/GostBlockungsergebnisKurs';
import { GostFachwahl, cast_de_nrw_schule_svws_core_data_gost_GostFachwahl } from '../../../core/data/gost/GostFachwahl';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { GostBlockungRegel, cast_de_nrw_schule_svws_core_data_gost_GostBlockungRegel } from '../../../core/data/gost/GostBlockungRegel';
import { GostKursart, cast_de_nrw_schule_svws_core_types_gost_GostKursart } from '../../../core/types/gost/GostKursart';
import { System, cast_java_lang_System } from '../../../java/lang/System';
import { GostBlockungsergebnis, cast_de_nrw_schule_svws_core_data_gost_GostBlockungsergebnis } from '../../../core/data/gost/GostBlockungsergebnis';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { GostKursblockungRegelTyp, cast_de_nrw_schule_svws_core_types_kursblockung_GostKursblockungRegelTyp } from '../../../core/types/kursblockung/GostKursblockungRegelTyp';
import { Schueler, cast_de_nrw_schule_svws_core_data_schueler_Schueler } from '../../../core/data/schueler/Schueler';
import { NullPointerException, cast_java_lang_NullPointerException } from '../../../java/lang/NullPointerException';
import { GostBlockungSchiene, cast_de_nrw_schule_svws_core_data_gost_GostBlockungSchiene } from '../../../core/data/gost/GostBlockungSchiene';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { Arrays, cast_java_util_Arrays } from '../../../java/util/Arrays';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';
import { HashSet, cast_java_util_HashSet } from '../../../java/util/HashSet';
import { GostBlockungKurs, cast_de_nrw_schule_svws_core_data_gost_GostBlockungKurs } from '../../../core/data/gost/GostBlockungKurs';

export class GostBlockungsergebnisManager extends JavaObject {

	private readonly _parent : GostBlockungsdatenManager;

	private readonly _ergebnis : GostBlockungsergebnis;

	private readonly _map_schienenNr_schiene : HashMap<Number, GostBlockungsergebnisSchiene> = new HashMap();

	private readonly _map_schienenID_schiene : HashMap<Number, GostBlockungsergebnisSchiene> = new HashMap();

	private readonly _map_schienenID_schuelerAnzahl : HashMap<Number, Number> = new HashMap();

	private readonly _map_schienenID_kollisionen : HashMap<Number, Number> = new HashMap();

	private readonly _map_schienenID_fachartID_kurse : HashMap<Number, HashMap<Number, Vector<GostBlockungsergebnisKurs>>> = new HashMap();

	private readonly _map_schuelerID_kurse : HashMap<Number, HashSet<GostBlockungsergebnisKurs>> = new HashMap();

	private readonly _map_schuelerID_kollisionen : HashMap<Number, Number> = new HashMap();

	private readonly _map_schuelerID_fachID_kurs : HashMap<Number, HashMap<Number, GostBlockungsergebnisKurs | null>> = new HashMap();

	private readonly _map_schuelerID_schienenID_kurse : HashMap<Number, HashMap<Number, HashSet<GostBlockungsergebnisKurs>>> = new HashMap();

	private readonly _map_kursID_schienen : HashMap<Number, HashSet<GostBlockungsergebnisSchiene>> = new HashMap();

	private readonly _map_kursID_kurs : HashMap<Number, GostBlockungsergebnisKurs> = new HashMap();

	private readonly _map_kursID_schuelerIDs : HashMap<Number, HashSet<Number>> = new HashMap();

	private readonly _map_fachID_kurse : HashMap<Number, Vector<GostBlockungsergebnisKurs>> = new HashMap();

	private readonly _map_fachartID_kurse : HashMap<Number, Vector<GostBlockungsergebnisKurs>> = new HashMap();

	private readonly _map_fachartID_kursdifferenz : HashMap<Number, Number> = new HashMap();

	private readonly _map_regeltyp_regeln : HashMap<GostKursblockungRegelTyp, HashSet<GostBlockungRegel>> = new HashMap();


	/**
	 * Erstellt einen leeren GostBlockungsergebnisManager in Bezug auf GostBlockungsdatenManager. Die ID des leeren
	 * Ergebnisses ist -1 und muss noch gesetzt werden.
	 * 
	 * @param pParent                  Das Eltern-Objekt. (Daten-Manager für die grundlegenden Definitionen der
	 *                                 Blockung)
	 * @param pGostBlockungsergebnisID Die ID des Blockungsergebnisses.
	 */
	public constructor(pParent : GostBlockungsdatenManager, pGostBlockungsergebnisID : number);

	/**
	 * Erstellt einen neuen Manager mit den Daten aus dem übergebenen Ergebnis.
	 * 
	 * @param pParent   Das Eltern-Objekt. (Daten-Manager für die grundlegenden Definitionen der Blockung)
	 * @param pErgebnis Das Ergebnis, welches kopiert wird.
	 */
	public constructor(pParent : GostBlockungsdatenManager, pErgebnis : GostBlockungsergebnis);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0 : GostBlockungsdatenManager, __param1 : GostBlockungsergebnis | number) {
		super();
		if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.nrw.schule.svws.core.utils.gost.GostBlockungsdatenManager')))) && ((typeof __param1 !== "undefined") && typeof __param1 === "number")) {
			let pParent : GostBlockungsdatenManager = cast_de_nrw_schule_svws_core_utils_gost_GostBlockungsdatenManager(__param0);
			let pGostBlockungsergebnisID : number = __param1 as number;
			this._parent = pParent;
			this._ergebnis = new GostBlockungsergebnis();
			this._ergebnis.id = pGostBlockungsergebnisID;
			this.stateClear(this._ergebnis);
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.nrw.schule.svws.core.utils.gost.GostBlockungsdatenManager')))) && ((typeof __param1 !== "undefined") && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('de.nrw.schule.svws.core.data.gost.GostBlockungsergebnis'))))) {
			let pParent : GostBlockungsdatenManager = cast_de_nrw_schule_svws_core_utils_gost_GostBlockungsdatenManager(__param0);
			let pErgebnis : GostBlockungsergebnis = cast_de_nrw_schule_svws_core_data_gost_GostBlockungsergebnis(__param1);
			this._parent = pParent;
			this._ergebnis = new GostBlockungsergebnis();
			this.stateClear(pErgebnis);
		} else throw new Error('invalid method overload');
	}

	private stateClear(pErgebnis : GostBlockungsergebnis | null) : void {
		this._ergebnis.id = (pErgebnis === null) ? -1 : pErgebnis.id;
		this._ergebnis.blockungID = this._parent.getID();
		this._ergebnis.name = (pErgebnis === null) ? "Ergebnis (ID noch nicht initialisiert)" : pErgebnis.name;
		this._ergebnis.gostHalbjahr = this._parent.daten().gostHalbjahr;
		this._ergebnis.istMarkiert = (pErgebnis === null) ? false : pErgebnis.istMarkiert;
		this._ergebnis.istVorlage = (pErgebnis === null) ? false : pErgebnis.istVorlage;
		this._ergebnis.bewertung.kursdifferenzMax = 0;
		this._ergebnis.bewertung.kursdifferenzHistogramm = Array(this._parent.getSchuelerAnzahl() + 1).fill(0);
		this._ergebnis.bewertung.anzahlSchuelerNichtZugeordnet += this._parent.daten().fachwahlen.size();
		for (let gRegelTyp of GostKursblockungRegelTyp.getCollection()) 
			this._map_regeltyp_regeln.put(gRegelTyp, new HashSet());
		for (let gRegel of this._parent.daten().regeln) 
			this.getOfRegeltypRegelmenge(GostKursblockungRegelTyp.fromTyp(gRegel.typ)).add(gRegel);
		for (let gSchiene of this._parent.daten().schienen) {
			let eSchiene : GostBlockungsergebnisSchiene = new GostBlockungsergebnisSchiene();
			eSchiene.id = gSchiene.id;
			this._ergebnis.schienen.add(eSchiene);
			if (this._map_schienenNr_schiene.put(gSchiene.nummer, eSchiene) !== null) 
				throw new NullPointerException("Schienen NR " + gSchiene.nummer + " doppelt!")
			if (this._map_schienenID_schiene.put(gSchiene.id, eSchiene) !== null) 
				throw new NullPointerException("Schienen ID " + gSchiene.id + " doppelt!")
			if (this._map_schienenID_schuelerAnzahl.put(gSchiene.id, 0) !== null) 
				throw new NullPointerException("Schienen ID " + gSchiene.id + " doppelt!")
			if (this._map_schienenID_kollisionen.put(gSchiene.id, 0) !== null) 
				throw new NullPointerException("Schienen ID " + gSchiene.id + " doppelt!")
		}
		for (let gKurs of this._parent.daten().kurse) {
			let eKurs : GostBlockungsergebnisKurs = new GostBlockungsergebnisKurs();
			eKurs.id = gKurs.id;
			eKurs.fachID = gKurs.fach_id;
			eKurs.kursart = gKurs.kursart;
			eKurs.anzahlSchienen = gKurs.anzahlSchienen;
			this._ergebnis.bewertung.anzahlKurseNichtZugeordnet += eKurs.anzahlSchienen;
			if (this._map_kursID_kurs.put(eKurs.id, eKurs) !== null) 
				throw new NullPointerException("Kurs-ID " + eKurs.id + " doppelt!")
			if (this._map_kursID_schienen.put(eKurs.id, new HashSet()) !== null) 
				throw new NullPointerException("Kurs-ID " + eKurs.id + " doppelt!")
			if (this._map_kursID_schuelerIDs.put(eKurs.id, new HashSet()) !== null) 
				throw new NullPointerException("Kurs-ID " + eKurs.id + " doppelt!")
			let fachgruppe : Vector<GostBlockungsergebnisKurs> | null = this._map_fachID_kurse.get(eKurs.fachID);
			if (fachgruppe === null) {
				fachgruppe = new Vector();
				this._map_fachID_kurse.put(eKurs.fachID, fachgruppe);
			}
			fachgruppe.add(eKurs);
			let fachartID : number = GostKursart.getFachartID(eKurs.fachID, eKurs.kursart);
			let fachartgruppe : Vector<GostBlockungsergebnisKurs> | null = this._map_fachartID_kurse.get(fachartID);
			if (fachartgruppe === null) {
				fachartgruppe = new Vector();
				this._map_fachartID_kurse.put(fachartID, fachartgruppe);
				this._map_fachartID_kursdifferenz.put(fachartID, 0);
				this._ergebnis.bewertung.kursdifferenzHistogramm[0]++;
			}
			fachartgruppe.add(eKurs);
		}
		for (let gSchiene of this._parent.daten().schienen) {
			this._map_schienenID_fachartID_kurse.put(gSchiene.id, new HashMap());
			for (let fachartID of this._map_fachartID_kursdifferenz.keySet()) 
				this.getOfSchieneFachartKursmengeMap(gSchiene.id).put(fachartID, new Vector());
		}
		for (let gSchueler of this._parent.daten().schueler) {
			let eSchuelerID : Number = gSchueler.id;
			if (this._map_schuelerID_kurse.put(eSchuelerID, new HashSet()) !== null) 
				throw new NullPointerException("Schüler ID " + eSchuelerID.valueOf() + " doppelt!")
			if (this._map_schuelerID_kollisionen.put(eSchuelerID, 0) !== null) 
				throw new NullPointerException("Schüler ID " + eSchuelerID.valueOf() + " doppelt!")
		}
		for (let gFachwahl of this._parent.daten().fachwahlen) {
			let mapFK : HashMap<Number, GostBlockungsergebnisKurs | null> | null = this._map_schuelerID_fachID_kurs.get(gFachwahl.schuelerID);
			if (mapFK === null) {
				mapFK = new HashMap();
				this._map_schuelerID_fachID_kurs.put(gFachwahl.schuelerID, mapFK);
			}
			if (mapFK.put(gFachwahl.fachID, null) !== null) 
				throw new NullPointerException("Schüler " + gFachwahl.schuelerID + " hat Fach " + gFachwahl.fachID + " doppelt!")
		}
		for (let gSchueler of this._parent.daten().schueler) {
			this._map_schuelerID_schienenID_kurse.put(gSchueler.id, new HashMap());
			for (let gSchiene of this._parent.daten().schienen) 
				this.getOfSchuelerSchienenKursmengeMap(gSchueler.id).put(gSchiene.id, new HashSet());
		}
		if (pErgebnis !== null) {
			let kursBearbeitet : HashSet<Number> | null = new HashSet();
			for (let schiene of pErgebnis.schienen) 
				for (let kurs of schiene.kurse) {
					this.setKursSchiene(kurs.id, schiene.id, true);
					if (kursBearbeitet.add(kurs.id)) 
						for (let schuelerID of kurs.schueler) 
							this.setSchuelerKurs(schuelerID.valueOf(), kurs.id, true);
				}
		}
		this.stateRegelvalidierung();
	}

	private stateRegelvalidierung() : void {
		let regelVerletzungen : Vector<Number> = this._ergebnis.bewertung.regelVerletzungen;
		regelVerletzungen.clear();
		for (let r of this.getOfRegeltypRegelmenge(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS)) {
			let schuelerID : number = r.parameter.get(0).valueOf();
			let kursID : number = r.parameter.get(1).valueOf();
			if (!this.getOfSchuelerOfKursIstZugeordnet(schuelerID, kursID)) 
				regelVerletzungen.add(r.id);
		}
		for (let r of this.getOfRegeltypRegelmenge(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS)) {
			let schuelerID : number = r.parameter.get(0).valueOf();
			let kursID : number = r.parameter.get(1).valueOf();
			if (this.getOfSchuelerOfKursIstZugeordnet(schuelerID, kursID)) 
				regelVerletzungen.add(r.id);
		}
		for (let r of this.getOfRegeltypRegelmenge(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE)) {
			let kursID : number = r.parameter.get(0).valueOf();
			let schienenNr : number = r.parameter.get(1).valueOf();
			let schiene : GostBlockungsergebnisSchiene | null = this.getSchieneEmitNr(schienenNr);
			if (!this.getOfKursSchienenmenge(kursID).contains(schiene)) 
				regelVerletzungen.add(r.id);
		}
		for (let r of this.getOfRegeltypRegelmenge(GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE)) {
			let kursID : number = r.parameter.get(0).valueOf();
			let schienenNr : number = r.parameter.get(1).valueOf();
			let schiene : GostBlockungsergebnisSchiene | null = this.getSchieneEmitNr(schienenNr);
			if (this.getOfKursSchienenmenge(kursID).contains(schiene)) 
				regelVerletzungen.add(r.id);
		}
		for (let r of this.getOfRegeltypRegelmenge(GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS)) {
			let kursart : number = r.parameter.get(0).valueOf();
			let schienenNrVon : number = r.parameter.get(1).valueOf();
			let schienenNrBis : number = r.parameter.get(2).valueOf();
			for (let schienenNr : number = schienenNrVon; schienenNr <= schienenNrBis; schienenNr++)
				for (let eKurs of this.getSchieneEmitNr(schienenNr).kurse) 
					if (eKurs.kursart === kursart) 
						regelVerletzungen.add(r.id);
		}
		for (let r of this.getOfRegeltypRegelmenge(GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS)) {
			let kursart : number = r.parameter.get(0).valueOf();
			let schienenNrVon : number = r.parameter.get(1).valueOf();
			let schienenNrBis : number = r.parameter.get(2).valueOf();
			for (let eKurs of this._map_kursID_kurs.values()) 
				for (let eSchieneID of eKurs.schienen) {
					let nr : number = this.getSchieneG(eSchieneID.valueOf()).nummer;
					let b1 : boolean = eKurs.kursart === kursart;
					let b2 : boolean = (schienenNrVon <= nr) && (nr <= schienenNrBis);
					if (b1 !== b2) 
						regelVerletzungen.add(r.id);
				}
		}
	}

	/**
	 * Fügt den Schüler dem Kurs hinzu.
	 * 
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * @param  pKursID     Die Datenbank-ID des Kurses.
	 * 
	 * @return             FALSE, falls der Schüler bereits zugeordnet war, sonst TRUE.
	 */
	private stateSchuelerKursHinzufuegen(pSchuelerID : number, pKursID : number) : boolean {
		let kurs : GostBlockungsergebnisKurs = this.getKursE(pKursID);
		let fachID : number = kurs.fachID;
		if (this.getOfSchuelerOfFachZugeordneterKurs(pSchuelerID, fachID) !== null) 
			return false;
		let kurseOfSchueler : HashSet<GostBlockungsergebnisKurs> = this.getOfSchuelerKursmenge(pSchuelerID);
		let schuelerIDsOfKurs : HashSet<Number> = this.getOfKursSchuelerIDmenge(pKursID);
		let mapSFK : HashMap<Number, GostBlockungsergebnisKurs | null> = this.getOfSchuelerFachIDKursMap(pSchuelerID);
		let fachartID : number = GostKursart.getFachartID(fachID, kurs.kursart);
		kurs.schueler.add(pSchuelerID);
		kurseOfSchueler.add(kurs);
		schuelerIDsOfKurs.add(pSchuelerID);
		this._ergebnis.bewertung.anzahlSchuelerNichtZugeordnet--;
		mapSFK.put(fachID, kurs);
		this.stateKursdifferenzUpdate(fachartID);
		for (let schieneID of kurs.schienen) 
			this.stateSchuelerSchieneHinzufuegen(pSchuelerID, schieneID.valueOf(), kurs);
		this.stateRegelvalidierung();
		return true;
	}

	/**
	 * Entfernt den Schüler aus dem Kurs.
	 * 
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * @param  pKursID     Die Datenbank-ID des Kurses.
	 * 
	 * @return             FALSE, falls der Schüler bereits nicht zugeordnet war, sonst TRUE.
	 */
	private stateSchuelerKursEntfernen(pSchuelerID : number, pKursID : number) : boolean {
		let kurs : GostBlockungsergebnisKurs = this.getKursE(pKursID);
		let fachID : number = kurs.fachID;
		if (this.getOfSchuelerOfFachZugeordneterKurs(pSchuelerID, fachID) as unknown !== kurs as unknown) 
			return false;
		let kurseOfSchueler : HashSet<GostBlockungsergebnisKurs> = this.getOfSchuelerKursmenge(pSchuelerID);
		let schuelerIDsOfKurs : HashSet<Number> = this.getOfKursSchuelerIDmenge(pKursID);
		let mapSFK : HashMap<Number, GostBlockungsergebnisKurs | null> = this.getOfSchuelerFachIDKursMap(pSchuelerID);
		let fachartID : number = GostKursart.getFachartID(fachID, kurs.kursart);
		kurs.schueler.remove(pSchuelerID);
		kurseOfSchueler.remove(kurs);
		schuelerIDsOfKurs.remove(pSchuelerID);
		this._ergebnis.bewertung.anzahlSchuelerNichtZugeordnet++;
		mapSFK.put(fachID, null);
		this.stateKursdifferenzUpdate(fachartID);
		for (let schieneID of kurs.schienen) 
			this.stateSchuelerSchieneEntfernen(pSchuelerID, schieneID.valueOf(), kurs);
		this.stateRegelvalidierung();
		return true;
	}

	/**
	 * Fügt den Kurs der Schiene hinzu.
	 * 
	 * @param  pKursID     Die Datenbank-ID des Kurses.
	 * @param  pSchienenID Die Datenbank-ID der Schiene.
	 * 
	 * @return             FALSE, falls der Kurs bereits in der Schiene war, sonst TRUE.
	 */
	private stateKursSchieneHinzufuegen(pKursID : number, pSchienenID : number) : boolean {
		let kurs : GostBlockungsergebnisKurs = this.getKursE(pKursID);
		let schiene : GostBlockungsergebnisSchiene = this.getSchieneE(pSchienenID);
		let schienenOfKurs : HashSet<GostBlockungsergebnisSchiene> = this.getOfKursSchienenmenge(pKursID);
		let fachID : number = kurs.fachID;
		let fachartID : number = GostKursart.getFachartID(fachID, kurs.kursart);
		if (schienenOfKurs.contains(schiene)) 
			return false;
		let kursGruppe : Vector<GostBlockungsergebnisKurs> | null = this.getOfSchieneFachartKursmengeMap(pSchienenID).get(fachartID);
		if (kursGruppe === null) 
			throw new NullPointerException("SchieneID=" + pSchienenID + " --> FachartID=" + fachartID + " --> NULL")
		this._ergebnis.bewertung.anzahlKurseNichtZugeordnet -= Math.abs(kurs.anzahlSchienen - schienenOfKurs.size());
		kurs.schienen.add(schiene.id);
		schiene.kurse.add(kurs);
		schienenOfKurs.add(schiene);
		for (let schuelerID of kurs.schueler) 
			this.stateSchuelerSchieneHinzufuegen(schuelerID.valueOf(), schiene.id, kurs);
		this._ergebnis.bewertung.anzahlKurseNichtZugeordnet += Math.abs(kurs.anzahlSchienen - schienenOfKurs.size());
		this._ergebnis.bewertung.anzahlKurseMitGleicherFachartProSchiene += kursGruppe.isEmpty() ? 0 : 1;
		kursGruppe.add(kurs);
		this.stateRegelvalidierung();
		return true;
	}

	/**
	 * Entfernt den Kurs aus der Schiene.
	 * 
	 * @param  pKursID     Die Datenbank-ID des Kurses.
	 * @param  pSchienenID Die Datenbank-ID der Schiene.
	 * 
	 * @return             FALSE, falls der Kurs bereits nicht in der Schiene war, sonst TRUE.
	 */
	private stateKursSchieneEntfernen(pKursID : number, pSchienenID : number) : boolean {
		let kurs : GostBlockungsergebnisKurs = this.getKursE(pKursID);
		let schiene : GostBlockungsergebnisSchiene = this.getSchieneE(pSchienenID);
		let schienenOfKurs : HashSet<GostBlockungsergebnisSchiene> = this.getOfKursSchienenmenge(pKursID);
		let fachID : number = kurs.fachID;
		let fachartID : number = GostKursart.getFachartID(fachID, kurs.kursart);
		if (!schienenOfKurs.contains(schiene)) 
			return false;
		let kursGruppe : Vector<GostBlockungsergebnisKurs> | null = this.getOfSchieneFachartKursmengeMap(pSchienenID).get(fachartID);
		if (kursGruppe === null) 
			throw new NullPointerException("SchieneID=" + pSchienenID + " --> FachartID=" + fachartID + " --> NULL")
		this._ergebnis.bewertung.anzahlKurseNichtZugeordnet -= Math.abs(kurs.anzahlSchienen - schienenOfKurs.size());
		kurs.schienen.remove(schiene.id);
		schiene.kurse.remove(kurs);
		schienenOfKurs.remove(schiene);
		for (let schuelerID of kurs.schueler) 
			this.stateSchuelerSchieneEntfernen(schuelerID.valueOf(), schiene.id, kurs);
		this._ergebnis.bewertung.anzahlKurseNichtZugeordnet += Math.abs(kurs.anzahlSchienen - schienenOfKurs.size());
		kursGruppe.remove(kurs);
		this._ergebnis.bewertung.anzahlKurseMitGleicherFachartProSchiene -= kursGruppe.isEmpty() ? 0 : 1;
		this.stateRegelvalidierung();
		return true;
	}

	private stateSchuelerSchieneHinzufuegen(pSchuelerID : number, pSchienenID : number, pKurs : GostBlockungsergebnisKurs) : void {
		let schieneSchuelerzahl : number = this.getOfSchieneAnzahlSchueler(pSchienenID);
		this._map_schienenID_schuelerAnzahl.put(pSchienenID, schieneSchuelerzahl + 1);
		let kursmenge : HashSet<GostBlockungsergebnisKurs> = this.getOfSchuelerOfSchieneKursmenge(pSchuelerID, pSchienenID);
		kursmenge.add(pKurs);
		if (kursmenge.size() > 1) {
			let schieneKollisionen : number = this.getOfSchieneAnzahlSchuelerMitKollisionen(pSchienenID);
			this._map_schienenID_kollisionen.put(pSchienenID, schieneKollisionen + 1);
			let schuelerKollisionen : number = this.getOfSchuelerAnzahlKollisionen(pSchuelerID);
			this._map_schuelerID_kollisionen.put(pSchuelerID, schuelerKollisionen + 1);
			this._ergebnis.bewertung.anzahlSchuelerKollisionen++;
		}
	}

	private stateSchuelerSchieneEntfernen(pSchuelerID : number, pSchienenID : number, pKurs : GostBlockungsergebnisKurs) : void {
		let schieneSchuelerzahl : number = this.getOfSchieneAnzahlSchueler(pSchienenID);
		if (schieneSchuelerzahl === 0) 
			throw new NullPointerException("schieneSchuelerzahl == 0 --> Entfernen unmöglich!")
		this._map_schienenID_schuelerAnzahl.put(pSchienenID, schieneSchuelerzahl - 1);
		let kursmenge : HashSet<GostBlockungsergebnisKurs> = this.getOfSchuelerOfSchieneKursmenge(pSchuelerID, pSchienenID);
		kursmenge.remove(pKurs);
		if (kursmenge.size() > 0) {
			let schieneKollisionen : number = this.getOfSchieneAnzahlSchuelerMitKollisionen(pSchienenID);
			if (schieneKollisionen === 0) 
				throw new NullPointerException("schieneKollisionen == 0 --> Entfernen unmöglich!")
			this._map_schienenID_kollisionen.put(pSchienenID, schieneKollisionen - 1);
			let schuelerKollisionen : number = this.getOfSchuelerAnzahlKollisionen(pSchuelerID);
			if (schuelerKollisionen === 0) 
				throw new NullPointerException("schuelerKollisionen == 0 --> Entfernen unmöglich!")
			this._map_schuelerID_kollisionen.put(pSchuelerID, schuelerKollisionen - 1);
			if (this._ergebnis.bewertung.anzahlSchuelerKollisionen === 0) 
				throw new NullPointerException("Gesamtkollisionen == 0 --> Entfernen unmöglich!")
			this._ergebnis.bewertung.anzahlSchuelerKollisionen--;
		}
	}

	private stateKursdifferenzUpdate(pFachartID : number) : void {
		let oldKD : number = this.getOfFachartKursdifferenz(pFachartID);
		let kurs1 : GostBlockungsergebnisKurs | null = this.getOfFachartKursmenge(pFachartID).firstElement();
		if (kurs1 === null) 
			throw new NullPointerException("Fachart-ID " + pFachartID + " ohne Kurs!")
		let min : number = kurs1.schueler.size();
		let max : number = min;
		for (let kurs of this.getOfFachartKursmenge(pFachartID)) {
			let size : number = kurs.schueler.size();
			min = Math.min(min, size);
			max = Math.max(max, size);
		}
		let newKD : number = max - min;
		if (newKD === oldKD) 
			return;
		this._map_fachartID_kursdifferenz.put(pFachartID, newKD);
		let kursdifferenzen : Array<number> | null = this._ergebnis.bewertung.kursdifferenzHistogramm;
		kursdifferenzen[oldKD]--;
		kursdifferenzen[newKD]++;
		if (oldKD === this._ergebnis.bewertung.kursdifferenzMax) 
			if (newKD > oldKD) {
				this._ergebnis.bewertung.kursdifferenzMax = newKD;
			} else {
				if (kursdifferenzen[oldKD] === 0) 
					this._ergebnis.bewertung.kursdifferenzMax = newKD;
			}
	}

	/**
	 * Liefert die Blockungs-ID. Das ist die ID des Elternteils.
	 * 
	 * @return Liefert die Blockungs-ID. Das ist die ID des Elternteils.
	 */
	public getBlockungsdatenID() : number {
		return this._ergebnis.blockungID;
	}

	/**
	 * Liefert das Blockungsergebnis zurück.
	 * 
	 * @return das Blockungsergebnis
	 */
	public getErgebnis() : GostBlockungsergebnis {
		return this._ergebnis;
	}

	/**
	 * Liefert den Wert des 1. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl der Regelverletzungen. <br>
	 * - Die Anzahl der nicht genügend gesetzten Kurse. <br>
	 * 
	 * @return Den Wert des 1. Bewertungskriteriums.
	 */
	public getOfBewertung1Wert() : number {
		let summe : number = 0;
		summe += this._ergebnis.bewertung.anzahlKurseNichtZugeordnet;
		summe += this._ergebnis.bewertung.regelVerletzungen.size();
		return summe;
	}

	/**
	 * Liefert eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der Regelverletzungen. <br>
	 * - Die Anzahl der nicht genügend gesetzten Kurse. <br>
	 * 
	 * @return Eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public getOfBewertung1Farbcode() : number {
		let summe : number = this.getOfBewertung1Wert();
		return 1 - 1 / (0.25 * summe + 1);
	}

	/**
	 * Liefert den Wert des 2. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht zugeordneten Schülerfachwahlen. <br>
	 * - Die Anzahl der Schülerkollisionen. <br>
	 * 
	 * @return Den Wert des 2. Bewertungskriteriums.
	 */
	public getOfBewertung2Wert() : number {
		let summe : number = 0;
		summe += this._ergebnis.bewertung.anzahlSchuelerNichtZugeordnet;
		summe += this._ergebnis.bewertung.anzahlSchuelerKollisionen;
		return summe;
	}

	/**
	 * Liefert eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht zugeordneten Schülerfachwahlen. <br>
	 * - Die Anzahl der Schülerkollisionen. <br>
	 * 
	 * @return Eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public getOfBewertung2Farbcode() : number {
		let summe : number = this.getOfBewertung2Wert();
		return 1 - 1 / (0.25 * summe + 1);
	}

	/**
	 * Liefert den Wert des 3. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Größte Kursdifferenz. <br>
	 * Der Wert 0 und 1 werden unterschieden, sind aber von der Bewertung her Äquivalent.
	 * 
	 * @return Den Wert des 3. Bewertungskriteriums.
	 */
	public getOfBewertung3Wert() : number {
		return this._ergebnis.bewertung.kursdifferenzMax;
	}

	/**
	 * Liefert eine Güte des 3. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Größte Kursdifferenz. <br>
	 * Der Wert 0 und 1 werden unterschieden, sind aber von der Bewertung her Äquivalent.
	 * 
	 * @return Eine Güte des 3. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public getOfBewertung3Farbcode() : number {
		let wert : number = this.getOfBewertung3Wert();
		if (wert > 0) 
			wert--;
		return 1 - 1 / (0.25 * wert + 1);
	}

	/**
	 * Liefert den Wert des 4. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl an Kursen mit gleicher Fachart (Fach, Kursart) in einer Schiene. <br>
	 * Dieses Bewertungskriterium wird teilweise absichtlich verletzt, wenn z. B. Schienen erzeugt werden mit dem selben
	 * Fach (Sport-Schiene). Nichtsdestotrotz möchte man häufig nicht die selben Fächer in einer Schiene, aufgrund von
	 * Raumkapazitäten (Fachräume).
	 * 
	 * @return Den Wert des 4. Bewertungskriteriums.
	 */
	public getOfBewertung4Wert() : number {
		return this._ergebnis.bewertung.anzahlKurseMitGleicherFachartProSchiene;
	}

	/**
	 * Liefert eine Güte des 4. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl an Kursen mit gleicher Fachart (Fach, Kursart) in einer Schiene. <br>
	 * Dieses Bewertungskriterium wird teilweise absichtlich verletzt, wenn z. B. Schienen erzeugt werden mit dem selben
	 * Fach (Sport-Schiene). Nichtsdestotrotz möchte man häufig nicht die selben Fächer in einer Schiene, aufgrund von
	 * Raumkapazitäten (Fachräume).
	 * 
	 * @return Eine Güte des 4. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public getOfBewertung4Farbcode() : number {
		let wert : number = this.getOfBewertung4Wert();
		return 1 - 1 / (0.25 * wert + 1);
	}

	/**
	 * Liefert die Anzahl an Schülerkollisionen. Ist ein Schüler x mal in einer Schiene und ist x > 1, dann wird dies
	 * als x-1 Kollisionen gezählt.
	 * 
	 * @return Die Gesamtzahl der Kollisionen zurück.
	 */
	public getOfBewertungAnzahlKollisionen() : number {
		return this._ergebnis.bewertung.anzahlSchuelerKollisionen;
	}

	/**
	 * Liefert die Anzahl nicht vollständig verteilter Kurse. Ein Multikurse der über zwei Schienen geht und gar nicht
	 * zugeteilt wurde, wird doppelt gezählt.
	 * 
	 * @return Liefert die Anzahl nicht vollständig verteilter Kurse.
	 */
	public getOfBewertungAnzahlNichtZugeordneterKurse() : number {
		return this._ergebnis.bewertung.anzahlKurseNichtZugeordnet;
	}

	/**
	 * Liefert die Anzahl an Fachwahlen, die nicht zugeordnet wurden.
	 * 
	 * @return Die Anzahl an Fachwahlen, die nicht zugeordnet wurden.
	 */
	public getOfBewertungAnzahlNichtzugeordneterFachwahlen() : number {
		return this._ergebnis.bewertung.anzahlSchuelerNichtZugeordnet;
	}

	/**
	 * Liefert die Menge der Regeln des angegebenen Typs. <br>
	 * Wenn es zu dem Typ keine Regel gibt, ist die Menge leer.
	 * 
	 * @param pTyp Der Typ der Regel.
	 * @return Die Menge der Regeln des angegebenen Typs.
	 */
	public getOfRegeltypRegelmenge(pTyp : GostKursblockungRegelTyp) : HashSet<GostBlockungRegel> {
		let set : HashSet<GostBlockungRegel> | null = this._map_regeltyp_regeln.get(pTyp);
		if (set === null) 
			return new HashSet();
		return set;
	}

	/**
	 * Ermittelt das Fach für die angegebene ID. Delegiert den Aufruf an den Fächer-Manager des Eltern-Objektes {@link GostBlockungsdatenManager}.
	 * Erzeugt eine NullPointerException im Fehlerfall, dass die ID nicht bekannt ist.
	 * 
	 * @param pFachID Die Datenbank-ID des Faches.
	 * @return Das GostFach-Objekt.
	 * @throws NullPointerException im Falle, dass die ID nicht bekannt ist.
	 */
	public getFach(pFachID : number) : GostFach {
		return this._parent.faecherManager().getOrException(pFachID);
	}

	/**
	 * Liefert die Menge aller Kurse mit dem angegebenen Fach. <br>
	 * Wirft eine Exception, wenn der ID kein Fach zugeordnet ist.
	 * 
	 * @param pFachID Die Datenbank-ID des Faches.
	 * @return Die Menge aller Kurse mit dem angegebenen Fach.
	 */
	public getOfFachKursmenge(pFachID : number) : Vector<GostBlockungsergebnisKurs> {
		let kurseOfFach : Vector<GostBlockungsergebnisKurs> | null = this._map_fachID_kurse.get(pFachID);
		if (kurseOfFach === null) 
			throw new NullPointerException("Fach-ID " + pFachID + " unbekannt!")
		return kurseOfFach;
	}

	/**
	 * Liefert die Kursmenge, die zur Fachart gehört. <br>
	 * Die Fachart-ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.
	 * 
	 * @param  pFachartID           Die ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.
	 * 
	 * @return                      Die Kursmenge, die zur Fachart gehört.
	 * 
	 * @throws NullPointerException Falls die Fachart-ID unbekannt ist.
	 */
	public getOfFachartKursmenge(pFachartID : number) : Vector<GostBlockungsergebnisKurs> {
		let fachartGruppe : Vector<GostBlockungsergebnisKurs> | null = this._map_fachartID_kurse.get(pFachartID);
		if (fachartGruppe === null) 
			throw new NullPointerException("Fachart-ID " + pFachartID + " unbekannt!")
		return fachartGruppe;
	}

	/**
	 * Liefert die Kursdifferenz der Fachart. <br>
	 * Die Fachart-ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.
	 * 
	 * @param  pFachartID           Die ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.
	 * 
	 * @return                      Die Kursdifferenz der Fachart.
	 * 
	 * @throws NullPointerException Falls die Fachart-ID unbekannt ist.
	 */
	public getOfFachartKursdifferenz(pFachartID : number) : number {
		let kursdifferenz : Number | null = this._map_fachartID_kursdifferenz.get(pFachartID);
		if (kursdifferenz === null) 
			throw new NullPointerException("Fachart-ID " + pFachartID + " unbekannt!")
		return kursdifferenz.valueOf();
	}

	/**
	 * Ermittelt den Schüler für die angegebene ID. Delegiert den Aufruf an das Eltern-Objekt {@link GostBlockungsdatenManager}.
	 * Erzeugt eine NullPointerException im Fehlerfall, dass die ID nicht bekannt ist.
	 * 
	 * @param pSchuelerID Die Datenbank-ID des Schülers.
	 * @return Das Schueler-Objekt.
	 * @throws     NullPointerException im Falle, dass die ID nicht bekannt ist
	 */
	public getSchuelerG(pSchuelerID : number) : Schueler {
		return this._parent.getSchueler(pSchuelerID);
	}

	/**
	 * Liefert einen Schüler-String im Format: 'Nachname, Vorname'.
	 * 
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * 
	 * @return             Einen Schüler-String im Format: 'Nachname, Vorname'.
	 */
	public getOfSchuelerNameVorname(pSchuelerID : number) : String {
		let schueler : Schueler = this._parent.getSchueler(pSchuelerID);
		return schueler.nachname + ", " + schueler.vorname;
	}

	/**
	 * Liefert die Menge aller Kurse, die dem Schüler zugeordnet sind. <br>
	 * Wirft eine Exception, wenn der ID kein Schüler zugeordnet ist.
	 * 
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * @return Die Menge aller Kurse, die dem Schüler zugeordnet sind.
	 */
	public getOfSchuelerKursmenge(pSchuelerID : number) : HashSet<GostBlockungsergebnisKurs> {
		let kursIDs : HashSet<GostBlockungsergebnisKurs> | null = this._map_schuelerID_kurse.get(pSchuelerID);
		if (kursIDs === null) 
			throw new NullPointerException("Schüler-ID " + pSchuelerID + " unbekannt!")
		return kursIDs;
	}

	/**
	 * Liefert die Menge aller Kurse des Schülers mit Kollisionen.
	 * 
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * @return Die Menge aller Kurse des Schülers mit Kollisionen.
	 */
	public getOfSchuelerKursmengeMitKollisionen(pSchuelerID : number) : HashSet<GostBlockungsergebnisKurs> {
		let mapSchieneKurse : HashMap<Number, HashSet<GostBlockungsergebnisKurs>> = this.getOfSchuelerSchienenKursmengeMap(pSchuelerID);
		let set : HashSet<GostBlockungsergebnisKurs> = new HashSet();
		for (let kurseDerSchiene of mapSchieneKurse.values()) 
			if (kurseDerSchiene.size() > 1) 
				set.addAll(kurseDerSchiene);
		return set;
	}

	/**
	 * Liefert TRUE, falls der Schüler mindestens eine Kollision hat. <br>
	 * Ein Schüler, der N>1 Mal in einer Schiene ist, erzeugt N-1 Kollisionen.
	 * 
	 * @param pSchuelerID Die Datenbank-ID des Schülers.
	 * @return TRUE, falls der Schüler mindestens eine Kollision hat.
	 */
	public getOfSchuelerHatKollision(pSchuelerID : number) : boolean {
		return this.getOfSchuelerAnzahlKollisionen(pSchuelerID) > 0;
	}

	/**
	 * Liefert die Anzahl an Kollisionen des Schülers. <br>
	 * Ein Schüler, der N>1 Mal in einer Schiene ist, erzeugt N-1 Kollisionen.
	 * 
	 * @param pSchuelerID Die Datenbank-ID des Schülers.
	 * @return Die Anzahl an Kollisionen des Schülers.
	 */
	public getOfSchuelerAnzahlKollisionen(pSchuelerID : number) : number {
		let anzahl : Number | null = this._map_schuelerID_kollisionen.get(pSchuelerID);
		if (anzahl === null) 
			throw new NullPointerException("Schüler-ID " + pSchuelerID + " unbekannt!")
		return anzahl.valueOf();
	}

	/**
	 * Liefert die Map die jedem Fach eines Schülers seinen Kurs zuordnet (oder null).
	 * 
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * @return Die Map die jedem Fach eines Schülers seinen Kurs zuordnet (oder null).
	 */
	public getOfSchuelerFachIDKursMap(pSchuelerID : number) : HashMap<Number, GostBlockungsergebnisKurs | null> {
		let mapFachKurs : HashMap<Number, GostBlockungsergebnisKurs | null> | null = this._map_schuelerID_fachID_kurs.get(pSchuelerID);
		if (mapFachKurs === null) 
			throw new NullPointerException("Schüler-ID " + pSchuelerID + " unbekannt!")
		return mapFachKurs;
	}

	/**
	 * Liefert die Map des Schülers, die einer Schiene die Kurse des Schülers zuordnet.
	 *   
	 * @param pSchuelerID Die Datenbank-ID des Schülers.
	 * @return Die Map des Schülers, die der Schiene die Kurse des Schülers zuordnet.
	 */
	public getOfSchuelerSchienenKursmengeMap(pSchuelerID : number) : HashMap<Number, HashSet<GostBlockungsergebnisKurs>> {
		let mapSchuelerKurse : HashMap<Number, HashSet<GostBlockungsergebnisKurs>> | null = this._map_schuelerID_schienenID_kurse.get(pSchuelerID);
		if (mapSchuelerKurse === null) 
			throw new NullPointerException("Schüler-ID " + pSchuelerID + " unbekannt!")
		return mapSchuelerKurse;
	}

	/**
	 * Liefert die Kursmenge des Schülers in der Schiene.
	 * 
	 * @param pSchuelerID Die Datenbank-ID des Schülers.
	 * @param pSchienenID Die Datenbank-ID der Schiene.
	 * @return Die Kursmenge des Schülers in der Schiene.
	 */
	public getOfSchuelerOfSchieneKursmenge(pSchuelerID : number, pSchienenID : number) : HashSet<GostBlockungsergebnisKurs> {
		let kursmenge : HashSet<GostBlockungsergebnisKurs> | null = this.getOfSchuelerSchienenKursmengeMap(pSchuelerID).get(pSchienenID);
		if (kursmenge === null) 
			throw new NullPointerException("Schüler-ID=" + pSchuelerID + ", Schienen-ID=" + pSchienenID + " unbekannt!")
		return kursmenge;
	}

	/**
	 * Ermittelt für den Schüler mit einem Fach den zugeordneten Kurs.
	 * 
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * @param  pFachID     Die Datenbank-ID des Faches.
	 * @return             Der zugeordnete Kurs oder null, falls es keine Zuordnung gibt.
	 */
	public getOfSchuelerOfFachZugeordneterKurs(pSchuelerID : number, pFachID : number) : GostBlockungsergebnisKurs | null {
		if (this.getOfSchuelerFachIDKursMap(pSchuelerID).containsKey(pFachID) === false) 
			throw new NullPointerException("Schüler " + pSchuelerID + " hat das Fach " + pFachID + " nicht gewählt!")
		return this.getOfSchuelerFachIDKursMap(pSchuelerID).get(pFachID);
	}

	/**
	 * Liefert TRUE, falls der Schüler dem Kurs zugeordnet ist.
	 * 
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * @param  pKursID     Die Datenbank-ID des Kurses.
	 * 
	 * @return             TRUE, falls der Schüler dem Kurs zugeordnet ist.
	 */
	public getOfSchuelerOfKursIstZugeordnet(pSchuelerID : number, pKursID : number) : boolean {
		let kurs : GostBlockungsergebnisKurs = this.getKursE(pKursID);
		let kurseOfSchueler : HashSet<GostBlockungsergebnisKurs> = this.getOfSchuelerKursmenge(pSchuelerID);
		return kurseOfSchueler.contains(kurs);
	}

	/**
	 * Ermittelt den Kurs für die angegebene ID. Delegiert den Aufruf an das Eltern-Objekt {@link GostBlockungsdatenManager}.
	 * Erzeugt eine NullPointerException im Fehlerfall, dass die ID nicht bekannt ist.
	 * 
	 * @param  pKursID Die ID des Kurses.
	 * @return Das GostBlockungKurs-Objekt.
	 * @throws NullPointerException im Falle, dass die ID nicht bekannt ist.
	 */
	public getKursG(pKursID : number) : GostBlockungKurs {
		return this._parent.getKurs(pKursID);
	}

	/**
	 * Liefert den Kurs (des Blockungsergebnisses) zur übergebenen ID. <br>
	 * Wirft eine Exception, wenn der ID kein Kurs zugeordnet ist.
	 * 
	 * @param  pKursID Die Datenbank-ID des Kurses.
	 * 
	 * @return         Den Kurs (des Blockungsergebnisses) zur übergebenen ID.
	 */
	public getKursE(pKursID : number) : GostBlockungsergebnisKurs {
		let kurs : GostBlockungsergebnisKurs | null = this._map_kursID_kurs.get(pKursID);
		if (kurs === null) 
			throw new NullPointerException("Kurs-ID " + pKursID + " unbekannt!")
		return kurs;
	}

	/**
	 * Liefert den Namen des Kurses. Der Name wird automatisch erzeugt aus dem Fach, der Kursart und der Nummer,
	 * beispielsweise D-GK1.
	 * 
	 * @param  pKursID Die Datenbank-ID des Kurses.
	 * 
	 * @return         Die Datenbank-ID des Kurses.
	 */
	public getOfKursName(pKursID : number) : String {
		return this._parent.getNameOfKurs(pKursID);
	}

	/**
	 * Liefert TRUE, falls der Kurs der Schiene zugeordnet ist.
	 * 
	 * @param  pKursID     Die Datenbank-ID des Kurses.
	 * @param  pSchienenID Die Datenbank-ID der Schiene.
	 * 
	 * @return             TRUE, falls der Kurs der Schiene zugeordnet ist.
	 */
	public getOfKursOfSchieneIstZugeordnet(pKursID : number, pSchienenID : number) : boolean {
		let schiene : GostBlockungsergebnisSchiene = this.getSchieneE(pSchienenID);
		let schienenOfKurs : HashSet<GostBlockungsergebnisSchiene> = this.getOfKursSchienenmenge(pKursID);
		return schienenOfKurs.contains(schiene);
	}

	/**
	 * Liefert die Menge aller Schüler-IDs, die dem Kurs zugeordnet sind. <br>
	 * Wirft eine Exception, wenn der ID kein Kurs zugeordnet ist.
	 * 
	 * @param pKursID Die Datenbank-ID des Kurses.
	 * @return Die Menge aller Schüler-IDs, die dem Kurs zugeordnet sind.
	 */
	public getOfKursSchuelerIDmenge(pKursID : number) : HashSet<Number> {
		let schuelerIDs : HashSet<Number> | null = this._map_kursID_schuelerIDs.get(pKursID);
		if (schuelerIDs === null) 
			throw new NullPointerException("Kurs-ID " + pKursID + " unbekannt!")
		return schuelerIDs;
	}

	/**
	 * Liefert die Schienenmenge des Kurses.
	 * 
	 * @param pKursID Die Datenbank-ID des Kurses.
	 * @return Die Schienenmenge des Kurses.
	 */
	public getOfKursSchienenmenge(pKursID : number) : HashSet<GostBlockungsergebnisSchiene> {
		let schienenOfKurs : HashSet<GostBlockungsergebnisSchiene> | null = this._map_kursID_schienen.get(pKursID);
		if (schienenOfKurs === null) 
			throw new NullPointerException("Kurs-ID " + pKursID + " unbekannt!")
		return schienenOfKurs;
	}

	/**
	 * Liefert TRUE, falls der Kurs mindestens eine Kollision hat. <br>
	 * Kollision: Der Schüler muss in einer Schiene des Kurses eine Kollision haben.
	 * 
	 * @param  pKursID Die Datenbank-ID des Kurses.
	 * @return TRUE, falls der Kurs mindestens eine Kollision hat.
	 */
	public getOfKursHatKollision(pKursID : number) : boolean {
		for (let schiene of this.getOfKursSchienenmenge(pKursID)) 
			for (let schuelerID of this.getKursE(pKursID).schueler) 
				if (this.getOfSchuelerOfSchieneKursmenge(schuelerID.valueOf(), schiene.id).size() > 1) 
					return true;
		return false;
	}

	/**
	 * Liefert die Anzahl an Schülern des Kurses mit Kollisionen. <br>
	 * Kollision: Der Schüler muss in einer Schiene des Kurses eine Kollision haben.
	 * 
	 * @param  pKursID Die Datenbank-ID des Kurses.
	 * @return Die Anzahl an Schülern des Kurses mit Kollisionen.
	 */
	public getOfKursAnzahlKollisionen(pKursID : number) : number {
		let summe : number = 0;
		for (let schiene of this.getOfKursSchienenmenge(pKursID)) 
			for (let schuelerID of this.getKursE(pKursID).schueler) 
				if (this.getOfSchuelerOfSchieneKursmenge(schuelerID.valueOf(), schiene.id).size() > 1) 
					summe++;
		return summe;
	}

	/**
	 * Liefert die Anzahl an Schienen in denen der Kurs gerade ist.
	 * 
	 * @param kursID Die Datenbank-ID des Kurses.
	 * @return Die Anzahl an Schienen in denen der Kurs gerade ist.
	 */
	public getOfKursAnzahlSchienenIst(kursID : number) : number {
		return this.getOfKursSchienenmenge(kursID).size();
	}

	/**
	 * Liefert die Anzahl an Schienen, die der Kurs haben sollte.
	 * 
	 * @param kursID Die Datenbank-ID des Kurses.
	 * @return Die Anzahl an Schienen, die der Kurs haben sollte.
	 */
	public getOfKursAnzahlSchienenSoll(kursID : number) : number {
		return this.getKursE(kursID).anzahlSchienen;
	}

	/**
	 * Liefert die Menge aller Schüler-IDs des Kurses mit Kollisionen.
	 * 
	 * @param  pKursID Die Datenbank-ID des Kurses.
	 * @return Die Menge aller Schüler-IDs des Kurses mit Kollisionen.
	 */
	public getOfKursSchuelermengeMitKollisionen(pKursID : number) : HashSet<Number> {
		let set : HashSet<Number> = new HashSet();
		for (let schiene of this.getOfKursSchienenmenge(pKursID)) 
			for (let schuelerID of this.getKursE(pKursID).schueler) 
				if (this.getOfSchuelerOfSchieneKursmenge(schuelerID.valueOf(), schiene.id).size() > 1) 
					set.add(schuelerID);
		return set;
	}

	/**
	 * Ermittelt die Schiene für die angegebene ID. Delegiert den Aufruf an den Fächer-Manager des Eltern-Objektes
	 * {@link GostBlockungsdatenManager}. <br>
	 * Erzeugt eine NullPointerException im Fehlerfall, dass die ID nicht bekannt ist.
	 * 
	 * @param pSchienenID Die ID der Schiene
	 * @return Das GostBlockungSchiene-Objekt.
	 * @throws NullPointerException im Falle, dass die ID nicht bekannt ist.
	 */
	public getSchieneG(pSchienenID : number) : GostBlockungSchiene {
		return this._parent.getSchiene(pSchienenID);
	}

	/**
	 * Liefert die Schiene  zur übergebenen ID. <br>
	 * Wirft eine Exception, wenn der ID keine Schiene zugeordnet ist.
	 * 
	 * @param pSchienenID Die Datenbank-ID der Schiene.
	 * @return Die Schiene (des Blockungsergebnisses) zur übergebenen ID.
	 */
	public getSchieneE(pSchienenID : number) : GostBlockungsergebnisSchiene {
		let schiene : GostBlockungsergebnisSchiene | null = this._map_schienenID_schiene.get(pSchienenID);
		if (schiene === null) 
			throw new NullPointerException("Schienen-ID " + pSchienenID + " unbekannt!")
		return schiene;
	}

	/**
	 * Liefert die Schiene mit der übergebenen Schienen-NR. <br>
	 * Wirft eine Exception, wenn eine Schiene mit NR nicht existiert.
	 * 
	 * @param pSchienenNr Die Nummer der Schiene.
	 * @return Die Schiene mit der übergebenen Schienen-NR.
	 */
	public getSchieneEmitNr(pSchienenNr : number) : GostBlockungsergebnisSchiene {
		let schiene : GostBlockungsergebnisSchiene | null = this._map_schienenNr_schiene.get(pSchienenNr);
		if (schiene === null) 
			throw new NullPointerException("Schienen-NR " + pSchienenNr + " unbekannt!")
		return schiene;
	}

	/**
	 * Liefert die Anzahl an Schülern in der Schiene mit der übergebenen ID zurück. <br>
	 * Falls ein Schüler mehrfach in der Schiene ist, wird er mehrfach gezählt!
	 * 
	 * @param pSchienenID Die Datenbank-ID der Schiene.
	 * @return Die Anzahl an Schülern in der Schiene.
	 */
	public getOfSchieneAnzahlSchueler(pSchienenID : number) : number {
		let anzahl : Number | null = this._map_schienenID_schuelerAnzahl.get(pSchienenID);
		if (anzahl === null) 
			throw new NullPointerException("Schienen-ID " + pSchienenID + " unbekannt!")
		return anzahl.valueOf();
	}

	/**
	 * Liefert die Anzahl an Schienen.
	 * 
	 * @return Die Anzahl an Schienen.
	 */
	public getOfSchieneAnzahl() : number {
		return this._map_schienenID_schiene.size();
	}

	/**
	 * TRUE, falls die Schiene mindestens eine Schüler-Kollision hat. 
	 * 
	 * @param pSchienenID Die Datenbank-ID der Schiene.
	 * @return TRUE, falls die Schiene mindestens eine Schüler-Kollision hat.
	 */
	public getOfSchieneHatKollision(pSchienenID : number) : boolean {
		return this.getOfSchieneAnzahlSchuelerMitKollisionen(pSchienenID) > 0;
	}

	/**
	 * Liefert die Anzahl an Schüler-Kollisionen der Schiene zurück. <br>
	 * Ein Schüler, der N>1 Mal in einer Schiene ist, erzeugt N-1 Kollisionen.
	 * 
	 * @param pSchienenID Die Datenbank-ID der Schiene.
	 * @return Die Anzahl an Schüler-Kollisionen in der Schiene.
	 */
	public getOfSchieneAnzahlSchuelerMitKollisionen(pSchienenID : number) : number {
		let anzahl : Number | null = this._map_schienenID_kollisionen.get(pSchienenID);
		if (anzahl === null) 
			throw new NullPointerException("Schienen-ID " + pSchienenID + " unbekannt!")
		return anzahl.valueOf();
	}

	/**
	 * Liefert die Menge an Schüler-IDs, die in der Schiene eine Kollision haben.
	 * 
	 * @param pSchienenID Die Datenbank-ID der Schiene.
	 * @return Die Menge an Schüler-IDs, die in der Schiene eine Kollision haben.
	 */
	public getOfSchieneSchuelermengeMitKollisionen(pSchienenID : number) : HashSet<Number> {
		let set : HashSet<Number> = new HashSet();
		for (let schuelerID of this._map_schuelerID_kollisionen.keySet()) 
			if (this.getOfSchuelerOfSchieneKursmenge(schuelerID.valueOf(), pSchienenID).size() > 1) 
				set.add(schuelerID);
		return set;
	}

	/**
	 * Liefert die Menge an Kursen, die in der Schiene eine Kollision haben.
	 * 
	 * @param pSchienenID Die Datenbank-ID der Schiene.
	 * @return Die Menge an Kursen, die in der Schiene eine Kollision haben.
	 */
	public getOfSchieneAnzahlKursmengeMitKollisionen(pSchienenID : number) : number {
		let summe : number = 0;
		for (let kurs of this.getSchieneE(pSchienenID).kurse) 
			if (this.getOfKursHatKollision(kurs.id)) 
				summe++;
		return summe;
	}

	/**
	 * Liefert die Menge an Kursen, die in der Schiene eine Kollision haben.
	 * 
	 * @param pSchienenID Die Datenbank-ID der Schiene.
	 * @return Die Menge an Kursen, die in der Schiene eine Kollision haben.
	 */
	public getOfSchieneKursmengeMitKollisionen(pSchienenID : number) : HashSet<GostBlockungsergebnisKurs> {
		let set : HashSet<GostBlockungsergebnisKurs> = new HashSet();
		for (let kurs of this.getSchieneE(pSchienenID).kurse) 
			if (this.getOfKursHatKollision(kurs.id)) 
				set.add(kurs);
		return set;
	}

	/**
	 * Liefert eine "FachartID --> Kurse" Map der Schiene.
	 *   
	 * @param pSchienenID Die Datenbank-ID der Schiene.
	 * @return Eine "FachartID --> Kurse" Map der Schiene.
	 */
	public getOfSchieneFachartKursmengeMap(pSchienenID : number) : HashMap<Number, Vector<GostBlockungsergebnisKurs>> {
		let map : HashMap<Number, Vector<GostBlockungsergebnisKurs>> | null = this._map_schienenID_fachartID_kurse.get(pSchienenID);
		if (map === null) 
			throw new NullPointerException("Die Schienen-ID " + pSchienenID + " ist unbekannt!")
		return map;
	}

	/**
	 * Liefert die Map, welche jedem Kurs seine Schülermenge zuordnet.
	 * 
	 * @return Die Map, welche jedem Kurs seine Schülermenge zuordnet.
	 */
	public getMappingKursIDSchuelerIDs() : HashMap<Number, HashSet<Number>> {
		return this._map_kursID_schuelerIDs;
	}

	/**
	 * Liefert die Map, welche jedem Kurs seine Schienenmenge zuordnet.
	 * 
	 * @return Die Map, welche jedem Kurs seine Schienenmenge zuordnet.
	 */
	public getMappingKursIDSchienenmenge() : HashMap<Number, HashSet<GostBlockungsergebnisSchiene>> {
		return this._map_kursID_schienen;
	}

	/**
	 * Liefert eine Menge aller Schüler-IDs mit mindestens einer Kollision.
	 * 
	 * @return Eine Menge aller Schüler-IDs mit mindestens einer Kollision.
	 */
	public getMengeDerSchuelerMitKollisionen() : HashSet<Number> {
		let set : HashSet<Number> = new HashSet();
		for (let schuelerID of this._map_schuelerID_kollisionen.keySet()) 
			if (this.getOfSchuelerHatKollision(schuelerID.valueOf())) 
				set.add(schuelerID);
		return set;
	}

	/**
	 * Liefert eine Menge aller Kurse mit mindestens einer Kollision.
	 * 
	 * @return Eine Menge aller Kurse mit mindestens einer Kollision.
	 */
	public getMengeDerKurseMitKollisionen() : HashSet<GostBlockungsergebnisKurs> {
		let set : HashSet<GostBlockungsergebnisKurs> = new HashSet();
		for (let kurs of this._map_kursID_kurs.values()) 
			if (this.getOfKursHatKollision(kurs.id)) 
				set.add(kurs);
		return set;
	}

	/**
	 * Liefert eine Menge aller Schienen mit mindestens einer Kollision.
	 * 
	 * @return Eine Menge aller Schienen mit mindestens einer Kollision.
	 */
	public getMengeDerSchienenMitKollisionen() : HashSet<GostBlockungsergebnisSchiene> {
		let set : HashSet<GostBlockungsergebnisSchiene> = new HashSet();
		for (let schiene of this._map_schienenID_schiene.values()) 
			if (this.getOfSchieneHatKollision(schiene.id)) 
				set.add(schiene);
		return set;
	}

	/**
	 * Liefert die Menge aller Schienen.
	 * 
	 * @return Die Menge aller Schienen.
	 */
	public getMengeAllerSchienen() : Vector<GostBlockungsergebnisSchiene> {
		return this._ergebnis.schienen;
	}

	/**
	 * Verknüpft einen Kurs mit einer Schiene. Die Schiene wird anhand ihrer Nummer (nicht die Datenbank-ID)
	 * identifiziert.
	 * 
	 * @param  pKursID              Die Datenbank-ID des Kurses.
	 * @param  pSchienenNr          Die Nummer der Schiene (nicht die Datenbank-ID).
	 * 
	 * @throws NullPointerException Falls ein Fehler passiert, z. B. wenn es die Zuordnung bereits gab.
	 */
	public setKursSchienenNr(pKursID : number, pSchienenNr : number) : void {
		let eSchiene : GostBlockungsergebnisSchiene | null = this._map_schienenNr_schiene.get(pSchienenNr);
		if (eSchiene === null) 
			throw new NullPointerException("SchienenNr. " + pSchienenNr + " unbekannt!")
		this.stateKursSchieneHinzufuegen(pKursID, eSchiene.id);
	}

	/**
	 * Verknüpft einen Kurs mit einer Schiene oder hebt die Verknüpfung auf.
	 * 
	 * @param  pKursID                   Die Datenbank-ID des Kurses.
	 * @param  pSchienenID               Die Datenbank-ID der Schiene.
	 * @param  pHinzufuegenOderEntfernen TRUE=Hinzufügen, FALSE=Entfernen
	 * 
	 * @return                           TRUE, falls die jeweilige Operation erfolgreich war.
	 * 
	 * @throws NullPointerException      Falls ein Fehler passiert, z. B. wenn es die Zuordnung bereits gab.
	 */
	public setKursSchiene(pKursID : number, pSchienenID : number, pHinzufuegenOderEntfernen : boolean) : boolean {
		if (pHinzufuegenOderEntfernen) 
			return this.stateKursSchieneHinzufuegen(pKursID, pSchienenID);
		return this.stateKursSchieneEntfernen(pKursID, pSchienenID);
	}

	/**
	 * Verknüpft einen Schüler mit einem Kurs oder hebt die Verknüpfung auf.
	 * 
	 * @param  pSchuelerID               Die Datenbank-ID des Schülers.
	 * @param  pKursID                   Die Datenbank-ID des Kurses.
	 * @param  pHinzufuegenOderEntfernen TRUE=Hinzufügen, FALSE=Entfernen
	 * 
	 * @return                           TRUE, falls die jeweilige Operation erfolgreich war.
	 * 
	 * @throws NullPointerException      Falls ein Fehler passiert, z. B. wenn es die Zuordnung bereits gab.
	 */
	public setSchuelerKurs(pSchuelerID : number, pKursID : number, pHinzufuegenOderEntfernen : boolean) : boolean {
		if (pHinzufuegenOderEntfernen) 
			return this.stateSchuelerKursHinzufuegen(pSchuelerID, pKursID);
		return this.stateSchuelerKursEntfernen(pSchuelerID, pKursID);
	}

	/**
	 * Nur für Debug-Zwecke.
	 */
	public debug() : void {
		console.log(JSON.stringify("----- Kurse sortiert nach Fachart -----"));
		for (let fachartID of this._map_fachartID_kurse.keySet()) {
			console.log(JSON.stringify("FachartID = " + fachartID.valueOf() + " (KD = " + this.getOfFachartKursdifferenz(fachartID.valueOf()) + ")"));
			for (let kurs of this.getOfFachartKursmenge(fachartID.valueOf())) {
				console.log(JSON.stringify("    " + this.getOfKursName(kurs.id).valueOf() + " : " + kurs.schueler.size() + " SuS"));
			}
		}
		console.log(JSON.stringify("KursdifferenzMax = " + this._ergebnis.bewertung.kursdifferenzMax));
		console.log(JSON.stringify("KursdifferenzHistogramm = " + Arrays.toString(this._ergebnis.bewertung.kursdifferenzHistogramm).valueOf()));
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.gost.GostBlockungsergebnisManager'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_gost_GostBlockungsergebnisManager(obj : unknown) : GostBlockungsergebnisManager {
	return obj as GostBlockungsergebnisManager;
}
