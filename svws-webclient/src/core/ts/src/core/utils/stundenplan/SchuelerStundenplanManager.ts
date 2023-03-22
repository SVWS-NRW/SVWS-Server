import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { SchuelerStundenplanUnterricht, cast_de_nrw_schule_svws_core_data_stundenplan_SchuelerStundenplanUnterricht } from '../../../core/data/stundenplan/SchuelerStundenplanUnterricht';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { List, cast_java_util_List } from '../../../java/util/List';
import { IntegerComparator, cast_de_nrw_schule_svws_core_utils_stundenplan_IntegerComparator } from '../../../core/utils/stundenplan/IntegerComparator';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';
import { StundenplanZeitraster, cast_de_nrw_schule_svws_core_data_stundenplan_StundenplanZeitraster } from '../../../core/data/stundenplan/StundenplanZeitraster';
import { SchuelerStundenplan, cast_de_nrw_schule_svws_core_data_stundenplan_SchuelerStundenplan } from '../../../core/data/stundenplan/SchuelerStundenplan';

export class SchuelerStundenplanManager extends JavaObject {

	/**
	 * Die Stundenplandaten, die im Manager vorhanden sind
	 */
	private readonly _daten : SchuelerStundenplan;

	/**
	 * Eine Map idUnterricht -> SchuelerStundenplanUnterricht
	 */
	private readonly _mapUnterricht : HashMap<number, SchuelerStundenplanUnterricht> = new HashMap();

	/**
	 * Eine Map idZeitraster -> Liste von Unterricht
	 */
	private readonly _mapZeitrasterUnterricht : HashMap<number, Vector<SchuelerStundenplanUnterricht>> = new HashMap();

	/**
	 * Eine Map wochentyp, idZeitraster -> Liste von Unterricht
	 */
	private readonly _mapWocheZeitrasterUnterricht : HashMap<number, HashMap<number, Vector<SchuelerStundenplanUnterricht>>> = new HashMap();

	/**
	 * Eine Map idZeitraster -> Zeitraster
	 */
	private readonly _mapZeitraster : HashMap<number, StundenplanZeitraster> = new HashMap();

	/**
	 * Eine Map wochentag, stunde -> Zeitraster
	 */
	private readonly _mapWochentagStundeZeitraster : HashMap<number, HashMap<number, StundenplanZeitraster>> = new HashMap();

	/**
	 * Eine Map wochentag -> Liste von Zeitrastern
	 */
	private readonly _mapWochentagZeitraster : HashMap<number, Vector<StundenplanZeitraster>> = new HashMap();

	/**
	 * Eine Map stunde -> Liste von Zeitrastern
	 */
	private readonly _mapStundeZeitraster : HashMap<number, Vector<StundenplanZeitraster>> = new HashMap();

	/**
	 *  Der minimale Wochentag, der in den Stundenplandaten vorkommt, z.B. 1 für
	 *  Montag
	 */
	private minWochentag : number = JavaInteger.MAX_VALUE;

	/**
	 *  Der maximale Wochentag, der in den Stundenplandaten vorkommt, z.B. 5 für
	 *  Freitag
	 */
	private maxWochentag : number = JavaInteger.MIN_VALUE;

	/**
	 *  Die minimale Unterrichtsstunde, die in den Stundenplandaten vorkommt, z.B. 1
	 *  für die 1. Stunde
	 */
	private minStunde : number = JavaInteger.MAX_VALUE;

	/**
	 *  Die maximale Unterrichtsstunde, die in den Stundenplandaten vorkommt, z.B. 9
	 *  für die 9. Stunde
	 */
	private maxStunde : number = JavaInteger.MIN_VALUE;


	/**
	 * Erstellt einen neuen Manager mit den angegebenen Stundenplandaten und erzeugt
	 * die privaten Attribute.
	 * 
	 * @param daten die Stundenplandaten
	 */
	public constructor(daten : SchuelerStundenplan) {
		super();
		this._daten = daten;
		for (let sz of this._daten.zeitraster) {
			if (sz.wochentag < this.minWochentag) 
				this.minWochentag = sz.wochentag;
			if (sz.wochentag > this.maxWochentag) 
				this.maxWochentag = sz.wochentag;
			if (sz.unterrichtstunde < this.minStunde) 
				this.minStunde = sz.unterrichtstunde;
			if (sz.unterrichtstunde > this.maxStunde) 
				this.maxStunde = sz.unterrichtstunde;
			this._mapZeitraster.put(sz.id, sz);
			let listWochentagZeitraster : Vector<StundenplanZeitraster> | null = this._mapWochentagZeitraster.get(sz.wochentag);
			if (listWochentagZeitraster === null) {
				listWochentagZeitraster = new Vector();
				this._mapWochentagZeitraster.put(sz.wochentag, listWochentagZeitraster);
			}
			listWochentagZeitraster.add(sz);
			let listStundeZeitraster : Vector<StundenplanZeitraster> | null = this._mapStundeZeitraster.get(sz.unterrichtstunde);
			if (listStundeZeitraster === null) {
				listStundeZeitraster = new Vector();
				this._mapStundeZeitraster.put(sz.unterrichtstunde, listStundeZeitraster);
			}
			listStundeZeitraster.add(sz);
			let mapStundeUnterricht : HashMap<number, StundenplanZeitraster> | null = this._mapWochentagStundeZeitraster.get(sz.wochentag);
			if (mapStundeUnterricht === null) {
				mapStundeUnterricht = new HashMap();
				this._mapWochentagStundeZeitraster.put(sz.wochentag, mapStundeUnterricht);
			}
			mapStundeUnterricht.put(sz.unterrichtstunde, sz);
		}
		for (let ssu of this._daten.unterricht) {
			this._mapUnterricht.put(ssu.idUnterricht, ssu);
			let listZeitrasterUnterricht : Vector<SchuelerStundenplanUnterricht> | null = this._mapZeitrasterUnterricht.get(ssu.idZeitraster);
			if (listZeitrasterUnterricht === null) {
				listZeitrasterUnterricht = new Vector();
				this._mapZeitrasterUnterricht.put(ssu.idZeitraster, listZeitrasterUnterricht);
			}
			listZeitrasterUnterricht.add(ssu);
			let mapZeitrasterUnterricht : HashMap<number, Vector<SchuelerStundenplanUnterricht>> | null = this._mapWocheZeitrasterUnterricht.get(ssu.wochentyp);
			if (mapZeitrasterUnterricht === null) {
				mapZeitrasterUnterricht = new HashMap();
				this._mapWocheZeitrasterUnterricht.put(ssu.wochentyp, mapZeitrasterUnterricht);
			}
			let listWocheZeitrasterUnterricht : Vector<SchuelerStundenplanUnterricht> | null = mapZeitrasterUnterricht.get(ssu.idZeitraster);
			if (listWocheZeitrasterUnterricht === null) {
				listWocheZeitrasterUnterricht = new Vector();
				mapZeitrasterUnterricht.put(ssu.idZeitraster, listWocheZeitrasterUnterricht);
			}
			listWocheZeitrasterUnterricht.add(ssu);
		}
	}

	/**
	 * Liefert die ID des Stundenplans
	 * 
	 * @return die ID des Stundenplans
	 */
	public getStundenplanID() : number {
		return this._daten.idStundenplan;
	}

	/**
	 * Liefert die ID des Schülers
	 * 
	 * @return die ID des Schülers
	 */
	public getSchuelerID() : number {
		return this._daten.idSchueler;
	}

	/**
	 * Liefert den minimalen Wochentag als int, z.B. 1 für Montag
	 * 
	 * @return den minimalen Wochentag
	 */
	public getMinWochentag() : number {
		return this.minWochentag;
	}

	/**
	 * Liefert den maximalen Wochentag als int, z.B. 5 für Freitag
	 * 
	 * @return den maximalen Wochentag
	 */
	public getMaxWochentag() : number {
		return this.maxWochentag;
	}

	/**
	 * Liefert die minimale Unterrichtsstunde als int, z.B. 1 für die 1. Stunde
	 * 
	 * @return die minimale Unterrichtsstunde
	 */
	public getMinStunde() : number {
		return this.minStunde;
	}

	/**
	 * Liefert die maximale Unterrichtsstunde als int, z.B. 9 für die 9. Stunde
	 * 
	 * @return die maximale Unterrichtsstunde
	 */
	public getMaxStunde() : number {
		return this.maxStunde;
	}

	/**
	 * Liefert das SchulerStundenplanUnterricht-Objekt zur übergebenen
	 * Unterrichts-ID
	 * 
	 * @param idUnterricht die ID des Unterrichts
	 * 
	 * @return das SchulerStundenplanUnterricht-Objekt
	 */
	public getUnterrichtById(idUnterricht : number) : SchuelerStundenplanUnterricht | null {
		return this._mapUnterricht.get(idUnterricht);
	}

	/**
	 * Liefert eine Liste von allen SchuelerStundenplanUnterricht-Objekten, die im übergeben Zeitraster liegen.
	 * 
	 * @param idZeitraster die ID des Zeitrasters
	 * 
	 * @return Liste von SchuelerStundenplanUnterricht-Objekten
	 */
	public getUnterrichtByZeitrasterId(idZeitraster : number) : List<SchuelerStundenplanUnterricht> | null {
		return this._mapZeitrasterUnterricht.get(idZeitraster);
	}

	/**
	 * Liefert eine Liste von allen SchuelerStundenplanUnterricht-Objekten, die im übergeben Wochentyp und im übergebenen Zeitraster liegen.
	 * 
	 * @param wochentyp der Wochentyp
	 * @param idZeitraster die ID des Zeitrasters
	 * 
	 * @return Liste von SchuelerStundenplanUnterricht-Objekten
	 */
	public getUnterrichtByWocheZeitrasterId(wochentyp : number, idZeitraster : number) : List<SchuelerStundenplanUnterricht> | null;

	/**
	 * Liefert eine Liste von allen SchuelerStundenplanUnterricht-Objekten, die im übergeben Wochentyp und im übergebenen Zeitraster liegen.
	 * Je nach Parameter inklWoche0 wird die Liste um den Unterricht aus Woche 0 ergänzt.
	 * 
	 * @param wochentyp der Wochentyp
	 * @param idZeitraster die ID des Zeitrasters
	 * @param inklWoche0 Ergänzung des Unterrichts um Woche 0
	 * 
	 * @return Liste von SchuelerStundenplanUnterricht-Objekten
	 */
	public getUnterrichtByWocheZeitrasterId(wochentyp : number, idZeitraster : number, inklWoche0 : boolean) : List<SchuelerStundenplanUnterricht> | null;

	/**
	 * Implementation for method overloads of 'getUnterrichtByWocheZeitrasterId'
	 */
	public getUnterrichtByWocheZeitrasterId(__param0 : number, __param1 : number, __param2? : boolean) : List<SchuelerStundenplanUnterricht> | null {
		if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && typeof __param1 === "number") && (typeof __param2 === "undefined")) {
			let wochentyp : number = __param0 as number;
			let idZeitraster : number = __param1 as number;
			return this.getUnterrichtByWocheZeitrasterId(wochentyp, idZeitraster, false);
		} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && typeof __param1 === "number") && ((typeof __param2 !== "undefined") && typeof __param2 === "boolean")) {
			let wochentyp : number = __param0 as number;
			let idZeitraster : number = __param1 as number;
			let inklWoche0 : boolean = __param2 as boolean;
			let mapZeitrasterUnterricht_Wochentyp : HashMap<number, Vector<SchuelerStundenplanUnterricht>> | null = this._mapWocheZeitrasterUnterricht.get(wochentyp);
			if (mapZeitrasterUnterricht_Wochentyp === null) {
				return null;
			}
			let retList : List<SchuelerStundenplanUnterricht> | null = mapZeitrasterUnterricht_Wochentyp.get(idZeitraster);
			if (retList === null) 
				retList = new Vector();
			if (wochentyp !== 0 && inklWoche0) {
				let mapZeitrasterUnterricht_Woche0 : HashMap<number, Vector<SchuelerStundenplanUnterricht>> | null = this._mapWocheZeitrasterUnterricht.get(0);
				if (mapZeitrasterUnterricht_Woche0 === null) {
					return retList;
				}
				let listUnterricht_Woche0 : List<SchuelerStundenplanUnterricht> | null = mapZeitrasterUnterricht_Woche0.get(idZeitraster);
				if (listUnterricht_Woche0 === null) {
					return retList;
				}
				retList.addAll(listUnterricht_Woche0);
			}
			return retList;
		} else throw new Error('invalid method overload');
	}

	/**
	 * Liefert das Zeitraster-Objekt zur übergebenen ID.
	 * 
	 * @param idZeitraster die ID des Zeitrasters
	 * 
	 * @return das Zeitraster-Objekt
	 */
	public getZeitrasterById(idZeitraster : number) : StundenplanZeitraster | null {
		return this._mapZeitraster.get(idZeitraster);
	}

	/**
	 * Liefert das Zeitraster-Objekt an dem übergebenen Wochentag in der übergebenen Stunde.
	 * 
	 * @param wochentag der Wochentag
	 * @param stunde die Stunde
	 * @return das Zeitraster-Objekt
	 */
	public getZeitrasterByWochentagStunde(wochentag : number, stunde : number) : StundenplanZeitraster | null {
		let map : HashMap<number, StundenplanZeitraster> | null = this._mapWochentagStundeZeitraster.get(wochentag);
		if (map === null) {
			return null;
		}
		return map.get(stunde);
	}

	/**
	 * Liefert die Liste von Zeitraster-Objekten am übergebenen Wochentag.
	 * 
	 * @param wochentag der Wochentag
	 * 
	 * @return Liste von Zeitraster-Objekten
	 */
	public getZeitrasterByWochentag(wochentag : number) : Vector<StundenplanZeitraster> | null {
		return this._mapWochentagZeitraster.get(wochentag);
	}

	/**
	 * Liefert die Liste von Zeitraster-Objekten in der übergebenen Stunde.
	 * 
	 * @param stunde die Unterrichtsstunde
	 * 
	 * @return Liste von Zeitraster-Objekten
	 */
	public getZeitrasterByStunde(stunde : number) : Vector<StundenplanZeitraster> | null {
		return this._mapStundeZeitraster.get(stunde);
	}

	/**
	 * Gibt zurück, ob es unterschiedliche Wochentypen gibt.
	 * 
	 * @return {@code true}, falls es sich um unterschiedliche Wochentypen handelt,
	 *         {@code false}, falls es nur einen Typen gibt
	 */
	public isAbWochen() : boolean {
		return this._mapWocheZeitrasterUnterricht.size() > 1;
	}

	/**
	 * Gibt die Anzahl der Wochentypen zurück.
	 * 
	 * @return die Anzahl der Wochentypen
	 */
	public getAnzahlWochentypen() : number {
		return this.isAbWochen() ? this._mapWocheZeitrasterUnterricht.size() - 1 : 1;
	}

	/**
	 * Gibt die Wochentypen ohne Typ 0 zurück.
	 * 
	 * @return die Wochentypen als Vector von Integern
	 */
	public getWochentypen() : Vector<number> {
		let retVec : Vector<number> | null = new Vector(this._mapWocheZeitrasterUnterricht.keySet());
		if (!this.isAbWochen()) {
			return retVec;
		}
		retVec.sort(new IntegerComparator());
		retVec.remove(0);
		return retVec;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.stundenplan.SchuelerStundenplanManager'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_stundenplan_SchuelerStundenplanManager(obj : unknown) : SchuelerStundenplanManager {
	return obj as SchuelerStundenplanManager;
}
