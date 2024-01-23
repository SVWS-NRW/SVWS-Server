import type { JavaIterator } from './JavaIterator';
import type { ListIterator } from './ListIterator';
import type { Collection } from './Collection';
import type { Comparator } from './Comparator';
import type { SequencedCollection } from './SequencedCollection';
import { NoSuchElementException } from './NoSuchElementException';

// TODO extends SequencedCollection instead of Collection
export interface List<E> extends Collection<E> {

	size() : number;

	isEmpty() : boolean;

	contains(o : any) : boolean;

	[Symbol.iterator](): Iterator<E>;

	iterator() : JavaIterator<E>;

	toArray() : Array<unknown>;

	toArray<U>(a: Array<U>) : Array<U>;

	add(e : E | null) : boolean;

	add(index : number, element : E | null) : void;

	remove(index : number) : E;

	remove(o : any) : boolean;

	containsAll(c : Collection<any> | null) : boolean;

	addAll(c : Collection<E> | null) : boolean;

	addAll(index : number, c : Collection<E>) : boolean;

	removeAll(c : Collection<any> | null) : boolean;

	retainAll(c : Collection<any> | null) : boolean;

	sort(c : Comparator<E>) : void;

	clear() : void;

	equals(obj : any) : boolean;

	hashCode() : number;


	get(index : number) : E;

	set(index : number, element : E) : E;

	removeElementAt(index : number) : void;

	indexOf(o : any) : number;

	lastIndexOf(o : any) : number;


	listIterator() : ListIterator<E>;

	listIterator(index : number) : ListIterator<E>;


	addFirst(e: E | null) : void;

	addLast(e: E | null) : void;

	getFirst() : E | null;

	getLast() : E | null;
	
	removeFirst() : E | null;

	removeLast() : E | null;

}


/* TODO: Implementierung der Default-Methode replaceAll */


/* Implementierung der Default-Methode addFirst */
export function java_util_List_addFirst<E>(this: List<E>, e: E | null) : void {
	this.add(0, e);
}


/* Implementierung der Default-Methode addLast */
export function java_util_List_addLast<E>(this: List<E>, e: E | null) : void {
	this.add(e);
}


/* Implementierung der Default-Methode getFirst */
export function java_util_List_getFirst<E>(this: List<E>) : E | null {
	if (this.isEmpty()) {
		throw new NoSuchElementException();
	} else {
		return this.get(0);
	}
}


/* Implementierung der Default-Methode getLast */
export function java_util_List_getLast<E>(this: List<E>) : E | null {
	if (this.isEmpty()) {
		throw new NoSuchElementException();
	} else {
		return this.get(this.size() - 1);
	}
}


/* Implementierung der Default-Methode removeFirst */
export function java_util_List_removeFirst<E>(this: List<E>) : E | null {
	if (this.isEmpty()) {
		throw new NoSuchElementException();
	} else {
		const e = this.get(0);
		this.removeElementAt(0);
		return e;
	}
}


/* Implementierung der Default-Methode removeLast */
export function java_util_List_removeLast<E>(this: List<E>) : E | null {
	if (this.isEmpty()) {
		throw new NoSuchElementException();
	} else {
		const e = this.get(this.size() - 1);
		this.removeElementAt(this.size() - 1)
		return e;
	}
}


export function cast_java_util_List<E>(obj : unknown) : List<E> {
	return obj as List<E>;
}
