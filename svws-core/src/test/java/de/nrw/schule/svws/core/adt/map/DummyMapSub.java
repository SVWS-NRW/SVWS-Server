package de.nrw.schule.svws.core.adt.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import de.nrw.schule.svws.core.adt.set.DummySet;
import de.nrw.schule.svws.core.adt.set.DummySetIntervall;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse definiert mit Hilfe von {@link DummyMapIntervall} eine Sub-Sicht auf das {@link DummyMap}. Der Parameter
 * {@link DummyMapSub#_asc} entscheidet darüber, ob die Sub-Sicht die Elemente auf- oder absteigend interpretiert.
 *
 * @author Benjamin A. Bartsch
 */
public class DummyMapSub implements NavigableMap<Integer, Integer> {

	/**
	 * Die Map auf die sich diese Sub-Map bezieht.
	 */
	private final @NotNull DummyMap _par;

	/**
	 * Das gültige Intervall dieser Sub-Map.
	 */
	private final @NotNull DummyMapIntervall _iv;

	/**
	 * Entscheidet darüber, ob die Sub-Sicht die Elemente auf- oder absteigend interpretiert.
	 */
	private boolean _asc;

	/**
	 * Definiert mit Hilfe von {@link DummySetIntervall} eine Sicht auf das {@link DummySet}.
	 * 
	 * @param par Das Set auf das sich diese Sub-Sicht bezieht.
	 * @param iv  Das Intervall dieser Sub-Map.
	 * @param asc Falls {@code false} werden alle Elemente absteigend interpretiert.
	 */
	public DummyMapSub(@NotNull DummyMap par, @NotNull DummyMapIntervall iv, boolean asc) {
		_par = par;
		_iv = iv;
		_asc = asc;
	}

	@Override
	public String toString() {
		String s = "";
		Iterator<Entry<Integer, Integer>> iter = entrySet().iterator();
		while (iter.hasNext())
			s += (s.isEmpty() ? "" : ", ") + iter.next();
		// for (Entry<Integer, Integer> e : entrySet())
		// s += (s.isEmpty() ? "" : ", ") + e;
		return "Entries = [" + s + "], iv = " + _iv + ", asc = " + _asc;
	}

	@Override
	public int hashCode() { // code adapted TreeMap
		int h = 0;
		for (Entry<Integer, Integer> entry : entrySet())
			h += entry.hashCode();
		return h;
	}

	@Override
	public boolean equals(@NotNull Object o) {
		if (o == this)
			return true;

		if (o instanceof Map<?, ?> == false)
			return false;

		Map<?, ?> mapO = (Map<?, ?>) o;

		if (mapO.size() != size())
			return false;

		// Da SIZE identisch ist, reicht es die KEYS in dieser Map
		// mit dem Mapping in mapO zu überprüfen.
		for (@NotNull
		Entry<@NotNull Integer, @NotNull Integer> e : entrySet())
			if (e.getValue().equals(mapO.get(e.getKey())) == false)
				return false;

		return true;
	}

	@Override
	public @NotNull Comparator<? super Integer> comparator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public @NotNull Integer firstKey() {
		return _asc ? _par.bcGetFirstKeyOrException(_iv) : _par.bcGetLastKeyOrException(_iv);
	}

	@Override
	public @NotNull Integer lastKey() {
		return _asc ? _par.bcGetLastKeyOrException(_iv) : _par.bcGetFirstKeyOrException(_iv);
	}

	@Override
	public @NotNull Set<@NotNull Integer> keySet() {
		return new DummyMapSubKeySet(this);
	}

	@Override
	public @NotNull Collection<@NotNull Integer> values() {
		return new DummyMapSubCollection(this);
	}

	@Override
	public @NotNull Set<@NotNull Entry<@NotNull Integer, @NotNull Integer>> entrySet() {
		return new DummyMapSubEntrySet(this);
	}

	@Override
	public int size() {
		return _par.bcGetSize(_iv);
	}

	@Override
	public boolean isEmpty() {
		return _par.bcIsEmpty(_iv);
	}

	@Override
	public boolean containsKey(@NotNull Object key) {
		return _par.bcContainsKey(_iv, key);
	}

	@Override
	public boolean containsValue(@NotNull Object value) {
		return _par.bcContainsValue(_iv, value);
	}

	@Override
	public Integer get(@NotNull Object key) {
		return _par.bcGetValueOfKeyOrNull(_iv, key);
	}

	@Override
	public Integer put(@NotNull Integer key, @NotNull Integer value) {
		return _par.bcAddEntryReturnOldValueOrNull(_iv, key, value);
	}

	@Override
	public Integer remove(@NotNull Object objKey) {
		return _par.bcRemoveKeyReturnOldValue(_iv, objKey);
	}

	@Override
	public void putAll(@NotNull Map<? extends @NotNull Integer, ? extends @NotNull Integer> m) {
		_par.bcAddAllEntriesOfMap(_iv, m);
	}

	@Override
	public void clear() {
		_par.bcClear(_iv);
	}

	@Override
	public Entry<@NotNull Integer, @NotNull Integer> lowerEntry(@NotNull Integer key) {
		return _asc ? _par.bcGetLowerEntryOrNull(_iv, key) : _par.bcGetHigherEntryOrNull(_iv, key);
	}

	@Override
	public Integer lowerKey(@NotNull Integer key) {
		return _asc ? _par.bcGetLowerKeyOrNull(_iv, key) : _par.bcGetHigherKeyOrNull(_iv, key);
	}

	@Override
	public Entry<@NotNull Integer, @NotNull Integer> floorEntry(@NotNull Integer key) {
		return _asc ? _par.bcGetFloorEntryOrNull(_iv, key) : _par.bcGetCeilingEntryOrNull(_iv, key);
	}

	@Override
	public Integer floorKey(@NotNull Integer key) {
		return _asc ? _par.bcGetFloorKeyOrNull(_iv, key) : _par.bcGetCeilingKeyOrNull(_iv, key);
	}

	@Override
	public Entry<@NotNull Integer, @NotNull Integer> ceilingEntry(@NotNull Integer key) {
		return _asc ? _par.bcGetCeilingEntryOrNull(_iv, key) : _par.bcGetFloorEntryOrNull(_iv, key);
	}

	@Override
	public Integer ceilingKey(@NotNull Integer key) {
		return _asc ? _par.bcGetCeilingKeyOrNull(_iv, key) : _par.bcGetFloorKeyOrNull(_iv, key);
	}

	@Override
	public Entry<@NotNull Integer, @NotNull Integer> higherEntry(@NotNull Integer key) {
		return _asc ? _par.bcGetHigherEntryOrNull(_iv, key) : _par.bcGetLowerEntryOrNull(_iv, key);
	}

	@Override
	public Integer higherKey(@NotNull Integer key) {
		return _asc ? _par.bcGetHigherKeyOrNull(_iv, key) : _par.bcGetLowerKeyOrNull(_iv, key);
	}

	@Override
	public Entry<@NotNull Integer, @NotNull Integer> firstEntry() {
		return _asc ? _par.bcGetFirstEntryOrNull(_iv) : _par.bcGetLastEntryOrNull(_iv);
	}

	@Override
	public Entry<@NotNull Integer, @NotNull Integer> lastEntry() {
		return _asc ? _par.bcGetLastEntryOrNull(_iv) : _par.bcGetFirstEntryOrNull(_iv);
	}

	@Override
	public Entry<@NotNull Integer, @NotNull Integer> pollFirstEntry() {
		return _asc ? _par.bcPollFirstEntryOrNull(_iv) : _par.bcPollLastEntryOrNull(_iv);
	}

	@Override
	public Entry<@NotNull Integer, @NotNull Integer> pollLastEntry() {
		return _asc ? _par.bcPollLastEntryOrNull(_iv) : _par.bcPollFirstEntryOrNull(_iv);
	}

	@Override
	public @NotNull NavigableMap<@NotNull Integer, @NotNull Integer> descendingMap() {
		return new DummyMapSub(_par, _iv, !_asc);
	}

	@Override
	public @NotNull NavigableSet<@NotNull Integer> navigableKeySet() {
		return new DummyMapSubKeySet(this);
	}

	@Override
	public @NotNull NavigableSet<@NotNull Integer> descendingKeySet() {
		return new DummyMapSubKeySet(new DummyMapSub(_par, _iv, !_asc));
	}

	@Override
	public @NotNull NavigableMap<@NotNull Integer, @NotNull Integer> subMap(@NotNull Integer fromKey,
			boolean fromInclusive, @NotNull Integer toKey, boolean toInclusive) {
		return _createMap(fromKey, fromInclusive, toKey, toInclusive, _asc);
	}

	@Override
	public @NotNull NavigableMap<@NotNull Integer, @NotNull Integer> headMap(@NotNull Integer toKey, boolean inc) {
		return _createMap(_iv.from, _iv.fromInc, toKey, inc, _asc);
	}

	@Override
	public @NotNull NavigableMap<@NotNull Integer, @NotNull Integer> tailMap(@NotNull Integer fromKey, boolean inc) {
		return _createMap(fromKey, inc, _iv.to, _iv.toInc, _asc);
	}

	@Override
	public @NotNull SortedMap<@NotNull Integer, @NotNull Integer> subMap(@NotNull Integer fromKey,
			@NotNull Integer toKey) {
		return _createMap(fromKey, true, toKey, false, _asc);
	}

	@Override
	public @NotNull SortedMap<@NotNull Integer, @NotNull Integer> headMap(@NotNull Integer toKey) {
		return _createMap(_iv.from, _iv.fromInc, toKey, false, _asc);
	}

	@Override
	public @NotNull SortedMap<@NotNull Integer, @NotNull Integer> tailMap(@NotNull Integer fromKey) {
		return _createMap(fromKey, true, _iv.to, _iv.toInc, _asc);
	}

	// ########################################################################
	// ########################## PROTECTED ###################################
	// ########################################################################

	/**
	 * Wird aufgerufen von {@link DummyMapSubKeySet#add(Integer)}. Fügt einen Schlüssel (Key) dieser Datenstruktur
	 * hinzu.
	 * 
	 * @param e Der einzufügende Schlüssel (Key).
	 * 
	 * @return TRUE, falls der Schlüssel (Key) noch nicht existierte, sonst FALSE.
	 */
	boolean bcAddKey(@NotNull Integer e) {
		return _par.bcAddKey(_iv, e);
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSubKeySet#addAll(Collection)}. Fügt alle Schlüssel (Keys) der Collection
	 * dieser Datenstruktur hinzu.
	 * 
	 * @param c Die Collection mit den einzufügenden Schlüsseln (Keys).
	 * 
	 * @return TRUE, falls mindestens ein Schlüssel (Key) noch nicht existierte und somit hinzugefügt wurde.
	 */
	boolean bcAddAllKeys(Collection<? extends @NotNull Integer> c) {
		return _par.bcAddAllKeys(_iv, c);
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSubEntrySet#add(java.util.Map.Entry)}. Fügt ein Entry der Datenstruktur hinzu.
	 * 
	 * @param e Das einzufügende Entry.
	 * 
	 * @return TRUE, falls das Entry (e.getKey(), e.getValue()) neu war und somit hinzugefügt wurde.
	 */
	boolean bcAddEntryReturnBool(@NotNull Entry<@NotNull Integer, @NotNull Integer> e) {
		return _par.bcAddEntry(_iv, e);
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSubEntrySet#addAll(Collection)}. Fügt alle Entries der Collection dieser
	 * Datenstruktur hinzu.
	 * 
	 * @param c Die Collection mit den einzufügenden Entries.
	 * 
	 * @return TRUE, falls mindestens ein Entry neu war und somit hinzugefügt wurde.
	 */
	boolean bcAddAllEntries(@NotNull Collection<? extends @NotNull Entry<@NotNull Integer, @NotNull Integer>> c) {
		return _par.bcAddAllEntries(_iv, c);
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSubKeySet#containsAll(Collection)}. Überprüft, ob alle Schlüssel (Keys) der
	 * Collection in dieser Datenstruktur existieren.
	 * 
	 * @param c Die Collection mit allen Schlüsseln (Keys) welche überprüft werden sollen.
	 * 
	 * @return TRUE, falls alle Schlüssel (Keys) der Collection in dieser Datenstruktur existieren.
	 */
	boolean bcContainsAllKeys(Collection<?> c) {
		return _par.bcContainsAllKeys(_iv, c);
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSubEntrySet#contains(Object)}. Überprüft, ob das übergebene Entry in dieser
	 * Datenstruktur existiert.
	 * 
	 * @param o Das Entry (Schlüssel-Wert-Paar) nach dem gesucht wird.
	 * 
	 * @return TRUE, falls das übergebene Entry bereits in dieser Datenstruktur existiert.
	 */
	boolean bcContainsEntry(Object o) {
		return _par.bcContainsEntry(_iv, o);
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSubEntrySet#containsAll(Collection)}. Überprüft, ob alle Entries der
	 * Collection in dieser Datenstruktur existieren.
	 * 
	 * @param c Die Collection mit den Entries welche überprüft werden sollen.
	 * 
	 * @return TRUE, falls alle Entries in dieser Datenstruktur existieren.
	 */
	boolean bcContainsAllEntries(@NotNull Collection<?> c) {
		return _par.bcContainsAllEntries(_iv, c);
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSubCollection#containsAll(Collection)}. Überprüft, ob alle Werte (Values) aus
	 * der Collection in dieser Datenstruktur vorkommen.
	 * 
	 * @param c Die Collection deren Werte (Values) überprüft werden sollen.
	 * 
	 * @return TRUE, falls alle Werte (Values) der Collection in dieser Datenstruktur existieren.
	 * 
	 */
	boolean bcContainsAllValues(@NotNull Collection<?> c) {
		return _par.bcContainsAllValues(_iv, c);
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSubKeySet#removeAll(Collection)}. Entfernt alle Schlüssel (Keys) aus dieser
	 * Datenstruktur.
	 * 
	 * @param c Die Collection mit allen Schlüsseln (Keys) die entfernt werden sollen.
	 * 
	 * @return TRUE, falls mindestens ein Schlüssel (Key) entfernt wurde.
	 */
	boolean bcRemoveAllKeysReturnBool(@NotNull Collection<?> c) {
		return _par.bcRemoveAllKeyReturnBool(_iv, c);
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSubEntrySet#remove(Object)}. Entfernt das Entry aus dieser Datenstruktur.
	 * 
	 * @param o Das Entry, welches entfernt werden soll.
	 * 
	 * @return TRUE, falls das Entry in der Datenstruktur existierte und somit entfernt wurde.
	 */
	boolean bcRemoveEntry(@NotNull Object o) {
		return _par.bcRemoveEntryReturnBool(_iv, o);
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSubEntrySet#removeAll(Collection)}. Entfernt alle Entries der Collection aus
	 * dieser Datenstruktur.
	 * 
	 * @param c Die Collection mit den Entries, welche entfernt werden sollen.
	 * 
	 * @return TRUE, falls mindestens ein Entry entfernt wurde.
	 */
	boolean bcRemoveAllEntriesReturnBool(@NotNull Collection<?> c) {
		return _par.bcRemoveAllEntriesReturnBool(_iv, c);
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSubKeySet#pollFirst()}. Entfernt und liefert den ersten Schlüssel (Key) dieser
	 * Datenstruktur. Dabei wird beachtet, ob diese Sub-Map aufsteigend oder absteigend zu interpretieren ist.
	 * 
	 * @return Entfernt und liefert den ersten Schlüssel (Key) dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	Integer bcPollFirstKeyOrNull() {
		return _asc ? _par.bcPollFirstKeyOrNull(_iv) : _par.bcPollLastKeyOrNull(_iv);
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSubKeySet#pollLast()}. Entfernt und liefert den letzten Schlüssel (Key) dieser
	 * Datenstruktur. Dabei wird beachtet, ob diese Sub-Map aufsteigend oder absteigend zu interpretieren ist.
	 * 
	 * @return Entfernt und liefert den letzten Schlüssel (Key) dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	Integer bcPollLastKeyOrNull() {
		return _asc ? _par.bcPollLastKeyOrNull(_iv) : _par.bcPollFirstKeyOrNull(_iv);
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSubKeySet#retainAll(Collection)}. Entfernt alle Schlüssel (Keys) aus dieser
	 * Datenstruktur, außer sie sind in der Collection enthalten.
	 * 
	 * @param c Die Collection deren Schlüssel (Keys) nicht entfernt werden dürfen.
	 * 
	 * @return TRUE, falls mindestens ein Schlüssel (Key) entfernt wurde.
	 */
	boolean bcRetainAllKeys(Collection<?> c) {
		boolean changed = false;
		Iterator<Integer> iterKey = keySet().iterator(); // Es muss der Iterator von "this" sein!
		while (iterKey.hasNext()) {
			Integer key = iterKey.next();
			if (c.contains(key) == false) {
				iterKey.remove();
				changed = true;
			}
		}
		return changed;
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSubEntrySet#retainAll(Collection)}. Entfernt alle Entries aus dieser
	 * Datenstruktur, außer sie sind in der Collection enthalten.
	 * 
	 * @param c Die Collection deren Entries nicht entfernt werden dürfen.
	 * 
	 * @return TRUE, falls mindestens ein Entry entfernt wurde.
	 */
	boolean bcRetainAllEntries(@NotNull Collection<?> c) {
		boolean changed = false;
		Iterator<Entry<Integer, Integer>> iterKey = entrySet().iterator(); // Es muss der Iterator von "this" sein!
		while (iterKey.hasNext()) {
			Entry<Integer, Integer> ent = iterKey.next();
			if (c.contains(ent) == false) {
				iterKey.remove();
				changed = true;
			}
		}
		return changed;
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSubKeySet#toArray()} und {@link DummyMapSubKeySet#toArray(Object[])}. Liefert
	 * eine {@link ArrayList} die alle Schlüssel (Keys) dieser Sub-Map beinhaltet.
	 * 
	 * @return Eine {@link ArrayList} die alle Schlüssel (Keys) dieser Sub-Map beinhaltet.
	 */
	@NotNull
	ArrayList<@NotNull Integer> bcGetArrayListOfKeys() {
		ArrayList<Integer> list = new ArrayList<>();
		@NotNull
		Iterator<@NotNull Integer> iter = navigableKeySet().iterator();
		while (iter.hasNext())
			list.add(iter.next());
		return list;
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSubCollection#toArray()} und {@link DummyMapSubCollection#toArray(Object[])}.
	 * Liefert eine {@link ArrayList} die alle Werte (Values) dieser Sub-Map beinhaltet.
	 * 
	 * @return Eine {@link ArrayList} die alle Werte (Values) dieser Sub-Map beinhaltet.
	 */
	@NotNull
	ArrayList<@NotNull Integer> bcGetArrayListOfValues() {
		@NotNull
		ArrayList<@NotNull Integer> list = new ArrayList<>();
		@NotNull
		Iterator<@NotNull Integer> iter = values().iterator();
		while (iter.hasNext())
			list.add(iter.next());
		return list;
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSubEntrySet#toArray()} und {@link DummyMapSubEntrySet#toArray(Object[])}.
	 * Liefert eine {@link ArrayList} die alle Entries dieser Sub-Map beinhaltet.
	 * 
	 * @return Eine {@link ArrayList} die alle Entries dieser Sub-Map beinhaltet.
	 */
	@NotNull
	ArrayList<@NotNull Entry<@NotNull Integer, @NotNull Integer>> bcGetArrayListOfEntries() {
		@NotNull
		ArrayList<@NotNull Entry<@NotNull Integer, @NotNull Integer>> list = new ArrayList<>();
		@NotNull
		Iterator<@NotNull Entry<@NotNull Integer, @NotNull Integer>> iter = entrySet().iterator();
		while (iter.hasNext())
			list.add(iter.next());
		// for (Entry<Integer, Integer> e : entrySet())
		// list.add(e);
		return list;
	}

	/**
	 * Wird aufgerufen von {@link DummyMapSubKeySet#iterator()}. Liefert einen {@link Iterator} von Schlüsseln (Keys)
	 * relativ zu dieser Sub-Map.
	 * 
	 * @return Einen {@link Iterator} von Schlüsseln (Keys) relativ zu dieser Sub-Map.
	 */
	@NotNull
	Iterator<@NotNull Integer> bcGetSubKeySetIterator() {
		return new DummyMapSubKeySetIterator(this);
	}

	/**
	 * Wird aufgerufen und von {@link DummyMapSubKeySet#descendingIterator()}. Liefert einen {@link Iterator} von
	 * Schlüsseln (Keys) relativ zu dieser <strong>absteigenden</strong> Sub-Map.
	 * 
	 * @return Ein {@link Iterator} von Schlüsseln (Keys) relativ zu dieser <strong>absteigenden</strong> Sub-Map.
	 */
	@NotNull
	Iterator<@NotNull Integer> bcGetSubKeySetDescendingIterator() {
		return new DummyMapSubKeySetIterator(new DummyMapSub(_par, _iv, !_asc));
	}

	/**
	 * Wird aufgerufen und von {@link DummyMapSubKeySet#descendingSet()}. Liefert ein {@link NavigableSet} von
	 * Schlüsseln (Keys) relativ zu dieser <strong>absteigenden</strong> Sub-Map.
	 * 
	 * @return Ein {@link NavigableSet} von Schlüsseln (Keys) relativ zu dieser <strong>absteigenden</strong> Sub-Map.
	 */
	@NotNull
	NavigableSet<@NotNull Integer> bcGetSubKeySetDescending() {
		return new DummyMapSubKeySet(new DummyMapSub(_par, _iv, !_asc));
	}

	/**
	 * Wird aufgerufen und von {@link DummyMapSubKeySet#subSet(Integer, boolean, Integer, boolean)}. Liefert ein
	 * {@link NavigableSet} von Schlüsseln (Keys) relativ zu dieser Sub-Map.
	 * 
	 * @param fromElement   Die linke (von) Intervallsgrenze.
	 * @param fromInclusive Gibt an, ob die linke (von) Intervallsgrenze inklusive ist.
	 * @param toElement     Die rechte (bis) Intervallsgrenze.
	 * @param toInclusive   Gibt an, ob die rechte (bis) Intervallsgrenze inklusive ist.
	 * 
	 * @return Ein {@link NavigableSet} von Schlüsseln (Keys) relativ zu dieser Sub-Map.
	 */
	@NotNull
	NavigableSet<@NotNull Integer> bcGetSubKeySet(@NotNull Integer fromElement, boolean fromInclusive,
			@NotNull Integer toElement, boolean toInclusive) {
		return _createSet(fromElement, fromInclusive, toElement, toInclusive, _asc);
	}

	/**
	 * Wird aufgerufen und von {@link DummyMapSubKeySet#headSet(Integer, boolean)}. Liefert ein {@link NavigableSet} von
	 * Schlüsseln (Keys) relativ zu dieser Sub-Map.
	 * 
	 * @param toElement Die rechte (bis) Intervallsgrenze.
	 * @param inclusive Gibt an, ob die rechte (bis) Intervallsgrenze inklusive ist.
	 * 
	 * @return Ein {@link NavigableSet} von Schlüsseln (Keys) relativ zu dieser Sub-Map.
	 */
	@NotNull
	NavigableSet<@NotNull Integer> bcGetSubKeyHeadSet(@NotNull Integer toElement, boolean inclusive) {
		return _createSet(_iv.from, _iv.fromInc, toElement, inclusive, _asc);
	}

	/**
	 * Wird aufgerufen und von {@link DummyMapSubKeySet#tailSet(Integer, boolean)}. Liefert ein {@link NavigableSet} von
	 * Schlüsseln (Keys) relativ zu dieser Sub-Map.
	 * 
	 * @param fromElement Die linke (von) Intervallsgrenze.
	 * @param inclusive   Gibt an, ob die linke (von) Intervallsgrenze inklusive ist.
	 * 
	 * @return Ein {@link NavigableSet} von Schlüsseln (Keys) relativ zu dieser Sub-Map.
	 */
	@NotNull
	NavigableSet<@NotNull Integer> bcGetSubKeyTailSet(@NotNull Integer fromElement, boolean inclusive) {
		return _createSet(fromElement, inclusive, _iv.to, _iv.toInc, _asc);
	}

	/**
	 * Wird aufgerufen und von {@link DummyMapSubKeySet#subSet(Integer, Integer)}. Liefert ein {@link NavigableSet} von
	 * Schlüsseln (Keys) relativ zu dieser Sub-Map.
	 * 
	 * Äquivalent zu {@link #bcGetSubKeySet(Integer, boolean, Integer, boolean)} mit den Werten (fromElement, true,
	 * toElement, false).
	 * 
	 * @param fromElement Die linke (von) Intervallsgrenze <strong>inklusive</strong>.
	 * @param toElement   Die rechte (bis) Intervallsgrenze <strong>exklusive</strong>.
	 * 
	 * @return Ein {@link NavigableSet} von Schlüsseln (Keys) relativ zu dieser Sub-Map.
	 */
	@NotNull
	SortedSet<@NotNull Integer> bcGetSubKeySet(@NotNull Integer fromElement, @NotNull Integer toElement) {
		return _createSet(fromElement, true, toElement, false, _asc);
	}

	/**
	 * Wird aufgerufen und von {@link DummyMapSubKeySet#headSet(Integer)}. Liefert ein {@link NavigableSet} von
	 * Schlüsseln (Keys) relativ zu dieser Sub-Map.
	 * 
	 * Äquivalent zu {@link #bcGetSubKeyHeadSet(Integer, boolean)} mit den Werten (toElement, false).
	 * 
	 * @param toElement Die rechte (bis) Intervallsgrenze <strong>exklusive</strong>.
	 * 
	 * @return Ein {@link NavigableSet} von Schlüsseln (Keys) relativ zu dieser Sub-Map.
	 */
	@NotNull
	SortedSet<@NotNull Integer> bcGetSubKeyHeadSet(@NotNull Integer toElement) {
		return _createSet(_iv.from, _iv.fromInc, toElement, false, _asc);
	}

	/**
	 * Wird aufgerufen und von {@link DummyMapSubKeySet#tailSet(Integer)}. Liefert ein {@link NavigableSet} von
	 * Schlüsseln (Keys) relativ zu dieser Sub-Map.
	 * 
	 * Äquivalent zu {@link #bcGetSubKeyTailSet(Integer, boolean)} mit den Werten (fromElement, true).
	 * 
	 * @param fromElement Die linke (von) Intervallsgrenze <strong>inklusive</strong>.
	 * 
	 * @return Ein {@link NavigableSet} von Schlüsseln (Keys) relativ zu dieser Sub-Map.
	 */
	@NotNull
	SortedSet<@NotNull Integer> bcGetSubKeyTailSet(@NotNull Integer fromElement) {
		return _createSet(fromElement, true, _iv.to, _iv.toInc, _asc);
	}

	// ########################################################################
	// ########################## PRIVATE #####################################
	// ########################################################################

	private @NotNull DummyMapSub _createMap(@NotNull Integer from, boolean fromInc, @NotNull Integer to, boolean toInc,
			boolean asc) {

		// System.out.println("IV = " + _iv);
		// System.out.println("--> " + from + ", " + fromInc + ", " + to + ", " + toInc);

		if (_iv.isOutOfRange(from, fromInc))
			throw new IllegalArgumentException("FROM-KEY " + from + "/" + fromInc + " nicht in " + _iv + ".");

		if (_iv.isOutOfRange(to, toInc))
			throw new IllegalArgumentException("TO-KEY " + to + "/" + toInc + " nicht in " + _iv + ".");

		return new DummyMapSub(_par, new DummyMapIntervall(from, fromInc, to, toInc), asc);
	}

	private @NotNull DummyMapSubKeySet _createSet(@NotNull Integer from, boolean fromInc, @NotNull Integer to,
			boolean toInc, boolean asc) {
		return new DummyMapSubKeySet(_createMap(from, fromInc, to, toInc, asc));
	}

}
