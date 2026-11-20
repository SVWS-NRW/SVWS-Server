package de.svws_nrw.repo.schule.kataloge.fahrschuelerart;

import java.util.List;
import java.util.Set;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOFahrschuelerart;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
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
	// Hilfskonstanten
	// -------------------------------------------------------------------------

	private static final String QUERY_BEZEICHNUNG_CREATE =
			"SELECT f FROM DTOFahrschuelerart f WHERE LOWER(f.bezeichnung) = LOWER(?1)";
	private static final String QUERY_BEZEICHNUNG_PATCH =
			"SELECT f FROM DTOFahrschuelerart f WHERE LOWER(f.bezeichnung) = LOWER(?1) AND f.id != ?2";
	private static final String QUERY_REFERENCED_IDS =
			"SELECT DISTINCT s.Fahrschueler_ID FROM DTOSchueler s WHERE s.Fahrschueler_ID IN :ids";

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

	// -------------------------------------------------------------------------
	// bezeichnungIsAlreadyUsedCreate
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("bezeichnungIsAlreadyUsedCreate")
	class BezeichnungIsAlreadyUsedCreate {

		@Test
		@DisplayName("Liefert true wenn Bezeichnung bereits existiert")
		void bezeichnungIsAlreadyUsedCreate_exists() {
			final var bezeichnung = "Bus";
			when(conn.existsBy(QUERY_BEZEICHNUNG_CREATE, DTOFahrschuelerart.class, bezeichnung)).thenReturn(true);

			final var result = repository.bezeichnungIsAlreadyUsedCreate(bezeichnung);

			assertThat(result).isTrue();
			verify(conn, times(1)).existsBy(QUERY_BEZEICHNUNG_CREATE, DTOFahrschuelerart.class, bezeichnung);
		}

		@Test
		@DisplayName("Liefert false wenn Bezeichnung nicht existiert")
		void bezeichnungIsAlreadyUsedCreate_notExists() {
			final var bezeichnung = "Unbekannt";
			when(conn.existsBy(QUERY_BEZEICHNUNG_CREATE, DTOFahrschuelerart.class, bezeichnung)).thenReturn(false);

			final var result = repository.bezeichnungIsAlreadyUsedCreate(bezeichnung);

			assertThat(result).isFalse();
			verify(conn, times(1)).existsBy(QUERY_BEZEICHNUNG_CREATE, DTOFahrschuelerart.class, bezeichnung);
		}
	}

	// -------------------------------------------------------------------------
	// bezeichnungIsAlreadyUsedPatch
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("bezeichnungIsAlreadyUsedPatch")
	class BezeichnungIsAlreadyUsedPatch {

		@Test
		@DisplayName("Liefert true wenn Bezeichnung bei einem anderen Eintrag existiert")
		void bezeichnungIsAlreadyUsedPatch_existsInOther() {
			final var bezeichnung = "Bus";
			final var id = 1L;
			when(conn.existsBy(QUERY_BEZEICHNUNG_PATCH, DTOFahrschuelerart.class, bezeichnung, id)).thenReturn(true);

			final var result = repository.bezeichnungIsAlreadyUsedPatch(bezeichnung, id);

			assertThat(result).isTrue();
			verify(conn, times(1)).existsBy(QUERY_BEZEICHNUNG_PATCH, DTOFahrschuelerart.class, bezeichnung, id);
		}

		@Test
		@DisplayName("Liefert false wenn Bezeichnung nur beim eigenen Eintrag existiert")
		void bezeichnungIsAlreadyUsedPatch_existsOnlyInSelf() {
			final var bezeichnung = "Bus";
			final var id = 1L;
			when(conn.existsBy(QUERY_BEZEICHNUNG_PATCH, DTOFahrschuelerart.class, bezeichnung, id)).thenReturn(false);

			final var result = repository.bezeichnungIsAlreadyUsedPatch(bezeichnung, id);

			assertThat(result).isFalse();
			verify(conn, times(1)).existsBy(QUERY_BEZEICHNUNG_PATCH, DTOFahrschuelerart.class, bezeichnung, id);
		}
	}

	// -------------------------------------------------------------------------
	// getReferencedIds
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getReferencedIds")
	class GetReferencedIds {

		@Test
		@DisplayName("Gibt leeres Set zurück bei null und führt keine Abfrage aus")
		void getReferencedIds_null() {
			final var result = repository.getReferencedIds(null);

			assertThat(result).isEmpty();
			verify(conn, never()).query(anyString(), any());
		}

		@Test
		@DisplayName("Gibt leeres Set zurück bei leerer Liste und führt keine Abfrage aus")
		void getReferencedIds_empty() {
			final var result = repository.getReferencedIds(List.of());

			assertThat(result).isEmpty();
			verify(conn, never()).query(anyString(), any());
		}

		@Test
		@DisplayName("Gibt referenzierte IDs zurück")
		void getReferencedIds_found() {
			@SuppressWarnings("unchecked")
			final TypedQuery<Long> queryMock = mock(TypedQuery.class);
			when(conn.query(QUERY_REFERENCED_IDS, Long.class)).thenReturn(queryMock);
			when(queryMock.setParameter("ids", List.of(1L, 2L, 3L))).thenReturn(queryMock);
			when(queryMock.getResultList()).thenReturn(List.of(1L, 2L));

			final var result = repository.getReferencedIds(List.of(1L, 2L, 3L));

			assertThat(result).isEqualTo(Set.of(1L, 2L));
		}

		@Test
		@DisplayName("Gibt leeres Set zurück wenn keine Referenzen gefunden")
		void getReferencedIds_noneFound() {
			@SuppressWarnings("unchecked")
			final TypedQuery<Long> queryMock = mock(TypedQuery.class);
			when(conn.query(QUERY_REFERENCED_IDS, Long.class)).thenReturn(queryMock);
			when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
			when(queryMock.getResultList()).thenReturn(List.of());

			final var result = repository.getReferencedIds(List.of(1L));

			assertThat(result).isEmpty();
		}

		@Test
		@DisplayName("Query referenziert DTOSchueler über Fahrschueler_ID")
		void getReferencedIds_queryContainsDTOSchueler() {
			@SuppressWarnings("unchecked")
			final TypedQuery<Long> queryMock = mock(TypedQuery.class);
			when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
			when(queryMock.getResultList()).thenReturn(List.of());

			when(conn.query(anyString(), eq(Long.class))).thenAnswer(invocation -> {
				final String query = invocation.getArgument(0);
				assertThat(query)
						.contains("DTOSchueler")
						.contains("Fahrschueler_ID");
				return queryMock;
			});

			repository.getReferencedIds(List.of(1L));

			verify(conn, times(1)).query(anyString(), eq(Long.class));
		}
	}

}
