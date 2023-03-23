import { JavaMap } from './JavaMap';
import { JavaSet } from './JavaSet';
import { List } from './List';
import { Vector } from './Vector';

import { JavaObject } from '../../java/lang/JavaObject';


export class Collections extends JavaObject {

	// TODO Create a new list class for the empty list that is immutable
	static EMPTY_LIST : List<unknown> = new Vector<unknown>();

	// TODO Create a new list class for the empty list that is immutable
	static EMPTY_MAP : JavaMap<unknown, unknown>;

	// TODO Create a new list class for the empty list that is immutable
	static EMPTY_SET : JavaSet<unknown>;


	static emptyList<T>() : List<T> {
		return Collections.EMPTY_LIST as List<T>;
	}

}


export function cast_java_util_Collections(obj : unknown) : Collections {
	return obj as Collections;
}
