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
 *   <li> {@link ValidatorSsmk10SchuelerStammdatenMigrationshintergrundKombination}
 * </ul>
 * </p>
 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 * Für jeden Testfall ist eine Methode vorgesehen, in der mittels setzeTestdaten(...) die zugehörigen Testfälle erzeugt werden.
 */
@DisplayName("Tests ValidatorSsmk10SchuelerStammdatenMigrationshintergrundKombination")
class TestValidatorSsmk10SchuelerStammdatenMigrationshintergrundKombination {

	private static final String TESTDATEN = """
			'DEU', 'DEU', 'DEU', 'de', false , true
			'DEU', 'DEU', 'DEU', 'de', true  , false
			'BEL', 'DEU', 'DEU', 'de', false , true
			'DEU', 'MAR', 'DEU', 'de', false , true
			'DEU', 'DEU', 'NED', 'de', true  , true
			'DEU', 'DEU', 'DEU', 'fr', true  , true
			'BEL', 'MAR', 'NED', 'fr', true  , true
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
	 * Test von ValidatorSsmk10SchuelerStammdatenMigrationshintergrundKombination
	 *
	 * @param geburtsland                das Geburtsland
	 * @param geburtslandMutter          das Geburtsland der Mutter
	 * @param geburtslandVater           das Geburtsland des Vaters
	 * @param verkehrsspracheFamilie     die Verkehrssprache der Familie
	 * @param hatMigrationshintergrund   die Migrationshintergrund vorhanden
	 * @param result                     gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorSsmk10SchuelerStammdatenMigrationshintergrundKombination")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorSsmk10SchuelerStammdatenMigrationshintergrundKombination(final String geburtsland, final String geburtslandMutter, final String geburtslandVater, final String verkehrsspracheFamilie, final boolean hatMigrationshintergrund, final boolean result) {
		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorSsmk10SchuelerStammdatenMigrationshintergrundKombination validator =
				new ValidatorSsmk10SchuelerStammdatenMigrationshintergrundKombination(
						() -> geburtsland, () -> geburtslandMutter, () -> geburtslandVater, () -> verkehrsspracheFamilie, () -> hatMigrationshintergrund, kontext);
		assertEquals(result, validator.pruefe());
	}


}
