import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { ReportingReportvorlageParameter } from '../../../core/data/reporting/ReportingReportvorlageParameter';
import type { JavaSet } from '../../../java/util/JavaSet';
import { HashMap } from '../../../java/util/HashMap';
import { ReportingSortierungDefinitionGruppe } from '../../../core/data/reporting/ReportingSortierungDefinitionGruppe';
import { ReportingEMailDaten } from '../../../core/data/reporting/ReportingEMailDaten';
import { ArrayList } from '../../../java/util/ArrayList';
import { JavaString } from '../../../java/lang/JavaString';
import { ReportingFilterKriterium } from '../../../core/data/reporting/ReportingFilterKriterium';
import { ReportingParameter } from '../../../core/data/reporting/ReportingParameter';
import { ReportingSortierungDefinition } from '../../../core/data/reporting/ReportingSortierungDefinition';
import { ReportingFilterDefinitionGruppe } from '../../../core/data/reporting/ReportingFilterDefinitionGruppe';
import type { List } from '../../../java/util/List';
import { ReportingAusgabeformat } from '../../../core/types/reporting/ReportingAusgabeformat';
import { HashSet } from '../../../java/util/HashSet';
import { ReportingFilterDefinitionFactory } from '../../../core/utils/reporting/ReportingFilterDefinitionFactory';
import { ReportingFilterVerknuepfung } from '../../../core/types/reporting/ReportingFilterVerknuepfung';
import { ReportingFilterDefinition } from '../../../core/data/reporting/ReportingFilterDefinition';
import { ReportingReportvorlageParameterTyp } from '../../../core/types/reporting/ReportingReportvorlageParameterTyp';
import { ReportingUIKomponentenTyp } from '../../../core/types/reporting/ReportingUIKomponentenTyp';
import { ReportingSortierungDefinitionFactory } from '../../../core/utils/reporting/ReportingSortierungDefinitionFactory';
import { ReportingFilterEintrag } from '../../../core/data/reporting/ReportingFilterEintrag';
import { ReportingReportvorlageParameterGruppe } from '../../../core/data/reporting/ReportingReportvorlageParameterGruppe';
import { Class } from '../../../java/lang/Class';
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
	public static readonly GOST_KLAUSURPLANUNG_V_KLAUSURTERMINE_MIT_KURSEN: ReportingReportvorlage = new ReportingReportvorlage("GOST_KLAUSURPLANUNG_V_KLAUSURTERMINE_MIT_KURSEN", 0, "GostKlausurplanung-KlausurtermineMitKursen", ReportingReportvorlage.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.PDF.getId()), ArrayList.of(ReportingReportvorlage.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "", true, 1, Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitKursklausuren", "mit Kursklausuren", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitNachschreibern", "mit Nachschreibern", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitKlausurschreiberNamen", "mit Namen der Klausurschreiber", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)))), new ArrayList(), new ArrayList(), false, false, true));

	/**
	 * Report-Vorlage: GOSt - Klausurplanung - Schueler-Klausuren
	 */
	public static readonly GOST_KLAUSURPLANUNG_V_SCHUELER_MIT_KLAUSUREN: ReportingReportvorlage = new ReportingReportvorlage("GOST_KLAUSURPLANUNG_V_SCHUELER_MIT_KLAUSUREN", 1, "GostKlausurplanung-SchuelerMitKlausuren", ReportingReportvorlage.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.PDF.getId()), new ArrayList(), new ArrayList(), new ArrayList(), false, true, true));

	/**
	 * Report-Vorlage: GOSt - Kursplanung - Kurs-Kurschüler
	 */
	public static readonly GOST_KURSPLANUNG_V_KURS_MIT_KURSSCHUELERN: ReportingReportvorlage = new ReportingReportvorlage("GOST_KURSPLANUNG_V_KURS_MIT_KURSSCHUELERN", 2, "GostKursplanung-KursMitKursschuelern", ReportingReportvorlage.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.PDF.getId()), new ArrayList(), new ArrayList(), new ArrayList(), false, false, true));

	/**
	 * Report-Vorlage: GOSt - Kursplanung - Kurse-Statistikwerte
	 */
	public static readonly GOST_KURSPLANUNG_V_KURSE_MIT_STATISTIKWERTEN: ReportingReportvorlage = new ReportingReportvorlage("GOST_KURSPLANUNG_V_KURSE_MIT_STATISTIKWERTEN", 3, "GostKursplanung-KurseMitStatistikwerten", ReportingReportvorlage.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.PDF.getId()), new ArrayList(), new ArrayList(), new ArrayList(), false, false, true));

	/**
	 * Report-Vorlage: GOSt - Kursplanung - Schüler-Kurse
	 */
	public static readonly GOST_KURSPLANUNG_V_SCHUELER_MIT_KURSEN: ReportingReportvorlage = new ReportingReportvorlage("GOST_KURSPLANUNG_V_SCHUELER_MIT_KURSEN", 4, "GostKursplanung-SchuelerMitKursen", ReportingReportvorlage.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.PDF.getId()), new ArrayList(), new ArrayList(), new ArrayList(), false, false, true));

	/**
	 * Report-Vorlage: GOSt - Kursplanung - Schüler-Schienen-Kurse
	 */
	public static readonly GOST_KURSPLANUNG_V_SCHUELER_MIT_SCHIENEN_KURSEN: ReportingReportvorlage = new ReportingReportvorlage("GOST_KURSPLANUNG_V_SCHUELER_MIT_SCHIENEN_KURSEN", 5, "GostKursplanung-SchuelerMitSchienenKursen", ReportingReportvorlage.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.PDF.getId()), new ArrayList(), new ArrayList(), new ArrayList(), false, false, true));

	/**
	 * Report-Vorlage: GOSt - Laufbahnplanung - Abiturjahrgang - Fachwahlstatistiken
	 */
	public static readonly GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG_V_FACHWAHLSTATISTIKEN: ReportingReportvorlage = new ReportingReportvorlage("GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG_V_FACHWAHLSTATISTIKEN", 6, "GostLaufbahnplanung-Abiturjahrgang-Fachwahlstatistiken", ReportingReportvorlage.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.PDF.getId()), new ArrayList(), new ArrayList(), new ArrayList(), false, false, true));

	/**
	 * Report-Vorlage: Klasse - Liste - Schüler - Kontaktdaten - Erzieher
	 */
	public static readonly KLASSEN_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER: ReportingReportvorlage = new ReportingReportvorlage("KLASSEN_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER", 7, "Klasse-Liste-Schueler-Kontaktdaten-Erzieher", ReportingReportvorlage.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.PDF.getId(), ReportingAusgabeformat.EMAIL.getId()), ArrayList.of(ReportingReportvorlage.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "", true, 3, Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("nurSchuelerRufname", "nur Rufname", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerGeschlecht", "mit Geschlecht", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerGebDat", "mit Geburtsdatum", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerStaat", "mit Staatsangehörigkeit", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerAnschrift", "mit Anschrift", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerTelefonPrivat", "mit Telefon (privat)", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerEmailSchule", "mit E-Mail (Schule)", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerEmailPrivat", "mit E-Mail (privat)", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSpalteSchuelerTelefonKontakte", "mit Telefonkontakten", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieher", "mit Erziehern", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieherAnschrift", "mit Erzieher-Anschrift", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieherEmailPrivat", "mit Erzieher-E-Mail", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)))), new ArrayList(), new ArrayList(), true, false, true));

	/**
	 * Report-Vorlage: Klasse - Liste - Schüler - Leistungsdaten
	 */
	public static readonly KLASSEN_V_LISTE_SCHUELER_LEISTUNGSDATEN: ReportingReportvorlage = new ReportingReportvorlage("KLASSEN_V_LISTE_SCHUELER_LEISTUNGSDATEN", 8, "Klasse-Liste-Schueler-Leistungsdaten", ReportingReportvorlage.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.PDF.getId()), ArrayList.of(ReportingReportvorlage.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "", true, 2, Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPunktenStattNoten", "Punkte statt Noten ausgeben", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitQuartalsnote", "Quartalsnoten statt Noten ausgeben", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitIndividuellerKursart", "mit individueller Kursart", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitZuweisung", "mit Zuweisungen", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitGesamtfehlstunden", "mit Gesamtfehlstunden", ReportingReportvorlageParameterTyp.BOOLEAN, "" + true, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitFachbezogenenFehlstunden", "mit fachbezogenen Fehlstunden", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitFachbezogenenBemerkungen", "mit fachbezogenen Bemerkungen", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitLernentwicklung", "mit Angabe zur Lernentwicklung", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitFoerderschwerpunkt", "mit Angaben zum Förderschwerpunkt", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitASVBemerkung", "mit ASV-Bemerkungen", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitAUEBemerkung", "mit AUE-Bemerkungen", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitZeugnisbemerkung", "mit Zeugnisbemerkungen", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchulformempfehlung", "mit Empfehlung der Schulform", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitVersetzungAbschluss", "mit Versetzung und Abschluss", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitVersetzungsentscheidung", "mit Text zur Versetzungsentscheidung", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)))), ArrayList.of(ReportingReportvorlage.erzeugeSortierungDefinitionGruppe("Fachsortierung", "ReportingFach", true, ReportingSortierungDefinitionFactory.definitionen(ReportingSortierungDefinitionFactory.standard("Standardsortierung der Fächer", "ReportingFach"), ReportingSortierungDefinitionFactory.definition("GOSt-Sortierung der Fächer", "ReportingFach", false, ArrayList.of("gostSortierung")), ReportingSortierungDefinitionFactory.definition("Sortierung nach Fachkürzeln", "ReportingFach", false, ArrayList.of("kuerzel"))))), ArrayList.of(ReportingReportvorlage.erzeugeFilterDefinitionGruppe("Fachfilter", "ReportingFach", true, true, ReportingFilterVerknuepfung.AND, ReportingFilterDefinitionFactory.definitionen(ReportingFilterDefinitionFactory.definition("Nur Fächer für Zeugnisrelevanz", "ReportingFach", ReportingFilterDefinitionFactory.and(ReportingFilterDefinitionFactory.eq("aufZeugnis", "true"))), ReportingFilterDefinitionFactory.definition("Nur Fächer mit Prüfungsordnungsrelevanz", "ReportingFach", ReportingFilterDefinitionFactory.and(ReportingFilterDefinitionFactory.eq("istPruefungsordnungsRelevant", "true")))))), true, false, true));

	/**
	 * Report-Vorlage: Kurs - Liste - Schüler - Kontaktdaten - Erzieher
	 */
	public static readonly KURSE_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER: ReportingReportvorlage = new ReportingReportvorlage("KURSE_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER", 9, "Kurs-Liste-Schueler-Kontaktdaten-Erzieher", ReportingReportvorlage.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.PDF.getId(), ReportingAusgabeformat.EMAIL.getId()), ArrayList.of(ReportingReportvorlage.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "", true, 3, Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerKlasse", "mit Klasse", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 3), ReportingReportvorlage.erzeugeVorlageParameter("nurSchuelerRufname", "nur Rufname", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerGeschlecht", "mit Geschlecht", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerGebDat", "mit Geburtsdatum", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerStaat", "mit Staatsangehörigkeit", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerAnschrift", "mit Anschrift", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerTelefonPrivat", "mit Telefon (privat)", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerEmailSchule", "mit E-Mail (Schule)", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerEmailPrivat", "mit E-Mail (privat)", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSpalteSchuelerTelefonKontakte", "mit Telefonkontakten", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieher", "mit Erziehern", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieherAnschrift", "mit Erzieher-Anschrift", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieherEmailPrivat", "mit Erzieher-E-Mail", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)))), new ArrayList(), new ArrayList(), true, false, true));

	/**
	 * Report-Vorlage: Kurs - Liste - Schüler - Leistungsdaten
	 */
	public static readonly KURSE_V_LISTE_SCHUELER_LEISTUNGSDATEN: ReportingReportvorlage = new ReportingReportvorlage("KURSE_V_LISTE_SCHUELER_LEISTUNGSDATEN", 10, "Kurs-Liste-Schueler-Leistungsdaten", ReportingReportvorlage.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.PDF.getId()), ArrayList.of(ReportingReportvorlage.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "", true, 1, Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPunktenStattNoten", "Punkte statt Noten ausgeben", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitBemerkungen", "mit fachbezogenen Bemerkungen", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)))), new ArrayList(), new ArrayList(), true, false, true));

	/**
	 * Report-Vorlage: Lehrer - Liste - Schüler - Leistungsdaten
	 */
	public static readonly LEHRER_V_LISTE_SCHUELER_LEISTUNGSDATEN: ReportingReportvorlage = new ReportingReportvorlage("LEHRER_V_LISTE_SCHUELER_LEISTUNGSDATEN", 11, "Lehrer-Liste-Schueler-Leistungsdaten", ReportingReportvorlage.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.PDF.getId()), ArrayList.of(ReportingReportvorlage.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "", true, 2, Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitKlassenunterricht", "mit Klassenunterricht", ReportingReportvorlageParameterTyp.BOOLEAN, "" + true, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitKursunterricht", "mit Kursunterricht", ReportingReportvorlageParameterTyp.BOOLEAN, "" + true, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitPunktenStattNoten", "Punkte statt Noten ausgeben", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitBemerkungen", "mit fachbezogenen Bemerkungen", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)))), new ArrayList(), new ArrayList(), true, false, true));

	/**
	 * Report-Vorlage: Lehrer - Stammdaten - Liste
	 */
	public static readonly LEHRER_V_STAMMDATENLISTE: ReportingReportvorlage = new ReportingReportvorlage("LEHRER_V_STAMMDATENLISTE", 12, "Lehrer-Stammdatenliste", ReportingReportvorlage.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.PDF.getId()), new ArrayList(), new ArrayList(), new ArrayList(), true, false, true));

	/**
	 * Report-Vorlage: GOSt - Abitur - APO - Anlage 12 (Abiturzeugnis) - DIN-A4
	 */
	public static readonly SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A4: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A4", 13, "Schueler-GostAbiturApoAnlage12-A4", ReportingReportvorlage.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.PDF.getId()), ArrayList.of(ReportingReportvorlage.erzeugeReportingvorlageParameterGruppe("Unterschriftenoptionen", "", true, 1, Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPersoenlichenUnterschriften", "mit persönlichen Unterschriften", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitZweiterBeratungslehrerUnterschrift", "mit Unterschrift 2. Beratungslehrer", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("textZAAVorsitzUnterschrift", "Unterschrift ZAA-Vorsitz", ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("textZAAVorsitzUnterschriftBezeichnung", "Bezeichnung ZAA-Vorsitz", ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1), ReportingReportvorlage.erzeugeVorlageParameter("textSchulleitungUnterschrift", "Unterschrift Schulleitung", ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("textSchulleitungUnterschriftBezeichnung", "Bezeichnung Schulleitung", ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1), ReportingReportvorlage.erzeugeVorlageParameter("textSchultraegerUnterschrift", "Unterschrift Schulträger", ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("textSchultraegerUnterschriftBezeichnung", "Bezeichnung Schulträger", ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1), ReportingReportvorlage.erzeugeVorlageParameter("textBeratungslehrerUnterschrift", "Unterschrift Beratungslehrer", ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1), ReportingReportvorlage.erzeugeVorlageParameter("textBeratungslehrerUnterschriftBezeichnung", "Bezeichnung Beratungslehrer", ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1)))), new ArrayList(), new ArrayList(), true, false, true));

	/**
	 * Report-Vorlage: GOSt - Abitur - APO - Anlage 12 (Abiturzeugnis) - DIN-A3
	 */
	public static readonly SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A3: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A3", 14, "Schueler-GostAbiturApoAnlage12-A3", ReportingReportvorlage.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.PDF.getId()), ArrayList.of(ReportingReportvorlage.erzeugeReportingvorlageParameterGruppe("Unterschriftenoptionen", "", true, 1, Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPersoenlichenUnterschriften", "mit persönlichen Unterschriften", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitZweiterBeratungslehrerUnterschrift", "mit Unterschrift 2. Beratungslehrer", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("textZAAVorsitzUnterschrift", "Unterschrift ZAA-Vorsitz", ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("textZAAVorsitzUnterschriftBezeichnung", "Bezeichnung ZAA-Vorsitz", ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1), ReportingReportvorlage.erzeugeVorlageParameter("textSchulleitungUnterschrift", "Unterschrift Schulleitung", ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("textSchulleitungUnterschriftBezeichnung", "Bezeichnung Schulleitung", ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1), ReportingReportvorlage.erzeugeVorlageParameter("textSchultraegerUnterschrift", "Unterschrift Schulträger", ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("textSchultraegerUnterschriftBezeichnung", "Bezeichnung Schulträger", ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1), ReportingReportvorlage.erzeugeVorlageParameter("textBeratungslehrerUnterschrift", "Unterschrift Beratungslehrer", ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1), ReportingReportvorlage.erzeugeVorlageParameter("textBeratungslehrerUnterschriftBezeichnung", "Bezeichnung Beratungslehrer", ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1)))), new ArrayList(), new ArrayList(), true, false, true));

	/**
	 * Report-Vorlage: GOSt - Laufbahnplanung - Ergebnisübersicht
	 */
	public static readonly SCHUELER_V_GOST_LAUFBAHNPLANUNG_ERGEBNISUEBERSICHT: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_V_GOST_LAUFBAHNPLANUNG_ERGEBNISUEBERSICHT", 15, "Schueler-GostLaufbahnplanungErgebnisuebersicht", ReportingReportvorlage.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.PDF.getId()), ArrayList.of(ReportingReportvorlage.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "", true, 1, Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitFehlernKommentaren", "mit Fehlern/Kommentaren", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitHinweisen", "mit Hinweisen", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)))), new ArrayList(), new ArrayList(), true, false, true));

	/**
	 * Report-Vorlage: GOSt - Laufbahnplanung - Wahlbogen
	 */
	public static readonly SCHUELER_V_GOST_LAUFBAHNPLANUNG_WAHLBOGEN: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_V_GOST_LAUFBAHNPLANUNG_WAHLBOGEN", 16, "Schueler-GostLaufbahnplanungWahlbogen", ReportingReportvorlage.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.PDF.getId()), ArrayList.of(ReportingReportvorlage.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "", true, 1, Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("nurBelegteFaecher", "nur belegte Fächer", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)))), new ArrayList(), new ArrayList(), true, false, true));

	/**
	 * Report-Vorlage: Schüler - Schulbescheinigung
	 */
	public static readonly SCHUELER_V_SCHULBESCHEINIGUNG: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_V_SCHULBESCHEINIGUNG", 17, "Schueler-Schulbescheinigung", ReportingReportvorlage.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.PDF.getId()), ArrayList.of(ReportingReportvorlage.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "", true, 3, Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("fuerErzieher", "für Erzieher", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 3), ReportingReportvorlage.erzeugeVorlageParameter("mitSchullogo", "mit Schullogo", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitBildBriefkopf", "mit Bild im Briefkopf", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 2), ReportingReportvorlage.erzeugeVorlageParameter("keineAnschrift", "ohne Anschrift", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("keinInfoblock", "ohne Infoblock", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("keineUnterschrift", "ohne Unterschrift", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)))), new ArrayList(), new ArrayList(), true, false, true));

	/**
	 * Report-Vorlage: Schüler - Liste - Kontaktdaten - Erzieher
	 */
	public static readonly SCHUELER_V_LISTE_KONTAKTDATENERZIEHER: ReportingReportvorlage = new ReportingReportvorlage("SCHUELER_V_LISTE_KONTAKTDATENERZIEHER", 18, "Schueler-Liste-Kontaktdaten-Erzieher", ReportingReportvorlage.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.PDF.getId()), ArrayList.of(ReportingReportvorlage.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "", true, 3, Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerKlasse", "mit Klasse", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 3), ReportingReportvorlage.erzeugeVorlageParameter("nurSchuelerRufname", "nur Rufname", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerGeschlecht", "mit Geschlecht", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerGebDat", "mit Geburtsdatum", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerStaat", "mit Staatsangehörigkeit", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerAnschrift", "mit Anschrift", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerTelefonPrivat", "mit Telefon (privat)", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerEmailSchule", "mit E-Mail (Schule)", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSchuelerEmailPrivat", "mit E-Mail (privat)", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitSpalteSchuelerTelefonKontakte", "mit Telefonkontakten", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieher", "mit Erziehern", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieherAnschrift", "mit Erzieher-Anschrift", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitErzieherEmailPrivat", "mit Erzieher-E-Mail", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)))), ArrayList.of(ReportingReportvorlage.erzeugeSortierungDefinitionGruppe("Schülersortierung", "ReportingSchueler", true, ReportingSortierungDefinitionFactory.definitionen(ReportingSortierungDefinitionFactory.standard("Standardsortierung der Schüler", "ReportingSchueler"), ReportingSortierungDefinitionFactory.definition("Sortierung nach Klasse, Name, Vorname", "ReportingSchueler", false, ArrayList.of("Klasse, Nachname, Vorname, Vornamen"))))), new ArrayList(), false, false, true));

	/**
	 * Report-Vorlage: Stundenplanung - Fach - Stundenplan
	 */
	public static readonly STUNDENPLANUNG_V_FACH_STUNDENPLAN: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_V_FACH_STUNDENPLAN", 19, "Stundenplanung-FachStundenplan", ReportingReportvorlage.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.PDF.getId()), ArrayList.of(ReportingReportvorlage.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "", true, 2, Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)))), new ArrayList(), new ArrayList(), false, true, true));

	/**
	 * Report-Vorlage: Stundenplanung - Klasse - Stundenplan
	 */
	public static readonly STUNDENPLANUNG_V_KLASSEN_STUNDENPLAN: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_V_KLASSEN_STUNDENPLAN", 20, "Stundenplanung-KlassenStundenplan", ReportingReportvorlage.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.PDF.getId(), ReportingAusgabeformat.EMAIL.getId()), ArrayList.of(ReportingReportvorlage.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "", true, 2, Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitFachStattKursbezeichnung", "Fach statt Kursbezeichnung", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung", "Fachkürzel statt Fachbezeichnung", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)))), new ArrayList(), new ArrayList(), false, true, true));

	/**
	 * Report-Vorlage: Stundenplanung - Lehrer - Stundenplan
	 */
	public static readonly STUNDENPLANUNG_V_LEHRER_STUNDENPLAN: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_V_LEHRER_STUNDENPLAN", 21, "Stundenplanung-LehrerStundenplan", ReportingReportvorlage.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.PDF.getId(), ReportingAusgabeformat.EMAIL.getId()), ArrayList.of(ReportingReportvorlage.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "", true, 2, Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitPausenaufsichten", "mit Pausenaufsichten", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung", "Fachkürzel statt Fachbezeichnung", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)))), new ArrayList(), new ArrayList(), false, true, true));

	/**
	 * Report-Vorlage: Stundenplanung - Lehrer - Stundenplan - Kombiniert
	 */
	public static readonly STUNDENPLANUNG_V_LEHRER_STUNDENPLAN_KOMBINIERT: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_V_LEHRER_STUNDENPLAN_KOMBINIERT", 22, "Stundenplanung-LehrerStundenplanKombiniert", ReportingReportvorlage.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.PDF.getId()), ArrayList.of(ReportingReportvorlage.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "", true, 2, Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitPausenaufsichten", "mit Pausenaufsichten", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung", "Fachkürzel statt Fachbezeichnung", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)))), new ArrayList(), new ArrayList(), false, false, true));

	/**
	 * Report-Vorlage: Stundenplanung - Fach - Stundenplan
	 */
	public static readonly STUNDENPLANUNG_V_RAUM_STUNDENPLAN: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_V_RAUM_STUNDENPLAN", 23, "Stundenplanung-RaumStundenplan", ReportingReportvorlage.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.PDF.getId()), ArrayList.of(ReportingReportvorlage.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "", true, 2, Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)))), new ArrayList(), new ArrayList(), false, true, true));

	/**
	 * Report-Vorlage: Stundenplanung - Schüler - Stundenplan
	 */
	public static readonly STUNDENPLANUNG_V_SCHUELER_STUNDENPLAN: ReportingReportvorlage = new ReportingReportvorlage("STUNDENPLANUNG_V_SCHUELER_STUNDENPLAN", 24, "Stundenplanung-SchuelerStundenplan", ReportingReportvorlage.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.PDF.getId(), ReportingAusgabeformat.EMAIL.getId()), ArrayList.of(ReportingReportvorlage.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "", true, 2, Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitFachStattKursbezeichnung", "Fach statt Kursbezeichnung", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung", "Fachkürzel statt Fachbezeichnung", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("mitIndividuelleKursart", "mit individueller Kursart", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)))), new ArrayList(), new ArrayList(), false, true, true));

	/**
	 * Die Bezeichnung der Report-Vorlage
	 */
	private readonly bezeichnung: string;

	/**
	 * Reporting-Parameter inkl. der gültigen Vorlage-Parametergruppen für diese Report-Vorlage.
	 */
	private readonly reportingParameter: ReportingParameter;

	/**
	 * Interne Map für direkten Zugriff auf Parameter: Reportvorlage > Parametername > Parameter
	 */
	private static readonly MAP_PARAMETER: JavaMap<string, JavaMap<string, ReportingReportvorlageParameter>> = new HashMap<string, JavaMap<string, ReportingReportvorlageParameter>>();

	/**
	 * Interne Map für direkten Zugriff auf Parametergruppen: Reportvorlage > Parametergruppe > Parameterliste
	 */
	private static readonly MAP_PARAMETERGRUPPEN: JavaMap<string, JavaMap<string, ReportingReportvorlageParameterGruppe>> = new HashMap<string, JavaMap<string, ReportingReportvorlageParameterGruppe>>();

	/**
	 * Interner Index, der Namensbestandteile der Enum mit den passenden Reportvorlagen verwaltet.
	 */
	private static readonly MAP_NAMENSBESTANDTEILE_REPORTVORLAGEN: JavaMap<string, List<ReportingReportvorlage>> = new HashMap<string, List<ReportingReportvorlage>>();

	/**
	 * Gibt an, ob die internen Indexstrukturen bereits initialisiert wurden.
	 */
	private static mapsInitialisiert: boolean = false;

	/**
	 * Konstruktor für eine Reporting-Reportvorlage.
	 *
	 * @param bezeichnung Die Bezeichnung der Reportvorlage. Darf nicht null sein.
	 * @param reportingParameter Eine Liste mit den Vorlage-Parametern, basierend auf der jeweiligen Definition. Darf nicht null sein.
	 */
	private constructor(name: string, ordinal: number, bezeichnung: string, reportingParameter: ReportingParameter) {
		super(name, ordinal);
		ReportingReportvorlage.all_values_by_ordinal.push(this);
		ReportingReportvorlage.all_values_by_name.set(name, this);
		this.bezeichnung = bezeichnung;
		reportingParameter.reportvorlage = bezeichnung;
		this.reportingParameter = reportingParameter;
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
	 * Gibt eine Kopie der ReportingParamater für diese Report-Vorlage zurück. So wird verhindert, dass die Werte der ReportingParameter in der ENUM
	 * verändert werden können, was im Client Server weit greifen würde.
	 *
	 * @return Die Kopie der ReportingParameter für diese Report-Vorlage.
	 */
	public getReportingParameter(): ReportingParameter {
		return ReportingReportvorlage.cloneReportingParameter(this.reportingParameter);
	}

	/**
	 * Diese Methode ermittelt die Report-Vorlage anhand der übergebenen Bezeichnung.
	 *
	 * @param bezeichnung Die Bezeichnung der Report-Vorlage
	 *
	 * @return Die Report-Vorlage
	 */
	public static getByBezeichnung(bezeichnung: string): ReportingReportvorlage | null {
		if (JavaString.isEmpty(bezeichnung)) {
			return null;
		}
		for (const rv of ReportingReportvorlage.values()) {
			if (JavaObject.equalsTranspiler(rv.bezeichnung, (bezeichnung))) {
				return rv;
			}
		}
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
		if (JavaString.isEmpty(name)) {
			return null;
		}
		for (const rv of ReportingReportvorlage.values()) {
			if (JavaString.equalsIgnoreCase(rv.name(), name)) {
				return rv;
			}
		}
		return null;
	}

	/**
	 * Ermittelt alle Report-Vorlagen, deren Enum-Name alle übergebenen Namensbestandteile enthält. Dabei werden im Enum-Namen nur die durch '_' getrennten Teile
	 * berücksichtigt.
	 * Der Vergleich erfolgt ohne Beachtung der Groß-/Kleinschreibung für Namensbestandteile mit einer Länge größer oder gleich 3. Suchbegriffe, die diese
	 * Bedingungen nicht erfüllen, werden ignoriert.
	 *
	 * @param namensbestandteile  die Liste der Namensbestandteile, die in den Namensbestandteilen enthalten sein müssen
	 *
	 * @return eine Liste aller passenden Report-Vorlagen
	 */
	public static getByNamensbestandteilen(namensbestandteile: List<string>): List<ReportingReportvorlage> {
		ReportingReportvorlage.mapsInitialisieren();
		const namensbestandteileNormalisiert: List<string> | null = new ArrayList<string>();
		for (const bestandteil of namensbestandteile) {
			if ((bestandteil !== null) && !JavaString.isBlank(bestandteil) && (bestandteil.trim().length >= 3)) {
				namensbestandteileNormalisiert.add(bestandteil.trim().toLowerCase());
			}
		}
		let gueltigeVorlagen: JavaSet<ReportingReportvorlage> | null = null;
		for (const suchbegriff of namensbestandteileNormalisiert) {
			const treffer: List<ReportingReportvorlage> | null = ReportingReportvorlage.MAP_NAMENSBESTANDTEILE_REPORTVORLAGEN.get(suchbegriff);
			if ((treffer === null) || treffer.isEmpty()) {
				return new ArrayList();
			}
			if (gueltigeVorlagen === null) {
				gueltigeVorlagen = new HashSet(treffer);
			} else {
				gueltigeVorlagen.retainAll(treffer);
				if (gueltigeVorlagen.isEmpty()) {
					return new ArrayList();
				}
			}
		}
		return (gueltigeVorlagen === null) ? new ArrayList() : new ArrayList(gueltigeVorlagen);
	}

	/**
	 * Liefert alle Default-Vorlageparameter der Reportvorlage über deren Bezeichnung als Liste ohne Gruppenzuordnung.
	 * Achtung: Diese Methode darf nur für lesende Zugriffe verwendet werden!
	 *
	 * @return Liste der Default-Vorlageparameter der Reportvorlage (ggf. leer)
	 */
	public getDefaultVorlageparameterByVorlage(): List<ReportingReportvorlageParameter> {
		ReportingReportvorlage.mapsInitialisieren();
		const key1: string | null = ReportingReportvorlage.normalizeKeyInput(this.bezeichnung);
		const mapParam: JavaMap<string, ReportingReportvorlageParameter> | null = ReportingReportvorlage.MAP_PARAMETER.get(key1);
		return (mapParam === null) ? new ArrayList() : new ArrayList(mapParam.values());
	}

	/**
	 * Liefert alle Default-Vorlageparameter einer Parametergruppe aus der Reportvorlage.
	 * Achtung: Diese Methode darf nur für lesende Zugriffe verwendet werden!
	 *
	 * @param parametergruppeName der Name der Vorlageparametergruppe
	 *
	 * @return Liste der Default-Vorlageparameter der Gruppe (ggf. leer)
	 */
	public getDefaultVorlageparameterByGruppe(parametergruppeName: string): List<ReportingReportvorlageParameter> {
		ReportingReportvorlage.mapsInitialisieren();
		const key1: string | null = ReportingReportvorlage.normalizeKeyInput(this.bezeichnung);
		const key2: string | null = ReportingReportvorlage.normalizeKeyInput(parametergruppeName);
		const mapGruppen: JavaMap<string, ReportingReportvorlageParameterGruppe> | null = ReportingReportvorlage.MAP_PARAMETERGRUPPEN.get(key1);
		if (mapGruppen !== null) {
			const gruppe: ReportingReportvorlageParameterGruppe | null = mapGruppen.get(key2);
			if ((gruppe !== null) && (gruppe.reportvorlageParameter !== null)) {
				return gruppe.reportvorlageParameter;
			}
		}
		return new ArrayList<ReportingReportvorlageParameter>();
	}

	/**
	 * Liefert genau den gewünschten Default-Vorlageparameter der Reportvorlage.
	 * Achtung: Diese Methode darf nur für lesende Zugriffe verwendet werden!
	 *
	 * @param parameterName der Name des Vorlageparameters
	 *
	 * @return der Default-Vorlageparameter oder null, falls nicht vorhanden.
	 */
	public getDefaultVorlageparameter(parameterName: string): ReportingReportvorlageParameter | null {
		ReportingReportvorlage.mapsInitialisieren();
		const key1: string | null = ReportingReportvorlage.normalizeKeyInput(this.bezeichnung);
		const key3: string | null = ReportingReportvorlage.normalizeKeyInput(parameterName);
		const mapParam: JavaMap<string, ReportingReportvorlageParameter> | null = ReportingReportvorlage.MAP_PARAMETER.get(key1);
		return (mapParam !== null) ? mapParam.get(key3) : null;
	}

	/**
	 * Setzt den Wert eines Vorlageparameters innerhalb der ReportingParameter, die übergeben werden. Die Default-Werte der ENUM werden dadurch nicht angepasst.
	 *
	 * @param reportingParameter   das zu verändernde ReportingParameter-Objekt.
	 * @param vorlageparameterName der Name des zu suchenden Vorlageparameters
	 * @param wert                 der zu setzende Wert als String
	 */
	public setReportingParameterVorlageparameter(reportingParameter: ReportingParameter, vorlageparameterName: string, wert: string): void {
		for (const gruppe of reportingParameter.reportvorlageParameterGruppen) {
			if (gruppe.reportvorlageParameter === null) {
				continue;
			}
			for (const param of gruppe.reportvorlageParameter) {
				if ((param.name !== null) && JavaObject.equalsTranspiler(param.name, (vorlageparameterName))) {
					param.wert = wert;
					return;
				}
			}
		}
	}

	/**
	 * Liefert die Default-Vorlageparametergruppe der Reportvorlage über deren Namen.
	 * Achtung: Diese Methode darf nur für lesende Zugriffe verwendet werden!
	 *
	 * @param gruppenName der Name der Vorlageparametergruppe
	 *
	 * @return die Default-Vorlageparametergruppe oder null, falls nicht vorhanden.
	 */
	public getDefaultVorlageparametergruppeByName(gruppenName: string): ReportingReportvorlageParameterGruppe | null {
		ReportingReportvorlage.mapsInitialisieren();
		const key1: string | null = ReportingReportvorlage.normalizeKeyInput(this.bezeichnung);
		const key2: string | null = ReportingReportvorlage.normalizeKeyInput(gruppenName);
		const mapGruppen: JavaMap<string, ReportingReportvorlageParameterGruppe> | null = ReportingReportvorlage.MAP_PARAMETERGRUPPEN.get(key1);
		return (mapGruppen !== null) ? mapGruppen.get(key2) : null;
	}

	/**
	 *Initialisiert einmalig interne ListMaps/Indizes für schnelleren Zugriff.
	 */
	private static mapsInitialisieren(): void {
		if (ReportingReportvorlage.mapsInitialisiert) {
			return;
		}
		for (const reportvorlage of ReportingReportvorlage.values()) {
			ReportingReportvorlage.initialisiereParameterMaps(reportvorlage);
			ReportingReportvorlage.initialisiereNamensbestandteileIndex(reportvorlage);
		}
		ReportingReportvorlage.mapsInitialisiert = true;
	}

	/**
	 * Initialisiert die Maps für die Parameter und Parametergruppen einer Reportvorlage.
	 *
	 * @param reportvorlage die Reportvorlage
	 */
	private static initialisiereParameterMaps(reportvorlage: ReportingReportvorlage): void {
		const key1: string | null = ReportingReportvorlage.normalizeKeyInput(reportvorlage.bezeichnung);
		let mapParam: JavaMap<string, ReportingReportvorlageParameter> | null = ReportingReportvorlage.MAP_PARAMETER.get(key1);
		if (mapParam === null) {
			mapParam = new HashMap();
			ReportingReportvorlage.MAP_PARAMETER.put(key1, mapParam);
		}
		let mapGruppen: JavaMap<string, ReportingReportvorlageParameterGruppe> | null = ReportingReportvorlage.MAP_PARAMETERGRUPPEN.get(key1);
		if (mapGruppen === null) {
			mapGruppen = new HashMap();
			ReportingReportvorlage.MAP_PARAMETERGRUPPEN.put(key1, mapGruppen);
		}
		for (const gruppe of reportvorlage.reportingParameter.reportvorlageParameterGruppen) {
			if ((gruppe === null) || (gruppe.name === null) || (gruppe.reportvorlageParameter === null)) {
				continue;
			}
			const key2: string | null = ReportingReportvorlage.normalizeKeyInput(gruppe.name);
			mapGruppen.put(key2, gruppe);
			for (const parameter of gruppe.reportvorlageParameter) {
				if ((parameter === null) || (parameter.name === null)) {
					continue;
				}
				const key3: string | null = ReportingReportvorlage.normalizeKeyInput(parameter.name);
				mapParam.put(key3, parameter);
			}
		}
	}

	/**
	 * Initialisiert den Suchindex für die Bestandteile des ENUM-Namens einer Reportvorlage.
	 *
	 * @param reportvorlage die Reportvorlage
	 */
	private static initialisiereNamensbestandteileIndex(reportvorlage: ReportingReportvorlage): void {
		const enumNameBestandteile: Array<string | null> | null = reportvorlage.name().toLowerCase().split("_");
		for (const bestandteil of enumNameBestandteile) {
			if ((bestandteil === null) || JavaString.isBlank(bestandteil)) {
				continue;
			}
			let vorlagen: List<ReportingReportvorlage> | null = ReportingReportvorlage.MAP_NAMENSBESTANDTEILE_REPORTVORLAGEN.get(bestandteil);
			if (vorlagen === null) {
				vorlagen = new ArrayList();
				ReportingReportvorlage.MAP_NAMENSBESTANDTEILE_REPORTVORLAGEN.put(bestandteil, vorlagen);
			}
			vorlagen.add(reportvorlage);
		}
	}

	/**
	 * Erzeugt ein {@link ReportingParameter}-Objekt mit Standardwerten und setzt die aktuell definierten Vorlage-Parametergruppen.
	 *
	 * @param ausgabeformatOptionen                 Liste der erlaubten Ausgabeformate (IDs aus {@link ReportingAusgabeformat}).
	 * @param reportvorlageParameterGruppen         die definierten Vorlage-Parametergruppen der Reportvorlage
	 * @param sortierungDefinitionenGruppen         Liste mit den in der UI angebotenen Optionen für die Sortierung
	 * @param filterDefinitionenGruppen             Liste mit den in der UI angebotenen Optionen für die Filterung
	 * @param uiIstSichtbarEinzelausgabeHauptdaten  Legt fest, ob in der UI eine Option erscheinen soll, die die Einzelausgabe der Hauptdaten regelt.
	 * @param uiIstSichtbarEinzelausgabeDetaildaten Legt fest, ob in der UI eine Option erscheinen soll, die die Einzelausgabe der Detaildaten regelt.
	 * @param uiIstSichtbarDuplexdruck              Legt fest, ob in der UI eine Option erscheinen soll, die den Duplexdruck regelt.
	 *
	 * @return ein {@link ReportingParameter}-Objekt mit Standardwerten und gesetzten Gruppen
	 */
	private static erzeugeReportingParameter(ausgabeformatOptionen: List<number> | null, reportvorlageParameterGruppen: List<ReportingReportvorlageParameterGruppe> | null, sortierungDefinitionenGruppen: List<ReportingSortierungDefinitionGruppe> | null, filterDefinitionenGruppen: List<ReportingFilterDefinitionGruppe> | null, uiIstSichtbarEinzelausgabeHauptdaten: boolean, uiIstSichtbarEinzelausgabeDetaildaten: boolean, uiIstSichtbarDuplexdruck: boolean): ReportingParameter {
		const reportingParameter: ReportingParameter | null = new ReportingParameter();
		reportingParameter.ausgabeformatOptionen = new ArrayList(((ausgabeformatOptionen === null) || ausgabeformatOptionen.isEmpty()) ? ArrayList.of(ReportingAusgabeformat.PDF.getId()) : ausgabeformatOptionen);
		const standardausgabeoptionenGruppe: ReportingReportvorlageParameterGruppe | null = ReportingReportvorlage.erzeugeReportingvorlageParameterGruppe("Ausgabeoptionen", "", true, 3, Arrays.asList(ReportingReportvorlage.erzeugeVorlageParameter("einzelausgabeHauptdaten", "Einzelausgabe der Daten", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, uiIstSichtbarEinzelausgabeHauptdaten, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("einzelausgabeDetaildaten", "Einzelausgabe der Daten", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, uiIstSichtbarEinzelausgabeDetaildaten, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlage.erzeugeVorlageParameter("duplexdruck", "Duplexdruck", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, uiIstSichtbarDuplexdruck, ReportingUIKomponentenTyp.CHECKBOX, 1)));
		reportingParameter.reportvorlageParameterGruppen = new ArrayList((reportvorlageParameterGruppen === null) ? new ArrayList() : reportvorlageParameterGruppen);
		reportingParameter.reportvorlageParameterGruppen.add(standardausgabeoptionenGruppe);
		reportingParameter.sortierungDefinitionenGruppen = new ArrayList((sortierungDefinitionenGruppen === null) ? new ArrayList() : sortierungDefinitionenGruppen);
		reportingParameter.filterDefinitionenGruppen = new ArrayList((filterDefinitionenGruppen === null) ? new ArrayList() : filterDefinitionenGruppen);
		return reportingParameter;
	}

	/**
	 * Erstellt eine neue Reporting-Vorlage-Parametergruppe mit den angegebenen Parametern, mit UI-Parametern.
	 *
	 * @param name                            Der Titel der Gruppe. Darf nicht null sein.
	 * @param beschreibung                    Die Beschreibung der Gruppe. Darf nicht null sein.
	 * @param uiIstSichtbar                   Gibt an, ob die Gruppe in der UI sichtbar sein soll. Darf nicht null sein.
	 * @param uiAnzahlSpalten                 Die Anzahl der Grid-Spalten, die die Gruppe in der UI einnehmen soll.
	 * @param reportingReportvorlageParameter Eine Liste von ReportingvorlageParametern, die in der Gruppe enthalten sein sollen.
	 *
	 * @return Eine neue Instanz von ReportingUIGruppenDefinition.
	 */
	private static erzeugeReportingvorlageParameterGruppe(name: string, beschreibung: string, uiIstSichtbar: boolean, uiAnzahlSpalten: number, reportingReportvorlageParameter: List<ReportingReportvorlageParameter>): ReportingReportvorlageParameterGruppe {
		const reportingReportvorlageParameterGruppe: ReportingReportvorlageParameterGruppe | null = new ReportingReportvorlageParameterGruppe();
		reportingReportvorlageParameterGruppe.name = name;
		reportingReportvorlageParameterGruppe.beschreibung = beschreibung;
		reportingReportvorlageParameterGruppe.uiIstSichtbar = uiIstSichtbar;
		reportingReportvorlageParameterGruppe.uiAnzahlSpalten = uiAnzahlSpalten;
		reportingReportvorlageParameterGruppe.reportvorlageParameter = reportingReportvorlageParameter;
		return reportingReportvorlageParameterGruppe;
	}

	/**
	 * Erstellt einen neuen Vorlage-Parameter mit dem angegebenen Namen, Typ und Wert, mit UI-Parametern.
	 *
	 * @param name             Der Name des Vorlage-Parameters. Darf nicht null sein.
	 * @param bezeichnung      Die Bezeichnung des Vorlage-Parameters. Darf nicht null sein.
	 * @param typ              Der Typ des Vorlage-Parameters. Darf nicht null sein.
	 * @param wert             Der Wert des Vorlage-Parameters. Darf nicht null sein.
	 * @param uiIstSichtbar    Gibt an, ob der Parameter in der UI sichtbar sein soll. Darf nicht null sein.
	 * @param uiKomponentenTyp Der Typ der UI-Komponente (z.B. 'checkbox', 'input', 'select', 'textarea', 'numberPicker', 'datePicker').
	 * @param uiAnzahlSpalten  Die Anzahl der Grid-Spalten, die der Parameter in der UI einnehmen soll.
	 *
	 * @return Ein neues Objekt der Klasse {@link ReportingReportvorlageParameter}, das den angegebenen Namen, Typ und Wert enthält.
	 */
	private static erzeugeVorlageParameter(name: string, bezeichnung: string, typ: ReportingReportvorlageParameterTyp, wert: string, uiIstSichtbar: boolean, uiKomponentenTyp: ReportingUIKomponentenTyp, uiAnzahlSpalten: number): ReportingReportvorlageParameter {
		const reportingReportVorlageParameter: ReportingReportvorlageParameter | null = new ReportingReportvorlageParameter();
		reportingReportVorlageParameter.name = name;
		reportingReportVorlageParameter.bezeichnung = bezeichnung;
		reportingReportVorlageParameter.typ = typ.getId();
		reportingReportVorlageParameter.wert = wert;
		reportingReportVorlageParameter.uiIstSichtbar = uiIstSichtbar;
		reportingReportVorlageParameter.uiKomponentenTyp = uiKomponentenTyp.getId();
		reportingReportVorlageParameter.uiAnzahlSpalten = uiAnzahlSpalten;
		return reportingReportVorlageParameter;
	}

	/**
	 * Erstellt eine neue Sortierungs-Definitionsgruppe.
	 *
	 * @param bezeichnung                    Die Bezeichnung der Gruppe (UI-Text).
	 * @param typ                            Der Typname des zu sortierenden Reporting-Datentyps.
	 * @param uiIstSichtbar                  Gibt an, ob die Gruppe in der UI sichtbar sein soll.
	 * @param sortierungDefinitionenOptionen Die Liste der Sortierungsdefinitionen, die in dieser Gruppe als Optionen zur Verfügung stehen.
	 *
	 * @return Eine neue Instanz von {@link ReportingSortierungDefinitionGruppe}.
	 */
	private static erzeugeSortierungDefinitionGruppe(bezeichnung: string, typ: string, uiIstSichtbar: boolean, sortierungDefinitionenOptionen: List<ReportingSortierungDefinition>): ReportingSortierungDefinitionGruppe {
		const gruppe: ReportingSortierungDefinitionGruppe | null = new ReportingSortierungDefinitionGruppe();
		gruppe.bezeichnung = bezeichnung;
		gruppe.typ = typ;
		gruppe.uiIstSichtbar = uiIstSichtbar;
		gruppe.sortierungDefinitionenOptionen = new ArrayList(sortierungDefinitionenOptionen);
		return gruppe;
	}

	/**
	 * Erstellt eine neue Filter-Definitionsgruppe.
	 *
	 * @param bezeichnung                Die Bezeichnung der Gruppe (UI-Text).
	 * @param typ                        Der Typname des zu filternden Reporting-Datentyps.
	 * @param uiIstSichtbar              Gibt an, ob die Gruppe in der UI sichtbar sein soll.
	 * @param uiIstMultiselect           Gibt an, ob mehrere Filterdefinitionen ausgewählt werden können.
	 * @param multiselectVerknuepfung    Die Verknüpfungsart bei Mehrfachauswahl (AND/OR).
	 * @param filterDefinitionenOptionen Die Liste der Filterdefinitionen, die in dieser Gruppe als Optionen zur Verfügung stehen.
	 *
	 * @return Eine neue Instanz von {@link ReportingFilterDefinitionGruppe}.
	 */
	private static erzeugeFilterDefinitionGruppe(bezeichnung: string, typ: string, uiIstSichtbar: boolean, uiIstMultiselect: boolean, multiselectVerknuepfung: ReportingFilterVerknuepfung, filterDefinitionenOptionen: List<ReportingFilterDefinition>): ReportingFilterDefinitionGruppe {
		const gruppe: ReportingFilterDefinitionGruppe | null = new ReportingFilterDefinitionGruppe();
		gruppe.bezeichnung = bezeichnung;
		gruppe.typ = typ;
		gruppe.uiIstSichtbar = uiIstSichtbar;
		gruppe.uiIstMultiselect = uiIstMultiselect;
		gruppe.multiselectVerknuepfung = multiselectVerknuepfung.getId();
		gruppe.filterDefinitionenOptionen = new ArrayList(filterDefinitionenOptionen);
		return gruppe;
	}

	/**
	 * Normalisiert den angegebenen String, indem alle Leerzeichen entfernt und die Zeichen in Kleinbuchstaben umgewandelt werden.
	 *
	 * @param input Der String, der normalisiert werden soll.
	 *
	 * @return Der normalisierte String.
	 */
	private static normalizeKeyInput(input: string | null): string {
		return (input === null) ? "" : input.trim().toLowerCase();
	}

	/**
	 * Erzeugt eine tiefe Kopie des ReportingParameter-Objekts inkl. aller Unterlisten und DTOs.
	 * Da die ReportingParameter als DTO konzipiert wurden und transpiliert werden, können dort keine Methoden implementiert werden und es wird hier auf
	 * Reflection verzichtet.
	 *
	 * @param source Ein ReportingParamater-Objekt als Quelle, die kopiert werden soll.
	 *
	 * @return Eine tiefe Kopie des ReportingParameter-Objekts, das unabhängig von der ENUM genutzt werden kann.
	 */
	private static cloneReportingParameter(source: ReportingParameter): ReportingParameter {
		const copy: ReportingParameter | null = new ReportingParameter();
		copy.idSchuljahresabschnitt = source.idSchuljahresabschnitt;
		copy.ausgabeformat = source.ausgabeformat;
		copy.reportvorlage = source.reportvorlage;
		copy.idHauptdatenObjekt = source.idHauptdatenObjekt;
		copy.ausgabeformatOptionen.addAll(source.ausgabeformatOptionen);
		copy.idsHauptdaten.addAll(source.idsHauptdaten);
		copy.idsDetaildaten.addAll(source.idsDetaildaten);
		if (source.eMailDaten !== null) {
			copy.eMailDaten = new ReportingEMailDaten();
			copy.eMailDaten.empfaengerTyp = source.eMailDaten.empfaengerTyp;
			copy.eMailDaten.istPrivateEmailAlternative = source.eMailDaten.istPrivateEmailAlternative;
			copy.eMailDaten.betreff = source.eMailDaten.betreff;
			copy.eMailDaten.text = source.eMailDaten.text;
		} else {
			copy.eMailDaten = null;
		}
		copy.reportvorlageParameterGruppen.addAll(ReportingReportvorlage.cloneVorlageParameterGruppen(source.reportvorlageParameterGruppen));
		copy.sortierungDefinitionenGruppen.addAll(ReportingReportvorlage.cloneSortierungDefinitionGruppen(source.sortierungDefinitionenGruppen));
		copy.filterDefinitionenGruppen.addAll(ReportingReportvorlage.cloneFilterDefinitionGruppen(source.filterDefinitionenGruppen));
		return copy;
	}

	private static cloneVorlageParameterGruppen(source: List<ReportingReportvorlageParameterGruppe> | null): List<ReportingReportvorlageParameterGruppe> {
		const result: List<ReportingReportvorlageParameterGruppe> | null = new ArrayList<ReportingReportvorlageParameterGruppe>();
		if ((source === null) || source.isEmpty()) {
			return result;
		}
		for (const vpg of source) {
			if (vpg === null) {
				continue;
			}
			const vpgCopy: ReportingReportvorlageParameterGruppe | null = new ReportingReportvorlageParameterGruppe();
			vpgCopy.name = vpg.name;
			vpgCopy.beschreibung = vpg.beschreibung;
			vpgCopy.uiIstSichtbar = vpg.uiIstSichtbar;
			vpgCopy.uiAnzahlSpalten = vpg.uiAnzahlSpalten;
			if (vpg.reportvorlageParameter !== null) {
				vpgCopy.reportvorlageParameter.addAll(ReportingReportvorlage.cloneVorlageParameter(vpg.reportvorlageParameter));
			}
			result.add(vpgCopy);
		}
		return result;
	}

	private static cloneVorlageParameter(source: List<ReportingReportvorlageParameter> | null): List<ReportingReportvorlageParameter> {
		const result: List<ReportingReportvorlageParameter> | null = new ArrayList<ReportingReportvorlageParameter>();
		if ((source === null) || source.isEmpty()) {
			return result;
		}
		for (const vp of source) {
			if (vp === null) {
				continue;
			}
			const vpCopy: ReportingReportvorlageParameter | null = new ReportingReportvorlageParameter();
			vpCopy.name = vp.name;
			vpCopy.bezeichnung = vp.bezeichnung;
			vpCopy.typ = vp.typ;
			vpCopy.wert = vp.wert;
			vpCopy.uiIstSichtbar = vp.uiIstSichtbar;
			vpCopy.uiKomponentenTyp = vp.uiKomponentenTyp;
			vpCopy.uiAnzahlSpalten = vp.uiAnzahlSpalten;
			result.add(vpCopy);
		}
		return result;
	}

	private static cloneSortierungDefinitionGruppen(source: List<ReportingSortierungDefinitionGruppe> | null): List<ReportingSortierungDefinitionGruppe> {
		const result: List<ReportingSortierungDefinitionGruppe> | null = new ArrayList<ReportingSortierungDefinitionGruppe>();
		if (source === null) {
			return result;
		}
		for (const sdg of source) {
			if (sdg.sortierungDefinitionenOptionen === null) {
				continue;
			}
			const sdgCopy: ReportingSortierungDefinitionGruppe | null = new ReportingSortierungDefinitionGruppe();
			sdgCopy.bezeichnung = sdg.bezeichnung;
			sdgCopy.typ = sdg.typ;
			sdgCopy.uiIstSichtbar = sdg.uiIstSichtbar;
			if (sdg.sortierungDefinitionenOptionen !== null) {
				sdgCopy.sortierungDefinitionenOptionen.addAll(ReportingReportvorlage.cloneSortierungDefinitionen(sdg.sortierungDefinitionenOptionen));
			}
			result.add(sdgCopy);
		}
		return result;
	}

	private static cloneSortierungDefinitionen(source: List<ReportingSortierungDefinition> | null): List<ReportingSortierungDefinition> {
		const result: List<ReportingSortierungDefinition> | null = new ArrayList<ReportingSortierungDefinition>();
		if ((source === null) || source.isEmpty()) {
			return result;
		}
		for (const sd of source) {
			if (sd === null) {
				continue;
			}
			const sdCopy: ReportingSortierungDefinition | null = new ReportingSortierungDefinition();
			sdCopy.bezeichnung = sd.bezeichnung;
			sdCopy.typ = sd.typ;
			sdCopy.verwendeStandardsortierung = sd.verwendeStandardsortierung;
			sdCopy.attribute.addAll(sd.attribute);
			result.add(sdCopy);
		}
		return result;
	}

	private static cloneFilterDefinitionGruppen(source: List<ReportingFilterDefinitionGruppe> | null): List<ReportingFilterDefinitionGruppe> {
		const result: List<ReportingFilterDefinitionGruppe> | null = new ArrayList<ReportingFilterDefinitionGruppe>();
		if ((source === null) || source.isEmpty()) {
			return result;
		}
		for (const fdg of source) {
			if (fdg === null) {
				continue;
			}
			const fdgCopy: ReportingFilterDefinitionGruppe | null = new ReportingFilterDefinitionGruppe();
			fdgCopy.bezeichnung = fdg.bezeichnung;
			fdgCopy.typ = fdg.typ;
			fdgCopy.uiIstSichtbar = fdg.uiIstSichtbar;
			fdgCopy.uiIstMultiselect = fdg.uiIstMultiselect;
			fdgCopy.multiselectVerknuepfung = fdg.multiselectVerknuepfung;
			if (fdg.filterDefinitionenOptionen !== null) {
				fdgCopy.filterDefinitionenOptionen.addAll(ReportingReportvorlage.cloneFilterDefinitionen(fdg.filterDefinitionenOptionen));
			}
			result.add(fdgCopy);
		}
		return result;
	}

	private static cloneFilterDefinitionen(source: List<ReportingFilterDefinition> | null): List<ReportingFilterDefinition> {
		const result: List<ReportingFilterDefinition> | null = new ArrayList<ReportingFilterDefinition>();
		if ((source === null) || source.isEmpty()) {
			return result;
		}
		for (const fd of source) {
			if (fd === null) {
				continue;
			}
			const fdCopy: ReportingFilterDefinition | null = new ReportingFilterDefinition();
			fdCopy.bezeichnung = fd.bezeichnung;
			fdCopy.typ = fd.typ;
			fdCopy.kriterien.addAll(ReportingReportvorlage.cloneFilterKriterien(fd.kriterien));
			result.add(fdCopy);
		}
		return result;
	}

	private static cloneFilterKriterien(source: List<ReportingFilterKriterium> | null): List<ReportingFilterKriterium> {
		const result: List<ReportingFilterKriterium> | null = new ArrayList<ReportingFilterKriterium>();
		if ((source === null) || source.isEmpty()) {
			return result;
		}
		for (const k of source) {
			if (k === null) {
				continue;
			}
			const kCopy: ReportingFilterKriterium | null = new ReportingFilterKriterium();
			kCopy.verknuepfung = k.verknuepfung;
			kCopy.nicht = k.nicht;
			if (k.eintraege !== null) {
				for (const e of k.eintraege) {
					const eCopy: ReportingFilterEintrag | null = new ReportingFilterEintrag();
					eCopy.attribut = e.attribut;
					eCopy.operation = e.operation;
					eCopy.werte.addAll(e.werte);
					kCopy.eintraege.add(eCopy);
				}
			}
			if (k.unterkriterien !== null) {
				kCopy.unterkriterien.addAll(ReportingReportvorlage.cloneFilterKriterien(k.unterkriterien));
			}
			result.add(kCopy);
		}
		return result;
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
