package de.nrw.schule.svws.core.adt.map;

import java.util.Map.Entry;

import jakarta.validation.constraints.NotNull;

/**
 * 
 * Diese Klasse definiert ein Knotenelement im Baum der {@link AVLMap}. Der Baum ist eine balancierter AVL-Baum mit
 * weiteren Informationen wie Nachfolger (_next), Vorgänger (_prev) und die Anzahl der Elemente im Sub-Baum (_size)
 * zuzüglich des Elemente im Knoten.
 * 
 * @author Thomas Bachran
 * @author Benjamin A. Bartsch
 *
 * @param <K> Der Typ des Schlüssel-Eintrages.
 * @param <V> Der Typ des zugeordneten Wertes des Schlüssel-Eintrages.
 */
public class AVLMapNode<@NotNull K, @NotNull V> implements Entry<@NotNull K, @NotNull V> {

	/**
	 * Der Schlüsselwert des Baum-Eintrags.
	 */
	final @NotNull K _key;

	/**
	 * Der zum Schlüsselwert zugeordnete Wert des Baum-Eintrags.
	 */
	@NotNull
	V _val;

	/**
	 * Der Vorgänger-Knoten.
	 */
	AVLMapNode<@NotNull K, @NotNull V> _prev = null; // NULL-Wert erlaubt.

	/**
	 * Der Nachfolger-Knoten.
	 */
	AVLMapNode<@NotNull K, @NotNull V> _next = null; // NULL-Wert erlaubt.

	/**
	 * Das linke Kind des Knotens.
	 */
	AVLMapNode<@NotNull K, @NotNull V> _childL = null; // NULL-Wert erlaubt.

	/**
	 * Das rechte Kind des Knotens.
	 */
	AVLMapNode<@NotNull K, @NotNull V> _childR = null; // NULL-Wert erlaubt.

	/**
	 * Die Höhe des Teilbaums dieses Knotens.
	 */
	int _height = 1;

	/**
	 * Die Summe aller Elemente der Sub-Bäume plus diesem Element.
	 */
	int _size = 1;

	/**
	 * Erstellt ein neues Blatt des Baumes.
	 * 
	 * @param key Der Schlüssel (Key). Der Wert darf nicht NULL sein.
	 * @param val Der Wert (Value), welcher dem Schlüssel (Key) zugeordnet ist. Der Wert darf nicht NULL sein.
	 */
	AVLMapNode(@NotNull K key, @NotNull V val) {
		_key = key;
		_val = val;
	}

	@Override
	public @NotNull String toString() {
		return "[" + _key + ", " + _val + "]";
	}

	@Override
	public boolean equals(@NotNull Object o) {
		if (o instanceof Entry<?, ?> == false)
			return false;
		Entry<?, ?> e = (Entry<?, ?>) o;
		return _key.equals(e.getKey()) && (_val.equals(e.getValue()));
	}

	@Override
	public int hashCode() {
		return _key.hashCode() ^ _val.hashCode();
	}

	@Override
	public @NotNull K getKey() {
		return _key;
	}

	@Override
	public @NotNull V getValue() {
		return _val;
	}

	@Override
	public @NotNull V setValue(@NotNull V value) {
		throw new UnsupportedOperationException(); // Kann sonst den Zustand zerstören.
		// V old = _val;
		// _val = value;
		// return old;
	}

}
