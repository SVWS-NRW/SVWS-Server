package de.svws_nrw.module.reporting.html.contexts.initializer;

import java.util.Map;

import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import de.svws_nrw.module.reporting.parameter.ReportingParameterTypisiert;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import jakarta.ws.rs.core.Response.Status;

/**
 * Basisklasse der Initializer für den Aufbau der Daten-Contexts. Sie hält die für alle Datenaufbauten gemeinsamen Felder und die Standard-Implementierung
 * von {@link #einzelContextBezeichnung()} für Datenaufbauten ohne Einzelausgabe.
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


	/**
	 * Erzeugt einen Initializer für einen konkreten Request.
	 * <p>Die Context-Map wird als Referenz übernommen und <b>nicht</b> kopiert: Der Initializer legt seine Contexts in genau der Map ab, aus der die
	 * HTML-Factory anschließend die Builder erzeugt.</p>
	 *
	 * @param reportingContext Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param mapHtmlContexts  Die Map, in der die erzeugten HTML-Contexts gesammelt werden.
	 */
	protected HtmlContextInitializerBasis(final ReportingContext reportingContext, final Map<String, HtmlContext<?>> mapHtmlContexts) {
		this.reportingContext = reportingContext;
		this.reportingParameter = reportingContext.reportingParameter();
		this.reportingReportvorlage = this.reportingParameter.reportVorlage();
		this.mapHtmlContexts = mapHtmlContexts;
	}


	/**
	 * Der Schlüssel des Haupt-Contexts in der Context-Map. Diese Standard-Implementierung gilt für Datenaufbauten ohne Einzelausgabe und wirft daher stets
	 * einen Fehler; Datenaufbauten mit Einzelausgabe überschreiben sie.
	 *
	 * @return Kehrt nicht zurück.
	 *
	 * @throws ApiOperationException Immer, da dieser Datenaufbau keine Einzelausgabe unterstützt.
	 */
	@Override
	public String einzelContextBezeichnung() throws ApiOperationException {
		throw new ApiOperationException(Status.BAD_REQUEST,
				"FEHLER: Die Vorlage " + this.reportingReportvorlage.getBezeichnung() + " unterstützt keine Einzelausgabe.");
	}

}
