package de.svws_nrw.repo.schule;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

}
