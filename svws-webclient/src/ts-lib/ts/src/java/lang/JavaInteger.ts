import { JavaObject } from './JavaObject';
import { NumberFormatException } from './NumberFormatException';

export class JavaInteger extends JavaObject {

    public static MAX_VALUE : number = +0x7fffffff;
    public static MIN_VALUE : number = -0x80000000;
    public static SIZE : number = 32;
    public static BYTES : number = 4;

    public static parseInt(s : String) : number {
        let a : number = parseInt(s.valueOf(), 10);
        if ((a === NaN) || (a < this.MIN_VALUE) || (a > this.MAX_VALUE))
            throw new NumberFormatException();
        return a;
    }

    public static compare(a : Number, b : Number) {
        let first : number = (a instanceof Number) ? a.valueOf() : a;
        let second : number = (b instanceof Number) ? b.valueOf() : b;
        return first === second ? 0 : (first < second) ? -1 : 1;
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


export function cast_java_lang_Integer(obj : unknown) : Number {
	return obj as Number;
}
