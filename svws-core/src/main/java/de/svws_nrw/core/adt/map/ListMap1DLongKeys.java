package de.svws_nrw.core.adt.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.utils.MapUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Diese 1D-List-Map ordnet einen Schlüssel auf eine Liste von Werten (V) ab.
 * <br> Die Einfüge-Reihenfolge bleibt bei allen Listen erhalten.
 * <br> Ein Entfernen aus der Datenstruktur ist im Allgemeinen nicht sinnvoll, da jeder Löschvorgang O(n) Laufzeit hat.
 *
 * @param <V> Der Typ der zugeordneten Werte.
 */
public class ListMap1DLongKeys<V> {

	private final @NotNull Map<Long, List<V>> _map = new HashMap<>();

	/**
	 * Konstruktor.
	 */
	public ListMap1DLongKeys() {
		// leer
	}

	/**
	 * Fügt das Element hinzu.
	 *
	 * @param key   Der Schlüssel.
	 * @param value Der zugeordnete Wert. Der Wert NULL ist nicht erlaubt.
	 */
	public void add(final long key, final @NotNull V value) {
		MapUtils.getOrCreateArrayList(_map, key).add(value);
	}

	/**
	 * Fügt das Element hinzu. Wirft eine Exception, falls es schon ein Element mit diesem Schlüssel gibt.
	 *
	 * @param key   Der Schlüssel.
	 * @param value Der zugeordnete Wert. Der Wert NULL ist nicht erlaubt.
	 */
	public void addSingle(final long key, final @NotNull V value) {
		if (!get(key).isEmpty()) {
			throw new DeveloperNotificationException("Es gibt schon ein Element mit (%d).".formatted(key));
		}
		add(key, value);
	}

	/**
	 * Erzeugt den Pfad (key) fügt aber nichts hinzu.
	 * Falls der Pfad vorher nicht existierte, verweist er dann auf eine leere Liste.
	 *
	 * @param key   Der Schlüssel.
	 */
	public void addEmpty(final long key) {
		MapUtils.getOrCreateArrayList(_map, key);
	}

	/**
	 * Entfernt den Wert aus der zum Schlüssel (key) gehörenden Value-Liste.
	 * Falls es den Pfad (key) nicht gibt oder der Wert nicht enthalten ist, wird eine Exception geworfen.
	 *
	 * @param key   Der Schlüssel.
	 * @param value Der zu entfernende Wert.
	 */
	public void removeValueOrException(final long key, final @NotNull V value) {
		MapUtils.removeFromListAndTrimOrException(_map, key, value);
	}

	/**
	 * Entfernt den Pfad (key) aus der Map.
	 *
	 * @param key   Der Schlüssel.
	 *
	 * @return der bisherige Wert zu (key) oder {@code null}, falls nicht existent.
	 */
	public List<V> remove(final long key) {
		return _map.remove(key);
	}

	/**
	 * Entfernt den Pfad (key) aus der Map.
	 * Falls es den Pfad (key) nicht gibt, wird eine Exception geworfen.
	 *
	 * @param key   Der Schlüssel.
	 *
	 * @return der bisherige Wert zu (key)
	 */
	public @NotNull List<V> removeOrException(final long key) {
		return DeveloperNotificationException.ifNull("Pfad (" + key + ") existiert nicht!", remove(key));
	}

	/**
	 * Entfernt den Pfad (key) aus der Map.
	 * Wirft eine DeveloperNotificationException, falls in der gemappten Liste das Element nicht als einziges enthalten ist.
	 * Falls es den Pfad (key) nicht gibt, wird eine Exception geworfen.
	 *
	 * @param key   Der Schlüssel.
	 *
	 * @return der bisherige Wert zu (key)
	 */
	public @NotNull V removeSingleOrException(final long key) {
		final List<V> values = removeOrException(key);
		DeveloperNotificationException.ifTrue(
			"Pfad (" + key + ") enthält nicht genau ein Element (tatsächlich "
			+ values.size() + ")!", values.size() != 1
		);
		return values.getFirst();
	}

	/**
	 * Gibt eine flache Liste aller Values in dieser 1D-ListMap zurück.
	 * Die Einfüge-Reihenfolge der einzelnen Listen bleibt erhalten.
	 *
	 * @return eine flache Liste aller enthaltenen Werte
	 */
	public @NotNull List<V> getAllValues() {
		final List<V> result = new ArrayList<>();
		for (final List<V> values : _map.values()) {
			result.addAll(values);
		}
		return result;
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key) gibt.
	 *
	 * @param key   Der Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key) gibt.
	 */
	public boolean containsKey(final long key) {
		return _map.containsKey(key);
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key).
	 *
	 * @param key   Der Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get(final long key) {
		final List<V> list = _map.get(key);
		if (list == null) {
			return new ArrayList<>();
		}
		return new ArrayList<>(list);
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key   Der Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingleOrNull(final long key) {
		final List<V> list = _map.get(key);
		if (list == null) {
			return null;
		}
		if (list.size() != 1) {
			return null;
		}
		return list.getFirst();
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key   Der Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public @NotNull V getSingleOrException(final long key) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingleOrNull(key));
	}

	/**
	 * Liefert das Key-Set der Map.
	 *
	 * @return das Key-Set der Map.
	 */
	public @NotNull Set<Long> keySet() {
		return _map.keySet();
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key).
	 *
	 * @param key   Der Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public @NotNull List<V> getOrException(final long key) {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !containsKey(key));
		return get(key);
	}

}
