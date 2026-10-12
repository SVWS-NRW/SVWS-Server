package de.svws_nrw.service.filter;

import java.util.Objects;

/**
 * Der eindeutige Schlüssel einer Filterkategorie. Der Schlüssel besteht aus mindestens zwei durch Punkte getrennten Segmenten aus Kleinbuchstaben und Ziffern, wobei jedes Segment mit einem Buchstaben beginnt (z. B. {@code schueler.individualdaten}).
 *
 * @param value Wert des Schlüssels
 */
public record FilterCategoryKey(String value) {

	/**
	 * Erzeugt einen neuen {@link FilterCategoryKey}.
	 *
	 * @param value Wert des Schlüssels
	 *
	 * @throws NullPointerException wenn der Wert {@code null} ist
	 * @throws IllegalArgumentException wenn der Wert nicht dem erwarteten Format entspricht
	 */
	public FilterCategoryKey {
		Objects.requireNonNull(value, "Der Schlüssel einer Filterkategorie darf nicht null sein.");

		if (!FilterKeyFormat.isValid(value)) {
			throw new IllegalArgumentException(("Ungültiger Schlüssel für eine Filterkategorie: '%s'. Erwartet werden mindestens zwei durch Punkte getrennte "
					+ "Segmente in Kleinbuchstaben, z. B. 'schueler.individualdaten'.").formatted(value));
		}
	}
}
