import { JavaEnum } from '../../../java/lang/JavaEnum';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';

export class FilterDomain extends JavaEnum<FilterDomain> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<FilterDomain> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, FilterDomain> = new Map<string, FilterDomain>();

	/**
	 * Schüler
	 */
	public static readonly SCHUELER: FilterDomain = new FilterDomain("SCHUELER", 0, "schueler");

	/**
	 * Ein eindeutiger Schlüssel zur Identifizierung der Domäne, z. B. in URLs
	 */
	private readonly key: string;

	/**
	 * Erzeugt eine neue Domäne für die Aufzählung.
	 *
	 * @param key Ein eindeutiger Schlüssel zur Identifizierung der Domäne
	 */
	private constructor(name: string, ordinal: number, key: string) {
		super(name, ordinal);
		FilterDomain.all_values_by_ordinal.push(this);
		FilterDomain.all_values_by_name.set(name, this);
		this.key = key;
	}

	/**
	 * Liefert den Schlüssel der Domäne zurück.
	 *
	 * @return Schlüssel
	 */
	public getKey(): string | null {
		return this.key;
	}

	/**
	 * Liefert die Domäne zu dem übergebenen Schlüssel zurück. Der Vergleich erfolgt ohne Beachtung der Groß- und Kleinschreibung.
	 *
	 * @param key der Schlüssel der Domäne
	 *
	 * @return die Domäne oder {@code null}, wenn es zu dem Schlüssel keine Domäne gibt
	 */
	public static getByKey(key: string | null): FilterDomain | null {
		for (const domain of this.values()) {
			if (JavaString.equalsIgnoreCase(domain.key, key)) {
				return domain;
			}
		}
		return null;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<FilterDomain> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): FilterDomain | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.filter.FilterDomain';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.types.filter.FilterDomain', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<FilterDomain>('de.svws_nrw.core.types.filter.FilterDomain');

}

export function cast_de_svws_nrw_core_types_filter_FilterDomain(obj: unknown): FilterDomain {
	return obj as FilterDomain;
}
