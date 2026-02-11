package de.svws_nrw.repo.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;


@ExtendWith(MockitoExtension.class)
class LehrerAbschnittsdatenRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@Mock
	private Benutzer benutzer;

	@InjectMocks
	private LehrerAbschnittsdatenRepositoryImpl repository;

	@Test
	@DisplayName("Test: Prüfe, ob getListByLehrerIdsAnsSchuljahresabschnitt die Lehrerabschnittsdaten für die Lehrer-IDs und den Schuljahresabschnitt liefert.")
	void testGetListByLehrerIdsAnsSchuljahresabschnitt() {
		// Szenario: Drei Lehrer-IDs, wobei nur für zwei Lehrer jeweils ein Abschnitt in dem Schuljahresabschnitt existiert
		final Schuljahresabschnitt sja = new Schuljahresabschnitt();
		sja.id = 10L;
		sja.schuljahr = 2026;
		sja.abschnitt = 2;
		final List<Long> idsLehrer = Arrays.asList(1L, 2L, 3L);
		final DTOLehrerAbschnittsdaten d1 = new DTOLehrerAbschnittsdaten(100L, 1L, sja.id);
		final DTOLehrerAbschnittsdaten d2 = new DTOLehrerAbschnittsdaten(101L, 2L, sja.id);

		// Mocking für die Abfrage der Schuljahresabschnitts-ID
		when(conn.getUser()).thenReturn(benutzer);
		when(benutzer.schuleGetAbschnittById(sja.id)).thenReturn(sja);

		// Mocking der Datenbank-Query
		final String query = "SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.Lehrer_ID IN ?1 AND e.Schuljahresabschnitts_ID = ?2";
		when(conn.queryList(query, DTOLehrerAbschnittsdaten.class, idsLehrer, sja.id)).thenReturn(Arrays.asList(d1, d2));

		// Aufruf der Methode
		final List<DTOLehrerAbschnittsdaten> result = repository.getListByLehrerIdsAndSchuljahresabschnitt(idsLehrer, sja.id);

		// Prüfen, ob die zwei Abschnittsdaten gefunden wurden
		assertNotNull(result);
		assertEquals(2, result.size());
		verify(conn).queryList(query, DTOLehrerAbschnittsdaten.class, idsLehrer, sja.id);
	}

	@Test
	@DisplayName("Test: Prüfe, ob getListByLehrerIdsAnsSchuljahresabschnitt bei ungültigem Abschnitt eine leere Liste ohne Datenbank-Abfrage liefert.")
	void testGetListByLehrerIdsWithInvalidAbschnitt() {
		final long idAbschnitt = 999L;
		when(conn.getUser()).thenReturn(benutzer);
		when(benutzer.schuleGetAbschnittById(idAbschnitt)).thenReturn(null);

		final List<DTOLehrerAbschnittsdaten> result = repository.getListByLehrerIdsAndSchuljahresabschnitt(List.of(1L), idAbschnitt);

		assertTrue(result.isEmpty());
		verify(conn, never()).queryList(anyString(), any(), any(), anyLong()); // Sicherstellen, dass keine Datenbank-Abfrage ausgeführt wurde
	}

	@Test
	@DisplayName("Test: Prüfe, ob getListByLehrerIdsAnsSchuljahresabschnitt bei leeren Lehrer-IDs oder null ohne Datenbank-Abfrage eine leere Liste liefert.")
	void testGetListByLehrerIdsEmpty() {
		// Fall der leeren Liste
		List<DTOLehrerAbschnittsdaten> result = repository.getListByLehrerIdsAndSchuljahresabschnitt(Collections.emptyList(), 10L);
		assertTrue(result.isEmpty());
		verifyNoInteractions(benutzer);
		verify(conn, never()).queryList(anyString(), any(), any(), anyLong()); // Sicherstellen, dass keine Datenbank-Abfrage ausgeführt wurde

		// Fall null für die Liste der IDs
		result = repository.getListByLehrerIdsAndSchuljahresabschnitt(null, 10L);
		assertTrue(result.isEmpty());
		verifyNoInteractions(benutzer);
		verify(conn, never()).queryList(anyString(), any(), any(), anyLong()); // Sicherstellen, dass keine Datenbank-Abfrage ausgeführt wurde
	}

	@Test
	@DisplayName("Test: Prüfe, ob die ID korrekt gesetzt wird.")
	void testCreate() {
		final DTOLehrerAbschnittsdaten neu = new DTOLehrerAbschnittsdaten(100L, 1L, 42L);
		final long neueId = 999L;

		when(conn.transactionGetNextID(DTOLehrerAbschnittsdaten.class)).thenReturn(neueId);
		when(conn.transactionPersist(neu)).thenReturn(true);

		final DTOLehrerAbschnittsdaten result = repository.create(neu);

		assertEquals(neueId, result.ID);
		verify(conn).transactionGetNextID(DTOLehrerAbschnittsdaten.class);
		verify(conn).transactionPersist(neu);
	}

}
