package de.nrw.schule.svws.core.adt.map;

import java.util.Map.Entry;

import jakarta.validation.constraints.NotNull;

/**
 * @author Benjamin A. Bartsch
 */
public class DummyMapEntry implements Entry<@NotNull Integer, @NotNull Integer> {

	/**
	 * Der Schlüsselwert.
	 */
	private @NotNull Integer _key;

	/**
	 * Der zum Schlüsselwert zugeordnete Wert.
	 */
	private @NotNull Integer _val;

	/**
	 * Konstruktor zum Erzeugen eines Tupels von zwei Integer Werten (key, val).
	 * 
	 * @param key Der Schlüsselwert.
	 * @param val Der zum Schlüsselwert zugeordnete Wert.
	 */
	public DummyMapEntry(@NotNull Integer key, @NotNull Integer val) {
		_key = key;
		_val = val;
	}

	@Override
	public String toString() {
		return "[" + _key + ", " + _val + "]";
	}

	@Override
	public boolean equals(Object o) {
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
	public @NotNull Integer getKey() {
		return _key;
	}

	@Override
	public @NotNull Integer getValue() {
		return _val;
	}

	@Override
	public @NotNull Integer setValue(@NotNull Integer value) {
		throw new UnsupportedOperationException(); // Kann sonst den Zustand zerstören.
		// Integer oldValue = _val;
		// _val = value;
		// return oldValue;
	}

}
