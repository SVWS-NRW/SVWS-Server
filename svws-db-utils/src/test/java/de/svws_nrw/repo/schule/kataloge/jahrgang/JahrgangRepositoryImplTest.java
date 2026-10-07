package de.svws_nrw.repo.schule.kataloge.jahrgang;

import java.util.List;
import java.util.Set;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class JahrgangRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private JahrgangRepositoryImpl repository;

	// -------------------------------------------------------------------------
	// Hilfskonstanten
	// -------------------------------------------------------------------------

	private static final String QUERY_KUERZEL_CREATE =
			"SELECT j FROM DTOJahrgang j WHERE LOWER(j.InternKrz) = LOWER(?1)";
	private static final String QUERY_BEZEICHNUNG_CREATE =
			"SELECT j FROM DTOJahrgang j WHERE LOWER(j.ASDBezeichnung) = LOWER(?1)";
	private static final String QUERY_KUERZEL_PATCH =
			"SELECT j FROM DTOJahrgang j WHERE LOWER(j.InternKrz) = LOWER(?1) AND j.ID != ?2";
	private static final String QUERY_BEZEICHNUNG_PATCH =
			"SELECT j FROM DTOJahrgang j WHERE LOWER(j.ASDBezeichnung) = LOWER(?1) AND j.ID != ?2";

	// -------------------------------------------------------------------------
	// Konstruktor
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		final var newRepository = new JahrgangRepositoryImpl(conn);

		assertThat(newRepository)
				.isNotNull()
				.isInstanceOf(JahrgangRepositoryImpl.class)
				.isInstanceOf(JahrgangRepository.class);
	}

	// -------------------------------------------------------------------------
	// existsById
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("existsById")
	class ExistsById {

		@Test
		@DisplayName("Gibt true zurück, wenn der Jahrgang vorhanden ist")
		void existsById_found() {
			final long idJahrgang = 500L;

			when(conn.existsBy(
					DTOJahrgang.QUERY_BY_ID,
					DTOJahrgang.class,
					idJahrgang))
					.thenReturn(true);

			final var result = repository.existsById(idJahrgang);

			assertThat(result).isTrue();

			verify(conn, times(1)).existsBy(
					DTOJahrgang.QUERY_BY_ID,
					DTOJahrgang.class,
					idJahrgang);
		}

		@Test
		@DisplayName("Gibt false zurück, wenn der Jahrgang nicht vorhanden ist")
		void existsById_notFound() {
			final long idJahrgang = 999L;

			when(conn.existsBy(
					DTOJahrgang.QUERY_BY_ID,
					DTOJahrgang.class,
					idJahrgang))
					.thenReturn(false);

			final var result = repository.existsById(idJahrgang);

			assertThat(result).isFalse();

			verify(conn, times(1)).existsBy(
					DTOJahrgang.QUERY_BY_ID,
					DTOJahrgang.class,
					idJahrgang);
		}
	}

	// -------------------------------------------------------------------------
	// kuerzelIsAlreadyUsedCreate
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("kuerzelIsAlreadyUsedCreate")
	class KuerzelIsAlreadyUsedCreate {

		@Test
		@DisplayName("Liefert true wenn Kürzel bereits existiert")
		void kuerzelIsAlreadyUsedCreate_exists() {
			final var kuerzel = "05";
			when(conn.existsBy(QUERY_KUERZEL_CREATE, DTOJahrgang.class, kuerzel)).thenReturn(true);

			final var result = repository.kuerzelIsAlreadyUsedCreate(kuerzel);

			assertThat(result).isTrue();
			verify(conn, times(1)).existsBy(QUERY_KUERZEL_CREATE, DTOJahrgang.class, kuerzel);
		}

		@Test
		@DisplayName("Liefert false wenn Kürzel nicht existiert")
		void kuerzelIsAlreadyUsedCreate_notExists() {
			final var kuerzel = "05";
			when(conn.existsBy(QUERY_KUERZEL_CREATE, DTOJahrgang.class, kuerzel)).thenReturn(false);

			final var result = repository.kuerzelIsAlreadyUsedCreate(kuerzel);

			assertThat(result).isFalse();
			verify(conn, times(1)).existsBy(QUERY_KUERZEL_CREATE, DTOJahrgang.class, kuerzel);
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
			final var bezeichnung = "3. Jahrgang";
			when(conn.existsBy(QUERY_BEZEICHNUNG_CREATE, DTOJahrgang.class, bezeichnung)).thenReturn(true);

			final var result = repository.bezeichnungIsAlreadyUsedCreate(bezeichnung);

			assertThat(result).isTrue();
			verify(conn, times(1)).existsBy(QUERY_BEZEICHNUNG_CREATE, DTOJahrgang.class, bezeichnung);
		}

		@Test
		@DisplayName("Liefert false wenn Bezeichnung nicht existiert")
		void bezeichnungIsAlreadyUsedCreate_notExists() {
			final var bezeichnung = "3. Jahrgang";
			when(conn.existsBy(QUERY_BEZEICHNUNG_CREATE, DTOJahrgang.class, bezeichnung)).thenReturn(false);

			final var result = repository.bezeichnungIsAlreadyUsedCreate(bezeichnung);

			assertThat(result).isFalse();
			verify(conn, times(1)).existsBy(QUERY_BEZEICHNUNG_CREATE, DTOJahrgang.class, bezeichnung);
		}
	}

	// -------------------------------------------------------------------------
	// kuerzelIsAlreadyUsedPatch
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("kuerzelIsAlreadyUsedPatch")
	class KuerzelIsAlreadyUsedPatch {

		@Test
		@DisplayName("Liefert true wenn ein anderer Jahrgang das Kürzel bereits verwendet")
		void kuerzelIsAlreadyUsedPatch_exists() {
			final var kuerzel = "05";
			final var id = 1L;
			when(conn.existsBy(QUERY_KUERZEL_PATCH, DTOJahrgang.class, kuerzel, id)).thenReturn(true);

			final var result = repository.kuerzelIsAlreadyUsedPatch(kuerzel, id);

			assertThat(result).isTrue();
			verify(conn, times(1)).existsBy(QUERY_KUERZEL_PATCH, DTOJahrgang.class, kuerzel, id);
		}

		@Test
		@DisplayName("Liefert false wenn nur der eigene Jahrgang das Kürzel verwendet")
		void kuerzelIsAlreadyUsedPatch_notExists() {
			final var kuerzel = "05";
			final var id = 1L;
			when(conn.existsBy(QUERY_KUERZEL_PATCH, DTOJahrgang.class, kuerzel, id)).thenReturn(false);

			final var result = repository.kuerzelIsAlreadyUsedPatch(kuerzel, id);

			assertThat(result).isFalse();
			verify(conn, times(1)).existsBy(QUERY_KUERZEL_PATCH, DTOJahrgang.class, kuerzel, id);
		}
	}

	// -------------------------------------------------------------------------
	// bezeichnungIsAlreadyUsedPatch
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("bezeichnungIsAlreadyUsedPatch")
	class BezeichnungIsAlreadyUsedPatch {

		@Test
		@DisplayName("Liefert true wenn ein anderer Jahrgang die Bezeichnung bereits verwendet")
		void bezeichnungIsAlreadyUsedPatch_exists() {
			final var bezeichnung = "3. Jahrgang";
			final var id = 1L;
			when(conn.existsBy(QUERY_BEZEICHNUNG_PATCH, DTOJahrgang.class, bezeichnung, id)).thenReturn(true);

			final var result = repository.bezeichnungIsAlreadyUsedPatch(bezeichnung, id);

			assertThat(result).isTrue();
			verify(conn, times(1)).existsBy(QUERY_BEZEICHNUNG_PATCH, DTOJahrgang.class, bezeichnung, id);
		}

		@Test
		@DisplayName("Liefert false wenn nur der eigene Jahrgang die Bezeichnung verwendet")
		void bezeichnungIsAlreadyUsedPatch_notExists() {
			final var bezeichnung = "3. Jahrgang";
			final var id = 1L;
			when(conn.existsBy(QUERY_BEZEICHNUNG_PATCH, DTOJahrgang.class, bezeichnung, id)).thenReturn(false);

			final var result = repository.bezeichnungIsAlreadyUsedPatch(bezeichnung, id);

			assertThat(result).isFalse();
			verify(conn, times(1)).existsBy(QUERY_BEZEICHNUNG_PATCH, DTOJahrgang.class, bezeichnung, id);
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
			verifyNoInteractions(conn);
		}

		@Test
		@DisplayName("Gibt leeres Set zurück bei leerer Liste")
		void getReferencedIds_empty() {
			final var result = repository.getReferencedIds(List.of());

			assertThat(result).isEmpty();
			verifyNoInteractions(conn);
		}

		@Test
		@DisplayName("Gibt referenzierte IDs ohne Duplikate zurück")
		void getReferencedIds_found() {
			@SuppressWarnings("unchecked")
			final TypedQuery<Long> queryMock = mock(TypedQuery.class);
			when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);
			when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
			when(queryMock.getResultList()).thenReturn(List.of(3L, 3L));

			final var result = repository.getReferencedIds(List.of(3L, 2L, 7L));

			assertThat(result).isEqualTo(Set.of(3L));
		}

		@Test
		@DisplayName("Gibt leeres Set zurück, wenn keine Referenzen gefunden")
		void getReferencedIds_noneFound() {
			@SuppressWarnings("unchecked")
			final TypedQuery<Long> queryMock = mock(TypedQuery.class);
			when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);
			when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
			when(queryMock.getResultList()).thenReturn(List.of());

			final var result = repository.getReferencedIds(List.of(1L, 2L, 7L));

			assertThat(result).isEmpty();
		}

		@Test
		@DisplayName("Query enthält alle fünf referenzierenden Tabellen")
		void getReferencedIds_queryContainsAllTables() {
			@SuppressWarnings("unchecked")
			final TypedQuery<Long> queryMock = mock(TypedQuery.class);
			when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
			when(queryMock.getResultList()).thenReturn(List.of());

			when(conn.query(anyString(), eq(Long.class))).thenAnswer(invocation -> {
				final String query = invocation.getArgument(0);
				assertThat(query)
						.contains("Entlassjahrgang_ID")
						.contains("DTOSchuelerLernabschnittsdaten")
						.contains("DTOKlassen")
						.contains("DTOStundenplanSchienen")
						.contains("DTOKurs")
						.contains("UNION ALL");
				return queryMock;
			});

			repository.getReferencedIds(List.of(1L));

			verify(conn, times(1)).query(anyString(), eq(Long.class));
		}
	}
}
