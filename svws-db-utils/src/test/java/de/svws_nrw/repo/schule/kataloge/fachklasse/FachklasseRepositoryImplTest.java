package de.svws_nrw.repo.schule.kataloge.fachklasse;

import java.util.List;
import java.util.Set;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.berufskolleg.DTOFachklassen;
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
class FachklasseRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private FachklasseRepositoryImpl repository;

	// -------------------------------------------------------------------------
	// Hilfskonstanten
	// -------------------------------------------------------------------------

	private static final String QUERY_KUERZEL_CREATE =
			"SELECT m FROM DTOFachklassen m WHERE LOWER(m.kuerzel) = LOWER(?1)";
	private static final String QUERY_KUERZEL_PATCH =
			"SELECT m FROM DTOFachklassen m WHERE LOWER(m.kuerzel) = LOWER(?1) AND m.id != ?2";

	// -------------------------------------------------------------------------
	// kuerzelIsAlreadyUsedCreate
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("kuerzelIsAlreadyUsedCreate")
	class KuerzelIsAlreadyUsedCreate {

		@Test
		@DisplayName("Liefert true wenn Kürzel bereits existiert")
		void kuerzelIsAlreadyUsedCreate_exists() {
			final var kuerzel = "BK-IT";
			when(conn.existsBy(QUERY_KUERZEL_CREATE, DTOFachklassen.class, kuerzel)).thenReturn(true);

			final var result = repository.kuerzelIsAlreadyUsedCreate(kuerzel);

			assertThat(result).isTrue();
			verify(conn, times(1)).existsBy(QUERY_KUERZEL_CREATE, DTOFachklassen.class, kuerzel);
		}

		@Test
		@DisplayName("Liefert false wenn Kürzel nicht existiert")
		void kuerzelIsAlreadyUsedCreate_notExists() {
			final var kuerzel = "NOTEXIST";
			when(conn.existsBy(QUERY_KUERZEL_CREATE, DTOFachklassen.class, kuerzel)).thenReturn(false);

			final var result = repository.kuerzelIsAlreadyUsedCreate(kuerzel);

			assertThat(result).isFalse();
			verify(conn, times(1)).existsBy(QUERY_KUERZEL_CREATE, DTOFachklassen.class, kuerzel);
		}
	}

	// -------------------------------------------------------------------------
	// kuerzelIsAlreadyUsedPatch
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("kuerzelIsAlreadyUsedPatch")
	class KuerzelIsAlreadyUsedPatch {

		@Test
		@DisplayName("Liefert true wenn Kürzel bei einem anderen Eintrag existiert")
		void kuerzelIsAlreadyUsedPatch_existsInOther() {
			final var kuerzel = "BK-IT";
			final var id = 1L;
			when(conn.existsBy(QUERY_KUERZEL_PATCH, DTOFachklassen.class, kuerzel, id)).thenReturn(true);

			final var result = repository.kuerzelIsAlreadyUsedPatch(kuerzel, id);

			assertThat(result).isTrue();
			verify(conn, times(1)).existsBy(QUERY_KUERZEL_PATCH, DTOFachklassen.class, kuerzel, id);
		}

		@Test
		@DisplayName("Liefert false wenn Kürzel nur beim eigenen Eintrag existiert")
		void kuerzelIsAlreadyUsedPatch_existsOnlyInSelf() {
			final var kuerzel = "BK-IT";
			final var id = 1L;
			when(conn.existsBy(QUERY_KUERZEL_PATCH, DTOFachklassen.class, kuerzel, id)).thenReturn(false);

			final var result = repository.kuerzelIsAlreadyUsedPatch(kuerzel, id);

			assertThat(result).isFalse();
			verify(conn, times(1)).existsBy(QUERY_KUERZEL_PATCH, DTOFachklassen.class, kuerzel, id);
		}
	}

	// -------------------------------------------------------------------------
	// Konstruktor
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		final var newRepository = new FachklasseRepositoryImpl(conn);

		assertThat(newRepository)
				.isNotNull()
				.isInstanceOf(FachklasseRepositoryImpl.class)
				.isInstanceOf(FachklasseRepository.class);
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
		@DisplayName("Query enthält alle vier Tabellen per UNION ALL")
		void getReferencedIds_queryContainsAllTables() {
			@SuppressWarnings("unchecked")
			final TypedQuery<Long> queryMock = mock(TypedQuery.class);
			when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
			when(queryMock.getResultList()).thenReturn(List.of());

			when(conn.query(anyString(), eq(Long.class))).thenAnswer(invocation -> {
				final String query = invocation.getArgument(0);
				assertThat(query)
						.contains("DTOZuordnungReportvorlagen")
						.contains("DTOSchueler")
						.contains("DTOSchuelerLernabschnittsdaten")
						.contains("DTOKlassen")
						.contains("UNION ALL");
				return queryMock;
			});

			repository.getReferencedIds(List.of(1L));

			verify(conn, times(1)).query(anyString(), eq(Long.class));
		}
	}
}
