package de.svws_nrw.repo.schule.kataloge.floskel;

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
import de.svws_nrw.db.dto.current.katalog.DTOFloskeln;

@ExtendWith(MockitoExtension.class)
class FloskelRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private FloskelRepositoryImpl repository;

	@Test
	@DisplayName("Test: Bestimme einen Eintrag anhand der ID.")
	void testGetById() {
		final Long id = 42L;
		final DTOFloskeln entity = new DTOFloskeln(id, "Kürzel", "Ein Floskeltext");

		when(conn.queryByKey(DTOFloskeln.class, id)).thenReturn(entity);

		final DTOFloskeln result = repository.getById(id);

		assertNotNull(result);
		assertEquals("Kürzel", result.Kuerzel);
		assertEquals("Ein Floskeltext", result.Text);
		verify(conn).queryByKey(DTOFloskeln.class, id);
	}


	@Test
	@DisplayName("Test: Der Konstruktor setzt die ID-Zugriffe (getId/setId) korrekt auf das ID-Feld.")
	void testConstructorAndIdAccess() {
		// Teste getId indirekt via getMap()
		final DTOFloskeln k = new DTOFloskeln(999L, "Kürzel", "Ein Floskeltext");
		when(conn.queryAll(DTOFloskeln.class)).thenReturn(List.of(k));
		assertThat(repository.getMap()).containsEntry(999L, k);

		// Teste setId über die create-Methode
		when(conn.transactionGetNextID(DTOFloskeln.class)).thenReturn(1000L);
		when(conn.transactionPersist(k)).thenReturn(true);
		repository.create(k);
		assertThat(k.ID).isEqualTo(1000L);
	}

}
