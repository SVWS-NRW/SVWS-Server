package de.svws_nrw.service.schueler.lernabschnitt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittsdaten;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerPSFachBemerkungen;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schueler.leistungsdaten.SchuelerLeistungsdatenMapper;
import de.svws_nrw.mapper.schueler.lernabschnittsdaten.SchuelerLernabschnittBemerkungenMapper;
import de.svws_nrw.mapper.schueler.lernabschnittsdaten.SchuelerLernabschnittsdatenMapper;
import de.svws_nrw.mapper.schueler.lernabschnittsdaten.SchuelerLernabschnittsdatenMappingContext;
import de.svws_nrw.repo.klassen.KlassenRepository;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import de.svws_nrw.repo.schueler.leistungsdaten.SchuelerLeistungsdatenRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittBemerkungRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import de.svws_nrw.repo.schule.kataloge.fachklasse.FachklasseRepository;
import de.svws_nrw.repo.schule.kataloge.foerderschwerpunkt.FoerderschwerpunktRepository;
import de.svws_nrw.repo.schule.kataloge.jahrgang.JahrgangRepository;
import de.svws_nrw.repo.schule.kataloge.schuelerschwerpunkt.SchuelerschwerpunktRepository;
import de.svws_nrw.service.benutzer.BenutzerKompetenzService;
import de.svws_nrw.service.schule.EigeneSchuleService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchuelerLernabschnittServiceTest {

	@Mock
	private SchuelerLernabschnittRepository repository;

	@Mock
	private SchuelerLernabschnittBemerkungRepository bemerkungRepository;

	@Mock
	private SchuelerLernabschnittsdatenMapper mapper;

	@Mock
	private SchuljahresabschnitteRepository schuljahresabschnitteRepo;

	@Mock
	private SchuelerLeistungsdatenRepository leistungsdatenRepository;

	@Mock
	private LehrerRepository lehrerRepository;

	@Mock
	private KlassenRepository klassenRepository;

	@Mock
	private FoerderschwerpunktRepository foerderschwerpunktRepository;

	@Mock
	private FachklasseRepository fachklasseRepository;

	@Mock
	private JahrgangRepository jahrgangRepository;

	@Mock
	private SchuelerschwerpunktRepository schuelerschwerpunktRepository;

	@Mock
	private BenutzerKompetenzService benutzerKompetenzService;

	@Mock
	private EigeneSchuleService eigeneSchuleService;

	private SchuelerLernabschnittsdatenRepos repos;
	private SchuelerLernabschnittService service;

	private static final int SCHULJAHR = 2024;
	private static final Schulform SCHULFORM = Schulform.GY;

	private static final long ID_LERNABSCHNITT = 1L;
	private static final long ID_SCHUELER = 10L;
	private static final long ID_SCHULJAHRESABSCHNITT = 20L;

	private DTOSchuelerLernabschnittsdaten entity;
	private SchuelerLernabschnittsdaten apiModel;

	@BeforeAll
	static void init() {
		ASDCoreTypeUtils.initAll();
	}

	@BeforeEach
	void setUp() {
		entity = createEntity(ID_LERNABSCHNITT, ID_SCHULJAHRESABSCHNITT);
		apiModel = createApiModel(ID_LERNABSCHNITT);

		repos = new SchuelerLernabschnittsdatenRepos(repository, bemerkungRepository,
				schuljahresabschnitteRepo, leistungsdatenRepository,
				lehrerRepository, klassenRepository, foerderschwerpunktRepository, fachklasseRepository, jahrgangRepository,
				schuelerschwerpunktRepository);
		service = new SchuelerLernabschnittService(repos, mapper, SchuelerLeistungsdatenMapper.INSTANCE,
				SchuelerLernabschnittBemerkungenMapper.INSTANCE, eigeneSchuleService, benutzerKompetenzService);

		// Default-Stubs für den ContextLoader — keine Bemerkungen, keine Leistungen
		lenient().when(eigeneSchuleService.getSchulform()).thenReturn(SCHULFORM);
		lenient().when(repos.bemerkungRepository().findMapByLernabschnittID(anyCollection())).thenReturn(Map.of());
		// wie das echte Repository: für jede übergebene ID ein Eintrag mit leerer Liste
		lenient().when(repos.leistungsdatenRepository().getMapListByLernabschnittsIds(anyCollection())).thenAnswer(invocation -> {
			final Collection<Long> ids = invocation.getArgument(0);
			final Map<Long, List<DTOSchuelerLeistungsdaten>> result = new HashMap<>();
			ids.forEach(id -> result.put(id, new ArrayList<>()));
			return result;
		});
	}

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private DTOSchuelerLernabschnittsdaten createEntity(final long id, final long schuljahresabschnittId) {
		return new DTOSchuelerLernabschnittsdaten(id, ID_SCHUELER, schuljahresabschnittId, true, null);
	}

	private DTOSchuljahresabschnitte createSchuljahresabschnitt(final long id) {
		return new DTOSchuljahresabschnitte(id, SCHULJAHR, 1);
	}

	private DTOJahrgang createJahrgang(final long id, final String asdJahrgang) {
		final var jahrgang = new DTOJahrgang(id);
		jahrgang.ASDJahrgang = asdJahrgang;
		return jahrgang;
	}

	private DTOKlassen createKlasse(final long id, final long schuljahresabschnittId, final Long jahrgangId) {
		final var klasse = new DTOKlassen(id, schuljahresabschnittId, "5a");
		klasse.Jahrgang_ID = jahrgangId;
		return klasse;
	}

	private SchuelerLernabschnittsdaten createApiModel(final long id) {
		final var daten = new SchuelerLernabschnittsdaten();
		daten.id = id;
		return daten;
	}

	private void stubToApi(final SchuelerLernabschnittsdaten result) {
		when(repos.schuljahresabschnitteRepo().findListByIds(anyCollection()))
				.thenReturn(List.of(createSchuljahresabschnitt(ID_SCHULJAHRESABSCHNITT)));
		when(mapper.toApi(any(DTOSchuelerLernabschnittsdaten.class), anyInt(), any(Schulform.class),
				any(SchuelerLernabschnittsdatenMappingContext.class)))
				.thenReturn(result);
	}

	private SchuelerLernabschnittsdatenMappingContext captureContext() {
		final var captor = ArgumentCaptor.forClass(SchuelerLernabschnittsdatenMappingContext.class);
		verify(mapper).toApi(any(DTOSchuelerLernabschnittsdaten.class), anyInt(), any(Schulform.class), captor.capture());
		return captor.getValue();
	}

	// -------------------------------------------------------------------------
	// get
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("get")
	class Get {

		@Test
		@DisplayName("Schülerlernabschnitt mit der ID nicht gefunden")
		void get_notFound() {
			when(repos.repository().findById(99L)).thenReturn(Optional.empty());

			assertThatThrownBy(() -> service.get(99L)).isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND)
					.hasMessage("Keine Lernabschnittsdaten zur Abschnitt-ID 99 gefunden.");
		}

		@Test
		@DisplayName("Schülerlernabschnitt mit der ID gefunden")
		void get_Found() {
			when(repos.repository().findById(ID_LERNABSCHNITT)).thenReturn(Optional.of(entity));
			stubToApi(apiModel);

			final var result = service.get(ID_LERNABSCHNITT);

			assertThat(result).isSameAs(apiModel);

		}

		@Test
		@DisplayName("Schuljahresabschnitt zum Schülerlernabschnitt mit der ID nicht gefunden")
		void get_schuljahresabschnittNotFound() {
			when(repos.repository().findById(ID_LERNABSCHNITT)).thenReturn(Optional.of(entity));
			when(schuljahresabschnitteRepo.findListByIds(List.of(ID_SCHULJAHRESABSCHNITT))).thenReturn(List.of());

			assertThatThrownBy(() -> service.get(ID_LERNABSCHNITT))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessage("Keine Schuljahresabschnitte gefunden für IDs: " + "[" + ID_SCHULJAHRESABSCHNITT + "]");
		}

		@Test
		@DisplayName("Bemerkungen zum Schülerlernabschnitt werden in den Kontext übernommen")
		void get_bemerkungen() {
			when(repos.repository().findById(ID_LERNABSCHNITT)).thenReturn(Optional.of(entity));

			final var bemerkung = new DTOSchuelerPSFachBemerkungen(5L, ID_LERNABSCHNITT);
			bemerkung.ASV = "asv";
			bemerkung.LELS = "lels";
			bemerkung.BemerkungVersetzung = "versetzt";
			when(repos.bemerkungRepository().findMapByLernabschnittID(List.of(ID_LERNABSCHNITT)))
					.thenReturn(Map.of(ID_LERNABSCHNITT, bemerkung));

			stubToApi(apiModel);

			service.get(ID_LERNABSCHNITT);

			final var ctx = captureContext();
			assertThat(ctx.bemerkung().zeugnisASV).isEqualTo("asv");
			assertThat(ctx.bemerkung().zeugnisLELS).isEqualTo("lels");
			assertThat(ctx.bemerkung().versetzungsentscheidung).isEqualTo("versetzt");
		}

		@Test
		@DisplayName("Leistungsdaten zum Schülerlernabschnitt werden gemappt in den Kontext übernommen")
		void get_leistungen() {
			when(repos.repository().findById(ID_LERNABSCHNITT)).thenReturn(Optional.of(entity));

			final var leistung = new DTOSchuelerLeistungsdaten(100L, ID_LERNABSCHNITT, 7L);
			when(repos.leistungsdatenRepository().getMapListByLernabschnittsIds(List.of(ID_LERNABSCHNITT)))
					.thenReturn(Map.of(ID_LERNABSCHNITT, List.of(leistung)));

			stubToApi(apiModel);

			service.get(ID_LERNABSCHNITT);

			final var ctx = captureContext();
			assertThat(ctx.leistungsdaten()).singleElement()
					.hasFieldOrPropertyWithValue("id", 100L)
					.hasFieldOrPropertyWithValue("lernabschnittID", ID_LERNABSCHNITT)
					.hasFieldOrPropertyWithValue("fachID", 7L);
		}
	}

	// -------------------------------------------------------------------------
	// getListBySchuelerAndSchuljahresabschnitt
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getListBySchuelerAndSchuljahresabschnitt")
	class GetListBySchuelerAndSchuljahresabschnitt {

		@Test
		@DisplayName("Keine Lernabschnitte zum Schüler im Schuljahresabschnitt - leere Liste")
		void getList_leer() {
			when(repos.repository().findListBySchuelerAndSchuljahresabschnitt(ID_SCHUELER, ID_SCHULJAHRESABSCHNITT))
					.thenReturn(List.of());

			final var result = service.getListBySchuelerAndSchuljahresabschnitt(ID_SCHUELER, ID_SCHULJAHRESABSCHNITT);

			assertThat(result).isEmpty();
			verifyNoInteractions(mapper, schuljahresabschnitteRepo, eigeneSchuleService,
					bemerkungRepository, leistungsdatenRepository);
		}

		@Test
		@DisplayName("Liefert mehrere Lernabschnitte in der richtigen Reihenfolge zurück")
		void getList_reihenfolge() {
			final var abschnitt5 = createEntity(5L, ID_SCHULJAHRESABSCHNITT);
			final var abschnitt7 = createEntity(7L, ID_SCHULJAHRESABSCHNITT);
			when(repos.repository().findListBySchuelerAndSchuljahresabschnitt(ID_SCHUELER, ID_SCHULJAHRESABSCHNITT))
					.thenReturn(List.of(abschnitt5, abschnitt7));
			when(repos.schuljahresabschnitteRepo().findListByIds(anyCollection()))
					.thenReturn(List.of(createSchuljahresabschnitt(ID_SCHULJAHRESABSCHNITT)));
			when(mapper.toApi(any(DTOSchuelerLernabschnittsdaten.class), anyInt(), any(Schulform.class),
					any(SchuelerLernabschnittsdatenMappingContext.class)))
					.thenAnswer(invocation -> {
						final DTOSchuelerLernabschnittsdaten e = invocation.getArgument(0);
						return createApiModel(e.ID);
					});

			final var result = service.getListBySchuelerAndSchuljahresabschnitt(ID_SCHUELER, ID_SCHULJAHRESABSCHNITT);

			assertThat(result).extracting(daten -> daten.id).containsExactly(5L, 7L);
		}
	}

	// -------------------------------------------------------------------------
	// getListBySchuelerIDsOhneLeistungsdaten
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getListBySchuelerIDsOhneLeistungsdaten")
	class GetListBySchuelerIDsOhneLeistungsdaten {

		@Test
		@DisplayName("Keine Lernabschnitte zu den Schülern - leere Liste")
		void getListOhneLeistungsdaten_leer() {
			when(repository.findListBySchuelerIDs(List.of(ID_SCHUELER))).thenReturn(List.of());

			final var result = service.getListBySchuelerIDsOhneLeistungsdaten(List.of(ID_SCHUELER));

			assertThat(result).isEmpty();
			verifyNoInteractions(mapper, schuljahresabschnitteRepo, bemerkungRepository, leistungsdatenRepository);
		}

		@Test
		@DisplayName("Lädt die Bemerkungen, aber keine Leistungsdaten - Reihenfolge bleibt erhalten")
		void getListOhneLeistungsdaten_mitBemerkungenOhneLeistungsdaten() {
			final var abschnitt5 = createEntity(5L, ID_SCHULJAHRESABSCHNITT);
			final var abschnitt7 = createEntity(7L, ID_SCHULJAHRESABSCHNITT);
			final var fachBem = new DTOSchuelerPSFachBemerkungen(50L, 5L);
			fachBem.ASV = "Arbeitet sorgfältig.";
			when(repository.findListBySchuelerIDs(List.of(ID_SCHUELER))).thenReturn(List.of(abschnitt5, abschnitt7));
			when(schuljahresabschnitteRepo.findListByIds(anyCollection()))
					.thenReturn(List.of(createSchuljahresabschnitt(ID_SCHULJAHRESABSCHNITT)));
			when(bemerkungRepository.findMapByLernabschnittID(List.of(5L, 7L))).thenReturn(Map.of(5L, fachBem));
			final Map<Long, SchuelerLernabschnittsdatenMappingContext> ctxById = new HashMap<>();
			when(mapper.toApi(any(DTOSchuelerLernabschnittsdaten.class), anyInt(), any(Schulform.class),
					any(SchuelerLernabschnittsdatenMappingContext.class)))
					.thenAnswer(invocation -> {
						final DTOSchuelerLernabschnittsdaten e = invocation.getArgument(0);
						ctxById.put(e.ID, invocation.getArgument(3));
						return createApiModel(e.ID);
					});

			final var result = service.getListBySchuelerIDsOhneLeistungsdaten(List.of(ID_SCHUELER));

			assertThat(result).extracting(daten -> daten.id).containsExactly(5L, 7L);
			assertThat(ctxById.get(5L).bemerkung().zeugnisASV).isEqualTo("Arbeitet sorgfältig.");
			assertThat(ctxById.get(7L).bemerkung().zeugnisASV).isEmpty();
			assertThat(ctxById.get(5L).leistungsdaten()).isEmpty();
			assertThat(ctxById.get(7L).leistungsdaten()).isEmpty();
			verifyNoInteractions(leistungsdatenRepository);
		}
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("patch")
	class Patch {

		private MockedStatic<TransactionSupport> transactionSupport;

		@BeforeEach
		void setUp() {
			// führt den Task direkt aus, ohne echte Datenbank-Transaktion
			transactionSupport = mockStatic(TransactionSupport.class);
			transactionSupport.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
					.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());
			lenient().when(repository.findById(ID_LERNABSCHNITT)).thenReturn(Optional.of(entity));
			lenient().when(schuljahresabschnitteRepo.findListByIds(anyCollection()))
					.thenReturn(List.of(createSchuljahresabschnitt(ID_SCHULJAHRESABSCHNITT)));
		}

		@AfterEach
		void tearDown() {
			transactionSupport.close();
		}

		/**
		 * Stellt das Verhalten des echten Mappers für die Jahrgang-ID nach, da der Mapper in diesem Test ein Mock ist.
		 */
		private void stubMapperPatchJahrgang() {
			doAnswer(invocation -> {
				final SchuelerLernabschnittsdatenPatchRequest dto = invocation.getArgument(0);
				final DTOSchuelerLernabschnittsdaten toPatch = invocation.getArgument(1);
				toPatch.Jahrgang_ID = dto.jahrgangID.get();
				return null;
			}).when(mapper).patch(any(), any(), anyInt(), any());
		}

		/**
		 * Stellt das Verhalten des echten Mappers für die Klassen-ID nach, da der Mapper in diesem Test ein Mock ist.
		 */
		private void stubMapperPatchKlasse() {
			doAnswer(invocation -> {
				final SchuelerLernabschnittsdatenPatchRequest dto = invocation.getArgument(0);
				final DTOSchuelerLernabschnittsdaten toPatch = invocation.getArgument(1);
				toPatch.Klassen_ID = dto.klassenID.get();
				return null;
			}).when(mapper).patch(any(), any(), anyInt(), any());
		}

		@Test
		@DisplayName("Jahrgang mit der ID existiert nicht - BAD_REQUEST")
		void patch_jahrgangNichtVorhanden() {
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.jahrgangID = JsonNullable.of(99L);
			when(jahrgangRepository.existsById(99L)).thenReturn(false);

			assertThatThrownBy(() -> service.patch(ID_LERNABSCHNITT, dto))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessage("Es existiert kein Jahrgang mit der ID 99.");
		}

		@Test
		@DisplayName("Jahrgang wird gesetzt - ASDJahrgang wird aus dem Jahrgang übernommen")
		void patch_jahrgangSetztAsdJahrgang() {
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.jahrgangID = JsonNullable.of(18L);
			entity.Jahrgang_ID = 17L;
			entity.ASDJahrgang = "05";
			when(jahrgangRepository.existsById(18L)).thenReturn(true);
			when(jahrgangRepository.findById(18L)).thenReturn(Optional.of(createJahrgang(18L, "06")));
			stubMapperPatchJahrgang();

			service.patch(ID_LERNABSCHNITT, dto);

			assertThat(entity.Jahrgang_ID).isEqualTo(18L);
			assertThat(entity.ASDJahrgang).isEqualTo("06");
		}

		@Test
		@DisplayName("Jahrgang wird entfernt - ASDJahrgang wird ebenfalls entfernt")
		void patch_jahrgangNullEntferntAsdJahrgang() {
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.jahrgangID = JsonNullable.of(null);
			entity.Jahrgang_ID = 17L;
			entity.ASDJahrgang = "05";
			stubMapperPatchJahrgang();

			service.patch(ID_LERNABSCHNITT, dto);

			assertThat(entity.Jahrgang_ID).isNull();
			assertThat(entity.ASDJahrgang).isNull();
			verifyNoInteractions(jahrgangRepository);
		}

		@Test
		@DisplayName("Klasse gehört zu einem anderen Schuljahresabschnitt - BAD_REQUEST")
		void patch_klasseAndererSchuljahresabschnitt() {
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.klassenID = JsonNullable.of(46L);
			when(klassenRepository.existsById(46L)).thenReturn(true);
			when(klassenRepository.findById(46L)).thenReturn(Optional.of(createKlasse(46L, 21L, null)));

			assertThatThrownBy(() -> service.patch(ID_LERNABSCHNITT, dto))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessage("Die Klasse mit der ID 46 gehört nicht zum Schuljahresabschnitt des Lernabschnitts.");
		}

		@Test
		@DisplayName("Jahrgang passt nicht zum Jahrgang der Klasse - BAD_REQUEST")
		void patch_jahrgangNichtKompatibelZurKlasse() {
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.jahrgangID = JsonNullable.of(18L);
			entity.Klassen_ID = 46L;
			when(jahrgangRepository.existsById(18L)).thenReturn(true);
			when(jahrgangRepository.findById(18L)).thenReturn(Optional.of(createJahrgang(18L, "06")));
			when(klassenRepository.findById(46L)).thenReturn(Optional.of(createKlasse(46L, ID_SCHULJAHRESABSCHNITT, 17L)));
			when(jahrgangRepository.findById(17L)).thenReturn(Optional.of(createJahrgang(17L, "05")));
			stubMapperPatchJahrgang();

			assertThatThrownBy(() -> service.patch(ID_LERNABSCHNITT, dto))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessage("Der Jahrgang mit der ID 18 ist für die Klasse mit der ID 46 nicht zulässig.");
		}

		@Test
		@DisplayName("Klasse ohne Jahrgang ist jahrgangsübergreifend - jeder Jahrgang ist zulässig")
		void patch_klasseOhneJahrgangIstJahrgangsuebergreifend() {
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.jahrgangID = JsonNullable.of(18L);
			entity.Klassen_ID = 46L;
			when(jahrgangRepository.existsById(18L)).thenReturn(true);
			when(jahrgangRepository.findById(18L)).thenReturn(Optional.of(createJahrgang(18L, "06")));
			when(klassenRepository.findById(46L)).thenReturn(Optional.of(createKlasse(46L, ID_SCHULJAHRESABSCHNITT, null)));
			stubMapperPatchJahrgang();

			service.patch(ID_LERNABSCHNITT, dto);

			assertThat(entity.Jahrgang_ID).isEqualTo(18L);
			assertThat(entity.ASDJahrgang).isEqualTo("06");
		}

		@Test
		@DisplayName("Klassenwechsel bei veraltetem ASDJahrgang am Lernabschnitt - Kompatibilität wird über den Jahrgangskatalog geprüft")
		void patch_klassenwechselMitVeraltetemAsdJahrgang() {
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.klassenID = JsonNullable.of(47L);
			entity.Klassen_ID = 46L;
			entity.Jahrgang_ID = 17L;
			entity.ASDJahrgang = "07";
			// anderer Jahrgang der Klasse, aber im Katalog mit demselben ASD-Jahrgang wie der Jahrgang des Lernabschnitts
			when(klassenRepository.existsById(47L)).thenReturn(true);
			when(klassenRepository.findById(47L)).thenReturn(Optional.of(createKlasse(47L, ID_SCHULJAHRESABSCHNITT, 16L)));
			when(jahrgangRepository.findById(16L)).thenReturn(Optional.of(createJahrgang(16L, "08")));
			when(jahrgangRepository.findById(17L)).thenReturn(Optional.of(createJahrgang(17L, "08")));
			stubMapperPatchKlasse();

			service.patch(ID_LERNABSCHNITT, dto);

			assertThat(entity.Klassen_ID).isEqualTo(47L);
			assertThat(entity.ASDJahrgang).isEqualTo("07");
		}

		@Test
		@DisplayName("Klassenwechsel bei gleichem Jahrgang - kompatibel ohne Abfrage des Jahrgangskatalogs")
		void patch_klassenwechselGleicherJahrgangOhneKatalog() {
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.klassenID = JsonNullable.of(47L);
			entity.Klassen_ID = 46L;
			entity.Jahrgang_ID = 17L;
			when(klassenRepository.existsById(47L)).thenReturn(true);
			when(klassenRepository.findById(47L)).thenReturn(Optional.of(createKlasse(47L, ID_SCHULJAHRESABSCHNITT, 17L)));
			stubMapperPatchKlasse();

			service.patch(ID_LERNABSCHNITT, dto);

			assertThat(entity.Klassen_ID).isEqualTo(47L);
			verifyNoInteractions(jahrgangRepository);
		}

		@Test
		@DisplayName("Schwerpunkt mit der ID existiert nicht - BAD_REQUEST")
		void patch_schwerpunktNichtVorhanden() {
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.schwerpunktID = JsonNullable.of(99L);
			when(schuelerschwerpunktRepository.existsById(99L)).thenReturn(false);

			assertThatThrownBy(() -> service.patch(ID_LERNABSCHNITT, dto))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessage("Es existiert kein Schülerschwerpunkt mit der ID 99.");
			verifyNoInteractions(mapper);
		}

		@Test
		@DisplayName("Schwerpunkt mit der ID existiert - Patch wird an den Mapper weitergegeben")
		void patch_schwerpunktVorhanden() {
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.schwerpunktID = JsonNullable.of(2L);
			when(schuelerschwerpunktRepository.existsById(2L)).thenReturn(true);

			service.patch(ID_LERNABSCHNITT, dto);

			verify(mapper).patch(any(), any(), anyInt(), any());
		}

		@Test
		@DisplayName("Schwerpunkt wird entfernt - keine Prüfung gegen den Katalog")
		void patch_schwerpunktNull() {
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.schwerpunktID = JsonNullable.of(null);

			service.patch(ID_LERNABSCHNITT, dto);

			verifyNoInteractions(schuelerschwerpunktRepository);
			verify(mapper).patch(any(), any(), anyInt(), any());
		}

		@Test
		@DisplayName("Tutor mit der ID existiert nicht - BAD_REQUEST")
		void patch_tutorNichtVorhanden() {
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.tutorID = JsonNullable.of(99L);
			when(lehrerRepository.existsById(99L)).thenReturn(false);

			assertThatThrownBy(() -> service.patch(ID_LERNABSCHNITT, dto))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessage("Es existiert kein Lehrer mit der ID 99.");
			verifyNoInteractions(mapper);
		}

		@Test
		@DisplayName("Sonderpädagoge mit der ID existiert nicht - BAD_REQUEST")
		void patch_sonderpaedagogeNichtVorhanden() {
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.sonderpaedagogeID = JsonNullable.of(99L);
			when(lehrerRepository.existsById(99L)).thenReturn(false);

			assertThatThrownBy(() -> service.patch(ID_LERNABSCHNITT, dto))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessage("Es existiert kein Lehrer mit der ID 99.");
			verifyNoInteractions(mapper);
		}

		@Test
		@DisplayName("Folgeklasse mit der ID existiert nicht - BAD_REQUEST")
		void patch_folgeklasseNichtVorhanden() {
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.folgeklassenID = JsonNullable.of(99L);
			when(klassenRepository.existsById(99L)).thenReturn(false);

			assertThatThrownBy(() -> service.patch(ID_LERNABSCHNITT, dto))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessage("Es existiert keine Klasse mit der ID 99.");
			verifyNoInteractions(mapper);
		}

		@Test
		@DisplayName("Förderschwerpunkt 1 mit der ID existiert nicht - BAD_REQUEST")
		void patch_foerderschwerpunkt1NichtVorhanden() {
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.foerderschwerpunkt1ID = JsonNullable.of(99L);
			when(foerderschwerpunktRepository.existsById(99L)).thenReturn(false);

			assertThatThrownBy(() -> service.patch(ID_LERNABSCHNITT, dto))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessage("Es existiert kein Förderschwerpunkt mit der ID 99.");
			verifyNoInteractions(mapper);
		}

		@Test
		@DisplayName("Förderschwerpunkt 2 mit der ID existiert nicht - BAD_REQUEST")
		void patch_foerderschwerpunkt2NichtVorhanden() {
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.foerderschwerpunkt2ID = JsonNullable.of(99L);
			when(foerderschwerpunktRepository.existsById(99L)).thenReturn(false);

			assertThatThrownBy(() -> service.patch(ID_LERNABSCHNITT, dto))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessage("Es existiert kein Förderschwerpunkt mit der ID 99.");
			verifyNoInteractions(mapper);
		}

		@Test
		@DisplayName("Fachklasse mit der ID existiert nicht - BAD_REQUEST")
		void patch_fachklasseNichtVorhanden() {
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.fachklasseID = JsonNullable.of(99L);
			when(fachklasseRepository.existsById(99L)).thenReturn(false);

			assertThatThrownBy(() -> service.patch(ID_LERNABSCHNITT, dto))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessage("Es existiert keine Fachklasse mit der ID 99.");
			verifyNoInteractions(mapper);
		}

		@Test
		@DisplayName("Klassenart mit der ID existiert nicht - BAD_REQUEST")
		void patch_klassenartUngueltig() {
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.idKlassenart = JsonNullable.of(999999L);

			assertThatThrownBy(() -> service.patch(ID_LERNABSCHNITT, dto))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessage("Es existiert keine gültige Klassenart mit der ID 999999.");
			verifyNoInteractions(mapper);
		}

		@Test
		@DisplayName("Schulgliederung mit der ID existiert nicht - BAD_REQUEST")
		void patch_schulgliederungUngueltig() {
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.idSchulgliederung = JsonNullable.of(999999L);

			assertThatThrownBy(() -> service.patch(ID_LERNABSCHNITT, dto))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessage("Es existiert keine gültige Schulgliederung mit der ID 999999.");
			verifyNoInteractions(mapper);
		}

		@Test
		@DisplayName("Organisationsform mit der ID existiert für die Schulform nicht - BAD_REQUEST")
		void patch_organisationsformUngueltig() {
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.idOrganisationsform = JsonNullable.of(999999L);

			assertThatThrownBy(() -> service.patch(ID_LERNABSCHNITT, dto))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessage("Es existiert keine gültige Organisationsform mit der ID 999999 für die Schulform GY.");
			verifyNoInteractions(mapper);
		}

		@Test
		@DisplayName("Besuchsjahre in der Schuleingangsphase mit der ID existieren nicht - BAD_REQUEST")
		void patch_epJahreUngueltig() {
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.idEpJahre = JsonNullable.of(999999L);

			assertThatThrownBy(() -> service.patch(ID_LERNABSCHNITT, dto))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessage("Es existiert keine gültige Anzahl an Besuchsjahren in der Schuleingangsphase mit der ID 999999.");
			verifyNoInteractions(mapper);
		}

		@Test
		@DisplayName("Besuchsjahre 4 in der Schuleingangsphase gibt es im Katalog nicht (alt erlaubt) - BAD_REQUEST")
		void patch_epJahreVierUngueltig() {
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.idEpJahre = JsonNullable.of(4L);

			assertThatThrownBy(() -> service.patch(ID_LERNABSCHNITT, dto))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessage("Es existiert keine gültige Anzahl an Besuchsjahren in der Schuleingangsphase mit der ID 4.");
			verifyNoInteractions(mapper);
		}

		@Test
		@DisplayName("Versetzungsvermerk mit der ID existiert für die Schulform nicht - BAD_REQUEST")
		void patch_versetzungsvermerkUngueltig() {
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.idVersetzungsvermerk = JsonNullable.of(999999L);

			assertThatThrownBy(() -> service.patch(ID_LERNABSCHNITT, dto))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessage("Es existiert kein gültiger Versetzungsvermerk mit der ID 999999 im Schuljahr 2024 für die Schulform GY.");
			verifyNoInteractions(mapper);
		}

		@Test
		@DisplayName("Bilingualer Zweig mit dem Kürzel existiert für die Schulform nicht - BAD_REQUEST")
		void patch_bilingualerZweigUngueltig() {
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.bilingualerZweig = JsonNullable.of("XX");

			assertThatThrownBy(() -> service.patch(ID_LERNABSCHNITT, dto))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessage("Es existiert kein gültiger bilingualer Zweig mit dem Kürzel XX im Schuljahr 2024 für die Schulform GY.");
			verifyNoInteractions(mapper);
		}

		@Test
		@DisplayName("Enddatum vor Anfangsdatum - BAD_REQUEST")
		void patch_datumEndeVorAnfang() {
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.datumEnde = JsonNullable.of("2024-07-31");
			// Zustand der Entity nach dem (gemockten) Mapper
			entity.DatumVon = "2024-08-01";
			entity.DatumBis = "2024-07-31";

			assertThatThrownBy(() -> service.patch(ID_LERNABSCHNITT, dto))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessage("Das Enddatum 2024-07-31 darf nicht vor dem Anfangsdatum 2024-08-01 liegen.");
		}

		@Test
		@DisplayName("Enddatum gleich Anfangsdatum ist zulässig")
		void patch_datumEndeGleichAnfangErlaubt() {
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.datumEnde = JsonNullable.of("2024-08-01");
			// Zustand der Entity nach dem (gemockten) Mapper
			entity.DatumVon = "2024-08-01";
			entity.DatumBis = "2024-08-01";

			service.patch(ID_LERNABSCHNITT, dto);

			verify(mapper).patch(any(), any(), anyInt(), any());
		}

		@Test
		@DisplayName("Leeres Enddatum wird wie null behandelt - kein BAD_REQUEST")
		void patch_datumEndeLeerErlaubt() {
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.datumEnde = JsonNullable.of("");
			entity.DatumVon = "2024-08-01";
			entity.DatumBis = "2025-01-31";

			service.patch(ID_LERNABSCHNITT, dto);

			verify(mapper).patch(any(), any(), anyInt(), any());
		}

		@Test
		@DisplayName("Unentschuldigte Fehlstunden übersteigen die gesamten Fehlstunden - BAD_REQUEST")
		void patch_fehlstundenUnentschuldigtUeberGesamt() {
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.fehlstundenUnentschuldigt = JsonNullable.of(8);
			// Zustand der Entity nach dem (gemockten) Mapper
			entity.SumFehlStd = 3;
			entity.SumFehlStdU = 8;

			assertThatThrownBy(() -> service.patch(ID_LERNABSCHNITT, dto))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessage("Die unentschuldigten Fehlstunden (8) dürfen die gesamten Fehlstunden (3) nicht übersteigen.");
		}

		@Test
		@DisplayName("Unentschuldigte Fehlstunden gleich den gesamten Fehlstunden ist zulässig")
		void patch_fehlstundenUnentschuldigtGleichGesamtErlaubt() {
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.fehlstundenGesamt = JsonNullable.of(5);
			// Zustand der Entity nach dem (gemockten) Mapper
			entity.SumFehlStd = 5;
			entity.SumFehlStdU = 5;

			service.patch(ID_LERNABSCHNITT, dto);

			verify(mapper).patch(any(), any(), anyInt(), any());
		}

		@Test
		@DisplayName("Lernabschnitt mit der ID existiert nicht - NOT_FOUND")
		void patch_lernabschnittNichtVorhanden() {
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.fehlstundenGesamt = JsonNullable.of(5);
			when(repository.findById(99L)).thenReturn(Optional.empty());

			assertThatThrownBy(() -> service.patch(99L, dto))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND)
					.hasMessage("Keine Lernabschnittsdaten zur Abschnitt-ID 99 gefunden.");
			verifyNoInteractions(benutzerKompetenzService, mapper);
		}

		@Test
		@DisplayName("Klassenwechsel - Kompetenz wird für alte und neue Klasse geprüft")
		void patch_kompetenzAlteUndNeueKlasse() {
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.klassenID = JsonNullable.of(47L);
			entity.Klassen_ID = 46L;
			when(klassenRepository.existsById(47L)).thenReturn(true);
			when(klassenRepository.findById(47L)).thenReturn(Optional.of(createKlasse(47L, ID_SCHULJAHRESABSCHNITT, null)));

			service.patch(ID_LERNABSCHNITT, dto);

			verify(benutzerKompetenzService).pruefeKompetenzLeistungsdatenAendernKlassen(46L, 47L);
		}

		@Test
		@DisplayName("Kein Klassenwechsel - Kompetenz wird nur für die aktuelle Klasse geprüft")
		void patch_kompetenzNurAktuelleKlasse() {
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			entity.Klassen_ID = 46L;

			service.patch(ID_LERNABSCHNITT, dto);

			verify(benutzerKompetenzService).pruefeKompetenzLeistungsdatenAendernKlassen(46L);
		}

		@Test
		@DisplayName("Keine Kompetenz für die Klasse - FORBIDDEN, es wird nichts gepatcht")
		void patch_keineKompetenz() {
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			entity.Klassen_ID = 46L;
			doThrow(new ApiOperationException(Response.Status.FORBIDDEN, "keine Kompetenz"))
					.when(benutzerKompetenzService).pruefeKompetenzLeistungsdatenAendernKlassen(46L);

			assertThatThrownBy(() -> service.patch(ID_LERNABSCHNITT, dto))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.FORBIDDEN);
			verifyNoInteractions(mapper);
		}
	}

	// -------------------------------------------------------------------------
	// patchBemerkungen
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("patchBemerkungen")
	class PatchBemerkungen {

		private MockedStatic<TransactionSupport> transactionSupport;

		@BeforeEach
		void setUp() {
			// führt den Task direkt aus, ohne echte Datenbank-Transaktion (Runnable-Variante, da ohne Rückgabe)
			transactionSupport = mockStatic(TransactionSupport.class);
			transactionSupport.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Runnable>any()))
					.thenAnswer(invocation -> {
						invocation.getArgument(0, Runnable.class).run();
						return null;
					});
			lenient().when(repository.findById(ID_LERNABSCHNITT)).thenReturn(Optional.of(entity));
		}

		@AfterEach
		void tearDown() {
			transactionSupport.close();
		}

		@Test
		@DisplayName("Lernabschnitt mit der ID existiert nicht - NOT_FOUND")
		void patchBemerkungen_lernabschnittNichtVorhanden() {
			final var dto = new SchuelerLernabschnittBemerkungenPatchRequest();
			when(repository.findById(99L)).thenReturn(Optional.empty());

			assertThatThrownBy(() -> service.patchBemerkungen(99L, dto))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
		}

		@Test
		@DisplayName("Nur allgemeine Zeugnisbemerkung - wird am Lernabschnitt gesetzt, Fach-Bemerkungen bleiben unberührt")
		void patchBemerkungen_nurZeugnisAllgemein() {
			final var dto = new SchuelerLernabschnittBemerkungenPatchRequest();
			dto.zeugnisAllgemein = JsonNullable.of("Versetzt.");

			service.patchBemerkungen(ID_LERNABSCHNITT, dto);

			assertThat(entity.ZeugnisBem).isEqualTo("Versetzt.");
			verify(bemerkungRepository, never()).findMapByLernabschnittID(anyCollection());
			verify(bemerkungRepository, never()).create(any(DTOSchuelerPSFachBemerkungen.class));
		}

		@Test
		@DisplayName("Fach-Bemerkungen existieren - Feld wird gesetzt, es wird kein neuer Datensatz angelegt")
		void patchBemerkungen_fachBemerkungVorhanden() {
			final var dto = new SchuelerLernabschnittBemerkungenPatchRequest();
			dto.zeugnisASV = JsonNullable.of("Arbeitet sorgfältig.");
			final var fachBem = new DTOSchuelerPSFachBemerkungen(5L, ID_LERNABSCHNITT);
			fachBem.AUE = "bleibt";
			when(bemerkungRepository.findMapByLernabschnittID(List.of(ID_LERNABSCHNITT))).thenReturn(Map.of(ID_LERNABSCHNITT, fachBem));

			service.patchBemerkungen(ID_LERNABSCHNITT, dto);

			assertThat(fachBem.ASV).isEqualTo("Arbeitet sorgfältig.");
			assertThat(fachBem.AUE).isEqualTo("bleibt");
			assertThat(entity.ZeugnisBem).isNull();
			verify(bemerkungRepository, never()).create(any(DTOSchuelerPSFachBemerkungen.class));
		}

		@Test
		@DisplayName("Fach-Bemerkungen fehlen - neuer Datensatz zum Lernabschnitt wird angelegt")
		void patchBemerkungen_fachBemerkungWirdAngelegt() {
			final var dto = new SchuelerLernabschnittBemerkungenPatchRequest();
			dto.versetzungsentscheidung = JsonNullable.of("Versetzt nach Konferenzbeschluss.");

			service.patchBemerkungen(ID_LERNABSCHNITT, dto);

			final ArgumentCaptor<DTOSchuelerPSFachBemerkungen> captor = ArgumentCaptor.forClass(DTOSchuelerPSFachBemerkungen.class);
			verify(bemerkungRepository).create(captor.capture());
			assertThat(captor.getValue().Abschnitt_ID).isEqualTo(ID_LERNABSCHNITT);
			assertThat(captor.getValue().BemerkungVersetzung).isEqualTo("Versetzt nach Konferenzbeschluss.");
		}

		@Test
		@DisplayName("Leerer Patch - es wird nichts geändert und nichts angelegt")
		void patchBemerkungen_leererPatch() {
			final var dto = new SchuelerLernabschnittBemerkungenPatchRequest();
			entity.ZeugnisBem = "bleibt";

			service.patchBemerkungen(ID_LERNABSCHNITT, dto);

			assertThat(entity.ZeugnisBem).isEqualTo("bleibt");
			verify(bemerkungRepository, never()).create(any(DTOSchuelerPSFachBemerkungen.class));
		}

		@Test
		@DisplayName("Keine Kompetenz für die Klasse - FORBIDDEN, es wird nichts geändert")
		void patchBemerkungen_keineKompetenz() {
			final var dto = new SchuelerLernabschnittBemerkungenPatchRequest();
			dto.zeugnisAllgemein = JsonNullable.of("neu");
			entity.Klassen_ID = 46L;
			entity.ZeugnisBem = "alt";
			doThrow(new ApiOperationException(Response.Status.FORBIDDEN, "keine Kompetenz"))
					.when(benutzerKompetenzService).pruefeKompetenzLeistungsdatenAendernKlassen(46L);

			assertThatThrownBy(() -> service.patchBemerkungen(ID_LERNABSCHNITT, dto))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.FORBIDDEN);
			assertThat(entity.ZeugnisBem).isEqualTo("alt");
			verify(bemerkungRepository, never()).create(any(DTOSchuelerPSFachBemerkungen.class));
		}
	}

	// -------------------------------------------------------------------------
	// create
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("create")
	class Create {

		private MockedStatic<TransactionSupport> transactionSupport;

		@BeforeEach
		void setUp() {
			// führt den Task direkt aus, ohne echte Datenbank-Transaktion (Runnable-Variante, da ohne Rückgabe)
			transactionSupport = mockStatic(TransactionSupport.class);
			transactionSupport.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Runnable>any()))
					.thenAnswer(invocation -> {
						invocation.getArgument(0, Runnable.class).run();
						return null;
					});
			lenient().when(schuljahresabschnitteRepo.findListByIds(anyCollection()))
					.thenReturn(List.of(createSchuljahresabschnitt(ID_SCHULJAHRESABSCHNITT)));
		}

		@AfterEach
		void tearDown() {
			transactionSupport.close();
		}

		@Test
		@DisplayName("Mit Klasse und Jahrgang - Lernabschnitt wird mit Defaults und ASDJahrgang angelegt")
		void create_mitKlasseUndJahrgang() {
			when(klassenRepository.existsById(46L)).thenReturn(true);
			when(klassenRepository.findById(46L)).thenReturn(Optional.of(createKlasse(46L, ID_SCHULJAHRESABSCHNITT, 17L)));
			when(jahrgangRepository.existsById(18L)).thenReturn(true);
			when(jahrgangRepository.findById(18L)).thenReturn(Optional.of(createJahrgang(18L, "05")));
			when(jahrgangRepository.findById(17L)).thenReturn(Optional.of(createJahrgang(17L, "05")));
			when(mapper.toDomain(any(SchuelerLernabschnittCreateRequest.class)))
					.thenAnswer(invocation -> SchuelerLernabschnittsdatenMapper.INSTANCE.toDomain(invocation.getArgument(0)));

			service.create(new SchuelerLernabschnittCreateRequest(ID_SCHUELER, ID_SCHULJAHRESABSCHNITT, 46L, 18L));

			final ArgumentCaptor<DTOSchuelerLernabschnittsdaten> captor = ArgumentCaptor.forClass(DTOSchuelerLernabschnittsdaten.class);
			verify(repository).create(captor.capture());
			final var neu = captor.getValue();
			assertThat(neu.Schueler_ID).isEqualTo(ID_SCHUELER);
			assertThat(neu.Schuljahresabschnitts_ID).isEqualTo(ID_SCHULJAHRESABSCHNITT);
			assertThat(neu.WechselNr).isZero();
			assertThat(neu.Klassen_ID).isEqualTo(46L);
			assertThat(neu.Jahrgang_ID).isEqualTo(18L);
			assertThat(neu.ASDJahrgang).isEqualTo("05");
			assertThat(neu.SemesterWertung).isTrue();
			assertThat(neu.FachPraktAnteilAusr).isTrue();
			assertThat(neu.meldungBAN).isFalse();
			assertThat(neu.Schwerbehinderung).isFalse();
			assertThat(neu.AOSF).isFalse();
			assertThat(neu.Autist).isFalse();
			assertThat(neu.ZieldifferentesLernen).isFalse();
			assertThat(neu.Wiederholung).isFalse();
			verify(benutzerKompetenzService).pruefeKompetenzLeistungsdatenAendernKlassen(46L);
		}

		@Test
		@DisplayName("Schuljahresabschnitt existiert nicht - BAD_REQUEST, es wird nichts angelegt")
		void create_schuljahresabschnittNichtVorhanden() {
			when(schuljahresabschnitteRepo.findListByIds(anyCollection())).thenReturn(List.of());

			assertThatThrownBy(() -> service.create(new SchuelerLernabschnittCreateRequest(ID_SCHUELER, 99L, null, null)))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
			verify(repository, never()).create(any(DTOSchuelerLernabschnittsdaten.class));
		}

		@Test
		@DisplayName("Klasse gehört zu einem anderen Schuljahresabschnitt - BAD_REQUEST")
		void create_klasseAndererSchuljahresabschnitt() {
			when(klassenRepository.existsById(46L)).thenReturn(true);
			when(klassenRepository.findById(46L)).thenReturn(Optional.of(createKlasse(46L, 21L, null)));

			assertThatThrownBy(() -> service.create(new SchuelerLernabschnittCreateRequest(ID_SCHUELER, ID_SCHULJAHRESABSCHNITT, 46L, null)))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessage("Die Klasse mit der ID 46 gehört nicht zum Schuljahresabschnitt des Lernabschnitts.");
			verify(repository, never()).create(any(DTOSchuelerLernabschnittsdaten.class));
		}

		@Test
		@DisplayName("Jahrgang passt nicht zum Jahrgang der Klasse - BAD_REQUEST")
		void create_jahrgangNichtKompatibelZurKlasse() {
			when(klassenRepository.existsById(46L)).thenReturn(true);
			when(klassenRepository.findById(46L)).thenReturn(Optional.of(createKlasse(46L, ID_SCHULJAHRESABSCHNITT, 17L)));
			when(jahrgangRepository.existsById(18L)).thenReturn(true);
			when(jahrgangRepository.findById(18L)).thenReturn(Optional.of(createJahrgang(18L, "06")));
			when(jahrgangRepository.findById(17L)).thenReturn(Optional.of(createJahrgang(17L, "05")));
			when(mapper.toDomain(any(SchuelerLernabschnittCreateRequest.class)))
					.thenAnswer(invocation -> SchuelerLernabschnittsdatenMapper.INSTANCE.toDomain(invocation.getArgument(0)));

			assertThatThrownBy(() -> service.create(new SchuelerLernabschnittCreateRequest(ID_SCHUELER, ID_SCHULJAHRESABSCHNITT, 46L, 18L)))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessage("Der Jahrgang mit der ID 18 ist für die Klasse mit der ID 46 nicht zulässig.");
			verify(repository, never()).create(any(DTOSchuelerLernabschnittsdaten.class));
		}

		@Test
		@DisplayName("Keine Kompetenz für die Klasse - FORBIDDEN, es wird nichts angelegt")
		void create_keineKompetenz() {
			doThrow(new ApiOperationException(Response.Status.FORBIDDEN, "keine Kompetenz"))
					.when(benutzerKompetenzService).pruefeKompetenzLeistungsdatenAendernKlassen(46L);

			assertThatThrownBy(() -> service.create(new SchuelerLernabschnittCreateRequest(ID_SCHUELER, ID_SCHULJAHRESABSCHNITT, 46L, null)))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.FORBIDDEN);
			verify(repository, never()).create(any(DTOSchuelerLernabschnittsdaten.class));
			verifyNoInteractions(klassenRepository);
		}
	}
}
