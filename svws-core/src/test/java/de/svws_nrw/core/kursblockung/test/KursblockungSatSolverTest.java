package de.svws_nrw.core.kursblockung.test;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.adt.collection.LinkedCollection;
import de.svws_nrw.core.kursblockung.satsolver.SatSolver3;
import de.svws_nrw.core.kursblockung.satsolver.SatSolverA;
import de.svws_nrw.core.kursblockung.satsolver.SatSolverWrapper;

/** Diese Klasse testet den SAT-Solver, indem verschiedene Formeln deren Lösbarkeit bekannt ist, an den SAT-Solver
 * geschickt werden. */
@DisplayName("SAT-Solver-Test.")
class KursblockungSatSolverTest {

	/** Erzeugt eine leere Formel und überprüft, ob die Formel lösbar bzw. {@link SatSolverA#RESULT_SATISFIABLE} ist. */
	@Test
	@DisplayName("testLeereFormelSAT")
	void testLeereFormelSAT() {
		final Random random = new Random(1);
		final SatSolverWrapper ssa = new SatSolverWrapper(new SatSolver3(random));

		// Leere Formel.

		// Lösen
		final int result = ssa.solve(60000);

		// Überprüfen
		if (result != SatSolverA.RESULT_SATISFIABLE) {
			fail("Solver antwortet nicht SATISFIABLE.");
		}

	}

	/** Erzeugt eine einfache Formel (x OR y OR z) und überprüft, ob die Formel lösbar bzw.
	 * {@link SatSolverA#RESULT_SATISFIABLE} ist. */
	@Test
	@DisplayName("testFreieVariablen")
	void testFreieVariablen() {
		final Random random = new Random(1);
		final SatSolverWrapper ssa = new SatSolverWrapper(new SatSolver3(random));

		// Formel.
		final int x = ssa.createNewVar();
		final int y = ssa.createNewVar();
		final int z = ssa.createNewVar();
		ssa.c_3(x, y, z);

		// Lösen
		final int result = ssa.solve(60000);

		// Überprüfen
		if (result != SatSolverA.RESULT_SATISFIABLE) {
			fail("Solver antwortet nicht SATISFIABLE.");
		}

	}

	/** Erzeugt eine einfache Formel (x) AND (x) und überprüft, ob die Formel lösbar bzw.
	 * {@link SatSolverA#RESULT_SATISFIABLE} ist. */
	@Test
	@DisplayName("testUnitSAT")
	void testUnitSAT() {
		final Random random = new Random(1);
		final SatSolverWrapper ssa = new SatSolverWrapper(new SatSolver3(random));

		// Formel erzeugen.
		final int x = ssa.createNewVar();
		ssa.c_1(x);
		ssa.c_1(x);

		// Lösen
		final int result = ssa.solve(60000);

		// Überprüfen
		if (result != SatSolverA.RESULT_SATISFIABLE) {
			fail("Solver antwortet nicht SATISFIABLE.");
		}

		if (!ssa.isVarTrue(x)) {
			fail("Solver sagt x ist nicht TRUE.");
		}

	}

	/** Erzeugt eine einfache Formel (x) AND (NOT x) und überprüft, ob die Formel unlösbar bzw.
	 * {@link SatSolverA#RESULT_UNSATISFIABLE} ist. */
	@Test
	@DisplayName("testUnitUNSAT")
	void testUnitUNSAT() {
		final Random random = new Random(1);
		final SatSolverWrapper ssa = new SatSolverWrapper(new SatSolver3(random));

		// Formel erzeugen.
		final int x = ssa.createNewVar();
		ssa.c_1(x);
		ssa.c_1(-x);

		// Lösen
		final int result = ssa.solve(60000);

		// Überprüfen
		if (result != SatSolverA.RESULT_UNSATISFIABLE) {
			fail("Solver antwortet nicht UNSATISFIABLE.");
		}

	}

	/** Erzeugt eine einfache Formel (x) AND (y) AND (NOT X OR NOT Y) und überprüft, ob die Formel unlösbar bzw.
	 * {@link SatSolverA#RESULT_UNSATISFIABLE} ist. */
	@Test
	@DisplayName("testNotBothUNSAT")
	void testNotBothUNSAT() {
		final Random random = new Random(1);
		final SatSolverWrapper ssa = new SatSolverWrapper(new SatSolver3(random));

		// Formel erzeugen.
		final int x = ssa.createNewVar();
		final int y = ssa.createNewVar();
		ssa.c_1(x);
		ssa.c_1(y);
		ssa.c_not_both(x, y);

		// Lösen
		final int result = ssa.solve(60000);

		// Überprüfen
		if (result != SatSolverA.RESULT_UNSATISFIABLE) {
			fail("Solver antwortet nicht UNSATISFIABLE.");
		}

	}

	/** Erzeugt für verschiedene Werte von n eine Formel mit 2n Variablen mit der Bedingung, dass genau n der 2n
	 * Variablen (also die Hälfte) TRUE sein muss und überprüft, ob die Formel lösbar bzw.
	 * {@link SatSolverA#RESULT_SATISFIABLE} ist. */
	@Test
	@DisplayName("testExactlyNoutOf2nSAT")
	void testExactlyNoutOf2nSAT() {
		final Random random = new Random(1);

		for (int n = 1; n <= 10; n++) {
			final SatSolverWrapper ssa = new SatSolverWrapper(new SatSolver3(random));

			// Formel erzeugen.
			final int[] vars = ssa.createNewVars(2 * n);
			ssa.c_exactly_GENERIC(vars, n);

			// Lösen
			final int result = ssa.solve(60000);

			// Überprüfen
			if (result != SatSolverA.RESULT_SATISFIABLE) {
				fail("Solver antwortet nicht SATISFIABLE.");
			}

			int count = 0;
			for (final int x : vars) {
				if (ssa.isVarTrue(x)) {
					count++;
				}
			}

			if (count != n) {
				fail("Summe ist " + count + " statt " + n + ".");
			}
		}

	}

	/** Erzeugt für verschiedene Werte von n eine Formel mit n² Variablen (quadratische Matrix) mit der Bedingung, dass
	 * in jeder Zeile und jeder Spalte der Matrix genau eine Variable TRUE sein muss und überprüft, ob die Formel lösbar
	 * bzw. {@link SatSolverA#RESULT_SATISFIABLE} ist. */
	@Test
	@DisplayName("testSquareExactlyOneRowAndColumnSAT")
	void testSquareExactlyOneRowAndColumnSAT() {
		final Random random = new Random(1);

		for (int n = 1; n <= 10; n++) {
			final SatSolverWrapper ssa = new SatSolverWrapper(new SatSolver3(random));

			// Formel erzeugen.
			final int[][] data = new int[n][n];
			for (int r = 0; r < n; r++)
				for (int c = 0; c < n; c++)
					data[r][c] = ssa.createNewVar();

			// Jede Zeile genau eine Variable TRUE.
			for (int r = 0; r < n; r++) {
				final LinkedCollection<Integer> list = new LinkedCollection<>();
				for (int c = 0; c < n; c++)
					list.addLast(data[r][c]);
				ssa.c_exactly_GENERIC(list, 1);
			}

			// Jede Spalte genau eine Variable TRUE.
			for (int c = 0; c < n; c++) {
				final LinkedCollection<Integer> list = new LinkedCollection<>();
				for (int r = 0; r < n; r++)
					list.addLast(data[r][c]);
				ssa.c_exactly_GENERIC(list, 1);
			}

			// Lösen
			final int result = ssa.solve(60000);

			// Überprüfen
			if (result != SatSolverA.RESULT_SATISFIABLE)
				fail("Solver antwortet nicht SATISFIABLE, bei n = " + n + ", sondern " + result);

			// Überprüfe Zeilen-Summen.
			for (int r = 0; r < n; r++) {
				int summe = 0;
				for (int c = 0; c < n; c++)
					if (ssa.isVarTrue(data[r][c]))
						summe++;

				if (summe != 1)
					fail("Zeile[" + r + "] --> Summe = " + summe + ", beim Quadrat mit n = " + n + ".");
			}

			// Überprüfe Spalten-Summen.
			for (int c = 0; c < n; c++) {
				int summe = 0;
				for (int r = 0; r < n; r++)
					if (ssa.isVarTrue(data[r][c]))
						summe++;

				if (summe != 1)
					fail("Spalte[" + c + "] --> Summe = " + summe + ", beim Quadrat mit n = " + n + ".");
			}

		}

	}

	/** Erzeugt für verschiedene Werte von n eine Formel mit n*(n+1) Variablen (Matrix der Dimension n mal n+1) mit der
	 * Bedingung, dass in jeder Zeile und jeder Spalte der Matrix genau eine Variable TRUE sein muss und überprüft, ob
	 * die Formel unlösbar bzw. {@link SatSolverA#RESULT_UNSATISFIABLE} ist. */
	@Test
	@DisplayName("testRectangleExactlyOneRowAndColumnUNSAT")
	void testRectangleExactlyOneRowAndColumnUNSAT() {
		final Random random = new Random(1);

		for (int n = 1; n <= 6; n++) {
			final SatSolverWrapper ssa = new SatSolverWrapper(new SatSolver3(random));

			// Formel erzeugen.
			final int[][] data = new int[n][n + 1];
			for (int r = 0; r < n; r++) {
				for (int c = 0; c < n + 1; c++) {
					data[r][c] = ssa.createNewVar();
				}
			}

			// Jede Zeile genau eine Variable TRUE.
			for (int r = 0; r < n; r++) {
				final LinkedCollection<Integer> list = new LinkedCollection<>();
				for (int c = 0; c < n + 1; c++) {
					list.addLast(data[r][c]);
				}
				ssa.c_exactly_GENERIC(list, 1);
			}

			// Jede Spalte genau eine Variable TRUE.
			for (int c = 0; c < n + 1; c++) {
				final LinkedCollection<Integer> list = new LinkedCollection<>();
				for (int r = 0; r < n; r++) {
					list.addLast(data[r][c]);
				}
				ssa.c_exactly_GENERIC(list, 1);
			}

			// Lösen
			final int result = ssa.solve(60000);

			// Überprüfen
			if (result != SatSolverA.RESULT_UNSATISFIABLE) {
				fail("Solver antwortet nicht UNSATISFIABLE, bei n = " + n);
			}

		}

	}

}
