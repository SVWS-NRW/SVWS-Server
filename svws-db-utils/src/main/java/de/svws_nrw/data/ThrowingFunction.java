package de.svws_nrw.data;

import java.util.function.Function;

/**
 * Ein funktionales Interface welches {@link Function} erweitert
 * und die Möglichkeit für Exceptions bietet
 *
 * @param <T> der Input-Typ der Funktion
 * @param <R> der Output-Typ der Funktion
 */
@FunctionalInterface
public interface ThrowingFunction<T, R> extends Function<T, R> {

    /**
     * Führt die Funktion auf den Input-Wert t aus und gibt das
     * Ergebnis zurück.
     *
     * @param t   der Input-Wert
     *
     * @return das Ergebnis der Funktion für den Eingabewert
     *
     * @throws Exception   im Fehlerfall
     */
    R applyThrows(T t) throws Exception;

    @Override
    default R apply(final T t) {
        try {
            return applyThrows(t);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

}
