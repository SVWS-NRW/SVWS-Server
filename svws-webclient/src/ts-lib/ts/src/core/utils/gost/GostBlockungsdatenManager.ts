import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { GostBlockungsergebnisListeneintrag, cast_de_nrw_schule_svws_core_data_gost_GostBlockungsergebnisListeneintrag } from '../../../core/data/gost/GostBlockungsergebnisListeneintrag';
import { GostFaecherManager, cast_de_nrw_schule_svws_core_utils_gost_GostFaecherManager } from '../../../core/utils/gost/GostFaecherManager';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { GostBlockungRegel, cast_de_nrw_schule_svws_core_data_gost_GostBlockungRegel } from '../../../core/data/gost/GostBlockungRegel';
import { GostKursart, cast_de_nrw_schule_svws_core_types_gost_GostKursart } from '../../../core/types/gost/GostKursart';
import { Comparator, cast_java_util_Comparator } from '../../../java/util/Comparator';
import { GostKursblockungRegelTyp, cast_de_nrw_schule_svws_core_types_kursblockung_GostKursblockungRegelTyp } from '../../../core/types/kursblockung/GostKursblockungRegelTyp';
import { NullPointerException, cast_java_lang_NullPointerException } from '../../../java/lang/NullPointerException';
import { GostHalbjahr, cast_de_nrw_schule_svws_core_types_gost_GostHalbjahr } from '../../../core/types/gost/GostHalbjahr';
import { JavaIterator, cast_java_util_Iterator } from '../../../java/util/JavaIterator';
import { List, cast_java_util_List } from '../../../java/util/List';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';
import { IllegalArgumentException, cast_java_lang_IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';
import { GostBlockungKurs, cast_de_nrw_schule_svws_core_data_gost_GostBlockungKurs } from '../../../core/data/gost/GostBlockungKurs';
import { HashSet, cast_java_util_HashSet } from '../../../java/util/HashSet';
import { GostFach, cast_de_nrw_schule_svws_core_data_gost_GostFach } from '../../../core/data/gost/GostFach';
import { GostFachwahl, cast_de_nrw_schule_svws_core_data_gost_GostFachwahl } from '../../../core/data/gost/GostFachwahl';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { GostBlockungsergebnis, cast_de_nrw_schule_svws_core_data_gost_GostBlockungsergebnis } from '../../../core/data/gost/GostBlockungsergebnis';
import { GostBlockungsdaten, cast_de_nrw_schule_svws_core_data_gost_GostBlockungsdaten } from '../../../core/data/gost/GostBlockungsdaten';
import { Schueler, cast_de_nrw_schule_svws_core_data_schueler_Schueler } from '../../../core/data/schueler/Schueler';
import { GostBlockungSchiene, cast_de_nrw_schule_svws_core_data_gost_GostBlockungSchiene } from '../../../core/data/gost/GostBlockungSchiene';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { GostBlockungsergebnisComparator, cast_de_nrw_schule_svws_core_utils_gost_GostBlockungsergebnisComparator } from '../../../core/utils/gost/GostBlockungsergebnisComparator';

export class GostBlockungsdatenManager extends JavaObject {

	private static readonly compSchiene : Comparator<GostBlockungSchiene> = { compare : (a: GostBlockungSchiene, b: GostBlockungSchiene) => {
		return JavaInteger.compare(a.nummer, b.nummer);
	} };

	private static readonly compRegel : Comparator<GostBlockungRegel> = { compare : (a: GostBlockungRegel, b: GostBlockungRegel) => {
		let result : number = JavaInteger.compare(a.typ, b.typ);
		if (result !== 0) 
			return result;
		return JavaLong.compare(a.id, b.id);
	} };

	private readonly _daten : GostBlockungsdaten;

	private readonly _faecherManager : GostFaecherManager;

	private readonly _mapKurse : HashMap<Number, GostBlockungKurs> = new HashMap();

	private readonly _mapSchienen : HashMap<Number, GostBlockungSchiene> = new HashMap();

	private readonly _mapRegeln : HashMap<Number, GostBlockungRegel> = new HashMap();

	private readonly _map_id_schueler : HashMap<Number, Schueler> = new HashMap();

	private readonly _map_schuelerID_fachwahlen : HashMap<Number, List<GostFachwahl>> = new HashMap();

	private readonly _map_schulerID_fachID_fachwahl : HashMap<Number, HashMap<Number, GostFachwahl>> = new HashMap();

	private readonly _mapErgebnis : HashMap<Number, GostBlockungsergebnisListeneintrag> = new HashMap();

	private readonly _compKurs_fach_kursart_kursnummer : Comparator<GostBlockungKurs>;

	private _kurse_sortiert_fach_kursart_kursnummer : List<GostBlockungKurs> = new Vector();

	private readonly _compKurs_kursart_fach_kursnummer : Comparator<GostBlockungKurs>;

	private _kurse_sortiert_kursart_fach_kursnummer : List<GostBlockungKurs> = new Vector();

	private readonly _compFachwahlen : Comparator<GostFachwahl>;

	private readonly _compErgebnisse : Comparator<GostBlockungsergebnisListeneintrag> = new GostBlockungsergebnisComparator();

	private _maxTimeMillis : number = 1000;


	/**
	 *
	 * Erstellt einen neuen Manager mit leeren Blockungsdaten und einem leeren Fächer-Manager.
	 */
	public constructor();

	/**
	 *Erstellt einen neuen Manager mit den angegebenen Blockungsdaten und dem Fächer-Manager.
	 * 
	 * @param pDaten          die Blockungsdaten
	 * @param pFaecherManager der Fächer-Manager 
	 */
	public constructor(pDaten : GostBlockungsdaten, pFaecherManager : GostFaecherManager);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : GostBlockungsdaten, __param1? : GostFaecherManager) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined")) {
			this._faecherManager = new GostFaecherManager();
			this._daten = new GostBlockungsdaten();
			this._daten.gostHalbjahr = GostHalbjahr.EF1.id;
			this._compKurs_fach_kursart_kursnummer = this.createComparatorKursFachKursartNummer();
			this._compKurs_kursart_fach_kursnummer = this.createComparatorKursKursartFachNummer();
			this._compFachwahlen = this.createComparatorFachwahlen();
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.nrw.schule.svws.core.data.gost.GostBlockungsdaten')))) && ((typeof __param1 !== "undefined") && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('de.nrw.schule.svws.core.utils.gost.GostFaecherManager'))))) {
			let pDaten : GostBlockungsdaten = cast_de_nrw_schule_svws_core_data_gost_GostBlockungsdaten(__param0);
			let pFaecherManager : GostFaecherManager = cast_de_nrw_schule_svws_core_utils_gost_GostFaecherManager(__param1);
			this._faecherManager = pFaecherManager;
			this._compKurs_fach_kursart_kursnummer = this.createComparatorKursFachKursartNummer();
			this._compKurs_kursart_fach_kursnummer = this.createComparatorKursKursartFachNummer();
			this._compFachwahlen = this.createComparatorFachwahlen();
			this._daten = new GostBlockungsdaten();
			this._daten.id = pDaten.id;
			this._daten.name = pDaten.name;
			this._daten.abijahrgang = pDaten.abijahrgang;
			this._daten.gostHalbjahr = pDaten.gostHalbjahr;
			this._daten.istAktiv = pDaten.istAktiv;
			this.addSchienListe(pDaten.schienen);
			this.addRegelListe(pDaten.regeln);
			this.addKursListe(pDaten.kurse);
			this.addSchuelerListe(pDaten.schueler);
			this.addFachwahlListe(pDaten.fachwahlen);
			this.addErgebnisListe(pDaten.ergebnisse);
		} else throw new Error('invalid method overload');
	}

	private createComparatorKursFachKursartNummer() : Comparator<GostBlockungKurs> {
		let comp : Comparator<GostBlockungKurs> = { compare : (a: GostBlockungKurs, b: GostBlockungKurs) => {
			let aFach : GostFach = this._faecherManager.getOrException(a.fach_id);
			let bFach : GostFach = this._faecherManager.getOrException(b.fach_id);
			let cmpFach : number = GostFaecherManager.comp.compare(aFach, bFach);
			if (cmpFach !== 0) 
				return cmpFach;
			if (a.kursart < b.kursart) 
				return -1;
			if (a.kursart > b.kursart) 
				return +1;
			return JavaInteger.compare(a.nummer, b.nummer);
		} };
		return comp;
	}

	private createComparatorKursKursartFachNummer() : Comparator<GostBlockungKurs> {
		let comp : Comparator<GostBlockungKurs> = { compare : (a: GostBlockungKurs, b: GostBlockungKurs) => {
			if (a.kursart < b.kursart) 
				return -1;
			if (a.kursart > b.kursart) 
				return +1;
			let aFach : GostFach = this._faecherManager.getOrException(a.fach_id);
			let bFach : GostFach = this._faecherManager.getOrException(b.fach_id);
			let cmpFach : number = GostFaecherManager.comp.compare(aFach, bFach);
			if (cmpFach !== 0) 
				return cmpFach;
			return JavaInteger.compare(a.nummer, b.nummer);
		} };
		return comp;
	}

	private createComparatorFachwahlen() : Comparator<GostFachwahl> {
		let comp : Comparator<GostFachwahl> = { compare : (a: GostFachwahl, b: GostFachwahl) => {
			if (a.schuelerID < b.schuelerID) 
				return -1;
			if (a.schuelerID > b.schuelerID) 
				return +1;
			if (a.kursartID < b.kursartID) 
				return -1;
			if (a.kursartID > b.kursartID) 
				return +1;
			let aFach : GostFach = this._faecherManager.getOrException(a.fachID);
			let bFach : GostFach = this._faecherManager.getOrException(b.fachID);
			return GostFaecherManager.comp.compare(aFach, bFach);
		} };
		return comp;
	}

	private getIstBlockungsVorlage() : boolean {
		if (this._daten.ergebnisse.size() !== 1) 
			return false;
		if (this._daten.ergebnisse.get(0).istVorlage === false) 
			return false;
		return true;
	}

	/**
	 * Liefert den Wert des 1. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht genügend gesetzten Kurse. <br>
	 * - Die Anzahl der Regelverletzungen. <br>
	 * 
	 * @param pErgebnisID Die Datenbank-ID des Listeneintrages. 
	 * @return Den Wert des 1. Bewertungskriteriums.
	 */
	public getOfBewertung1Wert(pErgebnisID : number) : number {
		let e : GostBlockungsergebnisListeneintrag = this.getErgebnis(pErgebnisID);
		let summe : number = 0;
		summe += e.bewertung.anzahlKurseNichtZugeordnet;
		summe += e.bewertung.regelVerletzungen.size();
		return summe;
	}

	/**
	 * Liefert eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der Regelverletzungen. <br>
	 * - Die Anzahl der nicht genügend gesetzten Kurse. <br>
	 * 
	 * @param pErgebnisID Die Datenbank-ID des Listeneintrages. 
	 * @return Eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public getOfBewertung1Intervall(pErgebnisID : number) : number {
		let summe : number = this.getOfBewertung1Wert(pErgebnisID);
		return 1 - 1 / (0.25 * summe + 1);
	}

	/**
	 * Liefert den Wert des 2. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht zugeordneten Schülerfachwahlen. <br>
	 * - Die Anzahl der Schülerkollisionen. <br>
	 * 
	 * @param pErgebnisID Die Datenbank-ID des Listeneintrages. 
	 * @return Den Wert des 2. Bewertungskriteriums.
	 */
	public getOfBewertung2Wert(pErgebnisID : number) : number {
		let e : GostBlockungsergebnisListeneintrag = this.getErgebnis(pErgebnisID);
		let summe : number = 0;
		summe += e.bewertung.anzahlSchuelerNichtZugeordnet;
		summe += e.bewertung.anzahlSchuelerKollisionen;
		return summe;
	}

	/**
	 * Liefert eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht zugeordneten Schülerfachwahlen. <br>
	 * - Die Anzahl der Schülerkollisionen. <br>
	 * 
	 * @param pErgebnisID Die Datenbank-ID des Listeneintrages. 
	 * @return Eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public getOfBewertung2Intervall(pErgebnisID : number) : number {
		let summe : number = this.getOfBewertung2Wert(pErgebnisID);
		return 1 - 1 / (0.25 * summe + 1);
	}

	/**
	 * Liefert den Wert des 3. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Größte Kursdifferenz. <br>
	 * Der Wert 0 und 1 werden unterschieden, sind aber von der Bewertung her Äquivalent.
	 * 
	 * @param pErgebnisID Die Datenbank-ID des Listeneintrages. 
	 * @return Den Wert des 3. Bewertungskriteriums.
	 */
	public getOfBewertung3Wert(pErgebnisID : number) : number {
		let e : GostBlockungsergebnisListeneintrag = this.getErgebnis(pErgebnisID);
		return e.bewertung.kursdifferenzMax;
	}

	/**
	 * Liefert eine Güte des 3. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Größte Kursdifferenz. <br>
	 * Der Wert 0 und 1 werden unterschieden, sind aber von der Bewertung her Äquivalent.
	 * 
	 * @param pErgebnisID Die Datenbank-ID des Listeneintrages. 
	 * @return Eine Güte des 3. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public getOfBewertung3Intervall(pErgebnisID : number) : number {
		let wert : number = this.getOfBewertung3Wert(pErgebnisID);
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
	 * @param pErgebnisID Die Datenbank-ID des Listeneintrages. 
	 * @return Den Wert des 4. Bewertungskriteriums.
	 */
	public getOfBewertung4Wert(pErgebnisID : number) : number {
		let e : GostBlockungsergebnisListeneintrag = this.getErgebnis(pErgebnisID);
		return e.bewertung.anzahlKurseMitGleicherFachartProSchiene;
	}

	/**
	 * Liefert eine Güte des 4. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl an Kursen mit gleicher Fachart (Fach, Kursart) in einer Schiene. <br>
	 * Dieses Bewertungskriterium wird teilweise absichtlich verletzt, wenn z. B. Schienen erzeugt werden mit dem selben
	 * Fach (Sport-Schiene). Nichtsdestotrotz möchte man häufig nicht die selben Fächer in einer Schiene, aufgrund von
	 * Raumkapazitäten (Fachräume).
	 * 
	 * @param pErgebnisID Die Datenbank-ID des Listeneintrages. 
	 * @return Eine Güte des 4. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public getOfBewertung4Intervall(pErgebnisID : number) : number {
		let wert : number = this.getOfBewertung4Wert(pErgebnisID);
		return 1 - 1 / (0.25 * wert + 1);
	}

	/**
	 *Gibt den Fächer-Manager zurück, der für die Blockungsdaten verwendet wird.
	 * 
	 * @return der Fächer-Manager (siehe {@link GostFaecherManager}) 
	 */
	public faecherManager() : GostFaecherManager {
		return this._faecherManager;
	}

	/**
	 *Liefert die Anzahl an Fächern.
	 * 
	 * @return Die Anzahl an Fächern. 
	 */
	public getFaecherAnzahl() : number {
		return this._faecherManager.faecher().size();
	}

	/**
	 *Liefert die Anzahl verschiedenen Kursarten. Dies passiert indem über alle Fachwahlen summiert wird.
	 * 
	 * @return Die Anzahl verschiedenen Kursarten. 
	 */
	public getKursartenAnzahl() : number {
		let setKursartenIDs : HashSet<Number> | null = new HashSet();
		for (let fachwahl of this._daten.fachwahlen) 
			setKursartenIDs.add(fachwahl.kursartID);
		return setKursartenIDs.size();
	}

	/**
	 *Gibt die Blockungsdaten zurück.
	 * 
	 * @return die Blockungsdaten (siehe {@link GostBlockungsdaten}) 
	 */
	public daten() : GostBlockungsdaten {
		return this._daten;
	}

	/**
	 *Gibt das Halbjahr der gymnasialen Oberstufe zurück, für welches die Blockung angelegt wurde.
	 * 
	 * @return das Halbjahr der gymnasialen Oberstufe 
	 */
	public getHalbjahr() : GostHalbjahr {
		return GostHalbjahr.fromIDorException(this._daten.gostHalbjahr);
	}

	/**
	 *Setzt das Halbjahr der gymnasialen Oberstufe, für welches die Blockung angelegt wurde.
	 * 
	 * @param pHalbjahr das Halbjahr der gymnasialen Oberstufe 
	 */
	public setHalbjahr(pHalbjahr : GostHalbjahr) : void {
		this._daten.gostHalbjahr = pHalbjahr.id;
	}

	/**
	 *Gibt die ID der Blockung zurück.
	 * 
	 * @return die ID der Blockung 
	 */
	public getID() : number {
		return this._daten.id;
	}

	/**
	 *Setzt die ID der Blockung
	 * 
	 * @param pBlockungsID die ID, welche der Blockung zugewiesen wird. 
	 */
	public setID(pBlockungsID : number) : void {
		if (pBlockungsID < 0) 
			throw new IllegalArgumentException("Die Blockungs-ID ist " + pBlockungsID + ", sie muss aber positiv sein!")
		this._daten.id = pBlockungsID;
	}

	/**
	 *Gibt den Namen der Blockung zurück.
	 * 
	 * @return der Name der Blockung 
	 */
	public getName() : String {
		return this._daten.name;
	}

	/**
	 *Setzt den Namen der Blockung
	 * 
	 * @param pName der Name, welcher der Blockung zugewiesen wird. 
	 */
	public setName(pName : String) : void {
		if (JavaObject.equalsTranspiler("", (pName))) 
			throw new IllegalArgumentException("Ein leerer Name ist für die Blockung nicht zulässig.")
		this._daten.name = pName;
	}

	/**
	 *Liefert die maximale Blockungszeit in Millisekunden.
	 * 
	 * @return Die maximale Blockungszeit in Millisekunden. 
	 */
	public getMaxTimeMillis() : number {
		return this._maxTimeMillis;
	}

	/**
	 *Setzt die maximale Blockungszeit in Millisekunden.
	 * 
	 * @param pZeit die maximale Blockungszeit in Millisekunden. 
	 */
	public setMaxTimeMillis(pZeit : number) : void {
		this._maxTimeMillis = pZeit;
	}

	private addErgebnisOhneSortierung(pErgebnis : GostBlockungsergebnisListeneintrag) : void {
		if (pErgebnis.id < 1) 
			throw new NullPointerException("Ergebnis.id = " + pErgebnis.id + " --> zu gering!")
		if (this._mapErgebnis.containsKey(pErgebnis.id)) 
			throw new NullPointerException("Ergebnis.id =  " + pErgebnis.id + " --> doppelt!")
		if (pErgebnis.blockungID < 1) 
			throw new NullPointerException("Ergebnis.blockungID = " + pErgebnis.blockungID + " --> zu gering!")
		if (GostHalbjahr.fromID(pErgebnis.gostHalbjahr) === null) 
			throw new NullPointerException("Ergebnis.gostHalbjahr = " + pErgebnis.gostHalbjahr + " --> unbekannt!")
		this._daten.ergebnisse.add(pErgebnis);
		this._mapErgebnis.put(pErgebnis.id, pErgebnis);
	}

	/**
	 *
	 * Fügt das übergebenen Ergebnis der Blockung hinzu.
	 * 
	 * @param pErgebnis Das {@link GostBlockungsergebnisListeneintrag}-Objekt, welches hinzugefügt wird.
	 */
	public addErgebnis(pErgebnis : GostBlockungsergebnisListeneintrag) : void {
		this.addErgebnisOhneSortierung(pErgebnis);
		this._daten.ergebnisse.sort(this._compErgebnisse);
	}

	/**
	 * Fügt die Menge an Ergebnissen {@link GostBlockungsergebnisListeneintrag} hinzu.
	 * 
	 * @param pErgebnisse Die Menge an Ergebnissen.
	 */
	public addErgebnisListe(pErgebnisse : List<GostBlockungsergebnisListeneintrag>) : void {
		for (let ergebnis of pErgebnisse) 
			this.addErgebnisOhneSortierung(ergebnis);
		this._daten.ergebnisse.sort(this._compErgebnisse);
	}

	/**
	 * Liefert einen {@link GostBlockungsergebnisListeneintrag} aus der Liste der Ergebnisse.
	 * Wirft eine Exception, falls es keinen Listeneintrag mit dieser ID gibt.
	 * 
	 * @param pErgebnisID           Die Datenbank-ID des Ergebnisses. 
	 * @return                      Liefert einen {@link GostBlockungsergebnisListeneintrag} aus der Liste der Ergebnisse.
	 * @throws NullPointerException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public getErgebnis(pErgebnisID : number) : GostBlockungsergebnisListeneintrag {
		let e : GostBlockungsergebnisListeneintrag | null = this._mapErgebnis.get(pErgebnisID);
		if (e === null) 
			throw new NullPointerException("Ergebnis mit ID = " + pErgebnisID + " nicht vorhanden!")
		return e;
	}

	/**
	 * Liefert eine sortierte Menge der {@link GostBlockungsergebnisListeneintrag} nach ihrer Bewertung.    
	 * 
	 * @return Eine sortierte Menge der {@link GostBlockungsergebnisListeneintrag} nach ihrer Bewertung.
	 */
	public getErgebnisseSortiertNachBewertung() : List<GostBlockungsergebnisListeneintrag> {
		return this._daten.ergebnisse;
	}

	/**
	 *
	 * Entfernt das Ergebnis mit der übergebenen ID aus der Blockung.
	 * 
	 * @param pErgebnisID Die Datenbank-ID des zu entfernenden Ergebnisses.
	 */
	public removeErgebnisByID(pErgebnisID : number) : void {
		let e : GostBlockungsergebnisListeneintrag = this.getErgebnis(pErgebnisID);
		this._daten.ergebnisse.remove(e);
		this._mapErgebnis.remove(pErgebnisID);
	}

	/**
	 * Entfernt das übergebenen Ergebnis aus der Blockung.
	 * 
	 * @param pErgebnis Das zu entfernende Ergebnis.
	 */
	public removeErgebnis(pErgebnis : GostBlockungsergebnisListeneintrag) : void {
		this.removeErgebnisByID(pErgebnis.id);
	}

	/**
	 * Aktualisiert die Bewertung im {@link GostBlockungsdatenManager} mit der aus dem {@link GostBlockungsergebnis}. <br>
	 * Wirft eine Exception, falls kein  {@link GostBlockungsergebnisListeneintrag} mit der ID gefunden wurde.
	 *  
	 * @param pErgebnis              Das Ergebnis mit der neuen Bewertung.
	 * @throws NullPointerException  Falls kein  {@link GostBlockungsergebnisListeneintrag} mit der ID gefunden wurde.
	 */
	public updateErgebnisBewertung(pErgebnis : GostBlockungsergebnis) : void {
		if (pErgebnis.id < 0) 
			throw new NullPointerException("GostBlockungsergebnis.id=" + pErgebnis.id + " zu klein!")
		if (pErgebnis.blockungID < 0) 
			throw new NullPointerException("GostBlockungsergebnis.blockungID=" + pErgebnis.blockungID + " zu klein!")
		for (let eintrag of this._daten.ergebnisse) 
			if (eintrag.id === pErgebnis.id) 
				eintrag.bewertung = pErgebnis.bewertung;
		this._daten.ergebnisse.sort(this._compErgebnisse);
	}

	private addKursOhneSortierung(pKurs : GostBlockungKurs) : void {
		if (pKurs.id < 0) 
			throw new NullPointerException("GostBlockungKurs.id=" + pKurs.id + " zu klein!")
		if (this._mapKurse.containsKey(pKurs.id)) 
			throw new NullPointerException("GostBlockungKurs.id =  " + pKurs.id + " --> doppelt!")
		if (pKurs.anzahlSchienen < 1) 
			throw new NullPointerException("GostBlockungKurs.anzahlSchienen = " + pKurs.anzahlSchienen + " --> zu gering!")
		let nSchienen : number = this.getSchienenAnzahl();
		if (pKurs.anzahlSchienen > nSchienen) 
			throw new NullPointerException("GostBlockungKurs.anzahlSchienen = " + nSchienen + " --> zu groß!")
		if (pKurs.nummer < 1) 
			throw new NullPointerException("GostBlockungKurs.nummer = " + pKurs.nummer + " --> zu gering!")
		if (this._faecherManager.get(pKurs.fach_id) === null) 
			throw new NullPointerException("GostBlockungKurs.fach_id = " + pKurs.fach_id + " --> unbekannt!")
		if (GostKursart.fromIDorNull(pKurs.kursart) === null) 
			throw new NullPointerException("GostBlockungKurs.kursart = " + pKurs.kursart + " --> unbekannt!")
		if (pKurs.wochenstunden < 0) 
			throw new NullPointerException("GostBlockungKurs.wochenstunden = " + pKurs.wochenstunden + " --> zu gering!")
		this._daten.kurse.add(pKurs);
		this._mapKurse.put(pKurs.id, pKurs);
		this._kurse_sortiert_fach_kursart_kursnummer.add(pKurs);
		this._kurse_sortiert_kursart_fach_kursnummer.add(pKurs);
	}

	/**
	 *
	 * Fügt den übergebenen Kurs zu der Blockung hinzu.
	 * 
	 * @param pKurs Das {@link GostBlockungKurs}-Objekt, welches hinzugefügt wird.
	 */
	public addKurs(pKurs : GostBlockungKurs) : void {
		this.addKursOhneSortierung(pKurs);
		this._kurse_sortiert_fach_kursart_kursnummer.sort(this._compKurs_fach_kursart_kursnummer);
		this._kurse_sortiert_kursart_fach_kursnummer.sort(this._compKurs_kursart_fach_kursnummer);
	}

	/**
	 * Fügt die Menge an Kursen hinzu.
	 * 
	 * @param pKurse Die Menge an Kursen.
	 */
	public addKursListe(pKurse : List<GostBlockungKurs>) : void {
		for (let gKurs of pKurse) 
			this.addKursOhneSortierung(gKurs);
		this._kurse_sortiert_fach_kursart_kursnummer.sort(this._compKurs_fach_kursart_kursnummer);
		this._kurse_sortiert_kursart_fach_kursnummer.sort(this._compKurs_kursart_fach_kursnummer);
	}

	/**
	 *Gibt den Kurs der Blockung anhand von dessen ID zurück.
	 * 
	 * @param  pKursID                   die ID des Kurses
	 * @return                      der Kurs
	 * @throws NullPointerException falls der Kurs nicht in der Blockung existiert 
	 */
	public getKurs(pKursID : number) : GostBlockungKurs {
		let kurs : GostBlockungKurs | null = this._mapKurse.get(pKursID);
		if (kurs === null) 
			throw new NullPointerException("Kurs mit ID = " + pKursID + " existiert nicht in der Blockung!")
		return kurs;
	}

	/**
	 * Liefert TRUE, falls der Kurs mit der übergebenen ID existiert.
	 * 
	 * @param pKursID  Die Datenbank-ID des Kurses.
	 * @return         TRUE, falls der Kurs mit der übergebenen ID existiert.
	 */
	public getKursExistiert(pKursID : number) : boolean {
		let kurs : GostBlockungKurs | null = this._mapKurse.get(pKursID);
		return kurs !== null;
	}

	/**
	 *Liefert die Anzahl an Kursen.
	 * 
	 * @return Die Anzahl an Kursen.
	 */
	public getKursAnzahl() : number {
		return this._mapKurse.size();
	}

	/**
	 * Liefert den Namen des Kurses der Form [Fach]+[Kursart][Kursnummer], beispielsweise D-GK1.
	 * 
	 * @param  pKursID  Die Datenbank-ID des Kurses.
	 * @return          Den Namen des Kurses der Form [Fach]+[Kursart][Kursnummer], beispielsweise D-GK1.
	 */
	public getNameOfKurs(pKursID : number) : String {
		let kurs : GostBlockungKurs = this.getKurs(pKursID);
		let gFach : GostFach = this._faecherManager.getOrException(kurs.fach_id);
		return gFach.kuerzel + "-" + GostKursart.fromID(kurs.kursart).kuerzel + kurs.nummer;
	}

	/**
	 * Liefert eine nach 'Fach, Kursart, Kursnummer' sortierte Kopie der Menge der Kurse.   
	 * 
	 * @return Eine nach 'Fach, Kursart, Kursnummer' sortierte Kopie der Menge der Kurse.
	 */
	public getKursmengeSortiertNachFachKursartNummer() : List<GostBlockungKurs> {
		return this._kurse_sortiert_fach_kursart_kursnummer;
	}

	/**
	 * Liefert eine nach 'Kursart, Fach, Kursnummer' sortierte Kopie der Menge der Kurse.   
	 * 
	 * @return Eine nach 'Kursart, Fach, Kursnummer' sortierte Kopie der Menge der Kurse.
	 */
	public getKursmengeSortiertNachKursartFachNummer() : List<GostBlockungKurs> {
		return this._kurse_sortiert_kursart_fach_kursnummer;
	}

	/**
	 *
	 * Liefert TRUE, falls ein Löschen des Kurses erlaubt ist. <br>
	 * Kriterium: Der Kurs muss existieren und das aktuelle Ergebnis muss eine Vorlage sein.
	 * 
	 * @param  pKursID               Die Datenbank-ID des Kurses.
	 * @return                       TRUE, falls ein Löschen des Kurses erlaubt ist.
	 * @throws NullPointerException  Falls der Kurs nicht existiert.
	 */
	public removeKursAllowed(pKursID : number) : boolean {
		return (this.getKurs(pKursID) !== null) && this.getIstBlockungsVorlage();
	}

	/**
	 *
	 * Entfernt den Kurs mit der übergebenen ID aus der Blockung.
	 * 
	 * @param pKursID Die Datenbank-ID des zu entfernenden Kurses.
	 */
	public removeKursByID(pKursID : number) : void {
		if (this.getIstBlockungsVorlage() === false) 
			throw new NullPointerException("Ein Löschen des Kurses ist nur bei einer Blockungsvorlage erlaubt!")
		let kurs : GostBlockungKurs = this.getKurs(pKursID);
		this._kurse_sortiert_fach_kursart_kursnummer.remove(kurs);
		this._kurse_sortiert_kursart_fach_kursnummer.remove(kurs);
		this._mapKurse.remove(pKursID);
		this._daten.kurse.remove(kurs);
	}

	/**
	 *
	 * Entfernt den übergebenen Kurs aus der Blockung.
	 * 
	 * @param pKurs  Der zu entfernende Kurs. 
	 */
	public removeKurs(pKurs : GostBlockungKurs) : void {
		this.removeKursByID(pKurs.id);
	}

	private addSchieneOhneSortierung(pSchiene : GostBlockungSchiene) : void {
		if (pSchiene.id < 1) 
			throw new NullPointerException("GostBlockungSchiene.id =  " + pSchiene.id + " --> zu klein!")
		if (JavaObject.equalsTranspiler("", (pSchiene.bezeichnung))) 
			throw new NullPointerException("GostBlockungSchiene.bezeichnung darf nicht leer sein!")
		if (pSchiene.nummer < 1) 
			throw new NullPointerException("GostBlockungSchiene.nummer =  " + pSchiene.nummer + " --> zu klein!")
		if (pSchiene.wochenstunden < 1) 
			throw new NullPointerException("GostBlockungSchiene.wochenstunden =  " + pSchiene.wochenstunden + " --> zu klein!")
		if (this._mapSchienen.containsKey(pSchiene.id)) 
			throw new NullPointerException("GostBlockungSchiene " + pSchiene.id + " doppelt!")
		this._mapSchienen.put(pSchiene.id, pSchiene);
		this._daten.schienen.add(pSchiene);
	}

	/**
	 *
	 * Fügt die übergebene Schiene zu der Blockung hinzu.
	 * 
	 * @param pSchiene               Die hinzuzufügende Schiene
	 * @throws NullPointerException  Falls es eine Schienen-ID-Dopplung gibt. 
	 */
	public addSchiene(pSchiene : GostBlockungSchiene) : void {
		this.addSchieneOhneSortierung(pSchiene);
		this._daten.schienen.sort(GostBlockungsdatenManager.compSchiene);
	}

	/**
	 * Fügt die Menge an Schienen hinzu.
	 * 
	 * @param pSchienen Die Menge an Schienen.
	 */
	public addSchienListe(pSchienen : List<GostBlockungSchiene>) : void {
		for (let schiene of pSchienen) 
			this.addSchieneOhneSortierung(schiene);
		this._daten.schienen.sort(GostBlockungsdatenManager.compSchiene);
	}

	/**
	 *
	 * Gibt die Schiene der Blockung anhand von deren ID zurück.
	 * 
	 * @param  pSchienenID          Die Datenbank-ID der Schiene.
	 * @return                      die Schiene
	 * @throws NullPointerException falls die Schiene nicht in der Blockung existiert
	 */
	public getSchiene(pSchienenID : number) : GostBlockungSchiene {
		let schiene : GostBlockungSchiene | null = this._mapSchienen.get(pSchienenID);
		if (schiene === null) 
			throw new NullPointerException("Eine Schiene mit der angegebenen ID existiert nicht in der Blockung.")
		return schiene;
	}

	/**
	 * Liefert TRUE, falls eine Schiene mit der übergebenen ID existiert.
	 * 
	 * @param pSchienenID  Die Datenbank-ID der Schiene.
	 * @return             TRUE, falls eine Schiene mit der übergebenen ID existiert.
	 */
	public getSchieneExistiert(pSchienenID : number) : boolean {
		return this._mapSchienen.get(pSchienenID) !== null;
	}

	/**
	 *
	 * Liefert die Anzahl an Schienen.
	 * 
	 * @return Die Anzahl an Schienen. 
	 */
	public getSchienenAnzahl() : number {
		return this._mapSchienen.size();
	}

	/**
	 * Liefert die aktuelle Menge aller Schienen. 
	 * Das ist die interne Referenz zur Liste der Schienen im {@link GostBlockungsdaten}-Objekt. 
	 * Diese Liste ist stets sortiert nach der Schienen-Nummer.
	 * 
	 * @return Die aktuelle Menge aller Schienen sortiert nach der Schienen-Nummer.
	 */
	public getMengeOfSchienen() : List<GostBlockungSchiene> {
		return this._daten.schienen;
	}

	/**
	 *
	 * Liefert TRUE, falls ein Löschen der Schiene erlaubt ist. <br>
	 * Kriterium: Die Schiene muss existieren und das aktuelle Ergebnis muss eine Vorlage sein.
	 * 
	 * @param  pSchienenID           Die Datenbank-ID der Schiene.
	 * @return                       TRUE, falls ein Löschen der Schiene erlaubt ist.
	 * @throws NullPointerException  Falls die Schiene nicht existiert.
	 */
	public removeSchieneAllowed(pSchienenID : number) : boolean {
		return (this.getSchiene(pSchienenID) !== null) && this.getIstBlockungsVorlage();
	}

	/**
	 *
	 * Entfernt die Schiene mit der übergebenen ID aus der Blockung.
	 * Konsequenz: <br>
	 * (1) Das Löschen der Schiene muss erlaubt sein, sonst Exception.
	 * (2) Die Schienen werden neu nummeriert. <br> 
	 * (3) Die Konsistenz der sortierten Schienen muss überprüft werden. <br>
	 * (4) Die Regeln müssen bei Schienen-Nummern angepasst werden. <br>
	 * 
	 * @param pSchienenID           Die Datenbank-ID der zu entfernenden Schiene.
	 * @throws NullPointerException Falls die Schiene nicht existiert oder ein Löschen nicht erlaubt ist.
	 */
	public removeSchieneByID(pSchienenID : number) : void {
		if (this.getIstBlockungsVorlage() === false) 
			throw new NullPointerException("Ein Löschen einer Schiene ist nur bei einer Blockungsvorlage erlaubt!")
		let schieneR : GostBlockungSchiene = this.getSchiene(pSchienenID);
		this._mapSchienen.remove(pSchienenID);
		this._daten.schienen.remove(schieneR);
		for (let schiene of this._daten.schienen) 
			if (schiene.nummer > schieneR.nummer) 
				schiene.nummer--;
		for (let index : number = 0; index < this._daten.schienen.size(); index++)
			if (this._daten.schienen.get(index).nummer !== index + 1) 
				throw new NullPointerException("Schiene am Index " + index + " hat nicht Nr. " + (index + 1) + "!")
		let iRegel : JavaIterator<GostBlockungRegel> | null = this._daten.regeln.iterator();
		while (iRegel.hasNext()) {
			let r : GostBlockungRegel = iRegel.next();
			let a : Array<number> | null = GostKursblockungRegelTyp.getNeueParameterBeiSchienenLoeschung(r, schieneR.nummer);
			if (a === null) 
				iRegel.remove(); else 
				for (let i : number = 0; i < a.length; i++)
					r.parameter.set(i, a[i]);
		}
	}

	/**
	 *
	 * Entfernt die übergebene Schiene aus der Blockung.
	 * 
	 * @param pSchiene Die zu entfernende Schiene. 
	 */
	public removeSchiene(pSchiene : GostBlockungSchiene) : void {
		this.removeSchieneByID(pSchiene.id);
	}

	/**
	 *
	 * Gibt die Default-Anzahl von Schienen zurück, die für die eine neue Blockung verwendet wird.
	 * 
	 * @param  pHalbjahr  Das Halbjahr, für welches die Blockung angelegt werden soll.
	 * @return            Die Anzahl an Schienen für eine Vorauswahl. 
	 */
	public static getDefaultSchienenAnzahl(pHalbjahr : GostHalbjahr) : number {
		return (pHalbjahr.id < 2) ? 13 : 11;
	}

	private addRegelOhneSortierung(pRegel : GostBlockungRegel) : void {
		if (pRegel.id < 1) 
			throw new NullPointerException("Regel.id =  " + pRegel.id + " --> zu klein!")
		if (this._mapRegeln.containsKey(pRegel.id)) 
			throw new NullPointerException("Regel.id = " + pRegel.id + " --> doppelt!")
		if (GostKursblockungRegelTyp.fromTyp(pRegel.typ) as unknown === GostKursblockungRegelTyp.UNDEFINIERT as unknown) 
			throw new NullPointerException("Regel.typ = " + pRegel.typ + " --> unbekannt!")
		this._daten.regeln.add(pRegel);
		this._mapRegeln.put(pRegel.id, pRegel);
	}

	/**
	 *Fügt die übergebene Regel zu der Blockung hinzu
	 * 
	 * @param pRegel die hinzuzufügende Regel 
	 */
	public addRegel(pRegel : GostBlockungRegel) : void {
		this.addRegelOhneSortierung(pRegel);
		this._daten.regeln.sort(GostBlockungsdatenManager.compRegel);
	}

	/**
	 * Fügt eine Menge an Regeln hinzu.
	 * 
	 * @param pRegeln Die Menge an Regeln.
	 */
	public addRegelListe(pRegeln : List<GostBlockungRegel>) : void {
		for (let regel of pRegeln) 
			this.addRegelOhneSortierung(regel);
		this._daten.regeln.sort(GostBlockungsdatenManager.compRegel);
	}

	/**
	 *
	 * Gibt die Regel der Blockung anhand von deren ID zurück.
	 * 
	 * @param  pRegelID              Die Datenbank-ID der Regel.
	 * @return                       Das GostBlockungRegel-Objekt.
	 * @throws NullPointerException  Falls die Regel nicht in der Blockung existiert.
	 */
	public getRegel(pRegelID : number) : GostBlockungRegel {
		let regel : GostBlockungRegel | null = this._mapRegeln.get(pRegelID);
		if (regel === null) 
			throw new NullPointerException("Regel.id = " + pRegelID + " existiert nicht in der Blockung.")
		return regel;
	}

	/**
	 * Liefert TRUE, falls die Regel mit der übergebenen ID existiert.
	 * 
	 * @param pRegelID  Die Datenbank-ID der Regel.
	 * @return          TRUE, falls die Regel mit der übergebenen ID existiert.
	 */
	public getRegelExistiert(pRegelID : number) : boolean {
		return this._mapRegeln.get(pRegelID) !== null;
	}

	/**
	 *
	 * Liefert die Anzahl an Regeln.
	 * 
	 * @return Die Anzahl an Regeln. 
	 */
	public getRegelAnzahl() : number {
		return this._mapRegeln.size();
	}

	/**
	 * Liefert die aktuelle Menge aller Regeln. 
	 * Das ist die interne Referenz zur Liste der Regeln im {@link GostBlockungsdaten}-Objekt. 
	 * Diese Liste ist stets sortiert nach (TYP, ID).
	 * 
	 * @return Die aktuelle Menge aller Regeln sortiert nach (TYP, id).
	 */
	public getMengeOfRegeln() : List<GostBlockungRegel> {
		return this._daten.regeln;
	}

	/**
	 *
	 * Liefert TRUE, falls ein Löschen der Regel erlaubt ist. <br>
	 * Kriterium: Die Regel muss existieren und das aktuelle Ergebnis muss eine Vorlage sein.
	 * 
	 * @param  pRegelID              Die Datenbank-ID der Regel.
	 * @return                       TRUE, falls ein Löschen der Regel erlaubt ist.
	 * @throws NullPointerException  Falls die Regel nicht existiert.
	 */
	public removeRegelAllowed(pRegelID : number) : boolean {
		return (this.getRegel(pRegelID) !== null) && this.getIstBlockungsVorlage();
	}

	/**
	 *
	 * Entfernt die Regel mit der übergebenen ID aus der Blockung.
	 * Wirft eine Exception, falls es sich nicht um eine Blockungsvorlage handelt.
	 * 
	 * @param pRegelID              Die Datenbank-ID der zu entfernenden Regel. 
	 * @throws NullPointerException Falls es sich nicht um eine Blockungsvorlage handelt.
	 */
	public removeRegelByID(pRegelID : number) : void {
		if (this.getIstBlockungsVorlage() === false) 
			throw new NullPointerException("Ein Löschen einer Regel ist nur bei einer Blockungsvorlage erlaubt!")
		let regel : GostBlockungRegel = this.getRegel(pRegelID);
		this._mapRegeln.remove(pRegelID);
		this._daten.regeln.remove(regel);
	}

	/**
	 *
	 * Entfernt die übergebene Regel aus der Blockung.
	 * 
	 * @param regel die zu entfernende Regel 
	 */
	public removeRegel(regel : GostBlockungRegel) : void {
		this.removeRegelByID(regel.id);
	}

	/**
	 * Fügt einen Schüler hinzu.<br>
	 * Wirft eine Exception, falls es eine Schüler-ID-Dopplung gibt. 
	 * 
	 * @param pSchueler             Der Schüler, der hinzugefügt wird.
	 * @throws NullPointerException Falls es eine Schüler-ID-Dopplung gibt.
	 */
	public addSchueler(pSchueler : Schueler) : void {
		if (pSchueler.id < 1) 
			throw new NullPointerException("Schueler.id =  " + pSchueler.id + " --> zu klein!")
		if (this._map_id_schueler.containsKey(pSchueler.id)) 
			throw new NullPointerException("Schueler.id =  " + pSchueler.id + " --> doppelt!")
		if (pSchueler.geschlecht < 0) 
			throw new NullPointerException("Schueler.geschlecht =  " + pSchueler.geschlecht + " --> zu klein!")
		this._daten.schueler.add(pSchueler);
		this._map_id_schueler.put(pSchueler.id, pSchueler);
		if (this._map_schuelerID_fachwahlen.containsKey(pSchueler.id) === false) 
			this._map_schuelerID_fachwahlen.put(pSchueler.id, new Vector());
		if (this._map_schulerID_fachID_fachwahl.containsKey(pSchueler.id) === false) 
			this._map_schulerID_fachID_fachwahl.put(pSchueler.id, new HashMap());
	}

	/**
	 * Fügt alle Schüler hinzu.
	 * 
	 * @param pSchueler Die Menge an Schülern.
	 */
	public addSchuelerListe(pSchueler : List<Schueler>) : void {
		for (let schueler of pSchueler) 
			this.addSchueler(schueler);
	}

	/**
	 *
	 * Ermittelt den Schüler für die angegebene ID. <br>
	 * Erzeugt eine NullPointerException im Fehlerfall, dass die ID nicht bekannt ist.
	 * 
	 * @param  pSchuelerID           Die Datenbank-ID des Schülers.
	 * @return                       Das {@link Schueler}-Objekt.
	 * @throws NullPointerException  Falls die ID nicht existiert. 
	 */
	public getSchueler(pSchuelerID : number) : Schueler {
		let schueler : Schueler | null = this._map_id_schueler.get(pSchuelerID);
		if (schueler === null) 
			throw new NullPointerException("Schüler-ID = " + pSchuelerID + " existiert nicht!")
		return schueler;
	}

	/**
	 * Liefert nur die Anzahl an Schülern, die mindestens eine Fachwahl haben.
	 * 
	 * @return die Anzahl an Schülern, die mindestens eine Fachwahl haben.
	 */
	public getSchuelerAnzahlMitFachwahlen() : number {
		let setSchuelerIDs : HashSet<Number> | null = new HashSet();
		for (let fachwahl of this._daten.fachwahlen) 
			setSchuelerIDs.add(fachwahl.schuelerID);
		return setSchuelerIDs.size();
	}

	/**
	 * Liefert die Anzahl an Schülern.
	 * 
	 * @return Die Anzahl an Schülern.
	 */
	public getSchuelerAnzahl() : number {
		return this._daten.schueler.size();
	}

	/**
	 * Liefert zu (Schüler, Fach) die jeweilige Kursart. <br>
	 * Wirft eine Exception, falls der Schüler das Fach nicht gewählt hat.
	 * 
	 * @param pSchuelerID            Die Datenbank-ID des Schülers.
	 * @param pFachID                Die Datenbank-ID des Faches.
	 * @return                       Die zu (Schüler, Fach) jeweilige {@link GostKursart}.
	 * @throws NullPointerException  Falls der Schüler das Fach nicht gewählt hat.
	 */
	public getOfSchuelerOfFachKursart(pSchuelerID : number, pFachID : number) : GostKursart {
		let fachwahl : GostFachwahl = this.getOfSchuelerOfFachFachwahl(pSchuelerID, pFachID);
		return GostKursart.fromID(fachwahl.kursartID);
	}

	/**
	 * Liefert zu (Schüler, Fach) die jeweilige Fachwahl. <br>
	 * Wirft eine Exception, falls der Schüler das Fach nicht gewählt hat.
	 * 
	 * @param pSchuelerID            Die Datenbank-ID des Schülers.
	 * @param pFachID                Die Datenbank-ID des Faches.
	 * @return                       Die zu (Schüler, Fach) jeweilige {@link GostFachwahl}.
	 * @throws NullPointerException  Falls der Schüler das Fach nicht gewählt hat.
	 */
	public getOfSchuelerOfFachFachwahl(pSchuelerID : number, pFachID : number) : GostFachwahl {
		let mapFachFachwahl : HashMap<Number, GostFachwahl> | null = this._map_schulerID_fachID_fachwahl.get(pSchuelerID);
		if (mapFachFachwahl === null) 
			throw new NullPointerException("Schüler-ID=" + pSchuelerID + " unbekannt!")
		let fachwahl : GostFachwahl | null = mapFachFachwahl.get(pFachID);
		if (fachwahl === null) 
			throw new NullPointerException("Schüler-ID=" + pSchuelerID + ", Fach-ID=" + pFachID + " unbekannt!")
		return fachwahl;
	}

	/**
	 * Liefert die Menge aller {@link GostFachwahl} des Schülers. 
	 * Diese Liste ist stets sortiert nach (KURSART, FACH.sortierung).
	 * 
	 * @param pSchuelerID Die Datenbank-ID des Schülers.
	 * @return Die Menge aller {@link GostFachwahl} des Schülers, sortiert (KURSART, FACH.sortierung).
	 */
	public getOfSchuelerFacharten(pSchuelerID : number) : List<GostFachwahl> {
		let fachwahlenDesSchuelers : List<GostFachwahl> | null = this._map_schuelerID_fachwahlen.get(pSchuelerID);
		if (fachwahlenDesSchuelers === null) 
			throw new NullPointerException("Schüler-ID=" + pSchuelerID + " unbekannt!")
		return fachwahlenDesSchuelers;
	}

	/**
	 * Fügt eine Fachwahl hinzu.
	 * Wirft eine Exception, falls das Paar (Schüler-ID, FachartID) doppelt existiert. 
	 * 
	 * @param pFachwahl              Die Fachwahl, die hinzugefügt wird.
	 * @throws NullPointerException  Falls es eine FachwahlDopplung gibt.
	 */
	public addFachwahl(pFachwahl : GostFachwahl) : void {
		let mapSFA : HashMap<Number, GostFachwahl> | null = this._map_schulerID_fachID_fachwahl.get(pFachwahl.schuelerID);
		if (mapSFA === null) {
			mapSFA = new HashMap();
			this._map_schulerID_fachID_fachwahl.put(pFachwahl.schuelerID, mapSFA);
		}
		let fachID : number = pFachwahl.fachID;
		if (mapSFA.put(fachID, pFachwahl) !== null) 
			throw new NullPointerException("Schüler-ID=" + pFachwahl.schuelerID + ", Fach-ID=" + fachID + " doppelt!")
		let fachwahlenDesSchuelers : List<GostFachwahl> | null = this._map_schuelerID_fachwahlen.get(pFachwahl.schuelerID);
		if (fachwahlenDesSchuelers === null) {
			fachwahlenDesSchuelers = new Vector();
			this._map_schuelerID_fachwahlen.put(pFachwahl.schuelerID, fachwahlenDesSchuelers);
		}
		fachwahlenDesSchuelers.add(pFachwahl);
		fachwahlenDesSchuelers.sort(this._compFachwahlen);
		this._daten.fachwahlen.add(pFachwahl);
	}

	/**
	 * Fügt alle Fachwahlen hinzu.
	 * 
	 * @param pFachwahlen Die Menge an Fachwahlen. 
	 */
	public addFachwahlListe(pFachwahlen : List<GostFachwahl>) : void {
		for (let gFachwahl of pFachwahlen) 
			this.addFachwahl(gFachwahl);
	}

	/**
	 *Liefert die Anzahl an Fachwahlen.
	 * 
	 * @return Die Anzahl an Fachwahlen.
	 */
	public getFachwahlAnzahl() : number {
		return this._daten.fachwahlen.size();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.gost.GostBlockungsdatenManager'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_gost_GostBlockungsdatenManager(obj : unknown) : GostBlockungsdatenManager {
	return obj as GostBlockungsdatenManager;
}
