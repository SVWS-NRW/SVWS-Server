package de.svws_nrw.core.adt.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import jakarta.validation.constraints.NotNull;

/**
 * Eine 5D-Map ist ein Mapping von vier Schlüsseln auf einen Wert.
 *
 * @param <K1> Der Typ des 1. Schlüsselwertes des Quintupels(key1, key2, key3, key4, key5).
 * @param <K2> Der Typ des 2. Schlüsselwertes des Quintupels(key1, key2, key3, key4, key5).
 * @param <K3> Der Typ des 3. Schlüsselwertes des Quintupels(key1, key2, key3, key4, key5).
 * @param <K4> Der Typ des 4. Schlüsselwertes des Quintupels(key1, key2, key3, key4, key5).
 * @param <K5> Der Typ des 4. Schlüsselwertes des Quintupels(key1, key2, key3, key4, key5).
 *
 * @param <V>  Der Typ des zugeordneten Wertes.
 */
public class HashMap5D<@NotNull K1, @NotNull K2, @NotNull K3, @NotNull K4, @NotNull K5, @NotNull V> {

	private final @NotNull Map<@NotNull K1, @NotNull HashMap4D<@NotNull K2, @NotNull K3, @NotNull K4, @NotNull K5, @NotNull V>> _map = new HashMap<>();

	/**
	 * Konstruktor.
	 */
	public HashMap5D() {
		// no implementation
	}

	/**
	 * Fügt die Zuordnung der Map hinzu. Falls es einen Teil-Pfad von (key1, key2,
	 * key3, key4, key5) nicht gibt, wird er erzeugt.
	 *
	 * @param key1  Der 1. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key2  Der 2. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key3  Der 3. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key4  Der 4. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key5  Der 4. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 *
	 * @param value Der zugeordnete Wert. Der Wert null ist erlaubt.
	 */
	public void put(final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull K3 key3, final @NotNull K4 key4, final @NotNull K5 key5, final @NotNull V value) {
		HashMap4D<@NotNull K2, @NotNull K3, @NotNull K4, @NotNull K5, @NotNull V> map2 = _map.get(key1);
		if (map2 == null) {
			map2 = new HashMap4D<>();
			_map.put(key1, map2);
		}

		map2.put(key2, key3, key4, key5, value);

	}

	/**
	 * Liefert den Wert zum Mapping (key1, key2, key3, key4). <br>
	 * Wirft eine Exception, falls es den Pfad (key1, key2, key3, key4) nicht gibt.
	 *
	 * @param key1 Der 1. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key2 Der 2. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key3 Der 3. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key4 Der 4. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key5 Der 5. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 *
	 * @return Den Wert zum Mapping (key1, key2, key3, key4).
	 *
	 * @throws NullPointerException falls es den Pfad (key1, key2, key3, key4) nicht gibt.
	 */
	public V getOrException(final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull K3 key3, final @NotNull K4 key4, final @NotNull K5 key5) throws NullPointerException {
		final HashMap4D<@NotNull K2, @NotNull K3, @NotNull K4, @NotNull K5, @NotNull V> map2 = _map.get(key1);
		if (map2 == null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ") ungültig!");

		return map2.getOrException(key2, key3, key4, key5);
	}

	/**
	 * Liefert den Wert zum Mapping (key1, key2, key3, key4) oder NULL. <br>
	 *
	 * @param key1 Der 1. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key2 Der 2. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key3 Der 3. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key4 Der 4. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key5 Der 5. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 *
	 * @return den Wert zum Mapping (key1, key2, key3, key4) oder NULL. <br>
	 */
	public V getOrNull(final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull K3 key3, final @NotNull K4 key4, final @NotNull K5 key5) {
		final HashMap4D<@NotNull K2, @NotNull K3, @NotNull K4, @NotNull K5, @NotNull V> map2 = _map.get(key1);
		if (map2 == null)
			return null;

		return map2.getOrNull(key2, key3, key4, key5);
	}

	/**
	 * Liefert die Map zum Mapping (key1) oder NULL. <br>
	 *
	 * @param key1 Der 1. Schlüssel.
	 *
	 * @return die Map zum Mapping key1 oder NULL. <br>
	 */
	public HashMap4D<@NotNull K2, @NotNull K3, @NotNull K4, @NotNull K5, @NotNull V> getMap2OrNull(final @NotNull K1 key1) {
		return _map.get(key1);
	}

	/**
	 * Liefert die Map zum Mapping (key1, key2) oder NULL. <br>
	 *
	 * @param key1 Der 1. Schlüssel des Paares(key1, key2).
	 * @param key2 Der 2. Schlüssel des Paares(key1, key2).
	 *
	 * @return die Map zum Mapping (key1, key2) oder NULL. <br>
	 */
	public Map<@NotNull K3, @NotNull Map<@NotNull K4, @NotNull Map<@NotNull K5, @NotNull V>>> getMap3OrNull(final @NotNull K1 key1, final @NotNull K2 key2) {
		final HashMap4D<@NotNull K2, @NotNull K3, @NotNull K4, @NotNull K5, @NotNull V> map2 = _map.get(key1);
		if (map2 == null)
			return null;

		return map2.getMap2OrNull(key2);
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
	public Map<@NotNull K4, @NotNull Map<@NotNull K5, @NotNull V>> getMap4OrNull(final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull K3 key3) {
		final HashMap4D<@NotNull K2, @NotNull K3, @NotNull K4, @NotNull K5, @NotNull V> map2 = _map.get(key1);
		if (map2 == null)
			return null;

		return map2.getMap3OrNull(key2, key3);
	}

	/**
	 * Liefert die Map zum Mapping (key1, key2, key3) oder NULL. <br>
	 *
	 * @param key1 Der 1. Schlüssel des Tripels(key1, key2, key3).
	 * @param key2 Der 2. Schlüssel des Tripels(key1, key2, key3).
	 * @param key3 Der 3. Schlüssel des Tripels(key1, key2, key3).
	 * @param key4 Der 4. Schlüssel des Tripels(key1, key2, key3).
	 *
	 * @return die Map zum Mapping (key1, key2) oder NULL. <br>
	 */
	public Map<@NotNull K5, @NotNull V> getMap5OrNull(final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull K3 key3, final @NotNull K4 key4) {
		final HashMap4D<@NotNull K2, @NotNull K3, @NotNull K4, @NotNull K5, @NotNull V> map2 = _map.get(key1);
		if (map2 == null)
			return null;

		return map2.getMap4OrNull(key2, key3, key4);
	}

	/**
	 * Liefert den Nicht-Null-Wert zum Mapping (key1, key2, key3, key4).<br>
	 * Wirft eine Exception, falls der Pfad (key1, key2, key3, key4) nicht existiert, oder
	 * NULL zugeordnet ist.
	 *
	 * @param key1 Der 1. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key2 Der 2. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key3 Der 3. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key4 Der 4. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key5 Der 5. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 *
	 * @return den Nicht-Null-Wert zum Mapping (key1, key2, key3).
	 * @throws NullPointerException falls der Pfad (key1, key2, key3) nicht
	 *                              existiert, oder NULL zugeordnet ist.
	 */
	public @NotNull V getNonNullOrException(final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull K3 key3, final @NotNull K4 key4, final @NotNull K5 key5) throws NullPointerException {
		final V value = getOrException(key1, key2, key3, key4, key5);

		if (value == null)
			throw new DeveloperNotificationException("value is NULL!");

		return value;
	}

	/**
	 * Liefert TRUE, falls für das Quadrupel (key1, key2, key3, key4) ein Mapping existiert.
	 *
	 * @param key1 Der 1. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key2 Der 2. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key3 Der 3. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key4 Der 4. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key5 Der 5. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 *
	 * @return TRUE, falls für das Quadrupel (key1, key2, key3, key4) ein Mapping existiert.
	 */
	public boolean contains(final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull K3 key3, final @NotNull K4 key4, final @NotNull K5 key5) {
		final HashMap4D<@NotNull K2, @NotNull K3, @NotNull K4, @NotNull K5, @NotNull V> map2 = _map.get(key1);
		if (map2 == null)
			return false;

		return map2.contains(key2, key3, key4, key5);
	}

	/**
	 * Löscht alle Zuordnungen der Map.
	 */
	public void clear() {
		_map.clear();
	}

	/**
	 * Entfernt das Mapping (key1, key2, key3, key4) falls es existiert<br>
	 * . Wirft eine {@link DeveloperNotificationException}, falls das Mapping nicht
	 * existiert.
	 *
	 * @param key1 Der 1. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key2 Der 2. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key3 Der 3. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key4 Der 4. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key5 Der 5. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 *
	 */
	public void removeOrException(final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull K3 key3, final @NotNull K4 key4, final @NotNull K5 key5) {
		final HashMap4D<@NotNull K2, @NotNull K3, @NotNull K4, @NotNull K5, @NotNull V> map2 = _map.get(key1);
		if (map2 == null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ") ungültig!");

		map2.removeOrException(key2, key3, key4, key5);
	}

	/**
	 * Entfernt das Mapping (key1, key2, key3, key4) falls es existiert<br>
	 * .
	 *
	 * @param key1 Der 1. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key2 Der 2. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key3 Der 3. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key4 Der 4. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 * @param key5 Der 5. Schlüssel des Quintupels(key1, key2, key3, key4, key5).
	 *
	 */
	public void remove(final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull K3 key3, final @NotNull K4 key4, final @NotNull K5 key5) {
		final HashMap4D<@NotNull K2, @NotNull K3, @NotNull K4, @NotNull K5, @NotNull V> map2 = _map.get(key1);
		if (map2 == null)
			return;

		map2.remove(key2, key3, key4, key5);
	}

	/**
	 * Liefert das KeySet des 1. Schlüssels.
	 *
	 * @return das KeySet der SubMap des 1. Schlüssels.
	 */
	public @NotNull Set<@NotNull K1> getKeySet() {
		return this._map.keySet();
	}

	/**
	 * Liefert eine Liste aller Values in dieser Map.
	 *
	 * @return eine Liste aller Values in dieser Map.
	 */
	public @NotNull List<@NotNull V> getNonNullValuesAsList() {
		final @NotNull ArrayList<@NotNull V> list = new ArrayList<>();

		for (final HashMap4D<@NotNull K2, @NotNull K3, @NotNull K4, @NotNull K5, @NotNull V> map2 : _map.values())
			list.addAll(map2.getNonNullValuesAsList());

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
		final @NotNull ArrayList<@NotNull V> list = new ArrayList<>();

		final HashMap4D<@NotNull K2, @NotNull K3, @NotNull K4, @NotNull K5, @NotNull V> map2 = _map.get(key1);
		if (map2 != null)
			list.addAll(map2.getNonNullValuesAsList());

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
		final @NotNull ArrayList<@NotNull V> list = new ArrayList<>();

		final HashMap4D<@NotNull K2, @NotNull K3, @NotNull K4, @NotNull K5, @NotNull V> map2 = _map.get(key1);
		if (map2 != null)
			list.addAll(map2.getNonNullValuesOfMap2AsList(key2));

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
		final @NotNull ArrayList<@NotNull V> list = new ArrayList<>();

		final HashMap4D<@NotNull K2, @NotNull K3, @NotNull K4, @NotNull K5, @NotNull V> map2 = _map.get(key1);
		if (map2 != null)
			list.addAll(map2.getNonNullValuesOfMap3AsList(key2, key3));

		return list;
	}

	/**
	 * Liefert eine Liste aller Values zum Mapping (key1, key2, key3)
	 *
	 * @param key1 Der 1. Schlüssel des Tripels(key1, key2, key3).
	 * @param key2 Der 2. Schlüssel des Tripels(key1, key2, key3).
	 * @param key3 Der 3. Schlüssel des Tripels(key1, key2, key3).
	 * @param key4 Der 4. Schlüssel des Tripels(key1, key2, key3).
	 *
	 * @return eine Liste aller Values in dieser Map.
	 */
	public @NotNull List<@NotNull V> getNonNullValuesOfMap5AsList(final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull K3 key3, final @NotNull K4 key4) {
		final @NotNull ArrayList<@NotNull V> list = new ArrayList<>();

		final HashMap4D<@NotNull K2, @NotNull K3, @NotNull K4, @NotNull K5, @NotNull V> map2 = _map.get(key1);
		if (map2 != null)
			list.addAll(map2.getNonNullValuesOfMap4AsList(key2, key3, key4));

		return list;
	}

}
