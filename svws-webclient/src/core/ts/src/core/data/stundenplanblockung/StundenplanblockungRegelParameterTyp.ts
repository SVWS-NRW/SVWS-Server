import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';

export class StundenplanblockungRegelParameterTyp extends JavaObject implements JavaEnum<StundenplanblockungRegelParameterTyp> {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<StundenplanblockungRegelParameterTyp> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, StundenplanblockungRegelParameterTyp> = new Map<string, StundenplanblockungRegelParameterTyp>();

	/**
	 * Der Parameter-Typ der Lehrkraft.
	 */
	public static readonly LEHRKRAFT_ID : StundenplanblockungRegelParameterTyp = new StundenplanblockungRegelParameterTyp("LEHRKRAFT_ID", 0, );

	/**
	 * Der Parameter-Typ der Klasse.
	 */
	public static readonly KLASSE_ID : StundenplanblockungRegelParameterTyp = new StundenplanblockungRegelParameterTyp("KLASSE_ID", 1, );

	/**
	 * Der Parameter-Typ des Faches.
	 */
	public static readonly FACH_ID : StundenplanblockungRegelParameterTyp = new StundenplanblockungRegelParameterTyp("FACH_ID", 2, );

	/**
	 * Der Parameter-Typ des Raumes.
	 */
	public static readonly RAUM_ID : StundenplanblockungRegelParameterTyp = new StundenplanblockungRegelParameterTyp("RAUM_ID", 3, );

	/**
	 * Der Parameter-Typ der Kopplung.
	 */
	public static readonly KOPPLUNG_ID : StundenplanblockungRegelParameterTyp = new StundenplanblockungRegelParameterTyp("KOPPLUNG_ID", 4, );

	/**
	 * Der Parameter-Typ der Lerngruppe.
	 */
	public static readonly LERNGRUPPE_ID : StundenplanblockungRegelParameterTyp = new StundenplanblockungRegelParameterTyp("LERNGRUPPE_ID", 5, );

	/**
	 * Der Parameter-Typ für JA=1 und NEIN=0 Werte.
	 */
	public static readonly WERT_BOOLEAN : StundenplanblockungRegelParameterTyp = new StundenplanblockungRegelParameterTyp("WERT_BOOLEAN", 6, );

	/**
	 * Der Parameter-Typ für Integer-Werte.
	 */
	public static readonly WERT_INTEGER : StundenplanblockungRegelParameterTyp = new StundenplanblockungRegelParameterTyp("WERT_INTEGER", 7, );

	private constructor(name : string, ordinal : number) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		StundenplanblockungRegelParameterTyp.all_values_by_ordinal.push(this);
		StundenplanblockungRegelParameterTyp.all_values_by_name.set(name, this);
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
		if (!(other instanceof StundenplanblockungRegelParameterTyp))
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
	public compareTo(other : StundenplanblockungRegelParameterTyp) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<StundenplanblockungRegelParameterTyp> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : StundenplanblockungRegelParameterTyp | null {
		const tmp : StundenplanblockungRegelParameterTyp | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplanblockung.StundenplanblockungRegelParameterTyp', 'java.lang.Enum'].includes(name);
	}

}

export function cast_de_svws_nrw_core_data_stundenplanblockung_StundenplanblockungRegelParameterTyp(obj : unknown) : StundenplanblockungRegelParameterTyp {
	return obj as StundenplanblockungRegelParameterTyp;
}
