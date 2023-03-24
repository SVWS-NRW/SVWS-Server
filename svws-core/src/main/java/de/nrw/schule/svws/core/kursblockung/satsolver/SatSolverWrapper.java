package de.nrw.schule.svws.core.kursblockung.satsolver;

import java.util.Iterator;

import de.nrw.schule.svws.core.adt.collection.LinkedCollection;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse definiert alle Methoden für einen SAT-Solver.
 * 
 * @author Benjamin A. Bartsch
 */
public class SatSolverWrapper extends SatSolverA {

	private final @NotNull SatSolverA _solver;

	private final int varFALSE;
	private final int varTRUE;

	/**
	 * Erstellt eine Ebene über dem {@link SatSolverA}, um verschiedene Bedingungen/Constraints als Klauseln zu
	 * codieren.
	 * 
	 * @param solver Der Solver, der intern verwendet wird.
	 */
	public SatSolverWrapper(final @NotNull SatSolverA solver) {
		_solver = solver;
		varTRUE = _solver.createNewVar();
		varFALSE = -varTRUE;
		c_1(varTRUE);
	}

	// ##########################################################
	// #### Die folgenden Methoden werden direkt delegiert. #####
	// ##########################################################

	@Override
	public int createNewVar() {
		return _solver.createNewVar();
	}

	@Override
	public void addClause(final @NotNull int[] pVars) {
		_solver.addClause(pVars);
	}

	@Override
	public boolean isVarTrue(final int pVar) {
		return _solver.isVarTrue(pVar);
	}

	@Override
	public int solve(final long maxTimeMillis) {
		return _solver.solve(maxTimeMillis);
	}

	@Override
	public int getVarCount() {
		return _solver.getVarCount();
	}

	@Override
	public int getClauseCount() {
		return _solver.getClauseCount();
	}

	// ##################################################################
	// ### Die folgenden Methoden ergänzen den Solver um Constraints. ###
	// ##################################################################

	/**
	 * Liefert ein Array der Länge n mit neu erzeugten Variablennummern.
	 * 
	 * @param n Die Länge des Arrays.
	 * 
	 * @return Ein Array der Länge n mit neu erzeugten Variablennummern.
	 */
	public @NotNull int[] createNewVars(final int n) {
		final int[] temp = new int[n];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = _solver.createNewVar();
		}
		return temp;
	}

	/**
	 * Liefert eine Variable, die zuvor auf FALSE forciert wurde.
	 * 
	 * @return Eine Variable, die zuvor auf FALSE forciert wurde.
	 */
	public int getVarFALSE() {
		return varFALSE;
	}

	/**
	 * Liefert eine Variable, die zuvor auf TRUE forciert wurde.
	 * 
	 * @return Eine Variable, die zuvor auf TRUE forciert wurde.
	 */
	public int getVarTRUE() {
		return varTRUE;
	}

	/**
	 * Fügt eine Klausel der Größe 1 hinzu. Forciert damit die übergebene Variable auf TRUE.
	 * 
	 * @param x Die Variable wird auf TRUE gesetzt.
	 */
	public void c_1(final int x) {
		_solver.addClause(new int[] { x });
	}

	/**
	 * Fügt eine Klausel der Größe 2 hinzu. Forciert damit, dass mindestens eine der beiden Variablen TRUE ist,
	 * letzlich @code{x + y >= 1}.
	 * 
	 * @param x Die Variable x der Klausel (x OR y).
	 * @param y Die Variable y der Klausel (x OR y).
	 */
	public void c_2(final int x, final int y) {
		_solver.addClause(new int[] { x, y });
	}

	/**
	 * Fügt eine Klausel der Größe 3 hinzu. Forciert damit, dass mindestens eine der drei Variablen TRUE ist,
	 * letzlich @code{x + y + z >= 1}.
	 * 
	 * @param x Die Variable x der Klausel (x OR y OR z).
	 * @param y Die Variable y der Klausel (x OR y OR z).
	 * @param z Die Variable z der Klausel (x OR y OR z).
	 */
	public void c_3(final int x, final int y, final int z) {
		_solver.addClause(new int[] { x, y, z });
	}

	/**
	 * Forciert, dass nicht beide Variablen TRUE sind, letzlich @code{x + y ≤ 1}.
	 * 
	 * @param x Die Variable x der Klausel (-x OR -y).
	 * @param y Die Variable y der Klausel (-x OR -y).
	 */
	public void c_not_both(final int x, final int y) {
		c_2(-x, -y);
	}

	/**
	 * Liefert die Variable z für die {@code z = x AND y} gilt.
	 * 
	 * @param x Variable der obigen Gleichung.
	 * @param y Variable der obigen Gleichung.
	 * 
	 * @return Die Variable z für die {@code z = x AND y} gilt.
	 */
	public int c_new_var_AND(final int x, final int y) {
		final int c = _solver.createNewVar();
		c_2(x, -c);
		c_2(y, -c);
		c_3(-x, -y, c);
		return c;
	}

	/**
	 * Forciert, dass genau {@code amount} Variablen des Arrays den Wert TRUE haben.
	 * 
	 * @param pArray Das Variablenarray.
	 * @param amount Die Anzahl an TRUEs in der Variablenliste.
	 */
	public void c_exactly_GENERIC(final @NotNull int[] pArray, final int amount) {
		c_exactly_GENERIC(toList(pArray), amount);
	}

	/**
	 * Forciert, dass genau {@code amount} Variablen der Variablenliste den Wert TRUE haben.
	 * 
	 * @param pList   Die Variablenliste.
	 * @param pAmount Die Anzahl an TRUEs in der Variablenliste.
	 */
	public void c_exactly_GENERIC(final @NotNull LinkedCollection<@NotNull Integer> pList, final int pAmount) {
		final @NotNull LinkedCollection<@NotNull Integer> list = new LinkedCollection<>(pList);
		// Error?
		if (pAmount > list.size()) {
			System.out.println("FEHLER: c_exactly_GENERIC --> amount > list.size()");
		}

		// special case: 0
		if (pAmount == 0) {
			for (final int x : list) {
				c_1(-x);
			}
			return;
		}
		// special case: N
		if (pAmount == list.size()) {
			for (final int x : list) {
				c_1(+x);
			}
			return;
		}
		// special case: 1
		if (pAmount == 1) {
			if (list.size() == 1) {
				c_1(list.getFirst());
				return;
			}
			if (list.size() == 2) {
				c_unequal(list.getFirst(), list.getLast());
				return;
			}
			c_exactly_one(list);
			return;
		}
		// else choose...
		c_exactly_NETWORK(list, pAmount);
	}

	/**
	 * Forciert, dass höchstens {@code maximum} Variablen der Variablenliste den Wert TRUE haben.
	 * 
	 * @param pList    Die Variablenliste.
	 * @param pMaximum Die maximale Anzahl an TRUEs in der Variablenliste.
	 */
	public void c_at_most_GENERIC(final @NotNull LinkedCollection<@NotNull Integer> pList, final int pMaximum) {
		final @NotNull LinkedCollection<@NotNull Integer> list = new LinkedCollection<>(pList);
		// special case: trivial
		if (pMaximum >= list.size()) {
			return;
		}
		// special case: all false
		if (pMaximum == 0) {
			for (final int x : list) {
				c_1(-x);
			}
			return;
		}
		// special case: at_most_one
		if (pMaximum == 1) {
			c_at_most_one_tree(list);
			return;
		}
		// else choose...
		c_at_most_NETWORK(list, pMaximum);
	}

	// ########################################
	// ############## PRIVATE ###############
	// ########################################

	/**
	 * Forciert, dass genau eine Variable der Liste TRUE ist. Falls die Liste leer ist, führt das zur direkten
	 * Unlösbarkeit der Formel.
	 * 
	 * @param list Genau eine der Variablen der Liste muss TRUE sein.
	 */
	private void c_exactly_one(final @NotNull LinkedCollection<@NotNull Integer> list) {
		c_1(c_at_most_one_tree(list));
	}

	/**
	 * Forciert, dass {@code z = x OR y} gilt.
	 * 
	 * @param x Variable der obigen Gleichung.
	 * @param y Variable der obigen Gleichung.
	 * @param z Variable der obigen Gleichung.
	 */
	private void c_z_equals_x_or_y(final int x, final int y, final int z) {
		c_2(-x, z);
		c_2(-y, z);
		c_3(x, y, -z);
	}

	/**
	 * Liefert die Variable z für die {@code z = x OR y} gilt.
	 * 
	 * @param x Variable der obigen Gleichung.
	 * @param y Variable der obigen Gleichung.
	 * 
	 * @return Die Variable z für die {@code z = x OR y} gilt.
	 */
	private int c_new_var_OR(final int x, final int y) {
		final int z = _solver.createNewVar();
		c_2(-x, z);
		c_2(-y, z);
		c_3(x, y, -z);
		return z;
	}

	/**
	 * Forciert, dass in der Liste maximal eine Variable TRUE ist. Die Ergebnisvariable ist eine OR-Verknüpfung aller
	 * Variablen der Liste.
	 * 
	 * @param pList Forciert, dass maximal eine Variable der Liste TRUE ist.
	 * 
	 * @return Die Ergebnisvariable ist eine OR-Verknüpfung aller Variablen der Liste.
	 */
	private int c_at_most_one_tree(final @NotNull LinkedCollection<@NotNull Integer> pList) {
		// Liste kopieren
		final @NotNull LinkedCollection<@NotNull Integer> list = new LinkedCollection<>(pList);

		// Sonderfall --> Erzeuge FALSE
		if (list.isEmpty()) {
			list.add(varFALSE);
		}

		// Solange es zwei Variablen gibt
		while (list.size() >= 2) {
			final int a = list.removeFirst();
			final int b = list.removeFirst();
			final int c = _solver.createNewVar();
			c_not_both(a, b);
			c_z_equals_x_or_y(a, b, c);
			list.add(c);
		}

		// Wurzel
		return list.removeFirst();
	}

	private void c_exactly_NETWORK(final @NotNull LinkedCollection<@NotNull Integer> list, final int amount) {
		// sort
		c_bitonic_sort(list);
		// force TRUE / FALSE
		int i = 0;
		final @NotNull
		Iterator<@NotNull Integer> iter = list.iterator();
		while (iter.hasNext()) {
			final @NotNull
			Integer value = iter.next();
			if (i < amount) {
				c_1(+value);
			} else {
				c_1(-value);
			}
			i++;
		}
	}

	private void c_at_most_NETWORK(final @NotNull LinkedCollection<@NotNull Integer> list, final int maximum) {
		// sort
		c_bitonic_sort(list);
		// force FALSE
		int i = 0;
		final @NotNull
		Iterator<@NotNull Integer> iter = list.iterator();
		while (iter.hasNext()) {
			final @NotNull
			Integer value = iter.next();
			if (i < maximum)
				i++;
			else
				c_1(-value);
		}
	}

	private void c_bitonic_sort(final @NotNull LinkedCollection<@NotNull Integer> list) {
		c_fill_False_until_power_two(list);
		c_bitonic_sort_power_two(list);
	}

	private void c_fill_False_until_power_two(final @NotNull LinkedCollection<@NotNull Integer> list) {
		int size = 1;
		while (size < list.size()) {
			size *= 2;
		}
		while (list.size() < size) {
			list.addLast(varFALSE);
		}
	}

	private void c_bitonic_sort_power_two(final @NotNull LinkedCollection<@NotNull Integer> list) {
		for (int window = 2; window <= list.size(); window *= 2) {
			c_bitonic_sort_spiral(list, window);
			for (int difference = window / 2; difference >= 2; difference /= 2) {
				c_bitonic_sort_difference(list, difference);
			}
		}
	}

	private void c_bitonic_sort_spiral(final @NotNull LinkedCollection<@NotNull Integer> list, final int size) {
		for (int i = 0; i < list.size(); i += size) {
			for (int i1 = i, i2 = i + size - 1; i1 < i2; i1++, i2--) {
				c_bitonic_comparator(list, i1, i2);
			}
		}
	}

	private void c_bitonic_sort_difference(final @NotNull LinkedCollection<@NotNull Integer> list, final int size) {
		final int half = size / 2;
		for (int i = 0; i < list.size(); i += size) {
			for (int j = 0; j < half; j++) {
				c_bitonic_comparator(list, i + j, i + j + half);
			}
		}
	}

	private void c_bitonic_comparator(final @NotNull LinkedCollection<@NotNull Integer> result, final int i1, final int i2) {
		if (i1 >= i2) {
			System.out.println("c_bitonic_comparator: " + i1 + "," + i2 + " <-- ERROR!!!");
		}
		final int a = result.get(i1);
		final int b = result.get(i2);
		result.set(i1, c_new_var_OR(a, b));
		result.set(i2, c_new_var_AND(a, b));
	}

	private static @NotNull LinkedCollection<@NotNull Integer> toList(final @NotNull int[] pArray) {
		final @NotNull
		LinkedCollection<@NotNull Integer> list = new LinkedCollection<>();
		for (final int x : pArray) {
			list.addLast(x);
		}
		return list;
	}

	private void c_unequal(final int x, final int y) {
		c_equal(x, -y);
	}

	private void c_equal(final int x, final int y) {
		c_2(-x, +y);
		c_2(+x, -y);
	}

}
