import { JavaEnum } from '../../../java/lang/JavaEnum';
import { IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';

export class GostLaufbahnplanungFachkombinationTyp extends JavaEnum<GostLaufbahnplanungFachkombinationTyp> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<GostLaufbahnplanungFachkombinationTyp> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, GostLaufbahnplanungFachkombinationTyp> = new Map<string, GostLaufbahnplanungFachkombinationTyp>();

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
		super(name, ordinal);
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
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.gost.GostLaufbahnplanungFachkombinationTyp';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.gost.GostLaufbahnplanungFachkombinationTyp', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_gost_GostLaufbahnplanungFachkombinationTyp(obj : unknown) : GostLaufbahnplanungFachkombinationTyp {
	return obj as GostLaufbahnplanungFachkombinationTyp;
}
