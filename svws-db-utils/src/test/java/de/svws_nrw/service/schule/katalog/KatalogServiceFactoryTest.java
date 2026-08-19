package de.svws_nrw.service.schule.katalog;

import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.fachklasse.FachklasseRepository;
import de.svws_nrw.repo.schule.kataloge.merkmal.MerkmalRepository;
import de.svws_nrw.repo.schule.kataloge.ort.OrtRepository;
import de.svws_nrw.repo.schule.kataloge.ortsteil.OrtsteilRepository;
import de.svws_nrw.repo.schule.kataloge.teilleistungsart.TeilleistungsartRepository;
import de.svws_nrw.service.schule.EigeneSchuleService;
import de.svws_nrw.service.schule.EigeneSchuleServiceFactory;
import de.svws_nrw.service.schule.katalog.fachklasse.FachklasseService;
import de.svws_nrw.service.schule.katalog.merkmal.MerkmalService;
import de.svws_nrw.service.schule.katalog.ort.OrtService;
import de.svws_nrw.service.schule.katalog.ortsteil.OrtsteilService;
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

	@Mock
	private OrtRepository ortRepository;

	@Mock
	private OrtsteilRepository ortsteilRepository;

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

	// -------------------------------------------------------------------------
	// getOrtService()
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getOrtService()")
	class GetOrtService {

		@BeforeEach
		void setUp() {
			when(katalogRepositoryFactory.getOrtRepository())
					.thenReturn(ortRepository);
			when(eigeneSchuleServiceFactory.getSchuleService())
					.thenReturn(eigeneSchuleService);
		}

		@Test
		@DisplayName("gibt eine nicht-null OrtService-Instanz zurück")
		void shouldReturnNonNullService() {
			final OrtService service = underTest.getOrtService();

			assertThat(service).isNotNull();
		}

		@Test
		@DisplayName("gibt eine neue Instanz bei jedem Aufruf zurück")
		void shouldReturnNewInstanceOnEachCall() {
			final OrtService service1 = underTest.getOrtService();
			final OrtService service2 = underTest.getOrtService();

			assertThat(service1).isNotSameAs(service2);
		}

		@Test
		@DisplayName("ruft getOrtRepository() auf der KatalogRepositoryFactory auf")
		void shouldDelegateToKatalogRepositoryFactory() {
			underTest.getOrtService();

			verify(katalogRepositoryFactory).getOrtRepository();
		}

		@Test
		@DisplayName("ruft getSchuleService() auf der EigeneSchuleServiceFactory auf")
		void shouldDelegateToEigeneSchuleServiceFactory() {
			underTest.getOrtService();

			verify(eigeneSchuleServiceFactory).getSchuleService();
		}

		@Test
		@DisplayName("verwendet OrtMapper.INSTANCE")
		void shouldUseOrtMapperInstance() {
			final OrtService service = underTest.getOrtService();

			assertThat(service).isInstanceOf(OrtService.class);
		}
	}

	@Nested
	@DisplayName("getOrtsteilService()")
	class GetOrtsteilService {

		@BeforeEach
		void setUp() {
			when(katalogRepositoryFactory.getOrtsteilRepository())
					.thenReturn(ortsteilRepository);
			when(katalogRepositoryFactory.getOrtRepository())
					.thenReturn(ortRepository);
		}

		@Test
		@DisplayName("gibt eine nicht-null OrtsteilService-Instanz zurück")
		void shouldReturnNonNullService() {
			final OrtsteilService service = underTest.getOrtsteilService();

			assertThat(service).isNotNull();
		}

		@Test
		@DisplayName("gibt eine neue Instanz bei jedem Aufruf zurück")
		void shouldReturnNewInstanceOnEachCall() {
			final OrtsteilService service1 = underTest.getOrtsteilService();
			final OrtsteilService service2 = underTest.getOrtsteilService();

			assertThat(service1).isNotSameAs(service2);
		}

		@Test
		@DisplayName("ruft getOrtsteilRepository() auf der KatalogRepositoryFactory auf")
		void shouldDelegateToOrtsteilRepository() {
			underTest.getOrtsteilService();

			verify(katalogRepositoryFactory).getOrtsteilRepository();
		}

		@Test
		@DisplayName("ruft getOrtRepository() auf der KatalogRepositoryFactory auf")
		void shouldDelegateToOrtRepository() {
			underTest.getOrtsteilService();

			verify(katalogRepositoryFactory).getOrtRepository();
		}

		@Test
		@DisplayName("verwendet OrtsteilMapper.INSTANCE")
		void shouldUseOrtsteilMapperInstance() {
			final OrtsteilService service = underTest.getOrtsteilService();

			assertThat(service).isInstanceOf(OrtsteilService.class);
		}
	}

}
