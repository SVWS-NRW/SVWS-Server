package de.svws_nrw.core.adt.map;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Diese Klasse testet die Funktionen der Klasse {@link ListMap4DLongKeys}.
 */
class TestListMap4DLongKeys {

	private static final ListMap4DLongKeys<String> MAP = new ListMap4DLongKeys<>();

	private static final String TEST_CONTAINS_1 = """
			true, 1
			true, 4
			true, 5
			false, 6
		""";

	private static final String TEST_CONTAINS_2 = """
			true, 2
			true, 4
			true, 5
			false, 6
		""";

	private static final String TEST_CONTAINS_3 = """
			true, 3
			true, 4
			true, 5
			false, 6
		""";

	private static final String TEST_CONTAINS_4 = """
			true, 4
			true, 5
			false, 6
		""";

	private static final String TEST_CONTAINS_12 = """
			true, 1, 2
			true, 1, 4
			true, 4, 4
			true, 5, 5
			false, 1, 3
		""";

	private static final String TEST_CONTAINS_13 = """
			true, 1, 3
			true, 1, 4
			true, 4, 4
			true, 5, 5
			false, 1, 6
		""";

	private static final String TEST_CONTAINS_14 = """
			true, 1, 4
			true, 4, 4
			true, 5, 5
			false, 1, 6
		""";

	private static final String TEST_CONTAINS_23 = """
			true, 2, 3
			true, 2, 4
			true, 4, 4
			true, 5, 5
			false, 2, 6
		""";

	private static final String TEST_CONTAINS_24 = """
			true, 2, 4
			true, 4, 4
			true, 5, 5
			false, 2, 6
		""";

	private static final String TEST_CONTAINS_34 = """
			true, 3, 4
			true, 4, 4
			true, 5, 5
			false, 3, 6
		""";

	private static final String TEST_CONTAINS_123 = """
			true, 1, 2, 3
			true, 1, 2, 4
			true, 1, 4, 4
			true, 4, 4, 4
			true, 5, 5, 5
			false, 1, 2, 6
		""";

	private static final String TEST_CONTAINS_124 = """
			true, 1, 2, 4
			true, 1, 4, 4
			true, 4, 4, 4
			true, 5, 5, 5
			false, 1, 2, 6
		""";

	private static final String TEST_CONTAINS_134 = """
			true, 1, 3, 4
			true, 1, 4, 4
			true, 4, 4, 4
			true, 5, 5, 5
			false, 1, 4, 6
		""";

	private static final String TEST_CONTAINS_234 = """
			true, 2, 3, 4
			true, 2, 4, 4
			true, 4, 4, 4
			true, 5, 5, 5
			false, 2, 3, 6
		""";

	private static final String TEST_CONTAINS_1234 = """
			true, 1, 2, 3, 4
			true, 1, 2, 4, 4
			true, 1, 4, 4, 4
			true, 4, 4, 4, 4
			true, 5, 5, 5, 5
			false, 1, 2, 3, 1
		""";


	/**
	 * Initialisiert die Grunddaten.
	 */
	@BeforeAll
	static void setup() {
		MAP.add(1, 2, 3, 4, "A");
		MAP.add(1, 2, 4, 4, "B");
		MAP.add(1, 4, 4, 4, "C");
		MAP.add(4, 4, 4, 4, "D");
		MAP.addEmpty(5, 5, 5, 5);
	}

	/**
	 * Test der 'containsKey1' Methode.
	 *
	 * @param key1      Der 1. Schlüssel.
	 * @param result    Das erwartete Resultat.
	 */
	@DisplayName("Test der 'containsKey1' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_CONTAINS_1)
	void test_containsKey1(final boolean result, final int key1) {
		assertEquals(result, MAP.containsKey1(key1));
	}

	/**
	 * Test der 'containsKey2' Methode.
	 *
	 * @param key2      Der 2. Schlüssel.
	 * @param result    Das erwartete Resultat.
	 */
	@DisplayName("Test der 'containsKey2' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_CONTAINS_2)
	void test_containsKey2(final boolean result, final int key2) {
		assertEquals(result, MAP.containsKey2(key2));
	}

	/**
	 * Test der 'containsKey3' Methode.
	 *
	 * @param key3      Der 3. Schlüssel.
	 * @param result    Das erwartete Resultat.
	 */
	@DisplayName("Test der 'containsKey3' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_CONTAINS_3)
	void test_containsKey3(final boolean result, final int key3) {
		assertEquals(result, MAP.containsKey3(key3));
	}

	/**
	 * Test der 'containsKey4' Methode.
	 *
	 * @param key4      Der 4. Schlüssel.
	 * @param result    Das erwartete Resultat.
	 */
	@DisplayName("Test der 'containsKey4' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_CONTAINS_4)
	void test_containsKey4(final boolean result, final int key4) {
		assertEquals(result, MAP.containsKey4(key4));
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
	@CsvSource(textBlock = TEST_CONTAINS_12)
	void test_containsKey12(final boolean result, final int key1, final int key2) {
		assertEquals(result, MAP.containsKey12(key1, key2));
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
	@CsvSource(textBlock = TEST_CONTAINS_13)
	void test_containsKey13(final boolean result, final int key1, final int key3) {
		assertEquals(result, MAP.containsKey13(key1, key3));
	}

	/**
	 * Test der 'containsKey14' Methode.
	 *
	 * @param key1      Der 1. Schlüssel.
	 * @param key4      Der 4. Schlüssel.
	 * @param result    Das erwartete Resultat.
	 */
	@DisplayName("Test der 'containsKey14' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_CONTAINS_14)
	void test_containsKey14(final boolean result, final int key1, final int key4) {
		assertEquals(result, MAP.containsKey14(key1, key4));
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
	@CsvSource(textBlock = TEST_CONTAINS_23)
	void test_containsKey23(final boolean result, final int key2, final int key3) {
		assertEquals(result, MAP.containsKey23(key2, key3));
	}

	/**
	 * Test der 'containsKey24' Methode.
	 *
	 * @param key2      Der 2. Schlüssel.
	 * @param key4      Der 4. Schlüssel.
	 * @param result    Das erwartete Resultat.
	 */
	@DisplayName("Test der 'containsKey24' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_CONTAINS_24)
	void test_containsKey24(final boolean result, final int key2, final int key4) {
		assertEquals(result, MAP.containsKey24(key2, key4));
	}

	/**
	 * Test der 'containsKey34' Methode.
	 *
	 * @param key3      Der 3. Schlüssel.
	 * @param key4      Der 4. Schlüssel.
	 * @param result    Das erwartete Resultat.
	 */
	@DisplayName("Test der 'containsKey34' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_CONTAINS_34)
	void test_containsKey34(final boolean result, final int key3, final int key4) {
		assertEquals(result, MAP.containsKey34(key3, key4));
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
	@CsvSource(textBlock = TEST_CONTAINS_123)
	void test_containsKey123(final boolean result, final int key1, final int key2, final int key3) {
		assertEquals(result, MAP.containsKey123(key1, key2, key3));
	}

	/**
	 * Test der 'containsKey124' Methode.
	 *
	 * @param key1      Der 1. Schlüssel.
	 * @param key2      Der 2. Schlüssel.
	 * @param key4      Der 4. Schlüssel.
	 * @param result    Das erwartete Resultat.
	 */
	@DisplayName("Test der 'containsKey124' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_CONTAINS_124)
	void test_containsKey124(final boolean result, final int key1, final int key2, final int key4) {
		assertEquals(result, MAP.containsKey124(key1, key2, key4));
	}

	/**
	 * Test der 'containsKey134' Methode.
	 *
	 * @param key1      Der 1. Schlüssel.
	 * @param key3      Der 3. Schlüssel.
	 * @param key4      Der 4. Schlüssel.
	 * @param result    Das erwartete Resultat.
	 */
	@DisplayName("Test der 'containsKey134' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_CONTAINS_134)
	void test_containsKey134(final boolean result, final int key1, final int key3, final int key4) {
		assertEquals(result, MAP.containsKey134(key1, key3, key4));
	}

	/**
	 * Test der 'containsKey234' Methode.
	 *
	 * @param key2      Der 2. Schlüssel.
	 * @param key3      Der 3. Schlüssel.
	 * @param key4      Der 4. Schlüssel.
	 * @param result    Das erwartete Resultat.
	 */
	@DisplayName("Test der 'containsKey234' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_CONTAINS_234)
	void test_containsKey234(final boolean result, final int key2, final int key3, final int key4) {
		assertEquals(result, MAP.containsKey234(key2, key3, key4));
	}

	/**
	 * Test der 'containsKey1234' Methode.
	 *
	 * @param key1      Der 1. Schlüssel.
	 * @param key2      Der 2. Schlüssel.
	 * @param key3      Der 3. Schlüssel.
	 * @param key4      Der 4. Schlüssel.
	 * @param result    Das erwartete Resultat.
	 */
	@DisplayName("Test der 'containsKey1234' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_CONTAINS_1234)
	void test_containsKey1234(final boolean result, final int key1, final int key2, final int key3, final int key4) {
		assertEquals(result, MAP.containsKey1234(key1, key2, key3, key4));
	}

}


