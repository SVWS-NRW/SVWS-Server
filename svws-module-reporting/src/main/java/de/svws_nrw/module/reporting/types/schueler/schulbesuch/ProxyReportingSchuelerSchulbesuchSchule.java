
package de.svws_nrw.module.reporting.types.schueler.schulbesuch;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchSchule;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.schule.ProxyReportingSchulkatalogEintragNRW;

/**
 * Proxy-Klasse für die Darstellung von Schulbesuchsdaten einer Schule für das Reporting.
 * Diese Klasse erweitert die ReportingSchuelerSchulbesuchSchule und wird über die Übergabe
 * eines SchuelerSchulbesuchSchule-Objektes gefüllt.
 */
public class ProxyReportingSchuelerSchulbesuchSchule extends ReportingSchuelerSchulbesuchSchule {

	/** Context mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingContext reportingContext;

	/**
	 * Erstellt eine neue Proxy-Instanz aus einem SchuelerSchulbesuchSchule-Objekt.
	 *
	 * @param reportingContext Repository für das Reporting.
	 * @param schulbesuch Das SchuelerSchulbesuchSchule-Objekt, aus dem die Proxy-Instanz erstellt wird
	 */
	public ProxyReportingSchuelerSchulbesuchSchule(final ReportingContext reportingContext, final SchuelerSchulbesuchSchule schulbesuch) {
		super(
				ersetzeNullBlankTrim(schulbesuch.datumVon),
				ersetzeNullBlankTrim(schulbesuch.datumBis),
				schulbesuch.idAbschlussart,
				schulbesuch.idEntlassgrund,
				schulbesuch.idOrganisationsform,
				ersetzeNullBlankTrim(schulbesuch.jahrgangVon),
				ersetzeNullBlankTrim(schulbesuch.jahrgangBis),
				createSchulkatalogEintrag(reportingContext, schulbesuch.idSchule),
				ersetzeNullBlankTrim(schulbesuch.schluesselSchulgliederung)
		);
		this.reportingContext = reportingContext;
	}

	/**
	 * Erstellt einen ProxyReportingSchulkatalogEintragNRW aus der Schul-ID.
	 *
	 * @param reportingContext Repository für das Reporting
	 * @param idSchule Die ID der Schule
	 * @return Ein ProxyReportingSchulkatalogEintragNRW-Objekt oder null, wenn die Schule nicht gefunden wurde
	 */
	private static ProxyReportingSchulkatalogEintragNRW createSchulkatalogEintrag(final ReportingContext reportingContext, final Long idSchule) {
		if (idSchule == null) {
			return null;
		}

		final var schulEintrag = reportingContext.repositoryKataloge().schule(idSchule);
		if (schulEintrag == null) {
			return null;
		}

		return new ProxyReportingSchulkatalogEintragNRW(reportingContext, schulEintrag);
	}

	// ##### Getter #####

	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 *
	 * @return Repository für das Reporting
	 */
	public ReportingContext reportingContext() {
		return reportingContext;
	}
}
