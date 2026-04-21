package de.svws_nrw.core.adt.map;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import jakarta.validation.constraints.NotNull;

import java.util.Map.Entry;

/**
 * Diese Klasse implementiert ein SubEntrySet für die {@link AVLMapSubMap}. <br>
 * Alle Methodenaufrufe werden an die {@link AVLMapSubMap} delegiert.
 *
 * @author Benjamin A. Bartsch
 * @author Thomas Bachran
 *
 * @param <K> Der Typ der Schlüssel-Werte.
 * @param <V> Der Typ der zugeordneten Werte.
 */
public final class AVLMapSubEntrySet<K, V> implements Set<Map.Entry<K, V>> {

	/**
	 * Die {@link AVLMapSubMap} auf der diese Sup-Map operiert.
	 */
	private final @NotNull AVLMapSubMap<K, V> delegate;

	/**
	 * Erstellt ein neues SubEntrySet auf die übergebene {@link AVLMap}.
	 *
	 * @param sub Die {@link AVLMapSubMap} auf der operiert wird.
	 */
	AVLMapSubEntrySet(final @NotNull AVLMapSubMap<K, V> sub) {
		this.delegate = sub;
	}

	@Override
	public @NotNull String toString() {
		return delegate.toString();
	}

	@Override
	public int size() {
		return delegate.size();
	}

	@Override
	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	@Override
	public boolean contains(final @NotNull Object o) {
		return delegate.bcContainsEntry(o);
	}

	@Override
	public @NotNull Iterator<Entry<K, V>> iterator() {
		return delegate.bcGetSubEntrySetIterator();
	}

	@Override
	public @NotNull Object[] toArray() {
		return delegate.bcGetArrayListOfEntries().toArray();
	}

	@Override
	public <T> @NotNull T[] toArray(final @NotNull T[] a) {
		return delegate.bcGetArrayListOfEntries().toArray(a);
	}

	@Override
	public boolean add(final @NotNull Entry<K, V> e) {
		return delegate.bcAddEntryReturnBool(e);
	}

	@Override
	public boolean remove(final @NotNull Object o) {
		return delegate.bcRemoveEntry(o);
	}

	@Override
	public boolean containsAll(final @NotNull Collection<?> c) {
		return delegate.bcContainsAllEntries(c);
	}

	@Override
	public boolean addAll(final @NotNull Collection<? extends Entry<K, V>> c) {
		return delegate.bcAddAllEntries(c);
	}

	@Override
	public boolean retainAll(final @NotNull Collection<?> c) {
		return delegate.bcRetainAllEntries(c);
	}

	@Override
	public boolean removeAll(final @NotNull Collection<?> c) {
		return delegate.bcRemoveAllEntries(c);
	}

	@Override
	public void clear() {
		delegate.clear();
	}

}
