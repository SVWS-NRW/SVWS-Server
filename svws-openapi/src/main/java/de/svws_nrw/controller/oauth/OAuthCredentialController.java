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

/**
 * Controller für die OAuth2-Credentials der Schule.
 *
 * <p>Der Controller enthält keine Fachlogik. Er validiert die Eingaben, delegiert an den
 * {@link OAuthCredentialService} und baut die HTTP-Antworten. Das Client-Secret wird in keiner Antwort
 * im Klartext zurückgegeben, sondern immer maskiert.</p>
 */
public class OAuthCredentialController {

	private final OAuthCredentialService service;
	private final OAuthCredentialsInternalMapper oAuthCredentialsInternalMapper;

	private static final String CREDENTIAL_NOT_FOUND = "Keine Credentials gefunden.";


	/**
	 * Erstellt einen neuen Controller.
	 *
	 * @param service                          der {@link OAuthCredentialService} mit der Fachlogik
	 * @param oAuthCredentialsInternalMapper   der {@link OAuthCredentialsInternalMapper} zur Umwandlung zwischen
	 *                                         dem internen und dem externen Modell
	 */
	public OAuthCredentialController(final OAuthCredentialService service, final OAuthCredentialsInternalMapper oAuthCredentialsInternalMapper) {
		this.service = service;
		this.oAuthCredentialsInternalMapper = oAuthCredentialsInternalMapper;
	}

	/**
	 * Legt neue Credentials an. Das Client-Secret wird verschlüsselt gespeichert.
	 *
	 * @param input   die {@link OAuthCreateCredential} mit den Daten der neuen Credentials
	 *
	 * @return die Antwort (201) mit den angelegten {@link OAuthCredentials} und maskiertem Client-Secret
	 *
	 */
	public Response create(final OAuthCreateCredential input) {
		BeanValidator.validate(input);

		final var result = service.create(oAuthCredentialsInternalMapper.toInternal(input));

		return Responses.created(oAuthCredentialsInternalMapper.fromInternal(result));
	}

	/**
	 * Liefert die Credentials zur übergebenen ID.
	 *
	 * @param id   die ID der Credentials
	 *
	 * @return die Antwort (200) mit den zugehörigen {@link OAuthCredentials} und maskiertem Client-Secret
	 *
	 */
	public Response get(final long id) {
		final var credential = service.get(id)
				.orElseThrow(() -> new ApiOperationException(
						Response.Status.NOT_FOUND, CREDENTIAL_NOT_FOUND));

		return Responses.ok(oAuthCredentialsInternalMapper.fromInternal(credential));
	}

	/**
	 * Löscht die Credentials zur übergebenen ID.
	 *
	 * @param id   die ID der Credentials
	 *
	 * @return die Antwort (200) mit dem Ergebnis der Löschoperation
	 */
	public Response delete(final long id) {
		final var log = service.delete(id);

		return Responses.ok(log);
	}

	/**
	 * Liefert alle bekannten Credentials.
	 *
	 * @return die Antwort (200) mit der Liste der {@link OAuthCredentials} und maskiertem Client-Secret
	 */
	public Response getAll() {
		final List<OAuthCredentials> results = service.getAll()
				.stream()
				.map(oAuthCredentialsInternalMapper::fromInternal)
				.toList();

		return Responses.ok(results);
	}

	/**
	 * Liefert alle bekannten Credentials einer Domäne.
	 *
	 * @param domain   der Name der {@link OAuthDomain}
	 *
	 * @return die Antwort (200) mit der Liste der {@link OAuthCredentials} der Domäne und maskiertem Client-Secret
	 *
	 */
	public Response getAll(final String domain) {
		final List<OAuthCredentials> results = service.getAll(OAuthDomain.valueOf(domain))
				.stream()
				.map(oAuthCredentialsInternalMapper::fromInternal)
				.toList();

		return Responses.ok(results);
	}
}
