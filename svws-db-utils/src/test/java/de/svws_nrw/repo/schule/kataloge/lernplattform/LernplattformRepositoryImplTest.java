package de.svws_nrw.repo.schule.kataloge.lernplattform;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.svws.auth.DTOLernplattformen;

/**
 * Tests für die Klasse {@link LernplattformRepositoryImpl}.
 */
@ExtendWith(MockitoExtension.class)
class LernplattformRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private LernplattformRepositoryImpl repository;

	@Test
	@DisplayName("Test: Prüfe, ob getAllIds die IDs aller Lernplattformen über die Datenbank-Verbindung bestimmt.")
	void testGetAllIds() {
		when(conn.queryList("SELECT e.ID FROM DTOLernplattformen e ORDER BY e.ID", Long.class)).thenReturn(List.of(1L, 2L, 5L));

		assertThat(repository.getAllIds()).containsExactly(1L, 2L, 5L);
	}

	@Test
	@DisplayName("Test: Prüfe, ob getAllIds eine leere Liste liefert, wenn keine Lernplattformen existieren.")
	void testGetAllIdsEmpty() {
		when(conn.queryList("SELECT e.ID FROM DTOLernplattformen e ORDER BY e.ID", Long.class)).thenReturn(List.of());

		assertThat(repository.getAllIds()).isEmpty();
	}

	@Test
	@DisplayName("Test: Prüfe, ob create die nächste freie ID als ID der Lernplattform setzt.")
	void testCreate() {
		final DTOLernplattformen neu = new DTOLernplattformen(0L, "Moodle");
		when(conn.transactionGetNextID(DTOLernplattformen.class)).thenReturn(7L);
		when(conn.transactionPersist(neu)).thenReturn(true);

		assertThat(repository.create(neu).ID).isEqualTo(7L);
	}

	@Test
	@DisplayName("Test: Prüfe, ob existsById true liefert, wenn die Lernplattform existiert.")
	void testExistsByIdFound() {
		when(conn.existsBy(DTOLernplattformen.QUERY_BY_ID, DTOLernplattformen.class, 7L)).thenReturn(true);

		assertThat(repository.existsById(7L)).isTrue();
	}

	@Test
	@DisplayName("Test: Prüfe, ob existsById false liefert, wenn die Lernplattform nicht existiert.")
	void testExistsByIdNotFound() {
		when(conn.existsBy(DTOLernplattformen.QUERY_BY_ID, DTOLernplattformen.class, 7L)).thenReturn(false);

		assertThat(repository.existsById(7L)).isFalse();
	}

}
