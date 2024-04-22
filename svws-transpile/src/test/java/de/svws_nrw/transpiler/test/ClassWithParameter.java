package de.svws_nrw.transpiler.test;

import jakarta.validation.constraints.NotNull;

/**
 * Eine generische Klasse mit Werten
 *
 * @param <T> der Typ des Wertes
 */
public class ClassWithParameter<@NotNull T> {

	private final @NotNull T value;

	/**
	 * Erzeugt ein neues Objekt mit dem übergebenen Wert
	 *
	 * @param value   der Wert
	 */
	public ClassWithParameter(final @NotNull T value) {
		this.value = value;
	}

	/**
	 * Gibt den Wert zurück.
	 *
	 * @return der wert
	 */
	public @NotNull T getValue() {
		return this.value;
	}

}
