package de.svws_nrw.repo.schueler.lernplattform;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernplattform;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernplattformPK;

/**
 * Tests für die Klasse {@link SchuelerLernplattformRepositoryImpl}.
 */
@ExtendWith(MockitoExtension.class)
class SchuelerLernplattformRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private SchuelerLernplattformRepositoryImpl repository;

	private static DTOSchuelerLernplattform dto(final long idSchueler, final long idLernplattform) {
		return new DTOSchuelerLernplattform(idSchueler, idLernplattform, false, false, false, false);
	}

	@Test
	@DisplayName("Test: Prüfe, ob mapIdToParameter den zusammengesetzten PK korrekt in ein Array umwandelt.")
	void testMapIdToParameter() {
		final Object[] params = repository.mapIdToParameter(new DTOSchuelerLernplattformPK(10L, 20L));

		assertNotNull(params);
		assertEquals(2, params.length);
		assertEquals(10L, params[0]);
		assertEquals(20L, params[1]);
	}

	@Test
	@DisplayName("Test: Prüfe, ob findById mit den beiden Teilen der zusammengesetzten ID an queryByKey delegiert.")
	void testFindById() {
		final DTOSchuelerLernplattform entity = dto(10L, 20L);
		when(conn.queryByKey(DTOSchuelerLernplattform.class, 10L, 20L)).thenReturn(entity);

		assertThat(repository.findById(new DTOSchuelerLernplattformPK(10L, 20L))).containsSame(entity);
	}

	@Test
	@DisplayName("Test: Prüfe, ob findById ein leeres Optional liefert, wenn keine Zuordnung existiert.")
	void testFindByIdNichtVorhanden() {
		when(conn.queryByKey(DTOSchuelerLernplattform.class, 10L, 20L)).thenReturn(null);

		assertThat(repository.findById(new DTOSchuelerLernplattformPK(10L, 20L))).isEmpty();
	}

	@Test
	@DisplayName("Test: Prüfe, ob getListByIdSchueler die Zuordnungen des Schülers über QUERY_BY_SCHUELERID bestimmt.")
	void testGetListByIdSchueler() {
		final List<DTOSchuelerLernplattform> entities = List.of(dto(1L, 10L), dto(1L, 11L));
		when(conn.queryList(DTOSchuelerLernplattform.QUERY_BY_SCHUELERID, DTOSchuelerLernplattform.class, 1L)).thenReturn(entities);

		assertThat(repository.getListByIdSchueler(1L)).containsExactlyElementsOf(entities);
	}

	@Test
	@DisplayName("Test: Prüfe, ob getListByIdLernplattform die Zuordnungen der Lernplattform über QUERY_BY_LERNPLATTFORMID bestimmt.")
	void testGetListByIdLernplattform() {
		final List<DTOSchuelerLernplattform> entities = List.of(dto(1L, 10L), dto(2L, 10L));
		when(conn.queryList(DTOSchuelerLernplattform.QUERY_BY_LERNPLATTFORMID, DTOSchuelerLernplattform.class, 10L)).thenReturn(entities);

		assertThat(repository.getListByIdLernplattform(10L)).containsExactlyElementsOf(entities);
	}

}
