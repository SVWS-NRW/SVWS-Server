package de.svws_nrw.core.adt;

import java.util.HashMap;

import de.svws_nrw.core.adt.map.AVLMap;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse dient als Meta-Schlüssel für eine Menge an Schlüsseln des Typs "long".<br>
 *
 * Der Schlüssel lässt sich im Anschluss bei einer Datenstruktur wie z. B. bei einer {@link HashMap}
 * oder einer {@link AVLMap} benutzen.
 *
 * @author Benjamin A. Bartsch
 */
public final class LongArrayKey implements Comparable<@NotNull LongArrayKey> {

	private final @NotNull long[] _keys;
	private final int _hashcode;

	/**
	 * Der Konstruktor kopiert sich die Schlüsselwerte und berechnet direkt den Hashwert.
	 *
	 * @param arrayOfKeys  Das Array der Schlüsselwerte.
	 */
	public LongArrayKey(final @NotNull long[] arrayOfKeys) {
		_keys = new long[arrayOfKeys.length];

		int hashCode = 1;
		for (int i = 0; i < arrayOfKeys.length; i++) {
			final long value = arrayOfKeys[i];
			_keys[i] = value;
			hashCode = 31 * hashCode + (int) (value ^ (value >>> 32));
		}

		_hashcode = hashCode;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null)
			return false;

		if (this == obj)
			return true;

		if (!(obj instanceof LongArrayKey))
			return false;

		final @NotNull long[] keys2 = ((LongArrayKey) obj)._keys;

		if (_keys.length != keys2.length)
			return false;

		for (int i = 0; i < _keys.length; i++)
			if (_keys[i] != keys2[i])
				return false;

		return true;
	}

	@Override
	public int hashCode() {
		return _hashcode;
	}

	@Override
	public int compareTo(final @NotNull LongArrayKey o) {

		final @NotNull long[] keys2 = o._keys;

		if (_keys.length < keys2.length)
			return -1;
		if (_keys.length > keys2.length)
			return +1;

		for (int i = 0; i < _keys.length; i++) {
			if (_keys[i] < keys2[i])
				return -1;
			if (_keys[i] > keys2[i])
				return +1;
		}

		return 0;
	}

}
