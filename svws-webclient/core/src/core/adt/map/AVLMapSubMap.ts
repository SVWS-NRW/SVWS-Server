import type { JavaFunction } from '../../../java/util/function/JavaFunction';
import type { JavaMapEntry } from '../../../java/util/JavaMapEntry';
import { cast_java_util_Map_Entry } from '../../../java/util/JavaMapEntry';
import type { NavigableSet } from '../../../java/util/NavigableSet';
import type { JavaSet } from '../../../java/util/JavaSet';
import type { NavigableMap } from '../../../java/util/NavigableMap';
import { StringBuilder } from '../../../java/lang/StringBuilder';
import { AVLMapSubCollection } from '../../../core/adt/map/AVLMapSubCollection';
import { AVLMapIntervall } from '../../../core/adt/map/AVLMapIntervall';
import { ArrayList } from '../../../java/util/ArrayList';
import { AVLMapSubEntrySetIterator } from '../../../core/adt/map/AVLMapSubEntrySetIterator';
import { AVLMapSubKeySet } from '../../../core/adt/map/AVLMapSubKeySet';
import type { SortedSet } from '../../../java/util/SortedSet';
import { AVLMap, cast_de_svws_nrw_core_adt_map_AVLMap } from '../../../core/adt/map/AVLMap';
import type { Comparator } from '../../../java/util/Comparator';
import { AVLMapNode } from '../../../core/adt/map/AVLMapNode';
import type { SortedMap } from '../../../java/util/SortedMap';
import type { JavaIterator } from '../../../java/util/JavaIterator';
import { AVLMapSubKeySetIterator } from '../../../core/adt/map/AVLMapSubKeySetIterator';
import type { Collection } from '../../../java/util/Collection';
import { JavaObject } from '../../../java/lang/JavaObject';
import { AVLMapSubEntrySet } from '../../../core/adt/map/AVLMapSubEntrySet';
import type { JavaMap } from '../../../java/util/JavaMap';
import { cast_java_util_Map } from '../../../java/util/JavaMap';
import { AVLMapSubCollectionIterator } from '../../../core/adt/map/AVLMapSubCollectionIterator';
import { IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';

export class AVLMapSubMap<K, V> extends JavaObject implements NavigableMap<K, V> {

	/**
	 *  Die {@link AVLMap} auf der diese Sup-Map operiert.
	 */
	private readonly _par : AVLMap<K, V>;

	/**
	 *  Das {@link AVLMapIntervall} auf das sich diese Sub-Map bezieht.
	 */
	private readonly _iv : AVLMapIntervall<K>;

	/**
	 *  Falls TRUE wird die {@link AVLMap} aufsteigend, andernfalls absteigend interpretiert.
	 */
	private readonly _asc : boolean;


	/**
	 * Erstellt eine neue Sub-Map relativ zur übergebenen {@link AVLMap}.
	 *
	 * @param parent    Die {@link AVLMap} auf der diese Sup-Map operiert.
	 * @param intervall Das {@link AVLMapIntervall} auf das sich diese Sub-Map bezieht.
	 * @param asc       Falls TRUE wird die {@link AVLMap} aufsteigend, andernfalls absteigend interpretiert.
	 */
	constructor(parent : AVLMap<K, V>, intervall : AVLMapIntervall<K>, asc : boolean) {
		super();
		this._par = parent;
		this._iv = intervall;
		this._asc = asc;
	}

	public toString() : string {
		const sb : StringBuilder | null = new StringBuilder();
		sb.append("Entries = [");
		let first : boolean = true;
		for (const e of this.entrySet()) {
			if (first)
				first = false;
			else
				sb.append(", ");
			sb.append(e);
		}
		sb.append("], iv = ");
		sb.append(this._iv);
		sb.append(", asc = ");
		sb.append(this._asc);
		return sb.toString();
	}

	public equals(o : unknown) : boolean {
		if (o as unknown === this as unknown)
			return true;
		if (!(((o instanceof JavaObject) && ((o as JavaObject).isTranspiledInstanceOf('java.util.Map')))))
			return false;
		const mapO : JavaMap<unknown, unknown> | null = cast_java_util_Map(o);
		if (mapO.size() !== this.size())
			return false;
		for (const e of this.entrySet())
			if (!JavaObject.equalsTranspiler(e.getValue(), (mapO.get(e.getKey()))))
				return false;
		return true;
	}

	public hashCode() : number {
		let h : number = 0;
		for (const entry of this.entrySet())
			h += JavaObject.getTranspilerHashCode(entry);
		return h;
	}

	public comparator() : Comparator<K> {
		return this._par.bcGetComparator(this._iv);
	}

	public firstKey() : K {
		return this._asc ? this._par.bcGetFirstKeyOrException(this._iv) : this._par.bcGetLastKeyOrException(this._iv);
	}

	public lastKey() : K {
		return this._asc ? this._par.bcGetLastKeyOrException(this._iv) : this._par.bcGetFirstKeyOrException(this._iv);
	}

	public keySet() : JavaSet<K> {
		return new AVLMapSubKeySet(this);
	}

	public values() : Collection<V> {
		return new AVLMapSubCollection(this);
	}

	public entrySet() : JavaSet<JavaMapEntry<K, V>> {
		return new AVLMapSubEntrySet(this);
	}

	public size() : number {
		return this._par.bcGetSize(this._iv);
	}

	public isEmpty() : boolean {
		return this._par.bcIsEmpty(this._iv);
	}

	public containsKey(key : unknown) : boolean {
		return this._par.bcContainsKey(key, this._iv);
	}

	public containsValue(value : unknown) : boolean {
		return this._par.bcContainsValue(value, this._iv);
	}

	public get(key : unknown) : V | null {
		return this._par.bcGetValueOfKeyOrNull(key, this._iv);
	}

	public put(key : K, value : V) : V | null {
		return this._par.bcAddEntryReturnOldValueOrNull(key, value, this._iv);
	}

	public remove(key : unknown) : V | null {
		return this._par.bcRemoveKeyReturnOldValueOrNull(key, this._iv);
	}

	public putAll(map : JavaMap<K, V>) : void {
		this._par.bcAddAllEntriesOfMap(map, this._iv);
	}

	public clear() : void {
		const iter : JavaIterator<JavaMapEntry<K | null, V | null> | null> | null = this.bcGetSubEntrySetIterator();
		while (iter.hasNext()) {
			iter.next();
			iter.remove();
		}
	}

	public lowerEntry(key : K) : JavaMapEntry<K, V> | null {
		return this._asc ? this._par.bcGetLowerEntryOrNull(key, this._iv) : this._par.bcGetHigherEntryOrNull(key, this._iv);
	}

	public lowerKey(key : K) : K | null {
		return this._asc ? this._par.bcGetLowerKeyOrNull(key, this._iv) : this._par.bcGetHigherKeyOrNull(key, this._iv);
	}

	public floorEntry(key : K) : JavaMapEntry<K, V> | null {
		return this._asc ? this._par.bcGetFloorEntryOrNull(key, this._iv) : this._par.bcGetCeilingEntryOrNull(key, this._iv);
	}

	public floorKey(key : K) : K | null {
		return this._asc ? this._par.bcGetFloorKeyOrNull(key, this._iv) : this._par.bcGetCeilingKeyOrNull(key, this._iv);
	}

	public ceilingEntry(key : K) : JavaMapEntry<K, V> | null {
		return this._asc ? this._par.bcGetCeilingEntryOrNull(key, this._iv) : this._par.bcGetFloorEntryOrNull(key, this._iv);
	}

	public ceilingKey(key : K) : K | null {
		return this._asc ? this._par.bcGetCeilingKeyOrNull(key, this._iv) : this._par.bcGetFloorKeyOrNull(key, this._iv);
	}

	public higherEntry(key : K) : JavaMapEntry<K, V> | null {
		return this._asc ? this._par.bcGetHigherEntryOrNull(key, this._iv) : this._par.bcGetLowerEntryOrNull(key, this._iv);
	}

	public higherKey(key : K) : K | null {
		return this._asc ? this._par.bcGetHigherKeyOrNull(key, this._iv) : this._par.bcGetLowerKeyOrNull(key, this._iv);
	}

	public firstEntry() : JavaMapEntry<K, V> | null {
		return this._asc ? this._par.bcGetFirstEntryOrNull(this._iv) : this._par.bcGetLastEntryOrNull(this._iv);
	}

	public lastEntry() : JavaMapEntry<K, V> | null {
		return this._asc ? this._par.bcGetLastEntryOrNull(this._iv) : this._par.bcGetFirstEntryOrNull(this._iv);
	}

	public pollFirstEntry() : JavaMapEntry<K, V> | null {
		return this._asc ? this._par.bcPollFirstEntryOrNull(this._iv) : this._par.bcPollLastEntryOrNull(this._iv);
	}

	public pollLastEntry() : JavaMapEntry<K, V> | null {
		return this._asc ? this._par.bcPollLastEntryOrNull(this._iv) : this._par.bcPollFirstEntryOrNull(this._iv);
	}

	public descendingMap() : NavigableMap<K, V> {
		return new AVLMapSubMap(this._par, this._iv, !this._asc);
	}

	public navigableKeySet() : NavigableSet<K> {
		return new AVLMapSubKeySet(this);
	}

	public descendingKeySet() : NavigableSet<K> {
		return new AVLMapSubKeySet(new AVLMapSubMap(this._par, this._iv, !this._asc));
	}

	public subMap(fromKey : K, fromInclusive : boolean, toKey : K, toInclusive : boolean) : NavigableMap<K, V>;

	public subMap(fromKey : K, toKey : K) : SortedMap<K, V>;

	/**
	 * Implementation for method overloads of 'subMap'
	 */
	public subMap(__param0 : K, __param1 : K | boolean, __param2? : K, __param3? : boolean) : NavigableMap<K, V> | SortedMap<K, V> {
		if (((typeof __param0 !== "undefined") && (typeof __param0 !== "undefined")) && ((typeof __param1 !== "undefined") && typeof __param1 === "boolean") && ((typeof __param2 !== "undefined") && (typeof __param2 !== "undefined")) && ((typeof __param3 !== "undefined") && typeof __param3 === "boolean")) {
			const fromKey : K = __param0 as unknown as K;
			const fromInclusive : boolean = __param1 as boolean;
			const toKey : K = __param2 as unknown as K;
			const toInclusive : boolean = __param3 as boolean;
			return this._createMap(fromKey, fromInclusive, toKey, toInclusive, this._asc);
		} else if (((typeof __param0 !== "undefined") && (typeof __param0 !== "undefined")) && ((typeof __param1 !== "undefined") && (typeof __param1 !== "undefined")) && (typeof __param2 === "undefined") && (typeof __param3 === "undefined")) {
			const fromKey : K = __param0 as unknown as K;
			const toKey : K = __param1 as unknown as K;
			return this._createMap(fromKey, true, toKey, false, this._asc);
		} else throw new Error('invalid method overload');
	}

	public headMap(toKey : K, inclusive : boolean) : NavigableMap<K, V>;

	public headMap(toKey : K) : SortedMap<K, V>;

	/**
	 * Implementation for method overloads of 'headMap'
	 */
	public headMap(__param0 : K, __param1? : boolean) : NavigableMap<K, V> | SortedMap<K, V> {
		if (((typeof __param0 !== "undefined") && (typeof __param0 !== "undefined")) && ((typeof __param1 !== "undefined") && typeof __param1 === "boolean")) {
			const toKey : K = __param0 as unknown as K;
			const inclusive : boolean = __param1 as boolean;
			return this._createMap(this._iv.from, this._iv.fromInc, toKey, inclusive, this._asc);
		} else if (((typeof __param0 !== "undefined") && (typeof __param0 !== "undefined")) && (typeof __param1 === "undefined")) {
			const toKey : K = __param0 as unknown as K;
			return this._createMap(this._iv.from, this._iv.fromInc, toKey, false, this._asc);
		} else throw new Error('invalid method overload');
	}

	public tailMap(fromKey : K, inclusive : boolean) : NavigableMap<K, V>;

	public tailMap(fromKey : K) : SortedMap<K, V>;

	/**
	 * Implementation for method overloads of 'tailMap'
	 */
	public tailMap(__param0 : K, __param1? : boolean) : NavigableMap<K, V> | SortedMap<K, V> {
		if (((typeof __param0 !== "undefined") && (typeof __param0 !== "undefined")) && ((typeof __param1 !== "undefined") && typeof __param1 === "boolean")) {
			const fromKey : K = __param0 as unknown as K;
			const inclusive : boolean = __param1 as boolean;
			return this._createMap(fromKey, inclusive, this._iv.to, this._iv.toInc, this._asc);
		} else if (((typeof __param0 !== "undefined") && (typeof __param0 !== "undefined")) && (typeof __param1 === "undefined")) {
			const fromKey : K = __param0 as unknown as K;
			return this._createMap(fromKey, true, this._iv.to, this._iv.toInc, this._asc);
		} else throw new Error('invalid method overload');
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubKeySet#add(Object)}. Fügt einen Schlüssel (Key) dieser Datenstruktur hinzu.
	 *
	 * @param e Der einzufügende Schlüssel (Key).
	 *
	 * @return TRUE, falls der Schlüssel (Key) noch nicht existierte, sonst FALSE.
	 */
	bcAddKey(e : K) : boolean {
		return this._par.bcAddKey(e, this._iv);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubKeySet#addAll(Collection)}. Fügt alle Schlüssel (Keys) der Collection dieser
	 * Datenstruktur hinzu.
	 *
	 * @param c Die Collection mit den einzufügenden Schlüsseln (Keys).
	 *
	 * @return TRUE, falls mindestens ein Schlüssel (Key) noch nicht existierte und somit hinzugefügt wurde.
	 */
	bcAddAllKeys(c : Collection<K>) : boolean {
		return this._par.bcAddAllKeys(c, this._iv);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubEntrySet#add(java.util.Map.Entry)}. Fügt ein Entry der Datenstruktur hinzu.
	 *
	 * @param e Das einzufügende Entry.
	 *
	 * @return TRUE, falls das Entry (e.getKey(), e.getValue()) neu war und somit hinzugefügt wurde.
	 */
	bcAddEntryReturnBool(e : JavaMapEntry<K, V>) : boolean {
		return this._par.bcAddEntryReturnBool(e, this._iv);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubEntrySet#addAll(Collection)}. Fügt alle Entries der Collection dieser
	 * Datenstruktur hinzu.
	 *
	 * @param c Die Collection mit den einzufügenden Entries.
	 *
	 * @return TRUE, falls mindestens ein Entry neu war und somit hinzugefügt wurde.
	 */
	bcAddAllEntries(c : Collection<JavaMapEntry<K, V>>) : boolean {
		return this._par.bcAddAllEntries(c, this._iv);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubKeySet#containsAll(Collection)}. Überprüft, ob alle Schlüssel (Keys) der
	 * Collection in dieser Datenstruktur existieren.
	 *
	 * @param c Die Collection mit allen Schlüsseln (Keys) welche überprüft werden sollen.
	 *
	 * @return TRUE, falls alle Schlüssel (Keys) der Collection in dieser Datenstruktur existieren.
	 */
	bcContainsAllKeys(c : Collection<unknown>) : boolean {
		return this._par.bcContainsAllKeys(c, this._iv);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubEntrySet#contains(Object)}. Überprüft, ob das übergebene Entry in dieser
	 * Datenstruktur existiert.
	 *
	 * @param o Das Entry (Schlüssel-Wert-Paar) nach dem gesucht wird.
	 *
	 * @return TRUE, falls das übergebene Entry bereits in dieser Datenstruktur existiert.
	 */
	bcContainsEntry(o : unknown) : boolean {
		return this._par.bcContainsEntry(o, this._iv);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubEntrySet#containsAll(Collection)}. Überprüft, ob alle Entries der Collection
	 * in dieser Datenstruktur existieren.
	 *
	 * @param c Die Collection mit den Entries welche überprüft werden sollen.
	 *
	 * @return TRUE, falls alle Entries in dieser Datenstruktur existieren.
	 */
	bcContainsAllEntries(c : Collection<unknown>) : boolean {
		return this._par.bcContainsAllEntries(c, this._iv);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubCollection#containsAll(Collection)}. Überprüft, ob alle Werte (Values) aus
	 * der Collection in dieser Datenstruktur vorkommen. Diese Methode sollte NICHT verwendet werden, da sie
	 * quadratische Laufzeit hat.
	 *
	 * @param c Die Collection deren Werte (Values) überprüft werden sollen.
	 *
	 * @return TRUE, falls alle Werte (Values) der Collection in dieser Datenstruktur existieren.
	 */
	bcContainsAllValues(c : Collection<unknown>) : boolean {
		return this._par.bcContainsAllValues(c, this._iv);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubKeySet#remove(Object)}. Entfernt einen Schlüssel (Key) aus dieser
	 * Datenstruktur.
	 *
	 * @param o Der Schlüssel (Key) der entfernt werden soll.
	 *
	 * @return TRUE, falls der Schlüssel existierte und somit entfernt wurde.
	 */
	bcRemoveKeyReturnBool(o : unknown) : boolean {
		return this._par.bcRemoveKeyReturnBool(o, this._iv);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubKeySet#removeAll(Collection)}. Entfernt alle Schlüssel (Keys) aus dieser
	 * Datenstruktur.
	 *
	 * @param c Die Collection mit allen Schlüsseln (Keys) die entfernt werden sollen.
	 *
	 * @return TRUE, falls mindestens ein Schlüssel (Key) entfernt wurde.
	 */
	bcRemoveAllKeys(c : Collection<unknown>) : boolean {
		return this._par.bcRemoveAllKeys(c, this._iv);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubEntrySet#remove(Object)}. Entfernt das Entry aus dieser Datenstruktur.
	 *
	 * @param o Das Entry, welches entfernt werden soll.
	 *
	 * @return TRUE, falls das Entry in der Datenstruktur existierte und somit entfernt wurde.
	 */
	bcRemoveEntry(o : unknown) : boolean {
		return this._par.bcRemoveEntry(o, this._iv);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubEntrySet#removeAll(Collection)}. Entfernt alle Entries der Collection aus
	 * dieser Datenstruktur.
	 *
	 * @param c Die Collection mit den Entries, welche entfernt werden sollen.
	 *
	 * @return TRUE, falls mindestens ein Entry entfernt wurde.
	 */
	bcRemoveAllEntries(c : Collection<unknown>) : boolean {
		return this._par.bcRemoveAllEntries(c, this._iv);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubKeySet#pollFirst()}. Entfernt und liefert den ersten Schlüssel (Key) dieser
	 * Datenstruktur. Dabei wird beachtet, ob diese Sub-Map aufsteigend oder absteigend zu interpretieren ist.
	 *
	 * @return Entfernt und liefert den ersten Schlüssel (Key) dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	bcPollFirstKeyOrNull() : K | null {
		return this._asc ? this._par.bcPollFirstKeyOrNull(this._iv) : this._par.bcPollLastKeyOrNull(this._iv);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubKeySet#pollLast()}. Entfernt und liefert den letzten Schlüssel (Key) dieser
	 * Datenstruktur. Dabei wird beachtet, ob diese Sub-Map aufsteigend oder absteigend zu interpretieren ist.
	 *
	 * @return Entfernt und liefert den letzten Schlüssel (Key) dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	bcPollLastKeyOrNull() : K | null {
		return this._asc ? this._par.bcPollLastKeyOrNull(this._iv) : this._par.bcPollFirstKeyOrNull(this._iv);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubKeySet#retainAll(Collection)}. Entfernt alle Schlüssel (Keys) aus dieser
	 * Datenstruktur, außer sie sind in der Collection enthalten.
	 *
	 * @param c Die Collection deren Schlüssel (Keys) nicht entfernt werden dürfen.
	 *
	 * @return TRUE, falls mindestens ein Schlüssel (Key) entfernt wurde.
	 */
	bcRetainAllKeys(c : Collection<unknown>) : boolean {
		const mapRetain : AVLMap<K, K> = new AVLMap();
		for (const obj of c) {
			const key : K = obj as unknown as K;
			mapRetain.put(key, key);
		}
		let changed : boolean = false;
		const iterOfKeys : JavaIterator<K | null> | null = this.bcGetSubKeySetIterator();
		while (iterOfKeys.hasNext()) {
			const key : K | null = iterOfKeys.next();
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
	bcRetainAllEntries(c : Collection<unknown>) : boolean {
		const mapSave : AVLMap<K, V> = new AVLMap();
		const setSave : JavaSet<JavaMapEntry<K, V>> = mapSave.entrySet();
		for (const o of c)
			if (this._par.bcContainsEntry(o, this._iv))
				setSave.add(cast_java_util_Map_Entry(o));
		let changed : boolean = false;
		const iterOfEntries : JavaIterator<JavaMapEntry<K | null, V | null> | null> | null = this.bcGetSubEntrySetIterator();
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
	bcGetFirstEntryAsNode() : AVLMapNode<K, V> | null {
		return this._asc ? this._par.bcGetFirstEntryOrNull(this._iv) : this._par.bcGetLastEntryOrNull(this._iv);
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
	bcGetNextEntryOrNull(node : AVLMapNode<K, V>) : AVLMapNode<K, V> | null {
		return this._asc ? this._par.bcGetNextEntryOrNull(node, this._iv) : this._par.bcGetPrevEntryOrNull(node, this._iv);
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
	bcGetFloorKeyOrNull(e : K) : K | null {
		return this._asc ? this._par.bcGetFloorKeyOrNull(e, this._iv) : this._par.bcGetCeilingKeyOrNull(e, this._iv);
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
	bcGetCeilingKeyOrNull(e : K) : K | null {
		return this._asc ? this._par.bcGetCeilingKeyOrNull(e, this._iv) : this._par.bcGetFloorKeyOrNull(e, this._iv);
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
	bcGetLowerKeyOrNull(e : K) : K | null {
		return this._asc ? this._par.bcGetLowerKeyOrNull(e, this._iv) : this._par.bcGetHigherKeyOrNull(e, this._iv);
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
	bcGetHigherKeyOrNull(e : K) : K | null {
		return this._asc ? this._par.bcGetHigherKeyOrNull(e, this._iv) : this._par.bcGetLowerKeyOrNull(e, this._iv);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubKeySet#toArray()} und {@link AVLMapSubKeySet#toArray(Object[])}. Liefert
	 * einen {@link ArrayList} der alle Schlüssel (Keys) dieser Sub-Map beinhaltet.
	 *
	 * @return Ein {@link ArrayList} der alle Schlüssel (Keys) dieser Sub-Map beinhaltet.
	 */
	bcGetArrayListOfKeys() : ArrayList<K | null> {
		const v : ArrayList<K | null> | null = new ArrayList();
		const iter : JavaIterator<K | null> | null = this.navigableKeySet().iterator();
		while (iter.hasNext())
			v.add(iter.next());
		return v;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubCollection#toArray()} und {@link AVLMapSubCollection#toArray(Object[])}.
	 * Liefert einen {@link ArrayList} der alle Werte (Values) dieser Sub-Map beinhaltet.
	 *
	 * @return Ein {@link ArrayList} der alle Werte (Values) dieser Sub-Map beinhaltet.
	 */
	bcGetArrayListOfValues() : ArrayList<V | null> {
		const v : ArrayList<V | null> | null = new ArrayList();
		const iter : JavaIterator<V | null> | null = this.values().iterator();
		while (iter.hasNext())
			v.add(iter.next());
		return v;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubEntrySet#toArray()} und {@link AVLMapSubEntrySet#toArray(Object[])}. Liefert
	 * einen {@link ArrayList} der alle Entries dieser Sub-Map beinhaltet.
	 *
	 * @return Ein {@link ArrayList} der alle Entries dieser Sub-Map beinhaltet.
	 */
	bcGetArrayListOfEntries() : ArrayList<JavaMapEntry<K | null, V | null> | null> {
		const v : ArrayList<JavaMapEntry<K | null, V | null> | null> | null = new ArrayList();
		const iter : JavaIterator<JavaMapEntry<K | null, V | null> | null> | null = this.entrySet().iterator();
		while (iter.hasNext())
			v.add(iter.next());
		return v;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubKeySet#iterator()}. Liefert einen {@link Iterator} von Schlüsseln (Keys)
	 * relativ zu dieser Sub-Map.
	 *
	 * @return Einen {@link Iterator} von Schlüsseln (Keys) relativ zu dieser Sub-Map.
	 */
	bcGetSubKeySetIterator() : JavaIterator<K> {
		return new AVLMapSubKeySetIterator(this);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubCollection#iterator()}. Liefert einen {@link Iterator} von Werten (Values)
	 * relativ zu dieser Sub-Map.
	 *
	 * @return Einen {@link Iterator} von Werten (Values) relativ zu dieser Sub-Map.
	 */
	bcGetSubCollectionIterator() : JavaIterator<V> {
		return new AVLMapSubCollectionIterator(this);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubEntrySet#iterator()}. Liefert einen {@link Iterator} von Entries relativ zu
	 * dieser Sub-Map.
	 *
	 * @return Ein {@link Iterator} von Entries relativ zu dieser Sub-Map.
	 */
	bcGetSubEntrySetIterator() : JavaIterator<JavaMapEntry<K, V>> {
		return new AVLMapSubEntrySetIterator(this);
	}

	/**
	 * Wird aufgerufen und von {@link AVLMapSubKeySet#descendingSet()}. Liefert ein {@link NavigableSet} von Schlüsseln
	 * (Keys) relativ zu dieser <strong>absteigenden</strong> Sub-Map.
	 *
	 * @return Ein {@link NavigableSet} von Schlüsseln (Keys) relativ zu dieser <strong>absteigenden</strong> Sub-Map.
	 */
	bcGetSubKeySetDescending() : NavigableSet<K> {
		return new AVLMapSubKeySet(new AVLMapSubMap(this._par, this._iv, !this._asc));
	}

	/**
	 * Wird aufgerufen und von {@link AVLMapSubKeySet#descendingIterator()}. Liefert einen {@link Iterator} von
	 * Schlüsseln (Keys) relativ zu dieser <strong>absteigenden</strong> Sub-Map.
	 *
	 * @return Ein {@link Iterator} von Schlüsseln (Keys) relativ zu dieser <strong>absteigenden</strong> Sub-Map.
	 */
	bcGetSubKeySetDescendingIterator() : JavaIterator<K> {
		return new AVLMapSubKeySetIterator(new AVLMapSubMap(this._par, this._iv, !this._asc));
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
	public bcGetSubKeySet(fromElement : K, fromInclusive : boolean, toElement : K, toInclusive : boolean) : NavigableSet<K>;

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
	public bcGetSubKeySet(fromElement : K, toElement : K) : SortedSet<K>;

	/**
	 * Implementation for method overloads of 'bcGetSubKeySet'
	 */
	public bcGetSubKeySet(__param0 : K, __param1 : K | boolean, __param2? : K, __param3? : boolean) : NavigableSet<K> | SortedSet<K> {
		if (((typeof __param0 !== "undefined") && (typeof __param0 !== "undefined")) && ((typeof __param1 !== "undefined") && typeof __param1 === "boolean") && ((typeof __param2 !== "undefined") && (typeof __param2 !== "undefined")) && ((typeof __param3 !== "undefined") && typeof __param3 === "boolean")) {
			const fromElement : K = __param0 as unknown as K;
			const fromInclusive : boolean = __param1 as boolean;
			const toElement : K = __param2 as unknown as K;
			const toInclusive : boolean = __param3 as boolean;
			return this._createSet(fromElement, fromInclusive, toElement, toInclusive, this._asc);
		} else if (((typeof __param0 !== "undefined") && (typeof __param0 !== "undefined")) && ((typeof __param1 !== "undefined") && (typeof __param1 !== "undefined")) && (typeof __param2 === "undefined") && (typeof __param3 === "undefined")) {
			const fromElement : K = __param0 as unknown as K;
			const toElement : K = __param1 as unknown as K;
			return this._createSet(fromElement, true, toElement, false, this._asc);
		} else throw new Error('invalid method overload');
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
	public bcGetSubKeyHeadSet(toElement : K, inclusive : boolean) : NavigableSet<K>;

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
	public bcGetSubKeyHeadSet(toElement : K) : SortedSet<K>;

	/**
	 * Implementation for method overloads of 'bcGetSubKeyHeadSet'
	 */
	public bcGetSubKeyHeadSet(__param0 : K, __param1? : boolean) : NavigableSet<K> | SortedSet<K> {
		if (((typeof __param0 !== "undefined") && (typeof __param0 !== "undefined")) && ((typeof __param1 !== "undefined") && typeof __param1 === "boolean")) {
			const toElement : K = __param0 as unknown as K;
			const inclusive : boolean = __param1 as boolean;
			return this._createSet(this._iv.from, this._iv.fromInc, toElement, inclusive, this._asc);
		} else if (((typeof __param0 !== "undefined") && (typeof __param0 !== "undefined")) && (typeof __param1 === "undefined")) {
			const toElement : K = __param0 as unknown as K;
			return this._createSet(this._iv.from, this._iv.fromInc, toElement, false, this._asc);
		} else throw new Error('invalid method overload');
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
	public bcGetSubKeyTailSet(fromElement : K, inclusive : boolean) : NavigableSet<K>;

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
	public bcGetSubKeyTailSet(fromElement : K) : SortedSet<K>;

	/**
	 * Implementation for method overloads of 'bcGetSubKeyTailSet'
	 */
	public bcGetSubKeyTailSet(__param0 : K, __param1? : boolean) : NavigableSet<K> | SortedSet<K> {
		if (((typeof __param0 !== "undefined") && (typeof __param0 !== "undefined")) && ((typeof __param1 !== "undefined") && typeof __param1 === "boolean")) {
			const fromElement : K = __param0 as unknown as K;
			const inclusive : boolean = __param1 as boolean;
			return this._createSet(fromElement, inclusive, this._iv.to, this._iv.toInc, this._asc);
		} else if (((typeof __param0 !== "undefined") && (typeof __param0 !== "undefined")) && (typeof __param1 === "undefined")) {
			const fromElement : K = __param0 as unknown as K;
			return this._createSet(fromElement, true, this._iv.to, this._iv.toInc, this._asc);
		} else throw new Error('invalid method overload');
	}

	private _createMap(from : K, fromInc : boolean, to : K, toInc : boolean, asc : boolean) : AVLMapSubMap<K, V> {
		if (this._par.bcCheckOutOfIntervall(from, fromInc, this._iv))
			throw new IllegalArgumentException("FROM-KEY " + from + "/" + fromInc + " nicht in " + this._iv)
		if (this._par.bcCheckOutOfIntervall(to, toInc, this._iv))
			throw new IllegalArgumentException("TO-KEY " + to + "/" + toInc + " nicht in " + this._iv)
		return new AVLMapSubMap(this._par, new AVLMapIntervall(from, fromInc, to, toInc), asc);
	}

	private _createSet(from : K, fromInc : boolean, to : K, toInc : boolean, asc : boolean) : AVLMapSubKeySet<K, V> {
		return new AVLMapSubKeySet(this._createMap(from, fromInc, to, toInc, asc));
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.adt.map.AVLMapSubMap', 'java.util.SequencedMap', 'java.util.Map', 'java.util.NavigableMap', 'java.util.SortedMap'].includes(name);
	}

	public reversed() : NavigableMap<K, V> {
		return this.descendingMap();
	}

	public computeIfAbsent(key : K, mappingFunction: JavaFunction<K, V> ) : V | null {
		const v : V | null = this.get(key);
		if (v != null)
			return v;
		const newValue : V = mappingFunction.apply(key);
		if (newValue == null)
			return null;
		this.put(key, newValue)
		return newValue;
	}

}

export function cast_de_svws_nrw_core_adt_map_AVLMapSubMap<K, V>(obj : unknown) : AVLMapSubMap<K, V> {
	return obj as AVLMapSubMap<K, V>;
}
