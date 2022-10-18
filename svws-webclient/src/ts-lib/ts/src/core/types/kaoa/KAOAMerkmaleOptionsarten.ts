import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class KAOAMerkmaleOptionsarten extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : String;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<KAOAMerkmaleOptionsarten> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<String, KAOAMerkmaleOptionsarten> = new Map<String, KAOAMerkmaleOptionsarten>();

	public static readonly KEINE : KAOAMerkmaleOptionsarten = new KAOAMerkmaleOptionsarten("KEINE", 0, null);

	public readonly kuerzel : String | null;

	/**
	 * Erzeugt ein neues Element in der Aufzählung.
	 * 
	 * @param kuerzel   das Kürzel  
	 */
	private constructor(name : string, ordinal : number, kuerzel : String | null) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		KAOAMerkmaleOptionsarten.all_values_by_ordinal.push(this);
		KAOAMerkmaleOptionsarten.all_values_by_name.set(name, this);
		this.kuerzel = kuerzel;
	}

	/**
	 * Gibt die Optionsart anhand des angegebenen Kürzels zurück.
	 * 
	 * @param kuerzel   das Kürzel
	 * 
	 * @return die Optionsart oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : String | null) : KAOAMerkmaleOptionsarten | null {
		for (let art of KAOAMerkmaleOptionsarten.values()) 
			if (JavaObject.equalsTranspiler(art.kuerzel, (kuerzel))) 
				return art;
		return null;
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
		if (!(other instanceof KAOAMerkmaleOptionsarten))
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
	public compareTo(other : KAOAMerkmaleOptionsarten) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<KAOAMerkmaleOptionsarten> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : String) : KAOAMerkmaleOptionsarten | null {
		let tmp : KAOAMerkmaleOptionsarten | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.kaoa.KAOAMerkmaleOptionsarten'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_kaoa_KAOAMerkmaleOptionsarten(obj : unknown) : KAOAMerkmaleOptionsarten {
	return obj as KAOAMerkmaleOptionsarten;
}
