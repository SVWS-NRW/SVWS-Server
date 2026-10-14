package de.svws_nrw.repo.lehrer.personalabschnittsdaten;

import java.util.List;

import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Tests für das Repository der Personalabschnittsdaten von Lehrkräften.
 */
@ExtendWith(MockitoExtension.class)
class LehrerPersonalabschnittsdatenRepositoryImplTest {

	private static final long ID_LEHRER                = 10L;
	private static final long ID_LEHRER_2              = 11L;
	private static final long ID_SCHULJAHRESABSCHNITT  = 5L;

	private static final String QUERY_BY_LEHRER_IDS_UND_ABSCHNITT =
			"SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.Lehrer_ID IN ?1 AND e.Schuljahresabschnitts_ID = ?2";
	private static final String QUERY_BY_LEHRER_IDS =
			"SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.Lehrer_ID IN ?1";

	@Mock()
	private DBEntityManager conn;

	@Test
	@DisplayName("constructor | Erfolg")
	void constructor_success() {
		final var repository = new LehrerPersonalabschnittsdatenRepositoryImpl(conn);

		assertThat(repository)
				.isNotNull()
				.isInstanceOf(LehrerPersonalabschnittsdatenRepository.class)
				.isInstanceOf(LehrerPersonalabschnittsdatenRepositoryImpl.class);
	}

	@Test
	@DisplayName("findByIdLehrer | delegiert an DBEntityManager.queryList")
	void findByIdLehrer_delegatesToConn() {
		final var repository = new LehrerPersonalabschnittsdatenRepositoryImpl(conn);
		final var expected = List.of(new DTOLehrerAbschnittsdaten(1L, 10L, 20L));

		when(conn.queryList(
				DTOLehrerAbschnittsdaten.QUERY_BY_LEHRER_ID,
				DTOLehrerAbschnittsdaten.class,
				10L
		)).thenReturn(expected);

		final var result = repository.findByIdLehrer(10L);

		assertThat(result).isSameAs(expected);
		verify(conn).queryList(
				DTOLehrerAbschnittsdaten.QUERY_BY_LEHRER_ID,
				DTOLehrerAbschnittsdaten.class,
				10L
		);
	}

	// -------------------------------------------------------------------------
	// findByIdsLehrer
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("findByIdsLehrer")
	class FindByIdsLehrer {

		@Test
		@DisplayName("Gibt leere Liste bei null zurück, ohne die Datenbank anzufragen")
		void findByIdsLehrer_null() {
			final var repository = new LehrerPersonalabschnittsdatenRepositoryImpl(conn);

			final var result = repository.findByIdsLehrer(null);

			assertThat(result).isEmpty();
			verifyNoInteractions(conn);
		}

		@Test
		@DisplayName("Gibt leere Liste bei leerer Eingabe zurück, ohne die Datenbank anzufragen")
		void findByIdsLehrer_leer() {
			final var repository = new LehrerPersonalabschnittsdatenRepositoryImpl(conn);

			final var result = repository.findByIdsLehrer(List.of());

			assertThat(result).isEmpty();
			verifyNoInteractions(conn);
		}

		@Test
		@DisplayName("Delegiert an DBEntityManager.queryList")
		void findByIdsLehrer_delegatesToConn() {
			final var repository = new LehrerPersonalabschnittsdatenRepositoryImpl(conn);
			final var idsLehrer = List.of(ID_LEHRER, ID_LEHRER_2);
			final var expected = List.of(new DTOLehrerAbschnittsdaten(1L, ID_LEHRER, 20L));

			when(conn.queryList(
					DTOLehrerAbschnittsdaten.QUERY_LIST_BY_LEHRER_ID,
					DTOLehrerAbschnittsdaten.class,
					idsLehrer
			)).thenReturn(expected);

			final var result = repository.findByIdsLehrer(idsLehrer);

			assertThat(result).isSameAs(expected);
		}
	}

	// -------------------------------------------------------------------------
	// getListByLehrerIds
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getListByLehrerIds")
	class GetListByLehrerIds {

		@Test
		@DisplayName("Gibt leere Liste bei null zurück, ohne die Datenbank anzufragen")
		void getListByLehrerIds_null() {
			final var repository = new LehrerPersonalabschnittsdatenRepositoryImpl(conn);

			final var result = repository.getListByLehrerIds(null);

			assertThat(result).isEmpty();
			verifyNoInteractions(conn);
		}

		@Test
		@DisplayName("Gibt leere Liste bei leerer Eingabe zurück, ohne die Datenbank anzufragen")
		void getListByLehrerIds_leer() {
			final var repository = new LehrerPersonalabschnittsdatenRepositoryImpl(conn);

			final var result = repository.getListByLehrerIds(List.of());

			assertThat(result).isEmpty();
			verifyNoInteractions(conn);
		}

		@Test
		@DisplayName("Delegiert an DBEntityManager.queryList")
		void getListByLehrerIds_delegatesToConn() {
			final var repository = new LehrerPersonalabschnittsdatenRepositoryImpl(conn);
			final var idsLehrer = List.of(ID_LEHRER, ID_LEHRER_2);
			final var expected = List.of(new DTOLehrerAbschnittsdaten(1L, ID_LEHRER, 20L));

			when(conn.queryList(
					QUERY_BY_LEHRER_IDS,
					DTOLehrerAbschnittsdaten.class,
					idsLehrer
			)).thenReturn(expected);

			final var result = repository.getListByLehrerIds(idsLehrer);

			assertThat(result).isSameAs(expected);
		}
	}

	// -------------------------------------------------------------------------
	// getListByLehrerIdsAndSchuljahresabschnitt
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getListByLehrerIdsAndSchuljahresabschnitt")
	class GetListByLehrerIdsAndSchuljahresabschnitt {

		@Test
		@DisplayName("Gibt leere Liste bei null zurück, ohne die Datenbank anzufragen")
		void getListByLehrerIdsAndSchuljahresabschnitt_null() {
			final var repository = new LehrerPersonalabschnittsdatenRepositoryImpl(conn);

			final var result = repository.getListByLehrerIdsAndSchuljahresabschnitt(null, ID_SCHULJAHRESABSCHNITT);

			assertThat(result).isEmpty();
			verifyNoInteractions(conn);
		}

		@Test
		@DisplayName("Gibt leere Liste bei leerer Eingabe zurück, ohne die Datenbank anzufragen")
		void getListByLehrerIdsAndSchuljahresabschnitt_leer() {
			final var repository = new LehrerPersonalabschnittsdatenRepositoryImpl(conn);

			final var result = repository.getListByLehrerIdsAndSchuljahresabschnitt(List.of(), ID_SCHULJAHRESABSCHNITT);

			assertThat(result).isEmpty();
			verifyNoInteractions(conn);
		}

		@Test
		@DisplayName("Gibt leere Liste zurück, wenn der Schuljahresabschnitt unbekannt ist")
		void getListByLehrerIdsAndSchuljahresabschnitt_abschnittUnbekannt() {
			final var repository = new LehrerPersonalabschnittsdatenRepositoryImpl(conn);

			when(conn.getUser()).thenReturn(mock(Benutzer.class));
			when(conn.getUser().schuleGetAbschnittById(ID_SCHULJAHRESABSCHNITT)).thenReturn(null);

			final var result = repository.getListByLehrerIdsAndSchuljahresabschnitt(List.of(ID_LEHRER), ID_SCHULJAHRESABSCHNITT);

			assertThat(result).isEmpty();
			verify(conn, never()).queryList(ArgumentMatchers.anyString(), any(), any(), any());
		}

		@Test
		@DisplayName("Delegiert an DBEntityManager.queryList, wenn der Schuljahresabschnitt bekannt ist")
		void getListByLehrerIdsAndSchuljahresabschnitt_delegatesToConn() {
			final var repository = new LehrerPersonalabschnittsdatenRepositoryImpl(conn);
			final var idsLehrer = List.of(ID_LEHRER, ID_LEHRER_2);
			final var expected = List.of(new DTOLehrerAbschnittsdaten(1L, ID_LEHRER, ID_SCHULJAHRESABSCHNITT));

			when(conn.getUser()).thenReturn(mock(Benutzer.class));
			when(conn.getUser().schuleGetAbschnittById(ID_SCHULJAHRESABSCHNITT))
					.thenAnswer(invocation -> mock(invocation.getMethod().getReturnType()));
			when(conn.queryList(
					QUERY_BY_LEHRER_IDS_UND_ABSCHNITT,
					DTOLehrerAbschnittsdaten.class,
					idsLehrer,
					ID_SCHULJAHRESABSCHNITT
			)).thenReturn(expected);

			final var result = repository.getListByLehrerIdsAndSchuljahresabschnitt(idsLehrer, ID_SCHULJAHRESABSCHNITT);

			assertThat(result).isSameAs(expected);
		}
	}
}

