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

	/** Eine Map von dem Hash-Wert des Objektes auf das Objekt in der Menge */
	protected readonly _map : Map<number, E>;

	public constructor(c? : Collection<E>) {
		super();
		this._map = new Map<number, E>();
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
		return this._map.size;
	}

	public isEmpty(): boolean {
		return this._map.size <= 0;
	}

	public contains(e: any): boolean {
		const hash = JavaObject.getTranspilerHashCode(e);
		return this._map.has(hash);
	}

	public add(e: E): boolean {
		const hash = JavaObject.getTranspilerHashCode(e);
		const result : boolean = this._map.has(hash);
		this._map.set(hash, e);
		this.modCount++;
		return !result;
	}

	public remove(o: any): boolean {
		const hash = JavaObject.getTranspilerHashCode(o);
		const result : boolean = this._map.delete(hash);
		if (result)
			this.modCount++;
		return result;
	}

	public clear(): void {
		if (this._map.size > 0)
			this.modCount++;
		this._map.clear();
	}

	public clone() : JavaObject {
		return new HashSet<E>(this);
	}

	public toArray() : Array<unknown>;
	public toArray<U>(a: Array<U>) : Array<U>;
	public toArray<T>(__param0? : Array<T> | null) : Array<T> | Array<unknown> {
		if ((__param0 === undefined) || (__param0 === null) || (__param0.length < this._map.size)) {
			const result : Array<E> = [];
			this._map.values().forEach((value : E) => { result.push(value) });
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
