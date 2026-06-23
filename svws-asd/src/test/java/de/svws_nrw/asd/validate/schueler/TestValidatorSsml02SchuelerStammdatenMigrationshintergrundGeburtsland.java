package de.svws_nrw.asd.validate.schueler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

/**
 * <p> Testklasse für den Validator
 * <ul>
 *   <li> {@link ValidatorSsml02SchuelerStammdatenMigrationshintergrundGeburtsland},
 * </ul>
 * </p>
 *
 * <p> Testdaten:
 *   <ul>
 *	   <li>  de/svws_nrw/asd/validate/lehrer/Testdaten_002_LehrerPersonalabschnittsdaten.json
 *   </ul>
 * </p>
 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 * Für jeden Testfall ist eine Methode vorgesehen, in der mittels setzeTestdaten(...) die zugehörigen Testfälle erzeugt werden.
 *
 * CoreType: LehrerStammdaten
 */
@DisplayName("Tests zur Validierung der ValidatorSsml02SchuelerStammdatenMigrationshintergrundGeburtsland")
class TestValidatorSsml02SchuelerStammdatenMigrationshintergrundGeburtsland {

	private static final String TESTDATEN_GEBURTSLAND = """
		68069085, 2018, true
		82087065, 1950, false
		82087065, 2018, true
		""";

	/** Stammdaten der Schule */
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

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
	 * Test von ValidatorSsml02SchuelerStammdatenMigrationshintergrundGeburtsland
	 *
	 * CoreType: LehrerPersonalabschnittsdaten
	 *
	 * @param idGeburtsland   ID Geburtsland
	 * @param schuljahr  	  das Schuljahr
	 * @param result          gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorSsml02SchuelerStammdatenMigrationshintergrundGeburtsland")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_GEBURTSLAND)
	void testValidatorLppb02LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(final Long idGeburtsland, final Integer schuljahr,
			final boolean result) {
		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
//		Setzen Schuljahr
		kontext.getSchuljahresabschnitt().schuljahr = schuljahr;
		final ValidatorSsml02SchuelerStammdatenMigrationshintergrundGeburtsland validator =
				new ValidatorSsml02SchuelerStammdatenMigrationshintergrundGeburtsland(
						() -> idGeburtsland,
						kontext);

		assertEquals(result, validator.pruefe());

	}

}
