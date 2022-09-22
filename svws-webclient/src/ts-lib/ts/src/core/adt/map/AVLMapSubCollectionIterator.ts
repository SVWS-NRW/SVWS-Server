import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { IllegalStateException, cast_java_lang_IllegalStateException } from '../../../java/lang/IllegalStateException';
import { AVLMapNode, cast_de_nrw_schule_svws_core_adt_map_AVLMapNode } from '../../../core/adt/map/AVLMapNode';
import { JavaIterator, cast_java_util_Iterator } from '../../../java/util/JavaIterator';
import { NoSuchElementException, cast_java_util_NoSuchElementException } from '../../../java/util/NoSuchElementException';
import { AVLMapSubMap, cast_de_nrw_schule_svws_core_adt_map_AVLMapSubMap } from '../../../core/adt/map/AVLMapSubMap';

export class AVLMapSubCollectionIterator<K, V> extends JavaObject implements JavaIterator<V> {

	private readonly _sub : AVLMapSubMap<K, V>;

	private _current : AVLMapNode<K, V> | null = null;

	private _next : AVLMapNode<K, V> | null = null;


	/**
	 * Erstellt einen neuen VALUES-Iterator, welcher auf der {@link AVLMapSubMap} operiert.
	 * 
	 * @param sub Die {@link AVLMapSubMap} auf der dieser Iterator operiert.
	 */
	constructor(sub : AVLMapSubMap<K, V>) {
		super();
		this._sub = sub;
		this._current = null;
		this._next = this._sub.bcGetFirstEntryAsNode();
	}

	public next() : V {
		if (this._next === null) 
			throw new NoSuchElementException()
		this._current = this._next;
		this._next = this._sub.bcGetNextEntryOrNull(this._current);
		return this._current._val;
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

	isTranspiledInstanceOf(name : string): boolean {
		return ['java.util.Iterator', 'de.nrw.schule.svws.core.adt.map.AVLMapSubCollectionIterator'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_adt_map_AVLMapSubCollectionIterator<K, V>(obj : unknown) : AVLMapSubCollectionIterator<K, V> {
	return obj as AVLMapSubCollectionIterator<K, V>;
}
