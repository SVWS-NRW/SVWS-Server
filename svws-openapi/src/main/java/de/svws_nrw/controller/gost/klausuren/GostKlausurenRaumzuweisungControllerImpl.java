package de.svws_nrw.controller.gost.klausuren;

import java.util.List;

import de.svws_nrw.core.data.gost.klausuren.GostKlausurraumRich;
import de.svws_nrw.data.Responses;
import de.svws_nrw.service.gost.klausuren.GostKlausurenRaumzuweisungService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden Raumzuweisungen für GOSt-Schülerklausurtermine gebündelt.
 */
public final class GostKlausurenRaumzuweisungControllerImpl implements GostKlausurenRaumzuweisungController {

	private final GostKlausurenRaumzuweisungService service;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param service der Service
	 */
	public GostKlausurenRaumzuweisungControllerImpl(final GostKlausurenRaumzuweisungService service) {
		this.service = service;
	}

	@Override
	public Response setzeRaumzuweisungenFuerSchuelerklausurtermine(final List<GostKlausurraumRich> raumSchuelerZuteilung) {
		return Responses.ok(service.setzeRaumzuweisungenFuerSchuelerklausurtermine(raumSchuelerZuteilung));
	}

	@Override
	public Response loescheRaumzuweisungenFuerSchuelerklausurtermine(final List<Long> schuelerklausurterminIds) {
		return Responses.ok(service.loescheRaumzuweisungenFuerSchuelerklausurtermine(schuelerklausurterminIds));
	}

}
