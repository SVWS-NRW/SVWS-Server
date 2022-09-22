import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { GostFach, cast_de_nrw_schule_svws_core_data_gost_GostFach } from '../../../core/data/gost/GostFach';
import { GostFaecherManager, cast_de_nrw_schule_svws_core_utils_gost_GostFaecherManager } from '../../../core/utils/gost/GostFaecherManager';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { GostBlockungRegel, cast_de_nrw_schule_svws_core_data_gost_GostBlockungRegel } from '../../../core/data/gost/GostBlockungRegel';
import { Comparator, cast_java_util_Comparator } from '../../../java/util/Comparator';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { GostBlockungsdaten, cast_de_nrw_schule_svws_core_data_gost_GostBlockungsdaten } from '../../../core/data/gost/GostBlockungsdaten';
import { NullPointerException, cast_java_lang_NullPointerException } from '../../../java/lang/NullPointerException';
import { GostBlockungSchiene, cast_de_nrw_schule_svws_core_data_gost_GostBlockungSchiene } from '../../../core/data/gost/GostBlockungSchiene';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { GostHalbjahr, cast_de_nrw_schule_svws_core_types_gost_GostHalbjahr } from '../../../core/types/gost/GostHalbjahr';
import { IllegalArgumentException, cast_java_lang_IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';
import { GostBlockungKurs, cast_de_nrw_schule_svws_core_data_gost_GostBlockungKurs } from '../../../core/data/gost/GostBlockungKurs';

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

	private readonly _mapKurse : HashMap<Number | null, GostBlockungKurs | null> = new HashMap();

	private readonly _mapSchienen : HashMap<Number | null, GostBlockungSchiene | null> = new HashMap();

	private readonly _mapRegeln : HashMap<Number | null, GostBlockungRegel | null> = new HashMap();

	private _halbjahr : GostHalbjahr;


	/**
	 * Erstellt einen neuen Manager mit leeren Blockungsdaten und einem
	 * leeren Fächer-Manager.
	 */
	public constructor();

	/**
	 * Erstellt einen neuen Manager mit den angegebenen Blockungsdaten und
	 * dem Fächer-Manager
	 *  
	 * @param daten            die Blockungsdaten
	 * @param faecherManager   der Fächer-Manager
	 */
	public constructor(daten : GostBlockungsdaten, faecherManager : GostFaecherManager);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : GostBlockungsdaten, __param1? : GostFaecherManager) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined")) {
			this._daten = new GostBlockungsdaten();
			this._faecherManager = new GostFaecherManager();
			this._halbjahr = GostHalbjahr.EF1;
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.nrw.schule.svws.core.data.gost.GostBlockungsdaten')))) && ((typeof __param1 !== "undefined") && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('de.nrw.schule.svws.core.utils.gost.GostFaecherManager'))))) {
			let daten : GostBlockungsdaten = cast_de_nrw_schule_svws_core_data_gost_GostBlockungsdaten(__param0);
			let faecherManager : GostFaecherManager = cast_de_nrw_schule_svws_core_utils_gost_GostFaecherManager(__param1);
			this._daten = daten;
			this._faecherManager = faecherManager;
			let halbjahr : GostHalbjahr | null = GostHalbjahr.fromID(daten.gostHalbjahr);
			if (halbjahr === null) 
				throw new NullPointerException("Blockungsdaten müssen einem Halbjahr der gymnasialen Oberstufe zugeordnet sein.")
			this._halbjahr = halbjahr;
			for (let schiene of daten.schienen) 
				this._mapSchienen.put(schiene.id, schiene);
			for (let kurs of daten.kurse) 
				this._mapKurse.put(kurs.id, kurs);
			for (let regel of daten.regeln) 
				this._mapRegeln.put(regel.id, regel);
		} else throw new Error('invalid method overload');
	}

	/**
	 * Gibt den Fächer-Manager zurück, der für die Blockungsdaten verwendet wird.
	 * 
	 * @return der Fächer-Manager (siehe {@link GostFaecherManager})
	 */
	public faecherManager() : GostFaecherManager {
		return this._faecherManager;
	}

	/**
	 * Gibt die Blockungsdaten zurück.
	 * 
	 * @return die Blockungsdaten (siehe {@link GostBlockungsdaten})
	 */
	public daten() : GostBlockungsdaten {
		return this._daten;
	}

	/**
	 * Gibt das Halbjahr der gymnasialen Oberstufe zurück, für welches die 
	 * Blockung angelegt wurde.
	 * 
	 * @return das Halbjahr der gymnasialen Oberstufe
	 */
	public getHalbjahr() : GostHalbjahr {
		return this._halbjahr;
	}

	/**
	 * Setzt das Halbjahr der gymnasialen Oberstufe, für welches die 
	 * Blockung angelegt wurde.
	 * 
	 * @param halbjahr   das Halbjahr der gymnasialen Oberstufe
	 */
	public setHalbjahr(halbjahr : GostHalbjahr) : void {
		this._halbjahr = halbjahr;
		this._daten.gostHalbjahr = halbjahr.id;
	}

	/**
	 * Gibt die ID der Blockung zurück.
	 * 
	 * @return die ID der Blockung
	 */
	public getID() : number {
		return this._daten.id;
	}

	/**
	 * Setzt die ID der Blockung
	 * 
	 * @param id   die ID, welche der Blockung zugewiesen wird.
	 */
	public setID(id : number) : void {
		if (id < 0) 
			throw new IllegalArgumentException("Die Blockungs-ID muss positiv sein und ist daher ungültig.")
		this._daten.id = id;
	}

	/**
	 * Gibt den Namen der Blockung zurück.
	 * 
	 * @return der Name der Blockung
	 */
	public getName() : String {
		return this._daten.name;
	}

	/**
	 * Setzt den Namen der Blockung
	 * 
	 * @param name   der Name, welcher der Blockung zugewiesen wird.
	 */
	public setName(name : String) : void {
		if (JavaObject.equalsTranspiler("", (name))) 
			throw new IllegalArgumentException("Ein leerer Name ist für die Blockung nicht zulässig.")
		this._daten.name = name;
	}

	/**
	 * Fügt den übergebenen Kurs zu der Blockung hinzu
	 * 
	 * @param kurs   der hinzuzufügende Kurs
	 */
	public addKurs(kurs : GostBlockungKurs) : void {
		let comp : Comparator<GostBlockungKurs> = { compare : (a: GostBlockungKurs, b: GostBlockungKurs) => {
			let aFach : GostFach | null = this._faecherManager.get(a.fach_id);
			let bFach : GostFach | null = this._faecherManager.get(b.fach_id);
			if (aFach === null) 
				return (bFach === null) ? 0 : -1;
			if (bFach === null) 
				return 1;
			let result : number = JavaInteger.compare(aFach.sortierung, bFach.sortierung);
			if (result !== 0) 
				return result;
			result = JavaInteger.compare(a.kursart, b.kursart);
			if (result !== 0) 
				return result;
			return JavaInteger.compare(a.nummer, b.nummer);
		} };
		this._daten.kurse.add(kurs);
		this._daten.kurse.sort(comp);
		this._mapKurse.put(kurs.id, kurs);
	}

	/**
	 * Gibt den Kurs der Blockung anhand von dessen ID zurück.
	 * 
	 * @param id   die ID des Kurses
	 * 
	 * @return der Kurs
	 * 
	 * @throws NullPointerException falls der Kurs nicht in der Blockung existiert 
	 */
	public getKurs(id : number) : GostBlockungKurs {
		let kurs : GostBlockungKurs | null = this._mapKurse.get(id);
		if (kurs === null) 
			throw new NullPointerException("Ein Kurs mit der angegebenen ID existiert nicht in der Blockung.")
		return kurs;
	}

	/**
	 * Entfernt den Kurs mit der übergebenen ID aus der Blockung
	 * 
	 * @param id   die ID des zu entfernenden Kurses
	 */
	public removeKursByID(id : number) : void {
		let kurs : GostBlockungKurs = this.getKurs(id);
		this._daten.kurse.remove(kurs);
		this._mapKurse.remove(id);
	}

	/**
	 * Entfernt den übergebenen Kurs aus der Blockung
	 * 
	 * @param kurs   der zu entfernende Kurs
	 */
	public removeKurs(kurs : GostBlockungKurs) : void {
		this.removeKursByID(kurs.id);
	}

	/**
	 * Fügt die übergebene Schiene zu der Blockung hinzu
	 * 
	 * @param schiene   die hinzuzufügende Schiene
	 */
	public addSchiene(schiene : GostBlockungSchiene) : void {
		this._daten.schienen.add(schiene);
		this._daten.schienen.sort(GostBlockungsdatenManager.compSchiene);
		this._mapSchienen.put(schiene.id, schiene);
	}

	/**
	 * Gibt die Schiene der Blockung anhand von deren ID zurück.
	 * 
	 * @param id   die ID der Schiene
	 * 
	 * @return die Schiene
	 * 
	 * @throws NullPointerException falls die Schiene nicht in der Blockung existiert 
	 */
	public getSchiene(id : number) : GostBlockungSchiene {
		let schiene : GostBlockungSchiene | null = this._mapSchienen.get(id);
		if (schiene === null) 
			throw new NullPointerException("Eine Schiene mit der angegebenen ID existiert nicht in der Blockung.")
		return schiene;
	}

	/**
	 * Entfernt die Schiene mit der übergebenen ID aus der Blockung
	 * 
	 * @param id   die ID der zu entfernenden Schiene
	 */
	public removeSchieneByID(id : number) : void {
		let schiene : GostBlockungSchiene = this.getSchiene(id);
		this._daten.schienen.remove(schiene);
		this._mapSchienen.remove(id);
	}

	/**
	 * Entfernt die übergebene Schiene aus der Blockung
	 * 
	 * @param schiene   die zu entfernende Schiene
	 */
	public removeSchiene(schiene : GostBlockungSchiene) : void {
		this.removeSchieneByID(schiene.id);
	}

	/**
	 * Gibt die Default-Anzahl von Schienen zurück, die für die eine neue
	 * Blockung verwendet wird.
	 * 
	 * @param halbjahr   das Halbjahr, für welches die Blockung angelegt werden soll 
	 * 
	 * @return die Anzahl an Schienen für eine Vorauswahl 
	 */
	public static getDefaultSchienenAnzahl(halbjahr : GostHalbjahr) : number {
		return (halbjahr.id < 2) ? 13 : 11;
	}

	/**
	 * Fügt die übergebene Regel zu der Blockung hinzu
	 * 
	 * @param regel   die hinzuzufügende Regel
	 */
	public addRegel(regel : GostBlockungRegel) : void {
		this._daten.regeln.add(regel);
		this._daten.regeln.sort(GostBlockungsdatenManager.compRegel);
		this._mapRegeln.put(regel.id, regel);
	}

	/**
	 * Gibt die Regel der Blockung anhand von deren ID zurück.
	 * 
	 * @param id   die ID der Regel
	 * 
	 * @return die Regel
	 * 
	 * @throws NullPointerException falls die Regel nicht in der Blockung existiert 
	 */
	public getRegel(id : number) : GostBlockungRegel {
		let regel : GostBlockungRegel | null = this._mapRegeln.get(id);
		if (regel === null) 
			throw new NullPointerException("Eine Regel mit der angegebenen ID existiert nicht in der Blockung.")
		return regel;
	}

	/**
	 * Entfernt die Regel mit der übergebenen ID aus der Blockung
	 * 
	 * @param id   die ID der zu entfernenden Regel
	 */
	public removeRegelByID(id : number) : void {
		let regel : GostBlockungRegel = this.getRegel(id);
		this._daten.regeln.remove(regel);
		this._mapRegeln.remove(id);
	}

	/**
	 * Entfernt die übergebene Regel aus der Blockung
	 * 
	 * @param regel   die zu entfernende Regel
	 */
	public removeRegel(regel : GostBlockungRegel) : void {
		this.removeRegelByID(regel.id);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.gost.GostBlockungsdatenManager'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_gost_GostBlockungsdatenManager(obj : unknown) : GostBlockungsdatenManager {
	return obj as GostBlockungsdatenManager;
}
