package de.svws_nrw.core.adt.set;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.SortedSet;

import de.svws_nrw.core.adt.map.AVLMap;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse implementiert das {@link NavigableSet}-Interface. Das Set dient zum Speichern eindeutiger
 * Schlüssel-Werte. Alle Anfragen werden an die Klasse {@link AVLMap} delegiert und auf einen Dummy-Wert
 * gemapped. NULL-Werte sind in dem Set nicht erlaubt.
 *
 * @author Benjamin A. Bartsch
 *
 * @param <E> Der Typ der Schlüssel-Werte.
 */
public final class AVLSet<E> implements NavigableSet<E> {

	private final @NotNull NavigableSet<E> delegate;

	/**
	 * Erzeugt ein leeres Set, welche bei den Schlüsselwerten die natürliche Ordnung des {@link Comparable} -
	 * Interface nutzt.
	 */
	public AVLSet() {
		final @NotNull AVLMap<E, E> map = new AVLMap<>();
		map.allowKeyAlone(true);
		delegate = map.navigableKeySet();
	}

	/**
	 * Erstellt eine neues Set und nutzt dabei die angegeben Ordnung der Schlüssel.
	 *
	 * @param comparator Die Ordnung für die Schlüssel.
	 */
	public AVLSet(final @NotNull Comparator<E> comparator) {
		final @NotNull AVLMap<E, E> map = new AVLMap<>(comparator);
		map.allowKeyAlone(true);
		delegate = map.navigableKeySet();
	}

	/**
	 * Erstellt ein neues Set mit den Daten des angegebenen Sets und nutzt dabei die Ordnung dieses Sets.
	 *
	 * @param set Die Map mit den Daten.
	 */
	public AVLSet(final @NotNull SortedSet<E> set) {
		final @NotNull AVLMap<E, E> map = new AVLMap<>();
		map.allowKeyAlone(true);
		delegate = map.navigableKeySet();
		delegate.addAll(set);
	}

	@Override
	public @NotNull Comparator<? super E> comparator() {
		return delegate.comparator();
	}

	@Override
	public @NotNull E first() {
		return delegate.first();
	}

	@Override
	public @NotNull E last() {
		return delegate.last();
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
		return delegate.contains(o);
	}

	@Override
	public @NotNull Object @NotNull [] toArray() {
		return delegate.toArray();
	}

	@Override
	public <T> @NotNull T @NotNull [] toArray(final @NotNull T @NotNull [] a) {
		return delegate.toArray(a);
	}

	@Override
	public boolean add(final @NotNull E e) {
		return delegate.add(e);
	}

	@Override
	public boolean remove(final @NotNull Object o) {
		return delegate.remove(o);
	}

	@Override
	public boolean containsAll(final @NotNull Collection<?> c) {
		return delegate.containsAll(c);
	}

	@Override
	public boolean addAll(final @NotNull Collection<? extends E> c) {
		return delegate.addAll(c);
	}

	@Override
	public boolean retainAll(final @NotNull Collection<?> c) {
		return delegate.retainAll(c);
	}

	@Override
	public boolean removeAll(final @NotNull Collection<?> c) {
		return delegate.removeAll(c);
	}

	@Override
	public void clear() {
		delegate.clear();
	}

	@Override
	public E lower(final @NotNull E e) { // return NULL erlaubt!
		return delegate.lower(e);
	}

	@Override
	public E floor(final @NotNull E e) { // return NULL erlaubt!
		return delegate.floor(e);
	}

	@Override
	public E ceiling(final @NotNull E e) { // return NULL erlaubt!
		return delegate.ceiling(e);
	}

	@Override
	public E higher(final @NotNull E e) { // return NULL erlaubt!
		return delegate.higher(e);
	}

	@Override
	public E pollFirst() { // return NULL erlaubt!
		return delegate.pollFirst();
	}

	@Override
	public E pollLast() { // return NULL erlaubt!
		return delegate.pollLast();
	}

	@Override
	public @NotNull Iterator<E> iterator() {
		return delegate.iterator();
	}

	@Override
	public @NotNull NavigableSet<E> descendingSet() {
		return delegate.descendingSet();
	}

	@Override
	public @NotNull Iterator<E> descendingIterator() {
		return delegate.descendingIterator();
	}

	@Override
	public @NotNull NavigableSet<E> subSet(final @NotNull E fromElement, final boolean fromInclusive, final @NotNull E toElement,
			final boolean toInclusive) {
		return delegate.subSet(fromElement, fromInclusive, toElement, toInclusive);
	}

	@Override
	public @NotNull NavigableSet<E> headSet(final @NotNull E toElement, final boolean inclusive) {
		return delegate.headSet(toElement, inclusive);
	}

	@Override
	public @NotNull NavigableSet<E> tailSet(final @NotNull E fromElement, final boolean inclusive) {
		return delegate.tailSet(fromElement, inclusive);
	}

	@Override
	public @NotNull SortedSet<E> subSet(final @NotNull E fromElement, final @NotNull E toElement) {
		return delegate.subSet(fromElement, toElement);
	}

	@Override
	public @NotNull SortedSet<E> headSet(final @NotNull E toElement) {
		return delegate.headSet(toElement);
	}

	@Override
	public @NotNull SortedSet<E> tailSet(final @NotNull E fromElement) {
		return delegate.tailSet(fromElement);
	}

}
