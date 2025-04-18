package de.svws_nrw.core.utils.stundenplan;

import java.util.Comparator;
import jakarta.validation.constraints.NotNull;

/**
 * Comparator, um Integers zu vergleichen, da der Transpiler dies wahrscheinlich benötigt.
 * Collections.sort funktioniert nicht und List.sort(null) auch nicht.
 */
public final class IntegerComparator implements Comparator<Integer> {

	/**
	 * Leerer Standardkonstruktor.
	 */
	public IntegerComparator() {
		// leer
	}

	@Override
	public int compare(final @NotNull Integer o1, final @NotNull Integer o2) {
		return o1 - o2;
	}

}
