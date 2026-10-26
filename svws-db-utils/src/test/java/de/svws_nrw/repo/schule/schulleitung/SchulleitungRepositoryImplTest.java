package de.svws_nrw.repo.schule.schulleitung;

import java.util.List;

import de.svws_nrw.asd.validate.DateManager;
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

	private static DTOSchulleitung eintrag(final long id, final long idFunktion, final long idLehrer, final String von, final String bis) {
		final var dto = new DTOSchulleitung(id, idFunktion, "", idLehrer);
		dto.Von = von;
		dto.Bis = bis;
		return dto;
	}

	@Test
	@DisplayName("getIdsLehrerByDates | Mehrere Lehrer mit SL oder Stv. SL im Zeitraum werden alle geliefert")
	void getIdsLehrerByDates_mehrereLehrer() throws Exception {
		when(conn.queryAll(DTOSchulleitung.class)).thenReturn(List.of(
				eintrag(1L, 1L, 10L, "2025-08-01", null),
				eintrag(2L, 1L, 20L, "2025-08-01", null),
				eintrag(3L, 2L, 30L, "2025-08-01", null),
				eintrag(4L, 2L, 40L, "2025-08-01", null)));

		final var result = repository.getIdsLehrerByDates(DateManager.fromValues(2025, 8, 1), DateManager.fromValues(2026, 1, 31));

		assertThat(result).containsExactlyInAnyOrder(10L, 20L, 30L, 40L);
	}

	@Test
	@DisplayName("getIdsLehrerByDates | Einträge außerhalb des Zeitraums und andere Leitungsfunktionen werden ignoriert")
	void getIdsLehrerByDates_ausserhalbUndAndereFunktion() throws Exception {
		when(conn.queryAll(DTOSchulleitung.class)).thenReturn(List.of(
				eintrag(1L, 1L, 10L, "2025-08-01", null),
				eintrag(2L, 1L, 20L, "2020-08-01", "2024-07-31"),
				eintrag(3L, 1L, 30L, "2026-02-01", null),
				eintrag(4L, 3L, 40L, "2025-08-01", null)));

		final var result = repository.getIdsLehrerByDates(DateManager.fromValues(2025, 8, 1), DateManager.fromValues(2026, 1, 31));

		assertThat(result).containsExactly(10L);
	}
}
