package de.svws_nrw.repo.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramt;

@ExtendWith(MockitoExtension.class)
class LehrerPersonaldatenLehramtRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private LehrerPersonaldatenLehramtRepositoryImpl repository;

	@Test
	@DisplayName("Test: Prüfe, ob getMapByLehrerID die Lehrämter korrekt nach den IDs der Lehrer gruppiert.")
	void testGetMapByLehramt() {
		// Szenario: drei Lehrer, wobei bei dem ersten Lehrer zwei, bei dem zweiten Lehrer ein und beim dritten Lehrer kein Lehramt zugeordnet ist
		final List<Long> idsLehrer = Arrays.asList(500L, 600L, 700L);
		final var l1 = new DTOLehrerPersonaldatenLehramt(1L, 500L);
		final var l2 = new DTOLehrerPersonaldatenLehramt(2L, 500L);
		final var l3 = new DTOLehrerPersonaldatenLehramt(3L, 600L);

		// Anfrage an das Repository
		when(conn.queryList(DTOLehrerPersonaldatenLehramt.QUERY_LIST_BY_LEHRER_ID, DTOLehrerPersonaldatenLehramt.class,
				idsLehrer)).thenReturn(Arrays.asList(l1, l2, l3));
		final Map<Long, List<DTOLehrerPersonaldatenLehramt>> result = repository.getMapByLehrerID(idsLehrer);

		// Prüfe, ob die Anzahl der Einträge in der Map Korrekt ist
		assertNotNull(result);
		assertEquals(3, result.size(), "Die Map sollte drei Einträge beinhalten.");

		// Prüfe, ob Lehrer 500 zwei Einträge hat
		assertEquals(2, result.get(500L).size());
		assertTrue(result.get(500L).contains(l1));
		assertTrue(result.get(500L).contains(l2));

		// Prüfe, ob Lehrer 600 einen Eintrag hat
		assertEquals(1, result.get(600L).size());
		assertEquals(l3, result.get(600L).get(0));

		// Prüfe, ob Lehrer 700 keinen Eintrag hat
		assertTrue(result.get(700L).isEmpty());

		// Eine einzelne Datenbank-Abfrage soll an dieser Stelle genügen.
		verify(conn).queryList(DTOLehrerPersonaldatenLehramt.QUERY_LIST_BY_LEHRER_ID, DTOLehrerPersonaldatenLehramt.class,
				idsLehrer);
	}

	@Test
	@DisplayName("Test: Prüfe, ob getMapByLehrerID bei bei null oder leeren IDs eine leere Map liefert.")
	void testGetMapByLehramtEmpty() {
		assertTrue(repository.getMapByLehrerID(null).isEmpty());
		assertTrue(repository.getMapByLehrerID(List.of()).isEmpty());
		verifyNoInteractions(conn);
	}

	@Test
	@DisplayName("Test: Prüfe, ob die ID korrekt gesetzt wird.")
	void testCreate() {
		final DTOLehrerPersonaldatenLehramt neu = new DTOLehrerPersonaldatenLehramt(1L, 500L);
		final long neueId = 999L;

		when(conn.transactionGetNextID(DTOLehrerPersonaldatenLehramt.class)).thenReturn(neueId);
		when(conn.transactionPersist(neu)).thenReturn(true);

		final DTOLehrerPersonaldatenLehramt result = repository.create(neu);

		assertEquals(neueId, result.ID);
		verify(conn).transactionGetNextID(DTOLehrerPersonaldatenLehramt.class);
		verify(conn).transactionPersist(neu);
	}

}
