package de.svws_nrw.core.utils.gost;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.data.gost.GostFach;

/**
 * Prüft, welche Fächer als erster Leistungskurs wählbar sind. Die Regel muss mit der Belegprüfung übereinstimmen:
 * Deutsch, Mathematik, eine fortgeführte Fremdsprache oder eine klassische Naturwissenschaft (BI, CH, PH).
 */
@DisplayName("Teste die Wählbarkeit eines Faches als erster Leistungskurs")
class TestGostFachUtilsLeistungskurs1 {

	/**
	 * Erzeugt ein Fach, welches in allen Halbjahren der Qualifikationsphase und als Leistungskurs möglich ist.
	 *
	 * @param kuerzel   das Kürzel des Faches
	 *
	 * @return das Fach
	 */
	private static GostFach fach(final String kuerzel) {
		final GostFach fach = new GostFach();
		fach.kuerzel = kuerzel;
		fach.istMoeglichAbiLK = true;
		fach.istMoeglichQ11 = true;
		fach.istMoeglichQ12 = true;
		fach.istMoeglichQ21 = true;
		fach.istMoeglichQ22 = true;
		return fach;
	}

	/** Deutsch, Mathematik und die klassischen Naturwissenschaften sind als erster Leistungskurs wählbar. */
	@Test
	@DisplayName("Deutsch, Mathematik und klassische Naturwissenschaften sind wählbar")
	void testWaehlbar() {
		for (final String kuerzel : new String[] { "D", "M", "BI", "CH", "PH" }) {
			assertTrue(GostFachUtils.istWaehlbarLeistungskurs1(fach(kuerzel)), kuerzel);
		}
	}

	/** Die neu einsetzenden Naturwissenschaften (EL, IF, TC) sind nicht als erster Leistungskurs wählbar. */
	@Test
	@DisplayName("Neu einsetzende Naturwissenschaften EL, IF und TC sind nicht wählbar")
	void testNeuEinsetzendeNaturwissenschaften() {
		for (final String kuerzel : new String[] { "EL", "IF", "TC" }) {
			assertFalse(GostFachUtils.istWaehlbarLeistungskurs1(fach(kuerzel)), kuerzel);
		}
	}

	/** Eine fortgeführte Fremdsprache ist wählbar, eine neu einsetzende nicht. */
	@Test
	@DisplayName("Fremdsprachen sind nur fortgeführt wählbar")
	void testFremdsprachen() {
		final GostFach fortgefuehrt = fach("E");
		assertTrue(GostFachUtils.istWaehlbarLeistungskurs1(fortgefuehrt));
		final GostFach neuEinsetzend = fach("S");
		neuEinsetzend.istFremdSpracheNeuEinsetzend = true;
		assertFalse(GostFachUtils.istWaehlbarLeistungskurs1(neuEinsetzend));
	}

	/** Ohne Möglichkeit als Leistungskurs oder ohne Belegbarkeit in allen Q-Halbjahren ist ein Fach nicht wählbar. */
	@Test
	@DisplayName("Fächer ohne LK-Möglichkeit oder mit fehlendem Q-Halbjahr sind nicht wählbar")
	void testVoraussetzungen() {
		final GostFach keinLK = fach("M");
		keinLK.istMoeglichAbiLK = false;
		assertFalse(GostFachUtils.istWaehlbarLeistungskurs1(keinLK));
		final GostFach keinQ22 = fach("M");
		keinQ22.istMoeglichQ22 = false;
		assertFalse(GostFachUtils.istWaehlbarLeistungskurs1(keinQ22));
		assertFalse(GostFachUtils.istWaehlbarLeistungskurs1(null));
	}

}
