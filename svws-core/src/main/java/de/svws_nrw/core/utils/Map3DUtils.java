package de.svws_nrw.core.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	public static <K1, K2, K3, V>  @NotNull List<V> getOrCreateArrayList(final @NotNull HashMap3D<K1, K2, K3, List<V>> map3D, final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull K3 key3) {
		final List<V> list = map3D.getOrNull(key1, key2, key3);
		if (list != null)
			return list;

		final @NotNull ArrayList<V> listNeu = new ArrayList<>();
		map3D.put(key1, key2, key3, listNeu);
		return listNeu;
	}


    /**
     * Liefert das "Set of V" des Schlüssels. Erstellt ein leeres "Set of V", falls eine solche Zuordnung nicht existierte.
     *
     * @param <K1>  Der Typ des 1. Schlüssels.
     * @param <K2>  Der Typ des 2. Schlüssels.
     * @param <K3>  Der Typ des 3. Schlüssels.
     * @param <V>   Der Typ der Objekte in dem HashSet.
     * @param map3D   Die Map, welche (K1, K2, K3) auf "Set of V" abbildet.
     * @param key1  Der 1. Schlüssel.
     * @param key2  Der 2. Schlüssel.
     * @param key3  Der 3. Schlüssel.
     *
     * @return die "Set of V" des Schlüssels. Erstellt ein leeres "Set of V", falls eine solche Zuordnung nicht existierte.
     */
	public static <K1, K2, K3, V>  @NotNull Set<V> getOrCreateSet(final @NotNull HashMap3D<K1, K2, K3, Set<V>> map3D, final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull K3 key3) {
		final Set<V> set = map3D.getOrNull(key1, key2, key3);
		if (set != null)
			return set;
		final @NotNull HashSet<V> setNeu = new HashSet<>();
		map3D.put(key1, key2, key3, setNeu);
		return setNeu;
	}


    /**
     * Fügt der dem Schlüssel (K1, K2, K3) zugeordneten Liste den Wert V hinzu.
     * Erzeugt eine Liste, falls noch keine existiert.
     *
     * @param <K1>   der Typ des 1. Schlüssels.
     * @param <K2>   der Typ des 2. Schlüssels.
     * @param <K3>   der Typ des 3. Schlüssels.
     * @param <V>    der Typ der Objekte in der ArrayList.
     * @param map3D  die Map, welche (K1, K2, K3) auf "ArrayList of V" abbildet.
     * @param key1   der 1. Schlüssel.
     * @param key2   der 2. Schlüssel.
     * @param key3   der 3. Schlüssel.
     * @param value  der Wert, welcher der zugeordneten Liste hinzugefügt wird.
     */
	public static <K1, K2, K3, V> void addToList(final @NotNull HashMap3D<K1, K2, K3, List<V>> map3D, final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull K3 key3, final @NotNull V value) {
		final List<V> list = map3D.getOrNull(key1, key2, key3);
		if (list != null) {
			list.add(value);
		} else {
			final @NotNull ArrayList<V> listNeu = new ArrayList<>();
			listNeu.add(value);
			map3D.put(key1, key2, key3, listNeu);
		}
	}


}
