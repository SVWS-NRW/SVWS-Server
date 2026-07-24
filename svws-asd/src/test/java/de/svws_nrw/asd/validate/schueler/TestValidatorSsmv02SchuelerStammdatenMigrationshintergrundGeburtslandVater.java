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
 * Testklasse für den Validator
 * {@link ValidatorSsmv02SchuelerStammdatenMigrationshintergrundGeburtslandVater}.
 */
@DisplayName("Tests ValidatorSsmv02SchuelerStammdatenMigrationshintergrundGeburtslandVater")
class TestValidatorSsmv02SchuelerStammdatenMigrationshintergrundGeburtslandVater {

	// Testdaten: idGeburtslandVater, result
	// 66069085 ist gültig im Zeitraum, 99999999 ist ungültig/existiert nicht
	private static final String TESTDATEN = """
			ID       , RESULT
			null     , true
			68069085 , true
			99999999 , false
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
	 * Test von ValidatorSsmv02SchuelerStammdatenMigrationshintergrundGeburtslandVater
	 *
	 * @param idGeburtslandVater   die ID des Geburtslandes des Vaters
	 * @param result                gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorSsmv02SchuelerStammdatenMigrationshintergrundGeburtslandVater")
	@ParameterizedTest
	@CsvSource(useHeadersInDisplayName = true, textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorSsmm02(final Long idGeburtslandVater, final boolean result) {
		final ValidatorKontext kontext = new ValidatorKontext(
				testdaten_001.schule.schulNr,
				Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
				testdaten_001.schule.abschnitte,
				testdaten_001.schule.idSchuljahresabschnitt,
				true);

		final ValidatorSsmv02SchuelerStammdatenMigrationshintergrundGeburtslandVater validator =
				new ValidatorSsmv02SchuelerStammdatenMigrationshintergrundGeburtslandVater(
						() -> idGeburtslandVater,
						() -> true, //spielt keine Rolle bei dem Test, wird aber im Konstuktor benötigt
						kontext);

		assertEquals(result, validator.pruefe());
	}
}
