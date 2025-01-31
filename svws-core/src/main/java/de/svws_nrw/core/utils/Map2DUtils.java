package de.svws_nrw.core.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse bietet hilfreiche statische Methoden für {@link HashMap2D}
 *
 * @author Benjamin A. Bartsch
 */
public final class Map2DUtils {

	private Map2DUtils() {
	}

	/**
	 * Liefert die "ArrayList of V" des Schlüssels. Erstellt eine leere "ArrayList of V", falls eine solche Zuordnung nicht existierte.
	 *
	 * @param <K1>   Der Typ des 1. Schlüssels.
	 * @param <K2>   Der Typ des 2. Schlüssels.
	 * @param <V>    Der Typ der Objekte in der ArrayList.
	 * @param map2D  Die Map, welche (K1, K2) auf "ArrayList of V" abbildet.
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return die "ArrayList of V" des Schlüssels. Erstellt eine leere "ArrayList of V", falls eine solche Zuordnung nicht existierte.
	 */
	public static <K1, K2, V> @NotNull List<V> getOrCreateArrayList(final @NotNull HashMap2D<K1, K2, List<V>> map2D, final @NotNull K1 key1,
			final @NotNull K2 key2) {
		final List<V> list = map2D.getOrNull(key1, key2);
		if (list != null)
			return list;

		final @NotNull ArrayList<V> listNeu = new ArrayList<>();
		map2D.put(key1, key2, listNeu);
		return listNeu;
	}

	/**
	 * Liefert die "HashSet of V" des Schlüssels. Erstellt eine leere "HashSet of V", falls eine solche Zuordnung nicht existierte.
	 *
	 * @param <K1>   Der Typ des 1. Schlüssels.
	 * @param <K2>   Der Typ des 2. Schlüssels.
	 * @param <V>    Der Typ der Objekte in der ArrayList.
	 * @param map2D  Die Map, welche (K1, K2) auf "HashSet of V" abbildet.
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return die "HashSet of V" des Schlüssels. Erstellt eine leere "HashSet of V", falls eine solche Zuordnung nicht existierte.
	 */
	public static <K1, K2, V> @NotNull Set<V> getOrCreateHashSet(final @NotNull HashMap2D<K1, K2, Set<V>> map2D, final @NotNull K1 key1,
			final @NotNull K2 key2) {
		final Set<V> set = map2D.getOrNull(key1, key2);
		if (set != null)
			return set;

		final @NotNull HashSet<V> setNeu = new HashSet<>();
		map2D.put(key1, key2, setNeu);
		return setNeu;
	}

	/**
	 * Liefert den Wert V des Schlüssels K, falls diese existiert, andernfalls den Default-Wert.
	 *
	 * @param <K1>         Der Typ des 1. Schlüssels.
	 * @param <K2>         Der Typ des 2. Schlüssels.
	 * @param <V>          Der Typ der Objekte in der Map.
	 * @param map2D        Die Map, welche (K1, K2) auf V abbildet.
	 * @param key1         Der 1. Schlüssel.
	 * @param key2         Der 2. Schlüssel.
	 * @param defaultValue Der Default Wert, falls kein Mapping existiert.
	 *
	 * @return den Wert V des Schlüssels K, falls diese existiert, andernfalls den Default-Wert.
	 */
	public static <K1, K2, V> @NotNull V getOrDefault(
			final @NotNull HashMap2D<K1, K2, V> map2D,
			final @NotNull K1 key1,
			final @NotNull K2 key2,
			final @NotNull V defaultValue) {

		final V value = map2D.getOrNull(key1, key2);
		if (value == null)
			return defaultValue;
		return value;
	}

	/**
	 * Fügt der dem Schlüssel (K1, K2) zugeordneten Liste den Wert V hinzu.
	 * Erzeugt eine Liste, falls noch keine existiert.
	 *
	 * @param <K1>   Der Typ des 1. Schlüssels.
	 * @param <K2>   Der Typ des 2. Schlüssels.
	 * @param <V>    Der Typ der Objekte in der ArrayList.
	 * @param map2D  Die Map, welche (K1, K2) auf "ArrayList of V" abbildet.
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param value  Der Wert, welcher der zugeordneten Liste hinzugefügt wird.
	 */
	public static <K1, K2, V> void addToList(final @NotNull HashMap2D<K1, K2, List<V>> map2D, final @NotNull K1 key1, final @NotNull K2 key2,
			final @NotNull V value) {
		final List<V> list = map2D.getOrNull(key1, key2);
		if (list != null) {
			list.add(value);
		} else {
			final @NotNull ArrayList<V> listNeu = new ArrayList<>();
			listNeu.add(value);
			map2D.put(key1, key2, listNeu);
		}
	}

	/**
	 * Fügt der dem Schlüssel (K1, K2) zugeordneten Liste den Wert V hinzu, falls dieser noch nicht existiert.
	 * Erzeugt eine Liste, falls noch keine existiert.
	 *
	 * @param <K1>   Der Typ des 1. Schlüssels.
	 * @param <K2>   Der Typ des 2. Schlüssels.
	 * @param <V>    Der Typ der Objekte in der ArrayList.
	 * @param map2D  Die Map, welche (K1, K2) auf "ArrayList of V" abbildet.
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param value  Der Wert, welcher der zugeordneten Liste hinzugefügt wird.
	 */
	public static <K1, K2, V> void addToListIfNotExists(final @NotNull HashMap2D<K1, K2, List<V>> map2D, final @NotNull K1 key1, final @NotNull K2 key2,
			final @NotNull V value) {
		final List<V> list = map2D.getOrNull(key1, key2);
		if (list != null) {
			if (!list.contains(value))
				list.add(value);
		} else {
			final @NotNull ArrayList<V> listNeu = new ArrayList<>();
			listNeu.add(value);
			map2D.put(key1, key2, listNeu);
		}
	}

	/**
	 * Liefert die "ArrayList of V" des Schlüssels. Erstellt eine leere "ArrayList of V", falls eine solche Zuordnung nicht existierte.
	 *
	 * @param <K1>   Der Typ des 1. Schlüssels.
	 * @param <K2>   Der Typ des 2. Schlüssels.
	 * @param <V>    Der Typ der Objekte in der ArrayList.
	 * @param map2D  Die Map, welche (K1, K2) auf "ArrayList of V" abbildet.
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param value  Der Wert, welcher aus der Liste von (K1, K2) entfernt werden soll.
	 */
	public static <K1, K2, V> void removeFromListAndTrimOrException(final @NotNull HashMap2D<K1, K2, List<V>> map2D, final @NotNull K1 key1,
			final @NotNull K2 key2, final @NotNull V value) {
		final List<V> list = map2D.getOrException(key1, key2);

		DeveloperNotificationException.ifListRemoveFailes("list", list, value);

		if (list.isEmpty())
			map2D.removeOrException(key1, key2);
	}
}
