package de.svws_nrw.core.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse bietet hilfreiche statische Methoden für Maps.
 *
 * @author Benjamin A. Bartsch
 */
public final class CollectionUtils {

	private CollectionUtils() {
	}

    /**
     * Liefert das "Set of V" des Schlüssels. Erstellt ein leeres "Set of V", falls eine solche Zuordnung nicht existierte.
     *
     * @param <K>  Der Typ der Schlüssel.
     * @param <V>  Der Typ des Objekte im Set.
     * @param map  Die Map, welche K auf "Set of V" abbildet.
     * @param key  Der Schlüssel.
     *
     * @return das "Set of V" des Schlüssels. Erstellt ein leeres "Set of V", falls eine solche Zuordnung nicht existierte.
     */
	public static <@NotNull K, @NotNull V>  @NotNull Set<@NotNull V> getOrCreateHashSet(@NotNull final Map<@NotNull K, @NotNull Set<@NotNull V>> map, final @NotNull K key) {
		final Set<@NotNull V> set = map.get(key);
		if (set != null)
			return set;

		final @NotNull HashSet<@NotNull V> setNeu = new HashSet<>();
		map.put(key, setNeu);
		return setNeu;
	}

	/**
	 * Liefert eine gefilterte {@link Collection} und liefert die Ergebnismenge in einem {@link Set}.
	 *
	 * @param <E>       Der Typ der Elemente in der {@link Collection}.
	 * @param values    Die {@link Collection}.
	 * @param predicate Das {@link Predicate}, welches entscheidet, ob ein Element zur Ergebnismenge gehört.
	 *
	 * @return eine gefilterte {@link Collection} und liefert die Ergebnismenge in einem {@link Set}.
	 */
	public static <@NotNull E> @NotNull Set<@NotNull E> toFilteredHashSet(final @NotNull Collection<@NotNull E> values, final @NotNull Predicate<@NotNull E> predicate) {
		final @NotNull HashSet<@NotNull E> set = new HashSet<>();
		for (final @NotNull E e : values)
			if (predicate.test(e))
				set.add(e);
		return set;
	}

	/**
	 * Liefert eine gefilterte {@link Collection} und liefert die Ergebnismenge in einer {@link List}.
	 *
	 * @param <E>       Der Typ der Elemente in der {@link Collection}.
	 * @param values    Die {@link Collection}.
	 * @param predicate Das {@link Predicate}, welches entscheidet, ob ein Element zur Ergebnismenge gehört.
	 *
	 * @return eine gefilterte {@link Collection} und liefert die Ergebnismenge in einer {@link List}.
	 */
	public static <@NotNull E> @NotNull List<@NotNull E> toFilteredArrayList(final @NotNull Collection<@NotNull E> values, final @NotNull Predicate<@NotNull E> predicate) {
		final @NotNull ArrayList<@NotNull E> set = new ArrayList<>();
		for (final @NotNull E e : values)
			if (predicate.test(e))
				set.add(e);
		return set;
	}

}
