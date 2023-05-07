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
public final class SatSolverSimple extends SatSolver {

	private @NotNull boolean[] _backtrackingState = new boolean[0];
	private @NotNull int[] _backtrackingLiteral = new int[0];
	private @NotNull int[] _solution = new int[0];
	private int _backtrackingIndex = 0;
	private boolean _backtrackingForward = true;
	private final @NotNull List<@NotNull Integer @NotNull[]> _clauses = new ArrayList<>();


	/**
	 * Ein simpler SAT-Solver, der via Backtracking eine Lösung sucht. <br>
	 * Das Backtracking ist ohne Rekursion implementiert.
	 */
	public SatSolverSimple() {
		// Die Datenstrukturen werden erst später erzeugt, da
		// Pre-Prozessoren ggf. den SatInput manipulieren.
	}

	@Override
	public @NotNull SatOutput apply(final @NotNull SatInput t) {
		final long timeEnd = System.currentTimeMillis() +  maxTimeMillis;

		initialize(t);

		// Solange nicht alle Variablen definiert wurden ...
		while (_backtrackingIndex < _backtrackingLiteral.length) {

			// Fall 1: Zeit abgelaufen? -->  UNKNOWN
			if (System.currentTimeMillis() > timeEnd)
				return new SatOutput();

			if (_backtrackingForward) {
				if (isContradiction()) {
					// Fall 2a: forward + contradiction --> backtrack
					_backtrackingForward = false;
					_backtrackingIndex--;
				} else {
					// Fall 2a: forward + no contradiction --> choose literal to propagate
					final int lit = choose_literal();
					propagate(lit);
					_backtrackingState[_backtrackingIndex] = true; // odd
					_backtrackingForward = true;
					_backtrackingIndex++;
				}
			} else {
				if (_backtrackingState[_backtrackingIndex]) {
					// Fall 3a: backward + odd --> try negated literal
					final int lit = _backtrackingLiteral[_backtrackingIndex];
					propagate_undo(lit);
					propagate(-lit);
					_backtrackingState[_backtrackingIndex] = false; // even
					_backtrackingForward = true;
				} else {
					// Fall 3b: backward + even --> backtrack
					_backtrackingForward = false;
					final int lit = _backtrackingLiteral[_backtrackingIndex];
					_backtrackingState[_backtrackingIndex] = false; // even
					propagate_undo(lit);
				}
			}


		}

		// SATISFIABLE
		return new SatOutput(_solution);
	}

	private void propagate(final int lit) {
		if (lit >= 0) {
			_solution[lit] = lit;
		} else {
			_solution[-lit] = lit;
		}
	}

	private void propagate_undo(final int lit) {
		if (lit >= 0) {
			_solution[lit] = 0;
		} else {
			_solution[-lit] = 0;
		}
	}

	private boolean isContradiction() {
		for (final @NotNull Integer[] clause : _clauses) {
			if (isEmpty(clause))
				return true;
		}
		return false;
	}

	private boolean isEmpty(final@NotNull Integer @NotNull[] clause) {
		for (final int literal : clause) {
			final int abs = Math.abs(literal);
			final int assignment = Math.abs(_solution[abs]);
			if (assignment == literal)
				return false;
		}
		return true;
	}

	private int choose_literal() {
		// Wähle die erste undefinierte Variable
		for (int literal = 1; literal < _solution.length; literal++)
			if (literal == 0)
				return literal;
		// Der Fall darf nicht passieren.
		throw new DeveloperNotificationException("Solver kann keine undefinierte Variable mehr finden!");
	}

	private void initialize(final @NotNull SatInput t) {
		final int nVars = t.getVarCount();
		_backtrackingState = new boolean[nVars];
		_backtrackingLiteral = new int[nVars];
		_solution = new int[nVars + 1]; // Variables are numbered from 1!
		_backtrackingIndex = 0;
		_backtrackingForward = true;

		_clauses.clear();
		_clauses.addAll(t.getClauses());
	}


}
