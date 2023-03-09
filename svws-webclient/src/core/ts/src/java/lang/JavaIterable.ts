import { JavaIterator } from '../../java/util/JavaIterator';

import { TranspiledObject } from './TranspiledObject';

export interface JavaIterable<T> extends TranspiledObject {

	[Symbol.iterator](): Iterator<T>;

    iterator() : JavaIterator<T>;

}


export function cast_java_lang_Iterable<T>(obj : unknown) : JavaIterable<T> {
	return obj as JavaIterable<T>;
}
