package de.svws_nrw.service.klassen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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

import de.svws_nrw.asd.data.klassen.KlassenDaten;
import de.svws_nrw.asd.data.schueler.Schueler;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.klassen.Klassenart;
import de.svws_nrw.asd.types.schule.AllgemeinbildendOrganisationsformen;
import de.svws_nrw.asd.types.schule.BerufskollegOrganisationsformen;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.asd.types.schule.WeiterbildungskollegOrganisationsformen;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.schild.schule.DTOTeilstandorte;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.klassen.KlassenMapper;
import de.svws_nrw.mapper.klassen.KlassenMappingContext;
import de.svws_nrw.mapper.schueler.SchuelerMapper;
import de.svws_nrw.repo.RepositoryException;
import de.svws_nrw.repo.klassen.KlassenRepository;
import de.svws_nrw.repo.klassen.KlassenleitungenRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepository;
import de.svws_nrw.repo.schule.kataloge.jahrgang.JahrgangRepository;
import de.svws_nrw.repo.schule.teilstandort.TeilstandortRepository;
import de.svws_nrw.service.schule.EigeneSchuleService;
import de.svws_nrw.service.schule.SchuljahresabschnittService;
import jakarta.ws.rs.core.Response;

/**
 * Test für die Klasse {@link KlassenService} (Lesen, Anlegen, Ändern, Löschen und Default-Sortierung)
 */
@ExtendWith(MockitoExtension.class)
class KlassenServiceTest {

	private static final int SCHULJAHR = 2024;

	private static final long ID_ABSCHNITT_1 = 11L;
	private static final long ID_ABSCHNITT_2 = 12L;
	private static final long ID_ABSCHNITT_VORHER = 10L;
	private static final long ID_NEUE_KLASSE = 99L;
	private static final long ID_KLASSE = 1L;
	private static final long ID_JAHRGANG = 4L;

	@Mock
	private KlassenRepository klassenRepository;

	@Mock
	private KlassenleitungenRepository klassenleitungenRepository;

	@Mock
	private SchuelerLernabschnittRepository lernabschnittRepository;

	@Mock
	private SchuelerRepository schuelerRepository;

	@Mock
	private EigeneSchuleService eigeneSchuleService;

	@Mock
	private SchuljahresabschnittService schuljahresabschnittService;

	@Mock
	private JahrgangRepository jahrgangRepository;

	@Mock
	private TeilstandortRepository teilstandortRepository;

	@Mock
	private KlassenleitungService klassenleitungService;

	private final Map<Long, Schuljahresabschnitt> abschnitte = new HashMap<>();

	private MockedStatic<TransactionSupport> transactionSupport;

	private KlassenMappingContextLoader kontextLoader;

	private KlassenService service;

	@BeforeAll
	static void initAsd() {
		ASDCoreTypeUtils.initAll();
	}

	@BeforeEach
	void setUp() {
		transactionSupport = mockStatic(TransactionSupport.class);
		transactionSupport.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());
		transactionSupport.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Runnable>any()))
				.thenAnswer(invocation -> {
					invocation.getArgument(0, Runnable.class).run();

					return null;
				});
		lenient().when(eigeneSchuleService.getSchulform()).thenReturn(Schulform.GY);
		lenient().when(schuljahresabschnittService.getMapByIds(anyCollection())).thenAnswer(invocation -> {
			final Collection<Long> ids = invocation.getArgument(0);
			return ids.stream()
					.filter(abschnitte::containsKey)
					.collect(Collectors.toMap(Function.identity(), abschnitte::get));
		});
		final KlassenSchuelerLoader schuelerLoader = new KlassenSchuelerLoader(lernabschnittRepository, schuelerRepository, SchuelerMapper.INSTANCE);
		kontextLoader = spy(new KlassenMappingContextLoader(klassenRepository, klassenleitungenRepository, schuelerLoader,
				eigeneSchuleService, schuljahresabschnittService));
		service = new KlassenService(
				new KlassenRepositories(klassenRepository, jahrgangRepository, teilstandortRepository, lernabschnittRepository),
				schuljahresabschnittService,
				eigeneSchuleService,
				kontextLoader,
				KlassenMapper.INSTANCE,
				klassenleitungService,
				new KlassenDefaultSorter(klassenRepository, jahrgangRepository));
	}

	@AfterEach
	void tearDown() {
		transactionSupport.close();
	}

	private static Schuljahresabschnitt createAbschnitt(final long id, final int abschnitt, final Long idVoriger, final Long idFolge) {
		final Schuljahresabschnitt result = new Schuljahresabschnitt();
		result.id = id;
		result.schuljahr = SCHULJAHR;
		result.abschnitt = abschnitt;
		result.idVorigerAbschnitt = idVoriger;
		result.idFolgeAbschnitt = idFolge;
		return result;
	}

	private static DTOKlassen createKlasse(final long id, final long idAbschnitt, final String kuerzel) {
		return new DTOKlassen(id, idAbschnitt, kuerzel, false, false);
	}

	private static DTOSchueler createSchueler(final long id, final boolean geloescht) {
		final DTOSchueler schueler = new DTOSchueler(id, "GU-" + id, 2, false);
		schueler.Nachname = "Nachname" + id;
		schueler.Vorname = "Vorname" + id;
		schueler.Geschlecht = Geschlecht.W;
		schueler.hatLoeschvermerk = geloescht;
		return schueler;
	}

	private void stubAbschnitt(final long id, final int abschnitt, final Long idVoriger, final Long idFolge) {
		final var schuljahresabschnitt = createAbschnitt(id, abschnitt, idVoriger, idFolge);
		abschnitte.put(id, schuljahresabschnitt);
		lenient().when(schuljahresabschnittService.getById(id)).thenReturn(schuljahresabschnitt);
	}

	private static void assertApiError(final Runnable aufruf, final Response.Status status, final String meldung) {
		assertThatThrownBy(aufruf::run)
				.isInstanceOfSatisfying(ApiOperationException.class, e -> {
					assertThat(e.getStatus()).isEqualTo(status);
					assertThat(e.getBody()).isEqualTo(meldung);
				});
	}

	private static Schuljahresabschnitt createStandardAbschnitt() {
		return createAbschnitt(ID_ABSCHNITT_1, 1, ID_ABSCHNITT_VORHER, ID_ABSCHNITT_2);
	}

	private static KlassenCreateRequest createRequest() {
		final var request = new KlassenCreateRequest();
		request.idSchuljahresabschnitt = ID_ABSCHNITT_1;
		request.kuerzel = "05a";
		request.idJahrgang = ID_JAHRGANG;

		return request;
	}

	private static DTOJahrgang createJahrgang(final String asdJahrgang) {
		final var jahrgang = new DTOJahrgang(ID_JAHRGANG);
		jahrgang.ASDJahrgang = asdJahrgang;

		return jahrgang;
	}

	private static DTOKlassen createStandardKlasse() {
		final var klasse = new DTOKlassen(ID_KLASSE, ID_ABSCHNITT_1, "05a", false, false);
		klasse.ASDKlasse = "05A";
		klasse.Jahrgang_ID = ID_JAHRGANG;

		return klasse;
	}

	private static void assertApiError(final Runnable aufruf, final Response.Status status) {
		assertThatThrownBy(aufruf::run)
				.isInstanceOfSatisfying(ApiOperationException.class, e -> assertThat(e.getStatus()).isEqualTo(status));
	}

	/** Bereitet die Mocks für ein erfolgreiches Anlegen vor. */
	private void stubCreate() {
		when(schuljahresabschnittService.findById(ID_ABSCHNITT_1)).thenReturn(Optional.of(createStandardAbschnitt()));
		when(teilstandortRepository.findFirst()).thenReturn(Optional.of(new DTOTeilstandorte("A")));
		lenient().when(klassenRepository.create(any(DTOKlassen.class))).thenAnswer(invocation -> {
			final DTOKlassen dto = invocation.getArgument(0);
			dto.ID = ID_NEUE_KLASSE;

			return dto;
		});
		lenient().doAnswer(invocation -> Map.of(ID_NEUE_KLASSE,
				new KlassenMappingContext(eigeneSchuleService.getSchulform(), createStandardAbschnitt(), null, null, null, null)))
				.when(kontextLoader).load(anyList(), eq(true));
	}

	private void stubJahrgang(final String asdJahrgang) {
		when(jahrgangRepository.findById(ID_JAHRGANG)).thenReturn(Optional.of(createJahrgang(asdJahrgang)));
	}

	private DTOKlassen createAndCapture(final KlassenCreateRequest request) {
		service.create(request);
		final var captor = ArgumentCaptor.forClass(DTOKlassen.class);
		verify(klassenRepository).create(captor.capture());

		return captor.getValue();
	}

	@Nested
	@DisplayName("getById")
	class GetById {

		@Test
		@DisplayName("Klasse mit Schülern: Schüler und Klassenleitungen werden geliefert, gelöschte Schüler nicht")
		void klasseMitSchuelern() {
			when(klassenRepository.findById(1L)).thenReturn(Optional.of(createKlasse(1L, ID_ABSCHNITT_1, "07a")));
			stubAbschnitt(ID_ABSCHNITT_1, 1, null, null);
			when(klassenleitungenRepository.getMapKlassenleitungen(List.of(1L))).thenReturn(Map.of(1L, List.of(5L, 6L)));
			when(lernabschnittRepository.getMapKlassenSchuelerAktuell(List.of(1L))).thenReturn(Map.of(1L, List.of(100L, 101L, 102L)));
			when(schuelerRepository.findListByIds(anyCollection()))
					.thenReturn(List.of(createSchueler(100L, false), createSchueler(101L, true), createSchueler(102L, false)));

			final KlassenDaten result = service.getById(1L);

			assertThat(result.id).isEqualTo(1L);
			assertThat(result.kuerzel).isEqualTo("07a");
			assertThat(result.klassenLeitungen).containsExactly(5L, 6L);
			assertThat(result.schueler).extracting(s -> s.id).containsExactly(100L, 102L);
			verify(klassenleitungenRepository, times(1)).getMapKlassenleitungen(anyCollection());
		}

		@Test
		@DisplayName("Ein Schüler ohne Löschvermerk (null) gilt als nicht gelöscht")
		void schuelerOhneLoeschvermerk() {
			when(klassenRepository.findById(1L)).thenReturn(Optional.of(createKlasse(1L, ID_ABSCHNITT_1, "07a")));
			stubAbschnitt(ID_ABSCHNITT_1, 1, null, null);
			when(klassenleitungenRepository.getMapKlassenleitungen(List.of(1L))).thenReturn(Map.of());
			when(lernabschnittRepository.getMapKlassenSchuelerAktuell(List.of(1L))).thenReturn(Map.of(1L, List.of(100L)));
			final DTOSchueler schueler = createSchueler(100L, false);
			schueler.hatLoeschvermerk = null;
			when(schuelerRepository.findListByIds(anyCollection())).thenReturn(List.of(schueler));

			assertThat(service.getById(1L).schueler).extracting(s -> s.id).containsExactly(100L);
		}

		@Test
		@DisplayName("istJva und istReformpaedagogik der Klasse werden übernommen")
		void jvaUndReformpaedagogik() {
			final var klasse = createKlasse(1L, ID_ABSCHNITT_1, "07a");
			klasse.istJva = true;
			klasse.istReformpaedagogik = true;
			when(klassenRepository.findById(1L)).thenReturn(Optional.of(klasse));
			stubAbschnitt(ID_ABSCHNITT_1, 1, null, null);
			when(klassenleitungenRepository.getMapKlassenleitungen(List.of(1L))).thenReturn(Map.of());

			final KlassenDaten result = service.getById(1L);

			assertThat(result.istJva).isTrue();
			assertThat(result.istReformpaedagogik).isTrue();
		}

		@Test
		@DisplayName("Schüler einer Klasse werden aufsteigend nach ihrer ID sortiert geliefert, unabhängig von der Reihenfolge der Abfrage")
		void schuelerSindNachIdSortiert() {
			when(klassenRepository.findById(1L)).thenReturn(Optional.of(createKlasse(1L, ID_ABSCHNITT_1, "07a")));
			stubAbschnitt(ID_ABSCHNITT_1, 1, null, null);
			when(klassenleitungenRepository.getMapKlassenleitungen(List.of(1L))).thenReturn(Map.of());
			when(lernabschnittRepository.getMapKlassenSchuelerAktuell(List.of(1L))).thenReturn(Map.of(1L, List.of(102L, 100L, 101L)));
			when(schuelerRepository.findListByIds(anyCollection()))
					.thenReturn(List.of(createSchueler(101L, false), createSchueler(102L, false), createSchueler(100L, false)));

			final KlassenDaten result = service.getById(1L);

			assertThat(result.schueler).extracting(s -> s.id).containsExactly(100L, 101L, 102L);
		}

		@Test
		@DisplayName("Schüler werden mit dem SchuelerMapper gemappt")
		void schuelerWerdenGemappt() {
			when(klassenRepository.findById(1L)).thenReturn(Optional.of(createKlasse(1L, ID_ABSCHNITT_1, "07a")));
			stubAbschnitt(ID_ABSCHNITT_1, 1, null, null);
			when(klassenleitungenRepository.getMapKlassenleitungen(List.of(1L))).thenReturn(Map.of());
			when(lernabschnittRepository.getMapKlassenSchuelerAktuell(List.of(1L))).thenReturn(Map.of(1L, List.of(100L)));
			final DTOSchueler dto = createSchueler(100L, false);
			when(schuelerRepository.findListByIds(anyCollection())).thenReturn(List.of(dto));

			final KlassenDaten result = service.getById(1L);

			final Schueler erwartet = SchuelerMapper.INSTANCE.toSchueler(dto);
			assertThat(result.schueler).hasSize(1);
			assertThat(result.schueler.getFirst()).usingRecursiveComparison().isEqualTo(erwartet);
		}

		@Test
		@DisplayName("Klasse ohne Schüler im Datenbestand: Die Schülerliste ist leer")
		void klasseMitSchuelernLeer() {
			when(klassenRepository.findById(1L)).thenReturn(Optional.of(createKlasse(1L, ID_ABSCHNITT_1, "07a")));
			stubAbschnitt(ID_ABSCHNITT_1, 1, null, null);
			when(klassenleitungenRepository.getMapKlassenleitungen(List.of(1L))).thenReturn(Map.of());
			when(lernabschnittRepository.getMapKlassenSchuelerAktuell(List.of(1L))).thenReturn(Map.of());
			when(schuelerRepository.findListByIds(anyCollection())).thenReturn(List.of());

			assertThat(service.getById(1L).schueler).isEmpty();
		}

		@Test
		@DisplayName("Unbekannte ID: 404 mit deutscher Meldung")
		void unbekannteId() {
			when(klassenRepository.findById(99L)).thenReturn(Optional.empty());

			assertApiError(() -> service.getById(99L), Response.Status.NOT_FOUND, "Keine Klasse zur ID 99 gefunden.");
			verifyNoInteractions(klassenleitungenRepository, lernabschnittRepository, schuelerRepository);
		}

		@Test
		@DisplayName("ID null: 400")
		void idNull() {
			assertApiError(() -> service.getById(null), Response.Status.BAD_REQUEST, "Die ID für die Klasse darf nicht null sein.");
		}

		@Test
		@DisplayName("Ungültiger Schuljahresabschnitt der Klasse: 404 des Abschnittsservice wird weitergegeben")
		void ungueltigerAbschnittDerKlasse() {
			when(klassenRepository.findById(1L)).thenReturn(Optional.of(createKlasse(1L, 77L, "07a")));
			when(schuljahresabschnittService.getMapByIds(List.of(77L)))
					.thenThrow(new ApiOperationException(Response.Status.NOT_FOUND, "Der Schuljahresabschnitt mit der ID 77 wurde nicht gefunden."));

			assertApiError(() -> service.getById(1L), Response.Status.NOT_FOUND, "Der Schuljahresabschnitt mit der ID 77 wurde nicht gefunden.");
			verifyNoInteractions(klassenleitungenRepository);
		}

	}

	@Nested
	@DisplayName("getListBySchuljahresabschnitt")
	class GetListBySchuljahresabschnitt {

		@Test
		@DisplayName("Mit Schülern: Alle Klassen mit Schülern, Leitungen und Schüler werden jeweils nur einmal abgefragt")
		void mitSchuelern() {
			stubAbschnitt(ID_ABSCHNITT_1, 1, null, null);
			when(klassenRepository.getListBySchuljahresabschnitt(ID_ABSCHNITT_1))
					.thenReturn(List.of(createKlasse(1L, ID_ABSCHNITT_1, "07a"), createKlasse(2L, ID_ABSCHNITT_1, "07b")));
			when(klassenleitungenRepository.getMapKlassenleitungen(List.of(1L, 2L))).thenReturn(Map.of(1L, List.of(5L), 2L, List.of()));
			when(lernabschnittRepository.getMapKlassenSchuelerAktuell(List.of(1L, 2L))).thenReturn(Map.of(1L, List.of(100L), 2L, List.of(200L, 201L)));
			when(schuelerRepository.findListByIds(anyCollection()))
					.thenReturn(List.of(createSchueler(100L, false), createSchueler(200L, false), createSchueler(201L, true)));

			final List<KlassenDaten> result = service.getListBySchuljahresabschnitt(ID_ABSCHNITT_1, true);

			assertThat(result).extracting(k -> k.kuerzel).containsExactly("07a", "07b");
			assertThat(result.get(0).schueler).extracting(s -> s.id).containsExactly(100L);
			assertThat(result.get(1).schueler).extracting(s -> s.id).containsExactly(200L);
			verify(klassenleitungenRepository, times(1)).getMapKlassenleitungen(anyCollection());
			verify(lernabschnittRepository, times(1)).getMapKlassenSchuelerAktuell(anyCollection());
			verify(schuelerRepository, times(1)).findListByIds(anyCollection());
		}

		@Test
		@DisplayName("Ohne Schüler: Die Schüler werden nicht abgefragt")
		void ohneSchueler() {
			stubAbschnitt(ID_ABSCHNITT_1, 1, null, null);
			when(klassenRepository.getListBySchuljahresabschnitt(ID_ABSCHNITT_1))
					.thenReturn(List.of(createKlasse(1L, ID_ABSCHNITT_1, "07a"), createKlasse(2L, ID_ABSCHNITT_1, "07b")));
			when(klassenleitungenRepository.getMapKlassenleitungen(List.of(1L, 2L))).thenReturn(Map.of());

			final List<KlassenDaten> result = service.getListBySchuljahresabschnitt(ID_ABSCHNITT_1, false);

			assertThat(result).hasSize(2).allSatisfy(k -> assertThat(k.schueler).isEmpty());
			verify(klassenleitungenRepository, times(1)).getMapKlassenleitungen(anyCollection());
			verifyNoInteractions(lernabschnittRepository, schuelerRepository);
		}

		@Test
		@DisplayName("Leerer Abschnitt: Leere Liste ohne weitere Abfragen")
		void leererAbschnitt() {
			stubAbschnitt(ID_ABSCHNITT_1, 1, null, null);
			when(klassenRepository.getListBySchuljahresabschnitt(ID_ABSCHNITT_1)).thenReturn(List.of());

			assertThat(service.getListBySchuljahresabschnitt(ID_ABSCHNITT_1, true)).isEmpty();
			verifyNoInteractions(klassenleitungenRepository, lernabschnittRepository, schuelerRepository);
		}

		@Test
		@DisplayName("Ungültiger Abschnitt: 404 mit deutscher Meldung, auch ohne Klassen")
		void ungueltigerAbschnitt() {
			when(schuljahresabschnittService.getById(77L))
					.thenThrow(new ApiOperationException(Response.Status.NOT_FOUND, "Der Schuljahresabschnitt mit der ID 77 wurde nicht gefunden."));

			assertApiError(() -> service.getListBySchuljahresabschnitt(77L, true), Response.Status.NOT_FOUND,
					"Der Schuljahresabschnitt mit der ID 77 wurde nicht gefunden.");
			verifyNoInteractions(klassenRepository, klassenleitungenRepository);
		}

	}

	@Nested
	@DisplayName("getListByIdsMitSchueler")
	class GetListByIds {

		@Test
		@DisplayName("Klassen aus verschiedenen Abschnitten: Reihenfolge der IDs bleibt erhalten, alle Abschnitte werden mit einer Abfrage geladen")
		void verschiedeneAbschnitte() {
			stubAbschnitt(ID_ABSCHNITT_1, 1, null, null);
			stubAbschnitt(ID_ABSCHNITT_2, 2, null, null);
			when(klassenRepository.findMapByIds(Set.of(2L, 1L, 3L))).thenReturn(Map.of(
					1L, createKlasse(1L, ID_ABSCHNITT_1, "07a"),
					2L, createKlasse(2L, ID_ABSCHNITT_2, "07a"),
					3L, createKlasse(3L, ID_ABSCHNITT_1, "07b")));
			when(klassenleitungenRepository.getMapKlassenleitungen(List.of(2L, 1L, 3L))).thenReturn(Map.of());

			final List<KlassenDaten> result = service.getListByIdsMitSchueler(List.of(2L, 1L, 3L));

			assertThat(result).extracting(k -> k.id).containsExactly(2L, 1L, 3L);
			verify(schuljahresabschnittService, times(1)).getMapByIds(argThat(ids -> (ids.size() == 2) && ids.containsAll(List.of(ID_ABSCHNITT_1, ID_ABSCHNITT_2))));
			verify(klassenleitungenRepository, times(1)).getMapKlassenleitungen(anyCollection());
			verify(lernabschnittRepository, times(1)).getMapKlassenSchuelerAktuell(anyCollection());
		}

		@Test
		@DisplayName("Doppelte IDs werden nur einmal berücksichtigt")
		void doppelteIds() {
			stubAbschnitt(ID_ABSCHNITT_1, 1, null, null);
			when(klassenRepository.findMapByIds(Set.of(1L))).thenReturn(Map.of(1L, createKlasse(1L, ID_ABSCHNITT_1, "07a")));
			when(klassenleitungenRepository.getMapKlassenleitungen(List.of(1L))).thenReturn(Map.of());

			assertThat(service.getListByIdsMitSchueler(List.of(1L, 1L))).extracting(k -> k.id).containsExactly(1L);
		}

		@Test
		@DisplayName("Fehlende IDs wie im Bestand: 404, es wird keine Teilmenge geliefert")
		void fehlendeIds() {
			when(klassenRepository.findMapByIds(Set.of(1L, 99L))).thenReturn(Map.of(1L, createKlasse(1L, ID_ABSCHNITT_1, "07a")));

			assertApiError(() -> service.getListByIdsMitSchueler(List.of(1L, 99L)), Response.Status.NOT_FOUND,
					"Es wurden nicht alle Klassen zu den IDs gefunden.");
			verifyNoInteractions(klassenleitungenRepository);
		}

		@Test
		@DisplayName("Leere ID-Liste: Leere Liste ohne Datenbankzugriff")
		void leereIdListe() {
			assertThat(service.getListByIdsMitSchueler(List.of())).isEmpty();
			verifyNoInteractions(klassenRepository, klassenleitungenRepository, lernabschnittRepository, schuelerRepository);
		}

		@Test
		@DisplayName("ID-Liste null: 400")
		void idListeNull() {
			assertApiError(() -> service.getListByIdsMitSchueler(null), Response.Status.BAD_REQUEST, "Die IDs für die Klassen dürfen nicht null sein.");
		}

		@Test
		@DisplayName("Mit Schülern: Schüler werden geliefert, gelöschte nicht")
		void mitSchuelern() {
			stubAbschnitt(ID_ABSCHNITT_1, 1, null, null);
			when(klassenRepository.findMapByIds(Set.of(1L, 2L))).thenReturn(Map.of(
					1L, createKlasse(1L, ID_ABSCHNITT_1, "07a"),
					2L, createKlasse(2L, ID_ABSCHNITT_1, "07b")));
			when(klassenleitungenRepository.getMapKlassenleitungen(List.of(1L, 2L))).thenReturn(Map.of());
			when(lernabschnittRepository.getMapKlassenSchuelerAktuell(List.of(1L, 2L))).thenReturn(Map.of(1L, List.of(100L, 101L)));
			when(schuelerRepository.findListByIds(anyCollection())).thenReturn(List.of(createSchueler(100L, false), createSchueler(101L, true)));

			final List<KlassenDaten> result = service.getListByIdsMitSchueler(List.of(1L, 2L));

			assertThat(result.get(0).schueler).extracting(s -> s.id).containsExactly(100L);
			assertThat(result.get(1).schueler).isEmpty();
			verify(klassenleitungenRepository, times(1)).getMapKlassenleitungen(anyCollection());
		}

		@Test
		@DisplayName("Ohne Schüler: Es werden keine Schüler geladen, die Reihenfolge der IDs bleibt erhalten")
		void ohneSchueler() {
			stubAbschnitt(ID_ABSCHNITT_1, 1, null, null);
			when(klassenRepository.findMapByIds(Set.of(2L, 1L))).thenReturn(Map.of(
					1L, createKlasse(1L, ID_ABSCHNITT_1, "07a"),
					2L, createKlasse(2L, ID_ABSCHNITT_1, "07b")));
			when(klassenleitungenRepository.getMapKlassenleitungen(List.of(2L, 1L))).thenReturn(Map.of());

			final List<KlassenDaten> result = service.getListByIds(List.of(2L, 1L), false);

			assertThat(result).extracting(k -> k.id).containsExactly(2L, 1L);
			assertThat(result).allSatisfy(k -> assertThat(k.schueler).isEmpty());
			verifyNoInteractions(lernabschnittRepository, schuelerRepository);
		}

	}

	@Nested
	@DisplayName("Vorgänger- und Folgeklasse")
	class Nachbarklassen {

		@Test
		@DisplayName("Halbjahresbetrieb, 2. Abschnitt: Vorgängerklasse ist die Klasse mit dem eigenen Kürzel im 1. Abschnitt")
		void halbjahresbetriebVorgaenger() {
			final DTOKlassen klasse = createKlasse(1L, ID_ABSCHNITT_2, "07a");
			klasse.VKlasse = "06a";
			when(klassenRepository.findById(1L)).thenReturn(Optional.of(klasse));
			stubAbschnitt(ID_ABSCHNITT_2, 2, ID_ABSCHNITT_1, null);
			when(klassenRepository.getListBySchuljahresabschnitt(ID_ABSCHNITT_1)).thenReturn(List.of(createKlasse(50L, ID_ABSCHNITT_1, "07a")));
			when(klassenleitungenRepository.getMapKlassenleitungen(List.of(1L))).thenReturn(Map.of());

			final KlassenDaten result = service.getById(1L);

			assertThat(result.kuerzelVorgaengerklasse).isEqualTo("07a");
			assertThat(result.idVorgaengerklasse).isEqualTo(50L);
		}

		@Test
		@DisplayName("Halbjahresbetrieb, 1. Abschnitt: Folgeklasse ist die Klasse mit dem eigenen Kürzel im 2. Abschnitt")
		void halbjahresbetriebFolge() {
			final DTOKlassen klasse = createKlasse(1L, ID_ABSCHNITT_1, "07a");
			klasse.FKlasse = "08a";
			when(klassenRepository.findById(1L)).thenReturn(Optional.of(klasse));
			stubAbschnitt(ID_ABSCHNITT_1, 1, null, ID_ABSCHNITT_2);
			when(klassenRepository.getListBySchuljahresabschnitt(ID_ABSCHNITT_2)).thenReturn(List.of(createKlasse(51L, ID_ABSCHNITT_2, "07a")));
			when(klassenleitungenRepository.getMapKlassenleitungen(List.of(1L))).thenReturn(Map.of());

			final KlassenDaten result = service.getById(1L);

			assertThat(result.kuerzelFolgeklasse).isEqualTo("07a");
			assertThat(result.idFolgeklasse).isEqualTo(51L);
		}

		@Test
		@DisplayName("Weiterbildungskolleg: Vorgänger- und Folgeklasse werden über die gespeicherten Kürzel bestimmt")
		void weiterbildungskolleg() {
			lenient().when(eigeneSchuleService.getSchulform()).thenReturn(Schulform.WB);
			final DTOKlassen klasse = createKlasse(1L, ID_ABSCHNITT_2, "S2");
			klasse.VKlasse = "S1";
			klasse.FKlasse = "S3";
			when(klassenRepository.findById(1L)).thenReturn(Optional.of(klasse));
			stubAbschnitt(ID_ABSCHNITT_2, 2, ID_ABSCHNITT_1, 13L);
			when(klassenRepository.getListBySchuljahresabschnitt(ID_ABSCHNITT_1))
					.thenReturn(List.of(createKlasse(60L, ID_ABSCHNITT_1, "S1"), createKlasse(61L, ID_ABSCHNITT_1, "S2")));
			when(klassenRepository.getListBySchuljahresabschnitt(13L)).thenReturn(List.of(createKlasse(62L, 13L, "S3")));
			when(klassenleitungenRepository.getMapKlassenleitungen(List.of(1L))).thenReturn(Map.of());

			final KlassenDaten result = service.getById(1L);

			assertThat(result.kuerzelVorgaengerklasse).isEqualTo("S1");
			assertThat(result.idVorgaengerklasse).isEqualTo(60L);
			assertThat(result.kuerzelFolgeklasse).isEqualTo("S3");
			assertThat(result.idFolgeklasse).isEqualTo(62L);
		}

		@Test
		@DisplayName("Ohne Nachbarabschnitte: Es werden keine Klassen der Nachbarabschnitte abgefragt und die IDs bleiben null")
		void ohneNachbarabschnitte() {
			final DTOKlassen klasse = createKlasse(1L, ID_ABSCHNITT_1, "07a");
			klasse.VKlasse = "06a";
			klasse.FKlasse = "08a";
			when(klassenRepository.findById(1L)).thenReturn(Optional.of(klasse));
			stubAbschnitt(ID_ABSCHNITT_1, 1, null, null);
			when(klassenleitungenRepository.getMapKlassenleitungen(List.of(1L))).thenReturn(Map.of());

			final KlassenDaten result = service.getById(1L);

			assertThat(result.idVorgaengerklasse).isNull();
			assertThat(result.kuerzelFolgeklasse).isEqualTo("07a");
			assertThat(result.idFolgeklasse).isNull();
			verify(klassenRepository, never()).getListBySchuljahresabschnitt(ID_ABSCHNITT_1);
		}

		@Test
		@DisplayName("Ohne Vorgänger- und Folgeklasse werden die Klassen der Nachbarabschnitte nicht abgefragt")
		void keineNachbarklassenOhneVorgaengerUndFolgeklasse() {
			when(klassenRepository.findById(1L)).thenReturn(Optional.of(createKlasse(1L, ID_ABSCHNITT_1, "07a")));
			stubAbschnitt(ID_ABSCHNITT_1, 1, ID_ABSCHNITT_VORHER, ID_ABSCHNITT_2);
			when(klassenleitungenRepository.getMapKlassenleitungen(List.of(1L))).thenReturn(Map.of());

			final KlassenDaten result = service.getById(1L);

			assertThat(result)
					.extracting(k -> k.idVorgaengerklasse, k -> k.idFolgeklasse)
					.containsExactly(null, null);
			verify(klassenRepository, never()).getListBySchuljahresabschnitt(ID_ABSCHNITT_VORHER);
			verify(klassenRepository, never()).getListBySchuljahresabschnitt(ID_ABSCHNITT_2);
		}

		@Test
		@DisplayName("Klassen der Nachbarabschnitte werden bei mehreren Klassen desselben Abschnitts nur einmal geladen")
		void nachbarabschnittNurEinmalLaden() {
			final DTOKlassen klasseA = createKlasse(1L, ID_ABSCHNITT_2, "07a");
			klasseA.VKlasse = "06a";
			final DTOKlassen klasseB = createKlasse(2L, ID_ABSCHNITT_2, "07b");
			klasseB.VKlasse = "06b";
			stubAbschnitt(ID_ABSCHNITT_2, 2, ID_ABSCHNITT_1, null);
			when(klassenRepository.getListBySchuljahresabschnitt(ID_ABSCHNITT_2)).thenReturn(List.of(klasseA, klasseB));
			when(klassenRepository.getListBySchuljahresabschnitt(ID_ABSCHNITT_1))
					.thenReturn(List.of(createKlasse(50L, ID_ABSCHNITT_1, "07a"), createKlasse(51L, ID_ABSCHNITT_1, "07b")));
			when(klassenleitungenRepository.getMapKlassenleitungen(List.of(1L, 2L))).thenReturn(Map.of());

			final List<KlassenDaten> result = service.getListBySchuljahresabschnitt(ID_ABSCHNITT_2, false);

			assertThat(result).extracting(k -> k.idVorgaengerklasse).containsExactly(50L, 51L);
			verify(klassenRepository, times(1)).getListBySchuljahresabschnitt(ID_ABSCHNITT_1);
		}

	}

	@Nested
	@DisplayName("Fallbacks")
	class Fallbacks {

		@Test
		@DisplayName("Klassenart: Eine ungültige Klassenart wird durch die Regelklasse ersetzt")
		void klassenartFallback() {
			final DTOKlassen klasse = createKlasse(1L, ID_ABSCHNITT_1, "07a");
			klasse.Klassenart = "ungueltig";
			when(klassenRepository.findById(1L)).thenReturn(Optional.of(klasse));
			stubAbschnitt(ID_ABSCHNITT_1, 1, null, null);
			when(klassenleitungenRepository.getMapKlassenleitungen(List.of(1L))).thenReturn(Map.of());

			assertThat(service.getById(1L).idKlassenart).isEqualTo(Klassenart.RK.daten(SCHULJAHR).id);
		}

		@Test
		@DisplayName("Schulgliederung: Eine ungültige Schulgliederung wird durch die Default-Schulgliederung der Schulform ersetzt")
		void schulgliederungFallback() {
			final DTOKlassen klasse = createKlasse(1L, ID_ABSCHNITT_1, "07a");
			klasse.ASDSchulformNr = "ungueltig";
			when(klassenRepository.findById(1L)).thenReturn(Optional.of(klasse));
			stubAbschnitt(ID_ABSCHNITT_1, 1, null, null);
			when(klassenleitungenRepository.getMapKlassenleitungen(List.of(1L))).thenReturn(Map.of());

			assertThat(service.getById(1L).idSchulgliederung).isEqualTo(Schulgliederung.getDefault(Schulform.GY).daten(SCHULJAHR).id);
		}

	}

	@Nested
	@DisplayName("Klassenleitungen")
	class Klassenleitungen {

		@Test
		@DisplayName("Die Reihenfolge der Klassenleitungen aus dem Repository bleibt erhalten")
		void reihenfolge() {
			when(klassenRepository.findById(1L)).thenReturn(Optional.of(createKlasse(1L, ID_ABSCHNITT_1, "07a")));
			stubAbschnitt(ID_ABSCHNITT_1, 1, null, null);
			when(klassenleitungenRepository.getMapKlassenleitungen(List.of(1L))).thenReturn(Map.of(1L, List.of(7L, 5L, 6L)));

			assertThat(service.getById(1L).klassenLeitungen).containsExactly(7L, 5L, 6L);
		}

		@Test
		@DisplayName("Klasse ohne Klassenleitung: Die Liste ist leer, auch wenn die Klasse nicht in der Map enthalten ist")
		void klasseOhneLeitung() {
			stubAbschnitt(ID_ABSCHNITT_1, 1, null, null);
			when(klassenRepository.getListBySchuljahresabschnitt(ID_ABSCHNITT_1))
					.thenReturn(List.of(createKlasse(1L, ID_ABSCHNITT_1, "07a"), createKlasse(2L, ID_ABSCHNITT_1, "07b")));
			when(klassenleitungenRepository.getMapKlassenleitungen(List.of(1L, 2L))).thenReturn(Map.of(1L, List.of()));

			final List<KlassenDaten> result = service.getListBySchuljahresabschnitt(ID_ABSCHNITT_1, false);

			assertThat(result).isNotEmpty().allSatisfy(k -> assertThat(k.klassenLeitungen).isEmpty());
		}

	}

	@Nested
	@DisplayName("create")
	class Create {

		@Test
		@DisplayName("Pflichtattribute und Standardwerte werden gesetzt, die Daten der neuen Klasse werden geliefert")
		void createMitStandardwerten() {
			stubCreate();
			stubJahrgang("05");

			final var result = service.create(createRequest());

			assertThat(result.id).isEqualTo(ID_NEUE_KLASSE);
			assertThat(result.kuerzel).isEqualTo("05a");
			final var captor = ArgumentCaptor.forClass(DTOKlassen.class);
			verify(klassenRepository).create(captor.capture());
			assertThat(captor.getValue()).satisfies(dto -> {
				assertThat(dto.Klasse).isEqualTo("05a");
				assertThat(dto.Schuljahresabschnitts_ID).isEqualTo(ID_ABSCHNITT_1);
				assertThat(dto.Jahrgang_ID).isEqualTo(ID_JAHRGANG);
				assertThat(dto.ASDKlasse).isEqualTo("05");
				assertThat(dto.Sortierung).isZero();
				assertThat(dto.AdrMerkmal).isEqualTo("A");
				assertThat(dto.OrgFormKrz).isEqualTo(AllgemeinbildendOrganisationsformen.NICHT_ZUGEORDNET.daten(SCHULJAHR).kuerzel);
				assertThat(dto.ASDSchulformNr).isEqualTo(Schulgliederung.getDefault(Schulform.GY).daten(SCHULJAHR).kuerzel);
				assertThat(dto.Klassenart).isEqualTo(Klassenart.RK.daten(SCHULJAHR).kuerzel);
				assertThat(dto.istJva).isFalse();
				assertThat(dto.istReformpaedagogik).isFalse();
			});
		}

		@Test
		@DisplayName("Ein Jahrgang null legt eine jahrgangsübergreifende Klasse an (JU)")
		void jahrgangNull() {
			stubCreate();
			final var request = createRequest();
			request.idJahrgang = null;

			final var dto = createAndCapture(request);

			assertThat(dto.Jahrgang_ID).isNull();
			assertThat(dto.ASDKlasse).isEqualTo("JU");
		}

		@Test
		@DisplayName("Die Parallelität wird an das ASD-Kürzel angehängt, einfache Attribute werden übernommen")
		void optionaleAttribute() {
			stubCreate();
			stubJahrgang("05");
			final var request = createRequest();
			request.parallelitaet = "B";
			request.sortierung = 3;
			request.beschreibung = "Bilingual";
			request.noteneingabeGesperrt = true;
			request.istJva = true;

			final var dto = createAndCapture(request);

			assertThat(dto.ASDKlasse).isEqualTo("05B");
			assertThat(dto.Sortierung).isEqualTo(3);
			assertThat(dto.Bezeichnung).isEqualTo("Bilingual");
			assertThat(dto.NotenGesperrt).isTrue();
			assertThat(dto.istJva).isTrue();
		}

		@Test
		@DisplayName("Ein ungültiger Schuljahresabschnitt führt zu 400")
		void abschnittUngueltig() {
			when(schuljahresabschnittService.findById(ID_ABSCHNITT_1)).thenReturn(Optional.empty());

			assertApiError(() -> service.create(createRequest()), Response.Status.BAD_REQUEST);

			verify(klassenRepository, never()).create(any(DTOKlassen.class));
		}

		@Test
		@DisplayName("Ein bereits vergebenes Kürzel im Schuljahresabschnitt führt zu 400")
		void kuerzelDoppelt() {
			when(schuljahresabschnittService.findById(ID_ABSCHNITT_1)).thenReturn(Optional.of(createStandardAbschnitt()));
			when(klassenRepository.kuerzelIsAlreadyUsedCreate("05a", ID_ABSCHNITT_1)).thenReturn(true);

			assertApiError(() -> service.create(createRequest()), Response.Status.BAD_REQUEST);

			verify(klassenRepository, never()).create(any(DTOKlassen.class));
		}

		@Test
		@DisplayName("Ein unbekannter Jahrgang führt zu 404")
		void jahrgangUnbekannt() {
			when(schuljahresabschnittService.findById(ID_ABSCHNITT_1)).thenReturn(Optional.of(createStandardAbschnitt()));
			lenient().when(teilstandortRepository.findFirst()).thenReturn(Optional.of(new DTOTeilstandorte("A")));
			when(jahrgangRepository.findById(ID_JAHRGANG)).thenReturn(Optional.empty());

			assertApiError(() -> service.create(createRequest()), Response.Status.NOT_FOUND);

			verify(klassenRepository, never()).create(any(DTOKlassen.class));
		}

		@Test
		@DisplayName("Ohne vorhandenen Teilstandort führt das Anlegen zu 404")
		void teilstandortFehlt() {
			when(schuljahresabschnittService.findById(ID_ABSCHNITT_1)).thenReturn(Optional.of(createStandardAbschnitt()));
			lenient().when(teilstandortRepository.findFirst()).thenReturn(Optional.empty());

			assertApiError(() -> service.create(createRequest()), Response.Status.NOT_FOUND);

			verify(klassenRepository, never()).create(any(DTOKlassen.class));
		}

		@Test
		@DisplayName("Berufsbildende Schulform: Organisationsform Vollzeit, erste passende Schulgliederung, keine Klassenart")
		void defaultsBerufsbildend() {
			when(eigeneSchuleService.getSchulform()).thenReturn(Schulform.BK);
			stubCreate();
			stubJahrgang("05");

			final var dto = createAndCapture(createRequest());

			assertThat(dto.OrgFormKrz).isEqualTo(BerufskollegOrganisationsformen.VOLLZEIT.daten(SCHULJAHR).kuerzel);
			assertThat(dto.ASDSchulformNr).isEqualTo(Schulgliederung.getBySchuljahrAndSchulform(SCHULJAHR, Schulform.BK).getFirst().daten(SCHULJAHR).kuerzel);
			assertThat(dto.Klassenart).isNull();
		}

		@Test
		@DisplayName("Weiterbildungskolleg: Organisationsform Vollzeit")
		void defaultsWeiterbildung() {
			when(eigeneSchuleService.getSchulform()).thenReturn(Schulform.WB);
			stubCreate();
			stubJahrgang("05");

			final var dto = createAndCapture(createRequest());

			assertThat(dto.OrgFormKrz).isEqualTo(WeiterbildungskollegOrganisationsformen.VOLLZEIT.daten(SCHULJAHR).kuerzel);
			assertThat(dto.Klassenart).isEqualTo(Klassenart.RK.daten(SCHULJAHR).kuerzel);
		}

		@Test
		@DisplayName("Eine Organisationsform, die nicht zur Schulform passt, führt zu 400")
		void organisationsformPasstNichtZurSchulform() {
			stubCreate();
			stubJahrgang("05");
			final var request = createRequest();
			request.idBerufsbildendOrganisationsform = BerufskollegOrganisationsformen.VOLLZEIT.daten(SCHULJAHR).id;

			assertApiError(() -> service.create(request), Response.Status.BAD_REQUEST);

			verify(klassenRepository, never()).create(any(DTOKlassen.class));
		}

		@Test
		@DisplayName("Klassenart, Schulgliederung, Vorgänger- und Folgeklasse aus dem Request werden übernommen")
		void katalogattributeUndBeziehungen() {
			stubCreate();
			stubJahrgang("05");
			when(klassenRepository.findById(20L)).thenReturn(Optional.of(new DTOKlassen(20L, ID_ABSCHNITT_VORHER, "04a", false, false)));
			when(klassenRepository.findById(21L)).thenReturn(Optional.of(new DTOKlassen(21L, ID_ABSCHNITT_2, "06a", false, false)));
			final var request = createRequest();
			request.idKlassenart = Klassenart.RK.daten(SCHULJAHR).id;
			request.idSchulgliederung = KlassenMapper.KEINE_SCHULGLIEDERUNG;
			request.idVorgaengerklasse = 20L;
			request.idFolgeklasse = 21L;

			final var dto = createAndCapture(request);

			assertThat(dto)
					.extracting(d -> d.Klassenart, d -> d.ASDSchulformNr, d -> d.VKlasse, d -> d.FKlasse)
					.containsExactly(Klassenart.RK.daten(SCHULJAHR).kuerzel, Schulgliederung.getDefault(Schulform.GY).daten(SCHULJAHR).kuerzel, "04a", "06a");
		}

		@Test
		@DisplayName("Ein explizit gesetzter Teilstandort überschreibt den Standard-Teilstandort")
		void teilstandortGesetzt() {
			stubCreate();
			stubJahrgang("05");
			when(teilstandortRepository.existsById("B")).thenReturn(true);
			final var request = createRequest();
			request.teilstandort = "B";

			final var dto = createAndCapture(request);

			assertThat(dto.AdrMerkmal).isEqualTo("B");
		}
	}

	@Nested
	@DisplayName("patch")
	class Patch {

		private DTOKlassen klasse;

		@BeforeEach
		void setUpKlasse() {
			klasse = createStandardKlasse();
			lenient().when(klassenRepository.findById(ID_KLASSE)).thenReturn(Optional.of(klasse));
			lenient().when(schuljahresabschnittService.getById(ID_ABSCHNITT_1)).thenReturn(createStandardAbschnitt());
			lenient().doAnswer(invocation -> Map.of(ID_KLASSE,
					new KlassenMappingContext(eigeneSchuleService.getSchulform(), createStandardAbschnitt(), null, null, null, null)))
					.when(kontextLoader).load(anyList(), eq(true));
		}

		@Test
		@DisplayName("Ein leerer Patch ändert nichts, die Klasse wird gespeichert und mit ihren Daten geliefert")
		void leererPatch() {
			final var result = service.patch(ID_KLASSE, new KlassenPatchRequest());

			assertThat(result).satisfies(daten -> {
				assertThat(daten.id).isEqualTo(ID_KLASSE);
				assertThat(daten.kuerzel).isEqualTo("05a");
			});
			assertThat(klasse.Klasse).isEqualTo("05a");
			assertThat(klasse.ASDKlasse).isEqualTo("05A");
			verify(klassenRepository).update(klasse);
		}

		@Test
		@DisplayName("Alle einfachen Attribute werden übernommen, explizit null bei der Beschreibung wird gesetzt")
		void einfacheAttribute() {
			klasse.Bezeichnung = "alt";
			final var request = new KlassenPatchRequest();
			request.sortierung = JsonNullable.of(5);
			request.beschreibung = JsonNullable.of(null);
			request.noteneingabeGesperrt = JsonNullable.of(true);
			request.verwendungAnkreuzkompetenzen = JsonNullable.of(true);
			request.beginnSommersemester = JsonNullable.of(true);
			request.istJva = JsonNullable.of(true);
			request.istReformpaedagogik = JsonNullable.of(true);

			final var result = service.patch(ID_KLASSE, request);

			assertThat(result.sortierung).isEqualTo(5);
			assertThat(klasse).satisfies(k -> {
				assertThat(k.Sortierung).isEqualTo(5);
				assertThat(k.Bezeichnung).isNull();
				assertThat(k.NotenGesperrt).isTrue();
				assertThat(k.Ankreuzzeugnisse).isTrue();
				assertThat(k.SommerSem).isTrue();
				assertThat(k.istJva).isTrue();
				assertThat(k.istReformpaedagogik).isTrue();
			});
		}

		@Test
		@DisplayName("Das Kürzel wird geändert, die eigene Klasse zählt bei der Eindeutigkeit nicht als Duplikat")
		void kuerzelAendern() {
			final var request = new KlassenPatchRequest();
			request.kuerzel = JsonNullable.of("05b");

			service.patch(ID_KLASSE, request);

			assertThat(klasse.Klasse).isEqualTo("05b");
			verify(klassenRepository).kuerzelIsAlreadyUsedPatch("05b", ID_ABSCHNITT_1, ID_KLASSE);
		}

		@Test
		@DisplayName("Ein Kürzel, das bei einer anderen Klasse des Abschnitts vergeben ist, führt zu 400")
		void kuerzelDoppelt() {
			when(klassenRepository.kuerzelIsAlreadyUsedPatch("05b", ID_ABSCHNITT_1, ID_KLASSE)).thenReturn(true);
			final var request = new KlassenPatchRequest();
			request.kuerzel = JsonNullable.of("05b");

			assertApiError(() -> service.patch(ID_KLASSE, request), Response.Status.BAD_REQUEST);

			assertThat(klasse.Klasse).isEqualTo("05a");
			verify(klassenRepository, never()).update(any(DTOKlassen.class));
		}

		@Test
		@DisplayName("Der Jahrgang E1 wird zu 1E, die Parallelität bleibt erhalten")
		void jahrgangE1() {
			stubJahrgang("E1");
			final var request = new KlassenPatchRequest();
			request.idJahrgang = JsonNullable.of(ID_JAHRGANG);

			service.patch(ID_KLASSE, request);

			assertThat(klasse.ASDKlasse).isEqualTo("1EA");
			assertThat(klasse.Jahrgang_ID).isEqualTo(ID_JAHRGANG);
		}

		@Test
		@DisplayName("Der Jahrgang E2 wird zu 2E")
		void jahrgangE2() {
			stubJahrgang("E2");
			final var request = new KlassenPatchRequest();
			request.idJahrgang = JsonNullable.of(ID_JAHRGANG);

			service.patch(ID_KLASSE, request);

			assertThat(klasse.ASDKlasse).isEqualTo("2EA");
		}

		@Test
		@DisplayName("Ein Jahrgang null setzt den Jahrgangsteil JU, die Parallelität bleibt erhalten")
		void jahrgangNull() {
			final var request = new KlassenPatchRequest();
			request.idJahrgang = JsonNullable.of(null);

			service.patch(ID_KLASSE, request);

			assertThat(klasse.Jahrgang_ID).isNull();
			assertThat(klasse.ASDKlasse).isEqualTo("JUA");
		}

		@Test
		@DisplayName("Ein unbekannter Jahrgang führt zu 404")
		void jahrgangUnbekannt() {
			when(jahrgangRepository.findById(ID_JAHRGANG)).thenReturn(Optional.empty());
			final var request = new KlassenPatchRequest();
			request.idJahrgang = JsonNullable.of(ID_JAHRGANG);

			assertApiError(() -> service.patch(ID_KLASSE, request), Response.Status.NOT_FOUND);
		}

		@Test
		@DisplayName("Die Parallelität wird geändert bzw. mit null entfernt")
		void parallelitaet() {
			final var request = new KlassenPatchRequest();
			request.parallelitaet = JsonNullable.of("C");
			service.patch(ID_KLASSE, request);
			assertThat(klasse.ASDKlasse).isEqualTo("05C");

			request.parallelitaet = JsonNullable.of(null);
			service.patch(ID_KLASSE, request);
			assertThat(klasse.ASDKlasse).isEqualTo("05");
		}

		@Test
		@DisplayName("Teilstandort, Klassenart und Fachklasse werden mit null bzw. dem vorhandenen Wert gesetzt, ein unbekannter Teilstandort führt zu 404")
		void katalogattribute() {
			final var request = new KlassenPatchRequest();
			request.idKlassenart = JsonNullable.of(null);
			request.idFachklasse = JsonNullable.of(null);
			klasse.Klassenart = "RK";
			klasse.Fachklasse_ID = 5L;

			service.patch(ID_KLASSE, request);

			assertThat(klasse.Klassenart).isNull();
			assertThat(klasse.Fachklasse_ID).isNull();

			final var requestTeilstandort = new KlassenPatchRequest();
			requestTeilstandort.teilstandort = JsonNullable.of("Z");
			when(teilstandortRepository.existsById("Z")).thenReturn(false);

			assertApiError(() -> service.patch(ID_KLASSE, requestTeilstandort), Response.Status.NOT_FOUND);
		}

		@Test
		@DisplayName("Vorgänger- und Folgeklasse: das Kürzel wird gespeichert, bei falschem Abschnitt gibt es 400, bei unbekannter Klasse 404")
		void vorgaengerUndFolgeklasse() {
			final var vorgaenger = new DTOKlassen(20L, ID_ABSCHNITT_VORHER, "04a", false, false);
			final var folge = new DTOKlassen(21L, ID_ABSCHNITT_2, "06a", false, false);
			final var falsch = new DTOKlassen(22L, ID_ABSCHNITT_1, "05z", false, false);
			when(klassenRepository.findById(20L)).thenReturn(Optional.of(vorgaenger));
			when(klassenRepository.findById(21L)).thenReturn(Optional.of(folge));
			when(klassenRepository.findById(22L)).thenReturn(Optional.of(falsch));
			when(klassenRepository.findById(23L)).thenReturn(Optional.empty());

			final var request = new KlassenPatchRequest();
			request.idVorgaengerklasse = JsonNullable.of(20L);
			request.idFolgeklasse = JsonNullable.of(21L);
			service.patch(ID_KLASSE, request);
			assertThat(klasse.VKlasse).isEqualTo("04a");
			assertThat(klasse.FKlasse).isEqualTo("06a");

			final var requestFalsch = new KlassenPatchRequest();
			requestFalsch.idVorgaengerklasse = JsonNullable.of(22L);
			assertApiError(() -> service.patch(ID_KLASSE, requestFalsch), Response.Status.BAD_REQUEST);

			final var requestUnbekannt = new KlassenPatchRequest();
			requestUnbekannt.idFolgeklasse = JsonNullable.of(23L);
			assertApiError(() -> service.patch(ID_KLASSE, requestUnbekannt), Response.Status.NOT_FOUND);

			final var requestLeer = new KlassenPatchRequest();
			requestLeer.idVorgaengerklasse = JsonNullable.of(null);
			requestLeer.idFolgeklasse = JsonNullable.of(null);
			service.patch(ID_KLASSE, requestLeer);
			assertThat(klasse.VKlasse).isNull();
			assertThat(klasse.FKlasse).isNull();
		}

		@Test
		@DisplayName("Klassenleitungen werden dem KlassenleitungService übergeben")
		void klassenleitungen() {
			final var request = new KlassenPatchRequest();
			request.klassenLeitungen = JsonNullable.of(List.of(7L, 10L, 8L));

			service.patch(ID_KLASSE, request);

			verify(klassenleitungService).replace(ID_KLASSE, List.of(7L, 10L, 8L));
		}

		@Test
		@DisplayName("Ohne Klassenleitungen im Patch wird der KlassenleitungService nicht aufgerufen")
		void ohneKlassenleitungen() {
			service.patch(ID_KLASSE, new KlassenPatchRequest());

			verifyNoInteractions(klassenleitungService);
		}

		@Test
		@DisplayName("Ein Fehler des KlassenleitungService bricht das Ändern ab")
		void klassenleitungenFehler() {
			doThrow(new ApiOperationException(Response.Status.BAD_REQUEST, "Fehler")).when(klassenleitungService).replace(ID_KLASSE, List.of(7L, 7L));
			final var request = new KlassenPatchRequest();
			request.klassenLeitungen = JsonNullable.of(List.of(7L, 7L));

			assertApiError(() -> service.patch(ID_KLASSE, request), Response.Status.BAD_REQUEST, "Fehler");

			verify(klassenRepository, never()).update(any(DTOKlassen.class));
		}

		@Test
		@DisplayName("Ohne vorigen Schuljahresabschnitt führt eine Vorgängerklasse zu 400")
		void vorgaengerklasseOhneVorigenAbschnitt() {
			when(schuljahresabschnittService.getById(ID_ABSCHNITT_1)).thenReturn(createAbschnitt(ID_ABSCHNITT_1, 1, null, ID_ABSCHNITT_2));
			when(klassenRepository.findById(20L)).thenReturn(Optional.of(new DTOKlassen(20L, ID_ABSCHNITT_VORHER, "04a", false, false)));
			final var request = new KlassenPatchRequest();
			request.idVorgaengerklasse = JsonNullable.of(20L);

			assertApiError(() -> service.patch(ID_KLASSE, request), Response.Status.BAD_REQUEST);
		}

		@Test
		@DisplayName("Eine unbekannte Klasse führt zu 404, eine ID null zu 400")
		void klasseNichtGefunden() {
			when(klassenRepository.findById(2L)).thenReturn(Optional.empty());

			assertApiError(() -> service.patch(2L, new KlassenPatchRequest()), Response.Status.NOT_FOUND);
			assertApiError(() -> service.patch(null, new KlassenPatchRequest()), Response.Status.BAD_REQUEST);
		}
	}

	@Nested
	@DisplayName("delete")
	class Delete {

		@Test
		@DisplayName("Klassen ohne Schüler werden gelöscht")
		void klassenOhneSchueler() {
			final var klasse1 = createKlasse(1L, ID_ABSCHNITT_1, "05a");
			final var klasse2 = createKlasse(2L, ID_ABSCHNITT_1, "05b");
			when(klassenRepository.findListByIds(List.of(1L, 2L))).thenReturn(List.of(klasse1, klasse2));

			final var result = service.delete(List.of(1L, 2L));

			assertThat(result)
					.extracting(r -> r.id, r -> r.success)
					.containsExactly(tuple(1L, true), tuple(2L, true));
			verify(klassenRepository).delete(klasse1);
			verify(klassenRepository).delete(klasse2);
		}

		@Test
		@DisplayName("Eine Klasse mit Schülern wird nicht gelöscht, die übrigen Klassen schon")
		void klasseMitSchuelern() {
			final var mitSchuelern = createKlasse(1L, ID_ABSCHNITT_1, "05a");
			final var ohneSchueler = createKlasse(2L, ID_ABSCHNITT_1, "05b");
			when(klassenRepository.findListByIds(List.of(1L, 2L))).thenReturn(List.of(mitSchuelern, ohneSchueler));
			when(lernabschnittRepository.getMapAnzahlSchuelerAktuell(anyCollection())).thenReturn(Map.of(1L, 2L));

			final var result = service.delete(List.of(1L, 2L));

			assertThat(result)
					.extracting(r -> r.id, r -> r.success)
					.containsExactly(tuple(1L, false), tuple(2L, true));
			assertThat(result.getFirst().log).containsExactly("Klasse 05a (ID: 1) hat noch 2 verknüpfte(n) Schüler.");
			verify(klassenRepository, never()).delete(mitSchuelern);
			verify(klassenRepository).delete(ohneSchueler);
			verify(lernabschnittRepository, times(1)).getMapAnzahlSchuelerAktuell(anyCollection());
			verifyNoInteractions(schuelerRepository);
		}

		@Test
		@DisplayName("Nicht gefundene IDs werden als Fehler zurückgegeben, doppelte IDs nur einmal und null gar nicht berücksichtigt")
		void nichtGefundeneIds() {
			final var klasse = createKlasse(1L, ID_ABSCHNITT_1, "05a");
			when(klassenRepository.findListByIds(List.of(1L, 99L))).thenReturn(List.of(klasse));

			final var result = service.delete(Arrays.asList(1L, null, 99L, 1L));

			assertThat(result)
					.extracting(r -> r.id, r -> r.success)
					.containsExactly(tuple(1L, true), tuple(99L, false));
			assertThat(result.getLast().log).containsExactly("Keine Klasse zur ID 99 gefunden.");
		}

		@Test
		@DisplayName("Werden keine Klassen gefunden, wird mit 404 abgelehnt")
		void keineKlasseGefunden() {
			when(klassenRepository.findListByIds(List.of(99L))).thenReturn(List.of());

			assertApiError(() -> service.delete(List.of(99L)), Response.Status.NOT_FOUND);

			verify(klassenRepository, never()).delete(any(DTOKlassen.class));
		}

		@Test
		@DisplayName("Ein Fehler beim Löschen wird als Fehlerergebnis der Klasse zurückgegeben")
		void fehlerBeimLoeschen() {
			final var klasse = createKlasse(1L, ID_ABSCHNITT_1, "05a");
			when(klassenRepository.findListByIds(List.of(1L))).thenReturn(List.of(klasse));
			doThrow(new RepositoryException("Fehler beim Löschen")).when(klassenRepository).delete(klasse);

			final var result = service.delete(List.of(1L));

			assertThat(result)
					.singleElement()
					.satisfies(r -> assertThat(r.success).isFalse())
					.satisfies(r -> assertThat(r.log).containsExactly("Fehler beim Löschen"));
		}

		@Test
		@DisplayName("Eine leere Liste liefert eine leere Liste, ohne auf die Datenbank zuzugreifen")
		void leereListe() {
			assertThat(service.delete(List.of())).isEmpty();

			verifyNoInteractions(klassenRepository, lernabschnittRepository, schuelerRepository);
		}

		@Test
		@DisplayName("Eine Liste null führt zu 400")
		void listeNull() {
			assertApiError(() -> service.delete(null), Response.Status.BAD_REQUEST);

			verifyNoInteractions(klassenRepository, lernabschnittRepository, schuelerRepository);
		}
	}

	@Nested
	@DisplayName("setDefaultSortierung")
	class SetDefaultSortierung {

		@Test
		@DisplayName("Die Sortierung wird an den KlassenDefaultSorter delegiert, die Details prüft KlassenDefaultSorterTest")
		void delegiertAnSorter() {
			final var klasse = createKlasse(ID_KLASSE, ID_ABSCHNITT_1, "5a");
			klasse.Jahrgang_ID = ID_JAHRGANG;
			klasse.ASDKlasse = "05A";
			final var jahrgang = new DTOJahrgang(ID_JAHRGANG);
			jahrgang.Sortierung = 1;
			when(jahrgangRepository.getMap()).thenReturn(Map.of(ID_JAHRGANG, jahrgang));
			when(klassenRepository.getListBySchuljahresabschnitt(ID_ABSCHNITT_1)).thenReturn(List.of(klasse));

			service.setDefaultSortierung(ID_ABSCHNITT_1);

			assertThat(klasse.Sortierung).isEqualTo(1);
			verify(klassenRepository).update(ArgumentMatchers.<List<DTOKlassen>>any());
		}
	}

}
