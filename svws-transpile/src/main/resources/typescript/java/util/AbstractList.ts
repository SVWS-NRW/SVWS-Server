import type { Collection } from './Collection';
import type { JavaIterator } from './JavaIterator';
import type { List } from './List';
import { java_util_List_addFirst, java_util_List_addLast, java_util_List_getFirst, java_util_List_getLast,
	java_util_List_removeFirst, java_util_List_removeLast } from './List';
import type { ListIterator } from './ListIterator';
import type { Comparator } from './Comparator';

import { AbstractCollection } from './AbstractCollection';
import { AbstractListListIterator } from './AbstractListListIterator';
import { IndexOutOfBoundsException } from '../../java/lang/IndexOutOfBoundsException';
import { JavaObject } from '../../java/lang/JavaObject';
import { UnsupportedOperationException } from '../../java/lang/UnsupportedOperationException';
import { NullPointerException } from '../lang/NullPointerException';


export abstract class AbstractList<E> extends AbstractCollection<E> implements List<E> {

	modCount : number = 0;


	protected constructor() {
		super();
	}


	public [Symbol.iterator](): Iterator<E> {
		const iter : JavaIterator<E> = this.iterator();
		const result : Iterator<E> = {
			next() : IteratorResult<E> {
				if (iter.hasNext())
					return { value : iter.next(), done : false };
				return { value : null, done : true };
			}
		};
		return result;
	}


	public iterator() : JavaIterator<E> {
		return new AbstractListListIterator<E>(this, 0);
	}


	public add(e : E | null) : boolean;
	public add(index : number, element : E | null) : void;
	public add(param1 : number | E | null, element? : E | null) : boolean | void {
		if ((param1 === null) || (element === null))
			throw new NullPointerException();
		if ((typeof param1 === "number") && (typeof element !== "undefined")) {
			throw new UnsupportedOperationException();
		} else if ((typeof param1 !== "number") && (typeof element === "undefined")) {
			this.add(this.size(), param1);
			return true;
		} else throw new Error("Invalid method overload.");
	}


	public remove(index : number) : E;
	public remove(e : E) : boolean;
	public remove(param : number | E) : E | boolean {
		return super.remove(param);
	}


	public removeElementAt(index: number): void {
		throw new Error('Method not implemented.');
	}


	public indexOf(e : E) : number {
		const it : ListIterator<E> = this.listIterator();
		if (e === null)
			return -1;
		while (it.hasNext())
			if (((e instanceof JavaObject) && (e.equals(it.next()))) || (e === it.next()))
				return it.previousIndex();
		return -1;
	}


	public lastIndexOf(e : E) : number {
		const it : ListIterator<E> = this.listIterator();
		if (e === null)
			return -1;
		while (it.hasPrevious())
			if (((e instanceof JavaObject) && (e.equals(it.previous()))) || (e === it.previous()))
				return it.nextIndex();
		return -1;
	}


	public clear() : void {
		this.removeRange(0, this.size());
	}


	public addAll(c : Collection<E>) : boolean;
	public addAll(index : number, c : Collection<E>) : boolean;
	public addAll(param1 : number | Collection<E>, param2? : Collection<E>) : boolean {
		if (((typeof param1 === "number") && (typeof param2 === "undefined")) ||
			((typeof param1 !== "number") && (typeof param2 !== "undefined")))
			throw new Error("invalid method overload");
		let index : number = (typeof param1 === "number") ? param1 : 0;
		const c : Collection<E> | undefined = (typeof param1 === "number") ? param2 : param1;
		if (!c)
			throw new Error("invalid method overload");
		if (index < 0 || index > this.size())
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size());
		let modified : boolean = false;
		const it : JavaIterator<E> = c.iterator();
		while (it.hasNext()) {
			this.add(index++, it.next());
			modified = true;
		}
		return modified;
	}


	public listIterator(param? : number) : ListIterator<E> {
		const index = (typeof param === "undefined") ? 0 : param;
		if (index < 0 || index > this.size())
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size());
		return new AbstractListListIterator(this, index);
	}


	public equals(obj : any) : boolean {
		// TODO any collection can be equal - check all elements with an iterator
		if (!(obj instanceof AbstractList))
			return false;
		const e1 : ListIterator<E> = this.listIterator();
		const c2 : AbstractList<E> = obj;
		const e2 : ListIterator<E> = c2.listIterator();
		while (e1.hasNext() && e2.hasNext()) {
			const o1 : E = e1.next();
			const o2 : E = e2.next();
			if (((o1 instanceof JavaObject) && (!o1.equals(o2))) || (o1 !== o2))
				return false;
		}
		return !(e1.hasNext() || e2.hasNext());
	}


	protected removeRange(fromIndex : number, toIndex : number) : void {
		const iter : ListIterator<E> = this.listIterator(fromIndex);
		const n = toIndex - fromIndex;
		for (let i : number = 0; i < n ; i++) {
			if (!iter.hasNext())
				break;
			iter.next();
			iter.remove();
		}
	}

	public abstract get(index : number) : E;


	public set(index : number, element : E) : E {
		throw new UnsupportedOperationException();
	}

	/**
	 * Sort the List with the given Comparator function
	 *
	 * @param {Comparator<E>} c
	 */
	public sort(c: Comparator<E>): void {
		const sorted = super.toArray() as E[];
		sorted.sort(c.compare);
		this.clear();
		for (const e of sorted) {
			this.add(e);
		}
	}


	/* Implementierung der Default-Methode addFirst */
	public addFirst: (e: E | null) => void = java_util_List_addFirst;

	/* Implementierung der Default-Methode addLast */
	public addLast: (e: E | null) => void = java_util_List_addLast;

	/* Implementierung der Default-Methode getFirst */
	public getFirst: () => E = java_util_List_getFirst;

	/* Implementierung der Default-Methode getLast */
	public getLast: () => E = java_util_List_getLast;

	/* Implementierung der Default-Methode removeFirst */
	public removeFirst: () => E = java_util_List_removeFirst;

	/* Implementierung der Default-Methode removeLast */
	public removeLast: () => E = java_util_List_removeLast;


	public transpilerCanonicalName(): string {
		return 'java.util.AbstractList';
	}

	public isTranspiledInstanceOf(name : string): boolean {
		return [
			'java.util.AbstractList',
			'java.util.AbstractCollection',
			'java.util.List',
			'java.util.Collection',
			'java.lang.Iterable',
			'java.lang.Object'
		].includes(name);
	}

}


export function cast_java_util_AbstractList<E>(obj : unknown) : AbstractList<E> {
	return obj as AbstractList<E>;
}
