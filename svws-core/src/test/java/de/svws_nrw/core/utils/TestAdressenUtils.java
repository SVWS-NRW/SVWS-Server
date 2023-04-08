package de.svws_nrw.core.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import de.svws_nrw.base.CsvReader;

/**
 * Diese Klasse enthält die Testroutinen für die Klasse {@link AdressenUtils}
 */
@DisplayName("Teste de.svws_nrw.core.utils.TestAdressenUtils")
public class TestAdressenUtils {

	/** Testdaten für das Testen des Aufteilens von Strasseninformationen in Name, Hausnummer und Zusatz */
	static List<TestdatenSplitStrasse> testdatenSplitStrasse;

    /**
     * Initialisiert den Test
     */
    @BeforeAll
    static void setup() {
        System.out.println(" - Lade Testdaten aus CSV-Resourcen...");
        testdatenSplitStrasse = CsvReader.fromResourceWithEmptyValues("de/svws_nrw/core/utils/TestdatenSplitStrasse.csv", TestdatenSplitStrasse.class);
        if ((testdatenSplitStrasse == null) || testdatenSplitStrasse.size() == 0)
        	fail("Fehler beim Laden der Testdaten!");
        System.out.println("    FERTIG!");
        System.out.println();
    }


    /**
     * Testet die Methode {@link AdressenUtils#splitStrasse(String)}
     *
     * @return ein Stream der Testfälle als {@link DynamicTest}-Objekte
     */
    @TestFactory
    @DisplayName("Teste Aufteilung von Strassen in Name, Hausnummer und Zusatz ...")
    Stream<DynamicTest> pruefePrognose() {
        System.out.println("  - Prüfe Aufteilung für " + testdatenSplitStrasse.size() + " Strassen:");
        final ArrayList<DynamicTest> tests = new ArrayList<>();
        testdatenSplitStrasse.forEach((data) -> {
        	tests.add(DynamicTest.dynamicTest(
				"Strasse \"" + data.strasse + "\"",
				() -> {
		        	System.out.println();
		            System.out.println("    - Prüfe Strasse \"" + data.strasse + "\":");
		            final String[] aufgeteilt = AdressenUtils.splitStrasse(data.strasse);
		            System.out.println("        -> Name: \"" + aufgeteilt[0] + "\" (erwartet: \"" + data.name + "\")");
		            System.out.println("        -> Hausnummer: \"" + aufgeteilt[1] + "\" (erwartet: \"" + data.hausNr + "\")");
		            System.out.println("        -> Zusatz: \"" + aufgeteilt[2] + "\" (erwartet: \"" + data.zusatz + "\")");

		            // Ausgabe überprüfen
		            assertEquals(aufgeteilt[0], data.name, "Fehler: Name der Strasse stimmt nicht mit den Testdaten überein!");
		            assertEquals(aufgeteilt[1], data.hausNr, "Fehler: Bezeichnung der Hausnummer stimmt nicht mit den Testdaten überein!");
		            assertEquals(aufgeteilt[2], data.zusatz, "Fehler: Zusatz zur Hausnummer stimmt nicht mit den Testdaten überein!");
				}));
        });
        return tests.stream();
    }

}
