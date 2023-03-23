import { JavaObject } from '../../../java/lang/JavaObject';
import { GostKlausurvorgabe } from '../../../core/data/gost/klausuren/GostKlausurvorgabe';
import { HashMap } from '../../../java/util/HashMap';
import { List } from '../../../java/util/List';
import { Vector } from '../../../java/util/Vector';

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
	 * Eine Map quartal -> kursartAllg -> fachId -> GostKlausurvorgabe
	 */
	private readonly _mapQuartalKursartFachKlausurvorgabe : HashMap<number, HashMap<string, HashMap<number, GostKlausurvorgabe>>> = new HashMap();

	/**
	 * Eine Map kursartAllg -> fachId -> Liste von GostKlausurvorgabe
	 */
	private readonly _mapKursartFachKlausurvorgaben : HashMap<string, HashMap<number, List<GostKlausurvorgabe>>> = new HashMap();


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
		for (const v of this._vorgaben) {
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
		let mapKursartFachKlausurvorgabe : HashMap<string, HashMap<number, GostKlausurvorgabe>> | null = this._mapQuartalKursartFachKlausurvorgabe.get(v.quartal);
		if (mapKursartFachKlausurvorgabe === null)
			this._mapQuartalKursartFachKlausurvorgabe.put(v.quartal, mapKursartFachKlausurvorgabe = new HashMap());
		let mapFachKlausurvorgabe : HashMap<number, GostKlausurvorgabe> | null = mapKursartFachKlausurvorgabe.get(v.kursart);
		if (mapFachKlausurvorgabe === null)
			mapKursartFachKlausurvorgabe.put(v.kursart, mapFachKlausurvorgabe = new HashMap());
		mapFachKlausurvorgabe.put(v.idFach, v);
		let mapFachKlausurvorgaben : HashMap<number, List<GostKlausurvorgabe>> | null = this._mapKursartFachKlausurvorgaben.get(v.kursart);
		if (mapFachKlausurvorgaben === null)
			this._mapKursartFachKlausurvorgaben.put(v.kursart, mapFachKlausurvorgaben = new HashMap());
		let listKlausurvorgaben : List<GostKlausurvorgabe> | null = mapFachKlausurvorgaben.get(v.idFach);
		if (listKlausurvorgaben === null)
			mapFachKlausurvorgaben.put(v.idFach, listKlausurvorgaben = new Vector());
		listKlausurvorgaben.add(v);
	}

	/**
	 * Aktualisiert die internen Strukturen, nachdem sich Informationen einer
	 * Klausurvorgabe geändert hat.
	 *
	 * @param vorgabe das GostKlausurvorgabe-Objekt
	 */
	public updateKlausurvorgabe(vorgabe : GostKlausurvorgabe) : void {
		if (!this._vorgaben.contains(vorgabe)) {
		}
		this.removeUpdateKlausurvorgabeCommons(vorgabe);
		this.addVorgabeToInternalMaps(vorgabe);
	}

	/**
	 * Fügt die Klausurvorgabe den internen Strukturen hinzu.
	 *
	 * @param vorgabe das GostKlausurvorgabe-Objekt
	 */
	public addKlausurvorgabe(vorgabe : GostKlausurvorgabe) : void {
		this._vorgaben.add(vorgabe);
		this._mapIdKlausurvorgabe.put(vorgabe.idVorgabe, vorgabe);
		this.removeUpdateKlausurvorgabeCommons(vorgabe);
		this.addVorgabeToInternalMaps(vorgabe);
	}

	private removeUpdateKlausurvorgabeCommons(vorgabe : GostKlausurvorgabe) : void {
		const listKlausurvorgabenMapQuartalKlausurvorgaben : Vector<GostKlausurvorgabe> | null = this._mapQuartalKlausurvorgaben.get(vorgabe.quartal);
		if (listKlausurvorgabenMapQuartalKlausurvorgaben !== null) {
			listKlausurvorgabenMapQuartalKlausurvorgaben.remove(vorgabe);
		}
		const map1 : HashMap<string, HashMap<number, GostKlausurvorgabe>> | null = this._mapQuartalKursartFachKlausurvorgabe.get(vorgabe.quartal);
		if (map1 !== null) {
			const map2 : HashMap<number, GostKlausurvorgabe> | null = map1.get(vorgabe.kursart);
			if (map2 !== null) {
				const kv : GostKlausurvorgabe | null = map2.get(vorgabe.idFach);
				if (kv as unknown === vorgabe as unknown)
					map2.remove(vorgabe.idFach);
			}
		}
		const map3 : HashMap<number, List<GostKlausurvorgabe>> | null = this._mapKursartFachKlausurvorgaben.get(vorgabe.kursart);
		if (map3 !== null) {
			const list : List<GostKlausurvorgabe> | null = map3.get(vorgabe.idFach);
			if (list !== null) {
				list.remove(vorgabe);
			}
		}
	}

	/**
	 * Löscht eine Klausurvorgabe aus den internen Strukturen
	 *
	 * @param vorgabe das GostKlausurvorgabe-Objekt
	 */
	public removeVorgabe(vorgabe : GostKlausurvorgabe) : void {
		this._vorgaben.remove(vorgabe);
		this._mapIdKlausurvorgabe.remove(vorgabe.idVorgabe);
		this.removeUpdateKlausurvorgabeCommons(vorgabe);
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
			const quartal : number = __param0 as number;
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
	public gibGostKlausurvorgabe(idVorgabe : number) : GostKlausurvorgabe | null;

	/**
	 * Gibt das GostKlausurvorgabe-Objekt zu den übergebenen Parametern zurück.
	 *
	 * @param quartal     das Quartal
	 * @param kursartAllg die Kursart
	 * @param idFach      die ID des Fachs
	 *
	 * @return das GostKlausurvorgabe-Objekt
	 */
	public gibGostKlausurvorgabe(quartal : number, kursartAllg : string | null, idFach : number) : GostKlausurvorgabe | null;

	/**
	 * Implementation for method overloads of 'gibGostKlausurvorgabe'
	 */
	public gibGostKlausurvorgabe(__param0 : number, __param1? : null | string, __param2? : number) : GostKlausurvorgabe | null {
		if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined")) {
			const idVorgabe : number = __param0 as number;
			return this._mapIdKlausurvorgabe.get(idVorgabe);
		} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && (typeof __param1 === "string") || (__param1 === null)) && ((typeof __param2 !== "undefined") && typeof __param2 === "number")) {
			const quartal : number = __param0 as number;
			const kursartAllg : string | null = __param1;
			const idFach : number = __param2 as number;
			const map1 : HashMap<string, HashMap<number, GostKlausurvorgabe>> | null = this._mapQuartalKursartFachKlausurvorgabe.get(quartal);
			if (map1 === null)
				return null;
			const map2 : HashMap<number, GostKlausurvorgabe> | null = map1.get(kursartAllg);
			if (map2 !== null)
				return map2.get(idFach);
			return null;
		} else throw new Error('invalid method overload');
	}

	/**
	 * Gibt die Liste der GostKlausurvorgabe-Objekte zu den übergebenen Parametern
	 * zurück.
	 *
	 * @param quartal     das Quartal, wenn 0, Vorgaben für alle Quartale
	 * @param kursartAllg die Kursart
	 * @param idFach      die ID des Fachs
	 *
	 * @return die Liste der GostKlausurvorgabe-Objekte
	 */
	public gibGostKlausurvorgaben(quartal : number, kursartAllg : string | null, idFach : number) : List<GostKlausurvorgabe> | null;

	/**
	 * Gibt die Liste der GostKlausurvorgabe-Objekte zu den übergebenen Parametern
	 * zurück.
	 *
	 * @param kursartAllg die Kursart
	 * @param idFach      die ID des Fachs
	 *
	 * @return die Liste der GostKlausurvorgabe-Objekte
	 */
	public gibGostKlausurvorgaben(kursartAllg : string | null, idFach : number) : List<GostKlausurvorgabe> | null;

	/**
	 * Implementation for method overloads of 'gibGostKlausurvorgaben'
	 */
	public gibGostKlausurvorgaben(__param0 : null | number | string, __param1 : null | number | string, __param2? : number) : List<GostKlausurvorgabe> | null {
		if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && (typeof __param1 === "string") || (__param1 === null)) && ((typeof __param2 !== "undefined") && typeof __param2 === "number")) {
			const quartal : number = __param0 as number;
			const kursartAllg : string | null = __param1;
			const idFach : number = __param2 as number;
			if (quartal > 0) {
				const retList : List<GostKlausurvorgabe> | null = new Vector();
				const vorgabe : GostKlausurvorgabe | null = this.gibGostKlausurvorgabe(quartal, kursartAllg, idFach);
				if (vorgabe !== null)
					retList.add(vorgabe);
				return retList;
			}
			return this.gibGostKlausurvorgaben(kursartAllg, idFach);
		} else if (((typeof __param0 !== "undefined") && (typeof __param0 === "string") || (__param0 === null)) && ((typeof __param1 !== "undefined") && typeof __param1 === "number") && (typeof __param2 === "undefined")) {
			const kursartAllg : string | null = __param0;
			const idFach : number = __param1 as number;
			const map1 : HashMap<number, List<GostKlausurvorgabe>> | null = this._mapKursartFachKlausurvorgaben.get(kursartAllg);
			if (map1 === null)
				return new Vector();
			const list : List<GostKlausurvorgabe> | null = map1.get(idFach);
			if (list === null)
				return new Vector();
			return list;
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.klausurplan.GostKlausurvorgabenManager'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_klausurplan_GostKlausurvorgabenManager(obj : unknown) : GostKlausurvorgabenManager {
	return obj as GostKlausurvorgabenManager;
}
