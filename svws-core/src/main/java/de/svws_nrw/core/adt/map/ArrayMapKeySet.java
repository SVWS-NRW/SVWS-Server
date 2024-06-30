package de.svws_nrw.core.adt.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse implementiert das Key-Set für die Klasse {@link ArrayMap}.
 * Es handelt sich um eine View, welche auf der zugrundeliegenden {@link ArrayMap}
 * operiert.
 *
 * @author Thomas Bachran
 *
 * @param <K> Der Typ der Schlüsselwerte.
 * @param <V> Der Typ der zugeordneten Werte.
 */
final class ArrayMapKeySet<K, V> implements Set<K> {

	/** Die {@link ArrayMap} zu der dieses Key-Set gehört. */
	private final @NotNull ArrayMap<K, V> _map;

	/**
	 * Erstellt eine neues Key-Set für die übergebene {@link ArrayMap}.
	 *
	 * @param map   die {@link ArrayMap}, zu welcher dieses Key-set gehört
	 */
	ArrayMapKeySet(final @NotNull ArrayMap<K, V> map) {
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

	@Override
	public boolean contains(final Object obj) {
		return _map.containsKey(obj);
	}

	@Override
	public @NotNull Iterator<K> iterator() {
		return new ArrayMapKeySetIterator<>(_map);
	}

	private @NotNull List<K> getKeyList() {
		final @NotNull ArrayList<K> list = new ArrayList<>(_map.size());
		for (int i = 0; i < _map.getNumberOfKeys(); i++) {
			final ArrayMapEntry<K, V> value = _map.getEntryByIndex(i);
			if (value != null)
				list.add(value.getKey());
		}
		return list;
	}

	@Override
	public @NotNull Object @NotNull [] toArray() {
		return getKeyList().toArray();
	}

	@Override
	public <@NotNull T> @NotNull T @NotNull [] toArray(final @NotNull T @NotNull [] a) {
		return getKeyList().toArray(a);
	}

	@Override
	public boolean add(final @NotNull K e) {
		throw new UnsupportedOperationException("add: Es kann kein Schlüsselwert ohne zugeordnetem Wert hinzugefügt werden (null ist nicht erlaubt).");
	}

	@Override
	public boolean remove(final Object obj) {
		return _map.remove(obj) != null;
	}

	@Override
	public boolean containsAll(final Collection<?> collection) {
		if ((collection == null) || (this == collection))
			return true;
		for (final Object obj : collection)
			if (!_map.containsKey(obj))
				return false;
		return true;
	}

	@Override
	public boolean addAll(final Collection<? extends K> c) {
		throw new UnsupportedOperationException("addAll: Es kann kein Schlüsselwert ohne zugeordnetem Wert hinzugefügt werden (null ist nicht erlaubt).");
	}

	@Override
	public boolean retainAll(final Collection<?> collection) {
		if (collection == null)
			throw new NullPointerException();
		boolean changed = false;
		for (int i = 0; i < _map.getNumberOfKeys(); i++) {
			final ArrayMapEntry<K, V> entry = _map.getEntryByIndex(i);
			if (entry == null)
				continue;
			if (!collection.contains(entry.getKey())) {
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
		boolean removed = false;
		for (final Object obj : collection) {
			if (_map.containsKey(obj)) {
				_map.remove(obj);
				removed = true;
			}
		}
		return removed;
	}

	@Override
	public void clear() {
		_map.clear();
	}

}
