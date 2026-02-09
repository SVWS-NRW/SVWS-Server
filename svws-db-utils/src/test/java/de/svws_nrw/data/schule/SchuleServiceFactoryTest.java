package de.svws_nrw.data.schule;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.repo.schule.SchuleRepositoryFactory;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;

/**
 * Tests für die Factory von Schul-Services
 */
@ExtendWith(MockitoExtension.class)
class SchuleServiceFactoryTest {

	@Mock
	private SchuleRepositoryFactory repoFactoryMock;

	@Mock
	private SchuljahresabschnitteRepository abschnitteRepoMock;

	@InjectMocks
	private SchuleServiceFactory serviceFactory;

	@Test
	@DisplayName("Test: getSchuljahresabschnittService liefert eine Instanz des Services")
	void testGetSchuljahresabschnittService() {
		when(repoFactoryMock.getSchuljahresabschnitteRepository()).thenReturn(abschnitteRepoMock);
		final SchuljahresabschnittService service = serviceFactory.getSchuljahresabschnittService();
		assertNotNull(service, "Der Service sollte nicht null sein.");
		verify(repoFactoryMock, times(1)).getSchuljahresabschnitteRepository();
	}

}
