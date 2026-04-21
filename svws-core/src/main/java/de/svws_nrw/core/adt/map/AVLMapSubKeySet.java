package de.svws_nrw.core.adt.map;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.SortedSet;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse implementiert ein Sub-Key-Set für einen AVL-Baum der Klasse {@link AVLMap}. Alle Methodenaufrufe werden
 * an die {@link AVLMap} delegiert.
 *
 * @author Benjamin A. Bartsch
 * @author Thomas Bachran
 *
 * @param <K> Der Typ der Schlüssel-Werte.
 * @param <V> Der Typ der zugeordneten Werte.
 */
public final class AVLMapSubKeySet<K, V> implements NavigableSet<K> {

	/**
	 * Die {@link AVLMap} auf der dieses Sub-Set operiert.
	 */
	private final @NotNull AVLMapSubMap<K, V> delegate;

	/**
	 * Erstellt eine neues Sub-Set auf die übergebene {@link AVLMap}.
	 *
	 * @param sub Die {@link AVLMap} auf der operiert wird.
	 */
	AVLMapSubKeySet(final @NotNull AVLMapSubMap<K, V> sub) {
		this.delegate = sub;
	}

	@Override
	public @NotNull String toString() {
		return delegate.toString();
	}

	@Override
	public @NotNull Comparator<? super K> comparator() {
		return delegate.comparator();
	}

	@Override
	public @NotNull K first() {
		return delegate.firstKey();
	}

	@Override
	public @NotNull K last() {
		return delegate.lastKey();
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
		return delegate.containsKey(o);
	}

	@Override
	public @NotNull Object @NotNull [] toArray() {
		return delegate.bcGetArrayListOfKeys().toArray();
	}

	@Override
	public <@NotNull T> @NotNull T @NotNull [] toArray(final @NotNull T @NotNull [] a) {
		return delegate.bcGetArrayListOfKeys().toArray(a);
	}

	@Override
	public boolean add(final @NotNull K e) {
		return delegate.bcAddKey(e);
	}

	@Override
	public boolean remove(final @NotNull Object o) {
		return delegate.bcRemoveKeyReturnBool(o);
	}

	@Override
	public boolean containsAll(final @NotNull Collection<?> c) {
		return delegate.bcContainsAllKeys(c);
	}

	@Override
	public boolean addAll(final @NotNull Collection<? extends K> c) {
		return delegate.bcAddAllKeys(c);
	}

	@Override
	public boolean retainAll(final @NotNull Collection<?> c) {
		return delegate.bcRetainAllKeys(c);
	}

	@Override
	public boolean removeAll(final @NotNull Collection<?> c) {
		return delegate.bcRemoveAllKeys(c);
	}

	@Override
	public void clear() {
		delegate.clear();
	}

	@Override
	public K lower(final @NotNull K e) { // return NULL erlaubt.
		return delegate.bcGetLowerKeyOrNull(e);
	}

	@Override
	public K floor(final @NotNull K e) { // return NULL erlaubt.
		return delegate.bcGetFloorKeyOrNull(e);
	}

	@Override
	public K ceiling(final @NotNull K e) { // return NULL erlaubt.
		return delegate.bcGetCeilingKeyOrNull(e);
	}

	@Override
	public K higher(final @NotNull K e) { // return NULL erlaubt.
		return delegate.bcGetHigherKeyOrNull(e);
	}

	@Override
	public K pollFirst() { // return NULL erlaubt.
		return delegate.bcPollFirstKeyOrNull();
	}

	@Override
	public K pollLast() { // return NULL erlaubt.
		return delegate.bcPollLastKeyOrNull();
	}

	@Override
	public @NotNull Iterator<K> iterator() {
		return delegate.bcGetSubKeySetIterator();
	}

	@Override
	public @NotNull NavigableSet<K> descendingSet() {
		return delegate.bcGetSubKeySetDescending();
	}

	@Override
	public @NotNull Iterator<K> descendingIterator() {
		return delegate.bcGetSubKeySetDescendingIterator();
	}

	@Override
	public @NotNull NavigableSet<K> subSet(final @NotNull K fromElement, final boolean fromInclusive, final @NotNull K toElement,
			final boolean toInclusive) {
		return delegate.bcGetSubKeySet(fromElement, fromInclusive, toElement, toInclusive);
	}

	@Override
	public @NotNull NavigableSet<K> headSet(final @NotNull K toElement, final boolean inclusive) {
		return delegate.bcGetSubKeyHeadSet(toElement, inclusive);
	}

	@Override
	public @NotNull NavigableSet<K> tailSet(final @NotNull K fromElement, final boolean inclusive) {
		return delegate.bcGetSubKeyTailSet(fromElement, inclusive);
	}

	@Override
	public @NotNull SortedSet<K> subSet(final @NotNull K fromElement, final @NotNull K toElement) {
		return delegate.bcGetSubKeySet(fromElement, toElement);
	}

	@Override
	public @NotNull SortedSet<K> headSet(final @NotNull K toElement) {
		return delegate.bcGetSubKeyHeadSet(toElement);
	}

	@Override
	public @NotNull SortedSet<K> tailSet(final @NotNull K fromElement) {
		return delegate.bcGetSubKeyTailSet(fromElement);
	}

}
