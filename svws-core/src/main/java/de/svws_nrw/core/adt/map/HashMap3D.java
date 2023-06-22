package de.svws_nrw.core.adt.map;

import java.util.HashMap;
import java.util.Map;

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
public class HashMap3D<@NotNull K1, @NotNull K2, @NotNull K3, @NotNull V> {

	private final @NotNull Map<@NotNull K1, @NotNull Map<@NotNull K2, @NotNull Map<@NotNull K3, V>>> _map1 = new HashMap<>();

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
	public void put(final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull K3 key3, final V value) {
		Map<@NotNull K2, @NotNull Map<@NotNull K3, V>> map2 = _map1.get(key1);
		if (map2 == null) {
			map2 = new HashMap<>();
			_map1.put(key1, map2);
		}

		Map<@NotNull K3, V> map3 = map2.get(key2);
		if (map3 == null) {
			map3 = new HashMap<>();
			map2.put(key2, map3);
		}

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
	 * @throws NullPointerException falls es den Pfad (key1, key2, key3) nicht gibt.
	 */
	public V getOrException(final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull K3 key3) throws NullPointerException {
		final Map<@NotNull K2, @NotNull Map<@NotNull K3, V>> map2 = _map1.get(key1);
		if (map2 == null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ") ungültig!");

		final Map<@NotNull K3, V> map3 = map2.get(key2);
		if (map3 == null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ", " + key2 + ") ungültig!");

		if (!map3.containsKey(key3))
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ", key2=" + key2 + ", key3=" + key3 + ") ungültig!");

		return map3.get(key3);
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
	 * Liefert TRUE, falls für das Tripel (key1, key2, key3) ein Mapping existiert.
	 *
	 * @param key1  Der 1. Schlüssel des Tripels(key1, key2, key3).
	 * @param key2  Der 2. Schlüssel des Tripels(key1, key2, key3).
	 * @param key3  Der 3. Schlüssel des Tripels(key1, key2, key3).
	 *
	 * @return TRUE, falls für das Tripel (key1, key2, key3) ein Mapping existiert.
	 */
	public boolean contains(final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull K3 key3) {
		final Map<@NotNull K2, @NotNull Map<@NotNull K3, V>> map2 = _map1.get(key1);
		if (map2 == null)
			return false;

		final Map<@NotNull K3, V> map3 = map2.get(key2);
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
		final Map<@NotNull K2, @NotNull Map<@NotNull K3, V>> map2 = _map1.get(key1);
		if (map2 == null)
			throw new DeveloperNotificationException("Pfad (key1=" + key1 + ") ungültig!");

		final Map<@NotNull K3, V> map3 = map2.get(key2);
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

}
