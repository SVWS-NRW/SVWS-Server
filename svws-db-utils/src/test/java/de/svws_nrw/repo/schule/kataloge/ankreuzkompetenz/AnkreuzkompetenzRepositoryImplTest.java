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
import de.svws_nrw.db.dto.current.schild.grundschule.DTOAnkreuzfloskeln;

@ExtendWith(MockitoExtension.class)
class AnkreuzkompetenzRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private AnkreuzkompetenzRepositoryImpl repository;

	@Test
	@DisplayName("Test: Bestimme einen Eintrag anhand der ID.")
	void testGetById() {
		final Long id = 42L;
		final DTOAnkreuzfloskeln entity = new DTOAnkreuzfloskeln(id, 0, "Eine Floskel");

		when(conn.queryByKey(DTOAnkreuzfloskeln.class, id)).thenReturn(entity);

		final DTOAnkreuzfloskeln result = repository.getById(id);

		assertNotNull(result);
		assertEquals(0, result.IstASV);
		assertEquals("Eine Floskel", result.FloskelText);
		verify(conn).queryByKey(DTOAnkreuzfloskeln.class, id);
	}


	@Test
	@DisplayName("Test: Der Konstruktor setzt die ID-Zugriffe (getId/setId) korrekt auf das ID-Feld.")
	void testConstructorAndIdAccess() {
		// Teste getId indirekt via getMap()
		final DTOAnkreuzfloskeln k = new DTOAnkreuzfloskeln(999L, 0, "Eine Floskel");
		when(conn.queryAll(DTOAnkreuzfloskeln.class)).thenReturn(List.of(k));
		assertThat(repository.getMap()).containsEntry(999L, k);

		// Teste setId über die create-Methode
		when(conn.transactionGetNextID(DTOAnkreuzfloskeln.class)).thenReturn(1000L);
		when(conn.transactionPersist(k)).thenReturn(true);
		repository.create(k);
		assertThat(k.ID).isEqualTo(1000L);
	}

}
