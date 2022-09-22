import { JavaObject } from './JavaObject';

export class JavaBoolean extends JavaObject {

    public static parseBoolean(s : String) : boolean {
        return (s === null) ? false : s.localeCompare("true", undefined, { sensitivity: 'accent' }) === 0;
    }

	isTranspiledInstanceOf(name : string): boolean {
		return [
            'java.lang.Boolean',
            'java.lang.Object',
            'java.lang.Comparable',
            'java.lang.Serializable'
        ].includes(name);
	}

}


export function cast_java_lang_Boolean(obj : unknown) : Boolean {
	return obj as Boolean;
}
