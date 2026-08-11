package de.svws_nrw.service.katalog.teilleistungsart;

import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TeilleistungsartServiceFactoryTest {

	@Mock
	private KatalogRepositoryFactory teilleistungsartRepository;
	@InjectMocks
	private TeilleistungsartServiceFactory teilLeistungsartServiceFactory;

	@Test
	void testFactory() {
		final var service = teilLeistungsartServiceFactory.getTeilLeistungsartenService();

		verify(teilleistungsartRepository).getTeilleistungsartRepository();
		assertNotNull(service);
	}

}
