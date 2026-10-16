package de.svws_nrw.repo.schule.kataloge.schuelerschwerpunkt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOSchuelerSchwerpunkt;

@ExtendWith(MockitoExtension.class)
class SchuelerschwerpunktRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private SchuelerschwerpunktRepositoryImpl repository;

	@Test
	@DisplayName("Test: Erstelle einen neuen Eintrag und prüfe die Zuweisung der neuen ID.")
	void testCreate() {
		final DTOSchuelerSchwerpunkt neu = new DTOSchuelerSchwerpunkt(-1L, "sozialwissenschaftlich");

		final long neueId = 42L;

		when(conn.transactionGetNextID(DTOSchuelerSchwerpunkt.class)).thenReturn(neueId);
		when(conn.transactionPersist(neu)).thenReturn(true);

		final DTOSchuelerSchwerpunkt result = repository.create(neu);

		assertNotNull(result);
		assertEquals(neueId, result.ID, "Die ID wurde nicht korrekt gesetzt.");
		verify(conn).transactionGetNextID(DTOSchuelerSchwerpunkt.class);
		verify(conn).transactionPersist(neu);
	}

	@Test
	@DisplayName("Test: Bestimme einen Eintrag anhand der ID.")
	void testGetById() {
		final Long id = 42L;
		final DTOSchuelerSchwerpunkt entity = new DTOSchuelerSchwerpunkt(id, "sozialwissenschaftlich");

		when(conn.queryByKey(DTOSchuelerSchwerpunkt.class, id)).thenReturn(entity);

		final DTOSchuelerSchwerpunkt result = repository.getById(id);

		assertNotNull(result);
		assertEquals("sozialwissenschaftlich", result.Bezeichnung);
		verify(conn).queryByKey(DTOSchuelerSchwerpunkt.class, id);
	}

	@Test
	@DisplayName("Test: findById liefert ein leeres Optional, wenn kein Eintrag mit der ID existiert.")
	void testFindByIdNichtVorhanden() {
		final Long id = 99L;

		when(conn.queryByKey(DTOSchuelerSchwerpunkt.class, id)).thenReturn(null);

		assertTrue(repository.findById(id).isEmpty());
		verify(conn).queryByKey(DTOSchuelerSchwerpunkt.class, id);
	}

}
