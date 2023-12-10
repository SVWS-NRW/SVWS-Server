import { ArrayMapCollectionIterator } from '../../../core/adt/map/ArrayMapCollectionIterator';
import type { JavaIterator } from '../../../java/util/JavaIterator';
import { ArrayList } from '../../../java/util/ArrayList';
import type { Collection } from '../../../java/util/Collection';
import { ArrayMap, cast_de_svws_nrw_core_adt_map_ArrayMap } from '../../../core/adt/map/ArrayMap';
import { JavaObject } from '../../../java/lang/JavaObject';
import type { List } from '../../../java/util/List';
import { UnsupportedOperationException } from '../../../java/lang/UnsupportedOperationException';

export class ArrayMapCollection<K, V> extends JavaObject implements Collection<V> {

	/**
	 * Die {@link ArrayMap} zu der diese Collection gehört.
	 */
	private readonly _map : ArrayMap<K, V>;


	/**
	 * Erstellt eine neues Key-Set für die übergebene {@link ArrayMap}.
	 *
	 * @param map   die {@link ArrayMap}, zu welcher dieses Key-set gehört
	 */
	constructor(map : ArrayMap<K, V>) {
		super();
		this._map = map;
	}

	public size() : number {
		return this._map.size();
	}

	public isEmpty() : boolean {
		return this._map.isEmpty();
	}

	public contains(obj : unknown | null) : boolean {
		return this._map.containsValue(obj);
	}

	public iterator() : JavaIterator<V> {
		return new ArrayMapCollectionIterator(this._map);
	}

	private getValueList() : List<V> {
		const list : ArrayList<V> = new ArrayList(this._map.size());
		for (let i : number = 0; i < this._map.getNumberOfKeys(); i++) {
			const value : V | null = this._map.getValueAt(i);
			if (value !== null)
				list.add(value);
		}
		return list;
	}

	public toArray() : Array<unknown>;

	public toArray<T>(a : Array<T>) : Array<T>;

	/**
	 * Implementation for method overloads of 'toArray'
	 */
	public toArray<T>(__param0? : Array<T>) : Array<T> | Array<unknown> {
		if ((typeof __param0 === "undefined")) {
			return this.getValueList().toArray();
		} else if (((typeof __param0 !== "undefined") && Array.isArray(__param0))) {
			const a : Array<T> = __param0;
			return this.getValueList().toArray(a);
		} else throw new Error('invalid method overload');
	}

	public add(e : V) : boolean {
		throw new UnsupportedOperationException("add: Werte können nicht ohne Schlüsselwerte hinzugefügt werden.")
	}

	public remove(o : unknown | null) : boolean {
		throw new UnsupportedOperationException("remove: Werte können nicht ohne einen Schlüsselwert entfernt werden.")
	}

	public containsAll(collection : Collection<any> | null) : boolean {
		if ((collection === null) || (this as unknown === collection as unknown))
			return true;
		for (const obj of collection)
			if (!this._map.containsValue(obj))
				return false;
		return true;
	}

	public addAll(c : Collection<V> | null) : boolean {
		throw new UnsupportedOperationException("addAll: Werte können nicht ohne Schlüsselwerte hinzugefügt werden.")
	}

	public removeAll(c : Collection<any> | null) : boolean {
		throw new UnsupportedOperationException("removeAll: Werte können nicht ohne einen Schlüsselwert entfernt werden.")
	}

	public retainAll(c : Collection<any> | null) : boolean {
		throw new UnsupportedOperationException("retainAll: Werte können nicht ohne einen Schlüsselwert entfernt werden.")
	}

	public clear() : void {
		this._map.clear();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.adt.map.ArrayMapCollection', 'java.util.Collection', 'java.lang.Iterable'].includes(name);
	}

	public [Symbol.iterator](): Iterator<V> {
		const iter : JavaIterator<V> = this.iterator();
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

export function cast_de_svws_nrw_core_adt_map_ArrayMapCollection<K, V>(obj : unknown) : ArrayMapCollection<K, V> {
	return obj as ArrayMapCollection<K, V>;
}
