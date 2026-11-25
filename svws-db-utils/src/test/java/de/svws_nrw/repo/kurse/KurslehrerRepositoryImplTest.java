package de.svws_nrw.repo.kurse;

import static org.assertj.core.api.Assertions.assertThat;
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
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursLehrer;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursLehrerPK;

/**
 * Tests für die Klasse {@link KurslehrerRepositoryImpl}.
 */
@ExtendWith(MockitoExtension.class)
class KurslehrerRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private KurslehrerRepositoryImpl repository;

	@Test
	@DisplayName("mapIdToParameter | Der zusammengesetzte PK wird in Kurs-ID und Lehrer-ID aufgeteilt")
	void mapIdToParameter() {
		assertThat(repository.mapIdToParameter(new DTOKursLehrerPK(10L, 20L))).containsExactly(10L, 20L);
	}

	@Test
	@DisplayName("findById | Delegiert mit den beiden Teilen der zusammengesetzten ID an queryByKey")
	void findById() {
		final var entity = new DTOKursLehrer(10L, 20L);
		when(conn.queryByKey(DTOKursLehrer.class, 10L, 20L)).thenReturn(entity);

		assertThat(repository.findById(new DTOKursLehrerPK(10L, 20L))).containsSame(entity);
	}

	@Test
	@DisplayName("getListByIdKurs | Bestimmt die Zusatzkräfte über QUERY_BY_KURS_ID")
	void getListByIdKurs() {
		final var entities = List.of(new DTOKursLehrer(1L, 10L), new DTOKursLehrer(1L, 11L));
		when(conn.queryList(DTOKursLehrer.QUERY_BY_KURS_ID, DTOKursLehrer.class, 1L)).thenReturn(entities);

		assertThat(repository.getListByIdKurs(1L)).containsExactlyElementsOf(entities);
	}

	@Test
	@DisplayName("getMapZusatzkraefte | Ordnet die Zusatzkräfte den Kursen zu und liefert für Kurse ohne Zusatzkräfte eine leere Liste")
	void getMapZusatzkraefte() {
		final var zusatzkraft = new DTOKursLehrer(1L, 10L);
		when(conn.queryList(DTOKursLehrer.QUERY_LIST_BY_KURS_ID, DTOKursLehrer.class, Set.of(1L, 2L))).thenReturn(List.of(zusatzkraft));

		assertThat(repository.getMapZusatzkraefte(Set.of(1L, 2L)))
				.hasSize(2)
				.containsEntry(1L, List.of(zusatzkraft))
				.containsEntry(2L, List.of());
	}

	@Test
	@DisplayName("getMapZusatzkraefte | Ohne Kurs-IDs wird eine leere Map geliefert")
	void getMapZusatzkraefte_ohneIds() {
		assertThat(repository.getMapZusatzkraefte(Set.of())).isEmpty();
		assertThat(repository.getMapZusatzkraefte(null)).isEmpty();
	}

}
