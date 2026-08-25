package de.svws_nrw.module.reporting.factories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.svws_nrw.base.ResourceUtils;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenzGruppe;
import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.core.types.reporting.ReportingReportvorlageDatenContext;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.builders.ReportBuilderHtml;
import de.svws_nrw.module.reporting.builders.ReportBuilderContextHtml;
import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextBasisdaten;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextAufteilbar;
import de.svws_nrw.module.reporting.html.contexts.initializer.HtmlContextAufbau;
import de.svws_nrw.module.reporting.html.contexts.initializer.HtmlContextInitializer;
import de.svws_nrw.module.reporting.html.contexts.initializer.HtmlContextInitializerRegistry;
import de.svws_nrw.module.reporting.html.contexts.initializer.HtmlContextSchluessel;
import de.svws_nrw.module.reporting.parameter.ReportingParameterTypisiert;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * <p>Diese Klasse erstellt HTML-Inhalte auf Basis des in den Reporting-Parametern übergebenen HTML-Templates und der übergebenen Daten.</p>
 * <p>Dabei erstellt die Factory bei der Initialisierung zunächst die Contexts mit den Daten gemäß dem HTML-Template.
 * Zum Erstellen der HTML-Inhalte generiert die Factory einen oder mehrere HTML-Builder, die aus dem Template das fertige HTML erzeugen.</p>
 * <p>Die HTML-Builder können extern weiter verarbeitet werden oder es kann intern eine Response im HTML-Format erzeugt werden.</p>
 */
public final class HtmlFactory {

	/** Context mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	private final ReportingContext reportingContext;

	/** Einstellungen und Daten zum Steuern der Report-Generierung. */
	private final ReportingParameterTypisiert reportingParameter;

	/** Reporting-Reportvorlage für die Erstellung der HTML-Datei. */
	private final ReportingReportvorlage reportingReportvorlage;

	/** Eine Map zum Sammeln der erstellten HTML-Contexts. */
	private final Map<String, HtmlContext<?>> mapHtmlContexts = new HashMap<>();

	/** Der Initializer, der die Daten-Contexts aufgebaut hat. */
	private HtmlContextInitializer htmlContextInitializer;


	/**
	 * Erzeugt eine einsatzbereite HTML-Factory: Sie prüft die Vorlage und die Benutzerrechte und baut anschließend die Daten-Contexts auf.
	 * <p>Der Aufbau der Daten-Contexts liegt bewusst hier und nicht im Konstruktor, damit kein halb fertiges Objekt entstehen kann. Weil der Konstruktor
	 * privat ist, ist jede erreichbare {@code HtmlFactory} vollständig initialisiert.</p>
	 *
	 * @param reportingContext Repository für das Reporting, welches verschiedene Daten aus der Datenbank zwischenspeichert.
	 *
	 * @return Die einsatzbereite HTML-Factory.
	 *
	 * @throws ApiOperationException    Bei einem Abbruch; die Exception trägt den Abbruchgrund als Meldung.
	 */
	static HtmlFactory erzeuge(final ReportingContext reportingContext) throws ApiOperationException {
		final HtmlFactory htmlFactory = new HtmlFactory(reportingContext);
		htmlFactory.erzeugeContexts();
		return htmlFactory;
	}


	/**
	 * Erzeugt eine neue HTML-Factory, um eine HTML-Datei aus einem HTML-Template zu erzeugen.
	 *
	 * @param reportingContext        Repository für das Reporting, welches verschiedene Daten aus der Datenbank zwischenspeichert.
	 *
	 * @throws ApiOperationException    Bei einem Abbruch; die Exception trägt den Abbruchgrund als Meldung.
	 */
	private HtmlFactory(final ReportingContext reportingContext)
			throws ApiOperationException {

		this.reportingContext = reportingContext;
		this.reportingParameter = this.reportingContext.reportingParameter();

		this.reportingContext.logger().logLn(LogLevel.DEBUG, 0,
				">>> Beginn der Initialisierung der HTML-Factory und der Validierung der übergebenen Daten.");

		// Validiere die Angaben zur HTML-Vorlage.
		this.reportingReportvorlage = this.reportingParameter.reportVorlage();
		if (this.reportingReportvorlage == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "### FEHLER: Für den Report konnte keine gültige Vorlage ermittelt werden.");
		}

		// Prüfe, ob die Rechte des Benutzers zu den in der TemplateDefinition hinterlegten Rechten passen.
		this.reportingContext.logger().logLn(LogLevel.DEBUG, 4,
				"Prüfe die Berechtigungen des Benutzers für den Zugriff auf die für die Ausgabe notwendigen Daten.");
		if (!this.reportingContext.benutzer().pruefeKompetenz(reportingReportvorlage.getBenutzerKompetenzen())) {
			throw new ApiOperationException(Status.FORBIDDEN,
					"### FEHLER: Für die Reportvorlage '%s' fehlt die Berechtigung; erforderlich ist mindestens eine dieser Kompetenzen: %s."
							.formatted(reportingReportvorlage.getBezeichnung(), benannteKompetenzen(reportingReportvorlage)));
		}

		this.reportingContext.logger().logLn(LogLevel.DEBUG, 0, "<<< Ende der Initialisierung der HTML-Factory und der Validierung der übergebenen Daten.");
	}


	/**
	 * Erzeugte die notwendigen Contexts für die HTML-Erstellung auf Basis des angegebenen HTML-Templates.
	 *
	 * @throws ApiOperationException    Bei einem Abbruch; die Exception trägt den Abbruchgrund als Meldung.
	 */
	private void erzeugeContexts() throws ApiOperationException {

		reportingContext.logger().logLn(LogLevel.DEBUG, 0, ">>> Beginn der Erzeugung der Datenkontexte für die HTML-Generierung.");
		reportingContext.logger().logLn(LogLevel.DEBUG, 4, "Erzeuge Datenkontext Schule für die HTML-Generierung.");

		final HtmlContextBasisdaten htmlContextBasisdaten = new HtmlContextBasisdaten(reportingContext);
		mapHtmlContexts.put(HtmlContextSchluessel.BASISDATEN, htmlContextBasisdaten);

		// Betrachte die HTML-Template-Definition und erzeuge damit die korrekten Contexts der Hauptdaten
		initContextUeberRegistry(reportingReportvorlage.getReportingReportvorlageDatenContext());

		reportingContext.logger().logLn(LogLevel.DEBUG, 0, "<<< Ende der Erzeugung der Datenkontexte für die HTML-Generierung.");
	}

	/**
	 * Baut die Daten-Contexts über den in der Registry hinterlegten Initializer des Datenaufbaus auf und hält den Initializer für die Einzelausgabe fest.
	 * Nach dem Aufbau muss der Ausgabeumfang gemeldet sein - er ist die Schranke, die jeden Datenaufbau an den Hinweisvertrag bindet: Ohne ihn gäbe es
	 * einen Header, dessen Zählwerte niemand ermittelt hat.
	 *
	 * @param datenContext Der Datenaufbau der Reportvorlage.
	 *
	 * @throws ApiOperationException    Bei einem Abbruch; die Exception trägt den Abbruchgrund als Meldung.
	 */
	private void initContextUeberRegistry(final ReportingReportvorlageDatenContext datenContext) throws ApiOperationException {
		final HtmlContextAufbau aufbau = HtmlContextInitializerRegistry.aufbau(reportingContext, datenContext);
		this.htmlContextInitializer = aufbau.initializer(reportingContext, mapHtmlContexts);
		this.htmlContextInitializer.init();
		if (reportingContext.ausgabeumfang() == null) {
			// Welcher Datenaufbau die Meldung schuldig blieb, steht in keiner Ursachenkette und benennt den Programmfehler erst.
			reportingContext.logger().logLn(LogLevel.ERROR, 4, "Datenaufbau %s, vorgesehene Meldestelle des Ausgabeumfangs: %s."
					.formatted(datenContext.name(), this.htmlContextInitializer.meldetAusgabeumfangImContextAufbau() ? "Context-Aufbau" : "Initializer"));
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "### FEHLER: Für den Report wurde kein Ausgabeumfang ermittelt.");
		}
	}

	/**
	 * Erzeugt auf Basis des gegebenen HTML-Templates und der übergebenen Daten die HTML-Builder, aus denen die HTML-Inhalte erzeugt werden können.
	 *
	 * @return Eine Liste mit ReportBuilderHtml-Instanzen.
	 *
	 * @throws ApiOperationException    Bei einem Abbruch; die Exception trägt den Abbruchgrund als Meldung.
	 */
	protected List<ReportBuilderHtml> createHtmlBuilders() throws ApiOperationException {
		return getHtmlBuilders();
	}


	/**
	 * Erstellt eine Response in Form einer einzelnen HTML-Datei, die für die Anzeige in einem Browser verwendet werden kann.
	 *
	 * @return Im Falle eines Success enthält die HTTP-Response das HTML-Dokument.
	 *
	 * @throws ApiOperationException    Bei einem Abbruch; die Exception trägt den Abbruchgrund als Meldung.
	 */
	protected Response createHtmlResponse() throws ApiOperationException {
		reportingContext.logger().logLn(LogLevel.DEBUG, 0, ">>> Beginn der Erzeugung der Response einer API-Anfrage für eine HTML-Generierung.");
		final List<ReportBuilderHtml> htmlBuilders = getHtmlBuilders();
		if (htmlBuilders.isEmpty()) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "### FEHLER: Für die HTML-Ausgabe ist kein Dokument entstanden.");
		}
		if (htmlBuilders.size() > 1) {
			// Reine Absicherung: Der Zweig ist unerreichbar, seit ReportingParameterBuilder die Aufteilung in Einzeldateien für die HTML-Ausgabe auf dem
			// fertig kombinierten Parametersatz auf false festlegt - ohne Aufteilung entsteht genau ein Builder.
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "### FEHLER: Für die HTML-Ausgabe ist mehr als ein Dokument entstanden.");
		}

		final String html = htmlBuilders.getFirst().generate();
		reportingContext.logger().logLn(LogLevel.DEBUG, 0, "<<< Ende der Erzeugung der Response einer API-Anfrage für eine HTML-Generierung.");
		// HTML bildet keinen Sonderpfad: Es trägt denselben Hinweis-Header wie PDF und ZIP. Dass der heutige generierte Client die Response-Metadaten
		// verwirft und ihn deshalb nicht anzeigt, ändert am Serververtrag nichts.
		return ReportingHinweiseHeader
				.ergaenze(Response.ok(html, "text/html; charset=UTF-8").header("Cache-Control", "no-store"), reportingContext)
				.build();
	}


	/**
	 * Gibt die Kompetenzen einer Reportvorlage als lesbare Aufzählung zurück, jeweils mit ihrer Gruppe.
	 * <p>Die Bezeichnung allein genügt nicht: Mehrere Kompetenzen heißen schlicht "Ansehen" und wären ohne ihre Gruppe nicht zuzuordnen. Ist die Gruppe
	 * nicht auflösbar, bleibt es bei der Bezeichnung - eine unvollständige Auskunft ist besser als keine.</p>
	 *
	 * @param reportvorlage Die Reportvorlage, deren Kompetenzen aufgezählt werden.
	 *
	 * @return Die Kompetenzen, durch Komma getrennt.
	 */
	private static String benannteKompetenzen(final ReportingReportvorlage reportvorlage) {
		return String.join(", ", reportvorlage.getBenutzerKompetenzen().stream().map(kompetenz -> {
			final BenutzerKompetenzGruppe gruppe = BenutzerKompetenzGruppe.getByID(kompetenz.daten.gruppe_id);
			return (gruppe == null) ? kompetenz.daten.bezeichnung : (gruppe.daten.bezeichnung + ": " + kompetenz.daten.bezeichnung);
		}).toList());
	}

	/**
	 * Erzeugt auf Basis der übergebenen HTML-Vorlage und Daten die HTML-Inhalte der Dateien und legt diese Inhalte in einer Liste ab.
	 *
	 * @return Eine Liste mit ReportBuilderHtml-Instanzen.
	 *
	 * @throws ApiOperationException Bei einem Abbruch; die Exception trägt den Abbruchgrund als Meldung.
	 */
	private List<ReportBuilderHtml> getHtmlBuilders() throws ApiOperationException {

		reportingContext.logger().logLn(LogLevel.DEBUG, 0, ">>> Beginn der Erzeugung der HTML-Builder.");

		// Lade den Inhalt des HTML-Codes aus dem Template. Eine leere Datei wird hier mitgeprüft: Sie ist als Vorlage ebenso unbrauchbar wie eine nicht
		// lesbare und ist als interne Ressource ein Serverfehler. Ohne die Prüfung lehnte sie erst der Builder-Kontext ab - mit falschem Status.
		final String htmlTemplateCode = ResourceUtils.text(reportingReportvorlage.getRootPfadHtmlTemplate());
		if ((htmlTemplateCode == null) || htmlTemplateCode.isBlank()) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"### FEHLER: Die HTML-Vorlage für den Report '%s' konnte auf dem Server nicht gelesen werden oder ist leer."
							.formatted(reportingReportvorlage.getBezeichnung()));
		}

		final List<ReportBuilderHtml> htmlBuilders = new ArrayList<>();

		if (reportingParameter.einzelausgabeDaten()) {
			erzeugeEinzelContexts(htmlBuilders, htmlTemplateCode);
		} else {
			htmlBuilders.add(getReportBuilderHtml(htmlTemplateCode));
		}
		reportingContext.logger().logLn(LogLevel.DEBUG, 0, "<<< Ende der Erzeugung der HTML-Builder.");
		return htmlBuilders;
	}

	/**
	 * Erstellt einzelne Haupt-Kontexte auf Basis der gegebenen Hauptdatenquelle, um separate HTML-Dateien zu generieren.
	 *
	 * @param htmlBuilders     Eine Liste von {@code ReportBuilderHtml}-Objekten, in die die erzeugten HTML-Inhalte gespeichert werden.
	 * @param htmlTemplateCode Der HTML-Template-Code, der beim Generieren der HTML-Inhalte verwendet wird.
	 *
	 * @throws ApiOperationException Bei einem Abbruch; die Exception trägt den Abbruchgrund als Meldung.
	 */
	private void erzeugeEinzelContexts(final List<ReportBuilderHtml> htmlBuilders, final String htmlTemplateCode) throws ApiOperationException {
		final String contextBezeichnung = htmlContextInitializer.einzelContextBezeichnung();
		final HtmlContext<?> baseContext = mapHtmlContexts.get(contextBezeichnung);

		// Ein fehlender Context und ein Context ohne Aufteilungsfähigkeit sind verschiedene Fehler und brauchen verschiedene Meldungen: Die Typprüfung unten
		// kann beides nicht unterscheiden und würde einen fehlenden Context als fehlende Fähigkeit ausweisen. Beide Fälle setzen eine fehlkonfigurierte
		// Registry voraus.
		if (baseContext == null) {
			// Der Schlüssel benennt die fehlerhafte Zuordnung in der Registry und steht in keiner Ursachenkette.
			reportingContext.logger().logLn(LogLevel.ERROR, 4, "Nicht aufgebauter Daten-Context der Einzelausgabe: " + contextBezeichnung);
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "### FEHLER: Für die Ausgabe in einzelne Dateien fehlen die aufbereiteten Daten.");
		}

		if (baseContext instanceof final HtmlContextAufteilbar<?> aufteilbarerContext) {
			reportingContext.logger().logLn(LogLevel.DEBUG, 4,
					"Erzeuge einzelne Kontexte für " + contextBezeichnung + " für Template " + reportingReportvorlage.name());

			final List<? extends HtmlContext<?>> einzelContexts = aufteilbarerContext.getEinzelContexts();

			for (final HtmlContext<?> einzelContext : einzelContexts) {
				mapHtmlContexts.put(contextBezeichnung, einzelContext);
				htmlBuilders.add(getReportBuilderHtml(htmlTemplateCode));
			}
		} else {
			// Der Schlüssel und die tatsächliche Klasse benennen die fehlerhafte Zuordnung in der Registry und stehen in keiner Ursachenkette.
			reportingContext.logger().logLn(LogLevel.ERROR, 4, "Nicht aufteilbarer Daten-Context der Einzelausgabe: %s (%s)"
					.formatted(contextBezeichnung, baseContext.getClass().getSimpleName()));
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"### FEHLER: Die Daten dieses Reports lassen sich nicht in einzelne Dateien aufteilen.");
		}
	}

	/**
	 * Ermittelt die IDs aus den in der HTML-Factory vorhandenen HTML-Kontexten. Die Methode iteriert über die Einträge der Map, sammelt die IDs aus den
	 * Kontexten, entfernt Duplikate und gibt die eindeutigen IDs zurück.
	 *
	 * @return Eine Liste von eindeutigen {@code Long}-IDs, die aus den HTML-Kontexten gesammelt wurden.
	 */
	private List<Long> getContextsIds() {
		final List<Long> ids = new ArrayList<>();

		for (final HtmlContext<?> context : mapHtmlContexts.values()) {
			if (context == null) {
				continue;
			}
			ids.addAll(context.getIds());
		}

		return ids.stream().filter(Objects::nonNull).distinct().toList();
	}

	/**
	 * Erstellt eine Instanz von ReportBuilderHtml basierend auf dem angegebenen HTML-Template-Code und spezifischen Kontextinformationen.
	 *
	 * @param htmlTemplateCode Der Code des HTML-Templates, das für den Reportbuilder verwendet werden soll.
	 *
	 * @return Eine Instanz von ReportBuilderHtml, die auf der Basis des angegebenen HTML-Templates erstellt wurde.
	 *
	 * @throws ApiOperationException Eine Ausnahme wird geworfen, wenn ein Fehler beim Erstellen des ReportBuilderHtml auftritt.
	 */
	private ReportBuilderHtml getReportBuilderHtml(final String htmlTemplateCode) throws ApiOperationException {
		final ReportBuilderContextHtml reportBuilderContext =
				new ReportBuilderContextHtml()
						.withHtmlTemplate(htmlTemplateCode)
						.addHtmlContexts(mapHtmlContexts.values().stream().toList())
						.addIds(getContextsIds())
						.withDateinamensvorlage(ladeDateinamensvorlageAusDatei(reportingReportvorlage.getPfadDateinamensvorlage()))
						.withStatischerDateiname(reportingReportvorlage.getDateiname())
						.withRootPfad(ReportingReportvorlage.getRootPfad());
		return new ReportBuilderHtml(reportBuilderContext);
	}

	/**
	 * Lädt die Vorlage für den Dateinamen aus einer ".name.tpl" Datei.
	 *
	 * @param pfadNameTemplate Der relative Pfad zur Namensvorlage-Datei
	 * @return Die geladene Vorlage oder ein leerer String bei einem Fehler.
	 */
	private static String ladeDateinamensvorlageAusDatei(final String pfadNameTemplate) {
		return ResourceUtils.textOrEmpty(ReportingReportvorlage.getRootPfad() + pfadNameTemplate);
	}

}
