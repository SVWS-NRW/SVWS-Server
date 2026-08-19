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
import de.svws_nrw.transpiler.annotations.AllowNull;

/**
 * <p> Testklasse für die Validatoren
 * <ul>
 * <li> {@link ValidatorSsms02SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie}
 * </ul>
 * </p>
 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 * Für jeden Testfall ist eine Methode vorgesehen, in der mittels setzeTestdaten(...) die zugehörigen Testfälle erzeugt werden.
 */
@DisplayName("Tests ValidatorSsms02SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie")
class TestValidatorSsms02SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie {

	private static final String TESTDATEN_VERKEHRSSPRACHE = """
            1000   , true
            -1     , false
            999999 , false
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
	 * Test von ValidatorSsms02SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie
	 *
	 * @param idVerkehrsspracheFamilie  die ID der Verkehrssprache der Familie
	 * @param result                    gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorSsms02SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_VERKEHRSSPRACHE, nullValues = { "null" })
	void testValidatorSsms02SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie(final @AllowNull Long idVerkehrsspracheFamilie,
			final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		final ValidatorSsms02SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie validator =
				new ValidatorSsms02SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie(
						() -> idVerkehrsspracheFamilie, () -> null, kontext);

		assertEquals(result, validator.pruefe());
	}

}
