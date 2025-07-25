import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';
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
	 * Report-Vorlage: GOSt - Klausurplanung - Schueler-Klausuren
	 */
	public static readonly GOST_KLAUSURPLANUNG_v_SCHUELER_MIT_KLAUSUREN : ReportingReportvorlage = new ReportingReportvorlage("GOST_KLAUSURPLANUNG_v_SCHUELER_MIT_KLAUSUREN", 1, "GostKlausurplanung-SchuelerMitKlausuren");

	/**
	 * Report-Vorlage: GOSt - Kursplanung - Kurs-Kurschüler
	 */
	public static readonly GOST_KURSPLANUNG_v_KURS_MIT_KURSSCHUELERN : ReportingReportvorlage = new ReportingReportvorlage("GOST_KURSPLANUNG_v_KURS_MIT_KURSSCHUELERN", 2, "GostKursplanung-KursMitKursschuelern");

	/**
	 * Report-Vorlage: GOSt - Kursplanung - Kurse-Statistikwerte
	 */
	public static readonly GOST_KURSPLANUNG_v_KURSE_MIT_STATISTIKWERTEN : ReportingReportvorlage = new ReportingReportvorlage("GOST_KURSPLANUNG_v_KURSE_MIT_STATISTIKWERTEN", 3, "GostKursplanung-KurseMitStatistikwerten");

	/**
	 * Report-Vorlage: GOSt - Kursplanung - Schüler-Kurse
	 */
	public static readonly GOST_KURSPLANUNG_v_SCHUELER_MIT_KURSEN : ReportingReportvorlage = new ReportingReportvorlage("GOST_KURSPLANUNG_v_SCHUELER_MIT_KURSEN", 4, "GostKursplanung-SchuelerMitKursen");

	/**
	 * Report-Vorlage: GOSt - Kursplanung - Schüler-Schienen-Kurse
	 */
	public static readonly GOST_KURSPLANUNG_v_SCHUELER_MIT_SCHIENEN_KURSEN : ReportingReportvorlage = new ReportingReportvorlage("GOST_KURSPLANUNG_v_SCHUELER_MIT_SCHIENEN_KURSEN", 5, "GostKursplanung-SchuelerMitSchienenKursen");

	/**
	 * Report-Vorlage: GOSt - Laufbahnplanung - Abiturjahrgang - Fachwahlstatistiken
	 */
	public static readonly GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG_v_FACHWAHLSTATISTIKEN : ReportingReportvorlage = new ReportingReportvorlage("GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG_v_FACHWAHLSTATISTIKEN", 6, "GostLaufbahnplanung-Abiturjahrgang-Fachwahlstatistiken");

	/**
	 * Report-Vorlage: Klasse - Schülerstammdaten - Liste
	 */
	public static readonly KLASSEN_v_KLASSE_SCHUELER_STAMMDATENLISTE : ReportingReportvorlage = new ReportingReportvorlage("KLASSEN_v_KLASSE_SCHUELER_STAMMDATENLISTE", 7, "Klasse-Schueler-Stammdatenliste");

	/**
	 * Report-Vorlage: Kurs - Schülerstammdaten - Liste
	 */
	public static readonly KURSE_v_KURS_SCHUELER_STAMMDATENLISTE : ReportingReportvorlage = new ReportingReportvorlage("KURSE_v_KURS_SCHUELER_STAMMDATENLISTE", 8, "Kurs-Schueler-Stammdatenliste");

	/**
	 * Report-Vorlage: Lehrer - Stammdaten - Liste
	 */
	public static readonly LEHRER_v_STAMMDATENLISTE : ReportingReportvorlage = new ReportingReportvorlage("LEHRER_v_STAMMDATENLISTE", 9, "Lehrer-Stammdatenliste");

	/**
	 * Report-Vorlage: GOSt - Abitur - APO - Anlage 12 (Abiturzeugnis) - Din-A4
	 */
	public static readonly SCHUELER_v_GOST_ABITUR_APO_ANLAGE_12_A4 : ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_v_GOST_ABITUR_APO_ANLAGE_12_A4", 10, "Schueler-GostAbiturApoAnlage12-A4");

	/**
	 * Report-Vorlage: GOSt - Abitur - APO - Anlage 12 (Abiturzeugnis) - Din-A3
	 */
	public static readonly SCHUELER_v_GOST_ABITUR_APO_ANLAGE_12_A3 : ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_v_GOST_ABITUR_APO_ANLAGE_12_A3", 11, "Schueler-GostAbiturApoAnlage12-A3");

	/**
	 * Report-Vorlage: GOSt - Laufbahnplanung - Ergebnisübersicht
	 */
	public static readonly SCHUELER_v_GOST_LAUFBAHNPLANUNG_ERGEBNISUEBERSICHT : ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_v_GOST_LAUFBAHNPLANUNG_ERGEBNISUEBERSICHT", 12, "Schueler-GostLaufbahnplanungErgebnisuebersicht");

	/**
	 * Report-Vorlage: GOSt - Laufbahnplanung - Wahlbogen
	 */
	public static readonly SCHUELER_v_GOST_LAUFBAHNPLANUNG_WAHLBOGEN : ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_v_GOST_LAUFBAHNPLANUNG_WAHLBOGEN", 13, "Schueler-GostLaufbahnplanungWahlbogen");

	/**
	 * Report-Vorlage: Schüler - Schulbescheinigung
	 */
	public static readonly SCHUELER_v_SCHULBESCHEINIGUNG : ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_v_SCHULBESCHEINIGUNG", 14, "Schueler-Schulbescheinigung");

	/**
	 * Report-Vorlage: Schüler - Stammdaten - Liste
	 */
	public static readonly SCHUELER_v_STAMMDATENLISTE : ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_v_STAMMDATENLISTE", 15, "Schueler-Stammdatenliste");

	/**
	 * Report-Vorlage: Stundenplanung - Fach - Stundenplan
	 */
	public static readonly STUNDENPLANUNG_v_FACH_STUNDENPLAN : ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_v_FACH_STUNDENPLAN", 16, "Stundenplanung-FachStundenplan");

	/**
	 * Report-Vorlage: Stundenplanung - Klasse - Stundenplan
	 */
	public static readonly STUNDENPLANUNG_v_KLASSEN_STUNDENPLAN : ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_v_KLASSEN_STUNDENPLAN", 17, "Stundenplanung-KlassenStundenplan");

	/**
	 * Report-Vorlage: Stundenplanung - Lehrer - Stundenplan
	 */
	public static readonly STUNDENPLANUNG_v_LEHRER_STUNDENPLAN : ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_v_LEHRER_STUNDENPLAN", 18, "Stundenplanung-LehrerStundenplan");

	/**
	 * Report-Vorlage: Stundenplanung - Lehrer - Stundenplan - Kombiniert
	 */
	public static readonly STUNDENPLANUNG_v_LEHRER_STUNDENPLAN_KOMBINIERT : ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_v_LEHRER_STUNDENPLAN_KOMBINIERT", 19, "Stundenplanung-LehrerStundenplanKombiniert");

	/**
	 * Report-Vorlage: Stundenplanung - Fach - Stundenplan
	 */
	public static readonly STUNDENPLANUNG_v_RAUM_STUNDENPLAN : ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_v_RAUM_STUNDENPLAN", 20, "Stundenplanung-RaumStundenplan");

	/**
	 * Report-Vorlage: Stundenplanung - Schüler - Stundenplan
	 */
	public static readonly STUNDENPLANUNG_v_SCHUELER_STUNDENPLAN : ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_v_SCHUELER_STUNDENPLAN", 21, "Stundenplanung-SchuelerStundenplan");

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
	public getBezeichnung() : string {
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

	public static class = new Class<ReportingReportvorlage>('de.svws_nrw.core.types.reporting.ReportingReportvorlage');

}

export function cast_de_svws_nrw_core_types_reporting_ReportingReportvorlage(obj : unknown) : ReportingReportvorlage {
	return obj as ReportingReportvorlage;
}
