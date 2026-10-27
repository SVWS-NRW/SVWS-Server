package de.svws_nrw.repo.schule.kataloge.lernplattform;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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
import de.svws_nrw.db.dto.current.svws.auth.DTOLernplattformen;
import jakarta.persistence.TypedQuery;

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
		when(conn.queryList("SELECT e.id FROM DTOLernplattformen e ORDER BY e.id", Long.class)).thenReturn(List.of(1L, 2L, 5L));

		assertThat(repository.getAllIds()).containsExactly(1L, 2L, 5L);
	}

	@Test
	@DisplayName("Test: Prüfe, ob getAllIds eine leere Liste liefert, wenn keine Lernplattformen existieren.")
	void testGetAllIdsEmpty() {
		when(conn.queryList("SELECT e.id FROM DTOLernplattformen e ORDER BY e.id", Long.class)).thenReturn(List.of());

		assertThat(repository.getAllIds()).isEmpty();
	}

	@Test
	@DisplayName("Test: Prüfe, ob create die nächste freie ID als ID der Lernplattform setzt.")
	void testCreate() {
		final DTOLernplattformen neu = new DTOLernplattformen(0L, "Moodle");
		when(conn.transactionGetNextID(DTOLernplattformen.class)).thenReturn(7L);
		when(conn.transactionPersist(neu)).thenReturn(true);

		assertThat(repository.create(neu).id).isEqualTo(7L);
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

	@Test
	@DisplayName("Test: Prüfe, ob bezeichnungIsAlreadyUsedCreate die Bezeichnung case-insensitiv über die Datenbank-Verbindung prüft.")
	void testBezeichnungIsAlreadyUsedCreate() {
		final String query = "SELECT l FROM DTOLernplattformen l WHERE LOWER(l.bezeichnung) = LOWER(?1)";
		when(conn.existsBy(query, DTOLernplattformen.class, "Moodle")).thenReturn(true);
		when(conn.existsBy(query, DTOLernplattformen.class, "IServ")).thenReturn(false);

		assertThat(repository.bezeichnungIsAlreadyUsedCreate("Moodle")).isTrue();
		assertThat(repository.bezeichnungIsAlreadyUsedCreate("IServ")).isFalse();
	}

	@Test
	@DisplayName("Test: Prüfe, ob bezeichnungIsAlreadyUsedPatch die eigene Lernplattform von der Prüfung ausschließt.")
	void testBezeichnungIsAlreadyUsedPatch() {
		final String query = "SELECT l FROM DTOLernplattformen l WHERE LOWER(l.bezeichnung) = LOWER(?1) AND l.id != ?2";
		when(conn.existsBy(query, DTOLernplattformen.class, "Moodle", 3L)).thenReturn(true);
		when(conn.existsBy(query, DTOLernplattformen.class, "Moodle", 4L)).thenReturn(false);

		assertThat(repository.bezeichnungIsAlreadyUsedPatch("Moodle", 3L)).isTrue();
		assertThat(repository.bezeichnungIsAlreadyUsedPatch("Moodle", 4L)).isFalse();
	}

	@Test
	@DisplayName("Test: Prüfe, ob getReferencedIds bei null oder einer leeren Liste keine Abfrage ausführt.")
	void testGetReferencedIdsEmptyInput() {
		assertThat(repository.getReferencedIds(null)).isEmpty();
		assertThat(repository.getReferencedIds(List.of())).isEmpty();

		verify(conn, never()).query(anyString(), eq(Long.class));
	}

	@Test
	@DisplayName("Test: Prüfe, ob getReferencedIds die Referenzen aus Schülern, Lehrern, Erziehern und Zugangsdaten vereinigt.")
	@SuppressWarnings("unchecked")
	void testGetReferencedIds() {
		final TypedQuery<Long> querySchueler = mock(TypedQuery.class);
		final TypedQuery<Long> queryLehrer = mock(TypedQuery.class);
		final TypedQuery<Long> queryErzieher = mock(TypedQuery.class);
		final TypedQuery<Long> queryCredentials = mock(TypedQuery.class);
		stubQuery("DTOSchuelerLernplattform", querySchueler, List.of(1L));
		stubQuery("DTOLehrerLernplattform", queryLehrer, List.of(1L, 2L));
		stubQuery("DTOErzieherLernplattform", queryErzieher, List.of(3L));
		stubQuery("DTOCredentialsLernplattformen", queryCredentials, List.of());

		assertThat(repository.getReferencedIds(List.of(1L, 2L, 3L, 4L))).isEqualTo(Set.of(1L, 2L, 3L));
	}

	@SuppressWarnings("unchecked")
	private void stubQuery(final String dtoName, final TypedQuery<Long> query, final List<Long> result) {
		when(conn.query(org.mockito.ArgumentMatchers.contains(dtoName), eq(Long.class))).thenReturn(query);
		when(query.setParameter(eq("ids"), any())).thenReturn(query);
		when(query.getResultList()).thenReturn(result);
	}

}
