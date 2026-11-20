package de.svws_nrw.mapper.schule.katalog.fahrschuelerart;

import de.svws_nrw.db.dto.current.schild.katalog.DTOFahrschuelerart;
import de.svws_nrw.service.schule.katalog.fahrschuelerart.FahrschuelerartCreateRequest;
import de.svws_nrw.service.schule.katalog.fahrschuelerart.FahrschuelerartPatchRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;

class FahrschuelerartMapperTest {

	private final FahrschuelerartMapper mapper = FahrschuelerartMapper.INSTANCE;

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private DTOFahrschuelerart createEntity(final long id) {
		return new DTOFahrschuelerart(id, "Bus");
	}

	private FahrschuelerartCreateRequest createRequest() {
		final var dto = new FahrschuelerartCreateRequest();
		dto.bezeichnung = "Bus";
		dto.sortierung = 5;
		dto.istSichtbar = false;
		dto.istAenderbar = false;
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
			entity.sortierung = 5;
			entity.istSichtbar = true;
			entity.istAenderbar = false;

			final var result = mapper.toApi(entity);

			assertThat(result)
					.returns(7L, f -> f.id)
					.returns("Bus", f -> f.bezeichnung)
					.returns(5, f -> f.sortierung)
					.returns(true, f -> f.istSichtbar)
					.returns(false, f -> f.istAenderbar);
		}

		@Test
		@DisplayName("Mappt sortierung als 0 bei null")
		void toApi_mapptSortierungBeiNullAlsZero() {
			final var entity = createEntity(1L);
			entity.sortierung = null;

			final var result = mapper.toApi(entity);

			assertThat(result).returns(0, f -> f.sortierung);
		}

		@Test
		@DisplayName("Mappt istSichtbar und istAenderbar als false bei null")
		void toApi_mapptBooleansFalseBeiNull() {
			final var entity = createEntity(1L);
			entity.istSichtbar = null;
			entity.istAenderbar = null;

			final var result = mapper.toApi(entity);

			assertThat(result)
					.returns(false, f -> f.istSichtbar)
					.returns(false, f -> f.istAenderbar);
		}

		@Test
		@DisplayName("referenziertInAnderenTabellen ist nach toApi immer false — wird extern gesetzt")
		void toApi_referenziertInAnderenTabellenIstFalse() {
			final var entity = createEntity(1L);

			final var result = mapper.toApi(entity);

			assertThat(result).returns(false, f -> f.referenziertInAnderenTabellen);
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
					.returns("Bus", f -> f.bezeichnung)
					.returns(5, f -> f.sortierung)
					.returns(false, f -> f.istSichtbar)
					.returns(false, f -> f.istAenderbar);
		}

		@Test
		@DisplayName("Übernimmt die Defaults des Requests")
		void toDomain_uebernimmtDefaultsDesRequests() {
			final var dto = new FahrschuelerartCreateRequest();
			dto.bezeichnung = "Bus";

			final var result = mapper.toDomain(dto);

			assertThat(result)
					.returns(32000, f -> f.sortierung)
					.returns(true, f -> f.istSichtbar)
					.returns(true, f -> f.istAenderbar);
		}

		@Test
		@DisplayName("id wird nicht gemappt — bleibt 0")
		void toDomain_idWirdNichtGemappt() {
			final var dto = createRequest();

			final var result = mapper.toDomain(dto);

			assertThat(result).returns(0L, f -> f.id);
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
			final var dto = new FahrschuelerartPatchRequest();
			dto.bezeichnung = JsonNullable.of("Zug");
			dto.sortierung = JsonNullable.of(2);
			dto.istSichtbar = JsonNullable.of(false);
			dto.istAenderbar = JsonNullable.of(false);
			final var entity = createEntity(1L);

			mapper.patch(dto, entity);

			assertThat(entity)
					.returns("Zug", f -> f.bezeichnung)
					.returns(2, f -> f.sortierung)
					.returns(false, f -> f.istSichtbar)
					.returns(false, f -> f.istAenderbar);
		}

		@Test
		@DisplayName("Lässt undefined Felder unverändert")
		void patch_laesstUndefinierteFelderUnveraendert() {
			final var dto = new FahrschuelerartPatchRequest();
			final var entity = createEntity(1L);
			entity.sortierung = 42;
			entity.istSichtbar = true;
			entity.istAenderbar = false;

			mapper.patch(dto, entity);

			assertThat(entity)
					.returns("Bus", f -> f.bezeichnung)
					.returns(42, f -> f.sortierung)
					.returns(true, f -> f.istSichtbar)
					.returns(false, f -> f.istAenderbar);
		}

		@Test
		@DisplayName("Mischt definierte und undefined Felder korrekt")
		void patch_mischtDefinierteUndUndefinierteFelder() {
			final var dto = new FahrschuelerartPatchRequest();
			dto.istAenderbar = JsonNullable.of(true);
			final var entity = createEntity(1L);
			entity.sortierung = 42;
			entity.istAenderbar = false;

			mapper.patch(dto, entity);

			assertThat(entity)
					.returns(true, f -> f.istAenderbar)
					.returns(42, f -> f.sortierung);
		}

		@Test
		@DisplayName("id wird durch patch nicht verändert")
		void patch_idWirdNichtVeraendert() {
			final var dto = new FahrschuelerartPatchRequest();
			final var entity = createEntity(42L);

			mapper.patch(dto, entity);

			assertThat(entity).returns(42L, f -> f.id);
		}
	}
}
