import type { JavaIterable } from '../../java/lang/JavaIterable';
import type { JavaIterator } from './JavaIterator';
import type { Collection } from './Collection';
import { HashSet } from './HashSet';


export interface JavaSet<E> extends JavaIterable<E> {

	size() : number;

	isEmpty() : boolean;

	contains(e : any) : boolean;

	[Symbol.iterator](): Iterator<E>;

	iterator() : JavaIterator<E>;

	toArray() : Array<unknown>;

	toArray<U>(a: Array<U>) : Array<U>;

	add(e : E | null) : boolean;

	remove(e : any) : boolean;


	containsAll(c : Collection<any> | null) : boolean;

	addAll(c : Collection<E> | null) : boolean;

	removeAll(c : Collection<any> | null) : boolean;

	retainAll(c : Collection<any> | null) : boolean;

	clear() : void;

	equals(obj : any) : boolean;

	hashCode() : number;

	// TODO further methods defined in Java...

}


export function cast_java_util_Set<E>(obj : unknown) : JavaSet<E> {
	return obj as JavaSet<E>;
}


/**
 * Gibt ein neues Set mit den übergebenen Elementen zurück.
 *
 * @return das neue Set
 */
export function java_util_Set_of<T>(...elements: T[]) : JavaSet<T> {
	const result = new HashSet<T>();
	for (const elem of elements)
		result.add(elem);
	return result;
}

