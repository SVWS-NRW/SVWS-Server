package de.svws_nrw.core.adt.map;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse testet die Funktionen der Klasse {@link ArrayMap}.
 */
class TestArrayMap {
	
	private static final long RANDOM_SEED = 1;
	private static final int RANDOM_ROUNDS = 1000000;
	private static final int MAX_VALUE = 100;

	/**
	 * Ein simpler Enum zum Testen
	 */
	enum TestEnum {
		/** Eintrag 0 */ A,
		/** Eintrag 1 */ B,
		/** Eintrag 2 */ C,
		/** Eintrag 3 */ D,
		/** Eintrag 4 */ E,
		/** Eintrag 5 */ F,
		/** Eintrag 6 */ G,
		/** Eintrag 7 */ H,
		/** Eintrag 8 */ I,
		/** Eintrag 9 */ J;
	}

	/**
	 * Testet den Konstruktor der ArrayMap.
	 */
	@Test
	@DisplayName("ArrayMap: Teste den Konstruktor für Enums.")
	void testKonstruktorEnum() {
		final ArrayMap<@NotNull TestEnum, @NotNull Object> map = new ArrayMap<>(TestEnum.values());
		assertEquals(0, map.size());
	}

	/**
	 * Testet den Konstruktor der ArrayMap.
	 */
	@Test
	@DisplayName("ArrayMap: Teste size, put und remove.")
	void testSizePutRemove() {
		final ArrayMap<@NotNull TestEnum, @NotNull String> map = new ArrayMap<>(TestEnum.values());
		assertEquals(0, map.size());
		map.put(TestEnum.D, "D");
		assertEquals(1, map.size());
		map.put(TestEnum.F, "F");
		assertEquals(2, map.size());
		map.put(TestEnum.D, "Dneu");
		assertEquals(2, map.size());
		map.remove(TestEnum.D);
		assertEquals(1, map.size());
		map.remove(TestEnum.D);
		assertEquals(1, map.size());
	}
	
	/**
	 * Testet alle Methoden in zufälliger Reihenfolge.
	 */
	@Test
	@DisplayName("ArrayMap: Teste alles random.")
	void testRandom() {
		final TestEnum[] teA = TestEnum.values();
		final @NotNull Random rnd = new Random(RANDOM_SEED);
		final ArrayMap<@NotNull TestEnum, @NotNull Integer> map1 = new ArrayMap<>(teA);
		final HashMap<@NotNull TestEnum, @NotNull Integer> map2 = new HashMap<>();
		final HashMap<@NotNull TestEnum, @NotNull Integer> map3 = new HashMap<>();
		
		// TODO BAR random value also null
		
		for (int i = 0; i < RANDOM_ROUNDS; i++) {
			int c = rnd.nextInt(14);

			switch (c) {
				case 0 -> { // clear
					if (rnd.nextInt(RANDOM_ROUNDS) < 100) {
						map1.clear();
						map2.clear();
						boolean re1 = map1.isEmpty();
						boolean re2 = map2.isEmpty();
						assertEquals(re1, re2);
					}
				}
				case 1 -> { // containsKey
					TestEnum key = teA[rnd.nextInt(teA.length)];
					boolean re1 = map1.containsKey(key);
					boolean re2 = map2.containsKey(key);
					assertEquals(re1, re2);
				}
				case 2 -> { // containsValue
					int value = rnd.nextInt(MAX_VALUE);
					boolean re1 = map1.containsValue(value);
					boolean re2 = map2.containsValue(value);
					assertEquals(re1, re2);
				}
				case 3 -> { // entrySet
					for (Entry<TestEnum, Integer> e2 : map2.entrySet()) {
						int value1 = map1.get(e2.getKey());
						int value2 = e2.getValue();
						assertEquals(value1, value2);
					}
					for (Entry<TestEnum, Integer> e1 : map1.entrySet()) {
						int value1 = e1.getValue();
						int value2 = map2.get(e1.getKey());
						assertEquals(value1, value2);
					}
				}
				case 4 -> { // equals 
					// TODO BAR BACHRAN ArrayMap.equals(...) funktioniert nicht
				}
				case 5 -> { // get
					TestEnum key = teA[rnd.nextInt(teA.length)];
					Integer re1 = map1.get(key);
					Integer re2 = map2.get(key);
					assertEquals(re1, re2);
				}
				case 6 -> { // isEmpty
					
					boolean re1 = map1.isEmpty();
					boolean re2 = map2.isEmpty();
					assertEquals(re1, re2);
				}
				case 7 -> { // keySet
					for (TestEnum key2 : map2.keySet()) {
						assertEquals(map1.containsKey(key2), true);
					}
					for (TestEnum key1 : map1.keySet()) {
						assertEquals(map2.containsKey(key1), true);
					}
				}
				case 8 -> { // put
					TestEnum key = teA[rnd.nextInt(teA.length)];
					int value = rnd.nextInt(MAX_VALUE);
					Integer re1 = map1.put(key, value);
					Integer re2 = map2.put(key, value);
					assertEquals(re1, re2);
				}
				case 9 -> { // putAll
					map3.clear();
					do {
						TestEnum key = teA[rnd.nextInt(teA.length)];
						int value = rnd.nextInt(MAX_VALUE);
						map3.put(key, value);
					} while (rnd.nextBoolean());
					map1.putAll(map3);
					map2.putAll(map3);
					assertEquals(map1.size(), map2.size());
				}
				case 10 -> { // remove
					TestEnum key = teA[rnd.nextInt(teA.length)];
					Integer re1 = map1.remove(key);
					Integer re2 = map2.remove(key);
					assertEquals(re1, re2);
				}
				case 11 -> { // remove(key, value)
					TestEnum key = teA[rnd.nextInt(teA.length)];
					int value = rnd.nextInt(MAX_VALUE);
					boolean re1 = map1.remove(key, value);
					boolean re2 = map2.remove(key, value);
					assertEquals(re1, re2);
				}
				case 12 -> { // size
					assertEquals(map1.size(), map2.size());
				}
				case 13 -> { // values
					for (Integer value1 : map1.values()) {
						assertEquals(map2.containsValue(value1), true);
					}
					for (Integer value2 : map2.values()) {
						assertEquals(map1.containsValue(value2), true);
					}
				}
			}

		}

	}


}
