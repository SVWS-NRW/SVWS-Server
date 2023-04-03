package de.svws_nrw.core.adt.map;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import jakarta.validation.constraints.NotNull;

import java.util.Map.Entry;

/**
 * Diese Klasse implementiert ein SubEntrySet für die {@link DummyMapSub}. <br>
 * Alle Methodenaufrufe werden an die {@link DummyMapSub} delegiert.
 *
 * @author Benjamin A. Bartsch
 *
 */
public final class DummyMapSubEntrySet implements Set<Map.@NotNull Entry<@NotNull Integer, @NotNull Integer>> {

	/**
	 * Die {@link DummyMapSub} auf der diese Sup-Map operiert.
	 */
	private final @NotNull DummyMapSub _sub;

	/**
	 * Erstellt ein neues SubEntrySet auf die übergebene {@link DummyMapSub}.
	 *
	 * @param sub Die {@link DummyMapSub} auf der operiert wird.
	 */
	DummyMapSubEntrySet(final @NotNull DummyMapSub sub) {
		_sub = sub;
	}

	@Override
	public String toString() {
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
	public boolean contains(@NotNull final Object o) {
		return _sub.bcContainsEntry(o);
	}

	@Override
	public @NotNull Iterator<@NotNull Entry<@NotNull Integer, @NotNull Integer>> iterator() {
		return new DummyMapSubEntrySetIterator(_sub);
	}

	@Override
	public @NotNull Object @NotNull [] toArray() {
		return _sub.bcGetArrayListOfEntries().toArray();
	}

	@Override
	public <@NotNull T> @NotNull T @NotNull [] toArray(@NotNull final T @NotNull [] a) {
		return _sub.bcGetArrayListOfEntries().toArray(a);
	}

	@Override
	public boolean add(@NotNull final Entry<@NotNull Integer, @NotNull Integer> e) {
		return _sub.bcAddEntryReturnBool(e);
	}

	@Override
	public boolean remove(@NotNull final Object e) {
		return _sub.bcRemoveEntry(e);
	}

	@Override
	public boolean containsAll(@NotNull final Collection<?> c) {
		return _sub.bcContainsAllEntries(c);
	}

	@Override
	public boolean addAll(@NotNull final Collection<? extends @NotNull Entry<@NotNull Integer, @NotNull Integer>> c) {
		return _sub.bcAddAllEntries(c);
	}

	@Override
	public boolean retainAll(@NotNull final Collection<?> c) {
		return _sub.bcRetainAllEntries(c);
	}

	@Override
	public boolean removeAll(@NotNull final Collection<?> c) {
		return _sub.bcRemoveAllEntriesReturnBool(c);
	}

	@Override
	public void clear() {
		_sub.clear();
	}

}
