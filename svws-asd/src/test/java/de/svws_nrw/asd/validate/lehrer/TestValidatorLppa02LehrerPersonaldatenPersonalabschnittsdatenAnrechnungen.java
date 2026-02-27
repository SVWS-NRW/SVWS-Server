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
 * Testklasse für den Validator {@link ValidatorLppa02LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen}.
 */
@DisplayName("Teste den Validator für die zeitliche Gültigkeit von Anrechnungsgründen (LPPA02)")
class TestValidatorLppa02LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen {

	/** Stammdaten der Schule für den Kontext. */
	private static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	/**
	 * Testdaten basierend auf LehrerAnrechnungsgrund.json:
	 * Spalte 1: idGrund (Die numerische ID aus der Katalog-Historie)
	 * Spalte 2: Erwartetes Ergebnis (true = gültig im Schuljahr, false = ungültig/Fehler)
	 */
	private static final String LPPA02_TESTDATEN = """
			310000, true
			500000, true
			null,   true
			999999, false
			""";

	/**
	 * Initialisiert die Core-Types für die Tests.
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@DisplayName("LPPA02: Prüfung der zeitlichen Gültigkeit")
	@ParameterizedTest(name = "ID {0} ergibt {1}")
	@CsvSource(textBlock = LPPA02_TESTDATEN, nullValues = { "null" })
	void testValidatorLppa02AnrechnungsstundenZeitlicheGueltigkeit(final Long idGrund, final boolean expectedResult) {
		final ValidatorKontext kontext = new ValidatorKontext(
				testdaten_001.schule.schulNr,
				Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
				testdaten_001.schule.abschnitte,
				testdaten_001.schule.idSchuljahresabschnitt,
				true);

		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> anrechnungenListe = new ArrayList<>();
		final LehrerPersonalabschnittsdatenAnrechnungsstunden eintrag = new LehrerPersonalabschnittsdatenAnrechnungsstunden();
		eintrag.idGrund = idGrund;
		anrechnungenListe.add(eintrag);

		final ValidatorLppa02LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen validator =
				new ValidatorLppa02LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(
						() -> anrechnungenListe,
						kontext);

		assertEquals(expectedResult, validator.pruefe(), "Fehler bei der Prüfung von idGrund: " + idGrund);
	}
}
