package de.svws_nrw.core.utils;

import java.util.Collection;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse bietet hilfreiche statische Methoden für Strings.
 *
 * @author Benjamin A. Bartsch
 */
public final class StringUtils {

	private static final @NotNull String @NotNull [] buchstaben = new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
			                                                          "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
			                                                          "U", "V", "W", "X", "Y", "Z"};

	private StringUtils() {
	}

	/**
	 * Liefert einen durch Komma getrennten String aller Inhalte der übergebenen {@link Collection}.
	 *
	 * @param collection  Die zu verbindenden Inhalte.
	 *
	 * @return einen durch Komma getrennten String aller Inhalte der übergebenen {@link Collection}.
	 */
	public static @NotNull String collectionToCommaSeparatedString(final @NotNull Collection<@NotNull String> collection) {
		final @NotNull StringBuilder sb = new StringBuilder();

		for (final @NotNull String s : collection)
			if (sb.isEmpty())
				sb.append(s);
			else
				sb.append(", " + s);

		return sb.toString();
	}

	/**
	 * Liefert die umgewandelte Zahl aus dem Bereich 0=A bis 25=Z in einen Buchstaben um.
	 *
	 * @param number  Die umzuwandelnde Zahl.
	 *
	 * @return die umgewandelte Zahl aus dem Bereich 0=A bis 25=Z in einen Buchstaben um.
	 */
	public static @NotNull String numberToLetterIndex0(final int number) {
		return (number < 0) || (number > 25) ? "" : buchstaben[number];
	}

	/**
	 * Liefert die umgewandelte Zahl aus dem Bereich 1=A bis 26=Z in einen Buchstaben um.
	 *
	 * @param number  Die umzuwandelnde Zahl.
	 *
	 * @return die umgewandelte Zahl aus dem Bereich 1=A bis 26=Z in einen Buchstaben um.
	 */
	public static @NotNull String numberToLetterIndex1(final int number) {
		return (number < 1) || (number > 26) ? "" : buchstaben[number - 1];
	}

	/**
	 * Liefert einen String der Zahl, welche ggf. mit Nullen vorne aufgefüllt wurde.
	 *
	 * @param zahl        Die umzuwandelnde Zahl.
	 * @param minGroesse  Die Mindestgröße des Ergebnis-Strings.
	 *
	 * @return einen String der Zahl, welche ggf. mit Nullen vorne aufgefüllt wurde.
	 */
	public static @NotNull String padZahl(final int zahl, final int minGroesse) {
		final String sNumber = "" + zahl;

		final StringBuilder sb = new StringBuilder();
		while (sb.length() + sNumber.length() < minGroesse)
			sb.append('0');
		sb.append(sNumber);

	    return sb.toString();
	}

	/**
	 * Liefert eine Kopie des String, welcher vorne mit 0en aufgefüllt wurde, bis "size" erreicht wurde.
	 *
	 * @param s     Der zu kopierende String.
	 * @param size  Die Mindestgröße des Ergebnis-Strings.
	 *
	 * @return eine Kopie des String, welcher vorne mit 0en aufgefüllt wurde, bis "size" erreicht wurde.
	 */
	public static @NotNull String fillWithLeadingZeros(final @NotNull String s, final int size) {

		final StringBuilder sb = new StringBuilder();
		while (sb.length() + s.length() < size)
			sb.append('0');
		sb.append(s);

	    return sb.toString();
	}
}
