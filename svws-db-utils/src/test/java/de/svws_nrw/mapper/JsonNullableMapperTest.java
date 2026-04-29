package de.svws_nrw.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;

class JsonNullableMapperTest {

	private JsonNullableMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new JsonNullableMapper() {
			// Interface mit Default-Methoden
		};
	}

	// --- isPresent ---

	@Test
	void isPresent_gibtTrue_wennWertVorhanden() {
		assertThat(mapper.isPresent(JsonNullable.of("wert"))).isTrue();
	}

	@Test
	void isPresent_gibtTrue_wennPresentAberNull() {
		// JsonNullable.of(null) bedeutet: explizit auf null gesetzt (PATCH-Semantik)
		assertThat(mapper.isPresent(JsonNullable.of(null))).isTrue();
	}

	@Test
	void isPresent_gibtFalse_wennUndefined() {
		assertThat(mapper.isPresent(JsonNullable.undefined())).isFalse();
	}

	@Test
	void isPresent_gibtFalse_wennNull() {
		assertThat(mapper.isPresent(null)).isFalse();
	}

	// --- unwrap ---

	@Test
	void unwrap_gibtWert_wennPresent() {
		assertThat(mapper.unwrap(JsonNullable.of("hallo"))).isEqualTo("hallo");
	}

	@Test
	void unwrap_gibtNull_wennInputNull() {
		assertThat(mapper.<String>unwrap(null)).isNull();
	}

	@Test
	void unwrap_funktioniert_mitVerschiedenenTypen() {
		assertThat(mapper.unwrap(JsonNullable.of(42))).isEqualTo(42);
		assertThat(mapper.unwrap(JsonNullable.of(true))).isTrue();
	}
}
