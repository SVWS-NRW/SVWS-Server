import { JavaObject } from '../../../java/lang/JavaObject';
import { IllegalStateException } from '../../../java/lang/IllegalStateException';
import { AVLMapNode } from '../../../core/adt/map/AVLMapNode';
import { JavaIterator } from '../../../java/util/JavaIterator';
import { NoSuchElementException } from '../../../java/util/NoSuchElementException';
import { AVLMapSubMap } from '../../../core/adt/map/AVLMapSubMap';

export class AVLMapSubKeySetIterator<K, V> extends JavaObject implements JavaIterator<K> {

	/**
	 *  Die {@link AVLMap} auf der diese Sup-Map operiert.
	 */
	private readonly _sub : AVLMapSubMap<K, V>;

	/**
	 *  Der aktuelle Eintrag. Ein NULL-Wert bedeutet, dass das Element bereits entfernt wurde oder der Iterator auf
	 *  einer ungültigen Position ist (z.B. vor dem ersten Element).
	 */
	private _current : AVLMapNode<K, V> | null = null;

	/**
	 *  Der nächste Eintrag.
	 */
	private _next : AVLMapNode<K, V> | null = null;


	/**
	 * Erstellt einen neuen KEY-Iterator für die angegebene {@link AVLMapSubMap} im gültigen Bereich
	 * {@link AVLMapIntervall}.
	 *
	 * @param sub Die {@link AVLMapSubMap} auf der operiert wird.
	 */
	constructor(sub : AVLMapSubMap<K, V>) {
		super();
		this._sub = sub;
		this._current = null;
		this._next = this._sub.bcGetFirstEntryAsNode();
	}

	public next() : K {
		if (this._next === null)
			throw new NoSuchElementException()
		this._current = this._next;
		this._next = this._sub.bcGetNextEntryOrNull(this._next);
		return this._current._key;
	}

	public hasNext() : boolean {
		return this._next !== null;
	}

	public remove() : void {
		if (this._current === null)
			throw new IllegalStateException()
		this._sub.remove(this._current._key);
		this._current = null;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.adt.map.AVLMapSubKeySetIterator', 'java.util.Iterator'].includes(name);
	}

}

export function cast_de_svws_nrw_core_adt_map_AVLMapSubKeySetIterator<K, V>(obj : unknown) : AVLMapSubKeySetIterator<K, V> {
	return obj as AVLMapSubKeySetIterator<K, V>;
}
