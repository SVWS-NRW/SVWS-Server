package de.svws_nrw.repo.schule.kataloge.ankreuzkompetenz;

import java.util.List;
import java.util.Set;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOAnkreuzkompetenz;
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
class AnkreuzkompetenzRepositoryImplTest {

	private static final String QUERY_FLOSKELTEXT_MIT_FACH =
			"SELECT a FROM DTOAnkreuzkompetenz a WHERE LOWER(a.floskelText) = LOWER(?1) AND a.idFach = ?2";

	private static final String QUERY_FLOSKELTEXT_OHNE_FACH =
			"SELECT a FROM DTOAnkreuzkompetenz a WHERE LOWER(a.floskelText) = LOWER(?1) AND a.idFach IS NULL";

	private static final String QUERY_FLOSKELTEXT_MIT_FACH_EXCEPT_ID =
			"SELECT a FROM DTOAnkreuzkompetenz a WHERE LOWER(a.floskelText) = LOWER(?1) AND a.idFach = ?2 AND a.id != ?3";

	private static final String QUERY_FLOSKELTEXT_OHNE_FACH_EXCEPT_ID =
			"SELECT a FROM DTOAnkreuzkompetenz a WHERE LOWER(a.floskelText) = LOWER(?1) AND a.idFach IS NULL AND a.id != ?2";

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private AnkreuzkompetenzRepositoryImpl repository;

	// -------------------------------------------------------------------------
	// Konstruktor
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		final var newRepository = new AnkreuzkompetenzRepositoryImpl(conn);

		assertThat(newRepository)
				.isNotNull()
				.isInstanceOf(AnkreuzkompetenzRepositoryImpl.class)
				.isInstanceOf(AnkreuzkompetenzRepository.class);
	}

	// -------------------------------------------------------------------------
	// existsById
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("existsById")
	class ExistsById {

		@Test
		@DisplayName("Gibt true zurück, wenn die Ankreuzkompetenz vorhanden ist")
		void existsById_found() {
			final var idAnkreuzkompetenz = 500L;

			when(conn.existsBy(
					DTOAnkreuzkompetenz.QUERY_BY_ID,
					DTOAnkreuzkompetenz.class,
					idAnkreuzkompetenz))
					.thenReturn(true);

			final var result = repository.existsById(idAnkreuzkompetenz);

			assertThat(result).isTrue();

			verify(conn, times(1)).existsBy(
					DTOAnkreuzkompetenz.QUERY_BY_ID,
					DTOAnkreuzkompetenz.class,
					idAnkreuzkompetenz);
		}

		@Test
		@DisplayName("Gibt false zurück, wenn die Ankreuzkompetenz nicht vorhanden ist")
		void existsById_notFound() {
			final var idAnkreuzkompetenz = 999L;

			when(conn.existsBy(
					DTOAnkreuzkompetenz.QUERY_BY_ID,
					DTOAnkreuzkompetenz.class,
					idAnkreuzkompetenz))
					.thenReturn(false);

			final var result = repository.existsById(idAnkreuzkompetenz);

			assertThat(result).isFalse();

			verify(conn, times(1)).existsBy(
					DTOAnkreuzkompetenz.QUERY_BY_ID,
					DTOAnkreuzkompetenz.class,
					idAnkreuzkompetenz);
		}
	}

	// -------------------------------------------------------------------------
	// floskelTextIsAlreadyUsedCreate
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("floskelTextIsAlreadyUsedCreate")
	class FloskelTextIsAlreadyUsedCreate {

		@Test
		@DisplayName("Liefert true, wenn der Text für das Fach bereits existiert")
		void mitFach_existiert() {
			when(conn.existsBy(QUERY_FLOSKELTEXT_MIT_FACH, DTOAnkreuzkompetenz.class, "Text", 5L)).thenReturn(true);

			final var result = repository.floskelTextIsAlreadyUsedCreate("Text", 5L);

			assertThat(result).isTrue();
			verify(conn, times(1)).existsBy(QUERY_FLOSKELTEXT_MIT_FACH, DTOAnkreuzkompetenz.class, "Text", 5L);
		}

		@Test
		@DisplayName("Liefert false, wenn der Text für das Fach nicht existiert")
		void mitFach_existiertNicht() {
			when(conn.existsBy(QUERY_FLOSKELTEXT_MIT_FACH, DTOAnkreuzkompetenz.class, "Text", 5L)).thenReturn(false);

			final var result = repository.floskelTextIsAlreadyUsedCreate("Text", 5L);

			assertThat(result).isFalse();
		}

		@Test
		@DisplayName("Prüft ohne Fach auf idFach IS NULL und übergibt keinen Fach-Parameter")
		void ohneFach_existiert() {
			when(conn.existsBy(QUERY_FLOSKELTEXT_OHNE_FACH, DTOAnkreuzkompetenz.class, "Text")).thenReturn(true);

			final var result = repository.floskelTextIsAlreadyUsedCreate("Text", null);

			assertThat(result).isTrue();
			verify(conn, times(1)).existsBy(QUERY_FLOSKELTEXT_OHNE_FACH, DTOAnkreuzkompetenz.class, "Text");
		}

		@Test
		@DisplayName("Liefert ohne Fach false, wenn der Text nicht existiert")
		void ohneFach_existiertNicht() {
			when(conn.existsBy(QUERY_FLOSKELTEXT_OHNE_FACH, DTOAnkreuzkompetenz.class, "Text")).thenReturn(false);

			final var result = repository.floskelTextIsAlreadyUsedCreate("Text", null);

			assertThat(result).isFalse();
		}
	}

	// -------------------------------------------------------------------------
	// floskelTextIsAlreadyUsedPatch
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("floskelTextIsAlreadyUsedPatch")
	class FloskelTextIsAlreadyUsedPatch {

		@Test
		@DisplayName("Liefert true, wenn der Text für das Fach bei einem anderen Eintrag existiert")
		void mitFach_existiertBeiAnderem() {
			when(conn.existsBy(QUERY_FLOSKELTEXT_MIT_FACH_EXCEPT_ID, DTOAnkreuzkompetenz.class, "Text", 5L, 1L)).thenReturn(true);

			final var result = repository.floskelTextIsAlreadyUsedPatch("Text", 5L, 1L);

			assertThat(result).isTrue();
			verify(conn, times(1)).existsBy(QUERY_FLOSKELTEXT_MIT_FACH_EXCEPT_ID, DTOAnkreuzkompetenz.class, "Text", 5L, 1L);
		}

		@Test
		@DisplayName("Liefert false, wenn der Text für das Fach nur beim eigenen Eintrag existiert")
		void mitFach_existiertNurBeiSich() {
			when(conn.existsBy(QUERY_FLOSKELTEXT_MIT_FACH_EXCEPT_ID, DTOAnkreuzkompetenz.class, "Text", 5L, 1L)).thenReturn(false);

			final var result = repository.floskelTextIsAlreadyUsedPatch("Text", 5L, 1L);

			assertThat(result).isFalse();
		}

		@Test
		@DisplayName("Prüft ohne Fach auf idFach IS NULL und schließt die eigene ID aus")
		void ohneFach_existiertBeiAnderem() {
			when(conn.existsBy(QUERY_FLOSKELTEXT_OHNE_FACH_EXCEPT_ID, DTOAnkreuzkompetenz.class, "Text", 1L)).thenReturn(true);

			final var result = repository.floskelTextIsAlreadyUsedPatch("Text", null, 1L);

			assertThat(result).isTrue();
			verify(conn, times(1)).existsBy(QUERY_FLOSKELTEXT_OHNE_FACH_EXCEPT_ID, DTOAnkreuzkompetenz.class, "Text", 1L);
		}

		@Test
		@DisplayName("Liefert ohne Fach false, wenn der Text nur beim eigenen Eintrag existiert")
		void ohneFach_existiertNurBeiSich() {
			when(conn.existsBy(QUERY_FLOSKELTEXT_OHNE_FACH_EXCEPT_ID, DTOAnkreuzkompetenz.class, "Text", 1L)).thenReturn(false);

			final var result = repository.floskelTextIsAlreadyUsedPatch("Text", null, 1L);

			assertThat(result).isFalse();
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
			verify(conn, times(0)).query(anyString(), eq(Long.class));
		}

		@Test
		@DisplayName("Gibt leeres Set zurück bei leerer Liste")
		void getReferencedIds_empty() {
			final var result = repository.getReferencedIds(List.of());

			assertThat(result).isEmpty();
			verify(conn, times(0)).query(anyString(), eq(Long.class));
		}

		@Test
		@DisplayName("Gibt nur die referenzierten IDs zurück")
		void getReferencedIds_found() {
			@SuppressWarnings("unchecked")
			final TypedQuery<Long> queryMock = mock(TypedQuery.class);
			when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);
			when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
			when(queryMock.getResultList()).thenReturn(List.of(1L, 3L));

			final var result = repository.getReferencedIds(List.of(1L, 2L, 3L));

			assertThat(result).isEqualTo(Set.of(1L, 3L));
			verify(queryMock, times(1)).setParameter("ids", List.of(1L, 2L, 3L));
		}

		@Test
		@DisplayName("Gibt leeres Set zurück, wenn keine Referenzen gefunden werden")
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
		@DisplayName("Query prüft DTOSchuelerAnkreuzfloskeln über Floskel_ID")
		void getReferencedIds_queryContainsDTOSchuelerAnkreuzfloskeln() {
			@SuppressWarnings("unchecked")
			final TypedQuery<Long> queryMock = mock(TypedQuery.class);
			when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
			when(queryMock.getResultList()).thenReturn(List.of());
			when(conn.query(anyString(), eq(Long.class))).thenAnswer(invocation -> {
				final String query = invocation.getArgument(0);
				assertThat(query)
						.contains("DTOSchuelerAnkreuzfloskeln")
						.contains("Floskel_ID");
				return queryMock;
			});

			repository.getReferencedIds(List.of(1L));

			verify(conn, times(1)).query(anyString(), eq(Long.class));
		}
	}
}
