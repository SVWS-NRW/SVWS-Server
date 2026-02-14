package de.svws_nrw.repo.enm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.notenmodul.DTONotenmodulVerbindungen;

/**
 * Test für die Klasse {@link NotenmodulVerbindungenRepositoryImpl}
 */
@ExtendWith(MockitoExtension.class)
class NotenmodulVerbindungenRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private NotenmodulVerbindungenRepositoryImpl repository;


	@Test
	@DisplayName("Test: Der Konstruktor setzt die ID-Zugriffe (getId/setId) korrekt auf das ID-Feld von DTONotenmodulVerbindungen.")
	void testConstructorAndIdAccess() {
		// Teste getId indirekt via getMap()
		final DTONotenmodulVerbindungen k = new DTONotenmodulVerbindungen(999L, "URL", "Client-ID", "Client-Secret");
		when(conn.queryAll(DTONotenmodulVerbindungen.class)).thenReturn(List.of(k));
		assertThat(repository.getMap()).containsEntry(999L, k);

		// Teste setId über die create-Methode
		when(conn.transactionGetNextID(DTONotenmodulVerbindungen.class)).thenReturn(1000L);
		when(conn.transactionPersist(k)).thenReturn(true);
		repository.create(k);
		assertThat(k.id).isEqualTo(1000L);
	}

}
