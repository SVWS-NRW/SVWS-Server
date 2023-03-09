import { JavaIterator } from './JavaIterator';
import { ListIterator } from './ListIterator';
import { Collection } from './Collection';
import { Comparator } from './Comparator';

export interface List<E> extends Collection<E> {

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

    addAll(index : number, c : Collection<E>) : boolean;

    removeAll(c : Collection<any> | null) : boolean;

    retainAll(c : Collection<any> | null) : boolean;

    sort(c : Comparator<E>) : void;

    clear() : void;

    equals(obj : any) : boolean;

    hashCode() : number;


    get(index : number) : E;

    set(index : number, element : E) : E;

    add(index : number, element : E) : void;

    remove(index : number) : E;

    removeElementAt(index : number) : void;

    indexOf(o : any) : number;

    lastIndexOf(o : any) : number;


    listIterator() : ListIterator<E>;

    listIterator(index : number) : ListIterator<E>;

}


export function cast_java_util_List<E>(obj : unknown) : List<E> {
	return obj as List<E>;
}
