package de.svws_nrw.core.adt.map;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse definiert ein gültiges Intervall. <br>
 * Die Intervallgrenzen können inklusive oder exklusive sein.
 *
 * @author Benjamin A. Bartsch
 *
 * @param <K> Der Typ der Schlüssel-Werte.
 */
public final class AVLMapIntervall<@NotNull K> {

	/**
	 * Ein Dummy-Element für den Schlüsselwert "-Unendlich".
	 */
	static final Object _INFINITY_MINUS = new Object();

	/**
	 * Ein Dummy-Element für den Schlüsselwert "+Unendlich".
	 */
	static final Object _INFINITY_PLUS = new Object();

	/**
	 * Der Anfang des Intervalls.
	 */
	final @NotNull K from;

	/**
	 * Gibt an, ob der Intervall-Anfang inklusive ist.
	 */
	final boolean fromInc;

	/**
	 * Das Ende des Intervalls.
	 */
	final @NotNull K to;

	/**
	 * Gibt an, ob das Intervall-Ende inklusive ist.
	 */
	final boolean toInc;

	/**
	 * @param pFrom    Der Anfang des Intervalls.
	 * @param pFromInc Gibt an, ob der Intervall-Anfang inklusive ist.
	 * @param pTo      Das Ende des Intervalls.
	 * @param pToInc   Gibt an, ob das Intervall-Ende inklusive ist.
	 */
	AVLMapIntervall(final @NotNull K pFrom, final boolean pFromInc, final @NotNull K pTo, final boolean pToInc) {
		from = pFrom;
		fromInc = pFromInc;
		to = pTo;
		toInc = pToInc;
	}

	@Override
	public @NotNull String toString() {
		final @NotNull
		String sFrom = (from == _INFINITY_MINUS) ? "-INF" : "" + from; // from.toString() Transpiler-Problem
		final @NotNull
		String sTo = (to == _INFINITY_PLUS) ? "+INF" : "" + to; // to.toString() Transpiler-Problem
		return "[" + sFrom + ", " + fromInc + ", " + sTo + ", " + toInc + "]";
	}

}
