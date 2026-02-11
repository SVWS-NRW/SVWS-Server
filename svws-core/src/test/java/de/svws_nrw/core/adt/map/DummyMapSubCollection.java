package de.svws_nrw.core.adt.map;

import java.util.Collection;
import java.util.Iterator;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse implementiert eine Sub-Collection für die {@link DummyMapSub}. <br>
 * Alle Methodenaufrufe werden an die {@link DummyMapSub} delegiert.
 *
 * @author Benjamin A. Bartsch
 *
 */
public final class DummyMapSubCollection implements Collection<@NotNull Integer> {

	/**
	 * Die {@link DummyMapSub} auf der diese Sub-Collection operiert.
	 */
	private final @NotNull DummyMapSub _sub;

	/**
	 * Erstellt eine neue Sub-Collection zur übergebenen {@link DummyMapSub}.
	 *
	 * @param sub Die {@link DummyMapSub} auf der diese Sub-Collection operiert.
	 */
	DummyMapSubCollection(final @NotNull DummyMapSub sub) {
		_sub = sub;
	}

	@Override
	public @NotNull String toString() {
		// TODO: Transpiler kann "for (Integer value : this)" noch nicht.
		final @NotNull StringBuilder sb = new StringBuilder();
		final Iterator<Integer> iter = this.iterator();
		while (iter.hasNext()) {
			sb.append((sb.isEmpty() ? "" : ", ") + iter.next());
		}
		return "values = [" + sb.toString() + "], size = " + size() + " --> " + _sub.toString();
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
		return _sub.containsValue(o);
	}

	@Override
	public @NotNull Iterator<@NotNull Integer> iterator() {
		return new DummyMapSubCollectionIterator(_sub);
	}

	@Override
	public @NotNull Object @NotNull [] toArray() {
		return _sub.bcGetArrayListOfValues().toArray();
	}

	@Override
	public <@NotNull T> @NotNull T @NotNull [] toArray(final @NotNull T @NotNull [] a) {
		return _sub.bcGetArrayListOfValues().toArray(a);
	}

	@Override
	public boolean add(final @NotNull Integer e) {
		throw new UnsupportedOperationException(); // TODO BAR
	}

	@Override
	public boolean remove(final @NotNull Object o) {
		throw new UnsupportedOperationException(); // TODO BAR
	}

	@Override
	public boolean containsAll(final @NotNull Collection<?> c) {
		return _sub.bcContainsAllValues(c);
	}

	@Override
	public boolean addAll(final @NotNull Collection<? extends @NotNull Integer> c) {
		throw new UnsupportedOperationException(); // TODO BAR
	}

	@Override
	public boolean removeAll(final @NotNull Collection<?> c) {
		throw new UnsupportedOperationException(); // TODO BAR
	}

	@Override
	public boolean retainAll(final @NotNull Collection<?> c) {
		throw new UnsupportedOperationException(); // TODO BAR
	}

	@Override
	public void clear() {
		_sub.clear();
	}

}
