import { JavaEnum } from '../../../java/lang/JavaEnum';
import { Class } from '../../../java/lang/Class';

export class FilterAttributeType extends JavaEnum<FilterAttributeType> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<FilterAttributeType> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, FilterAttributeType> = new Map<string, FilterAttributeType>();

	/**
	 * Zeichenkette
	 */
	public static readonly STRING: FilterAttributeType = new FilterAttributeType("STRING", 0, "string");

	/**
	 * Zahl
	 */
	public static readonly NUMBER: FilterAttributeType = new FilterAttributeType("NUMBER", 1, "number");

	/**
	 * Datum
	 */
	public static readonly DATE: FilterAttributeType = new FilterAttributeType("DATE", 2, "date");

	/**
	 * Wahrheitswert
	 */
	public static readonly BOOLEAN: FilterAttributeType = new FilterAttributeType("BOOLEAN", 3, "boolean");

	/**
	 * Wert aus einem Enum
	 */
	public static readonly ENUM: FilterAttributeType = new FilterAttributeType("ENUM", 4, "enum");

	/**
	 * Wert aus einem CoreType
	 */
	public static readonly CORETYPE: FilterAttributeType = new FilterAttributeType("CORETYPE", 5, "coretype");

	/**
	 * Wert aus einem Katalog
	 */
	public static readonly CATALOG: FilterAttributeType = new FilterAttributeType("CATALOG", 6, "catalog");

	/**
	 * Ein eindeutiger Schlüssel zur Identifizierung des Enum-Eintrags
	 */
	private readonly key: string;

	/**
	 * @param key Ein eindeutiger Schlüssel zur Identifizierung des Enum-Eintrags
	 */
	private constructor(name: string, ordinal: number, key: string) {
		super(name, ordinal);
		FilterAttributeType.all_values_by_ordinal.push(this);
		FilterAttributeType.all_values_by_name.set(name, this);
		this.key = key;
	}

	/**
	 * Liefert den Schlüssel zurück.
	 *
	 * @return Schlüssel
	 */
	public getKey(): string | null {
		return this.key;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<FilterAttributeType> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): FilterAttributeType | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.filter.FilterAttributeType';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.types.filter.FilterAttributeType', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<FilterAttributeType>('de.svws_nrw.core.types.filter.FilterAttributeType');

}

export function cast_de_svws_nrw_core_types_filter_FilterAttributeType(obj: unknown): FilterAttributeType {
	return obj as FilterAttributeType;
}
