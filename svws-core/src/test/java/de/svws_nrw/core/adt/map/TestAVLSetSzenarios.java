package de.svws_nrw.core.adt.map;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.adt.set.AVLSet;

/**
 * Testet die Klasse {@link AVLSet}, deren Methoden und ihre abhängige Klassen.
 *
 * @author Benjamin A. Bartsch
 */
class TestAVLSetSzenarios {
	private final AVLSet<String> setOfString = new AVLSet<>();

	/**
	 * Testet die Klasse {@link AVLSet}, Szenario 1.
	 */
	@Test
	@DisplayName("Testet die KLasse AVLSet, Szenario 1.")
	void testSzenario_01() {
		assertEquals(true, setOfString.add("2.07"));
		assertEquals(true, setOfString.add("3.08"));
		assertEquals(2, setOfString.size());
	}

}
