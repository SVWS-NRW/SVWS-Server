package de.svws_nrw.controller.wiedervorlage;

import java.util.Set;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.WiedervorlageEintrag;
import de.svws_nrw.core.data.schule.WiedervorlageErledigungRequest;
import de.svws_nrw.data.Responses;
import de.svws_nrw.service.wiedervorlage.WiedervorlageCreateRequest;
import de.svws_nrw.service.wiedervorlage.WiedervorlagePatchRequest;
import de.svws_nrw.service.wiedervorlage.WiedervorlageService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

/**
 * Controller für Wiedervorlage-Endpunkte.
 * Enthält keine Geschäftslogik – delegiert ausschließlich an den {@link WiedervorlageService}.
 */
public final class WiedervorlageController {

	private final WiedervorlageService wiedervorlageService;

	/**
	 * Erstellt einen neuen {@link WiedervorlageController}.
	 *
	 * @param wiedervorlageService der zu verwendende Service
	 */
	public WiedervorlageController(final WiedervorlageService wiedervorlageService) {
		this.wiedervorlageService = wiedervorlageService;
	}

	/**
	 * Gibt alle Wiedervorlage-Einträge zurück, auf die der aktuelle Benutzer Zugriff hat.
	 *
	 * @return {@link Response} mit Liste aller {@link WiedervorlageEintrag}-Objekte
	 */
	public Response getAll() {
		return Responses.ok(wiedervorlageService.getAll());
	}

	/**
	 * Gibt einen einzelnen Wiedervorlage-Eintrag anhand der ID zurück.
	 *
	 * @param id die ID des Wiedervorlage-Eintrags
	 *
	 * @return {@link Response} mit dem zugehörigen {@link WiedervorlageEintrag}
	 */
	public Response get(final long id) {
		return Responses.ok(wiedervorlageService.get(id));
	}

	/**
	 * Erstellt einen neuen Wiedervorlage-Eintrag.
	 *
	 * @param request das Create-Request-DTO
	 *
	 * @return {@link Response} mit dem neu erstellten {@link WiedervorlageEintrag}
	 */
	public Response create(final WiedervorlageCreateRequest request) {
		BeanValidator.validate(request);

		return Responses.created(wiedervorlageService.create(request));
	}

	/**
	 * Aktualisiert einen bestehenden Wiedervorlage-Eintrag partiell.
	 *
	 * @param id      die ID des zu aktualisierenden Eintrags
	 * @param request das Patch-Request-DTO
	 *
	 * @return {@link Response} mit dem aktualisierten {@link WiedervorlageEintrag}
	 */
	public Response patch(final long id, final WiedervorlagePatchRequest request) {
		BeanValidator.validate(request);

		return Responses.ok(wiedervorlageService.patch(request, id));
	}

	/**
	 * Löscht einen Wiedervorlage-Eintrag anhand der ID.
	 *
	 * @param id die ID des zu löschenden Eintrags
	 *
	 * @return {@link Response} mit {@link SimpleOperationResponse} log
	 */
	public Response delete(final long id) {
		final var log = wiedervorlageService.delete(id);

		return Responses.ok(log);
	}

	/**
	 * Löscht einen Wiedervorlage-Eintrag anhand der ID.
	 *
	 * @param ids die IDs der zu löschenden Einträge
	 *
	 * @return {@link Response} mit Liste von {@link SimpleOperationResponse} logs
	 */
	public Response delete(final Set<Long> ids) {
		final var logs = wiedervorlageService.delete(ids);

		return Responses.ok(logs);
	}

	/**
	 * Gibt die Anzahl offener Wiedervorlagen des heutigen Datums zurück
	 *
	 * @return {@link Response} mit Anzahl offener Wiedervorlagen
	 */
	public Response getAnzahlOffeneWiedervorlagen() {
		final var anzahlOffenerWiedervorlagen = wiedervorlageService.getAnzahlOffeneWiedervorlagen();

		return Responses.ok(anzahlOffenerWiedervorlagen);
	}

	/**
	 * Markiert einen Wiedervorlage-Eintrag als erledigt.
	 *
	 * @param id die ID des als erledigt zu markierenden Eintrags
	 *
	 * @return {@link Response} mit dem aktualisierten {@link WiedervorlageEintrag}
	 */
	public Response markiereAlsErledigt(final long id) {
		return Responses.ok(wiedervorlageService.markiereAlsErledigt(id));
	}

	/**
	 * Setzt den Erledigungsstatus eines Wiedervorlage-Eintrags.
	 *
	 * @param id           die ID des Wiedervorlage-Eintrags, dessen Erledigungsstatus gesetzt werden soll
	 * @param patchRequest der Request-Body mit dem gewünschten Erledigungsstatus
	 *
	 * @return {@link Response} mit dem aktualisierten {@link WiedervorlageEintrag}
	 */
	public Response setErledigung(final long id, final WiedervorlageErledigungRequest patchRequest) {
		return Responses.ok(wiedervorlageService.setErledigung(id, patchRequest));
	}
}
