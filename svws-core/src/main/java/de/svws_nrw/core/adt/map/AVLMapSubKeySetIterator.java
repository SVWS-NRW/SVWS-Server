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
class AVLMapSubKeySetIterator<K, V> implements Iterator<K> {

	/**
	 * Die {@link AVLMap} auf der diese Sup-Map operiert.
	 */
	private final @NotNull AVLMapSubMap<K, V> delegate;

	/**
	 * Der aktuelle Eintrag. Ein NULL-Wert bedeutet, dass das Element bereits entfernt wurde oder der Iterator auf
	 * einer ungültigen Position ist (z.B. vor dem ersten Element).
	 */
	private AVLMapNode<K, V> current; // NULL-Wert erlaubt.

	/**
	 * Der nächste Eintrag.
	 */
	private AVLMapNode<K, V> next; // NULL-Wert erlaubt.

	/**
	 * Erstellt einen neuen KEY-Iterator für die angegebene {@link AVLMapSubMap} im gültigen Bereich
	 * {@link AVLMapIntervall}.
	 *
	 * @param sub Die {@link AVLMapSubMap} auf der operiert wird.
	 */
	AVLMapSubKeySetIterator(final @NotNull AVLMapSubMap<K, V> sub) {
		this.delegate = sub;
		this.current = null;
		this.next = delegate.bcGetFirstEntryAsNode();
	}

	@Override
	public @NotNull K next() {
		if (next == null) {
			throw new NoSuchElementException();
		}
		current = next;
		next = delegate.bcGetNextEntryOrNull(next);
		return current.key;
	}

	@Override
	public final boolean hasNext() {
		return next != null;
	}

	@Override
	public void remove() {
		if (current == null) {
			throw new IllegalStateException();
		}
		delegate.remove(current.key);
		current = null;
	}

}
