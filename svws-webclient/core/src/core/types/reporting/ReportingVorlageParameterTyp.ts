import { JavaEnum } from '../../../java/lang/JavaEnum';
import { Class } from '../../../java/lang/Class';

export class ReportingVorlageParameterTyp extends JavaEnum<ReportingVorlageParameterTyp> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<ReportingVorlageParameterTyp> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, ReportingVorlageParameterTyp> = new Map<string, ReportingVorlageParameterTyp>();

	/**
	 * Vorlage-Parameter wurde vom Typ her noch nicht festgelegt.
	 */
	public static readonly UNDEFINED: ReportingVorlageParameterTyp = new ReportingVorlageParameterTyp("UNDEFINED", 0, 0);

	/**
	 * Vorlage-Parameter des Typs BOOLEAN
	 */
	public static readonly BOOLEAN: ReportingVorlageParameterTyp = new ReportingVorlageParameterTyp("BOOLEAN", 1, 1);

	/**
	 * Vorlage-Parameter des Typs String
	 */
	public static readonly STRING: ReportingVorlageParameterTyp = new ReportingVorlageParameterTyp("STRING", 2, 2);

	/**
	 * Vorlage-Parameter des Typs LONG
	 */
	public static readonly LONG: ReportingVorlageParameterTyp = new ReportingVorlageParameterTyp("LONG", 3, 3);

	/**
	 * Vorlage-Parameter des Typs INTEGER
	 */
	public static readonly INTEGER: ReportingVorlageParameterTyp = new ReportingVorlageParameterTyp("INTEGER", 4, 4);

	/**
	 * Vorlage-Parameter des Typs DECIMAL
	 */
	public static readonly DECIMAL: ReportingVorlageParameterTyp = new ReportingVorlageParameterTyp("DECIMAL", 5, 5);

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
		ReportingVorlageParameterTyp.all_values_by_ordinal.push(this);
		ReportingVorlageParameterTyp.all_values_by_name.set(name, this);
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
	public static getByID(id: number): ReportingVorlageParameterTyp {
		for (const dp of ReportingVorlageParameterTyp.values())
			if (dp.id === id)
				return dp;
		return ReportingVorlageParameterTyp.UNDEFINED;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<ReportingVorlageParameterTyp> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): ReportingVorlageParameterTyp | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.reporting.ReportingVorlageParameterTyp';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.types.reporting.ReportingVorlageParameterTyp', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<ReportingVorlageParameterTyp>('de.svws_nrw.core.types.reporting.ReportingVorlageParameterTyp');

}

export function cast_de_svws_nrw_core_types_reporting_ReportingVorlageParameterTyp(obj: unknown): ReportingVorlageParameterTyp {
	return obj as ReportingVorlageParameterTyp;
}
