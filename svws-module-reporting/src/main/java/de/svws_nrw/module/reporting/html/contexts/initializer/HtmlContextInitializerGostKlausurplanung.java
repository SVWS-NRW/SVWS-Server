package de.svws_nrw.module.reporting.html.contexts.initializer;

import java.util.Map;

import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import de.svws_nrw.module.reporting.repositories.ReportingContext;

/**
 * Initializer für die Datenaufbauten der GOSt-Klausurplanung: gymnasiale Oberstufe und — sofern Hauptdaten übergeben wurden — Abiturjahrgang und Halbjahre
 * prüfen, Haupt-Context erzeugen.
 * <p>Die beiden Sichtweisen — Schüler und Klausurtermine — teilen diesen Ablauf und unterscheiden sich allein in ihrer
 * {@link HtmlContextAufbauGostKlausurplanung}.</p>
 */
final class HtmlContextInitializerGostKlausurplanung extends HtmlContextInitializerBasis {

	/** Die Konfiguration des Datenaufbaus, den dieser Initializer ausführt. */
	private final HtmlContextAufbauGostKlausurplanung aufbau;


	/**
	 * Erzeugt den Initializer für einen konkreten Request.
	 *
	 * @param reportingContext Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param mapHtmlContexts  Die Map, in der die erzeugten HTML-Contexts gesammelt werden.
	 * @param aufbau           Die Konfiguration des auszuführenden Datenaufbaus.
	 */
	HtmlContextInitializerGostKlausurplanung(final ReportingContext reportingContext, final Map<String, HtmlContext<?>> mapHtmlContexts,
			final HtmlContextAufbauGostKlausurplanung aufbau) {
		super(reportingContext, mapHtmlContexts);
		this.aufbau = aufbau;
	}


	/**
	 * Prüft die gymnasiale Oberstufe sowie die Parameter für Abiturjahrgang und Halbjahre und legt den erzeugten Haupt-Context in der Context-Map ab.
	 *
	 * @throws ApiOperationException Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	@Override
	public void init() throws ApiOperationException {
		reportingContext.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für einen Gost-Klausurplan für die HTML-Generierung.");

		HtmlContextValidierung.validiereSchuleMitGost(reportingContext);
		if (!reportingParameter.idsHauptdaten().isEmpty()) {
			HtmlContextValidierung.validiereParameterFuerAbiturjahrgangUndHalbjahre(reportingContext, true);
		}

		reportingContext.logger().logLn(LogLevel.DEBUG, 4,
				"Erzeuge Datenkontext Gost-Klausurplanung für die HTML-Generierung mit Template %s.".formatted(reportingReportvorlage.name()));
		mapHtmlContexts.put(aufbau.contextSchluessel(), aufbau.contextErzeuger().apply(reportingContext));
	}


	/**
	 * Der Schlüssel des Haupt-Contexts in der Context-Map. Die Datenaufbauten der GOSt-Klausurplanung unterstützen die Einzelausgabe.
	 *
	 * @return Der Schlüssel des Haupt-Contexts in der Context-Map.
	 */
	@Override
	public String einzelContextBezeichnung() {
		return aufbau.contextSchluessel();
	}

}
