package de.svws_nrw.data.statistik;

import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import jakarta.ws.rs.core.Response;
import de.svws_nrw.data.Responses;

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

}
