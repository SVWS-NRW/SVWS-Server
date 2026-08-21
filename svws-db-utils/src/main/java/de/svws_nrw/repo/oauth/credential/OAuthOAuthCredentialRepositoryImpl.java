package de.svws_nrw.repo.oauth.credential;

import java.util.List;
import java.util.Optional;

import de.svws_nrw.core.types.oauth2.OAuthServiceDomain;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.svws.auth.DTOSchuleOAuthSecrets;
import de.svws_nrw.repo.RepositoryImpl;

import static de.svws_nrw.db.dto.current.svws.auth.DTOSchuleOAuthSecrets.QUERY_BY_SERVICEDOMAIN;

public final class OAuthOAuthCredentialRepositoryImpl extends RepositoryImpl<DTOSchuleOAuthSecrets> implements OAuthCredentialRepository {

	/**
	 * Erstellt ein neues Repository-Objekt mit der übergebenen Datenbank-Verbindung
	 *
	 * @param conn die Datenbank-Verbindung
	 */
	public OAuthOAuthCredentialRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOSchuleOAuthSecrets.class, c -> c.id, (o, id) -> o.id = id);
	}

	@Override
	public Optional<DTOSchuleOAuthSecrets> findByServiceDomain(final OAuthServiceDomain domain) {
		return conn.queryList(QUERY_BY_SERVICEDOMAIN, DTOSchuleOAuthSecrets.class, domain)
				.stream()
				.findFirst();
	}

	@Override
	public List<DTOSchuleOAuthSecrets> findAllByServiceDomain(final OAuthServiceDomain domain) {
		return conn.queryList(QUERY_BY_SERVICEDOMAIN, DTOSchuleOAuthSecrets.class, domain);
	}
}
