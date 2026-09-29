package de.svws_nrw.controller.abschluesse;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.abschluesse.AbschlussPatchRequest;
import de.svws_nrw.service.abschluesse.AbschlussService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

/**
 * Die Implementierung für den Controller mit den Methoden für die Abschlüsse
 */
public final class AbschlussControllerImpl implements AbschlussController {

	private final AbschlussService service;

	/**
	 * @param service {@link AbschlussService}
	 */
	public AbschlussControllerImpl(final AbschlussService service) {
		this.service = service;
	}

	@Override
	public Response getByIdSchuelerAndIdSchuljahresabschnitt(final long idSchueler, final long idSchuljahresabschnitt)  {
		return Responses.ok(service.getByIdSchuelerAndIdSchuljahresabschnitt(idSchueler, idSchuljahresabschnitt));
	}

	@Override
	public Response getByIdLernabschnitt(final long idLernabschnitt) {
		return Responses.ok(service.getByIdLernabschnitt(idLernabschnitt));
	}

	@Override
	public Response patch(final long idLernabschnitt, final AbschlussPatchRequest patch) {
		BeanValidator.validate(patch);
		final var patched = this.service.patch(idLernabschnitt, patch);
		return Responses.ok(patched);
	}

}
