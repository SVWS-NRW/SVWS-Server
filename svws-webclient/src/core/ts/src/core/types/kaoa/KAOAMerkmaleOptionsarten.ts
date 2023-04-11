import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';

export class KAOAMerkmaleOptionsarten extends JavaObject implements JavaEnum<KAOAMerkmaleOptionsarten> {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<KAOAMerkmaleOptionsarten> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, KAOAMerkmaleOptionsarten> = new Map<string, KAOAMerkmaleOptionsarten>();

	/**
	 * Keine Option für das KAoA-Merkmal
	 */
	public static readonly KEINE : KAOAMerkmaleOptionsarten = new KAOAMerkmaleOptionsarten("KEINE", 0, null);

	/**
	 * Das Kürzel für die Optionsart
	 */
	public readonly kuerzel : string | null;

	/**
	 * Erzeugt ein neues Element in der Aufzählung.
	 *
	 * @param kuerzel   das Kürzel
	 */
	private constructor(name : string, ordinal : number, kuerzel : string | null) {
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
	public static getByKuerzel(kuerzel : string | null) : KAOAMerkmaleOptionsarten | null {
		for (const art of KAOAMerkmaleOptionsarten.values())
			if (JavaObject.equalsTranspiler(art.kuerzel, (kuerzel)))
				return art;
		return null;
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	public name() : string {
		return this.__name;
	}

	/**
	 * Returns the ordinal value of this enumeration value.
	 *
	 * @returns the ordinal value
	 */
	public ordinal() : number {
		return this.__ordinal;
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	public toString() : string {
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
	public static valueOf(name : string) : KAOAMerkmaleOptionsarten | null {
		const tmp : KAOAMerkmaleOptionsarten | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.kaoa.KAOAMerkmaleOptionsarten', 'java.lang.Enum'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_kaoa_KAOAMerkmaleOptionsarten(obj : unknown) : KAOAMerkmaleOptionsarten {
	return obj as KAOAMerkmaleOptionsarten;
}
