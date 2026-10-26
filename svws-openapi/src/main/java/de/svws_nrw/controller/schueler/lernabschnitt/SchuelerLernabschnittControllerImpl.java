package de.svws_nrw.controller.schueler.lernabschnitt;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schueler.lernabschnitt.SchuelerLernabschnittService;
import de.svws_nrw.service.schueler.lernabschnitt.SchuelerLernabschnittBemerkungenPatchRequest;
import de.svws_nrw.service.schueler.lernabschnitt.SchuelerLernabschnittCreateRequest;
import de.svws_nrw.service.schueler.lernabschnitt.SchuelerLernabschnittsdatenPatchRequest;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

/**
 * Implementierung des Controllers für die Lernabschnittsdaten eines Schülers.
 */
public final class SchuelerLernabschnittControllerImpl implements SchuelerLernabschnittController {

	private final SchuelerLernabschnittService service;

	/**
	 * Erstellt einen neuen {@code SchuelerLernabschnittController} mit dem angegebenen Service.
	 *
	 * @param service der {@link SchuelerLernabschnittService}
	 */
	public SchuelerLernabschnittControllerImpl(final SchuelerLernabschnittService service) {
		this.service = service;
	}

	@Override
	public Response get(final long id) {
		return Responses.ok(service.get(id));
	}

	@Override
	public Response getListBySchuelerAndSchuljahresabschnitt(final long idSchueler, final long idSchuljahresabschnitt) {
		return Responses.ok(service.getListBySchuelerAndSchuljahresabschnitt(idSchueler, idSchuljahresabschnitt));
	}

	@Override
	public Response create(final SchuelerLernabschnittCreateRequest dto) {
		BeanValidator.validate(dto);
		return Responses.created(service.create(dto));
	}

	@Override
	public Response patch(final long id, final SchuelerLernabschnittsdatenPatchRequest dto) {
		BeanValidator.validate(dto);
		service.patch(id, dto);
		return Responses.noContent();
	}

	@Override
	public Response patchBemerkungen(final long id, final SchuelerLernabschnittBemerkungenPatchRequest dto) {
		BeanValidator.validate(dto);
		service.patchBemerkungen(id, dto);
		return Responses.noContent();
	}
}
