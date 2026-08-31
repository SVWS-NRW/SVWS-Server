package de.svws_nrw.asd.validate.kurse;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.kurse.KursLehrer;
import de.svws_nrw.asd.data.statistik.LehrerStatistikGesamt;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

/**
 * <p> Testklasse für die Validatoren
 * <ul>
 *   <li> {@link ValidatorUzl01UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft}
 * </ul>
 * </p>

 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 * Für jeden Testfall ist eine Methode vorgesehen, in der mittels setzeTestdaten(...) die zugehörigen Testfälle erzeugt werden.
 *
 */
@DisplayName("Tests ValidatorUzl01UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft")
class TestValidatorUzl01UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft {

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
	 * Test von ValidatorUzl01UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft
	 *
	 * CoreType: KursDaten
	 * @param anzahl   Steuerung
	 * @param result   gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorUzl01UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorUf01UnterrichtsverteilungsdatenFach(final Integer anzahl, final boolean result) {

		final List<KursLehrer> listWeitereLehrer = new ArrayList<KursLehrer>();
		final List<LehrerStatistikGesamt> listLehrer = new ArrayList<LehrerStatistikGesamt>();

		if (anzahl > 0) {
			// Liste KursLehrer erzeugen
			final KursLehrer kursLehrer1 = new KursLehrer();
			final KursLehrer kursLehrer2 = new KursLehrer();
			final KursLehrer kursLehrer3 = new KursLehrer();

			kursLehrer1.idLehrer = 1;
			kursLehrer2.idLehrer = 2;
			kursLehrer3.idLehrer = 3;

			listWeitereLehrer.add(kursLehrer1);
			listWeitereLehrer.add(kursLehrer2);
			listWeitereLehrer.add(kursLehrer3);

			// Liste Lehrer erzeugen
			final LehrerStatistikGesamt lehrerStatistikGesamt1 = new LehrerStatistikGesamt();
			final LehrerStatistikGesamt lehrerStatistikGesamt2 = new LehrerStatistikGesamt();
			final LehrerStatistikGesamt lehrerStatistikGesamt3 = new LehrerStatistikGesamt();

			lehrerStatistikGesamt1.id = 1;
			lehrerStatistikGesamt2.id = 2;
			lehrerStatistikGesamt3.id = 3;

			listLehrer.add(lehrerStatistikGesamt1);
			listLehrer.add(lehrerStatistikGesamt2);
			listLehrer.add(lehrerStatistikGesamt3);

			if (anzahl >= 2) {
				final KursLehrer kursLehrer4 = new KursLehrer();

				kursLehrer4.idLehrer = 4;
				listWeitereLehrer.add(kursLehrer4);
			}
		}


		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorUzl01UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft validator =
				new ValidatorUzl01UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft(() -> listWeitereLehrer, () -> listLehrer, kontext);
		assertEquals(result, validator.pruefe());
	}


}
