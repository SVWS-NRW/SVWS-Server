package de.svws_nrw.core.adt.map;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Diese Klasse testet die Funktionen der Klasse {@link ListMap2DLongKeys}.
 */
class TestListMap2DLongKeys {

	/**
	 * Testet den Konstruktor der ArrayMap.
	 */
	@Test
	@DisplayName("ArrayMap: Teste das Szenario 1.")
	void testSzenario1() {
		final ListMap2DLongKeys<String> map = new ListMap2DLongKeys<>();

		// add
		map.add(2, 3, "Ben");
		map.add(2, 4, "Bar");

		// contains
		assertEquals(true, map.containsKey1(2));
		assertEquals(false, map.containsKey1(3));

		assertEquals(true, map.containsKey2(3));
		assertEquals(false, map.containsKey2(2));

		assertEquals(true, map.containsKey12(2, 3));
		assertEquals(true, map.containsKey12(2, 4));
		assertEquals(false, map.containsKey12(2, 5));

		// get
		assertEquals(2, map.get1(2).size());
		assertEquals(0, map.get1(3).size());

		assertEquals(1, map.get2(3).size());
		assertEquals(1, map.get2(4).size());
		assertEquals(0, map.get2(5).size());

		assertEquals(1, map.get12(2, 3).size());
		assertEquals(1, map.get12(2, 4).size());
		assertEquals(0, map.get12(3, 4).size());

		// keyset
		assertEquals(1, map.keySet1().size());
		assertEquals(2, map.keySet2().size());
		assertEquals(2, map.keySet12().size());

		// V
		assertEquals("Ben", map.get12(2, 3).getFirst());
		assertEquals("Bar", map.get12(2, 4).getFirst());

		// (2, 3, "Ben")
		// (2, 4, "Bar")
		map.addEmpty(2, 5);
		assertEquals(true, map.containsKey1(2));
		assertEquals(true, map.containsKey2(5));
		assertEquals(true, map.containsKey12(2, 5));

		assertEquals(2, map.get1(2).size());
		assertEquals(0, map.get2(5).size());
		assertEquals(true, map.get12(2, 5).isEmpty());
	}

}
