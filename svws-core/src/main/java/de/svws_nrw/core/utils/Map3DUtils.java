package de.svws_nrw.core.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import de.svws_nrw.core.adt.map.HashMap3D;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse bietet hilfreiche statische Methoden für {@link HashMap3D}
 *
 * @author esr
 */
public final class Map3DUtils {

	private Map3DUtils() {
	}

    /**
     * Liefert die "ArrayList of V" des Schlüssels. Erstellt eine leere "ArrayList of V", falls eine solche Zuordnung nicht existierte.
     *
     * @param <K1>  Der Typ des 1. Schlüssels.
     * @param <K2>  Der Typ des 2. Schlüssels.
     * @param <K3>  Der Typ des 3. Schlüssels.
     * @param <V>   Der Typ der Objekte in der ArrayList.
     * @param map3D   Die Map, welche (K1, K2, K3) auf "ArrayList of V" abbildet.
     * @param key1  Der 1. Schlüssel.
     * @param key2  Der 2. Schlüssel.
     * @param key3  Der 3. Schlüssel.
     *
     * @return die "ArrayList of V" des Schlüssels. Erstellt eine leere "ArrayList of V", falls eine solche Zuordnung nicht existierte.
     */
	public static <@NotNull K1, @NotNull K2, @NotNull K3, @NotNull V>  @NotNull List<@NotNull V> getOrCreateArrayList(final @NotNull HashMap3D<@NotNull K1, @NotNull K2, @NotNull K3, @NotNull List<@NotNull V>> map3D, final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull K3 key3) {
		final List<@NotNull V> list = map3D.getOrNull(key1, key2, key3);
		if (list != null)
			return list;

		final @NotNull ArrayList<@NotNull V> listNeu = new ArrayList<>();
		map3D.put(key1, key2, key3, listNeu);
		return listNeu;
	}


    /**
     * Liefert das "HashSet of V" des Schlüssels. Erstellt ein leeres "HashSet of V", falls eine solche Zuordnung nicht existierte.
     *
     * @param <K1>  Der Typ des 1. Schlüssels.
     * @param <K2>  Der Typ des 2. Schlüssels.
     * @param <K3>  Der Typ des 3. Schlüssels.
     * @param <V>   Der Typ der Objekte in dem HashSet.
     * @param map3D   Die Map, welche (K1, K2, K3) auf "HashSet of V" abbildet.
     * @param key1  Der 1. Schlüssel.
     * @param key2  Der 2. Schlüssel.
     * @param key3  Der 3. Schlüssel.
     *
     * @return die "HashSet of V" des Schlüssels. Erstellt ein leeres "HashSet of V", falls eine solche Zuordnung nicht existierte.
     */
	public static <@NotNull K1, @NotNull K2, @NotNull K3, @NotNull V>  @NotNull HashSet<@NotNull V> getOrCreateHashSet(final @NotNull HashMap3D<@NotNull K1, @NotNull K2, @NotNull K3, @NotNull HashSet<@NotNull V>> map3D, final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull K3 key3) {
		final HashSet<@NotNull V> set = map3D.getOrNull(key1, key2, key3);
		if (set != null)
			return set;

		final @NotNull HashSet<@NotNull V> setNeu = new HashSet<>();
		map3D.put(key1, key2, key3, setNeu);
		return setNeu;
	}

}
