package de.svws_nrw.core.utils.stundenplan;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.svws_nrw.core.data.stundenplan.StundenplanFach;
import de.svws_nrw.core.data.stundenplan.StundenplanKomplett;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse testet den {@link StundenplanManager}.
 */
@DisplayName("Diese Klasse testet den {@link StundenplanManager}.")
@TestMethodOrder(MethodOrderer.MethodName.class)
class TestStundenplanManagerRandomized {

	private static final long _SEED = 1L;

	/**
	 * Diese Klasse testet den {@link StundenplanManager} mit randomisierten Daten.
	 */
	@DisplayName("testStundenplanManager")
	@Test
	void testManagerRandom() {
		testManagerModifications(1);
		testManagerModifications(2);
		testManagerModifications(4);
		testManagerModifications(8);
		testManagerModifications(16);
		testManagerModifications(32);
		testManagerModifications(64);
		testManagerModifications(128);
		testManagerModifications(256);
	}

	private static void testManagerModifications(final int runden) {
		System.out.println("\nRunden = " + runden);
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
		// kalenderwochenzuordnung
		// fach
		// jahrgang
		// zeitraster
		// raum
		// pausenzeit
		// aufsichtsbereich
		// lehrer
		// schueler
		// klasse
		// schiene
		// klassenunterricht
		// pausenaufsicht
		// kurs
		// unterricht

		testManagerModificationFach(rnd, m1, m2);

	}

	private static void testManagerModificationFach(final @NotNull Random rnd, final @NotNull StundenplanManager m1, final @NotNull StundenplanManagerDummy m2) {
		testManagerModificationFachAdd(rnd, m1, m2);
		testManagerModificationFachAddAll(rnd, m1, m2);
		testManagerModificationFachGetByIdOrException(rnd, m1, m2);
		testManagerModificationFachGetMengeAsList(m1, m2);
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
		while (i1.hasNext() || i2.hasNext()) {
			assertEquals(i1.hasNext(), i2.hasNext());
			assertEquals(i1.next().id, i2.next().id);
		}
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

	@SuppressWarnings("unused")
	private static String fachListToString(@NotNull final List<@NotNull StundenplanFach> fachList) {
		String s = "";
		for (final StundenplanFach fach : fachList)
			s = s + ", " + fach.id;
		return s;
	}

}
