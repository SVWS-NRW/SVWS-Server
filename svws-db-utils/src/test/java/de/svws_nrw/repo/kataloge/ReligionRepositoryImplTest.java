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
import de.svws_nrw.db.dto.current.schild.katalog.DTOKonfession;

@ExtendWith(MockitoExtension.class)
class ReligionRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private ReligionRepositoryImpl repository;

	@Test
	@DisplayName("Test: Erstelle einen neuen Eintrag und prüfe die Zuweisung der neuen ID.")
	void testCreate() {
		final DTOKonfession neu = new DTOKonfession(-1L, "evangelisch");

		final long neueId = 42L;

		when(conn.transactionGetNextID(DTOKonfession.class)).thenReturn(neueId);
		when(conn.transactionPersist(neu)).thenReturn(true);

		final DTOKonfession result = repository.create(neu);

		assertNotNull(result);
		assertEquals(neueId, result.ID, "Die ID wurde nicht korrekt gesetzt.");
		verify(conn).transactionGetNextID(DTOKonfession.class);
		verify(conn).transactionPersist(neu);
	}

	@Test
	@DisplayName("Test: Bestimme einen Eintrag anhand der ID.")
	void testGetById() {
		final Long id = 42L;
		final DTOKonfession entity = new DTOKonfession(id, "evangelisch");

		when(conn.queryByKey(DTOKonfession.class, id)).thenReturn(entity);

		final DTOKonfession result = repository.getById(id);

		assertNotNull(result);
		assertEquals("evangelisch", result.Bezeichnung);
		verify(conn).queryByKey(DTOKonfession.class, id);
	}

}
