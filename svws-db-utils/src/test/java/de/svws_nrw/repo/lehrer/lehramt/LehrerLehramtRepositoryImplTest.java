package de.svws_nrw.repo.lehrer.lehramt;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramt;
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
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LehrerLehramtRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private LehrerLehramtRepositoryImpl repository;

	// -------------------------------------------------------------------------
	// Konstruktor
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		final var newRepository = new LehrerLehramtRepositoryImpl(conn);

		assertThat(newRepository)
				.isNotNull()
				.isInstanceOf(LehrerLehramtRepositoryImpl.class)
				.isInstanceOf(LehrerLehramtRepository.class);
	}

	// -------------------------------------------------------------------------
	// findByIdsLehrer
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("findByIdsLehrer")
	class FindByIdsLehrer {

		@Test
		@DisplayName("Gibt leere Liste bei null zurück")
		void findByIdsLehrer_null() {
			final var result = repository.findByIdsLehrer(null);

			assertThat(result).isEmpty();
			verifyNoInteractions(conn);
		}

		@Test
		@DisplayName("Gibt leere Liste bei leerer Liste zurück")
		void findByIdsLehrer_empty() {
			final var result = repository.findByIdsLehrer(List.of());

			assertThat(result).isEmpty();
			verifyNoInteractions(conn);
		}

		@Test
		@DisplayName("Gibt gefundene Lehrämter zurück")
		void findByIdsLehrer_found() {
			final var idsLehrer = List.of(4711L, 4712L);

			final var lehramt1 = new DTOLehrerPersonaldatenLehramt(1L, 4711L);
			final var lehramt2 = new DTOLehrerPersonaldatenLehramt(2L, 4712L);

			when(conn.queryList(
					DTOLehrerPersonaldatenLehramt.QUERY_LIST_BY_IDLEHRER,
					DTOLehrerPersonaldatenLehramt.class,
					idsLehrer))
					.thenReturn(List.of(lehramt1, lehramt2));

			final var result = repository.findByIdsLehrer(idsLehrer);

			assertThat(result).containsExactly(lehramt1, lehramt2);

			verify(conn, times(1)).queryList(
					DTOLehrerPersonaldatenLehramt.QUERY_LIST_BY_IDLEHRER,
					DTOLehrerPersonaldatenLehramt.class,
					idsLehrer);
		}

		@Test
		@DisplayName("Gibt leere Liste zurück, wenn keine Lehrämter gefunden werden")
		void findByIdsLehrer_noResults() {
			final var idsLehrer = List.of(4711L, 4712L);

			when(conn.queryList(
					DTOLehrerPersonaldatenLehramt.QUERY_LIST_BY_IDLEHRER,
					DTOLehrerPersonaldatenLehramt.class,
					idsLehrer))
					.thenReturn(List.of());

			final var result = repository.findByIdsLehrer(idsLehrer);

			assertThat(result).isEmpty();

			verify(conn, times(1)).queryList(
					DTOLehrerPersonaldatenLehramt.QUERY_LIST_BY_IDLEHRER,
					DTOLehrerPersonaldatenLehramt.class,
					idsLehrer);
		}
	}

	// -------------------------------------------------------------------------
	// existsById
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("existsById")
	class ExistsById {

		@Test
		@DisplayName("Gibt true zurück, wenn ein Lehramt mit der ID existiert")
		void existsById_true() {
			final long idLehramt = 4711L;

			when(conn.existsBy(
					DTOLehrerPersonaldatenLehramt.QUERY_BY_ID,
					DTOLehrerPersonaldatenLehramt.class,
					idLehramt))
					.thenReturn(true);

			final var result = repository.existsById(idLehramt);

			assertThat(result).isTrue();

			verify(conn, times(1)).existsBy(
					DTOLehrerPersonaldatenLehramt.QUERY_BY_ID,
					DTOLehrerPersonaldatenLehramt.class,
					idLehramt);
		}

		@Test
		@DisplayName("Gibt false zurück, wenn kein Lehramt mit der ID existiert")
		void existsById_false() {
			final long idLehramt = 9999L;

			when(conn.existsBy(
					DTOLehrerPersonaldatenLehramt.QUERY_BY_ID,
					DTOLehrerPersonaldatenLehramt.class,
					idLehramt))
					.thenReturn(false);

			final var result = repository.existsById(idLehramt);

			assertThat(result).isFalse();

			verify(conn, times(1)).existsBy(
					DTOLehrerPersonaldatenLehramt.QUERY_BY_ID,
					DTOLehrerPersonaldatenLehramt.class,
					idLehramt);
		}
	}
}
