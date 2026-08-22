package de.svws_nrw.module.reporting.html.contexts.initializer;

import java.util.Map;

import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextGostLaufbahnplanungAbiturjahrgangFachwahlstatistiken;
import de.svws_nrw.module.reporting.repositories.ReportingContext;

/**
 * Initializer für den Datenaufbau der GOSt-Laufbahnplanung eines Abiturjahrgangs mit dessen Fachwahlstatistiken.
 * <p>Dieser Datenaufbau ist ein Einzelfall: Er kennt nur eine Reportvorlage, und die Hauptdaten sind kein Bestand an IDs, sondern ein Abiturjahrgang mit
 * seinen Halbjahren. Er überschreibt {@link HtmlContextInitializerBasis#einzelContextBezeichnung()} bewusst <b>nicht</b> — dieser Datenaufbau unterstützt
 * keine Einzelausgabe, und die Standard-Implementierung der Basisklasse wirft dafür den passenden Fehler.</p>
 */
final class HtmlContextInitializerGostLaufbahnplanung extends HtmlContextInitializerBasis {

	/**
	 * Erzeugt den Initializer für einen konkreten Request.
	 *
	 * @param reportingContext Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param mapHtmlContexts  Die Map, in der die erzeugten HTML-Contexts gesammelt werden.
	 */
	HtmlContextInitializerGostLaufbahnplanung(final ReportingContext reportingContext, final Map<String, HtmlContext<?>> mapHtmlContexts) {
		super(reportingContext, mapHtmlContexts);
	}


	/**
	 * Prüft die gymnasiale Oberstufe sowie die Parameter für Abiturjahrgang und Halbjahre und legt den erzeugten Haupt-Context in der Context-Map ab.
	 *
	 * @throws ApiOperationException Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	@Override
	public void init() throws ApiOperationException {
		reportingContext.logger().logLn(LogLevel.DEBUG, 4,
				"Validiere die Daten für einen Gost-Laufbahnplan eines Abiturjahrgangs und dessen Fachwahlstatistiken für die HTML-Generierung.");

		HtmlContextValidierung.validiereSchuleMitGost(reportingContext);
		HtmlContextValidierung.validiereAbiturjahrgangAlsHauptressource(reportingContext);

		reportingContext.logger().logLn(LogLevel.DEBUG, 4,
				"Erzeuge Datenkontext Gost-Laufbahnplan-Abiturjahrgang-Fachwahlstatistiken für die HTML-Generierung mit Template %s."
						.formatted(reportingReportvorlage.name()));
		mapHtmlContexts.put(HtmlContextSchluessel.GOST_LAUFBAHNPLANUNG_FACHWAHLSTATISTIKEN,
				new HtmlContextGostLaufbahnplanungAbiturjahrgangFachwahlstatistiken(reportingContext));
	}


	/**
	 * Die Zähleinheit dieses Datenaufbaus sind die Fächer der Statistik; sie stehen erst mit deren Erstellung im Context fest.
	 *
	 * @return true, denn der Context-Aufbau meldet den Ausgabeumfang.
	 */
	@Override
	public boolean meldetAusgabeumfangImContextAufbau() {
		return true;
	}

}
