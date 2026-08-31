package de.svws_nrw.repo.schule.kataloge.ankreuzkompetenz;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOAnkreuzfloskeln;
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
class AnkreuzkompetenzRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private AnkreuzkompetenzRepositoryImpl repository;

	// -------------------------------------------------------------------------
	// Konstruktor
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		final var newRepository = new AnkreuzkompetenzRepositoryImpl(conn);

		assertThat(newRepository)
				.isNotNull()
				.isInstanceOf(AnkreuzkompetenzRepositoryImpl.class)
				.isInstanceOf(AnkreuzkompetenzRepository.class);
	}

	// -------------------------------------------------------------------------
	// existsById
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("existsById")
	class ExistsById {

		@Test
		@DisplayName("Gibt true zurück, wenn die Ankreuzkompetenz vorhanden ist")
		void existsById_found() {
			final long idAnkreuzkompetenz = 500L;

			when(conn.existsBy(
					DTOAnkreuzfloskeln.QUERY_BY_ID,
					DTOAnkreuzfloskeln.class,
					idAnkreuzkompetenz))
					.thenReturn(true);

			final var result = repository.existsById(idAnkreuzkompetenz);

			assertThat(result).isTrue();

			verify(conn, times(1)).existsBy(
					DTOAnkreuzfloskeln.QUERY_BY_ID,
					DTOAnkreuzfloskeln.class,
					idAnkreuzkompetenz);
		}

		@Test
		@DisplayName("Gibt false zurück, wenn die Ankreuzkompetenz nicht vorhanden ist")
		void existsById_notFound() {
			final long idAnkreuzkompetenz = 999L;

			when(conn.existsBy(
					DTOAnkreuzfloskeln.QUERY_BY_ID,
					DTOAnkreuzfloskeln.class,
					idAnkreuzkompetenz))
					.thenReturn(false);

			final var result = repository.existsById(idAnkreuzkompetenz);

			assertThat(result).isFalse();

			verify(conn, times(1)).existsBy(
					DTOAnkreuzfloskeln.QUERY_BY_ID,
					DTOAnkreuzfloskeln.class,
					idAnkreuzkompetenz);
		}
	}
}
