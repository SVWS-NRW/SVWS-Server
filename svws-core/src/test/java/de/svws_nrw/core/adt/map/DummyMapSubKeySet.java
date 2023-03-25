package de.svws_nrw.core.adt.map;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.SortedSet;

import jakarta.validation.constraints.NotNull;

/**
 * @author Benjamin A. Bartsch
 */
public class DummyMapSubKeySet implements NavigableSet<@NotNull Integer> {

	private final @NotNull DummyMapSub _sub;

	/**
	 * @param sub Die Sup-Map auf der operiert wird.
	 */
	public DummyMapSubKeySet(DummyMapSub sub) {
		_sub = sub;
	}

	@Override
	public String toString() {
		return _sub.toString();
	}

	@Override
	public Comparator<? super @NotNull Integer> comparator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public @NotNull Integer first() {
		return _sub.firstKey();
	}

	@Override
	public @NotNull Integer last() {
		return _sub.lastKey();
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
	public boolean contains(Object o) {
		return _sub.containsKey(o);
	}

	@Override
	public Object[] toArray() {
		return _sub.bcGetArrayListOfKeys().toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return _sub.bcGetArrayListOfKeys().toArray(a);
	}

	@Override
	public boolean add(@NotNull Integer e) {
		return _sub.bcAddKey(e);
	}

	@Override
	public boolean remove(Object o) {
		return _sub.remove(o) != null;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return _sub.bcContainsAllKeys(c);
	}

	@Override
	public boolean addAll(Collection<? extends @NotNull Integer> c) {
		return _sub.bcAddAllKeys(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return _sub.bcRetainAllKeys(c);
	}

	@Override
	public boolean removeAll(@NotNull Collection<?> c) {
		return _sub.bcRemoveAllKeysReturnBool(c);
	}

	@Override
	public void clear() {
		_sub.clear();
	}

	@Override
	public @NotNull Integer lower(@NotNull Integer e) {
		return _sub.lowerKey(e);
	}

	@Override
	public @NotNull Integer floor(@NotNull Integer e) {
		return _sub.floorKey(e);
	}

	@Override
	public @NotNull Integer ceiling(@NotNull Integer e) {
		return _sub.ceilingKey(e);
	}

	@Override
	public @NotNull Integer higher(@NotNull Integer e) {
		return _sub.higherKey(e);
	}

	@Override
	public Integer pollFirst() {
		return _sub.bcPollFirstKeyOrNull();
	}

	@Override
	public Integer pollLast() {
		return _sub.bcPollLastKeyOrNull();
	}

	@Override
	public @NotNull Iterator<@NotNull Integer> iterator() {
		return _sub.bcGetSubKeySetIterator();
	}

	@Override
	public @NotNull NavigableSet<@NotNull Integer> descendingSet() {
		return _sub.bcGetSubKeySetDescending();
	}

	@Override
	public @NotNull Iterator<@NotNull Integer> descendingIterator() {
		return _sub.bcGetSubKeySetDescendingIterator();
	}

	@Override
	public @NotNull NavigableSet<@NotNull Integer> subSet(@NotNull Integer fromElement, boolean fromInclusive,
			@NotNull Integer toElement, boolean toInclusive) {
		return _sub.bcGetSubKeySet(fromElement, fromInclusive, toElement, toInclusive);
	}

	@Override
	public @NotNull NavigableSet<@NotNull Integer> headSet(@NotNull Integer toElement, boolean inclusive) {
		return _sub.bcGetSubKeyHeadSet(toElement, inclusive);
	}

	@Override
	public @NotNull NavigableSet<@NotNull Integer> tailSet(@NotNull Integer fromElement, boolean inclusive) {
		return _sub.bcGetSubKeyTailSet(fromElement, inclusive);
	}

	@Override
	public @NotNull SortedSet<@NotNull Integer> subSet(@NotNull Integer fromElement, @NotNull Integer toElement) {
		return _sub.bcGetSubKeySet(fromElement, toElement);
	}

	@Override
	public @NotNull SortedSet<@NotNull Integer> headSet(@NotNull Integer toElement) {
		return _sub.bcGetSubKeyHeadSet(toElement);
	}

	@Override
	public @NotNull SortedSet<@NotNull Integer> tailSet(@NotNull Integer fromElement) {
		return _sub.bcGetSubKeyTailSet(fromElement);
	}

}
