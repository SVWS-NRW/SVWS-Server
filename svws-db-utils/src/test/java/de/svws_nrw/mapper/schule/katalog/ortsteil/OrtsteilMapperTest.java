package de.svws_nrw.mapper.schule.katalog.ortsteil;

import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrtsteil;
import de.svws_nrw.service.schule.katalog.ortsteil.OrtsteilCreateRequest;
import de.svws_nrw.service.schule.katalog.ortsteil.OrtsteilPatchRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;

class OrtsteilMapperTest {

	private final OrtsteilMapper mapper = OrtsteilMapper.INSTANCE;

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private DTOOrtsteil createEntity(final long id) {
		return new DTOOrtsteil(id, "Sieglar");
	}

	private DTOOrt createOrt() {
		return new DTOOrt(42L, "53840", "Troisdorf");
	}

	private OrtsteilCreateRequest createRequest() {
		final var dto = new OrtsteilCreateRequest();
		dto.ortsteil = "Sieglar";
		dto.idOrt = 42L;
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
			entity.idOrt = 42L;
			entity.sortierung = 1;
			entity.istSichtbar = true;
			entity.istAenderbar = false;
			final var ort = createOrt();

			final var result = mapper.toApi(entity, ort);

			assertThat(result)
					.hasFieldOrPropertyWithValue("id", 7L)
					.hasFieldOrPropertyWithValue("ortsteil", "Sieglar")
					.hasFieldOrPropertyWithValue("idOrt", 42L)
					.hasFieldOrPropertyWithValue("sortierung", 1)
					.hasFieldOrPropertyWithValue("istSichtbar", true)
					.hasFieldOrPropertyWithValue("istAenderbar", false);
		}

		@Test
		@DisplayName("Mappt bezeichnungOrt und plzOrt aus DTOOrt via @AfterMapping")
		void toApi_mapptBezeichnungOrtUndPlzOrt() {
			final var entity = createEntity(1L);
			final var ort = createOrt();

			final var result = mapper.toApi(entity, ort);

			assertThat(result.bezeichnungOrt).isEqualTo("Troisdorf");
			assertThat(result.plzOrt).isEqualTo("53840");
		}

		@Test
		@DisplayName("Mappt sortierung mit Defaultwert 32000 bei null")
		void toApi_mapptSortierungDefaultWertBeiNull() {
			final var entity = createEntity(1L);
			entity.sortierung = null;

			final var result = mapper.toApi(entity, createOrt());

			assertThat(result.sortierung).isEqualTo(32000);
		}

		@Test
		@DisplayName("Mappt istSichtbar als false bei null")
		void toApi_mapptIstSichtbarFalseBeiNull() {
			final var entity = createEntity(1L);
			entity.istSichtbar = null;

			final var result = mapper.toApi(entity, createOrt());

			assertThat(result.istSichtbar).isFalse();
		}

		@Test
		@DisplayName("Mappt istAenderbar als false bei null")
		void toApi_mapptIstAenderbarFalseBeiNull() {
			final var entity = createEntity(1L);
			entity.istAenderbar = null;

			final var result = mapper.toApi(entity, createOrt());

			assertThat(result.istAenderbar).isFalse();
		}

		@Test
		@DisplayName("referenziertInAnderenTabellen ist nach toApi immer false — wird extern gesetzt")
		void toApi_referenziertInAnderenTabellenIstFalse() {
			final var entity = createEntity(1L);

			final var result = mapper.toApi(entity, createOrt());

			assertThat(result.referenziertInAnderenTabellen).isFalse();
		}

		@Test
		@DisplayName("Setzt bezeichnungOrt und plzOrt nicht wenn ort null")
		void toApi_mapptBezeichnungOrtUndPlzOrtNichtBeiNullOrt() {
			final var entity = createEntity(1L);

			final var result = mapper.toApi(entity, null);

			assertThat(result.bezeichnungOrt).isNull();
			assertThat(result.plzOrt).isNull();
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
					.hasFieldOrPropertyWithValue("ortsteil", "Sieglar")
					.hasFieldOrPropertyWithValue("idOrt", 42L)
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
		@DisplayName("schluesselOrtsteil wird nicht gemappt — bleibt null")
		void toDomain_schluesselOrtsteilWirdNichtGemappt() {
			final var dto = createRequest();

			final var result = mapper.toDomain(dto);

			assertThat(result.schluesselOrtsteil).isNull();
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
			final var dto = new OrtsteilPatchRequest();
			dto.ortsteil = JsonNullable.of("Mitte");
			dto.idOrt = JsonNullable.of(99L);
			dto.sortierung = JsonNullable.of(5);
			dto.istSichtbar = JsonNullable.of(false);
			dto.istAenderbar = JsonNullable.of(false);
			final var entity = createEntity(1L);

			mapper.patch(dto, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("ortsteil", "Mitte")
					.hasFieldOrPropertyWithValue("idOrt", 99L)
					.hasFieldOrPropertyWithValue("sortierung", 5)
					.hasFieldOrPropertyWithValue("istSichtbar", false)
					.hasFieldOrPropertyWithValue("istAenderbar", false);
		}

		@Test
		@DisplayName("Lässt undefined Felder unverändert")
		void patch_laesst_undefinierteFelder_unveraendert() {
			final var dto = new OrtsteilPatchRequest();
			final var entity = createEntity(1L);
			entity.idOrt = 42L;
			entity.sortierung = 7;

			mapper.patch(dto, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("ortsteil", "Sieglar")
					.hasFieldOrPropertyWithValue("idOrt", 42L)
					.hasFieldOrPropertyWithValue("sortierung", 7);
		}

		@Test
		@DisplayName("Mischt definierte und undefined Felder korrekt")
		void patch_mischtDefinierteUndUndefinierteFelder() {
			final var dto = new OrtsteilPatchRequest();
			dto.ortsteil = JsonNullable.of("Mitte");
			final var entity = createEntity(1L);
			entity.idOrt = 42L;

			mapper.patch(dto, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("ortsteil", "Mitte")
					.hasFieldOrPropertyWithValue("idOrt", 42L);
		}

		@Test
		@DisplayName("id wird durch patch nicht verändert")
		void patch_idWirdNichtVeraendert() {
			final var dto = new OrtsteilPatchRequest();
			final var entity = createEntity(42L);

			mapper.patch(dto, entity);

			assertThat(entity.id).isEqualTo(42L);
		}

		@Test
		@DisplayName("schluesselOrtsteil wird durch patch nicht verändert — wird extern gesetzt")
		void patch_schluesselOrtsteilWirdNichtGemappt() {
			final var dto = new OrtsteilPatchRequest();
			final var entity = createEntity(1L);
			entity.schluesselOrtsteil = "ORIGINAL";

			mapper.patch(dto, entity);

			assertThat(entity.schluesselOrtsteil).isEqualTo("ORIGINAL");
		}
	}
}
