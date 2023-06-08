package de.svws_nrw.core.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse bietet hilfreiche statische Methoden für Maps.
 *
 * @author Benjamin A. Bartsch
 */
public final class MapUtils {

	private MapUtils() {
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
     * Liefert die "ArrayList of V" des Schlüssels. Erstellt eine leere "ArrayList of V", falls eine solche Zuordnung nicht existierte.
     *
     * @param <K>  Der Typ der Schlüssel.
     * @param <V>  Der Typ des Objekte in der ArrayList.
     * @param map  Die Map, welche K auf "ArrayList of V" abbildet.
     * @param key  Der Schlüssel.
     *
     * @return die "ArrayList of V" des Schlüssels. Erstellt eine leere "ArrayList of V", falls eine solche Zuordnung nicht existierte.
     */
	public static <@NotNull K, @NotNull V>  @NotNull List<@NotNull V> getOrCreateArrayList(@NotNull final Map<@NotNull K, @NotNull List<@NotNull V>> map, final @NotNull K key) {
		final List<@NotNull V> list = map.get(key);
		if (list != null)
			return list;

		final @NotNull ArrayList<@NotNull V> listNeu = new ArrayList<>();
		map.put(key, listNeu);
		return listNeu;
	}


}
