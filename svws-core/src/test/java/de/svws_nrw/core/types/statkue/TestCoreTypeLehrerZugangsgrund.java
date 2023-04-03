package de.svws_nrw.core.types.statkue;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.types.lehrer.LehrerZugangsgrund;


/**
 * Diese Klasse enthält die Testroutinen für den Core-Type LehrerZugangsgrund.
 */
@DisplayName("Teste den Core-Type LehrerZugangsgrund")
public class TestCoreTypeLehrerZugangsgrund {

	/**
	 * Führt grundlegende Tests zu dem Core-Type LehrerZugangsgrund aus.
	 */
    @Test
    @DisplayName("Teste den Typ LehrerZugangsgrund")
    void testLehrerZugangsgrund() {
    	assertEquals(LehrerZugangsgrund.NEU, LehrerZugangsgrund.getByKuerzel("NEU"));
    	assertEquals(LehrerZugangsgrund.AndBuLand, LehrerZugangsgrund.getByKuerzel("AndBuLand"));
    	assertEquals(LehrerZugangsgrund.WECHSEL, LehrerZugangsgrund.getByKuerzel("WECHSEL"));
    	assertEquals(LehrerZugangsgrund.WIEDER, LehrerZugangsgrund.getByKuerzel("WIEDER"));
    	assertEquals(LehrerZugangsgrund.SONSTIG, LehrerZugangsgrund.getByKuerzel("SONSTIG"));

    	assertEquals(LehrerZugangsgrund.NEU.daten.schluessel, ("1"));
    	assertEquals(LehrerZugangsgrund.AndBuLand.daten.schluessel, ("2"));
    	assertEquals(LehrerZugangsgrund.WECHSEL.daten.schluessel, ("3"));
    	assertEquals(LehrerZugangsgrund.WIEDER.daten.schluessel, ("4"));
    	assertEquals(LehrerZugangsgrund.SONSTIG.daten.schluessel, ("5"));

    	assertEquals(LehrerZugangsgrund.NEU.daten.text, ("Neueintritt in den Schuldienst mit abgelegter 2. Staatsprüfung oder anderweitig erfüllter Eingangsvoraussetzung"));
        assertEquals(LehrerZugangsgrund.AndBuLand.daten.text, ("Übertritt aus dem Schuldienst eines anderen Bundeslandes"));
        assertEquals(LehrerZugangsgrund.WECHSEL.daten.text, ("Wechsel innerhalb des Landes von einer anderen Schule an die berichtende Schule"));
        assertEquals(LehrerZugangsgrund.WIEDER.daten.text, ("Wiedereintritt in den Schuldienst"));
        assertEquals(LehrerZugangsgrund.SONSTIG.daten.text, ("Sonstige Zugänge"));
    }

	// TODO add additional tests

}
