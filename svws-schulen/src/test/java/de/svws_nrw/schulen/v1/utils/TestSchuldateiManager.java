package de.svws_nrw.schulen.v1.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.schulen.v1.SchuldateiReader;

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
		assertEquals("0A", msb.getSchulform(2024));
		assertEquals("0A", msb.getSchulformASD(2024));
		assertEquals("", msb.getSchulart(2024));
		final SchuldateiOrganisationseinheitManager schule100015 = manager.getOrganisationsheinheitManager(100015);
		final SchuldateiOrganisationseinheitAdressManager schule10015adr1 = schule100015.getHauptstandort(2019);
		final SchuldateiOrganisationseinheitAdressManager schule10015adr2 = schule100015.getHauptstandort(2020);
		final SchuldateiOrganisationseinheitAdressManager schule10015adr3 = schule100015.getHauptstandort(2022);
		final SchuldateiOrganisationseinheitAdressManager schule10015adr4 = schule100015.getHauptstandort(2023);
		assertEquals(1855, schule10015adr1.getID());
		assertEquals(1857, schule10015adr2.getID());
		assertEquals(1857, schule10015adr3.getID());
		assertEquals(1856, schule10015adr4.getID());
	}

}
