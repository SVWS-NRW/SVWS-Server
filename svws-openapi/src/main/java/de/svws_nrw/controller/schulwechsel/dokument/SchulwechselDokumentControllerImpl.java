package de.svws_nrw.controller.schulwechsel.dokument;

import java.util.List;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schulwechsel.dokument.SchulwechselDokumentCreateRequest;
import de.svws_nrw.service.schulwechsel.dokument.SchulwechselDokumentPatchRequest;
import de.svws_nrw.service.schulwechsel.dokument.SchulwechselDokumentService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

/**
 * Controller für CRUD-Operationen auf {@code SchulwechselDokument}-Einträgen.
 */
public final class SchulwechselDokumentControllerImpl implements SchulwechselDokumentController {

	private final SchulwechselDokumentService service;

	/**
	 * Erstellt einen neuen {@code SchulwechselDokumentControllerImpl}.
	 *
	 * @param service der zugehörige Service
	 */
	public SchulwechselDokumentControllerImpl(final SchulwechselDokumentService service) {
		this.service = service;
	}

	/**
	 * Gibt alle Schulwechsel-Dokumente zurück.
	 *
	 * @return Response mit der Liste aller Dokumente
	 */
	@Override
	public Response getAll() {
		return Responses.ok(this.service.getAll());
	}

	/**
	 * Gibt ein Dokument anhand seiner ID zurück.
	 *
	 * @param id die ID des Dokuments
	 * @return Response mit dem gefundenen Dokument
	 */
	@Override
	public Response getById(final long id) {
		return Responses.ok(this.service.getById(id));
	}

	/**
	 * Erstellt ein neues Schulwechsel-Dokument.
	 *
	 * @param dto die Eingabedaten
	 * @return Response mit dem erstellten Dokument
	 */
	@Override
	public Response create(final SchulwechselDokumentCreateRequest dto) {
		BeanValidator.validate(dto);
		return Responses.created(this.service.create(dto));
	}

	/**
	 * Aktualisiert ein bestehendes Dokument teilweise.
	 *
	 * @param id  die ID des Dokuments
	 * @param dto die zu aktualisierenden Felder
	 * @return Response mit dem aktualisierten Dokument
	 */
	@Override
	public Response patch(final long id, final SchulwechselDokumentPatchRequest dto) {
		BeanValidator.validate(dto);
		return Responses.ok(this.service.patch(id, dto));
	}

	/**
	 * Löscht mehrere Dokumente anhand ihrer IDs.
	 *
	 * @param ids die zu löschenden IDs
	 * @return Response mit den Löschergebnissen
	 */
	@Override
	public Response delete(final List<Long> ids) {
		return Responses.ok(this.service.delete(ids));
	}
}
