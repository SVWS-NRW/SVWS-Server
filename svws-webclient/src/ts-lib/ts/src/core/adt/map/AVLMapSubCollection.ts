import { JavaIterator, cast_java_util_Iterator } from '../../../java/util/JavaIterator';
import { Collection, cast_java_util_Collection } from '../../../java/util/Collection';
import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { AVLMapSubMap, cast_de_nrw_schule_svws_core_adt_map_AVLMapSubMap } from '../../../core/adt/map/AVLMapSubMap';
import { UnsupportedOperationException, cast_java_lang_UnsupportedOperationException } from '../../../java/lang/UnsupportedOperationException';

export class AVLMapSubCollection<K, V> extends JavaObject implements Collection<V> {

	private readonly _sub : AVLMapSubMap<K, V>;


	/**
	 * Erstellt eine neue Sub-Collection zur übergebenen {@link AVLMapSubMap}.
	 * 
	 * @param sub Die {@link AVLMapSubMap} auf der diese Sub-Collection operiert.
	 */
	constructor(sub : AVLMapSubMap<K, V>) {
		super();
		this._sub = sub;
	}

	public toString() : String {
		let s : String | null = "";
		for (let value of this) 
			s += (s.length === 0 ? "" : ", ") + value;
		return "values = [" + s.valueOf() + "], size = " + this.size() + " --> " + this._sub.toString().valueOf();
	}

	public size() : number {
		return this._sub.size();
	}

	public isEmpty() : boolean {
		return this._sub.isEmpty();
	}

	public contains(o : unknown) : boolean {
		return this._sub.containsValue(o);
	}

	public iterator() : JavaIterator<V> {
		return this._sub.bcGetSubCollectionIterator();
	}

	public toArray() : Array<unknown | null>;

	public toArray<T>(a : Array<T | null>) : Array<T | null>;

	/**
	 * Implementation for method overloads of 'toArray'
	 */
	public toArray<T>(__param0? : Array<T | null>) : Array<T | null> | Array<unknown | null> {
		if ((typeof __param0 === "undefined")) {
			return this._sub.bcGetVectorOfValues().toArray();
		} else if (((typeof __param0 !== "undefined") && Array.isArray(__param0))) {
			let a : Array<T | null> = __param0;
			return this._sub.bcGetVectorOfValues().toArray(a);
		} else throw new Error('invalid method overload');
	}

	public add(e : V) : boolean {
		throw new UnsupportedOperationException()
	}

	public remove(o : unknown) : boolean {
		throw new UnsupportedOperationException()
	}

	public containsAll(c : Collection<unknown>) : boolean {
		return this._sub.bcContainsAllValues(c);
	}

	public addAll(c : Collection<V>) : boolean {
		throw new UnsupportedOperationException()
	}

	public removeAll(c : Collection<unknown>) : boolean {
		throw new UnsupportedOperationException()
	}

	public retainAll(c : Collection<unknown>) : boolean {
		throw new UnsupportedOperationException()
	}

	public clear() : void {
		this._sub.clear();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.adt.map.AVLMapSubCollection', 'java.util.Collection', 'java.lang.Iterable'].includes(name);
	}

	public [Symbol.iterator](): Iterator<V> {
		let iter : JavaIterator<V> = this.iterator();
		const result : Iterator<V> = {
			next() : IteratorResult<V> {
				if (iter.hasNext())
					return { value : iter.next(), done : false };
				return { value : null, done : true };
			}
		};
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_adt_map_AVLMapSubCollection<K, V>(obj : unknown) : AVLMapSubCollection<K, V> {
	return obj as AVLMapSubCollection<K, V>;
}
