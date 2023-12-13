package de.svws_nrw.core.utils.stundenplan;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.svws_nrw.core.data.stundenplan.StundenplanAufsichtsbereich;
import de.svws_nrw.core.data.stundenplan.StundenplanFach;
import de.svws_nrw.core.data.stundenplan.StundenplanJahrgang;
import de.svws_nrw.core.data.stundenplan.StundenplanKlasse;
import de.svws_nrw.core.data.stundenplan.StundenplanKomplett;
import de.svws_nrw.core.data.stundenplan.StundenplanLehrer;
import de.svws_nrw.core.data.stundenplan.StundenplanRaum;
import de.svws_nrw.core.data.stundenplan.StundenplanSchueler;
import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import jakarta.validation.constraints.NotNull;

// TODO check implementation aller patch --> ohne remove/add
// TODO check implementation aller remove --> Kaskaden?

/**
 * Diese Klasse testet den {@link StundenplanManager}.
 */
@DisplayName("Diese Klasse testet den {@link StundenplanManager}.")
@TestMethodOrder(MethodOrderer.MethodName.class)
class TestStundenplanManagerRandomized {

	private static final long _SEED = 1L;

	@SuppressWarnings("unused")
	private static String fachListToString(final @NotNull List<@NotNull StundenplanFach> fachList) {
		String s = "";

		for (final @NotNull StundenplanFach fach : fachList)
			s = s + ", " + fach.id;

		return s;
	}

	@SuppressWarnings("unused")
	private static String longListToString(final @NotNull List<@NotNull Long> idList) {
		String s = "";

		for (final @NotNull Long id : idList)
			s = s + ", " + id;

		return s;
	}

	@SuppressWarnings("unused")
	private static String lehrerListToString(final @NotNull List<@NotNull StundenplanLehrer> lehrerList) {
		String s = "";

		for (final @NotNull StundenplanLehrer lehrer : lehrerList)
			s = s + ", " + lehrer.id;

		return s;
	}

	/**
	 * Diese Klasse testet den {@link StundenplanManager} mit randomisierten Daten.
	 */
	@DisplayName("testAllRandomized")
	@Test
	void testAllRandomized() {
		for (int runden = 1; runden <= 1024; runden *= 2)
			testeMehrereRunden(runden);

	}

	/**
	 * Diese Klasse testet den {@link StundenplanManager} mit deterministischen Daten.
	 */
	@DisplayName("testAllDeterministic")
	@Test
	void testAllDeterministic() {
		test_gueltig_bis();
	}

	private static void test_gueltig_bis() {
		final @NotNull StundenplanKomplett komplett = new StundenplanKomplett();
		komplett.daten.gueltigAb = "2022-03-15";
		komplett.daten.gueltigBis = "2022-07-15";

		final @NotNull StundenplanManager m1 = new StundenplanManager(komplett);
		assertEquals("2022-07-15", m1.getGueltigBis());

		komplett.daten.gueltigAb = "2022-07-15";
		komplett.daten.gueltigBis = "";
		final @NotNull StundenplanManager m2 = new StundenplanManager(komplett);
		assertEquals("2022-07-31", m2.getGueltigBis());

		komplett.daten.gueltigAb = "2022-09-15";
		komplett.daten.gueltigBis = "";
		final @NotNull StundenplanManager m3 = new StundenplanManager(komplett);
		assertEquals("2023-07-31", m3.getGueltigBis());

		// komplett.daten.gueltigBis = null; // TODO Fall: NULL
	}

	private static void testeMehrereRunden(final int runden) {
		// System.out.println("\nRunden = " + runden);
		final @NotNull Random rnd = new Random(_SEED);

		final @NotNull StundenplanKomplett komplett = new StundenplanKomplett();
		komplett.daten.gueltigAb = "2022-03-15";
		komplett.daten.gueltigBis = "2022-09-25";

		final @NotNull StundenplanManager m1 = new StundenplanManager(komplett);
		final @NotNull StundenplanManagerDummy m2 = new StundenplanManagerDummy();

		for (int i = 0; i < runden; i++)
			testeJedeMethode(rnd, m1, m2);
	}

	private static void testeJedeMethode(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		testFach(rnd, m1, m2);
		testRaum(rnd, m1, m2);
		testSchueler(rnd, m1, m2);
		testJahrgang(rnd, m1, m2);
		testAufsichtsbereich(rnd, m1, m2);
		testZeitraster(rnd, m1, m2);

		testLehrer(rnd, m1, m2);
		testKlasse(rnd, m1, m2);

		// kalenderwochenzuordnung
		// zeitraster
		// pausenzeit
		// aufsichtsbereich
		// schiene
		// klassenunterricht
		// pausenaufsicht
		// kurs
		// unterricht
	}

	private static void testFach(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		testFachAdd(rnd, m1, m2);
		testFachAddAll(rnd, m1, m2);
		testFachGetByIdOrException(rnd, m1, m2);
		testFachGetMengeAsList(m1, m2); // Mengen-Check zuletzt
	}

	private static void testFachAdd(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		final @NotNull StundenplanFach fach = StundenplanManagerDummy.fachCreateRandom(rnd);

		boolean ex1 = false;
		try {
			m1.fachAdd(fach);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex1 = true;
		}

		boolean ex2 = false;
		try {
			m2.fachAdd(fach);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex2 = true;
		}

		assertEquals(true, ex1 == ex2);
	}

	private static void testFachAddAll(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		final @NotNull List<@NotNull StundenplanFach> fachList = StundenplanManagerDummy.fachListCreateRandom(rnd);

		boolean ex1 = false;
		try {
			m1.fachAddAll(fachList);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex1 = true;
		}

		boolean ex2 = false;
		try {
			m2.fachAddAll(fachList);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex2 = true;
		}

		assertEquals(true, ex1 == ex2);
	}

	private static void testFachGetByIdOrException(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		final long idFach = rnd.nextLong(StundenplanManagerDummy.FACH_MAX_ID);

		StundenplanFach fach1 = null;
		try {
			fach1 = m1.fachGetByIdOrException(idFach);
		} catch (@SuppressWarnings("unused") final Exception e) {
			fach1 = null;
		}

		StundenplanFach fach2 = null;
		try {
			fach2 = m2.fachGetByIdOrException(idFach);
		} catch (@SuppressWarnings("unused") final Exception e) {
			fach2 = null;
		}

		if ((fach1 != null) && (fach2 != null))
			assertEquals(true, fach1.id == fach2.id);
		else
			assertEquals(true, (fach1 == null) && (fach2 == null));
	}

	private static void testFachGetMengeAsList(final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		assertEquals(m1.fachGetMengeAsList().size(), m2.fachGetMengeAsList().size());

		final @NotNull Iterator<@NotNull StundenplanFach> i1 = m1.fachGetMengeAsList().iterator();
		final @NotNull Iterator<@NotNull StundenplanFach> i2 = m2.fachGetMengeAsList().iterator();
		assertEquals(i1.hasNext(), i2.hasNext());
		while (i1.hasNext() || i2.hasNext()) {
			assertEquals(i1.hasNext(), i2.hasNext());
			assertEquals(i1.next().id, i2.next().id);
		}
	}

	private static void testRaum(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		testRaumAdd(rnd, m1, m2);
		testRaumAddAll(rnd, m1, m2);
		testRaumGetByIdOrException(rnd, m1, m2);
		testRaumPatchAttributes(rnd, m1, m2);
		testRaumRemoveById(rnd, m1, m2);
		testRaumRemoveAll(rnd, m1, m2);
		testRaumGetMengeAsList(m1, m2); // Mengen-Check zuletzt
	}

	private static void testRaumAdd(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		final @NotNull StundenplanRaum raum = StundenplanManagerDummy.raumCreateRandom(rnd);

		boolean ex1 = false;
		try {
			m1.raumAdd(raum);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex1 = true;
		}

		boolean ex2 = false;
		try {
			m2.raumAdd(raum);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex2 = true;
		}

		assertEquals(true, ex1 == ex2);
	}

	private static void testRaumAddAll(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		final @NotNull List<@NotNull StundenplanRaum> raumList = StundenplanManagerDummy.raumListCreateRandom(rnd);

		boolean ex1 = false;
		try {
			m1.raumAddAll(raumList);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex1 = true;
		}

		boolean ex2 = false;
		try {
			m2.raumAddAll(raumList);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex2 = true;
		}

		assertEquals(true, ex1 == ex2);
	}

	private static void testRaumGetByIdOrException(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		final long idRaum = rnd.nextLong(StundenplanManagerDummy.RAUM_MAX_ID);

		StundenplanRaum raum1 = null;
		try {
			raum1 = m1.raumGetByIdOrException(idRaum);
		} catch (@SuppressWarnings("unused") final Exception e) {
			raum1 = null;
		}

		StundenplanRaum raum2 = null;
		try {
			raum2 = m2.raumGetByIdOrException(idRaum);
		} catch (@SuppressWarnings("unused") final Exception e) {
			raum2 = null;
		}

		if ((raum1 != null) && (raum2 != null))
			assertEquals(true, raum1.id == raum2.id);
		else
			assertEquals(true, (raum1 == null) && (raum2 == null));
	}

	private static void testRaumGetMengeAsList(final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		assertEquals(m1.raumGetMengeAsList().size(), m2.raumGetMengeAsList().size());

		final @NotNull Iterator<@NotNull StundenplanRaum> i1 = m1.raumGetMengeAsList().iterator();
		final @NotNull Iterator<@NotNull StundenplanRaum> i2 = m2.raumGetMengeAsList().iterator();
		assertEquals(i1.hasNext(), i2.hasNext());
		while (i1.hasNext() || i2.hasNext()) {
			assertEquals(i1.hasNext(), i2.hasNext());
			assertEquals(i1.next().id, i2.next().id);
		}
	}

	private static void testRaumPatchAttributes(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		final @NotNull StundenplanRaum raum = StundenplanManagerDummy.raumCreateRandom(rnd);

		boolean ex1 = false;
		try {
			m1.raumPatchAttributes(raum);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex1 = true;
		}

		boolean ex2 = false;
		try {
			m2.raumPatchAttributes(raum);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex2 = true;
		}

		assertEquals(true, ex1 == ex2);
	}

	private static void testRaumRemoveById(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		final @NotNull StundenplanRaum raum = StundenplanManagerDummy.raumCreateRandom(rnd);

		boolean ex1 = false;
		try {
			m1.raumRemoveById(raum.id);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex1 = true;
		}

		boolean ex2 = false;
		try {
			m2.raumRemoveById(raum.id);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex2 = true;
		}

		assertEquals(true, ex1 == ex2);
	}

	private static void testRaumRemoveAll(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		final @NotNull List<@NotNull StundenplanRaum> raumList = StundenplanManagerDummy.raumListCreateRandom(rnd);

		boolean ex1 = false;
		try {
			m1.raumRemoveAll(raumList);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex1 = true;
		}

		boolean ex2 = false;
		try {
			m2.raumRemoveAll(raumList);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex2 = true;
		}

		assertEquals(true, ex1 == ex2);
	}

	private static void testSchueler(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		testSchuelerAdd(rnd, m1, m2);
		testSchuelerAddAll(rnd, m1, m2);
		testSchuelerGetAnzahlByKlasseIdOrException(m1, m2);
		// schuelerGetIDorException
		testSchuelerGetByIdOrException(rnd, m1, m2);
		testSchuelerGetMengeByKlasseIdAsListOrException(m1, m2);
		// schuelerGetAnzahlByKursIdAsListOrException
		// schuelerGetMengeByKursIdAsListOrException
		// schuelerRemoveById
		testSchuelerGetMengeAsList(m1, m2); // Mengen-Check zuletzt
	}

	private static void testSchuelerAdd(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		final @NotNull StundenplanSchueler schueler = StundenplanManagerDummy.schuelerCreateRandom(rnd);

		boolean ex1 = false;
		try {
			m1.schuelerAdd(schueler);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex1 = true;
		}

		boolean ex2 = false;
		try {
			m2.schuelerAdd(schueler);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex2 = true;
		}

		assertEquals(true, ex1 == ex2);
	}

	private static void testSchuelerAddAll(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		final @NotNull List<@NotNull StundenplanSchueler> schuelerList = StundenplanManagerDummy.schuelerListCreateRandom(rnd);

		boolean ex1 = false;
		try {
			m1.schuelerAddAll(schuelerList);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex1 = true;
		}

		boolean ex2 = false;
		try {
			m2.schuelerAddAll(schuelerList);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex2 = true;
		}

		assertEquals(true, ex1 == ex2);
	}

	private static void testSchuelerGetMengeByKlasseIdAsListOrException(final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		final @NotNull Iterator<@NotNull StundenplanKlasse> i1 = m1.klasseGetMengeAsList().iterator();
		final @NotNull Iterator<@NotNull StundenplanKlasse> i2 = m2.klasseGetMengeAsList().iterator();

		// Über Klassen iterieren ...
		assertEquals(m1.klasseGetMengeAsList().size(), m2.klasseGetMengeAsList().size());
		while (i1.hasNext() || i2.hasNext()) {
			// Klasse extrahieren
			final long idKlasse1 = i1.next().id;
			final long idKlasse2 = i2.next().id;
			assertEquals(i1.hasNext(), i2.hasNext());
			assertEquals(idKlasse1, idKlasse2);

			// Schüler der Klasse vergleichen ...
			final @NotNull Iterator<@NotNull StundenplanSchueler> si1 = m1.schuelerGetMengeByKlasseIdAsListOrException(idKlasse1).iterator();
			final @NotNull Iterator<@NotNull StundenplanSchueler> si2 = m2.schuelerGetMengeByKlasseIdAsListOrException(idKlasse2).iterator();
			assertEquals(m1.schuelerGetMengeByKlasseIdAsListOrException(idKlasse1).size(), m2.schuelerGetMengeByKlasseIdAsListOrException(idKlasse2).size());
			while (si1.hasNext() || si2.hasNext()) {
				assertEquals(si1.next().id, si2.next().id);
				assertEquals(si1.hasNext(), si2.hasNext());
			}
		}

	}

	private static void testSchuelerGetAnzahlByKlasseIdOrException(final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		final @NotNull Iterator<@NotNull StundenplanKlasse> i1 = m1.klasseGetMengeAsList().iterator();
		final @NotNull Iterator<@NotNull StundenplanKlasse> i2 = m2.klasseGetMengeAsList().iterator();

		assertEquals(m1.schuelerGetMengeAsList().size(), m2.schuelerGetMengeAsList().size());
		while (i1.hasNext() || i2.hasNext()) {
			final int klassengroesse1 = m1.schuelerGetAnzahlByKlasseIdOrException(i1.next().id);
			final int klassengroesse2 = m2.schuelerGetAnzahlByKlasseIdOrException(i2.next().id);
			assertEquals(klassengroesse1, klassengroesse2);
			assertEquals(i1.hasNext(), i2.hasNext());
		}
	}

	private static void testSchuelerGetByIdOrException(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		final long idSchueler = rnd.nextLong(StundenplanManagerDummy.SCHUELER_MAX_ID);

		StundenplanSchueler schueler1 = null;
		try {
			schueler1 = m1.schuelerGetByIdOrException(idSchueler);
		} catch (@SuppressWarnings("unused") final Exception e) {
			schueler1 = null;
		}

		StundenplanSchueler schueler2 = null;
		try {
			schueler2 = m2.schuelerGetByIdOrException(idSchueler);
		} catch (@SuppressWarnings("unused") final Exception e) {
			schueler2 = null;
		}

		if ((schueler1 != null) && (schueler2 != null))
			assertEquals(true, schueler1.id == schueler2.id);
		else
			assertEquals(true, (schueler1 == null) && (schueler2 == null));
	}

	private static void testSchuelerGetMengeAsList(final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		assertEquals(m1.schuelerGetMengeAsList().size(), m2.schuelerGetMengeAsList().size());

		final @NotNull Iterator<@NotNull StundenplanSchueler> i1 = m1.schuelerGetMengeAsList().iterator();
		final @NotNull Iterator<@NotNull StundenplanSchueler> i2 = m2.schuelerGetMengeAsList().iterator();
		while (i1.hasNext() || i2.hasNext()) {
			assertEquals(i1.next().id, i2.next().id);
			assertEquals(i1.hasNext(), i2.hasNext());
		}
	}

	private static void testJahrgang(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
			testJahrgangAdd(rnd, m1, m2);
			testJahrgangAddAll(rnd, m1, m2);
			testJahrgangGetByIdOrException(rnd, m1, m2);
			testJahrgangPatchAttributes(rnd, m1, m2);
//			m1.jahrgangRemoveAll
//			m1.jahrgangRemoveById
			testJahrgangGetMengeAsList(m1, m2); // Mengen-Check zuletzt
	}

	private static void testJahrgangAdd(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		final @NotNull StundenplanJahrgang jahrgang = StundenplanManagerDummy.jahrgangCreateRandom(rnd);

		boolean ex1 = false;
		try {
			m1.jahrgangAdd(jahrgang);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex1 = true;
		}

		boolean ex2 = false;
		try {
			m2.jahrgangAdd(jahrgang);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex2 = true;
		}

		assertEquals(true, ex1 == ex2);
	}

	private static void testJahrgangAddAll(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		final @NotNull List<@NotNull StundenplanJahrgang> jahrgangList = StundenplanManagerDummy.jahrgangListCreateRandom(rnd);

		boolean ex1 = false;
		try {
			m1.jahrgangAddAll(jahrgangList);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex1 = true;
		}

		boolean ex2 = false;
		try {
			m2.jahrgangAddAll(jahrgangList);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex2 = true;
		}

		assertEquals(true, ex1 == ex2);
	}

	private static void testJahrgangGetByIdOrException(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		final long idJahrgang = rnd.nextLong(StundenplanManagerDummy.JAHRGANG_MAX_ID);

		StundenplanJahrgang jahrgang1 = null;
		try {
			jahrgang1 = m1.jahrgangGetByIdOrException(idJahrgang);
		} catch (@SuppressWarnings("unused") final Exception e) {
			jahrgang1 = null;
		}

		StundenplanJahrgang jahrgang2 = null;
		try {
			jahrgang2 = m2.jahrgangGetByIdOrException(idJahrgang);
		} catch (@SuppressWarnings("unused") final Exception e) {
			jahrgang2 = null;
		}

		if ((jahrgang1 != null) && (jahrgang2 != null))
			assertEquals(true, jahrgang1.id == jahrgang2.id);
		else
			assertEquals(true, (jahrgang1 == null) && (jahrgang2 == null));
	}

	private static void testJahrgangPatchAttributes(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		final @NotNull StundenplanJahrgang jahrgang = StundenplanManagerDummy.jahrgangCreateRandom(rnd);

		boolean ex1 = false;
		try {
			m1.jahrgangPatchAttributes(jahrgang);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex1 = true;
		}

		boolean ex2 = false;
		try {
			m2.jahrgangPatchAttributes(jahrgang);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex2 = true;
		}

		assertEquals(true, ex1 == ex2);
	}

	private static void testJahrgangGetMengeAsList(final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		assertEquals(m1.jahrgangGetMengeAsList().size(), m2.jahrgangGetMengeAsList().size());

		final @NotNull Iterator<@NotNull StundenplanJahrgang> i1 = m1.jahrgangGetMengeAsList().iterator();
		final @NotNull Iterator<@NotNull StundenplanJahrgang> i2 = m2.jahrgangGetMengeAsList().iterator();
		while (i1.hasNext() || i2.hasNext()) {
			assertEquals(i1.next().id, i2.next().id);
			assertEquals(i1.hasNext(), i2.hasNext());
		}
	}

	private static void testAufsichtsbereich(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		testAufsichtsbereichAdd(rnd, m1, m2);
		testAufsichtsbereichAddAll(rnd, m1, m2);
		testAufsichtsbereichGetByIdOrException(rnd, m1, m2);
		testAufsichtsbereichPatchAttributes(rnd, m1, m2);
		testAufsichtsbereichRemoveById(rnd, m1, m2);
		testAufsichtsbereichRemoveAll(rnd, m1, m2);
		testAufsichtsbereichGetMengeAsList(m1, m2); // Mengen-Check zuletzt
	}

	private static void testAufsichtsbereichAdd(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		final @NotNull StundenplanAufsichtsbereich aufsichtsbereich = StundenplanManagerDummy.aufsichtsbereichCreateRandom(rnd);

		boolean ex1 = false;
		try {
			m1.aufsichtsbereichAdd(aufsichtsbereich);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex1 = true;
		}

		boolean ex2 = false;
		try {
			m2.aufsichtsbereichAdd(aufsichtsbereich);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex2 = true;
		}

		assertEquals(true, ex1 == ex2);
	}

	private static void testAufsichtsbereichAddAll(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		final @NotNull List<@NotNull StundenplanAufsichtsbereich> aufsichtsbereichList = StundenplanManagerDummy.aufsichtsbereichListCreateRandom(rnd);

		boolean ex1 = false;
		try {
			m1.aufsichtsbereichAddAll(aufsichtsbereichList);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex1 = true;
		}

		boolean ex2 = false;
		try {
			m2.aufsichtsbereichAddAll(aufsichtsbereichList);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex2 = true;
		}

		assertEquals(true, ex1 == ex2);
	}

	private static void testAufsichtsbereichGetByIdOrException(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		final long idAufsichtsbereich = rnd.nextLong(StundenplanManagerDummy.AUFSICHTSBEREICH_MAX_ID);

		StundenplanAufsichtsbereich aufsichtsbereich1 = null;
		try {
			aufsichtsbereich1 = m1.aufsichtsbereichGetByIdOrException(idAufsichtsbereich);
		} catch (@SuppressWarnings("unused") final Exception e) {
			aufsichtsbereich1 = null;
		}

		StundenplanAufsichtsbereich aufsichtsbereich2 = null;
		try {
			aufsichtsbereich2 = m2.aufsichtsbereichGetByIdOrException(idAufsichtsbereich);
		} catch (@SuppressWarnings("unused") final Exception e) {
			aufsichtsbereich2 = null;
		}

		if ((aufsichtsbereich1 != null) && (aufsichtsbereich2 != null))
			assertEquals(true, aufsichtsbereich1.id == aufsichtsbereich2.id);
		else
			assertEquals(true, (aufsichtsbereich1 == null) && (aufsichtsbereich2 == null));
	}

	private static void testAufsichtsbereichPatchAttributes(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		final @NotNull StundenplanAufsichtsbereich aufsichtsbereich = StundenplanManagerDummy.aufsichtsbereichCreateRandom(rnd);

		boolean ex1 = false;
		try {
			m1.aufsichtsbereichPatchAttributes(aufsichtsbereich);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex1 = true;
		}

		boolean ex2 = false;
		try {
			m2.aufsichtsbereichPatchAttributes(aufsichtsbereich);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex2 = true;
		}

		assertEquals(true, ex1 == ex2);
	}

	private static void testAufsichtsbereichRemoveById(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		final @NotNull StundenplanAufsichtsbereich aufsichtsbereich = StundenplanManagerDummy.aufsichtsbereichCreateRandom(rnd);

		boolean ex1 = false;
		try {
			m1.aufsichtsbereichRemoveById(aufsichtsbereich.id);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex1 = true;
		}

		boolean ex2 = false;
		try {
			m2.aufsichtsbereichRemoveById(aufsichtsbereich.id);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex2 = true;
		}

		assertEquals(true, ex1 == ex2);
	}

	private static void testAufsichtsbereichRemoveAll(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		final @NotNull List<@NotNull StundenplanAufsichtsbereich> aufsichtsbereichList = StundenplanManagerDummy.aufsichtsbereichListCreateRandom(rnd);

		boolean ex1 = false;
		try {
			m1.aufsichtsbereichRemoveAll(aufsichtsbereichList);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex1 = true;
		}

		boolean ex2 = false;
		try {
			m2.aufsichtsbereichRemoveAll(aufsichtsbereichList);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex2 = true;
		}

		assertEquals(true, ex1 == ex2);
	}

	private static void testAufsichtsbereichGetMengeAsList(final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		assertEquals(m1.aufsichtsbereichGetMengeAsList().size(), m2.aufsichtsbereichGetMengeAsList().size());

		final @NotNull Iterator<@NotNull StundenplanAufsichtsbereich> i1 = m1.aufsichtsbereichGetMengeAsList().iterator();
		final @NotNull Iterator<@NotNull StundenplanAufsichtsbereich> i2 = m2.aufsichtsbereichGetMengeAsList().iterator();
		while (i1.hasNext() || i2.hasNext()) {
			assertEquals(i1.next().id, i2.next().id);
			assertEquals(i1.hasNext(), i2.hasNext());
		}
	}

	private static void testZeitraster(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		testZeitrasterAdd(rnd, m1, m2);
		testZeitrasterAddAll(rnd, m1, m2); // TODO add all erzeugt ungleichen Zustand
		testZeitrasterExistsByWochentag(rnd, m1, m2);
		testZeitrasterExistsByWochentagAndStunde(rnd, m1, m2);
	}

	private static void testZeitrasterAdd(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		final @NotNull StundenplanZeitraster zeitraster = StundenplanManagerDummy.zeitrasterCreateRandom(rnd);

		boolean ex1 = false;
		try {
			m1.zeitrasterAdd(zeitraster);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex1 = true;
		}

		boolean ex2 = false;
		try {
			m2.zeitrasterAdd(zeitraster);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex2 = true;
		}

		assertEquals(true, ex1 == ex2);
	}

	private static void testZeitrasterAddAll(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		final @NotNull List<@NotNull StundenplanZeitraster> zeitrasterList = StundenplanManagerDummy.zeitrasterListCreateRandom(rnd);

		boolean ex1 = false;
		try {
			m1.zeitrasterAddAll(zeitrasterList);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex1 = true;
		}

		boolean ex2 = false;
		try {
			m2.zeitrasterAddAll(zeitrasterList);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex2 = true;
		}

		assertEquals(true, ex1 == ex2);
	}

	private static void testZeitrasterExistsByWochentag(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		final int wochentag = StundenplanManagerDummy.zeitrasterGetRandomWochentag(rnd).id;
		final boolean b1 = m1.zeitrasterExistsByWochentag(wochentag);
		final boolean b2 = m2.zeitrasterExistsByWochentag(wochentag);
		assertEquals(b1, b2);
	}

	private static void testZeitrasterExistsByWochentagAndStunde(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		final int wochentag = StundenplanManagerDummy.zeitrasterGetRandomWochentag(rnd).id;
		final int stunde = StundenplanManagerDummy.zeitrasterGetRandomStunde(rnd);
		final boolean b1 = m1.zeitrasterExistsByWochentagAndStunde(wochentag, stunde);
		final boolean b2 = m2.zeitrasterExistsByWochentagAndStunde(wochentag, stunde);
		assertEquals(b1, b2);
	}

	private static void testLehrer(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		testLehrerAdd(rnd, m1, m2);
		testLehrerAddAll(rnd, m1, m2);
		testLehrerGetByIdOrException(rnd, m1, m2);
		testLehrerPatchAttributes(rnd, m1, m2);
		testLehrerRemoveById(rnd, m1, m2);
		testLehrerRemoveAll(rnd, m1, m2);
		testLehrerGetMengeAsList(m1, m2); // Mengen-Check zuletzt
	}

	private static void testLehrerAdd(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		final @NotNull StundenplanLehrer lehrer = StundenplanManagerDummy.lehrerCreateRandom(rnd);

		boolean ex1 = false;
		try {
			m1.lehrerAdd(lehrer);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex1 = true;
		}

		boolean ex2 = false;
		try {
			m2.lehrerAdd(lehrer);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex2 = true;
		}

		assertEquals(true, ex1 == ex2);
	}

	private static void testLehrerAddAll(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		final @NotNull List<@NotNull StundenplanLehrer> lehrerList = StundenplanManagerDummy.lehrerListCreateRandom(rnd);

		boolean ex1 = false;
		try {
			m1.lehrerAddAll(lehrerList);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex1 = true;
		}

		boolean ex2 = false;
		try {
			m2.lehrerAddAll(lehrerList);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex2 = true;
		}

		assertEquals(true, ex1 == ex2);
	}

	private static void testLehrerGetByIdOrException(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		final long idLehrer = rnd.nextLong(StundenplanManagerDummy.LEHRER_MAX_ID);

		StundenplanLehrer lehrer1 = null;
		try {
			lehrer1 = m1.lehrerGetByIdOrException(idLehrer);
		} catch (@SuppressWarnings("unused") final Exception e) {
			lehrer1 = null;
		}

		StundenplanLehrer lehrer2 = null;
		try {
			lehrer2 = m2.lehrerGetByIdOrException(idLehrer);
		} catch (@SuppressWarnings("unused") final Exception e) {
			lehrer2 = null;
		}

		if ((lehrer1 != null) && (lehrer2 != null))
			assertEquals(true, lehrer1.id == lehrer2.id);
		else
			assertEquals(true, (lehrer1 == null) && (lehrer2 == null));
	}

	private static void testLehrerPatchAttributes(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		final @NotNull StundenplanLehrer lehrer = StundenplanManagerDummy.lehrerCreateRandom(rnd);

		boolean ex1 = false;
		try {
			m1.lehrerPatchAttributes(lehrer);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex1 = true;
		}

		boolean ex2 = false;
		try {
			m2.lehrerPatchAttributes(lehrer);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex2 = true;
		}

		assertEquals(true, ex1 == ex2);
	}

	private static void testLehrerRemoveById(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		final @NotNull StundenplanLehrer lehrer = StundenplanManagerDummy.lehrerCreateRandom(rnd);

		boolean ex1 = false;
		try {
			m1.lehrerRemoveById(lehrer.id);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex1 = true;
		}

		boolean ex2 = false;
		try {
			m2.lehrerRemoveById(lehrer.id);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex2 = true;
		}

		assertEquals(true, ex1 == ex2);
	}

	private static void testLehrerRemoveAll(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		final @NotNull List<@NotNull StundenplanLehrer> lehrerList = StundenplanManagerDummy.lehrerListCreateRandom(rnd);

		boolean ex1 = false;
		try {
			m1.lehrerRemoveAll(lehrerList);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex1 = true;
		}

		boolean ex2 = false;
		try {
			m2.lehrerRemoveAll(lehrerList);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex2 = true;
		}

		assertEquals(true, ex1 == ex2);
	}

	private static void testLehrerGetMengeAsList(final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		assertEquals(m1.lehrerGetMengeAsList().size(), m2.lehrerGetMengeAsList().size());

		final @NotNull Iterator<@NotNull StundenplanLehrer> i1 = m1.lehrerGetMengeAsList().iterator();
		final @NotNull Iterator<@NotNull StundenplanLehrer> i2 = m2.lehrerGetMengeAsList().iterator();
		assertEquals(i1.hasNext(), i2.hasNext());
		while (i1.hasNext() || i2.hasNext()) {
			assertEquals(i1.hasNext(), i2.hasNext());
			assertEquals(i1.next().id, i2.next().id);
		}
	}

	private static void testKlasse(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		testKlasseAdd(rnd, m1, m2);
		testKlasseGetMengeAsList(m1, m2); // Mengen-Check zuletzt
	}

	private static void testKlasseAdd(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		final @NotNull StundenplanKlasse klasse = StundenplanManagerDummy.klasseCreateRandom(rnd);

		boolean ex1 = false;
		try {
			m1.klasseAdd(klasse);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex1 = true;
		}

		boolean ex2 = false;
		try {
			m2.klasseAdd(klasse);
		} catch (@SuppressWarnings("unused") final Exception e) {
			ex2 = true;
		}

		assertEquals(true, ex1 == ex2);
	}

	private static void testKlasseGetMengeAsList(final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		assertEquals(m1.klasseGetMengeAsList().size(), m2.klasseGetMengeAsList().size());

		final @NotNull Iterator<@NotNull StundenplanKlasse> i1 = m1.klasseGetMengeAsList().iterator();
		final @NotNull Iterator<@NotNull StundenplanKlasse> i2 = m2.klasseGetMengeAsList().iterator();
		while (i1.hasNext() || i2.hasNext()) {
			assertEquals(i1.next().id, i2.next().id);
			assertEquals(i1.hasNext(), i2.hasNext());
		}
	}
}
