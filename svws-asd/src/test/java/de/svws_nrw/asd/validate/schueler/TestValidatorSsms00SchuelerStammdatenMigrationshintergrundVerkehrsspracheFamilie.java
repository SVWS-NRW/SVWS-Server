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
 * <li> {@link ValidatorSsms00SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie}
 * </ul>
 * </p>
 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 * Für jeden Testfall ist eine Methode vorgesehen, in der mittels setzeTestdaten(...) die zugehörigen Testfälle erzeugt werden.
 */
@DisplayName("Tests ValidatorSsms00SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie")
class TestValidatorSsms00SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie {

	private static final String TESTDATEN_VERKEHRSSPRACHE = """
            ID   , hatMH , RESULT
            null , false , true
            null , true  , false
            1    , true  , true
            1000 , true  , true
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
	 * Test von ValidatorSsms00SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie
	 *
	 * @param idVerkehrsspracheFamilie  die ID der Verkehrssprache der Familie
	 * @param hatMigrationshintergrund  hat Migrationshintergrund
	 * @param result                    gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorSsms00SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie")
	@ParameterizedTest
	@CsvSource(useHeadersInDisplayName = true, textBlock = TESTDATEN_VERKEHRSSPRACHE, nullValues = { "null" })
	void testValidatorSsms00SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie(final Long idVerkehrsspracheFamilie,
			final boolean hatMigrationshintergrund,
			final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		final ValidatorSsms00SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie validator =
				new ValidatorSsms00SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie(
						() -> idVerkehrsspracheFamilie, () -> hatMigrationshintergrund, kontext);

		assertEquals(result, validator.pruefe());
	}

}
