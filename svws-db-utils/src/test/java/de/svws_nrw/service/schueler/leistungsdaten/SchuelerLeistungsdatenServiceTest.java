package de.svws_nrw.service.schueler.leistungsdaten;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schueler.leistungsdaten.SchuelerLeistungsdatenMapper;
import de.svws_nrw.repo.benutzer.BenutzerAllgemeinRepository;
import de.svws_nrw.repo.kurse.KurseRepository;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import de.svws_nrw.repo.schueler.leistungsdaten.SchuelerLeistungsdatenRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import de.svws_nrw.repo.schule.kataloge.fach.FachRepository;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchuelerLeistungsdatenServiceTest {

	private static final long ID_LEISTUNG = 1L;
	private static final long ID_LERNABSCHNITT = 10L;
	private static final long ID_KLASSE = 100L;
	private static final long ID_SCHULJAHRESABSCHNITT = 20L;
	private static final int SCHULJAHR = 2024;
	private static final long ID_FACH = 5L;
	private static final int SORTIERUNG_FACH = 3;
	private static final long ID_KURS = 30L;
	private static final long ID_KURSLEHRER = 40L;
	private static final long ID_FACHLEHRER = 41L;

	@Mock
	private SchuelerLeistungsdatenRepository repository;

	@Mock
	private SchuelerLernabschnittRepository lernabschnittRepository;

	@Mock
	private SchuljahresabschnitteRepository schuljahresabschnitteRepository;

	@Mock
	private FachRepository fachRepository;

	@Mock
	private KurseRepository kurseRepository;

	@Mock
	private LehrerRepository lehrerRepository;

	@Mock
	private EigeneSchuleService eigeneSchuleService;

	@Mock
	private BenutzerKompetenzService benutzerKompetenzService;

	private SchuelerLeistungsdatenRepositories repos;
	private SchuelerLeistungsdatenService service;
	private MockedStatic<TransactionSupport> transactionSupport;

	private DTOSchuelerLeistungsdaten entity;
	private DTOSchuelerLernabschnittsdaten lernabschnitt;

	@BeforeAll
	static void init() {
		ASDCoreTypeUtils.initAll();
	}

	@BeforeEach
	void setUp() {
		// führt den Task direkt aus, ohne echte Datenbank-Transaktion
		transactionSupport = mockStatic(TransactionSupport.class);
		transactionSupport.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());

		repos = new SchuelerLeistungsdatenRepositories(repository, lernabschnittRepository, schuljahresabschnitteRepository, fachRepository, kurseRepository,
				lehrerRepository);
		service = new SchuelerLeistungsdatenService(repos, SchuelerLeistungsdatenMapper.INSTANCE, eigeneSchuleService, benutzerKompetenzService);

		entity = new DTOSchuelerLeistungsdaten(ID_LEISTUNG, ID_LERNABSCHNITT, ID_FACH);
		entity.Fachlehrer_ID = ID_FACHLEHRER;
		lernabschnitt = createLernabschnitt(ID_LERNABSCHNITT, ID_SCHULJAHRESABSCHNITT, ID_KLASSE);

		lenient().when(repository.findById(ID_LEISTUNG)).thenReturn(Optional.of(entity));
		lenient().when(lernabschnittRepository.findById(ID_LERNABSCHNITT)).thenReturn(Optional.of(lernabschnitt));
		lenient().when(lernabschnittRepository.findListByIds(anyCollection())).thenReturn(List.of(lernabschnitt));
		lenient().when(schuljahresabschnitteRepository.findListByIds(anyCollection()))
				.thenReturn(List.of(new DTOSchuljahresabschnitte(ID_SCHULJAHRESABSCHNITT, SCHULJAHR, 1)));
		lenient().when(schuljahresabschnitteRepository.findById(ID_SCHULJAHRESABSCHNITT))
				.thenReturn(Optional.of(new DTOSchuljahresabschnitte(ID_SCHULJAHRESABSCHNITT, SCHULJAHR, 1)));
		lenient().when(eigeneSchuleService.getIdSchuljahresabschnitt()).thenReturn(ID_SCHULJAHRESABSCHNITT);
		lenient().when(fachRepository.findById(ID_FACH)).thenReturn(Optional.of(createFach(ID_FACH)));
		lenient().when(kurseRepository.findById(ID_KURS)).thenReturn(Optional.of(createKurs("LK")));
		lenient().when(lehrerRepository.existsByIds(List.of(ID_KURSLEHRER))).thenReturn(Set.of(ID_KURSLEHRER));
		lenient().when(repository.create(any(DTOSchuelerLeistungsdaten.class))).thenAnswer(invocation -> invocation.getArgument(0));
	}

	@AfterEach
	void tearDown() {
		transactionSupport.close();
	}

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private static DTOSchuelerLernabschnittsdaten createLernabschnitt(final long id, final long idSchuljahresabschnitt, final Long idKlasse) {
		final var daten = new DTOSchuelerLernabschnittsdaten(id, 99L, idSchuljahresabschnitt, true, null, 1L);
		daten.Klassen_ID = idKlasse;
		return daten;
	}

	/**
	 * Die Kursart-Fälle: allgemeine Kursart des Kurses, Abiturfach im Request, erwartete Kursart und erwartetes Abiturfach.
	 *
	 * @return die Argumente für die parametrisierten Tests
	 */
	static Stream<Arguments> kursartFaelle() {
		return Stream.of(
				// Sonderfälle der Gesamtschule
				Arguments.of("E", null, "E", null),
				Arguments.of("G", null, "G", null),
				Arguments.of("DK", null, "G", null),
				// Grundkurse berücksichtigen die Abiturfächer 3 und 4, die Abiturfächer 1 und 2 werden entfernt
				Arguments.of("GK", null, "GKM", null),
				Arguments.of("GK", 1, "GKM", null),
				Arguments.of("GK", 3, "AB3", "3"),
				Arguments.of("GK", 4, "AB4", "4"),
				// Leistungskurse berücksichtigen das Abiturfach 2, ohne Abiturfach wird 1 gesetzt
				Arguments.of("LK", null, "LK1", "1"),
				Arguments.of("LK", 2, "LK2", "2"),
				// Alle übrigen allgemeinen Kursarten verwenden die erste zulässige Kursart
				Arguments.of("SF", null, "SF", null),
				Arguments.of("XYZ", null, null, null));
	}

	private static DTOFach createFach(final long id) {
		final var fach = new DTOFach(id, true);
		fach.SortierungAllg = SORTIERUNG_FACH;
		return fach;
	}

	private static DTOKurs createKurs(final String kursartAllg) {
		final var kurs = new DTOKurs(ID_KURS, ID_SCHULJAHRESABSCHNITT, "D-LK1", ID_FACH);
		kurs.KursartAllg = kursartAllg;
		kurs.Lehrer_ID = ID_KURSLEHRER;
		return kurs;
	}

	static Stream<Arguments> andereAttribute() {
		return Stream.of(
				Arguments.of("lernabschnittID", (Consumer<SchuelerLeistungsdatenPatchRequest>) r -> r.lernabschnittID = JsonNullable.of(1L)),
				Arguments.of("fachID", (Consumer<SchuelerLeistungsdatenPatchRequest>) r -> r.fachID = JsonNullable.of(1L)),
				Arguments.of("kursID", (Consumer<SchuelerLeistungsdatenPatchRequest>) r -> r.kursID = JsonNullable.of(1L)),
				Arguments.of("kursart", (Consumer<SchuelerLeistungsdatenPatchRequest>) r -> r.kursart = JsonNullable.of("LK1")),
				Arguments.of("abifach", (Consumer<SchuelerLeistungsdatenPatchRequest>) r -> r.abifach = JsonNullable.of(1)),
				Arguments.of("istZP10oderZKEF", (Consumer<SchuelerLeistungsdatenPatchRequest>) r -> r.istZP10oderZKEF = JsonNullable.of(true)),
				Arguments.of("koopSchule", (Consumer<SchuelerLeistungsdatenPatchRequest>) r -> r.koopSchule = JsonNullable.of(100000)),
				Arguments.of("lehrerID", (Consumer<SchuelerLeistungsdatenPatchRequest>) r -> r.lehrerID = JsonNullable.of(1L)),
				Arguments.of("wochenstunden", (Consumer<SchuelerLeistungsdatenPatchRequest>) r -> r.wochenstunden = JsonNullable.of(1)),
				Arguments.of("zusatzkraftID", (Consumer<SchuelerLeistungsdatenPatchRequest>) r -> r.zusatzkraftID = JsonNullable.of(1L)),
				Arguments.of("zusatzkraftWochenstunden", (Consumer<SchuelerLeistungsdatenPatchRequest>) r -> r.zusatzkraftWochenstunden = JsonNullable.of(1)),
				Arguments.of("aufZeugnis", (Consumer<SchuelerLeistungsdatenPatchRequest>) r -> r.aufZeugnis = JsonNullable.of(true)),
				Arguments.of("istGemahnt", (Consumer<SchuelerLeistungsdatenPatchRequest>) r -> r.istGemahnt = JsonNullable.of(true)),
				Arguments.of("mahndatum", (Consumer<SchuelerLeistungsdatenPatchRequest>) r -> r.mahndatum = JsonNullable.of("2025-01-20")),
				Arguments.of("istEpochal", (Consumer<SchuelerLeistungsdatenPatchRequest>) r -> r.istEpochal = JsonNullable.of(true)),
				Arguments.of("geholtJahrgangAbgeschlossen",
						(Consumer<SchuelerLeistungsdatenPatchRequest>) r -> r.geholtJahrgangAbgeschlossen = JsonNullable.of("EF")),
				Arguments.of("gewichtungAllgemeinbildend",
						(Consumer<SchuelerLeistungsdatenPatchRequest>) r -> r.gewichtungAllgemeinbildend = JsonNullable.of(1)),
				Arguments.of("textFachbezogeneLernentwicklung",
						(Consumer<SchuelerLeistungsdatenPatchRequest>) r -> r.textFachbezogeneLernentwicklung = JsonNullable.of("Text")),
				Arguments.of("umfangLernstandsbericht",
						(Consumer<SchuelerLeistungsdatenPatchRequest>) r -> r.umfangLernstandsbericht = JsonNullable.of("V"))
		);
	}

	private static SchuelerLeistungsdatenCreateRequest createCreateRequest() {
		final var request = new SchuelerLeistungsdatenCreateRequest();
		request.lernabschnittID = ID_LERNABSCHNITT;
		request.fachID = ID_FACH;
		return request;
	}

	private DTOSchuelerLeistungsdaten captureCreated() {
		final var captor = ArgumentCaptor.forClass(DTOSchuelerLeistungsdaten.class);
		verify(repository).create(captor.capture());
		return captor.getValue();
	}

	private static void assertApiException(final Runnable aufruf, final Response.Status status) {
		assertThatThrownBy(aufruf::run)
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", status);
	}

	// -------------------------------------------------------------------------
	// getById
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getById")
	class GetById {

		@Test
		@DisplayName("Leistungsdaten nicht gefunden - NOT_FOUND")
		void notFound() {
			when(repository.findById(99L)).thenReturn(Optional.empty());

			assertThatThrownBy(() -> service.getById(99L))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND)
					.hasMessage("Die Leistungsdaten mit der ID 99 wurden in der Datenbank nicht gefunden.");
		}

		@Test
		@DisplayName("Leistungsdaten gefunden - werden mit dem Schuljahr des Lernabschnitts gemappt")
		void found() {
			entity.NotenKrz = "2+";

			final var result = service.getById(ID_LEISTUNG);

			assertThat(result)
					.hasFieldOrPropertyWithValue("id", ID_LEISTUNG)
					.hasFieldOrPropertyWithValue("lernabschnittID", ID_LERNABSCHNITT)
					.hasFieldOrPropertyWithValue("fachID", ID_FACH)
					.hasFieldOrPropertyWithValue("lehrerID", ID_FACHLEHRER)
					.hasFieldOrPropertyWithValue("note", "2+")
					.hasFieldOrPropertyWithValue("kursart", "PUK");
		}

		@Test
		@DisplayName("Zum Lernabschnitt existiert kein Schuljahresabschnitt - INTERNAL_SERVER_ERROR")
		void keinSchuljahresabschnitt() {
			when(schuljahresabschnitteRepository.findListByIds(anyCollection())).thenReturn(List.of());

			assertThatThrownBy(() -> service.getById(ID_LEISTUNG))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.INTERNAL_SERVER_ERROR)
					.hasMessage("Zum Lernabschnitt mit der ID 10 wurde kein Schuljahresabschnitt gefunden.");
		}
	}

	// -------------------------------------------------------------------------
	// getListByLernabschnitte
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getListByLernabschnitte")
	class GetListByLernabschnitte {

		@Test
		@DisplayName("Keine IDs - leere Liste ohne Datenbankzugriff")
		void keineIds() {
			assertThat(service.getListByLernabschnitte(List.of())).isEmpty();

			verifyNoInteractions(repository, lernabschnittRepository, schuljahresabschnitteRepository);
		}

		@Test
		@DisplayName("Keine Leistungsdaten - leere Liste")
		void keineLeistungsdaten() {
			when(repository.findListByLernabschnitt(List.of(ID_LERNABSCHNITT))).thenReturn(List.of());

			assertThat(service.getListByLernabschnitte(List.of(ID_LERNABSCHNITT))).isEmpty();
		}

		@Test
		@DisplayName("Das Schuljahr wird für alle Lernabschnitte gebündelt bestimmt")
		void schuljahrGebuendelt() {
			final var zweiterEntity = new DTOSchuelerLeistungsdaten(2L, 11L, ID_FACH);
			final var dritterEntity = new DTOSchuelerLeistungsdaten(3L, ID_LERNABSCHNITT, ID_FACH);
			final var zweiterLernabschnitt = createLernabschnitt(11L, 21L, ID_KLASSE);
			when(repository.findListByLernabschnitt(List.of(ID_LERNABSCHNITT, 11L))).thenReturn(List.of(entity, zweiterEntity, dritterEntity));
			when(lernabschnittRepository.findListByIds(List.of(ID_LERNABSCHNITT, 11L))).thenReturn(List.of(lernabschnitt, zweiterLernabschnitt));
			when(schuljahresabschnitteRepository.findListByIds(List.of(ID_SCHULJAHRESABSCHNITT, 21L))).thenReturn(List.of(
					new DTOSchuljahresabschnitte(ID_SCHULJAHRESABSCHNITT, SCHULJAHR, 1),
					new DTOSchuljahresabschnitte(21L, SCHULJAHR, 2)));

			final var result = service.getListByLernabschnitte(List.of(ID_LERNABSCHNITT, 11L));

			assertThat(result)
					.extracting(l -> l.id)
					.containsExactly(ID_LEISTUNG, 2L, 3L);
			verify(lernabschnittRepository, times(1)).findListByIds(anyCollection());
			verify(schuljahresabschnitteRepository, times(1)).findListByIds(anyCollection());
		}
	}

	// -------------------------------------------------------------------------
	// create
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("create")
	class Create {

		@Test
		@DisplayName("Lernabschnitt nicht vorhanden - CONFLICT")
		void lernabschnittNichtVorhanden() {
			final var request = createCreateRequest();
			request.lernabschnittID = 99L;
			when(lernabschnittRepository.findById(99L)).thenReturn(Optional.empty());

			assertApiException(() -> service.create(request), Response.Status.CONFLICT);

			verify(repository, never()).create(any(DTOSchuelerLeistungsdaten.class));
		}

		@Test
		@DisplayName("Fach nicht vorhanden - CONFLICT")
		void fachNichtVorhanden() {
			final var request = createCreateRequest();
			request.fachID = 99L;
			when(fachRepository.findById(99L)).thenReturn(Optional.empty());

			assertApiException(() -> service.create(request), Response.Status.CONFLICT);

			verify(repository, never()).create(any(DTOSchuelerLeistungsdaten.class));
		}

		@Test
		@DisplayName("Keine Kompetenz für die Klasse des Lernabschnitts - FORBIDDEN, es wird nichts angelegt")
		void keineKompetenz() {
			doThrow(new ApiOperationException(Response.Status.FORBIDDEN, "verboten"))
					.when(benutzerKompetenzService).pruefeKompetenzLeistungsdatenAendernKlassen(ID_KLASSE);

			assertApiException(() -> service.create(createCreateRequest()), Response.Status.FORBIDDEN);

			verify(repository, never()).create(any(DTOSchuelerLeistungsdaten.class));
		}

		@Test
		@DisplayName("Nur Pflichtfelder - Sortierung kommt vom Fach, der Kurs bleibt leer")
		void nurPflichtfelder() {
			final var result = service.create(createCreateRequest());

			verify(benutzerKompetenzService).pruefeKompetenzLeistungsdatenAendernKlassen(ID_KLASSE);
			assertThat(captureCreated())
					.hasFieldOrPropertyWithValue("Abschnitt_ID", ID_LERNABSCHNITT)
					.hasFieldOrPropertyWithValue("Fach_ID", ID_FACH)
					.hasFieldOrPropertyWithValue("Sortierung", SORTIERUNG_FACH)
					.hasFieldOrPropertyWithValue("Kurs_ID", null)
					.hasFieldOrPropertyWithValue("Kursart", null);
			assertThat(result)
					.hasFieldOrPropertyWithValue("lernabschnittID", ID_LERNABSCHNITT)
					.hasFieldOrPropertyWithValue("fachID", ID_FACH);
		}

		@Test
		@DisplayName("Weitere Felder werden übernommen")
		void weitereFelder() {
			final var request = createCreateRequest();
			request.note = "2+";
			request.wochenstunden = 4;
			request.lehrerID = ID_KURSLEHRER;
			request.zusatzkraftID = ID_KURSLEHRER;
			request.abifach = 3;
			request.umfangLernstandsbericht = "V";

			service.create(request);

			assertThat(captureCreated())
					.hasFieldOrPropertyWithValue("NotenKrz", "2+")
					.hasFieldOrPropertyWithValue("Wochenstunden", 4)
					.hasFieldOrPropertyWithValue("Fachlehrer_ID", ID_KURSLEHRER)
					.hasFieldOrPropertyWithValue("Zusatzkraft_ID", ID_KURSLEHRER)
					.hasFieldOrPropertyWithValue("AbiFach", "3")
					.hasFieldOrPropertyWithValue("Umfang", "V");
		}

		@Test
		@DisplayName("Ein Kurs setzt Fachlehrer und Kursart, das Abiturfach wird berücksichtigt")
		void kursSetztFachlehrerUndKursart() {
			final var request = createCreateRequest();
			request.kursID = ID_KURS;
			request.abifach = 2;

			service.create(request);

			assertThat(captureCreated())
					.hasFieldOrPropertyWithValue("Kurs_ID", ID_KURS)
					.hasFieldOrPropertyWithValue("Fachlehrer_ID", ID_KURSLEHRER)
					.hasFieldOrPropertyWithValue("Kursart", "LK2")
					.hasFieldOrPropertyWithValue("KursartAllg", "LK")
					.hasFieldOrPropertyWithValue("AbiFach", "2");
		}

		@ParameterizedTest(name = "Kurs {0}, Abiturfach {1} -> Kursart {2}, Abiturfach {3}")
		@MethodSource("de.svws_nrw.service.schueler.leistungsdaten.SchuelerLeistungsdatenServiceTest#kursartFaelle")
		@DisplayName("Kursart und Abiturfach werden aus dem Kurs bestimmt")
		void kursartAusKurs(final String kursartAllg, final Integer abifach, final String erwarteteKursart, final String erwartetesAbifach) {
			when(kurseRepository.findById(ID_KURS)).thenReturn(Optional.of(createKurs(kursartAllg)));
			final var request = createCreateRequest();
			request.kursID = ID_KURS;
			request.abifach = abifach;

			service.create(request);

			assertThat(captureCreated())
					.hasFieldOrPropertyWithValue("Kursart", erwarteteKursart)
					.hasFieldOrPropertyWithValue("AbiFach", erwartetesAbifach);
		}

		@Test
		@DisplayName("Eine gesendete Kursart gewinnt gegenüber der Kursart des Kurses")
		void gesendeteKursartGewinnt() {
			final var request = createCreateRequest();
			request.kursID = ID_KURS;
			request.kursart = "GKM";

			service.create(request);

			assertThat(captureCreated())
					.hasFieldOrPropertyWithValue("Kurs_ID", ID_KURS)
					.hasFieldOrPropertyWithValue("Kursart", "GKM")
					.hasFieldOrPropertyWithValue("KursartAllg", "GK");
		}

		@Test
		@DisplayName("Ohne Kursart und Kurs bleiben Kursart und allgemeine Kursart leer")
		void ohneKursartBleibenKursartenLeer() {
			service.create(createCreateRequest());

			assertThat(captureCreated())
					.hasFieldOrPropertyWithValue("Kursart", null)
					.hasFieldOrPropertyWithValue("KursartAllg", null);
		}

		@Test
		@DisplayName("Ein Kurs null lässt den Fachlehrer unverändert")
		void kursNullLaesstFachlehrerUnveraendert() {
			final var request = createCreateRequest();
			request.lehrerID = ID_KURSLEHRER;
			request.kursID = null;

			service.create(request);

			assertThat(captureCreated())
					.hasFieldOrPropertyWithValue("Kurs_ID", null)
					.hasFieldOrPropertyWithValue("Fachlehrer_ID", ID_KURSLEHRER);
		}

		private void assertValidierungSchlaegtFehl(final SchuelerLeistungsdatenCreateRequest request, final Response.Status status) {
			assertApiException(() -> service.create(request), status);

			verify(repository, never()).create(any(DTOSchuelerLeistungsdaten.class));
		}

		@Test
		@DisplayName("Kurs nicht vorhanden - CONFLICT")
		void kursNichtVorhanden() {
			final var request = createCreateRequest();
			request.kursID = 99L;
			when(kurseRepository.findById(99L)).thenReturn(Optional.empty());

			assertValidierungSchlaegtFehl(request, Response.Status.CONFLICT);
		}

		@Test
		@DisplayName("Fachlehrer nicht vorhanden - CONFLICT")
		void lehrerNichtVorhanden() {
			final var request = createCreateRequest();
			request.lehrerID = 99L;
			when(lehrerRepository.existsByIds(List.of(99L))).thenReturn(Set.of());

			assertValidierungSchlaegtFehl(request, Response.Status.CONFLICT);
		}

		@Test
		@DisplayName("Zusatzkraft nicht vorhanden - CONFLICT")
		void zusatzkraftNichtVorhanden() {
			final var request = createCreateRequest();
			request.zusatzkraftID = 99L;
			when(lehrerRepository.existsByIds(List.of(99L))).thenReturn(Set.of());

			assertValidierungSchlaegtFehl(request, Response.Status.CONFLICT);
		}

		@Test
		@DisplayName("Unbekannte Kursart - BAD_REQUEST")
		void unbekannteKursart() {
			final var request = createCreateRequest();
			request.kursart = "XYZ";

			assertValidierungSchlaegtFehl(request, Response.Status.BAD_REQUEST);
		}

		@Test
		@DisplayName("Ungültige Werte werden vor den Abfragen auf Fach, Kurs und Lehrer abgelehnt")
		void ungueltigeWerteVorDatenbankabfragen() {
			final var request = createCreateRequest();
			request.kursart = "XYZ";
			request.kursID = ID_KURS;
			request.lehrerID = ID_KURSLEHRER;

			assertValidierungSchlaegtFehl(request, Response.Status.BAD_REQUEST);

			verifyNoInteractions(fachRepository, kurseRepository, lehrerRepository);
		}

		@Test
		@DisplayName("Fachlehrer und Zusatzkraft werden mit einer Abfrage geprüft")
		void lehrerMitEinerAbfragePruefen() {
			final var request = createCreateRequest();
			request.lehrerID = ID_KURSLEHRER;
			request.zusatzkraftID = 42L;
			when(lehrerRepository.existsByIds(List.of(ID_KURSLEHRER, 42L))).thenReturn(Set.of(ID_KURSLEHRER, 42L));

			service.create(request);

			verify(lehrerRepository, times(1)).existsByIds(anyCollection());
		}

		@Test
		@DisplayName("Ohne Lehrer im Request wird keine Lehrer-ID geprüft")
		void ohneLehrerKeinePruefung() {
			service.create(createCreateRequest());

			verify(lehrerRepository).existsByIds(List.of());
		}

		@ParameterizedTest(name = "Abiturfach {0} - BAD_REQUEST")
		@ValueSource(ints = { 0, 6, -1 })
		void ungueltigesAbifach(final int abifach) {
			final var request = createCreateRequest();
			request.abifach = abifach;

			assertValidierungSchlaegtFehl(request, Response.Status.BAD_REQUEST);
		}

		@ParameterizedTest(name = "Notenkürzel \"{0}\" - BAD_REQUEST")
		@ValueSource(strings = { "7", "xyz", "1++" })
		void ungueltigeNote(final String note) {
			final var request = createCreateRequest();
			request.note = note;

			assertValidierungSchlaegtFehl(request, Response.Status.BAD_REQUEST);
		}

		@Test
		@DisplayName("Ungültiges Quartalsnotenkürzel - BAD_REQUEST")
		void ungueltigeNoteQuartal() {
			final var request = createCreateRequest();
			request.noteQuartal = "xyz";

			assertValidierungSchlaegtFehl(request, Response.Status.BAD_REQUEST);
		}

		@ParameterizedTest(name = "Notenkürzel \"{0}\" ist gültig")
		@ValueSource(strings = { "", "1", "2-", "3+" })
		void gueltigeNote(final String note) {
			final var request = createCreateRequest();
			request.note = note;

			assertThatCode(() -> service.create(request)).doesNotThrowAnyException();
		}

		@Test
		@DisplayName("Ungültiger Umfang des Lernstandsberichts - BAD_REQUEST")
		void ungueltigerUmfang() {
			final var request = createCreateRequest();
			request.umfangLernstandsbericht = "X";

			assertValidierungSchlaegtFehl(request, Response.Status.BAD_REQUEST);
		}

		@ParameterizedTest(name = "Umfang \"{0}\" ist gültig")
		@ValueSource(strings = { "V", "R", "", " " })
		void gueltigerUmfang(final String umfang) {
			final var request = createCreateRequest();
			request.umfangLernstandsbericht = umfang;

			assertThatCode(() -> service.create(request)).doesNotThrowAnyException();
		}
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("patch")
	class Patch {

		@Test
		@DisplayName("Leistungsdaten nicht gefunden - NOT_FOUND")
		void notFound() {
			when(repository.findById(99L)).thenReturn(Optional.empty());

			assertApiException(() -> service.patch(99L, new SchuelerLeistungsdatenPatchRequest()), Response.Status.NOT_FOUND);
		}

		@Test
		@DisplayName("Leerer Patch ändert nichts")
		void leererPatch() {
			final var result = service.patch(ID_LEISTUNG, new SchuelerLeistungsdatenPatchRequest());

			assertThat(result).hasFieldOrPropertyWithValue("id", ID_LEISTUNG);
			assertThat(entity)
					.hasFieldOrPropertyWithValue("Abschnitt_ID", ID_LERNABSCHNITT)
					.hasFieldOrPropertyWithValue("Fach_ID", ID_FACH)
					.hasFieldOrPropertyWithValue("Fachlehrer_ID", ID_FACHLEHRER);
		}

		@Test
		@DisplayName("Gibt die aktualisierten Leistungsdaten zurück")
		void gibtAktualisierteLeistungsdatenZurueck() {
			final var request = new SchuelerLeistungsdatenPatchRequest();
			request.note = JsonNullable.of("2-");
			request.fehlstundenGesamt = JsonNullable.of(7);

			final var result = service.patch(ID_LEISTUNG, request);

			assertThat(result)
					.hasFieldOrPropertyWithValue("note", "2-")
					.hasFieldOrPropertyWithValue("fehlstundenGesamt", 7);
		}

		@Test
		@DisplayName("Wertänderung auf null entfernt den Wert")
		void nullEntferntWert() {
			entity.Warndatum = "2025-01-20";
			final var request = new SchuelerLeistungsdatenPatchRequest();
			request.mahndatum = JsonNullable.of(null);

			service.patch(ID_LEISTUNG, request);

			assertThat(entity.Warndatum).isNull();
		}

		@Nested
		@DisplayName("Berechtigungsprüfung")
		class Berechtigung {

			@Test
			@DisplayName("Nur Noten und Fehlstunden im aktuellen Schuljahresabschnitt - Fachlehrer-Regel ist anwendbar")
			void fachlehrerRegelAnwendbar() {
				final var request = new SchuelerLeistungsdatenPatchRequest();
				request.note = JsonNullable.of("2");

				service.patch(ID_LEISTUNG, request);

				verify(benutzerKompetenzService).checkKompetenzLeistungsdatenAendern(ID_FACHLEHRER, true, ID_KLASSE);
			}

			@Test
			@DisplayName("Weitere Attribute im Patch - Fachlehrer-Regel ist nicht anwendbar")
			void weitereAttribute() {
				final var request = new SchuelerLeistungsdatenPatchRequest();
				request.note = JsonNullable.of("2");
				request.wochenstunden = JsonNullable.of(3);

				service.patch(ID_LEISTUNG, request);

				verify(benutzerKompetenzService).checkKompetenzLeistungsdatenAendern(ID_FACHLEHRER, false, ID_KLASSE);
			}

			@Test
			@DisplayName("Lernabschnitt in einem anderen als dem aktuellen Schuljahresabschnitt - Fachlehrer-Regel ist nicht anwendbar")
			void vergangenerSchuljahresabschnitt() {
				when(eigeneSchuleService.getIdSchuljahresabschnitt()).thenReturn(ID_SCHULJAHRESABSCHNITT + 1);
				final var request = new SchuelerLeistungsdatenPatchRequest();
				request.note = JsonNullable.of("2");

				service.patch(ID_LEISTUNG, request);

				verify(benutzerKompetenzService).checkKompetenzLeistungsdatenAendern(ID_FACHLEHRER, false, ID_KLASSE);
			}

			@Test
			@DisplayName("Keine Kompetenz - FORBIDDEN, der Patch wird nicht angewendet")
			void keineKompetenz() {
				doThrow(new ApiOperationException(Response.Status.FORBIDDEN, "verboten"))
						.when(benutzerKompetenzService).checkKompetenzLeistungsdatenAendern(ID_FACHLEHRER, true, ID_KLASSE);
				final var request = new SchuelerLeistungsdatenPatchRequest();
				request.note = JsonNullable.of("2");

				assertApiException(() -> service.patch(ID_LEISTUNG, request), Response.Status.FORBIDDEN);

				assertThat(entity.NotenKrz).isNull();
			}

			@Test
			@DisplayName("Neuer Lernabschnitt im Patch - Klassenprüfung auch für den neuen Lernabschnitt")
			void neuerLernabschnitt() {
				final var neuerLernabschnitt = createLernabschnitt(11L, ID_SCHULJAHRESABSCHNITT, 101L);
				when(lernabschnittRepository.findById(11L)).thenReturn(Optional.of(neuerLernabschnitt));
				final var request = new SchuelerLeistungsdatenPatchRequest();
				request.lernabschnittID = JsonNullable.of(11L);

				service.patch(ID_LEISTUNG, request);

				verify(benutzerKompetenzService).pruefeKompetenzLeistungsdatenAendernKlassen(101L);
				verify(benutzerKompetenzService).checkKompetenzLeistungsdatenAendern(ID_FACHLEHRER, false, ID_KLASSE);
				assertThat(entity.Abschnitt_ID).isEqualTo(11L);
			}

			@Test
			@DisplayName("Keine Kompetenz für den neuen Lernabschnitt - FORBIDDEN, der Patch wird nicht angewendet")
			void keineKompetenzFuerNeuenLernabschnitt() {
				final var neuerLernabschnitt = createLernabschnitt(11L, ID_SCHULJAHRESABSCHNITT, 101L);
				when(lernabschnittRepository.findById(11L)).thenReturn(Optional.of(neuerLernabschnitt));
				doThrow(new ApiOperationException(Response.Status.FORBIDDEN, "verboten"))
						.when(benutzerKompetenzService).pruefeKompetenzLeistungsdatenAendernKlassen(101L);
				final var request = new SchuelerLeistungsdatenPatchRequest();
				request.lernabschnittID = JsonNullable.of(11L);

				assertApiException(() -> service.patch(ID_LEISTUNG, request), Response.Status.FORBIDDEN);

				assertThat(entity.Abschnitt_ID).isEqualTo(ID_LERNABSCHNITT);
			}
		}

		@Nested
		@DisplayName("Validierung")
		class Validierung {

			private void assertValidierungSchlaegtFehl(final SchuelerLeistungsdatenPatchRequest request, final Response.Status status) {
				assertApiException(() -> service.patch(ID_LEISTUNG, request), status);

				// Es wird erst validiert und danach gepatcht, ein gültiges Feld im Patch darf nicht übernommen werden
				assertThat(entity.NotenKrz).isNull();
			}

			private SchuelerLeistungsdatenPatchRequest createRequestMitGueltigerNote() {
				final var request = new SchuelerLeistungsdatenPatchRequest();
				request.note = JsonNullable.of("2");
				return request;
			}

			@Test
			@DisplayName("Neuer Lernabschnitt nicht vorhanden - CONFLICT")
			void lernabschnittNichtVorhanden() {
				final var request = createRequestMitGueltigerNote();
				request.lernabschnittID = JsonNullable.of(99L);
				when(lernabschnittRepository.findById(99L)).thenReturn(Optional.empty());

				assertValidierungSchlaegtFehl(request, Response.Status.CONFLICT);
			}

			@Test
			@DisplayName("Fach nicht vorhanden - CONFLICT")
			void fachNichtVorhanden() {
				final var request = createRequestMitGueltigerNote();
				request.fachID = JsonNullable.of(99L);
				when(fachRepository.findById(99L)).thenReturn(Optional.empty());

				assertValidierungSchlaegtFehl(request, Response.Status.CONFLICT);
			}

			@Test
			@DisplayName("Kurs nicht vorhanden - CONFLICT")
			void kursNichtVorhanden() {
				final var request = createRequestMitGueltigerNote();
				request.kursID = JsonNullable.of(99L);
				when(kurseRepository.findById(99L)).thenReturn(Optional.empty());

				assertValidierungSchlaegtFehl(request, Response.Status.CONFLICT);
			}

			@Test
			@DisplayName("Fachlehrer nicht vorhanden - CONFLICT")
			void lehrerNichtVorhanden() {
				final var request = createRequestMitGueltigerNote();
				request.lehrerID = JsonNullable.of(99L);

				assertValidierungSchlaegtFehl(request, Response.Status.CONFLICT);
			}

			@Test
			@DisplayName("Unbekannte Kursart - BAD_REQUEST")
			void unbekannteKursart() {
				final var request = createRequestMitGueltigerNote();
				request.kursart = JsonNullable.of("XYZ");

				assertValidierungSchlaegtFehl(request, Response.Status.BAD_REQUEST);
			}

			@Test
			@DisplayName("Ungültiges Abiturfach - BAD_REQUEST")
			void ungueltigesAbifach() {
				final var request = createRequestMitGueltigerNote();
				request.abifach = JsonNullable.of(6);

				assertValidierungSchlaegtFehl(request, Response.Status.BAD_REQUEST);
			}

			@Test
			@DisplayName("Ungültiges Notenkürzel der Quartalsnote - BAD_REQUEST")
			void ungueltigeNoteQuartal() {
				final var request = createRequestMitGueltigerNote();
				request.noteQuartal = JsonNullable.of("xyz");

				assertValidierungSchlaegtFehl(request, Response.Status.BAD_REQUEST);
			}

			@Test
			@DisplayName("Ungültiger Umfang des Lernstandsberichts - BAD_REQUEST")
			void ungueltigerUmfang() {
				final var request = createRequestMitGueltigerNote();
				request.umfangLernstandsbericht = JsonNullable.of("X");

				assertValidierungSchlaegtFehl(request, Response.Status.BAD_REQUEST);
			}
		}

		@Nested
		@DisplayName("Abgeleitete Felder")
		class AbgeleiteteFelder {

			@Test
			@DisplayName("Ein neues Fach setzt die Sortierung und den Kurs auf null")
			void fachSetztSortierungUndKursNull() {
				entity.Kurs_ID = ID_KURS;
				when(fachRepository.findById(6L)).thenReturn(Optional.of(createFach(6L)));
				final var request = new SchuelerLeistungsdatenPatchRequest();
				request.fachID = JsonNullable.of(6L);

				service.patch(ID_LEISTUNG, request);

				assertThat(entity)
						.hasFieldOrPropertyWithValue("Fach_ID", 6L)
						.hasFieldOrPropertyWithValue("Sortierung", SORTIERUNG_FACH)
						.hasFieldOrPropertyWithValue("Kurs_ID", null);
			}

			@Test
			@DisplayName("Fach und Kurs im selben Patch - der Kurs wird nach dem Fach gesetzt")
			void fachVorKurs() {
				entity.Kurs_ID = 77L;
				when(fachRepository.findById(6L)).thenReturn(Optional.of(createFach(6L)));
				final var request = new SchuelerLeistungsdatenPatchRequest();
				request.kursID = JsonNullable.of(ID_KURS);
				request.fachID = JsonNullable.of(6L);

				service.patch(ID_LEISTUNG, request);

				assertThat(entity)
						.hasFieldOrPropertyWithValue("Fach_ID", 6L)
						.hasFieldOrPropertyWithValue("Kurs_ID", ID_KURS);
			}

			@Test
			@DisplayName("Ein Kurs setzt den Fachlehrer und passt Kursart und Abiturfach an")
			void kursSetztFachlehrerUndKursart() {
				final var request = new SchuelerLeistungsdatenPatchRequest();
				request.kursID = JsonNullable.of(ID_KURS);

				service.patch(ID_LEISTUNG, request);

				assertThat(entity)
						.hasFieldOrPropertyWithValue("Kurs_ID", ID_KURS)
						.hasFieldOrPropertyWithValue("Fachlehrer_ID", ID_KURSLEHRER)
						.hasFieldOrPropertyWithValue("Kursart", "LK1")
						.hasFieldOrPropertyWithValue("KursartAllg", "LK")
						.hasFieldOrPropertyWithValue("AbiFach", "1");
			}

			@ParameterizedTest(name = "Kurs {0}, Abiturfach {1} -> Kursart {2}, Abiturfach {3}")
			@MethodSource("de.svws_nrw.service.schueler.leistungsdaten.SchuelerLeistungsdatenServiceTest#kursartFaelle")
			@DisplayName("Kursart und Abiturfach werden aus dem Kurs bestimmt")
			void kursartAusKurs(final String kursartAllg, final Integer abifach, final String erwarteteKursart, final String erwartetesAbifach) {
				when(kurseRepository.findById(ID_KURS)).thenReturn(Optional.of(createKurs(kursartAllg)));
				final var request = new SchuelerLeistungsdatenPatchRequest();
				request.kursID = JsonNullable.of(ID_KURS);
				if (abifach != null) {
					request.abifach = JsonNullable.of(abifach);
				}

				service.patch(ID_LEISTUNG, request);

				assertThat(entity)
						.hasFieldOrPropertyWithValue("Kursart", erwarteteKursart)
						.hasFieldOrPropertyWithValue("AbiFach", erwartetesAbifach);
			}

			@Test
			@DisplayName("Das Abiturfach im selben Patch wird vor dem Kurs gesetzt und berücksichtigt")
			void abifachVorKurs() {
				final var request = new SchuelerLeistungsdatenPatchRequest();
				request.kursID = JsonNullable.of(ID_KURS);
				request.abifach = JsonNullable.of(2);

				service.patch(ID_LEISTUNG, request);

				assertThat(entity)
						.hasFieldOrPropertyWithValue("Kursart", "LK2")
						.hasFieldOrPropertyWithValue("AbiFach", "2");
			}

			@Test
			@DisplayName("Ein Abiturfach null entfernt das Abiturfach")
			void abifachNull() {
				entity.AbiFach = "3";
				final var request = new SchuelerLeistungsdatenPatchRequest();
				request.abifach = JsonNullable.of(null);

				service.patch(ID_LEISTUNG, request);

				assertThat(entity.AbiFach).isNull();
			}

			@Test
			@DisplayName("Ein Kurs null entfernt den Kurs, der Fachlehrer bleibt")
			void kursNull() {
				entity.Kurs_ID = ID_KURS;
				final var request = new SchuelerLeistungsdatenPatchRequest();
				request.kursID = JsonNullable.of(null);

				service.patch(ID_LEISTUNG, request);

				assertThat(entity)
						.hasFieldOrPropertyWithValue("Kurs_ID", null)
						.hasFieldOrPropertyWithValue("Fachlehrer_ID", ID_FACHLEHRER);
			}

			@Test
			@DisplayName("Eine gesendete Kursart gewinnt gegenüber der Kursart des Kurses")
			void gesendeteKursartGewinnt() {
				final var request = new SchuelerLeistungsdatenPatchRequest();
				request.kursart = JsonNullable.of("GKM");
				request.kursID = JsonNullable.of(ID_KURS);

				service.patch(ID_LEISTUNG, request);

				assertThat(entity)
						.hasFieldOrPropertyWithValue("Kursart", "GKM")
						.hasFieldOrPropertyWithValue("KursartAllg", "GK");
			}

			@Test
			@DisplayName("Eine Kursart null ergibt PUK")
			void kursartNull() {
				final var request = new SchuelerLeistungsdatenPatchRequest();
				request.kursart = JsonNullable.of(null);

				service.patch(ID_LEISTUNG, request);

				assertThat(entity)
						.hasFieldOrPropertyWithValue("Kursart", "PUK")
						.hasFieldOrPropertyWithValue("KursartAllg", "PUK");
			}

			@Test
			@DisplayName("Ein Lehrer im Patch wird durch den Lehrer des Kurses überschrieben")
			void kursUeberschreibtLehrer() {
				when(lehrerRepository.existsByIds(List.of(42L))).thenReturn(Set.of(42L));
				final var request = new SchuelerLeistungsdatenPatchRequest();
				request.lehrerID = JsonNullable.of(42L);
				request.kursID = JsonNullable.of(ID_KURS);

				service.patch(ID_LEISTUNG, request);

				assertThat(entity.Fachlehrer_ID).isEqualTo(ID_KURSLEHRER);
			}
		}
	}

	// -------------------------------------------------------------------------
	// hasOnlyFachlehrerAttribute
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("hasOnlyFachlehrerAttribute")
	class HasOnlyFachlehrerAttribute {

		@Test
		@DisplayName("leerer Patch")
		void hasOnlyFachlehrerAttribute_leer() {
			assertThat(SchuelerLeistungsdatenService.hasOnlyFachlehrerAttribute(new SchuelerLeistungsdatenPatchRequest())).isTrue();
		}

		@Test
		@DisplayName("Noten und Fehlstunden")
		void hasOnlyFachlehrerAttribute_notenUndFehlstunden() {
			final var request = new SchuelerLeistungsdatenPatchRequest();
			request.note = JsonNullable.of("2");
			request.noteQuartal = JsonNullable.of("3");
			request.fehlstundenGesamt = JsonNullable.of(4);
			request.fehlstundenUnentschuldigt = JsonNullable.of(1);

			assertThat(SchuelerLeistungsdatenService.hasOnlyFachlehrerAttribute(request)).isTrue();
		}

		@ParameterizedTest(name = "{0} ist kein Fachlehrer-Attribut")
		@MethodSource("de.svws_nrw.service.schueler.leistungsdaten.SchuelerLeistungsdatenServiceTest#andereAttribute")
		void hasOnlyFachlehrerAttribute_andereAttribute(final String attribut, final Consumer<SchuelerLeistungsdatenPatchRequest> setter) {
			final var request = new SchuelerLeistungsdatenPatchRequest();
			request.note = JsonNullable.of("2");
			setter.accept(request);

			assertThat(SchuelerLeistungsdatenService.hasOnlyFachlehrerAttribute(request)).isFalse();
		}

		@Test
		@DisplayName("jedes andere Attribut des Requests sperrt die Fachlehrer-Regel")
		void hasOnlyFachlehrerAttribute_alleAnderenAttributeSperren() {
			final var erlaubt = Set.of("note", "noteQuartal", "fehlstundenGesamt", "fehlstundenUnentschuldigt");

			final var attribute = Stream.of(SchuelerLeistungsdatenPatchRequest.class.getFields())
					.filter(feld -> JsonNullable.class.isAssignableFrom(feld.getType()))
					.filter(feld -> !erlaubt.contains(feld.getName()))
					.toList();

			assertThat(attribute)
					.isNotEmpty()
					.allSatisfy(feld -> assertThat(SchuelerLeistungsdatenService.hasOnlyFachlehrerAttribute(createRequestMitAttribut(feld))).as(feld.getName()).isFalse());
		}

		private static SchuelerLeistungsdatenPatchRequest createRequestMitAttribut(final Field feld) {
			final var request = new SchuelerLeistungsdatenPatchRequest();
			try {
				feld.set(request, JsonNullable.of(null));
			} catch (final IllegalAccessException e) {
				throw new IllegalStateException(e);
			}

			return request;
		}
	}

	// -------------------------------------------------------------------------
	// deleteMultiple
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("deleteMultiple")
	class DeleteMultiple {

		private void stubDelete(final List<Long> ids, final List<DTOSchuelerLeistungsdaten> gefunden) {
			when(repository.findListByIds(ids)).thenReturn(gefunden);
			lenient().when(repository.delete(ArgumentMatchers.<List<DTOSchuelerLeistungsdaten>>any())).thenAnswer(invocation -> invocation.getArgument(0));
		}

		@Test
		@DisplayName("Löscht gefundene Einträge und markiert nicht gefundene IDs")
		void loeschtUndMarkiert() {
			final var zweiter = new DTOSchuelerLeistungsdaten(2L, ID_LERNABSCHNITT, ID_FACH);
			stubDelete(List.of(1L, 2L, 3L), List.of(entity, zweiter));

			final var result = service.deleteMultiple(List.of(1L, 2L, 3L));

			assertThat(result)
					.hasSize(3)
					.extracting(r -> r.id, r -> r.success)
					.containsExactly(
							org.assertj.core.groups.Tuple.tuple(1L, true),
							org.assertj.core.groups.Tuple.tuple(2L, true),
							org.assertj.core.groups.Tuple.tuple(3L, false));
			assertThat(result.get(2).log).containsExactly("Leistungsdaten mit ID 3 wurde nicht gefunden.");
			verify(benutzerKompetenzService).pruefeKompetenzLeistungsdatenAendernKlassen(ID_KLASSE);
		}

		@Test
		@DisplayName("Die Klassenprüfung erfolgt einmal für alle Klassen der Lernabschnitte")
		void pruefungFuerAlleKlassen() {
			final var zweiter = new DTOSchuelerLeistungsdaten(2L, 11L, ID_FACH);
			final var dritter = new DTOSchuelerLeistungsdaten(3L, ID_LERNABSCHNITT, ID_FACH);
			stubDelete(List.of(1L, 2L, 3L), List.of(entity, zweiter, dritter));
			when(lernabschnittRepository.findListByIds(List.of(ID_LERNABSCHNITT, 11L)))
					.thenReturn(List.of(lernabschnitt, createLernabschnitt(11L, ID_SCHULJAHRESABSCHNITT, 101L)));

			service.deleteMultiple(List.of(1L, 2L, 3L));

			verify(benutzerKompetenzService).pruefeKompetenzLeistungsdatenAendernKlassen(ID_KLASSE, 101L);
		}

		@Test
		@DisplayName("Keine Kompetenz für eine Klasse - FORBIDDEN, es wird nichts gelöscht")
		void keineKompetenz() {
			final var zweiter = new DTOSchuelerLeistungsdaten(2L, 11L, ID_FACH);
			when(repository.findListByIds(List.of(1L, 2L))).thenReturn(List.of(entity, zweiter));
			when(lernabschnittRepository.findListByIds(List.of(ID_LERNABSCHNITT, 11L)))
					.thenReturn(List.of(lernabschnitt, createLernabschnitt(11L, ID_SCHULJAHRESABSCHNITT, 101L)));
			doThrow(new ApiOperationException(Response.Status.FORBIDDEN, "verboten"))
					.when(benutzerKompetenzService).pruefeKompetenzLeistungsdatenAendernKlassen(ID_KLASSE, 101L);

			assertApiException(() -> service.deleteMultiple(List.of(1L, 2L)), Response.Status.FORBIDDEN);

			verify(repository, never()).delete(ArgumentMatchers.<List<DTOSchuelerLeistungsdaten>>any());
		}

		@Test
		@DisplayName("Liste null - BAD_REQUEST, es wird nichts geprüft oder gelöscht")
		void listeNull() {
			assertApiException(() -> service.deleteMultiple(null), Response.Status.BAD_REQUEST);

			verifyNoInteractions(repository, benutzerKompetenzService);
		}

		@Test
		@DisplayName("Keine Einträge gefunden - alle IDs werden markiert")
		void nichtsGefunden() {
			stubDelete(List.of(5L), List.of());

			final var result = service.deleteMultiple(List.of(5L));

			assertThat(result)
					.hasSize(1)
					.allSatisfy(r -> assertThat(r.success).isFalse());
		}
	}

	// -------------------------------------------------------------------------
	// Berechtigungsmatrix mit dem echten BenutzerKompetenzService
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("Berechtigungsmatrix")
	class Berechtigungsmatrix {

		@Mock
		private BenutzerAllgemeinRepository benutzerRepository;

		@Mock
		private Benutzer benutzer;

		private SchuelerLeistungsdatenService serviceMitEchterKompetenzpruefung;

		@BeforeEach
		void setUp() {
			lenient().when(benutzerRepository.getAktuellerBenutzer()).thenReturn(benutzer);
			serviceMitEchterKompetenzpruefung = new SchuelerLeistungsdatenService(repos, SchuelerLeistungsdatenMapper.INSTANCE, eigeneSchuleService,
					new BenutzerKompetenzService(benutzerRepository));
			lenient().when(repository.findListByIds(List.of(ID_LEISTUNG))).thenReturn(List.of(entity));
			lenient().when(repository.delete(ArgumentMatchers.<List<DTOSchuelerLeistungsdaten>>any())).thenAnswer(invocation -> invocation.getArgument(0));
		}

		private void stubNurAlleAendern() {
			lenient().when(benutzer.hatVerwendeteKompetenz(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN)).thenReturn(true);
		}

		private void stubNurFunktionsbezogen(final Set<Long> klassen, final Long idLehrer) {
			lenient().when(benutzer.hatVerwendeteKompetenz(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN)).thenReturn(false);
			lenient().when(benutzer.hatVerwendeteKompetenz(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN)).thenReturn(true);
			lenient().when(benutzer.getKlassenIDs()).thenReturn(klassen);
			lenient().when(benutzer.getIdLehrer()).thenReturn(idLehrer);
		}

		private SchuelerLeistungsdatenPatchRequest createNotenPatch() {
			final var request = new SchuelerLeistungsdatenPatchRequest();
			request.note = JsonNullable.of("2");
			request.fehlstundenGesamt = JsonNullable.of(3);
			return request;
		}

		private SchuelerLeistungsdatenPatchRequest createWochenstundenPatch() {
			final var request = new SchuelerLeistungsdatenPatchRequest();
			request.wochenstunden = JsonNullable.of(3);
			return request;
		}

		@Test
		@DisplayName("Nur übergreifende Kompetenz - Anlegen, Ändern und Löschen sind für jede Klasse erlaubt")
		void nurAlleAendern() {
			stubNurAlleAendern();

			assertThatCode(() -> serviceMitEchterKompetenzpruefung.create(createCreateRequest())).doesNotThrowAnyException();
			assertThatCode(() -> serviceMitEchterKompetenzpruefung.patch(ID_LEISTUNG, createWochenstundenPatch())).doesNotThrowAnyException();
			assertThatCode(() -> serviceMitEchterKompetenzpruefung.deleteMultiple(List.of(ID_LEISTUNG))).doesNotThrowAnyException();
		}

		@Test
		@DisplayName("Nur funktionsbezogene Kompetenz mit Zuständigkeit für die Klasse - Anlegen, Ändern und Löschen sind erlaubt")
		void funktionsbezogenMitKlasse() {
			stubNurFunktionsbezogen(Set.of(ID_KLASSE), null);

			assertThatCode(() -> serviceMitEchterKompetenzpruefung.create(createCreateRequest())).doesNotThrowAnyException();
			assertThatCode(() -> serviceMitEchterKompetenzpruefung.patch(ID_LEISTUNG, createWochenstundenPatch())).doesNotThrowAnyException();
			assertThatCode(() -> serviceMitEchterKompetenzpruefung.deleteMultiple(List.of(ID_LEISTUNG))).doesNotThrowAnyException();
		}

		@Test
		@DisplayName("Nur funktionsbezogene Kompetenz ohne Zuständigkeit für die Klasse - Anlegen, Ändern und Löschen sind verboten")
		void funktionsbezogenOhneKlasse() {
			stubNurFunktionsbezogen(Set.of(7L), null);

			assertApiException(() -> serviceMitEchterKompetenzpruefung.create(createCreateRequest()), Response.Status.FORBIDDEN);
			assertApiException(() -> serviceMitEchterKompetenzpruefung.patch(ID_LEISTUNG, createWochenstundenPatch()), Response.Status.FORBIDDEN);
			assertApiException(() -> serviceMitEchterKompetenzpruefung.deleteMultiple(List.of(ID_LEISTUNG)), Response.Status.FORBIDDEN);

			verify(repository, never()).create(any(DTOSchuelerLeistungsdaten.class));
			verify(repository, never()).delete(ArgumentMatchers.<List<DTOSchuelerLeistungsdaten>>any());
		}

		@Test
		@DisplayName("Nur funktionsbezogene Kompetenz bei einem Lernabschnitt ohne Klasse - Anlegen, Ändern und Löschen sind verboten")
		void funktionsbezogenLernabschnittOhneKlasse() {
			stubNurFunktionsbezogen(Set.of(ID_KLASSE), null);
			lernabschnitt.Klassen_ID = null;

			assertApiException(() -> serviceMitEchterKompetenzpruefung.create(createCreateRequest()), Response.Status.FORBIDDEN);
			assertApiException(() -> serviceMitEchterKompetenzpruefung.patch(ID_LEISTUNG, createWochenstundenPatch()), Response.Status.FORBIDDEN);
			assertApiException(() -> serviceMitEchterKompetenzpruefung.deleteMultiple(List.of(ID_LEISTUNG)), Response.Status.FORBIDDEN);
		}

		@Test
		@DisplayName("Fachlehrer ändert Noten mit neuem Lernabschnitt - die Fachlehrer-Regel greift nicht, die Klassen werden geprüft")
		void fachlehrerMitNeuemLernabschnitt() {
			stubNurFunktionsbezogen(Set.of(101L), ID_FACHLEHRER);
			when(lernabschnittRepository.findById(11L)).thenReturn(Optional.of(createLernabschnitt(11L, ID_SCHULJAHRESABSCHNITT, 101L)));
			final var request = createNotenPatch();
			request.lernabschnittID = JsonNullable.of(11L);

			assertApiException(() -> serviceMitEchterKompetenzpruefung.patch(ID_LEISTUNG, request), Response.Status.FORBIDDEN);
		}

		@Test
		@DisplayName("Fachlehrer ändert Noten und Fehlstunden im aktuellen Schuljahresabschnitt ohne Zuständigkeit für die Klasse - erlaubt")
		void fachlehrerRegel() {
			stubNurFunktionsbezogen(Set.of(7L), ID_FACHLEHRER);

			assertThatCode(() -> serviceMitEchterKompetenzpruefung.patch(ID_LEISTUNG, createNotenPatch())).doesNotThrowAnyException();
		}

		@Test
		@DisplayName("Fachlehrer ändert weitere Attribute ohne Zuständigkeit für die Klasse - FORBIDDEN")
		void fachlehrerWeitereAttribute() {
			stubNurFunktionsbezogen(Set.of(7L), ID_FACHLEHRER);
			final var request = createNotenPatch();
			request.wochenstunden = JsonNullable.of(3);

			assertApiException(() -> serviceMitEchterKompetenzpruefung.patch(ID_LEISTUNG, request), Response.Status.FORBIDDEN);
		}

		@Test
		@DisplayName("Fachlehrer ändert Noten in einem vergangenen Schuljahresabschnitt ohne Zuständigkeit für die Klasse - FORBIDDEN")
		void fachlehrerVergangenerAbschnitt() {
			stubNurFunktionsbezogen(Set.of(7L), ID_FACHLEHRER);
			when(eigeneSchuleService.getIdSchuljahresabschnitt()).thenReturn(ID_SCHULJAHRESABSCHNITT + 1);

			assertApiException(() -> serviceMitEchterKompetenzpruefung.patch(ID_LEISTUNG, createNotenPatch()), Response.Status.FORBIDDEN);
		}

		@Test
		@DisplayName("Ein anderer Lehrer ändert Noten ohne Zuständigkeit für die Klasse - FORBIDDEN")
		void anderesLehrer() {
			stubNurFunktionsbezogen(Set.of(7L), ID_FACHLEHRER + 1);

			assertApiException(() -> serviceMitEchterKompetenzpruefung.patch(ID_LEISTUNG, createNotenPatch()), Response.Status.FORBIDDEN);
		}
	}

	// -------------------------------------------------------------------------
	// Antwortobjekte
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("deleteMultiple | Die Antwort enthält SimpleOperationResponse-Einträge")
	void deleteMultiple_antworttyp() {
		when(repository.findListByIds(List.of(ID_LEISTUNG))).thenReturn(List.of(entity));
		when(repository.delete(ArgumentMatchers.<List<DTOSchuelerLeistungsdaten>>any())).thenAnswer(invocation -> invocation.getArgument(0));

		final List<SimpleOperationResponse> result = service.deleteMultiple(List.of(ID_LEISTUNG));

		assertThat(result)
				.singleElement()
				.hasFieldOrPropertyWithValue("id", ID_LEISTUNG)
				.hasFieldOrPropertyWithValue("success", true);
	}

}
