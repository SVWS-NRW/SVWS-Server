package de.svws_nrw.core.kursblockung;

import java.util.Random;

import jakarta.validation.constraints.NotNull;

// TODO BAR KursblockungStatic --> adt???

/** Diese Klasse bietet statische Helfer-Methoden an.
 *
 * @author Benjamin A. Bartsch */
public class KursblockungStatic {

	private KursblockungStatic() {
		throw new IllegalStateException("Instantiation of " + KursblockungStatic.class.getName() + " not allowed");
	}

	/** Erzeugt ein Array der Größe {@code n}, füllt es mit den Zahlen {@code 0 bis n-1} und permutiert das Array dann
	 * zufällig.
	 *
	 * @param  pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param  n       Die Größe des Arrays.
	 * @return         Eine Array-Referenz. */
	public static @NotNull int[] gibPermutation(final @NotNull Random pRandom, final int n) {
		final @NotNull int[] temp = new int[n];
		for (int i = 0; i < n; i++) {
			temp[i] = i;
		}
		aktionPermutiere(pRandom, temp);
		return temp;
	}

	/** Permutiert das Array {@code perm} zufällig.
	 *
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param perm    Das zu permutierende Array. */
	public static void aktionPermutiere(final @NotNull Random pRandom, final @NotNull int[] perm) {
		final int n = perm.length;
		for (int i1 = 0; i1 < n; i1++) {
			final int i2 = pRandom.nextInt(n);
			final int s1 = perm[i1];
			final int s2 = perm[i2];
			perm[i1] = s2;
			perm[i2] = s1;
		}
	}


}
