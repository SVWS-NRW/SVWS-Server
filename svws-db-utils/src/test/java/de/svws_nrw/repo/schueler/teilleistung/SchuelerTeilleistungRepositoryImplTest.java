package de.svws_nrw.repo.schueler.teilleistung;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerTeilleistung;

@ExtendWith(MockitoExtension.class)
class SchuelerTeilleistungRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private SchuelerTeilleistungRepositoryImpl repository;

	@Test
	@DisplayName("Test: Erstelle einen neuen Eintrag und prüfe die Zuweisung der neuen ID.")
	void testCreate() {
		final long idAlt = -1L;
		final long idLeistung = 4711L;
		final DTOSchuelerTeilleistung neu = new DTOSchuelerTeilleistung(idAlt, idLeistung);

		final long idNeu = 42L;

		when(conn.transactionGetNextID(DTOSchuelerTeilleistung.class)).thenReturn(idNeu);
		when(conn.transactionPersist(neu)).thenReturn(true);

		final DTOSchuelerTeilleistung result = repository.create(neu);

		assertNotNull(result);
		assertEquals(idNeu, result.ID, "Die ID wurde nicht korrekt gesetzt.");
		verify(conn).transactionGetNextID(DTOSchuelerTeilleistung.class);
		verify(conn).transactionPersist(neu);
	}

	@Test
	@DisplayName("Test: Der Konstruktor setzt die ID-Zugriffe (getId/setId) korrekt auf das ID-Feld.")
	void testConstructorAndIdAccess() {
		// Teste getId indirekt via getMap()
		final DTOSchuelerTeilleistung k = new DTOSchuelerTeilleistung(999L, 24L);
		when(conn.queryAll(DTOSchuelerTeilleistung.class)).thenReturn(List.of(k));
		assertThat(repository.getMap()).containsEntry(999L, k);

		// Teste setId über die create-Methode
		when(conn.transactionGetNextID(DTOSchuelerTeilleistung.class)).thenReturn(1000L);
		when(conn.transactionPersist(k)).thenReturn(true);
		repository.create(k);
		assertThat(k.ID).isEqualTo(1000L);
	}


	@Test
	@DisplayName("Test: findListByLeistungsdaten() ermittelt Teilleistungen für eine Liste von Leistungs-IDs.")
	void testFindListByLeistungsdaten() {
		// Fall: Es wird eine leere Liste von Leistungsdaten-IDs übergeben
		List<DTOSchuelerTeilleistung> result = repository.findListByLeistungsdaten(Collections.emptyList());
		assertThat(result).isEmpty();
		verifyNoInteractions(conn);

		// Fall: Es wird eine Liste von Leistungs-IDs übergeben und eine Telleistung wird gefunden.
		final Collection<Long> leistungIds = List.of(100L, 101L);
		final DTOSchuelerTeilleistung tl = new DTOSchuelerTeilleistung(101L, 1L);

		when(conn.queryList(DTOSchuelerTeilleistung.QUERY_LIST_BY_LEISTUNG_ID, DTOSchuelerTeilleistung.class, leistungIds))
			.thenReturn(List.of(tl));

		result = repository.findListByLeistungsdaten(leistungIds);

		assertThat(result).as("Die Liste sollte die Teilleistung enthalten").containsExactly(tl);

		verify(conn).queryList(DTOSchuelerTeilleistung.QUERY_LIST_BY_LEISTUNG_ID, DTOSchuelerTeilleistung.class, leistungIds);
	}

}
