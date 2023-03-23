import { NullPointerException } from './NullPointerException';
import { StringIndexOutOfBoundsException } from './StringIndexOutOfBoundsException';

export interface CharSequence {

    length() : number;

    charAt(index : number) : string;

    isEmpty() : boolean;

    subSequence(start : number, end : number) : CharSequence;

    toString() : string | null;

}

export namespace I {
    function compare(cs1 : CharSequence, cs2 : CharSequence) : number {
    	if ((cs1 === null) || (cs2 === null))
    		throw new NullPointerException();
    	if (cs1 === cs2)
    		return 0;
    	for (let i : number = 0, len = Math.min(cs1.length(), cs2.length()); i < len; i++) {
    		const a : string = cs1.charAt(i);
    		const b : string = cs2.charAt(i);
    		if (a != b) {
    			const cpA : number | undefined = a.codePointAt(0);
    			const cpB : number | undefined = b.codePointAt(0);
    			if (!cpA)
    				throw new StringIndexOutOfBoundsException(0);
    			if (!cpB)
    				throw new StringIndexOutOfBoundsException(0);
    			return cpA - cpB;
    		}
    	}
    	return cs1.length() - cs2.length();
    }
}


export function cast_java_lang_CharSequence(obj : unknown) : CharSequence {
	return obj as CharSequence;
}
