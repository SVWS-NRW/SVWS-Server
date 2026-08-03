package de.svws_nrw.module.reporting.html.contexts.initializer;

import java.util.Map;

import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import jakarta.ws.rs.core.Response.Status;

/**
 * Initializer für die Datenaufbauten der GOSt-Kursplanung: gymnasiale Oberstufe und übergebene Blockungsergebnis-ID prüfen, Haupt-Context erzeugen.
 * <p>Die beiden Sichtweisen — Kurse und Schüler — teilen diesen Ablauf und unterscheiden sich allein in ihrer {@link HtmlContextAufbauGostKursplanung}.</p>
 */
final class HtmlContextInitializerGostKursplanung extends HtmlContextInitializerBasis {

	/** Die Konfiguration des Datenaufbaus, den dieser Initializer ausführt. */
	private final HtmlContextAufbauGostKursplanung aufbau;


	/**
	 * Erzeugt den Initializer für einen konkreten Request.
	 *
	 * @param reportingContext Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param mapHtmlContexts  Die Map, in der die erzeugten HTML-Contexts gesammelt werden.
	 * @param aufbau           Die Konfiguration des auszuführenden Datenaufbaus.
	 */
	HtmlContextInitializerGostKursplanung(final ReportingContext reportingContext, final Map<String, HtmlContext<?>> mapHtmlContexts,
			final HtmlContextAufbauGostKursplanung aufbau) {
		super(reportingContext, mapHtmlContexts);
		this.aufbau = aufbau;
	}


	/**
	 * Prüft die gymnasiale Oberstufe und die übergebene Blockungsergebnis-ID und legt den erzeugten Haupt-Context in der Context-Map ab.
	 *
	 * @throws ApiOperationException Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	@Override
	public void init() throws ApiOperationException {
		reportingContext.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für ein Gost-Blockungsergebnis für die HTML-Generierung.");

		HtmlContextValidierung.validiereSchuleMitGost(reportingContext);
		if (reportingParameter.idHauptdatenObjekt() < 0) {
			reportingContext.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Es wurde keine ID für ein Blockungsergebnis übergeben.");
			throw new ApiOperationException(Status.BAD_REQUEST, "FEHLER: Es wurde keine ID für ein Blockungsergebnis übergeben.");
		}

		reportingContext.logger().logLn(LogLevel.DEBUG, 4,
				"Erzeuge Datenkontext Gost-Kursplanung-Blockungsergebnis für die HTML-Generierung mit ID %s für Template %s."
						.formatted(reportingParameter.idHauptdatenObjekt(), reportingReportvorlage.name()));
		mapHtmlContexts.put(aufbau.contextSchluessel(), aufbau.contextErzeuger().apply(reportingContext));
	}


	/**
	 * Der Schlüssel des Haupt-Contexts in der Context-Map. Die Datenaufbauten der GOSt-Kursplanung unterstützen die Einzelausgabe.
	 *
	 * @return Der Schlüssel des Haupt-Contexts in der Context-Map.
	 */
	@Override
	public String einzelContextBezeichnung() {
		return aufbau.contextSchluessel();
	}

}
