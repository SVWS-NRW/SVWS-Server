import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { BerufskollegBerufsebeneKatalogEintrag, cast_de_nrw_schule_svws_core_data_schule_BerufskollegBerufsebeneKatalogEintrag } from '../../../core/data/schule/BerufskollegBerufsebeneKatalogEintrag';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class BerufskollegBerufsebene1 extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : String;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<BerufskollegBerufsebene1> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<String, BerufskollegBerufsebene1> = new Map<String, BerufskollegBerufsebene1>();

	public static readonly AW : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("AW", 0, [new BerufskollegBerufsebeneKatalogEintrag(100001000, 1, "AW", "Agrarwirtschaft", null, null)]);

	public static readonly AB : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("AB", 1, [new BerufskollegBerufsebeneKatalogEintrag(100002000, 1, "AB", "Agrarwirtschaft, Bio- und Umwelttechnologie", null, null)]);

	public static readonly ER : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("ER", 2, [new BerufskollegBerufsebeneKatalogEintrag(100003000, 1, "ER", "Ernährung", null, null)]);

	public static readonly EU : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("EU", 3, [new BerufskollegBerufsebeneKatalogEintrag(100004000, 1, "EU", "Ernährung und Hauswirtschaft", null, null)]);

	public static readonly EH : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("EH", 4, [new BerufskollegBerufsebeneKatalogEintrag(100005000, 1, "EH", "Ernährung/Hauswirtschaft", null, null)]);

	public static readonly EV : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("EV", 5, [new BerufskollegBerufsebeneKatalogEintrag(100006000, 1, "EV", "Ernährungs- und Versorgungsmanagement", null, null)]);

	public static readonly GT : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("GT", 6, [new BerufskollegBerufsebeneKatalogEintrag(100007000, 1, "GT", "Gestaltung", null, null)]);

	public static readonly GU : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("GU", 7, [new BerufskollegBerufsebeneKatalogEintrag(100008000, 1, "GU", "Gesundheit und Soziales", null, null)]);

	public static readonly GE : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("GE", 8, [new BerufskollegBerufsebeneKatalogEintrag(100009000, 1, "GE", "Gesundheit/Erziehung und Soziales", null, null)]);

	public static readonly GS : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("GS", 9, [new BerufskollegBerufsebeneKatalogEintrag(100010000, 1, "GS", "Gesundheit/Soziales", null, null)]);

	public static readonly IF : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("IF", 10, [new BerufskollegBerufsebeneKatalogEintrag(100011000, 1, "IF", "Informatik", null, null)]);

	public static readonly OZ : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("OZ", 11, [new BerufskollegBerufsebeneKatalogEintrag(100012000, 1, "OZ", "ohne besondere Zuordnung", null, null)]);

	public static readonly SV : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("SV", 12, [new BerufskollegBerufsebeneKatalogEintrag(100013000, 1, "SV", "ohne Fachbereich / Schulversuch", null, null)]);

	public static readonly SW : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("SW", 13, [new BerufskollegBerufsebeneKatalogEintrag(100014000, 1, "SW", "Sozialwesen", null, null)]);

	public static readonly TE : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("TE", 14, [new BerufskollegBerufsebeneKatalogEintrag(100015000, 1, "TE", "Technik", null, null)]);

	public static readonly TN : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("TN", 15, [new BerufskollegBerufsebeneKatalogEintrag(100016000, 1, "TN", "Technik/Naturwissenschaften", null, null)]);

	public static readonly WI : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("WI", 16, [new BerufskollegBerufsebeneKatalogEintrag(100017000, 1, "WI", "Wirtschaft", null, null)]);

	public static readonly WV : BerufskollegBerufsebene1 = new BerufskollegBerufsebene1("WV", 17, [new BerufskollegBerufsebeneKatalogEintrag(100018000, 1, "WV", "Wirtschaft und Verwaltung", null, null)]);

	public static VERSION : number = 1;

	public readonly daten : BerufskollegBerufsebeneKatalogEintrag;

	public readonly historie : Array<BerufskollegBerufsebeneKatalogEintrag>;

	private static readonly _ebenen : HashMap<String, BerufskollegBerufsebene1> = new HashMap();

	/**
	 * Erzeugt eine neue Berufsebene in der Aufzählung.
	 * 
	 * @param historie   die Historie der Berufsebene, welches ein Array von {@link BerufskollegBerufsebeneKatalogEintrag} ist  
	 */
	private constructor(name : string, ordinal : number, historie : Array<BerufskollegBerufsebeneKatalogEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		BerufskollegBerufsebene1.all_values_by_ordinal.push(this);
		BerufskollegBerufsebene1.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den Kürzels der Berufsebenen auf die zugehörigen Berufsebenen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Kürzeln der Berufsebene auf die zugehörigen Berufsebene
	 */
	private static getMapBerufsebenenByKuerzel() : HashMap<String, BerufskollegBerufsebene1> {
		if (BerufskollegBerufsebene1._ebenen.size() === 0) {
			for (let s of BerufskollegBerufsebene1.values()) {
				if (s.daten !== null) 
					BerufskollegBerufsebene1._ebenen.put(s.daten.kuerzel, s);
			}
		}
		return BerufskollegBerufsebene1._ebenen;
	}

	/**
	 * Gibt die Berufsebene für das angegebene Kürzel zurück.
	 * 
	 * @param kuerzel   das Kürzel der Berufsebene
	 * 
	 * @return die Berufsebene oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : String | null) : BerufskollegBerufsebene1 | null {
		return BerufskollegBerufsebene1.getMapBerufsebenenByKuerzel().get(kuerzel);
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
		if (!(other instanceof BerufskollegBerufsebene1))
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
	public compareTo(other : BerufskollegBerufsebene1) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<BerufskollegBerufsebene1> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : String) : BerufskollegBerufsebene1 | null {
		let tmp : BerufskollegBerufsebene1 | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.schule.BerufskollegBerufsebene1'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_schule_BerufskollegBerufsebene1(obj : unknown) : BerufskollegBerufsebene1 {
	return obj as BerufskollegBerufsebene1;
}
