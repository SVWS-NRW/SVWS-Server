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

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.abitur.DTOSchuelerAbitur;

@ExtendWith(MockitoExtension.class)
class SchuelerAbiturRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private SchuelerAbiturRepositoryImpl repository;

	private final DTOSchuelerAbitur testAbitur1 = new DTOSchuelerAbitur(1L, 100L);
	private final DTOSchuelerAbitur testAbitur2 = new DTOSchuelerAbitur(2L, 101L);


	@Test
	@DisplayName("Test: Prüfe, ob getListBySchuelerIds die Abiturdaten für mehrere Schüler korrekt liefert.")
	void testGetListBySchuelerIds() {
		final List<Long> idsSchueler = Arrays.asList(100L, 101L);

		when(conn.queryList(DTOSchuelerAbitur.QUERY_LIST_BY_SCHUELER_ID, DTOSchuelerAbitur.class, idsSchueler))
				.thenReturn(Arrays.asList(testAbitur1, testAbitur2));

		final List<DTOSchuelerAbitur> result = repository.getListBySchuelerIds(idsSchueler);

		assertNotNull(result);
		assertEquals(2, result.size());
		assertTrue(result.contains(testAbitur1));
		assertTrue(result.contains(testAbitur2));
		verify(conn).queryList(DTOSchuelerAbitur.QUERY_LIST_BY_SCHUELER_ID, DTOSchuelerAbitur.class, idsSchueler);
	}

	@Test
	@DisplayName("Test: Prüfe, ob getListBySchuelerIds eine leere Liste bei null oder leeren IDs ohne Datenbank-Interaktion liefert.")
	void testGetListBySchuelerIdsEmpty() {
		assertTrue(repository.getListBySchuelerIds(null).isEmpty());
		assertTrue(repository.getListBySchuelerIds(Collections.emptyList()).isEmpty());
		verifyNoInteractions(conn);
	}

	@Test
	@DisplayName("Test: Prüfe, ob die ID bei create über den Consumer korrekt gesetzt wird.")
	void testCreate() {
		final DTOSchuelerAbitur neu = new DTOSchuelerAbitur(1L, 100L);
		final long neueId = 42L;

		when(conn.transactionGetNextID(DTOSchuelerAbitur.class)).thenReturn(neueId);
		when(conn.transactionPersist(neu)).thenReturn(true);

		final DTOSchuelerAbitur result = repository.create(neu);

		assertEquals(neueId, result.ID);
		verify(conn).transactionGetNextID(DTOSchuelerAbitur.class);
		verify(conn).transactionPersist(neu);
	}

}
