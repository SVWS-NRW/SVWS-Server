import { Exception } from '../lang/Exception';

export class IOException extends Exception {
    
	isTranspiledInstanceOf(name : string): boolean {
		return [
            'java.io.IOException',
            'java.lang.Exception',
            'java.lang.Throwable',
            'java.lang.Object',
            'java.lang.Serializable',
        ].includes(name);
	}
    
}


export function cast_java_io_IOException(obj : unknown) : IOException {
	return obj as IOException;
}
