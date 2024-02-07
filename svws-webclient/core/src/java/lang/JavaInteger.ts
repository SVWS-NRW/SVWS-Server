import { JavaObject } from './JavaObject';
import { NullPointerException } from './NullPointerException';
import { NumberFormatException } from './NumberFormatException';

export class JavaInteger extends JavaObject {

	public static MAX_VALUE : number = +0x7fffffff;
	public static MIN_VALUE : number = -0x80000000;
	public static SIZE : number = 32;
	public static BYTES : number = 4;

	public static parseInt(s : string | null) : number {
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
		return 'java.lang.Integer';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return [
			'java.lang.Integer',
			'java.lang.Number',
			'java.lang.Object',
			'java.lang.Comparable',
			'java.lang.Serializable'
		].includes(name);
	}

}


export function cast_java_lang_Integer(obj : unknown) : number | null {
	return obj as number | null;
}
