package de.svws_nrw.asd.types.schule;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Teste den Core-Type Floskelgruppenart")
class FloskelgruppenartTest {

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("Anzahl der Coretypes")
	void anzahlCoretypesTest() {
		assertThat(Floskelgruppenart.data().getWerte()).hasSize(11);
	}

}
