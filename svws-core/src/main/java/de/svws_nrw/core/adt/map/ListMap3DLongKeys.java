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
 * Diese 3D-List-Map ordnet 3 Schlüssel auf eine Liste von Werten (V) ab.
 * <br> Diese spezielle Map stellt Zugriffsmethoden für alle Kombinationen der Schlüssel auf die Werte (V) zur Verfügung.
 * <br> Die Einfüge-Reihenfolge bleibt bei allen Listen erhalten.
 * <br> Ein Entfernen aus der Datenstruktur ist nicht vorgesehen.
 *
 * @param <V> Der Typ der zugeordneten Werte.
 */
public class ListMap3DLongKeys<V> {

	private Map<Long, List<V>> _map1 = null;
	private Map<Long, List<V>> _map2 = null;
	private Map<Long, List<V>> _map3 = null;

	private Map<LongArrayKey, List<V>> _map12 = null;
	private Map<LongArrayKey, List<V>> _map13 = null;
	private Map<LongArrayKey, List<V>> _map23 = null;

	private final @NotNull Map<LongArrayKey, List<V>> _map123 = new HashMap<>();
	private final @NotNull List<Pair<LongArrayKey, V>> _list = new ArrayList<>();

	/**
	 * Konstruktor.
	 */
	public ListMap3DLongKeys() {
		// lerr
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
	 *
	 * @param value Der zugeordnete Wert. Der Wert NULL ist nicht erlaubt.
	 */
	public void add(final long key1, final long key2, final long key3, final @NotNull V value) {
		final @NotNull LongArrayKey key = new LongArrayKey(key1, key2, key3);
		MapUtils.getOrCreateArrayList(_map123, key).add(value);
		_list.add(new Pair<>(key, value));

		if (_map1 != null)
			MapUtils.getOrCreateArrayList(_map1, key1).add(value);
		if (_map2 != null)
			MapUtils.getOrCreateArrayList(_map2, key2).add(value);
		if (_map3 != null)
			MapUtils.getOrCreateArrayList(_map3, key3).add(value);

		if (_map12 != null)
			MapUtils.getOrCreateArrayList(_map12, new LongArrayKey(key1, key2)).add(value);
		if (_map13 != null)
			MapUtils.getOrCreateArrayList(_map13, new LongArrayKey(key1, key3)).add(value);
		if (_map23 != null)
			MapUtils.getOrCreateArrayList(_map23, new LongArrayKey(key2, key3)).add(value);
	}

	/**
	 * Erzeugt den Pfad (key1, key2, key3) fügt aber nichts hinzu.
	 * Alle Pfade, die es vorher nicht gab, verweisen dann auf leere Listen.
	 *
	 * @param key1  Der 1. Schlüssel.
	 * @param key2  Der 2. Schlüssel.
	 * @param key3  Der 3. Schlüssel.
	 */
	public void addEmpty(final long key1, final long key2, final long key3) {
		final @NotNull LongArrayKey key = new LongArrayKey(key1, key2, key3);
		MapUtils.getOrCreateArrayList(_map123, key);
		_list.add(new Pair<>(key, null));

		if (_map1 != null)
			MapUtils.getOrCreateArrayList(_map1, key1);
		if (_map2 != null)
			MapUtils.getOrCreateArrayList(_map2, key2);
		if (_map3 != null)
			MapUtils.getOrCreateArrayList(_map3, key3);

		if (_map12 != null)
			MapUtils.getOrCreateArrayList(_map12, new LongArrayKey(key1, key2));
		if (_map13 != null)
			MapUtils.getOrCreateArrayList(_map13, new LongArrayKey(key1, key3));
		if (_map23 != null)
			MapUtils.getOrCreateArrayList(_map23, new LongArrayKey(key2, key3));
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
	 * Liefert TRUE, falls es den Schlüssel (key1, key2, key3) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key1, key2, key3) gibt.
	 */
	public boolean containsKey123(final long key1, final long key2, final long key3) {
		return _map123.containsKey(new LongArrayKey(key1, key2, key3));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1).
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
	 *
	 * @param key3   Der 3. Schlüssel
	 *
	 * @return eine Kopie der Liste aller Values zum Mapping (key3).
	 */
	public @NotNull List<V> get3(final long key3) {
		if (_map3 == null)
			_map3 = _lazyLoad3();
		if (!_map3.containsKey(key3))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map3, key3));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key2).
	 *
	 * @param key1   Der 1. Schlüssel
	 * @param key2   Der 2. Schlüssel
	 *
	 * @return eine Kopie der Liste aller Values zum Mapping (key1, key2).
	 */
	public @NotNull List<V> get12(final long key1, final long key2) {
		if (_map12 == null)
			_map12 = _lazyLoad12();
		final @NotNull LongArrayKey key = new LongArrayKey(key1, key2);
		if (!_map12.containsKey(key))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map12, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key3).
	 *
	 * @param key1   Der 1. Schlüssel
	 * @param key3   Der 3. Schlüssel
	 *
	 * @return eine Kopie der Liste aller Values zum Mapping (key1, key3).
	 */
	public @NotNull List<V> get13(final long key1, final long key3) {
		if (_map13 == null)
			_map13 = _lazyLoad13();
		final @NotNull LongArrayKey key = new LongArrayKey(key1, key3);
		if (!_map13.containsKey(key))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map13, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key2, key3).
	 *
	 * @param key2   Der 2. Schlüssel
	 * @param key3   Der 3. Schlüssel
	 *
	 * @return eine Kopie der Liste aller Values zum Mapping (key2, key3).
	 */
	public @NotNull List<V> get23(final long key2, final long key3) {
		if (_map23 == null)
			_map23 = _lazyLoad23();
		final @NotNull LongArrayKey key = new LongArrayKey(key2, key3);
		if (!_map23.containsKey(key))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map23, key));
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key2, key3).
	 *
	 * @param key1   Der 1. Schlüssel
	 * @param key2   Der 2. Schlüssel
	 * @param key3   Der 3. Schlüssel
	 *
	 * @return eine Kopie der Liste aller Values zum Mapping (key1, key2, key3).
	 */
	public @NotNull List<V> get123(final long key1, final long key2, final long key3) {
		final @NotNull LongArrayKey key = new LongArrayKey(key1, key2, key3);
		if (!_map123.containsKey(key))
			return new ArrayList<>();
		return new ArrayList<>(MapUtils.getOrCreateArrayList(_map123, key));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key1   Der 1. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingleOrNull1(final long key1) {
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
	public V getSingleOrNull2(final long key2) {
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
	public V getSingleOrNull3(final long key3) {
		if (_map3 == null)
			_map3 = _lazyLoad3();
		return getSingleOrNullHelperLong(_map3, key3);
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1, key2), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1, key2), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingleOrNull12(final long key1, final long key2) {
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
	public V getSingleOrNull13(final long key1, final long key3) {
		if (_map13 == null)
			_map13 = _lazyLoad13();
		return getSingleOrNullHelperLongArray(_map13, new LongArrayKey(key1, key3));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key2, key3), falls es genau eines gibt, andernfalls NULL.
	 *
	 * @param key2   Der 2. Schlüssel.
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key2, key3), falls es genau eines gibt, andernfalls NULL.
	 */
	public V getSingleOrNull23(final long key2, final long key3) {
		if (_map23 == null)
			_map23 = _lazyLoad23();
		return getSingleOrNullHelperLongArray(_map23, new LongArrayKey(key2, key3));
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
	public V getSingleOrNull123(final long key1, final long key2, final long key3) {
		return getSingleOrNullHelperLongArray(_map123, new LongArrayKey(key1, key2, key3));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key1), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key1   Der 1. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key1), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public @NotNull V getSingleOrException1(final long key1) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingleOrNull1(key1));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key2), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key2), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public @NotNull V getSingleOrException2(final long key2) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingleOrNull2(key2));
	}

	/**
	 * Liefert das zugeordnete Element zum Mapping (key3), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 *
	 * @param key3   Der 3. Schlüssel.
	 *
	 * @return das zugeordnete Element zum Mapping (key3), falls es genau eines gibt, andernfalls wird eine Exception geworfen.
	 * @throws DeveloperNotificationException falls nicht genau ein Element zugeordnet ist.
	 */
	public @NotNull V getSingleOrException3(final long key3) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingleOrNull3(key3));
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
	public @NotNull V getSingleOrException12(final long key1, final long key2) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingleOrNull12(key1, key2));
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
	public @NotNull V getSingleOrException13(final long key1, final long key3) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingleOrNull13(key1, key3));
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
	public @NotNull V getSingleOrException23(final long key2, final long key3) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingleOrNull23(key2, key3));
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
	public @NotNull V getSingleOrException123(final long key1, final long key2, final long key3) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Das Element ist nicht eindeutig!", getSingleOrNull123(key1, key2, key3));
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
	 * Liefert das Key-Set der Map123.
	 *
	 * @return das Key-Set der Map123.
	 */
	public @NotNull Set<LongArrayKey> keySet123() {
		return _map123.keySet();
	}
}
