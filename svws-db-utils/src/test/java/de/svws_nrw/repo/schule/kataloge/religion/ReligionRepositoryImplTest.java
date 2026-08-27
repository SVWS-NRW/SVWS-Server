package de.svws_nrw.repo.schule.kataloge.religion;

import java.util.List;
import java.util.Set;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOReligion;
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
class ReligionRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private ReligionRepositoryImpl repository;

	// -------------------------------------------------------------------------
	// Hilfskonstanten
	// -------------------------------------------------------------------------

	private static final String QUERY_BEZEICHNUNG_CREATE =
			"SELECT m FROM DTOReligion m WHERE LOWER(m.bezeichnung) = LOWER(?1)";
	private static final String QUERY_BEZEICHNUNG_PATCH = """
        SELECT m FROM DTOReligion m
        WHERE LOWER(m.bezeichnung) = LOWER(?1)
          AND m.id != ?2
        """;

	// -------------------------------------------------------------------------
	// bezeichnungIsAlreadyUsedCreate
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("bezeichnungIsAlreadyUsedCreate")
	class BezeichnungIsAlreadyUsedCreate {

		@Test
		@DisplayName("Liefert true wenn Bezeichnung bereits existiert")
		void bezeichnungIsAlreadyUsedCreate_exists() {
			final var bezeichnung = "Evangelisch";
			when(conn.existsBy(QUERY_BEZEICHNUNG_CREATE, DTOReligion.class, bezeichnung)).thenReturn(true);

			final var result = repository.bezeichnungIstBereitsVergeben(bezeichnung);

			assertThat(result).isTrue();
			verify(conn, times(1)).existsBy(QUERY_BEZEICHNUNG_CREATE, DTOReligion.class, bezeichnung);
		}

		@Test
		@DisplayName("Liefert false wenn Bezeichnung nicht existiert")
		void bezeichnungIsAlreadyUsedCreate_notExists() {
			final var bezeichnung = "Unbekannt";
			when(conn.existsBy(QUERY_BEZEICHNUNG_CREATE, DTOReligion.class, bezeichnung)).thenReturn(false);

			final var result = repository.bezeichnungIstBereitsVergeben(bezeichnung);

			assertThat(result).isFalse();
			verify(conn, times(1)).existsBy(QUERY_BEZEICHNUNG_CREATE, DTOReligion.class, bezeichnung);
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
			final var bezeichnung = "Evangelisch";
			final var id = 1L;
			when(conn.existsBy(QUERY_BEZEICHNUNG_PATCH, DTOReligion.class, bezeichnung, id)).thenReturn(true);

			final var result = repository.bezeichnungIstBereitsVergebenExceptId(bezeichnung, id);

			assertThat(result).isTrue();
			verify(conn, times(1)).existsBy(QUERY_BEZEICHNUNG_PATCH, DTOReligion.class, bezeichnung, id);
		}

		@Test
		@DisplayName("Liefert false wenn Bezeichnung nur beim eigenen Eintrag existiert")
		void bezeichnungIsAlreadyUsedPatch_existsOnlyInSelf() {
			final var bezeichnung = "Evangelisch";
			final var id = 1L;
			when(conn.existsBy(QUERY_BEZEICHNUNG_PATCH, DTOReligion.class, bezeichnung, id)).thenReturn(false);

			final var result = repository.bezeichnungIstBereitsVergebenExceptId(bezeichnung, id);

			assertThat(result).isFalse();
			verify(conn, times(1)).existsBy(QUERY_BEZEICHNUNG_PATCH, DTOReligion.class, bezeichnung, id);
		}
	}

	// -------------------------------------------------------------------------
	// Konstruktor
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		final var newRepository = new ReligionRepositoryImpl(conn);

		assertThat(newRepository)
				.isNotNull()
				.isInstanceOf(ReligionRepositoryImpl.class)
				.isInstanceOf(ReligionRepository.class);
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
		@DisplayName("Query referenziert DTOSchueler über Religion_ID")
		void getReferencedIds_queryContainsDTOSchueler() {
			@SuppressWarnings("unchecked")
			final TypedQuery<Long> queryMock = mock(TypedQuery.class);
			when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
			when(queryMock.getResultList()).thenReturn(List.of());

			when(conn.query(anyString(), eq(Long.class))).thenAnswer(invocation -> {
				final String query = invocation.getArgument(0);
				assertThat(query)
						.contains("DTOSchueler")
						.contains("Religion_ID");
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
		@DisplayName("Gibt true zurück, wenn die Religion vorhanden ist")
		void existsById_found() {
			final long idReligion = 500L;

			when(conn.existsBy(
					DTOReligion.QUERY_BY_ID,
					DTOReligion.class,
					idReligion))
					.thenReturn(true);

			final var result = repository.existsById(idReligion);

			assertThat(result).isTrue();

			verify(conn, times(1)).existsBy(
					DTOReligion.QUERY_BY_ID,
					DTOReligion.class,
					idReligion);
		}

		@Test
		@DisplayName("Gibt false zurück, wenn die Religion nicht vorhanden ist")
		void existsById_notFound() {
			final long idReligion = 999L;

			when(conn.existsBy(
					DTOReligion.QUERY_BY_ID,
					DTOReligion.class,
					idReligion))
					.thenReturn(false);

			final var result = repository.existsById(idReligion);

			assertThat(result).isFalse();

			verify(conn, times(1)).existsBy(
					DTOReligion.QUERY_BY_ID,
					DTOReligion.class,
					idReligion);
		}
	}
}
