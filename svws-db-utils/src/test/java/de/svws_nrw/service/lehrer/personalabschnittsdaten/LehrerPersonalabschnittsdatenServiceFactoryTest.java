package de.svws_nrw.service.lehrer.personalabschnittsdaten;

import de.svws_nrw.mapper.lehrer.LehrerPersonalabschnittsdatenMapper;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.service.lehrer.LehrerServiceFactory;
import de.svws_nrw.service.lehrer.funktion.LehrerFunktionServiceFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class LehrerPersonalabschnittsdatenServiceFactoryTest {

	private MockedStatic<LehrerRepositoryFactory> lehrerRepoFactoryMock;
	private MockedStatic<KatalogRepositoryFactory> katalogRepositoryFactoryMockedStatic;
	private MockedStatic<EigeneSchuleRepositoryFactory> schuleRepoFactoryMock;
	private MockedStatic<LehrerServiceFactory> lehrerServiceFactoryMock;
	private MockedStatic<LehrerFunktionServiceFactory> funktionServiceFactoryMock;

	@BeforeEach
	void setUp() {
		lehrerRepoFactoryMock = mockStatic(LehrerRepositoryFactory.class);
		katalogRepositoryFactoryMockedStatic = mockStatic(KatalogRepositoryFactory.class);
		schuleRepoFactoryMock = mockStatic(EigeneSchuleRepositoryFactory.class);
		lehrerServiceFactoryMock = mockStatic(LehrerServiceFactory.class);
		funktionServiceFactoryMock = mockStatic(LehrerFunktionServiceFactory.class);
	}

	@AfterEach
	void tearDown() {
		lehrerRepoFactoryMock.close();
		katalogRepositoryFactoryMockedStatic.close();
		schuleRepoFactoryMock.close();
		lehrerServiceFactoryMock.close();
		funktionServiceFactoryMock.close();
	}

	@Test
	@DisplayName("getNewInstance (no-arg) | Erfolg")
	void getNewInstance_success() {
		final var mockedLehrerRepoFactory = mock(LehrerRepositoryFactory.class);
		final var mockedKatalogRepoFactory = mock(KatalogRepositoryFactory.class);
		final var mockedSchuleRepoFactory = mock(EigeneSchuleRepositoryFactory.class);
		final var mockedLehrerServiceFactory = mock(LehrerServiceFactory.class);
		final var mockedFunktionServiceFactory = mock(LehrerFunktionServiceFactory.class);

		lehrerRepoFactoryMock.when(LehrerRepositoryFactory::getNewInstance).thenReturn(mockedLehrerRepoFactory);
		katalogRepositoryFactoryMockedStatic.when(KatalogRepositoryFactory::getNewInstance).thenReturn(mockedKatalogRepoFactory);
		schuleRepoFactoryMock.when(EigeneSchuleRepositoryFactory::getNewInstance).thenReturn(mockedSchuleRepoFactory);
		lehrerServiceFactoryMock.when(() -> LehrerServiceFactory.getNewInstance(mockedLehrerRepoFactory, mockedSchuleRepoFactory))
				.thenReturn(mockedLehrerServiceFactory);
		funktionServiceFactoryMock.when(LehrerFunktionServiceFactory::getNewInstance).thenReturn(mockedFunktionServiceFactory);

		final var factory = LehrerPersonalabschnittsdatenServiceFactory.getNewInstance();

		assertThat(factory).isNotNull().isInstanceOf(LehrerPersonalabschnittsdatenServiceFactory.class);
		lehrerRepoFactoryMock.verify(LehrerRepositoryFactory::getNewInstance, times(1));
		katalogRepositoryFactoryMockedStatic.verify(KatalogRepositoryFactory::getNewInstance, times(1));
		schuleRepoFactoryMock.verify(EigeneSchuleRepositoryFactory::getNewInstance, times(1));
		lehrerServiceFactoryMock.verify(() -> LehrerServiceFactory.getNewInstance(mockedLehrerRepoFactory, mockedSchuleRepoFactory), times(1));
		funktionServiceFactoryMock.verify(LehrerFunktionServiceFactory::getNewInstance, times(1));
	}

	@Test
	@DisplayName("getNewInstance (no-arg) | Erstellt neue Instanz")
	void getNewInstance_createsNewInstance() {
		final var mockedLehrerRepoFactory = mock(LehrerRepositoryFactory.class);
		final var mockedKatalogRepoFactory = mock(KatalogRepositoryFactory.class);
		final var mockedSchuleRepoFactory = mock(EigeneSchuleRepositoryFactory.class);
		final var mockedLehrerServiceFactory = mock(LehrerServiceFactory.class);
		final var mockedFunktionServiceFactory = mock(LehrerFunktionServiceFactory.class);

		lehrerRepoFactoryMock.when(LehrerRepositoryFactory::getNewInstance).thenReturn(mockedLehrerRepoFactory);
		katalogRepositoryFactoryMockedStatic.when(KatalogRepositoryFactory::getNewInstance).thenReturn(mockedKatalogRepoFactory);
		schuleRepoFactoryMock.when(EigeneSchuleRepositoryFactory::getNewInstance).thenReturn(mockedSchuleRepoFactory);
		lehrerServiceFactoryMock.when(() -> LehrerServiceFactory.getNewInstance(mockedLehrerRepoFactory, mockedSchuleRepoFactory))
				.thenReturn(mockedLehrerServiceFactory);
		funktionServiceFactoryMock.when(LehrerFunktionServiceFactory::getNewInstance).thenReturn(mockedFunktionServiceFactory);

		final var factory1 = LehrerPersonalabschnittsdatenServiceFactory.getNewInstance();
		final var factory2 = LehrerPersonalabschnittsdatenServiceFactory.getNewInstance();

		assertThat(factory1).isNotSameAs(factory2);
	}

	@Test
	@DisplayName("getNewInstance (with args) | Erfolg")
	void getNewInstance_withArgs_success() {
		final var mockedLehrerRepoFactory = mock(LehrerRepositoryFactory.class);
		final var mockedKatalogRepoFactory = mock(KatalogRepositoryFactory.class);
		final var mockedSchuleRepoFactory = mock(EigeneSchuleRepositoryFactory.class);
		final var mockedLehrerServiceFactory = mock(LehrerServiceFactory.class);
		final var mockedFunktionServiceFactory = mock(LehrerFunktionServiceFactory.class);

		final var factory = LehrerPersonalabschnittsdatenServiceFactory.getNewInstance(
				mockedLehrerRepoFactory,
				mockedKatalogRepoFactory,
				mockedSchuleRepoFactory,
				mockedLehrerServiceFactory,
				mockedFunktionServiceFactory,
				LehrerPersonalabschnittsdatenMapper.INSTANCE
		);

		assertThat(factory).isNotNull().isInstanceOf(LehrerPersonalabschnittsdatenServiceFactory.class);
	}

	@Test
	@DisplayName("getLehrerPersonalabschnittsdatenService | Erfolg")
	void getLehrerPersonalabschnittsdatenService_success() {
		final var mockedLehrerRepoFactory = mock(LehrerRepositoryFactory.class);
		final var mockedKatalogRepoFactory = mock(KatalogRepositoryFactory.class);
		final var mockedSchuleRepoFactory = mock(EigeneSchuleRepositoryFactory.class);
		final var mockedLehrerServiceFactory = mock(LehrerServiceFactory.class);
		final var mockedFunktionServiceFactory = mock(LehrerFunktionServiceFactory.class);

		final var factory = LehrerPersonalabschnittsdatenServiceFactory.getNewInstance(
				mockedLehrerRepoFactory,
				mockedKatalogRepoFactory,
				mockedSchuleRepoFactory,
				mockedLehrerServiceFactory,
				mockedFunktionServiceFactory,
				LehrerPersonalabschnittsdatenMapper.INSTANCE
		);

		final var service = factory.getLehrerPersonalabschnittsdatenService();

		assertThat(service).isNotNull().isInstanceOf(LehrerPersonalabschnittsdatenService.class);
	}

	@Test
	@DisplayName("getLehrerPersonalabschnittsdatenService | Erstellt neue Instanz bei jedem Aufruf")
	void getLehrerPersonalabschnittsdatenService_createsNewInstance() {
		final var mockedLehrerRepoFactory = mock(LehrerRepositoryFactory.class);
		final var mockedKatalogRepoFactory = mock(KatalogRepositoryFactory.class);
		final var mockedSchuleRepoFactory = mock(EigeneSchuleRepositoryFactory.class);
		final var mockedLehrerServiceFactory = mock(LehrerServiceFactory.class);
		final var mockedFunktionServiceFactory = mock(LehrerFunktionServiceFactory.class);

		final var factory = LehrerPersonalabschnittsdatenServiceFactory.getNewInstance(
				mockedLehrerRepoFactory,
				mockedKatalogRepoFactory,
				mockedSchuleRepoFactory,
				mockedLehrerServiceFactory,
				mockedFunktionServiceFactory,
				LehrerPersonalabschnittsdatenMapper.INSTANCE
		);

		final var service1 = factory.getLehrerPersonalabschnittsdatenService();
		final var service2 = factory.getLehrerPersonalabschnittsdatenService();

		assertThat(service1).isNotSameAs(service2);
	}
}
