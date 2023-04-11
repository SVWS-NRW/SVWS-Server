package de.svws_nrw.core.adt.map;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse testet die Funktionen der Klasse {@link ArrayMap}.
 */
class TestArrayMap {

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


}
