import type { JavaSet } from './JavaSet';
import type { SequencedSet } from './SequencedSet';
import type { Comparator } from './Comparator';

export interface SortedSet<E> extends JavaSet<E>, SequencedSet<E> {

    comparator() : Comparator<E>;

    subSet(fromElement : E, toElement : E) : SortedSet<E>;
    headSet(toElement : E ) : SortedSet<E>;
    tailSet(fromElement : E) : SortedSet<E>;

    first() : E;
    last() : E;

}


export function cast_java_util_SortedSet<E>(obj : unknown) : SortedSet<E> {
	return obj as SortedSet<E>;
}
