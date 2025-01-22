package de.svws_nrw.core.adt.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.core.adt.LongArrayKey;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.utils.MapUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Diese 5D-List-Map ordnet 5 Schlüssel auf eine Liste von Werten (V) ab.
 * <br> Diese spezielle Map stellt Zugriffsmethoden für alle Kombinationen der Schlüssel auf die Werte (V) zur Verfügung.
 * <br> Die Einfüge-Reihenfolge bleibt bei allen Listen erhalten.
 * <br> Ein Entfernen aus der Datenstruktur ist nicht vorgesehen.
 *
 * @param <V> Der Typ der zugeordneten Werte.
 */
public class ListMap5DLongKeys<V> {

	private Map<Long, List<V>> _map1 = null;
	private Map<Long, List<V>> _map2 = null;
	private Map<Long, List<V>> _map3 = null;
	private Map<Long, List<V>> _map4 = null;
	private Map<Long, List<V>> _map5 = null;

	private Map<LongArrayKey, List<V>> _map12 = null;
	private Map<LongArrayKey, List<V>> _map13 = null;
	private Map<LongArrayKey, List<V>> _map14 = null;
	private Map<LongArrayKey, List<V>> _map15 = null;
	private Map<LongArrayKey, List<V>> _map23 = null;
	private Map<LongArrayKey, List<V>> _map24 = null;
	private Map<LongArrayKey, List<V>> _map25 = null;
	private Map<LongArrayKey, List<V>> _map34 = null;
	private Map<LongArrayKey, List<V>> _map35 = null;
	private Map<LongArrayKey, List<V>> _map45 = null;

	private Map<LongArrayKey, List<V>> _map123 = null;
	private Map<LongArrayKey, List<V>> _map124 = null;
	private Map<LongArrayKey, List<V>> _map125 = null;
	private Map<LongArrayKey, List<V>> _map134 = null;
	private Map<LongArrayKey, List<V>> _map135 = null;
	private Map<LongArrayKey, List<V>> _map145 = null;
	private Map<LongArrayKey, List<V>> _map234 = null;
	private Map<LongArrayKey, List<V>> _map235 = null;
	private Map<LongArrayKey, List<V>> _map245 = null;
	private Map<LongArrayKey, List<V>> _map345 = null;

	private Map<LongArrayKey, List<V>> _map1234 = null;
	private Map<LongArrayKey, List<V>> _map1235 = null;
	private Map<LongArrayKey, List<V>> _map1245 = null;
	private Map<LongArrayKey, List<V>> _map1345 = null;
	private Map<LongArrayKey, List<V>> _map2345 = null;

	private final @NotNull Map<LongArrayKey, List<V>> _map12345 = new HashMap<>();
	private final @NotNull List<Pair<LongArrayKey, V>> _list = new ArrayList<>();

	/**
	 * Konstruktor.
	 */
	public ListMap5DLongKeys() {
		// leer
	}

    private @NotNull Map<Long, List<V>> _lazyLoad1() {
		final Map<Long, List<V>> map = new HashMap<>();

		for (final @NotNull Pair<LongArrayKey, V> e : _list) {
			final long key1 = e.a.getKeyAt(0);
			if (e.b == null)
				MapUtils.getOrCreateArrayList(map, key1);
			else
				MapUtils.getOrCreateArrayList(map, key1).add(e.b);
		}

		return map;
	}

	private @NotNull Map<Long, List<V>> _lazyLoad2() {
		final Map<Long, List<V>> map = new HashMap<>();

		for (final @NotNull Pair<LongArrayKey, V> e : _list) {
			final long key2 = e.a.getKeyAt(1);
			if (e.b == null)
				MapUtils.getOrCreateArrayList(map, key2);
			else
				MapUtils.getOrCreateArrayList(map, key2).add(e.b);
		}

		return map;
	}

	private @NotNull Map<Long, List<V>> _lazyLoad3() {
		final Map<Long, List<V>> map = new HashMap<>();

		for (final @NotNull Pair<LongArrayKey, V> e : _list) {
			final long key3 = e.a.getKeyAt(2);
			if (e.b == null)
				MapUtils.getOrCreateArrayList(map, key3);
			else
				MapUtils.getOrCreateArrayList(map, key3).add(e.b);
		}

		return map;
	}

	private @NotNull Map<Long, List<V>> _lazyLoad4() {
		final Map<Long, List<V>> map = new HashMap<>();

		for (final @NotNull Pair<LongArrayKey, V> e : _list) {
			final long key4 = e.a.getKeyAt(3);
			if (e.b == null)
				MapUtils.getOrCreateArrayList(map, key4);
			else
				MapUtils.getOrCreateArrayList(map, key4).add(e.b);
		}

		return map;
	}

	private @NotNull Map<Long, List<V>> _lazyLoad5() {
		final Map<Long, List<V>> map = new HashMap<>();

		for (final @NotNull Pair<LongArrayKey, V> e : _list) {
			final long key5 = e.a.getKeyAt(4);
			if (e.b == null)
				MapUtils.getOrCreateArrayList(map, key5);
			else
				MapUtils.getOrCreateArrayList(map, key5).add(e.b);
		}

		return map;
	}

	private @NotNull Map<LongArrayKey, List<V>> _lazyLoad12() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Pair<LongArrayKey, V> e : _list) {
			final long key1 = e.a.getKeyAt(0);
			final long key2 = e.a.getKeyAt(1);
			if (e.b == null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2)).add(e.b);
		}

		return map;
	}

	private @NotNull Map<LongArrayKey, List<V>> _lazyLoad13() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Pair<LongArrayKey, V> e : _list) {
			final long key1 = e.a.getKeyAt(0);
			final long key3 = e.a.getKeyAt(2);
			if (e.b == null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key3));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key3)).add(e.b);
		}

		return map;
	}

	private @NotNull Map<LongArrayKey, List<V>> _lazyLoad14() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Pair<LongArrayKey, V> e : _list) {
			final long key1 = e.a.getKeyAt(0);
			final long key4 = e.a.getKeyAt(3);
			if (e.b == null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key4));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key4)).add(e.b);
		}

		return map;
	}

	private @NotNull Map<LongArrayKey, List<V>> _lazyLoad15() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Pair<LongArrayKey, V> e : _list) {
			final long key1 = e.a.getKeyAt(0);
			final long key5 = e.a.getKeyAt(4);
			if (e.b == null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key5));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key5)).add(e.b);
		}

		return map;
	}

	private @NotNull Map<LongArrayKey, List<V>> _lazyLoad23() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Pair<LongArrayKey, V> e : _list) {
			final long key2 = e.a.getKeyAt(1);
			final long key3 = e.a.getKeyAt(2);
			if (e.b == null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key2, key3));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key2, key3)).add(e.b);
		}

		return map;
	}

	private @NotNull Map<LongArrayKey, List<V>> _lazyLoad24() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Pair<LongArrayKey, V> e : _list) {
			final long key2 = e.a.getKeyAt(1);
			final long key4 = e.a.getKeyAt(3);
			if (e.b == null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key2, key4));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key2, key4)).add(e.b);
		}

		return map;
	}

	private @NotNull Map<LongArrayKey, List<V>> _lazyLoad25() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Pair<LongArrayKey, V> e : _list) {
			final long key2 = e.a.getKeyAt(1);
			final long key5 = e.a.getKeyAt(4);
			if (e.b == null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key2, key5));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key2, key5)).add(e.b);
		}

		return map;
	}

	private @NotNull Map<LongArrayKey, List<V>> _lazyLoad34() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Pair<LongArrayKey, V> e : _list) {
			final long key3 = e.a.getKeyAt(2);
			final long key4 = e.a.getKeyAt(3);
			if (e.b == null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key3, key4));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key3, key4)).add(e.b);
		}

		return map;
	}

	private @NotNull Map<LongArrayKey, List<V>> _lazyLoad35() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Pair<LongArrayKey, V> e : _list) {
			final long key3 = e.a.getKeyAt(2);
			final long key5 = e.a.getKeyAt(4);
			if (e.b == null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key3, key5));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key3, key5)).add(e.b);
		}

		return map;
	}

	private @NotNull Map<LongArrayKey, List<V>> _lazyLoad45() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Pair<LongArrayKey, V> e : _list) {
			final long key4 = e.a.getKeyAt(3);
			final long key5 = e.a.getKeyAt(4);
			if (e.b == null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key4, key5));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key4, key5)).add(e.b);
		}

		return map;
	}

    private @NotNull Map<LongArrayKey, List<V>> _lazyLoad125() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Pair<LongArrayKey, V> e : _list) {
			final long key1 = e.a.getKeyAt(0);
			final long key2 = e.a.getKeyAt(1);
			final long key5 = e.a.getKeyAt(4);
			if (e.b == null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2, key5));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2, key5)).add(e.b);
		}

		return map;
	}

	private @NotNull Map<LongArrayKey, List<V>> _lazyLoad135() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Pair<LongArrayKey, V> e : _list) {
			final long key1 = e.a.getKeyAt(0);
			final long key3 = e.a.getKeyAt(2);
			final long key5 = e.a.getKeyAt(4);
			if (e.b == null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key3, key5));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key3, key5)).add(e.b);
		}

		return map;
	}

	private @NotNull Map<LongArrayKey, List<V>> _lazyLoad145() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Pair<LongArrayKey, V> e : _list) {
			final long key1 = e.a.getKeyAt(0);
			final long key4 = e.a.getKeyAt(3);
			final long key5 = e.a.getKeyAt(4);
			if (e.b == null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key4, key5));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key4, key5)).add(e.b);
		}

		return map;
	}

	private @NotNull Map<LongArrayKey, List<V>> _lazyLoad235() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Pair<LongArrayKey, V> e : _list) {
			final long key2 = e.a.getKeyAt(1);
			final long key3 = e.a.getKeyAt(2);
			final long key5 = e.a.getKeyAt(4);
			if (e.b == null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key2, key3, key5));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key2, key3, key5)).add(e.b);
		}

		return map;
	}

	private @NotNull Map<LongArrayKey, List<V>> _lazyLoad245() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Pair<LongArrayKey, V> e : _list) {
			final long key2 = e.a.getKeyAt(1);
			final long key4 = e.a.getKeyAt(3);
			final long key5 = e.a.getKeyAt(4);
			if (e.b == null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key2, key4, key5));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key2, key4, key5)).add(e.b);
		}

		return map;
	}

    private @NotNull Map<LongArrayKey, List<V>> _lazyLoad123() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Pair<LongArrayKey, V> e : _list) {
			final long key1 = e.a.getKeyAt(0);
			final long key2 = e.a.getKeyAt(1);
			final long key3 = e.a.getKeyAt(2);
			if (e.b == null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2, key3));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2, key3)).add(e.b);
		}

		return map;
	}

	private @NotNull Map<LongArrayKey, List<V>> _lazyLoad124() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Pair<LongArrayKey, V> e : _list) {
			final long key1 = e.a.getKeyAt(0);
			final long key2 = e.a.getKeyAt(1);
			final long key4 = e.a.getKeyAt(3);
			if (e.b == null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2, key4));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2, key4)).add(e.b);
		}

		return map;
	}

    private @NotNull Map<LongArrayKey, List<V>> _lazyLoad134() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Pair<LongArrayKey, V> e : _list) {
			final long key1 = e.a.getKeyAt(0);
			final long key3 = e.a.getKeyAt(2);
			final long key4 = e.a.getKeyAt(3);
			if (e.b == null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key3, key4));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key3, key4)).add(e.b);
		}

		return map;
	}

	private @NotNull Map<LongArrayKey, List<V>> _lazyLoad234() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Pair<LongArrayKey, V> e : _list) {
			final long key2 = e.a.getKeyAt(1);
			final long key3 = e.a.getKeyAt(2);
			final long key4 = e.a.getKeyAt(3);
			if (e.b == null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key2, key3, key4));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key2, key3, key4)).add(e.b);
		}

		return map;
	}

	private @NotNull Map<LongArrayKey, List<V>> _lazyLoad1234() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Pair<LongArrayKey, V> e : _list) {
			final long key1 = e.a.getKeyAt(0);
			final long key2 = e.a.getKeyAt(1);
			final long key3 = e.a.getKeyAt(2);
			final long key4 = e.a.getKeyAt(3);
			if (e.b == null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2, key3, key4));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2, key3, key4)).add(e.b);
		}

		return map;
	}

	private @NotNull Map<LongArrayKey, List<V>> _lazyLoad1235() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Pair<LongArrayKey, V> e : _list) {
			final long key1 = e.a.getKeyAt(0);
			final long key2 = e.a.getKeyAt(1);
			final long key3 = e.a.getKeyAt(2);
			final long key5 = e.a.getKeyAt(4);
			if (e.b == null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2, key3, key5));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2, key3, key5)).add(e.b);
		}

		return map;
	}

	private @NotNull Map<LongArrayKey, List<V>> _lazyLoad1245() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Pair<LongArrayKey, V> e : _list) {
			final long key1 = e.a.getKeyAt(0);
			final long key2 = e.a.getKeyAt(1);
			final long key4 = e.a.getKeyAt(3);
			final long key5 = e.a.getKeyAt(4);
			if (e.b == null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2, key4, key5));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2, key4, key5)).add(e.b);
		}

		return map;
	}

	private @NotNull Map<LongArrayKey, List<V>> _lazyLoad1345() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Pair<LongArrayKey, V> e : _list) {
			final long key1 = e.a.getKeyAt(0);
			final long key3 = e.a.getKeyAt(2);
			final long key4 = e.a.getKeyAt(3);
			final long key5 = e.a.getKeyAt(4);
			if (e.b == null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key3, key4, key5));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key3, key4, key5)).add(e.b);
		}

		return map;
	}

	private @NotNull Map<LongArrayKey, List<V>> _lazyLoad2345() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Pair<LongArrayKey, V> e : _list) {
			final long key2 = e.a.getKeyAt(1);
			final long key3 = e.a.getKeyAt(2);
			final long key4 = e.a.getKeyAt(3);
			final long key5 = e.a.getKeyAt(4);
			if (e.b == null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key2, key3, key4, key5));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key2, key3, key4, key5)).add(e.b);
		}

		return map;
	}

	private @NotNull Map<LongArrayKey, List<V>> _lazyLoad12345() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Pair<LongArrayKey, V> e : _list) {
			final long key1 = e.a.getKeyAt(0);
			final long key2 = e.a.getKeyAt(1);
			final long key3 = e.a.getKeyAt(2);
			final long key4 = e.a.getKeyAt(3);
			final long key5 = e.a.getKeyAt(4);
			if (e.b == null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2, key3, key4, key5));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key1, key2, key3, key4, key5)).add(e.b);
		}

		return map;
	}

	private @NotNull Map<LongArrayKey, List<V>> _lazyLoad345() {
		final Map<LongArrayKey, List<V>> map = new HashMap<>();

		for (final @NotNull Pair<LongArrayKey, V> e : _list) {
			final long key3 = e.a.getKeyAt(2);
			final long key4 = e.a.getKeyAt(3);
			final long key5 = e.a.getKeyAt(4);
			if (e.b == null)
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key3, key4, key5));
			else
				MapUtils.getOrCreateArrayList(map, new LongArrayKey(key3, key4, key5)).add(e.b);
		}

		return map;
	}

    private V getSingleOrNullHelperLong(final @NotNull Map<Long, List<V>> map, final long key) {
		List<V> list = map.get(key);
		if (list == null)
			return null;
		if (list.size() != 1)
			return null;
		return list.getFirst();
	}

	private V getSingleOrNullHelperLongArray(final @NotNull Map<LongArrayKey, List<V>> map, final @NotNull LongArrayKey key) {
		List<V> list = map.get(key);
		if (list == null)
			return null;
		if (list.size() != 1)
			return null;
		return list.getFirst();
	}

	/**
	 * Fügt das Element hinzu.
	 *
	 * @param key1  Der 1. Schlüssel.
	 * @param key2  Der 2. Schlüssel.
	 * @param key3  Der 3. Schlüssel.
	 * @param key4  Der 4. Schlüssel.
	 * @param key5  Der 5. Schlüssel.
	 *
	 * @param value Der zugeordnete Wert. Der Wert NULL ist nicht erlaubt.
	 */
	public void add(final long key1, final long key2, final long key3, final long key4, final long key5, final @NotNull V value) {
		final @NotNull LongArrayKey key = new LongArrayKey(key1, key2, key3, key4, key5);
		MapUtils.getOrCreateArrayList(_map12345, key).add(value);
		_list.add(new Pair<>(key, value));

		if (_map1 != null)
			MapUtils.getOrCreateArrayList(_map1, key1).add(value);
		if (_map2 != null)
			MapUtils.getOrCreateArrayList(_map2, key2).add(value);
		if (_map3 != null)
			MapUtils.getOrCreateArrayList(_map3, key3).add(value);
		if (_map4 != null)
			MapUtils.getOrCreateArrayList(_map4, key4).add(value);
		if (_map5 != null)
			MapUtils.getOrCreateArrayList(_map5, key5).add(value);

		if (_map12 != null)
			MapUtils.getOrCreateArrayList(_map12, new LongArrayKey(key1, key2)).add(value);
		if (_map13 != null)
			MapUtils.getOrCreateArrayList(_map13, new LongArrayKey(key1, key3)).add(value);
		if (_map14 != null)
			MapUtils.getOrCreateArrayList(_map14, new LongArrayKey(key1, key4)).add(value);
		if (_map15 != null)
			MapUtils.getOrCreateArrayList(_map15, new LongArrayKey(key1, key5)).add(value);
		if (_map23 != null)
			MapUtils.getOrCreateArrayList(_map23, new LongArrayKey(key2, key3)).add(value);
		if (_map24 != null)
			MapUtils.getOrCreateArrayList(_map24, new LongArrayKey(key2, key4)).add(value);
		if (_map25 != null)
			MapUtils.getOrCreateArrayList(_map25, new LongArrayKey(key2, key5)).add(value);
		if (_map34 != null)
			MapUtils.getOrCreateArrayList(_map34, new LongArrayKey(key3, key4)).add(value);
		if (_map35 != null)
			MapUtils.getOrCreateArrayList(_map35, new LongArrayKey(key3, key5)).add(value);
		if (_map45 != null)
			MapUtils.getOrCreateArrayList(_map45, new LongArrayKey(key4, key5)).add(value);

		if (_map123 != null)
			MapUtils.getOrCreateArrayList(_map123, new LongArrayKey(key1, key2, key3)).add(value);
		if (_map124 != null)
			MapUtils.getOrCreateArrayList(_map124, new LongArrayKey(key1, key2, key4)).add(value);
		if (_map125 != null)
			MapUtils.getOrCreateArrayList(_map125, new LongArrayKey(key1, key2, key5)).add(value);
		if (_map134 != null)
			MapUtils.getOrCreateArrayList(_map134, new LongArrayKey(key1, key3, key4)).add(value);
		if (_map135 != null)
			MapUtils.getOrCreateArrayList(_map135, new LongArrayKey(key1, key3, key5)).add(value);
		if (_map145 != null)
			MapUtils.getOrCreateArrayList(_map145, new LongArrayKey(key1, key4, key5)).add(value);
		if (_map234 != null)
			MapUtils.getOrCreateArrayList(_map234, new LongArrayKey(key2, key3, key4)).add(value);
		if (_map235 != null)
			MapUtils.getOrCreateArrayList(_map235, new LongArrayKey(key2, key3, key5)).add(value);
		if (_map245 != null)
			MapUtils.getOrCreateArrayList(_map245, new LongArrayKey(key2, key4, key5)).add(value);
		if (_map345 != null)
			MapUtils.getOrCreateArrayList(_map345, new LongArrayKey(key3, key4, key5)).add(value);

		if (_map1234 != null)
			MapUtils.getOrCreateArrayList(_map1234, new LongArrayKey(key1, key2, key3, key4)).add(value);
		if (_map1235 != null)
			MapUtils.getOrCreateArrayList(_map1235, new LongArrayKey(key1, key2, key3, key5)).add(value);
		if (_map1245 != null)
			MapUtils.getOrCreateArrayList(_map1245, new LongArrayKey(key1, key2, key4, key5)).add(value);
		if (_map1345 != null)
			MapUtils.getOrCreateArrayList(_map1345, new LongArrayKey(key1, key3, key4, key5)).add(value);
		if (_map2345 != null)
			MapUtils.getOrCreateArrayList(_map2345, new LongArrayKey(key2, key3, key4, key5)).add(value);
	}

	/**
	 * Erzeugt den Pfad (key1, key2, key3, key4, key5) fügt aber nichts hinzu.
	 * Alle Pfad, die es vorher nicht gab, verweisen dann auf leere Listen.
	 *
	 * @param key1  Der 1. Schlüssel.
	 * @param key2  Der 2. Schlüssel.
	 * @param key3  Der 3. Schlüssel.
	 * @param key4  Der 4. Schlüssel.
	 * @param key5  Der 5. Schlüssel.
	 */
	public void addEmpty(final long key1, final long key2, final long key3, final long key4, final long key5) {
		final @NotNull LongArrayKey key = new LongArrayKey(key1, key2, key3, key4, key5);
		MapUtils.getOrCreateArrayList(_map12345, key);
		_list.add(new Pair<>(key, null));

		if (_map1 != null)
			MapUtils.getOrCreateArrayList(_map1, key1);
		if (_map2 != null)
			MapUtils.getOrCreateArrayList(_map2, key2);
		if (_map3 != null)
			MapUtils.getOrCreateArrayList(_map3, key3);
		if (_map4 != null)
			MapUtils.getOrCreateArrayList(_map4, key4);
		if (_map5 != null)
			MapUtils.getOrCreateArrayList(_map5, key5);

		if (_map12 != null)
			MapUtils.getOrCreateArrayList(_map12, new LongArrayKey(key1, key2));
		if (_map13 != null)
			MapUtils.getOrCreateArrayList(_map13, new LongArrayKey(key1, key3));
		if (_map14 != null)
			MapUtils.getOrCreateArrayList(_map14, new LongArrayKey(key1, key4));
		if (_map15 != null)
			MapUtils.getOrCreateArrayList(_map15, new LongArrayKey(key1, key5));
		if (_map23 != null)
			MapUtils.getOrCreateArrayList(_map23, new LongArrayKey(key2, key3));
		if (_map24 != null)
			MapUtils.getOrCreateArrayList(_map24, new LongArrayKey(key2, key4));
		if (_map25 != null)
			MapUtils.getOrCreateArrayList(_map25, new LongArrayKey(key2, key5));
		if (_map34 != null)
			MapUtils.getOrCreateArrayList(_map34, new LongArrayKey(key3, key4));
		if (_map35 != null)
			MapUtils.getOrCreateArrayList(_map35, new LongArrayKey(key3, key5));
		if (_map45 != null)
			MapUtils.getOrCreateArrayList(_map45, new LongArrayKey(key4, key5));

		if (_map123 != null)
			MapUtils.getOrCreateArrayList(_map123, new LongArrayKey(key1, key2, key3));
		if (_map124 != null)
			MapUtils.getOrCreateArrayList(_map124, new LongArrayKey(key1, key2, key4));
		if (_map125 != null)
			MapUtils.getOrCreateArrayList(_map125, new LongArrayKey(key1, key2, key5));
		if (_map134 != null)
			MapUtils.getOrCreateArrayList(_map134, new LongArrayKey(key1, key3, key4));
		if (_map135 != null)
			MapUtils.getOrCreateArrayList(_map135, new LongArrayKey(key1, key3, key5));
		if (_map145 != null)
			MapUtils.getOrCreateArrayList(_map145, new LongArrayKey(key1, key4, key5));
		if (_map234 != null)
			MapUtils.getOrCreateArrayList(_map234, new LongArrayKey(key2, key3, key4));
		if (_map235 != null)
			MapUtils.getOrCreateArrayList(_map235, new LongArrayKey(key2, key3, key5));
		if (_map245 != null)
			MapUtils.getOrCreateArrayList(_map245, new LongArrayKey(key2, key4, key5));
		if (_map345 != null)
			MapUtils.getOrCreateArrayList(_map345, new LongArrayKey(key3, key4, key5));

		if (_map1234 != null)
			MapUtils.getOrCreateArrayList(_map1234, new LongArrayKey(key1, key2, key3, key4));
		if (_map1235 != null)
			MapUtils.getOrCreateArrayList(_map1235, new LongArrayKey(key1, key2, key3, key5));
		if (_map1245 != null)
			MapUtils.getOrCreateArrayList(_map1245, new LongArrayKey(key1, key2, key4, key5));
		if (_map1345 != null)
			MapUtils.getOrCreateArrayList(_map1345, new LongArrayKey(key1, key3, key4, key5));
		if (_map2345 != null)
			MapUtils.getOrCreateArrayList(_map2345, new LongArrayKey(key2, key3, key4, key5));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key1) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key1) gibt.
	 */
	public boolean containsKey1(final long key1) {
		if (_map1 == null)
			_map1 = _lazyLoad1();
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
		if (_map2 == null)
			_map2 = _lazyLoad2();
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
		if (_map3 == null)
			_map3 = _lazyLoad3();
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
		if (_map4 == null)
			_map4 = _lazyLoad4();
		return _map4.containsKey(key4);
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key5) gibt.
	 *
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key5) gibt.
	 */
	public boolean containsKey5(final long key5) {
		if (_map5 == null)
			_map5 = _lazyLoad5();
		return _map5.containsKey(key5);
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
		if (_map12 == null)
			_map12 = _lazyLoad12();
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
		if (_map13 == null)
			_map13 = _lazyLoad13();
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
		if (_map14 == null)
			_map14 = _lazyLoad14();
		return _map14.containsKey(new LongArrayKey(key1, key4));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key1, key5) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key1, key5) gibt.
	 */
	public boolean containsKey15(final long key1, final long key5) {
		if (_map15 == null)
			_map15 = _lazyLoad15();
		return _map15.containsKey(new LongArrayKey(key1, key5));
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
		if (_map23 == null)
			_map23 = _lazyLoad23();
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
		if (_map24 == null)
			_map24 = _lazyLoad24();
		return _map24.containsKey(new LongArrayKey(key2, key4));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key2, key5) gibt.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key2, key5) gibt.
	 */
	public boolean containsKey25(final long key2, final long key5) {
		if (_map25 == null)
			_map25 = _lazyLoad25();
		return _map25.containsKey(new LongArrayKey(key2, key5));
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
		if (_map34 == null)
			_map34 = _lazyLoad34();
		return _map34.containsKey(new LongArrayKey(key3, key4));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key3, key5) gibt.
	 *
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key3, key5) gibt.
	 */
	public boolean containsKey35(final long key3, final long key5) {
		if (_map35 == null)
			_map35 = _lazyLoad35();
		return _map35.containsKey(new LongArrayKey(key3, key5));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key4, key5) gibt.
	 *
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key4, key5) gibt.
	 */
	public boolean containsKey45(final long key4, final long key5) {
		if (_map45 == null)
			_map45 = _lazyLoad45();
		return _map45.containsKey(new LongArrayKey(key4, key5));
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
		if (_map123 == null)
			_map123 = _lazyLoad123();
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
		if (_map124 == null)
			_map124 = _lazyLoad124();
		return _map124.containsKey(new LongArrayKey(key1, key2, key4));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key1, key2, key5) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key1, key2, key5) gibt.
	 */
	public boolean containsKey125(final long key1, final long key2, final long key5) {
		if (_map125 == null)
			_map125 = _lazyLoad125();
		return _map125.containsKey(new LongArrayKey(key1, key2, key5));
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
		if (_map134 == null)
			_map134 = _lazyLoad134();
		return _map134.containsKey(new LongArrayKey(key1, key3, key4));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key1, key3, key5) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key1, key3, key5) gibt.
	 */
	public boolean containsKey135(final long key1, final long key3, final long key5) {
		if (_map135 == null)
			_map135 = _lazyLoad135();
		return _map135.containsKey(new LongArrayKey(key1, key3, key5));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key1, key4, key5) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key1, key4, key5) gibt.
	 */
	public boolean containsKey145(final long key1, final long key4, final long key5) {
		if (_map145 == null)
			_map145 = _lazyLoad145();
		return _map145.containsKey(new LongArrayKey(key1, key4, key5));
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
		if (_map234 == null)
			_map234 = _lazyLoad234();
		return _map234.containsKey(new LongArrayKey(key2, key3, key4));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key2, key3, key5) gibt.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key2, key3, key5) gibt.
	 */
	public boolean containsKey235(final long key2, final long key3, final long key5) {
		if (_map235 == null)
			_map235 = _lazyLoad235();
		return _map235.containsKey(new LongArrayKey(key2, key3, key5));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key2, key4, key5) gibt.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key2, key4, key5) gibt.
	 */
	public boolean containsKey245(final long key2, final long key4, final long key5) {
		if (_map245 == null)
			_map245 = _lazyLoad245();
		return _map245.containsKey(new LongArrayKey(key2, key4, key5));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key3, key4, key5) gibt.
	 *
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key3, key4, key5) gibt.
	 */
	public boolean containsKey345(final long key3, final long key4, final long key5) {
		if (_map345 == null)
			_map345 = _lazyLoad345();
		return _map345.containsKey(new LongArrayKey(key3, key4, key5));
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
		if (_map1234 == null)
			_map1234 = _lazyLoad1234();
		return _map1234.containsKey(new LongArrayKey(key1, key2, key3, key4));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key1, key2, key3, key5) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key1, key2, key3, key5) gibt.
	 */
	public boolean containsKey1235(final long key1, final long key2, final long key3, final long key5) {
		if (_map1235 == null)
			_map1235 = _lazyLoad1235();
		return _map1235.containsKey(new LongArrayKey(key1, key2, key3, key5));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key1, key2, key4, key5) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key1, key2, key4, key5) gibt.
	 */
	public boolean containsKey1245(final long key1, final long key2, final long key4, final long key5) {
		if (_map1245 == null)
			_map1245 = _lazyLoad1245();
		return _map1245.containsKey(new LongArrayKey(key1, key2, key4, key5));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key1, key3, key4, key5) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key1, key3, key4, key5) gibt.
	 */
	public boolean containsKey1345(final long key1, final long key3, final long key4, final long key5) {
		if (_map1345 == null)
			_map1345 = _lazyLoad1345();
		return _map1345.containsKey(new LongArrayKey(key1, key3, key4, key5));
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key2, key3, key4, key5) gibt.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key2, key3, key4, key5) gibt.
	 */
	public boolean containsKey2345(final long key2, final long key3, final long key4, final long key5) {
		if (_map2345 == null)
			_map2345 = _lazyLoad2345();
		return _map2345.containsKey(new LongArrayKey(key2, key3, key4, key5));
	}

    /**
	 * Liefert TRUE, falls es den Schlüssel (key1, key2, key3, key4, key5) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
     * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (ky1, key2, key3, key4, key5) gibt.
	 */
	public boolean containsKey12345(final long key1, final long key2, final long key3, final long key4, final long key5) {
		return _map12345.containsKey(new LongArrayKey(key1, key2, key3, key4, key5));
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
		if (_map1 == null)
			_map1 = _lazyLoad1();
		if (!_map1.containsKey(key1))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map1, key1));
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
		if (_map2 == null)
			_map2 = _lazyLoad2();
		if (!_map2.containsKey(key2))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map2, key2));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key3).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get3(final long key3) {
		if (_map3 == null)
			_map3 = _lazyLoad3();
		if (!_map3.containsKey(key3))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map3, key3));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key4).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get4(final long key4) {
		if (_map4 == null)
			_map4 = _lazyLoad4();
		if (!_map4.containsKey(key4))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map4, key4));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key5).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get5(final long key5) {
		if (_map5 == null)
			_map5 = _lazyLoad5();
		if (!_map5.containsKey(key5))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map5, key5));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key2).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get12(final long key1, final long key2) {
		if (_map12 == null)
			_map12 = _lazyLoad12();
		final LongArrayKey key = new LongArrayKey(key1, key2);
		if (!_map12.containsKey(key))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map12, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key3).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get13(final long key1, final long key3) {
		if (_map13 == null)
			_map13 = _lazyLoad13();
		final LongArrayKey key = new LongArrayKey(key1, key3);
		if (!_map13.containsKey(key))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map13, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key4).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get14(final long key1, final long key4) {
		if (_map14 == null)
			_map14 = _lazyLoad14();
		final LongArrayKey key = new LongArrayKey(key1, key4);
		if (!_map14.containsKey(key))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map14, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key5).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get15(final long key1, final long key5) {
		if (_map15 == null)
			_map15 = _lazyLoad15();
		final LongArrayKey key = new LongArrayKey(key1, key5);
		if (!_map15.containsKey(key))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map15, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key2, key3).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get23(final long key2, final long key3) {
		if (_map23 == null)
			_map23 = _lazyLoad23();
		final LongArrayKey key = new LongArrayKey(key2, key3);
		if (!_map23.containsKey(key))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map23, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key2, key4).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get24(final long key2, final long key4) {
		if (_map24 == null)
			_map24 = _lazyLoad24();
		final LongArrayKey key = new LongArrayKey(key2, key4);
		if (!_map24.containsKey(key))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map24, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key2, key5).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get25(final long key2, final long key5) {
		if (_map25 == null)
			_map25 = _lazyLoad25();
		final LongArrayKey key = new LongArrayKey(key2, key5);
		if (!_map25.containsKey(key))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map25, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key3, key4).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get34(final long key3, final long key4) {
		if (_map34 == null)
			_map34 = _lazyLoad34();
		final LongArrayKey key = new LongArrayKey(key3, key4);
		if (!_map34.containsKey(key))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map34, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key3, key5).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get35(final long key3, final long key5) {
		if (_map35 == null)
			_map35 = _lazyLoad35();
		final LongArrayKey key = new LongArrayKey(key3, key5);
		if (!_map35.containsKey(key))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map35, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key4, key5).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get45(final long key4, final long key5) {
		if (_map45 == null)
			_map45 = _lazyLoad45();
		final LongArrayKey key = new LongArrayKey(key4, key5);
		if (!_map45.containsKey(key))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map45, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key2, key3).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get123(final long key1, final long key2, final long key3) {
		if (_map123 == null)
			_map123 = _lazyLoad123();
		final LongArrayKey key = new LongArrayKey(key1, key2, key3);
		if (!_map123.containsKey(key))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map123, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key2, key4).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get124(final long key1, final long key2, final long key4) {
		if (_map124 == null)
			_map124 = _lazyLoad124();
		final LongArrayKey key = new LongArrayKey(key1, key2, key4);
		if (!_map124.containsKey(key))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map124, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key2, key5).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get125(final long key1, final long key2, final long key5) {
		if (_map125 == null)
			_map125 = _lazyLoad125();
		final LongArrayKey key = new LongArrayKey(key1, key2, key5);
		if (!_map125.containsKey(key))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map125, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key3, key4).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get134(final long key1, final long key3, final long key4) {
		if (_map134 == null)
			_map134 = _lazyLoad134();
		final LongArrayKey key = new LongArrayKey(key1, key3, key4);
		if (!_map134.containsKey(key))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map134, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key3, key5).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get135(final long key1, final long key3, final long key5) {
		if (_map135 == null)
			_map135 = _lazyLoad135();
		final LongArrayKey key = new LongArrayKey(key1, key3, key5);
		if (!_map135.containsKey(key))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map135, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key4, key5).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get145(final long key1, final long key4, final long key5) {
		if (_map145 == null)
			_map145 = _lazyLoad145();
		final LongArrayKey key = new LongArrayKey(key1, key4, key5);
		if (!_map145.containsKey(key))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map145, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key2, key3, key4).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get234(final long key2, final long key3, final long key4) {
		if (_map234 == null)
			_map234 = _lazyLoad234();
		final LongArrayKey key = new LongArrayKey(key2, key3, key4);
		if (!_map234.containsKey(key))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map234, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key2, key3, key5).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get235(final long key2, final long key3, final long key5) {
		if (_map235 == null)
			_map235 = _lazyLoad235();
		final LongArrayKey key = new LongArrayKey(key2, key3, key5);
		if (!_map235.containsKey(key))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map235, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key2, key4, key5).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get245(final long key2, final long key4, final long key5) {
		if (_map245 == null)
			_map245 = _lazyLoad245();
		final LongArrayKey key = new LongArrayKey(key2, key4, key5);
		if (!_map245.containsKey(key))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map245, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key3, key4, key5).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get345(final long key3, final long key4, final long key5) {
		if (_map345 == null)
			_map345 = _lazyLoad345();
		final LongArrayKey key = new LongArrayKey(key3, key4, key5);
		if (!_map345.containsKey(key))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map345, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key2, key3, key4).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get1234(final long key1, final long key2, final long key3, final long key4) {
		if (_map1234 == null)
			_map1234 = _lazyLoad1234();
		final LongArrayKey key = new LongArrayKey(key1, key2, key3, key4);
		if (!_map1234.containsKey(key))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map1234, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key2, key3, key5).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get1235(final long key1, final long key2, final long key3, final long key5) {
		if (_map1235 == null)
			_map1235 = _lazyLoad1235();
		final LongArrayKey key = new LongArrayKey(key1, key2, key3, key5);
		if (!_map1235.containsKey(key))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map1235, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key2, key4, key5).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get1245(final long key1, final long key2, final long key4, final long key5) {
		if (_map1245 == null)
			_map1245 = _lazyLoad1245();
		final LongArrayKey key = new LongArrayKey(key1, key2, key4, key5);
		if (!_map1245.containsKey(key))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map1245, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key3, key4, key5).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get1345(final long key1, final long key3, final long key4, final long key5) {
		if (_map1345 == null)
			_map1345 = _lazyLoad1345();
		final LongArrayKey key = new LongArrayKey(key1, key3, key4, key5);
		if (!_map1345.containsKey(key))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map1345, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key2, key3, key4, key5).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get2345(final long key2, final long key3, final long key4, final long key5) {
		if (_map2345 == null)
			_map2345 = _lazyLoad2345();
		final LongArrayKey key = new LongArrayKey(key2, key3, key4, key5);
		if (!_map2345.containsKey(key))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map2345, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key2, key3, key4, key5).
	 * Falls es kein Mapping gibt, wird eine leere Liste geliefert.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get12345(final long key1, final long key2, final long key3, final long key4, final long key5) {
		final LongArrayKey key = new LongArrayKey(key1, key2, key3, key4, key5);
		if (!_map12345.containsKey(key))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map12345, key));
	}

    /**
	 * Liefert das zugeordnete Element zum Mapping (key1), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key1   Der 1. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingle1OrNull(final long key1) {
		if (_map1 == null)
			_map1 = _lazyLoad1();
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
		if (_map2 == null)
			_map2 = _lazyLoad2();
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
		if (_map3 == null)
			_map3 = _lazyLoad3();
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
		if (_map4 == null)
			_map4 = _lazyLoad4();
		return getSingleOrNullHelperLong(_map4, key4);
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key5), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key5), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingle5OrNull(final long key5) {
		if (_map5 == null)
			_map5 = _lazyLoad5();
		return getSingleOrNullHelperLong(_map5, key5);
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
		if (_map12 == null)
			_map12 = _lazyLoad12();
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
		if (_map13 == null)
			_map13 = _lazyLoad13();
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
		if (_map14 == null)
			_map14 = _lazyLoad14();
		return getSingleOrNullHelperLongArray(_map14, new LongArrayKey(key1, key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key5), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key5), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingle15OrNull(final long key1, final long key5) {
		if (_map15 == null)
			_map15 = _lazyLoad15();
		return getSingleOrNullHelperLongArray(_map15, new LongArrayKey(key1, key5));
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
		if (_map23 == null)
			_map23 = _lazyLoad23();
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
		if (_map24 == null)
			_map24 = _lazyLoad24();
		return getSingleOrNullHelperLongArray(_map24, new LongArrayKey(key2, key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key2, key5), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key2, key5), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingle25OrNull(final long key2, final long key5) {
		if (_map25 == null)
			_map25 = _lazyLoad25();
		return getSingleOrNullHelperLongArray(_map25, new LongArrayKey(key2, key5));
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
		if (_map34 == null)
			_map34 = _lazyLoad34();
		return getSingleOrNullHelperLongArray(_map34, new LongArrayKey(key3, key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key3, key5), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key3, key5), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingle35OrNull(final long key3, final long key5) {
		if (_map35 == null)
			_map35 = _lazyLoad35();
		return getSingleOrNullHelperLongArray(_map35, new LongArrayKey(key3, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key4, key5), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key4, key5), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingle45OrNull(final long key4, final long key5) {
		if (_map45 == null)
			_map45 = _lazyLoad45();
		return getSingleOrNullHelperLongArray(_map45, new LongArrayKey(key4, key5));
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
		if (_map123 == null)
			_map123 = _lazyLoad123();
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
		if (_map124 == null)
			_map124 = _lazyLoad124();
		return getSingleOrNullHelperLongArray(_map124, new LongArrayKey(key1, key2, key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key2, key5), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key2, key5), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingle125OrNull(final long key1, final long key2, final long key5) {
		if (_map125 == null)
			_map125 = _lazyLoad125();
		return getSingleOrNullHelperLongArray(_map125, new LongArrayKey(key1, key2, key5));
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
		if (_map134 == null)
			_map134 = _lazyLoad134();
		return getSingleOrNullHelperLongArray(_map134, new LongArrayKey(key1, key3, key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key3, key5), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key3, key5), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingle135OrNull(final long key1, final long key3, final long key5) {
		if (_map135 == null)
			_map135 = _lazyLoad135();
		return getSingleOrNullHelperLongArray(_map135, new LongArrayKey(key1, key3, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key4, key5), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key4, key5), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingle145OrNull(final long key1, final long key4, final long key5) {
		if (_map145 == null)
			_map145 = _lazyLoad145();
		return getSingleOrNullHelperLongArray(_map145, new LongArrayKey(key1, key4, key5));
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
		if (_map234 == null)
			_map234 = _lazyLoad234();
		return getSingleOrNullHelperLongArray(_map234, new LongArrayKey(key2, key3, key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key2, key3, key5), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key2, key3, key5), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingle235OrNull(final long key2, final long key3, final long key5) {
		if (_map235 == null)
			_map235 = _lazyLoad235();
		return getSingleOrNullHelperLongArray(_map235, new LongArrayKey(key2, key3, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key2, key4, key5), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key2, key4, key5), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingle245OrNull(final long key2, final long key4, final long key5) {
		if (_map245 == null)
			_map245 = _lazyLoad245();
		return getSingleOrNullHelperLongArray(_map245, new LongArrayKey(key2, key4, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key3, key4, key5), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key3, key4, key5), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingle345OrNull(final long key3, final long key4, final long key5) {
		if (_map345 == null)
			_map345 = _lazyLoad345();
		return getSingleOrNullHelperLongArray(_map345, new LongArrayKey(key3, key4, key5));
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
		if (_map1234 == null)
			_map1234 = _lazyLoad1234();
		return getSingleOrNullHelperLongArray(_map1234, new LongArrayKey(key1, key2, key3, key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key2, key3, key5), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key2, key3, key5), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingle1235OrNull(final long key1, final long key2, final long key3, final long key5) {
		if (_map1235 == null)
			_map1235 = _lazyLoad1235();
		return getSingleOrNullHelperLongArray(_map1235, new LongArrayKey(key1, key2, key3, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key2, key4, key5), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key2, key4, key5), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingle1245OrNull(final long key1, final long key2, final long key4, final long key5) {
		if (_map1245 == null)
			_map1245 = _lazyLoad1245();
		return getSingleOrNullHelperLongArray(_map1245, new LongArrayKey(key1, key2, key4, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key3, key4, key5), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key3, key4, key5), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingle1345OrNull(final long key1, final long key3, final long key4, final long key5) {
		if (_map1345 == null)
			_map1345 = _lazyLoad1345();
		return getSingleOrNullHelperLongArray(_map1345, new LongArrayKey(key1, key3, key4, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key2, key3, key4, key5), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key2, key3, key4, key5), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingle2345OrNull(final long key2, final long key3, final long key4, final long key5) {
		if (_map2345 == null)
			_map2345 = _lazyLoad2345();
		return getSingleOrNullHelperLongArray(_map2345, new LongArrayKey(key2, key3, key4, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key2, key3, key4, key5), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key2, key3, key4, key5), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingle12345OrNull(final long key1, final long key2, final long key3, final long key4, final long key5) {
		return getSingleOrNullHelperLongArray(_map12345, new LongArrayKey(key1, key2, key3, key4, key5));
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
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingle1OrNull(key1));
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
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingle2OrNull(key2));
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
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingle3OrNull(key3));
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
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingle4OrNull(key4));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public @NotNull V getSingle5OrException(final long key5) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingle5OrNull(key5));
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
	 * Liefert das zugeordnete Element zum Mapping (key1, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public @NotNull V getSingle15OrException(final long key1, final long key5) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingle15OrNull(key1, key5));
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
	 * Liefert das zugeordnete Element zum Mapping (key2, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key2, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public @NotNull V getSingle25OrException(final long key2, final long key5) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingle25OrNull(key2, key5));
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
	 * Liefert das zugeordnete Element zum Mapping (key3, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key3, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public @NotNull V getSingle35OrException(final long key3, final long key5) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingle35OrNull(key3, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key4, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key4, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public @NotNull V getSingle45OrException(final long key4, final long key5) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingle45OrNull(key4, key5));
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
	 * Liefert das zugeordnete Element zum Mapping (key1, key2, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key2, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public @NotNull V getSingle125OrException(final long key1, final long key2, final long key5) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingle125OrNull(key1, key2, key5));
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
	 * Liefert das zugeordnete Element zum Mapping (key1, key3, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key3, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public @NotNull V getSingle135OrException(final long key1, final long key3, final long key5) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingle135OrNull(key1, key3, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key4, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key4, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public @NotNull V getSingle145OrException(final long key1, final long key4, final long key5) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingle145OrNull(key1, key4, key5));
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
	 * Liefert das zugeordnete Element zum Mapping (key2, key3, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key2, key3, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public @NotNull V getSingle235OrException(final long key2, final long key3, final long key5) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingle235OrNull(key2, key3, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key2, key4, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key2, key4, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public @NotNull V getSingle245OrException(final long key2, final long key4, final long key5) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingle245OrNull(key2, key4, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key3, key4, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key3, key4, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public @NotNull V getSingle345OrException(final long key3, final long key4, final long key5) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingle345OrNull(key3, key4, key5));
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
	 * Liefert das zugeordnete Element zum Mapping (key1, key2, key3, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key2, key3, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public @NotNull V getSingle1235OrException(final long key1, final long key2, final long key3, final long key5) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingle1235OrNull(key1, key2, key3, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key2, key4, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key2, key4, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public @NotNull V getSingle1245OrException(final long key1, final long key2, final long key4, final long key5) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingle1245OrNull(key1, key2, key4, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key3, key4, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key3, key4, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public @NotNull V getSingle1345OrException(final long key1, final long key3, final long key4, final long key5) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingle1345OrNull(key1, key3, key4, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key2, key3, key4, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key2, key3, key4, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public @NotNull V getSingle2345OrException(final long key2, final long key3, final long key4, final long key5) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingle2345OrNull(key2, key3, key4, key5));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key2, key3, key4, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key2, key3, key4, key5), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public @NotNull V getSingle12345OrException(final long key1, final long key2, final long key3, final long key4, final long key5) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingle12345OrNull(key1, key2, key3, key4, key5));
	}

    /**
	 * Liefert das Key-Set der Map1.
	 *
	 * @return das Key-Set der Map1.
	 */
	public @NotNull Set<Long> keySet1() {
		if (_map1 == null)
			_map1 = _lazyLoad1();
		return _map1.keySet();
	}

	/**
	 * Liefert das Key-Set der Map2.
	 *
	 * @return das Key-Set der Map2.
	 */
	public @NotNull Set<Long> keySet2() {
		if (_map2 == null)
			_map2 = _lazyLoad2();
		return _map2.keySet();
	}

	/**
	 * Liefert das Key-Set der Map3.
	 *
	 * @return das Key-Set der Map3.
	 */
	public @NotNull Set<Long> keySet3() {
		if (_map3 == null)
			_map3 = _lazyLoad3();
		return _map3.keySet();
	}

	/**
	 * Liefert das Key-Set der Map4.
	 *
	 * @return das Key-Set der Map4.
	 */
	public @NotNull Set<Long> keySet4() {
		if (_map4 == null)
			_map4 = _lazyLoad4();
		return _map4.keySet();
	}

	/**
	 * Liefert das Key-Set der Map5.
	 *
	 * @return das Key-Set der Map5.
	 */
	public @NotNull Set<Long> keySet5() {
		if (_map5 == null)
			_map5 = _lazyLoad5();
		return _map5.keySet();
	}

	/**
	 * Liefert das Key-Set der Map12.
	 *
	 * @return das Key-Set der Map12.
	 */
	public @NotNull Set<LongArrayKey> keySet12() {
		if (_map12 == null)
			_map12 = _lazyLoad12();
		return _map12.keySet();
	}

	/**
	 * Liefert das Key-Set der Map13.
	 *
	 * @return das Key-Set der Map13.
	 */
	public @NotNull Set<LongArrayKey> keySet13() {
		if (_map13 == null)
			_map13 = _lazyLoad13();
		return _map13.keySet();
	}

	/**
	 * Liefert das Key-Set der Map14.
	 *
	 * @return das Key-Set der Map14.
	 */
	public @NotNull Set<LongArrayKey> keySet14() {
		if (_map14 == null)
			_map14 = _lazyLoad14();
		return _map14.keySet();
	}

	/**
	 * Liefert das Key-Set der Map15.
	 *
	 * @return das Key-Set der Map15.
	 */
	public @NotNull Set<LongArrayKey> keySet15() {
		if (_map15 == null)
			_map15 = _lazyLoad15();
		return _map15.keySet();
	}

	/**
	 * Liefert das Key-Set der Map23.
	 *
	 * @return das Key-Set der Map23.
	 */
	public @NotNull Set<LongArrayKey> keySet23() {
		if (_map23 == null)
			_map23 = _lazyLoad23();
		return _map23.keySet();
	}

	/**
	 * Liefert das Key-Set der Map24.
	 *
	 * @return das Key-Set der Map24.
	 */
	public @NotNull Set<LongArrayKey> keySet24() {
		if (_map24 == null)
			_map24 = _lazyLoad24();
		return _map24.keySet();
	}

	/**
	 * Liefert das Key-Set der Map25.
	 *
	 * @return das Key-Set der Map25.
	 */
	public @NotNull Set<LongArrayKey> keySet25() {
		if (_map25 == null)
			_map25 = _lazyLoad25();
		return _map25.keySet();
	}

	/**
	 * Liefert das Key-Set der Map34.
	 *
	 * @return das Key-Set der Map34.
	 */
	public @NotNull Set<LongArrayKey> keySet34() {
		if (_map34 == null)
			_map34 = _lazyLoad34();
		return _map34.keySet();
	}

	/**
	 * Liefert das Key-Set der Map35.
	 *
	 * @return das Key-Set der Map35.
	 */
	public @NotNull Set<LongArrayKey> keySet35() {
		if (_map35 == null)
			_map35 = _lazyLoad35();
		return _map35.keySet();
	}

	/**
	 * Liefert das Key-Set der Map45.
	 *
	 * @return das Key-Set der Map45.
	 */
	public @NotNull Set<LongArrayKey> keySet45() {
		if (_map45 == null)
			_map45 = _lazyLoad45();
		return _map45.keySet();
	}

	/**
	 * Liefert das Key-Set der Map123.
	 *
	 * @return das Key-Set der Map123.
	 */
	public @NotNull Set<LongArrayKey> keySet123() {
		if (_map123 == null)
			_map123 = _lazyLoad123();
		return _map123.keySet();
	}

	/**
	 * Liefert das Key-Set der Map124.
	 *
	 * @return das Key-Set der Map124.
	 */
	public @NotNull Set<LongArrayKey> keySet124() {
		if (_map124 == null)
			_map124 = _lazyLoad124();
		return _map124.keySet();
	}

	/**
	 * Liefert das Key-Set der Map125.
	 *
	 * @return das Key-Set der Map125.
	 */
	public @NotNull Set<LongArrayKey> keySet125() {
		if (_map125 == null)
			_map125 = _lazyLoad125();
		return _map125.keySet();
	}

	/**
	 * Liefert das Key-Set der Map134.
	 *
	 * @return das Key-Set der Map134.
	 */
	public @NotNull Set<LongArrayKey> keySet134() {
		if (_map134 == null)
			_map134 = _lazyLoad134();
		return _map134.keySet();
	}

	/**
	 * Liefert das Key-Set der Map135.
	 *
	 * @return das Key-Set der Map135.
	 */
	public @NotNull Set<LongArrayKey> keySet135() {
		if (_map135 == null)
			_map135 = _lazyLoad135();
		return _map135.keySet();
	}

	/**
	 * Liefert das Key-Set der Map145.
	 *
	 * @return das Key-Set der Map145.
	 */
	public @NotNull Set<LongArrayKey> keySet145() {
		if (_map145 == null)
			_map145 = _lazyLoad145();
		return _map145.keySet();
	}

	/**
	 * Liefert das Key-Set der Map234.
	 *
	 * @return das Key-Set der Map234.
	 */
	public @NotNull Set<LongArrayKey> keySet234() {
		if (_map234 == null)
			_map234 = _lazyLoad234();
		return _map234.keySet();
	}

	/**
	 * Liefert das Key-Set der Map235.
	 *
	 * @return das Key-Set der Map235.
	 */
	public @NotNull Set<LongArrayKey> keySet235() {
		if (_map235 == null)
			_map235 = _lazyLoad235();
		return _map235.keySet();
	}

	/**
	 * Liefert das Key-Set der Map245.
	 *
	 * @return das Key-Set der Map245.
	 */
	public @NotNull Set<LongArrayKey> keySet245() {
		if (_map245 == null)
			_map245 = _lazyLoad245();
		return _map245.keySet();
	}

	/**
	 * Liefert das Key-Set der Map345.
	 *
	 * @return das Key-Set der Map345.
	 */
	public @NotNull Set<LongArrayKey> keySet345() {
		if (_map345 == null)
			_map345 = _lazyLoad345();
		return _map345.keySet();
	}

	/**
	 * Liefert das Key-Set der Map1234.
	 *
	 * @return das Key-Set der Map1234.
	 */
	public @NotNull Set<LongArrayKey> keySet1234() {
		if (_map1234 == null)
			_map1234 = _lazyLoad1234();
		return _map1234.keySet();
	}

	/**
	 * Liefert das Key-Set der Map1235.
	 *
	 * @return das Key-Set der Map1235.
	 */
	public @NotNull Set<LongArrayKey> keySet1235() {
		if (_map1235 == null)
			_map1235 = _lazyLoad1235();
		return _map1235.keySet();
	}

	/**
	 * Liefert das Key-Set der Map1245.
	 *
	 * @return das Key-Set der Map1245.
	 */
	public @NotNull Set<LongArrayKey> keySet1245() {
		if (_map1245 == null)
			_map1245 = _lazyLoad1245();
		return _map1245.keySet();
	}

	/**
	 * Liefert das Key-Set der Map1345.
	 *
	 * @return das Key-Set der Map1345.
	 */
	public @NotNull Set<LongArrayKey> keySet1345() {
		if (_map1345 == null)
			_map1345 = _lazyLoad1345();
		return _map1345.keySet();
	}

	/**
	 * Liefert das Key-Set der Map2345.
	 *
	 * @return das Key-Set der Map2345.
	 */
	public @NotNull Set<LongArrayKey> keySet2345() {
		if (_map2345 == null)
			_map2345 = _lazyLoad2345();
		return _map2345.keySet();
	}

	/**
	 * Liefert das Key-Set der Map12345.
	 *
	 * @return das Key-Set der Map12345.
	 */
	public @NotNull Set<LongArrayKey> keySet12345() {
		return _map12345.keySet();
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
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !containsKey1(key1));
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
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !containsKey2(key2));
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
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key5).
	 *
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public @NotNull List<V> get5OrException(final long key5) {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !containsKey5(key5));
		return get5(key5);
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
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key1, key5).
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public @NotNull List<V> get15OrException(final long key1, final long key5) {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !containsKey15(key1, key5));
		return get15(key1, key5);
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
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key2, key5).
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public @NotNull List<V> get25OrException(final long key2, final long key5) {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !containsKey25(key2, key5));
		return get25(key2, key5);
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
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key3, key5).
	 *
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public @NotNull List<V> get35OrException(final long key3, final long key5) {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !containsKey35(key3, key5));
		return get35(key3, key5);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key4, key5).
	 *
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public @NotNull List<V> get45OrException(final long key4, final long key5) {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !containsKey45(key4, key5));
		return get45(key4, key5);
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
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key1, key2, key5).
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public @NotNull List<V> get125OrException(final long key1, final long key2, final long key5) {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !containsKey125(key1, key2, key5));
		return get125(key1, key2, key5);
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
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key1, key3, key5).
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public @NotNull List<V> get135OrException(final long key1, final long key3, final long key5) {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !containsKey135(key1, key3, key5));
		return get135(key1, key3, key5);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key1, key4, key5).
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public @NotNull List<V> get145OrException(final long key1, final long key4, final long key5) {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !containsKey145(key1, key4, key5));
		return get145(key1, key4, key5);
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
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key2, key3, key5).
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public @NotNull List<V> get235OrException(final long key2, final long key3, final long key5) {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !containsKey235(key2, key3, key5));
		return get235(key2, key3, key5);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key2, key4, key5).
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public @NotNull List<V> get245OrException(final long key2, final long key4, final long key5) {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !containsKey245(key2, key4, key5));
		return get245(key2, key4, key5);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key3, key4, key5).
	 *
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public @NotNull List<V> get345OrException(final long key3, final long key4, final long key5) {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !containsKey345(key3, key4, key5));
		return get345(key3, key4, key5);
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

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key1, key2, key3, key5).
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public @NotNull List<V> get1235OrException(final long key1, final long key2, final long key3, final long key5) {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !containsKey1235(key1, key2, key3, key5));
		return get1235(key1, key2, key3, key5);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key1, key2, key4, key5).
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public @NotNull List<V> get1245OrException(final long key1, final long key2, final long key4, final long key5) {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !containsKey1245(key1, key2, key4, key5));
		return get1245(key1, key2, key4, key5);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key1, key3, key4, key5).
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public @NotNull List<V> get1345OrException(final long key1, final long key3, final long key4, final long key5) {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !containsKey1345(key1, key3, key4, key5));
		return get1345(key1, key3, key4, key5);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key2, key3, key4, key5).
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public @NotNull List<V> get2345OrException(final long key2, final long key3, final long key4, final long key5) {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !containsKey2345(key2, key3, key4, key5));
		return get2345(key2, key3, key4, key5);
	}

	/**
	 * Liefert eine Kopie der Liste aller zugeordneten Values zum Mapping (key1, key2, key3, key4, key5).
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 * @param key4   Der 4. Schlüssel.
	 * @param key5   Der 5. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 * @throws DeveloperNotificationException falls es kein Mapping gibt.
	 */
	public @NotNull List<V> get12345OrException(final long key1, final long key2, final long key3, final long key4, final long key5) {
		DeveloperNotificationException.ifTrue("Es keine Liste zugeordnet.", !containsKey12345(key1, key2, key3, key4, key5));
		return get12345(key1, key2, key3, key4, key5);
	}

}
