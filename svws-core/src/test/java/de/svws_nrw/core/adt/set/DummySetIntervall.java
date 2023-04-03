package de.svws_nrw.core.adt.set;

/**
 * Diese Klasse definiert ein gültiges Intervall. Die Intervallgrenzen können inklusive oder exklusive sein.
 *
 * @author Benjamin A. Bartsch
 *
 */
public final class DummySetIntervall {

	/**
	 * Der Anfang des Intervalls.
	 */
	final int from;

	/**
	 * Gibt an, ob der Intervall-Anfang inklusive ist.
	 */
	final boolean fromInc;

	/**
	 * Das Ende des Intervalls.
	 */
	final int to;

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
	DummySetIntervall(final int pFrom, final boolean pFromInc, final int pTo, final boolean pToInc) {
		from = pFrom;
		fromInc = pFromInc;
		to = pTo;
		toInc = pToInc;
	}

	@Override
	public String toString() {
		return "[" + from + ", " + fromInc + ", " + to + ", " + toInc + "]";
	}

	/**
	 * Liefert das ganzzahlige Minimum des Intervalls. Beachtet dabei, ob die Intervallsgrenze inklusive interpretiert
	 * werden muss.
	 *
	 * @return Das ganzzahlige Minimum des Intervalls. Beachtet dabei, ob die Intervallsgrenze inklusive interpretiert
	 *         werden muss.
	 */
	int min() {
		return fromInc ? from : from + 1;
	}

	/**
	 * Liefert das ganzzahlige Maximum des Intervalls. Beachtet dabei, ob die Intervallsgrenze inklusive interpretiert
	 * werden muss.
	 *
	 * @return Das ganzzahlige Maximum des Intervalls. Beachtet dabei, ob die Intervallsgrenze inklusive interpretiert
	 *         werden muss.
	 */
	int max() {
		return toInc ? to : to - 1;
	}

	/**
	 * Liefert TRUE, wenn der übergebene Wert im Intervall liegt.
	 *
	 * @param e Der Wert der überprüft werden soll, ob er im Intervall liegt.
	 *
	 * @return TRUE, wenn der übergebene Wert im Intervall liegt.
	 */
	boolean contains(final int e) {
		return (e >= min()) && (e <= max());
	}

	/**
	 * Liefert TRUE, wenn der übergebene Wert im Intervall liegt. Beachtet dabei, ob der Wert inklusive interpretiert
	 * werden muss.
	 *
	 * @param e         Der Wert der überprüft werden soll, ob er im Intervall liegt.
	 * @param inclusive Falls TRUE, dann soll der übergebene Wert inklusive interpretiert werden.
	 *
	 * @return TRUE, wenn der übergebene Wert im Intervall liegt.
	 */
	boolean contains(final int e, final boolean inclusive) {
		if (inclusive)
			return contains(e);
		return (e >= min() + 1) && (e <= max() - 1);
	}

}
