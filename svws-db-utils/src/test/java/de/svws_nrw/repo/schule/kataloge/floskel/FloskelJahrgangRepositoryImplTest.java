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
import de.svws_nrw.db.dto.current.katalog.DTOFloskelnJahrgaenge;

@ExtendWith(MockitoExtension.class)
class FloskelJahrgangRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private FloskelJahrgangRepositoryImpl repository;

	@Test
	@DisplayName("Test: Bestimme einen Eintrag anhand der ID.")
	void testGetById() {
		final long id = 42L;
		final long idFloskel = 43L;
		final long idJahrgang = 44L;
		final DTOFloskelnJahrgaenge entity = new DTOFloskelnJahrgaenge(id, idFloskel, idJahrgang);

		when(conn.queryByKey(DTOFloskelnJahrgaenge.class, id)).thenReturn(entity);

		final DTOFloskelnJahrgaenge result = repository.getById(id);

		assertNotNull(result);
		assertEquals(idFloskel, result.Floskel_ID);
		assertEquals(idJahrgang, result.Jahrgang_ID);
		verify(conn).queryByKey(DTOFloskelnJahrgaenge.class, id);
	}


	@Test
	@DisplayName("Test: Der Konstruktor setzt die ID-Zugriffe (getId/setId) korrekt auf das ID-Feld.")
	void testConstructorAndIdAccess() {
		// Teste getId indirekt via getMap()
		final DTOFloskelnJahrgaenge k = new DTOFloskelnJahrgaenge(999L, 4711L, 10L);
		when(conn.queryAll(DTOFloskelnJahrgaenge.class)).thenReturn(List.of(k));
		assertThat(repository.getMap()).containsEntry(999L, k);

		// Teste setId über die create-Methode
		when(conn.transactionGetNextID(DTOFloskelnJahrgaenge.class)).thenReturn(1000L);
		when(conn.transactionPersist(k)).thenReturn(true);
		repository.create(k);
		assertThat(k.ID).isEqualTo(1000L);
	}

}
