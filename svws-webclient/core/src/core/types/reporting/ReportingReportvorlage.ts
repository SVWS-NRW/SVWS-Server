import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { ReportingVorlageParameterGruppe } from '../../../core/data/reporting/ReportingVorlageParameterGruppe';
import { HashMap } from '../../../java/util/HashMap';
import { ReportingVorlageParameter } from '../../../core/data/reporting/ReportingVorlageParameter';
import { ReportingVorlageParameterTyp } from '../../../core/types/reporting/ReportingVorlageParameterTyp';
import { ArrayList } from '../../../java/util/ArrayList';
import { ReportingUIKomponentenTyp } from '../../../core/types/reporting/ReportingUIKomponentenTyp';
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
	public static readonly GOST_KLAUSURPLANUNG_V_KLAUSURTERMINE_MIT_KURSEN: ReportingReportvorlage = new ReportingReportvorlage("GOST_KLAUSURPLANUNG_V_KLAUSURTERMINE_MIT_KURSEN", 0, "GostKlausurplanung-KlausurtermineMitKursen", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitKursklausuren", "mit Kursklausuren", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitNachschreibern", "mit Nachschreibern", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitKlausurschreiberNamen", "mit Namen der Klausurschreiber", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1)));

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
	public static readonly KLASSEN_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER: ReportingReportvorlage = new ReportingReportvorlage("KLASSEN_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER", 7, "Klasse-Liste-Schueler-Kontaktdaten-Erzieher", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("nurSchuelerRufname", "nur Rufname", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerGeschlecht", "mit Geschlecht", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerGebDat", "mit Geburtsdatum", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerStaat", "mit Staatsangehörigkeit", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerAnschrift", "mit Anschrift", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerTelefonPrivat", "mit Telefon (privat)", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerEmailSchule", "mit E-Mail (Schule)", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerEmailPrivat", "mit E-Mail (privat)", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSpalteSchuelerTelefonKontakte", "mit Telefonkontakten", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieher", "mit Erziehern", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieherAnschrift", "mit Erzieher-Anschrift", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieherEmailPrivat", "mit Erzieher-E-Mail", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1)));

	/**
	 * Report-Vorlage: Klasse - Liste - Schüler - Leistungsdaten
	 */
	public static readonly KLASSEN_V_LISTE_SCHUELER_LEISTUNGSDATEN: ReportingReportvorlage = new ReportingReportvorlage("KLASSEN_V_LISTE_SCHUELER_LEISTUNGSDATEN", 8, "Klasse-Liste-Schueler-Leistungsdaten", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPunktenStattNoten", "Punkte statt Noten ausgeben", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitQuartalsnote", "Quartalsnoten statt Noten ausgeben", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitIndividuellerKursart", "mit individueller Kursart", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitZuweisung", "mit Zuweisungen", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitGesamtfehlstunden", "mit Gesamtfehlstunden", ReportingVorlageParameterTyp.BOOLEAN, "true", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitFachbezogenenFehlstunden", "mit fachbezogenen Fehlstunden", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitFachbezogenenBemerkungen", "mit fachbezogenen Bemerkungen", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitLernentwicklung", "mit Angabe zur Lernentwicklung", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitFoerderschwerpunkt", "mit Angaben zum Förderschwerpunkt", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitASVBemerkung", "mit ASV-Bemerkungen", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitAUEBemerkung", "mit AUE-Bemerkungen", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitZeugnisbemerkung", "mit Zeugnisbemerkungen", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchulformempfehlung", "mit Empfehlung der Schulform", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitVersetzungAbschluss", "mit Versetzung und Abschluss", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitVersetzungsentscheidung", "mit Text zur Versetzungsentscheidung", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1)));

	/**
	 * Report-Vorlage: Kurs - Liste - Schüler - Kontaktdaten - Erzieher
	 */
	public static readonly KURSE_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER: ReportingReportvorlage = new ReportingReportvorlage("KURSE_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER", 9, "Kurs-Liste-Schueler-Kontaktdaten-Erzieher", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerKlasse", "mit Klasse", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("nurSchuelerRufname", "nur Rufname", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerGeschlecht", "mit Geschlecht", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerGebDat", "mit Geburtsdatum", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerStaat", "mit Staatsangehörigkeit", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerAnschrift", "mit Anschrift", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerTelefonPrivat", "mit Telefon (privat)", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerEmailSchule", "mit E-Mail (Schule)", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerEmailPrivat", "mit E-Mail (privat)", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSpalteSchuelerTelefonKontakte", "mit Telefonkontakten", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieher", "mit Erziehern", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieherAnschrift", "mit Erzieher-Anschrift", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieherEmailPrivat", "mit Erzieher-E-Mail", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1)));

	/**
	 * Report-Vorlage: Kurs - Liste - Schüler - Leistungsdaten
	 */
	public static readonly KURSE_V_LISTE_SCHUELER_LEISTUNGSDATEN: ReportingReportvorlage = new ReportingReportvorlage("KURSE_V_LISTE_SCHUELER_LEISTUNGSDATEN", 10, "Kurs-Liste-Schueler-Leistungsdaten", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPunktenStattNoten", "Punkte statt Noten ausgeben", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitBemerkungen", "mit fachbezogenen Bemerkungen", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1)));

	/**
	 * Report-Vorlage: Lehrer - Liste - Schüler - Leistungsdaten
	 */
	public static readonly LEHRER_V_LISTE_SCHUELER_LEISTUNGSDATEN: ReportingReportvorlage = new ReportingReportvorlage("LEHRER_V_LISTE_SCHUELER_LEISTUNGSDATEN", 11, "Lehrer-Liste-Schueler-Leistungsdaten", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitKlassenunterricht", "mit Klassenunterricht", ReportingVorlageParameterTyp.BOOLEAN, "true", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitKursunterricht", "mit Kursunterricht", ReportingVorlageParameterTyp.BOOLEAN, "true", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitPunktenStattNoten", "Punkte statt Noten ausgeben", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitBemerkungen", "mit fachbezogenen Bemerkungen", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1)));

	/**
	 * Report-Vorlage: Lehrer - Stammdaten - Liste
	 */
	public static readonly LEHRER_V_STAMMDATENLISTE: ReportingReportvorlage = new ReportingReportvorlage("LEHRER_V_STAMMDATENLISTE", 12, "Lehrer-Stammdatenliste", new ArrayList());

	/**
	 * Report-Vorlage: GOSt - Abitur - APO - Anlage 12 (Abiturzeugnis) - Din-A4
	 */
	public static readonly SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A4: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A4", 13, "Schueler-GostAbiturApoAnlage12-A4", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPersoenlichenUnterschriften", "mit persönlichen Unterschriften", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitZweiterBeratungslehrerUnterschrift", "mit Unterschrift 2. Beratungslehrer", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("textZAAVorsitzUnterschrift", "Unterschrift ZAA-Vorsitz", ReportingVorlageParameterTyp.STRING, "", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("textZAAVorsitzUnterschriftBezeichnung", "Bezeichnung ZAA-Vorsitz", ReportingVorlageParameterTyp.STRING, "", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("textSchulleitungUnterschrift", "Unterschrift Schulleitung", ReportingVorlageParameterTyp.STRING, "", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("textSchulleitungUnterschriftBezeichnung", "Bezeichnung Schulleitung", ReportingVorlageParameterTyp.STRING, "", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("textSchultraegerUnterschrift", "Unterschrift Schulträger", ReportingVorlageParameterTyp.STRING, "", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("textSchultraegerUnterschriftBezeichnung", "Bezeichnung Schulträger", ReportingVorlageParameterTyp.STRING, "", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("textBeratungslehrerUnterschrift", "Unterschrift Beratungslehrer", ReportingVorlageParameterTyp.STRING, "", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("textBeratungslehrerUnterschriftBezeichnung", "Bezeichnung Beratungslehrer", ReportingVorlageParameterTyp.STRING, "", "true", ReportingUIKomponentenTyp.CHECKBOX, 1)));

	/**
	 * Report-Vorlage: GOSt - Abitur - APO - Anlage 12 (Abiturzeugnis) - Din-A3
	 */
	public static readonly SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A3: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A3", 14, "Schueler-GostAbiturApoAnlage12-A3", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPersoenlichenUnterschriften", "mit persönlichen Unterschriften", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitZweiterBeratungslehrerUnterschrift", "mit Unterschrift 2. Beratungslehrer", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("textZAAVorsitzUnterschrift", "Unterschrift ZAA-Vorsitz", ReportingVorlageParameterTyp.STRING, "", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("textZAAVorsitzUnterschriftBezeichnung", "Bezeichnung ZAA-Vorsitz", ReportingVorlageParameterTyp.STRING, "", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("textSchulleitungUnterschrift", "Unterschrift Schulleitung", ReportingVorlageParameterTyp.STRING, "", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("textSchulleitungUnterschriftBezeichnung", "Bezeichnung Schulleitung", ReportingVorlageParameterTyp.STRING, "", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("textSchultraegerUnterschrift", "Unterschrift Schulträger", ReportingVorlageParameterTyp.STRING, "", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("textSchultraegerUnterschriftBezeichnung", "Bezeichnung Schulträger", ReportingVorlageParameterTyp.STRING, "", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("textBeratungslehrerUnterschrift", "Unterschrift Beratungslehrer", ReportingVorlageParameterTyp.STRING, "", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("textBeratungslehrerUnterschriftBezeichnung", "Bezeichnung Beratungslehrer", ReportingVorlageParameterTyp.STRING, "", "true", ReportingUIKomponentenTyp.CHECKBOX, 1)));

	/**
	 * Report-Vorlage: GOSt - Laufbahnplanung - Ergebnisübersicht
	 */
	public static readonly SCHUELER_V_GOST_LAUFBAHNPLANUNG_ERGEBNISUEBERSICHT: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_V_GOST_LAUFBAHNPLANUNG_ERGEBNISUEBERSICHT", 15, "Schueler-GostLaufbahnplanungErgebnisuebersicht", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitFehlernKommentaren", "mit Fehlern/Kommentaren", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitHinweisen", "mit Hinweisen", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1)));

	/**
	 * Report-Vorlage: GOSt - Laufbahnplanung - Wahlbogen
	 */
	public static readonly SCHUELER_V_GOST_LAUFBAHNPLANUNG_WAHLBOGEN: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_V_GOST_LAUFBAHNPLANUNG_WAHLBOGEN", 16, "Schueler-GostLaufbahnplanungWahlbogen", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("nurBelegteFaecher", "nur belegte Fächer", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1)));

	/**
	 * Report-Vorlage: Schüler - Schulbescheinigung
	 */
	public static readonly SCHUELER_V_SCHULBESCHEINIGUNG: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_V_SCHULBESCHEINIGUNG", 17, "Schueler-Schulbescheinigung", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("fuerErzieher", "für Erzieher", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitBildBriefkopf", "mit Bild im Briefkopf", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchullogo", "mit Schullogo", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("keineAnschrift", "ohne Anschrift", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("keinInfoblock", "ohne Infoblock", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("keineUnterschrift", "ohne Unterschrift", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1)));

	/**
	 * Report-Vorlage: Schüler - Liste - Kontaktdaten - Erzieher
	 */
	public static readonly SCHUELER_V_LISTE_KONTAKTDATENERZIEHER: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_V_LISTE_KONTAKTDATENERZIEHER", 18, "Schueler-Liste-Kontaktdaten-Erzieher", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerKlasse", "mit Klasse", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("nurSchuelerRufname", "nur Rufname", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerGeschlecht", "mit Geschlecht", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerGebDat", "mit Geburtsdatum", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerStaat", "mit Staatsangehörigkeit", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerAnschrift", "mit Anschrift", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerTelefonPrivat", "mit Telefon (privat)", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerEmailSchule", "mit E-Mail (Schule)", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerEmailPrivat", "mit E-Mail (privat)", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSpalteSchuelerTelefonKontakte", "mit Telefonkontakten", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieher", "mit Erziehern", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieherAnschrift", "mit Erzieher-Anschrift", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieherEmailPrivat", "mit Erzieher-E-Mail", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1)));

	/**
	 * Report-Vorlage: Stundenplanung - Fach - Stundenplan
	 */
	public static readonly STUNDENPLANUNG_V_FACH_STUNDENPLAN: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_V_FACH_STUNDENPLAN", 19, "Stundenplanung-FachStundenplan", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1)));

	/**
	 * Report-Vorlage: Stundenplanung - Klasse - Stundenplan
	 */
	public static readonly STUNDENPLANUNG_V_KLASSEN_STUNDENPLAN: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_V_KLASSEN_STUNDENPLAN", 20, "Stundenplanung-KlassenStundenplan", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitFachStattKursbezeichnung", "Fach statt Kursbezeichnung", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung", "Fachkürzel statt Fachbezeichnung", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1)));

	/**
	 * Report-Vorlage: Stundenplanung - Lehrer - Stundenplan
	 */
	public static readonly STUNDENPLANUNG_V_LEHRER_STUNDENPLAN: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_V_LEHRER_STUNDENPLAN", 21, "Stundenplanung-LehrerStundenplan", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitPausenaufsichten", "mit Pausenaufsichten", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung", "Fachkürzel statt Fachbezeichnung", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1)));

	/**
	 * Report-Vorlage: Stundenplanung - Lehrer - Stundenplan - Kombiniert
	 */
	public static readonly STUNDENPLANUNG_V_LEHRER_STUNDENPLAN_KOMBINIERT: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_V_LEHRER_STUNDENPLAN_KOMBINIERT", 22, "Stundenplanung-LehrerStundenplanKombiniert", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitPausenaufsichten", "mit Pausenaufsichten", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung", "Fachkürzel statt Fachbezeichnung", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1)));

	/**
	 * Report-Vorlage: Stundenplanung - Fach - Stundenplan
	 */
	public static readonly STUNDENPLANUNG_V_RAUM_STUNDENPLAN: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_V_RAUM_STUNDENPLAN", 23, "Stundenplanung-RaumStundenplan", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1)));

	/**
	 * Report-Vorlage: Stundenplanung - Schüler - Stundenplan
	 */
	public static readonly STUNDENPLANUNG_V_SCHUELER_STUNDENPLAN: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_V_SCHUELER_STUNDENPLAN", 24, "Stundenplanung-SchuelerStundenplan", Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitFachStattKursbezeichnung", "Fach statt Kursbezeichnung", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung", "Fachkürzel statt Fachbezeichnung", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitIndividuelleKursart", "mit individueller Kursart", ReportingVorlageParameterTyp.BOOLEAN, "false", "true", ReportingUIKomponentenTyp.CHECKBOX, 1)));

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
	 * Erstellt einen neuen Vorlage-Parameter mit dem angegebenen Namen, Typ und Wert, mit UI-Parametern.
	 *
	 * @param name                Der Name des Vorlage-Parameters. Darf nicht null sein.
	 * @param bezeichnung         Die Bezeichnung des Vorlage-Parameters. Darf nicht null sein.
	 * @param typ                 Der Typ des Vorlage-Parameters. Darf nicht null sein.
	 * @param wert                Der Wert des Vorlage-Parameters. Darf nicht null sein.
	 * @param istSichtbar         Gibt an, ob der Parameter in der UI sichtbar sein soll. Darf nicht null sein.
	 * @param komponentenTyp      Der Typ der UI-Komponente (z.B. 'checkbox', 'input', 'select', 'textarea', 'numberPicker', 'datePicker').
	 * @param anzahlSpalten       Die Anzahl der Grid-Spalten, die der Parameter in der UI einnehmen soll.
	 * @return Ein neues Objekt der Klasse {@link ReportingVorlageParameter}, das den angegebenen Namen, Typ und Wert enthält.
	 */
	private static erzeugeVorlageParameter(name: string, bezeichnung: string, typ: ReportingVorlageParameterTyp, wert: string, istSichtbar: string, komponentenTyp: ReportingUIKomponentenTyp, anzahlSpalten: number): ReportingVorlageParameter {
		const reportingVorlageParameter: ReportingVorlageParameter | null = new ReportingVorlageParameter();
		reportingVorlageParameter.name = name;
		reportingVorlageParameter.bezeichnung = bezeichnung;
		reportingVorlageParameter.typ = typ.getId();
		reportingVorlageParameter.wert = wert;
		reportingVorlageParameter.istSichtbar = istSichtbar;
		reportingVorlageParameter.komponentenTyp = komponentenTyp.getId();
		reportingVorlageParameter.spaltenAnzahl = anzahlSpalten;
		return reportingVorlageParameter;
	}

	/**
	 * Erstellt eine neue Reporting-Vorlage-Parametergruppe mit den angegebenen Parametern, mit UI-Parametern.
	 *
	 * @param name                      Der Titel der Gruppe. Darf nicht null sein.
	 * @param beschreibung              Die Beschreibung der Gruppe. Darf nicht null sein.
	 * @param istSichtbar               Gibt an, ob die Gruppe in der UI sichtbar sein soll. Darf nicht null sein.
	 * @param anzahlSpalten             Die Anzahl der Grid-Spalten, die die Gruppe in der UI einnehmen soll.
	 * @param reportingVorlageParameter Eine Liste von ReportingVorlageParametern, die in der Gruppe enthalten sein sollen.
	 * @return Eine neue Instanz von ReportingUIGruppenDefinition.
	 */
	private static erzeugeReportingVorlageParameterGruppe(name: string, beschreibung: string, istSichtbar: string, anzahlSpalten: number, reportingVorlageParameter: List<ReportingVorlageParameter>): ReportingVorlageParameterGruppe {
		const reportingVorlageParameterGruppe: ReportingVorlageParameterGruppe | null = new ReportingVorlageParameterGruppe();
		reportingVorlageParameterGruppe.name = name;
		reportingVorlageParameterGruppe.beschreibung = beschreibung;
		reportingVorlageParameterGruppe.istSichtbar = istSichtbar;
		reportingVorlageParameterGruppe.anzahlSpalten = anzahlSpalten;
		reportingVorlageParameterGruppe.reportingVorlageParameterList = reportingVorlageParameter;
		return reportingVorlageParameterGruppe;
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
