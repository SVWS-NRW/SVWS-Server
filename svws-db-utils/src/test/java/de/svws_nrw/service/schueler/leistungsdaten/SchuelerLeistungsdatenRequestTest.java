package de.svws_nrw.service.schueler.leistungsdaten;

import java.util.function.Consumer;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openapitools.jackson.nullable.JsonNullable;
import org.openapitools.jackson.nullable.JsonNullableModule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Prüft die Validierung und die Hilfsmethoden der Requests für Schülerleistungsdaten.
 */
class SchuelerLeistungsdatenRequestTest {

	private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JsonNullableModule());

	private static SchuelerLeistungsdatenCreateRequest createCreateRequest() {
		final var request = new SchuelerLeistungsdatenCreateRequest();
		request.lernabschnittID = 1L;
		request.fachID = 2L;
		return request;
	}

	static Stream<Arguments> ungueltigeCreateWerte() {
		return Stream.of(
				Arguments.of("koopSchule", (Consumer<SchuelerLeistungsdatenCreateRequest>) r -> r.koopSchule = 99999),
				Arguments.of("koopSchule", (Consumer<SchuelerLeistungsdatenCreateRequest>) r -> r.koopSchule = 1000000),
				Arguments.of("wochenstunden", (Consumer<SchuelerLeistungsdatenCreateRequest>) r -> r.wochenstunden = -1),
				Arguments.of("wochenstunden", (Consumer<SchuelerLeistungsdatenCreateRequest>) r -> r.wochenstunden = 1000),
				Arguments.of("zusatzkraftWochenstunden", (Consumer<SchuelerLeistungsdatenCreateRequest>) r -> r.zusatzkraftWochenstunden = 1000),
				Arguments.of("gewichtungAllgemeinbildend", (Consumer<SchuelerLeistungsdatenCreateRequest>) r -> r.gewichtungAllgemeinbildend = 10),
				Arguments.of("fehlstundenGesamt", (Consumer<SchuelerLeistungsdatenCreateRequest>) r -> r.fehlstundenGesamt = 100000),
				Arguments.of("fehlstundenUnentschuldigt", (Consumer<SchuelerLeistungsdatenCreateRequest>) r -> r.fehlstundenUnentschuldigt = -1),
				Arguments.of("umfangLernstandsbericht", (Consumer<SchuelerLeistungsdatenCreateRequest>) r -> r.umfangLernstandsbericht = "VR")
		);
	}

	static Stream<Arguments> gueltigeCreateWerte() {
		return Stream.of(
				Arguments.of((Consumer<SchuelerLeistungsdatenCreateRequest>) r -> r.koopSchule = 100000),
				Arguments.of((Consumer<SchuelerLeistungsdatenCreateRequest>) r -> r.koopSchule = 999999),
				Arguments.of((Consumer<SchuelerLeistungsdatenCreateRequest>) r -> r.wochenstunden = 0),
				Arguments.of((Consumer<SchuelerLeistungsdatenCreateRequest>) r -> r.wochenstunden = 999),
				Arguments.of((Consumer<SchuelerLeistungsdatenCreateRequest>) r -> r.gewichtungAllgemeinbildend = 9),
				Arguments.of((Consumer<SchuelerLeistungsdatenCreateRequest>) r -> r.fehlstundenGesamt = 99999),
				Arguments.of((Consumer<SchuelerLeistungsdatenCreateRequest>) r -> r.umfangLernstandsbericht = "V"),
				Arguments.of((Consumer<SchuelerLeistungsdatenCreateRequest>) r -> r.umfangLernstandsbericht = ""),
				Arguments.of((Consumer<SchuelerLeistungsdatenCreateRequest>) r -> r.mahndatum = "keinDatum")
		);
	}

	static Stream<Arguments> ungueltigePatchWerte() {
		return Stream.of(
				Arguments.of("koopSchule", (Consumer<SchuelerLeistungsdatenPatchRequest>) r -> r.koopSchule = JsonNullable.of(99999)),
				Arguments.of("koopSchule", (Consumer<SchuelerLeistungsdatenPatchRequest>) r -> r.koopSchule = JsonNullable.of(1000000)),
				Arguments.of("wochenstunden", (Consumer<SchuelerLeistungsdatenPatchRequest>) r -> r.wochenstunden = JsonNullable.of(-1)),
				Arguments.of("wochenstunden", (Consumer<SchuelerLeistungsdatenPatchRequest>) r -> r.wochenstunden = JsonNullable.of(1000)),
				Arguments.of("zusatzkraftWochenstunden", (Consumer<SchuelerLeistungsdatenPatchRequest>) r -> r.zusatzkraftWochenstunden = JsonNullable.of(1000)),
				Arguments.of("gewichtungAllgemeinbildend", (Consumer<SchuelerLeistungsdatenPatchRequest>) r -> r.gewichtungAllgemeinbildend = JsonNullable.of(10)),
				Arguments.of("fehlstundenGesamt", (Consumer<SchuelerLeistungsdatenPatchRequest>) r -> r.fehlstundenGesamt = JsonNullable.of(100000)),
				Arguments.of("fehlstundenUnentschuldigt", (Consumer<SchuelerLeistungsdatenPatchRequest>) r -> r.fehlstundenUnentschuldigt = JsonNullable.of(-1)),
				Arguments.of("umfangLernstandsbericht", (Consumer<SchuelerLeistungsdatenPatchRequest>) r -> r.umfangLernstandsbericht = JsonNullable.of("VR")),
				Arguments.of("istZP10oderZKEF", (Consumer<SchuelerLeistungsdatenPatchRequest>) r -> r.istZP10oderZKEF = JsonNullable.of(null)),
				Arguments.of("aufZeugnis", (Consumer<SchuelerLeistungsdatenPatchRequest>) r -> r.aufZeugnis = JsonNullable.of(null)),
				Arguments.of("istGemahnt", (Consumer<SchuelerLeistungsdatenPatchRequest>) r -> r.istGemahnt = JsonNullable.of(null)),
				Arguments.of("istEpochal", (Consumer<SchuelerLeistungsdatenPatchRequest>) r -> r.istEpochal = JsonNullable.of(null))
		);
	}

	@Nested
	@DisplayName("SchuelerLeistungsdatenCreateRequest")
	class CreateRequest {

		@Test
		@DisplayName("validate | Pflichtfelder gesetzt")
		void validate_pflichtfelder_success() {
			assertThatCode(() -> BeanValidator.validate(createCreateRequest())).doesNotThrowAnyException();
		}

		@Test
		@DisplayName("validate | lernabschnittID fehlt")
		void validate_lernabschnittIDFehlt_throwsBadRequest() {
			final var request = createCreateRequest();
			request.lernabschnittID = null;

			assertThatThrownBy(() -> BeanValidator.validate(request))
					.isInstanceOf(ApiOperationException.class)
					.hasMessageContaining("lernabschnittID")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}

		@Test
		@DisplayName("validate | fachID fehlt")
		void validate_fachIDFehlt_throwsBadRequest() {
			final var request = createCreateRequest();
			request.fachID = null;

			assertThatThrownBy(() -> BeanValidator.validate(request))
					.isInstanceOf(ApiOperationException.class)
					.hasMessageContaining("fachID")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}

		@ParameterizedTest(name = "validate | ungültiger Wert für {0}")
		@MethodSource("de.svws_nrw.service.schueler.leistungsdaten.SchuelerLeistungsdatenRequestTest#ungueltigeCreateWerte")
		void validate_ungueltigerWert_throwsBadRequest(final String attribut, final Consumer<SchuelerLeistungsdatenCreateRequest> setter) {
			final var request = createCreateRequest();
			setter.accept(request);

			assertThatThrownBy(() -> BeanValidator.validate(request))
					.isInstanceOf(ApiOperationException.class)
					.hasMessageContaining(attribut)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}

		@ParameterizedTest(name = "validate | gültiger Wert {index}")
		@MethodSource("de.svws_nrw.service.schueler.leistungsdaten.SchuelerLeistungsdatenRequestTest#gueltigeCreateWerte")
		void validate_gueltigerWert_success(final Consumer<SchuelerLeistungsdatenCreateRequest> setter) {
			final var request = createCreateRequest();
			setter.accept(request);

			assertThatCode(() -> BeanValidator.validate(request)).doesNotThrowAnyException();
		}

		@Test
		@DisplayName("Jackson | unbekannte Attribute werden ignoriert, nicht angegebene Attribute bleiben null")
		void jackson_ignoriertUnbekannteAttribute() throws Exception {
			final var json = "{\"lernabschnittID\":1,\"fachID\":2,\"id\":5,\"noteBerufsabschluss\":\"1\",\"note\":\"2\"}";

			final var request = MAPPER.readValue(json, SchuelerLeistungsdatenCreateRequest.class);

			assertThat(request)
					.hasFieldOrPropertyWithValue("lernabschnittID", 1L)
					.hasFieldOrPropertyWithValue("fachID", 2L)
					.hasFieldOrPropertyWithValue("note", "2")
					.hasFieldOrPropertyWithValue("kursID", null);
		}
	}

	@Nested
	@DisplayName("SchuelerLeistungsdatenPatchRequest")
	class PatchRequest {

		@Test
		@DisplayName("validate | leerer Patch")
		void validate_leererPatch_success() {
			assertThatCode(() -> BeanValidator.validate(new SchuelerLeistungsdatenPatchRequest())).doesNotThrowAnyException();
		}

		@ParameterizedTest(name = "validate | ungültiger Wert für {0}")
		@MethodSource("de.svws_nrw.service.schueler.leistungsdaten.SchuelerLeistungsdatenRequestTest#ungueltigePatchWerte")
		void validate_ungueltigerWert_throwsBadRequest(final String attribut, final Consumer<SchuelerLeistungsdatenPatchRequest> setter) {
			final var request = new SchuelerLeistungsdatenPatchRequest();
			setter.accept(request);

			assertThatThrownBy(() -> BeanValidator.validate(request))
					.isInstanceOf(ApiOperationException.class)
					.hasMessageContaining(attribut)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}

		@Test
		@DisplayName("validate | lernabschnittID und fachID dürfen nicht null sein")
		void validate_idsNull_throwsBadRequest() {
			final var request = new SchuelerLeistungsdatenPatchRequest();
			request.lernabschnittID = JsonNullable.of(null);
			request.fachID = JsonNullable.of(null);

			assertThatThrownBy(() -> BeanValidator.validate(request))
					.isInstanceOf(ApiOperationException.class)
					.hasMessageContaining("lernabschnittID")
					.hasMessageContaining("fachID")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}

		@Test
		@DisplayName("Jackson | unbekannte Attribute werden ignoriert, null wird als vorhandener Wert gelesen")
		void jackson_ignoriertUnbekannteAttribute() throws Exception {
			final var json = "{\"id\":5,\"noteBerufsabschluss\":\"1\",\"kursID\":null,\"note\":\"2\"}";

			final var request = MAPPER.readValue(json, SchuelerLeistungsdatenPatchRequest.class);

			assertThat(request)
					.hasFieldOrPropertyWithValue("note", JsonNullable.of("2"))
					.satisfies(r -> assertThat(r.kursID.isPresent()).isTrue())
					.satisfies(r -> assertThat(r.kursID.get()).isNull())
					.satisfies(r -> assertThat(r.fachID.isPresent()).isFalse());
		}
	}

}
