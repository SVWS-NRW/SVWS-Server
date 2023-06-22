import type { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';

export class GostKursblockungRegelParameterTyp extends JavaObject implements JavaEnum<GostKursblockungRegelParameterTyp> {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<GostKursblockungRegelParameterTyp> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, GostKursblockungRegelParameterTyp> = new Map<string, GostKursblockungRegelParameterTyp>();

	/**
	 * Der Parameter-Typ Kursart.
	 */
	public static readonly KURSART : GostKursblockungRegelParameterTyp = new GostKursblockungRegelParameterTyp("KURSART", 0, );

	/**
	 * Der Parameter-Typ Schienennummer.
	 */
	public static readonly SCHIENEN_NR : GostKursblockungRegelParameterTyp = new GostKursblockungRegelParameterTyp("SCHIENEN_NR", 1, );

	/**
	 * Der Parameter-Typ Kurs-ID.
	 */
	public static readonly KURS_ID : GostKursblockungRegelParameterTyp = new GostKursblockungRegelParameterTyp("KURS_ID", 2, );

	/**
	 * Der Parameter Typ Schüler-ID.
	 */
	public static readonly SCHUELER_ID : GostKursblockungRegelParameterTyp = new GostKursblockungRegelParameterTyp("SCHUELER_ID", 3, );

	/**
	 * Der Parameter Typ für eine Ja=1/Nein=0 Entscheidung.
	 */
	public static readonly BOOLEAN : GostKursblockungRegelParameterTyp = new GostKursblockungRegelParameterTyp("BOOLEAN", 4, );

	/**
	 * Der Parameter Typ für eine ganze Zahl.
	 */
	public static readonly GANZZAHL : GostKursblockungRegelParameterTyp = new GostKursblockungRegelParameterTyp("GANZZAHL", 5, );

	private constructor(name : string, ordinal : number) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		GostKursblockungRegelParameterTyp.all_values_by_ordinal.push(this);
		GostKursblockungRegelParameterTyp.all_values_by_name.set(name, this);
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
		if (!(other instanceof GostKursblockungRegelParameterTyp))
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
	public compareTo(other : GostKursblockungRegelParameterTyp) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<GostKursblockungRegelParameterTyp> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : GostKursblockungRegelParameterTyp | null {
		const tmp : GostKursblockungRegelParameterTyp | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.kursblockung.GostKursblockungRegelParameterTyp', 'java.lang.Enum'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_kursblockung_GostKursblockungRegelParameterTyp(obj : unknown) : GostKursblockungRegelParameterTyp {
	return obj as GostKursblockungRegelParameterTyp;
}
