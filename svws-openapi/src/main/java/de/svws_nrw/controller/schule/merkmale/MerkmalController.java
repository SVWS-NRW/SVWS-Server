package de.svws_nrw.controller.schule.merkmale;

import java.util.List;

import de.svws_nrw.data.Responses;
import de.svws_nrw.service.schule.katalog.merkmal.MerkmalCreateRequest;
import de.svws_nrw.service.schule.katalog.merkmal.MerkmalPatchRequest;
import de.svws_nrw.service.schule.katalog.merkmal.MerkmalService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.Response;


public final class MerkmalController {

	private final MerkmalService merkmalService;

	/**
	 * Erstellt einen neuen MerkmalController mit dem angegebenen Service.
	 *
	 * @param merkmalService der Service zur Verarbeitung der Merkmal-Geschäftslogik
	 */
	public MerkmalController(final MerkmalService merkmalService) {
		this.merkmalService = merkmalService;
	}

	/**
	 * Ruft alle Merkmal-Entitäten ab.
	 *
	 * @return eine Response mit der Liste aller Merkmal-Entitäten
	 */
	public Response getAll() {
		final var merkmale = this.merkmalService.getAll();
		return Responses.ok(merkmale);
	}

	/**
	 * Erstellt eine neue Merkmal-Entität.
	 * <p>
	 * Das Request-DTO wird vor der Verarbeitung validiert.
	 * </p>
	 *
	 * @param dto das Request-Objekt mit den Daten für das neue Merkmal
	 * @return eine Response mit der erstellten Merkmal-Entität
	 * @throws ValidationException wenn die DTO-Validierung fehlschlägt
	 */
	public Response create(final MerkmalCreateRequest dto) {
		BeanValidator.validate(dto);
		final var created = merkmalService.create(dto);
		return Responses.created(created);
	}

	/**
	 * Löscht mehrere Merkmal-Entitäten anhand ihrer IDs.
	 *
	 * @param ids eine Liste von IDs der zu löschenden Merkmal-Entitäten
	 * @return eine Response mit den Löschergebnissen
	 */
	public Response delete(final List<Long> ids) {
		final var responses = this.merkmalService.delete(ids);
		return Responses.ok(responses);
	}


	/**
	 * Aktualisiert eine bestehende Merkmal-Entität teilweise.
	 * <p>
	 * Das Request-DTO wird vor der Verarbeitung validiert.
	 * </p>
	 *
	 * @param id die ID der zu aktualisierenden Merkmal-Entität
	 * @param dto das Request-Objekt mit den zu aktualisierenden Feldern
	 * @return eine Response mit der aktualisierten Merkmal-Entität
	 * @throws ValidationException wenn die DTO-Validierung fehlschlägt
	 */
	public Response patch(final long id, final MerkmalPatchRequest dto) {
		BeanValidator.validate(dto);
		final var patched = this.merkmalService.patch(id, dto);
		return Responses.ok(patched);
	}

}
