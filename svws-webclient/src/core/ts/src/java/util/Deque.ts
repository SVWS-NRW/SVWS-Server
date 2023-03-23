import { JavaIterable } from '../../java/lang/JavaIterable';

import { JavaIterator } from './JavaIterator';
import { Collection } from './Collection';
import { Queue } from './Queue';

export interface Deque<E> extends Queue<E> {

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

	addFirst(e : E | null) : void;
    addLast(e : E | null) : void;

    offerFirst(e : E | null) : boolean;
	offerLast(e : E | null) : boolean;

	removeFirst() : E;
    removeLast() : E;

	pollFirst() : E | null;
    pollLast() : E | null;

    getFirst() : E;
    getLast() : E;

    peekFirst() : E | null;
    peekLast() : E | null;

	removeFirstOccurrence(obj : any) : boolean;
	removeLastOccurrence(obj : any) : boolean;

    push(e : E) : void;
    pop() : E;

    descendingIterator() : JavaIterator<E>;

}


export function cast_java_util_Deque<E>(obj : unknown) : Deque<E> {
	return obj as Deque<E>;
}
