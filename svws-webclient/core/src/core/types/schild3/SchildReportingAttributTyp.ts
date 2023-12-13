import { JavaEnum } from '../../../java/lang/JavaEnum';

export class SchildReportingAttributTyp extends JavaEnum<SchildReportingAttributTyp> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<SchildReportingAttributTyp> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, SchildReportingAttributTyp> = new Map<string, SchildReportingAttributTyp>();

	/**
	 * Boolean
	 */
	public static readonly BOOLEAN : SchildReportingAttributTyp = new SchildReportingAttributTyp("BOOLEAN", 0, "boolean");

	/**
	 * Ganzzahl
	 */
	public static readonly INT : SchildReportingAttributTyp = new SchildReportingAttributTyp("INT", 1, "integer");

	/**
	 * Zahl, auch Kommazahlen
	 */
	public static readonly NUMBER : SchildReportingAttributTyp = new SchildReportingAttributTyp("NUMBER", 2, "number");

	/**
	 * Zeichenkette
	 */
	public static readonly STRING : SchildReportingAttributTyp = new SchildReportingAttributTyp("STRING", 3, "string");

	/**
	 * Mehrzeilige Zeichenkette
	 */
	public static readonly MEMO : SchildReportingAttributTyp = new SchildReportingAttributTyp("MEMO", 4, "memo");

	/**
	 * Datumsangabe
	 */
	public static readonly DATE : SchildReportingAttributTyp = new SchildReportingAttributTyp("DATE", 5, "date");

	/**
	 * Der JSON-Typ als String
	 */
	private readonly type : string;

	/**
	 * Initialisiert den Datentyp für die Aufzählung
	 *
	 * @param type   der JSON-Datentyp
	 */
	private constructor(name : string, ordinal : number, type : string) {
		super(name, ordinal);
		SchildReportingAttributTyp.all_values_by_ordinal.push(this);
		SchildReportingAttributTyp.all_values_by_name.set(name, this);
		this.type = type;
	}

	public toString() : string {
		return this.type;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<SchildReportingAttributTyp> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : SchildReportingAttributTyp | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.schild3.SchildReportingAttributTyp';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.schild3.SchildReportingAttributTyp', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_schild3_SchildReportingAttributTyp(obj : unknown) : SchildReportingAttributTyp {
	return obj as SchildReportingAttributTyp;
}
