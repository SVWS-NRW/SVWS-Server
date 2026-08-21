package de.svws_nrw.controller.oauth;

import java.util.List;

import de.svws_nrw.core.data.oauth2.OAuthCredentials;
import de.svws_nrw.data.Responses;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.oauth.OAuthCredentialsInternalMapper;
import de.svws_nrw.oauth.internal.OAuthDomain;
import de.svws_nrw.service.oauth.credential.OAuthCredentialService;
import de.svws_nrw.service.oauth.credential.OAuthCreateCredential;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

public class OAuthCredentialController {

	private final OAuthCredentialService service;
	private final OAuthCredentialsInternalMapper oAuthCredentialsInternalMapper;

	private static final String CREDENTIAL_NOT_FOUND = "Keine Credentials gefunden.";


	/**
	 * Konstruktor
	 *
	 * @param service                        {@link OAuthCredentialService}
	 * @param oAuthCredentialsInternalMapper {@link OAuthCredentialsInternalMapper}
	 */
	public OAuthCredentialController(final OAuthCredentialService service, final OAuthCredentialsInternalMapper oAuthCredentialsInternalMapper) {
		this.service = service;
		this.oAuthCredentialsInternalMapper = oAuthCredentialsInternalMapper;
	}

	/**
	 * Fügt Credentials zur Laufzeit hinzu.
	 *
	 * @param input {@link OAuthCreateCredential}
	 * @return erzeugtes Entity
	 */
	public Response create(final OAuthCreateCredential input) {
		BeanValidator.validate(input);

		final var result = service.create(oAuthCredentialsInternalMapper.toInternal(input));

		return Responses.created(result);
	}

	/**
	 * Liefert Credential pro ID
	 *
	 * @param id die ID der Zugangsdaten
	 * @return zugehörige Credentials
	 */
	public Response get(final long id) {
		final var credential = service.get(id)
				.orElseThrow(() -> new ApiOperationException(
						Response.Status.NOT_FOUND, CREDENTIAL_NOT_FOUND));

		return Responses.ok(oAuthCredentialsInternalMapper.fromInternal(credential));
	}

	/**
	 * Löscht Credentials per ID
	 *
	 * @param id Primary key
	 * @return 204 No Content
	 */
	public Response delete(final long id) {
		final var log = service.delete(id);

		return Responses.ok(log);
	}

	/**
	 * Liefert alle bekannten Client Credentials
	 *
	 * @return Liste von Credentials
	 */
	public Response getAll() {
		final List<OAuthCredentials> results = service.getAll()
				.stream()
				.map(oAuthCredentialsInternalMapper::fromInternal)
				.toList();

		return Responses.ok(results);
	}

	/**
	 * Liefert alle bekannten Client Credentials einer zugehörigen Domäne
	 *
	 * @param domain die Domäne
	 * @return Liste von Credentials
	 */
	public Response getAll(final String domain) {
		final List<OAuthCredentials> results = service.getAll(OAuthDomain.valueOf(domain))
				.stream()
				.map(oAuthCredentialsInternalMapper::fromInternal)
				.toList();

		return Responses.ok(results);
	}
}
