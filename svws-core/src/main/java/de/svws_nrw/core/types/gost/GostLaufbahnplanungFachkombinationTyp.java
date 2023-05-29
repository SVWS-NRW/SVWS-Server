package de.svws_nrw.core.types.gost;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung enthält die unterschiedlichen Typen
 * von Fach-Kombinationen im Rahmen der Laufbahnplanung,
 * d.h. ob sie zulässig sind oder auch vorhanden sein müssen.
 */
public enum GostLaufbahnplanungFachkombinationTyp {

	/** Gibt an, das eine Fachkombination unzulässig ist */
	VERBOTEN(0),

	/** Gibt an, das eine Fachkombination erforderlich ist */
	ERFORDERLICH(1);

	/** Der Typ als Integer-Wert */
	private final int value;

	/**
	 * Erstellt einen neuen Fachkombinations-Typ
	 *
	 * @param value   der numerische Wert des Typs
	 */
	GostLaufbahnplanungFachkombinationTyp(final int value) {
		this.value = value;
	}

	/**
	 * Gibt den numerischen Wert des Fachkombination-Typs zurück.
	 *
	 * @return der numerische Wert
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Gibt den Fachkombination-Typ für den angegebenen numerischen Wert
	 * zurück.
	 *
	 * @param value   der numerische Wert
	 *
	 * @return der Typ der Fachkombination
	 *
	 * @throws IllegalArgumentException   bei einem ungültigen numerischen Wert
	 */
	public static @NotNull GostLaufbahnplanungFachkombinationTyp fromValue(final int value) throws IllegalArgumentException {
		if ((value < 0) || (value > 1))
			throw new IllegalArgumentException("Der Parameter value " + value + "ist für den Typ einer Fachkombination ungültig.");
		return (value == 0) ? GostLaufbahnplanungFachkombinationTyp.VERBOTEN : GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH;
	}

}
