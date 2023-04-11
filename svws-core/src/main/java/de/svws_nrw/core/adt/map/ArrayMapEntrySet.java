package de.svws_nrw.core.adt.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse implementiert das Entry-Set für die Klasse {@link ArrayMap}.
 * Es handelt sich um eine View, welche auf der zugrundeliegenden {@link ArrayMap}
 * operiert.
 *
 * @author Thomas Bachran
 *
 * @param <K> Der Typ der Schlüsselwerte.
 * @param <V> Der Typ der zugeordneten Werte.
 */
final class ArrayMapEntrySet<@NotNull K, @NotNull V> implements Set<Map.@NotNull Entry<@NotNull K, @NotNull V>> {

	/** Die {@link ArrayMap} zu der dieses Entry-Set gehört. */
	private final @NotNull ArrayMap<@NotNull K, @NotNull V> _map;

	/**
	 * Erstellt eine neues Entry-Set für die übergebene {@link ArrayMap}.
	 *
	 * @param map   die {@link ArrayMap}, zu welcher dieses Entry-Set gehört
	 */
	ArrayMapEntrySet(final @NotNull ArrayMap<@NotNull K, @NotNull V> map) {
		_map = map;
	}

	@Override
	public int size() {
		return _map.size();
	}

	@Override
	public boolean isEmpty() {
		return _map.isEmpty();
	}

	private Entry<@NotNull K, @NotNull V> toEntry(final Object obj) {
		if (obj == null)
			return null;
		if (!(obj instanceof Entry))
			return null;
		@SuppressWarnings("unchecked")
		final @NotNull Entry<@NotNull K, @NotNull V> entry = (@NotNull Entry<@NotNull K, @NotNull V>) obj;
		return entry;
	}

	private boolean containsEntry(final Entry<@NotNull K, @NotNull V> entry) {
		if (entry == null)
			return false;
		if (!_map.containsKey(entry.getKey()))
			return false;
		final V value = _map.get(entry.getKey());
		if (value == null)
			return false;
		return value.equals(entry.getValue());
	}

	@Override
	public boolean contains(final Object obj) {
		final Entry<@NotNull K, @NotNull V> entry = toEntry(obj);
		if (entry == null)
			return false;
		return containsEntry(entry);
	}

	@Override
	public @NotNull Iterator<@NotNull Entry<@NotNull K, @NotNull V>> iterator() {
		return new ArrayMapEntrySetIterator<>(_map);
	}

	private @NotNull List<@NotNull ArrayMapEntry<@NotNull K, @NotNull V>> getEntryList() {
		final @NotNull ArrayList<@NotNull ArrayMapEntry<@NotNull K, @NotNull V>> list = new ArrayList<>(_map.size());
		for (int i = 0; i < _map.getNumberOfKeys(); i++) {
			final ArrayMapEntry<@NotNull K, @NotNull V> value = _map.getEntryByIndex(i);
			if (value != null)
				list.add(value);
		}
		return list;
	}

	@Override
	public @NotNull Object@NotNull[] toArray() {
		return getEntryList().toArray();
	}

	@Override
	public <@NotNull T> @NotNull T@NotNull[] toArray(final @NotNull T@NotNull[] a) {
		return getEntryList().toArray(a);
	}

	@Override
	public boolean add(final @NotNull Entry<@NotNull K, @NotNull V> e) {
		if (e == null)
			return false;
		_map.put(e.getKey(), e.getValue());
		return true;
	}

	@Override
	public boolean remove(final Object obj) {
		final Entry<@NotNull K, @NotNull V> entry = toEntry(obj);
		if (entry == null)
			return false;
		if (!containsEntry(entry))
			return false;
		_map.remove(entry.getKey());
		return true;
	}

	@Override
	public boolean containsAll(final Collection<?> collection) {
		if ((collection == null) || (this == collection))
			return true;
		for (final Object obj : collection)
			if (!contains(obj))
				return false;
		return true;
	}

	@Override
	public boolean addAll(final Collection<? extends @NotNull Entry<@NotNull K, @NotNull V>> collection) {
		if (collection == null)
			throw new NullPointerException();
		if (this == collection)
			return true;
		for (final @NotNull Entry<@NotNull K, @NotNull V> entry : collection)
			if (!add(entry))
				return false;
		return true;
	}

	@Override
	public boolean retainAll(final Collection<?> collection) {
		if (collection == null)
			throw new NullPointerException();
		boolean changed = false;
		for (int i = 0; i < _map.getNumberOfKeys(); i++) {
			final ArrayMapEntry<@NotNull K, @NotNull V> entry = _map.getEntryByIndex(i);
			if (entry == null)
				continue;
			if (!collection.contains(entry)) {
				_map.remove(entry.getKey());
				changed = true;
			}
		}
		return changed;
	}

	@Override
	public boolean removeAll(final Collection<?> collection) {
		if (collection == null)
			throw new NullPointerException();
		boolean changed = false;
		for (final Object obj : collection) {
			if (contains(obj)) {
				remove(obj);
				changed = true;
			}
		}
		return changed;
	}

	@Override
	public void clear() {
		_map.clear();
	}

}
