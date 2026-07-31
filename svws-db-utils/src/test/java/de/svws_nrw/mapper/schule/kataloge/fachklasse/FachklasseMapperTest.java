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
		dto.idSchulgliederung = 1001000L; // A01
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

			final var result = mapper.toApi(entity, 2024);

			assertThat(result.id).isEqualTo(42L);
		}

		@Test
		@DisplayName("Mappt kuerzel korrekt")
		void toApi_mapptKuerzel() {
			final var entity = createEntity(1L);
			entity.kuerzel = "BK-TEST";

			final var result = mapper.toApi(entity, 2024);

			assertThat(result.kuerzel).isEqualTo("BK-TEST");
		}

		@Test
		@DisplayName("Mappt bezeichnung korrekt")
		void toApi_mapptBezeichnung() {
			final var entity = createEntity(1L);
			entity.bezeichnung = "Elektrotechnik";

			final var result = mapper.toApi(entity, 2024);

			assertThat(result.bezeichnung).isEqualTo("Elektrotechnik");
		}

		@Test
		@DisplayName("Mappt sortierung korrekt")
		void toApi_mapptSortierung() {
			final var entity = createEntity(1L);
			entity.sortierung = 32000;

			final var result = mapper.toApi(entity, 2024);

			assertThat(result.sortierung).isEqualTo(32000);
		}

		@Test
		@DisplayName("Mappt istSichtbar korrekt")
		void toApi_mapptIstSichtbar() {
			final var entity = createEntity(1L);
			entity.istSichtbar = true;

			final var result = mapper.toApi(entity, 2024);

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

			final var result = mapper.toApi(entity, 2024);

			assertThat(result)
					.hasFieldOrPropertyWithValue("id", 7L)
					.hasFieldOrPropertyWithValue("kuerzel", "IT")
					.hasFieldOrPropertyWithValue("bezeichnung", "Informationstechnik")
					.hasFieldOrPropertyWithValue("sortierung", 100)
					.hasFieldOrPropertyWithValue("istSichtbar", false);
		}
	}

	// -------------------------------------------------------------------------
	// mapKennungToApi
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("mapKennungToApi")
	class MapKennungToApi {

		@Test
		@DisplayName("Rekonstruiert Schlüssel korrekt bei zweistelligem Präfix (60-102-00 -> 60-10200)")
		void mapKennungToApi_rekonstruiertZweistelligenPraefix() {
			final var entity = createEntity(1L);
			entity.Kennung = "60-102-00";

			final var result = mapper.toApi(entity, 2000);

			assertThat(result.idFachklasse).isEqualTo(1867000L);
		}

		@Test
		@DisplayName("Rekonstruiert Schlüssel korrekt bei dreistelligem Präfix (210-148-18 -> 210-14818)")
		void mapKennungToApi_rekonstruiertDreistelligenPraefix() {
			final var entity = createEntity(1L);
			entity.Kennung = "210-148-18";

			final var result = mapper.toApi(entity, 2024);

			assertThat(result.idFachklasse).isEqualTo(1429000L);
		}

		@Test
		@DisplayName("Befüllt idSchulgliederung bei bekanntem BKIndexTyp und gültigem Schuljahr")
		void toApi_befuelltIdSchulgliederung() {
			final var entity = createEntity(1L);
			entity.BKIndexTyp = "A01";

			final var result = mapper.toApi(entity, 2024);

			assertThat(result.idSchulgliederung).isNotNull();
		}

		@Test
		@DisplayName("Liefert null für idFachklasse bei unbekanntem Schlüssel")
		void mapKennungToApi_liefertNullBeiUnbekanntemSchluessel() {
			final var entity = createEntity(1L);
			entity.Kennung = "99-999-99";

			final var result = mapper.toApi(entity, 2024);

			assertThat(result.idFachklasse).isNull();
		}

		@Test
		@DisplayName("Liefert null für idSchulgliederung bei unbekanntem BKIndexTyp")
		void toApi_liefertNullIdSchulgliederungBeiUnbekanntemBKIndexTyp() {
			final var entity = createEntity(1L);
			entity.BKIndexTyp = "UNBEKANNT";

			final var result = mapper.toApi(entity, 2024);

			assertThat(result.idSchulgliederung).isNull();
		}

		@ParameterizedTest
		@DisplayName("Liefert null für idFachklasse bei null oder leerem Kennung-Feld")
		@NullSource
		@ValueSource(strings = {" ", "   "})
		void mapKennungToApi_liefertNullBeiNullOderBlank(final String kennung) {
			final var entity = createEntity(1L);
			entity.Kennung = kennung;

			final var result = mapper.toApi(entity, 2024);

			assertThat(result.idFachklasse).isNull();
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

			final var result = mapper.toDomain(dto);

			assertThat(result)
					.hasFieldOrPropertyWithValue("BKIndex", 10)
					.hasFieldOrPropertyWithValue("FKS", "101")
					.hasFieldOrPropertyWithValue("AP", "00")
					.hasFieldOrPropertyWithValue("Kennung", "10-101-00")
					.hasFieldOrPropertyWithValue("FKS_AP_SIM", "10100");
		}

		@Test
		@DisplayName("Befüllt BKIndexTyp korrekt aus idSchulgliederung")
		void toDomain_mapptBKIndexTypAusIdSchulgliederung() {
			final var dto = createRequest();

			final var result = mapper.toDomain(dto);

			assertThat(result.BKIndexTyp).isEqualTo("A01");
		}

		@Test
		@DisplayName("Befüllt einfache Felder korrekt")
		void toDomain_mapptEinfacheFelder() {
			final var dto = createRequest();

			final var result = mapper.toDomain(dto);

			assertThat(result)
					.hasFieldOrPropertyWithValue("bezeichnung", "Anlagenmechaniker/-in")
					.hasFieldOrPropertyWithValue("kuerzel", "AM")
					.hasFieldOrPropertyWithValue("istSichtbar", true)
					.hasFieldOrPropertyWithValue("sortierung", 100);
		}

		@Test
		@DisplayName("Befüllt bezeichnungWeiblich und Berufsebenen korrekt")
		void toDomain_mapptBezeichnungWeiblichUndBerufsebenen() {
			final var dto = createRequest();
			dto.bezeichnungWeiblich = "Anlagenmechanikerin";
			dto.berufsebene1 = "Metall";
			dto.berufsebene2 = "Sanitär";
			dto.berufsebene3 = "Heizung";

			final var result = mapper.toDomain(dto);

			assertThat(result)
					.hasFieldOrPropertyWithValue("bezeichnungWeiblich", "Anlagenmechanikerin")
					.hasFieldOrPropertyWithValue("berufsebene1", "Metall")
					.hasFieldOrPropertyWithValue("berufsebene2", "Sanitär")
					.hasFieldOrPropertyWithValue("berufsebene3", "Heizung");
		}

		@Test
		@DisplayName("Befüllt idDqrNiveau direkt aus Request")
		void toDomain_mapptIdDqrNiveau() {
			final var dto = createRequest();
			dto.idDqrNiveau = 3;

			final var result = mapper.toDomain(dto);

			assertThat(result.idDqrNiveau).isEqualTo(3);
		}

		@Test
		@DisplayName("Setzt idDqrNiveau auf null wenn im Request nicht gesetzt")
		void toDomain_idDqrNiveauNullWennNichtGesetzt() {
			final var dto = createRequest();
			dto.idDqrNiveau = null;

			final var result = mapper.toDomain(dto);

			assertThat(result.idDqrNiveau).isNull();
		}

		@Test
		@DisplayName("Befüllt keine CoreType-Felder bei null idFachklasse")
		void toDomain_mapptKeineCoreTypeFelderBeiNullId() {
			final var dto = createRequest();
			dto.idFachklasse = null;

			final var result = mapper.toDomain(dto);

			assertThat(result)
					.hasFieldOrPropertyWithValue("BKIndex", null)
					.hasFieldOrPropertyWithValue("FKS", null)
					.hasFieldOrPropertyWithValue("AP", null)
					.hasFieldOrPropertyWithValue("Kennung", null)
					.hasFieldOrPropertyWithValue("FKS_AP_SIM", null);
		}

		@Test
		@DisplayName("Befüllt keine CoreType-Felder bei unbekannter idFachklasse")
		void toDomain_mapptKeineCoreTypeFelderBeiUnbekannterIdFachklasse() {
			final var dto = createRequest();
			dto.idFachklasse = -1L;

			final var result = mapper.toDomain(dto);

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

			mapper.patch(dto, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("bezeichnung", "Neue Bezeichnung")
					.hasFieldOrPropertyWithValue("kuerzel", "NK")
					.hasFieldOrPropertyWithValue("istSichtbar", false)
					.hasFieldOrPropertyWithValue("sortierung", 200);
		}

		@Test
		@DisplayName("Aktualisiert bezeichnungWeiblich und Berufsebenen korrekt")
		void patch_aktualisiertBezeichnungWeiblichUndBerufsebenen() {
			final var dto = new FachklasseEintragPatchRequest();
			dto.bezeichnungWeiblich = JsonNullable.of("Anlagenmechanikerin");
			dto.berufsebene1 = JsonNullable.of("Metall");
			dto.berufsebene2 = JsonNullable.of("Sanitär");
			dto.berufsebene3 = JsonNullable.of("Heizung");
			final var entity = createEntity(1L);

			mapper.patch(dto, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("bezeichnungWeiblich", "Anlagenmechanikerin")
					.hasFieldOrPropertyWithValue("berufsebene1", "Metall")
					.hasFieldOrPropertyWithValue("berufsebene2", "Sanitär")
					.hasFieldOrPropertyWithValue("berufsebene3", "Heizung");
		}

		@Test
		@DisplayName("Aktualisiert idDqrNiveau direkt aus Request")
		void patch_aktualisiertIdDqrNiveau() {
			final var dto = new FachklasseEintragPatchRequest();
			dto.idDqrNiveau = JsonNullable.of(3);
			final var entity = createEntity(1L);

			mapper.patch(dto, entity);

			assertThat(entity.idDqrNiveau).isEqualTo(3);
		}

		@Test
		@DisplayName("Lässt idDqrNiveau unverändert wenn undefined")
		void patch_laesst_idDqrNiveau_unveraendert_bei_undefined() {
			final var dto = new FachklasseEintragPatchRequest();
			final var entity = createEntity(1L);
			entity.idDqrNiveau = 4;

			mapper.patch(dto, entity);

			assertThat(entity.idDqrNiveau).isEqualTo(4);
		}

		@Test
		@DisplayName("Aktualisiert CoreType-Felder bei gesetzter idFachklasse")
		void patch_aktualisiertCoreTypeFelder() {
			final var dto = new FachklasseEintragPatchRequest();
			dto.idFachklasse = JsonNullable.of(5000L);
			final var entity = createEntity(1L);

			mapper.patch(dto, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("BKIndex", 10)
					.hasFieldOrPropertyWithValue("FKS", "101")
					.hasFieldOrPropertyWithValue("AP", "00")
					.hasFieldOrPropertyWithValue("Kennung", "10-101-00")
					.hasFieldOrPropertyWithValue("FKS_AP_SIM", "10100");
		}

		@Test
		@DisplayName("BKIndexTyp wird beim Patch nicht verändert (nicht patchbar)")
		void patch_veraendertBKIndexTypNicht() {
			final var dto = new FachklasseEintragPatchRequest();
			dto.idFachklasse = JsonNullable.of(5000L);
			final var entity = createEntity(1L);
			entity.BKIndexTyp = "ORIGINAL";

			mapper.patch(dto, entity);

			assertThat(entity.BKIndexTyp).isEqualTo("ORIGINAL");
		}

		@Test
		@DisplayName("Lässt undefined Felder unverändert")
		void patch_laesst_undefinierteFelder_unveraendert() {
			final var dto = new FachklasseEintragPatchRequest();
			final var entity = createEntity(1L);
			entity.bezeichnung = "Original";
			entity.kuerzel = "OR";
			entity.BKIndex = 10;

			mapper.patch(dto, entity);

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

			mapper.patch(dto, entity);

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

			mapper.patch(dto, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("bezeichnung", "Geändert")
					.hasFieldOrPropertyWithValue("kuerzel", "ORIGINAL");
		}
	}
}
