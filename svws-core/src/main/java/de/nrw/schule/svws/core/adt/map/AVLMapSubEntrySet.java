package de.nrw.schule.svws.core.adt.map;

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
public class AVLMapSubEntrySet<@NotNull K, @NotNull V> implements Set<Map.@NotNull Entry<@NotNull K, @NotNull V>> {

	/**
	 * Die {@link AVLMapSubMap} auf der diese Sup-Map operiert.
	 */
	private final @NotNull AVLMapSubMap<@NotNull K, @NotNull V> _sub;

	/**
	 * Erstellt ein neues SubEntrySet auf die übergebene {@link AVLMap}.
	 * 
	 * @param sub Die {@link AVLMapSubMap} auf der operiert wird.
	 */
	AVLMapSubEntrySet(final @NotNull AVLMapSubMap<@NotNull K, @NotNull V> sub) {
		_sub = sub;
	}

	@Override
	public @NotNull String toString() {
		return _sub.toString();
	}

	@Override
	public int size() {
		return _sub.size();
	}

	@Override
	public boolean isEmpty() {
		return _sub.isEmpty();
	}

	@Override
	public boolean contains(final @NotNull Object o) {
		return _sub.bcContainsEntry(o);
	}

	@Override
	public @NotNull Iterator<@NotNull Entry<@NotNull K, @NotNull V>> iterator() {
		return _sub.bcGetSubEntrySetIterator();
	}

	@Override
	public @NotNull Object[] toArray() {
		return _sub.bcGetVectorOfEntries().toArray();
	}

	@Override
	public <@NotNull T> @NotNull T[] toArray(final @NotNull T[] a) {
		return _sub.bcGetVectorOfEntries().toArray(a);
	}

	@Override
	public boolean add(final @NotNull Entry<@NotNull K, @NotNull V> e) {
		return _sub.bcAddEntryReturnBool(e);
	}

	@Override
	public boolean remove(final @NotNull Object o) {
		return _sub.bcRemoveEntry(o);
	}

	@Override
	public boolean containsAll(final @NotNull Collection<@NotNull ?> c) {
		return _sub.bcContainsAllEntries(c);
	}

	@Override
	public boolean addAll(final @NotNull Collection<? extends @NotNull Entry<@NotNull K, @NotNull V>> c) {
		return _sub.bcAddAllEntries(c);
	}

	@Override
	public boolean retainAll(final @NotNull Collection<@NotNull ?> c) {
		return _sub.bcRetainAllEntries(c);
	}

	@Override
	public boolean removeAll(final @NotNull Collection<@NotNull ?> c) {
		return _sub.bcRemoveAllEntries(c);
	}

	@Override
	public void clear() {
		_sub.clear();
	}

}
