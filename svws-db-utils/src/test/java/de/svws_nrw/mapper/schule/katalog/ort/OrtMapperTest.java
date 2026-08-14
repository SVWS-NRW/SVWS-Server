package de.svws_nrw.mapper.schule.katalog.ort;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.service.schule.katalog.ort.OrtCreateRequest;
import de.svws_nrw.service.schule.katalog.ort.OrtPatchRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;

class OrtMapperTest {

	private final OrtMapper mapper = OrtMapper.INSTANCE;

	private static final long VALID_ID_BUNDESLAND = 1010L;
	private static final String VALID_SCHLUESSEL_BUNDESLAND = "SH";

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private DTOOrt createEntity(final long id) {
		return new DTOOrt(id, "53840", "Troisdorf");
	}

	private OrtCreateRequest createRequest() {
		final var dto = new OrtCreateRequest();
		dto.plz = "53840";
		dto.ortsname = "Troisdorf";
		dto.kreis = "RSK";
		dto.idBundesland = VALID_ID_BUNDESLAND;
		dto.sortierung = 1;
		dto.istSichtbar = true;
		dto.istAenderbar = true;
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
			entity.kreis = "RSK";
			entity.sortierung = 1;
			entity.istSichtbar = true;
			entity.istAenderbar = false;

			final var result = mapper.toApi(entity, 2024);

			assertThat(result)
					.hasFieldOrPropertyWithValue("id", 7L)
					.hasFieldOrPropertyWithValue("plz", "53840")
					.hasFieldOrPropertyWithValue("ortsname", "Troisdorf")
					.hasFieldOrPropertyWithValue("kreis", "RSK")
					.hasFieldOrPropertyWithValue("sortierung", 1)
					.hasFieldOrPropertyWithValue("istSichtbar", true)
					.hasFieldOrPropertyWithValue("istAenderbar", false);
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
		@DisplayName("Mappt istAenderbar als false bei null")
		void toApi_mapptIstAenderbarFalseBeiNull() {
			final var entity = createEntity(1L);
			entity.istAenderbar = null;

			final var result = mapper.toApi(entity, 2024);

			assertThat(result.istAenderbar).isFalse();
		}

		@Test
		@DisplayName("Mappt idBundesland korrekt bei bekanntem Schlüssel")
		void toApi_mapptIdBundeslandBeiBekanntemSchluessel() {
			final var entity = createEntity(1L);
			entity.schluesselBundesland = VALID_SCHLUESSEL_BUNDESLAND;

			final var result = mapper.toApi(entity, 2024);

			assertThat(result.idBundesland).isEqualTo(VALID_ID_BUNDESLAND);
		}

		@Test
		@DisplayName("Mappt idBundesland als null bei unbekanntem Schlüssel")
		void toApi_mapptIdBundeslandNullBeiUnbekanntemSchluessel() {
			final var entity = createEntity(1L);
			entity.schluesselBundesland = "UNBEKANNT";

			final var result = mapper.toApi(entity, 2024);

			assertThat(result.idBundesland).isNull();
		}

		@ParameterizedTest
		@DisplayName("Mappt idBundesland als null bei null Schlüssel")
		@NullSource
		void toApi_mapptIdBundeslandNullBeiNullSchluessel(final String schluessel) {
			final var entity = createEntity(1L);
			entity.schluesselBundesland = schluessel;

			final var result = mapper.toApi(entity, 2024);

			assertThat(result.idBundesland).isNull();
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
					.hasFieldOrPropertyWithValue("plz", "53840")
					.hasFieldOrPropertyWithValue("ortsname", "Troisdorf")
					.hasFieldOrPropertyWithValue("kreis", "RSK")
					.hasFieldOrPropertyWithValue("sortierung", 1)
					.hasFieldOrPropertyWithValue("istSichtbar", true)
					.hasFieldOrPropertyWithValue("istAenderbar", true);
		}

		@Test
		@DisplayName("id wird nicht gemappt — bleibt 0")
		void toDomain_idWirdNichtGemappt() {
			final var dto = createRequest();

			final var result = mapper.toDomain(dto);

			assertThat(result.id).isZero();
		}

		@Test
		@DisplayName("schluesselBundesland wird nicht gemappt — bleibt null")
		void toDomain_schluesselBundeslandWirdNichtGemappt() {
			final var dto = createRequest();

			final var result = mapper.toDomain(dto);

			assertThat(result.schluesselBundesland).isNull();
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
			final var dto = new OrtPatchRequest();
			dto.plz = JsonNullable.of("50667");
			dto.ortsname = JsonNullable.of("Köln");
			dto.kreis = JsonNullable.of("K");
			dto.sortierung = JsonNullable.of(99);
			dto.istSichtbar = JsonNullable.of(false);
			dto.istAenderbar = JsonNullable.of(false);
			final var entity = createEntity(1L);

			mapper.patch(dto, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("plz", "50667")
					.hasFieldOrPropertyWithValue("ortsname", "Köln")
					.hasFieldOrPropertyWithValue("kreis", "K")
					.hasFieldOrPropertyWithValue("sortierung", 99)
					.hasFieldOrPropertyWithValue("istSichtbar", false)
					.hasFieldOrPropertyWithValue("istAenderbar", false);
		}

		@Test
		@DisplayName("Lässt undefined Felder unverändert")
		void patch_laesst_undefinierteFelder_unveraendert() {
			final var dto = new OrtPatchRequest();
			final var entity = createEntity(1L);
			entity.kreis = "ORIGINAL";
			entity.sortierung = 42;

			mapper.patch(dto, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("plz", "53840")
					.hasFieldOrPropertyWithValue("ortsname", "Troisdorf")
					.hasFieldOrPropertyWithValue("kreis", "ORIGINAL")
					.hasFieldOrPropertyWithValue("sortierung", 42);
		}

		@Test
		@DisplayName("Mischt definierte und undefined Felder korrekt")
		void patch_mischtDefinierteUndUndefinierteFelder() {
			final var dto = new OrtPatchRequest();
			dto.ortsname = JsonNullable.of("Bonn");
			final var entity = createEntity(1L);
			entity.kreis = "ORIGINAL";

			mapper.patch(dto, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("ortsname", "Bonn")
					.hasFieldOrPropertyWithValue("kreis", "ORIGINAL");
		}

		@Test
		@DisplayName("schluesselBundesland wird nicht durch patch verändert — wird extern gesetzt")
		void patch_schluesselBundeslandWirdNichtGemappt() {
			final var dto = new OrtPatchRequest();
			dto.idBundesland = JsonNullable.of(VALID_ID_BUNDESLAND);
			final var entity = createEntity(1L);
			entity.schluesselBundesland = "ORIGINAL";

			mapper.patch(dto, entity);

			assertThat(entity.schluesselBundesland).isEqualTo("ORIGINAL");
		}

		@Test
		@DisplayName("id wird durch patch nicht verändert")
		void patch_idWirdNichtVeraendert() {
			final var dto = new OrtPatchRequest();
			final var entity = createEntity(42L);

			mapper.patch(dto, entity);

			assertThat(entity.id).isEqualTo(42L);
		}
	}
}
