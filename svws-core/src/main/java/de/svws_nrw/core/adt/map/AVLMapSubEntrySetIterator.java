package de.svws_nrw.core.adt.map;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse implementiert einen ENTRY-Iterator, welcher auf der {@link AVLMapSubMap} operiert.
 *
 * @author Thomas Bachran
 * @author Benjamin A. Bartsch
 *
 * @param <K> Der Typ der Schlüssel-Werte.
 * @param <V> Der Typ der zugeordneten Werte.
 */
class AVLMapSubEntrySetIterator<@NotNull K, @NotNull V> implements Iterator<@NotNull Entry<@NotNull K, @NotNull V>> {

	/**
	 * Die {@link AVLMapSubMap} auf der dieser Iterator operiert.
	 */
	private final @NotNull AVLMapSubMap<@NotNull K, @NotNull V> _sub;

	/**
	 * Der aktuelle Eintrag. Ein NULL-Wert bedeutet, dass das Element bereits entfernt wurde oder der Iterator auf einer
	 * ungültigen Position ist, beispielsweise vor dem ersten Element.
	 */
	private Entry<@NotNull K, @NotNull V> _current; // NULL-Wert möglich.

	/**
	 * Der nächste Eintrag.
	 */
	private Entry<@NotNull K, @NotNull V> _next; // NULL-Wert möglich

	/**
	 * Erstellt einen neuen ENTRY-Iterator für die angegebene {@link AVLMapSubMap} im gültigen {@link AVLMapIntervall}.
	 *
	 * @param sub Die {@link AVLMapSubMap} auf der operiert wird.
	 */
	AVLMapSubEntrySetIterator(final @NotNull AVLMapSubMap<@NotNull K, @NotNull V> sub) {
		_sub = sub;
		_current = null;
		_next = _sub.firstEntry();
	}

	@Override
	public @NotNull Entry<@NotNull K, @NotNull V> next() {
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
