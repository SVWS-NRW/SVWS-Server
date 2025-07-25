package de.svws_nrw.core.adt.map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;

/**
 * Diese Klasse testet die Funktionen der Klasse {@link ListMap2DLongKeys}.
 */
class TestListMap2DLongKeys {

	private static final String TEST_CONTAINSKEY_1 = """
			true, 1
			true, 2
			true, 3
			true, 4
			false, 5
		""";

	private static final String TEST_CONTAINSKEY_2 = """
			true, 1
			true, 2
			true, 3
			true, 4
			false, 5
		""";

	private static final String TEST_CONTAINSKEY_12 = """
			true, 2, 1
			true, 1, 2
			true, 1, 1
			true, 3, 3
			true, 4, 4
			false, 5,5
		""";

	private static final String TEST_GET_1 = """
			2, 1
			1, 2
			1, 3
			0, 4
			0, 5
		""";

	private static final String TEST_GET_2 = """
			2, 1
			1, 2
			1, 3
			0, 4
			0, 5
		""";

	private static final String TEST_GET_12 = """
			1, 2, 1
			1, 1, 2
			1, 1, 1
			1, 3, 3
			0, 4, 4
			0, 5, 5
		""";

	private static final String TEST_GET_SINGLE_OR_NULL_1 = """
			{null}, 1
			A, 2
			D, 3
			{null}, 4
			{null}, 5
		""";

	private static final String TEST_GET_SINGLE_OR_NULL_2 = """
			{null}, 1
			B, 2
			D, 3
			{null}, 4
			{null}, 5
		""";

	private static final String TEST_GET_SINGLE_OR_NULL_12 = """
			A, 2, 1
			B, 1, 2
			C, 1, 1
			D, 3, 3
			{null}, 4, 4
			{null}, 5, 5
		""";

	private static final String TEST_GET_SINGLE_OR_EXCEPTION_1 = """
			{null}, 1
			A, 2
			D, 3
			{null}, 4
			{null}, 5
		""";

	private static final String TEST_GET_SINGLE_OR_EXCEPTION_2 = """
			{null}, 1
			B, 2
			D, 3
			{null}, 4
			{null}, 5
		""";

	private static final String TEST_GET_SINGLE_OR_EXCEPTION_12 = """
			A, 2, 1
			B, 1, 2
			C, 1, 1
			D, 3, 3
			{null}, 4, 4
			{null}, 5, 5
		""";

	private static final String TEST_KEYSET_1 = """
			4
		""";

	private static final String TEST_KEYSET_2 = """
			4
		""";

	private static final String TEST_KEYSET_12 = """
			5
		""";

	private static final String TEST_GET_OR_EXCEPTION_1 = """
			2, 1
			1, 2
			1, 3
			0, 4
			{null}, 5
		""";

	private static final String TEST_GET_OR_EXCEPTION_2 = """
			2, 1
			1, 2
			1, 3
			0, 4
			{null}, 5
		""";

	private static final String TEST_GET_OR_EXCEPTION_12 = """
			1, 2, 1
			1, 1, 2
			1, 1, 1
			1, 3, 3
			0, 4, 4
			{null}, 5, 5
		""";

	private static final String TEST_REMOVE_VALUE_OR_EXCEPTION = """
		    1, 2, 1, A, {null}
		    1, 1, 2, B, {null}
		    1, 3, 3, D, {null}
		""";

	private static final String TEST_REMOVE_SINGLE_OR_EXCEPTION = """
		    1, 1, 2, B
		    1, 3, 3, D
		""";

	private static final String TEST_REMOVE_ALL_BY_KEY1 = """
		    2, 1
		    1, 1
		""";

	private static final String TEST_REMOVE_AND_GET_EMPTY_OR_MISSING = """
		    2, 1, "A", remove, true, 0
		    99, 77, {null}, remove, false, 0
		""";


	/**
	 * Initialisiert die Grunddaten.
	 */
	@BeforeEach
	void setup() {
		map = new ListMap2DLongKeys<>();
		map.add(2, 1, "A");
		map.add(1, 2, "B");
		map.add(1, 1, "C");
		map.add(3, 3, "D");
		map.addEmpty(4, 4);
	}

	private ListMap2DLongKeys<String> map = new ListMap2DLongKeys<>();

	/**
	 * Test der 'containsKey1' Methode.
	 *
	 * @param key1      Der 1. Schlüssel.
	 * @param result    Das erwartete Resultat.
	 */
	@DisplayName("Test der 'containsKey1' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_CONTAINSKEY_1)
	void test_containsKey1(final boolean result, final int key1) {
		assertEquals(result, map.containsKey1(key1));

		if (!result) {
			map.add(key1, key1, "X");
			assertEquals(true, map.containsKey1(key1));
		}
	}

	/**
	 * Test der 'containsKey2' Methode.
	 *
	 * @param key2      Der 2. Schlüssel.
	 * @param result    Das erwartete Resultat.
	 */
	@DisplayName("Test der 'containsKey2' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_CONTAINSKEY_2)
	void test_containsKey2(final boolean result, final int key2) {
		assertEquals(result, map.containsKey2(key2));

		if (!result) {
			map.add(key2, key2, "X");
			assertEquals(true, map.containsKey2(key2));
		}
	}

	/**
	 * Test der 'containsKey12' Methode.
	 *
	 * @param key1      Der 1. Schlüssel.
	 * @param key2      Der 2. Schlüssel.
	 * @param result    Das erwartete Resultat.
	 */
	@DisplayName("Test der 'containsKey12' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_CONTAINSKEY_12)
	void test_containsKey12(final boolean result, final int key1, final int key2) {
		assertEquals(result, map.containsKey12(key1, key2));

		if (!result) {
			map.add(key1, key2, "X");
			assertEquals(true, map.containsKey12(key1, key2));
		}
	}

	/**
	 * Test der 'get1' Methode.
	 *
	 * @param size      Die erwartete Listengröße.
	 * @param key1      Der 1. Schlüssel.
	 */
	@DisplayName("Test der 'get1' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_1)
	void test_get1(final int size, final int key1) {
		assertEquals(size, map.get1(key1).size());

		map.addEmpty(key1, 99);
		assertEquals(size, map.get1(key1).size());

		map.add(key1, 99, "X");
		assertEquals(size + 1, map.get1(key1).size());
	}

	/**
	 * Test der 'get2' Methode.
	 *
	 * @param size      Die erwartete Listengröße.
	 * @param key2      Der 2. Schlüssel.
	 */
	@DisplayName("Test der 'get2' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_2)
	void test_get2(final int size, final int key2) {
		assertEquals(size, map.get2(key2).size());

		map.addEmpty(99, key2);
		assertEquals(size, map.get2(key2).size());

		map.add(99, key2, "X");
		assertEquals(size + 1, map.get2(key2).size());
	}

	/**
	 * Test der 'get12' Methode.
	 *
	 * @param size      Die erwartete Listengröße.
	 * @param key1      Der 1. Schlüssel.
	 * @param key2      Der 2. Schlüssel.
	 */
	@DisplayName("Test der 'get12' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_12)
	void test_get12(final int size, final int key1, final int key2) {
		assertEquals(size, map.get12(key1, key2).size());

		map.addEmpty(key1, key2);
		assertEquals(size, map.get12(key1, key2).size());

		map.add(key1, key2, "X");
		assertEquals(size + 1, map.get12(key1, key2).size());
	}

	/**
	 * Test der 'getSingleOrNull1' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key1      Der 1. Schlüssel.
	 */
	@DisplayName("Test der 'getSingleOrNull1' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_SINGLE_OR_NULL_1, nullValues = "{null}")
	void test_getSingle1OrNull(final String result, final int key1) {
		assertEquals(result, map.getSingle1OrNull(key1));

		map.addEmpty(66, 66);
		assertEquals(result, map.getSingle1OrNull(key1));

		map.add(77, 77, "X");
		assertEquals(result, map.getSingle1OrNull(key1));
	}

	/**
	 * Test der 'getSingleOrNull2' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key2      Der 2. Schlüssel.
	 */
	@DisplayName("Test der 'getSingleOrNull2' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_SINGLE_OR_NULL_2, nullValues = "{null}")
	void test_getSingleOrNull2(final String result, final int key2) {
		assertEquals(result, map.getSingle2OrNull(key2));

		map.addEmpty(66, 66);
		assertEquals(result, map.getSingle2OrNull(key2));

		map.add(77, 77, "X");
		assertEquals(result, map.getSingle2OrNull(key2));
	}

	/**
	 * Test der 'getSingleOrNull12' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key1      Der 1. Schlüssel.
	 * @param key2      Der 2. Schlüssel.
	 */
	@DisplayName("Test der 'getSingleOrNull12' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_SINGLE_OR_NULL_12, nullValues = "{null}")
	void test_getSingleOrNull12(final String result, final int key1, final int key2) {
		assertEquals(result, map.getSingle12OrNull(key1, key2));

		map.addEmpty(66, 66);
		assertEquals(result, map.getSingle12OrNull(key1, key2));

		map.add(77, 77, "X");
		assertEquals(result, map.getSingle12OrNull(key1, key2));
	}

	/**
	 * Test der 'getSingleOrException1' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key1      Der 1. Schlüssel.
	 */
	@DisplayName("Test der 'getSingleOrException1' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_SINGLE_OR_EXCEPTION_1, nullValues = "{null}")
	void test_getSingleOrException1(final String result, final int key1) {
		if (result == null)
			assertThrows(DeveloperNotificationException.class, () -> {
				map.getSingle1OrException(key1);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.getSingle1OrException(key1));
			});
	}

	/**
	 * Test der 'getSingleOrException2' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key2      Der 2. Schlüssel.
	 */
	@DisplayName("Test der 'getSingleOrException2' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_SINGLE_OR_EXCEPTION_2, nullValues = "{null}")
	void test_getSingleOrException2(final String result, final int key2) {
		if (result == null)
			assertThrows(DeveloperNotificationException.class, () -> {
				map.getSingle2OrException(key2);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.getSingle2OrException(key2));
			});
	}

	/**
	 * Test der 'getSingleOrException12' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key1      Der 1. Schlüssel.
	 * @param key2      Der 2. Schlüssel.
	 */
	@DisplayName("Test der 'getSingleOrException12' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_SINGLE_OR_EXCEPTION_12, nullValues = "{null}")
	void test_getSingleOrException12(final String result, final int key1, final int key2) {
		if (result == null)
			assertThrows(DeveloperNotificationException.class, () -> {
				map.getSingle12OrException(key1, key2);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.getSingle12OrException(key1, key2));
			});
	}

	/**
	 * Test der 'keySet1' Methode.
	 *
	 * @param size      Die erwartete Größe des Keysets1.
	 */
	@DisplayName("Test der 'keySet1' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_KEYSET_1)
	void test_keySet1(final int size) {
		assertEquals(size, map.keySet1().size());

		map.addEmpty(66, 66);
		assertEquals(size + 1, map.keySet1().size());

		map.add(77, 77, "X");
		assertEquals(size + 2, map.keySet1().size());
	}

	/**
	 * Test der 'keySet2' Methode.
	 *
	 * @param size      Die erwartete Größe des Keysets2.
	 */
	@DisplayName("Test der 'keySet2' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_KEYSET_2)
	void test_keySet2(final int size) {
		assertEquals(size, map.keySet2().size());

		map.addEmpty(66, 66);
		assertEquals(size + 1, map.keySet2().size());

		map.add(77, 77, "X");
		assertEquals(size + 2, map.keySet2().size());
	}

	/**
	 * Test der 'keySet12' Methode.
	 *
	 * @param size      Die erwartete Größe des Keysets12.
	 */
	@DisplayName("Test der 'keySet12' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_KEYSET_12)
	void test_keySet12(final int size) {
		assertEquals(size, map.keySet12().size());

		map.addEmpty(66, 66);
		assertEquals(size + 1, map.keySet12().size());

		map.add(77, 77, "X");
		assertEquals(size + 2, map.keySet12().size());
	}

	/**
	 * Test der 'get1OrException' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key1      Der 1. Schlüssel.
	 */
	@DisplayName("Test der 'get1OrException' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_OR_EXCEPTION_1, nullValues = "{null}")
	void test_get1OrException(final Integer result, final int key1) {
		if (result == null)
			assertThrows(DeveloperNotificationException.class, () -> {
				map.get1OrException(key1);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.get1OrException(key1).size());
			});
	}

	/**
	 * Test der 'get2OrException' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key2      Der 2. Schlüssel.
	 */
	@DisplayName("Test der 'get2OrException' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_OR_EXCEPTION_2, nullValues = "{null}")
	void test_get2OrException(final Integer result, final int key2) {
		if (result == null)
			assertThrows(DeveloperNotificationException.class, () -> {
				map.get2OrException(key2);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.get2OrException(key2).size());
			});
	}

	/**
	 * Test der 'get12OrException' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key1      Der 1. Schlüssel.
	 * @param key2      Der 2. Schlüssel.
	 */
	@DisplayName("Test der 'get12OrException' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_OR_EXCEPTION_12, nullValues = "{null}")
	void test_get12OrException(final Integer result, final int key1, final int key2) {
		if (result == null)
			assertThrows(DeveloperNotificationException.class, () -> {
				map.get12OrException(key1, key2);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.get12OrException(key1, key2).size());
			});
	}

	/**
	 * Test der 'removeValueOrException' Methode.
	 *
	 * @param sizeVorher      Die Listengröße vorher.
	 * @param key1            Der 1. Schlüssel.
	 * @param key2            Der 2. Schlüssel.
	 * @param value           Der zu entfernende Wert.
	 * @param resultErwartet  Der nach dem Entfernen erwartete Wert ({null} falls leer).
	 */
	@DisplayName("Test der 'removeValueOrException' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_REMOVE_VALUE_OR_EXCEPTION, nullValues = "{null}")
	void test_removeValueOrException(final int sizeVorher, final int key1, final int key2, final String value, final String resultErwartet) {
	    assertEquals(sizeVorher, map.get12(key1, key2).size());
	    map.removeValueOrException(key1, key2, value);
	    if (resultErwartet == null)
	        assertEquals(0, map.get12(key1, key2).size());
	    else
	        assertEquals(resultErwartet, map.get12(key1, key2).getFirst());
	}

	/**
	 * Test der 'removeSingleOrException' Methode.
	 *
	 * @param sizeVorher      Die Listengröße vorher.
	 * @param key1            Der 1. Schlüssel.
	 * @param key2            Der 2. Schlüssel.
	 * @param value           Der einzig verbleibende Wert.
	 */
	@DisplayName("Test der 'removeSingleOrException' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_REMOVE_SINGLE_OR_EXCEPTION, nullValues = "{null}")
	void test_removeSingleOrException(final int sizeVorher, final int key1, final int key2, final String value) {
	    assertEquals(sizeVorher, map.get12(key1, key2).size());
	    assertEquals(value, map.removeSingleOrException(key1, key2));
	    assertEquals(0, map.get12(key1, key2).size());
	}

	/**
	 * Test der 'removeAllByKey1' Methode.
	 *
	 * @param key1   Der 1. Schlüssel, dessen Zuordnungen entfernt werden.
	 */
	@DisplayName("Test der 'removeAllByKey1' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_REMOVE_ALL_BY_KEY1)
	void test_removeAllByKey1(final int key1) {
	    map.removeAllByKey1(key1);
	    // Prüfe, dass zu diesem key1 keine Zuordnung mehr existiert
	    for (int k2 = 1; k2 <= 4; k2++)
	        assertEquals(0, map.get12(key1, k2).size());
	}

	/**
	 * Test der 'getAllValues' Methode.
	 */
	@DisplayName("Test der 'getAllValues' Methode.")
	@org.junit.jupiter.api.Test
	void test_getAllValues() {
	    // Nach Setup: "A", "B", "C", "D"
	    assertEquals(4, map.getAllValues().size());
	    assert (map.getAllValues().containsAll(java.util.List.of("A", "B", "C", "D")));
	    // Nach Entfernen aller mit key1=1: nur "A", "D" bleiben übrig
	    map.removeAllByKey1(1);
	    assertEquals(2, map.getAllValues().size());
	    assert (map.getAllValues().containsAll(java.util.List.of("A", "D")));
	}

	@DisplayName("Test remove auf leerer oder nie existenter Liste: Caches korrekt, get12 liefert leere Liste")
	@ParameterizedTest
	@CsvSource(
	    textBlock = TEST_REMOVE_AND_GET_EMPTY_OR_MISSING,
	    nullValues = "{null}"
	)
	void test_remove_on_empty_or_missing_path(final int key1, final int key2, final String addValue, final String action, final boolean existedBefore, final int expectedAfter) {
	    // 1. Falls addValue gesetzt ist, Wert hinzufügen
	    if (addValue != null && !"remove".equals(addValue)) {
	        map.add(key1, key2, addValue.replace("\"", ""));
	    }
	    // 2. Vorher: existiert der Pfad?
	    boolean existed = map.containsKey12(key1, key2);
	    assertEquals(existedBefore, existed);

	    // 3. remove auf Pfad (auch wenn schon leer oder nie da)
	    map.remove(key1, key2);

	    // 4. Danach: get12 ist niemals null, sondern eine (neue) leere Liste
	    List<String> afterList = map.get12(key1, key2);
	    assertNotNull(afterList);
	    assertEquals(expectedAfter, afterList.size());

	    // 5. Nach erneutem add sind Werte sofort wieder sichtbar
	    map.add(key1, key2, "Z");
	    List<String> newList = map.get12(key1, key2);
	    assertEquals(1, newList.size());
	    assertEquals("Z", newList.getFirst());
	}




}
