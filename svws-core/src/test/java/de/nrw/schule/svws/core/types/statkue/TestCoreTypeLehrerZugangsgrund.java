package de.nrw.schule.svws.core.types.statkue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


/**
 * Diese Klasse enthält die Testroutinen für den Core-Type LehrerZugangsgrund.
 */
@DisplayName("Teste den Core-Type LehrerZugangsgrund")
public class TestCoreTypeLehrerZugangsgrund {
	
    /**
     * Initialisiert den Test
     */
    @BeforeAll
    static void setup() {
    }

    
    @Test
    @DisplayName("Teste den Typ LehrerZugangsgrund")
    void testLehrerZugangsgrund() {
    	assertEquals(LehrerZugangsgrund.NEU, LehrerZugangsgrund.getByKuerzel("NEU"));
    	assertEquals(LehrerZugangsgrund.AndBuLand, LehrerZugangsgrund.getByKuerzel("AndBuLand"));
    	assertEquals(LehrerZugangsgrund.WECHSEL, LehrerZugangsgrund.getByKuerzel("WECHSEL"));
    	assertEquals(LehrerZugangsgrund.WIEDER, LehrerZugangsgrund.getByKuerzel("WIEDER"));
    	assertEquals(LehrerZugangsgrund.SONSTIG, LehrerZugangsgrund.getByKuerzel("SONSTIG"));
    }

	// TODO add additional tests
    
}
