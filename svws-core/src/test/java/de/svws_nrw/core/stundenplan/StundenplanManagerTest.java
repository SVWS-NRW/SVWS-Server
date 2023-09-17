package de.svws_nrw.core.stundenplan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.svws_nrw.core.data.stundenplan.StundenplanFach;
import de.svws_nrw.core.data.stundenplan.StundenplanJahrgang;
import de.svws_nrw.core.data.stundenplan.StundenplanKalenderwochenzuordnung;
import de.svws_nrw.core.data.stundenplan.StundenplanKlasse;
import de.svws_nrw.core.data.stundenplan.StundenplanLehrer;
import de.svws_nrw.core.utils.stundenplan.StundenplanManager;

/**
 * Diese Klasse testet den {@link StundenplanManager}.
 */
@DisplayName("Diese Klasse testet den {@link StundenplanManager}.")
@TestMethodOrder(MethodOrderer.MethodName.class)
class StundenplanManagerTest {

	// TODO Zwei Kalenderwochenzuordnungen via Definition drehen.
	// TODO Alle "private" Mengen des Managers testen.

	/**
	 * Diese Klasse testet den {@link StundenplanManager} mit randomisierten Daten.
	 */
	@DisplayName("testStundenplanManager")
	@Test
	void testDatensatz_2023_08_31() {
		final String location = "de/svws_nrw/core/utils/stundenplan/StupasSchulmanagerExport.txt";
		final StupasSchulmanagerFormatReader reader = new StupasSchulmanagerFormatReader();
		final StundenplanManager m = reader.toManager(location, "2022-03-15", "2022-09-25"); // 11 KW bis 38 KW

		test_fach_getter(m);
		test_jahrgang_getter(m);
		test_kwz_getter(m);
		test_klasse_getter(m);
		test_klassenunterricht_getter(m);

		// Datenkonsistenz überprüfen
		assertEquals(43, m.raumGetMengeAsList().size());
		assertEquals(60, m.lehrerGetMengeAsList().size());
		assertEquals(35, m.schieneGetMengeAsList().size());
	}

	private static void test_klassenunterricht_getter(final StundenplanManager m) {
		assertEquals(234, m.klassenunterrichtGetMengeAsList().size());

		 final StundenplanKlasse kl10d =  m.klasseGetByIdOrException(21);
		 assertEquals("10d", kl10d.kuerzel);

		 final StundenplanLehrer le25 =  m.lehrerGetByIdOrException(25);
		 assertEquals(5, m.klassenunterrichtGetMengeByLehrerIdAsList(le25.id).size());


//		 m.klassenunterrichtGetMengeByLehrerIdAsList(0)


		// ...
	}

	private static void test_klasse_getter(final StundenplanManager m) {
		assertEquals(32, m.klasseGetMengeAsList().size());

		final StundenplanKlasse klasseAlt = m.klasseGetByIdOrException(7);
		assertEquals(7, klasseAlt.id);
		assertEquals("06d", klasseAlt.kuerzel);
		assertEquals(1, klasseAlt.jahrgaenge.size());

		final StundenplanJahrgang jg06 = m.jahrgangGetByIdOrException(klasseAlt.jahrgaenge.get(0));
		assertEquals("06", jg06.kuerzel);

		final StundenplanKlasse klasseNeu = new StundenplanKlasse();
		klasseNeu.id = klasseAlt.id;
		klasseNeu.kuerzel = "06x";
		klasseNeu.bezeichnung = klasseAlt.bezeichnung;
		klasseNeu.jahrgaenge = new ArrayList<>(klasseAlt.jahrgaenge);
		klasseNeu.schueler = new ArrayList<>(klasseAlt.schueler);
		m.klassePatchAttributes(klasseNeu);
		assertEquals("06x", m.klasseGetByIdOrException(7).kuerzel);
		m.klassePatchAttributes(klasseAlt);
		assertEquals("06d", m.klasseGetByIdOrException(7).kuerzel);
	}

	private static void test_kwz_getter(final StundenplanManager m) {
		assertEquals(28, m.kalenderwochenzuordnungGetMengeAsList().size());

		final StundenplanKalenderwochenzuordnung kw11 = m.kalenderwochenzuordnungGetByJahrAndKWOrException(2022, 11);
		final StundenplanKalenderwochenzuordnung kw11b = m.kalenderwochenzuordnungGetByDatum("2022-03-15");
		final StundenplanKalenderwochenzuordnung kw12 = m.kalenderwochenzuordnungGetByJahrAndKWOrException(2022, 12);
		final StundenplanKalenderwochenzuordnung kw12b = m.kalenderwochenzuordnungGetNextOrNull(kw11);
		final StundenplanKalenderwochenzuordnung kw10 = m.kalenderwochenzuordnungGetPrevOrNull(kw11);
		assertEquals(1, kw11.wochentyp);
		assertEquals(kw11b, kw11);
		assertEquals(kw12b, kw12);
		assertEquals(null, kw10);

		final StundenplanKalenderwochenzuordnung kw38 = m.kalenderwochenzuordnungGetByJahrAndKWOrException(2022, 38);
		final StundenplanKalenderwochenzuordnung kw38b = m.kalenderwochenzuordnungGetByDatum("2022-09-25");
		final StundenplanKalenderwochenzuordnung kw39 = m.kalenderwochenzuordnungGetNextOrNull(kw38);
		final StundenplanKalenderwochenzuordnung kw37 = m.kalenderwochenzuordnungGetByJahrAndKWOrException(2022, 37);
		final StundenplanKalenderwochenzuordnung kw37b = m.kalenderwochenzuordnungGetPrevOrNull(kw38);
		assertEquals(2, kw38.wochentyp);
		assertEquals(kw38, kw38b);
		assertEquals(null, kw39);
		assertEquals(kw37, kw37b);

		try {
			m.kalenderwochenzuordnungGetByJahrAndKWOrException(2022, 10);
			fail("KWZ 2022.10 sollte es nicht geben!");
		} catch (@SuppressWarnings("unused") final Exception ex) {
			// okay
		}

		try {
			m.kalenderwochenzuordnungGetByJahrAndKWOrException(2022, 39);
			fail("KWZ 2022.39 sollte es nicht geben!");
		} catch (@SuppressWarnings("unused") final Exception ex) {
			// okay
		}

		final String kw37wochenString = m.kalenderwochenzuordnungGetWocheAsString(kw37);
		assertEquals("KW 37 (12.09.2022–18.09.2022)", kw37wochenString);

		assertEquals(2, m.kalenderwochenzuordnungGetWochentypOrDefault(2022, 20));
		assertEquals(1, m.kalenderwochenzuordnungGetWochentypOrDefault(2022, 21));

		assertEquals(false, m.kalenderwochenzuordnungGetWochentypUsesMapping(2022, 20));
		assertEquals(false, m.kalenderwochenzuordnungGetWochentypUsesMapping(2022, 21));
	}

	private static void test_jahrgang_getter(final StundenplanManager m) {
		assertEquals(8, m.jahrgangGetMengeAsList().size());

		final StundenplanJahrgang jahrgangAlt = m.jahrgangGetByIdOrException(0);
		assertEquals(0, jahrgangAlt.id);
		assertEquals("05", jahrgangAlt.kuerzel);

		final StundenplanJahrgang jahrgangNeu = new StundenplanJahrgang();
		jahrgangNeu.id = jahrgangAlt.id;
		jahrgangNeu.bezeichnung = jahrgangAlt.bezeichnung;
		jahrgangNeu.kuerzel = "Stufe 05";
		m.jahrgangPatchAttributes(jahrgangNeu);

		final StundenplanJahrgang jahrgangCheck = m.jahrgangGetByIdOrException(0);
		assertEquals(0, jahrgangCheck.id);
		assertEquals("Stufe 05", jahrgangCheck.kuerzel);

		m.jahrgangPatchAttributes(jahrgangAlt);
		assertEquals("05", jahrgangAlt.kuerzel);
	}

	private static void test_fach_getter(final StundenplanManager m) {
		final StundenplanFach fach = m.fachGetByIdOrException(2);
		assertEquals("BI", fach.kuerzel);
		assertEquals(42, m.fachGetMengeAsList().size());
	}

}
