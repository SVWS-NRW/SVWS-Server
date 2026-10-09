package de.svws_nrw.repo.lehrer.foto;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerFoto;
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
class LehrerFotoRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private LehrerFotoRepositoryImpl repository;

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		final var newRepository = new LehrerFotoRepositoryImpl(conn);

		assertThat(newRepository)
				.isNotNull()
				.isInstanceOf(LehrerFotoRepositoryImpl.class)
				.isInstanceOf(LehrerFotoRepository.class);
	}

	@Test
	@DisplayName("findById | Gibt Eintrag zurück wenn vorhanden")
	void findById_returnsEntity_ifEntityExists() {
		final var id = 1L;
		final var entity = new DTOLehrerFoto(1L);

		when(conn.queryByKey(DTOLehrerFoto.class, id)).thenReturn(entity);

		final var result = repository.findById(id);

		assertThat(result).isPresent().contains(entity);
		verify(conn, times(1)).queryByKey(DTOLehrerFoto.class, id);
	}

	@Test
	@DisplayName("findById | Gibt leer zurück wenn Eintrag nicht vorhanden")
	void findById_returnsEmpty_ifEntityDoesNotExist() {
		final var id = 999L;

		when(conn.queryByKey(DTOLehrerFoto.class, id)).thenReturn(null);

		final var result = repository.findById(id);

		assertThat(result).isEmpty();
		verify(conn, times(1)).queryByKey(DTOLehrerFoto.class, id);
	}

	@Test
	@DisplayName("autoAssignId | gibt false zurück")
	void autoAssignId_returnsFalse() {
		assertThat(repository.autoAssignId()).isFalse();
	}

}
