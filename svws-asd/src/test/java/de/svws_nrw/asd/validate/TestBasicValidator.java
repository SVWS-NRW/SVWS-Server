package de.svws_nrw.asd.validate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Diese Klasse testet die grundlegende Funktionalität eines BasicValidator
 */
class TestBasicValidator {

	/**
	 * Eine Test-Implementierung des abstrakten BasicValidator
	 */
	private static class TestValidator extends BasicValidator {

		/** Zur Steuerung in den Tests, ob die Prüf-Routine erfolgreich ist oder nicht. */
		public boolean pruefungErfolgreich = true;

		/** Zur Steuerung in den Tests, ob eine RuntimeException während der Prüf-Routine erzeugt werden soll. */
		public RuntimeException pruefungException = null;

		/**
		 * Erzeugt einen Test-Validator mit der angegebenen Fehlerart.
		 *
		 * @param fehlerart   die Fehlerart
		 */
		protected TestValidator(final ValidatorFehlerart fehlerart) {
			super(fehlerart);
		}

		@Override
		protected boolean pruefe() {
			// Für das Testen, falls in einem Validator eine Exception auftritt
			if (pruefungException != null)
				throw pruefungException;

			// Fügt einen Fehlertext hinzu, falls die Rüfung des Validators nicht erfolgreich ist
			if (!pruefungErfolgreich) {
				addFehler(1, "Test Fehler");
			}
			return pruefungErfolgreich;
		}

	}

	/** Die Instanz des TestValidators für die einzelnen Tests */
	private TestValidator validator;

	/** Die im Test verwendete Fehlerart. */
	private final ValidatorFehlerart fehlerart = ValidatorFehlerart.MUSS;

	@BeforeEach
	void setUp() {
		validator = new TestValidator(fehlerart);
	}

	@Test
	@DisplayName("Test: Handhabung von Exception in der Prüfroutine")
	void testException() {
		validator.pruefungException = new RuntimeException("Fehler!!!");

		final boolean result = validator.run();

		assertFalse(result, "Validator bei einer Exception false zurückgeben");
		assertFalse(validator.getFehler().isEmpty(), "Die Fehlerliste darf nicht leer sein");
		assertEquals(1, validator.getFehler().size());
		assertEquals("Unerwarteter Fehler bei der Validierung: Fehler!!!", validator.getFehler().get(0).getFehlermeldung(),
				"Die Fehlermeldung muss mit der Message der Exception übereinstimmen.");
	}

	@Test
	@DisplayName("Test: Prüfe, ob die Fehlerliste bei einer erneuten Ausführung geleert wird und die Fehlerart zurückgesetzt wird.")
	void testStatusZuruecksetzen() {
		// 1. Lauf: Schlägt fehl
		validator.pruefungErfolgreich = false;
		validator.run();
		assertEquals(1, validator.getFehler().size());
		assertNotEquals(ValidatorFehlerart.UNGENUTZT, validator.getFehlerart());

		// 2. Lauf: Erfolgreich
		validator.pruefungErfolgreich = true;
		final boolean result = validator.run();

		assertTrue(result);
		assertTrue(validator.getFehler().isEmpty(), "Die Liste MUSS leer sein (clear wurde aufgerufen)");
		assertEquals(ValidatorFehlerart.UNGENUTZT, validator.getFehlerart(), "Die run()-Methode setzt die _fehlerart korrekt zurück!");
	}

	@Test
	@DisplayName("Test: Prüfe, ob die Fehlerart korrekt gesetzt wird.")
	void testAktualisierungFehlerart() {
		validator.pruefungErfolgreich = false;
		validator.run();
		assertEquals(fehlerart, validator.getFehlerart());
	}

}
