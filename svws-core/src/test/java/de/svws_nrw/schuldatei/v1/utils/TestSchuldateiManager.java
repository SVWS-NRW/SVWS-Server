package de.svws_nrw.schuldatei.v1.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.schuldatei.v1.SchuldateiReader;

/**
 * Tests für den {@link SchuldateiManager}.
 */
@DisplayName("Teste die Funktionen des Schuldatei-Managers.")
class TestSchuldateiManager {

	/** Der Manager für die Schuldatei */
	static final SchuldateiManager manager = SchuldateiReader.getManagerLokal();


	@Test
	@DisplayName("Prüfe exemplarisch den Zugriff auf eine Organisationsheit")
	void testSchuleXY() {
		final SchuldateiOrganisationseinheitManager msb = manager.getOrganisationsheinheitManager(1016);
		assertEquals("NRW", msb.getBundeslandkennung());
		assertEquals("3", msb.getArt());
		assertEquals("Düsseldorf, MSB", msb.getKurzbezeichnung(2024));
	}


}
