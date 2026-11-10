package de.svws_nrw.service.lehrer.stammdaten;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openapitools.jackson.nullable.JsonNullable;
import org.openapitools.jackson.nullable.JsonNullableModule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Prüft die Validierung des {@link LehrerStammdatenCreateRequest} und des {@link LehrerStammdatenPatchRequest}.
 */
class LehrerStammdatenRequestTest {

	private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JsonNullableModule());

	private static LehrerStammdatenCreateRequest createRequest() {
		final var request = new LehrerStammdatenCreateRequest();
		request.kuerzel = "MUS";
		request.personalTyp = "LEHRKRAFT";
		request.nachname = "Mustermann";
		request.vorname = "Max";
		request.geschlecht = 3;

		return request;
	}

	@Test
	@DisplayName("Create | gültiger Request mit Pflichtfeldern")
	void create_gueltig() {
		assertThatCode(() -> BeanValidator.validate(createRequest())).doesNotThrowAnyException();
	}

	@ParameterizedTest(name = "Create | ungültiges Kürzel \"{0}\"")
	@NullAndEmptySource
	@ValueSource(strings = { " ", "ZU-LANGES-KUERZEL" })
	void create_kuerzelUngueltig(final String kuerzel) {
		final var request = createRequest();
		request.kuerzel = kuerzel;

		assertThatThrownBy(() -> BeanValidator.validate(request))
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("kuerzel")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("Create | fehlendes Geschlecht und ungültiges Geburtsdatum")
	void create_geschlechtUndGeburtsdatum() {
		final var request = createRequest();
		request.geschlecht = null;
		request.geburtsdatum = "11.11.1911";

		assertThatThrownBy(() -> BeanValidator.validate(request))
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("geschlecht")
				.hasMessageContaining("geburtsdatum");
	}

	@Test
	@DisplayName("Create | Sichtbarkeit und Statistik-Relevanz sind ohne Angabe true")
	void create_standardwerte() {
		assertThat(createRequest())
				.returns(true, r -> r.istSichtbar)
				.returns(true, r -> r.istRelevantFuerStatistik);
	}

	@Test
	@DisplayName("Create | explizites null bei Sichtbarkeit und Statistik-Relevanz ist ungültig")
	void create_nullBeiSichtbarkeit() {
		final var request = createRequest();
		request.istSichtbar = null;
		request.istRelevantFuerStatistik = null;

		assertThatThrownBy(() -> BeanValidator.validate(request))
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("istSichtbar")
				.hasMessageContaining("istRelevantFuerStatistik")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("Create | Lesefelder und foto werden beim Einlesen ignoriert")
	void create_lesefelderIgnoriert() throws JsonProcessingException {
		final var json = "{\"kuerzel\":\"MUS\",\"id\":1,\"leitungsfunktionen\":[],\"kuerzelStatistik\":\"ALBE\",\"foto\":\"abc\"}";

		assertThat(MAPPER.readValue(json, LehrerStammdatenCreateRequest.class).kuerzel).isEqualTo("MUS");
	}

	@Test
	@DisplayName("Create | ein unbekanntes Attribut führt beim Einlesen zu einem Fehler")
	void create_unbekanntesAttribut() {
		assertThatThrownBy(() -> MAPPER.readValue("{\"kuerzel\":\"MUS\",\"unbekannt\":1}", LehrerStammdatenCreateRequest.class))
				.isInstanceOf(UnrecognizedPropertyException.class);
	}

	@Test
	@DisplayName("Patch | Lesefelder werden beim Einlesen ignoriert")
	void patch_lesefelderIgnoriert() throws JsonProcessingException {
		final var json = "{\"kuerzel\":\"MUS\",\"id\":1,\"leitungsfunktionen\":[],\"kuerzelStatistik\":\"ALBE\"}";

		assertThat(MAPPER.readValue(json, LehrerStammdatenPatchRequest.class).kuerzel).isEqualTo(JsonNullable.of("MUS"));
	}

	@Test
	@DisplayName("Patch | ein unbekanntes Attribut führt beim Einlesen zu einem Fehler")
	void patch_unbekanntesAttribut() {
		assertThatThrownBy(() -> MAPPER.readValue("{\"kuerzel\":\"MUS\",\"unbekannt\":1}", LehrerStammdatenPatchRequest.class))
				.isInstanceOf(UnrecognizedPropertyException.class);
	}

	@Test
	@DisplayName("Patch | leerer Request ist gültig")
	void patch_leer() {
		assertThatCode(() -> BeanValidator.validate(new LehrerStammdatenPatchRequest())).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Patch | explizites null bei optionalen Feldern ist gültig")
	void patch_nullBeiOptionalenFeldern() {
		final var request = new LehrerStammdatenPatchRequest();
		request.titel = JsonNullable.of(null);
		request.geburtsdatum = JsonNullable.of(null);
		request.wohnortID = JsonNullable.of(null);
		request.foto = JsonNullable.of(null);

		assertThatCode(() -> BeanValidator.validate(request)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Patch | null bei Pflichtfeldern ist ungültig")
	void patch_nullBeiPflichtfeldern() {
		final var request = new LehrerStammdatenPatchRequest();
		request.nachname = JsonNullable.of(null);
		request.istSichtbar = JsonNullable.of(null);

		assertThatThrownBy(() -> BeanValidator.validate(request))
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("nachname")
				.hasMessageContaining("istSichtbar")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("Patch | zu lange Zeichenketten sind ungültig")
	void patch_zuLang() {
		final var request = new LehrerStammdatenPatchRequest();
		request.anrede = JsonNullable.of("x".repeat(11));

		assertThatThrownBy(() -> BeanValidator.validate(request))
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("anrede");
	}

}
