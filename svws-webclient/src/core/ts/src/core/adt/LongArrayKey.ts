import type { Comparable } from '../../java/lang/Comparable';
import { JavaObject } from '../../java/lang/JavaObject';

export class LongArrayKey extends JavaObject implements Comparable<LongArrayKey> {

	private readonly _keys : Array<number>;

	private readonly _hashcode : number;


	/**
	 * Der Konstruktor kopiert sich die Schlüsselwerte und berechnet direkt den Hashwert.
	 *
	 * @param arrayOfKeys  Das Array der Schlüsselwerte.
	 */
	public constructor(arrayOfKeys : Array<number>) {
		super();
		this._keys = Array(arrayOfKeys.length).fill(0);
		let hashCode : number = 1;
		for (let i : number = 0; i < arrayOfKeys.length; i++) {
			const value : number = arrayOfKeys[i];
			this._keys[i] = value;
			hashCode = 31 * hashCode + (value ^ (value >>> 32)) as number;
		}
		this._hashcode = hashCode;
	}

	public equals(obj : unknown | null) : boolean {
		if (obj === null)
			return false;
		if (this as unknown === obj as unknown)
			return true;
		if (!(((obj instanceof JavaObject) && ((obj as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.adt.LongArrayKey')))))
			return false;
		const keys2 : Array<number> = (cast_de_svws_nrw_core_adt_LongArrayKey(obj))._keys;
		if (this._keys.length !== keys2.length)
			return false;
		for (let i : number = 0; i < this._keys.length; i++)
			if (this._keys[i] !== keys2[i])
				return false;
		return true;
	}

	public hashCode() : number {
		return this._hashcode;
	}

	public compareTo(o : LongArrayKey) : number {
		const keys2 : Array<number> = o._keys;
		if (this._keys.length < keys2.length)
			return -1;
		if (this._keys.length > keys2.length)
			return +1;
		for (let i : number = 0; i < this._keys.length; i++) {
			if (this._keys[i] < keys2[i])
				return -1;
			if (this._keys[i] > keys2[i])
				return +1;
		}
		return 0;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['java.lang.Comparable', 'de.svws_nrw.core.adt.LongArrayKey'].includes(name);
	}

}

export function cast_de_svws_nrw_core_adt_LongArrayKey(obj : unknown) : LongArrayKey {
	return obj as LongArrayKey;
}
