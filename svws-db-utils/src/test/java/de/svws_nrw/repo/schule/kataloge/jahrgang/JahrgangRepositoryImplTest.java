package de.svws_nrw.repo.schule.kataloge.jahrgang;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
class JahrgangRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private JahrgangRepositoryImpl repository;

	// -------------------------------------------------------------------------
	// Konstruktor
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		final var newRepository = new JahrgangRepositoryImpl(conn);

		assertThat(newRepository)
				.isNotNull()
				.isInstanceOf(JahrgangRepositoryImpl.class)
				.isInstanceOf(JahrgangRepository.class);
	}

	// -------------------------------------------------------------------------
	// existsById
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("existsById")
	class ExistsById {

		@Test
		@DisplayName("Gibt true zurück, wenn der Jahrgang vorhanden ist")
		void existsById_found() {
			final long idJahrgang = 500L;

			when(conn.existsBy(
					DTOJahrgang.QUERY_BY_ID,
					DTOJahrgang.class,
					idJahrgang))
					.thenReturn(true);

			final var result = repository.existsById(idJahrgang);

			assertThat(result).isTrue();

			verify(conn, times(1)).existsBy(
					DTOJahrgang.QUERY_BY_ID,
					DTOJahrgang.class,
					idJahrgang);
		}

		@Test
		@DisplayName("Gibt false zurück, wenn der Jahrgang nicht vorhanden ist")
		void existsById_notFound() {
			final long idJahrgang = 999L;

			when(conn.existsBy(
					DTOJahrgang.QUERY_BY_ID,
					DTOJahrgang.class,
					idJahrgang))
					.thenReturn(false);

			final var result = repository.existsById(idJahrgang);

			assertThat(result).isFalse();

			verify(conn, times(1)).existsBy(
					DTOJahrgang.QUERY_BY_ID,
					DTOJahrgang.class,
					idJahrgang);
		}
	}
}
