package de.svws_nrw.core.adt.map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;

/**
 * Diese Klasse testet die Funktionen der Klasse {@link ListMap2DLongKeys}.
 */
class TestListMap2DLongKeys {

	private static final ListMap2DLongKeys<String> MAP = new ListMap2DLongKeys<>();

	private static final String TEST_CONTAINSKEY_1 = """
			true, 1
			true, 2
			true, 3
			false, 4
		""";

	private static final String TEST_CONTAINSKEY_2 = """
			true, 4
			true, 3
			true, 5
			true, 7
			false, 1
		""";

	private static final String TEST_CONTAINSKEY_12 = """
			true, 1, 4
			true, 2, 3
			true, 2, 5
			true, 3, 7
			false, 1, 5
		""";

	private static final String TEST_GET_1 = """
			1, 1
			2, 2
			0, 3
			0, 4
		""";

	private static final String TEST_GET_2 = """
			1, 4
			1, 3
			1, 5
			0, 7
		""";

	private static final String TEST_GET_12 = """
			1, 1, 4
			1, 2, 3
			1, 2, 5
			0, 3, 7
		""";

	private static final String TEST_GET_SINGLE_OR_NULL_1 = """
			A, 1
			{null}, 2
			{null}, 3
			{null}, 4
		""";

	private static final String TEST_GET_SINGLE_OR_NULL_2 = """
			A, 4
			B, 3
			C, 5
			{null}, 7
			{null}, 8
		""";

	private static final String TEST_GET_SINGLE_OR_NULL_12 = """
			A, 1, 4
			B, 2, 3
			C, 2, 5
			{null}, 3, 7
			{null}, 1, 8
		""";


	private static final String TEST_GET_SINGLE_OR_EXCEPTION_1 = """
			A, 1
			{null}, 2
			{null}, 3
			{null}, 4
		""";

	private static final String TEST_GET_SINGLE_OR_EXCEPTION_2 = """
			A, 4
			B, 3
			C, 5
			{null}, 7
			{null}, 8
		""";

	private static final String TEST_GET_SINGLE_OR_EXCEPTION_12 = """
			A, 1, 4
			B, 2, 3
			C, 2, 5
			{null}, 3, 7
			{null}, 1, 8
		""";

	private static final String TEST_KEYSET_1 = """
			3
		""";

	private static final String TEST_KEYSET_2 = """
			4
		""";

	private static final String TEST_KEYSET_12 = """
			4
		""";

	/**
	 * Initialisiert die Grunddaten.
	 */
	@BeforeAll
	static void setup() {
		MAP.add(1, 4, "A");
		MAP.add(2, 3, "B");
		MAP.add(2, 5, "C");
		MAP.addEmpty(3, 7);
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
		assertEquals(result, MAP.containsKey1(key1));
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
		assertEquals(result, MAP.containsKey2(key2));
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
		assertEquals(result, MAP.containsKey12(key1, key2));
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
		assertEquals(size, MAP.get1(key1).size());
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
		assertEquals(size, MAP.get2(key2).size());
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
		assertEquals(size, MAP.get12(key1, key2).size());
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
		assertEquals(result, MAP.getSingleOrNull1(key1));
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
		assertEquals(result, MAP.getSingleOrNull2(key2));
	}

	/**
	 * Test der 'getSingleOrNull12' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key1      Der 1. Schlüssel.
	 * @param key2      Der 2. Schlüssel.
	 */
	@DisplayName("Test der 'getSingleOrNull2' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_SINGLE_OR_NULL_12, nullValues = "{null}")
	void test_getSingleOrNull12(final String result, final int key1, final int key2) {
		assertEquals(result, MAP.getSingleOrNull12(key1, key2));
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
				MAP.getSingleOrException1(key1);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, MAP.getSingleOrException1(key1));
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
				MAP.getSingleOrException2(key2);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, MAP.getSingleOrException2(key2));
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
				MAP.getSingleOrException12(key1, key2);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, MAP.getSingleOrException12(key1, key2));
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
		assertEquals(size, MAP.keySet1().size());
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
		assertEquals(size, MAP.keySet2().size());
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
		assertEquals(size, MAP.keySet12().size());
	}
}
