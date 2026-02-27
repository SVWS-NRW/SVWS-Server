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
 * Testklasse für den Validator {@link ValidatorLppa11LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen}.
 */
@DisplayName("Teste den Validator LPPA11: Pflichtstundensoll-Abgleich für Schulverwaltungsassistenten")
class TestValidatorLppa11LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen {

	/** Stammdaten der Schule für den Kontext. */
	private static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	/**
	 * Testdaten für den Stundensummen-Abgleich:
	 * Spalte 1: Lehramt-Key (Enum-Konstante aus LehrerLehramt)
	 * Spalte 2: Grund-Key (Bezeichner aus LehrerAnrechnungsgrund)
	 * Spalte 3: Anzahl (Anrechnungsstunden)
	 * Spalte 4: Soll (Pflichtstundensoll)
	 * Spalte 5: Erwartetes Ergebnis (true = OK, false = Abweichung bei Schulverwaltungsassistenz)
	 */
	private static final String LPPA11_TESTDATEN = """
			ID_70,  ID_935,  28.0, 28.0, true
			ID_70,  ID_935,  20.0, 28.0, false
			ID_10,  ID_935,  10.0, 28.0, true
			ID_70,  ID_310,  28.0, 28.0, true
			null,   ID_935,  28.0, 28.0, true
			""";

	/**
	 * Initialisiert die Core-Types für die Tests.
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@DisplayName("LPPA11: Vergleich Summe Anrechnung 935 mit Pflichtstundensoll")
	@ParameterizedTest(name = "Lehramt {0}, Grund {1}, Summe {2} vs Soll {3}")
	@CsvSource(textBlock = LPPA11_TESTDATEN, nullValues = { "null" })
	void testValidatorLppa11SchulverwaltungsassistenzStundensumme(final String lehramtKey, final String grundKey, final Double anzahl, final Double soll,
			final boolean expectedResult) {

		final ValidatorKontext kontext = new ValidatorKontext(
				testdaten_001.schule.schulNr,
				Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
				testdaten_001.schule.abschnitte,
				testdaten_001.schule.idSchuljahresabschnitt,
				true);

		// Lehrämter vorbereiten: ID über Enum und das Schuljahr auflösen
		final List<LehrerLehramtEintrag> lehraemter = new ArrayList<>();
		if (lehramtKey != null) {
			final LehrerLehramt lehramt = LehrerLehramt.valueOf(lehramtKey);
			final LehrerLehramtEintrag le = new LehrerLehramtEintrag();
			le.idKatalogLehramt = lehramt.daten(kontext.getSchuljahr()).id;
			lehraemter.add(le);
		}

		// Anrechnungen vorbereiten: ID über Bezeichner im Katalog auflösen
		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> anrechnungen = new ArrayList<>();
		if (grundKey != null) {
			final LehrerAnrechnungsgrund grund = LehrerAnrechnungsgrund.data().getWertByBezeichner(grundKey);
			final LehrerPersonalabschnittsdatenAnrechnungsstunden an = new LehrerPersonalabschnittsdatenAnrechnungsstunden();
			// Sicherstellen, dass die ID für das aktuelle Schuljahr aus dem Katalog gezogen wird
			an.idGrund = (grund != null) ? grund.daten(kontext.getSchuljahr()).id : -999L;
			an.anzahl = (anzahl != null) ? anzahl : 0.0;
			anrechnungen.add(an);
		}

		final ValidatorLppa11LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen validator =
				new ValidatorLppa11LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(
						() -> anrechnungen,
						() -> lehraemter,
						() -> soll,
						kontext);

		assertEquals(expectedResult, validator.pruefe(),
				String.format("Fehlgeschlagen für Lehramt %s und Grund %s (Summe %s vs Soll %s)", lehramtKey, grundKey, anzahl, soll));
	}
}
