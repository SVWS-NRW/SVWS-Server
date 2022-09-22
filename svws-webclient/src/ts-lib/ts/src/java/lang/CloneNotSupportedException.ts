import { RuntimeException } from './RuntimeException';

export class CloneNotSupportedException extends RuntimeException {

	isTranspiledInstanceOf(name : string): boolean {
		return [
            'java.lang.CloneNotSupportedException',
            'java.lang.Exception',
            'java.lang.Throwable',
            'java.lang.Object',
            'java.lang.Serializable',
        ].includes(name);
	}

}

export function cast_java_lang_CloneNotSupportedException(obj : unknown) : CloneNotSupportedException {
	return obj as CloneNotSupportedException;
}
