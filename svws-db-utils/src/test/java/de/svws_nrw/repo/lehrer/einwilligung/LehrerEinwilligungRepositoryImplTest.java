package de.svws_nrw.repo.lehrer.einwilligung;

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
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerDatenschutz;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerDatenschutzPK;

/**
 * Tests für die Klasse {@link LehrerEinwilligungRepositoryImpl}.
 */
@ExtendWith(MockitoExtension.class)
class LehrerEinwilligungRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private LehrerEinwilligungRepositoryImpl repository;

	private static DTOLehrerDatenschutz dto(final long idLehrer, final long idEinwilligungsart) {
		return new DTOLehrerDatenschutz(idLehrer, idEinwilligungsart, false, false);
	}

	@Test
	@DisplayName("Test: Prüfe, ob mapIdToParameter den zusammengesetzten PK korrekt in ein Array umwandelt.")
	void testMapIdToParameter() {
		final Object[] params = repository.mapIdToParameter(new DTOLehrerDatenschutzPK(10L, 20L));

		assertNotNull(params);
		assertEquals(2, params.length);
		assertEquals(10L, params[0]);
		assertEquals(20L, params[1]);
	}

	@Test
	@DisplayName("Test: Prüfe, ob findById mit den beiden Teilen der zusammengesetzten ID an queryByKey delegiert.")
	void testFindById() {
		final DTOLehrerDatenschutz entity = dto(10L, 20L);
		when(conn.queryByKey(DTOLehrerDatenschutz.class, 10L, 20L)).thenReturn(entity);

		assertThat(repository.findById(new DTOLehrerDatenschutzPK(10L, 20L))).containsSame(entity);
	}

	@Test
	@DisplayName("Test: Prüfe, ob findById ein leeres Optional liefert, wenn keine Einwilligung existiert.")
	void testFindByIdNichtVorhanden() {
		when(conn.queryByKey(DTOLehrerDatenschutz.class, 10L, 20L)).thenReturn(null);

		assertThat(repository.findById(new DTOLehrerDatenschutzPK(10L, 20L))).isEmpty();
	}

	@Test
	@DisplayName("Test: Prüfe, ob getListByIdLehrer die Einwilligungen des Lehrers über QUERY_BY_LEHRERID bestimmt.")
	void testGetListByIdLehrer() {
		final List<DTOLehrerDatenschutz> entities = List.of(dto(1L, 10L), dto(1L, 11L));
		when(conn.queryList(DTOLehrerDatenschutz.QUERY_BY_LEHRERID, DTOLehrerDatenschutz.class, 1L)).thenReturn(entities);

		assertThat(repository.getListByIdLehrer(1L)).containsExactlyElementsOf(entities);
	}

	@Test
	@DisplayName("Test: Prüfe, ob getListByIdEinwilligungsart die Einwilligungen der Einwilligungsart über QUERY_BY_DATENSCHUTZID bestimmt.")
	void testGetListByIdEinwilligungsart() {
		final List<DTOLehrerDatenschutz> entities = List.of(dto(1L, 10L), dto(2L, 10L));
		when(conn.queryList(DTOLehrerDatenschutz.QUERY_BY_DATENSCHUTZID, DTOLehrerDatenschutz.class, 10L)).thenReturn(entities);

		assertThat(repository.getListByIdEinwilligungsart(10L)).containsExactlyElementsOf(entities);
	}

}
