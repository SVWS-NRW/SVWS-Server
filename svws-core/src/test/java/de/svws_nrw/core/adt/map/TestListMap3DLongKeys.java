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

	private static final String TEST_CONTAINSKEY_3 = """
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
			false, 5, 5
		""";

	private static final String TEST_CONTAINSKEY_13 = """
			true, 2, 1
			true, 1, 2
			true, 1, 1
			true, 3, 3
			true, 4, 4
			false, 5, 5
		""";

	private static final String TEST_CONTAINSKEY_23 = """
			true, 2, 1
			true, 1, 2
			true, 1, 1
			true, 3, 3
			true, 4, 4
			false, 5, 5
		""";

	private static final String TEST_CONTAINSKEY_123 = """
			true, 2, 1, 1
			true, 1, 2, 1
			true, 1, 1, 2
			true, 3, 3, 3
			true, 4, 4, 4
			false, 5, 5, 5
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

	private static final String TEST_GET_3 = """
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

	private static final String TEST_GET_13 = """
			1, 2, 1
			1, 1, 2
			1, 1, 1
			1, 3, 3
			0, 4, 4
			0, 5, 5
		""";

	private static final String TEST_GET_23 = """
			1, 2, 1
			1, 1, 2
			1, 1, 1
			1, 3, 3
			0, 4, 4
			0, 5, 5
		""";

	private static final String TEST_GET_123 = """
			1, 2, 1, 1
			1, 1, 2, 1
			1, 1, 1, 2
			1, 3, 3, 3
			0, 4, 4, 4
			0, 5, 5, 5
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

	private static final String TEST_GET_SINGLE_OR_NULL_3 = """
			{null}, 1
			C, 2
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

	private static final String TEST_GET_SINGLE_OR_NULL_13 = """
			A, 2, 1
			B, 1, 1
			C, 1, 2
			D, 3, 3
			{null}, 4, 4
			{null}, 5, 5
		""";

	private static final String TEST_GET_SINGLE_OR_NULL_23 = """
			A, 1, 1
			B, 2, 1
			C, 1, 2
			D, 3, 3
			{null}, 4, 4
			{null}, 5, 5
		""";

	private static final String TEST_GET_SINGLE_OR_NULL_123 = """
			A, 2, 1, 1
			B, 1, 2, 1
			C, 1, 1, 2
			D, 3, 3, 3
			{null}, 4, 4, 4
			{null}, 5, 5, 5
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

	private static final String TEST_GET_SINGLE_OR_EXCEPTION_3 = """
			{null}, 1
			C, 2
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

	private static final String TEST_GET_SINGLE_OR_EXCEPTION_13 = """
			A, 2, 1
			B, 1, 1
			C, 1, 2
			D, 3, 3
			{null}, 4, 4
			{null}, 5, 5
		""";

	private static final String TEST_GET_SINGLE_OR_EXCEPTION_23 = """
			A, 1, 1
			B, 2, 1
			C, 1, 2
			D, 3, 3
			{null}, 4, 4
			{null}, 5, 5
		""";

	private static final String TEST_GET_SINGLE_OR_EXCEPTION_123 = """
			A, 2, 1, 1
			B, 1, 2, 1
			C, 1, 1, 2
			D, 3, 3, 3
			{null}, 4, 4, 4
			{null}, 5, 5, 5
		""";

	private static final String TEST_KEYSET_1 = """
			4
		""";

	private static final String TEST_KEYSET_2 = """
			4
		""";

	private static final String TEST_KEYSET_3 = """
			4
		""";

	private static final String TEST_KEYSET_12 = """
			5
		""";

	private static final String TEST_KEYSET_13 = """
			5
		""";

	private static final String TEST_KEYSET_23 = """
			5
		""";

	private static final String TEST_KEYSET_123 = """
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

	private static final String TEST_GET_OR_EXCEPTION_3 = """
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

	private static final String TEST_GET_OR_EXCEPTION_13 = """
			1, 2, 1
			1, 1, 2
			1, 1, 1
			1, 3, 3
			0, 4, 4
			{null}, 5, 5
		""";

	private static final String TEST_GET_OR_EXCEPTION_23 = """
			1, 2, 1
			1, 1, 2
			1, 1, 1
			1, 3, 3
			0, 4, 4
			{null}, 5, 5
		""";

	private static final String TEST_GET_OR_EXCEPTION_123 = """
			1, 2, 1, 1
			1, 1, 2, 1
			1, 1, 1, 2
			1, 3, 3, 3
			0, 4, 4, 4
			{null}, 5, 5, 5
		""";

	private ListMap3DLongKeys<String> map = new ListMap3DLongKeys<>();

	/**
	 * Initialisiert die Grunddaten.
	 */
	@BeforeEach
	void setup() {
		map = new ListMap3DLongKeys<>();
		map.add(2, 1, 1, "A");
		map.add(1, 2, 1, "B");
		map.add(1, 1, 2, "C");
		map.add(3, 3, 3, "D");
		map.addEmpty(4, 4, 4);
	}

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
			map.add(key1, key1, key1, "X");
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
			map.add(key2, key2, key2, "X");
			assertEquals(true, map.containsKey2(key2));
		}
	}

	/**
	 * Test der 'containsKey3' Methode.
	 *
	 * @param key3      Der 3. Schlüssel.
	 * @param result    Das erwartete Resultat.
	 */
	@DisplayName("Test der 'containsKey3' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_CONTAINSKEY_3)
	void test_containsKey3(final boolean result, final int key3) {
		assertEquals(result, map.containsKey3(key3));

		if (!result) {
			map.add(key3, key3, key3, "X");
			assertEquals(true, map.containsKey3(key3));
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
			map.add(key1, key2, 99, "X");
			assertEquals(true, map.containsKey12(key1, key2));
		}
	}

	/**
	 * Test der 'containsKey13' Methode.
	 *
	 * @param key1      Der 1. Schlüssel.
	 * @param key3      Der 3. Schlüssel.
	 * @param result    Das erwartete Resultat.
	 */
	@DisplayName("Test der 'containsKey13' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_CONTAINSKEY_13)
	void test_containsKey13(final boolean result, final int key1, final int key3) {
		assertEquals(result, map.containsKey13(key1, key3));

		if (!result) {
			map.add(key1, 99, key3, "X");
			assertEquals(true, map.containsKey13(key1, key3));
		}
	}

	/**
	 * Test der 'containsKey23' Methode.
	 *
	 * @param key2      Der 2. Schlüssel.
	 * @param key3      Der 3. Schlüssel.
	 * @param result    Das erwartete Resultat.
	 */
	@DisplayName("Test der 'containsKey23' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_CONTAINSKEY_23)
	void test_containsKey23(final boolean result, final int key2, final int key3) {
		assertEquals(result, map.containsKey23(key2, key3));

		if (!result) {
			map.add(99, key2, key3, "X");
			assertEquals(true, map.containsKey23(key2, key3));
		}
	}

	/**
	 * Test der 'containsKey123' Methode.
	 *
	 * @param key1      Der 1. Schlüssel.
	 * @param key2      Der 2. Schlüssel.
	 * @param key3      Der 3. Schlüssel.
	 * @param result    Das erwartete Resultat.
	 */
	@DisplayName("Test der 'containsKey123' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_CONTAINSKEY_123)
	void test_containsKey123(final boolean result, final int key1, final int key2, final int key3) {
		assertEquals(result, map.containsKey123(key1, key2, key3));

		if (!result) {
			map.add(key1, key2, key3, "X");
			assertEquals(true, map.containsKey123(key1, key2, key3));
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

		map.addEmpty(key1, 99, 99);
		assertEquals(size, map.get1(key1).size());

		map.add(key1, 99, 99, "X");
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

		map.addEmpty(99, key2, 99);
		assertEquals(size, map.get2(key2).size());

		map.add(99, key2, 99, "X");
		assertEquals(size + 1, map.get2(key2).size());
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

		map.addEmpty(99, 99, key3);
		assertEquals(size, map.get3(key3).size());

		map.add(99, 99, key3, "X");
		assertEquals(size + 1, map.get3(key3).size());
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

		map.addEmpty(key1, key2, 99);
		assertEquals(size, map.get12(key1, key2).size());

		map.add(key1, key2, 99, "X");
		assertEquals(size + 1, map.get12(key1, key2).size());
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

		map.addEmpty(key1, 99, key3);
		assertEquals(size, map.get13(key1, key3).size());

		map.add(key1, 99, key3, "X");
		assertEquals(size + 1, map.get13(key1, key3).size());
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

		map.addEmpty(99, key2, key3);
		assertEquals(size, map.get23(key2, key3).size());

		map.add(99, key2, key3, "X");
		assertEquals(size + 1, map.get23(key2, key3).size());
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

		map.addEmpty(key1, key2, key3);
		assertEquals(size, map.get123(key1, key2, key3).size());

		map.add(key1, key2, key3, "X");
		assertEquals(size + 1, map.get123(key1, key2, key3).size());
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
		assertEquals(result, map.getSingle1OrNull(key1));

		map.addEmpty(66, 66, 66);
		assertEquals(result, map.getSingle1OrNull(key1));

		map.add(77, 77, 77, "X");
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

		map.addEmpty(66, 66, 66);
		assertEquals(result, map.getSingle2OrNull(key2));

		map.add(77, 77, 77, "X");
		assertEquals(result, map.getSingle2OrNull(key2));
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
		assertEquals(result, map.getSingle3OrNull(key3));

		map.addEmpty(66, 66, 66);
		assertEquals(result, map.getSingle3OrNull(key3));

		map.add(77, 77, 77, "X");
		assertEquals(result, map.getSingle3OrNull(key3));
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

		map.addEmpty(66, 66, 66);
		assertEquals(result, map.getSingle12OrNull(key1, key2));

		map.add(77, 77, 77, "X");
		assertEquals(result, map.getSingle12OrNull(key1, key2));
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
		assertEquals(result, map.getSingle13OrNull(key1, key3));

		map.addEmpty(66, 66, 66);
		assertEquals(result, map.getSingle13OrNull(key1, key3));

		map.add(77, 77, 77, "X");
		assertEquals(result, map.getSingle13OrNull(key1, key3));
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
		assertEquals(result, map.getSingle23OrNull(key2, key3));

		map.addEmpty(66, 66, 66);
		assertEquals(result, map.getSingle23OrNull(key2, key3));

		map.add(77, 77, 77, "X");
		assertEquals(result, map.getSingle23OrNull(key2, key3));
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
		assertEquals(result, map.getSingle123OrNull(key1, key2, key3));

		map.addEmpty(66, 66, 66);
		assertEquals(result, map.getSingle123OrNull(key1, key2, key3));

		map.add(77, 77, 77, "X");
		assertEquals(result, map.getSingle123OrNull(key1, key2, key3));
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
				map.getSingle3OrException(key3);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.getSingle3OrException(key3));
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
				map.getSingle13OrException(key1, key3);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.getSingle13OrException(key1, key3));
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
				map.getSingle23OrException(key2, key3);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.getSingle23OrException(key2, key3));
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
				map.getSingle123OrException(key1, key2, key3);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.getSingle123OrException(key1, key2, key3));
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

		map.addEmpty(66, 66, 66);
		assertEquals(size + 1, map.keySet1().size());

		map.add(77, 77, 77, "X");
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

		map.addEmpty(66, 66, 66);
		assertEquals(size + 1, map.keySet2().size());

		map.add(77, 77, 77, "X");
		assertEquals(size + 2, map.keySet2().size());
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

		map.addEmpty(66, 66, 66);
		assertEquals(size + 1, map.keySet3().size());

		map.add(77, 77, 77, "X");
		assertEquals(size + 2, map.keySet3().size());
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

		map.addEmpty(66, 66, 66);
		assertEquals(size + 1, map.keySet12().size());

		map.add(77, 77, 77, "X");
		assertEquals(size + 2, map.keySet12().size());
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

		map.addEmpty(66, 66, 66);
		assertEquals(size + 1, map.keySet13().size());

		map.add(77, 77, 77, "X");
		assertEquals(size + 2, map.keySet13().size());
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

		map.addEmpty(66, 66, 66);
		assertEquals(size + 1, map.keySet23().size());

		map.add(77, 77, 77, "X");
		assertEquals(size + 2, map.keySet23().size());
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

		map.addEmpty(66, 66, 66);
		assertEquals(size + 1, map.keySet123().size());

		map.add(77, 77, 77, "X");
		assertEquals(size + 2, map.keySet123().size());
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
	 * Test der 'get3OrException' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key3      Der 3. Schlüssel.
	 */
	@DisplayName("Test der 'get3OrException' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_OR_EXCEPTION_3, nullValues = "{null}")
	void test_get3OrException(final Integer result, final int key3) {
		if (result == null)
			assertThrows(DeveloperNotificationException.class, () -> {
				map.get3OrException(key3);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.get3OrException(key3).size());
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
	 * Test der 'get13OrException' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key1      Der 1. Schlüssel.
	 * @param key3      Der 3. Schlüssel.
	 */
	@DisplayName("Test der 'get13OrException' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_OR_EXCEPTION_13, nullValues = "{null}")
	void test_get13OrException(final Integer result, final int key1, final int key3) {
		if (result == null)
			assertThrows(DeveloperNotificationException.class, () -> {
				map.get13OrException(key1, key3);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.get13OrException(key1, key3).size());
			});
	}

	/**
	 * Test der 'get23OrException' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key2      Der 2. Schlüssel.
	 * @param key3      Der 3. Schlüssel.
	 */
	@DisplayName("Test der 'get23OrException' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_OR_EXCEPTION_23, nullValues = "{null}")
	void test_get23OrException(final Integer result, final int key2, final int key3) {
		if (result == null)
			assertThrows(DeveloperNotificationException.class, () -> {
				map.get23OrException(key2, key3);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.get23OrException(key2, key3).size());
			});
	}

	/**
	 * Test der 'get123OrException' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key1      Der 1. Schlüssel.
	 * @param key2      Der 2. Schlüssel.
	 * @param key3      Der 3. Schlüssel.
	 */
	@DisplayName("Test der 'get123OrException' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_OR_EXCEPTION_123, nullValues = "{null}")
	void test_get123OrException(final Integer result, final int key1, final int key2, final int key3) {
		if (result == null)
			assertThrows(DeveloperNotificationException.class, () -> {
				map.get123OrException(key1, key2, key3);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.get123OrException(key1, key2, key3).size());
			});
	}

}
