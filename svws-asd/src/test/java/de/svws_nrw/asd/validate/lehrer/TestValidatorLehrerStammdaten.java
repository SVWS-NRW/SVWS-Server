package de.svws_nrw.asd.validate.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.data.schule.SchuleStammdaten;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

/**
 * Testklasse für den Validator {@link ValidatorNoteStammdaten}
 *
 * Testdaten 1: de/svws_nrw/asd/validate/schule/Testdaten_001_SchuleStammdaten.json
 * Testdaten 2: de/svws_nrw/asd/validate/lehrer/Testdaten_001_LehrerStammdaten.json
 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 * Für jeden Testfall ist eine Methode vorgesehen, in der mittels setzeTestdaten(...) die zugehörigen Testfälle erzeugt werden.
 *
 * CoreType: LehrerStammdaten
 */
@DisplayName("Tests zum Validator der LehrerStammdaten folgen:")
class TestValidatorLehrerStammdaten {

	static final SchuleStammdaten schuleTestdaten_001 = JsonReader.fromResource("de/svws_nrw/asd/validate/schule/Testdaten_001_SchuleStammdaten.json", SchuleStammdaten.class);
	static final LehrerStammdaten lehrerTestdaten_001 = JsonReader.fromResource("de/svws_nrw/asd/validate/lehrer/Testdaten_001_LehrerStammdaten.json", LehrerStammdaten.class);

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
	 * Test von ValidatorLehrerStammdaten mit validen Daten
	 *
	 * CoreType: LehrerStammdaten
	 * Testfall: Daten zulässig
	 * Ergebnis: Validator soll TRUE liefern
	 */
	@DisplayName("TestValidatorLehrerStammdaten: Test mit gültigen Daten auf TRUE")
	@Test
	void testValidatorLehrerStammdaten_ValideDaten() {
		// Testdaten setzen
		setzeTestdaten("Müller", "Frank");

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);
		final ValidatorLehrerStammdaten validator = new ValidatorLehrerStammdaten(lehrerTestdaten_001, kontext);
		assertEquals(true, validator.run());
	}


	/**
	 * Test von ValidatorLehrerStammdaten auf Nachname mit führendem Leerzeichen
	 *
	 * CoreType: LehrerStammdaten
	 * Testfall: Der Nachname hat ein führendes Leerzeichen. Es wird auch sichergestellt, dass der Kindvalidator aufgerufen wird.
	 * Ergebnis: Validator soll FALSE liefern
	 */
	@DisplayName("TestValidatorLehrerStammdaten: Test auf ungültigen Nachname mit führendem Leerzeichen")
	@Test
	void testValidatorLehrerStammdaten_NachnameMitLeerzeichen() {
		// Testdaten setzen
		setzeTestdaten(" Müller", "Frank");

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);
		final ValidatorLehrerStammdaten validator = new ValidatorLehrerStammdaten(lehrerTestdaten_001, kontext);
		assertEquals(false, validator.run());
	}


	/**
	 * Test von ValidatorLehrerStammdatenNachname mit validen Daten
	 *
	 * CoreType: LehrerStammdaten
	 * Testfall: Daten zulässig
	 * Ergebnis: Validator soll TRUE liefern
	 */
	@DisplayName("TestValidatorLehrerStammdatenNachname: Test mit gültigen Daten auf TRUE")
	@Test
	void testValidatorLehrerStammdatenNachname_ValideDaten() {
		// Testdaten setzen
		setzeTestdaten("Müller", "Frank");

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);
		final ValidatorLehrerStammdatenNachname validator = new ValidatorLehrerStammdatenNachname(lehrerTestdaten_001, kontext);
		assertEquals(true, validator.run());
	}


	/**
	 * Test von ValidatorLehrerStammdatenNachname auf Nachname mit führendem Leerzeichen
	 *
	 * CoreType: LehrerStammdaten
	 * Testfall: Der Nachname hat ein führendes Leerzeichen.
	 * Ergebnis: Validator soll FALSE liefern
	 */
	@DisplayName("TestValidatorLehrerStammdatenNachname: Test auf ungültigen Nachname mit führendem Leerzeichen")
	@Test
	void testValidatorLehrerStammdatenNachname_NachnameLeerzeichenVorne() {
		// Testdaten setzen
		setzeTestdaten(" Müller", "Frank");

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);
		final ValidatorLehrerStammdatenNachname validator = new ValidatorLehrerStammdatenNachname(lehrerTestdaten_001, kontext);
		assertEquals(false, validator.run());
	}


	/**
	 * Test von ValidatorLehrerStammdatenNachname auf Nachname mit Großbuchstaben an zweiter Stelle
	 *
	 * CoreType: LehrerStammdaten
	 * Testfall: Nachname hat Großbuchstaben an zweiter Stelle
	 * Ergebnis: Validator soll FALSE liefern
	 */
	@DisplayName("TestValidatorLehrerStammdatenNachname: Test auf ungültigen Nachnamen mit zwei Großbuchstaben zu Beginn")
	@Test
	void testValidatorLehrerStammdatenNachname_NachnameZweiGrossbuchstaben() {
		// Testdaten setzen
		setzeTestdaten("MÜller", "Frank");

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);
		final ValidatorLehrerStammdatenNachname validator = new ValidatorLehrerStammdatenNachname(lehrerTestdaten_001, kontext);
		assertEquals(false, validator.run());
	}


	/**
	 * Test von ValidatorLehrerStammdatenNachname auf gültigen Nachnamen mit Zusatz
	 *
	 * CoreType: LehrerStammdaten
	 * Testfall: Nachname hat einen Vorsatz z.B. von
	 * Ergebnis: Validator soll TRUE liefern
	 */
	@DisplayName("TestValidatorLehrerStammdatenNachname: Test mit gültigen Namenszusatz im Namen auf TRUE")
	@Test
	void testValidatorLehrerStammdatenNachname_ValiderNamenszusatz() {
		// Testdaten setzen
		setzeTestdaten("von Müller", "Frank");

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);
		final ValidatorLehrerStammdatenNachname validator = new ValidatorLehrerStammdatenNachname(lehrerTestdaten_001, kontext);
		assertEquals(true, validator.run());
	}


	/**
	 * Test von ValidatorLehrerStammdatenNachname auf Nachnamen mit gültigen Zusatz aber ungültigen Nnamen, der zwei Großbuchstaben hat
	 *
	 * CoreType: LehrerStammdaten
	 * Testfall: Der Nachname hat einen gültigen Zusatz "von" aber der Name zwei Großbuchstaben
	 * Ergebnis: Validator soll FALSE liefern
	 */
	@DisplayName("TestValidatorLehrerStammdatenNachname: gültiger Zusatz aber Name mit zwei Großbuchstaben")
	@Test
	void testValidatorLehrerStammdatenNachname_MitZusatzUndNameZweiGrossbuchstaben() {
		// Testdaten setzen
		setzeTestdaten("von MÜller", "Frank");

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);
		final ValidatorLehrerStammdatenNachname validator = new ValidatorLehrerStammdatenNachname(lehrerTestdaten_001, kontext);
		assertEquals(false, validator.run());
	}


	/**
	 * Test von ValidatorLehrerStammdatenNachname auf Nachnamen mit kleinen Anfangsbuchstaben prüfen
	 *
	 * CoreType: LehrerStammdaten
	 * Testfall: Der Nachname beginnt mit einem Kleinbuchstaben
	 * Ergebnis: Validator soll FALSE liefern
	 */
	@DisplayName("TestValidatorLehrerStammdatenNachname: ungültiger Nachname mit kleinem Anfang")
	@Test
	void testValidatorLehrerStammdatenNachname_NachnameBeginntKlein() {
		// Testdaten setzen
		setzeTestdaten("mÜller", "Frank");

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);
		final ValidatorLehrerStammdatenNachname validator = new ValidatorLehrerStammdatenNachname(lehrerTestdaten_001, kontext);
		assertEquals(false, validator.run());
	}


	/**
	 * Test von ValidatorLehrerStammdatenNachname auf Nachnamen mit gültigen Zusatz aber Name beginnt kleine
	 *
	 * CoreType: LehrerStammdaten
	 * Testfall: Der Nachname hat einen gültigen Zusatz "von" aber der Name beginnt klein
	 * Ergebnis: Validator soll FALSE liefern
	 */
	@DisplayName("TestValidatorLehrerStammdatenNachname: gültiger Zusatz aber Name beginnt klein")
	@Test
	void testValidatorLehrerStammdatenNachname_MitZusatzUndNameBeginntKlein() {
		// Testdaten setzen
		setzeTestdaten("von mÜller", "Frank");

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);
		final ValidatorLehrerStammdatenNachname validator = new ValidatorLehrerStammdatenNachname(lehrerTestdaten_001, kontext);
		assertEquals(false, validator.run());
	}


	/**
	 * Test von ValidatorLehrerStammdatenNachname mit validem Nachname der einem Vorsatz entspricht
	 *
	 * CoreType: LehrerStammdaten
	 * Testfall: Daten zulässig - Der Nachname heißt wie der Vorsatz Thor
	 * Ergebnis: Validator soll TRUE liefern
	 */
	@DisplayName("TestValidatorLehrerStammdatenNachname: Test mit gültigen Nachname gleichlautend wie Zusatz auf TRUE")
	@Test
	void testValidatorLehrerStammdatenNachname_ValiderNachnameLautetWieZusatz() {
		// Testdaten setzen
		setzeTestdaten("Thor", "Frank");

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);
		final ValidatorLehrerStammdatenNachname validator = new ValidatorLehrerStammdatenNachname(lehrerTestdaten_001, kontext);
		assertEquals(true, validator.run());
	}


	/**
	 * Test von ValidatorLehrerStammdatenNachname auf ungültigen Nachnamen "thor"
	 *
	 * CoreType: LehrerStammdaten
	 * Testfall: Der Nachname besteht nur aus dem zulässigen Zusatz "thor"
	 * Ergebnis: Validator soll FALSE liefern
	 */
	@DisplayName("TestValidatorLehrerStammdatenNachname: Nachname besteht nur aus Zusatz thor")
	@Test
	void testValidatorLehrerStammdatenNachname_NachnameNurAusZusatz() {
		// Testdaten setzen
		setzeTestdaten("thor", "Frank");

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);
		final ValidatorLehrerStammdatenNachname validator = new ValidatorLehrerStammdatenNachname(lehrerTestdaten_001, kontext);
		assertEquals(false, validator.run());
	}


	/**
	 * Test von ValidatorLehrerStammdatenVorname mit validen Daten
	 *
	 * CoreType: LehrerStammdaten
	 * Testfall: Daten zulässig
	 * Ergebnis: Validator soll TRUE liefern
	 */
	@DisplayName("TestValidatorLehrerStammdatenVorname: Test mit gültigen Daten auf TRUE")
	@Test
	void testValidatorLehrerStammdatenVorname_ValideDaten() {
		// Testdaten setzen
		setzeTestdaten("Müller", "Frank");

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);
		final ValidatorLehrerStammdatenVorname validator = new ValidatorLehrerStammdatenVorname(lehrerTestdaten_001, kontext);
		assertEquals(true, validator.run());
	}


	/**
	 * Test von ValidatorLehrerStammdatenVorname auf ungültigen Vornamen mit führenden Leerzeichen
	 *
	 * CoreType: LehrerStammdaten
	 * Testfall: Der vorname hat ein führendes Leerzeichen
	 * Ergebnis: Validator soll FALSE liefern
	 */
	@DisplayName("TestValidatorLehrerStammdatenVorname: Vorname hat führendes Leerzeichen")
	@Test
	void testValidatorLehrerStammdatenVorname_VornameLeerzeichenVorne() {
		// Testdaten setzen
		setzeTestdaten("Müller", " Linksbündig");

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);
		final ValidatorLehrerStammdatenVorname validator = new ValidatorLehrerStammdatenVorname(lehrerTestdaten_001, kontext);
		assertEquals(false, validator.run());
	}


	/**
	 * Test von ValidatorLehrerStammdatenVorname auf Vornamen mit zwei Großbuchstaben zu Beginn
	 *
	 * CoreType: LehrerStammdaten
	 * Testfall: Der Vorname hat zwei Großbuchstaben zu Beginn
	 * Ergebnis: Validator soll FALSE liefern
	 */
	@DisplayName("TestValidatorLehrerStammdatenVorname: ungültiger Vorname mit zwei Großbuchstaben")
	@Test
	void testValidatorLehrerStammdatenVorname_VornameZweiGrossbuchstaben() {
		// Testdaten setzen
		setzeTestdaten("Müller", "GRoßbuchstaben");

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);
		final ValidatorLehrerStammdatenVorname validator = new ValidatorLehrerStammdatenVorname(lehrerTestdaten_001, kontext);
		assertEquals(false, validator.run());
	}


	/**
	 * Test von ValidatorLehrerStammdatenVorname auf Vornamen mit drei Großbuchstaben zu Beginn
	 *
	 * CoreType: LehrerStammdaten
	 * Testfall: Der Vorname hat drei Großbuchstaben zu Beginn
	 * Ergebnis: Validator soll FALSE liefern
	 */
	@DisplayName("TestValidatorLehrerStammdatenVorname: ungültiger Vorname mit drei Großbuchstaben")
	@Test
	void testValidatorLehrerStammdatenVorname_VornameDreiGrossbuchstaben() {
		// Testdaten setzen
		setzeTestdaten("Müller", "GROßbuchstaben");

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);
		final ValidatorLehrerStammdatenVorname validator = new ValidatorLehrerStammdatenVorname(lehrerTestdaten_001, kontext);
		assertEquals(false, validator.run());
	}


	/**
	 * Test von ValidatorLehrerStammdatenVorname mit ungültigem Großbuchstaben an dritter Stelle
	 *
	 * CoreType: LehrerStammdaten
	 * Testfall: Der Vorname hat an erster und dritter Stelle einen Großbuchstaben
	 * Ergebnis: Validator soll FALSE liefern
	 */
	@DisplayName("TestValidatorLehrerStammdatenVorname: Vorname -erster und dritter Buchstabe sind groß")
	@Test
	void testValidatorLehrerStammdatenVorname_VornameBuchstabenEinsUndDreiGross() {
		// Testdaten setzen
		setzeTestdaten("Müller", "GrOßbuchstaben");

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);
		final ValidatorLehrerStammdatenVorname validator = new ValidatorLehrerStammdatenVorname(lehrerTestdaten_001, kontext);
		assertEquals(false, validator.run());
	}


	/**
	 * Test von ValidatorLehrerStammdatenVorname auf Vornamen mit kleinen Anfangsbuchstaben prüfen
	 *
	 * CoreType: LehrerStammdaten
	 * Testfall: Vorname hat kleinen Anfangsbuchstaben
	 * Ergebnis: Validator soll FALSE liefern
	 */
	@DisplayName("TestValidatorLehrerStammdatenVorname: Vorname beginnt klein")
	@Test
	void testValidatorLehrerStammdatenVorname_VornameBeginntKlein() {
		// Testdaten setzen
		setzeTestdaten("Müller", "kleinbuchstabe");

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);
		final ValidatorLehrerStammdatenVorname validator = new ValidatorLehrerStammdatenVorname(lehrerTestdaten_001, kontext);
		assertEquals(false, validator.run());
	}


	/**
	 * Test von ValidatorLehrerStammdatenVorname auf gültigen zweiteiligen Vornamen
	 *
	 * CoreType: LehrerStammdaten
	 * Testfall: gültiger Vorname aus zwei Teilen
	 * Ergebnis: Validator soll TRUE liefern
	 */
	@DisplayName("TestValidatorLehrerStammdatenVorname: Vorname ist zweiteilig")
	@Test
	void testValidatorLehrerStammdatenVorname_VornameZweiteilig() {
		// Testdaten setzen
		setzeTestdaten("Müller", "Thor Hendrik");

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);
		final ValidatorLehrerStammdatenVorname validator = new ValidatorLehrerStammdatenVorname(lehrerTestdaten_001, kontext);
		assertEquals(true, validator.run());
	}


	/**
	 * Diese Methode erlaubt das Setzen der gewünschten Werte für den Test im CoreType LehrerPersonalabschnittsdaten
	 *
	 * @param nachname - der Nachname
	 * @param vorname - der Vorname
	 */
	void setzeTestdaten(final String nachname, final String vorname) {
		lehrerTestdaten_001.nachname = nachname;
		lehrerTestdaten_001.vorname = vorname;
	}

}
