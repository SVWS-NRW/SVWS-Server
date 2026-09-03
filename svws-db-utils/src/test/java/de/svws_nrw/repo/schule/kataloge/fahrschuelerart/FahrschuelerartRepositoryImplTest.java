package de.svws_nrw.repo.schule.kataloge.fahrschuelerart;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOFahrschuelerart;
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
class FahrschuelerartRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private FahrschuelerartRepositoryImpl repository;

	// -------------------------------------------------------------------------
	// Konstruktor
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		final var newRepository = new FahrschuelerartRepositoryImpl(conn);

		assertThat(newRepository)
				.isNotNull()
				.isInstanceOf(FahrschuelerartRepositoryImpl.class)
				.isInstanceOf(FahrschuelerartRepository.class);
	}

	// -------------------------------------------------------------------------
	// existsById
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("existsById")
	class ExistsById {

		@Test
		@DisplayName("Gibt true zurück, wenn die Fahrschülerart vorhanden ist")
		void existsById_found() {
			final long idFahrschuelerart = 500L;

			when(conn.existsBy(
					DTOFahrschuelerart.QUERY_BY_ID,
					DTOFahrschuelerart.class,
					idFahrschuelerart))
					.thenReturn(true);

			final var result = repository.existsById(idFahrschuelerart);

			assertThat(result).isTrue();

			verify(conn, times(1)).existsBy(
					DTOFahrschuelerart.QUERY_BY_ID,
					DTOFahrschuelerart.class,
					idFahrschuelerart);
		}

		@Test
		@DisplayName("Gibt false zurück, wenn die Fahrschülerart nicht vorhanden ist")
		void existsById_notFound() {
			final long idFahrschuelerart = 999L;

			when(conn.existsBy(
					DTOFahrschuelerart.QUERY_BY_ID,
					DTOFahrschuelerart.class,
					idFahrschuelerart))
					.thenReturn(false);

			final var result = repository.existsById(idFahrschuelerart);

			assertThat(result).isFalse();

			verify(conn, times(1)).existsBy(
					DTOFahrschuelerart.QUERY_BY_ID,
					DTOFahrschuelerart.class,
					idFahrschuelerart);
		}
	}

	// -------------------------------------------------------------------------
	// existsByIds
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("existsByIds")
	class ExistsByIds {

		@Test
		@DisplayName("Gibt leeres Set zurück bei null")
		void existsByIds_null() {
			final var result = repository.existsByIds(null);

			assertThat(result).isEmpty();
		}

		@Test
		@DisplayName("Gibt leeres Set zurück bei leerer Liste")
		void existsByIds_empty() {
			final var result = repository.existsByIds(List.of());

			assertThat(result).isEmpty();
		}

		@Test
		@DisplayName("Gibt nur die gefundenen IDs zurück")
		void existsByIds_partialMatch() {
			final var dto1 = new DTOFahrschuelerart(1L, "gut");
			final var dto2 = new DTOFahrschuelerart(2L, "auch gut");

			when(conn.queryList(DTOFahrschuelerart.QUERY_LIST_PK, DTOFahrschuelerart.class, List.of(1L, 2L, 99L)))
					.thenReturn(List.of(dto1, dto2));

			final var result = repository.existsByIds(List.of(1L, 2L, 99L));

			assertThat(result).containsExactlyInAnyOrder(1L, 2L);
		}

		@Test
		@DisplayName("Gibt leeres Set zurück wenn keine IDs gefunden")
		void existsByIds_noneFound() {
			when(conn.queryList(DTOFahrschuelerart.QUERY_LIST_PK, DTOFahrschuelerart.class, List.of(99L)))
					.thenReturn(List.of());

			final var result = repository.existsByIds(List.of(99L));

			assertThat(result).isEmpty();
		}
	}

}
