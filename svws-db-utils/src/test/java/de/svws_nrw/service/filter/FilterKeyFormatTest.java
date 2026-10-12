package de.svws_nrw.service.filter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("FilterKeyFormat")
class FilterKeyFormatTest {

	@ParameterizedTest(name = "''{0}'' ist gültig")
	@ValueSource(strings = { "schueler.wohnort", "schueler.adresse.plz", "schueler2.wohnort1", "a.b" })
	@DisplayName("isValid | akzeptiert mindestens zwei Segmente aus Kleinbuchstaben und Ziffern, die mit einem Buchstaben beginnen")
	void isValidAccepts(final String value) {
		assertThat(FilterKeyFormat.isValid(value)).isTrue();
	}

	@ParameterizedTest(name = "''{0}'' ist ungültig")
	@ValueSource(strings = { "", ".", "schueler", "schueler.", ".wohnort", "schueler..wohnort", "schueler.1abc", "1abc.wohnort", "Schueler.wohnort",
			"schueler.Wohnort", "schueler.wohn_ort", "schueler.wohn-ort", "schueler. wohnort", "schüler.wohnort", "schueler.wohnort\n" })
	@DisplayName("isValid | lehnt zu wenige, leere oder ungültige Segmente ab")
	void isValidRejects(final String value) {
		assertThat(FilterKeyFormat.isValid(value)).isFalse();
	}

	@Test
	@DisplayName("isValid | wirft NullPointerException bei null")
	void isValidNull() {
		assertThatThrownBy(() -> FilterKeyFormat.isValid(null)).isInstanceOf(NullPointerException.class);
	}

	@Test
	@DisplayName("isValid | verarbeitet sehr lange Schlüssel ohne StackOverflowError")
	void isValidLongInput() {
		final String valid = "a.b".repeat(1) + ".c".repeat(200_000);
		final String invalid = valid + ".";

		assertThatCode(() -> assertThat(FilterKeyFormat.isValid(valid)).isTrue()).doesNotThrowAnyException();
		assertThatCode(() -> assertThat(FilterKeyFormat.isValid(invalid)).isFalse()).doesNotThrowAnyException();
	}
}
