package de.svws_nrw.asd.validate.gesamt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.statistik.LehrerStatistikGesamt;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

/**
 * <p> Testklasse für den Validator {@link ValidatorGlplGesamtLehrerPersonaldatenLehramt}.
 * </p>
 *
 * <p> Der Validator prüft:
 * <ul>
 *   <li> Bei allen Schulformen außer <code>FW</code> (Freie Waldorfschule) muss jede Lehrkraft mindestens ein Lehramt haben. </li>
 *   <li> Bei <code>FW</code> darf keine Lehrkraft ein Lehramt besitzen. </li>
 * </ul>
 * </p>
 *
 * <p> Testdaten:
 *   <ul>
 *     <li> de/svws_nrw/asd/validate/Testdaten_001_SchuleStatistikdatenGesamt_LehrerPersonaldaten_Lehramt.json </li>
 *   </ul>
 * </p>
 *
 * Die Testdaten enthalten 5 Lehrkräfte. Je nach Testfall werden die Lehrämter
 * dynamisch im Code gesetzt, um verschiedene Konstellationen zu simulieren.
 *
 * CoreType: {@link de.svws_nrw.asd.data.lehrer.LehrerPersonaldaten}
 */
@DisplayName("Tests für ValidatorGesamtLehrerPersonaldatenLehramt (5 Lehrkräfte)")
class TestValidatorGlpl01GesamtLehrerPersonaldatenLehramt {

	/** Statistikdaten der Schule*/
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	/**
	 * Initialisiert die Core-Types, damit die Tests ausgeführt werden können.
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	/**
	 * Testet den {@link ValidatorGlplGesamtLehrerPersonaldatenLehramt} mit fünf Lehrkräften.
	 *
	 * <p> Die CSV-Werte stehen für:
	 * <ul>
	 *   <li><b>schulform</b>: Schulformkürzel (z. B. "GY" oder "FW")</li>
	 *   <li><b>anzahlMitLehramt</b>: Anzahl der Lehrkräfte, die mindestens ein Lehramt haben</li>
	 *   <li><b>result</b>: Ergebnis des Validators (true/false)</li>
	 * </ul>
	 * </p>
	 *
	 * @param schulform         Schulform (GY oder FW)
	 * @param anzahlMitLehramt  Anzahl der Lehrkräfte mit Lehramt
	 * @param result          erwartetes Ergebnis (true/false)
	 *
	 * @throws IOException
	 */
	@DisplayName("Tests mit 5 Lehrkräften und variabler Lehramtsverteilung")
	@ParameterizedTest
	@CsvSource({
			// FW = kein Lehramt erlaubt
			"FW, 0, true",
			"FW, 1, false",
			"FW, 5, false"
	})
	void testValidator(final String schulform, final int anzahlMitLehramt, final boolean result) {
		testdaten_001.schule.schulform = schulform;

		// Testdatensatz setzen

		final LehrerStatistikGesamt lehrerStatistikGesamt1 = new LehrerStatistikGesamt();
		final LehrerStatistikGesamt lehrerStatistikGesamt2 = new LehrerStatistikGesamt();
		final LehrerStatistikGesamt lehrerStatistikGesamt3 = new LehrerStatistikGesamt();
		final LehrerStatistikGesamt lehrerStatistikGesamt4 = new LehrerStatistikGesamt();
		final LehrerStatistikGesamt lehrerStatistikGesamt5 = new LehrerStatistikGesamt();

		final List<LehrerStatistikGesamt> listLehrerStatistikGesamt = new ArrayList<>();
		listLehrerStatistikGesamt.add(lehrerStatistikGesamt1);
		listLehrerStatistikGesamt.add(lehrerStatistikGesamt2);
		listLehrerStatistikGesamt.add(lehrerStatistikGesamt3);
		listLehrerStatistikGesamt.add(lehrerStatistikGesamt4);
		listLehrerStatistikGesamt.add(lehrerStatistikGesamt5);

		// Lehrämter dynamisch verteilen:
		// Die ersten N Lehrkräfte erhalten je 1 Lehramt, der Rest 0
		for (int i = 0; i < listLehrerStatistikGesamt.size(); i++) {
			listLehrerStatistikGesamt.get(i).lehraemter.clear();
			if (i < anzahlMitLehramt) {
				listLehrerStatistikGesamt.get(i).lehraemter.add(new LehrerLehramtEintrag());
			}
		}

		// Validator ausführen
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final var validator = new ValidatorGlpl01GesamtLehrerPersonaldatenLehramt(() -> listLehrerStatistikGesamt, kontext);

		// Ergebnis prüfen
		assertEquals(result, validator.run());
	}
}
