import { JavaObject } from './JavaObject';
import { NullPointerException } from './NullPointerException';
import { NumberFormatException } from './NumberFormatException';

export class JavaFloat extends JavaObject {

    public static MAX_VALUE : number = Number.MAX_VALUE;
    public static MIN_VALUE : number = Number.MIN_VALUE;
    public static SIZE : number = 32;
    public static BYTES : number = 4;

    public static parseFloat(s : string | null) : number {
        if (s === null)
            throw new NullPointerException();
        let a : number = parseFloat(s);
        if (Number.isNaN(a) || (a < this.MIN_VALUE) || (a > this.MAX_VALUE))
            throw new NumberFormatException();
        return a;
    }

    public static compare(a : number, b : number) {
        return a === b ? 0 : (a < b) ? -1 : 1;
    }

	isTranspiledInstanceOf(name : string): boolean {
		return [
            'java.lang.Float',
            'java.lang.Number',
            'java.lang.Object',
            'java.lang.Comparable',
            'java.lang.Serializable'
        ].includes(name);
	}

}


export function cast_java_lang_Float(obj : unknown) : number | null {
	return obj as number | null;
}
