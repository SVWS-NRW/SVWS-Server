package de.svws_nrw.service.klassen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.asd.data.klassen.KlassenDaten;
import de.svws_nrw.asd.data.schueler.Schueler;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.klassen.Klassenart;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.klassen.KlassenMapper;
import de.svws_nrw.mapper.schueler.SchuelerMapper;
import de.svws_nrw.repo.klassen.KlassenRepository;
import de.svws_nrw.repo.klassen.KlassenleitungenRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepository;
import de.svws_nrw.service.schule.EigeneSchuleService;
import de.svws_nrw.service.schule.SchuljahresabschnittService;
import jakarta.ws.rs.core.Response;

/**
 * Test für die Klasse {@link KlassenService}
 */
@ExtendWith(MockitoExtension.class)
class KlassenServiceTest {

	private static final int SCHULJAHR = 2024;

	private static final long ID_ABSCHNITT_1 = 11L;
	private static final long ID_ABSCHNITT_2 = 12L;

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

	private KlassenService service;

	@BeforeAll
	static void initAsd() {
		ASDCoreTypeUtils.initAll();
	}

	@BeforeEach
	void setUp() {
		lenient().when(eigeneSchuleService.getSchulform()).thenReturn(Schulform.GY);
		final KlassenSchuelerLoader schuelerLoader = new KlassenSchuelerLoader(lernabschnittRepository, schuelerRepository, SchuelerMapper.INSTANCE);
		final KlassenMappingContextLoader kontextLoader = new KlassenMappingContextLoader(klassenRepository, klassenleitungenRepository, schuelerLoader,
				eigeneSchuleService, schuljahresabschnittService);
		service = new KlassenService(klassenRepository, schuljahresabschnittService, kontextLoader, KlassenMapper.INSTANCE);
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
		final DTOSchueler schueler = new DTOSchueler(id, "GU-" + id, false);
		schueler.Nachname = "Nachname" + id;
		schueler.Vorname = "Vorname" + id;
		schueler.Geschlecht = Geschlecht.W;
		schueler.idStatus = 2;
		schueler.hatLoeschvermerk = geloescht;
		return schueler;
	}

	private void stubAbschnitt(final long id, final int abschnitt, final Long idVoriger, final Long idFolge) {
		when(schuljahresabschnittService.getById(id)).thenReturn(createAbschnitt(id, abschnitt, idVoriger, idFolge));
	}

	private static void assertApiError(final Runnable aufruf, final Response.Status status, final String meldung) {
		assertThatThrownBy(aufruf::run)
				.isInstanceOfSatisfying(ApiOperationException.class, e -> {
					assertThat(e.getStatus()).isEqualTo(status);
					assertThat(e.getBody()).isEqualTo(meldung);
				});
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
			when(schuljahresabschnittService.getById(77L))
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

		@Test
		@DisplayName("Abschnitt-ID null: 400")
		void abschnittNull() {
			assertApiError(() -> service.getListBySchuljahresabschnitt(null, true), Response.Status.BAD_REQUEST,
					"Die ID für den Schuljahresabschnitt darf nicht null sein.");
		}

	}

	@Nested
	@DisplayName("getListByIdsMitSchueler")
	class GetListByIds {

		@Test
		@DisplayName("Klassen aus verschiedenen Abschnitten: Reihenfolge der IDs bleibt erhalten, jeder Abschnitt wird einmal geladen")
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
			verify(schuljahresabschnittService, times(1)).getById(ID_ABSCHNITT_1);
			verify(schuljahresabschnittService, times(1)).getById(ID_ABSCHNITT_2);
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

}
