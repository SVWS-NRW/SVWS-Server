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
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramtFachrichtung;


@ExtendWith(MockitoExtension.class)
class LehrerPersonaldatenLehramtFachrichtungRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private LehrerPersonaldatenLehramtFachrichtungRepositoryImpl repository;

	@Test
	@DisplayName("Test: Prüfe, ob getMapByLehramt die Fachrichtungen korrekt nach den IDs der Lehrämter gruppiert.")
	void testGetMapByLehramt() {
		// Szenario: drei Lehrämter, wobei bei dem ersten Lehramt zwei, bei dem zweiten Lehramt eine und beim dritten Lehramt keine Fachrichtung zugeordnet ist
		final List<Long> idsLehraemter = Arrays.asList(500L, 600L, 700L);
		final var f1 = new DTOLehrerPersonaldatenLehramtFachrichtung(1L, 500L, 03L);
		final var f2 = new DTOLehrerPersonaldatenLehramtFachrichtung(2L, 500L, 04L);
		final var f3 = new DTOLehrerPersonaldatenLehramtFachrichtung(3L, 600L, 05L);

		// Anfrage an das Repository
		when(conn.queryList(DTOLehrerPersonaldatenLehramtFachrichtung.QUERY_LIST_BY_LEHRERAMT_ID, DTOLehrerPersonaldatenLehramtFachrichtung.class,
				idsLehraemter)).thenReturn(Arrays.asList(f1, f2, f3));
		final Map<Long, List<DTOLehrerPersonaldatenLehramtFachrichtung>> result = repository.getMapByLehramt(idsLehraemter);

		// Prüfe, ob die Anzahl der Einträge in der Map Korrekt ist
		assertNotNull(result);
		assertEquals(3, result.size(), "Die Map sollte drei Einträge beinhalten.");

		// Prüfe, ob Lehramt 500 zwei Einträge hat
		assertEquals(2, result.get(500L).size());
		assertTrue(result.get(500L).contains(f1));
		assertTrue(result.get(500L).contains(f2));

		// Prüfe, ob Lehramt 600 einen Eintrag hat
		assertEquals(1, result.get(600L).size());
		assertEquals(f3, result.get(600L).get(0));

		// Prüfe, ob Lehramt 700 keinen Eintrag hat
		assertTrue(result.get(700L).isEmpty());

		// Eine einzelne Datenbank-Abfrage soll an dieser Stelle genügen.
		verify(conn).queryList(DTOLehrerPersonaldatenLehramtFachrichtung.QUERY_LIST_BY_LEHRERAMT_ID, DTOLehrerPersonaldatenLehramtFachrichtung.class,
				idsLehraemter);
	}

	@Test
	@DisplayName("Test: Prüfe, ob getMapByLehramt bei bei null oder leeren IDs eine leere Map liefert.")
	void testGetMapByLehramtEmpty() {
		assertTrue(repository.getMapByLehramt(null).isEmpty());
		assertTrue(repository.getMapByLehramt(List.of()).isEmpty());
		verifyNoInteractions(conn);
	}

	@Test
	@DisplayName("Test: Prüfe, ob die ID korrekt gesetzt wird.")
	void testCreate() {
		final DTOLehrerPersonaldatenLehramtFachrichtung neu = new DTOLehrerPersonaldatenLehramtFachrichtung(1L, 500L, 03L);
		final long neueId = 999L;

		when(conn.transactionGetNextID(DTOLehrerPersonaldatenLehramtFachrichtung.class)).thenReturn(neueId);
		when(conn.transactionPersist(neu)).thenReturn(true);

		final DTOLehrerPersonaldatenLehramtFachrichtung result = repository.create(neu);

		assertEquals(neueId, result.ID);
		verify(conn).transactionGetNextID(DTOLehrerPersonaldatenLehramtFachrichtung.class);
		verify(conn).transactionPersist(neu);
	}

}
