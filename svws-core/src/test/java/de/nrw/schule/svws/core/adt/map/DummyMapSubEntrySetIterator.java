package de.nrw.schule.svws.core.adt.map;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse implementiert einen ENTRY-Iterator, welcher auf der {@link DummyMapSub} operiert.
 *
 * @author Benjamin A. Bartsch
 * 
 */
class DummyMapSubEntrySetIterator implements Iterator<@NotNull Entry<@NotNull Integer, @NotNull Integer>> {

	/**
	 * Die {@link DummyMapSub} auf der dieser Iterator operiert.
	 */
	private final @NotNull DummyMapSub _sub;

	/**
	 * Der aktuelle Eintrag. Ein NULL-Wert bedeutet, dass das Element bereits entfernt wurde oder der Iterator auf einer
	 * ungültigen Position ist, beispielsweise vor dem ersten Element.
	 */
	private Entry<@NotNull Integer, @NotNull Integer> _current; // NULL-Wert möglich.

	/**
	 * Der nächste Eintrag.
	 */
	private Entry<@NotNull Integer, @NotNull Integer> _next; // NULL-Wert möglich

	/**
	 * Erstellt einen neuen ENTRY-Iterator für die angegebene {@link DummyMapSub}.
	 * 
	 * @param sub Die {@link DummyMapSub} auf der operiert wird.
	 */
	DummyMapSubEntrySetIterator(@NotNull DummyMapSub sub) {
		_sub = sub;
		_current = null;
		_next = _sub.firstEntry();
	}

	@Override
	public @NotNull Entry<@NotNull Integer, @NotNull Integer> next() {
		if (_next == null)
			throw new NoSuchElementException();
		_current = _next;
		_next = _sub.higherEntry(_next.getKey());
		return _current;
	}

	@Override
	public final boolean hasNext() {
		return _next != null;
	}

	@Override
	public void remove() {
		if (_current == null)
			throw new IllegalStateException();
		_sub.remove(_current.getKey());
		_current = null;
	}

}
