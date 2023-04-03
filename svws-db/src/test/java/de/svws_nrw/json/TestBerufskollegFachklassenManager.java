package de.svws_nrw.json;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.data.schule.BerufskollegFachklassenKatalogDaten;
import de.svws_nrw.core.data.schule.BerufskollegFachklassenKatalogEintrag;
import de.svws_nrw.core.data.schule.BerufskollegFachklassenKatalogIndex;
import de.svws_nrw.core.utils.schule.BerufskollegFachklassenManager;

/**
 * Diese Klasse testet die einzelnen Kataloge, welche im DB-Projekt als
 * JSON-Dateien hinterlegt sind. Hierbei werden die Daten auch vereinzelt
 * auf Korrektheit geprüft.
 */
public class TestBerufskollegFachklassenManager {

	// TODO Tests für BerufskollegFachklassenManager manager = JsonDaten.fachklassenManager;
	/**
	 * Prüft die Einträge der Kataloge für die Fachklassen
	 * bei den berufsbildenden Schulformen.
	 */
	@Test
	@DisplayName("Prüfe Fachklassendaten Jsons auf Korrektheit.")
	void testBerufskollegFachklassen() {
		System.out.println("Erstelle FachklassenManager...");
		final BerufskollegFachklassenManager manager = JsonDaten.fachklassenManager;
		System.out.println("  Die Daten liegen in Version " + manager.getVersion() + " vor.");
		System.out.println("Prüfe die Einträge des Fachklassen-Katalogs für berufsbildende Schulformen");
		for (final BerufskollegFachklassenKatalogIndex index : manager.getKatalog().indizes) {
			for (final BerufskollegFachklassenKatalogEintrag eintrag : index.fachklassen) {
			if (eintrag.schluessel == null)
					fail("Katalog-Eintrag " + eintrag.schluessel + " ist fehlerhaft, da bei Fachklassen der erste Teil des Schlüssels nicht leer sein darf .");
			if (eintrag.schluessel2 == null)
				fail("Katalog-Eintrag " + eintrag.schluessel2 + " ist fehlerhaft, da bei Fachklassen der zweite Teil des Schlüssels nicht leer sein darf .");
			for (final BerufskollegFachklassenKatalogDaten daten : eintrag.historie) {
			    if (daten.bezeichnung == null)
	                fail("Katalog-Eintrag " + daten.bezeichnung + " ist fehlerhaft, da bei Fachklassen die Bezeichnung nicht leer sein darf .");
			    if (daten.bezeichnungM == null)
	                fail("Katalog-Eintrag " + daten.bezeichnungM + " ist fehlerhaft, da bei Fachklassen die Bezeichnung (männlich) nicht leer sein darf .");
                if (daten.bezeichnungW == null)
	                fail("Katalog-Eintrag " + daten.bezeichnungW + " ist fehlerhaft, da bei Fachklassen die Bezeichnung (weiblich) nicht leer sein darf .");
			    }
			}
		System.out.println("Prüfung des Katalogs der Fachklassen erfolgreich abgeschlossen.");
		}
	}
}

