import { IllegalArgumentException } from './IllegalArgumentException';

export class NumberFormatException extends IllegalArgumentException {

	isTranspiledInstanceOf(name : string): boolean {
		return [
            'java.lang.NumberFormatException',
            'java.lang.IllegalArgumentException',
            'java.lang.RuntimeException',
            'java.lang.Exception',
            'java.lang.Throwable',
            'java.lang.Object',
            'java.lang.Serializable',
        ].includes(name);
	}

}


export function cast_java_lang_NumberFormatException(obj : unknown) : NumberFormatException {
	return obj as NumberFormatException;
}
