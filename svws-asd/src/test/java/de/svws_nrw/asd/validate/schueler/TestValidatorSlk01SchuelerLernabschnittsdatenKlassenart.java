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
 *   <li> {@link ValidatorSlk01SchuelerLernabschnittsdatenKlassenart}
 * </ul>
 * </p>
 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 */
@DisplayName("Tests ValidatorSlk01SchuelerLernabschnittsdatenKlassenart")
class TestValidatorSlk01SchuelerLernabschnittsdatenKlassenart {

	private static final String TESTDATEN_KLASSENART = """
			-1          , false
			5000        , true
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
	 * Test von ValidatorSlk01SchuelerLernabschnittsdatenKlassenart
	 *
	 * @param idKlassenart  ID Klassenart
	 * @param result        gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorSlk01SchuelerLernabschnittsdatenKlassenart")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_KLASSENART, nullValues = { "null" })
	void testValidatorSsml01SchuelerStammdatenMigrationshintergrundGeburtsland(final Long idKlassenart,
			final boolean result) {
		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorSlk01SchuelerLernabschnittsdatenKlassenart validator =
				new ValidatorSlk01SchuelerLernabschnittsdatenKlassenart(
						() -> idKlassenart, kontext);
		assertEquals(result, validator.pruefe());
	}


}
