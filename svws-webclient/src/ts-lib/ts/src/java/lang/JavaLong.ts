import { JavaObject } from './JavaObject';
import { NullPointerException } from './NullPointerException';
import { NumberFormatException } from './NumberFormatException';

export class JavaLong extends JavaObject {

    public static MAX_VALUE : number = +0x7fffffffffffffff;
    public static MIN_VALUE : number = -0x8000000000000000;
    public static SIZE : number = 64;
    public static BYTES : number = 8;

    public static parseLong(s : string | null) : number {
        if (s === null)
            throw new NullPointerException();
        let a : number = parseInt(s, 10);
        if (Number.isNaN(a) || (a < this.MIN_VALUE) || (a > this.MAX_VALUE))
            throw new NumberFormatException();
        return a;
    }

    public static compare(a : number, b : number) {
        return a === b ? 0 : (a < b) ? -1 : 1;
    }

	isTranspiledInstanceOf(name : string): boolean {
		return [
            'java.lang.Long',
            'java.lang.Number',
            'java.lang.Object',
            'java.lang.Comparable',
            'java.lang.Serializable'
        ].includes(name);
	}

}


export function cast_java_lang_Long(obj : unknown) : number | null {
	return obj as number | null;
}
