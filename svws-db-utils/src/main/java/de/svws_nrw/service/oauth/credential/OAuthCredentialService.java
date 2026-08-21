package de.svws_nrw.service.oauth.credential;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.service.utils.BulkDeleteUtils;
import java.util.List;
import java.util.Optional;

import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.oauth.OAuthCredentialMapper;
import de.svws_nrw.mapper.oauth.OAuthDomainMapper;
import de.svws_nrw.oauth.internal.Credentials;
import de.svws_nrw.oauth.internal.OAuthDomain;
import de.svws_nrw.repo.oauth.credential.OAuthCredentialRepository;
import jakarta.ws.rs.core.Response;

/**
 * Service zur Verwaltung von OAuth-Zugangsdaten.
 */
public class OAuthCredentialService {

	private final OAuthCredentialRepository repository;
	private final OAuthCredentialMapper mapper;
	private final OAuthDomainMapper oAuthDomainMapper;

	private static final String CREDENTIAL_PRESENT_FOR_DOMAIN = "Es existiert bereits ein Datensatz zu dieser Domäne";

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param repository        Repository für Zugangsdaten
	 * @param mapper            Mapper für Client Credentials
	 * @param oAuthDomainMapper Mapper für Domänen
	 */
	public OAuthCredentialService(
			final OAuthCredentialRepository repository,
			final OAuthCredentialMapper mapper, final OAuthDomainMapper oAuthDomainMapper) {
		this.repository = repository;
		this.mapper = mapper;
		this.oAuthDomainMapper = oAuthDomainMapper;
	}

	/**
	 * Gibt alle OAuth-Zugangsdaten zurück.
	 *
	 * @return Liste aller Zugangsdaten
	 */
	public List<Credentials> getAll() {
		return repository.getAll()
				.stream()
				.map(mapper::fromDomain)
				.toList();
	}

	/**
	 * Gibt alle OAuth-Zugangsdaten der übergebenen Domäne zurück.
	 *
	 * @param domain die Domäne
	 * @return Liste der Zugangsdaten der Domäne
	 */
	public List<Credentials> getAll(final OAuthDomain domain) {
		return repository.findAllByServiceDomain(oAuthDomainMapper.toDomain(domain))
				.stream()
				.map(mapper::fromDomain)
				.toList();
	}

	/**
	 * Gibt die Zugangsdaten zur übergebenen ID zurück, falls vorhanden.
	 *
	 * @param id die ID der Zugangsdaten
	 * @return die Zugangsdaten, sofern vorhanden
	 */
	public Optional<Credentials> get(final long id) {
		return repository.findById(id)
				.map(mapper::fromDomain);
	}

	/**
	 * Erstellt neue OAuth-Zugangsdaten.
	 *
	 * @param credentials zu erstellende Zugangsdaten
	 * @return erstellte Zugangsdaten
	 */
	public Credentials create(final Credentials credentials) {
		return TransactionSupport.transactional(() -> {
			final var existing = repository.findByServiceDomain(oAuthDomainMapper.toDomain(credentials.serviceDomain()));
			if (existing.isPresent()) {
				throw new ApiOperationException(Response.Status.BAD_REQUEST, CREDENTIAL_PRESENT_FOR_DOMAIN);
			}

			final var entity = mapper.toDomain(credentials);

			repository.create(entity);

			return mapper.fromDomain(entity);
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
}
