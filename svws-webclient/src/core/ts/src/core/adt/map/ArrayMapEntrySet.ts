import { JavaMapEntry, cast_java_util_Map_Entry } from '../../../java/util/JavaMapEntry';
import { ArrayMapEntrySetIterator } from '../../../core/adt/map/ArrayMapEntrySetIterator';
import { JavaSet } from '../../../java/util/JavaSet';
import { ArrayMapEntry, cast_de_svws_nrw_core_adt_map_ArrayMapEntry } from '../../../core/adt/map/ArrayMapEntry';
import { NullPointerException } from '../../../java/lang/NullPointerException';
import { JavaIterator } from '../../../java/util/JavaIterator';
import { ArrayList } from '../../../java/util/ArrayList';
import { ArrayMap, cast_de_svws_nrw_core_adt_map_ArrayMap } from '../../../core/adt/map/ArrayMap';
import { Collection } from '../../../java/util/Collection';
import { JavaObject } from '../../../java/lang/JavaObject';
import { List } from '../../../java/util/List';
import { JavaMap, cast_java_util_Map } from '../../../java/util/JavaMap';

export class ArrayMapEntrySet<K, V> extends JavaObject implements JavaSet<JavaMapEntry<K, V>> {

	/**
	 * Die {@link ArrayMap} zu der dieses Entry-Set gehört.
	 */
	private readonly _map : ArrayMap<K, V>;


	/**
	 * Erstellt eine neues Entry-Set für die übergebene {@link ArrayMap}.
	 *
	 * @param map   die {@link ArrayMap}, zu welcher dieses Entry-Set gehört
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

	private toEntry(obj : unknown | null) : JavaMapEntry<K, V> | null {
		if (obj === null)
			return null;
		if (!(((obj instanceof JavaObject) && ((obj as JavaObject).isTranspiledInstanceOf('java.util.Map.Entry')))))
			return null;
		const entry : JavaMapEntry<K, V> = cast_java_util_Map_Entry(obj);
		return entry;
	}

	private containsEntry(entry : JavaMapEntry<K, V> | null) : boolean {
		if (entry === null)
			return false;
		if (!this._map.containsKey(entry.getKey()))
			return false;
		const value : V | null = this._map.get(entry.getKey());
		if (value === null)
			return false;
		return JavaObject.equalsTranspiler(value, (entry.getValue()));
	}

	public contains(obj : unknown | null) : boolean {
		const entry : JavaMapEntry<K, V> | null = this.toEntry(obj);
		if (entry === null)
			return false;
		return this.containsEntry(entry);
	}

	public iterator() : JavaIterator<JavaMapEntry<K, V>> {
		return new ArrayMapEntrySetIterator(this._map);
	}

	private getEntryList() : List<ArrayMapEntry<K, V>> {
		const list : ArrayList<ArrayMapEntry<K, V>> = new ArrayList(this._map.size());
		for (let i : number = 0; i < this._map.getNumberOfKeys(); i++) {
			const value : ArrayMapEntry<K, V> | null = this._map.getEntryByIndex(i);
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
			return this.getEntryList().toArray();
		} else if (((typeof __param0 !== "undefined") && Array.isArray(__param0))) {
			const a : Array<T> = __param0;
			return this.getEntryList().toArray(a);
		} else throw new Error('invalid method overload');
	}

	public add(e : JavaMapEntry<K, V>) : boolean {
		if (e === null)
			return false;
		this._map.put(e.getKey(), e.getValue());
		return true;
	}

	public remove(obj : unknown | null) : boolean {
		const entry : JavaMapEntry<K, V> | null = this.toEntry(obj);
		if (entry === null)
			return false;
		if (!this.containsEntry(entry))
			return false;
		this._map.remove(entry.getKey());
		return true;
	}

	public containsAll(collection : Collection<unknown> | null) : boolean {
		if ((collection === null) || (this as unknown === collection as unknown))
			return true;
		for (const obj of collection)
			if (!this.contains(obj))
				return false;
		return true;
	}

	public addAll(collection : Collection<JavaMapEntry<K, V>> | null) : boolean {
		if (collection === null)
			throw new NullPointerException()
		if (this as unknown === collection as unknown)
			return true;
		for (const entry of collection)
			if (!this.add(entry))
				return false;
		return true;
	}

	public retainAll(collection : Collection<unknown> | null) : boolean {
		if (collection === null)
			throw new NullPointerException()
		let changed : boolean = false;
		for (let i : number = 0; i < this._map.getNumberOfKeys(); i++) {
			const entry : ArrayMapEntry<K, V> | null = this._map.getEntryByIndex(i);
			if (entry === null)
				continue;
			if (!collection.contains(entry)) {
				this._map.remove(entry.getKey());
				changed = true;
			}
		}
		return changed;
	}

	public removeAll(collection : Collection<unknown> | null) : boolean {
		if (collection === null)
			throw new NullPointerException()
		let changed : boolean = false;
		for (const obj of collection) {
			if (this.contains(obj)) {
				this.remove(obj);
				changed = true;
			}
		}
		return changed;
	}

	public clear() : void {
		this._map.clear();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.adt.map.ArrayMapEntrySet', 'java.util.Collection', 'java.util.Set', 'java.lang.Iterable'].includes(name);
	}

	public [Symbol.iterator](): Iterator<JavaMapEntry<any, any>> {
		const iter : JavaIterator<JavaMapEntry<any, any>> = this.iterator();
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

export function cast_de_svws_nrw_core_adt_map_ArrayMapEntrySet<K, V>(obj : unknown) : ArrayMapEntrySet<K, V> {
	return obj as ArrayMapEntrySet<K, V>;
}
