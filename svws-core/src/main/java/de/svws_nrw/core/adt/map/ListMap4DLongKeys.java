package de.svws_nrw.core.adt.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.svws_nrw.core.adt.LongArrayKey;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.utils.MapUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Diese 4D-List-Map ordnet 4 Schlüssel auf eine Liste von Werten (V) ab.
 * <br> Diese spezielle Map stellt Zugriffsmethoden für alle Kombinationen der Schlüssel auf die Werte (V) zur Verfügung.
 * <br> Die Einfüge-Reihenfolge bleibt bei allen Listen erhalten.
 * <br> Ein Entfernen aus der Datenstruktur ist im Allgemeinen nicht sinnvoll, da jeder Löschvorgang O(n) Laufzeit hat und zusätzlich bei den folgenden Zugriffen alle Cache-Maps neu aufgebaut werden müssen.
 *
 * @param <V> Der Typ der zugeordneten Werte.
 */
public class ListMap4DLongKeys<V> {

	private Map<Long, List<V>> _map1 = null;
	private Map<Long, List<V>> _map2 = null;
	private Map<Long, List<V>> _map3 = null;
	private Map<Long, List<V>> _map4 = null;

	private Map<LongArrayKey, List<V>> _map12 = null;
	private Map<LongArrayKey, List<V>> _map13 = null;
	private Map<LongArrayKey, List<V>> _map14 = null;
	private Map<LongArrayKey, List<V>> _map23 = null;
	private Map<LongArrayKey, List<V>> _map24 = null;
	private Map<LongArrayKey, List<V>> _map34 = null;

	private Map<LongArrayKey, List<V>> _map123 = null;
	private Map<LongArrayKey, List<V>> _map124 = null;
	private Map<LongArrayKey, List<V>> _map134 = null;
	private Map<LongArrayKey, List<V>> _map234 = null;

	private final @NotNull Map<LongArrayKey, List<V>> _map1234 = new HashMap<>();

	/**
	 * Konstruktor.
	 */
	public ListMap4DLongKeys() {
		// leer
	}

	private @NotNull Map<Long, List<V>> _lazyLoad1() {
		final Map<Long, List<V>> map = new HashMap<>();

		for (final @NotNull Entry<LongArrayKey, List<V>> e : _map1234.entrySet()) {
			final long key1 = e.getKey().getKeyAt(0);
			if (e.getValue().isEmpty()) {
				MapUtils.getOrCreateArrayList(map, key1);
			} else {
				MapUtils.getOrCreateArrayList(map, key1).addAll(e.getValue());
			}
		}

		return map;
	}

	private @NotNull Map<Long, List<V>> _lazyLoad2() {
		final Map<Long, List<V>> map = new HashMap<>();

		for (final @NotNull Entry<LongArrayKey, List<V>> e : _map1234.entrySet()) {
			final long key2 = e.getKey().getKeyAt(1);
			if (e.getValue().isEmpty()) {
				MapUtils.getOrCreateArrayList(map, key2);
			} else {
				MapUtils.getOrCreateArrayList(map, key2).addAll(e.getValue());
			}
		}

		return map;
	}

	private @NotNull Map<Long, List<V>> _lazyLoad3() {
		final Map<Long, List<V>> map = new HashMap<>();

		for (final @NotNull Entry<LongArrayKey, List<V>> e : _map1234.entrySet()) {
			final long key3 = e.getKey().getKeyAt(2);
			if (e.getValue().isEmpty()) {
				MapUtils.getOrCreateArrayList(map, key3);
			} else {
				MapUtils.getOrCreateArrayList(map, key3).addAll(e.getValue());
			}
		}

		return map;
	}

	private @NotNull Map<Long, List<V>> _lazyLoad4() {
		final Map<Long, List<V>> map = new HashMap<>();

		for (final @NotNull Entry<LongArrayKey, List<V>> e : _map1234.entrySet()) {
			final long key4 = e.getKey().getKeyAt(3);
			if (e.getValue().isEmpty()) {
				MapUtils.getOrCreateArrayList(map, key4);
			} else {
				MapUtils.getOrCreateArrayList(map, key4).addAll(e.getValue());
			}
		}

		return map;
	}

	private @NotNull Map<LongArrayKey, List<V>> _lazyLoad12() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Entry<LongArrayKey, List<V>> e : _map1234.entrySet()) {
			final long key1 = e.getKey().getKeyAt(0);
			final long key2 = e.getKey().getKeyAt(1);
			if (e.getValue().isEmpty()) {
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2));
			} else {
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2)).addAll(e.getValue());
			}
		}

		return map;
	}

	private @NotNull Map<LongArrayKey, List<V>> _lazyLoad13() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Entry<LongArrayKey, List<V>> e : _map1234.entrySet()) {
			final long key1 = e.getKey().getKeyAt(0);
			final long key3 = e.getKey().getKeyAt(2);
			if (e.getValue().isEmpty()) {
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key3));
			} else {
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key3)).addAll(e.getValue());
			}
		}

		return map;
	}

	private @NotNull Map<LongArrayKey, List<V>> _lazyLoad14() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Entry<LongArrayKey, List<V>> e : _map1234.entrySet()) {
			final long key1 = e.getKey().getKeyAt(0);
			final long key4 = e.getKey().getKeyAt(3);
			if (e.getValue().isEmpty()) {
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key4));
			} else {
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key4)).addAll(e.getValue());
			}
		}

		return map;
	}

	private @NotNull Map<LongArrayKey, List<V>> _lazyLoad23() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Entry<LongArrayKey, List<V>> e : _map1234.entrySet()) {
			final long key2 = e.getKey().getKeyAt(1);
			final long key3 = e.getKey().getKeyAt(2);
			if (e.getValue().isEmpty()) {
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key2, key3));
			} else {
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key2, key3)).addAll(e.getValue());
			}
		}

		return map;
	}

	private @NotNull Map<LongArrayKey, List<V>> _lazyLoad24() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Entry<LongArrayKey, List<V>> e : _map1234.entrySet()) {
			final long key2 = e.getKey().getKeyAt(1);
			final long key4 = e.getKey().getKeyAt(3);
			if (e.getValue().isEmpty()) {
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key2, key4));
			} else {
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key2, key4)).addAll(e.getValue());
			}
		}

		return map;
	}

	private @NotNull Map<LongArrayKey, List<V>> _lazyLoad34() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Entry<LongArrayKey, List<V>> e : _map1234.entrySet()) {
			final long key3 = e.getKey().getKeyAt(2);
			final long key4 = e.getKey().getKeyAt(3);
			if (e.getValue().isEmpty()) {
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key3, key4));
			} else {
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key3, key4)).addAll(e.getValue());
			}
		}

		return map;
	}

	private @NotNull Map<LongArrayKey, List<V>> _lazyLoad123() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Entry<LongArrayKey, List<V>> e : _map1234.entrySet()) {
			final long key1 = e.getKey().getKeyAt(0);
			final long key2 = e.getKey().getKeyAt(1);
			final long key3 = e.getKey().getKeyAt(2);
			if (e.getValue().isEmpty()) {
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2, key3));
			} else {
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2, key3)).addAll(e.getValue());
			}
		}

		return map;
	}

	private @NotNull Map<LongArrayKey, List<V>> _lazyLoad124() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Entry<LongArrayKey, List<V>> e : _map1234.entrySet()) {
			final long key1 = e.getKey().getKeyAt(0);
			final long key2 = e.getKey().getKeyAt(1);
			final long key4 = e.getKey().getKeyAt(3);
			if (e.getValue().isEmpty()) {
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2, key4));
			} else {
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2, key4)).addAll(e.getValue());
			}
		}

		return map;
	}

	private @NotNull Map<LongArrayKey, List<V>> _lazyLoad134() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Entry<LongArrayKey, List<V>> e : _map1234.entrySet()) {
			final long key1 = e.getKey().getKeyAt(0);
			final long key3 = e.getKey().getKeyAt(2);
			final long key4 = e.getKey().getKeyAt(3);
			if (e.getValue().isEmpty()) {
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key3, key4));
			} else {
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key3, key4)).addAll(e.getValue());
			}
		}

		return map;
	}

	private @NotNull Map<LongArrayKey, List<V>> _lazyLoad234() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Entry<LongArrayKey, List<V>> e : _map1234.entrySet()) {
			final long key2 = e.getKey().getKeyAt(1);
			final long key3 = e.getKey().getKeyAt(2);
			final long key4 = e.getKey().getKeyAt(3);
			if (e.getValue().isEmpty()) {
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key2, key3, key4));
			} else {
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key2, key3, key4)).addAll(e.getValue());
			}
		}

		return map;
	}

	private V getSingleOrNullHelperLong(final @NotNull Map<Long, List<V>> map, final long key) {
		final List<V> list = map.get(key);
		if (list == null) {
			return null;
		}
		if (list.size() != 1) {
			return null;
		}
		return list.getFirst();
	}

	private V getSingleOrNullHelperLongArray(final @NotNull Map<LongArrayKey, List<V>> map, final @NotNull LongArrayKey key) {
		final List<V> list = map.get(key);
		if (list == null) {
			return null;
		}
		if (list.size() != 1) {
			return null;
		}
		return list.getFirst();
	}

	/**
	 * Fügt das Element hinzu.
	 *
	 * @param key1  Der 1. Schlüssel.
	 * @param key2  Der 2. Schlüssel.
	 * @param key3  Der 3. Schlüssel.
	 * @param key4  Der 4. Schlüssel.
	 *
	 * @param value Der zugeordnete Wert. Der Wert NULL ist nicht erlaubt.
	 */
	public void add(final long key1, final long key2, final long key3, final long key4, final @NotNull V value) {
		final @NotNull LongArrayKey key = new LongArrayKey(key1, key2, key3, key4);
		MapUtils.getOrCreateArrayList(_map1234, key).add(value);

		if (_map1 != null) {
			MapUtils.getOrCreateArrayList(_map1, key1).add(value);
		}
		if (_map2 != null) {
			MapUtils.getOrCreateArrayList(_map2, key2).add(value);
		}
		if (_map3 != null) {
			MapUtils.getOrCreateArrayList(_map3, key3).add(value);
		}
		if (_map4 != null) {
			MapUtils.getOrCreateArrayList(_map4, key4).add(value);
		}

		if (_map12 != null) {
			MapUtils.getOrCreateArrayList(_map12, new LongArrayKey(key1, key2)).add(value);
		}
		if (_map13 != null) {
			MapUtils.getOrCreateArrayList(_map13, new LongArrayKey(key1, key3)).add(value);
		}
		if (_map14 != null) {
			MapUtils.getOrCreateArrayList(_map14, new LongArrayKey(key1, key4)).add(value);
		}
		if (_map23 != null) {
			MapUtils.getOrCreateArrayList(_map23, new LongArrayKey(key2, key3)).add(value);
		}
		if (_map24 != null) {
			MapUtils.getOrCreateArrayList(_map24, new LongArrayKey(key2, key4)).add(value);
		}
		if (_map34 != null) {
			MapUtils.getOrCreateArrayList(_map34, new LongArrayKey(key3, key4)).add(value);
		}

		if (_map123 != null) {
			MapUtils.getOrCreateArrayList(_map123, new LongArrayKey(key1, key2, key3)).add(value);
		}
		if (_map124 != null) {
			MapUtils.getOrCreateArrayList(_map124, new LongArrayKey(key1, key2, key4)).add(value);
		}
		if (_map134 != null) {
			MapUtils.getOrCreateArrayList(_map134, new LongArrayKey(key1, key3, key4)).add(value);
		}
		if (_map234 != null) {
			MapUtils.getOrCreateArrayList(_map234, new LongArrayKey(key2, key3, key4)).add(value);
		}
	}

	/**
	 * Fügt das Element hinzu. Wirft eine Exception, falls es schon ein Element mit diesem Schlüssel gibt.
	 *
	 * @param key1  Der 1. Schlüssel.
	 * @param key2  Der 2. Schlüssel.
	 * @param key3  Der 3. Schlüssel.
	 * @param key4  Der 4. Schlüssel.
	 *
	 * @param value Der zugeordnete Wert. Der Wert NULL ist nicht erlaubt.
	 */
	public void addSingle(final long key1, final long key2, final long key3, final long key4, final @NotNull V value) {
		if (!get1234(key1, key2, key3, key4).isEmpty()) {
			throw new DeveloperNotificationException("Es gibt schon ein Element mit (%d, %d, %d, %d).".formatted(key1, key2, key3, key4));
		}
		add(key1, key2, key3, key4, value);
	}

	/**
	 * Erzeugt den Pfad (key1, key2, key3, key4) fügt aber nichts hinzu.
	 * Alle Pfad, die es vorher nicht gab, verweisen dann auf leere Listen.
	 *
	 * @param key1  Der 1. Schlüssel.
	 * @param key2  Der 2. Schlüssel.
	 * @param key3  Der 3. Schlüssel.
	 * @param key4  Der 4. Schlüssel.
	 */
	public void addEmpty(final long key1, final long key2, final long key3, final long key4) {
		final @NotNull LongArrayKey key = new LongArrayKey(key1, key2, key3, key4);
		MapUtils.getOrCreateArrayList(_map1234, key);

		if (_map1 != null) {
			MapUtils.getOrCreateArrayList(_map1, key1);
		}
		if (_map2 != null) {
			MapUtils.getOrCreateArrayList(_map2, key2);
		}
		if (_map3 != null) {
			MapUtils.getOrCreateArrayList(_map3, key3);
		}
		if (_map4 != null) {
			MapUtils.getOrCreateArrayList(_map4, key4);
		}

		if (_map12 != null) {
			MapUtils.getOrCreateArrayList(_map12, new LongArrayKey(key1, key2));
		}
		if (_map13 != null) {
			MapUtils.getOrCreateArrayList(_map13, new LongArrayKey(key1, key3));
		}
		if (_map14 != null) {
			MapUtils.getOrCreateArrayList(_map14, new LongArrayKey(key1, key4));
		}
		if (_map23 != null) {
			MapUtils.getOrCreateArrayList(_map23, new LongArrayKey(key2, key3));
		}
		if (_map24 != null) {
			MapUtils.getOrCreateArrayList(_map24, new LongArrayKey(key2, key4));
		}
		if (_map34 != null) {
			MapUtils.getOrCreateArrayList(_map34, new LongArrayKey(key3, key4));
		}

		if (_map123 != null) {
			MapUtils.getOrCreateArrayList(_map123, new LongArrayKey(key1, key2, key3));
		}
		if (_map124 != null) {
			MapUtils.getOrCreateArrayList(_map124, new LongArrayKey(key1, key2, key4));
		}
		if (_map134 != null) {
			MapUtils.getOrCreateArrayList(_map134, new LongArrayKey(key1, key3, key4));
		}
		if (_map234 != null) {
			MapUtils.getOrCreateArrayList(_map234, new LongArrayKey(key2, key3, key4));
		}
	}

	private void invalidateCaches() {
		_map1 = null;
		_map2 = null;
		_map3 = null;
		_map4 = null;
		_map12 = null;
		_map13 = null;
		_map14 = null;
		_map23 = null;
		_map24 = null;
		_map34 = null;
		_map123 = null;
		_map124 = null;
		_map134 = null;
		_map234 = null;
	}

	/**
	 * Entfernt den Wert aus der zur Zuordnung (key1, key2, key3, key4) gehörenden Value-Liste.
	 * Falls es den Pfad nicht gibt, wird eine Exception geworfen.
	 * Alle Caches der Map werden gelöscht und müssen beim ersten Zugriff neu aufgebaut werden.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param value  Der zu entfernende Wert.
	 */
	public void removeValueOrException(final long key1, final long key2, final long key3, final long key4, final @NotNull V value) {
		final @NotNull LongArrayKey key = new LongArrayKey(key1, key2, key3, key4);
		MapUtils.removeFromListAndTrimOrException(_map1234, key, value);
		invalidateCaches();
	}

	/**
	 * Entfernt den Pfad (key1, key2, key3, key4) aus der Map.
	 * Alle Caches der Map werden gelöscht und müssen beim ersten Zugriff neu aufgebaut werden.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return der bisherige Wert zu (key1, key2, key3, key4) oder {@code null}, falls nicht existent.
	 */
	public List<V> remove(final long key1, final long key2, final long key3, final long key4) {
		final @NotNull LongArrayKey key = new LongArrayKey(key1, key2, key3, key4);
		final List<V> values = _map1234.remove(key);
		if (values != null) {
			invalidateCaches();
		}
		return values;
	}

	/**
	 * Entfernt den Pfad (key1, key2, key3, key4) aus der Map.
	 * Falls es den Pfad nicht gibt, wird eine Exception geworfen.
	 * Alle Caches der Map werden gelöscht und müssen beim ersten Zugriff neu aufgebaut werden.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return der bisherige Wert zu (key1, key2, key3, key4)
	 */
	public @NotNull List<V> removeOrException(final long key1, final long key2, final long key3, final long key4) {
		return DeveloperNotificationException.ifNull("Pfad (" + key1 + ", " + key2 + ", " + key3 + ", " + key4 + ") existiert nicht!", remove(key1, key2, key3, key4));
	}

	/**
	 * Entfernt den Pfad (key1, key2, key3, key4) aus der Map.
	 * Wirft eine DeveloperNotificationException, falls in der gemappten Liste das Element nicht als einziges enthalten ist.
	 * Falls es den Pfad nicht gibt, wird eine Exception geworfen.
	 * Alle Caches der Map werden gelöscht und müssen beim ersten Zugriff neu aufgebaut werden.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return der bisherige Wert zu (key1, key2, key3, key4)
	 */
	public @NotNull V removeSingleOrException(final long key1, final long key2, final long key3, final long key4) {
		final List<V> values = removeOrException(key1, key2, key3, key4);
		DeveloperNotificationException.ifTrue(
			"Pfad (" + key1 + ", " + key2 + ", " + key3 + ", " + key4 + ") enthält nicht genau ein Element (tatsächlich "
			+ values.size() + ")!", values.size() != 1
		);
		return values.getFirst();
	}

	/**
	 * Entfernt alle Einträge, bei denen der erste Schlüssel (key1) übereinstimmt.
	 * Falls kein Eintrag zu key1 existiert, passiert nichts.
	 * Alle Caches der Map werden gelöscht und müssen beim ersten Zugriff neu aufgebaut werden.
	 *
	 * @param key1   Der 1. Schlüssel.
	 */
	public void removeAllByKey1(final long key1) {
		removeAllByKeyX(key1, 0);
	}

	/**
	 * Entfernt alle Einträge, bei denen der zweite Schlüssel (key2) übereinstimmt.
	 * Falls kein Eintrag zu key2 existiert, passiert nichts.
	 * Alle Caches der Map werden gelöscht und müssen beim ersten Zugriff neu aufgebaut werden.
	 *
	 * @param key2   Der 2. Schlüssel.
	 */
	public void removeAllByKey2(final long key2) {
		removeAllByKeyX(key2, 1);
	}

	/**
	 * Entfernt alle Einträge, bei denen der dritte Schlüssel (key3) übereinstimmt.
	 * Falls kein Eintrag zu key3 existiert, passiert nichts.
	 * Alle Caches der Map werden gelöscht und müssen beim ersten Zugriff neu aufgebaut werden.
	 *
	 * @param key3   Der 3. Schlüssel.
	 */
	public void removeAllByKey3(final long key3) {
		removeAllByKeyX(key3, 2);
	}

	/**
	 * Entfernt alle Einträge, bei denen der vierte Schlüssel (key4) übereinstimmt.
	 * Falls kein Eintrag zu key4 existiert, passiert nichts.
	 * Alle Caches der Map werden gelöscht und müssen beim ersten Zugriff neu aufgebaut werden.
	 *
	 * @param key4   Der 4. Schlüssel.
	 */
	public void removeAllByKey4(final long key4) {
		removeAllByKeyX(key4, 3);
	}

	private void removeAllByKeyX(final long key, final int x) {
		final List<LongArrayKey> toRemove = new ArrayList<>();
		for (final LongArrayKey keyEntry : _map1234.keySet()) {
			if (keyEntry.getKeyAt(x) == key) {
				toRemove.add(keyEntry);
			}
		}
		for (final LongArrayKey keyEntry : toRemove) {
			_map1234.remove(keyEntry);
		}

		if (!toRemove.isEmpty()) {
			invalidateCaches();
		}
	}

	/**
	 * Gibt eine flache Liste aller Values in dieser 4D-ListMap zurück.
	 * Die Einfüge-Reihenfolge der einzelnen Listen bleibt erhalten.
	 *
	 * @return eine flache Liste aller enthaltenen Werte
	 */
	public @NotNull List<V> getAllValues() {
		final List<V> result = new ArrayList<>();
		for (final List<V> values : _map1234.values()) {
			result.addAll(values);
		}
		return result;
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key1) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key1) gibt.
	 */
	public boolean containsKey1(final long key1) {
		if (_map1 == null) {
			_map1 = _lazyLoad1();
		}
		return _map1.containsKey(key1);
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key2) gibt.
	 *
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key2) gibt.
	 */
	public boolean containsKey2(final long key2) {
		if (_map2 == null) {
			_map2 = _lazyLoad2();
		}
		return _map2.containsKey(key2);
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key3) gibt.
	 *
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key3) gibt.
	 */
	public boolean containsKey3(final long key3) {
		if (_map3 == null) {
			_map3 = _lazyLoad3();
		}
		return _map3.containsKey(key3);
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key4) gibt.
	 *
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key4) gibt.
	 */
	public boolean containsKey4(final long key4) {
		if (_map4 == null) {
			_map4 = _lazyLoad4();
		}
		return _map4.containsKey(key4);
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key1, key2) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key1, key2) gibt.
	 */
	public boolean containsKey12(final long key1, final long key2) {
		if (_map12 == null) {
			_map12 = _lazyLoad12();
		}
		return _map12.containsKey(new LongArrayKey(key1, key2));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key1, key3) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key1, key3) gibt.
	 */
	public boolean containsKey13(final long key1, final long key3) {
		if (_map13 == null) {
			_map13 = _lazyLoad13();
		}
		return _map13.containsKey(new LongArrayKey(key1, key3));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key1, key4) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key1, key4) gibt.
	 */
	public boolean containsKey14(final long key1, final long key4) {
		if (_map14 == null) {
			_map14 = _lazyLoad14();
		}
		return _map14.containsKey(new LongArrayKey(key1, key4));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key2, key3) gibt.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key2, key3) gibt.
	 */
	public boolean containsKey23(final long key2, final long key3) {
		if (_map23 == null) {
			_map23 = _lazyLoad23();
		}
		return _map23.containsKey(new LongArrayKey(key2, key3));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key2, key4) gibt.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key2, key4) gibt.
	 */
	public boolean containsKey24(final long key2, final long key4) {
		if (_map24 == null) {
			_map24 = _lazyLoad24();
		}
		return _map24.containsKey(new LongArrayKey(key2, key4));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key3, key4) gibt.
	 *
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key3, key4) gibt.
	 */
	public boolean containsKey34(final long key3, final long key4) {
		if (_map34 == null) {
			_map34 = _lazyLoad34();
		}
		return _map34.containsKey(new LongArrayKey(key3, key4));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key1, key2, key3) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key1, key2, key3) gibt.
	 */
	public boolean containsKey123(final long key1, final long key2, final long key3) {
		if (_map123 == null) {
			_map123 = _lazyLoad123();
		}
		return _map123.containsKey(new LongArrayKey(key1, key2, key3));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key1, key2, key4) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key1, key2, key4) gibt.
	 */
	public boolean containsKey124(final long key1, final long key2, final long key4) {
		if (_map124 == null) {
			_map124 = _lazyLoad124();
		}
		return _map124.containsKey(new LongArrayKey(key1, key2, key4));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key1, key3, key4) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key1, key3, key4) gibt.
	 */
	public boolean containsKey134(final long key1, final long key3, final long key4) {
		if (_map134 == null) {
			_map134 = _lazyLoad134();
		}
		return _map134.containsKey(new LongArrayKey(key1, key3, key4));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key2, key3, key4) gibt.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key2, key3, key4) gibt.
	 */
	public boolean containsKey234(final long key2, final long key3, final long key4) {
		if (_map234 == null) {
			_map234 = _lazyLoad234();
		}
		return _map234.containsKey(new LongArrayKey(key2, key3, key4));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key1, key2, key3, key4) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key1, key2, key3, key4) gibt.
	 */
	public boolean containsKey1234(final long key1, final long key2, final long key3, final long key4) {
		return _map1234.containsKey(new LongArrayKey(key1, key2, key3, key4));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get1(final long key1) {
		if (_map1 == null) {
			_map1 = _lazyLoad1();
		}
		final List<V> list = _map1.get(key1);
		return (list == null) ? new ArrayList<>() : new ArrayList<>(list);
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key2).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get2(final long key2) {
		if (_map2 == null) {
			_map2 = _lazyLoad2();
		}
		final List<V> list = _map2.get(key2);
		return (list == null) ? new ArrayList<>() : new ArrayList<>(list);
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key3).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key3   Der 3. Schlüssel
	 *
	 * @return eine Kopie der Liste aller Values zum Mapping (key3).
	 */
	public @NotNull List<V> get3(final long key3) {
		if (_map3 == null) {
			_map3 = _lazyLoad3();
		}
		final List<V> list = _map3.get(key3);
		return (list == null) ? new ArrayList<>() : new ArrayList<>(list);
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key4).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key4   Der 4. Schlüssel
	 *
	 * @return eine Kopie der Liste aller Values zum Mapping (key4).
	 */
	public @NotNull List<V> get4(final long key4) {
		if (_map4 == null) {
			_map4 = _lazyLoad4();
		}
		final List<V> list = _map4.get(key4);
		return (list == null) ? new ArrayList<>() : new ArrayList<>(list);
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key2).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel
	 * @param key2   Der 2. Schlüssel
	 *
	 * @return eine Kopie der Liste aller Values zum Mapping (key1, key2).
	 */
	public @NotNull List<V> get12(final long key1, final long key2) {
		if (_map12 == null) {
			_map12 = _lazyLoad12();
		}
		final List<V> list = _map12.get(new LongArrayKey(key1, key2));
		return (list == null) ? new ArrayList<>() : new ArrayList<>(list);
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key3).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel
	 * @param key3   Der 3. Schlüssel
	 *
	 * @return eine Kopie der Liste aller Values zum Mapping (key1, key3).
	 */
	public @NotNull List<V> get13(final long key1, final long key3) {
		if (_map13 == null) {
			_map13 = _lazyLoad13();
		}
		final List<V> list = _map13.get(new LongArrayKey(key1, key3));
		return (list == null) ? new ArrayList<>() : new ArrayList<>(list);
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key4).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel
	 * @param key4   Der 4. Schlüssel
	 *
	 * @return eine Kopie der Liste aller Values zum Mapping (key1, key4).
	 */
	public @NotNull List<V> get14(final long key1, final long key4) {
		if (_map14 == null) {
			_map14 = _lazyLoad14();
		}
		final List<V> list = _map14.get(new LongArrayKey(key1, key4));
		return (list == null) ? new ArrayList<>() : new ArrayList<>(list);
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key2, key3).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key2   Der 2. Schlüssel
	 * @param key3   Der 3. Schlüssel
	 *
	 * @return eine Kopie der Liste aller Values zum Mapping (key2, key3).
	 */
	public @NotNull List<V> get23(final long key2, final long key3) {
		if (_map23 == null) {
			_map23 = _lazyLoad23();
		}
		final List<V> list = _map23.get(new LongArrayKey(key2, key3));
		return (list == null) ? new ArrayList<>() : new ArrayList<>(list);
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key2, key4).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key2   Der 2. Schlüssel
	 * @param key4   Der 4. Schlüssel
	 *
	 * @return eine Kopie der Liste aller Values zum Mapping (key2, key4).
	 */
	public @NotNull List<V> get24(final long key2, final long key4) {
		if (_map24 == null) {
			_map24 = _lazyLoad24();
		}
		final List<V> list = _map24.get(new LongArrayKey(key2, key4));
		return (list == null) ? new ArrayList<>() : new ArrayList<>(list);
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key3, key4).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key3   Der 3. Schlüssel
	 * @param key4   Der 4. Schlüssel
	 *
	 * @return eine Kopie der Liste aller Values zum Mapping (key3, key4).
	 */
	public @NotNull List<V> get34(final long key3, final long key4) {
		if (_map34 == null) {
			_map34 = _lazyLoad34();
		}
		final List<V> list = _map34.get(new LongArrayKey(key3, key4));
		return (list == null) ? new ArrayList<>() : new ArrayList<>(list);
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key2, key3).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel
	 * @param key2   Der 2. Schlüssel
	 * @param key3   Der 3. Schlüssel
	 *
	 * @return eine Kopie der Liste aller Values zum Mapping (key1, key2, key3).
	 */
	public @NotNull List<V> get123(final long key1, final long key2, final long key3) {
		if (_map123 == null) {
			_map123 = _lazyLoad123();
		}
		final List<V> list = _map123.get(new LongArrayKey(key1, key2, key3));
		return (list == null) ? new ArrayList<>() : new ArrayList<>(list);
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key2, key4).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel
	 * @param key2   Der 2. Schlüssel
	 * @param key4   Der 4. Schlüssel
	 *
	 * @return eine Kopie der Liste aller Values zum Mapping (key1, key2, key4).
	 */
	public @NotNull List<V> get124(final long key1, final long key2, final long key4) {
		if (_map124 == null) {
			_map124 = _lazyLoad124();
		}
		final List<V> list = _map124.get(new LongArrayKey(key1, key2, key4));
		return (list == null) ? new ArrayList<>() : new ArrayList<>(list);
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key3, key4).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel
	 * @param key3   Der 3. Schlüssel
	 * @param key4   Der 4. Schlüssel
	 *
	 * @return eine Kopie der Liste aller Values zum Mapping (key1, key3, key4).
	 */
	public @NotNull List<V> get134(final long key1, final long key3, final long key4) {
		if (_map134 == null) {
			_map134 = _lazyLoad134();
		}
		final List<V> list = _map134.get(new LongArrayKey(key1, key3, key4));
		return (list == null) ? new ArrayList<>() : new ArrayList<>(list);
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key2, key3, key4).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key2   Der 2. Schlüssel
	 * @param key3   Der 3. Schlüssel
	 * @param key4   Der 4. Schlüssel
	 *
	 * @return eine Kopie der Liste aller Values zum Mapping (key2, key3, key4).
	 */
	public @NotNull List<V> get234(final long key2, final long key3, final long key4) {
		if (_map234 == null) {
			_map234 = _lazyLoad234();
		}
		final List<V> list = _map234.get(new LongArrayKey(key2, key3, key4));
		return (list == null) ? new ArrayList<>() : new ArrayList<>(list);
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key2, key3, key4).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel
	 * @param key2   Der 2. Schlüssel
	 * @param key3   Der 3. Schlüssel
	 * @param key4   Der 4. Schlüssel
	 *
	 * @return eine Kopie der Liste aller Values zum Mapping (key1, key2, key3, key4).
	 */
	public @NotNull List<V> get1234(final long key1, final long key2, final long key3, final long key4) {
		final List<V> list = _map1234.get(new LongArrayKey(key1, key2, key3, key4));
		return (list == null) ? new ArrayList<>() : new ArrayList<>(list);
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key1   Der 1. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingle1OrNull(final long key1) {
		if (_map1 == null) {
			_map1 = _lazyLoad1();
		}
		return getSingleOrNullHelperLong(_map1, key1);
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key2), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key2), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingle2OrNull(final long key2) {
		if (_map2 == null) {
			_map2 = _lazyLoad2();
		}
		return getSingleOrNullHelperLong(_map2, key2);
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key3), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key3), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingle3OrNull(final long key3) {
		if (_map3 == null) {
			_map3 = _lazyLoad3();
		}
		return getSingleOrNullHelperLong(_map3, key3);
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key4), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key4), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingle4OrNull(final long key4) {
		if (_map4 == null) {
			_map4 = _lazyLoad4();
		}
		return getSingleOrNullHelperLong(_map4, key4);
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key2), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key2), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingle12OrNull(final long key1, final long key2) {
		if (_map12 == null) {
			_map12 = _lazyLoad12();
		}
		return getSingleOrNullHelperLongArray(_map12, new LongArrayKey(key1, key2));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key3), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key3), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingle13OrNull(final long key1, final long key3) {
		if (_map13 == null) {
			_map13 = _lazyLoad13();
		}
		return getSingleOrNullHelperLongArray(_map13, new LongArrayKey(key1, key3));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key4), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key4), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingle14OrNull(final long key1, final long key4) {
		if (_map14 == null) {
			_map14 = _lazyLoad14();
		}
		return getSingleOrNullHelperLongArray(_map14, new LongArrayKey(key1, key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key2, key3), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key2, key3), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingle23OrNull(final long key2, final long key3) {
		if (_map23 == null) {
			_map23 = _lazyLoad23();
		}
		return getSingleOrNullHelperLongArray(_map23, new LongArrayKey(key2, key3));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key2, key4), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key2, key4), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingle24OrNull(final long key2, final long key4) {
		if (_map24 == null) {
			_map24 = _lazyLoad24();
		}
		return getSingleOrNullHelperLongArray(_map24, new LongArrayKey(key2, key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key3, key4), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key3, key4), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingle34OrNull(final long key3, final long key4) {
		if (_map34 == null) {
			_map34 = _lazyLoad34();
		}
		return getSingleOrNullHelperLongArray(_map34, new LongArrayKey(key3, key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key2, key3), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key2, key3), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingle123OrNull(final long key1, final long key2, final long key3) {
		if (_map123 == null) {
			_map123 = _lazyLoad123();
		}
		return getSingleOrNullHelperLongArray(_map123, new LongArrayKey(key1, key2, key3));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key2, key4), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key2, key4), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingle124OrNull(final long key1, final long key2, final long key4) {
		if (_map124 == null) {
			_map124 = _lazyLoad124();
		}
		return getSingleOrNullHelperLongArray(_map124, new LongArrayKey(key1, key2, key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key3, key4), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key3, key4), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingle134OrNull(final long key1, final long key3, final long key4) {
		if (_map134 == null) {
			_map134 = _lazyLoad134();
		}
		return getSingleOrNullHelperLongArray(_map134, new LongArrayKey(key1, key3, key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key2, key3, key4), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key2, key3, key4), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingle234OrNull(final long key2, final long key3, final long key4) {
		if (_map234 == null) {
			_map234 = _lazyLoad234();
		}
		return getSingleOrNullHelperLongArray(_map234, new LongArrayKey(key2, key3, key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key2, key3, key4), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key2, key3, key4), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingle1234OrNull(final long key1, final long key2, final long key3, final long key4) {
		return getSingleOrNullHelperLongArray(_map1234, new LongArrayKey(key1, key2, key3, key4));
	}


	/**
	 * Liefert das zugeordnete Element zum Mapping (key1), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key1   Der 1. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public @NotNull V getSingle1OrException(final long key1) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("getSingle1OrException: Dem Key %d ist keine Liste zugeordnet.", getSingle1OrNull(key1));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key2), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key2), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public @NotNull V getSingle2OrException(final long key2) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("getSingle2OrException: Dem Key %d ist keine Liste zugeordnet.", getSingle2OrNull(key2));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key3), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key3), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public @NotNull V getSingle3OrException(final long key3) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("getSingle3OrException: Dem Key %d ist keine Liste zugeordnet.", getSingle3OrNull(key3));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key4), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key4), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public @NotNull V getSingle4OrException(final long key4) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("getSingle4OrException: Dem Key %d ist keine Liste zugeordnet.", getSingle4OrNull(key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key2), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key2), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public @NotNull V getSingle12OrException(final long key1, final long key2) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingle12OrNull(key1, key2));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key3), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key3), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public @NotNull V getSingle13OrException(final long key1, final long key3) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingle13OrNull(key1, key3));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key4), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key4), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public @NotNull V getSingle14OrException(final long key1, final long key4) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingle14OrNull(key1, key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key2, key3), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key2, key3), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public @NotNull V getSingle23OrException(final long key2, final long key3) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingle23OrNull(key2, key3));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key2, key4), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key2, key4), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public @NotNull V getSingle24OrException(final long key2, final long key4) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingle24OrNull(key2, key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key3, key4), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key3, key4), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public @NotNull V getSingle34OrException(final long key3, final long key4) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingle34OrNull(key3, key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key2, key3), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key2, key3), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public @NotNull V getSingle123OrException(final long key1, final long key2, final long key3) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingle123OrNull(key1, key2, key3));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key2, key4), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key2, key4), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public @NotNull V getSingle124OrException(final long key1, final long key2, final long key4) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingle124OrNull(key1, key2, key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key3, key4), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key3, key4), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public @NotNull V getSingle134OrException(final long key1, final long key3, final long key4) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingle134OrNull(key1, key3, key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key2, key3, key4), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key2, key3, key4), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public @NotNull V getSingle234OrException(final long key2, final long key3, final long key4) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingle234OrNull(key2, key3, key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key2, key3, key4), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key2, key3, key4), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public @NotNull V getSingle1234OrException(final long key1, final long key2, final long key3, final long key4) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingle1234OrNull(key1, key2, key3, key4));
	}

	/**
	 * Liefert das Key-Set der Map1.
	 *
	 * @return das Key-Set der Map1.
	 */
	public @NotNull Set<Long> keySet1() {
		if (_map1 == null) {
			_map1 = _lazyLoad1();
		}
		return _map1.keySet();
	}

	/**
	 * Liefert das Key-Set der Map2.
	 *
	 * @return das Key-Set der Map2.
	 */
	public @NotNull Set<Long> keySet2() {
		if (_map2 == null) {
			_map2 = _lazyLoad2();
		}
		return _map2.keySet();
	}

	/**
	 * Liefert das Key-Set der Map3.
	 *
	 * @return das Key-Set der Map3.
	 */
	public @NotNull Set<Long> keySet3() {
		if (_map3 == null) {
			_map3 = _lazyLoad3();
		}
		return _map3.keySet();
	}

	/**
	 * Liefert das Key-Set der Map4.
	 *
	 * @return das Key-Set der Map4.
	 */
	public @NotNull Set<Long> keySet4() {
		if (_map4 == null) {
			_map4 = _lazyLoad4();
		}
		return _map4.keySet();
	}

	/**
	 * Liefert das Key-Set der Map12.
	 *
	 * @return das Key-Set der Map12.
	 */
	public @NotNull Set<LongArrayKey> keySet12() {
		if (_map12 == null) {
			_map12 = _lazyLoad12();
		}
		return _map12.keySet();
	}

	/**
	 * Liefert das Key-Set der Map13.
	 *
	 * @return das Key-Set der Map13.
	 */
	public @NotNull Set<LongArrayKey> keySet13() {
		if (_map13 == null) {
			_map13 = _lazyLoad13();
		}
		return _map13.keySet();
	}

	/**
	 * Liefert das Key-Set der Map14.
	 *
	 * @return das Key-Set der Map14.
	 */
	public @NotNull Set<LongArrayKey> keySet14() {
		if (_map14 == null) {
			_map14 = _lazyLoad14();
		}
		return _map14.keySet();
	}

	/**
	 * Liefert das Key-Set der Map23.
	 *
	 * @return das Key-Set der Map23.
	 */
	public @NotNull Set<LongArrayKey> keySet23() {
		if (_map23 == null) {
			_map23 = _lazyLoad23();
		}
		return _map23.keySet();
	}

	/**
	 * Liefert das Key-Set der Map24.
	 *
	 * @return das Key-Set der Map24.
	 */
	public @NotNull Set<LongArrayKey> keySet24() {
		if (_map24 == null) {
			_map24 = _lazyLoad24();
		}
		return _map24.keySet();
	}

	/**
	 * Liefert das Key-Set der Map34.
	 *
	 * @return das Key-Set der Map34.
	 */
	public @NotNull Set<LongArrayKey> keySet34() {
		if (_map34 == null) {
			_map34 = _lazyLoad34();
		}
		return _map34.keySet();
	}

	/**
	 * Liefert das Key-Set der Map123.
	 *
	 * @return das Key-Set der Map123.
	 */
	public @NotNull Set<LongArrayKey> keySet123() {
		if (_map123 == null) {
			_map123 = _lazyLoad123();
		}
		return _map123.keySet();
	}

	/**
	 * Liefert das Key-Set der Map124.
	 *
	 * @return das Key-Set der Map124.
	 */
	public @NotNull Set<LongArrayKey> keySet124() {
		if (_map124 == null) {
			_map124 = _lazyLoad124();
		}
		return _map124.keySet();
	}

	/**
	 * Liefert das Key-Set der Map134.
	 *
	 * @return das Key-Set der Map134.
	 */
	public @NotNull Set<LongArrayKey> keySet134() {
		if (_map134 == null) {
			_map134 = _lazyLoad134();
		}
		return _map134.keySet();
	}

	/**
	 * Liefert das Key-Set der Map234.
	 *
	 * @return das Key-Set der Map234.
	 */
	public @NotNull Set<LongArrayKey> keySet234() {
		if (_map234 == null) {
			_map234 = _lazyLoad234();
		}
		return _map234.keySet();
	}

	/**
	 * Liefert das Key-Set der Map1234.
	 *
	 * @return das Key-Set der Map1234.
	 */
	public @NotNull Set<LongArrayKey> keySet1234() {
		return _map1234.keySet();
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key1).
	 *
	 * @param key1   Der 1. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public @NotNull List<V> get1OrException(final long key1) {
		DeveloperNotificationException.ifTrue("get1OrException: Es keine Liste zugeordnet.", !containsKey1(key1));
		return get1(key1);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key2).
	 *
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public @NotNull List<V> get2OrException(final long key2) {
		DeveloperNotificationException.ifTrue("get2OrException: Es keine Liste zugeordnet.", !containsKey2(key2));
		return get2(key2);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key3).
	 *
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public @NotNull List<V> get3OrException(final long key3) {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !containsKey3(key3));
		return get3(key3);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key4).
	 *
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public @NotNull List<V> get4OrException(final long key4) {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !containsKey4(key4));
		return get4(key4);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key1, key2).
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public @NotNull List<V> get12OrException(final long key1, final long key2) {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !containsKey12(key1, key2));
		return get12(key1, key2);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key1, key3).
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public @NotNull List<V> get13OrException(final long key1, final long key3) {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !containsKey13(key1, key3));
		return get13(key1, key3);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key1, key4).
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public @NotNull List<V> get14OrException(final long key1, final long key4) {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !containsKey14(key1, key4));
		return get14(key1, key4);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key2, key3).
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public @NotNull List<V> get23OrException(final long key2, final long key3) {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !containsKey23(key2, key3));
		return get23(key2, key3);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key2, key4).
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public @NotNull List<V> get24OrException(final long key2, final long key4) {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !containsKey24(key2, key4));
		return get24(key2, key4);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key3, key4).
	 *
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public @NotNull List<V> get34OrException(final long key3, final long key4) {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !containsKey34(key3, key4));
		return get34(key3, key4);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key1, key2, key3).
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public @NotNull List<V> get123OrException(final long key1, final long key2, final long key3) {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !containsKey123(key1, key2, key3));
		return get123(key1, key2, key3);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key1, key2, key4).
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public @NotNull List<V> get124OrException(final long key1, final long key2, final long key4) {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !containsKey124(key1, key2, key4));
		return get124(key1, key2, key4);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key1, key3, key4).
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public @NotNull List<V> get134OrException(final long key1, final long key3, final long key4) {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !containsKey134(key1, key3, key4));
		return get134(key1, key3, key4);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key2, key3, key4).
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public @NotNull List<V> get234OrException(final long key2, final long key3, final long key4) {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !containsKey234(key2, key3, key4));
		return get234(key2, key3, key4);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key1, key2, key3, key4).
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public @NotNull List<V> get1234OrException(final long key1, final long key2, final long key3, final long key4) {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !containsKey1234(key1, key2, key3, key4));
		return get1234(key1, key2, key3, key4);
	}

}
