package de.svws_nrw.asd.validate.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten;
import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.data.schule.SchuleStammdaten;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.DateManager;
import de.svws_nrw.asd.validate.InvalidDateException;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Testklasse für den Validator LehrerStammdaten
 *
 * Testdaten 1: de/svws_nrw/asd/validate/schule/Testdaten_001_SchuleStammdaten.json
 * Testdaten 2: de/svws_nrw/asd/validate/lehrer/Testdaten_001_LehrerStammdaten.json
 * Testdaten 3: de/svws_nrw/asd/validate/lehrer/Testdaten_002_LehrerPersonalabschnittsdaten.json
 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 * Für jeden Testfall ist eine Methode vorgesehen, in der mittels setzeTestdaten(...) die zugehörigen Testfälle erzeugt werden.
 *
 * CoreType: LehrerStammdaten
 */
@DisplayName("Teste den Validator zu LehrerPersonalabschnittsdaten")
class TestValidatorLehrerPersonalabschnittsdaten {

	/** Stammdaten der Schule */
	static final SchuleStammdaten schuleTestdaten_001 = JsonReader.fromResource(
			"de/svws_nrw/asd/validate/schule/Testdaten_001_SchuleStammdaten.json", SchuleStammdaten.class);
	/** Stammdaten des Lehrers */
	static final LehrerStammdaten lsdTestdaten_001 = JsonReader.fromResource(
			"de/svws_nrw/asd/validate/lehrer/Testdaten_001_LehrerStammdaten.json", LehrerStammdaten.class);
	/** Personalabschnittsdaten des Lehrers */
	static final LehrerPersonalabschnittsdaten lpadTestdaten_002 = JsonReader.fromResource(
			"de/svws_nrw/asd/validate/lehrer/Testdaten_002_LehrerPersonalabschnittsdaten.json",
			LehrerPersonalabschnittsdaten.class);

	/**
	 * Initialisiert die Core-Types, damit die Tests ausgeführt werden können.
	 * Beim Laden der Core-Type-Daten werden die JSON-Dateien auf Plausibilität
	 * geprüft.
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}


	/**
	 * Test von ValidatorLehrerPersonalabschnittsdaten mit validen Daten
	 *
	 * CoreType: LehrerPersonalabschnittsdaten
	 * Testfall: Daten zulässig
	 * Ergebnis: Validator soll TRUE liefern
	 */
	@DisplayName("TestValidatorLehrerPersonalabschnittsdaten: Test mit gültigen Daten auf TRUE")
	@Test
	void testValidatorLehrerPersonalabschnittsdaten_ValideDaten() {
		// Testdaten setzen
		setzeTestdaten(25.5);

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);
		final ValidatorLehrerPersonalabschnittsdaten validator = new ValidatorLehrerPersonalabschnittsdaten(lpadTestdaten_002, lsdTestdaten_001, kontext);
		assertEquals(true, validator.run());
	}


	/**
	 * Test von ValidatorLehrerPersonalabschnittsdaten auf Pflichtstundensoll zu hoch
	 *
	 * CoreType: LehrerPersonalabschnittsdaten
	 * Testfall: Das Pflichtstundensoll ist zu hoch
	 * Ergebnis: Validator soll FALSE liefern
	 */
	@DisplayName("TestValidatorLehrerPersonalabschnittsdaten: Fehler Pflichtstundensoll zu hoch")
	@Test
	void testValidatorLehrerPersonalabschnittsdaten_PflichtstundensollZuHoch() {
		// Testdaten setzen
		setzeTestdaten(42.5);

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);
		final ValidatorLehrerPersonalabschnittsdaten validator = new ValidatorLehrerPersonalabschnittsdaten(lpadTestdaten_002, lsdTestdaten_001, kontext);
		assertEquals(false, validator.run());
	}


	/**
	 * Test von ValidatorLehrerPersonalabschnittsdatenPflichtstundensoll
	 *
	 * CoreType: LehrerPersonalabschnittsdaten
	 * Testfall: Daten zulässig
	 * Ergebnis: Validator soll TRUE liefern
	 */
	@DisplayName("TestValidatorLehrerPersonalabschnittsdatenPflichtstundensoll: Test mit gültigen Daten auf TRUE")
	@Test
	void testValidatorLehrerPersonalabschnittsdatenPflichtstundensoll_ValideDaten() {
		// Testdaten setzen
		setzeTestdaten(25.5);

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);
		final ValidatorLehrerPersonalabschnittsdatenPflichtstundensoll validator = new ValidatorLehrerPersonalabschnittsdatenPflichtstundensoll(lpadTestdaten_002, kontext);
		assertEquals(true, validator.run());
	}


	/**
	 * Test von TestValidatorLehrerPersonalabschnittsdatenPflichtstundensoll auf Pflichtstundensoll zu hoch
	 *
	 * CoreType: LehrerPersonalabschnittsdaten
	 * Testfall: Das Pflichtstundensoll ist zu hoch
	 * Ergebnis: Validator soll FALSE liefern
	 */
	@DisplayName("TestValidatorLehrerPersonalabschnittsdatenPflichtstundensoll: Fehler Pflichtstundensoll zu hoch")
	@Test
	void testValidatorLehrerPersonalabschnittsdatenPflichtstundensoll_PflichtstundensollZuHoch() {
		// Testdaten setzen
		setzeTestdaten(42.5);

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);
		LehrerPersonalabschnittsdaten lpad = lpadTestdaten_002;
		lpad.pflichtstundensoll = 42.0;
		final ValidatorLehrerPersonalabschnittsdatenPflichtstundensoll validator = new ValidatorLehrerPersonalabschnittsdatenPflichtstundensoll(lpad, kontext);
		assertEquals(false, validator.run());
	}


	/**
	 * Test von ValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnisGeburtsdatum mit validen Daten
	 *
	 * CoreType: LehrerPersonalabschnittsdaten
	 * Testfall: Daten zulässig
	 * Ergebnis: Validator soll TRUE liefern
	 */
	@DisplayName("TestValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnisGeburtsdatum:  Test mit gültigen Daten auf TRUE")
	@Test
	void testValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnisGeburtsdatum_ValideDaten() {
		// Testdaten setzen
		setzeTestdaten(25.5);

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);

		try {
			final @NotNull DateManager geburtsdatum = DateManager.from(lsdTestdaten_001.geburtsdatum);
			final ValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnis validator = new ValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnis(lpadTestdaten_002, geburtsdatum, kontext);
			assertEquals(true, validator.run());
		} catch (@SuppressWarnings("unused") final InvalidDateException e) {
			assertEquals(true, false); // darf hier nicht hin
			// Ist kein gültiges Geburtsdatum gesetzt, so werden die Prüfungen übersprungen.
			// Die eigentliche Validierung des Geburtsdatums erfolgt bei den Lehrer-Stammdaten
		}
	}


	/**
	 * Test von ValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnisGeburtsdatum auf zu junges Geburtsdatum
	 *
	 * CoreType: LehrerPersonalabschnittsdaten
	 * Testfall: Das Geburtsdatum ist für das Rechtsverhältnis zu jung
	 * Ergebnis: Validator soll FALSE liefern
	 */
	@DisplayName("TestValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnisGeburtsdatum: Fehler Geburtsdatum zu jung")
	@Test
	void testValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnisGeburtsdatum_GeburtsdatumZuJung() {
		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);

		try {
			final @NotNull DateManager geburtsdatum = DateManager.from("2004-01-01");
			final ValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnis validator = new ValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnis(lpadTestdaten_002, geburtsdatum, kontext);
			assertEquals(false, validator.run());
		} catch (@SuppressWarnings("unused") final InvalidDateException e) {
			assertEquals(true, false); // darf hier nicht hin
			// Ist kein gültiges Geburtsdatum gesetzt, so werden die Prüfungen übersprungen.
			// Die eigentliche Validierung des Geburtsdatums erfolgt bei den Lehrer-Stammdaten
		}
	}


	/**
	 * Test von TestValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnisGeburtsdatum auf falsches Datumsformat
	 *
	 * CoreType: LehrerPersonalabschnittsdaten
	 * Testfall: Das Geburtsdatum ist im falschen Format, es wird auf das Auslösen der Exception vom DateManager getestet
	 * Ergebnis: Validator wird nicht aufgerufen, Exception vom DateManager muss auslösen
	 */
	@DisplayName("TestValidatorLehrerPersonalabschnittsdaten: Fehler Format Geburtsdatum ungültig")
	@Test
	void testValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnisGeburtsdatum() {
		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);

		// Wir erwarten eine InvalidDateException bei der Übergabe des Datums.
		try {
			final @NotNull DateManager geburtsdatum = DateManager.from("01.01.1990");
			new ValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnis(lpadTestdaten_002, geburtsdatum, kontext);
		} catch (final InvalidDateException e) {
			assertTrue(e.getMessage().contains("ist nicht konform zu ISO8601"));
		}
	}

	/**
	 * Diese Methode erlaubt das Setzen der gewünschten Werte für den Test im CoreType LehrerPersonalabschnittsdaten
	 *
	 * @param pflichtstunden - die Pflichtstundenzahl
	 */
	void setzeTestdaten(final double pflichtstunden) {
		lpadTestdaten_002.pflichtstundensoll = pflichtstunden;
	}

}
