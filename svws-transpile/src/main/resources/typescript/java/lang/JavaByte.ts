import { JavaObject } from './JavaObject';
import { NumberFormatException } from './NumberFormatException';

export class JavaByte extends JavaObject {

    public static MAX_VALUE : number = +0x7f;
    public static MIN_VALUE : number = -0x80;
    public static SIZE : number = 8;
    public static BYTES : number = 1;

    public static parseShort(s : String) : number {
        let a : number = parseInt(s.valueOf(), 10);
        if (Number.isNaN(a) || (a < this.MIN_VALUE) || (a > this.MAX_VALUE))
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
            'java.lang.Byte',
            'java.lang.Number',
            'java.lang.Object',
            'java.lang.Comparable',
            'java.lang.Serializable'
        ].includes(name);
	}

}


export function cast_java_lang_Byte(obj : unknown) : Number {
	return obj as Number;
}
