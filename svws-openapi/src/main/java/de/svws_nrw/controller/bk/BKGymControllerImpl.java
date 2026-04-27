package de.svws_nrw.controller.bk;

import de.svws_nrw.core.data.bk.abi.BKGymAbiturdaten;
import de.svws_nrw.core.data.bk.abi.BKGymLeistungen;
import de.svws_nrw.data.Responses;
import de.svws_nrw.service.bk.BKGymAbiturdatenService;
import de.svws_nrw.service.bk.BKGymLeistungsdatenService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden die Methoden zur Behandlung der API-Zugriffe im Bereich Berufliches Gymnasium gebündelt
 */
public final class BKGymControllerImpl implements BKGymController {

	/** Der zugehörige Abiturdaten-Service */
	private final BKGymAbiturdatenService abidatenService;

	/** Der zugehörige Leistungsdaten-Service */
	private final BKGymLeistungsdatenService leistungsdatenService;


	/**
	 * Erstellt für  die Datenbank-Verbindung eine neue Controller-Instanz.
	 *
	 * @param abidatenService         der Service für die Abiturdaten
	 * @param leistungsdatenService   der Service für die Leistungsdaten
	 *
	 */
	public BKGymControllerImpl(final BKGymAbiturdatenService abidatenService, final BKGymLeistungsdatenService leistungsdatenService) {
		this.abidatenService = abidatenService;
		this.leistungsdatenService = leistungsdatenService;
	}

	@Override
	public Response getAbiturdaten(final Long id) {
		final BKGymAbiturdaten abiturdaten = abidatenService.get(id);
		return Responses.ok(abiturdaten);
	}

	@Override
	public Response getLeistungsdaten(final Long id) {
		final BKGymLeistungen leistungsdaten = leistungsdatenService.get(id);
		return Responses.ok(leistungsdaten);
	}

}
