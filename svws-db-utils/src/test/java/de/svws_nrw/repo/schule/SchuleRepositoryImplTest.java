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
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;

@ExtendWith(MockitoExtension.class)
class SchuleRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private SchuleRepositoryImpl repository;

	@Test
	@DisplayName("Test: Erstelle einen neuen Eintrag und prüfe die Zuweisung der neuen ID.")
	void testCreateSchule() {
		final DTOEigeneSchule neu = new DTOEigeneSchule(-1L);

		final long neueId = 42L;

		when(conn.transactionGetNextID(DTOEigeneSchule.class)).thenReturn(neueId);
		when(conn.transactionPersist(neu)).thenReturn(true);

		final DTOEigeneSchule result = repository.create(neu);

		assertNotNull(result);
		assertEquals(neueId, result.ID, "Die ID wurde nicht korrekt gesetzt.");
		verify(conn).transactionGetNextID(DTOEigeneSchule.class);
		verify(conn).transactionPersist(neu);
	}

	@Test
	@DisplayName("Test: Bestimme einen Eintrag anhand der ID.")
	void testGetById() {
		final Long id = 42L;
		final DTOEigeneSchule entity = new DTOEigeneSchule(id);

		when(conn.queryByKey(DTOEigeneSchule.class, id)).thenReturn(entity);

		final DTOEigeneSchule result = repository.getById(id);

		assertNotNull(result);
		verify(conn).queryByKey(DTOEigeneSchule.class, id);
	}


	@Test
	@DisplayName("Test: Bestimme den aktuellen Schuljahresabschnitt über den Schuleintrag.")
	void testGetSchuljahresabschnitt() {
		// Szenario: Die Schule befindet sich in dem Schuljahresabschnitt mit der ID 42
		final long idSchuljahresabschnitt = 42L;
		final DTOEigeneSchule schule = new DTOEigeneSchule(1L);
		schule.Schuljahresabschnitts_ID = idSchuljahresabschnitt;

		when(conn.querySingle(DTOEigeneSchule.class)).thenReturn(schule);
		final long result = repository.getSchuljahresabschnitt();

		assertEquals(idSchuljahresabschnitt, result, "Die ID des Schuljahresabschnitts wurde nicht korrekt ausgelesen.");
		verify(conn).querySingle(DTOEigeneSchule.class);
	}

}
