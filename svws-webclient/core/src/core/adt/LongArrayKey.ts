import type { Comparable } from '../../java/lang/Comparable';
import { JavaObject } from '../../java/lang/JavaObject';
import { Class } from '../../java/lang/Class';
import { Arrays } from '../../java/util/Arrays';
import { System } from '../../java/lang/System';

export class LongArrayKey extends JavaObject implements Comparable<LongArrayKey> {

	private readonly _keys : Array<number>;

	private readonly _hashcode : number;


	/**
	 * Der Konstruktor kopiert sich die Schlüsselwerte und berechnet direkt den Hashwert.
	 *
	 * @param arrayOfKeys  Das Array der Schlüsselwerte.
	 */
	public constructor(arrayOfKeys : Array<number>);

	/**
	 * Konstruktor für einen Parameter.
	 *
	 * @param v1   Der 1. Parameter
	 */
	public constructor(v1 : number);

	/**
	 * Konstruktor für zwei Parameter.
	 *
	 * @param v1   Der 1. Parameter
	 * @param v2   Der 2. Parameter
	 */
	public constructor(v1 : number, v2 : number);

	/**
	 * Konstruktor für drei Parameter.
	 *
	 * @param v1   Der 1. Parameter
	 * @param v2   Der 2. Parameter
	 * @param v3   Der 3. Parameter
	 */
	public constructor(v1 : number, v2 : number, v3 : number);

	/**
	 * Konstruktor für vier Parameter.
	 *
	 * @param v1   Der 1. Parameter
	 * @param v2   Der 2. Parameter
	 * @param v3   Der 3. Parameter
	 * @param v4   Der 4. Parameter
	 */
	public constructor(v1 : number, v2 : number, v3 : number, v4 : number);

	/**
	 * Konstruktor für vier Parameter.
	 *
	 * @param v1   Der 1. Parameter
	 * @param v2   Der 2. Parameter
	 * @param v3   Der 3. Parameter
	 * @param v4   Der 4. Parameter
	 * @param v5   Der 5. Parameter
	 */
	public constructor(v1 : number, v2 : number, v3 : number, v4 : number, v5 : number);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0 : Array<number> | number, __param1? : number, __param2? : number, __param3? : number, __param4? : number) {
		super();
		if (((__param0 !== undefined) && Array.isArray(__param0)) && (__param1 === undefined) && (__param2 === undefined) && (__param3 === undefined) && (__param4 === undefined)) {
			const arrayOfKeys : Array<number> = __param0 as unknown as Array<number>;
			this._keys = Array(arrayOfKeys.length).fill(0);
			System.arraycopy(arrayOfKeys, 0, this._keys, 0, arrayOfKeys.length);
			this._hashcode = this.calculateHashcode();
		} else if (((__param0 !== undefined) && typeof __param0 === "number") && (__param1 === undefined) && (__param2 === undefined) && (__param3 === undefined) && (__param4 === undefined)) {
			const v1 : number = __param0 as number;
			this._keys = [v1];
			this._hashcode = this.calculateHashcode();
		} else if (((__param0 !== undefined) && typeof __param0 === "number") && ((__param1 !== undefined) && typeof __param1 === "number") && (__param2 === undefined) && (__param3 === undefined) && (__param4 === undefined)) {
			const v1 : number = __param0 as number;
			const v2 : number = __param1 as number;
			this._keys = [v1, v2];
			this._hashcode = this.calculateHashcode();
		} else if (((__param0 !== undefined) && typeof __param0 === "number") && ((__param1 !== undefined) && typeof __param1 === "number") && ((__param2 !== undefined) && typeof __param2 === "number") && (__param3 === undefined) && (__param4 === undefined)) {
			const v1 : number = __param0 as number;
			const v2 : number = __param1 as number;
			const v3 : number = __param2 as number;
			this._keys = [v1, v2, v3];
			this._hashcode = this.calculateHashcode();
		} else if (((__param0 !== undefined) && typeof __param0 === "number") && ((__param1 !== undefined) && typeof __param1 === "number") && ((__param2 !== undefined) && typeof __param2 === "number") && ((__param3 !== undefined) && typeof __param3 === "number") && (__param4 === undefined)) {
			const v1 : number = __param0 as number;
			const v2 : number = __param1 as number;
			const v3 : number = __param2 as number;
			const v4 : number = __param3 as number;
			this._keys = [v1, v2, v3, v4];
			this._hashcode = this.calculateHashcode();
		} else if (((__param0 !== undefined) && typeof __param0 === "number") && ((__param1 !== undefined) && typeof __param1 === "number") && ((__param2 !== undefined) && typeof __param2 === "number") && ((__param3 !== undefined) && typeof __param3 === "number") && ((__param4 !== undefined) && typeof __param4 === "number")) {
			const v1 : number = __param0 as number;
			const v2 : number = __param1 as number;
			const v3 : number = __param2 as number;
			const v4 : number = __param3 as number;
			const v5 : number = __param4 as number;
			this._keys = [v1, v2, v3, v4, v5];
			this._hashcode = this.calculateHashcode();
		} else throw new Error('invalid method overload');
	}

	private calculateHashcode() : number {
		let hashCode : number = 1;
		for (let i : number = 0; i < this._keys.length; i++) {
			const value : number = this._keys[i];
			hashCode = (31 * hashCode) + (value ^ (value >>> 32)) as number;
		}
		return hashCode;
	}

	public toString() : string | null {
		return Arrays.toString(this._keys);
	}

	public equals(obj : unknown | null) : boolean {
		if (obj === null)
			return false;
		if (this as unknown === obj as unknown)
			return true;
		if (!(((obj instanceof JavaObject) && (obj.isTranspiledInstanceOf('de.svws_nrw.core.adt.LongArrayKey')))))
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

	/**
	 * Liefert den Key-Wert an dem Index.
	 *
	 * @param i    der Index
	 *
	 * @return den Key-Wert an dem Index.
	 */
	public getKeyAt(i : number) : number {
		return this._keys[i];
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.adt.LongArrayKey';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['java.lang.Comparable', 'de.svws_nrw.core.adt.LongArrayKey'].includes(name);
	}

	public static class = new Class<LongArrayKey>('de.svws_nrw.core.adt.LongArrayKey');

}

export function cast_de_svws_nrw_core_adt_LongArrayKey(obj : unknown) : LongArrayKey {
	return obj as LongArrayKey;
}
