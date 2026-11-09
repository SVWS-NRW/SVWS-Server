package de.svws_nrw.asd.validate.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.types.lehrer.LehrerRechtsverhaeltnis;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.DateManager;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Testklasse für den Validator {@link ValidatorLppr11LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis}.
 *
 * Die Testdaten werden aus einer statischen JSON-Ressource eingelesen.
 */
@DisplayName("Tests ValidatorLppr11LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis")
class TestValidatorLppr11LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis {

	private static final String TESTDATEN = """
			2000    ,1    ,'1981-01-01'  , true
			2000    ,null ,'1981-01-01'  , true
			2000    ,2    ,null          , true
			2000    ,-123 ,'1981-01-01'  , true
			2018    ,1    ,'1981-01-01'  , true
			2018    ,2    ,'1949-01-01'  , false
			2018    ,2    ,'1990-01-01'  , true
			2025    ,2    ,'1969-01-01'  , false
			2025    ,2    ,'1970-01-01'  , true
			2025    ,2    ,'2006-01-01'  , false
			2025    ,2    ,'2005-01-01'  , true
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
	 * Test von ValidatorLppr11LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis.
	 *
	 * @param schuljahr                 Schuljahr
	 * @param idRechtsverhaeltnis       die Katalog-ID des Rechtsverhaeltnisses
	 * @param geburtsdatum              das Geburtsdatum des Lehrers
	 * @param result                    gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLppr11LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN, nullValues = { "null" })
	void l(final int schuljahr, final Long idRechtsverhaeltnis, final String geburtsdatum, final boolean result) {

		final @NotNull Supplier<LehrerRechtsverhaeltnis> rechtsverhaeltnisNotNull =
				() -> LehrerRechtsverhaeltnis.data().getWertByIDOrNull(idRechtsverhaeltnis);

		@NotNull Supplier<@AllowNull DateManager> supplierGeburtsdatumNullable;
		try {
			final @NotNull DateManager datum = DateManager.from(geburtsdatum);
			supplierGeburtsdatumNullable =  () -> datum;
		} catch (Exception e) {
			supplierGeburtsdatumNullable = () -> null;
		}

		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorLppr11LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis validator =
				new ValidatorLppr11LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(() -> schuljahr, rechtsverhaeltnisNotNull, supplierGeburtsdatumNullable, kontext);
		assertEquals(result, validator.pruefe());
	}

}
