package de.svws_nrw.mapper.schule.merkmale;

import de.svws_nrw.db.dto.current.schild.schule.DTOMerkmale;
import de.svws_nrw.mapper.schule.katalog.merkmal.MerkmalMapper;
import de.svws_nrw.service.schule.katalog.merkmal.MerkmalCreateRequest;
import de.svws_nrw.service.schule.katalog.merkmal.MerkmalPatchRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;

class MerkmalMapperTest {

	private final MerkmalMapper mapper = MerkmalMapper.INSTANCE;

	@Test
	@DisplayName("toApi | Mappt alle Felder korrekt")
	void toApi_mapsAllFields() {
		final var entity = new DTOMerkmale(42L);
		entity.kuerzel = "GANZTAG";
		entity.bezeichnung = "Ganztagsschule";
		entity.istSchulmerkmal = true;
		entity.istSchuelermerkmal = false;

		final var result = mapper.toApi(entity);

		assertThat(result)
				.extracting("id", "kuerzel", "bezeichnung", "istSchulmerkmal", "istSchuelermerkmal")
				.containsExactly(42L, "GANZTAG", "Ganztagsschule", true, false);
	}

	@Test
	@DisplayName("toDomain | Mappt alle Felder korrekt")
	void toDomain_mapsAllFields() {
		final var dto = new MerkmalCreateRequest();
		dto.kuerzel = "GANZTAG";
		dto.bezeichnung = "Ganztagsschule";
		dto.istSchulmerkmal = true;
		dto.istSchuelermerkmal = false;

		final var result = mapper.toDomain(dto);

		assertThat(result)
				.extracting("kuerzel", "bezeichnung", "istSchulmerkmal", "istSchuelermerkmal")
				.containsExactly("GANZTAG", "Ganztagsschule", true, false);
	}

	@Test
	@DisplayName("applyPatch | Aktualisiert alle Felder mit definierten Werten")
	void patch_updatesAllDefinedFields() {
		final var dto = new MerkmalPatchRequest();
		dto.kuerzel = JsonNullable.of("NEU");
		dto.bezeichnung = JsonNullable.of("Neue Bezeichnung");
		dto.istSchulmerkmal = JsonNullable.of(false);
		dto.istSchuelermerkmal = JsonNullable.of(true);

		final var entity = new DTOMerkmale(1L);
		entity.kuerzel = "ALT";
		entity.bezeichnung = "Alte Bezeichnung";
		entity.istSchulmerkmal = true;
		entity.istSchuelermerkmal = false;

		mapper.patch(dto, entity);

		assertThat(entity)
				.extracting("kuerzel", "bezeichnung", "istSchulmerkmal", "istSchuelermerkmal")
				.containsExactly("NEU", "Neue Bezeichnung", false, true);
	}

	@Test
	@DisplayName("applyPatch | Lässt undefined Felder unverändert")
	void patch_keepsUndefinedFieldsUnchanged() {
		final var dto = new MerkmalPatchRequest();
		dto.kuerzel = JsonNullable.undefined();
		dto.bezeichnung = JsonNullable.undefined();
		dto.istSchulmerkmal = JsonNullable.undefined();
		dto.istSchuelermerkmal = JsonNullable.undefined();

		final var entity = new DTOMerkmale(1L);
		entity.kuerzel = "ORIGINAL";
		entity.bezeichnung = "Original Bezeichnung";
		entity.istSchulmerkmal = true;
		entity.istSchuelermerkmal = false;

		mapper.patch(dto, entity);

		assertThat(entity)
				.extracting("kuerzel", "bezeichnung", "istSchulmerkmal", "istSchuelermerkmal")
				.containsExactly("ORIGINAL", "Original Bezeichnung", true, false);
	}

	@Test
	@DisplayName("applyPatch | Setzt null-Werte korrekt")
	void patch_setsNullValues() {
		final var dto = new MerkmalPatchRequest();
		dto.kuerzel = JsonNullable.of(null);
		dto.bezeichnung = JsonNullable.of(null);

		final var entity = new DTOMerkmale(1L);
		entity.kuerzel = "ALT";
		entity.bezeichnung = "Alte Bezeichnung";

		mapper.patch(dto, entity);

		assertThat(entity)
				.extracting("kuerzel", "bezeichnung")
				.containsExactly(null, null);
	}
}
