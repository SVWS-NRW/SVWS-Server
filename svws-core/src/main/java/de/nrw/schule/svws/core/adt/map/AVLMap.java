package de.nrw.schule.svws.core.adt.map;

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
 * Diese Klasse stellt einen AVL-Baum zur Verfügung, welcher eine Zuordnung von Schlüsseln (Keys) des Typs K zu Werten
 * (Value) vom Typ V unterstützt.
 *
 * @author Benjamin A. Bartsch
 * @author Thomas Bachran
 * 
 * @param <K> Der Typ der Schlüssel-Werte.
 * @param <V> Der Typ der zugeordneten Werte.
 */
public class AVLMap<@NotNull K, @NotNull V> implements NavigableMap<@NotNull K, @NotNull V> {

	/**
	 * Ein Dummy-Element für den Schlüsselwert "-Unendlich".
	 */
	@SuppressWarnings("unchecked")
	private final @NotNull K _infinityMinus = (@NotNull K) AVLMapIntervall._INFINITY_MINUS;

	/**
	 * Ein Dummy-Element für den Schlüsselwert "+Unendlich".
	 */
	@SuppressWarnings("unchecked")
	private final @NotNull K _infinityPlus = (@NotNull K) AVLMapIntervall._INFINITY_PLUS;

	/**
	 * Ein Dummy-Element für ein Pseudo-Mapping.
	 */
	@SuppressWarnings("unchecked")
	private final @NotNull V _dummyValue = (@NotNull V) new Object();

	/**
	 * Alle Anfragen werden an die Sub-Map delegiert. Diese hat einen Bereich von "-Unendlich" bis "+Unendlich" und
	 * beinhaltet somit alle Elemente.
	 */
	private final @NotNull AVLMapSubMap<@NotNull K, @NotNull V> _sub = new AVLMapSubMap<>(this,
			new AVLMapIntervall<>(_infinityMinus, false, _infinityPlus, false), true);

	/**
	 * Der {@link Comparator}, der zum Vergleichen der Schlüsselwerte genutzt wird.
	 */
	private final @NotNull Comparator<@NotNull K> _comparator;

	/**
	 * Der {@link Comparator}, der zum Vergleichen der Schlüsselwerte genutzt wird, wenn eine natürliche Ordnung über
	 * das {@link Comparable} - Interface verwendet wird.
	 */
	private final @NotNull Comparator<@NotNull K> _comparatorNatural = (@NotNull K key1, @NotNull K key2) -> {
		if ((key1 == null) || (key2 == null))
			throw new NullPointerException();
		if (!((key1 instanceof Comparable) && (key2 instanceof Comparable)))
			throw new ClassCastException();
		@SuppressWarnings("unchecked")
		@NotNull
		Comparable<@NotNull K> k1 = (@NotNull Comparable<@NotNull K>) key1;
		return k1.compareTo(key2);
	};

	/**
	 * Die Wurzel des Baumes. Bei einem leeren Baum ist diese Referenz NULL.
	 */
	private AVLMapNode<@NotNull K, @NotNull V> _root = null; // NULL-Wert erlaubt.

	/**
	 * Gibt an, ob das Hinzufügen von KEYs ohne VALUE erlaubt ist. Falls TRUE, dann wird der KEY einer Pseudo-VALUE
	 * zugeordnet.
	 */
	private boolean _allowKeyAlone = false;

	/**
	 * Erzeugt einen leere Map, welche bei den Schlüsselwerten die natürliche Ordnung des {@link Comparable} - Interface
	 * nutzt.
	 */
	public AVLMap() {
		_comparator = _comparatorNatural;
	}

	/**
	 * Erstellt eine neue leere Map und nutzt dabei die angegeben Ordnung der Schlüssel.
	 * 
	 * @param comparator Die Ordnung für die Schlüssel.
	 */
	public AVLMap(@NotNull Comparator<@NotNull K> comparator) {
		_comparator = comparator;
	}

	/**
	 * Erstellt eine neue Map mit den Daten aus der angegebenen Map und nutzt dabei die Ordnung dieser Map.
	 * 
	 * @param map Die Map mit den Daten.
	 */
	@SuppressWarnings("unchecked")
	public AVLMap(@NotNull SortedMap<@NotNull K, ? extends @NotNull V> map) {
		_comparator = (@NotNull Comparator<@NotNull @NotNull K>) map.comparator();
		_sub.putAll(map);
	}

	@Override
	public @NotNull String toString() {
		return _sub.toString();
	}

	/**
	 * Bewirkt, dass das Hinzufügen von Keys ohne Value durch {@link AVLMapSubKeySet} erlaubt ist. Die Keys werden auf
	 * einen Dummy-Wert gemapped.
	 * 
	 * @param b Falls TRUE, dürfen KEYs ohne VALUE hinzugefügt werden.
	 */
	public void allowKeyAlone(boolean b) {
		_allowKeyAlone = b;
	}

	@Override
	public boolean equals(@NotNull Object o) {
		return _sub.equals(o);
	}

	@Override
	public int hashCode() {
		return _sub.hashCode();
	}

	// ################################################
	// ################ NavigableMap ##################
	// ################################################

	@Override
	public @NotNull Comparator<@NotNull K> comparator() {
		return _sub.comparator();
	}

	@Override
	public @NotNull K firstKey() {
		return _sub.firstKey();
	}

	@Override
	public @NotNull K lastKey() {
		return _sub.lastKey();
	}

	@Override
	public @NotNull Set<@NotNull K> keySet() {
		return _sub.keySet();
	}

	@Override
	public @NotNull Collection<@NotNull V> values() {
		return _sub.values();
	}

	@Override
	public @NotNull Set<@NotNull Entry<@NotNull K, @NotNull V>> entrySet() {
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
	public boolean containsKey(@NotNull Object key) {
		return _sub.containsKey(key);
	}

	@Override
	public boolean containsValue(@NotNull Object value) {
		return _sub.containsValue(value);
	}

	@Override
	public V get(@NotNull Object key) { // return NULL erlaubt.
		return _sub.get(key);
	}

	@Override
	public V put(@NotNull K key, @NotNull V value) { // return NULL erlaubt.
		return _sub.put(key, value);
	}

	@Override
	public V remove(@NotNull Object key) { // return NULL erlaubt.
		return _sub.remove(key);
	}

	@Override
	public void putAll(@NotNull Map<? extends @NotNull K, ? extends @NotNull V> m) {
		_sub.putAll(m);
	}

	@Override
	public void clear() {
		_sub.clear();
	}

	@Override
	public Entry<@NotNull K, @NotNull V> lowerEntry(@NotNull K key) { // return NULL erlaubt.
		return _sub.lowerEntry(key);
	}

	@Override
	public K lowerKey(@NotNull K key) { // return NULL erlaubt.
		return _sub.lowerKey(key);
	}

	@Override
	public Entry<@NotNull K, @NotNull V> floorEntry(@NotNull K key) { // return NULL erlaubt.
		return _sub.floorEntry(key);
	}

	@Override
	public K floorKey(@NotNull K key) { // return NULL erlaubt.
		return _sub.floorKey(key);
	}

	@Override
	public Entry<@NotNull K, @NotNull V> ceilingEntry(@NotNull K key) { // return NULL erlaubt.
		return _sub.ceilingEntry(key);
	}

	@Override
	public K ceilingKey(@NotNull K key) { // return NULL erlaubt.
		return _sub.ceilingKey(key);
	}

	@Override
	public Entry<@NotNull K, @NotNull V> higherEntry(@NotNull K key) { // return NULL erlaubt.
		return _sub.higherEntry(key);
	}

	@Override
	public K higherKey(@NotNull K key) { // return NULL erlaubt.
		return _sub.higherKey(key);
	}

	@Override
	public Entry<@NotNull K, @NotNull V> firstEntry() { // return NULL erlaubt.
		return _sub.firstEntry();
	}

	@Override
	public Entry<@NotNull K, @NotNull V> lastEntry() { // return NULL erlaubt.
		return _sub.lastEntry();
	}

	@Override
	public Entry<@NotNull K, @NotNull V> pollFirstEntry() { // return NULL erlaubt.
		return _sub.pollFirstEntry();
	}

	@Override
	public Entry<@NotNull K, @NotNull V> pollLastEntry() { // return NULL erlaubt.
		return _sub.pollLastEntry();
	}

	@Override
	public @NotNull NavigableMap<@NotNull K, @NotNull V> descendingMap() {
		return _sub.descendingMap();
	}

	@Override
	public @NotNull NavigableSet<@NotNull K> navigableKeySet() {
		return _sub.navigableKeySet();
	}

	@Override
	public @NotNull NavigableSet<@NotNull K> descendingKeySet() {
		return _sub.descendingKeySet();
	}

	@Override
	public @NotNull NavigableMap<@NotNull K, @NotNull V> subMap(@NotNull K fromKey, boolean fromInclusive,
			@NotNull K toKey, boolean toInclusive) {
		return _sub.subMap(fromKey, fromInclusive, toKey, toInclusive);
	}

	@Override
	public @NotNull NavigableMap<@NotNull K, @NotNull V> headMap(@NotNull K toKey, boolean inclusive) {
		return _sub.headMap(toKey, inclusive);
	}

	@Override
	public @NotNull NavigableMap<@NotNull K, @NotNull V> tailMap(@NotNull K fromKey, boolean inclusive) {
		return _sub.tailMap(fromKey, inclusive);
	}

	@Override
	public @NotNull SortedMap<@NotNull K, @NotNull V> subMap(@NotNull K fromKey, @NotNull K toKey) {
		return _sub.subMap(fromKey, toKey);
	}

	@Override
	public @NotNull SortedMap<@NotNull K, @NotNull V> headMap(@NotNull K toKey) {
		return _sub.headMap(toKey);
	}

	@Override
	public @NotNull SortedMap<@NotNull K, @NotNull V> tailMap(@NotNull K fromKey) {
		return _sub.tailMap(fromKey);
	}

	// ##########################################################
	// ################## PROTECTED (backcalls) #################
	// ##########################################################

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Fügt ein Entry der Datenstruktur hinzu.
	 * 
	 * @param e  Das einzufügende Entry.
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return TRUE, falls das Entry (e.getKey(), e.getValue()) neu war und somit hinzugefügt wurde.
	 */
	boolean bcAddEntryReturnBool(@NotNull Entry<@NotNull K, @NotNull V> e, @NotNull AVLMapIntervall<@NotNull K> iv) {
		V old = bcAddEntryReturnOldValueOrNull(e.getKey(), e.getValue(), iv);
		return !_valEquals(old, e.getValue());
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Fügt ein Entry bzw. ein Key-Value-Paar der Datenstruktur hinzu.
	 * 
	 * @param key   Der Schlüssel (Key) des Entrys.
	 * @param value Der zum Schlüssel (Key) zugehörige Wert (Value).
	 * @param iv    Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Den alten Wert (Value), falls ein zugehöriger Schlüssel (Key) existierte, sonst NULL.
	 */
	V bcAddEntryReturnOldValueOrNull(@NotNull K key, @NotNull V value, @NotNull AVLMapIntervall<@NotNull K> iv) {
		if (key == null) // Sonderfall: NULL-Key
			throw new NullPointerException("TreeMap erlaubt keine NULL keys.");
		if (_isOutOfRange(key, iv)) // Sonderfall: Bereich
			throw new IllegalArgumentException("Der Schlüsselwert liegt nicht im gültigen Bereich.");
		if (_root == null) { // Sonderfall: Baum leer
			_root = new AVLMapNode<>(key, value);
			return null;
		}
		// Alten Wert (Value) sichern.
		AVLMapNode<@NotNull K, @NotNull V> node = _nodeGetOrNull(key, iv);
		V old = (node == null) ? null : node._val;
		// Entry einfügen.
		_root = _nodePutRecursive(_root, key, value);
		return old;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Fügt alle Entries der Collection der Datenstruktur hinzu.
	 * 
	 * @param c  Die Collection mit den einzufügenden Entries.
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return TRUE, falls mindestens ein Entry neu war und somit hinzugefügt wurde.
	 */
	boolean bcAddAllEntries(@NotNull Collection<? extends @NotNull Entry<@NotNull K, @NotNull V>> c,
			@NotNull AVLMapIntervall<@NotNull K> iv) {
		boolean changed = false;
		for (@NotNull
		Entry<@NotNull K, @NotNull V> entry : c)
			changed |= bcAddEntryReturnBool(entry, iv);
		return changed;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Fügt alle Entrys der übergebenen Map dieser Datenstruktur hinzu.
	 * 
	 * @param map Die Map, deren Entries dieser Datenstruktur hinzugefügt werden soll.
	 * @param iv  Das Intervall der {@link AVLMapSubMap}.
	 */
	void bcAddAllEntriesOfMap(@NotNull Map<? extends @NotNull K, ? extends @NotNull V> map,
			@NotNull AVLMapIntervall<@NotNull K> iv) {
		for (@NotNull
		Entry<? extends @NotNull K, ? extends @NotNull V> entry : map.entrySet())
			bcAddEntryReturnOldValueOrNull(entry.getKey(), entry.getValue(), iv);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Bei dem Versuch einen Schlüssel (Key) ohne Wert (Value) hinzuzufügen,
	 * kann es zu einer {@link UnsupportedOperationException} kommen, wenn das Attribut {@link #_allowKeyAlone} auf
	 * FALSE gesetzt ist. Andernfalls wird dem Schlüssel (Key) ein Dummy-Wert {@link #_dummyValue} zugeordnet. Der
	 * Schlüssel (Key) wird jedoch nur dann hinzugefügt, falls er noch nicht existierte.
	 * 
	 * @param e  Der Schlüssel (Key) der hinzugefügt werden soll.
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return TRUE, falls der Schlüssel (Key) noch nicht existierte und somit hinzugefügt wurde.
	 * @throws UnsupportedOperationException wenn ein alleiniges Hinzufügen eines Schlüssels nicht erlaubt ist.
	 */
	boolean bcAddKey(@NotNull K e, @NotNull AVLMapIntervall<@NotNull K> iv) {
		if (_allowKeyAlone == false)
			throw new UnsupportedOperationException(); // KEY kann nicht ohne VALUE hinzugefügt werden.
		if (bcContainsKey(e, iv))
			return false;
		bcAddEntryReturnOldValueOrNull(e, _dummyValue, iv);
		return true;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Versucht alle Schlüssel (Keys) der Collection hinzuzufügen. Ob das
	 * Hinzufügen eines Schlüssels (Key) ohne Wert (Value) erlaubt ist, hängt vom Attribut {@link #_allowKeyAlone} ab.
	 * 
	 * @param c  Die Collection mit allen Schlüsseln (Keys) die hinzugefügt werden sollen.
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return TRUE, falls mindestens ein Schlüssel (Key) noch nicht existierte und somit hinzugefügt wurde.
	 * @throws UnsupportedOperationException wenn ein alleiniges Hinzufügen eines Schlüssels nicht erlaubt ist.
	 */
	boolean bcAddAllKeys(@NotNull Collection<? extends @NotNull K> c, @NotNull AVLMapIntervall<@NotNull K> iv) {
		boolean changed = false;
		for (K key : c)
			changed |= bcAddKey(key, iv);
		return changed;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Überprüft, ob ein Schlüssel (Key) in dieser Datenstruktur existiert.
	 * 
	 * @param objKey Der Schlüssel (Key) nach dem gesucht wird.
	 * @param iv     Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return TRUE, falls der Schlüssel (Key) in dieser Datenstruktur existiert.
	 */
	@SuppressWarnings("unchecked")
	boolean bcContainsKey(@NotNull Object objKey, @NotNull AVLMapIntervall<@NotNull K> iv) {
		return _nodeGetOrNull((@NotNull K) objKey, iv) != null;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Überprüft, ob alle Schlüssel (Keys) der Collection in dieser
	 * Datenstruktur existieren.
	 * 
	 * @param c  Die Collection mit allen Schlüsseln (Keys), welche überprüft werden sollen.
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return TRUE, falls alle Schlüssel (Keys) der Collection in dieser Datenstruktur existieren.
	 */
	boolean bcContainsAllKeys(@NotNull Collection<@NotNull ?> c, @NotNull AVLMapIntervall<@NotNull K> iv) {
		for (@NotNull
		Object key : c)
			if (!bcContainsKey(key, iv))
				return false;
		return true;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Überprüft, ob ein Wert (Value) in dieser Datenstruktur existiert. Die
	 * Laufzeit ist linear, da die gesamte Datenstruktur überprüft werden muss.
	 * 
	 * @param objValue Der Wert (Value) nach dem gesucht wird.
	 * @param iv       Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return TRUE, falls der Wert (Value) in dieser Datenstruktur existiert.
	 */
	boolean bcContainsValue(@NotNull Object objValue, @NotNull AVLMapIntervall<@NotNull K> iv) {
		@SuppressWarnings("unchecked")
		@NotNull
		V value = (@NotNull V) objValue;
		AVLMapNode<@NotNull K, @NotNull V> n1 = _nodeFirstOrNull(iv);
		if (n1 == null)
			return false;
		AVLMapNode<@NotNull K, @NotNull V> n2 = _nodeLastOrNull(iv);
		if (n2 == null)
			return false; // kann nicht passieren.
		while (n1 != n2) {
			if (n1 == null)
				throw new NullPointerException(); // kann/sollte nicht passieren.
			if (_valEquals(n1._val, value))
				return true;
			n1 = n1._next; // iv-Check nicht nötig.
		}
		return _valEquals(n2._val, value);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Überprüft, ob alle Werte (Values) aus der Collection in dieser
	 * Datenstruktur vorkommen. Diese Methode sollte NICHT verwendet werden, da sie quadratische Laufzeit hat.
	 * 
	 * @param c  Die Collection deren Werte (Values) überprüft werden sollen.
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return TRUE, falls alle Werte (Values) der Collection in dieser Datenstruktur existieren.
	 */
	boolean bcContainsAllValues(@NotNull Collection<@NotNull ?> c, @NotNull AVLMapIntervall<@NotNull K> iv) {
		for (@NotNull
		Object val : c)
			if (!bcContainsValue(val, iv))
				return false;
		return true;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Überprüft, ob das übergebene Entry in dieser Datenstruktur existiert.
	 * 
	 * @param o  Das Entry (Schlüssel-Wert-Paar) nach dem gesucht wird.
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return TRUE, falls das übergebene Entry in dieser Datenstruktur existiert.
	 */
	@SuppressWarnings("unchecked")
	boolean bcContainsEntry(@NotNull Object o, @NotNull AVLMapIntervall<@NotNull K> iv) {
		if (o instanceof Entry == false)
			return false;
		@NotNull
		Entry<@NotNull K, @NotNull V> e = (@NotNull Entry<@NotNull K, @NotNull V>) o;
		AVLMapNode<@NotNull K, @NotNull V> node = _nodeGetOrNull(e.getKey(), iv);
		return (node == null) ? false : _valEquals(node._val, e.getValue());
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Überprüft, ob alle Entries der Collection in dieser Datenstruktur
	 * existieren.
	 * 
	 * @param c  Die Collection mit den Entries welche überprüft werden sollen.
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return TRUE, falls alle Entries in dieser Datenstruktur existieren.
	 */
	boolean bcContainsAllEntries(@NotNull Collection<@NotNull ?> c, @NotNull AVLMapIntervall<@NotNull K> iv) {
		for (@NotNull
		Object entry : c)
			if (!bcContainsEntry(entry, iv))
				return false;
		return true;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Versucht einen Schlüssel (Key) aus dieser Datenstruktur zu entfernen.
	 * In dieser Implementierung kann ein Schlüssel (Key) keinem NULL-Wert zugeordnet sein. Ist das Ergebnis NULL,
	 * bedeutet dies, dass der Schlüssel (Key) definitiv nicht existierte.
	 * 
	 * @param obj Der Schlüssel (Key), welcher entfernt werden soll.
	 * @param iv  Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Liefert den zum Schlüssel (Key) zugehörigen Wert (Value), falls es eine Zuordnung gab, andernfalls NULL.
	 */
	@SuppressWarnings("unchecked")
	V bcRemoveKeyReturnOldValueOrNull(@NotNull Object obj, @NotNull AVLMapIntervall<@NotNull K> iv) {
		if (obj == null) // Sonderfall: NULL-Key
			throw new NullPointerException("TreeMap unterstützt keine NULL-Schlüssel.");
		@NotNull
		K key = (@NotNull K) obj;
		// Alten Wert (Value) sichern.
		AVLMapNode<@NotNull K, @NotNull V> old = _nodeGetOrNull(key, iv);
		if (old == null)
			return null;
		if (_root == null)
			throw new NullPointerException(); // Transpiler-Hilfe
		// Schlüssel (Key) löschen.
		_root = _nodeRemoveKeyRecursive(_root, key);
		return old._val;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Entfernt einen Schlüssel (Key) aus dieser Datenstruktur.
	 * 
	 * @param o  Der Schlüssel (Key) der entfernt werden soll.
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return TRUE, falls der Schlüssel existierte und somit entfernt wurde.
	 */
	boolean bcRemoveKeyReturnBool(@NotNull Object o, @NotNull AVLMapIntervall<@NotNull K> iv) {
		if (!bcContainsKey(o, iv))
			return false; // keine Exception (anders als in der JAVA-TreeMap-Implementation)
		bcRemoveKeyReturnOldValueOrNull(o, iv);
		return true;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Entfernt alle Schlüssel (Keys) der Collection aus dieser Datenstruktur.
	 * 
	 * @param c  Die Collection mit allen Schlüsseln (Keys) die entfernt werden sollen.
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return TRUE, falls mindestens ein Schlüssel (Key) entfernt wurde.
	 */
	boolean bcRemoveAllKeys(@NotNull Collection<@NotNull ?> c, @NotNull AVLMapIntervall<@NotNull K> iv) {
		boolean changed = false;
		for (@NotNull
		Object obj : c)
			changed |= bcRemoveKeyReturnBool(obj, iv);
		return changed;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Entfernt das Entry aus dieser Datenstruktur.
	 * 
	 * @param o  Das Entry, welches entfernt werden soll.
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return TRUE, falls das Entry in der Datenstruktur existierte und somit entfernt wurde.
	 */
	@SuppressWarnings("unchecked")
	boolean bcRemoveEntry(@NotNull Object o, @NotNull AVLMapIntervall<@NotNull K> iv) {
		if (o instanceof Entry == false)
			return false;
		if (!bcContainsEntry(o, iv))
			return false;
		if (_root == null)
			throw new NullPointerException(); // Transpiler-Hilfe
		@NotNull
		Entry<@NotNull K, @NotNull V> e = (@NotNull Entry<@NotNull K, @NotNull V>) o;
		_root = _nodeRemoveKeyRecursive(_root, e.getKey());
		return true;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Entfernt alle Entries der Collection aus dieser Datenstruktur.
	 * 
	 * @param c  Die Collection mit den Entries, welche entfernt werden sollen.
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return TRUE, falls mindestens ein Entry entfernt wurde.
	 */
	boolean bcRemoveAllEntries(@NotNull Collection<@NotNull ?> c, @NotNull AVLMapIntervall<@NotNull K> iv) {
		boolean removedAny = false;
		for (@NotNull
		Object entry : c)
			removedAny |= bcRemoveEntry(entry, iv);
		return removedAny;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Entfernt und liefert das erste Entry dieser Datenstruktur.
	 * 
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Entfernt und liefert das erste Entry dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	Entry<@NotNull K, @NotNull V> bcPollFirstEntryOrNull(@NotNull AVLMapIntervall<@NotNull K> iv) {
		AVLMapNode<@NotNull K, @NotNull V> node = _nodeFirstOrNull(iv);
		if (node == null)
			return null;
		if (_root == null)
			throw new NullPointerException(); // Transpiler-Hilfe
		_root = _nodeRemoveKeyRecursive(_root, node._key);
		return node;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Entfernt und liefert den ersten Schlüssel (Key) dieser Datenstruktur.
	 * 
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Entfernt und liefert den ersten Schlüssel (Key) dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	K bcPollFirstKeyOrNull(@NotNull AVLMapIntervall<@NotNull K> iv) {
		AVLMapNode<@NotNull K, @NotNull V> node = _nodeFirstOrNull(iv);
		if (node == null)
			return null;
		if (_root == null)
			throw new NullPointerException(); // kann/sollte nicht passieren.
		_root = _nodeRemoveKeyRecursive(_root, node._key);
		return node._key;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Entfernt und liefert das letzte Entry dieser Datenstruktur.
	 * 
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Entfernt und liefert das letzte Entry dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	Entry<@NotNull K, @NotNull V> bcPollLastEntryOrNull(@NotNull AVLMapIntervall<@NotNull K> iv) {
		AVLMapNode<@NotNull K, @NotNull V> node = _nodeLastOrNull(iv);
		if (node == null)
			return null;
		if (_root == null)
			throw new NullPointerException(); // kann/sollte nicht passieren.
		_root = _nodeRemoveKeyRecursive(_root, node._key);
		return node;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Entfernt und liefert den letzten Schlüssel (Key) dieser Datenstruktur.
	 * 
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Entfernt und liefert den letzten Schlüssel (Key) dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	K bcPollLastKeyOrNull(@NotNull AVLMapIntervall<@NotNull K> iv) {
		AVLMapNode<@NotNull K, @NotNull V> node = _nodeLastOrNull(iv);
		if (node == null)
			return null;
		if (_root == null)
			throw new NullPointerException(); // kann/sollte nicht passieren.
		_root = _nodeRemoveKeyRecursive(_root, node._key);
		return node._key;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Liefert die Anzahl der Elemente innerhalb des übergebenen Intervalls.
	 * 
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Liefert die Anzahl der Elemente innerhalb des übergebenen Intervalls.
	 */
	int bcGetSize(@NotNull AVLMapIntervall<@NotNull K> iv) {
		AVLMapNode<@NotNull K, @NotNull V> n1 = _nodeFirstOrNull(iv);
		if (n1 == null)
			return 0;
		AVLMapNode<@NotNull K, @NotNull V> n2 = _nodeLastOrNull(iv);
		if (n2 == null)
			return 0; // Transpiler-Hilfe
		return _nodeIndexOf(n2._key) - _nodeIndexOf(n1._key) + 1;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Überprüft, ob die Datenstruktur innerhalb des Intervalls leer ist.
	 * 
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return TRUE, falls die Datenstruktur innerhalb des Intervalls leer ist.
	 */
	boolean bcIsEmpty(@NotNull AVLMapIntervall<@NotNull K> iv) {
		// Hinweis: Mann kann nicht testen, ob die Wurzel-Referenz NULL ist,
		// da sich die Anfrage auf das Intervall "iv" bezieht.
		return _nodeFirstOrNull(iv) == null;
	}

	/**
	 * Liefert den Comparator dieser Map.
	 * 
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Liefert den Comparator dieser Map.
	 */
	@NotNull
	Comparator<@NotNull K> bcGetComparator(@NotNull AVLMapIntervall<@NotNull K> iv) {
		return _comparator;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Liefert das erste Entry dieser Datenstruktur.
	 * 
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Das erste Entry dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	AVLMapNode<@NotNull K, @NotNull V> bcGetFirstEntryOrNull(@NotNull AVLMapIntervall<@NotNull K> iv) {
		return _nodeFirstOrNull(iv);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Liefert den ersten Schlüssel (Key) dieser Datenstruktur.
	 * 
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Liefert den ersten Schlüssel (Key) dieser Datenstruktur, falls vorhanden.
	 * @throws NoSuchElementException falls es kein erstes Element gibt.
	 */
	@NotNull
	K bcGetFirstKeyOrException(@NotNull AVLMapIntervall<@NotNull K> iv) {
		return _keyOrExeption(_nodeFirstOrNull(iv));
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Liefert das letzte Entry dieser Datenstruktur.
	 * 
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Liefert das letzte Entry dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	AVLMapNode<@NotNull K, @NotNull V> bcGetLastEntryOrNull(@NotNull AVLMapIntervall<@NotNull K> iv) {
		return _nodeLastOrNull(iv);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Liefert den letzten Schlüssel (Key) dieser Datenstruktur.
	 * 
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Liefert den letzten Schlüssel (Key) dieser Datenstruktur falls vorhanden.
	 * @throws NoSuchElementException falls es kein letztes Element gibt.
	 */
	@NotNull
	K bcGetLastKeyOrException(@NotNull AVLMapIntervall<@NotNull K> iv) {
		return _keyOrExeption(_nodeLastOrNull(iv));
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Liefert das nächste Entry dieser Datenstruktur.
	 * 
	 * @param current Das aktuelle Entry .
	 * @param iv      Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Liefert das nächste Entry dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	AVLMapNode<@NotNull K, @NotNull V> bcGetNextEntryOrNull(@NotNull AVLMapNode<@NotNull K, @NotNull V> current,
			@NotNull AVLMapIntervall<@NotNull K> iv) {
		return _nodeNextOrNull(current, iv);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Liefert das vorherige Entry dieser Datenstruktur.
	 * 
	 * @param current Das aktuelle Entry.
	 * @param iv      Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Liefert das vorherige Entry dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	AVLMapNode<@NotNull K, @NotNull V> bcGetPrevEntryOrNull(@NotNull AVLMapNode<@NotNull K, @NotNull V> current,
			@NotNull AVLMapIntervall<@NotNull K> iv) {
		return _nodePrevOrNull(current, iv);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Liefert den Wert (Value) eines bestimmten Schlüssels (Key).
	 * 
	 * @param objKey Der Schlüssel (Key) dessen Wert (Value) angefordert wird.
	 * @param iv     Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Den Wert (Value) eines bestimmten Schlüssels (Key) falls vorhanden, sonst NULL.
	 */
	@SuppressWarnings("unchecked")
	V bcGetValueOfKeyOrNull(@NotNull Object objKey, @NotNull AVLMapIntervall<@NotNull K> iv) {
		@NotNull
		K key = (@NotNull K) objKey;
		AVLMapNode<@NotNull K, @NotNull V> node = _nodeGetOrNull(key, iv);
		return (node == null) ? null : node._val;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Liefert das größte Entry welches kleiner ist als der übergebene
	 * Schlüssel (Key), somit den Vorgänger-Entry des Schlüssels (Key).
	 * 
	 * @param key Der Schlüssel (Key) dessen Vorgänger-Entry gesucht wird.
	 * @param iv  Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Liefert den Vorgänger-Entry des Schlüssels (Key) falls vorhanden, sonst NULL.
	 */
	AVLMapNode<@NotNull K, @NotNull V> bcGetLowerEntryOrNull(@NotNull K key, @NotNull AVLMapIntervall<@NotNull K> iv) {
		return _nodeLowerOrNull(key, iv);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Liefert den größten Schlüssel (Key) welcher kleiner ist als der
	 * übergebene Schlüssel (Key), somit den Vorgänger-Schlüssel des Schlüssels (Key).
	 * 
	 * @param key Der Schlüssel (Key) dessen Vorgänger gesucht wird.
	 * @param iv  Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Den Vorgänger-Schlüssel des übergebenen Schlüssels (Key) falls vorhanden, sonst NULL.
	 */
	K bcGetLowerKeyOrNull(@NotNull K key, @NotNull AVLMapIntervall<@NotNull K> iv) {
		return _keyOrNull(_nodeLowerOrNull(key, iv));
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Liefert das größte Entry welches kleiner oder gleich dem übergebenen
	 * Schlüssel (Key) ist. Somit das zugehörige Entry des Schlüssels (Key) falls vorhanden, andernfalls den
	 * Vorgänger-Entry falls vorhanden, andernfalls NULL.
	 * 
	 * @param key Der Schlüssel (Key) dessen Entry bzw. Vorgänger-Entry gesucht wird.
	 * @param iv  Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Das zugehörige Entry des Schlüssels (Key) falls vorhanden, andernfalls den Vorgänger-Entry falls
	 *         vorhanden, andernfalls NULL.
	 */
	AVLMapNode<@NotNull K, @NotNull V> bcGetFloorEntryOrNull(@NotNull K key, @NotNull AVLMapIntervall<@NotNull K> iv) {
		return _nodeFloorOrNull(key, iv);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Liefert den größten Schlüssel (Key) welcher kleiner oder gleich dem
	 * übergebenen Schlüssel (Key) ist. Somit der selbe Schlüssel (Key) falls vorhanden, andernfalls den
	 * Vorgänger-Schlüssel (Key) falls vorhanden, andernfalls NULL.
	 * 
	 * @param key Der Schlüssel (Key) der gesucht wird bzw. sein Vorgänger-Schlüssel.
	 * @param iv  Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Den selben Schlüssel (Key) falls vorhanden, andernfalls sein Vorgänger-Schlüssel falls vorhanden,
	 *         andernfalls NULL.
	 */
	K bcGetFloorKeyOrNull(@NotNull K key, @NotNull AVLMapIntervall<@NotNull K> iv) {
		return _keyOrNull(_nodeFloorOrNull(key, iv));
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Liefert das kleinste Entry welches größer oder gleich dem übergebenen
	 * Schlüssel (Key) ist. Somit das zugehörige Entry des Schlüssels (Key) falls vorhanden, andernfalls den
	 * Nachfolger-Entry falls vorhanden, andernfalls NULL.
	 * 
	 * @param key Der Schlüssel (Key) dessen Entry bzw. Nachfolger-Entry gesucht wird.
	 * @param iv  Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Das zugehörige Entry des Schlüssels (Key) falls vorhanden, andernfalls den Nachfolger-Entry falls
	 *         vorhanden, andernfalls NULL.
	 */
	AVLMapNode<@NotNull K, @NotNull V> bcGetCeilingEntryOrNull(@NotNull K key,
			@NotNull AVLMapIntervall<@NotNull K> iv) {
		return _nodeCeilingOrNull(key, iv);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Liefert den kleinsten Schlüssel (Key) welcher größer oder gleich dem
	 * übergebenen Schlüssel (Key) ist. Somit der selbe Schlüssel (Key) falls vorhanden, andernfalls sein
	 * Nachfolger-Schlüssel (Key) falls vorhanden, andernfalls NULL.
	 * 
	 * @param key Der Schlüssel (Key) der gesucht wird bzw. sein Nachfolger-Schlüssel.
	 * @param iv  Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Den selben Schlüssel (Key) falls vorhanden, andernfalls sein Nachfolger-Schlüssel falls vorhanden,
	 *         andernfalls NULL.
	 */
	K bcGetCeilingKeyOrNull(@NotNull K key, @NotNull AVLMapIntervall<@NotNull K> iv) {
		return _keyOrNull(_nodeCeilingOrNull(key, iv));
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Liefert das kleinste Entry welches größer ist als der übergebene
	 * Schlüssel (Key), somit den Nachfolger-Entry des Schlüssels (Key).
	 * 
	 * @param key Der Schlüssel (Key) dessen Nachfolger-Entry gesucht wird.
	 * @param iv  Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Liefert den Nachfolger-Entry des Schlüssels (Key) falls vorhanden, sonst NULL.
	 */
	AVLMapNode<@NotNull K, @NotNull V> bcGetHigherEntryOrNull(@NotNull K key, @NotNull AVLMapIntervall<@NotNull K> iv) {
		return _nodeHigherOrNull(key, iv);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Liefert den kleinsten Schlüssel (Key) welcher größer ist als der
	 * übergebene Schlüssel (Key), somit den Nachfolger-Schlüssel des übergebenen Schlüssels (Key).
	 * 
	 * @param key Der Schlüssel (Key) dessen Nachfolger-Schlüssel gesucht wird.
	 * @param iv  Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Den Nachfolger-Schlüssel des übergebenen Schlüssels (Key) falls vorhanden, sonst NULL.
	 */
	K bcGetHigherKeyOrNull(@NotNull K key, @NotNull AVLMapIntervall<@NotNull K> iv) {
		return _keyOrNull(_nodeHigherOrNull(key, iv));
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Überprüft, ob ein übergebener Schlüssel sich außerhalb des Bereichs des
	 * übergebenen Intervalls befindet. Der Parameter {@code inc} gibt an, auf der übergebene Schlüssel selbst inklusive
	 * zu interpretieren ist.
	 * 
	 * @param key Der gesuchte Schlüssel.
	 * @param inc Falls TRUE, dann ist der Schlüssel key inklusive zu interpretieren.
	 * @param iv  Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return TRUE, falls der übergebene Schlüssel außerhalb des übergebenen Intervalls ist.
	 */
	boolean bcCheckOutOfIntervall(@NotNull K key, boolean inc, @NotNull AVLMapIntervall<@NotNull K> iv) {
		if ((key == _infinityMinus) || (key == _infinityPlus))
			return false;

		int cmpF = _compare(key, iv.from);
		// Fall: Links von "from" --> außerhalb
		if (cmpF < 0)
			return true;
		// Fall: Gleich "from", aber Intervall nicht inklusive und Schlüssel inklusive --> außerhalb
		if ((cmpF == 0) && (!iv.fromInc) && (inc))
			return true;

		int cmpT = _compare(key, iv.to);
		// Fall: Rechts von "to" --> außerhalb
		if (cmpT > 0)
			return true;
		// Fall: Gleich "to", aber Intervall nicht inklusive und Schlüssel inklusive --> außerhalb
		if ((cmpT == 0) && (!iv.toInc) && (inc))
			return true;

		return false;
	}

	// ##########################################################
	// ################# PRIVATE (intern calls) #################
	// ##########################################################

	private K _keyOrNull(AVLMapNode<@NotNull K, @NotNull V> node) {
		return (node == null) ? null : node._key;
	}

	private boolean _valEquals(V v1, V v2) {
		return (v1 == null) ? v2 == null : v1.equals(v2);
	}

	private @NotNull K _keyOrExeption(AVLMapNode<@NotNull K, @NotNull V> node) {
		if (node == null)
			throw new NoSuchElementException();
		return node._key;
	}

	private int _compare(@NotNull K key1, @NotNull K key2) {
		if ((key1 == _infinityMinus) || (key2 == _infinityPlus))
			return -1;
		if ((key1 == _infinityPlus) || (key2 == _infinityMinus))
			return +1;
		return _comparator.compare(key1, key2);
	}

	private boolean _isOutOfRange(@NotNull K key, @NotNull AVLMapIntervall<@NotNull K> iv) {
		int cmpKeyFrom = _compare(key, iv.from);
		if ((cmpKeyFrom < 0) || (cmpKeyFrom == 0) && (!iv.fromInc))
			return true;
		int cmpKeyTo = _compare(key, iv.to);
		if ((cmpKeyTo > 0) || (cmpKeyTo == 0) && (!iv.toInc))
			return true;
		return false;
	}

	private AVLMapNode<@NotNull K, @NotNull V> _nodeFirstOrNull(@NotNull AVLMapIntervall<@NotNull K> iv) {
		return iv.fromInc ? //
				_nodeCeilingOrNull(iv.from, iv) //
				: _nodeHigherOrNull(iv.from, iv);
	}

	private AVLMapNode<@NotNull K, @NotNull V> _nodeLastOrNull(@NotNull AVLMapIntervall<@NotNull K> iv) {
		return iv.toInc ? //
				_nodeFloorOrNull(iv.to, iv) //
				: _nodeLowerOrNull(iv.to, iv);
	}

	private AVLMapNode<@NotNull K, @NotNull V> _nodeCeilingOrNull(@NotNull K key,
			@NotNull AVLMapIntervall<@NotNull K> iv) {
		AVLMapNode<@NotNull K, @NotNull V> node = _nodeDeepestOrNull(key, iv);
		if (node == null)
			return null;
		int cmpNodeKey = _compare(node._key, key);
		return cmpNodeKey >= 0 ? node : _nodeNextOrNull(node, iv);
	}

	private AVLMapNode<@NotNull K, @NotNull V> _nodeHigherOrNull(@NotNull K key,
			@NotNull AVLMapIntervall<@NotNull K> iv) {
		AVLMapNode<@NotNull K, @NotNull V> node = _nodeDeepestOrNull(key, iv);
		if (node == null)
			return null;
		int cmpNodeKey = _compare(node._key, key);
		return cmpNodeKey > 0 ? node : _nodeNextOrNull(node, iv);
	}

	private AVLMapNode<@NotNull K, @NotNull V> _nodeFloorOrNull(@NotNull K key,
			@NotNull AVLMapIntervall<@NotNull K> iv) {
		AVLMapNode<@NotNull K, @NotNull V> node = _nodeDeepestOrNull(key, iv);
		if (node == null)
			return null;
		int cmpNodeKey = _compare(node._key, key);
		return cmpNodeKey <= 0 ? node : _nodePrevOrNull(node, iv);
	}

	private AVLMapNode<@NotNull K, @NotNull V> _nodeLowerOrNull(@NotNull K key,
			@NotNull AVLMapIntervall<@NotNull K> iv) {
		AVLMapNode<@NotNull K, @NotNull V> node = _nodeDeepestOrNull(key, iv);
		if (node == null)
			return null;
		int cmpNodeKey = _compare(node._key, key);
		return cmpNodeKey < 0 ? node : _nodePrevOrNull(node, iv);
	}

	private AVLMapNode<@NotNull K, @NotNull V> _nodeNextOrNull(@NotNull AVLMapNode<@NotNull K, @NotNull V> node,
			@NotNull AVLMapIntervall<@NotNull K> iv) {
		AVLMapNode<@NotNull K, @NotNull V> next = node._next;
		return (next == null) ? null : _isOutOfRange(next._key, iv) ? null : next;
	}

	private AVLMapNode<@NotNull K, @NotNull V> _nodePrevOrNull(@NotNull AVLMapNode<@NotNull K, @NotNull V> node,
			@NotNull AVLMapIntervall<@NotNull K> iv) {
		AVLMapNode<@NotNull K, @NotNull V> prev = node._prev;
		return (prev == null) ? null : _isOutOfRange(prev._key, iv) ? null : prev;
	}

	private AVLMapNode<@NotNull K, @NotNull V> _nodeGetOrNull(@NotNull K key, @NotNull AVLMapIntervall<@NotNull K> iv) {
		AVLMapNode<@NotNull K, @NotNull V> node = _nodeDeepestOrNull(key, iv);
		if (node == null)
			return null;
		return _compare(key, node._key) == 0 ? node : null;
	}

	// Der KEY muss im Baum existieren!
	private int _nodeIndexOf(@NotNull K key) {
		AVLMapNode<@NotNull K, @NotNull V> current = _root;

		int index = 0;
		while (true) {
			if (current == null)
				throw new NullPointerException(); // kann/sollte nicht passieren.
			int cmp = _compare(key, current._key);
			if (cmp < 0) {
				current = current._childL;
				continue;
			}
			AVLMapNode<@NotNull K, @NotNull V> left = current._childL;
			int sizeL = (left == null) ? 0 : left._size;
			if (cmp > 0) {
				index += sizeL + 1;
				current = current._childR;
				continue;
			}
			return index + sizeL;
		}
	}

	private AVLMapNode<@NotNull K, @NotNull V> _nodeDeepestOrNull(@NotNull K key,
			@NotNull AVLMapIntervall<@NotNull K> iv) {
		AVLMapNode<@NotNull K, @NotNull V> current = _root;
		AVLMapNode<@NotNull K, @NotNull V> last = null; // Letztes gültiges gefundenes Element im Intervall.

		while (current != null) {
			// Fall: Gültiger Bereich liegt links
			int cmpToKey = _compare(iv.to, current._key);
			if ((cmpToKey < 0) || (cmpToKey == 0) && (!iv.toInc)) {
				current = current._childL;
				continue;
			}

			// Fall: Gültiger Bereich liegt rechts
			int cmpFromKey = _compare(iv.from, current._key);
			if ((cmpFromKey > 0) || (cmpFromKey == 0) && (!iv.fromInc)) {
				current = current._childR;
				continue;
			}

			last = current;
			int cmp = _compare(key, current._key);

			// Fall: Links weitersuchen
			if (cmp < 0) {
				current = current._childL;
				continue;
			}

			// Fall: Rechts weitersuchen
			if (cmp > 0) {
				current = current._childR;
				continue;
			}

			// Fall: KEY gefunden
			return current;
		}

		return last;
	}

	private @NotNull AVLMapNode<@NotNull K, @NotNull V> _nodePutRecursive(
			@NotNull AVLMapNode<@NotNull K, @NotNull V> current, @NotNull K key, @NotNull V value) {
		int cmp = _compare(key, current._key);

		if (cmp == 0) { // Key gefunden --> Value ersetzen
			current._val = value;
			return current;
		}

		if (cmp < 0) // links (einfügen oder weitersuchen)
			current._childL = (current._childL == null) ? // ...
					_nodeCreateLeaf(current._prev, current, key, value) // ...
					: _nodePutRecursive(current._childL, key, value);
		else // rechts (einfügen oder weitersuchen)
			current._childR = (current._childR == null) ? // ...
					_nodeCreateLeaf(current, current._next, key, value)// ...
					: _nodePutRecursive(current._childR, key, value);

		return _nodeRevalidate(current); // ggf. rotieren?
	}

	private @NotNull AVLMapNode<@NotNull K, @NotNull V> _nodeCreateLeaf(AVLMapNode<@NotNull K, @NotNull V> prev,
			AVLMapNode<@NotNull K, @NotNull V> next, @NotNull K key, @NotNull V value) {
		AVLMapNode<@NotNull K, @NotNull V> child = new AVLMapNode<>(key, value);
		if (prev != null) {
			prev._next = child;
			child._prev = prev;
		}
		if (next != null) {
			next._prev = child;
			child._next = next;
		}
		return child;
	}

	// Darf nur aufgerufen werden, wenn der Schlüssel existiert!
	// return NULL möglich!
	private AVLMapNode<@NotNull K, @NotNull V> _nodeRemoveKeyRecursive(
			@NotNull AVLMapNode<@NotNull K, @NotNull V> current, @NotNull K key) {
		int cmp = _compare(key, current._key);
		// Fall: Links weitersuchen
		if (cmp < 0) {
			if (current._childL == null)
				throw new NullPointerException(); // kann/sollte nicht passieren.
			current._childL = _nodeRemoveKeyRecursive(current._childL, key);
			return _nodeRevalidate(current);
		}
		// Fall: Rechts weitersuchen
		if (cmp > 0) {
			if (current._childR == null)
				throw new NullPointerException(); // kann/sollte nicht passieren.
			current._childR = _nodeRemoveKeyRecursive(current._childR, key);
			return _nodeRevalidate(current);
		}
		// Fall (cmp == 0): Direkt löschen (rechtes Kind hochziehen)
		if (current._childL == null) {
			_nodeRemovePrevNext(current);
			return current._childR; // _revalidateNode nicht nötig
		}
		// Fall (cmp == 0): Direkt löschen (linkes Kind hochziehen)
		if (current._childR == null) {
			_nodeRemovePrevNext(current);
			return current._childL; // _revalidateNode nicht nötig
		}
		// Fall (cmp == 0): Lösche 'next', ersetze dann 'current' durch 'next'.
		AVLMapNode<@NotNull K, @NotNull V> next = current._next;
		if (next == null)
			throw new NullPointerException(); // kann/sollte nicht passieren.
		current._childR = _nodeRemoveKeyRecursive(current._childR, next._key);
		return _nodeRevalidate(_nodeReplaceReferencesFromAwithB(next, current));
	}

	private @NotNull AVLMapNode<@NotNull K, @NotNull V> _nodeReplaceReferencesFromAwithB(
			@NotNull AVLMapNode<@NotNull K, @NotNull V> a, @NotNull AVLMapNode<@NotNull K, @NotNull V> b) {
		a._childL = b._childL;
		a._childR = b._childR;
		AVLMapNode<@NotNull K, @NotNull V> p = b._prev;
		AVLMapNode<@NotNull K, @NotNull V> n = b._next;
		a._prev = p;
		a._next = n;
		if (p != null)
			p._next = a;
		if (n != null)
			n._prev = a;
		return a;
	}

	private void _nodeRemovePrevNext(@NotNull AVLMapNode<@NotNull K, @NotNull V> current) {
		// Speichere 'next' und 'prev'.
		AVLMapNode<@NotNull K, @NotNull V> nodeP = current._prev;
		AVLMapNode<@NotNull K, @NotNull V> nodeN = current._next;
		// Entkopple 'current'
		if (nodeP != null)
			nodeP._next = nodeN;
		if (nodeN != null)
			nodeN._prev = nodeP;
	}

	/**
	 * Aktualisiert {@link node} und liefert, wenn es zur Rotation kommt, eine neue Sub-Wurzel.
	 * 
	 * @param node Der Knoten, der revalidiert werden soll.
	 * 
	 * @return node, oder die neue Sub-Wurzel, wenn es zur Rotation kam.
	 */
	private @NotNull AVLMapNode<@NotNull K, @NotNull V> _nodeRevalidate(
			@NotNull AVLMapNode<@NotNull K, @NotNull V> node) {
		// revalidate balance (check for rotation)
		int heightBalance = _nodeGetHeightBalance(node);

		// right sub-tree has more height
		if (heightBalance > +1) {
			if (node._childR == null)
				throw new NullPointerException(); // kann/sollte nicht passieren.
			if (_nodeGetHeightBalance(node._childR) < 0)
				node._childR = _nodeRotateRight(node._childR);
			return _nodeRotateLeft(node);
		}

		// left sub-tree has more height
		if (heightBalance < -1) {
			if (node._childL == null)
				throw new NullPointerException(); // kann/sollte nicht passieren.
			if (_nodeGetHeightBalance(node._childL) > 0)
				node._childL = _nodeRotateLeft(node._childL);
			return _nodeRotateRight(node);
		}

		_nodeRevalidateHeightAndSize(node);
		return node;
	}

	private @NotNull AVLMapNode<@NotNull K, @NotNull V> _nodeRotateLeft(
			@NotNull AVLMapNode<@NotNull K, @NotNull V> nodeM) {
		if (nodeM._childR == null)
			throw new NullPointerException(); // kann/sollte nicht passieren.
		@NotNull
		AVLMapNode<@NotNull K, @NotNull V> nodeR = nodeM._childR;
		nodeM._childR = nodeR._childL;
		nodeR._childL = nodeM;
		_nodeRevalidateHeightAndSize(nodeM);
		_nodeRevalidateHeightAndSize(nodeR);
		return nodeR;
	}

	private @NotNull AVLMapNode<@NotNull K, @NotNull V> _nodeRotateRight(
			@NotNull AVLMapNode<@NotNull K, @NotNull V> nodeM) {
		if (nodeM._childL == null)
			throw new NullPointerException(); // kann/sollte nicht passieren.
		@NotNull
		AVLMapNode<@NotNull K, @NotNull V> nodeL = nodeM._childL;
		nodeM._childL = nodeL._childR;
		nodeL._childR = nodeM;
		_nodeRevalidateHeightAndSize(nodeM);
		_nodeRevalidateHeightAndSize(nodeL);
		return nodeL;
	}

	private void _nodeRevalidateHeightAndSize(@NotNull AVLMapNode<@NotNull K, @NotNull V> node) {
		// revalidate size
		int sizeL = (node._childL == null) ? 0 : node._childL._size;
		int sizeR = (node._childR == null) ? 0 : node._childR._size;
		node._size = sizeL + sizeR + 1;

		// revalidate height
		int heightL = (node._childL == null) ? 0 : node._childL._height;
		int heightR = (node._childR == null) ? 0 : node._childR._height;
		node._height = Math.max(heightL, heightR) + 1;
	}

	private int _nodeGetHeightBalance(@NotNull AVLMapNode<@NotNull K, @NotNull V> node) {
		int heightL = (node._childL == null) ? 0 : node._childL._height;
		int heightR = (node._childR == null) ? 0 : node._childR._height;
		return heightR - heightL;
	}

}
