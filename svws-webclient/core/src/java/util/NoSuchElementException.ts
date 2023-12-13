import { RuntimeException } from '../../java/lang/RuntimeException';

export class NoSuchElementException extends RuntimeException {

	public transpilerCanonicalName(): string {
		return 'java.util.NoSuchElementException';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return [
			'java.util.NoSuchElementException',
			'java.lang.RuntimeException',
			'java.lang.Exception',
			'java.lang.Throwable',
			'java.lang.Object',
			'java.lang.Serializable',
		].includes(name);
	}

}

export function cast_java_util_NoSuchElementException(obj : unknown) : NoSuchElementException {
	return obj as NoSuchElementException;
}
