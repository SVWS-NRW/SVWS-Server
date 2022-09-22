import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { ConcurrentModificationException, cast_java_util_ConcurrentModificationException } from '../../../java/util/ConcurrentModificationException';
import { MinHeap, cast_de_nrw_schule_svws_core_adt_tree_MinHeap } from '../../../core/adt/tree/MinHeap';
import { JavaIterator, cast_java_util_Iterator } from '../../../java/util/JavaIterator';
import { NoSuchElementException, cast_java_util_NoSuchElementException } from '../../../java/util/NoSuchElementException';
import { UnsupportedOperationException, cast_java_lang_UnsupportedOperationException } from '../../../java/lang/UnsupportedOperationException';

export class MinHeapIterator<T> extends JavaObject implements JavaIterator<T> {

	private readonly _elements : Array<T | null>;

	private _current : number = 0;

	private readonly _heap : MinHeap<T>;

	private readonly _expModCount : number;


	/**
	 * Erstellt einen neuen Iterator für die Klasse MinHeap
	 * 
	 * @param elem   die Elemente des Minimum Heaps 
	 * @param heap   eine Referenz zum Minimum Heap, um auf parallel erfolgende modifizierende Zugriffe reagieren zu können.
	 */
	constructor(elem : Array<T | null>, heap : MinHeap<T>) {
		super();
		this._current = -1;
		this._elements = elem;
		this._heap = heap;
		this._expModCount = heap.getModCount();
	}

	public hasNext() : boolean {
		if (this._heap.getModCount() !== this._expModCount) 
			throw new ConcurrentModificationException()
		return ((this._current + 1) < this._heap.size());
	}

	public next() : T {
		if (!this.hasNext()) 
			throw new NoSuchElementException("Keine weiteren Elemente vorhanden. Eine Prüfung mit hasNext() vorab ist empfehlenswert.")
		let elem : T | null = this._elements[++this._current];
		if (elem === null) 
			throw new NoSuchElementException("Interner Fehler in der Datenstruktur.")
		return elem;
	}

	public remove() : void {
		throw new UnsupportedOperationException("remove")
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['java.util.Iterator', 'de.nrw.schule.svws.core.adt.tree.MinHeapIterator'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_adt_tree_MinHeapIterator<T>(obj : unknown) : MinHeapIterator<T> {
	return obj as MinHeapIterator<T>;
}
