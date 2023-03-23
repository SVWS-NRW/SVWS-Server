import { Exception } from './Exception';

export class RuntimeException extends Exception {

	isTranspiledInstanceOf(name : string): boolean {
		return [
			'java.lang.RuntimeException',
			'java.lang.Exception',
			'java.lang.Throwable',
			'java.lang.Object',
			'java.lang.Serializable',
		].includes(name);
	}

}


export function cast_java_lang_RuntimeException(obj : unknown) : RuntimeException {
	return obj as RuntimeException;
}
