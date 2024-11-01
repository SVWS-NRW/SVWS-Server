import type { Collection} from './Collection';
import type { JavaIterator } from './JavaIterator';
import type { JavaSet } from './JavaSet';
import type { Cloneable } from '../../java/lang/Cloneable';
import type { Serializable } from '../../java/io/Serializable';

import { AbstractSet } from './AbstractSet';
import { cast_java_util_Collection } from './Collection';
import { HashSetIterator } from './HashSetIterator';
import { JavaObject } from '../../java/lang/JavaObject';



export class HashSet<E> extends AbstractSet<E> implements JavaSet<E>, Cloneable, Serializable {

	// Ein internes Java-Set, welches aber nicht korrekt mit der equals-Methode von Java-Objekte arbeitet
	protected readonly _set : Set<E>;

	// Eine Map mit Kollisionslisten, falls es sich um Java-Objekte handelt
	protected readonly _mapKollisionen : Map<number, Set<E>>;


	public constructor(c? : Collection<E>) {
		super();
		this._set = new Set<E>();
		this._mapKollisionen = new Map<number, Set<E>>();
		if (c === undefined)
			return;
		if ((c instanceof JavaObject) && c.isTranspiledInstanceOf('java.util.Collection')) {
			const coll : Collection<E> = cast_java_util_Collection(c);
			this.addAll(coll);
		}
	}

	public iterator() : JavaIterator<E> {
		return new HashSetIterator<E>(this);
	}

	public size() : number {
		return this._set.size;
	}

	public isEmpty(): boolean {
		return this._set.size <= 0;
	}

	public contains(e: any): boolean {
		if (e instanceof JavaObject) {
			const hash = JavaObject.getTranspilerHashCode(e);
			const s = this._mapKollisionen.get(hash);
			if (s === undefined)
				return false;
			for (const elem of s)
				if (e.equals(elem))
					return true;
			return false;
		}
		return this._set.has(e);
	}

	public add(e: E): boolean {
		if (this.contains(e))
			return false;
		this._set.add(e);
		if (e instanceof JavaObject) {
			const hash = JavaObject.getTranspilerHashCode(e);
			let s = this._mapKollisionen.get(hash);
			if (s === undefined) {
				s = new Set<E>();
				this._mapKollisionen.set(hash, s);
			}
			s.add(e);
		}
		this.modCount++;
		return true;
	}

	public remove(o: any): boolean {
		if (o instanceof JavaObject) {
			const hash = JavaObject.getTranspilerHashCode(o);
			const s = this._mapKollisionen.get(hash);
			if (s === undefined)
				return false;
			let obj = undefined;
			for (const elem of s)
				if (o.equals(elem))
					obj = elem;
			if (obj === undefined)
				return false;
			s.delete(obj as unknown as E);
			if (s.size === 0)
				this._mapKollisionen.delete(hash);
			this._set.delete(obj as unknown as E);
			this.modCount++;
			return true;
		} else {
			if (!this.contains(o))
				return false;
			this._set.delete(o);
			this.modCount++;
			return true;
		}
	}

	public clear(): void {
		if (this._set.size > 0)
			this.modCount++;
		this._set.clear();
		this._mapKollisionen.clear();
	}

	public clone() : JavaObject {
		return new HashSet<E>(this);
	}

	public toArray() : Array<unknown>;
	public toArray<U>(a: Array<U>) : Array<U>;
	public toArray<T>(__param0? : Array<T> | null) : Array<T> | Array<unknown> {
		if ((__param0 === undefined) || (__param0 === null) || (__param0.length < this._set.size)) {
			const result : Array<E> = [];
			this._set.forEach((value : E) => { result.push(value) });
			return result;
		} else if (Array.isArray(__param0)) {
			// TODO handle the case where a is not null and try to fill into the parameter array if possible - see JavaDoc for implementation
			throw new Error('not yet implemented')
		} else throw new Error('invalid method overload');
	}

	public transpilerCanonicalName(): string {
		return 'java.util.HashSet';
	}

	public isTranspiledInstanceOf(name : string): boolean {
		return [
			'java.util.HashSet',
			'java.util.AbstractSet',
			'java.util.AbstractCollection',
			'java.lang.Object',
			'java.io.Serializable',
			'java.lang.Cloneable',
			'java.lang.Iterable',
			'java.util.Collection',
			'java.util.Set'
		].includes(name);
	}

}


export function cast_java_util_HashSet<E>(obj : unknown) : HashSet<E> {
	return obj as HashSet<E>;
}
