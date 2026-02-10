import { JavaEnum } from '../../../java/lang/JavaEnum';
import { Class } from '../../../java/lang/Class';

export class ReportingEMailEmpfaengerTyp extends JavaEnum<ReportingEMailEmpfaengerTyp> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<ReportingEMailEmpfaengerTyp> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, ReportingEMailEmpfaengerTyp> = new Map<string, ReportingEMailEmpfaengerTyp>();

	/**
	 * Undefiniert, ein Versand kann nicht stattfinden.
	 */
	public static readonly UNDEFINED: ReportingEMailEmpfaengerTyp = new ReportingEMailEmpfaengerTyp("UNDEFINED", 0, 0);

	/**
	 * Versand an Schüler. Die IDs werden als Schüler-IDs interpretiert.
	 */
	public static readonly SCHUELER: ReportingEMailEmpfaengerTyp = new ReportingEMailEmpfaengerTyp("SCHUELER", 1, 1);

	/**
	 * Versand an Lehrkräfte. Die IDs werden als Lehrer-IDs interpretiert.
	 */
	public static readonly LEHRER: ReportingEMailEmpfaengerTyp = new ReportingEMailEmpfaengerTyp("LEHRER", 2, 2);

	/**
	 * Versand an Klassenlehrer (Leitungen). Die IDs werden als Klassen-IDs interpretiert, welche dann intern die Klassenleitung-IDs ermitteln.
	 */
	public static readonly KLASSENLEHRER: ReportingEMailEmpfaengerTyp = new ReportingEMailEmpfaengerTyp("KLASSENLEHRER", 3, 3);

	/**
	 * Versand an Kurslehrer (im Kurs unterrichtende Lehrer). Die IDs werden als Kurs-IDs interpretiert, welche dann intern die Kurslehrer-IDs ermitteln.
	 */
	public static readonly KURSLEHRER: ReportingEMailEmpfaengerTyp = new ReportingEMailEmpfaengerTyp("KURSLEHRER", 4, 4);

	/**
	 * Versand an Kurslehrer (im Kurs unterrichtende Lehrer) im Rahmen der Kursplanung für die Oberstufe. Die IDs werden als Kurs-IDs in der Kursplanung
	 *  interpretiert, welche dann intern die Kurslehrer-IDs ermitteln.
	 */
	public static readonly GOSTKURSPLANUNG_KURSLEHRER: ReportingEMailEmpfaengerTyp = new ReportingEMailEmpfaengerTyp("GOSTKURSPLANUNG_KURSLEHRER", 5, 5);

	/**
	 * Die ID des Reporting-E-Mail-Empfaenger-Typs
	 */
	private readonly id: number;

	/**
	 * Erstellt ein neues Reporting-E-Mail-Empfaenger-Typ-Objekt.
	 * @param id Die ID des Reporting-E-Mail-Empfaenger-Typs
	 */
	private constructor(name: string, ordinal: number, id: number) {
		super(name, ordinal);
		ReportingEMailEmpfaengerTyp.all_values_by_ordinal.push(this);
		ReportingEMailEmpfaengerTyp.all_values_by_name.set(name, this);
		this.id = id;
	}

	/**
	 * Gibt die ID dieses Reporting-E-Mail-Empfaenger-Typs zurück
	 * @return Die ID dieses Reporting-E-Mail-Empfaenger-Typs
	 */
	public getId(): number {
		return this.id;
	}

	/**
	 * Diese Methode ermittelt den Reporting-E-Mail-Empfaenger-Typ anhand der übergebenen ID.
	 * @param id   	Die ID des gesuchten Reporting-E-Mail-Empfaenger-Typs
	 * @return 		Der Reporting-E-Mail-Empfaenger-Typ
	 */
	public static getByID(id: number): ReportingEMailEmpfaengerTyp | null {
		for (const em of ReportingEMailEmpfaengerTyp.values())
			if (em.id === id)
				return em;
		return ReportingEMailEmpfaengerTyp.UNDEFINED;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<ReportingEMailEmpfaengerTyp> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): ReportingEMailEmpfaengerTyp | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.reporting.ReportingEMailEmpfaengerTyp';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.types.reporting.ReportingEMailEmpfaengerTyp', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<ReportingEMailEmpfaengerTyp>('de.svws_nrw.core.types.reporting.ReportingEMailEmpfaengerTyp');

}

export function cast_de_svws_nrw_core_types_reporting_ReportingEMailEmpfaengerTyp(obj: unknown): ReportingEMailEmpfaengerTyp {
	return obj as ReportingEMailEmpfaengerTyp;
}
