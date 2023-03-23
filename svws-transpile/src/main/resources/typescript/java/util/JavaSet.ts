import { JavaIterable } from '../../java/lang/JavaIterable';

import { JavaIterator } from './JavaIterator';
import { Collection } from './Collection';

export interface JavaSet<E> extends JavaIterable<E> {

    size() : number;

    isEmpty() : boolean;

    contains(e : any) : boolean;

    [Symbol.iterator](): Iterator<E>;

    iterator() : JavaIterator<E>;

    toArray() : Array<unknown>;

    toArray<U>(a: Array<U>) : Array<U>;

    add(e : E | null) : boolean;

    remove(e : any) : boolean;


    containsAll(c : Collection<any> | null) : boolean;

    addAll(c : Collection<E> | null) : boolean;

    removeAll(c : Collection<any> | null) : boolean;

    retainAll(c : Collection<any> | null) : boolean;

    clear() : void;

    equals(obj : any) : boolean;

    hashCode() : number;

    // TODO further methods defined in Java...

}


export function cast_java_util_Set<E>(obj : unknown) : JavaSet<E> {
	return obj as JavaSet<E>;
}
