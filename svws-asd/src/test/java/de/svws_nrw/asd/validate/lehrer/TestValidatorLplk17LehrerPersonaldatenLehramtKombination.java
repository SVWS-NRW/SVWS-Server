package de.svws_nrw.asd.validate.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.types.lehrer.LehrerLehramt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

@DisplayName("TestValidatorLplk17: Prüfung der Lehramtskombinationen")
class TestValidatorLplk17LehrerPersonaldatenLehramtKombination {

	/** Stammdaten der Schule */
	static StatistikGesamt testdaten_001 = JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
		// Schuljahr auf 2024 (ID 20 im JSON), damit die Core-Types sicher existieren
		testdaten_001.schule.idSchuljahresabschnitt = 20;
	}

	/**
	 * Hilfsmethode zur Durchführung der Validierung.
	 * @param result  Das erwartete Ergebnis (true = kein Fehler, false = Fehler gefunden)
	 * @param typen     Liste der Lehrämter, die der Lehrer hat (als Array)
	 */
	private static void teste(final boolean result, final LehrerLehramt[] typen) {
		final List<LehrerLehramtEintrag> liste = new ArrayList<>();
		for (final LehrerLehramt typ : typen) {
			final LehrerLehramtEintrag e = new LehrerLehramtEintrag();
			e.idKatalogLehramt = typ.daten(2024).id;
			liste.add(e);
		}

		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		final ValidatorLplk17LehrerPersonaldatenLehramtKombination validator =
				new ValidatorLplk17LehrerPersonaldatenLehramtKombination(() -> liste, kontext);

		assertEquals(result, validator.run());
	}

	@Test
	@DisplayName("'Handwerksmeistern/-innen' (ID_64) + normales Lehramt ist VERBOTEN")
	void testHandwerksmeisterUndAnderes() {
		// ID_64 (Handwerksmeistern/-innen) + ID_25 (Gymnasium) - Explizites Array statt Varargs
		teste(false, new LehrerLehramt[] { LehrerLehramt.ID_64, LehrerLehramt.ID_25 });
	}

	@Test
	@DisplayName("Nur Lehramt 'Handwerksmeistern/-innen' ist ERLAUBT")
	void testNurHandwerksmeister() {
		// ID_64 (Handwerksmeistern/-innen) - Explizites Array statt Varargs
		teste(true, new LehrerLehramt[] { LehrerLehramt.ID_64 });
	}

	@Test
	@DisplayName("Nur normales Lehramt ist ERLAUBT")
	void testNurNormalesLehramt() {
		// ID_25 (Gymnasium) - Explizites Array statt Varargs
		teste(true, new LehrerLehramt[] { LehrerLehramt.ID_25 });
	}
}
