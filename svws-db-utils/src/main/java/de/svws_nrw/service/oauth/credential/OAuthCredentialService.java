package de.svws_nrw.service.oauth.credential;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.service.utils.BulkDeleteUtils;
import java.util.List;
import java.util.Optional;

import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.oauth.OAuthCredentialMapper;
import de.svws_nrw.mapper.oauth.OAuthDomainMapper;
import de.svws_nrw.oauth.internal.CredentialStore;
import de.svws_nrw.oauth.internal.Credentials;
import de.svws_nrw.oauth.internal.OAuthDomain;
import de.svws_nrw.repo.oauth.credential.OAuthCredentialRepository;
import de.svws_nrw.service.crypto.secret.SecretCipher;
import jakarta.ws.rs.core.Response;

/**
 * Service zur Verwaltung von OAuth-Zugangsdaten.
 *
 * <p>Dient ueber {@link CredentialStore} zugleich als Credential-Quelle des OAuth-Token-Flows.
 * Das Client-Secret wird verschluesselt gespeichert. Nur {@link #get(OAuthDomain)} liefert es entschluesselt,
 * alle anderen Methoden liefern es maskiert ({@link #MASKED_SECRET}).
 */
public class OAuthCredentialService implements CredentialStore {


	private final OAuthCredentialRepository repository;
	private final OAuthCredentialMapper mapper;
	private final OAuthDomainMapper oAuthDomainMapper;
	private final SecretCipher secretCipher;

	private static final String CREDENTIAL_PRESENT_FOR_DOMAIN = "Es existiert bereits ein Datensatz zu dieser Domäne";
	private static final String MASKED_SECRET = "*******";

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param repository        Repository für Zugangsdaten
	 * @param mapper            Mapper für Client Credentials
	 * @param oAuthDomainMapper Mapper für Domänen
	 * @param secretCipher      Komponente zur Ver- und Entschlüsselung des Client-Secrets
	 */
	public OAuthCredentialService(
			final OAuthCredentialRepository repository,
			final OAuthCredentialMapper mapper, final OAuthDomainMapper oAuthDomainMapper,
			final SecretCipher secretCipher) {
		this.repository = repository;
		this.mapper = mapper;
		this.oAuthDomainMapper = oAuthDomainMapper;
		this.secretCipher = secretCipher;
	}

	/**
	 * Gibt alle OAuth-Zugangsdaten mit maskiertem Client-Secret zurück.
	 *
	 * @return Liste aller Zugangsdaten
	 */
	public List<Credentials> getAll() {
		return repository.getAll()
				.stream()
				.map(mapper::fromDomain)
				.map(OAuthCredentialService::masked)
				.toList();
	}

	/**
	 * Gibt alle OAuth-Zugangsdaten der übergebenen Domäne mit maskiertem Client-Secret zurück.
	 *
	 * @param domain die Domäne
	 * @return Liste der Zugangsdaten der Domäne
	 */
	public List<Credentials> getAll(final OAuthDomain domain) {
		return repository.findAllByServiceDomain(oAuthDomainMapper.toDomain(domain))
				.stream()
				.map(mapper::fromDomain)
				.map(OAuthCredentialService::masked)
				.toList();
	}

	/**
	 * Gibt die Zugangsdaten zur übergebenen ID mit maskiertem Client-Secret zurück, falls vorhanden.
	 *
	 * @param id die ID der Zugangsdaten
	 * @return die Zugangsdaten, sofern vorhanden
	 */
	public Optional<Credentials> get(final long id) {
		return repository.findById(id)
				.map(mapper::fromDomain)
				.map(OAuthCredentialService::masked);
	}

	/**
	 * Gibt die Zugangsdaten zur uebergebenen Domaene mit entschluesseltem Client-Secret zurueck, falls vorhanden.
	 *
	 * @param domain die Domaene
	 *
	 * @return die Zugangsdaten der Domaene, sofern vorhanden
	 *
	 * @throws ApiOperationException (500) falls das Client-Secret nicht entschluesselt werden kann
	 */
	@Override
	public Optional<Credentials> get(final OAuthDomain domain) {
		return repository.findByServiceDomain(oAuthDomainMapper.toDomain(domain))
				.map(mapper::fromDomain)
				.map(this::decrypted);
	}

	/**
	 * Erstellt neue OAuth-Zugangsdaten. Das Client-Secret wird verschlüsselt gespeichert.
	 *
	 * @param credentials zu erstellende Zugangsdaten
	 * @return erstellte Zugangsdaten mit maskiertem Client-Secret
	 *
	 * @throws ApiOperationException (400) falls für die Domäne bereits Zugangsdaten existieren,
	 *                               (500) falls das Client-Secret nicht verschlüsselt werden kann
	 */
	public Credentials create(final Credentials credentials) {
		return TransactionSupport.transactional(() -> {
			final var existing = repository.findByServiceDomain(oAuthDomainMapper.toDomain(credentials.serviceDomain()));
			if (existing.isPresent()) {
				throw new ApiOperationException(Response.Status.BAD_REQUEST, CREDENTIAL_PRESENT_FOR_DOMAIN);
			}

			final var encrypted = withSecret(credentials, secretCipher.encrypt(credentials.clientSecret()));
			final var entity = mapper.toDomain(encrypted);

			repository.create(entity);

			return masked(mapper.fromDomain(entity));
		});
	}

	/**
	 * Löscht OAuth-Zugangsdaten.
	 *
	 * @param id ID der Zugangsdaten
	 *
	 * @return {@link SimpleOperationResponse}
	 */
	public SimpleOperationResponse delete(final long id) {
		return TransactionSupport.transactional(() ->
				BulkDeleteUtils.delete(
						List.of(id),
						repository,
						e -> e.id,
						"OAuth-Credentials"
				).getFirst());
	}

	private Credentials decrypted(final Credentials credentials) {
		return withSecret(credentials, secretCipher.decrypt(credentials.clientSecret()));
	}

	private static Credentials masked(final Credentials credentials) {
		return withSecret(credentials, MASKED_SECRET);
	}

	private static Credentials withSecret(final Credentials credentials, final String clientSecret) {
		return new Credentials(credentials.id(), credentials.clientId(), clientSecret, credentials.authServerUrl(),
				credentials.requestedScope(), credentials.serviceDomain());
	}
}
