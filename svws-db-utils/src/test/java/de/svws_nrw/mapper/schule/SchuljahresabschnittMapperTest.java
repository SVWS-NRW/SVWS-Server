package de.svws_nrw.mapper.schule;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;

/**
 * Testet den Mapper für Schuljahresabschnitte
 */
class SchuljahresabschnittMapperTest {

	private final SchuljahresabschnittMapper mapper = SchuljahresabschnittMapper.INSTANCE;

	@Test
	@DisplayName("toApi | Mappt alle Felder korrekt")
	void toApi_mapsAllFields() {
		final var entity = new DTOSchuljahresabschnitte(42L, 2025, 2);
		entity.VorigerAbschnitt_ID = 41L;
		entity.FolgeAbschnitt_ID = 43L;

		final var result = mapper.toApi(entity);

		assertThat(result)
				.extracting("id", "schuljahr", "abschnitt", "idVorigerAbschnitt", "idFolgeAbschnitt")
				.containsExactly(42L, 2025, 2, 41L, 43L);
	}

	@Test
	@DisplayName("toApi | Übernimmt fehlende Verknüpfungen als null")
	void toApi_mapsMissingLinksAsNull() {
		final var entity = new DTOSchuljahresabschnitte(1L, 2025, 1);

		final var result = mapper.toApi(entity);

		assertThat(result.idVorigerAbschnitt).isNull();
		assertThat(result.idFolgeAbschnitt).isNull();
	}

}
