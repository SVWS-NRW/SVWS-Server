package de.svws_nrw.repo.schueler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;

/**
 * Testklasse für das SchuelerRepositoryImpl zur Validierung der
 * schülerspezifischen Abfragen.
 */
@ExtendWith(MockitoExtension.class)
class SchuelerRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private SchuelerRepositoryImpl repository;

	private final DTOSchueler testSchueler1 = getTestSchueler1();

	private static DTOSchueler getTestSchueler1() {
		final var testSchueler = new DTOSchueler(1L, "Fake-GU-ID 1", false);
		testSchueler.Vorname = "Vorname 1";
		testSchueler.Nachname = "Nachname 1";
		testSchueler.Schuljahresabschnitts_ID = 42L;
		testSchueler.idStatus = 2;
		return testSchueler;
	}

	private final DTOSchueler testSchueler2 = getTestSchueler2();

	private static DTOSchueler getTestSchueler2() {
		final var testSchueler = new DTOSchueler(2L, "Fake-GU-ID 2", false);
		testSchueler.Vorname = "Vorname 2";
		testSchueler.Nachname = "Nachname 2";
		testSchueler.Schuljahresabschnitts_ID = 42L;
		testSchueler.idStatus = 2;
		return testSchueler;
	}

	/**
	 * Initialisiere die Core-Types, u.a. den für den Schüler-Status
	 */
	@BeforeAll
	static void init() {
		ASDCoreTypeUtils.initAll();
	}


	@Test
	@DisplayName("Test: getListAktiveBySchuljahresabschnitt liest die Schüler aus der Datenbank")
	void testGetListAktiveBySchuljahresabschnitt() {
		// Die erwartetet Datenbank-Anfrage und ihre Parameter
		final String query = "SELECT e FROM DTOSchueler e WHERE e.Schuljahresabschnitts_ID = ?1 AND e.idStatus = ?2 AND e.Geloescht = ?3";
		final long idAbschnitt = 42L;
		final long idStatus = SchuelerStatus.AKTIV.historie().getLast().id;

		when(conn.queryList(query, DTOSchueler.class, idAbschnitt, idStatus, false)).thenReturn(Arrays.asList(testSchueler1, testSchueler2));
		final List<DTOSchueler> result = repository.getListAktiveBySchuljahresabschnitt(idAbschnitt);

		// Prüfe, ob die Liste die beiden Test-Schüler beinhaltet
		assertNotNull(result);
		assertEquals(2, result.size());
		verify(conn).queryList(query, DTOSchueler.class, idAbschnitt, idStatus, false);
	}

	@Test
	@DisplayName("Test: getMapAktiveBySchuljahresabschnitt liefert eine Map der aktiven Schüler des Schuljahresabschnittes anhand ihrer ID.")
	void testGetMapAktiveBySchuljahresabschnitt() {
		// Die erwartetet Datenbank-Anfrage und ihre Parameter
		final String query = "SELECT e FROM DTOSchueler e WHERE e.Schuljahresabschnitts_ID = ?1 AND e.idStatus = ?2 AND e.Geloescht = ?3";
		final long idAbschnitt = 42L;
		final long idStatus = SchuelerStatus.AKTIV.historie().getLast().id;

		when(conn.queryList(query, DTOSchueler.class, idAbschnitt, idStatus, false)).thenReturn(List.of(testSchueler1, testSchueler2));
		final Map<Long, DTOSchueler> result = repository.getMapAktiveBySchuljahresabschnitt(idAbschnitt);

		// Prüfe, ob die Map die beiden Test-Schüler beinhaltet
		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals(testSchueler1, result.get(1L));
		assertEquals(testSchueler2, result.get(2L));
		verify(conn).queryList(query, DTOSchueler.class, idAbschnitt, idStatus, false);
	}


	@Test
	@DisplayName("Test: Prüfe, ob die ID korrekt gesetzt wird.")
	void testCreate() {
		final DTOSchueler neu = new DTOSchueler(1L, "Fake-GU-ID 1", false);
		final long neueId = 999L;

		when(conn.transactionGetNextID(DTOSchueler.class)).thenReturn(neueId);
		when(conn.transactionPersist(neu)).thenReturn(true);

		final DTOSchueler result = repository.create(neu);

		assertEquals(neueId, result.ID);
		verify(conn).transactionGetNextID(DTOSchueler.class);
		verify(conn).transactionPersist(neu);
	}

}
