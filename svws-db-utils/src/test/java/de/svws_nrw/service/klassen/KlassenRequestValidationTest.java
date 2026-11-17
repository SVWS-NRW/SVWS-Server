package de.svws_nrw.service.klassen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.openapitools.jackson.nullable.JsonNullableModule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

/**
 * Test für die Validierungsannotationen und die JSON-Deserialisierung von {@link KlassenCreateRequest} und {@link KlassenPatchRequest}.
 */
class KlassenRequestValidationTest {

	private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JsonNullableModule());

	private static KlassenCreateRequest createValidCreateRequest() {
		final var request = new KlassenCreateRequest();
		request.idSchuljahresabschnitt = 11L;
		request.kuerzel = "5a";
		request.idJahrgang = 4L;

		return request;
	}

	private static void assertValidationError(final Runnable validierung, final String feld) {
		assertThatThrownBy(validierung::run)
				.isInstanceOfSatisfying(ApiOperationException.class, e -> assertThat(e.getStatus()).isEqualTo(Response.Status.BAD_REQUEST))
				.hasMessageContaining(feld);
	}

	@Nested
	@DisplayName("KlassenCreateRequest")
	class CreateRequest {

		@Test
		@DisplayName("Ein Request mit den Pflichtfeldern ist gültig")
		void pflichtfelder() {
			assertThatCode(() -> BeanValidator.validate(createValidCreateRequest())).doesNotThrowAnyException();
		}

		@Test
		@DisplayName("Ein Request mit allen optionalen Feldern im gültigen Bereich ist gültig")
		void alleFelder() {
			final var request = createValidCreateRequest();
			request.parallelitaet = "Z";
			request.sortierung = 0;
			request.teilstandort = "A";
			request.beschreibung = "x".repeat(150);
			request.noteneingabeGesperrt = true;
			request.istJva = false;

			assertThatCode(() -> BeanValidator.validate(request)).doesNotThrowAnyException();
		}

		@Test
		@DisplayName("Ein Request ohne optionale Felder ist gültig, null steht für den Standardwert")
		void optionaleFelderNull() {
			final var request = createValidCreateRequest();
			request.idJahrgang = null;

			assertThatCode(() -> BeanValidator.validate(request)).doesNotThrowAnyException();
			assertThat(request)
					.extracting(r -> r.parallelitaet, r -> r.sortierung, r -> r.teilstandort, r -> r.beschreibung, r -> r.noteneingabeGesperrt,
							r -> r.verwendungAnkreuzkompetenzen, r -> r.beginnSommersemester, r -> r.istJva, r -> r.istReformpaedagogik)
					.containsOnlyNulls();
		}

		@Test
		@DisplayName("Ein fehlender Schuljahresabschnitt ist ungültig")
		void abschnittFehlt() {
			final var request = createValidCreateRequest();
			request.idSchuljahresabschnitt = null;

			assertValidationError(() -> BeanValidator.validate(request), "idSchuljahresabschnitt");
		}

		@Test
		@DisplayName("Ein fehlendes oder leeres Kürzel ist ungültig")
		void kuerzelFehlt() {
			final var ohne = createValidCreateRequest();
			ohne.kuerzel = null;
			final var leer = createValidCreateRequest();
			leer.kuerzel = " ";

			assertValidationError(() -> BeanValidator.validate(ohne), "kuerzel");
			assertValidationError(() -> BeanValidator.validate(leer), "kuerzel");
		}

		@Test
		@DisplayName("Ein Kürzel mit 15 Zeichen ist gültig, mit 16 Zeichen ungültig")
		void kuerzelLaenge() {
			final var gueltig = createValidCreateRequest();
			gueltig.kuerzel = "k".repeat(15);
			final var zuLang = createValidCreateRequest();
			zuLang.kuerzel = "k".repeat(16);

			assertThatCode(() -> BeanValidator.validate(gueltig)).doesNotThrowAnyException();
			assertValidationError(() -> BeanValidator.validate(zuLang), "kuerzel");
		}

		@Test
		@DisplayName("Parallelität: Nur ein Großbuchstabe A bis Z ist gültig")
		void parallelitaet() {
			for (final String ungueltig : List.of("a", "AB", "1", "", "ä")) {
				final var request = createValidCreateRequest();
				request.parallelitaet = ungueltig;

				assertValidationError(() -> BeanValidator.validate(request), "parallelitaet");
			}
		}

		@Test
		@DisplayName("Sortierung: Negative Werte sind ungültig")
		void sortierung() {
			final var request = createValidCreateRequest();
			request.sortierung = -1;

			assertValidationError(() -> BeanValidator.validate(request), "sortierung");
		}

		@Test
		@DisplayName("Teilstandort: Leer, nur Leerzeichen oder länger als 1 Zeichen ist ungültig")
		void teilstandort() {
			for (final String ungueltig : List.of("", " ", "AB")) {
				final var request = createValidCreateRequest();
				request.teilstandort = ungueltig;

				assertValidationError(() -> BeanValidator.validate(request), "teilstandort");
			}
		}

		@Test
		@DisplayName("Beschreibung: Mehr als 150 Zeichen sind ungültig, leer ist gültig")
		void beschreibung() {
			final var zuLang = createValidCreateRequest();
			zuLang.beschreibung = "x".repeat(151);
			final var leer = createValidCreateRequest();
			leer.beschreibung = "";

			assertValidationError(() -> BeanValidator.validate(zuLang), "beschreibung");
			assertThatCode(() -> BeanValidator.validate(leer)).doesNotThrowAnyException();
		}

		@Test
		@DisplayName("Deserialisierung: Unbekannte Eigenschaften (auch klassenLeitungen) werden ignoriert")
		void unbekannteEigenschaften() throws JsonProcessingException {
			final var json = """
					{"idSchuljahresabschnitt": 11, "kuerzel": "5a", "idJahrgang": 4, "klassenLeitungen": [1, 2], "gibtEsNicht": true}
					""";

			final var request = MAPPER.readValue(json, KlassenCreateRequest.class);

			assertThat(request.idSchuljahresabschnitt).isEqualTo(11L);
			assertThat(request.kuerzel).isEqualTo("5a");
			assertThat(request.idJahrgang).isEqualTo(4L);
		}

		@Test
		@DisplayName("Deserialisierung: Ein fehlender und ein null-Jahrgang ergeben beide null")
		void jahrgangFehltOderNull() throws JsonProcessingException {
			final var ohne = MAPPER.readValue("{\"idSchuljahresabschnitt\": 11, \"kuerzel\": \"5a\"}", KlassenCreateRequest.class);
			final var mitNull = MAPPER.readValue("{\"idSchuljahresabschnitt\": 11, \"kuerzel\": \"5a\", \"idJahrgang\": null}", KlassenCreateRequest.class);

			assertThat(ohne.idJahrgang).isNull();
			assertThat(mitNull.idJahrgang).isNull();
		}

	}

	@Nested
	@DisplayName("KlassenPatchRequest")
	class PatchRequest {

		@Test
		@DisplayName("Ein leerer Patch ist gültig")
		void leererPatch() {
			assertThatCode(() -> BeanValidator.validate(new KlassenPatchRequest())).doesNotThrowAnyException();
		}

		@Test
		@DisplayName("Kürzel: Leer, nur Leerzeichen, null oder länger als 15 Zeichen ist ungültig")
		void kuerzel() {
			for (final String ungueltig : Arrays.asList("", " ", null, "k".repeat(16))) {
				final var request = new KlassenPatchRequest();
				request.kuerzel = JsonNullable.of(ungueltig);

				assertValidationError(() -> BeanValidator.validate(request), "kuerzel");
			}
		}

		@Test
		@DisplayName("Kürzel: Ein Kürzel mit 15 Zeichen ist gültig")
		void kuerzelGueltig() {
			final var request = new KlassenPatchRequest();
			request.kuerzel = JsonNullable.of("k".repeat(15));

			assertThatCode(() -> BeanValidator.validate(request)).doesNotThrowAnyException();
		}

		@Test
		@DisplayName("Jahrgang: Explizit null ist gültig (jahrgangsübergreifend)")
		void jahrgangNull() {
			final var request = new KlassenPatchRequest();
			request.idJahrgang = JsonNullable.of(null);

			assertThatCode(() -> BeanValidator.validate(request)).doesNotThrowAnyException();
		}

		@Test
		@DisplayName("Klassenleitungen: Eine leere Liste ist gültig, null als Liste oder als Element ist ungültig")
		void klassenLeitungen() {
			final var leer = new KlassenPatchRequest();
			leer.klassenLeitungen = JsonNullable.of(List.of());
			final var gueltig = new KlassenPatchRequest();
			gueltig.klassenLeitungen = JsonNullable.of(List.of(1L, 2L));
			final var ohneListe = new KlassenPatchRequest();
			ohneListe.klassenLeitungen = JsonNullable.of(null);
			final var nullElement = new KlassenPatchRequest();
			nullElement.klassenLeitungen = JsonNullable.of(Arrays.asList(1L, null));

			assertThatCode(() -> BeanValidator.validate(leer)).doesNotThrowAnyException();
			assertThatCode(() -> BeanValidator.validate(gueltig)).doesNotThrowAnyException();
			assertValidationError(() -> BeanValidator.validate(ohneListe), "klassenLeitungen");
			assertValidationError(() -> BeanValidator.validate(nullElement), "klassenLeitungen");
		}

		@Test
		@DisplayName("Deserialisierung: Nicht änderbare und unbekannte Eigenschaften werden ignoriert")
		void unbekannteEigenschaften() throws JsonProcessingException {
			final var json = """
					{"id": 5, "idSchuljahresabschnitt": 11, "kuerzelVorgaengerklasse": "4a", "pruefungsordnung": "x", "kuerzel": "5b", "gibtEsNicht": 1}
					""";

			final var request = MAPPER.readValue(json, KlassenPatchRequest.class);

			assertThat(request.kuerzel.get()).isEqualTo("5b");
			assertThat(request.idJahrgang.isPresent()).isFalse();
		}

		@Test
		@DisplayName("Deserialisierung: Fehlende Felder sind undefined, explizit null ist present mit Wert null")
		void undefinedOderNull() throws JsonProcessingException {
			final var request = MAPPER.readValue("{\"idJahrgang\": null, \"beschreibung\": null, \"klassenLeitungen\": [3, 1]}", KlassenPatchRequest.class);

			assertThat(request.idJahrgang.isPresent()).isTrue();
			assertThat(request.idJahrgang.get()).isNull();
			assertThat(request.beschreibung.isPresent()).isTrue();
			assertThat(request.beschreibung.get()).isNull();
			assertThat(request.klassenLeitungen.get()).containsExactly(3L, 1L);
			assertThat(request.kuerzel.isPresent()).isFalse();
			assertThat(request.sortierung.isPresent()).isFalse();
		}

	}

}
