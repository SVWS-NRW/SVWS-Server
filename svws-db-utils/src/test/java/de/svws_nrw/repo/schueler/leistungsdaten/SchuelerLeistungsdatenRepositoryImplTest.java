package de.svws_nrw.repo.schueler.leistungsdaten;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;

@ExtendWith(MockitoExtension.class)
class SchuelerLeistungsdatenRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private SchuelerLeistungsdatenRepositoryImpl repository;

	@Test
	@DisplayName("Test: Erstelle einen neuen Eintrag und prüfe die Zuweisung der neuen ID.")
	void testCreate() {
		final long idAlt = -1L;
		final long idAbschnitt = 4711L;
		final long idFach = 77L;
		final DTOSchuelerLeistungsdaten neu = new DTOSchuelerLeistungsdaten(idAlt, idAbschnitt, idFach);

		final long idNeu = 42L;

		when(conn.transactionGetNextID(DTOSchuelerLeistungsdaten.class)).thenReturn(idNeu);
		when(conn.transactionPersist(neu)).thenReturn(true);

		final DTOSchuelerLeistungsdaten result = repository.create(neu);

		assertNotNull(result);
		assertEquals(idNeu, result.ID, "Die ID wurde nicht korrekt gesetzt.");
		verify(conn).transactionGetNextID(DTOSchuelerLeistungsdaten.class);
		verify(conn).transactionPersist(neu);
	}

	@Test
	@DisplayName("Test: Der Konstruktor setzt die ID-Zugriffe (getId/setId) korrekt auf das ID-Feld.")
	void testConstructorAndIdAccess() {
		// Teste getId indirekt via getMap()
		final DTOSchuelerLeistungsdaten k = new DTOSchuelerLeistungsdaten(999L, 24L, 42L);
		when(conn.queryAll(DTOSchuelerLeistungsdaten.class)).thenReturn(List.of(k));
		assertThat(repository.getMap()).containsEntry(999L, k);

		// Teste setId über die create-Methode
		when(conn.transactionGetNextID(DTOSchuelerLeistungsdaten.class)).thenReturn(1000L);
		when(conn.transactionPersist(k)).thenReturn(true);
		repository.create(k);
		assertThat(k.ID).isEqualTo(1000L);
	}


	@Test
	@DisplayName("Test: findListByLernabschnitt(ids) bestimmt die Liste der Leistungsdaten für Lernabschnitte.")
	void testFindListByLernabschnitt() {
		// Fall: Keine Lernabschnitt-IDs übergeben
		assertThat(repository.findListByLernabschnitt(Collections.emptyList())).isEmpty();
		verifyNoInteractions(conn);

		// Fall: Es gibt Lernabschnitt-IDs und ein Datensatz wird gefunden
		final Collection<Long> abschnittIds = List.of(1L, 2L);
		final DTOSchuelerLeistungsdaten d1 = new DTOSchuelerLeistungsdaten(100L, 1L, 42L);

		when(conn.queryList(DTOSchuelerLeistungsdaten.QUERY_LIST_BY_ABSCHNITT_ID, DTOSchuelerLeistungsdaten.class, abschnittIds))
				.thenReturn(List.of(d1));

		final List<DTOSchuelerLeistungsdaten> result = repository.findListByLernabschnitt(abschnittIds);

		assertThat(result).containsExactly(d1);
	}

	@Test
	@DisplayName("Test: findListByLernabschnittAndFachlehrer(ids, lehrerIds) bestimmt die Liste der Leistungsdaten für Lernabschnitte.")
	void testFindListByLernabschnittAndFachlehrer() {
		// Fälle: Liste der Lernabschnitt-IDs oder der Fachlehrer-IDs leer
		assertThat(repository.findListByLernabschnittAndFachlehrer(Collections.emptyList(), List.of(1L))).isEmpty();
		assertThat(repository.findListByLernabschnittAndFachlehrer(List.of(1L), Collections.emptyList())).isEmpty();
		verifyNoInteractions(conn);

		// Fall: Es werden Lernabschnitts-IDs und Fachlehrer-IDs angegeben und es wird ein Fachlehrer-Datensatz gefunden
		final Collection<Long> idsLernabschnitte = List.of(10L);
		final Collection<Long> idsFachlehrer = List.of(5L);
		final DTOSchuelerLeistungsdaten d1 = new DTOSchuelerLeistungsdaten(100L, 1L, 42L);
		d1.Fachlehrer_ID = 5L;

		final String expectedQuery = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Abschnitt_ID IN ?1 AND e.Fachlehrer_ID IN ?2";

		when(conn.queryList(expectedQuery, DTOSchuelerLeistungsdaten.class, idsLernabschnitte, idsFachlehrer)).thenReturn(List.of(d1));

		final List<DTOSchuelerLeistungsdaten> result = repository.findListByLernabschnittAndFachlehrer(idsLernabschnitte, idsFachlehrer);

		assertThat(result).containsExactly(d1);
	}

	@Test
	@DisplayName("Test: getMapByLernabschnittsIds(ids) liefert eine 2D-Map (Abschnitt_ID, Fach_ID) -> DTOSchuelerLeistungsdaten.")
	void testGetMapByLernabschnittsIds() {
		// Fall: Keine IDs uebergeben -> leere Map ohne Datenbankzugriff
		final HashMap2D<Long, Long, DTOSchuelerLeistungsdaten> emptyResult = repository.getMapByLernabschnittsIds(Collections.emptyList());
		assertNotNull(emptyResult);
		assertEquals(0, emptyResult.size());
		verifyNoInteractions(conn);

		// Fall: IDs vorhanden -> DTOs werden anhand (Abschnitt_ID, Fach_ID) in die 2D-Map übernommen
		final Collection<Long> abschnittIds = List.of(1L, 2L);
		final DTOSchuelerLeistungsdaten d1 = new DTOSchuelerLeistungsdaten(100L, 1L, 42L);
		final DTOSchuelerLeistungsdaten d2 = new DTOSchuelerLeistungsdaten(101L, 2L, 43L);

		when(conn.queryList(DTOSchuelerLeistungsdaten.QUERY_LIST_BY_ABSCHNITT_ID, DTOSchuelerLeistungsdaten.class, abschnittIds))
				.thenReturn(List.of(d1, d2));

		final HashMap2D<Long, Long, DTOSchuelerLeistungsdaten> result = repository.getMapByLernabschnittsIds(abschnittIds);

		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals(d1, result.getOrException(1L, 42L));
		assertEquals(d2, result.getOrException(2L, 43L));
		verify(conn).queryList(DTOSchuelerLeistungsdaten.QUERY_LIST_BY_ABSCHNITT_ID, DTOSchuelerLeistungsdaten.class, abschnittIds);
	}

}
