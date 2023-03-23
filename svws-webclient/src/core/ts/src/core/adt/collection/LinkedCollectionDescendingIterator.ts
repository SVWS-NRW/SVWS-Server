import { JavaObject } from '../../../java/lang/JavaObject';
import { ConcurrentModificationException } from '../../../java/util/ConcurrentModificationException';
import { LinkedCollection, cast_de_nrw_schule_svws_core_adt_collection_LinkedCollection } from '../../../core/adt/collection/LinkedCollection';
import { JavaIterator } from '../../../java/util/JavaIterator';
import { NoSuchElementException } from '../../../java/util/NoSuchElementException';
import { LinkedCollectionElement } from '../../../core/adt/collection/LinkedCollectionElement';
import { UnsupportedOperationException } from '../../../java/lang/UnsupportedOperationException';

export class LinkedCollectionDescendingIterator<E> extends JavaObject implements JavaIterator<E> {

	/**
	 * Die dem Iterator zugehörige Collection
	 */
	private _collection : LinkedCollection<E>;

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
		this._current = collection._tail;
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
		let result : E = this._current.getValue();
		this._current = this._current.getPrev();
		return result;
	}

	public remove() : void {
		throw new UnsupportedOperationException("remove")
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.adt.collection.LinkedCollectionDescendingIterator', 'java.util.Iterator'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_adt_collection_LinkedCollectionDescendingIterator<E>(obj : unknown) : LinkedCollectionDescendingIterator<E> {
	return obj as LinkedCollectionDescendingIterator<E>;
}
