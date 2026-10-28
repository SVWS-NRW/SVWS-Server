package de.svws_nrw.mapper.schule.katalog.ankreuzkompetenz;

import de.svws_nrw.db.dto.current.schild.grundschule.DTOAnkreuzkompetenz;
import de.svws_nrw.service.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzCreateRequest;
import de.svws_nrw.service.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzPatchRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AnkreuzkompetenzMapper")
class AnkreuzkompetenzMapperTest {

	private final AnkreuzkompetenzMapper mapper = AnkreuzkompetenzMapper.INSTANCE;

	private static DTOAnkreuzkompetenz createEntity() {
		final var entity = new DTOAnkreuzkompetenz(1L, 0, "Text");
		entity.idFach = 10L;
		entity.schulgliederung = "A02";
		entity.abschnitt = 1;
		entity.istAktiv = true;
		entity.istSichtbar = true;
		entity.fachSortierung = 5;
		entity.sortierung = 7;
		return entity;
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

			assertThat(result).isNotNull();
			assertThat(result.id).isEqualTo(1L);
			assertThat(result.istASV).isFalse();
			assertThat(result.idFach).isEqualTo(10L);
			assertThat(result.schulgliederung).isEqualTo("A02");
			assertThat(result.floskelText).isEqualTo("Text");
			assertThat(result.abschnitt).isEqualTo(1);
			assertThat(result.istAktiv).isTrue();
			assertThat(result.istSichtbar).isTrue();
			assertThat(result.fachSortierung).isEqualTo(5);
			assertThat(result.sortierung).isEqualTo(7);
		}

		@Test
		@DisplayName("Setzt istASV auf true bei 1 und blendet das Fach aus")
		void toApi_asvBlendetFachAus() {
			final var entity = createEntity();
			entity.istASV = 1;
			entity.idFach = 10L;

			final var result = mapper.toApi(entity);

			assertThat(result.istASV).isTrue();
			assertThat(result.idFach).isNull();
		}

		@Test
		@DisplayName("Setzt istASV auf false bei 0 und übernimmt das Fach")
		void toApi_ohneAsvUebernimmtFach() {
			final var entity = createEntity();
			entity.istASV = 0;

			final var result = mapper.toApi(entity);

			assertThat(result.istASV).isFalse();
			assertThat(result.idFach).isEqualTo(10L);
		}

		@Test
		@DisplayName("Lässt idFach null, wenn kein Fach gesetzt ist")
		void toApi_ohneFach() {
			final var entity = createEntity();
			entity.idFach = null;

			final var result = mapper.toApi(entity);

			assertThat(result.idFach).isNull();
		}

		@Test
		@DisplayName("Mappt floskelText als Leerstring bei null")
		void toApi_floskelTextDefault() {
			final var entity = createEntity();
			entity.floskelText = null;

			final var result = mapper.toApi(entity);

			assertThat(result.floskelText).isEmpty();
		}

		@Test
		@DisplayName("Mappt istAktiv und istSichtbar als false bei null")
		void toApi_booleanDefaults() {
			final var entity = createEntity();
			entity.istAktiv = null;
			entity.istSichtbar = null;

			final var result = mapper.toApi(entity);

			assertThat(result.istAktiv).isFalse();
			assertThat(result.istSichtbar).isFalse();
		}

		@Test
		@DisplayName("Mappt fachSortierung auf 0 und sortierung auf 32000 bei null")
		void toApi_sortierungDefaults() {
			final var entity = createEntity();
			entity.fachSortierung = null;
			entity.sortierung = null;

			final var result = mapper.toApi(entity);

			assertThat(result.fachSortierung).isZero();
			assertThat(result.sortierung).isEqualTo(32000);
		}

		@Test
		@DisplayName("Lässt schulgliederung und abschnitt bei null null")
		void toApi_optionaleFelderNull() {
			final var entity = createEntity();
			entity.schulgliederung = null;
			entity.abschnitt = null;

			final var result = mapper.toApi(entity);

			assertThat(result.schulgliederung).isNull();
			assertThat(result.abschnitt).isNull();
		}

		@Test
		@DisplayName("Setzt weder Referenz-Kennzeichen noch Jahrgangszuordnungen")
		void toApi_mapptNichtReferenzUndJahrgaenge() {
			final var result = mapper.toApi(createEntity());

			assertThat(result.referenziertInAnderenTabellen).isFalse();
			assertThat(result.jahrgaengezuordnung).isEmpty();
		}

		@Test
		@DisplayName("Gibt null zurück bei null")
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
		@DisplayName("Mappt alle Felder des Requests")
		void toDomain_mapptAlleFelder() {
			final var request = new AnkreuzkompetenzCreateRequest();
			request.istASV = false;
			request.idFach = 10L;
			request.floskelText = "Text";
			request.schulgliederung = "A02";
			request.abschnitt = 2;
			request.istAktiv = true;
			request.istSichtbar = false;
			request.fachSortierung = 5;
			request.sortierung = 7;

			final var result = mapper.toDomain(request);

			assertThat(result.istASV).isZero();
			assertThat(result.idFach).isEqualTo(10L);
			assertThat(result.floskelText).isEqualTo("Text");
			assertThat(result.schulgliederung).isEqualTo("A02");
			assertThat(result.abschnitt).isEqualTo(2);
			assertThat(result.istAktiv).isTrue();
			assertThat(result.istSichtbar).isFalse();
			assertThat(result.fachSortierung).isEqualTo(5);
			assertThat(result.sortierung).isEqualTo(7);
		}

		@Test
		@DisplayName("Wandelt istASV true in 1 um")
		void toDomain_asvTrue() {
			final var request = new AnkreuzkompetenzCreateRequest();
			request.istASV = true;
			request.floskelText = "Text";

			final var result = mapper.toDomain(request);

			assertThat(result.istASV).isEqualTo(1);
			assertThat(result.idFach).isNull();
		}

		@Test
		@DisplayName("Speichert nicht gesetzte optionale Felder als null")
		void toDomain_optionaleFelderBleibenNull() {
			final var request = new AnkreuzkompetenzCreateRequest();
			request.istASV = false;
			request.floskelText = "Text";

			final var result = mapper.toDomain(request);

			assertThat(result.idFach).isNull();
			assertThat(result.schulgliederung).isNull();
			assertThat(result.abschnitt).isNull();
			assertThat(result.istAktiv).isNull();
			assertThat(result.istSichtbar).isNull();
			assertThat(result.fachSortierung).isNull();
			assertThat(result.sortierung).isNull();
		}

		@Test
		@DisplayName("Gibt null zurück bei null")
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
		@DisplayName("Ändert nur gesetzte Felder")
		void patch_aendertNurGesetzteFelder() {
			final var entity = createEntity();
			final var request = new AnkreuzkompetenzPatchRequest();
			request.floskelText = JsonNullable.of("Neu");

			mapper.patch(request, entity);

			assertThat(entity.floskelText).isEqualTo("Neu");
			assertThat(entity.istASV).isZero();
			assertThat(entity.idFach).isEqualTo(10L);
			assertThat(entity.schulgliederung).isEqualTo("A02");
			assertThat(entity.abschnitt).isEqualTo(1);
			assertThat(entity.istAktiv).isTrue();
			assertThat(entity.istSichtbar).isTrue();
			assertThat(entity.fachSortierung).isEqualTo(5);
			assertThat(entity.sortierung).isEqualTo(7);
		}

		@Test
		@DisplayName("Mappt alle Felder des Requests")
		void patch_mapptAlleFelder() {
			final var entity = createEntity();
			final var request = new AnkreuzkompetenzPatchRequest();
			request.istASV = JsonNullable.of(false);
			request.idFach = JsonNullable.of(20L);
			request.floskelText = JsonNullable.of("Neu");
			request.schulgliederung = JsonNullable.of("A03");
			request.abschnitt = JsonNullable.of(2);
			request.istAktiv = JsonNullable.of(false);
			request.istSichtbar = JsonNullable.of(false);
			request.fachSortierung = JsonNullable.of(50);
			request.sortierung = JsonNullable.of(70);

			mapper.patch(request, entity);

			assertThat(entity.id).isEqualTo(1L);
			assertThat(entity.istASV).isZero();
			assertThat(entity.idFach).isEqualTo(20L);
			assertThat(entity.floskelText).isEqualTo("Neu");
			assertThat(entity.schulgliederung).isEqualTo("A03");
			assertThat(entity.abschnitt).isEqualTo(2);
			assertThat(entity.istAktiv).isFalse();
			assertThat(entity.istSichtbar).isFalse();
			assertThat(entity.fachSortierung).isEqualTo(50);
			assertThat(entity.sortierung).isEqualTo(70);
		}

		@ParameterizedTest(name = "istASV = {0} ergibt {1}")
		@ValueSource(booleans = { true, false })
		@DisplayName("Wandelt istASV in den int-Wert der Datenbank um")
		void patch_wandeltIstASVUm(final boolean istASV) {
			final var entity = createEntity();
			entity.istASV = istASV ? 0 : 1;
			final var request = new AnkreuzkompetenzPatchRequest();
			request.istASV = JsonNullable.of(istASV);

			mapper.patch(request, entity);

			assertThat(entity.istASV).isEqualTo(istASV ? 1 : 0);
		}

		@Test
		@DisplayName("Übernimmt ein explizites null bei nullable Feldern")
		void patch_uebernimmtExplizitesNull() {
			final var entity = createEntity();
			final var request = new AnkreuzkompetenzPatchRequest();
			request.idFach = JsonNullable.of(null);
			request.schulgliederung = JsonNullable.of(null);
			request.abschnitt = JsonNullable.of(null);
			request.istAktiv = JsonNullable.of(null);
			request.fachSortierung = JsonNullable.of(null);

			mapper.patch(request, entity);

			assertThat(entity.idFach).isNull();
			assertThat(entity.schulgliederung).isNull();
			assertThat(entity.abschnitt).isNull();
			assertThat(entity.istAktiv).isNull();
			assertThat(entity.fachSortierung).isNull();
		}

		@Test
		@DisplayName("Lässt nicht gesetzte Felder bei undefined unverändert")
		void patch_laesstUndefinedUnveraendert() {
			final var entity = createEntity();

			mapper.patch(new AnkreuzkompetenzPatchRequest(), entity);

			assertThat(entity.istASV).isZero();
			assertThat(entity.idFach).isEqualTo(10L);
			assertThat(entity.floskelText).isEqualTo("Text");
			assertThat(entity.schulgliederung).isEqualTo("A02");
			assertThat(entity.abschnitt).isEqualTo(1);
			assertThat(entity.istAktiv).isTrue();
			assertThat(entity.istSichtbar).isTrue();
			assertThat(entity.fachSortierung).isEqualTo(5);
			assertThat(entity.sortierung).isEqualTo(7);
		}

		@Test
		@DisplayName("Lässt die ID unverändert")
		void patch_aendertIdNicht() {
			final var entity = createEntity();

			mapper.patch(new AnkreuzkompetenzPatchRequest(), entity);

			assertThat(entity.id).isEqualTo(1L);
		}

		@Test
		@DisplayName("Lässt die Entity unverändert bei null-Request")
		void patch_nullRequest() {
			final var entity = createEntity();

			mapper.patch(null, entity);

			assertThat(entity.floskelText).isEqualTo("Text");
			assertThat(entity.idFach).isEqualTo(10L);
		}
	}
}
