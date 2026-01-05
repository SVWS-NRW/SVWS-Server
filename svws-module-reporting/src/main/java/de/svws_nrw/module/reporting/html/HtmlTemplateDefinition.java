package de.svws_nrw.module.reporting.html;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.base.ResourceUtils;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.core.types.reporting.ReportingReportvorlage;

/**
 * <p>Eine ENUM der integrierten Report-Vorlagen des SVWS-Servers.
 * Sie definiert die im CoreType {@link ReportingReportvorlage} angelegten Report-Vorlagen
 * hinsichtlich der verwendeten Template-Dateien und Benutzerrechte genauer.</p>
 * <p>Hinweise:</p>
 * <p>Die Bezeichnungen der ENUM-Werte dienen auch dazu, die Daten-Contexts korrekt zu füllen.
 * Die Benennung der Vorlagen erfolgt nach dem Schema Hauptdaten_V_Detaildaten. Bei der Report-Generierung erfolgt in
 * Teilen ein entsprechendes Füllen der Datenkontexte anhand der Benennung.</p>
 * <p>Jede Templatedefinition hat eine Pfadangabe für den Root-Pfad und eine zur HTML-Templatedatei. Letztere Angabe hat relativ zum Root zu erfolgen.
 * Unter dem Root müssen alle weiteren Dateien zum Template zu finden sein, bspw. die CSS-Dateien.
 * Innerhalb der HTML-Vorlagendatei sind alle Pfade relativ zum Root anzugeben.</p>
 * <p>Es gibt zwei Einträge für die Erzeugung des Dateinamens. Einen "statischen" Dateinamen und eine Dateinamensvorlage.
 * Die Vorlage stellt den BODY eines HTML-Thymeleaf-Templates dar, welches eine einzige Zeile mit einem Absatz-Tag
 * ergeben muss. Diese Zeile wird dann für die Erzeugung des Dateinamens aus den Daten genutzt.</p>
 */
public enum HtmlTemplateDefinition {

	/** Report-Vorlage: GOSt - Klausurplanung - Klausurtermine - Kurse */
	GOST_KLAUSURPLANUNG_V_KLAUSURTERMINE_MIT_KURSEN(
			HauptdatenContextDefinition.GOST_KLAUSURPLANUNG,
			"gost/klausurplanung/GostKlausurplanungKlausurtermineMitKursen.html",
			"GOSt-Klausurplanung-Klausurtermine-Kurse",
			List.of(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN,
					BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION)),

	/** Report-Vorlage: GOSt - Klausurplanung - Schueler - Klausuren */
	GOST_KLAUSURPLANUNG_V_SCHUELER_MIT_KLAUSUREN(
			HauptdatenContextDefinition.GOST_KLAUSURPLANUNG,
			"gost/klausurplanung/GostKlausurplanungSchuelerMitKlausuren.html",
			"GOSt-Klausurplanung-Schueler-Klausuren",
			List.of(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN,
					BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION)),

	/** Report-Vorlage: GOSt - Kursplanung - Kurs - Kurschüler */
	GOST_KURSPLANUNG_V_KURS_MIT_KURSSCHUELERN(
			HauptdatenContextDefinition.GOST_KURSPLANUNG,
			"gost/kursplanung/GostKursplanungKursMitKursschuelern.html",
			"GOSt-Blockungsergebnis-Kurs-Schueler",
			List.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
					BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)),

	/** Report-Vorlage: GOSt - Kursplanung - Kurse - Statistikwerte */
	GOST_KURSPLANUNG_V_KURSE_MIT_STATISTIKWERTEN(
			HauptdatenContextDefinition.GOST_KURSPLANUNG,
			"gost/kursplanung/GostKursplanungKurseMitStatistikwerten.html",
			"GOSt-Blockungsergebnis-Kurse-Statistikwerte",
			List.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
					BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)),

	/** Report-Vorlage: GOSt - Kursplanung - Schüler - Kurse */
	GOST_KURSPLANUNG_V_SCHUELER_MIT_KURSEN(
			HauptdatenContextDefinition.GOST_KURSPLANUNG,
			"gost/kursplanung/GostKursplanungSchuelerMitKursen.html",
			"GOSt-Blockungsergebnis-Schueler-Kurse",
			List.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
					BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)),

	/** Report-Vorlage: GOSt - Kursplanung - Schüler - Schienen-Kurse */
	GOST_KURSPLANUNG_V_SCHUELER_MIT_SCHIENEN_KURSEN(
			HauptdatenContextDefinition.GOST_KURSPLANUNG,
			"gost/kursplanung/GostKursplanungSchuelerMitSchienenKursen.html",
			"GOSt-Blockungsergebnis-Schueler-Schienen-Kurse",
			List.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
					BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)),

	/** Report-Vorlage: GOSt - Laufbahnplanung - Abiturjahrgang - Fachwahlstatistiken */
	GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG_V_FACHWAHLSTATISTIKEN(
			HauptdatenContextDefinition.GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG,
			"gost/laufbahnplanung/GostLaufbahnplanungAbiturjahrgangFachwahlstatistiken.html",
			"GOSt-Laufbahnplanung-Abiturjahrgang-Fachwahlstatistiken",
			List.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
					BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)),

	/** Report-Vorlage: Klasse - Liste - Schüler - Kontaktdaten - Erzieher */
	KLASSEN_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER(
			HauptdatenContextDefinition.KLASSEN,
			"klassen/KlasseListeSchuelerKontaktdatenErzieher.html",
			"Klasse-Liste-Schueler-Kontaktdaten-Erzieher",
			List.of(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN)),

	/** Report-Vorlage: Kurs - Liste - Schüler - Kontaktdaten - Erzieher */
	KURSE_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER(
			HauptdatenContextDefinition.KURSE,
			"kurse/KursListeSchuelerKontaktdatenErzieher.html",
			"Kurs-Liste-Schueler-Kontaktdaten-Erzieher",
			List.of(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN)),

	/** Report-Vorlage: Klasse - Liste - Schüler - Leistungsdaten */
	KLASSEN_V_LISTE_SCHUELER_LEISTUNGSDATEN(
			HauptdatenContextDefinition.KLASSEN,
			"klassen/leistungsdaten/KlasseListeSchuelerLeistungsdaten.html",
			"Klassen-Liste-Schueler-Leistungsdaten",
			List.of(BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN)),

	/** Report-Vorlage: Kurs - Liste - Schüler - Leistungsdaten */
	KURSE_V_LISTE_SCHUELER_LEISTUNGSDATEN(
			HauptdatenContextDefinition.KURSE,
			"kurse/leistungsdaten/KursListeSchuelerLeistungsdaten.html",
			"Kurs-Liste-Schueler-Leistungsdaten",
			List.of(BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN)),

	/** Report-Vorlage: Lehrer - Liste - Schüler - Leistungsdaten */
	LEHRER_V_LISTE_SCHUELER_LEISTUNGSDATEN(
			HauptdatenContextDefinition.LEHRER,
			"lehrer/leistungsdaten/LehrerListeSchuelerLeistungsdaten.html",
			"Lehrer-Liste-Schueler-Leistungsdaten",
			List.of(BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN)),

	/** Report-Vorlage: Lehrer - Stammdaten - Liste */
	LEHRER_V_STAMMDATENLISTE(
			HauptdatenContextDefinition.LEHRER,
			"lehrer/stammdaten/LehrerStammdatenliste.html",
			"Lehrer-Stammdatenliste",
			List.of(BenutzerKompetenz.LEHRERDATEN_ANSEHEN)),

	/** Report-Vorlage: Schüler - GOSt - Abitur - APO - Anlage 12 (Abiturzeugnis) - Din-A4 */
	SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A4(
			HauptdatenContextDefinition.SCHUELER,
			"schueler/gost/abitur/apo/SchuelerGostAbiturApoAnlage12-A4.html",
			"APO-GOSt-Anlage12",
			List.of(BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN, BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN)),


	/** Report-Vorlage: Schüler - GOSt - Abitur - APO - Anlage 12 (Abiturzeugnis) - Din-A3 */
	SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A3(
			HauptdatenContextDefinition.SCHUELER,
			"schueler/gost/abitur/apo/SchuelerGostAbiturApoAnlage12-A3.html",
			"APO-GOSt-Anlage12",
			List.of(BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN, BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN)),

	/** Report-Vorlage: Schüler - GOSt - Laufbahnplanung - Ergebnisübersicht */
	SCHUELER_V_GOST_LAUFBAHNPLANUNG_ERGEBNISUEBERSICHT(
			HauptdatenContextDefinition.SCHUELER,
			"schueler/gost/laufbahnplanung/SchuelerGostLaufbahnplanungErgebnisuebersicht.html",
			"GOSt-Laufbahnplanung-Pruefungsergebnisse",
			List.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
					BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)),

	/** Report-Vorlage: Schüler - GOSt - Laufbahnplanung - Wahlbogen */
	SCHUELER_V_GOST_LAUFBAHNPLANUNG_WAHLBOGEN(
			HauptdatenContextDefinition.SCHUELER,
			"schueler/gost/laufbahnplanung/SchuelerGostLaufbahnplanungWahlbogen.html",
			"GOSt-Laufbahnplanung-Wahlboegen",
			List.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
					BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)),

	/** Report-Vorlage: Schüler - Schulbescheinigung */
	SCHUELER_V_SCHULBESCHEINIGUNG(
			HauptdatenContextDefinition.SCHUELER,
			"schueler/anschreiben/SchuelerSchulbescheinigung.html",
			"Schueler-Schulbescheinigung",
			List.of(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN)),

	/** Report-Vorlage: Schüler - Stammdaten - Liste */
	SCHUELER_V_LISTE_KONTAKTDATENERZIEHER(
			HauptdatenContextDefinition.SCHUELER,
			"schueler/listen/SchuelerListeKontaktdatenErzieher.html",
			"Schueler-Liste-Kontaktdaten-Erzieher",
			List.of(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN)),

	/** Report-Vorlage: Stundenplanung - Fach - Stundenplan */
	STUNDENPLANUNG_V_FACH_STUNDENPLAN(
			HauptdatenContextDefinition.STUNDENPLANUNG,
			"stundenplanung/StundenplanungFachStundenplan.html",
			"Fach-Stundenplan",
			List.of(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN)),

	/** Report-Vorlage: Stundenplanung - Klasse - Stundenplan */
	STUNDENPLANUNG_V_KLASSEN_STUNDENPLAN(
			HauptdatenContextDefinition.STUNDENPLANUNG,
			"stundenplanung/StundenplanungKlassenStundenplan.html",
			"Klassen-Stundenplan",
			List.of(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN)),

	/** Report-Vorlage: Stundenplanung - Lehrer - Stundenplan */
	STUNDENPLANUNG_V_LEHRER_STUNDENPLAN(
			HauptdatenContextDefinition.STUNDENPLANUNG,
			"stundenplanung/StundenplanungLehrerStundenplan.html",
			"Lehrer-Stundenplan",
			List.of(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN)),

	/** Report-Vorlage: Stundenplanung - Lehrer - Stundenplan */
	STUNDENPLANUNG_V_LEHRER_STUNDENPLAN_KOMBINIERT(
			HauptdatenContextDefinition.STUNDENPLANUNG,
			"stundenplanung/StundenplanungLehrerStundenplanKombiniert.html",
			"Lehrer-Stundenplan-Kombiniert",
			List.of(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN)),

	/** Report-Vorlage: Stundenplanung - Raum - Stundenplan */
	STUNDENPLANUNG_V_RAUM_STUNDENPLAN(
			HauptdatenContextDefinition.STUNDENPLANUNG,
			"stundenplanung/StundenplanungRaumStundenplan.html",
			"Raum-Stundenplan",
			List.of(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN)),

	/** Report-Vorlage: Stundenplanung - Schüler - Stundenplan */
	STUNDENPLANUNG_V_SCHUELER_STUNDENPLAN(
			HauptdatenContextDefinition.STUNDENPLANUNG,
			"stundenplanung/StundenplanungSchuelerStundenplan.html",
			"Schueler-Stundenplan",
			List.of(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN));

	/** Der Root-Pfad zum Verzeichnis, unter dem alle Dateien dieser HtmlTemplateDefinition (html, css, fonts) auffindbar sind. */
	private static final String ROOT_PATH = "de/svws_nrw/module/reporting/";

	/** Eine Map zum schnellen Nachschlagen der Template-Definition anhand der Report-Vorlage. */
	private static final Map<ReportingReportvorlage, HtmlTemplateDefinition> mapReportingReportvorlage = new EnumMap<>(ReportingReportvorlage.class);

	static {
		for (final HtmlTemplateDefinition td : HtmlTemplateDefinition.values()) {
			mapReportingReportvorlage.put(td.reportingReportvorlage, td);
		}
	}

	/** Der CoreType {@link ReportingReportvorlage} dieser Template-Definition. */
	private final ReportingReportvorlage reportingReportvorlage;

	/** Der Hauptdaten-Context, der für die HTML-Template-Datei verwendet wird. */
	private final HauptdatenContextDefinition hauptdatenContextDefinition;

	/** Pfad zur HTML-Template-Datei. Angabe erfolgt relativ zum Root-Pfad. */
	private final String pfadHtmlTemplate;

	/** Der statische Dateiname ohne Dateiendung, der bei der Ausgabe als ZIP-Datei verwendet wird. */
	private final String dateiname;

	/** Die Vorlage für dynamische Generierung des Dateinamens ohne Dateiendung. Sie ist in der Form eines thymeleaf-HTML-Templates anzulegen. */
	private final String dateinamensvorlage;

	/** Die List mit Benutzerkompetenzen gemäß {@link BenutzerKompetenz}, die zur Nutzung des Templates erforderlich sind. */
	private final List<BenutzerKompetenz> benutzerKompetenzen;

	/**
	 * Erstellt eine neue Template-Definition.
	 *
	 * @param hauptdatenContextDefinition Der Hauptdaten-Context, der für die HTML-Template-Datei verwendet wird.
	 * @param pfadHtmlTemplate 			  Pfad zur HTML-Template-Datei. Angabe erfolgt relativ zum Root-Pfad.
	 * @param dateiname 				  Der statische Dateiname ohne Dateiendung, der unter anderem bei der Ausgabe als ZIP-Datei verwendet wird.
	 * @param benutzerKompetenzen 		  Die List mit Benutzerkompetenzen gemäß {@link BenutzerKompetenz}, die zur Nutzung des Templates erforderlich sind.
	 *									  Die Vorlage für die dynamische Dateinamensgenerierung wird automatisch aus der HTML-Template-Datei geladen.
	 */
	HtmlTemplateDefinition(final HauptdatenContextDefinition hauptdatenContextDefinition, final String pfadHtmlTemplate, final String dateiname,
			final List<BenutzerKompetenz> benutzerKompetenzen) {
		this.hauptdatenContextDefinition = hauptdatenContextDefinition;
		this.reportingReportvorlage = getReportingReportvorlage();
		this.pfadHtmlTemplate = pfadHtmlTemplate;
		this.dateiname = dateiname;
		this.dateinamensvorlage = ladeDateinamensvorlageAusDatei(pfadHtmlTemplate);
		this.benutzerKompetenzen = benutzerKompetenzen;
	}

	/**
	 * Lädt die Vorlage für den Dateinamen aus einer ".filename.name.tpl" Datei.
	 *
	 * @param pfadHtmlTemplate Der Pfad zum HTML-Template
	 * @return Die geladene Vorlage oder leerer String bei einem Fehler.
	 */
	private static String ladeDateinamensvorlageAusDatei(final String pfadHtmlTemplate) {
		// Erstelle Pfad analog zum HTML-Template
		final String tplPfad = pfadHtmlTemplate.replace(".html", ".name.tpl");
		final String vollPfad = ROOT_PATH + tplPfad;
		try {
			final String content = ResourceUtils.text(vollPfad);
			if (content == null)
				return "";
			return content;
		} catch (final Exception e) {
			return "";
		}
	}

	/**
	 * Gibt den statischen Root-Pfad zurück.
	 *
	 * @return der Root-Pfad für alle Reporting-Templates
	 */
	public static String getRootPfad() {
		return ROOT_PATH;
	}

	/**
	 * Liefert die {@link ReportingReportvorlage}, der dieser Template-Definition zugeordnet ist.
	 * Die Methode verwendet den Namen der Enum-Konstanten dieser Klasse, um den entsprechenden {@link ReportingReportvorlage}-Wert zu bestimmen.
	 *
	 * @return der CoreType {@link ReportingReportvorlage} dieser Template-Definition
	 */
	private ReportingReportvorlage getReportingReportvorlage() {
		return ReportingReportvorlage.getByName(this.name());
	}

	/**
	 * Liefert den Hauptdaten-Context der aktuellen HTML-Template-Definition.
	 *
	 * @return Der Hauptdaten-Context
	 */
	public HauptdatenContextDefinition getHauptdatenContextDefinition() {
		return this.hauptdatenContextDefinition;
	}

	/**
	 * Pfad zur HTML-Template-Datei. Angabe erfolgt relativ zum Root-Pfad.
	 *
	 * @return Der Dateipfad zur HTML-Template-Datei
	 */
	public String getPfadHtmlTemplate() {
		return this.pfadHtmlTemplate;
	}

	/**
	 * Pfad zur HTML-Template-Datei, inklusive des Root-Pfads der HtmlTemplateDefinition.
	 *
	 * @return Der Root-Dateipfad zur HTML-Template-Datei
	 */
	public String getRootPfadHtmlTemplate() {
		return ROOT_PATH + this.pfadHtmlTemplate;
	}

	/**
	 * Gibt den statischen Dateinamen ohne Dateiendung zurück, der unter anderem bei der Ausgabe als ZIP-Datei verwendet wird.
	 *
	 * @return Der statische Dateiname
	 */
	public String getDateiname() {
		return this.dateiname;
	}

	/**
	 * Gibt die Vorlage für die dynamische Generierung des Dateinamens ohne Dateiendung zurück.
	 *
	 * @return Die Vorlage für den Dateinamen
	 */
	public String getDateinamensvorlage() {
		return this.dateinamensvorlage;
	}

	/**
	 * Gibt die Benutzer-Kompetenzen für diese Template-Definition zurück
	 *
	 * @return Die Liste der Benutzerkompetenzen
	 */
	public List<BenutzerKompetenz> getBenutzerKompetenzen() {
		return this.benutzerKompetenzen;
	}

	/**
	 * Diese Methode ermittelt die HtmlTemplateDefinition anhand der Reportvorlage.
	 *
	 * @param reportingReportvorlage  Der CoreType der Report-Vorlage
	 *
	 * @return Die Template-Definition
	 */
	public static HtmlTemplateDefinition getByReportvorlage(final ReportingReportvorlage reportingReportvorlage) {
		return mapReportingReportvorlage.get(reportingReportvorlage);
	}

}
