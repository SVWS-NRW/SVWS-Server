package de.svws_nrw.controller.schild3;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schild3.Schild3FachklasseDQRNiveauZuordnungService;
import jakarta.ws.rs.core.Response;

public final class Schild3FachklasseSchild3FachklasseDQRNiveauZuordnungZuordnungImpl implements Schild3FachklasseDQRNiveauZuordnungController {

	private final Schild3FachklasseDQRNiveauZuordnungService service;

	/**
	 * Initialisiert einen neuen Controller
	 *
	 * @param service {@link Schild3FachklasseDQRNiveauZuordnungService}
	 */
	public Schild3FachklasseSchild3FachklasseDQRNiveauZuordnungZuordnungImpl(final Schild3FachklasseDQRNiveauZuordnungService service) {
		this.service = service;
	}

	@Override
	public Response getAll() {
		final var daten = service.getAll();
		return Responses.ok(daten);
	}

}
