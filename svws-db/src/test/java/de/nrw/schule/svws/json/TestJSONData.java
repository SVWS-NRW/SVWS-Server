package de.nrw.schule.svws.json;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.nrw.schule.svws.core.data.schule.AbgangsartKatalog;
import de.nrw.schule.svws.core.data.schule.AbgangsartKatalogEintrag;
import de.nrw.schule.svws.core.types.schule.SchulabschlussBerufsbildend;
import de.nrw.schule.svws.core.utils.schule.AbgangsartenManager;

/**
 * Diese Klasse testet die einzelnen Katalog, welche im DB-Projekt als
 * JSON-Dateien hinterlegt sind. Hierbei werden die Daten auch vereinzelt
 * auf Korrektheit geprüft.
 */
public class TestJSONData {
	
	/**
	 * Prüft die Einträge der beiden Kataloge für die Abgangsarten
	 * bei allgemeinbildenden bzw. berifsbildenden Schulformen.
	 */
	@Test
	@DisplayName("Prüfe AbgangsartenAllgemeinbildend.json und AbgangsartenBerufsbildend.json")
	void testAbgangsarten() {
		System.out.println("Lade daten/json/AbgangsartenAllgemeinbildend.json");
		AbgangsartKatalog katalogAllg = JsonReader.fromResource("daten/json/AbgangsartenAllgemeinbildend.json", AbgangsartKatalog.class);
		System.out.println("Lade daten/json/AbgangsartenBerufsbildend.json");
		AbgangsartKatalog katalogBeruf = JsonReader.fromResource("daten/json/AbgangsartenBerufsbildend.json", AbgangsartKatalog.class);
		System.out.println("Erstelle AbgangsartenManager...");
		AbgangsartenManager manager = new AbgangsartenManager(katalogAllg, katalogBeruf);
		System.out.println("  Die Daten liegen in Version " + manager.getVersion() + " vor.");
		System.out.println("Prüfe die Einträge des Abgangsarten-Katalogs für allgemeinbildende Schulformen");
		for (AbgangsartKatalogEintrag eintrag : katalogAllg.eintraege) {
			if (eintrag.kuerzel.length() != 1)
				fail("Katalog-Eintrag " + eintrag.kuerzel + " ist fehlerhaft, da bei allgemeinbildenden Katalog-Einträgen das Kürzel einstellig sein muss .");
			if (AbgangsartenManager.getAbschlussAllgemeinbildend(eintrag) == null)
				fail("Katalog-Eintrag " + eintrag.kuerzel + " ist fehlerhaft und enthält keinen gültigen Eintrag für einen allgmeinbildenden Abschluss.");
			if (AbgangsartenManager.getAbschlussBerufsbildend(eintrag) != null)
				fail("Katalog-Eintrag " + eintrag.kuerzel + " ist fehlerhaft, da ein Katalog-Eintrag für allgemeinbildende Schulformen keinen berufsbildenden Abschluss beinhalten kann.");
		}
		System.out.println("Prüfe die Einträge des Abgangsarten-Katalogs für berufsbildende Schulformen");
		for (AbgangsartKatalogEintrag eintrag : katalogBeruf.eintraege) {
			if (eintrag.kuerzel.length() != 2)
				fail("Katalog-Eintrag " + eintrag.kuerzel + " ist fehlerhaft, da bei berufsbildenden Katalog-Einträgen das Kürzel zweistellig sein muss .");
			if (AbgangsartenManager.getAbschlussAllgemeinbildend(eintrag) == null)
				fail("Katalog-Eintrag " + eintrag.kuerzel + " ist fehlerhaft und enthält keinen gültigen Eintrag für einen allgmeinbildenden Abschluss.");
			SchulabschlussBerufsbildend abschlussBeruf = AbgangsartenManager.getAbschlussBerufsbildend(eintrag);
			if (abschlussBeruf == null)
				fail("Katalog-Eintrag " + eintrag.kuerzel + " ist fehlerhaft und enthält keinen gültigen Eintrag für einen berufsbildenden Abschluss.");
		}
		System.out.println("Prüfung des Katalogs der Abgangsarten erfolgreich abgeschlossen.");
	}

}
