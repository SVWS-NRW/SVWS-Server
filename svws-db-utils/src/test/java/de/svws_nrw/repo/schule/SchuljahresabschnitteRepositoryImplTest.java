package de.svws_nrw.repo.schule;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;

@ExtendWith(MockitoExtension.class)
class SchuljahresabschnitteRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private SchuljahresabschnitteRepositoryImpl repository;

	@Test
	@DisplayName("Test: Erstelle einen neuen Eintrag und prüfe die Zuweisung der neuen ID.")
	void testCreate() {
		final DTOSchuljahresabschnitte neu = new DTOSchuljahresabschnitte(-1L, 2026, 1);

		final long neueId = 42L;

		when(conn.transactionGetNextID(DTOSchuljahresabschnitte.class)).thenReturn(neueId);
		when(conn.transactionPersist(neu)).thenReturn(true);

		final DTOSchuljahresabschnitte result = repository.create(neu);

		assertNotNull(result);
		assertEquals(neueId, result.ID, "Die ID wurde nicht korrekt gesetzt.");
		verify(conn).transactionGetNextID(DTOSchuljahresabschnitte.class);
		verify(conn).transactionPersist(neu);
	}

	@Test
	@DisplayName("Test: Bestimme einen Eintrag anhand der ID.")
	void testGetById() {
		final Long id = 42L;
		final DTOSchuljahresabschnitte entity = new DTOSchuljahresabschnitte(id, 2026, 1);

		when(conn.queryByKey(DTOSchuljahresabschnitte.class, id)).thenReturn(entity);

		final DTOSchuljahresabschnitte result = repository.getById(id);

		assertNotNull(result);
		assertEquals(2026, result.Jahr);
		assertEquals(1, result.Abschnitt);
		verify(conn).queryByKey(DTOSchuljahresabschnitte.class, id);
	}

	@Test
	@DisplayName("Test: Bestimme die Schuljahresabschnitte anhand der Schuljahre.")
	void testFindListBySchuljahre() {
		final var schuljahre = List.of(2025, 2026);
		final var entities = List.of(new DTOSchuljahresabschnitte(1L, 2025, 1), new DTOSchuljahresabschnitte(2L, 2026, 2));

		when(conn.queryList(anyString(), eq(DTOSchuljahresabschnitte.class), eq(schuljahre))).thenReturn(entities);

		final var result = repository.findListBySchuljahre(schuljahre);

		assertEquals(entities, result);
	}

	@Test
	@DisplayName("Test: Ohne Schuljahre wird eine leere Liste geliefert, ohne die Datenbank abzufragen.")
	void testFindListBySchuljahreOhneSchuljahre() {
		assertTrue(repository.findListBySchuljahre(List.of()).isEmpty());
		assertTrue(repository.findListBySchuljahre(null).isEmpty());
		verifyNoInteractions(conn);
	}

}
