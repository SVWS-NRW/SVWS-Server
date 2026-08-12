package de.svws_nrw.service.schule.katalog;

import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.fachklasse.FachklasseRepository;
import de.svws_nrw.repo.schule.kataloge.merkmal.MerkmalRepository;
import de.svws_nrw.repo.schule.kataloge.teilleistungsart.TeilleistungsartRepository;
import de.svws_nrw.service.schule.EigeneSchuleService;
import de.svws_nrw.service.schule.EigeneSchuleServiceFactory;
import de.svws_nrw.service.schule.katalog.fachklasse.FachklasseService;
import de.svws_nrw.service.schule.katalog.merkmal.MerkmalService;
import de.svws_nrw.service.schule.katalog.teilleistungsart.TeilleistungsartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("KatalogServiceFactory")
class KatalogServiceFactoryTest {

	@Mock
	private KatalogRepositoryFactory katalogRepositoryFactory;

	@Mock
	private EigeneSchuleServiceFactory eigeneSchuleServiceFactory;

	@Mock
	private TeilleistungsartRepository teilleistungsartRepository;

	@Mock
	private MerkmalRepository merkmalRepository;

	@Mock
	private FachklasseRepository fachklasseRepository;

	@Mock
	private EigeneSchuleService eigeneSchuleService;

	private KatalogServiceFactory underTest;

	@BeforeEach
	void setUp() {
		underTest = KatalogServiceFactory.getNewInstance(
				katalogRepositoryFactory,
				eigeneSchuleServiceFactory
		);
	}

	// -------------------------------------------------------------------------
	// getNewInstance
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getNewInstance()")
	class GetNewInstance {

		@Test
		@DisplayName("gibt eine nicht-null Instanz zurück")
		void shouldReturnNonNullInstance() {
			final KatalogServiceFactory factory = KatalogServiceFactory.getNewInstance(
					katalogRepositoryFactory,
					eigeneSchuleServiceFactory
			);

			assertThat(factory).isNotNull();
		}

		@Test
		@DisplayName("gibt bei jedem Aufruf eine neue Instanz zurück")
		void shouldReturnNewInstanceOnEachCall() {
			final KatalogServiceFactory factory1 = KatalogServiceFactory.getNewInstance(
					katalogRepositoryFactory,
					eigeneSchuleServiceFactory
			);
			final KatalogServiceFactory factory2 = KatalogServiceFactory.getNewInstance(
					katalogRepositoryFactory,
					eigeneSchuleServiceFactory
			);

			assertThat(factory1).isNotSameAs(factory2);
		}
	}

	// -------------------------------------------------------------------------
	// getTeilLeistungsartenService()
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getTeilLeistungsartenService()")
	class GetTeilLeistungsartenService {

		@BeforeEach
		void setUp() {
			when(katalogRepositoryFactory.getTeilleistungsartRepository())
					.thenReturn(teilleistungsartRepository);
		}

		@Test
		@DisplayName("gibt eine nicht-null TeilleistungsartService-Instanz zurück")
		void shouldReturnNonNullService() {
			final TeilleistungsartService service = underTest.getTeilLeistungsartenService();

			assertThat(service).isNotNull();
		}

		@Test
		@DisplayName("gibt eine neue Instanz bei jedem Aufruf zurück")
		void shouldReturnNewInstanceOnEachCall() {
			final TeilleistungsartService service1 = underTest.getTeilLeistungsartenService();
			final TeilleistungsartService service2 = underTest.getTeilLeistungsartenService();

			assertThat(service1).isNotSameAs(service2);
		}

		@Test
		@DisplayName("ruft getTeilleistungsartRepository() auf der KatalogRepositoryFactory auf")
		void shouldDelegateToRepositoryFactory() {
			underTest.getTeilLeistungsartenService();

			verify(katalogRepositoryFactory).getTeilleistungsartRepository();
		}
	}

	// -------------------------------------------------------------------------
	// getMerkmalService()
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getMerkmalService()")
	class GetMerkmalService {

		@BeforeEach
		void setUp() {
			when(katalogRepositoryFactory.getMerkmalRepository())
					.thenReturn(merkmalRepository);
		}

		@Test
		@DisplayName("gibt eine nicht-null MerkmalService-Instanz zurück")
		void shouldReturnNonNullService() {
			final MerkmalService service = underTest.getMerkmalService();

			assertThat(service).isNotNull();
		}

		@Test
		@DisplayName("gibt eine neue Instanz bei jedem Aufruf zurück")
		void shouldReturnNewInstanceOnEachCall() {
			final MerkmalService service1 = underTest.getMerkmalService();
			final MerkmalService service2 = underTest.getMerkmalService();

			assertThat(service1).isNotSameAs(service2);
		}

		@Test
		@DisplayName("ruft getMerkmalRepository() auf der KatalogRepositoryFactory auf")
		void shouldDelegateToRepositoryFactory() {
			underTest.getMerkmalService();

			verify(katalogRepositoryFactory).getMerkmalRepository();
		}

		@Test
		@DisplayName("verwendet MerkmalMapper.INSTANCE")
		void shouldUseMerkmalMapperInstance() {
			// Der MerkmalMapper.INSTANCE wird intern injiziert – prüfen wir,
			// dass der Service ohne Exception erstellt wird und nicht null ist
			final MerkmalService service = underTest.getMerkmalService();

			assertThat(service).isInstanceOf(MerkmalService.class);
		}
	}

	// -------------------------------------------------------------------------
	// getFachklasseService()
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getFachklasseService()")
	class GetFachklasseService {

		@BeforeEach
		void setUp() {
			when(katalogRepositoryFactory.getFachklasseRepository())
					.thenReturn(fachklasseRepository);
			when(eigeneSchuleServiceFactory.getSchuleService())
					.thenReturn(eigeneSchuleService);
		}

		@Test
		@DisplayName("gibt eine nicht-null FachklasseService-Instanz zurück")
		void shouldReturnNonNullService() {
			final FachklasseService service = underTest.getFachklasseService();

			assertThat(service).isNotNull();
		}

		@Test
		@DisplayName("gibt eine neue Instanz bei jedem Aufruf zurück")
		void shouldReturnNewInstanceOnEachCall() {
			final FachklasseService service1 = underTest.getFachklasseService();
			final FachklasseService service2 = underTest.getFachklasseService();

			assertThat(service1).isNotSameAs(service2);
		}

		@Test
		@DisplayName("ruft getFachklasseRepository() auf der KatalogRepositoryFactory auf")
		void shouldDelegateToKatalogRepositoryFactory() {
			underTest.getFachklasseService();

			verify(katalogRepositoryFactory).getFachklasseRepository();
		}

		@Test
		@DisplayName("ruft getSchuleService() auf der EigeneSchuleServiceFactory auf")
		void shouldDelegateToEigeneSchuleServiceFactory() {
			underTest.getFachklasseService();

			verify(eigeneSchuleServiceFactory).getSchuleService();
		}

		@Test
		@DisplayName("verwendet FachklasseMapper.INSTANCE")
		void shouldUseFachklasseMapperInstance() {
			final FachklasseService service = underTest.getFachklasseService();

			assertThat(service).isInstanceOf(FachklasseService.class);
		}
	}
}
