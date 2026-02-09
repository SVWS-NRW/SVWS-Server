package de.svws_nrw.repo.fach;

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
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.repo.faecher.FachRepositoryImpl;

@ExtendWith(MockitoExtension.class)
class FachRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private FachRepositoryImpl repository;

	private final DTOFach testFach1 = getTestFach1();

	private static DTOFach getTestFach1() {
		final var testFach = new DTOFach(42, true);
		testFach.Kuerzel = "IF";
		return testFach;
	}

	private final DTOFach testFach2 = getTestFach2();

	private static DTOFach getTestFach2() {
		final var testFach = new DTOFach(43, true);
		testFach.Kuerzel = "PH";
		return testFach;
	}

	@Test
	@DisplayName("Test: Bestimme ein Fach anhand seiner ID.")
	void testGetById() {
		final Long id = 42L;
		when(conn.queryByKey(DTOFach.class, id)).thenReturn(testFach1);

		final DTOFach result = repository.getById(id);

		assertNotNull(result);
		assertEquals(testFach1, result);
		verify(conn).queryByKey(DTOFach.class, id);
	}

	@Test
	@DisplayName("Test: Bestimme mehrere Fächer anhand einer Liste von IDs.")
	void testGetListByIds() {
		final List<Long> ids = Arrays.asList(42L, 43L);
		final List<DTOFach> list = Arrays.asList(testFach1, testFach2);

		when(conn.queryByKeyList(DTOFach.class, ids)).thenReturn(list);

		final List<DTOFach> result = repository.findListByIds(ids);

		assertEquals(2, result.size());
		verify(conn).queryByKeyList(DTOFach.class, ids);
	}

	@Test
	@DisplayName("Test: Erhalten eine leere Liste bei einen Aufruf von getListByIds mit einer leeren Liste oder null ohne eine Datenbank-Aufruf")
	void testGetListByIdsEmpty() {
		final List<DTOFach> resultNull = repository.findListByIds(null);
		final List<DTOFach> resultEmpty = repository.findListByIds(Collections.emptyList());
		assertTrue(resultNull.isEmpty());
		assertTrue(resultEmpty.isEmpty());
		verifyNoInteractions(conn);
	}

	@Test
	@DisplayName("Test: Bestimme alle Fächer.")
	void testGetAll() {
		when(conn.queryAll(DTOFach.class)).thenReturn(Arrays.asList(testFach1));
		final List<DTOFach> result = repository.getAll();
		assertFalse(result.isEmpty());
		verify(conn).queryAll(DTOFach.class);
	}

	@Test
	@DisplayName("Test: Erstelle ein neues Fach mit der automatischen ID-Zuweisung.")
	void testCreate() {
		final DTOFach neuesFach = new DTOFach(-1, true);

		final long erwarteteId = 1001L;
		when(conn.transactionGetNextID(DTOFach.class)).thenReturn(erwarteteId);
		when(conn.transactionPersist(neuesFach)).thenReturn(true);

		final DTOFach result = repository.create(neuesFach);

		// Verifizierung
		assertEquals(erwarteteId, result.ID, "Die ID wurde nicht durch den setId-Consumer im Repository korrekt gesetzt.");

		verify(conn).transactionGetNextID(DTOFach.class);
		verify(conn).transactionPersist(neuesFach);
	}

	@Test
	@DisplayName("Test: Aktualisiere bzw. speichere alle Fächer.")
	void testUpdate() {
		when(conn.transactionPersist(testFach1)).thenReturn(true);
		repository.update(testFach1);
		verify(conn).transactionPersist(testFach1);
	}

	@Test
	@DisplayName("Test: Lösche ein Fach")
	void testDelete() {
		when(conn.transactionRemove(testFach1)).thenReturn(true);
		repository.delete(testFach1);
		verify(conn).transactionRemove(testFach1);
	}

}
