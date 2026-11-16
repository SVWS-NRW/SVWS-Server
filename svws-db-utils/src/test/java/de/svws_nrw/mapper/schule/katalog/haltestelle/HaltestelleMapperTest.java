package de.svws_nrw.mapper.schule.katalog.haltestelle;

import de.svws_nrw.db.dto.current.schild.katalog.DTOHaltestellen;
import de.svws_nrw.service.schule.katalog.haltestelle.HaltestelleCreateRequest;
import de.svws_nrw.service.schule.katalog.haltestelle.HaltestellePatchRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;

class HaltestelleMapperTest {

	private final HaltestelleMapper mapper = HaltestelleMapper.INSTANCE;

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private DTOHaltestellen createEntity(final long id) {
		return new DTOHaltestellen(id, "RE7 Hauptbahnhof");
	}

	private HaltestelleCreateRequest createRequest() {
		final var dto = new HaltestelleCreateRequest();
		dto.bezeichnung = "RE7 Hauptbahnhof";
		dto.entfernungSchule = 7.5;
		dto.sortierung = 3;
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
		@DisplayName("Mappt alle Felder korrekt")
		void toApi_mapptAlleFelder() {
			final var entity = createEntity(7L);
			entity.entfernungSchule = 7.5;
			entity.sortierung = 3;
			entity.istSichtbar = true;

			final var result = mapper.toApi(entity);

			assertThat(result)
					.hasFieldOrPropertyWithValue("id", 7L)
					.hasFieldOrPropertyWithValue("bezeichnung", "RE7 Hauptbahnhof")
					.hasFieldOrPropertyWithValue("entfernungSchule", 7.5)
					.hasFieldOrPropertyWithValue("sortierung", 3)
					.hasFieldOrPropertyWithValue("istSichtbar", true);
		}

		@Test
		@DisplayName("Mappt entfernungSchule als null bei null")
		void toApi_mapptEntfernungSchuleNullBeiNull() {
			final var entity = createEntity(1L);
			entity.entfernungSchule = null;

			final var result = mapper.toApi(entity);

			assertThat(result.entfernungSchule).isNull();
		}

		@Test
		@DisplayName("Mappt sortierung als 0 bei null")
		void toApi_mapptSortierungBeiNull() {
			final var entity = createEntity(1L);
			entity.sortierung = null;

			final var result = mapper.toApi(entity);

			assertThat(result.sortierung).isZero();
		}

		@Test
		@DisplayName("Mappt istSichtbar als false bei null")
		void toApi_mapptIstSichtbarFalseBeiNull() {
			final var entity = createEntity(1L);
			entity.istSichtbar = null;

			final var result = mapper.toApi(entity);

			assertThat(result.istSichtbar).isFalse();
		}

		@Test
		@DisplayName("Gibt null zurück bei null")
		void toApi_nullEntity() {
			assertThat(mapper.toApi(null)).isNull();
		}

		@Test
		@DisplayName("referenziertInAnderenTabellen ist nach toApi immer false — wird extern gesetzt")
		void toApi_referenziertInAnderenTabellenIstFalse() {
			final var entity = createEntity(1L);

			final var result = mapper.toApi(entity);

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
		@DisplayName("Mappt alle Felder korrekt")
		void toDomain_mapptAlleFelder() {
			final var dto = createRequest();

			final var result = mapper.toDomain(dto);

			assertThat(result)
					.hasFieldOrPropertyWithValue("bezeichnung", "RE7 Hauptbahnhof")
					.hasFieldOrPropertyWithValue("entfernungSchule", 7.5)
					.hasFieldOrPropertyWithValue("sortierung", 3)
					.hasFieldOrPropertyWithValue("istSichtbar", true);
		}

		@Test
		@DisplayName("Optionale Felder bleiben null, wenn sie im Request fehlen")
		void toDomain_optionaleFelderBleibenNull() {
			final var dto = new HaltestelleCreateRequest();
			dto.bezeichnung = "RE7 Hauptbahnhof";

			final var result = mapper.toDomain(dto);

			assertThat(result.bezeichnung).isEqualTo("RE7 Hauptbahnhof");
			assertThat(result.entfernungSchule).isNull();
			assertThat(result.sortierung).isNull();
			assertThat(result.istSichtbar).isNull();
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
		@DisplayName("Gibt null zurück bei null")
		void toDomain_nullRequest() {
			assertThat(mapper.toDomain(null)).isNull();
		}
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("patch")
	class Patch {

		@Test
		@DisplayName("Aktualisiert alle Felder korrekt")
		void patch_aktualisiertAlleFelder() {
			final var dto = new HaltestellePatchRequest();
			dto.bezeichnung = JsonNullable.of("Bus 5 Rathaus");
			dto.entfernungSchule = JsonNullable.of(2.0);
			dto.sortierung = JsonNullable.of(9);
			dto.istSichtbar = JsonNullable.of(false);
			final var entity = createEntity(1L);

			mapper.patch(dto, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("bezeichnung", "Bus 5 Rathaus")
					.hasFieldOrPropertyWithValue("entfernungSchule", 2.0)
					.hasFieldOrPropertyWithValue("sortierung", 9)
					.hasFieldOrPropertyWithValue("istSichtbar", false);
		}

		@Test
		@DisplayName("Lässt undefined Felder unverändert")
		void patch_laesstUndefinierteFelderUnveraendert() {
			final var dto = new HaltestellePatchRequest();
			final var entity = createEntity(1L);
			entity.entfernungSchule = 7.5;
			entity.sortierung = 42;
			entity.istSichtbar = true;

			mapper.patch(dto, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("bezeichnung", "RE7 Hauptbahnhof")
					.hasFieldOrPropertyWithValue("entfernungSchule", 7.5)
					.hasFieldOrPropertyWithValue("sortierung", 42)
					.hasFieldOrPropertyWithValue("istSichtbar", true);
		}

		@Test
		@DisplayName("Mischt definierte und undefined Felder korrekt")
		void patch_mischtDefinierteUndUndefinierteFelder() {
			final var dto = new HaltestellePatchRequest();
			dto.bezeichnung = JsonNullable.of("Bus 5 Rathaus");
			final var entity = createEntity(1L);
			entity.entfernungSchule = 7.5;

			mapper.patch(dto, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("bezeichnung", "Bus 5 Rathaus")
					.hasFieldOrPropertyWithValue("entfernungSchule", 7.5);
		}

		@Test
		@DisplayName("Übernimmt ein explizites null bei entfernungSchule")
		void patch_uebernimmtExplizitesNullBeiEntfernungSchule() {
			final var dto = new HaltestellePatchRequest();
			dto.entfernungSchule = JsonNullable.of(null);
			final var entity = createEntity(1L);
			entity.entfernungSchule = 7.5;

			mapper.patch(dto, entity);

			assertThat(entity.entfernungSchule).isNull();
		}

		@Test
		@DisplayName("id wird durch patch nicht verändert")
		void patch_idWirdNichtVeraendert() {
			final var dto = new HaltestellePatchRequest();
			dto.bezeichnung = JsonNullable.of("Bus 5 Rathaus");
			final var entity = createEntity(42L);

			mapper.patch(dto, entity);

			assertThat(entity.id).isEqualTo(42L);
		}

		@Test
		@DisplayName("istAenderbar wird durch patch nicht verändert")
		void patch_istAenderbarWirdNichtVeraendert() {
			final var dto = new HaltestellePatchRequest();
			dto.bezeichnung = JsonNullable.of("Bus 5 Rathaus");
			final var entity = createEntity(1L);
			entity.istAenderbar = true;

			mapper.patch(dto, entity);

			assertThat(entity.istAenderbar).isTrue();
		}

		@Test
		@DisplayName("Tut nichts bei null-Request")
		void patch_nullRequest() {
			final var entity = createEntity(1L);

			mapper.patch(null, entity);

			assertThat(entity.bezeichnung).isEqualTo("RE7 Hauptbahnhof");
		}
	}
}
