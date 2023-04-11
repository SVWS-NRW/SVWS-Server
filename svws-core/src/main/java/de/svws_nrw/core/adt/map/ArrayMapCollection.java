package de.svws_nrw.core.adt.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse implementiert eine Collection als View für die Klasse {@link ArrayMap}.
 *
 * @author Thomas Bachran
 *
 * @param <K> Der Typ der Schlüsselwerte.
 * @param <V> Der Typ der zugeordneten Werte.
 */
final class ArrayMapCollection<@NotNull K, @NotNull V> implements Collection<@NotNull V> {

	/** Die {@link ArrayMap} zu der diese Collection gehört. */
	private final @NotNull ArrayMap<@NotNull K, @NotNull V> _map;

	/**
	 * Erstellt eine neues Key-Set für die übergebene {@link ArrayMap}.
	 *
	 * @param map   die {@link ArrayMap}, zu welcher dieses Key-set gehört
	 */
	ArrayMapCollection(final @NotNull ArrayMap<@NotNull K, @NotNull V> map) {
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
		return _map.containsValue(obj);
	}

	@Override
	public @NotNull Iterator<@NotNull V> iterator() {
		return new ArrayMapCollectionIterator<>(_map);
	}

	private @NotNull List<@NotNull V> getValueList() {
		final @NotNull ArrayList<@NotNull V> list = new ArrayList<>(_map.size());
		for (int i = 0; i < _map.getNumberOfKeys(); i++) {
			final V value = _map.getValueAt(i);
			if (value != null)
				list.add(value);
		}
		return list;
	}

	@Override
	public @NotNull Object@NotNull[] toArray() {
		return getValueList().toArray();
	}

	@Override
	public <@NotNull T> @NotNull T@NotNull[] toArray(final @NotNull T@NotNull[] a) {
		return getValueList().toArray(a);
	}

	@Override
	public boolean add(final @NotNull V e) {
		throw new UnsupportedOperationException("add: Werte können nicht ohne Schlüsselwerte hinzugefügt werden.");
	}

	@Override
	public boolean remove(final Object o) {
		throw new UnsupportedOperationException("remove: Werte können nicht ohne einen Schlüsselwert entfernt werden.");
	}

	@Override
	public boolean containsAll(final Collection<?> collection) {
		if ((collection == null) || (this == collection))
			return true;
		for (final Object obj : collection)
			if (!_map.containsValue(obj))
				return false;
		return true;
	}

	@Override
	public boolean addAll(final Collection<? extends @NotNull V> c) {
		throw new UnsupportedOperationException("addAll: Werte können nicht ohne Schlüsselwerte hinzugefügt werden.");
	}

	@Override
	public boolean removeAll(final Collection<?> c) {
		throw new UnsupportedOperationException("removeAll: Werte können nicht ohne einen Schlüsselwert entfernt werden.");
	}

	@Override
	public boolean retainAll(final Collection<?> c) {
		throw new UnsupportedOperationException("retainAll: Werte können nicht ohne einen Schlüsselwert entfernt werden.");
	}

	@Override
	public void clear() {
		_map.clear();
	}

}
