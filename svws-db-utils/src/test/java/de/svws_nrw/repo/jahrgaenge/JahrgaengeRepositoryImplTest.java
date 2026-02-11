package de.svws_nrw.repo.jahrgaenge;

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
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;

@ExtendWith(MockitoExtension.class)
class JahrgaengeRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private JahrgaengeRepositoryImpl repository;

	private final DTOJahrgang testJahrgang1 = getTestJahrgang1();

	private static DTOJahrgang getTestJahrgang1() {
		final var testJahrgang = new DTOJahrgang(42);
		testJahrgang.InternKrz = "05";
		return testJahrgang;
	}

	private final DTOJahrgang testJahrgang2 = getTestJahrgang2();

	private static DTOJahrgang getTestJahrgang2() {
		final var testJahrgang = new DTOJahrgang(43);
		testJahrgang.InternKrz = "06";
		return testJahrgang;
	}

	@Test
	@DisplayName("Test: Bestimme einen Jahrgang anhand seiner ID.")
	void testGetById() {
		final Long id = 42L;
		when(conn.queryByKey(DTOJahrgang.class, id)).thenReturn(testJahrgang1);

		final DTOJahrgang result = repository.getById(id);

		assertNotNull(result);
		assertEquals(testJahrgang1, result);
		verify(conn).queryByKey(DTOJahrgang.class, id);
	}

	@Test
	@DisplayName("Test: Bestimme mehrere Jahrgänge anhand einer Liste von IDs.")
	void testGetListByIds() {
		final List<Long> ids = Arrays.asList(42L, 43L);
		final List<DTOJahrgang> list = Arrays.asList(testJahrgang1, testJahrgang2);

		when(conn.queryByKeyList(DTOJahrgang.class, ids)).thenReturn(list);

		final List<DTOJahrgang> result = repository.findListByIds(ids);

		assertEquals(2, result.size());
		verify(conn).queryByKeyList(DTOJahrgang.class, ids);
	}

	@Test
	@DisplayName("Test: Erhalten eine leere Liste bei einen Aufruf von getListByIds mit einer leeren Liste oder null ohne eine Datenbank-Aufruf")
	void testGetListByIdsEmpty() {
		final List<DTOJahrgang> resultNull = repository.findListByIds(null);
		final List<DTOJahrgang> resultEmpty = repository.findListByIds(Collections.emptyList());
		assertTrue(resultNull.isEmpty());
		assertTrue(resultEmpty.isEmpty());
		verifyNoInteractions(conn);
	}

	@Test
	@DisplayName("Test: Bestimme alle Jahrgänge.")
	void testGetAll() {
		when(conn.queryAll(DTOJahrgang.class)).thenReturn(Arrays.asList(testJahrgang1));
		final List<DTOJahrgang> result = repository.getAll();
		assertFalse(result.isEmpty());
		verify(conn).queryAll(DTOJahrgang.class);
	}

	@Test
	@DisplayName("Test: Erstelle einen neuen Jahrgang mit der automatischen ID-Zuweisung.")
	void testCreate() {
		final DTOJahrgang neuesFach = new DTOJahrgang(-1);

		final long erwarteteId = 1001L;
		when(conn.transactionGetNextID(DTOJahrgang.class)).thenReturn(erwarteteId);
		when(conn.transactionPersist(neuesFach)).thenReturn(true);

		final DTOJahrgang result = repository.create(neuesFach);

		// Verifizierung
		assertEquals(erwarteteId, result.ID, "Die ID wurde nicht durch den setId-Consumer im Repository korrekt gesetzt.");

		verify(conn).transactionGetNextID(DTOJahrgang.class);
		verify(conn).transactionPersist(neuesFach);
	}

	@Test
	@DisplayName("Test: Aktualisiere bzw. speichere alle Jahrgänge.")
	void testUpdate() {
		when(conn.transactionPersist(testJahrgang1)).thenReturn(true);
		repository.update(testJahrgang1);
		verify(conn).transactionPersist(testJahrgang1);
	}

	@Test
	@DisplayName("Test: Lösche einen Jahrgang")
	void testDelete() {
		when(conn.transactionRemove(testJahrgang1)).thenReturn(true);
		repository.delete(testJahrgang1);
		verify(conn).transactionRemove(testJahrgang1);
	}

}
