package de.svws_nrw.repo.schule.teilstandort;

import java.util.List;
import java.util.Set;

import de.svws_nrw.db.DBEntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeilstandortRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private TeilstandortRepositoryImpl repository;

	// -------------------------------------------------------------------------
	// Konstruktor
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		final var newRepository = new TeilstandortRepositoryImpl(conn);

		assertThat(newRepository)
				.isNotNull()
				.isInstanceOf(TeilstandortRepositoryImpl.class)
				.isInstanceOf(TeilstandortRepository.class);
	}

	// -------------------------------------------------------------------------
	// mapIdToParameter
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("mapIdToParameter | liefert das Adressmerkmal als einziges Element")
	void mapIdToParameter_success() {
		final var result = repository.mapIdToParameter("A");

		assertThat(result).containsExactly("A");
	}

	// -------------------------------------------------------------------------
	// getReferencedIds
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getReferencedIds")
	class GetReferencedIds {

		@Test
		@DisplayName("Gibt leeres Set zurück bei null, ohne DB-Zugriff")
		void getReferencedIds_null() {
			final var result = repository.getReferencedIds(null);

			assertThat(result).isEmpty();
			verifyNoInteractions(conn);
		}

		@Test
		@DisplayName("Gibt leeres Set zurück bei leerer Liste, ohne DB-Zugriff")
		void getReferencedIds_empty() {
			final var result = repository.getReferencedIds(List.of());

			assertThat(result).isEmpty();
			verifyNoInteractions(conn);
		}

		@Test
		@DisplayName("Gibt referenzierte Adressmerkmale zurück")
		void getReferencedIds_found() {
			final var adrMerkmale = List.of("A", "B", "C");
			@SuppressWarnings("unchecked")
			final TypedQuery<String> queryMock = mock(TypedQuery.class);
			when(conn.query(anyString(), eq(String.class))).thenReturn(queryMock);
			when(queryMock.setParameter("adrMerkmale", adrMerkmale)).thenReturn(queryMock);
			when(queryMock.getResultList()).thenReturn(List.of("A", "B"));

			final var result = repository.getReferencedIds(adrMerkmale);

			assertThat(result).isEqualTo(Set.of("A", "B"));
		}

		@Test
		@DisplayName("Gibt leeres Set zurück wenn keine Referenzen gefunden")
		void getReferencedIds_noneFound() {
			final var adrMerkmale = List.of("A");
			@SuppressWarnings("unchecked")
			final TypedQuery<String> queryMock = mock(TypedQuery.class);
			when(conn.query(anyString(), eq(String.class))).thenReturn(queryMock);
			when(queryMock.setParameter("adrMerkmale", adrMerkmale)).thenReturn(queryMock);
			when(queryMock.getResultList()).thenReturn(List.of());

			final var result = repository.getReferencedIds(adrMerkmale);

			assertThat(result).isEmpty();
		}

		@Test
		@DisplayName("Query referenziert DTOKlassen über AdrMerkmal")
		void getReferencedIds_queryContainsDTOKlassen() {
			final var adrMerkmale = List.of("A");
			@SuppressWarnings("unchecked")
			final TypedQuery<String> queryMock = mock(TypedQuery.class);
			when(queryMock.setParameter("adrMerkmale", adrMerkmale)).thenReturn(queryMock);
			when(queryMock.getResultList()).thenReturn(List.of());
			when(conn.query(anyString(), eq(String.class))).thenAnswer(invocation -> {
				final String query = invocation.getArgument(0);
				assertThat(query)
						.contains("DTOKlassen")
						.contains("AdrMerkmal");
				return queryMock;
			});

			repository.getReferencedIds(adrMerkmale);

			verify(conn, times(1)).query(anyString(), eq(String.class));
		}
	}
}
