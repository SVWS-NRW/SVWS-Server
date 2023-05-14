/*
 * Copyright 2022 Marina Bachran
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.svws_nrw.core.adt.sat;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Function;

import org.junit.jupiter.api.Test;

import jakarta.validation.constraints.NotNull;

/**
 * Testet die folgenden Klassen: <br>
 * {@link SatInput} <br>
 * {@link SatOutput} <br>
 * {@link SatWrapper1} <br>
 * {@link SatSolverSimple1} <br>
 *
 * @author Benjamin A. Bartsch
 */
class TestSatSolver {

	/**
	 * Erzeugt eine NxN Matrix bei der für jede Zeile und für jede Spalte
	 * die Bedingung 'exactly one' gilt. Diese Formel ist lösbar.
	 */
	@Test
	void testCaseSAT() {

		final SatSolver solver1 =  new SatSolverSimple1();
		solver1.setMaxTimeMillis(20000);

		final Function<@NotNull SatInput, @NotNull SatOutput> solver = new SatWrapper1(solver1);

		for (int n = 1; n <= 4; n++) { // Vorsicht: N <= 4, sonst timeout!
			// create "in"
			final SatInput in = new SatInput();
			final int[][] matrix = in.create_vars2D(n, n);
			for (int i = 0; i < n; i++)
				in.add_clause_exactly_in_column(matrix, i, 1);
			for (int i = 0; i < n; i++)
				in.add_clause_exactly_in_row(matrix, i, 1);

			// create "out"
			final SatOutput out =  solver.apply(in);
			assertTrue(out.isSatisfiable());
			assertFalse(out.isUnknown());
			assertFalse(out.isUnsatisfiable());
			assertTrue(in.isValidSolution(out.getSolution()));
		}

	}

}
