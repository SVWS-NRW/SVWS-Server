package de.svws_nrw.repo.lehrer.lernplattform;

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
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerLernplattform;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerLernplattformPK;

/**
 * Tests für die Klasse {@link LehrerLernplattformRepositoryImpl}.
 */
@ExtendWith(MockitoExtension.class)
class LehrerLernplattformRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private LehrerLernplattformRepositoryImpl repository;

	private static DTOLehrerLernplattform dto(final long idLehrer, final long idLernplattform) {
		return new DTOLehrerLernplattform(idLehrer, idLernplattform, false, false, false, false);
	}

	@Test
	@DisplayName("Test: Prüfe, ob mapIdToParameter den zusammengesetzten PK korrekt in ein Array umwandelt.")
	void testMapIdToParameter() {
		final Object[] params = repository.mapIdToParameter(new DTOLehrerLernplattformPK(10L, 20L));

		assertNotNull(params);
		assertEquals(2, params.length);
		assertEquals(10L, params[0]);
		assertEquals(20L, params[1]);
	}

	@Test
	@DisplayName("Test: Prüfe, ob findById mit den beiden Teilen der zusammengesetzten ID an queryByKey delegiert.")
	void testFindById() {
		final DTOLehrerLernplattform entity = dto(10L, 20L);
		when(conn.queryByKey(DTOLehrerLernplattform.class, 10L, 20L)).thenReturn(entity);

		assertThat(repository.findById(new DTOLehrerLernplattformPK(10L, 20L))).containsSame(entity);
	}

	@Test
	@DisplayName("Test: Prüfe, ob findById ein leeres Optional liefert, wenn keine Zuordnung existiert.")
	void testFindByIdNichtVorhanden() {
		when(conn.queryByKey(DTOLehrerLernplattform.class, 10L, 20L)).thenReturn(null);

		assertThat(repository.findById(new DTOLehrerLernplattformPK(10L, 20L))).isEmpty();
	}

	@Test
	@DisplayName("Test: Prüfe, ob getListByIdLehrer die Zuordnungen des Lehrers über QUERY_BY_LEHRERID bestimmt.")
	void testGetListByIdLehrer() {
		final List<DTOLehrerLernplattform> entities = List.of(dto(1L, 10L), dto(1L, 11L));
		when(conn.queryList(DTOLehrerLernplattform.QUERY_BY_LEHRERID, DTOLehrerLernplattform.class, 1L)).thenReturn(entities);

		assertThat(repository.getListByIdLehrer(1L)).containsExactlyElementsOf(entities);
	}

	@Test
	@DisplayName("Test: Prüfe, ob getListByIdLernplattform die Zuordnungen der Lernplattform über QUERY_BY_LERNPLATTFORMID bestimmt.")
	void testGetListByIdLernplattform() {
		final List<DTOLehrerLernplattform> entities = List.of(dto(1L, 10L), dto(2L, 10L));
		when(conn.queryList(DTOLehrerLernplattform.QUERY_BY_LERNPLATTFORMID, DTOLehrerLernplattform.class, 10L)).thenReturn(entities);

		assertThat(repository.getListByIdLernplattform(10L)).containsExactlyElementsOf(entities);
	}

}
