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
	 * Daten-Context ist SCHUELER
	 */
	public static readonly SCHUELER: ReportingReportvorlageDatenContext = new ReportingReportvorlageDatenContext("SCHUELER", 0, "SCHUELER");

	/**
	 * Daten-Context ist LEHRER
	 */
	public static readonly LEHRER: ReportingReportvorlageDatenContext = new ReportingReportvorlageDatenContext("LEHRER", 1, "LEHRER");

	/**
	 * Daten-Context ist KLASSEN
	 */
	public static readonly KLASSEN: ReportingReportvorlageDatenContext = new ReportingReportvorlageDatenContext("KLASSEN", 2, "KLASSEN");

	/**
	 * Daten-Context ist KURSE
	 */
	public static readonly KURSE: ReportingReportvorlageDatenContext = new ReportingReportvorlageDatenContext("KURSE", 3, "KURSE");

	/**
	 * Daten-Context ist GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG
	 */
	public static readonly GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG: ReportingReportvorlageDatenContext = new ReportingReportvorlageDatenContext("GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG", 4, "GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG");

	/**
	 * Daten-Context ist GOST_KURSPLANUNG
	 */
	public static readonly GOST_KURSPLANUNG: ReportingReportvorlageDatenContext = new ReportingReportvorlageDatenContext("GOST_KURSPLANUNG", 5, "GOST_KURSPLANUNG");

	/**
	 * Daten-Context ist GOST_KLAUSURPLANUNG
	 */
	public static readonly GOST_KLAUSURPLANUNG: ReportingReportvorlageDatenContext = new ReportingReportvorlageDatenContext("GOST_KLAUSURPLANUNG", 6, "GOST_KLAUSURPLANUNG");

	/**
	 * Daten-Context ist STUNDENPLANUNG
	 */
	public static readonly STUNDENPLANUNG: ReportingReportvorlageDatenContext = new ReportingReportvorlageDatenContext("STUNDENPLANUNG", 7, "STUNDENPLANUNG");

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
