package de.svws_nrw.repo.kataloge;

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
import de.svws_nrw.db.dto.current.schild.schueler.DTOFoerderschwerpunkt;

@ExtendWith(MockitoExtension.class)
class FoerderschwerpunkteRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private FoerderschwerpunkteRepositoryImpl repository;

	@Test
	@DisplayName("Test: Erstelle einen neuen Eintrag und prüfe die Zuweisung der neuen ID.")
	void testCreate() {
		final DTOFoerderschwerpunkt neu = new DTOFoerderschwerpunkt(-1L, "Lernen");

		final long neueId = 42L;

		when(conn.transactionGetNextID(DTOFoerderschwerpunkt.class)).thenReturn(neueId);
		when(conn.transactionPersist(neu)).thenReturn(true);

		final DTOFoerderschwerpunkt result = repository.create(neu);

		assertNotNull(result);
		assertEquals(neueId, result.ID, "Die ID wurde nicht korrekt gesetzt.");
		verify(conn).transactionGetNextID(DTOFoerderschwerpunkt.class);
		verify(conn).transactionPersist(neu);
	}

	@Test
	@DisplayName("Test: Bestimme einen Eintrag anhand der ID.")
	void testGetById() {
		final Long id = 42L;
		final DTOFoerderschwerpunkt entity = new DTOFoerderschwerpunkt(id, "Lernen");

		when(conn.queryByKey(DTOFoerderschwerpunkt.class, id)).thenReturn(entity);

		final DTOFoerderschwerpunkt result = repository.getById(id);

		assertNotNull(result);
		assertEquals("Lernen", result.Bezeichnung);
		verify(conn).queryByKey(DTOFoerderschwerpunkt.class, id);
	}

}
