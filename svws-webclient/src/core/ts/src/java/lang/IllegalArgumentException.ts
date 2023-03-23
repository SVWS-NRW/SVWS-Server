import { RuntimeException } from './RuntimeException';

export class IllegalArgumentException extends RuntimeException {

	isTranspiledInstanceOf(name : string): boolean {
		return [
			'java.lang.IllegalArgumentException',
			'java.lang.RuntimeException',
			'java.lang.Exception',
			'java.lang.Throwable',
			'java.lang.Object',
			'java.lang.Serializable',
		].includes(name);
	}

}


export function cast_java_lang_IllegalArgumentException(obj : unknown) : IllegalArgumentException {
	return obj as IllegalArgumentException;
}
