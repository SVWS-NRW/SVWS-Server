package de.svws_nrw.mapper.schule.lernplattform;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import de.svws_nrw.db.dto.current.svws.auth.DTOLernplattformen;
import de.svws_nrw.mapper.schule.katalog.lernplattform.LernplattformMapper;
import de.svws_nrw.service.schule.katalog.lernplattform.LernplattformCreateRequest;
import de.svws_nrw.service.schule.katalog.lernplattform.LernplattformPatchRequest;

/**
 * Tests für den {@link LernplattformMapper}.
 */
class LernplattformMapperTest {

	private final LernplattformMapper mapper = LernplattformMapper.INSTANCE;

	@Test
	@DisplayName("toApi | Mappt ID und Bezeichnung und setzt das Referenz-Flag nicht")
	void toApi_mapsFields() {
		final DTOLernplattformen entity = new DTOLernplattformen(42L, "IServ");

		final var result = mapper.toApi(entity);

		assertThat(result.id).isEqualTo(42L);
		assertThat(result.bezeichnung).isEqualTo("IServ");
		assertThat(result.referenziertInAnderenTabellen).isFalse();
	}

	@Test
	@DisplayName("toDomain | Mappt die Bezeichnung und lässt ID und übrige Spalten unberührt")
	void toDomain_mapsBezeichnung() {
		final var dto = new LernplattformCreateRequest();
		dto.bezeichnung = "Moodle";

		final var result = mapper.toDomain(dto);

		assertThat(result.id).isZero();
		assertThat(result.bezeichnung).isEqualTo("Moodle");
		assertThat(result.benutzernameSuffixLehrer).isNull();
		assertThat(result.benutzernameSuffixErzieher).isNull();
		assertThat(result.benutzernameSuffixSchueler).isNull();
		assertThat(result.konfiguration).isNull();
	}

	@Test
	@DisplayName("patch | Aktualisiert die Bezeichnung, wenn sie im Request definiert ist")
	void patch_updatesDefinedBezeichnung() {
		final var dto = new LernplattformPatchRequest();
		dto.bezeichnung = JsonNullable.of("Neu");
		final DTOLernplattformen entity = new DTOLernplattformen(1L, "Alt");

		mapper.patch(dto, entity);

		assertThat(entity.id).isEqualTo(1L);
		assertThat(entity.bezeichnung).isEqualTo("Neu");
	}

	@Test
	@DisplayName("patch | Lässt die Bezeichnung und die übrigen Spalten unverändert, wenn sie nicht definiert sind")
	void patch_ignoresUndefinedBezeichnung() {
		final var dto = new LernplattformPatchRequest();
		final DTOLernplattformen entity = new DTOLernplattformen(1L, "Alt");
		entity.benutzernameSuffixLehrer = "@lehrer";
		entity.konfiguration = "{}";

		mapper.patch(dto, entity);

		assertThat(entity.bezeichnung).isEqualTo("Alt");
		assertThat(entity.benutzernameSuffixLehrer).isEqualTo("@lehrer");
		assertThat(entity.konfiguration).isEqualTo("{}");
	}

}
