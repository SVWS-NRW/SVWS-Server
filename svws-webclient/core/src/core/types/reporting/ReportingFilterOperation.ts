import { JavaEnum } from '../../../java/lang/JavaEnum';
import { Class } from '../../../java/lang/Class';

export class ReportingFilterOperation extends JavaEnum<ReportingFilterOperation> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<ReportingFilterOperation> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, ReportingFilterOperation> = new Map<string, ReportingFilterOperation>();

	/**
	 * Undefiniert, eine Filter-Operation wurde nicht angegeben.
	 */
	public static readonly UNDEFINED: ReportingFilterOperation = new ReportingFilterOperation("UNDEFINED", 0, 0);

	/**
	 * Prüfung auf Gleichheit
	 */
	public static readonly EQUAL: ReportingFilterOperation = new ReportingFilterOperation("EQUAL", 1, 1);

	/**
	 * Prüfung auf Ungleichheit
	 */
	public static readonly NOT_EQUAL: ReportingFilterOperation = new ReportingFilterOperation("NOT_EQUAL", 2, 2);

	/**
	 * Prüfung, ob der Wert im Filterwert enthalten ist (bei Strings)
	 */
	public static readonly CONTAINS: ReportingFilterOperation = new ReportingFilterOperation("CONTAINS", 3, 3);

	/**
	 * Prüfung, ob der Wert mit dem Filterwert beginnt (bei Strings)
	 */
	public static readonly STARTS_WITH: ReportingFilterOperation = new ReportingFilterOperation("STARTS_WITH", 4, 4);

	/**
	 * Prüfung, ob der Wert mit dem Filterwert endet (bei Strings)
	 */
	public static readonly ENDS_WITH: ReportingFilterOperation = new ReportingFilterOperation("ENDS_WITH", 5, 5);

	/**
	 * Prüfung auf größer als
	 */
	public static readonly GREATER: ReportingFilterOperation = new ReportingFilterOperation("GREATER", 6, 6);

	/**
	 * Prüfung auf größer oder gleich
	 */
	public static readonly GREATER_OR_EQUAL: ReportingFilterOperation = new ReportingFilterOperation("GREATER_OR_EQUAL", 7, 7);

	/**
	 * Prüfung auf kleiner als
	 */
	public static readonly LESS: ReportingFilterOperation = new ReportingFilterOperation("LESS", 8, 8);

	/**
	 * Prüfung auf kleiner oder gleich
	 */
	public static readonly LESS_OR_EQUAL: ReportingFilterOperation = new ReportingFilterOperation("LESS_OR_EQUAL", 9, 9);

	/**
	 * Prüfung, ob der Wert in einer Liste von Werten enthalten ist
	 */
	public static readonly IN: ReportingFilterOperation = new ReportingFilterOperation("IN", 10, 10);

	/**
	 * Prüfung, ob der Wert zwischen zwei Werten liegt (inklusiv)
	 */
	public static readonly BETWEEN: ReportingFilterOperation = new ReportingFilterOperation("BETWEEN", 11, 11);

	/**
	 * Die ID der Filter-Operation
	 */
	private readonly id: number;

	/**
	 * Erstellt eine neue Filter-Operation
	 *
	 * @param id Die ID der Filter-Operation
	 */
	private constructor(name: string, ordinal: number, id: number) {
		super(name, ordinal);
		ReportingFilterOperation.all_values_by_ordinal.push(this);
		ReportingFilterOperation.all_values_by_name.set(name, this);
		this.id = id;
	}

	/**
	 * Gibt die ID der Filter-Operation zurück
	 *
	 * @return Die ID der Filter-Operation
	 */
	public getId(): number {
		return this.id;
	}

	/**
	 * Diese Methode ermittelt die Filter-Operation anhand der übergebenen ID.
	 *
	 * @param id   	Die ID der gesuchten Filter-Operation
	 *
	 * @return 		Die Filter-Operation
	 */
	public static getByID(id: number): ReportingFilterOperation {
		for (const op of ReportingFilterOperation.values())
			if (op.id === id)
				return op;
		return ReportingFilterOperation.UNDEFINED;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<ReportingFilterOperation> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): ReportingFilterOperation | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.reporting.ReportingFilterOperation';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.types.reporting.ReportingFilterOperation', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<ReportingFilterOperation>('de.svws_nrw.core.types.reporting.ReportingFilterOperation');

}

export function cast_de_svws_nrw_core_types_reporting_ReportingFilterOperation(obj: unknown): ReportingFilterOperation {
	return obj as ReportingFilterOperation;
}
