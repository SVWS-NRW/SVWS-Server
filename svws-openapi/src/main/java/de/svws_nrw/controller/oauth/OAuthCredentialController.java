package de.svws_nrw.controller.oauth;

import java.util.List;

import de.svws_nrw.core.data.oauth2.OAuthCredentials;
import de.svws_nrw.data.Responses;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.oauth.OAuthCredentialsInternalMapper;
import de.svws_nrw.service.oauth.credential.OAuthCreateCredential;
import de.svws_nrw.service.oauth.credential.OAuthCredentialService;
import de.svws_nrw.service.oauth.credential.OAuthDomainBerechtigung;
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

	private static final String CREDENTIAL_NOT_FOUND = "Keine Credentials gefunden.";

	private final OAuthCredentialService service;
	private final OAuthCredentialsInternalMapper oAuthCredentialsInternalMapper;
	private final OAuthDomainBerechtigung berechtigung;

	/**
	 * Erstellt einen neuen Controller.
	 *
	 * @param service                          der {@link OAuthCredentialService} mit der Fachlogik
	 * @param oAuthCredentialsInternalMapper   der {@link OAuthCredentialsInternalMapper} zur Umwandlung zwischen
	 *                                         dem internen und dem externen Modell
	 * @param berechtigung                     die {@link OAuthDomainBerechtigung} des angemeldeten Benutzers
	 */
	public OAuthCredentialController(final OAuthCredentialService service,
			final OAuthCredentialsInternalMapper oAuthCredentialsInternalMapper,
			final OAuthDomainBerechtigung berechtigung) {
		this.service = service;
		this.oAuthCredentialsInternalMapper = oAuthCredentialsInternalMapper;
		this.berechtigung = berechtigung;
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

		final var credential = oAuthCredentialsInternalMapper.toInternal(input);
		berechtigung.checkDomain(credential.serviceDomain());

		final var result = service.create(credential);

		return Responses.created(oAuthCredentialsInternalMapper.fromInternal(result));
	}

	/**
	 * Löscht die Credentials zur übergebenen ID.
	 *
	 * @param id   die ID der Credentials
	 *
	 * @return die Antwort (200) mit dem Ergebnis der Löschoperation
	 */
	public Response delete(final long id) {
		final var credential = service.get(id).orElseThrow(() -> new ApiOperationException(Response.Status.NOT_FOUND, CREDENTIAL_NOT_FOUND));
		berechtigung.checkDomain(credential.serviceDomain());

		final var log = service.delete(id);

		return Responses.ok(log);
	}

	/**
	 * Liefert alle bekannten Credentials, auf deren Domäne der Benutzer zugreifen darf.
	 *
	 * @return die Antwort (200) mit der Liste der {@link OAuthCredentials} und maskiertem Client-Secret
	 */
	public Response getAll() {
		final List<OAuthCredentials> results = service.getAll().stream()
				.filter(c -> berechtigung.isErlaubt(c.serviceDomain()))
				.map(oAuthCredentialsInternalMapper::fromInternal)
				.toList();

		return Responses.ok(results);
	}
}
