import { JavaObject } from './JavaObject';
import { NumberFormatException } from './NumberFormatException';

export class JavaDouble extends JavaObject {

    public static MAX_VALUE : number = Number.MAX_VALUE;
    public static MIN_VALUE : number = Number.MIN_VALUE;
    public static SIZE : number = 64;
    public static BYTES : number = 8;

    public static parseDouble(s : String) : number {
        let a : number = parseFloat(s.valueOf());
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
            'java.lang.Double',
            'java.lang.Number',
            'java.lang.Object',
            'java.lang.Comparable',
            'java.lang.Serializable'
        ].includes(name);
	}

}


export function cast_java_lang_Double(obj : unknown) : Number {
	return obj as Number;
}
