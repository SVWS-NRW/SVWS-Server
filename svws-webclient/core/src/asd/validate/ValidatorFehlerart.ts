import { JavaEnum } from '../../java/lang/JavaEnum';
import { Class } from '../../java/lang/Class';

export class ValidatorFehlerart extends JavaEnum<ValidatorFehlerart> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<ValidatorFehlerart> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, ValidatorFehlerart> = new Map<string, ValidatorFehlerart>();

	/**
	 * MUSS-Fehler : verhindert das Absenden der Statistik
	 */
	public static readonly MUSS : ValidatorFehlerart = new ValidatorFehlerart("MUSS", 0, );

	/**
	 * KANN-Fehler: Wahrscheinlicher Fehler, der erklärt werden muss, aber das Absenden der Statistik nicht verhindert
	 */
	public static readonly KANN : ValidatorFehlerart = new ValidatorFehlerart("KANN", 1, );

	/**
	 * HINWEIS: auf einen möglichen Fehler
	 */
	public static readonly HINWEIS : ValidatorFehlerart = new ValidatorFehlerart("HINWEIS", 2, );

	/**
	 * UNGENUTZT: der Validator soll nicht ausgeführt werden, wegen Ausschluss im Umfeld oder Schulform
	 */
	public static readonly UNGENUTZT : ValidatorFehlerart = new ValidatorFehlerart("UNGENUTZT", 3, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		ValidatorFehlerart.all_values_by_ordinal.push(this);
		ValidatorFehlerart.all_values_by_name.set(name, this);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<ValidatorFehlerart> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : ValidatorFehlerart | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.ValidatorFehlerart';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.validate.ValidatorFehlerart', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<ValidatorFehlerart>('de.svws_nrw.asd.validate.ValidatorFehlerart');

}

export function cast_de_svws_nrw_asd_validate_ValidatorFehlerart(obj : unknown) : ValidatorFehlerart {
	return obj as ValidatorFehlerart;
}
