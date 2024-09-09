package de.svws_nrw.core.types.statkue;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.types.lehrer.LehrerZugangsgrund;


/**
 * Diese Klasse enthält die Testroutinen für den Core-Type LehrerZugangsgrund.
 */
@DisplayName("Teste den Core-Type LehrerZugangsgrund")
class TestCoreTypeLehrerZugangsgrund {

	/**
	 * Führt grundlegende Tests zu dem Core-Type LehrerZugangsgrund aus.
	 */
	@Test
	@DisplayName("Teste den Typ LehrerZugangsgrund")
	void testLehrerZugangsgrund() {
		assertEquals(LehrerZugangsgrund.NEU, LehrerZugangsgrund.data().getWertByKuerzel("NEU"));
		assertEquals(LehrerZugangsgrund.AndBuLand, LehrerZugangsgrund.data().getWertByKuerzel("AndBuLand"));
		assertEquals(LehrerZugangsgrund.WECHSEL, LehrerZugangsgrund.data().getWertByKuerzel("WECHSEL"));
		assertEquals(LehrerZugangsgrund.WIEDER, LehrerZugangsgrund.data().getWertByKuerzel("WIEDER"));
		assertEquals(LehrerZugangsgrund.SONSTIG, LehrerZugangsgrund.data().getWertByKuerzel("SONSTIG"));

		assertEquals(LehrerZugangsgrund.NEU.daten(2024).schluessel, ("1"));
		assertEquals(LehrerZugangsgrund.AndBuLand.daten(2024).schluessel, ("2"));
		assertEquals(LehrerZugangsgrund.WECHSEL.daten(2024).schluessel, ("3"));
		assertEquals(LehrerZugangsgrund.WIEDER.daten(2024).schluessel, ("4"));
		assertEquals(LehrerZugangsgrund.SONSTIG.daten(2024).schluessel, ("5"));

		assertEquals(LehrerZugangsgrund.NEU.daten(2024).text,
				("Neueintritt in den Schuldienst mit abgelegter 2. Staatsprüfung oder anderweitig erfüllter Eingangsvoraussetzung"));
		assertEquals(LehrerZugangsgrund.AndBuLand.daten(2024).text, ("Übertritt aus dem Schuldienst eines anderen Bundeslandes"));
		assertEquals(LehrerZugangsgrund.WECHSEL.daten(2024).text, ("Wechsel innerhalb des Landes von einer anderen Schule an die berichtende Schule"));
		assertEquals(LehrerZugangsgrund.WIEDER.daten(2024).text, ("Wiedereintritt in den Schuldienst"));
		assertEquals(LehrerZugangsgrund.SONSTIG.daten(2024).text, ("Sonstige Zugänge"));
	}

	// TODO add additional tests

}
