import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { GostKlausurvorgabe, cast_de_nrw_schule_svws_core_data_gost_klausuren_GostKlausurvorgabe } from '../../../core/data/gost/klausuren/GostKlausurvorgabe';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { List, cast_java_util_List } from '../../../java/util/List';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class GostKlausurvorgabenManager extends JavaObject {

	/**
	 * Die GostKlausurvorgaben, die im Manager vorhanden sind 
	 */
	private readonly _vorgaben : List<GostKlausurvorgabe>;

	/**
	 * Eine Map quartal -> Liste von GostKlausurvorgaben 
	 */
	private readonly _mapQuartalKlausurvorgaben : HashMap<number, Vector<GostKlausurvorgabe>> = new HashMap();

	/**
	 * Eine Map id -> GostKlausurvorgabe 
	 */
	private readonly _mapIdKlausurvorgabe : HashMap<number, GostKlausurvorgabe> = new HashMap();


	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen
	 * GostKlausurvorgaben und erzeugt die privaten Attribute.
	 * 
	 * @param vorgaben die Liste der GostKlausurvorgaben eines Abiturjahrgangs und
	 *                 Gost-Halbjahres
	 */
	public constructor(vorgaben : List<GostKlausurvorgabe>) {
		super();
		this._vorgaben = vorgaben;
		for (let v of this._vorgaben) {
			this._mapIdKlausurvorgabe.put(v.idVorgabe, v);
			this.addVorgabeToInternalMaps(v);
		}
	}

	private addVorgabeToInternalMaps(v : GostKlausurvorgabe) : void {
		let listKlausurvorgabenMapQuartalKlausurvorgaben : Vector<GostKlausurvorgabe> | null = this._mapQuartalKlausurvorgaben.get(v.quartal);
		if (listKlausurvorgabenMapQuartalKlausurvorgaben === null) {
			listKlausurvorgabenMapQuartalKlausurvorgaben = new Vector();
			this._mapQuartalKlausurvorgaben.put(v.quartal, listKlausurvorgabenMapQuartalKlausurvorgaben);
		}
		listKlausurvorgabenMapQuartalKlausurvorgaben.add(v);
	}

	/**
	 * Aktualisiert die internen Strukturen, nachdem sich Informationen einer
	 * Klausurvorgabe geändert hat.
	 * 
	 * @param vorgabe das GostKlausurvorgabe-Objekt
	 */
	public updateKlausurvorgabe(vorgabe : GostKlausurvorgabe) : void {
		if (!this._vorgaben.contains(vorgabe)) {
			this._vorgaben.add(vorgabe);
		}
		this._mapIdKlausurvorgabe.put(vorgabe.idVorgabe, vorgabe);
		let quartalList : Vector<GostKlausurvorgabe> | null = this._mapQuartalKlausurvorgaben.get(vorgabe.quartal);
		if (quartalList !== null) {
			quartalList.remove(vorgabe);
		} else {
		}
		this.addVorgabeToInternalMaps(vorgabe);
	}

	/**
	 * Löscht eine Klausurvorgabe aus den internen Strukturen
	 * 
	 * @param vorgabe das GostKlausurvorgabe-Objekt
	 */
	public removeVorgabe(vorgabe : GostKlausurvorgabe) : void {
		let listKlausurvorgabenMapQuartalKlausurvorgaben : Vector<GostKlausurvorgabe> | null = this._mapQuartalKlausurvorgaben.get(vorgabe.quartal);
		if (listKlausurvorgabenMapQuartalKlausurvorgaben === null) {
			return;
		}
		listKlausurvorgabenMapQuartalKlausurvorgaben.remove(vorgabe);
		this._vorgaben.remove(vorgabe);
		this._mapIdKlausurvorgabe.remove(vorgabe.idVorgabe);
	}

	/**
	 * Liefert eine Liste von GostKlausurvorgabe-Objekten des Halbjahres
	 * 
	 * @return die Liste von GostKlausurvorgabe-Objekten
	 */
	public getKlausurvorgaben() : List<GostKlausurvorgabe>;

	/**
	 * Liefert eine Liste von GostKlausurvorgabe-Objekten zum übergebenen Quartal
	 * 
	 * @param quartal die Nummer des Quartals
	 * 
	 * @return die Liste von GostKlausurvorgabe-Objekten
	 */
	public getKlausurvorgaben(quartal : number) : List<GostKlausurvorgabe> | null;

	/**
	 * Implementation for method overloads of 'getKlausurvorgaben'
	 */
	public getKlausurvorgaben(__param0? : number) : List<GostKlausurvorgabe> | null {
		if ((typeof __param0 === "undefined")) {
			return this._vorgaben;
		} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number")) {
			let quartal : number = __param0 as number;
			return this._mapQuartalKlausurvorgaben.get(quartal);
		} else throw new Error('invalid method overload');
	}

	/**
	 * Gibt das GostKlausurvorgabe-Objekt zur übergebenen id zurück.
	 * 
	 * @param idVorgabe die ID der Klausurvorgabe
	 * 
	 * @return das GostKlausurvorgabe-Objekt
	 */
	public gibGostKlausurvorgabe(idVorgabe : number) : GostKlausurvorgabe | null {
		return this._mapIdKlausurvorgabe.get(idVorgabe);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.klausurplan.GostKlausurvorgabenManager'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_klausurplan_GostKlausurvorgabenManager(obj : unknown) : GostKlausurvorgabenManager {
	return obj as GostKlausurvorgabenManager;
}
