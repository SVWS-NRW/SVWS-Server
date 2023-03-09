import { JavaIterable } from '../lang/JavaIterable';
import { JavaIterator } from './JavaIterator';

export interface Collection<E> extends JavaIterable<E> {

    size() : number;

    isEmpty() : boolean;

    contains(o : any) : boolean;

	[Symbol.iterator](): Iterator<E>;

    iterator() : JavaIterator<E>;

    toArray() : Array<unknown>;

    toArray<U>(a: Array<U>) : Array<U>;

    add(e : E | null) : boolean;

    remove(o : any) : boolean;


    containsAll(c : Collection<any> | null) : boolean;

    addAll(c : Collection<E> | null) : boolean;

    removeAll(c : Collection<any> | null) : boolean;

    retainAll(c : Collection<any> | null) : boolean;

    clear() : void;

    equals(obj : any) : boolean;

    hashCode() : number;
    
}


export function cast_java_util_Collection<E>(obj : unknown) : Collection<E> {
	return obj as Collection<E>;
}
