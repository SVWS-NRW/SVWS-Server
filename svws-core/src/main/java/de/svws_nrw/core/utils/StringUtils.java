package de.svws_nrw.core.utils;

import java.util.Collection;

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

}
