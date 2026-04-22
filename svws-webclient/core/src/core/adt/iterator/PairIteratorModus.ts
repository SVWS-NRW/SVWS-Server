import { JavaEnum } from '../../../java/lang/JavaEnum';
import { Class } from '../../../java/lang/Class';

export class PairIteratorModus extends JavaEnum<PairIteratorModus> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<PairIteratorModus> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, PairIteratorModus> = new Map<string, PairIteratorModus>();

	/**
	 * Alle Paare (i, j), auch i == j. Liefert insgesamt n² Paare.
	 */
	public static readonly ALL: PairIteratorModus = new PairIteratorModus("ALL", 0, );

	/**
	 * Alle Paare (i, j) mit i != j. Liefert insgesamt n² - n Paare.
	 */
	public static readonly NO_EQUAL: PairIteratorModus = new PairIteratorModus("NO_EQUAL", 1, );

	/**
	 * Alle Paare (i, j) mit i &lt; j. Liefert insgesamt (n² - n)/2 Paare, somit keine Duplikate.
	 */
	public static readonly LOWER_ONLY: PairIteratorModus = new PairIteratorModus("LOWER_ONLY", 2, );

	private constructor(name: string, ordinal: number) {
		super(name, ordinal);
		PairIteratorModus.all_values_by_ordinal.push(this);
		PairIteratorModus.all_values_by_name.set(name, this);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<PairIteratorModus> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): PairIteratorModus | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.adt.iterator.PairIteratorModus';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.adt.iterator.PairIteratorModus', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<PairIteratorModus>('de.svws_nrw.core.adt.iterator.PairIteratorModus');

}

export function cast_de_svws_nrw_core_adt_iterator_PairIteratorModus(obj: unknown): PairIteratorModus {
	return obj as PairIteratorModus;
}
