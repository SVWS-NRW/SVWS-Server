package de.svws_nrw.mapper.schule.kataloge.fachklasse;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.db.dto.current.schild.berufskolleg.DTOFachklassen;
import de.svws_nrw.service.schule.kataloge.fachklasse.FachklasseEintragCreateRequest;
import de.svws_nrw.service.schule.kataloge.fachklasse.FachklasseEintragPatchRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;

class FachklasseMapperTest {

	private final FachklasseMapper mapper = FachklasseMapper.INSTANCE;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private DTOFachklassen createEntity(final long id) {
		return new DTOFachklassen(id);
	}

	private FachklasseEintragCreateRequest createRequest() {
		final var dto = new FachklasseEintragCreateRequest();
		dto.idFachklasse = 5000L;
		dto.bezeichnung = "Anlagenmechaniker/-in";
		dto.kuerzel = "AM";
		dto.istSichtbar = true;
		dto.sortierung = 100;
		return dto;
	}

	// -------------------------------------------------------------------------
	// toApi
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("toApi")
	class ToApi {

		@Test
		@DisplayName("Mappt ID korrekt")
		void toApi_mapptId() {
			final var entity = createEntity(42L);

			final var result = mapper.toApi(entity);

			assertThat(result.id).isEqualTo(42L);
		}

		@Test
		@DisplayName("Mappt kuerzel korrekt")
		void toApi_mapptKuerzel() {
			final var entity = createEntity(1L);
			entity.kuerzel = "BK-TEST";

			final var result = mapper.toApi(entity);

			assertThat(result.kuerzel).isEqualTo("BK-TEST");
		}

		@Test
		@DisplayName("Mappt bezeichnung korrekt")
		void toApi_mapptBezeichnung() {
			final var entity = createEntity(1L);
			entity.bezeichnung = "Elektrotechnik";

			final var result = mapper.toApi(entity);

			assertThat(result.bezeichnung).isEqualTo("Elektrotechnik");
		}

		@Test
		@DisplayName("Mappt sortierung korrekt")
		void toApi_mapptSortierung() {
			final var entity = createEntity(1L);
			entity.sortierung = 32000;

			final var result = mapper.toApi(entity);

			assertThat(result.sortierung).isEqualTo(32000);
		}

		@Test
		@DisplayName("Mappt istSichtbar korrekt")
		void toApi_mapptIstSichtbar() {
			final var entity = createEntity(1L);
			entity.istSichtbar = true;

			final var result = mapper.toApi(entity);

			assertThat(result.istSichtbar).isTrue();
		}

		@Test
		@DisplayName("Mappt alle einfachen Felder in einem Durchgang korrekt")
		void toApi_mapptAlleEinfachenFelder() {
			final var entity = createEntity(7L);
			entity.kuerzel = "IT";
			entity.bezeichnung = "Informationstechnik";
			entity.sortierung = 100;
			entity.istSichtbar = false;

			final var result = mapper.toApi(entity);

			assertThat(result)
					.hasFieldOrPropertyWithValue("id", 7L)
					.hasFieldOrPropertyWithValue("kuerzel", "IT")
					.hasFieldOrPropertyWithValue("bezeichnung", "Informationstechnik")
					.hasFieldOrPropertyWithValue("sortierung", 100)
					.hasFieldOrPropertyWithValue("istSichtbar", false);
		}
	}

	// -------------------------------------------------------------------------
	// mapIdFachklasseToApi
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("mapIdFachklasse")
	class MapIdFachklasse {

		@Test
		@DisplayName("Rekonstruiert Schlüssel korrekt bei zweistelligem Präfix (60-102-00 -> 60-10200)")
		void mapIdFachklasse_rekonstruiertZweistelligenPraefix() {
			final var entity = createEntity(1L);
			entity.Kennung = "60-102-00";

			final var result = mapper.toApi(entity);

			assertThat(result.idFachklasse).isEqualTo(1867000L);
		}

		@Test
		@DisplayName("Rekonstruiert Schlüssel korrekt bei dreistelligem Präfix (210-148-18 -> 210-14818)")
		void mapIdFachklasse_rekonstruiertDreistelligenPraefix() {
			final var entity = createEntity(1L);
			entity.Kennung = "210-148-18";

			final var result = mapper.toApi(entity);

			assertThat(result.idFachklasse).isEqualTo(1429000L);
		}

		@Test
		@DisplayName("Liefert null bei unbekanntem Schlüssel")
		void mapIdFachklasse_liefertNullBeiUnbekanntemSchluessel() {
			final var entity = createEntity(1L);
			entity.Kennung = "99-999-99";

			final var result = mapper.toApi(entity);

			assertThat(result.idFachklasse).isNull();
		}

		@ParameterizedTest
		@DisplayName("Liefert null bei null oder leerem Kennung-Feld")
		@NullSource
		@ValueSource(strings = {" ", "   "})
		void mapIdFachklasse_liefertNullBeiNullOderBlank(final String kennung) {
			final var entity = createEntity(1L);
			entity.Kennung = kennung;

			final var result = mapper.toApi(entity);

			assertThat(result.idFachklasse).isNull();
		}

		@Test
		@DisplayName("Direkt: gibt null zurück bei null")
		void mapIdFachklasse_direktNull() {
			assertThat(mapper.mapIdFachklasseToApi(null)).isNull();
		}

		@Test
		@DisplayName("Direkt: gibt null zurück bei leerem String")
		void mapIdFachklasse_direktBlank() {
			assertThat(mapper.mapIdFachklasseToApi("   ")).isNull();
		}

		@Test
		@DisplayName("Direkt: gibt null zurück bei unbekanntem Schlüssel")
		void mapIdFachklasse_direktUnbekannt() {
			assertThat(mapper.mapIdFachklasseToApi("99-999-99")).isNull();
		}

		@Test
		@DisplayName("Direkt: liefert korrekte ID bei bekanntem Schlüssel")
		void mapIdFachklasse_direktBekannterSchluessel() {
			assertThat(mapper.mapIdFachklasseToApi("60-102-00")).isEqualTo(1867000L);
		}
	}

	// -------------------------------------------------------------------------
	// toDomain
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("toDomain")
	class ToDomain {

		@Test
		@DisplayName("Befüllt CoreType-abhängige Felder korrekt bei gültiger idFachklasse")
		void toDomain_mapptCoreTypeFelder() {
			final var dto = createRequest();

			final var result = mapper.toDomain(dto, 2024);

			assertThat(result)
					.hasFieldOrPropertyWithValue("BKIndex", 10)
					.hasFieldOrPropertyWithValue("FKS", "101")
					.hasFieldOrPropertyWithValue("AP", "00")
					.hasFieldOrPropertyWithValue("Kennung", "10-101-00")
					.hasFieldOrPropertyWithValue("FKS_AP_SIM", "10100")
					.hasFieldOrPropertyWithValue("BKIndexTyp", "A01")
					.hasFieldOrPropertyWithValue("DQR_Niveau", 4);
		}

		@Test
		@DisplayName("Befüllt einfache Felder korrekt")
		void toDomain_mapptEinfacheFelder() {
			final var dto = createRequest();

			final var result = mapper.toDomain(dto, 2024);

			assertThat(result)
					.hasFieldOrPropertyWithValue("bezeichnung", "Anlagenmechaniker/-in")
					.hasFieldOrPropertyWithValue("kuerzel", "AM")
					.hasFieldOrPropertyWithValue("istSichtbar", true)
					.hasFieldOrPropertyWithValue("sortierung", 100);
		}

		@Test
		@DisplayName("Befüllt keine CoreType-Felder bei null idFachklasse")
		void toDomain_mapptKeineCoreTypeFelderBeiNullId() {
			final var dto = createRequest();
			dto.idFachklasse = null;

			final var result = mapper.toDomain(dto, 2024);

			assertThat(result)
					.hasFieldOrPropertyWithValue("BKIndex", null)
					.hasFieldOrPropertyWithValue("FKS", null)
					.hasFieldOrPropertyWithValue("AP", null)
					.hasFieldOrPropertyWithValue("Kennung", null)
					.hasFieldOrPropertyWithValue("FKS_AP_SIM", null)
					.hasFieldOrPropertyWithValue("BKIndexTyp", null)
					.hasFieldOrPropertyWithValue("DQR_Niveau", null);
		}

		@Test
		@DisplayName("Befüllt keine CoreType-Felder bei unbekannter idFachklasse")
		void toDomain_mapptKeineCoreTypeFelderBeiUnbekannterIdFachklasse() {
			final var dto = createRequest();
			dto.idFachklasse = -1L;

			final var result = mapper.toDomain(dto, 2024);

			assertThat(result)
					.hasFieldOrPropertyWithValue("BKIndex", null)
					.hasFieldOrPropertyWithValue("FKS", null)
					.hasFieldOrPropertyWithValue("AP", null)
					.hasFieldOrPropertyWithValue("Kennung", null);
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
			final var dto = new FachklasseEintragPatchRequest();
			dto.bezeichnung = JsonNullable.of("Neue Bezeichnung");
			dto.kuerzel = JsonNullable.of("NK");
			dto.istSichtbar = JsonNullable.of(false);
			dto.sortierung = JsonNullable.of(200);
			final var entity = createEntity(1L);

			mapper.patch(dto, 2024, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("bezeichnung", "Neue Bezeichnung")
					.hasFieldOrPropertyWithValue("kuerzel", "NK")
					.hasFieldOrPropertyWithValue("istSichtbar", false)
					.hasFieldOrPropertyWithValue("sortierung", 200);
		}

		@Test
		@DisplayName("Aktualisiert CoreType-Felder bei gesetzter idFachklasse")
		void patch_aktualisiertCoreTypeFelder() {
			final var dto = new FachklasseEintragPatchRequest();
			dto.idFachklasse = JsonNullable.of(5000L);
			final var entity = createEntity(1L);

			mapper.patch(dto, 2024, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("BKIndex", 10)
					.hasFieldOrPropertyWithValue("FKS", "101")
					.hasFieldOrPropertyWithValue("AP", "00")
					.hasFieldOrPropertyWithValue("Kennung", "10-101-00")
					.hasFieldOrPropertyWithValue("FKS_AP_SIM", "10100")
					.hasFieldOrPropertyWithValue("BKIndexTyp", "A01")
					.hasFieldOrPropertyWithValue("DQR_Niveau", 4);
		}

		@Test
		@DisplayName("Lässt undefined Felder unverändert")
		void patch_laesst_undefinierteFelder_unveraendert() {
			final var dto = new FachklasseEintragPatchRequest();
			final var entity = createEntity(1L);
			entity.bezeichnung = "Original";
			entity.kuerzel = "OR";
			entity.BKIndex = 10;

			mapper.patch(dto, 2024, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("bezeichnung", "Original")
					.hasFieldOrPropertyWithValue("kuerzel", "OR")
					.hasFieldOrPropertyWithValue("BKIndex", 10);
		}

		@Test
		@DisplayName("Lässt CoreType-Felder unverändert wenn idFachklasse undefined")
		void patch_laesst_coreTypeFelder_unveraendert_bei_undefinedId() {
			final var dto = new FachklasseEintragPatchRequest();
			final var entity = createEntity(1L);
			entity.BKIndex = 99;
			entity.Kennung = "ORIGINAL";

			mapper.patch(dto, 2024, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("BKIndex", 99)
					.hasFieldOrPropertyWithValue("Kennung", "ORIGINAL");
		}

		@Test
		@DisplayName("Mischt definierte und undefined Felder korrekt")
		void patch_mischtDefinierteUndUndefinierteFelder() {
			final var dto = new FachklasseEintragPatchRequest();
			dto.bezeichnung = JsonNullable.of("Geändert");
			final var entity = createEntity(1L);
			entity.bezeichnung = "Alt";
			entity.kuerzel = "ORIGINAL";

			mapper.patch(dto, 2024, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("bezeichnung", "Geändert")
					.hasFieldOrPropertyWithValue("kuerzel", "ORIGINAL");
		}
	}
}
