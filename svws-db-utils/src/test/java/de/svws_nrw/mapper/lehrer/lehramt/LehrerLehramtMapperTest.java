package de.svws_nrw.mapper.lehrer.lehramt;

import de.svws_nrw.asd.data.lehrer.LehrerFachrichtungEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungEintrag;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramt;
import de.svws_nrw.service.lehrer.lehramt.LehrerLehramtCreateRequest;
import de.svws_nrw.service.lehrer.lehramt.LehrerLehramtPatchRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LehrerLehramtMapperTest {

	private final LehrerLehramtMapper mapper = LehrerLehramtMapper.INSTANCE;

	private static final long ID                  = 1L;
	private static final long ID_LEHRER           = 4711L;
	private static final long ID_KATALOG_LEHRAMT  = 4712L;
	private static final long ID_ANERKENNUNGSGRUND = 4713L;

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private DTOLehrerPersonaldatenLehramt createEntity() {
		final var entity = new DTOLehrerPersonaldatenLehramt(ID, ID_LEHRER);
		entity.idKatalogLehramt = ID_KATALOG_LEHRAMT;
		entity.idAnerkennungsgrund = ID_ANERKENNUNGSGRUND;
		return entity;
	}

	private LehrerLehramtCreateRequest createCreateRequest() {
		final var request = new LehrerLehramtCreateRequest();
		request.idLehrer = ID_LEHRER;
		request.idKatalogLehramt = ID_KATALOG_LEHRAMT;
		request.idAnerkennungsgrund = ID_ANERKENNUNGSGRUND;
		return request;
	}

	private LehrerFachrichtungEintrag createFachrichtungEintrag() {
		final var eintrag = new LehrerFachrichtungEintrag();
		eintrag.id = 10L;
		eintrag.idLehramt = ID;
		eintrag.idFachrichtung = 20L;
		return eintrag;
	}

	private LehrerLehrbefaehigungEintrag createLehrbefaehigungEintrag() {
		final var eintrag = new LehrerLehrbefaehigungEintrag();
		eintrag.id = 30L;
		eintrag.idLehramt = ID;
		eintrag.idLehrbefaehigung = 40L;
		return eintrag;
	}

	// -------------------------------------------------------------------------
	// toApi
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("toApi")
	class ToApi {

		@Test
		@DisplayName("Mappt alle Basisfelder korrekt")
		void toApi_mapptAlleBasisfelder() {
			final var entity = createEntity();
			final var ctx = LehrerLehramtMappingContext.empty();

			assertThat(mapper.toApi(entity, ctx))
					.isNotNull()
					.isInstanceOf(LehrerLehramtEintrag.class)
					.hasFieldOrPropertyWithValue("id", ID)
					.hasFieldOrPropertyWithValue("idLehrer", ID_LEHRER)
					.hasFieldOrPropertyWithValue("idKatalogLehramt", ID_KATALOG_LEHRAMT)
					.hasFieldOrPropertyWithValue("idAnerkennungsgrund", ID_ANERKENNUNGSGRUND);
		}

		@Test
		@DisplayName("Mappt einen fehlenden Anerkennungsgrund als null")
		void toApi_mapptFehlendenAnerkennungsgrundAlsNull() {
			final var entity = createEntity();
			entity.idAnerkennungsgrund = null;
			final var ctx = LehrerLehramtMappingContext.empty();

			final var result = mapper.toApi(entity, ctx);

			assertThat(result.idAnerkennungsgrund).isNull();
		}

		@Test
		@DisplayName("Befüllt fachrichtungen aus dem Kontext")
		void toApi_befuelltFachrichtungenAusKontext() {
			final var entity = createEntity();
			final var fachrichtung = createFachrichtungEintrag();
			final var ctx = new LehrerLehramtMappingContext(List.of(fachrichtung), List.of());

			final var result = mapper.toApi(entity, ctx);

			assertThat(result.fachrichtungen)
					.hasSize(1)
					.containsExactly(fachrichtung);
		}

		@Test
		@DisplayName("Befüllt lehrbefaehigungen aus dem Kontext")
		void toApi_befuelltLehrbefaehigungenAusKontext() {
			final var entity = createEntity();
			final var lehrbefaehigung = createLehrbefaehigungEintrag();
			final var ctx = new LehrerLehramtMappingContext(List.of(), List.of(lehrbefaehigung));

			final var result = mapper.toApi(entity, ctx);

			assertThat(result.lehrbefaehigungen)
					.hasSize(1)
					.containsExactly(lehrbefaehigung);
		}

		@Test
		@DisplayName("Gibt leere Listen zurück bei leerem Kontext")
		void toApi_gibtLeereListenBeiLeeremKontext() {
			final var entity = createEntity();
			final var ctx = LehrerLehramtMappingContext.empty();

			final var result = mapper.toApi(entity, ctx);

			assertThat(result.fachrichtungen).isEmpty();
			assertThat(result.lehrbefaehigungen).isEmpty();
		}

		@Test
		@DisplayName("Befüllt fachrichtungen und lehrbefaehigungen gleichzeitig aus dem Kontext")
		void toApi_befuelltBeideListenAusKontext() {
			final var entity = createEntity();
			final var fachrichtung = createFachrichtungEintrag();
			final var lehrbefaehigung = createLehrbefaehigungEintrag();
			final var ctx = new LehrerLehramtMappingContext(List.of(fachrichtung), List.of(lehrbefaehigung));

			final var result = mapper.toApi(entity, ctx);

			assertThat(result.fachrichtungen).containsExactly(fachrichtung);
			assertThat(result.lehrbefaehigungen).containsExactly(lehrbefaehigung);
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
					.hasFieldOrPropertyWithValue("idLehrer", ID_LEHRER)
					.hasFieldOrPropertyWithValue("idKatalogLehramt", ID_KATALOG_LEHRAMT)
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
			final var request = new LehrerLehramtPatchRequest();
			request.idLehrer = JsonNullable.of(100L);
			request.idKatalogLehramt = JsonNullable.of(200L);
			request.idAnerkennungsgrund = JsonNullable.of(300L);

			final var entity = createEntity();

			mapper.patch(request, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("id", ID)
					.hasFieldOrPropertyWithValue("idLehrer", 100L)
					.hasFieldOrPropertyWithValue("idKatalogLehramt", 200L)
					.hasFieldOrPropertyWithValue("idAnerkennungsgrund", 300L);
		}

		@Test
		@DisplayName("Lässt undefined Felder unverändert")
		void patch_laesstUndefinedFelderUnveraendert() {
			final var request = new LehrerLehramtPatchRequest();
			final var entity = createEntity();

			mapper.patch(request, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("id", ID)
					.hasFieldOrPropertyWithValue("idLehrer", ID_LEHRER)
					.hasFieldOrPropertyWithValue("idKatalogLehramt", ID_KATALOG_LEHRAMT)
					.hasFieldOrPropertyWithValue("idAnerkennungsgrund", ID_ANERKENNUNGSGRUND);
		}

		@Test
		@DisplayName("Mischt definierte und undefined Felder korrekt")
		void patch_mischtDefinierteUndUndefinedFelder() {
			final var request = new LehrerLehramtPatchRequest();
			request.idKatalogLehramt = JsonNullable.of(999L);

			final var entity = createEntity();

			mapper.patch(request, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("idLehrer", ID_LEHRER)
					.hasFieldOrPropertyWithValue("idKatalogLehramt", 999L)
					.hasFieldOrPropertyWithValue("idAnerkennungsgrund", ID_ANERKENNUNGSGRUND);
		}

		@Test
		@DisplayName("Setzt den Anerkennungsgrund bei explizitem null auf null")
		void patch_setztAnerkennungsgrundBeiExplizitemNullAufNull() {
			final var request = new LehrerLehramtPatchRequest();
			request.idAnerkennungsgrund = JsonNullable.of(null);

			final var entity = createEntity();

			mapper.patch(request, entity);

			assertThat(entity.idAnerkennungsgrund).isNull();
		}

		@Test
		@DisplayName("id wird durch patch nicht verändert")
		void patch_idWirdNichtVeraendert() {
			final var request = new LehrerLehramtPatchRequest();
			request.idLehrer = JsonNullable.of(100L);
			request.idKatalogLehramt = JsonNullable.of(200L);
			request.idAnerkennungsgrund = JsonNullable.of(300L);

			final var entity = createEntity();

			mapper.patch(request, entity);

			assertThat(entity.id).isEqualTo(ID);
		}
	}
}
