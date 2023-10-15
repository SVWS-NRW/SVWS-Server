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
	void testManagerRandom() {
		for (int runden = 1; runden <= 1024; runden *= 2)
			testManagerModifications(runden);
	}

	private static void testManagerModifications(final int runden) {
		// System.out.println("\nRunden = " + runden)
		final @NotNull Random rnd = new Random(_SEED);

		final @NotNull StundenplanKomplett komplett = new StundenplanKomplett();
		komplett.daten.gueltigAb = "2022-03-15";
		komplett.daten.gueltigBis = "2022-09-25";

		final @NotNull StundenplanManager m1 = new StundenplanManager(komplett);
		final @NotNull StundenplanManagerDummy m2 = new StundenplanManagerDummy();

		for (int i = 0; i < runden; i++)
			testManagerModification(rnd, m1, m2);
	}

	private static void testManagerModification(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		testManagerModificationFach(rnd, m1, m2);
		testManagerModificationRaum(rnd, m1, m2);
		testManagerModificationSchueler(rnd, m1, m2);
		testManagerModificationJahrgang(rnd, m1, m2);
		testManagerModificationAufsichtsbereich(rnd, m1, m2);

		testManagerModificationLehrer(rnd, m1, m2);
		testManagerModificationKlasse(rnd, m1, m2);

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

	private static void testManagerModificationFach(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		testManagerModificationFachAdd(rnd, m1, m2);
		testManagerModificationFachAddAll(rnd, m1, m2);
		testManagerModificationFachGetByIdOrException(rnd, m1, m2);
		testManagerModificationFachGetMengeAsList(m1, m2); // Mengen-Check zuletzt
	}

	private static void testManagerModificationFachAdd(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testManagerModificationFachAddAll(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testManagerModificationFachGetByIdOrException(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testManagerModificationFachGetMengeAsList(final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		assertEquals(m1.fachGetMengeAsList().size(), m2.fachGetMengeAsList().size());

		final @NotNull Iterator<@NotNull StundenplanFach> i1 = m1.fachGetMengeAsList().iterator();
		final @NotNull Iterator<@NotNull StundenplanFach> i2 = m2.fachGetMengeAsList().iterator();
		assertEquals(i1.hasNext(), i2.hasNext());
		while (i1.hasNext() || i2.hasNext()) {
			assertEquals(i1.hasNext(), i2.hasNext());
			assertEquals(i1.next().id, i2.next().id);
		}
	}

	private static void testManagerModificationRaum(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		testManagerModificationRaumAdd(rnd, m1, m2);
		testManagerModificationRaumAddAll(rnd, m1, m2);
		testManagerModificationRaumGetByIdOrException(rnd, m1, m2);
		testManagerModificationRaumPatchAttributes(rnd, m1, m2);
		testManagerModificationRaumRemoveById(rnd, m1, m2);
		testManagerModificationRaumRemoveAll(rnd, m1, m2);
		testManagerModificationRaumGetMengeAsList(m1, m2); // Mengen-Check zuletzt
	}

	private static void testManagerModificationRaumAdd(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testManagerModificationRaumAddAll(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testManagerModificationRaumGetByIdOrException(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testManagerModificationRaumGetMengeAsList(final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		assertEquals(m1.raumGetMengeAsList().size(), m2.raumGetMengeAsList().size());

		final @NotNull Iterator<@NotNull StundenplanRaum> i1 = m1.raumGetMengeAsList().iterator();
		final @NotNull Iterator<@NotNull StundenplanRaum> i2 = m2.raumGetMengeAsList().iterator();
		assertEquals(i1.hasNext(), i2.hasNext());
		while (i1.hasNext() || i2.hasNext()) {
			assertEquals(i1.hasNext(), i2.hasNext());
			assertEquals(i1.next().id, i2.next().id);
		}
	}

	private static void testManagerModificationRaumPatchAttributes(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testManagerModificationRaumRemoveById(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testManagerModificationRaumRemoveAll(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testManagerModificationSchueler(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		testManagerModificationSchuelerAdd(rnd, m1, m2);
		testManagerModificationSchuelerAddAll(rnd, m1, m2);
		testManagerModificationSchuelerGetAnzahlByKlasseIdOrException(m1, m2);
		// schuelerGetIDorException
		testManagerModificationSchuelerGetByIdOrException(rnd, m1, m2);
		testManagerModificationSchuelerGetMengeByKlasseIdAsListOrException(m1, m2);
		// schuelerGetAnzahlByKursIdAsListOrException
		// schuelerGetMengeByKursIdAsListOrException
		// schuelerRemoveById
		testManagerModificationSchuelerGetMengeAsList(m1, m2); // Mengen-Check zuletzt
	}

	private static void testManagerModificationSchuelerAdd(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testManagerModificationSchuelerAddAll(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testManagerModificationSchuelerGetMengeByKlasseIdAsListOrException(final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testManagerModificationSchuelerGetAnzahlByKlasseIdOrException(final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testManagerModificationSchuelerGetByIdOrException(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testManagerModificationSchuelerGetMengeAsList(final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		assertEquals(m1.schuelerGetMengeAsList().size(), m2.schuelerGetMengeAsList().size());

		final @NotNull Iterator<@NotNull StundenplanSchueler> i1 = m1.schuelerGetMengeAsList().iterator();
		final @NotNull Iterator<@NotNull StundenplanSchueler> i2 = m2.schuelerGetMengeAsList().iterator();
		while (i1.hasNext() || i2.hasNext()) {
			assertEquals(i1.next().id, i2.next().id);
			assertEquals(i1.hasNext(), i2.hasNext());
		}
	}

	private static void testManagerModificationJahrgang(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
			testManagerModificationJahrgangAdd(rnd, m1, m2);
			testManagerModificationJahrgangAddAll(rnd, m1, m2);
			testManagerModificationJahrgangGetByIdOrException(rnd, m1, m2);
			testManagerModificationJahrgangPatchAttributes(rnd, m1, m2);
//			m1.jahrgangRemoveAll
//			m1.jahrgangRemoveById
			testManagerModificationJahrgangGetMengeAsList(m1, m2); // Mengen-Check zuletzt
	}

	private static void testManagerModificationJahrgangAdd(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testManagerModificationJahrgangAddAll(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testManagerModificationJahrgangGetByIdOrException(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testManagerModificationJahrgangPatchAttributes(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testManagerModificationJahrgangGetMengeAsList(final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		assertEquals(m1.jahrgangGetMengeAsList().size(), m2.jahrgangGetMengeAsList().size());

		final @NotNull Iterator<@NotNull StundenplanJahrgang> i1 = m1.jahrgangGetMengeAsList().iterator();
		final @NotNull Iterator<@NotNull StundenplanJahrgang> i2 = m2.jahrgangGetMengeAsList().iterator();
		while (i1.hasNext() || i2.hasNext()) {
			assertEquals(i1.next().id, i2.next().id);
			assertEquals(i1.hasNext(), i2.hasNext());
		}
	}

	private static void testManagerModificationAufsichtsbereich(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		testManagerModificationAufsichtsbereichAdd(rnd, m1, m2);
		testManagerModificationAufsichtsbereichAddAll(rnd, m1, m2);
		testManagerModificationAufsichtsbereichGetByIdOrException(rnd, m1, m2);
		testManagerModificationAufsichtsbereichPatchAttributes(rnd, m1, m2);
		testManagerModificationAufsichtsbereichRemoveById(rnd, m1, m2);
		testManagerModificationAufsichtsbereichRemoveAll(rnd, m1, m2);
		testManagerModificationAufsichtsbereichGetMengeAsList(m1, m2); // Mengen-Check zuletzt
	}

	private static void testManagerModificationAufsichtsbereichAdd(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testManagerModificationAufsichtsbereichAddAll(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testManagerModificationAufsichtsbereichGetByIdOrException(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testManagerModificationAufsichtsbereichPatchAttributes(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testManagerModificationAufsichtsbereichRemoveById(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testManagerModificationAufsichtsbereichRemoveAll(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testManagerModificationAufsichtsbereichGetMengeAsList(final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		assertEquals(m1.aufsichtsbereichGetMengeAsList().size(), m2.aufsichtsbereichGetMengeAsList().size());

		final @NotNull Iterator<@NotNull StundenplanAufsichtsbereich> i1 = m1.aufsichtsbereichGetMengeAsList().iterator();
		final @NotNull Iterator<@NotNull StundenplanAufsichtsbereich> i2 = m2.aufsichtsbereichGetMengeAsList().iterator();
		while (i1.hasNext() || i2.hasNext()) {
			assertEquals(i1.next().id, i2.next().id);
			assertEquals(i1.hasNext(), i2.hasNext());
		}
	}

	private static void testManagerModificationLehrer(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		testManagerModificationLehrerAdd(rnd, m1, m2);
		testManagerModificationLehrerAddAll(rnd, m1, m2);
		testManagerModificationLehrerGetByIdOrException(rnd, m1, m2);
		testManagerModificationLehrerPatchAttributes(rnd, m1, m2);
		testManagerModificationLehrerRemoveById(rnd, m1, m2);
		testManagerModificationLehrerRemoveAll(rnd, m1, m2);
		testManagerModificationLehrerGetMengeAsList(m1, m2); // Mengen-Check zuletzt
	}

	private static void testManagerModificationLehrerAdd(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testManagerModificationLehrerAddAll(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testManagerModificationLehrerGetByIdOrException(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testManagerModificationLehrerPatchAttributes(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testManagerModificationLehrerRemoveById(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testManagerModificationLehrerRemoveAll(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testManagerModificationLehrerGetMengeAsList(final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		assertEquals(m1.lehrerGetMengeAsList().size(), m2.lehrerGetMengeAsList().size());

		final @NotNull Iterator<@NotNull StundenplanLehrer> i1 = m1.lehrerGetMengeAsList().iterator();
		final @NotNull Iterator<@NotNull StundenplanLehrer> i2 = m2.lehrerGetMengeAsList().iterator();
		assertEquals(i1.hasNext(), i2.hasNext());
		while (i1.hasNext() || i2.hasNext()) {
			assertEquals(i1.hasNext(), i2.hasNext());
			assertEquals(i1.next().id, i2.next().id);
		}
	}

	private static void testManagerModificationKlasse(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		testManagerModificationKlasseAdd(rnd, m1, m2);
		testManagerModificationKlasseGetMengeAsList(m1, m2); // Mengen-Check zuletzt
	}

	private static void testManagerModificationKlasseAdd(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
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

	private static void testManagerModificationKlasseGetMengeAsList(final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		assertEquals(m1.klasseGetMengeAsList().size(), m2.klasseGetMengeAsList().size());

		final @NotNull Iterator<@NotNull StundenplanKlasse> i1 = m1.klasseGetMengeAsList().iterator();
		final @NotNull Iterator<@NotNull StundenplanKlasse> i2 = m2.klasseGetMengeAsList().iterator();
		while (i1.hasNext() || i2.hasNext()) {
			assertEquals(i1.next().id, i2.next().id);
			assertEquals(i1.hasNext(), i2.hasNext());
		}
	}
}
