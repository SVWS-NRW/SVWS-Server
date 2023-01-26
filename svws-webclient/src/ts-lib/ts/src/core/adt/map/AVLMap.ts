import { JavaMapEntry, cast_java_util_Map_Entry } from '../../../java/util/JavaMapEntry';
import { Comparable, cast_java_lang_Comparable } from '../../../java/lang/Comparable';
import { NavigableSet, cast_java_util_NavigableSet } from '../../../java/util/NavigableSet';
import { JavaSet, cast_java_util_Set } from '../../../java/util/JavaSet';
import { NavigableMap, cast_java_util_NavigableMap } from '../../../java/util/NavigableMap';
import { AVLMapIntervall, cast_de_nrw_schule_svws_core_adt_map_AVLMapIntervall } from '../../../core/adt/map/AVLMapIntervall';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Comparator, cast_java_util_Comparator } from '../../../java/util/Comparator';
import { NullPointerException, cast_java_lang_NullPointerException } from '../../../java/lang/NullPointerException';
import { AVLMapNode, cast_de_nrw_schule_svws_core_adt_map_AVLMapNode } from '../../../core/adt/map/AVLMapNode';
import { ClassCastException, cast_java_lang_ClassCastException } from '../../../java/lang/ClassCastException';
import { SortedMap, cast_java_util_SortedMap } from '../../../java/util/SortedMap';
import { Collection, cast_java_util_Collection } from '../../../java/util/Collection';
import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaMap, cast_java_util_Map } from '../../../java/util/JavaMap';
import { IllegalArgumentException, cast_java_lang_IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';
import { NoSuchElementException, cast_java_util_NoSuchElementException } from '../../../java/util/NoSuchElementException';
import { AVLMapSubMap, cast_de_nrw_schule_svws_core_adt_map_AVLMapSubMap } from '../../../core/adt/map/AVLMapSubMap';
import { UnsupportedOperationException, cast_java_lang_UnsupportedOperationException } from '../../../java/lang/UnsupportedOperationException';

export class AVLMap<K, V> extends JavaObject implements NavigableMap<K, V> {

	/**
	 *  Ein Dummy-Element für den Schlüsselwert "-Unendlich".
	 */
	private readonly _infinityMinus : K = AVLMapIntervall._INFINITY_MINUS as unknown as K;

	/**
	 *  Ein Dummy-Element für den Schlüsselwert "+Unendlich".
	 */
	private readonly _infinityPlus : K = AVLMapIntervall._INFINITY_PLUS as unknown as K;

	/**
	 *  Ein Dummy-Element für ein Pseudo-Mapping.
	 */
	private readonly _dummyValue : V = new Object() as unknown as V;

	/**
	 *  Alle Anfragen werden an die Sub-Map delegiert. Diese hat einen Bereich von "-Unendlich" bis "+Unendlich" und
	 *  beinhaltet somit alle Elemente.
	 */
	private readonly _sub : AVLMapSubMap<K, V> = new AVLMapSubMap(this, new AVLMapIntervall(this._infinityMinus, false, this._infinityPlus, false), true);

	/**
	 *  Der {@link Comparator}, der zum Vergleichen der Schlüsselwerte genutzt wird.
	 */
	private readonly _comparator : Comparator<K>;

	/**
	 *  Der {@link Comparator}, der zum Vergleichen der Schlüsselwerte genutzt wird, wenn eine natürliche Ordnung über
	 *  das {@link Comparable} - Interface verwendet wird.
	 */
	private readonly _comparatorNatural : Comparator<K> = { compare : (key1: K, key2: K) => {
		if ((key1 === null) || (key2 === null)) 
			throw new NullPointerException()
		if (!((((key1 instanceof JavaObject) && (key1.isTranspiledInstanceOf('java.lang.Comparable')))) && (((key2 instanceof JavaObject) && (key2.isTranspiledInstanceOf('java.lang.Comparable')))))) 
			throw new ClassCastException()
		let k1 : Comparable<K> = cast_java_lang_Comparable(key1);
		return k1.compareTo(key2);
	} };

	/**
	 *  Die Wurzel des Baumes. Bei einem leeren Baum ist diese Referenz NULL.
	 */
	private _root : AVLMapNode<K, V> | null = null;

	/**
	 *  Gibt an, ob das Hinzufügen von KEYs ohne VALUE erlaubt ist. Falls TRUE, dann wird der KEY einer Pseudo-VALUE
	 *  zugeordnet.
	 */
	private _allowKeyAlone : boolean = false;


	/**
	 * Erzeugt einen leere Map, welche bei den Schlüsselwerten die natürliche Ordnung des {@link Comparable} - Interface
	 * nutzt.
	 */
	public constructor();

	/**
	 * Erstellt eine neue leere Map und nutzt dabei die angegeben Ordnung der Schlüssel.
	 * 
	 * @param comparator Die Ordnung für die Schlüssel.
	 */
	public constructor(comparator : Comparator<K>);

	/**
	 * Erstellt eine neue Map mit den Daten aus der angegebenen Map und nutzt dabei die Ordnung dieser Map.
	 * 
	 * @param map Die Map mit den Daten.
	 */
	public constructor(map : SortedMap<K, V>);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : Comparator<K> | SortedMap<K, V>) {
		super();
		if ((typeof __param0 === "undefined")) {
			this._comparator = this._comparatorNatural;
		} else if (((typeof __param0 !== "undefined") && ((typeof __param0 !== 'undefined') && (__param0 instanceof Object) && (__param0 !== null) && ('compare' in __param0) && (typeof __param0.compare === 'function')) || (__param0 === null))) {
			let comparator : Comparator<K> = cast_java_util_Comparator(__param0);
			this._comparator = comparator;
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('java.util.SortedMap'))) || (__param0 === null))) {
			let map : SortedMap<K, V> = cast_java_util_SortedMap(__param0);
			this._comparator = cast_java_util_Comparator(map.comparator());
			this._sub.putAll(map);
		} else throw new Error('invalid method overload');
	}

	public toString() : string {
		return this._sub.toString();
	}

	/**
	 * Bewirkt, dass das Hinzufügen von Keys ohne Value durch {@link AVLMapSubKeySet} erlaubt ist. Die Keys werden auf
	 * einen Dummy-Wert gemapped.
	 * 
	 * @param b Falls TRUE, dürfen KEYs ohne VALUE hinzugefügt werden.
	 */
	public allowKeyAlone(b : boolean) : void {
		this._allowKeyAlone = b;
	}

	public equals(o : unknown) : boolean {
		return JavaObject.equalsTranspiler(this._sub, (o));
	}

	public hashCode() : number {
		return JavaObject.getTranspilerHashCode(this._sub);
	}

	public comparator() : Comparator<K> {
		return this._sub.comparator();
	}

	public firstKey() : K {
		return this._sub.firstKey();
	}

	public lastKey() : K {
		return this._sub.lastKey();
	}

	public keySet() : JavaSet<K> {
		return this._sub.keySet();
	}

	public values() : Collection<V> {
		return this._sub.values();
	}

	public entrySet() : JavaSet<JavaMapEntry<K, V>> {
		return this._sub.entrySet();
	}

	public size() : number {
		return this._sub.size();
	}

	public isEmpty() : boolean {
		return this._sub.isEmpty();
	}

	public containsKey(key : unknown) : boolean {
		return this._sub.containsKey(key);
	}

	public containsValue(value : unknown) : boolean {
		return this._sub.containsValue(value);
	}

	public get(key : unknown) : V | null {
		return this._sub.get(key);
	}

	public put(key : K, value : V) : V | null {
		return this._sub.put(key, value);
	}

	public remove(key : unknown) : V | null {
		return this._sub.remove(key);
	}

	public putAll(m : JavaMap<K, V>) : void {
		this._sub.putAll(m);
	}

	public clear() : void {
		this._sub.clear();
	}

	public lowerEntry(key : K) : JavaMapEntry<K, V> | null {
		return this._sub.lowerEntry(key);
	}

	public lowerKey(key : K) : K | null {
		return this._sub.lowerKey(key);
	}

	public floorEntry(key : K) : JavaMapEntry<K, V> | null {
		return this._sub.floorEntry(key);
	}

	public floorKey(key : K) : K | null {
		return this._sub.floorKey(key);
	}

	public ceilingEntry(key : K) : JavaMapEntry<K, V> | null {
		return this._sub.ceilingEntry(key);
	}

	public ceilingKey(key : K) : K | null {
		return this._sub.ceilingKey(key);
	}

	public higherEntry(key : K) : JavaMapEntry<K, V> | null {
		return this._sub.higherEntry(key);
	}

	public higherKey(key : K) : K | null {
		return this._sub.higherKey(key);
	}

	public firstEntry() : JavaMapEntry<K, V> | null {
		return this._sub.firstEntry();
	}

	public lastEntry() : JavaMapEntry<K, V> | null {
		return this._sub.lastEntry();
	}

	public pollFirstEntry() : JavaMapEntry<K, V> | null {
		return this._sub.pollFirstEntry();
	}

	public pollLastEntry() : JavaMapEntry<K, V> | null {
		return this._sub.pollLastEntry();
	}

	public descendingMap() : NavigableMap<K, V> {
		return this._sub.descendingMap();
	}

	public navigableKeySet() : NavigableSet<K> {
		return this._sub.navigableKeySet();
	}

	public descendingKeySet() : NavigableSet<K> {
		return this._sub.descendingKeySet();
	}

	public subMap(fromKey : K, fromInclusive : boolean, toKey : K, toInclusive : boolean) : NavigableMap<K, V>;

	public subMap(fromKey : K, toKey : K) : SortedMap<K, V>;

	/**
	 * Implementation for method overloads of 'subMap'
	 */
	public subMap(__param0 : K, __param1 : K | boolean, __param2? : K, __param3? : boolean) : NavigableMap<K, V> | SortedMap<K, V> {
		if (((typeof __param0 !== "undefined") && (typeof __param0 !== "undefined")) && ((typeof __param1 !== "undefined") && typeof __param1 === "boolean") && ((typeof __param2 !== "undefined") && (typeof __param2 !== "undefined")) && ((typeof __param3 !== "undefined") && typeof __param3 === "boolean")) {
			let fromKey : K = __param0 as unknown as K;
			let fromInclusive : boolean = __param1 as boolean;
			let toKey : K = __param2 as unknown as K;
			let toInclusive : boolean = __param3 as boolean;
			return this._sub.subMap(fromKey, fromInclusive, toKey, toInclusive);
		} else if (((typeof __param0 !== "undefined") && (typeof __param0 !== "undefined")) && ((typeof __param1 !== "undefined") && (typeof __param1 !== "undefined")) && (typeof __param2 === "undefined") && (typeof __param3 === "undefined")) {
			let fromKey : K = __param0 as unknown as K;
			let toKey : K = __param1 as unknown as K;
			return this._sub.subMap(fromKey, toKey);
		} else throw new Error('invalid method overload');
	}

	public headMap(toKey : K, inclusive : boolean) : NavigableMap<K, V>;

	public headMap(toKey : K) : SortedMap<K, V>;

	/**
	 * Implementation for method overloads of 'headMap'
	 */
	public headMap(__param0 : K, __param1? : boolean) : NavigableMap<K, V> | SortedMap<K, V> {
		if (((typeof __param0 !== "undefined") && (typeof __param0 !== "undefined")) && ((typeof __param1 !== "undefined") && typeof __param1 === "boolean")) {
			let toKey : K = __param0 as unknown as K;
			let inclusive : boolean = __param1 as boolean;
			return this._sub.headMap(toKey, inclusive);
		} else if (((typeof __param0 !== "undefined") && (typeof __param0 !== "undefined")) && (typeof __param1 === "undefined")) {
			let toKey : K = __param0 as unknown as K;
			return this._sub.headMap(toKey);
		} else throw new Error('invalid method overload');
	}

	public tailMap(fromKey : K, inclusive : boolean) : NavigableMap<K, V>;

	public tailMap(fromKey : K) : SortedMap<K, V>;

	/**
	 * Implementation for method overloads of 'tailMap'
	 */
	public tailMap(__param0 : K, __param1? : boolean) : NavigableMap<K, V> | SortedMap<K, V> {
		if (((typeof __param0 !== "undefined") && (typeof __param0 !== "undefined")) && ((typeof __param1 !== "undefined") && typeof __param1 === "boolean")) {
			let fromKey : K = __param0 as unknown as K;
			let inclusive : boolean = __param1 as boolean;
			return this._sub.tailMap(fromKey, inclusive);
		} else if (((typeof __param0 !== "undefined") && (typeof __param0 !== "undefined")) && (typeof __param1 === "undefined")) {
			let fromKey : K = __param0 as unknown as K;
			return this._sub.tailMap(fromKey);
		} else throw new Error('invalid method overload');
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Fügt ein Entry der Datenstruktur hinzu.
	 * 
	 * @param e  Das einzufügende Entry.
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return TRUE, falls das Entry (e.getKey(), e.getValue()) neu war und somit hinzugefügt wurde.
	 */
	bcAddEntryReturnBool(e : JavaMapEntry<K, V>, iv : AVLMapIntervall<K>) : boolean {
		let old : V | null = this.bcAddEntryReturnOldValueOrNull(e.getKey(), e.getValue(), iv);
		return !this._valEquals(old, e.getValue());
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
	bcAddEntryReturnOldValueOrNull(key : K, value : V, iv : AVLMapIntervall<K>) : V | null {
		if (key === null) 
			throw new NullPointerException("TreeMap erlaubt keine NULL keys.")
		if (this._isOutOfRange(key, iv)) 
			throw new IllegalArgumentException("Der Schlüsselwert liegt nicht im gültigen Bereich.")
		if (this._root === null) {
			this._root = new AVLMapNode(key, value);
			return null;
		}
		let node : AVLMapNode<K, V> | null = this._nodeGetOrNull(key, iv);
		let old : V | null = (node === null) ? null : node._val;
		this._root = this._nodePutRecursive(this._root, key, value);
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
	bcAddAllEntries(c : Collection<JavaMapEntry<K, V>>, iv : AVLMapIntervall<K>) : boolean {
		let changed : boolean = false;
		for (let entry of c) 
			changed = changed || this.bcAddEntryReturnBool(entry, iv);
		return changed;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Fügt alle Entrys der übergebenen Map dieser Datenstruktur hinzu.
	 * 
	 * @param map Die Map, deren Entries dieser Datenstruktur hinzugefügt werden soll.
	 * @param iv  Das Intervall der {@link AVLMapSubMap}.
	 */
	bcAddAllEntriesOfMap(map : JavaMap<K, V>, iv : AVLMapIntervall<K>) : void {
		for (let entry of map.entrySet()) 
			this.bcAddEntryReturnOldValueOrNull(entry.getKey(), entry.getValue(), iv);
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
	bcAddKey(e : K, iv : AVLMapIntervall<K>) : boolean {
		if (this._allowKeyAlone === false) 
			throw new UnsupportedOperationException()
		if (this.bcContainsKey(e, iv)) 
			return false;
		this.bcAddEntryReturnOldValueOrNull(e, this._dummyValue, iv);
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
	bcAddAllKeys(c : Collection<K>, iv : AVLMapIntervall<K>) : boolean {
		let changed : boolean = false;
		for (let key of c) 
			changed = changed || this.bcAddKey(key, iv);
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
	bcContainsKey(objKey : unknown, iv : AVLMapIntervall<K>) : boolean {
		return this._nodeGetOrNull(objKey as unknown as K, iv) !== null;
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
	bcContainsAllKeys(c : Collection<unknown>, iv : AVLMapIntervall<K>) : boolean {
		for (let key of c) 
			if (!this.bcContainsKey(key, iv)) 
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
	bcContainsValue(objValue : unknown, iv : AVLMapIntervall<K>) : boolean {
		let value : V = objValue as unknown as V;
		let n1 : AVLMapNode<K, V> | null = this._nodeFirstOrNull(iv);
		if (n1 === null) 
			return false;
		let n2 : AVLMapNode<K, V> | null = this._nodeLastOrNull(iv);
		if (n2 === null) 
			return false;
		while (n1 as unknown !== n2 as unknown) {
			if (n1 === null) 
				throw new NullPointerException()
			if (this._valEquals(n1._val, value)) 
				return true;
			n1 = n1._next;
		}
		return this._valEquals(n2._val, value);
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
	bcContainsAllValues(c : Collection<unknown>, iv : AVLMapIntervall<K>) : boolean {
		for (let val of c) 
			if (!this.bcContainsValue(val, iv)) 
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
	bcContainsEntry(o : unknown, iv : AVLMapIntervall<K>) : boolean {
		if (((o instanceof JavaObject) && (o.isTranspiledInstanceOf('java.util.Map.Entry'))) === false) 
			return false;
		let e : JavaMapEntry<K, V> = cast_java_util_Map_Entry(o);
		let node : AVLMapNode<K, V> | null = this._nodeGetOrNull(e.getKey(), iv);
		return (node === null) ? false : this._valEquals(node._val, e.getValue());
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
	bcContainsAllEntries(c : Collection<unknown>, iv : AVLMapIntervall<K>) : boolean {
		for (let entry of c) 
			if (!this.bcContainsEntry(entry, iv)) 
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
	bcRemoveKeyReturnOldValueOrNull(obj : unknown, iv : AVLMapIntervall<K>) : V | null {
		if (obj === null) 
			throw new NullPointerException("TreeMap unterstützt keine NULL-Schlüssel.")
		let key : K = obj as unknown as K;
		let old : AVLMapNode<K, V> | null = this._nodeGetOrNull(key, iv);
		if (old === null) 
			return null;
		if (this._root === null) 
			throw new NullPointerException()
		this._root = this._nodeRemoveKeyRecursive(this._root, key);
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
	bcRemoveKeyReturnBool(o : unknown, iv : AVLMapIntervall<K>) : boolean {
		if (!this.bcContainsKey(o, iv)) 
			return false;
		this.bcRemoveKeyReturnOldValueOrNull(o, iv);
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
	bcRemoveAllKeys(c : Collection<unknown>, iv : AVLMapIntervall<K>) : boolean {
		let changed : boolean = false;
		for (let obj of c) 
			changed = changed || this.bcRemoveKeyReturnBool(obj, iv);
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
	bcRemoveEntry(o : unknown, iv : AVLMapIntervall<K>) : boolean {
		if (((o instanceof JavaObject) && (o.isTranspiledInstanceOf('java.util.Map.Entry'))) === false) 
			return false;
		if (!this.bcContainsEntry(o, iv)) 
			return false;
		if (this._root === null) 
			throw new NullPointerException()
		let e : JavaMapEntry<K, V> = cast_java_util_Map_Entry(o);
		this._root = this._nodeRemoveKeyRecursive(this._root, e.getKey());
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
	bcRemoveAllEntries(c : Collection<unknown>, iv : AVLMapIntervall<K>) : boolean {
		let removedAny : boolean = false;
		for (let entry of c) 
			removedAny = removedAny || this.bcRemoveEntry(entry, iv);
		return removedAny;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Entfernt und liefert das erste Entry dieser Datenstruktur.
	 * 
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Entfernt und liefert das erste Entry dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	bcPollFirstEntryOrNull(iv : AVLMapIntervall<K>) : JavaMapEntry<K, V> | null {
		let node : AVLMapNode<K, V> | null = this._nodeFirstOrNull(iv);
		if (node === null) 
			return null;
		if (this._root === null) 
			throw new NullPointerException()
		this._root = this._nodeRemoveKeyRecursive(this._root, node._key);
		return node;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Entfernt und liefert den ersten Schlüssel (Key) dieser Datenstruktur.
	 * 
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Entfernt und liefert den ersten Schlüssel (Key) dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	bcPollFirstKeyOrNull(iv : AVLMapIntervall<K>) : K | null {
		let node : AVLMapNode<K, V> | null = this._nodeFirstOrNull(iv);
		if (node === null) 
			return null;
		if (this._root === null) 
			throw new NullPointerException()
		this._root = this._nodeRemoveKeyRecursive(this._root, node._key);
		return node._key;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Entfernt und liefert das letzte Entry dieser Datenstruktur.
	 * 
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Entfernt und liefert das letzte Entry dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	bcPollLastEntryOrNull(iv : AVLMapIntervall<K>) : JavaMapEntry<K, V> | null {
		let node : AVLMapNode<K, V> | null = this._nodeLastOrNull(iv);
		if (node === null) 
			return null;
		if (this._root === null) 
			throw new NullPointerException()
		this._root = this._nodeRemoveKeyRecursive(this._root, node._key);
		return node;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Entfernt und liefert den letzten Schlüssel (Key) dieser Datenstruktur.
	 * 
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Entfernt und liefert den letzten Schlüssel (Key) dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	bcPollLastKeyOrNull(iv : AVLMapIntervall<K>) : K | null {
		let node : AVLMapNode<K, V> | null = this._nodeLastOrNull(iv);
		if (node === null) 
			return null;
		if (this._root === null) 
			throw new NullPointerException()
		this._root = this._nodeRemoveKeyRecursive(this._root, node._key);
		return node._key;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Liefert die Anzahl der Elemente innerhalb des übergebenen Intervalls.
	 * 
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Liefert die Anzahl der Elemente innerhalb des übergebenen Intervalls.
	 */
	bcGetSize(iv : AVLMapIntervall<K>) : number {
		let n1 : AVLMapNode<K, V> | null = this._nodeFirstOrNull(iv);
		if (n1 === null) 
			return 0;
		let n2 : AVLMapNode<K, V> | null = this._nodeLastOrNull(iv);
		if (n2 === null) 
			return 0;
		return this._nodeIndexOf(n2._key) - this._nodeIndexOf(n1._key) + 1;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Überprüft, ob die Datenstruktur innerhalb des Intervalls leer ist.
	 * 
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return TRUE, falls die Datenstruktur innerhalb des Intervalls leer ist.
	 */
	bcIsEmpty(iv : AVLMapIntervall<K>) : boolean {
		return this._nodeFirstOrNull(iv) === null;
	}

	/**
	 * Liefert den Comparator dieser Map.
	 * 
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Liefert den Comparator dieser Map.
	 */
	bcGetComparator(iv : AVLMapIntervall<K>) : Comparator<K> {
		return this._comparator;
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Liefert das erste Entry dieser Datenstruktur.
	 * 
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Das erste Entry dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	bcGetFirstEntryOrNull(iv : AVLMapIntervall<K>) : AVLMapNode<K, V> | null {
		return this._nodeFirstOrNull(iv);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Liefert den ersten Schlüssel (Key) dieser Datenstruktur.
	 * 
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Liefert den ersten Schlüssel (Key) dieser Datenstruktur, falls vorhanden.
	 * @throws NoSuchElementException falls es kein erstes Element gibt.
	 */
	bcGetFirstKeyOrException(iv : AVLMapIntervall<K>) : K {
		return this._keyOrExeption(this._nodeFirstOrNull(iv));
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Liefert das letzte Entry dieser Datenstruktur.
	 * 
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Liefert das letzte Entry dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	bcGetLastEntryOrNull(iv : AVLMapIntervall<K>) : AVLMapNode<K, V> | null {
		return this._nodeLastOrNull(iv);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Liefert den letzten Schlüssel (Key) dieser Datenstruktur.
	 * 
	 * @param iv Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Liefert den letzten Schlüssel (Key) dieser Datenstruktur falls vorhanden.
	 * @throws NoSuchElementException falls es kein letztes Element gibt.
	 */
	bcGetLastKeyOrException(iv : AVLMapIntervall<K>) : K {
		return this._keyOrExeption(this._nodeLastOrNull(iv));
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Liefert das nächste Entry dieser Datenstruktur.
	 * 
	 * @param current Das aktuelle Entry .
	 * @param iv      Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Liefert das nächste Entry dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	bcGetNextEntryOrNull(current : AVLMapNode<K, V>, iv : AVLMapIntervall<K>) : AVLMapNode<K, V> | null {
		return this._nodeNextOrNull(current, iv);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Liefert das vorherige Entry dieser Datenstruktur.
	 * 
	 * @param current Das aktuelle Entry.
	 * @param iv      Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Liefert das vorherige Entry dieser Datenstruktur falls vorhanden, andernfalls NULL.
	 */
	bcGetPrevEntryOrNull(current : AVLMapNode<K, V>, iv : AVLMapIntervall<K>) : AVLMapNode<K, V> | null {
		return this._nodePrevOrNull(current, iv);
	}

	/**
	 * Wird aufgerufen von {@link AVLMapSubMap}. Liefert den Wert (Value) eines bestimmten Schlüssels (Key).
	 * 
	 * @param objKey Der Schlüssel (Key) dessen Wert (Value) angefordert wird.
	 * @param iv     Das Intervall der {@link AVLMapSubMap}.
	 * 
	 * @return Den Wert (Value) eines bestimmten Schlüssels (Key) falls vorhanden, sonst NULL.
	 */
	bcGetValueOfKeyOrNull(objKey : unknown, iv : AVLMapIntervall<K>) : V | null {
		let key : K = objKey as unknown as K;
		let node : AVLMapNode<K, V> | null = this._nodeGetOrNull(key, iv);
		return (node === null) ? null : node._val;
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
	bcGetLowerEntryOrNull(key : K, iv : AVLMapIntervall<K>) : AVLMapNode<K, V> | null {
		return this._nodeLowerOrNull(key, iv);
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
	bcGetLowerKeyOrNull(key : K, iv : AVLMapIntervall<K>) : K | null {
		return this._keyOrNull(this._nodeLowerOrNull(key, iv));
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
	bcGetFloorEntryOrNull(key : K, iv : AVLMapIntervall<K>) : AVLMapNode<K, V> | null {
		return this._nodeFloorOrNull(key, iv);
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
	bcGetFloorKeyOrNull(key : K, iv : AVLMapIntervall<K>) : K | null {
		return this._keyOrNull(this._nodeFloorOrNull(key, iv));
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
	bcGetCeilingEntryOrNull(key : K, iv : AVLMapIntervall<K>) : AVLMapNode<K, V> | null {
		return this._nodeCeilingOrNull(key, iv);
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
	bcGetCeilingKeyOrNull(key : K, iv : AVLMapIntervall<K>) : K | null {
		return this._keyOrNull(this._nodeCeilingOrNull(key, iv));
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
	bcGetHigherEntryOrNull(key : K, iv : AVLMapIntervall<K>) : AVLMapNode<K, V> | null {
		return this._nodeHigherOrNull(key, iv);
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
	bcGetHigherKeyOrNull(key : K, iv : AVLMapIntervall<K>) : K | null {
		return this._keyOrNull(this._nodeHigherOrNull(key, iv));
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
	bcCheckOutOfIntervall(key : K, inc : boolean, iv : AVLMapIntervall<K>) : boolean {
		if ((key === this._infinityMinus) || (key === this._infinityPlus)) 
			return false;
		let cmpF : number = this._compare(key, iv.from);
		if (cmpF < 0) 
			return true;
		if ((cmpF === 0) && (!iv.fromInc) && (inc)) 
			return true;
		let cmpT : number = this._compare(key, iv.to);
		if (cmpT > 0) 
			return true;
		if ((cmpT === 0) && (!iv.toInc) && (inc)) 
			return true;
		return false;
	}

	private _keyOrNull(node : AVLMapNode<K, V> | null) : K | null {
		return (node === null) ? null : node._key;
	}

	private _valEquals(v1 : V | null, v2 : V | null) : boolean {
		return (v1 === null) ? v2 === null : JavaObject.equalsTranspiler(v1, (v2));
	}

	private _keyOrExeption(node : AVLMapNode<K, V> | null) : K {
		if (node === null) 
			throw new NoSuchElementException()
		return node._key;
	}

	private _compare(key1 : K, key2 : K) : number {
		if ((key1 === this._infinityMinus) || (key2 === this._infinityPlus)) 
			return -1;
		if ((key1 === this._infinityPlus) || (key2 === this._infinityMinus)) 
			return +1;
		return this._comparator.compare(key1, key2);
	}

	private _isOutOfRange(key : K, iv : AVLMapIntervall<K>) : boolean {
		let cmpKeyFrom : number = this._compare(key, iv.from);
		if ((cmpKeyFrom < 0) || (cmpKeyFrom === 0) && (!iv.fromInc)) 
			return true;
		let cmpKeyTo : number = this._compare(key, iv.to);
		if ((cmpKeyTo > 0) || (cmpKeyTo === 0) && (!iv.toInc)) 
			return true;
		return false;
	}

	private _nodeFirstOrNull(iv : AVLMapIntervall<K>) : AVLMapNode<K, V> | null {
		return iv.fromInc ? this._nodeCeilingOrNull(iv.from, iv) : this._nodeHigherOrNull(iv.from, iv);
	}

	private _nodeLastOrNull(iv : AVLMapIntervall<K>) : AVLMapNode<K, V> | null {
		return iv.toInc ? this._nodeFloorOrNull(iv.to, iv) : this._nodeLowerOrNull(iv.to, iv);
	}

	private _nodeCeilingOrNull(key : K, iv : AVLMapIntervall<K>) : AVLMapNode<K, V> | null {
		let node : AVLMapNode<K, V> | null = this._nodeDeepestOrNull(key, iv);
		if (node === null) 
			return null;
		let cmpNodeKey : number = this._compare(node._key, key);
		return cmpNodeKey >= 0 ? node : this._nodeNextOrNull(node, iv);
	}

	private _nodeHigherOrNull(key : K, iv : AVLMapIntervall<K>) : AVLMapNode<K, V> | null {
		let node : AVLMapNode<K, V> | null = this._nodeDeepestOrNull(key, iv);
		if (node === null) 
			return null;
		let cmpNodeKey : number = this._compare(node._key, key);
		return cmpNodeKey > 0 ? node : this._nodeNextOrNull(node, iv);
	}

	private _nodeFloorOrNull(key : K, iv : AVLMapIntervall<K>) : AVLMapNode<K, V> | null {
		let node : AVLMapNode<K, V> | null = this._nodeDeepestOrNull(key, iv);
		if (node === null) 
			return null;
		let cmpNodeKey : number = this._compare(node._key, key);
		return cmpNodeKey <= 0 ? node : this._nodePrevOrNull(node, iv);
	}

	private _nodeLowerOrNull(key : K, iv : AVLMapIntervall<K>) : AVLMapNode<K, V> | null {
		let node : AVLMapNode<K, V> | null = this._nodeDeepestOrNull(key, iv);
		if (node === null) 
			return null;
		let cmpNodeKey : number = this._compare(node._key, key);
		return cmpNodeKey < 0 ? node : this._nodePrevOrNull(node, iv);
	}

	private _nodeNextOrNull(node : AVLMapNode<K, V>, iv : AVLMapIntervall<K>) : AVLMapNode<K, V> | null {
		let next : AVLMapNode<K, V> | null = node._next;
		return (next === null) ? null : this._isOutOfRange(next._key, iv) ? null : next;
	}

	private _nodePrevOrNull(node : AVLMapNode<K, V>, iv : AVLMapIntervall<K>) : AVLMapNode<K, V> | null {
		let prev : AVLMapNode<K, V> | null = node._prev;
		return (prev === null) ? null : this._isOutOfRange(prev._key, iv) ? null : prev;
	}

	private _nodeGetOrNull(key : K, iv : AVLMapIntervall<K>) : AVLMapNode<K, V> | null {
		let node : AVLMapNode<K, V> | null = this._nodeDeepestOrNull(key, iv);
		if (node === null) 
			return null;
		return this._compare(key, node._key) === 0 ? node : null;
	}

	private _nodeIndexOf(key : K) : number {
		let current : AVLMapNode<K, V> | null = this._root;
		let index : number = 0;
		while (true) {
			if (current === null) 
				throw new NullPointerException()
			let cmp : number = this._compare(key, current._key);
			if (cmp < 0) {
				current = current._childL;
				continue;
			}
			let left : AVLMapNode<K, V> | null = current._childL;
			let sizeL : number = (left === null) ? 0 : left._size;
			if (cmp > 0) {
				index += sizeL + 1;
				current = current._childR;
				continue;
			}
			return index + sizeL;
		}
	}

	private _nodeDeepestOrNull(key : K, iv : AVLMapIntervall<K>) : AVLMapNode<K, V> | null {
		let current : AVLMapNode<K, V> | null = this._root;
		let last : AVLMapNode<K, V> | null = null;
		while (current !== null) {
			let cmpToKey : number = this._compare(iv.to, current._key);
			if ((cmpToKey < 0) || (cmpToKey === 0) && (!iv.toInc)) {
				current = current._childL;
				continue;
			}
			let cmpFromKey : number = this._compare(iv.from, current._key);
			if ((cmpFromKey > 0) || (cmpFromKey === 0) && (!iv.fromInc)) {
				current = current._childR;
				continue;
			}
			last = current;
			let cmp : number = this._compare(key, current._key);
			if (cmp < 0) {
				current = current._childL;
				continue;
			}
			if (cmp > 0) {
				current = current._childR;
				continue;
			}
			return current;
		}
		return last;
	}

	private _nodePutRecursive(current : AVLMapNode<K, V>, key : K, value : V) : AVLMapNode<K, V> {
		let cmp : number = this._compare(key, current._key);
		if (cmp === 0) {
			current._val = value;
			return current;
		}
		if (cmp < 0) 
			current._childL = (current._childL === null) ? this._nodeCreateLeaf(current._prev, current, key, value) : this._nodePutRecursive(current._childL, key, value); else 
			current._childR = (current._childR === null) ? this._nodeCreateLeaf(current, current._next, key, value) : this._nodePutRecursive(current._childR, key, value);
		return this._nodeRevalidate(current);
	}

	private _nodeCreateLeaf(prev : AVLMapNode<K, V> | null, next : AVLMapNode<K, V> | null, key : K, value : V) : AVLMapNode<K, V> {
		let child : AVLMapNode<K, V> | null = new AVLMapNode(key, value);
		if (prev !== null) {
			prev._next = child;
			child._prev = prev;
		}
		if (next !== null) {
			next._prev = child;
			child._next = next;
		}
		return child;
	}

	private _nodeRemoveKeyRecursive(current : AVLMapNode<K, V>, key : K) : AVLMapNode<K, V> | null {
		let cmp : number = this._compare(key, current._key);
		if (cmp < 0) {
			if (current._childL === null) 
				throw new NullPointerException()
			current._childL = this._nodeRemoveKeyRecursive(current._childL, key);
			return this._nodeRevalidate(current);
		}
		if (cmp > 0) {
			if (current._childR === null) 
				throw new NullPointerException()
			current._childR = this._nodeRemoveKeyRecursive(current._childR, key);
			return this._nodeRevalidate(current);
		}
		if (current._childL === null) {
			this._nodeRemovePrevNext(current);
			return current._childR;
		}
		if (current._childR === null) {
			this._nodeRemovePrevNext(current);
			return current._childL;
		}
		let next : AVLMapNode<K, V> | null = current._next;
		if (next === null) 
			throw new NullPointerException()
		current._childR = this._nodeRemoveKeyRecursive(current._childR, next._key);
		return this._nodeRevalidate(this._nodeReplaceReferencesFromAwithB(next, current));
	}

	private _nodeReplaceReferencesFromAwithB(a : AVLMapNode<K, V>, b : AVLMapNode<K, V>) : AVLMapNode<K, V> {
		a._childL = b._childL;
		a._childR = b._childR;
		let p : AVLMapNode<K, V> | null = b._prev;
		let n : AVLMapNode<K, V> | null = b._next;
		a._prev = p;
		a._next = n;
		if (p !== null) 
			p._next = a;
		if (n !== null) 
			n._prev = a;
		return a;
	}

	private _nodeRemovePrevNext(current : AVLMapNode<K, V>) : void {
		let nodeP : AVLMapNode<K, V> | null = current._prev;
		let nodeN : AVLMapNode<K, V> | null = current._next;
		if (nodeP !== null) 
			nodeP._next = nodeN;
		if (nodeN !== null) 
			nodeN._prev = nodeP;
	}

	/**
	 * Aktualisiert {@link node} und liefert, wenn es zur Rotation kommt, eine neue Sub-Wurzel.
	 * 
	 * @param node Der Knoten, der revalidiert werden soll.
	 * 
	 * @return node, oder die neue Sub-Wurzel, wenn es zur Rotation kam.
	 */
	private _nodeRevalidate(node : AVLMapNode<K, V>) : AVLMapNode<K, V> {
		let heightBalance : number = this._nodeGetHeightBalance(node);
		if (heightBalance > +1) {
			if (node._childR === null) 
				throw new NullPointerException()
			if (this._nodeGetHeightBalance(node._childR) < 0) 
				node._childR = this._nodeRotateRight(node._childR);
			return this._nodeRotateLeft(node);
		}
		if (heightBalance < -1) {
			if (node._childL === null) 
				throw new NullPointerException()
			if (this._nodeGetHeightBalance(node._childL) > 0) 
				node._childL = this._nodeRotateLeft(node._childL);
			return this._nodeRotateRight(node);
		}
		this._nodeRevalidateHeightAndSize(node);
		return node;
	}

	private _nodeRotateLeft(nodeM : AVLMapNode<K, V>) : AVLMapNode<K, V> {
		if (nodeM._childR === null) 
			throw new NullPointerException()
		let nodeR : AVLMapNode<K, V> = nodeM._childR;
		nodeM._childR = nodeR._childL;
		nodeR._childL = nodeM;
		this._nodeRevalidateHeightAndSize(nodeM);
		this._nodeRevalidateHeightAndSize(nodeR);
		return nodeR;
	}

	private _nodeRotateRight(nodeM : AVLMapNode<K, V>) : AVLMapNode<K, V> {
		if (nodeM._childL === null) 
			throw new NullPointerException()
		let nodeL : AVLMapNode<K, V> = nodeM._childL;
		nodeM._childL = nodeL._childR;
		nodeL._childR = nodeM;
		this._nodeRevalidateHeightAndSize(nodeM);
		this._nodeRevalidateHeightAndSize(nodeL);
		return nodeL;
	}

	private _nodeRevalidateHeightAndSize(node : AVLMapNode<K, V>) : void {
		let sizeL : number = (node._childL === null) ? 0 : node._childL._size;
		let sizeR : number = (node._childR === null) ? 0 : node._childR._size;
		node._size = sizeL + sizeR + 1;
		let heightL : number = (node._childL === null) ? 0 : node._childL._height;
		let heightR : number = (node._childR === null) ? 0 : node._childR._height;
		node._height = Math.max(heightL, heightR) + 1;
	}

	private _nodeGetHeightBalance(node : AVLMapNode<K, V>) : number {
		let heightL : number = (node._childL === null) ? 0 : node._childL._height;
		let heightR : number = (node._childR === null) ? 0 : node._childR._height;
		return heightR - heightL;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['java.util.Map', 'de.nrw.schule.svws.core.adt.map.AVLMap', 'java.util.NavigableMap', 'java.util.SortedMap'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_adt_map_AVLMap<K, V>(obj : unknown) : AVLMap<K, V> {
	return obj as AVLMap<K, V>;
}
