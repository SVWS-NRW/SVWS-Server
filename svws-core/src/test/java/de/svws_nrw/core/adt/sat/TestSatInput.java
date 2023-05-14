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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Testet die folgenden Klassen: <br>
 * {@link SatInput}
 *
 * @author Benjamin A. Bartsch
 */
class TestSatInput {

	/**
	 * Test: {@link SatInput#add_clause_1(int)}
	 */
	@Test
	void testAddClause1() {
		final SatInput in = new SatInput();
		final int x1 = in.create_var();
		in.add_clause_1(x1);
		assertEquals(true, in.isValidSolution(new int[] {0, x1}));
		assertEquals(false, in.isValidSolution(new int[] {0, -x1}));
	}

	/**
	 * Test: {@link SatInput#add_clause_2(int, int)}
	 */
	@Test
	void testAddClause2() {
		final SatInput in = new SatInput();
		final int x1 = in.create_var();
		final int x2 = in.create_var();
		in.add_clause_2(x1, x2);
		assertEquals(true, in.isValidSolution(new int[] {0, x1, x2}));
		assertEquals(true, in.isValidSolution(new int[] {0, -x1, x2}));
		assertEquals(true, in.isValidSolution(new int[] {0, x1, -x2}));
		assertEquals(false, in.isValidSolution(new int[] {0, -x1, -x2}));
	}

	/**
	 * Test: {@link SatInput#add_clause_3(int, int, int)}
	 */
	@Test
	void testAddClause3() {
		final SatInput in = new SatInput();
		final int x1 = in.create_var();
		final int x2 = in.create_var();
		final int x3 = in.create_var();
		in.add_clause_3(x1, x2, x3);
		assertEquals(true, in.isValidSolution(new int[] {0, x1, x2, x3}));
		assertEquals(true, in.isValidSolution(new int[] {0, x1, x2, -x3}));
		assertEquals(true, in.isValidSolution(new int[] {0, x1, -x2, x3}));
		assertEquals(true, in.isValidSolution(new int[] {0, x1, -x2, -x3}));
		assertEquals(true, in.isValidSolution(new int[] {0, -x1, x2, x3}));
		assertEquals(true, in.isValidSolution(new int[] {0, -x1, x2, -x3}));
		assertEquals(true, in.isValidSolution(new int[] {0, -x1, -x2, x3}));
		assertEquals(false, in.isValidSolution(new int[] {0, -x1, -x2, -x3}));
	}

	/**
	 * Test: {@link SatInput#add_clause_not_both(int, int)}
	 */
	@Test
	void testAddClauseNotBoth() {
		final SatInput in = new SatInput();
		final int x1 = in.create_var();
		final int x2 = in.create_var();
		in.add_clause_not_both(x1, x2);
		assertEquals(false, in.isValidSolution(new int[] {0, x1, x2}));
		assertEquals(true, in.isValidSolution(new int[] {0, -x1, x2}));
		assertEquals(true, in.isValidSolution(new int[] {0, x1, -x2}));
		assertEquals(true, in.isValidSolution(new int[] {0, -x1, -x2}));
	}

	/**
	 * Test: {@link SatInput#add_clause_equal(int, int)}
	 */
	@Test
	void testAddClauseEqual() {
		final SatInput in = new SatInput();
		final int x1 = in.create_var();
		final int x2 = in.create_var();
		in.add_clause_equal(x1, x2);
		assertEquals(true, in.isValidSolution(new int[] {0, x1, x2}));
		assertEquals(false, in.isValidSolution(new int[] {0, -x1, x2}));
		assertEquals(false, in.isValidSolution(new int[] {0, x1, -x2}));
		assertEquals(true, in.isValidSolution(new int[] {0, -x1, -x2}));
	}

	/**
	 * Test: {@link SatInput#add_clause_unequal(int, int)}
	 */
	@Test
	void testAddClauseUnequal() {
		final SatInput in = new SatInput();
		final int x1 = in.create_var();
		final int x2 = in.create_var();
		in.add_clause_unequal(x1, x2);
		assertEquals(false, in.isValidSolution(new int[] {0, x1, x2}));
		assertEquals(true, in.isValidSolution(new int[] {0, -x1, x2}));
		assertEquals(true, in.isValidSolution(new int[] {0, x1, -x2}));
		assertEquals(false, in.isValidSolution(new int[] {0, -x1, -x2}));
	}

}
