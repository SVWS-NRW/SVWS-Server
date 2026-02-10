package de.svws_nrw.core.types.reporting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.reporting.ReportingVorlageParameterGruppe;
import de.svws_nrw.core.data.reporting.ReportingVorlageParameter;
import jakarta.validation.constraints.NotNull;

/**
 * Eine ENUM der integrierten Report-Vorlagen des SVWS-Servers. Im Rahmen des Reportings werden auf Basis dieses CoreTyps Template-Definitionen vorgenommen.
 * Hinweis: Es ist nicht ausreichend, eine neue Vorlage nur hier einzubinden. Es muss in jedem Fall auch eine neue HTML-Template-Definition im
 * Reporting-Modul erstellt werden.
 * Anmerkung: Die Benennung der Vorlagen erfolgt nach dem Schema Hauptdaten_v_Detaildaten. Bei der Report-Generierung erfolgt in Teilen ein entsprechendes
 * Füllen der Datenkontexte anhand der Benennung.
 */
public enum ReportingReportvorlage {

	/** Report-Vorlage: GOSt - Klausurplanung - Klausurtermine-Kurse */
	GOST_KLAUSURPLANUNG_V_KLAUSURTERMINE_MIT_KURSEN("GostKlausurplanung-KlausurtermineMitKursen", Arrays.asList(
			erzeugeVorlageParameter("mitKursklausuren", "mit Kursklausuren", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitNachschreibern", "mit Nachschreibern", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitKlausurschreiberNamen", "mit Namen der Klausurschreiber", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1))),

	/** Report-Vorlage: GOSt - Klausurplanung - Schueler-Klausuren */
	GOST_KLAUSURPLANUNG_V_SCHUELER_MIT_KLAUSUREN("GostKlausurplanung-SchuelerMitKlausuren", new ArrayList<>()),

	/** Report-Vorlage: GOSt - Kursplanung - Kurs-Kurschüler */
	GOST_KURSPLANUNG_V_KURS_MIT_KURSSCHUELERN("GostKursplanung-KursMitKursschuelern", new ArrayList<>()),

	/** Report-Vorlage: GOSt - Kursplanung - Kurse-Statistikwerte */
	GOST_KURSPLANUNG_V_KURSE_MIT_STATISTIKWERTEN("GostKursplanung-KurseMitStatistikwerten", new ArrayList<>()),

	/** Report-Vorlage: GOSt - Kursplanung - Schüler-Kurse */
	GOST_KURSPLANUNG_V_SCHUELER_MIT_KURSEN("GostKursplanung-SchuelerMitKursen", new ArrayList<>()),

	/** Report-Vorlage: GOSt - Kursplanung - Schüler-Schienen-Kurse */
	GOST_KURSPLANUNG_V_SCHUELER_MIT_SCHIENEN_KURSEN("GostKursplanung-SchuelerMitSchienenKursen", new ArrayList<>()),

	/** Report-Vorlage: GOSt - Laufbahnplanung - Abiturjahrgang - Fachwahlstatistiken */
	GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG_V_FACHWAHLSTATISTIKEN("GostLaufbahnplanung-Abiturjahrgang-Fachwahlstatistiken", new ArrayList<>()),

	/** Report-Vorlage: Klasse - Liste - Schüler - Kontaktdaten - Erzieher */
	KLASSEN_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER("Klasse-Liste-Schueler-Kontaktdaten-Erzieher", Arrays.asList(
			erzeugeVorlageParameter("nurSchuelerRufname", "nur Rufname", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitSchuelerGeschlecht", "mit Geschlecht", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitSchuelerGebDat", "mit Geburtsdatum", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitSchuelerStaat", "mit Staatsangehörigkeit", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitSchuelerAnschrift", "mit Anschrift", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitSchuelerTelefonPrivat", "mit Telefon (privat)", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitSchuelerEmailSchule", "mit E-Mail (Schule)", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitSchuelerEmailPrivat", "mit E-Mail (privat)", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitSpalteSchuelerTelefonKontakte", "mit Telefonkontakten", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitErzieher", "mit Erziehern", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitErzieherAnschrift", "mit Erzieher-Anschrift", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitErzieherEmailPrivat", "mit Erzieher-E-Mail", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1))),

	/** Report-Vorlage: Klasse - Liste - Schüler - Leistungsdaten */
	KLASSEN_V_LISTE_SCHUELER_LEISTUNGSDATEN("Klasse-Liste-Schueler-Leistungsdaten", Arrays.asList(
			erzeugeVorlageParameter("mitPunktenStattNoten", "Punkte statt Noten ausgeben", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitQuartalsnote", "Quartalsnoten statt Noten ausgeben", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitIndividuellerKursart", "mit individueller Kursart", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitZuweisung", "mit Zuweisungen", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitGesamtfehlstunden", "mit Gesamtfehlstunden", ReportingVorlageParameterTyp.BOOLEAN, "true", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitFachbezogenenFehlstunden", "mit fachbezogenen Fehlstunden", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitFachbezogenenBemerkungen", "mit fachbezogenen Bemerkungen", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitLernentwicklung", "mit Angabe zur Lernentwicklung", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitFoerderschwerpunkt", "mit Angaben zum Förderschwerpunkt", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitASVBemerkung", "mit ASV-Bemerkungen", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitAUEBemerkung", "mit AUE-Bemerkungen", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitZeugnisbemerkung", "mit Zeugnisbemerkungen", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitSchulformempfehlung", "mit Empfehlung der Schulform", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitVersetzungAbschluss", "mit Versetzung und Abschluss", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitVersetzungsentscheidung", "mit Text zur Versetzungsentscheidung", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1))),

	/** Report-Vorlage: Kurs - Liste - Schüler - Kontaktdaten - Erzieher */
	KURSE_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER("Kurs-Liste-Schueler-Kontaktdaten-Erzieher", Arrays.asList(
			erzeugeVorlageParameter("mitSchuelerKlasse", "mit Klasse", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("nurSchuelerRufname", "nur Rufname", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitSchuelerGeschlecht", "mit Geschlecht", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitSchuelerGebDat", "mit Geburtsdatum", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitSchuelerStaat", "mit Staatsangehörigkeit", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitSchuelerAnschrift", "mit Anschrift", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitSchuelerTelefonPrivat", "mit Telefon (privat)", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitSchuelerEmailSchule", "mit E-Mail (Schule)", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitSchuelerEmailPrivat", "mit E-Mail (privat)", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitSpalteSchuelerTelefonKontakte", "mit Telefonkontakten", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitErzieher", "mit Erziehern", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitErzieherAnschrift", "mit Erzieher-Anschrift", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitErzieherEmailPrivat", "mit Erzieher-E-Mail", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1))),

	/** Report-Vorlage: Kurs - Liste - Schüler - Leistungsdaten */
	KURSE_V_LISTE_SCHUELER_LEISTUNGSDATEN("Kurs-Liste-Schueler-Leistungsdaten", Arrays.asList(
			erzeugeVorlageParameter("mitPunktenStattNoten", "Punkte statt Noten ausgeben", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitBemerkungen", "mit fachbezogenen Bemerkungen", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1))),

	/** Report-Vorlage: Lehrer - Liste - Schüler - Leistungsdaten */
	LEHRER_V_LISTE_SCHUELER_LEISTUNGSDATEN("Lehrer-Liste-Schueler-Leistungsdaten", Arrays.asList(
			erzeugeVorlageParameter("mitKlassenunterricht", "mit Klassenunterricht", ReportingVorlageParameterTyp.BOOLEAN, "true", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitKursunterricht", "mit Kursunterricht", ReportingVorlageParameterTyp.BOOLEAN, "true", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitPunktenStattNoten", "Punkte statt Noten ausgeben", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitBemerkungen", "mit fachbezogenen Bemerkungen", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1))),

	/** Report-Vorlage: Lehrer - Stammdaten - Liste */
	LEHRER_V_STAMMDATENLISTE("Lehrer-Stammdatenliste", new ArrayList<>()),

	/** Report-Vorlage: GOSt - Abitur - APO - Anlage 12 (Abiturzeugnis) - Din-A4 */
	SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A4("Schueler-GostAbiturApoAnlage12-A4", Arrays.asList(
			erzeugeVorlageParameter("mitPersoenlichenUnterschriften", "mit persönlichen Unterschriften", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitZweiterBeratungslehrerUnterschrift", "mit Unterschrift 2. Beratungslehrer", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("textZAAVorsitzUnterschrift", "Unterschrift ZAA-Vorsitz", ReportingVorlageParameterTyp.STRING, "", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("textZAAVorsitzUnterschriftBezeichnung", "Bezeichnung ZAA-Vorsitz", ReportingVorlageParameterTyp.STRING, "", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("textSchulleitungUnterschrift", "Unterschrift Schulleitung", ReportingVorlageParameterTyp.STRING, "", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("textSchulleitungUnterschriftBezeichnung", "Bezeichnung Schulleitung", ReportingVorlageParameterTyp.STRING, "", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("textSchultraegerUnterschrift", "Unterschrift Schulträger", ReportingVorlageParameterTyp.STRING, "", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("textSchultraegerUnterschriftBezeichnung", "Bezeichnung Schulträger", ReportingVorlageParameterTyp.STRING, "", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("textBeratungslehrerUnterschrift", "Unterschrift Beratungslehrer", ReportingVorlageParameterTyp.STRING, "", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("textBeratungslehrerUnterschriftBezeichnung", "Bezeichnung Beratungslehrer", ReportingVorlageParameterTyp.STRING, "", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1))),

	/** Report-Vorlage: GOSt - Abitur - APO - Anlage 12 (Abiturzeugnis) - Din-A3 */
	SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A3("Schueler-GostAbiturApoAnlage12-A3", Arrays.asList(
			erzeugeVorlageParameter("mitPersoenlichenUnterschriften", "mit persönlichen Unterschriften", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitZweiterBeratungslehrerUnterschrift", "mit Unterschrift 2. Beratungslehrer", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("textZAAVorsitzUnterschrift", "Unterschrift ZAA-Vorsitz", ReportingVorlageParameterTyp.STRING, "", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("textZAAVorsitzUnterschriftBezeichnung", "Bezeichnung ZAA-Vorsitz", ReportingVorlageParameterTyp.STRING, "", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("textSchulleitungUnterschrift", "Unterschrift Schulleitung", ReportingVorlageParameterTyp.STRING, "", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("textSchulleitungUnterschriftBezeichnung", "Bezeichnung Schulleitung", ReportingVorlageParameterTyp.STRING, "", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("textSchultraegerUnterschrift", "Unterschrift Schulträger", ReportingVorlageParameterTyp.STRING, "", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("textSchultraegerUnterschriftBezeichnung", "Bezeichnung Schulträger", ReportingVorlageParameterTyp.STRING, "", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("textBeratungslehrerUnterschrift", "Unterschrift Beratungslehrer", ReportingVorlageParameterTyp.STRING, "", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("textBeratungslehrerUnterschriftBezeichnung", "Bezeichnung Beratungslehrer", ReportingVorlageParameterTyp.STRING, "", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1))),

	/** Report-Vorlage: GOSt - Laufbahnplanung - Ergebnisübersicht */
	SCHUELER_V_GOST_LAUFBAHNPLANUNG_ERGEBNISUEBERSICHT("Schueler-GostLaufbahnplanungErgebnisuebersicht", Arrays.asList(
			erzeugeVorlageParameter("mitFehlernKommentaren", "mit Fehlern/Kommentaren", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitHinweisen", "mit Hinweisen", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1))),

	/** Report-Vorlage: GOSt - Laufbahnplanung - Wahlbogen */
	SCHUELER_V_GOST_LAUFBAHNPLANUNG_WAHLBOGEN("Schueler-GostLaufbahnplanungWahlbogen", Arrays.asList(
			erzeugeVorlageParameter("nurBelegteFaecher", "nur belegte Fächer", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1))),

	/** Report-Vorlage: Schüler - Schulbescheinigung */
	SCHUELER_V_SCHULBESCHEINIGUNG("Schueler-Schulbescheinigung", Arrays.asList(
			erzeugeVorlageParameter("fuerErzieher", "für Erzieher", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitBildBriefkopf", "mit Bild im Briefkopf", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitSchullogo", "mit Schullogo", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("keineAnschrift", "ohne Anschrift", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("keinInfoblock", "ohne Infoblock", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("keineUnterschrift", "ohne Unterschrift", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1))),

	/** Report-Vorlage: Schüler - Liste - Kontaktdaten - Erzieher */
	SCHUELER_V_LISTE_KONTAKTDATENERZIEHER("Schueler-Liste-Kontaktdaten-Erzieher", Arrays.asList(
			erzeugeVorlageParameter("mitSchuelerKlasse", "mit Klasse", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("nurSchuelerRufname", "nur Rufname", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitSchuelerGeschlecht", "mit Geschlecht", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitSchuelerGebDat", "mit Geburtsdatum", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitSchuelerStaat", "mit Staatsangehörigkeit", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitSchuelerAnschrift", "mit Anschrift", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitSchuelerTelefonPrivat", "mit Telefon (privat)", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitSchuelerEmailSchule", "mit E-Mail (Schule)", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitSchuelerEmailPrivat", "mit E-Mail (privat)", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitSpalteSchuelerTelefonKontakte", "mit Telefonkontakten", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitErzieher", "mit Erziehern", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitErzieherAnschrift", "mit Erzieher-Anschrift", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitErzieherEmailPrivat", "mit Erzieher-E-Mail", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1))),

	/** Report-Vorlage: Stundenplanung - Fach - Stundenplan */
	STUNDENPLANUNG_V_FACH_STUNDENPLAN("Stundenplanung-FachStundenplan", Arrays.asList(
			erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1))),

	/** Report-Vorlage: Stundenplanung - Klasse - Stundenplan */
	STUNDENPLANUNG_V_KLASSEN_STUNDENPLAN("Stundenplanung-KlassenStundenplan", Arrays.asList(
			erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitFachStattKursbezeichnung", "Fach statt Kursbezeichnung", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung", "Fachkürzel statt Fachbezeichnung", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1))),

	/** Report-Vorlage: Stundenplanung - Lehrer - Stundenplan */
	STUNDENPLANUNG_V_LEHRER_STUNDENPLAN("Stundenplanung-LehrerStundenplan", Arrays.asList(
			erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitPausenaufsichten", "mit Pausenaufsichten", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung", "Fachkürzel statt Fachbezeichnung", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1))),

	/** Report-Vorlage: Stundenplanung - Lehrer - Stundenplan - Kombiniert */
	STUNDENPLANUNG_V_LEHRER_STUNDENPLAN_KOMBINIERT("Stundenplanung-LehrerStundenplanKombiniert", Arrays.asList(
			erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitPausenaufsichten", "mit Pausenaufsichten", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung", "Fachkürzel statt Fachbezeichnung", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1))),

	/** Report-Vorlage: Stundenplanung - Fach - Stundenplan */
	STUNDENPLANUNG_V_RAUM_STUNDENPLAN("Stundenplanung-RaumStundenplan", Arrays.asList(
			erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1))),

	/** Report-Vorlage: Stundenplanung - Schüler - Stundenplan */
	STUNDENPLANUNG_V_SCHUELER_STUNDENPLAN("Stundenplanung-SchuelerStundenplan", Arrays.asList(
			erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitFachStattKursbezeichnung", "Fach statt Kursbezeichnung", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung", "Fachkürzel statt Fachbezeichnung", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1),
			erzeugeVorlageParameter("mitIndividuelleKursart", "mit individueller Kursart", ReportingVorlageParameterTyp.BOOLEAN, "false", "true",
					ReportingUIKomponentenTyp.CHECKBOX, 1)));


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
	private static @NotNull ReportingVorlageParameter erzeugeVorlageParameter(
			final @NotNull String name,
			final @NotNull String bezeichnung,
			final @NotNull ReportingVorlageParameterTyp typ,
			final @NotNull String wert,
			final @NotNull String istSichtbar,
			final @NotNull ReportingUIKomponentenTyp komponentenTyp,
			final int anzahlSpalten) {
		final ReportingVorlageParameter reportingVorlageParameter = new ReportingVorlageParameter();
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
	private static @NotNull ReportingVorlageParameterGruppe erzeugeReportingVorlageParameterGruppe(
			final @NotNull String name,
			final @NotNull String beschreibung,
			final @NotNull String istSichtbar,
			final int anzahlSpalten,
			final @NotNull List<ReportingVorlageParameter> reportingVorlageParameter) {
		final ReportingVorlageParameterGruppe reportingVorlageParameterGruppe = new ReportingVorlageParameterGruppe();
		reportingVorlageParameterGruppe.name = name;
		reportingVorlageParameterGruppe.beschreibung = beschreibung;
		reportingVorlageParameterGruppe.istSichtbar = istSichtbar;
		reportingVorlageParameterGruppe.anzahlSpalten = anzahlSpalten;
		reportingVorlageParameterGruppe.reportingVorlageParameterList = reportingVorlageParameter;
		return reportingVorlageParameterGruppe;
	}

	/** Die Bezeichnung der Report-Vorlage */
	private final @NotNull String bezeichnung;

	/** Eine Liste, in der die gültigen Vorlage-Parameter der Report-Vorlage enthalten sind. */
	private final @NotNull List<ReportingVorlageParameter> vorlageParameterList = new ArrayList<>();

	/** Eine Map, die die gültigen Vorlage-Parameter der Report-Vorlage zum Namen des Parameters enthält. */
	private final @NotNull Map<String, ReportingVorlageParameter> vorlageParameterMap = new HashMap<>();

	/**
	 * Konstruktor für eine Reporting-Reportvorlage.
	 *
	 * @param bezeichnung Die Bezeichnung der Reportvorlage. Darf nicht null sein.
	 * @param vorlageParameterList Eine Liste mit den Vorlage-Parametern, basierend auf der jeweiligen Definition. Darf nicht null sein.
	 */
	ReportingReportvorlage(final @NotNull String bezeichnung, final @NotNull List<ReportingVorlageParameter> vorlageParameterList) {
		this.bezeichnung = bezeichnung;
		this.vorlageParameterList.addAll(vorlageParameterList);
		for (final ReportingVorlageParameter vp : vorlageParameterList)
			this.vorlageParameterMap.put(vp.name, vp);
	}

	/**
	 * Diese Methode ermittelt die Report-Vorlage anhand der übergebenen Bezeichnung.
	 *
	 * @param bezeichnung Die Bezeichnung der Report-Vorlage
	 *
	 * @return Die Report-Vorlage
	 */
	public static ReportingReportvorlage getByBezeichnung(final @NotNull String bezeichnung) {
		if (bezeichnung.isEmpty())
			return null;
		for (final ReportingReportvorlage rv : ReportingReportvorlage.values())
			if (rv.bezeichnung.equals(bezeichnung))
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
	public static ReportingReportvorlage getByName(final @NotNull String name) {
		if (name.isEmpty())
			return null;
		for (final ReportingReportvorlage rv : ReportingReportvorlage.values())
			if (rv.name().equalsIgnoreCase(name))
				return rv;
		return null;
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
	 * Gibt die Liste der Report-Parameter für diese Report-Vorlage zurück.
	 *
	 * @return Die Liste der Report-Parameter für diese Report-Vorlage.
	 */
	public @NotNull List<ReportingVorlageParameter> getVorlageParameterList() {
		return this.vorlageParameterList;
	}

	/**
	 * Gibt die Map der Report-Parameter für diese Report-Vorlage zurück.
	 *
	 * @return Die Map der Report-Parameter für diese Report-Vorlage.
	 */
	public @NotNull Map<String, ReportingVorlageParameter> getVorlageParameterMap() {
		return this.vorlageParameterMap;
	}

}
