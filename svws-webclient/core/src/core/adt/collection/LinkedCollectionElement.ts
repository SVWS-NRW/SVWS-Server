import { JavaObject } from '../../../java/lang/JavaObject';

export class LinkedCollectionElement<E> extends JavaObject {

	/**
	 * Der Wert, der in diesem Element gespeichert wird.
	 */
	private _value : E;

	/**
	 * Referenz auf das vorige Element.
	 */
	private _prev : LinkedCollectionElement<E> | null = null;

	/**
	 * Referenz auf das nachfolgende Element.
	 */
	private _next : LinkedCollectionElement<E> | null = null;


	/**
	 * Erstellt eine neues LinkedCollectionElement mit den Wert _value und den
	 * übergebenen Vorgänger bzw. Nachfolger
	 *
	 * @param value   der Wert des SimpleCollectionElements
	 * @param prev    der Vorgänger
	 * @param next    der Nachfolger
	 */
	constructor(value : E, prev : LinkedCollectionElement<E> | null, next : LinkedCollectionElement<E> | null) {
		super();
		this._value = value;
		this._prev = prev;
		this._next = next;
	}

	/**
	 * Gibt den Wert des Elements zurück.
	 *
	 * @return der Wert des Elements
	 */
	getValue() : E {
		return this._value;
	}

	/**
	 * Ersetzt den Wert des Elements.
	 *
	 * @param value   der neue Wert des Elements
	 *
	 * @return der alte Wert des Elements
	 */
	setValue(value : E) : E {
		const oldValue : E = this._value;
		this._value = value;
		return oldValue;
	}

	/**
	 * Gibt den Vorgänger des Elementes zurück.
	 *
	 * @return   das LinkedCollectionElement das der Vorgänger des Elementes ist
	 */
	getPrev() : LinkedCollectionElement<E> | null {
		return this._prev;
	}

	/**
	 * Setzt den Vorgänger des Elementes auf _prev
	 *
	 * @param prev   der Vorgänger des Elements
	 */
	setPrev(prev : LinkedCollectionElement<E> | null) : void {
		this._prev = prev;
	}

	/**
	 * Gibt den Nachfolger des Elementes zurück.
	 *
	 * @return   das LinkedCollectionElement das der Nachfolger des Elementes ist
	 */
	getNext() : LinkedCollectionElement<E> | null {
		return this._next;
	}

	/**
	 * Setzt den Nachfolger des Elementes auf _next
	 *
	 * @param next   der Nachfolger des Elements
	 */
	setNext(next : LinkedCollectionElement<E> | null) : void {
		this._next = next;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.adt.collection.LinkedCollectionElement';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.adt.collection.LinkedCollectionElement'].includes(name);
	}

}

export function cast_de_svws_nrw_core_adt_collection_LinkedCollectionElement<E>(obj : unknown) : LinkedCollectionElement<E> {
	return obj as LinkedCollectionElement<E>;
}
