package de.svws_nrw.controller.lehrer;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten;
import de.svws_nrw.data.Responses;
import de.svws_nrw.service.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenBatchPatchRequest;
import de.svws_nrw.service.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenCreateRequest;
import de.svws_nrw.service.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenPatchRequest;
import de.svws_nrw.service.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

public final class LehrerPersonalabschnittsdatenController {

	private final LehrerPersonalabschnittsdatenService service;

	/**
	 * Erstellt einen neuen {@code LehrerPersonalAbschnittsdatenController} mit dem angegebenen Service.
	 *
	 * @param service der {@link LehrerPersonalabschnittsdatenService}
	 */
	public LehrerPersonalabschnittsdatenController(final LehrerPersonalabschnittsdatenService service) {
		this.service = service;
	}

	/**
	 * Gibt eine {@link LehrerPersonalabschnittsdaten} anhand ihrer ID zurück.
	 *
	 * @param id die ID der LehrerPersonalabschnittsdaten
	 * @return eine Response mit den LehrerPersonalabschnittsdaten
	 */
	public Response get(final long id) {
		return Responses.ok(service.get(id));
	}

	/**
	 * Gibt alle {@link LehrerPersonalabschnittsdaten} zu einem Lehrerabschnitt zurück.
	 *
	 * @param ids die IDs der Lehrerabschnittsdaten
	 * @return eine Response mit der Liste der LehrerPersonalabschnittsdaten
	 */
	public Response getList(final List<Long> ids) {
		return Responses.ok(service.getList(ids));
	}

	/**
	 * Erstellt eine neue {@link LehrerPersonalabschnittsdaten}.
	 *
	 * @param dto das Request-Objekt mit den Daten für neue LehrerPersonalabschnittsdaten
	 * @return eine Response mit der erstellten LehrerPersonalabschnittsdaten
	 */
	public Response create(final LehrerPersonalabschnittsdatenCreateRequest dto) {
		BeanValidator.validate(dto);
		return Responses.created(service.create(dto));
	}

	/**
	 * Erstellt mehrere neue {@link LehrerPersonalabschnittsdaten}.
	 *
	 * @param dtos die Liste der Request-Objekte mit den Daten für die neuen LehrerPersonalabschnittsdaten
	 * @return eine Response mit den erstellten LehrerPersonalabschnittsdaten
	 */
	public Response createMultiple(final List<LehrerPersonalabschnittsdatenCreateRequest> dtos) {
		dtos.forEach(BeanValidator::validate);
		return Responses.created(service.createMultiple(dtos));
	}

	/**
	 * Aktualisiert eine bestehende {@link LehrerPersonalabschnittsdaten} teilweise (PATCH).
	 *
	 * @param id  die ID der zu aktualisierenden LehrerPersonalabschnittsdaten
	 * @param dto das Request-Objekt mit den zu aktualisierenden Feldern
	 * @return eine Response mit der aktualisierten LehrerPersonalabschnittsdaten
	 */
	public Response patch(final long id, final LehrerPersonalabschnittsdatenPatchRequest dto) {
		BeanValidator.validate(dto);
		return Responses.ok(service.patch(id, dto));
	}

	/**
	 * Aktualisiert mehrere bestehende {@link LehrerPersonalabschnittsdaten} teilweise (PATCH).
	 *
	 * @param dtos die Liste der Request-Objekte mit den zu aktualisierenden Feldern
	 * @return eine Response mit den aktualisierten LehrerPersonalabschnittsdaten
	 */
	public Response patchMultiple(final List<LehrerPersonalabschnittsdatenBatchPatchRequest> dtos) {
		dtos.forEach(BeanValidator::validate);
		return Responses.ok(service.patchMultiple(dtos));
	}

	/**
	 * Löscht eine {@link LehrerPersonalabschnittsdaten} anhand ihrer ID.
	 *
	 * @param id die ID des zu löschenden Eintrags
	 * @return eine Response mit dem Ergebnis der Löschoperation
	 */
	public Response delete(final long id) {
		return Responses.ok(service.delete(id));
	}

	/**
	 * Löscht mehrere {@link LehrerPersonalabschnittsdaten} anhand ihrer IDs.
	 *
	 * @param ids die IDs der zu löschenden Einträge
	 * @return eine Response mit den Ergebnissen der Löschoperationen
	 */
	public Response deleteMultiple(final Collection<Long> ids) {
		return Responses.ok(service.deleteMultiple(ids));
	}
}
