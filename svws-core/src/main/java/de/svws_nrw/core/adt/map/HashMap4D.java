package de.svws_nrw.core.adt.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import jakarta.validation.constraints.NotNull;

/**
 * Eine 4D-Map ist ein Mapping von vier Schlüsseln auf einen Wert.
 *
 * @param <K1> Der Typ des 1. Schlüsselwertes des Quadrupels(key1, key2, key3, key4).
 * @param <K2> Der Typ des 2. Schlüsselwertes des Quadrupels(key1, key2, key3, key4).
 * @param <K3> Der Typ des 3. Schlüsselwertes des Quadrupels(key1, key2, key3, key4).
 * @param <K4> Der Typ des 4. Schlüsselwertes des Quadrupels(key1, key2, key3, key4).
 *
 * @param <V>  Der Typ des zugeordneten Wertes.
 */
public class HashMap4D<@NotNull K1, @NotNull K2, @NotNull K3, @NotNull K4, @NotNull V> {

	private final @NotNull Map<@NotNull K1, @NotNull Map<@NotNull K2, @NotNull Map<@NotNull K3, @NotNull Map<@NotNull K4, @NotNull V>>>> _map1 = new HashMap<>();

	/**
	 * Konstruktor.
	 */
	public HashMap4D() {
		// no implementation
	}

	/**
	 * Fügt die Zuordnung der Map hinzu. Falls es einen Teil-Pfad von (key1, key2,
	 * key3, key4) nicht gibt, wird er erzeugt.
	 *
	 * @param key1  Der 1. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key2  Der 2. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key3  Der 3. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key4  Der 4. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 *
	 * @param value Der zugeordnete Wert. Der Wert null ist erlaubt.
	 */
	public void put(final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull K3 key3, final @NotNull K4 key4, final @NotNull V value) {
		Map<@NotNull K2, @NotNull Map<@NotNull K3, @NotNull Map<@NotNull K4, @NotNull V>>> map2 = _map1.get(key1);
		if (map2 == null) {
			map2 = new HashMap<>();
			_map1.put(key1, map2);
		}

		Map<@NotNull K3, @NotNull Map<@NotNull K4, @NotNull V>> map3 = map2.get(key2);
		if (map3 == null) {
			map3 = new HashMap<>();
			map2.put(key2, map3);
		}

		Map<@NotNull K4, @NotNull V> map4 = map3.get(key3);
		if (map4 == null) {
			map4 = new HashMap<>();
			map3.put(key3, map4);
		}

		map4.put(key4, value);
	}

	/**
	 * Liefert den Wert zum Mapping (key1, key2, key3, key4). <br>
	 * Wirft eine Exception, falls es den Pfad (key1, key2, key3, key4) nicht gibt.
	 *
	 * @param key1 Der 1. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key2 Der 2. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key3 Der 3. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key4 Der 4. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 *
	 * @return Den Wert zum Mapping (key1, key2, key3, key4).
	 *
	 * @throws NullPointerException falls es den Pfad (key1, key2, key3, key4) nicht gibt.
	 */
	public V getOrException(final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull K3 key3, final @NotNull K4 key4) throws NullPointerException {
		final Map<@NotNull K2, @NotNull Map<@NotNull K3, @NotNull Map<@NotNull K4, @NotNull V>>> map2 = _map1.get(key1);
		if (map2 == null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ") ungültig!");

		final Map<@NotNull K3, @NotNull Map<@NotNull K4, @NotNull V>> map3 = map2.get(key2);
		if (map3 == null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ", " + key2 + ") ungültig!");

		final Map<@NotNull K4, @NotNull V> map4 = map3.get(key3);
		if (map4 == null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ", key2=" + key2 + ", key3=" + key3 + ") ungültig!");

		if (!map4.containsKey(key4))
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ", key2=" + key2 + ", key3=" + key3 + ", key4=" + key4 + ") ungültig!");

		return map4.get(key4);
	}

	/**
	 * Liefert den Wert zum Mapping (key1, key2, key3, key4) oder NULL. <br>
	 *
	 * @param key1 Der 1. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key2 Der 2. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key3 Der 3. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key4 Der 4. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 *
	 * @return den Wert zum Mapping (key1, key2, key3, key4) oder NULL. <br>
	 */
	public V getOrNull(final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull K3 key3, final @NotNull K4 key4) {
		final Map<@NotNull K2, @NotNull Map<@NotNull K3, @NotNull Map<@NotNull K4, @NotNull V>>> map2 = _map1.get(key1);
		if (map2 == null)
			return null;

		final Map<@NotNull K3, @NotNull Map<@NotNull K4, @NotNull V>> map3 = map2.get(key2);
		if (map3 == null)
			return null;

		final Map<@NotNull K4, @NotNull V> map4 = map3.get(key3);
		if (map4 == null)
			return null;

		return map4.get(key4);
	}

	/**
	 * Liefert die Map zum Mapping (key1) oder NULL. <br>
	 *
	 * @param key1 Der 1. Schlüssel.
	 *
	 * @return die Map zum Mapping key1 oder NULL. <br>
	 */
	public Map<@NotNull K2, @NotNull Map<@NotNull K3, @NotNull Map<@NotNull K4, @NotNull V>>> getMap2OrNull(final @NotNull K1 key1) {
		return _map1.get(key1);
	}

	/**
	 * Liefert die Map zum Mapping (key1, key2) oder NULL. <br>
	 *
	 * @param key1 Der 1. Schlüssel des Paares(key1, key2).
	 * @param key2 Der 2. Schlüssel des Paares(key1, key2).
	 *
	 * @return die Map zum Mapping (key1, key2) oder NULL. <br>
	 */
	public Map<@NotNull K3, @NotNull Map<@NotNull K4, @NotNull V>> getMap3OrNull(final @NotNull K1 key1, final @NotNull K2 key2) {
		final Map<@NotNull K2, @NotNull Map<@NotNull K3, @NotNull Map<@NotNull K4, @NotNull V>>> map2 = _map1.get(key1);
		if (map2 == null)
			return null;

		return map2.get(key2);
	}

	/**
	 * Liefert die Map zum Mapping (key1, key2, key3) oder NULL. <br>
	 *
	 * @param key1 Der 1. Schlüssel des Tripels(key1, key2, key3).
	 * @param key2 Der 2. Schlüssel des Tripels(key1, key2, key3).
	 * @param key3 Der 3. Schlüssel des Tripels(key1, key2, key3).
	 *
	 * @return die Map zum Mapping (key1, key2) oder NULL. <br>
	 */
	public Map<@NotNull K4, @NotNull V> getMap4OrNull(final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull K3 key3) {
		final Map<@NotNull K3, @NotNull Map<@NotNull K4, @NotNull V>> map3 = getMap3OrNull(key1, key2);
		if (map3 == null)
			return null;

		return map3.get(key3);
	}

	/**
	 * Liefert den Nicht-Null-Wert zum Mapping (key1, key2, key3, key4).<br>
	 * Wirft eine Exception, falls der Pfad (key1, key2, key3, key4) nicht existiert, oder
	 * NULL zugeordnet ist.
	 *
	 * @param key1 Der 1. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key2 Der 2. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key3 Der 3. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key4 Der 4. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 *
	 * @return den Nicht-Null-Wert zum Mapping (key1, key2, key3).
	 * @throws NullPointerException falls der Pfad (key1, key2, key3) nicht
	 *                              existiert, oder NULL zugeordnet ist.
	 */
	public @NotNull V getNonNullOrException(final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull K3 key3, final @NotNull K4 key4) throws NullPointerException {
		final V value = getOrException(key1, key2, key3, key4);

		if (value == null)
			throw new DeveloperNotificationException("value is NULL!");

		return value;
	}

	/**
	 * Liefert TRUE, falls für das Quadrupel (key1, key2, key3, key4) ein Mapping existiert.
	 *
	 * @param key1 Der 1. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key2 Der 2. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key3 Der 3. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key4 Der 4. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 *
	 * @return TRUE, falls für das Quadrupel (key1, key2, key3, key4) ein Mapping existiert.
	 */
	public boolean contains(final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull K3 key3, final @NotNull K4 key4) {
		final Map<@NotNull K2, @NotNull Map<@NotNull K3, @NotNull Map<@NotNull K4, @NotNull V>>> map2 = _map1.get(key1);
		if (map2 == null)
			return false;

		final Map<@NotNull K3, @NotNull Map<@NotNull K4, @NotNull V>> map3 = map2.get(key2);
		if (map3 == null)
			return false;

		final Map<@NotNull K4, @NotNull V> map4 = map3.get(key3);
		if (map4 == null)
			return false;

		return map4.containsKey(key4);
	}

	/**
	 * Löscht alle Zuordnungen der Map.
	 */
	public void clear() {
		_map1.clear();
	}

	/**
	 * Entfernt das Mapping (key1, key2, key3, key4) falls es existiert<br>
	 * . Wirft eine {@link DeveloperNotificationException}, falls das Mapping nicht
	 * existiert.
	 *
	 * @param key1 Der 1. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key2 Der 2. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key3 Der 3. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key4 Der 4. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 *
	 */
	public void removeOrException(final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull K3 key3, final @NotNull K4 key4) {
		final Map<@NotNull K2, @NotNull Map<@NotNull K3, @NotNull Map<@NotNull K4, @NotNull V>>> map2 = _map1.get(key1);
		if (map2 == null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ") ungültig!");

		final Map<@NotNull K3, @NotNull Map<@NotNull K4, @NotNull V>> map3 = map2.get(key2);
		if (map3 == null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ", " + key2 + ") ungültig!");

		final Map<@NotNull K4, @NotNull V> map4 = map3.get(key3);
		if (map4 == null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ", key2=" + key2 + ", key3=" + key3 + ") ungültig!");

		if (!map4.containsKey(key4))
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ", key2=" + key2 + ", key3=" + key3 + ", key4=" + key4 + ") ungültig!");

		// Mapping-Baum abschneiden.
		map4.remove(key4);
		if (map4.isEmpty()) {
			map3.remove(key3);
			if (map3.isEmpty()) {
				map2.remove(key2);
				if (map2.isEmpty()) {
					_map1.remove(key1);
				}
			}
		}

	}

	/**
	 * Entfernt das Mapping (key1, key2, key3, key4) falls es existiert<br>
	 * .
	 *
	 * @param key1 Der 1. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key2 Der 2. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key3 Der 3. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 * @param key4 Der 4. Schlüssel des Quadrupels(key1, key2, key3, key4).
	 *
	 */
	public void remove(final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull K3 key3, final @NotNull K4 key4) {
		final Map<@NotNull K2, @NotNull Map<@NotNull K3, @NotNull Map<@NotNull K4, @NotNull V>>> map2 = _map1.get(key1);
		if (map2 == null)
			return;

		final Map<@NotNull K3, @NotNull Map<@NotNull K4, @NotNull V>> map3 = map2.get(key2);
		if (map3 == null)
			return;

		final Map<@NotNull K4, @NotNull V> map4 = map3.get(key3);
		if (map4 == null)
			return;

		if (!map4.containsKey(key4))
			return;

		// Mapping-Baum abschneiden.
		map4.remove(key4);
		if (map4.isEmpty()) {
			map3.remove(key3);
			if (map3.isEmpty()) {
				map2.remove(key2);
				if (map2.isEmpty()) {
					_map1.remove(key1);
				}
			}
		}

	}

	/**
	 * Liefert das KeySet des 1. Schlüssels.
	 *
	 * @return das KeySet der SubMap des 1. Schlüssels.
	 */
	public @NotNull Set<@NotNull K1> getKeySet() {
		return this._map1.keySet();
	}

	/**
	 * Liefert eine Liste aller Values in dieser Map.
	 *
	 * @return eine Liste aller Values in dieser Map.
	 */
	public @NotNull List<@NotNull V> getNonNullValuesAsList() {
		@NotNull final ArrayList<@NotNull V> list = new ArrayList<>();

		for (@NotNull final Map<@NotNull K2, @NotNull Map<@NotNull K3, @NotNull Map<@NotNull K4, @NotNull V>>> map2 : _map1.values())
			for (@NotNull final Map<@NotNull K3, @NotNull Map<@NotNull K4, @NotNull V>> map3 : map2.values())
				for (@NotNull final Map<@NotNull K4, @NotNull V> map4 : map3.values())
					for (final @NotNull V value : map4.values())
						list.add(value);

		return list;
	}

	/**
	 * Liefert eine Liste aller Values zum Mapping (key1)
	 *
	 * @param key1 Schlüssel
	 *
	 * @return eine Liste aller Values in dieser Map.
	 */
	public @NotNull List<@NotNull V> getNonNullValuesOfMap2AsList(final @NotNull K1 key1) {
		@NotNull final ArrayList<@NotNull V> list = new ArrayList<>();
		final Map<@NotNull K2, @NotNull Map<@NotNull K3, @NotNull Map<@NotNull K4, @NotNull V>>> map2 = _map1.get(key1);
		if (map2 != null) {
			for (@NotNull final Map<@NotNull K3, @NotNull Map<@NotNull K4, @NotNull V>> map3 : map2.values())
				for (@NotNull final Map<@NotNull K4, @NotNull V> map4 : map3.values())
					for (final @NotNull V value : map4.values())
						list.add(value);
		}

		return list;
	}

	/**
	 * Liefert eine Liste aller Values zum Mapping (key1, key2)
	 *
	 * @param key1 Der 1. Schlüssel des Paares(key1, key2).
	 * @param key2 Der 2. Schlüssel des Paares(key1, key2).
	 *
	 * @return eine Liste aller Values in dieser Map.
	 */
	public @NotNull List<@NotNull V> getNonNullValuesOfMap3AsList(final @NotNull K1 key1, final @NotNull K2 key2) {
		@NotNull final ArrayList<@NotNull V> list = new ArrayList<>();
		final Map<@NotNull K2, @NotNull Map<@NotNull K3, @NotNull Map<@NotNull K4, @NotNull V>>> map2 = _map1.get(key1);
		if (map2 != null) {
			final Map<@NotNull K3, @NotNull Map<@NotNull K4, @NotNull V>> map3 = map2.get(key2);
			if (map3 != null)
				for (@NotNull final Map<@NotNull K4, @NotNull V> map4 : map3.values())
					for (final @NotNull V value : map4.values())
						list.add(value);
		}

		return list;
	}

	/**
	 * Liefert eine Liste aller Values zum Mapping (key1, key2, key3)
	 *
	 * @param key1 Der 1. Schlüssel des Tripels(key1, key2, key3).
	 * @param key2 Der 2. Schlüssel des Tripels(key1, key2, key3).
	 * @param key3 Der 3. Schlüssel des Tripels(key1, key2, key3).
	 *
	 * @return eine Liste aller Values in dieser Map.
	 */
	public @NotNull List<@NotNull V> getNonNullValuesOfMap4AsList(final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull K3 key3) {
		@NotNull final ArrayList<@NotNull V> list = new ArrayList<>();
		final Map<@NotNull K2, @NotNull Map<@NotNull K3, @NotNull Map<@NotNull K4, @NotNull V>>> map2 = _map1.get(key1);
		if (map2 != null) {
			final Map<@NotNull K3, @NotNull Map<@NotNull K4, @NotNull V>> map3 = map2.get(key2);
			if (map3 != null) {
				final Map<@NotNull K4, @NotNull V> map4 = map3.get(key3);
				if (map4 != null)
					for (final @NotNull V value : map4.values())
						list.add(value);
			}
		}

		return list;
	}

}
