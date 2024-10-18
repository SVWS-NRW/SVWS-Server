package de.svws_nrw.core.adt.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import jakarta.validation.constraints.NotNull;

/**
 * Eine 3D-Map ist ein Mapping von drei Schlüsseln auf einen Wert.
 *
 * @author Benjamin A. Bartsch

 * @param <K1> Der Typ des 1. Schlüsselwertes des Tripels(key1, key2, key3).
 * @param <K2> Der Typ des 2. Schlüsselwertes des Tripels(key1, key2, key3).
 * @param <K3> Der Typ des 3. Schlüsselwertes des Tripels(key1, key2, key3).
 *
 * @param <V> Der Typ des zugeordneten Wertes.
 */
public class HashMap3D<K1, K2, K3, V> {

	private final @NotNull Map<K1, Map<K2, Map<K3, V>>> _map1 = new HashMap<>();

	/**
	 * Konstruktor.
	 */
	public HashMap3D() {
		// no implementation
	}

	/**
	 * Fügt die Zuordnung der Map hinzu.
	 * Falls es einen Teil-Pfad von (key1, key2, key3) nicht gibt, wird er erzeugt.
	 *
	 * @param key1  Der 1. Schlüssel des Tripels(key1, key2, key3).
	 * @param key2  Der 2. Schlüssel des Tripels(key1, key2, key3).
	 * @param key3  Der 3. Schlüssel des Tripels(key1, key2, key3).
	 *
	 * @param value Der zugeordnete Wert. Der Wert null ist erlaubt.
	 */
	public void put(final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull K3 key3, final @NotNull V value) {
		final Map<K2, Map<K3, V>> map2 = _map1.computeIfAbsent(key1, k -> new HashMap<>());
		if (map2 == null)
			throw new NullPointerException();
		final Map<K3, V> map3 = map2.computeIfAbsent(key2, k -> new HashMap<>());
		if (map3 == null)
			throw new NullPointerException();
		map3.put(key3, value);
	}

	/**
	 * Liefert den Wert zum Mapping (key1, key2, key3). <br>
	 * Wirft eine Exception, falls es den Pfad (key1, key2, key3) nicht gibt.
	 *
	 * @param key1  Der 1. Schlüssel des Tripels(key1, key2, key3).
	 * @param key2  Der 2. Schlüssel des Tripels(key1, key2, key3).
	 * @param key3  Der 3. Schlüssel des Tripels(key1, key2, key3).
	 *
	 * @return Den Wert zum Mapping (key1, key2, key3).
	 *
	 * @throws DeveloperNotificationException falls es den Pfad (key1, key2, key3) nicht gibt.
	 */
	public @NotNull V getOrException(final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull K3 key3) throws DeveloperNotificationException {
		final Map<K2, Map<K3, V>> map2 = _map1.get(key1);
		if (map2 == null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ") ungültig!");

		final Map<K3, V> map3 = map2.get(key2);
		if (map3 == null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ", " + key2 + ") ungültig!");

		final V value = map3.get(key3);
		if (value == null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ", key2=" + key2 + ", key3=" + key3 + ") ungültig!");
		return value;
	}

	/**
	 * Liefert den Wert zum Mapping (key1, key2, key3) oder NULL. <br>
	 *
	 * @param key1  Der 1. Schlüssel des Tripels(key1, key2, key3).
	 * @param key2  Der 2. Schlüssel des Tripels(key1, key2, key3).
	 * @param key3  Der 3. Schlüssel des Tripels(key1, key2, key3).
	 *
	 * @return den Wert zum Mapping (key1, key2, key3) oder NULL. <br>
	 */
	public V getOrNull(final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull K3 key3) {
		final Map<K2, Map<K3, V>> map2 = _map1.get(key1);
		if (map2 == null)
			return null;

		final Map<K3, V> map3 = map2.get(key2);
		if (map3 == null)
			return null;

		return map3.get(key3);
	}

	/**
	 * Liefert die Map zum Mapping (key1) oder NULL. <br>
	 *
	 * @param key1  Der 1. Schlüssel.
	 *
	 * @return die Map zum Mapping key1 oder NULL. <br>
	 */
	public Map<K2, Map<K3, V>> getMap2OrNull(final @NotNull K1 key1) {
		return _map1.get(key1);
	}

	/**
	 * Liefert die Map zum Mapping (key1, key2) oder NULL. <br>
	 *
	 * @param key1  Der 1. Schlüssel des Paares(key1, key2).
	 * @param key2  Der 2. Schlüssel des Paares(key1, key2).
	 *
	 * @return die Map zum Mapping (key1, key2) oder NULL. <br>
	 */
	public Map<K3, V> getMap3OrNull(final @NotNull K1 key1, final @NotNull K2 key2) {
		final Map<K2, Map<K3, V>> map2 = _map1.get(key1);
		if (map2 == null)
			return null;

		return map2.get(key2);
	}

	/**
	 * Liefert den Nicht-Null-Wert zum Mapping (key1, key2, key3).<br>
	 * Wirft eine Exception, falls der Pfad (key1, key2, key3) nicht existiert, oder NULL zugeordnet ist.
	 *
	 * @param key1  Der 1. Schlüssel des Tripels(key1, key2, key3).
	 * @param key2  Der 2. Schlüssel des Tripels(key1, key2, key3).
	 * @param key3  Der 3. Schlüssel des Tripels(key1, key2, key3).
	 *
	 * @return den Nicht-Null-Wert zum Mapping (key1, key2, key3).
	 * @throws NullPointerException falls der Pfad (key1, key2, key3) nicht existiert, oder NULL zugeordnet ist.
	 */
	public @NotNull V getNonNullOrException(final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull K3 key3) throws NullPointerException {
		final V value = getOrException(key1, key2, key3);

		if (value == null)
			throw new DeveloperNotificationException("value is NULL!");

		return value;
	}

	/**
	 * Liefert TRUE, falls es den Teilpfad gibt.
	 *
	 * @param key1  Der 1. Schlüssel des Paares(key1, key2).
	 *
	 * @return TRUE, falls es den Teilpfad gibt.
	 */
	public boolean containsKey1(final @NotNull K1 key1) {
		return _map1.containsKey(key1);
	}

	/**
	 * Liefert TRUE, falls es den Teilpfad gibt.
	 *
	 * @param key1  Der 1. Schlüssel des Paares(key1, key2).
	 * @param key2  Der 2. Schlüssel des Paares(key1, key2).
	 *
	 * @return TRUE, falls es den Teilpfad gibt.
	 */
	public boolean containsKey1AndKey2(final @NotNull K1 key1, final @NotNull K2 key2) {
		final Map<K2, Map<K3, V>> map2 = _map1.get(key1);
		if (map2 == null)
			return false;
		return map2.containsKey(key2);
	}

	/**
	 * Liefert TRUE, falls für das Tripel (key1, key2, key3) ein Mapping existiert.
	 *
	 * @param key1  Der 1. Schlüssel des Tripels(key1, key2, key3).
	 * @param key2  Der 2. Schlüssel des Tripels(key1, key2, key3).
	 * @param key3  Der 3. Schlüssel des Tripels(key1, key2, key3).
	 *
	 * @return TRUE, falls für das Tripel (key1, key2, key3) ein Mapping existiert.
	 */
	public boolean contains(final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull K3 key3) {
		final Map<K2, Map<K3, V>> map2 = _map1.get(key1);
		if (map2 == null)
			return false;

		final Map<K3, V> map3 = map2.get(key2);
		if (map3 == null)
			return false;

		return map3.containsKey(key3);
	}

	/**
	 * Löscht alle Zuordnungen der Map.
	 */
	public void clear() {
		_map1.clear();
	}

	/**
	 * Entfernt das Mapping (key1, key2, key3) falls es existiert<br>.
	 * Wirft eine {@link DeveloperNotificationException}, falls das Mapping nicht existiert.
	 *
	 * @param key1  Der 1. Schlüssel des Tripels(key1, key2, key3).
	 * @param key2  Der 2. Schlüssel des Tripels(key1, key2, key3).
	 * @param key3  Der 3. Schlüssel des Tripels(key1, key2, key3).
	 *
	 */
	public void removeOrException(final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull K3 key3) {
		final Map<K2, Map<K3, V>> map2 = _map1.get(key1);
		if (map2 == null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ") ungültig!");

		final Map<K3, V> map3 = map2.get(key2);
		if (map3 == null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ", " + key2 + ") ungültig!");

		if (!map3.containsKey(key3))
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ", key2=" + key2 + ", key3=" + key3 + ") ungültig!");

		// Mapping-Baum abschneiden.
		map3.remove(key3);
		if (map3.isEmpty()) {
			map2.remove(key2);
			if (map2.isEmpty()) {
				_map1.remove(key1);
			}
		}

	}

	/**
	 * Entfernt das Mapping (key1, key2, key3) falls es existiert<br>.
	 *
	 * @param key1  Der 1. Schlüssel des Tripels(key1, key2, key3).
	 * @param key2  Der 2. Schlüssel des Tripels(key1, key2, key3).
	 * @param key3  Der 3. Schlüssel des Tripels(key1, key2, key3).
	 *
	 */
	public void remove(final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull K3 key3) {
		final Map<K2, Map<K3, V>> map2 = _map1.get(key1);
		if (map2 == null)
			return;

		final Map<K3, V> map3 = map2.get(key2);
		if (map3 == null)
			return;

		if (!map3.containsKey(key3))
			return;

		// Mapping-Baum abschneiden.
		map3.remove(key3);
		if (map3.isEmpty()) {
			map2.remove(key2);
			if (map2.isEmpty()) {
				_map1.remove(key1);
			}
		}

	}

	/**
	 * Liefert das KeySet des 1. Schlüssels.
	 *
	 * @return das KeySet der SubMap des 1. Schlüssels.
	 */
	public @NotNull Set<K1> getKeySet() {
		return this._map1.keySet();
	}

	/**
	 * Liefert das EntrySet des 1. Schlüssels.
	 *
	 * @return das EntrySet der SubMap des 1. Schlüssels.
	 */
	public @NotNull Set<Entry<K1, Map<K2, Map<K3, V>>>> getEntrySet() {
		return this._map1.entrySet();
	}

	/**
	 * Liefert eine Liste aller Values in dieser Map.
	 *
	 * @return eine Liste aller Values in dieser Map.
	 */
	public @NotNull List<V> getNonNullValuesAsList() {
		final @NotNull ArrayList<V> list = new ArrayList<>();

		for (final @NotNull Map<K2, Map<K3, V>> map2 : _map1.values())
			for (final @NotNull Map<K3, V> map3 : map2.values())
				for (final @NotNull V value : map3.values())
					list.add(value);

		return list;
	}


	/**
	 * Liefert eine Liste aller Values zum Mapping (key1)
	 *
	 * @param key1  Schlüssel
	 *
	 * @return eine Liste aller Values in dieser Map.
	 */
	public @NotNull List<V> getNonNullValuesOfMap2AsList(final @NotNull K1 key1) {
		final @NotNull ArrayList<V> list = new ArrayList<>();
		final Map<K2, Map<K3, V>> map2 = _map1.get(key1);
		if (map2 != null) {
			for (final @NotNull Map<K3, V> map3 : map2.values())
				for (final @NotNull V value : map3.values())
					list.add(value);
		}

		return list;
	}


	/**
	 * Liefert eine Liste aller Values zum Mapping (key1, key2)
	 *
	 * @param key1  Der 1. Schlüssel des Paares(key1, key2).
	 * @param key2  Der 2. Schlüssel des Paares(key1, key2).
	 *
	 * @return eine Liste aller Values in dieser Map.
	 */
	public @NotNull List<V> getNonNullValuesOfMap3AsList(final @NotNull K1 key1, final @NotNull K2 key2) {
		final @NotNull ArrayList<V> list = new ArrayList<>();
		final Map<K2, Map<K3, V>> map2 = _map1.get(key1);
		if (map2 != null) {
			final Map<K3, V> map3 = map2.get(key2);
			if (map3 != null)
				for (final @NotNull V value : map3.values())
					list.add(value);
		}

		return list;
	}

}
