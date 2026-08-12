package de.svws_nrw.repo.schueler.foto;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerFoto;
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
class SchuelerFotoRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private SchuelerFotoRepositoryImpl repository;

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		final var newRepository = new SchuelerFotoRepositoryImpl(conn);

		assertThat(newRepository)
				.isNotNull()
				.isInstanceOf(SchuelerFotoRepositoryImpl.class)
				.isInstanceOf(SchuelerFotoRepository.class);
	}

	@Test
	@DisplayName("existsById | Gibt true zurück wenn Eintrag vorhanden")
	void existsById_returnsTrue_whenEntityExists() {
		final var id = 1L;
		final var entity = new DTOSchuelerFoto(1L);

		when(conn.queryByKey(DTOSchuelerFoto.class, id)).thenReturn(entity);

		final var result = repository.findById(id);

		assertThat(result).isPresent().contains(entity);
		verify(conn, times(1)).queryByKey(DTOSchuelerFoto.class, id);
	}

	@Test
	@DisplayName("existsById | Gibt false zurück wenn Eintrag nicht vorhanden")
	void existsById_returnsFalse_whenEntityDoesNotExist() {
		final var id = 999L;

		when(conn.queryByKey(DTOSchuelerFoto.class, id)).thenReturn(null);

		final var result = repository.findById(id);

		assertThat(result).isEmpty();
		verify(conn, times(1)).queryByKey(DTOSchuelerFoto.class, id);
	}

}
