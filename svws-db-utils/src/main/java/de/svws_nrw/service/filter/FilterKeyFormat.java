package de.svws_nrw.service.filter;

import java.util.regex.Pattern;

/**
 * Das gemeinsame Format der Schlüssel von Filterkategorien und Filterattributen. Ein Schlüssel besteht aus mindestens zwei durch Punkte getrennten
 * Segmenten aus Kleinbuchstaben und Ziffern, wobei jedes Segment mit einem Buchstaben beginnt (z. B. {@code schueler.wohnort}).
 *
 * <p>Die Segmente werden einzeln geprüft, statt eine wiederholte Gruppe in einem regulären Ausdruck zu verwenden. Das vermeidet die rekursive
 * Auswertung von Java und damit einen {@link StackOverflowError} bei sehr langen Eingaben.</p>
 */
final class FilterKeyFormat {

	private static final Pattern SEGMENT = Pattern.compile("[a-z][a-z0-9]*");

	private FilterKeyFormat() {
	}

	/**
	 * Prüft, ob der übergebene Wert dem Format eines Filterschlüssels entspricht.
	 *
	 * @param value der zu prüfende Wert
	 *
	 * @return {@code true}, wenn der Wert aus mindestens zwei gültigen, durch Punkte getrennten Segmenten besteht
	 *
	 * @throws NullPointerException wenn der Wert {@code null} ist
	 */
	static boolean isValid(final String value) {
		final String[] segments = value.split("\\.", -1);
		if (segments.length < 2) {
			return false;
		}
		for (final String segment : segments) {
			if (!SEGMENT.matcher(segment).matches()) {
				return false;
			}
		}
		return true;
	}
}
