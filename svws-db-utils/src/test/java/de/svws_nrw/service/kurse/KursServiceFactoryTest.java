package de.svws_nrw.service.kurse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.repo.kurse.KurseRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;

/**
 * Testklasse für die {@link KursServiceFactory}
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests für die KursServiceFactory")
class KursServiceFactoryTest {

	@Mock
	private KurseRepositoryFactory kurseRepositoryFactory;

	@Mock
	private SchuelerRepositoryFactory schuelerRepositoryFactory;

	@Mock
	private KatalogRepositoryFactory katalogRepositoryFactory;

	@Test
	@DisplayName("getKursService: Der Service wird mit den Repositories der übergebenen Factories erstellt")
	void getKursService_nutztRepositoriesDerFactories() {
		final var factory = KursServiceFactory.getNewInstance(kurseRepositoryFactory, schuelerRepositoryFactory, katalogRepositoryFactory);

		assertThat(factory.getKursService()).isNotNull();

		verify(kurseRepositoryFactory).getKurseRepository();
		verify(kurseRepositoryFactory).getKursSchuelerRepository();
		verify(kurseRepositoryFactory).getKurslehrerRepository();
		verify(schuelerRepositoryFactory).getSchuelerRepository();
		verify(katalogRepositoryFactory).getJahrgangRepository();
	}

}
