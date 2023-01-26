import { IndexOutOfBoundsException } from './IndexOutOfBoundsException';
import { JavaObject } from './JavaObject';
import { NullPointerException } from './NullPointerException';

export class System extends JavaObject {

    public static arraycopy(src : Array<unknown> | null, srcPos : number, dest : Array<unknown> | null, destPos : number, length : number) : void {
        if ((src === null) || (dest === null))
            throw new NullPointerException();
        if ((src.length < srcPos + length) || (dest.length < destPos + length)) 
            throw new IndexOutOfBoundsException();
        for (let i : number = 0; i < length; i++) {
            dest[destPos + i] = src[srcPos + i];
        }
    }

    public static currentTimeMillis() : number {
        return Date.now();
    }

    public static lineSeparator() : string {
        return "\n";
    }

	isTranspiledInstanceOf(name : string): boolean {
		return [
            'java.lang.System',
            'java.lang.Object'
        ].includes(name);
	}

}


export function cast_java_lang_System(obj : unknown) : System {
	return obj as System;
}
