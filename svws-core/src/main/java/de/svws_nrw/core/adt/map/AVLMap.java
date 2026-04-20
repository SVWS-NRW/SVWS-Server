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
 * Diese Klasse stellt einen AVL-Baum zur Verfügung, welcher eine Zuordnung von Schlüsseln (Keys) des Typs K zu Werten
 * (Value) vom Typ V unterstützt.
 *
 * @author Benjamin A. Bartsch
 * @author Thomas Bachran
 *
 * @param <K> Der Typ der Schlüssel-Werte.
 * @param <V> Der Typ der zugeordneten Werte.
 */
public final class AVLMap<K, V> implements NavigableMap<K, V> {

	/**
	 * Ein Dummy-Element für den Schlüsselwert "-Unendlich".
	 */
	@SuppressWarnings("unchecked")
	private final @NotNull K infinityMinus = (@NotNull K) AVLMapIntervall._INFINITY_MINUS;

	/**
	 * Ein Dummy-Element für den Schlüsselwert "+Unendlich".
	 */
	@SuppressWarnings("unchecked")
	private final @NotNull K infinityPlus = (@NotNull K) AVLMapIntervall._INFINITY_PLUS;

	/**
	 * Ein Dummy-Element für ein Pseudo-Mapping.
	 */
	@SuppressWarnings("unchecked")
	private final @NotNull V dummyValue = (@NotNull V) new Object();

	/**
	 * Alle Anfragen werden an die Sub-Map delegiert. Diese hat einen Bereich von "-Unendlich" bis "+Unendlich" und
	 * beinhaltet somit alle Elemente.
	 */
	private final @NotNull AVLMapSubMap<K, V> sub = new AVLMapSubMap<>(this,
			new AVLMapIntervall<>(infinityMinus, false, infinityPlus, false), true);

	/**
	 * Der {@link Comparator}, der zum Vergleichen der Schlüsselwerte genutzt wird.
	 */
	private final @NotNull Comparator<K> compK;

	/**
	 * Der {@link Comparator}, der zum Vergleichen der Schlüsselwerte genutzt wird, wenn eine natürliche Ordnung über
	 * das {@link Comparable} - Interface verwendet wird.
	 */
	@SuppressWarnings("unchecked")
	private final @NotNull Comparator<K> compNatural = (final @NotNull K key1, final @NotNull K key2) -> {
		if ((key1 == null) || (key2 == null)) {
			throw new NullPointerException();
		}
		if (!((key1 instanceof Comparable) && (key2 instanceof Comparable))) {
			throw new ClassCastException();
		}
		final @NotNull Comparable<K> k1 = (@NotNull Comparable<K>) key1;
		return k1.compareTo(key2);
	};

	/**
	 * Die Wurzel des Baumes. Bei einem leeren Baum ist diese Referenz NULL.
	 */
	private AVLMapNode<K, V> root = null; // NULL-Wert erlaubt.

	/**
	 * Gibt an, ob das Hinzufügen von KEYs ohne VALUE erlaubt ist. Falls TRUE, dann wird der KEY einer Pseudo-VALUE
	 * zugeordnet.
	 */
	private boolean allowKeyAlone = false;

	/**
	 * Erzeugt einen leere Map, welche bei den Schlüsselwerten die natürliche Ordnung des {@link Comparable} - Interface
	 * nutzt.
	 */
	public AVLMap() {
		compK = compNatural;
	}

	/**
	 * Erstellt eine neue leere Map und nutzt dabei die angegeben Ordnung der Schlüssel.
	 *
	 * @param comparator Die Ordnung für die Schlüssel.
	 */
	public AVLMap(final @NotNull Comparator<K> comparator) {
		compK = comparator;
	}

	/**
	 * Erstellt eine neue Map mit den Daten aus der angegebenen Map und nutzt dabei die Ordnung dieser Map.
	 *
	 * @param map Die Map mit den Daten.
	 */
	@SuppressWarnings("unchecked")
	public AVLMap(final @NotNull SortedMap<K, ? extends V> map) {
		compK = (@NotNull Comparator<K>) map.comparator();
		sub.putAll(map);
	}

	@Override
	public @NotNull String toString() {
		return sub.toString();
	}

	/**
	 * Bewirkt, dass das Hinzufügen von Keys ohne Value durch {@link AVLMapSubKeySet} erlaubt ist. Die Keys werden auf
	 * einen Dummy-Wert gemapped.
	 *
	 * @param b Falls TRUE, dürfen KEYs ohne VALUE hinzugefügt werden.
	 */
	public void allowKeyAlone(final boolean b) {
		allowKeyAlone = b;
	}

	@Override
	public boolean equals(final Object o) {
		return sub.equals(o);
	}

	@Override
	public int hashCode() {
		return sub.hashCode();
	}

	// ################################################
	// ################ NavigableMap ##################
	// ################################################

	@Override
	public @NotNull Comparator<K> comparator() {
		return sub.comparator();
	}

	@Override
	public @NotNull K firstKey() {
		return sub.firstKey();
	}

	@Override
	public @NotNull K lastKey() {
		return sub.lastKey();
	}

	@Override
	public @NotNull Set<K> keySet() {
		return sub.keySet();
	}

	@Override
	public @NotNull Collection<V> values() {
		return sub.values();
	}

	@Override
	public @NotNull Set<Entry<K, V>> entrySet() {
		return sub.entrySet();
	}

	@Override
	public int size() {
		return sub.size();
	}

	@Override
	public boolean isEmpty() {
		return sub.isEmpty();
	}

	@Override
	public boolean containsKey(final @NotNull Object key) {
		return sub.containsKey(key);
	}

	@Override
	public boolean containsValue(final @NotNull Object value) {
		return sub.containsValue(value);
	}

	@Override
	public V get(final @NotNull Object key) { // return NULL erlaubt.
		return sub.get(key);
	}

	@Override
	public V put(final @NotNull K key, final @NotNull V value) { // return NULL erlaubt.
		return sub.put(key, value);
	}

	@Override
	public V remove(final @NotNull Object key) { // return NULL erlaubt.
		return sub.remove(key);
	}

	@Override
	public void putAll(final @NotNull Map<? extends K, ? extends V> m) {
		sub.putAll(m);
	}

	@Override
	public void clear() {
		sub.clear();
	}

	@Override
	public Entry<K, V> lowerEntry(final @NotNull K key) { // return NULL erlaubt.
		return sub.lowerEntry(key);
	}

	@Override
	public K lowerKey(final @NotNull K key) { // return NULL erlaubt.
		return sub.lowerKey(key);
	}

	@Override
	public Entry<K, V> floorEntry(final @NotNull K key) { // return NULL erlaubt.
		return sub.floorEntry(key);
	}

	@Override
	public K floorKey(final @NotNull K key) { // return NULL erlaubt.
		return sub.floorKey(key);
	}

	@Override
	public Entry<K, V> ceilingEntry(final @NotNull K key) { // return NULL erlaubt.
		return sub.ceilingEntry(key);
	}

	@Override
	public K ceilingKey(final @NotNull K key) { // return NULL erlaubt.
		return sub.ceilingKey(key);
	}

	@Override
	public Entry<K, V> higherEntry(final @NotNull K key) { // return NULL erlaubt.
		return sub.higherEntry(key);
	}

	@Override
	public K higherKey(final @NotNull K key) { // return NULL erlaubt.
		return sub.higherKey(key);
	}

	@Override
	public Entry<K, V> firstEntry() { // return NULL erlaubt.
		return sub.firstEntry();
	}

	@Override
	public Entry<K, V> lastEntry() { // return NULL erlaubt.
		return sub.lastEntry();
	}

	@Override
	public Entry<K, V> pollFirstEntry() { // return NULL erlaubt.
		return sub.pollFirstEntry();
	}

	@Override
	public Entry<K, V> pollLastEntry() { // return NULL erlaubt.
		return sub.pollLastEntry();
	}

	@Override
	public @NotNull NavigableMap<K, V> descendingMap() {
		return sub.descendingMap();
	}

	@Override
	public @NotNull NavigableSet<K> navigableKeySet() {
		return sub.navigableKeySet();
	}

	@Override
	public @NotNull NavigableSet<K> descendingKeySet() {
		return sub.descendingKeySet();
	}

	@Override
	public @NotNull NavigableMap<K, V> subMap(final @NotNull K fromKey, final boolean fromInclusive,
			final @NotNull K toKey, final boolean toInclusive) {
		return sub.subMap(fromKey, fromInclusive, toKey, toInclusive);
	}

	@Override
	public @NotNull NavigableMap<K, V> headMap(final @NotNull K toKey, final boolean inclusive) {
		return sub.headMap(toKey, inclusive);
	}

	@Override
	public @NotNull NavigableMap<K, V> tailMap(final @NotNull K fromKey, final boolean inclusive) {
		return sub.tailMap(fromKey, inclusive);
	}

	@Override
	public @NotNull SortedMap<K, V> subMap(final @NotNull K fromKey, final @NotNull K toKey) {
		return sub.subMap(fromKey, toKey);
	}

	@Override
	public @NotNull SortedMap<K, V> headMap(final @NotNull K toKey) {
		return sub.headMap(toKey);
	}

	@Override
	public @NotNull SortedMap<K, V> tailMap(final @NotNull K fromKey) {
		return sub.tailMap(fromKey);
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
	boolean bcAddEntryReturnBool(final @NotNull Entry<K, V> e, final @NotNull AVLMapIntervall<K> iv) {
		final V old = bcAddEntryReturnOldValueOrNull(e.getKey(), e.getValue(), iv);
		return !valEquals(old, e.getValue());
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
	V bcAddEntryReturnOldValueOrNull(final @NotNull K key, final @NotNull V value, final @NotNull AVLMapIntervall<K> iv) {
		if (key == null) { // Sonderfall: NULL-Key
			throw new NullPointerException("TreeMap erlaubt keine NULL keys.");
		}
		if (isOutOfRange(key, iv)) { // Sonderfall: Bereich
			throw new IllegalArgumentException("Der Schlüsselwert liegt nicht im gültigen Bereich.");
		}
		if (root == null) { // Sonderfall: Baum leer
			root = new AVLMapNode<>(key, value);
			return null;
		}
		// Alten Wert (Value) sichern.
		final AVLMapNode<K, V> node = nodeGetOrNull(key, iv);
		final V old = (node == null) ? null : node._val;
		// Entry einfügen.
		root = nodePutRecursive(root, key, value);
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
	boolean bcAddAllEntries(final @NotNull Collection<? extends Entry<K, V>> c, final @NotNull AVLMapIntervall<K> iv) {
		boolean changed = false;
		for (final @NotNull Entry<K, V> entry : c) {
			changed |= bcAddEntryReturnBool(entry, iv);
		}
		return changed;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Fügt alle Entrys der übergebenen Map dieser Datenstruktur hinzu.
	 *
	 * @param map Die Map, deren Entries dieser Datenstruktur hinzugefügt werden soll.
	 * @param iv  Das Intervall der {@link AVLMapSubMap}.
	 */
	void bcAddAllEntriesOfMap(final @NotNull Map<? extends K, ? extends V> map, final @NotNull AVLMapIntervall<K> iv) {
		for (final @NotNull Entry<? extends K, ? extends V> entry : map.entrySet()) {
			bcAddEntryReturnOldValueOrNull(entry.getKey(), entry.getValue(), iv);
		}
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Bei dem Versuch einen Schlüssel (Key) ohne Wert (Value) hinzuzufügen,
	 * kann es zu einer {@link UnsupportedOperationException} kommen, wenn das Attribut {@link #allowKeyAlone} auf
	 * FALSE gesetzt ist. Andernfalls wird dem Schlüssel (Key) ein Dummy-Wert {@link #dummyValue} zugeordnet. Der
	 * Schlüssel (Key) wird jedoch nur dann hinzugefügt, falls er noch nicht existierte.
	 *
	 * @param e  Der Schlüssel (Key) der hinzugefügt werden soll.
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 *
	 * @return TRUE, falls der Schlüssel (Key) noch nicht existierte und somit hinzugefügt wurde.
	 * @throws UnsupportedOperationException wenn ein alleiniges Hinzufügen eines Schlüssels nicht erlaubt ist.
	 */
	boolean bcAddKey(final @NotNull K e, final @NotNull AVLMapIntervall<K> iv) {
		if (!allowKeyAlone) {
			throw new UnsupportedOperationException(); // KEY kann nicht ohne VALUE hinzugefügt werden.
		}
		if (bcContainsKey(e, iv)) {
			return false;
		}
		bcAddEntryReturnOldValueOrNull(e, dummyValue, iv);
		return true;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Versucht alle Schlüssel (Keys) der Collection hinzuzufügen. Ob das
	 * Hinzufügen eines Schlüssels (Key) ohne Wert (Value) erlaubt ist, hängt vom Attribut {@link #allowKeyAlone} ab.
	 *
	 * @param c  Die Collection mit allen Schlüsseln (Keys) die hinzugefügt werden sollen.
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 *
	 * @return TRUE, falls mindestens ein Schlüssel (Key) noch nicht existierte und somit hinzugefügt wurde.
	 * @throws UnsupportedOperationException wenn ein alleiniges Hinzufügen eines Schlüssels nicht erlaubt ist.
	 */
	boolean bcAddAllKeys(final @NotNull Collection<? extends K> c, final @NotNull AVLMapIntervall<K> iv) {
		boolean changed = false;
		for (final K key : c) {
			changed |= bcAddKey(key, iv);
		}
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
	boolean bcContainsKey(final @NotNull Object objKey, final @NotNull AVLMapIntervall<K> iv) {
		return nodeGetOrNull((@NotNull K) objKey, iv) != null;
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
	boolean bcContainsAllKeys(final @NotNull Collection<?> c, final @NotNull AVLMapIntervall<K> iv) {
		for (final @NotNull Object key : c) {
			if (!bcContainsKey(key, iv)) {
				return false;
			}
		}
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
	@SuppressWarnings("unchecked")
	boolean bcContainsValue(final @NotNull Object objValue, final @NotNull AVLMapIntervall<K> iv) {
		final @NotNull V value = (@NotNull V) objValue;
		AVLMapNode<K, V> n1 = nodeFirstOrNull(iv);
		if (n1 == null) {
			return false;
		}
		final AVLMapNode<K, V> n2 = nodeLastOrNull(iv);
		if (n2 == null) {
			return false; // kann nicht passieren.
		}
		while (n1 != n2) {
			if (n1 == null) {
				throw new NullPointerException(); // kann/sollte nicht passieren.
			}
			if (valEquals(n1._val, value)) {
				return true;
			}
			n1 = n1._next; // iv-Check nicht nötig.
		}
		return valEquals(n2._val, value);
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
	boolean bcContainsAllValues(final @NotNull Collection<?> c, final @NotNull AVLMapIntervall<K> iv) {
		for (final @NotNull Object val : c) {
			if (!bcContainsValue(val, iv)) {
				return false;
			}
		}
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
	boolean bcContainsEntry(final @NotNull Object o, final @NotNull AVLMapIntervall<K> iv) {
		if (!(o instanceof Entry)) {
			return false;
		}
		final @NotNull Entry<K, V> e = (@NotNull Entry<K, V>) o;
		final AVLMapNode<K, V> node = nodeGetOrNull(e.getKey(), iv);
		if (node == null) {
			return false;
		}
		return valEquals(node._val, e.getValue());
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
	boolean bcContainsAllEntries(final @NotNull Collection<?> c, final @NotNull AVLMapIntervall<K> iv) {
		for (final @NotNull Object entry : c) {
			if (!bcContainsEntry(entry, iv)) {
				return false;
			}
		}
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
	V bcRemoveKeyReturnOldValueOrNull(final @NotNull Object obj, final @NotNull AVLMapIntervall<K> iv) {
		if (obj == null) { // Sonderfall: NULL-Key
			throw new NullPointerException("TreeMap unterstützt keine NULL-Schlüssel.");
		}
		final @NotNull K key = (@NotNull K) obj;
		// Alten Wert (Value) sichern.
		final AVLMapNode<K, V> old = nodeGetOrNull(key, iv);
		if (old == null) {
			return null;
		}
		if (root == null) {
			throw new NullPointerException(); // Transpiler-Hilfe
		}
		// Schlüssel (Key) löschen.
		root = nodeRemoveKeyRecursive(root, key);
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
	boolean bcRemoveKeyReturnBool(final @NotNull Object o, final @NotNull AVLMapIntervall<K> iv) {
		if (!bcContainsKey(o, iv)) {
			return false; // keine Exception (anders als in der JAVA-TreeMap-Implementation)
		}
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
	boolean bcRemoveAllKeys(final @NotNull Collection<?> c, final @NotNull AVLMapIntervall<K> iv) {
		boolean changed = false;
		for (final @NotNull Object obj : c) {
			changed |= bcRemoveKeyReturnBool(obj, iv);
		}
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
	boolean bcRemoveEntry(final @NotNull Object o, final @NotNull AVLMapIntervall<K> iv) {
		if (!(o instanceof Entry)) {
			return false;
		}
		if (!bcContainsEntry(o, iv)) {
			return false;
		}
		if (root == null) {
			throw new NullPointerException(); // Transpiler-Hilfe
		}
		final @NotNull Entry<K, V> e = (@NotNull Entry<K, V>) o;
		root = nodeRemoveKeyRecursive(root, e.getKey());
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
	boolean bcRemoveAllEntries(final @NotNull Collection<?> c, final @NotNull AVLMapIntervall<K> iv) {
		boolean removedAny = false;
		for (final @NotNull Object entry : c) {
			removedAny |= bcRemoveEntry(entry, iv);
		}
		return removedAny;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Entfernt und liefert das erste Entry dieser Datenstruktur.
	 *
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 *
	 * @return Entfernt und liefert das erste Entry dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	Entry<K, V> bcPollFirstEntryOrNull(final @NotNull AVLMapIntervall<K> iv) {
		final AVLMapNode<K, V> node = nodeFirstOrNull(iv);
		if (node == null) {
			return null;
		}
		if (root == null) {
			throw new NullPointerException(); // Transpiler-Hilfe
		}
		root = nodeRemoveKeyRecursive(root, node._key);
		return node;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Entfernt und liefert den ersten Schlüssel (Key) dieser Datenstruktur.
	 *
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 *
	 * @return Entfernt und liefert den ersten Schlüssel (Key) dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	K bcPollFirstKeyOrNull(final @NotNull AVLMapIntervall<K> iv) {
		final AVLMapNode<K, V> node = nodeFirstOrNull(iv);
		if (node == null) {
			return null;
		}
		if (root == null) {
			throw new NullPointerException(); // kann/sollte nicht passieren.
		}
		root = nodeRemoveKeyRecursive(root, node._key);
		return node._key;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Entfernt und liefert das letzte Entry dieser Datenstruktur.
	 *
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 *
	 * @return Entfernt und liefert das letzte Entry dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	Entry<K, V> bcPollLastEntryOrNull(final @NotNull AVLMapIntervall<K> iv) {
		final AVLMapNode<K, V> node = nodeLastOrNull(iv);
		if (node == null) {
			return null;
		}
		if (root == null) {
			throw new NullPointerException(); // kann/sollte nicht passieren.
		}
		root = nodeRemoveKeyRecursive(root, node._key);
		return node;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Entfernt und liefert den letzten Schlüssel (Key) dieser Datenstruktur.
	 *
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 *
	 * @return Entfernt und liefert den letzten Schlüssel (Key) dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	K bcPollLastKeyOrNull(final @NotNull AVLMapIntervall<K> iv) {
		final AVLMapNode<K, V> node = nodeLastOrNull(iv);
		if (node == null) {
			return null;
		}
		if (root == null) {
			throw new NullPointerException(); // kann/sollte nicht passieren.
		}
		root = nodeRemoveKeyRecursive(root, node._key);
		return node._key;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Liefert die Anzahl der Elemente innerhalb des übergebenen Intervalls.
	 *
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 *
	 * @return Liefert die Anzahl der Elemente innerhalb des übergebenen Intervalls.
	 */
	int bcGetSize(final @NotNull AVLMapIntervall<K> iv) {
		final AVLMapNode<K, V> n1 = nodeFirstOrNull(iv);
		if (n1 == null) {
			return 0;
		}
		final AVLMapNode<K, V> n2 = nodeLastOrNull(iv);
		if (n2 == null) {
			return 0; // Transpiler-Hilfe
		}
		return (nodeIndexOf(n2._key) - nodeIndexOf(n1._key)) + 1;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Überprüft, ob die Datenstruktur innerhalb des Intervalls leer ist.
	 *
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 *
	 * @return TRUE, falls die Datenstruktur innerhalb des Intervalls leer ist.
	 */
	boolean bcIsEmpty(final @NotNull AVLMapIntervall<K> iv) {
		// Hinweis: Mann kann nicht testen, ob die Wurzel-Referenz NULL ist,
		// da sich die Anfrage auf das Intervall "iv" bezieht.
		return nodeFirstOrNull(iv) == null;
	}

	/**
	 * Liefert den Comparator dieser Map.
	 *
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 *
	 * @return Liefert den Comparator dieser Map.
	 */
	@NotNull
	Comparator<K> bcGetComparator(final @NotNull AVLMapIntervall<K> iv) {
		return compK;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Liefert das erste Entry dieser Datenstruktur.
	 *
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 *
	 * @return Das erste Entry dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	AVLMapNode<K, V> bcGetFirstEntryOrNull(final @NotNull AVLMapIntervall<K> iv) {
		return nodeFirstOrNull(iv);
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
	K bcGetFirstKeyOrException(final @NotNull AVLMapIntervall<K> iv) {
		return keyOrExeption(nodeFirstOrNull(iv));
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Liefert das letzte Entry dieser Datenstruktur.
	 *
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 *
	 * @return Liefert das letzte Entry dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	AVLMapNode<K, V> bcGetLastEntryOrNull(final @NotNull AVLMapIntervall<K> iv) {
		return nodeLastOrNull(iv);
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
	K bcGetLastKeyOrException(final @NotNull AVLMapIntervall<K> iv) {
		return keyOrExeption(nodeLastOrNull(iv));
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Liefert das nächste Entry dieser Datenstruktur.
	 *
	 * @param current Das aktuelle Entry .
	 * @param iv      Das Intervall der {@link AVLMapSubMap}.
	 *
	 * @return Liefert das nächste Entry dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	AVLMapNode<K, V> bcGetNextEntryOrNull(final @NotNull AVLMapNode<K, V> current, final @NotNull AVLMapIntervall<K> iv) {
		return nodeNextOrNull(current, iv);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Liefert das vorherige Entry dieser Datenstruktur.
	 *
	 * @param current Das aktuelle Entry.
	 * @param iv      Das Intervall der {@link AVLMapSubMap}.
	 *
	 * @return Liefert das vorherige Entry dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	AVLMapNode<K, V> bcGetPrevEntryOrNull(final @NotNull AVLMapNode<K, V> current, final @NotNull AVLMapIntervall<K> iv) {
		return nodePrevOrNull(current, iv);
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
	V bcGetValueOfKeyOrNull(final @NotNull Object objKey, final @NotNull AVLMapIntervall<K> iv) {
		final @NotNull K key = (@NotNull K) objKey;
		final AVLMapNode<K, V> node = nodeGetOrNull(key, iv);
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
	AVLMapNode<K, V> bcGetLowerEntryOrNull(final @NotNull K key, final @NotNull AVLMapIntervall<K> iv) {
		return nodeLowerOrNull(key, iv);
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
	K bcGetLowerKeyOrNull(final @NotNull K key, final @NotNull AVLMapIntervall<K> iv) {
		return keyOrNull(nodeLowerOrNull(key, iv));
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
	AVLMapNode<K, V> bcGetFloorEntryOrNull(final @NotNull K key, final @NotNull AVLMapIntervall<K> iv) {
		return nodeFloorOrNull(key, iv);
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
	K bcGetFloorKeyOrNull(final @NotNull K key, final @NotNull AVLMapIntervall<K> iv) {
		return keyOrNull(nodeFloorOrNull(key, iv));
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
	AVLMapNode<K, V> bcGetCeilingEntryOrNull(final @NotNull K key, final @NotNull AVLMapIntervall<K> iv) {
		return nodeCeilingOrNull(key, iv);
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
	K bcGetCeilingKeyOrNull(final @NotNull K key, final @NotNull AVLMapIntervall<K> iv) {
		return keyOrNull(nodeCeilingOrNull(key, iv));
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
	AVLMapNode<K, V> bcGetHigherEntryOrNull(final @NotNull K key, final @NotNull AVLMapIntervall<K> iv) {
		return nodeHigherOrNull(key, iv);
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
	K bcGetHigherKeyOrNull(final @NotNull K key, final @NotNull AVLMapIntervall<K> iv) {
		return keyOrNull(nodeHigherOrNull(key, iv));
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
	boolean bcCheckOutOfIntervall(final @NotNull K key, final boolean inc, final @NotNull AVLMapIntervall<K> iv) {
		if ((key == infinityMinus) || (key == infinityPlus)) {
			return false;
		}

		final int cmpF = compare(key, iv.from);
		// Fall: Links von "from" --> außerhalb
		if (cmpF < 0) {
			return true;
		}
		// Fall: Gleich "from", aber Intervall nicht inklusive und Schlüssel inklusive --> außerhalb
		if ((cmpF == 0) && (!iv.fromInc) && (inc)) {
			return true;
		}

		final int cmpT = compare(key, iv.to);
		// Fall: Rechts von "to" --> außerhalb
		if (cmpT > 0) {
			return true;
		}
		// Fall: Gleich "to", aber Intervall nicht inklusive und Schlüssel inklusive --> außerhalb
		return ((cmpT == 0) && (!iv.toInc) && (inc));
	}

	// ##########################################################
	// ################# PRIVATE (intern calls) #################
	// ##########################################################

	private K keyOrNull(final AVLMapNode<K, V> node) {
		return (node == null) ? null : node._key;
	}

	private boolean valEquals(final V v1, final V v2) {
		return (v1 == null) ? (v2 == null) : v1.equals(v2);
	}

	private @NotNull K keyOrExeption(final AVLMapNode<K, V> node) {
		if (node == null) {
			throw new NoSuchElementException();
		}
		return node._key;
	}

	private int compare(final @NotNull K key1, final @NotNull K key2) {
		if ((key1 == infinityMinus) || (key2 == infinityPlus)) {
			return -1;
		}
		if ((key1 == infinityPlus) || (key2 == infinityMinus)) {
			return +1;
		}
		return compK.compare(key1, key2);
	}

	private boolean isOutOfRange(final @NotNull K key, final @NotNull AVLMapIntervall<K> iv) {
		final int cmpKeyFrom = compare(key, iv.from);
		if ((cmpKeyFrom < 0) || ((cmpKeyFrom == 0) && (!iv.fromInc))) {
			return true;
		}
		final int cmpKeyTo = compare(key, iv.to);
		return ((cmpKeyTo > 0) || ((cmpKeyTo == 0) && (!iv.toInc)));
	}

	private AVLMapNode<K, V> nodeFirstOrNull(final @NotNull AVLMapIntervall<K> iv) {
		return iv.fromInc ? nodeCeilingOrNull(iv.from, iv) : nodeHigherOrNull(iv.from, iv);
	}

	private AVLMapNode<K, V> nodeLastOrNull(final @NotNull AVLMapIntervall<K> iv) {
		return iv.toInc ? nodeFloorOrNull(iv.to, iv) : nodeLowerOrNull(iv.to, iv);
	}

	private AVLMapNode<K, V> nodeCeilingOrNull(final @NotNull K key, final @NotNull AVLMapIntervall<K> iv) {
		final AVLMapNode<K, V> node = nodeDeepestOrNull(key, iv);
		if (node == null) {
			return null;
		}
		final int cmpNodeKey = compare(node._key, key);
		return (cmpNodeKey >= 0) ? node : nodeNextOrNull(node, iv);
	}

	private AVLMapNode<K, V> nodeHigherOrNull(final @NotNull K key, final @NotNull AVLMapIntervall<K> iv) {
		final AVLMapNode<K, V> node = nodeDeepestOrNull(key, iv);
		if (node == null) {
			return null;
		}
		final int cmpNodeKey = compare(node._key, key);
		return (cmpNodeKey > 0) ? node : nodeNextOrNull(node, iv);
	}

	private AVLMapNode<K, V> nodeFloorOrNull(final @NotNull K key, final @NotNull AVLMapIntervall<K> iv) {
		final AVLMapNode<K, V> node = nodeDeepestOrNull(key, iv);
		if (node == null) {
			return null;
		}
		final int cmpNodeKey = compare(node._key, key);
		return (cmpNodeKey <= 0) ? node : nodePrevOrNull(node, iv);
	}

	private AVLMapNode<K, V> nodeLowerOrNull(final @NotNull K key, final @NotNull AVLMapIntervall<K> iv) {
		final AVLMapNode<K, V> node = nodeDeepestOrNull(key, iv);
		if (node == null) {
			return null;
		}
		final int cmpNodeKey = compare(node._key, key);
		return (cmpNodeKey < 0) ? node : nodePrevOrNull(node, iv);
	}

	private AVLMapNode<K, V> nodeNextOrNull(final @NotNull AVLMapNode<K, V> node, final @NotNull AVLMapIntervall<K> iv) {
		final AVLMapNode<K, V> next = node._next;
		if (next == null) {
			return null;
		}
		return isOutOfRange(next._key, iv) ? null : next;
	}

	private AVLMapNode<K, V> nodePrevOrNull(final @NotNull AVLMapNode<K, V> node, final @NotNull AVLMapIntervall<K> iv) {
		final AVLMapNode<K, V> prev = node._prev;
		if (prev == null) {
			return null;
		}
		return isOutOfRange(prev._key, iv) ? null : prev;
	}

	private AVLMapNode<K, V> nodeGetOrNull(final @NotNull K key, final @NotNull AVLMapIntervall<K> iv) {
		final AVLMapNode<K, V> node = nodeDeepestOrNull(key, iv);
		if (node == null) {
			return null;
		}
		return (compare(key, node._key) == 0) ? node : null;
	}

	// Der KEY muss im Baum existieren!
	private int nodeIndexOf(final @NotNull K key) {
		AVLMapNode<K, V> current = root;

		int index = 0;
		while (true) {
			if (current == null) {
				throw new NullPointerException(); // kann/sollte nicht passieren.
			}
			final int cmp = compare(key, current._key);
			if (cmp < 0) {
				current = current._childL;
				continue;
			}
			final AVLMapNode<K, V> left = current._childL;
			final int sizeL = (left == null) ? 0 : left._size;
			if (cmp > 0) {
				index += sizeL + 1;
				current = current._childR;
				continue;
			}
			return index + sizeL;
		}
	}

	private AVLMapNode<K, V> nodeDeepestOrNull(final @NotNull K key, final @NotNull AVLMapIntervall<K> iv) {
		AVLMapNode<K, V> current = root;
		AVLMapNode<K, V> last = null; // Letztes gültiges gefundenes Element im Intervall.

		while (current != null) {
			// Fall: Gültiger Bereich liegt links
			final int cmpToKey = compare(iv.to, current._key);
			if ((cmpToKey < 0) || ((cmpToKey == 0) && (!iv.toInc))) {
				current = current._childL;
				continue;
			}

			// Fall: Gültiger Bereich liegt rechts
			final int cmpFromKey = compare(iv.from, current._key);
			if ((cmpFromKey > 0) || ((cmpFromKey == 0) && (!iv.fromInc))) {
				current = current._childR;
				continue;
			}

			last = current;
			final int cmp = compare(key, current._key);

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

	private @NotNull AVLMapNode<K, V> nodePutRecursive(final @NotNull AVLMapNode<K, V> current, final @NotNull K key, final @NotNull V value) {
		final int cmp = compare(key, current._key);

		if (cmp == 0) { // Key gefunden --> Value ersetzen
			current._val = value;
			return current;
		}

		if (cmp < 0) { // links (einfügen oder weitersuchen)
			current._childL = (current._childL == null) ? nodeCreateLeaf(current._prev, current, key, value)
			: nodePutRecursive(current._childL, key, value);
		} else { // rechts (einfügen oder weitersuchen)
			current._childR = (current._childR == null) ? nodeCreateLeaf(current, current._next, key, value)
			: nodePutRecursive(current._childR, key, value);
		}

		return nodeRevalidate(current); // ggf. rotieren?
	}

	private @NotNull AVLMapNode<K, V> nodeCreateLeaf(final AVLMapNode<K, V> prev, final AVLMapNode<K, V> next, final @NotNull K key, final @NotNull V value) {
		final AVLMapNode<K, V> child = new AVLMapNode<>(key, value);
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
	private AVLMapNode<K, V> nodeRemoveKeyRecursive(final @NotNull AVLMapNode<K, V> current, final @NotNull K key) {
		final int cmp = compare(key, current._key);
		// Fall: Links weitersuchen
		if (cmp < 0) {
			if (current._childL == null) {
				throw new NullPointerException(); // kann/sollte nicht passieren.
			}
			current._childL = nodeRemoveKeyRecursive(current._childL, key);
			return nodeRevalidate(current);
		}
		// Fall: Rechts weitersuchen
		if (cmp > 0) {
			if (current._childR == null) {
				throw new NullPointerException(); // kann/sollte nicht passieren.
			}
			current._childR = nodeRemoveKeyRecursive(current._childR, key);
			return nodeRevalidate(current);
		}
		// Fall (cmp == 0): Direkt löschen (rechtes Kind hochziehen)
		if (current._childL == null) {
			nodeRemovePrevNext(current);
			return current._childR; // _revalidateNode nicht nötig
		}
		// Fall (cmp == 0): Direkt löschen (linkes Kind hochziehen)
		if (current._childR == null) {
			nodeRemovePrevNext(current);
			return current._childL; // _revalidateNode nicht nötig
		}
		// Fall (cmp == 0): Lösche 'next', ersetze dann 'current' durch 'next'.
		final AVLMapNode<K, V> next = current._next;
		if (next == null) {
			throw new NullPointerException(); // kann/sollte nicht passieren.
		}
		current._childR = nodeRemoveKeyRecursive(current._childR, next._key);
		return nodeRevalidate(nodeReplaceReferencesFromAwithB(next, current));
	}

	private @NotNull AVLMapNode<K, V> nodeReplaceReferencesFromAwithB(final @NotNull AVLMapNode<K, V> a, final @NotNull AVLMapNode<K, V> b) {
		a._childL = b._childL;
		a._childR = b._childR;
		final AVLMapNode<K, V> p = b._prev;
		final AVLMapNode<K, V> n = b._next;
		a._prev = p;
		a._next = n;
		if (p != null) {
			p._next = a;
		}
		if (n != null) {
			n._prev = a;
		}
		return a;
	}

	private void nodeRemovePrevNext(final @NotNull AVLMapNode<K, V> current) {
		// Speichere 'next' und 'prev'.
		final AVLMapNode<K, V> nodeP = current._prev;
		final AVLMapNode<K, V> nodeN = current._next;
		// Entkopple 'current'
		if (nodeP != null) {
			nodeP._next = nodeN;
		}
		if (nodeN != null) {
			nodeN._prev = nodeP;
		}
	}

	/**
	 * Aktualisiert {@link node} und liefert, wenn es zur Rotation kommt, eine neue Sub-Wurzel.
	 *
	 * @param node Der Knoten, der revalidiert werden soll.
	 *
	 * @return node, oder die neue Sub-Wurzel, wenn es zur Rotation kam.
	 */
	private @NotNull AVLMapNode<K, V> nodeRevalidate(final @NotNull AVLMapNode<K, V> node) {
		// revalidate balance (check for rotation)
		final int heightBalance = nodeGetHeightBalance(node);

		// right sub-tree has more height
		if (heightBalance > +1) {
			if (node._childR == null) {
				throw new NullPointerException(); // kann/sollte nicht passieren.
			}
			if (nodeGetHeightBalance(node._childR) < 0) {
				node._childR = nodeRotateRight(node._childR);
			}
			return nodeRotateLeft(node);
		}

		// left sub-tree has more height
		if (heightBalance < -1) {
			if (node._childL == null) {
				throw new NullPointerException(); // kann/sollte nicht passieren.
			}
			if (nodeGetHeightBalance(node._childL) > 0) {
				node._childL = nodeRotateLeft(node._childL);
			}
			return nodeRotateRight(node);
		}

		nodeRevalidateHeightAndSize(node);
		return node;
	}

	private @NotNull AVLMapNode<K, V> nodeRotateLeft(final @NotNull AVLMapNode<K, V> nodeM) {
		if (nodeM._childR == null) {
			throw new NullPointerException(); // kann/sollte nicht passieren.
		}
		final @NotNull AVLMapNode<K, V> nodeR = nodeM._childR;
		nodeM._childR = nodeR._childL;
		nodeR._childL = nodeM;
		nodeRevalidateHeightAndSize(nodeM);
		nodeRevalidateHeightAndSize(nodeR);
		return nodeR;
	}

	private @NotNull AVLMapNode<K, V> nodeRotateRight(final @NotNull AVLMapNode<K, V> nodeM) {
		if (nodeM._childL == null) {
			throw new NullPointerException(); // kann/sollte nicht passieren.
		}
		final @NotNull AVLMapNode<K, V> nodeL = nodeM._childL;
		nodeM._childL = nodeL._childR;
		nodeL._childR = nodeM;
		nodeRevalidateHeightAndSize(nodeM);
		nodeRevalidateHeightAndSize(nodeL);
		return nodeL;
	}

	private void nodeRevalidateHeightAndSize(final @NotNull AVLMapNode<K, V> node) {
		// revalidate size
		final int sizeL = (node._childL == null) ? 0 : node._childL._size;
		final int sizeR = (node._childR == null) ? 0 : node._childR._size;
		node._size = sizeL + sizeR + 1;

		// revalidate height
		final int heightL = (node._childL == null) ? 0 : node._childL._height;
		final int heightR = (node._childR == null) ? 0 : node._childR._height;
		node._height = Math.max(heightL, heightR) + 1;
	}

	private int nodeGetHeightBalance(final @NotNull AVLMapNode<K, V> node) {
		final int heightL = (node._childL == null) ? 0 : node._childL._height;
		final int heightR = (node._childR == null) ? 0 : node._childR._height;
		return heightR - heightL;
	}

}
