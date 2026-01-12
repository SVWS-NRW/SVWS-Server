package de.svws_nrw.core.types.reporting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
			erzeugeVorlageParameter("mitKursklausuren", "mit Kursklausuren", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitNachschreibern", "mit Nachschreibern", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitKlausurschreiberNamen", "mit Namen der Klausurschreiber", ReportingVorlageParameterTyp.BOOLEAN, "false"))),

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
			erzeugeVorlageParameter("nurSchuelerRufname", "nur Rufname", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitSchuelerGeschlecht", "mit Geschlecht", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitSchuelerGebDat", "mit Geburtsdatum", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitSchuelerStaat", "mit Staatsangehörigkeit", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitSchuelerAnschrift", "mit Anschrift", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitSchuelerTelefonPrivat", "mit Telefon (privat)", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitSchuelerEmailSchule", "mit E-Mail (Schule)", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitSchuelerEmailPrivat", "mit E-Mail (privat)", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitSpalteSchuelerTelefonKontakte", "mit Telefonkontakten", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitErzieher", "mit Erziehern", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitErzieherAnschrift", "mit Erzieher-Anschrift", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitErzieherEmailPrivat", "mit Erzieher-E-Mail", ReportingVorlageParameterTyp.BOOLEAN, "false"))),

	/** Report-Vorlage: Klasse - Liste - Schüler - Leistungsdaten */
	KLASSEN_V_LISTE_SCHUELER_LEISTUNGSDATEN("Klasse-Liste-Schueler-Leistungsdaten", Arrays.asList(
			erzeugeVorlageParameter("mitPunktenStattNoten", "Punkte statt Noten ausgeben", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitQuartalsnote", "Quartalsnoten statt Noten ausgeben", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitIndividuellerKursart", "mit individueller Kursart", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitZuweisung", "mit Zuweisungen", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitGesamtfehlstunden", "mit Gesamtfehlstunden", ReportingVorlageParameterTyp.BOOLEAN, "true"),
			erzeugeVorlageParameter("mitFachbezogenenFehlstunden", "mit fachbezogenen Fehlstunden", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitFachbezogenenBemerkungen", "mit fachbezogenen Bemerkungen", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitLernentwicklung", "mit Angabe zur Lernentwicklung", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitFoerderschwerpunkt", "mit Angaben zum Förderschwerpunkt", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitASVBemerkung", "mit ASV-Bemerkungen", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitAUEBemerkung", "mit AUE-Bemerkungen", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitZeugnisbemerkung", "mit Zeugnisbemerkungen", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitSchulformempfehlung", "mit Empfehlung der Schulform", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitVersetzungAbschluss", "mit Versetzung und Abschluss", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitVersetzungsentscheidung", "mit Text zur Versetzungsentscheidung", ReportingVorlageParameterTyp.BOOLEAN, "false"))),

	/** Report-Vorlage: Kurs - Liste - Schüler - Kontaktdaten - Erzieher */
	KURSE_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER("Kurs-Liste-Schueler-Kontaktdaten-Erzieher", Arrays.asList(
			erzeugeVorlageParameter("mitSchuelerKlasse", "mit Klasse", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("nurSchuelerRufname", "nur Rufname", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitSchuelerGeschlecht", "mit Geschlecht", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitSchuelerGebDat", "mit Geburtsdatum", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitSchuelerStaat", "mit Staatsangehörigkeit", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitSchuelerAnschrift", "mit Anschrift", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitSchuelerTelefonPrivat", "mit Telefon (privat)", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitSchuelerEmailSchule", "mit E-Mail (Schule)", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitSchuelerEmailPrivat", "mit E-Mail (privat)", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitSpalteSchuelerTelefonKontakte", "mit Telefonkontakten", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitErzieher", "mit Erziehern", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitErzieherAnschrift", "mit Erzieher-Anschrift", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitErzieherEmailPrivat", "mit Erzieher-E-Mail", ReportingVorlageParameterTyp.BOOLEAN, "false"))),

	/** Report-Vorlage: Kurs - Liste - Schüler - Leistungsdaten */
	KURSE_V_LISTE_SCHUELER_LEISTUNGSDATEN("Kurs-Liste-Schueler-Leistungsdaten", Arrays.asList(
			erzeugeVorlageParameter("mitPunktenStattNoten", "Punkte statt Noten ausgeben", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitBemerkungen", "mit fachbezogenen Bemerkungen", ReportingVorlageParameterTyp.BOOLEAN, "false"))),

	/** Report-Vorlage: Lehrer - Liste - Schüler - Leistungsdaten */
	LEHRER_V_LISTE_SCHUELER_LEISTUNGSDATEN("Lehrer-Liste-Schueler-Leistungsdaten", Arrays.asList(
			erzeugeVorlageParameter("mitKlassenunterricht", "mit Klassenunterricht", ReportingVorlageParameterTyp.BOOLEAN, "true"),
			erzeugeVorlageParameter("mitKursunterricht", "mit Kursunterricht", ReportingVorlageParameterTyp.BOOLEAN, "true"),
			erzeugeVorlageParameter("mitPunktenStattNoten", "Punkte statt Noten ausgeben", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitBemerkungen", "mit fachbezogenen Bemerkungen", ReportingVorlageParameterTyp.BOOLEAN, "false"))),

	/** Report-Vorlage: Lehrer - Stammdaten - Liste */
	LEHRER_V_STAMMDATENLISTE("Lehrer-Stammdatenliste", new ArrayList<>()),

	/** Report-Vorlage: GOSt - Abitur - APO - Anlage 12 (Abiturzeugnis) - Din-A4 */
	SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A4("Schueler-GostAbiturApoAnlage12-A4", Arrays.asList(
			erzeugeVorlageParameter("mitPersoenlichenUnterschriften", "mit persönlichen Unterschriften", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitZweiterBeratungslehrerUnterschrift", "mit Unterschrift 2. Beratungslehrer", ReportingVorlageParameterTyp.BOOLEAN,
					"false"),
			erzeugeVorlageParameter("textZAAVorsitzUnterschrift", "Unterschrift ZAA-Vorsitz", ReportingVorlageParameterTyp.STRING, ""),
			erzeugeVorlageParameter("textZAAVorsitzUnterschriftBezeichnung", "Bezeichnung ZAA-Vorsitz", ReportingVorlageParameterTyp.STRING, ""),
			erzeugeVorlageParameter("textSchulleitungUnterschrift", "Unterschrift Schulleitung", ReportingVorlageParameterTyp.STRING, ""),
			erzeugeVorlageParameter("textSchulleitungUnterschriftBezeichnung", "Bezeichnung Schulleitung", ReportingVorlageParameterTyp.STRING, ""),
			erzeugeVorlageParameter("textSchultraegerUnterschrift", "Unterschrift Schulträger", ReportingVorlageParameterTyp.STRING, ""),
			erzeugeVorlageParameter("textSchultraegerUnterschriftBezeichnung", "Bezeichnung Schulträger", ReportingVorlageParameterTyp.STRING, ""),
			erzeugeVorlageParameter("textBeratungslehrerUnterschrift", "Unterschrift Beratungslehrer", ReportingVorlageParameterTyp.STRING, ""),
			erzeugeVorlageParameter("textBeratungslehrerUnterschriftBezeichnung", "Bezeichnung Beratungslehrer", ReportingVorlageParameterTyp.STRING, ""))),

	/** Report-Vorlage: GOSt - Abitur - APO - Anlage 12 (Abiturzeugnis) - Din-A3 */
	SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A3("Schueler-GostAbiturApoAnlage12-A3", Arrays.asList(
			erzeugeVorlageParameter("mitPersoenlichenUnterschriften", "mit persönlichen Unterschriften", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitZweiterBeratungslehrerUnterschrift", "mit Unterschrift 2. Beratungslehrer", ReportingVorlageParameterTyp.BOOLEAN,
					"false"),
			erzeugeVorlageParameter("textZAAVorsitzUnterschrift", "Unterschrift ZAA-Vorsitz", ReportingVorlageParameterTyp.STRING, ""),
			erzeugeVorlageParameter("textZAAVorsitzUnterschriftBezeichnung", "Bezeichnung ZAA-Vorsitz", ReportingVorlageParameterTyp.STRING, ""),
			erzeugeVorlageParameter("textSchulleitungUnterschrift", "Unterschrift Schulleitung", ReportingVorlageParameterTyp.STRING, ""),
			erzeugeVorlageParameter("textSchulleitungUnterschriftBezeichnung", "Bezeichnung Schulleitung", ReportingVorlageParameterTyp.STRING, ""),
			erzeugeVorlageParameter("textSchultraegerUnterschrift", "Unterschrift Schulträger", ReportingVorlageParameterTyp.STRING, ""),
			erzeugeVorlageParameter("textSchultraegerUnterschriftBezeichnung", "Bezeichnung Schulträger", ReportingVorlageParameterTyp.STRING, ""),
			erzeugeVorlageParameter("textBeratungslehrerUnterschrift", "Unterschrift Beratungslehrer", ReportingVorlageParameterTyp.STRING, ""),
			erzeugeVorlageParameter("textBeratungslehrerUnterschriftBezeichnung", "Bezeichnung Beratungslehrer", ReportingVorlageParameterTyp.STRING, ""))),

	/** Report-Vorlage: GOSt - Laufbahnplanung - Ergebnisübersicht */
	SCHUELER_V_GOST_LAUFBAHNPLANUNG_ERGEBNISUEBERSICHT("Schueler-GostLaufbahnplanungErgebnisuebersicht", Arrays.asList(
			erzeugeVorlageParameter("mitFehlernKommentaren", "mit Fehlern/Kommentaren", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitHinweisen", "mit Hinweisen", ReportingVorlageParameterTyp.BOOLEAN, "false"))),

	/** Report-Vorlage: GOSt - Laufbahnplanung - Wahlbogen */
	SCHUELER_V_GOST_LAUFBAHNPLANUNG_WAHLBOGEN("Schueler-GostLaufbahnplanungWahlbogen", Arrays.asList(
			erzeugeVorlageParameter("nurBelegteFaecher", "nur belegte Fächer", ReportingVorlageParameterTyp.BOOLEAN, "false"))),

	/** Report-Vorlage: Schüler - Schulbescheinigung */
	SCHUELER_V_SCHULBESCHEINIGUNG("Schueler-Schulbescheinigung", Arrays.asList(
			erzeugeVorlageParameter("fuerErzieher", "für Erzieher", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitBildBriefkopf", "mit Bild im Briefkopf", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitSchullogo", "mit Schullogo", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("keineAnschrift", "ohne Anschrift", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("keinInfoblock", "ohne Infoblock", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("keineUnterschrift", "ohne Unterschrift", ReportingVorlageParameterTyp.BOOLEAN, "false"))),

	/** Report-Vorlage: Schüler - Liste - Kontaktdaten - Erzieher */
	SCHUELER_V_LISTE_KONTAKTDATENERZIEHER("Schueler-Liste-Kontaktdaten-Erzieher", Arrays.asList(
			erzeugeVorlageParameter("mitSchuelerKlasse", "mit Klasse", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("nurSchuelerRufname", "nur Rufname", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitSchuelerGeschlecht", "mit Geschlecht", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitSchuelerGebDat", "mit Geburtsdatum", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitSchuelerStaat", "mit Staatsangehörigkeit", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitSchuelerAnschrift", "mit Anschrift", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitSchuelerTelefonPrivat", "mit Telefon (privat)", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitSchuelerEmailSchule", "mit E-Mail (Schule)", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitSchuelerEmailPrivat", "mit E-Mail (privat)", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitSpalteSchuelerTelefonKontakte", "mit Telefonkontakten", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitErzieher", "mit Erziehern", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitErzieherAnschrift", "mit Erzieher-Anschrift", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitErzieherEmailPrivat", "mit Erzieher-E-Mail", ReportingVorlageParameterTyp.BOOLEAN, "false"))),

	/** Report-Vorlage: Stundenplanung - Fach - Stundenplan */
	STUNDENPLANUNG_V_FACH_STUNDENPLAN("Stundenplanung-FachStundenplan", Arrays.asList(
			erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingVorlageParameterTyp.BOOLEAN, "false"))),

	/** Report-Vorlage: Stundenplanung - Klasse - Stundenplan */
	STUNDENPLANUNG_V_KLASSEN_STUNDENPLAN("Stundenplanung-KlassenStundenplan", Arrays.asList(
			erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitFachStattKursbezeichnung", "Fach statt Kursbezeichnung", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung", "Fachkürzel statt Fachbezeichnung", ReportingVorlageParameterTyp.BOOLEAN, "false"))),

	/** Report-Vorlage: Stundenplanung - Lehrer - Stundenplan */
	STUNDENPLANUNG_V_LEHRER_STUNDENPLAN("Stundenplanung-LehrerStundenplan", Arrays.asList(
			erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitPausenaufsichten", "mit Pausenaufsichten", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung", "Fachkürzel statt Fachbezeichnung", ReportingVorlageParameterTyp.BOOLEAN, "false"))),

	/** Report-Vorlage: Stundenplanung - Lehrer - Stundenplan - Kombiniert */
	STUNDENPLANUNG_V_LEHRER_STUNDENPLAN_KOMBINIERT("Stundenplanung-LehrerStundenplanKombiniert", Arrays.asList(
			erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitPausenaufsichten", "mit Pausenaufsichten", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung", "Fachkürzel statt Fachbezeichnung", ReportingVorlageParameterTyp.BOOLEAN, "false"))),

	/** Report-Vorlage: Stundenplanung - Fach - Stundenplan */
	STUNDENPLANUNG_V_RAUM_STUNDENPLAN("Stundenplanung-RaumStundenplan", Arrays.asList(
			erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingVorlageParameterTyp.BOOLEAN, "false"))),

	/** Report-Vorlage: Stundenplanung - Schüler - Stundenplan */
	STUNDENPLANUNG_V_SCHUELER_STUNDENPLAN("Stundenplanung-SchuelerStundenplan", Arrays.asList(
			erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitFachStattKursbezeichnung", "Fach statt Kursbezeichnung", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung", "Fachkürzel statt Fachbezeichnung", ReportingVorlageParameterTyp.BOOLEAN, "false"),
			erzeugeVorlageParameter("mitIndividuelleKursart", "mit individueller Kursart", ReportingVorlageParameterTyp.BOOLEAN, "false")));


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
	private static @NotNull ReportingVorlageParameter erzeugeVorlageParameter(final @NotNull String name, final @NotNull String bezeichnung,
			final @NotNull ReportingVorlageParameterTyp typ,
			final @NotNull String wert) {
		final ReportingVorlageParameter reportingVorlageParameter = new ReportingVorlageParameter();
		reportingVorlageParameter.name = name;
		reportingVorlageParameter.bezeichnung = bezeichnung;
		reportingVorlageParameter.typ = typ.getId();
		reportingVorlageParameter.wert = wert;
		return reportingVorlageParameter;
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
