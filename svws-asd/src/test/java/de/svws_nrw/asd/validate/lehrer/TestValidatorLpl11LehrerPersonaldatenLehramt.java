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
import de.svws_nrw.asd.validate.DateManager;
import de.svws_nrw.asd.validate.InvalidDateException;
import de.svws_nrw.asd.validate.ValidatorKontext;

/**
 * <p> Testklasse für die Validatoren
 * <ul>
 *   <li> {@link ValidatorLplLehrerPersonaldatenLehramt},
 * </ul>
 * </p>
 */
@DisplayName("Tests für ValidatorLplLehrerPersonaldatenLehramt")
class TestValidatorLpl11LehrerPersonaldatenLehramt {

	// id (Lehramt), idLehrer, idKatalogLehramt, Geburtsjahr, Schulform, result
	// bei den ids 555,556,666 und 667 wird getestet, ob ein überhaupt ein Lehramt vorliegt
	private static final String TESTDATEN_LEHRAMT_MEHRMALS = """
			666,   '2005-10-02',  GY,	true
			87,    '1995-01-02',  GY,	true
			88,    '2005-01-02',  GY,	false
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
	 * Test von ValidatorLpl11LehrerPersonaldatenLehramt
	 *
	 * @param id   				die Id des Lehramt-Eintrages
	 * @param geburtsdatum      geburtsdatum
	 * @param schulform       	schulform
	 * @param result			gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLplLehrerPersonaldatenLehramt")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_LEHRAMT_MEHRMALS)
	void testValidatorLpl11LehrerPersonaldatenLehramt(final long id, final String geburtsdatum, final String schulform, final boolean result) {

		// Testdaten setzen
		final LehrerPersonaldaten lehrerPersonaldaten = new LehrerPersonaldaten();
		lehrerPersonaldaten.id = 1;
		testdaten_001.schule.schulform = schulform;

		final LehrerLehramtEintrag lehrerLehramtEintrag1 = new LehrerLehramtEintrag();
		lehrerLehramtEintrag1.idKatalogLehramt = id;

		final List<LehrerLehramtEintrag> listLehrerLehramtEintrag = new ArrayList<>();

		listLehrerLehramtEintrag.add(lehrerLehramtEintrag1);

		lehrerPersonaldaten.lehraemter.addAll(listLehrerLehramtEintrag);

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorLpl11LehrerPersonaldatenLehramt validator =
				new ValidatorLpl11LehrerPersonaldatenLehramt(
						() -> listLehrerLehramtEintrag,
						() -> {
							try {
								return DateManager.from(geburtsdatum);
							} catch (@SuppressWarnings("unused") final InvalidDateException e) {
								return null;
							}
						},
						kontext
				);
		assertEquals(result, validator.run());
	}

}
