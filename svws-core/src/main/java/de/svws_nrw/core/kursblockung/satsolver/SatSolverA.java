package de.svws_nrw.core.kursblockung.satsolver;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse definiert alle Methoden die ein SAT-Solver haben muss. Diese abstrakte Klasse garantiert, dass
 * unterschiedliche Implementierungen getauscht werden können.
 *
 * @author Benjamin A. Bartsch
 */
public abstract class SatSolverA {

	/**
	 * Ergebnis der Methode {@link #solve}, wenn die Formel unlösbar ist.
	 */
	public static final int RESULT_UNSATISFIABLE = -1;

	/**
	 * Ergebnis der Methode {@link #solve}, wenn die Lösbarkeit der Formel in der vorgegebenen Zeit nicht bestimmt
	 * werden konnte.
	 */
	public static final int RESULT_UNKNOWN = 0;

	/**
	 * Ergebnis der Methode {@link #solve}, wenn die Formel gelöst wurde ist.
	 */
	public static final int RESULT_SATISFIABLE = 1;

	/**
	 * Erzeugte eine neue Variable. Den zurückgegebenen Integer-Wert darf man nun in Klauseln (auch negiert)
	 * benutzen. Eine Variable hat niemals den Wert 0, da dieser Wert nicht negiert werden kann.
	 *
	 * @return Die Nummer der neuen Variablen.
	 */
	public abstract int createNewVar();

	/**
	 * Hinzufügen einer Klausel. Eine Klausel ist eine Menge von Variablen, die mit einem logischen ODER verknüpft
	 * sind. Die Variablen dürfen negiert sein.<br>
	 * {@code Beispiel: [-3, 8, 2] bedeutet (NOT x3) OR x8 OR x2}<br>
	 * Die Menge aller Klauseln sind mit einem AND verknüpft.
	 *
	 * @param pVars Die Variablen (auch negiert) der Klausel mit den zuvor generierten Variablen. Ist das Array
	 *              leer, wird es ignoriert.
	 */
	public abstract void addClause(@NotNull int[] pVars);

	/**
	 * Gibt den Wert TRUE oder FALSE der angefragten Variable zurück. Das Verhalten dieser Methode ergibt nur dann
	 * Sinn, wenn der Solver mit SATISFIABLE nach Aufruf der Methode {@link #solve } geantwortet hat.
	 *
	 * @param pVar Die angefragte Variable (kann auch negativ sein, aber nicht 0).
	 * @return TRUE, falls die angefragte Variable nach der Berechnnung TRUE ist.
	 */
	public abstract boolean isVarTrue(int pVar);

	/**
	 * Startet die Berechnung für maximal {@code maxTimeMillis} Millisekunden und liefert einen der drei möglichen
	 * Werte {@link #RESULT_SATISFIABLE}, {@link #RESULT_UNKNOWN} oder {@link #RESULT_UNSATISFIABLE}.
	 *
	 * @param maxTimeMillis Die maximale Berechnungszeit (in Millisekunden).
	 *
	 * @return Liefert einen der drei möglichen Werte {@link #RESULT_SATISFIABLE}, {@link #RESULT_UNKNOWN} oder
	 *         {@link #RESULT_UNSATISFIABLE}.
	 */
	public abstract int solve(long maxTimeMillis);

	/**
	 * Liefert die interne Anzahl an erzeugten Variablen.
	 *
	 * @return Die interne Anzahl an erzeugten Variablen.
	 */
	public abstract int getVarCount();

	/**
	 * Liefert die interne Anzahl an erzeugten Klauseln.
	 *
	 * @return Die interne Anzahl an erzeugten Klauseln.
	 */
	public abstract int getClauseCount();

}
