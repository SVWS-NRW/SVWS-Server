import { JavaIterable } from '../../java/lang/JavaIterable';

import { JavaIterator } from './JavaIterator';
import { Collection } from './Collection';

export interface Queue<E> extends Collection<E> {

    size() : number;

    isEmpty() : boolean;

    contains(o : any) : boolean;

	[Symbol.iterator](): Iterator<E>;

    iterator() : JavaIterator<E>;

    toArray() : Array<unknown>;

    toArray<U>(a: Array<U>) : Array<U>;

    add(e : E | null) : boolean;

    remove(o : any) : boolean;

    remove() : E;

    containsAll(c : Collection<any> | null) : boolean;

    addAll(c : Collection<E> | null) : boolean;

    removeAll(c : Collection<any> | null) : boolean;

    retainAll(c : Collection<any> | null) : boolean;

    clear() : void;

    equals(obj : any) : boolean;

    hashCode() : number;


    element() : E;

    offer(e : E | null) : boolean;

    peek() : E | null;

    poll() : E | null;
}


export function cast_java_util_Queue<E>(obj : unknown) : Queue<E> {
	return obj as Queue<E>;
}
