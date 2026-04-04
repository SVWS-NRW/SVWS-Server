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
import de.svws_nrw.db.dto.current.notenmodul.DTONotenmodulCredentials;

/**
 * Test für die Klasse {@link NotenmodulCredentialsRepositoryImpl}
 */
@ExtendWith(MockitoExtension.class)
class NotenmodulCredentialsRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private NotenmodulCredentialsRepositoryImpl repository;


	@Test
	@DisplayName("Test: Der Konstruktor setzt die ID-Zugriffe (getId/setId) korrekt auf das ID-Feld von DTONotenmodulCredentials.")
	void testConstructorAndIdAccess() {
		// Teste getId indirekt via getMap()
		final DTONotenmodulCredentials k = new DTONotenmodulCredentials(999L, "Streng Geheim", "Ein Hash evtl. von dem PW", 0, true);
		when(conn.queryAll(DTONotenmodulCredentials.class)).thenReturn(List.of(k));
		assertThat(repository.getMap()).containsEntry(999L, k);

		// Teste setId über die create-Methode
		when(conn.transactionGetNextID(DTONotenmodulCredentials.class)).thenReturn(1000L);
		when(conn.transactionPersist(k)).thenReturn(true);
		repository.create(k);
		assertThat(k.idLehrer).isEqualTo(1000L);
	}

}
