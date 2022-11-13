package de.nrw.schule.svws.abschluesse.ge.test;

import de.nrw.schule.svws.base.ResourceUtils;
import de.nrw.schule.svws.core.abschluss.AbschlussManager;
import de.nrw.schule.svws.core.abschluss.ge.ServiceAbschlussHA10;
import de.nrw.schule.svws.core.abschluss.ge.ServiceAbschlussHA9;
import de.nrw.schule.svws.core.abschluss.ge.ServiceAbschlussMSA;
import de.nrw.schule.svws.core.abschluss.ge.ServiceBerechtigungMSAQ;
import de.nrw.schule.svws.core.abschluss.ge.ServicePrognose;
import de.nrw.schule.svws.core.data.abschluss.AbschlussErgebnis;
import de.nrw.schule.svws.core.logger.LogLevel;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Map;
import java.util.Vector;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Diese Klasse enthält die Testroutinen für die Abschlussberechnung an der 
 * Gesamtschule.
 */
@DisplayName("Teste Abschlussberechnung für die Gesamtschule")
public class TestGEAbschluesse {

	/** Eine MAP mit den Testfällen aus den zugeörigen JSON-Dateien */
    static Map<String, GEAbschlussTestfall> notenBilder; 

    /**
     * Initialisiert den Test und lädt dafür die Notenbilder aus den 
     * zugehörigen JSON-Dateien mit den Testfällen.
     */
    @BeforeAll
    static void setup() {
        System.out.println(" - Lade Notenbilder aus den JSON-Resourcen...");
        notenBilder = ResourceUtils.json2Classes("de.nrw.schule.svws.abschluesse.ge.test", "geabschlusstest_", GEAbschlussTestfall.class);
        if ((notenBilder == null) || notenBilder.size() == 0)
        	fail("Fehler beim laden der Notenbilder!");
        System.out.println("    FERTIG!");
        System.out.println();
    }


    /**
     * Führt für alle Testfälle eine Abschlussprognose durch.
     * 
     * @return ein Stream der Testfälle als {@link DynamicTest}-Objekte
     */
    @TestFactory
    @DisplayName("Teste Prognose ...")
    Stream<DynamicTest> pruefePrognose() {
        System.out.println("  - Prüfe Abschluss-Prognose für " + notenBilder.size() + " Notenbilder:");
        Vector<DynamicTest> tests = new Vector<>();
        notenBilder.forEach((name, data) -> {
        	tests.add(DynamicTest.dynamicTest(
				"ID " + name + "", 
				() -> {
		        	System.out.println();
		            System.out.println("    - Prüfe Notenbild mit der ID " + name + ":");
		            ServicePrognose abschlussBerechnung = new ServicePrognose();
		            AbschlussErgebnis output = abschlussBerechnung.handle(data.input);            
		            System.out.println(abschlussBerechnung.getLog().getText(LogLevel.DEBUG, "        "));
		            
		            // Ausgabe überprüfen
		            boolean testAbschluss = AbschlussManager.equalsAbschluesse(output.abschluss, data.prognose.abschluss);
		            boolean testAbschlussErworben = (output.erworben == data.prognose.erworben);
		            System.out.println("    - Vergleiche Prognose-Ergebnis mit den Vorgaben der JSON-Datei (" + AbschlussManager.getAbschluss(data.prognose) + "): " + (testAbschluss ? "OK" : "FEHLER"));
		            assertTrue(testAbschluss, "Fehler: Prognose stimmt nicht mit den Testdaten überein!");
		            assertTrue(testAbschlussErworben, "Fehler: Prognose stimmt nicht mit den Testdaten überein! (Attribut erworben)");
				}));
        });
        return tests.stream();
    }



    /**
     * Führt für alle Testfälle eine Abschlussberechnung auf den HA9-Abschluss durch.
     * 
     * @return ein Stream der Testfälle als {@link DynamicTest}-Objekte
     */
    @TestFactory
    @DisplayName("Teste HA 9 ...")
    Stream<DynamicTest> pruefeHA9() {
        System.out.println("  - Prüfe HA9:");
        Vector<DynamicTest> tests = new Vector<>();
        notenBilder.forEach((name, data) -> {
        	if ("09".equals(data.input.jahrgang) || "9".equals(data.input.jahrgang))
	        	tests.add(DynamicTest.dynamicTest(
    				"ID " + name + "", 
    				() -> {
    		        	System.out.println();
    		            System.out.println("    - Prüfe Notenbild mit der ID " + name + ":");
    		            ServiceAbschlussHA9 ha9 = new ServiceAbschlussHA9();
    		            AbschlussErgebnis output = ha9.handle(data.input);
    		            System.out.println(ha9.getLog().getText(LogLevel.DEBUG, "        "));
    					
    		            // Ausgabe überprüfen
    		            boolean testAbschluss = (output.erworben == data.ha9.erworben);
    		            System.out.println("    - Vergleiche HA9-Abschluss mit den Vorgaben der JSON-Datei (" + data.ha9.erworben + "): " + (testAbschluss ? "OK" : "FEHLER"));
    		            assertTrue(testAbschluss, "Fehler: Abschlussberechnung stimmt nicht mit den Testdaten überein!");

    		            boolean testNPFaecher = GEAbschlussTestfall.vergleicheNachpruefungsfaecher(data.ha9.npFaecher, output.npFaecher);
    		            System.out.println("    - Vergleiche Nachprüfungsfächer mit den Vorgaben der JSON-Datei (" + AbschlussManager.getNPFaecherString(data.ha9) + "): " + (testNPFaecher ? "OK" : "FEHLER"));
    		            assertTrue(testNPFaecher, "Fehler: Nachprüfungsfächer unterscheiden sich zu den Testdaten!");
	    		}));
        });
        return tests.stream();
    }
    

    /**
     * Führt für alle Testfälle eine Abschlussberechnung auf den HA10-Abschluss durch.
     * 
     * @return ein Stream der Testfälle als {@link DynamicTest}-Objekte
     */
    @TestFactory
    @DisplayName("Teste HA 10 ...")
    Stream<DynamicTest> pruefeHA10() {
        System.out.println("  - Prüfe HA10:");
        Vector<DynamicTest> tests = new Vector<>();
        notenBilder.forEach((name, data) -> {
        	if ("10".equals(data.input.jahrgang))
	        	tests.add(DynamicTest.dynamicTest(
    				"ID " + name + "", 
    				() -> {
    		        	System.out.println();
    		            System.out.println("    - Prüfe Notenbild mit der ID " + name + ":");
    		            ServiceAbschlussHA10 ha10 = new ServiceAbschlussHA10();
    		            AbschlussErgebnis output = ha10.handle(data.input);
    		            System.out.println(ha10.getLog().getText(LogLevel.DEBUG, "        "));
    		            // Ausgabe überprüfen
    		            boolean testAbschluss = (output.erworben == data.ha10.erworben);
    		            System.out.println("    - Vergleiche HA10-Abschluss mit den Vorgaben der JSON-Datei (" + data.ha10.erworben + "): " + (testAbschluss ? "OK" : "FEHLER"));
    		            assertTrue(testAbschluss, "Fehler: Abschlussberechnung stimmt nicht mit den Testdaten überein!");
    		
    		            boolean testNPFaecher = GEAbschlussTestfall.vergleicheNachpruefungsfaecher(data.ha10.npFaecher, output.npFaecher);
    		            System.out.println("    - Vergleiche Nachprüfungsfächer mit den Vorgaben der JSON-Datei (" + AbschlussManager.getNPFaecherString(data.ha10) + "): " + (testNPFaecher ? "OK" : "FEHLER"));
    		            assertTrue(testNPFaecher, "Fehler: Nachprüfungsfächer unterscheiden sich zu den Testdaten!");
	    		}));
        });
        return tests.stream();
    }


    /**
     * Führt für alle Testfälle eine Abschlussberechnung auf den Mittleren Schulabschluss durch.
     * 
     * @return ein Stream der Testfälle als {@link DynamicTest}-Objekte
     */
    @TestFactory
    @DisplayName("Teste MSA ...")
    Stream<DynamicTest> pruefeMSA() {
        System.out.println("  - Prüfe MSA:");
        Vector<DynamicTest> tests = new Vector<>();
        notenBilder.forEach((name, data) -> {
        	if ("10".equals(data.input.jahrgang))
	        	tests.add(DynamicTest.dynamicTest(
    				"ID " + name + "", 
    				() -> {
    		        	System.out.println();
    		            System.out.println("    - Prüfe Notenbild mit der ID " + name + ":");
			            ServiceAbschlussMSA msa = new ServiceAbschlussMSA();
			            AbschlussErgebnis output = msa.handle(data.input);
			            System.out.println(msa.getLog().getText(LogLevel.DEBUG, "        "));
			            // Ausgabe überprüfen
			            boolean testAbschluss = (output.erworben == data.msa.erworben);
			            System.out.println("    - Vergleiche MSA-Abschluss mit den Vorgaben der JSON-Datei (" + data.msa.erworben + "): " + (testAbschluss ? "OK" : "FEHLER"));
			            assertTrue(testAbschluss, "Fehler: Abschlussberechnung stimmt nicht mit den Testdaten überein!");
			
			            boolean testNPFaecher = GEAbschlussTestfall.vergleicheNachpruefungsfaecher(data.msa.npFaecher, output.npFaecher);
			            System.out.println("    - Vergleiche Nachprüfungsfächer mit den Vorgaben der JSON-Datei (" + AbschlussManager.getNPFaecherString(data.msa) + "): " + (testNPFaecher ? "OK" : "FEHLER"));
			            assertTrue(testNPFaecher, "Fehler: Nachprüfungsfächer unterscheiden sich zu den Testdaten!");
    				}));
        });
        return tests.stream();
    }

    /**
     * Führt für alle Testfälle eine Abschlussberechnung auf den Mittleren Schulabschluss mit der 
     * Qualifikation zum Besuch der gymnasialen Oberstufe durch.
     * 
     * @return ein Stream der Testfälle als {@link DynamicTest}-Objekte
     */
    @TestFactory
    @DisplayName("Teste MSA-Q ...")
    Stream<DynamicTest> pruefeMSAQ() {
        System.out.println("  - Prüfe MSA-Q:");
        Vector<DynamicTest> tests = new Vector<>();
        notenBilder.forEach((name, data) -> {
        	if ("10".equals(data.input.jahrgang))
	        	tests.add(DynamicTest.dynamicTest(
    				"ID " + name + "", 
    				() -> {
    		        	System.out.println();
    		            System.out.println("    - Prüfe Notenbild mit der ID " + name + ":");
			            ServiceBerechtigungMSAQ msaq = new ServiceBerechtigungMSAQ();
			            AbschlussErgebnis output = msaq.handle(data.input);
			            System.out.println(msaq.getLog().getText(LogLevel.DEBUG, "        "));
			            // Ausgabe überprüfen
			            boolean testAbschluss = (output.erworben == data.msa_q.erworben);
			            System.out.println("    - Vergleiche MSA-Q-Abschluss mit den Vorgaben der JSON-Datei (" + data.msa_q.erworben + "): " + (testAbschluss ? "OK" : "FEHLER"));
			            assertTrue(testAbschluss, "Fehler: Abschlussberechnung stimmt nicht mit den Testdaten überein!");
			
			            boolean testNPFaecher = GEAbschlussTestfall.vergleicheNachpruefungsfaecher(data.msa_q.npFaecher, output.npFaecher);
			            System.out.println("    - Vergleiche Nachprüfungsfächer mit den Vorgaben der JSON-Datei (" + AbschlussManager.getNPFaecherString(data.msa_q) + "): " + (testNPFaecher ? "OK" : "FEHLER"));
			            assertTrue(testNPFaecher, "Fehler: Nachprüfungsfächer unterscheiden sich zu den Testdaten!");
					}));
        });
        return tests.stream();
    }

}
