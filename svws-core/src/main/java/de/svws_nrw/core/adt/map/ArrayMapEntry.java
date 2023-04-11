package de.svws_nrw.core.adt.map;

import java.util.Map.Entry;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse definiert ein Eintrag in der {@link ArrayMap}.
 *
 * @author Thomas Bachran
 *
 * @param <K> Der Typ des Schlüssel-Eintrages.
 * @param <V> Der Typ des zugeordneten Wertes des Schlüssel-Eintrages.
 */
final class ArrayMapEntry<@NotNull K, @NotNull V> implements Entry<@NotNull K, @NotNull V> {

	/** Der Schlüsselwert. */
	final @NotNull K _key;

	/** Der zugeordnete Wert. */
	final @NotNull V _val;


	/**
	 * Erstellt einen neuen Eintrag.
	 *
	 * @param key   Der Schlüsselwert. Dieser darf nicht NULL sein.
	 * @param val   Der zugeordnete Wert. Dieser darf nicht NULL sein.
	 */
	ArrayMapEntry(final @NotNull K key, final @NotNull V val) {
		_key = key;
		_val = val;
	}

	@Override
	public @NotNull String toString() {
		return "(" + _key + ", " + _val + ")";
	}

	@Override
	public boolean equals(final @NotNull Object o) {
		if (!(o instanceof Entry<?, ?>))
			return false;
		final Entry<?, ?> e = (Entry<?, ?>) o;
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
	public @NotNull V setValue(final @NotNull V value) {
		throw new UnsupportedOperationException();
	}

}
