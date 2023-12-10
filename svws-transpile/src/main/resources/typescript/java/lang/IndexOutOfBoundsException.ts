import { RuntimeException } from "../../java/lang/RuntimeException";

export class IndexOutOfBoundsException extends RuntimeException {

	transpilerCanonicalName(): string {
		return 'java.lang.IndexOutOfBoundsException';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return [
			'java.lang.IndexOutOfBoundsException',
			'java.lang.RuntimeException',
			'java.lang.Exception',
			'java.lang.Throwable',
			'java.lang.Object',
			'java.lang.Serializable',
		].includes(name);
	}

}


export function cast_java_lang_IndexOutOfBoundsException(obj : unknown) : IndexOutOfBoundsException {
	return obj as IndexOutOfBoundsException;
}
