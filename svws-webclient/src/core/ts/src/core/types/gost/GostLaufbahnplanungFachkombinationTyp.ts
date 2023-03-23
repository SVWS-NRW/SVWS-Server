import { JavaObject } from '../../../java/lang/JavaObject';
import { IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';

export class GostLaufbahnplanungFachkombinationTyp extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<GostLaufbahnplanungFachkombinationTyp> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, GostLaufbahnplanungFachkombinationTyp> = new Map<string, GostLaufbahnplanungFachkombinationTyp>();

	/**
	 * Gibt an, das eine Fachkombination unzulässig ist
	 */
	public static readonly VERBOTEN : GostLaufbahnplanungFachkombinationTyp = new GostLaufbahnplanungFachkombinationTyp("VERBOTEN", 0, 0);

	/**
	 * Gibt an, das eine Fachkombination erforderlich ist
	 */
	public static readonly ERFORDERLICH : GostLaufbahnplanungFachkombinationTyp = new GostLaufbahnplanungFachkombinationTyp("ERFORDERLICH", 1, 1);

	/**
	 * Der Typ als Integer-Wert
	 */
	private readonly value : number;

	/**
	 * Erstellt einen neuen Fachkombinations-Typ
	 *
	 * @param value   der numerische Wert des Typs
	 */
	private constructor(name : string, ordinal : number, value : number) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		GostLaufbahnplanungFachkombinationTyp.all_values_by_ordinal.push(this);
		GostLaufbahnplanungFachkombinationTyp.all_values_by_name.set(name, this);
		this.value = value;
	}

	/**
	 * Gibt den numerischen Wert des Fachkombination-Typs zurück.
	 *
	 * @return der numerische Wert
	 */
	public getValue() : number {
		return this.value;
	}

	/**
	 * Gibt den Fachkombination-Typ für den angegebenen numerischen Wert
	 * zurück.
	 *
	 * @param value   der numerische Wert
	 *
	 * @return der Typ der Fachkombination
	 *
	 * @throws IllegalArgumentException   bei einem ungültigen numerischen Wert
	 */
	public static fromValue(value : number) : GostLaufbahnplanungFachkombinationTyp {
		if ((value < 0) || (value > 1))
			throw new IllegalArgumentException("Der Parameter value " + value + "ist für den Typ einer Fachkombination ungültig.")
		return (value === 0) ? GostLaufbahnplanungFachkombinationTyp.VERBOTEN : GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH;
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
		if (!(other instanceof GostLaufbahnplanungFachkombinationTyp))
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
	public compareTo(other : GostLaufbahnplanungFachkombinationTyp) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<GostLaufbahnplanungFachkombinationTyp> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : GostLaufbahnplanungFachkombinationTyp | null {
		const tmp : GostLaufbahnplanungFachkombinationTyp | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.gost.GostLaufbahnplanungFachkombinationTyp'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_gost_GostLaufbahnplanungFachkombinationTyp(obj : unknown) : GostLaufbahnplanungFachkombinationTyp {
	return obj as GostLaufbahnplanungFachkombinationTyp;
}
