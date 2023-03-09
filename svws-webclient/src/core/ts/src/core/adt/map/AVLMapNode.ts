import { JavaMapEntry, cast_java_util_Map_Entry } from '../../../java/util/JavaMapEntry';
import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { UnsupportedOperationException, cast_java_lang_UnsupportedOperationException } from '../../../java/lang/UnsupportedOperationException';

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

	public equals(o : unknown) : boolean {
		if (((o instanceof JavaObject) && (o.isTranspiledInstanceOf('java.util.Map.Entry'))) === false) 
			return false;
		let e : JavaMapEntry<unknown, unknown> | null = cast_java_util_Map_Entry(o);
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

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.adt.map.AVLMapNode', 'java.util.Map.Entry'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_adt_map_AVLMapNode<K, V>(obj : unknown) : AVLMapNode<K, V> {
	return obj as AVLMapNode<K, V>;
}
