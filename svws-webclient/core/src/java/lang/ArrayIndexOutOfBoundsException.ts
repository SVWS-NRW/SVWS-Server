import { IndexOutOfBoundsException } from './IndexOutOfBoundsException';

export class ArrayIndexOutOfBoundsException extends IndexOutOfBoundsException {

	public constructor(param? : string | number) {
		super(param === undefined ? "" : (typeof param === "number" ? "Array index out of range: " + param : param));
	}

	transpilerCanonicalName(): string {
		return 'java.lang.ArrayIndexOutOfBoundsException';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return [
			'java.lang.ArrayIndexOutOfBoundsException',
			'java.lang.IndexOutOfBoundsException',
			'java.lang.RuntimeException',
			'java.lang.Exception',
			'java.lang.Throwable',
			'java.lang.Object',
			'java.lang.Serializable',
		].includes(name);
	}

}

export function cast_java_lang_ArrayIndexOutOfBoundsException(obj : unknown) : ArrayIndexOutOfBoundsException {
	return obj as ArrayIndexOutOfBoundsException;
}
