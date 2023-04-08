import { AbstractList } from './AbstractList';
import { Collection } from './Collection';
import { ConcurrentModificationException } from './ConcurrentModificationException';
import { Enumeration } from './Enumeration';
import { List } from './List';
import { NoSuchElementException } from './NoSuchElementException';
import { RandomAccess } from './RandomAccess';
import { ArrayListEnumerator } from './ArrayListEnumerator';

import { Consumer } from './function/Consumer';

import { ArrayIndexOutOfBoundsException } from '../../java/lang/ArrayIndexOutOfBoundsException';
import { Cloneable } from '../../java/lang/Cloneable';
import { IndexOutOfBoundsException } from '../../java/lang/IndexOutOfBoundsException';
import { JavaObject } from '../../java/lang/JavaObject';
import { NullPointerException } from '../lang/NullPointerException';





export class ArrayList<E> extends AbstractList<E> implements List<E>, RandomAccess, Cloneable {

	protected elementData : E[];


	public constructor(param1? : number | Collection<E>, param2? : number) {
		super();
		if ((typeof param2 === "undefined") && (typeof param1 !== "undefined") && (typeof param1 !== "number")) {
			const c : Collection<E> = param1
	        const a : E[] = c.toArray() as E[];
	        this.elementData = a;
		} else if (((typeof param1 === "undefined") && (typeof param2 === "undefined")) ||
                   ((typeof param1 === "number") && (typeof param2 === "undefined")) ||
                   ((typeof param1 === "number") && (typeof param2 === "number"))) {
			// do nothing special here since the typescript implementation does not need the capacity internally - so the parameters are not relevant
			this.elementData = new Array<E>();
		} else throw new Error('invalid method overload');
	}


	/** Copies the elements of this ArrayList into given array
     * @param {E[]} anArray
     * @returns {void}
     */
	public copyInto(anArray : E[]) : void {
		anArray.splice(0, this.elementData.length, ...this.elementData);
	}


	public trimToSize() : void {
		// do nothing here since the typescript implementation does not need the capacity internally
	}


	public ensureCapacity(minCapacity : number) : void {
		// do nothing here since the typescript implementation does not need the capacity internally
	}


	public setSize(newSize : number) : void {
		if (newSize < 0)
			throw new ArrayIndexOutOfBoundsException(newSize);
		this.elementData.length = newSize;
	}

	/**
     * Returns the length of the ArrayList
     *
     * @returns {number}
     */
	public capacity() : number {
		return this.elementData.length;
	}


	/**
     * Returns the length of the ArrayList
     *
     * @returns {number}
     */
	public size() : number {
		return this.elementData.length;
	}

	/**
     * Returns whether the ArrayList is empty
     *
     * @returns {boolean}
     */
	public isEmpty() : boolean {
		return this.elementData.length === 0;
	}

	/**
     * Returns an instance of ArrayListEnumerator of this ArrayList
     *
     * @returns {ArrayListEnumerator<E>}
     */
	public elements() : Enumeration<E> {
		return new ArrayListEnumerator<E>(this);
	}

	/**
     * Returns whether the ArrayList contains given element
     *
     * @param {E} e
     *
     * @returns {boolean}
     */
	public contains(e : E) : boolean {
		return this.indexOf(e, 0) >= 0;
	}


	/**
     * Checks whether the two elements are equal or not
     *
     * @param e1   the first element
     * @param e2   the second element
     *
     * @returns true if the elements are equal and false otherwise
     */
	private equalElements(e1 : E, e2 : E) : boolean {
		if ((e1 === null) && (e2 === null))
			return true;
		if ((e1 === null) || (e2 === null))
			return false;
		if (e1 instanceof JavaObject) {
			if (e1.equals(e2)) {
				return true;
			}
		} else if (e1 === e2) {
			return true;
		}
		return false;
	}

	/**
     * Returns the index of given element. Used with an index it starts searching for object from given position.
     * Returns the position of the first found element or -1 if unsuccessful
     *
     * @param {E} e
     * @param {number} index
     * @returns {number}
     *
     * compare to {@link ArrayList#lastIndexOf}
     */
	public indexOf(e : E) : number;
	public indexOf(e : E, index : number) : number;
	public indexOf(e : E, index? : number) : number {
		if (typeof index === "undefined")
			return this.indexOf(e, 0);
		if (index < 0)
			throw new IndexOutOfBoundsException(`Invalid index: ${index}`);
		for (let i : number = index ; i < this.elementData.length ; i++)
			if (this.equalElements(e, this.elementData[i]))
				return i;
		return -1;
	}

	/**
     * Returns the position of the last instance of e and if given an index from that position
     * on backwards. Returns position or -1 if unsuccessful
     *
     * @param {E} e
     * @param {number} index
     * @returns {number}
     *
     * compare to {@link ArrayList#indexOf}
     */
	public lastIndexOf(e : E) : number;
	public lastIndexOf(e : E, index : number) : number;
	public lastIndexOf(e : E, index? : number) : number {
		if (typeof index === "undefined")
			return this.lastIndexOf(e, this.elementData.length - 1);
		if (index >= this.elementData.length)
			throw new IndexOutOfBoundsException(`Invalid index: ${index} is greater than the number of elements.`);
		for (let i : number = index; i >= 0; i--)
			if (this.equalElements(e, this.elementData[i]))
				return i;
		return -1;
	}

	/**
     * Returns the element E from index
     *
     * @param {number} index
     *
     * @returns {E}
     */
	public elementAt(index : number) : E {
		if ((index < 0) || (index >= this.elementData.length))
			throw new ArrayIndexOutOfBoundsException("Invalid index: " + index + " (valid: 0 - " + (this.elementData.length - 1) + ")");
		return this.elementData[index];
	}

	/**
     * Returns the first Element E from ArrayList
     *
     * @returns {E}
     */
	public firstElement() : E {
		if (this.elementData.length === 0)
			throw new NoSuchElementException();
		return this.elementData[0];
	}

	/**
     * Returns the last Element E from ArrayList
     *
     * @returns {E}
     */
	public lastElement() : E {
		if (this.elementData.length === 0)
			throw new NoSuchElementException();
		return this.elementData[this.elementData.length - 1];
	}

	/**
     * Adds an element to the ArrayList in given position
     *
     * @param {E} e The new element
     * @param {number} index position of the new element
     *
     * @returns {void}
     */
	public setElementAt(e : E, index : number) : void {
		if ((index < 0) || (index >= this.elementData.length))
			throw new ArrayIndexOutOfBoundsException(`Invalid index: ${index} (valid: 0 - ${this.elementData.length - 1})`);
		this.elementData[index] = e;
	}

	/**
     * Removes element at given position
     *
     * @param {number} index
     *
     * @returns {void}
     */
	public removeElementAt(index : number) : void {
		if ((index < 0) || (index >= this.elementData.length))
			throw new ArrayIndexOutOfBoundsException(`Invalid index: ${index} (valid: 0 - ${this.elementData.length - 1})`);
		const j : number = this.elementData.length - index;
		if (j > 0)
			for (let i : number = 0; i < j; i++)
				this.elementData[index + i] = this.elementData[index + i + 1];
		this.modCount++;
		this.elementData.length = this.elementData.length -1;
	}

	/**
     * Inserts an element at the given position
     *
     * @param {E} e the element
     * @param {number} index the position
     *
     * @returns {void}
     */
	public insertElementAt(e : E, index : number) : void {
		if ((index < 0) || (index > this.elementData.length))
			throw new ArrayIndexOutOfBoundsException(`Invalid index: ${index} (valid: 0 - ${this.elementData.length})`);
		this.modCount++;
		for (let i : number = 0; i < this.elementData.length - index; i++)
			this.elementData[this.elementData.length - i] = this.elementData[this.elementData.length - i - 1];
		this.elementData[index] = e;
	}

	/**
     * Appends ArrayList with given element
     *
     * @param {E} e the element
     *
     * @returns {void}
     */
	public addElement(e : E) : void {
		this.modCount++;
		this.elementData.push(e);
	}

	/**
     * Removes element from ArrayList
     *
     * @param {E} e the element
     *
     * @returns {boolean} true if element is removed
     */
	public removeElement(e : E) : boolean {
		this.modCount++;
		const i : number = this.indexOf(e);
		if (i >= 0) {
			this.removeElementAt(i);
			return true;
		}
		return false;
	}

	/**
     * Removes all elements from ArrayList but keeps the reference to the internal array
     *
     * @returns {void}
     */
	public removeAllElements() : void {
		this.elementData.length = 0;
		this.modCount++;
	}

	/**
     * Returns a clone of the current ArrayList instance. Just a copy though
     *
     * @returns {ArrayList<E>}
     */
	public clone() : ArrayList<E> {
		return new ArrayList<E>(this);
	}

	/**
     * Either returns a new Array of ArrayList elements or copies elements into given array
     *
     * @param {Array<E>?} array the optional array
     *
     * @returns {Array<E>}
     */
	public toArray() : Array<unknown>;
	public toArray<T>(a : Array<T>) : Array<T>;
	public toArray<T>(__param0? : Array<T>) : Array<T> | Array<unknown> {
		if ((typeof __param0 === "undefined") || (__param0 == null) || (__param0.length < this.size())) {
			return [...this.elementData];
		} else if (Array.isArray(__param0)) {
			const a : Array<T> = __param0;
			if (__param0.length >= this.elementData.length) {
				for (let i = 0; i < this.elementData.length; i++)
					a[i] = this.elementData[i] as unknown as T;
				return a;
			}
			return [...this.elementData];
		} else throw new Error('invalid method overload');

	}

	/**
     * Returns element from index
     * see {@link ArrayList#elementAt}
     *
     * @param {number} index
     *
     * @returns {E}
     */
	public get(index : number) : E {
		if ((index < 0) || (index >= this.elementData.length))
			throw new ArrayIndexOutOfBoundsException(`Invalid index: ${index} (valid: 0 - ${this.elementData.length - 1})`);
		return this.elementData[index];
	}

	/**
     * Sets element at index
     * see {@link ArrayList#setElementAt}
     *
     * @param {number} index
     * @param {E} e
     *
     * @returns {E}
     */
	public set(index : number, e : E) : E {
		if ((index < 0) || (index >= this.elementData.length))
			throw new ArrayIndexOutOfBoundsException(`Invalid index: ${index} (valid: 0 - ${this.elementData.length - 1})`);
		const old : E = this.elementData[index];
		this.elementData[index] = e;
		return old;
	}

	/**
     * Adds element by appending or position
     *
     * @param {E | number} e
     *
     * @returns {boolean | E}
     */
	public add(e : E | null) : boolean;
	public add(index : number, e : E | null) : void;
	public add(param1 : E | null | number, param2? : E | null) : boolean | void {
		if ((param1 === null) || (param2 === null))
			throw new NullPointerException();
		if ((typeof param1 === "number") && (typeof param2 !== "undefined")) {
			this.insertElementAt(param2, param1);
		} else if ((typeof param1 === "number") && (typeof param2 === "undefined")) {
			this.addElement(Number(param1) as unknown as E);
		} else if ((typeof param1 !== "number") && (typeof param2 === "undefined")) {
			this.addElement(param1);
			return true;
		} else throw new Error("invalid method overload");
	}

	/**
     * Removes element either via element
     * For removing the element via index use removeElementAt instead.
     *
     * @param {E | number} e
     *
     * @returns {E | boolean}
     */
	public remove(e : E) : boolean;
	public remove(index : number) : E;
	public remove(param : E | number) : boolean | E {
		// WARNING: Removing the element via index is not available since the ArrayList may contain number values...
		return this.removeElement(param as E);
	}

	/**
     * Removes all elements from ArrayList
     *
     * @returns {void}
     */
	public clear() : void {
		this.removeAllElements();
	}

	/**
     * Checks if incoming Collection is part of the ArrayList
     *
     * @param {Collection<E>} c
     *
     * @returns {boolean}
     */
	public containsAll(c : Collection<E>) : boolean {
		return super.containsAll(c);
	}

	/** add a collection of elements to the ArrayList
     * @param {Collection<E>} c
     * @returns {boolean}
     */
	public addAll(c : Collection<E>) : boolean;

	/**
     * Add a collection of elements to the ArrayList at a given position
     *
     * @param {number} index
     * @param {Collection<E>} c
     *
     * @returns {boolean}
     */
	public addAll(index : number, c : Collection<E>) : boolean;

	public addAll(param1 : number | Collection<E>, param2? : Collection<E>) : boolean {
		if (!((typeof param1 === "number") && (typeof param2 !== "undefined")) &&
            !((typeof param1 !== "number") && (typeof param2 === "undefined")))
			throw new Error("invalid method overload");
		const index = (typeof param1 === "number") ? param1 : this.elementData.length;
		if ((index < 0) || (index > this.elementData.length))
			throw new ArrayIndexOutOfBoundsException(`Invalid index: ${index} (valid: 0 - ${this.elementData.length})`);
		const c = (typeof param1 === "number") ? param2 : param1;
		if (!c)
			return false
		const e : E[] = c.toArray() as E[];
		if (e.length === 0)
			return false;
		this.modCount++;
		const nMove : number = this.elementData.length - index;
		for (let i : number = nMove-1; i >= 0; i--)
			this.elementData[index + i + e.length] = this.elementData[index + i];
		for (let i : number = 0; i < e.length; i++)
			this.elementData[index + i] = e[i];
		return true;
	}


	// TODO public removeIf(filter : Predicate<E>) : boolean;


	protected removeRange(fromIndex : number, toIndex : number) : void {
		this.modCount++;
		const nRemove : number = toIndex - fromIndex;
		const nMove : number = this.elementData.length - toIndex;
		for (let i : number = nMove - 1; i >= 0; i--)
			this.elementData[fromIndex + i] = this.elementData[toIndex + i];
		for (let i : number = 0; i < nRemove; i++)
			delete this.elementData[this.elementData.length - 1];
	}


	/**
     * Adds each Element from ArrayList to Consumer
     *
     * @param {Consumer<E>} action
     */
	public forEach(action : Consumer<E>) : void {
		if (action === null)
			throw new NullPointerException();
		const expectedModCount : number = this.modCount;
		for (let i : number = 0; i < this.elementData.length; i++) {
			action.accept(this.elementData[i]);
			if (this.modCount != expectedModCount)
				throw new ConcurrentModificationException();
		}
	}

	/**
     * Returns whether given string is included in list of class tree
     *
     * @param {string} name
     *
     * @returns {boolean}
     */
	public isTranspiledInstanceOf(name : string): boolean {
		return [
			'java.util.ArrayList',
			'java.util.AbstractCollection',
			'java.util.AbstractList',
			'java.util.List',
			'java.util.Collection',
			'java.util.RandomAccess',
			'java.lang.Cloneable',
			'java.lang.Iterable',
			'java.lang.Object',
			'java.io.Serializable'
		].includes(name);
	}

}



export function cast_java_util_ArrayList<E>(obj : unknown) : ArrayList<E> {
	return obj as ArrayList<E>;
}
