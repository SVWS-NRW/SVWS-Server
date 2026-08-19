package de.svws_nrw.mapper.lehrer.fachrichtung;

import de.svws_nrw.asd.data.lehrer.LehrerFachrichtungEintrag;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramtFachrichtung;
import de.svws_nrw.service.lehrer.fachrichtung.LehrerFachrichtungCreateRequest;
import de.svws_nrw.service.lehrer.fachrichtung.LehrerFachrichtungPatchRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;

class LehrerFachrichtungMapperTest {

	private final LehrerFachrichtungMapper mapper = LehrerFachrichtungMapper.INSTANCE;

	private static final long ID = 7L;
	private static final long ID_LEHRAMT = 4712L;
	private static final long ID_FACHRICHTUNG = 4713L;
	private static final long ID_ANERKENNUNGSGRUND = 4714L;

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private DTOLehrerPersonaldatenLehramtFachrichtung createEntity() {
		final var entity = new DTOLehrerPersonaldatenLehramtFachrichtung(
				ID,
				ID_LEHRAMT,
				ID_FACHRICHTUNG);
		entity.idAnerkennungsgrund = ID_ANERKENNUNGSGRUND;
		return entity;
	}

	private LehrerFachrichtungCreateRequest createCreateRequest() {
		final var request = new LehrerFachrichtungCreateRequest();
		request.idLehramt = ID_LEHRAMT;
		request.idFachrichtung = ID_FACHRICHTUNG;
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

			final LehrerFachrichtungEintrag result = mapper.toApi(entity);

			assertThat(result)
					.isNotNull()
					.isInstanceOf(LehrerFachrichtungEintrag.class)
					.hasFieldOrPropertyWithValue("id", ID)
					.hasFieldOrPropertyWithValue("idLehramt", ID_LEHRAMT)
					.hasFieldOrPropertyWithValue("idFachrichtung", ID_FACHRICHTUNG)
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
					.hasFieldOrPropertyWithValue("idFachrichtung", ID_FACHRICHTUNG)
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
			final var request = new LehrerFachrichtungPatchRequest();
			request.idLehramt = JsonNullable.of(100L);
			request.idFachrichtung = JsonNullable.of(200L);
			request.idAnerkennungsgrund = JsonNullable.of(300L);

			final var entity = createEntity();

			mapper.patch(request, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("id", ID)
					.hasFieldOrPropertyWithValue("idLehramt", 100L)
					.hasFieldOrPropertyWithValue("idFachrichtung", 200L)
					.hasFieldOrPropertyWithValue("idAnerkennungsgrund", 300L);
		}

		@Test
		@DisplayName("Lässt undefined Felder unverändert")
		void patch_laesstUndefinedFelderUnveraendert() {
			final var request = new LehrerFachrichtungPatchRequest();
			final var entity = createEntity();

			mapper.patch(request, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("id", ID)
					.hasFieldOrPropertyWithValue("idLehramt", ID_LEHRAMT)
					.hasFieldOrPropertyWithValue("idFachrichtung", ID_FACHRICHTUNG)
					.hasFieldOrPropertyWithValue("idAnerkennungsgrund", ID_ANERKENNUNGSGRUND);
		}

		@Test
		@DisplayName("Mischt definierte und undefined Felder korrekt")
		void patch_mischtDefinierteUndUndefinedFelder() {
			final var request = new LehrerFachrichtungPatchRequest();
			request.idFachrichtung = JsonNullable.of(999L);

			final var entity = createEntity();

			mapper.patch(request, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("idLehramt", ID_LEHRAMT)
					.hasFieldOrPropertyWithValue("idFachrichtung", 999L)
					.hasFieldOrPropertyWithValue("idAnerkennungsgrund", ID_ANERKENNUNGSGRUND);
		}

		@Test
		@DisplayName("Setzt den Anerkennungsgrund bei explizitem null auf null")
		void patch_setztAnerkennungsgrundBeiExplizitemNullAufNull() {
			final var request = new LehrerFachrichtungPatchRequest();
			request.idAnerkennungsgrund = JsonNullable.of(null);

			final var entity = createEntity();

			mapper.patch(request, entity);

			assertThat(entity.idAnerkennungsgrund).isNull();
		}

		@Test
		@DisplayName("id wird durch patch nicht verändert")
		void patch_idWirdNichtVeraendert() {
			final var request = new LehrerFachrichtungPatchRequest();
			request.idLehramt = JsonNullable.of(100L);
			request.idFachrichtung = JsonNullable.of(200L);
			request.idAnerkennungsgrund = JsonNullable.of(300L);

			final var entity = createEntity();

			mapper.patch(request, entity);

			assertThat(entity.id).isEqualTo(ID);
		}
	}
}
