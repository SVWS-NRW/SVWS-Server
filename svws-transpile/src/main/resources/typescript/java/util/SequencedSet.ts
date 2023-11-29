import type { JavaSet } from './JavaSet';
import type { SequencedCollection } from './SequencedCollection';

export interface SequencedSet<E> extends SequencedCollection<E>, JavaSet<E> {

    reversed() : SequencedSet<E>;

}


export function cast_java_util_SequencedSet<E>(obj : unknown) : SequencedSet<E> {
	return obj as SequencedSet<E>;
}
