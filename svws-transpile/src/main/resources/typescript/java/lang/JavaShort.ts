import { JavaObject } from './JavaObject';
import { NullPointerException } from './NullPointerException';
import { NumberFormatException } from './NumberFormatException';

export class JavaShort extends JavaObject {

	public static MAX_VALUE : number = +0x7fff;
	public static MIN_VALUE : number = -0x8000;
	public static SIZE : number = 16;
	public static BYTES : number = 2;

	public static parseShort(s : string | null) : number {
		if (s === null)
			throw new NullPointerException();
		const a : number = parseInt(s, 10);
		if (Number.isNaN(a) || (a < this.MIN_VALUE) || (a > this.MAX_VALUE))
			throw new NumberFormatException();
		return a;
	}

	public static compare(a : number, b : number) {
		return a === b ? 0 : (a < b) ? -1 : 1;
	}

	public static hashCode(value : number) : number {
		return value;
	}

	transpilerCanonicalName(): string {
		return 'java.lang.Short';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return [
			'java.lang.Short',
			'java.lang.Number',
			'java.lang.Object',
			'java.lang.Comparable',
			'java.lang.Serializable'
		].includes(name);
	}

}


export function cast_java_lang_Short(obj : unknown) : number | null {
	return obj as number | null;
}
