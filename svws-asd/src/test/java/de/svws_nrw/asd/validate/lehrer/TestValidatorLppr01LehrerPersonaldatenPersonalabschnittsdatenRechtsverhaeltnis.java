package de.svws_nrw.asd.validate.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.DateManager;
import de.svws_nrw.asd.validate.InvalidDateException;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Testklasse für den Validator LehrerStammdaten
 *
 * Testdaten 2: de/svws_nrw/asd/validate/lehrer/Testdaten_002_LehrerPersonalabschnittsdaten.json
 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 * Für jeden Testfall ist eine Methode vorgesehen, in der mittels setzeTestdaten(...) die zugehörigen Testfälle erzeugt werden.
 *
 * CoreType: LehrerStammdaten
 */
@DisplayName("Teste den Validator zu LehrerPersonalabschnittsdaten")
class TestValidatorLppr01LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis {

	/** Stammdaten der Schule */
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	/** Personalabschnittsdaten des Lehrers */
	static final LehrerPersonalabschnittsdaten LehrerPersonalabschnittsdaten_Plausibel = JsonReader.fromResource(
			"de/svws_nrw/asd/validate/lehrer/Testdaten_Plausibel_LehrerPersonalabschnittsdaten.json",
			LehrerPersonalabschnittsdaten.class);


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
	 * Test von ValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnisGeburtsdatum auf zu junges Geburtsdatum
	 *
	 * CoreType: LehrerPersonalabschnittsdaten
	 * Testfall: Das Geburtsdatum ist für das Rechtsverhältnis zu jung
	 * Ergebnis: Validator soll FALSE liefern
	 */
	@DisplayName("TestValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnisGeburtsdatum: Fehler Geburtsdatum zu jung")
	@Test
	void testValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnisGeburtsdatum_GeburtsdatumZuJung() {
		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
				testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		try {
			final @NotNull DateManager geburtsdatum = DateManager.from("2025-01-01");
			String rechtsverhaeltnis = "L";
			Long idSchuljahresabschnitt = kontext.getSchuljahresabschnitt().id;
			final ValidatorLppr01LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis validator = new ValidatorLppr01LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(
					() -> idSchuljahresabschnitt,
					() -> rechtsverhaeltnis,
					() -> geburtsdatum,
					kontext);
			assertEquals(false, validator.run());
		} catch (@SuppressWarnings("unused") final InvalidDateException e) {
			assertEquals(true, false); // darf hier nicht hin
			// Ist kein gültiges Geburtsdatum gesetzt, so werden die Prüfungen übersprungen.
			// Die eigentliche Validierung des Geburtsdatums erfolgt bei den Lehrer-Stammdaten
		}


		}
	}
