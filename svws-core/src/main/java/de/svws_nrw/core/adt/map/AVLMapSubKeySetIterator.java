package de.svws_nrw.core.adt.map;

import java.util.Iterator;
import java.util.NoSuchElementException;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse implementiert einen KEY-Iterator, welcher auf der {@link AVLMapSubMap} operiert.
 *
 * @author Thomas Bachran
 * @author Benjamin A. Bartsch
 *
 * @param <K> Der Typ für die Schlüsselwerte der {@link AVLMap}.
 * @param <V> Der Typ für die Werte der {@link AVLMap}, die den Schlüsselwerten zugeordnet sind.
 */
class AVLMapSubKeySetIterator<@NotNull K, @NotNull V> implements Iterator<@NotNull K> {

	/**
	 * Die {@link AVLMap} auf der diese Sup-Map operiert.
	 */
	private final @NotNull AVLMapSubMap<@NotNull K, @NotNull V> _sub;

	/**
	 * Der aktuelle Eintrag. Ein NULL-Wert bedeutet, dass das Element bereits entfernt wurde oder der Iterator auf
	 * einer ungültigen Position ist (z.B. vor dem ersten Element).
	 */
	private AVLMapNode<@NotNull K, @NotNull V> _current; // NULL-Wert erlaubt.

	/**
	 * Der nächste Eintrag.
	 */
	private AVLMapNode<@NotNull K, @NotNull V> _next; // NULL-Wert erlaubt.

	/**
	 * Erstellt einen neuen KEY-Iterator für die angegebene {@link AVLMapSubMap} im gültigen Bereich
	 * {@link AVLMapIntervall}.
	 *
	 * @param sub Die {@link AVLMapSubMap} auf der operiert wird.
	 */
	AVLMapSubKeySetIterator(final @NotNull AVLMapSubMap<@NotNull K, @NotNull V> sub) {
		_sub = sub;
		_current = null;
		_next = _sub.bcGetFirstEntryAsNode();
	}

	@Override
	public @NotNull K next() {
		if (_next == null)
			throw new NoSuchElementException();
		_current = _next;
		_next = _sub.bcGetNextEntryOrNull(_next);
		return _current._key;
	}

	@Override
	public final boolean hasNext() {
		return _next != null;
	}

	@Override
	public void remove() {
		if (_current == null)
			throw new IllegalStateException();
		_sub.remove(_current._key);
		_current = null;
	}

}
