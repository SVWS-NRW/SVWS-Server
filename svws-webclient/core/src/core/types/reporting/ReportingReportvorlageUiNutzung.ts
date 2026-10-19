import { JavaEnum } from '../../../java/lang/JavaEnum';
import { Class } from '../../../java/lang/Class';

export class ReportingReportvorlageUiNutzung extends JavaEnum<ReportingReportvorlageUiNutzung> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<ReportingReportvorlageUiNutzung> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, ReportingReportvorlageUiNutzung> = new Map<string, ReportingReportvorlageUiNutzung>();

	/**
	 * Bereich der Schülerinnen und Schüler
	 */
	public static readonly SCHUELER: ReportingReportvorlageUiNutzung = new ReportingReportvorlageUiNutzung("SCHUELER", 0);

	/**
	 * Bereich der Klassen
	 */
	public static readonly KLASSEN: ReportingReportvorlageUiNutzung = new ReportingReportvorlageUiNutzung("KLASSEN", 1);

	/**
	 * Bereich der Kurse
	 */
	public static readonly KURSE: ReportingReportvorlageUiNutzung = new ReportingReportvorlageUiNutzung("KURSE", 2);

	/**
	 * Bereich der Lehrkräfte
	 */
	public static readonly LEHRER: ReportingReportvorlageUiNutzung = new ReportingReportvorlageUiNutzung("LEHRER", 3);

	/**
	 * Bereich der Fächer
	 */
	public static readonly FAECHER: ReportingReportvorlageUiNutzung = new ReportingReportvorlageUiNutzung("FAECHER", 4);

	private constructor(name: string, ordinal: number) {
		super(name, ordinal);
		ReportingReportvorlageUiNutzung.all_values_by_ordinal.push(this);
		ReportingReportvorlageUiNutzung.all_values_by_name.set(name, this);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<ReportingReportvorlageUiNutzung> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): ReportingReportvorlageUiNutzung | null {
		const tmp = this.all_values_by_name.get(name);
		return tmp ?? null;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.reporting.ReportingReportvorlageUiNutzung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.types.reporting.ReportingReportvorlageUiNutzung', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<ReportingReportvorlageUiNutzung>('de.svws_nrw.core.types.reporting.ReportingReportvorlageUiNutzung');

}

export function cast_de_svws_nrw_core_types_reporting_ReportingReportvorlageUiNutzung(obj: unknown): ReportingReportvorlageUiNutzung {
	return obj as ReportingReportvorlageUiNutzung;
}
