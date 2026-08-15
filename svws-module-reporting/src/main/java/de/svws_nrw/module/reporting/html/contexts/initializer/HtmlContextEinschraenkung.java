package de.svws_nrw.module.reporting.html.contexts.initializer;

import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.diagnose.ReportingAuswahlergebnis;
import de.svws_nrw.module.reporting.repositories.ReportingContext;

/**
 * Die fachliche Einschränkung einer bereits getroffenen Auswahl - etwa "der Schüler gehört zum Abiturjahrgang".
 * <p>Sie arbeitet auf dem Auswahlergebnis und nicht auf den Request-IDs: Was die Auswahl ausgelassen hat, bleibt ausgelassen, und was die Einschränkung
 * entfernt, ist danach als ausgelassen vermerkt. Eine eigene Schnittstelle statt {@code BiFunction} braucht es, weil eine Einschränkung werfen darf - eine
 * verletzte Voraussetzung des gesamten Reports wie eine Schule ohne gymnasiale Oberstufe beendet die Ausgabe.</p>
 *
 * @param <T> Der Reporting-Typ der ausgewählten Hauptdaten.
 */
@FunctionalInterface
interface HtmlContextEinschraenkung<T> {

	/**
	 * Schränkt die übergebene Auswahl fachlich ein.
	 *
	 * @param reportingContext Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param auswahl          Die bisherige Auswahl.
	 *
	 * @return Die eingeschränkte Auswahl.
	 *
	 * @throws ApiOperationException Falls eine Voraussetzung des gesamten Datenaufbaus verletzt ist.
	 */
	ReportingAuswahlergebnis<T> schraenkeEin(ReportingContext reportingContext, ReportingAuswahlergebnis<T> auswahl) throws ApiOperationException;

}
