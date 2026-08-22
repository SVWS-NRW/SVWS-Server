package de.svws_nrw.module.reporting.factories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.svws_nrw.base.ResourceUtils;
import de.svws_nrw.core.logger.LogLevel;
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
	 * @throws ApiOperationException    Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
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
	 * @throws ApiOperationException    Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
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
			this.reportingContext.logger()
					.logLn(LogLevel.ERROR, 4, "FEHLER: Die Template-Definitionen für die HTML-Factory sind inkonsistent.");
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "FEHLER: Die Template-Definitionen für die HTML-Factory sind inkonsistent.");
		}

		// Prüfe, ob die Rechte des Benutzers zu den in der TemplateDefinition hinterlegten Rechten passen.
		this.reportingContext.logger().logLn(LogLevel.DEBUG, 4,
				"Prüfe die Berechtigungen des Benutzers für den Zugriff auf die für die Ausgabe notwendigen Daten.");
		if (!this.reportingContext.benutzer().pruefeKompetenz(reportingReportvorlage.getBenutzerKompetenzen())) {
			this.reportingContext.logger()
					.logLn(LogLevel.ERROR, 4,
							"FEHLER: Der Benutzer hat nicht die erforderlichen Rechte, um auf die Daten für die Erstellung der Ausgabe zu zugreifen.");
			throw new ApiOperationException(Status.FORBIDDEN,
					"FEHLER: Der Benutzer hat nicht die erforderlichen Rechte, um auf die Daten für die Erstellung der Ausgabe zu zugreifen.");
		}

		this.reportingContext.logger().logLn(LogLevel.DEBUG, 0, "<<< Ende der Initialisierung der HTML-Factory und der Validierung der übergebenen Daten.");
	}


	/**
	 * Erzeugte die notwendigen Contexts für die HTML-Erstellung auf Basis des angegebenen HTML-Templates.
	 *
	 * @throws ApiOperationException    Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
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
	 * @throws ApiOperationException    Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	private void initContextUeberRegistry(final ReportingReportvorlageDatenContext datenContext) throws ApiOperationException {
		final HtmlContextAufbau aufbau = HtmlContextInitializerRegistry.aufbau(reportingContext, datenContext);
		this.htmlContextInitializer = aufbau.initializer(reportingContext, mapHtmlContexts);
		this.htmlContextInitializer.init();
		if (reportingContext.ausgabeumfang() == null) {
			final String fehlermeldung = ("FEHLER: Der Datenaufbau %s hat keinen Ausgabeumfang gemeldet. Die Meldestelle liegt laut Initializer %s "
					+ "und fehlt dort.").formatted(datenContext.name(),
							this.htmlContextInitializer.meldetAusgabeumfangImContextAufbau() ? "im Context-Aufbau" : "im Initializer selbst");
			reportingContext.logger().logLn(LogLevel.ERROR, 4, fehlermeldung);
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, fehlermeldung);
		}
	}

	/**
	 * Erzeugt auf Basis des gegebenen HTML-Templates und der übergebenen Daten die HTML-Builder, aus denen die HTML-Inhalte erzeugt werden können.
	 *
	 * @return Eine Liste mit ReportBuilderHtml-Instanzen.
	 *
	 * @throws ApiOperationException    Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	protected List<ReportBuilderHtml> createHtmlBuilders() throws ApiOperationException {
		return getHtmlBuilders();
	}


	/**
	 * Erstellt eine Response in Form einer einzelnen HTML-Datei, die für die Anzeige in einem Browser verwendet werden kann.
	 *
	 * @return Im Falle eines Success enthält die HTTP-Response das HTML-Dokument.
	 *
	 * @throws ApiOperationException    Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	protected Response createHtmlResponse() throws ApiOperationException {
		try {
			reportingContext.logger().logLn(LogLevel.DEBUG, 0, ">>> Beginn der Erzeugung der Response einer API-Anfrage für eine HTML-Generierung.");
			final List<ReportBuilderHtml> htmlBuilders = getHtmlBuilders();
			if (!htmlBuilders.isEmpty()) {
				final ReportBuilderHtml firstHtmlBuilder = htmlBuilders.getFirst();
				if (htmlBuilders.size() == 1) {
					final String html = firstHtmlBuilder.generate();
					reportingContext.logger().logLn(LogLevel.DEBUG, 0, "<<< Ende der Erzeugung der Response einer API-Anfrage für eine HTML-Generierung.");
					// HTML bildet keinen Sonderpfad: Es trägt denselben Hinweis-Header wie PDF und ZIP. Dass der heutige generierte Client die
					// Response-Metadaten verwirft und ihn deshalb nicht anzeigt, ändert am Serververtrag nichts.
					return ReportingHinweiseHeader
							.ergaenze(Response.ok(html, "text/html; charset=UTF-8").header("Cache-Control", "no-store"), reportingContext)
							.build();
				} else {
					// Reine Absicherung: Der Zweig ist unerreichbar, seit ReportingParameterBuilder die Aufteilung in Einzeldateien für die
					// HTML-Ausgabe auf dem fertig kombinierten Parametersatz auf false festlegt - ohne Aufteilung entsteht genau ein Builder.
					reportingContext.logger().logLn(LogLevel.ERROR, 0,
							"### Fehler bei der Erzeugung der Response einer API-Anfrage für eine HTML-Generierung. Es wurde mehr als ein Builder übergeben.");
					throw new ApiOperationException(Status.BAD_REQUEST,
							"### Fehler bei der Erzeugung der Response einer API-Anfrage für eine HTML-Generierung. Es wurde mehr als ein Builder übergeben.");
				}
			}
			reportingContext.logger().logLn(LogLevel.ERROR, 0,
					"### Fehler bei der Erzeugung der Response einer API-Anfrage für eine HTML-Generierung. Es sind keine HTML-Inhalte generiert worden.");
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"### Fehler bei der Erzeugung der Response einer API-Anfrage für eine HTML-Generierung. Es sind keine HTML-Inhalte generiert worden.");
		} catch (final Exception e) {
			reportingContext.logger().logLn(LogLevel.ERROR, 0, "### Fehler bei der Erzeugung der Response einer API-Anfrage für eine HTML-Generierung.");
			throw e;
		}
	}


	/**
	 * Erzeugt auf Basis der übergebenen HTML-Vorlage und Daten die HTML-Inhalte der Dateien und legt diese Inhalte in einer Liste ab.
	 *
	 * @return Eine Liste mit ReportBuilderHtml-Instanzen.
	 *
	 * @throws ApiOperationException Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	private List<ReportBuilderHtml> getHtmlBuilders() throws ApiOperationException {

		reportingContext.logger().logLn(LogLevel.DEBUG, 0, ">>> Beginn der Erzeugung der HTML-Builder.");

		// Lade den Inhalt des HTML-Codes aus dem Template. Eine leere Datei wird hier mitgeprüft: Sie ist als Vorlage ebenso unbrauchbar wie eine nicht
		// lesbare und ist als interne Ressource ein Serverfehler. Ohne die Prüfung lehnte sie erst der Builder-Kontext ab - mit falschem Status.
		final String htmlTemplateCode = ResourceUtils.text(reportingReportvorlage.getRootPfadHtmlTemplate());
		if ((htmlTemplateCode == null) || htmlTemplateCode.isBlank()) {
			reportingContext.logger().logLn(LogLevel.ERROR, 4, "### FEHLER: Die HTML-Template-Datei für die HTML-Erzeugung konnte nicht eingelesen werden "
					+ "oder ist leer.");
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"### FEHLER: Die HTML-Template-Datei für die HTML-Erzeugung konnte nicht eingelesen werden oder ist leer.");
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
	 * @throws ApiOperationException Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	private void erzeugeEinzelContexts(final List<ReportBuilderHtml> htmlBuilders, final String htmlTemplateCode) throws ApiOperationException {
		final String contextBezeichnung = htmlContextInitializer.einzelContextBezeichnung();
		final HtmlContext<?> baseContext = mapHtmlContexts.get(contextBezeichnung);

		// Ein fehlender Context und ein Context ohne Aufteilungsfähigkeit sind verschiedene Fehler und brauchen verschiedene Meldungen: Die Typprüfung unten
		// kann beides nicht unterscheiden und würde einen fehlenden Context als fehlende Fähigkeit ausweisen. Beide Fälle setzen eine fehlkonfigurierte
		// Registry voraus.
		if (baseContext == null) {
			reportingContext.logger().logLn(LogLevel.ERROR, 4,
					"FEHLER: Der Kontext " + contextBezeichnung + " für die Aufteilung in Einzeldokumente wurde nicht aufgebaut.");
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"FEHLER: Der Kontext " + contextBezeichnung + " für die Aufteilung in Einzeldokumente wurde nicht aufgebaut.");
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
			reportingContext.logger().logLn(LogLevel.ERROR, 4,
					"FEHLER: Der Kontext " + contextBezeichnung + " unterstützt das Aufteilen in Einzeldokumente nicht.");
			throw new ApiOperationException(Status.BAD_REQUEST,
					"FEHLER: Der Kontext " + contextBezeichnung + " unterstützt das Aufteilen in Einzeldokumente nicht.");
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
						.withRootPfad(ReportingReportvorlage.getRootPfad())
						.withLogger(reportingContext.logger());
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
