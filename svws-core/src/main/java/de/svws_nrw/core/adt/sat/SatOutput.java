package de.svws_nrw.core.adt.sat;

import jakarta.validation.constraints.NotNull;

/**
 * ...
 * @author Benjamin A. Bartsch
 */
public final class SatOutput {

	/** Es existiert eine Lösung. */
	public static final int TYPE_UNKNOWN = 0;

	/** Es existiert (beweisbar) keine Lösung existiert. */
	public static final int TYPE_SATISFIABLE = 1;

	/** Unbekannt, ob eine Lösung existiert (z. B. bei einem TimeOut). */
	public static final int TYPE_UNSATISFIABLE = 2;

	/**
	 * Eine Lösung einer Formel {@link SatInput}. Das Format muss wie folgt aussehen:
	 * <br><br>
	 * Das Array muss um Eins größer sein, als die Anzahl der verwendeten Variablen.
	 * An Position i steht der Wert i. Positiv, falls die Variable i TRUE ist, oder negativ,
	 * falls die Variable i FALSE ist. Der 0-Index wird ignoriert.
	 * <br><br>
	 * Beispiel solution = {0, -1, 2, -3, -4} bedeutet x1=FALSE, x2=TRUE, x3=FALSE, x4=FALSE
	 *
	 */
	private final @NotNull int[] solution;

	private final int type;

	/**
	 * Erzeugt eine Lösung anhand der übergebenen Parameter.
	 *
	 * @param pSolution Das Array der Variablen.
	 * @param pType     Einer der drei möglichen Typen.
	 */
	private SatOutput(@NotNull final int[] pSolution, final int pType) {
		solution = pSolution;
		type = pType;
	}

	/**
	 * Getter für data;
	 *
	 * @return data
	 */
	public @NotNull int[] getSolution() {
		return solution;
	}

	/**
	 * Liefert TRUE, falls eine Lösung existiert.
	 *
	 * @return TRUE, falls eine Lösung existiert.
	 */
	public boolean isSatisfiable() {
		return type == TYPE_SATISFIABLE;
	}

	/**
	 * Liefert TRUE, falls (beweisbar) keine Lösung existiert.
	 *
	 * @return TRUE, falls (beweisbar) keine Lösung existiert.
	 */
	public boolean isUnsatisfiable() {
		return type == TYPE_UNSATISFIABLE;
	}

	/**
	 * Liefert TRUE, falls unbekannt ist, ob eine Lösung existiert (z. B. bei einem TimeOut).
	 *
	 * @return TRUE, falls unbekannt ist, ob eine Lösung existiert (z. B. bei einem TimeOut).
	 */
	public boolean isUnknown() {
		return type == TYPE_UNKNOWN;
	}

	/**
	 * Liefert ein Objekt dieser Klasse mit dem Typ TYPE_UNKNOWN  (z. B. bei einem TimeOut).
	 *
	 * @return ein Objekt dieser Klasse mit dem Typ TYPE_UNKNOWN  (z. B. bei einem TimeOut).
	 */
	public static @NotNull SatOutput createUNKNOWN() {
		return new SatOutput(new int[0], TYPE_UNKNOWN);
	}

	/**
	 * Liefert ein Objekt dieser Klasse mit dem Typ TYPE_UNSATISFIABLE.
	 *
	 * @return ein Objekt dieser Klasse mit dem Typ TYPE_UNSATISFIABLE
	 */
	public static @NotNull SatOutput createUNSATISFIABLE() {
		return new SatOutput(new int[0], TYPE_UNSATISFIABLE);
	}

	/**
	 * Liefert ein Objekt dieser Klasse mit dem Typ TYPE_SATISFIABLE.
	 *
	 * @param pSolution Die Lösung der Variablenbelegungen.
	 * @return ein Objekt dieser Klasse mit dem Typ TYPE_SATISFIABLE.
	 */
	public static @NotNull SatOutput createSATISFIABLE(@NotNull final int[] pSolution) {
		return new SatOutput(pSolution, TYPE_SATISFIABLE);
	}

	/**
	 * Liefert eine Kopie, welche aber potentiell eine andere Lösung besitzt.
	 *
	 * @param pOutput   Das zu kopierende Objekt.
	 * @param pSolution Die Lösung der Variablenbelegungen.
	 * @return eine Kopie, welche aber potentiell eine andere Lösung besitzt.
	 */
	public static @NotNull SatOutput createCopy(@NotNull final SatOutput pOutput, @NotNull final int[] pSolution) {
		return new SatOutput(pSolution, pOutput.type);
	}

}
