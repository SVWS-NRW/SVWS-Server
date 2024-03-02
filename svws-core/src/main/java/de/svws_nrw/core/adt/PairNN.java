package de.svws_nrw.core.adt;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse definiert ein einfaches Paar von Werten der spezifizierten Typen.
 * Beide Werte dürfen nicht NULL sein.
 *
 * @param <A> Der 1. Typ des Paares.
 * @param <B> Der 2. Typ des Paares.
 */
public final class PairNN<@NotNull A, @NotNull B> {

	/** Der 1. Wert des Paares. */
	public @NotNull A a;

	/** Der 2. Wert des Paares. */
	public @NotNull B b;

	/**
	 * Erstellt ein neues Paar.
	 *
	 * @param a   der erste Wert des Paares
	 * @param b   der zweite Wert des Paares
	 */
	public PairNN(final @NotNull A a, final @NotNull B b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public @NotNull String toString() {
		return "[" + a + ", " + b + "]";
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null)
			return false;
		if (!(o instanceof PairNN<?, ?>))
			return false;
		final PairNN<?, ?> e = (PairNN<?, ?>) o;
		final boolean a_equals = a.equals(e.a);
		final boolean b_equals = b.equals(e.b);
		return a_equals && b_equals;
	}

	@Override
	public int hashCode() {
		return a.hashCode() ^ b.hashCode();
	}

}
