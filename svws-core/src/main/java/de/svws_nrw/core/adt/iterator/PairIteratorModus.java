package de.svws_nrw.core.adt.iterator;

/**
 * Definiert die Iterationsmodi für den {@link PairIterator}.
 * <ul>
 *   <li>{@link #ALL}        – alle (i, j), auch i == j, insgesamt n² Paare.</li>
 *   <li>{@link #NO_EQUAL}   – alle (i, j) mit i != j, insgesamt n² - n Paare.</li>
 *   <li>{@link #LOWER_ONLY} – alle (i, j) mit i &lt; j, insgesamt (n² - n)/2 Paare, somit keine Duplikate.</li>
 * </ul>
 */
public enum PairIteratorModus {

    /** Alle Paare (i, j), auch i == j. Liefert insgesamt n² Paare. */
    ALL,

    /** Alle Paare (i, j) mit i != j. Liefert insgesamt n² - n Paare. */
    NO_EQUAL,

    /** Alle Paare (i, j) mit i &lt; j. Liefert insgesamt (n² - n)/2 Paare, somit keine Duplikate. */
    LOWER_ONLY

}
