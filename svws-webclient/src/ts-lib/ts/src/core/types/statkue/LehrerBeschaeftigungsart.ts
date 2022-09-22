import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { LehrerKatalogBeschaeftigungsartEintrag, cast_de_nrw_schule_svws_core_data_lehrer_LehrerKatalogBeschaeftigungsartEintrag } from '../../../core/data/lehrer/LehrerKatalogBeschaeftigungsartEintrag';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class LehrerBeschaeftigungsart extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : String;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<LehrerBeschaeftigungsart> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<String, LehrerBeschaeftigungsart> = new Map<String, LehrerBeschaeftigungsart>();

	public static readonly V : LehrerBeschaeftigungsart = new LehrerBeschaeftigungsart("V", 0, [new LehrerKatalogBeschaeftigungsartEintrag(1, "V", "Vollzeit", null, null)]);

	public static readonly T : LehrerBeschaeftigungsart = new LehrerBeschaeftigungsart("T", 1, [new LehrerKatalogBeschaeftigungsartEintrag(2, "T", "Teilzeit", null, null)]);

	public static readonly AT : LehrerBeschaeftigungsart = new LehrerBeschaeftigungsart("AT", 2, [new LehrerKatalogBeschaeftigungsartEintrag(3, "AT", "Altersteilzeit (Beschäftigungsphase)", null, null)]);

	public static readonly TA : LehrerBeschaeftigungsart = new LehrerBeschaeftigungsart("TA", 3, [new LehrerKatalogBeschaeftigungsartEintrag(4, "TA", "Altersteilzeit, vorm. teilzeitbeschäftigt (Verzichtsphase Altersermäßigung)", null, null)]);

	public static readonly VA : LehrerBeschaeftigungsart = new LehrerBeschaeftigungsart("VA", 4, [new LehrerKatalogBeschaeftigungsartEintrag(5, "VA", "Altersteilzeit, vorm. vollzeitbeschäftigt (Verzichtsphase Altersermäßigung)", null, null)]);

	public static readonly TS : LehrerBeschaeftigungsart = new LehrerBeschaeftigungsart("TS", 5, [new LehrerKatalogBeschaeftigungsartEintrag(6, "TS", "Sabbatjahr", null, null)]);

	public static readonly SB : LehrerBeschaeftigungsart = new LehrerBeschaeftigungsart("SB", 6, [new LehrerKatalogBeschaeftigungsartEintrag(8, "SB", "Nebenberufliche Beschäftigtigung", null, null)]);

	public static readonly GB : LehrerBeschaeftigungsart = new LehrerBeschaeftigungsart("GB", 7, [new LehrerKatalogBeschaeftigungsartEintrag(9, "GB", "Geringfügige Beschäftigtigung", null, null)]);

	public static readonly ST : LehrerBeschaeftigungsart = new LehrerBeschaeftigungsart("ST", 8, [new LehrerKatalogBeschaeftigungsartEintrag(11, "ST", "Studierende", null, null)]);

	public static readonly NA : LehrerBeschaeftigungsart = new LehrerBeschaeftigungsart("NA", 9, [new LehrerKatalogBeschaeftigungsartEintrag(12, "NA", "Beamter, nebenamtlich (nicht hauptamtlich im Schuldienst)", null, null)]);

	public static readonly G : LehrerBeschaeftigungsart = new LehrerBeschaeftigungsart("G", 10, [new LehrerKatalogBeschaeftigungsartEintrag(13, "G", "Gestellungsvertrag", null, null)]);

	public static readonly X : LehrerBeschaeftigungsart = new LehrerBeschaeftigungsart("X", 11, [new LehrerKatalogBeschaeftigungsartEintrag(15, "X", "Unentgeltlich Beschäftigte", null, null)]);

	public static readonly WT : LehrerBeschaeftigungsart = new LehrerBeschaeftigungsart("WT", 12, [new LehrerKatalogBeschaeftigungsartEintrag(16, "WT", "Beamte auf Widerruf (LAA) in Teilzeit", null, null)]);

	public static readonly WV : LehrerBeschaeftigungsart = new LehrerBeschaeftigungsart("WV", 13, [new LehrerKatalogBeschaeftigungsartEintrag(17, "WV", "Beamte auf Widerruf (LAA) in Vollzeit", null, null)]);

	public static VERSION : number = 1;

	public readonly daten : LehrerKatalogBeschaeftigungsartEintrag;

	public readonly historie : Array<LehrerKatalogBeschaeftigungsartEintrag>;

	private static readonly _artenByID : HashMap<Number, LehrerBeschaeftigungsart> = new HashMap();

	private static readonly _artenByKuerzel : HashMap<String, LehrerBeschaeftigungsart> = new HashMap();

	/**
	 * Erzeugt eine neue Beschäftigungsart in der Aufzählung.
	 * 
	 * @param historie   die Historie der Beschäftigungsart, welches ein Array von {@link LehrerKatalogBeschaeftigungsartEintrag} ist  
	 */
	private constructor(name : string, ordinal : number, historie : Array<LehrerKatalogBeschaeftigungsartEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		LehrerBeschaeftigungsart.all_values_by_ordinal.push(this);
		LehrerBeschaeftigungsart.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den IDs der Beschäftigungsarten auf die zugehörigen Beschäftigungsarten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den IDs der Beschäftigungsarten auf die zugehörigen Beschäftigungsarten
	 */
	private static getMapArtenByID() : HashMap<Number, LehrerBeschaeftigungsart> {
		if (LehrerBeschaeftigungsart._artenByID.size() === 0) 
			for (let l of LehrerBeschaeftigungsart.values()) 
				LehrerBeschaeftigungsart._artenByID.put(l.daten.id, l);
		return LehrerBeschaeftigungsart._artenByID;
	}

	/**
	 * Gibt eine Map von den Kürzeln der Beschäftigungsarten auf die zugehörigen Beschäftigungsarten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den Kürzeln der Beschäftigungsarten auf die zugehörigen Beschäftigungsarten
	 */
	private static getMapArtenByKuerzel() : HashMap<String, LehrerBeschaeftigungsart> {
		if (LehrerBeschaeftigungsart._artenByKuerzel.size() === 0) 
			for (let l of LehrerBeschaeftigungsart.values()) 
				LehrerBeschaeftigungsart._artenByKuerzel.put(l.daten.kuerzel, l);
		return LehrerBeschaeftigungsart._artenByKuerzel;
	}

	/**
	 * Gibt die Beschäftigungsart anhand der angegebenen ID zurück.
	 * 
	 * @param id   die ID der Beschäftigungsart
	 * 
	 * @return die Beschäftigungsart oder null, falls die ID ungültig ist
	 */
	public static getByID(id : number) : LehrerBeschaeftigungsart | null {
		return LehrerBeschaeftigungsart.getMapArtenByID().get(id);
	}

	/**
	 * Gibt die Beschäftigungsart anhand des angegebenen Kürzels zurück.
	 * 
	 * @param kuerzel   das Kürzel der Beschäftigungsart
	 * 
	 * @return die Beschäftigungsart oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : String | null) : LehrerBeschaeftigungsart | null {
		return LehrerBeschaeftigungsart.getMapArtenByKuerzel().get(kuerzel);
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	private name() : String {
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
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	public toString() : String {
		return this.__name;
	}

	/**
	 * Returns true if this and the other enumeration values are equal.
	 *
	 * @param other   the other enumeration value
	 *
	 * @returns true if they are equal and false otherwise
	 */
	public equals(other : JavaObject) : boolean {
		if (!(other instanceof LehrerBeschaeftigungsart))
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
	public compareTo(other : LehrerBeschaeftigungsart) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<LehrerBeschaeftigungsart> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : String) : LehrerBeschaeftigungsart | null {
		let tmp : LehrerBeschaeftigungsart | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.statkue.LehrerBeschaeftigungsart'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_statkue_LehrerBeschaeftigungsart(obj : unknown) : LehrerBeschaeftigungsart {
	return obj as LehrerBeschaeftigungsart;
}
