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
 * Diese 2D-List-Map ordnet 2 Schlüssel auf eine Liste von Werten (V) ab.
 * <br> Diese spezielle Map stellt Zugriffsmethoden für alle Kombinationen der Schlüssel auf die Werte (V) zur Verfügung.
 * <br> Die Einfüge-Reihenfolge bleibt bei allen Listen erhalten.
 * <br> Ein Entfernen aus der Datenstruktur ist nicht vorgesehen.
 *
 * @param <V> Der Typ der zugeordneten Werte.
 */
public class ListMap2DLongKeys<V> {

	private Map<Long, List<V>> _map1 = null;
	private Map<Long, List<V>> _map2 = null;

	private final @NotNull Map<LongArrayKey, List<V>> _map12 = new HashMap<>();
	private final @NotNull List<Pair<LongArrayKey, V>> _list = new ArrayList<>();

	/**
	 * Konstruktor.
	 */
	public ListMap2DLongKeys() {
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

	/**
	 * Fügt das Element hinzu.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @param value Der zugeordnete Wert. Der Wert NULL ist nicht erlaubt.
	 */
	public void add(final long key1, final long key2, final @NotNull V value) {
		final @NotNull LongArrayKey key = new LongArrayKey(key1, key2);
		MapUtils.getOrCreateArrayList(_map12, key).add(value);
		_list.add(new Pair<>(key, value));

		if (_map1 != null)
			MapUtils.getOrCreateArrayList(_map1, key1).add(value);
		if (_map2 != null)
			MapUtils.getOrCreateArrayList(_map2, key2).add(value);
	}

	/**
	 * Erzeugt den Pfad (key1, key2) fügt aber nichts hinzu.
	 * Alle Pfad, die es vorher nicht gab, verweisen dann auf leere Listen.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 */
	public void addEmpty(final long key1, final long key2) {
		final @NotNull LongArrayKey key12 = new LongArrayKey(key1, key2);
		MapUtils.getOrCreateArrayList(_map12, key12);
		_list.add(new Pair<>(key12, null));

		if (_map1 != null)
			MapUtils.getOrCreateArrayList(_map1, key1);
		if (_map2 != null)
			MapUtils.getOrCreateArrayList(_map2, key2);
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
	 * Liefert TRUE, falls es den Schlüssel (key1, key2) gibt.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key1, key2) gibt.
	 */
	public boolean containsKey12(final long key1, final long key2) {
		return _map12.containsKey(new LongArrayKey(key1, key2));
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
		List<V> list = _map1.get(key1);
		if (list == null)
			return new ArrayList<>();
		return new ArrayList<>(list);
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
		List<V> list = _map2.get(key2);
		if (list == null)
			return new ArrayList<>();
		return new ArrayList<>(list);
	}

	/**
	 * Liefert eine Kopie der Liste aller Values zum Mapping (key1, key2).
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get12(final long key1, final long key2) {
		final @NotNull LongArrayKey key12 = new LongArrayKey(key1, key2);
		List<V> list = _map12.get(key12);
		if (list == null)
			return new ArrayList<>();
		return new ArrayList<>(list);
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
		List<V> list = _map1.get(key1);
		if (list == null)
			return null;
		if (list.size() != 1)
			return null;
		return list.getFirst();
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
		List<V> list = _map2.get(key2);
		if (list == null)
			return null;
		if (list.size() != 1)
			return null;
		return list.getFirst();
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
		final @NotNull LongArrayKey key12 = new LongArrayKey(key1, key2);
		List<V> list = _map12.get(key12);
		if (list == null)
			return null;
		if (list.size() != 1)
			return null;
		return list.getFirst();
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
	 * Liefert das Key-Set der Map12.
	 *
	 * @return das Key-Set der Map12.
	 */
	public @NotNull Set<LongArrayKey> keySet12() {
		return _map12.keySet();
	}
}
