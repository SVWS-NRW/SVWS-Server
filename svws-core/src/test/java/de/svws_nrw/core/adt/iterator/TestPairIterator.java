package de.svws_nrw.core.adt.iterator;

import de.svws_nrw.asd.adt.PairNN;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;
import java.util.Set;

/**
 * Testet die Klasse {@link PairIterable}.
 *
 * @author Benjamin A. Bartsch
 */
class TestPairIterator {

	// ################################################################################
	// Hilfsmethoden
	// ################################################################################

	@SuppressWarnings("unused")
	private static <T> int zaehle(final Iterable<T> source, final PairIteratorModus modus) {
		int count = 0;
		for (final PairNN<T, T> ignored : new PairIterable<>(source, modus)) {
			count++;
		}
		return count;
	}

	private static int zaehleErwartet_ALL(final int n) {
		return n * n;
	}

	private static int zaehleErwartet_NO_EQUAL(final int n) {
		return n * n - n;
	}

	private static int zaehleErwartet_LOWER_ONLY(final int n) {
		return (n * n - n) / 2;
	}

	// ################################################################################
	// Anzahl-Tests mit Integer
	// ################################################################################

	@Test
	void testAnzahl_Integer_ALL() {
		final List<Integer> liste = List.of(1, 2, 3, 4, 5);
		assertEquals(zaehleErwartet_ALL(liste.size()), zaehle(liste, PairIteratorModus.ALL));
	}

	@Test
	void testAnzahl_Integer_NO_EQUAL() {
		final List<Integer> liste = List.of(1, 2, 3, 4, 5);
		assertEquals(zaehleErwartet_NO_EQUAL(liste.size()), zaehle(liste, PairIteratorModus.NO_EQUAL));
	}

	@Test
	void testAnzahl_Integer_LOWER_ONLY() {
		final List<Integer> liste = List.of(1, 2, 3, 4, 5);
		assertEquals(zaehleErwartet_LOWER_ONLY(liste.size()), zaehle(liste, PairIteratorModus.LOWER_ONLY));
	}

	// ################################################################################
	// Anzahl-Tests mit Double
	// ################################################################################

	@Test
	void testAnzahl_Double_ALL() {
		final List<Double> liste = List.of(1.1, 2.2, 3.3);
		assertEquals(zaehleErwartet_ALL(liste.size()), zaehle(liste, PairIteratorModus.ALL));
	}

	@Test
	void testAnzahl_Double_NO_EQUAL() {
		final List<Double> liste = List.of(1.1, 2.2, 3.3);
		assertEquals(zaehleErwartet_NO_EQUAL(liste.size()), zaehle(liste, PairIteratorModus.NO_EQUAL));
	}

	@Test
	void testAnzahl_Double_LOWER_ONLY() {
		final List<Double> liste = List.of(1.1, 2.2, 3.3);
		assertEquals(zaehleErwartet_LOWER_ONLY(liste.size()), zaehle(liste, PairIteratorModus.LOWER_ONLY));
	}

	// ################################################################################
	// Anzahl-Tests mit String
	// ################################################################################

	@Test
	void testAnzahl_String_ALL() {
		final List<String> liste = List.of("Alpha", "Beta", "Gamma", "Delta");
		assertEquals(zaehleErwartet_ALL(liste.size()), zaehle(liste, PairIteratorModus.ALL));
	}

	@Test
	void testAnzahl_String_NO_EQUAL() {
		final List<String> liste = List.of("Alpha", "Beta", "Gamma", "Delta");
		assertEquals(zaehleErwartet_NO_EQUAL(liste.size()), zaehle(liste, PairIteratorModus.NO_EQUAL));
	}

	@Test
	void testAnzahl_String_LOWER_ONLY() {
		final List<String> liste = List.of("Alpha", "Beta", "Gamma", "Delta");
		assertEquals(zaehleErwartet_LOWER_ONLY(liste.size()), zaehle(liste, PairIteratorModus.LOWER_ONLY));
	}

	// ################################################################################
	// Anzahl-Tests mit Character
	// ################################################################################

	@Test
	void testAnzahl_Character_ALL() {
		final List<Character> liste = List.of('A', 'B', 'C', 'D', 'E', 'F');
		assertEquals(zaehleErwartet_ALL(liste.size()), zaehle(liste, PairIteratorModus.ALL));
	}

	@Test
	void testAnzahl_Character_NO_EQUAL() {
		final List<Character> liste = List.of('A', 'B', 'C', 'D', 'E', 'F');
		assertEquals(zaehleErwartet_NO_EQUAL(liste.size()), zaehle(liste, PairIteratorModus.NO_EQUAL));
	}

	@Test
	void testAnzahl_Character_LOWER_ONLY() {
		final List<Character> liste = List.of('A', 'B', 'C', 'D', 'E', 'F');
		assertEquals(zaehleErwartet_LOWER_ONLY(liste.size()), zaehle(liste, PairIteratorModus.LOWER_ONLY));
	}

	// ################################################################################
	// Anzahl-Tests mit Set (LinkedHashSet via List.copyOf entfällt → direkt Set)
	// ################################################################################

	@Test
	void testAnzahl_Set_LOWER_ONLY() {
		final Set<String> set = Set.of("X", "Y", "Z");
		assertEquals(zaehleErwartet_LOWER_ONLY(set.size()), zaehle(set, PairIteratorModus.LOWER_ONLY));
	}

	// ################################################################################
	// Grenzfälle: leere und einelementige Collection
	// ################################################################################

	@Test
	void testLeereCollection() {
		final List<Integer> leer = List.of();
		assertEquals(0, zaehle(leer, PairIteratorModus.ALL));
		assertEquals(0, zaehle(leer, PairIteratorModus.NO_EQUAL));
		assertEquals(0, zaehle(leer, PairIteratorModus.LOWER_ONLY));
	}

	@Test
	void testEinElementCollection() {
		final List<Integer> einElement = List.of(42);
		assertEquals(1, zaehle(einElement, PairIteratorModus.ALL));
		assertEquals(0, zaehle(einElement, PairIteratorModus.NO_EQUAL));
		assertEquals(0, zaehle(einElement, PairIteratorModus.LOWER_ONLY));
	}

	// ################################################################################
	// Mehrfacher Durchlauf (Iterable-Verhalten)
	// ################################################################################

	@SuppressWarnings("unused")
	@Test
	void testMehrfacherDurchlauf() {
		final List<Integer> liste = List.of(1, 2, 3);
		final PairIterable<Integer> iterable = new PairIterable<>(liste, PairIteratorModus.LOWER_ONLY);
		final int erwartet = zaehleErwartet_LOWER_ONLY(liste.size());

		int n1 = 0;
		for (final PairNN<Integer, Integer> ignored : iterable) {
			n1++;
		}

		int n2 = 0;
		for (final PairNN<Integer, Integer> ignored : iterable) {
			n2++;
		}

		assertEquals(erwartet, n1, "1. Durchlauf muss korrekte Anzahl liefern");
		assertEquals(erwartet, n2, "2. Durchlauf muss dieselbe Anzahl liefern");
	}

	// ################################################################################
	// Inhaltliche Korrektheit: LOWER_ONLY darf kein i==j und kein Duplikat enthalten
	// ################################################################################

	@Test
	void testInhalt_LOWER_ONLY_keineReflexivitaet() {
		final List<String> liste = List.of("A", "B", "C");
		for (final PairNN<String, String> pair : new PairIterable<>(liste, PairIteratorModus.LOWER_ONLY)) {
			assertNotEquals(pair.a, pair.b, "LOWER_ONLY darf kein Paar (x, x) enthalten");
		}
	}

	@Test
	void testInhalt_NO_EQUAL_keineReflexivitaet() {
		final List<String> liste = List.of("A", "B", "C");
		for (final PairNN<String, String> pair : new PairIterable<>(liste, PairIteratorModus.NO_EQUAL)) {
			assertNotEquals(pair.a, pair.b, "NO_EQUAL darf kein Paar (x, x) enthalten");
		}
	}

	@Test
	void testInhalt_ALL_enthaeltReflexivePaare() {
		final List<String> liste = List.of("A", "B", "C");
		int reflexiv = 0;
		for (final PairNN<String, String> pair : new PairIterable<>(liste, PairIteratorModus.ALL)) {
			if (pair.a.equals(pair.b)) {
				reflexiv++;
			}
		}
		assertEquals(liste.size(), reflexiv, "ALL muss genau n reflexive Paare enthalten");
	}

}
