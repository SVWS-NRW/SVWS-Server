package de.svws_nrw.core.adt.sat;

import java.util.function.Function;

import jakarta.validation.constraints.NotNull;

/**
 * Ein simpler SAT-Solver, der via Backtracking eine Lösung sucht.
 *
 * @author Benjamin A. Bartsch
 */
public abstract class SatSolver implements Function<@NotNull SatInput, @NotNull SatOutput> {


	/** Die maximale Zeit, die der Solver zum Lösen verwenden darf. */
	protected long maxTimeMillis = 1000L; // Default 1 Sekunde.

	/**
	 * Setzt die maximale Zeit, die der Solver zum Lösen verwenden darf.
	 *
	 * @param pMaxTimeMillis die maximale Zeit, die der Solver zum Lösen verwenden darf.
	 */
	public void setMaxTimeMillis(final long pMaxTimeMillis) {
		maxTimeMillis = pMaxTimeMillis;
	}

	@Override
	public abstract @NotNull SatOutput apply(@NotNull SatInput t);

}
