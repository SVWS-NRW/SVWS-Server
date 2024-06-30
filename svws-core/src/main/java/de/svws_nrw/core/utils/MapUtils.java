package de.svws_nrw.core.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;
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
	public static <K, V> @NotNull Set<V> getOrCreateHashSet(final @NotNull Map<K, Set<V>> map, final @NotNull K key) {
		final Set<V> set = map.get(key);
		if (set != null)
			return set;

		final @NotNull HashSet<V> setNeu = new HashSet<>();
		map.put(key, setNeu);
		return setNeu;
	}

	/**
	 * Liefert die "ArrayList of V" des Schlüssels. Erstellt eine leere "ArrayList of V", falls eine solche Zuordnung nicht existierte.
	 *
	 * @param <K>  Der Typ der Schlüssel.
	 * @param <V>  Der Typ der Objekte in der ArrayList.
	 * @param map  Die Map, welche K auf "ArrayList of V" abbildet.
	 * @param key  Der Schlüssel.
	 *
	 * @return die "ArrayList of V" des Schlüssels. Erstellt eine leere "ArrayList of V", falls eine solche Zuordnung nicht existierte.
	 */
	public static <K, V> @NotNull List<V> getOrCreateArrayList(final @NotNull Map<K, List<V>> map, final @NotNull K key) {
		final List<V> list = map.get(key);
		if (list != null)
			return list;

		final @NotNull ArrayList<V> listNeu = new ArrayList<>();
		map.put(key, listNeu);
		return listNeu;
	}

	/**
	 * Fügt der dem Schlüssel K zugeordneten Liste den Wert V hinzu, falls dieser nicht bereits existiert.
	 * Erzeugt eine zugeordnete Liste, falls diese noch nicht existiert.
	 *
	 * @param <K>    Der Typ der Schlüssel.
	 * @param <V>    Der Typ der Objekte in der ArrayList.
	 * @param map    Die Map, welche K auf "ArrayList of V" abbildet.
	 * @param key    Der Schlüssel.
	 * @param value  Der Wert, welcher der Liste der Liste hinzugefügt werden soll.
	 */
	public static <K, V> void addToListIfNotExists(final @NotNull Map<K, List<V>> map, final @NotNull K key, final @NotNull V value) {
		final List<V> list = map.get(key);
		if (list != null) {
			if (!list.contains(value))
				list.add(value);
		} else {
			final List<V> listNeu = new ArrayList<>();
			listNeu.add(value);
			map.put(key, listNeu);
		}
	}

	/**
	 * Fügt der dem Schlüssel K zugeordneten Liste den Wert V hinzu.
	 * Erzeugt eine zugeordnete Liste, falls diese noch nicht existiert.
	 *
	 * @param <K>    Der Typ der Schlüssel.
	 * @param <V>    Der Typ der Objekte in der ArrayList.
	 * @param map    Die Map, welche K auf "ArrayList of V" abbildet.
	 * @param key    Der Schlüssel.
	 * @param value  Der Wert, welcher der Liste der Liste hinzugefügt werden soll.
	 */
	public static <K, V> void addToList(final @NotNull Map<K, List<V>> map, final @NotNull K key, final @NotNull V value) {
		final List<V> list = map.get(key);
		if (list != null) {
			list.add(value);
		} else {
			final List<V> listNeu = new ArrayList<>();
			listNeu.add(value);
			map.put(key, listNeu);
		}
	}

	/**
	 * Liefert die "ArrayList of V" des Schlüssels. Erstellt eine leere "ArrayList of V", falls eine solche Zuordnung nicht existierte.
	 *
	 * @param <K>    Der Typ des 1. Schlüssels.
	 * @param <V>    Der Typ der Objekte in der ArrayList.
	 * @param map    Die Map, welche K auf "ArrayList of V" abbildet.
	 * @param key    Der 1. Schlüssel.
	 * @param value  Der Wert, welcher aus der Liste von (K1, K2) entfernt werden soll.
	 */
	public static <K, V> void removeFromListAndTrimOrException(final @NotNull Map<K, List<V>> map, final @NotNull K key, final @NotNull V value) {
		final List<V> list = DeveloperNotificationException.ifMapGetIsNull(map, key);

		DeveloperNotificationException.ifListRemoveFailes("list", list, value);

		if (list.isEmpty())
			DeveloperNotificationException.ifMapRemoveFailes(map, key);
	}

	/**
	 * Liefert den Wert V des Schlüssels K, falls diese existiert, andernfalls den Default-Wert.
	 *
	 * @param <K>          Der Typ der Schlüssel.
	 * @param <V>          Der Typ der Objekte in der Map.
	 * @param map          Die Map, welche K auf V abbildet.
	 * @param key          Der Schlüssel.
	 * @param defaultValue Der Default Wert, falls kein Mapping existiert.
	 *
	 * @return den Wert V des Schlüssels K, falls diese existiert, andernfalls den Default-Wert.
	 */
	public static <K, V> @NotNull V getOrDefault(final @NotNull Map<K, V> map, final @NotNull K key, final @NotNull V defaultValue) {
		final V value = map.get(key);
		if (value == null)
			return defaultValue;
		return value;
	}

	/**
	 * Falls der Schlüssel K keinen zugeordneten Wert hat, wird der übergebene Wert hinzugefügt.
	 *
	 * @param <K>    Der Typ der Schlüssel.
	 * @param <V>    Der Typ der Objekte in der ArrayList.
	 *
	 * @param map    Die Map, welche K auf V abbildet.
	 * @param key    Der Schlüssel.
	 * @param value  Der Wert, welcher hinzugefügt werden soll, falls es noch keine Zuordnung gibt.
	 */
	public static <K, V> void putNonNullIfNotExists(final @NotNull Map<K, V> map, final @NotNull K key, final @NotNull V value) {
		if (map.containsKey(key))
			return;
		map.put(key, value);
	}

}
