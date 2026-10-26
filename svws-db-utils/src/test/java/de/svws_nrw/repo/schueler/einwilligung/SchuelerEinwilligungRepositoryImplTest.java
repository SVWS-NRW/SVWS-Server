package de.svws_nrw.repo.schueler.einwilligung;

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
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerDatenschutz;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerDatenschutzPK;

/**
 * Tests für die Klasse {@link SchuelerEinwilligungRepositoryImpl}.
 */
@ExtendWith(MockitoExtension.class)
class SchuelerEinwilligungRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private SchuelerEinwilligungRepositoryImpl repository;

	private static DTOSchuelerDatenschutz dto(final long idSchueler, final long idEinwilligungsart) {
		return new DTOSchuelerDatenschutz(idSchueler, idEinwilligungsart, false, false);
	}

	@Test
	@DisplayName("Test: Prüfe, ob mapIdToParameter den zusammengesetzten PK korrekt in ein Array umwandelt.")
	void testMapIdToParameter() {
		final Object[] params = repository.mapIdToParameter(new DTOSchuelerDatenschutzPK(10L, 20L));

		assertNotNull(params);
		assertEquals(2, params.length);
		assertEquals(10L, params[0]);
		assertEquals(20L, params[1]);
	}

	@Test
	@DisplayName("Test: Prüfe, ob findById mit den beiden Teilen der zusammengesetzten ID an queryByKey delegiert.")
	void testFindById() {
		final DTOSchuelerDatenschutz entity = dto(10L, 20L);
		when(conn.queryByKey(DTOSchuelerDatenschutz.class, 10L, 20L)).thenReturn(entity);

		assertThat(repository.findById(new DTOSchuelerDatenschutzPK(10L, 20L))).containsSame(entity);
	}

	@Test
	@DisplayName("Test: Prüfe, ob findById ein leeres Optional liefert, wenn keine Einwilligung existiert.")
	void testFindByIdNichtVorhanden() {
		when(conn.queryByKey(DTOSchuelerDatenschutz.class, 10L, 20L)).thenReturn(null);

		assertThat(repository.findById(new DTOSchuelerDatenschutzPK(10L, 20L))).isEmpty();
	}

	@Test
	@DisplayName("Test: Prüfe, ob getListByIdSchueler die Einwilligungen des Schülers über QUERY_BY_SCHUELER_ID bestimmt.")
	void testGetListByIdSchueler() {
		final List<DTOSchuelerDatenschutz> entities = List.of(dto(1L, 10L), dto(1L, 11L));
		when(conn.queryList(DTOSchuelerDatenschutz.QUERY_BY_SCHUELER_ID, DTOSchuelerDatenschutz.class, 1L)).thenReturn(entities);

		assertThat(repository.getListByIdSchueler(1L)).containsExactlyElementsOf(entities);
	}

	@Test
	@DisplayName("Test: Prüfe, ob getListByIdEinwilligungsart die Einwilligungen der Einwilligungsart über QUERY_BY_DATENSCHUTZ_ID bestimmt.")
	void testGetListByIdEinwilligungsart() {
		final List<DTOSchuelerDatenschutz> entities = List.of(dto(1L, 10L), dto(2L, 10L));
		when(conn.queryList(DTOSchuelerDatenschutz.QUERY_BY_DATENSCHUTZ_ID, DTOSchuelerDatenschutz.class, 10L)).thenReturn(entities);

		assertThat(repository.getListByIdEinwilligungsart(10L)).containsExactlyElementsOf(entities);
	}

}
