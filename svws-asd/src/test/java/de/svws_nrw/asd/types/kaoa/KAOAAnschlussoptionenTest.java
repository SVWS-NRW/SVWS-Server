package de.svws_nrw.asd.types.kaoa;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link KAOAAnschlussoptionen}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type KAOAAnschlussoptionen")
class KAOAAnschlussoptionenTest {

	/**
	 * Initialisiert die Core-Types, damit die Tests ausgeführt werden können.
	 * Beim Laden der Core-Type-Daten werden die JSON-Dateien auf Plausibilität
	 * geprüft.
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}


	// TODO Schreiben von Tests

	/**
	 * Führt einfache Tests zu dem Core-Type KAOAAnschlussoptionen aus.
	 */
	@Test
	@DisplayName("Prüfe die Anzahl der Anlagen am KAOAAnschlussoptionen.")
	void testKAOAAnschlussoptionen() {
		assertEquals(37, KAOAAnschlussoptionen.data().getWerte().size());
	}

	/**
	 * Führt einfache Tests zu dem Core-Type KAOAAnschlussoptionen aus.
	 */
	@Test
	@DisplayName("Prüfe ob der merkmal anzeigeZusatzmerkmal enthalten ist")
	void testKAOAAnschlussoptionenAnzeigeZusatzmerkmal() {
		assertEquals("SBO_10_7_1", KAOAAnschlussoptionen.data().getHistorieByWert(KAOAAnschlussoptionen.BBA).getFirst().anzeigeZusatzmerkmal.getFirst());
	}


	/**
	 * Führt einfache Tests zu dem Core-Type KAOAAnschlussoptionen aus.
	 */
	@Test
	@DisplayName("Prüfe ob der richtige stufen enthalten ist")
	void testKAOAAnschlussoptionenStufen() {
		assertEquals("SEKUNDARSTUFE_I", KAOAAnschlussoptionen.data().getHistorieByWert(KAOAAnschlussoptionen.BBA).getFirst().stufen.getFirst());
	}


}
