import { AbstractSet } from './AbstractSet';
import { Collection, cast_java_util_Collection } from './Collection';
import { HashSetIterator } from './HashSetIterator';
import { JavaIterator } from './JavaIterator';
import { JavaSet } from './JavaSet';

import { Cloneable } from '../../java/lang/Cloneable';
import { JavaObject } from '../../java/lang/JavaObject';

import { Serializable } from '../../java/io/Serializable';


export class HashSet<E> extends AbstractSet<E> implements JavaSet<E>, Cloneable, Serializable {

	protected readonly _set : Set<E>;

	public constructor(c? : Collection<E>) {
		super();
		this._set = new Set<E>();
		if (typeof c === "undefined")
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
		return this._set.has(e);
	}

	public add(e: E): boolean {
		const result : boolean = this._set.has(e);
		this._set.add(e);
		this.modCount++;
		return !result;
	}

	public remove(o: any): boolean {
		const result : boolean = this._set.delete(o);
		if (result)
			this.modCount++;
		return result;
	}

	public clear(): void {
		if (this._set.size > 0)
			this.modCount++;
		this._set.clear();
	}

	public clone() : JavaObject {
		return new HashSet<E>(this);
	}

	public toArray() : Array<unknown>;
	public toArray<U>(a: Array<U>) : Array<U>;
	public toArray<T>(__param0? : Array<T>) : Array<T> | Array<unknown> {
		if ((typeof __param0 === "undefined") || (__param0 == null) || (__param0.length < this._set.size)) {
			const result : Array<E> = [];
			this._set.forEach((value : E) => {result.push(value)});
			return result;
		} else if (Array.isArray(__param0)) {
			// TODO handle the case where a is not null and try to fill into the parameter array if possible - see JavaDoc for implementation
			throw new Error('not yet implemented')
		} else throw new Error('invalid method overload');
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
