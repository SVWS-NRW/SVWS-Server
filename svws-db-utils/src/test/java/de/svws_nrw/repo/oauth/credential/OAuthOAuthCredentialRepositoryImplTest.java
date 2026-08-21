package de.svws_nrw.repo.oauth.credential;

import java.util.List;

import de.svws_nrw.core.types.oauth2.OAuthServiceDomain;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.svws.auth.DTOSchuleOAuthSecrets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static de.svws_nrw.db.dto.current.svws.auth.DTOSchuleOAuthSecrets.QUERY_BY_SERVICEDOMAIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OAuthOAuthCredentialRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private OAuthOAuthCredentialRepositoryImpl cut;


	@Test
	@DisplayName("findByServiceDomain | Delegiert Query an DBEntityManager und gibt erstes Ergebnis zurueck")
	void findByServiceDomainSuccess() {
		final var dto = buildEntity();
		when(conn.queryList(QUERY_BY_SERVICEDOMAIN, DTOSchuleOAuthSecrets.class, OAuthServiceDomain.IT_NRW))
				.thenReturn(List.of(dto));

		final var result = cut.findByServiceDomain(OAuthServiceDomain.IT_NRW);

		assertThat(result).contains(dto);
	}

	@Test
	@DisplayName("findByServiceDomain | gibt leeres Optional zurueck wenn kein Ergebnis existiert")
	void findByServiceDomainEmpty() {
		when(conn.queryList(QUERY_BY_SERVICEDOMAIN, DTOSchuleOAuthSecrets.class, OAuthServiceDomain.IT_NRW))
				.thenReturn(List.of());

		final var result = cut.findByServiceDomain(OAuthServiceDomain.IT_NRW);

		assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("findAllByServiceDomain | Delegiert Query an DBEntityManager und gibt alle Ergebnisse zurueck")
	void findAllByServiceDomainSuccess() {
		final var dto = buildEntity();
		when(conn.queryList(QUERY_BY_SERVICEDOMAIN, DTOSchuleOAuthSecrets.class, OAuthServiceDomain.IT_NRW))
				.thenReturn(List.of(dto));

		final var result = cut.findAllByServiceDomain(OAuthServiceDomain.IT_NRW);

		assertThat(result).containsExactly(dto);
	}

	private static DTOSchuleOAuthSecrets buildEntity() {
		final var dto = new DTOSchuleOAuthSecrets(3L, "https://auth.example.com/token", "client-id", "client-secret");
		dto.serviceDomain = OAuthServiceDomain.IT_NRW;
		return dto;
	}
}
