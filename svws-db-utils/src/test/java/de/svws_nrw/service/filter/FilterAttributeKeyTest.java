package de.svws_nrw.service.filter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("FilterAttributeKey")
class FilterAttributeKeyTest {

	@ParameterizedTest(name = "''{0}'' wird akzeptiert")
	@ValueSource(strings = { "schueler.wohnort", "schueler.adresse.plz", "schueler2.wohnort1", "a.b" })
	@DisplayName("akzeptiert mindestens zwei Segmente aus Kleinbuchstaben und Ziffern, die mit einem Buchstaben beginnen")
	void acceptsValidKeys(final String value) {
		final FilterAttributeKey key = new FilterAttributeKey(value);

		assertThat(key.value()).isEqualTo(value);
	}

	@ParameterizedTest(name = "''{0}'' wird abgelehnt")
	@ValueSource(strings = { "", ".", "schueler", "schueler.", ".wohnort", "schueler..wohnort", "schueler.1abc", "1schueler.wohnort",
			"Schueler.wohnort", "schueler.Wohnort", "schueler.wohn_ort", "schueler.wohn-ort", "schueler. wohnort", "schüler.wohnort",
			"schueler.wohnort\n" })
	@DisplayName("lehnt zu wenige, leere oder ungültige Segmente mit IllegalArgumentException ab")
	void rejectsInvalidKeys(final String value) {
		assertThatThrownBy(() -> new FilterAttributeKey(value))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("Ungültiger Schlüssel für ein Filterattribut")
				.hasMessageContaining("'" + value + "'");
	}

	@Test
	@DisplayName("wirft bei null eine NullPointerException mit deutscher Meldung")
	void rejectsNull() {
		assertThatThrownBy(() -> new FilterAttributeKey(null))
				.isInstanceOf(NullPointerException.class)
				.hasMessage("Der Schlüssel eines Filterattributes darf nicht null sein.");
	}

	@Test
	@DisplayName("Schlüssel mit gleichem Wert sind gleich")
	void equalsByValue() {
		assertThat(new FilterAttributeKey("schueler.wohnort")).isEqualTo(new FilterAttributeKey("schueler.wohnort"))
				.hasSameHashCodeAs(new FilterAttributeKey("schueler.wohnort"))
				.isNotEqualTo(new FilterAttributeKey("schueler.status"));
	}

	@Test
	@DisplayName("die in FilterAttributeKeys definierten Konstanten sind gültig")
	void definedConstantsAreValid() {
		assertThat(FilterAttributeKeys.SCHUELER_STATUS.value()).isEqualTo("schueler.status");
		assertThat(FilterAttributeKeys.SCHUELER_GESCHLECHT.value()).isEqualTo("schueler.geschlecht");
		assertThat(FilterAttributeKeys.SCHUELER_WOHNORT.value()).isEqualTo("schueler.wohnort");
	}
}
