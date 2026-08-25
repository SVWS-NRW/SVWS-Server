package de.svws_nrw.module.reporting.html.contexts.initializer;

import java.util.Map;

import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import de.svws_nrw.module.reporting.parameter.ReportingParameterTypisiert;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import jakarta.ws.rs.core.Response.Status;

/**
 * Basisklasse der Initializer für den Aufbau der Daten-Contexts. Sie hält die für alle Datenaufbauten gemeinsamen Felder und entscheidet über die
 * Einzelausgabe: {@link #einzelContextBezeichnung()} gibt den Schlüssel des Haupt-Contexts nur heraus, wenn der Datenaufbau sie zusagt. Die Zusage hat
 * damit genau einen Leser, und kein Datenaufbau kann sie durch einen eigenen Override umgehen.
 * <p>Die Prüfungen der Eingabeparameter liegen bewusst nicht hier, sondern in {@link HtmlContextValidierung}: Sie werden auch aus der Konfiguration der
 * Registry heraus als Methodenreferenz benötigt, und die Registry ist keine Unterklasse dieser Basisklasse.</p>
 */
abstract class HtmlContextInitializerBasis implements HtmlContextInitializer {

	/** Context mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	protected final ReportingContext reportingContext;

	/** Einstellungen und Daten zum Steuern der Report-Generierung. */
	protected final ReportingParameterTypisiert reportingParameter;

	/** Reporting-Reportvorlage für die Erstellung der HTML-Datei. */
	protected final ReportingReportvorlage reportingReportvorlage;

	/** Die Map, in der die erzeugten HTML-Contexts gesammelt werden. */
	protected final Map<String, HtmlContext<?>> mapHtmlContexts;

	/** Die request-unabhängige Konfiguration des Datenaufbaus. Ihre Zusage entscheidet über die Einzelausgabe. */
	private final HtmlContextAufbau aufbau;


	/**
	 * Erzeugt einen Initializer für einen konkreten Request.
	 * <p>Die Context-Map wird als Referenz übernommen und <b>nicht</b> kopiert: Der Initializer legt seine Contexts in genau der Map ab, aus der die
	 * HTML-Factory anschließend die Builder erzeugt.</p>
	 *
	 * @param reportingContext Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param mapHtmlContexts  Die Map, in der die erzeugten HTML-Contexts gesammelt werden.
	 * @param aufbau           Die request-unabhängige Konfiguration des Datenaufbaus.
	 */
	protected HtmlContextInitializerBasis(final ReportingContext reportingContext, final Map<String, HtmlContext<?>> mapHtmlContexts,
			final HtmlContextAufbau aufbau) {
		this.reportingContext = reportingContext;
		this.reportingParameter = reportingContext.reportingParameter();
		this.reportingReportvorlage = this.reportingParameter.reportVorlage();
		this.mapHtmlContexts = mapHtmlContexts;
		this.aufbau = aufbau;
	}


	/**
	 * Der Schlüssel des Haupt-Contexts in der Context-Map, sofern der Datenaufbau die Einzelausgabe zusagt. Sagt er sie nicht zu, ist die Anfrage ein
	 * Client-Fehler: Der Anwender hat für diese Vorlage eine Ausgabe verlangt, die es nicht gibt.
	 *
	 * @return Der Schlüssel des Haupt-Contexts in der Context-Map.
	 *
	 * @throws ApiOperationException Falls der Datenaufbau keine Einzelausgabe unterstützt.
	 */
	@Override
	public final String einzelContextBezeichnung() throws ApiOperationException {
		if (!aufbau.unterstuetztEinzelausgabe()) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"### FEHLER: Die Reportvorlage '%s' kann nicht in einzelne Dateien ausgegeben werden."
							.formatted(this.reportingReportvorlage.getBezeichnung()));
		}
		return aufbau.contextSchluessel();
	}

}
