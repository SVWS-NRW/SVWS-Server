import { JavaMapEntry, cast_java_util_Map_Entry } from '../../../java/util/JavaMapEntry';
import { JavaSet, cast_java_util_Set } from '../../../java/util/JavaSet';
import { JavaIterator, cast_java_util_Iterator } from '../../../java/util/JavaIterator';
import { Collection, cast_java_util_Collection } from '../../../java/util/Collection';
import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { JavaMap, cast_java_util_Map } from '../../../java/util/JavaMap';
import { AVLMapSubMap, cast_de_nrw_schule_svws_core_adt_map_AVLMapSubMap } from '../../../core/adt/map/AVLMapSubMap';

export class AVLMapSubEntrySet<K, V> extends JavaObject implements JavaSet<JavaMapEntry<K, V>> {

	/**
	 *  Die {@link AVLMapSubMap} auf der diese Sup-Map operiert.
	 */
	private readonly _sub : AVLMapSubMap<K, V>;


	/**
	 * Erstellt ein neues SubEntrySet auf die übergebene {@link AVLMap}.
	 * 
	 * @param sub Die {@link AVLMapSubMap} auf der operiert wird.
	 */
	constructor(sub : AVLMapSubMap<K, V>) {
		super();
		this._sub = sub;
	}

	public toString() : string {
		return this._sub.toString();
	}

	public size() : number {
		return this._sub.size();
	}

	public isEmpty() : boolean {
		return this._sub.isEmpty();
	}

	public contains(o : unknown) : boolean {
		return this._sub.bcContainsEntry(o);
	}

	public iterator() : JavaIterator<JavaMapEntry<K, V>> {
		return this._sub.bcGetSubEntrySetIterator();
	}

	public toArray() : Array<unknown | null>;

	public toArray<T>(a : Array<T | null>) : Array<T | null>;

	/**
	 * Implementation for method overloads of 'toArray'
	 */
	public toArray<T>(__param0? : Array<T | null>) : Array<T | null> | Array<unknown | null> {
		if ((typeof __param0 === "undefined")) {
			return this._sub.bcGetVectorOfEntries().toArray();
		} else if (((typeof __param0 !== "undefined") && Array.isArray(__param0))) {
			let a : Array<T | null> = __param0;
			return this._sub.bcGetVectorOfEntries().toArray(a);
		} else throw new Error('invalid method overload');
	}

	public add(e : JavaMapEntry<K, V>) : boolean {
		return this._sub.bcAddEntryReturnBool(e);
	}

	public remove(o : unknown) : boolean {
		return this._sub.bcRemoveEntry(o);
	}

	public containsAll(c : Collection<unknown>) : boolean {
		return this._sub.bcContainsAllEntries(c);
	}

	public addAll(c : Collection<JavaMapEntry<K, V>>) : boolean {
		return this._sub.bcAddAllEntries(c);
	}

	public retainAll(c : Collection<unknown>) : boolean {
		return this._sub.bcRetainAllEntries(c);
	}

	public removeAll(c : Collection<unknown>) : boolean {
		return this._sub.bcRemoveAllEntries(c);
	}

	public clear() : void {
		this._sub.clear();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['java.util.Collection', 'java.util.Set', 'java.lang.Iterable', 'de.nrw.schule.svws.core.adt.map.AVLMapSubEntrySet'].includes(name);
	}

	public [Symbol.iterator](): Iterator<JavaMapEntry<any, any>> {
		let iter : JavaIterator<JavaMapEntry<any, any>> = this.iterator();
		const result : Iterator<JavaMapEntry<any, any>> = {
			next() : IteratorResult<JavaMapEntry<any, any>> {
				if (iter.hasNext())
					return { value : iter.next(), done : false };
				return { value : null, done : true };
			}
		};
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_adt_map_AVLMapSubEntrySet<K, V>(obj : unknown) : AVLMapSubEntrySet<K, V> {
	return obj as AVLMapSubEntrySet<K, V>;
}
