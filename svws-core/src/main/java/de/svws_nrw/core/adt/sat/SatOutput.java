package de.svws_nrw.core.adt.sat;

import jakarta.validation.constraints.NotNull;

/**
 * ...
 * @author Benjamin A. Bartsch
 */
public final class SatOutput {

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
	private @NotNull int[] solution;

	/**
	 * Konstruktor.
	 */
	public SatOutput() {
		this.solution = new int[0];
	}

	/**
	 * Konstruktor für den Fall, dass eine Lösung gefunden wurde.
	 *
	 *
	 *
	 * @param pSolution Die Lösung der Variablenbelegungen.
	 */
	public SatOutput(@NotNull final int[] pSolution) {
		this.solution = pSolution;
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
	 * Setzt die Anzahl an Variablen.
	 * @param pSize Die Anzahl an Variablen.
	 */
	public void setSolutionSize(final int pSize) {
		this.solution = new int[pSize + 1];
	}

}
