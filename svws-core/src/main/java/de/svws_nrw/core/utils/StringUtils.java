package de.svws_nrw.core.utils;

import java.util.Set;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse bietet hilfreiche statische Methoden für Strings.
 *
 * @author Benjamin A. Bartsch
 */
public final class StringUtils {

	private StringUtils() {
	}

	/**
	 * Liefert einen durch Komma getrennten String aller Inhalte des übergebenen Sets.
	 *
	 * @param set  Das Set der zu verbindenden Inhalte.
	 *
	 * @return einen durch Komma getrennten String aller Inhalte des übergebenen Sets.
	 */
	public static @NotNull String toKommaSeperatedString(final @NotNull Set<@NotNull String> set) {
		final @NotNull StringBuilder sb = new StringBuilder();

		for (final @NotNull String s : set)
			if (sb.isEmpty())
				sb.append(s);
			else
				sb.append(", " + s);

		return sb.toString();
	}

}
