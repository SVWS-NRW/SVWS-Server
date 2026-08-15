package de.svws_nrw.module.reporting.factories;

import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.module.reporting.diagnose.ReportingHinweisSerializer;
import de.svws_nrw.module.reporting.html.contexts.initializer.HtmlContextInitializerRegistry;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import jakarta.ws.rs.core.Response;

/**
 * Ergänzt eine erfolgreiche Reporting-Antwort um den öffentlichen Hinweis-Header.
 * <p>Diese Klasse ist die einzige Stelle, die über das Setzen entscheidet - sonst träfen HTML-, PDF- und ZIP-Ausgabe die Entscheidung dreimal getrennt.
 * Gesetzt wird der Header nur, wenn die Registry den Datenaufbau als vollständig an die Diagnose angebunden führt; andernfalls fehlt er ganz, weil ein
 * {@code gesamt=0} an einem nicht angebundenen Pfad eine geprüfte Vollständigkeit behaupten würde. E-Mail bleibt außen vor, denn ihre JSON-Startantwort ist
 * kein Dokument.</p>
 */
final class ReportingHinweiseHeader {

	private ReportingHinweiseHeader() {
		throw new IllegalStateException("Hilfsklasse - Initialisierung nicht möglich.");
	}


	/**
	 * Ergänzt den übergebenen Antwortaufbau um den Hinweis-Header, sofern der Datenaufbau des Aufrufs vollständig an die Diagnose angebunden ist.
	 *
	 * @param response         Der Aufbau der erfolgreichen Antwort.
	 * @param reportingContext Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param bewusstLeer      true, wenn Datensätze angefordert waren und keiner übrig blieb.
	 *
	 * @return Derselbe Antwortaufbau, um den Header ergänzt oder unverändert.
	 */
	static Response.ResponseBuilder ergaenze(final Response.ResponseBuilder response, final ReportingContext reportingContext, final boolean bewusstLeer) {
		if (!istAngebunden(reportingContext)) {
			return response;
		}
		return response.header(ReportingHinweisSerializer.HEADER_NAME,
				ReportingHinweisSerializer.headerwert(reportingContext.ausgabeprobleme(), bewusstLeer));
	}

	/**
	 * Gibt an, ob der Datenaufbau dieses Aufrufs seine Ausgabeprobleme vollständig über die Diagnose meldet.
	 *
	 * @param reportingContext Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 *
	 * @return true, wenn die Anbindung vollständig ist, sonst false.
	 */
	private static boolean istAngebunden(final ReportingContext reportingContext) {
		final ReportingReportvorlage reportvorlage = reportingContext.reportingParameter().reportVorlage();
		if (reportvorlage == null) {
			return false;
		}
		return HtmlContextInitializerRegistry.istAnHinweisvertragAngebunden(reportvorlage.getReportingReportvorlageDatenContext());
	}

}
