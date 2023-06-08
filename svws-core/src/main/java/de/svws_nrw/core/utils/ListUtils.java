package de.svws_nrw.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse bietet hilfreiche statische Methoden für Listen.
 *
 * @author Benjamin A. Bartsch
 */
public final class ListUtils {

	private ListUtils() {
	}

    /**
     * Liefert eine gefilterte Kopie der Liste.
     *
     * @param <T>    Der Inhaltstyp der Liste.
     * @param list   Die zu filternde Liste.
     * @param filter Die Funktion, welche bestimmt ob ein Objekt der Liste gefiltert werden soll.
     *
     * @return eine gefilterte Kopie der Liste.
     */
    public static <@NotNull T> @NotNull List<@NotNull T> getCopyFiltered(@NotNull final List<@NotNull T> list, final @NotNull Predicate<@NotNull T> filter) {
        @NotNull final ArrayList<@NotNull T> listFiltered = new ArrayList<>();
        for (final @NotNull T t : list)
            if (filter.test(t))
                listFiltered.add(t);
        return listFiltered;
    }

}
