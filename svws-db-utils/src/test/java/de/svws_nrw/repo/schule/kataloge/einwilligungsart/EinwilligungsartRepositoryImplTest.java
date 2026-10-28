package de.svws_nrw.repo.schule.kataloge.einwilligungsart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
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

import de.svws_nrw.core.types.schule.PersonTyp;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOEinwilligungsart;
import jakarta.persistence.TypedQuery;

/**
 * Tests für die Klasse {@link EinwilligungsartRepositoryImpl}.
 */
@ExtendWith(MockitoExtension.class)
class EinwilligungsartRepositoryImplTest {

	private static final String QUERY_IDS_BY_PERSONTYP =
			"SELECT e.id FROM DTOEinwilligungsart e WHERE e.personTyp = ?1 ORDER BY e.id";
	private static final String QUERY_BEZEICHNUNG_CREATE =
			"SELECT e FROM DTOEinwilligungsart e WHERE LOWER(e.bezeichnung) = LOWER(?1) AND e.personTyp = ?2";
	private static final String QUERY_BEZEICHNUNG_PATCH =
			"SELECT e FROM DTOEinwilligungsart e WHERE LOWER(e.bezeichnung) = LOWER(?1) AND e.personTyp = ?2 AND e.id != ?3";
	private static final String QUERY_SCHLUESSEL_CREATE =
			"SELECT e FROM DTOEinwilligungsart e WHERE LOWER(e.schluessel) = LOWER(?1) AND e.personTyp = ?2";
	private static final String QUERY_SCHLUESSEL_PATCH =
			"SELECT e FROM DTOEinwilligungsart e WHERE LOWER(e.schluessel) = LOWER(?1) AND e.personTyp = ?2 AND e.id != ?3";
	private static final String QUERY_EXISTS = "SELECT e FROM DTOEinwilligungsart e WHERE e.id = ?1 AND e.personTyp = ?2";

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private EinwilligungsartRepositoryImpl repository;

	@Test
	@DisplayName("Test: Prüfe, ob getAllIdsByPersonTyp die IDs aufsteigend sortiert liefert.")
	void testGetAllIdsByPersonTyp() {
		when(conn.queryList(QUERY_IDS_BY_PERSONTYP, Long.class, PersonTyp.SCHUELER)).thenReturn(List.of(2L, 5L, 7L));

		assertThat(repository.getAllIdsByPersonTyp(PersonTyp.SCHUELER)).containsExactly(2L, 5L, 7L);
	}

	@Test
	@DisplayName("Test: Prüfe, ob getAllIdsByPersonTyp eine leere Liste liefert, wenn keine Einwilligungsarten existieren.")
	void testGetAllIdsByPersonTypEmpty() {
		when(conn.queryList(QUERY_IDS_BY_PERSONTYP, Long.class, PersonTyp.LEHRER)).thenReturn(List.of());

		assertThat(repository.getAllIdsByPersonTyp(PersonTyp.LEHRER)).isEmpty();
	}

	@Test
	@DisplayName("Test: Prüfe, ob existsByIdAndPersonTyp true liefert, wenn die Einwilligungsart für den Personentyp existiert.")
	void testExistsByIdAndPersonTypFound() {
		when(conn.existsBy(QUERY_EXISTS, DTOEinwilligungsart.class, 3L, PersonTyp.SCHUELER)).thenReturn(true);

		assertThat(repository.existsByIdAndPersonTyp(3L, PersonTyp.SCHUELER)).isTrue();
	}

	@Test
	@DisplayName("Test: Prüfe, ob existsByIdAndPersonTyp false liefert, wenn die Einwilligungsart nicht existiert.")
	void testExistsByIdAndPersonTypNotFound() {
		when(conn.existsBy(QUERY_EXISTS, DTOEinwilligungsart.class, 3L, PersonTyp.SCHUELER)).thenReturn(false);

		assertThat(repository.existsByIdAndPersonTyp(3L, PersonTyp.SCHUELER)).isFalse();
	}

	@Test
	@DisplayName("Test: Prüfe, ob bezeichnungIsAlreadyUsedCreate die Bezeichnung case-insensitiv je Personentyp über die Datenbank-Verbindung prüft.")
	void testBezeichnungIsAlreadyUsedCreate() {
		when(conn.existsBy(QUERY_BEZEICHNUNG_CREATE, DTOEinwilligungsart.class, "Foto", PersonTyp.SCHUELER)).thenReturn(true);
		when(conn.existsBy(QUERY_BEZEICHNUNG_CREATE, DTOEinwilligungsart.class, "Foto", PersonTyp.LEHRER)).thenReturn(false);

		assertThat(repository.bezeichnungIsAlreadyUsedCreate("Foto", PersonTyp.SCHUELER)).isTrue();
		assertThat(repository.bezeichnungIsAlreadyUsedCreate("Foto", PersonTyp.LEHRER)).isFalse();
	}

	@Test
	@DisplayName("Test: Prüfe, ob bezeichnungIsAlreadyUsedPatch die eigene Einwilligungsart von der Prüfung ausschließt.")
	void testBezeichnungIsAlreadyUsedPatch() {
		when(conn.existsBy(QUERY_BEZEICHNUNG_PATCH, DTOEinwilligungsart.class, "Foto", PersonTyp.SCHUELER, 3L)).thenReturn(true);
		when(conn.existsBy(QUERY_BEZEICHNUNG_PATCH, DTOEinwilligungsart.class, "Foto", PersonTyp.SCHUELER, 4L)).thenReturn(false);

		assertThat(repository.bezeichnungIsAlreadyUsedPatch("Foto", PersonTyp.SCHUELER, 3L)).isTrue();
		assertThat(repository.bezeichnungIsAlreadyUsedPatch("Foto", PersonTyp.SCHUELER, 4L)).isFalse();
	}

	@Test
	@DisplayName("Test: Prüfe, ob schluesselIsAlreadyUsedCreate den Schlüssel case-insensitiv je Personentyp über die Datenbank-Verbindung prüft.")
	void testSchluesselIsAlreadyUsedCreate() {
		when(conn.existsBy(QUERY_SCHLUESSEL_CREATE, DTOEinwilligungsart.class, "FOTO", PersonTyp.SCHUELER)).thenReturn(true);
		when(conn.existsBy(QUERY_SCHLUESSEL_CREATE, DTOEinwilligungsart.class, "FOTO", PersonTyp.LEHRER)).thenReturn(false);

		assertThat(repository.schluesselIsAlreadyUsedCreate("FOTO", PersonTyp.SCHUELER)).isTrue();
		assertThat(repository.schluesselIsAlreadyUsedCreate("FOTO", PersonTyp.LEHRER)).isFalse();
	}

	@Test
	@DisplayName("Test: Prüfe, ob schluesselIsAlreadyUsedPatch die eigene Einwilligungsart von der Prüfung ausschließt.")
	void testSchluesselIsAlreadyUsedPatch() {
		when(conn.existsBy(QUERY_SCHLUESSEL_PATCH, DTOEinwilligungsart.class, "FOTO", PersonTyp.LEHRER, 3L)).thenReturn(true);
		when(conn.existsBy(QUERY_SCHLUESSEL_PATCH, DTOEinwilligungsart.class, "FOTO", PersonTyp.LEHRER, 4L)).thenReturn(false);

		assertThat(repository.schluesselIsAlreadyUsedPatch("FOTO", PersonTyp.LEHRER, 3L)).isTrue();
		assertThat(repository.schluesselIsAlreadyUsedPatch("FOTO", PersonTyp.LEHRER, 4L)).isFalse();
	}

	@Test
	@DisplayName("Test: Prüfe, ob getReferencedIds bei null oder einer leeren Liste keine Abfrage ausführt.")
	void testGetReferencedIdsEmptyInput() {
		assertThat(repository.getReferencedIds(null)).isEmpty();
		assertThat(repository.getReferencedIds(List.of())).isEmpty();

		verify(conn, never()).query(anyString(), eq(Long.class));
	}

	@Test
	@DisplayName("Test: Prüfe, ob getReferencedIds die Referenzen aus den Einwilligungen von Schülern, Lehrern und Erziehern vereinigt.")
	void testGetReferencedIds() {
		stubQuery("DTOSchuelerDatenschutz", List.of(1L));
		stubQuery("DTOLehrerDatenschutz", List.of(1L, 2L));
		stubQuery("DTOErzieherDatenschutz", List.of(3L));

		assertThat(repository.getReferencedIds(List.of(1L, 2L, 3L, 4L))).isEqualTo(Set.of(1L, 2L, 3L));
	}

	@SuppressWarnings("unchecked")
	private void stubQuery(final String dtoName, final List<Long> result) {
		final TypedQuery<Long> query = mock(TypedQuery.class);
		when(conn.query(contains(dtoName), eq(Long.class))).thenReturn(query);
		when(query.setParameter(eq("ids"), any())).thenReturn(query);
		when(query.getResultList()).thenReturn(result);
	}

}
