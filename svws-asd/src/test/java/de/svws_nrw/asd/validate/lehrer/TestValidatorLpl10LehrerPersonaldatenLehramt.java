package de.svws_nrw.asd.validate.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerPersonaldaten;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

/**
 * <p> Testklasse für die Validatoren
 * <ul>
 *   <li> {@link ValidatorLplLehrerPersonaldatenLehramt},
 * </ul>
 * </p>
 */
@DisplayName("Tests für ValidatorLplLehrerPersonaldatenLehramt")
class TestValidatorLpl10LehrerPersonaldatenLehramt {

	// id (Lehramt), idLehrer, idKatalogLehramt, Geburtsjahr, Schulform, result
	// bei den ids 555,556,666 und 667 wird getestet, ob ein überhaupt ein Lehramt vorliegt
	private static final String TESTDATEN_LEHRAMT_MEHRMALS = """
			116,   1,      0,    GY,	true
			117,   1,    112,    GY,	false
			115,   1,    113,    GY,	false
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
	 * Test von ValidatorLplLehrerPersonaldatenLehramt
	 *
	 * @param id   				die Id des Lehramt-Eintrages
	 * @param idLehrer			die Id des Lehrers
	 * @param idKatalogLehramt	die Katalog-ID des Lehramts, welche bei den eingelesenen Testdaten ersetzt wird
	 * @param schulform       	schulform
	 * @param result			gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLplLehrerPersonaldatenLehramt")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_LEHRAMT_MEHRMALS)
	void testValidatorLplLehrerPersonaldatenLehramt(final long id, final long idLehrer, final long idKatalogLehramt, final String schulform, final boolean result) {

		// Testdaten setzen
		LehrerPersonaldaten lehrerPersonaldaten = new LehrerPersonaldaten();
		lehrerPersonaldaten.id = 1;
		testdaten_001.schule.schulform = schulform;

		LehrerLehramtEintrag lehrerLehramtEintrag1 = new LehrerLehramtEintrag();
		LehrerLehramtEintrag lehrerLehramtEintrag2 = new LehrerLehramtEintrag();
		LehrerLehramtEintrag lehrerLehramtEintrag3 = new LehrerLehramtEintrag();
		LehrerLehramtEintrag lehrerLehramtEintrag4 = new LehrerLehramtEintrag();
		LehrerLehramtEintrag lehrerLehramtEintrag5 = new LehrerLehramtEintrag();

		lehrerLehramtEintrag1.idKatalogLehramt = 111;
		lehrerLehramtEintrag2.idKatalogLehramt = 112;
		lehrerLehramtEintrag3.idKatalogLehramt = 113;
		lehrerLehramtEintrag4.idKatalogLehramt = 114;

		lehrerLehramtEintrag5.idKatalogLehramt = idKatalogLehramt;
		lehrerLehramtEintrag5.idLehrer = idLehrer;

		List<LehrerLehramtEintrag> listLehrerLehramtEintrag = new ArrayList<>();

		if (id != 555 && id != 556 && id != 666 && id != 667) {
			listLehrerLehramtEintrag.add(lehrerLehramtEintrag1);
			listLehrerLehramtEintrag.add(lehrerLehramtEintrag2);
			listLehrerLehramtEintrag.add(lehrerLehramtEintrag3);
			listLehrerLehramtEintrag.add(lehrerLehramtEintrag4);
			listLehrerLehramtEintrag.add(lehrerLehramtEintrag5);
		} else {
			if (id == 556 || id == 666) {
				listLehrerLehramtEintrag.add(lehrerLehramtEintrag1);
			}
		}

		lehrerPersonaldaten.lehraemter.addAll(listLehrerLehramtEintrag);

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
				testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorLpl10LehrerPersonaldatenLehramt validator = new ValidatorLpl10LehrerPersonaldatenLehramt(() -> listLehrerLehramtEintrag, kontext);
		assertEquals(result, validator.run());
	}

}
