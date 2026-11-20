package de.svws_nrw.service.schueler.lernabschnitt;

import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.schueler.SchuelerLeistungsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittBemerkungen;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerPSFachBemerkungen;
import de.svws_nrw.mapper.schueler.leistungsdaten.SchuelerLeistungsdatenMapper;
import de.svws_nrw.mapper.schueler.lernabschnittsdaten.SchuelerLernabschnittBemerkungenMapper;
import de.svws_nrw.repo.schueler.leistungsdaten.SchuelerLeistungsdatenRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittBemerkungRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchuelerLernabschnittMappingContextLoaderTest {

	private static final long ID_SCHUELER = 10L;
	private static final long ID_ABSCHNITT_2024 = 20L;
	private static final long ID_ABSCHNITT_2025 = 21L;
	private static final Map<Long, Integer> SCHULJAHR_BY_ID_ABSCHNITT = Map.of(ID_ABSCHNITT_2024, 2024, ID_ABSCHNITT_2025, 2025);

	@Mock
	private SchuelerLernabschnittBemerkungRepository bemerkungRepository;

	@Mock
	private SchuelerLeistungsdatenRepository leistungsdatenRepository;

	@Mock
	private SchuelerLeistungsdatenMapper leistungsdatenMapper;

	@Mock
	private SchuelerLernabschnittBemerkungenMapper bemerkungenMapper;

	private SchuelerLernabschnittMappingContextLoader loader;

	@BeforeEach
	void setUp() {
		loader = new SchuelerLernabschnittMappingContextLoader(bemerkungRepository, leistungsdatenRepository, leistungsdatenMapper,
				bemerkungenMapper);
	}

	private static DTOSchuelerLernabschnittsdaten createLernabschnitt(final long id, final long idSchuljahresabschnitt) {
		return new DTOSchuelerLernabschnittsdaten(id, ID_SCHUELER, idSchuljahresabschnitt, true, null, 1L);
	}

	@Test
	@DisplayName("Leere Liste - leere Map ohne Datenbankabfrage")
	void load_leereListe() {
		final var result = loader.load(List.of(), SCHULJAHR_BY_ID_ABSCHNITT, true);

		assertThat(result).isEmpty();
		verifyNoInteractions(bemerkungRepository, leistungsdatenRepository);
	}

	@Test
	@DisplayName("Bemerkungen und Leistungsdaten werden je Lernabschnitt gemappt, Leistungsdaten mit dem Schuljahr des Abschnitts")
	void load_mitLeistungsdaten() {
		final var lernabschnitt1 = createLernabschnitt(1L, ID_ABSCHNITT_2024);
		final var lernabschnitt2 = createLernabschnitt(2L, ID_ABSCHNITT_2025);
		final var bemerkungDto = new DTOSchuelerPSFachBemerkungen(5L, 1L);
		final var bemerkung = new SchuelerLernabschnittBemerkungen();
		final var leistungDto = mock(DTOSchuelerLeistungsdaten.class);
		final var leistung = new SchuelerLeistungsdaten();
		when(bemerkungRepository.findMapByLernabschnittID(List.of(1L, 2L))).thenReturn(Map.of(1L, bemerkungDto));
		when(leistungsdatenRepository.getMapListByLernabschnittsIds(List.of(1L, 2L))).thenReturn(Map.of(2L, List.of(leistungDto)));
		when(bemerkungenMapper.toApi(bemerkungDto)).thenReturn(bemerkung);
		when(leistungsdatenMapper.toApi(leistungDto, 2025)).thenReturn(leistung);

		final var result = loader.load(List.of(lernabschnitt1, lernabschnitt2), SCHULJAHR_BY_ID_ABSCHNITT, true);

		assertThat(result).hasSize(2);
		assertThat(result.get(1L)).satisfies(kontext -> {
			assertThat(kontext.bemerkung()).isSameAs(bemerkung);
			assertThat(kontext.leistungsdaten()).isEmpty();
		});
		assertThat(result.get(2L)).satisfies(kontext -> {
			assertThat(kontext.bemerkung()).isNotNull().isNotSameAs(bemerkung);
			assertThat(kontext.leistungsdaten()).containsExactly(leistung);
		});
	}

	@Test
	@DisplayName("Ohne Leistungsdaten - nur die Bemerkungen werden geladen")
	void load_ohneLeistungsdaten() {
		final var lernabschnitt = createLernabschnitt(1L, ID_ABSCHNITT_2024);
		when(bemerkungRepository.findMapByLernabschnittID(List.of(1L))).thenReturn(Map.of());

		final var result = loader.load(List.of(lernabschnitt), SCHULJAHR_BY_ID_ABSCHNITT, false);

		assertThat(result.get(1L)).satisfies(kontext -> {
			assertThat(kontext.bemerkung()).isNotNull();
			assertThat(kontext.leistungsdaten()).isEmpty();
		});
		verifyNoInteractions(leistungsdatenRepository, leistungsdatenMapper, bemerkungenMapper);
	}

}
