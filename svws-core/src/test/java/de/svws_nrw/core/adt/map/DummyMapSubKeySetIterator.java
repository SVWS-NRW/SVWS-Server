package de.svws_nrw.core.adt.map;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import jakarta.validation.constraints.NotNull;

/**
 * @author Benjamin A. Bartsch
 */
public final class DummyMapSubKeySetIterator implements Iterator<@NotNull Integer> {

	/**
	 * Die {@link DummyMapSub} auf der dieser Iterator operiert.
	 */
	private final @NotNull DummyMapSub _sub;

	/**
	 * Der aktuelle Eintrag. Ein NULL-Wert bedeutet, dass das Element bereits entfernt wurde oder der Iterator auf einer
	 * ungültigen Position ist (z.B. vor dem ersten Element).
	 */
	private Entry<@NotNull Integer, @NotNull Integer> _current; // NULL-Wert erlaubt.

	/**
	 * Der nächste Eintrag. Ein NULL-Wert bedeutet, dass kein nächster Eintrag existiert.
	 */
	private Entry<@NotNull Integer, @NotNull Integer> _next; // NULL-Wert erlaubt.

	/**
	 * Erzeugt einen DummyMapSubKeySetIterator der auf der {@link DummyMapSub} operiert.
	 *
	 * @param sub Die {@link DummyMapSub} auf der dieser Iterator operiert.
	 */
	DummyMapSubKeySetIterator(final @NotNull DummyMapSub sub) {
		_sub = sub;
		_current = null;
		_next = _sub.firstEntry();
	}

	@Override
	public @NotNull Integer next() {
		if (_next == null)
			throw new NoSuchElementException();
		_current = _next;
		_next = _sub.higherEntry(_next.getKey());
		return _current.getKey();
	}

	@Override
	public boolean hasNext() {
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
