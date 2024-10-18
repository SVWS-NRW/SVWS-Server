package de.svws_nrw.asd.types.fach;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link Sprachreferenzniveau}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type Sprachreferenzniveau")
class SprachreferenzniveauTest {

	/**
	 * Initialisiert die Core-Types, damit die Tests ausgeführt werden können.
	 * Beim Laden der Core-Type-Daten werden die JSON-Dateien auf Plausibilität
	 * geprüft.
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}


	// TODO Schreiben von Tests

	/**
	 * Führt einfache Tests zu dem Core-Type Sprachreferenzniveau aus.
	 */
	@Test
	@DisplayName("Prüfe die Anzahl der Anlagen am Berufskolleg.")
	void testSprachreferenzniveau() {
		assertEquals(13, Sprachreferenzniveau.data().getWerte().size());
	}

	/**
	 * Führt einfache Tests zu dem Core-Type Sprachreferenzniveau aus.
	 */
	@Test
	@DisplayName("Prüfe ob der richtige Name enthalten ist")
	void testSprachreferenzniveauKuerzel() {
		assertEquals("A1+", Sprachreferenzniveau.data().getHistorieByWert(Sprachreferenzniveau.A1P).getFirst().kuerzel);
	}

}
