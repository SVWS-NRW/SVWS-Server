package de.svws_nrw.core.utils.uvblockung;

/**
 * Eine Regel, welche bei Veränderung den Malus manipuliert.
 *
 * @author Benjamin A. Bartsch
 */
public interface UvAlgorithmusDynDatenRegel {



	/**
	 * Addiert oder subtrahiert den Malus der Regel, je nach Faktor.
	 *
	 * @param faktor   Der Faktor mit dem der Malus multipliziert wird (1 oder -1).
	 */
	void changeMalus(int faktor);

}
