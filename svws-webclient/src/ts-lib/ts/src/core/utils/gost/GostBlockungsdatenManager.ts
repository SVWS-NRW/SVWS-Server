import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { GostFach, cast_de_nrw_schule_svws_core_data_gost_GostFach } from '../../../core/data/gost/GostFach';
import { GostFaecherManager, cast_de_nrw_schule_svws_core_utils_gost_GostFaecherManager } from '../../../core/utils/gost/GostFaecherManager';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { GostFachwahl, cast_de_nrw_schule_svws_core_data_gost_GostFachwahl } from '../../../core/data/gost/GostFachwahl';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { GostBlockungRegel, cast_de_nrw_schule_svws_core_data_gost_GostBlockungRegel } from '../../../core/data/gost/GostBlockungRegel';
import { GostKursart, cast_de_nrw_schule_svws_core_types_gost_GostKursart } from '../../../core/types/gost/GostKursart';
import { Comparator, cast_java_util_Comparator } from '../../../java/util/Comparator';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { GostBlockungsdaten, cast_de_nrw_schule_svws_core_data_gost_GostBlockungsdaten } from '../../../core/data/gost/GostBlockungsdaten';
import { Schueler, cast_de_nrw_schule_svws_core_data_schueler_Schueler } from '../../../core/data/schueler/Schueler';
import { NullPointerException, cast_java_lang_NullPointerException } from '../../../java/lang/NullPointerException';
import { GostBlockungSchiene, cast_de_nrw_schule_svws_core_data_gost_GostBlockungSchiene } from '../../../core/data/gost/GostBlockungSchiene';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { GostHalbjahr, cast_de_nrw_schule_svws_core_types_gost_GostHalbjahr } from '../../../core/types/gost/GostHalbjahr';
import { List, cast_java_util_List } from '../../../java/util/List';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';
import { IllegalArgumentException, cast_java_lang_IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';
import { GostBlockungKurs, cast_de_nrw_schule_svws_core_data_gost_GostBlockungKurs } from '../../../core/data/gost/GostBlockungKurs';
import { HashSet, cast_java_util_HashSet } from '../../../java/util/HashSet';

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

	private readonly _map_ID_schueler : HashMap<Number, Schueler> = new HashMap();

	private readonly _map_fachwahlen : HashMap<Number, HashMap<Number, GostFachwahl>> = new HashMap();

	private readonly _compKurs_fach_kursart_kursnummer : Comparator<GostBlockungKurs>;

	private _kurse_sortiert_fach_kursart_kursnummer : Vector<GostBlockungKurs> | null = null;

	private readonly _compKurs_kursart_fach_kursnummer : Comparator<GostBlockungKurs>;

	private _kurse_sortiert_kursart_fach_kursnummer : Vector<GostBlockungKurs> | null = null;

	private _halbjahr : GostHalbjahr;

	private _maxTimeMillis : number = 1000;


	/**
	 *Erstellt einen neuen Manager mit leeren Blockungsdaten und einem leeren Fächer-Manager. 
	 */
	public constructor();

	/**
	 *Erstellt einen neuen Manager mit den angegebenen Blockungsdaten und dem Fächer-Manager
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
			this._daten = new GostBlockungsdaten();
			this._faecherManager = new GostFaecherManager();
			this._halbjahr = GostHalbjahr.EF1;
			this._compKurs_fach_kursart_kursnummer = this.createComparatorKursFachKursartNummer();
			this._compKurs_kursart_fach_kursnummer = this.createComparatorKursKursartFachNummer();
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.nrw.schule.svws.core.data.gost.GostBlockungsdaten')))) && ((typeof __param1 !== "undefined") && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('de.nrw.schule.svws.core.utils.gost.GostFaecherManager'))))) {
			let pDaten : GostBlockungsdaten = cast_de_nrw_schule_svws_core_data_gost_GostBlockungsdaten(__param0);
			let pFaecherManager : GostFaecherManager = cast_de_nrw_schule_svws_core_utils_gost_GostFaecherManager(__param1);
			this._halbjahr = GostHalbjahr.fromIDorException(pDaten.gostHalbjahr);
			this._faecherManager = pFaecherManager;
			this._compKurs_fach_kursart_kursnummer = this.createComparatorKursFachKursartNummer();
			this._compKurs_kursart_fach_kursnummer = this.createComparatorKursKursartFachNummer();
			this._daten = new GostBlockungsdaten();
			this._daten.id = pDaten.id;
			this._daten.name = pDaten.name;
			this._daten.abijahrgang = pDaten.abijahrgang;
			this._daten.gostHalbjahr = pDaten.gostHalbjahr;
			this._daten.istAktiv = pDaten.istAktiv;
			this._daten.vorlageID = pDaten.vorlageID;
			this._daten.ergebnisse.addAll(pDaten.ergebnisse);
			this.addSchienListe(pDaten.schienen);
			this.addRegelListe(pDaten.regeln);
			this.addKursListe(pDaten.kurse);
			this.addSchuelerListe(pDaten.schueler);
			this.addFachwahlListe(pDaten.fachwahlen);
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

	/**
	 *Gibt den Fächer-Manager zurück, der für die Blockungsdaten verwendet wird.
	 * 
	 * @return der Fächer-Manager (siehe {@link GostFaecherManager}) 
	 */
	public faecherManager() : GostFaecherManager {
		return this._faecherManager;
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
		return this._halbjahr;
	}

	/**
	 *Setzt das Halbjahr der gymnasialen Oberstufe, für welches die Blockung angelegt wurde.
	 * 
	 * @param halbjahr das Halbjahr der gymnasialen Oberstufe 
	 */
	public setHalbjahr(halbjahr : GostHalbjahr) : void {
		this._halbjahr = halbjahr;
		this._daten.gostHalbjahr = halbjahr.id;
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
	 * @param id die ID, welche der Blockung zugewiesen wird. 
	 */
	public setID(id : number) : void {
		if (id < 0) 
			throw new IllegalArgumentException("Die Blockungs-ID muss positiv sein und ist daher ungültig.")
		this._daten.id = id;
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
	 * @param name der Name, welcher der Blockung zugewiesen wird. 
	 */
	public setName(name : String) : void {
		if (JavaObject.equalsTranspiler("", (name))) 
			throw new IllegalArgumentException("Ein leerer Name ist für die Blockung nicht zulässig.")
		this._daten.name = name;
	}

	/**
	 *Fügt den übergebenen Kurs zu der Blockung hinzu
	 * 
	 * @param kurs der hinzuzufügende Kurs 
	 */
	public addKurs(kurs : GostBlockungKurs) : void {
		if (this._mapKurse.containsKey(kurs.id)) 
			throw new NullPointerException("Kurs " + kurs.id + " doppelt!")
		this._daten.kurse.add(kurs);
		this._mapKurse.put(kurs.id, kurs);
		this._kurse_sortiert_fach_kursart_kursnummer = null;
		this._kurse_sortiert_kursart_fach_kursnummer = null;
	}

	/**
	 * Fügt die Menge an Kursen hinzu.
	 * 
	 * @param pKurse Die Menge an Kursen.
	 */
	public addKursListe(pKurse : List<GostBlockungKurs>) : void {
		for (let gKurs of pKurse) 
			this.addKurs(gKurs);
	}

	/**
	 *Gibt den Kurs der Blockung anhand von dessen ID zurück.
	 * 
	 * @param  id                   die ID des Kurses
	 * @return                      der Kurs
	 * @throws NullPointerException falls der Kurs nicht in der Blockung existiert 
	 */
	public getKurs(id : number) : GostBlockungKurs {
		let kurs : GostBlockungKurs | null = this._mapKurse.get(id);
		if (kurs === null) 
			throw new NullPointerException("Ein Kurs mit der angegebenen ID existiert nicht in der Blockung.")
		return kurs;
	}

	/**
	 * Liefert eine nach 'Fach, Kursart, Kursnummer' sortierte Kopie der Menge der Kurse.   
	 * 
	 * @return Eine nach 'Fach, Kursart, Kursnummer' sortierte Kopie der Menge der Kurse.
	 */
	public getKursmengeSortiertNachFachKursartNummer() : Vector<GostBlockungKurs> {
		if (this._kurse_sortiert_fach_kursart_kursnummer === null) {
			this._kurse_sortiert_fach_kursart_kursnummer = new Vector(this._daten.kurse);
			this._kurse_sortiert_fach_kursart_kursnummer.sort(this._compKurs_fach_kursart_kursnummer);
		}
		return this._kurse_sortiert_fach_kursart_kursnummer;
	}

	/**
	 * Liefert eine nach 'Kursart, Fach, Kursnummer' sortierte Kopie der Menge der Kurse.   
	 * 
	 * @return Eine nach 'Kursart, Fach, Kursnummer' sortierte Kopie der Menge der Kurse.
	 */
	public getKursmengeSortiertNachKursartFachNummer() : Vector<GostBlockungKurs> {
		if (this._kurse_sortiert_kursart_fach_kursnummer === null) {
			this._kurse_sortiert_kursart_fach_kursnummer = new Vector(this._daten.kurse);
			this._kurse_sortiert_kursart_fach_kursnummer.sort(this._compKurs_kursart_fach_kursnummer);
		}
		return this._kurse_sortiert_kursart_fach_kursnummer;
	}

	/**
	 *Entfernt den Kurs mit der übergebenen ID aus der Blockung
	 * 
	 * @param id die ID des zu entfernenden Kurses 
	 */
	public removeKursByID(id : number) : void {
		let kurs : GostBlockungKurs = this.getKurs(id);
		this._daten.kurse.remove(kurs);
		this._mapKurse.remove(id);
		this._kurse_sortiert_fach_kursart_kursnummer = null;
		this._kurse_sortiert_kursart_fach_kursnummer = null;
	}

	/**
	 *Entfernt den übergebenen Kurs aus der Blockung
	 * 
	 * @param kurs der zu entfernende Kurs 
	 */
	public removeKurs(kurs : GostBlockungKurs) : void {
		this.removeKursByID(kurs.id);
	}

	/**
	 *Fügt die übergebene Schiene zu der Blockung hinzu
	 * 
	 * @param pSchiene die hinzuzufügende Schiene
	 * @throws NullPointerException Falls es eine Schienen-ID-Dopplung gibt. 
	 */
	public addSchiene(pSchiene : GostBlockungSchiene) : void {
		if (this._mapSchienen.containsKey(pSchiene.id)) 
			throw new NullPointerException("Schiene " + pSchiene.id + " doppelt!")
		this._daten.schienen.add(pSchiene);
		this._daten.schienen.sort(GostBlockungsdatenManager.compSchiene);
		this._mapSchienen.put(pSchiene.id, pSchiene);
	}

	/**
	 * Fügt die Menge an Schienen hinzu.
	 * 
	 * @param pSchienen Die Menge an Schienen.
	 */
	public addSchienListe(pSchienen : List<GostBlockungSchiene>) : void {
		for (let schiene of pSchienen) 
			this.addSchiene(schiene);
	}

	/**
	 *Gibt die Schiene der Blockung anhand von deren ID zurück.
	 * 
	 * @param  id                   die ID der Schiene
	 * @return                      die Schiene
	 * @throws NullPointerException falls die Schiene nicht in der Blockung existiert 
	 */
	public getSchiene(id : number) : GostBlockungSchiene {
		let schiene : GostBlockungSchiene | null = this._mapSchienen.get(id);
		if (schiene === null) 
			throw new NullPointerException("Eine Schiene mit der angegebenen ID existiert nicht in der Blockung.")
		return schiene;
	}

	/**
	 *Entfernt die Schiene mit der übergebenen ID aus der Blockung
	 * 
	 * @param id die ID der zu entfernenden Schiene 
	 */
	public removeSchieneByID(id : number) : void {
		let schiene : GostBlockungSchiene = this.getSchiene(id);
		this._daten.schienen.remove(schiene);
		this._mapSchienen.remove(id);
	}

	/**
	 *Entfernt die übergebene Schiene aus der Blockung
	 * 
	 * @param schiene die zu entfernende Schiene 
	 */
	public removeSchiene(schiene : GostBlockungSchiene) : void {
		this.removeSchieneByID(schiene.id);
	}

	/**
	 *Gibt die Default-Anzahl von Schienen zurück, die für die eine neue Blockung verwendet wird.
	 * 
	 * @param  halbjahr das Halbjahr, für welches die Blockung angelegt werden soll
	 * @return          die Anzahl an Schienen für eine Vorauswahl 
	 */
	public static getDefaultSchienenAnzahl(halbjahr : GostHalbjahr) : number {
		return (halbjahr.id < 2) ? 13 : 11;
	}

	/**
	 *Fügt die übergebene Regel zu der Blockung hinzu
	 * 
	 * @param pRegel die hinzuzufügende Regel 
	 */
	public addRegel(pRegel : GostBlockungRegel) : void {
		if (this._mapRegeln.containsKey(pRegel.id)) 
			throw new NullPointerException("Regel " + pRegel.id + " doppelt!")
		this._daten.regeln.add(pRegel);
		this._daten.regeln.sort(GostBlockungsdatenManager.compRegel);
		this._mapRegeln.put(pRegel.id, pRegel);
	}

	/**
	 * Fügt eine Menge an Regeln hinzu.
	 * 
	 * @param regeln Die Menge an Regeln.
	 */
	public addRegelListe(regeln : List<GostBlockungRegel>) : void {
		for (let regel of regeln) 
			this.addRegel(regel);
	}

	/**
	 *Gibt die Regel der Blockung anhand von deren ID zurück.
	 * 
	 * @param  id                   die ID der Regel
	 * @return                      die Regel
	 * @throws NullPointerException falls die Regel nicht in der Blockung existiert 
	 */
	public getRegel(id : number) : GostBlockungRegel {
		let regel : GostBlockungRegel | null = this._mapRegeln.get(id);
		if (regel === null) 
			throw new NullPointerException("Eine Regel mit der angegebenen ID existiert nicht in der Blockung.")
		return regel;
	}

	/**
	 *Entfernt die Regel mit der übergebenen ID aus der Blockung
	 * 
	 * @param id die ID der zu entfernenden Regel 
	 */
	public removeRegelByID(id : number) : void {
		let regel : GostBlockungRegel = this.getRegel(id);
		this._daten.regeln.remove(regel);
		this._mapRegeln.remove(id);
	}

	/**
	 *Entfernt die übergebene Regel aus der Blockung
	 * 
	 * @param regel die zu entfernende Regel 
	 */
	public removeRegel(regel : GostBlockungRegel) : void {
		this.removeRegelByID(regel.id);
	}

	/**
	 *Ermittelt den Schüler für die angegebene ID. Erzeugt eine NullPointerException im Fehlerfall, dass die ID nicht
	 * bekannt ist.
	 * 
	 * @param  id                   die ID des Schülers
	 * @return                      die Daten zu dem Schüler der Blockung
	 * @throws NullPointerException im Falle, dass die ID nicht bekannt ist 
	 */
	public getSchueler(id : number) : Schueler {
		let schueler : Schueler | null = this._map_ID_schueler.get(id);
		if (schueler === null) 
			throw new NullPointerException("ID des Schülers ist nicht bekannt. Dies ist auf inkonsistente Daten oder einen Programmierfehler zurückzuführen.")
		return schueler;
	}

	/**
	 * Fügt einen Schüler hinzu.<br>
	 * Wirft eine Exception, falls es eine Schüler-ID-Dopplung gibt. 
	 * 
	 * @param pSchueler Der Schüler, der hinzugefügt wird.
	 * @throws NullPointerException Falls es eine Schüler-ID-Dopplung gibt.
	 */
	public addSchueler(pSchueler : Schueler) : void {
		if (this._map_ID_schueler.containsKey(pSchueler.id)) 
			throw new NullPointerException("Schüler " + pSchueler.id + " doppelt!")
		this._daten.schueler.add(pSchueler);
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
	 * Fügt eine Fachwahl hinzu.
	 * Wirft eine Exception, falls das Paar (Schüler-ID, FachartID) doppelt existiert. 
	 * 
	 * @param pFachwahl Die Fachwahl, die hinzugefügt wird.
	 * @throws NullPointerException  Falls es eine FachwahlDopplung gibt.
	 */
	public addFachwahl(pFachwahl : GostFachwahl) : void {
		let mapSW : HashMap<Number, GostFachwahl> | null = this._map_fachwahlen.get(pFachwahl.schuelerID);
		if (mapSW === null) {
			mapSW = new HashMap();
			this._map_fachwahlen.put(pFachwahl.schuelerID, mapSW);
		}
		let fachartID : number = GostKursart.getFachartID(pFachwahl);
		if (mapSW.containsKey(fachartID)) 
			throw new NullPointerException("Fachwahl " + pFachwahl.schuelerID + "," + fachartID + " doppelt!")
		mapSW.put(fachartID, pFachwahl);
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

	/**
	 *Liefert die Anzahl an Schienen.
	 * 
	 * @return Die Anzahl an Schienen. 
	 */
	public getSchienenAnzahl() : number {
		return this._mapSchienen.size();
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
	 *Liefert die Anzahl an Fachwahlen.
	 * 
	 * @return Die Anzahl an Fachwahlen. 
	 */
	public getFachwahlAnzahl() : number {
		return this._daten.fachwahlen.size();
	}

	/**
	 *Liefert die Anzahl an Regeln.
	 * 
	 * @return Die Anzahl an Regeln. 
	 */
	public getRegelAnzahl() : number {
		return this._mapRegeln.size();
	}

	/**
	 * Liefert den Namen des Kurses. Der Name wird automatisch erzeugt aus dem Fach, der Kursart und der Nummer,
	 * beispielsweise D-GK1.
	 * 
	 * @param  pKursID Die Datenbank-ID des Kurses.
	 * 
	 * @return         Die Datenbank-ID des Kurses.
	 */
	public getNameOfKurs(pKursID : number) : String {
		let kurs : GostBlockungKurs = this.getKurs(pKursID);
		let gFach : GostFach = this._faecherManager.getOrException(kurs.fach_id);
		return gFach.kuerzel + "-" + GostKursart.fromID(kurs.kursart).kuerzel + kurs.nummer;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.gost.GostBlockungsdatenManager'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_gost_GostBlockungsdatenManager(obj : unknown) : GostBlockungsdatenManager {
	return obj as GostBlockungsdatenManager;
}
