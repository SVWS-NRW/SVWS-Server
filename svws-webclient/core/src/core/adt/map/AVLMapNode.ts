import type { JavaMapEntry } from '../../../java/util/JavaMapEntry';
import { cast_java_util_Map_Entry } from '../../../java/util/JavaMapEntry';
import { JavaObject } from '../../../java/lang/JavaObject';
import { UnsupportedOperationException } from '../../../java/lang/UnsupportedOperationException';

export class AVLMapNode<K, V> extends JavaObject implements JavaMapEntry<K, V> {

	/**
	 *  Der Schlüsselwert des Baum-Eintrags.
	 */
	readonly _key : K;

	/**
	 *  Der zum Schlüsselwert zugeordnete Wert des Baum-Eintrags.
	 */
	_val : V;

	/**
	 *  Der Vorgänger-Knoten.
	 */
	_prev : AVLMapNode<K, V> | null = null;

	/**
	 *  Der Nachfolger-Knoten.
	 */
	_next : AVLMapNode<K, V> | null = null;

	/**
	 *  Das linke Kind des Knotens.
	 */
	_childL : AVLMapNode<K, V> | null = null;

	/**
	 *  Das rechte Kind des Knotens.
	 */
	_childR : AVLMapNode<K, V> | null = null;

	/**
	 *  Die Höhe des Teilbaums dieses Knotens.
	 */
	_height : number = 1;

	/**
	 *  Die Summe aller Elemente der Sub-Bäume plus diesem Element.
	 */
	_size : number = 1;


	/**
	 * Erstellt ein neues Blatt des Baumes.
	 *
	 * @param key Der Schlüssel (Key). Der Wert darf nicht NULL sein.
	 * @param val Der Wert (Value), welcher dem Schlüssel (Key) zugeordnet ist. Der Wert darf nicht NULL sein.
	 */
	constructor(key : K, val : V) {
		super();
		this._key = key;
		this._val = val;
	}

	public toString() : string {
		return "[" + this._key + ", " + this._val + "]";
	}

	public equals(o : unknown | null) : boolean {
		if (o === null)
			return false;
		if (!(((o instanceof JavaObject) && ((o as JavaObject).isTranspiledInstanceOf('java.util.Map.Entry')))))
			return false;
		const e : JavaMapEntry<any, any> | null = cast_java_util_Map_Entry(o);
		return JavaObject.equalsTranspiler(this._key, (e.getKey())) && (JavaObject.equalsTranspiler(this._val, (e.getValue())));
	}

	public hashCode() : number {
		return JavaObject.getTranspilerHashCode(this._key) ^ JavaObject.getTranspilerHashCode(this._val);
	}

	public getKey() : K {
		return this._key;
	}

	public getValue() : V {
		return this._val;
	}

	public setValue(value : V) : V {
		throw new UnsupportedOperationException()
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.adt.map.AVLMapNode';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['java.util.Map.Entry', 'de.svws_nrw.core.adt.map.AVLMapNode'].includes(name);
	}

}

export function cast_de_svws_nrw_core_adt_map_AVLMapNode<K, V>(obj : unknown) : AVLMapNode<K, V> {
	return obj as AVLMapNode<K, V>;
}
