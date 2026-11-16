package de.svws_nrw.asd.validate.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

/**
 * <p> Testklasse für die Validatoren
 * <ul>
 *   <li> {@link ValidatorLppa13LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen}
 * </ul>
 * </p>
 */
@DisplayName("Tests ValidatorLppa13LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen")
class TestValidatorLppa13LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen {

	// Katalog-IDs (historie.id):
	// LehrerMehrleistungsarten: 1 = 'ID_100', 2 = 'ID_110'
	private static final String TESTDATEN = """
			idGrund1 , idGrund2 , result
			null     , null     , true
			1        , null     , true
			1        , 2        , true
			1        , 1        , false
		""";

	/** Stammdaten der Schule */
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
	 * Test von ValidatorLppa13LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen
	 *
	 * @param idGrund1   die erste Mehrleistungs-ID
	 * @param idGrund2   die zweite Mehrleistungs-ID
	 * @param result     gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLppa13LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen")
	@ParameterizedTest
	@CsvSource(useHeadersInDisplayName = true, textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorLppa13LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(
			final Long idGrund1,
			final Long idGrund2,
			final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> mehrleistung = new ArrayList<>();
		if (idGrund1 != null) {
			final LehrerPersonalabschnittsdatenAnrechnungsstunden eintrag1 = new LehrerPersonalabschnittsdatenAnrechnungsstunden();
			eintrag1.idGrund = idGrund1;
			mehrleistung.add(eintrag1);
		}
		if (idGrund2 != null) {
			final LehrerPersonalabschnittsdatenAnrechnungsstunden eintrag2 = new LehrerPersonalabschnittsdatenAnrechnungsstunden();
			eintrag2.idGrund = idGrund2;
			mehrleistung.add(eintrag2);
		}

		final ValidatorLppa13LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen validator =
				new ValidatorLppa13LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(() -> mehrleistung, kontext);
		assertEquals(result, validator.pruefe());
	}

}
