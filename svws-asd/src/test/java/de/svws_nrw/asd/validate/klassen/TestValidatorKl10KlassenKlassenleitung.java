package de.svws_nrw.asd.validate.klassen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Testklasse für den ValidatorKl10KlassenKlassenleitung
 */
@DisplayName("Teste den Validator Kl10 für die Klassenleitungen")
class TestValidatorKl10KlassenKlassenleitung {

	/** Statistikdaten der Schule*/
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	private static final String TESTDATEN = """
			0  ,	false
			4  ,	true
		""";

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
	 * Test von ValidatorKl10KlassenKlassenleitung
	 *
	 * CoreType: KlassenDaten
	 *
	 * @param anzahl          die Anzahl der Klassenleitungen
	 * @param result     	  gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorKl10KlassenKlassenleitung")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN)
	void testValidatorGld0GesamtLehrerdatenDuplikate(final int anzahl, final boolean result) {

		@NotNull List<Long> listLleitungen = new ArrayList<Long>();

		if (anzahl != 0) {
			listLleitungen.add(0L);
			listLleitungen.add(4L);
			listLleitungen.add(2L);
			listLleitungen.add(3L);
		}


		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorKl10KlassenKlassenleitung validator =
				new ValidatorKl10KlassenKlassenleitung(
						() -> listLleitungen,
						kontext);
		assertEquals(result, validator.pruefe());
	}

}
