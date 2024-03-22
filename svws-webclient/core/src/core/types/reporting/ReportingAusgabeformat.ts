import { JavaEnum } from '../../../java/lang/JavaEnum';

export class ReportingAusgabeformat extends JavaEnum<ReportingAusgabeformat> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<ReportingAusgabeformat> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, ReportingAusgabeformat> = new Map<string, ReportingAusgabeformat>();

	/**
	 * Report-Ausgabeformat ist HTML
	 */
	public static readonly HTML : ReportingAusgabeformat = new ReportingAusgabeformat("HTML", 0, 1);

	/**
	 * Report-Ausgabeformat ist PDF
	 */
	public static readonly PDF : ReportingAusgabeformat = new ReportingAusgabeformat("PDF", 1, 2);

	/**
	 * Die ID des Report-Ausgabeformats
	 */
	private readonly id : number;

	/**
	 * Erstellt ein neues Report-Ausgabeformat
	 * @param id Die ID des Report-Ausgabeformats
	 */
	private constructor(name : string, ordinal : number, id : number) {
		super(name, ordinal);
		ReportingAusgabeformat.all_values_by_ordinal.push(this);
		ReportingAusgabeformat.all_values_by_name.set(name, this);
		this.id = id;
	}

	/**
	 * Gibt die ID dieses Report-Ausgabeformats zurück
	 * @return Die ID dieses Report-Ausgabeformats
	 */
	public getId() : number {
		return this.id;
	}

	/**
	 * Diese Methode ermittelt das Report-Ausgabeformat anhand der übergebenen ID.
	 * @param id   	Die ID des gesuchten Report-Ausgabeformats
	 * @return 		Das Report-Ausgabeformat
	 */
	public static getByID(id : number) : ReportingAusgabeformat | null {
		for (const af of ReportingAusgabeformat.values())
			if (af.id === id)
				return af;
		return null;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<ReportingAusgabeformat> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : ReportingAusgabeformat | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.reporting.ReportingAusgabeformat';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.reporting.ReportingAusgabeformat', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_reporting_ReportingAusgabeformat(obj : unknown) : ReportingAusgabeformat {
	return obj as ReportingAusgabeformat;
}
