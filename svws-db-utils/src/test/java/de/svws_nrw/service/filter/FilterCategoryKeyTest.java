package de.svws_nrw.service.filter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("FilterCategoryKey")
class FilterCategoryKeyTest {

	@ParameterizedTest(name = "''{0}'' wird akzeptiert")
	@ValueSource(strings = { "schueler.individualdaten", "schueler.adresse.kontakt", "schueler2.daten1", "a.b" })
	@DisplayName("akzeptiert mindestens zwei Segmente aus Kleinbuchstaben und Ziffern, die mit einem Buchstaben beginnen")
	void acceptsValidKeys(final String value) {
		final FilterCategoryKey key = new FilterCategoryKey(value);

		assertThat(key.value()).isEqualTo(value);
	}

	@ParameterizedTest(name = "''{0}'' wird abgelehnt")
	@ValueSource(strings = { "", ".", "schueler", "schueler.", ".individualdaten", "schueler..individualdaten", "schueler.1daten", "1schueler.daten",
			"Schueler.individualdaten", "schueler.Individualdaten", "schueler.individual_daten", "schueler.individual-daten",
			"schueler. individualdaten", "schüler.individualdaten", "schueler.individualdaten\n" })
	@DisplayName("lehnt zu wenige, leere oder ungültige Segmente mit IllegalArgumentException ab")
	void rejectsInvalidKeys(final String value) {
		assertThatThrownBy(() -> new FilterCategoryKey(value))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("Ungültiger Schlüssel für eine Filterkategorie")
				.hasMessageContaining("'" + value + "'");
	}

	@Test
	@DisplayName("wirft bei null eine NullPointerException mit deutscher Meldung")
	void rejectsNull() {
		assertThatThrownBy(() -> new FilterCategoryKey(null))
				.isInstanceOf(NullPointerException.class)
				.hasMessage("Der Schlüssel einer Filterkategorie darf nicht null sein.");
	}

	@Test
	@DisplayName("Schlüssel mit gleichem Wert sind gleich")
	void equalsByValue() {
		assertThat(new FilterCategoryKey("schueler.individualdaten")).isEqualTo(new FilterCategoryKey("schueler.individualdaten"))
				.hasSameHashCodeAs(new FilterCategoryKey("schueler.individualdaten"))
				.isNotEqualTo(new FilterCategoryKey("schueler.andere"));
	}

	@Test
	@DisplayName("die in FilterCategoryKeys definierten Konstanten sind gültig")
	void definedConstantsAreValid() {
		assertThat(FilterCategoryKeys.SCHUELER_INDIVIDUALDATEN.value()).isEqualTo("schueler.individualdaten");
	}
}
