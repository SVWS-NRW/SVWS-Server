package de.svws_nrw.repo.schueler.schulbesuch;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerMerkmale;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchuelerMerkmalRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private SchuelerMerkmalRepositoryImpl repository;

	@Test
	@DisplayName("getAllByIdSchueler | Gibt Liste zurück")
	void getAllByIdSchueler_returnsList() {
		final var idSchueler = 42L;
		final var entity1 = new DTOSchuelerMerkmale(1L, idSchueler);
		final var entity2 = new DTOSchuelerMerkmale(2L, idSchueler);

		when(conn.queryList(DTOSchuelerMerkmale.QUERY_BY_IDSCHUELER, DTOSchuelerMerkmale.class, idSchueler))
				.thenReturn(List.of(entity1, entity2));

		final var result = repository.getAllByIdSchueler(idSchueler);

		assertThat(result)
				.isNotNull()
				.hasSize(2)
				.containsExactly(entity1, entity2);

		verify(conn, times(1)).queryList(DTOSchuelerMerkmale.QUERY_BY_IDSCHUELER, DTOSchuelerMerkmale.class, idSchueler);
	}

	@Test
	@DisplayName("getAllByIdSchueler | Leere Liste wenn keine Einträge vorhanden")
	void getAllByIdSchueler_emptyList() {
		final var idSchueler = 99L;

		when(conn.queryList(DTOSchuelerMerkmale.QUERY_BY_IDSCHUELER, DTOSchuelerMerkmale.class, idSchueler))
				.thenReturn(List.of());

		final var result = repository.getAllByIdSchueler(idSchueler);

		assertThat(result).isNotNull().isEmpty();
		verify(conn, times(1)).queryList(DTOSchuelerMerkmale.QUERY_BY_IDSCHUELER, DTOSchuelerMerkmale.class, idSchueler);
	}

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		final var newRepository = new SchuelerMerkmalRepositoryImpl(conn);

		assertThat(newRepository)
				.isNotNull()
				.isInstanceOf(SchuelerMerkmalRepositoryImpl.class)
				.isInstanceOf(SchuelerMerkmalRepository.class);
	}

}
