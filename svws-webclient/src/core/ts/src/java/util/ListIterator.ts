import { JavaIterator } from './JavaIterator';

export interface ListIterator<E> extends JavaIterator<E> {

    hasNext() : boolean;
    next(): E;
    nextIndex() : number;

    hasPrevious() : boolean;
    previous() : E;
    previousIndex() : number;

    set(e : E) : void;
    add(e : E) : void;
    remove() : void;

    // TODO forEachRemaining(action : Consumer<E>) : void;

}


export function cast_java_util_ListIterator<E>(obj : unknown) : ListIterator<E> {
	return obj as ListIterator<E>;
}
