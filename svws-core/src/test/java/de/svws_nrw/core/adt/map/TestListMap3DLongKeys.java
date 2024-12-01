package de.svws_nrw.core.adt.map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;

/**
 * Diese Klasse testet die Funktionen der Klasse {@link ListMap3DLongKeys}.
 */
class TestListMap3DLongKeys {

	private static final String TEST_CONTAINSKEY_1 = """
			true, 1
			true, 2
			true, 7
			true, 8
			true, 9
			false, 10
		""";

	private static final String TEST_CONTAINSKEY_2 = """
			true, 2
			true, 4
			true, 1
			true, 6
			true, 9
			false, 7
		""";

	private static final String TEST_CONTAINSKEY_3 = """
			true, 3
			true, 2
			true, 5
			true, 1
			true, 9
			false, 7
		""";

	private static final String TEST_CONTAINSKEY_12 = """
			true, 1, 2
			true, 1, 4
			true, 2, 1
			true, 2, 6
			true, 7, 1
			true, 8, 2
			true, 9, 9
			false, 7, 2
			false, 5, 5
		""";

	private static final String TEST_CONTAINSKEY_13 = """
			true, 1, 3
			true, 1, 2
			true, 2, 5
			true, 2, 1
			true, 7, 2
			true, 8, 1
			true, 9, 9
			false, 7, 1
			false, 4, 4
		""";

	private static final String TEST_CONTAINSKEY_23 = """
			true, 2, 3
			true, 4, 2
			true, 1, 5
			true, 6, 1
			true, 1, 2
			true, 2, 1
			true, 9, 9
			false, 6, 2
			false, 7, 7
		""";

	private static final String TEST_CONTAINSKEY_123 = """
			true, 1, 2, 3
			true, 1, 4, 2
			true, 2, 1, 5
			true, 2, 6, 1
			true, 7, 1, 2
			true, 8, 2, 1
			true, 9, 9, 9
			false, 7, 1, 3
			false, 8, 3, 4
			false, 5, 5, 6
		""";

	private static final String TEST_GET_1 = """
			2, 1
			2, 2
			0, 3
			0, 4
			0, 5
			0, 6
			1, 7
			1, 8
			0, 9
		""";

	private static final String TEST_GET_2 = """
			2, 1
			2, 2
			0, 3
			1, 4
			0, 5
			1, 6
			0, 7
			0, 8
			0, 9
		""";

	private static final String TEST_GET_3 = """
			2, 1
			2, 2
			1, 3
			0, 4
			1, 5
			0, 6
			0, 7
			0, 8
			0, 9
		""";

	private static final String TEST_GET_12 = """
			1, 1, 2
			1, 1, 4
			1, 2, 1
			1, 2, 6
			1, 7, 1
			1, 8, 2
			0, 9, 9
			0, 7, 2
		""";

	private static final String TEST_GET_13 = """
			1, 1, 3
			1, 1, 2
			1, 2, 5
			1, 2, 1
			1, 7, 2
			1, 8, 1
			0, 9, 9
			0, 7, 3
		""";

	private static final String TEST_GET_23 = """
			1, 2, 3
			1, 4, 2
			1, 1, 5
			1, 6, 1
			1, 1, 2
			1, 2, 1
			0, 9, 9
			0, 6, 2
		""";

	private static final String TEST_GET_123 = """
			1, 1, 2, 3
			1, 1, 4, 2
			1, 2, 1, 5
			1, 2, 6, 1
			1, 7, 1, 2
			1, 8, 2, 1
			0, 9, 9, 9
			0, 7, 1, 3
			0, 4, 4, 4
		""";

	private static final String TEST_GET_SINGLE_OR_NULL_1 = """
			{null}, 1
			{null}, 2
			E, 7
			F, 8
			{null}, 9
			{null}, 4
		""";

	private static final String TEST_GET_SINGLE_OR_NULL_2 = """
			{null}, 2
			B, 4
			{null}, 1
			D, 6
			{null}, 9
			{null}, 7
		""";

	private static final String TEST_GET_SINGLE_OR_NULL_3 = """
			A, 3
			{null}, 2
			C, 5
			{null}, 1
			{null}, 9
			{null}, 4
		""";

	private static final String TEST_GET_SINGLE_OR_NULL_12 = """
			A, 1, 2
			B, 1, 4
			C, 2, 1
			D, 2, 6
			E, 7, 1
			F, 8, 2
			{null}, 9, 9
			{null}, 8, 3
			{null}, 4, 4
		""";

	private static final String TEST_GET_SINGLE_OR_NULL_13 = """
			A, 1, 3
			B, 1, 2
			C, 2, 5
			D, 2, 1
			E, 7, 2
			F, 8, 1
			{null}, 9, 9
			{null}, 8, 3
			{null}, 4, 4
		""";

	private static final String TEST_GET_SINGLE_OR_NULL_23 = """
			A, 2, 3
			B, 4, 2
			C, 1, 5
			D, 6, 1
			E, 1, 2
			F, 2, 1
			{null}, 9, 9
			{null}, 2, 4
			{null}, 7, 7
		""";

	private static final String TEST_GET_SINGLE_OR_NULL_123 = """
			A, 1, 2, 3
			B, 1, 4, 2
			C, 2, 1, 5
			D, 2, 6, 1
			E, 7, 1, 2
			F, 8, 2, 1
			{null}, 9, 9, 9
			{null}, 2, 1, 6
			{null}, 2, 7, 3
			{null}, 6, 3, 4
		""";

	private static final String TEST_GET_SINGLE_OR_EXCEPTION_1 = """
			{null}, 1
			{null}, 2
			E, 7
			F, 8
			{null}, 9
			{null}, 4
		""";

	private static final String TEST_GET_SINGLE_OR_EXCEPTION_2 = """
			{null}, 2
			B, 4
			{null}, 1
			D, 6
			{null}, 9
			{null}, 7
		""";

	private static final String TEST_GET_SINGLE_OR_EXCEPTION_3 = """
			A, 3
			{null}, 2
			C, 5
			{null}, 1
			{null}, 9
			{null}, 4
		""";

	private static final String TEST_GET_SINGLE_OR_EXCEPTION_12 = """
			A, 1, 2
			B, 1, 4
			C, 2, 1
			D, 2, 6
			E, 7, 1
			F, 8, 2
			{null}, 9, 9
			{null}, 8, 3
			{null}, 4, 4
		""";

	private static final String TEST_GET_SINGLE_OR_EXCEPTION_13 = """
			A, 1, 3
			B, 1, 2
			C, 2, 5
			D, 2, 1
			E, 7, 2
			F, 8, 1
			{null}, 9, 9
			{null}, 8, 3
			{null}, 4, 4
		""";

	private static final String TEST_GET_SINGLE_OR_EXCEPTION_23 = """
			A, 2, 3
			B, 4, 2
			C, 1, 5
			D, 6, 1
			E, 1, 2
			F, 2, 1
			{null}, 9, 9
			{null}, 2, 4
			{null}, 7, 7
		""";

	private static final String TEST_GET_SINGLE_OR_EXCEPTION_123 = """
			A, 1, 2, 3
			B, 1, 4, 2
			C, 2, 1, 5
			D, 2, 6, 1
			E, 7, 1, 2
			F, 8, 2, 1
			{null}, 9, 9, 9
			{null}, 2, 1, 6
			{null}, 2, 7, 3
			{null}, 6, 3, 4
		""";


	private static final String TEST_KEYSET_1 = """
			5
		""";

	private static final String TEST_KEYSET_2 = """
			5
		""";

	private static final String TEST_KEYSET_3 = """
			5
		""";

	private static final String TEST_KEYSET_12 = """
			7
		""";

	private static final String TEST_KEYSET_13 = """
			7
		""";

	private static final String TEST_KEYSET_23 = """
			7
		""";

	private static final String TEST_KEYSET_123 = """
			7
		""";

	private ListMap3DLongKeys<String> map = new ListMap3DLongKeys<>();

	/**
	 * Initialisiert die Grunddaten.
	 */
	@BeforeEach
	void setup() {
		map = new ListMap3DLongKeys<>();
		map.add(1, 2, 3, "A");
		map.add(1, 4, 2, "B");
		map.add(2, 1, 5, "C");
		map.add(2, 6, 1, "D");
		map.add(7, 1, 2, "E");
		map.add(8, 2, 1, "F");
		map.addEmpty(9, 9, 9);
	}

	/**
	 * Test der 'containsKey1' Methode.
	 *
	 * @param result    Das erwartete Resultat.
	 * @param key1      Der 1. Schlüssel.
	 */
	@DisplayName("Test der 'containsKey1' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_CONTAINSKEY_1)
	void test_containsKey1(final boolean result, final int key1) {
		assertEquals(result, map.containsKey1(key1));
	}

	/**
	 * Test der 'containsKey2' Methode.
	 *
	 * @param result    Das erwartete Resultat.
	 * @param key2      Der 2. Schlüssel.
	 */
	@DisplayName("Test der 'containsKey2' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_CONTAINSKEY_2)
	void test_containsKey2(final boolean result, final int key2) {
		assertEquals(result, map.containsKey2(key2));
	}

	/**
	 * Test der 'containsKey3' Methode.
	 *
	 * @param result    Das erwartete Resultat.
	 * @param key3      Der 3. Schlüssel.
	 */
	@DisplayName("Test der 'containsKey3' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_CONTAINSKEY_3)
	void test_containsKey3(final boolean result, final int key3) {
		assertEquals(result, map.containsKey3(key3));
	}

	/**
	 * Test der 'containsKey12' Methode.
	 *
	 * @param result    Das erwartete Resultat.
	 * @param key1      Der 1. Schlüssel.
	 * @param key2      Der 2. Schlüssel.
	 */
	@DisplayName("Test der 'containsKey12' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_CONTAINSKEY_12)
	void test_containsKey12(final boolean result, final int key1, final int key2) {
		assertEquals(result, map.containsKey12(key1, key2));
	}

	/**
	 * Test der 'containsKey13' Methode.
	 *
	 * @param result    Das erwartete Resultat.
	 * @param key1      Der 1. Schlüssel.
	 * @param key3      Der 3. Schlüssel.
	 */
	@DisplayName("Test der 'containsKey13' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_CONTAINSKEY_13)
	void test_containsKey13(final boolean result, final int key1, final int key3) {
		assertEquals(result, map.containsKey13(key1, key3));
	}

	/**
	 * Test der 'containsKey23' Methode.
	 *
	 * @param result    Das erwartete Resultat.
	 * @param key2      Der 2. Schlüssel.
	 * @param key3      Der 3. Schlüssel.
	 */
	@DisplayName("Test der 'containsKey23' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_CONTAINSKEY_23)
	void test_containsKey23(final boolean result, final int key2, final int key3) {
		assertEquals(result, map.containsKey23(key2, key3));
	}

	/**
	 * Test der 'containsKey123' Methode.
	 *
	 * @param result    Das erwartete Resultat.
	 * @param key1      Der 1. Schlüssel.
	 * @param key2      Der 2. Schlüssel.
	 * @param key3      Der 3. Schlüssel.
	 */
	@DisplayName("Test der 'containsKey123' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_CONTAINSKEY_123)
	void test_containsKey123(final boolean result, final int key1, final int key2, final int key3) {
		assertEquals(result, map.containsKey123(key1, key2, key3));
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
	}

	/**
	 * Test der 'get3' Methode.
	 *
	 * @param size      Die erwartete Listengröße.
	 * @param key3      Der 3. Schlüssel.
	 */
	@DisplayName("Test der 'get3' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_3)
	void test_get3(final int size, final int key3) {
		assertEquals(size, map.get3(key3).size());
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
	}

	/**
	 * Test der 'get13' Methode.
	 *
	 * @param size      Die erwartete Listengröße.
	 * @param key1      Der 1. Schlüssel.
	 * @param key3      Der 3. Schlüssel.
	 */
	@DisplayName("Test der 'get13' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_13)
	void test_get13(final int size, final int key1, final int key3) {
		assertEquals(size, map.get13(key1, key3).size());
	}

	/**
	 * Test der 'get23' Methode.
	 *
	 * @param size      Die erwartete Listengröße.
	 * @param key2      Der 2. Schlüssel.
	 * @param key3      Der 3. Schlüssel.
	 */
	@DisplayName("Test der 'get23' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_23)
	void test_get23(final int size, final int key2, final int key3) {
		assertEquals(size, map.get23(key2, key3).size());
	}

	/**
	 * Test der 'get123' Methode.
	 *
	 * @param size      Die erwartete Listengröße.
	 * @param key1      Der 1. Schlüssel.
	 * @param key2      Der 2. Schlüssel.
	 * @param key3      Der 3. Schlüssel.
	 */
	@DisplayName("Test der 'get123' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_123)
	void test_get123(final int size, final int key1, final int key2, final int key3) {
		assertEquals(size, map.get123(key1, key2, key3).size());
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
	void test_getSingleOrNull1(final String result, final int key1) {
		assertEquals(result, map.getSingleOrNull1(key1));
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
		assertEquals(result, map.getSingleOrNull2(key2));
	}

	/**
	 * Test der 'getSingleOrNull3' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key3      Der 3. Schlüssel.
	 */
	@DisplayName("Test der 'getSingleOrNull3' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_SINGLE_OR_NULL_3, nullValues = "{null}")
	void test_getSingleOrNull3(final String result, final int key3) {
		assertEquals(result, map.getSingleOrNull3(key3));
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
		assertEquals(result, map.getSingleOrNull12(key1, key2));
	}

	/**
	 * Test der 'getSingleOrNull13' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key1      Der 1. Schlüssel.
	 * @param key3      Der 3. Schlüssel.
	 */
	@DisplayName("Test der 'getSingleOrNull13' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_SINGLE_OR_NULL_13, nullValues = "{null}")
	void test_getSingleOrNull13(final String result, final int key1, final int key3) {
		assertEquals(result, map.getSingleOrNull13(key1, key3));
	}

	/**
	 * Test der 'getSingleOrNull23' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key2      Der 2. Schlüssel.
	 * @param key3      Der 3. Schlüssel.
	 */
	@DisplayName("Test der 'getSingleOrNull23' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_SINGLE_OR_NULL_23, nullValues = "{null}")
	void test_getSingleOrNull23(final String result, final int key2, final int key3) {
		assertEquals(result, map.getSingleOrNull23(key2, key3));
	}

	/**
	 * Test der 'getSingleOrNull123' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key1      Der 1. Schlüssel.
	 * @param key2      Der 2. Schlüssel.
	 * @param key3      Der 3. Schlüssel.
	 */
	@DisplayName("Test der 'getSingleOrNull123' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_SINGLE_OR_NULL_123, nullValues = "{null}")
	void test_getSingleOrNull123(final String result, final int key1, final int key2, final int key3) {
		assertEquals(result, map.getSingleOrNull123(key1, key2, key3));
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
				map.getSingleOrException1(key1);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.getSingleOrException1(key1));
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
				map.getSingleOrException2(key2);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.getSingleOrException2(key2));
			});
	}

	/**
	 * Test der 'getSingleOrException3' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key3      Der 3. Schlüssel.
	 */
	@DisplayName("Test der 'getSingleOrException3' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_SINGLE_OR_EXCEPTION_3, nullValues = "{null}")
	void test_getSingleOrException3(final String result, final int key3) {
		if (result == null)
			assertThrows(DeveloperNotificationException.class, () -> {
				map.getSingleOrException3(key3);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.getSingleOrException3(key3));
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
				map.getSingleOrException12(key1, key2);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.getSingleOrException12(key1, key2));
			});
	}

	/**
	 * Test der 'getSingleOrException13' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key1      Der 1. Schlüssel.
	 * @param key3      Der 3. Schlüssel.
	 */
	@DisplayName("Test der 'getSingleOrException13' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_SINGLE_OR_EXCEPTION_13, nullValues = "{null}")
	void test_getSingleOrException13(final String result, final int key1, final int key3) {
		if (result == null)
			assertThrows(DeveloperNotificationException.class, () -> {
				map.getSingleOrException13(key1, key3);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.getSingleOrException13(key1, key3));
			});
	}

	/**
	 * Test der 'getSingleOrException23' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key2      Der 2. Schlüssel.
	 * @param key3      Der 3. Schlüssel.
	 */
	@DisplayName("Test der 'getSingleOrException23' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_SINGLE_OR_EXCEPTION_23, nullValues = "{null}")
	void test_getSingleOrException23(final String result, final int key2, final int key3) {
		if (result == null)
			assertThrows(DeveloperNotificationException.class, () -> {
				map.getSingleOrException23(key2, key3);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.getSingleOrException23(key2, key3));
			});
	}

	/**
	 * Test der 'getSingleOrException123' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key1      Der 1. Schlüssel.
	 * @param key2      Der 2. Schlüssel.
	 * @param key3      Der 3. Schlüssel.
	 */
	@DisplayName("Test der 'getSingleOrException123' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_SINGLE_OR_EXCEPTION_123, nullValues = "{null}")
	void test_getSingleOrException123(final String result, final int key1, final int key2, final int key3) {
		if (result == null)
			assertThrows(DeveloperNotificationException.class, () -> {
				map.getSingleOrException123(key1, key2, key3);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.getSingleOrException123(key1, key2, key3));
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
	}

	/**
	 * Test der 'keySet3' Methode.
	 *
	 * @param size      Die erwartete Größe des Keysets3.
	 */
	@DisplayName("Test der 'keySet3' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_KEYSET_3)
	void test_keySet3(final int size) {
		assertEquals(size, map.keySet3().size());
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
	}

	/**
	 * Test der 'keySet13' Methode.
	 *
	 * @param size      Die erwartete Größe des Keysets13.
	 */
	@DisplayName("Test der 'keySet13' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_KEYSET_13)
	void test_keySet13(final int size) {
		assertEquals(size, map.keySet13().size());
	}

	/**
	 * Test der 'keySet23' Methode.
	 *
	 * @param size      Die erwartete Größe des Keysets23.
	 */
	@DisplayName("Test der 'keySet23' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_KEYSET_23)
	void test_keySet23(final int size) {
		assertEquals(size, map.keySet23().size());
	}

	/**
	 * Test der 'keySet123' Methode.
	 *
	 * @param size      Die erwartete Größe des Keysets123.
	 */
	@DisplayName("Test der 'keySet123' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_KEYSET_123)
	void test_keySet123(final int size) {
		assertEquals(size, map.keySet123().size());
	}

}
