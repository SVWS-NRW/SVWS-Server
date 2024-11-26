package de.svws_nrw.core.adt.map;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Diese Klasse testet die Funktionen der Klasse {@link ListMap3DLongKeys}.
 */
class TestListMap3DLongKeys {

	/**
	 * Testet den Konstruktor der ArrayMap.
	 */
	@Test
	@DisplayName("ArrayMap: Teste das Szenario 1.")
	void testSzenario1() {
		final ListMap3DLongKeys<String> map = new ListMap3DLongKeys<>();

		// add
		map.add(1, 2, 3, "A");
		map.add(1, 2, 4, "B");
		map.add(1, 5, 3, "C");
		map.add(2, 2, 3, "D");
		map.add(2, 5, 9, "E");

		// contains
		assertEquals(true, map.containsKey1(1));
		assertEquals(true, map.containsKey1(2));
		assertEquals(false, map.containsKey1(3));

		assertEquals(true, map.containsKey2(2));
		assertEquals(true, map.containsKey2(5));
		assertEquals(false, map.containsKey2(8));

		assertEquals(true, map.containsKey3(3));
		assertEquals(true, map.containsKey3(4));
		assertEquals(true, map.containsKey3(9));
		assertEquals(false, map.containsKey3(7));

		assertEquals(true, map.containsKey12(1, 2));
		assertEquals(true, map.containsKey12(1, 5));
		assertEquals(true, map.containsKey12(2, 2));
		assertEquals(true, map.containsKey12(2, 5));
		assertEquals(false, map.containsKey12(2, 8));

		assertEquals(true, map.containsKey13(1, 3));
		assertEquals(true, map.containsKey13(1, 4));
		assertEquals(true, map.containsKey13(2, 3));
		assertEquals(true, map.containsKey13(2, 9));
		assertEquals(false, map.containsKey13(3, 3));

		assertEquals(true, map.containsKey23(2, 3));
		assertEquals(true, map.containsKey23(2, 4));
		assertEquals(true, map.containsKey23(5, 3));
		assertEquals(true, map.containsKey23(5, 9));
		assertEquals(false, map.containsKey23(6, 7));

		assertEquals(true, map.containsKey123(1, 2, 3));
		assertEquals(true, map.containsKey123(1, 2, 4));
		assertEquals(true, map.containsKey123(1, 5, 3));
		assertEquals(true, map.containsKey123(2, 2, 3));
		assertEquals(true, map.containsKey123(2, 5, 9));
		assertEquals(false, map.containsKey123(2, 1, 9));

		// get
		assertEquals(3, map.get1(1).size());
		assertEquals(2, map.get1(2).size());
		assertEquals(0, map.get1(3).size());

		assertEquals(3, map.get2(2).size());
		assertEquals(2, map.get2(5).size());
		assertEquals(0, map.get2(8).size());

		assertEquals(3, map.get3(3).size());
		assertEquals(1, map.get3(4).size());
		assertEquals(1, map.get3(9).size());
		assertEquals(0, map.get3(7).size());

		assertEquals(2, map.get12(1, 2).size());
		assertEquals(1, map.get12(1, 5).size());
		assertEquals(1, map.get12(2, 2).size());
		assertEquals(1, map.get12(2, 5).size());
		assertEquals(0, map.get12(2, 8).size());

		assertEquals(2, map.get13(1, 3).size());
		assertEquals(1, map.get13(1, 4).size());
		assertEquals(1, map.get13(2, 3).size());
		assertEquals(1, map.get13(2, 9).size());
		assertEquals(0, map.get13(3, 3).size());

		assertEquals(2, map.get23(2, 3).size());
		assertEquals(1, map.get23(2, 4).size());
		assertEquals(1, map.get23(5, 3).size());
		assertEquals(1, map.get23(5, 9).size());
		assertEquals(0, map.get23(6, 7).size());

		assertEquals(1, map.get123(1, 2, 3).size());
		assertEquals(1, map.get123(1, 2, 4).size());
		assertEquals(1, map.get123(1, 5, 3).size());
		assertEquals(1, map.get123(2, 2, 3).size());
		assertEquals(1, map.get123(2, 5, 9).size());
		assertEquals(0, map.get123(2, 1, 9).size());

		// keyset
		assertEquals(2, map.keySet1().size());
		assertEquals(2, map.keySet2().size());
		assertEquals(3, map.keySet3().size());

		assertEquals(4, map.keySet12().size());
		assertEquals(4, map.keySet23().size());
		assertEquals(4, map.keySet13().size());

		assertEquals(5, map.keySet123().size());

		// V
		assertEquals("A", map.get123(1, 2, 3).getFirst());
		assertEquals("B", map.get123(1, 2, 4).getFirst());
		assertEquals("C", map.get123(1, 5, 3).getFirst());
		assertEquals("D", map.get123(2, 2, 3).getFirst());
		assertEquals("E", map.get123(2, 5, 9).getFirst());
	}


}
