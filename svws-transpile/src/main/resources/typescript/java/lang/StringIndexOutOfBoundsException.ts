import  { IndexOutOfBoundsException } from './IndexOutOfBoundsException';

export class StringIndexOutOfBoundsException extends IndexOutOfBoundsException {

	public constructor(param? : string | number) {
		if (typeof param === "undefined")
			super();
		else if (typeof param === "string")
			super(param);
		else
			super("String index invalid: " + param);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return [
			'java.lang.StringIndexOutOfBoundsException',
			'java.lang.IndexOutOfBoundsException',
			'java.lang.RuntimeException',
			'java.lang.Exception',
			'java.lang.Throwable',
			'java.lang.Object',
			'java.lang.Serializable',
		].includes(name);
	}

}


export function cast_java_lang_StringIndexOutOfBoundsException(obj : unknown) : StringIndexOutOfBoundsException {
	return obj as StringIndexOutOfBoundsException;
}
