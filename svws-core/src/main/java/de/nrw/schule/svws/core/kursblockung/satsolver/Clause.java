package de.nrw.schule.svws.core.kursblockung.satsolver;

import java.util.Iterator;

import de.nrw.schule.svws.core.adt.set.AVLSet;
import jakarta.validation.constraints.NotNull;

/**
 * Eine Klausel.
 * 
 * @author Benjamin A. Bartsch
 */
public class Clause implements Comparable<@NotNull Clause> {

	/**
	 * Alle Variablen, die zu dieser Klausel gehören.
	 */
	final @NotNull Variable@NotNull[] variables;

	/**
	 * Die Anzahl an noch nicht definierten Literalen.
	 */
	int free;

	/**
	 * Die Anzahl an erfüllten Literalen.
	 */
	int sat;

	/**
	 * Konstruktor für eine 1-CNF-Klausel.
	 * 
	 * @param pX Die 1. Variable in dieser Klausel.
	 */
	Clause(@NotNull Variable pX) {
		variables = new Variable[] { pX };
		free = 1;
		sat = 0;
	}

	/**
	 * Konstruktor für eine 2-CNF-Klausel.
	 * 
	 * @param pX Die 1. Variable in dieser Klausel.
	 * @param pY Die 2. Variable in dieser Klausel.
	 */
	Clause(@NotNull Variable pX, @NotNull Variable pY) {
		variables = new Variable[] { pX, pY };
		free = 2;
		sat = 0;
	}

	/**
	 * Konstruktor für eine 3-CNF-Klausel.
	 * 
	 * @param pX Die 1. Variable in dieser Klausel.
	 * @param pY Die 2. Variable in dieser Klausel.
	 * @param pZ Die 3. Variable in dieser Klausel.
	 */
	Clause(@NotNull Variable pX, @NotNull Variable pY, @NotNull Variable pZ) {
		variables = new Variable[] { pX, pY, pZ };
		free = 3;
		sat = 0;
	}

	@Override
	public @NotNull String toString() {
		@NotNull String s = "";
		for (@NotNull Variable v : variables) {
			if (v.index == -1) {
				return "[SAT]";
			}
			if (v.index >= 0) {
				s = s + " " + v.nr;

			}
		}

		return "[" + s + "]";
	}

	private @NotNull AVLSet<@NotNull Integer> getSet() {
		@NotNull AVLSet<@NotNull Integer> set = new AVLSet<>();
		for (@NotNull Variable v : variables) {
			if (v.index >= 0) {
				set.add(v.nr);
			}
		}
		return set;
	}

	@Override
	public int compareTo(@NotNull Clause o) {
		@NotNull AVLSet<@NotNull Integer> set1 = getSet();
		@NotNull AVLSet<@NotNull Integer> set2 = o.getSet();

		if (set1.size() < set2.size())
			return -1;
		if (set1.size() > set2.size())
			return +1;

		Iterator<@NotNull Integer> i1 = set1.iterator();
		Iterator<@NotNull Integer> i2 = set2.iterator();
		
		if ((i1 == null) || (i2 == null))
			throw new NullPointerException();

		while (i1.hasNext()) {
			int cmp = Integer.compare(i1.next(), i2.next());
			if (cmp != 0)
				return cmp;
		}

		return 0;
	}

}
