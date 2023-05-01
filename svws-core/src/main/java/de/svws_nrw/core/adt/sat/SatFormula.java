package de.svws_nrw.core.adt.sat;

import java.util.ArrayList;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse definiert eine aussagenlogische Formel in konjunktiver Normalform (CNF).
 * Die Formel besteht aus einer Menge von Klauseln, welche mit einem logischen UND verbunden sind.
 * Jede Klausel beinhaltet booleschen Variablen (auch negiert), welche mit einem logischen ODER verbunden sind.
 *  <br>
 * Ein sogenannter SAT-Solver erhält diese Formel als Eingabe und sucht nach einer Belegung der Variablen,
 * so dass jede Klausel erfüllt (also TRUE) ist.
 *
 * @author Benjamin A. Bartsch
 */
public final class SatFormula {

	/** Die aktuelle Anzahl an Variablen. */
	private int _nVars;

	/** Die aktuelle Anzahl an Variablen. */
	private final @NotNull ArrayList<@NotNull int[]> _clauses;

	/**
	 * Erzeugt eine neues Objekt. Anschließend lassen sich Variablen erzeugen und Klauseln hinzufügen.
     * Möchte man die Formel f = (x1 OR x2 OR NOT x3) AND (NOT x2 OR x3) AND (x5) kodieren, so funktioniert das so:  <br>
     * <pre>
     *     SatFormula f = new SatFormula();
     *     int x1 = f.createNewVar();
     *     int x2 = f.createNewVar();
     *     int x3 = f.createNewVar();
     *     f.createNewVar(); // not used
     *     int x5 = f.createNewVar();
     *
     *     f.addClause(new int[] {x1, x2, -x3}); // adds {1, 2, -3}
     *     f.addClause(new int[] {-x2, x3});     // adds {-2, 3}
     *     f.addClause(new int[] {x5});          // adds {5}
     * </pre>
     *
	 */
	public SatFormula() {
		_nVars = 0;
		_clauses = new ArrayList<>();
	}

	@Override
	public String toString() {
		return getDimacsHeader();
	}

	/**
	 * Erzeugte eine neue Variable. Den zurückgegebenen Integer-Wert darf man nun in Klauseln (auch negiert)
	 * benutzen. Eine Variable hat niemals den Wert 0, da dieser Wert nicht negiert werden kann. Ebenso darf
	 * eine Variable nicht 0 sein, da im DIMACS CNF FORMAT das Symbol 0 zum Kodieren eines Zeilenendes benutzt wird.
	 *
	 * @return Die Nummer der neuen Variablen.
	 */
	public int createNewVar() {
		_nVars++;
		return _nVars;
	}

	/**
	 * Hinzufügen einer Klausel. Eine Klausel ist eine Menge von Variablen, die mit einem logischen ODER verknüpft
	 * sind. Die Variablen dürfen negiert sein. <br>
	 * <pre>
	 * Das Array [-3, 8, 2]
	 * wird als  (NOT x3) OR x8 OR x2 interpretiert.
	 * </pre>
	 * Die Menge aller Klauseln sind mit einem AND verknüpft.
	 *
	 * @param pVars Die Variablen (auch negiert) der Klausel mit den zuvor generierten Variablen. Ist das Array
	 *              leer, wird es ignoriert.
	 * @throws DeveloperNotificationException falls die angegebenen Variablen ungültig sind.
	 */
	public void addClause(final @NotNull int[] pVars) throws DeveloperNotificationException {

		for (final int v : pVars) {
			DeveloperNotificationException.ifTrue("Variable 0 ist nicht erlaubt!", v == 0);
			final int absV = Math.abs(v);
			DeveloperNotificationException.ifTrue("Variable " + absV + " wurde vorher nicht erzeugt!", absV > _nVars);
		}

		_clauses.add(pVars);
	}

	/**
	 * Liefert die interne Anzahl an erzeugten Variablen.
	 *
	 * @return Die interne Anzahl an erzeugten Variablen.
	 */
	public int getVarCount() {
		return _nVars;
	}

	/**
	 * Liefert die interne Anzahl an erzeugten Klauseln.
	 *
	 * @return Die interne Anzahl an erzeugten Klauseln.
	 */
	public int getClauseCount() {
		return _clauses.size();
	}

	private String getDimacsHeader() {
		return "p cnf " + _nVars + " " + _clauses.size();
	}
}
