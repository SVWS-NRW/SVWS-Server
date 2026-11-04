package de.svws_nrw.core.utils.gost;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.types.gost.GostHalbjahr;

/**
 * Prüft die Regeln für die Wiederholung zweier Halbjahre im folgenden Abiturjahrgang: Der Schüler steigt im Abschnitt nach dem Anlegen
 * in den neuen Jahrgang ein und gehört für Abschnitte davor noch zum Jahrgang seines Lernabschnitts.
 */
@DisplayName("Teste die Wiederholung im folgenden Abiturjahrgang")
class TestGostAbiturjahrUtilsWiederholung {

	/**
	 * Von Q1.2 nach Q1.1: Eine im Schuljahr 2026/27, 2. Halbjahr angelegte Wiederholung führt in die Q1.1 des Abiturjahrgangs 2029.
	 */
	@Test
	@DisplayName("Von Q1.2 nach Q1.1 im Folgeschuljahr")
	void testQ12NachQ11() {
		final GostHalbjahr halbjahr = GostAbiturjahrUtils.getWiederholungHalbjahr(2029, 2026, 2);
		assertEquals(GostHalbjahr.Q11, halbjahr);
		assertTrue(GostAbiturjahrUtils.liegtVorWiederholung(2029, halbjahr, 2026, 2));
		assertFalse(GostAbiturjahrUtils.liegtVorWiederholung(2029, halbjahr, 2027, 1));
		assertFalse(GostAbiturjahrUtils.liegtVorWiederholung(2029, halbjahr, 2027, 2));
	}

	/**
	 * Von Q1.1 nach EF.2: Eine im Schuljahr 2026/27, 1. Halbjahr angelegte Wiederholung führt in die EF.2 des Abiturjahrgangs 2029.
	 */
	@Test
	@DisplayName("Von Q1.1 nach EF.2 im selben Schuljahr")
	void testQ11NachEF2() {
		final GostHalbjahr halbjahr = GostAbiturjahrUtils.getWiederholungHalbjahr(2029, 2026, 1);
		assertEquals(GostHalbjahr.EF2, halbjahr);
		assertTrue(GostAbiturjahrUtils.liegtVorWiederholung(2029, halbjahr, 2026, 1));
		assertFalse(GostAbiturjahrUtils.liegtVorWiederholung(2029, halbjahr, 2026, 2));
	}

	/**
	 * Von Q2.1 nach Q1.2: Eine im Schuljahr 2026/27, 1. Halbjahr angelegte Wiederholung führt in die Q1.2 des Abiturjahrgangs 2028.
	 */
	@Test
	@DisplayName("Von Q2.1 nach Q1.2 im selben Schuljahr")
	void testQ21NachQ12() {
		final GostHalbjahr halbjahr = GostAbiturjahrUtils.getWiederholungHalbjahr(2028, 2026, 1);
		assertEquals(GostHalbjahr.Q12, halbjahr);
		assertTrue(GostAbiturjahrUtils.liegtVorWiederholung(2028, halbjahr, 2026, 1));
		assertFalse(GostAbiturjahrUtils.liegtVorWiederholung(2028, halbjahr, 2026, 2));
	}

	/**
	 * Von EF.2 nach EF.1: Eine im Schuljahr 2026/27, 2. Halbjahr angelegte Wiederholung führt in die EF.1 des Abiturjahrgangs 2030.
	 */
	@Test
	@DisplayName("Von EF.2 nach EF.1 im Folgeschuljahr")
	void testEF2NachEF1() {
		final GostHalbjahr halbjahr = GostAbiturjahrUtils.getWiederholungHalbjahr(2030, 2026, 2);
		assertEquals(GostHalbjahr.EF1, halbjahr);
		assertTrue(GostAbiturjahrUtils.liegtVorWiederholung(2030, halbjahr, 2026, 2));
		assertFalse(GostAbiturjahrUtils.liegtVorWiederholung(2030, halbjahr, 2027, 1));
	}

	/**
	 * Von Q2.2 nach Q2.1: Eine im Schuljahr 2026/27, 2. Halbjahr angelegte Wiederholung führt in die Q2.1 des Abiturjahrgangs 2028.
	 */
	@Test
	@DisplayName("Von Q2.2 nach Q2.1 im Folgeschuljahr")
	void testQ22NachQ21() {
		final GostHalbjahr halbjahr = GostAbiturjahrUtils.getWiederholungHalbjahr(2028, 2026, 2);
		assertEquals(GostHalbjahr.Q21, halbjahr);
		assertTrue(GostAbiturjahrUtils.liegtVorWiederholung(2028, halbjahr, 2026, 2));
		assertFalse(GostAbiturjahrUtils.liegtVorWiederholung(2028, halbjahr, 2027, 1));
	}

	/**
	 * Ein Jahrgang, der im Folgeabschnitt noch nicht oder nicht mehr in der Oberstufe ist, ergibt kein Einstiegshalbjahr.
	 */
	@Test
	@DisplayName("Jahrgang außerhalb der Oberstufe")
	void testJahrgangAusserhalbDerOberstufe() {
		assertNull(GostAbiturjahrUtils.getWiederholungHalbjahr(2031, 2026, 2));
		assertNull(GostAbiturjahrUtils.getWiederholungHalbjahr(2027, 2026, 2));
	}

}
