package de.svws_nrw.schuldatei.v1.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.schuldatei.v1.SchuldateiReader;
import de.svws_nrw.schuldatei.v1.data.SchuldateiOrganisationseinheit;

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
		final SchuldateiOrganisationseinheit msb = manager.getOrganisationsheinheitBySchulnummer(1016);
		assertEquals("NRW", msb.bundeslandkennung);
		assertEquals("3", msb.oeart);
	}


}
