package de.svws_nrw.asd.validate.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

/**
 * Testklasse für den Validator {@link ValidatorLppa00LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen}.
 */
@DisplayName("Teste den Validator für Lehrer-Anrechnungsstunden (LPPA00)")
class TestValidatorLppa00LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen {

	/** Stammdaten der Schule für den Kontext. */
	private static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	/**
	 * Testdaten für die Pflichtfeldprüfung:
	 * Spalte 1: idGrund (Long-Wert oder null)
	 * Spalte 2: Erwartetes Ergebnis (boolean)
	 */
	private static final String LPPA00_TESTDATEN = """
			4713, true
			null, false
			""";

	/**
	 * Initialisiert die Core-Types für die Tests.
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@DisplayName("LPPA00: Test der Pflichtfeldprüfung für idGrund")
	@ParameterizedTest(name = "idGrund={0} -> erwartet {1}")
	@CsvSource(textBlock = LPPA00_TESTDATEN, nullValues = { "null" })
	void testValidatorLppa00AnrechnungsstundenGrund(final Long idGrund, final boolean result) {
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

		//Leere Einträge, da die Signatur des Validators die Parameter erwartet, aber nicht testet
		final List<LehrerLehramtEintrag> lehraemter = new ArrayList<>();
		final Double soll = null;

		final ValidatorLppa00LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen validator =
				new ValidatorLppa00LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(
						() -> anrechnungenListe,
						() -> lehraemter,
						() -> soll,
						kontext);

		assertEquals(result, validator.pruefe());
	}
}
