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
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerMehrleistung;

@ExtendWith(MockitoExtension.class)
class LehrerMehrleistungRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private LehrerMehrleistungRepositoryImpl repository;

	@Test
	@DisplayName("Test: Prüfe, ob getMapByAbschnitt die Mehrleistungen nach Abschnitt-IDs korrekt gruppiert.")
	void testGetMapByAbschnitt() {
		// Stenario: Drei Abschnitte mit zwei, einem bzw. keinem Mehrleistungsgrund
		final List<Long> idsAbschnitte = Arrays.asList(100L, 200L, 300L);
		final DTOLehrerMehrleistung e1 = new DTOLehrerMehrleistung(1L, 100L, "160");
		final DTOLehrerMehrleistung e2 = new DTOLehrerMehrleistung(2L, 100L, "150");
		final DTOLehrerMehrleistung e3 = new DTOLehrerMehrleistung(3L, 200L, "160");

		when(conn.queryList(DTOLehrerMehrleistung.QUERY_LIST_BY_ABSCHNITT_ID, DTOLehrerMehrleistung.class, idsAbschnitte))
				.thenReturn(Arrays.asList(e1, e2, e3));

		final Map<Long, List<DTOLehrerMehrleistung>> result = repository.getMapByAbschnitt(idsAbschnitte);

		// Prüfe, ob die Gruppierung korrekt durchgeführt wurde
		assertNotNull(result);
		assertEquals(3, result.size(), "Die Map sollte Einträge für drei Abschnitte enthalten.");

		// Prüfung Abschnitt 100
		assertEquals(2, result.get(100L).size());
		assertTrue(result.get(100L).contains(e1));
		assertTrue(result.get(100L).contains(e2));

		// Prüfung Abschnitt 200
		assertEquals(1, result.get(200L).size());
		assertEquals(e3, result.get(200L).get(0));

		// Prüfung Abschnitt 300
		assertTrue(result.get(300L).isEmpty());

		verify(conn).queryList(DTOLehrerMehrleistung.QUERY_LIST_BY_ABSCHNITT_ID, DTOLehrerMehrleistung.class, idsAbschnitte);
	}

	@Test
	@DisplayName("Test: Prüfe, ob getMapByAbschnitt bei null oder leeren IDs eine leere Map liefert.")
	void testGetMapByAbschnittEmpty() {
		assertTrue(repository.getMapByAbschnitt(null).isEmpty());
		assertTrue(repository.getMapByAbschnitt(List.of()).isEmpty());
		verifyNoInteractions(conn);
	}

	@Test
	@DisplayName("Test: Prüfe, ob die ID korrekt gesetzt wird.")
	void testCreate() {
		final DTOLehrerMehrleistung neu = new DTOLehrerMehrleistung(-1L, 100L, "160");
		final long neueId = 999L;

		when(conn.transactionGetNextID(DTOLehrerMehrleistung.class)).thenReturn(neueId);
		when(conn.transactionPersist(neu)).thenReturn(true);

		final DTOLehrerMehrleistung result = repository.create(neu);

		assertEquals(neueId, result.ID);
		verify(conn).transactionGetNextID(DTOLehrerMehrleistung.class);
		verify(conn).transactionPersist(neu);
	}

}
