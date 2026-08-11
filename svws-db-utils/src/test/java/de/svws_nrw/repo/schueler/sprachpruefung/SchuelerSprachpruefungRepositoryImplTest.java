package de.svws_nrw.repo.schueler.sprachpruefung;

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
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerSprachpruefungen;

@ExtendWith(MockitoExtension.class)
class SchuelerSprachpruefungRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private SchuelerSprachpruefungRepositoryImpl repository;

	private final DTOSchuelerSprachpruefungen testSprachpruefung1 = getTestSprachpruefung1();

	private static DTOSchuelerSprachpruefungen getTestSprachpruefung1() {
		final var testSprachpruefung = new DTOSchuelerSprachpruefungen(10L, 100L, "IM");
		testSprachpruefung.ASDJahrgang = "09";
		testSprachpruefung.Pruefungsdatum = "2024-06-15";
		testSprachpruefung.IstHSUPruefung = true;
		testSprachpruefung.IstFeststellungspruefung = false;
		testSprachpruefung.KannErstePflichtfremdspracheErsetzen = true;
		testSprachpruefung.KannZweitePflichtfremdspracheErsetzen = false;
		testSprachpruefung.KannWahlpflichtfremdspracheErsetzen = true;
		testSprachpruefung.KannBelegungAlsFortgefuehrteSpracheErlauben = false;
		testSprachpruefung.Referenzniveau = null;
		testSprachpruefung.NotePruefung = null;
		testSprachpruefung.Zeugnisbezeichnung = "Englisch";
		return testSprachpruefung;
	}

	private final DTOSchuelerSprachpruefungen testSprachpruefung2 = getTestSprachpruefung2();

	private static DTOSchuelerSprachpruefungen getTestSprachpruefung2() {
		final var testSprachpruefung = new DTOSchuelerSprachpruefungen(11L, 101L, "F");
		testSprachpruefung.ASDJahrgang = "10";
		testSprachpruefung.IstHSUPruefung = false;
		return testSprachpruefung;
	}

	private final DTOSchuelerSprachpruefungen testSprachpruefung3 = getTestSprachpruefung3();

	private static DTOSchuelerSprachpruefungen getTestSprachpruefung3() {
		final var testSprachpruefung = new DTOSchuelerSprachpruefungen(12L, 100L, "L");
		testSprachpruefung.ASDJahrgang = "11";
		return testSprachpruefung;
	}

	@Test
	@DisplayName("Test: prüfe, ob getListBySchuelerIds bei null/leer eine leere Liste ohne DB-Interaktion zurückgibt.")
	void testGetListBySchuelerIdsEmptyInputs() {
		assertTrue(repository.getListBySchuelerIds(null).isEmpty());
		assertTrue(repository.getListBySchuelerIds(Collections.emptyList()).isEmpty());
		verifyNoInteractions(conn);
	}

	@Test
	@DisplayName("Test: prüfe, ob getListBySchuelerIds die Trefferliste für Schüler-IDs liefert.")
	void testGetListBySchuelerIds() {
		final List<Long> idsSchueler = Arrays.asList(100L, 101L);

		when(conn.queryList(DTOSchuelerSprachpruefungen.QUERY_LIST_BY_SCHUELER_ID, DTOSchuelerSprachpruefungen.class, idsSchueler))
				.thenReturn(List.of(testSprachpruefung1, testSprachpruefung2));

		final List<DTOSchuelerSprachpruefungen> result = repository.getListBySchuelerIds(idsSchueler);

		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals(testSprachpruefung1, result.get(0));
		assertEquals(testSprachpruefung2, result.get(1));
		verify(conn).queryList(DTOSchuelerSprachpruefungen.QUERY_LIST_BY_SCHUELER_ID, DTOSchuelerSprachpruefungen.class, idsSchueler);
	}

	@Test
	@DisplayName("Test: prüfe, ob getListBySchuelerIds bei nicht-leerem Input und leerem DB-Ergebnis eine leere Liste liefert.")
	void testGetListBySchuelerIdsEmptyDbResult() {
		final List<Long> idsSchueler = Arrays.asList(100L, 101L);

		when(conn.queryList(DTOSchuelerSprachpruefungen.QUERY_LIST_BY_SCHUELER_ID, DTOSchuelerSprachpruefungen.class, idsSchueler))
				.thenReturn(Collections.emptyList());

		final List<DTOSchuelerSprachpruefungen> result = repository.getListBySchuelerIds(idsSchueler);

		assertNotNull(result);
		assertTrue(result.isEmpty());
		verify(conn).queryList(DTOSchuelerSprachpruefungen.QUERY_LIST_BY_SCHUELER_ID, DTOSchuelerSprachpruefungen.class, idsSchueler);
	}

	@Test
	@DisplayName("Test: prüfe, ob getMapBySchuelerIDs bei null/leer eine leere Map ohne DB-Interaktion zurückgibt.")
	void testGetMapBySchuelerIDsEmptyInputs() {
		assertTrue(repository.getMapBySchuelerIDs(null).isEmpty());
		assertTrue(repository.getMapBySchuelerIDs(Collections.emptyList()).isEmpty());
		verifyNoInteractions(conn);
	}

	@Test
	@DisplayName("Test: prüfe, ob getMapBySchuelerIDs Sprachprüfungen korrekt nach Schüler-ID gruppiert.")
	void testGetMapBySchuelerIDs() {
		final List<Long> idsSchueler = Arrays.asList(100L, 101L);
		final String query = "SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.Schueler_ID IN ?1";

		when(conn.queryList(query, DTOSchuelerSprachpruefungen.class, idsSchueler))
				.thenReturn(List.of(testSprachpruefung1, testSprachpruefung2, testSprachpruefung3));

		final Map<Long, List<DTOSchuelerSprachpruefungen>> result = repository.getMapBySchuelerIDs(idsSchueler);

		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals(List.of(testSprachpruefung1, testSprachpruefung3), result.get(100L));
		assertEquals(List.of(testSprachpruefung2), result.get(101L));
		verify(conn).queryList(query, DTOSchuelerSprachpruefungen.class, idsSchueler);
	}

	@Test
	@DisplayName("Test: prüfe, ob getMapBySchuelerIDs bei nicht-leerem Input und leerem DB-Ergebnis eine leere Map liefert.")
	void testGetMapBySchuelerIDsEmptyDbResult() {
		final List<Long> idsSchueler = Arrays.asList(100L, 101L);
		final String query = "SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.Schueler_ID IN ?1";

		when(conn.queryList(query, DTOSchuelerSprachpruefungen.class, idsSchueler)).thenReturn(Collections.emptyList());

		final Map<Long, List<DTOSchuelerSprachpruefungen>> result = repository.getMapBySchuelerIDs(idsSchueler);

		assertNotNull(result);
		assertTrue(result.isEmpty());
		verify(conn).queryList(query, DTOSchuelerSprachpruefungen.class, idsSchueler);
	}

	@Test
	@DisplayName("Test: prüfe, ob bei create die ID korrekt gesetzt wird.")
	void testCreate() {
		final DTOSchuelerSprachpruefungen neu = new DTOSchuelerSprachpruefungen(10L, 100L, "E");
		final long neueId = 999L;

		when(conn.transactionGetNextID(DTOSchuelerSprachpruefungen.class)).thenReturn(neueId);
		when(conn.transactionPersist(neu)).thenReturn(true);

		final DTOSchuelerSprachpruefungen result = repository.create(neu);

		assertEquals(neueId, result.ID);
		verify(conn).transactionGetNextID(DTOSchuelerSprachpruefungen.class);
		verify(conn).transactionPersist(neu);
	}
}
