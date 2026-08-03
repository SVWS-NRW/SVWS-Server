import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';

export class ReportingReportvorlageDatenContext extends JavaEnum<ReportingReportvorlageDatenContext> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<ReportingReportvorlageDatenContext> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, ReportingReportvorlageDatenContext> = new Map<string, ReportingReportvorlageDatenContext>();

	/**
	 * Daten-Context ist SCHUELER - Schülerdaten ohne weitere Zusatzprüfungen
	 */
	public static readonly SCHUELER: ReportingReportvorlageDatenContext = new ReportingReportvorlageDatenContext("SCHUELER", 0, "SCHUELER");

	/**
	 * Daten-Context ist SCHUELER_GOST_LAUFBAHNPLANUNG - Schülerdaten mit den Beratungs- und Abiturdaten der GOSt-Laufbahnplanung
	 */
	public static readonly SCHUELER_GOST_LAUFBAHNPLANUNG: ReportingReportvorlageDatenContext = new ReportingReportvorlageDatenContext("SCHUELER_GOST_LAUFBAHNPLANUNG", 1, "SCHUELER_GOST_LAUFBAHNPLANUNG");

	/**
	 * Daten-Context ist SCHUELER_GOST_ABITUR - Schülerdaten mit den Abiturdaten der GOSt
	 */
	public static readonly SCHUELER_GOST_ABITUR: ReportingReportvorlageDatenContext = new ReportingReportvorlageDatenContext("SCHUELER_GOST_ABITUR", 2, "SCHUELER_GOST_ABITUR");

	/**
	 * Daten-Context ist LEHRER - Daten der Lehrkräfte
	 */
	public static readonly LEHRER: ReportingReportvorlageDatenContext = new ReportingReportvorlageDatenContext("LEHRER", 3, "LEHRER");

	/**
	 * Daten-Context ist KLASSEN - Daten der Klassen
	 */
	public static readonly KLASSEN: ReportingReportvorlageDatenContext = new ReportingReportvorlageDatenContext("KLASSEN", 4, "KLASSEN");

	/**
	 * Daten-Context ist KURSE - Daten der Kurse
	 */
	public static readonly KURSE: ReportingReportvorlageDatenContext = new ReportingReportvorlageDatenContext("KURSE", 5, "KURSE");

	/**
	 * Daten-Context ist GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG - Fachwahlstatistiken der GOSt-Laufbahnplanung eines Abiturjahrgangs
	 */
	public static readonly GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG: ReportingReportvorlageDatenContext = new ReportingReportvorlageDatenContext("GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG", 6, "GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG");

	/**
	 * Daten-Context ist GOST_KURSPLANUNG_KURSE - Blockungsergebnis der GOSt-Kursplanung aus Sicht der Kurse
	 */
	public static readonly GOST_KURSPLANUNG_KURSE: ReportingReportvorlageDatenContext = new ReportingReportvorlageDatenContext("GOST_KURSPLANUNG_KURSE", 7, "GOST_KURSPLANUNG_KURSE");

	/**
	 * Daten-Context ist GOST_KURSPLANUNG_SCHUELER - Blockungsergebnis der GOSt-Kursplanung aus Sicht der Schüler
	 */
	public static readonly GOST_KURSPLANUNG_SCHUELER: ReportingReportvorlageDatenContext = new ReportingReportvorlageDatenContext("GOST_KURSPLANUNG_SCHUELER", 8, "GOST_KURSPLANUNG_SCHUELER");

	/**
	 * Daten-Context ist GOST_KLAUSURPLANUNG_SCHUELER - Klausurplan der GOSt aus Sicht der Schüler
	 */
	public static readonly GOST_KLAUSURPLANUNG_SCHUELER: ReportingReportvorlageDatenContext = new ReportingReportvorlageDatenContext("GOST_KLAUSURPLANUNG_SCHUELER", 9, "GOST_KLAUSURPLANUNG_SCHUELER");

	/**
	 * Daten-Context ist GOST_KLAUSURPLANUNG_TERMINE - Klausurplan der GOSt aus Sicht der Klausurtermine
	 */
	public static readonly GOST_KLAUSURPLANUNG_TERMINE: ReportingReportvorlageDatenContext = new ReportingReportvorlageDatenContext("GOST_KLAUSURPLANUNG_TERMINE", 10, "GOST_KLAUSURPLANUNG_TERMINE");

	/**
	 * Daten-Context ist STUNDENPLANUNG_FACH - Stundenplan aus Sicht der Fächer
	 */
	public static readonly STUNDENPLANUNG_FACH: ReportingReportvorlageDatenContext = new ReportingReportvorlageDatenContext("STUNDENPLANUNG_FACH", 11, "STUNDENPLANUNG_FACH");

	/**
	 * Daten-Context ist STUNDENPLANUNG_KLASSEN - Stundenplan aus Sicht der Klassen
	 */
	public static readonly STUNDENPLANUNG_KLASSEN: ReportingReportvorlageDatenContext = new ReportingReportvorlageDatenContext("STUNDENPLANUNG_KLASSEN", 12, "STUNDENPLANUNG_KLASSEN");

	/**
	 * Daten-Context ist STUNDENPLANUNG_LEHRER - Stundenplan aus Sicht der Lehrkräfte
	 */
	public static readonly STUNDENPLANUNG_LEHRER: ReportingReportvorlageDatenContext = new ReportingReportvorlageDatenContext("STUNDENPLANUNG_LEHRER", 13, "STUNDENPLANUNG_LEHRER");

	/**
	 * Daten-Context ist STUNDENPLANUNG_RAUM - Stundenplan aus Sicht der Räume
	 */
	public static readonly STUNDENPLANUNG_RAUM: ReportingReportvorlageDatenContext = new ReportingReportvorlageDatenContext("STUNDENPLANUNG_RAUM", 14, "STUNDENPLANUNG_RAUM");

	/**
	 * Daten-Context ist STUNDENPLANUNG_SCHUELER - Stundenplan aus Sicht der Schüler
	 */
	public static readonly STUNDENPLANUNG_SCHUELER: ReportingReportvorlageDatenContext = new ReportingReportvorlageDatenContext("STUNDENPLANUNG_SCHUELER", 15, "STUNDENPLANUNG_SCHUELER");

	/**
	 * Die Bezeichnung des Daten-Kontexts
	 */
	private readonly bezeichnung: string;

	/**
	 * Erstellt eine neue DatenContextDefinition mit der angegebenen Bezeichnung.
	 *
	 * @param bezeichnung die Bezeichnung des Daten-Kontexts
	 */
	private constructor(name: string, ordinal: number, bezeichnung: string) {
		super(name, ordinal);
		ReportingReportvorlageDatenContext.all_values_by_ordinal.push(this);
		ReportingReportvorlageDatenContext.all_values_by_name.set(name, this);
		this.bezeichnung = bezeichnung;
	}

	/**
	 * Gibt die Bezeichnung des Daten-Kontexts zurück.
	 *
	 * @return die Bezeichnung
	 */
	public getBezeichnung(): string {
		return this.bezeichnung;
	}

	/**
	 * Gibt den Daten-Kontext anhand der Bezeichnung zurück.
	 *
	 * @param bezeichnung die Bezeichnung des Daten-Kontexts
	 *
	 * @return der Daten-Kontext oder null, wenn die Bezeichnung nicht gefunden wurde
	 */
	public getByBezeichnung(bezeichnung: string): ReportingReportvorlageDatenContext | null {
		if (JavaString.isEmpty(bezeichnung)) {
			return null;
		}
		for (const rdc of ReportingReportvorlageDatenContext.values()) {
			if (JavaObject.equalsTranspiler(rdc.bezeichnung, (bezeichnung))) {
				return rdc;
			}
		}
		return null;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<ReportingReportvorlageDatenContext> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): ReportingReportvorlageDatenContext | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.reporting.ReportingReportvorlageDatenContext';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.types.reporting.ReportingReportvorlageDatenContext', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<ReportingReportvorlageDatenContext>('de.svws_nrw.core.types.reporting.ReportingReportvorlageDatenContext');

}

export function cast_de_svws_nrw_core_types_reporting_ReportingReportvorlageDatenContext(obj: unknown): ReportingReportvorlageDatenContext {
	return obj as ReportingReportvorlageDatenContext;
}
