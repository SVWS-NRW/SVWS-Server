
package de.svws_nrw.module.reporting.proxytypes.schueler.schule;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchSchule;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.schueler.schulbesuch.ReportingSchuelerSchulbesuchSchule;
import de.svws_nrw.module.reporting.proxytypes.schule.ProxyReportingSchulkatalogEintragNRW;

/**
 * Proxy-Klasse für die Darstellung von Schulbesuchsdaten einer Schule für das Reporting.
 * Diese Klasse erweitert die ReportingSchuelerSchulbesuchSchule und wird über die Übergabe
 * eines SchuelerSchulbesuchSchule-Objektes gefüllt.
 */
public class ProxyReportingSchuelerSchulbesuchSchule extends ReportingSchuelerSchulbesuchSchule {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/**
	 * Erstellt eine neue Proxy-Instanz aus einem SchuelerSchulbesuchSchule-Objekt.
	 *
	 * @param reportingRepository Repository für das Reporting.
	 * @param schulbesuch Das SchuelerSchulbesuchSchule-Objekt, aus dem die Proxy-Instanz erstellt wird
	 */
	public ProxyReportingSchuelerSchulbesuchSchule(final ReportingRepository reportingRepository, final SchuelerSchulbesuchSchule schulbesuch) {
		super(
				schulbesuch.datumVon,
				schulbesuch.datumBis,
				schulbesuch.abschlussartID,
				schulbesuch.entlassgrundID,
				schulbesuch.organisationsFormID,
				schulbesuch.jahrgangVon,
				schulbesuch.jahrgangBis,
				createSchulkatalogEintrag(reportingRepository, schulbesuch.idSchule),
				schulbesuch.schulgliederung
		);
		this.reportingRepository = reportingRepository;
	}

	/**
	 * Erstellt einen ProxyReportingSchulkatalogEintragNRW aus der Schul-ID.
	 *
	 * @param reportingRepository Repository für das Reporting
	 * @param idSchule Die ID der Schule
	 * @return Ein ProxyReportingSchulkatalogEintragNRW-Objekt oder null, wenn die Schule nicht gefunden wurde
	 */
	private static ProxyReportingSchulkatalogEintragNRW createSchulkatalogEintrag(final ReportingRepository reportingRepository, final Long idSchule) {
		if (idSchule == null)
			return null;

		final var schulEintrag = reportingRepository.katalogSchulen().get(idSchule);
		if (schulEintrag == null)
			return null;

		return new ProxyReportingSchulkatalogEintragNRW(reportingRepository, schulEintrag);
	}

	// ##### Getter #####

	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 *
	 * @return Repository für das Reporting
	 */
	public ReportingRepository reportingRepository() {
		return reportingRepository;
	}
}
