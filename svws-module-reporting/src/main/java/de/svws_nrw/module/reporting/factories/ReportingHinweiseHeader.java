package de.svws_nrw.module.reporting.factories;

import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.module.reporting.diagnose.ReportingAusgabeumfang;
import de.svws_nrw.module.reporting.diagnose.ReportingHinweisSerializer;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import jakarta.ws.rs.core.Response;

/**
 * Ergänzt eine erfolgreiche Reporting-Antwort um den öffentlichen Hinweis-Header.
 * <p>Diese Klasse ist die einzige Stelle, die über das Setzen entscheidet - sonst träfen HTML-, PDF- und ZIP-Ausgabe die Entscheidung dreimal getrennt.
 * Vorausgesetzt wird der gemeldete Ausgabeumfang; dass er vorliegt, erzwingt die Ausgabefactory nach dem Aufbau der Daten-Contexts. E-Mail bleibt außen vor,
 * denn ihre JSON-Startantwort ist kein Dokument.</p>
 * <p><b>Der Header wird derzeit nur im Server-Modus {@code DEV} gesetzt.</b> Seine Auslieferung wartet auf die Entscheidung über die Auswertung im Webclient;
 * bis dahin sieht eine stabile Installation die unveränderte Antwort. Ermittelt und geprüft werden die Angaben in jedem Modus, damit ein Datenaufbau ohne
 * gemeldeten Ausgabeumfang weiterhin sofort auffällt.</p>
 */
final class ReportingHinweiseHeader {

	private ReportingHinweiseHeader() {
		throw new IllegalStateException("Hilfsklasse - Initialisierung nicht möglich.");
	}


	/**
	 * Ergänzt den übergebenen Antwortaufbau um den Hinweis-Header aus dem gemeldeten Ausgabeumfang und den gemeldeten Ausgabeproblemen. Außerhalb des
	 * Server-Modus {@code DEV} bleibt die Antwort unverändert (siehe Klassenbeschreibung).
	 * <p>Verletzt der Umfang die Zusagen des Vertrags - negative Werte oder mehr ausgegeben als angefordert -, bleibt die Antwort ebenfalls ohne Header: Ein
	 * fehlender Header bedeutet "unbekannt", ein vertragswidriger wäre eine falsche Auskunft. Die fertige Ausgabe wird deshalb nicht verworfen, der
	 * Programmierfehler steht im Log.</p>
	 *
	 * @param response         Der Aufbau der erfolgreichen Antwort.
	 * @param reportingContext Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 *
	 * @return Derselbe Antwortaufbau, um den Header ergänzt oder unverändert.
	 */
	static Response.ResponseBuilder ergaenze(final Response.ResponseBuilder response, final ReportingContext reportingContext) {
		// Die Prüfung steht vor der Modus-Abfrage: Ein vertragswidriger Ausgabeumfang ist ein Programmierfehler und soll in jedem Modus im Log auffallen,
		// nicht erst, wenn jemand in der Entwicklung nachsieht.
		final ReportingAusgabeumfang umfang = reportingContext.ausgabeumfang();
		final boolean verwertbar = (umfang != null) && !verletztInvarianten(umfang);
		if (!verwertbar) {
			reportingContext.logger().logLn(LogLevel.WARNING, 4,
					"Der Hinweis-Header wird nicht gesetzt: Der gemeldete Ausgabeumfang fehlt oder verletzt die Zusagen des Vertrags (%s)."
							.formatted((umfang == null) ? "keine Meldung" : ("angefordert=" + umfang.angefordert() + ", ausgegeben=" + umfang.ausgegeben())));
		}

		// TODO Auslieferung des Headers freigeben, sobald über die Auswertung im Webclient entschieden ist: ServerMode.DEV durch ServerMode.STABLE ersetzen
		// und die Beschreibung des Headers in APIReporting wieder aufnehmen. Bis dahin bleibt die Antwort außerhalb der Entwicklung unverändert - der Header
		// träte sonst in Umlauf, bevor ein Verbraucher feststeht.
		if (!verwertbar || !ServerMode.DEV.checkServerMode(reportingContext.serverMode())) {
			return response;
		}
		return response.header(ReportingHinweisSerializer.HEADER_NAME,
				ReportingHinweisSerializer.headerwert(umfang, reportingContext.ausgabeprobleme()));
	}

	/**
	 * Gibt an, ob der gemeldete Ausgabeumfang die Zusagen des Vertrags verletzt: Beide Zählwerte sind nicht negativ, und es wird höchstens so viel
	 * ausgegeben wie angefordert.
	 *
	 * @param umfang Der gemeldete Ausgabeumfang.
	 *
	 * @return true, wenn der Umfang die Zusagen verletzt, sonst false.
	 */
	private static boolean verletztInvarianten(final ReportingAusgabeumfang umfang) {
		return (umfang.angefordert() < 0) || (umfang.ausgegeben() < 0) || (umfang.ausgegeben() > umfang.angefordert());
	}

}
