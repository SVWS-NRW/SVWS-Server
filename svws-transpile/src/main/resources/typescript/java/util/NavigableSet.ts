import { Comparator } from './Comparator';
import { JavaIterator } from './JavaIterator';
import { SortedSet } from './SortedSet';

export interface NavigableSet<E> extends SortedSet<E> {

    comparator() : Comparator<E>;

    subSet(fromElement : E, toElement : E) : SortedSet<E>;
    subSet(fromElement : E, fromInclusive : boolean, toElement : E, toInclusive : boolean) : NavigableSet<E>
    headSet(toElement : E ) : SortedSet<E>;
    headSet(toElement : E, inclusive : boolean) : NavigableSet<E>;
    tailSet(fromElement : E) : SortedSet<E>;
    tailSet(fromElement : E, inclusive : boolean) : NavigableSet<E>

    descendingSet() : NavigableSet<E>;

    iterator() : JavaIterator<E>;
    descendingIterator() : JavaIterator<E>;

    first() : E;
    last() : E;

    ceiling(e : E) : E | null;
    floor(e : E) : E | null;

    higher(e : E) : E | null;
    lower(e : E) : E | null;

    pollFirst() : E | null;
    pollLast() : E | null;
}


export function cast_java_util_NavigableSet<E>(obj : unknown) : NavigableSet<E> {
	return obj as NavigableSet<E>;
}
