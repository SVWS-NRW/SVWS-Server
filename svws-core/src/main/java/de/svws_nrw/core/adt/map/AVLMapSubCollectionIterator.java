package de.svws_nrw.core.adt.map;

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
class AVLMapSubCollectionIterator<K, V> implements Iterator<V> {

	/**
	 * Die {@link AVLMapSubMap} auf der dieser Iterator operiert.
	 */
	private final @NotNull AVLMapSubMap<K, V> subMap;

	/**
	 * Der aktuelle Eintrag. Ein NULL-Wert bedeutet, dass das Element bereits entfernt wurde oder der Iterator auf einer
	 * ungültigen Position ist (z.B. vor dem ersten Element).
	 */
	private AVLMapNode<K, V> current; // NULL-Wert erlaubt.

	/**
	 * Der nächste Eintrag. Ein NULL-Wert bedeutet, dass kein nächster Eintrag existiert.
	 */
	private AVLMapNode<K, V> next; // NULL-Wert erlaubt.

	/**
	 * Erstellt einen neuen VALUES-Iterator, welcher auf der {@link AVLMapSubMap} operiert.
	 *
	 * @param sub Die {@link AVLMapSubMap} auf der dieser Iterator operiert.
	 */
	AVLMapSubCollectionIterator(final @NotNull AVLMapSubMap<K, V> sub) {
		this.subMap = sub;
		this.current = null;
		this.next = subMap.bcGetFirstEntryAsNode();
	}

	@Override
	public @NotNull V next() {
		if (next == null) {
			throw new NoSuchElementException();
		}
		current = next;
		next = subMap.bcGetNextEntryOrNull(current);
		return current.value;
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
		subMap.remove(current.getKey());
		current = null;
	}

}
