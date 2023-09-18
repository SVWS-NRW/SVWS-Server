import type { JavaSet } from '../../../java/util/JavaSet';
import { ArrayMapEntry } from '../../../core/adt/map/ArrayMapEntry';
import { NullPointerException } from '../../../java/lang/NullPointerException';
import type { JavaIterator } from '../../../java/util/JavaIterator';
import { ArrayMapKeySetIterator } from '../../../core/adt/map/ArrayMapKeySetIterator';
import { ArrayList } from '../../../java/util/ArrayList';
import { ArrayMap, cast_de_svws_nrw_core_adt_map_ArrayMap } from '../../../core/adt/map/ArrayMap';
import type { Collection } from '../../../java/util/Collection';
import { JavaObject } from '../../../java/lang/JavaObject';
import type { List } from '../../../java/util/List';
import { UnsupportedOperationException } from '../../../java/lang/UnsupportedOperationException';

export class ArrayMapKeySet<K, V> extends JavaObject implements JavaSet<K> {

	/**
	 * Die {@link ArrayMap} zu der dieses Key-Set gehört.
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
		return this._map.containsKey(obj);
	}

	public iterator() : JavaIterator<K> {
		return new ArrayMapKeySetIterator(this._map);
	}

	private getKeyList() : List<K> {
		const list : ArrayList<K> = new ArrayList(this._map.size());
		for (let i : number = 0; i < this._map.getNumberOfKeys(); i++) {
			const value : ArrayMapEntry<K, V> | null = this._map.getEntryByIndex(i);
			if (value !== null)
				list.add(value.getKey());
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
			return this.getKeyList().toArray();
		} else if (((typeof __param0 !== "undefined") && Array.isArray(__param0))) {
			const a : Array<T> = __param0;
			return this.getKeyList().toArray(a);
		} else throw new Error('invalid method overload');
	}

	public add(e : K) : boolean {
		throw new UnsupportedOperationException("add: Es kann kein Schlüsselwert ohne zugeordnetem Wert hinzugefügt werden (null ist nicht erlaubt).")
	}

	public remove(obj : unknown | null) : boolean {
		return this._map.remove(obj) !== null;
	}

	public containsAll(collection : Collection<unknown> | null) : boolean {
		if ((collection === null) || (this as unknown === collection as unknown))
			return true;
		for (const obj of collection)
			if (!this._map.containsKey(obj))
				return false;
		return true;
	}

	public addAll(c : Collection<K> | null) : boolean {
		throw new UnsupportedOperationException("addAll: Es kann kein Schlüsselwert ohne zugeordnetem Wert hinzugefügt werden (null ist nicht erlaubt).")
	}

	public retainAll(collection : Collection<unknown> | null) : boolean {
		if (collection === null)
			throw new NullPointerException()
		let changed : boolean = false;
		for (let i : number = 0; i < this._map.getNumberOfKeys(); i++) {
			const entry : ArrayMapEntry<K, V> | null = this._map.getEntryByIndex(i);
			if (entry === null)
				continue;
			if (!collection.contains(entry.getKey())) {
				this._map.remove(entry.getKey());
				changed = true;
			}
		}
		return changed;
	}

	public removeAll(collection : Collection<unknown> | null) : boolean {
		if (collection === null)
			throw new NullPointerException()
		let removed : boolean = false;
		for (const obj of collection) {
			if (this._map.containsKey(obj)) {
				this._map.remove(obj);
				removed = true;
			}
		}
		return removed;
	}

	public clear() : void {
		this._map.clear();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['java.util.Collection', 'java.util.Set', 'de.svws_nrw.core.adt.map.ArrayMapKeySet', 'java.lang.Iterable'].includes(name);
	}

	public [Symbol.iterator](): Iterator<K> {
		const iter : JavaIterator<K> = this.iterator();
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

export function cast_de_svws_nrw_core_adt_map_ArrayMapKeySet<K, V>(obj : unknown) : ArrayMapKeySet<K, V> {
	return obj as ArrayMapKeySet<K, V>;
}
