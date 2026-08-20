package de.svws_nrw.repo.lehrer.lehrbefaehigung;

import java.util.List;
import java.util.Map;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramtBefaehigung;
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
class LehrerLehramtLehrbefaehigungRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private LehrerLehramtLehrbefaehigungRepositoryImpl repository;

	// -------------------------------------------------------------------------
	// Konstruktor
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		final var newRepository = new LehrerLehramtLehrbefaehigungRepositoryImpl(conn);

		assertThat(newRepository)
				.isNotNull()
				.isInstanceOf(LehrerLehramtLehrbefaehigungRepositoryImpl.class)
				.isInstanceOf(LehrerLehramtLehrbefaehigungRepository.class);
	}

	// -------------------------------------------------------------------------
	// getLehrerLehrbefaehigungByIdLehramt
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getLehrerLehrbefaehigungByIdLehramt")
	class GetLehrerLehrbefaehigungByIdLehramt {

		@Test
		@DisplayName("Gibt leere Map bei null zurück")
		void getLehrerLehrbefaehigungByIdLehramt_null() {
			final var result = repository.getLehrerLehrbefaehigungByIdLehramt(null);

			assertThat(result).isEmpty();
			verifyNoInteractions(conn);
		}

		@Test
		@DisplayName("Gibt leere Map bei leerer Collection zurück")
		void getLehrerLehrbefaehigungByIdLehramt_empty() {
			final var result = repository.getLehrerLehrbefaehigungByIdLehramt(List.of());

			assertThat(result).isEmpty();
			verifyNoInteractions(conn);
		}

		@Test
		@DisplayName("Gruppiert Lehrbefaehigungen nach Lehramts-ID")
		void getLehrerLehrbefaehigungByIdLehramt_groupedByLehramtId() {
			final var idsLehraemter = List.of(500L, 600L, 700L);

			final var lehrbefaehigung1 =
					new DTOLehrerPersonaldatenLehramtBefaehigung(1L, 500L, 3L);
			final var lehrbefaehigung2 =
					new DTOLehrerPersonaldatenLehramtBefaehigung(2L, 500L, 4L);
			final var lehrbefaehigung3 =
					new DTOLehrerPersonaldatenLehramtBefaehigung(3L, 600L, 5L);

			when(conn.queryList(
					DTOLehrerPersonaldatenLehramtBefaehigung.QUERY_LIST_BY_IDLEHRAMT,
					DTOLehrerPersonaldatenLehramtBefaehigung.class,
					idsLehraemter))
					.thenReturn(List.of(lehrbefaehigung1, lehrbefaehigung2, lehrbefaehigung3));

			final Map<Long, List<DTOLehrerPersonaldatenLehramtBefaehigung>> result =
					repository.getLehrerLehrbefaehigungByIdLehramt(idsLehraemter);

			assertThat(result)
					.containsOnlyKeys(500L, 600L)
					.containsEntry(500L, List.of(lehrbefaehigung1, lehrbefaehigung2))
					.containsEntry(600L, List.of(lehrbefaehigung3));

			verify(conn, times(1)).queryList(
					DTOLehrerPersonaldatenLehramtBefaehigung.QUERY_LIST_BY_IDLEHRAMT,
					DTOLehrerPersonaldatenLehramtBefaehigung.class,
					idsLehraemter);
		}

		@Test
		@DisplayName("Gibt leere Map zurück, wenn keine Lehrbefaehigungen gefunden werden")
		void getLehrerLehrbefaehigungByIdLehramt_noResults() {
			final var idsLehraemter = List.of(500L, 600L);

			when(conn.queryList(
					DTOLehrerPersonaldatenLehramtBefaehigung.QUERY_LIST_BY_IDLEHRAMT,
					DTOLehrerPersonaldatenLehramtBefaehigung.class,
					idsLehraemter))
					.thenReturn(List.of());

			final var result = repository.getLehrerLehrbefaehigungByIdLehramt(idsLehraemter);

			assertThat(result).isEmpty();

			verify(conn, times(1)).queryList(
					DTOLehrerPersonaldatenLehramtBefaehigung.QUERY_LIST_BY_IDLEHRAMT,
					DTOLehrerPersonaldatenLehramtBefaehigung.class,
					idsLehraemter);
		}
	}

	// -------------------------------------------------------------------------
	// getByLehramtId
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getByLehramtId")
	class GetByLehramtId {

		@Test
		@DisplayName("Gibt Lehrbefaehigungen für ein Lehramt zurück")
		void getByLehramtId_found() {
			final long idLehramt = 500L;

			final var lehrbefaehigung1 =
					new DTOLehrerPersonaldatenLehramtBefaehigung(1L, idLehramt, 3L);
			final var lehrbefaehigung2 =
					new DTOLehrerPersonaldatenLehramtBefaehigung(2L, idLehramt, 4L);

			when(conn.queryList(
					DTOLehrerPersonaldatenLehramtBefaehigung.QUERY_BY_IDLEHRAMT,
					DTOLehrerPersonaldatenLehramtBefaehigung.class,
					idLehramt))
					.thenReturn(List.of(lehrbefaehigung1, lehrbefaehigung2));

			final var result = repository.getByIdLehramt(idLehramt);

			assertThat(result)
					.containsExactly(lehrbefaehigung1, lehrbefaehigung2);

			verify(conn, times(1)).queryList(
					DTOLehrerPersonaldatenLehramtBefaehigung.QUERY_BY_IDLEHRAMT,
					DTOLehrerPersonaldatenLehramtBefaehigung.class,
					idLehramt);
		}

		@Test
		@DisplayName("Gibt leere Liste zurück, wenn keine Lehrbefaehigungen gefunden werden")
		void getByLehramtId_noResults() {
			final long idLehramt = 700L;

			when(conn.queryList(
					DTOLehrerPersonaldatenLehramtBefaehigung.QUERY_BY_IDLEHRAMT,
					DTOLehrerPersonaldatenLehramtBefaehigung.class,
					idLehramt))
					.thenReturn(List.of());

			final var result = repository.getByIdLehramt(idLehramt);

			assertThat(result).isEmpty();

			verify(conn, times(1)).queryList(
					DTOLehrerPersonaldatenLehramtBefaehigung.QUERY_BY_IDLEHRAMT,
					DTOLehrerPersonaldatenLehramtBefaehigung.class,
					idLehramt);
		}
	}

	// -------------------------------------------------------------------------
	// create
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("create | Setzt die nächste ID und persistiert das DTO")
	void create_success() {
		final var neueLehrbefaehigung =
				new DTOLehrerPersonaldatenLehramtBefaehigung(1L, 500L, 3L);
		final long neueId = 999L;

		when(conn.transactionGetNextID(
				DTOLehrerPersonaldatenLehramtBefaehigung.class))
				.thenReturn(neueId);
		when(conn.transactionPersist(neueLehrbefaehigung))
				.thenReturn(true);

		final var result = repository.create(neueLehrbefaehigung);

		assertThat(result)
				.isSameAs(neueLehrbefaehigung)
				.extracting(dto -> dto.id)
				.isEqualTo(neueId);

		verify(conn, times(1)).transactionGetNextID(
				DTOLehrerPersonaldatenLehramtBefaehigung.class);
		verify(conn, times(1)).transactionPersist(neueLehrbefaehigung);
	}
}
