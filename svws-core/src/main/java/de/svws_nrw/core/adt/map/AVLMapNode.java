package de.svws_nrw.core.adt.map;

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
public final class AVLMapNode<K, V> implements Entry<K, V> {

	/**
	 * Der Schlüsselwert des Baum-Eintrags.
	 */
	final @NotNull K key;

	/**
	 * Der zum Schlüsselwert zugeordnete Wert des Baum-Eintrags.
	 */
	@NotNull
	V value;

	/**
	 * Der Vorgänger-Knoten.
	 */
	AVLMapNode<K, V> prev = null; // NULL-Wert erlaubt.

	/**
	 * Der Nachfolger-Knoten.
	 */
	AVLMapNode<K, V> next = null; // NULL-Wert erlaubt.

	/**
	 * Das linke Kind des Knotens.
	 */
	AVLMapNode<K, V> childL = null; // NULL-Wert erlaubt.

	/**
	 * Das rechte Kind des Knotens.
	 */
	AVLMapNode<K, V> childR = null; // NULL-Wert erlaubt.

	/**
	 * Die Höhe des Teilbaums dieses Knotens.
	 */
	int height = 1;

	/**
	 * Die Summe aller Elemente der Sub-Bäume plus diesem Element.
	 */
	int size = 1;

	/**
	 * Erstellt ein neues Blatt des Baumes.
	 *
	 * @param key Der Schlüssel (Key). Der Wert darf nicht NULL sein.
	 * @param val Der Wert (Value), welcher dem Schlüssel (Key) zugeordnet ist. Der Wert darf nicht NULL sein.
	 */
	AVLMapNode(final @NotNull K key, final @NotNull V val) {
		this.key = key;
		this.value = val;
	}

	@Override
	public @NotNull String toString() {
		return "[" + key + ", " + value + "]";
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof Entry<?, ?>)) {
			return false;
		}
		final Entry<?, ?> e = (Entry<?, ?>) o;
		return key.equals(e.getKey()) && (value.equals(e.getValue()));
	}

	@Override
	public int hashCode() {
		return key.hashCode() ^ value.hashCode();
	}

	@Override
	public @NotNull K getKey() {
		return key;
	}

	@Override
	public @NotNull V getValue() {
		return value;
	}

	@Override
	public @NotNull V setValue(final @NotNull V value) {
		throw new UnsupportedOperationException(); // Kann sonst den Zustand zerstören.
	}

}
