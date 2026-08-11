package de.svws_nrw.repo.schueler.lernabschnitt;

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
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;

@ExtendWith(MockitoExtension.class)
class SchuelerLernabschnittRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private SchuelerLernabschnittRepositoryImpl repository;

	private final DTOSchuelerLernabschnittsdaten testSchuelerLernabschnitt1 = getTestLernabschnitt1();

	private static DTOSchuelerLernabschnittsdaten getTestLernabschnitt1() {
		final var testLernabschnitt = new DTOSchuelerLernabschnittsdaten(10L, 100L, 42L, false, false);
		testLernabschnitt.Klassen_ID = 1L;
		testLernabschnitt.WechselNr = 0;
		return testLernabschnitt;
	}

	private final DTOSchuelerLernabschnittsdaten testSchuelerLernabschnitt2 = getTestLernabschnitt2();

	private static DTOSchuelerLernabschnittsdaten getTestLernabschnitt2() {
		final var testLernabschnitt = new DTOSchuelerLernabschnittsdaten(11L, 101L, 42L, false, false);
		testLernabschnitt.Klassen_ID = 1L;
		testLernabschnitt.WechselNr = 0;
		return testLernabschnitt;
	}

	private final DTOSchuelerLernabschnittsdaten testSchuelerLernabschnitt3 = getTestLernabschnitt3();

	private static DTOSchuelerLernabschnittsdaten getTestLernabschnitt3() {
		final var testLernabschnitt = new DTOSchuelerLernabschnittsdaten(12L, 102L, 42L, false, false);
		testLernabschnitt.Klassen_ID = 2L;
		testLernabschnitt.WechselNr = 0;
		return testLernabschnitt;
	}


	@Test
	@DisplayName("Test: Prüfe, ob getMapKlassenSchueler die Schüler-IDs anhand der Lernabschnittsdaten korrekt nach Klassen-IDs gruppiert.")
	void testGetMapKlassenSchueler() {
		final List<Long> idsKlassen = Arrays.asList(1L, 2L);

		when(conn.queryList(DTOSchuelerLernabschnittsdaten.QUERY_LIST_BY_KLASSEN_ID, DTOSchuelerLernabschnittsdaten.class, idsKlassen))
				.thenReturn(Arrays.asList(testSchuelerLernabschnitt1, testSchuelerLernabschnitt2, testSchuelerLernabschnitt3));

		final Map<Long, List<Long>> result = repository.getMapKlassenSchueler(idsKlassen);

		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals(2, result.get(1L).size());
		assertTrue(result.get(1L).contains(testSchuelerLernabschnitt1.Schueler_ID));
		assertEquals(testSchuelerLernabschnitt3.Schueler_ID, result.get(2L).get(0));
	}

	@Test
	@DisplayName("Test: getMapBySchuelerIDsAndSchuljahreabschnitt nutzt korrekte JPQL-Parameter.")
	void testGetMapBySchuelerIDsAndSchuljahreabschnitt() {
		final List<Long> idsSchueler = Arrays.asList(100L);
		final long idAbschnitt = 42L;
		final String query =
				"SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID IN ?1 AND e.WechselNr = ?2 AND e.Schuljahresabschnitts_ID = ?3";

		when(conn.queryList(query, DTOSchuelerLernabschnittsdaten.class, idsSchueler, 0, idAbschnitt)).thenReturn(List.of(testSchuelerLernabschnitt1));

		final Map<Long, DTOSchuelerLernabschnittsdaten> result = repository.getMapBySchuelerIDsAndSchuljahreabschnitt(idsSchueler, idAbschnitt);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(testSchuelerLernabschnitt1, result.get(testSchuelerLernabschnitt1.Schueler_ID));

		verify(conn).queryList(query, DTOSchuelerLernabschnittsdaten.class, idsSchueler, 0, idAbschnitt);
	}

	@Test
	@DisplayName("Test: Prüfe, ob getMapKlassenSchueler bei null oder einer leeren Liste eine leere Map ohne DB-Interaktion zurückgibt.")
	void testGetMapKlassenSchuelerEmptyInputs() {
		assertTrue(repository.getMapKlassenSchueler(null).isEmpty());
		assertTrue(repository.getMapKlassenSchueler(Collections.emptyList()).isEmpty());
		verifyNoInteractions(conn);
	}

	@Test
	@DisplayName("Test: Prüfe, ob getMapBySchuelerIDsAndSchuljahreabschnitt bei null oder einer leeren Liste eine leere Map ohne DB-Interaktion zurückgibt.")
	void testGetMapBySchuelerIDsAndSchuljahreabschnittEmptyInputs() {
		assertTrue(repository.getMapBySchuelerIDsAndSchuljahreabschnitt(null, 42L).isEmpty());
		assertTrue(repository.getMapBySchuelerIDsAndSchuljahreabschnitt(Collections.emptyList(), 42L).isEmpty());
		verifyNoInteractions(conn);
	}

	@Test
	@DisplayName("Test: Prüfe, ob getMapBySchuelerIDsAndSchuljahreabschnitt bei nicht-leerem Input und leerem DB-Ergebnis eine leere Map liefert.")
	void testGetMapBySchuelerIDsAndSchuljahreabschnitt_EmptyDbResult() {
		final List<Long> idsSchueler = Arrays.asList(100L, 101L);
		final long idAbschnitt = 42L;
		final String query =
				"SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID IN ?1 AND e.WechselNr = ?2 AND e.Schuljahresabschnitts_ID = ?3";

		when(conn.queryList(query, DTOSchuelerLernabschnittsdaten.class, idsSchueler, 0, idAbschnitt)).thenReturn(Collections.emptyList());

		final Map<Long, DTOSchuelerLernabschnittsdaten> result = repository.getMapBySchuelerIDsAndSchuljahreabschnitt(idsSchueler, idAbschnitt);

		assertNotNull(result);
		assertTrue(result.isEmpty());
		verify(conn).queryList(query, DTOSchuelerLernabschnittsdaten.class, idsSchueler, 0, idAbschnitt);
	}

	@Test
	@DisplayName("Test: Prüfe, ob getMapByLernabschnittIDAndSchuljahreabschnitt bei nicht-leerem Input und leerem DB-Ergebnis eine leere Map liefert.")
	void testGetMapByLernabschnittIDAndSchuljahreabschnitt_EmptyDbResult() {
		final List<Long> idsSchueler = Arrays.asList(100L, 101L);
		final long idAbschnitt = 42L;
		final String query =
				"SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID IN ?1 AND e.WechselNr = ?2 AND e.Schuljahresabschnitts_ID = ?3";

		when(conn.queryList(query, DTOSchuelerLernabschnittsdaten.class, idsSchueler, 0, idAbschnitt)).thenReturn(Collections.emptyList());

		final Map<Long, DTOSchuelerLernabschnittsdaten> result = repository.getMapByLernabschnittIDAndSchuljahreabschnitt(idsSchueler, idAbschnitt);

		assertNotNull(result);
		assertTrue(result.isEmpty());
		verify(conn).queryList(query, DTOSchuelerLernabschnittsdaten.class, idsSchueler, 0, idAbschnitt);
	}


	@Test
	@DisplayName("Test: Prüfe, ob die ID korrekt gesetzt wird.")
	void testCreate() {
		final DTOSchuelerLernabschnittsdaten neu = new DTOSchuelerLernabschnittsdaten(10L, 100L, 42L, false, false);
		final long neueId = 999L;

		when(conn.transactionGetNextID(DTOSchuelerLernabschnittsdaten.class)).thenReturn(neueId);
		when(conn.transactionPersist(neu)).thenReturn(true);

		final DTOSchuelerLernabschnittsdaten result = repository.create(neu);

		assertEquals(neueId, result.ID);
		verify(conn).transactionGetNextID(DTOSchuelerLernabschnittsdaten.class);
		verify(conn).transactionPersist(neu);
	}

	@Test
	@DisplayName("Test: prüfe, ob getMapByLernabschnittID(ids) bei leerem Input eine leere Map und sonst ein Mapping nach Lernabschnitts-ID liefert.")
	void testGetMapByLernabschnittID() {
		// Fall: null oder leere IDs -> leere Map ohne DB-Interaktion
		assertTrue(repository.getMapByLernabschnittID(null).isEmpty());
		assertTrue(repository.getMapByLernabschnittID(Collections.emptyList()).isEmpty());
		verifyNoInteractions(conn);

		// Fall: IDs vorhanden -> Mapping nach Lernabschnitts-ID
		final List<Long> idsSchueler = Arrays.asList(100L, 101L);
		final String query = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID IN ?1 AND e.WechselNr = ?2";

		when(conn.queryList(query, DTOSchuelerLernabschnittsdaten.class, idsSchueler, 0))
				.thenReturn(List.of(testSchuelerLernabschnitt1, testSchuelerLernabschnitt2));

		final Map<Long, DTOSchuelerLernabschnittsdaten> result = repository.getMapByLernabschnittID(idsSchueler);

		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals(testSchuelerLernabschnitt1, result.get(testSchuelerLernabschnitt1.ID));
		assertEquals(testSchuelerLernabschnitt2, result.get(testSchuelerLernabschnitt2.ID));
		verify(conn).queryList(query, DTOSchuelerLernabschnittsdaten.class, idsSchueler, 0);
	}

	@Test
	@DisplayName("Test: pruft, ob getMapByLernabschnittID(ids) bei nicht-leerem Input und leerem DB-Ergebnis eine leere Map liefert.")
	void testGetMapByLernabschnittID_EmptyDbResult() {
		final List<Long> idsSchueler = Arrays.asList(100L, 101L);
		final String query = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID IN ?1 AND e.WechselNr = ?2";

		when(conn.queryList(query, DTOSchuelerLernabschnittsdaten.class, idsSchueler, 0)).thenReturn(Collections.emptyList());

		final Map<Long, DTOSchuelerLernabschnittsdaten> result = repository.getMapByLernabschnittID(idsSchueler);

		assertNotNull(result);
		assertTrue(result.isEmpty());
		verify(conn).queryList(query, DTOSchuelerLernabschnittsdaten.class, idsSchueler, 0);
	}

	@Test
	@DisplayName("Test: prüft, ob getMapAllLernabschnittIDsBySchuelerIDs bei null/leer eine leere Map ohne DB-Interaktion zurück gibt.")
	void testGetMapAllLernabschnittIDsBySchuelerIDsEmptyInputs() {
		assertTrue(repository.getMapAllLernabschnittIDsBySchuelerIDs(null).isEmpty());
		assertTrue(repository.getMapAllLernabschnittIDsBySchuelerIDs(Collections.emptyList()).isEmpty());
		verifyNoInteractions(conn);
	}

	@Test
	@DisplayName("Test: prüft, ob getMapAllLernabschnittIDsBySchuelerIDs Lernabschnitts-IDs korrekt nach Schüler-IDs gruppiert.")
	void testGetMapAllLernabschnittIDsBySchuelerIDs() {
		final List<Long> idsSchueler = Arrays.asList(100L, 101L);
		final String query = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID IN ?1";
		final DTOSchuelerLernabschnittsdaten d4 = new DTOSchuelerLernabschnittsdaten(13L, 100L, 43L, false, false);
		d4.WechselNr = 1;

		when(conn.queryList(query, DTOSchuelerLernabschnittsdaten.class, idsSchueler))
				.thenReturn(List.of(testSchuelerLernabschnitt1, testSchuelerLernabschnitt2, d4));

		final Map<Long, List<Long>> result = repository.getMapAllLernabschnittIDsBySchuelerIDs(idsSchueler);

		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals(List.of(10L, 13L), result.get(100L));
		assertEquals(List.of(11L), result.get(101L));
		verify(conn).queryList(query, DTOSchuelerLernabschnittsdaten.class, idsSchueler);
	}

	@Test
	@DisplayName("Test: prüft, ob getMapAllLernabschnittIDsBySchuelerIDs bei nicht-leerem Input und leerem DB-Ergebnis eine leere Map liefert.")
	void testGetMapAllLernabschnittIDsBySchuelerIDsEmptyDbResult() {
		final List<Long> idsSchueler = Arrays.asList(100L, 101L);
		final String query = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID IN ?1";

		when(conn.queryList(query, DTOSchuelerLernabschnittsdaten.class, idsSchueler)).thenReturn(Collections.emptyList());

		final Map<Long, List<Long>> result = repository.getMapAllLernabschnittIDsBySchuelerIDs(idsSchueler);

		assertNotNull(result);
		assertTrue(result.isEmpty());
		verify(conn).queryList(query, DTOSchuelerLernabschnittsdaten.class, idsSchueler);
	}

}
