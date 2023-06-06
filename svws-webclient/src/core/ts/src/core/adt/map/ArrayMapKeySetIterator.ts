import { JavaObject } from '../../../java/lang/JavaObject';
import { IllegalStateException } from '../../../java/lang/IllegalStateException';
import type { JavaIterator } from '../../../java/util/JavaIterator';
import { ArrayMap, cast_de_svws_nrw_core_adt_map_ArrayMap } from '../../../core/adt/map/ArrayMap';
import { NoSuchElementException } from '../../../java/util/NoSuchElementException';

export class ArrayMapKeySetIterator<K, V> extends JavaObject implements JavaIterator<K> {

	/**
	 * Die {@link ArrayMap}, deren Schlüsselwerte iteriert werden.
	 */
	private readonly _map : ArrayMap<K, V>;

	/**
	 * Die aktuelle Position in der Map. Der Wert null bedeutet, dass sich der Iterator vor dem
	 *  ersten Element befindet.
	 */
	private _current : number | null = null;

	/**
	 * Die nächste Position in der Map. Der wert null bedeutet, dass kein nächster Eintrag vorhanden ist.
	 */
	private _next : number | null = null;


	/**
	 * Erstellt einen neuen Iterator für die angegebene {@link ArrayMap}
	 *
	 * @param map  die {@link ArrayMap}
	 */
	constructor(map : ArrayMap<K, V>) {
		super();
		this._map = map;
		this._current = null;
		this._next = this.getNextIndex(this._current);
	}

	private getNextIndex(from : number | null) : number | null {
		const start : number = (from === null) ? 0 : from! + 1;
		for (let i : number = start; i < this._map.getNumberOfKeys(); i++) {
			if (this._map.getEntryByIndex(i) !== null)
				return i;
		}
		return null;
	}

	public next() : K {
		if (this._next === null)
			throw new NoSuchElementException()
		this._current = this._next;
		this._next = this.getNextIndex(this._current);
		const key : K | null = this._map.getKeyAt(this._current!);
		if (key === null)
			throw new NoSuchElementException()
		return key;
	}

	public hasNext() : boolean {
		return this._next !== null;
	}

	public remove() : void {
		if (this._current === null)
			throw new IllegalStateException()
		this._map.remove(this._map.getKeyAt(this._current!));
		this._current = null;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['java.util.Iterator', 'de.svws_nrw.core.adt.map.ArrayMapKeySetIterator'].includes(name);
	}

}

export function cast_de_svws_nrw_core_adt_map_ArrayMapKeySetIterator<K, V>(obj : unknown) : ArrayMapKeySetIterator<K, V> {
	return obj as ArrayMapKeySetIterator<K, V>;
}
