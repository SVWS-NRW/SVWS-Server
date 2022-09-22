package de.nrw.schule.svws.core.adt;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse definiert ein einfaches Paar von Werten der 
 * spezifizierten Typen. Der zweite Wert kann auch null werden. 
 * 
 * @param <A> Der erste Typ bei dem Paar.
 * @param <B> Der zweite Typ bei dem Paar.
 */
public class Pair<@NotNull A, @NotNull B> {

	/** Der erster Wert des Paares */
	public @NotNull A a;

	/** Der zweite Wert des Paares */
	public B b;

	/**
	 * Erstellt ein neues Paar.
	 * 
	 * @param a   der erste Wert des Paares
	 * @param b   der zweite Wert des Paares
	 */
	public Pair(@NotNull A a, B b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public @NotNull String toString() {
		return "[" + a + ", " + b + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o instanceof Pair<?, ?> == false)
			return false;
		Pair<?, ?> e = (Pair<?, ?>) o;
		boolean a_equals = a.equals(e.a);
		boolean b_equals = (b == null) ? (e.b == null) : b.equals(e.b);
		return a_equals && b_equals;
	}

	@Override
	public int hashCode() {
		return a.hashCode() ^ ((b == null) ? 0 : b.hashCode());
	}

}
