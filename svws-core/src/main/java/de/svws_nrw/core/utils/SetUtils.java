package de.svws_nrw.core.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse bietet hilfreiche statische Methoden für Listen.
 *
 * @author Benjamin A. Bartsch
 */
public final class SetUtils {

	private SetUtils() {
	}

	/**
	 * Liefert ein Set (HashSet), welches mit einem Element gefüllt wurde.
	 *
	 * @param <E>      Der Inhaltstyp der Liste.
	 * @param element  Das Element, welches hinzugefügt wird.
	 *
	 * @return ein Set, welches mit einem Element gefüllt wurde.
	 */
	public static <@NotNull E> @NotNull Set<@NotNull E> create1(final @NotNull E element) {
		final @NotNull HashSet<@NotNull E> set = new HashSet<>();
		set.add(element);
		return set;
	}

	/**
	 * Liefert ein Set (HashSet), welches mit einem Element gefüllt wurde.
	 *
	 * @param <E>       Der Inhaltstyp der Liste.
	 * @param element1  Ein Element, welches hinzugefügt wird.
	 * @param element2  Ein Element, welches hinzugefügt wird.
	 *
	 * @return ein Set, welches mit einem Element gefüllt wurde.
	 */
	public static <@NotNull E> @NotNull Set<@NotNull E> create2(final @NotNull E element1, final @NotNull E element2) {
		final @NotNull HashSet<@NotNull E> set = new HashSet<>();
		set.add(element1);
		set.add(element2);
		return set;
	}

	/**
	 * Liefert ein Set (HashSet), welches mit den Elementen der Liste gefüllt wurde.
	 *
	 * @param <E>   Der Inhaltstyp der Liste.
	 * @param list  Die Liste der Elemente.
	 *
	 * @return ein Set (HashSet), welches mit den Elementen der Liste gefüllt wurde.
	 */
	public static <@NotNull E> @NotNull Set<@NotNull E> createFromList(final @NotNull List<@NotNull E> list) {
		final @NotNull HashSet<@NotNull E> set = new HashSet<>();
		set.addAll(list);
		return set;
	}

}
