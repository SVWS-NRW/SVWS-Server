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
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;

@ExtendWith(MockitoExtension.class)
class OrteRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private OrteRepositoryImpl repository;

	@Test
	@DisplayName("Test: Erstelle einen neuen Eintrag und prüfe die Zuweisung der neuen ID.")
	void testCreate() {
		final DTOOrt neu = new DTOOrt(-1L, "40190", "Düsseldorf");

		final long neueId = 42L;

		when(conn.transactionGetNextID(DTOOrt.class)).thenReturn(neueId);
		when(conn.transactionPersist(neu)).thenReturn(true);

		final DTOOrt result = repository.create(neu);

		assertNotNull(result);
		assertEquals(neueId, result.ID, "Die ID wurde nicht korrekt gesetzt.");
		verify(conn).transactionGetNextID(DTOOrt.class);
		verify(conn).transactionPersist(neu);
	}

	@Test
	@DisplayName("Test: Bestimme einen Eintrag anhand der ID.")
	void testGetById() {
		final Long id = 42L;
		final DTOOrt entity = new DTOOrt(id, "40190", "Düsseldorf");

		when(conn.queryByKey(DTOOrt.class, id)).thenReturn(entity);

		final DTOOrt result = repository.getById(id);

		assertNotNull(result);
		assertEquals("40190", result.PLZ);
		assertEquals("Düsseldorf", result.Bezeichnung);
		verify(conn).queryByKey(DTOOrt.class, id);
	}

}
