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

/**
 * <p> Testklasse für die Validatoren
 * <ul>
 *   <li> {@link ValidatorKll10KlassenKlassenleitungslisteLehrkraft}
 * </ul>
 * </p>

 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 * Für jeden Testfall ist eine Methode vorgesehen, in der mittels setzeTestdaten(...) die zugehörigen Testfälle erzeugt werden.
 *
 */
@DisplayName("Tests ValidatorKll10KlassenKlassenleitungslisteLehrkraft")
class TestValidatorKll10KlassenKlassenleitungslisteLehrkraft {

	private static final String TESTDATEN = """
			0, true
			1, true
			2, false
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
	 * Test von ValidatorKll10KlassenKlassenleitungslisteLehrkraft
	 *
	 * CoreType: KursDaten
	 * @param anzahl   Steuerung
	 * @param result   gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorKll10KlassenKlassenleitungslisteLehrkraft")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorKll10KlassenKlassenleitungslisteLehrkraft(final Integer anzahl, final boolean result) {

		final List<Long> listKlassenleitungen = new ArrayList<>();
		final List<Long> listLehrer = new ArrayList<>();

		if (anzahl > 0) {
			// Liste KursLehrer erzeugen
			final Long klassenleitung1 = 1L;
			final Long klassenleitung2 = 2L;
			final Long klassenleitung3 = 3L;

			listKlassenleitungen.add(klassenleitung1);
			listKlassenleitungen.add(klassenleitung2);
			listKlassenleitungen.add(klassenleitung3);

			// Liste Lehrer erzeugen
			final Long lehrer1 = 1L;
			final Long lehrer2 = 2L;
			final Long lehrer3 = 3L;

			listLehrer.add(lehrer1);
			listLehrer.add(lehrer2);
			listLehrer.add(lehrer3);

			if (anzahl >= 2) {
				final Long klassenleitung4 = 4L;

				listKlassenleitungen.add(klassenleitung4);
			}
		}


		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorKll10KlassenKlassenleitungslisteLehrkraft validator =
				new ValidatorKll10KlassenKlassenleitungslisteLehrkraft(() -> listKlassenleitungen, () -> listLehrer, kontext);
		assertEquals(result, validator.pruefe());
	}


}
