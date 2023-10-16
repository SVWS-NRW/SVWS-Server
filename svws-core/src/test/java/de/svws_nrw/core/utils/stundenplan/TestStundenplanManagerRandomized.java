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
	@DisplayName("testStundenplanManager")
	@Test
	void testRandom() {
		for (int runden = 1; runden <= 1024; runden *= 2)
			testModifications(runden);
	}

	private static void testModifications(final int runden) {
		// System.out.println("\nRunden = " + runden);
		final @NotNull Random rnd = new Random(_SEED);

		final @NotNull StundenplanKomplett komplett = new StundenplanKomplett();
		komplett.daten.gueltigAb = "2022-03-15";
		komplett.daten.gueltigBis = "2022-09-25";

		final @NotNull StundenplanManager m1 = new StundenplanManager(komplett);
		final @NotNull StundenplanManagerDummy m2 = new StundenplanManagerDummy();

		for (int i = 0; i < runden; i++)
			testModification(rnd, m1, m2);
	}

	private static void testModification(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		testModificationFach(rnd, m1, m2);
		testModificationRaum(rnd, m1, m2);
		testModificationSchueler(rnd, m1, m2);
		testModificationJahrgang(rnd, m1, m2);
		testModificationAufsichtsbereich(rnd, m1, m2);
		testModificationZeitraster(rnd, m1, m2);

		testModificationLehrer(rnd, m1, m2);
		testModificationKlasse(rnd, m1, m2);

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

	private static void testModificationFach(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		testModificationFachAdd(rnd, m1, m2);
		testModificationFachAddAll(rnd, m1, m2);
		testModificationFachGetByIdOrException(rnd, m1, m2);
		testModificationFachGetMengeAsList(m1, m2); // Mengen-Check zuletzt
	}

	private static void testModificationFachAdd(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testModificationFachAddAll(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testModificationFachGetByIdOrException(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testModificationFachGetMengeAsList(final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		assertEquals(m1.fachGetMengeAsList().size(), m2.fachGetMengeAsList().size());

		final @NotNull Iterator<@NotNull StundenplanFach> i1 = m1.fachGetMengeAsList().iterator();
		final @NotNull Iterator<@NotNull StundenplanFach> i2 = m2.fachGetMengeAsList().iterator();
		assertEquals(i1.hasNext(), i2.hasNext());
		while (i1.hasNext() || i2.hasNext()) {
			assertEquals(i1.hasNext(), i2.hasNext());
			assertEquals(i1.next().id, i2.next().id);
		}
	}

	private static void testModificationRaum(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		testModificationRaumAdd(rnd, m1, m2);
		testModificationRaumAddAll(rnd, m1, m2);
		testModificationRaumGetByIdOrException(rnd, m1, m2);
		testModificationRaumPatchAttributes(rnd, m1, m2);
		testModificationRaumRemoveById(rnd, m1, m2);
		testModificationRaumRemoveAll(rnd, m1, m2);
		testModificationRaumGetMengeAsList(m1, m2); // Mengen-Check zuletzt
	}

	private static void testModificationRaumAdd(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testModificationRaumAddAll(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testModificationRaumGetByIdOrException(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testModificationRaumGetMengeAsList(final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		assertEquals(m1.raumGetMengeAsList().size(), m2.raumGetMengeAsList().size());

		final @NotNull Iterator<@NotNull StundenplanRaum> i1 = m1.raumGetMengeAsList().iterator();
		final @NotNull Iterator<@NotNull StundenplanRaum> i2 = m2.raumGetMengeAsList().iterator();
		assertEquals(i1.hasNext(), i2.hasNext());
		while (i1.hasNext() || i2.hasNext()) {
			assertEquals(i1.hasNext(), i2.hasNext());
			assertEquals(i1.next().id, i2.next().id);
		}
	}

	private static void testModificationRaumPatchAttributes(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testModificationRaumRemoveById(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testModificationRaumRemoveAll(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testModificationSchueler(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		testModificationSchuelerAdd(rnd, m1, m2);
		testModificationSchuelerAddAll(rnd, m1, m2);
		testModificationSchuelerGetAnzahlByKlasseIdOrException(m1, m2);
		// schuelerGetIDorException
		testModificationSchuelerGetByIdOrException(rnd, m1, m2);
		testModificationSchuelerGetMengeByKlasseIdAsListOrException(m1, m2);
		// schuelerGetAnzahlByKursIdAsListOrException
		// schuelerGetMengeByKursIdAsListOrException
		// schuelerRemoveById
		testModificationSchuelerGetMengeAsList(m1, m2); // Mengen-Check zuletzt
	}

	private static void testModificationSchuelerAdd(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testModificationSchuelerAddAll(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testModificationSchuelerGetMengeByKlasseIdAsListOrException(final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testModificationSchuelerGetAnzahlByKlasseIdOrException(final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testModificationSchuelerGetByIdOrException(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testModificationSchuelerGetMengeAsList(final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		assertEquals(m1.schuelerGetMengeAsList().size(), m2.schuelerGetMengeAsList().size());

		final @NotNull Iterator<@NotNull StundenplanSchueler> i1 = m1.schuelerGetMengeAsList().iterator();
		final @NotNull Iterator<@NotNull StundenplanSchueler> i2 = m2.schuelerGetMengeAsList().iterator();
		while (i1.hasNext() || i2.hasNext()) {
			assertEquals(i1.next().id, i2.next().id);
			assertEquals(i1.hasNext(), i2.hasNext());
		}
	}

	private static void testModificationJahrgang(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
			testModificationJahrgangAdd(rnd, m1, m2);
			testModificationJahrgangAddAll(rnd, m1, m2);
			testModificationJahrgangGetByIdOrException(rnd, m1, m2);
			testModificationJahrgangPatchAttributes(rnd, m1, m2);
//			m1.jahrgangRemoveAll
//			m1.jahrgangRemoveById
			testModificationJahrgangGetMengeAsList(m1, m2); // Mengen-Check zuletzt
	}

	private static void testModificationJahrgangAdd(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testModificationJahrgangAddAll(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testModificationJahrgangGetByIdOrException(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testModificationJahrgangPatchAttributes(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testModificationJahrgangGetMengeAsList(final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		assertEquals(m1.jahrgangGetMengeAsList().size(), m2.jahrgangGetMengeAsList().size());

		final @NotNull Iterator<@NotNull StundenplanJahrgang> i1 = m1.jahrgangGetMengeAsList().iterator();
		final @NotNull Iterator<@NotNull StundenplanJahrgang> i2 = m2.jahrgangGetMengeAsList().iterator();
		while (i1.hasNext() || i2.hasNext()) {
			assertEquals(i1.next().id, i2.next().id);
			assertEquals(i1.hasNext(), i2.hasNext());
		}
	}

	private static void testModificationAufsichtsbereich(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		testModificationAufsichtsbereichAdd(rnd, m1, m2);
		testModificationAufsichtsbereichAddAll(rnd, m1, m2);
		testModificationAufsichtsbereichGetByIdOrException(rnd, m1, m2);
		testModificationAufsichtsbereichPatchAttributes(rnd, m1, m2);
		testModificationAufsichtsbereichRemoveById(rnd, m1, m2);
		testModificationAufsichtsbereichRemoveAll(rnd, m1, m2);
		testModificationAufsichtsbereichGetMengeAsList(m1, m2); // Mengen-Check zuletzt
	}

	private static void testModificationAufsichtsbereichAdd(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testModificationAufsichtsbereichAddAll(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testModificationAufsichtsbereichGetByIdOrException(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testModificationAufsichtsbereichPatchAttributes(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testModificationAufsichtsbereichRemoveById(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testModificationAufsichtsbereichRemoveAll(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testModificationAufsichtsbereichGetMengeAsList(final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		assertEquals(m1.aufsichtsbereichGetMengeAsList().size(), m2.aufsichtsbereichGetMengeAsList().size());

		final @NotNull Iterator<@NotNull StundenplanAufsichtsbereich> i1 = m1.aufsichtsbereichGetMengeAsList().iterator();
		final @NotNull Iterator<@NotNull StundenplanAufsichtsbereich> i2 = m2.aufsichtsbereichGetMengeAsList().iterator();
		while (i1.hasNext() || i2.hasNext()) {
			assertEquals(i1.next().id, i2.next().id);
			assertEquals(i1.hasNext(), i2.hasNext());
		}
	}

	private static void testModificationZeitraster(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		testModificationZeitrasterAdd(rnd, m1, m2);
	}

	private static void testModificationZeitrasterAdd(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testModificationLehrer(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		testModificationLehrerAdd(rnd, m1, m2);
		testModificationLehrerAddAll(rnd, m1, m2);
		testModificationLehrerGetByIdOrException(rnd, m1, m2);
		testModificationLehrerPatchAttributes(rnd, m1, m2);
		testModificationLehrerRemoveById(rnd, m1, m2);
		testModificationLehrerRemoveAll(rnd, m1, m2);
		testModificationLehrerGetMengeAsList(m1, m2); // Mengen-Check zuletzt
	}

	private static void testModificationLehrerAdd(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testModificationLehrerAddAll(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testModificationLehrerGetByIdOrException(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testModificationLehrerPatchAttributes(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testModificationLehrerRemoveById(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testModificationLehrerRemoveAll(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testModificationLehrerGetMengeAsList(final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		assertEquals(m1.lehrerGetMengeAsList().size(), m2.lehrerGetMengeAsList().size());

		final @NotNull Iterator<@NotNull StundenplanLehrer> i1 = m1.lehrerGetMengeAsList().iterator();
		final @NotNull Iterator<@NotNull StundenplanLehrer> i2 = m2.lehrerGetMengeAsList().iterator();
		assertEquals(i1.hasNext(), i2.hasNext());
		while (i1.hasNext() || i2.hasNext()) {
			assertEquals(i1.hasNext(), i2.hasNext());
			assertEquals(i1.next().id, i2.next().id);
		}
	}

	private static void testModificationKlasse(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		testModificationKlasseAdd(rnd, m1, m2);
		testModificationKlasseGetMengeAsList(m1, m2); // Mengen-Check zuletzt
	}

	private static void testModificationKlasseAdd(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testModificationKlasseGetMengeAsList(final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		assertEquals(m1.klasseGetMengeAsList().size(), m2.klasseGetMengeAsList().size());

		final @NotNull Iterator<@NotNull StundenplanKlasse> i1 = m1.klasseGetMengeAsList().iterator();
		final @NotNull Iterator<@NotNull StundenplanKlasse> i2 = m2.klasseGetMengeAsList().iterator();
		while (i1.hasNext() || i2.hasNext()) {
			assertEquals(i1.next().id, i2.next().id);
			assertEquals(i1.hasNext(), i2.hasNext());
		}
	}
}
