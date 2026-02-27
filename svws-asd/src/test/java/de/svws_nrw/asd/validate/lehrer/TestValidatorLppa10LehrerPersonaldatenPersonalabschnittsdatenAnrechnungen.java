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
import de.svws_nrw.asd.types.lehrer.LehrerAnrechnungsgrund;
import de.svws_nrw.asd.types.lehrer.LehrerLehramt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

/**
 * Testklasse für den Validator {@link ValidatorLppa10LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen}.
 */
@DisplayName("Teste den Validator LPPA10: Spezialprüfung für Schulverwaltungsassistenten")
class TestValidatorLppa10LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen {

	/** Stammdaten der Schule für den Kontext. */
	private static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	/**
	 * Testdaten für die Exklusivitätsprüfung:
	 * Spalte 1: Bezeichner für das Lehramt (aus LehrerLehramt Enum)
	 * Spalte 2: Bezeichner für den Anrechnungsgrund (aus LehrerAnrechnungsgrund)
	 * Spalte 3: Erwartetes Ergebnis (true = OK, false = unzulässige Kombination)
	 */
	private static final String LPPA10_TESTDATEN = """
			ID_70, ID_935, true
			ID_70, ID_310, false
			ID_10, ID_310, true
			null,  ID_935, true
			""";

	/**
	 * Initialisiert die Core-Types für die Tests.
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@DisplayName("LPPA10: Prüfung der Anrechnungsgründe bei Lehramt ID_70")
	@ParameterizedTest(name = "Lehramt {0} mit Grund {1} -> erwartet {2}")
	@CsvSource(textBlock = LPPA10_TESTDATEN, nullValues = { "null" })
	void testValidatorLppa10Schulverwaltungsassistenz(final String lehramtKey, final String grundKey, final boolean expectedResult) {

		final ValidatorKontext kontext = new ValidatorKontext(
				testdaten_001.schule.schulNr,
				Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
				testdaten_001.schule.abschnitte,
				testdaten_001.schule.idSchuljahresabschnitt,
				true);

		// Lehrämter vorbereiten: Auflösung über Enum-Key und Schuljahr
		final List<LehrerLehramtEintrag> lehraemter = new ArrayList<>();
		if (lehramtKey != null) {
			final LehrerLehramt lehramt = LehrerLehramt.valueOf(lehramtKey);
			final LehrerLehramtEintrag le = new LehrerLehramtEintrag();
			le.idKatalogLehramt = lehramt.daten(kontext.getSchuljahr()).id;
			lehraemter.add(le);
		}

		// Anrechnungen vorbereiten: Auflösung über Katalog-Bezeichner
		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> anrechnungen = new ArrayList<>();
		if (grundKey != null) {
			final LehrerAnrechnungsgrund grund = LehrerAnrechnungsgrund.data().getWertByBezeichner(grundKey);
			final LehrerPersonalabschnittsdatenAnrechnungsstunden an = new LehrerPersonalabschnittsdatenAnrechnungsstunden();
			an.idGrund = (grund != null) ? grund.daten(kontext.getSchuljahr()).id : -999L;
			an.anzahl = 1.0;
			anrechnungen.add(an);
		}

		final ValidatorLppa10LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen validator =
				new ValidatorLppa10LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(
						() -> anrechnungen,
						() -> lehraemter,
						kontext);

		assertEquals(expectedResult, validator.pruefe(),
				String.format("Fehlgeschlagen für Lehramt %s und Grund %s", lehramtKey, grundKey));
	}
}
