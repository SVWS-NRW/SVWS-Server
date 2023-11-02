package de.svws_nrw.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;
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
     * @param <E>    Der Inhaltstyp der Liste.
     * @param list   Die zu filternde Liste.
     * @param filter Die Funktion, welche bestimmt ob ein Objekt der Liste gefiltert werden soll.
     *
     * @return eine gefilterte Kopie der Liste.
     */
    public static <@NotNull E> @NotNull List<@NotNull E> getCopyFiltered(final @NotNull List<@NotNull E> list, final @NotNull Predicate<@NotNull E> filter) {
        final @NotNull ArrayList<@NotNull E> listFiltered = new ArrayList<>();
        for (final @NotNull E t : list)
            if (filter.test(t))
                listFiltered.add(t);
        return listFiltered;
    }

    /**
     * Liefert die Anzahl an Elementen, die in der Liste den Filterkriterien entsprechen.
     *
     * @param <E>    Der Inhaltstyp der Liste.
     * @param list   Die zu filternde Liste.
     * @param filter Die Funktion, welche bestimmt ob ein Objekt das Kriterium erfüllt.
     *
     * @return die Anzahl an Elementen, die in der Liste den Filterkriterien entsprechen.
     */
    public static <@NotNull E> int getCountFiltered(final @NotNull List<@NotNull E> list, final @NotNull Predicate<@NotNull E> filter) {
        int summe = 0;
        for (final @NotNull E t : list)
            if (filter.test(t))
                summe++;
        return summe;
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

    /**
     * Fügt das Element der Liste hinzu, falls es nicht bereits existiert. Verhindert so Duplikate.
     *
     * @param <E>    Der Inhaltstyp der Liste.
     * @param list   Die Liste in welche hinzugefügt wird.
     * @param e      Das Element, welches ggf. hinzugefügt wird.
     *
     */
    public static <@NotNull E> void addIfNotExists(final @NotNull List<@NotNull E> list, final @NotNull E e) {
        if (!list.contains(e))
        	list.add(e);
    }

    /**
     * Fügt das Element der Liste hinzu, falls es nicht bereits existiert. Verhindert so Duplikate.
     *
     * @param <E>        Der Inhaltstyp der Liste.
     * @param list       Die Liste in welche hinzugefügt wird.
     * @param listToAdd  Die Liste aus welcher kopiert wird.
     */
    public static <@NotNull E> void addAllIfNotExists(final @NotNull List<@NotNull E> list, final @NotNull List<@NotNull E> listToAdd) {
        for (final E e : listToAdd)
        	if (!list.contains(e))
        		list.add(e);
    }

    /**
     * Liefert das NON NULL Element an Index i, oder eine Exception.
     *
     * @param <E>   Der Inhaltstyp der Liste.
     * @param list  Die Liste.
     * @param i     Der Index i.
     *
     * @return das NON NULL Element an Index i, oder eine Exception.
     */
    public static <@NotNull E> @NotNull E getNonNullElementAtOrException(final @NotNull List<@NotNull E> list, final int i) {
    	final E element = list.get(i);
    	if (element == null)
    		throw new DeveloperNotificationException("Kein Element bei Index " + i + "!");
    	return element;
    }

    /**
     * Liefert eine Liste, welche mit einem Element gefüllt wurde.
     *
     * @param <E>      Der Inhaltstyp der Liste.
     * @param element  Das Element, welches hinzugefügt wird.
     *
     * @return eine Liste, welche mit einem Element gefüllt wurde.
     */
    public static <@NotNull E> @NotNull List<@NotNull E> create1(final @NotNull E element) {
        final @NotNull ArrayList<@NotNull E> list = new ArrayList<>();
        list.add(element);
        return list;
    }

}
