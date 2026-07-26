package de.svws_nrw.controller.gost.klausuren;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.gost.klausuren.GostKlausurenVorgabeVorlagenService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden die API-Zugriffe für Vorlagenoperationen auf GOSt-Klausurvorgaben gebündelt.
 */
public final class GostKlausurenVorgabeVorlagenControllerImpl implements GostKlausurenVorgabeVorlagenController {

	private final GostKlausurenVorgabeVorlagenService gostKlausurenVorgabeVorlagenService;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param gostKlausurenVorgabeVorlagenService der zugehörige Service
	 */
	public GostKlausurenVorgabeVorlagenControllerImpl(final GostKlausurenVorgabeVorlagenService gostKlausurenVorgabeVorlagenService) {
		this.gostKlausurenVorgabeVorlagenService = gostKlausurenVorgabeVorlagenService;
	}

	@Override
	public Response copyVorlagenToJahrgang(final int abiturjahr, final int halbjahr, final int quartal) {
		return Responses.ok(gostKlausurenVorgabeVorlagenService.copyVorlagenToJahrgang(abiturjahr, halbjahr, quartal));
	}

	@Override
	public Response createMissingVorlagen(final int halbjahr, final int quartal) {
		return Responses.ok(gostKlausurenVorgabeVorlagenService.createMissingVorlagen(halbjahr, quartal));
	}

}
