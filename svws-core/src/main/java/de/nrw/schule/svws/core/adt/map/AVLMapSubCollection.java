package de.nrw.schule.svws.core.adt.map;

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
public class AVLMapSubCollection<@NotNull K, @NotNull V> implements Collection<@NotNull V> {

	/**
	 * Die {@link AVLMapSubMap} auf der diese Sub-Collection operiert.
	 */
	private final @NotNull AVLMapSubMap<@NotNull K, @NotNull V> _sub;

	/**
	 * Erstellt eine neue Sub-Collection zur übergebenen {@link AVLMapSubMap}.
	 * 
	 * @param sub Die {@link AVLMapSubMap} auf der diese Sub-Collection operiert.
	 */
	AVLMapSubCollection(@NotNull AVLMapSubMap<@NotNull K, @NotNull V> sub) {
		_sub = sub;
	}


	@Override
	public @NotNull String toString() {
		String s = "";
		for (V value : this)
			s += (s.length() == 0 ? "" : ", ") + value;
		return "values = [" + s + "], size = " + size()+" --> "+_sub.toString();
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
	public boolean contains(@NotNull Object o) { // contains VALUE not KEY!!!
		return _sub.containsValue(o);
	}

	@Override
	public @NotNull Iterator<@NotNull V> iterator() {
		return _sub.bcGetSubCollectionIterator();
	}

	@Override
	public @NotNull Object[] toArray() {
		return _sub.bcGetVectorOfValues().toArray();
	}

	@Override
	public <@NotNull T> @NotNull T[] toArray(@NotNull T[] a) {
		return _sub.bcGetVectorOfValues().toArray(a);
	}

	@Override
	public boolean add(@NotNull V e) {
		throw new UnsupportedOperationException(); // VALUE cannot be added without KEY
	}

	@Override
	public boolean remove(@NotNull Object o) {
		throw new UnsupportedOperationException(); // VALUE cannot be removed without KEY
	}

	@Override
	public boolean containsAll(@NotNull Collection<@NotNull ?> c) {
		return _sub.bcContainsAllValues(c);
	}

	@Override
	public boolean addAll(@NotNull Collection<? extends @NotNull V> c) {
		throw new UnsupportedOperationException(); // VALUES cannot be added without KEY
	}

	@Override
	public boolean removeAll(@NotNull Collection<@NotNull ?> c) {
		throw new UnsupportedOperationException(); // VALUES cannot be removed without KEY
	}

	@Override
	public boolean retainAll(@NotNull Collection<@NotNull ?> c) {
		throw new UnsupportedOperationException(); // VALUES cannot be retained without KEY
	}

	@Override
	public void clear() {
		_sub.clear(); // clears VALUES (and their KEYS)
	}

}
