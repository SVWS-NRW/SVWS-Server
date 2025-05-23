import { JavaObject } from '../../../java/lang/JavaObject';
import { IllegalStateException } from '../../../java/lang/IllegalStateException';
import type { JavaMapEntry } from '../../../java/util/JavaMapEntry';
import { ArrayMapEntry, cast_de_svws_nrw_core_adt_map_ArrayMapEntry } from '../../../core/adt/map/ArrayMapEntry';
import type { JavaIterator } from '../../../java/util/JavaIterator';
import { ArrayMap, cast_de_svws_nrw_core_adt_map_ArrayMap } from '../../../core/adt/map/ArrayMap';
import { Class } from '../../../java/lang/Class';
import { NoSuchElementException } from '../../../java/util/NoSuchElementException';

export class ArrayMapEntrySetIterator<K, V> extends JavaObject implements JavaIterator<JavaMapEntry<K, V>> {

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
		const start : number = (from === null) ? 0 : (from + 1);
		for (let i : number = start; i < this._map.getNumberOfKeys(); i++) {
			if (this._map.getEntryByIndex(i) !== null)
				return i;
		}
		return null;
	}

	public next() : JavaMapEntry<K, V> {
		if (this._next === null)
			throw new NoSuchElementException()
		this._current = this._next;
		this._next = this.getNextIndex(this._current);
		const result : ArrayMapEntry<K, V> | null = this._map.getEntryByIndex(this._current);
		if (result === null)
			throw new NoSuchElementException()
		return result;
	}

	public hasNext() : boolean {
		return this._next !== null;
	}

	public remove() : void {
		if (this._current === null)
			throw new IllegalStateException()
		this._map.remove(this._map.getKeyAt(this._current));
		this._current = null;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.adt.map.ArrayMapEntrySetIterator';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['java.util.Iterator', 'de.svws_nrw.core.adt.map.ArrayMapEntrySetIterator'].includes(name);
	}

	public static class = new Class<ArrayMapEntrySetIterator<any, any>>('de.svws_nrw.core.adt.map.ArrayMapEntrySetIterator');

}

export function cast_de_svws_nrw_core_adt_map_ArrayMapEntrySetIterator<K, V>(obj : unknown) : ArrayMapEntrySetIterator<K, V> {
	return obj as ArrayMapEntrySetIterator<K, V>;
}
