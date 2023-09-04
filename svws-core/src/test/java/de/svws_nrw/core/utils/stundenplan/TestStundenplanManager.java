package de.svws_nrw.core.utils.stundenplan;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.svws_nrw.base.ResourceUtils;
import de.svws_nrw.core.data.stundenplan.Stundenplan;
import de.svws_nrw.core.data.stundenplan.StundenplanKomplett;
import de.svws_nrw.core.data.stundenplan.StundenplanPausenaufsicht;
import de.svws_nrw.core.data.stundenplan.StundenplanUnterricht;
import de.svws_nrw.core.data.stundenplan.StundenplanUnterrichtsverteilung;

/**
 * Diese Klasse testet den {@link StundenplanManager} mit Testdaten.
 */
@DisplayName("Teste {@link StundenplanManager} mit Testdaten")
@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestStundenplanManager {

	/** Eine Map mit den vollständiges Stundenplandaten  */
	static Map<String, StundenplanKomplett> testStundenplaene = new HashMap<>();

	/** Eine Map mit den Jahrgängen der Gymnasialen Oberstufe aus den zugehörigen JSON-Dateien mit den Testfällen */
	static Map<String, Stundenplan> testStundenplanDefinitionen;

	/** Eine Map mit den Listen der Pausenaufsichten aus den zugehörigen JSON-Dateien mit den Testfällen */
	static Map<String, List<StundenplanPausenaufsicht>> testStundenplanPausenaufsichten;

	/** Eine Map mit den Listen der Unterrichte aus den zugehörigen JSON-Dateien mit den Testfällen */
	static Map<String, List<StundenplanUnterricht>> testStundenplanUnterrichte;

	/** Eine Map mit den Unterrichtsverteilungen für die Stundenpläne aus den zugehörigen JSON-Dateien mit den Testfällen */
	static Map<String, StundenplanUnterrichtsverteilung> testStundenplanUnterrichtsverteilungen;

	/** Eine Map mit den Stundenplan-Managern  */
	static Map<String, StundenplanManager> testStundenplanManager = new HashMap<>();



	/**
	 * Initialisiert den Test und lädt dafür die Stundenpläne aus den
	 * zugehörigen JSON-Dateien mit den Testfällen.
	 *
	 * @throws IOException bei einem Fehler beim Laden der JSON-Resourcen
	 */
	@BeforeAll
	static void setup() throws IOException {
		System.out.println("- Lade die Stundenplan-Definitionen aus den JSON-Resourcen...");
		testStundenplanDefinitionen = ResourceUtils.json2Classes("de.svws_nrw.core.utils.stundenplan", "", "_Stundenplan", Stundenplan.class);
		assert (testStundenplanDefinitionen != null) && testStundenplanDefinitionen.size() != 0 : "Fehler beim Laden der Stundenpläne!";
		System.out.println("  FERTIG!");

		System.out.println("- Lade die Stundenplan-Pausenaufsichten aus den JSON-Resourcen...");
		testStundenplanPausenaufsichten = ResourceUtils.json2Lists("de.svws_nrw.core.utils.stundenplan", "", "_StundenplanPausenaufsichten", StundenplanPausenaufsicht.class);
		assert (testStundenplanPausenaufsichten != null) && testStundenplanPausenaufsichten.size() != 0 : "Fehler beim Laden der Stundenpläne!";
		System.out.println("  FERTIG!");

		System.out.println("- Lade die Stundenplan-Unterrichte aus den JSON-Resourcen...");
		testStundenplanUnterrichte = ResourceUtils.json2Lists("de.svws_nrw.core.utils.stundenplan", "", "_StundenplanUnterrichte", StundenplanUnterricht.class);
		assert (testStundenplanUnterrichte != null) && testStundenplanUnterrichte.size() != 0 : "Fehler beim Laden der Stundenpläne!";
		System.out.println("  FERTIG!");

		System.out.println("- Lade die Stundenplan-Unterrichtsverteilungen aus den JSON-Resourcen...");
		testStundenplanUnterrichtsverteilungen = ResourceUtils.json2Classes("de.svws_nrw.core.utils.stundenplan", "", "_StundenplanUnterrichtsverteilung", StundenplanUnterrichtsverteilung.class);
		assert (testStundenplanUnterrichtsverteilungen != null) && testStundenplanUnterrichtsverteilungen.size() != 0 : "Fehler beim Laden der Unterrichtsverteilungen für die Stundenläne!";
		System.out.println("  FERTIG!");

		testStundenplanDefinitionen.forEach((praefixStundenplan, stundenplandefinition) -> {
			final StundenplanKomplett stundenplan = new StundenplanKomplett();
			stundenplan.daten = stundenplandefinition;
			stundenplan.pausenaufsichten = testStundenplanPausenaufsichten.get(praefixStundenplan);
			assert stundenplan.pausenaufsichten != null;
			stundenplan.unterrichte = testStundenplanUnterrichte.get(praefixStundenplan);
			assert stundenplan.unterrichte != null;
			stundenplan.unterrichtsverteilung = testStundenplanUnterrichtsverteilungen.get(praefixStundenplan);
			assert stundenplan.unterrichtsverteilung != null;
			testStundenplaene.put(praefixStundenplan, stundenplan);
		});

		testStundenplaene.forEach((praefixStundenplan, stundenplan) -> {
			final StundenplanManager manager = new StundenplanManager(stundenplan);
			testStundenplanManager.put(praefixStundenplan, manager);
		});
	}


	/**
	 * Testet die Abfrage der ID des Schuljahresabschnittes des Stundenplans.
	 */
	@DisplayName("Teste die Abfrage der ID des Schuljahresabschnittes des Stundenplans")
	@Test
	void testIdSchuljahreabschnitt() {
		final StundenplanManager manager = testStundenplanManager.get("01");
		assertEquals(1, manager.getIDSchuljahresabschnitt());
	}

}
