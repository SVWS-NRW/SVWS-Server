package de.svws_nrw.controller.schule.logoverwaltung;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import de.svws_nrw.core.data.schule.Logo;
import de.svws_nrw.data.Responses;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.schule.logoverwaltung.DataUrlResolver;
import de.svws_nrw.service.schule.logoverwaltung.LogoCreateRequest;
import de.svws_nrw.service.schule.logoverwaltung.LogoPatchRequest;
import de.svws_nrw.service.schule.logoverwaltung.LogoverwaltungService;
import de.svws_nrw.base.compression.Zip;
import de.svws_nrw.validation.BeanValidator;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.Response;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.CollectionUtils;

public final class LogoverwaltungControllerImpl implements LogoverwaltungController {

	private final LogoverwaltungService service;

	/**
	 * @param service {@link LogoverwaltungService}
	 */
	public LogoverwaltungControllerImpl(final LogoverwaltungService service) {
		this.service = service;
	}

	/**
	 * Ruft alle Logo-Entitäten ab.
	 *
	 * @return {@link Response} mit Status {@code 200 OK}, den Logo-Entitäten als Body
	 */
	@Override
	public Response getAll() {
		return Responses.ok(service.getAll());
	}

	/**
	 * Gibt mehrere Logos als ZIP-Archiv zurück.
	 *
	 * <p>Für jede übergebene Logo-ID wird das zugehörige Logo aus dem System geladen.
	 * Der Base64-kodierte Bildinhalt wird dekodiert und zusammen mit einem Dateinamen,
	 * der sich aus der Kennung und der Dateiendung des jeweiligen Logos zusammensetzt,
	 * in ein ZIP-Archiv verpackt.
	 *
	 * <p>Der Dateiname im Archiv folgt dem Schema: {@code <kennung>.<extension>},
	 * z. B. {@code logo_abc.png}. Ist keine Dateiendung ermittelbar, wird ausschließlich
	 * die Kennung als Dateiname verwendet.
	 *
	 * @param ids Liste der Logo-IDs, für die das ZIP-Archiv erstellt werden soll.
	 *            Darf nicht {@code null} oder leer sein.
	 * @return {@link Response} mit Status {@code 200 OK}, dem ZIP-Archiv als Body
	 *         ({@code Content-Type: application/zip}) sowie den Headern
	 *         {@code Content-Disposition: attachment; filename="logos.zip"} und
	 *         {@code Content-Length}.
	 * @throws ApiOperationException mit Status {@code 400 BAD_REQUEST},
	 *         wenn {@code ids} leer oder {@code null} ist.
	 * @throws ApiOperationException mit Status {@code 500 INTERNAL_SERVER_ERROR},
	 *         wenn der Base64-Inhalt eines Logos nicht gelesen werden kann.
	 */
	@Override
	public Response getByIdsAsZip(final List<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Es muss mindestens eine Logo ID im Request übergeben werden.");
		}

		final var logos = service.getByIds(ids);

		final Map<String, byte[]> fileNameToBytes = createZipArchiveEntries(logos);

		final byte[] zipBytes = Zip.createArchive(fileNameToBytes);

		return Response.ok(zipBytes)
				.header("Content-Disposition", "attachment; filename=\"logos.zip\"")
				.header("Content-Length", zipBytes.length)
				.build();
	}

	/**
	 * Erstellt eine neue Logo-Entität.
	 *
	 * @param createRequest das Request-Objekt mit den Daten für die neue Logo-Entität
	 *
	 * @return eine Response mit der erstellten Logo-Entität
	 *
	 * @throws ValidationException wenn die DTO-Validierung fehlschlägt
	 */
	@Override
	public Response create(final LogoCreateRequest createRequest) {
		BeanValidator.validate(createRequest);
		final var created = service.create(createRequest);
		return Responses.created(created);
	}

	/**
	 * Aktualisiert eine bestehende Logo-Entität teilweise.
	 * Das Request-DTO wird vor der Verarbeitung validiert.
	 *
	 * @param id die ID der zu aktualisierenden Logo-Entität
	 * @param patchRequest das Request-Objekt mit den zu aktualisierenden Feldern
	 *
	 * @return eine Response mit der aktualisierten Logo-Entität
	 *
	 * @throws ValidationException wenn die DTO-Validierung fehlschlägt
	 */
	@Override
	public Response patch(final long id, final LogoPatchRequest patchRequest) {
		BeanValidator.validate(patchRequest);
		final var patched = this.service.patch(id, patchRequest);
		return Responses.ok(patched);
	}

	/**
	 * Löscht eine Logo-Entität anhand ihrer ID.
	 *
	 * @param id die ID der zu löschenden Logo-Entität
	 *
	 * @return eine Response mit dem Löschergebnis
	 */
	@Override
	public Response delete(final Long id) {
		final var response = service.delete(id);
		return Responses.ok(response);
	}

	/**
	 * Löscht mehrere Logo-Entitäten anhand ihrer IDs.
	 *
	 * @param ids eine Liste von IDs der zu löschenden Logo-Entitäten
	 * @return eine Response mit den Löschergebnissen
	 */
	@Override
	public Response delete(final List<Long> ids) {
		final var responses = service.delete(ids);
		return Responses.ok(responses);
	}

	private static Map<String, byte[]> createZipArchiveEntries(final List<Logo> logos) {
		final Map<String, byte[]> fileNameToBytes = new HashMap<>();
		for (final var logo : logos) {
			final var dataUrl = DataUrlResolver.resolve(logo.logoBase64)
					.orElseThrow(() -> new ApiOperationException(
							Response.Status.INTERNAL_SERVER_ERROR,
							"Fehler beim Erstellen des ZIP-Archives: Das Base64 des Logos mit der ID %d konnte nicht gelesen werden.".formatted(logo.id)
					));

			final var fileName = Optional.ofNullable(dataUrl.fileExtension())
					.map(extension -> String.format("%s%s", logo.kennung, extension))
					.orElse(logo.kennung);
			final var fileData = Base64.decodeBase64(dataUrl.payload());

			fileNameToBytes.put(fileName, fileData);
		}

		return fileNameToBytes;
	}

}
