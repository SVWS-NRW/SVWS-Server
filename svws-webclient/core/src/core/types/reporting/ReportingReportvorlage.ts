import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap } from '../../../java/util/HashMap';
import { ReportingVorlageParameter } from '../../../core/data/reporting/ReportingVorlageParameter';
import { ReportingVorlageParameterTyp } from '../../../core/types/reporting/ReportingVorlageParameterTyp';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { Arrays } from '../../../java/util/Arrays';
import type { JavaMap } from '../../../java/util/JavaMap';

export class ReportingReportvorlage extends JavaEnum<ReportingReportvorlage> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<ReportingReportvorlage> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, ReportingReportvorlage> = new Map<string, ReportingReportvorlage>();

	/**
	 * Report-Vorlage: GOSt - Klausurplanung - Klausurtermine-Kurse
	 */
	public static readonly GOST_KLAUSURPLANUNG_v_KLAUSURTERMINE_MIT_KURSEN: ReportingReportvorlage = new ReportingReportvorlage("GOST_KLAUSURPLANUNG_v_KLAUSURTERMINE_MIT_KURSEN", 0, "GostKlausurplanung-KlausurtermineMitKursen", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitKursklausuren", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitNachschreibern", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitKlausurschreiberNamen", ReportingVorlageParameterTyp.BOOLEAN, "false")));

	/**
	 * Report-Vorlage: GOSt - Klausurplanung - Schueler-Klausuren
	 */
	public static readonly GOST_KLAUSURPLANUNG_v_SCHUELER_MIT_KLAUSUREN: ReportingReportvorlage = new ReportingReportvorlage("GOST_KLAUSURPLANUNG_v_SCHUELER_MIT_KLAUSUREN", 1, "GostKlausurplanung-SchuelerMitKlausuren", new ArrayList());

	/**
	 * Report-Vorlage: GOSt - Kursplanung - Kurs-Kurschüler
	 */
	public static readonly GOST_KURSPLANUNG_v_KURS_MIT_KURSSCHUELERN: ReportingReportvorlage = new ReportingReportvorlage("GOST_KURSPLANUNG_v_KURS_MIT_KURSSCHUELERN", 2, "GostKursplanung-KursMitKursschuelern", new ArrayList());

	/**
	 * Report-Vorlage: GOSt - Kursplanung - Kurse-Statistikwerte
	 */
	public static readonly GOST_KURSPLANUNG_v_KURSE_MIT_STATISTIKWERTEN: ReportingReportvorlage = new ReportingReportvorlage("GOST_KURSPLANUNG_v_KURSE_MIT_STATISTIKWERTEN", 3, "GostKursplanung-KurseMitStatistikwerten", new ArrayList());

	/**
	 * Report-Vorlage: GOSt - Kursplanung - Schüler-Kurse
	 */
	public static readonly GOST_KURSPLANUNG_v_SCHUELER_MIT_KURSEN: ReportingReportvorlage = new ReportingReportvorlage("GOST_KURSPLANUNG_v_SCHUELER_MIT_KURSEN", 4, "GostKursplanung-SchuelerMitKursen", new ArrayList());

	/**
	 * Report-Vorlage: GOSt - Kursplanung - Schüler-Schienen-Kurse
	 */
	public static readonly GOST_KURSPLANUNG_v_SCHUELER_MIT_SCHIENEN_KURSEN: ReportingReportvorlage = new ReportingReportvorlage("GOST_KURSPLANUNG_v_SCHUELER_MIT_SCHIENEN_KURSEN", 5, "GostKursplanung-SchuelerMitSchienenKursen", new ArrayList());

	/**
	 * Report-Vorlage: GOSt - Laufbahnplanung - Abiturjahrgang - Fachwahlstatistiken
	 */
	public static readonly GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG_v_FACHWAHLSTATISTIKEN: ReportingReportvorlage = new ReportingReportvorlage("GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG_v_FACHWAHLSTATISTIKEN", 6, "GostLaufbahnplanung-Abiturjahrgang-Fachwahlstatistiken", new ArrayList());

	/**
	 * Report-Vorlage: Klasse - Liste - Schüler - Kontaktdaten - Erzieher
	 */
	public static readonly KLASSEN_v_LISTE_SCHUELER_KONTAKTDATENERZIEHER: ReportingReportvorlage = new ReportingReportvorlage("KLASSEN_v_LISTE_SCHUELER_KONTAKTDATENERZIEHER", 7, "Klasse-Liste-Schueler-Kontaktdaten-Erzieher", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("nurSchuelerRufname", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerGeschlecht", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerGebDat", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerStaat", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerAnschrift", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerTelefonPrivat", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerEmailSchule", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerEmailPrivat", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSpalteSchuelerTelefonKontakte", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieher", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieherAnschrift", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieherEmailPrivat", ReportingVorlageParameterTyp.BOOLEAN, "false")));

	/**
	 * Report-Vorlage: Kurs - Liste - Schüler - Kontaktdaten - Erzieher
	 */
	public static readonly KURSE_v_LISTE_SCHUELER_KONTAKTDATENERZIEHER: ReportingReportvorlage = new ReportingReportvorlage("KURSE_v_LISTE_SCHUELER_KONTAKTDATENERZIEHER", 8, "Kurs-Liste-Schueler-Kontaktdaten-Erzieher", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerKlasse", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("nurSchuelerRufname", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerGeschlecht", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerGebDat", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerStaat", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerAnschrift", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerTelefonPrivat", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerEmailSchule", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerEmailPrivat", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSpalteSchuelerTelefonKontakte", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieher", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieherAnschrift", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieherEmailPrivat", ReportingVorlageParameterTyp.BOOLEAN, "false")));

	/**
	 * Report-Vorlage: Lehrer - Stammdaten - Liste
	 */
	public static readonly LEHRER_v_STAMMDATENLISTE: ReportingReportvorlage = new ReportingReportvorlage("LEHRER_v_STAMMDATENLISTE", 9, "Lehrer-Stammdatenliste", new ArrayList());

	/**
	 * Report-Vorlage: GOSt - Abitur - APO - Anlage 12 (Abiturzeugnis) - Din-A4
	 */
	public static readonly SCHUELER_v_GOST_ABITUR_APO_ANLAGE_12_A4: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_v_GOST_ABITUR_APO_ANLAGE_12_A4", 10, "Schueler-GostAbiturApoAnlage12-A4", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPersoenlichenUnterschriften", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitZweiterBeratungslehrerUnterschrift", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("textZAAVorsitzUnterschrift", ReportingVorlageParameterTyp.STRING, ""), ReportingReportvorlage.erzeugeVorlageParameter("textZAAVorsitzUnterschriftBezeichnung", ReportingVorlageParameterTyp.STRING, ""), ReportingReportvorlage.erzeugeVorlageParameter("textSchulleitungUnterschrift", ReportingVorlageParameterTyp.STRING, ""), ReportingReportvorlage.erzeugeVorlageParameter("textSchulleitungUnterschriftBezeichnung", ReportingVorlageParameterTyp.STRING, ""), ReportingReportvorlage.erzeugeVorlageParameter("textSchultraegerUnterschrift", ReportingVorlageParameterTyp.STRING, ""), ReportingReportvorlage.erzeugeVorlageParameter("textSchultraegerUnterschriftBezeichnung", ReportingVorlageParameterTyp.STRING, ""), ReportingReportvorlage.erzeugeVorlageParameter("textBeratungslehrerUnterschrift", ReportingVorlageParameterTyp.STRING, ""), ReportingReportvorlage.erzeugeVorlageParameter("textBeratungslehrerUnterschriftBezeichnung", ReportingVorlageParameterTyp.STRING, "")));

	/**
	 * Report-Vorlage: GOSt - Abitur - APO - Anlage 12 (Abiturzeugnis) - Din-A3
	 */
	public static readonly SCHUELER_v_GOST_ABITUR_APO_ANLAGE_12_A3: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_v_GOST_ABITUR_APO_ANLAGE_12_A3", 11, "Schueler-GostAbiturApoAnlage12-A3", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPersoenlichenUnterschriften", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitZweiterBeratungslehrerUnterschrift", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("textZAAVorsitzUnterschrift", ReportingVorlageParameterTyp.STRING, ""), ReportingReportvorlage.erzeugeVorlageParameter("textZAAVorsitzUnterschriftBezeichnung", ReportingVorlageParameterTyp.STRING, ""), ReportingReportvorlage.erzeugeVorlageParameter("textSchulleitungUnterschrift", ReportingVorlageParameterTyp.STRING, ""), ReportingReportvorlage.erzeugeVorlageParameter("textSchulleitungUnterschriftBezeichnung", ReportingVorlageParameterTyp.STRING, ""), ReportingReportvorlage.erzeugeVorlageParameter("textSchultraegerUnterschrift", ReportingVorlageParameterTyp.STRING, ""), ReportingReportvorlage.erzeugeVorlageParameter("textSchultraegerUnterschriftBezeichnung", ReportingVorlageParameterTyp.STRING, ""), ReportingReportvorlage.erzeugeVorlageParameter("textBeratungslehrerUnterschrift", ReportingVorlageParameterTyp.STRING, ""), ReportingReportvorlage.erzeugeVorlageParameter("textBeratungslehrerUnterschriftBezeichnung", ReportingVorlageParameterTyp.STRING, "")));

	/**
	 * Report-Vorlage: GOSt - Laufbahnplanung - Ergebnisübersicht
	 */
	public static readonly SCHUELER_v_GOST_LAUFBAHNPLANUNG_ERGEBNISUEBERSICHT: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_v_GOST_LAUFBAHNPLANUNG_ERGEBNISUEBERSICHT", 12, "Schueler-GostLaufbahnplanungErgebnisuebersicht", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitFehlernKommentaren", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitHinweisen", ReportingVorlageParameterTyp.BOOLEAN, "false")));

	/**
	 * Report-Vorlage: GOSt - Laufbahnplanung - Wahlbogen
	 */
	public static readonly SCHUELER_v_GOST_LAUFBAHNPLANUNG_WAHLBOGEN: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_v_GOST_LAUFBAHNPLANUNG_WAHLBOGEN", 13, "Schueler-GostLaufbahnplanungWahlbogen", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("nurBelegteFaecher", ReportingVorlageParameterTyp.BOOLEAN, "false")));

	/**
	 * Report-Vorlage: Schüler - Schulbescheinigung
	 */
	public static readonly SCHUELER_v_SCHULBESCHEINIGUNG: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_v_SCHULBESCHEINIGUNG", 14, "Schueler-Schulbescheinigung", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("fuerErzieher", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitBildBriefkopf", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchullogo", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("keineAnschrift", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("keinInfoblock", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("keineUnterschrift", ReportingVorlageParameterTyp.BOOLEAN, "false")));

	/**
	 * Report-Vorlage: Schüler - Liste - Kontaktdaten - Erzieher
	 */
	public static readonly SCHUELER_v_LISTE_KONTAKTDATENERZIEHER: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_v_LISTE_KONTAKTDATENERZIEHER", 15, "Schueler-Liste-Kontaktdaten-Erzieher", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerKlasse", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("nurSchuelerRufname", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerGeschlecht", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerGebDat", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerStaat", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerAnschrift", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerTelefonPrivat", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerEmailSchule", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerEmailPrivat", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSpalteSchuelerTelefonKontakte", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieher", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieherAnschrift", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieherEmailPrivat", ReportingVorlageParameterTyp.BOOLEAN, "false")));

	/**
	 * Report-Vorlage: Stundenplanung - Fach - Stundenplan
	 */
	public static readonly STUNDENPLANUNG_v_FACH_STUNDENPLAN: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_v_FACH_STUNDENPLAN", 16, "Stundenplanung-FachStundenplan", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPausenzeiten", ReportingVorlageParameterTyp.BOOLEAN, "false")));

	/**
	 * Report-Vorlage: Stundenplanung - Klasse - Stundenplan
	 */
	public static readonly STUNDENPLANUNG_v_KLASSEN_STUNDENPLAN: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_v_KLASSEN_STUNDENPLAN", 17, "Stundenplanung-KlassenStundenplan", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPausenzeiten", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitFachStattKursbezeichnung", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung", ReportingVorlageParameterTyp.BOOLEAN, "false")));

	/**
	 * Report-Vorlage: Stundenplanung - Lehrer - Stundenplan
	 */
	public static readonly STUNDENPLANUNG_v_LEHRER_STUNDENPLAN: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_v_LEHRER_STUNDENPLAN", 18, "Stundenplanung-LehrerStundenplan", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPausenzeiten", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitPausenaufsichten", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung", ReportingVorlageParameterTyp.BOOLEAN, "false")));

	/**
	 * Report-Vorlage: Stundenplanung - Lehrer - Stundenplan - Kombiniert
	 */
	public static readonly STUNDENPLANUNG_v_LEHRER_STUNDENPLAN_KOMBINIERT: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_v_LEHRER_STUNDENPLAN_KOMBINIERT", 19, "Stundenplanung-LehrerStundenplanKombiniert", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPausenzeiten", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitPausenaufsichten", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung", ReportingVorlageParameterTyp.BOOLEAN, "false")));

	/**
	 * Report-Vorlage: Stundenplanung - Fach - Stundenplan
	 */
	public static readonly STUNDENPLANUNG_v_RAUM_STUNDENPLAN: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_v_RAUM_STUNDENPLAN", 20, "Stundenplanung-RaumStundenplan", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPausenzeiten", ReportingVorlageParameterTyp.BOOLEAN, "false")));

	/**
	 * Report-Vorlage: Stundenplanung - Schüler - Stundenplan
	 */
	public static readonly STUNDENPLANUNG_v_SCHUELER_STUNDENPLAN: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_v_SCHUELER_STUNDENPLAN", 21, "Stundenplanung-SchuelerStundenplan", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPausenzeiten", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitFachStattKursbezeichnung", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitIndividuelleKursart", ReportingVorlageParameterTyp.BOOLEAN, "false")));

	/**
	 * Die Bezeichnung der Report-Vorlage
	 */
	private readonly bezeichnung: string;

	/**
	 * Eine Liste, in der die gültigen Vorlage-Parameter der Report-Vorlage enthalten sind.
	 */
	private readonly vorlageParameterList: List<ReportingVorlageParameter> = new ArrayList<ReportingVorlageParameter>();

	/**
	 * Eine Map, die die gültigen Vorlage-Parameter der Report-Vorlage zum Namen des Parameters enthält.
	 */
	private readonly vorlageParameterMap: JavaMap<string, ReportingVorlageParameter> = new HashMap<string, ReportingVorlageParameter>();

	/**
	 * Konstruktor für eine Reporting-Reportvorlage.
	 *
	 * @param bezeichnung Die Bezeichnung der Reportvorlage. Darf nicht null sein.
	 * @param vorlageParameterList Eine Liste mit den Vorlage-Parametern, basierend auf der jeweiligen Definition. Darf nicht null sein.
	 */
	private constructor(name: string, ordinal: number, bezeichnung: string, vorlageParameterList: List<ReportingVorlageParameter>) {
		super(name, ordinal);
		ReportingReportvorlage.all_values_by_ordinal.push(this);
		ReportingReportvorlage.all_values_by_name.set(name, this);
		this.bezeichnung = bezeichnung;
		this.vorlageParameterList.addAll(vorlageParameterList);
		for (const vp of vorlageParameterList)
			this.vorlageParameterMap.put(vp.name, vp);
	}

	/**
	 * Erstellt einen neuen Vorlage-Parameter mit dem angegebenen Namen, Typ und Wert.
	 *
	 * @param name   der Name des Vorlage-Parameters. Darf nicht null sein.
	 * @param typ    der Typ des Vorlage-Parameters. Darf nicht null sein.
	 * @param wert   der Wert des Vorlage-Parameters. Darf nicht null sein.
	 *
	 * @return Ein neues Objekt der Klasse {@link ReportingVorlageParameter}, das den angegebenen Namen, Typ und Wert enthält.
	 */
	private static erzeugeVorlageParameter(name: string, typ: ReportingVorlageParameterTyp, wert: string): ReportingVorlageParameter {
		const reportingVorlageParameter: ReportingVorlageParameter | null = new ReportingVorlageParameter();
		reportingVorlageParameter.name = name;
		reportingVorlageParameter.typ = typ.getId();
		reportingVorlageParameter.wert = wert;
		return reportingVorlageParameter;
	}

	/**
	 * Diese Methode ermittelt die Report-Vorlage anhand der übergebenen Bezeichnung.
	 *
	 * @param bezeichnung Die Bezeichnung der Report-Vorlage
	 *
	 * @return Die Report-Vorlage
	 */
	public static getByBezeichnung(bezeichnung: string): ReportingReportvorlage | null {
		if (JavaString.isEmpty(bezeichnung))
			return null;
		for (const rv of ReportingReportvorlage.values())
			if (JavaObject.equalsTranspiler(rv.bezeichnung, (bezeichnung)))
				return rv;
		return null;
	}

	/**
	 * Gibt die Bezeichnung dieser Report-Vorlage zurück
	 *
	 * @return Die Bezeichnung dieser Report-Vorlage
	 */
	public getBezeichnung(): string {
		return (this.bezeichnung !== null) ? this.bezeichnung : "";
	}

	/**
	 * Gibt die Liste der Report-Parameter für diese Report-Vorlage zurück.
	 *
	 * @return Die Liste der Report-Parameter für diese Report-Vorlage.
	 */
	public getVorlageParameterList(): List<ReportingVorlageParameter> {
		return this.vorlageParameterList;
	}

	/**
	 * Gibt die Map der Report-Parameter für diese Report-Vorlage zurück.
	 *
	 * @return Die Map der Report-Parameter für diese Report-Vorlage.
	 */
	public getVorlageParameterMap(): JavaMap<string, ReportingVorlageParameter> {
		return this.vorlageParameterMap;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<ReportingReportvorlage> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): ReportingReportvorlage | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.reporting.ReportingReportvorlage';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.types.reporting.ReportingReportvorlage', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<ReportingReportvorlage>('de.svws_nrw.core.types.reporting.ReportingReportvorlage');

}

export function cast_de_svws_nrw_core_types_reporting_ReportingReportvorlage(obj: unknown): ReportingReportvorlage {
	return obj as ReportingReportvorlage;
}
