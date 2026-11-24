package de.svws_nrw.asd.validate.kurse;

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
 *   <li> {@link ValidatorUzl10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft}
 * </ul>
 * </p>

 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 * Für jeden Testfall ist eine Methode vorgesehen, in der mittels setzeTestdaten(...) die zugehörigen Testfälle erzeugt werden.
 *
 */
@DisplayName("Tests ValidatorUzl10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft")
class TestValidatorUzl10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft {

	private static final String TESTDATEN = """
			0, true
			1, false
			2, false
			5, true
			6, true
		 null, true
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
	 * Test von ValidatorUzl10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft
	 *
	 * CoreType: KursDaten
	 * @param idLehrer die ID des Kurslehrers
	 * @param result   gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorUzl10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorUzl10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft(final Long idLehrer, final boolean result) {

		final List<Long> idsWeitereLehrer = new ArrayList<>();

		if (idLehrer != null && idLehrer != 6) {
			// Liste Ds weiterer Lehrer erzeugen
			idsWeitereLehrer.add(1L);
			idsWeitereLehrer.add(2L);
			idsWeitereLehrer.add(3L);
		}
		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorUzl10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft validator =
				new ValidatorUzl10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft(() -> idsWeitereLehrer.isEmpty() ? null : idsWeitereLehrer, () -> idLehrer, kontext);
		assertEquals(result, validator.pruefe());
	}

}
