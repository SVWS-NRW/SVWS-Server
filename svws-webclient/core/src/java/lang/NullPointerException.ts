import { RuntimeException } from './RuntimeException';

export class NullPointerException extends RuntimeException {

	isTranspiledInstanceOf(name : string): boolean {
		return [
			'java.lang.NullPointerException',
			'java.lang.RuntimeException',
			'java.lang.Exception',
			'java.lang.Throwable',
			'java.lang.Object',
			'java.lang.Serializable',
		].includes(name);
	}

}


export function cast_java_lang_NullPointerException(obj : unknown) : NullPointerException {
	return obj as NullPointerException;
}

