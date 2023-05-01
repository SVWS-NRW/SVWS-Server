import { JavaSet } from './JavaSet';
import { JavaMapEntry } from './JavaMapEntry';

import { JavaObject } from '../../java/lang/JavaObject';
import { Collection } from './Collection';
import { JavaIterator } from './JavaIterator';
import { UnsupportedOperationException } from '../lang/UnsupportedOperationException';

export class HashMapKeySet<K, V> extends JavaObject implements JavaSet<K> {

	protected readonly _map : Map<K, JavaMapEntry<K, V>>;

	public constructor(map : Map<K, JavaMapEntry<K, V>>) {
		super();
		this._map = map;
	}

	size(): number {
		return this._map.size;
	}

	isEmpty(): boolean {
		return this._map.size === 0;
	}

	contains(key: any): boolean {
		for (const [k, e] of this._map) {
			if (k === key)
				return true;
			if ((k instanceof JavaObject) && (k.equals(key)))
				return true;
		}
		return false;
	}

	iterator(): JavaIterator<K> {
		const it = this[Symbol.iterator]();
		let next_item = it.next();
		return {
			hasNext() : boolean {
				return next_item.done ? false : true;
			},
			next() : K {
				const e = next_item.value;
				next_item = it.next();
				return e.getKey();
			},
			remove() {
			},
		}
	}

	public toArray() : Array<unknown>;
	public toArray<U>(a: Array<U>) : Array<U>;
	public toArray<T>(__param0? : Array<T>) : Array<T> | Array<unknown> {
		if ((typeof __param0 === "undefined") || (__param0 == null) || (__param0.length < this.size())) {
			const r : Array<K> = [];
			for (const [k, e] of this._map)
				r.push(k);
			return r;
		} else if (Array.isArray(__param0)) {
			// TODO handle the case where a is not null and try to fill into the parameter array if possible - see JavaDoc for implementation
			throw new Error('not yet implemented')
		} else throw new Error('invalid method overload');
	}

	add(e: K | null): boolean {
		throw new UnsupportedOperationException();
	}

	remove(e: any): boolean {
		throw new UnsupportedOperationException();
	}

	containsAll(c: Collection<any> | null): boolean {
		throw new UnsupportedOperationException();
	}

	addAll(c: Collection<K> | null): boolean {
		throw new UnsupportedOperationException();
	}

	removeAll(c: Collection<any> | null): boolean {
		throw new UnsupportedOperationException();
	}

	retainAll(c: Collection<any> | null): boolean {
		throw new UnsupportedOperationException();
	}

	clear(): void {
		this._map.clear();
	}

	[Symbol.iterator](): Iterator<K, any, undefined> {
		return this._map.keys();
	}

}