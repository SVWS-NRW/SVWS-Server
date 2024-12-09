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
 * Diese Klasse testet die Funktionen der Klasse {@link ListMap4DLongKeys}.
 */
class TestListMap4DLongKeys {

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

	private static final String TEST_CONTAINSKEY_4 = """
			true, 1
			true, 2
			true, 3
			true, 4
			false, 5
		""";

	private static final String TEST_CONTAINSKEY_12 = """
			true, 1, 2
			true, 2, 1
			true, 2, 2
			true, 3, 3
			true, 4, 4
			false, 5, 5
		""";

	private static final String TEST_CONTAINSKEY_13 = """
			true, 1, 2
			true, 2, 1
			true, 2, 2
			true, 3, 3
			true, 4, 4
			false, 5, 5
		""";

	private static final String TEST_CONTAINSKEY_14 = """
			true, 1, 2
			true, 2, 1
			true, 2, 2
			true, 3, 3
			true, 4, 4
			false, 5, 5
		""";

	private static final String TEST_CONTAINSKEY_23 = """
			true, 1, 2
			true, 2, 1
			true, 2, 2
			true, 3, 3
			true, 4, 4
			false, 5, 5
		""";

	private static final String TEST_CONTAINSKEY_24 = """
			true, 1, 2
			true, 2, 1
			true, 2, 2
			true, 3, 3
			true, 4, 4
			false, 5, 5
		""";

	private static final String TEST_CONTAINSKEY_34 = """
			true, 1, 2
			true, 2, 1
			true, 2, 2
			true, 3, 3
			true, 4, 4
			false, 5, 5
		""";

	private static final String TEST_CONTAINSKEY_123 = """
			true, 1, 2, 2
			true, 2, 1, 2
			true, 2, 2, 1
			true, 2, 2, 2
			true, 3, 3, 3
			true, 4, 4, 4
			false, 5, 5, 5
		""";

	private static final String TEST_CONTAINSKEY_124 = """
			true, 1, 2, 2
			true, 2, 1, 2
			true, 2, 2, 1
			true, 2, 2, 2
			true, 3, 3, 3
			true, 4, 4, 4
			false, 5, 5, 5
		""";

	private static final String TEST_CONTAINSKEY_134 = """
			true, 1, 2, 2
			true, 2, 1, 2
			true, 2, 2, 1
			true, 2, 2, 2
			true, 3, 3, 3
			true, 4, 4, 4
			false, 5, 5, 5
		""";

	private static final String TEST_CONTAINSKEY_234 = """
			true, 1, 2, 2
			true, 2, 1, 2
			true, 2, 2, 1
			true, 2, 2, 2
			true, 3, 3, 3
			true, 4, 4, 4
			false, 5, 5, 5

		""";

	private static final String TEST_CONTAINSKEY_1234 = """
			true, 1, 2, 2, 2
			true, 2, 1, 2, 2
			true, 2, 2, 1, 2
			true, 2, 2, 2, 1
			true, 3, 3, 3, 3
			true, 4, 4, 4, 4
			false, 5, 5, 5, 5
		""";

	private static final String TEST_GET_1 = """
			1, 1
			3, 2
			1, 3
			0, 4
			0, 5
		""";

	private static final String TEST_GET_2 = """
			1, 1
			3, 2
			1, 3
			0, 4
			0, 5
		""";

	private static final String TEST_GET_3 = """
			1, 1
			3, 2
			1, 3
			0, 4
			0, 5
		""";

	private static final String TEST_GET_4 = """
			1, 1
			3, 2
			1, 3
			0, 4
			0, 5
		""";

	private static final String TEST_GET_12 = """
			1, 1, 2
			1, 2, 1
			2, 2, 2
			1, 3, 3
			0, 4, 4
			0, 5, 5
		""";

	private static final String TEST_GET_13 = """
			1, 1, 2
			1, 2, 1
			2, 2, 2
			1, 3, 3
			0, 4, 4
			0, 5, 5
		""";

	private static final String TEST_GET_14 = """
			1, 1, 2
			1, 2, 1
			2, 2, 2
			1, 3, 3
			0, 4, 4
			0, 5, 5
		""";

	private static final String TEST_GET_23 = """
			1, 1, 2
			1, 2, 1
			2, 2, 2
			1, 3, 3
			0, 4, 4
			0, 5, 5
		""";

	private static final String TEST_GET_24 = """
			1, 1, 2
			1, 2, 1
			2, 2, 2
			1, 3, 3
			0, 4, 4
			0, 5, 5
		""";

	private static final String TEST_GET_34 = """
			1, 1, 2
			1, 2, 1
			2, 2, 2
			1, 3, 3
			0, 4, 4
			0, 5, 5
		""";

	private static final String TEST_GET_123 = """
			1, 1, 2, 2
			1, 2, 1, 2
			1, 2, 2, 1
			1, 2, 2, 2
			1, 3, 3, 3
			0, 4, 4, 4
			0, 5, 5, 5
		""";

	private static final String TEST_GET_124 = """
			1, 1, 2, 2
			1, 2, 1, 2
			1, 2, 2, 1
			1, 2, 2, 2
			1, 3, 3, 3
			0, 4, 4, 4
			0, 5, 5, 5
		""";

	private static final String TEST_GET_134 = """
			1, 1, 2, 2
			1, 2, 1, 2
			1, 2, 2, 1
			1, 2, 2, 2
			1, 3, 3, 3
			0, 4, 4, 4
			0, 5, 5, 5
		""";

	private static final String TEST_GET_234 = """
			1, 1, 2, 2
			1, 2, 1, 2
			1, 2, 2, 1
			1, 2, 2, 2
			1, 3, 3, 3
			0, 4, 4, 4
			0, 5, 5, 5
		""";

	private static final String TEST_GET_1234 = """
			1, 1, 2, 2, 2
			1, 2, 1, 2, 2
			1, 2, 2, 1, 2
			1, 2, 2, 2, 1
			1, 3, 3, 3, 3
			0, 4, 4, 4, 4
			0, 5, 5, 5, 5
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

	private static final String TEST_KEYSET_4 = """
			4
		""";

	private static final String TEST_KEYSET_12 = """
			5
		""";

	private static final String TEST_KEYSET_13 = """
			5
		""";

	private static final String TEST_KEYSET_14 = """
			5
		""";

	private static final String TEST_KEYSET_23 = """
			5
		""";

	private static final String TEST_KEYSET_24 = """
			5
		""";

	private static final String TEST_KEYSET_34 = """
			5
		""";

	private static final String TEST_KEYSET_123 = """
			6
		""";

	private static final String TEST_KEYSET_124 = """
			6
		""";

	private static final String TEST_KEYSET_134 = """
			6
		""";

	private static final String TEST_KEYSET_234 = """
			6
		""";

	private static final String TEST_KEYSET_1234 = """
			6
		""";

	private static final String TEST_GET_SINGLE_OR_NULL_1 = """
			A, 1
			{null}, 2
			E, 3
			{null}, 4
			{null}, 5
		""";

	private static final String TEST_GET_SINGLE_OR_NULL_2 = """
			B, 1
			{null}, 2
			E, 3
			{null}, 4
			{null}, 5
		""";

	private static final String TEST_GET_SINGLE_OR_NULL_3 = """
			C, 1
			{null}, 2
			E, 3
			{null}, 4
			{null}, 5
		""";

	private static final String TEST_GET_SINGLE_OR_NULL_4 = """
			D, 1
			{null}, 2
			E, 3
			{null}, 4
			{null}, 5
		""";

	private static final String TEST_GET_SINGLE_OR_NULL_12 = """
			A, 1, 2
			B, 2, 1
			{null}, 2, 2
			E, 3, 3
			{null}, 4, 4
			{null}, 5, 5
		""";

	private static final String TEST_GET_SINGLE_OR_NULL_13 = """
			A, 1, 2
			C, 2, 1
			{null}, 2, 2
			E, 3, 3
			{null}, 4, 4
			{null}, 5, 5
		""";

	private static final String TEST_GET_SINGLE_OR_NULL_14 = """
			A, 1, 2
			D, 2, 1
			{null}, 2, 2
			E, 3, 3
			{null}, 4, 4
			{null}, 5, 5
		""";

	private static final String TEST_GET_SINGLE_OR_NULL_23 = """
			B, 1, 2
			C, 2, 1
			{null}, 2, 2
			E, 3, 3
			{null}, 4, 4
			{null}, 5, 5
		""";

	private static final String TEST_GET_SINGLE_OR_NULL_24 = """
			B, 1, 2
			D, 2, 1
			{null}, 2, 2
			E, 3, 3
			{null}, 4, 4
			{null}, 5, 5
		""";

	private static final String TEST_GET_SINGLE_OR_NULL_34 = """
			C, 1, 2
			D, 2, 1
			{null}, 2, 2
			E, 3, 3
			{null}, 4, 4
			{null}, 5, 5
		""";

	private static final String TEST_GET_SINGLE_OR_NULL_123 = """
			A, 1, 2, 2
			B, 2, 1, 2
			C, 2, 2, 1
			D, 2, 2, 2
			E, 3, 3, 3
			{null}, 4, 4, 4
			{null}, 5, 5, 5
		""";

	private static final String TEST_GET_SINGLE_OR_NULL_124 = """
			A, 1, 2, 2
			B, 2, 1, 2
			D, 2, 2, 1
			C, 2, 2, 2
			E, 3, 3, 3
			{null}, 4, 4, 4
			{null}, 5, 5, 5
		""";

	private static final String TEST_GET_SINGLE_OR_NULL_134 = """
			A, 1, 2, 2
			C, 2, 1, 2
			D, 2, 2, 1
			B, 2, 2, 2
			E, 3, 3, 3
			{null}, 4, 4, 4
			{null}, 5, 5, 5
		""";

	private static final String TEST_GET_SINGLE_OR_NULL_234 = """
			B, 1, 2, 2
			C, 2, 1, 2
			D, 2, 2, 1
			A, 2, 2, 2
			E, 3, 3, 3
			{null}, 4, 4, 4
			{null}, 5, 5, 5
		""";

	private static final String TEST_GET_SINGLE_OR_NULL_1234 = """
			A, 1, 2, 2, 2
			B, 2, 1, 2, 2
			C, 2, 2, 1, 2
			D, 2, 2, 2, 1
			E, 3, 3, 3, 3
			{null}, 4, 4, 4, 4
			{null}, 5, 5, 5, 5
		""";

	private static final String TEST_GET_SINGLE_OR_EXCEPTION_1 = """
			A, 1
			{null}, 2
			E, 3
			{null}, 4
			{null}, 5
		""";

	private static final String TEST_GET_SINGLE_OR_EXCEPTION_2 = """
			B, 1
			{null}, 2
			E, 3
			{null}, 4
			{null}, 5
		""";

	private static final String TEST_GET_SINGLE_OR_EXCEPTION_3 = """
			C, 1
			{null}, 2
			E, 3
			{null}, 4
			{null}, 5
		""";

	private static final String TEST_GET_SINGLE_OR_EXCEPTION_4 = """
			D, 1
			{null}, 2
			E, 3
			{null}, 4
			{null}, 5
		""";

	private static final String TEST_GET_SINGLE_OR_EXCEPTION_12 = """
			A, 1, 2
			B, 2, 1
			{null}, 2, 2
			E, 3, 3
			{null}, 4, 4
			{null}, 5, 5
		""";

	private static final String TEST_GET_SINGLE_OR_EXCEPTION_13 = """
			A, 1, 2
			C, 2, 1
			{null}, 2, 2
			E, 3, 3
			{null}, 4, 4
			{null}, 5, 5
		""";

	private static final String TEST_GET_SINGLE_OR_EXCEPTION_14 = """
			A, 1, 2
			D, 2, 1
			{null}, 2, 2
			E, 3, 3
			{null}, 4, 4
			{null}, 5, 5
		""";

	private static final String TEST_GET_SINGLE_OR_EXCEPTION_23 = """
			B, 1, 2
			C, 2, 1
			{null}, 2, 2
			E, 3, 3
			{null}, 4, 4
			{null}, 5, 5
		""";

	private static final String TEST_GET_SINGLE_OR_EXCEPTION_24 = """
			B, 1, 2
			D, 2, 1
			{null}, 2, 2
			E, 3, 3
			{null}, 4, 4
			{null}, 5, 5
		""";

	private static final String TEST_GET_SINGLE_OR_EXCEPTION_34 = """
			C, 1, 2
			D, 2, 1
			{null}, 2, 2
			E, 3, 3
			{null}, 4, 4
			{null}, 5, 5
		""";

	private static final String TEST_GET_SINGLE_OR_EXCEPTION_123 = """
			A, 1, 2, 2
			B, 2, 1, 2
			C, 2, 2, 1
			D, 2, 2, 2
			E, 3, 3, 3
			{null}, 4, 4, 4
			{null}, 5, 5, 5
		""";

	private static final String TEST_GET_SINGLE_OR_EXCEPTION_124 = """
			A, 1, 2, 2
			B, 2, 1, 2
			D, 2, 2, 1
			C, 2, 2, 2
			E, 3, 3, 3
			{null}, 4, 4, 4
			{null}, 5, 5, 5
		""";

	private static final String TEST_GET_SINGLE_OR_EXCEPTION_134 = """
			A, 1, 2, 2
			C, 2, 1, 2
			D, 2, 2, 1
			B, 2, 2, 2
			E, 3, 3, 3
			{null}, 4, 4, 4
			{null}, 5, 5, 5
		""";

	private static final String TEST_GET_SINGLE_OR_EXCEPTION_234 = """
			B, 1, 2, 2
			C, 2, 1, 2
			D, 2, 2, 1
			A, 2, 2, 2
			E, 3, 3, 3
			{null}, 4, 4, 4
			{null}, 5, 5, 5
		""";

	private static final String TEST_GET_SINGLE_OR_EXCEPTION_1234 = """
			A, 1, 2, 2, 2
			B, 2, 1, 2, 2
			C, 2, 2, 1, 2
			D, 2, 2, 2, 1
			E, 3, 3, 3, 3
			{null}, 4, 4, 4, 4
			{null}, 5, 5, 5, 5
		""";

	private static final String TEST_GET_OR_EXCEPTION_1 = """
			1, 1
			3, 2
			1, 3
			0, 4
			{null}, 5
		""";

	private static final String TEST_GET_OR_EXCEPTION_2 = """
			1, 1
			3, 2
			1, 3
			0, 4
			{null}, 5
		""";

	private static final String TEST_GET_OR_EXCEPTION_3 = """
			1, 1
			3, 2
			1, 3
			0, 4
			{null}, 5
		""";

	private static final String TEST_GET_OR_EXCEPTION_4 = """
			1, 1
			3, 2
			1, 3
			0, 4
			{null}, 5
		""";

	private static final String TEST_GET_OR_EXCEPTION_12 = """
			1, 1, 2
			1, 2, 1
			2, 2, 2
			1, 3, 3
			0, 4, 4
			{null}, 5, 5
		""";

	private static final String TEST_GET_OR_EXCEPTION_13 = """
			1, 1, 2
			1, 2, 1
			2, 2, 2
			1, 3, 3
			0, 4, 4
			{null}, 5, 5
		""";

	private static final String TEST_GET_OR_EXCEPTION_14 = """
			1, 1, 2
			1, 2, 1
			2, 2, 2
			1, 3, 3
			0, 4, 4
			{null}, 5, 5
		""";

	private static final String TEST_GET_OR_EXCEPTION_23 = """
			1, 1, 2
			1, 2, 1
			2, 2, 2
			1, 3, 3
			0, 4, 4
			{null}, 5, 5
		""";

	private static final String TEST_GET_OR_EXCEPTION_24 = """
			1, 1, 2
			1, 2, 1
			2, 2, 2
			1, 3, 3
			0, 4, 4
			{null}, 5, 5
		""";

	private static final String TEST_GET_OR_EXCEPTION_34 = """
			1, 1, 2
			1, 2, 1
			2, 2, 2
			1, 3, 3
			0, 4, 4
			{null}, 5, 5
		""";

	private static final String TEST_GET_OR_EXCEPTION_123 = """
			1, 1, 2, 2
			1, 2, 1, 2
			1, 2, 2, 1
			1, 2, 2, 2
			1, 3, 3, 3
			0, 4, 4, 4
			{null}, 5, 5, 5
		""";

	private static final String TEST_GET_OR_EXCEPTION_124 = """
			1, 1, 2, 2
			1, 2, 1, 2
			1, 2, 2, 1
			1, 2, 2, 2
			1, 3, 3, 3
			0, 4, 4, 4
			{null}, 5, 5, 5
		""";

	private static final String TEST_GET_OR_EXCEPTION_134 = """
			1, 1, 2, 2
			1, 2, 1, 2
			1, 2, 2, 1
			1, 2, 2, 2
			1, 3, 3, 3
			0, 4, 4, 4
			{null}, 5, 5, 5
		""";

	private static final String TEST_GET_OR_EXCEPTION_234 = """
			1, 1, 2, 2
			1, 2, 1, 2
			1, 2, 2, 1
			1, 2, 2, 2
			1, 3, 3, 3
			0, 4, 4, 4
			{null}, 5, 5, 5
		""";

	private static final String TEST_GET_OR_EXCEPTION_1234 = """
			1, 1, 2, 2, 2
			1, 2, 1, 2, 2
			1, 2, 2, 1, 2
			1, 2, 2, 2, 1
			1, 3, 3, 3, 3
			0, 4, 4, 4, 4
			{null}, 5, 5, 5, 5
		""";

	/**
	 * Initialisiert die Grunddaten.
	 */
	@BeforeEach
	void setup() {
		map.add(1, 2, 2, 2, "A");
		map.add(2, 1, 2, 2, "B");
		map.add(2, 2, 1, 2, "C");
		map.add(2, 2, 2, 1, "D");
		map.add(3, 3, 3, 3, "E");
		map.addEmpty(4, 4, 4, 4);
	}

	private final ListMap4DLongKeys<String> map = new ListMap4DLongKeys<>();

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
			map.add(key1, key1, key1, key1, "X");
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
			map.add(key2, key2, key2, key2, "X");
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
			map.add(key3, key3, key3, key3, "X");
			assertEquals(true, map.containsKey3(key3));
		}
	}

	/**
	 * Test der 'containsKey4' Methode.
	 *
	 * @param key4      Der 4. Schlüssel.
	 * @param result    Das erwartete Resultat.
	 */
	@DisplayName("Test der 'containsKey4' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_CONTAINSKEY_4)
	void test_containsKey4(final boolean result, final int key4) {
		assertEquals(result, map.containsKey4(key4));

		if (!result) {
			map.add(key4, key4, key4, key4, "X");
			assertEquals(true, map.containsKey4(key4));
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
			map.add(key1, key2, 99, 99, "X");
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
			map.add(key1, 99, key3, 99, "X");
			assertEquals(true, map.containsKey13(key1, key3));
		}
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
	@CsvSource(textBlock = TEST_CONTAINSKEY_14)
	void test_containsKey14(final boolean result, final int key1, final int key4) {
		assertEquals(result, map.containsKey14(key1, key4));

		if (!result) {
			map.add(key1, 99, 99, key4, "X");
			assertEquals(true, map.containsKey14(key1, key4));
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
			map.add(99, key2, key3, 99, "X");
			assertEquals(true, map.containsKey23(key2, key3));
		}
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
	@CsvSource(textBlock = TEST_CONTAINSKEY_24)
	void test_containsKey24(final boolean result, final int key2, final int key4) {
		assertEquals(result, map.containsKey24(key2, key4));

		if (!result) {
			map.add(99, key2, 99, key4, "X");
			assertEquals(true, map.containsKey24(key2, key4));
		}
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
	@CsvSource(textBlock = TEST_CONTAINSKEY_34)
	void test_containsKey34(final boolean result, final int key3, final int key4) {
		assertEquals(result, map.containsKey34(key3, key4));

		if (!result) {
			map.add(99, 99, key3, key4, "X");
			assertEquals(true, map.containsKey34(key3, key4));
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
			map.add(key1, key2, key3, 99, "X");
			assertEquals(true, map.containsKey123(key1, key2, key3));
		}
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
	@CsvSource(textBlock = TEST_CONTAINSKEY_124)
	void test_containsKey124(final boolean result, final int key1, final int key2, final int key4) {
		assertEquals(result, map.containsKey124(key1, key2, key4));

		if (!result) {
			map.add(key1, key2, 99, key4, "X");
			assertEquals(true, map.containsKey124(key1, key2, key4));
		}
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
	@CsvSource(textBlock = TEST_CONTAINSKEY_134)
	void test_containsKey134(final boolean result, final int key1, final int key3, final int key4) {
		assertEquals(result, map.containsKey134(key1, key3, key4));

		if (!result) {
			map.add(key1, 99, key3, key4, "X");
			assertEquals(true, map.containsKey134(key1, key3, key4));
		}
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
	@CsvSource(textBlock = TEST_CONTAINSKEY_234)
	void test_containsKey234(final boolean result, final int key2, final int key3, final int key4) {
		assertEquals(result, map.containsKey234(key2, key3, key4));

		if (!result) {
			map.add(99, key2, key3, key4, "X");
			assertEquals(true, map.containsKey234(key2, key3, key4));
		}
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
	@CsvSource(textBlock = TEST_CONTAINSKEY_1234)
	void test_containsKey1234(final boolean result, final int key1, final int key2, final int key3, final int key4) {
		assertEquals(result, map.containsKey1234(key1, key2, key3, key4));
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

		map.addEmpty(key1, 99, 99, 99);
		assertEquals(size, map.get1(key1).size());

		map.add(key1, 99, 99, 99, "X");
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

		map.addEmpty(99, key2, 99, 99);
		assertEquals(size, map.get2(key2).size());

		map.add(99, key2, 99, 99, "X");
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

		map.addEmpty(99, 99, key3, 99);
		assertEquals(size, map.get3(key3).size());

		map.add(99, 99, key3, 99, "X");
		assertEquals(size + 1, map.get3(key3).size());
	}

	/**
	 * Test der 'get4' Methode.
	 *
	 * @param size      Die erwartete Listengröße.
	 * @param key4      Der 4. Schlüssel.
	 */
	@DisplayName("Test der 'get4' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_4)
	void test_get4(final int size, final int key4) {
		assertEquals(size, map.get4(key4).size());

		map.addEmpty(99, 99, 99, key4);
		assertEquals(size, map.get4(key4).size());

		map.add(99, 99, 99, key4, "X");
		assertEquals(size + 1, map.get4(key4).size());
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

		map.addEmpty(key1, key2, 99, 99);
		assertEquals(size, map.get12(key1, key2).size());

		map.add(key1, key2, 99, 99, "X");
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

		map.addEmpty(key1, 99, key3, 99);
		assertEquals(size, map.get13(key1, key3).size());

		map.add(key1, 99, key3, 99, "X");
		assertEquals(size + 1, map.get13(key1, key3).size());
	}

	/**
	 * Test der 'get14' Methode.
	 *
	 * @param size      Die erwartete Listengröße.
	 * @param key1      Der 1. Schlüssel.
	 * @param key4      Der 4. Schlüssel.
	 */
	@DisplayName("Test der 'get14' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_14)
	void test_get14(final int size, final int key1, final int key4) {
		assertEquals(size, map.get14(key1, key4).size());

		map.addEmpty(key1, 99, 99, key4);
		assertEquals(size, map.get14(key1, key4).size());

		map.add(key1, 99, 99, key4, "X");
		assertEquals(size + 1, map.get14(key1, key4).size());
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

		map.addEmpty(99, key2, key3, 99);
		assertEquals(size, map.get23(key2, key3).size());

		map.add(99, key2, key3, 99, "X");
		assertEquals(size + 1, map.get23(key2, key3).size());
	}

	/**
	 * Test der 'get24' Methode.
	 *
	 * @param size      Die erwartete Listengröße.
	 * @param key2      Der 2. Schlüssel.
	 * @param key4      Der 4. Schlüssel.
	 */
	@DisplayName("Test der 'get24' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_24)
	void test_get24(final int size, final int key2, final int key4) {
		assertEquals(size, map.get24(key2, key4).size());

		map.addEmpty(99, key2, 99, key4);
		assertEquals(size, map.get24(key2, key4).size());

		map.add(99, key2, 99, key4, "X");
		assertEquals(size + 1, map.get24(key2, key4).size());
	}

	/**
	 * Test der 'get34' Methode.
	 *
	 * @param size      Die erwartete Listengröße.
	 * @param key3      Der 3. Schlüssel.
	 * @param key4      Der 4. Schlüssel.
	 */
	@DisplayName("Test der 'get34' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_34)
	void test_get34(final int size, final int key3, final int key4) {
		assertEquals(size, map.get34(key3, key4).size());

		map.addEmpty(99, 99, key3, key4);
		assertEquals(size, map.get34(key3, key4).size());

		map.add(99, 99, key3, key4, "X");
		assertEquals(size + 1, map.get34(key3, key4).size());
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

		map.addEmpty(key1, key2, key3, 99);
		assertEquals(size, map.get123(key1, key2, key3).size());

		map.add(key1, key2, key3, 99, "X");
		assertEquals(size + 1, map.get123(key1, key2, key3).size());
	}

	/**
	 * Test der 'get124' Methode.
	 *
	 * @param size      Die erwartete Listengröße.
	 * @param key1      Der 1. Schlüssel.
	 * @param key2      Der 2. Schlüssel.
	 * @param key4      Der 4. Schlüssel.
	 */
	@DisplayName("Test der 'get124' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_124)
	void test_get124(final int size, final int key1, final int key2, final int key4) {
		assertEquals(size, map.get124(key1, key2, key4).size());

		map.addEmpty(key1, key2, 99, key4);
		assertEquals(size, map.get124(key1, key2, key4).size());

		map.add(key1, key2, 99, key4, "X");
		assertEquals(size + 1, map.get124(key1, key2, key4).size());
	}

	/**
	 * Test der 'get134' Methode.
	 *
	 * @param size      Die erwartete Listengröße.
	 * @param key1      Der 1. Schlüssel.
	 * @param key3      Der 3. Schlüssel.
	 * @param key4      Der 4. Schlüssel.
	 */
	@DisplayName("Test der 'get134' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_134)
	void test_get134(final int size, final int key1, final int key3, final int key4) {
		assertEquals(size, map.get134(key1, key3, key4).size());

		map.addEmpty(key1, 99, key3, key4);
		assertEquals(size, map.get134(key1, key3, key4).size());

		map.add(key1, 99, key3, key4, "X");
		assertEquals(size + 1, map.get134(key1, key3, key4).size());
	}

	/**
	 * Test der 'get234' Methode.
	 *
	 * @param size      Die erwartete Listengröße.
	 * @param key2      Der 2. Schlüssel.
	 * @param key3      Der 3. Schlüssel.
	 * @param key4      Der 4. Schlüssel.
	 */
	@DisplayName("Test der 'get234' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_234)
	void test_get234(final int size, final int key2, final int key3, final int key4) {
		assertEquals(size, map.get234(key2, key3, key4).size());

		map.addEmpty(99, key2, key3, key4);
		assertEquals(size, map.get234(key2, key3, key4).size());

		map.add(99, key2, key3, key4, "X");
		assertEquals(size + 1, map.get234(key2, key3, key4).size());
	}

	/**
	 * Test der 'get1234' Methode.
	 *
	 * @param size      Die erwartete Listengröße.
	 * @param key1      Der 1. Schlüssel.
	 * @param key2      Der 2. Schlüssel.
	 * @param key3      Der 3. Schlüssel.
	 * @param key4      Der 4. Schlüssel.
	 */
	@DisplayName("Test der 'get1234' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_1234)
	void test_get1234(final int size, final int key1, final int key2, final int key3, final int key4) {
		assertEquals(size, map.get1234(key1, key2, key3, key4).size());

		map.addEmpty(key1, key2, key3, key4);
		assertEquals(size, map.get1234(key1, key2, key3, key4).size());

		map.add(key1, key2, key3, key4, "X");
		assertEquals(size + 1, map.get1234(key1, key2, key3, key4).size());
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

		map.addEmpty(66, 66, 66, 66);
		assertEquals(size + 1, map.keySet1().size());

		map.add(77, 77, 77, 77, "X");
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

		map.addEmpty(66, 66, 66, 66);
		assertEquals(size + 1, map.keySet2().size());

		map.add(77, 77, 77, 77, "X");
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

		map.addEmpty(66, 66, 66, 66);
		assertEquals(size + 1, map.keySet3().size());

		map.add(77, 77, 77, 77, "X");
		assertEquals(size + 2, map.keySet3().size());
	}

	/**
	 * Test der 'keySet4' Methode.
	 *
	 * @param size      Die erwartete Größe des Keysets4.
	 */
	@DisplayName("Test der 'keySet4' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_KEYSET_4)
	void test_keySet4(final int size) {
		assertEquals(size, map.keySet4().size());

		map.addEmpty(66, 66, 66, 66);
		assertEquals(size + 1, map.keySet4().size());

		map.add(77, 77, 77, 77, "X");
		assertEquals(size + 2, map.keySet4().size());
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

		map.addEmpty(66, 66, 66, 66);
		assertEquals(size + 1, map.keySet12().size());

		map.add(77, 77, 77, 77, "X");
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

		map.addEmpty(66, 66, 66, 66);
		assertEquals(size + 1, map.keySet13().size());

		map.add(77, 77, 77, 77, "X");
		assertEquals(size + 2, map.keySet13().size());
	}

	/**
	 * Test der 'keySet14' Methode.
	 *
	 * @param size      Die erwartete Größe des Keysets14.
	 */
	@DisplayName("Test der 'keySet14' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_KEYSET_14)
	void test_keySet14(final int size) {
		assertEquals(size, map.keySet14().size());

		map.addEmpty(66, 66, 66, 66);
		assertEquals(size + 1, map.keySet14().size());

		map.add(77, 77, 77, 77, "X");
		assertEquals(size + 2, map.keySet14().size());
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

		map.addEmpty(66, 66, 66, 66);
		assertEquals(size + 1, map.keySet23().size());

		map.add(77, 77, 77, 77, "X");
		assertEquals(size + 2, map.keySet23().size());
	}

	/**
	 * Test der 'keySet24' Methode.
	 *
	 * @param size      Die erwartete Größe des Keysets24.
	 */
	@DisplayName("Test der 'keySet24' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_KEYSET_24)
	void test_keySet24(final int size) {
		assertEquals(size, map.keySet24().size());

		map.addEmpty(66, 66, 66, 66);
		assertEquals(size + 1, map.keySet24().size());

		map.add(77, 77, 77, 77, "X");
		assertEquals(size + 2, map.keySet24().size());
	}

	/**
	 * Test der 'keySet34' Methode.
	 *
	 * @param size      Die erwartete Größe des Keysets34.
	 */
	@DisplayName("Test der 'keySet34' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_KEYSET_34)
	void test_keySet34(final int size) {
		assertEquals(size, map.keySet34().size());

		map.addEmpty(66, 66, 66, 66);
		assertEquals(size + 1, map.keySet34().size());

		map.add(77, 77, 77, 77, "X");
		assertEquals(size + 2, map.keySet34().size());
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

		map.addEmpty(66, 66, 66, 66);
		assertEquals(size + 1, map.keySet123().size());

		map.add(77, 77, 77, 77, "X");
		assertEquals(size + 2, map.keySet123().size());
	}

	/**
	 * Test der 'keySet124' Methode.
	 *
	 * @param size      Die erwartete Größe des Keysets124.
	 */
	@DisplayName("Test der 'keySet124' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_KEYSET_124)
	void test_keySet124(final int size) {
		assertEquals(size, map.keySet124().size());

		map.addEmpty(66, 66, 66, 66);
		assertEquals(size + 1, map.keySet124().size());

		map.add(77, 77, 77, 77, "X");
		assertEquals(size + 2, map.keySet124().size());
	}

	/**
	 * Test der 'keySet134' Methode.
	 *
	 * @param size      Die erwartete Größe des Keysets134.
	 */
	@DisplayName("Test der 'keySet134' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_KEYSET_134)
	void test_keySet134(final int size) {
		assertEquals(size, map.keySet134().size());

		map.addEmpty(66, 66, 66, 66);
		assertEquals(size + 1, map.keySet134().size());

		map.add(77, 77, 77, 77, "X");
		assertEquals(size + 2, map.keySet134().size());
	}

	/**
	 * Test der 'keySet234' Methode.
	 *
	 * @param size      Die erwartete Größe des Keysets234.
	 */
	@DisplayName("Test der 'keySet234' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_KEYSET_234)
	void test_keySet234(final int size) {
		assertEquals(size, map.keySet234().size());

		map.addEmpty(66, 66, 66, 66);
		assertEquals(size + 1, map.keySet234().size());

		map.add(77, 77, 77, 77, "X");
		assertEquals(size + 2, map.keySet234().size());
	}

	/**
	 * Test der 'keySet1234' Methode.
	 *
	 * @param size      Die erwartete Größe des Keysets1234.
	 */
	@DisplayName("Test der 'keySet1234' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_KEYSET_1234)
	void test_keySet1234(final int size) {
		assertEquals(size, map.keySet1234().size());

		map.addEmpty(66, 66, 66, 66);
		assertEquals(size + 1, map.keySet1234().size());

		map.add(77, 77, 77, 77, "X");
		assertEquals(size + 2, map.keySet1234().size());
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

		map.addEmpty(66, 66, 66, 66);
		assertEquals(result, map.getSingle1OrNull(key1));

		map.add(77, 77, 77, 77, "X");
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

		map.addEmpty(66, 66, 66, 66);
		assertEquals(result, map.getSingle2OrNull(key2));

		map.add(77, 77, 77, 77, "X");
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

		map.addEmpty(66, 66, 66, 66);
		assertEquals(result, map.getSingle3OrNull(key3));

		map.add(77, 77, 77, 77, "X");
		assertEquals(result, map.getSingle3OrNull(key3));
	}

	/**
	 * Test der 'getSingleOrNull4' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key4      Der 4. Schlüssel.
	 */
	@DisplayName("Test der 'getSingleOrNull4' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_SINGLE_OR_NULL_4, nullValues = "{null}")
	void test_getSingleOrNull4(final String result, final int key4) {
		assertEquals(result, map.getSingle4OrNull(key4));

		map.addEmpty(66, 66, 66, 66);
		assertEquals(result, map.getSingle4OrNull(key4));

		map.add(77, 77, 77, 77, "X");
		assertEquals(result, map.getSingle4OrNull(key4));
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

		map.addEmpty(66, 66, 66, 66);
		assertEquals(result, map.getSingle12OrNull(key1, key2));

		map.add(77, 77, 77, 77, "X");
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

		map.addEmpty(66, 66, 66, 66);
		assertEquals(result, map.getSingle13OrNull(key1, key3));

		map.add(77, 77, 77, 77, "X");
		assertEquals(result, map.getSingle13OrNull(key1, key3));
	}

	/**
	 * Test der 'getSingleOrNull14' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key1      Der 1. Schlüssel.
	 * @param key4      Der 4. Schlüssel.
	 */
	@DisplayName("Test der 'getSingleOrNull14' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_SINGLE_OR_NULL_14, nullValues = "{null}")
	void test_getSingleOrNull14(final String result, final int key1, final int key4) {
		assertEquals(result, map.getSingle14OrNull(key1, key4));

		map.addEmpty(66, 66, 66, 66);
		assertEquals(result, map.getSingle14OrNull(key1, key4));

		map.add(77, 77, 77, 77, "X");
		assertEquals(result, map.getSingle14OrNull(key1, key4));
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

		map.addEmpty(66, 66, 66, 66);
		assertEquals(result, map.getSingle23OrNull(key2, key3));

		map.add(77, 77, 77, 77, "X");
		assertEquals(result, map.getSingle23OrNull(key2, key3));
	}

	/**
	 * Test der 'getSingleOrNull24' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key2      Der 2. Schlüssel.
	 * @param key4      Der 4. Schlüssel.
	 */
	@DisplayName("Test der 'getSingleOrNull24' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_SINGLE_OR_NULL_24, nullValues = "{null}")
	void test_getSingleOrNull24(final String result, final int key2, final int key4) {
		assertEquals(result, map.getSingle24OrNull(key2, key4));

		map.addEmpty(66, 66, 66, 66);
		assertEquals(result, map.getSingle24OrNull(key2, key4));

		map.add(77, 77, 77, 77, "X");
		assertEquals(result, map.getSingle24OrNull(key2, key4));
	}

	/**
	 * Test der 'getSingleOrNull34' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key3      Der 3. Schlüssel.
	 * @param key4      Der 4. Schlüssel.
	 */
	@DisplayName("Test der 'getSingleOrNull34' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_SINGLE_OR_NULL_34, nullValues = "{null}")
	void test_getSingleOrNull34(final String result, final int key3, final int key4) {
		assertEquals(result, map.getSingle34OrNull(key3, key4));

		map.addEmpty(66, 66, 66, 66);
		assertEquals(result, map.getSingle34OrNull(key3, key4));

		map.add(77, 77, 77, 77, "X");
		assertEquals(result, map.getSingle34OrNull(key3, key4));
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

		map.addEmpty(66, 66, 66, 66);
		assertEquals(result, map.getSingle123OrNull(key1, key2, key3));

		map.add(77, 77, 77, 77, "X");
		assertEquals(result, map.getSingle123OrNull(key1, key2, key3));
	}

	/**
	 * Test der 'getSingleOrNull124' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key1      Der 1. Schlüssel.
	 * @param key2      Der 2. Schlüssel.
	 * @param key4      Der 4. Schlüssel.
	 */
	@DisplayName("Test der 'getSingleOrNull124' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_SINGLE_OR_NULL_124, nullValues = "{null}")
	void test_getSingleOrNull124(final String result, final int key1, final int key2, final int key4) {
		assertEquals(result, map.getSingle124OrNull(key1, key2, key4));

		map.addEmpty(66, 66, 66, 66);
		assertEquals(result, map.getSingle124OrNull(key1, key2, key4));

		map.add(77, 77, 77, 77, "X");
		assertEquals(result, map.getSingle124OrNull(key1, key2, key4));
	}

	/**
	 * Test der 'getSingleOrNull134' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key1      Der 1. Schlüssel.
	 * @param key3      Der 3. Schlüssel.
	 * @param key4      Der 4. Schlüssel.
	 */
	@DisplayName("Test der 'getSingleOrNull134' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_SINGLE_OR_NULL_134, nullValues = "{null}")
	void test_getSingleOrNull134(final String result, final int key1, final int key3, final int key4) {
		assertEquals(result, map.getSingle134OrNull(key1, key3, key4));

		map.addEmpty(66, 66, 66, 66);
		assertEquals(result, map.getSingle134OrNull(key1, key3, key4));

		map.add(77, 77, 77, 77, "X");
		assertEquals(result, map.getSingle134OrNull(key1, key3, key4));
	}

	/**
	 * Test der 'getSingleOrNull234' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key2      Der 2. Schlüssel.
	 * @param key3      Der 3. Schlüssel.
	 * @param key4      Der 4. Schlüssel.
	 */
	@DisplayName("Test der 'getSingleOrNull234' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_SINGLE_OR_NULL_234, nullValues = "{null}")
	void test_getSingleOrNull234(final String result, final int key2, final int key3, final int key4) {
		assertEquals(result, map.getSingle234OrNull(key2, key3, key4));

		map.addEmpty(66, 66, 66, 66);
		assertEquals(result, map.getSingle234OrNull(key2, key3, key4));

		map.add(77, 77, 77, 77, "X");
		assertEquals(result, map.getSingle234OrNull(key2, key3, key4));
	}

	/**
	 * Test der 'getSingleOrNull1234' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key1      Der 1. Schlüssel.
	 * @param key2      Der 2. Schlüssel.
	 * @param key3      Der 3. Schlüssel.
	 * @param key4      Der 4. Schlüssel.
	 */
	@DisplayName("Test der 'getSingleOrNull1234' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_SINGLE_OR_NULL_1234, nullValues = "{null}")
	void test_getSingleOrNull1234(final String result, final int key1, final int key2, final int key3, final int key4) {
		assertEquals(result, map.getSingle1234OrNull(key1, key2, key3, key4));

		map.addEmpty(66, 66, 66, 66);
		assertEquals(result, map.getSingle1234OrNull(key1, key2, key3, key4));

		map.add(77, 77, 77, 77, "X");
		assertEquals(result, map.getSingle1234OrNull(key1, key2, key3, key4));
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
	 * Test der 'getSingleOrException4' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key4      Der 4. Schlüssel.
	 */
	@DisplayName("Test der 'getSingleOrException4' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_SINGLE_OR_EXCEPTION_4, nullValues = "{null}")
	void test_getSingleOrException4(final String result, final int key4) {
		if (result == null)
			assertThrows(DeveloperNotificationException.class, () -> {
				map.getSingle4OrException(key4);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.getSingle4OrException(key4));
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
	 * Test der 'getSingleOrException14' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key1      Der 1. Schlüssel.
	 * @param key4      Der 4. Schlüssel.
	 */
	@DisplayName("Test der 'getSingleOrException14' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_SINGLE_OR_EXCEPTION_14, nullValues = "{null}")
	void test_getSingleOrException14(final String result, final int key1, final int key4) {
		if (result == null)
			assertThrows(DeveloperNotificationException.class, () -> {
				map.getSingle14OrException(key1, key4);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.getSingle14OrException(key1, key4));
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
	 * Test der 'getSingleOrException24' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key2      Der 2. Schlüssel.
	 * @param key4      Der 4. Schlüssel.
	 */
	@DisplayName("Test der 'getSingleOrException24' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_SINGLE_OR_EXCEPTION_24, nullValues = "{null}")
	void test_getSingleOrException24(final String result, final int key2, final int key4) {
		if (result == null)
			assertThrows(DeveloperNotificationException.class, () -> {
				map.getSingle24OrException(key2, key4);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.getSingle24OrException(key2, key4));
			});
	}

	/**
	 * Test der 'getSingleOrException34' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key3      Der 3. Schlüssel.
	 * @param key4      Der 4. Schlüssel.
	 */
	@DisplayName("Test der 'getSingleOrException34' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_SINGLE_OR_EXCEPTION_34, nullValues = "{null}")
	void test_getSingleOrException34(final String result, final int key3, final int key4) {
		if (result == null)
			assertThrows(DeveloperNotificationException.class, () -> {
				map.getSingle34OrException(key3, key4);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.getSingle34OrException(key3, key4));
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
	 * Test der 'getSingleOrException124' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key1      Der 1. Schlüssel.
	 * @param key2      Der 2. Schlüssel.
	 * @param key4      Der 4. Schlüssel.
	 */
	@DisplayName("Test der 'getSingleOrException124' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_SINGLE_OR_EXCEPTION_124, nullValues = "{null}")
	void test_getSingleOrException124(final String result, final int key1, final int key2, final int key4) {
		if (result == null)
			assertThrows(DeveloperNotificationException.class, () -> {
				map.getSingle124OrException(key1, key2, key4);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.getSingle124OrException(key1, key2, key4));
			});
	}

	/**
	 * Test der 'getSingleOrException134' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key1      Der 1. Schlüssel.
	 * @param key3      Der 3. Schlüssel.
	 * @param key4      Der 4. Schlüssel.
	 */
	@DisplayName("Test der 'getSingleOrException134' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_SINGLE_OR_EXCEPTION_134, nullValues = "{null}")
	void test_getSingleOrException134(final String result, final int key1, final int key3, final int key4) {
		if (result == null)
			assertThrows(DeveloperNotificationException.class, () -> {
				map.getSingle134OrException(key1, key3, key4);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.getSingle134OrException(key1, key3, key4));
			});
	}

	/**
	 * Test der 'getSingleOrException234' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key2      Der 2. Schlüssel.
	 * @param key3      Der 3. Schlüssel.
	 * @param key4      Der 4. Schlüssel.
	 */
	@DisplayName("Test der 'getSingleOrException234' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_SINGLE_OR_EXCEPTION_234, nullValues = "{null}")
	void test_getSingleOrException234(final String result, final int key2, final int key3, final int key4) {
		if (result == null)
			assertThrows(DeveloperNotificationException.class, () -> {
				map.getSingle234OrException(key2, key3, key4);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.getSingle234OrException(key2, key3, key4));
			});
	}

	/**
	 * Test der 'getSingleOrException1234' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key1      Der 1. Schlüssel.
	 * @param key2      Der 2. Schlüssel.
	 * @param key3      Der 3. Schlüssel.
	 * @param key4      Der 4. Schlüssel.
	 */
	@DisplayName("Test der 'getSingleOrException1234' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_SINGLE_OR_EXCEPTION_1234, nullValues = "{null}")
	void test_getSingleOrException1234(final String result, final int key1, final int key2, final int key3, final int key4) {
		if (result == null)
			assertThrows(DeveloperNotificationException.class, () -> {
				map.getSingle1234OrException(key1, key2, key3, key4);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.getSingle1234OrException(key1, key2, key3, key4));
			});
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
	 * Test der 'get4OrException' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key4      Der 4. Schlüssel.
	 */
	@DisplayName("Test der 'get4OrException' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_OR_EXCEPTION_4, nullValues = "{null}")
	void test_get4OrException(final Integer result, final int key4) {
		if (result == null)
			assertThrows(DeveloperNotificationException.class, () -> {
				map.get4OrException(key4);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.get4OrException(key4).size());
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
	 * Test der 'get14OrException' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key1      Der 1. Schlüssel.
	 * @param key4      Der 4. Schlüssel.
	 */
	@DisplayName("Test der 'get14OrException' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_OR_EXCEPTION_14, nullValues = "{null}")
	void test_get14OrException(final Integer result, final int key1, final int key4) {
		if (result == null)
			assertThrows(DeveloperNotificationException.class, () -> {
				map.get14OrException(key1, key4);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.get14OrException(key1, key4).size());
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
	 * Test der 'get24OrException' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key2      Der 2. Schlüssel.
	 * @param key4      Der 4. Schlüssel.
	 */
	@DisplayName("Test der 'get24OrException' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_OR_EXCEPTION_24, nullValues = "{null}")
	void test_get24OrException(final Integer result, final int key2, final int key4) {
		if (result == null)
			assertThrows(DeveloperNotificationException.class, () -> {
				map.get24OrException(key2, key4);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.get24OrException(key2, key4).size());
			});
	}

	/**
	 * Test der 'get34OrException' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key3      Der 3. Schlüssel.
	 * @param key4      Der 4. Schlüssel.
	 */
	@DisplayName("Test der 'get34OrException' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_OR_EXCEPTION_34, nullValues = "{null}")
	void test_get34OrException(final Integer result, final int key3, final int key4) {
		if (result == null)
			assertThrows(DeveloperNotificationException.class, () -> {
				map.get34OrException(key3, key4);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.get34OrException(key3, key4).size());
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

	/**
	 * Test der 'get124OrException' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key1      Der 1. Schlüssel.
	 * @param key2      Der 2. Schlüssel.
	 * @param key4      Der 4. Schlüssel.
	 */
	@DisplayName("Test der 'get124OrException' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_OR_EXCEPTION_124, nullValues = "{null}")
	void test_get124OrException(final Integer result, final int key1, final int key2, final int key4) {
		if (result == null)
			assertThrows(DeveloperNotificationException.class, () -> {
				map.get124OrException(key1, key2, key4);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.get124OrException(key1, key2, key4).size());
			});
	}

	/**
	 * Test der 'get134OrException' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key1      Der 1. Schlüssel.
	 * @param key3      Der 3. Schlüssel.
	 * @param key4      Der 4. Schlüssel.
	 */
	@DisplayName("Test der 'get134OrException' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_OR_EXCEPTION_134, nullValues = "{null}")
	void test_get134OrException(final Integer result, final int key1, final int key3, final int key4) {
		if (result == null)
			assertThrows(DeveloperNotificationException.class, () -> {
				map.get134OrException(key1, key3, key4);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.get134OrException(key1, key3, key4).size());
			});
	}

	/**
	 * Test der 'get234OrException' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key2      Der 2. Schlüssel.
	 * @param key3      Der 3. Schlüssel.
	 * @param key4      Der 4. Schlüssel.
	 */
	@DisplayName("Test der 'get234OrException' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_OR_EXCEPTION_234, nullValues = "{null}")
	void test_get234OrException(final Integer result, final int key2, final int key3, final int key4) {
		if (result == null)
			assertThrows(DeveloperNotificationException.class, () -> {
				map.get234OrException(key2, key3, key4);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.get234OrException(key2, key3, key4).size());
			});
	}

	/**
	 * Test der 'get1234OrException' Methode.
	 *
	 * @param result    Das erwartete Ergebnis.
	 * @param key1      Der 1. Schlüssel.
	 * @param key2      Der 2. Schlüssel.
	 * @param key3      Der 3. Schlüssel.
	 * @param key4      Der 4. Schlüssel.
	 */
	@DisplayName("Test der 'get1234OrException' Methode.")
	@ParameterizedTest
	@CsvSource(textBlock = TEST_GET_OR_EXCEPTION_1234, nullValues = "{null}")
	void test_get1234OrException(final Integer result, final int key1, final int key2, final int key3, final int key4) {
		if (result == null)
			assertThrows(DeveloperNotificationException.class, () -> {
				map.get1234OrException(key1, key2, key3, key4);
			});
		else
			assertDoesNotThrow(() -> {
				assertEquals(result, map.get1234OrException(key1, key2, key3, key4).size());
			});
	}

}


