package de.svws_nrw.repo.schueler.lernabschnitt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerPSFachBemerkungen;

@ExtendWith(MockitoExtension.class)
class SchuelerLernabschnittBemerkungRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private SchuelerLernabschnittBemerkungRepositoryImpl repository;

	@Test
	@DisplayName("Test: Erstelle einen neuen Eintrag und prüfe die Zuweisung der neuen ID.")
	void testCreate() {
		final long idAlt = -1L;
		final long idAbschnitt = 4711L;
		final DTOSchuelerPSFachBemerkungen neu = new DTOSchuelerPSFachBemerkungen(idAlt, idAbschnitt);

		final long idNeu = 42L;

		when(conn.transactionGetNextID(DTOSchuelerPSFachBemerkungen.class)).thenReturn(idNeu);
		when(conn.transactionPersist(neu)).thenReturn(true);

		final DTOSchuelerPSFachBemerkungen result = repository.create(neu);

		assertNotNull(result);
		assertEquals(idNeu, result.ID, "Die ID wurde nicht korrekt gesetzt.");
		verify(conn).transactionGetNextID(DTOSchuelerPSFachBemerkungen.class);
		verify(conn).transactionPersist(neu);
	}

	@Test
	@DisplayName("Test: Der Konstruktor setzt die ID-Zugriffe (getId/setId) korrekt auf das ID-Feld.")
	void testConstructorAndIdAccess() {
		// Teste getId indirekt via getMap()
		final DTOSchuelerPSFachBemerkungen k = new DTOSchuelerPSFachBemerkungen(999L, 4711L);
		when(conn.queryAll(DTOSchuelerPSFachBemerkungen.class)).thenReturn(List.of(k));
		assertThat(repository.getMap()).containsEntry(999L, k);

		// Teste setId über die create-Methode
		when(conn.transactionGetNextID(DTOSchuelerPSFachBemerkungen.class)).thenReturn(1000L);
		when(conn.transactionPersist(k)).thenReturn(true);
		repository.create(k);
		assertThat(k.ID).isEqualTo(1000L);
	}


	@Test
	@DisplayName("Test: findMapByLernabschnittID() liefert eine Map mit den Bemerkungen zugeordnet zu deren Lernabschnitts-ID.")
	void testFindMapByLernabschnittID() {
		// Teste den Fall einer leeren Menge von IDs
		Map<Long, DTOSchuelerPSFachBemerkungen> result = repository.findMapByLernabschnittID(Collections.emptyList());
		assertThat(result).isEmpty();
		verifyNoInteractions(conn);

		// Teste den Fall einer Menge von Lernabschnitts-IDs (hier zwei)
		final long idAbschnitt1 = 1001L;
		final long idAbschnitt2 = 1002L;
		final Collection<Long> ids = List.of(idAbschnitt1, idAbschnitt2);
		final DTOSchuelerPSFachBemerkungen b1 = new DTOSchuelerPSFachBemerkungen(1L, idAbschnitt1);
		final DTOSchuelerPSFachBemerkungen b2 = new DTOSchuelerPSFachBemerkungen(2L, idAbschnitt2);

		when(conn.queryList(DTOSchuelerPSFachBemerkungen.QUERY_LIST_BY_ABSCHNITT_ID, DTOSchuelerPSFachBemerkungen.class, ids))
				.thenReturn(List.of(b1, b2));

		result = repository.findMapByLernabschnittID(ids);

		assertThat(result).hasSize(2).containsOnly(entry(idAbschnitt1, b1), entry(idAbschnitt2, b2));
		verify(conn).queryList(DTOSchuelerPSFachBemerkungen.QUERY_LIST_BY_ABSCHNITT_ID, DTOSchuelerPSFachBemerkungen.class, ids);
	}

}
