package de.svws_nrw.core.adt.sat;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import jakarta.validation.constraints.NotNull;

/**
 * Ein simpler SAT-Solver, der via Backtracking eine Lösung sucht.
 *
 * @author Benjamin A. Bartsch
 */
public final class SatSolverSimple1 extends SatSolver {

	/** Eine Kopie aller Klauseln. */
	private final @NotNull List<@NotNull Integer @NotNull []> _clauses = new ArrayList<>();

	/** Das Array, welches jeder Variablen (1-indiziert) seine Lösung zuordnet. */
	private @NotNull int[] _solution = new int[0];

	/**
	 * Ein simpler SAT-Solver, der via Backtracking eine Lösung sucht. <br>
	 * Das Backtracking ist ohne Rekursion implementiert.
	 */
	public SatSolverSimple1() {
		// Die Datenstrukturen werden erst später erzeugt,
		// da Preprozessoren ggf. SatInput manipulieren.
	}

	@Override
	public @NotNull SatOutput apply(final @NotNull SatInput t) {
		final long timeEnd = System.currentTimeMillis() + maxTimeMillis;

		final int nVars = t.getVarCount();
		DeveloperNotificationException.ifSmaller("nVars < 1", nVars, 1);

		_clauses.clear();
		_clauses.addAll(t.getClauses());
		_solution = new int[nVars + 1]; // Variablen sind 1-indiziert.


		int i = 1;
		while (System.currentTimeMillis() <= timeEnd) {

			// IF conflict THEN backtrack
			if (conflict()) {
				while (true) {
					// previous
					i--;
					if (i < 1) // unsolvable?
						return SatOutput.createUNSATISFIABLE();
					// stop backtrack
					if (_solution[i] != -i)
						break;
					// continue backtrack
					_solution[i] = 0;
				}
			}

			// solved?
			if (i >= _solution.length)
				return SatOutput.createSATISFIABLE(_solution);

			// forward
			if (_solution[i] == 0) {
				// first choice
				_solution[i] = i;
			} else {
				// second choice
				_solution[i] = -i;
			}

			// next
			i++;
		}

		// SATISFIABLE
		return SatOutput.createUNKNOWN();
	}

	private boolean conflict() {
		for (final @NotNull Integer[] clause : _clauses)
			if (isEmpty(clause))
				return true;
		return false;
	}

	private boolean isEmpty(final @NotNull Integer @NotNull [] clause) {
		for (final int literal : clause) {
			final int abs = Math.abs(literal);
			final int assignment = _solution[abs];
			if ((assignment == literal) || (assignment == 0))
				return false;
		}
		return true;
	}

}
