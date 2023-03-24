package de.nrw.schule.svws.core.adt.map;

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
public class AVLMapSubKeySet<@NotNull K, @NotNull V> implements NavigableSet<@NotNull K> {

	/**
	 * Die {@link AVLMap} auf der dieses Sub-Set operiert.
	 */
	private final @NotNull AVLMapSubMap<@NotNull K, @NotNull V> _sub;

	/**
	 * Erstellt eine neues Sub-Set auf die übergebene {@link AVLMap}.
	 * 
	 * @param sub Die {@link AVLMap} auf der operiert wird.
	 */
	AVLMapSubKeySet(final @NotNull AVLMapSubMap<@NotNull K, @NotNull V> sub) {
		_sub = sub;
	}

	@Override
	public @NotNull String toString() {
		return _sub.toString();
	}

	@Override
	public @NotNull Comparator<? super @NotNull K> comparator() {
		return _sub.comparator();
	}

	@Override
	public @NotNull K first() {
		return _sub.firstKey();
	}

	@Override
	public @NotNull K last() {
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
	public boolean contains(final @NotNull Object o) {
		return _sub.containsKey(o);
	}

	@Override
	public @NotNull Object @NotNull [] toArray() {
		return _sub.bcGetVectorOfKeys().toArray();
	}

	@Override
	public <@NotNull T> @NotNull T @NotNull [] toArray(final @NotNull T @NotNull [] a) {
		return _sub.bcGetVectorOfKeys().toArray(a);
	}

	@Override
	public boolean add(final @NotNull K e) {
		return _sub.bcAddKey(e);
	}

	@Override
	public boolean remove(final @NotNull Object o) {
		return _sub.bcRemoveKeyReturnBool(o);
	}

	@Override
	public boolean containsAll(final @NotNull Collection<@NotNull ?> c) {
		return _sub.bcContainsAllKeys(c);
	}

	@Override
	public boolean addAll(final @NotNull Collection<? extends @NotNull K> c) {
		return _sub.bcAddAllKeys(c);
	}

	@Override
	public boolean retainAll(final @NotNull Collection<@NotNull ?> c) {
		return _sub.bcRetainAllKeys(c);
	}

	@Override
	public boolean removeAll(final @NotNull Collection<@NotNull ?> c) {
		return _sub.bcRemoveAllKeys(c);
	}

	@Override
	public void clear() {
		_sub.clear();
	}

	@Override
	public K lower(final @NotNull K e) { // return NULL erlaubt.
		return _sub.bcGetLowerKeyOrNull(e);
	}

	@Override
	public K floor(final @NotNull K e) { // return NULL erlaubt.
		return _sub.bcGetFloorKeyOrNull(e);
	}

	@Override
	public K ceiling(final @NotNull K e) { // return NULL erlaubt.
		return _sub.bcGetCeilingKeyOrNull(e);
	}

	@Override
	public K higher(final @NotNull K e) { // return NULL erlaubt.
		return _sub.bcGetHigherKeyOrNull(e);
	}

	@Override
	public K pollFirst() { // return NULL erlaubt.
		return _sub.bcPollFirstKeyOrNull();
	}

	@Override
	public K pollLast() { // return NULL erlaubt.
		return _sub.bcPollLastKeyOrNull();
	}

	@Override
	public @NotNull Iterator<@NotNull K> iterator() {
		return _sub.bcGetSubKeySetIterator();
	}

	@Override
	public @NotNull NavigableSet<@NotNull K> descendingSet() {
		return _sub.bcGetSubKeySetDescending();
	}

	@Override
	public @NotNull Iterator<@NotNull K> descendingIterator() {
		return _sub.bcGetSubKeySetDescendingIterator();
	}

	@Override
	public @NotNull NavigableSet<@NotNull K> subSet(final @NotNull K fromElement, final boolean fromInclusive, final @NotNull K toElement,
			final boolean toInclusive) {
		return _sub.bcGetSubKeySet(fromElement, fromInclusive, toElement, toInclusive);
	}

	@Override
	public @NotNull NavigableSet<@NotNull K> headSet(final @NotNull K toElement, final boolean inclusive) {
		return _sub.bcGetSubKeyHeadSet(toElement, inclusive);
	}

	@Override
	public @NotNull NavigableSet<@NotNull K> tailSet(final @NotNull K fromElement, final boolean inclusive) {
		return _sub.bcGetSubKeyTailSet(fromElement, inclusive);
	}

	@Override
	public @NotNull SortedSet<@NotNull K> subSet(final @NotNull K fromElement, final @NotNull K toElement) {
		return _sub.bcGetSubKeySet(fromElement, toElement);
	}

	@Override
	public @NotNull SortedSet<@NotNull K> headSet(final @NotNull K toElement) {
		return _sub.bcGetSubKeyHeadSet(toElement);
	}

	@Override
	public @NotNull SortedSet<@NotNull K> tailSet(final @NotNull K fromElement) {
		return _sub.bcGetSubKeyTailSet(fromElement);
	}

}
