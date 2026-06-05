package de.svws_nrw.repo.schule.schulleitung;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOSchulleitung;
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
class SchulleitungRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private SchulleitungRepositoryImpl repository;

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		final var newRepository = new SchulleitungRepositoryImpl(conn);

		assertThat(newRepository)
				.isNotNull()
				.isInstanceOf(SchulleitungRepositoryImpl.class)
				.isInstanceOf(SchulleitungRepository.class);
	}

	@Test
	@DisplayName("getAllByIdLehrer | Gibt Liste zurück")
	void getAllByIdLehrer_returnsList() {
		final var idLehrer = 42L;
		final var entity1 = new DTOSchulleitung(1L, 1L, "Schulleitung", idLehrer);
		final var entity2 = new DTOSchulleitung(2L, 2L, "Koordination", idLehrer);

		when(conn.queryList(DTOSchulleitung.QUERY_BY_LEHRERID, DTOSchulleitung.class, idLehrer))
				.thenReturn(List.of(entity1, entity2));

		final var result = repository.getAllByIdLehrer(idLehrer);

		assertThat(result)
				.isNotNull()
				.hasSize(2)
				.containsExactly(entity1, entity2);

		verify(conn, times(1)).queryList(DTOSchulleitung.QUERY_BY_LEHRERID, DTOSchulleitung.class, idLehrer);
	}

	@Test
	@DisplayName("getAllByIdLehrer | Leere Liste wenn keine Einträge vorhanden")
	void getAllByIdLehrer_emptyList() {
		final var idLehrer = 99L;

		when(conn.queryList(DTOSchulleitung.QUERY_BY_LEHRERID, DTOSchulleitung.class, idLehrer))
				.thenReturn(List.of());

		final var result = repository.getAllByIdLehrer(idLehrer);

		assertThat(result).isNotNull().isEmpty();
		verify(conn, times(1)).queryList(DTOSchulleitung.QUERY_BY_LEHRERID, DTOSchulleitung.class, idLehrer);
	}

	@Test
	@DisplayName("getAllByIdLeitungsfunktion | Gibt Liste zurück")
	void getAllByIdLeitungsfunktion_returnsList() {
		final var idLeitungsfunktion = 1L;
		final var entity1 = new DTOSchulleitung(1L, idLeitungsfunktion, "Schulleitung", 10L);
		final var entity2 = new DTOSchulleitung(2L, idLeitungsfunktion, "Schulleitung", 20L);

		when(conn.queryList(DTOSchulleitung.QUERY_BY_LEITUNGSFUNKTIONID, DTOSchulleitung.class, idLeitungsfunktion))
				.thenReturn(List.of(entity1, entity2));

		final var result = repository.getAllByIdLeitungsfunktion(idLeitungsfunktion);

		assertThat(result)
				.isNotNull()
				.hasSize(2)
				.containsExactly(entity1, entity2);

		verify(conn, times(1)).queryList(DTOSchulleitung.QUERY_BY_LEITUNGSFUNKTIONID, DTOSchulleitung.class, idLeitungsfunktion);
	}

	@Test
	@DisplayName("getAllByIdLeitungsfunktion | Leere Liste wenn keine Einträge vorhanden")
	void getAllByIdLeitungsfunktion_emptyList() {
		final var idLeitungsfunktion = 99L;

		when(conn.queryList(DTOSchulleitung.QUERY_BY_LEITUNGSFUNKTIONID, DTOSchulleitung.class, idLeitungsfunktion))
				.thenReturn(List.of());

		final var result = repository.getAllByIdLeitungsfunktion(idLeitungsfunktion);

		assertThat(result).isNotNull().isEmpty();
		verify(conn, times(1)).queryList(DTOSchulleitung.QUERY_BY_LEITUNGSFUNKTIONID, DTOSchulleitung.class, idLeitungsfunktion);
	}
}
