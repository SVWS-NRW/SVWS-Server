import type { JavaSet } from './JavaSet';
import type { JavaMapEntry } from './JavaMapEntry';
import type { Collection } from './Collection';
import type { JavaIterator } from './JavaIterator';
import type { HashMap } from './HashMap';

import { JavaObject } from '../../java/lang/JavaObject';
import { UnsupportedOperationException } from '../lang/UnsupportedOperationException';

export class HashMapKeySet<K, V> extends JavaObject implements JavaSet<K> {

	protected readonly _map : HashMap<K, V>;

	public constructor(map : HashMap<K, V>) {
		super();
		this._map = map;
	}

	size(): number {
		return this._map.size();
	}

	isEmpty(): boolean {
		return this._map.isEmpty();
	}

	contains(key: any): boolean {
		return this._map.containsKey(key);
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
		if ((typeof __param0 === "undefined") || (__param0 === null) || (__param0.length < this.size())) {
			const r : Array<K> = [];
			for (const e of this._map)
				r.push(e.getKey());
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
		const iter = this._map[Symbol.iterator]();
		const result : Iterator<K> = {
			next() : IteratorResult<K> {
				const result : IteratorResult<JavaMapEntry<K, V>> = iter.next();
				if (result.done === true)
					return { value : null, done : true };
				return { value : result.value.getKey(), done : false };
			}
		};
		return result;
	}

	public transpilerCanonicalName(): string {
		return 'java.util.HashMapKeySet';
	}

}