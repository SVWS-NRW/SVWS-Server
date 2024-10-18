import { JavaObject } from '../../../java/lang/JavaObject';
import { IllegalStateException } from '../../../java/lang/IllegalStateException';
import type { JavaMapEntry } from '../../../java/util/JavaMapEntry';
import type { JavaIterator } from '../../../java/util/JavaIterator';
import { Class } from '../../../java/lang/Class';
import { NoSuchElementException } from '../../../java/util/NoSuchElementException';
import { AVLMapSubMap } from '../../../core/adt/map/AVLMapSubMap';

export class AVLMapSubEntrySetIterator<K, V> extends JavaObject implements JavaIterator<JavaMapEntry<K, V>> {

	/**
	 *  Die {@link AVLMapSubMap} auf der dieser Iterator operiert.
	 */
	private readonly _sub : AVLMapSubMap<K, V>;

	/**
	 *  Der aktuelle Eintrag. Ein NULL-Wert bedeutet, dass das Element bereits entfernt wurde oder der Iterator auf einer
	 *  ungültigen Position ist, beispielsweise vor dem ersten Element.
	 */
	private _current : JavaMapEntry<K, V> | null = null;

	/**
	 *  Der nächste Eintrag.
	 */
	private _next : JavaMapEntry<K, V> | null = null;


	/**
	 * Erstellt einen neuen ENTRY-Iterator für die angegebene {@link AVLMapSubMap} im gültigen {@link AVLMapIntervall}.
	 *
	 * @param sub Die {@link AVLMapSubMap} auf der operiert wird.
	 */
	constructor(sub : AVLMapSubMap<K, V>) {
		super();
		this._sub = sub;
		this._current = null;
		this._next = this._sub.firstEntry();
	}

	public next() : JavaMapEntry<K, V> {
		if (this._next === null)
			throw new NoSuchElementException()
		this._current = this._next;
		this._next = this._sub.higherEntry(this._next.getKey());
		return this._current;
	}

	public hasNext() : boolean {
		return this._next !== null;
	}

	public remove() : void {
		if (this._current === null)
			throw new IllegalStateException()
		this._sub.remove(this._current.getKey());
		this._current = null;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.adt.map.AVLMapSubEntrySetIterator';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['java.util.Iterator', 'de.svws_nrw.core.adt.map.AVLMapSubEntrySetIterator'].includes(name);
	}

	public static class = new Class<AVLMapSubEntrySetIterator<any, any>>('de.svws_nrw.core.adt.map.AVLMapSubEntrySetIterator');

}

export function cast_de_svws_nrw_core_adt_map_AVLMapSubEntrySetIterator<K, V>(obj : unknown) : AVLMapSubEntrySetIterator<K, V> {
	return obj as AVLMapSubEntrySetIterator<K, V>;
}
