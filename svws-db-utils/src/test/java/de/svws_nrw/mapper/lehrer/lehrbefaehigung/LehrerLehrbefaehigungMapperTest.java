package de.svws_nrw.mapper.lehrer.lehrbefaehigung;

import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungEintrag;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramtBefaehigung;
import de.svws_nrw.service.lehrer.lehrbefaehigung.LehrerLehrbefaehigungCreateRequest;
import de.svws_nrw.service.lehrer.lehrbefaehigung.LehrerLehrbefaehigungPatchRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;

class LehrerLehrbefaehigungMapperTest {

	private final LehrerLehrbefaehigungMapper mapper = LehrerLehrbefaehigungMapper.INSTANCE;

	private static final long ID = 7L;
	private static final long ID_LEHRAMT = 4712L;
	private static final long ID_LEHRBEFAEHIGUNG = 4713L;
	private static final long ID_ANERKENNUNGSGRUND = 4714L;

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private DTOLehrerPersonaldatenLehramtBefaehigung createEntity() {
		final var entity = new DTOLehrerPersonaldatenLehramtBefaehigung(
				ID,
				ID_LEHRAMT,
				ID_LEHRBEFAEHIGUNG);
		entity.idAnerkennungsgrund = ID_ANERKENNUNGSGRUND;
		return entity;
	}

	private LehrerLehrbefaehigungCreateRequest createCreateRequest() {
		final var request = new LehrerLehrbefaehigungCreateRequest();
		request.idLehramt = ID_LEHRAMT;
		request.idLehrbefaehigung = ID_LEHRBEFAEHIGUNG;
		request.idAnerkennungsgrund = ID_ANERKENNUNGSGRUND;
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
			final var entity = createEntity();

			assertThat(mapper.toApi(entity))
					.isNotNull()
					.isInstanceOf(LehrerLehrbefaehigungEintrag.class)
					.hasFieldOrPropertyWithValue("id", ID)
					.hasFieldOrPropertyWithValue("idLehramt", ID_LEHRAMT)
					.hasFieldOrPropertyWithValue("idLehrbefaehigung", ID_LEHRBEFAEHIGUNG)
					.hasFieldOrPropertyWithValue("idAnerkennungsgrund", ID_ANERKENNUNGSGRUND);
		}

		@Test
		@DisplayName("Mappt einen fehlenden Anerkennungsgrund als null")
		void toApi_mapptFehlendenAnerkennungsgrundAlsNull() {
			final var entity = createEntity();
			entity.idAnerkennungsgrund = null;

			final var result = mapper.toApi(entity);

			assertThat(result.idAnerkennungsgrund).isNull();
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
			final var request = createCreateRequest();

			final var result = mapper.toDomain(request);

			assertThat(result)
					.isNotNull()
					.hasFieldOrPropertyWithValue("idLehramt", ID_LEHRAMT)
					.hasFieldOrPropertyWithValue("idLehrbefaehigung", ID_LEHRBEFAEHIGUNG)
					.hasFieldOrPropertyWithValue("idAnerkennungsgrund", ID_ANERKENNUNGSGRUND);
		}

		@Test
		@DisplayName("id wird nicht gemappt und bleibt 0")
		void toDomain_idWirdNichtGemappt() {
			final var request = createCreateRequest();

			final var result = mapper.toDomain(request);

			assertThat(result.id).isZero();
		}

		@Test
		@DisplayName("Mappt einen fehlenden Anerkennungsgrund als null")
		void toDomain_mapptFehlendenAnerkennungsgrundAlsNull() {
			final var request = createCreateRequest();
			request.idAnerkennungsgrund = null;

			final var result = mapper.toDomain(request);

			assertThat(result.idAnerkennungsgrund).isNull();
		}
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("patch")
	class Patch {

		@Test
		@DisplayName("Aktualisiert alle definierten Felder korrekt")
		void patch_aktualisiertAlleDefiniertenFelder() {
			final var request = new LehrerLehrbefaehigungPatchRequest();
			request.idLehramt = JsonNullable.of(100L);
			request.idLehrbefaehigung = JsonNullable.of(200L);
			request.idAnerkennungsgrund = JsonNullable.of(300L);

			final var entity = createEntity();

			mapper.patch(request, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("id", ID)
					.hasFieldOrPropertyWithValue("idLehramt", 100L)
					.hasFieldOrPropertyWithValue("idLehrbefaehigung", 200L)
					.hasFieldOrPropertyWithValue("idAnerkennungsgrund", 300L);
		}

		@Test
		@DisplayName("Lässt undefined Felder unverändert")
		void patch_laesstUndefinedFelderUnveraendert() {
			final var request = new LehrerLehrbefaehigungPatchRequest();
			final var entity = createEntity();

			mapper.patch(request, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("id", ID)
					.hasFieldOrPropertyWithValue("idLehramt", ID_LEHRAMT)
					.hasFieldOrPropertyWithValue("idLehrbefaehigung", ID_LEHRBEFAEHIGUNG)
					.hasFieldOrPropertyWithValue("idAnerkennungsgrund", ID_ANERKENNUNGSGRUND);
		}

		@Test
		@DisplayName("Mischt definierte und undefined Felder korrekt")
		void patch_mischtDefinierteUndUndefinedFelder() {
			final var request = new LehrerLehrbefaehigungPatchRequest();
			request.idLehrbefaehigung = JsonNullable.of(999L);

			final var entity = createEntity();

			mapper.patch(request, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("idLehramt", ID_LEHRAMT)
					.hasFieldOrPropertyWithValue("idLehrbefaehigung", 999L)
					.hasFieldOrPropertyWithValue("idAnerkennungsgrund", ID_ANERKENNUNGSGRUND);
		}

		@Test
		@DisplayName("Setzt den Anerkennungsgrund bei explizitem null auf null")
		void patch_setztAnerkennungsgrundBeiExplizitemNullAufNull() {
			final var request = new LehrerLehrbefaehigungPatchRequest();
			request.idAnerkennungsgrund = JsonNullable.of(null);

			final var entity = createEntity();

			mapper.patch(request, entity);

			assertThat(entity.idAnerkennungsgrund).isNull();
		}

		@Test
		@DisplayName("id wird durch patch nicht verändert")
		void patch_idWirdNichtVeraendert() {
			final var request = new LehrerLehrbefaehigungPatchRequest();
			request.idLehramt = JsonNullable.of(100L);
			request.idLehrbefaehigung = JsonNullable.of(200L);
			request.idAnerkennungsgrund = JsonNullable.of(300L);

			final var entity = createEntity();

			mapper.patch(request, entity);

			assertThat(entity.id).isEqualTo(ID);
		}
	}
}
