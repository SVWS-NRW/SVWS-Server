package de.svws_nrw.repo.schule.kataloge.ankreuzkompetenz;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOAnkreuzdaten;

@ExtendWith(MockitoExtension.class)
class AnkreuzkompetenzKonfigurationRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private AnkreuzkompetenzKonfigurationRepositoryImpl repository;

	@Test
	@DisplayName("Test: Bestimme einen Eintrag anhand der ID.")
	void testGetById() {
		final Long id = 42L;
		final DTOAnkreuzdaten entity = new DTOAnkreuzdaten(id);

		when(conn.queryByKey(DTOAnkreuzdaten.class, id)).thenReturn(entity);

		final DTOAnkreuzdaten result = repository.getById(id);

		assertNotNull(result);
		verify(conn).queryByKey(DTOAnkreuzdaten.class, id);
	}


	@Test
	@DisplayName("Test: Der Konstruktor setzt die ID-Zugriffe (getId/setId) korrekt auf das ID-Feld.")
	void testConstructorAndIdAccess() {
		// Teste getId indirekt via getMap()
		final DTOAnkreuzdaten k = new DTOAnkreuzdaten(999L);
		when(conn.queryAll(DTOAnkreuzdaten.class)).thenReturn(List.of(k));
		assertThat(repository.getMap()).containsEntry(999L, k);

		// Teste setId über die create-Methode
		when(conn.transactionGetNextID(DTOAnkreuzdaten.class)).thenReturn(1000L);
		when(conn.transactionPersist(k)).thenReturn(true);
		repository.create(k);
		assertThat(k.ID).isEqualTo(1000L);
	}

}
