import { JavaEnum } from '../../../java/lang/JavaEnum';
import { Class } from '../../../java/lang/Class';

export class ReportingReportvorlageParameterTyp extends JavaEnum<ReportingReportvorlageParameterTyp> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<ReportingReportvorlageParameterTyp> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, ReportingReportvorlageParameterTyp> = new Map<string, ReportingReportvorlageParameterTyp>();

	/**
	 * Vorlage-Parameter wurde vom Typ her noch nicht festgelegt.
	 */
	public static readonly UNDEFINED: ReportingReportvorlageParameterTyp = new ReportingReportvorlageParameterTyp("UNDEFINED", 0, 0);

	/**
	 * Vorlage-Parameter des Typs BOOLEAN
	 */
	public static readonly BOOLEAN: ReportingReportvorlageParameterTyp = new ReportingReportvorlageParameterTyp("BOOLEAN", 1, 1);

	/**
	 * Vorlage-Parameter des Typs String
	 */
	public static readonly STRING: ReportingReportvorlageParameterTyp = new ReportingReportvorlageParameterTyp("STRING", 2, 2);

	/**
	 * Vorlage-Parameter des Typs LONG
	 */
	public static readonly LONG: ReportingReportvorlageParameterTyp = new ReportingReportvorlageParameterTyp("LONG", 3, 3);

	/**
	 * Vorlage-Parameter des Typs INTEGER
	 */
	public static readonly INTEGER: ReportingReportvorlageParameterTyp = new ReportingReportvorlageParameterTyp("INTEGER", 4, 4);

	/**
	 * Vorlage-Parameter des Typs DECIMAL
	 */
	public static readonly DECIMAL: ReportingReportvorlageParameterTyp = new ReportingReportvorlageParameterTyp("DECIMAL", 5, 5);

	/**
	 * Die ID des Vorlage-Parameter-Typs
	 */
	private readonly id: number;

	/**
	 * Erstellt einen neuen ReportingDVorlageParameterTyp
	 *
	 * @param id Die ID des ReportingVorlageParameterTyp
	 */
	private constructor(name: string, ordinal: number, id: number) {
		super(name, ordinal);
		ReportingReportvorlageParameterTyp.all_values_by_ordinal.push(this);
		ReportingReportvorlageParameterTyp.all_values_by_name.set(name, this);
		this.id = id;
	}

	/**
	 * Gibt die ID dieses ReportingDVorlageParameterTyp zurück
	 *
	 * @return Die ID dieses ReportingDVorlageParameterTyp
	 */
	public getId(): number {
		return this.id;
	}

	/**
	 * Diese Methode ermittelt den ReportingDVorlageParameterTyp anhand der übergebenen ID.
	 *
	 * @param id   	Die ID des gesuchten Vorlage-Parameters
	 *
	 * @return 		Der ReportingDVorlageParameterTyp
	 */
	public static getByID(id: number): ReportingReportvorlageParameterTyp {
		for (const dp of ReportingReportvorlageParameterTyp.values()) {
			if (dp.id === id) {
				return dp;
			}
		}
		return ReportingReportvorlageParameterTyp.UNDEFINED;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<ReportingReportvorlageParameterTyp> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): ReportingReportvorlageParameterTyp | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.reporting.ReportingReportvorlageParameterTyp';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.types.reporting.ReportingReportvorlageParameterTyp', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<ReportingReportvorlageParameterTyp>('de.svws_nrw.core.types.reporting.ReportingReportvorlageParameterTyp');

}

export function cast_de_svws_nrw_core_types_reporting_ReportingReportvorlageParameterTyp(obj: unknown): ReportingReportvorlageParameterTyp {
	return obj as ReportingReportvorlageParameterTyp;
}
