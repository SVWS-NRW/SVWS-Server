package de.nrw.schule.svws.core.adt.set;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.SortedSet;

import de.nrw.schule.svws.core.adt.map.AVLMap;
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
public class AVLSet<@NotNull E> implements NavigableSet<@NotNull E> {

	private final @NotNull NavigableSet<@NotNull E> _set;

	/**
	 * Erzeugt ein leeres Set, welche bei den Schlüsselwerten die natürliche Ordnung des {@link Comparable} -
	 * Interface nutzt.
	 */
	public AVLSet() {
		@NotNull AVLMap<@NotNull E, @NotNull E> map = new AVLMap<>();
		map.allowKeyAlone(true);
		_set = map.navigableKeySet();
	}

	/**
	 * Erstellt eine neues Set und nutzt dabei die angegeben Ordnung der Schlüssel.
	 * 
	 * @param comparator Die Ordnung für die Schlüssel.
	 */
	public AVLSet(@NotNull Comparator<@NotNull E> comparator) {
		@NotNull AVLMap<@NotNull E, @NotNull E> map = new AVLMap<>(comparator);
		map.allowKeyAlone(true);
		_set = map.navigableKeySet();
	}

	/**
	 * Erstellt ein neues Set mit den Daten des angegebenen Sets und nutzt dabei die Ordnung dieses Sets.
	 * 
	 * @param set Die Map mit den Daten.
	 */
	public AVLSet(@NotNull SortedSet<@NotNull E> set) {
		@NotNull AVLMap<@NotNull E, @NotNull E> map = new AVLMap<>();
		map.allowKeyAlone(true);
		_set = map.navigableKeySet();
		_set.addAll(set);
	}

	@Override
	public @NotNull Comparator<? super @NotNull E> comparator() {
		return _set.comparator();
	}

	@Override
	public @NotNull E first() {
		return _set.first();
	}

	@Override
	public @NotNull E last() {
		return _set.last();
	}

	@Override
	public int size() {
		return _set.size();
	}

	@Override
	public boolean isEmpty() {
		return _set.isEmpty();
	}

	@Override
	public boolean contains(@NotNull Object o) {
		return _set.contains(o);
	}

	@Override
	public @NotNull Object @NotNull [] toArray() {
		return _set.toArray();
	}

	@Override
	public <@NotNull T> @NotNull T @NotNull [] toArray(@NotNull T @NotNull [] a) {
		return _set.toArray(a);
	}

	@Override
	public boolean add(@NotNull E e) {
		return _set.add(e);
	}

	@Override
	public boolean remove(@NotNull Object o) {
		return _set.remove(o);
	}

	@Override
	public boolean containsAll(@NotNull Collection<@NotNull ?> c) {
		return _set.containsAll(c);
	}

	@Override
	public boolean addAll(@NotNull Collection<? extends @NotNull E> c) {
		return _set.addAll(c);
	}

	@Override
	public boolean retainAll(@NotNull Collection<@NotNull ?> c) {
		return _set.retainAll(c);
	}

	@Override
	public boolean removeAll(@NotNull Collection<@NotNull ?> c) {
		return _set.removeAll(c);
	}

	@Override
	public void clear() {
		_set.clear();
	}

	@Override
	public E lower(@NotNull E e) { // return NULL erlaubt!
		return _set.lower(e);
	}

	@Override
	public E floor(@NotNull E e) { // return NULL erlaubt!
		return _set.floor(e);
	}

	@Override
	public E ceiling(@NotNull E e) { // return NULL erlaubt!
		return _set.ceiling(e);
	}

	@Override
	public E higher(@NotNull E e) { // return NULL erlaubt!
		return _set.higher(e);
	}

	@Override
	public E pollFirst() { // return NULL erlaubt!
		return _set.pollFirst();
	}

	@Override
	public E pollLast() { // return NULL erlaubt!
		return _set.pollLast();
	}

	@Override
	public @NotNull Iterator<@NotNull E> iterator() {
		return _set.iterator();
	}

	@Override
	public @NotNull NavigableSet<@NotNull E> descendingSet() {
		return _set.descendingSet();
	}

	@Override
	public @NotNull Iterator<@NotNull E> descendingIterator() {
		return _set.descendingIterator();
	}

	@Override
	public @NotNull NavigableSet<@NotNull E> subSet(@NotNull E fromElement, boolean fromInclusive, @NotNull E toElement,
			boolean toInclusive) {
		return _set.subSet(fromElement, fromInclusive, toElement, toInclusive);
	}

	@Override
	public @NotNull NavigableSet<@NotNull E> headSet(@NotNull E toElement, boolean inclusive) {
		return _set.headSet(toElement, inclusive);
	}

	@Override
	public @NotNull NavigableSet<@NotNull E> tailSet(@NotNull E fromElement, boolean inclusive) {
		return _set.tailSet(fromElement, inclusive);
	}

	@Override
	public @NotNull SortedSet<@NotNull E> subSet(@NotNull E fromElement, @NotNull E toElement) {
		return _set.subSet(fromElement, toElement);
	}

	@Override
	public @NotNull SortedSet<@NotNull E> headSet(@NotNull E toElement) {
		return _set.headSet(toElement);
	}

	@Override
	public @NotNull SortedSet<@NotNull E> tailSet(@NotNull E fromElement) {
		return _set.tailSet(fromElement);
	}

}
