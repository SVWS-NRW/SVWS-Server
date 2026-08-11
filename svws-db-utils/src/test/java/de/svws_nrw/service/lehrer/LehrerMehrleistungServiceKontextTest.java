package de.svws_nrw.service.lehrer;

import java.util.List;
import java.util.Map;
import java.util.Set;

import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerMehrleistung;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.lehrer.mehrleistung.LehrerMehrleistungRepository;
import de.svws_nrw.repo.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests für LehrerMehrleistungServiceKontext")
class LehrerMehrleistungServiceKontextTest {

	@Mock
	private SchuljahresabschnitteRepository repoSchuljahr;
	@Mock
	private LehrerPersonalabschnittsdatenRepository repoAbschnitt;
	@Mock
	private LehrerMehrleistungRepository repoMehrleistung;

	private LehrerMehrleistungServiceKontext kontext;

	@BeforeEach
	void setupKontext() {
		kontext = LehrerMehrleistungServiceKontext.of(repoSchuljahr, repoAbschnitt, repoMehrleistung);
	}

	private void mockSetup(final long idMehrleistung) {
		final var mehrleistung = new DTOLehrerMehrleistung(idMehrleistung, 100L, "160");
		final var abschnitt = new DTOLehrerAbschnittsdaten(100L, 42L, 200L);
		final var schuljahresabschnitt = new DTOSchuljahresabschnitte(200L, 2024, 1);

		lenient().when(repoMehrleistung.findListByIds(List.of(idMehrleistung))).thenReturn(List.of(mehrleistung));
		lenient().when(repoAbschnitt.findListByIds(Set.of(100L))).thenReturn(List.of(abschnitt));
		lenient().when(repoSchuljahr.getAll()).thenReturn(List.of(schuljahresabschnitt));
	}

	@Test
	@DisplayName("of: Erstellt eine neue Instanz des Kontextes mit den Repositories")
	void ofCreatesInstance() {
		final long idMehrleistung = 1L;
		final long idAbschnitt = 100L;
		mockSetup(idMehrleistung);

		final var newKontext = LehrerMehrleistungServiceKontext.of(repoSchuljahr, repoAbschnitt, repoMehrleistung);

		assertThat(newKontext).isNotNull();

		final var result = newKontext.fetch(List.of(idMehrleistung));

		assertThat(result).hasSize(1);
		verify(repoMehrleistung).findListByIds(List.of(idMehrleistung));
		verify(repoAbschnitt).findListByIds(Set.of(idAbschnitt));
		verify(repoSchuljahr).getAll();
	}

	@Test
	@DisplayName("fetch: Lädt Mehrleistungen und kaskadiert korrekt zu Abschnitten und Schuljahren")
	void fetch() {
		mockSetup(1L);
		final var result = kontext.fetch(List.of(1L));

		assertThat(result).hasSize(1);
		assertThat(result.getFirst().id).isEqualTo(1L);
		final var abschnitt = kontext.getLehrerAbschnitt(100L);
		assertThat(abschnitt).isNotNull();
		assertThat(abschnitt.Schuljahresabschnitts_ID).isEqualTo(200L);
		assertThat(kontext.getSchuljahresabschnitt(200L)).isNotNull();

		verify(repoMehrleistung).findListByIds(anyList());
		verify(repoAbschnitt).findListByIds(anySet());
		verify(repoSchuljahr).getAll();
	}

	@Test
	@DisplayName("create: Erzeugt neue DTOs mit den IDs und fügt diese zum Cache hinzu")
	void create() {
		mockSetup(500L);
		when(repoMehrleistung.getNextID()).thenReturn(500L);

		final var patch = new LehrerMehrleistungCreateRequest();
		patch.idAbschnittsdaten = 100L;
		patch.idGrund = 2L;
		patch.anzahl = 2.0;

		final var list = kontext.create(List.of(patch));

		assertThat(list).hasSize(1);
		assertThat(list.getFirst().id).isEqualTo(500L);

		verify(repoAbschnitt).findListByIds(anySet());
		verify(repoSchuljahr).getAll();
		verify(repoMehrleistung).flush();
	}

	@Test
	@DisplayName("fetchByLehrerabschnittsdatenId: Lädt über Abschnitts-ID und befüllt den Cache manuell")
	void fetchByLehrerabschnittsdatenId() {
		final long idMehrleistung = 1L;
		final long idAbschnitt = 100L;
		mockSetup(idMehrleistung);

		final var mehrleistung = new DTOLehrerMehrleistung(idMehrleistung, idAbschnitt, "");
		when(repoMehrleistung.getMapByIdsLehrerAbschnittsdaten(List.of(idAbschnitt)))
				.thenReturn(Map.of(idAbschnitt, List.of(mehrleistung)));

		final var result = kontext.fetchByLehrerabschnittsdatenId(idAbschnitt);

		assertThat(result).hasSize(1);
		assertThat(result.getFirst().id).isEqualTo(idMehrleistung);
		assertThat(kontext.getMehrleistung(idMehrleistung)).isNotNull();
	}

	@Test
	@DisplayName("persist: Delegiert den gesamten Cache-Inhalt an das Repository-Update")
	void persistEntities() {
		final long id = 1L;
		mockSetup(id);
		final var loaded = kontext.fetch(List.of(id));

		kontext.persist(loaded);

		verify(repoMehrleistung).update(loaded);
		verify(repoMehrleistung).flush();
	}

	@Test
	@DisplayName("delete: Löscht alle Entitäten, die sich aktuell im Cache befinden")
	void deleteEntities() {
		final long id = 1L;
		mockSetup(id);

		final var loaded = kontext.fetch(List.of(id));

		kontext.delete(loaded);

		verify(repoMehrleistung).delete(loaded);
	}

	@Test
	@DisplayName("fetch: wirft NOT_FOUND wenn nicht alle IDs gefunden werden")
	void fetch_throwsNotFound_whenNotAllFound() {
		when(repoMehrleistung.findListByIds(List.of(1L, 2L)))
				.thenReturn(List.of(new DTOLehrerMehrleistung(1L, 100L, "160")));

		assertThatException().isThrownBy(() -> kontext.fetch(List.of(1L, 2L)))
				.isExactlyInstanceOf(ApiOperationException.class)
				.satisfies(e -> assertThat(((ApiOperationException) e).getStatus()).isEqualTo(Response.Status.NOT_FOUND));

		verify(repoAbschnitt, never()).findListByIds(anySet());
		verify(repoSchuljahr, never()).getAll();
	}

	@Test
	@DisplayName("fetch: wirft INTERNAL_SERVER_ERROR wenn Abschnittsdaten fehlen")
	void fetch_throwsInternalServerError_whenAbschnittMissing() {
		final var mehrleistung = new DTOLehrerMehrleistung(1L, 100L, "160");

		when(repoMehrleistung.findListByIds(List.of(1L))).thenReturn(List.of(mehrleistung));
		// absichtlich: Abschnitt nicht gefunden
		when(repoAbschnitt.findListByIds(Set.of(100L))).thenReturn(List.of());

		assertThatException().isThrownBy(() -> kontext.fetch(List.of(1L)))
				.isExactlyInstanceOf(ApiOperationException.class)
				.satisfies(e -> assertThat(((ApiOperationException) e).getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR));

		verify(repoSchuljahr, never()).getAll();
	}

	@Test
	@DisplayName("fetchMapByAbschnittIds: delegiert und prefetch't Abschnitt + Schuljahr")
	void fetchMapByAbschnittIds_delegatesAndPrefetches() {
		final var ids = List.of(100L, 200L);

		when(repoMehrleistung.getListByIdLehrerAbschnittsdaten(ids)).thenReturn(Map.of(
				100L, List.of(new DTOLehrerMehrleistung(1L, 100L, "160")),
				200L, List.of()
		));

		when(repoAbschnitt.findListByIds(ids)).thenReturn(List.of(
				new DTOLehrerAbschnittsdaten(100L, 42L, 300L),
				new DTOLehrerAbschnittsdaten(200L, 42L, 301L)
		));
		when(repoSchuljahr.getAll()).thenReturn(List.of(
				new DTOSchuljahresabschnitte(300L, 2024, 1),
				new DTOSchuljahresabschnitte(301L, 2025, 1)
		));

		final var result = kontext.fetchMapByAbschnittIds(ids);

		assertThat(result).containsKeys(100L, 200L);

		// Cache wurde befüllt (Abschnitt & Schuljahr)
		assertThat(kontext.getLehrerAbschnitt(100L)).isNotNull();
		assertThat(kontext.getSchuljahresabschnitt(300L)).isNotNull();

		verify(repoMehrleistung).getListByIdLehrerAbschnittsdaten(ids);
		verify(repoAbschnitt).findListByIds(ids);
		verify(repoSchuljahr).getAll();
	}

	@Test
	@DisplayName("fetchByLehrerabschnittsdatenId: kein Treffer -> leere Liste, Cache leer")
	void fetchByLehrerabschnittsdatenId_noResult_returnsEmpty() {
		final long idAbschnitt = 100L;

		when(repoMehrleistung.getMapByIdsLehrerAbschnittsdaten(List.of(idAbschnitt))).thenReturn(Map.of());
		when(repoAbschnitt.findListByIds(Set.of())).thenReturn(List.of());
		when(repoSchuljahr.getAll()).thenReturn(List.of());

		final var result = kontext.fetchByLehrerabschnittsdatenId(idAbschnitt);

		assertThat(result).isEmpty();
		assertThat(kontext.getMehrleistung(1L)).isNull();
		assertThat(kontext.getLehrerAbschnitt(idAbschnitt)).isNull();

		verify(repoMehrleistung).getMapByIdsLehrerAbschnittsdaten(List.of(idAbschnitt));
		verify(repoAbschnitt).findListByIds(Set.of());
		verify(repoSchuljahr).getAll();
	}

}
