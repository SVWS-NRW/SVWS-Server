import { Class } from './Class';

export interface TranspiledObject {
    getClass<T extends TranspiledObject>() : Class<T>;
    hashCode() : number;
    equals(obj : any) : boolean;

    clone() : unknown;

    toString() : String;

    isTranspiledInstanceOf(name: string) : boolean;
}


export function cast_java_lang_TranspiledObject(obj : unknown) : TranspiledObject {
	return obj as TranspiledObject;
}
