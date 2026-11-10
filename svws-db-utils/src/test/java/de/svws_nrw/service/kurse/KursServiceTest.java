package de.svws_nrw.service.kurse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import org.openapitools.jackson.nullable.JsonNullable;

import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursLehrer;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursSchueler;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.kurse.KursMapper;
import de.svws_nrw.repo.kurse.KurseRepository;
import de.svws_nrw.repo.kurse.KursSchuelerRepository;
import de.svws_nrw.repo.kurse.KurslehrerRepository;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schueler.leistungsdaten.SchuelerLeistungsdatenRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import de.svws_nrw.repo.schule.kataloge.fach.FachRepository;
import de.svws_nrw.repo.schule.kataloge.jahrgang.JahrgangRepository;
import de.svws_nrw.repo.schule.kataloge.schule.SchuleRepository;
import jakarta.ws.rs.core.Response.Status;

/**
 * Testklasse für den {@link KursService}
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests für den KursService")
class KursServiceTest {

	@Mock
	private KurseRepository kurseRepository;

	@Mock
	private KursSchuelerRepository kursSchuelerRepository;

	@Mock
	private KurslehrerRepository kurslehrerRepository;

	@Mock
	private SchuelerRepository schuelerRepository;

	@Mock
	private JahrgangRepository jahrgangRepository;

	@Mock
	private FachRepository fachRepository;

	@Mock
	private LehrerRepository lehrerRepository;

	@Mock
	private SchuleRepository schuleRepository;

	@Mock
	private SchuljahresabschnitteRepository schuljahresabschnitteRepository;

	@Mock
	private SchuelerLeistungsdatenRepository schuelerLeistungsdatenRepository;

	@Captor
	private ArgumentCaptor<List<DTOSchuelerLeistungsdaten>> leistungsdatenCaptor;

	private KursService service;

	private MockedStatic<TransactionSupport> transactionSupportMock;

	@BeforeEach
	void setUp() {
		final var repositories = new KursRepositories(kurseRepository, kursSchuelerRepository, kurslehrerRepository, schuelerRepository,
				jahrgangRepository, fachRepository, lehrerRepository, schuleRepository, schuljahresabschnitteRepository,
				schuelerLeistungsdatenRepository);
		service = new KursService(repositories, KursMapper.INSTANCE);
		transactionSupportMock = mockStatic(TransactionSupport.class);
	}

	@AfterEach
	void tearDown() {
		transactionSupportMock.close();
	}

	private void mockTransaction() {
		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<?>>any()))
				.thenAnswer(inv -> inv.getArgument(0, Supplier.class).get());
		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.any(Runnable.class)))
				.thenAnswer(inv -> {
					inv.getArgument(0, Runnable.class).run();
					return null;
				});
	}

	private static DTOKurs createKurs(final long id, final int sortierung) {
		final var kurs = new DTOKurs(id, 10L, "K" + id, 3L);
		kurs.Sortierung = sortierung;
		return kurs;
	}

	private static DTOSchueler createSchueler(final long id, final Boolean hatLoeschvermerk) {
		final var schueler = new DTOSchueler(id, "GU-" + id, false);
		schueler.Nachname = "Nachname" + id;
		schueler.Vorname = "Vorname" + id;
		schueler.Geschlecht = Geschlecht.W;
		schueler.idStatus = 8;
		schueler.hatLoeschvermerk = hatLoeschvermerk;
		return schueler;
	}

	private void mockSchueler(final long idKurs, final DTOSchueler... schueler) {
		final var zuordnung = Arrays.stream(schueler)
				.map(s -> new DTOKursSchueler(idKurs, s.ID, 1L))
				.toList();
		when(kursSchuelerRepository.getMapByKurse(List.of(idKurs))).thenReturn(Map.of(idKurs, zuordnung));
		when(schuelerRepository.findMapByIds(anyCollection()))
				.thenReturn(Arrays.stream(schueler).collect(Collectors.toMap(s -> s.ID, s -> s)));
	}

	private static KursCreateRequest createRequest() {
		final var request = new KursCreateRequest();
		request.idSchuljahresabschnitt = 10L;
		request.kuerzel = "D-GK1";
		request.idFach = 3L;
		request.kursartAllg = "GK";
		return request;
	}

	private void mockCreateVoraussetzungen() {
		mockTransaction();
		when(schuljahresabschnitteRepository.existsById(10L)).thenReturn(true);
		when(fachRepository.findById(3L)).thenReturn(Optional.of(new DTOFach(3L, false)));
	}

	private DTOKurs createKursUndLiefereGespeicherten(final KursCreateRequest request) {
		mockCreateVoraussetzungen();
		when(kurseRepository.create(any(DTOKurs.class))).thenAnswer(inv -> inv.getArgument(0));
		when(kurslehrerRepository.getMapZusatzkraefte(anyCollection())).thenReturn(Map.of());

		service.create(request);

		final var captor = ArgumentCaptor.forClass(DTOKurs.class);
		verify(kurseRepository).create(captor.capture());
		return captor.getValue();
	}

	@Test
	@DisplayName("create: Ein Jahrgang setzt Jahrgang_ID und ASDJahrgang")
	void create_einJahrgang() {
		final var request = createRequest();
		request.idJahrgaenge = List.of(4L);
		final var jahrgang = new DTOJahrgang(4L);
		jahrgang.ASDJahrgang = "05";
		when(jahrgangRepository.findMapByIds(List.of(4L))).thenReturn(Map.of(4L, jahrgang));

		assertThat(createKursUndLiefereGespeicherten(request))
				.hasFieldOrPropertyWithValue("Jahrgang_ID", 4L)
				.hasFieldOrPropertyWithValue("ASDJahrgang", "05")
				.hasFieldOrPropertyWithValue("Jahrgaenge", null);
	}

	@Test
	@DisplayName("create: Der Kurs wird mit den Prüfungen gespeichert und ohne Schüler geliefert, die Schulnummer wird als Text gesucht")
	void create_speichertKursUndLiefertKursdaten() {
		final var request = createRequest();
		request.idLehrer = 7L;
		request.schulnummer = 123456;
		when(lehrerRepository.existsById(7L)).thenReturn(true);
		when(schuleRepository.existsBySchulnummer("123456")).thenReturn(true);

		final var gespeichert = createKursUndLiefereGespeicherten(request);

		assertThat(gespeichert)
				.hasFieldOrPropertyWithValue("KurzBez", "D-GK1")
				.hasFieldOrPropertyWithValue("Lehrer_ID", 7L)
				.hasFieldOrPropertyWithValue("SchulNr", 123456);
		verifyNoInteractions(kursSchuelerRepository, schuelerRepository);
	}

	@Test
	@DisplayName("create: Ein unbekannter Schuljahresabschnitt führt zu 404")
	void create_abschnittUnbekannt() {
		mockTransaction();
		when(schuljahresabschnitteRepository.existsById(10L)).thenReturn(false);
		final var request = createRequest();

		assertThatThrownBy(() -> service.create(request)).isInstanceOf(ApiOperationException.class)
				.hasMessage("Es konnte kein Schuljahresabschnitt mit der ID 10 gefunden werden.")
				.hasFieldOrPropertyWithValue("status", Status.NOT_FOUND);
		verify(kurseRepository, never()).create(any(DTOKurs.class));
	}

	@Test
	@DisplayName("create: Ein fehlendes Fach führt zu 400")
	void create_fachNull() {
		final var request = createRequest();
		request.idFach = null;
		mockTransaction();
		when(schuljahresabschnitteRepository.existsById(10L)).thenReturn(true);

		assertThatThrownBy(() -> service.create(request)).isInstanceOf(ApiOperationException.class)
				.hasMessage("Die ID des Faches darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("create: Ein unbekanntes Fach führt zu 404")
	void create_fachUnbekannt() {
		mockTransaction();
		when(schuljahresabschnitteRepository.existsById(10L)).thenReturn(true);
		when(fachRepository.findById(3L)).thenReturn(Optional.empty());
		final var request = createRequest();

		assertThatThrownBy(() -> service.create(request)).isInstanceOf(ApiOperationException.class)
				.hasMessage("Es konnte kein Fach mit der ID 3 gefunden werden.")
				.hasFieldOrPropertyWithValue("status", Status.NOT_FOUND);
	}

	@Test
	@DisplayName("create: Ein unbekannter Lehrer führt zu 404")
	void create_lehrerUnbekannt() {
		final var request = createRequest();
		request.idLehrer = 7L;
		mockCreateVoraussetzungen();
		when(lehrerRepository.existsById(7L)).thenReturn(false);

		assertThatThrownBy(() -> service.create(request)).isInstanceOf(ApiOperationException.class)
				.hasMessage("Es konnte kein Lehrer mit der angegebenen ID 7 gefunden werden.")
				.hasFieldOrPropertyWithValue("status", Status.NOT_FOUND);
	}

	@Test
	@DisplayName("create: Eine unbekannte Schulnummer führt zu 404")
	void create_schulnummerUnbekannt() {
		final var request = createRequest();
		request.schulnummer = 123456;
		mockCreateVoraussetzungen();
		when(schuleRepository.existsBySchulnummer("123456")).thenReturn(false);

		assertThatThrownBy(() -> service.create(request)).isInstanceOf(ApiOperationException.class)
				.hasMessage("Für die Schulnummer 123456 konnte keine passende Schule gefunden werden.")
				.hasFieldOrPropertyWithValue("status", Status.NOT_FOUND);
	}

	@Test
	@DisplayName("create: Ein unbekannter Jahrgang führt zu 400")
	void create_jahrgangUnbekannt() {
		final var request = createRequest();
		request.idJahrgaenge = List.of(4L, 5L);
		mockCreateVoraussetzungen();
		when(jahrgangRepository.findMapByIds(List.of(4L, 5L))).thenReturn(Map.of(4L, new DTOJahrgang(4L)));

		assertThatThrownBy(() -> service.create(request)).isInstanceOf(ApiOperationException.class)
				.hasMessage("Mindestens einer der angegebenen Jahrgang-IDs existiert nicht in der SVWS-Datenbank")
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
		verify(kurseRepository, never()).create(any(DTOKurs.class));
	}

	@Test
	@DisplayName("delete: Alle Kurse werden gelöscht und als Erfolg gemeldet, ohne Referenzprüfung")
	void delete_alleLoeschbar() {
		mockTransaction();
		final var kurse = List.of(createKurs(1L, 1), createKurs(2L, 2));
		when(kurseRepository.findListByIds(List.of(1L, 2L))).thenReturn(kurse);
		when(kurseRepository.delete(kurse)).thenReturn(kurse);

		assertThat(service.delete(List.of(1L, 2L)))
				.hasSize(2)
				.allSatisfy(r -> assertThat(r.success).isTrue());
	}

	@Test
	@DisplayName("delete: Eine unbekannte ID liefert einen Fehlereintrag, die übrigen Kurse werden gelöscht")
	void delete_unbekannteId() {
		mockTransaction();
		final var kurse = List.of(createKurs(1L, 1));
		when(kurseRepository.findListByIds(List.of(1L, 99L))).thenReturn(kurse);
		when(kurseRepository.delete(kurse)).thenReturn(kurse);

		assertThat(service.delete(List.of(1L, 99L)))
				.extracting(r -> r.id, r -> r.success)
				.containsExactly(tuple(1L, true), tuple(99L, false));
	}

	@Test
	@DisplayName("delete: Eine leere ID-Liste liefert eine leere Antwort")
	void delete_leereListe() {
		mockTransaction();
		when(kurseRepository.findListByIds(List.of())).thenReturn(List.of());
		when(kurseRepository.delete(List.<DTOKurs>of())).thenReturn(List.of());

		assertThat(service.delete(List.of())).isEmpty();
	}

	private DTOKurs mockKursFuerPatch() {
		mockTransaction();
		final var kurs = createKurs(1L, 5);
		when(kurseRepository.findById(1L)).thenReturn(Optional.of(kurs));
		return kurs;
	}

	@Test
	@DisplayName("patch: Ein unbekannter Kurs führt zu 404")
	void patch_unbekannterKurs() {
		mockTransaction();
		when(kurseRepository.findById(99L)).thenReturn(Optional.empty());
		final var request = new KursPatchRequest();

		assertThatThrownBy(() -> service.patch(99L, request)).isInstanceOf(ApiOperationException.class)
				.hasMessage("Kein Kurs mit der ID 99 gefunden.")
				.hasFieldOrPropertyWithValue("status", Status.NOT_FOUND);
		verify(kurseRepository, never()).update(any(DTOKurs.class));
	}

	@Test
	@DisplayName("patch: Nicht gesetzte Attribute ändern nichts und lösen keine Prüfung aus")
	void patch_nichtGesetzt() {
		final var kurs = mockKursFuerPatch();

		service.patch(1L, new KursPatchRequest());

		assertThat(kurs)
				.hasFieldOrPropertyWithValue("KurzBez", "K1")
				.hasFieldOrPropertyWithValue("Fach_ID", 3L)
				.hasFieldOrPropertyWithValue("Sortierung", 5);
		verify(kurseRepository).update(kurs);
		verifyNoInteractions(fachRepository, lehrerRepository, schuleRepository, jahrgangRepository, schuelerLeistungsdatenRepository);
	}

	@Test
	@DisplayName("patch: Gesetzte Referenzen werden geprüft, der Kurs wird gespeichert und der Fachlehrer übernommen")
	void patch_prueftReferenzenUndSpeichert() {
		final var kurs = mockKursFuerPatch();
		final var request = new KursPatchRequest();
		request.idFach = JsonNullable.of(8L);
		request.idLehrer = JsonNullable.of(7L);
		request.schulnummer = JsonNullable.of(123456);
		request.sortierung = JsonNullable.of(7);
		when(fachRepository.findById(8L)).thenReturn(Optional.of(new DTOFach(8L, false)));
		when(lehrerRepository.existsById(7L)).thenReturn(true);
		when(schuleRepository.existsBySchulnummer("123456")).thenReturn(true);
		when(schuelerLeistungsdatenRepository.findListByKurs(1L)).thenReturn(List.of());

		service.patch(1L, request);

		assertThat(kurs)
				.hasFieldOrPropertyWithValue("Fach_ID", 8L)
				.hasFieldOrPropertyWithValue("Lehrer_ID", 7L)
				.hasFieldOrPropertyWithValue("SchulNr", 123456)
				.hasFieldOrPropertyWithValue("Sortierung", 7);
		verify(kurseRepository).update(kurs);
	}

	@Test
	@DisplayName("patch: Der geänderte Kurs wird mit Schülern zurückgegeben")
	void patch_liefertGeaenderteDaten() {
		mockKursFuerPatch();
		mockSchueler(1L, createSchueler(100L, false));
		final var request = new KursPatchRequest();
		request.sortierung = JsonNullable.of(9);

		final var result = service.patch(1L, request);

		assertThat(result)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("sortierung", 9)
				.satisfies(k -> assertThat(k.schueler).extracting(sch -> sch.id).containsExactly(100L));
	}

	@Test
	@DisplayName("patch: Ein fehlendes Fach führt zu 400")
	void patch_fachNull() {
		mockKursFuerPatch();
		final var request = new KursPatchRequest();
		request.idFach = JsonNullable.of(null);

		assertThatThrownBy(() -> service.patch(1L, request)).isInstanceOf(ApiOperationException.class)
				.hasMessage("Die ID des Faches darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch: Ein unbekanntes Fach führt zu 404 mit der ID in der Meldung")
	void patch_fachUnbekannt() {
		mockKursFuerPatch();
		final var request = new KursPatchRequest();
		request.idFach = JsonNullable.of(8L);
		when(fachRepository.findById(8L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.patch(1L, request)).isInstanceOf(ApiOperationException.class)
				.hasMessage("Es konnte kein Fach mit der ID 8 gefunden werden.")
				.hasFieldOrPropertyWithValue("status", Status.NOT_FOUND);
	}

	@Test
	@DisplayName("patch: Ein unbekannter Lehrer führt zu 404")
	void patch_lehrerUnbekannt() {
		mockKursFuerPatch();
		final var request = new KursPatchRequest();
		request.idLehrer = JsonNullable.of(7L);
		when(lehrerRepository.existsById(7L)).thenReturn(false);

		assertThatThrownBy(() -> service.patch(1L, request)).isInstanceOf(ApiOperationException.class)
				.hasMessage("Es konnte kein Lehrer mit der angegebenen ID 7 gefunden werden.")
				.hasFieldOrPropertyWithValue("status", Status.NOT_FOUND);
	}

	@Test
	@DisplayName("patch: Eine unbekannte Schulnummer führt zu 404, die Prüfung nutzt die Schulnummer als Text")
	void patch_schulnummerUnbekannt() {
		mockKursFuerPatch();
		final var request = new KursPatchRequest();
		request.schulnummer = JsonNullable.of(123456);
		when(schuleRepository.existsBySchulnummer("123456")).thenReturn(false);

		assertThatThrownBy(() -> service.patch(1L, request)).isInstanceOf(ApiOperationException.class)
				.hasMessage("Für die Schulnummer 123456 konnte keine passende Schule gefunden werden.")
				.hasFieldOrPropertyWithValue("status", Status.NOT_FOUND);
		verify(schuleRepository).existsBySchulnummer("123456");
	}

	@Test
	@DisplayName("patch: Ein unbekannter Jahrgang führt zu 400")
	void patch_jahrgangUnbekannt() {
		mockKursFuerPatch();
		final var request = new KursPatchRequest();
		request.idJahrgaenge = JsonNullable.of(List.of(4L));
		when(jahrgangRepository.findMapByIds(List.of(4L))).thenReturn(Map.of());

		assertThatThrownBy(() -> service.patch(1L, request)).isInstanceOf(ApiOperationException.class)
				.hasMessage("Mindestens einer der angegebenen Jahrgang-IDs existiert nicht in der SVWS-Datenbank")
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch: Ein Jahrgang setzt Jahrgang_ID und ASDJahrgang und leert die Liste")
	void patch_einJahrgang() {
		final var kurs = mockKursFuerPatch();
		kurs.Jahrgaenge = "5,6";
		final var jahrgang = new DTOJahrgang(4L);
		jahrgang.ASDJahrgang = "05";
		final var request = new KursPatchRequest();
		request.idJahrgaenge = JsonNullable.of(List.of(4L));
		when(jahrgangRepository.findMapByIds(List.of(4L))).thenReturn(Map.of(4L, jahrgang));

		service.patch(1L, request);

		assertThat(kurs)
				.hasFieldOrPropertyWithValue("Jahrgang_ID", 4L)
				.hasFieldOrPropertyWithValue("ASDJahrgang", "05")
				.hasFieldOrPropertyWithValue("Jahrgaenge", null);
	}

	@Test
	@DisplayName("patch: Bei geändertem Lehrer wird nur bei abweichenden Leistungsdaten der Fachlehrer angepasst")
	void patch_fachlehrerSeiteneffekt() {
		final var kurs = mockKursFuerPatch();
		final var request = new KursPatchRequest();
		request.idLehrer = JsonNullable.of(7L);
		when(lehrerRepository.existsById(7L)).thenReturn(true);
		final var abweichend = new DTOSchuelerLeistungsdaten(100L, 1L, 3L);
		abweichend.Fachlehrer_ID = 5L;
		final var gleich = new DTOSchuelerLeistungsdaten(101L, 1L, 3L);
		gleich.Fachlehrer_ID = 7L;
		when(schuelerLeistungsdatenRepository.findListByKurs(1L)).thenReturn(List.of(abweichend, gleich));

		service.patch(1L, request);

		verify(schuelerLeistungsdatenRepository).update(leistungsdatenCaptor.capture());
		assertThat(kurs.Lehrer_ID).isEqualTo(7L);
		assertThat(leistungsdatenCaptor.getValue()).containsExactly(abweichend);
		assertThat(abweichend.Fachlehrer_ID).isEqualTo(7L);
	}

	@Test
	@DisplayName("patch: Stimmt der Fachlehrer überall überein, wird nichts gespeichert")
	void patch_fachlehrerUnveraendert() {
		mockKursFuerPatch();
		final var request = new KursPatchRequest();
		request.idLehrer = JsonNullable.of(7L);
		when(lehrerRepository.existsById(7L)).thenReturn(true);
		final var gleich = new DTOSchuelerLeistungsdaten(101L, 1L, 3L);
		gleich.Fachlehrer_ID = 7L;
		when(schuelerLeistungsdatenRepository.findListByKurs(1L)).thenReturn(List.of(gleich));

		service.patch(1L, request);

		verify(schuelerLeistungsdatenRepository, never()).update(ArgumentMatchers.<List<DTOSchuelerLeistungsdaten>>any());
	}

	@Test
	@DisplayName("getById: Der Kurs wird mit Schülern und weiteren Lehrern geliefert")
	void getById_liefertKursMitSchuelernUndLehrern() {
		final var lehrer = new DTOKursLehrer(1L, 20L);
		lehrer.Anteil = 1.0;
		when(kurseRepository.findById(1L)).thenReturn(Optional.of(createKurs(1L, 5)));
		when(kurslehrerRepository.getMapZusatzkraefte(List.of(1L))).thenReturn(Map.of(1L, List.of(lehrer)));
		mockSchueler(1L, createSchueler(100L, false));

		final var result = service.getById(1L);

		assertThat(result)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("kuerzel", "K1")
				.satisfies(k -> assertThat(k.schueler).extracting(s -> s.id).containsExactly(100L))
				.satisfies(k -> assertThat(k.weitereLehrer).extracting(l -> l.idLehrer).containsExactly(20L));
	}

	@Test
	@DisplayName("getById: Ein Kurs ohne Schüler und Zusatzkräfte hat leere Listen")
	void getById_kursOhneSchueler() {
		when(kurseRepository.findById(1L)).thenReturn(Optional.of(createKurs(1L, 5)));
		when(kurslehrerRepository.getMapZusatzkraefte(List.of(1L))).thenReturn(Map.of(1L, List.of()));
		when(kursSchuelerRepository.getMapByKurse(List.of(1L))).thenReturn(Map.of(1L, List.of()));
		when(schuelerRepository.findMapByIds(List.of())).thenReturn(Map.of());

		final var result = service.getById(1L);

		assertThat(result)
				.satisfies(k -> assertThat(k.schueler).isEmpty())
				.satisfies(k -> assertThat(k.weitereLehrer).isEmpty());
	}

	@Test
	@DisplayName("getById: Eine unbekannte ID führt zu 404 mit deutscher Meldung")
	void getById_unbekannteId() {
		when(kurseRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.getById(99L)).isInstanceOf(ApiOperationException.class)
				.hasMessage("Kein Kurs mit der ID 99 gefunden.")
				.hasFieldOrPropertyWithValue("status", Status.NOT_FOUND);
	}

	@Test
	@DisplayName("getListBySchuljahresabschnitt: Die Kurse des Abschnitts werden nach Sortierung sortiert geliefert")
	void getListBySchuljahresabschnitt_sortiertNachSortierung() {
		when(kurseRepository.getListBySchuljahresabschnitt(10L))
				.thenReturn(List.of(createKurs(1L, 30), createKurs(2L, 10), createKurs(3L, 20)));
		when(kurslehrerRepository.getMapZusatzkraefte(List.of(1L, 2L, 3L))).thenReturn(Map.of());
		when(jahrgangRepository.getAll()).thenReturn(List.of());

		final var result = service.getListBySchuljahresabschnitt(10L, false);

		assertThat(result).extracting(k -> k.id).containsExactly(2L, 3L, 1L);
		verifyNoInteractions(kursSchuelerRepository, schuelerRepository);
	}

	@Test
	@DisplayName("getListBySchuljahresabschnitt: Ohne Abschnitt werden die Kurse aller Abschnitte geliefert")
	void getListBySchuljahresabschnitt_abschnittNullLiefertAlleKurse() {
		when(kurseRepository.getAll()).thenReturn(List.of(createKurs(1L, 1)));
		when(kurslehrerRepository.getMapZusatzkraefte(List.of(1L))).thenReturn(Map.of());
		when(jahrgangRepository.getAll()).thenReturn(List.of());

		assertThat(service.getListBySchuljahresabschnitt(null, false)).extracting(k -> k.id).containsExactly(1L);
		verify(kurseRepository, never()).getListBySchuljahresabschnitt(anyLong());
	}

	@Test
	@DisplayName("getListBySchuljahresabschnitt: Ohne Kurse ist das Ergebnis eine leere Liste")
	void getListBySchuljahresabschnitt_keineKurse() {
		when(kurseRepository.getListBySchuljahresabschnitt(10L)).thenReturn(List.of());
		when(jahrgangRepository.getAll()).thenReturn(List.of());

		assertThat(service.getListBySchuljahresabschnitt(10L, true)).isEmpty();
		verifyNoInteractions(kurslehrerRepository, kursSchuelerRepository, schuelerRepository);
	}

	@Test
	@DisplayName("getListBySchuljahresabschnitt: Nicht mehr existierende Jahrgangs-IDs werden entfernt")
	void getListBySchuljahresabschnitt_entferntUnbekannteJahrgaenge() {
		final var kurs = createKurs(1L, 1);
		kurs.Jahrgang_ID = 4L;
		kurs.Jahrgaenge = "5,6";
		when(kurseRepository.getListBySchuljahresabschnitt(10L)).thenReturn(List.of(kurs));
		when(kurslehrerRepository.getMapZusatzkraefte(List.of(1L))).thenReturn(Map.of());
		when(jahrgangRepository.getAll()).thenReturn(List.of(new DTOJahrgang(4L), new DTOJahrgang(6L)));

		assertThat(service.getListBySchuljahresabschnitt(10L, false)).singleElement()
				.satisfies(k -> assertThat(k.idJahrgaenge).containsExactly(4L, 6L));
	}

	@Test
	@DisplayName("getListBySchuljahresabschnitt: Die weiteren Lehrer aller Kurse werden mit einer einzigen Abfrage bestimmt")
	void getListBySchuljahresabschnitt_zusatzkraefteMitEinerAbfrage() {
		final var lehrer1 = new DTOKursLehrer(1L, 20L);
		final var lehrer2 = new DTOKursLehrer(2L, 21L);
		when(kurseRepository.getListBySchuljahresabschnitt(10L)).thenReturn(List.of(createKurs(1L, 1), createKurs(2L, 2)));
		when(kurslehrerRepository.getMapZusatzkraefte(List.of(1L, 2L)))
				.thenReturn(Map.of(1L, List.of(lehrer1), 2L, List.of(lehrer2)));
		when(jahrgangRepository.getAll()).thenReturn(List.of());

		final var result = service.getListBySchuljahresabschnitt(10L, false);

		assertThat(result).extracting(k -> k.weitereLehrer.getFirst().idLehrer).containsExactly(20L, 21L);
		verify(kurslehrerRepository, times(1)).getMapZusatzkraefte(anyCollection());
	}

	@Test
	@DisplayName("getListBySchuljahresabschnitt: Schüler mit Löschvermerk true werden ausgeschlossen, null zählt wie false")
	void getListBySchuljahresabschnitt_beruecksichtigtLoeschvermerk() {
		when(kurseRepository.getListBySchuljahresabschnitt(10L)).thenReturn(List.of(createKurs(1L, 1)));
		when(kurslehrerRepository.getMapZusatzkraefte(List.of(1L))).thenReturn(Map.of());
		when(jahrgangRepository.getAll()).thenReturn(List.of());
		mockSchueler(1L, createSchueler(100L, true), createSchueler(101L, false), createSchueler(102L, null));

		final var result = service.getListBySchuljahresabschnitt(10L, true);

		assertThat(result).singleElement()
				.satisfies(k -> assertThat(k.schueler).extracting(s -> s.id).containsExactly(101L, 102L));
	}

	@Test
	@DisplayName("getListByIds: Die Kurse zu den IDs werden ohne Sortierung geliefert")
	void getListByIds_liefertKurse() {
		when(kurseRepository.findListByIds(List.of(2L, 1L))).thenReturn(List.of(createKurs(2L, 20), createKurs(1L, 10)));
		when(kurslehrerRepository.getMapZusatzkraefte(List.of(2L, 1L))).thenReturn(Map.of());

		final var result = service.getListByIds(List.of(2L, 1L), false);

		assertThat(result).extracting(k -> k.id).containsExactly(2L, 1L);
	}

	@Test
	@DisplayName("getListByIds: Mit Schülern werden diese zu den Kursen ergänzt")
	void getListByIds_mitSchuelern() {
		when(kurseRepository.findListByIds(List.of(1L))).thenReturn(List.of(createKurs(1L, 10)));
		when(kurslehrerRepository.getMapZusatzkraefte(List.of(1L))).thenReturn(Map.of());
		mockSchueler(1L, createSchueler(100L, false));

		assertThat(service.getListByIds(List.of(1L), true)).singleElement()
				.satisfies(k -> assertThat(k.schueler).extracting(s -> s.id).containsExactly(100L));
	}

	@Test
	@DisplayName("getListByIds: Eine leere oder fehlende ID-Liste ergibt eine leere Liste ohne Datenbankzugriff")
	void getListByIds_leereIdListe() {
		assertThat(service.getListByIds(List.of(), true)).isEmpty();
		assertThat(service.getListByIds(null, true)).isEmpty();
		verifyNoInteractions(kurseRepository, kurslehrerRepository, kursSchuelerRepository, schuelerRepository);
	}

	@Test
	@DisplayName("getListByIds: Unbekannte IDs ergeben eine leere Liste")
	void getListByIds_unbekannteIds() {
		when(kurseRepository.findListByIds(List.of(99L))).thenReturn(List.of());

		assertThat(service.getListByIds(List.of(99L), true)).isEmpty();
	}

}
