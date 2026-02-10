import { JavaEnum } from '../../../java/lang/JavaEnum';
import { Class } from '../../../java/lang/Class';

export class ReportingFilterVerknuepfung extends JavaEnum<ReportingFilterVerknuepfung> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<ReportingFilterVerknuepfung> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, ReportingFilterVerknuepfung> = new Map<string, ReportingFilterVerknuepfung>();

	/**
	 * Keine Verknüpfung (Einfaches Kriterium)
	 */
	public static readonly UNDEFINED: ReportingFilterVerknuepfung = new ReportingFilterVerknuepfung("UNDEFINED", 0, 0);

	/**
	 * Logisches UND
	 */
	public static readonly AND: ReportingFilterVerknuepfung = new ReportingFilterVerknuepfung("AND", 1, 1);

	/**
	 * Logisches ODER
	 */
	public static readonly OR: ReportingFilterVerknuepfung = new ReportingFilterVerknuepfung("OR", 2, 2);

	/**
	 * Die ID der Verknüpfung
	 */
	private readonly id: number;

	/**
	 * Erstellt eine neue Filter-Verknüpfung.
	 *
	 * @param id Die ID der Filter-Verknüpfung
	 */
	private constructor(name: string, ordinal: number, id: number) {
		super(name, ordinal);
		ReportingFilterVerknuepfung.all_values_by_ordinal.push(this);
		ReportingFilterVerknuepfung.all_values_by_name.set(name, this);
		this.id = id;
	}

	/**
	 * Gibt die ID der Filter-Verknüpfung zurück.
	 *
	 * @return Die ID der Filter-Verknüpfung
	 */
	public getId(): number {
		return this.id;
	}

	/**
	 * Diese Methode ermittelt die Filter-Verknüpfung anhand der übergebenen ID.
	 *
	 * @param id   	Die ID der gesuchten Filter-Verknüpfung
	 *
	 * @return 		Die Filter-Verknüpfung
	 */
	public static getByID(id: number): ReportingFilterVerknuepfung {
		for (const op of ReportingFilterVerknuepfung.values())
			if (op.id === id)
				return op;
		return ReportingFilterVerknuepfung.UNDEFINED;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<ReportingFilterVerknuepfung> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): ReportingFilterVerknuepfung | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.reporting.ReportingFilterVerknuepfung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.types.reporting.ReportingFilterVerknuepfung', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<ReportingFilterVerknuepfung>('de.svws_nrw.core.types.reporting.ReportingFilterVerknuepfung');

}

export function cast_de_svws_nrw_core_types_reporting_ReportingFilterVerknuepfung(obj: unknown): ReportingFilterVerknuepfung {
	return obj as ReportingFilterVerknuepfung;
}
