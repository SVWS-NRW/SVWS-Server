package de.svws_nrw.core.adt.map;

import java.util.HashMap;
import java.util.Map;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import jakarta.validation.constraints.NotNull;

/**
 * Eine 2D-Map ist ein Mapping von zwei Schlüsseln auf einen Wert.
 *
 * @author Benjamin A. Bartsch

 * @param <K1> Der Typ des 1. Schlüsselwertes des Paares(key1, key2).
 * @param <K2> Der Typ des 2. Schlüsselwertes des Paares(key1, key2).
 * @param <V> Der Typ des zugeordneten Wertes.
 */
public class HashMap2D<@NotNull K1, @NotNull K2, @NotNull V> {

	private final @NotNull Map<@NotNull K1, @NotNull Map<@NotNull K2, V>> _map = new HashMap<>();

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
	public void put(final @NotNull K1 key1, final @NotNull K2 key2, final V value) {
		Map<@NotNull K2, V> map2 = _map.get(key1);
		if (map2 == null) {
			map2 = new HashMap<>();
			_map.put(key1, map2);
		}
		map2.put(key2, value);
	}

	/**
	 * Liefert den Wert zum Mapping (key1, key2). <br>
	 * Falls es den Pfad (key1) oder (key1, key2) nicht gibt, wird eine Exception geworfen.
	 *
	 * @param key1  Der 1. Schlüssel des Paares(key1, key2).
	 * @param key2  Der 2. Schlüssel des Paares(key1, key2).
	 *
	 * @return Den Wert zum Mapping (key1, key2).
	 * @throws NullPointerException Falls ein Teilpfad (key1, key2) nicht existiert!
	 */
	public V getOrException(final @NotNull K1 key1, final @NotNull K2 key2) throws NullPointerException {
		final @NotNull Map<@NotNull K2, V> map2 = getSubMapOrException(key1);
		if (!map2.containsKey(key2))
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ", key2=" + key2 + ") ungültig!");
		return map2.get(key2);
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
		final Map<@NotNull K2, V> map2 = _map.get(key1);
		if (map2 == null)
			return null;
		return map2.get(key2);
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
	 * @throws NullPointerException Falls ein Teilpfad (key1, key2) nicht existiert!
	 */
	public @NotNull V getNonNullOrException(final @NotNull K1 key1, final @NotNull K2 key2) throws NullPointerException {
		final V value = getOrException(key1, key2);
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
	public @NotNull Map<@NotNull K2, V> getSubMapOrException(final @NotNull K1 key1) {
		final Map<@NotNull K2, V> map2 = _map.get(key1);
		if (map2 == null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ") ungültig!");
		return map2;
	}

	/**
	 * Löscht alle Zuordnungen der Map.
	 */
	public void clear() {
		_map.clear();
	}

}
