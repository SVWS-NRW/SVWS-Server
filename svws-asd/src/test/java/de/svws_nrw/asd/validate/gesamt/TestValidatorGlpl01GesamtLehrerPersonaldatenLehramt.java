package de.svws_nrw.asd.validate.gesamt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.schule.SchuleStatistikdatenGesamt;
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

	/** Basis-Testdaten (5 Lehrkräfte, alle ohne Lehramt) laden*/
	static final String gesamt = JsonReader.fromResourceOrEmptyString(
			"de/svws_nrw/asd/validate/Testdaten_001_SchuleStatistikdatenGesamt_LehrerPersonaldaten_Lehramt.json");

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
	void testValidator(final String schulform, final int anzahlMitLehramt, final boolean result) throws IOException {

		final SchuleStatistikdatenGesamt daten = JsonReader.mapper.readValue(gesamt, SchuleStatistikdatenGesamt.class);

		// Schulform setzen

		daten.stammdaten.schulform = schulform;

		// Lehrämter dynamisch verteilen:
		// Die ersten N Lehrkräfte erhalten je 1 Lehramt, der Rest 0
		for (int i = 0; i < daten.lehrerPersonaldaten.size(); i++) {
			daten.lehrerPersonaldaten.get(i).lehraemter.clear();
			if (i < anzahlMitLehramt)
				daten.lehrerPersonaldaten.get(i).lehraemter.add(new LehrerLehramtEintrag());
		}

		// Validator ausführen
		final var kontext = new ValidatorKontext(daten.stammdaten, true);
		final var validator = new ValidatorGlpl01GesamtLehrerPersonaldatenLehramt(daten.lehrerPersonaldaten, kontext);

		// Ergebnis prüfen
		assertEquals(result, validator.run());
	}
}
