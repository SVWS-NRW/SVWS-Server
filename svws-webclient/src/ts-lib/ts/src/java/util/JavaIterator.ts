import { Consumer } from "./function/Consumer"; 

export interface JavaIterator<E> {

    hasNext() : boolean;
    next(): E;

    remove() : void;

    // TODO forEachRemaining(action : Consumer<E>) : void;
    
}


export function cast_java_util_Iterator<E>(obj : unknown) : JavaIterator<E> {
	return obj as JavaIterator<E>;
}
