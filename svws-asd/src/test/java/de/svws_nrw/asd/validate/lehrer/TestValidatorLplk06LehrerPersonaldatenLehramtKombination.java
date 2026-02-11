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
import de.svws_nrw.asd.data.schule.SchuleStammdaten;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

/**
 * <p> Testklasse für die Validatoren
 * <ul>
 *   <li> {@link TestValidatorLplk06LehrerPersonaldatenLehramtKombination},
 * </ul>
 * </p>
 *
 * <p> Testdaten:
 *   <ul>
 *     <li> de/svws_nrw/asd/validate/schule/Testdaten_001_SchuleStammdaten.json
 *   </ul>
 * </p>
 *
 */
@DisplayName("Tests für TestValidatorLplk06LehrerPersonaldatenLehramtKombination")
class TestValidatorLplk06LehrerPersonaldatenLehramtKombination {

	// id1 (Lehramt), idLehrer1, idKatalogLehramt1, id2 (Lehramt), idLehrer2, idKatalogLehramt2, id3 (Lehramt), idLehrer3, idKatalogLehramt3, result
	// idKatalogLehramt = 112 entspricht ID_57
	// idKatalogLehramt = 115 entspricht ID_60
	private static final String TESTDATEN_LEHRAMT_KOMBINATIONEN = """
			111,   1,    112,	112,   1,    115,	113,   1,    124,	false
			111,   1,    112,	112,   1,    120,	113,   1,    124,	true
			111,   1,    115,	112,   1,    120,	113,   1,    124,	true
		""";

	/** Stammdaten der Schule */
	static final SchuleStammdaten schuleTestdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/schule/Testdaten_001_SchuleStammdaten.json", SchuleStammdaten.class);

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
	 * Test von ValidatorLplk06LehrerPersonaldatenLehramt
	 *
	 * @param id1   			die Id des ersten Lehramt-Eintrages
	 * @param idLehrer1			die Id des ersten Lehrers
	 * @param idKatalogLehramt1	die Katalog-ID des ersten Lehramts, welche bei den eingelesenen Testdaten ersetzt wird
	 * @param id2 			die Id des zweiten Lehramt-Eintrages
	 * @param idLehrer2			die Id des zweiten Lehrers
	 * @param idKatalogLehramt2	die Katalog-ID des zweiten Lehramts, welche bei den eingelesenen Testdaten ersetzt wird
	 * @param id3   			die Id des dritten Lehramt-Eintrages
	 * @param idLehrer3			die Id des dritten Lehrers
	 * @param idKatalogLehramt3	die Katalog-ID des dritten Lehramts, welche bei den eingelesenen Testdaten ersetzt wird
	 * @param result			gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLplk06LehrerPersonaldatenLehramtKombination")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_LEHRAMT_KOMBINATIONEN)
	void testValidatorLplk06LehrerPersonaldatenLehramtKombination(final long id1, final long idLehrer1, final long idKatalogLehramt1, final long id2, final long idLehrer2, final long idKatalogLehramt2, final long id3, final long idLehrer3, final long idKatalogLehramt3, final boolean result) {

		// Testdaten setzen
		LehrerPersonaldaten lehrerPersonaldaten = new LehrerPersonaldaten();
		lehrerPersonaldaten.id = 1;

		LehrerLehramtEintrag lehrerLehramtEintrag1 = new LehrerLehramtEintrag();
		LehrerLehramtEintrag lehrerLehramtEintrag2 = new LehrerLehramtEintrag();
		LehrerLehramtEintrag lehrerLehramtEintrag3 = new LehrerLehramtEintrag();

		lehrerLehramtEintrag1.idKatalogLehramt = idKatalogLehramt1;

		lehrerLehramtEintrag2.idKatalogLehramt = idKatalogLehramt2;
		lehrerLehramtEintrag2.idLehrer = idLehrer2;

		lehrerLehramtEintrag3.idKatalogLehramt = idKatalogLehramt3;
		lehrerLehramtEintrag3.idLehrer = idLehrer3;

		List<LehrerLehramtEintrag> listLehrerLehramtEintrag = new ArrayList<>();
		listLehrerLehramtEintrag.add(lehrerLehramtEintrag1);
		listLehrerLehramtEintrag.add(lehrerLehramtEintrag2);
		listLehrerLehramtEintrag.add(lehrerLehramtEintrag3);

		lehrerPersonaldaten.lehraemter.addAll(listLehrerLehramtEintrag);

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);
		final ValidatorLplk06LehrerPersonaldatenLehramtKombination validator = new ValidatorLplk06LehrerPersonaldatenLehramtKombination(lehrerPersonaldaten, kontext);
		assertEquals(result, validator.run());
	}


}
