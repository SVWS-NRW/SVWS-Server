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
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAnrechnungsstunde;

@ExtendWith(MockitoExtension.class)
class LehrerAnrechnungRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private LehrerAnrechnungRepositoryImpl repository;

	@Test
	@DisplayName("Test: Prüfe, ob getMapByAbschnitt die Anrechnungen nach Abschnitt-IDs korrekt gruppiert.")
	void testGetMapByAbschnitt() {
		// Stenario: Drei Abschnitte mit zwei, einem bzw. keinem Anrechnungsgrund
		final List<Long> idsAbschnitte = Arrays.asList(100L, 200L, 300L);
		final DTOLehrerAnrechnungsstunde e1 = new DTOLehrerAnrechnungsstunde(1L, 100L);
		final DTOLehrerAnrechnungsstunde e2 = new DTOLehrerAnrechnungsstunde(2L, 100L);
		final DTOLehrerAnrechnungsstunde e3 = new DTOLehrerAnrechnungsstunde(3L, 200L);

		when(conn.queryList(DTOLehrerAnrechnungsstunde.QUERY_LIST_BY_ABSCHNITT_ID, DTOLehrerAnrechnungsstunde.class, idsAbschnitte))
				.thenReturn(Arrays.asList(e1, e2, e3));

		final Map<Long, List<DTOLehrerAnrechnungsstunde>> result = repository.getMapByAbschnitt(idsAbschnitte);

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

		verify(conn).queryList(DTOLehrerAnrechnungsstunde.QUERY_LIST_BY_ABSCHNITT_ID, DTOLehrerAnrechnungsstunde.class, idsAbschnitte);
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
		final DTOLehrerAnrechnungsstunde neu = new DTOLehrerAnrechnungsstunde(-1L, 100L);
		final long neueId = 999L;

		when(conn.transactionGetNextID(DTOLehrerAnrechnungsstunde.class)).thenReturn(neueId);
		when(conn.transactionPersist(neu)).thenReturn(true);

		final DTOLehrerAnrechnungsstunde result = repository.create(neu);

		assertEquals(neueId, result.ID);
		verify(conn).transactionGetNextID(DTOLehrerAnrechnungsstunde.class);
		verify(conn).transactionPersist(neu);
	}

}
