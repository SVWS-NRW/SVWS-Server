package de.svws_nrw.service.filter;

import java.util.Objects;

/**
 * Der eindeutige Schlüssel eines Filterattributes. Der Schlüssel besteht aus mindestens zwei durch Punkte getrennten Segmenten aus Kleinbuchstaben und Ziffern, wobei jedes Segment mit einem Buchstaben beginnt (z. B. {@code schueler.wohnort}).
 *
 * @param value Wert des Schlüssels
 */
public record FilterAttributeKey(String value) {

	/**
	 * Erzeugt einen neuen {@link FilterAttributeKey}.
	 *
	 * @param value Wert des Schlüssels
	 *
	 * @throws NullPointerException wenn der Wert {@code null} ist
	 * @throws IllegalArgumentException wenn der Wert nicht dem erwarteten Format entspricht
	 */
	public FilterAttributeKey {
		Objects.requireNonNull(value, "Der Schlüssel eines Filterattributes darf nicht null sein.");

		if (!FilterKeyFormat.isValid(value)) {
			throw new IllegalArgumentException(("Ungültiger Schlüssel für ein Filterattribut: '%s'. Erwartet werden mindestens zwei durch Punkte getrennte "
					+ "Segmente in Kleinbuchstaben, z. B. 'schueler.wohnort'.").formatted(value));
		}
	}
}
