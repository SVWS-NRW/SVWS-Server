package de.svws_nrw.core.adt.sat;

import java.util.Random;
import java.util.function.Function;

import jakarta.validation.constraints.NotNull;

/**
 * Dieser Wrapper permutiert die Nummern der Variablen vor dem Lösen,
 * anschließend wird die Lösung zurückpermutiert.
 *
 * @author Benjamin A. Bartsch
 */
public final class SatWrapper1 implements Function<@NotNull SatInput, @NotNull SatOutput> {

	private final @NotNull Function<@NotNull SatInput, @NotNull SatOutput> next;
	private final @NotNull Random rnd = new Random();

	/**
	 * Konstruktor.
	 *
	 * @param next Der nächste Preprocessor oder SatSolver
	 */
	public SatWrapper1(@NotNull final Function<@NotNull SatInput, @NotNull SatOutput> next) {
		super();
		this.next = next;
	}

	@Override
	public @NotNull SatOutput apply(@NotNull final SatInput in1) {
		// create "map1to2"
		final int nVars = in1.getVarCount();
		final @NotNull int[] map1to2 = new int[nVars + 1];
		for (int i = 1; i <= nVars; i++)
			map1to2[i] = i;
		for (int i1 = 1; i1 <= nVars; i1++) {
			final int i2 = rnd.nextInt(nVars) + 1;
			final int save1 = map1to2[i1];
			final int save2 = map1to2[i2];
			map1to2[i1] = save2;
			map1to2[i2] = save1;
		}

		// use "map1to2" to map from "in1" to "in2"
		final @NotNull SatInput in2 = new SatInput();
		for (final @NotNull Integer @NotNull[] clause : in1.getClauses()) {
			// map clause-indices
			@NotNull final Integer @NotNull[] clause2 = new Integer[clause.length];
			for (int i = 0; i < clause.length; i++) {
				final int lit = clause[i];
				clause2[i] = (lit >= 0) ? map1to2[lit] : -map1to2[-lit];
			}
			// add to input
			in2.add_clause_and_variables(clause2);
		}

		// calculate "out2" by using next solver
		final @NotNull SatOutput out2 = next.apply(in2);

		// recover solution1
		final @NotNull int[] solution2 = out2.getSolution();

		// Vorsicht: Das Array kann Größe 0 haben, also NICHT "nVars" benutzen!
		final @NotNull int[] solution1 = new int[solution2.length];
		for (int lit1 = 1; lit1 < solution2.length; lit1++) {
			final int lit2 = map1to2[lit1];
			solution1[lit1] = solution2[lit2] >= 0 ? lit1 : -lit1;
		}

		return SatOutput.createCopy(out2, solution1);
	}

}
