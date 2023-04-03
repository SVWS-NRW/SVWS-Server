import { StringBuilder } from '../../../java/lang/StringBuilder';
import { IndexOutOfBoundsException } from '../../../java/lang/IndexOutOfBoundsException';
import { Deque } from '../../../java/util/Deque';
import { LinkedCollectionElement } from '../../../core/adt/collection/LinkedCollectionElement';
import { Comparator } from '../../../java/util/Comparator';
import { NullPointerException } from '../../../java/lang/NullPointerException';
import { JavaIterator } from '../../../java/util/JavaIterator';
import { Collection, cast_java_util_Collection } from '../../../java/util/Collection';
import { Cloneable } from '../../../java/lang/Cloneable';
import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { LinkedCollectionDescendingIterator } from '../../../core/adt/collection/LinkedCollectionDescendingIterator';
import { LinkedCollectionIterator } from '../../../core/adt/collection/LinkedCollectionIterator';
import { Arrays } from '../../../java/util/Arrays';
import { NoSuchElementException } from '../../../java/util/NoSuchElementException';

export class LinkedCollection<E> extends JavaObject implements Deque<E>, Cloneable {

	/**
	 * Das erste Element der Collection.
	 */
	_head : LinkedCollectionElement<E> | null = null;

	/**
	 * Das letze Element der Collection.
	 */
	_tail : LinkedCollectionElement<E> | null = null;

	/**
	 * Die Anzahl der Elemente in der Collection.
	 */
	private _size : number = 0;

	/**
	 * Die Anzahl der Modifikationen, die an dieser Datenstruktur vorgenommen wurden
	 */
	_modCount : number = 0;


	/**
	 * Erzeugt eine neue LinkedCollection.
	 */
	public constructor();

	/**
	 * Erzeugt eine neue LinkedCollection als Kopie der übergebenen
	 * LinkedCollection
	 *
	 * @param c   die LinkedCollection, die kopiert wird
	 */
	public constructor(c : LinkedCollection<E> | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : LinkedCollection<E> | null) {
		super();
		if ((typeof __param0 === "undefined")) {
			this._head = null;
			this._tail = null;
			this._size = 0;
			this._modCount = 0;
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.svws_nrw.core.adt.collection.LinkedCollection'))) || (__param0 === null))) {
			const c : LinkedCollection<E> | null = cast_de_svws_nrw_core_adt_collection_LinkedCollection(__param0);
			this._size = 0;
			this._modCount = 0;
			const iter : JavaIterator<E> = c.iterator();
			while (iter.hasNext())
				this.add(iter.next());
			this._modCount = c._modCount;
		} else throw new Error('invalid method overload');
	}

	public size() : number {
		return this._size;
	}

	public isEmpty() : boolean {
		return (this._head === null);
	}

	public contains(obj : unknown | null) : boolean {
		if (this.isEmpty())
			return false;
		const iter : JavaIterator<E> = this.iterator();
		while (iter.hasNext())
			if (JavaObject.equalsTranspiler(iter.next(), (obj)))
				return true;
		return false;
	}

	public iterator() : JavaIterator<E> {
		return new LinkedCollectionIterator(this);
	}

	public toArray() : Array<unknown>;

	public toArray<T>(a : Array<T>) : Array<T>;

	/**
	 * Implementation for method overloads of 'toArray'
	 */
	public toArray<T>(__param0? : Array<T>) : Array<T> | Array<unknown> {
		if ((typeof __param0 === "undefined")) {
			if (this._size === 0)
				return Array(0).fill(null);
			const array : Array<E> = Array(this._size).fill(null);
			const iter : JavaIterator<E> = this.iterator();
			for (let i : number = 0; i < this._size; i++) {
				array[i] = iter.next();
			}
			return array;
		} else if (((typeof __param0 !== "undefined") && Array.isArray(__param0))) {
			const a : Array<T> = __param0;
			if (a.length < this._size)
				return this.toArray();
			const iter : JavaIterator<E> = this.iterator();
			for (let i : number = 0; i < this._size; i++) {
				const e : E = iter.next();
				a[i] = e as unknown as T;
			}
			Arrays.fill(a, this._size, a.length, null);
			return a;
		} else throw new Error('invalid method overload');
	}

	public add(e : E | null) : boolean {
		if (e === null)
			return false;
		const newElem : LinkedCollectionElement<E> = new LinkedCollectionElement(e, null, null);
		if ((this._head === null) || (this._tail === null)) {
			this._head = newElem;
			this._tail = newElem;
			this._size++;
			this._modCount++;
			return true;
		}
		newElem.setPrev(this._tail);
		this._tail.setNext(newElem);
		this._tail = newElem;
		this._size++;
		this._modCount++;
		return true;
	}

	/**
	 * Entfernt das übergebene Element.
	 *
	 * @param elem   das zu entfernende Element
	 *
	 * @return true, falls das Element erfolgreich entfernt wurde, und false, falls null übergeben wurde.
	 */
	private removeElement(elem : LinkedCollectionElement<E> | null) : boolean {
		if (elem === null)
			return false;
		const prev : LinkedCollectionElement<E> | null = elem.getPrev();
		const next : LinkedCollectionElement<E> | null = elem.getNext();
		if (this._size === 1) {
			this._head = null;
			this._tail = null;
		} else
			if (JavaObject.equalsTranspiler(elem, (this._head))) {
				if (next === null)
					throw new NullPointerException()
				this._head = next;
				next.setPrev(null);
			} else
				if (JavaObject.equalsTranspiler(elem, (this._tail))) {
					if (prev === null)
						throw new NullPointerException()
					this._tail = prev;
					prev.setNext(null);
				} else {
					if ((next === null) || (prev === null))
						throw new NullPointerException()
					next.setPrev(prev);
					prev.setNext(next);
				}
		this._size--;
		this._modCount++;
		return true;
	}

	public remove(obj : unknown | null) : boolean;

	public remove() : E;

	/**
	 * Implementation for method overloads of 'remove'
	 */
	public remove(__param0? : null | unknown) : E | boolean {
		if (((typeof __param0 !== "undefined") && ((__param0 instanceof Object) || ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('java.lang.Object')))) || (__param0 === null))) {
			const obj : unknown | null = (__param0 instanceof JavaObject) ? cast_java_lang_Object(__param0) : __param0;
			if (this.isEmpty())
				return false;
			return this.removeElement(this.findFirst(obj));
		} else if ((typeof __param0 === "undefined")) {
			const value : E | null = this.poll();
			if (value === null)
				throw new NoSuchElementException()
			return value;
		} else throw new Error('invalid method overload');
	}

	public containsAll(c : Collection<unknown> | null) : boolean {
		if ((c === null) || (this as unknown === c as unknown))
			return true;
		for (const o of c)
			if (!this.contains(o))
				return false;
		return true;
	}

	public addAll(c : Collection<E> | null) : boolean {
		if ((c === null) || (c.size() === 0))
			return false;
		if (((c instanceof JavaObject) && (c.isTranspiledInstanceOf('de.svws_nrw.core.adt.collection.LinkedCollection')))) {
			const coll : LinkedCollection<E> = cast_de_svws_nrw_core_adt_collection_LinkedCollection(c);
			if ((coll._tail === null) || (coll._head === null))
				throw new NullPointerException()
			const last : LinkedCollectionElement<E> = coll._tail;
			let current : LinkedCollectionElement<E> | null = coll._head;
			this.add(current.getValue());
			while (current as unknown !== last as unknown) {
				current = current.getNext();
				if (current === null)
					throw new NullPointerException()
				this.add(current.getValue());
			}
			return true;
		}
		let result : boolean = false;
		for (const elem of c) {
			if (this.add(elem))
				result = true;
		}
		return result;
	}

	public removeAll(c : Collection<unknown> | null) : boolean {
		if (c === null)
			return false;
		if (this as unknown === c as unknown) {
			if (this.size() === 0)
				return false;
			this.clear();
			return true;
		}
		let result : boolean = false;
		for (const o of c) {
			while (this.remove(o))
				result = true;
		}
		return result;
	}

	public retainAll(c : Collection<unknown> | null) : boolean {
		if ((this as unknown === c as unknown) || (this._head === null))
			return false;
		if (c === null) {
			this.clear();
			return true;
		}
		const iter : JavaIterator<E> = this.iterator();
		const tmp : LinkedCollection<E> = new LinkedCollection();
		while (iter.hasNext()) {
			const elem : E = iter.next();
			if (!c.contains(elem))
				tmp.add(elem);
		}
		if (tmp.isEmpty())
			return false;
		return this.removeAll(tmp);
	}

	public clear() : void {
		this._head = null;
		this._tail = null;
		this._size = 0;
		this._modCount++;
	}

	public hashCode() : number {
		let hashCode : number = 1;
		for (const e of this)
			hashCode = 31 * hashCode + (e === null ? 0 : JavaObject.getTranspilerHashCode(e));
		return hashCode;
	}

	public equals(obj : unknown | null) : boolean {
		if ((obj === null) || (!(((obj instanceof JavaObject) && (obj.isTranspiledInstanceOf('java.util.Collection'))))))
			return false;
		const other : Collection<unknown> = cast_java_util_Collection(obj);
		if (this._size !== other.size())
			return false;
		const iter : JavaIterator<E> = this.iterator();
		const otherIter : JavaIterator<unknown> = other.iterator();
		while (iter.hasNext()) {
			if (!JavaObject.equalsTranspiler(iter.next(), (otherIter.next())))
				return false;
		}
		return true;
	}

	public clone() : unknown {
		return new LinkedCollection(this);
	}

	public toString() : string {
		const sb : StringBuilder = new StringBuilder();
		sb.append("[");
		const iter : JavaIterator<E> = this.iterator();
		while (iter.hasNext()) {
			sb.append(iter.next());
			if (iter.hasNext())
				sb.append(", ");
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Diese Methode ist eine Hilfsmethode für die Methode sort(). Sie mischt die beiden über die prev-Zeiger
	 * verketteten Listen left und right zu einer kombinierten, über die prev-Zeiger verketteten Liste.
	 *
	 * @param comparator   ein {@link Comparator} zum Vergleichen zweier Elemente
	 * @param left         die erste sortierte Liste
	 * @param right        die zweite sortierte Liste
	 *
	 * @return die kombinierte sortierte Liste
	 */
	private merge(comparator : Comparator<E>, left : LinkedCollectionElement<E>, right : LinkedCollectionElement<E>) : LinkedCollectionElement<E> {
		let headTo : LinkedCollectionElement<E> | null;
		let headFrom : LinkedCollectionElement<E> | null;
		if (comparator.compare(left.getValue(), right.getValue()) > 0) {
			headFrom = left;
			headTo = right;
		} else {
			headFrom = right;
			headTo = left;
		}
		let target : LinkedCollectionElement<E> = headTo;
		while (headFrom !== null) {
			const current : LinkedCollectionElement<E> = headFrom;
			headFrom = headFrom.getPrev();
			let targetPrev : LinkedCollectionElement<E> | null = target.getPrev();
			while ((targetPrev !== null) && (comparator.compare(targetPrev.getValue(), current.getValue()) < 0)) {
				target = targetPrev;
				targetPrev = target.getPrev();
			}
			if (targetPrev === null) {
				target.setPrev(current);
				break;
			}
			current.setPrev(targetPrev);
			target.setPrev(current);
		}
		return headTo;
	}

	/**
	 * Sortiert den Inhalte dieser Liste mithilfe des übergebenen {@link Comparator}-Objekts.
	 *
	 * @param comparator   ein {@link Comparator} zum Vergleichen zweier Elemente
	 *
	 * @return true, falls eine Sortierung erfolgreich war
	 */
	public sort(comparator : Comparator<E> | null) : boolean {
		if (comparator === null)
			return false;
		if ((this._size <= 1) || (this._head === null) || (this._tail === null))
			return true;
		this._modCount++;
		for (let current : LinkedCollectionElement<E> | null = this._head; current !== null; current = current.getNext())
			current.setPrev(null);
		while (this._head !== null) {
			const left : LinkedCollectionElement<E> = this._head;
			const right : LinkedCollectionElement<E> | null = left.getNext();
			if (right === null)
				throw new NullPointerException()
			this._head = right.getNext();
			left.setNext(null);
			right.setNext(null);
			const sorted : LinkedCollectionElement<E> = this.merge(comparator, left, right);
			this._tail.setNext(sorted);
			this._tail = sorted;
		}
		this._head = this._tail;
		this._tail.setNext(this._tail.getPrev());
		while (this._tail.getPrev() !== null) {
			this._tail = this._tail.getPrev();
			if (this._tail === null)
				throw new NullPointerException()
			this._tail.setNext(this._tail.getPrev());
		}
		this._head.setPrev(null);
		let current : LinkedCollectionElement<E> = this._head;
		let next : LinkedCollectionElement<E> | null = current.getNext();
		while (next !== null) {
			next.setPrev(current);
			current = next;
			next = current.getNext();
		}
		this._modCount++;
		return true;
	}

	/**
	 * Sucht das Element an der Stelle Index.
	 *
	 * @param index   die Stelle des gesuchten Elements
	 *
	 * @return das Element an der gesuchten Stelle
	 *
	 * @throws IndexOutOfBoundsException   wenn der Index nicht im gültigen Bereich liegt (index >= 0) && (index < size()))
	 */
	private find(index : number) : LinkedCollectionElement<E> {
		if ((index < 0) || (index >= this._size))
			throw new IndexOutOfBoundsException()
		let current : LinkedCollectionElement<E> | null = this._head;
		for (let i : number = 0; (current !== null); i++, current = current.getNext())
			if (i === index)
				return current;
		throw new IndexOutOfBoundsException()
	}

	/**
	 * Sucht ein LinkedCollectionElement in der Collection mit dem Wert obj
	 * und gibt es zurück
	 *
	 * @param obj   der Wert der in der LinkedCollection gesucht werden soll
	 *
	 * @return  ein LinkedCollectionElement<E> falls der Wert in der Collection
	 * 			enthalten ist und das Element dessen , ansonsten null
	 */
	private findFirst(obj : unknown | null) : LinkedCollectionElement<E> | null {
		if (obj === null)
			return null;
		let current : LinkedCollectionElement<E> | null = this._head;
		while (current !== null) {
			if (JavaObject.equalsTranspiler(current.getValue(), (obj)))
				return current;
			current = current.getNext();
		}
		return null;
	}

	/**
	 * Sucht ein LinkedCollectionElement in der Collection mit dem Wert obj
	 * und gibt es zurück
	 *
	 * @param obj   der Wert der in der LinkedCollection gesucht werden soll
	 *
	 * @return  ein LinkedCollectionElement<E> falls der Wert in der Collection
	 * 			enthalten ist und das Element dessen, ansonsten null
	 */
	private findLast(obj : unknown | null) : LinkedCollectionElement<E> | null {
		if (obj === null)
			return null;
		let current : LinkedCollectionElement<E> | null = this._tail;
		while (current !== null) {
			if (JavaObject.equalsTranspiler(current.getValue(), (obj)))
				return current;
			current = current.getPrev();
		}
		return null;
	}

	public offer(e : E | null) : boolean {
		return this.add(e);
	}

	public poll() : E | null {
		if (this._head === null)
			return null;
		const value : E = this._head.getValue();
		this._head = this._head.getNext();
		if (this._head === null)
			this._tail = null;
		else
			this._head.setPrev(null);
		this._size--;
		this._modCount++;
		return value;
	}

	public element() : E {
		if (this._head === null)
			throw new NoSuchElementException()
		return this._head.getValue();
	}

	public peek() : E | null {
		return (this._head === null) ? null : this._head.getValue();
	}

	public addFirst(e : E | null) : void {
		if (e === null)
			throw new NullPointerException()
		const newElem : LinkedCollectionElement<E> = new LinkedCollectionElement(e, null, null);
		if ((this._head === null) || (this._tail === null)) {
			this._head = newElem;
			this._tail = newElem;
		} else {
			newElem.setNext(this._head);
			this._head.setPrev(newElem);
			this._head = newElem;
		}
		this._size++;
		this._modCount++;
	}

	public addLast(e : E | null) : void {
		if (e === null)
			throw new NullPointerException()
		this.add(e);
	}

	public offerFirst(e : E | null) : boolean {
		this.addFirst(e);
		return true;
	}

	public offerLast(e : E | null) : boolean {
		this.addLast(e);
		return true;
	}

	public removeFirst() : E {
		const value : E | null = this.poll();
		if (value === null)
			throw new NoSuchElementException()
		return value;
	}

	public removeLast() : E {
		const value : E | null = this.pollLast();
		if (value === null)
			throw new NoSuchElementException()
		return value;
	}

	public pollFirst() : E | null {
		return this.poll();
	}

	public pollLast() : E | null {
		if (this._tail === null)
			return null;
		const value : E = this._tail.getValue();
		this._tail = this._tail.getPrev();
		if (this._tail === null)
			this._head = null;
		else
			this._tail.setNext(null);
		this._size--;
		this._modCount++;
		return value;
	}

	public getFirst() : E {
		if (this._head === null)
			throw new NoSuchElementException()
		return this._head.getValue();
	}

	public getLast() : E {
		if (this._tail === null)
			throw new NoSuchElementException()
		return this._tail.getValue();
	}

	public peekFirst() : E | null {
		return (this._head === null) ? null : this._head.getValue();
	}

	public peekLast() : E | null {
		return (this._tail === null) ? null : this._tail.getValue();
	}

	public removeFirstOccurrence(obj : unknown | null) : boolean {
		if (this.isEmpty())
			return false;
		return this.removeElement(this.findFirst(obj));
	}

	public removeLastOccurrence(obj : unknown | null) : boolean {
		if (this.isEmpty())
			return false;
		return this.removeElement(this.findLast(obj));
	}

	public push(e : E) : void {
		this.addFirst(e);
	}

	public pop() : E {
		const value : E | null = this.poll();
		if (value === null)
			throw new NoSuchElementException()
		return value;
	}

	public descendingIterator() : JavaIterator<E> {
		return new LinkedCollectionDescendingIterator(this);
	}

	/**
	 * Gibt den Wert an der Stelle index zurück.
	 *
	 * @param index   der Index
	 *
	 * @return der Wert
	 *
	 * @throws IndexOutOfBoundsException   wenn der Index nicht im gültigen Bereich liegt {@code (index >= 0) && (index < size()))}
	 */
	public get(index : number) : E {
		return this.find(index).getValue();
	}

	/**
	 * Ersetzt den Wert an der Stelle index mit dem neuen übergebenen Wert.
	 *
	 * @param index     die Stelle, wo der Wert ersetzt werden soll
	 * @param element   der neue Wert für die Stelle
	 *
	 * @return der alte Wert an der Stelle
	 *
	 * @throws IndexOutOfBoundsException   wenn der Index nicht im gültigen Bereich liegt {@code (index >= 0) && (index < size()))}
	 */
	public set(index : number, element : E) : E {
		return this.find(index).setValue(element);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['java.lang.Cloneable', 'java.util.Collection', 'de.svws_nrw.core.adt.collection.LinkedCollection', 'java.util.Queue', 'java.util.Deque', 'java.lang.Iterable'].includes(name);
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

}

export function cast_de_svws_nrw_core_adt_collection_LinkedCollection<E>(obj : unknown) : LinkedCollection<E> {
	return obj as LinkedCollection<E>;
}
