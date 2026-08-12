package de.svws_nrw.controller.katalog.teilleistungsart;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.kataloge.Teilleistungsart;
import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schule.katalog.teilleistungsart.TeilleistungsartCreateRequest;
import de.svws_nrw.service.schule.katalog.teilleistungsart.TeilleistungsartPatchRequest;
import de.svws_nrw.service.schule.katalog.teilleistungsart.TeilleistungsartService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

public final class TeilleistungsartController {

	private final TeilleistungsartService teilLeistungsartenService;

	/**
	 * Initialisiert einen neuen Controller
	 *
	 * @param teilLeistungsartenService {@link TeilleistungsartService}
	 */
	public TeilleistungsartController(final TeilleistungsartService teilLeistungsartenService) {
		this.teilLeistungsartenService = teilLeistungsartenService;
	}

	/**
	 * Liefert system-bekannte {@link Teilleistungsart}
	 *
	 * @return Liste von {@link Teilleistungsart} als Response
	 */
	public Response getAll() {
		final List<Teilleistungsart> daten = teilLeistungsartenService.getAll();

		return Responses.ok(daten);
	}


	/**
	 * Erstellt neue {@link Teilleistungsart}
	 *
	 * @param input {@link Teilleistungsart}
	 * @return erstellte {@link Teilleistungsart} als Response
	 */
	public Response create(final TeilleistungsartCreateRequest input) {
		BeanValidator.validate(input);

		final Teilleistungsart created = teilLeistungsartenService.create(input);

		return Responses.created(created);
	}

	/**
	 * Löscht alle nicht referenzierten {@link Teilleistungsart} und gibt ein Log als {@link de.svws_nrw.core.data.SimpleOperationResponse} zurück.
	 *
	 * @param ids zu löschende {@link Teilleistungsart} anhand der ID.
	 * @return Liste {@link de.svws_nrw.core.data.SimpleOperationResponse} - Aktions-logs als Response
	 */
	public Response delete(final List<Long> ids) {
		final List<SimpleOperationResponse> deleted = teilLeistungsartenService.delete(ids);

		return Responses.ok(deleted);
	}

	/**
	 * Patcht Eigenschaften vorhandener {@link Teilleistungsart}.
	 *
	 * @param id ID der zu patchenden Resource.
	 * @param patch Delta als {@link JsonNode}
	 * @return upgedatetes Objekt.
	 */
	public Response patch(final long id, final TeilleistungsartPatchRequest patch) {
		BeanValidator.validate(patch);

		final Teilleistungsart updated = teilLeistungsartenService.patch(id, patch);

		return Responses.ok(updated);
	}

}
