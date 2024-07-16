import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { JavaString } from '../../../java/lang/JavaString';

export class ReportingReportvorlage extends JavaEnum<ReportingReportvorlage> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<ReportingReportvorlage> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, ReportingReportvorlage> = new Map<string, ReportingReportvorlage>();

	/**
	 * Report-Vorlage: GOSt - Klausurplanung - Klausurtermine-Kurse
	 */
	public static readonly GOST_KLAUSURPLANUNG_v_KLAUSURTERMINE_MIT_KURSEN : ReportingReportvorlage = new ReportingReportvorlage("GOST_KLAUSURPLANUNG_v_KLAUSURTERMINE_MIT_KURSEN", 0, "GostKlausurplanung-KlausurtermineMitKursen");

	/**
	 * Report-Vorlage: GOSt - Kursplanung - Kurs-Kurschüler
	 */
	public static readonly GOST_KURSPLANUNG_v_KURS_MIT_KURSSCHUELERN : ReportingReportvorlage = new ReportingReportvorlage("GOST_KURSPLANUNG_v_KURS_MIT_KURSSCHUELERN", 1, "GostKursplanung-KursMitKursschuelern");

	/**
	 * Report-Vorlage: GOSt - Kursplanung - Kurse-Statistikwerte
	 */
	public static readonly GOST_KURSPLANUNG_v_KURSE_MIT_STATISTIKWERTEN : ReportingReportvorlage = new ReportingReportvorlage("GOST_KURSPLANUNG_v_KURSE_MIT_STATISTIKWERTEN", 2, "GostKursplanung-KurseMitStatistikwerten");

	/**
	 * Report-Vorlage: GOSt - Kursplanung - Schüler-Kurse
	 */
	public static readonly GOST_KURSPLANUNG_v_SCHUELER_MIT_KURSEN : ReportingReportvorlage = new ReportingReportvorlage("GOST_KURSPLANUNG_v_SCHUELER_MIT_KURSEN", 3, "GostKursplanung-SchuelerMitKursen");

	/**
	 * Report-Vorlage: GOSt - Kursplanung - Schüler-Schienen-Kurse
	 */
	public static readonly GOST_KURSPLANUNG_v_SCHUELER_MIT_SCHIENEN_KURSEN : ReportingReportvorlage = new ReportingReportvorlage("GOST_KURSPLANUNG_v_SCHUELER_MIT_SCHIENEN_KURSEN", 4, "GostKursplanung-SchuelerMitSchienenKursen");

	/**
	 * Report-Vorlage: GOSt - Abitur - APO - Anlage 12 (Abiturzeugnis)
	 */
	public static readonly SCHUELER_v_GOST_ABITUR_APO_ANLAGE_12 : ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_v_GOST_ABITUR_APO_ANLAGE_12", 5, "Schueler-GostAbiturApoAnlage12");

	/**
	 * Report-Vorlage: GOSt - Laufbahnplanung - Ergebnisübersicht
	 */
	public static readonly SCHUELER_v_GOST_LAUFBAHNPLANUNG_ERGEBNISUEBERSICHT : ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_v_GOST_LAUFBAHNPLANUNG_ERGEBNISUEBERSICHT", 6, "Schueler-GostLaufbahnplanungErgebnisuebersicht");

	/**
	 * Report-Vorlage: GOSt - Laufbahnplanung - Wahlbogen
	 */
	public static readonly SCHUELER_v_GOST_LAUFBAHNPLANUNG_WAHLBOGEN : ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_v_GOST_LAUFBAHNPLANUNG_WAHLBOGEN", 7, "Schueler-GostLaufbahnplanungWahlbogen");

	/**
	 * Die Bezeichnung der Report-Vorlage
	 */
	private readonly bezeichnung : string | null;

	/**
	 * Erstellt eine neue Report-Vorlage
	 * @param bezeichnung Der Name der Report-Vorlage
	 */
	private constructor(name : string, ordinal : number, bezeichnung : string | null) {
		super(name, ordinal);
		ReportingReportvorlage.all_values_by_ordinal.push(this);
		ReportingReportvorlage.all_values_by_name.set(name, this);
		this.bezeichnung = bezeichnung;
	}

	/**
	 * Gibt die Bezeichnung dieser Report-Vorlage zurück
	 * @return Die Bezeichnung dieser Report-Vorlage
	 */
	public getBezeichnung() : string | null {
		return (this.bezeichnung !== null) ? this.bezeichnung : "";
	}

	/**
	 * Diese Methode ermittelt die Report-Vorlage anhand der übergebenen Bezeichnung.
	 * @param bezeichnung Die Bezeichnung der Report-Vorlage
	 * @return Die Report-Vorlage
	 */
	public static getByBezeichnung(bezeichnung : string | null) : ReportingReportvorlage | null {
		if ((bezeichnung === null) || (JavaString.isEmpty(bezeichnung)))
			return null;
		for (const rv of ReportingReportvorlage.values())
			if (JavaObject.equalsTranspiler(rv.bezeichnung, (bezeichnung)))
				return rv;
		return null;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<ReportingReportvorlage> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : ReportingReportvorlage | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.reporting.ReportingReportvorlage';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.reporting.ReportingReportvorlage', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_reporting_ReportingReportvorlage(obj : unknown) : ReportingReportvorlage {
	return obj as ReportingReportvorlage;
}
