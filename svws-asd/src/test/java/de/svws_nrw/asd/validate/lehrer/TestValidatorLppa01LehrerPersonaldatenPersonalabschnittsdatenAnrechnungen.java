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
 * Testklasse für den Validator {@link ValidatorLppa01LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen}.
 */
@DisplayName("Teste den Validator für gültige Anrechnungsgründe (LPPA01)")
class TestValidatorLppa01LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen {

	/** Stammdaten der Schule für den Kontext. */
	private static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	/**
	 * Testdaten für die Existenzprüfung im Katalog:
	 * Spalte 1: idGrund (ID aus dem Katalog der Anrechnungsgründe)
	 * Spalte 2: Erwartetes Ergebnis (true = ID existiert, false = ID unbekannt)
	 */
	private static final String LPPA01_TESTDATEN = """
			310000, true
			500000, true
			999999, false
			""";

	/**
	 * Initialisiert die Core-Types für die Tests.
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@DisplayName("LPPA01: Test der Existenzprüfung gegen LehrerAnrechnungsgrund.json")
	@ParameterizedTest(name = "idGrund={0} -> erwartet {1}")
	@CsvSource(textBlock = LPPA01_TESTDATEN, nullValues = { "null" })
	void testValidatorLppa01AnrechnungsstundenGueltigkeit(final Long idGrund, final boolean expectedResult) {
		final ValidatorKontext kontext = new ValidatorKontext(
				testdaten_001.schule.schulNr,
				Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
				testdaten_001.schule.abschnitte,
				testdaten_001.schule.idSchuljahresabschnitt,
				true);

		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> anrechnungenListe = new ArrayList<>();
		final LehrerPersonalabschnittsdatenAnrechnungsstunden eintrag = new LehrerPersonalabschnittsdatenAnrechnungsstunden();
		eintrag.id = 1;
		eintrag.idGrund = idGrund;
		eintrag.anzahl = 1.0;
		anrechnungenListe.add(eintrag);

		final ValidatorLppa01LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen validator =
				new ValidatorLppa01LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(
						() -> anrechnungenListe,
						kontext);

		assertEquals(expectedResult, validator.pruefe(), "Fehler bei Validierung der idGrund: " + idGrund);
	}
}
