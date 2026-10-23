package de.svws_nrw.repo.schule.schulinternefunktion;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOSchulinterneFunktion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchulinterneFunktionRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private SchulinterneFunktionRepositoryImpl repository;

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		final var newRepository = new SchulinterneFunktionRepositoryImpl(conn);

		assertThat(newRepository)
				.isNotNull()
				.isInstanceOf(SchulinterneFunktionRepositoryImpl.class)
				.isInstanceOf(SchulinterneFunktionRepository.class);
	}

	@Test
	@DisplayName("existsById | Gibt true zurück wenn Eintrag vorhanden")
	void existsById_returnsTrue_whenEntityExists() {
		final var id = 1L;
		final var entity = new DTOSchulinterneFunktion(1L);
		entity.id = id;

		when(conn.queryByKey(DTOSchulinterneFunktion.class, id)).thenReturn(entity);

		final var result = repository.findById(id);

		assertThat(result).isPresent().contains(entity);
		verify(conn, times(1)).queryByKey(DTOSchulinterneFunktion.class, id);
	}

	@Test
	@DisplayName("existsById | Gibt false zurück wenn Eintrag nicht vorhanden")
	void existsById_returnsFalse_whenEntityDoesNotExist() {
		final var id = 999L;

		when(conn.queryByKey(DTOSchulinterneFunktion.class, id)).thenReturn(null);

		final var result = repository.findById(id);

		assertThat(result).isEmpty();
		verify(conn, times(1)).queryByKey(DTOSchulinterneFunktion.class, id);
	}
}
