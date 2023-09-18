import { RuntimeException } from "../../java/lang/RuntimeException";

export class ConcurrentModificationException extends RuntimeException {

	isTranspiledInstanceOf(name : string): boolean {
		return [
			'java.util.ConcurrentModificationException',
			'java.lang.RuntimeException',
			'java.lang.Exception',
			'java.lang.Throwable',
			'java.lang.Object',
			'java.lang.Serializable',
		].includes(name);
	}

}


export function cast_java_util_ConcurrentModificationException(obj : unknown) : ConcurrentModificationException {
	return obj as ConcurrentModificationException;
}
