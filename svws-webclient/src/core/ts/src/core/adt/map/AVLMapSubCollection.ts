import { StringBuilder } from '../../../java/lang/StringBuilder';
import type { JavaIterator } from '../../../java/util/JavaIterator';
import type { Collection } from '../../../java/util/Collection';
import { JavaObject } from '../../../java/lang/JavaObject';
import { AVLMapSubMap } from '../../../core/adt/map/AVLMapSubMap';
import { UnsupportedOperationException } from '../../../java/lang/UnsupportedOperationException';

export class AVLMapSubCollection<K, V> extends JavaObject implements Collection<V> {

	/**
	 *  Die {@link AVLMapSubMap} auf der diese Sub-Collection operiert.
	 */
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

	public toString() : string {
		const sb : StringBuilder | null = new StringBuilder();
		sb.append("values = [");
		let first : boolean = true;
		for (const value of this) {
			if (first)
				first = false;
			else
				sb.append(", ");
			sb.append(value);
		}
		sb.append("], size = ");
		sb.append(this.size());
		sb.append(" --> ");
		sb.append(this._sub.toString());
		return sb.toString();
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
			return this._sub.bcGetArrayListOfValues().toArray();
		} else if (((typeof __param0 !== "undefined") && Array.isArray(__param0))) {
			const a : Array<T | null> = __param0;
			return this._sub.bcGetArrayListOfValues().toArray(a);
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
		return ['java.util.Collection', 'java.lang.Iterable', 'de.svws_nrw.core.adt.map.AVLMapSubCollection'].includes(name);
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

export function cast_de_svws_nrw_core_adt_map_AVLMapSubCollection<K, V>(obj : unknown) : AVLMapSubCollection<K, V> {
	return obj as AVLMapSubCollection<K, V>;
}
