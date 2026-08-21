package de.svws_nrw.repo.oauth.credential;

import de.svws_nrw.db.DBEntityManager;
import jakarta.servlet.http.HttpServletRequest;
import org.jboss.resteasy.core.ResteasyContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OAuthCredentialRepositoryFactoryTest {

	@Mock
	private DBEntityManager mockedConn;

	@Mock
	private HttpServletRequest mockedRequest;

	@BeforeEach
	void setup() {
		// RestEasy-Kontext vorbereiten, da RepositoryFactory darauf zugreift, um die DB-Verbindung zu ermitteln
		ResteasyContext.pushContext(HttpServletRequest.class, mockedRequest);
		when(mockedRequest.getAttribute("connection")).thenReturn(mockedConn);
	}

	@AfterEach
	void tearDown() {
		ResteasyContext.clearContextData();
	}

	@Test
	@DisplayName("getRepository | Repository wird erstellt und ist nicht null")
	void getRepository() {
		final var factory = OAuthCredentialRepositoryFactory.getNewInstance();

		final var repository = factory.getRepository();

		assertNotNull(repository);
	}

	@Test
	@DisplayName("getRepository | Wiederholter Aufruf liefert dieselbe Instanz (Request-Scope Cache)")
	void getRepositoryReturnsCachedInstance() {
		final var factory = OAuthCredentialRepositoryFactory.getNewInstance();

		final var first = factory.getRepository();
		final var second = factory.getRepository();

		assertSame(first, second);
	}
}
