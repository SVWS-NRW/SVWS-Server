package de.svws_nrw.core.types.reporting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.svws_nrw.core.data.reporting.ReportingEMailDaten;
import de.svws_nrw.core.data.reporting.ReportingFilterDefinition;
import de.svws_nrw.core.data.reporting.ReportingFilterDefinitionGruppe;
import de.svws_nrw.core.data.reporting.ReportingFilterEintrag;
import de.svws_nrw.core.data.reporting.ReportingFilterKriterium;
import de.svws_nrw.core.data.reporting.ReportingParameter;
import de.svws_nrw.core.data.reporting.ReportingReportvorlageParameterGruppe;
import de.svws_nrw.core.data.reporting.ReportingReportvorlageParameter;
import de.svws_nrw.core.data.reporting.ReportingSortierungDefinition;
import de.svws_nrw.core.data.reporting.ReportingSortierungDefinitionGruppe;
import de.svws_nrw.core.utils.reporting.ReportingFilterDefinitionFactory;
import de.svws_nrw.core.utils.reporting.ReportingSortierungDefinitionFactory;
import jakarta.validation.constraints.NotNull;

/**
 * Eine ENUM der integrierten Report-Vorlagen des SVWS-Servers. Im Rahmen des Reportings werden auf Basis dieses CoreTyps Template-Definitionen vorgenommen.
 * Wichtig:
 * - Die Bezeichnung der Vorlagen muss eindeutig sein und darf keine Leerzeichen enthalten. Die Bezeichnung darf nur aus Buchstaben, Ziffern, Bindestrichen und
 * Unterstrichen
 * bestehen.
 * - Die Parametergruppen, aber auch die Parameter müssen eindeutig sein pro ENUM-Eintrag und dürfen keine Leerzeichen enthalten. Die Parametergruppen und die
 * Parameter dürfen nur aus Buchstaben, Ziffern, Bindestrichen und Unterstrichen bestehen.
 *
 * Hinweis: Es ist nicht ausreichend, eine neue Vorlage nur hier einzubinden. Es muss in jedem Fall auch eine neue HTML-Template-Definition im
 * Reporting-Modul erstellt werden.
 *
 * Anmerkung: Die Benennung der Vorlagen erfolgt nach dem Schema Hauptdaten_v_Detaildaten. Bei der Report-Generierung erfolgt in Teilen ein entsprechendes
 * Füllen der Datenkontexte anhand der Benennung.
 */
// SONARQUBE WARNUNG: Es sollen Konstanten für wiederkehrende Strings definiert werden. Das ist in einer ENUM nicht ohne Komplexitätserhöhung möglich.
@SuppressWarnings("java:S1192")
public enum ReportingReportvorlage {

	/** Report-Vorlage: GOSt - Klausurplanung - Klausurtermine-Kurse */
	GOST_KLAUSURPLANUNG_V_KLAUSURTERMINE_MIT_KURSEN("GostKlausurplanung-KlausurtermineMitKursen",
			erzeugeReportingParameter(List.of(ReportingAusgabeformat.PDF.getId()), List.of(
					erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "", true, 1, Arrays.asList(
							erzeugeVorlageParameter("mitKursklausuren", "mit Kursklausuren", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitNachschreibern", "mit Nachschreibern", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitKlausurschreiberNamen", "mit Namen der Klausurschreiber", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)
					))
			), new ArrayList<>(), new ArrayList<>(), false, false, true)
	),

	/** Report-Vorlage: GOSt - Klausurplanung - Schueler-Klausuren */
	GOST_KLAUSURPLANUNG_V_SCHUELER_MIT_KLAUSUREN("GostKlausurplanung-SchuelerMitKlausuren",
			erzeugeReportingParameter(List.of(ReportingAusgabeformat.PDF.getId()), new ArrayList<>(),
					new ArrayList<>(), new ArrayList<>(), false, true, true)
	),

	/** Report-Vorlage: GOSt - Kursplanung - Kurs-Kurschüler */
	GOST_KURSPLANUNG_V_KURS_MIT_KURSSCHUELERN("GostKursplanung-KursMitKursschuelern",
			erzeugeReportingParameter(List.of(ReportingAusgabeformat.PDF.getId()), new ArrayList<>(), new ArrayList<>(),
					new ArrayList<>(), false, false, true)
	),

	/** Report-Vorlage: GOSt - Kursplanung - Kurse-Statistikwerte */
	GOST_KURSPLANUNG_V_KURSE_MIT_STATISTIKWERTEN("GostKursplanung-KurseMitStatistikwerten",
			erzeugeReportingParameter(List.of(ReportingAusgabeformat.PDF.getId()), new ArrayList<>(),
					new ArrayList<>(), new ArrayList<>(), false, false, true)
	),

	/** Report-Vorlage: GOSt - Kursplanung - Schüler-Kurse */
	GOST_KURSPLANUNG_V_SCHUELER_MIT_KURSEN("GostKursplanung-SchuelerMitKursen",
			erzeugeReportingParameter(List.of(ReportingAusgabeformat.PDF.getId()), new ArrayList<>(), new ArrayList<>(),
					new ArrayList<>(), false, false, true)
	),

	/** Report-Vorlage: GOSt - Kursplanung - Schüler-Schienen-Kurse */
	GOST_KURSPLANUNG_V_SCHUELER_MIT_SCHIENEN_KURSEN("GostKursplanung-SchuelerMitSchienenKursen",
			erzeugeReportingParameter(List.of(ReportingAusgabeformat.PDF.getId()), new ArrayList<>(),
					new ArrayList<>(), new ArrayList<>(), false, false, true)
	),

	/** Report-Vorlage: GOSt - Laufbahnplanung - Abiturjahrgang - Fachwahlstatistiken */
	GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG_V_FACHWAHLSTATISTIKEN("GostLaufbahnplanung-Abiturjahrgang-Fachwahlstatistiken",
			erzeugeReportingParameter(List.of(ReportingAusgabeformat.PDF.getId()), new ArrayList<>(),
					new ArrayList<>(), new ArrayList<>(), false, false, true)
	),

	/** Report-Vorlage: Klasse - Liste - Schüler - Kontaktdaten - Erzieher */
	KLASSEN_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER("Klasse-Liste-Schueler-Kontaktdaten-Erzieher",
			erzeugeReportingParameter(List.of(ReportingAusgabeformat.PDF.getId(), ReportingAusgabeformat.EMAIL.getId()), List.of(
					erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "", true, 3, Arrays.asList(
							erzeugeVorlageParameter("nurSchuelerRufname", "nur Rufname", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false,
									true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitSchuelerGeschlecht", "mit Geschlecht", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitSchuelerGebDat", "mit Geburtsdatum", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitSchuelerStaat", "mit Staatsangehörigkeit", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitSchuelerAnschrift", "mit Anschrift", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitSchuelerTelefonPrivat", "mit Telefon (privat)", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitSchuelerEmailSchule", "mit E-Mail (Schule)", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitSchuelerEmailPrivat", "mit E-Mail (privat)", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitSpalteSchuelerTelefonKontakte", "mit Telefonkontakten",
									ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitErzieher", "mit Erziehern", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false,
									true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitErzieherAnschrift", "mit Erzieher-Anschrift", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitErzieherEmailPrivat", "mit Erzieher-E-Mail", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)
					))
			), new ArrayList<>(), new ArrayList<>(), true, false, true)
	),

	/** Report-Vorlage: Klasse - Liste - Schüler - Leistungsdaten */
	KLASSEN_V_LISTE_SCHUELER_LEISTUNGSDATEN("Klasse-Liste-Schueler-Leistungsdaten",
			erzeugeReportingParameter(List.of(ReportingAusgabeformat.PDF.getId()),
					List.of(erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "", true, 2, Arrays.asList(
							erzeugeVorlageParameter("mitPunktenStattNoten", "Punkte statt Noten ausgeben", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitQuartalsnote", "Quartalsnoten statt Noten ausgeben", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitIndividuellerKursart", "mit individueller Kursart", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitZuweisung", "mit Zuweisungen", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false,
									true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitGesamtfehlstunden", "mit Gesamtfehlstunden", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + true, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitFachbezogenenFehlstunden", "mit fachbezogenen Fehlstunden", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitFachbezogenenBemerkungen", "mit fachbezogenen Bemerkungen", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitLernentwicklung", "mit Angabe zur Lernentwicklung", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitFoerderschwerpunkt", "mit Angaben zum Förderschwerpunkt", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitASVBemerkung", "mit ASV-Bemerkungen", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitAUEBemerkung", "mit AUE-Bemerkungen", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitZeugnisbemerkung", "mit Zeugnisbemerkungen", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitSchulformempfehlung", "mit Empfehlung der Schulform", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitVersetzungAbschluss", "mit Versetzung und Abschluss", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitVersetzungsentscheidung", "mit Text zur Versetzungsentscheidung",
									ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1))
					)),
					List.of(erzeugeSortierungDefinitionGruppe("Fachsortierung", "ReportingFach", true,
							ReportingSortierungDefinitionFactory.definitionen(
									ReportingSortierungDefinitionFactory.standard(
											"Standardsortierung der Fächer", "ReportingFach"),
									ReportingSortierungDefinitionFactory.definition(
											"GOSt-Sortierung der Fächer", "ReportingFach", false, List.of("gostSortierung")),
									ReportingSortierungDefinitionFactory.definition(
											"Sortierung nach Fachkürzeln", "ReportingFach", false, List.of("kuerzel")))
					)),
					List.of(erzeugeFilterDefinitionGruppe("Fachfilter", "ReportingFach", true, true,
							ReportingFilterVerknuepfung.AND,
							ReportingFilterDefinitionFactory.definitionen(
									ReportingFilterDefinitionFactory.definition(
											"Nur Fächer für Zeugnisrelevanz", "ReportingFach",
											ReportingFilterDefinitionFactory.and(
													ReportingFilterDefinitionFactory.eq("aufZeugnis", "true")
											)
									),
									ReportingFilterDefinitionFactory.definition(
											"Nur Fächer mit Prüfungsordnungsrelevanz", "ReportingFach",
											ReportingFilterDefinitionFactory.and(
													ReportingFilterDefinitionFactory.eq("istPruefungsordnungsRelevant", "true")
											)
									))
					)),
					true, false, true)
	),

	/** Report-Vorlage: Kurs - Liste - Schüler - Kontaktdaten - Erzieher */
	KURSE_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER("Kurs-Liste-Schueler-Kontaktdaten-Erzieher",
			erzeugeReportingParameter(List.of(ReportingAusgabeformat.PDF.getId(), ReportingAusgabeformat.EMAIL.getId()), List.of(
					erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "", true, 3, Arrays.asList(
							erzeugeVorlageParameter("mitSchuelerKlasse", "mit Klasse", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false,
									true, ReportingUIKomponentenTyp.CHECKBOX, 3),
							erzeugeVorlageParameter("nurSchuelerRufname", "nur Rufname", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false,
									true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitSchuelerGeschlecht", "mit Geschlecht", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitSchuelerGebDat", "mit Geburtsdatum", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitSchuelerStaat", "mit Staatsangehörigkeit", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitSchuelerAnschrift", "mit Anschrift", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitSchuelerTelefonPrivat", "mit Telefon (privat)", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitSchuelerEmailSchule", "mit E-Mail (Schule)", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitSchuelerEmailPrivat", "mit E-Mail (privat)", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitSpalteSchuelerTelefonKontakte", "mit Telefonkontakten",
									ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitErzieher", "mit Erziehern", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false,
									true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitErzieherAnschrift", "mit Erzieher-Anschrift", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitErzieherEmailPrivat", "mit Erzieher-E-Mail", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)
					))
			), new ArrayList<>(), new ArrayList<>(), true, false, true)
	),

	/** Report-Vorlage: Kurs - Liste - Schüler - Leistungsdaten */
	KURSE_V_LISTE_SCHUELER_LEISTUNGSDATEN("Kurs-Liste-Schueler-Leistungsdaten",
			erzeugeReportingParameter(List.of(ReportingAusgabeformat.PDF.getId()), List.of(
					erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "", true, 1, Arrays.asList(
							erzeugeVorlageParameter("mitPunktenStattNoten", "Punkte statt Noten ausgeben", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitBemerkungen", "mit fachbezogenen Bemerkungen", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)
					))
			), new ArrayList<>(), new ArrayList<>(), true, false, true)
	),

	/** Report-Vorlage: Lehrer - Liste - Schüler - Leistungsdaten */
	LEHRER_V_LISTE_SCHUELER_LEISTUNGSDATEN("Lehrer-Liste-Schueler-Leistungsdaten",
			erzeugeReportingParameter(List.of(ReportingAusgabeformat.PDF.getId()), List.of(
					erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "", true, 2, Arrays.asList(
							erzeugeVorlageParameter("mitKlassenunterricht", "mit Klassenunterricht", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + true, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitKursunterricht", "mit Kursunterricht", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + true, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitPunktenStattNoten", "Punkte statt Noten ausgeben", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitBemerkungen", "mit fachbezogenen Bemerkungen", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)
					))
			), new ArrayList<>(), new ArrayList<>(), true, false, true)
	),

	/** Report-Vorlage: Lehrer - Stammdaten - Liste */
	LEHRER_V_STAMMDATENLISTE("Lehrer-Stammdatenliste",
			erzeugeReportingParameter(List.of(ReportingAusgabeformat.PDF.getId()), new ArrayList<>(), new ArrayList<>(),
					new ArrayList<>(), true, false, true)
	),

	/** Report-Vorlage: GOSt - Abitur - APO - Anlage 12 (Abiturzeugnis) - DIN-A4 */
	SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A4("Schueler-GostAbiturApoAnlage12-A4",
			erzeugeReportingParameter(List.of(ReportingAusgabeformat.PDF.getId()), List.of(
					erzeugeReportingvorlageParameterGruppe("Unterschriftenoptionen", "", true, 1, Arrays.asList(
							erzeugeVorlageParameter("mitPersoenlichenUnterschriften", "mit persönlichen Unterschriften",
									ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true,
									ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitZweiterBeratungslehrerUnterschrift", "mit Unterschrift 2. Beratungslehrer",
									ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true,
									ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("textZAAVorsitzUnterschrift", "Unterschrift ZAA-Vorsitz", ReportingReportvorlageParameterTyp.STRING, "",
									true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("textZAAVorsitzUnterschriftBezeichnung", "Bezeichnung ZAA-Vorsitz",
									ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1),
							erzeugeVorlageParameter("textSchulleitungUnterschrift", "Unterschrift Schulleitung", ReportingReportvorlageParameterTyp.STRING, "",
									true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("textSchulleitungUnterschriftBezeichnung", "Bezeichnung Schulleitung",
									ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1),
							erzeugeVorlageParameter("textSchultraegerUnterschrift", "Unterschrift Schulträger", ReportingReportvorlageParameterTyp.STRING, "",
									true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("textSchultraegerUnterschriftBezeichnung", "Bezeichnung Schulträger",
									ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1),
							erzeugeVorlageParameter("textBeratungslehrerUnterschrift", "Unterschrift Beratungslehrer",
									ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1),
							erzeugeVorlageParameter("textBeratungslehrerUnterschriftBezeichnung", "Bezeichnung Beratungslehrer",
									ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1)
					))
			), new ArrayList<>(), new ArrayList<>(), true, false, true)
	),

	/** Report-Vorlage: GOSt - Abitur - APO - Anlage 12 (Abiturzeugnis) - DIN-A3 */
	SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A3("Schueler-GostAbiturApoAnlage12-A3",
			erzeugeReportingParameter(List.of(ReportingAusgabeformat.PDF.getId()), List.of(
					erzeugeReportingvorlageParameterGruppe("Unterschriftenoptionen", "", true, 1, Arrays.asList(
							erzeugeVorlageParameter("mitPersoenlichenUnterschriften", "mit persönlichen Unterschriften",
									ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true,
									ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitZweiterBeratungslehrerUnterschrift", "mit Unterschrift 2. Beratungslehrer",
									ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true,
									ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("textZAAVorsitzUnterschrift", "Unterschrift ZAA-Vorsitz", ReportingReportvorlageParameterTyp.STRING, "",
									true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("textZAAVorsitzUnterschriftBezeichnung", "Bezeichnung ZAA-Vorsitz",
									ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1),
							erzeugeVorlageParameter("textSchulleitungUnterschrift", "Unterschrift Schulleitung", ReportingReportvorlageParameterTyp.STRING, "",
									true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("textSchulleitungUnterschriftBezeichnung", "Bezeichnung Schulleitung",
									ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1),
							erzeugeVorlageParameter("textSchultraegerUnterschrift", "Unterschrift Schulträger", ReportingReportvorlageParameterTyp.STRING, "",
									true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("textSchultraegerUnterschriftBezeichnung", "Bezeichnung Schulträger",
									ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1),
							erzeugeVorlageParameter("textBeratungslehrerUnterschrift", "Unterschrift Beratungslehrer",
									ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1),
							erzeugeVorlageParameter("textBeratungslehrerUnterschriftBezeichnung", "Bezeichnung Beratungslehrer",
									ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1)
					))
			), new ArrayList<>(), new ArrayList<>(), true, false, true)
	),

	/** Report-Vorlage: GOSt - Laufbahnplanung - Ergebnisübersicht */
	SCHUELER_V_GOST_LAUFBAHNPLANUNG_ERGEBNISUEBERSICHT("Schueler-GostLaufbahnplanungErgebnisuebersicht",
			erzeugeReportingParameter(List.of(ReportingAusgabeformat.PDF.getId()), List.of(
					erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "", true, 1, Arrays.asList(
							erzeugeVorlageParameter("mitFehlernKommentaren", "mit Fehlern/Kommentaren", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitHinweisen", "mit Hinweisen", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false,
									true, ReportingUIKomponentenTyp.CHECKBOX, 1)
					))
			), new ArrayList<>(), new ArrayList<>(), true, false, true)
	),

	/** Report-Vorlage: GOSt - Laufbahnplanung - Wahlbogen */
	SCHUELER_V_GOST_LAUFBAHNPLANUNG_WAHLBOGEN("Schueler-GostLaufbahnplanungWahlbogen",
			erzeugeReportingParameter(List.of(ReportingAusgabeformat.PDF.getId()), List.of(
					erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "", true, 1, Arrays.asList(
							erzeugeVorlageParameter("nurBelegteFaecher", "nur belegte Fächer", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)
					))
			), new ArrayList<>(), new ArrayList<>(), true, false, true)
	),

	/** Report-Vorlage: Schüler - Schulbescheinigung */
	SCHUELER_V_SCHULBESCHEINIGUNG("Schueler-Schulbescheinigung",
			erzeugeReportingParameter(List.of(ReportingAusgabeformat.PDF.getId()), List.of(
					erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "", true, 3, Arrays.asList(
							erzeugeVorlageParameter("fuerErzieher", "für Erzieher", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false,
									true, ReportingUIKomponentenTyp.CHECKBOX, 3),
							erzeugeVorlageParameter("mitSchullogo", "mit Schullogo", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false,
									true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitBildBriefkopf", "mit Bild im Briefkopf", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 2),
							erzeugeVorlageParameter("keineAnschrift", "ohne Anschrift", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false,
									true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("keinInfoblock", "ohne Infoblock", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false,
									true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("keineUnterschrift", "ohne Unterschrift", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)
					))
			), new ArrayList<>(), new ArrayList<>(), true, false, true)
	),

	/** Report-Vorlage: Schüler - Liste - Kontaktdaten - Erzieher */
	SCHUELER_V_LISTE_KONTAKTDATENERZIEHER("Schueler-Liste-Kontaktdaten-Erzieher",
			erzeugeReportingParameter(List.of(ReportingAusgabeformat.PDF.getId()), List.of(
					erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "", true, 3, Arrays.asList(
							erzeugeVorlageParameter("mitSchuelerKlasse", "mit Klasse", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false,
									true, ReportingUIKomponentenTyp.CHECKBOX, 3),
							erzeugeVorlageParameter("nurSchuelerRufname", "nur Rufname", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false,
									true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitSchuelerGeschlecht", "mit Geschlecht", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitSchuelerGebDat", "mit Geburtsdatum", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitSchuelerStaat", "mit Staatsangehörigkeit", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitSchuelerAnschrift", "mit Anschrift", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitSchuelerTelefonPrivat", "mit Telefon (privat)", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitSchuelerEmailSchule", "mit E-Mail (Schule)", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitSchuelerEmailPrivat", "mit E-Mail (privat)", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitSpalteSchuelerTelefonKontakte", "mit Telefonkontakten", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitErzieher", "mit Erziehern", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false,
									true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitErzieherAnschrift", "mit Erzieher-Anschrift", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitErzieherEmailPrivat", "mit Erzieher-E-Mail", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1))
					)),
					List.of(erzeugeSortierungDefinitionGruppe("Schülersortierung", "ReportingSchueler", true,
							ReportingSortierungDefinitionFactory.definitionen(
									ReportingSortierungDefinitionFactory.standard(
											"Standardsortierung der Schüler", "ReportingSchueler"),
									ReportingSortierungDefinitionFactory.definition(
											"Sortierung nach Klasse, Name, Vorname", "ReportingSchueler", false, List.of("Klasse, Nachname, Vorname, "
													+ "Vornamen")))
					)),
					new ArrayList<>(), false, false, true)
	),

	/** Report-Vorlage: Stundenplanung - Fach - Stundenplan */
	STUNDENPLANUNG_V_FACH_STUNDENPLAN("Stundenplanung-FachStundenplan",
			erzeugeReportingParameter(List.of(ReportingAusgabeformat.PDF.getId()), List.of(
					erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "", true, 2, Arrays.asList(
							erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false,
									true, ReportingUIKomponentenTyp.CHECKBOX, 1)
					))
			), new ArrayList<>(), new ArrayList<>(), false, true, true)
	),

	/** Report-Vorlage: Stundenplanung - Klasse - Stundenplan */
	STUNDENPLANUNG_V_KLASSEN_STUNDENPLAN("Stundenplanung-KlassenStundenplan",
			erzeugeReportingParameter(List.of(ReportingAusgabeformat.PDF.getId(), ReportingAusgabeformat.EMAIL.getId()),
					List.of(
							erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "", true, 2, Arrays.asList(
									erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false,
											true, ReportingUIKomponentenTyp.CHECKBOX, 1),
									erzeugeVorlageParameter("mitFachStattKursbezeichnung", "Fach statt Kursbezeichnung",
											ReportingReportvorlageParameterTyp.BOOLEAN,
											"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
									erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung", "Fachkürzel statt Fachbezeichnung",
											ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true,
											ReportingUIKomponentenTyp.CHECKBOX, 1)
							))
					), new ArrayList<>(), new ArrayList<>(), false, true, true)
	),

	/** Report-Vorlage: Stundenplanung - Lehrer - Stundenplan */
	STUNDENPLANUNG_V_LEHRER_STUNDENPLAN("Stundenplanung-LehrerStundenplan",
			erzeugeReportingParameter(List.of(ReportingAusgabeformat.PDF.getId(), ReportingAusgabeformat.EMAIL.getId()),
					List.of(
							erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "", true, 2, Arrays.asList(
									erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false,
											true, ReportingUIKomponentenTyp.CHECKBOX, 1),
									erzeugeVorlageParameter("mitPausenaufsichten", "mit Pausenaufsichten", ReportingReportvorlageParameterTyp.BOOLEAN,
											"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
									erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung", "Fachkürzel statt Fachbezeichnung",
											ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true,
											ReportingUIKomponentenTyp.CHECKBOX, 1)
							))
					), new ArrayList<>(), new ArrayList<>(), false, true, true)
	),

	/** Report-Vorlage: Stundenplanung - Lehrer - Stundenplan - Kombiniert */
	STUNDENPLANUNG_V_LEHRER_STUNDENPLAN_KOMBINIERT("Stundenplanung-LehrerStundenplanKombiniert",
			erzeugeReportingParameter(List.of(ReportingAusgabeformat.PDF.getId()), List.of(
					erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "", true, 2, Arrays.asList(
							erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false,
									true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitPausenaufsichten", "mit Pausenaufsichten", ReportingReportvorlageParameterTyp.BOOLEAN,
									"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
							erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung", "Fachkürzel statt Fachbezeichnung",
									ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true,
									ReportingUIKomponentenTyp.CHECKBOX, 1)
					))
			), new ArrayList<>(), new ArrayList<>(), false, false, true)
	),

	/** Report-Vorlage: Stundenplanung - Fach - Stundenplan */
	STUNDENPLANUNG_V_RAUM_STUNDENPLAN("Stundenplanung-RaumStundenplan",
			erzeugeReportingParameter(List.of(ReportingAusgabeformat.PDF.getId()), List.of(
					erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "", true, 2, Arrays.asList(
							erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false,
									true, ReportingUIKomponentenTyp.CHECKBOX, 1)
					))
			), new ArrayList<>(), new ArrayList<>(), false, true, true)
	),

	/** Report-Vorlage: Stundenplanung - Schüler - Stundenplan */
	STUNDENPLANUNG_V_SCHUELER_STUNDENPLAN("Stundenplanung-SchuelerStundenplan",
			erzeugeReportingParameter(List.of(ReportingAusgabeformat.PDF.getId(), ReportingAusgabeformat.EMAIL.getId()),
					List.of(
							erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "", true, 2, Arrays.asList(
									erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false,
											true, ReportingUIKomponentenTyp.CHECKBOX, 1),
									erzeugeVorlageParameter("mitFachStattKursbezeichnung", "Fach statt Kursbezeichnung",
											ReportingReportvorlageParameterTyp.BOOLEAN,
											"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
									erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung", "Fachkürzel statt Fachbezeichnung",
											ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true,
											ReportingUIKomponentenTyp.CHECKBOX, 1),
									erzeugeVorlageParameter("mitIndividuelleKursart", "mit individueller Kursart", ReportingReportvorlageParameterTyp.BOOLEAN,
											"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)
							))
					), new ArrayList<>(), new ArrayList<>(), false, true, true)
	);


	/** Die Bezeichnung der Report-Vorlage */
	private final @NotNull String bezeichnung;

	/** Reporting-Parameter inkl. der gültigen Vorlage-Parametergruppen für diese Report-Vorlage. */
	private final @NotNull ReportingParameter reportingParameter;

	/** Interne Map für direkten Zugriff auf Parameter: Reportvorlage > Parametername > Parameter */
	private static final @NotNull Map<String, Map<String, ReportingReportvorlageParameter>> MAP_PARAMETER = new HashMap<>();

	/** Interne Map für direkten Zugriff auf Parametergruppen: Reportvorlage > Parametergruppe > Parameterliste */
	private static final @NotNull Map<String, Map<String, ReportingReportvorlageParameterGruppe>> MAP_PARAMETERGRUPPEN = new HashMap<>();

	/** Interner Index, der Namensbestandteile der Enum mit den passenden Reportvorlagen verwaltet. */
	private static final @NotNull Map<String, List<ReportingReportvorlage>> MAP_NAMENSBESTANDTEILE_REPORTVORLAGEN = new HashMap<>();

	/** Gibt an, ob die internen Indexstrukturen bereits initialisiert wurden. */
	private static boolean mapsInitialisiert = false;

	/**
	 * Konstruktor für eine Reporting-Reportvorlage.
	 *
	 * @param bezeichnung Die Bezeichnung der Reportvorlage. Darf nicht null sein.
	 * @param reportingParameter Eine Liste mit den Vorlage-Parametern, basierend auf der jeweiligen Definition. Darf nicht null sein.
	 */
	ReportingReportvorlage(final @NotNull String bezeichnung, final @NotNull ReportingParameter reportingParameter) {
		this.bezeichnung = bezeichnung;
		reportingParameter.reportvorlage = bezeichnung;
		this.reportingParameter = reportingParameter;
	}


	/**
	 * Gibt die Bezeichnung dieser Report-Vorlage zurück
	 *
	 * @return Die Bezeichnung dieser Report-Vorlage
	 */
	public @NotNull String getBezeichnung() {
		return (this.bezeichnung != null) ? this.bezeichnung : "";
	}

	/**
	 * Gibt eine Kopie der ReportingParamater für diese Report-Vorlage zurück. So wird verhindert, dass die Werte der ReportingParameter in der ENUM
	 * verändert werden können, was im Client Server weit greifen würde.
	 *
	 * @return Die Kopie der ReportingParameter für diese Report-Vorlage.
	 */
	public @NotNull ReportingParameter getReportingParameter() {
		return cloneReportingParameter(this.reportingParameter);
	}


	/**
	 * Diese Methode ermittelt die Report-Vorlage anhand der übergebenen Bezeichnung.
	 *
	 * @param bezeichnung Die Bezeichnung der Report-Vorlage
	 *
	 * @return Die Report-Vorlage
	 */
	public static ReportingReportvorlage getByBezeichnung(final @NotNull String bezeichnung) {
		if (bezeichnung.isEmpty()) {
			return null;
		}
		for (final ReportingReportvorlage rv : ReportingReportvorlage.values()) {
			if (rv.bezeichnung.equals(bezeichnung)) {
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
	public static ReportingReportvorlage getByName(final @NotNull String name) {
		if (name.isEmpty()) {
			return null;
		}
		for (final ReportingReportvorlage rv : ReportingReportvorlage.values()) {
			if (rv.name().equalsIgnoreCase(name)) {
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
	public static @NotNull List<ReportingReportvorlage> getByNamensbestandteilen(final @NotNull List<String> namensbestandteile) {
		mapsInitialisieren();

		final List<String> namensbestandteileNormalisiert = new ArrayList<>();
		for (final String bestandteil : namensbestandteile) {
			if ((bestandteil != null) && !bestandteil.isBlank() && (bestandteil.trim().length() >= 3)) {
				namensbestandteileNormalisiert.add(bestandteil.trim().toLowerCase());
			}
		}

		Set<ReportingReportvorlage> gueltigeVorlagen = null;
		for (final String suchbegriff : namensbestandteileNormalisiert) {
			final List<ReportingReportvorlage> treffer = MAP_NAMENSBESTANDTEILE_REPORTVORLAGEN.get(suchbegriff);
			if ((treffer == null) || treffer.isEmpty()) {
				return new ArrayList<>();
			}

			if (gueltigeVorlagen == null) {
				gueltigeVorlagen = new HashSet<>(treffer);
			} else {
				gueltigeVorlagen.retainAll(treffer);
				if (gueltigeVorlagen.isEmpty()) {
					return new ArrayList<>();
				}
			}
		}

		return (gueltigeVorlagen == null) ? new ArrayList<>() : new ArrayList<>(gueltigeVorlagen);
	}

	/**
	 * Liefert alle Default-Vorlageparameter der Reportvorlage über deren Bezeichnung als Liste ohne Gruppenzuordnung.
	 * Achtung: Diese Methode darf nur für lesende Zugriffe verwendet werden!
	 *
	 * @return Liste der Default-Vorlageparameter der Reportvorlage (ggf. leer)
	 */
	public @NotNull List<ReportingReportvorlageParameter> getDefaultVorlageparameterByVorlage() {
		mapsInitialisieren();

		final String key1 = normalizeKeyInput(this.bezeichnung);
		final Map<String, ReportingReportvorlageParameter> mapParam = MAP_PARAMETER.get(key1);
		return (mapParam == null) ? new ArrayList<>() : new ArrayList<>(mapParam.values());
	}

	/**
	 * Liefert alle Default-Vorlageparameter einer Parametergruppe aus der Reportvorlage.
	 * Achtung: Diese Methode darf nur für lesende Zugriffe verwendet werden!
	 *
	 * @param parametergruppeName der Name der Vorlageparametergruppe
	 *
	 * @return Liste der Default-Vorlageparameter der Gruppe (ggf. leer)
	 */
	public @NotNull List<ReportingReportvorlageParameter> getDefaultVorlageparameterByGruppe(final @NotNull String parametergruppeName) {
		mapsInitialisieren();

		final String key1 = normalizeKeyInput(this.bezeichnung);
		final String key2 = normalizeKeyInput(parametergruppeName);

		final Map<String, ReportingReportvorlageParameterGruppe> mapGruppen = MAP_PARAMETERGRUPPEN.get(key1);
		if (mapGruppen != null) {
			final ReportingReportvorlageParameterGruppe gruppe = mapGruppen.get(key2);
			if ((gruppe != null) && (gruppe.reportvorlageParameter != null)) {
				return gruppe.reportvorlageParameter;
			}
		}
		return new ArrayList<>();
	}

	/**
	 * Liefert genau den gewünschten Default-Vorlageparameter der Reportvorlage.
	 * Achtung: Diese Methode darf nur für lesende Zugriffe verwendet werden!
	 *
	 * @param parameterName der Name des Vorlageparameters
	 *
	 * @return der Default-Vorlageparameter oder null, falls nicht vorhanden.
	 */
	public ReportingReportvorlageParameter getDefaultVorlageparameter(final @NotNull String parameterName) {
		mapsInitialisieren();

		final String key1 = normalizeKeyInput(this.bezeichnung);
		final String key3 = normalizeKeyInput(parameterName);

		final Map<String, ReportingReportvorlageParameter> mapParam = MAP_PARAMETER.get(key1);
		return (mapParam != null) ? mapParam.get(key3) : null;
	}

	/**
	 * Setzt den Wert eines Vorlageparameters innerhalb der ReportingParameter, die übergeben werden. Die Default-Werte der ENUM werden dadurch nicht angepasst.
	 *
	 * @param reportingParameter   das zu verändernde ReportingParameter-Objekt.
	 * @param vorlageparameterName der Name des zu suchenden Vorlageparameters
	 * @param wert                 der zu setzende Wert als String
	 */
	public void setReportingParameterVorlageparameter(final @NotNull ReportingParameter reportingParameter, final @NotNull String vorlageparameterName,
			final @NotNull String wert) {
		for (final ReportingReportvorlageParameterGruppe gruppe : reportingParameter.reportvorlageParameterGruppen) {
			if (gruppe.reportvorlageParameter == null) {
				continue;
			}
			for (final ReportingReportvorlageParameter param : gruppe.reportvorlageParameter) {
				if ((param.name != null) && param.name.equals(vorlageparameterName)) {
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
	public ReportingReportvorlageParameterGruppe getDefaultVorlageparametergruppeByName(final @NotNull String gruppenName) {
		mapsInitialisieren();

		final String key1 = normalizeKeyInput(this.bezeichnung);
		final String key2 = normalizeKeyInput(gruppenName);

		final Map<String, ReportingReportvorlageParameterGruppe> mapGruppen = MAP_PARAMETERGRUPPEN.get(key1);
		return (mapGruppen != null) ? mapGruppen.get(key2) : null;
	}


	// ##### Hilfsmethoden #####

	/** Initialisiert einmalig interne ListMaps/Indizes für schnelleren Zugriff. */
	private static synchronized void mapsInitialisieren() {
		if (mapsInitialisiert) {
			return;
		}

		for (final ReportingReportvorlage reportvorlage : ReportingReportvorlage.values()) {
			initialisiereParameterMaps(reportvorlage);
			initialisiereNamensbestandteileIndex(reportvorlage);
		}

		mapsInitialisiert = true;
	}

	/**
	 * Initialisiert die Maps für die Parameter und Parametergruppen einer Reportvorlage.
	 *
	 * @param reportvorlage die Reportvorlage
	 */
	private static void initialisiereParameterMaps(final @NotNull ReportingReportvorlage reportvorlage) {
		final String key1 = normalizeKeyInput(reportvorlage.bezeichnung);
		Map<String, ReportingReportvorlageParameter> mapParam = MAP_PARAMETER.get(key1);
		if (mapParam == null) {
			mapParam = new HashMap<>();
			MAP_PARAMETER.put(key1, mapParam);
		}
		Map<String, ReportingReportvorlageParameterGruppe> mapGruppen = MAP_PARAMETERGRUPPEN.get(key1);
		if (mapGruppen == null) {
			mapGruppen = new HashMap<>();
			MAP_PARAMETERGRUPPEN.put(key1, mapGruppen);
		}

		for (final ReportingReportvorlageParameterGruppe gruppe : reportvorlage.reportingParameter.reportvorlageParameterGruppen) {
			if ((gruppe == null) || (gruppe.name == null) || (gruppe.reportvorlageParameter == null)) {
				continue;
			}

			final String key2 = normalizeKeyInput(gruppe.name);
			mapGruppen.put(key2, gruppe);

			for (final ReportingReportvorlageParameter parameter : gruppe.reportvorlageParameter) {
				if ((parameter == null) || (parameter.name == null)) {
					continue;
				}

				final String key3 = normalizeKeyInput(parameter.name);
				mapParam.put(key3, parameter);
			}
		}
	}

	/**
	 * Initialisiert den Suchindex für die Bestandteile des ENUM-Namens einer Reportvorlage.
	 *
	 * @param reportvorlage die Reportvorlage
	 */
	private static void initialisiereNamensbestandteileIndex(final @NotNull ReportingReportvorlage reportvorlage) {
		final String[] enumNameBestandteile = reportvorlage.name().toLowerCase().split("_");
		for (final String bestandteil : enumNameBestandteile) {
			if ((bestandteil == null) || bestandteil.isBlank()) {
				continue;
			}
			// Da der Transpiler Probleme mit FomputeIfAbsend hat, wird hier auf diese Funktion verzichtet.
			@SuppressWarnings("java:S3824") List<ReportingReportvorlage> vorlagen = MAP_NAMENSBESTANDTEILE_REPORTVORLAGEN.get(bestandteil);
			if (vorlagen == null) {
				vorlagen = new ArrayList<>();
				MAP_NAMENSBESTANDTEILE_REPORTVORLAGEN.put(bestandteil, vorlagen);
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
	private static @NotNull ReportingParameter erzeugeReportingParameter(final List<Integer> ausgabeformatOptionen,
			final List<ReportingReportvorlageParameterGruppe> reportvorlageParameterGruppen,
			final List<ReportingSortierungDefinitionGruppe> sortierungDefinitionenGruppen,
			final List<ReportingFilterDefinitionGruppe> filterDefinitionenGruppen,
			final @NotNull boolean uiIstSichtbarEinzelausgabeHauptdaten, final @NotNull boolean uiIstSichtbarEinzelausgabeDetaildaten,
			final @NotNull boolean uiIstSichtbarDuplexdruck) {
		// Erstelle ein ReportingParameterObjekt mit dessen Standardwerten.
		final ReportingParameter reportingParameter = new ReportingParameter();
		reportingParameter.ausgabeformatOptionen = new ArrayList<>(
				((ausgabeformatOptionen == null) || ausgabeformatOptionen.isEmpty()) ? List.of(ReportingAusgabeformat.PDF.getId()) : ausgabeformatOptionen);

		// Erzeuge die Gruppe der Standardausgabeoptionen (Einzelausgabe und Duplexdruck).
		// In der Regel ist immer nur eine der beiden Optionen für den Einzeldruck sichtbar.
		final ReportingReportvorlageParameterGruppe standardausgabeoptionenGruppe =
				erzeugeReportingvorlageParameterGruppe("Ausgabeoptionen", "", true, 3, Arrays.asList(
						erzeugeVorlageParameter("einzelausgabeHauptdaten", "Einzelausgabe der Daten", ReportingReportvorlageParameterTyp.BOOLEAN,
								"" + false, uiIstSichtbarEinzelausgabeHauptdaten, ReportingUIKomponentenTyp.CHECKBOX, 1),
						erzeugeVorlageParameter("einzelausgabeDetaildaten", "Einzelausgabe der Daten", ReportingReportvorlageParameterTyp.BOOLEAN,
								"" + false, uiIstSichtbarEinzelausgabeDetaildaten, ReportingUIKomponentenTyp.CHECKBOX, 1),
						erzeugeVorlageParameter("duplexdruck", "Duplexdruck", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false,
								uiIstSichtbarDuplexdruck, ReportingUIKomponentenTyp.CHECKBOX, 1)));

		// Füge dann die übergebenen ReportvorlageParameter-Gruppen und die Ausgabeoptionen mit ihren ReportvorlageParametern hinzu.
		reportingParameter.reportvorlageParameterGruppen = new ArrayList<>(
				(reportvorlageParameterGruppen == null) ? new ArrayList<>() : reportvorlageParameterGruppen);
		reportingParameter.reportvorlageParameterGruppen.add(standardausgabeoptionenGruppe);

		// Setze die Sortierungs- und Filtergruppen.
		reportingParameter.sortierungDefinitionenGruppen = new ArrayList<>(
				(sortierungDefinitionenGruppen == null) ? new ArrayList<>() : sortierungDefinitionenGruppen);
		reportingParameter.filterDefinitionenGruppen = new ArrayList<>(
				(filterDefinitionenGruppen == null) ? new ArrayList<>() : filterDefinitionenGruppen);

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
	private static @NotNull ReportingReportvorlageParameterGruppe erzeugeReportingvorlageParameterGruppe(
			final @NotNull String name, final @NotNull String beschreibung, final @NotNull boolean uiIstSichtbar, final int uiAnzahlSpalten,
			final @NotNull List<ReportingReportvorlageParameter> reportingReportvorlageParameter) {
		final ReportingReportvorlageParameterGruppe reportingReportvorlageParameterGruppe = new ReportingReportvorlageParameterGruppe();
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
	private static @NotNull ReportingReportvorlageParameter erzeugeVorlageParameter(
			final @NotNull String name, final @NotNull String bezeichnung, final @NotNull ReportingReportvorlageParameterTyp typ,
			final @NotNull String wert, final @NotNull boolean uiIstSichtbar, final @NotNull ReportingUIKomponentenTyp uiKomponentenTyp,
			final int uiAnzahlSpalten) {
		final ReportingReportvorlageParameter reportingReportVorlageParameter = new ReportingReportvorlageParameter();
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
	private static @NotNull ReportingSortierungDefinitionGruppe erzeugeSortierungDefinitionGruppe(
			final @NotNull String bezeichnung, final @NotNull String typ, final boolean uiIstSichtbar,
			final @NotNull List<ReportingSortierungDefinition> sortierungDefinitionenOptionen) {
		final ReportingSortierungDefinitionGruppe gruppe = new ReportingSortierungDefinitionGruppe();
		gruppe.bezeichnung = bezeichnung;
		gruppe.typ = typ;
		gruppe.uiIstSichtbar = uiIstSichtbar;
		gruppe.sortierungDefinitionenOptionen = new ArrayList<>(sortierungDefinitionenOptionen);
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
	private static @NotNull ReportingFilterDefinitionGruppe erzeugeFilterDefinitionGruppe(
			final @NotNull String bezeichnung, final @NotNull String typ, final boolean uiIstSichtbar, final boolean uiIstMultiselect,
			final @NotNull ReportingFilterVerknuepfung multiselectVerknuepfung,
			final @NotNull List<ReportingFilterDefinition> filterDefinitionenOptionen) {
		final ReportingFilterDefinitionGruppe gruppe = new ReportingFilterDefinitionGruppe();
		gruppe.bezeichnung = bezeichnung;
		gruppe.typ = typ;
		gruppe.uiIstSichtbar = uiIstSichtbar;
		gruppe.uiIstMultiselect = uiIstMultiselect;
		gruppe.multiselectVerknuepfung = multiselectVerknuepfung.getId();
		gruppe.filterDefinitionenOptionen = new ArrayList<>(filterDefinitionenOptionen);
		return gruppe;
	}

	/**
	 * Normalisiert den angegebenen String, indem alle Leerzeichen entfernt und die Zeichen in Kleinbuchstaben umgewandelt werden.
	 *
	 * @param input Der String, der normalisiert werden soll.
	 *
	 * @return Der normalisierte String.
	 */
	private static @NotNull String normalizeKeyInput(final String input) {
		return (input == null) ? "" : input.trim().toLowerCase();
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
	private static @NotNull ReportingParameter cloneReportingParameter(final @NotNull ReportingParameter source) {
		final ReportingParameter copy = new ReportingParameter();

		copy.idSchuljahresabschnitt = source.idSchuljahresabschnitt;
		copy.ausgabeformat = source.ausgabeformat;
		copy.reportvorlage = source.reportvorlage;
		copy.idHauptdatenObjekt = source.idHauptdatenObjekt;
		copy.ausgabeformatOptionen.addAll(source.ausgabeformatOptionen);
		copy.idsHauptdaten.addAll(source.idsHauptdaten);
		copy.idsDetaildaten.addAll(source.idsDetaildaten);

		if (source.eMailDaten != null) {
			copy.eMailDaten = new ReportingEMailDaten();
			copy.eMailDaten.empfaengerTyp = source.eMailDaten.empfaengerTyp;
			copy.eMailDaten.istPrivateEmailAlternative = source.eMailDaten.istPrivateEmailAlternative;
			copy.eMailDaten.betreff = source.eMailDaten.betreff;
			copy.eMailDaten.text = source.eMailDaten.text;
		} else {
			copy.eMailDaten = null;
		}

		copy.reportvorlageParameterGruppen.addAll(cloneVorlageParameterGruppen(source.reportvorlageParameterGruppen));
		copy.sortierungDefinitionenGruppen.addAll(cloneSortierungDefinitionGruppen(source.sortierungDefinitionenGruppen));
		copy.filterDefinitionenGruppen.addAll(cloneFilterDefinitionGruppen(source.filterDefinitionenGruppen));

		return copy;
	}

	private static @NotNull List<ReportingReportvorlageParameterGruppe> cloneVorlageParameterGruppen(final List<ReportingReportvorlageParameterGruppe> source) {
		final List<ReportingReportvorlageParameterGruppe> result = new ArrayList<>();
		if ((source == null) || source.isEmpty()) {
			return result;
		}
		for (final ReportingReportvorlageParameterGruppe vpg : source) {
			if (vpg == null) {
				continue;
			}
			final ReportingReportvorlageParameterGruppe vpgCopy = new ReportingReportvorlageParameterGruppe();
			vpgCopy.name = vpg.name;
			vpgCopy.beschreibung = vpg.beschreibung;
			vpgCopy.uiIstSichtbar = vpg.uiIstSichtbar;
			vpgCopy.uiAnzahlSpalten = vpg.uiAnzahlSpalten;
			if (vpg.reportvorlageParameter != null) {
				vpgCopy.reportvorlageParameter.addAll(cloneVorlageParameter(vpg.reportvorlageParameter));
			}
			result.add(vpgCopy);
		}
		return result;
	}

	private static @NotNull List<ReportingReportvorlageParameter> cloneVorlageParameter(final List<ReportingReportvorlageParameter> source) {
		final List<ReportingReportvorlageParameter> result = new ArrayList<>();
		if ((source == null) || source.isEmpty()) {
			return result;
		}
		for (final ReportingReportvorlageParameter vp : source) {
			if (vp == null) {
				continue;
			}
			final ReportingReportvorlageParameter vpCopy = new ReportingReportvorlageParameter();
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

	private static @NotNull List<ReportingSortierungDefinitionGruppe> cloneSortierungDefinitionGruppen(final List<ReportingSortierungDefinitionGruppe> source) {
		final List<ReportingSortierungDefinitionGruppe> result = new ArrayList<>();
		if (source == null) {
			return result;
		}
		for (final ReportingSortierungDefinitionGruppe sdg : source) {
			if (sdg.sortierungDefinitionenOptionen == null) {
				continue;
			}
			final ReportingSortierungDefinitionGruppe sdgCopy = new ReportingSortierungDefinitionGruppe();
			sdgCopy.bezeichnung = sdg.bezeichnung;
			sdgCopy.typ = sdg.typ;
			sdgCopy.uiIstSichtbar = sdg.uiIstSichtbar;
			if (sdg.sortierungDefinitionenOptionen != null) {
				sdgCopy.sortierungDefinitionenOptionen.addAll(cloneSortierungDefinitionen(sdg.sortierungDefinitionenOptionen));
			}
			result.add(sdgCopy);
		}
		return result;
	}

	private static @NotNull List<ReportingSortierungDefinition> cloneSortierungDefinitionen(final List<ReportingSortierungDefinition> source) {
		final List<ReportingSortierungDefinition> result = new ArrayList<>();
		if ((source == null) || source.isEmpty()) {
			return result;
		}
		for (final ReportingSortierungDefinition sd : source) {
			if (sd == null) {
				continue;
			}
			final ReportingSortierungDefinition sdCopy = new ReportingSortierungDefinition();
			sdCopy.bezeichnung = sd.bezeichnung;
			sdCopy.typ = sd.typ;
			sdCopy.verwendeStandardsortierung = sd.verwendeStandardsortierung;
			sdCopy.attribute.addAll(sd.attribute);
			result.add(sdCopy);
		}
		return result;
	}

	private static @NotNull List<ReportingFilterDefinitionGruppe> cloneFilterDefinitionGruppen(final List<ReportingFilterDefinitionGruppe> source) {
		final List<ReportingFilterDefinitionGruppe> result = new ArrayList<>();
		if ((source == null) || source.isEmpty()) {
			return result;
		}
		for (final ReportingFilterDefinitionGruppe fdg : source) {
			if (fdg == null) {
				continue;
			}
			final ReportingFilterDefinitionGruppe fdgCopy = new ReportingFilterDefinitionGruppe();
			fdgCopy.bezeichnung = fdg.bezeichnung;
			fdgCopy.typ = fdg.typ;
			fdgCopy.uiIstSichtbar = fdg.uiIstSichtbar;
			fdgCopy.uiIstMultiselect = fdg.uiIstMultiselect;
			fdgCopy.multiselectVerknuepfung = fdg.multiselectVerknuepfung;
			if (fdg.filterDefinitionenOptionen != null) {
				fdgCopy.filterDefinitionenOptionen.addAll(cloneFilterDefinitionen(fdg.filterDefinitionenOptionen));
			}
			result.add(fdgCopy);
		}
		return result;
	}

	private static @NotNull List<ReportingFilterDefinition> cloneFilterDefinitionen(final List<ReportingFilterDefinition> source) {
		final List<ReportingFilterDefinition> result = new ArrayList<>();
		if ((source == null) || source.isEmpty()) {
			return result;
		}
		for (final ReportingFilterDefinition fd : source) {
			if (fd == null) {
				continue;
			}
			final ReportingFilterDefinition fdCopy = new ReportingFilterDefinition();
			fdCopy.bezeichnung = fd.bezeichnung;
			fdCopy.typ = fd.typ;
			fdCopy.kriterien.addAll(cloneFilterKriterien(fd.kriterien));
			result.add(fdCopy);
		}
		return result;
	}

	private static @NotNull List<ReportingFilterKriterium> cloneFilterKriterien(final List<ReportingFilterKriterium> source) {
		final List<ReportingFilterKriterium> result = new ArrayList<>();
		if ((source == null) || source.isEmpty()) {
			return result;
		}
		for (final ReportingFilterKriterium k : source) {
			if (k == null) {
				continue;
			}
			final ReportingFilterKriterium kCopy = new ReportingFilterKriterium();
			kCopy.verknuepfung = k.verknuepfung;
			kCopy.nicht = k.nicht;
			if (k.eintraege != null) {
				for (final ReportingFilterEintrag e : k.eintraege) {
					final ReportingFilterEintrag eCopy = new ReportingFilterEintrag();
					eCopy.attribut = e.attribut;
					eCopy.operation = e.operation;
					eCopy.werte.addAll(e.werte);
					kCopy.eintraege.add(eCopy);
				}
			}
			if (k.unterkriterien != null) {
				kCopy.unterkriterien.addAll(cloneFilterKriterien(k.unterkriterien));
			}
			result.add(kCopy);
		}
		return result;
	}

}
