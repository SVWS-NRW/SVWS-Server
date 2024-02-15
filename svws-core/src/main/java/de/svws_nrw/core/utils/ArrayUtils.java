package de.svws_nrw.core.utils;

import java.util.Random;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse bietet hilfreiche statische Methoden für Arrays.
 *
 * @author Benjamin A. Bartsch
 */
public final class ArrayUtils {

	private ArrayUtils() {
	}

	/**
	 * Liefert das Array mit den Werten 0 bis size-1 in permutierter Reihenfolge.
	 * <br> Beispiel für size = 5 --> {2, 3, 0, 1, 4}
	 *
	 * @param size    Die Länge des Arrays.
	 * @param random  Ein {@link Random}-Objekt zum Permutieren der Elemente des Arrays.
     *
     * @return das Array mit den Werten 0 bis size-1 in permutierter Reihenfolge.
	 */
   	public static @NotNull int[] getIndexPermutation(final int size, final @NotNull Random random) {

   		final int[] perm = new int[size];
		for (int i = 0; i < perm.length; i++)
			perm[i] = i;

		for (int i1 = 0; i1 < perm.length; i1++) {
			final int i2 = random.nextInt(perm.length);
			final int save1 = perm[i1];
			final int save2 = perm[i2];
			perm[i1] = save2;
			perm[i2] = save1;
		}

		return perm;
	}

   	/**
   	 * Liefert eine Kopie des übergebenen Arrays ergänzt um eine übergebene Zahl.
   	 *
   	 * @param a      Das übergebene Array.
   	 * @param value  Die Zahl, welche ergänzt wird.
   	 *
   	 * @return eine Kopie des übergebenen Arrays ergänzt um eine übergebene Zahl.
   	 */
	public static @NotNull int[] erweitern(final @NotNull int[] a, final int value) {
		final int[] temp = new int[a.length + 1];
		System.arraycopy(a, 0, temp, 0, a.length);
		temp[a.length] = value;
		return temp;
	}

}
