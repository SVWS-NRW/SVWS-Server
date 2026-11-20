package de.svws_nrw.repo.schueler.kaoa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerKAoADaten;

/**
 * Tests für die Klasse {@link SchuelerKAoARepositoryImpl}.
 */
@ExtendWith(MockitoExtension.class)
class SchuelerKAoARepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private SchuelerKAoARepositoryImpl repository;

	@Test
	@DisplayName("findListByLernabschnitte | Bestimmt die KAoA-Daten über QUERY_LIST_BY_IDLERNABSCHNITT")
	void findListByLernabschnitte_fragtDatenbankAb() {
		final var ids = Set.of(7L, 8L);
		final var entities = List.of(new DTOSchuelerKAoADaten(1L, 7L, 10L), new DTOSchuelerKAoADaten(2L, 8L, 10L));
		when(conn.queryList(DTOSchuelerKAoADaten.QUERY_LIST_BY_IDLERNABSCHNITT, DTOSchuelerKAoADaten.class, ids)).thenReturn(entities);

		assertThat(repository.findListByLernabschnitte(ids)).containsExactlyElementsOf(entities);
	}

	@Test
	@DisplayName("findListByLernabschnitte | Eine leere ID-Liste liefert eine leere Liste ohne Datenbankzugriff")
	void findListByLernabschnitte_leereEingabe() {
		assertThat(repository.findListByLernabschnitte(Set.of())).isEmpty();
		verifyNoInteractions(conn);
	}

}
