import { Throwable } from './Throwable';

export class Exception extends Throwable {

	transpilerCanonicalName(): string {
		return 'java.lang.Exception';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return [
			'java.lang.Exception',
			'java.lang.Throwable',
			'java.lang.Object',
			'java.lang.Serializable',
		].includes(name);
	}

}

export function cast_java_lang_Exception(obj : unknown) : Exception {
	return obj as Exception;
}
