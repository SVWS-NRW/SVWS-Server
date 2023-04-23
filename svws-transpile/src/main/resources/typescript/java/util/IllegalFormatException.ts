import { IllegalArgumentException } from '../lang/IllegalArgumentException';

export class IllegalFormatException extends IllegalArgumentException {

	isTranspiledInstanceOf(name : string): boolean {
		return [
			'java.util.IllegalFormatException',
			'java.lang.IllegalArgumentException',
			'java.lang.RuntimeException',
			'java.lang.Exception',
			'java.lang.Throwable',
			'java.lang.Object',
			'java.lang.Serializable',
		].includes(name);
	}

}


export function cast_java_lang_IllegalFormatException(obj : unknown) : IllegalFormatException {
	return obj as IllegalFormatException;
}
