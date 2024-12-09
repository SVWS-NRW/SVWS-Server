package de.svws_nrw.core.kursblockung.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

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

import de.svws_nrw.asd.data.CoreTypeException;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.adt.LongArrayKey;
import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostBlockungKursLehrer;
import de.svws_nrw.core.data.gost.GostBlockungRegel;
import de.svws_nrw.core.data.gost.GostBlockungSchiene;
import de.svws_nrw.core.data.gost.GostBlockungsdaten;
import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostFachwahl;
import de.svws_nrw.core.data.schueler.Schueler;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.exceptions.UserNotificationException;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelTyp;
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
												    //      10 000 ~    10 s     (84,6 %)
	private static final int RUNDEN = 1000;         //     100 000 ~    70 s     (89,5 %)
	private static final int SCHULJAHR = 2024;      //   1 000 000 ~   664 s     (91,9 %)
                                                    //  10 000 000 ~  6600 s     (94,0 %)
													// 100 000 000 ~ ????? s

	private final Random RND = new Random(1);
	private final GostFaecherManager FM = new GostFaecherManager(SCHULJAHR);
	private final GostBlockungsdatenManager m1 = new GostBlockungsdatenManager(new GostBlockungsdaten(), FM);
	private final DummyGostBlockungsdatenManager m2 = new DummyGostBlockungsdatenManager(new GostBlockungsdaten(), FM);

	// Grenzen für die Anzahl bestimmer Objekte während der randomisierten Erzeugung.
	private static final int MAX_SCHIENEN = 5;
	private static final int MAX_KURSE = 5;
	private static final int MAX_LEHRER = 5;
	private static final int MAX_SCHUELER = 5;
	private static final int MAX_FACHWAHLEN = 5 * MAX_SCHUELER;
	private static final int MAX_KURS_LEHRER = 3;
	private static final int MAX_ERGEBNISSE = 2;
	private static final int MAX_REGELN = 5;

	private static boolean schieneAdd = true;
	private static boolean kursAdd = true;
	private static boolean regelAdd = true;

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
			testeEinePermutation(i);
			/*
			if ((i == 1) || (i == 10) || (i == 100) || (i == 1000) || (i == 10000) || (i == 100000))
				System.out.println("Runde " + i
						+ ": Ergebnisse " + m1.ergebnisGetAnzahl()
						+ ", Schienen " + m1.schieneGetAnzahl()
						+ ", Schüler " + m1.schuelerGetAnzahl()
						+ ", Kurse " + m1.kursGetAnzahl()
						+ ", Fachwahlen " + m1.fachwahlGetAnzahl()
						+ ", Regeln " + m1.regelGetAnzahl()
				);
			 */
		}
	}

	private void testeEinePermutation(final int runde) {
		// Erzeuge eine zufällige Permutation der Methoden-Aufrufe.
		final int[] perm = new int[130];
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

			String debugBefore = m1.getDebugString();

			switch (perm[i]) {
				// GostBlockungsdatenManager - Schienen (fertig)
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

				// GostBlockungsdatenManager - Kurse (fertig)
				case 12 -> _kursAdd();
				case 13 -> _kursAddListe();
				case 14 -> _kursGetExistiert();
				case 15 -> _kursGetAnzahl();
				case 16 -> _kursGetName();
				case 17 -> _kursGetNameOhneSuffix();
				case 18 -> _kursGet();
				case 19 -> _kursGetLehrkraftMitNummer();
				case 20 -> _kursGetLehrkraftMitID();
				case 21 -> _kursGetLehrkraftMitNummerExists();
				case 22 -> _kursGetLehrkraftMitIDExists();
				case 23 -> _kursGetLehrkraefteSortiert();
				case 24 -> _kursAddLehrkraft();
				case 25 -> _kursRemoveLehrkraft();
				case 26 -> _kursGetListeSortiertNachFachKursartNummer();
				case 27 -> _kursGetListeSortiertNachKursartFachNummer();
				case 28 -> _kursGetListeByFachUndKursart();
				case 29 -> _kursGetIsRemoveAllowed();
				case 30 -> _kursGetIstVerbotenInSchiene();
				case 31 -> _kursGetHatSperrungInSchiene();
				case 32 -> _kursGetRegelGesperrtInSchiene();
				case 33 -> _kursGetHatFixierungInSchiene();
				case 34 -> _kursGetRegelFixierungInSchiene();
				case 35 -> _kursIstWeitereFixierungErlaubt();
				case 36 -> _kursGetRegelDummySchuelerOrNull();
				case 37 -> _kursmengeGetSetDerIDs();
				case 38 -> _kurseRemoveByID();
				case 39 -> _kursRemoveByID();
				case 40 -> _kursRemove();
				case 41 -> _kursMerge();
				case 42 -> _kurseRemove();
				case 43 -> _kursSetSuffix();

				// GostBlockungsdatenManager - Ergebnisse
				case 44 -> _ergebnisAdd();
				case 45 -> _ergebnisAddListe();
				case 46 -> _ergebnisGet();
				case 47 -> _ergebnisManagerGet();
				case 48 -> _ergebnisManagerExists();
				case 49 -> _ergebnisManagerGetListeUnsortiert();
				case 50 -> _ergebnisGetListeSortiert(); // nach Bewertung und nach ID
				case 51 -> _ergebnisRemoveListeByIDs();
				case 52 -> _ergebnisRemoveListe();
				case 53 -> _ergebnisRemoveByID();
				case 54 -> _ergebnisRemove();
				case 55 -> _ergebnisUpdateBewertung();
				case 56 -> _ergebnisAlleRevalidieren();
				case 57 -> _ergebnisGetAnzahl();
				case 58 -> _ergebnisGetBewertung1Wert();
				case 59 -> _ergebnisGetBewertung1Intervall();
				case 60 -> _ergebnisGetBewertung2Wert();
				case 61 -> _ergebnisGetBewertung2Intervall();
				case 62 -> _ergebnisGetBewertung3Wert();
				case 63 -> _ergebnisGetBewertung3Intervall();
				case 64 -> _ergebnisGetBewertung4Wert();
				case 65 -> _ergebnisGetBewertung4Intervall();

				// GostBlockungsdatenManager - Schüler
				case 66 -> _schuelerAdd();
				case 67 -> _schuelerAddListe();
				case 68 -> _schuelerGetAnzahlMitMindestensEinerFachwahl();
				case 69 -> _schuelerGetAnzahl();
				case 70 -> _schuelerGet();
				case 71 -> _schuelerGetOrNull();
				case 72 -> _schuelerGetListe();
				case 73 -> _schuelerGetOfFachKursart();
				case 74 -> _schuelerGetOfFachFachwahl();
				case 75 -> _schuelerGetOfFachFachwahlOrNull();
				case 76 -> _schuelerGetHatFach();
				case 77 -> _schuelerGetHatDieSelbeKursartMitSchuelerInFach();
				case 78 -> _schuelerGetHatFachart();
				case 79 -> _schuelerGetListeOfFachwahlen();
				case 80 -> _schuelerGetFachListeGemeinsamerFacharten();
				case 81 -> _schuelerGetIstVerbotenInKurs();
				case 82 -> _schuelerGetRegelVerbotenInKurs();
				case 83 -> _schuelerGetIstFixiertInKurs();
				case 84 -> _schuelerGetRegelFixiertInKurs();

				// GostBlockungsdatenManager - Fach / Fachwahl
				case 85 -> _fachGetMengeKursarten();
				case 86 -> _fachwahlAdd();
				case 87 -> _fachwahlAddListe();
				case 88 -> _fachwahlGetAnzahl();
				case 89 -> _fachwahlGetName();
				case 90 -> _fachwahlGetListeOfFachart();
				case 91 -> _fachwahlGetAnzahlVerwendeterKursarten();

				// GostBlockungsdatenManager - Regeln
				case 92 -> _regelAdd();
				case 93 -> _regelAddListe();
				case 94 -> _regelGetAnzahl();
				case 95 -> _regelGet();
				case 96 -> _regelGetByLongArrayKeyOrNull();
				case 97 -> _regelGetListe();
				case 98 -> _regelGetListeOfTyp();
				case 99 -> _regelGetRegelOrDummyKursGesperrtInSchiene();
				case 100 -> _regelGetRegelOrDummyKursFixierungInSchiene();
				case 101 -> _regelGetRegelOrDummySchuelerInKursFixierung();
				case 102 -> _regelGetExistiert();
				case 103 -> _regelGetIsRemoveAllowed();
				case 104 -> _regelRemoveByID();
				case 105 -> _regelRemoveListe();
				case 106 -> _regelRemoveListeByIDs();
				case 107 -> _regelRemove();

				// Weiteres
				case 108 -> _toStringKursSimple();
				case 109 -> _getMaxTimeMillis();
				case 110 -> _setMaxTimeMillis();
				case 111 -> _getName();
				case 112 -> _setName();
				case 113 -> _getHalbjahr();
				case 114 -> _setHalbjahr();
				case 115 -> _getFaecherAnzahl();
				case 116 -> _getDebugString();
				case 117 -> _spezialfall01();
			}

			if (!m1.testMultimap()) {
				System.out.println("################## RUNDE " + runde + " ##############################");
				System.out.println("MultiMap passt nicht mehr!");
				System.out.println();
				System.out.println();
				System.out.println("Called perm[i] = " + perm[i]);
				System.out.println();
				System.out.println();
				System.out.println("Debug vorher:");
				System.out.println(debugBefore);
				System.out.println();
				System.out.println();
				System.out.println("Debug nachher:");
				System.out.println(m1.getDebugString());
				fail("In Runde " + runde + " passte die MultiMap nicht mehr!");
			}

			// Add <--> Remove wechsel
			schieneAdd = RND.nextInt(MAX_SCHIENEN + 1) >= m1.schieneGetAnzahl();
			kursAdd = RND.nextInt(MAX_KURSE + 1) >= m1.kursGetAnzahl();
			regelAdd = RND.nextInt(MAX_REGELN + 1) >= m1.regelGetAnzahl();


			// Mengen vergleichen
			_schieneGetListe();
			_kursGetListeSortiertNachFachKursartNummer();
			_regelGetListe();

		}

	}

	private void _spezialfall01() {
		// int typ = GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ
		// Sperre Kurs 6 in Schiene 1
		// Sperre Kurs 6 in Schiene 2
		// Lösche Schiene 1.
		// Kann zu einem Fehler führen.
		// Im Prozess wird "Sperre Kurs 6 in Schiene 2" umgeändert zu "Sperre Kurs 6 in Schiene 1"
		// Dann existiert aber kurzzeitig  "Sperre Kurs 6 in Schiene 1" doppelt, was die Maps zerstört.
	}

	private void _toStringKursSimple() {
		final long idKurs = getKursIdAuchUngueltig();
		final String s1 = m1.toStringKursSimple(idKurs);
		final String s2 = m2.toStringKursSimple(idKurs);
		assertEquals(s1, s2);
	}

	private void _getMaxTimeMillis() {
		final long l1 = m1.getMaxTimeMillis();
		final long l2 = m2.getMaxTimeMillis();
		assertEquals(l1, l2);
	}

	private void _setMaxTimeMillis() {
		final long value = RND.nextLong(-100, 5000);

		Exception ex1 = null;
		Exception ex2 = null;

		try {
			m1.setMaxTimeMillis(value);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.setMaxTimeMillis(value);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		_getMaxTimeMillis();
	}

	private void _getName() {
		final String s1 = m1.getName();
		final String s2 = m2.getName();
		assertEquals(s1, s2);
	}

	private void _setName() {
		final String value = RND.nextBoolean() ? null : (RND.nextBoolean() ? "" : ("Blockung" + RND.nextInt(100)));

		Exception ex1 = null;
		Exception ex2 = null;

		try {
			m1.setName(value);
		} catch (final UserNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.setName(value);
		} catch (final UserNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		_getName();
	}

	private void _getHalbjahr() {
		final @NotNull GostHalbjahr v1 = m1.getHalbjahr();
		final @NotNull GostHalbjahr v2 = m2.getHalbjahr();
		assertEqualHalbjahr(v1, v2);
	}

	private void _setHalbjahr() {
		final GostHalbjahr h = getValidHalbjahr();
		m1.setHalbjahr(h);
		m2.setHalbjahr(h);
		_getHalbjahr();
	}

	private void _getFaecherAnzahl() {
		final int v1 = m1.getFaecherAnzahl();
		final int v2 = m2.getFaecherAnzahl();
		assertEquals(v1, v2);
	}

	private void _getDebugString() {
		final String s1 = m1.getDebugString();
		final String s2 = m2.getDebugString();
		assertEquals(s1, s2);
	}

	private void _regelAdd() {
		if (!regelAdd)
			return;
		if (m1.regelGetAnzahl() >= MAX_REGELN)
			return;

		final @NotNull GostBlockungRegel[] r = createRegelDuplikateAuchUngueltig();
		Exception ex1 = null;
		Exception ex2 = null;

		try {
			m1.regelAdd(r[0]);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.regelAdd(r[1]);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		_regelGetAnzahl();
	}

	private void _regelAddListe() {
		if (!regelAdd)
			return;
		if (m1.regelGetAnzahl() >= MAX_REGELN)
			return;

		final @NotNull List<GostBlockungRegel> list1 = new ArrayList<>();
		final @NotNull List<GostBlockungRegel> list2 = new ArrayList<>();
		do {
			final @NotNull GostBlockungRegel[] a = createRegelDuplikateAuchUngueltig();
			list1.add(a[0]);
			list2.add(a[1]);
		} while (RND.nextBoolean());


		Exception ex1 = null;
		Exception ex2 = null;
		try {
			m1.regelAddListe(list1);
		} catch (final DeveloperNotificationException | CoreTypeException ex) {
			ex1 = ex;
		}

		try {
			m2.regelAddListe(list2);
		} catch (final DeveloperNotificationException | CoreTypeException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		_regelGetAnzahl();
	}

	private void _regelGetAnzahl() {
		final int v1 = m1.regelGetAnzahl();
		final int v2 = m2.regelGetAnzahl();
		assertEquals(v1, v2);

		// Vergleiche auch die Elemente.
		final @NotNull List<GostBlockungRegel> list1 = m1.regelGetListe();
		final @NotNull List<GostBlockungRegel> list2 = m2.regelGetListe();
		assertEquals(list1.size(), list2.size());
		for (int i = 0; i < list1.size(); i++)
			assertEqualRegel(list1.get(i), list2.get(i));
	}


	private void _regelGet() {
		final long idRegel = getRegelIdAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.regelGet(idRegel);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.regelGet(idRegel);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		if (ex1 == null)
			assertEquals(m1.regelGet(idRegel).id, m2.regelGet(idRegel).id);
	}

	private void _regelGetByLongArrayKeyOrNull() {
		final @NotNull GostBlockungRegel[] r = createRegelDuplikateAuchUngueltig();
		final @NotNull LongArrayKey key1 = _regelToMultiKey(r[0]);
		final @NotNull LongArrayKey key2 = _regelToMultiKey(r[1]);

		if (m1.regelGetByLongArrayKeyOrNull(key1) == null)
			assertEquals(null, m2.regelGetByLongArrayKeyOrNull(key2));
		else
			assertEquals(m1.regelGetByLongArrayKeyOrNull(key1).id,
					m2.regelGetByLongArrayKeyOrNull(key2).id);
	}

	private static @NotNull LongArrayKey _regelToMultiKey(final @NotNull GostBlockungRegel regel) {
		long[] a = new long[regel.parameter.size() + 1];
		a[0] = regel.typ;
		for (int i = 1; i < a.length; i++)
			a[i] = regel.parameter.get(i - 1);
		return new LongArrayKey(a);
	}

	private void _regelGetListe() {
		final @NotNull List<GostBlockungRegel> list1 = m1.regelGetListe();
		final @NotNull List<GostBlockungRegel> list2 = m2.regelGetListe();

		assertEquals(list1.size(), list2.size());
		for (int i = 0; i < list1.size(); i++)
			assertEqualRegel(list1.get(i), list2.get(i));
	}

	private void _regelGetListeOfTyp() {
		final int typ = getRegelTypAuchUngueltig(0.1);
		final @NotNull GostKursblockungRegelTyp gTyp = GostKursblockungRegelTyp.fromTyp(typ);

		final @NotNull List<GostBlockungRegel> list1 = m1.regelGetListeOfTyp(gTyp);
		final @NotNull List<GostBlockungRegel> list2 = m2.regelGetListeOfTyp(gTyp);

		assertEquals(list1.size(), list2.size());
		for (int i = 0; i < list1.size(); i++)
			assertEqualRegel(list1.get(i), list2.get(i));
	}

	private void _regelGetRegelOrDummyKursGesperrtInSchiene() {
		final long idKurs = getKursIdAuchUngueltig();
		final int nrSchiene = getSchieneNrAuchUngueltig();

		assertEqualRegel(m1.regelGetRegelOrDummyKursGesperrtInSchiene(idKurs, nrSchiene),
				m2.regelGetRegelOrDummyKursGesperrtInSchiene(idKurs, nrSchiene));
	}

	private void _regelGetRegelOrDummyKursFixierungInSchiene() {
		final long idKurs = getKursIdAuchUngueltig();
		final int nrSchiene = getSchieneNrAuchUngueltig();

		assertEqualRegel(m1.regelGetRegelOrDummyKursFixierungInSchiene(idKurs, nrSchiene),
				m2.regelGetRegelOrDummyKursFixierungInSchiene(idKurs, nrSchiene));
	}

	private void _regelGetRegelOrDummySchuelerInKursFixierung() {
		final long idSchueler = getSchuelerIdAuchUngueltig();
		final long idKurs = getKursIdAuchUngueltig();

		assertEqualRegel(m1.regelGetRegelOrDummySchuelerInKursFixierung(idSchueler, idKurs),
				m2.regelGetRegelOrDummySchuelerInKursFixierung(idSchueler, idKurs));
	}

	private void _regelGetExistiert() {
		final long idRegel = getRegelIdAuchUngueltig();
		final boolean b1 = m1.regelGetExistiert(idRegel);
		final boolean b2 = m2.regelGetExistiert(idRegel);
		assertEquals(b1, b2);
	}

	private void _regelGetIsRemoveAllowed() {
		final long idRegel = getRegelIdAuchUngueltig();
		final boolean b1 = m1.regelGetIsRemoveAllowed(idRegel);
		final boolean b2 = m2.regelGetIsRemoveAllowed(idRegel);
		assertEquals(b1, b2);
	}

	private void _regelRemoveByID() {
		if (regelAdd)
			return;

		final @NotNull GostBlockungRegel[] a = createRegelDuplikateAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.regelRemoveByID(a[0].id);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.regelRemoveByID(a[1].id);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		_regelGetAnzahl();
	}

	private void _regelRemoveListe() {
		if (regelAdd)
			return;

		final @NotNull List<GostBlockungRegel> list1 = new ArrayList<>();
		final @NotNull List<GostBlockungRegel> list2 = new ArrayList<>();
		do {
			final @NotNull GostBlockungRegel[] a = createRegelDuplikateAuchUngueltig();
			list1.add(a[0]);
			list2.add(a[1]);
		} while (RND.nextBoolean());

		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.regelRemoveListe(list1);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.regelRemoveListe(list2);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		_regelGetAnzahl();
	}

	private void _regelRemoveListeByIDs() {
		if (regelAdd)
			return;

		final @NotNull Set<Long> set1 = new HashSet<>();
		final @NotNull Set<Long> set2 = new HashSet<>();
		do {
			final long idRegel = getRegelIdAuchUngueltig();
			set1.add(idRegel);
			set2.add(idRegel);
		} while (RND.nextBoolean());

		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.regelRemoveListeByIDs(set1);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.regelRemoveListeByIDs(set2);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		_regelGetAnzahl();
	}

	private void _regelRemove() {
		if (regelAdd)
			return;

		final @NotNull GostBlockungRegel[] a = createRegelDuplikateAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.regelRemove(a[0]);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.regelRemove(a[1]);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		_regelGetAnzahl();
	}

	private void _fachGetMengeKursarten() {
		final long idFach = getFachIdAuchUngueltig();
		final @NotNull List<GostKursart> list1 = m1.fachGetMengeKursarten(idFach);
		final @NotNull List<GostKursart> list2 = m2.fachGetMengeKursarten(idFach);

		assertEquals(list1.size(), list2.size());
		for (int i = 0; i < list1.size(); i++)
			assertEquals(list1.get(i).id, list2.get(i).id);
	}

	private void _fachwahlAdd() {
		if (m1.fachwahlGetAnzahl() >= MAX_FACHWAHLEN)
			return;

		final @NotNull GostFachwahl[] f = createFachwahlDuplikateAuchUngueltig();
		Exception ex1 = null;
		Exception ex2 = null;

		try {
			m1.fachwahlAdd(f[0]);
		} catch (final DeveloperNotificationException | CoreTypeException ex) {
			ex1 = ex;
		}
		try {
			m2.fachwahlAdd(f[1]);
		} catch (final DeveloperNotificationException | CoreTypeException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		_fachwahlGetAnzahl();
	}

	private void _fachwahlAddListe() {
		if (m1.fachwahlGetAnzahl() >= MAX_FACHWAHLEN)
			return;

		final @NotNull List<GostFachwahl> list1 = new ArrayList<>();
		final @NotNull List<GostFachwahl> list2 = new ArrayList<>();
		do {
			final @NotNull GostFachwahl[] f = createFachwahlDuplikateAuchUngueltig();
			list1.add(f[0]);
			list2.add(f[1]);
		} while (RND.nextBoolean());

		Exception ex1 = null;
		Exception ex2 = null;
		try {
			m1.fachwahlAddListe(list1);
		} catch (final DeveloperNotificationException | CoreTypeException ex) {
			ex1 = ex;
		}
		try {
			m2.fachwahlAddListe(list2);
		} catch (final DeveloperNotificationException | CoreTypeException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		_fachwahlGetAnzahl();
	}

	private void _fachwahlGetAnzahl() {
		final int v1 = m1.fachwahlGetAnzahl();
		final int v2 = m2.fachwahlGetAnzahl();
		assertEquals(v1, v2);
	}

	private void _fachwahlGetName() {
		final @NotNull GostFachwahl[] f = createFachwahlDuplikateAuchUngueltig();
		Exception ex1 = null;
		Exception ex2 = null;

		try {
			m1.fachwahlGetName(f[0]);
		} catch (final DeveloperNotificationException | CoreTypeException ex) {
			ex1 = ex;
		}
		try {
			m2.fachwahlGetName(f[1]);
		} catch (final DeveloperNotificationException | CoreTypeException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		if (ex1 == null)
			assertEquals(m1.fachwahlGetName(f[0]), m2.fachwahlGetName(f[1]));
	}

	private void _fachwahlGetListeOfFachart() {
		long idFachart = getFachartIdAuchUngueltig();
		final @NotNull List<GostFachwahl> list1 = m1.fachwahlGetListeOfFachart(idFachart);
		final @NotNull List<GostFachwahl> list2 = m2.fachwahlGetListeOfFachart(idFachart);

		assertEquals(list1.size(), list2.size());
		for (int i = 0; i < list1.size(); i++)
			assertEqualFachwahl(list1.get(i), list2.get(i));
	}

	private void _fachwahlGetAnzahlVerwendeterKursarten() {
		final int v1 = m1.fachwahlGetAnzahlVerwendeterKursarten();
		final int v2 = m2.fachwahlGetAnzahlVerwendeterKursarten();
		assertEquals(v1, v2);
	}

	private void _schuelerAdd() {
		if (m1.schuelerGetAnzahl() >= MAX_SCHUELER)
			return;

		final @NotNull Schueler[] s = createSchuelerDuplikateAuchUngueltig();
		Exception ex1 = null;
		Exception ex2 = null;

		try {
			m1.schuelerAdd(s[0]);
		} catch (final DeveloperNotificationException | CoreTypeException ex) {
			ex1 = ex;
		}
		try {
			m2.schuelerAdd(s[1]);
		} catch (final DeveloperNotificationException | CoreTypeException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		_schuelerGetAnzahl();
	}

	private void _schuelerAddListe() {
		if (m1.schuelerGetAnzahl() >= MAX_SCHIENEN)
			return;

		final @NotNull List<Schueler> list1 = new ArrayList<>();
		final @NotNull List<Schueler> list2 = new ArrayList<>();
		do {
			final @NotNull Schueler[] s = createSchuelerDuplikateAuchUngueltig();
			list1.add(s[0]);
			list2.add(s[1]);
		} while (RND.nextBoolean());

		Exception ex1 = null;
		Exception ex2 = null;
		try {
			m1.schuelerAddListe(list1);
		} catch (final DeveloperNotificationException | CoreTypeException ex) {
			ex1 = ex;
		}
		try {
			m2.schuelerAddListe(list2);
		} catch (final DeveloperNotificationException | CoreTypeException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		_schuelerGetAnzahl();
	}

	private void _schuelerGetAnzahlMitMindestensEinerFachwahl() {
		final int v1 = m1.schuelerGetAnzahlMitMindestensEinerFachwahl();
		final int v2 = m2.schuelerGetAnzahlMitMindestensEinerFachwahl();
		assertEquals(v1, v2);
	}

	private void _schuelerGetAnzahl() {
		final int v1 = m1.schuelerGetAnzahl();
		final int v2 = m2.schuelerGetAnzahl();
		assertEquals(v1, v2);
	}

	private void _schuelerGet() {
		final long idSchueler = getSchuelerIdAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.schuelerGet(idSchueler);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.schuelerGet(idSchueler);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		if (ex1 == null)
			assertEquals(m1.schuelerGet(idSchueler).id, m2.schuelerGet(idSchueler).id);
	}

	private void _schuelerGetOrNull() {
		final long idSchueler = getSchuelerIdAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.schuelerGetOrNull(idSchueler);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.schuelerGetOrNull(idSchueler);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		if (ex1 == null)
			if (m1.schuelerGetOrNull(idSchueler) == null)
				assertEquals(null, m2.schuelerGetOrNull(idSchueler));
			else
				assertEquals(m1.schuelerGet(idSchueler).id, m2.schuelerGet(idSchueler).id);
	}

	private void _schuelerGetListe() {
		final @NotNull List<Schueler> list1 = m1.schuelerGetListe();
		final @NotNull List<Schueler> list2 = m2.schuelerGetListe();

		assertEquals(list1.size(), list2.size());
		for (int i = 0; i < list1.size(); i++)
			assertEquals(list1.get(i).id, list2.get(i).id);
	}

	private void _schuelerGetOfFachKursart() {
		final long idSchueler = getSchuelerIdAuchUngueltig();
		final long idFach = getFachIdAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.schuelerGetOfFachKursart(idSchueler, idFach);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.schuelerGetOfFachKursart(idSchueler, idFach);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		if (ex1 == null)
			assertEquals(m1.schuelerGetOfFachKursart(idSchueler, idFach).id,
					m2.schuelerGetOfFachKursart(idSchueler, idFach).id);
	}

	private void _schuelerGetOfFachFachwahl() {
		final long idSchueler = getSchuelerIdAuchUngueltig();
		final long idFach = getFachIdAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.schuelerGetOfFachFachwahl(idSchueler, idFach);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.schuelerGetOfFachFachwahl(idSchueler, idFach);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		if (ex1 == null)
			assertEqualFachwahl(m1.schuelerGetOfFachFachwahl(idSchueler, idFach),
					m2.schuelerGetOfFachFachwahl(idSchueler, idFach));
	}

	private void _schuelerGetOfFachFachwahlOrNull() {
		final long idSchueler = getSchuelerIdAuchUngueltig();
		final long idFach = getFachIdAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.schuelerGetOfFachFachwahlOrNull(idSchueler, idFach);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.schuelerGetOfFachFachwahlOrNull(idSchueler, idFach);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		if (ex1 == null)
			assertEqualFachwahl(m1.schuelerGetOfFachFachwahlOrNull(idSchueler, idFach),
					m2.schuelerGetOfFachFachwahlOrNull(idSchueler, idFach));
	}

	private void _schuelerGetHatFach() {
		final long idSchueler = getSchuelerIdAuchUngueltig();
		final long idFach = getFachIdAuchUngueltig();
		final boolean v1 = m1.schuelerGetHatFach(idSchueler, idFach);
		final boolean v2 = m2.schuelerGetHatFach(idSchueler, idFach);
		assertEquals(v1, v2);
	}

	private void _schuelerGetHatDieSelbeKursartMitSchuelerInFach() {
		final long idSchueler1 = getSchuelerIdAuchUngueltig();
		final long idSchueler2 = getSchuelerIdAuchUngueltig();
		final long idFach = getFachIdAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.schuelerGetHatDieSelbeKursartMitSchuelerInFach(idSchueler1, idSchueler2, idFach);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.schuelerGetHatDieSelbeKursartMitSchuelerInFach(idSchueler1, idSchueler2, idFach);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		if (ex1 == null)
			assertEquals(m1.schuelerGetHatDieSelbeKursartMitSchuelerInFach(idSchueler1, idSchueler2, idFach),
					m2.schuelerGetHatDieSelbeKursartMitSchuelerInFach(idSchueler1, idSchueler2, idFach));
	}

	private void _schuelerGetHatFachart() {
		final long idSchueler = getSchuelerIdAuchUngueltig();
		final long idFach = getFachIdAuchUngueltig();
		final int idKursart = getKursartAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.schuelerGetHatFachart(idSchueler, idFach, idKursart);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.schuelerGetHatFachart(idSchueler, idFach, idKursart);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		if (ex1 == null)
			assertEquals(m1.schuelerGetHatFachart(idSchueler, idFach, idKursart),
					m2.schuelerGetHatFachart(idSchueler, idFach, idKursart));
	}

	private void _schuelerGetListeOfFachwahlen() {
		final long idSchueler = getSchuelerIdAuchUngueltig();
		final @NotNull List<GostFachwahl> list1 = m1.schuelerGetListeOfFachwahlen(idSchueler);
		final @NotNull List<GostFachwahl> list2 = m2.schuelerGetListeOfFachwahlen(idSchueler);

		assertEquals(list1.size(), list2.size());
		for (int i = 0; i < list1.size(); i++)
			assertEqualFachwahl(list1.get(i), list2.get(i));
	}

	private void _schuelerGetFachListeGemeinsamerFacharten() {
		final long idSchueler1 = getSchuelerIdAuchUngueltig();
		final long idSchueler2 = getSchuelerIdAuchUngueltig();
		final @NotNull List<GostFach> list1 = m1.schuelerGetFachListeGemeinsamerFacharten(idSchueler1, idSchueler2);
		final @NotNull List<GostFach> list2 = m2.schuelerGetFachListeGemeinsamerFacharten(idSchueler1, idSchueler2);

		assertEquals(list1.size(), list2.size());
		for (int i = 0; i < list1.size(); i++)
			assertEquals(list1.get(i).id, list2.get(i).id);
	}

	private void _schuelerGetIstVerbotenInKurs() {
		final long idSchueler = getSchuelerIdAuchUngueltig();
		final long idKurs = getKursIdAuchUngueltig();
		final boolean v1 = m1.schuelerGetIstVerbotenInKurs(idSchueler, idKurs);
		final boolean v2 = m2.schuelerGetIstVerbotenInKurs(idSchueler, idKurs);
		assertEquals(v1, v2);
	}

	private void _schuelerGetRegelVerbotenInKurs() {
		final long idSchueler = getSchuelerIdAuchUngueltig();
		final long idKurs = getKursIdAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.schuelerGetRegelVerbotenInKurs(idSchueler, idKurs);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.schuelerGetRegelVerbotenInKurs(idSchueler, idKurs);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		if (ex1 == null)
			assertEquals(m1.schuelerGetRegelVerbotenInKurs(idSchueler, idKurs).id,
					m2.schuelerGetRegelVerbotenInKurs(idSchueler, idKurs).id);
	}

	private void _schuelerGetIstFixiertInKurs() {
		final long idSchueler = getSchuelerIdAuchUngueltig();
		final long idKurs = getKursIdAuchUngueltig();
		final boolean v1 = m1.schuelerGetIstFixiertInKurs(idSchueler, idKurs);
		final boolean v2 = m2.schuelerGetIstFixiertInKurs(idSchueler, idKurs);
		assertEquals(v1, v2);
	}

	private void _schuelerGetRegelFixiertInKurs() {
		final long idSchueler = getSchuelerIdAuchUngueltig();
		final long idKurs = getKursIdAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.schuelerGetRegelFixiertInKurs(idSchueler, idKurs);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.schuelerGetRegelFixiertInKurs(idSchueler, idKurs);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		if (ex1 == null)
			assertEquals(m1.schuelerGetRegelFixiertInKurs(idSchueler, idKurs).id,
					m2.schuelerGetRegelFixiertInKurs(idSchueler, idKurs).id);
	}

	private void _ergebnisAdd() {
		if ((m1.ergebnisGetAnzahl() == 1) && (RND.nextDouble() < 0.99))
			return;
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

	private void _ergebnisAddListe() {
		if ((m1.ergebnisGetAnzahl() == 1) && (RND.nextDouble() < 0.99))
			return;
		if (m1.ergebnisGetAnzahl() >= MAX_SCHIENEN)
			return;

		final @NotNull List<GostBlockungsergebnis> list1 = new ArrayList<>();
		final @NotNull List<GostBlockungsergebnis> list2 = new ArrayList<>();
		do {
			final @NotNull GostBlockungsergebnis[] e = createErgebnisDuplikateAuchUngueltig();
			list1.add(e[0]);
			list2.add(e[1]);
		} while (RND.nextBoolean());
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.ergebnisAddListe(list1);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.ergebnisAddListe(list2);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		_ergebnisGetAnzahl();
	}

	private void _ergebnisGet() {
		final long idErgebnis = getErgebnisIdAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.ergebnisGet(idErgebnis);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.ergebnisGet(idErgebnis);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		if (ex1 == null)
			assertEquals(m1.ergebnisGet(idErgebnis).id, m2.ergebnisGet(idErgebnis).id);
	}

	private void _ergebnisManagerGet() {
		final long idErgebnis = getErgebnisIdAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.ergebnisManagerGet(idErgebnis);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.ergebnisManagerGet(idErgebnis);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		if (ex1 == null)
			assertEquals(m1.ergebnisManagerGet(idErgebnis).getErgebnis().id, m2.ergebnisManagerGet(idErgebnis).getErgebnis().id);
	}

	private void _ergebnisManagerExists() {
		final long idErgebnis = getErgebnisIdAuchUngueltig();
		final boolean b1 = m1.ergebnisManagerExists(idErgebnis);
		final boolean b2 = m2.ergebnisManagerExists(idErgebnis);
		assertEquals(b1, b2);
	}

	private void _ergebnisManagerGetListeUnsortiert() {
		final @NotNull List<GostBlockungsergebnisManager> list1 = m1.ergebnisManagerGetListeUnsortiert();
		final @NotNull List<DummyGostBlockungsergebnisManager> list2 = m2.ergebnisManagerGetListeUnsortiert();
		assertEquals(list1.size(), list2.size()); // Inhalte können nicht verglichen werden, da nicht sortiert.
	}

	private void _ergebnisGetListeSortiert() {
		final @NotNull List<GostBlockungsergebnis> list1 = m1.ergebnisGetListeSortiertNachBewertung();
		final @NotNull List<GostBlockungsergebnis> list2 = m2.ergebnisGetListeSortiertNachBewertung();

		assertEquals(list1.size(), list2.size()); // TODO DummyManager hat noch keine Sortiertung der Ergebnisse.

		final @NotNull List<GostBlockungsergebnis> list3 = m1.ergebnisGetListeSortiertNachID();
		final @NotNull List<GostBlockungsergebnis> list4 = m2.ergebnisGetListeSortiertNachID();

		assertEquals(list3.size(), list4.size());
		for (int i = 0; i < list3.size(); i++)
			assertEqualErgebnis(list3.get(i), list4.get(i));
	}

	private void _ergebnisRemoveListeByIDs() {
		if ((m1.ergebnisGetAnzahl() == 1) && (RND.nextDouble() < 0.99))
			return;

		final @NotNull Set<Long> listeDerErgebnisIDs1 = new HashSet<>();
		final @NotNull Set<Long> listeDerErgebnisIDs2 = new HashSet<>();
		do {
			final long idErgebnis = getErgebnisIdAuchUngueltig();
			listeDerErgebnisIDs1.add(idErgebnis);
			listeDerErgebnisIDs2.add(idErgebnis);
		} while (RND.nextBoolean());

		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.ergebnisRemoveListeByIDs(listeDerErgebnisIDs1);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.ergebnisRemoveListeByIDs(listeDerErgebnisIDs2);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		_ergebnisGetAnzahl();
	}

	private void _ergebnisRemoveListe() {
		if ((m1.ergebnisGetAnzahl() == 1) && (RND.nextDouble() < 0.99))
			return;

		final @NotNull List<GostBlockungsergebnis> list1 = new ArrayList<>();
		final @NotNull List<GostBlockungsergebnis> list2 = new ArrayList<>();
		do {
			final @NotNull GostBlockungsergebnis[] e = createErgebnisDuplikateAuchUngueltig();
			list1.add(e[0]);
			list2.add(e[1]);
		} while (RND.nextBoolean());

		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.ergebnisRemoveListe(list1);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.ergebnisRemoveListe(list2);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		_ergebnisGetAnzahl();
	}

	private void _ergebnisRemoveByID() {
		if ((m1.ergebnisGetAnzahl() == 1) && (RND.nextDouble() < 0.99))
			return;

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

	private void _ergebnisRemove() {
		if ((m1.ergebnisGetAnzahl() == 1) && (RND.nextDouble() < 0.99))
			return;

		final @NotNull GostBlockungsergebnis[] e = createErgebnisDuplikateAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.ergebnisRemove(e[0]);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.ergebnisRemove(e[1]);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		_ergebnisGetAnzahl();
	}

	private void _ergebnisUpdateBewertung() {
		final @NotNull GostBlockungsergebnis[] e = createErgebnisDuplikateAuchUngueltig();
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.ergebnisUpdateBewertung(e[0]);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.ergebnisUpdateBewertung(e[1]);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		_ergebnisGetListeSortiert();
	}

	private void _ergebnisAlleRevalidieren() {
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;

		try {
			m1.ergebnisAlleRevalidieren();
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.ergebnisAlleRevalidieren();
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		_ergebnisGetListeSortiert();
	}

	private void _ergebnisGetAnzahl() {
		final int v1 = m1.ergebnisGetAnzahl();
		final int v2 = m2.ergebnisGetAnzahl();
		assertEquals(v1, v2);
	}

	private void _ergebnisGetBewertung1Wert() {
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;
		final long idErgebnis = getErgebnisIdAuchUngueltig();

		try {
			m1.ergebnisGetBewertung1Wert(idErgebnis);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.ergebnisGetBewertung1Wert(idErgebnis);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		// TODO Der Wert muss gleich sein! (falls keine Exception)
	}

	private void _ergebnisGetBewertung1Intervall() {
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;
		final long idErgebnis = getErgebnisIdAuchUngueltig();

		try {
			m1.ergebnisGetBewertung1Intervall(idErgebnis);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.ergebnisGetBewertung1Intervall(idErgebnis);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		// TODO Der Wert muss gleich sein! (falls keine Exception)
	}

	private void _ergebnisGetBewertung2Wert() {
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;
		final long idErgebnis = getErgebnisIdAuchUngueltig();

		try {
			m1.ergebnisGetBewertung2Wert(idErgebnis);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.ergebnisGetBewertung2Wert(idErgebnis);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		// TODO Der Wert muss gleich sein! (falls keine Exception)
	}

	private void _ergebnisGetBewertung2Intervall() {
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;
		final long idErgebnis = getErgebnisIdAuchUngueltig();

		try {
			m1.ergebnisGetBewertung2Intervall(idErgebnis);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.ergebnisGetBewertung2Intervall(idErgebnis);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		// TODO Der Wert muss gleich sein! (falls keine Exception)
	}

	private void _ergebnisGetBewertung3Wert() {
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;
		final long idErgebnis = getErgebnisIdAuchUngueltig();

		try {
			m1.ergebnisGetBewertung3Wert(idErgebnis);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.ergebnisGetBewertung3Wert(idErgebnis);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		// TODO Der Wert muss gleich sein! (falls keine Exception)
	}

	private void _ergebnisGetBewertung3Intervall() {
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;
		final long idErgebnis = getErgebnisIdAuchUngueltig();

		try {
			m1.ergebnisGetBewertung3Intervall(idErgebnis);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.ergebnisGetBewertung3Intervall(idErgebnis);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		// TODO Der Wert muss gleich sein! (falls keine Exception)
	}

	private void _ergebnisGetBewertung4Wert() {
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;
		final long idErgebnis = getErgebnisIdAuchUngueltig();

		try {
			m1.ergebnisGetBewertung4Wert(idErgebnis);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.ergebnisGetBewertung4Wert(idErgebnis);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		// TODO Der Wert muss gleich sein! (falls keine Exception)
	}

	private void _ergebnisGetBewertung4Intervall() {
		DeveloperNotificationException ex1 = null;
		DeveloperNotificationException ex2 = null;
		final long idErgebnis = getErgebnisIdAuchUngueltig();

		try {
			m1.ergebnisGetBewertung4Intervall(idErgebnis);
		} catch (final DeveloperNotificationException ex) {
			ex1 = ex;
		}
		try {
			m2.ergebnisGetBewertung4Intervall(idErgebnis);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		// TODO Der Wert muss gleich sein! (falls keine Exception)
	}

	private void _schieneAdd() {
		if (!schieneAdd)
			return;
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
				e.setAddSchieneByID(s[1].id);
		} catch (final DeveloperNotificationException ex) {
			ex2 = ex;
		}

		assertEqualExceptions(ex1, ex2);
		_schieneGetAnzahl();
	}


	private void _schieneAddListe() {
		if (!schieneAdd)
			return;
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
			assertEqualSchienen(list1.get(i), list2.get(i));
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
		if (schieneAdd)
			return;
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
		if (schieneAdd)
			return;
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
		final int v2 = m2.schieneGetAnzahl();
		assertEquals(v1, v2);

		// Auch Schienen-Nummern testen
		final List<GostBlockungSchiene> schienen1 = m1.schieneGetListe();
		final List<GostBlockungSchiene> schienen2 = m2.schieneGetListe();
		for (int i = 0; i < schienen1.size(); i++)
			assertEqualSchienen(schienen1.get(i), schienen2.get(i));

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
		if (!kursAdd)
			return;
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
		if (!kursAdd)
			return;
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
			if (m2.kursGet(idKurs).lehrer.size() >= MAX_KURS_LEHRER)
				return;
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
			assertEqualKurs(list1.get(i), list2.get(i));
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
		if (ex1 == null)
			assertEquals(m1.kursGetIstVerbotenInSchiene(idKurs, idSchiene),
					m2.kursGetIstVerbotenInSchiene(idKurs, idSchiene));
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
		if (ex1 == null)
			assertEquals(m1.kursGetHatSperrungInSchiene(idKurs, idSchiene),
					m2.kursGetHatSperrungInSchiene(idKurs, idSchiene));
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
			assertEquals(m1.kursGetRegelFixierungInSchiene(idKurs, idSchiene).id,
					m2.kursGetRegelFixierungInSchiene(idKurs, idSchiene).id);
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
		if (kursAdd)
			return;

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
		if (kursAdd)
			return;

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
		if (kursAdd)
			return;

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
		if (kursAdd)
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
		if (kursAdd)
			return;

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

	private long getBlockungIdAuchUngueltig() {
		return RND.nextBoolean() ? 777 : 888;
	}

	private long getFachartIdAuchUngueltig() {
		long idFach = getFachIdAuchUngueltig();
		int idKursart = getKursartAuchUngueltig();
		return GostKursart.getFachartID(idFach, idKursart);
	}

	// Enum-Typ {@link Geschlecht}
	private int getSchuelerGeschlechtAuchUngueltig(final double chanceUngueltig) {
		return RND.nextDouble() < chanceUngueltig ? -1 : Geschlecht.values()[RND.nextInt(Geschlecht.values().length)].id;
	}

	// Enum-Typ {@link SchuelerStatus}
	private int getSchuelerStatusAuchUngueltig(final double chanceUngueltig) {
		return RND.nextDouble() < chanceUngueltig ? -1 : SchuelerStatus.values()[RND.nextInt(SchuelerStatus.values().length)].ordinal();
	}

	private long getFachIdAuchUngueltig() {
		return RND.nextLong(-2, FM.faecher().size() + 2);
	}

	private int getKursartAuchUngueltig() {
		return RND.nextInt(-2, GostKursart.values().length + 2);
	}

	private long getErgebnisIdAuchUngueltig() {
		return RND.nextLong(-2, MAX_ERGEBNISSE * 2 + 2);
	}

	private long getLehrkraftIdAuchUngueltig() {
		return RND.nextLong(-2, MAX_LEHRER * 2 + 2);
	}

	private long getSchuelerIdAuchUngueltig() {
		return RND.nextLong(-2, MAX_SCHUELER * 2 + 2);
	}

	private long getSchieneIdAuchUngueltig() {
		return RND.nextLong(-2, MAX_SCHIENEN * 2 + 2);
	}

	private int getSchieneNrAuchUngueltig() {
		return RND.nextInt(-2, MAX_SCHIENEN * 2 + 2);
	}

	private long getKursIdAuchUngueltig() {
		return RND.nextLong(-2, MAX_KURSE * 2 + 2);
	}

	private long getRegelIdAuchUngueltig() {
		return RND.nextLong(-2, MAX_REGELN * 2 + 2);
	}

	private int getRegelTypAuchUngueltig(final double chanceUngueltig) {
		if (RND.nextDouble() < chanceUngueltig)
			return -1;
		GostKursblockungRegelTyp[] a = GostKursblockungRegelTyp.values();
		return a[RND.nextInt(a.length)].typ;
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

	private @NotNull GostBlockungKurs[] createKursDuplikateAuchUngueltig() {
		final @NotNull GostBlockungKurs kurs1 = new GostBlockungKurs();
		kurs1.id = getKursIdAuchUngueltig();
		kurs1.fach_id = getFachIdAuchUngueltig();
		kurs1.kursart = getKursartAuchUngueltig();
		kurs1.nummer = RND.nextInt(0, 5);
		kurs1.istKoopKurs = RND.nextBoolean();
		kurs1.suffix = RND.nextBoolean() ? "" : "#" + RND.nextInt(0, 999) + "#";
		kurs1.wochenstunden = RND.nextBoolean() ? 3 : RND.nextInt(-2, 5);
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

	private @NotNull Schueler[] createSchuelerDuplikateAuchUngueltig() {
		final @NotNull Schueler ergebnis1 = new Schueler();
		ergebnis1.id = getSchuelerIdAuchUngueltig();
		ergebnis1.nachname = "N" + RND.nextInt();
		ergebnis1.vorname = "V" + RND.nextInt();
		ergebnis1.geschlecht = getSchuelerGeschlechtAuchUngueltig(0.1);
		ergebnis1.status = getSchuelerStatusAuchUngueltig(0.1);
		ergebnis1.abschlussjahrgang = RND.nextBoolean() ? -1 : RND.nextInt(1900, 2200);
		ergebnis1.externeSchulNr = RND.nextBoolean() ? null : "" + RND.nextInt(100);

		final @NotNull Schueler ergebnis2 = new Schueler();
		ergebnis2.id = ergebnis1.id;
		ergebnis2.nachname = ergebnis1.nachname;
		ergebnis2.vorname = ergebnis1.vorname;
		ergebnis2.geschlecht = ergebnis1.geschlecht;
		ergebnis2.status = ergebnis1.status;
		ergebnis2.abschlussjahrgang = ergebnis1.abschlussjahrgang;
		ergebnis2.externeSchulNr = ergebnis1.externeSchulNr;

		return new Schueler[] { ergebnis1, ergebnis2 };
	}

	private @NotNull GostFachwahl[] createFachwahlDuplikateAuchUngueltig() {
		final @NotNull GostFachwahl ergebnis1 = new GostFachwahl();
		ergebnis1.fachID = getFachIdAuchUngueltig();
		ergebnis1.kursartID = getKursartAuchUngueltig();
		ergebnis1.schuelerID = getSchieneIdAuchUngueltig();
		ergebnis1.istSchriftlich = RND.nextBoolean();
		ergebnis1.abiturfach = RND.nextBoolean() ? null : RND.nextInt(-2, 10);

		final @NotNull GostFachwahl ergebnis2 = new GostFachwahl();
		ergebnis2.fachID = ergebnis1.fachID;
		ergebnis2.kursartID = ergebnis1.kursartID;
		ergebnis2.schuelerID = ergebnis1.schuelerID;
		ergebnis2.istSchriftlich = ergebnis1.istSchriftlich;
		ergebnis2.abiturfach = ergebnis1.abiturfach;

		return new GostFachwahl[] { ergebnis1, ergebnis2 };
	}

	private @NotNull GostBlockungRegel[] createRegelDuplikateAuchUngueltig() {
		// TODO bessere Regeln erzeugen, am besten pro Typ eine eigene Methode
		final @NotNull GostBlockungRegel ergebnis1 = new GostBlockungRegel();
		ergebnis1.id = getRegelIdAuchUngueltig();
		ergebnis1.typ = getRegelTypAuchUngueltig(0.1);
		int parameterAnzahl = RND.nextInt(6);
		for (int i = 0; i < parameterAnzahl; i++)
			ergebnis1.parameter.add(RND.nextLong(-1, 10));

		final @NotNull GostBlockungRegel ergebnis2 = new GostBlockungRegel();
		ergebnis2.id = ergebnis1.id;
		ergebnis2.typ = ergebnis1.typ;
		ergebnis2.parameter.addAll(ergebnis1.parameter);

		return new GostBlockungRegel[] { ergebnis1, ergebnis2 };
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

	private void assertEqualExceptions(final Exception ex1, final Exception ex2) {
		if ((ex1 == null) != (ex2 == null)) {
			System.out.println("assertEqualExceptions failed");
			System.out.println("EX1 = " + ex1);
			System.out.println("EX2 = " + ex2);

			System.out.println();
			System.out.println();
			System.out.println("Debug-Ausgabe von M1:");
			System.out.println(m1.getDebugString());

			System.out.println();
			System.out.println();
			System.out.println("Debug-Ausgabe von M2:");
			System.out.println(m1.getDebugString());


			if (ex1 != null)
				ex1.printStackTrace();
			if (ex2 != null)
				ex2.printStackTrace();
		}
		assertEquals(ex1 == null, ex2 == null);
	}

	private static void assertEqualKurs(final @NotNull GostBlockungKurs k1, final @NotNull GostBlockungKurs k2) {
		assertEquals(k1.id, k2.id);
		assertEquals(k1.fach_id, k2.fach_id);
		assertEquals(k1.kursart, k2.kursart);
		assertEquals(k1.nummer, k2.nummer);
		assertEquals(k1.istKoopKurs, k2.istKoopKurs);
		assertEquals(k1.suffix, k2.suffix);
		assertEquals(k1.wochenstunden, k2.wochenstunden);
		assertEquals(k1.anzahlSchienen, k2.anzahlSchienen);
		assertEquals(k1.lehrer.size(), k2.lehrer.size());
		final @NotNull HashSet<Long> setLehrerIDs = new HashSet<>();
		for (final @NotNull GostBlockungKursLehrer l1 : k1.lehrer)
			setLehrerIDs.add(l1.id);
		for (final @NotNull GostBlockungKursLehrer l2 : k2.lehrer)
			assertEquals(true, setLehrerIDs.contains(l2.id));
	}

	private static void assertEqualFachwahl(final @NotNull GostFachwahl fw1, final @NotNull GostFachwahl fw2) {
		if ((fw1 == null) || (fw2 == null)) {
			if ((fw1 == null) && (fw2 == null))
				return;
			if (fw1 == null)
				fail("Fachwahl1 ist NULL, Fachwahl2 aber nicht!");
			else
				fail("Fachwahl2 ist NULL, Fachwahl1 aber nicht!");
			return;
		}
		assertEquals(fw1.fachID, fw2.fachID);
		assertEquals(fw1.schuelerID, fw2.schuelerID);
		assertEquals(fw1.kursartID, fw2.kursartID);
		assertEquals(fw1.istSchriftlich, fw2.istSchriftlich);
		assertEquals(fw1.abiturfach, fw2.abiturfach);
	}

	private static void assertEqualSchienen(final GostBlockungSchiene s1, final GostBlockungSchiene s2) {
		assertEquals(s1.id, s2.id);
		assertEquals(s1.bezeichnung, s2.bezeichnung);
		assertEquals(s1.nummer, s2.nummer);
		assertEquals(s1.wochenstunden, s2.wochenstunden);
	}


	private void assertEqualRegel(final GostBlockungRegel r1, final GostBlockungRegel r2) {
		if (r1.id != r2.id) {
			System.out.println("----- STATE DER REGEL-LISTEN ------");

			final @NotNull List<GostBlockungRegel> list1 = m1.regelGetListe();
			final @NotNull List<GostBlockungRegel> list2 = m2.regelGetListe();
			for (int i = 0; i < list1.size(); i++) {
				System.out.println(m1.toStringRegel(list1.get(i).id));
				System.out.println(m2.toStringRegel(list2.get(i).id));
				System.out.println();
			}

			System.out.println("---------------------------------------");
		}

		assertEquals(r1.id, r2.id);
		assertEquals(r1.typ, r2.typ);
		assertEquals(r1.parameter.size(), r2.parameter.size());
		for (int i = 0; i < r1.parameter.size(); i++)
			assertEquals(r1.parameter.get(i), r2.parameter.get(i));
	}

	private static void assertEqualHalbjahr(final @NotNull GostHalbjahr r1, final @NotNull GostHalbjahr r2) {
		assertEquals(r1.id, r2.id);
		assertEquals(r1.jahrgang, r2.jahrgang);
		assertEquals(r1.halbjahr, r2.halbjahr);
		assertEquals(r1.kuerzel, r2.kuerzel);
		assertEquals(r1.kuerzelAlt, r2.kuerzelAlt);
		assertEquals(r1.beschreibung, r2.beschreibung);
	}

	private static void assertEqualErgebnis(final @NotNull GostBlockungsergebnis a, final @NotNull GostBlockungsergebnis b) {
		assertEquals(a.id, b.id);
		//assertEquals(a.blockungID, b.blockungID);
		// assertEquals(a.gostHalbjahr, b.gostHalbjahr);
		//assertEquals(a.istAktiv, b.istAktiv);
		//assertEquals(a.name, b.name);
		// assertEquals(a.schienen.size(), b.schienen.size());
	}

}
