package de.svws_nrw.mapper.schule.teilstandort;

import de.svws_nrw.db.dto.current.schild.schule.DTOTeilstandorte;
import de.svws_nrw.service.schule.teilstandort.TeilstandortCreateRequest;
import de.svws_nrw.service.schule.teilstandort.TeilstandortPatchRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;

class TeilstandortMapperTest {

	private final TeilstandortMapper mapper = TeilstandortMapper.INSTANCE;

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private DTOTeilstandorte createEntity() {
		final var entity = new DTOTeilstandorte("A");
		entity.PLZ = "53840";
		entity.Ort = "Troisdorf";
		entity.Strassenname = "Hauptstraße";
		entity.HausNr = "12";
		entity.HausNrZusatz = "b";
		entity.Bemerkung = "Nebengebäude";
		entity.Kuerzel = "HS";
		return entity;
	}

	private TeilstandortCreateRequest createRequest() {
		final var request = new TeilstandortCreateRequest();
		request.adrMerkmal = "A";
		request.plz = "53840";
		request.ort = "Troisdorf";
		request.strassenname = "Hauptstraße";
		request.hausNr = "12";
		request.hausNrZusatz = "b";
		request.bemerkung = "Nebengebäude";
		request.kuerzel = "HS";
		return request;
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
			final var result = mapper.toApi(createEntity());

			assertThat(result)
					.hasFieldOrPropertyWithValue("adrMerkmal", "A")
					.hasFieldOrPropertyWithValue("plz", "53840")
					.hasFieldOrPropertyWithValue("ort", "Troisdorf")
					.hasFieldOrPropertyWithValue("strassenname", "Hauptstraße")
					.hasFieldOrPropertyWithValue("hausNr", "12")
					.hasFieldOrPropertyWithValue("hausNrZusatz", "b")
					.hasFieldOrPropertyWithValue("bemerkung", "Nebengebäude")
					.hasFieldOrPropertyWithValue("kuerzel", "HS");
		}

		@Test
		@DisplayName("Mappt optionale Felder als null, wenn sie in der Entity null sind")
		void toApi_mapptOptionaleFelderAlsNull() {
			final var result = mapper.toApi(new DTOTeilstandorte("A"));

			assertThat(result.adrMerkmal).isEqualTo("A");
			assertThat(result.plz).isNull();
			assertThat(result.ort).isNull();
			assertThat(result.strassenname).isNull();
			assertThat(result.hausNr).isNull();
			assertThat(result.hausNrZusatz).isNull();
			assertThat(result.bemerkung).isNull();
			assertThat(result.kuerzel).isNull();
		}

		@Test
		@DisplayName("Setzt referenziertInAnderenTabellen nicht (bleibt false, wird im Service gesetzt)")
		void toApi_referenziertInAnderenTabellenBleibtFalse() {
			final var result = mapper.toApi(createEntity());

			assertThat(result.referenziertInAnderenTabellen).isFalse();
		}

		@Test
		@DisplayName("Gibt null zurück bei null-Entity")
		void toApi_null() {
			assertThat(mapper.toApi(null)).isNull();
		}
	}

	// -------------------------------------------------------------------------
	// toDomain
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("toDomain")
	class ToDomain {

		@Test
		@DisplayName("Mappt alle Felder des CreateRequests auf die Entity")
		void toDomain_mapptAlleFelder() {
			final var result = mapper.toDomain(createRequest());

			assertThat(result)
					.hasFieldOrPropertyWithValue("AdrMerkmal", "A")
					.hasFieldOrPropertyWithValue("PLZ", "53840")
					.hasFieldOrPropertyWithValue("Ort", "Troisdorf")
					.hasFieldOrPropertyWithValue("Strassenname", "Hauptstraße")
					.hasFieldOrPropertyWithValue("HausNr", "12")
					.hasFieldOrPropertyWithValue("HausNrZusatz", "b")
					.hasFieldOrPropertyWithValue("Bemerkung", "Nebengebäude")
					.hasFieldOrPropertyWithValue("Kuerzel", "HS");
		}

		@Test
		@DisplayName("Mappt optionale Felder als null, wenn sie im Request null sind")
		void toDomain_mapptOptionaleFelderAlsNull() {
			final var request = new TeilstandortCreateRequest();
			request.adrMerkmal = "B";

			final var result = mapper.toDomain(request);

			assertThat(result.AdrMerkmal).isEqualTo("B");
			assertThat(result.PLZ).isNull();
			assertThat(result.Ort).isNull();
			assertThat(result.Strassenname).isNull();
			assertThat(result.HausNr).isNull();
			assertThat(result.HausNrZusatz).isNull();
			assertThat(result.Bemerkung).isNull();
			assertThat(result.Kuerzel).isNull();
		}

		@Test
		@DisplayName("Gibt null zurück bei null-Request")
		void toDomain_null() {
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
		@DisplayName("Aktualisiert alle gesetzten Felder")
		void patch_aktualisiertAlleFelder() {
			final var dto = new TeilstandortPatchRequest();
			dto.plz = JsonNullable.of("40213");
			dto.ort = JsonNullable.of("Düsseldorf");
			dto.strassenname = JsonNullable.of("Muster Str.");
			dto.hausNr = JsonNullable.of("4");
			dto.hausNrZusatz = JsonNullable.of("a-c");
			dto.bemerkung = JsonNullable.of("Hauptgebäude");
			dto.kuerzel = JsonNullable.of("Hauptst.");
			final var entity = createEntity();

			mapper.patch(dto, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("PLZ", "40213")
					.hasFieldOrPropertyWithValue("Ort", "Düsseldorf")
					.hasFieldOrPropertyWithValue("Strassenname", "Muster Str.")
					.hasFieldOrPropertyWithValue("HausNr", "4")
					.hasFieldOrPropertyWithValue("HausNrZusatz", "a-c")
					.hasFieldOrPropertyWithValue("Bemerkung", "Hauptgebäude")
					.hasFieldOrPropertyWithValue("Kuerzel", "Hauptst.");
		}

		@Test
		@DisplayName("Lässt undefined Felder unverändert")
		void patch_laesstUndefinierteFelderUnveraendert() {
			final var entity = createEntity();

			mapper.patch(new TeilstandortPatchRequest(), entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("PLZ", "53840")
					.hasFieldOrPropertyWithValue("Ort", "Troisdorf")
					.hasFieldOrPropertyWithValue("Strassenname", "Hauptstraße")
					.hasFieldOrPropertyWithValue("HausNr", "12")
					.hasFieldOrPropertyWithValue("HausNrZusatz", "b")
					.hasFieldOrPropertyWithValue("Bemerkung", "Nebengebäude")
					.hasFieldOrPropertyWithValue("Kuerzel", "HS");
		}

		@Test
		@DisplayName("Mischt gesetzte und undefined Felder korrekt")
		void patch_mischtGesetzteUndUndefinierteFelder() {
			final var dto = new TeilstandortPatchRequest();
			dto.plz = JsonNullable.of("40213");
			final var entity = createEntity();

			mapper.patch(dto, entity);

			assertThat(entity.PLZ).isEqualTo("40213");
			assertThat(entity.Ort).isEqualTo("Troisdorf");
		}

		@Test
		@DisplayName("Setzt ein Feld auf null, wenn null explizit gesendet wird")
		void patch_nullLoeschtFeld() {
			final var dto = new TeilstandortPatchRequest();
			dto.plz = JsonNullable.of(null);
			final var entity = createEntity();

			mapper.patch(dto, entity);

			assertThat(entity.PLZ).isNull();
			assertThat(entity.Ort).isEqualTo("Troisdorf");
		}

		@Test
		@DisplayName("Übernimmt einen leeren String unverändert als leeren String")
		void patch_leererStringBleibtLeer() {
			final var dto = new TeilstandortPatchRequest();
			dto.ort = JsonNullable.of("");
			final var entity = createEntity();

			mapper.patch(dto, entity);

			assertThat(entity.Ort).isEmpty();
		}

		@Test
		@DisplayName("Ändert das Adressmerkmal nie")
		void patch_aendertAdrMerkmalNicht() {
			final var dto = new TeilstandortPatchRequest();
			dto.plz = JsonNullable.of("40213");
			final var entity = createEntity();

			mapper.patch(dto, entity);

			assertThat(entity.AdrMerkmal).isEqualTo("A");
		}
	}
}
