package de.svws_nrw.controller.lehrer;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.asd.data.lehrer.LehrerFunktion;
import de.svws_nrw.data.Responses;
import de.svws_nrw.service.lehrer.funktion.LehrerFunktionBatchPatchRequest;
import de.svws_nrw.service.lehrer.funktion.LehrerFunktionCreateRequest;
import de.svws_nrw.service.lehrer.funktion.LehrerFunktionPatchRequest;
import de.svws_nrw.service.lehrer.funktion.LehrerFunktionService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

public final class LehrerFunktionController {

	private final LehrerFunktionService service;

	/**
	 * Erstellt einen neuen {@code LehrerFunktionController} mit dem angegebenen Service.
	 *
	 * @param service der {@link LehrerFunktionService}
	 */
	public LehrerFunktionController(final LehrerFunktionService service) {
		this.service = service;
	}

	/**
	 * Gibt eine {@link LehrerFunktion} anhand ihrer ID zurück.
	 *
	 * @param id die ID der Lehrerfunktion
	 * @return eine Response mit der Lehrerfunktion
	 */
	public Response get(final long id) {
		return Responses.ok(service.get(id));
	}

	/**
	 * Gibt alle {@link LehrerFunktion}en zurück.
	 *
	 * @return eine Response mit der Liste aller Lehrerfunktionen
	 */
	public Response getAll() {
		return Responses.ok(service.getAll());
	}

	/**
	 * Gibt alle {@link LehrerFunktion}en zu einem Lehrerabschnitt zurück.
	 *
	 * @param idAbschnitt die ID der Lehrerabschnittsdaten
	 * @return eine Response mit der Liste der Lehrerfunktionen
	 */
	public Response getListByIdAbschnitt(final long idAbschnitt) {
		return Responses.ok(service.getListByIdAbschnitt(idAbschnitt));
	}

	/**
	 * Erstellt eine neue {@link LehrerFunktion}.
	 *
	 * @param dto das Request-Objekt mit den Daten für die neue Lehrerfunktion
	 * @return eine Response mit der erstellten Lehrerfunktion
	 */
	public Response create(final LehrerFunktionCreateRequest dto) {
		BeanValidator.validate(dto);
		return Responses.created(service.create(dto));
	}

	/**
	 * Erstellt mehrere neue {@link LehrerFunktion}en.
	 *
	 * @param dtos die Liste der Request-Objekte mit den Daten für die neuen Lehrerfunktionen
	 * @return eine Response mit den erstellten Lehrerfunktionen
	 */
	public Response createMultiple(final List<LehrerFunktionCreateRequest> dtos) {
		dtos.forEach(BeanValidator::validate);
		return Responses.created(service.createMultiple(dtos));
	}

	/**
	 * Aktualisiert eine bestehende {@link LehrerFunktion} teilweise (PATCH).
	 *
	 * @param id  die ID der zu aktualisierenden Lehrerfunktion
	 * @param dto das Request-Objekt mit den zu aktualisierenden Feldern
	 * @return eine Response mit der aktualisierten Lehrerfunktion
	 */
	public Response patch(final long id, final LehrerFunktionPatchRequest dto) {
		BeanValidator.validate(dto);
		return Responses.ok(service.patch(id, dto));
	}

	/**
	 * Aktualisiert mehrere bestehende {@link LehrerFunktion}en teilweise (PATCH).
	 *
	 * @param dtos die Liste der Request-Objekte mit den zu aktualisierenden Feldern
	 * @return eine Response mit den aktualisierten Lehrerfunktionen
	 */
	public Response patchMultiple(final List<LehrerFunktionBatchPatchRequest> dtos) {
		dtos.forEach(BeanValidator::validate);
		return Responses.ok(service.patchMultiple(dtos));
	}

	/**
	 * Löscht eine {@link LehrerFunktion} anhand ihrer ID.
	 *
	 * @param id die ID des zu löschenden Eintrags
	 * @return eine Response mit dem Ergebnis der Löschoperation
	 */
	public Response delete(final long id) {
		return Responses.ok(service.delete(id));
	}

	/**
	 * Löscht mehrere {@link LehrerFunktion}en anhand ihrer IDs.
	 *
	 * @param ids die IDs der zu löschenden Einträge
	 * @return eine Response mit den Ergebnissen der Löschoperationen
	 */
	public Response deleteMultiple(final Collection<Long> ids) {
		return Responses.ok(service.deleteMultiple(ids));
	}
}
