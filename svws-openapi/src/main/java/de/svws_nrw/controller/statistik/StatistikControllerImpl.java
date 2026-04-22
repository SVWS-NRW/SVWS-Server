package de.svws_nrw.controller.statistik;

import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.export.data.StatistikExport;
import de.svws_nrw.data.Responses;
import de.svws_nrw.service.statistik.StatistikService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden die Methoden zur Behandlung der API-Zugriffe im Bereich der amtlichen Schulstatistik gebündelt
 */
public final class StatistikControllerImpl implements StatistikController {

	/** Der zugehörige Service */
	private final StatistikService service;

	/**
	 * Erstellt für  die Datenbank-Verbindung eine neue Controller-Instanz.
	 *
	 * @param service        der zugehörige Service
	 */
	public StatistikControllerImpl(final StatistikService service) {
		this.service = service;
	}

	@Override
	public Response getStatistikGesamt() {
		final StatistikGesamt daten = service.get();
		return Responses.ok(daten);
	}

	@Override
	public Response getStatistikExport() {
		final StatistikExport daten = service.getExport();
		return Responses.ok(daten);
	}

}
