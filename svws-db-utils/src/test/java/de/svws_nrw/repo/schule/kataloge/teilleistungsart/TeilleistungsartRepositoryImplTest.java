package de.svws_nrw.repo.schule.kataloge.teilleistungsart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;

import de.svws_nrw.db.dto.current.schild.schueler.DTOTeilleistungsarten;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.DBEntityManager;

@ExtendWith(MockitoExtension.class)
class TeilleistungsartRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private TeilleistungsartRepositoryImpl repository;

	@Test
	@DisplayName("Test: Erstelle einen neuen Eintrag und prüfe die Zuweisung der neuen ID.")
	void testCreate() {
		final DTOTeilleistungsarten neu = new DTOTeilleistungsarten(-1L);

		final long neueId = 42L;

		when(conn.transactionGetNextID(DTOTeilleistungsarten.class)).thenReturn(neueId);
		when(conn.transactionPersist(neu)).thenReturn(true);

		final DTOTeilleistungsarten result = repository.create(neu);

		assertNotNull(result);
		assertEquals(neueId, result.ID, "Die ID wurde nicht korrekt gesetzt.");
		verify(conn).transactionGetNextID(DTOTeilleistungsarten.class);
		verify(conn).transactionPersist(neu);
	}

	@Test
	@DisplayName("Test: Der Konstruktor setzt die ID-Zugriffe (getId/setId) korrekt auf das ID-Feld.")
	void testConstructorAndIdAccess() {
		// Teste getId indirekt via getMap()
		final DTOTeilleistungsarten k = new DTOTeilleistungsarten(999L);
		when(conn.queryAll(DTOTeilleistungsarten.class)).thenReturn(List.of(k));
		assertThat(repository.getMap()).containsEntry(999L, k);

		// Teste setId über die create-Methode
		when(conn.transactionGetNextID(DTOTeilleistungsarten.class)).thenReturn(1000L);
		when(conn.transactionPersist(k)).thenReturn(true);
		repository.create(k);
		assertThat(k.ID).isEqualTo(1000L);
	}

	@Nested
	@DisplayName("getReferencedIds")
	class GetReferencedIds {

		@Test
		@DisplayName("Gibt leeres Set zurück bei null, ohne eine Abfrage auszuführen")
		void getReferencedIds_null() {
			final var result = repository.getReferencedIds(null);

			assertThat(result).isEmpty();
			verifyNoInteractions(conn);
		}

		@Test
		@DisplayName("Gibt leeres Set zurück bei leerer Liste, ohne eine Abfrage auszuführen")
		void getReferencedIds_empty() {
			final var result = repository.getReferencedIds(List.of());

			assertThat(result).isEmpty();
			verifyNoInteractions(conn);
		}

		@Test
		@DisplayName("Gibt die in Teilleistungen referenzierten IDs ohne Duplikate zurück")
		void getReferencedIds_found() {
			final var queryMock = mockQuery(List.of(1L, 2L, 2L));

			final var result = repository.getReferencedIds(List.of(1L, 2L, 3L));

			assertThat(result).isEqualTo(Set.of(1L, 2L));
			verify(queryMock).setParameter("ids", List.of(1L, 2L, 3L));
		}

		@Test
		@DisplayName("Gibt leeres Set zurück, wenn keine Referenzen gefunden werden")
		void getReferencedIds_noneFound() {
			mockQuery(List.of());

			final var result = repository.getReferencedIds(List.of(1L));

			assertThat(result).isEmpty();
		}

		@Test
		@DisplayName("Query prüft DTOSchuelerTeilleistung über Art_ID")
		void getReferencedIds_queryContainsDTOSchuelerTeilleistung() {
			mockQuery(List.of());

			repository.getReferencedIds(List.of(1L));

			verify(conn).query("SELECT DISTINCT a.Art_ID FROM DTOSchuelerTeilleistung a WHERE a.Art_ID IN :ids", Long.class);
		}

		private TypedQuery<Long> mockQuery(final List<Long> resultList) {
			@SuppressWarnings("unchecked")
			final TypedQuery<Long> queryMock = mock(TypedQuery.class);
			when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);
			when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
			when(queryMock.getResultList()).thenReturn(resultList);
			return queryMock;
		}
	}

}
