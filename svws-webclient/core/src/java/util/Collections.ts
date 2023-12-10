import type { JavaMap } from './JavaMap';
import type { JavaSet } from './JavaSet';
import type { List } from './List';

import { ArrayList } from './ArrayList';
import { JavaObject } from '../../java/lang/JavaObject';


export class Collections extends JavaObject {

	// TODO Create a new list class for the empty list that is immutable
	static EMPTY_LIST : List<unknown> = new ArrayList<unknown>();

	// TODO Create a new list class for the empty list that is immutable
	static EMPTY_MAP : JavaMap<unknown, unknown>;

	// TODO Create a new list class for the empty list that is immutable
	static EMPTY_SET : JavaSet<unknown>;


	static emptyList<T>() : List<T> {
		return Collections.EMPTY_LIST as List<T>;
	}

	public transpilerCanonicalName(): string {
		return 'java.util.Collections';
	}

	public isTranspiledInstanceOf(name : string): boolean {
		return [
			'java.util.Collections',
			'java.lang.Object'
		].includes(name);
	}

}


export function cast_java_util_Collections(obj : unknown) : Collections {
	return obj as Collections;
}
