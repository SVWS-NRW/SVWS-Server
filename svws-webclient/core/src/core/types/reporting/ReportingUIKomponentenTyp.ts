import { JavaEnum } from '../../../java/lang/JavaEnum';
import { Class } from '../../../java/lang/Class';

export class ReportingUIKomponentenTyp extends JavaEnum<ReportingUIKomponentenTyp> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<ReportingUIKomponentenTyp> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, ReportingUIKomponentenTyp> = new Map<string, ReportingUIKomponentenTyp>();

	/**
	 * KomponentenTyp wurde vom Typ her noch nicht festgelegt.
	 */
	public static readonly UNDEFINED: ReportingUIKomponentenTyp = new ReportingUIKomponentenTyp("UNDEFINED", 0, 0);

	/**
	 * KomponentenTyp Checkbox, der für die Eingabe von Boolean-Werten verwendet werden kann.
	 */
	public static readonly CHECKBOX: ReportingUIKomponentenTyp = new ReportingUIKomponentenTyp("CHECKBOX", 1, 1);

	/**
	 * KomponentenTyp Input, der für die Eingabe von String-Werten verwendet werden kann.
	 */
	public static readonly INPUT: ReportingUIKomponentenTyp = new ReportingUIKomponentenTyp("INPUT", 2, 2);

	/**
	 * KomponentenTyp Select, der für die Auswahl von Werten aus einer Liste verwendet werden kann.
	 */
	public static readonly SELECT: ReportingUIKomponentenTyp = new ReportingUIKomponentenTyp("SELECT", 3, 3);

	/**
	 * KomponentenTyp Textarea, der für die Eingabe von längeren String-Werten verwendet werden kann.
	 */
	public static readonly TEXTAREA: ReportingUIKomponentenTyp = new ReportingUIKomponentenTyp("TEXTAREA", 4, 4);

	/**
	 * KomponentenTyp NumberPicker, der für die Eingabe von numerischen Werten verwendet werden kann.
	 */
	public static readonly NUMBERPICKER: ReportingUIKomponentenTyp = new ReportingUIKomponentenTyp("NUMBERPICKER", 5, 5);

	/**
	 * KomponentenTyp DatePicker, der für die Eingabe von Datumswerten verwendet werden kann.
	 */
	public static readonly DATEPICKER: ReportingUIKomponentenTyp = new ReportingUIKomponentenTyp("DATEPICKER", 6, 6);

	/**
	 * Die ID des KomponentenTyps
	 */
	private readonly id: number;

	/**
	 * Erstellt einen neuen ReportingUIKomponentenTyp
	 *
	 * @param id Die ID des ReportingUIKomponentenTyp
	 */
	private constructor(name: string, ordinal: number, id: number) {
		super(name, ordinal);
		ReportingUIKomponentenTyp.all_values_by_ordinal.push(this);
		ReportingUIKomponentenTyp.all_values_by_name.set(name, this);
		this.id = id;
	}

	/**
	 * Gibt die ID dieses ReportingUIKomponentenTyp zurück
	 *
	 * @return Die ID dieses ReportingUIKomponentenTyp
	 */
	public getId(): number {
		return this.id;
	}

	/**
	 * Diese Methode ermittelt den ReportingUIKomponentenTyp anhand der übergebenen ID.
	 *
	 * @param id   	Die ID des gesuchten KomponentenTyps
	 *
	 * @return 		Der ReportingUIKomponentenTyp
	 */
	public static getByID(id: number): ReportingUIKomponentenTyp {
		for (const dp of ReportingUIKomponentenTyp.values())
			if (dp.id === id)
				return dp;
		return ReportingUIKomponentenTyp.UNDEFINED;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<ReportingUIKomponentenTyp> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): ReportingUIKomponentenTyp | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.reporting.ReportingUIKomponentenTyp';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.types.reporting.ReportingUIKomponentenTyp', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<ReportingUIKomponentenTyp>('de.svws_nrw.core.types.reporting.ReportingUIKomponentenTyp');

}

export function cast_de_svws_nrw_core_types_reporting_ReportingUIKomponentenTyp(obj: unknown): ReportingUIKomponentenTyp {
	return obj as ReportingUIKomponentenTyp;
}
