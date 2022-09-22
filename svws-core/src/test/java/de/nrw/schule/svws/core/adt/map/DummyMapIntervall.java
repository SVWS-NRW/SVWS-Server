package de.nrw.schule.svws.core.adt.map;

/**
 * Diese Klasse definiert ein gültiges Intervall. Die Intervallgrenzen können inklusive oder exklusive sein.
 *
 * @author Benjamin A. Bartsch
 */
public class DummyMapIntervall {

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
	DummyMapIntervall(int pFrom, boolean pFromInc, int pTo, boolean pToInc) {
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
	boolean contains(int e) {
		return (e >= min()) && (e <= max());
	}

	/**
	 * Liefert TRUE, wenn der übergebene Wert außerhalb des Intervalls liegt. Beachtet dabei, ob der Wert selbst
	 * inklusive interpretiert werden muss.
	 * 
	 * @param x   Der Wert, der überprüft werden soll, ob er im Intervall liegt.
	 * @param inc TRUE, falls der Wert selbst inklusive interpretiert werden muss.
	 * 
	 * @return TRUE, wenn der übergebene Wert außerhalb des Intervalls liegt. Beachtet dabei, ob der Wert selbst
	 *         inklusive interpretiert werden muss.
	 */
	boolean isOutOfRange(int x, boolean inc) {

		if (x < from)
			return true;

		if ((x == from) && (!fromInc) && (inc))
			return true;

		if (x > to)
			return true;

		if ((x == to) && (!toInc) && (inc))
			return true;

		return false;
	}

}
