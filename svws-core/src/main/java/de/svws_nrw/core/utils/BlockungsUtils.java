package de.svws_nrw.core.utils;

import java.util.Comparator;
import java.util.List;

import de.svws_nrw.core.data.gost.GostBlockungListeneintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse bietet hilfreiche statische Methoden für die Blockung.
 *
 * @author Benjamin A. Bartsch
 */
public final class BlockungsUtils {

	private BlockungsUtils() {
		throw new IllegalStateException("Instantiation not allowed");
	}

	/** Ein Comparator zum Sortieren von {@link GostBlockungListeneintrag}, welcher beachtet, dass der Suffix Zahlen enthält. */
	private static final @NotNull Comparator<@NotNull GostBlockungListeneintrag> _compGostBlockungListeneintrag = (final @NotNull GostBlockungListeneintrag a, final @NotNull GostBlockungListeneintrag b) -> {
		// Zerlege den Namen der Blockung in [Präfix, Suffix]. Der Suffix besteht nur aus Ziffern.
		final @NotNull String @NotNull [] splitA = extractTrailingNumber(a.name.trim());
		final @NotNull String @NotNull [] splitB = extractTrailingNumber(b.name.trim());

		// Sortiere nach dem Präfix.
		final @NotNull String praefixA = splitA[0].trim();
		final @NotNull String praefixB = splitB[0].trim();
		final int cmpPrefix = praefixA.compareTo(praefixB);
		if (cmpPrefix != 0)
			return cmpPrefix;

		// Sortiere nach dem Zahlen-Suffix.
		final int sizeA = splitA[1].length();
		final int sizeB = splitB[1].length();
		final @NotNull String suffixA = sizeA >= sizeB ? splitA[1] : StringUtils.fillWithLeadingZeros(splitA[1], sizeB);
		final @NotNull String suffixB = sizeB >= sizeA ? splitB[1] : StringUtils.fillWithLeadingZeros(splitB[1], sizeA);
		final int cmpSuffix = suffixA.compareTo(suffixB);
		if (cmpSuffix != 0)
			return cmpSuffix;

		// Sortiere nach der ID.
		return Long.compare(a.id, b.id);
	};

	/**
	 * Sortiert die Liste nach dem Namen der Blockung. Ist der Suffix eine Zahl, so wird dies bei der Sortierung beachtet.
	 *
	 * @param list Die zu sortierende Liste.
	 */
	public static void sortGostBlockungListeneintrag(final @NotNull List<@NotNull GostBlockungListeneintrag> list) {
		list.sort(_compGostBlockungListeneintrag);
	}

	private static @NotNull String @NotNull [] extractTrailingNumber(final @NotNull String s) {
		final String prefix = s.replaceAll("\\d*$", "");
		final String suffix = s.substring(prefix.length());
		return new String[] {prefix, suffix};
	}

	/**
	 * Liefert das Minimum beider Zahlenwerte. Ein NULL-Wert wird ignoriert.
	 *
	 * @param value1        Der 1. Zahlenwert.
	 * @param value2orNull  Der 2. Zahlenwert (oder NULL).
	 *
	 * @return das Minimum beider Zahlenwerte. Ein NULL-Wert wird ignoriert.
	 */
	public static int minVI(final int value1, final Integer value2orNull) {
		if (value2orNull == null)
			return value1;
		final int value2 = value2orNull;
		return value1 <= value2 ? value1 : value2;
	}

	/**
	 * Liefert das Maximum beider Zahlenwerte. Ein NULL-Wert wird ignoriert.
	 *
	 * @param value1        Der 1. Zahlenwert.
	 * @param value2orNull  Der 2. Zahlenwert (oder NULL).
	 *
	 * @return das Maximum beider Zahlenwerte. Ein NULL-Wert wird ignoriert.
	 */
	public static int maxVI(final int value1, final Integer value2orNull) {
		if (value2orNull == null)
			return value1;
		final int value2 = value2orNull;
		return value1 >= value2 ? value1 : value2;
	}

	/**
	 * Liefert das Minimum zweier Integer-Zahlenwerte. Ein NULL-Wert wird ignoriert.
	 *
	 * @param a  Der 1. Zahlenwert (oder NULL).
	 * @param b  Der 2. Zahlenwert (oder NULL).
	 *
	 * @return das Minimum zweier Integer-Zahlenwerte. Ein NULL-Wert wird ignoriert.
	 */
	public static Integer minII(final Integer a, final Integer b) {
		if (a == null)
			return b;
		if (b == null)
			return a;
		return a <= b ? a : b;
	}

	/**
	 * Liefert das Maximum zweier Integer-Zahlenwerte. Ein NULL-Wert wird ignoriert.
	 *
	 * @param a  Der 1. Zahlenwert (oder NULL).
	 * @param b  Der 2. Zahlenwert (oder NULL).
	 *
	 * @return das Maximum zweier Integer-Zahlenwerte. Ein NULL-Wert wird ignoriert.
	 */
	public static Integer maxII(final Integer a, final Integer b) {
		if (a == null)
			return b;
		if (b == null)
			return a;
		return a >= b ? a : b;
	}

}
