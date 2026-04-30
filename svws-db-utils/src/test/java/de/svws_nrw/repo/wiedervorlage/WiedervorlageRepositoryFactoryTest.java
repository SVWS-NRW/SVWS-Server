package de.svws_nrw.repo.wiedervorlage;

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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WiedervorlageRepositoryFactoryTest {
	@Mock
	private DBEntityManager mockedConn;

	@Mock
	private HttpServletRequest mockedRequest;

	@BeforeEach
	void setup() {
		// RestEasy-Kontext vorbereiten, da RepositorySupport genutzt wird und dieser dafür benötigt wird
		ResteasyContext.pushContext(HttpServletRequest.class, mockedRequest);
		when(mockedRequest.getAttribute("connection")).thenReturn(mockedConn);
	}


	@AfterEach
	void tearDown() {
		ResteasyContext.clearContextData();
	}

	@Test
	@DisplayName("getWiedervorlageRepository | Repository wird erstellt und ist nicht null")
	void getWiedervorlageRepository() {
		final var factory = WiedervorlageRepositoryFactory.getNewInstance();
		final var repository = factory.getWiedervorlageRepository();

		assertNotNull(repository);

	}
}
