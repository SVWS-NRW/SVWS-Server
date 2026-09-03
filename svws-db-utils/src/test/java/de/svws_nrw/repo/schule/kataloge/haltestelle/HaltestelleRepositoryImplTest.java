package de.svws_nrw.repo.schule.kataloge.haltestelle;

import java.util.List;

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
			final var dto1 = new DTOHaltestellen(1L, "Troisdorf");
			final var dto2 = new DTOHaltestellen(2L, "Bonn");

			when(conn.queryList(DTOHaltestellen.QUERY_LIST_PK, DTOHaltestellen.class, List.of(1L, 2L, 99L)))
					.thenReturn(List.of(dto1, dto2));

			final var result = repository.existsByIds(List.of(1L, 2L, 99L));

			assertThat(result).containsExactlyInAnyOrder(1L, 2L);
		}

		@Test
		@DisplayName("Gibt leeres Set zurück wenn keine IDs gefunden")
		void existsByIds_noneFound() {
			when(conn.queryList(DTOHaltestellen.QUERY_LIST_PK, DTOHaltestellen.class, List.of(99L)))
					.thenReturn(List.of());

			final var result = repository.existsByIds(List.of(99L));

			assertThat(result).isEmpty();
		}
	}

}
