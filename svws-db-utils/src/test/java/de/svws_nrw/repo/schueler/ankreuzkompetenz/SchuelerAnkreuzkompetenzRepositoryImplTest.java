package de.svws_nrw.repo.schueler.ankreuzkompetenz;

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
import de.svws_nrw.db.dto.current.schild.grundschule.DTOSchuelerAnkreuzfloskeln;

@ExtendWith(MockitoExtension.class)
class SchuelerAnkreuzkompetenzRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private SchuelerAnkreuzkompetenzRepositoryImpl repository;

	@Test
	@DisplayName("Test: Erstelle einen neuen Eintrag und prüfe die Zuweisung der neuen ID.")
	void testCreate() {
		final long idAlt = -1L;
		final long idAbschnitt = 4711L;
		final long idFloskel = 77L;
		final DTOSchuelerAnkreuzfloskeln neu = new DTOSchuelerAnkreuzfloskeln(idAlt, idAbschnitt, idFloskel);

		final long idNeu = 42L;

		when(conn.transactionGetNextID(DTOSchuelerAnkreuzfloskeln.class)).thenReturn(idNeu);
		when(conn.transactionPersist(neu)).thenReturn(true);

		final DTOSchuelerAnkreuzfloskeln result = repository.create(neu);

		assertNotNull(result);
		assertEquals(idNeu, result.ID, "Die ID wurde nicht korrekt gesetzt.");
		verify(conn).transactionGetNextID(DTOSchuelerAnkreuzfloskeln.class);
		verify(conn).transactionPersist(neu);
	}

	@Test
	@DisplayName("Test: Der Konstruktor setzt die ID-Zugriffe (getId/setId) korrekt auf das ID-Feld.")
	void testConstructorAndIdAccess() {
		// Teste getId indirekt via getMap()
		final DTOSchuelerAnkreuzfloskeln k = new DTOSchuelerAnkreuzfloskeln(999L, 24L, 42L);
		when(conn.queryAll(DTOSchuelerAnkreuzfloskeln.class)).thenReturn(List.of(k));
		assertThat(repository.getMap()).containsEntry(999L, k);

		// Teste setId über die create-Methode
		when(conn.transactionGetNextID(DTOSchuelerAnkreuzfloskeln.class)).thenReturn(1000L);
		when(conn.transactionPersist(k)).thenReturn(true);
		repository.create(k);
		assertThat(k.ID).isEqualTo(1000L);
	}


	@Test
	@DisplayName("Test: findListByLernabschnitt() ermittelt die Ankreuzkompetenzen anhand der Lernabschnitte.")
	void testFindListByLernabschnitt() {
		// Fall: Leere Liste von Lernabschnitts-IDs
		List<DTOSchuelerAnkreuzfloskeln> result = repository.findListByLernabschnitt(Collections.emptyList());
		assertThat(result).isEmpty();
		verifyNoInteractions(conn);

		// Fall: Es gibt für die Liste der Lernabschnitts-IDs eine Ankreuzkompetenz als Rückgabe.
		final Collection<Long> abschnittIds = List.of(500L, 501L);
		final DTOSchuelerAnkreuzfloskeln floskel = new DTOSchuelerAnkreuzfloskeln(1L, 501L, 42L);

		when(conn.queryList(DTOSchuelerAnkreuzfloskeln.QUERY_LIST_BY_ABSCHNITT_ID, DTOSchuelerAnkreuzfloskeln.class, abschnittIds))
			.thenReturn(List.of(floskel));

		result = repository.findListByLernabschnitt(abschnittIds);

		assertThat(result).as("Die Liste sollte die Ankreuzkompetenz enthalten.").containsExactly(floskel);

		verify(conn).queryList(DTOSchuelerAnkreuzfloskeln.QUERY_LIST_BY_ABSCHNITT_ID, DTOSchuelerAnkreuzfloskeln.class, abschnittIds);
	}

}
