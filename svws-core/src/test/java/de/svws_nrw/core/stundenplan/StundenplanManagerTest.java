package de.svws_nrw.core.stundenplan;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.svws_nrw.core.data.stundenplan.StundenplanFach;
import de.svws_nrw.core.data.stundenplan.StundenplanJahrgang;
import de.svws_nrw.core.utils.stundenplan.StundenplanManager;

/**
 * Diese Klasse testet den {@link StundenplanManager}.
 */
@DisplayName("Diese Klasse testet den {@link StundenplanManager}.")
@TestMethodOrder(MethodOrderer.MethodName.class)
class StundenplanManagerTest {

	/**
	 * Diese Klasse testet den {@link StundenplanManager} mit randomisierten Daten.
	 */
	@DisplayName("testStundenplanManager")
	@Test
	void testDatenstz_2023_08_31() {
		final String location = "de/svws_nrw/core/utils/stundenplan/StupasSchulmanagerExport.txt";
		final StupasSchulmanagerFormatReader reader = new StupasSchulmanagerFormatReader();
		final StundenplanManager m = reader.toManager(location);

		testFach(m);
		testJahrgang(m);
		// jahrgangRemoveOhneUpdateById, jahrgangRemoveOhneUpdateById, jahrgangRemoveAll






		// Datenkonsistenz überprüfen
		assertEquals(43, m.raumGetMengeAsList().size());
		assertEquals(60, m.lehrerGetMengeAsList().size());
		assertEquals(32, m.klasseGetMengeAsList().size());
		assertEquals(35, m.schieneGetMengeAsList().size());
	}

	private static void testJahrgang(final StundenplanManager m) {
		assertEquals(8, m.jahrgangGetMengeAsList().size());

		final StundenplanJahrgang jahrgangAlt = m.jahrgangGetByIdOrException(0);
		assertEquals("05", jahrgangAlt.kuerzel);

		final StundenplanJahrgang jahrgangNeu = new StundenplanJahrgang();
		jahrgangNeu.id = jahrgangAlt.id;
		jahrgangNeu.bezeichnung = jahrgangAlt.bezeichnung;
		jahrgangNeu.kuerzel = "Stufe 05";
		m.jahrgangPatchAttributes(jahrgangNeu);

		final StundenplanJahrgang jahrgangCheck = m.jahrgangGetByIdOrException(0);
		assertEquals("Stufe 05", jahrgangCheck.kuerzel);

		m.jahrgangPatchAttributes(jahrgangAlt);
		assertEquals("05", jahrgangAlt.kuerzel);
	}

	private static void testFach(final StundenplanManager m) {
		final StundenplanFach fach = m.fachGetByIdOrException(2);
		assertEquals("BI", fach.kuerzel);
		assertEquals(42, m.fachGetMengeAsList().size());
	}

}
