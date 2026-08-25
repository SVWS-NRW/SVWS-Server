package de.svws_nrw.module.reporting.html.contexts.initializer;

import java.util.List;
import java.util.Map;

import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.diagnose.ReportingAusgabeumfang;
import de.svws_nrw.module.reporting.diagnose.ReportingAuswahlergebnis;
import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungStundenplan;
import jakarta.ws.rs.core.Response.Status;

/**
 * Initializer für alle Datenaufbauten der Stundenplanung: Stundenplan zur übergebenen ID laden, die Hauptdaten der Sichtweise auswählen, Haupt-Context
 * erzeugen. Eine ID, die die Auswahl nicht auflösen kann, wird ausgelassen und gemeldet; geworfen wird allein bei einer im Request leeren ID-Liste.
 * <p>Der Stundenplan ist die gemeinsame Klammer der fünf Sichtweisen (Fächer, Klassen, Lehrkräfte, Räume, Schüler); sie unterscheiden sich allein in ihrer
 * {@link HtmlContextAufbauStundenplan}-Konfiguration.</p>
 *
 * @param <T> Der Reporting-Typ der Context-Daten dieser Sichtweise.
 * @param <H> Der Reporting-Typ der ausgewählten Hauptdaten dieser Sichtweise.
 */
final class HtmlContextInitializerStundenplan<T, H> extends HtmlContextInitializerBasis {

	/** Die Konfiguration des Datenaufbaus, den dieser Initializer ausführt. */
	private final HtmlContextAufbauStundenplan<T, H> aufbau;


	/**
	 * Erzeugt den Initializer für einen konkreten Request.
	 *
	 * @param reportingContext Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param mapHtmlContexts  Die Map, in der die erzeugten HTML-Contexts gesammelt werden.
	 * @param aufbau           Die Konfiguration des auszuführenden Datenaufbaus.
	 */
	HtmlContextInitializerStundenplan(final ReportingContext reportingContext, final Map<String, HtmlContext<?>> mapHtmlContexts,
			final HtmlContextAufbauStundenplan<T, H> aufbau) {
		super(reportingContext, mapHtmlContexts, aufbau);
		this.aufbau = aufbau;
	}


	/**
	 * Lädt den Stundenplan zur übergebenen ID, wählt die Hauptdaten der Sichtweise aus, meldet die dabei ausgelassenen und legt den erzeugten Haupt-Context
	 * in der Context-Map ab. Der Context entsteht aus den ausgewählten IDs und nicht erneut aus den Request-IDs - sonst wäre das Auslassen wirkungslos.
	 *
	 * @throws ApiOperationException Bei einem Abbruch; die Exception trägt den Abbruchgrund als Meldung.
	 */
	@Override
	public void init() throws ApiOperationException {
		reportingContext.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für einen Stundenplan für die HTML-Generierung.");

		final ReportingStundenplanungStundenplan stundenplan = reportingContext.repositoryStundenplan().stundenplan(reportingParameter.idHauptdatenObjekt());
		if (stundenplan == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "### FEHLER: Der angeforderte Stundenplan ist nicht vorhanden.");
		}

		reportingContext.logger().logLn(LogLevel.DEBUG, 4,
				"Erzeuge Datenkontext Stundenplan für die HTML-Generierung mit Template %s.".formatted(reportingReportvorlage.name()));

		final List<Long> ids = reportingParameter.idsHauptdaten();
		final ReportingAuswahlergebnis<H> auswahl = aufbau.auswahl().waehle(reportingContext, stundenplan, ids);
		HtmlContextValidierung.pruefeUndMeldeAuswahl(reportingContext, auswahl, aufbau.objektart(), aufbau.bezeichnungen());
		reportingContext.meldeAusgabeumfang(ReportingAusgabeumfang.ausAuswahl(auswahl));

		mapHtmlContexts.put(aufbau.contextSchluessel(), aufbau.contextErzeuger().erzeuge(reportingContext, stundenplan, auswahl.idsAusgewaehlt()));
	}


	/**
	 * Der Ausgabeumfang wird direkt in {@link #init()} aus der Auswahl gemeldet.
	 *
	 * @return false, denn dieser Initializer meldet selbst.
	 */
	@Override
	public boolean meldetAusgabeumfangImContextAufbau() {
		return false;
	}

}
