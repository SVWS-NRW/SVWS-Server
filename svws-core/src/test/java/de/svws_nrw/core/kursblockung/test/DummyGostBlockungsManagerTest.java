package de.svws_nrw.core.kursblockung.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostBlockungKursLehrer;
import de.svws_nrw.core.data.gost.GostBlockungSchiene;
import de.svws_nrw.core.data.gost.GostBlockungsdaten;
import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import jakarta.validation.constraints.NotNull;

/**
 * Testet den {@link GostBlockungsergebnisManager} und den {@link GostBlockungsergebnisManager}.
 * Zum Testen der Methoden werden zwei weitere Manager erzeugt, die das Verhalten mit randomosierten Tests immitieren.
 */
@DisplayName("Testet den {@link GostBlockungsergebnisManager} und den {@link GostBlockungsergebnisManager}.")
@TestMethodOrder(MethodOrderer.MethodName.class)
class DummyGostBlockungsManagerTest {

	private static final int RUNDEN = 10000;
	private static final int SCHULJAHR = 2024;

	private final Random RND = new Random(1);
	private final GostFaecherManager FM = new GostFaecherManager(SCHULJAHR);
	private final GostBlockungsdatenManager m1 = new GostBlockungsdatenManager(new GostBlockungsdaten(), FM);
	private final DummyGostBlockungsdatenManager m2 = new DummyGostBlockungsdatenManager(new GostBlockungsdaten(), FM);

	// Grenzen für die Anzahl bestimmer Objekte während der randomisierten Erzeugung.
	private static final int MAX_SCHIENEN = 13;
	private static final int MAX_KURSE = 50;
	private static final int MAX_LEHRER = 60;
	private static final int MAX_KURS_LEHRER = 3;
	private static final int MAX_ERGEBNISSE = 2;

	/**
	 * Initialisierung der Core-Types
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	/**
	 * Testet alle Methoden von {@link GostBlockungsergebnisManager} und {@link GostBlockungsergebnisManager}.
	 */
	@Test
	@DisplayName("testeMehrereRunden")
	void testeMehrereRunden() {
		// FaecherManager
		createFaecher();

		// Ohne Nummer Exception, beim Hinzufügen von Ergebnissen.
		m1.setID(777);
		m2.setID(777);

		// In jeder Runde werden alle Methoden in permutierter Reihenfolge durchlaufen.
		for (int i = 1; i <= RUNDEN; i++) {
			testeEinePermutation();
			/*	if ((i == 1) || (i == 10) || (i == 100) || (i == 1000) || (i == 10000) || (i == 100000))
				System.out.println("Runde " + i
						+ ": Ergebnisse " + m1.ergebnisGetAnzahl()
						+ ", Schienen " + m1.schieneGetAnzahl()
						+ ", Kurse " + m1.kursGetAnzahl());*/
		}
	}

	private void testeEinePermutation() {
		// Erzeuge eine zufällige Permutation der Methoden-Aufrufe.
		final int[] perm = new int[100];
		for (int i = 0; i < perm.length; i++)
			perm[i] = i;
		for (int i1 = 0; i1 < perm.length; i1++) {
			final int i2 = RND.nextInt(perm.length);
			final int v1 = perm[i1];
			final int v2 = perm[i2];
			perm[i1] = v2;
			perm[i2] = v1;
		}

		for (int i = 0; i < perm.length; i++) {
			switch (perm[i]) {
				// GostBlockungsdatenManager - Schienen
				case 0 -> _schieneAdd();
				case 1 -> _schieneAddListe();
				case 2 -> _schieneGet();
				case 3 -> _schieneGetExists();
				case 4 -> _schieneGetListe();
				case 5 -> _schieneGetIsRemoveAllowed();
				case 6 -> _schienePatchBezeichnung();
				case 7 -> _schienePatchWochenstunden();
				case 8 -> _schieneRemoveByID();
				case 9 -> _schieneRemove();
				case 10 -> _schieneGetAnzahl();
				case 11 -> _schieneGetDefaultAnzahl();

				// GostBlockungsdatenManager - Kurse
				case 20 -> _kursAdd();
				case 21 -> _kursAddListe();
				case 22 -> _kursGetExistiert();
				case 23 -> _kursGetAnzahl();
				case 24 -> _kursGetName();
				case 25 -> _kursGetNameOhneSuffix();
				case 26 -> _kursGet();
				case 27 -> _kursGetLehrkraftMitNummer();
				case 28 -> _kursGetLehrkraftMitID();
				case 29 -> _kursGetLehrkraftMitNummerExists();
				case 30 -> _kursGetLehrkraftMitIDExists();
				case 31 -> _kursGetLehrkraefteSortiert();
				case 32 -> _kursAddLehrkraft();
				case 33 -> _kursRemoveLehrkraft();
				case 34 -> _kursGetListeSortiertNachFachKursartNummer();
				case 35 -> _kursGetListeSortiertNachKursartFachNummer();
				case 36 -> _kursGetListeByFachUndKursart();
				case 37 -> _kursGetIsRemoveAllowed();
				case 38 -> _kursGetIstVerbotenInSchiene();
				case 39 -> _kursGetHatSperrungInSchiene();
				case 40 -> _kursGetRegelGesperrtInSchiene();
				case 41 -> _kursGetHatFixierungInSchiene();
				case 42 -> _kursGetRegelFixierungInSchiene();
				case 43 -> _kursIstWeitereFixierungErlaubt();
				case 44 -> _kursGetRegelDummySchuelerOrNull();
				case 45 -> _kursmengeGetSetDerIDs();
				case 46 -> _kurseRemoveByID();
				case 47 -> _kursRemoveByID();
				case 48 -> _kursRemove();
				case 49 -> _kursMerge();
				case 50 -> _kurseRemove();
				case 51 -> _kursSetSuffix();


				// GostBlockungsdatenManager - Ergebnisse
				case 60 -> _ergebnisAdd();
				case 61 -> _ergebnisRemoveByID();
				case 64 -> _ergebnisGetAnzahl();

				// GostBlockungsdatenManager - Regeln
				case 90 -> _regelGetAnzahl();

				default -> {
					// ...
				}
			}
			// Konsistenzcheck aller Objekte
			_schieneGetListe();
		}

	}

	private static void assertEqualExceptions(final DeveloperNotificationException ex1, final DeveloperNotificationException ex2) {
		if ((ex1 == null) != (ex2 == null)) {
			System.out.println("EX1 = " + ex1);
			System.out.println("EX2 = " + ex2);
			if (ex1 != null)
				ex1.printStackTrace();
			if (ex2 != null)
				ex2.printStackTrace();
		}
		assertEquals(ex1 == null, ex2 == null);
	}

	private void _ergebnisAdd() {
		if (m1.ergebnisGetAnzahl() >= MAX_ERGEBNISSE)
			return;

		final @NotNull GostBlockungsergebnis[] e = createErgebnisDuplikateAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.ergebnisAdd(e[0]);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.ergebnisAdd(e[1]);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		_ergebnisGetAnzahl();
	}

	private void _ergebnisRemoveByID() {
		final @NotNull GostBlockungsergebnis[] e = createErgebnisDuplikateAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.ergebnisRemoveByID(e[0].id);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.ergebnisRemoveByID(e[1].id);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		_ergebnisGetAnzahl();
	}

	private void _ergebnisGetAnzahl() {
		final int v1 = m1.ergebnisGetAnzahl();
		final int v2 = m1.ergebnisGetAnzahl();
		assertEquals(v1, v2);
	}


	private void _schieneAdd() {
		if (m1.schieneGetAnzahl() >= MAX_SCHIENEN)
			return;
		final @NotNull GostBlockungSchiene[] s = createSchieneDuplikateAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.schieneAdd(s[0]);
			// Informiere alle E-Manager.
			for (final @NotNull GostBlockungsergebnisManager e : m1.ergebnisManagerGetListeUnsortiert())
				e.setAddSchieneByID(s[0].id);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.schieneAdd(s[1]);
			// Informiere alle E-Manager.
			for (final @NotNull DummyGostBlockungsergebnisManager e : m2.ergebnisManagerGetListeUnsortiert())
				e.setAddSchieneByID(s[0].id);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		_schieneGetAnzahl();
	}


	private void _schieneAddListe() {
		if (m1.schieneGetAnzahl() >= MAX_SCHIENEN)
			return;

		final @NotNull List<GostBlockungSchiene> list1 = new ArrayList<>();
		final @NotNull List<GostBlockungSchiene> list2 = new ArrayList<>();
		do {
			final @NotNull GostBlockungSchiene[] s = createSchieneDuplikateAuchUngueltig();
			list1.add(s[0]);
			list2.add(s[1]);
		} while (RND.nextBoolean());
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.schieneAddListe(list1);
			// Informiere alle E-Manager.
			for (final @NotNull GostBlockungsergebnisManager e1 : m1.ergebnisManagerGetListeUnsortiert())
				for (final GostBlockungSchiene schiene1 : list1)
					e1.setAddSchieneByID(schiene1.id);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.schieneAddListe(list2);
			// Informiere alle E-Manager.
			for (final @NotNull DummyGostBlockungsergebnisManager e2 : m2.ergebnisManagerGetListeUnsortiert())
				for (final GostBlockungSchiene schiene2 : list2)
					e2.setAddSchieneByID(schiene2.id);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		_schieneGetAnzahl();
	}

	private void _schieneGet() {
		final long idSchiene = getSchieneIdAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.schieneGet(idSchiene);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.schieneGet(idSchiene);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		if (ex1 == null)
			assertEquals(m1.schieneGet(idSchiene).id, m2.schieneGet(idSchiene).id);
	}

	private void _schieneGetExists() {
		final long idSchiene = getSchieneIdAuchUngueltig();
		final boolean b1 = m1.schieneGetExistiert(idSchiene);
		final boolean b2 = m2.schieneGetExistiert(idSchiene);
		assertEquals(b1, b2);
	}

	private void _schieneGetListe() {
		final @NotNull List<GostBlockungSchiene> list1 = m1.schieneGetListe();
		final @NotNull List<GostBlockungSchiene> list2 = m2.schieneGetListe();

		assertEquals(list1.size(), list2.size());
		for (int i = 0; i < list1.size(); i++)
			assertEquals(list1.get(i).id, list2.get(i).id);
	}


	private void _schieneGetIsRemoveAllowed() {
		final long idSchiene = getSchieneIdAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.schieneGetIsRemoveAllowed(idSchiene);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.schieneGetIsRemoveAllowed(idSchiene);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		if (ex1 == null)
			assertEquals(m1.schieneGetIsRemoveAllowed(idSchiene), m2.schieneGetIsRemoveAllowed(idSchiene));
	}

	private void _schienePatchBezeichnung() {
		final String sNeueBezeichnung = "Schiene " + RND.nextInt();
		final long idSchiene = getSchieneIdAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.schienePatchBezeichnung(idSchiene, sNeueBezeichnung);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.schienePatchBezeichnung(idSchiene, sNeueBezeichnung);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		if (ex1 == null)
			assertEquals(m1.schieneGet(idSchiene).bezeichnung, m2.schieneGet(idSchiene).bezeichnung);
	}

	private void _schienePatchWochenstunden() {
		final int sNeueWochenstunden = RND.nextInt(0, 5);
		final long idSchiene = getSchieneIdAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.schienePatchWochenstunden(idSchiene, sNeueWochenstunden);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.schienePatchWochenstunden(idSchiene, sNeueWochenstunden);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		if (ex1 == null)
			assertEquals(m1.schieneGet(idSchiene).wochenstunden, m2.schieneGet(idSchiene).wochenstunden);
	}

	private void _schieneRemoveByID() {
		final long idSchiene = getSchieneIdAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.schieneRemoveByID(idSchiene);
			// Informiere alle E-Manager.
			for (final @NotNull GostBlockungsergebnisManager e : m1.ergebnisManagerGetListeUnsortiert())
				e.setRemoveSchieneByID(idSchiene);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.schieneRemoveByID(idSchiene);
			// Informiere alle E-Manager.
			for (final @NotNull DummyGostBlockungsergebnisManager e : m2.ergebnisManagerGetListeUnsortiert())
				e.setRemoveSchieneByID(idSchiene);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		_schieneGetAnzahl();
		_regelGetAnzahl();
	}


	private void _schieneRemove() {
		final @NotNull GostBlockungSchiene[] s = createSchieneDuplikateAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.schieneRemove(s[0]);
			// Informiere alle E-Manager.
			for (final @NotNull GostBlockungsergebnisManager e : m1.ergebnisManagerGetListeUnsortiert())
				e.setRemoveSchieneByID(s[0].id);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.schieneRemove(s[1]);
			// Informiere alle E-Manager.
			for (final @NotNull DummyGostBlockungsergebnisManager e : m2.ergebnisManagerGetListeUnsortiert())
				e.setRemoveSchieneByID(s[1].id);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		_schieneGetAnzahl();
	}

	private void _schieneGetAnzahl() {
		final int v1 = m1.schieneGetAnzahl();
		final int v2 = m1.schieneGetAnzahl();
		assertEquals(v1, v2);

		// Auch E-Schienen testen.
		for (final @NotNull GostBlockungsergebnisManager e : m1.ergebnisManagerGetListeUnsortiert())
			assertEquals(v1, e.getAnzahlSchienen());
		for (final @NotNull DummyGostBlockungsergebnisManager e : m2.ergebnisManagerGetListeUnsortiert())
			assertEquals(v1, e.getAnzahlSchienen());
	}

	private void _schieneGetDefaultAnzahl() {
		final @NotNull GostHalbjahr halbjahr = getValidHalbjahr();
		final int v1 = GostBlockungsdatenManager.schieneGetDefaultAnzahl(halbjahr);
		final int v2 = DummyGostBlockungsdatenManager.schieneGetDefaultAnzahl(halbjahr);
		assertEquals(v1, v2);
	}


	private void _kursAdd() {
		if (m1.kursGetAnzahl() >= MAX_KURSE)
			return;

		final @NotNull GostBlockungKurs[] k = createKursDuplikateAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.kursAdd(k[0]);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.kursAdd(k[1]);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		_kursGetAnzahl();
	}

	private void _kursAddListe() {
		if (m1.kursGetAnzahl() >= MAX_KURSE)
			return;

		final @NotNull List<GostBlockungKurs> list1 = new ArrayList<>();
		final @NotNull List<GostBlockungKurs> list2 = new ArrayList<>();
		do {
			final @NotNull GostBlockungKurs[] k = createKursDuplikateAuchUngueltig();
			list1.add(k[0]);
			list2.add(k[1]);
		} while (RND.nextBoolean());

		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.kursAddListe(list1);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.kursAddListe(list2);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		_kursGetAnzahl();
	}

	private void _kursGetExistiert() {
		final long idKurs = getKursIdAuchUngueltig();
		final boolean b1 = m1.kursGetExistiert(idKurs);
		final boolean b2 = m2.kursGetExistiert(idKurs);
		assertEquals(b1, b2);
	}

	private void _kursGetAnzahl() {
		final int v1 = m1.kursGetAnzahl();
		final int v2 = m2.kursGetAnzahl();
		assertEquals(v1, v2);
	}

	private void _kursGetName() {
		final long idKurs = getKursIdAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.kursGetName(idKurs);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.kursGetName(idKurs);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		if (ex1 == null)
			assertEquals(m1.kursGetName(idKurs), m2.kursGetName(idKurs));
	}

	private void _kursGetNameOhneSuffix() {
		final long idKurs = getKursIdAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.kursGetNameOhneSuffix(idKurs);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.kursGetNameOhneSuffix(idKurs);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		if (ex1 == null)
			assertEquals(m1.kursGetNameOhneSuffix(idKurs), m2.kursGetNameOhneSuffix(idKurs));
	}

	private void _kursGet() {
		final long idKurs = getKursIdAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.kursGet(idKurs);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.kursGet(idKurs);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		if (ex1 == null)
			assertEquals(m1.kursGet(idKurs).id, m2.kursGet(idKurs).id);
	}

	private void _kursGetLehrkraftMitNummer() {
		final long idKurs = getKursIdAuchUngueltig();
		final int reihenfolgeNr = RND.nextInt(-2, 4);
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.kursGetLehrkraftMitNummer(idKurs, reihenfolgeNr);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.kursGetLehrkraftMitNummer(idKurs, reihenfolgeNr);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		if (ex1 == null)
			assertEquals(m1.kursGetLehrkraftMitNummer(idKurs, reihenfolgeNr).id, m2.kursGetLehrkraftMitNummer(idKurs, reihenfolgeNr).id);
	}

	private void _kursGetLehrkraftMitID() {
		final long idKurs = getKursIdAuchUngueltig();
		final long idKursLehrkraft = getLehrkraftIdAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.kursGetLehrkraftMitID(idKurs, idKursLehrkraft);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.kursGetLehrkraftMitID(idKurs, idKursLehrkraft);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		if (ex1 == null)
			assertEquals(m1.kursGetLehrkraftMitID(idKurs, idKursLehrkraft).id, m2.kursGetLehrkraftMitID(idKurs, idKursLehrkraft).id);
	}

	private void _kursGetLehrkraftMitNummerExists() {
		final long idKurs = getKursIdAuchUngueltig();
		final int reihenfolge = RND.nextInt(-2, 4);
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.kursGetLehrkraftMitNummerExists(idKurs, reihenfolge);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.kursGetLehrkraftMitNummerExists(idKurs, reihenfolge);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		if (ex1 == null)
			assertEquals(m1.kursGetLehrkraftMitNummerExists(idKurs, reihenfolge),
					m2.kursGetLehrkraftMitNummerExists(idKurs, reihenfolge));
	}

	private void _kursGetLehrkraftMitIDExists() {
		final long idKurs = getKursIdAuchUngueltig();
		final long idLehrkraft = getLehrkraftIdAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.kursGetLehrkraftMitIDExists(idKurs, idLehrkraft);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.kursGetLehrkraftMitIDExists(idKurs, idLehrkraft);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		if (ex1 == null)
			assertEquals(m1.kursGetLehrkraftMitIDExists(idKurs, idLehrkraft),
					m2.kursGetLehrkraftMitIDExists(idKurs, idLehrkraft));
	}

	private void _kursGetLehrkraefteSortiert() {
		final long idKurs = getKursIdAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.kursGetLehrkraefteSortiert(idKurs);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.kursGetLehrkraefteSortiert(idKurs);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		if (ex1 == null) {
			final @NotNull List<GostBlockungKursLehrer> list1 = m1.kursGetLehrkraefteSortiert(idKurs);
			final @NotNull List<GostBlockungKursLehrer> list2 = m2.kursGetLehrkraefteSortiert(idKurs);
			assertEquals(list1.size(), list2.size());
			for (int i = 0; i < list1.size(); i++)
				assertEquals(list1.get(i).id, list2.get(i).id);
		}
	}

	private void _kursAddLehrkraft() {
		final long idKurs = getKursIdAuchUngueltig();
		final @NotNull GostBlockungKursLehrer[] l = createKurslehrerDuplikateAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			if (m1.kursGet(idKurs).lehrer.size() >= MAX_KURS_LEHRER)
				return;
			m1.kursAddLehrkraft(idKurs, l[0]);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.kursAddLehrkraft(idKurs, l[1]);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		if (ex1 == null)
			assertEquals(m1.kursGet(idKurs).lehrer.size(), m2.kursGet(idKurs).lehrer.size());
	}

	private void _kursRemoveLehrkraft() {
		final long idKurs = getKursIdAuchUngueltig();
		final long idLehrkraft = getLehrkraftIdAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.kursRemoveLehrkraft(idKurs, idLehrkraft);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.kursRemoveLehrkraft(idKurs, idLehrkraft);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		if (ex1 == null)
			assertEquals(m1.kursGet(idKurs).lehrer.size(), m2.kursGet(idKurs).lehrer.size());
	}

	private void _kursGetListeSortiertNachFachKursartNummer() {
		final @NotNull List<GostBlockungKurs> list1 = m1.kursGetListeSortiertNachFachKursartNummer();
		final @NotNull List<GostBlockungKurs> list2 = m2.kursGetListeSortiertNachFachKursartNummer();

		assertEquals(list1.size(), list2.size());
		for (int i = 0; i < list1.size(); i++)
			assertEquals(list1.get(i).id, list2.get(i).id);
	}

	private void _kursGetListeSortiertNachKursartFachNummer() {
		final @NotNull List<GostBlockungKurs> list1 = m1.kursGetListeSortiertNachKursartFachNummer();
		final @NotNull List<GostBlockungKurs> list2 = m2.kursGetListeSortiertNachKursartFachNummer();

		assertEquals(list1.size(), list2.size());
		for (int i = 0; i < list1.size(); i++)
			assertEquals(list1.get(i).id, list2.get(i).id);
	}

	private void _kursGetListeByFachUndKursart() {
		final long idFach = getFachIdAuchUngueltig();
		final int idKursart = getKursartAuchUngueltig();
		final @NotNull List<GostBlockungKurs> list1 = m1.kursGetListeByFachUndKursart(idFach, idKursart);
		final @NotNull List<GostBlockungKurs> list2 = m2.kursGetListeByFachUndKursart(idFach, idKursart);

		assertEquals(list1.size(), list2.size());
		for (int i = 0; i < list1.size(); i++)
			assertEquals(list1.get(i).id, list2.get(i).id);
	}

	private void _kursGetIsRemoveAllowed() {
		final long idKurs = getKursartAuchUngueltig();
		final boolean b1 = m1.kursGetIsRemoveAllowed(idKurs);
		final boolean b2 = m2.kursGetIsRemoveAllowed(idKurs);
		assertEquals(b1, b2);
	}

	private void _kursGetIstVerbotenInSchiene() {
		final long idKurs = getKursIdAuchUngueltig();
		final long idSchiene = getSchieneIdAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.kursGetIstVerbotenInSchiene(idKurs, idSchiene);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.kursGetIstVerbotenInSchiene(idKurs, idSchiene);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
	}


	private void _kursGetHatSperrungInSchiene() {
		final long idKurs = getKursIdAuchUngueltig();
		final long idSchiene = getSchieneIdAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.kursGetHatSperrungInSchiene(idKurs, idSchiene);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.kursGetHatSperrungInSchiene(idKurs, idSchiene);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
	}


	private void _kursGetRegelGesperrtInSchiene() {
		final long idKurs = getKursIdAuchUngueltig();
		final long idSchiene = getSchieneIdAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.kursGetRegelGesperrtInSchiene(idKurs, idSchiene);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.kursGetRegelGesperrtInSchiene(idKurs, idSchiene);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		if (ex1 == null)
			assertEquals(m1.kursGetRegelGesperrtInSchiene(idKurs, idSchiene).id,
					m2.kursGetRegelGesperrtInSchiene(idKurs, idSchiene).id);
	}

	private void _kursGetHatFixierungInSchiene() {
		final long idKurs = getKursIdAuchUngueltig();
		final long idSchiene = getSchieneIdAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.kursGetHatFixierungInSchiene(idKurs, idSchiene);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.kursGetHatFixierungInSchiene(idKurs, idSchiene);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
	}

	private void _kursGetRegelFixierungInSchiene() {
		final long idKurs = getKursIdAuchUngueltig();
		final long idSchiene = getSchieneIdAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.kursGetRegelFixierungInSchiene(idKurs, idSchiene);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.kursGetRegelFixierungInSchiene(idKurs, idSchiene);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		if (ex1 == null)
			assertEquals(m1.kursGetRegelGesperrtInSchiene(idKurs, idSchiene).id,
					m2.kursGetRegelGesperrtInSchiene(idKurs, idSchiene).id);
	}

	private void _kursIstWeitereFixierungErlaubt() {
		final long idKurs = getKursIdAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.kursIstWeitereFixierungErlaubt(idKurs);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.kursIstWeitereFixierungErlaubt(idKurs);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
	}

	private void _kursGetRegelDummySchuelerOrNull() {
		final long idKurs = getKursIdAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.kursGetRegelDummySchuelerOrNull(idKurs);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.kursGetRegelDummySchuelerOrNull(idKurs);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		if (ex1 == null) {
			assertEquals(m1.kursGetRegelDummySchuelerOrNull(idKurs) == null,
					m2.kursGetRegelDummySchuelerOrNull(idKurs) == null);
			if (m1.kursGetRegelDummySchuelerOrNull(idKurs) != null)
				assertEquals(m1.kursGetRegelDummySchuelerOrNull(idKurs).id,
						m2.kursGetRegelDummySchuelerOrNull(idKurs).id);
		}

	}

	private void _kursmengeGetSetDerIDs() {
		final @NotNull Set<Long> set1 = m1.kursmengeGetSetDerIDs();
		final @NotNull Set<Long> set2 = m2.kursmengeGetSetDerIDs();

		assertEquals(set1.size(), set2.size());
		for (final long id1 : set1)
			assertEquals(true, set2.contains(id1));
	}

	private void _kurseRemoveByID() {
		final @NotNull Set<Long> set1 = new HashSet<>();
		final @NotNull Set<Long> set2 = new HashSet<>();
		do {
			final long idKurs = getKursIdAuchUngueltig();
			set1.add(idKurs);
			set2.add(idKurs);
		} while (RND.nextBoolean());

		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.kurseRemoveByID(set1);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.kurseRemoveByID(set2);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		_kursGetAnzahl();
		_regelGetAnzahl();
	}

	private void _kursRemoveByID() {
		final long idKurs = getKursIdAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.kursRemoveByID(idKurs);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.kursRemoveByID(idKurs);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		_kursGetAnzahl();
		_regelGetAnzahl();
	}

	private void _kursRemove() {
		final @NotNull GostBlockungKurs[] k = createKursDuplikateAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.kursRemove(k[0]);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.kursRemove(k[1]);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		_kursGetAnzahl();
		_regelGetAnzahl();
	}

	private void _kurseRemove() {
		final @NotNull List<GostBlockungKurs> list1 = new ArrayList<>();
		final @NotNull List<GostBlockungKurs> list2 = new ArrayList<>();
		do {
			final @NotNull GostBlockungKurs[] k = createKursDuplikateAuchUngueltig();
			list1.add(k[0]);
			list2.add(k[1]);
		} while (RND.nextBoolean());

		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.kurseRemove(list1);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.kurseRemove(list2);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		_kursGetAnzahl();
		_regelGetAnzahl();
	}

	private void _kursSetSuffix() {
		final String sNeuerSuffix = "Suffix " + RND.nextInt();
		final long idKurs = getKursIdAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.kursSetSuffix(idKurs, sNeuerSuffix);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.kursSetSuffix(idKurs, sNeuerSuffix);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		if (ex1 == null)
			assertEquals(m1.kursGet(idKurs).suffix, m2.kursGet(idKurs).suffix);
	}

	private void _kursMerge() {
		final long idKursID1keep = getKursIdAuchUngueltig();
		final long idKursID2delete = getKursIdAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.kursMerge(idKursID1keep, idKursID2delete);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.kursMerge(idKursID1keep, idKursID2delete);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		_kursGetAnzahl();
		_regelGetAnzahl();
	}

	private void _regelGetAnzahl() {
		final int v1 = m1.regelGetAnzahl();
		final int v2 = m2.regelGetAnzahl();
		assertEquals(v1, v2);
	}

	private @NotNull GostBlockungKurs[] createKursDuplikateAuchUngueltig() {
		final @NotNull GostBlockungKurs kurs1 = new GostBlockungKurs();
		kurs1.id = getKursIdAuchUngueltig();
		kurs1.fach_id = getFachIdAuchUngueltig();
		kurs1.kursart = getKursartAuchUngueltig();
		kurs1.nummer = RND.nextInt(0, 5);
		kurs1.istKoopKurs = RND.nextBoolean();
		kurs1.suffix = RND.nextBoolean() ? "" : "#" + RND.nextInt(0, 999) + "#";
		kurs1.wochenstunden = RND.nextBoolean() ? 3 : RND.nextInt(0, 5);
		kurs1.anzahlSchienen = RND.nextBoolean() ? 1 : RND.nextInt(0, 5);
		kurs1.lehrer = new ArrayList<>(); // werden später hinzugefügt

		final @NotNull GostBlockungKurs kurs2 = new GostBlockungKurs();
		kurs2.id = kurs1.id;
		kurs2.fach_id = kurs1.fach_id;
		kurs2.kursart = kurs1.kursart;
		kurs2.nummer = kurs1.nummer;
		kurs2.istKoopKurs = kurs1.istKoopKurs;
		kurs2.suffix = kurs1.suffix;
		kurs2.wochenstunden = kurs1.wochenstunden;
		kurs2.anzahlSchienen = kurs1.anzahlSchienen;
		kurs2.lehrer = new ArrayList<>(); // werden später hinzugefügt

		return new GostBlockungKurs[] { kurs1, kurs2 };
	}

	private @NotNull GostBlockungKursLehrer[] createKurslehrerDuplikateAuchUngueltig() {
		final @NotNull GostBlockungKursLehrer lehrer1 = new GostBlockungKursLehrer();
		lehrer1.id = getLehrkraftIdAuchUngueltig();
		lehrer1.vorname = "V" + lehrer1.id;
		lehrer1.nachname = "N" + lehrer1.id;
		lehrer1.reihenfolge = RND.nextInt(-2, 4);
		lehrer1.wochenstunden = RND.nextInt(-1, 5);
		lehrer1.istExtern = RND.nextBoolean();

		final @NotNull GostBlockungKursLehrer lehrer2 = new GostBlockungKursLehrer();
		lehrer2.id = lehrer1.id;
		lehrer2.vorname = lehrer1.vorname;
		lehrer2.nachname = lehrer1.nachname;
		lehrer2.reihenfolge = lehrer1.reihenfolge;
		lehrer2.wochenstunden = lehrer1.wochenstunden;
		lehrer2.istExtern = lehrer1.istExtern;

		return new GostBlockungKursLehrer[] { lehrer1, lehrer2 };
	}

	private @NotNull GostBlockungSchiene[] createSchieneDuplikateAuchUngueltig() {
		final @NotNull GostBlockungSchiene schiene1 = new GostBlockungSchiene();
		schiene1.id = getSchieneIdAuchUngueltig();
		schiene1.nummer = getSchieneNrAuchUngueltig();
		schiene1.bezeichnung = "Nr. " + schiene1.nummer;
		schiene1.wochenstunden = RND.nextInt(0, 5);

		final @NotNull GostBlockungSchiene schiene2 = new GostBlockungSchiene();
		schiene2.id = schiene1.id;
		schiene2.nummer = schiene1.nummer;
		schiene2.bezeichnung = schiene1.bezeichnung;
		schiene2.wochenstunden = schiene1.wochenstunden;

		return new GostBlockungSchiene[] { schiene1, schiene2 };
	}

	private @NotNull GostBlockungsergebnis[] createErgebnisDuplikateAuchUngueltig() {
		final @NotNull GostHalbjahr halbjahr = getValidHalbjahr();

		final @NotNull GostBlockungsergebnis ergebnis1 = new GostBlockungsergebnis();
		ergebnis1.id = getErgebnisIdAuchUngueltig();
		ergebnis1.blockungID = getBlockungIdAuchUngueltig();
		ergebnis1.name = "";
		ergebnis1.gostHalbjahr = halbjahr.id;
		ergebnis1.istAktiv = RND.nextBoolean();

		final @NotNull GostBlockungsergebnis ergebnis2 = new GostBlockungsergebnis();
		ergebnis2.id = ergebnis1.id;
		ergebnis2.blockungID = ergebnis1.blockungID;
		ergebnis2.name = ergebnis1.name;
		ergebnis2.gostHalbjahr = ergebnis1.gostHalbjahr;
		ergebnis2.istAktiv = ergebnis1.istAktiv;

		return new GostBlockungsergebnis[] { ergebnis1, ergebnis2 };
	}

	private void createFaecher() {
		for (int i = 0; i < Fach.values().length; i++) {
			final Fach f = Fach.values()[i];
			final @NotNull GostFach fach = new GostFach();
			fach.id = i + 1;
			fach.kuerzel = f.name();
			FM.add(fach);
		}
	}

	private long getBlockungIdAuchUngueltig() {
		return RND.nextBoolean() ? 777 : 888;
	}

	private long getErgebnisIdAuchUngueltig() {
		return RND.nextLong(-MAX_ERGEBNISSE / 2, MAX_ERGEBNISSE + MAX_ERGEBNISSE / 2);
	}

	private long getLehrkraftIdAuchUngueltig() {
		return RND.nextLong(-MAX_LEHRER / 2, MAX_LEHRER + MAX_LEHRER / 2);
	}

	private long getSchieneIdAuchUngueltig() {
		return RND.nextLong(-MAX_SCHIENEN / 2, MAX_SCHIENEN + MAX_SCHIENEN / 2);
	}

	private int getSchieneNrAuchUngueltig() {
		return RND.nextInt(-MAX_SCHIENEN / 2, MAX_SCHIENEN + MAX_SCHIENEN / 2);
	}

	private long getKursIdAuchUngueltig() {
		return RND.nextLong(-MAX_KURSE / 2, MAX_KURSE + MAX_KURSE / 2);
	}

	private long getFachIdAuchUngueltig() {
		return RND.nextLong(FM.faecher().size() * 2 + 2);
	}

	private int getKursartAuchUngueltig() {
		return RND.nextInt(GostKursart.values().length * 2 + 2);
	}

	private @NotNull GostFach getValidFach() {
		final List<GostFach> menge = FM.faecher();
		return menge.get(RND.nextInt(menge.size()));
	}

	private @NotNull GostKursart getValidKursArt() {
		final GostKursart[] menge = GostKursart.values();
		return menge[RND.nextInt(menge.length)];
	}

	private @NotNull GostHalbjahr getValidHalbjahr() {
		final @NotNull GostHalbjahr[] halbjahre = GostHalbjahr.values();
		final @NotNull GostHalbjahr halbjahr = halbjahre[RND.nextInt(halbjahre.length)];
		return halbjahr;
	}
}
