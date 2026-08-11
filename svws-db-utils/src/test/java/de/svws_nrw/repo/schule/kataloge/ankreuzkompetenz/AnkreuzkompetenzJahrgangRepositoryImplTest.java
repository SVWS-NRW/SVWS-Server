package de.svws_nrw.repo.schule.kataloge.ankreuzkompetenz;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import de.svws_nrw.db.dto.current.katalog.DTOAnkreuzkompetenzJahrgang;

@ExtendWith(MockitoExtension.class)
class AnkreuzkompetenzJahrgangRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private AnkreuzkompetenzJahrgangRepositoryImpl repository;

	@Test
	@DisplayName("Test: Bestimme einen Eintrag anhand der ID.")
	void testGetById() {
		final long id = 42L;
		final long idAnkreuzkompetenz = 4711L;
		final long idJahrgang = 10L;
		final DTOAnkreuzkompetenzJahrgang entity = new DTOAnkreuzkompetenzJahrgang(id, idAnkreuzkompetenz, idJahrgang);

		when(conn.queryByKey(DTOAnkreuzkompetenzJahrgang.class, id)).thenReturn(entity);

		final DTOAnkreuzkompetenzJahrgang result = repository.getById(id);

		assertNotNull(result);
		assertEquals(idAnkreuzkompetenz, result.idAnkreuzkompetenz);
		assertEquals(idJahrgang, result.idJahrgang);
		verify(conn).queryByKey(DTOAnkreuzkompetenzJahrgang.class, id);
	}


	@Test
	@DisplayName("Test: Der Konstruktor setzt die ID-Zugriffe (getId/setId) korrekt auf das ID-Feld.")
	void testConstructorAndIdAccess() {
		// Teste getId indirekt via getMap()
		final DTOAnkreuzkompetenzJahrgang k = new DTOAnkreuzkompetenzJahrgang(999L, 4711L, 10L);
		when(conn.queryAll(DTOAnkreuzkompetenzJahrgang.class)).thenReturn(List.of(k));
		assertThat(repository.getMap()).containsEntry(999L, k);

		// Teste setId über die create-Methode
		when(conn.transactionGetNextID(DTOAnkreuzkompetenzJahrgang.class)).thenReturn(1000L);
		when(conn.transactionPersist(k)).thenReturn(true);
		repository.create(k);
		assertThat(k.id).isEqualTo(1000L);
	}

}
