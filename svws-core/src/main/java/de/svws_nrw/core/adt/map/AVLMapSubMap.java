package de.svws_nrw.core.adt.map;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.ArrayList;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse implementiert eine Sub-Map für einen AVL-Baum der Klasse {@link AVLMap}. Fast alle Methodenaufrufe
 * werden zusammen mit dem {@link AVLMapIntervall} dieser {@link AVLMapSubMap} an die {@link AVLMap} delegiert.
 *
 * @author Benjamin A. Bartsch
 * @author Thomas Bachran
 *
 * @param <K> Der Typ der Schlüssel-Werte.
 * @param <V> Der Typ der zugeordneten Werte.
 */
public final class AVLMapSubMap<K, V> implements NavigableMap<K, V> {

	/**
	 * Die {@link AVLMap} auf der diese Sup-Map operiert.
	 */
	private final @NotNull AVLMap<K, V> parent;

	/**
	 * Das {@link AVLMapIntervall} auf das sich diese Sub-Map bezieht.
	 */
	private final @NotNull AVLMapIntervall<K> intervall;

	/**
	 * Falls TRUE wird die {@link AVLMap} aufsteigend, andernfalls absteigend interpretiert.
	 */
	private final boolean ascending;

	/**
	 * Erstellt eine neue Sub-Map relativ zur übergebenen {@link AVLMap}.
	 *
	 * @param parent    Die {@link AVLMap} auf der diese Sup-Map operiert.
	 * @param intervall Das {@link AVLMapIntervall} auf das sich diese Sub-Map bezieht.
	 * @param asc       Falls TRUE wird die {@link AVLMap} aufsteigend, andernfalls absteigend interpretiert.
	 */
	AVLMapSubMap(final @NotNull AVLMap<K, V> parent, final @NotNull AVLMapIntervall<K> intervall,
			final boolean asc) {
		this.parent = parent;
		this.intervall = intervall;
		this.ascending = asc;
	}

	// ########################################################################
	// ############################# PUBLIC ###################################
	// ########################################################################

	@Override
	public @NotNull String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Entries = [");
		boolean first = true;
		for (final Entry<K, V> e : entrySet()) {
			if (first) {
				first = false;
			} else {
				sb.append(", ");
			}
			sb.append(e);
		}
		sb.append("], iv = ");
		sb.append(intervall);
		sb.append(", asc = ");
		sb.append(ascending);
		return sb.toString();
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null) {
			return false;
		}
		if (o == this) {
			return true;
		}
		if (!(o instanceof Map<?, ?>)) {
			return false;
		}
		final Map<?, ?> mapO = (Map<?, ?>) o;
		if (mapO.size() != size()) {
			return false;
		}
		// Da SIZE identisch ist, reicht es die KEYS in dieser Map
		// mit dem Mapping in mapO zu überprüfen.
		for (final @NotNull Entry<K, V> e : entrySet()) {
			if (!e.getValue().equals(mapO.get(e.getKey()))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() { // code adapted TreeMap
		int h = 0;
		for (final Entry<K, V> entry : entrySet()) {
			h += entry.hashCode();
		}
		return h;
	}

	@Override
	public @NotNull Comparator<K> comparator() {
		return parent.bcGetComparator(intervall);
	}

	@Override
	public @NotNull K firstKey() {
		return ascending ? parent.bcGetFirstKeyOrException(intervall) : parent.bcGetLastKeyOrException(intervall);
	}

	@Override
	public @NotNull K lastKey() {
		return ascending ? parent.bcGetLastKeyOrException(intervall) : parent.bcGetFirstKeyOrException(intervall);
	}

	@Override
	public @NotNull Set<K> keySet() {
		return new AVLMapSubKeySet<>(this);
	}

	@Override
	public @NotNull Collection<V> values() {
		return new AVLMapSubCollection<>(this);
	}

	@Override
	public @NotNull Set<Entry<K, V>> entrySet() {
		return new AVLMapSubEntrySet<>(this);
	}

	@Override
	public int size() {
		return parent.bcGetSize(intervall);
	}

	@Override
	public boolean isEmpty() {
		return parent.bcIsEmpty(intervall);
	}

	@Override
	public boolean containsKey(final @NotNull Object key) {
		return parent.bcContainsKey(key, intervall);
	}

	@Override
	public boolean containsValue(final @NotNull Object value) {
		return parent.bcContainsValue(value, intervall);
	}

	@Override
	public V get(final @NotNull Object key) { // return NULL erlaubt.
		return parent.bcGetValueOfKeyOrNull(key, intervall);
	}

	@Override
	public V put(final @NotNull K key, final @NotNull V value) { // return NULL erlaubt.
		return parent.bcAddEntryReturnOldValueOrNull(key, value, intervall);
	}

	@Override
	public V remove(final @NotNull Object key) { // return NULL erlaubt.
		return parent.bcRemoveKeyReturnOldValueOrNull(key, intervall);
	}

	@Override
	public void putAll(final @NotNull Map<? extends K, ? extends V> map) {
		parent.bcAddAllEntriesOfMap(map, intervall);
	}

	@Override
	public void clear() {
		final Iterator<Entry<K, V>> iter = this.bcGetSubEntrySetIterator();
		while (iter.hasNext()) {
			iter.next();
			iter.remove();
		}
	}

	@Override
	public Entry<K, V> lowerEntry(final @NotNull K key) { // return NULL erlaubt.
		return ascending ? parent.bcGetLowerEntryOrNull(key, intervall) : parent.bcGetHigherEntryOrNull(key, intervall);
	}

	@Override
	public K lowerKey(final @NotNull K key) { // return NULL erlaubt.
		return ascending ? parent.bcGetLowerKeyOrNull(key, intervall) : parent.bcGetHigherKeyOrNull(key, intervall);
	}

	@Override
	public Entry<K, V> floorEntry(final @NotNull K key) { // return NULL erlaubt.
		return ascending ? parent.bcGetFloorEntryOrNull(key, intervall) : parent.bcGetCeilingEntryOrNull(key, intervall);
	}

	@Override
	public K floorKey(final @NotNull K key) { // return NULL erlaubt.
		return ascending ? parent.bcGetFloorKeyOrNull(key, intervall) : parent.bcGetCeilingKeyOrNull(key, intervall);
	}

	@Override
	public Entry<K, V> ceilingEntry(final @NotNull K key) { // return NULL erlaubt.
		return ascending ? parent.bcGetCeilingEntryOrNull(key, intervall) : parent.bcGetFloorEntryOrNull(key, intervall);
	}

	@Override
	public K ceilingKey(final @NotNull K key) { // return NULL erlaubt.
		return ascending ? parent.bcGetCeilingKeyOrNull(key, intervall) : parent.bcGetFloorKeyOrNull(key, intervall);
	}

	@Override
	public Entry<K, V> higherEntry(final @NotNull K key) { // return NULL erlaubt.
		return ascending ? parent.bcGetHigherEntryOrNull(key, intervall) : parent.bcGetLowerEntryOrNull(key, intervall);
	}

	@Override
	public K higherKey(final @NotNull K key) { // return NULL erlaubt.
		return ascending ? parent.bcGetHigherKeyOrNull(key, intervall) : parent.bcGetLowerKeyOrNull(key, intervall);
	}

	@Override
	public Entry<K, V> firstEntry() { // return NULL erlaubt.
		return ascending ? parent.bcGetFirstEntryOrNull(intervall) : parent.bcGetLastEntryOrNull(intervall);
	}

	@Override
	public Entry<K, V> lastEntry() { // return NULL erlaubt.
		return ascending ? parent.bcGetLastEntryOrNull(intervall) : parent.bcGetFirstEntryOrNull(intervall);
	}

	@Override
	public Entry<K, V> pollFirstEntry() { // return NULL erlaubt.
		return ascending ? parent.bcPollFirstEntryOrNull(intervall) : parent.bcPollLastEntryOrNull(intervall);
	}

	@Override
	public Entry<K, V> pollLastEntry() { // return NULL erlaubt.
		return ascending ? parent.bcPollLastEntryOrNull(intervall) : parent.bcPollFirstEntryOrNull(intervall);
	}

	@Override
	public @NotNull NavigableMap<K, V> descendingMap() {
		return new AVLMapSubMap<>(parent, intervall, !ascending);
	}

	@Override
	public @NotNull NavigableSet<K> navigableKeySet() {
		return new AVLMapSubKeySet<>(this);
	}

	@Override
	public @NotNull NavigableSet<K> descendingKeySet() {
		return new AVLMapSubKeySet<>(new AVLMapSubMap<>(parent, intervall, !ascending));
	}

	@Override
	public @NotNull NavigableMap<K, V> subMap(final @NotNull K fromKey, final boolean fromInclusive,
			final @NotNull K toKey, final boolean toInclusive) {
		return createMap(fromKey, fromInclusive, toKey, toInclusive, ascending);
	}

	@Override
	public @NotNull NavigableMap<K, V> headMap(final @NotNull K toKey, final boolean inclusive) {
		return createMap(intervall.from, intervall.fromInc, toKey, inclusive, ascending);
	}

	@Override
	public @NotNull NavigableMap<K, V> tailMap(final @NotNull K fromKey, final boolean inclusive) {
		return createMap(fromKey, inclusive, intervall.to, intervall.toInc, ascending);
	}

	@Override
	public @NotNull SortedMap<K, V> subMap(final @NotNull K fromKey, final @NotNull K toKey) {
		return createMap(fromKey, true, toKey, false, ascending);
	}

	@Override
	public @NotNull SortedMap<K, V> headMap(final @NotNull K toKey) {
		return createMap(intervall.from, intervall.fromInc, toKey, false, ascending);
	}

	@Override
	public @NotNull SortedMap<K, V> tailMap(final @NotNull K fromKey) {
		return createMap(fromKey, true, intervall.to, intervall.toInc, ascending);
	}

	// ########################################################################
	// ######################### PROTECTED ####################################
	// ########################################################################

	/**
	 * Wird aufgerufen von {@link AVLMapSubKeySet#add(Object)}. Fügt einen Schlüssel (Key) dieser Datenstruktur hinzu.
	 *
	 * @param e Der einzufügende Schlüssel (Key).
	 *
	 * @return TRUE, falls der Schlüssel (Key) noch nicht existierte, sonst FALSE.
	 */
	boolean bcAddKey(final @NotNull K e) {
		return parent.bcAddKey(e, intervall);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubKeySet#addAll(Collection)}. Fügt alle Schlüssel (Keys) der Collection dieser
	 * Datenstruktur hinzu.
	 *
	 * @param c Die Collection mit den einzufügenden Schlüsseln (Keys).
	 *
	 * @return TRUE, falls mindestens ein Schlüssel (Key) noch nicht existierte und somit hinzugefügt wurde.
	 */
	boolean bcAddAllKeys(final @NotNull Collection<? extends K> c) {
		return parent.bcAddAllKeys(c, intervall);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubEntrySet#add(java.util.Map.Entry)}. Fügt ein Entry der Datenstruktur hinzu.
	 *
	 * @param e Das einzufügende Entry.
	 *
	 * @return TRUE, falls das Entry (e.getKey(), e.getValue()) neu war und somit hinzugefügt wurde.
	 */
	boolean bcAddEntryReturnBool(final @NotNull Entry<K, V> e) {
		return parent.bcAddEntryReturnBool(e, intervall);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubEntrySet#addAll(Collection)}. Fügt alle Entries der Collection dieser
	 * Datenstruktur hinzu.
	 *
	 * @param c Die Collection mit den einzufügenden Entries.
	 *
	 * @return TRUE, falls mindestens ein Entry neu war und somit hinzugefügt wurde.
	 */
	boolean bcAddAllEntries(final @NotNull Collection<? extends Entry<K, V>> c) {
		return parent.bcAddAllEntries(c, intervall);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubKeySet#containsAll(Collection)}. Überprüft, ob alle Schlüssel (Keys) der
	 * Collection in dieser Datenstruktur existieren.
	 *
	 * @param c Die Collection mit allen Schlüsseln (Keys) welche überprüft werden sollen.
	 *
	 * @return TRUE, falls alle Schlüssel (Keys) der Collection in dieser Datenstruktur existieren.
	 */
	boolean bcContainsAllKeys(final @NotNull Collection<?> c) {
		return parent.bcContainsAllKeys(c, intervall);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubEntrySet#contains(Object)}. Überprüft, ob das übergebene Entry in dieser
	 * Datenstruktur existiert.
	 *
	 * @param o Das Entry (Schlüssel-Wert-Paar) nach dem gesucht wird.
	 *
	 * @return TRUE, falls das übergebene Entry bereits in dieser Datenstruktur existiert.
	 */
	boolean bcContainsEntry(final @NotNull Object o) {
		return parent.bcContainsEntry(o, intervall);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubEntrySet#containsAll(Collection)}. Überprüft, ob alle Entries der Collection
	 * in dieser Datenstruktur existieren.
	 *
	 * @param c Die Collection mit den Entries welche überprüft werden sollen.
	 *
	 * @return TRUE, falls alle Entries in dieser Datenstruktur existieren.
	 */
	boolean bcContainsAllEntries(final @NotNull Collection<?> c) {
		return parent.bcContainsAllEntries(c, intervall);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubCollection#containsAll(Collection)}. Überprüft, ob alle Werte (Values) aus
	 * der Collection in dieser Datenstruktur vorkommen. Diese Methode sollte NICHT verwendet werden, da sie
	 * quadratische Laufzeit hat.
	 *
	 * @param c Die Collection deren Werte (Values) überprüft werden sollen.
	 *
	 * @return TRUE, falls alle Werte (Values) der Collection in dieser Datenstruktur existieren.
	 *
	 */
	boolean bcContainsAllValues(final @NotNull Collection<?> c) {
		return parent.bcContainsAllValues(c, intervall);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubKeySet#remove(Object)}. Entfernt einen Schlüssel (Key) aus dieser
	 * Datenstruktur.
	 *
	 * @param o Der Schlüssel (Key) der entfernt werden soll.
	 *
	 * @return TRUE, falls der Schlüssel existierte und somit entfernt wurde.
	 */
	boolean bcRemoveKeyReturnBool(final @NotNull Object o) {
		return parent.bcRemoveKeyReturnBool(o, intervall);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubKeySet#removeAll(Collection)}. Entfernt alle Schlüssel (Keys) aus dieser
	 * Datenstruktur.
	 *
	 * @param c Die Collection mit allen Schlüsseln (Keys) die entfernt werden sollen.
	 *
	 * @return TRUE, falls mindestens ein Schlüssel (Key) entfernt wurde.
	 */
	boolean bcRemoveAllKeys(final @NotNull Collection<?> c) {
		return parent.bcRemoveAllKeys(c, intervall);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubEntrySet#remove(Object)}. Entfernt das Entry aus dieser Datenstruktur.
	 *
	 * @param o Das Entry, welches entfernt werden soll.
	 *
	 * @return TRUE, falls das Entry in der Datenstruktur existierte und somit entfernt wurde.
	 */
	boolean bcRemoveEntry(final @NotNull Object o) {
		return parent.bcRemoveEntry(o, intervall);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubEntrySet#removeAll(Collection)}. Entfernt alle Entries der Collection aus
	 * dieser Datenstruktur.
	 *
	 * @param c Die Collection mit den Entries, welche entfernt werden sollen.
	 *
	 * @return TRUE, falls mindestens ein Entry entfernt wurde.
	 */
	boolean bcRemoveAllEntries(final @NotNull Collection<?> c) {
		return parent.bcRemoveAllEntries(c, intervall);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubKeySet#pollFirst()}. Entfernt und liefert den ersten Schlüssel (Key) dieser
	 * Datenstruktur. Dabei wird beachtet, ob diese Sub-Map aufsteigend oder absteigend zu interpretieren ist.
	 *
	 * @return Entfernt und liefert den ersten Schlüssel (Key) dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	K bcPollFirstKeyOrNull() {
		return ascending ? parent.bcPollFirstKeyOrNull(intervall) : parent.bcPollLastKeyOrNull(intervall);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubKeySet#pollLast()}. Entfernt und liefert den letzten Schlüssel (Key) dieser
	 * Datenstruktur. Dabei wird beachtet, ob diese Sub-Map aufsteigend oder absteigend zu interpretieren ist.
	 *
	 * @return Entfernt und liefert den letzten Schlüssel (Key) dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	K bcPollLastKeyOrNull() {
		return ascending ? parent.bcPollLastKeyOrNull(intervall) : parent.bcPollFirstKeyOrNull(intervall);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubKeySet#retainAll(Collection)}. Entfernt alle Schlüssel (Keys) aus dieser
	 * Datenstruktur, außer sie sind in der Collection enthalten.
	 *
	 * @param c Die Collection deren Schlüssel (Keys) nicht entfernt werden dürfen.
	 *
	 * @return TRUE, falls mindestens ein Schlüssel (Key) entfernt wurde.
	 */
	@SuppressWarnings("unchecked")
	boolean bcRetainAllKeys(final @NotNull Collection<?> c) {
		// Wandle die Collection in eine Map um, damit der Zugriff schnell ist.
		// Dies ist erlaubt, da die Schlüssel eine natürliche Ordnung aufweisen.
		final @NotNull AVLMap<K, K> mapRetain = new AVLMap<>();
		for (final @NotNull Object obj : c) {
			final @NotNull K key = (@NotNull K) obj;
			mapRetain.put(key, key);
		}

		// Iteriere und lösche falls nötig...
		boolean changed = false;
		final Iterator<K> iterOfKeys = bcGetSubKeySetIterator();
		while (iterOfKeys.hasNext()) {
			final K key = iterOfKeys.next();
			if (!mapRetain.containsKey(key)) {
				iterOfKeys.remove();
				changed = true;
			}
		}
		return changed;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubEntrySet#retainAll(Collection)}. Entfernt alle Entries aus dieser
	 * Datenstruktur, außer sie sind in der Collection enthalten.
	 *
	 * @param c Die Collection deren Entries nicht entfernt werden dürfen.
	 *
	 * @return TRUE, falls mindestens ein Entry entfernt wurde.
	 */
	@SuppressWarnings("unchecked")
	boolean bcRetainAllEntries(final @NotNull Collection<?> c) {
		// Vorsicht: c könnte verschiedene Entries mit dem selben KEY haben.

		// Sammle alle Entries, die bleiben sollen.
		final @NotNull AVLMap<K, V> mapSave = new AVLMap<>();

		final @NotNull Set<Entry<K, V>> setSave = mapSave.entrySet();
		for (final @NotNull Object o : c) {
			if (parent.bcContainsEntry(o, intervall)) {
				setSave.add((@NotNull Entry<K, V>) o);
			}
		}

		// Iteriere und lösche falls nötig...
		boolean changed = false;
		final Iterator<Entry<K, V>> iterOfEntries = bcGetSubEntrySetIterator();
		while (iterOfEntries.hasNext()) {
			if (!setSave.contains(iterOfEntries.next())) {
				iterOfEntries.remove();
				changed = true;
			}
		}
		return changed;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubCollectionIterator} und {@link AVLMapSubKeySetIterator}. Liefert das erste
	 * Entry als {@link AVLMapNode}, um über diese Datenstruktur zu iterieren. Dabei wird beachtet, ob diese Sub-Map
	 * aufsteigend oder absteigend zu interpretieren ist.
	 *
	 * @return Das erste Entry als {@link AVLMapNode} dieser Datenstruktur.
	 */
	AVLMapNode<@NotNull K, V> bcGetFirstEntryAsNode() { // return NULL erlaubt.
		return ascending ? parent.bcGetFirstEntryOrNull(intervall) : parent.bcGetLastEntryOrNull(intervall);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubCollectionIterator} und {@link AVLMapSubKeySetIterator}. Liefert das nächste
	 * Entry relativ zu einem übergebenen Entry. Dabei wird beachtet, ob diese Sub-Map aufsteigend oder absteigend zu
	 * interpretieren ist.
	 *
	 * @param node Das Entry dessen Nachfolger verlangt wird.
	 *
	 * @return Das nächste Entry relativ zu einem übergebenen Entry.
	 */
	AVLMapNode<K, V> bcGetNextEntryOrNull(final @NotNull AVLMapNode<K, V> node) {
		return ascending ? parent.bcGetNextEntryOrNull(node, intervall) : parent.bcGetPrevEntryOrNull(node, intervall);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubKeySet#floor(Object)}. Liefert den größten Schlüssel (Key) welcher kleiner
	 * oder gleich dem übergebenen Schlüssel (Key) ist. Somit der selbe Schlüssel (Key) falls vorhanden, andernfalls den
	 * Vorgänger-Schlüssel (Key) falls vorhanden, andernfalls NULL. Dabei wird beachtet, ob diese Sub-Map aufsteigend
	 * oder absteigend zu interpretieren ist.
	 *
	 * @param e Der Schlüssel (Key) der gesucht wird bzw. sein Vorgänger-Schlüssel.
	 *
	 * @return Den selben Schlüssel (Key) falls vorhanden, andernfalls sein Vorgänger-Schlüssel falls vorhanden,
	 *         andernfalls NULL.
	 */
	K bcGetFloorKeyOrNull(final @NotNull K e) {
		return ascending ? parent.bcGetFloorKeyOrNull(e, intervall) : parent.bcGetCeilingKeyOrNull(e, intervall);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubKeySet#ceiling(Object)}. Liefert den kleinsten Schlüssel (Key) welcher größer
	 * oder gleich dem übergebenen Schlüssel (Key) ist. Somit der selbe Schlüssel (Key) falls vorhanden, andernfalls
	 * sein Nachfolger-Schlüssel (Key) falls vorhanden, andernfalls NULL. Dabei wird beachtet, ob diese Sub-Map
	 * aufsteigend oder absteigend zu interpretieren ist.
	 *
	 * @param e Der Schlüssel (Key) der gesucht wird bzw. sein Nachfolger-Schlüssel.
	 *
	 * @return Den selben Schlüssel (Key) falls vorhanden, andernfalls sein Nachfolger-Schlüssel falls vorhanden,
	 *         andernfalls NULL.
	 */
	K bcGetCeilingKeyOrNull(final @NotNull K e) {
		return ascending ? parent.bcGetCeilingKeyOrNull(e, intervall) : parent.bcGetFloorKeyOrNull(e, intervall);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubKeySet#lower(Object)}. Liefert den größten Schlüssel (Key) welcher kleiner
	 * ist als der übergebene Schlüssel (Key), somit den Vorgänger-Schlüssel des Schlüssels (Key). Dabei wird beachtet,
	 * ob diese Sub-Map aufsteigend oder absteigend zu interpretieren ist.
	 *
	 * @param e Der Schlüssel (Key) dessen Vorgänger gesucht wird.
	 *
	 * @return Den Vorgänger-Schlüssel des übergebenen Schlüssels (Key) falls vorhanden, sonst NULL.
	 */
	K bcGetLowerKeyOrNull(final @NotNull K e) {
		return ascending ? parent.bcGetLowerKeyOrNull(e, intervall) : parent.bcGetHigherKeyOrNull(e, intervall);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubKeySet#higher(Object)}. Liefert den kleinsten Schlüssel (Key) welcher größer
	 * ist als der übergebene Schlüssel (Key), somit den Nachfolger-Schlüssel des übergebenen Schlüssels (Key). Dabei
	 * wird beachtet, ob diese Sub-Map aufsteigend oder absteigend zu interpretieren ist.
	 *
	 * @param e Der Schlüssel (Key) dessen Nachfolger-Schlüssel gesucht wird.
	 *
	 * @return Den Nachfolger-Schlüssel des übergebenen Schlüssels (Key) falls vorhanden, sonst NULL.
	 */
	K bcGetHigherKeyOrNull(final @NotNull K e) {
		return ascending ? parent.bcGetHigherKeyOrNull(e, intervall) : parent.bcGetLowerKeyOrNull(e, intervall);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubKeySet#toArray()} und {@link AVLMapSubKeySet#toArray(Object[])}. Liefert
	 * einen {@link ArrayList} der alle Schlüssel (Keys) dieser Sub-Map beinhaltet.
	 *
	 * @return Ein {@link ArrayList} der alle Schlüssel (Keys) dieser Sub-Map beinhaltet.
	 */
	@NotNull
	ArrayList<K> bcGetArrayListOfKeys() {
		final ArrayList<K> v = new ArrayList<>();
		final Iterator<K> iter = navigableKeySet().iterator();
		while (iter.hasNext()) {
			v.add(iter.next());
		}
		return v;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubCollection#toArray()} und {@link AVLMapSubCollection#toArray(Object[])}.
	 * Liefert einen {@link ArrayList} der alle Werte (Values) dieser Sub-Map beinhaltet.
	 *
	 * @return Ein {@link ArrayList} der alle Werte (Values) dieser Sub-Map beinhaltet.
	 */
	@NotNull
	ArrayList<V> bcGetArrayListOfValues() {
		final ArrayList<V> v = new ArrayList<>();
		final Iterator<V> iter = values().iterator();
		while (iter.hasNext()) {
			v.add(iter.next());
		}
		return v;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubEntrySet#toArray()} und {@link AVLMapSubEntrySet#toArray(Object[])}. Liefert
	 * einen {@link ArrayList} der alle Entries dieser Sub-Map beinhaltet.
	 *
	 * @return Ein {@link ArrayList} der alle Entries dieser Sub-Map beinhaltet.
	 */
	@NotNull
	ArrayList<Entry<K, V>> bcGetArrayListOfEntries() {
		final ArrayList<Entry<K, V>> v = new ArrayList<>();
		final Iterator<Entry<K, V>> iter = entrySet().iterator();
		while (iter.hasNext()) {
			v.add(iter.next());
		}
		return v;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubKeySet#iterator()}. Liefert einen {@link Iterator} von Schlüsseln (Keys)
	 * relativ zu dieser Sub-Map.
	 *
	 * @return Einen {@link Iterator} von Schlüsseln (Keys) relativ zu dieser Sub-Map.
	 */
	@NotNull
	Iterator<K> bcGetSubKeySetIterator() {
		return new AVLMapSubKeySetIterator<>(this);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubCollection#iterator()}. Liefert einen {@link Iterator} von Werten (Values)
	 * relativ zu dieser Sub-Map.
	 *
	 * @return Einen {@link Iterator} von Werten (Values) relativ zu dieser Sub-Map.
	 */
	@NotNull
	Iterator<V> bcGetSubCollectionIterator() {
		return new AVLMapSubCollectionIterator<>(this);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubEntrySet#iterator()}. Liefert einen {@link Iterator} von Entries relativ zu
	 * dieser Sub-Map.
	 *
	 * @return Ein {@link Iterator} von Entries relativ zu dieser Sub-Map.
	 */
	@NotNull
	Iterator<Entry<K, V>> bcGetSubEntrySetIterator() {
		return new AVLMapSubEntrySetIterator<>(this);
	}

	/**
	 * Wird aufgerufen und von {@link AVLMapSubKeySet#descendingSet()}. Liefert ein {@link NavigableSet} von Schlüsseln
	 * (Keys) relativ zu dieser <strong>absteigenden</strong> Sub-Map.
	 *
	 * @return Ein {@link NavigableSet} von Schlüsseln (Keys) relativ zu dieser <strong>absteigenden</strong> Sub-Map.
	 */
	@NotNull
	NavigableSet<K> bcGetSubKeySetDescending() {
		return new AVLMapSubKeySet<>(new AVLMapSubMap<>(parent, intervall, !ascending));
	}

	/**
	 * Wird aufgerufen und von {@link AVLMapSubKeySet#descendingIterator()}. Liefert einen {@link Iterator} von
	 * Schlüsseln (Keys) relativ zu dieser <strong>absteigenden</strong> Sub-Map.
	 *
	 * @return Ein {@link Iterator} von Schlüsseln (Keys) relativ zu dieser <strong>absteigenden</strong> Sub-Map.
	 */
	@NotNull
	Iterator<K> bcGetSubKeySetDescendingIterator() {
		return new AVLMapSubKeySetIterator<>(new AVLMapSubMap<>(parent, intervall, !ascending));
	}

	/**
	 * Wird aufgerufen und von {@link AVLMapSubKeySet#subSet(Object, boolean, Object, boolean)}. Liefert ein
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
	NavigableSet<K> bcGetSubKeySet(final @NotNull K fromElement, final boolean fromInclusive, final @NotNull K toElement,
			final boolean toInclusive) {
		return createSet(fromElement, fromInclusive, toElement, toInclusive, ascending);
	}

	/**
	 * Wird aufgerufen und von {@link AVLMapSubKeySet#headSet(Object, boolean)}. Liefert ein {@link NavigableSet} von
	 * Schlüsseln (Keys) relativ zu dieser Sub-Map.
	 *
	 * @param toElement Die rechte (bis) Intervallsgrenze.
	 * @param inclusive Gibt an, ob die rechte (bis) Intervallsgrenze inklusive ist.
	 *
	 * @return Ein {@link NavigableSet} von Schlüsseln (Keys) relativ zu dieser Sub-Map.
	 */
	@NotNull
	NavigableSet<K> bcGetSubKeyHeadSet(final @NotNull K toElement, final boolean inclusive) {
		return createSet(intervall.from, intervall.fromInc, toElement, inclusive, ascending);
	}

	/**
	 * Wird aufgerufen und von {@link AVLMapSubKeySet#tailSet(Object, boolean)}. Liefert ein {@link NavigableSet} von
	 * Schlüsseln (Keys) relativ zu dieser Sub-Map.
	 *
	 * @param fromElement Die linke (von) Intervallsgrenze.
	 * @param inclusive   Gibt an, ob die linke (von) Intervallsgrenze inklusive ist.
	 *
	 * @return Ein {@link NavigableSet} von Schlüsseln (Keys) relativ zu dieser Sub-Map.
	 */
	@NotNull
	NavigableSet<K> bcGetSubKeyTailSet(final @NotNull K fromElement, final boolean inclusive) {
		return createSet(fromElement, inclusive, intervall.to, intervall.toInc, ascending);
	}

	/**
	 * Wird aufgerufen und von {@link AVLMapSubKeySet#subSet(Object, Object)}. Liefert ein {@link NavigableSet} von
	 * Schlüsseln (Keys) relativ zu dieser Sub-Map.
	 *
	 * Äquivalent zu {@link #bcGetSubKeySet(Object, boolean, Object, boolean)} mit den Werten (fromElement, true,
	 * toElement, false).
	 *
	 * @param fromElement Die linke (von) Intervallsgrenze <strong>inklusive</strong>.
	 * @param toElement   Die rechte (bis) Intervallsgrenze <strong>exklusive</strong>.
	 *
	 * @return Ein {@link NavigableSet} von Schlüsseln (Keys) relativ zu dieser Sub-Map.
	 */
	@NotNull
	SortedSet<K> bcGetSubKeySet(final @NotNull K fromElement, final @NotNull K toElement) {
		return createSet(fromElement, true, toElement, false, ascending);
	}

	/**
	 * Wird aufgerufen und von {@link AVLMapSubKeySet#headSet(Object)}. Liefert ein {@link NavigableSet} von Schlüsseln
	 * (Keys) relativ zu dieser Sub-Map.
	 *
	 * Äquivalent zu {@link #bcGetSubKeyHeadSet(Object, boolean)} mit den Werten (toElement, false).
	 *
	 * @param toElement Die rechte (bis) Intervallsgrenze <strong>exklusive</strong>.
	 *
	 * @return Ein {@link NavigableSet} von Schlüsseln (Keys) relativ zu dieser Sub-Map.
	 */
	@NotNull
	SortedSet<K> bcGetSubKeyHeadSet(final @NotNull K toElement) {
		return createSet(intervall.from, intervall.fromInc, toElement, false, ascending);
	}

	/**
	 * Wird aufgerufen und von {@link AVLMapSubKeySet#tailSet(Object)}. Liefert ein {@link NavigableSet} von Schlüsseln
	 * (Keys) relativ zu dieser Sub-Map.
	 *
	 * Äquivalent zu {@link #bcGetSubKeyTailSet(Object, boolean)} mit den Werten (fromElement, true).
	 *
	 * @param fromElement Die linke (von) Intervallsgrenze <strong>inklusive</strong>.
	 *
	 * @return Ein {@link NavigableSet} von Schlüsseln (Keys) relativ zu dieser Sub-Map.
	 */
	@NotNull
	SortedSet<K> bcGetSubKeyTailSet(final @NotNull K fromElement) {
		return createSet(fromElement, true, intervall.to, intervall.toInc, ascending);
	}

	// ########################################################################
	// ########################## PRIVATE #####################################
	// ########################################################################

	private @NotNull AVLMapSubMap<K, V> createMap(final @NotNull K from, final boolean fromInc, final @NotNull K to,
			final boolean toInc, final boolean asc) {
		if (parent.bcCheckOutOfIntervall(from, fromInc, intervall)) {
			throw new IllegalArgumentException("FROM-KEY " + from + "/" + fromInc + " nicht in " + intervall);
		}
		if (parent.bcCheckOutOfIntervall(to, toInc, intervall)) {
			throw new IllegalArgumentException("TO-KEY " + to + "/" + toInc + " nicht in " + intervall);
		}

		return new AVLMapSubMap<>(parent, new AVLMapIntervall<>(from, fromInc, to, toInc), asc);
	}

	private @NotNull AVLMapSubKeySet<K, V> createSet(final @NotNull K from, final boolean fromInc, final @NotNull K to,
			final boolean toInc, final boolean asc) {
		return new AVLMapSubKeySet<>(createMap(from, fromInc, to, toInc, asc));
	}

}
