package de.svws_nrw.json;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.data.schule.AbgangsartKatalogEintrag;
import de.svws_nrw.core.types.schule.SchulabschlussBerufsbildend;
import de.svws_nrw.core.utils.schule.AbgangsartenManager;

/**
 * Diese Klasse testet die einzelnen Kataloge, welche im DB-Projekt als
 * JSON-Dateien hinterlegt sind. Hierbei werden die Daten auch vereinzelt
 * auf Korrektheit geprüft.
 */
class TestAbgangsartenManager {

	/**
	 * Prüft die Einträge der beiden Kataloge für die Abgangsarten
	 * bei allgemeinbildenden bzw. berufsbildenden Schulformen.
	 */
	@Test
	@DisplayName("Prüfe AbgangsartenAllgemeinbildend.json und AbgangsartenBerufsbildend.json")
	void testAbgangsarten() {
		System.out.println("Erstelle AbgangsartenManager...");
		final AbgangsartenManager manager = JsonDaten.abgangsartenManager;
		System.out.println("  Die Daten liegen in Version " + manager.getVersion() + " vor.");
		System.out.println("Prüfe die Einträge des Abgangsarten-Katalogs für allgemeinbildende Schulformen");
		for (final AbgangsartKatalogEintrag eintrag : manager.getKatalogAllgemeinbildend().eintraege) {
			if (eintrag.kuerzel.length() != 1)
				fail("Katalog-Eintrag " + eintrag.kuerzel + " ist fehlerhaft, da bei allgemeinbildenden Katalog-Einträgen das Kürzel einstellig sein muss .");
			if (AbgangsartenManager.getAbschlussAllgemeinbildend(eintrag) == null)
				fail("Katalog-Eintrag " + eintrag.kuerzel + " ist fehlerhaft und enthält keinen gültigen Eintrag für einen allgmeinbildenden Abschluss.");
			if (AbgangsartenManager.getAbschlussBerufsbildend(eintrag) != null)
				fail("Katalog-Eintrag " + eintrag.kuerzel + " ist fehlerhaft, da ein Katalog-Eintrag für allgemeinbildende Schulformen keinen berufsbildenden Abschluss beinhalten kann.");
		}
		System.out.println("Prüfe die Einträge des Abgangsarten-Katalogs für berufsbildende Schulformen");
		for (final AbgangsartKatalogEintrag eintrag : manager.getKatalogBerufsbildend().eintraege) {
			if (eintrag.kuerzel.length() != 2)
				fail("Katalog-Eintrag " + eintrag.kuerzel + " ist fehlerhaft, da bei berufsbildenden Katalog-Einträgen das Kürzel zweistellig sein muss .");
			if (AbgangsartenManager.getAbschlussAllgemeinbildend(eintrag) == null)
				fail("Katalog-Eintrag " + eintrag.kuerzel + " ist fehlerhaft und enthält keinen gültigen Eintrag für einen allgmeinbildenden Abschluss.");
			final SchulabschlussBerufsbildend abschlussBeruf = AbgangsartenManager.getAbschlussBerufsbildend(eintrag);
			if (abschlussBeruf == null)
				fail("Katalog-Eintrag " + eintrag.kuerzel + " ist fehlerhaft und enthält keinen gültigen Eintrag für einen berufsbildenden Abschluss.");
		}
		System.out.println("Prüfung des Katalogs der Abgangsarten erfolgreich abgeschlossen.");
	}

}
