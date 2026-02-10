package de.svws_nrw.repo.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;


@ExtendWith(MockitoExtension.class)
class LehrerRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private LehrerRepositoryImpl repository;

	private final DTOLehrer testLehrer1 = new DTOLehrer(42, "L01", "Testlehrer 1");
	private final DTOLehrer testLehrer2 = new DTOLehrer(43, "L02", "Testlehrer 2");

	@Test
	@DisplayName("Test: Frage alle statistik-relevanten Lehrer ab.")
	void testGetAllStatistikRelevant() {
		final List<DTOLehrer> lehrerList = Arrays.asList(testLehrer1, testLehrer2);

		when(conn.queryList(DTOLehrer.QUERY_BY_STATISTIKRELEVANT, DTOLehrer.class, true))
				.thenReturn(lehrerList);

		final List<DTOLehrer> result = repository.getAllStatistikRelevant();

		assertNotNull(result);
		assertEquals(2, result.size());
		verify(conn).queryList(DTOLehrer.QUERY_BY_STATISTIKRELEVANT, DTOLehrer.class, true);
	}

	@Test
	@DisplayName("Test: Bestimme den ersten Lehrer in der Liste.")
	void testGetFirst() {
		when(conn.querySingle(DTOLehrer.class)).thenReturn(testLehrer1);

		final DTOLehrer result = repository.getFirst();

		assertNotNull(result);
		assertEquals(testLehrer1, result);
		verify(conn).querySingle(DTOLehrer.class);
	}

	@Test
	@DisplayName("Test: Bestimme einen Lehrer anhand seiner ID.")
	void testGetById() {
		final Long id = 123L;
		when(conn.queryByKey(DTOLehrer.class, id)).thenReturn(testLehrer1);

		final DTOLehrer result = repository.getById(id);

		assertNotNull(result);
		assertEquals(testLehrer1, result);
		verify(conn).queryByKey(DTOLehrer.class, id);
	}

	@Test
	@DisplayName("Test: Bestimme mehrere Lehrer anhand einer Liste von IDs.")
	void testGetListByIds() {
		final List<Long> ids = Arrays.asList(42L, 43L);
		final List<DTOLehrer> lehrerList = Arrays.asList(testLehrer1, testLehrer2);

		when(conn.queryByKeyList(DTOLehrer.class, ids)).thenReturn(lehrerList);

		final List<DTOLehrer> result = repository.findListByIds(ids);

		assertEquals(2, result.size());
		verify(conn).queryByKeyList(DTOLehrer.class, ids);
	}

	@Test
	@DisplayName("Test: Erhalten eine leere Liste bei einen Aufruf von getListByIds mit einer leeren Liste oder null ohne eine Datenbank-Aufruf")
	void testGetListByIdsEmpty() {
		final List<DTOLehrer> resultNull = repository.findListByIds(null);
		final List<DTOLehrer> resultEmpty = repository.findListByIds(Collections.emptyList());
		assertTrue(resultNull.isEmpty());
		assertTrue(resultEmpty.isEmpty());
		verifyNoInteractions(conn);
	}

	@Test
	@DisplayName("Test: Bestimme alle Lehrer.")
	void testGetAll() {
		when(conn.queryAll(DTOLehrer.class)).thenReturn(Arrays.asList(testLehrer1));
		final List<DTOLehrer> result = repository.getAll();
		assertFalse(result.isEmpty());
		verify(conn).queryAll(DTOLehrer.class);
	}

	@Test
	@DisplayName("Test: Erstelle einen neuen Lehrer mit der automatischen ID-Zuweisung.")
	void testCreate() {
		final DTOLehrer neuerLehrer = new DTOLehrer(-1, "L01", "Testlehrer 1");

		final long erwarteteId = 1001L;
		when(conn.transactionGetNextID(DTOLehrer.class)).thenReturn(erwarteteId);
		when(conn.transactionPersist(neuerLehrer)).thenReturn(true);

		final DTOLehrer result = repository.create(neuerLehrer);

		// Verifizierung
		assertEquals(erwarteteId, result.ID, "Die ID wurde nicht durch den setId-Consumer im Repository korrekt gesetzt.");

		verify(conn).transactionGetNextID(DTOLehrer.class);
		verify(conn).transactionPersist(neuerLehrer);
	}

	@Test
	@DisplayName("Test: Aktualisiere bzw. speichere alle Lehrer.")
	void testUpdate() {
		when(conn.transactionPersist(testLehrer1)).thenReturn(true);
		repository.update(testLehrer1);
		verify(conn).transactionPersist(testLehrer1);
	}

	@Test
	@DisplayName("Test: Lösche einen Lehrer")
	void testDelete() {
		when(conn.transactionRemove(testLehrer1)).thenReturn(true);
		repository.delete(testLehrer1);
		verify(conn).transactionRemove(testLehrer1);
	}

}
