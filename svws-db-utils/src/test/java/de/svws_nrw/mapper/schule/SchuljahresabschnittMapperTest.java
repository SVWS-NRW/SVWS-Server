package de.svws_nrw.mapper.schule;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.service.schule.SchuljahresabschnittCreateRequest;
import de.svws_nrw.service.schule.SchuljahresabschnittPatchRequest;

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

	@Test
	@DisplayName("toDomain | Mappt alle Felder korrekt und vergibt keine ID")
	void toDomain_mapsAllFields() {
		final var dto = new SchuljahresabschnittCreateRequest();
		dto.schuljahr = 2025;
		dto.abschnitt = 2;
		dto.idVorigerAbschnitt = 41L;
		dto.idFolgeAbschnitt = 43L;

		final var result = mapper.toDomain(dto);

		assertThat(result)
				.extracting("ID", "Jahr", "Abschnitt", "VorigerAbschnitt_ID", "FolgeAbschnitt_ID")
				.containsExactly(0L, 2025, 2, 41L, 43L);
	}

	@Test
	@DisplayName("toDomain | Optionale Verknüpfungen bleiben null")
	void toDomain_optionalLinksStayNull() {
		final var dto = new SchuljahresabschnittCreateRequest();
		dto.schuljahr = 2025;
		dto.abschnitt = 1;

		final var result = mapper.toDomain(dto);

		assertThat(result.VorigerAbschnitt_ID).isNull();
		assertThat(result.FolgeAbschnitt_ID).isNull();
	}

	@Test
	@DisplayName("patch | Aktualisiert beide Verknüpfungen mit definierten Werten")
	void patch_updatesDefinedFields() {
		final var dto = new SchuljahresabschnittPatchRequest();
		dto.idVorigerAbschnitt = JsonNullable.of(10L);
		dto.idFolgeAbschnitt = JsonNullable.of(12L);

		final var entity = new DTOSchuljahresabschnitte(11L, 2025, 2);
		entity.VorigerAbschnitt_ID = 1L;
		entity.FolgeAbschnitt_ID = 2L;

		mapper.patch(dto, entity);

		assertThat(entity)
				.extracting("ID", "Jahr", "Abschnitt", "VorigerAbschnitt_ID", "FolgeAbschnitt_ID")
				.containsExactly(11L, 2025, 2, 10L, 12L);
	}

	@Test
	@DisplayName("patch | Lässt undefined Felder unverändert")
	void patch_keepsUndefinedFieldsUnchanged() {
		final var dto = new SchuljahresabschnittPatchRequest();

		final var entity = new DTOSchuljahresabschnitte(11L, 2025, 2);
		entity.VorigerAbschnitt_ID = 10L;
		entity.FolgeAbschnitt_ID = 12L;

		mapper.patch(dto, entity);

		assertThat(entity)
				.extracting("VorigerAbschnitt_ID", "FolgeAbschnitt_ID")
				.containsExactly(10L, 12L);
	}

	@Test
	@DisplayName("patch | Ein explizites null löst die Verknüpfung")
	void patch_explicitNullClearsLink() {
		final var dto = new SchuljahresabschnittPatchRequest();
		dto.idVorigerAbschnitt = JsonNullable.of(null);

		final var entity = new DTOSchuljahresabschnitte(11L, 2025, 2);
		entity.VorigerAbschnitt_ID = 10L;
		entity.FolgeAbschnitt_ID = 12L;

		mapper.patch(dto, entity);

		assertThat(entity)
				.extracting("VorigerAbschnitt_ID", "FolgeAbschnitt_ID")
				.containsExactly(null, 12L);
	}

}
