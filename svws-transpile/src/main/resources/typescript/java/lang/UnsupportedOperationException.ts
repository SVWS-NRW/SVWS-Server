import { RuntimeException } from "./RuntimeException";

export class UnsupportedOperationException extends RuntimeException {

	public transpilerCanonicalName(): string {
		return 'java.lang.UnsupportedOperationException';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return [
			'java.lang.UnsupportedOperationException',
			'java.lang.RuntimeException',
			'java.lang.Exception',
			'java.lang.Throwable',
			'java.lang.Object',
			'java.lang.Serializable',
		].includes(name);
	}

}


export function cast_java_lang_UnsupportedOperationException(obj : unknown) : UnsupportedOperationException {
	return obj as UnsupportedOperationException;
}
