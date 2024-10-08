import type { Collection } from './Collection';
import type { JavaIterator } from './JavaIterator';
import { Class } from '../../java/lang/Class';
import type { TranspiledObject } from '../../java/lang/TranspiledObject';
import type { JavaMapEntry } from './JavaMapEntry';
import type { JavaObject } from '../../java/lang/JavaObject';
import type { HashMap } from './HashMap';

import { UnsupportedOperationException } from '../../java/lang/UnsupportedOperationException';


export class HashMapCollection<K, V> implements Collection<V> {

	protected readonly _map : HashMap<K, V>;

	public constructor(map : HashMap<K, V>) {
		this._map = map;
	}

	size(): number {
		return this._map.size();
	}

	isEmpty(): boolean {
		return this._map.isEmpty();
	}

	contains(value: any): boolean {
		return this._map.containsValue(value);
	}

	iterator(): JavaIterator<V> {
		const it = this[Symbol.iterator]()
		let next_item = it.next()
		return {
			hasNext():boolean {
				return next_item.done ? false : true
			},
			next(): V {
				const e = next_item.value
				next_item = it.next()
				return e;
			},
			remove() {
			},
		}
	}

	public toArray() : Array<unknown>;
	public toArray<U>(a: Array<U>) : Array<U>;
	public toArray<T>(__param0? : Array<T>) : Array<T> | Array<unknown> {
		if ((__param0 === undefined) || (__param0 === null) || (__param0.length < this.size())) {
			const r : Array<V> = new Array<V>(this.size());
			const it : JavaIterator<V> = this.iterator();
			let i : number = 0
			while (it.hasNext()) {
				r[i] = it.next();
				i++;
			}
			return r;
		} else if (Array.isArray(__param0)) {
			// TODO handle the case where a is not null and try to fill into the parameter array if possible - see JavaDoc for implementation
			throw new Error('not yet implemented')
		} else throw new Error('invalid method overload');
	}

	add(e: V): boolean {
		throw new UnsupportedOperationException();
	}

	remove(o: any): boolean {
		throw new UnsupportedOperationException();
	}

	containsAll(c: Collection<any>): boolean {
		throw new UnsupportedOperationException();
	}

	addAll(c: Collection<V>): boolean {
		throw new UnsupportedOperationException();
	}

	removeAll(c: Collection<any>): boolean {
		throw new UnsupportedOperationException();
	}

	retainAll(c: Collection<any>): boolean {
		throw new UnsupportedOperationException();
	}

	clear(): void {
		throw new UnsupportedOperationException();
	}

	equals(obj: any): boolean {
		throw new UnsupportedOperationException();
	}

	hashCode(): number {
		throw new UnsupportedOperationException();
	}

	[Symbol.iterator](): Iterator<V> {
		const iter = this._map[Symbol.iterator]();
		const result : Iterator<V> = {
			next() : IteratorResult<V> {
				const result : IteratorResult<JavaMapEntry<K, V>> = iter.next();
				if (result.done === true)
					return { value : null, done : true };
				return { value : result.value.getValue(), done : false };
			}
		};
		return result;
	}

	getClass<T extends TranspiledObject>(): Class<T> {
		return new Class(this.transpilerCanonicalName());
	}

	clone(): unknown {
		throw new UnsupportedOperationException();
	}

	toString(): string | null {
		let res = '[';
		for (const e of this._map)
			res = res + (e.getValue() as unknown as JavaObject).toString() + ', ';
		res = res.substring(-2, 0);
		res = res + ']';
		return res;
	}

	public transpilerCanonicalName(): string {
		return 'java.util.HashMapCollection';
	}

	isTranspiledInstanceOf(name: string): boolean {
		throw new UnsupportedOperationException();
	}

}
