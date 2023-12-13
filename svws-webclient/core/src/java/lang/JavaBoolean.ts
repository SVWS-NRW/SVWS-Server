import { JavaObject } from './JavaObject';

export class JavaBoolean extends JavaObject {

	public static parseBoolean(s : string | null) : boolean {
		return (s === null) ? false : s.localeCompare("true", undefined, { sensitivity: 'accent' }) === 0;
	}

	transpilerCanonicalName(): string {
		return 'java.lang.Boolean';
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


export function cast_java_lang_Boolean(obj : unknown) : boolean {
	return obj as boolean;
}
