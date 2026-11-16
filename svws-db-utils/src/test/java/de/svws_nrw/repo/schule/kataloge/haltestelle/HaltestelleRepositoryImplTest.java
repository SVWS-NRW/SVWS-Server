package de.svws_nrw.repo.schule.kataloge.haltestelle;

import java.util.List;
import java.util.Set;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOHaltestellen;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HaltestelleRepositoryImplTest {

	private static final String QUERY_BEZEICHNUNG_CREATE =
			"SELECT h FROM DTOHaltestellen h WHERE LOWER(h.bezeichnung) = LOWER(?1)";
	private static final String QUERY_BEZEICHNUNG_PATCH =
			"SELECT h FROM DTOHaltestellen h WHERE LOWER(h.bezeichnung) = LOWER(?1) AND h.id != ?2";

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
	// bezeichnungIstBereitsVergeben
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("bezeichnungIstBereitsVergeben")
	class BezeichnungIstBereitsVergeben {

		@Test
		@DisplayName("Liefert true, wenn die Bezeichnung bereits existiert")
		void exists() {
			final var bezeichnung = "Troisdorf";
			when(conn.existsBy(QUERY_BEZEICHNUNG_CREATE, DTOHaltestellen.class, bezeichnung)).thenReturn(true);

			final var result = repository.bezeichnungExists(bezeichnung);

			assertThat(result).isTrue();
			verify(conn, times(1)).existsBy(QUERY_BEZEICHNUNG_CREATE, DTOHaltestellen.class, bezeichnung);
		}

		@Test
		@DisplayName("Liefert false, wenn die Bezeichnung nicht existiert")
		void notExists() {
			final var bezeichnung = "Unbekannt";
			when(conn.existsBy(QUERY_BEZEICHNUNG_CREATE, DTOHaltestellen.class, bezeichnung)).thenReturn(false);

			final var result = repository.bezeichnungExists(bezeichnung);

			assertThat(result).isFalse();
			verify(conn, times(1)).existsBy(QUERY_BEZEICHNUNG_CREATE, DTOHaltestellen.class, bezeichnung);
		}
	}

	// -------------------------------------------------------------------------
	// bezeichnungIstBereitsVergebenExceptId
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("bezeichnungIstBereitsVergebenExceptId")
	class BezeichnungIstBereitsVergebenExceptId {

		@Test
		@DisplayName("Liefert true, wenn die Bezeichnung bei einer anderen Haltestelle existiert")
		void existsInOther() {
			final var bezeichnung = "Troisdorf";
			final var id = 1L;
			when(conn.existsBy(QUERY_BEZEICHNUNG_PATCH, DTOHaltestellen.class, bezeichnung, id)).thenReturn(true);

			final var result = repository.bezeichnungIstBereitsVergebenExceptId(bezeichnung, id);

			assertThat(result).isTrue();
			verify(conn, times(1)).existsBy(QUERY_BEZEICHNUNG_PATCH, DTOHaltestellen.class, bezeichnung, id);
		}

		@Test
		@DisplayName("Liefert false, wenn die Bezeichnung nur bei der eigenen Haltestelle existiert")
		void existsOnlyInSelf() {
			final var bezeichnung = "Troisdorf";
			final var id = 1L;
			when(conn.existsBy(QUERY_BEZEICHNUNG_PATCH, DTOHaltestellen.class, bezeichnung, id)).thenReturn(false);

			final var result = repository.bezeichnungIstBereitsVergebenExceptId(bezeichnung, id);

			assertThat(result).isFalse();
			verify(conn, times(1)).existsBy(QUERY_BEZEICHNUNG_PATCH, DTOHaltestellen.class, bezeichnung, id);
		}
	}

	// -------------------------------------------------------------------------
	// getReferencedIds
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getReferencedIds")
	class GetReferencedIds {

		@Test
		@DisplayName("Gibt leeres Set zurück bei null")
		void getReferencedIds_null() {
			final var result = repository.getReferencedIds(null);

			assertThat(result).isEmpty();
		}

		@Test
		@DisplayName("Gibt leeres Set zurück bei leerer Liste")
		void getReferencedIds_empty() {
			final var result = repository.getReferencedIds(List.of());

			assertThat(result).isEmpty();
		}

		@Test
		@DisplayName("Gibt die referenzierten IDs zurück")
		void getReferencedIds_found() {
			@SuppressWarnings("unchecked")
			final TypedQuery<Long> queryMock = mock(TypedQuery.class);
			when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);
			when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
			when(queryMock.getResultList()).thenReturn(List.of(1L, 2L));

			final var result = repository.getReferencedIds(List.of(1L, 2L, 3L));

			assertThat(result).isEqualTo(Set.of(1L, 2L));
		}

		@Test
		@DisplayName("Gibt leeres Set zurück, wenn keine Referenzen gefunden wurden")
		void getReferencedIds_noneFound() {
			@SuppressWarnings("unchecked")
			final TypedQuery<Long> queryMock = mock(TypedQuery.class);
			when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);
			when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
			when(queryMock.getResultList()).thenReturn(List.of());

			final var result = repository.getReferencedIds(List.of(1L));

			assertThat(result).isEmpty();
		}

		@Test
		@DisplayName("Query referenziert DTOSchueler über Haltestelle_ID")
		void getReferencedIds_queryContainsDTOSchueler() {
			@SuppressWarnings("unchecked")
			final TypedQuery<Long> queryMock = mock(TypedQuery.class);
			when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
			when(queryMock.getResultList()).thenReturn(List.of());
			when(conn.query(anyString(), eq(Long.class))).thenAnswer(invocation -> {
				final String query = invocation.getArgument(0);
				assertThat(query)
						.contains("DTOSchueler")
						.contains("Haltestelle_ID");
				return queryMock;
			});

			repository.getReferencedIds(List.of(1L));

			verify(conn, times(1)).query(anyString(), eq(Long.class));
		}
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
