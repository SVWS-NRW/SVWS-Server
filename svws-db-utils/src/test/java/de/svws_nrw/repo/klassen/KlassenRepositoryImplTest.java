package de.svws_nrw.repo.klassen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static de.svws_nrw.repo.klassen.KlassenRepositoryImpl.QUERY_BY_ASD_KUERZEL_AND_SCHULJAHRESABSCHNITT;
import static de.svws_nrw.repo.klassen.KlassenRepositoryImpl.QUERY_BY_KUERZEL_AND_SCHULJAHRESABSCHNITT;
import static de.svws_nrw.repo.klassen.KlassenRepositoryImpl.QUERY_BY_KUERZEL_AND_SCHULJAHRESABSCHNITT_EXCLUDING_ID;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;

/**
 * Test für die Klasse {@link KlassenRepositoryImpl}
 */
@ExtendWith(MockitoExtension.class)
class KlassenRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private KlassenRepositoryImpl repository;


	@Test
	@DisplayName("Test: getListBySchuljahresabschnitt() liefert die Liste der Klassen.")
	void testGetListBySchuljahresabschnitt() {
		final long idSchuljahresabschnitt = 123L;
		final DTOKlassen klasse1 = new DTOKlassen(1L, idSchuljahresabschnitt, "07a");
		final List<DTOKlassen> expectedList = List.of(klasse1);

		when(conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, idSchuljahresabschnitt))
				.thenReturn(expectedList);

		final List<DTOKlassen> result = repository.getListBySchuljahresabschnitt(idSchuljahresabschnitt);

		assertThat(result).containsExactly(klasse1);
		verify(conn).queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, idSchuljahresabschnitt);
	}

	@Test
	@DisplayName("Test: getMapBySchuljahresabschnitt() liefert die Klassen und erzeugt daraus eine Map.")
	void testGetMapBySchuljahresabschnitt() {
		final long idSchuljahresabschnitt = 456L;
		final DTOKlassen klasse1 = new DTOKlassen(1L, idSchuljahresabschnitt, "07a");
		final DTOKlassen klasse2 = new DTOKlassen(2L, idSchuljahresabschnitt, "07b");

		when(conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, idSchuljahresabschnitt))
				.thenReturn(List.of(klasse1, klasse2));

		final Map<Long, DTOKlassen> resultMap = repository.getMapBySchuljahresabschnitt(idSchuljahresabschnitt);

		assertThat(resultMap)
				.hasSize(2)
				.containsOnly(
						entry(1L, klasse1),
						entry(2L, klasse2)
				);
	}

	@Test
	@DisplayName("Test: Der Konstruktor setzt die ID-Zugriffe (getId/setId) korrekt auf das ID-Feld von DTOKlassen.")
	void testConstructorAndIdAccess() {
		// Teste getId indirekt via getMap()
		final DTOKlassen k = new DTOKlassen(999L, 1L, "08a");
		when(conn.queryAll(DTOKlassen.class)).thenReturn(List.of(k));
		assertThat(repository.getMap()).containsEntry(999L, k);

		// Teste setId über die create-Methode
		when(conn.transactionGetNextID(DTOKlassen.class)).thenReturn(1000L);
		when(conn.transactionPersist(k)).thenReturn(true);
		repository.create(k);
		assertThat(k.ID).isEqualTo(1000L);
	}


	@Nested
	@DisplayName("findListByIds() (geerbt aus RepositoryImpl)")
	class FindListByIds {

		@Test
		@DisplayName("Treffer: Es werden nur die vorhandenen Klassen geliefert, fehlende IDs werden nicht geliefert.")
		void testTreffer() {
			final List<Long> ids = List.of(1L, 2L, 99L);
			final DTOKlassen klasse1 = new DTOKlassen(1L, 10L, "07a");
			final DTOKlassen klasse2 = new DTOKlassen(2L, 10L, "07b");
			when(conn.queryByKeyList(DTOKlassen.class, ids)).thenReturn(List.of(klasse1, klasse2));

			assertThat(repository.findListByIds(ids)).containsExactly(klasse1, klasse2);
		}

		@Test
		@DisplayName("Kein Treffer: Es wird eine leere Liste geliefert.")
		void testKeinTreffer() {
			final List<Long> ids = List.of(99L);
			when(conn.queryByKeyList(DTOKlassen.class, ids)).thenReturn(Collections.emptyList());

			assertThat(repository.findListByIds(ids)).isEmpty();
		}

		@Test
		@DisplayName("Leere ID-Liste: Es wird eine leere Liste geliefert, ohne die Datenbank abzufragen.")
		void testLeereIdListe() {
			assertThat(repository.findListByIds(Collections.emptyList())).isEmpty();
			verifyNoInteractions(conn);
		}

	}


	@Nested
	@DisplayName("findByKuerzelAndSchuljahresabschnitt()")
	class FindByKuerzelAndSchuljahresabschnitt {

		@Test
		@DisplayName("Treffer: Die Klasse wird geliefert.")
		void testTreffer() {
			final DTOKlassen klasse = new DTOKlassen(1L, 10L, "07a");
			when(conn.queryList(QUERY_BY_KUERZEL_AND_SCHULJAHRESABSCHNITT, DTOKlassen.class, "07a", 10L)).thenReturn(List.of(klasse));

			assertThat(repository.findByKuerzelAndSchuljahresabschnitt("07a", 10L)).containsSame(klasse);
		}

		@Test
		@DisplayName("Kein Treffer: Es wird ein leeres Optional geliefert.")
		void testKeinTreffer() {
			when(conn.queryList(QUERY_BY_KUERZEL_AND_SCHULJAHRESABSCHNITT, DTOKlassen.class, "07x", 10L)).thenReturn(Collections.emptyList());

			assertThat(repository.findByKuerzelAndSchuljahresabschnitt("07x", 10L)).isEmpty();
		}

		@Test
		@DisplayName("Kürzel in anderem Abschnitt: Die Klasse des anderen Abschnitts wird nicht geliefert.")
		void testKuerzelInAnderemAbschnitt() {
			when(conn.queryList(QUERY_BY_KUERZEL_AND_SCHULJAHRESABSCHNITT, DTOKlassen.class, "07a", 11L)).thenReturn(Collections.emptyList());

			assertThat(repository.findByKuerzelAndSchuljahresabschnitt("07a", 11L)).isEmpty();
			verify(conn).queryList(QUERY_BY_KUERZEL_AND_SCHULJAHRESABSCHNITT, DTOKlassen.class, "07a", 11L);
		}

		@Test
		@DisplayName("Kürzel null: Es wird ein leeres Optional geliefert, ohne die Datenbank abzufragen.")
		void testKuerzelNull() {
			assertThat(repository.findByKuerzelAndSchuljahresabschnitt(null, 10L)).isEmpty();
			verifyNoInteractions(conn);
		}

	}


	@Nested
	@DisplayName("findByKuerzelOrASDKuerzelAndSchuljahresabschnitt()")
	class FindByKuerzelOrASDKuerzelAndSchuljahresabschnitt {

		@Test
		@DisplayName("Treffer über das Kürzel: Das ASD-Kürzel wird nicht mehr abgefragt.")
		void testTrefferUeberKuerzel() {
			final DTOKlassen klasse = new DTOKlassen(1L, 10L, "07a");
			when(conn.queryList(QUERY_BY_KUERZEL_AND_SCHULJAHRESABSCHNITT, DTOKlassen.class, "07a", 10L)).thenReturn(List.of(klasse));

			assertThat(repository.findByKuerzelOrASDKuerzelAndSchuljahresabschnitt("07a", "07A", 10L)).containsSame(klasse);
			verify(conn, never()).queryList(QUERY_BY_ASD_KUERZEL_AND_SCHULJAHRESABSCHNITT, DTOKlassen.class, "07A", 10L);
		}

		@Test
		@DisplayName("Kein Treffer über das Kürzel: Die Klasse wird über das ASD-Kürzel geliefert.")
		void testTrefferUeberASDKuerzel() {
			final DTOKlassen klasse = new DTOKlassen(1L, 10L, "07a");
			when(conn.queryList(QUERY_BY_KUERZEL_AND_SCHULJAHRESABSCHNITT, DTOKlassen.class, "07x", 10L)).thenReturn(Collections.emptyList());
			when(conn.queryList(QUERY_BY_ASD_KUERZEL_AND_SCHULJAHRESABSCHNITT, DTOKlassen.class, "07A", 10L)).thenReturn(List.of(klasse));

			assertThat(repository.findByKuerzelOrASDKuerzelAndSchuljahresabschnitt("07x", "07A", 10L)).containsSame(klasse);
		}

		@Test
		@DisplayName("Kürzel null: Die Klasse wird über das ASD-Kürzel geliefert, ohne nach dem Kürzel zu suchen.")
		void testKuerzelNull() {
			final DTOKlassen klasse = new DTOKlassen(1L, 10L, "07a");
			when(conn.queryList(QUERY_BY_ASD_KUERZEL_AND_SCHULJAHRESABSCHNITT, DTOKlassen.class, "07A", 10L)).thenReturn(List.of(klasse));

			assertThat(repository.findByKuerzelOrASDKuerzelAndSchuljahresabschnitt(null, "07A", 10L)).containsSame(klasse);
			verify(conn, never()).queryList(QUERY_BY_KUERZEL_AND_SCHULJAHRESABSCHNITT, DTOKlassen.class, null, 10L);
		}

		@Test
		@DisplayName("Kein Treffer über das Kürzel und ASD-Kürzel null: Es wird ein leeres Optional geliefert.")
		void testKeinTrefferOhneASDKuerzel() {
			when(conn.queryList(QUERY_BY_KUERZEL_AND_SCHULJAHRESABSCHNITT, DTOKlassen.class, "07x", 10L)).thenReturn(Collections.emptyList());

			assertThat(repository.findByKuerzelOrASDKuerzelAndSchuljahresabschnitt("07x", null, 10L)).isEmpty();
			verify(conn, never()).queryList(QUERY_BY_ASD_KUERZEL_AND_SCHULJAHRESABSCHNITT, DTOKlassen.class, null, 10L);
		}

		@Test
		@DisplayName("Kein Treffer über Kürzel und ASD-Kürzel: Es wird ein leeres Optional geliefert.")
		void testKeinTreffer() {
			when(conn.queryList(QUERY_BY_KUERZEL_AND_SCHULJAHRESABSCHNITT, DTOKlassen.class, "07x", 10L)).thenReturn(Collections.emptyList());
			when(conn.queryList(QUERY_BY_ASD_KUERZEL_AND_SCHULJAHRESABSCHNITT, DTOKlassen.class, "07X", 10L)).thenReturn(Collections.emptyList());

			assertThat(repository.findByKuerzelOrASDKuerzelAndSchuljahresabschnitt("07x", "07X", 10L)).isEmpty();
		}

		@Test
		@DisplayName("Kürzel und ASD-Kürzel null: Es wird ein leeres Optional geliefert, ohne die Datenbank abzufragen.")
		void testBeideNull() {
			assertThat(repository.findByKuerzelOrASDKuerzelAndSchuljahresabschnitt(null, null, 10L)).isEmpty();
			verifyNoInteractions(conn);
		}

		@Test
		@DisplayName("Mehrere Treffer: Es wird wie im Bestand der erste Treffer geliefert.")
		void testMehrereTreffer() {
			final DTOKlassen erste = new DTOKlassen(1L, 10L, "07a");
			final DTOKlassen zweite = new DTOKlassen(2L, 10L, "07a");
			when(conn.queryList(QUERY_BY_KUERZEL_AND_SCHULJAHRESABSCHNITT, DTOKlassen.class, "07a", 10L)).thenReturn(List.of(erste, zweite));

			assertThat(repository.findByKuerzelOrASDKuerzelAndSchuljahresabschnitt("07a", null, 10L)).containsSame(erste);
		}

	}


	@Nested
	@DisplayName("kuerzelIsAlreadyUsedCreate()")
	class KuerzelIsAlreadyUsedCreate {

		@Test
		@DisplayName("Treffer: Das Kürzel ist im Abschnitt bereits vergeben.")
		void testTreffer() {
			when(conn.existsBy(QUERY_BY_KUERZEL_AND_SCHULJAHRESABSCHNITT, DTOKlassen.class, "07a", 10L)).thenReturn(true);

			assertThat(repository.kuerzelIsAlreadyUsedCreate("07a", 10L)).isTrue();
		}

		@Test
		@DisplayName("Kein Treffer: Das Kürzel ist im Abschnitt nicht vergeben.")
		void testKeinTreffer() {
			when(conn.existsBy(QUERY_BY_KUERZEL_AND_SCHULJAHRESABSCHNITT, DTOKlassen.class, "07x", 10L)).thenReturn(false);

			assertThat(repository.kuerzelIsAlreadyUsedCreate("07x", 10L)).isFalse();
		}

		@Test
		@DisplayName("Kürzel in anderem Abschnitt: Es wird nur im angegebenen Abschnitt gesucht.")
		void testKuerzelInAnderemAbschnitt() {
			assertThat(repository.kuerzelIsAlreadyUsedCreate("07a", 11L)).isFalse();
			verify(conn).existsBy(QUERY_BY_KUERZEL_AND_SCHULJAHRESABSCHNITT, DTOKlassen.class, "07a", 11L);
		}

	}


	@Nested
	@DisplayName("kuerzelIsAlreadyUsedPatch()")
	class KuerzelIsAlreadyUsedPatch {

		@Test
		@DisplayName("Treffer: Eine andere Klasse hat das Kürzel im Abschnitt bereits.")
		void testTreffer() {
			when(conn.existsBy(QUERY_BY_KUERZEL_AND_SCHULJAHRESABSCHNITT_EXCLUDING_ID, DTOKlassen.class, "07a", 10L, 5L)).thenReturn(true);

			assertThat(repository.kuerzelIsAlreadyUsedPatch("07a", 10L, 5L)).isTrue();
		}

		@Test
		@DisplayName("Kein Treffer: Keine andere Klasse hat das Kürzel im Abschnitt.")
		void testKeinTreffer() {
			when(conn.existsBy(QUERY_BY_KUERZEL_AND_SCHULJAHRESABSCHNITT_EXCLUDING_ID, DTOKlassen.class, "07x", 10L, 5L)).thenReturn(false);

			assertThat(repository.kuerzelIsAlreadyUsedPatch("07x", 10L, 5L)).isFalse();
		}

		@Test
		@DisplayName("Kürzel in anderem Abschnitt: Es wird nur im angegebenen Abschnitt gesucht.")
		void testKuerzelInAnderemAbschnitt() {
			assertThat(repository.kuerzelIsAlreadyUsedPatch("07a", 11L, 5L)).isFalse();
			verify(conn).existsBy(QUERY_BY_KUERZEL_AND_SCHULJAHRESABSCHNITT_EXCLUDING_ID, DTOKlassen.class, "07a", 11L, 5L);
		}

		@Test
		@DisplayName("Eigene Klasse: Die ID der eigenen Klasse wird von der Prüfung ausgeschlossen.")
		void testEigeneKlasse() {
			assertThat(repository.kuerzelIsAlreadyUsedPatch("07a", 10L, 5L)).isFalse();

			verify(conn).existsBy(QUERY_BY_KUERZEL_AND_SCHULJAHRESABSCHNITT_EXCLUDING_ID, DTOKlassen.class, "07a", 10L, 5L);
			assertThat(QUERY_BY_KUERZEL_AND_SCHULJAHRESABSCHNITT_EXCLUDING_ID).contains("e.ID <> ?3");
		}

	}

}
