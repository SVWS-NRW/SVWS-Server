package de.svws_nrw.repo.schueler.sprachenfolge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerSprachenfolge;

@ExtendWith(MockitoExtension.class)
class SchuelerSprachenfolgeRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private SchuelerSprachenfolgeRepositoryImpl repository;

	private final DTOSchuelerSprachenfolge testSprachenfolge1 = getTestSprachenfolge1();

	private static DTOSchuelerSprachenfolge getTestSprachenfolge1() {
		final var testSprachenfolge = new DTOSchuelerSprachenfolge(10L, 100L, "E");
		testSprachenfolge.IstNachweis = true;
		testSprachenfolge.ReihenfolgeNr = 1;
		testSprachenfolge.ASDJahrgangVon = "05";
		testSprachenfolge.ASDJahrgangBis = "10";
		testSprachenfolge.AbschnittVon = 1;
		testSprachenfolge.AbschnittBis = 2;
		testSprachenfolge.Referenzniveau = "B1";
		testSprachenfolge.KleinesLatinumErreicht = false;
		testSprachenfolge.LatinumErreicht = true;
		testSprachenfolge.GraecumErreicht = false;
		testSprachenfolge.HebraicumErreicht = false;
		return testSprachenfolge;
	}

	private final DTOSchuelerSprachenfolge testSprachenfolge2 = getTestSprachenfolge2();

	private static DTOSchuelerSprachenfolge getTestSprachenfolge2() {
		final var testSprachenfolge = new DTOSchuelerSprachenfolge(11L, 101L, "F");
		testSprachenfolge.IstNachweis = false;
		testSprachenfolge.ReihenfolgeNr = 2;
		return testSprachenfolge;
	}

	private final DTOSchuelerSprachenfolge testSprachenfolge3 = getTestSprachenfolge3();

	private static DTOSchuelerSprachenfolge getTestSprachenfolge3() {
		final var testSprachenfolge = new DTOSchuelerSprachenfolge(12L, 100L, "L");
		testSprachenfolge.ReihenfolgeNr = 3;
		return testSprachenfolge;
	}

	@Test
	@DisplayName("Test: prüfe, ob getListBySchuelerIds bei null/leer eine leere Liste ohne DB-Interaktion zurück gibt.")
	void testGetListBySchuelerIdsEmptyInputs() {
		assertTrue(repository.getListBySchuelerIds(null).isEmpty());
		assertTrue(repository.getListBySchuelerIds(Collections.emptyList()).isEmpty());
		verifyNoInteractions(conn);
	}

	@Test
	@DisplayName("Test: prüfe, ob getListBySchuelerIds die Trefferliste für Schüler-IDs liefert.")
	void testGetListBySchuelerIds() {
		final List<Long> idsSchueler = Arrays.asList(100L, 101L);

		when(conn.queryList(DTOSchuelerSprachenfolge.QUERY_LIST_BY_SCHUELER_ID, DTOSchuelerSprachenfolge.class, idsSchueler))
				.thenReturn(List.of(testSprachenfolge1, testSprachenfolge2));

		final List<DTOSchuelerSprachenfolge> result = repository.getListBySchuelerIds(idsSchueler);

		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals(testSprachenfolge1, result.get(0));
		assertEquals(testSprachenfolge2, result.get(1));
		verify(conn).queryList(DTOSchuelerSprachenfolge.QUERY_LIST_BY_SCHUELER_ID, DTOSchuelerSprachenfolge.class, idsSchueler);
	}

	@Test
	@DisplayName("Test: prüfe, ob getListBySchuelerIds bei nicht-leerem Input und leerem DB-Ergebnis eine leere Liste liefert.")
	void testGetListBySchuelerIdsEmptyDbResult() {
		final List<Long> idsSchueler = Arrays.asList(100L, 101L);

		when(conn.queryList(DTOSchuelerSprachenfolge.QUERY_LIST_BY_SCHUELER_ID, DTOSchuelerSprachenfolge.class, idsSchueler))
				.thenReturn(Collections.emptyList());

		final List<DTOSchuelerSprachenfolge> result = repository.getListBySchuelerIds(idsSchueler);

		assertNotNull(result);
		assertTrue(result.isEmpty());
		verify(conn).queryList(DTOSchuelerSprachenfolge.QUERY_LIST_BY_SCHUELER_ID, DTOSchuelerSprachenfolge.class, idsSchueler);
	}

	@Test
	@DisplayName("Test: prüfe, ob getMapBySchuelerIDs bei null/leer eine leere Map ohne DB-Interaktion zurück gibt.")
	void testGetMapBySchuelerIDsEmptyInputs() {
		assertTrue(repository.getMapBySchuelerIDs(null).isEmpty());
		assertTrue(repository.getMapBySchuelerIDs(Collections.emptyList()).isEmpty());
		verifyNoInteractions(conn);
	}

	@Test
	@DisplayName("Test: prüfe, ob getMapBySchuelerIDs Sprachenfolgen korrekt nach Schüler-ID gruppiert.")
	void testGetMapBySchuelerIDs() {
		final List<Long> idsSchueler = Arrays.asList(100L, 101L);
		final String query = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.Schueler_ID IN ?1";

		when(conn.queryList(query, DTOSchuelerSprachenfolge.class, idsSchueler))
				.thenReturn(List.of(testSprachenfolge1, testSprachenfolge2, testSprachenfolge3));

		final Map<Long, List<DTOSchuelerSprachenfolge>> result = repository.getMapBySchuelerIDs(idsSchueler);

		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals(List.of(testSprachenfolge1, testSprachenfolge3), result.get(100L));
		assertEquals(List.of(testSprachenfolge2), result.get(101L));
		verify(conn).queryList(query, DTOSchuelerSprachenfolge.class, idsSchueler);
	}

	@Test
	@DisplayName("Test: prüfe, ob getMapBySchuelerIDs bei nicht-leerem Input und leerem DB-Ergebnis eine leere Map liefert.")
	void testGetMapBySchuelerIDsEmptyDbResult() {
		final List<Long> idsSchueler = Arrays.asList(100L, 101L);
		final String query = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.Schueler_ID IN ?1";

		when(conn.queryList(query, DTOSchuelerSprachenfolge.class, idsSchueler)).thenReturn(Collections.emptyList());

		final Map<Long, List<DTOSchuelerSprachenfolge>> result = repository.getMapBySchuelerIDs(idsSchueler);

		assertNotNull(result);
		assertTrue(result.isEmpty());
		verify(conn).queryList(query, DTOSchuelerSprachenfolge.class, idsSchueler);
	}

	@Test
	@DisplayName("Test: Prüfe, ob die ID korrekt gesetzt wird.")
	void testCreate() {
		final DTOSchuelerSprachenfolge neu = new DTOSchuelerSprachenfolge(10L, 100L, "E");
		final long neueId = 999L;

		when(conn.transactionGetNextID(DTOSchuelerSprachenfolge.class)).thenReturn(neueId);
		when(conn.transactionPersist(neu)).thenReturn(true);

		final DTOSchuelerSprachenfolge result = repository.create(neu);

		assertEquals(neueId, result.ID);
		verify(conn).transactionGetNextID(DTOSchuelerSprachenfolge.class);
		verify(conn).transactionPersist(neu);
	}
}
