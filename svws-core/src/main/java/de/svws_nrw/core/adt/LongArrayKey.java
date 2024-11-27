package de.svws_nrw.core.adt;

import java.util.Arrays;
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
public final class LongArrayKey implements Comparable<LongArrayKey> {

	private final @NotNull long[] _keys;
	private final int _hashcode;

	/**
	 * Der Konstruktor kopiert sich die Schlüsselwerte und berechnet direkt den Hashwert.
	 *
	 * @param arrayOfKeys  Das Array der Schlüsselwerte.
	 */
	public LongArrayKey(final @NotNull long[] arrayOfKeys) {
		_keys = new long[arrayOfKeys.length];
		System.arraycopy(arrayOfKeys, 0, _keys, 0, arrayOfKeys.length);
		_hashcode = calculateHashcode();
	}

	/**
	 * Konstruktor für einen Parameter.
	 *
	 * @param v1   Der 1. Parameter
	 */
	public LongArrayKey(final long v1) {
		_keys = new long[] {v1};
		_hashcode = calculateHashcode();
	}

	/**
	 * Konstruktor für zwei Parameter.
	 *
	 * @param v1   Der 1. Parameter
	 * @param v2   Der 2. Parameter
	 */
	public LongArrayKey(final long v1, final long v2) {
		_keys = new long[] {v1, v2};
		_hashcode = calculateHashcode();
	}

	/**
	 * Konstruktor für drei Parameter.
	 *
	 * @param v1   Der 1. Parameter
	 * @param v2   Der 2. Parameter
	 * @param v3   Der 3. Parameter
	 */
	public LongArrayKey(final long v1, final long v2, final long v3) {
		_keys = new long[] {v1, v2, v3};
		_hashcode = calculateHashcode();
	}

	/**
	 * Konstruktor für vier Parameter.
	 *
	 * @param v1   Der 1. Parameter
	 * @param v2   Der 2. Parameter
	 * @param v3   Der 3. Parameter
	 * @param v4   Der 4. Parameter
	 */
	public LongArrayKey(final long v1, final long v2, final long v3, final long v4) {
		_keys = new long[] {v1, v2, v3, v4};
		_hashcode = calculateHashcode();
	}

	private int calculateHashcode() {
		int hashCode = 1;
		for (int i = 0; i < _keys.length; i++) {
			final long value = _keys[i];
			hashCode = (31 * hashCode) + (int) (value ^ (value >>> 32));
		}
		return hashCode;
	}

	@Override
	public String toString() {
		return Arrays.toString(_keys);
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

	/**
	 * Liefert den Key-Wert an dem Index.
	 *
	 * @param i    der Index
	 *
	 * @return den Key-Wert an dem Index.
	 */
	public long getKeyAt(final int i) {
		return _keys[i];
	}

}
