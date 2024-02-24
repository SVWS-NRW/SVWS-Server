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
package de.svws_nrw.core.adt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.constraints.NotNull;

/**
 * Testet die Klasse {@link ForEach}.
 *
 * @author Benjamin A. Bartsch
 */
class TestForEachPair {

	/**
	 * Testet den 1. Fall.
	 */
	@Test
	@DisplayName("ArrayMap: Teste den Konstruktor für Enums.")
	void testFall1() {
		final @NotNull List<@NotNull String> list1 = new ArrayList<>();
		list1.add("A");
		list1.add("B");
		list1.add("C");

		final @NotNull Set<@NotNull Integer> set2 = new HashSet<>();
		set2.add(1);
		set2.add(2);
		set2.add(3);
		set2.add(4);

		int count = 0;
		boolean found = false;
		for (final @NotNull Pair<@NotNull String, @NotNull Integer> pair : ForEach.pair(list1, set2)) {
			count++;
			if (pair.a.equals("A") && (pair.b == 4))
				found = true;
		}
		assertEquals(12, count);
		assertEquals(true, found);
	}

}
