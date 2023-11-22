import type { Collection } from './Collection';

export interface SequencedCollection<E> extends Collection<E> {

    reversed() : SequencedCollection<E>;

}


export function cast_java_util_SequencedCollection<E>(obj : unknown) : SequencedCollection<E> {
	return obj as SequencedCollection<E>;
}
