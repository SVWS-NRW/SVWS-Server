package de.nrw.schule.svws.core.adt.map;

import java.util.Iterator;
import java.util.NoSuchElementException;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse implementiert einen VALUES-Iterator, welcher auf der {@link AVLMapSubMap} operiert. <br>
 * Alle Operation werden an die {@link AVLMapSubMap} delegiert.
 *
 * @author Thomas Bachran
 * @author Benjamin A. Bartsch
 * 
 * @param <K> Der Typ der Schlüssel-Werte.
 * @param <V> Der Typ der zugeordneten Werte.
 */
class AVLMapSubCollectionIterator<@NotNull K, @NotNull V> implements Iterator<@NotNull V> {

	/**
	 * Die {@link AVLMapSubMap} auf der dieser Iterator operiert.
	 */
	private final @NotNull AVLMapSubMap<@NotNull K, @NotNull V> _sub;

	/**
	 * Der aktuelle Eintrag. Ein NULL-Wert bedeutet, dass das Element bereits entfernt wurde oder der Iterator auf einer
	 * ungültigen Position ist (z.B. vor dem ersten Element).
	 */
	private AVLMapNode<@NotNull K, @NotNull V> _current; // NULL-Wert erlaubt.

	/**
	 * Der nächste Eintrag. Ein NULL-Wert bedeutet, dass kein nächster Eintrag existiert.
	 */
	private AVLMapNode<@NotNull K, @NotNull V> _next; // NULL-Wert erlaubt.

	/**
	 * Erstellt einen neuen VALUES-Iterator, welcher auf der {@link AVLMapSubMap} operiert.
	 * 
	 * @param sub Die {@link AVLMapSubMap} auf der dieser Iterator operiert.
	 */
	AVLMapSubCollectionIterator(@NotNull AVLMapSubMap<@NotNull K, @NotNull V> sub) {
		_sub = sub;
		_current = null;
		_next = _sub.bcGetFirstEntryAsNode();
	}

	@Override
	public @NotNull V next() {
		if (_next == null)
			throw new NoSuchElementException();
		_current = _next;
		_next = _sub.bcGetNextEntryOrNull(_current);
		return _current._val;
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
