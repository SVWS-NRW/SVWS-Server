import { JavaObject } from '../../../java/lang/JavaObject';
import { ConcurrentModificationException } from '../../../java/util/ConcurrentModificationException';
import { LinkedCollection, cast_de_svws_nrw_core_adt_collection_LinkedCollection } from '../../../core/adt/collection/LinkedCollection';
import type { JavaIterator } from '../../../java/util/JavaIterator';
import { NoSuchElementException } from '../../../java/util/NoSuchElementException';
import { LinkedCollectionElement } from '../../../core/adt/collection/LinkedCollectionElement';
import { UnsupportedOperationException } from '../../../java/lang/UnsupportedOperationException';

export class LinkedCollectionIterator<E> extends JavaObject implements JavaIterator<E> {

	/**
	 * Die dem Iterator zugehörige Collection
	 */
	private readonly _collection : LinkedCollection<E>;

	/**
	 * Der Zeiger auf das aktuelle Element
	 */
	private _current : LinkedCollectionElement<E> | null = null;

	/**
	 * Die Anzahl der Modifikationen, die bei der {@link LinkedCollection} zur Zeit des Erzeugen des Iterators
	 *  gemacht wurden. Dieser Wert muss mit dem bei der {@link LinkedCollection} übereinstimmen. Ansonsten
	 *  wird eine {@link ConcurrentModificationException} generiert.
	 */
	private readonly _expModCount : number;


	/**
	 * Erzeugt einen neuen LinkedCollectionIterator. Dabei wird die Referenz auf
	 * die {@link LinkedCollection} übergeben.
	 *
	 * @param collection   die zum Iterator zugehörige {@link LinkedCollection}
	 */
	constructor(collection : LinkedCollection<E>) {
		super();
		this._collection = collection;
		this._expModCount = collection._modCount;
		this._current = collection._head;
	}

	public hasNext() : boolean {
		if (this._collection._modCount !== this._expModCount)
			throw new ConcurrentModificationException()
		return (this._current !== null);
	}

	public next() : E {
		if (this._collection._modCount !== this._expModCount)
			throw new ConcurrentModificationException()
		if (this._current === null)
			throw new NoSuchElementException()
		const result : E = this._current.getValue();
		this._current = this._current.getNext();
		return result;
	}

	public remove() : void {
		throw new UnsupportedOperationException("remove")
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.adt.collection.LinkedCollectionIterator';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['java.util.Iterator', 'de.svws_nrw.core.adt.collection.LinkedCollectionIterator'].includes(name);
	}

}

export function cast_de_svws_nrw_core_adt_collection_LinkedCollectionIterator<E>(obj : unknown) : LinkedCollectionIterator<E> {
	return obj as LinkedCollectionIterator<E>;
}
