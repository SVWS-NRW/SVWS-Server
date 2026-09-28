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
 * <p> Testklasse für die Validatoren
 * <ul>
 *   <li> {@link ValidatorSsmz11SchuelerStammdatenMigrationshintergrundZuzugsjahr}
 * </ul>
 * </p>
 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 * Für jeden Testfall ist eine Methode vorgesehen, in der mittels setzeTestdaten(...) die zugehörigen Testfälle erzeugt werden.
 */
@DisplayName("Tests ValidatorSsmz11SchuelerStammdatenMigrationshintergrundZuzugsjahr")
class TestValidatorSsmz11SchuelerStammdatenMigrationshintergrundZuzugsjahr {

	private static final String TESTDATEN_ZUZUGSJAHR = """
			2026, 2013, 2010-08-01, true, true
			2026, 2099, 2010-08-01, true, false
			2026, 2013, 2027-08-01, true, false
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
	 * Test von ValidatorSsmz11SchuelerStammdatenMigrationshintergrundZuzugsjahr
	 *
 	 * @param schuljahr                 das Schuljahr des Schülers
    * @param zuzugsjahr                das Zuzugsjahr des Schülers
	 * @param geburtsdatum              das Geburtsdatum des Schülers
	 * @param hatMigrationshintergrund  hat Migrationshintergrund
	 * @param result                    gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorSsmz11SchuelerStammdatenMigrationshintergrundZuzugsjahr")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_ZUZUGSJAHR, nullValues = { "null" })
	void testValidatorSsmz11SchuelerStammdatenMigrationshintergrundZuzugsjahr(final Integer schuljahr, final Integer zuzugsjahr, final String geburtsdatum, final boolean hatMigrationshintergrund,
			final boolean result) {
		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorSsmz11SchuelerStammdatenMigrationshintergrundZuzugsjahr validator =
				new ValidatorSsmz11SchuelerStammdatenMigrationshintergrundZuzugsjahr(
						() -> schuljahr, () -> zuzugsjahr, () -> geburtsdatum, () -> hatMigrationshintergrund, kontext);
		assertEquals(result, validator.pruefe());
	}


}
