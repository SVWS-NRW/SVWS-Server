package de.svws_nrw.service.kurse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursLehrer;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.kurse.KurseRepository;
import de.svws_nrw.repo.kurse.KursSchuelerRepository;
import de.svws_nrw.repo.kurse.KurslehrerRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schule.kataloge.jahrgang.JahrgangRepository;
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

	private KursService service;

	@BeforeEach
	void setUp() {
		service = new KursService(kurseRepository, kursSchuelerRepository, kurslehrerRepository, schuelerRepository, jahrgangRepository);
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
