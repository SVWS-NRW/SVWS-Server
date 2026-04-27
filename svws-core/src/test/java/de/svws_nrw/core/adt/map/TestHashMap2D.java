package de.svws_nrw.core.adt.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;

/**
 * Tests fuer die aktuelle API der Klasse {@link HashMap2D}.
 */
class TestHashMap2D {

	private static final String TEST_CONTAINSKEY_1 = """
			true, 1
			true, 2
			false, 3
			false, 101
		""";

	private static final String TEST_CONTAINS_12 = """
			true, 1, 101
			true, 1, 102
			true, 2, 201
			false, 2, 202
			false, 9, 909
			false, 101, 1
		""";

	private HashMap2D<Integer, Integer, String> map = new HashMap2D<>();

	@BeforeEach
	void setup() {
		map = new HashMap2D<>();
		map.put(1, 101, "1.101");
		map.put(1, 102, "1.102");
		map.put(2, 201, "2.201");
	}

	@DisplayName("Test der 'containsKey1' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_CONTAINSKEY_1)
	void test_containsKey1(final boolean result, final int key1) {
		assertEquals(result, map.containsKey1(key1));

		if (!result) {
			final int key2 = key1 + 1000;
			map.put(key1, key2, key1 + "." + key2);
			assertTrue(map.containsKey1(key1));
		}
	}

	@DisplayName("Test der 'contains' Methode fuer (key1, key2).")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_CONTAINS_12)
	void test_contains(final boolean result, final int key1, final int key2) {
		assertEquals(result, map.contains(key1, key2));

		if (!result) {
			map.put(key1, key2, key1 + "." + key2);
			assertTrue(map.contains(key1, key2));
		}
	}

	@Test
	@DisplayName("Copy-Konstruktor erstellt eine inhaltlich gleiche und unabhaengige Kopie")
	void test_copyKonstruktor() {
		final HashMap2D<Integer, Integer, String> copy = new HashMap2D<>(map);

		assertNotNull(copy);
		assertEquals(map.size(), copy.size());
		assertEquals("1.101", copy.getOrException(1, 101));
		assertEquals("1.102", copy.getOrException(1, 102));
		assertEquals("2.201", copy.getOrException(2, 201));

		map.put(1, 101, "A-neu");
		map.removeOrException(2, 201);
		assertEquals("1.101", copy.getOrException(1, 101));
		assertEquals("2.201", copy.getOrException(2, 201));

		copy.put(9, 909, "9.909");
		assertFalse(map.contains(9, 909));
	}

	@Test
	@DisplayName("getOrNull und getOrException verhalten sich korrekt fuer vorhandene und fehlende Pfade")
	void test_getVarianten() {
		assertEquals("1.101", map.getOrNull(1, 101));
		assertNull(map.getOrNull(99, 999));
		assertEquals("1.102", map.getOrException(1, 102));
		assertThrows(DeveloperNotificationException.class, () -> map.getOrException(99, 999));
	}

	@Test
	@DisplayName("removeOrException entfernt Mapping und trimmt leere SubMap")
	void test_removeOrException() {
		assertEquals("2.201", map.removeOrException(2, 201));
		assertFalse(map.containsKey1(2));
		assertThrows(DeveloperNotificationException.class, () -> map.removeOrException(2, 201));
	}

	@Test
	@DisplayName("getSubMapOrNull und getSubMapOrException liefern fuer vorhandene/fehlende Keys korrekte Ergebnisse")
	void test_getSubMapVarianten() {
		final Map<Integer, String> subMapOrNull = map.getSubMapOrNull(1);
		assertNotNull(subMapOrNull);
		assertEquals(2, subMapOrNull.size());
		assertEquals("1.101", subMapOrNull.get(101));
		assertEquals("1.102", subMapOrNull.get(102));

		assertNull(map.getSubMapOrNull(99));

		final Map<Integer, String> subMapOrException = map.getSubMapOrException(1);
		assertNotNull(subMapOrException);
		assertEquals(2, subMapOrException.size());
		assertThrows(DeveloperNotificationException.class, () -> map.getSubMapOrException(99));
	}

	@Test
	@DisplayName("removeSubMap und removeSubMapOrException liefern die erwarteten Ergebnisse")
	void test_removeSubMapVarianten() {
		final Map<Integer, String> removed = map.removeSubMap(1);
		assertNotNull(removed);
		assertEquals(2, removed.size());
		assertNull(map.removeSubMap(1));
		assertThrows(DeveloperNotificationException.class, () -> map.removeSubMapOrException(1));
	}

	@Test
	@DisplayName("removeSubMapOrException liefert bei vorhandenem Key die SubMap und entfernt key1")
	void test_removeSubMapOrException_success() {
		final Map<Integer, String> removed = map.removeSubMapOrException(2);
		assertNotNull(removed);
		assertEquals(1, removed.size());
		assertEquals("2.201", removed.get(201));
		assertFalse(map.containsKey1(2));
	}

	@Test
	@DisplayName("Values-Listen enthalten die erwarteten Werte")
	void test_valueListen() {
		final List<String> valuesOfKey1 = map.getNonNullValuesOfKey1AsList(1);
		assertEquals(2, valuesOfKey1.size());
		assertTrue(valuesOfKey1.containsAll(List.of("1.101", "1.102")));

		final List<String> valuesOfKey1OrNull = map.getNonNullValuesOfKey1AsListOrNull(1);
		assertNotNull(valuesOfKey1OrNull);
		assertEquals(2, valuesOfKey1OrNull.size());
		assertTrue(valuesOfKey1OrNull.containsAll(List.of("1.101", "1.102")));

		assertNull(map.getNonNullValuesOfKey1AsListOrNull(99));

		final List<String> allValues = map.getNonNullValuesAsList();
		assertEquals(3, allValues.size());
		assertTrue(allValues.containsAll(List.of("1.101", "1.102", "2.201")));
	}

	@Test
	@DisplayName("KeySets und Groessenmethoden liefern konsistente Ergebnisse")
	void test_keySetsUndGroessen() {
		assertEquals(3, map.size());
		assertEquals(2, map.getSubMapSizeOrZero(1));
		assertEquals(0, map.getSubMapSizeOrZero(99));

		assertTrue(map.getKeySet().containsAll(List.of(1, 2)));
		assertEquals(2, map.getKeySetOf(1).size());
		assertThrows(DeveloperNotificationException.class, () -> map.getKeySetOf(99));

		assertEquals(2, map.getEntrySet().size());
	}

	@Test
	@DisplayName("clear leert die komplette Map")
	void test_clear() {
		map.clear();
		assertEquals(0, map.size());
		assertTrue(map.getKeySet().isEmpty());
	}
}
