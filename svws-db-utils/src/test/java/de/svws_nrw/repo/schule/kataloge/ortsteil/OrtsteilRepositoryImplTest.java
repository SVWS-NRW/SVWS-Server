package de.svws_nrw.repo.schule.kataloge.ortsteil;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrtsteil;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrtsteilRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private OrtsteilRepositoryImpl repository;

	// -------------------------------------------------------------------------
	// Konstruktor
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		final var newRepository = new OrtsteilRepositoryImpl(conn);

		assertThat(newRepository)
				.isNotNull()
				.isInstanceOf(OrtsteilRepositoryImpl.class)
				.isInstanceOf(OrtsteilRepository.class);
	}

	// -------------------------------------------------------------------------
	// ortsnameIsUniqueForIdOrt (Create)
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("ortsnameIsUniqueForIdOrt (Create)")
	class OrtsnameIsUniqueForIdOrtCreate {

		private static final String EXPECTED_QUERY =
				"SELECT o FROM DTOOrtsteil o WHERE LOWER(o.ortsteil) = LOWER(?1) AND o.idOrt = ?2";

		@Test
		@DisplayName("Gibt true zurück wenn Ortsteil für idOrt noch nicht vergeben")
		void ortsnameIsUniqueForIdOrt_create_unique() {
			when(conn.existsBy(EXPECTED_QUERY, DTOOrtsteil.class, "Mitte", 42L)).thenReturn(false);

			final var result = repository.ortsteilnameIsUniqueForIdOrtCreate("Mitte", 42L);

			assertThat(result).isTrue();
			verify(conn, times(1)).existsBy(EXPECTED_QUERY, DTOOrtsteil.class, "Mitte", 42L);
		}

		@Test
		@DisplayName("Gibt false zurück wenn Ortsteil für idOrt bereits vergeben")
		void ortsnameIsUniqueForIdOrt_create_notUnique() {
			when(conn.existsBy(EXPECTED_QUERY, DTOOrtsteil.class, "Mitte", 42L)).thenReturn(true);

			final var result = repository.ortsteilnameIsUniqueForIdOrtCreate("Mitte", 42L);

			assertThat(result).isFalse();
			verify(conn, times(1)).existsBy(EXPECTED_QUERY, DTOOrtsteil.class, "Mitte", 42L);
		}
	}

	// -------------------------------------------------------------------------
	// ortsnameIsUniqueForIdOrt (Patch)
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("ortsnameIsUniqueForIdOrt (Patch)")
	class OrtsnameIsUniqueForIdOrtPatch {

		private static final String EXPECTED_QUERY =
				"SELECT o FROM DTOOrtsteil o WHERE LOWER(o.ortsteil) = LOWER(?1) AND o.idOrt = ?2 AND o.id != ?3";

		@Test
		@DisplayName("Gibt true zurück wenn kein anderer Ortsteil den Namen für idOrt verwendet")
		void ortsnameIsUniqueForIdOrt_patch_unique() {
			when(conn.existsBy(EXPECTED_QUERY, DTOOrtsteil.class, "Mitte", 42L, 7L)).thenReturn(false);

			final var result = repository.ortsteilnameIsUniqueForIdOrtPatch("Mitte", 42L, 7L);

			assertThat(result).isTrue();
			verify(conn, times(1)).existsBy(EXPECTED_QUERY, DTOOrtsteil.class, "Mitte", 42L, 7L);
		}

		@Test
		@DisplayName("Gibt false zurück wenn ein anderer Ortsteil den Namen für idOrt bereits verwendet")
		void ortsnameIsUniqueForIdOrt_patch_notUnique() {
			when(conn.existsBy(EXPECTED_QUERY, DTOOrtsteil.class, "Mitte", 42L, 7L)).thenReturn(true);

			final var result = repository.ortsteilnameIsUniqueForIdOrtPatch("Mitte", 42L, 7L);

			assertThat(result).isFalse();
			verify(conn, times(1)).existsBy(EXPECTED_QUERY, DTOOrtsteil.class, "Mitte", 42L, 7L);
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
		@DisplayName("Gibt referenzierte IDs zurück")
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
		@DisplayName("Gibt leeres Set zurück wenn keine Referenzen gefunden")
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
		@DisplayName("Duplikate in Ergebnisliste werden dedupliziert")
		void getReferencedIds_deduplicates() {
			@SuppressWarnings("unchecked")
			final TypedQuery<Long> queryMock = mock(TypedQuery.class);
			when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);
			when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
			when(queryMock.getResultList()).thenReturn(List.of(1L, 1L, 2L));

			final var result = repository.getReferencedIds(List.of(1L, 2L));

			assertThat(result).containsExactlyInAnyOrder(1L, 2L);
		}

		@Test
		@DisplayName("Query enthält alle vier Tabellen per UNION ALL")
		void getReferencedIds_queryContainsAllTables() {
			@SuppressWarnings("unchecked")
			final TypedQuery<Long> queryMock = mock(TypedQuery.class);
			when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
			when(queryMock.getResultList()).thenReturn(List.of());

			when(conn.query(anyString(), eq(Long.class))).thenAnswer(invocation -> {
				final String query = invocation.getArgument(0);
				assertThat(query)
						.contains("DTOLehrer")
						.contains("DTOSchueler")
						.contains("DTOSchuelerErzieherAdresse")
						.contains("DTOBetrieb")
						.contains("UNION ALL");
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
		@DisplayName("Gibt true zurück, wenn das Lehramt vorhanden ist")
		void existsById_found() {
			final long idOrtsteil = 500L;

			when(conn.existsBy(
					DTOOrtsteil.QUERY_BY_ID,
					DTOOrtsteil.class,
					idOrtsteil))
					.thenReturn(true);

			final var result = repository.existsById(idOrtsteil);

			assertThat(result).isTrue();

			verify(conn, times(1)).existsBy(
					DTOOrtsteil.QUERY_BY_ID,
					DTOOrtsteil.class,
					idOrtsteil);
		}

		@Test
		@DisplayName("Gibt false zurück, wenn das Lehramt nicht vorhanden ist")
		void existsById_notFound() {
			final long idOrtsteil = 999L;

			when(conn.existsBy(
					DTOOrtsteil.QUERY_BY_ID,
					DTOOrtsteil.class,
					idOrtsteil))
					.thenReturn(false);

			final var result = repository.existsById(idOrtsteil);

			assertThat(result).isFalse();

			verify(conn, times(1)).existsBy(
					DTOOrtsteil.QUERY_BY_ID,
					DTOOrtsteil.class,
					idOrtsteil);
		}
	}
}
