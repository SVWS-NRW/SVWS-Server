package de.svws_nrw.core.types.statkue;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.data.schule.SchulgliederungKatalogEintrag;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;


/**
 * Diese Klasse enthält die Testroutinen für Core-Type Schulgliederung.
 */
@DisplayName("Teste den Core-Type Schulgliederung")
class TestCoreTypeSchulgliederung {

	/**
	 * Initialisierung der Core-Types
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}


	/**
	 * Prüft, ob die Schulformen mindestens eine Schulgliederungen haben.
	 */
	@Test
	@DisplayName("Teste ob die Schulformen mindestens eine Schulgliederungen haben")
	void testSchulformMindestensEineSchulgliederung() {
		for (final Schulform sf : Schulform.values()) {
			if (Schulgliederung.getBySchuljahrAndSchulform(2022, sf).size() <= 0)
				fail("Keine Schulgliederungen für die Schulform " + sf.name() + " gefunden.");
		}
	}

	/**
	 * Prüft, ob die Schulgliederungen jeweils mindestens eine Schulform haben.
	 */
	@Test
	@DisplayName("Teste ob die Schulgliederungen jeweils mindestens eine Schulform haben")
	void testSchulgliederungMindestensEineSchulform() {
		for (final Schulgliederung gl : Schulgliederung.values()) {
			final SchulgliederungKatalogEintrag k = gl.daten(2024);
			if ((k != null) && (k.schulformen.size() <= 0))
				fail("Keine Schulform für die Schulgliederung " + gl.name() + "angegeben.");
		}
	}

	// TODO weitere Tests

}
