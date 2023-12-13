import type { JavaMapEntry } from '../../../java/util/JavaMapEntry';
import { cast_java_util_Map_Entry } from '../../../java/util/JavaMapEntry';
import { JavaObject } from '../../../java/lang/JavaObject';
import { UnsupportedOperationException } from '../../../java/lang/UnsupportedOperationException';

export class ArrayMapEntry<K, V> extends JavaObject implements JavaMapEntry<K, V> {

	/**
	 * Der Schlüsselwert.
	 */
	readonly _key : K;

	/**
	 * Der zugeordnete Wert.
	 */
	readonly _val : V;


	/**
	 * Erstellt einen neuen Eintrag.
	 *
	 * @param key   Der Schlüsselwert. Dieser darf nicht NULL sein.
	 * @param val   Der zugeordnete Wert. Dieser darf nicht NULL sein.
	 */
	constructor(key : K, val : V) {
		super();
		this._key = key;
		this._val = val;
	}

	public toString() : string {
		return "(" + this._key + ", " + this._val + ")";
	}

	public equals(o : unknown) : boolean {
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
		return 'de.svws_nrw.core.adt.map.ArrayMapEntry';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.adt.map.ArrayMapEntry', 'java.util.Map.Entry'].includes(name);
	}

}

export function cast_de_svws_nrw_core_adt_map_ArrayMapEntry<K, V>(obj : unknown) : ArrayMapEntry<K, V> {
	return obj as ArrayMapEntry<K, V>;
}
