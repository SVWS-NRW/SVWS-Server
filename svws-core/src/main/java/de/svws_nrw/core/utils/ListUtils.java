package de.svws_nrw.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    public static <@NotNull T> @NotNull List<@NotNull T> getCopyFiltered(final @NotNull List<@NotNull T> list, final @NotNull Predicate<@NotNull T> filter) {
        @NotNull final ArrayList<@NotNull T> listFiltered = new ArrayList<>();
        for (final @NotNull T t : list)
            if (filter.test(t))
                listFiltered.add(t);
        return listFiltered;
    }

	/**
	 * Liefert eine permutierte Kopie des Arrays als Liste.
	 *
     * @param <T>      Der Inhaltstyp der Liste.
     * @param arrayOfT Das Array mit allen Elementen vom Typ T.
	 * @param random   Ein {@link Random}-Objekt zum Permutieren der Elemente des Arrays.
     *
     * @return eine permutierte Kopie des Arrays als Liste.
	 */
	public static <@NotNull T> @NotNull List<@NotNull T> getCopyAsArrayListPermuted(final @NotNull T @NotNull [] arrayOfT, final @NotNull Random random) {
		final @NotNull List<@NotNull T> list = new ArrayList<>();

		// Erstelle permutierte Indices.
		final int[] perm = new int[arrayOfT.length];
		for (int i = 0; i < perm.length; i++)
			perm[i] = i;
		for (int i1 = 0; i1 < perm.length; i1++) {
			final int i2 = random.nextInt(perm.length);
			final int save1 = perm[i1];
			final int save2 = perm[i2];
			perm[i1] = save2;
			perm[i2] = save1;
		}

		// Füge in zufälliger Reihenfolge hinzu.
		for (int i = 0; i < arrayOfT.length; i++)
			list.add(arrayOfT[perm[i]]);

		return list;
	}

}
