package de.svws_nrw.repo.lehrer.lehramt;

import java.util.List;
import java.util.Map;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
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
	// getMapByLehrerID
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getMapByLehrerID")
	class GetMapByLehrerId {

		@Test
		@DisplayName("Gibt leere Map bei null zurück")
		void getMapByLehrerId_null() {
			final var result = repository.getMapByLehrerID(null);

			assertThat(result).isEmpty();
			verifyNoInteractions(conn);
		}

		@Test
		@DisplayName("Gibt leere Map bei leerer Collection zurück")
		void getMapByLehrerId_empty() {
			final var result = repository.getMapByLehrerID(List.of());

			assertThat(result).isEmpty();
			verifyNoInteractions(conn);
		}

		@Test
		@DisplayName("Gruppiert Lehrämter nach Lehrer-ID")
		void getMapByLehrerId_groupedByLehrerId() {
			final var idsLehrer = List.of(500L, 600L, 700L);

			final var lehramt1 =
					new DTOLehrerPersonaldatenLehramt(1L, 500L);
			final var lehramt2 =
					new DTOLehrerPersonaldatenLehramt(2L, 500L);
			final var lehramt3 =
					new DTOLehrerPersonaldatenLehramt(3L, 600L);

			when(conn.queryList(
					DTOLehrerPersonaldatenLehramt.QUERY_LIST_BY_LEHRER_ID,
					DTOLehrerPersonaldatenLehramt.class,
					idsLehrer))
					.thenReturn(List.of(lehramt1, lehramt2, lehramt3));

			final Map<Long, List<DTOLehrerPersonaldatenLehramt>> result =
					repository.getMapByLehrerID(idsLehrer);

			assertThat(result)
					.containsOnlyKeys(500L, 600L, 700L)
					.containsEntry(500L, List.of(lehramt1, lehramt2))
					.containsEntry(600L, List.of(lehramt3))
					.containsEntry(700L, List.of());

			verify(conn, times(1)).queryList(
					DTOLehrerPersonaldatenLehramt.QUERY_LIST_BY_LEHRER_ID,
					DTOLehrerPersonaldatenLehramt.class,
					idsLehrer);
		}

		@Test
		@DisplayName("Erzeugt leere Einträge für Lehrer ohne Lehramt")
		void getMapByLehrerId_createsEmptyEntries() {
			final var idsLehrer = List.of(500L, 600L);

			final var lehramt =
					new DTOLehrerPersonaldatenLehramt(1L, 500L);

			when(conn.queryList(
					DTOLehrerPersonaldatenLehramt.QUERY_LIST_BY_LEHRER_ID,
					DTOLehrerPersonaldatenLehramt.class,
					idsLehrer))
					.thenReturn(List.of(lehramt));

			final var result = repository.getMapByLehrerID(idsLehrer);

			assertThat(result)
					.containsOnlyKeys(500L, 600L)
					.containsEntry(500L, List.of(lehramt))
					.containsEntry(600L, List.of());
		}

		@Test
		@DisplayName("Gibt für eine leere Datenbankabfrage leere Einträge für alle Lehrer zurück")
		void getMapByLehrerId_noResults() {
			final var idsLehrer = List.of(500L, 600L);

			when(conn.queryList(
					DTOLehrerPersonaldatenLehramt.QUERY_LIST_BY_LEHRER_ID,
					DTOLehrerPersonaldatenLehramt.class,
					idsLehrer))
					.thenReturn(List.of());

			final var result = repository.getMapByLehrerID(idsLehrer);

			assertThat(result)
					.containsOnlyKeys(500L, 600L)
					.containsEntry(500L, List.of());

			verify(conn, times(1)).queryList(
					DTOLehrerPersonaldatenLehramt.QUERY_LIST_BY_LEHRER_ID,
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
		@DisplayName("Gibt true zurück, wenn das Lehramt vorhanden ist")
		void existsById_found() {
			final long idLehramt = 500L;

			when(conn.existsBy(
					DTOLehrer.QUERY_BY_ID,
					DTOLehrer.class,
					idLehramt))
					.thenReturn(true);

			final var result = repository.existsById(idLehramt);

			assertThat(result).isTrue();

			verify(conn, times(1)).existsBy(
					DTOLehrer.QUERY_BY_ID,
					DTOLehrer.class,
					idLehramt);
		}

		@Test
		@DisplayName("Gibt false zurück, wenn das Lehramt nicht vorhanden ist")
		void existsById_notFound() {
			final long idLehramt = 999L;

			when(conn.existsBy(
					DTOLehrer.QUERY_BY_ID,
					DTOLehrer.class,
					idLehramt))
					.thenReturn(false);

			final var result = repository.existsById(idLehramt);

			assertThat(result).isFalse();

			verify(conn, times(1)).existsBy(
					DTOLehrer.QUERY_BY_ID,
					DTOLehrer.class,
					idLehramt);
		}
	}

	// -------------------------------------------------------------------------
	// create
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("create | Setzt die nächste ID und persistiert das DTO")
	void create_success() {
		final var neuesLehramt =
				new DTOLehrerPersonaldatenLehramt(1L, 500L);
		final long neueId = 999L;

		when(conn.transactionGetNextID(
				DTOLehrerPersonaldatenLehramt.class))
				.thenReturn(neueId);
		when(conn.transactionPersist(neuesLehramt))
				.thenReturn(true);

		final var result = repository.create(neuesLehramt);

		assertThat(result)
				.isSameAs(neuesLehramt);

		assertThat(result.ID)
				.isEqualTo(neueId);

		verify(conn, times(1)).transactionGetNextID(
				DTOLehrerPersonaldatenLehramt.class);
		verify(conn, times(1)).transactionPersist(neuesLehramt);
	}
}
