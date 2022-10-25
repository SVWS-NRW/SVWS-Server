package de.nrw.schule.svws.core.types.statkue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.nrw.schule.svws.core.types.lehrer.LehrerMinderleistungArt;


/**
 * Diese Klasse enthält die Testroutinen für den Core-Type LehrerMinderleistungArt.
 */
@DisplayName("Teste den Core-Type LehrerMinderleistungArt")
public class TestCoreTypeLehrerMinderleistungArt {
	
    /**
     * Initialisiert den Test
     */
    @BeforeAll
    static void setup() {
    }

    
    @Test
    @DisplayName("Teste den Typ LehrerMinderleistungArt")
    void testLehrerMinderleistungArt() {
    	assertEquals(LehrerMinderleistungArt.ID_200, LehrerMinderleistungArt.getByKuerzel("200"));
    	assertEquals(LehrerMinderleistungArt.ID_210, LehrerMinderleistungArt.getByKuerzel("210"));
    	assertEquals(LehrerMinderleistungArt.ID_220, LehrerMinderleistungArt.getByKuerzel("220"));
    	assertEquals(LehrerMinderleistungArt.ID_230, LehrerMinderleistungArt.getByKuerzel("230"));
    	assertEquals(LehrerMinderleistungArt.ID_240, LehrerMinderleistungArt.getByKuerzel("240"));
    	assertEquals(LehrerMinderleistungArt.ID_250, LehrerMinderleistungArt.getByKuerzel("250"));
    	assertEquals(LehrerMinderleistungArt.ID_255, LehrerMinderleistungArt.getByKuerzel("255"));
    	assertEquals(LehrerMinderleistungArt.ID_260, LehrerMinderleistungArt.getByKuerzel("260"));
    	assertEquals(LehrerMinderleistungArt.ID_270, LehrerMinderleistungArt.getByKuerzel("270"));
    	assertEquals(LehrerMinderleistungArt.ID_275, LehrerMinderleistungArt.getByKuerzel("275"));
    	assertEquals(LehrerMinderleistungArt.ID_280, LehrerMinderleistungArt.getByKuerzel("280"));
    	assertEquals(LehrerMinderleistungArt.ID_290, LehrerMinderleistungArt.getByKuerzel("290"));
    	assertEquals(LehrerMinderleistungArt.ID_300, LehrerMinderleistungArt.getByKuerzel("300"));
    	assertEquals(LehrerMinderleistungArt.ID_350, LehrerMinderleistungArt.getByKuerzel("350"));
    	assertEquals(LehrerMinderleistungArt.ID_360, LehrerMinderleistungArt.getByKuerzel("360"));
    	assertEquals(LehrerMinderleistungArt.ID_365, LehrerMinderleistungArt.getByKuerzel("365"));
    	assertEquals(LehrerMinderleistungArt.ID_370, LehrerMinderleistungArt.getByKuerzel("370"));
    	assertEquals(LehrerMinderleistungArt.ID_380, LehrerMinderleistungArt.getByKuerzel("380"));
    	
    	assertEquals(LehrerMinderleistungArt.ID_200.daten.kuerzel, ("200"));
    	assertEquals(LehrerMinderleistungArt.ID_210.daten.kuerzel, ("210"));
    	assertEquals(LehrerMinderleistungArt.ID_220.daten.kuerzel, ("220"));
    	assertEquals(LehrerMinderleistungArt.ID_230.daten.kuerzel, ("230"));
    	assertEquals(LehrerMinderleistungArt.ID_240.daten.kuerzel, ("240"));
    	assertEquals(LehrerMinderleistungArt.ID_250.daten.kuerzel, ("250"));
    	assertEquals(LehrerMinderleistungArt.ID_255.daten.kuerzel, ("255"));
    	assertEquals(LehrerMinderleistungArt.ID_260.daten.kuerzel, ("260"));
    	assertEquals(LehrerMinderleistungArt.ID_270.daten.kuerzel, ("270"));
    	assertEquals(LehrerMinderleistungArt.ID_275.daten.kuerzel, ("275"));
    	assertEquals(LehrerMinderleistungArt.ID_280.daten.kuerzel, ("280"));
    	assertEquals(LehrerMinderleistungArt.ID_290.daten.kuerzel, ("290"));
    	assertEquals(LehrerMinderleistungArt.ID_300.daten.kuerzel, ("300"));
    	assertEquals(LehrerMinderleistungArt.ID_350.daten.kuerzel, ("350"));
    	assertEquals(LehrerMinderleistungArt.ID_360.daten.kuerzel, ("360"));
    	assertEquals(LehrerMinderleistungArt.ID_365.daten.kuerzel, ("365"));
    	assertEquals(LehrerMinderleistungArt.ID_370.daten.kuerzel, ("370"));
    	assertEquals(LehrerMinderleistungArt.ID_380.daten.kuerzel, ("380"));
    	
    	assertEquals(LehrerMinderleistungArt.ID_200.daten.text, ("Pflichtstundenermäßigung aus Altersgründen"));
    	assertEquals(LehrerMinderleistungArt.ID_210.daten.text, ("Pflichtstundenermäßigung wegen Schwerbehinderung (Regelanrechnung)"));
    	assertEquals(LehrerMinderleistungArt.ID_220.daten.text, ("Pflichtstundenermäßigung wegen Schwerbehinderung (Erhöhung auf Antrag)"));
    	assertEquals(LehrerMinderleistungArt.ID_230.daten.text, ("Beurlaubung, Rückkehr im Laufe des Schuljahres"));
    	assertEquals(LehrerMinderleistungArt.ID_240.daten.text, ("Langfristige Erkrankung"));
    	assertEquals(LehrerMinderleistungArt.ID_250.daten.text, ("Abwesend wegen Beschäftigungsverbot gem. § 3 MuSchG"));
    	assertEquals(LehrerMinderleistungArt.ID_255.daten.text, ("Abwesend wegen Teilbeschäftigungsverbot gem. § 3 MuSchG"));
    	assertEquals(LehrerMinderleistungArt.ID_260.daten.text, ("Wiedereingliederungsmaßnahme"));
    	assertEquals(LehrerMinderleistungArt.ID_270.daten.text, ("Rückgabe vorgeleisteter Stunden wegen Nichtinanspruchnahme von Altersteilzeit"));
    	assertEquals(LehrerMinderleistungArt.ID_275.daten.text, ("Rückgabe der Vorgriffsstunden"));
    	assertEquals(LehrerMinderleistungArt.ID_280.daten.text, ("Seiteneinsteigerentlastung"));
    	assertEquals(LehrerMinderleistungArt.ID_290.daten.text, ("Ermäßigungs-/Freistellungsphase \"Teilzeitbeschäftigung im Blockmodell\" (§ 65 LBG) (vormals Sabbatjahr)"));
    	assertEquals(LehrerMinderleistungArt.ID_300.daten.text, ("Sonstige Ermäßigungen aus besonderen persönlichen Gründen"));
    	assertEquals(LehrerMinderleistungArt.ID_350.daten.text, ("Abrundung der Pflichtstundenzahl wegen Aufrundung im vorhergehenden Schuljahr"));
    	assertEquals(LehrerMinderleistungArt.ID_360.daten.text, ("Unterschreitung der Pflichtstundenzahl aus organisatorischen Gründen (z. B. Epochenunterricht)"));
    	assertEquals(LehrerMinderleistungArt.ID_365.daten.text, ("Unterschreitung der Pflichtstundenzahl wegen COVID-19"));
    	assertEquals(LehrerMinderleistungArt.ID_370.daten.text, ("Unterschreitung der Pflichtstundenzahl wegen Pflichstunden-Bandbreite"));
    	assertEquals(LehrerMinderleistungArt.ID_380.daten.text, ("Fortbildung: Nachträglicher Erwerb des sonderpädagogischen Lehramtes"));
    	
    }

	// TODO add additional tests
    
}
