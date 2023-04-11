package de.svws_nrw.core.adt.map;

import java.util.Iterator;
import java.util.NoSuchElementException;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse implementiert einen {@link Iterator} für die Klasse {@link ArrayMapCollection},
 * einer View für die Klasse {@link ArrayMap}.
 *
 * @author Thomas Bachran
 *
 * @param <K> Der Typ für die Schlüsselwerte
 * @param <V> Der Typ für die zugeordneten Werte in der {@link ArrayMap}
 */
class ArrayMapCollectionIterator<@NotNull K, @NotNull V> implements Iterator<@NotNull V> {

	/** Die {@link ArrayMap}, deren Schlüsselwerte iteriert werden. */
	private final @NotNull ArrayMap<@NotNull K, @NotNull V> _map;

	/** Die aktuelle Position in der Map. Der Wert null bedeutet, dass sich der Iterator vor dem
	 * ersten Element befindet. */
	private Integer _current;

	/** Die nächste Position in der Map. Der wert null bedeutet, dass kein nächster Eintrag vorhanden ist. */
	private Integer _next;

	/**
	 * Erstellt einen neuen Iterator für die angegebene {@link ArrayMap}
	 *
	 * @param map  die {@link ArrayMap}
	 */
	ArrayMapCollectionIterator(final @NotNull ArrayMap<@NotNull K, @NotNull V> map) {
		_map = map;
		_current = null;
		_next = getNextIndex(_current);
	}

	private Integer getNextIndex(final Integer from) {
		final int start = (from == null) ? 0 : from + 1;
		for (int i = start; i < _map.getNumberOfKeys(); i++) {
			if (_map.getEntryByIndex(i) != null)
				return i;
		}
		return null;
	}

	@Override
	public @NotNull V next() {
		if (_next == null)
			throw new NoSuchElementException();
		_current = _next;
		_next = getNextIndex(_current);
		final V result = _map.getValueAt(_current);
		if (result == null)
			throw new NoSuchElementException();
		return result;
	}

	@Override
	public final boolean hasNext() {
		return _next != null;
	}

	@Override
	public void remove() {
		if (_current == null)
			throw new IllegalStateException();
		_map.remove(_map.getKeyAt(_current));
		_current = null;
	}

}
