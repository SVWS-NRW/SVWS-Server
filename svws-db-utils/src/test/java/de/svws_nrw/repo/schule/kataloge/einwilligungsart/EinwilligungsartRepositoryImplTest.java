package de.svws_nrw.repo.schule.kataloge.einwilligungsart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.core.types.schule.PersonTyp;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKatalogEinwilligungsart;

/**
 * Tests für die Klasse {@link EinwilligungsartRepositoryImpl}.
 */
@ExtendWith(MockitoExtension.class)
class EinwilligungsartRepositoryImplTest {

	private static final String QUERY_EXISTS = "SELECT e FROM DTOKatalogEinwilligungsart e WHERE e.ID = ?1 AND e.personTyp = ?2";

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private EinwilligungsartRepositoryImpl repository;

	private static DTOKatalogEinwilligungsart dto(final long id) {
		return new DTOKatalogEinwilligungsart(id, "Bezeichnung " + id, true, 1);
	}

	@Test
	@DisplayName("Test: Prüfe, ob getAllIdsByPersonTyp die IDs aufsteigend sortiert liefert.")
	void testGetAllIdsByPersonTyp() {
		when(conn.queryList(DTOKatalogEinwilligungsart.QUERY_BY_PERSONTYP, DTOKatalogEinwilligungsart.class, PersonTyp.SCHUELER))
				.thenReturn(List.of(dto(7L), dto(2L), dto(5L)));

		assertThat(repository.getAllIdsByPersonTyp(PersonTyp.SCHUELER)).containsExactly(2L, 5L, 7L);
	}

	@Test
	@DisplayName("Test: Prüfe, ob getAllIdsByPersonTyp eine leere Liste liefert, wenn keine Einwilligungsarten existieren.")
	void testGetAllIdsByPersonTypEmpty() {
		when(conn.queryList(DTOKatalogEinwilligungsart.QUERY_BY_PERSONTYP, DTOKatalogEinwilligungsart.class, PersonTyp.LEHRER))
				.thenReturn(List.of());

		assertThat(repository.getAllIdsByPersonTyp(PersonTyp.LEHRER)).isEmpty();
	}

	@Test
	@DisplayName("Test: Prüfe, ob existsByIdAndPersonTyp true liefert, wenn die Einwilligungsart für den Personentyp existiert.")
	void testExistsByIdAndPersonTypFound() {
		when(conn.existsBy(QUERY_EXISTS, DTOKatalogEinwilligungsart.class, 3L, PersonTyp.SCHUELER)).thenReturn(true);

		assertThat(repository.existsByIdAndPersonTyp(3L, PersonTyp.SCHUELER)).isTrue();
	}

	@Test
	@DisplayName("Test: Prüfe, ob existsByIdAndPersonTyp false liefert, wenn die Einwilligungsart nicht existiert.")
	void testExistsByIdAndPersonTypNotFound() {
		when(conn.existsBy(QUERY_EXISTS, DTOKatalogEinwilligungsart.class, 3L, PersonTyp.SCHUELER)).thenReturn(false);

		assertThat(repository.existsByIdAndPersonTyp(3L, PersonTyp.SCHUELER)).isFalse();
	}

}
