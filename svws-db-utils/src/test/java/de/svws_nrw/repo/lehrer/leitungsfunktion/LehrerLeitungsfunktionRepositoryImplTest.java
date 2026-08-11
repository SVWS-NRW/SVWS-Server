package de.svws_nrw.repo.lehrer.leitungsfunktion;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOLeitungsfunktion;
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
class LehrerLeitungsfunktionRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private LehrerLeitungsfunktionRepositoryImpl repository;

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		final var newRepository = new LehrerLeitungsfunktionRepositoryImpl(conn);

		assertThat(newRepository)
				.isNotNull()
				.isInstanceOf(LehrerLeitungsfunktionRepositoryImpl.class)
				.isInstanceOf(LehrerLeitungsfunktionRepository.class);
	}

	@Test
	@DisplayName("existsById | Gibt true zurück wenn Eintrag vorhanden")
	void existsById_returnsTrue_whenEntityExists() {
		final var id = 1L;
		final var entity = new DTOLeitungsfunktion(1L);
		entity.ID = id;

		when(conn.queryByKey(DTOLeitungsfunktion.class, id)).thenReturn(entity);

		final var result = repository.findById(id);

		assertThat(result).isPresent().contains(entity);
		verify(conn, times(1)).queryByKey(DTOLeitungsfunktion.class, id);
	}

	@Test
	@DisplayName("existsById | Gibt false zurück wenn Eintrag nicht vorhanden")
	void existsById_returnsFalse_whenEntityDoesNotExist() {
		final var id = 999L;

		when(conn.queryByKey(DTOLeitungsfunktion.class, id)).thenReturn(null);

		final var result = repository.findById(id);

		assertThat(result).isEmpty();
		verify(conn, times(1)).queryByKey(DTOLeitungsfunktion.class, id);
	}
}
