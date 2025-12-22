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
	public static readonly GOST_KLAUSURPLANUNG_V_KLAUSURTERMINE_MIT_KURSEN: ReportingReportvorlage = new ReportingReportvorlage("GOST_KLAUSURPLANUNG_V_KLAUSURTERMINE_MIT_KURSEN", 0, "GostKlausurplanung-KlausurtermineMitKursen", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitKursklausuren", "mit Kursklausuren", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitNachschreibern", "mit Nachschreibern", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitKlausurschreiberNamen", "mit Namen der Klausurschreiber", ReportingVorlageParameterTyp.BOOLEAN, "false")));

	/**
	 * Report-Vorlage: GOSt - Klausurplanung - Schueler-Klausuren
	 */
	public static readonly GOST_KLAUSURPLANUNG_V_SCHUELER_MIT_KLAUSUREN: ReportingReportvorlage = new ReportingReportvorlage("GOST_KLAUSURPLANUNG_V_SCHUELER_MIT_KLAUSUREN", 1, "GostKlausurplanung-SchuelerMitKlausuren", new ArrayList());

	/**
	 * Report-Vorlage: GOSt - Kursplanung - Kurs-Kurschüler
	 */
	public static readonly GOST_KURSPLANUNG_V_KURS_MIT_KURSSCHUELERN: ReportingReportvorlage = new ReportingReportvorlage("GOST_KURSPLANUNG_V_KURS_MIT_KURSSCHUELERN", 2, "GostKursplanung-KursMitKursschuelern", new ArrayList());

	/**
	 * Report-Vorlage: GOSt - Kursplanung - Kurse-Statistikwerte
	 */
	public static readonly GOST_KURSPLANUNG_V_KURSE_MIT_STATISTIKWERTEN: ReportingReportvorlage = new ReportingReportvorlage("GOST_KURSPLANUNG_V_KURSE_MIT_STATISTIKWERTEN", 3, "GostKursplanung-KurseMitStatistikwerten", new ArrayList());

	/**
	 * Report-Vorlage: GOSt - Kursplanung - Schüler-Kurse
	 */
	public static readonly GOST_KURSPLANUNG_V_SCHUELER_MIT_KURSEN: ReportingReportvorlage = new ReportingReportvorlage("GOST_KURSPLANUNG_V_SCHUELER_MIT_KURSEN", 4, "GostKursplanung-SchuelerMitKursen", new ArrayList());

	/**
	 * Report-Vorlage: GOSt - Kursplanung - Schüler-Schienen-Kurse
	 */
	public static readonly GOST_KURSPLANUNG_V_SCHUELER_MIT_SCHIENEN_KURSEN: ReportingReportvorlage = new ReportingReportvorlage("GOST_KURSPLANUNG_V_SCHUELER_MIT_SCHIENEN_KURSEN", 5, "GostKursplanung-SchuelerMitSchienenKursen", new ArrayList());

	/**
	 * Report-Vorlage: GOSt - Laufbahnplanung - Abiturjahrgang - Fachwahlstatistiken
	 */
	public static readonly GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG_V_FACHWAHLSTATISTIKEN: ReportingReportvorlage = new ReportingReportvorlage("GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG_V_FACHWAHLSTATISTIKEN", 6, "GostLaufbahnplanung-Abiturjahrgang-Fachwahlstatistiken", new ArrayList());

	/**
	 * Report-Vorlage: Klasse - Liste - Schüler - Kontaktdaten - Erzieher
	 */
	public static readonly KLASSEN_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER: ReportingReportvorlage = new ReportingReportvorlage("KLASSEN_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER", 7, "Klasse-Liste-Schueler-Kontaktdaten-Erzieher", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("nurSchuelerRufname", "nur Rufname", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerGeschlecht", "mit Geschlecht", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerGebDat", "mit Geburtsdatum", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerStaat", "mit Staatsangehörigkeit", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerAnschrift", "mit Anschrift", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerTelefonPrivat", "mit Telefon (privat)", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerEmailSchule", "mit E-Mail (Schule)", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerEmailPrivat", "mit E-Mail (privat)", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSpalteSchuelerTelefonKontakte", "mit Telefonkontakten", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieher", "mit Erziehern", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieherAnschrift", "mit Erzieher-Anschrift", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieherEmailPrivat", "mit Erzieher-E-Mail", ReportingVorlageParameterTyp.BOOLEAN, "false")));

	/**
	 * Report-Vorlage: Kurs - Liste - Schüler - Kontaktdaten - Erzieher
	 */
	public static readonly KURSE_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER: ReportingReportvorlage = new ReportingReportvorlage("KURSE_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER", 8, "Kurs-Liste-Schueler-Kontaktdaten-Erzieher", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerKlasse", "mit Klasse", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("nurSchuelerRufname", "nur Rufname", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerGeschlecht", "mit Geschlecht", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerGebDat", "mit Geburtsdatum", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerStaat", "mit Staatsangehörigkeit", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerAnschrift", "mit Anschrift", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerTelefonPrivat", "mit Telefon (privat)", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerEmailSchule", "mit E-Mail (Schule)", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerEmailPrivat", "mit E-Mail (privat)", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSpalteSchuelerTelefonKontakte", "mit Telefonkontakten", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieher", "mit Erziehern", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieherAnschrift", "mit Erzieher-Anschrift", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieherEmailPrivat", "mit Erzieher-E-Mail", ReportingVorlageParameterTyp.BOOLEAN, "false")));

	/**
	 * Report-Vorlage: Kurs - Liste - Schüler - Leistungsdaten
	 */
	public static readonly KURSE_V_LISTE_SCHUELER_LEISTUNGSDATEN: ReportingReportvorlage = new ReportingReportvorlage("KURSE_V_LISTE_SCHUELER_LEISTUNGSDATEN", 9, "Kurs-Liste-Schueler-Leistungsdaten", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitBemerkungen", "mit Bemerkungen", ReportingVorlageParameterTyp.BOOLEAN, "false")));

	/**
	 * Report-Vorlage: Lehrer - Liste - Schüler - Leistungsdaten
	 */
	public static readonly LEHRER_V_LISTE_SCHUELER_LEISTUNGSDATEN: ReportingReportvorlage = new ReportingReportvorlage("LEHRER_V_LISTE_SCHUELER_LEISTUNGSDATEN", 10, "Lehrer-Liste-Schueler-Leistungsdaten", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitKlassenunterricht", "mit Klassenunterricht", ReportingVorlageParameterTyp.BOOLEAN, "true"), ReportingReportvorlage.erzeugeVorlageParameter("mitKursunterricht", "mit Kursunterricht", ReportingVorlageParameterTyp.BOOLEAN, "true"), ReportingReportvorlage.erzeugeVorlageParameter("mitBemerkungen", "mit Bemerkungen", ReportingVorlageParameterTyp.BOOLEAN, "false")));

	/**
	 * Report-Vorlage: Lehrer - Stammdaten - Liste
	 */
	public static readonly LEHRER_V_STAMMDATENLISTE: ReportingReportvorlage = new ReportingReportvorlage("LEHRER_V_STAMMDATENLISTE", 11, "Lehrer-Stammdatenliste", new ArrayList());

	/**
	 * Report-Vorlage: GOSt - Abitur - APO - Anlage 12 (Abiturzeugnis) - Din-A4
	 */
	public static readonly SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A4: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A4", 12, "Schueler-GostAbiturApoAnlage12-A4", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPersoenlichenUnterschriften", "mit persönlichen Unterschriften", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitZweiterBeratungslehrerUnterschrift", "mit Unterschrift 2. Beratungslehrer", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("textZAAVorsitzUnterschrift", "Unterschrift ZAA-Vorsitz", ReportingVorlageParameterTyp.STRING, ""), ReportingReportvorlage.erzeugeVorlageParameter("textZAAVorsitzUnterschriftBezeichnung", "Bezeichnung ZAA-Vorsitz", ReportingVorlageParameterTyp.STRING, ""), ReportingReportvorlage.erzeugeVorlageParameter("textSchulleitungUnterschrift", "Unterschrift Schulleitung", ReportingVorlageParameterTyp.STRING, ""), ReportingReportvorlage.erzeugeVorlageParameter("textSchulleitungUnterschriftBezeichnung", "Bezeichnung Schulleitung", ReportingVorlageParameterTyp.STRING, ""), ReportingReportvorlage.erzeugeVorlageParameter("textSchultraegerUnterschrift", "Unterschrift Schulträger", ReportingVorlageParameterTyp.STRING, ""), ReportingReportvorlage.erzeugeVorlageParameter("textSchultraegerUnterschriftBezeichnung", "Bezeichnung Schulträger", ReportingVorlageParameterTyp.STRING, ""), ReportingReportvorlage.erzeugeVorlageParameter("textBeratungslehrerUnterschrift", "Unterschrift Beratungslehrer", ReportingVorlageParameterTyp.STRING, ""), ReportingReportvorlage.erzeugeVorlageParameter("textBeratungslehrerUnterschriftBezeichnung", "Bezeichnung Beratungslehrer", ReportingVorlageParameterTyp.STRING, "")));

	/**
	 * Report-Vorlage: GOSt - Abitur - APO - Anlage 12 (Abiturzeugnis) - Din-A3
	 */
	public static readonly SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A3: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A3", 13, "Schueler-GostAbiturApoAnlage12-A3", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPersoenlichenUnterschriften", "mit persönlichen Unterschriften", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitZweiterBeratungslehrerUnterschrift", "mit Unterschrift 2. Beratungslehrer", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("textZAAVorsitzUnterschrift", "Unterschrift ZAA-Vorsitz", ReportingVorlageParameterTyp.STRING, ""), ReportingReportvorlage.erzeugeVorlageParameter("textZAAVorsitzUnterschriftBezeichnung", "Bezeichnung ZAA-Vorsitz", ReportingVorlageParameterTyp.STRING, ""), ReportingReportvorlage.erzeugeVorlageParameter("textSchulleitungUnterschrift", "Unterschrift Schulleitung", ReportingVorlageParameterTyp.STRING, ""), ReportingReportvorlage.erzeugeVorlageParameter("textSchulleitungUnterschriftBezeichnung", "Bezeichnung Schulleitung", ReportingVorlageParameterTyp.STRING, ""), ReportingReportvorlage.erzeugeVorlageParameter("textSchultraegerUnterschrift", "Unterschrift Schulträger", ReportingVorlageParameterTyp.STRING, ""), ReportingReportvorlage.erzeugeVorlageParameter("textSchultraegerUnterschriftBezeichnung", "Bezeichnung Schulträger", ReportingVorlageParameterTyp.STRING, ""), ReportingReportvorlage.erzeugeVorlageParameter("textBeratungslehrerUnterschrift", "Unterschrift Beratungslehrer", ReportingVorlageParameterTyp.STRING, ""), ReportingReportvorlage.erzeugeVorlageParameter("textBeratungslehrerUnterschriftBezeichnung", "Bezeichnung Beratungslehrer", ReportingVorlageParameterTyp.STRING, "")));

	/**
	 * Report-Vorlage: GOSt - Laufbahnplanung - Ergebnisübersicht
	 */
	public static readonly SCHUELER_V_GOST_LAUFBAHNPLANUNG_ERGEBNISUEBERSICHT: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_V_GOST_LAUFBAHNPLANUNG_ERGEBNISUEBERSICHT", 14, "Schueler-GostLaufbahnplanungErgebnisuebersicht", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitFehlernKommentaren", "mit Fehlern/Kommentaren", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitHinweisen", "mit Hinweisen", ReportingVorlageParameterTyp.BOOLEAN, "false")));

	/**
	 * Report-Vorlage: GOSt - Laufbahnplanung - Wahlbogen
	 */
	public static readonly SCHUELER_V_GOST_LAUFBAHNPLANUNG_WAHLBOGEN: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_V_GOST_LAUFBAHNPLANUNG_WAHLBOGEN", 15, "Schueler-GostLaufbahnplanungWahlbogen", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("nurBelegteFaecher", "nur belegte Fächer", ReportingVorlageParameterTyp.BOOLEAN, "false")));

	/**
	 * Report-Vorlage: Schüler - Schulbescheinigung
	 */
	public static readonly SCHUELER_V_SCHULBESCHEINIGUNG: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_V_SCHULBESCHEINIGUNG", 16, "Schueler-Schulbescheinigung", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("fuerErzieher", "für Erzieher", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitBildBriefkopf", "mit Bild im Briefkopf", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchullogo", "mit Schullogo", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("keineAnschrift", "ohne Anschrift", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("keinInfoblock", "ohne Infoblock", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("keineUnterschrift", "ohne Unterschrift", ReportingVorlageParameterTyp.BOOLEAN, "false")));

	/**
	 * Report-Vorlage: Schüler - Liste - Kontaktdaten - Erzieher
	 */
	public static readonly SCHUELER_V_LISTE_KONTAKTDATENERZIEHER: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_V_LISTE_KONTAKTDATENERZIEHER", 17, "Schueler-Liste-Kontaktdaten-Erzieher", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerKlasse", "mit Klasse", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("nurSchuelerRufname", "nur Rufname", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerGeschlecht", "mit Geschlecht", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerGebDat", "mit Geburtsdatum", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerStaat", "mit Staatsangehörigkeit", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerAnschrift", "mit Anschrift", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerTelefonPrivat", "mit Telefon (privat)", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerEmailSchule", "mit E-Mail (Schule)", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerEmailPrivat", "mit E-Mail (privat)", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitSpalteSchuelerTelefonKontakte", "mit Telefonkontakten", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieher", "mit Erziehern", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieherAnschrift", "mit Erzieher-Anschrift", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieherEmailPrivat", "mit Erzieher-E-Mail", ReportingVorlageParameterTyp.BOOLEAN, "false")));

	/**
	 * Report-Vorlage: Stundenplanung - Fach - Stundenplan
	 */
	public static readonly STUNDENPLANUNG_V_FACH_STUNDENPLAN: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_V_FACH_STUNDENPLAN", 18, "Stundenplanung-FachStundenplan", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingVorlageParameterTyp.BOOLEAN, "false")));

	/**
	 * Report-Vorlage: Stundenplanung - Klasse - Stundenplan
	 */
	public static readonly STUNDENPLANUNG_V_KLASSEN_STUNDENPLAN: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_V_KLASSEN_STUNDENPLAN", 19, "Stundenplanung-KlassenStundenplan", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitFachStattKursbezeichnung", "Fach statt Kursbezeichnung", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung", "Fachkürzel statt Fachbezeichnung", ReportingVorlageParameterTyp.BOOLEAN, "false")));

	/**
	 * Report-Vorlage: Stundenplanung - Lehrer - Stundenplan
	 */
	public static readonly STUNDENPLANUNG_V_LEHRER_STUNDENPLAN: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_V_LEHRER_STUNDENPLAN", 20, "Stundenplanung-LehrerStundenplan", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitPausenaufsichten", "mit Pausenaufsichten", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung", "Fachkürzel statt Fachbezeichnung", ReportingVorlageParameterTyp.BOOLEAN, "false")));

	/**
	 * Report-Vorlage: Stundenplanung - Lehrer - Stundenplan - Kombiniert
	 */
	public static readonly STUNDENPLANUNG_V_LEHRER_STUNDENPLAN_KOMBINIERT: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_V_LEHRER_STUNDENPLAN_KOMBINIERT", 21, "Stundenplanung-LehrerStundenplanKombiniert", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitPausenaufsichten", "mit Pausenaufsichten", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung", "Fachkürzel statt Fachbezeichnung", ReportingVorlageParameterTyp.BOOLEAN, "false")));

	/**
	 * Report-Vorlage: Stundenplanung - Fach - Stundenplan
	 */
	public static readonly STUNDENPLANUNG_V_RAUM_STUNDENPLAN: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_V_RAUM_STUNDENPLAN", 22, "Stundenplanung-RaumStundenplan", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingVorlageParameterTyp.BOOLEAN, "false")));

	/**
	 * Report-Vorlage: Stundenplanung - Schüler - Stundenplan
	 */
	public static readonly STUNDENPLANUNG_V_SCHUELER_STUNDENPLAN: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_V_SCHUELER_STUNDENPLAN", 23, "Stundenplanung-SchuelerStundenplan", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitFachStattKursbezeichnung", "Fach statt Kursbezeichnung", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung", "Fachkürzel statt Fachbezeichnung", ReportingVorlageParameterTyp.BOOLEAN, "false"), ReportingReportvorlage.erzeugeVorlageParameter("mitIndividuelleKursart", "mit individueller Kursart", ReportingVorlageParameterTyp.BOOLEAN, "false")));

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
	 * @param bezeichnung die Bezeichnung des Vorlage-Parameters. Darf nicht null sein.
	 * @param typ    der Typ des Vorlage-Parameters. Darf nicht null sein.
	 * @param wert   der Wert des Vorlage-Parameters. Darf nicht null sein.
	 *
	 * @return Ein neues Objekt der Klasse {@link ReportingVorlageParameter}, das den angegebenen Namen, Typ und Wert enthält.
	 */
	private static erzeugeVorlageParameter(name: string, bezeichnung: string, typ: ReportingVorlageParameterTyp, wert: string): ReportingVorlageParameter {
		const reportingVorlageParameter: ReportingVorlageParameter | null = new ReportingVorlageParameter();
		reportingVorlageParameter.name = name;
		reportingVorlageParameter.bezeichnung = bezeichnung;
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
	 * Diese Methode ermittelt die Report-Vorlage anhand des übergebenen Namens. Der Vergleich ignoriert Groß- und Kleinschreibung.
	 *
	 * @param name Der Name der Report-Vorlage
	 *
	 * @return Die Report-Vorlage
	 */
	public static getByName(name: string): ReportingReportvorlage | null {
		if (JavaString.isEmpty(name))
			return null;
		for (const rv of ReportingReportvorlage.values())
			if (JavaString.equalsIgnoreCase(rv.name(), name))
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
