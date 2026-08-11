package de.svws_nrw.repo.lehrer.minderleistung;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerEntlastungsstunde;

@ExtendWith(MockitoExtension.class)
class LehrerMinderleistungRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private LehrerMinderleistungRepositoryImpl repository;

	@Test
	@DisplayName("Test: Prüfe, ob getMapByAbschnitt die Minderleistungen nach Abschnitt-IDs korrekt gruppiert.")
	void testGetMapByAbschnittIds() {
		// Stenario: Drei Abschnitte mit zwei, einem bzw. keinem Minderleistungsgrund
		final List<Long> idsAbschnitte = Arrays.asList(100L, 200L, 300L);
		final DTOLehrerEntlastungsstunde e1 = new DTOLehrerEntlastungsstunde(1L, 100L);
		final DTOLehrerEntlastungsstunde e2 = new DTOLehrerEntlastungsstunde(2L, 100L);
		final DTOLehrerEntlastungsstunde e3 = new DTOLehrerEntlastungsstunde(3L, 200L);

		when(conn.queryList(DTOLehrerEntlastungsstunde.QUERY_LIST_BY_IDABSCHNITTSDATEN, DTOLehrerEntlastungsstunde.class, idsAbschnitte))
				.thenReturn(Arrays.asList(e1, e2, e3));

		final Map<Long, List<DTOLehrerEntlastungsstunde>> result = repository.getMapByAbschnittIds(idsAbschnitte);

		// Prüfe, ob die Gruppierung korrekt durchgeführt wurde
		assertNotNull(result);
		assertEquals(3, result.size(), "Die Map sollte Einträge für drei Abschnitte enthalten.");

		// Prüfung Abschnitt 100
		assertEquals(2, result.get(100L).size());
		assertTrue(result.get(100L).contains(e1));
		assertTrue(result.get(100L).contains(e2));

		// Prüfung Abschnitt 200
		assertEquals(1, result.get(200L).size());
		assertEquals(e3, result.get(200L).getFirst());

		// Prüfung Abschnitt 300
		assertTrue(result.get(300L).isEmpty());

		verify(conn).queryList(DTOLehrerEntlastungsstunde.QUERY_LIST_BY_IDABSCHNITTSDATEN, DTOLehrerEntlastungsstunde.class, idsAbschnitte);
	}

	@Test
	@DisplayName("Test: Prüfe, ob getMapByAbschnitt bei null oder leeren IDs eine leere Map liefert.")
	void testGetMapByAbschnittIdsEmpty() {
		assertTrue(repository.getMapByAbschnittIds(null).isEmpty());
		assertTrue(repository.getMapByAbschnittIds(List.of()).isEmpty());
		verifyNoInteractions(conn);
	}

	@Test
	@DisplayName("Test: Prüfe, ob die ID korrekt gesetzt wird.")
	void testCreate() {
		final DTOLehrerEntlastungsstunde neu = new DTOLehrerEntlastungsstunde(-1L, 100L);
		final long neueId = 999L;

		when(conn.transactionGetNextID(DTOLehrerEntlastungsstunde.class)).thenReturn(neueId);
		when(conn.transactionPersist(neu)).thenReturn(true);

		final DTOLehrerEntlastungsstunde result = repository.create(neu);

		assertEquals(neueId, result.id);
		verify(conn).transactionGetNextID(DTOLehrerEntlastungsstunde.class);
		verify(conn).transactionPersist(neu);
	}

	@Test
	@DisplayName("getAllByLehrerAbschnittId: Delegiert korrekt an DBEntityManager.queryList")
	void testGetAllByLehrerAbschnittId() {
		final long idAbschnitt = 100L;

		final var e1 = new DTOLehrerEntlastungsstunde(1L, idAbschnitt);
		final var e2 = new DTOLehrerEntlastungsstunde(2L, idAbschnitt);

		when(conn.queryList(DTOLehrerEntlastungsstunde.QUERY_BY_IDABSCHNITTSDATEN, DTOLehrerEntlastungsstunde.class, idAbschnitt))
				.thenReturn(Arrays.asList(e1, e2));

		final var result = repository.getAllByLehrerAbschnittId(idAbschnitt);

		assertNotNull(result);
		assertEquals(2, result.size());
		assertTrue(result.contains(e1));
		assertTrue(result.contains(e2));

		verify(conn).queryList(DTOLehrerEntlastungsstunde.QUERY_BY_IDABSCHNITTSDATEN, DTOLehrerEntlastungsstunde.class, idAbschnitt);
	}

	@Test
	@DisplayName("getListByIdLehrerAbschnittsdaten: Gruppiert korrekt nach Abschnittsdaten-ID (ohne Auffüllen fehlender Keys)")
	void testGetListByIdLehrerAbschnittsdaten() {
		final List<Long> idsAbschnitte = Arrays.asList(100L, 200L, 300L);

		final var e1 = new DTOLehrerEntlastungsstunde(1L, 100L);
		final var e2 = new DTOLehrerEntlastungsstunde(2L, 100L);
		final var e3 = new DTOLehrerEntlastungsstunde(3L, 200L);

		when(conn.queryList(DTOLehrerEntlastungsstunde.QUERY_LIST_BY_IDABSCHNITTSDATEN, DTOLehrerEntlastungsstunde.class, idsAbschnitte))
				.thenReturn(Arrays.asList(e1, e2, e3));

		final Map<Long, List<DTOLehrerEntlastungsstunde>> result = repository.getListByIdLehrerAbschnittsdaten(idsAbschnitte);

		assertNotNull(result);
		assertEquals(2, result.size());

		assertEquals(2, result.get(100L).size());
		assertTrue(result.get(100L).contains(e1));
		assertTrue(result.get(100L).contains(e2));

		assertEquals(1, result.get(200L).size());
		assertEquals(e3, result.get(200L).getFirst());

		// wichtig: im Gegensatz zu getMapByAbschnittIds wird 300 NICHT automatisch ergänzt
		assertFalse(result.containsKey(300L));

		verify(conn).queryList(DTOLehrerEntlastungsstunde.QUERY_LIST_BY_IDABSCHNITTSDATEN, DTOLehrerEntlastungsstunde.class, idsAbschnitte);
	}

	@Test
	@DisplayName("getListByIdLehrerAbschnittsdaten: Leeres DB-Ergebnis -> leere Map")
	void testGetListByIdLehrerAbschnittsdatenEmptyResult() {
		final List<Long> idsAbschnitte = List.of(100L);

		when(conn.queryList(DTOLehrerEntlastungsstunde.QUERY_LIST_BY_IDABSCHNITTSDATEN, DTOLehrerEntlastungsstunde.class, idsAbschnitte))
				.thenReturn(List.of());

		final var result = repository.getListByIdLehrerAbschnittsdaten(idsAbschnitte);

		assertNotNull(result);
		assertTrue(result.isEmpty());

		verify(conn).queryList(DTOLehrerEntlastungsstunde.QUERY_LIST_BY_IDABSCHNITTSDATEN, DTOLehrerEntlastungsstunde.class, idsAbschnitte);
	}

}
