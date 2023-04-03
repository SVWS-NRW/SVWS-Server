package de.svws_nrw.core.adt.map;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse implementiert das {@link NavigableMap}-Interface und dient zur Zuordnung von Integer Werten im Bereich
 * [0;Maximimalwert-1] zu einem anderen Integer Wert. Die Zuordnung wird in Integer-Array gespeichert.
 *
 * @author Benjamin A. Bartsch
 */
public final class DummyMap implements NavigableMap<Integer, Integer> {

	/**
	 * Die zugeordneten Key-Value-Daten. Existiert keine Zuordnung, so ist ein NULL-Wert im Array.
	 */
	private final Integer[] _isMapped;

	/**
	 * Diese Map erzeugt eine Sub-Map, welche das gesamte Intervall einschließt. Die Sub-Map widerum delegiert ihre
	 * Aufrufe zurück an diese Map (package-protected Methoden).
	 */
	private final DummyMapSub _sub;

	/**
	 * Gibt an, ob das Hinzufügen von KEYs ohne VALUE erlaubt ist. Falls TRUE, dann wird der KEY einer Pseudo-VALUE
	 * zugeordnet.
	 */
	private boolean _allowKeyAlone = false;

	/**
	 * Erzeugt eineMap mit Hilfe eines Arrays der Größe {@code maxValueExclusive} zum Zuordnen von Werten von 0 bis
	 * maxValueExclusive-1 auf einen Integer-Wert.
	 *
	 * @param maxValueExclusive Der größte erlaubte Integer-Wert (exklusiv) im Set. Der kleinste Wert ist 0.
	 */
	public DummyMap(final int maxValueExclusive) {
		_isMapped = new Integer[maxValueExclusive];
		_sub = new DummyMapSub(this, new DummyMapIntervall(0, true, maxValueExclusive, false), true);
	}

	/**
	 * Bewirkt, dass das Hinzufügen von Keys ohne Value durch {@link DummyMapSubKeySet} erlaubt ist. Die Keys werden auf
	 * einen Dummy-Wert gemapped.
	 *
	 * @param b Falls TRUE, dürfen KEYs ohne VALUE hinzugefügt werden.
	 */
	public void allowKeyAlone(final boolean b) {
		_allowKeyAlone = b;
	}

	// ########################################################################
	// ######################## DELEGIEREN AN SUB MAP #########################
	// ########################################################################

	@Override
	public String toString() {
		return _sub.toString();
	}

	@Override
	public int hashCode() {
		return _sub.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		return _sub.equals(obj);
	}

	@Override
	public @NotNull Comparator<? super Integer> comparator() {
		return _sub.comparator();
	}

	@Override
	public @NotNull Integer firstKey() {
		return _sub.firstKey();
	}

	@Override
	public @NotNull Integer lastKey() {
		return _sub.lastKey();
	}

	@Override
	public @NotNull Set<Integer> keySet() {
		return _sub.keySet();
	}

	@Override
	public @NotNull Collection<Integer> values() {
		return _sub.values();
	}

	@Override
	public @NotNull Set<Entry<Integer, Integer>> entrySet() {
		return _sub.entrySet();
	}

	@Override
	public int size() {
		return _sub.size();
	}

	@Override
	public boolean isEmpty() {
		return _sub.isEmpty();
	}

	@Override
	public boolean containsKey(final Object key) {
		return _sub.containsKey(key);
	}

	@Override
	public boolean containsValue(final Object value) {
		return _sub.containsValue(value);
	}

	@Override
	public Integer get(final Object key) {
		return _sub.get(key);
	}

	@Override
	public Integer put(final Integer key, final Integer value) {
		return _sub.put(key, value);
	}

	@Override
	public Integer remove(final Object key) {
		return _sub.remove(key);
	}

	@Override
	public void putAll(final Map<? extends Integer, ? extends Integer> m) {
		_sub.putAll(m);
	}

	@Override
	public void clear() {
		_sub.clear();
	}

	@Override
	public Entry<Integer, Integer> lowerEntry(final Integer key) {
		return _sub.lowerEntry(key);
	}

	@Override
	public Integer lowerKey(final Integer key) {
		return _sub.lowerKey(key);
	}

	@Override
	public Entry<Integer, Integer> floorEntry(final Integer key) {
		return _sub.floorEntry(key);
	}

	@Override
	public Integer floorKey(final Integer key) {
		return _sub.floorKey(key);
	}

	@Override
	public Entry<Integer, Integer> ceilingEntry(final Integer key) {
		return _sub.ceilingEntry(key);
	}

	@Override
	public Integer ceilingKey(final Integer key) {
		return _sub.ceilingKey(key);
	}

	@Override
	public Entry<Integer, Integer> higherEntry(final Integer key) {
		return _sub.higherEntry(key);
	}

	@Override
	public Integer higherKey(final Integer key) {
		return _sub.higherKey(key);
	}

	@Override
	public Entry<Integer, Integer> firstEntry() {
		return _sub.firstEntry();
	}

	@Override
	public Entry<Integer, Integer> lastEntry() {
		return _sub.lastEntry();
	}

	@Override
	public Entry<Integer, Integer> pollFirstEntry() {
		return _sub.pollFirstEntry();
	}

	@Override
	public Entry<Integer, Integer> pollLastEntry() {
		return _sub.pollLastEntry();
	}

	@Override
	public NavigableMap<Integer, Integer> descendingMap() {
		return _sub.descendingMap();
	}

	@Override
	public NavigableSet<Integer> navigableKeySet() {
		return _sub.navigableKeySet();
	}

	@Override
	public NavigableSet<Integer> descendingKeySet() {
		return _sub.descendingKeySet();
	}

	@Override
	public NavigableMap<Integer, Integer> subMap(final Integer fromKey, final boolean fromInclusive, final Integer toKey,
			final boolean toInclusive) {
		return _sub.subMap(fromKey, fromInclusive, toKey, toInclusive);
	}

	@Override
	public NavigableMap<Integer, Integer> headMap(final Integer toKey, final boolean inclusive) {
		return _sub.headMap(toKey, inclusive);
	}

	@Override
	public NavigableMap<Integer, Integer> tailMap(final Integer fromKey, final boolean inclusive) {
		return _sub.tailMap(fromKey, inclusive);
	}

	@Override
	public SortedMap<Integer, Integer> subMap(final Integer fromKey, final Integer toKey) {
		return _sub.subMap(fromKey, toKey);
	}

	@Override
	public SortedMap<Integer, Integer> headMap(final Integer toKey) {
		return _sub.headMap(toKey);
	}

	@Override
	public SortedMap<Integer, Integer> tailMap(final Integer fromKey) {
		return _sub.tailMap(fromKey);
	}

	// ########################################################################
	// ############################ BACK CALLS ################################
	// ########################################################################

	/**
	 * Wird aufgerufen von {@link DummyMapSub#bcAddKey(Integer)}. Bei dem Versuch einen Schlüssel (Key) ohne Wert
	 * (Value) hinzuzufügen, kann es zu einer {@link UnsupportedOperationException} kommen, wenn das Attribut
	 * {@link #_allowKeyAlone} auf FALSE gesetzt ist. Andernfalls wird dem Schlüssel (Key) ein Dummy-Wert zugeordnet.
	 * Der Schlüssel (Key) wird jedoch nur dann hinzugefügt, falls er noch nicht existierte.
	 *
	 * @param iv Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 * @param e  Der Schlüssel (Key) der hinzugefügt werden soll.
	 *
	 * @return TRUE, falls der Schlüssel (Key) noch nicht existierte und somit hinzugefügt wurde.
	 * @throws UnsupportedOperationException wenn ein alleiniges Hinzufügen eines Schlüssels nicht erlaubt ist.
	 */
	boolean bcAddKey(@NotNull final DummyMapIntervall iv, @NotNull final Integer e) {
		if (!_allowKeyAlone)
			throw new UnsupportedOperationException(); // KEY kann nicht ohne VALUE hinzugefügt werden.
		if (bcContainsKey(iv, e))
			return false;
		bcAddEntryReturnOldValueOrNull(iv, e, e);
		return true;
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSub#bcAddAllKeys(Collection)}. Versucht alle Schlüssel (Keys) der Collection
	 * hinzuzufügen. Ob das Hinzufügen eines Schlüssels (Key) ohne Wert (Value) erlaubt ist, hängt vom Attribut
	 * {@link #_allowKeyAlone} ab.
	 *
	 * @param c  Die Collection mit allen Schlüsseln (Keys) die hinzugefügt werden sollen.
	 * @param iv Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 *
	 * @return TRUE, falls mindestens ein Schlüssel (Key) noch nicht existierte und somit hinzugefügt wurde.
	 * @throws UnsupportedOperationException wenn ein alleiniges Hinzufügen eines Schlüssels nicht erlaubt ist.
	 */
	boolean bcAddAllKeys(@NotNull final DummyMapIntervall iv, final Collection<? extends @NotNull Integer> c) {
		boolean changed = false;
		for (final Integer key : c)
			changed |= bcAddKey(iv, key);
		return changed;
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSub#bcAddEntryReturnBool(java.util.Map.Entry)}. Fügt ein Entry der
	 * Datenstruktur hinzu.
	 *
	 * @param e  Das einzufügende Entry.
	 * @param iv Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 *
	 * @return TRUE, falls das Entry (e.getKey(), e.getValue()) neu war und somit hinzugefügt wurde.
	 */
	boolean bcAddEntry(@NotNull final DummyMapIntervall iv, @NotNull final Entry<@NotNull Integer, @NotNull Integer> e) {
		// KEY, VALUE schon vorhanden?
		final Integer key = e.getKey();
		final Integer val = e.getValue();
		if (bcContainsKey(iv, key) && val.equals(bcGetValueOfKeyOrNull(iv, key)))
			return false;
		// Hinzufügen, da neu KEY oder VALUE neu.
		bcAddEntryReturnOldValueOrNull(iv, key, val);
		return true;
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSub#bcAddAllEntries(Collection)}. Fügt alle Entries der Collection der
	 * Datenstruktur hinzu.
	 *
	 * @param c  Die Collection mit den einzufügenden Entries.
	 * @param iv Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 *
	 * @return TRUE, falls mindestens ein Entry neu war und somit hinzugefügt wurde.
	 */
	boolean bcAddAllEntries(@NotNull final DummyMapIntervall iv,
			@NotNull final Collection<? extends @NotNull Entry<@NotNull Integer, @NotNull Integer>> c) {
		boolean changed = false;
		for (@NotNull final Entry<@NotNull Integer, @NotNull Integer> ent : c)
			changed |= bcAddEntry(iv, ent);
		return changed;
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSub#containsKey(Object)}. Überprüft, ob ein Schlüssel (Key) in dieser
	 * Datenstruktur existiert.
	 *
	 * @param key Der Schlüssel (Key) nach dem gesucht wird.
	 * @param iv  Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 *
	 * @return TRUE, falls der Schlüssel (Key) in dieser Datenstruktur existiert.
	 */
	boolean bcContainsKey(@NotNull final DummyMapIntervall iv, @NotNull final Object key) {
		final int e = (Integer) key;
		return iv.contains(e) && (_isMapped[e] != null);
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSub#bcContainsAllKeys(Collection)}. Überprüft, ob alle Schlüssel (Keys) der
	 * Collection in dieser Datenstruktur existieren.
	 *
	 * @param c  Die Collection mit allen Schlüsseln (Keys) welche überprüft werden sollen.
	 * @param iv Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 *
	 * @return TRUE, falls alle Schlüssel (Keys) der Collection in dieser Datenstruktur existieren.
	 */
	boolean bcContainsAllKeys(@NotNull final DummyMapIntervall iv, @NotNull final Collection<?> c) {
		for (@NotNull final Object key : c)
			if (!bcContainsKey(iv, key))
				return false;
		return true;
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSub#containsValue(Object)}. Überprüft, ob ein Wert (Value) in dieser
	 * Datenstruktur existiert.
	 *
	 * @param value Der Wert (Value) nach dem gesucht wird.
	 * @param iv    Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 *
	 * @return TRUE, falls der Wert (Value) in dieser Datenstruktur existiert.
	 */
	boolean bcContainsValue(@NotNull final DummyMapIntervall iv, @NotNull final Object value) {
		for (int i = iv.min(); i <= iv.max(); i++)
			if (valEqual(_isMapped[i], value))
				return true;
		return false;
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSub#bcContainsAllValues(Collection)}. Überprüft, ob alle Werte (Values) aus
	 * der Collection in dieser Datenstruktur vorkommen.
	 *
	 * @param c  Die Collection deren Werte (Values) überprüft werden sollen.
	 * @param iv Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 *
	 * @return TRUE, falls alle Werte (Values) der Collection in dieser Datenstruktur existieren.
	 */
	boolean bcContainsAllValues(@NotNull final DummyMapIntervall iv, @NotNull final Collection<?> c) {
		for (final Object obj : c)
			if (!bcContainsValue(iv, obj))
				return false;
		return true;
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSub#bcContainsEntry(Object)}. Überprüft, ob das übergebene Entry in dieser
	 * Datenstruktur existiert.
	 *
	 * @param o  Das Entry (Schlüssel-Wert-Paar) nach dem gesucht wird.
	 * @param iv Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 *
	 * @return TRUE, falls das übergebene Entry in dieser Datenstruktur existiert.
	 */
	@SuppressWarnings("unchecked")
	boolean bcContainsEntry(@NotNull final DummyMapIntervall iv, final Object o) {
		if (!(o instanceof Entry<?, ?>))
			return false;
		@NotNull
		final Entry<@NotNull Integer, @NotNull Integer> e = (@NotNull Entry<@NotNull Integer, @NotNull Integer>) o;
		final int key = (Integer) e.getKey();
		if (!iv.contains(key))
			return false;
		if (_isMapped[key] == null)
			return false;
		return _isMapped[key].equals(e.getValue());
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSub#bcContainsAllEntries(Collection)}. Überprüft, ob alle Entries der
	 * Collection in dieser Datenstruktur existieren.
	 *
	 * @param c  Die Collection mit den Entries welche überprüft werden sollen.
	 * @param iv Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 *
	 * @return TRUE, falls alle Entries in dieser Datenstruktur existieren.
	 */
	boolean bcContainsAllEntries(@NotNull final DummyMapIntervall iv, @NotNull final Collection<?> c) {
		for (final Object obj : c)
			if (!bcContainsEntry(iv, obj))
				return false;
		return true;
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSub#remove(Object)}. Versucht einen Schlüssel (Key) aus dieser Datenstruktur
	 * zu entfernen. In dieser Implementierung kann ein Schlüssel (Key) keinem NULL-Wert zugeordnet sein. Ist das
	 * Ergebnis NULL, bedeutet dies, dass der Schlüssel (Key) definitiv nicht existierte.
	 *
	 * @param objKey Der Schlüssel (Key), welcher entfernt werden soll.
	 * @param iv     Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 *
	 * @return Liefert den zum Schlüssel (Key) zugehörigen Wert (Value), falls es eine Zuordnung gab, andernfalls NULL.
	 */
	Integer bcRemoveKeyReturnOldValue(@NotNull final DummyMapIntervall iv, @NotNull final Object objKey) {
		final int key = (Integer) objKey;
		if (!iv.contains(key))
			return null; // keine Exception (analog zur JAVA-TreeMap-Implementation)
		final Integer oldValue = _isMapped[key];
		_isMapped[key] = null;
		return oldValue;
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSub#bcRemoveAllKeysReturnBool(Collection)}. Entfernt alle Schlüssel (Keys) aus
	 * dieser Datenstruktur.
	 *
	 * @param c  Die Collection mit allen Schlüsseln (Keys) die entfernt werden sollen.
	 * @param iv Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 *
	 * @return TRUE, falls mindestens ein Schlüssel (Key) entfernt wurde.
	 */
	boolean bcRemoveAllKeyReturnBool(@NotNull final DummyMapIntervall iv, @NotNull final Collection<?> c) {
		boolean changed = false;
		for (@NotNull final Object obj : c)
			changed |= removeKeyReturnBool(iv, obj);
		return changed;
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSub#bcRemoveEntry(Object)}. Entfernt das Entry aus dieser Datenstruktur.
	 *
	 * @param o  Das Entry, welches entfernt werden soll.
	 * @param iv Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 *
	 * @return TRUE, falls das Entry in der Datenstruktur existierte und somit entfernt wurde.
	 */
	@SuppressWarnings({ "unchecked" })
	boolean bcRemoveEntryReturnBool(@NotNull final DummyMapIntervall iv, @NotNull final Object o) {
		if (!bcContainsEntry(iv, o))
			return false;
		@NotNull
		final Entry<@NotNull Integer, @NotNull Integer> e = (@NotNull Entry<@NotNull Integer, @NotNull Integer>) o;
		return removeKeyReturnBool(iv, e.getKey());
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSub#bcRemoveAllEntriesReturnBool(Collection)}. Entfernt alle Entries der
	 * Collection aus dieser Datenstruktur.
	 *
	 * @param c  Die Collection mit den Entries, welche entfernt werden sollen.
	 * @param iv Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 *
	 * @return TRUE, falls mindestens ein Entry entfernt wurde.
	 */
	boolean bcRemoveAllEntriesReturnBool(@NotNull final DummyMapIntervall iv, @NotNull final Collection<?> c) {
		boolean changed = false;
		for (@NotNull final Object obj : c)
			changed |= bcRemoveEntryReturnBool(iv, obj);
		return changed;
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSub#pollFirstEntry()} bzw. {@link DummyMapSub#pollLastEntry()} falls
	 * absteigend sortiert. Entfernt und liefert das erste Entry dieser Datenstruktur.
	 *
	 * @param iv Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 *
	 * @return Entfernt und liefert das erste Entry dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	Entry<@NotNull Integer, @NotNull Integer> bcPollFirstEntryOrNull(@NotNull final DummyMapIntervall iv) {
		for (int i = iv.min(); i <= iv.max(); i++)
			if (_isMapped[i] != null) {
				final Entry<Integer, Integer> e = new DummyMapEntry(i, _isMapped[i]);
				_isMapped[i] = null;
				return e;
			}
		return null;
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSub#bcPollFirstKeyOrNull()} bzw. {@link DummyMapSub#bcPollLastKeyOrNull()}
	 * falls absteigend sortiert. Entfernt und liefert den ersten Schlüssel (Key) dieser Datenstruktur.
	 *
	 * @param iv Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 *
	 * @return Entfernt und liefert den ersten Schlüssel (Key) dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	Integer bcPollFirstKeyOrNull(@NotNull final DummyMapIntervall iv) {
		for (int i = iv.min(); i <= iv.max(); i++)
			if (_isMapped[i] != null) {
				_isMapped[i] = null;
				return i;
			}
		return null;
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSub#pollLastEntry()} bzw. {@link DummyMapSub#pollFirstEntry()} falls
	 * absteigend sortiert. Entfernt und liefert das letzte Entry dieser Datenstruktur.
	 *
	 * @param iv Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 *
	 * @return Entfernt und liefert das letzte Entry dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	Entry<@NotNull Integer, @NotNull Integer> bcPollLastEntryOrNull(@NotNull final DummyMapIntervall iv) {
		for (int i = iv.max(); i >= iv.min(); i--)
			if (_isMapped[i] != null) {
				final Entry<Integer, Integer> e = new DummyMapEntry(i, _isMapped[i]);
				_isMapped[i] = null;
				return e;
			}
		return null;
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSub#bcPollLastKeyOrNull()} bzw. {@link DummyMapSub#bcPollFirstKeyOrNull()}
	 * falls absteigend sortiert. Entfernt und liefert den letzten Schlüssel (Key) dieser Datenstruktur.
	 *
	 * @param iv Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 *
	 * @return Entfernt und liefert den letzten Schlüssel (Key) dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	Integer bcPollLastKeyOrNull(@NotNull final DummyMapIntervall iv) {
		for (int i = iv.max(); i >= iv.min(); i--)
			if (_isMapped[i] != null) {
				_isMapped[i] = null;
				return i;
			}
		return null;
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSub#put(Integer, Integer)}. Fügt ein Entry bzw. ein Key-Value-Paar der
	 * Datenstruktur hinzu.
	 *
	 * @param key   Der Schlüssel (Key) des Entrys.
	 * @param value Der zum Schlüssel (Key) zugehörige Wert (Value).
	 * @param iv    Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 *
	 * @return Den alten Wert (Value), falls ein zugehöriger Schlüssel (Key) existierte, sonst NULL.
	 */
	Integer bcAddEntryReturnOldValueOrNull(@NotNull final DummyMapIntervall iv, @NotNull final Integer key,
			@NotNull final Integer value) {
		if (!(iv.contains(key)))
			throw new IllegalArgumentException("Der Schlüsselwert liegt nicht im gültigen Bereich.");
		final Integer oldValue = _isMapped[key];
		_isMapped[key] = value;
		return oldValue;
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSub#putAll(Map)}. Fügt alle Entrys der übergebenen Map dieser Datenstruktur
	 * hinzu.
	 *
	 * @param map Die Map, deren Entries dieser Datenstruktur hinzugefügt werden soll.
	 * @param iv  Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 */
	void bcAddAllEntriesOfMap(@NotNull final DummyMapIntervall iv,
			@NotNull final Map<? extends @NotNull Integer, ? extends @NotNull Integer> map) {
		for (final Entry<? extends @NotNull Integer, ? extends @NotNull Integer> e : map.entrySet())
			bcAddEntryReturnOldValueOrNull(iv, e.getKey(), e.getValue());
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSub#isEmpty()}. Überprüft, ob die Datenstruktur innerhalb der Intervallgrenzen
	 * leer ist.
	 *
	 * @param iv Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 *
	 * @return TRUE, falls die Datenstruktur innerhalb der Intervallgrenzen leer ist.
	 */
	boolean bcIsEmpty(@NotNull final DummyMapIntervall iv) {
		for (int i = iv.min(); i <= iv.max(); i++)
			if (_isMapped[i] != null)
				return false;
		return true;
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSub#clear()}. Entfernt alle Elemente innerhalb des übergebenen Intervalls.
	 *
	 * @param iv Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 */
	void bcClear(@NotNull final DummyMapIntervall iv) {
		for (int i = iv.min(); i <= iv.max(); i++)
			_isMapped[i] = null;
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSub#get(Object)}. Liefert den Wert (Value) eines bestimmten Schlüssels (Key)
	 * falls vorhanden, sonst NULL.
	 *
	 * @param objKey Der Schlüssel (Key) dessen Wert (Value) angefordert wird.
	 * @param iv     Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 *
	 * @return Den Wert (Value) eines bestimmten Schlüssels (Key) falls vorhanden, sonst NULL.
	 */
	Integer bcGetValueOfKeyOrNull(@NotNull final DummyMapIntervall iv, @NotNull final Object objKey) {
		final int e = (Integer) objKey;
		if (!iv.contains(e))
			return null;
		// throw new IllegalArgumentException("Der Schlüsselwert liegt nicht im gültigen Bereich.");
		return _isMapped[e];
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSub#size()}. Liefert die Anzahl der Elemente innerhalb des übergebenen
	 * Intervalls.
	 *
	 * @param iv Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 *
	 * @return Liefert die Anzahl der Elemente innerhalb des übergebenen Intervalls.
	 */
	int bcGetSize(@NotNull final DummyMapIntervall iv) {
		int size = 0;
		for (int i = iv.min(); i <= iv.max(); i++)
			if (_isMapped[i] != null)
				size++;
		return size;
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSub#lowerEntry(Integer)} bzw. {@link DummyMapSub#higherEntry(Integer)} falls
	 * absteigend sortiert. Liefert das größte Entry welches kleiner ist als der übergebene Schlüssel (Key), somit den
	 * Vorgänger-Entry des Schlüssels (Key).
	 *
	 * @param key Der Schlüssel (Key) dessen Vorgänger-Entry gesucht wird.
	 * @param iv  Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 *
	 * @return Liefert den Vorgänger-Entry des Schlüssels (Key) falls vorhanden, sonst NULL.
	 */
	Entry<@NotNull Integer, @NotNull Integer> bcGetLowerEntryOrNull(@NotNull final DummyMapIntervall iv,
			@NotNull final Integer key) {
		for (int i = Math.min(key - 1, iv.max()); i >= iv.min(); i--)
			if (_isMapped[i] != null)
				return new DummyMapEntry(i, _isMapped[i]);
		return null;
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSub#lowerKey(Integer)} bzw. {@link DummyMapSub#higherKey(Integer)} falls
	 * absteigend sortiert. Liefert den größten Schlüssel (Key) welcher kleiner ist als der übergebene Schlüssel (Key),
	 * somit den Vorgänger-Schlüssel des Schlüssels (Key).
	 *
	 * @param key Der Schlüssel (Key) dessen Vorgänger gesucht wird.
	 * @param iv  Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 *
	 * @return Den Vorgänger-Schlüssel des übergebenen Schlüssels (Key) falls vorhanden, sonst NULL.
	 */
	Integer bcGetLowerKeyOrNull(@NotNull final DummyMapIntervall iv, @NotNull final Integer key) {
		for (int i = Math.min(key - 1, iv.max()); i >= iv.min(); i--)
			if (_isMapped[i] != null)
				return i;
		return null;
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSub#floorEntry(Integer)} bzw. {@link DummyMapSub#ceilingEntry(Integer)} falls
	 * absteigend sortiert. Liefert das größte Entry welches kleiner oder gleich dem übergebenen Schlüssel (Key) ist.
	 * Somit das zugehörige Entry des Schlüssels (Key) falls vorhanden, andernfalls den Vorgänger-Entry falls vorhanden,
	 * andernfalls NULL.
	 *
	 * @param key Der Schlüssel (Key) dessen Entry bzw. Vorgänger-Entry gesucht wird.
	 * @param iv  Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 *
	 * @return Das zugehörige Entry des Schlüssels (Key) falls vorhanden, andernfalls den Vorgänger-Entry falls
	 *         vorhanden, andernfalls NULL.
	 */
	Entry<@NotNull Integer, @NotNull Integer> bcGetFloorEntryOrNull(@NotNull final DummyMapIntervall iv,
			@NotNull final Integer key) {
		for (int i = Math.min(key, iv.max()); i >= iv.min(); i--)
			if (_isMapped[i] != null)
				return new DummyMapEntry(i, _isMapped[i]);
		return null;
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSub#floorKey(Integer)} bzw. {@link DummyMapSub#ceilingKey(Integer)} falls
	 * absteigend sortiert. Liefert den größten Schlüssel (Key) welcher kleiner oder gleich dem übergebenen Schlüssel
	 * (Key) ist. Somit der selbe Schlüssel (Key) falls vorhanden, andernfalls den Vorgänger-Schlüssel (Key) falls
	 * vorhanden, andernfalls NULL.
	 *
	 * @param key Der Schlüssel (Key) der gesucht wird bzw. sein Vorgänger-Schlüssel.
	 * @param iv  Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 *
	 * @return Den selben Schlüssel (Key) falls vorhanden, andernfalls sein Vorgänger-Schlüssel falls vorhanden,
	 *         andernfalls NULL.
	 */
	Integer bcGetFloorKeyOrNull(@NotNull final DummyMapIntervall iv, @NotNull final Integer key) {
		for (int i = Math.min(key, iv.max()); i >= iv.min(); i--)
			if (_isMapped[i] != null)
				return i;
		return null;
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSub#ceilingEntry(Integer)} bzw. {@link DummyMapSub#floorEntry(Integer)} falls
	 * absteigend sortiert. Liefert das kleinste Entry welches größer oder gleich dem übergebenen Schlüssel (Key) ist.
	 * Somit das zugehörige Entry des Schlüssels (Key) falls vorhanden, andernfalls den Nachfolger-Entry falls
	 * vorhanden, andernfalls NULL.
	 *
	 * @param key Der Schlüssel (Key) dessen Entry bzw. Nachfolger-Entry gesucht wird.
	 * @param iv  Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 *
	 * @return Das zugehörige Entry des Schlüssels (Key) falls vorhanden, andernfalls den Nachfolger-Entry falls
	 *         vorhanden, andernfalls NULL.
	 */
	Entry<@NotNull Integer, @NotNull Integer> bcGetCeilingEntryOrNull(@NotNull final DummyMapIntervall iv,
			@NotNull final Integer key) {
		for (int i = Math.max(key, iv.min()); i <= iv.max(); i++)
			if (_isMapped[i] != null)
				return new DummyMapEntry(i, _isMapped[i]);
		return null;
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSub#ceilingKey(Integer)} bzw. {@link DummyMapSub#floorKey(Integer)} falls
	 * absteigend sortiert. Liefert den kleinsten Schlüssel (Key) welcher größer oder gleich dem übergebenen Schlüssel
	 * (Key) ist. Somit der selbe Schlüssel (Key) falls vorhanden, andernfalls sein Nachfolger-Schlüssel (Key) falls
	 * vorhanden, andernfalls NULL.
	 *
	 * @param key Der Schlüssel (Key) der gesucht wird bzw. sein Nachfolger-Schlüssel.
	 * @param iv  Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 *
	 * @return Den selben Schlüssel (Key) falls vorhanden, andernfalls sein Nachfolger-Schlüssel falls vorhanden,
	 *         andernfalls NULL.
	 */
	Integer bcGetCeilingKeyOrNull(@NotNull final DummyMapIntervall iv, @NotNull final Integer key) {
		for (int i = Math.max(key, iv.min()); i <= iv.max(); i++)
			if (_isMapped[i] != null)
				return i;
		return null;
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSub#higherEntry(Integer)} bzw. {@link DummyMapSub#lowerEntry(Integer)} falls
	 * absteigend sortiert. Liefert das kleinste Entry welches größer ist als der übergebene Schlüssel (Key), somit den
	 * Nachfolger-Entry des Schlüssels (Key).
	 *
	 * @param key Der Schlüssel (Key) dessen Nachfolger-Entry gesucht wird.
	 * @param iv  Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 *
	 * @return Liefert den Nachfolger-Entry des Schlüssels (Key) falls vorhanden, sonst NULL.
	 */
	Entry<@NotNull Integer, @NotNull Integer> bcGetHigherEntryOrNull(@NotNull final DummyMapIntervall iv,
			@NotNull final Integer key) {
		for (int i = Math.max(key + 1, iv.min()); i <= iv.max(); i++)
			if (_isMapped[i] != null)
				return new DummyMapEntry(i, _isMapped[i]);
		return null;
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSub#higherKey(Integer)} bzw. {@link DummyMapSub#lowerKey(Integer)} falls
	 * absteigend sortiert. Liefert den kleinsten Schlüssel (Key) welcher größer ist als der übergebene Schlüssel (Key),
	 * somit den Nachfolger-Schlüssel des übergebenen Schlüssels (Key).
	 *
	 * @param key Der Schlüssel (Key) dessen Nachfolger-Schlüssel gesucht wird.
	 * @param iv  Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 *
	 * @return Den Nachfolger-Schlüssel des übergebenen Schlüssels (Key) falls vorhanden, sonst NULL.
	 */
	Integer bcGetHigherKeyOrNull(@NotNull final DummyMapIntervall iv, @NotNull final Integer key) {
		for (int i = Math.max(key + 1, iv.min()); i <= iv.max(); i++)
			if (_isMapped[i] != null)
				return i;
		return null;
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSub#firstEntry()} bzw. {@link DummyMapSub#lastEntry()} falls absteigend
	 * sortiert. Liefert das erste Entry dieser Datenstruktur.
	 *
	 * @param iv Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 *
	 * @return Das erste Entry dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	Entry<@NotNull Integer, @NotNull Integer> bcGetFirstEntryOrNull(@NotNull final DummyMapIntervall iv) {
		for (int i = iv.min(); i <= iv.max(); i++)
			if (_isMapped[i] != null)
				return new DummyMapEntry(i, _isMapped[i]);
		return null;
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSub#firstKey()} bzw. {@link DummyMapSub#lastKey()} falls absteigend sortiert.
	 * Liefert den ersten Schlüssel (Key) dieser Datenstruktur.
	 *
	 * @param iv Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 *
	 * @return Liefert den ersten Schlüssel (Key) dieser Datenstruktur, falls vorhanden.
	 * @throws NoSuchElementException falls es kein erstes Element gibt.
	 */
	@NotNull Integer bcGetFirstKeyOrException(@NotNull final DummyMapIntervall iv) {
		for (int i = iv.min(); i <= iv.max(); i++)
			if (_isMapped[i] != null)
				return i;
		throw new NoSuchElementException();
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSub#lastEntry()} bzw. {@link DummyMapSub#firstEntry()} falls absteigend
	 * sortiert. Liefert das letzte Entry dieser Datenstruktur.
	 *
	 * @param iv Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 *
	 * @return Liefert das letzte Entry dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	Entry<@NotNull Integer, @NotNull Integer> bcGetLastEntryOrNull(@NotNull final DummyMapIntervall iv) {
		for (int i = iv.max(); i >= iv.min(); i--)
			if (_isMapped[i] != null)
				return new DummyMapEntry(i, _isMapped[i]);
		return null;
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSub#lastKey()} bzw. {@link DummyMapSub#firstKey()} falls absteigend sortiert.
	 * Liefert den letzten Schlüssel (Key) dieser Datenstruktur.
	 *
	 * @param iv Das {@link DummyMapIntervall} dieser {@link DummyMap}.
	 *
	 * @return Liefert den letzten Schlüssel (Key) dieser Datenstruktur falls vorhanden.
	 * @throws NoSuchElementException falls es kein letztes Element gibt.
	 */
	@NotNull Integer bcGetLastKeyOrException(@NotNull final DummyMapIntervall iv) {
		for (int i = iv.max(); i >= iv.min(); i--)
			if (_isMapped[i] != null)
				return i;
		throw new NoSuchElementException();
	}

	// ########################################################################
	// ############################# PRIVATE ##################################
	// ########################################################################

	private boolean removeKeyReturnBool(@NotNull final DummyMapIntervall iv, @NotNull final Object objKey) {
		return bcRemoveKeyReturnOldValue(iv, objKey) != null;
	}

	private static boolean valEqual(final Object a, final Object b) {
		return (a == null) ? (b == null) : a.equals(b);
	}

}
