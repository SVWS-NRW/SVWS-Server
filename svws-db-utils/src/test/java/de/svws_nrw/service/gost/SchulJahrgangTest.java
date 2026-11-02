package de.svws_nrw.service.gost;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;

/**
 * Testet den Record mit den Daten eines Jahrgangs der Schule
 */
@DisplayName("SchulJahrgang")
class SchulJahrgangTest {

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	private static SchulJahrgang jahrgang(final String kuerzelASDJahrgang) {
		return new SchulJahrgang(kuerzelASDJahrgang, Schulgliederung.DEFAULT);
	}

	@ParameterizedTest(name = "Jahrgang {0}: gymnasiale Oberstufe = {1}")
	@CsvSource({ "EF, true", "Q1, true", "Q2, true", "09, false" })
	@DisplayName("istGymOb unterscheidet Jahrgänge der gymnasialen Oberstufe")
	void istGymOb(final String kuerzel, final boolean erwartet) {
		assertThat(jahrgang(kuerzel).istGymOb()).isEqualTo(erwartet);
	}

	@ParameterizedTest(name = "Gymnasium: Jahrgang {0} hat {1} Restjahre")
	@CsvSource({ "Q2, 1", "Q1, 2", "EF, 3", "09, 4" })
	@DisplayName("hatRestjahreBisAbitur am Gymnasium ohne Zuschlag")
	void hatRestjahreBisAbitur_gymnasium(final String kuerzel, final int restjahre) {
		assertThat(jahrgang(kuerzel).hatRestjahreBisAbitur(Schulform.GY, restjahre)).isTrue();
		assertThat(jahrgang(kuerzel).hatRestjahreBisAbitur(Schulform.GY, restjahre + 1)).isFalse();
	}

	@Test
	@DisplayName("hatRestjahreBisAbitur rechnet außerhalb von Gymnasien bei der Sekundarstufe I die Oberstufenzeit hinzu")
	void hatRestjahreBisAbitur_andereSchulform_mitZuschlag() {
		assertThat(jahrgang("09").hatRestjahreBisAbitur(Schulform.GE, 5)).isTrue();
		assertThat(jahrgang("09").hatRestjahreBisAbitur(Schulform.GE, 2)).isFalse();
		// Jahrgänge der gymnasialen Oberstufe erhalten keinen Zuschlag
		assertThat(jahrgang("Q1").hatRestjahreBisAbitur(Schulform.GE, 2)).isTrue();
	}

	@Test
	@DisplayName("hatRestjahreBisAbitur ist false, wenn sich die Restjahre nicht bestimmen lassen")
	void hatRestjahreBisAbitur_ohneRestjahre_false() {
		assertThat(jahrgang("ZZ").hatRestjahreBisAbitur(Schulform.GY, 0)).isFalse();
	}

}
