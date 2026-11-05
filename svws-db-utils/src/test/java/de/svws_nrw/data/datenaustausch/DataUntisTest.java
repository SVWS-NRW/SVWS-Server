package de.svws_nrw.data.datenaustausch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.repo.klassen.KlassenleitungenRepository;
import de.svws_nrw.repo.lehrer.LehrerRepository;

/**
 * Tests für {@link DataUntis}.
 */
@DisplayName("DataUntis")
class DataUntisTest {

	private KlassenleitungenRepository klassenleitungenRepository;
	private LehrerRepository lehrerRepository;

	@BeforeEach
	void setUp() {
		klassenleitungenRepository = mock(KlassenleitungenRepository.class);
		lehrerRepository = mock(LehrerRepository.class);
	}

	private static DTOLehrer createLehrer(final long id) {
		return new DTOLehrer(id, "K" + id, "Nachname" + id);
	}

	@Test
	@DisplayName("getKlassenlehrerByKlassenId: Mehrere Klassen, Reihenfolge der Leitung bleibt erhalten, ein Lehrer in mehreren Klassen")
	void getKlassenlehrerByKlassenId_mehrereKlassen() {
		when(klassenleitungenRepository.getMapKlassenleitungen(List.of(1L, 2L))).thenReturn(Map.of(1L, List.of(12L, 11L), 2L, List.of(11L)));
		when(lehrerRepository.findMapByIds(anyCollection())).thenReturn(Map.of(11L, createLehrer(11L), 12L, createLehrer(12L)));

		final var result = DataUntis.getKlassenlehrerByKlassenId(klassenleitungenRepository, lehrerRepository, List.of(1L, 2L));

		assertThat(result).containsOnlyKeys(1L, 2L);
		assertThat(result.get(1L)).extracting(l -> l.ID).containsExactly(12L, 11L);
		assertThat(result.get(2L)).extracting(l -> l.ID).containsExactly(11L);
		verify(lehrerRepository, times(1)).findMapByIds(anyCollection());
	}

	@Test
	@DisplayName("getKlassenlehrerByKlassenId: Klassen ohne Klassenleitung fehlen in der Zuordnung")
	void getKlassenlehrerByKlassenId_klasseOhneLeitung() {
		when(klassenleitungenRepository.getMapKlassenleitungen(List.of(1L, 2L))).thenReturn(Map.of(1L, List.of(11L), 2L, List.of()));
		when(lehrerRepository.findMapByIds(anyCollection())).thenReturn(Map.of(11L, createLehrer(11L)));

		final var result = DataUntis.getKlassenlehrerByKlassenId(klassenleitungenRepository, lehrerRepository, List.of(1L, 2L));

		assertThat(result).containsOnlyKeys(1L);
	}

	@Test
	@DisplayName("getKlassenlehrerByKlassenId: Unbekannte Lehrer-IDs werden übersprungen")
	void getKlassenlehrerByKlassenId_unbekannterLehrer() {
		when(klassenleitungenRepository.getMapKlassenleitungen(List.of(1L, 2L))).thenReturn(Map.of(1L, List.of(99L, 11L), 2L, List.of(98L)));
		when(lehrerRepository.findMapByIds(anyCollection())).thenReturn(Map.of(11L, createLehrer(11L)));

		final var result = DataUntis.getKlassenlehrerByKlassenId(klassenleitungenRepository, lehrerRepository, List.of(1L, 2L));

		assertThat(result).containsOnlyKeys(1L);
		assertThat(result.get(1L)).extracting(l -> l.ID).containsExactly(11L);
	}

	@Test
	@DisplayName("getKlassenlehrerByKlassenId: Ohne Klassenleitungen werden keine Lehrer geladen")
	void getKlassenlehrerByKlassenId_keineLeitungen() {
		when(klassenleitungenRepository.getMapKlassenleitungen(List.of(1L))).thenReturn(Map.of(1L, List.of()));

		final var result = DataUntis.getKlassenlehrerByKlassenId(klassenleitungenRepository, lehrerRepository, List.of(1L));

		assertThat(result).isEmpty();
		verify(lehrerRepository, never()).findMapByIds(anyCollection());
	}

}
