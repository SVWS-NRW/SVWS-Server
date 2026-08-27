package de.svws_nrw.repo.schule.kataloge.haltestelle;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOHaltestellen;
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
class HaltestelleRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private HaltestelleRepositoryImpl repository;

	// -------------------------------------------------------------------------
	// Konstruktor
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		final var newRepository = new HaltestelleRepositoryImpl(conn);

		assertThat(newRepository)
				.isNotNull()
				.isInstanceOf(HaltestelleRepositoryImpl.class)
				.isInstanceOf(HaltestelleRepository.class);
	}

	// -------------------------------------------------------------------------
	// existsById
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("existsById")
	class ExistsById {

		@Test
		@DisplayName("Gibt true zurück, wenn die Haltestelle vorhanden ist")
		void existsById_found() {
			final long idHaltestelle = 500L;

			when(conn.existsBy(
					DTOHaltestellen.QUERY_BY_ID,
					DTOHaltestellen.class,
					idHaltestelle))
					.thenReturn(true);

			final var result = repository.existsById(idHaltestelle);

			assertThat(result).isTrue();

			verify(conn, times(1)).existsBy(
					DTOHaltestellen.QUERY_BY_ID,
					DTOHaltestellen.class,
					idHaltestelle);
		}

		@Test
		@DisplayName("Gibt false zurück, wenn die Haltestelle nicht vorhanden ist")
		void existsById_notFound() {
			final long idHaltestelle = 999L;

			when(conn.existsBy(
					DTOHaltestellen.QUERY_BY_ID,
					DTOHaltestellen.class,
					idHaltestelle))
					.thenReturn(false);

			final var result = repository.existsById(idHaltestelle);

			assertThat(result).isFalse();

			verify(conn, times(1)).existsBy(
					DTOHaltestellen.QUERY_BY_ID,
					DTOHaltestellen.class,
					idHaltestelle);
		}
	}

}
