package de.svws_nrw.core.adt.map;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.function.Function;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt eine {@link NavigableMap} zur Verfügung, welcher eine Zuordnung von
 * Schlüsseln (Keys) des Typs K zu Werten (Value) vom Typ V unterstützt. Hierbei wird eine
 * Schlüsselmenge unterstützt, welche in einem Array mit Elementen des Typs K übergeben werden.
 * Außerdem muss eine weitere Funktion übergeben werden, welche die Zuordnung eines
 * Elementes auf den Index in dem Array zur Verfügung stellt.
 * Diese Datenstruktur lässt sich gut bei Enums einsetzen.
 *
 * @author Thomas Bachran
 *
 * @param <K> Der Typ der Schlüssel-Werte.
 * @param <V> Der Typ der zugeordneten Werte.
 */
public final class ArrayMap<@NotNull K, @NotNull V> implements Map<@NotNull K, @NotNull V> {

	/** Das Array mit allen gültigen Schlüsselwerten */
	private final @NotNull K @NotNull [] keyArray;

	/** Die Funktion, welche jedem Schlüsselwert einem Index im Array zuordnet. */
	private final @NotNull Function<@NotNull K, @NotNull Integer> keyIndexFunction;

	/** Das Array mit den Einträgen der Map. Dieses hat die gleiche Länge wie das keyArray. Nicht vorhandene Einträge sind mit null initialisiert. */
	private final @NotNull ArrayMapEntry<@NotNull K, @NotNull V>[] entries;

	/** Das Key-Set als View auf diese Map */
	private final @NotNull Set<@NotNull K> _keySet;

	/** Das Entry-Set als View auf diese Map */
	private final @NotNull Collection<@NotNull V> _collection;

	/** Das Entry-Set als View auf diese Map */
	private final @NotNull Set<@NotNull Entry<@NotNull K, @NotNull V>> _entrySet;

	private @NotNull int numEntries = 0;


	/** Die Funktion, welche jedem Schlüsselwert einem Index im Array zuordnet - für den Fall, dass es sich um einen Enum-Type handelt. */
	private final @NotNull Function<@NotNull K, @NotNull Integer> keyIndexFunctionEnum = (final @NotNull K key) -> {
		final boolean isEnum = key instanceof Enum<?>;
		if (!isEnum)
			throw new IllegalArgumentException("Der Schlüsselwerte ist keine Enum-Konstanten und somit nicht zulässig.");
		return ((Enum<?>) key).ordinal();
	};


	/**
	 * Erzeugt eine neue {@link ArrayMap} basierend auf dem übergebenen Array mit den Schlüsselwerten.
	 * Bei diesem Konstruktor müssen die Schlüsselwerte Elemente einer Aufzählung (Enum) sein.
	 *
	 * @param keyArray           das Array mit den Schlüsselwerten
	 */
	@SuppressWarnings("unchecked")
	public ArrayMap(final @NotNull K @NotNull [] keyArray) {
		if (keyArray.length <= 0)
			throw new IllegalArgumentException("Das Array mit den gültigen Schlüsselwerten darf nicht leer sein.");
		final @NotNull K firstKey = keyArray[0];
		if (!(firstKey instanceof Enum<?>))
			throw new IllegalArgumentException("Enthält das Array der Schlüsselwerte keine Enum-Konstanten, so muss ein Funktion für die Zuordnung von Schlüsselwerten angegeben werden.");
		this.keyArray = keyArray;
		this.keyIndexFunction = keyIndexFunctionEnum;
		this.entries = (ArrayMapEntry<@NotNull K, @NotNull V>[]) Array.newInstance(ArrayMapEntry.class, keyArray.length);
		this._keySet = new ArrayMapKeySet<>(this);
		this._collection = new ArrayMapCollection<>(this);
		this._entrySet = new ArrayMapEntrySet<>(this);
	}

	/**
	 * Erzeugt eine neue {@link ArrayMap} basierend auf dem übergebenen Array mit den Schlüsselwerten
	 * und der zugehörigen Funktion zur Bestimmung des Index eines Schlüsselwertes im Array.
	 *
	 * @param keyArray           das Array mit den Schlüsselwerten
	 * @param keyIndexFunction   die Funktion zur Bestimmung des Index eines Schlüsselwertes im Array
	 */
	@SuppressWarnings("unchecked")
	public ArrayMap(final @NotNull K @NotNull [] keyArray, final @NotNull Function<@NotNull K, @NotNull Integer> keyIndexFunction) {
		if (keyArray.length <= 0)
			throw new IllegalArgumentException("Das Array mit den gültigen Schlüsselwerten darf nicht leer sein.");
		this.keyArray = keyArray;
		this.keyIndexFunction = keyIndexFunction;
		this.entries = (ArrayMapEntry<@NotNull K, @NotNull V>[]) Array.newInstance(ArrayMapEntry.class, keyArray.length);
		this._keySet = new ArrayMapKeySet<>(this);
		this._collection = new ArrayMapCollection<>(this);
		this._entrySet = new ArrayMapEntrySet<>(this);
	}

	@Override
	public @NotNull Set<@NotNull K> keySet() {
		return _keySet;
	}

	@Override
	public @NotNull Collection<@NotNull V> values() {
		return _collection;
	}

	@Override
	public @NotNull Set<@NotNull Entry<@NotNull K, @NotNull V>> entrySet() {
		return _entrySet;
	}

	/**
	 * Gibt die Anzahl der möglichen Schlüsselwerte für diese
	 * Map zurück. Das entspricht der Länge des Schlüsselwert-Arrays.
	 *
	 * @return die Anzahl der möglichen Schlüsselwerte
	 */
	public int getNumberOfKeys() {
		return this.keyArray.length;
	}

	@Override
	public int size() {
		return this.numEntries;
	}

	@Override
	public boolean isEmpty() {
		return this.numEntries == 0;
	}

	private boolean isValidIndex(final int index) {
		return ((index >= 0) && (index < this.keyArray.length));
	}

	/**
	 * Gibt den Schlüsselwert an der übergebenen Stelle index im
	 * Array der Schlüsselwerte zurück.
	 *
	 * @param index   die Stelle im Array der Schlüsselwerte
	 *
	 * @return der Schlüsselwerte oder null, falls der Index nicht gültig ist.
	 */
	public K getKeyAt(final int index) {
		return isValidIndex(index) ? this.keyArray[index] : null;
	}

	/**
	 * Ermittelt für den übergebenen Index auf das Array der Schlüsselwerte
	 * den, dem entsprechenden Schlüsselwert, zugeordneten Eintrag.
	 *
	 * @param index  der Index in das Array der Schlüsselwerte
	 *
	 * @return der zugeordnete Eintrag oder null, wenn kein Eintrag zugeordnet ist
	 */
	ArrayMapEntry<@NotNull K, @NotNull V> getEntryByIndex(final int index) {
		return isValidIndex(index) ? this.entries[index] : null;
	}

	/**
	 * Ermittelt für den übergebenen Schlüsselwert den zugeordneten Eintrag.
	 *
	 * @param key  der Schlüsselwert
	 *
	 * @return der zugeordnete Eintrag oder null, wenn kein Eintrag zugeordnet ist
	 */
	ArrayMapEntry<@NotNull K, @NotNull V> getEntry(final Object key) {
		@SuppressWarnings("unchecked")
		final K k = (K) key;
		if (k == null)
			return null;
		final int index = keyIndexFunction.apply(k);
		return getEntryByIndex(index);
	}

	@Override
	public boolean containsKey(final Object key) {
		return this.getEntry(key) != null;
	}

	@Override
	public boolean containsValue(final Object value) {
		if (value == null)
			return false;
		for (final ArrayMapEntry<@NotNull K, @NotNull V> entry : entries)
			if ((entry != null) && (value.equals(entry.getValue())))
				return true;
		return false;
	}

	@Override
	public V get(final Object key) {
		final ArrayMapEntry<@NotNull K, @NotNull V> entry = this.getEntry(key);
		return (entry == null) ? null : entry.getValue();
	}

	/**
	 * Bestimmt den Wert aus der Map anhand des Schlüsselwertes, welcher in dem
	 * Array der Schlüsselwerte an der Stelle index steht.
	 *
	 * @param index   die Stelle im Array der Schlüsselwerte
	 *
	 * @return der Wert an der Stelle oder null, falls kein Wert zugeordnet ist
	 *
	 * @throws ArrayIndexOutOfBoundsException falls der Index ungültig ist
	 */
	public V getValueAt(final int index) {
		if (!isValidIndex(index))
			throw new ArrayIndexOutOfBoundsException("Fehlerhafter Index für die Schlüsselwerte");
		final ArrayMapEntry<@NotNull K, @NotNull V> entry = this.getEntryByIndex(index);
		return (entry == null) ? null : entry.getValue();
	}

	@Override
	public V put(@NotNull final K key, @NotNull final V value) {
		final int index = keyIndexFunction.apply(key);
		if (!isValidIndex(index))
			throw new IllegalArgumentException("Der Schlüsselwert ist ungültig und kann keinem Index zugeordnet werden.");
		final ArrayMapEntry<@NotNull K, @NotNull V> entry = this.getEntryByIndex(index);
		if (entry == null)
			this.numEntries++;
		this.entries[index] = new ArrayMapEntry<>(key, value);
		return entry == null ? null : entry.getValue();
	}

	@Override
	public V remove(final Object key) {
		if (key == null)
			throw new NullPointerException("Der Schlüsselwert darf nicht null sein.");
		@SuppressWarnings("unchecked")
		final int index = keyIndexFunction.apply((K) key);
		if (!isValidIndex(index))
			return null;
		final ArrayMapEntry<@NotNull K, @NotNull V> entry = this.getEntryByIndex(index);
		if (entry != null) {
			this.entries[index] = null;
			this.numEntries--;
		}
		return entry == null ? null : entry.getValue();
	}

	@Override
	public void putAll(final Map<? extends @NotNull K, ? extends @NotNull V> map) {
		if (map == null)
			throw new NullPointerException("Der Parameter map darf nicht null sein.");
		for (final @NotNull Entry<? extends @NotNull K, ? extends @NotNull V> entry : map.entrySet())
			this.put(entry.getKey(), entry.getValue());
	}

	@Override
	public void clear() {
		Arrays.fill(entries, null);
		this.numEntries = 0;
	}

}
