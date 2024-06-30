package de.svws_nrw.core.adt.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import jakarta.validation.constraints.NotNull;

/**
 * Eine 2D-Map ist ein Mapping von zwei Schlüsseln auf einen Wert.
 *
 * @author Benjamin A. Bartsch

 * @param <K1> Der Typ des 1. Schlüsselwertes des Paares(key1, key2).
 * @param <K2> Der Typ des 2. Schlüsselwertes des Paares(key1, key2).
 * @param <V>  Der Typ des zugeordneten Wertes.
 */
public class HashMap2D<K1, K2, V> {

	private final @NotNull Map<K1, Map<K2, V>> _map = new HashMap<>();

	/**
	 * Konstruktor.
	 */
	public HashMap2D() {
		// no implementation
	}

	/**
	 * Fügt die Zuordnung der Map hinzu.
	 * Falls es den Pfad (key1) oder (key1, key2) nicht gibt, wird er erzeugt.
	 *
	 * @param key1  Der 1. Schlüssel des Paares(key1, key2).
	 * @param key2  Der 2. Schlüssel des Paares(key1, key2).
	 * @param value Der zugeordnete Wert. Der Wert null ist erlaubt.
	 */
	public void put(final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull V value) {
		Map<K2, V> map2 = _map.get(key1);
		if (map2 == null) {
			map2 = new HashMap<>();
			_map.put(key1, map2);
		}
		map2.put(key2, value);
	}

	/**
	 * Liefert den Wert zum Mapping (key1, key2) oder NULL. <br>
	 *
	 * @param key1  Der 1. Schlüssel des Paares(key1, key2).
	 * @param key2  Der 2. Schlüssel des Paares(key1, key2).
	 *
	 * @return den Wert zum Mapping (key1, key2) oder NULL. <br>
	 */
	public V getOrNull(final @NotNull K1 key1, final @NotNull K2 key2) {
		final Map<K2, V> map2 = _map.get(key1);
		if (map2 == null)
			return null;
		return map2.get(key2);
	}

	/**
	 * Liefert den Wert zum Mapping (key1, key2). <br>
	 * Falls es den Pfad (key1) oder (key1, key2) nicht gibt, wird eine Exception geworfen.
	 *
	 * @param key1  Der 1. Schlüssel des Paares(key1, key2).
	 * @param key2  Der 2. Schlüssel des Paares(key1, key2).
	 *
	 * @return Den Wert zum Mapping (key1, key2).
	 * @throws DeveloperNotificationException Falls ein Teilpfad (key1, key2) nicht existiert!
	 */
	@SuppressWarnings("cast")
	public @NotNull V getOrException(final @NotNull K1 key1, final @NotNull K2 key2) throws DeveloperNotificationException {
		final @NotNull Map<K2, V> map2 = getSubMapOrException(key1);
		if (!map2.containsKey(key2))
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ", key2=" + key2 + ") ungültig!");
		final V value = map2.get(key2);
		if (value == null)
			return (@NotNull V) value;
		return value;
	}

	/**
	 * Liefert den Nicht-Null-Wert zum Mapping (key1, key2).<br>
	 * Falls es den Pfad (key1) oder (key1, key2) nicht gibt, wird eine Exception geworfen.<br>
	 * Falls der zugeordnete Wert NULL ist, wird eine Exception geworfen.
	 *
	 * @param key1  Der 1. Schlüssel des Paares(key1, key2).
	 * @param key2  Der 2. Schlüssel des Paares(key1, key2).
	 *
	 * @return Den Nicht-Null-Wert zum Mapping (key1, key2).
	 * @throws DeveloperNotificationException Falls ein Teilpfad (key1, key2) nicht existiert!
	 */
	public @NotNull V getNonNullOrException2(final @NotNull K1 key1, final @NotNull K2 key2) throws DeveloperNotificationException {
		final @NotNull V value = getOrException(key1, key2);
		if (value == null)
			throw new DeveloperNotificationException("value is NULL!");
		return value;
	}

	/**
	 * Liefert für den Schlüssel (key1) die Map (key2 --> V) oder eine Exception.
	 *
	 * @param key1 Der 1. Schlüssel des Paares(key1, key2).
	 *
	 * @return Für den Schlüssel (key1) die Map (key2 --> V) oder eine Exception.
	 */
	public @NotNull Map<K2, V> getSubMapOrException(final @NotNull K1 key1) {
		final Map<K2, V> map2 = _map.get(key1);
		if (map2 == null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ") ungültig!");
		return map2;
	}

	/**
	 * Liefert für den Schlüssel (key1) die Map (key2 --> V) oder NULL.
	 *
	 * @param key1 Der 1. Schlüssel des Paares(key1, key2).
	 *
	 * @return Für den Schlüssel (key1) die Map (key2 --> V) oder NULL.
	 */
	public Map<K2, V> getSubMapOrNull(final @NotNull K1 key1) {
		return _map.get(key1);
	}

	/**
	 * Liefert TRUE, falls für den Schlüssel (key1, key2) ein Mapping existiert.
	 *
	 * @param key1  Der 1. Schlüssel des Paares(key1, key2).
	 * @param key2  Der 2. Schlüssel des Paares(key1, key2).
	 *
	 * @return TRUE, falls für den Schlüssel (key1, key2) ein Mapping existiert.
	 */
	public boolean contains(final @NotNull K1 key1, final @NotNull K2 key2) {
		final Map<K2, V> map2 = _map.get(key1);
		if (map2 == null)
			return false;
		return map2.containsKey(key2);
	}

	/**
	 * Liefert TRUE, falls es den Teilpfad gibt.
	 *
	 * @param key1  Der 1. Schlüssel des Paares(key1, key2).
	 *
	 * @return TRUE, falls es den Teilpfad gibt.
	 */
	public boolean containsKey1(final @NotNull K1 key1) {
		return _map.containsKey(key1);
	}

	/**
	 * Löscht alle Zuordnungen der Map.
	 */
	public void clear() {
		_map.clear();
	}

	/**
	 * Entfernt das Mapping (key1, key2) falls es existiert, andernfalls wird eine {@link DeveloperNotificationException} geworfen.
	 *
	 * @param key1  Der 1. Schlüssel des Paares(key1, key2).
	 * @param key2  Der 2. Schlüssel des Paares(key1, key2).
	 *
	 * @return Den Wert zum Mapping (key1, key2) vor dem Löschen.
	 */
	@SuppressWarnings("cast")
	public @NotNull V removeOrException(final @NotNull K1 key1, final @NotNull K2 key2) {
		final @NotNull Map<K2, V> map2 = getSubMapOrException(key1);
		if (!map2.containsKey(key2))
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ", key2=" + key2 + ") ungültig!");
		final V value = map2.remove(key2);
		if (map2.isEmpty())   // Wenn map2 durch das Löschen leer wurde, dann entferne den key1 (trim).
			_map.remove(key1);
		if (value == null)
			return (@NotNull V) value;
		return value;
	}

	/**
	 * Entfernt für den Schlüssel (key1) die Submap, falls key1 existiert, andernfalls passiert nichts.
	 *
	 * @param key1  Der 1. Schlüssel.
	 *
	 * @return Für den Schlüssel (key1) die Map (key2 --> V) oder null.
	 */
	public Map<K2, V> removeSubMap(final @NotNull K1 key1) {
		return _map.remove(key1);
	}

	/**
	 * Entfernt für den Schlüssel (key1) die Submap, falls key1 existiert, andernfalls wird eine {@link DeveloperNotificationException} geworfen.
	 *
	 * @param key1  Der 1. Schlüssel.
	 *
	 * @return Für den Schlüssel (key1) die Map (key2 --> V) oder eine Exception.
	 */
	public @NotNull Map<K2, V> removeSubMapOrException(final @NotNull K1 key1) {
		final Map<K2, V> map2 = _map.remove(key1);
		if (map2 == null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ") existiert nicht!");
		return map2;
	}

	/**
	 * Liefert eine Liste aller Values des 1. Keys in dieser Map.
	 *
	 * @param key1  Der 1. Schlüssel des Paares(key1, key2).
	 *
	 * @return eine Liste aller Values des 1. Keys in dieser Map.
	 */
	public @NotNull List<V> getNonNullValuesOfKey1AsList(final @NotNull K1 key1) {
		return new ArrayList<>(getSubMapOrException(key1).values());
	}

	/**
	 * Liefert eine Liste aller Values in dieser Map.
	 *
	 * @return eine Liste aller Values in dieser Map.
	 */
	public @NotNull List<V> getNonNullValuesAsList() {
		final @NotNull ArrayList<V> list = new ArrayList<>();

		for (final @NotNull Map<K2, V> map2 : _map.values())
			for (final @NotNull V value : map2.values())
				list.add(value);

		return list;
	}

	/**
	 * Liefert das KeySet des 1. Schlüssels.
	 *
	 * @return das KeySet der SubMap des 1. Schlüssels.
	 */
	public @NotNull Set<K1> getKeySet() {
		return this._map.keySet();
	}

	/**
	 * Liefert das KeySet der SubMap des 1. Schlüssels.
	 *
	 * @param key1  Der 1. Schlüssel des Paares(key1, key2).
	 *
	 * @return das KeySet der SubMap des 1. Schlüssels.
	 */
	public @NotNull Set<K2> getKeySetOf(final @NotNull K1 key1) {
		return getSubMapOrException(key1).keySet();
	}

	/**
	 * Liefert die Anzahl an Mappings, der des Pfades (key1) oder 0, falls der Pfad nicht existiert.
	 *
	 * @param key1  Der 1. Schlüssel des Paares(key1, key2).
	 *
	 * @return die Anzahl an Mappings, der des Pfades (key1) oder 0, falls der Pfad nicht existiert.
	 */
	public int getSubMapSizeOrZero(final @NotNull K1 key1) {
		final Map<K2, V> map2 = _map.get(key1);
		if (map2 == null)
			return 0;
		return map2.size();
	}

	/**
	 * Liefert die Anzahl an gespeicherten Mappings.
	 *
	 * @return die Anzahl an gespeicherten Mappings.
	 */
	public int size() {
		int size = 0;

		for (final @NotNull Map<K2, V> map2 : _map.values())
			size += map2.size();

		return size;
	}

}
