package de.svws_nrw.core.adt.map;

import java.util.Collection;
import java.util.Iterator;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse implementiert eine Sub-Collection für die {@link AVLMapSubMap}. <br>
 * Alle Methodenaufrufe werden an die {@link AVLMapSubMap} delegiert. <br>
 *
 * @author Benjamin A. Bartsch
 *
 * @param <K> Der Typ der Schlüssel-Werte.
 * @param <V> Der Typ der zugeordneten Werte.
 */
public final class AVLMapSubCollection<K, V> implements Collection<V> {

	/**
	 * Die {@link AVLMapSubMap} auf der diese Sub-Collection operiert.
	 */
	private final @NotNull AVLMapSubMap<K, V> delegate;

	/**
	 * Erstellt eine neue Sub-Collection zur übergebenen {@link AVLMapSubMap}.
	 *
	 * @param sub Die {@link AVLMapSubMap} auf der diese Sub-Collection operiert.
	 */
	AVLMapSubCollection(final @NotNull AVLMapSubMap<K, V> sub) {
		this.delegate = sub;
	}


	@Override
	public @NotNull String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("values = [");
		boolean first = true;
		for (final V value : this) {
			if (first) {
				first = false;
			} else {
				sb.append(", ");
			}
			sb.append(value);
		}
		sb.append("], size = ");
		sb.append(size());
		sb.append(" --> ");
		sb.append(delegate.toString());
		return sb.toString();
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
	public boolean contains(final @NotNull Object o) { // contains VALUE not KEY!!!
		return delegate.containsValue(o);
	}

	@Override
	public @NotNull Iterator<V> iterator() {
		return delegate.bcGetSubCollectionIterator();
	}

	@Override
	public @NotNull Object[] toArray() {
		return delegate.bcGetArrayListOfValues().toArray();
	}

	@Override
	public <T> @NotNull T[] toArray(final @NotNull T[] a) {
		return delegate.bcGetArrayListOfValues().toArray(a);
	}

	@Override
	public boolean add(final @NotNull V e) {
		throw new UnsupportedOperationException(); // VALUE cannot be added without KEY
	}

	@Override
	public boolean remove(final @NotNull Object o) {
		throw new UnsupportedOperationException(); // VALUE cannot be removed without KEY
	}

	@Override
	public boolean containsAll(final @NotNull Collection<?> c) {
		return delegate.bcContainsAllValues(c);
	}

	@Override
	public boolean addAll(final @NotNull Collection<? extends V> c) {
		throw new UnsupportedOperationException(); // VALUES cannot be added without KEY
	}

	@Override
	public boolean removeAll(final @NotNull Collection<?> c) {
		throw new UnsupportedOperationException(); // VALUES cannot be removed without KEY
	}

	@Override
	public boolean retainAll(final @NotNull Collection<?> c) {
		throw new UnsupportedOperationException(); // VALUES cannot be retained without KEY
	}

	@Override
	public void clear() {
		delegate.clear(); // clears VALUES (and their KEYS)
	}

}
