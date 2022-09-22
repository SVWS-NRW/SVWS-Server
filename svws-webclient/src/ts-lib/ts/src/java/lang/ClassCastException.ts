import { RuntimeException } from './RuntimeException';

export class ClassCastException extends RuntimeException {

	isTranspiledInstanceOf(name : string): boolean {
		return [
            'java.lang.ClassCastException',
            'java.lang.RuntimeException',
            'java.lang.Exception',
            'java.lang.Throwable',
            'java.lang.Object',
            'java.lang.Serializable',
        ].includes(name);
	}

}

export function cast_java_lang_ClassCastException(obj : unknown) : ClassCastException {
	return obj as ClassCastException;
}
