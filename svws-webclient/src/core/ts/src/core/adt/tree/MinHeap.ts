import { IllegalStateException } from '../../../java/lang/IllegalStateException';
import { MinHeapIterator } from '../../../core/adt/tree/MinHeapIterator';
import { StringBuilder } from '../../../java/lang/StringBuilder';
import { System } from '../../../java/lang/System';
import { Comparator, cast_java_util_Comparator } from '../../../java/util/Comparator';
import { JavaInteger } from '../../../java/lang/JavaInteger';
import { NullPointerException } from '../../../java/lang/NullPointerException';
import { JavaIterator } from '../../../java/util/JavaIterator';
import { Collection } from '../../../java/util/Collection';
import { Cloneable } from '../../../java/lang/Cloneable';
import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { Arrays } from '../../../java/util/Arrays';
import { Queue } from '../../../java/util/Queue';
import { IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';
import { NoSuchElementException } from '../../../java/util/NoSuchElementException';

export class MinHeap<T> extends JavaObject implements Queue<T>, Cloneable {

	/**
	 * Die Anzahl der Elemente in diesem Heap.
	 */
	private _size : number = 0;

	/**
	 * Dieses Array enthält die Elemente des MinHeap.
	 */
	private _nodes : Array<T | null> = Array(0).fill(null);

	/**
	 * Ein Objekt zum Vergleichen von Werten.
	 */
	private readonly _comparator : Comparator<T>;

	/**
	 * Die initiale Kapazität des Baums
	 */
	private readonly _initialCapacity : number;

	/**
	 * Die Anzahl der Modifikationen, die an dieser Datenstruktur vorgenommen wurden
	 */
	protected _modCount : number = 0;


	/**
	 * Erzeugt einen neuen Minimum-Heap mit dem übergebenen {@link Comparator} und
	 * der übergebenen initialen Kapazität.
	 *
	 * @param comparator      das Objekt zum Vergleich von zwei Objekten des Typ T
	 * @param initialCapacity die initiale Kapazität des Baums
	 */
	public constructor(comparator : Comparator<T>, initialCapacity : number);

	/**
	 * Erzeugt einen neuen Minimum-Heap mit dem übergebenen {@link Comparator} und
	 * einer initialen Kapazität von 63.
	 *
	 * @param comparator das Objekt zum Vergleich von zwei Objekten des Typ T
	 */
	public constructor(comparator : Comparator<T>);

	/**
	 * Erstellt eine Kopie des als Parameter übergebenen Heaps.
	 *
	 * @param original    Das zu kopierende Original
	 */
	public constructor(original : MinHeap<T>);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0 : Comparator<T> | MinHeap<T>, __param1? : number) {
		super();
		if (((typeof __param0 !== "undefined") && ((typeof __param0 !== 'undefined') && (__param0 instanceof Object) && (__param0 !== null) && ('compare' in __param0) && (typeof __param0.compare === 'function')) || (__param0 === null)) && ((typeof __param1 !== "undefined") && typeof __param1 === "number")) {
			let comparator : Comparator<T> = cast_java_util_Comparator(__param0);
			let initialCapacity : number = __param1 as number;
			if (initialCapacity <= 0)
				throw new IllegalArgumentException("Die initiale Kapazität muss größer als 0 sein.")
			this._comparator = comparator;
			this._initialCapacity = initialCapacity;
			this._modCount = 0;
		} else if (((typeof __param0 !== "undefined") && ((typeof __param0 !== 'undefined') && (__param0 instanceof Object) && (__param0 !== null) && ('compare' in __param0) && (typeof __param0.compare === 'function')) || (__param0 === null)) && (typeof __param1 === "undefined")) {
			let comparator : Comparator<T> = cast_java_util_Comparator(__param0);
			this._comparator = comparator;
			this._initialCapacity = 63;
			this._modCount = 0;
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.nrw.schule.svws.core.adt.tree.MinHeap'))) || (__param0 === null)) && (typeof __param1 === "undefined")) {
			let original : MinHeap<T> = cast_de_nrw_schule_svws_core_adt_tree_MinHeap(__param0);
			this._comparator = original._comparator;
			this._initialCapacity = original._initialCapacity;
			this._nodes = Arrays.copyOf(original._nodes, original._nodes.length);
			this._size = original._size;
			this._modCount = original._modCount;
		} else throw new Error('invalid method overload');
	}

	public add(e : T) : boolean {
		if (e === null)
			return false;
		if (this._nodes.length === 0)
			this._nodes = this.newArray(e, this._initialCapacity);
		if (this._size === this._nodes.length)
			this.grow();
		this._nodes[this._size] = e;
		this.heapifyUp(this._size++);
		this._modCount++;
		return true;
	}

	public element() : T {
		if ((this._size === 0) || (this._nodes[0] === null))
			throw new NoSuchElementException()
		return this._nodes[0];
	}

	public offer(e : T) : boolean {
		return this.add(e);
	}

	public peek() : T | null {
		return this._nodes.length === 0 ? null : this._nodes[0];
	}

	public poll() : T | null {
		if (this._size === 0)
			return null;
		let elem : T | null = this._nodes[0];
		this._nodes[0] = this._nodes[--this._size];
		this._nodes[this._size] = null;
		this.heapifyDown(0);
		this._modCount++;
		return elem;
	}

	public remove() : T;

	public remove(o : unknown | null) : boolean;

	/**
	 * Implementation for method overloads of 'remove'
	 */
	public remove(__param0? : null | unknown) : T | boolean {
		if ((typeof __param0 === "undefined")) {
			let result : T | null = this.poll();
			if (result === null)
				throw new NoSuchElementException()
			return result;
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof Object) || ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('java.lang.Object')))) || (__param0 === null))) {
			let o : unknown | null = (__param0 instanceof JavaObject) ? cast_java_lang_Object(__param0) : __param0;
			if (o === null)
				return false;
			let index : number = this.findIndex(o);
			if (index === -1)
				return false;
			this._size--;
			this._modCount++;
			if (index === this._size)
				return true;
			this._nodes[index] = this._nodes[this._size];
			this._nodes[this._size] = null;
			this.heapifyUp(index);
			this.heapifyDown(index);
			return true;
		} else throw new Error('invalid method overload');
	}

	public size() : number {
		return this._size;
	}

	public isEmpty() : boolean {
		return this._size === 0;
	}

	public contains(o : unknown | null) : boolean {
		if (o === null)
			return false;
		for (let i : number = 0; i < this._size; i++) {
			if (JavaObject.equalsTranspiler(this._nodes[i], (o)))
				return true;
		}
		return false;
	}

	public containsAll(c : Collection<unknown> | null) : boolean {
		if (c === null)
			return true;
		if (this as unknown === c as unknown)
			return true;
		for (let o of c)
			if (!this.contains(o))
				return false;
		return true;
	}

	public addAll(c : Collection<T> | null) : boolean {
		if (c === null)
			return false;
		if (this as unknown === c as unknown) {
			if (this._size === 0)
				return false;
			let tmp : Array<T | null> = Arrays.copyOf(this._nodes, this._size);
			for (let t of tmp)
				if (t !== null)
					this.add(t);
			return true;
		}
		let result : boolean = false;
		for (let t of c) {
			if (this.add(t))
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
		for (let o of c) {
			if (this.remove(o)) {
				result = true;
				while (this.remove(o)) {
					// empty block
				}
			}
		}
		return result;
	}

	public retainAll(c : Collection<unknown> | null) : boolean {
		if (this._size === 0)
			return false;
		if (c === null) {
			this.clear();
			return true;
		}
		if (this as unknown === c as unknown)
			return false;
		let tmp : Array<T | null> = this.newArray(this._nodes[0], this._nodes.length);
		if (tmp === null)
			return false;
		let i : number = 0;
		let elem : T | null;
		let changed : boolean = false;
		while ((elem = this.poll()) !== null) {
			if (c.contains(elem))
				tmp[i++] = elem;
			else
				changed = true;
		}
		this._nodes = tmp;
		this._size = i;
		this._modCount++;
		return changed;
	}

	public clear() : void {
		this._nodes = Array(0).fill(null);
		this._size = 0;
		this._modCount++;
	}

	public toArray() : Array<unknown>;

	public toArray<U>(a : Array<U>) : Array<U>;

	/**
	 * Implementation for method overloads of 'toArray'
	 */
	public toArray<U>(__param0? : Array<U>) : Array<U> | Array<unknown> {
		if ((typeof __param0 === "undefined")) {
			return this.copyNodes();
		} else if (((typeof __param0 !== "undefined") && Array.isArray(__param0))) {
			let a : Array<U> = __param0;
			if (a.length < this._size)
				return this.copyNodes();
			System.arraycopy(this._nodes, 0, a, 0, this._size);
			Arrays.fill(a, this._size, a.length, null);
			return a;
		} else throw new Error('invalid method overload');
	}

	public iterator() : JavaIterator<T> {
		return new MinHeapIterator(this._nodes, this);
	}

	public clone() : unknown {
		return new MinHeap(this);
	}

	/**
	 * Gibt den {@link Comparator} des Minimum Heaps zurück.
	 *
	 * @return der Comparator
	 */
	public comparator() : Comparator<T> {
		return this._comparator;
	}

	/**
	 * Gibt die aktuelle Kapazität des Arrays zurück.
	 *
	 * @return die aktuelle Kapazität des Arrays zurück
	 */
	public capacity() : number {
		return (this._nodes.length === 0) ? this._initialCapacity : this._nodes.length;
	}

	/**
	 * Gibt den Inhalt des Minimum Heaps in einem sortierten Array zurück.
	 *
	 * @return ein sortiertes Array mit den Elementen des Minimum Heaps.
	 */
	public toSortedArray() : Array<T> {
		if (this._size === 0)
			return Array(0).fill(null);
		let copy : MinHeap<T> = new MinHeap(this);
		let tmp : Array<T> = this.newArray(this._nodes[0], this._size);
		let current : T | null;
		let i : number = 0;
		while ((current = copy.poll()) !== null)
			tmp[i++] = current;
		return tmp;
	}

	/**
	 * Gibt den Inhalt des Heaps als Array-Repräsentation aus.
	 *
	 * @return der Inhalt des Heaps
	 */
	public toString() : string {
		let sb : StringBuilder = new StringBuilder();
		for (let i : number = 0; i < this._size; i++) {
			sb.append(this._nodes[i]);
			if (i !== this._size - 1)
				sb.append(", ");
		}
		return sb.toString();
	}

	/**
	 * Ermittelt eine Hash-Code für dieses Objekt basierend auf den gespeicherten
	 * Daten im Heap (die konkrete Ordnung des Baumes wird nicht unterschieden).
	 *
	 * @return der Hashcode des Minimum Heaps
	 */
	public hashCode() : number {
		return Arrays.deepHashCode(this.toSortedArray());
	}

	/**
	 * Prüft, ob das übergebene Objekt ein Minimum-Heap ist, der
	 * die gleichen Elemente mit der gleichen Ordnung beinhaltet.
	 *
	 * @param obj   das zu vergleichende Objekt
	 */
	public equals(obj : unknown | null) : boolean {
		if (this as unknown === obj as unknown)
			return true;
		if (obj === null)
			return false;
		if (((obj instanceof JavaObject) && (obj.isTranspiledInstanceOf('de.nrw.schule.svws.core.adt.tree.MinHeap')))) {
			let other : MinHeap<unknown> | null = cast_de_nrw_schule_svws_core_adt_tree_MinHeap(obj);
			return Arrays.deepEquals(this.toSortedArray(), other.toSortedArray());
		}
		return false;
	}

	/**
	 * Liefert zum Index i den Index des Elternteils zurück.
	 *
	 * @param i
	 *
	 * @return den Index des Elternteils
	 */
	private static getParentIndex(i : number) : number {
		return (i <= 0) ? -1 : Math.trunc((i - 1) / 2);
	}

	/**
	 * Liefert zum Index i den Index des linken Kindes zurück.
	 *
	 * @param i
	 *
	 * @return den Index des linken Kindes
	 */
	private static getLeftChildIndex(i : number) : number {
		return 2 * i + 1;
	}

	/**
	 * Liefert zum Index i den Index des rechten Kindes zurück.
	 *
	 * @param i
	 *
	 * @return den Index des rechten Kindes
	 */
	private static getRightChildIndex(i : number) : number {
		return 2 * i + 2;
	}

	/**
	 * Tauscht die Elemente an den Stellen i und j im Array
	 *
	 * @param i
	 * @param j
	 */
	private swap(i : number, j : number) : void {
		let elem : T | null = this._nodes[i];
		this._nodes[i] = this._nodes[j];
		this._nodes[j] = elem;
	}

	/**
	 * Stellt die Minimum Heap Eigenschaft vom Index i aus im Baum abwärts her.
	 *
	 * @param i   ab diesem Index wird im Baum abwärts geprüft.
	 */
	private heapifyDown(i : number) : void {
		let left : number = MinHeap.getLeftChildIndex(i);
		let right : number = MinHeap.getRightChildIndex(i);
		if (left >= this._size)
			return;
		let child : number = right;
		if (right === this._size) {
			child = left;
		} else {
			let nodeLeft : T | null = this._nodes[left];
			let nodeRight : T | null = this._nodes[right];
			if ((nodeLeft === null) || (nodeRight === null))
				return;
			if (this._comparator.compare(nodeLeft, nodeRight) < 0)
				child = left;
		}
		let nodeCurrent : T | null = this._nodes[i];
		let nodeChild : T | null = this._nodes[child];
		if ((nodeCurrent === null) || (nodeChild === null))
			throw new NullPointerException()
		if (this._comparator.compare(nodeCurrent, nodeChild) <= 0)
			return;
		this.swap(i, child);
		this.heapifyDown(child);
	}

	/**
	 * Stellt die Minimum-Heap-Eigenschaft des Arrays ab Position i aufwärts wieder her.
	 *
	 * @param i   ab diesem Index wird überprüft
	 */
	private heapifyUp(i : number) : void {
		let parentIndex : number = MinHeap.getParentIndex(i);
		if (parentIndex < 0)
			return;
		let nodeCurrent : T | null = this._nodes[i];
		let nodeParent : T | null = this._nodes[parentIndex];
		if ((nodeCurrent === null) || (nodeParent === null) || (this._comparator.compare(nodeCurrent, nodeParent) >= 0))
			return;
		this.swap(i, parentIndex);
		this.heapifyUp(parentIndex);
	}

	/**
	 * Erstellt ein neues Array vom Typ T mit der angegebenen Länge.
	 *
	 * @param elem     Ein Element vom Typ T, welches als Vorlage für die Elemente des Arrays dient
	 * @param length   die Länge des neuen Arrays
	 *
	 * @return das neue Array
	 */
	private newArray(elem : T | null, length : number) : Array<T> {
		if (elem === null)
			return Array(length).fill(null);
		return Array(length).fill(null);
	}

	/**
	 * Erzeugt eine Kopie des internen Arrays _nodes.
	 *
	 * @return die Kopie des _nodes-Array.
	 */
	private copyNodes() : Array<T> {
		let result : Array<T> = this.newArray(this._size <= 0 ? null : this._nodes[0], this._size);
		System.arraycopy(this._nodes, 0, result, 0, this._size);
		return result;
	}

	/**
	 * Lässt den dem Baum zu Grunde liegenden Baum wachsen. Verdoppelt die Menge der Elemente, die im Heap
	 * gespeichert werden können.
	 *
	 * Falls der Heap durch das Wachsen auf mehr als {@link Integer.MAX_VALUE} Elemente ansteigen würde,
	 * wird eine IllegalStateException geworfen.
	 *
	 * @throws IllegalStateException
	 */
	private grow() : void {
		if (this._nodes.length === JavaInteger.MAX_VALUE)
			throw new IllegalStateException("Der Minimum-Heap kann nicht mehr als " + JavaInteger.MAX_VALUE + " Elemente beinhalten.")
		let newLength : number = this._nodes.length * 2 + 1;
		if (newLength < 0)
			newLength = JavaInteger.MAX_VALUE;
		let tmp : Array<T> = this.newArray(this._nodes[0], newLength);
		System.arraycopy(this._nodes, 0, tmp, 0, this._size);
		this._nodes = tmp;
	}

	/**
	 * Findet den Index an dem das Element t im dem dem Heap zu Grunde liegendem Array gespeichert ist.
	 * Gibt -1 zurück, falls das Element nicht vorhanden ist.
	 *
	 * @param t   zu diesem Element soll der Index gefunden werden
	 *
	 * @return  der Index, falls das Element enthalten ist, ansonsten -1
	 */
	private findIndex(obj : unknown | null) : number {
		if (obj === null)
			return -1;
		for (let i : number = 0; i < this._size; i++) {
			if (JavaObject.equalsTranspiler(this._nodes[i], (obj)))
				return i;
		}
		return -1;
	}

	/**
	 * Gibt die Anzahl der Operationen zurück, die diese Datenstruktur
	 * verändert haben.
	 *
	 * @return die Anzahl der Operationen
	 */
	getModCount() : number {
		return this._modCount;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['java.lang.Cloneable', 'de.nrw.schule.svws.core.adt.tree.MinHeap', 'java.util.Collection', 'java.util.Queue', 'java.lang.Iterable'].includes(name);
	}

	public [Symbol.iterator](): Iterator<T> {
		const iter : JavaIterator<T> = this.iterator();
		const result : Iterator<T> = {
			next() : IteratorResult<T> {
				if (iter.hasNext())
					return { value : iter.next(), done : false };
				return { value : null, done : true };
			}
		};
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_adt_tree_MinHeap<T>(obj : unknown) : MinHeap<T> {
	return obj as MinHeap<T>;
}
