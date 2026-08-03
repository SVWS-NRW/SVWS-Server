package de.svws_nrw.module.reporting.html.contexts.initializer;

import java.util.List;
import java.util.Map;

import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungStundenplan;
import jakarta.ws.rs.core.Response.Status;

/**
 * Initializer für alle Datenaufbauten der Stundenplanung: Stundenplan zur übergebenen ID laden, die Hauptdaten-IDs der Sichtweise prüfen, Haupt-Context
 * erzeugen.
 * <p>Der Stundenplan ist die gemeinsame Klammer der fünf Sichtweisen (Fächer, Klassen, Lehrkräfte, Räume, Schüler); sie unterscheiden sich allein in ihrer
 * {@link HtmlContextAufbauStundenplan}-Konfiguration.</p>
 *
 * @param <T> Der Reporting-Typ der Context-Daten dieser Sichtweise.
 */
final class HtmlContextInitializerStundenplan<T> extends HtmlContextInitializerBasis {

	/** Die Konfiguration des Datenaufbaus, den dieser Initializer ausführt. */
	private final HtmlContextAufbauStundenplan<T> aufbau;


	/**
	 * Erzeugt den Initializer für einen konkreten Request.
	 *
	 * @param reportingContext Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param mapHtmlContexts  Die Map, in der die erzeugten HTML-Contexts gesammelt werden.
	 * @param aufbau           Die Konfiguration des auszuführenden Datenaufbaus.
	 */
	HtmlContextInitializerStundenplan(final ReportingContext reportingContext, final Map<String, HtmlContext<?>> mapHtmlContexts,
			final HtmlContextAufbauStundenplan<T> aufbau) {
		super(reportingContext, mapHtmlContexts);
		this.aufbau = aufbau;
	}


	/**
	 * Lädt den Stundenplan zur übergebenen ID, prüft die Hauptdaten-IDs der Sichtweise und legt den erzeugten Haupt-Context in der Context-Map ab.
	 *
	 * @throws ApiOperationException Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	@Override
	public void init() throws ApiOperationException {
		reportingContext.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für einen Stundenplan für die HTML-Generierung.");

		final ReportingStundenplanungStundenplan stundenplan = reportingContext.repositoryStundenplan().stundenplan(reportingParameter.idHauptdatenObjekt());
		if (stundenplan == null) {
			reportingContext.logger().logLn(LogLevel.ERROR, 4,
					"FEHLER: Mit der angegebenen Stundenplan-ID konnte kein Stundenplan ermittelt werden.");
			throw new ApiOperationException(Status.NOT_FOUND, "FEHLER: Mit der angegebenen Stundenplan-ID konnte kein Stundenplan ermittelt werden.");
		}

		reportingContext.logger().logLn(LogLevel.DEBUG, 4,
				"Erzeuge Datenkontext Stundenplan für die HTML-Generierung mit Template %s.".formatted(reportingReportvorlage.name()));

		final List<Long> ids = reportingParameter.idsHauptdaten();
		aufbau.pruefung().accept(reportingContext, ids);
		mapHtmlContexts.put(aufbau.contextSchluessel(), aufbau.contextErzeuger().erzeuge(reportingContext, stundenplan, ids));
	}


	/**
	 * Der Schlüssel des Haupt-Contexts in der Context-Map. Die Datenaufbauten der Stundenplanung unterstützen die Einzelausgabe.
	 *
	 * @return Der Schlüssel des Haupt-Contexts in der Context-Map.
	 */
	@Override
	public String einzelContextBezeichnung() {
		return aufbau.contextSchluessel();
	}

}
