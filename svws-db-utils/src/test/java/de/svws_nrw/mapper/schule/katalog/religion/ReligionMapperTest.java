package de.svws_nrw.mapper.schule.katalog.religion;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.db.dto.current.schild.katalog.DTOReligion;
import de.svws_nrw.service.schule.katalog.religion.ReligionCreateRequest;
import de.svws_nrw.service.schule.katalog.religion.ReligionPatchRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;

class ReligionMapperTest {

	private final ReligionMapper mapper = ReligionMapper.INSTANCE;

	private static final long VALID_ID_RELIGION = 1000L;
	private static final String VALID_SCHLUESSEL_RELIGION = "AR";

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private DTOReligion createEntity(final long id) {
		return new DTOReligion(id, "röm.-kath.");
	}

	private ReligionCreateRequest createRequest() {
		final var dto = new ReligionCreateRequest();
		dto.bezeichnung = "röm.-kath.";
		dto.bezeichnungZeugnis = "katholisch";
		dto.idReligion = VALID_ID_RELIGION;
		dto.sortierung = 1;
		dto.istSichtbar = true;
		return dto;
	}

	// -------------------------------------------------------------------------
	// toApi
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("toApi")
	class ToApi {

		@Test
		@DisplayName("Mappt alle einfachen Felder korrekt")
		void toApi_mapptAlleEinfachenFelder() {
			final var entity = createEntity(7L);
			entity.bezeichnungZeugnis = "katholisch";
			entity.sortierung = 1;
			entity.istSichtbar = true;

			final var result = mapper.toApi(entity, 2024);

			assertThat(result)
					.hasFieldOrPropertyWithValue("id", 7L)
					.hasFieldOrPropertyWithValue("bezeichnung", "röm.-kath.")
					.hasFieldOrPropertyWithValue("bezeichnungZeugnis", "katholisch")
					.hasFieldOrPropertyWithValue("sortierung", 1)
					.hasFieldOrPropertyWithValue("istSichtbar", true);
		}

		@Test
		@DisplayName("Mappt sortierung mit Defaultwert 32000 bei null")
		void toApi_mapptSortierungDefaultWertBeiNull() {
			final var entity = createEntity(1L);
			entity.sortierung = null;

			final var result = mapper.toApi(entity, 2024);

			assertThat(result.sortierung).isEqualTo(32000);
		}

		@Test
		@DisplayName("Mappt istSichtbar als false bei null")
		void toApi_mapptIstSichtbarFalseBeiNull() {
			final var entity = createEntity(1L);
			entity.istSichtbar = null;

			final var result = mapper.toApi(entity, 2024);

			assertThat(result.istSichtbar).isFalse();
		}

		@Test
		@DisplayName("Mappt idReligion korrekt bei bekanntem Schlüssel")
		void toApi_mapptIdReligionBeiBekanntemSchluessel() {
			final var entity = createEntity(1L);
			entity.schluesselReligion = VALID_SCHLUESSEL_RELIGION;

			final var result = mapper.toApi(entity, 2024);

			assertThat(result.idReligion).isEqualTo(VALID_ID_RELIGION);
		}

		@Test
		@DisplayName("Mappt idReligion als null bei unbekanntem Schlüssel")
		void toApi_mapptIdReligionNullBeiUnbekanntemSchluessel() {
			final var entity = createEntity(1L);
			entity.schluesselReligion = "UNBEKANNT";

			final var result = mapper.toApi(entity, 2024);

			assertThat(result.idReligion).isNull();
		}

		@ParameterizedTest
		@DisplayName("Mappt idReligion als null bei null Schlüssel")
		@NullSource
		void toApi_mapptIdReligionNullBeiNullSchluessel(final String schluessel) {
			final var entity = createEntity(1L);
			entity.schluesselReligion = schluessel;

			final var result = mapper.toApi(entity, 2024);

			assertThat(result.idReligion).isNull();
		}

		@Test
		@DisplayName("referenziertInAnderenTabellen ist nach toApi immer false — wird extern gesetzt")
		void toApi_referenziertInAnderenTabellenIstFalse() {
			final var entity = createEntity(1L);

			final var result = mapper.toApi(entity, 2024);

			assertThat(result.referenziertInAnderenTabellen).isFalse();
		}
	}

	// -------------------------------------------------------------------------
	// toDomain
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("toDomain")
	class ToDomain {

		@Test
		@DisplayName("Mappt einfache Felder korrekt")
		void toDomain_mapptEinfacheFelder() {
			final var dto = createRequest();

			final var result = mapper.toDomain(dto);

			assertThat(result)
					.hasFieldOrPropertyWithValue("bezeichnung", "röm.-kath.")
					.hasFieldOrPropertyWithValue("bezeichnungZeugnis", "katholisch")
					.hasFieldOrPropertyWithValue("sortierung", 1)
					.hasFieldOrPropertyWithValue("istSichtbar", true);
		}

		@Test
		@DisplayName("id wird nicht gemappt — bleibt 0")
		void toDomain_idWirdNichtGemappt() {
			final var dto = createRequest();

			final var result = mapper.toDomain(dto);

			assertThat(result.id).isZero();
		}

		@Test
		@DisplayName("istAenderbar wird nicht gemappt — bleibt null")
		void toDomain_istAenderbarWirdNichtGemappt() {
			final var dto = createRequest();

			final var result = mapper.toDomain(dto);

			assertThat(result.istAenderbar).isNull();
		}

		@Test
		@DisplayName("bezeichnungExport wird nicht gemappt — bleibt null")
		void toDomain_bezeichnungExportWirdNichtGemappt() {
			final var dto = createRequest();

			final var result = mapper.toDomain(dto);

			assertThat(result.bezeichnungExport).isNull();
		}

		@Test
		@DisplayName("Mappt kuerzel korrekt bei bekannter idReligion")
		void toDomain_mapptKuerzelBeiBekannterIdReligion() {
			final var dto = createRequest();
			dto.idReligion = VALID_ID_RELIGION;

			final var result = mapper.toDomain(dto);

			assertThat(result.schluesselReligion).isEqualTo(VALID_SCHLUESSEL_RELIGION);
		}
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("patch")
	class Patch {

		@Test
		@DisplayName("Aktualisiert einfache Felder korrekt")
		void patch_aktualisiertEinfacheFelder() {
			final var dto = new ReligionPatchRequest();
			dto.bezeichnung = JsonNullable.of("ev.");
			dto.bezeichnungZeugnis = JsonNullable.of("evangelisch");
			dto.sortierung = JsonNullable.of(2);
			dto.istSichtbar = JsonNullable.of(false);
			final var entity = createEntity(1L);

			mapper.patch(dto, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("bezeichnung", "ev.")
					.hasFieldOrPropertyWithValue("bezeichnungZeugnis", "evangelisch")
					.hasFieldOrPropertyWithValue("sortierung", 2)
					.hasFieldOrPropertyWithValue("istSichtbar", false);
		}

		@Test
		@DisplayName("Lässt undefined Felder unverändert")
		void patch_laesst_undefinierteFelder_unveraendert() {
			final var dto = new ReligionPatchRequest();
			final var entity = createEntity(1L);
			entity.bezeichnungZeugnis = "katholisch";
			entity.sortierung = 42;

			mapper.patch(dto, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("bezeichnung", "röm.-kath.")
					.hasFieldOrPropertyWithValue("bezeichnungZeugnis", "katholisch")
					.hasFieldOrPropertyWithValue("sortierung", 42);
		}

		@Test
		@DisplayName("Mischt definierte und undefined Felder korrekt")
		void patch_mischtDefinierteUndUndefinierteFelder() {
			final var dto = new ReligionPatchRequest();
			dto.bezeichnung = JsonNullable.of("ev.");
			final var entity = createEntity(1L);
			entity.bezeichnungZeugnis = "ORIGINAL";

			mapper.patch(dto, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("bezeichnung", "ev.")
					.hasFieldOrPropertyWithValue("bezeichnungZeugnis", "ORIGINAL");
		}

		@Test
		@DisplayName("id wird durch patch nicht verändert")
		void patch_idWirdNichtVeraendert() {
			final var dto = new ReligionPatchRequest();
			final var entity = createEntity(42L);

			mapper.patch(dto, entity);

			assertThat(entity.id).isEqualTo(42L);
		}

		@Test
		@DisplayName("istAenderbar wird durch patch nicht verändert")
		void patch_istAenderbarWirdNichtVeraendert() {
			final var dto = new ReligionPatchRequest();
			final var entity = createEntity(1L);
			entity.istAenderbar = true;

			mapper.patch(dto, entity);

			assertThat(entity.istAenderbar).isTrue();
		}
	}
}
