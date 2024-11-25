package de.svws_nrw.core.adt.map;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.adt.LongArrayKey;
import de.svws_nrw.core.utils.MapUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Diese 2D-List-Map ordnet 2 Schlüssel auf eine Liste von Werten (V) ab.
 * Diese spezielle Map stellt für alle Teilmengen der 2 KEYs Zugriffsmethoden auf die Werte (V) zur Verfügung.
 *
 * @param <V> Der Typ des zugeordneten Wertes.
 */
public class ListMap2DMultiKeyLong<V> {

	private final @NotNull Map<LongArrayKey, List<V>> _map12 = new HashMap<>();
	private final @NotNull Map<LongArrayKey, List<V>> _map1 = new HashMap<>();
	private final @NotNull Map<LongArrayKey, List<V>> _map2 = new HashMap<>();

	/**
	 * Konstruktor.
	 */
	public ListMap2DMultiKeyLong() {
		// no implementation
	}

	/**
	 * Fügt das Element allen Listen hinzu.
	 *
	 * @param key1  Der 1. Schlüssel des Paares (key1, key2).
	 * @param key2  Der 2. Schlüssel des Paares (key1, key2).
	 *
	 * @param value Der zugeordnete Wert. Der Wert null ist erlaubt.
	 */
	public void add(final long key1, final long key2, final @NotNull V value) {
		MapUtils.getOrCreateArrayList(_map12, new LongArrayKey(key1, key2)).add(value);

		MapUtils.getOrCreateArrayList(_map1, new LongArrayKey(key1)).add(value);
		MapUtils.getOrCreateArrayList(_map2, new LongArrayKey(key2)).add(value);
	}

	/**
	 * Entfernt das Element aus den Listen.
	 *
	 * @param key1  Der 1. Schlüssel des Paares (key1, key2).
	 * @param key2  Der 2. Schlüssel des Paares (key1, key2).
	 *
	 * @param value Der zugeordnete Wert. Der Wert null ist erlaubt.
	 */
	public void remove(final long key1, final long key2, final @NotNull V value) {
		MapUtils.getOrCreateArrayList(_map12, new LongArrayKey(key1, key2)).remove(value);

		MapUtils.getOrCreateArrayList(_map1, new LongArrayKey(key1)).remove(value);
		MapUtils.getOrCreateArrayList(_map2, new LongArrayKey(key2)).remove(value);
	}

	/**
	 * Liefert eine Liste aller Values zum Mapping (key1, key2).
	 *
	 * @param key1  Der 1. Schlüssel des Paares (key1, key2).
	 * @param key2  Der 2. Schlüssel des Paares (key1, key2).
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get12(final long key1, final long key2) {
		return MapUtils.getOrCreateArrayList(_map12, new LongArrayKey(key1, key2));
	}

	/**
	 * Liefert eine Liste aller Values zum Mapping (key1).
	 *
	 * @param key1  Der 1. Schlüssel des Paares (key1, key2).
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get1(final long key1) {
		return MapUtils.getOrCreateArrayList(_map1, new LongArrayKey(key1));
	}

	/**
	 * Liefert eine Liste aller Values zum Mapping (key2).
	 *
	 * @param key2  Der 2. Schlüssel des des Paares (key1, key2).
	 *
	 * @return eine Liste aller Values in dieser Zuordnung.
	 */
	public @NotNull List<V> get2(final long key2) {
		return MapUtils.getOrCreateArrayList(_map2, new LongArrayKey(key2));
	}

}
