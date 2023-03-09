import { NavigableSet, cast_java_util_NavigableSet } from '../../../java/util/NavigableSet';
import { JavaIterator, cast_java_util_Iterator } from '../../../java/util/JavaIterator';
import { Collection, cast_java_util_Collection } from '../../../java/util/Collection';
import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { SortedSet, cast_java_util_SortedSet } from '../../../java/util/SortedSet';
import { AVLMapSubMap, cast_de_nrw_schule_svws_core_adt_map_AVLMapSubMap } from './AVLMapSubMap';
import { Comparator, cast_java_util_Comparator } from '../../../java/util/Comparator';

export class AVLMapSubKeySet<K, V> extends JavaObject implements NavigableSet<K> {

	/**
	 *  Die {@link AVLMap} auf der dieses Sub-Set operiert.
	 */
	private readonly _sub : AVLMapSubMap<K, V>;


	/**
	 * Erstellt eine neues Sub-Set auf die übergebene {@link AVLMap}.
	 * 
	 * @param sub Die {@link AVLMap} auf der operiert wird.
	 */
	constructor(sub : AVLMapSubMap<K, V>) {
		super();
		this._sub = sub;
	}

	public toString() : string {
		return this._sub.toString();
	}

	public comparator() : Comparator<Partial<K>> {
		return this._sub.comparator();
	}

	public first() : K {
		return this._sub.firstKey();
	}

	public last() : K {
		return this._sub.lastKey();
	}

	public size() : number {
		return this._sub.size();
	}

	public isEmpty() : boolean {
		return this._sub.isEmpty();
	}

	public contains(o : unknown) : boolean {
		return this._sub.containsKey(o);
	}

	public toArray() : Array<unknown>;

	public toArray<T>(a : Array<T>) : Array<T>;

	/**
	 * Implementation for method overloads of 'toArray'
	 */
	public toArray<T>(__param0? : Array<T>) : Array<T> | Array<unknown> {
		if ((typeof __param0 === "undefined")) {
			return this._sub.bcGetVectorOfKeys().toArray();
		} else if (((typeof __param0 !== "undefined") && Array.isArray(__param0))) {
			let a : Array<T> = __param0;
			return this._sub.bcGetVectorOfKeys().toArray(a);
		} else throw new Error('invalid method overload');
	}

	public add(e : K) : boolean {
		return this._sub.bcAddKey(e);
	}

	public remove(o : unknown) : boolean {
		return this._sub.bcRemoveKeyReturnBool(o);
	}

	public containsAll(c : Collection<unknown>) : boolean {
		return this._sub.bcContainsAllKeys(c);
	}

	public addAll(c : Collection<K>) : boolean {
		return this._sub.bcAddAllKeys(c);
	}

	public retainAll(c : Collection<unknown>) : boolean {
		return this._sub.bcRetainAllKeys(c);
	}

	public removeAll(c : Collection<unknown>) : boolean {
		return this._sub.bcRemoveAllKeys(c);
	}

	public clear() : void {
		this._sub.clear();
	}

	public lower(e : K) : K | null {
		return this._sub.bcGetLowerKeyOrNull(e);
	}

	public floor(e : K) : K | null {
		return this._sub.bcGetFloorKeyOrNull(e);
	}

	public ceiling(e : K) : K | null {
		return this._sub.bcGetCeilingKeyOrNull(e);
	}

	public higher(e : K) : K | null {
		return this._sub.bcGetHigherKeyOrNull(e);
	}

	public pollFirst() : K | null {
		return this._sub.bcPollFirstKeyOrNull();
	}

	public pollLast() : K | null {
		return this._sub.bcPollLastKeyOrNull();
	}

	public iterator() : JavaIterator<K> {
		return this._sub.bcGetSubKeySetIterator();
	}

	public descendingSet() : NavigableSet<K> {
		return this._sub.bcGetSubKeySetDescending();
	}

	public descendingIterator() : JavaIterator<K> {
		return this._sub.bcGetSubKeySetDescendingIterator();
	}

	public subSet(fromElement : K, fromInclusive : boolean, toElement : K, toInclusive : boolean) : NavigableSet<K>;

	public subSet(fromElement : K, toElement : K) : SortedSet<K>;

	/**
	 * Implementation for method overloads of 'subSet'
	 */
	public subSet(__param0 : K, __param1 : K | boolean, __param2? : K, __param3? : boolean) : NavigableSet<K> | SortedSet<K> {
		if (((typeof __param0 !== "undefined") && (typeof __param0 !== "undefined")) && ((typeof __param1 !== "undefined") && typeof __param1 === "boolean") && ((typeof __param2 !== "undefined") && (typeof __param2 !== "undefined")) && ((typeof __param3 !== "undefined") && typeof __param3 === "boolean")) {
			let fromElement : K = __param0 as unknown as K;
			let fromInclusive : boolean = __param1 as boolean;
			let toElement : K = __param2 as unknown as K;
			let toInclusive : boolean = __param3 as boolean;
			return this._sub.bcGetSubKeySet(fromElement, fromInclusive, toElement, toInclusive);
		} else if (((typeof __param0 !== "undefined") && (typeof __param0 !== "undefined")) && ((typeof __param1 !== "undefined") && (typeof __param1 !== "undefined")) && (typeof __param2 === "undefined") && (typeof __param3 === "undefined")) {
			let fromElement : K = __param0 as unknown as K;
			let toElement : K = __param1 as unknown as K;
			return this._sub.bcGetSubKeySet(fromElement, toElement);
		} else throw new Error('invalid method overload');
	}

	public headSet(toElement : K, inclusive : boolean) : NavigableSet<K>;

	public headSet(toElement : K) : SortedSet<K>;

	/**
	 * Implementation for method overloads of 'headSet'
	 */
	public headSet(__param0 : K, __param1? : boolean) : NavigableSet<K> | SortedSet<K> {
		if (((typeof __param0 !== "undefined") && (typeof __param0 !== "undefined")) && ((typeof __param1 !== "undefined") && typeof __param1 === "boolean")) {
			let toElement : K = __param0 as unknown as K;
			let inclusive : boolean = __param1 as boolean;
			return this._sub.bcGetSubKeyHeadSet(toElement, inclusive);
		} else if (((typeof __param0 !== "undefined") && (typeof __param0 !== "undefined")) && (typeof __param1 === "undefined")) {
			let toElement : K = __param0 as unknown as K;
			return this._sub.bcGetSubKeyHeadSet(toElement);
		} else throw new Error('invalid method overload');
	}

	public tailSet(fromElement : K, inclusive : boolean) : NavigableSet<K>;

	public tailSet(fromElement : K) : SortedSet<K>;

	/**
	 * Implementation for method overloads of 'tailSet'
	 */
	public tailSet(__param0 : K, __param1? : boolean) : NavigableSet<K> | SortedSet<K> {
		if (((typeof __param0 !== "undefined") && (typeof __param0 !== "undefined")) && ((typeof __param1 !== "undefined") && typeof __param1 === "boolean")) {
			let fromElement : K = __param0 as unknown as K;
			let inclusive : boolean = __param1 as boolean;
			return this._sub.bcGetSubKeyTailSet(fromElement, inclusive);
		} else if (((typeof __param0 !== "undefined") && (typeof __param0 !== "undefined")) && (typeof __param1 === "undefined")) {
			let fromElement : K = __param0 as unknown as K;
			return this._sub.bcGetSubKeyTailSet(fromElement);
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['java.util.SortedSet', 'java.util.Collection', 'de.nrw.schule.svws.core.adt.map.AVLMapSubKeySet', 'java.util.Set', 'java.util.NavigableSet', 'java.lang.Iterable'].includes(name);
	}

	public [Symbol.iterator](): Iterator<K> {
		let iter : JavaIterator<K> = this.iterator();
		const result : Iterator<K> = {
			next() : IteratorResult<K> {
				if (iter.hasNext())
					return { value : iter.next(), done : false };
				return { value : null, done : true };
			}
		};
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_adt_map_AVLMapSubKeySet<K, V>(obj : unknown) : AVLMapSubKeySet<K, V> {
	return obj as AVLMapSubKeySet<K, V>;
}
