import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { GostFachwahl, cast_de_nrw_schule_svws_core_data_gost_GostFachwahl } from '../../../core/data/gost/GostFachwahl';
import { List, cast_java_util_List } from '../../../java/util/List';
import { ZulaessigeKursart, cast_de_nrw_schule_svws_core_types_kurse_ZulaessigeKursart } from '../../../core/types/kurse/ZulaessigeKursart';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { DeveloperNotificationException, cast_de_nrw_schule_svws_core_exceptions_DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { Arrays, cast_java_util_Arrays } from '../../../java/util/Arrays';
import { GostBlockungKurs, cast_de_nrw_schule_svws_core_data_gost_GostBlockungKurs } from '../../../core/data/gost/GostBlockungKurs';

export class GostKursart extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<GostKursart> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, GostKursart> = new Map<string, GostKursart>();

	/**
	 * Leistungskurs = LK
	 */
	public static readonly LK : GostKursart = new GostKursart("LK", 0, 1, "LK", "Leistungskurs", Arrays.asList(ZulaessigeKursart.LK1, ZulaessigeKursart.LK2));

	/**
	 * Grundkurs = GK
	 */
	public static readonly GK : GostKursart = new GostKursart("GK", 1, 2, "GK", "Grundkurs", Arrays.asList(ZulaessigeKursart.GKM, ZulaessigeKursart.GKS, ZulaessigeKursart.AB3, ZulaessigeKursart.AB4, ZulaessigeKursart.EFSP));

	/**
	 * Zusatzkurs = ZK
	 */
	public static readonly ZK : GostKursart = new GostKursart("ZK", 2, 3, "ZK", "Zusatzkurs", Arrays.asList(ZulaessigeKursart.ZK));

	/**
	 * Projektkurs = PJK
	 */
	public static readonly PJK : GostKursart = new GostKursart("PJK", 3, 4, "PJK", "Projektkurs", Arrays.asList(ZulaessigeKursart.PJK));

	/**
	 * Vertiefungskurs = VTF
	 */
	public static readonly VTF : GostKursart = new GostKursart("VTF", 4, 5, "VTF", "Vertiefungskurs", Arrays.asList(ZulaessigeKursart.VTF));

	private static readonly FACHART_ID_FAKTOR : number = 1000;

	/**
	 * Die Zuordnung der Kursarten zu dem Kürzel der Kursart
	 */
	private static readonly _mapKuerzel : HashMap<string, GostKursart> = new HashMap();

	/**
	 * Die Zuordnung der Kursarten zu der jeweiligen zulässigen Kursart
	 */
	private static readonly _mapZulKursart : HashMap<ZulaessigeKursart, GostKursart> = new HashMap();

	/**
	 * Die eindeutige ID der Kursart der Gymnasialen Oberstufe
	 */
	public readonly id : number;

	/**
	 * Das Kürzel der Kursart der Gymnasialen Oberstufe
	 */
	public readonly kuerzel : string;

	/**
	 * Die textuelle Beschreibung der allgemeinen Kursart der Gymnasialen Oberstufe
	 */
	public readonly beschreibung : string;

	/**
	 * Die Liste der Kursarten, welche zu dieser Gost-Kursart gehören
	 */
	private readonly kursarten : List<ZulaessigeKursart>;

	/**
	 * Erzeugt eine neue Kursart für die Aufzählung.
	 *
	 * @param id             die eindeutige ID der Kursart der Gymnasialen Oberstufe
	 * @param kuerzel        das Kürzel der Kursart der Gymnasialen Oberstufe
	 * @param beschreibung   die textuelle Beschreibung der allgemeinen Kursart der Gymnasialen Oberstufe
	 */
	private constructor(name : string, ordinal : number, id : number, kuerzel : string, beschreibung : string, kursarten : List<ZulaessigeKursart>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		GostKursart.all_values_by_ordinal.push(this);
		GostKursart.all_values_by_name.set(name, this);
		this.id = id;
		this.kuerzel = kuerzel;
		this.beschreibung = beschreibung;
		this.kursarten = kursarten;
	}

	/**
	 * Prüft die Anzahl der Wochenstunden zu der Kursart.
	 *
	 * @param anzahl   Anzahl der Wochenstunden
	 *
	 * @return         Anzahl der Wochenstunden der Kursart korrekt, true oder false
	 */
	public pruefeWochenstunden(anzahl : number) : boolean {
		switch (this.kuerzel) {
			case "GK": {
				return (anzahl === 3) || (anzahl === 4);
			}
			case "LK": {
				return (anzahl === 5);
			}
			case "PJK": {
				return (anzahl === 2) || (anzahl === 3);
			}
			case "VTF": {
				return (anzahl === 2);
			}
			case "ZK": {
				return (anzahl === 3);
			}
			default: {
				return false;
			}
		}
	}

	/**
	 * Gibt eine Map von den Kürzeln auf die Gost-Kursart zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln auf die Gost-Kursarten
	 */
	private static getMapByKuerzel() : HashMap<string, GostKursart> {
		if (GostKursart._mapKuerzel.size() === 0)
			for (let k of GostKursart.values())
				GostKursart._mapKuerzel.put(k.kuerzel, k);
		return GostKursart._mapKuerzel;
	}

	/**
	 * Gibt eine Map von den zulässigen Kursarten auf die Gost-Kursart zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den zulässigen Kursarten auf die Gost-Kursarten
	 */
	private static getMapByZulKursart() : HashMap<ZulaessigeKursart, GostKursart> {
		if (GostKursart._mapZulKursart.size() === 0)
			for (let k of GostKursart.values())
				for (let zulKursart of k.kursarten)
					GostKursart._mapZulKursart.put(zulKursart, k);
		return GostKursart._mapZulKursart;
	}

	/**
	 * Gibt die Liste der zulässigen Kursarten zurück.
	 *
	 * @return die Liste der zulässigen Kursarten
	 */
	public getKursarten() : List<ZulaessigeKursart> {
		return this.kursarten;
	}

	/**
	 * Gibt die Kursart aus der ID Kursart zurück.
	 *
	 * @param id    die ID der Kursart
	 *
	 * @return die Kursart
	 *
	 * @throws DeveloperNotificationException falls die ID ungültig ist
	 */
	public static fromID(id : number) : GostKursart {
		switch (id) {
			case 1: {
				return GostKursart.LK;
			}
			case 2: {
				return GostKursart.GK;
			}
			case 3: {
				return GostKursart.ZK;
			}
			case 4: {
				return GostKursart.PJK;
			}
			case 5: {
				return GostKursart.VTF;
			}
			default: {
				throw new DeveloperNotificationException("Invalid ID value.")
			}
		}
	}

	/**
	 * Liefert die Kursart anhand der Kursart-ID der Fachwahl.
	 *
	 * @param pFachwahl Das Fachwahl-Objekt.
	 * @return die Kursart anhand der Kursart-ID der Fachwahl.
	 * @throws DeveloperNotificationException falls die ID ungültig ist
	 */
	public static fromFachwahlOrException(pFachwahl : GostFachwahl) : GostKursart {
		return GostKursart.fromID(pFachwahl.kursartID);
	}

	/**
	 * Gibt die Kursart aus der ID Kursart zurück.
	 *
	 * @param id    die ID der Kursart
	 *
	 * @return die Kursart oder null falls die ID ungültig ist
	 */
	public static fromIDorNull(id : number) : GostKursart | null {
		switch (id) {
			case 1: {
				return GostKursart.LK;
			}
			case 2: {
				return GostKursart.GK;
			}
			case 3: {
				return GostKursart.ZK;
			}
			case 4: {
				return GostKursart.PJK;
			}
			case 5: {
				return GostKursart.VTF;
			}
			default: {
				return null;
			}
		}
	}

	/**
	 * Gibt die Gost-Kursart aus dem Kürzel der Kursart zurück.
	 *
	 * @param kuerzel    das Kürzel der Kursart
	 *
	 * @return die Kursart oder null, falls das Kürzel ungültig ist
	 */
	public static fromKuerzel(kuerzel : string | null) : GostKursart | null {
		return GostKursart.getMapByKuerzel().get(kuerzel);
	}

	/**
	 * Bestimmt die Gost-Kursart anhand der übergebenen zulässigen Kursart
	 *
	 * @param kursart   die Kursart
	 *
	 * @return die Gost-Kursart
	 */
	public static fromKursart(kursart : ZulaessigeKursart | null) : GostKursart | null {
		return GostKursart.getMapByZulKursart().get(kursart);
	}

	/**
	 * Berechnet mit der Formel pFachID * {@link #FACHART_ID_FAKTOR} + pKursartID die ID der Fachart.
	 *
	 * @param  pFachID    Die DatenbankID des Faches.
	 * @param  pKursartID Die DatenbankID der Kursart.
	 *
	 * @return pFachID * {@link #FACHART_ID_FAKTOR} + pKursartID
	 */
	public static getFachartID(pFachID : number, pKursartID : number) : number;

	/**
	 * Berechnet anhand des Fachwahl-Objektes die FachartID.
	 * @param pFachwahl Das Fachwahl-Objekt.
	 *
	 * @return pFachwahl.fachID * {@link #FACHART_ID_FAKTOR} + pFachwahl.kursartID
	 */
	public static getFachartID(pFachwahl : GostFachwahl) : number;

	/**
	 * Berechnet anhand des Kurs-Objektes die FachartID.
	 *
	 * @param pKurs Das Kurs-Objekt.
	 *
	 * @return pKurs.fachID * {@link #FACHART_ID_FAKTOR} + pKurs.kursartID
	 */
	public static getFachartID(pKurs : GostBlockungKurs) : number;

	/**
	 * Implementation for method overloads of 'getFachartID'
	 */
	public static getFachartID(__param0 : GostBlockungKurs | GostFachwahl | number, __param1? : number) : number {
		if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && typeof __param1 === "number")) {
			let pFachID : number = __param0 as number;
			let pKursartID : number = __param1 as number;
			return pFachID * GostKursart.FACHART_ID_FAKTOR + pKursartID;
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.nrw.schule.svws.core.data.gost.GostFachwahl')))) && (typeof __param1 === "undefined")) {
			let pFachwahl : GostFachwahl = cast_de_nrw_schule_svws_core_data_gost_GostFachwahl(__param0);
			return GostKursart.getFachartID(pFachwahl.fachID, pFachwahl.kursartID);
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.nrw.schule.svws.core.data.gost.GostBlockungKurs')))) && (typeof __param1 === "undefined")) {
			let pKurs : GostBlockungKurs = cast_de_nrw_schule_svws_core_data_gost_GostBlockungKurs(__param0);
			return GostKursart.getFachartID(pKurs.fach_id, pKurs.kursart);
		} else throw new Error('invalid method overload');
	}

	/**
	 * Berechnet anhand der Fachart-ID die Fach-ID.
	 *
	 * @param pFachartID Die ID der Fachart, welche das Fach und die Kursart kodiert.
	 *
	 * @return Ganzzahlige Division von pFachartID durch {@link #FACHART_ID_FAKTOR}
	 */
	public static getFachID(pFachartID : number) : number {
		return Math.trunc(pFachartID / GostKursart.FACHART_ID_FAKTOR);
	}

	/**
	 * Berechnet anhand der Fachart-ID die Kursart-ID.
	 *
	 * @param pFachartID Die ID der Fachart, welche das Fach und die Kursart kodiert.
	 *
	 * @return Rest der ganzzahligen Division von pFachartID durch {@link #FACHART_ID_FAKTOR}
	 */
	public static getKursartID(pFachartID : number) : number {
		return (pFachartID % GostKursart.FACHART_ID_FAKTOR) as number;
	}

	public toString() : string {
		return this.kuerzel;
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	private name() : string {
		return this.__name;
	}

	/**
	 * Returns the ordinal value of this enumeration value.
	 *
	 * @returns the ordinal value
	 */
	private ordinal() : number {
		return this.__ordinal;
	}

	/**
	 * Returns true if this and the other enumeration values are equal.
	 *
	 * @param other   the other enumeration value
	 *
	 * @returns true if they are equal and false otherwise
	 */
	public equals(other : JavaObject) : boolean {
		if (!(other instanceof GostKursart))
			return false;
		return this === other;
	}

	/**
	 * Returns the ordinal value as hashcode, since the ordinal value is unique.
	 *
	 * @returns the ordinal value as hashcode
	 */
	public hashCode() : number {
		return this.__ordinal;
	}

	/**
	 * Compares this enumeration value with the other enumeration value by their ordinal value.
	 *
	 * @param other   the other enumeration value
	 *
	 * @returns a negative, zero or postive value as this enumeration value is less than, equal to
	 *          or greater than the other enumeration value
	 */
	public compareTo(other : GostKursart) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<GostKursart> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : GostKursart | null {
		let tmp : GostKursart | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.gost.GostKursart'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_gost_GostKursart(obj : unknown) : GostKursart {
	return obj as GostKursart;
}
