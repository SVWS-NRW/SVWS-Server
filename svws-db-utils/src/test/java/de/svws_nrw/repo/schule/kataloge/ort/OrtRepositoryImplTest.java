package de.svws_nrw.repo.schule.kataloge.ort;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
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
class OrtRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private OrtRepositoryImpl repository;

	// -------------------------------------------------------------------------
	// Konstruktor
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		final var newRepository = new OrtRepositoryImpl(conn);

		assertThat(newRepository)
				.isNotNull()
				.isInstanceOf(OrtRepositoryImpl.class)
				.isInstanceOf(OrtRepository.class);
	}

	// -------------------------------------------------------------------------
	// ortsnameIsUniqueForPlzCreate
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("ortsnameIsUniqueForPlzCreate")
	class OrtsnameIsUniqueForPlzCreate {

		private static final String EXPECTED_QUERY =
				"SELECT o FROM DTOOrt o WHERE LOWER(o.ortsname) = LOWER(?1) AND LOWER(o.plz) = LOWER(?2)";

		@Test
		@DisplayName("Gibt true zurück wenn Ortsname für PLZ noch nicht vergeben")
		void ortsnameIsUniqueForPlzCreate_unique() {
			when(conn.existsBy(EXPECTED_QUERY, DTOOrt.class, "Troisdorf", "53840")).thenReturn(false);

			final var result = repository.ortsnameIsUniqueForPlzCreate("Troisdorf", "53840");

			assertThat(result).isTrue();
			verify(conn, times(1)).existsBy(EXPECTED_QUERY, DTOOrt.class, "Troisdorf", "53840");
		}

		@Test
		@DisplayName("Gibt false zurück wenn Ortsname für PLZ bereits vergeben")
		void ortsnameIsUniqueForPlzCreate_notUnique() {
			when(conn.existsBy(EXPECTED_QUERY, DTOOrt.class, "Troisdorf", "53840")).thenReturn(true);

			final var result = repository.ortsnameIsUniqueForPlzCreate("Troisdorf", "53840");

			assertThat(result).isFalse();
			verify(conn, times(1)).existsBy(EXPECTED_QUERY, DTOOrt.class, "Troisdorf", "53840");
		}
	}

	// -------------------------------------------------------------------------
	// ortsnameIsUniqueForPlzPatch
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("ortsnameIsUniqueForPlzPatch")
	class OrtsnameIsUniqueForPlzPatch {

		private static final String EXPECTED_QUERY =
				"SELECT o FROM DTOOrt o WHERE LOWER(o.ortsname) = LOWER(?1) AND LOWER(o.plz) = LOWER(?2) AND o.id != ?3";

		@Test
		@DisplayName("Gibt true zurück wenn kein anderer Ort den Ortsnamen für die PLZ verwendet")
		void ortsnameIsUniqueForPlzPatch_unique() {
			when(conn.existsBy(EXPECTED_QUERY, DTOOrt.class, "Troisdorf", "53840", 1L)).thenReturn(false);

			final var result = repository.ortsnameIsUniqueForPlzPatch("Troisdorf", "53840", 1L);

			assertThat(result).isTrue();
			verify(conn, times(1)).existsBy(EXPECTED_QUERY, DTOOrt.class, "Troisdorf", "53840", 1L);
		}

		@Test
		@DisplayName("Gibt false zurück wenn ein anderer Ort den Ortsnamen für die PLZ bereits verwendet")
		void ortsnameIsUniqueForPlzPatch_notUnique() {
			when(conn.existsBy(EXPECTED_QUERY, DTOOrt.class, "Troisdorf", "53840", 1L)).thenReturn(true);

			final var result = repository.ortsnameIsUniqueForPlzPatch("Troisdorf", "53840", 1L);

			assertThat(result).isFalse();
			verify(conn, times(1)).existsBy(EXPECTED_QUERY, DTOOrt.class, "Troisdorf", "53840", 1L);
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
		@DisplayName("Query enthält alle fünf Tabellen per UNION ALL")
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
						.contains("DTOOrtsteil")
						.contains("UNION ALL");
				return queryMock;
			});

			repository.getReferencedIds(List.of(1L));

			verify(conn, times(1)).query(anyString(), eq(Long.class));
		}
	}
}
