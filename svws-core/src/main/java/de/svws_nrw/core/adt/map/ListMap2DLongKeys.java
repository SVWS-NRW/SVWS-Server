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
 * Diese 2D-List-Map ordnet 2 Schlüssel auf eine Liste von Werten (V) ab.
 * <br> Diese spezielle Map stellt Zugriffsmethoden für alle Kombinationen der Schlüssel auf die Werte (V) zur Verfügung.
 * <br> Die Einfüge-Reihenfolge bleibt bei allen Listen erhalten.
 * <br> Ein Entfernen aus der Datenstruktur ist im Allgemeinen nicht sinnvoll, da jeder Löschvorgang O(n) Laufzeit hat und zusätzlich bei den folgenden Zugriffen alle Cache-Maps neu aufgebaut werden müssen.
 *
 * @param <V> Der Typ der zugeordneten Werte.
 */
public class ListMap2DLongKeys<V> {

	private Map<Long, List<V>> _map1 = null;
	private Map<Long, List<V>> _map2 = null;

	private final @NotNull Map<LongArrayKey, List<V>> _map12 = new HashMap<>();

	/**
	 * Konstruktor.
	 */
	public ListMap2DLongKeys() {
		// leer
	}

	private @NotNull Map<Long, List<V>> getMap1() {
		if (_map1 == null)
			_map1 = _lazyLoad1();
		return _map1;
	}

	private @NotNull Map<Long, List<V>> getMap2() {
		if (_map2 == null)
			_map2 = _lazyLoad2();
		return _map2;
	}

	private @NotNull Map<Long, List<V>> _lazyLoad1() {
		final Map<Long, List<V>> map = new HashMap<>();

		for (final @NotNull Entry<LongArrayKey, List<V>> entry12 : _map12.entrySet()) {
			final long key1 = entry12.getKey().getKeyAt(0);
			if (entry12.getValue().isEmpty())
				MapUtils.getOrCreateArrayList(map, key1);
			else
				MapUtils.getOrCreateArrayList(map, key1).addAll(entry12.getValue());
		}
		return map;
	}

	private @NotNull Map<Long, List<V>> _lazyLoad2() {
	    final Map<Long, List<V>> map = new HashMap<>();

	    for (final @NotNull Entry<LongArrayKey, List<V>> entry12 : _map12.entrySet()) {
	        final long key2 = entry12.getKey().getKeyAt(1);
	        if (entry12.getValue().isEmpty())
	            MapUtils.getOrCreateArrayList(map, key2);
	        else
	            MapUtils.getOrCreateArrayList(map, key2).addAll(entry12.getValue());
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

		if (_map1 != null)
			MapUtils.getOrCreateArrayList(_map1, key1);
		if (_map2 != null)
			MapUtils.getOrCreateArrayList(_map2, key2);
	}

	private void invalidateCaches() {
		_map1 = null;
		_map2 = null;
	}

	/**
	 * Entfernt den Wert aus der zur Zuordnung (key1, key2) gehörenden Value-Liste.
	 * Falls es den Pfad (key1) oder (key1, key2) nicht gibt, wird eine Exception geworfen.
	 * Alle Caches der Map werden gelöscht und müssen beim ersten Zugriff neu aufgebaut werden.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param value  Der zu entfernende Wert.
	 */
	public void removeValueOrException(final long key1, final long key2, final @NotNull V value) {
		final @NotNull LongArrayKey key = new LongArrayKey(key1, key2);
		MapUtils.removeFromListAndTrimOrException(_map12, key, value);
		invalidateCaches();
	}

	/**
	 * Entfernt den Pfad (key1, key2) aus der Map.
	 * Alle Caches der Map werden gelöscht und müssen beim ersten Zugriff neu aufgebaut werden.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return der bisherige Wert zu (key1, key2) oder {@code null}, falls nicht existent.
	 */
	public List<V> remove(final long key1, final long key2) {
	    final @NotNull LongArrayKey key = new LongArrayKey(key1, key2);
	    final List<V> values = _map12.remove(key);
	    if (values != null)
	    	invalidateCaches();
	    return values;
	}

	/**
	 * Entfernt den Pfad (key1, key2) aus der Map.
	 * Falls es den Pfad (key1) oder (key1, key2) nicht gibt, wird eine Exception geworfen.
	 * Alle Caches der Map werden gelöscht und müssen beim ersten Zugriff neu aufgebaut werden.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return der bisherige Wert zu (key1, key2)
	 */
	public @NotNull List<V> removeOrException(final long key1, final long key2) {
	    return DeveloperNotificationException.ifNull("Pfad (" + key1 + ", " + key2 + ") existiert nicht!", remove(key1, key2));
	}

	/**
	 * Entfernt den Pfad (key1, key2) aus der Map.
	 * Wirft eine DeveloperNotificationException, falls in der gemappten Liste das Element nicht als einziges enthalten ist.
	 * Falls es den Pfad (key1) oder (key1, key2) nicht gibt, wird eine Exception geworfen.
	 * Alle Caches der Map werden gelöscht und müssen beim ersten Zugriff neu aufgebaut werden.
	 *
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return der bisherige Wert zu (key1, key2)
	 */
	public @NotNull V removeSingleOrException(final long key1, final long key2) {
	    final List<V> values = removeOrException(key1, key2);
	    DeveloperNotificationException.ifTrue(
	        "Pfad (" + key1 + ", " + key2 + ") enthält nicht genau ein Element (tatsächlich "
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

	private void removeAllByKeyX(final long key, final int x) {
	    final List<LongArrayKey> toRemove = new ArrayList<>();
	    for (final LongArrayKey keyEntry : _map12.keySet())
	        if (keyEntry.getKeyAt(x) == key)
	            toRemove.add(keyEntry);
	    for (final LongArrayKey keyEntry : toRemove)
	        _map12.remove(keyEntry);

	    if (!toRemove.isEmpty())
	    	invalidateCaches();
	}

	/**
	 * Gibt eine flache Liste aller Values in dieser 2D-ListMap zurück.
	 * Die Einfüge-Reihenfolge der einzelnen Listen bleibt erhalten.
	 *
	 * @return eine flache Liste aller enthaltenen Werte
	 */
	public @NotNull List<V> getAllValues() {
	    final List<V> result = new ArrayList<>();
	    for (final List<V> values : _map12.values()) {
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
		return getMap1().containsKey(key1);
	}

	/**
	 * Liefert TRUE, falls es den Schlüssel (key2) gibt.
	 *
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return TRUE, falls es den Schlüssel (key2) gibt.
	 */
	public boolean containsKey2(final long key2) {
		return getMap2().containsKey(key2);
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
		List<V> list = getMap1().get(key1);
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
		List<V> list = getMap2().get(key2);
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
	public V getSingle1OrNull(final long key1) {
		List<V> list = getMap1().get(key1);
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
	public V getSingle2OrNull(final long key2) {
		List<V> list = getMap2().get(key2);
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
	public V getSingle12OrNull(final long key1, final long key2) {
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
	 * Liefert das Key-Set der Map1.
	 *
	 * @return das Key-Set der Map1.
	 */
	public @NotNull Set<Long> keySet1() {
		return getMap1().keySet();
	}

	/**
	 * Liefert das Key-Set der Map2.
	 *
	 * @return das Key-Set der Map2.
	 */
	public @NotNull Set<Long> keySet2() {
		return getMap2().keySet();
	}

	/**
	 * Liefert das Key-Set der Map12.
	 *
	 * @return das Key-Set der Map12.
	 */
	public @NotNull Set<LongArrayKey> keySet12() {
		return _map12.keySet();
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

}
