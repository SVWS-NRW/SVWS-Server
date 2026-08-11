package de.svws_nrw.service.lehrer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Set;

import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenRepository;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAnrechnungsstunde;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.repo.lehrer.anrechnung.LehrerAnrechnungRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests für LehrerAnrechnungsstundenServiceKontext")
class LehrerAnrechnungsstundenServiceKontextTest {

	@Mock
	private SchuljahresabschnitteRepository repoSchuljahr;
	@Mock
	private LehrerPersonalabschnittsdatenRepository repoAbschnitt;
	@Mock
	private LehrerAnrechnungRepository repoAnrechnung;

	@InjectMocks
	private LehrerAnrechnungsstundenServiceKontext kontext;


	private void mockSetup(final long idAnrechnungsstunde) {
		final var anrechnungsstunde = new DTOLehrerAnrechnungsstunde(idAnrechnungsstunde, 100L);
		final var abschnitt = new DTOLehrerAbschnittsdaten(100L, 42L, 200L);
		final var schuljahresabschnitt = new DTOSchuljahresabschnitte(200L, 2024, 1);

		// Gibt jeweils die Daten zurück
		lenient().when(repoAnrechnung.findListByIds(List.of(idAnrechnungsstunde))).thenReturn(List.of(anrechnungsstunde));
		lenient().when(repoAbschnitt.findListByIds(Set.of(100L))).thenReturn(List.of(abschnitt));
		lenient().when(repoSchuljahr.getAll()).thenReturn(List.of(schuljahresabschnitt));
	}


	@Test
	@DisplayName("of: Erstellt eine neue Instanz des Kontextes mit den Repositories")
	void of_createsInstance() {
		final long idAnrechnung = 1L;
		final long idAbschnitt = 100L;
		mockSetup(idAnrechnung);

		final var newKontext = LehrerAnrechnungsstundenServiceKontext.of(repoSchuljahr, repoAbschnitt, repoAnrechnung);

		assertThat(newKontext).isNotNull();

		final var result = newKontext.fetch(List.of(idAnrechnung));

		assertThat(result).hasSize(1);
		verify(repoAnrechnung).findListByIds(List.of(idAnrechnung));
		verify(repoAbschnitt).findListByIds(Set.of(idAbschnitt));
		verify(repoSchuljahr).getAll();
	}


	@Test
	@DisplayName("fetch: Lädt Anrechnungsstunden und kaskadiert korrekt zu Abschnitten und Schuljahren")
	void fetch() {
		mockSetup(1L);
		final var result = kontext.fetch(List.of(1L));

		assertThat(result).hasSize(1);
		assertThat(result.getFirst().ID).isEqualTo(1L);
		final var abschnitt = kontext.getAbschnitt(100L);
		assertThat(abschnitt).isNotNull();
		assertThat(abschnitt.Schuljahresabschnitts_ID).isEqualTo(200L);
		assertThat(kontext.getSchuljahresabschnitt(200L)).isNotNull();

		verify(repoAnrechnung).findListByIds(anyList());
		verify(repoAbschnitt).findListByIds(anySet());
		verify(repoSchuljahr).getAll();
	}

	@Test
	@DisplayName("create: Erzeugt neue DTOs mit den IDs und fügt diese zum Cache hinzu")
	void create() {
		mockSetup(500L);
		when(repoAnrechnung.getNextID()).thenReturn(500L);

		final var patch = new LehrerAnrechnungsstundenCreateRequest();
		patch.idAbschnittsdaten = 100L;
		patch.idGrund = 310000L;
		patch.anzahl = 2.0;

		final var list = kontext.create(List.of(patch));

		assertThat(list).hasSize(1);
		assertThat(list.getFirst().ID).isEqualTo(500L);

		verify(repoAbschnitt).findListByIds(anySet());
		verify(repoSchuljahr).getAll();
		verify(repoAnrechnung).flush();
	}


	@Test
	@DisplayName("fetchByLehrerabschnittsdatenId: Lädt über Abschnitts-ID und befüllt den Cache manuell")
	void fetchByLehrerabschnittsdatenId() {
		final long idAnrechnungsstunde = 1L;
		final long idAbschnitt = 100L;
		mockSetup(idAnrechnungsstunde);

		final var anrechnungsstunde = new DTOLehrerAnrechnungsstunde(idAnrechnungsstunde, idAbschnitt);
		when(repoAnrechnung.getMapByAbschnitt(List.of(idAbschnitt)))
				.thenReturn(Map.of(idAbschnitt, List.of(anrechnungsstunde)));

		final var result = kontext.fetchByLehrerabschnittsdatenId(idAbschnitt);

		assertThat(result).hasSize(1);
		assertThat(result.getFirst().ID).isEqualTo(idAnrechnungsstunde);
		assertThat(kontext.getAnrechnungsstunden(idAnrechnungsstunde)).isNotNull();
	}

	@Test
	@DisplayName("persist: Delegiert den gesamten Cache-Inhalt an das Repository-Update")
	void persistEntities() {
		final long id = 1L;
		mockSetup(id);
		final var loaded = kontext.fetch(List.of(id));

		kontext.persist(loaded);

		verify(repoAnrechnung).update(loaded);
		verify(repoAnrechnung).flush();
	}

	@Test
	@DisplayName("delete: Löscht alle Entitäten, die sich aktuell im Cache befinden")
	void deleteEntities() {
		final long id = 1L;
		mockSetup(id);

		final var loaded = kontext.fetch(List.of(id));

		kontext.delete(loaded);

		verify(repoAnrechnung).delete(loaded);
	}

	@Test
	@DisplayName("fetch: wirft NOT_FOUND wenn nicht alle IDs gefunden werden")
	void fetch_throwsNotFound_whenNotAllFound() {
		final long requested1 = 1L;
		final long requested2 = 2L;

		// Repo liefert nur einen Treffer
		when(repoAnrechnung.findListByIds(List.of(requested1, requested2)))
				.thenReturn(List.of(new DTOLehrerAnrechnungsstunde(requested1, 100L)));

		assertThatException()
				.isThrownBy(() -> kontext.fetch(List.of(requested1, requested2)))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);

		// preFetch darf dann nicht weiterkaskadieren (weil Exception vorher)
		verify(repoAbschnitt, never()).findListByIds(anySet());
		verify(repoSchuljahr, never()).getAll();
	}

	@Test
	@DisplayName("fetch: wirft INTERNAL_SERVER_ERROR wenn Abschnittsdaten fehlen")
	void fetch_throwsInternalServerError_whenAbschnittMissing() {
		final var anr = new DTOLehrerAnrechnungsstunde(1L, 100L);

		when(repoAnrechnung.findListByIds(List.of(1L))).thenReturn(List.of(anr));
		// absichtlich: Abschnitt nicht gefunden
		when(repoAbschnitt.findListByIds(Set.of(100L))).thenReturn(List.of());

		assertThatException()
				.isThrownBy(() -> kontext.fetch(List.of(1L)))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.INTERNAL_SERVER_ERROR);

		verify(repoSchuljahr, never()).getAll();
	}

	@Test
	@DisplayName("create: vergibt fortlaufende IDs und hält Reihenfolge der Requests")
	void create_multiple_assignsIncrementingIds_inOrder() {
		// zwei unterschiedliche Abschnitt-IDs, damit preFetchByAbschnitt/Abschnittsdaten geprüft werden kann
		final var abs1 = new DTOLehrerAbschnittsdaten(100L, 42L, 200L);
		final var abs2 = new DTOLehrerAbschnittsdaten(101L, 42L, 200L);
		when(repoAbschnitt.findListByIds(Set.of(100L, 101L))).thenReturn(List.of(abs1, abs2));
		when(repoSchuljahr.getAll()).thenReturn(List.of(new DTOSchuljahresabschnitte(200L, 2024, 1)));

		when(repoAnrechnung.getNextID()).thenReturn(500L);

		final var p1 = new LehrerAnrechnungsstundenCreateRequest();
		p1.idAbschnittsdaten = 100L;
		p1.idGrund = 1L;
		p1.anzahl = 1.0;

		final var p2 = new LehrerAnrechnungsstundenCreateRequest();
		p2.idAbschnittsdaten = 101L;
		p2.idGrund = 2L;
		p2.anzahl = 2.0;

		final var result = kontext.create(List.of(p1, p2));

		assertThat(result).hasSize(2);
		assertThat(result.get(0).ID).isEqualTo(500L);
		assertThat(result.get(0).Abschnitt_ID).isEqualTo(100L);
		assertThat(result.get(1).ID).isEqualTo(501L);
		assertThat(result.get(1).Abschnitt_ID).isEqualTo(101L);

		verify(repoAbschnitt).findListByIds(Set.of(100L, 101L));
		verify(repoSchuljahr).getAll();
		verify(repoAnrechnung).flush();
	}

	@Test
	@DisplayName("fetchMapByAbschnittIds: delegiert ans Repository und prefetch't Abschnitt + Schuljahr")
	void fetchMapByAbschnittIds_delegatesAndPrefetches() {
		final var ids = List.of(100L, 200L);

		when(repoAnrechnung.getListByIdLehrerAbschnittsdaten(ids)).thenReturn(Map.of(
				100L, List.of(new DTOLehrerAnrechnungsstunde(1L, 100L)),
				200L, List.of()
		));

		// preFetchByAbschnittIds lädt Abschnittsdaten und Schuljahresabschnitte
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
		verify(repoAnrechnung).getListByIdLehrerAbschnittsdaten(ids);
		verify(repoAbschnitt).findListByIds(ids);
		verify(repoSchuljahr).getAll();
	}


}
