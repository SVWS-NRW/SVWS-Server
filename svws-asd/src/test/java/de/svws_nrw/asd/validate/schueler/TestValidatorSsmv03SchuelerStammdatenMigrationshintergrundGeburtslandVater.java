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
 * {@link ValidatorSsmv03SchuelerStammdatenMigrationshintergrundGeburtslandVater}.
 */
@DisplayName("Tests ValidatorSsmv03SchuelerStammdatenMigrationshintergrundGeburtslandVater")
class TestValidatorSsmv03SchuelerStammdatenMigrationshintergrundGeburtslandVater {

	// Testdaten: ID, hatMH (hat Migrationshintergrund), RESULT
	// 66080985 ist eine valide Länder-ID.
	private static final String TESTDATEN = """
			ID       , hatMH , RESULT
			null     , false , true
			null     , true  , true
			66080985 , true  , true
			66080985 , false , false
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
	 * Test von ValidatorSsmv03SchuelerStammdatenMigrationshintergrundGeburtslandVater
	 *
	 * @param idGeburtslandVater        die ID des Geburtslandes des Vaters
	 * @param hatMigrationshintergrund   Migrationshintergrund vorhanden
	 * @param result                     gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorSsmv03SchuelerStammdatenMigrationshintergrundGeburtslandVater")
	@ParameterizedTest
	@CsvSource(useHeadersInDisplayName = true, textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorSsmm03(final Long idGeburtslandVater, final boolean hatMigrationshintergrund, final boolean result) {
		final ValidatorKontext kontext = new ValidatorKontext(
				testdaten_001.schule.schulNr,
				Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
				testdaten_001.schule.abschnitte,
				testdaten_001.schule.idSchuljahresabschnitt,
				true);

		final ValidatorSsmv03SchuelerStammdatenMigrationshintergrundGeburtslandVater validator =
				new ValidatorSsmv03SchuelerStammdatenMigrationshintergrundGeburtslandVater(
						() -> idGeburtslandVater,
						() -> hatMigrationshintergrund,
						kontext);

		assertEquals(result, validator.pruefe());
	}
}
