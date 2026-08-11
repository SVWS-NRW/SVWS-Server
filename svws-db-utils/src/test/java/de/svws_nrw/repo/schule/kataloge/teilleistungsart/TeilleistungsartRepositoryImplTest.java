package de.svws_nrw.repo.schule.kataloge.teilleistungsart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import de.svws_nrw.db.dto.current.schild.schueler.DTOTeilleistungsarten;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.DBEntityManager;

@ExtendWith(MockitoExtension.class)
class TeilleistungsartRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private TeilleistungsartRepositoryImpl repository;

	@Test
	@DisplayName("Test: Erstelle einen neuen Eintrag und prüfe die Zuweisung der neuen ID.")
	void testCreate() {
		final DTOTeilleistungsarten neu = new DTOTeilleistungsarten(-1L);

		final long neueId = 42L;

		when(conn.transactionGetNextID(DTOTeilleistungsarten.class)).thenReturn(neueId);
		when(conn.transactionPersist(neu)).thenReturn(true);

		final DTOTeilleistungsarten result = repository.create(neu);

		assertNotNull(result);
		assertEquals(neueId, result.ID, "Die ID wurde nicht korrekt gesetzt.");
		verify(conn).transactionGetNextID(DTOTeilleistungsarten.class);
		verify(conn).transactionPersist(neu);
	}

	@Test
	@DisplayName("Test: Der Konstruktor setzt die ID-Zugriffe (getId/setId) korrekt auf das ID-Feld.")
	void testConstructorAndIdAccess() {
		// Teste getId indirekt via getMap()
		final DTOTeilleistungsarten k = new DTOTeilleistungsarten(999L);
		when(conn.queryAll(DTOTeilleistungsarten.class)).thenReturn(List.of(k));
		assertThat(repository.getMap()).containsEntry(999L, k);

		// Teste setId über die create-Methode
		when(conn.transactionGetNextID(DTOTeilleistungsarten.class)).thenReturn(1000L);
		when(conn.transactionPersist(k)).thenReturn(true);
		repository.create(k);
		assertThat(k.ID).isEqualTo(1000L);
	}

}
