import { RuntimeException } from "../../java/lang/RuntimeException";

export class IllegalStateException extends RuntimeException {

	transpilerCanonicalName(): string {
		return 'java.lang.IllegalStateException';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return [
			'java.lang.IllegalStateException',
			'java.lang.RuntimeException',
			'java.lang.Exception',
			'java.lang.Throwable',
			'java.lang.Object',
			'java.lang.Serializable',
		].includes(name);
	}

}


export function cast_java_lang_IllegalStateException(obj : unknown) : IllegalStateException {
	return obj as IllegalStateException;
}
