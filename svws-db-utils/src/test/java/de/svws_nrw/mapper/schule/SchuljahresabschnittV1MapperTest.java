package de.svws_nrw.mapper.schule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;

/**
 * Testet den Mapper für Schuljahresabschnitte der externen API (Version 1).
 */
class SchuljahresabschnittV1MapperTest {

	private final SchuljahresabschnittV1Mapper mapper = SchuljahresabschnittV1Mapper.INSTANCE;

	private static Schuljahresabschnitt createAbschnitt(final long id, final int schuljahr, final int abschnitt, final Long idVoriger, final Long idFolge) {
		final var result = new Schuljahresabschnitt();
		result.id = id;
		result.schuljahr = schuljahr;
		result.abschnitt = abschnitt;
		result.idVorigerAbschnitt = idVoriger;
		result.idFolgeAbschnitt = idFolge;
		return result;
	}

	@Test
	@DisplayName("toApi | Mappt alle Felder korrekt")
	void toApi_mapsAllFields() {
		final var result = mapper.toApi(createAbschnitt(42L, 2025, 2, 41L, 43L));

		assertThat(result)
				.extracting("id", "schuljahr", "abschnitt", "idVorigerAbschnitt", "idFolgeAbschnitt")
				.containsExactly(42L, 2025, 2, 41L, 43L);
	}

	@Test
	@DisplayName("toApi | Übernimmt fehlende Verknüpfungen als null")
	void toApi_mapsMissingLinksAsNull() {
		final var result = mapper.toApi(createAbschnitt(1L, 2025, 1, null, null));

		assertThat(result)
				.extracting("idVorigerAbschnitt", "idFolgeAbschnitt")
				.containsExactly(null, null);
	}

	@Test
	@DisplayName("toApiListNewestFirst | Sortiert nach Schuljahr und Abschnitt absteigend")
	void toApiListNewestFirst_sortsDescending() {
		final var abschnitte = List.of(
				createAbschnitt(2L, 2025, 2, 1L, 3L),
				createAbschnitt(3L, 2026, 1, 2L, null),
				createAbschnitt(1L, 2025, 1, null, 2L));

		final var result = mapper.toApiListNewestFirst(abschnitte);

		assertThat(result)
				.extracting("id", "schuljahr", "abschnitt")
				.containsExactly(
						tuple(3L, 2026, 1),
						tuple(2L, 2025, 2),
						tuple(1L, 2025, 1));
	}

	@Test
	@DisplayName("toApiListNewestFirst | Eine leere Liste ergibt eine leere Liste")
	void toApiListNewestFirst_emptyList() {
		assertThat(mapper.toApiListNewestFirst(List.of())).isEmpty();
	}

}
