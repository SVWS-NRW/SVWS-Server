package de.svws_nrw.service.schule.katalog.fachklasse;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class FachklasseKennungUtilsTest {

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	@Nested
	@DisplayName("toSchluessel")
	class ToSchluessel {

		@ParameterizedTest(name = "{0} -> {1}")
		@DisplayName("Entfernt den letzten Bindestrich der Kennung")
		@CsvSource({
			"10-179-02, 10-17902",
			"60-102-00, 60-10200",
			"210-148-18, 210-14818",
			"1017902, 1017902"
		})
		void toSchluessel_entferntLetztenBindestrich(final String kennung, final String erwartet) {
			assertThat(FachklasseKennungUtils.toSchluessel(kennung)).isEqualTo(erwartet);
		}

		@ParameterizedTest
		@DisplayName("Liefert null bei null oder leerer Kennung")
		@NullSource
		@ValueSource(strings = {"", " ", "   "})
		void toSchluessel_liefertNullBeiNullOderBlank(final String kennung) {
			assertThat(FachklasseKennungUtils.toSchluessel(kennung)).isNull();
		}

	}

	@Nested
	@DisplayName("findIdFachklasseStatistik")
	class FindIdFachklasseStatistik {

		@ParameterizedTest(name = "{0} im Schuljahr {1} -> {2}")
		@DisplayName("Liefert die ID des im Schuljahr gültigen Core-Type-Eintrags")
		@CsvSource({
			"60-102-00, 2000, 1867000",
			"210-148-18, 2024, 1429000"
		})
		void findIdFachklasseStatistik_liefertId(final String kennung, final int schuljahr, final long erwartet) {
			assertThat(FachklasseKennungUtils.findIdFachklasseStatistik(kennung, schuljahr)).isEqualTo(erwartet);
		}

		@Test
		@DisplayName("Liefert null bei unbekanntem Schlüssel")
		void findIdFachklasseStatistik_liefertNullBeiUnbekanntemSchluessel() {
			assertThat(FachklasseKennungUtils.findIdFachklasseStatistik("99-999-99", 2024)).isNull();
		}

		@ParameterizedTest
		@DisplayName("Liefert null bei null oder leerer Kennung")
		@NullSource
		@ValueSource(strings = {"", " "})
		void findIdFachklasseStatistik_liefertNullBeiNullOderBlank(final String kennung) {
			assertThat(FachklasseKennungUtils.findIdFachklasseStatistik(kennung, 2024)).isNull();
		}

	}

}
