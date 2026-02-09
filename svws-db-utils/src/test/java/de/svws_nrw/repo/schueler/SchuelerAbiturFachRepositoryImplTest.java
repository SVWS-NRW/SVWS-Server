package de.svws_nrw.repo.schueler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.core.types.gost.GostAbiturFach;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.abitur.DTOSchuelerAbiturFach;

@ExtendWith(MockitoExtension.class)
class SchuelerAbiturFachRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private SchuelerAbiturFachRepositoryImpl repository;

	private final DTOSchuelerAbiturFach testAbiturFach1 = new DTOSchuelerAbiturFach(1L, 100L, 10L);
	private final DTOSchuelerAbiturFach testAbiturFach2 = new DTOSchuelerAbiturFach(2L, 100L, 11L);

	@Test
	@DisplayName("Test: Prüfe, ob getListBySchuelerIds alle Fachinformationen für Schüler korrekt liefert.")
	void testGetListBySchuelerIds() {
		final List<Long> idsSchueler = List.of(100L);

		when(conn.queryList(DTOSchuelerAbiturFach.QUERY_LIST_BY_SCHUELER_ID, DTOSchuelerAbiturFach.class, idsSchueler))
				.thenReturn(Arrays.asList(testAbiturFach1, testAbiturFach2));

		final List<DTOSchuelerAbiturFach> result = repository.getListBySchuelerIds(idsSchueler);

		assertNotNull(result);
		assertEquals(2, result.size());
		assertTrue(result.contains(testAbiturFach1));
		assertTrue(result.contains(testAbiturFach2));
		verify(conn).queryList(DTOSchuelerAbiturFach.QUERY_LIST_BY_SCHUELER_ID, DTOSchuelerAbiturFach.class, idsSchueler);
	}

	@Test
	@DisplayName("Test: Prüfe, ob getListBySchuelerIdsNurPruefungsfaecher alle Fachinformationen zu Prüfungsfächern für Schüler korrekt liefert.")
	void testGetListBySchuelerIdsNurPruefungsfaecher() {
		final List<Long> idsSchueler = List.of(100L);
		final String query = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Schueler_ID IN ?1 AND e.AbiturFach IS NOT NULL";

		final DTOSchuelerAbiturFach testAbiFach = new DTOSchuelerAbiturFach(1L, 100L, 10L);
		testAbiFach.AbiturFach = GostAbiturFach.LK1;

		when(conn.queryList(query, DTOSchuelerAbiturFach.class, idsSchueler)).thenReturn(List.of(testAbiFach));

		final List<DTOSchuelerAbiturFach> result = repository.getListBySchuelerIdsNurPruefungsfaecher(idsSchueler);

		assertEquals(1, result.size());
		assertEquals(testAbiFach, result.get(0));
		verify(conn).queryList(query, DTOSchuelerAbiturFach.class, idsSchueler);
	}

	@Test
	@DisplayName("Test: Prüfe, ob bei leeren IDs keine Datenbank-Interaktion erfolgt.")
	void testGetListEmptyInputs() {
		assertTrue(repository.getListBySchuelerIds(null).isEmpty());
		assertTrue(repository.getListBySchuelerIdsNurPruefungsfaecher(Collections.emptyList()).isEmpty());
		verifyNoInteractions(conn);
	}

	@Test
	@DisplayName("Test: Prüfe, ob die ID bei create über den Consumer korrekt gesetzt wird.")
	void testCreate() {
		final DTOSchuelerAbiturFach neu = new DTOSchuelerAbiturFach(1L, 100L, 10L);
		final long neueId = 12345L;

		when(conn.transactionGetNextID(DTOSchuelerAbiturFach.class)).thenReturn(neueId);
		when(conn.transactionPersist(neu)).thenReturn(true);

		final DTOSchuelerAbiturFach result = repository.create(neu);

		assertEquals(neueId, result.ID);
		verify(conn).transactionGetNextID(DTOSchuelerAbiturFach.class);
		verify(conn).transactionPersist(neu);
	}

}
