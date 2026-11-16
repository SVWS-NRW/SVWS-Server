package de.svws_nrw.asd.validate.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.types.lehrer.LehrerLehramt;
import de.svws_nrw.asd.types.lehrer.LehrerRechtsverhaeltnis;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Testklasse für den Validator {@link ValidatorLppr15LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis}.
 *
 * Die Testdaten werden aus einer statischen JSON-Ressource eingelesen.
 */
@DisplayName("Tests ValidatorLppr15LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis")
class TestValidatorLppr15LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis {

	// idRechtsverhaeltnis: 9 = 'W' (Beamte auf Widerruf / LAA), 1 = anderes Rechtsverhältnis
	// bezeichnerLehramt:   'ID_98' = Lehramtsanwärter/-in / Studienreferendar/-in, 'ID_00' = anderes Lehramt
	private static final String TESTDATEN = """
			1    , 'ID_98' , true
			1    , 'ID_00' , true
			1    , null    , true
			null , 'ID_00' , true
			-123 , 'ID_00' , true
			9    , 'ID_98' , true
			9    , 'ID_00' , false
			9    , null    , false
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
	 * Test von ValidatorLppr15LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis.
	 *
	 * @param idRechtsverhaeltnis   die Katalog-ID des Rechtsverhältnisses
	 * @param bezeichnerLehramt     der Bezeichner des Lehramts (z. B. 'ID_98')
	 * @param result                gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLppr15LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorLppr15LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(
			final Long idRechtsverhaeltnis,
			final String bezeichnerLehramt,
			final boolean result) {

		final @NotNull Supplier<@AllowNull LehrerRechtsverhaeltnis> rechtsverhaeltnis =
				() -> LehrerRechtsverhaeltnis.data().getWertByIDOrNull(idRechtsverhaeltnis);

		final List<Long> lehraemter = new ArrayList<>();
		if (bezeichnerLehramt != null) {
			final LehrerLehramt lehramt = LehrerLehramt.data().getWertByBezeichner(bezeichnerLehramt);
			lehraemter.add(LehrerLehramt.data().getHistorieByWert(lehramt).get(0).id);
		}

		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorLppr15LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis validator =
				new ValidatorLppr15LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(rechtsverhaeltnis, () -> lehraemter, kontext);
		assertEquals(result, validator.pruefe());
	}

}
