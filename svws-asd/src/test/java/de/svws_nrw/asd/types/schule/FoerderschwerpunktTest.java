package de.svws_nrw.asd.types.schule;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

	/**
	 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link Foerderschwerpunkt}
	 * zur Verfügung.
	 */
	@DisplayName("Teste den Core-Type Foerderschwerpunkt")
	class FoerderschwerpunktTest {

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
		 * Führt einfache Tests zu dem Core-Type Foerderschwerpunkt aus.
		 */
		@Test
		@DisplayName("Prüfe die Anzahl der Anlagen am Foerderschwerpunkt.")
		void testFoerderschwerpunkt() {
			assertEquals(16, Foerderschwerpunkt.data().getWerte().size());
		}

		/**
		 * Führt einfache Tests zu dem Core-Type Foerderschwerpunkt aus.
		 */
		@Test
		@DisplayName("Prüfe ob text richtig ist")
		void testFoerderschwerpunktText() {
			assertEquals("Körperliche und motorische Entwicklung", Foerderschwerpunkt.data().getHistorieByWert(Foerderschwerpunkt.KB).getFirst().text);
		}

}
