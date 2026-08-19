package de.svws_nrw.repo.lehrer.fachrichtung;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramtFachrichtung;

@ExtendWith(MockitoExtension.class)
class LehrerLehramtFachrichtungRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private LehrerLehramtFachrichtungRepositoryImpl repository;

	// -------------------------------------------------------------------------
	// Konstruktor
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		final var newRepository = new LehrerLehramtFachrichtungRepositoryImpl(conn);

		assertThat(newRepository)
				.isNotNull()
				.isInstanceOf(LehrerLehramtFachrichtungRepositoryImpl.class)
				.isInstanceOf(LehrerLehramtFachrichtungRepository.class);
	}

	// -------------------------------------------------------------------------
	// getLehrerFachrichtungByIdLehramt
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getLehrerFachrichtungByIdLehramt")
	class GetLehrerFachrichtungByIdLehramt {

		@Test
		@DisplayName("Gibt leere Map bei null zurück")
		void getLehrerFachrichtungByIdLehramt_null() {
			final var result = repository.getLehrerFachrichtungenByIdLehramt(null);

			assertThat(result).isEmpty();
			verifyNoInteractions(conn);
		}

		@Test
		@DisplayName("Gibt leere Map bei leerer Collection zurück")
		void getLehrerFachrichtungByIdLehramt_empty() {
			final var result = repository.getLehrerFachrichtungenByIdLehramt(List.of());

			assertThat(result).isEmpty();
			verifyNoInteractions(conn);
		}

		@Test
		@DisplayName("Gruppiert Fachrichtungen nach Lehramts-ID")
		void getLehrerFachrichtungByIdLehramt_groupedByLehramtId() {
			final var idsLehraemter = List.of(500L, 600L, 700L);

			final var fachrichtung1 =
					new DTOLehrerPersonaldatenLehramtFachrichtung(1L, 500L, 3L);
			final var fachrichtung2 =
					new DTOLehrerPersonaldatenLehramtFachrichtung(2L, 500L, 4L);
			final var fachrichtung3 =
					new DTOLehrerPersonaldatenLehramtFachrichtung(3L, 600L, 5L);

			when(conn.queryList(
					DTOLehrerPersonaldatenLehramtFachrichtung.QUERY_LIST_BY_IDLEHRAMT,
					DTOLehrerPersonaldatenLehramtFachrichtung.class,
					idsLehraemter))
					.thenReturn(List.of(fachrichtung1, fachrichtung2, fachrichtung3));

			final Map<Long, List<DTOLehrerPersonaldatenLehramtFachrichtung>> result =
					repository.getLehrerFachrichtungenByIdLehramt(idsLehraemter);

			assertThat(result)
					.containsOnlyKeys(500L, 600L)
					.containsEntry(500L, List.of(fachrichtung1, fachrichtung2))
					.containsEntry(600L, List.of(fachrichtung3));

			verify(conn, times(1)).queryList(
					DTOLehrerPersonaldatenLehramtFachrichtung.QUERY_LIST_BY_IDLEHRAMT,
					DTOLehrerPersonaldatenLehramtFachrichtung.class,
					idsLehraemter);
		}

		@Test
		@DisplayName("Gibt leere Map zurück, wenn keine Fachrichtungen gefunden werden")
		void getLehrerFachrichtungByIdLehramt_noResults() {
			final var idsLehraemter = List.of(500L, 600L);

			when(conn.queryList(
					DTOLehrerPersonaldatenLehramtFachrichtung.QUERY_LIST_BY_IDLEHRAMT,
					DTOLehrerPersonaldatenLehramtFachrichtung.class,
					idsLehraemter))
					.thenReturn(List.of());

			final var result = repository.getLehrerFachrichtungenByIdLehramt(idsLehraemter);

			assertThat(result).isEmpty();

			verify(conn, times(1)).queryList(
					DTOLehrerPersonaldatenLehramtFachrichtung.QUERY_LIST_BY_IDLEHRAMT,
					DTOLehrerPersonaldatenLehramtFachrichtung.class,
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
		@DisplayName("Gibt Fachrichtungen für ein Lehramt zurück")
		void getByLehramtId_found() {
			final long idLehramt = 500L;

			final var fachrichtung1 =
					new DTOLehrerPersonaldatenLehramtFachrichtung(1L, idLehramt, 3L);
			final var fachrichtung2 =
					new DTOLehrerPersonaldatenLehramtFachrichtung(2L, idLehramt, 4L);

			when(conn.queryList(
					DTOLehrerPersonaldatenLehramtFachrichtung.QUERY_BY_IDLEHRAMT,
					DTOLehrerPersonaldatenLehramtFachrichtung.class,
					idLehramt))
					.thenReturn(List.of(fachrichtung1, fachrichtung2));

			final var result = repository.getByLehramtId(idLehramt);

			assertThat(result)
					.containsExactly(fachrichtung1, fachrichtung2);

			verify(conn, times(1)).queryList(
					DTOLehrerPersonaldatenLehramtFachrichtung.QUERY_BY_IDLEHRAMT,
					DTOLehrerPersonaldatenLehramtFachrichtung.class,
					idLehramt);
		}

		@Test
		@DisplayName("Gibt leere Liste zurück, wenn keine Fachrichtungen gefunden werden")
		void getByLehramtId_noResults() {
			final long idLehramt = 700L;

			when(conn.queryList(
					DTOLehrerPersonaldatenLehramtFachrichtung.QUERY_BY_IDLEHRAMT,
					DTOLehrerPersonaldatenLehramtFachrichtung.class,
					idLehramt))
					.thenReturn(List.of());

			final var result = repository.getByLehramtId(idLehramt);

			assertThat(result).isEmpty();

			verify(conn, times(1)).queryList(
					DTOLehrerPersonaldatenLehramtFachrichtung.QUERY_BY_IDLEHRAMT,
					DTOLehrerPersonaldatenLehramtFachrichtung.class,
					idLehramt);
		}
	}

	// -------------------------------------------------------------------------
	// create
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("create | Setzt die nächste ID und persistiert das DTO")
	void create_success() {
		final var neueFachrichtung =
				new DTOLehrerPersonaldatenLehramtFachrichtung(1L, 500L, 3L);
		final long neueId = 999L;

		when(conn.transactionGetNextID(
				DTOLehrerPersonaldatenLehramtFachrichtung.class))
				.thenReturn(neueId);
		when(conn.transactionPersist(neueFachrichtung))
				.thenReturn(true);

		final var result = repository.create(neueFachrichtung);

		assertThat(result)
				.isSameAs(neueFachrichtung)
				.extracting(dto -> dto.id)
				.isEqualTo(neueId);

		verify(conn, times(1)).transactionGetNextID(
				DTOLehrerPersonaldatenLehramtFachrichtung.class);
		verify(conn, times(1)).transactionPersist(neueFachrichtung);
	}
}
