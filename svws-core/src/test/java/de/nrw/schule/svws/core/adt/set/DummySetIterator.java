package de.nrw.schule.svws.core.adt.set;

import java.util.Iterator;
import java.util.NoSuchElementException;

import jakarta.validation.constraints.NotNull;

/**
 * Iterator
 *
 * @author Benjamin A. Bartsch
 */
public class DummySetIterator implements Iterator<Integer> {

	/**
	 * Das {@link DummySetSub} auf dem dieser Iterator operiert.
	 */
	private final @NotNull DummySetSub _parent;

	/**
	 * Der aktuelle Eintrag. Ein NULL-Wert bedeutet, dass das Element bereits entfernt wurde oder der Iterator auf einer
	 * ungültigen Position ist (z.B. vor dem ersten Element).
	 */
	private Integer _current; // NULL-Wert erlaubt.

	/**
	 * Der nächste Eintrag.
	 */
	private Integer _next; // NULL-Wert erlaubt.

	/**
	 * Konstruktor.
	 * 
	 * @param parent Das {@link DummySetSub} auf dem dieser Iterator operiert.
	 */
	public DummySetIterator(DummySetSub parent) {
		_parent = parent;
		_current = null;
		_next = _parent.isEmpty() ? null : _parent.first();
	}

	@Override
	public boolean hasNext() {
		return _next != null;
	}

	@Override
	public Integer next() {
		if (_next == null)
			throw new NoSuchElementException();
		_current = _next;
		_next = _parent.higher(_next);
		return _current;
	}

}
